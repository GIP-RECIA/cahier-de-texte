/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AdminControl.java,v 1.13 2010/06/08 12:24:06 ent_breyton Exp $
 */

package org.crlr.web.application.control.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.crlr.dto.application.admin.DatePeriodeQO;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.DateAnneeScolaireQO;
import org.crlr.dto.application.base.EnseignementQO;
import org.crlr.dto.application.base.EtablissementComplementDTO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.GenericDTO;
import org.crlr.dto.application.base.GenericTroisDTO;
import org.crlr.dto.application.base.OuvertureQO;
import org.crlr.dto.application.base.PeriodeVacanceQO;
import org.crlr.dto.application.base.TypeCategorieTypeDevoir;
import org.crlr.dto.application.base.TypeReglesAdmin;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.metier.facade.EtablissementFacade;
import org.crlr.services.AnneeScolaireService;
import org.crlr.services.DevoirService;
import org.crlr.services.EnseignementService;
import org.crlr.services.EtablissementService;
import org.crlr.utils.BooleanUtils;
import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.control.AbstractControl;
import org.crlr.web.application.control.MenuControl;
import org.crlr.web.application.form.admin.AdminForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.dto.GrilleHoraireDTO;
import org.crlr.web.dto.TypeOngletAdmin;
import org.crlr.web.dto.TypeSemaine;
import org.crlr.web.utils.MessageUtils;
import org.richfaces.event.ItemChangeEvent;

/**
 * AdminControl.
 *
 * @author breytond
 * @version $Revision: 1.13 $
 */
@ManagedBean(name="admin")
@ViewScoped
public class AdminControl extends AbstractControl<AdminForm> {
    
    /** Service annee scolaire. */
    @ManagedProperty(value="#{anneeScolaireService}")
    private transient AnneeScolaireService anneeScolaireService; 
    
    /** Service etablissement. */
    @ManagedProperty(value="#{etablissementService}")
    private transient EtablissementService etablissementService; 
    
    /** Service enseignement. */
    @ManagedProperty(value="#{enseignementService}")
    private transient EnseignementService enseignementService;
    
    /** Service devoir. */
    @ManagedProperty(value="#{devoirService}")
    private transient DevoirService devoirService;
    
    /**
     * Injection Spring de anneeScolaireService.
     * @param anneeScolaireService le anneeScolaireService à modifier.
     */
    public void setAnneeScolaireService(AnneeScolaireService anneeScolaireService) {
        this.anneeScolaireService = anneeScolaireService;
    }
    
    /**
     * Injection Spring de etablissementService.
     * @param etablissementService le etablissementService à modifier.
     */
    public void setEtablissementService(EtablissementService etablissementService) {
        this.etablissementService = etablissementService;
    }
    
    /**
     * Injection Spring de enseignementService.
     * @param enseignementService le enseignementService à modifier.
     */
    public void setEnseignementService(EnseignementService enseignementService) {
        this.enseignementService = enseignementService;
    }
    
    /**
     * Injection Spring de devoirService.
     * @param devoirService le devoirService à modifier.
     */
    public void setDevoirService(DevoirService devoirService) {
        this.devoirService = devoirService;
    }

/**
     * Constructeur.
     */
    public AdminControl() {
        super(new AdminForm());        
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    public void onLoad() {
        //permet de faire un seule appel métier durant le cycle de l'outil.
        //valable pour les outils dont les paramètres ne sont pas saisies par l'utilisateur.
        
        //gestion des droits d'accès aux onglet
        gestionAccesOnglet();
        
        //appel métier doit être fait uniquement sur l'onglet actif
        gestionnaireChargementOnglet();
        log.debug("une fois test onload !!");
    
        log.debug("ONGLET selectionné {0}", form.getOngletSelectionne());
    }
    
    /**
     * Si l'utilisateur est administrateur de ressource, il a alors accès à l'ensemble des onglets. 
     * S'il est adm local il a alors accès uniquement au paramétrage de son établissement.
     */
    private void gestionAccesOnglet () {
        final Map<String,Boolean> mapAccesOnglet = form.getMapAccesOnglet();
        
        final UtilisateurDTO utilisateurDTO = obtenirSessionUtilisateurDTO();
    
        final String sirenSel = utilisateurDTO.getSirenEtablissement();
        final Boolean vraiOuFauxAccesAdmLoc = utilisateurDTO.getAdminLocalSiren().contains(sirenSel);        
        final Boolean vraiOuFauxAccesAdmRess = utilisateurDTO.getAdminRessourceSiren().contains(sirenSel);
        final Boolean vraiOuFauxAdmRessEnt = utilisateurDTO.getVraiOuFauxAdmRessourceENT();
        final Boolean vraiOuFauxAdmCentral = utilisateurDTO.getVraiOuFauxAdmCentral();
        if (vraiOuFauxAccesAdmLoc || vraiOuFauxAccesAdmRess || vraiOuFauxAdmRessEnt || vraiOuFauxAdmCentral) {
            if (vraiOuFauxAdmRessEnt || vraiOuFauxAdmCentral) {
                mapAccesOnglet.put(TypeOngletAdmin.ANNEE_SCOLAIRE.name(), true);
                mapAccesOnglet.put(TypeOngletAdmin.GRILLE_HORAIRE.name(), true);
                mapAccesOnglet.put(TypeOngletAdmin.JOURS_OUVRES.name(), true);
                mapAccesOnglet.put(TypeOngletAdmin.LIBELLES.name(), true);
                mapAccesOnglet.put(TypeOngletAdmin.OUVERTURE.name(), true);
                
                //selectionne le premier onglet accessible pour le profil
                form.setOngletSelectionne(TypeOngletAdmin.ANNEE_SCOLAIRE.name());
            } else {
                mapAccesOnglet.put(TypeOngletAdmin.GRILLE_HORAIRE.name(), true);
                mapAccesOnglet.put(TypeOngletAdmin.JOURS_OUVRES.name(), true);
                mapAccesOnglet.put(TypeOngletAdmin.LIBELLES.name(), true);
                mapAccesOnglet.put(TypeOngletAdmin.OUVERTURE.name(), true);
                
                //selectionne le premier onglet accessible pour le profil si l'étabblissement est fermé
                if (!BooleanUtils.isTrue(utilisateurDTO.getVraiOuFauxCahierOuvertEtab())) {
                    form.setOngletSelectionne(TypeOngletAdmin.JOURS_OUVRES.name());
                } else {
                    form.setOngletSelectionne(TypeOngletAdmin.LIBELLES.name());
                }
                
                //le cahier de texte n'est pas ouvert aux établissements.
                if (!BooleanUtils.isTrue(form.getVraiOuFauxOuvertEntSource())) {
                    MessageUtils.addMessage(new Message(TypeReglesAdmin.ADMIN_16.name()), getClass());
                }
            }
        } else {
            mapAccesOnglet.put(TypeOngletAdmin.ANNEE_SCOLAIRE.name(), false);
            mapAccesOnglet.put(TypeOngletAdmin.GRILLE_HORAIRE.name(), false);
            mapAccesOnglet.put(TypeOngletAdmin.JOURS_OUVRES.name(), false);
            mapAccesOnglet.put(TypeOngletAdmin.LIBELLES.name(), false);
            mapAccesOnglet.put(TypeOngletAdmin.OUVERTURE.name(), false);
            
            form.setOngletSelectionne(TypeOngletAdmin.OUVERTURE.name());
        }
    }
    
    ///////////////////////////////////////Onglet Annee scolaire 
    
    /**
     * Retourne l'année scolaire en session.
     * @return le dto.
     */
    private AnneeScolaireDTO obtenirSessionAnneeScolaireDTO() {
        return ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getAnneeScolaireDTO();
    }
    
    /**
     * Sélectionne le mois depuis la barre de mois et génére le calendrier des jours chomés.
     */
    public void selectionneBarreMois() {
        log.debug("selection barre mois {0}", form.getDateMoisSelectionne());

        final AnneeScolaireDTO anneeScolaireDTO = obtenirSessionAnneeScolaireDTO();  
        
        form.setListeVancance(GenerateurDTO.genererVacanceCalendar(anneeScolaireDTO.getDateRentree(), 
                anneeScolaireDTO.getDateSortie(), DateUtils.parse(form.getDateMoisSelectionne()), 
                form.getListeJourOuvreGlobale(), GenerateurDTO.generateSetPeriodeVancanceFromDb(anneeScolaireDTO.getPeriodeVacances())));        

        form.setListeMoisAnneeSco( 
                GenerateurDTO.genererBarreMois(anneeScolaireDTO.getDateRentree(),
                        anneeScolaireDTO.getDateSortie(), form.getDateMoisSelectionne()));        
        
        form.setNomMethodeSauvegarde("saveAlternancesSemaines");
    }
    
    /**
     * Appel du service métier de sauvegarde de l'année scolaire.
     */
    public void saveAnneeScolaire() {
        try {
            anneeScolaireService.saveAnneeScolaire(form.getDateAnneeScolaireQO());
            //mise à jour du contexte utilisateur
            final AnneeScolaireDTO anneeScolaireDTO = obtenirSessionAnneeScolaireDTO();
            final DateAnneeScolaireQO dateAnneeScolaireQO = ObjectUtils.clone(form.getDateAnneeScolaireQO());
            anneeScolaireDTO.setDateRentree(dateAnneeScolaireQO.getDateRentree());
            anneeScolaireDTO.setDateSortie(dateAnneeScolaireQO.getDateSortie());
            
            //navigation
            form.setDateMoisSelectionne(DateUtils.formatDateAuPremierJourDuMois(dateAnneeScolaireQO.getDateRentree()));           
            selectionneBarreMois();
        } catch (MetierException e) {
            log.debug("Erreur de sauvegarde de l'année scolaire : {0}.", e.getMessage()); 
        }
    }
    
    /**
     * Sauvegarde de la plage de date de jours chomés séléctionnée.
     */
    public void ajouterPlageFerie() {
        final Date dateDebut = (form.getDateDebutFerie() != null) ? ((Date)form.getDateDebutFerie().clone()) : null;
        final Date dateFin = (form.getDateFinFerie() != null) ? ((Date)form.getDateFinFerie().clone()) : null;
        try {
            final AnneeScolaireDTO anneeScolaireDTO = obtenirSessionAnneeScolaireDTO();
            
            // Check regle de non nullité et de cohérence des dates et de chevauchement de plage.
            final DatePeriodeQO datePeriodeQO = new DatePeriodeQO();
            datePeriodeQO.setDateDebutPlage(dateDebut);
            datePeriodeQO.setDateFinPlage(dateFin);
            datePeriodeQO.setPlageExistante(anneeScolaireDTO.getPeriodeVacances());
            datePeriodeQO.setDateRentree(anneeScolaireDTO.getDateRentree());
            datePeriodeQO.setDateSortie(anneeScolaireDTO.getDateSortie());
            anneeScolaireService.checkDatesPeriode(datePeriodeQO);
            
            final List<GenericDTO<Date, Date>> listeClonee = 
                    ObjectUtils.clone(form.getListeJourFerie());
        
            listeClonee.add(new GenericDTO<Date, Date>(dateDebut, dateFin));
           
            //listeClonee au cas ou un échec de persitence
            savePeriodeVacance(listeClonee);            
            
            form.setListeJourFerie(listeClonee);
            
            //reset
            form.setDateDebutFerie(null);
            form.setDateFinFerie(null);
            
            final Date dateDebutMoisSel = (Date)dateDebut.clone();
            //navigation
            form.setDateMoisSelectionne(DateUtils.formatDateAuPremierJourDuMois(dateDebutMoisSel));
            selectionneBarreMois();
        } catch (MetierException e) {
            log.debug("Erreur de sauvegarde des périodes de vacances : {0}", e.getMessage()); 
        }
    }
    
    /**
     * Supprime une plage de jours vaqués.
     */
    public void supprimerPlageFerie() {      
        form.getListeJourFerie().remove(form.getPlageJourFerieSelectionne());
        try {
            savePeriodeVacance(form.getListeJourFerie());
            
            //navigation
            form.setDateMoisSelectionne(DateUtils.formatDateAuPremierJourDuMois(form.getPlageJourFerieSelectionne().getValeur1()));
            selectionneBarreMois();
        } catch (MetierException e) {
            log.debug("Erreur de sauvegarde des périodes de vacances : {0}", e.getMessage()); 
        }
    }
    
    /**
     * Appel métier de la sauvegarde des jours indisponibles (chomés).
     * @param listePlageIndispo la nouvelle liste de plage.
     * @throws MetierException l'exception potentielle.
     * 
     */
    private void savePeriodeVacance(final List<GenericDTO<Date, Date>> listePlageIndispo) throws MetierException {      
      
            final String chainePeriode = GenerateurDTO.generatePeriodeVacanceToDb(listePlageIndispo);
            final PeriodeVacanceQO periodeVacanceQO = form.getPeriodeVacanceQO();            
            periodeVacanceQO.setPeriodeVacances(chainePeriode);
            anneeScolaireService.savePeriodeVacance(periodeVacanceQO);
            //mise en session
            obtenirSessionAnneeScolaireDTO().setPeriodeVacances(chainePeriode);
       
    }
    
    /**
     * Appel du service métier de sauvegarde de l'ouverture ENT.
     */
    public void saveOuvertureENT() {
        final OuvertureQO ouvertureQO = form.getOuvertureQO();
        try {
            anneeScolaireService.saveOuvertureENT(ouvertureQO);
            //mise à jour du contexte utilisateur
            final Boolean vraiOuFauxOuvert = ouvertureQO.getVraiOuFauxOuvert();
            final AnneeScolaireDTO anneeScolaireDTO = obtenirSessionAnneeScolaireDTO();            
            anneeScolaireDTO.setVraiOuFauxCahierOuvertENT(vraiOuFauxOuvert);
            form.setVraiOuFauxOuvertEntSource(vraiOuFauxOuvert);
            //réinitialise le menu
            final MenuControl menuControl = ContexteUtils.getMenuControl();           
            menuControl.init();
        } catch (MetierException e) {
            ouvertureQO.setVraiOuFauxOuvert(!ouvertureQO.getVraiOuFauxOuvert());
            log.debug("Erreur de sauvegarde de l'ouverture aux établissements: {0}.", e.getMessage()); 
        }
    }
    
    
    ///////////////////////////////////////Onglet grille horaire
    
    /**
     * Retourne l'utilisateurDTO en session.
     * @return le dto.
     */
    private UtilisateurDTO obtenirSessionUtilisateurDTO() {
        return ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
    }  
    
    /**
     * Initalise la grille horaire et les valeurs permettant de la générer.
     */
    public void initialiseValeurEtGrilleHoraire() {
        final EtablissementComplementDTO etablissementComplementDTO = 
            etablissementService.findDonneeComplementaireEtablissement(obtenirSessionUtilisateurDTO().getIdEtablissement());
        
        form.setDureeCours(ObjectUtils.defaultIfNull(etablissementComplementDTO.getDureeCours(), form.getDureeCours()));
        form.setHeureDebut(ObjectUtils.defaultIfNull(etablissementComplementDTO.getHeureDebutCours(), form.getHeureDebut()));
        form.setMinuteDebut(ObjectUtils.defaultIfNull(etablissementComplementDTO.getMinuteDebutCours(), form.getMinuteDebut()));
        form.setHeureFin(ObjectUtils.defaultIfNull(etablissementComplementDTO.getHeureFinCours(), form.getHeureFin()));
        form.setFractionnement(etablissementComplementDTO.getFractionnement());
        
        if (etablissementComplementDTO.getHoraireCours() != null) {
            form.setHorairesCours(GenerateurDTO.generateGrilleHoraireFromDb(etablissementComplementDTO.getHoraireCours()));
        } else {
            form.setHorairesCours(new ArrayList<GrilleHoraireDTO>());
        }
        
        form.setNomMethodeSauvegarde("saveGrilleHoraire");
    }
    
    /**
     * Génère la grille horaire.
     */
    public void genererGrilleHoraire() {

        // Initialisation
        final List<GrilleHoraireDTO> listeHoraires = new ArrayList<GrilleHoraireDTO>();
        final Integer dureeCours = form.getDureeCours();
        
        // Initialise la boucle
        Integer heureCourante = form.getHeureDebut();            
        Integer minuteCourante = form.getMinuteDebut();        
        
        // Boucle jusqu'à la dernière heure
        while (!form.getHeureFin().equals(heureCourante)) {
            
            // Initialise une nouvelle plage
            final GrilleHoraireDTO plage = new GrilleHoraireDTO();
            plage.setHeureDebut(heureCourante);
            plage.setMinuteDebut(minuteCourante);
            plage.setHeureFin((heureCourante*60 + minuteCourante + dureeCours) / 60 );
            plage.setMinuteFin((heureCourante*60 + minuteCourante + dureeCours) % 60);
            
            // Ajoute la plage
            listeHoraires.add(plage);
            
            // Passe à la plage suivante
            heureCourante = plage.getHeureFin();
            minuteCourante = plage.getMinuteFin();
        }
        
        
        // Positionne la liste dans le form
        form.setHorairesCours(listeHoraires);        
    }
    
    /**
     * Sauvegarde de la grille horaire.
     */
    public void saveGrilleHoraire() {
        final EtablissementDTO etablissementQO = new EtablissementDTO();
        etablissementQO.setId(obtenirSessionUtilisateurDTO().getIdEtablissement());
        etablissementQO.setHoraireCours(GenerateurDTO.generateGrilleHoraireToDb(form.getHorairesCours()));
        etablissementQO.setDureeCours(form.getDureeCours());
        etablissementQO.setHeureDebut(form.getHeureDebut());
        etablissementQO.setMinuteDebut(form.getMinuteDebut());
        etablissementQO.setHeureFin(form.getHeureFin());
        etablissementQO.setFractionnement(form.getFractionnement());
        try {
            etablissementService.saveComplementEtablissement(etablissementQO);
        } catch (MetierException e) {
            log.debug("Erreur de sauvegarde de la grille horaire {0}", e.getMessage()); 
        }
    }

    /**
     * Methode appele par le a4j:jsFunction lors de la sélection d'une plage à modifier / supprimer.
     * Positionne sur le formulaire la plage à éditer.
     */
    public void chargerPlageSelected() {
        log.info("chargerPlageSelected");
        
        
        
        final Integer index = form.getPlageSelectedIndex();
        
        //Création
        if (index == form.getHorairesCours().size()) {
            log.info("Ajoute un nouveau DTO {0}", index);

            GrilleHoraireDTO nouveauObject = new GrilleHoraireDTO();

            form.getHorairesCours().add(nouveauObject);

        }
        
        final GrilleHoraireDTO plage = form.getHorairesCours().get(index);
        form.setPlageEditee(ObjectUtils.clone(plage));
        
        log.info(" chargerPlageSelected Selected index {0}, total grille {1}, debut {2}:{3}, fin {4}:{5}.  obj {6} obj {7}", 
                form.getPlageSelectedIndex(),
                form.getHorairesCours().size(), 
                form.getPlageEditee().getHeureDebut(),
                form.getPlageEditee().getMinuteDebut(),
                form.getPlageEditee().getHeureFin(),
                form.getPlageEditee().getMinuteFin(),
                form.getHorairesCours().get(form.getPlageSelectedIndex()),
                form.getPlageEditee()
                );
    }
    
    /**
     * Methode appele par le a4j:jsFunction lors de l'édition d'une plage à modifier / supprimer.
     * Positionne sur le formulaire la plage à éditer : fait un clone.
     */
    public void editerPlageSelected() {
        log.info("editerPlageSelected");
        
        final Integer index = form.getPlageSelectedIndex();
        final GrilleHoraireDTO plage = ObjectUtils.clone(form.getHorairesCours().get(index));
        form.setPlageEditee(plage);
    }
    
    
    
    /**
     * Mutateur de plageSelectedIndex. 
     * @param plageSelectedIndex le plageSelectedIndex to set
     */
    public void setPlageSelectedIndex(Integer plageSelectedIndex) {
        this.form.setPlageSelectedIndex(plageSelectedIndex);
    }
    
    
    //////////////////////////////////////////////onglet JOURS_OUVRES
    
    /**
     * Initialise les jours ouvrés ou vaqués.
     */
    private void initialiserBarreJourOuvre() {
        form.setListeOuvre(GenerateurDTO.generateListeJourOuvreFerieFromDb(obtenirSessionUtilisateurDTO().getJoursOuvresEtablissement()));
    }
    
    /**
     * Sauvegarde des jours ouvrés ou vaqués.
     */
    public void saveJourOuvre() {
        final List<GenericTroisDTO<String, String, Boolean>> listeJour = 
                ObjectUtils.clone(form.getListeOuvre());        
        
        //passe le jour a ouvré ou vaqué.
        for (final GenericTroisDTO<String, String, Boolean> jour : listeJour) {
            if (StringUtils.equals(jour.getValeur1(), form.getLibelleJourOuvreSel())) {
                jour.setValeur3(!jour.getValeur3());
                break;
            }
        }
        
        final UtilisateurDTO utilisateurDTO = obtenirSessionUtilisateurDTO();
       
        final EtablissementDTO etablissementQO = new EtablissementDTO();
        etablissementQO.setId(utilisateurDTO.getIdEtablissement());
        //genere la chaîne à persister contenant les jours ouvrés.
        etablissementQO.setJoursOuvres(GenerateurDTO.generateJourOuvreToDb(listeJour));
        
        try {
            etablissementService.saveEtablissementJoursOuvres(etablissementQO);
            //mise à jour de la liste
            form.setListeOuvre(listeJour);
            //mise à jour de la session
            utilisateurDTO.setJoursOuvresEtablissement(etablissementQO.getJoursOuvres());            
        } catch (MetierException e) {
            log.debug("Erreur de sauvegarde des jours ouvrés : {0}", e.getMessage()); 
        }        
    }
    
    /**
     * Initialise la barre d'alternance des semaines.
     */
    private void initialiserBarreAlternanceSemaine() {
        final String alternanceSemaine = 
            etablissementService.findAlternanceSemaine(obtenirSessionUtilisateurDTO().getIdEtablissement());
        
        final Boolean vraiOuFauxAlternance = !StringUtils.isEmpty(alternanceSemaine);
        
        form.setVraiOuFauxAlternanceSemaine(vraiOuFauxAlternance);
        
        final AnneeScolaireDTO anneeScolaireDTO = obtenirSessionAnneeScolaireDTO();
        
        final List<BarreSemaineDTO> listeSemaine = 
            GenerateurDTO.generateListeSemaine(anneeScolaireDTO, alternanceSemaine, vraiOuFauxAlternance); 
        form.setListeBarreSemaine(listeSemaine);
    }

    
    
    /**
     * Active/désactive l'alternance des semaines.
     */
    public void activerAlternanceSemaine() {
        if (form.getVraiOuFauxAlternanceSemaine()) {
            //initialisation.
            final AnneeScolaireDTO anneeScolaireDTO = obtenirSessionAnneeScolaireDTO();
            form.setListeBarreSemaine(GenerateurDTO.generateListeSemaine(anneeScolaireDTO, 
                     "", true));
        } else {
            //désactivation.
            final AnneeScolaireDTO anneeScolaireDTO = obtenirSessionAnneeScolaireDTO();
            form.setListeBarreSemaine(GenerateurDTO.generateListeSemaine(anneeScolaireDTO, 
                    "", false));
            saveAlternancesSemaines();
        }
    }

    /** Methode tests.*/ 
    public void activerOnglet() {
        final String ongletSelectionne = form.getOngletSelectionne();
        form.setOngletSelectionne(ongletSelectionne);
    }
    
    /**
     * Met à jour la liste d'alternance des semaines.
     */
    public void saveAlternanceSemaine() {
        final String numeroSemSel = form.getNumeroSemaineSelectionne();
        form.setNomMethodeSauvegarde("saveAlternancesSemaines");
        
        Boolean repercutionActive = false;
        final Boolean vraiOuFauxRepercution = form.getRepercutionModificationAlternance();

        if (!StringUtils.isEmpty(numeroSemSel)) {
            for (final BarreSemaineDTO barreSemaineDTO : form.getListeBarreSemaine()) {
                final TypeSemaine type = barreSemaineDTO.getTypeJourEmploi(); 
                if (StringUtils.equals(barreSemaineDTO.getNumeroSemaine(), numeroSemSel)) {
                    if (vraiOuFauxRepercution) {
                        repercutionActive = true;
                    }
                    basculementAlternanceUneSemaine(type, barreSemaineDTO);                  
                } else if (repercutionActive) {
                    basculementAlternanceUneSemaine(type, barreSemaineDTO);
                }
            }
        }
    }
    
    /**
     * Inverse l'aternance de la semaine.
     * @param typeJourEmploi le type.
     * @param barreSemaineDTO la semaine.
     */
    private void basculementAlternanceUneSemaine(final TypeSemaine typeJourEmploi, final BarreSemaineDTO barreSemaineDTO) {
        if (!TypeSemaine.VAQUE.equals(typeJourEmploi)) {
            if (TypeSemaine.IMPAIR.equals(typeJourEmploi)) {
                barreSemaineDTO.setTypeJourEmploi(TypeSemaine.PAIR);
                barreSemaineDTO.setCouleur(GenerateurDTO.getMapAlternanceCouleur().get(TypeSemaine.PAIR));
            } else {
                barreSemaineDTO.setTypeJourEmploi(TypeSemaine.IMPAIR);
                barreSemaineDTO.setCouleur(GenerateurDTO.getMapAlternanceCouleur().get(TypeSemaine.IMPAIR));
            }
        }
        
    }
    
    /**
     * Appel métier de sauvegarde de l'alternance des semaines 1 et 2.
     */
    public void saveAlternancesSemaines() {
        final EtablissementDTO etablissementQO = new EtablissementDTO();
        etablissementQO.setId(obtenirSessionUtilisateurDTO().getIdEtablissement());
        etablissementQO.setAlternanceSemaine(GenerateurDTO.generateAlternanceSemaineToDb(form.getListeBarreSemaine()));
       
        try {
            etablissementService.saveEtablissementAlternance(etablissementQO);
        } catch (MetierException e) {
            log.debug("Erreur de sauvegarde de l'alternance des semaines : {0}", e.getMessage()); 
        }
        
    }
    
    /**
     * Réinitialise la barre de semaine.
     */
    public void reinitialiserAlternanacesSemaines() {
        initialiserBarreAlternanceSemaine();
    }    
    
    //////////////////////////////////////////////Onglet libellés
    /**
     * Initalise la liste d'enseignement et de type de devoir.
     */
    public void initialisationListeLibelle() {
        
        // Charge les libelles des categorie de devoir
        final List<SelectItem> listeCategorieTypeDevoir = new ArrayList<SelectItem>();
        for (final TypeCategorieTypeDevoir t : TypeCategorieTypeDevoir.values()) {
            final SelectItem item = new SelectItem(t.getId(), t.getLibelle());
            listeCategorieTypeDevoir.add(item);
        }
        form.setListeCategorieTypeDevoir(listeCategorieTypeDevoir);
        
        // Alimente les libelle enseignements correspondant à l'établissement
        final Integer idEtablissement = obtenirSessionUtilisateurDTO().getIdEtablissement();
        form.setListeEnseignement( 
            enseignementService.findEnseignementEtablissement(idEtablissement));
        
        // Charge les type de devoir pour l'etablissement
        initialiserListeLibelleTypeDevoir(idEtablissement);
    }
    
    /**
     * Appel métier de recherche de la liste des types de devoir.
     * @param idEtablissement l'identifiant de l'établissement.
     */
    private void initialiserListeLibelleTypeDevoir(final Integer idEtablissement) {
        form.setListeTypeDevoir( 
                devoirService.findListeTypeDevoir(idEtablissement).getValeurDTO());
    }
    
    /**
     * Sélection d'un libellé d'enseignement.
     */
    public void selectionnerLibelleEnseignement() {
        form.setLibelleEnseignement(form.getEnseignementSel().getLibellePerso());
    }
    
    /**
     * Appel du service métier de sauvegarde du libellé d'enseignement perso.
     */
    public void saveLibelleEnseignement() {
        final EnseignementQO enseignementQO = new EnseignementQO();
        enseignementQO.setIdEnseignement(form.getEnseignementSel().getId());
        enseignementQO.setIdEtablissement(obtenirSessionUtilisateurDTO().getIdEtablissement());
        enseignementQO.setLibelle(form.getLibelleEnseignement());
        
        try {
            enseignementService.saveEnseignementLibelle(enseignementQO);
            
            form.getEnseignementSel().setLibellePerso(form.getLibelleEnseignement());        
            form.setLibelleEnseignement(null);
        } catch (MetierException e) {
            log.debug("Erreur de sauvegarde du libellé de l'enseignement : {0}", e.getMessage()); 
        }
    }
    
    /**
     * Sélection d'un libellé du type de devoir.
     */
    public void selectionnerLibelleTypeDevoir() {
        form.setLibelleTypeDevoir(form.getTypeDevoirSel().getLibelle());
        form.setVraiOuFauxDevoir(TypeCategorieTypeDevoir.DEVOIR.equals(form.getTypeDevoirSel().getCategorie().getId()));
    }
    
    /**
     * Ajout d'un type de devoir.
     */
    public void ajoutTypeDevoir() {
        form.setLibelleTypeDevoir(null);
        form.setVraiOuFauxDevoir(false);
        form.setTypeDevoirSel(new TypeDevoirDTO());
    }
    
    /**
     * Appel du service métier de sauvegarde du type de devoir.
     */
    public void saveLibelleTypeDevoir() {
        final Integer idTypeDevoir = form.getTypeDevoirSel().getId();
        final TypeCategorieTypeDevoir typeCategorieTypeDevoir;
        if (form.getVraiOuFauxDevoir()) {
            typeCategorieTypeDevoir = TypeCategorieTypeDevoir.DEVOIR;
        } else {
            typeCategorieTypeDevoir = TypeCategorieTypeDevoir.NORMAL;
        }
        final Integer idEtablissement = obtenirSessionUtilisateurDTO().getIdEtablissement();
       
        final TypeDevoirQO typeDevoirQO = new TypeDevoirQO();
        typeDevoirQO.setId(idTypeDevoir);
        typeDevoirQO.setCategorie(typeCategorieTypeDevoir);
        typeDevoirQO.setIdEtablissement(idEtablissement);
        typeDevoirQO.setLibelle(form.getLibelleTypeDevoir());
        
        try {
            devoirService.saveTypeDevoir(typeDevoirQO);
            
            if (idTypeDevoir == null) {
                initialiserListeLibelleTypeDevoir(idEtablissement);
                form.setTypeDevoirSel(new TypeDevoirDTO());
            } else {
                form.getTypeDevoirSel().setLibelle(form.getLibelleTypeDevoir());
                form.getTypeDevoirSel().setCategorie(typeCategorieTypeDevoir);
            }
            form.setLibelleTypeDevoir(null);
        } catch (MetierException e) {
            log.debug("Erreur de sauvegarde du libellé de type de devoir : {0}", e.getMessage()); 
        }
    }
    
    /**
     * Appel métier de destruction d'un type de devoir.
     */
    public void deleteTypeDevoir() {
        try {
            devoirService.deleteTypeDevoir(form.getTypeDevoirSel().getId());
            initialiserListeLibelleTypeDevoir(obtenirSessionUtilisateurDTO().getIdEtablissement());
        } catch (MetierException e) {
            log.debug("Erreur de destrcution du type de devoir : {0}", e.getMessage()); 
        }
    }
    
    
    /////////////////////////////////////////////Onglet ouverture
    /**
     * Appel métier de mise à jour de l'ouverture d'un établissement.
     */
    public void saveOuvertureEtablissement() {
        final Boolean vraiOuFauxOuvert = form.getVraiOuFauxOuvertEtab();
        final UtilisateurDTO utilisateurDTO = obtenirSessionUtilisateurDTO();
        final OuvertureQO ouvertureQO = new OuvertureQO(utilisateurDTO.getIdEtablissement(), 
                vraiOuFauxOuvert);       
        try {
            etablissementService.saveEtablissementOuverture(ouvertureQO);
            utilisateurDTO.setVraiOuFauxCahierOuvertEtab(vraiOuFauxOuvert);
            //réinitialise le menu
            final MenuControl menuControl = ContexteUtils.getMenuControl();
            form.setVraiOuFauxOuvertEtabSource(vraiOuFauxOuvert);
            menuControl.init();
        } catch (MetierException e) {
            form.setVraiOuFauxOuvertEtab(!vraiOuFauxOuvert);
            log.debug("Erreur de sauvegarde de l'ouverture établissement : {0}", e.getMessage()); 
        }
    }
    
    
    //////////////////////////////////////////////Navigation entre onglet
    
    /**
     * Listener du changement d'onglet.
     * @param valueChangeEvent l'événement de changement.
     */
    public void navigationOnlget(ItemChangeEvent valueChangeEvent)  {   
        final String tab = valueChangeEvent.getNewItemName();
        form.resetOnglet();
        form.setOngletSelectionne(tab);
        gestionnaireChargementOnglet();
    }
    
    /**
     * Effectue les appels métiers en fonction de l'onglet.
     */
    private void gestionnaireChargementOnglet() {
        switch (TypeOngletAdmin.valueOf(form.getOngletSelectionne())) {
        case ANNEE_SCOLAIRE:
            //Onglet année scolaire
            selectionneBarreMois();      
            break;
        case GRILLE_HORAIRE:
            //ONGLET Grille horaire            
            initialiseValeurEtGrilleHoraire();
            break;
        case JOURS_OUVRES:
            initialiserBarreJourOuvre();
            initialiserBarreAlternanceSemaine();
            break;
        case LIBELLES:
            initialisationListeLibelle();
            break;
        case OUVERTURE:
            break;
        default:
            break;
        }
    }
    
    /**
     * Appelee lors de la validation de modification/ajout d'une plage horaire de la grille emploi du temps. 
     */
    public void validerModificationHorairePlage() {
        
        if (form.getPlageSelectedIndex() != null && form.getPlageEditee() != null) {
        log.info("validerModificationHorairePlage Selected index {0}, total grille {1}, debut {2}:{3}, fin {4}:{5}.  obj {6} obj {7}", 
                form.getPlageSelectedIndex(),
                form.getHorairesCours().size(), 
                form.getPlageEditee().getHeureDebut(),
                form.getPlageEditee().getMinuteDebut(),
                form.getPlageEditee().getHeureFin(),
                form.getPlageEditee().getMinuteFin(),
                form.getHorairesCours().get(form.getPlageSelectedIndex()),
                form.getPlageEditee()
                );
        }
        
        form.recalculerHorairesCoursJSON();
        
    }
    
    /**
     * Bascule dans la plage identifiee par plageSelectedIndex, les valeur de la plage editee.  
     */
    public void validerEditionHorairePlage() {
                
        ConteneurMessage cm = new ConteneurMessage();
        
        List<GrilleHoraireDTO> listeAVerifier = new ArrayList<GrilleHoraireDTO>();
        listeAVerifier.add(form.getPlageEditee());
        
        EtablissementFacade.verifierGrilleHoraireDebutFin(cm, listeAVerifier);
        
        if (cm.contientMessageBloquant()) {
            MessageUtils.addMessages(cm, null, getClass());
            return;
        }
        

        form.getHorairesCours().set(form.getPlageSelectedIndex(), form.getPlageEditee());
        
        validerModificationHorairePlage();
        
        
    }
    
    /**
     * Supprime la plage identifiee par plageSelectedIndex.
     */
    public void supprimerPlage() {
        
        log.info("supprimerPlage Selected index {0}, total grille {1}, debut {2}:{3}, fin {4}:{5}.  obj {6} obj {7}", 
                form.getPlageSelectedIndex(),
                form.getHorairesCours().size(), 
                form.getPlageEditee().getHeureDebut(),
                form.getPlageEditee().getMinuteDebut(),
                form.getPlageEditee().getHeureFin(),
                form.getPlageEditee().getMinuteFin(),
                form.getHorairesCours().get(form.getPlageSelectedIndex()),
                form.getPlageEditee()
                );
        
        final Integer index = form.getPlageSelectedIndex();
        final GrilleHoraireDTO plageOri = form.getHorairesCours().get(index);
        form.getHorairesCours().remove(plageOri);
        
        form.setPlageEditee(null);
        form.setPlageSelectedIndex(null);
        
        validerModificationHorairePlage();
    }
}
