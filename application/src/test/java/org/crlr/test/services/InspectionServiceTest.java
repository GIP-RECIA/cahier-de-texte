/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: InspectionServiceTest,v 1.0 2011/02/24 10:31:30  $
 */
package org.crlr.test.services;

import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.UserDTOForList;
import org.crlr.dto.application.inspection.DroitInspecteurDTO;
import org.crlr.dto.application.inspection.RechercheDroitInspecteurQO;
import org.crlr.dto.application.inspection.SaveDroitInspecteurQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.InspectionService;
import org.crlr.test.AbstractMetierTest;
import org.crlr.utils.AssertionException;

/**
 * Test JUnit pour InspectionService.
 * @author FMOULIN
 * @version 1.0
 */
public class InspectionServiceTest extends AbstractMetierTest {

    InspectionService inspectionService;

    /** setteur de inspectionService.
     * @param inspectionService the inspectionService to set
     */
    public void setInspectionService(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }
    
    /**
     * test sur findListeInspecteurs.
     * 
     * fonctionne.
     */
    public void testFindListeInspecteurs(){
        final List<UserDTOForList> retour = inspectionService.findListeInspecteurs();
        assertEquals(61, retour.size());
    }
    
   
    
    /**
     * test sur findDroitsInspection avec le parametre null.
     * 
     * fonctionne.
     * 
     * @throws MetierException l'exception super bloquante .
     */
    public void testFindDroitsInspectionNull() throws MetierException{
        try {
            inspectionService.findDroitsInspection(null);
        } catch (AssertionException e) {
            log.debug("testFindDroitsInspectionNull OK");
            return;
        }
        throw new MetierException("testFindDroitsInspectionNull NOK - QO null");
        
    }
    
    /**
     * test sur findDroitsInspection avec le parametre non null.
     * 
     * fonctionne.
     * 
     * @throws MetierException l'exception super bloquante .
     */
    public void testFindDroitsInspectionNonNull() throws MetierException{
        try {
            final RechercheDroitInspecteurQO rechercheDroitInspecteurQO = new RechercheDroitInspecteurQO();
            inspectionService.findDroitsInspection(rechercheDroitInspecteurQO);
        } catch (AssertionException e) {
            log.debug("testFindDroitsInspectionNonNull OK");
            return;
        }
        throw new MetierException("testFindDroitsInspectionNonNull NOK - QO null");
        
    }
    
    /**
     * test sur findDroitsInspection avec recheche directeur.
     * 
     * 
     * 
     * fonctionne.
     * 
     * @throws MetierException l'exception super bloquante .
     */
    public void testFindDroitsInspectionRechercheDir() throws MetierException{
            final RechercheDroitInspecteurQO rechercheDroitInspecteurQO = new RechercheDroitInspecteurQO();
            rechercheDroitInspecteurQO.setIdEtablissement(47);
            rechercheDroitInspecteurQO.setVraiOuFauxRechercheFromDirecteur(true);
            final ResultatDTO<List<DroitInspecteurDTO>> res = inspectionService.findDroitsInspection(rechercheDroitInspecteurQO);
            
            assertEquals(3, res.getValeurDTO().size());
            
            for(DroitInspecteurDTO ins : res.getValeurDTO()){
                log.debug("id ins :{0}",ins.getInspecteur().getIdentifiant());
              
            }
            log.debug("testFindDroitsInspectionRechercheDir OK");
        
    }
    
    /**
     * test sur findDroitsInspection avec recheche inspecteur.
     * 
     * fonctionne.
     * 
     * @throws MetierException l'exception super bloquante .
     */
    public void testFindDroitsInspectionRechercheIns() throws MetierException{
            final RechercheDroitInspecteurQO rechercheDroitInspecteurQO = new RechercheDroitInspecteurQO();
            rechercheDroitInspecteurQO.setIdEtablissement(47);
            rechercheDroitInspecteurQO.setVraiOuFauxRechercheFromDirecteur(false);
            rechercheDroitInspecteurQO.setIdInspecteur(2);
            final ResultatDTO<List<DroitInspecteurDTO>> res = inspectionService.findDroitsInspection(rechercheDroitInspecteurQO);
            
            assertEquals(2, res.getValeurDTO().size());
            
            for(DroitInspecteurDTO ins : res.getValeurDTO()){
                log.debug("id ins :{0}",ins.getInspecteur().getIdentifiant());
           
            }
            log.debug("testFindDroitsInspectionRechercheIns OK");
    }
    
    /**
     * test sur findDroitsInspection avec recheche inspecteur et etab inexistant.
     * 
     * fonctionne.
     * 
     * @throws MetierException l'exception super bloquante .
     */
    public void testFindDroitsInspectionRechercheInsEtabInexistant() throws MetierException{
            final RechercheDroitInspecteurQO rechercheDroitInspecteurQO = new RechercheDroitInspecteurQO();
            rechercheDroitInspecteurQO.setIdEtablissement(987654321);
            rechercheDroitInspecteurQO.setVraiOuFauxRechercheFromDirecteur(false);
            rechercheDroitInspecteurQO.setIdInspecteur(2);
            final ResultatDTO<List<DroitInspecteurDTO>> res = inspectionService.findDroitsInspection(rechercheDroitInspecteurQO);
            
            assertEquals(0, res.getValeurDTO().size());
    }
    
    /**
     * test sur findDroitsInspection avec recheche inspecteur et etab sans inspecteur.
     * 
     * fonctionne.
     * 
     * @throws MetierException l'exception super bloquante .
     */
    public void testFindDroitsInspectionRechercheInsEtabSansIns() throws MetierException{
            final RechercheDroitInspecteurQO rechercheDroitInspecteurQO = new RechercheDroitInspecteurQO();
            rechercheDroitInspecteurQO.setIdEtablissement(98);
            rechercheDroitInspecteurQO.setVraiOuFauxRechercheFromDirecteur(false);
            rechercheDroitInspecteurQO.setIdInspecteur(2);
            final ResultatDTO<List<DroitInspecteurDTO>> res = inspectionService.findDroitsInspection(rechercheDroitInspecteurQO);
            
            assertEquals(0, res.getValeurDTO().size());
    }
    
    /**
     * test sur findDroitsInspection avec recheche directeur et etab sans inspecteur.
     * 
     * fonctionne.
     * 
     * @throws MetierException l'exception super bloquante .
     */
    public void testFindDroitsInspectionRechercheDirEtabSansIns() throws MetierException{
            final RechercheDroitInspecteurQO rechercheDroitInspecteurQO = new RechercheDroitInspecteurQO();
            rechercheDroitInspecteurQO.setIdEtablissement(1);
            rechercheDroitInspecteurQO.setVraiOuFauxRechercheFromDirecteur(true);
            final ResultatDTO<List<DroitInspecteurDTO>> res = inspectionService.findDroitsInspection(rechercheDroitInspecteurQO);
            log.debug("nb ret :{0}",res.getValeurDTO().size());
            
            assertEquals(0, res.getValeurDTO().size());
    }
    
    /**
     * test sur findDroitsInspection avec recheche directeur et etab inexistant.
     * 
     * fonctionne.
     * 
     * @throws MetierException l'exception super bloquante .
     */
    public void testFindDroitsInspectionRechercheDirEtabInexistant() throws MetierException{
            final RechercheDroitInspecteurQO rechercheDroitInspecteurQO = new RechercheDroitInspecteurQO();
            rechercheDroitInspecteurQO.setIdEtablissement(987654321);
            rechercheDroitInspecteurQO.setVraiOuFauxRechercheFromDirecteur(true);
            final ResultatDTO<List<DroitInspecteurDTO>> res = inspectionService.findDroitsInspection(rechercheDroitInspecteurQO);
            
            assertEquals(0, res.getValeurDTO().size());
    }
    
    /**
     * test sur saveDroit QO null.
     * @throws MetierException .
     */
    public void testSaveDroitNull() throws MetierException{
        try{
            inspectionService.saveDroit(null);
        }catch (AssertionException e) {
            log.debug("testSaveDroitNull ok !");
            return;
        }
        throw new MetierException("testSaveDroitNull NOK - QO NULL");
    }
    
    /**
     * test sur saveDroit Champs null.
     * @throws MetierException .
     */
    public void testSaveDroitChampsNull() throws MetierException{
        try{
            final SaveDroitInspecteurQO sDIQO = new SaveDroitInspecteurQO();
            inspectionService.saveDroit(sDIQO);
        }catch (AssertionException e) {
            log.debug("testSaveDroitChampsNull ok ! : {0}", e.getMessage());
            return;
        }
        throw new MetierException("testSaveDroitChampsNull NOK - Champs du QO NULL");
    }
    
 
   
}
