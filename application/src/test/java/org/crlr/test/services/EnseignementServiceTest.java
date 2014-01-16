/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignementServiceTest.java,v 1.4 2009/04/27 14:59:30 vibertd Exp $
 */

package org.crlr.test.services;

import static org.junit.Assert.*;
import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.EnseignementQO;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.EnseignementService;
import org.crlr.test.AbstractMetierTest;
import org.crlr.utils.AssertionException;

/**
 * Test Junit pour EnseignementService.
 *  
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class EnseignementServiceTest extends AbstractMetierTest {
    /** Le service à utiliser. */
    private EnseignementService enseignementService;

    /**
     * Mutateur de EnseignementService.
     *
     * @param enseignementService le nouvel enseignementService
     */
    public void setEnseignementService(EnseignementService enseignementService) {
        this.enseignementService = enseignementService;
    }

    /**
     * Test findEnseignementPopup.
     * avec parametre null.
     *
     * @throws MetierException .
     */
    public void testfindEnseignementPopupNull() throws MetierException {
        try{
            enseignementService.findEnseignementPopup(null);
        }catch (AssertionException e) {
            log.debug("testfindEnseignementPopupNull OK - AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * Test findEnseignementPopup.
     *avec enseignant pas dans l'etablissement
     * @throws MetierException .
     */
    public void testfindEnseignementPopupEnsNoEtab() throws MetierException {
        final RechercheEnseignementPopupQO rechercheEnseignementPopupQO =
            new RechercheEnseignementPopupQO();
        rechercheEnseignementPopupQO.setIdEnseignant(1335);
        rechercheEnseignementPopupQO.setIdEtablissement(99);
        final ResultatDTO<List<EnseignementDTO>> resultat =
            enseignementService.findEnseignementPopup(rechercheEnseignementPopupQO);

        final List<EnseignementDTO> enseignementsDTO = resultat.getValeurDTO();
        assertEquals(enseignementsDTO.size(), 0);
    }
    
    /**
     * Test findEnseignementPopup.
     *avec enseignant pas dans l'etablissement
     * @throws MetierException .
     */
    public void testfindEnseignementPopupEnsInexistant() throws MetierException {
        final RechercheEnseignementPopupQO rechercheEnseignementPopupQO =
            new RechercheEnseignementPopupQO();
        rechercheEnseignementPopupQO.setIdEnseignant(1335987654);
        rechercheEnseignementPopupQO.setIdEtablissement(98);
        final ResultatDTO<List<EnseignementDTO>> resultat =
            enseignementService.findEnseignementPopup(rechercheEnseignementPopupQO);

        final List<EnseignementDTO> enseignementsDTO = resultat.getValeurDTO();
        assertEquals(enseignementsDTO.size(), 0);
    }
    
    /**
     * Test findEnseignementPopup.
     *avec enseignant pas dans l'etablissement
     * @throws MetierException .
     */
    public void testfindEnseignementPopupEtabInexistant() throws MetierException {
        final RechercheEnseignementPopupQO rechercheEnseignementPopupQO =
            new RechercheEnseignementPopupQO();
        rechercheEnseignementPopupQO.setIdEnseignant(1335);
        rechercheEnseignementPopupQO.setIdEtablissement(987645321);
        final ResultatDTO<List<EnseignementDTO>> resultat =
            enseignementService.findEnseignementPopup(rechercheEnseignementPopupQO);

        final List<EnseignementDTO> enseignementsDTO = resultat.getValeurDTO();
        assertEquals(enseignementsDTO.size(), 0);
    }
    
    /**
     * Test sans erreur de findEnseignementPopup.
     *
     * @throws MetierException .
     */
    public void testfindEnseignementPopup() throws MetierException {
        final RechercheEnseignementPopupQO rechercheEnseignementPopupQO =
            new RechercheEnseignementPopupQO();
        rechercheEnseignementPopupQO.setIdEnseignant(1335);
        rechercheEnseignementPopupQO.setIdEtablissement(98);
        final ResultatDTO<List<EnseignementDTO>> resultat =
            enseignementService.findEnseignementPopup(rechercheEnseignementPopupQO);

        final List<EnseignementDTO> enseignementsDTO = resultat.getValeurDTO();
        assertEquals(enseignementsDTO.size(), 1);

        Boolean un = false;
        for (EnseignementDTO enseignementDTO : enseignementsDTO) {
            final int id = enseignementDTO.getId();
            final String code = enseignementDTO.getCode();
            final String intitule = enseignementDTO.getIntitule();
            switch (id) {
                case 7:
                    assertFalse(un);
                    un = true;
                    assertEquals(code, "L1000");
                    assertEquals(intitule, "HISTOIRE GEOGRAPHIE");
                    break;
                default:
                    throw new MetierException("testfindEnseignementPopup NOK : a renvoyer un enseignement n'appartenant pas à l'enseignant 1");
            }
        }
    }

    /**
     * Test de findEnseignementPopup s'il n'y a pas d'enseignement associé à l'enseignant.
     *
     * impossible --> un enseignant est obligatoirement rattaché à un enseignement.
     *
     * @throws MetierException .
     */
    /*public void testfindEnseignementPopupNoEnseignement()
                                                 throws MetierException {
        final RechercheEnseignementPopupQO rechercheEnseignementPopupQO =
            new RechercheEnseignementPopupQO();
        rechercheEnseignementPopupQO.setIdEnseignant(3);
        final ResultatDTO<List<EnseignementDTO>> resultat =
            enseignementService.findEnseignementPopup(rechercheEnseignementPopupQO);

        final List<EnseignementDTO> enseignementsDTO = resultat.getValeurDTO();
        assertEquals(enseignementsDTO.size(), 0);
    }*/
    
    /**
     * Test find.
     *avec null
     * @throws MetierException .
     */
    public void testfindNull() throws MetierException {
        try{
        enseignementService.find(null);
        }catch(AssertionException e){
            log.debug("testfindNull OK AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * Test sans erreur de find.
     *
     * @throws MetierException .
     */
    public void testfind() throws MetierException {
        final EnseignementDTO enseignementDTO = enseignementService.find(7);
        final String code = enseignementDTO.getCode();
        final int id = enseignementDTO.getId();
        final String designation = enseignementDTO.getIntitule();
        assertEquals(7, id);
        assertEquals(code, "L1000");
        assertEquals(designation, "HISTOIRE GEOGRAPHIE");
    }

    /**
     * Test de find si l'enseignement n'existe pas.
     *
     * @throws MetierException .
     */
    public void testfindNoEns() throws MetierException {
        final EnseignementDTO enseignementDTO = enseignementService.find(987654321);
        assertNull(enseignementDTO);
    }
    
    /**
     * test de findEnseignementEtablissement.
     * ok
     */
    public void testFindEnseignementEtablissement(){
        assertEquals(enseignementService.findEnseignementEtablissement(98).size(),16);
    }
    
    /**
     * test de findEnseignementEtablissement.
     * avec null
     */
    public void testFindEnseignementEtablissementNull(){
        try{
            assertEquals(enseignementService.findEnseignementEtablissement(null).size(),0);
            log.debug("testFindEnseignementEtablissementNull OK : Pas de probleme lié à la nullité.");
        }catch (AssertionException e) {
            log.debug("testFindEnseignementEtablissementNull OK : AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de findEnseignementEnseignant.
     * avec null
     */
    public void testFindEnseignementEnseignantNull(){
        try{
            enseignementService.findEnseignementEnseignant(null);
        }catch (AssertionException e) {
            log.debug("testFindEnseignementEnseignantNull OK : AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de findEnseignementEnseignant.
     * avec champ null
     */
    public void testFindEnseignementEnseignantChampsNull(){
        try{
            final RechercheEnseignementPopupQO rechercheEnseignementPopupQO = new RechercheEnseignementPopupQO();
            enseignementService.findEnseignementEnseignant(rechercheEnseignementPopupQO);
        }catch (AssertionException e) {
            log.debug("testFindEnseignementEnseignantChampsNull OK : AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de findEnseignementEnseignant.
     * avec idEnseignant
     */
    public void testFindEnseignementEnseignantIdEns(){
        try{
            final RechercheEnseignementPopupQO rechercheEnseignementPopupQO = new RechercheEnseignementPopupQO();
            rechercheEnseignementPopupQO.setIdEnseignant(1335);
            enseignementService.findEnseignementEnseignant(rechercheEnseignementPopupQO);
        }catch (AssertionException e) {
            log.debug("testFindEnseignementEnseignantChampsNull OK : AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de findEnseignementEnseignant.
     * avec idEnseignant et idEtab
     */
    public void testFindEnseignementEnseignantIdEtab(){
            final RechercheEnseignementPopupQO rechercheEnseignementPopupQO = new RechercheEnseignementPopupQO();
            rechercheEnseignementPopupQO.setIdEnseignant(1335);
            rechercheEnseignementPopupQO.setIdEtablissement(98);
            assertEquals(1, enseignementService.findEnseignementEnseignant(rechercheEnseignementPopupQO).size());
    }
    
    /**
     * test de findEnseignementEnseignant.
     * avec ens pas dans etab
     */
    public void testFindEnseignementEnseignantEnsNoEtab(){
            final RechercheEnseignementPopupQO rechercheEnseignementPopupQO = new RechercheEnseignementPopupQO();
            rechercheEnseignementPopupQO.setIdEnseignant(1335);
            rechercheEnseignementPopupQO.setIdEtablissement(99);
            assertEquals(0, enseignementService.findEnseignementEnseignant(rechercheEnseignementPopupQO).size());
    }
    
    /**
     * test de findEnseignementEnseignant.
     * avec ens inexistant
     */
    public void testFindEnseignementEnseignantEnsInexistant(){
            final RechercheEnseignementPopupQO rechercheEnseignementPopupQO = new RechercheEnseignementPopupQO();
            rechercheEnseignementPopupQO.setIdEnseignant(987654321);
            rechercheEnseignementPopupQO.setIdEtablissement(98);
            assertEquals(0, enseignementService.findEnseignementEnseignant(rechercheEnseignementPopupQO).size());
    }
    
    /**
     * test de findEnseignementEnseignant.
     * avec etab inexistant
     */
    public void testFindEnseignementEnseignantEtabInexistant(){
            final RechercheEnseignementPopupQO rechercheEnseignementPopupQO = new RechercheEnseignementPopupQO();
            rechercheEnseignementPopupQO.setIdEnseignant(1335);
            rechercheEnseignementPopupQO.setIdEtablissement(987654321);
            assertEquals(0, enseignementService.findEnseignementEnseignant(rechercheEnseignementPopupQO).size());
    }
    
    /**
     * test de saveEnseignementLibelle.
     * avec null
     * @throws MetierException bloquante
     */
    public void testSaveEnseignementLibelleNull() throws MetierException{
        try{
            enseignementService.saveEnseignementLibelle(null);
        }catch(AssertionException e){
            log.debug("testSaveEnseignementLibelle OK : AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de saveEnseignementLibelle.
     * avec Champs null
     * @throws MetierException bloquante
     */
    public void testSaveEnseignementLibelleChampsNull() throws MetierException{
        try{
            final EnseignementQO enseignementQO = new EnseignementQO();
            enseignementService.saveEnseignementLibelle(enseignementQO);
        }catch(AssertionException e){
            log.debug("testSaveEnseignementLibelle OK : AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de saveEnseignementLibelle.
     * avec idEtab
     * @throws MetierException bloquante
     */
    public void testSaveEnseignementLibelleIdEtab() throws MetierException{
        try{
            final EnseignementQO enseignementQO = new EnseignementQO();
            enseignementQO.setIdEtablissement(98);
            enseignementService.saveEnseignementLibelle(enseignementQO);
        }catch(AssertionException e){
            log.debug("testSaveEnseignementLibelleIdEtab OK : AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de saveEnseignementLibelle.
     * avec idEtab et idEnseignement
     * 
     * @throws MetierException bloquante
     */
    public void testSaveEnseignementLibelleIdEns() throws MetierException{
            final EnseignementQO enseignementQO = new EnseignementQO();
            enseignementQO.setIdEtablissement(98);
            enseignementQO.setIdEnseignement(7);
            enseignementQO.setLibelle("HG");
            enseignementService.saveEnseignementLibelle(enseignementQO);
    }
    
    /**
     * test de saveEnseignementLibelle.
     * avec idEtabInexistant et idEnseignement
     * 
     * Exception logique mais inattrapable :
     * 
     * contrainte sur idEtablissement ==> inexistant.
     * 
     * @throws MetierException bloquante
     */
    public void testSaveEnseignementLibelleEtabInexistant() throws MetierException{
            final EnseignementQO enseignementQO = new EnseignementQO();
            enseignementQO.setIdEtablissement(987654321);
            enseignementQO.setIdEnseignement(7);
            enseignementQO.setLibelle("HG");
                enseignementService.saveEnseignementLibelle(enseignementQO);
    }
    
    /**
     * test de saveEnseignementLibelle.
     * avec idEtab et idEnseignementInexistant
     * 
     * Exception logique mais inattrapable :
     * 
     * contrainte sur idEnseignement ==> inexistant.
     * 
     * @throws MetierException bloquante
     */
    public void testSaveEnseignementLibelleEnsInexistant() throws MetierException{
            final EnseignementQO enseignementQO = new EnseignementQO();
            enseignementQO.setIdEtablissement(98);
            enseignementQO.setIdEnseignement(987564321);
            enseignementQO.setLibelle("HG1");
                enseignementService.saveEnseignementLibelle(enseignementQO);
    }
    
    /**
     * test de saveEnseignementLibelle.
     * avec idEtab et idEnseignement non lié
     * 
     * @throws MetierException bloquante
     */
    public void testSaveEnseignementLibelleEnsNoEtab() throws MetierException{
            final EnseignementQO enseignementQO = new EnseignementQO();
            enseignementQO.setIdEtablissement(98);
            enseignementQO.setIdEnseignement(1);
            enseignementQO.setLibelle("HG123");
                enseignementService.saveEnseignementLibelle(enseignementQO);
    }
    
    /**
     * test de saveEnseignementLibelle.
     * libelle existant
     * 
     * @throws MetierException bloquante
     */
    public void testSaveEnseignementLibelleLabelExistant() throws MetierException{
            final EnseignementQO enseignementQO = new EnseignementQO();
            enseignementQO.setIdEtablissement(98);
            enseignementQO.setIdEnseignement(2);
            enseignementQO.setLibelle("HG123");
            try{
                enseignementService.saveEnseignementLibelle(enseignementQO);
            }catch(MetierException e){
                log.debug("testSaveEnseignementLibelleLabelExistant OK : detection doublon : {0}", e.getMessage());
            }
    }
    
    /**
     * test de saveEnseignementLibelle.
     * avec idEtab et idEnseignement non lié
     * libelle vide --> suppression de celui ci
     * 
     * @throws MetierException bloquante
     */
    public void testSaveEnseignementLibelleLabelVide() throws MetierException{
            final EnseignementQO enseignementQO = new EnseignementQO();
            enseignementQO.setIdEtablissement(98);
            enseignementQO.setIdEnseignement(1);
            enseignementQO.setLibelle("");
                enseignementService.saveEnseignementLibelle(enseignementQO);
    }
    
}
