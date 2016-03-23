/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConfidentialiteService.java,v 1.8 2010/05/20 08:24:50 ent_breyton Exp $
 */

package org.crlr.test.services;


import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.UserDTO;
import org.crlr.dto.application.base.CheckSaisieSimplifieeQO;
import org.crlr.dto.application.base.ElevesParentDTO;
import org.crlr.dto.application.base.EtablissementAccessibleQO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.securite.AuthentificationQO;
import org.crlr.dto.securite.TypeAuthentification;
import org.crlr.exception.metier.MetierException;
import org.crlr.exception.metier.MetierRuntimeException;
import org.crlr.services.ConfidentialiteService;
import org.crlr.test.AbstractMetierTest;
import org.crlr.utils.AssertionException;
import org.apache.commons.lang.StringUtils;

/**
 * @author root
 *
 */
public class ConfidentialiteServiceTest extends AbstractMetierTest {

    ConfidentialiteService confidentialiteService;
    
    /**
     * @param confidentialiteService the confidentialiteService to set
     */
    public void setConfidentialiteService(
            ConfidentialiteService confidentialiteService) {
        this.confidentialiteService = confidentialiteService;
    }

    /**
     * test de l'appel à la fonction. 
     * 
     * initialisationAuthentification(final AuthentificationQO authentificationQO) throws MetierException
     * 
     * avec parametre null
     * 
     * @throws MetierException l'exception bloquante.
     */
    public void testInitialisationAuthentificationNull() throws MetierException{
        try{
            confidentialiteService.initialisationAuthentification(null);
        }catch (AssertionException e) {
            log.debug("testInitialisationAuthentificationNull OK - AssertionException");
        }catch (NullPointerException e){
            log.debug("testInitialisationAuthentificationNull NOK - pas AssertionException");
            throw new MetierException("testInitialisationAuthentificationNull NOK - pas AssertionException");
        }
        log.debug("testInitialisationAuthentificationNull NOK - pas AssertionException & NullPointerException");
        throw new MetierException("testInitialisationAuthentificationNull NOK - pas AssertionException & NullPointerException");
    }
    
    /**
     * test de l'appel à la fonction. 
     * 
     * initialisationAuthentification(final AuthentificationQO authentificationQO) throws MetierException
     * 
     * TypeAuthentification = null
     * 
     * @throws MetierException l'exception bloquante.
     */
    public void testInitialisationAuthentificationTypeAuthNull() throws MetierException{
        try{
            final AuthentificationQO authentificationQO = new AuthentificationQO();
            authentificationQO.setTypeAuthentification(null);
            authentificationQO.setIdentifiant("kaa000wa");
            confidentialiteService.initialisationAuthentification(authentificationQO);            
        }catch (AssertionException e) {
            log.debug("testInitialisationAuthentificationNull OK - AssertionException");
            return;
        }
        log.debug("testInitialisationAuthentificationNull NOK - pas AssertionException & NullPointerException");
        throw new MetierException("testInitialisationAuthentificationNull NOK - pas AssertionException & NullPointerException");
    }
    /**
     * test de l'appel à la fonction. 
     * 
     * initialisationAuthentification(final AuthentificationQO authentificationQO) throws MetierException
     * TypeAuthentification.CAS
     * avec id null;
     * 
     * @throws MetierException l'exception bloquante.
     */
    public void testInitialisationAuthentificationIdNull() throws MetierException{
        try{
            final AuthentificationQO authentificationQO = new AuthentificationQO();
            authentificationQO.setTypeAuthentification(TypeAuthentification.CAS);
            authentificationQO.setIdentifiant(null);
            confidentialiteService.initialisationAuthentification(authentificationQO);
        }catch (MetierRuntimeException e) {
            log.debug("testInitialisationAuthentification OK", e.getMessage());
            return;
        }
        log.debug("testInitialisationAuthentification NOK - pas AssertionException & NullPointerException");
        throw new MetierException("testInitialisationAuthentification NOK - pas AssertionException & NullPointerException");
    }
    
    /**
     * test l'appel à la fonction findEleveDuParent.
     * 
     * avec null
     * @throws MetierException exception bloquante.
     */
    public void testFindEleveDuParentNull() throws MetierException{
        try{
            confidentialiteService.findEleveDuParent(null);
        }catch(AssertionException e){
            log.debug("testFindEleveDuParentNull OK - AssertionException");
            return;
        }
        log.debug("testFindEleveDuParentNull NOK - AssertionException");
        throw new MetierException("testFindEleveDuParentNull NOK - AssertionException");
    }
    
    /**
     * test l'appel à la fonction findEleveDuParent.
     * 
     * avec tout ok
     * 
     * jamais l'uid n'est mis dans UserDTO
     * 
     * @throws MetierException exception bloquante.
     */
    public void testFindEleveDuParentOk() throws MetierException{
        final String uid = "kaa000wa";
        final Set<String> listUid = new HashSet<String>();
        listUid.add(uid);
        final ResultatDTO<ElevesParentDTO> res = confidentialiteService.findEleveDuParent(listUid);
        final ElevesParentDTO ele = res.getValeurDTO();
        assertEquals(1, ele.getListeEnfant().size());
        for(UserDTO user : ele.getListeEnfant()){
            assertEquals("AUDOUIN", user.getNom());
            assertEquals("Alexandre", user.getPrenom());
            assertEquals( uid, user.getUid());
            assertEquals( (Integer)53, user.getIdentifiant());
        }
    }
    
    /**
     * test l'appel à la fonction findEleveDuParent.
     * 
     * liste d'uid avec un uid = ""
     * 
     * NullPointerException --> uid n'existe pas 
     * 
     * public EleveBean find(String uid)
     * 
     * retourne null et aucune verif avant utilisation. 
     * 
     */
    public void testFindEleveDuParentUidVide(){
        final String uid = "";
        final Set<String> listUid = new HashSet<String>();
        listUid.add(uid);
        final ResultatDTO<ElevesParentDTO> res = confidentialiteService.findEleveDuParent(listUid);
        final ElevesParentDTO ele = res.getValeurDTO();
        assertEquals(0, ele.getListeEnfant().size());
    }
    
    /**
     * test l'appel à la fonction findEleveDuParent.
     * 
     * liste d'uid avec un uid = ""
     * 
     *   meme chose qu'en dessus
     * 
     */
    public void testFindEleveDuParentDeuxUidEtUnVide(){
        final String uid1 = "kaa000wa";
        final String uid2 = "";
        final String uid3 = "kaa000wb";
        final Set<String> listUid = new HashSet<String>();
        listUid.add(uid1);
        listUid.add(uid2);
        listUid.add(uid3);
        final ResultatDTO<ElevesParentDTO> res = confidentialiteService.findEleveDuParent(listUid);
        final ElevesParentDTO ele = res.getValeurDTO();
        assertEquals(2, ele.getListeEnfant().size());
    }
    
    /**
     * test de l'appel à la fonction findListeEtablissement.
     * @throws MetierException .
     */
    public void testFindListeEtablissement() throws MetierException{
        final ResultatDTO<List<EtablissementDTO>> res = confidentialiteService.findListeEtablissement();
        assertEquals(146, res.getValeurDTO().size());
    }
    
    /**
     * test de l'appel à la fonction findListeEtablissementEnseignant(EtablissementAccessibleQO).
     * 
     * avec parametre null.
     */
    public void testFindListeEtablissementEnseignantNull(){
        try{
            confidentialiteService.findListeEtablissementEnseignant(null);
        }catch (AssertionException e) {
            log.debug("testFindListeEtablissementEnseignantNull ok");
            return;
        }
    }
    
    /**
     * test de l'appel à la fonction findListeEtablissementEnseignant(EtablissementAccessibleQO).
     * champ vide.
     * 
     * pas d'assertion sur la liste des sirens
     * @throws MetierException bloquant.
     */
    public void testFindListeEtablissementEnseignantChampsVide() throws MetierException{
        
            final EtablissementAccessibleQO etab = new EtablissementAccessibleQO();
            try{
                confidentialiteService.findListeEtablissementEnseignant(etab);
            }catch (Exception e) {
                log.debug("testFindListeEtablissementEnseignantChampsVide NOK : {0}", e.getMessage());
                throw new MetierException("testFindListeEtablissementEnseignantChampsVide NOK : {0}", e.getMessage());
            }
    }
    
    /**
     * test de l'appel à la fonction findListeEtablissementEnseignant(EtablissementAccessibleQO).
     * 
     * champ ok.
     * 
     * @throws MetierException bloquant.
     */
    public void testFindListeEtablissementEnseignantOk() throws MetierException{
            final EtablissementAccessibleQO etab = new EtablissementAccessibleQO();
            etab.setIdEnseignant(1335);
            final List<String> sirens = new ArrayList<String>();
            sirens.add("19340083500016");
            etab.setListeSiren(sirens);
            final ResultatDTO<List<EtablissementDTO>> res = 
                confidentialiteService.findListeEtablissementEnseignant(etab);
            assertEquals(1, res.getValeurDTO().size());
            assertEquals((Integer)98, res.getValeurDTO().get(0).getId());
            assertEquals("19340083500016", res.getValeurDTO().get(0).getCode());
            assertEquals("CLG-ALAIN SAVARY-ac-MONTPEL.", res.getValeurDTO().get(0).getDesignation());
    }
    
    /**
     * test de l'appel à la fonction findListeEtablissementEnseignant(EtablissementAccessibleQO).
     * 
     * Enseignant n'appartenant pas à l'etablissement.
     * 
     * @throws MetierException bloquant.
     */
    public void testFindListeEtablissementEnseignantEnsNonEtab() throws MetierException{
            final EtablissementAccessibleQO etab = new EtablissementAccessibleQO();
            etab.setIdEnseignant(1335);
            final List<String> sirens = new ArrayList<String>();
            sirens.add("19300949500014");
            etab.setListeSiren(sirens);
            final ResultatDTO<List<EtablissementDTO>> res = 
                confidentialiteService.findListeEtablissementEnseignant(etab);
            assertEquals(0, res.getValeurDTO().size());
    }
    
    /**
     * test de l'appel à la fonction findListeEtablissementEnseignant(EtablissementAccessibleQO).
     * 
     * enseignant inexistant.
     * 
     * @throws MetierException bloquant.
     */
    public void testFindListeEtablissementEnseignantEnsInexistant() throws MetierException{
            final EtablissementAccessibleQO etab = new EtablissementAccessibleQO();
            etab.setIdEnseignant(987654321);
            final List<String> sirens = new ArrayList<String>();
            sirens.add("19300949500014");
            etab.setListeSiren(sirens);
            final ResultatDTO<List<EtablissementDTO>> res = 
                confidentialiteService.findListeEtablissementEnseignant(etab);
            assertEquals(0, res.getValeurDTO().size());
    }
    
    /**
     * test de l'appel à la fonction findListeEtablissementEnseignant(EtablissementAccessibleQO).
     * 
     * etablissement inexstant.
     * 
     * @throws MetierException bloquant.
     */
    public void testFindListeEtablissementEnseignantSirenInexistant() throws MetierException{
            final EtablissementAccessibleQO etab = new EtablissementAccessibleQO();
            etab.setIdEnseignant(1335);
            final List<String> sirens = new ArrayList<String>();
            sirens.add("193009495000149");
            etab.setListeSiren(sirens);
            final ResultatDTO<List<EtablissementDTO>> res = 
                confidentialiteService.findListeEtablissementEnseignant(etab);
            assertEquals(0, res.getValeurDTO().size());
    }
    
    /**
     * test de l'appel à la fonction findUtilisateurPreferences(String uid).
     * avec null
     * @throws MetierException bloquante.
     */
    public void testFindUtilisateurPreferencesNull() throws MetierException{
        try {
            final Boolean ok =StringUtils.isEmpty(confidentialiteService.findUtilisateurPreferences(null));
            if(!ok){
                throw new MetierException("testFindUtilisateurPreferencesNull NOK : \n{0}", 
                        "renvoie des chose alors que le param est NULL");
            }else{
                log.debug("testFindUtilisateurPreferencesNull OK Pas de retour");
            }
        } catch (AssertionException e) {
            log.debug("testFindUtilisateurPreferencesNull OK AssertionException generee");
            return;
        }
    }
    
    /**
     * test de l'appel à la fonction findUtilisateurPreferences(String uid).
     * avec uid inextant.
     * @throws MetierException bloquante
     */
    public void testFindUtilisateurPreferencesUidInexistant() throws MetierException{
        
            final Boolean ok =StringUtils.isEmpty(confidentialiteService.findUtilisateurPreferences("kaa100wa"));
            if(!ok){
                throw new MetierException("testFindUtilisateurPreferencesUidInexistant NOK : \n{0}", 
                        "renvoie des chose alors que l'uid n'existe pas");
            }else{
                log.debug("testFindUtilisateurPreferencesUidInexistant OK Pas de retour");
            }
    }
    
    /**
     * test de l'appel à la fonction findUtilisateurPreferences(String uid).
     * avec uid exitant.
     * @throws MetierException bloquante
     */
    public void testFindUtilisateurPreferencesOk() throws MetierException{
        
            final String pref =  confidentialiteService.findUtilisateurPreferences("kac0013v");
            final Boolean ok = StringUtils.isEmpty(pref);
            if(!ok){
                assertEquals("SEANCES", pref);
            }else{
                log.debug("testFindUtilisateurPreferencesOk NOK : \n{0}", 
                "renvoie rien alors que l'uid existe");
                throw new MetierException("testFindUtilisateurPreferencesOk NOK : {0}", 
                "renvoie rien alors que l'uid existe");
            }
    }
    
    /**
     * test de l'appel à la fonction findUtilisateurPreferences(String uid).
     * avec uid existant mais pas dans la table des préférences.
     * @throws MetierException bloquante
     */
    public void testFindUtilisateurPreferencesUidPasPref() throws MetierException{
        
            final String pref =  confidentialiteService.findUtilisateurPreferences("kaa000wa");
            final Boolean ok = StringUtils.isEmpty(pref);
            if(!ok){
                log.debug("testFindUtilisateurPreferencesUidPasPref NOK : \n{0}", 
                "renvoie rien alors que l'uid n'a pas de preferences");
                throw new MetierException("testFindUtilisateurPreferencesUidPasPref NOK : {0}", 
                "renvoie rien alors que l'uid n'a pas de preferences");
            }else{
                log.debug("testFindUtilisateurPreferencesUidPasPref Ok");
            }
    }
    
    /**
     * test de l'appel à la fonction findListeEtablissementEnseignantAdmRessources(EtablissementAccessibleQO)
     * 
     * avec parametre null.
     */
    public void testFindListeEtablissementEnseignantAdmRessourcesNull(){
        try {
            confidentialiteService.findListeEtablissementEnseignantAdmRessources(null);
        } catch (AssertionException e) {
            log.debug("testFindListeEtablissementEnseignantAdmRessourcesNull OK - AssertionException OK : {0}", e.getMessage());
        }
    }
    
    /**
     * test de l'appel à la fonction findListeEtablissementEnseignantAdmRessources(EtablissementAccessibleQO)
     * 
     * avec parametre conforme.
     * 
     */
    public void testFindListeEtablissementEnseignantAdmRessourcesOk(){
        
            final EtablissementAccessibleQO etab = new EtablissementAccessibleQO();
            etab.setIdEnseignant(1335);
            final List<String> sirens = new ArrayList<String>();
            sirens.add("19340083500016");
            etab.setListeSiren(sirens);
            final ResultatDTO<List<EtablissementDTO>> res = confidentialiteService.findListeEtablissementEnseignantAdmRessources(etab);
            assertEquals(146, res.getValeurDTO().size());
        
    }
    
    /**
     * test de l'appel à la fonction findListeEtablissementEnseignantAdmRessources(EtablissementAccessibleQO)
     * 
     * avec Ens inexistant.
     * 
     */
    public void testFindListeEtablissementEnseignantAdmRessourcesEnsInexistant(){
        
            final EtablissementAccessibleQO etab = new EtablissementAccessibleQO();
            etab.setIdEnseignant(987654321);
            final List<String> sirens = new ArrayList<String>();
            sirens.add("19340083500016");
            etab.setListeSiren(sirens);
            final ResultatDTO<List<EtablissementDTO>> res = confidentialiteService.findListeEtablissementEnseignantAdmRessources(etab);
            log.debug("nb ret {0}", res.getValeurDTO().size());
            assertEquals(146, res.getValeurDTO().size());
    }
    
    /**
     * test de l'appel à la fonction findListeEtablissementEnseignantAdmRessources(EtablissementAccessibleQO)
     * 
     * avec Etab inexistant.
     * 
     */
    public void testFindListeEtablissementEnseignantAdmRessourcesEtabInexistant(){
            final EtablissementAccessibleQO etab = new EtablissementAccessibleQO();
            etab.setIdEnseignant(1335);
            final List<String> sirens = new ArrayList<String>();
            sirens.add("19340083500016 123");
            etab.setListeSiren(sirens);
            final ResultatDTO<List<EtablissementDTO>> res = confidentialiteService.findListeEtablissementEnseignantAdmRessources(etab);
            log.debug("nb ret {0}", res.getValeurDTO().size());
            assertEquals(146, res.getValeurDTO().size());
    }
    
    /**
     * test de l'appel à la fonction saveCheckAutiomatisationActivationSaisieSimplifiee(CheckSaisieSimplifieeQO).
     * avec param null;
     * @throws MetierException bloquante.
     */
    public void testSaveCheckAutiomatisationActivationSaisieSimplifieeNull() throws MetierException{
        try{
            confidentialiteService.saveCheckAutiomatisationActivationSaisieSimplifiee(null);
        }catch(AssertionException e){
            log.debug("testSaveCheckAutiomatisationActivationSaisieSimplifieeNull OK");
        }
    }
    
    /**
     * test de l'appel à la fonction saveCheckAutiomatisationActivationSaisieSimplifiee(CheckSaisieSimplifieeQO).
     * avec champs null;
     * @throws MetierException bloquante.
     */
    public void testSaveCheckAutiomatisationActivationSaisieSimplifieeChampsNull() throws MetierException{
        try{
            final CheckSaisieSimplifieeQO checkSaisieSimplifieeQO = new CheckSaisieSimplifieeQO(null, null, null, null,null);
            confidentialiteService.saveCheckAutiomatisationActivationSaisieSimplifiee(checkSaisieSimplifieeQO);
        }catch(AssertionException e){
            log.debug("testSaveCheckAutiomatisationActivationSaisieSimplifieeChampsNull OK");
        }
    }
    
    
}
