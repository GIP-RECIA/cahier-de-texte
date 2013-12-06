/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Test.java,v 1.2 2009/03/17 17:19:51 ent_breyton Exp $
 */

package org.crlr.test.services;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.PeriodeEmploiDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.dto.application.emploi.RechercheEmploiQO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.dto.application.sequence.SaveSequenceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.business.EnseignantHibernateBusinessService;
import org.crlr.metier.entity.EnseignantBean;
import org.crlr.metier.facade.AnneeScolaireFacadeService;
import org.crlr.metier.facade.EmploiFacadeService;
import org.crlr.metier.facade.EnseignementFacadeService;
import org.crlr.metier.facade.GroupeClasseFacadeService;
import org.crlr.metier.facade.SeanceFacadeService;
import org.crlr.metier.facade.SequenceFacadeService;
import org.crlr.test.AbstractMetierTest;
import org.crlr.utils.DateUtils;
import org.crlr.web.dto.TypeSemaine;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Classe de test temporaire.
 * 
 * @author breytond.
 * @version $Revision: 1.2 $
 */
public class Test extends AbstractMetierTest {
    /**
     *
     */
    @Autowired
    EmploiFacadeService emploiService;

    @Autowired
    EnseignantHibernateBusinessService enseignantService;

    @Autowired
    SequenceFacadeService sequenceService;

    @Autowired
    EnseignementFacadeService enseignementService;

    @Autowired
    GroupeClasseFacadeService groupeClasseService;

    @Autowired
    AnneeScolaireFacadeService anneeScolaireService;
    
    @Autowired
    SeanceFacadeService seanceFacade;
    
    public void testSeancePlanning() throws MetierException {
        final RechercheSeanceQO rechercheSeanceQO = new RechercheSeanceQO();
        rechercheSeanceQO.setIdEleve(205);
        rechercheSeanceQO.setIdEtablissement(114);
        rechercheSeanceQO.setDateDebut(DateUtils.creer(2012, 3, 1));
        rechercheSeanceQO.setDateFin(DateUtils.creer(2012, 3, 31));
                
        final ResultatDTO<List<SeanceDTO>> listeSeanceDTO =
                seanceFacade.findSeanceForPlanning(rechercheSeanceQO);
        /*
        reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(
                    "/com/company/app/dao/sql/SqlQueryFile.sql")));*/
        log.info("res {0}", listeSeanceDTO.getValeurDTO().size());
    }

    public void testCreateSequence() throws MetierException {

        EnseignementDTO matiere = enseignementService.find(7);

        GroupesClassesDTO classe = groupeClasseService.findClasse(85);

        SaveSequenceQO saveSequence = new SaveSequenceQO();
        saveSequence.setEnseignementSelectionne(matiere);
        saveSequence.setClasseGroupeSelectionne(classe);
        saveSequence.setIntitule("Test");
        saveSequence.setDescription("Desc");
        saveSequence.setDateDebut(DateUtils.creer(2012, 2, 24));
        saveSequence.setDateFin(DateUtils.creer(2012, 4, 24));

        saveSequence.setIdEnseignant(536);
        saveSequence.setIdEtablissement(114);
        sequenceService.saveSequence(saveSequence);
    }

    public void testCreatePeriode() throws MetierException {
        final String uid = "kbm003u1";
        final Integer etablissementId = 169;

        EnseignantBean enseignant = enseignantService.find(uid);

        List<PeriodeEmploiDTO> listePeriodes = emploiService.findPeriodes(
                enseignant.getId(), etablissementId);

        assertEquals(4, listePeriodes.size());

        List<DetailJourEmploiDTO> listeJours = emploiService.findEmploi(
                enseignant.getId(), etablissementId, null,
                listePeriodes.get(3).getId()).getValeurDTO();

        assertEquals(3, listeJours.size());

        listeJours = emploiService.findEmploi(enseignant.getId(),
                etablissementId, null, listePeriodes.get(0).getId())
                .getValeurDTO();

        assertEquals(0, listeJours.size());

        PeriodeEmploiDTO newPeriode = new PeriodeEmploiDTO();
        newPeriode.setDateDebut(DateUtils.creer(2012, 11, 15));
        newPeriode.setIdEnseignant(enseignant.getId());
        newPeriode.setIdEtablissement(169);

        emploiService.creerPeriode(newPeriode, 2, listePeriodes.get(3).getId());

        listeJours = emploiService.findEmploi(enseignant.getId(),
                etablissementId, null, listePeriodes.get(0).getId())
                .getValeurDTO();

        listePeriodes = emploiService.findPeriodes(enseignant.getId(),
                etablissementId);

        listeJours = emploiService.findEmploi(enseignant.getId(),
                etablissementId, null, listePeriodes.get(4).getId())
                .getValeurDTO();

        assertEquals(3, listeJours.size());

        emploiService.deletePeriode(listePeriodes.get(4));
    }

    public void st() throws MetierException {
        /*
         * for(int d = 3; d <= 9; ++d) { List<Integer> periodeIds =
         * emploiService.findValidesPeriodeIds(169, new GregorianCalendar(2012,
         * 11, d).getTime());
         * 
         * log.info("periode ids {0}", periodeIds); }
         */

        GregorianCalendar gc = new GregorianCalendar(2012, 0, 1);
        gc.setFirstDayOfWeek(Calendar.MONDAY);

        log.info("# week {0}", gc.get(Calendar.WEEK_OF_YEAR));

        gc = new GregorianCalendar(2012, 11, 31);
        gc.setFirstDayOfWeek(Calendar.MONDAY);

        log.info("# week {0}", gc.get(Calendar.WEEK_OF_YEAR));

        gc = new GregorianCalendar(2012, 11, 3);
        gc.setFirstDayOfWeek(Calendar.MONDAY);

        log.info("# week {0}", gc.get(Calendar.WEEK_OF_YEAR));

        RechercheEmploiQO re = new RechercheEmploiQO();

        re.setIdEtablissement(169);
        re.setIdGroupeOuClasse(68);
        re.setVraiOuFauxClasse(true);
        re.setTypeSemaine(TypeSemaine.PAIR);
        re.setDateDebut(new GregorianCalendar(2012, 11, 3).getTime());
        re.setDateFin(new GregorianCalendar(2012, 11, 9).getTime());

        List<DetailJourEmploiDTO> events = emploiService
                .findEmploiConsolidation(re).getValeurDTO();

        log.info("Num events {0}", events.size());

        for (DetailJourEmploiDTO event : events) {
            log.info("Event jour {0}.  id {1}", event.getJour().name(),
                    event.getId());
        }

    }
}
