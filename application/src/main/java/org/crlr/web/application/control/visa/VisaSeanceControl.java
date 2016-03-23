/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: VisaSeanceControl.java,v 1.23 2010/05/19 09:10:20 jerome.carriere Exp $
 */

package org.crlr.web.application.control.visa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang.BooleanUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.Outil;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.ArchiveSeanceDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.remplacement.RechercheRemplacementQO;
import org.crlr.dto.application.remplacement.RemplacementDTO;
import org.crlr.dto.application.seance.RechercheVisaSeanceQO;
import org.crlr.dto.application.visa.DateListeVisaSeanceDTO;
import org.crlr.dto.application.visa.ResultatRechercheVisaSeanceDTO;
import org.crlr.dto.application.visa.TypeVisa;
import org.crlr.dto.application.visa.VisaDTO;
import org.crlr.dto.application.visa.VisaDTO.VisaProfil;
import org.crlr.dto.application.visa.VisaEnseignantDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.VisaFacadeService;
import org.crlr.services.EtablissementService;
import org.crlr.services.RemplacementService;
import org.crlr.services.SequenceService;
import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.control.AbstractControl;
import org.crlr.web.application.control.ClasseGroupeControl;
import org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener;
import org.crlr.web.application.control.EnseignantControl;
import org.crlr.web.application.control.EnseignantControl.EnseignantListener;
import org.crlr.web.application.control.EnseignementControl;
import org.crlr.web.application.control.EnseignementControl.EnseignementListener;
import org.crlr.web.application.control.SequenceControl;
import org.crlr.web.application.control.SequenceControl.SequenceListener;
import org.crlr.web.application.control.seance.AjoutSeanceControl;
import org.crlr.web.application.control.seance.SaisirSeanceControl;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.application.form.visa.VisaSeanceForm;
import org.crlr.web.application.form.visa.VisaSeanceForm.Affichage;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.utils.NavigationUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.23 $
 */
@ManagedBean(name="visaSeance")
@ViewScoped
public class VisaSeanceControl extends AbstractControl<VisaSeanceForm> 
implements ClasseGroupeListener, EnseignantListener, EnseignementListener, SequenceListener  {
    
    /** DOCUMENTATION INCOMPLETE! */
    private final static String IDFORM = VisaSeanceControl.class.getName() + "form";

    @ManagedProperty(value = "#{classeGroupe}")
    private transient ClasseGroupeControl classeGroupeControl;
    
    @ManagedProperty(value = "#{enseignement}")
    protected transient EnseignementControl enseignementControl;
    
    @ManagedProperty(value = "#{enseignant}")
    protected transient EnseignantControl enseignantControl;
    
    @ManagedProperty(value = "#{enseignant2}")
    protected transient EnseignantControl enseignantRemplaceControl;
    
    @ManagedProperty(value = "#{sequenceControl}")
    protected transient SequenceControl sequenceControl;
    

    /** sequenceService. */
    @ManagedProperty(value="#{sequenceService}")
    protected transient SequenceService sequenceService;

    /** Le controleur des seances ajout . */
    @ManagedProperty(value="#{ajoutSeance}")
    private transient AjoutSeanceControl ajoutSeance;
    
    @ManagedProperty(value = "#{remplacementService}")
    private transient RemplacementService remplacementService;

    
    /**
     * @param classeGroupeControl the classeGroupeControl to set
     */
    public void setClasseGroupeControl(ClasseGroupeControl classeGroupeControl) {
        this.classeGroupeControl = classeGroupeControl;
    }
    

   
    
    @ManagedProperty(value = "#{visaFacade}")
    private transient VisaFacadeService visaFacade;
    
    @ManagedProperty(value = "#{etablissementService}")
    private transient EtablissementService etablissementService;
    
    
    

    /** Controleur de la saisie par semaine pour acceder aux fonctionnalités agenda d'un enseignant. */ 
    @ManagedProperty(value = "#{saisirSeance}")
    private transient SaisirSeanceControl saisirSeanceControl;
    
    private EnseignantRemplacementListener enseignantRemplacementListener;
    

    /**
     * Mutateur de saisirSeanceControl {@link #saisirSeanceControl}.
     * @param saisirSeanceControl le saisirSeanceControl to set
     */
    public void setSaisirSeanceControl(SaisirSeanceControl saisirSeanceControl) {
        this.saisirSeanceControl = saisirSeanceControl;
    }
    
    /**
     * Constructeur.
     */
    public VisaSeanceControl() {
        super(new VisaSeanceForm());
    }

    /**
     * 
     */
    @PostConstruct
    public void onLoad() {
        
        form.setTexteAide(visaFacade.getAideContextuelleSeance());
        
        VisaDTO visa = (VisaDTO) ContexteUtils.getContexteOutilControl()
            .recupererEtSupprimerObjet(VisaDTO.class.getName());

        // Recupere le visaDTO d'origine si on vient de visaListe
        form.setVisaOrigine(visa);


        // Recupere le visaEnseignantDTO d'origine si on vient de visaListe
        VisaEnseignantDTO visaEnseignant = (VisaEnseignantDTO) ContexteUtils.getContexteOutilControl()
            .recupererEtSupprimerObjet(VisaEnseignantDTO.class.getName());
        form.setVisaEnseignantOrigine(visaEnseignant);
       
        reset();
        
        //Apres reset
        classeGroupeControl.setListener(this);
        enseignementControl.setListener(this);
        enseignantControl.setListener(this);
        sequenceControl.setListener(this);
        
        enseignantRemplacementListener = new EnseignantRemplacementListener();
        enseignantRemplaceControl.setListener(enseignantRemplacementListener);
        
        
        
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        final Profil profilUser = utilisateurDTO.getProfil();

        if (Profil.DIRECTION_ETABLISSEMENT == profilUser) {
            form.setProfilVisaUser(VisaProfil.ENTDirecteur);
            form.setModeAffichage(Affichage.LISTE);
        } else {
            form.setProfilVisaUser(VisaProfil.ENTInspecteur);
            form.setModeAffichage(Affichage.DETAIL);
        }
        enseignantControl.getForm().setFiltreParEnseignant(true);
        enseignantRemplaceControl.getForm().setFiltreParEnseignant(true);
        
        getForm().setModeRemplacant(false);
        
        // On declenche la recherche auto si on vient de visaListe avec un cahierVisa selectionne
        if (visa != null) {
            rechercher();
        }
    }

    /** Reinit tous les criteres par defaut. */
    public void reset() {
        form.reset();
        
        form.setAfficheRetour(form.getVisaOrigine()!=null || form.getVisaEnseignantOrigine()!=null);
        
        GroupesClassesDTO groupesClassesDTO = form.getVisaOrigine() == null ? null :
                form.getVisaOrigine().getClasseGroupe();
        
        rechercherEnseignant();
        
        //Initialisation basé sur l'enseignant sélectionné (premier dans la liste)
        enseignantSelectionnee();
        
        
        if (form.getVisaOrigine() != null) {
            enseignementControl.getForm().setTous(false);
            enseignementControl.getForm().setEnseignementSelectionne(form.getVisaOrigine().getEnseignementDTO());
        }
        
        if (groupesClassesDTO != null) {
            classeGroupeControl.getForm().setTous(false);
            classeGroupeControl.getForm().setTypeGroupeSelectionne(groupesClassesDTO.getTypeGroupe());
            classeGroupeControl.classeGroupeTypeSelectionne(null);
            classeGroupeControl.getForm().setGroupeClasseSelectionne(groupesClassesDTO);
        } 
        
        //Puisque les critères de enseignement ou classe groupe ont changé par rapport au défauts établis dans 
        //enseignantSelectionné, il faut rechercher les séquences
        if (form.getVisaOrigine() != null || groupesClassesDTO != null) {
            rechercherListeSequenceSelectionnee();
        }
        
    }
    
    /**
     * 
     * Les listeners pour la sélection du remplaçement.
     *
     */
    class EnseignantRemplacementListener implements EnseignantListener {

        /* (non-Javadoc)
         * @see org.crlr.web.application.control.EnseignantControl.EnseignantListener#enseignantTousOuUnSelectionne(boolean)
         */
        @Override
        public void enseignantTousOuUnSelectionne(boolean tous) {
            
            
        }

        /* (non-Javadoc)
         * @see org.crlr.web.application.control.EnseignantControl.EnseignantListener#enseignantSelectionnee()
         */
        @Override
        public void enseignantSelectionnee() {
            form.setListeSeance(new ArrayList<DateListeVisaSeanceDTO>());

            form.setVraiOuFauxRechercheActive(false);
        }
        
    }
    
    /**
     * Recherche la liste des enseignants visible pour l'utilisateur connecte.
     */
    private void rechercherEnseignant() {
        
        
     // Charge la liste des enseignants de l'utilisateur connecte
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        
        // Le directeur voit touts les enseignants de son etablissement 
        final List<EnseignantDTO> listeEnseignant =
                visaFacade.findListeEnseignant(utilisateurDTO).getValeurDTO();

        enseignantControl.getForm().setListeEnseignant(listeEnseignant);
        
     // selectionne l'enseignant par defaut
        
        if (form.getVisaOrigine() == null && form.getVisaEnseignantOrigine() == null) {
            if (listeEnseignant != null && listeEnseignant.size()>0) {
                enseignantControl.getForm().setEnseignantSelectionne(listeEnseignant.get(0));
            }
        } else {
            final Integer idEnseignantVisa;
            if (form.getVisaOrigine() != null) {
                idEnseignantVisa = form.getVisaOrigine().getIdEnseignant(); 
            } else {
                idEnseignantVisa = form.getVisaEnseignantOrigine().getEnseignant().getId();
            }
            
            for (final EnseignantDTO enseignant : listeEnseignant) {
                if (enseignant.getId().equals(idEnseignantVisa)) {
                    enseignantControl.getForm().setEnseignantSelectionne(enseignant);   
                }
            }
            if (enseignantControl.getForm().getEnseignantSelectionne()==null) {
                if (listeEnseignant != null && listeEnseignant.size()>0) {
                    enseignantControl.getForm().setEnseignantSelectionne(listeEnseignant.get(0));
                }
            }
        }
        
        // Charge pour chaque enseignant la liste des visa / cahiers de texte
        enseignantControl.getForm().setListeEnseignant(listeEnseignant);
        
        
    }
    
    //Declenchee lors du click sur la loupe d'une ligne de visa seance.
    public void chargerSeance() {

        log.info("chargerSeance");
        form.setAfficheVisualiserArchiveSeance(true);
        
        ajoutSeance.getForm().setSeance( form.getVisaSeanceSelected() );
    }
    
    public void chargerSeanceAjax(javax.faces.event.AjaxBehaviorEvent event) throws javax.faces.event.AbortProcessingException {
        log.info("chargerSeanceAjax");
        chargerSeance();
    }
    
    /**
     * Charger l'archive séance.
     */
    public void chargerArchiveSeance() {

        form.setAfficheVisualiserArchiveSeance(false);
        
        Integer idVisa = form.getVisaSeanceSelected().getVisaDeUtilisateur()
                .getIdVisa();

        try {
            ArchiveSeanceDTO archiveSeance = null;
            archiveSeance = visaFacade.findArchiveSeance(
                    form.getVisaSeanceSelected().getId(), idVisa)
                    .getValeurDTO();

            if (archiveSeance != null) {
                ajoutSeance.getForm().setSeance(archiveSeance);
            } else {
                log.error("Archive séance n'a pas été trouvé");
                ajoutSeance.getForm().setSeance(new SeanceDTO());
            }
        } catch (MetierException ex) {
            log.debug( "ex", ex);
        }
    }
    
    public void chargerDevoir() {
        
    }

    /**
     * Appel métier de recherche des enseignements.
     */
    private void rechercherEnseignement() {
        final UtilisateurDTO utilisateurDTO =
                ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
          
            
        EnseignantDTO enseignant = enseignantControl.getForm().getEnseignantSelectionne();
        final Integer idEnseignant = enseignant == null ? null : enseignant.getId();
        
        enseignementControl.chargerListeEnseignementDeEnseignant(utilisateurDTO.getIdEtablissement(),
                idEnseignant);
    }
    
    /**
     * Recherche la liste des classes/groupes qui sont affichees dans la popup de selection classe/groupe.
     */
    private void rechercherListeClasseGroupe() {
        final Integer idEnseignant;
        final EnseignantDTO enseignant = enseignantControl.getForm().getEnseignantSelectionne();
        if (enseignant != null) {
            idEnseignant = enseignant.getId();
        } else {
            idEnseignant = null;
        }
        classeGroupeControl.chargerGroupeClasseEnseignant(idEnseignant);
    }
    
    /**
     * Appelee quand on change de radio bouton pour enseignement.
     */
    public void selectionnerCritereEnseignement() {
        
    }
    
    /**
     * Appelee quand on change de radio bouton pour sequence.
     */
    public void selectionnerCritereSequence() {
        
    }
    
    /**
     * Appelee quand on change de radio bouton pour le mode d'affichage.
     */
    public void selectionnerModeAffichage() {
        
    }
    
    
    
    
    
    
    /**
     * Recherche la liste des séquences.
     */
    public void rechercherListeSequenceSelectionnee() {
        sequenceControl.chargerListeSequence(
                 enseignementControl.getForm().getEnseignementSelectionne(), 
                enseignantControl.getForm().getEnseignantSelectionne(),
                classeGroupeControl.getForm().getGroupeClasseSelectionne());
        
    }

    
    
    @Override
    public void classeGroupeSelectionnee() {
        
        form.setListeSeance(new ArrayList<DateListeVisaSeanceDTO>());
        sequenceControl.getForm().setSequenceSelectionnee(null);
        sequenceControl.getForm().setTousSelectionne(true);
        
        rechercherListeSequenceSelectionnee();
        
        form.setVraiOuFauxRechercheActive(false);
    }

    @Override
    public void classeGroupeTypeSelectionne() {
        form.setListeSeance(new ArrayList<DateListeVisaSeanceDTO>());
        sequenceControl.getForm().setSequenceSelectionnee(null);
        sequenceControl.getForm().setTousSelectionne(true);
        
        rechercherListeSequenceSelectionnee();
        form.setVraiOuFauxRechercheActive(false);
    }

    /**
     * Retourne le titre de la popup agenda. 
     * @return le titre.
     */
    public String getTitreAgenda() {
        EnseignantDTO enseignantDTO = enseignantControl.getForm().getEnseignantSelectionne();
        if (enseignantDTO != null) {
            return "Remplissage du cahier de texte de " + enseignantDTO.getNomComplet();
        } else {
            return "";
        }
         
    }
    

    /**
     * Appel métier de recherche des séquences en fonction de potentiels
     * critères.
     */
    public void rechercher() {
        
        /** les critères de recherche. */
        final RechercheVisaSeanceQO rechercheQO = new RechercheVisaSeanceQO();
        
        Integer idEnseignant = null;
        
        if (BooleanUtils.isNotTrue(getForm().getModeRemplacant())) {
            
            //En mode normale, on met l'enseignement et la séquence
            if (enseignementControl.getForm().getEnseignementSelectionne() != null) {
                rechercheQO.setCodeEnseignement(enseignementControl.getForm().getEnseignementSelectionne().getCode());
            }
            if (sequenceControl.getForm().getSequenceSelectionnee() != null) {
                rechercheQO.setIdSequence(sequenceControl.getForm().getSequenceSelectionnee().getId());
            }
            if (enseignementControl.getForm().getEnseignementSelectionne() != null) {
                rechercheQO.setIdEnseignement(enseignementControl.getForm().getEnseignementSelectionne().getId());
            }
            
            EnseignantDTO enseignant = enseignantControl.getForm().getEnseignantSelectionne();
            log.debug("Recherche visa pour un enseignant {}", enseignant);
            idEnseignant = enseignant == null ? null : enseignant.getId();
            rechercheQO.setIdEnseignant(idEnseignant);
            rechercheQO.setIdEnseignantRemplacant(null);
            
        } else {
            EnseignantDTO enseignantRemplacant = enseignantControl.getForm().getEnseignantSelectionne();
            log.debug("Recherche visa pour un enseignant {}", enseignantRemplacant);
            idEnseignant = enseignantRemplacant == null ? null : enseignantRemplacant.getId();
            rechercheQO.setIdEnseignantRemplacant(idEnseignant);
            
            //Utilise le remplaçement choisi
            EnseignantDTO enseignantRemplace = enseignantRemplaceControl.getForm().getEnseignantSelectionne();
          
            log.debug("Recherche visa pour un enseignant remplaçé {}", enseignantRemplace);
            rechercheQO.setIdEnseignant(enseignantRemplace.getId());
        }
        
        
        final Integer idEtablissement =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getIdEtablissement();
        
        
        if (idEnseignant == null) {
            log.warn("Id enseignant null, l'établissement n'a pas d'enseignants");
            return;
        }
            

        Boolean seancesAutomatique = etablissementService.checkSaisieSimplifieeEtablissement(idEtablissement, idEnseignant).getValeurDTO();
        
        form.setAfficheSequenceTitre(BooleanUtils.isNotTrue(seancesAutomatique));
        
        rechercheQO.setIdEtablissement(idEtablissement);
        rechercheQO.setGroupeClasseSelectionne(classeGroupeControl.getForm().getGroupeClasseSelectionne());
        rechercheQO.setAfficheNonVisees(form.getVraiOuFauxNonVisee());
        rechercheQO.setAfficheVisees(form.getVraiOuFauxVisee());
        rechercheQO.setAffichePerimees(form.getVraiOuFauxPerimee());
        
        rechercheQO.setUtilisateurConnecte(ContexteUtils.getContexteUtilisateur().getUtilisateurDTOConnecte());
        
        try {
            final ResultatDTO<List<DateListeVisaSeanceDTO>> listeSeanceDTO = 
                    visaFacade.findVisaSeance(rechercheQO);
            form.setListeSeance(ObjectUtils.clone(listeSeanceDTO.getValeurDTO()));
            form.setVraiOuFauxRechercheActive(true);
        } catch (MetierException e) {
            this.form.setListeSeance(new ArrayList<DateListeVisaSeanceDTO>());
            log.debug("{0}", e.getMessage());
        }
        
         
        //BOUCHON
        /*
        if (form.getListeSeance().size() > 0) {
       log.error("Is modified {0}", form.getListeSeance().get(0).getListeVisaSeance().get(0).getVraiOuFauxModifiee());
        form.getListeSeance().get(0).getListeVisaSeance().get(0).setIntitule("t" + DateUtils.format(new Date(), "hh:mm:ss"));
        }*/
    }

    /**
     *  
     */
    public void afficherDetailSeance() {
        
    }
    
    /**
     * Navigue vers écran précedent.
     * @return ma chaine de navigation.
     */
    public String retour() {
        form.reset();
        return NavigationUtils.retourOutilPrecedentEnDepilant();
    }

    /**
     * Appellée lors de la consultation de l'agenda d'un enseignant.
     * @throws MetierException l'exception  
     */
    public void consulterEnseignantAgenda() throws MetierException {
        final EnseignantDTO enseignant = enseignantControl.getForm().getEnseignantSelectionne();
        if (enseignant != null && enseignant.getId() != null) {
            saisirSeanceControl.getForm().setIdEnseignant(enseignant.getId());
            saisirSeanceControl.selectionnerSemaine();
        }
    }

    /**
     * Affichage la semaine precedente.
     * @throws MetierException  execept
     */
    public void naviguerSemainePrecedent() throws MetierException {
        BarreSemaineDTO semaine = saisirSeanceControl.getForm().getSemaineSelectionne();
        if (semaine == null) {
            return;
        }
        if (saisirSeanceControl.getForm().getListeBarreSemaine() == null ||saisirSeanceControl.getForm().getListeBarreSemaine().isEmpty()) {
            return;
        }
        final Integer i = saisirSeanceControl.getForm().getListeBarreSemaine().indexOf(semaine);
        if (i!=null && i>0) {
            semaine = saisirSeanceControl.getForm().getListeBarreSemaine().get(i-1);
            saisirSeanceControl.getForm().setSemaineSelectionne(semaine);
            saisirSeanceControl.selectionnerSemaine();
        }
    }
    /**
     * Affichage la semaine suivante.
     * @throws MetierException  execept
     */
    public void naviguerSemaineSuivant() throws MetierException {
        BarreSemaineDTO semaine = saisirSeanceControl.getForm().getSemaineSelectionne();
        if (semaine == null) {
            return;
        }
        if (saisirSeanceControl.getForm().getListeBarreSemaine() == null ||saisirSeanceControl.getForm().getListeBarreSemaine().isEmpty()) {
            return;
        }
        final Integer i = saisirSeanceControl.getForm().getListeBarreSemaine().indexOf(semaine);
        if (i!=null && i<saisirSeanceControl.getForm().getListeBarreSemaine().size()-1) {
            semaine = saisirSeanceControl.getForm().getListeBarreSemaine().get(i+1);
            saisirSeanceControl.getForm().setSemaineSelectionne(semaine);
            saisirSeanceControl.selectionnerSemaine();
        }
    }
    
    /**
     * Poser un visa simple sur toutes les seances non visées ou perimées.
     */
    public void addVisaSimple() {
        addVisa(TypeVisa.SIMPLE);
    }
    
    /**
     * @param typeVisa tv
     */
    private void addVisa(TypeVisa typeVisa) {
        
        // Cherche toutes les seances non visee ou qui sont perimee pour le
        // profil.
        for (final DateListeVisaSeanceDTO dateListe : form.getListeSeance()) {
            for (final ResultatRechercheVisaSeanceDTO seanceVisa : dateListe
                    .getListeVisaSeance()) {

                VisaDTO visa = seanceVisa.getVisaDeUtilisateur();

                // Ne pose pas un visa sur des séances visés / non perimés
                if (BooleanUtils.isNotTrue(seanceVisa.getVraiOuFauxModifiee()) 
                        && visa.getExists()
                        && BooleanUtils.isFalse(visa.getEstPerime())) {
                    continue;
                }

                seanceVisa.setVraiOuFauxModifiee(true);

                visa.setDateVisa(new Date());
                visa.setTypeVisa(typeVisa);

            }
        }
    }
    
    /**
     * Poser un visa Mémo sur toutes les seances non visées ou perimées.
     */
    public void addVisaMemo() {
        addVisa(TypeVisa.MEMO);
    }
    
    /**
     * Affiche la page d'aide.
     */
    public void afficheAide() {
        
    }
    
    /**
     * Sauvegarde des modifications du formulaire.
     */
    public void sauver() {
        
        final List<ResultatRechercheVisaSeanceDTO> listeVisaSeance = new ArrayList<ResultatRechercheVisaSeanceDTO>();

        for (final DateListeVisaSeanceDTO dateListe : form.getListeSeance()) {
            for (final ResultatRechercheVisaSeanceDTO visaSeance : dateListe
                    .getListeVisaSeance()) {
                if (visaSeance.getVraiOuFauxModifiee()) {
                    listeVisaSeance.add(visaSeance);
                }
            }
        }

        try {

            visaFacade.saveListeVisaSeance(listeVisaSeance);
            form.setAfficheSauvegarde(false);

            rechercher();
        } catch (MetierException ex) {
            log.debug("ex", ex);
        }        
    }
    
    /**
     * Verifie si la semaine precedente peut etre affichee ou non.
     * @return true / false
     */
    public Boolean getAfficheSemainePrecedente() {
        final BarreSemaineDTO semaine = saisirSeanceControl.getForm().getSemaineSelectionne(); 
        if (semaine== null) { 
            return false;
        }
        if (saisirSeanceControl.getForm().getListeBarreSemaine() == null ||saisirSeanceControl.getForm().getListeBarreSemaine().isEmpty()) {
            return false;
        }
        if (semaine.equals(saisirSeanceControl.getForm().getListeBarreSemaine().get(0))) {
            return false;
        }
        return true;
    }
    
    /**
     * Verifie si la semaine suivante peut etre affichee ou non.
     * @return true / false
     */
    public Boolean getAfficheSemaineSuivante() {
        final BarreSemaineDTO semaine = saisirSeanceControl.getForm().getSemaineSelectionne(); 
        if (semaine== null) { 
            return false;
        }
        if (saisirSeanceControl.getForm().getListeBarreSemaine() == null ||saisirSeanceControl.getForm().getListeBarreSemaine().isEmpty()) {
            return false;
        }
        if (semaine.equals(saisirSeanceControl.getForm().getListeBarreSemaine().get(saisirSeanceControl.getForm().getListeBarreSemaine().size()-1))) {
            return false;
        }
        return true;
    }
    
    /**
     * Retourne le titre de la semaine courante.
     * @return le titr.
     */
    public String getTitreSemaine() {
        final BarreSemaineDTO semaine = saisirSeanceControl.getForm().getSemaineSelectionne(); 
        if (semaine== null) { 
            return "";
        }
        return "du " + DateUtils.format(semaine.getLundi()) + " au " + DateUtils.format(semaine.getDimanche());  
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Sélectionne la ligne et redirection vers le sous écran de l'édition d'une
     * séance.
     *
     * @return outcome.
     */
    public String selectionner() {
        log.debug("----------------- MODIFIER -----------------");
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(IDFORM, form);
        form.getResultatSelectionne().setMode(AbstractForm.MODE_MODIF);
        return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(Outil.AJOUT_SEANCE,
                                                                            VisaSeanceControl.class.getName(),
                                                                            ObjectUtils.clone(form.getResultatSelectionne()));
    }


    /**
     * Appel métier pour supprimer une séance.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String supprimer() {
        log.debug("----------------- SUPPRESSION -----------------");
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(IDFORM, form);
        form.getResultatSelectionne().setMode(AbstractForm.MODE_DELETE);
        return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(Outil.AJOUT_SEANCE,
                                                                            VisaSeanceControl.class.getName(),
                                                                            ObjectUtils.clone(form.getResultatSelectionne()));
    }

   

    /**
     * Appel métier pour dupliquer une séance.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String dupliquer() {
        log.debug("----------------- DUPLIQUER -----------------");
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(IDFORM, form);
        form.getResultatSelectionne().setMode(AbstractForm.MODE_DUPLICATE);
        return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(Outil.AJOUT_SEANCE,
                                                                            VisaSeanceControl.class.getName(),
                                                                            ObjectUtils.clone(form.getResultatSelectionne()));
    }

    /**
     * @return the ajoutSeance
     */
    public AjoutSeanceControl getAjoutSeance() {
        return ajoutSeance;
    }

    /**
     * @param ajoutSeance the ajoutSeance to set
     */
    public void setAjoutSeance(AjoutSeanceControl ajoutSeance) {
        this.ajoutSeance = ajoutSeance;
    }

    /**
     * @param visaFacade the visaFacade to set
     */
    public void setVisaFacade(VisaFacadeService visaFacade) {
        this.visaFacade = visaFacade;
    }

    /**
     * @param etablissementService the etablissementService to set
     */
    public void setEtablissementService(EtablissementService etablissementService) {
        this.etablissementService = etablissementService;
    }

    /**
     * @param enseignementControl the enseignementControl to set
     */
    public void setEnseignementControl(EnseignementControl enseignementControl) {
        this.enseignementControl = enseignementControl;
    }

    /**
     * @param enseignantControl the enseignantControl to set
     */
    public void setEnseignantControl(EnseignantControl enseignantControl) {
        this.enseignantControl = enseignantControl;
    }

    /**
     * .
     */
    public void modeRemplacantChange() {
        log.debug("Mode remplaçement {}", form.getModeRemplacant());
        
        form.setListeSeance(new ArrayList<DateListeVisaSeanceDTO>());
        
        form.setVraiOuFauxRechercheActive(false);
    }
    
    /* (non-Javadoc)
     * @see org.crlr.web.application.control.EnseignementControl.EnseignementListener#enseignementTousOuUnSelectionne(boolean)
     */
    @Override
    public void enseignementTousOuUnSelectionne(boolean tous) {
        sequenceControl.getForm().setSequenceSelectionnee(null);
        sequenceControl.getForm().setTousSelectionne(true);
        
        rechercherListeSequenceSelectionnee();
        form.setVraiOuFauxRechercheActive(false);
        
        
    }
    
    

    /* (non-Javadoc)
     * @see org.crlr.web.application.control.EnseignementControl.EnseignementListener#enseignementSelectionnee()
     */
    @Override
    public void enseignementSelectionnee() {
        form.setListeSeance(new ArrayList<DateListeVisaSeanceDTO>());
        
        sequenceControl.getForm().setSequenceSelectionnee(null);
        sequenceControl.getForm().setTousSelectionne(true);
        
        rechercherListeSequenceSelectionnee();
        
        form.setVraiOuFauxRechercheActive(false);
        
    }

    /* (non-Javadoc)
     * @see org.crlr.web.application.control.EnseignantControl.EnseignantListener#enseignantTousOuUnSelectionne(boolean)
     */
    @Override
    public void enseignantTousOuUnSelectionne(boolean tous) {
        form.setListeSeance(new ArrayList<DateListeVisaSeanceDTO>());
        
        sequenceControl.getForm().setSequenceSelectionnee(null);
        sequenceControl.getForm().setTousSelectionne(true);
        
        rechercherListeSequenceSelectionnee();
        rechercherListeClasseGroupe();
        
        
        form.setVraiOuFauxRechercheActive(false);
        
    }

    /* (non-Javadoc)
     * @see org.crlr.web.application.control.EnseignantControl.EnseignantListener#enseignantSelectionnee()
     */
    @Override
    public void enseignantSelectionnee() {
        //Défauts
        classeGroupeControl.getForm().setTous(true);
        classeGroupeControl.getForm().setTypeGroupeSelectionne(null);
        classeGroupeControl.getForm().setGroupeClasseSelectionne(null);

        sequenceControl.getForm().setSequenceSelectionnee(null);
        sequenceControl.getForm().setTousSelectionne(true);
        
        enseignementControl.getForm().setTous(true);
        enseignementControl.getForm().setEnseignementSelectionne(null);
        
        rechercherEnseignement();
        rechercherListeSequenceSelectionnee();
        rechercherListeClasseGroupe();
        
        form.setListeSeance(new ArrayList<DateListeVisaSeanceDTO>());
        form.setVraiOuFauxRechercheActive(false);
        

        getForm().setModeRemplacant(false);
        
        if (null == enseignantControl.getForm().getEnseignantSelectionne()) {
            log.warn("Pas d'enseignant dans l'établissement");
            return;
        }            
        
        //Trouver si l'enseignant sélectionné était un remplaçant
        RechercheRemplacementQO rechercheRemplacementQO = new RechercheRemplacementQO();
        rechercheRemplacementQO.setIdEnseignantRemplacant(enseignantControl.getForm().getEnseignantSelectionne().getId());
        rechercheRemplacementQO.setIdEtablissement(ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getIdEtablissement());
        
        List<RemplacementDTO> liste =
                remplacementService.findListeRemplacement(rechercheRemplacementQO).getValeurDTO();
        
        List<EnseignantDTO> listeRemplace = Lists.newArrayList();
        Set<Integer> ensIds = Sets.newHashSet();
        
        log.debug("Liste remplacement taille {}", liste.size());
        
        for(RemplacementDTO rem : liste) {
            log.debug("Ens absent {}", rem.getEnseignantAbsent());
            if (!ensIds.contains(rem.getEnseignantAbsent().getId())) {
                ensIds.add(rem.getEnseignantAbsent().getId());
                listeRemplace.add(rem.getEnseignantAbsent());
            }
        }
        
        enseignantRemplaceControl.getForm().setListeEnseignant(listeRemplace);
        if (listeRemplace.size() > 0) {
            enseignantRemplaceControl.getForm().setEnseignantSelectionne(listeRemplace.get(0));
        }
        
    }

    /**
     * @param sequenceService the sequenceService to set
     */
    public void setSequenceService(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    /* (non-Javadoc)
     * @see org.crlr.web.application.control.SequenceControl.SequenceListener#sequenceTousOuUnSelectionne(boolean)
     */
    @Override
    public void sequenceTousOuUnSelectionne(boolean tous) {
        form.setVraiOuFauxRechercheActive(false);
        
    }

    /* (non-Javadoc)
     * @see org.crlr.web.application.control.SequenceControl.SequenceListener#sequenceSelectionnee()
     */
    @Override
    public void sequenceSelectionnee() {
        form.setVraiOuFauxRechercheActive(false);    
    }

    /**
     * @param sequenceControl the sequenceControl to set
     */
    public void setSequenceControl(SequenceControl sequenceControl) {
        this.sequenceControl = sequenceControl;
    }

    /**
     * Declencee si on clique sur un des filitre de l'etat de visee.
     */
    public void resetFiltreVisee() {
        form.setVraiOuFauxRechercheActive(false);
    }

    public void modificationFiltre() {
        
    }

    /**
     * @return the enseignantRemplaceControl
     */
    public EnseignantControl getEnseignantRemplaceControl() {
        return enseignantRemplaceControl;
    }

    /**
     * @param enseignantRemplaceControl the enseignantRemplaceControl to set
     */
    public void setEnseignantRemplaceControl(
            EnseignantControl enseignantRemplaceControl) {
        this.enseignantRemplaceControl = enseignantRemplaceControl;
    }

    /**
     * @return the remplacementService
     */
    public RemplacementService getRemplacementService() {
        return remplacementService;
    }

    /**
     * @param remplacementService the remplacementService to set
     */
    public void setRemplacementService(RemplacementService remplacementService) {
        this.remplacementService = remplacementService;
    }

}
