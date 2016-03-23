/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConsoliderEmpControl.java,v 1.14 2010/06/01 13:45:24 ent_breyton Exp $
 */

package org.crlr.web.application.control.emploi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.PrintEmploiQO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.TypeEditionEmploiTemps;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.dto.application.emploi.RechercheEmploiQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.EtablissementFacadeService;
import org.crlr.services.EmploiService;
import org.crlr.services.SeanceService;
import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.web.application.control.AbstractPopupControl;
import org.crlr.web.application.control.ClasseGroupeControl;
import org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener;
import org.crlr.web.application.form.emploi.ConsoliderEmpForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.dto.EnteteEmploiJoursDTO;
import org.crlr.web.dto.TypeCouleurJour;
import org.crlr.web.dto.TypeSemaine;
import org.crlr.web.utils.ReportUtils;

/**
 * ConstituerEmpControl.
 * 
 * @author carriere
 * @version $Revision: 1.14 $
 */
@ManagedBean(name = "consoliderEmp")
@ViewScoped
public class ConsoliderEmpControl extends
        AbstractPopupControl<ConsoliderEmpForm> implements ClasseGroupeListener {
    
    
    
    /** Service d'établissement. */
    @ManagedProperty(value = "#{etablissementFacade}")
    private EtablissementFacadeService etablissementService;

    /** Service d'emploi du temps. */
    @ManagedProperty(value = "#{emploiService}")
    private transient EmploiService emploiService;

    @ManagedProperty(value = "#{classeGroupe}")
    private transient ClasseGroupeControl classeGroupeControl;
    

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
     * Mutateur de seanceService.
     * 
     * @param seanceService
     *            le seanceService à modifier.
     */
    public void setSeanceService(SeanceService seanceService) {
        this.seanceService = seanceService;
    }

    /**
     * Constructeur.
     */
    public ConsoliderEmpControl() {
        super(new ConsoliderEmpForm());

        // Gestion des profils
        if (Profil.ELEVE.equals(form.getProfilEnCours())
                || Profil.PARENT.equals(form.getProfilEnCours())) {
            this.usePopupGroupeClasse = false;
        } else {
            this.usePopupGroupeClasse = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    public void onLoad() {
        classeGroupeControl.setListener(this);

        // Initialisation de la barre des semaines et pré sélectionne la
        // semaine en cours si elle n'est pas vaquée.
        this.initialiserBarreSemaine();

        final Integer idEtablissement = ContexteUtils.getContexteUtilisateur()
                .getUtilisateurDTO().getIdEtablissement();

        form.setEtablissement(etablissementService.findEtablissement(
                idEtablissement).getValeurDTO());

        final String alternance = etablissementService
                .findAlternanceSemaine(idEtablissement);
        final Boolean vraiOuFauxAlternance = StringUtils.isEmpty(alternance) ? false
                : true;
        form.setVraiOuFauxAlternance(vraiOuFauxAlternance);

        // recherche automatique pour le parent ou l'élève
        if (Profil.ELEVE.equals(form.getProfilEnCours())
                || Profil.PARENT.equals(form.getProfilEnCours())) {
            this.rechercher();
        }

    }

    
    /**
     * Initialise la barre d'alternance des semaines.
     */
    private void initialiserBarreSemaine() {
        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();
        final String alternanceSemaine = etablissementService
                .findAlternanceSemaine(utilisateurDTO.getIdEtablissement());

        final Boolean vraiOuFauxAlternance = !StringUtils
                .isEmpty(alternanceSemaine);

        final AnneeScolaireDTO anneeScolaireDTO = utilisateurDTO
                .getAnneeScolaireDTO();

        final List<BarreSemaineDTO> listeSemaine = GenerateurDTO
                .generateListeSemaine(anneeScolaireDTO, alternanceSemaine,
                        vraiOuFauxAlternance);

        form.setListeBarreSemaine(listeSemaine);

        form.setListeMois(GenerateurDTO.genererListeMois(
                anneeScolaireDTO.getDateRentree(),
                anneeScolaireDTO.getDateSortie()));
        
        // Selectionne la semaine du jour par defaut
        form.setSemaineSelectionnee(GenerateurDTO.selectionnerSemaineJour(form.getListeBarreSemaine()));
    }

    /**
     * Réinitialisation.
     */
    public void reset() {
        form.reset();
    }

    /**
     * Navigation entre les semaines.
     */
    public void naviguerSemaine() {
        final List<BarreSemaineDTO> listeSem = form.getListeBarreSemaine();
        final BarreSemaineDTO semaine = form.getSemaineSelectionnee();
        
        for (final BarreSemaineDTO semaineDTO : listeSem) {
            if (semaine.getNumeroSemaine().equals(semaineDTO.getNumeroSemaine())) {
                // selection
                semaineDTO.setVraiOuFauxSelection(true);
                semaineDTO.setCouleur(TypeCouleurJour.YELLOW.getId());
            } else {
                semaineDTO.setVraiOuFauxSelection(false);
                semaineDTO.setCouleur(GenerateurDTO.getMapAlternanceCouleur()
                        .get(semaineDTO.getTypeJourEmploi()));
            }
        }
        this.rechercher();
    }

    /**
     * Génération de l'entête de l'emploi du temps.
     */
    private void genererEnteteEmploi() {
        final Set<Date> listeVaque = form.getListeVaque();

        final Date lundi = form.getSemaineSelectionnee().getLundi();
        final Map<String, EnteteEmploiJoursDTO> enteteEmploi = new HashMap<String, EnteteEmploiJoursDTO>();

        enteteEmploi.put(TypeJour.LUNDI.name(),
                new EnteteEmploiJoursDTO(!listeVaque.contains(lundi), lundi,
                        DateUtils.format(lundi, "dd/MM")));

        final Date dateMardi = DateUtils.ajouter(lundi, Calendar.DAY_OF_MONTH,
                1);
        enteteEmploi.put(TypeJour.MARDI.name(),
                new EnteteEmploiJoursDTO(!listeVaque.contains(dateMardi),
                        dateMardi, DateUtils.format(dateMardi, "dd/MM")));

        final Date dateMercredi = DateUtils.ajouter(lundi,
                Calendar.DAY_OF_MONTH, 2);
        enteteEmploi.put(TypeJour.MERCREDI.name(),
                new EnteteEmploiJoursDTO(!listeVaque.contains(dateMercredi),
                        dateMercredi, DateUtils.format(dateMercredi, "dd/MM")));

        final Date dateJeudi = DateUtils.ajouter(lundi, Calendar.DAY_OF_MONTH,
                3);
        enteteEmploi.put(TypeJour.JEUDI.name(),
                new EnteteEmploiJoursDTO(!listeVaque.contains(dateJeudi),
                        dateJeudi, DateUtils.format(dateJeudi, "dd/MM")));

        final Date dateVendredi = DateUtils.ajouter(lundi,
                Calendar.DAY_OF_MONTH, 4);
        enteteEmploi.put(TypeJour.VENDREDI.name(),
                new EnteteEmploiJoursDTO(!listeVaque.contains(dateVendredi),
                        dateVendredi, DateUtils.format(dateVendredi, "dd/MM")));

        final Date dateSamedi = DateUtils.ajouter(lundi, Calendar.DAY_OF_MONTH,
                5);
        enteteEmploi.put(TypeJour.SAMEDI.name(),
                new EnteteEmploiJoursDTO(!listeVaque.contains(dateSamedi),
                        dateSamedi, DateUtils.format(dateSamedi, "dd/MM")));

        final Date dateDimanche = DateUtils.ajouter(lundi,
                Calendar.DAY_OF_MONTH, 6);
        enteteEmploi.put(TypeJour.DIMANCHE.name(),
                new EnteteEmploiJoursDTO(!listeVaque.contains(dateDimanche),
                        dateDimanche, DateUtils.format(dateDimanche, "dd/MM")));

        form.setEnteteEmploi(enteteEmploi);
    }

    /**
     * Sélectionner une classe ou un groupe.
     */
    public void classeGroupeSelectionnee() {
        final Date today = DateUtils.getAujourdhui();
        final Date lundiToday = DateUtils.setJourSemaine(today, Calendar.MONDAY);
        
        for(final BarreSemaineDTO semaine : form.getListeBarreSemaine()) {
            if (semaine.getLundi().equals(lundiToday)) {
                semaine.setVraiOuFauxSelection(true);
                form.setSemaineSelectionnee(semaine);
            } else {
                semaine.setVraiOuFauxSelection(false);
            }
        }
        
        naviguerSemaine();

    }

    /**
     * Méthode appelé au moment de la navigation entre semaine et lors de la
     * selection d'un groupe ou d'une classe.
     */
    public void rechercher() {
        this.classeGroupeControl.getForm().setFiltreClasseGroupe("");

        // si la semaine sélectionnée est valide on recherche la grille horaire.
        if (form.getSemaineSelectionnee().getTypeJourEmploi() != null) {
            this.genererEnteteEmploi();
            this.rechercherListeEmploiDeTemps();
        }

        form.setRenderedBarreSemaine(true);
    }

    /**
     * 
     */
    private void rechercherListeEmploiDeTemps() {

        
        // Appel metier pour la récupération des enregistrement depuis la base
        // de données
        List<DetailJourEmploiDTO> listeFromDatabase = new ArrayList<DetailJourEmploiDTO>();

        form.setListeEmploiDeTemps(listeFromDatabase);

        if (!Profil.PARENT.equals(form.getProfilEnCours())
                && !Profil.ELEVE.equals(form.getProfilEnCours())
                && null == classeGroupeControl.getForm().getGroupeClasseSelectionne()) {
            return;
        }
        try {
            listeFromDatabase = ObjectUtils.clone(emploiService
                    .findEmploiConsolidation(this.genererQORecherche())
                    .getValeurDTO());
        } catch (MetierException e) {
            log.debug("Echec de la recherche des séances : {0}", e.getMessage());
        }

        for (final DetailJourEmploiDTO detailJourEmploiDTO : listeFromDatabase) {

            // Formatage du titre de la matière
            final String matiereCoupe = org.apache.commons.lang.StringUtils
                    .abbreviate(detailJourEmploiDTO.getMatiere().getIntitule(),
                            15);
            detailJourEmploiDTO.setMatiereCourt(matiereCoupe);

        }

        form.setListeEmploiDeTemps(listeFromDatabase);

    }

    /**
     * Génération du QO de recherche.
     * 
     * @return le QO de recherche.
     */
    private RechercheEmploiQO genererQORecherche() {
        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();

        // contruction du QO contenant les critères de recherche
        final RechercheEmploiQO rechercheEmploiQO = new RechercheEmploiQO();
        final Profil profilEnCours = form.getProfilEnCours();

        BarreSemaineDTO semaine = form.getSemaineSelectionnee();

        rechercheEmploiQO.setDateDebut(semaine.getLundi());
        rechercheEmploiQO.setDateFin(semaine.getDimanche());

        final TypeSemaine typeSemaine = form.getSemaineSelectionnee()
                .getTypeJourEmploi();
        rechercheEmploiQO.setTypeSemaine(typeSemaine);

        if (!Profil.ELEVE.equals(profilEnCours)
                && !Profil.PARENT.equals(profilEnCours) &&
                null != classeGroupeControl.getForm()
                .getGroupeClasseSelectionne()) {
            rechercheEmploiQO.setIdGroupeOuClasse(classeGroupeControl.getForm()
                    .getGroupeClasseSelectionne().getId());
            rechercheEmploiQO.setVraiOuFauxClasse(classeGroupeControl.getForm()
                    .getGroupeClasseSelectionne().getVraiOuFauxClasse());

            rechercheEmploiQO.setListeDeGroupes(null);
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(classeGroupeControl.getForm()
                    .getListeGroupe())) {
                final Set<Integer> listeIdgp = new HashSet<Integer>();
                for (final GroupeDTO gp : classeGroupeControl.getForm().getListeGroupe()) {
                    if (gp.getSelectionner()) {
                        listeIdgp.add(gp.getId());
                    }
                }

                if (!org.apache.commons.collections.CollectionUtils
                        .isEmpty(listeIdgp)) {
                    rechercheEmploiQO.setListeDeGroupes(listeIdgp);
                } else {
                    rechercheEmploiQO.setListeDeGroupes(null);
                }
            }
        } else {
            final Integer idEleve = utilisateurDTO.getUserDTO()
                    .getIdentifiant();
            rechercheEmploiQO.setIdGroupeOuClasse(groupeClasseService
                    .findIdClasseEleve(idEleve));
            rechercheEmploiQO.setListeDeGroupes(groupeClasseService
                    .findIdGroupesEleve(idEleve));
            rechercheEmploiQO.setVraiOuFauxClasse(true);
        }

        rechercheEmploiQO.setIdEtablissement(utilisateurDTO
                .getIdEtablissement());

        return rechercheEmploiQO;
    }

    

    /**
     * Methode d'impression de l'emploi du temps.
     */
    public void printEmp() {
        try {

            final PrintEmploiQO printEmploiQO = new PrintEmploiQO();

            printEmploiQO.setTypeGroupeSelectionne(classeGroupeControl.getForm()
                    .getTypeGroupeSelectionne());
            printEmploiQO.setGroupeClasseDTO(classeGroupeControl.getForm().getGroupeClasseSelectionne());
            printEmploiQO.setListeGroupe(classeGroupeControl.getForm().getListeGroupe());
            printEmploiQO.setListeEmploiDeTemps(form.getListeEmploiDeTemps());

            final UtilisateurDTO utilisateurDTO = ContexteUtils
                    .getContexteUtilisateur().getUtilisateurDTO();

            printEmploiQO.setJourOuvreAccessible(GenerateurDTO
                    .getListeJourOuvreFromDb(utilisateurDTO
                            .getJoursOuvresEtablissement()));

            printEmploiQO.setHeureDebut(form.getEtablissement().getHeureDebut());

            printEmploiQO.setHeureFin(form.getEtablissement().getHeureFin() + 1);

            printEmploiQO.setJourOuvreAccessible(GenerateurDTO
                    .getListeJourOuvreFromDb(utilisateurDTO
                            .getJoursOuvresEtablissement()));

            printEmploiQO.setListeEmploiDeTemps(form.getListeEmploiDeTemps());

            if (Profil.ELEVE.equals(form.getProfilEnCours())) {
                printEmploiQO
                        .setTypeEditionEmploiTemps(TypeEditionEmploiTemps.Eleve);
            } else if (Profil.PARENT.equals(form.getProfilEnCours())) {
                printEmploiQO
                        .setTypeEditionEmploiTemps(TypeEditionEmploiTemps.Parent);
            } else {
                printEmploiQO
                        .setTypeEditionEmploiTemps(TypeEditionEmploiTemps.ClasseOuGroupe);
            }

            printEmploiQO.setUtilisateurDTO(ContexteUtils
                    .getContexteUtilisateur().getUtilisateurDTO());

            switch (form.getSemaineSelectionnee().getTypeJourEmploi()) {
            case IMPAIR:
                printEmploiQO.setTypeSemaine(false);
                break;
            case PAIR:
                printEmploiQO.setTypeSemaine(true);
                break;
            case NEUTRE:
            default:
                printEmploiQO.setTypeSemaine(null);
            }
            
            printEmploiQO.setEcranOrigin(PrintEmploiQO.EcranOrigin.CONSOLIDATION);
            printEmploiQO.setDateDebut(form.getSemaineSelectionnee().getLundi());
            printEmploiQO.setDateFin(form.getSemaineSelectionnee().getDimanche());

            ReportUtils.stream(emploiService.printEmploiDuTemps(printEmploiQO));
        } catch (Exception e) {
            log.debug("Erreur lors de l'édition des comptes");
        }

    }

    /**
     * Charger l'emploi de temps a partir du index affecté.
     */
    public void chargerDetailJourEmploiDTOSel() {

        Integer index = form.getDetailJourEmploiDTOSelIndex();

        log.info("chargerDetailJourEmploiDTOSel : " + index);

        final List<DetailJourEmploiDTO> listeEmploiDeTemps = form
                .getListeEmploiDeTemps();

        form.setDetailJourEmploiDTOSel(listeEmploiDeTemps.get(index));
    }

    /**
     * @param event
     */
    public void classeGroupeTypeSelectionne() {
        // Reset de l'emploi du temps
        form.setListeEmploiDeTemps(new ArrayList<DetailJourEmploiDTO>());
        form.setRenderedBarreSemaine(false);
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

    /**
     * @param classeGroupeControl the classeGroupeControl to set
     */
    public void setClasseGroupeControl(ClasseGroupeControl classeGroupeControl) {
        this.classeGroupeControl = classeGroupeControl;
    }

}
