/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.test.services;

import junit.framework.Assert;

import org.crlr.dto.application.base.EtablissementComplementDTO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.OuvertureQO;
import org.crlr.dto.application.base.TypeReglesAdmin;
import org.crlr.exception.metier.MetierException;
import org.crlr.exception.metier.MetierRuntimeException;
import org.crlr.services.EtablissementService;
import org.crlr.test.AbstractMetierTest;
import org.crlr.utils.AssertionException;

import static org.junit.Assert.*;
/**
 * DOCUMENT ME!
 *
 * @author FMOULIN
 */
public class EtablissementServiceTest extends AbstractMetierTest {
    EtablissementService etablissementService;

    /**
     * DOCUMENT ME!
     *
     * @param etablissementService the etablissementService to set
     */
    public void setEtablissementService(EtablissementService etablissementService) {
        this.etablissementService = etablissementService;
    }

    /**
     * test saveEtablissementJoursOuvres.  fonctionne mais enregistre les trucs
     * bidons.
     *
     * @throws MetierException .
     */
    public void testSaveEtablissementJoursOuvres()
                                          throws MetierException {
        final EtablissementDTO etabQO = new EtablissementDTO();
        etabQO.setId(37);
        etabQO.setJoursOuvres("Fermé");
        try {
            etablissementService.saveEtablissementJoursOuvres(etabQO);
        } catch (AssertionException e) {
            log.debug("testSaveEtablissementJoursOuvres OK : AssertioNException : {0}",
                      e.getMessage());
            return;
        }
        throw new MetierException("testSaveEtablissementJoursOuvres NOK - Sauvegarde d'une chaîne inutile.");
    }

    /**
     * test saveEtablissementJoursOuvres. avec parametre null
     *
     * @throws MetierException .
     */
    public void testSaveEtablissementJoursOuvresNull()
                                              throws MetierException {
        try {
            etablissementService.saveEtablissementJoursOuvres(null);
        } catch (AssertionException e) {
            log.debug("testSaveEtablissementJoursOuvresNull OK : AssertioNException : {0}",
                      e.getMessage());
        }
    }

    /**
     * test saveEtablissementJoursOuvres. champs null
     *
     * @throws MetierException .
     */
    public void testSaveEtablissementJoursOuvresChampsNull()
        throws MetierException {
        final EtablissementDTO etabQO = new EtablissementDTO();
        try {
            etablissementService.saveEtablissementJoursOuvres(etabQO);
        } catch (AssertionException e) {
            log.debug("testSaveEtablissementJoursOuvresNull OK : AssertioNException : {0}",
                      e.getMessage());
        }
    }

    /**
     * test saveEtablissementJoursOuvres. idEtab
     *
     * @throws MetierException .
     */
    public void testSaveEtablissementJoursOuvresIdEtab()
                                                throws MetierException {
        final EtablissementDTO etabQO = new EtablissementDTO();
        etabQO.setId(98);
        try {
            etablissementService.saveEtablissementJoursOuvres(etabQO);
        } catch (MetierException e) {
            log.debug("testSaveEtablissementJoursOuvresIdEtab OK : Jour(s) Ouvré(s) non défini : {0}",
                      e.getMessage());
        }
    }

    /**
     * test saveEtablissementJoursOuvres. idEtab inexistant
     *
     * @throws MetierException .  fonction mais n'a rien fait en base, idEtab inexistant
     *         sur un update.
     */
    public void testSaveEtablissementJoursOuvresEtabInexistant()
        throws MetierException {
        final EtablissementDTO etabQO = new EtablissementDTO();
        etabQO.setId(987654321);
        etabQO.setJoursOuvres("LUNDI|");
        try {
            etablissementService.saveEtablissementJoursOuvres(etabQO);
        } catch (MetierException e) {
            log.debug("testSaveEtablissementJoursOuvresEtabInexistant OK : Jour(s) Ouvré(s) non défini : {0}",
                      e.getMessage());
            return;
        }
        throw new MetierException("testSaveEtablissementJoursOuvresEtabInexistant NOK");
    }

    /**
     * test sur findDonneeComplementaireEtablissement. avec null ok
     */
    public void testFindDonneeComplementaireEtablissementNull() {
        try {
            etablissementService.findDonneeComplementaireEtablissement(null);
        } catch (AssertionException e) {
            log.debug("testFindDonneeComplementaireEtablissementNull OK : AssertionException : {0}",
                      e.getMessage());
        }
    }

    /**
     * test sur findDonneeComplementaireEtablissement.  ok
     */
    public void testFindDonneeComplementaireEtablissement() {
        final EtablissementComplementDTO etabcompl =
            etablissementService.findDonneeComplementaireEtablissement(98);
        assertEquals(null, etabcompl.getHoraireCours());
        assertEquals(null, etabcompl.getDureeCours());
        assertEquals(null, etabcompl.getHeureDebutCours());
        assertEquals(null, etabcompl.getHeureFinCours());
    }

    /**
     * test sur findDonneeComplementaireEtablissement avec etablissement
     * inexistant.  ok
     *
     * @throws MetierException exception bloquante.
     */
    public void testFindDonneeComplementaireEtablissementInexistant()
        throws MetierException {
        try {
            etablissementService.findDonneeComplementaireEtablissement(987654321);
        } catch (MetierRuntimeException e) {
            log.debug("testFindDonneeComplementaireEtablissementInexistant ok");
            return;
        }
        throw new MetierException("testFindDonneeComplementaireEtablissementInexistant NOK - etab inexistant");
    }

    

    /**
     * test findAlternanceSemaine. avec null
     */
    public void testFindAlternanceSemaineNull() {
        try {
            etablissementService.findAlternanceSemaine(null);
        } catch (AssertionException e) {
            log.debug("testFindAlternanceSemaineNull OK : AssertionException : {0}",
                      e.getMessage());
        }
    }

    /**
     * test findAlternanceSemaine. avec idEtablissement inexistant
     */
    public void testFindAlternanceSemaineEtabInexistant() {
        try {
            etablissementService.findAlternanceSemaine(123456790);
        } catch (MetierRuntimeException e) {
            log.debug("testFindAlternanceSemaineEtabInexistant OK : MetierRuntimeException : {0}",
                      e.getMessage());
        }
    }

  
    /**
     * test saveEtablissementAlternance. avec parametre Null;
     *
     * @throws MetierException la bloquante.
     */
    public void testSaveEtablissementAlternanceNull()
                                             throws MetierException {
        try {
            etablissementService.saveEtablissementAlternance(null);
        } catch (AssertionException e) {
            log.debug("testSaveEtablissementAlternanceNull OK : AssertionException : {0}",
                      e.getMessage());
        }
    }

    /**
     * test saveEtablissementAlternance.
     *
     * @throws MetierException la bloquante.
     */
    public void testSaveEtablissementAlternanceChampNull()
        throws MetierException {
        final EtablissementDTO etablissementQO = new EtablissementDTO();

        try {
            etablissementService.saveEtablissementAlternance(etablissementQO);
        } catch (AssertionException e) {
            log.debug("testSaveEtablissementAlternanceNull OK : AssertionException : {0}",
                      e.getMessage());
        }
    }

    /**
     * test saveEtablissementAlternance.
     *
     * @throws MetierException la bloquante.
     */
    public void testSaveEtablissementAlternanceIdEtab()
                                               throws MetierException {
        final EtablissementDTO etablissementQO = new EtablissementDTO();
        etablissementQO.setId(98);
        try {
            etablissementService.saveEtablissementAlternance(etablissementQO);
        } catch (AssertionException e) {
            log.debug("testSaveEtablissementAlternanceNull OK : AssertionException : {0}",
                      e.getMessage());
        }
    }

    /**
     * test saveEtablissementOuverture. avec null
     *
     * @throws MetierException le bloooooooooooooooooooooooooooooooquaaaaaaaaaaaaaaant !
     */
    public void testSaveEtablissementOuvertureNull()
                                            throws MetierException {
        try {
            etablissementService.saveEtablissementOuverture(null);
        } catch (AssertionException e) {
            log.debug("testSaveEtablissementOuvertureNull OK : AssertionException : {0}",
                      e.getMessage());
        }
    }

    /**
     * test saveEtablissementOuverture. champs null
     *
     * @throws MetierException la bloquante.
     */
    public void testSaveEtablissementOuvertureChampNull()
                                                 throws MetierException {
        final OuvertureQO ouvertureQO = new OuvertureQO(null, null);
        try {
            etablissementService.saveEtablissementOuverture(ouvertureQO);
        } catch (AssertionException e) {
            log.debug("testSaveEtablissementOuvertureChampNull OK : AssertionException : {0}",
                      e.getMessage());
        }
    }

    /**
     * test saveEtablissementOuverture. idEtab
     *
     * @throws MetierException la bloquante.
     */
    public void testSaveEtablissementOuvertureOuvert()
                                              throws MetierException {
        final OuvertureQO ouvertureQO = new OuvertureQO(98, true);
        etablissementService.saveEtablissementOuverture(ouvertureQO);
    }

    /**
     * test saveEtablissementOuverture. idEtab
     *
     * @throws MetierException la bloquante.
     */
    public void testSaveEtablissementOuvertureEtabInexistant()
        throws MetierException {
        final OuvertureQO ouvertureQO = new OuvertureQO(987654321, true);
        try {
            etablissementService.saveEtablissementOuverture(ouvertureQO);
            Assert.fail("L'établissement n'existe pas, il est impossible de l'ouvrir");
        } catch (MetierException e) {
            log.debug("testSaveEtablissementOuvertureEtabInexistant OK : MetierException : {0}",
                      e.getMessage());
        }
    }

    /**
     * test findHorairesCoursEtablissement.
     *
     * @throws MetierException bloohooohooohoooquaaaaaaaaaaahhahhaaante.
     */
    public void testFindHorairesCoursEtablissementNuul()
                                                throws MetierException {
        try {
            etablissementService.findHorairesCoursEtablissement(null);
        } catch (AssertionException e) {
            log.debug("testFindHorairesCoursEtablissementNuul OK : AssertionException : {0}",
                      e.getMessage());
        }
    }

    /**
     * test findHorairesCoursEtablissement.
     *
     * @throws MetierException bloohooohooohoooquaaaaaaaaaaahhahhaaante.
     */
    public void testFindHorairesCoursEtablissementIdInconnu()
        throws MetierException {
        try {
            etablissementService.findHorairesCoursEtablissement(98);
        } catch (MetierRuntimeException e) {
            log.debug("MetierRuntimeException : {0}", e.getMessage());
        }
    }

    

    /**
     * test findJourOuvreEtablissementParCode.
     *
     * @throws MetierException la bloquante.
     */
    public void testFindJourOuvreEtablissementParCodeNull()
        throws MetierException {
        try {
            etablissementService.findJourOuvreEtablissementParCode(null, null, null);
        } catch (AssertionException e) {
            log.debug("testFindJourOuvreEtablissementParCodeNull OK : AssertionException : {0}",
                      e.getMessage());
            return;
        }
        throw new MetierException("erreur parametre null !");
    }

   

    /**
     * test findJourOuvreEtablissementParCode.
     *
     * @throws MetierException la bloquante.
     */
    public void testFindJourOuvreEtablissementParCodeSirenBidon()
        throws MetierException {
        try {
            etablissementService.findJourOuvreEtablissementParCode("azertyuiopqsdfg",
                                                                   false, null);
        } catch (MetierRuntimeException e) {
            log.debug("MetierRuntimeException : {0}", e.getMessage());
        }
    }

    /**
     * test findJourOuvreEtablissementParCode.
     *
     * @throws MetierException la bloquante.
     */
    public void testSauvegarderGrilleHoraire()
        throws MetierException {
        EtablissementDTO etablissementDTO = new EtablissementDTO();
        etablissementDTO.setId(114);
        etablissementDTO.setFractionnement(4);
        etablissementDTO.setHeureDebut(8);
        etablissementDTO.setMinuteDebut(0);
        etablissementDTO.setHeureFin(18);
        etablissementDTO.setHoraireCours("9:00#10:00|10:05#10:20|10:02#10:06");
        
        boolean hasException = false;
        
        try {
            etablissementService.saveComplementEtablissement(etablissementDTO);
        } catch (MetierException e) {
            log.debug("MetierRuntimeException : {0}", e.getMessage());
            assertEquals(TypeReglesAdmin.ADMIN_22.name(), e.getConteneurMessage().iterator().next().getCode());
            hasException = true;
        }
        
        assertTrue(hasException);
        hasException = false;
        
        etablissementDTO.setHoraireCours("9:00#10:00|10:06#10:02");
        
        try {
            etablissementService.saveComplementEtablissement(etablissementDTO);
        } catch (MetierException e) {
            log.debug("MetierRuntimeException : {0}", e.getMessage());
            assertEquals(TypeReglesAdmin.ADMIN_23.name(), e.getConteneurMessage().iterator().next().getCode());
            hasException = true;
        }
        
        assertTrue(hasException);
        hasException = false;
        
        etablissementDTO.setHoraireCours("9:00#10:00|10:02#10:06");
        etablissementDTO.setHeureDebut(10);
        etablissementDTO.setMinuteDebut(0);
        etablissementDTO.setHeureFin(11);
        
        try {
            etablissementService.saveComplementEtablissement(etablissementDTO);
        } catch (MetierException e) {
            log.debug("MetierRuntimeException : {0}", e.getMessage());
            assertEquals(TypeReglesAdmin.ADMIN_24.name(), e.getConteneurMessage().iterator().next().getCode());
            hasException = true;
        }
        
        assertTrue(hasException);
        hasException = false;
        
        etablissementDTO.setHoraireCours("9:00#10:00|10:02#10:06");
        etablissementDTO.setHeureDebut(9);
        etablissementDTO.setMinuteDebut(0);
        etablissementDTO.setHeureFin(11);
        
        try {
            etablissementService.saveComplementEtablissement(etablissementDTO);
        } catch (MetierException e) {
            log.debug("MetierRuntimeException : {0}", e.getMessage());
            assertTrue(false);
        }
    }
}
