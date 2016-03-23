/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PreferencesServiceTest.java,v 1.0 2011/03/04 11:12:21 fabrice.moulin Exp $
 */
package org.crlr.test.services;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.PreferencesQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.PreferencesService;
import org.crlr.test.AbstractMetierTest;
import org.crlr.utils.AssertionException;
import org.apache.commons.lang.StringUtils;

import org.crlr.message.Message;

import static org.junit.Assert.*;
/**
 * @author root
 *
 */
public class PreferencesServiceTest extends AbstractMetierTest {

    PreferencesService preferencesService;

    /**
     * setter de preferencesService.
     * @param preferencesService the preferencesService to set
     */
    public void setPreferencesService(PreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }
    
    /**
     * test de l'appel à la fonction findUtilisateurPreferences().
     * avec null
     * @throws MetierException bloquante.
     */
    public void testFindUtilisateurPreferencesNull() throws MetierException{
        try{
            final String s = preferencesService.findUtilisateurPreferences(null);
            if(StringUtils.isEmpty(s)){
                log.debug("testFindUtilisateurPreferences ok : pas de retour");
            }else{
                log.debug("testFindUtilisateurPreferences NOK : retourne quelque chose alors que l'uid n'est pas setter :\n {0}", s);
                throw new MetierException("testFindUtilisateurPreferences NOK : retourne quelque chose alors que l'uid n'est pas setter :\n {0}", s);
            }
        }catch (AssertionException e) {
            log.debug("testFindUtilisateurPreferences ok : AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de l'appel à la fonction findUtilisateurPreferences().
     * uid existant.
     * @throws MetierException bloquante.
     */
    public void testFindUtilisateurPreferencesOk() throws MetierException{
        try{
            final String s = preferencesService.findUtilisateurPreferences("kac0013v");
            if(StringUtils.isEmpty(s)){
                log.debug("testFindUtilisateurPreferences NOK : pas de retour");
                throw new MetierException("testFindUtilisateurPreferences NOK : pas de retour");
            }else{
                log.debug("testFindUtilisateurPreferences OK : retourne :\n {0}", s);
                assertEquals("SEANCES", s);
            }
        }catch (AssertionException e) {
            log.debug("testFindUtilisateurPreferences ok : AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de l'appel à la fonction findUtilisateurPreferences().
     * uid existant sans preferences.
     * @throws MetierException bloquante.
     */
    public void testFindUtilisateurPreferencesPersSansPref() throws MetierException{
        try{
            final String s = preferencesService.findUtilisateurPreferences("kav000ar");
            if(StringUtils.isEmpty(s)){
                log.debug("testFindUtilisateurPreferencesPersSansPref OK : chaine vide");                
            }else{
                log.debug("testFindUtilisateurPreferencesPersSansPref NOK : retourne :\n {0}", s);
                throw new MetierException("testFindUtilisateurPreferencesPersSansPref NOK : retourne :\n {0}", s);
            }
        }catch (AssertionException e) {
            log.debug("testFindUtilisateurPreferencesPersSansPref ok : AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de l'appel à la fonction findUtilisateurPreferences().
     * uid inexistant.
     * @throws MetierException bloquante.
     */
    public void testFindUtilisateurPreferencesPersInexistante() throws MetierException{
        try{
            final String s = preferencesService.findUtilisateurPreferences("Zav000ar");
            if(StringUtils.isEmpty(s)){
                log.debug("testFindUtilisateurPreferencesPersSansPref OK : chaine vide");                
            }else{
                log.debug("testFindUtilisateurPreferencesPersSansPref NOK : retourne :\n {0}", s);
                throw new MetierException("testFindUtilisateurPreferencesPersSansPref NOK : retourne :\n {0}", s);
            }
        }catch (AssertionException e) {
            log.debug("testFindUtilisateurPreferencesPersSansPref ok : AssertionException : {0}", e.getMessage());
        }
    }
    
    /**
     * test de l'appel à la fonction savePreferences().
     * avec parm null
     * @throws MetierException bloquante.
     */
    public void testSavePreferencesNull() throws MetierException{
        try{
            preferencesService.savePreferences(null);
        }catch(AssertionException e){
            log.debug("testSavePreferencesNull OK : AssertionException : {0}", e.getMessage());
        }catch(NullPointerException e){
            log.debug("testSavePreferencesNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
            throw new MetierException("testSavePreferencesNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
        }
    }
    
    /**
     * test de l'appel à la fonction savePreferences().
     * avec champs null
     * @throws MetierException bloquante.
     * 
     * erreur ecriture de Null en base. 
     */
    public void testSavePreferencesChampsNull() throws MetierException{
        try{
            final PreferencesQO preferencesQO = new PreferencesQO();
            preferencesService.savePreferences(preferencesQO);
        }catch(AssertionException e){
            log.debug("testSavePreferencesChampsNull OK : AssertionException : {0}", e.getMessage());
        }catch(NullPointerException e){
            log.debug("testSavePreferencesChampsNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
            throw new MetierException("testSavePreferencesChampsNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
        }
    }
    
    /**
     * test de l'appel à la fonction savePreferences().
     * avec tout ok
     * @throws MetierException bloquante.
     * 
     */
    public void testSavePreferencesOk() throws MetierException{
            final PreferencesQO preferencesQO = new PreferencesQO();
            preferencesQO.setUid("kav000ar");
            preferencesQO.setPreferences("SEANCES");
            final ResultatDTO<Integer> res = preferencesService.savePreferences(preferencesQO);
            for(Message message : res.getConteneurMessage().getMessages()){
                log.debug("Message : {0}", message.getParametres());
            }     
    }
    
    /**
     * test de l'appel à la fonction savePreferences().
     * avec préférences bidon
     * @throws MetierException bloquante.
     * 
     */
    public void testSavePreferencesPrefBidon() throws MetierException{
            final PreferencesQO preferencesQO = new PreferencesQO();
            preferencesQO.setUid("kav000ar");
            preferencesQO.setPreferences("moi je ne veux pas de préférence, j'aime tout le monde !");
            preferencesService.savePreferences(preferencesQO);
            log.debug("testSavePreferencesPrefBidon NOK : \n{0} : \n{1}",
                    "enregistre en base des préférences des plus douteuse !", preferencesQO.getPreferences());
            throw new MetierException("testSavePreferencesPrefBidon NOK : \n{0} : \n{1}",
                    "enregistre en base des préférences des plus douteuse !", preferencesQO.getPreferences());
    }
    
    /**
     * test de l'appel à la fonction savePreferences().
     * avec préférences null
     * @throws MetierException bloquante.
     * 
     */
    public void testSavePreferencesPrefNull() throws MetierException{
            final PreferencesQO preferencesQO = new PreferencesQO();
            preferencesQO.setUid("kav000ar");
            preferencesQO.setPreferences(null);
            preferencesService.savePreferences(preferencesQO);
    }
    
    /**
     * test de l'appel à la fonction savePreferences().
     * avec préférences null
     * @throws MetierException bloquante.
     * 
     * erreur car l'uid n'existe pas ! et essai d'écrire cet uid en base !
     */
    public void testSavePreferencesUIdBidon() throws MetierException{
            final PreferencesQO preferencesQO = new PreferencesQO();
            preferencesQO.setUid("qav000ar");
            preferencesQO.setPreferences(null);
            preferencesService.savePreferences(preferencesQO);
    }
}
