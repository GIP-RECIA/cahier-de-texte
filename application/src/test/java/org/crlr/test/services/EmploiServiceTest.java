/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirServiceTest.java,v 1.10 2010/04/01 11:06:15 ent_breyton Exp $
 */

package org.crlr.test.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.PeriodeEmploiDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.dto.application.emploi.RechercheEmploiQO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.EmploiService;
import org.crlr.test.AbstractMetierTest;
import org.crlr.utils.AssertionException;
import org.crlr.utils.DateUtils;
import org.crlr.web.dto.GrilleHoraireDTO;
import org.springframework.util.Assert;

/**
 * @author root
 *
 */
public class EmploiServiceTest extends AbstractMetierTest {

    EmploiService emploiService;

    /**
     * @param emploiService the emploiService to set
     */
    public void setEmploiService(EmploiService emploiService) {
        this.emploiService = emploiService;
    }
    
    /**
     * test sur saveEmpliDuTemps avec parametre null.
     * @throws MetierException bloquante.
     */
    public void testSaveEmploiDuTempsNull() throws MetierException{
        try {
            emploiService.saveEmploiDuTemps(null);
        } catch (AssertionException e) {
            log.debug(" testSaveEmploiDuTempsNull OK ");
            return;
        }
        throw new MetierException("testSaveEmploiDuTempsNull NOK");
        
    }
    
   
    
    /**
     * test sur findEmploi().
     * avec null,null,null
     * @throws MetierException exception bloquante.
     */
    public void testFindEmploiNull() throws MetierException{
        try{
            final ResultatDTO<List<DetailJourEmploiDTO>> res = emploiService.findEmploi(null, null, null, null);
            if(CollectionUtils.isEmpty(res.getValeurDTO())){
                log.debug("testFindEmploiNull OK : retourne rien");
            }else{
                throw new MetierException("testFindEmploiNull NOK : retourne des choses avec null en entrée");
            }
        }catch (AssertionException e) {
            log.debug("testFindEmploiNull OK : AssertionException : {0}", e.getMessage());
        }
    }
    
 
    
    /**
     * test l'appel à la fonction findEmploiConsolidation(RechercheEmploiQO).
     * avec parmetre null.
     * @throws MetierException bloquante.
     */
    public void testFindEmploiConsolidationNull() throws MetierException{
        try{
        emploiService.findEmploiConsolidation(null);
        }catch (AssertionException e) {
            log.debug("testFindEmploiConsolidationNull OK - AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test l'appel à la fonction findEmploiConsolidation(RechercheEmploiQO).
     * avec les champs du parmetre null.
     * @throws MetierException bloquante.
     */
    public void testFindEmploiConsolidationChampsNull() throws MetierException{
        try{
            final RechercheEmploiQO rechercheEmploiQO = new RechercheEmploiQO();
            final ResultatDTO<List<DetailJourEmploiDTO>> res = emploiService.findEmploiConsolidation(rechercheEmploiQO);
            assertEquals(0, res.getValeurDTO().size());
        }catch (AssertionException e) {
            log.debug("testFindEmploiConsolidationChampsNull OK - AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test l'appel à la fonction findEmploiConsolidation(RechercheEmploiQO).
     * avec idEtablissement.
     * @throws MetierException bloquante.
     */
    public void testFindEmploiConsolidationIdEtab() throws MetierException{
        try{
            final RechercheEmploiQO rechercheEmploiQO = new RechercheEmploiQO();
            rechercheEmploiQO.setIdEtablissement(98);
            final ResultatDTO<List<DetailJourEmploiDTO>> res = emploiService.findEmploiConsolidation(rechercheEmploiQO);
            assertEquals(0, res.getValeurDTO().size());
        }catch (AssertionException e) {
            log.debug("testFindEmploiConsolidationChampsNull OK - AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test l'appel à la fonction findEmploiConsolidation(RechercheEmploiQO).
     * avec typeSemaine.
     * @throws MetierException bloquante.
     */
    public void testFindEmploiConsolidationTypeSemaine() throws MetierException{
        try{
            final RechercheEmploiQO rechercheEmploiQO = new RechercheEmploiQO();
            rechercheEmploiQO.setTypeSemaine(org.crlr.web.dto.TypeSemaine.PAIR);
            final ResultatDTO<List<DetailJourEmploiDTO>> res = emploiService.findEmploiConsolidation(rechercheEmploiQO);
            assertEquals(0, res.getValeurDTO().size());
        }catch (AssertionException e) {
            log.debug("testFindEmploiConsolidationChampsNull OK - AssertionException : {0}", e.getMessage());
        }
    }
    
  
    
    
    /**
     * test l'appel à la fonction findSeanceEmploiSemaine.
     * avec parametre null
     */
    public void testFindSeanceEmploiSemaineNull(){
        try{
            emploiService.findSeanceEmploiSemaine(null);
        }catch (AssertionException e) {
            log.debug("testFindSeanceEmploiSemaineNull OK - AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test l'appel à la fonction findSeanceEmploiSemaine.
     * avec champs null
     */
    public void testFindSeanceEmploiSemaineChampsNull(){
        try{
            final RechercheSeanceQO rechercheSeanceQO = new RechercheSeanceQO();
            emploiService.findSeanceEmploiSemaine(rechercheSeanceQO);
        }catch (AssertionException e) {
            log.debug("testFindSeanceEmploiSemaineChampsNull OK - AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test l'appel à la fonction findSeanceEmploiSemaine.
     * 
     */
    public void testFindSeanceEmploiSemaineOk(){
            final RechercheSeanceQO rechercheSeanceQO = new RechercheSeanceQO();
            rechercheSeanceQO.setDateDebut(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
            rechercheSeanceQO.setDateFin(DateUtils.creer(2011, Calendar.JULY, 1));
            rechercheSeanceQO.setIdEtablissement(98);
            rechercheSeanceQO.setIdEnseignant(1335);
            final Map<String, Integer> map = emploiService.findSeanceEmploiSemaine(rechercheSeanceQO);
            assertEquals(13, map.size());
    }
    
    /**
     * test l'appel à la fonction findSeanceEmploiSemaine.
     * avec enseignant pas dans l'etablissement.
     */
    public void testFindSeanceEmploiSemaineEnsNonEtab(){
            final RechercheSeanceQO rechercheSeanceQO = new RechercheSeanceQO();
            rechercheSeanceQO.setDateDebut(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
            rechercheSeanceQO.setDateFin(DateUtils.creer(2011, Calendar.JULY, 1));
            rechercheSeanceQO.setIdEtablissement(99);
            rechercheSeanceQO.setIdEnseignant(1335);
            final Map<String, Integer> map = emploiService.findSeanceEmploiSemaine(rechercheSeanceQO);
            assertEquals(0, map.size());
    }
    
    /**
     * test l'appel à la fonction findSeanceEmploiSemaine.
     * avec enseignant pas dans l'etablissement.
     */
    public void testFindSeanceEmploiSemaineFinAvDebut(){
            final RechercheSeanceQO rechercheSeanceQO = new RechercheSeanceQO();
            rechercheSeanceQO.setDateDebut(DateUtils.creer(2011, Calendar.SEPTEMBER, 1));
            rechercheSeanceQO.setDateFin(DateUtils.creer(2010, Calendar.JULY, 1));
            rechercheSeanceQO.setIdEtablissement(98);
            rechercheSeanceQO.setIdEnseignant(1335);
            final Map<String, Integer> map = emploiService.findSeanceEmploiSemaine(rechercheSeanceQO);
            assertEquals(0, map.size());
    }
    
    /**
     * test l'appel à la fonction findSeanceEmploiSemaine.
     * avec enseignant inexistant.
     */
    public void testFindSeanceEmploiSemaineEnsInexistant(){
            final RechercheSeanceQO rechercheSeanceQO = new RechercheSeanceQO();
            rechercheSeanceQO.setDateDebut(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
            rechercheSeanceQO.setDateFin(DateUtils.creer(2011, Calendar.JULY, 1));
            rechercheSeanceQO.setIdEtablissement(98);
            rechercheSeanceQO.setIdEnseignant(133587542);
            final Map<String, Integer> map = emploiService.findSeanceEmploiSemaine(rechercheSeanceQO);
            assertEquals(0, map.size());
    }
    
    /**
     * test l'appel à la fonction findSeanceEmploiSemaine.
     * avec l'etablissement inexistant.
     */
    public void testFindSeanceEmploiSemaineEtabInexistant(){
            final RechercheSeanceQO rechercheSeanceQO = new RechercheSeanceQO();
            rechercheSeanceQO.setDateDebut(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
            rechercheSeanceQO.setDateFin(DateUtils.creer(2011, Calendar.JULY, 1));
            rechercheSeanceQO.setIdEtablissement(987654321);
            rechercheSeanceQO.setIdEnseignant(1335);
            final Map<String, Integer> map = emploiService.findSeanceEmploiSemaine(rechercheSeanceQO);
            assertEquals(0, map.size());
    }
    
    /**
     * test de l'appel à la fonction findSeanceEmploiSemainePassee.
     * avec null, null
     */
    public void testFindSeanceEmploiSemainePasseeNull(){
        try{
        emploiService.findSeanceEmploiSemainePassee(null, null);
        }catch (AssertionException e) {
            log.debug("testFindSeanceEmploiSemainePasseeNull OK - AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de l'appel à la fonction findSeanceEmploiSemainePassee.
     * avec RechercheSeanceQO, null
     */
    public void testFindSeanceEmploiSemainePasseeRechSeanNNull(){
        try{
            final RechercheSeanceQO rechercheSeanceQO = new RechercheSeanceQO();
        emploiService.findSeanceEmploiSemainePassee(rechercheSeanceQO, null);
        }catch (AssertionException e) {
            log.debug("testFindSeanceEmploiSemainePasseeRechSeanNNull OK - AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de l'appel à la fonction findSeanceEmploiSemainePassee.
     * avec RechercheSeanceQO, List'<'GrilleHoraireDTO'>'
     * champs null.
     */
    public void testFindSeanceEmploiSemainePasseeChampsNull(){
        try{
            final List<GrilleHoraireDTO> listeHoraire = new ArrayList<GrilleHoraireDTO>();
            final RechercheSeanceQO rechercheSeanceQO = new RechercheSeanceQO();
        emploiService.findSeanceEmploiSemainePassee(rechercheSeanceQO, listeHoraire);
        }catch (AssertionException e) {
            log.debug("testFindSeanceEmploiSemainePasseeChampsNull OK - AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de l'appel à la fonction findSeanceEmploiSemainePassee.
     * avec RechercheSeanceQO, List'<'GrilleHoraireDTO'>'
     * normal.
     * 
     */
    public void testFindSeanceEmploiSemainePasseeOk(){
            final List<GrilleHoraireDTO> listeHoraire = new ArrayList<GrilleHoraireDTO>();
            final GrilleHoraireDTO gHDTO = new GrilleHoraireDTO();
            gHDTO.setHeureDebut(8);
            gHDTO.setHeureFin(18);
            gHDTO.setMinuteDebut(0);
            gHDTO.setMinuteFin(0);
            listeHoraire.add(gHDTO);
            final RechercheSeanceQO rechercheSeanceQO = new RechercheSeanceQO();
            rechercheSeanceQO.setDateDebut(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
            rechercheSeanceQO.setDateFin(DateUtils.creer(2011, Calendar.JULY, 1));
            rechercheSeanceQO.setIdEtablissement(98);
            rechercheSeanceQO.setIdEnseignant(1335);
        
        final Map<String, SeanceDTO> map = emploiService.findSeanceEmploiSemainePassee(rechercheSeanceQO, listeHoraire);
        log.debug("nb ret : {0}", map.size());
        assertEquals(3, map.size());
        assertEquals((Integer)1, map.get("14/09/201008001800").getId());
        assertEquals((Integer)133, map.get("23/11/201008001800").getId());
        assertEquals((Integer)161, map.get("14/12/201008001800").getId());
    }
    
    
    
    public void testFindPeriodes(){
        List<PeriodeEmploiDTO> liste = emploiService.findPeriodes(311,169);
        Assert.isTrue(liste.size() == 2);
}
    
    public void testFindEmpTemps(){
        List<DetailJourEmploiDTO> liste = emploiService.findEmploi(311,169, org.crlr.web.dto.TypeSemaine.PAIR, 1784).getValeurDTO();
        Assert.isTrue(liste.size() == 2);
    }
}
