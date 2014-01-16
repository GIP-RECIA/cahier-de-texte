/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirControl.java,v 1.44 2010/06/02 12:41:27 ent_breyton Exp $
 */

package org.crlr.web.application.control.seance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.dto.Outil;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.DetailJourDTO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.dto.application.seance.ResultatRechercheSeanceDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.AnneeScolaireService;
import org.crlr.services.EtablissementService;
import org.crlr.services.SeanceService;
import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.control.AbstractPopupControl;
import org.crlr.web.application.control.ClasseGroupeControl;
import org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener;
import org.crlr.web.application.control.EnseignementControl;
import org.crlr.web.application.control.EnseignementControl.EnseignementListener;
import org.crlr.web.application.form.seance.SeanceSemaineForm;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.dto.JoursDTO;
import org.crlr.web.dto.MoisDTO;
import org.crlr.web.utils.ConverteurDTOUtils;
import org.springframework.util.CollectionUtils;

/**
 * Controleur de l'ecran de visualisation des devoirs. 
 * @author G-SAFIR-FRMP
 *
 */
@ManagedBean(name="seanceSemaine")
@ViewScoped
public class SeanceSemaineControl extends AbstractPopupControl<SeanceSemaineForm> 
implements ClasseGroupeListener, EnseignementListener {

    /** Service des Seance. */
    @ManagedProperty(value="#{seanceService}")
    private transient SeanceService seanceService;

    /** Service des anneeScolaire. */
    @ManagedProperty(value="#{anneeScolaireService}")
    private transient AnneeScolaireService anneeScolaireService;
    
    /** Le controleur des seances ajout . */
    @ManagedProperty(value="#{ajoutSeance}")
    private transient AjoutSeanceControl ajoutSeance;
    
    /** Service etablissement. */
    @ManagedProperty(value="#{etablissementService}")
    private transient EtablissementService etablissementService; 
    
    @ManagedProperty(value = "#{classeGroupe}")
    private transient ClasseGroupeControl classeGroupeControl;
    
    @ManagedProperty(value = "#{enseignement}")
    protected transient EnseignementControl enseignementControl;
    
    
    /**
     * Mutateur de ajoutSeance {@link #ajoutSeance}.
     * @param ajoutSeance le ajoutSeance to set
     */
    public void setAjoutSeance(AjoutSeanceControl ajoutSeance) {
        this.ajoutSeance = ajoutSeance;
    }
    
    /**
     * Mutateur de anneeScolaireService {@link #anneeScolaireService}.
     * @param anneeScolaireService le anneeScolaireService to set
     */
    public void setAnneeScolaireService(AnneeScolaireService anneeScolaireService) {
        this.anneeScolaireService = anneeScolaireService;
    }

    /**
     * Mutateur de seanceService {@link #seanceService}.
     * @param seanceService le seanceService to set
     */
    public void setSeanceService(SeanceService seanceService) {
        this.seanceService = seanceService;
    }

    /**
     * Mutateur de etablissementService {@link #etablissementService}.
     * @param etablissementService le etablissementService to set
     */
    public void setEtablissementService(EtablissementService etablissementService) {
        this.etablissementService = etablissementService;
    }

    /**
     * Constructeur.
     */
    public SeanceSemaineControl() {
        super(new SeanceSemaineForm());
        usePopupEnseignement = true;
    }

    /**
     * Methode de chargement du controleur.
     */
    @PostConstruct
    public void onLoad() {
        classeGroupeControl.setListener(this);
        enseignementControl.setListener(this);
        
        form.reset();        
        try {
            // Charge les jours ouvrés de l'etablissement
            chargerJoursOuvres();

            // Charge la liste des annee scolaire
            rechercherAnneeScolaire();
            
            // La barre des semaines
            alimenteBarreSemaine();
            
            // Charge la liste de enseignements proposes dans le filtre
            rechercherEnseignement();
            
            
            // Declenche la recherche
            final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
            if (Profil.ELEVE.equals(utilisateurDTO.getProfil()) || Profil.PARENT.equals(utilisateurDTO.getProfil())) {
                rechercher();
            }
        } catch (Exception e) {
            log.error( "Exception", e);
        }
        
    }
    
    /**
     * Declenche par l'IHM lors de la selection d'une annee scolaire.
     * @throws MetierException  e
     */
    public void selectionnerAnneeScolaire() throws MetierException {
        
        form.reset();
        
        // Charge les jours ouvrés de l'etablissement
        chargerJoursOuvres();

        // Charge la liste des annee scolaire
        rechercherAnneeScolaire();
        
        // La barre des semaines
        alimenteBarreSemaine();
        
        // Charge la liste de enseignements proposes dans le filtre
        rechercherEnseignement();
        
    }
    
    /**
     * Declenchee lors du changement de choix d'affichage. 
     */
    public void resetTypeAffichage() {
        if (form.getTypeAffichage().equals("LISTE")) {
            form.setDateDebut(form.getSemaineSelectionne().getLundi());
            form.setDateFin(form.getSemaineSelectionne().getDimanche());
        }
        getSeance();
    }
    
    /**
     * Charge les jours ouvré de l'etablissement.
     */
    private void chargerJoursOuvres() {
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        
        List<TypeJour> listeJoursOuvre = GenerateurDTO
                .getListeJourOuvreFromDb(utilisateurDTO
                        .getJoursOuvresEtablissement());
        
        form.setListeJoursOuvre(listeJoursOuvre);
        
        if (CollectionUtils.isEmpty(form.getListeSeance())) {
            return;
        }
        
        //Ajoute des jours si il y a une séance qui tombe sur un jour non ouvré
        for (final DetailJourDTO detail : form.getListeSeance()) {
            final TypeJour jourSeance = TypeJour.getTypeJourFromDate(detail.getDateSeance());
            if (!listeJoursOuvre.contains(jourSeance)) {
                listeJoursOuvre.add(jourSeance);
            }
        }
        
        Collections.sort(listeJoursOuvre);
        form.setListeJoursOuvre(listeJoursOuvre);
        
    }
    
    
    /**
     * Appel metier de recherche des devoirs en fonction de potentiels criteres.
     */
    public void rechercher() {
        log.debug("----------------- RECHERCHE -----------------");
        form.setListe(new ArrayList<JoursDTO>());
        form.setVraiOuFauxRechercheActive(true);
        getSeance();
    }
    
   

    /**
     * Fait appel a la fct en fonction du profil.
     */
    public void getSeance() {
        if (form.getVraiOuFauxRechercheActive()) {
            final Profil profil = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getProfil();
            if (Profil.ELEVE.equals(profil)) {
                getSeanceEleveFamille();
            } else if (Profil.PARENT.equals(profil)) {
                getSeanceEleveFamille();
            } else if (Profil.ENSEIGNANT.equals(profil)) {
                getSeanceEnseignant();
            } else if (Profil.DIRECTION_ETABLISSEMENT.equals(profil)) {
                getSeanceEnseignant();
            } else if (Profil.DOCUMENTALISTE.equals(profil)) {
                getSeanceEnseignant();
            } else if (Profil.INSPECTION_ACADEMIQUE.equals(profil)) {
                getSeanceEnseignant();
            }
            
            chargerJoursOuvres();
        }
    }
 
    /**
     * Retourne les devoirs pour les Ã©lÃ¨ves et la famille en fonction des date de
     * la semaine affichÃ©e.
     */
    private void getSeanceEleveFamille() {
        
        final RechercheSeanceQO rechercheSeanceQO = new RechercheSeanceQO();

        final Integer idEleve =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO()
                         .getIdentifiant();
        rechercheSeanceQO.setIdEleve(idEleve);

        if (form.getTypeAffichage().equals("SEMAINE")) {
            rechercheSeanceQO.setPremierJourSemaine(form.getSemaineSelectionne().getLundi());
            rechercheSeanceQO.setDernierJourSemaine(form.getSemaineSelectionne().getDimanche());
        } else {
            rechercheSeanceQO.setPremierJourSemaine(form.getDateDebut());
            rechercheSeanceQO.setDernierJourSemaine(form.getDateFin());
        }
        rechercheSeanceQO.setJourCourant(DateUtils.getAujourdhui());

        if (enseignementControl.getForm().getEnseignementSelectionne() != null ) {
            rechercheSeanceQO.setIdEnseignement(enseignementControl.getForm().getEnseignementSelectionne().getId());
        }
        
        
        ResultatDTO<List<ResultatRechercheSeanceDTO>> resultat =
            new ResultatDTO<List<ResultatRechercheSeanceDTO>>();
        try {
            resultat = seanceService.listeSeanceAffichage(rechercheSeanceQO);
            
            final List<DetailJourDTO> liste = new ArrayList<DetailJourDTO>();
            
            if(resultat != null) {
                for (final ResultatRechercheSeanceDTO resultatRechercheSeanceDTO : resultat.getValeurDTO()) {
                    final DetailJourDTO dev = new DetailJourDTO();
                    dev.setSeance(resultatRechercheSeanceDTO);
                    dev.setHeureSeance(String.format("%dh%02d - %dh%02d",
                            resultatRechercheSeanceDTO.getHeureDebut(), resultatRechercheSeanceDTO.getMinuteDebut(),
                            resultatRechercheSeanceDTO.getHeureFin(), resultatRechercheSeanceDTO.getMinuteFin()
                    ));
                    dev.setMatiere(resultatRechercheSeanceDTO.getDesignationEnseignement());
                    dev.setDenomination(resultatRechercheSeanceDTO.getEnseignantDTO().getCivilite());
                    dev.setNom(resultatRechercheSeanceDTO.getEnseignantDTO().getNom());
                    generateDescriptionAbrege(dev);
                    liste.add(dev);
                }
            }
            form.setListe(ConverteurDTOUtils.convertWeekCalendar(liste, true));
            form.setListeSeance(liste);
        } catch (final MetierException e) {
            log.debug("{0}", e.getMessage());
            form.setVraiOuFauxRechercheActive(false);
        }
    }

    /**
     * Retourne les devoirs pour les enseignants en fonction des date de la
     * semaine affichÃ©e.
     */
    private void getSeanceEnseignant() {
        final RechercheSeanceQO rechercheSeanceQO = new RechercheSeanceQO();

        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        final Integer idUtilisateur = utilisateurDTO.getUserDTO()
        .getIdentifiant();
        
        final Boolean vraiOuFauxEnseignant = Profil.ENSEIGNANT.equals(ContexteUtils.getContexteUtilisateur()
        .getUtilisateurDTO().getProfil());
        
        // En mode archive, le id etablissement peut etre different
        final Integer idEtablissement = getIdEtablissementArchive(form.getModeArchive(), form.getExercice());
        
        if (utilisateurDTO.getProfil().equals(Profil.INSPECTION_ACADEMIQUE)){
            rechercheSeanceQO.setIdInspecteur(idUtilisateur);
        } 
        
        if (form.getModeArchive()) {
            rechercheSeanceQO.setArchive(true);
            rechercheSeanceQO.setIdAnneeScolaire(form.getIdAnneeScolaire());
            rechercheSeanceQO.setExerciceAnneeScolaire(form.getExercice());
        }

        rechercheSeanceQO.setIdEtablissement(idEtablissement);
        rechercheSeanceQO.setIdEnseignant(idUtilisateur);
        rechercheSeanceQO.setGroupeClasseSelectionne(classeGroupeControl.getForm().getGroupeClasseSelectionne());
        
        //Si il n'y a pas une classe / un group sélectionné, il faut mettre le type pour les controls de métier
        rechercheSeanceQO.setTypeGroupe(classeGroupeControl.getForm().getTypeGroupeSelectionne());
        
        if (form.getTypeAffichage().equals("SEMAINE")) {
            rechercheSeanceQO.setPremierJourSemaine(form.getSemaineSelectionne().getLundi());
            rechercheSeanceQO.setDernierJourSemaine(form.getSemaineSelectionne().getDimanche());
        } else {
            rechercheSeanceQO.setPremierJourSemaine(form.getDateDebut());
            rechercheSeanceQO.setDernierJourSemaine(form.getDateFin());
        }
        rechercheSeanceQO.setJourCourant(DateUtils.getAujourdhui());
        
        
        
        rechercheSeanceQO.setListeGroupeDTO( classeGroupeControl.getForm().getListeGroupe() );
        
        if (enseignementControl.getForm().getEnseignementSelectionne() != null ) {
            rechercheSeanceQO.setIdEnseignement(enseignementControl.getForm().getEnseignementSelectionne().getId());
        }
        
        final ContexteUtilisateur contextUtilisateur = ContexteUtils.getContexteUtilisateur();
        
        rechercheSeanceQO.setIdEnseignantConnecte(
                contextUtilisateur.getUtilisateurDTOConnecte().getUserDTO().getIdentifiant());
        

        ResultatDTO<List<ResultatRechercheSeanceDTO>> resultat =
            new ResultatDTO<List<ResultatRechercheSeanceDTO>>();
        try {
            
            resultat = seanceService.listeSeanceAffichage(rechercheSeanceQO);

            final List<DetailJourDTO> liste = new ArrayList<DetailJourDTO>();

            if(resultat != null) {
                for (final ResultatRechercheSeanceDTO resultatRechercheSeanceDTO : resultat.getValeurDTO()) {
                    final DetailJourDTO dev = new DetailJourDTO();
                    dev.setHeureSeance(String.format("%dh%02d - %dh%02d",
                            resultatRechercheSeanceDTO.getHeureDebut(), resultatRechercheSeanceDTO.getMinuteDebut(),
                            resultatRechercheSeanceDTO.getHeureFin(), resultatRechercheSeanceDTO.getMinuteFin()
                    ));
                    dev.setSeance(resultatRechercheSeanceDTO);
                    
                    dev.setMatiere(resultatRechercheSeanceDTO.getDesignationEnseignement());
                    dev.setDenomination(resultatRechercheSeanceDTO.getEnseignantDTO().getCivilite());
                    dev.setNom(resultatRechercheSeanceDTO.getEnseignantDTO().getNom());
                    
                    
                    liste.add(dev);
                }
            }
            form.setListeSeance(liste);
            form.setListe(ConverteurDTOUtils.convertWeekCalendar(liste, true));
        } catch (final MetierException e) {
            log.debug("{0}", e.getMessage());
            form.setVraiOuFauxRechercheActive(false);
        }
    }

    /**
     * Génère la description abrégée visible dans le listing de devoir.
     * @param devoirDTO le devoir courant.
     */
    private void generateDescriptionAbrege(final DetailJourDTO devoirDTO) {
        /*final String descriptionFinale = 
            StringUtils.truncate(StringUtils.removeBalise(devoirDTO.getDescription()), 25, "..");*/                            
        
        final String descriptionFinale = org.crlr.utils.StringUtils
                .truncateHTMLString(org.crlr.utils.StringUtils
                        .removeBalise(devoirDTO.getDescription()), 25);

        final String descriptionInfoBulle = StringEscapeUtils
                .unescapeHtml(org.crlr.utils.StringUtils.truncate(org.crlr.utils.StringUtils
                        .removeBalise(devoirDTO.getDescription()), 200,
                        ".."));

        devoirDTO.setDescriptionSansBaliseAbrege(descriptionFinale);
        devoirDTO.setDescriptionSansBalise(descriptionInfoBulle);
    }
    
    /**
     * Alimente la liste des semaines et mois.
     * @throws MetierException exception
     */
    private void alimenteBarreSemaine() throws MetierException {
        // Charge la barre des semaines / mois
        
        final ContexteUtilisateur ctxUtilisateur = ContexteUtils.getContexteUtilisateur(); 
        final UtilisateurDTO utilisateurDTO = ctxUtilisateur.getUtilisateurDTO();
        
        AnneeScolaireDTO anneeScolaireDTO = ObjectUtils.clone(utilisateurDTO.getAnneeScolaireDTO());
        if (form.getModeArchive()) {
            for (final AnneeScolaireDTO annee : form.getListeAnneeScolaire()) {
                if (annee.getId().equals(form.getIdAnneeScolaire())) {
                    anneeScolaireDTO = annee;
                    
                    ajoutSeance.getForm().setModeArchive(true);
                    ajoutSeance.getForm().setExercice(annee.getExercice());
                    
                    break;
                }
            }
        }
        
        final String alternanceSemaine =  etablissementService.findAlternanceSemaine(utilisateurDTO.getIdEtablissement());
        final Boolean vraiOuFauxAlternance = !StringUtils.isEmpty(alternanceSemaine);
        final List<BarreSemaineDTO> listeSemaine = 
            GenerateurDTO.generateListeSemaine(anneeScolaireDTO, alternanceSemaine, vraiOuFauxAlternance); 
        form.setListeBarreSemaine(listeSemaine);
        form.setListeMois( 
                GenerateurDTO.genererListeMois(anneeScolaireDTO.getDateRentree(),
                        anneeScolaireDTO.getDateSortie()));        
        form.setDecalageDebutAnnee(MoisDTO.getDecalageDebutAnnee(anneeScolaireDTO.getDateRentree()));
        form.setFinAnneeScolaire(anneeScolaireDTO.getDateSortie());
        form.setSemaineSelectionne(GenerateurDTO.selectionnerSemaineJour(form.getListeBarreSemaine()));
    }
 
   
    
    /**
     * Methode appeles lors de la selection d'une semaine dans la barre des semaines.
     */
    public void selectionnerSemaine() {
        getSeance();
    }
    
    /**
     * Voir le detail d'un devoir.
     * Charge le devoir grace a son id.
     */
    public void naviguerDetailSeance() {
        final Integer idSeance = form.getSelectSeance().getIdSeance();
        
        // Lance la recherche et alimente dans le form de ajout seance la seance selectionnee.
        //=> la seance dans ajoutSeance.form.seance contient la seanceDTO à afficher dans la popup de detail.
        ajoutSeance.alimenterSeance(idSeance);
    }

    /**
     * Charge la liste des annees scolaire si on est en mode archive.
     */
    private void rechercherAnneeScolaire() {
        final ContexteUtilisateur ctxUtilisateur = ContexteUtils.getContexteUtilisateur(); 
        if (ctxUtilisateur.getOutil().equals(Outil.CAHIER_ARCHIVE)) {
            form.setModeArchive(true);
            classeGroupeControl.getForm().setArchive(true);
            try {
                final ResultatDTO<List<AnneeScolaireDTO>> resultatDTO = anneeScolaireService.findListeAnneeScolaire();
                final List<AnneeScolaireDTO> listAnneeScolaire = resultatDTO.getValeurDTO();
                form.setListeAnneeScolaire(listAnneeScolaire);
                if (listAnneeScolaire!=null && listAnneeScolaire.size()>0) {
                    form.setIdAnneeScolaire(listAnneeScolaire.get(0).getId());
                    classeGroupeControl.getForm().setExercice(listAnneeScolaire.get(0).getExercice());
                    
                    ajoutSeance.getForm().setModeArchive(true);
                    ajoutSeance.getForm().setExercice(listAnneeScolaire.get(0).getExercice());
                }
            } catch (MetierException e) {
                log.error("Erreur de chargement des années scolaire", e.toString());
                return;
            }
        } else {
            form.setModeArchive(false);
            form.setListeAnneeScolaire(new ArrayList<AnneeScolaireDTO>());
            classeGroupeControl.getForm().setArchive(false);
        }
        // Recharge les classe groupes pour l'annnee cible
        classeGroupeControl.classeGroupeTypeSelectionne(null);
    }
    
    /**
     * Recherche dans l'archive le id de l'etablissement. Si on est pas 
     * en mode archive, on retourne l'id courant.
     * @param archive si on est en mode archive
     * @param exercice exercice 
     * @return l'id 
     */
    private Integer getIdEtablissementArchive(Boolean archive, String exercice) {
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        if (archive==null || !archive || exercice==null) {
            return utilisateurDTO.getIdEtablissement();
        }
        return etablissementService.findIdEtablissementParCode(utilisateurDTO.getSirenEtablissement(), archive, exercice);
    }
    
    /**
     * Appel métier de recherche des enseignements.
     */
    
    private void rechercherEnseignement() {
        
        enseignementControl.chargerListeEnseignement(classeGroupeControl
                .getForm().getGroupeClasseSelectionne(), classeGroupeControl
                .getForm().getTypeGroupeSelectionne(), false, form
                .getModeArchive(), form.getExercice(), classeGroupeControl
                .getForm().getListeGroupe());
        
    }


    /**
     * @param classeGroupeControl the classeGroupeControl to set
     */
    public void setClasseGroupeControl(ClasseGroupeControl classeGroupeControl) {
        this.classeGroupeControl = classeGroupeControl;
    }


    /** 
     * .
     * @see org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener#classeGroupeTypeSelectionne()
     */
    @Override
    public void classeGroupeTypeSelectionne() {
        form.setListe(new ArrayList<JoursDTO>());
        form.setListeSeance(new ArrayList<DetailJourDTO>());
        form.setVraiOuFauxRechercheActive(false);
        
        enseignementControl.getForm().setEnseignementSelectionne(null);
        enseignementControl.getForm().setFiltreParEnseignement(false);
        
    }


    /**
     * 
     * .
     * @see
     * org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener
     * #classeGroupeSelectionnee()
     */
    @Override
    public void classeGroupeSelectionnee() {

        form.setListe(new ArrayList<JoursDTO>());
        form.setListeSeance(new ArrayList<DetailJourDTO>());

        // Rafraichit la liste des enseignement dispo
        enseignementControl.getForm().setEnseignementSelectionne(null);
        enseignementControl.getForm().setFiltreParEnseignement(false);
        rechercherEnseignement();

        form.setVraiOuFauxRechercheActive(false);

    }

    /**
     * @param enseignementControl the enseignementControl to set
     */
    public void setEnseignementControl(EnseignementControl enseignementControl) {
        this.enseignementControl = enseignementControl;
    }

    /* (non-Javadoc)
     * @see org.crlr.web.application.control.EnseignementControl.EnseignementListener#enseignementTousOuUnSelectionne(boolean)
     */
    @Override
    public void enseignementTousOuUnSelectionne(boolean tous) {
        form.setListe(new ArrayList<JoursDTO>());
        form.setListeSeance(new ArrayList<DetailJourDTO>());
        form.setVraiOuFauxRechercheActive(false);
        
        
        
    }

    /* (non-Javadoc)
     * @see org.crlr.web.application.control.EnseignementControl.EnseignementListener#enseignementSelectionnee()
     */
    @Override
    public void enseignementSelectionnee() {
        form.setListe(new ArrayList<JoursDTO>());
        form.setListeSeance(new ArrayList<DetailJourDTO>());
        form.setVraiOuFauxRechercheActive(false);
        
    }
}
