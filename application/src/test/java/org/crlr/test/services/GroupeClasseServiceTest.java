/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GroupeClasseServiceTest.java,v 1.6 2009/04/23 07:42:30 vibertd Exp $
 */

package org.crlr.test.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignantsClasseGroupeQO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.RechercheGroupeClassePopupQO;
import org.crlr.dto.application.base.RechercheGroupeQO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.exception.metier.MetierException;
import org.crlr.exception.metier.MetierRuntimeException;
import org.crlr.services.GroupeClasseService;
import org.crlr.test.AbstractMetierTest;
import org.crlr.utils.AssertionException;
import org.apache.commons.collections.CollectionUtils;

import static org.junit.Assert.*;
/**
 * Test Junit pour GroupeClasseService.
 *
 * @author $author$
 * @version $Revision: 1.6 $
 */
public class GroupeClasseServiceTest extends AbstractMetierTest {
    /** Le service à utiliser. */
    private GroupeClasseService groupeClasseService;

    /**
     * Mutateur de groupeClasseService.
     *
     * @param groupeClasseService le nouvel groupeClasseService.
     */
    public void setGroupeClasseService(GroupeClasseService groupeClasseService) {
        this.groupeClasseService = groupeClasseService;
    }

    /**
     * Test de findClasse avec null.
     *
     * @throws MetierException .
     */
    public void testfindClasseNull() throws MetierException {
        try{
            final GroupesClassesDTO groupeClasseDTO = groupeClasseService.findClasse(null);
            assertEquals(null, groupeClasseDTO);
        }catch (AssertionException e) {
            log.debug("testfindClasseNull OK : AssertionException : {0}", e.getMessage());
        }catch(NullPointerException e){
            log.debug("testfindClasseNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
            throw new MetierException("testfindClasseNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
        }
    }
    
    /**
     * Test sans erreur de findClasse.
     *
     * @throws MetierException .
     */
    public void testfindClasse() throws MetierException {
        final GroupesClassesDTO groupeClasseDTO = groupeClasseService.findClasse(2760);
        final int id = groupeClasseDTO.getId();
        final String intitule = groupeClasseDTO.getIntitule();
        final String code = groupeClasseDTO.getCode();
        assertEquals(id, 2760);
        assertEquals(intitule, "5G1");
        assertEquals(code, "CLA2760");
    }

    /**
     * Test de findClasse si la classe n'existe pas.
     *
     * @throws MetierException .
     */
    public void testfindClasseNotExist() throws MetierException {
        final GroupesClassesDTO groupeClasseDTO = groupeClasseService.findClasse(987654321);
        assertNull(groupeClasseDTO);
    }
    
    /**
     * Test de findGroupe avec null.
     *
     * @throws MetierException .
     * 
     */
    public void testfindGroupeNull() throws MetierException {
        try{
            final GroupesClassesDTO groupeClasseDTO = groupeClasseService.findGroupe(null);
            assertEquals(null, groupeClasseDTO);
        }catch (AssertionException e) {
            log.debug("testfindGroupeNull OK : AssertionException : {0}", e.getMessage());
        }catch(NullPointerException e){
            log.debug("testfindGroupeNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
            throw new MetierException("testfindGroupeNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
        }
    }
    
    /**
     * Test sans erreur de findGroupe.
     *
     * @throws MetierException .
     */
    public void testfindGroupe() throws MetierException {
        final GroupesClassesDTO groupeClasseDTO = groupeClasseService.findGroupe(1);
        final int id = groupeClasseDTO.getId();
        final String intitule = groupeClasseDTO.getIntitule();
        final String code = groupeClasseDTO.getCode();
        assertEquals(id, 1);
        assertEquals(intitule, "1 CAPMVA");
        assertEquals(code, "GRP1");
    }

    /**
     * Test de findGroupe si le groupe n'existe pas.
     *
     * @throws MetierException .
     */
    public void testfindGroupeNotExist() throws MetierException {
        final GroupesClassesDTO groupeClasseDTO = groupeClasseService.findGroupe(987654312);
        assertNull(groupeClasseDTO);
    }

    /**
     * Test sans erreur de findGroupeByClasse.
     *
     * @throws MetierException .
     */
    public void testfindGroupeByClasseNull() throws MetierException {
        try{
            //final List<GroupeDTO> resultat =
                groupeClasseService.findGroupeByClasse(null);
        }catch (AssertionException e) {
            log.debug("testfindGroupeByClasseNull OK : AssertionException : {0}", e.getMessage());
        }catch(NullPointerException e){
            log.debug("testfindGroupeByClasseNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
            throw new MetierException("testfindGroupeByClasseNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
        }
    }
    
    /**
     * Test sans erreur de findGroupeByClasse.
     *
     * @throws MetierException .
     */
    public void testfindGroupeByClasseChampsNull() throws MetierException {
        final RechercheGroupeQO rechercheGroupeQO = new RechercheGroupeQO();
        List<GroupeDTO> resultat = new ArrayList<GroupeDTO>();
        try{
                    resultat = groupeClasseService.findGroupeByClasse(rechercheGroupeQO);
        }catch (AssertionException e) {
            log.debug("testfindGroupeByClasseNull OK : AssertionException : {0}", e.getMessage());
        }catch(NullPointerException e){
            log.debug("testfindGroupeByClasseNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
            throw new MetierException("testfindGroupeByClasseNull NOK : NullPointerException : {0}",(Object)  e.getStackTrace());
        }catch(MetierException e){
            log.debug("testfindGroupeByClasseNull OK : MetierException : {0}", e.getMessage());
        }
        assertEquals(0, resultat.size());
    }
    
    /**
     * Test sans erreur de findGroupeByClasse.
     *
     * @throws MetierException .
     */
    public void testfindGroupeByClasse() throws MetierException {
        final RechercheGroupeQO rechercheGroupeQO = new RechercheGroupeQO();
        rechercheGroupeQO.setIdEnseignant(1335);
        rechercheGroupeQO.setCodeClasse("CLA2760");

        final List<GroupeDTO> resultat =
            groupeClasseService.findGroupeByClasse(rechercheGroupeQO);

        assertEquals(resultat.size(), 1);
        assertEquals(resultat.get(0).getId(), (Integer)13378);
        assertEquals(resultat.get(0).getCode(), "GRP13378");
        assertEquals(resultat.get(0).getIntitule(), "3132ESPE");
    }

    /**
     * Test de findGroupeByClasse si la classe n'a pas de groupe associé.
     *
     * @throws MetierException .
     */
    public void testfindGroupeByClasseSansGroupe()
                                          throws MetierException {
        final RechercheGroupeQO rechercheGroupeQO = new RechercheGroupeQO();
        rechercheGroupeQO.setIdEnseignant(1366);
        rechercheGroupeQO.setCodeClasse("CLA1");

        final List<GroupeDTO> resultat =
            groupeClasseService.findGroupeByClasse(rechercheGroupeQO);

        assertEquals(resultat.size(), 0);
    }

    /**
     * Test de findGroupeByClasse si la classe n'existe pas.
     *
     * @throws MetierException .
     */
    public void testfindGroupeByClasseClasseExistePas()
                                               throws MetierException {
        final RechercheGroupeQO rechercheGroupeQO = new RechercheGroupeQO();
        rechercheGroupeQO.setIdEnseignant(1335);
        rechercheGroupeQO.setCodeClasse("CLA987654321");

        try {
            groupeClasseService.findGroupeByClasse(rechercheGroupeQO);
        } catch (Exception e) {
            log.debug("testfindGroupeByClasseSansGroupe OK: la classe n'existe pas");
            return;
        }
        throw new MetierException("testAffichageElDevoirEleveExistePas NOK: la classe n'existe pas");
    }

    /**
     * Test de findGroupeByClasse si l'enseignant n'a pas les droits sur la classe.
     *
     * @throws MetierException .
     */
    public void testfindGroupeByClassePasDroitClasse()
                                              throws MetierException {
        final RechercheGroupeQO rechercheGroupeQO = new RechercheGroupeQO();
        rechercheGroupeQO.setIdEnseignant(3);
        rechercheGroupeQO.setCodeClasse("CLA2760");

        try {
            groupeClasseService.findGroupeByClasse(rechercheGroupeQO);
        } catch (Exception e) {
            log.debug("testfindGroupeByClassePasDroitClasse OK: l'enseignant n'a pas les droits sur la classe");
            return;
        }
        throw new MetierException("testfindGroupeByClassePasDroitClasse NOK: l'enseignant n'a pas les droits sur la classe");
    }
    
    /**
     * Test sans erreur de findGroupeClassePopup pour QO null.
     * 
     * @throws MetierException bloquante.
     * 
     * 
     */
    
    public void testFindGroupeClassePopupNull() throws MetierException{
        try{
            groupeClasseService.findGroupeClassePopup(null);
        }catch(AssertionException e){
            log.debug("testFindGroupeClassePopupNull OK : AssertionException : {0}", e.getMessage());
        }catch(NullPointerException e){
            log.debug("testFindGroupeClassePopupNull NOK : NullPointerException : {0}", e.getStackTrace()[0]);
            throw new MetierException("testFindGroupeClassePopupNull NOK : NullPointerException : {0}", e.getStackTrace()[0]);
        }
    }
    
    /**
     * Test sans erreur de findGroupeClassePopup pour les champs QO null.
     * 
     * @throws MetierException bloquante.
     * 
     * 
     */
    
    public void testFindGroupeClassePopupChampsNull() throws MetierException{
        try{
            final RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO =
                new RechercheGroupeClassePopupQO();
            final ResultatDTO<List<GroupesClassesDTO>> res = groupeClasseService.findGroupeClassePopup(rechercheGroupeClassePopupQO);
            assertEquals(null, res.getValeurDTO());
        }catch(AssertionException e){
            log.debug("testFindGroupeClassePopupChampsNull OK : AssertionException : {0}", e.getMessage());
        }catch(NullPointerException e){
            log.debug("testFindGroupeClassePopupChampsNull NOK : NullPointerException : {0}", e.getStackTrace()[0]);
            throw new MetierException("testFindGroupeClassePopupChampsNull NOK : NullPointerException : {0}", e.getStackTrace()[0]);
        }
    }
    
    /**
     * Test sans erreur de findGroupeClassePopup pour une classe.
     *
     * @throws MetierException .
     */
    public void testfindGroupeClassePopupClasse()
                                         throws MetierException {
        final RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO =
            new RechercheGroupeClassePopupQO();
        rechercheGroupeClassePopupQO.setIdEnseignant(1335);
        rechercheGroupeClassePopupQO.setTypeGroupeClasse(TypeGroupe.CLASSE);
        rechercheGroupeClassePopupQO.setIdAnneeScolaire(1);
        rechercheGroupeClassePopupQO.setIdEtablissement(98);
        

        final ResultatDTO<List<GroupesClassesDTO>> resultat =
            groupeClasseService.findGroupeClassePopup(rechercheGroupeClassePopupQO);
        final List<GroupesClassesDTO> groupesClassesDTOs = resultat.getValeurDTO();
        assertEquals(groupesClassesDTOs.size(), 3);

        
        final GroupesClassesDTO groupesClassesDTO = groupesClassesDTOs.get(0);
        final int id = groupesClassesDTO.getId();
        final String code = groupesClassesDTO.getCode();
        final String intitule = groupesClassesDTO.getIntitule();

        assertEquals(id, 2760);
        assertEquals(code, "CLA2760");
        assertEquals(intitule, "5G1");
    }

    /**
     * Test sans erreur de findGroupeClassePopup pour un groupe.
     *
     * @throws MetierException .
     */
    public void testfindGroupeClassePopupGroupe()
                                         throws MetierException {
        final RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO =
            new RechercheGroupeClassePopupQO();
        rechercheGroupeClassePopupQO.setIdEnseignant(4197);
        rechercheGroupeClassePopupQO.setTypeGroupeClasse(TypeGroupe.GROUPE);
        rechercheGroupeClassePopupQO.setIdAnneeScolaire(1);
        rechercheGroupeClassePopupQO.setIdEtablissement(63);

        final ResultatDTO<List<GroupesClassesDTO>> resultat =
            groupeClasseService.findGroupeClassePopup(rechercheGroupeClassePopupQO);
        final List<GroupesClassesDTO> groupesClassesDTOs = resultat.getValeurDTO();
        //log.debug("nb ret : {0}", groupesClassesDTOs.size());
        assertEquals(groupesClassesDTOs.size(), 8);

    }

    /**
     * Test de findGroupeClassePopup si l'enseignant n'existe pas.
     * 
     * @throws MetierException .
     */
    public void testfindGroupeClassePopupEnsExistePas()
                                               throws MetierException {
        final RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO =
            new RechercheGroupeClassePopupQO();
        rechercheGroupeClassePopupQO.setIdEnseignant(2000000);
        rechercheGroupeClassePopupQO.setTypeGroupeClasse(TypeGroupe.GROUPE);
        rechercheGroupeClassePopupQO.setIdAnneeScolaire(1);
        rechercheGroupeClassePopupQO.setIdEtablissement(1);

        final ResultatDTO<List<GroupesClassesDTO>> resultat =
            groupeClasseService.findGroupeClassePopup(rechercheGroupeClassePopupQO);
        final List<GroupesClassesDTO> groupesClassesDTOs = resultat.getValeurDTO();
        assertEquals(groupesClassesDTOs.size(), 0);
    }

    /**
     * Test de findGroupeClassePopup si le type n'est pas renseigné.
     *
     * @throws MetierException .
     */
    public void testfindGroupeClassePopupSansType()
                                           throws MetierException {
        final RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO =
            new RechercheGroupeClassePopupQO();

        final ResultatDTO<List<GroupesClassesDTO>> resultat =
            groupeClasseService.findGroupeClassePopup(rechercheGroupeClassePopupQO);
        final List<GroupesClassesDTO> groupesClassesDTOs = resultat.getValeurDTO();
        assertNull(groupesClassesDTOs);
    }

    /**
     * Test de findGroupeClassePopup si le groupe n'est pas renseigné.
     *
     * @throws MetierException .
     */
    public void testfindGroupeClassePopupNoGroupe()
                                           throws MetierException {
        final RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO =
            new RechercheGroupeClassePopupQO();
        rechercheGroupeClassePopupQO.setIdEnseignant(1335);
        rechercheGroupeClassePopupQO.setTypeGroupeClasse(TypeGroupe.GROUPE);
        
        rechercheGroupeClassePopupQO.setIdAnneeScolaire(1);
        rechercheGroupeClassePopupQO.setIdEtablissement(98);

        final ResultatDTO<List<GroupesClassesDTO>> resultat =
            groupeClasseService.findGroupeClassePopup(rechercheGroupeClassePopupQO);
        final List<GroupesClassesDTO> groupesClassesDTOs = resultat.getValeurDTO();
        assertEquals(groupesClassesDTOs.size(), 0);
    }
    
    /**
     * Test de findGroupeClassePopup si l'etablissement n'existe pas.
     *
     * @throws MetierException .
     */
    public void testfindGroupeClassePopupNoEtablissement()
                                           throws MetierException {
        final RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO =
            new RechercheGroupeClassePopupQO();
        rechercheGroupeClassePopupQO.setIdEnseignant(1335);
        rechercheGroupeClassePopupQO.setTypeGroupeClasse(TypeGroupe.GROUPE);
        
        rechercheGroupeClassePopupQO.setIdAnneeScolaire(1);
        rechercheGroupeClassePopupQO.setIdEtablissement(987654321);

        final ResultatDTO<List<GroupesClassesDTO>> resultat =
            groupeClasseService.findGroupeClassePopup(rechercheGroupeClassePopupQO);
        final List<GroupesClassesDTO> groupesClassesDTOs = resultat.getValeurDTO();
        assertEquals(groupesClassesDTOs.size(), 0);
    }
    
    /**
     * Test de findGroupeClassePopup si l'année scolaire est mauvaise n'existe pas.
     *
     * @throws MetierException .
     */
    public void testfindGroupeClassePopupNoAnneeScolaire()
                                           throws MetierException {
        final RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO =
            new RechercheGroupeClassePopupQO();
        rechercheGroupeClassePopupQO.setIdEnseignant(1335);
        rechercheGroupeClassePopupQO.setTypeGroupeClasse(TypeGroupe.GROUPE);
        
        rechercheGroupeClassePopupQO.setIdAnneeScolaire(5);
        rechercheGroupeClassePopupQO.setIdEtablissement(98);

        final ResultatDTO<List<GroupesClassesDTO>> resultat =
            groupeClasseService.findGroupeClassePopup(rechercheGroupeClassePopupQO);
        final List<GroupesClassesDTO> groupesClassesDTOs = resultat.getValeurDTO();
        assertEquals(groupesClassesDTOs.size(), 0);
    }
    
    /**
     * test de l'appel à la fonction findIdGroupesEleve.
     * avec null.
     */
    public void testFindIdGroupesEleveNull(){
        try{
            groupeClasseService.findIdGroupesEleve(null);
        }catch(AssertionException e){
            log.debug("testFindIdGroupesEleveNull OK : AssertionExcpetion : {0}", e.getMessage());
        }
    }
    
    /**
     * test de l'appel à la fonction findIdGroupesEleve.
     * avec eleve existant.
     */
    public void testFindIdGroupesEleveOk(){
            final Set<Integer> ret = groupeClasseService.findIdGroupesEleve(1);
            assertEquals(0, ret.size());
    }
    
    /**
     * test de l'appel à la fonction findIdGroupesEleve.
     * avec eleve inexistant.
     */
    public void testFindIdGroupesEleveInexistant(){
            final Set<Integer> ret = groupeClasseService.findIdGroupesEleve(987654321);
            assertEquals(0, ret.size());
    }
    
    /**
     * test de l'appel à la fonction findIdClasseEleve.
     * avec null.
     */
    public void testFindIdClasseEleveNull(){
        try{
            groupeClasseService.findIdClasseEleve(null);
        }catch(AssertionException e){
            log.debug("testFindIdGroupesEleveNull OK : AssertionExcpetion : {0}", e.getMessage());
        }
    }
    
    /**
     * test de l'appel à la fonction findIdClasseEleve.
     * avec eleve existant.
     */
    public void testFindIdClasseEleveOk(){
            final Integer ret = groupeClasseService.findIdClasseEleve(1);
            assertEquals((Integer)2251, ret);
    }
    
    /**
     * test de l'appel à la fonction findIdClasseEleve.
     * avec eleve inexistant.
     */
    public void testFindIdClasseEleveInexistant(){
        try{
            groupeClasseService.findIdClasseEleve(987654321);
        }catch(MetierRuntimeException e){
            log.debug("testFindIdClasseEleveInexistant ok : MetierRuntimeException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de l'appel à la fonction findEnseignantsClasse.
     * avec null
     * @throws MetierException bloquante.
     */
    public void testFindEnseignantsClasseNull() throws MetierException{
        try{
            groupeClasseService.findEnseignantsClasse(null);            
        }catch(AssertionException e){
            log.debug("testFindEnseignantsClasseNull OK : AssertionException : {0}", e.getMessage());
        }catch(NullPointerException e){
            log.debug("testFindEnseignantsClasseNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
            throw new MetierException("testFindEnseignantsClasseNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
        }
    }
    
    /**
     * test de l'appel à la fonction findEnseignantsClasse.
     * avec champs null
     * @throws MetierException bloquante.
     */
    public void testFindEnseignantsClasseChampsNull() throws MetierException{
        try{
            final EnseignantsClasseGroupeQO enseignantsClasseGroupeQO = new EnseignantsClasseGroupeQO();
            groupeClasseService.findEnseignantsClasse(enseignantsClasseGroupeQO);            
        }catch(AssertionException e){
            log.debug("testFindEnseignantsClasseChampsNull OK : AssertionException : {0}", e.getMessage());
        }catch(NullPointerException e){
            log.debug("testFindEnseignantsClasseChampsNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
            throw new MetierException("testFindEnseignantsClasseChampsNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
        }
    }
    
    
    
    
    
    /**
     * test de l'appel à la fonction findEnseignantsClasse.
     * avec null
     * @throws MetierException bloquante.
     */
    public void testFindEnseignantsGroupeNull() throws MetierException{
        try{
            groupeClasseService.findEnseignantsGroupe(null);            
        }catch(AssertionException e){
            log.debug("testFindEnseignantsGroupeNull OK : AssertionException : {0}", e.getMessage());
        }catch(NullPointerException e){
            log.debug("testFindEnseignantsGroupeNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
            throw new MetierException("testFindEnseignantsGroupeNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
        }
    }
    
    /**
     * test de l'appel à la fonction findEnseignantsClasse.
     * avec champs null
     * @throws MetierException bloquante.
     */
    public void testFindEnseignantsGroupeChampsNull() throws MetierException{
        try{
            final EnseignantsClasseGroupeQO enseignantsClasseGroupeQO = new EnseignantsClasseGroupeQO();
            groupeClasseService.findEnseignantsGroupe(enseignantsClasseGroupeQO);            
        }catch(AssertionException e){
            log.debug("testFindEnseignantsGroupeChampsNull OK : AssertionException : {0}", e.getMessage());
        }catch(NullPointerException e){
            log.debug("testFindEnseignantsGroupeChampsNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
            throw new MetierException("testFindEnseignantsGroupeChampsNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
        }
    }
    
   
    
   
    
    
    /**
     * test de l'appel à la fonction findClasseEleve.
     * avec null
     */
    public void testFindClasseEleveNull(){
        try{
            groupeClasseService.findClasseEleve(null);
        }catch(AssertionException e ){
            log.debug("testFindClasseEleveNull OK : AssertionExcpetion : {0}", e.getMessage());
        }
    }
    
    /**
     * test de l'appel à la fonction findClasseEleve.
     * avec eleve existant
     */
    public void testFindClasseEleveOk(){
            final GroupesClassesDTO gCDTO = groupeClasseService.findClasseEleve(24);
            assertEquals(gCDTO.getId(), (Integer)3623);
            assertEquals(gCDTO.getCode(), "CLA3623");
            assertEquals(gCDTO.getIntitule(), "1ES1");
    }
    
    /**
     * test de l'appel à la fonction findClasseEleve.
     * avec eleve inexistant
     */
    public void testFindClasseEleveInexistant(){
        try{
            assertNull(groupeClasseService.findClasseEleve(123456790));
        }catch(MetierRuntimeException e){
            log.debug("testFindClasseEleveInexistant OK : MetierRuntimeException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de l'appel à la fonction findGroupeEleve.
     * avec null
     */
    public void testFindGroupeEleveNull(){
        try{
            groupeClasseService.findGroupesEleve(null);
        }catch(AssertionException e ){
            log.debug("testFindGroupeEleveNull OK : AssertionExcpetion : {0}", e.getMessage());
        }
    }
    
    /**
     * Test de l'appel à la fonction findGroupeEleve avec eleve existant.
     * @throws MetierException .
     */
    public void testFindGroupeEleveOk() throws MetierException{
            final List<GroupeDTO> gDTOs = groupeClasseService.findGroupesEleve(24);
            assertEquals(2, gDTOs.size());
        for(GroupeDTO gDTO : gDTOs){
            switch(gDTO.getId()){
                case 17913 :
                    assertEquals("GRP17913",gDTO.getCode());
                    assertEquals("1ES1A",gDTO.getIntitule());
                    break;
                case 17914 :
                    assertEquals("GRP17914",gDTO.getCode());
                    assertEquals("1MATCES",gDTO.getIntitule());
                    break;
                default : 
                    throw new MetierException("testFindGroupeEleveOk NOK : a renvoyé un mauvais ID : {0}", gDTO.getId());
            }
        }
    }
    
    /**
     * test de l'appel à la fonction findGroupeEleve.
     * avec eleve inexistant
     * @throws MetierException bloquante.
     */
    public void testFindGroupeEleveInexistant() throws MetierException{
        try{
            final List<GroupeDTO> gDTOs = groupeClasseService.findGroupesEleve(123456790);
            if(CollectionUtils.isEmpty(gDTOs)){
                log.debug("testFindGroupeEleveInexistant OK : pas de resultat !");
            }else{
                throw new MetierException("testFindGroupeEleveOk NOK : " +
                        "a renvoyé des choses alors que l'élève n'existe pas - nb ret : {0}", gDTOs.size());
            }
        }catch(MetierRuntimeException e){
            log.debug("testFindGroupeEleveInexistant OK : MetierRuntimeException : {0}", e.getMessage());
        }
    }
    
}
