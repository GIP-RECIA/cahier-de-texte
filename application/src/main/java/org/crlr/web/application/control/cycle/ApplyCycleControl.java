package org.crlr.web.application.control.cycle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.cycle.CycleDTO;
import org.crlr.dto.application.cycle.CycleSeanceDTO;
import org.crlr.dto.application.cycle.CycleSeanceFinalDTO;
import org.crlr.dto.application.cycle.RechercheCycleEmploiQO;
import org.crlr.dto.application.cycle.RechercheCycleSeanceQO;
import org.crlr.dto.application.seance.SaveSeanceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.services.CycleService;
import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.control.AbstractPopupControl;
import org.crlr.web.application.control.ClasseGroupeControl;
import org.crlr.web.application.control.EnseignementControl;
import org.crlr.web.application.control.SequenceControl;
import org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener;
import org.crlr.web.application.control.seance.AjoutSeanceControl;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.utils.NavigationUtils;
import org.springframework.util.CollectionUtils;

/**
 * Controleur associe au formulaire de creation auto des seances d'un cycle.
 * @author G-SAFIR-FRMP
 */
@ManagedBean(name = "applyCycle")
@ViewScoped
public class ApplyCycleControl extends AbstractPopupControl<ApplyCycleForm> implements ClasseGroupeListener {

    /** Le controleur de classeGroupe. */
    @ManagedProperty(value = "#{classeGroupe}")
    private transient ClasseGroupeControl classeGroupeControl;

    /** Le controleur de sequence. */
    @ManagedProperty(value = "#{sequenceControl}")
    protected transient SequenceControl sequenceControl;

    /** Le controleur de saisie de seance. */
    @ManagedProperty(value = "#{ajoutSeance}")
    protected transient AjoutSeanceControl ajoutSeance;
    
    /** Le controleur de enseignement. */
    @ManagedProperty(value = "#{enseignement}")
    protected transient EnseignementControl enseignementControl;
    
    /** cycleService. */
    @ManagedProperty(value = "#{cycleService}")
    private transient CycleService cycleService;

    /** Le logger. */
    protected static final Log log = LogFactory.getLog(ApplyCycleControl.class);

    /**
     * Mutateur de classeGroupeControl {@link #classeGroupeControl}.
     * @param classeGroupeControl le classeGroupeControl to set
     */
    public void setClasseGroupeControl(ClasseGroupeControl classeGroupeControl) {
        this.classeGroupeControl = classeGroupeControl;
    }

    /**
     * Mutateur de ajoutSeance {@link #ajoutSeance}.
     * @param ajoutSeance le ajoutSeance to set
     */
    public void setAjoutSeance(AjoutSeanceControl ajoutSeance) {
        this.ajoutSeance = ajoutSeance;
    }

    /**
     * Mutateur de enseignementControl {@link #enseignementControl}.
     * @param enseignementControl le enseignementControl to set
     */
    public void setEnseignementControl(EnseignementControl enseignementControl) {
        this.enseignementControl = enseignementControl;
    }

    /**
     * Mutateur de sequenceControl {@link #sequenceControl}.
     * @param sequenceControl le sequenceControl to set
     */
    public void setSequenceControl(SequenceControl sequenceControl) {
        this.sequenceControl = sequenceControl;
    }

    /**
     * Mutateur de cycleService {@link #cycleService}.
     * @param cycleService le cycleService to set
     */
    public void setCycleService(CycleService cycleService) {
        this.cycleService = cycleService;
    }

    // *************************************************************************
    // Construction / Initialisation 
    // *************************************************************************
    /**
     * @param form
     */
    public ApplyCycleControl() {
        super(new ApplyCycleForm());
    }

    /**
     * {@inheritDoc}
     */    
    @PostConstruct
    public void onLoad() {
        form.reset();
        
        classeGroupeControl.setListener(this);
        
        // Recupere l'enseignant courant (l'utilisateur)
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTOConnecte();
        final EnseignantDTO enseignantDTO = new EnseignantDTO();
        enseignantDTO.setId(utilisateurDTO.getUserDTO().getIdentifiant());
        enseignantDTO.setUid(utilisateurDTO.getUserDTO().getUid());
        form.setEnseignantDTO(enseignantDTO);
        
        // Recupere les enseignements
        enseignementControl.chargerListeEnseignementDeEnseignant(
                utilisateurDTO.getIdEtablissement(), 
                utilisateurDTO.getUserDTO().getIdentifiant());
        final List<EnseignementDTO> listeEnseignement = enseignementControl.getForm().getListeEnseignement();
        form.setListeEnseignement(listeEnseignement);
        
        // Recupere les sequences de l'enseignant
        
        // Recupere le cycle
        final CycleDTO cycleDTO = (CycleDTO) ContexteUtils
                .getContexteOutilControl().recupererEtSupprimerObjet(
                        RechercheCycleControl.class.getName());
        if (cycleDTO != null) {
            
            // Charge les seance pedagogique du cycle
            final RechercheCycleSeanceQO rechercheCycleSeanceQO = new RechercheCycleSeanceQO();
            rechercheCycleSeanceQO.setCycleDTO(cycleDTO);
            rechercheCycleSeanceQO.setAvecDetail(true);
            final List<CycleSeanceDTO> listeSeance = cycleService.findListeCycleSeance(rechercheCycleSeanceQO);
            cycleDTO.setListeSeance(listeSeance);
         
            form.setCycle(cycleDTO);
        }
    }

    /**
     * Initialise la liste des cycleSeance en positionnant une seance sur chaque element.
     */
    private void initialiseListeSeance() {
        
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTOConnecte();
        final List<CycleSeanceFinalDTO> listeSeance = new ArrayList<CycleSeanceFinalDTO>();
        final EnseignementDTO enseignementDTO;
        if (form.getListeEnseignement() != null && !form.getListeEnseignement().isEmpty()) {
            enseignementDTO = form.getEnseignementDafaultDTO();
        } else {
            enseignementDTO = null;
        }
        if (form.getCycle() != null && form.getCycle().getListeSeance()!=null) {
            for (final CycleSeanceDTO cycleSeance : form.getCycle().getListeSeance()) {
                
                // Recopie les infos de l'objet CycleSeanceDTO vers le CycleSeanceFinalDTO
                final CycleSeanceFinalDTO seanceFinal = new CycleSeanceFinalDTO();
                seanceFinal.setVraiOuFauxChecked(true);
                ObjectUtils.copyProperties(seanceFinal, cycleSeance);
                if (!cycleSeance.getAnnotationsVisible() && 
                        !cycleSeance.getEnseignantDTO().getId().equals(utilisateurDTO.getUserDTO().getIdentifiant())) {
                    seanceFinal.setAnnotations(null);
                }
                
                
                // Initialise une nouvelle SeanceDTO
                seanceFinal.initSeanceDTO(form.getEnseignantDTO(), enseignementDTO);
                
                // Ajout dans la liste la seance final
                listeSeance.add(seanceFinal);
            }
        }
        form.setListeSeance(listeSeance);
    }
    
    /**
     * Extrait de la liste de toutes les sequences de l'enseignant uniquement celles
     * qui concerne l'enseignement.
     * @param idEnseignement id de l'enseignement.
     * @param date la date de la seance. 
     * @return une liste.
     */
    private List<SequenceDTO> extraitListeSequenceEnseignement(final Integer idEnseignement, final Date date) {
        final List<SequenceDTO> liste = new ArrayList<SequenceDTO>();
        for (final SequenceDTO sequenceDTO : sequenceControl.getForm().getListeSequence()) {
            if (sequenceDTO.getIdEnseignement().equals(idEnseignement) && 
                    DateUtils.isBetween(date, sequenceDTO.getDateDebut(), sequenceDTO.getDateFin())) {
                liste.add(sequenceDTO);
            }
        }
        return liste;
    }
    
    /**
     * Génère la liste des séances datées à partir de la date de départ. 
     * @param dateDepart date de départ.
     */
    private void genererListeSeanceEnDate(final Date dateDepart) {
        
        // Recupere l'utilisateur
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTOConnecte();
        final Integer idEnseignant = utilisateurDTO.getUserDTO().getIdentifiant();
        
        // Id etablissement
        final Integer idEtablissement = utilisateurDTO.getIdEtablissement();
        
        // Id de la classe ou groupe
        final GroupesClassesDTO groupeClasseDTO = classeGroupeControl.getForm().getGroupeClasseSelectionne();
        
        final RechercheCycleEmploiQO rechercheCycleEmploiQO = new RechercheCycleEmploiQO();
        rechercheCycleEmploiQO.setDateDepart(dateDepart);
        rechercheCycleEmploiQO.setIdEnseignant(idEnseignant);
        rechercheCycleEmploiQO.setGroupeClasse(groupeClasseDTO);
        rechercheCycleEmploiQO.setIdEtablissement(idEtablissement);
        rechercheCycleEmploiQO.setAnneeScolaire(utilisateurDTO.getAnneeScolaireDTO());
        rechercheCycleEmploiQO.setListeSequence(sequenceControl.getForm().getListeSequence());
        
        // Liste des seances finals avec l'enseignement correspondant :
        // On ne garde que celle qui sont cochees
        /*final List<CycleSeanceFinalDTO> liste = new ArrayList<CycleSeanceFinalDTO>();
        for (final CycleSeanceFinalDTO seance : form.getListeSeance()) {
            if (seance.getVraiOuFauxChecked()) {
                liste.add(seance);
            }
        }*/
        rechercheCycleEmploiQO.setListeSeance(form.getListeSeance());

        // Appelle le service qui va retourner les seances remplies 
        final ResultatDTO<List<CycleSeanceFinalDTO>> resultat = cycleService.findListeEmploiDTO(rechercheCycleEmploiQO);
        final List<CycleSeanceFinalDTO> listeSeance = resultat.getValeurDTO(); 

        // Positionne les seance sur le formulaire
        form.setListeSeance(listeSeance);        
    }
    
    /**
     * Recherche dans la liste existe la seance par son Id.
     * @param id le id de seance.
     * @return l'objet.
     */
    private CycleSeanceFinalDTO findCycleSeanceById(final Integer id) {
        if (id != null) {
            for (final CycleSeanceFinalDTO seance : form.getListeSeance()) {
                if (id.equals(seance.getId())) {
                    return seance;
                }
            }
        }
        return null;
    }
    
    /**
     * Recherche dans la liste existe l'enseignement par son Id.
     * @param id le id de l'enseignement.
     * @return l'objet.
     */
    private EnseignementDTO findEnseignementById(final Integer id) {
        if (id!=null) {
            for (final EnseignementDTO enseignement : form.getListeEnseignement()) {
                if (id.equals(enseignement.getId())) {
                    return enseignement;
                }
            }
        }
        return null;
    }
    
    // *************************************************************************
    // Methode invoquees depuis le formulaire 
    // *************************************************************************
    /**
     * Sauvergarde toutes les seances qui sont cochees.
     */
    public void sauver() {
        final List<SaveSeanceQO> listSaveSeanceQO = new ArrayList<SaveSeanceQO>();
        for (final CycleSeanceFinalDTO cycleSeanceFinalDTO : form.getListeSeance()) {
            if (!cycleSeanceFinalDTO.getVraiOuFauxChecked()) {
                continue;
            }
            // Positionne la sequence sur chaque seance en fonction de l'enseignement choisi
            final List<SequenceDTO> listeSequence = 
                extraitListeSequenceEnseignement(cycleSeanceFinalDTO.getEnseignementDTO().getId(), cycleSeanceFinalDTO.getSeanceDTO().getDate());
            if (!listeSequence.isEmpty()) {
                cycleSeanceFinalDTO.getSeanceDTO().setSequenceDTO(listeSequence.get(0));
            } else {
                cycleSeanceFinalDTO.getSeanceDTO().setSequenceDTO(new SequenceDTO());
            }
            
            final SaveSeanceQO saveSeanceQO = ajoutSeance.construitAjoutSaveSeanceQO(cycleSeanceFinalDTO.getSeanceDTO());
            listSaveSeanceQO.add(saveSeanceQO);
        }
        try {
            seanceService.saveListeSeance(listSaveSeanceQO);
            
            
        } catch (MetierException e) {
            log.error("Erreur de sauvegarde de la liste des séances");
        }
    }

    /**
     * Genere la liste des seances sur la classe selectionnee à partir de la date de debut.
     */
    public void genererSeance() {
        
        // Initialise les séances si pas déjà fait 
        if (CollectionUtils.isEmpty(form.getListeSeance())) {
            initialiseListeSeance();
        }
        
        // Date de depart
        Date dateCourante = DateUtils.setTime(form.getDateDepart(), 0, 0);

        // Lance le calcul
        genererListeSeanceEnDate(dateCourante);
    }
    
    /**
     * Reset le forumaire : efface les seances genérées et reset la classe et date de debut.
     */
    public void reset() {
        form.reset();
        classeGroupeControl.reset();
    }
    
    /**
     * Vide les seances. 
     */
    public void viderListeSeance() {
        // Vide le calcul eventuel 
        form.setListeSeance(new ArrayList<CycleSeanceFinalDTO>());
    }
    
    /**
     * Navigue vers l'ecran precedent.
     * @return ma chaine de navigation.
     */
    public String retour() {
        form.reset();
        return NavigationUtils.retourOutilPrecedentEnDepilant();
    }

    /**
     * Calcule les dates de toutes les seances en fonction de la date de depart.
     * Se base sur les case de l'emploi du temps de la classe. 
     */
    public void caclulerDateSeance() {
        // Date de depart
        Date dateCourante = DateUtils.setTime(form.getDateDepart(), 0, 0);

        // Lance le calcul
        genererListeSeanceEnDate(dateCourante);
    }
    
    /**
     * Recaclules les dates des seances suivantes de celles qui est sélectionnée suite à un changement 
     * de date de seance ou d'enseignement. 
     */
    public void recaclulerDateSeance() {
        if (form.getSeanceSelected() == null) { 
            return;
        }
        
        // Date de depart
        final Date dateDepart = form.getSeanceSelected().getSeanceDTO().getDate();
        
        // Lance le calcul
        genererListeSeanceEnDate(dateDepart);
    }

    /**
     * Affiche dans la popup la seance sélectionnée.
     */
    public void afficherSeance() {
        final CycleSeanceFinalDTO cycleSeance = form.getSeanceSelected() ; 
        if (cycleSeance != null) {
            final SeanceDTO seance = ObjectUtils.clone(form.getSeanceSelected().getSeanceDTO());
            ajoutSeance.getForm().setSeance(seance);
            final List<SequenceDTO> listeSequence = 
                extraitListeSequenceEnseignement(cycleSeance.getEnseignementDTO().getId(), cycleSeance.getSeanceDTO().getDate());
            ajoutSeance.getForm().setListeSequenceSeance(listeSequence);
            if (!listeSequence.isEmpty()) {
                seance.setSequenceDTO(listeSequence.get(0)); // On positionne la premiere sequence par defaut.
            } else {
                seance.setSequenceDTO(null);
            }
        }
    }

    /**
     * Valide la saisie de la seance/devoir affichée dans la popup.
     */
    public void validerSeance() {
        form.getSeanceSelected().setSeanceDTO(ajoutSeance.getForm().getSeance());
    }

    /**
     * Appele apres que la saisie d'une seance ai ete validee.
     */
    public void refreshListeSeance() {
        
    }
    
    /**
     * Appele suite a la modification de l'enseignement sur une ligne.
     */
    public void modifieEnseignement() {
        final Integer idLigne = form.getLigneSeanceId();
        final Integer idEnseignement = form.getLigneEnseignementId();
        if (idLigne !=null) {
            final CycleSeanceFinalDTO seance = findCycleSeanceById(idLigne);
            final EnseignementDTO enseignement = findEnseignementById(idEnseignement);
            if (seance != null && enseignement != null && seance.getSeanceDTO().getDate() != null) {
                
                // Verifie s'il existe des sequence pour cet enseignement
                final List<SequenceDTO> listeSequence = 
                    extraitListeSequenceEnseignement(idEnseignement, seance.getSeanceDTO().getDate());
                
                if (listeSequence.size() == 0) {
                    seance.setVraiOuFauxChecked(false);
                    seance.getSeanceDTO().setSequenceDTO(new SequenceDTO());
                } else {
                    seance.getSeanceDTO().setSequenceDTO(listeSequence.get(0));
                }
                seance.setEnseignementDTO(enseignement);
                form.setSeanceSelected(seance);
                recaclulerDateSeance();
            }
        }
    }
    
    
    // ------------------------------------------------------------------------
    // Implementation des methodes des controleurs embarques 
    // ------------------------------------------------------------------------
    /**
     * {@inheritDoc} 
     */
    @Override
    public void classeGroupeTypeSelectionne() {

        // Vide le calcul eventuel
        viderListeSeance();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void classeGroupeSelectionnee() {

        // Rafraichit les sequence de l'enseignant
        sequenceControl.chargerListeSequence(null, form.getEnseignantDTO(), classeGroupeControl.getForm().getGroupeClasseSelectionne());
        
        // On vide les séances évenutellement initialisées
        viderListeSeance();
    }

    // ------------------------------------------------------------------------
    // Les Getter sur le controleur
    // ------------------------------------------------------------------------
    /**
     * Verifie si bouton generer les seances peut etre affiche ou non.
     * @return true / false
     */
    public Boolean getAffichageGenerer() {
        if (form.getDateDepart() == null || form.getClass() == null || classeGroupeControl.getForm().getGroupeClasseSelectionne()==null || 
            form.getEnseignementDafaultDTO()==null ) {
            return false;
        }
        return true;
    }

    /**
     * Verifie si bouton Sauver les seances peut etre affiche ou non.
     * @return true / false
     */
    public Boolean getAffichageSauver() {
        if (form.getDateDepart() == null || form.getClass() == null || classeGroupeControl.getForm().getGroupeClasseSelectionne()==null 
            || form.getListeSeance() == null || form.getListeSeance().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Retourne un message si aucune sequence n'est dispo sur la seance selectionnée.
     * @return un message.
     */
    public String getAfficheErreur() {
        if (form.getSeanceSelected() != null) {
            if (form.getSeanceSelected().getSeanceDTO().getSequenceDTO() == null 
                    || form.getSeanceSelected().getSeanceDTO().getSequenceDTO().getId()==null) {
                return "pas de sequence";
            }
        }
        return null;
    }
}
