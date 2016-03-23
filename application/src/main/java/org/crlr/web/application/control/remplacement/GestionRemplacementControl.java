package org.crlr.web.application.control.remplacement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.remplacement.RechercheRemplacementQO;
import org.crlr.dto.application.remplacement.RemplacementDTO;
import org.crlr.dto.securite.AuthentificationQO;
import org.crlr.dto.securite.TypeAuthentification;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.ConfidentialiteService;
import org.crlr.services.RemplacementService;
import org.crlr.utils.DateUtils;
import org.crlr.web.application.control.AbstractPopupControl;
import org.crlr.web.application.control.EnseignantControl;
import org.crlr.web.application.control.MenuControl;
import org.crlr.web.application.control.EnseignantControl.EnseignantListener;
import org.crlr.web.application.form.remplacement.GestionRemplacementForm;
import org.crlr.web.contexte.ContexteApplication;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.joda.time.LocalDate;

import com.google.common.base.Preconditions;

/**
 * Controleur de gestion de la page de gestion des remplacements.
 * @author DURUPT-C
 */
@ManagedBean(name = "gestionRemplacement")
@ViewScoped
public class GestionRemplacementControl 
extends AbstractPopupControl<GestionRemplacementForm> {

    /** Service Remplacement. */
    @ManagedProperty(value = "#{remplacementService}")
    private transient RemplacementService remplacementService;

    @ManagedProperty(value = "#{enseignant}")
    protected transient EnseignantControl enseignantAbsentControl;
    
    @ManagedProperty(value = "#{enseignant2}")
    protected transient EnseignantControl enseignantRemplaceControl;
    
    
    /**
     * Mutateur de remplacementService {@link #remplacementService}.
     * @param remplacementService le remplacementService to set
     */
    public void setRemplacementService(RemplacementService remplacementService) {
        this.remplacementService = remplacementService;
    }

    /** confidentialiteService. */
    @ManagedProperty(value = "#{confidentialiteService}")
    private transient ConfidentialiteService confidentialiteService;

    
    EnseignantAbsentListener enseignantAbsentListener;
    EnseignantRemplacementListener  enseignantRemplacementListener;
    
    //Pour empecher les dépendances circulair.  C'est ajoutSeanceControl qui mettre la date d'effet dans la formulaire
       
    /**
     * Mutateur de confidentialiteService {@link #confidentialiteService}.
     * @param confidentialiteService le confidentialiteService to set
     */
    public void setConfidentialiteService(
            ConfidentialiteService confidentialiteService) {
        this.confidentialiteService = confidentialiteService;
    }

    /**
     * Constructeur par defaut.
     */
    public GestionRemplacementControl() {
        super(new GestionRemplacementForm());
    }
    
    /**
     * Methode onLoad : appellee une fois apres le constructeur de la classe.
     */
    @PostConstruct
    public void onLoad() {
        
        // Charge la liste des enseignants de l'établissement
        form.setListeEnseignant(loadListeEnseignant());
        
        enseignantAbsentControl.getForm().setListeEnseignant(form.getListeEnseignant());
        enseignantRemplaceControl.getForm().setListeEnseignant(form.getListeEnseignant());
        
        // Charge la liste des remplacements visibles pour l'utilisateur connecte
        form.setListeRemplacement(loadListeRemplacement());
        
        enseignantAbsentListener = new EnseignantAbsentListener();
        enseignantRemplacementListener = new EnseignantRemplacementListener();
        
        enseignantAbsentControl.setListener(enseignantAbsentListener);
        enseignantRemplaceControl.setListener(enseignantRemplacementListener);
        
        
    }

    /**
     * Charge la liste des enseignant qui exercent dans l'etablissement.
     * @return une liste d'enseignants.
     */
    private List<EnseignantDTO> loadListeEnseignant() {

        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        final RechercheRemplacementQO rechercheRemplacementQO = new RechercheRemplacementQO();
        rechercheRemplacementQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
        final ResultatDTO<List<EnseignantDTO>> resultat = remplacementService.findListeEnseignant(rechercheRemplacementQO);
        
        if (resultat != null && CollectionUtils.isNotEmpty(resultat.getValeurDTO())) {
            return resultat.getValeurDTO();
        } else {
            return new ArrayList<EnseignantDTO>();
        }
    }

    /**
     * Charge la liste des remplacement visibles pour l'utilisateur connecte.
     * Un enseignant ne voit que ses propres remplacement (en tant qu'absent ou remplacant)
     * Un directeur voit tous les remplacements enregistres sur l'etablissement.
     * @return une liste de remplacements.
     */
    private List<RemplacementDTO> loadListeRemplacement() {

        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        final RechercheRemplacementQO rechercheRemplacementQO = new RechercheRemplacementQO();
        rechercheRemplacementQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
        
        // Pour un enseignant on positionne son id pour restreindre la vue aux remplacements dans 
        // lesquels il intervient (en tant qu'absent ou remplacant).
        if (utilisateurDTO.getProfil() == Profil.ENSEIGNANT) {
            rechercheRemplacementQO.setIdEnseignant(utilisateurDTO.getUserDTO().getIdentifiant());
        }
        
        final ResultatDTO<List<RemplacementDTO>> resultat = remplacementService.findListeRemplacement(rechercheRemplacementQO);
        
        if (resultat != null && CollectionUtils.isNotEmpty(resultat.getValeurDTO())) {
            return resultat.getValeurDTO();
        } else {
            return new ArrayList<RemplacementDTO>();
        }
    }
    
    //-------------------------------------------------------------------------
    //-- Methodes outils appellees en local 
    //-------------------------------------------------------------------------
    /**
     * Recherche dans la liste des enseignants un enseignant par son id.
     * @param idEnseignant : id de l'enseignant recherche.
     * @return EnseignantDTO
     */
    private EnseignantDTO trouverEnseignant(final Integer idEnseignant) {
        for(final EnseignantDTO enseignant : form.getListeEnseignant()) {
            if (enseignant.getId().equals(idEnseignant)) {
                return enseignant;
            }
        }
        return null;
    }
    
    /**
     * Met a jour la date de fin et de debut de remplacement en correspondance 
     * avec la date de fin et de debut de l'annee scolaire.
     */
    private void calculerDateFinRemplacement() {
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        for (final RemplacementDTO remplacement : form.getListeRemplacement()) {
            if (remplacement.getDateFin() == null 
             || DateUtils.lessStrict(utilisateurDTO.getAnneeScolaireDTO().getDateSortie(), remplacement.getDateFin())) {
                remplacement.setDateFin(utilisateurDTO.getAnneeScolaireDTO().getDateSortie());
            }
            if (remplacement.getDateDebut() == null 
                    || DateUtils.lessStrict(remplacement.getDateDebut(), utilisateurDTO.getAnneeScolaireDTO().getDateRentree())) {
                remplacement.setDateDebut(utilisateurDTO.getAnneeScolaireDTO().getDateRentree());
            }
        }
        
    }
    
    //-------------------------------------------------------------------------
    //-- Methodes appelees par les elements du fichier XHTML 
    //-------------------------------------------------------------------------    
    
    /**
     * Sauvegarde les modifications effectuees sur la fenetre. 
     */
    public void sauver() {
        calculerDateFinRemplacement();
        try {
            final ResultatDTO<Integer> resultat = remplacementService.saveListeRemplacement(form.getListeRemplacement());
            log.debug("Sauvegarde des remplacements : {0} modifications", resultat.getValeurDTO());
            if (!resultat.getConteneurMessage().contientMessageBloquant()) {
                recharger();
            }
        } catch (MetierException e) {
            log.debug("{0}", e.getMessage());
        }
    }

    /**
     * Recharge la liste des remplacements. 
     */
    public void recharger() {
        
        // Charge la liste des remplacements visibles pour l'utilisateur connecte
        form.setListeRemplacement(loadListeRemplacement());
    }
    
    /**
     * Supprime le remplacement sélectionné dans la liste.
     */
    public void supprimerRemplacement() {
        final RemplacementDTO remplacement = form.getRemplacementSelected();
        if (remplacement != null) {

            // Si le remplacement vient d'etre ajoute sans sauvegarde, on supprime direct de la liste
            if (remplacement.getEstAjoute()) {
                form.getListeRemplacement().remove(remplacement);
                
            // On marque le remplacement comme supprime / ou on le re-active
            } else {
                remplacement.setEstSupprimee(BooleanUtils.isFalse(remplacement.getEstSupprimee()));
            }
        }
    }
    
    /**
     * Ajoute une ligne pour la saisie d'un remplacement.
     */
    public void ajouterRemplacement() {
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO(); 
        final RemplacementDTO remplacement = new RemplacementDTO();
        remplacement.setDateDebut(DateUtils.getAujourdhui());
        remplacement.setIdEtablissement(utilisateurDTO.getIdEtablissement());
        if (utilisateurDTO.getProfil() == Profil.ENSEIGNANT) {
            final EnseignantDTO enseignantAbsent = trouverEnseignant(utilisateurDTO.getUserDTO().getIdentifiant());
            remplacement.setEnseignantAbsent(enseignantAbsent);
        }
        // Indique qu'on vient d'ajouter cette ligne
        remplacement.setEstAjoute(true);
        
        // Ajoute le nouvel element dans la liste
        form.getListeRemplacement().add(remplacement);
    }
    
    /**
     * Methode invoquee par un enseignant remplacant pour prendre le role 
     * de l'enseignant absent. Il simule l'enseignant absent. 
     */
    public void simulerEnseignant() {
        final RemplacementDTO remplacement = form.getRemplacementSelected(); 
        if (remplacement == null) {
            return;
        }
        
        final String uid = remplacement.getEnseignantAbsent().getUid();

        // Construit les critere d'auth
        final AuthentificationQO criteres = new AuthentificationQO();
        final TypeAuthentification typeAuthentification = TypeAuthentification.CAS;
        criteres.setIdentifiant(uid);
        criteres.setTypeAuthentification(typeAuthentification);
        final ContexteApplication contexteApplication = ContexteUtils.getContexteApplication();
        criteres.setEnvironnement(contexteApplication.getEnvironnement());
        criteres.setMapProfil(contexteApplication.getMapProfil());
        criteres.setGroupsADMCentral(contexteApplication.getGroupsADMCentral());
        criteres.setRegexpAdmLocal(contexteApplication.getRegexpAdmLocal());
        
        
        ResultatDTO<UtilisateurDTO> resultat;
        try {
            resultat = confidentialiteService.initialisationAuthentification(criteres);
            
            final UtilisateurDTO utilisateurDTO = resultat.getValeurDTO();
            final ContexteUtilisateur contextUtilisateur = ContexteUtils.getContexteUtilisateur();
            
            // Si on n'est pas en train de simuler un eleve, on stock les info de l'enseignant remplacant.
            if (contextUtilisateur.getUtilisateurDTOOrigine() == null) {
                contextUtilisateur.setUtilisateurDTOOrigine(contextUtilisateur.getUtilisateurDTO());
            } 
            
           // Positionne dans le contexte l'utilisateurDTO de l'enseignant absent.
            contextUtilisateur.setUtilisateurDTO(utilisateurDTO);
            
            utilisateurDTO.setPeriodeRemplacement(remplacement);
            
            //réinitialise le menu.  Si l'enseignant remplaçant n'a pas la même configuration par rapport aux séquences automatique / simplifiées
            final MenuControl menuControl = ContexteUtils.getMenuControl();           
            menuControl.init();
            
            log.debug("Changement vers un enseignant absent : {}", uid);
            
        } catch (MetierException e) {
            log.error("Une erreur est survenue lors du passage en remplacement de l'enseignant : " + uid);
        }
    
    }
    
    /**
     * Methode invoquee par un enseignant qui en mode simulation remplacement d'un
     * enseignant absent. Repasse en mode normal : met fin à la simulation de l'enseignant. 
     */
    public void arreterSimulerEnseignant() {
        final ContexteUtilisateur contextUtilisateur = ContexteUtils.getContexteUtilisateur();
        if (contextUtilisateur.getUtilisateurDTOOrigine() != null) {
            contextUtilisateur.setUtilisateurDTO(contextUtilisateur.getUtilisateurDTOOrigine());
            contextUtilisateur.setUtilisateurDTOOrigine(null);
        }
    }
    
    /**
     * Appelee pour la refresh de la liste des remplacements.
     */
    public void refreshListing() {
        
    }
    
    /**
     * Appelee si on clique sur l'icone d'ouverture de la popup de sélection d'un enseignant remplaçant.
     * Par : l'enseignant absent ou par le directeur dans la colonne enseignant remplacant.
     */
    public void selectionnerEnseignantRemplacant() {
        form.setVraiOuFauxChoixAbsent(false);
    }

    /**
     * Appelee si on clique sur l'icone d'ouverture de la popup de sélection d'un enseignant absent.
     * Par : le directeur dans la colonne enseignant absent.
     */
    public void selectionnerEnseignantAbsent() {
        form.setVraiOuFauxChoixAbsent(true);
    }
    
    /**
     * Appelee par la popup de selection d'un enseignant. 
     * Met a jour l'enseignant selectionne dans le remplacement selected pour l'enseignant absent ou remplacant
     * selon le type en cours.
     */
    public void selectionnerEnseignant() {
        
    }
    
    /**
     * Appelee suite a la modification d'une date sur un remplacement existant. 
     * Positionne l'indication que le remplacement a ete modifie.
     */
    public void refreshDate() {
        final RemplacementDTO remplacement = form.getRemplacementSelected();
        if (remplacement != null) {
            remplacement.setEstModifiee(true);
        }
    }
    
    //---------------------------------------------------------------------------------------
    //-- Methodes appelees par d'autres controleurs pour verifier les droits de remplacements 
    //---------------------------------------------------------------------------------------
    
    /**
     * Verifie si l'utilisateur est habilité à faire le remplacement sur la date de la seance en cours
     * d'édition dans le controleur ajoutSeance.
     * @return true si l'enseignant est en cours de remplacement de l'enseignant courant à la date spécifiée
     */
    public Boolean getVraiOuFauxHabiliteSeanceEnCours() {
        final ContexteUtilisateur contextUtilisateur = ContexteUtils.getContexteUtilisateur();
        
        // Si on est pas en phase de remplacement, ras : l'enseigant en cours n'est pas en simulation 
        if (contextUtilisateur.getUtilisateurDTOOrigine() == null) {
            return true;
        }
        
        // Recupere la date d'effet dans le controleur ajoutSeance
        Date dateEffet = form.getDateEffet();
        
        if (dateEffet == null) {
            dateEffet = DateUtils.getAujourdhui();
            log.debug("Utilise la date d'effet d'aujourd'hui {}", dateEffet);
        } else {
            log.debug("Utilise la date d'effet {}", dateEffet);
        }
        
        /*
        if (ajoutSeanceControl.getForm().getSeance() != null && ajoutSeanceControl.getForm().getSeance().getDate() != null) {
            //TODO marche-t-il avec des modifications de séances aussi ?
            dateEffet = ajoutSeanceControl.getForm().getSeance().getDate();
        }*/
        
        dateEffet = org.apache.commons.lang.time.DateUtils.truncate(dateEffet, Calendar.DATE);
        
        return getVraiOuFauxHabiliteSeanceEnCours(dateEffet);
    }
    
    /**
     * Verifie si l'utilisateur est habilité à faire le remplacement sur la date de la seance en cours
     * d'édition dans le controleur ajoutSeance.
     * @param dateEffet la date à utiliser
     * @return true si l'enseignant est en cours de remplacement de l'enseignant courant à la date spécifiée
     * vrai si la séance devrait être modifiable
     */
    public Boolean getVraiOuFauxHabiliteSeanceEnCours(Date dateEffet) {
        
        Preconditions.checkNotNull(dateEffet);
         
        final ContexteUtilisateur contextUtilisateur = ContexteUtils.getContexteUtilisateur();
        
        // Si on est pas en phase de remplacement, ras : l'enseigant en cours n'est pas en simulation 
        if (contextUtilisateur.getUtilisateurDTOOrigine() == null) {
            log.debug("vraiOuFauxHabiliteSeanceEnCours date : {} pas en mode remplaçaint, renvoie true", dateEffet);
            return true;
        }
        
        // Recupere la date d'effet dans le controleur ajoutSeance
        LocalDate dt = new LocalDate(dateEffet);
        
       // dateEffet = org.apache.commons.lang.time.DateUtils.truncate(dateEffet, Calendar.DATE);
        
        // Verifie si un des remplacements correspond avec la date 
        for (final RemplacementDTO remplacement : form.getListeRemplacement()) {
            if (remplacement.getEnseignantAbsent() != null && remplacement.getEnseignantRemplacant() != null &&
                remplacement.getEnseignantAbsent().getUid().equals(contextUtilisateur.getUtilisateurDTO().getUserDTO().getUid()) &&
                remplacement.getEnseignantRemplacant().getUid().equals(contextUtilisateur.getUtilisateurDTOOrigine().getUserDTO().getUid()) &&
                // DateTimeComparator.getDateOnlyInstance().compare(dt, second))
                DateUtils.isBetween(dt.toDate(), remplacement.getDateDebut(), remplacement.getDateFin())) 
            {
                log.debug("vraiOuFauxHabiliteSeanceEnCours date : {} valide période de remplaçement trouvé {}", dateEffet, remplacement);
                return true;
            }
        }
        
        log.debug("vraiOuFauxHabiliteSeanceEnCours date : {} Pas de valide période de remplaçement ", dateEffet);
        return false;
    }
    
    
    class EnseignantRemplacementListener implements EnseignantListener {

        /* (non-Javadoc)
         * @see org.crlr.web.application.control.EnseignantControl.EnseignantListener#enseignantTousOuUnSelectionne(boolean)
         */
        @Override
        public void enseignantTousOuUnSelectionne(boolean tous) {
            //Vide exprès
            
        }

        /* (non-Javadoc)
         * @see org.crlr.web.application.control.EnseignantControl.EnseignantListener#enseignantSelectionnee()
         */
        @Override 
        public void enseignantSelectionnee() {
            final RemplacementDTO remplacement = form.getRemplacementSelected();
            if (remplacement == null) {
                return;
            }
                
            final EnseignantDTO enseignant = enseignantRemplaceControl.getForm().getEnseignantSelectionne();
            log.debug("Enseignant remplacement   nom {}", enseignant);
            if (enseignant == null) {
                return;
            }
               
            remplacement.setEnseignantRemplacant(enseignant);
    
            if (BooleanUtils.isNotTrue(remplacement.getEstAjoute())) {
                remplacement.setEstModifiee(true);
            }
                
            
            
        }
        
    }
    
    class EnseignantAbsentListener implements EnseignantListener {

        /* (non-Javadoc)
         * @see org.crlr.web.application.control.EnseignantControl.EnseignantListener#enseignantTousOuUnSelectionne(boolean)
         */
        @Override
        public void enseignantTousOuUnSelectionne(boolean tous) {
          //Vide exprès
            
        }

        /* (non-Javadoc)
         * @see org.crlr.web.application.control.EnseignantControl.EnseignantListener#enseignantSelectionnee()
         */
        @Override
        public void enseignantSelectionnee() {
                        
            final RemplacementDTO remplacement = form.getRemplacementSelected();
            if (remplacement == null) {
                return;
            }
            
            final EnseignantDTO enseignant = enseignantAbsentControl.getForm().getEnseignantSelectionne();
            log.debug("Enseignant absent nom {}", enseignant);
            if (enseignant == null) {
                return;
            }
            
            remplacement.setEnseignantAbsent(enseignant);
            
            if (BooleanUtils.isNotTrue(remplacement.getEstAjoute())) {
                remplacement.setEstModifiee(true);
            }
        
        
        }
        
    }
 

    /**
     * @param enseignantRemplaceControl the enseignantRemplaceControl to set
     */
    public void setEnseignantRemplaceControl(
            EnseignantControl enseignantRemplaceControl) {
        this.enseignantRemplaceControl = enseignantRemplaceControl;
    }

    /**
     * @return the enseignantAbsentControl
     */
    public EnseignantControl getEnseignantAbsentControl() {
        return enseignantAbsentControl;
    }

    /**
     * @param enseignantAbsentControl the enseignantAbsentControl to set
     */
    public void setEnseignantAbsentControl(EnseignantControl enseignantAbsentControl) {
        this.enseignantAbsentControl = enseignantAbsentControl;
    }

    /**
     * @return the enseignantRemplaceControl
     */
    public EnseignantControl getEnseignantRemplaceControl() {
        return enseignantRemplaceControl;
    }
}

