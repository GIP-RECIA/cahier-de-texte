/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.application.control.emploi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.commons.lang.ObjectUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.PeriodeEmploiDTO;
import org.crlr.dto.application.base.PrintEmploiQO;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.dto.application.base.RechercheGroupeClassePopupQO;
import org.crlr.dto.application.base.TypeEditionEmploiTemps;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.dto.application.emploi.SaveEmploiQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.metier.facade.EmploiFacade;
import org.crlr.metier.facade.EmploiFacadeService;
import org.crlr.metier.facade.EtablissementFacadeService;
import org.crlr.services.EmploiService;
import org.crlr.services.EnseignementService;
import org.crlr.services.GroupeClasseService;
import org.crlr.utils.CollectionUtils;
import org.crlr.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.web.application.control.AbstractControl;
import org.crlr.web.application.form.emploi.ConstituerEmpForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.GrilleHoraireDTO;
import org.crlr.web.dto.TypeSemaine;
import org.crlr.web.utils.MessageUtils;
import org.crlr.web.utils.ReportUtils;
import org.richfaces.event.ItemChangeEvent;

/**
 * ConstituerEmpControl.
 *
 * @author breytond
 * @version $Revision: 1.18 $
 */
@ManagedBean(name = "constituerEmp")
@ViewScoped
public class ConstituerEmpControl extends AbstractControl<ConstituerEmpForm> {
    /** Service de groupes et classes. */
    @ManagedProperty(value = "#{groupeClasseService}")
    private GroupeClasseService groupeClasseService;

    /** Service d'enseignement. */
    @ManagedProperty(value = "#{enseignementService}")
    private EnseignementService enseignementService;

    /** Service d'établissement. */
    @ManagedProperty(value = "#{etablissementFacade}")
    private EtablissementFacadeService etablissementService;

    /** Service d'emploi du temps. */
    @ManagedProperty(value = "#{emploiService}")
    private EmploiService emploiService;

    /**
     *
     */
    @ManagedProperty(value = "#{emploiFacade}")
    private EmploiFacadeService emploiFacade;

    /**
     * Mutateur de enseignementService.
     *
     * @param enseignementService
     *            le enseignementService à modifier.
     */
    public void setEnseignementService(EnseignementService enseignementService) {
        this.enseignementService = enseignementService;
    }

    /**
     * DOCUMENT ME!
     *
     * @param groupeClasseService
     *            DOCUMENTATION INCOMPLETE!
     */
    public void setGroupeClasseService(GroupeClasseService groupeClasseService) {
        this.groupeClasseService = groupeClasseService;
    }

    

    /**
     * Mutateur de emploiService.
     *
     * @param emploiService
     *            le emploiService à modifier.
     */
    public void setEmploiService(EmploiService emploiService) {
        this.emploiService = emploiService;
    }

    /**
     * Constructeur.
     */
    public ConstituerEmpControl() {
        super(new ConstituerEmpForm());
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    public void onLoad() {
        log.info("onLoad");
        // permet de faire un seule appel métier durant le cycle de l'outil.
        // valable pour les outils dont les paramètres ne sont pas saisies par
        // l'utilisateur.
        

        form.setTexteAide(emploiService.getAideContextuelle());

        // Récupération du contexte utilisateur
        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();
        final Integer idEnseignant = utilisateurDTO.getUserDTO()
                .getIdentifiant();
        final Integer idEtablissement = utilisateurDTO.getIdEtablissement();
        
        form.setEtablissement(etablissementService.findEtablissement(idEtablissement).getValeurDTO());

        obtenirListeClasseGroupe(idEnseignant, idEtablissement,
                utilisateurDTO.getAnneeScolaireDTO().getId());

        obtenirListeEnseignement(idEnseignant, idEtablissement);

        obtenirHoraireCours(idEtablissement);

        // récupération de l'alternance des semaines ou non
        final String alternance = etablissementService
                .findAlternanceSemaine(idEtablissement);
        final Boolean vraiOuFauxAlternance = StringUtils
                .isEmpty(alternance) ? false : true;
        form.setVraiOuFauxAlternanceSemaine(vraiOuFauxAlternance);

        obtenirPeriodes();

        obtenirEmploiDuTemps();


        
    }

    /**
     * Récupère la liste des classes et des groupes.
     *
     * @param idEnseignant
     *            l'identifiant de l'enseignant
     * @param idEtablissement
     *            l'identifiant de l'établissement
     * @param idAnneeScolaire
     *            l'identifiant de l'année scolaire
     */
    private void obtenirListeClasseGroupe(final Integer idEnseignant,
            final Integer idEtablissement, final Integer idAnneeScolaire) {
        // Recherche des classes de l'enseignant.
        final RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO = new RechercheGroupeClassePopupQO();
        rechercheGroupeClassePopupQO.setIdEnseignant(idEnseignant);
        rechercheGroupeClassePopupQO.setTypeGroupeClasse(TypeGroupe.CLASSE);
        rechercheGroupeClassePopupQO.setIdAnneeScolaire(idAnneeScolaire);
        rechercheGroupeClassePopupQO.setIdEtablissement(idEtablissement);
        try {
            final ResultatDTO<List<GroupesClassesDTO>> resultatClasses = groupeClasseService
                    .findGroupeClassePopup(rechercheGroupeClassePopupQO);
            final List<GroupesClassesDTO> listeClasses = org.crlr.utils.ObjectUtils.clone(resultatClasses.getValeurDTO());

            for (final GroupesClassesDTO classeDTO : listeClasses) {
                final String libelleDecoupe = org.crlr.utils.StringUtils.truncateHTMLString(
                        classeDTO.getIntitule(), 12);
                classeDTO.setCode(libelleDecoupe);
                classeDTO.setTypeGroupe(TypeGroupe.CLASSE);
            }

            form.getListeClasses().addAll(listeClasses);
        } catch (MetierException e) {
            log.debug(
                    "Echec de la recherche des classes de l'enseignant : {0}",
                    e.getMessage());
        }

        // Recherche des groupes de l'enseignant.
        rechercheGroupeClassePopupQO.setTypeGroupeClasse(TypeGroupe.GROUPE);
        try {
            final ResultatDTO<List<GroupesClassesDTO>> resultatGroupes = groupeClasseService
                    .findGroupeClassePopup(rechercheGroupeClassePopupQO);
            final List<GroupesClassesDTO> listeGroupes = org.crlr.utils.ObjectUtils.clone(resultatGroupes.getValeurDTO());

            for (final GroupesClassesDTO groupeDTO : listeGroupes) {
                final String libelleDecoupe = org.crlr.utils.StringUtils.truncateHTMLString(
                        groupeDTO.getIntitule(), 13);
                groupeDTO.setCode(libelleDecoupe);
                groupeDTO.setTypeGroupe(TypeGroupe.GROUPE);
            }

            form.getListeGroupes().addAll(listeGroupes);
        } catch (MetierException e) {
            log.debug(
                    "Echec de la recherche des groupes de l'enseignant : {0}",
                    e.getMessage());
        }
    }

    /**
     * Permet d'obtenir la liste des enseignements.
     *
     * @param idEnseignant
     *            l'identifiant de l'enseignant
     * @param idEtablissement
     *            l'identifiant de l'établissement
     */
    private void obtenirListeEnseignement(final Integer idEnseignant,
            final Integer idEtablissement) {
        final RechercheEnseignementPopupQO rechercheEnseignementPopupQO = new RechercheEnseignementPopupQO();
        rechercheEnseignementPopupQO.setIdEnseignant(idEnseignant);
        rechercheEnseignementPopupQO.setIdEtablissement(idEtablissement);

        final List<EnseignementDTO> listeEnseignements = enseignementService
                .findEnseignementEnseignant(rechercheEnseignementPopupQO);

        for (final EnseignementDTO enseignementDTO : listeEnseignements) {
            final String libelleDecoupe = org.apache.commons.lang.StringUtils.abbreviate(enseignementDTO.getIntitule(), 32);
            enseignementDTO.setCode(libelleDecoupe);
        }

        form.setListeEnseignements(listeEnseignements);
    }

    /**
     * Appel métier pour la récuprération des horaires de cours d'un
     * établissement.
     *
     * @param idEtablissement
     *            l'établissement pour lequel on souhaite avoir les horaires.
     */
    private void obtenirHoraireCours(final Integer idEtablissement) {
        try {

            List<GrilleHoraireDTO> listeHoraires = GenerateurDTO
                    .generateGrilleHoraireFromDb(etablissementService
                            .findHorairesCoursEtablissement(idEtablissement)
                            .getValeurDTO());

            /*
             * BOUCHON listeHoraires.clear();
             *
             * listeHoraires.add(new GrilleHoraireDTO());
             * listeHoraires.get(listeHoraires.size() - 1).setHeureDebut(8);
             * listeHoraires.get(listeHoraires.size() - 1).setMinuteDebut(17);
             * listeHoraires.get(listeHoraires.size() - 1).setHeureFin(10);
             * listeHoraires.get(listeHoraires.size() - 1).setMinuteFin(10);
             *
             * listeHoraires.add(new GrilleHoraireDTO());
             * listeHoraires.get(listeHoraires.size() - 1).setHeureDebut(10);
             * listeHoraires.get(listeHoraires.size() - 1).setMinuteDebut(17);
             * listeHoraires.get(listeHoraires.size() - 1).setHeureFin(11);
             * listeHoraires.get(listeHoraires.size() - 1).setMinuteFin(10);
             *
             * listeHoraires.add(new GrilleHoraireDTO());
             * listeHoraires.get(listeHoraires.size() - 1).setHeureDebut(11);
             * listeHoraires.get(listeHoraires.size() - 1).setMinuteDebut(10);
             * listeHoraires.get(listeHoraires.size() - 1).setHeureFin(12);
             * listeHoraires.get(listeHoraires.size() - 1).setMinuteFin(05);
             *
             * listeHoraires.add(new GrilleHoraireDTO());
             * listeHoraires.get(listeHoraires.size() - 1).setHeureDebut(13);
             * listeHoraires.get(listeHoraires.size() - 1).setMinuteDebut(27);
             * listeHoraires.get(listeHoraires.size() - 1).setHeureFin(14);
             * listeHoraires.get(listeHoraires.size() - 1).setMinuteFin(30);
             *
             * listeHoraires.add(new GrilleHoraireDTO());
             * listeHoraires.get(listeHoraires.size() - 1).setHeureDebut(14);
             * listeHoraires.get(listeHoraires.size() - 1).setMinuteDebut(47);
             * listeHoraires.get(listeHoraires.size() - 1).setHeureFin(15);
             * listeHoraires.get(listeHoraires.size() - 1).setMinuteFin(40);
             *
             * listeHoraires.add(new GrilleHoraireDTO());
             * listeHoraires.get(listeHoraires.size() - 1).setHeureDebut(17);
             * listeHoraires.get(listeHoraires.size() - 1).setMinuteDebut(17);
             * listeHoraires.get(listeHoraires.size() - 1).setHeureFin(18);
             * listeHoraires.get(listeHoraires.size() - 1).setMinuteFin(10);
             */

            form.setHorairesCours(listeHoraires);

        } catch (MetierException e) {
            log.debug(
                    "Echec de la récupération des horaires de cours de l'établissement : {0}",
                    e.getMessage());
        }
    }

    /**
     * Chercher les périodes.
     */
    private void obtenirPeriodes() {

        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();

        final Integer idEtablissement = utilisateurDTO.getIdEtablissement();
        final Integer idEnseignant = utilisateurDTO.getUserDTO()
                .getIdentifiant();

        List<PeriodeEmploiDTO> listePeriode = emploiService.findPeriodes(
                idEnseignant, idEtablissement);

        form.setListePeriode(listePeriode);

        log.info("Periods:  " + listePeriode.size());

        final Date today = DateUtils.getAujourdhui();
        
        form.setPeriodeSelectionnee(PeriodeEmploiDTO.findPeriodePourDate(listePeriode, today));
        
    }

    /**
     * Fonction de recupération d'emploi de temps.
     */
    private void obtenirEmploiDuTemps() {

        log.info("obtenirEmploiDuTemps");

        if (null == form.getPeriodeSelectionnee()) {
            log.info("No période");
            form.setListeEmploiDeTemps(new ArrayList<DetailJourEmploiDTO>());
            return;
        }

        final Boolean recupererSemaine2 = form.getVraiOuFauxAlternanceSemaine()
                && TypeSemaine.IMPAIR.equals(form.getOngletSelectionne());

        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();
        final Integer idEtablissement = utilisateurDTO.getIdEtablissement();
        final Integer idEnseignant = utilisateurDTO.getUserDTO()
                .getIdentifiant();

        final Integer idPeriode = form.getPeriodeSelectionnee().getId();

        // Appel metier pour la récupération des enregistrement depuis la base
        // de données
        final List<DetailJourEmploiDTO> listeFromDatabase = emploiService
                .findEmploi(
                        idEnseignant,
                        idEtablissement,
                        Boolean.TRUE.equals(recupererSemaine2) ? TypeSemaine.IMPAIR
                                : TypeSemaine.PAIR, idPeriode).getValeurDTO();

        log.info("Id periode : {0}.  Results : {1}", idPeriode,
                listeFromDatabase.size());

        form.setListeEmploiDeTemps(CollectionUtils.cloneCollection(listeFromDatabase));

    }

    public void creerDetailJourEmploiDTOSel() {
        log.info("Ajoute un nouveau DTO {0}");

        DetailJourEmploiDTO nouveauObject = new DetailJourEmploiDTO();

        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();
        final Integer idEnseignant = utilisateurDTO.getUserDTO()
                .getIdentifiant();
        final Integer idEtablissement = utilisateurDTO.getIdEtablissement();

        nouveauObject.setIdEnseignant(idEnseignant);
        nouveauObject.setIdEtablissement(idEtablissement);
        nouveauObject.setIdPeriodeEmploi(form.getPeriodeSelectionnee()
                .getId());
        nouveauObject.setTypeSemaine(form.getOngletSelectionne());
        nouveauObject.getGroupeOuClasse().setTypeGroupe(TypeGroupe.CLASSE);

        if (nouveauObject.getTypeSemaine() == null) {
            nouveauObject.setTypeSemaine(TypeSemaine.PAIR);
        }

        //listeEmploiDeTemps.add(nouveauObject);
        form.setDetailJourEmploiDTOSel(nouveauObject);
        form.setDetailJourEmploiDTOSelIndex(form.getListeEmploiDeTemps().size());
    }
    /**
     * Charger l'emploi de temps a partir du index mis.
     */
    public void chargerDetailJourEmploiDTOSel() {

        Integer index = form.getDetailJourEmploiDTOSelIndex();

        log.info("chargerDetailJourEmploiDTOSel : " + index);

        final List<DetailJourEmploiDTO> listeEmploiDeTemps = form
                .getListeEmploiDeTemps();

        if (index > listeEmploiDeTemps.size()) {
            log.error("Index non valide");
        } else if (index == listeEmploiDeTemps.size()) {
            log.info("Afficher popup évènement crée");
        } else {
            form.setDetailJourEmploiDTOSel(org.crlr.utils.ObjectUtils.clone(listeEmploiDeTemps.get(index)));
        }
    }

    /**
     * Réinitialisation.
     */
    public void reset() {
        form.setVraiOuFauxModification(false);
        form.resetGrilleEmploi();
        form.getListeEmploiDeTempsASupprimer().clear();

        obtenirEmploiDuTemps();
    }

    /**
     * Initialise la semaine 2 avec les informations de la semaine 1.
     */
    public void initialiserSemaine2Avec1() {

        //Sauvegarde les emploi de temps actuels
        boolean sauvegardeOK = saveMetier();

        if (!sauvegardeOK) {
            return;
        }
        // Construit le QO pour les verifications de coherence

        // listeJourSemaine1Copie contient le planning de la semaine 1
        final List<DetailJourEmploiDTO> listeJourSemaine1Copie = form
                .getListeEmploiDeTemps();

        // Reset à null les id des jours pour la semaine 2
        for (final DetailJourEmploiDTO detail : listeJourSemaine1Copie) {
            detail.setId(null);
        }

        final PeriodeEmploiDTO periode = form.getPeriodeSelectionnee();

        // Appel métier pour la sauvegarde
        try {
            final TypeSemaine typeSemaine = TypeSemaine.IMPAIR;
            emploiFacade.dupliquerEmploiSemaine(TypeSemaine.PAIR, typeSemaine,
                    periode).getValeurDTO();
            reset();
            
            form.setOngletSelectionneStr("ongletSemaine2");
            navigationOnlget(null);
        } catch (MetierException e) {
            log.debug(
                    "Erreur lors de la persistance de l'emploi du temps : {0}",
                    e.getMessage());
        }
    }

    /**
     * Construit ke QO qui va servir pour la sauvegarde de l'emploi du temps.
     * @param listeSelectionnee l
     * @return r
     */
    private SaveEmploiQO obtenirSaveEmploiQO(
            final List<DetailJourEmploiDTO> listeSelectionnee) {

        final SaveEmploiQO saveEmploiQO = new SaveEmploiQO();

        saveEmploiQO.setListeDetailJourEmploiDTO(listeSelectionnee);

        return saveEmploiQO;
    }

    
    /**
     * @return vrai si il a réussi
     */
    public boolean saveMetier() {

        final List<DetailJourEmploiDTO> listeSelectionnee = form
                .getListeEmploiDeTemps();

        // construit le QO de sauvegarde
        final SaveEmploiQO saveEmploiQO = obtenirSaveEmploiQO(listeSelectionnee);

        saveEmploiQO.setListeIdEmploiTempsAsupprimer(form
                .getListeEmploiDeTempsASupprimer());

        // Appel métier pour la sauvegarde
        try {
            emploiService.saveEmploiDuTemps(saveEmploiQO).getValeurDTO();
            reset();

        } catch (MetierException e) {

            log.debug(
                    "Erreur lors de la persistance de l'emploi du temps : {0}",
                    e.getMessage());
            return false;
        }

        return true;

    }

    /**
     * Appel métier pour la sauvegarde de l'emploi du temps.
     *
     * @param valueChangeEvent
     *            DOCUMENT ME!
     */
    /**
     * Listener du changement d'onglet.
     *
     * @param valueChangeEvent
     *            l'événement de changement.
     */
    public void navigationOnlget(ItemChangeEvent valueChangeEvent) {
        form.setVraiOuFauxModification(false);
        
        log.info("Onglet a changé, recharge emploi de temps");
        obtenirEmploiDuTemps();
        

    }

    /**
     * Impression de l'emploi du temps.
     */
    public void printEmp() {
        
        try {
            // Récupération du contexte utilisateur
            final UtilisateurDTO utilisateurDTO = ContexteUtils
                    .getContexteUtilisateur().getUtilisateurDTO();
            final PrintEmploiQO printEmploiQO = new PrintEmploiQO();
            printEmploiQO.setUtilisateurDTO(utilisateurDTO);

            printEmploiQO.setJourOuvreAccessible(GenerateurDTO
                    .getListeJourOuvreFromDb(utilisateurDTO
                            .getJoursOuvresEtablissement()));

            printEmploiQO.setHeureDebut(form.getEtablissement().getHeureDebut());

            printEmploiQO.setHeureFin(form.getEtablissement().getHeureFin() + 1);

            printEmploiQO.setListeEmploiDeTemps(form.getListeEmploiDeTemps());

            if (TypeSemaine.IMPAIR.equals(form.getOngletSelectionne())) {
                printEmploiQO.setTypeSemaine(false);
            } else {
                if (form.getVraiOuFauxAlternanceSemaine()) {
                    printEmploiQO.setTypeSemaine(true);
                } else {
                    printEmploiQO.setTypeSemaine(null);
                }
            }

            printEmploiQO
                    .setTypeEditionEmploiTemps(TypeEditionEmploiTemps.Enseignant);

            ReportUtils.stream(emploiService.printEmploiDuTemps(printEmploiQO));
        } catch (Exception e) {
            log.debug("Erreur lors de l'édition des comptes", e);
        }
    }

    /**
     * Supprimmer une case.
     * @return rien
     */
    public String supprimerEvenement() {
        log.info("supprimerEvenement");
        
        if (form.getDetailJourEmploiDTOSelIndex() >= form.getListeEmploiDeTemps().size()) {
            log.info("Suppression d'un évènement nouveau");
            return null;
        }

        List<DetailJourEmploiDTO> listeJours = new ArrayList<DetailJourEmploiDTO>(
                form.getListeEmploiDeTemps());
        DetailJourEmploiDTO empDeTempsASup = listeJours.remove(form
                .getDetailJourEmploiDTOSelIndex().intValue());
        form.setListeEmploiDeTemps(listeJours);

        if (empDeTempsASup != null && empDeTempsASup.getId() != null) {
            form.getListeEmploiDeTempsASupprimer().add(empDeTempsASup.getId());
        }
        return null;
    }

    /**
     * Sauvegarder une case.
     * @return rien
     */
    public Object sauvegarderEvenement() {
        log.info("sauvegarderEvenement");
        log.info("id: " + form.getDetailJourEmploiDTOSel().getMatiere().getId());

        log.info("index: " + form.getDetailJourEmploiDTOSelIndex());
        
        //Faire une vérification de cette évènement
        ConteneurMessage cm = new ConteneurMessage();
        List<DetailJourEmploiDTO> listeAVerifier = new ArrayList<DetailJourEmploiDTO>();
        listeAVerifier.add(form.getDetailJourEmploiDTOSel());
        EmploiFacade.verifierEmploiDeTempsDebutFin(cm, listeAVerifier);
        
        if (cm.contientMessageBloquant()) {
            MessageUtils.addMessages(cm, null, getClass());
            return null;
        }

        //Remplie classe / groupe données
        if (null != form.getDetailJourEmploiDTOSel().getGroupeOuClasse()
                .getId()) {
            if (form.getDetailJourEmploiDTOSel().getGroupeOuClasse()
                    .getVraiOuFauxClasse()) {
                form.getDetailJourEmploiDTOSel().setGroupeOuClasse(
                        groupeClasseService.findClasse(form
                                .getDetailJourEmploiDTOSel()
                                .getGroupeOuClasse().getId()));
            } else if (Boolean.FALSE.equals(form.getDetailJourEmploiDTOSel()
                    .getGroupeOuClasse().getVraiOuFauxClasse())) {
                form.getDetailJourEmploiDTOSel().setGroupeOuClasse(
                        groupeClasseService.findGroupe(form
                                .getDetailJourEmploiDTOSel()
                                .getGroupeOuClasse().getId()));
            }
        }

        log.info("Debut {0}:{1}  Fin {2}:{3} Jour: {4}", form
                .getDetailJourEmploiDTOSel().getHeureDebut(), form
                .getDetailJourEmploiDTOSel().getMinuteDebut(), form
                .getDetailJourEmploiDTOSel().getHeureFin(), form
                .getDetailJourEmploiDTOSel().getMinuteFin(), form
                .getDetailJourEmploiDTOSel().getJour());
        log.info("desc" + form.getDetailJourEmploiDTOSel().getDescription());        
        log.info("couleur " + form.getDetailJourEmploiDTOSel().getTypeCouleur());
        log.info("salle" + form.getDetailJourEmploiDTOSel().getCodeSalle());
        
        if (form.getDetailJourEmploiDTOSelIndex() == form.getListeEmploiDeTemps().size()) {
            //Il s'agit d'un ajout
            form.getListeEmploiDeTemps().add(form.getDetailJourEmploiDTOSelIndex(), form.getDetailJourEmploiDTOSel());
        } else {        
            //Remettre l'objet cloné dans la liste
            form.getListeEmploiDeTemps().set(form.getDetailJourEmploiDTOSelIndex(), form.getDetailJourEmploiDTOSel());
        }
        
        for (DetailJourEmploiDTO emp : form.getListeEmploiDeTemps()) {
            log.info("Debut {0}:{1}  Fin {2}:{3} Jour: {4}", 
                    emp.getHeureDebut(), emp.getMinuteDebut(), emp.getHeureFin(), emp.getMinuteFin(), emp.getJour());
        }

        form.setListeEmploiDeTempsJSON(ConstituerEmpForm
                .createListeEmploiJSON(form.getListeEmploiDeTemps()));
        return null;
    }

    /**
     * Recharge le JSON de la liste des évènement après un changement effectue dans la couche javascript. 
     * @return null
     */
    public Object affecterDetailJourEmploiDTOSel() {
        
        if (form.getDetailJourEmploiDTOSelIndex() == form.getListeEmploiDeTemps().size()) {
            //Il s'agit d'un changement à un évènement ajouté    
            log.debug("affecterDetailJourEmploiDTOSel pour un nouveau évènement");
        } else {        
            //Remettre l'objet cloné dans la liste pour que les changements soient pris en compte
            form.getListeEmploiDeTemps().set(form.getDetailJourEmploiDTOSelIndex(), form.getDetailJourEmploiDTOSel());
        }
        
        form.setListeEmploiDeTempsJSON(ConstituerEmpForm
                .createListeEmploiJSON(form.getListeEmploiDeTemps()));
        return null;
    }


    /**
     * @return rien
     */
    public Object creerPeriode() {
        log.info("" + form.getInitPeriodeAvecCopie());
        log.info("" + form.getPeriodeACopie());

        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();
        final Integer idEnseignant = utilisateurDTO.getUserDTO()
                .getIdentifiant();
        final Integer idEtablissement = utilisateurDTO.getIdEtablissement();

        PeriodeEmploiDTO periode = new PeriodeEmploiDTO();
        periode.setDateDebut(form.getCreerPeriodeDate());
        periode.setIdEnseignant(idEnseignant);
        periode.setIdEtablissement(idEtablissement);

        Integer periodeACopieId = null;

        if (form.getPeriodeACopie() != null) {
            periodeACopieId = form.getPeriodeACopie().getId();
        }

        try {
            emploiFacade.creerPeriode(periode, utilisateurDTO.getAnneeScolaireDTO().getId(), periodeACopieId);

            obtenirPeriodes();
            
            //Sélectionne la période qu'on vient de créer
            for (PeriodeEmploiDTO periodeIterator :  form.getListePeriode()) {
                if (ObjectUtils.equals(periodeIterator.getDateDebut(),
                        periode.getDateDebut())) {
                    form.setPeriodeSelectionnee(periodeIterator);
                    break;
                }
            }
            
            obtenirEmploiDuTemps();
        } catch (MetierException ex) {
            log.debug( "ex", ex);
        }

        return null;
    }

    /**
     * @return rien
     */
    public Object annulerPeriode() {
        return null;
    }

    /**
     * Quand la période est changé depuis la liste déroulante.
     * @param event e
     * @throws javax.faces.event.AbortProcessingException ex
     */
    public void chargerPeriode(AjaxBehaviorEvent event)
            throws javax.faces.event.AbortProcessingException {
        log.info("chargerPeriode "
                + (this.getForm().getPeriodeSelectionnee() != null ? this
                        .getForm().getPeriodeSelectionnee().getId() : ""));

        obtenirEmploiDuTemps();

    }

    /**
     * Toolbar bouton sauvegarder.
     * @return rien
     */
    public Object sauvegarder() {        
        saveMetier();
        return null;
    }

    /**
     * Toolbar bouton supprimer.
     * @return rien
     */
    public Object supprimer() {

        this.emploiFacade.deletePeriode(form.getPeriodeSelectionnee());

        obtenirPeriodes();
        obtenirEmploiDuTemps();

        return null;
    }

    /**
     * Initialise popup ajouter période.
     * @return rien
     */
    public Object initAjoutePeriodePopup() {
        form.setInitPeriodeAvecCopie(false);
        form.setPeriodeACopie(null);
        form.setCreerPeriodeDate(null);

        return null;
    }

    /**
     * @param emploiFacade
     *            the emploiFacade to set
     */
    public void setEmploiFacade(EmploiFacadeService emploiFacade) {
        this.emploiFacade = emploiFacade;
    }

    /**
     * @return the etablissementService
     */
    public EtablissementFacadeService getEtablissementService() {
        return etablissementService;
    }

    /**
     * @param etablissementService the etablissementService to set
     */
    public void setEtablissementService(
            EtablissementFacadeService etablissementService) {
        this.etablissementService = etablissementService;
    }

}
