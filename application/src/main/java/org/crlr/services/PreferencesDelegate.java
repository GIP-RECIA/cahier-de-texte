/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PreferencesDelegate.java,v 1
 */

package org.crlr.services;

import java.util.Set;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.PreferencesQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.PreferencesFacadeService;
import org.crlr.web.dto.TypePreferencesEtab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PreferencesDelegate.
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
@Service
public class PreferencesDelegate implements PreferencesService {
  
    /** Injection spring automatique de la preferencesFacade. */
    @Autowired
    private PreferencesFacadeService preferencesFacadeService;

    /**
     * Mutateur preferencesFacadeService.
     *
     * @param preferencesFacadeService preferencesFacadeService à modifier.
     */
    public void setPreferencesFacadeService(PreferencesFacadeService preferencesFacadeService) {
        this.preferencesFacadeService = preferencesFacadeService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> savePreferences(PreferencesQO preferencesQO)
         throws MetierException {
       return preferencesFacadeService.savePreferences(preferencesQO);
    }
    
    /**
     * {@inheritDoc}
     */
    public String findUtilisateurPreferences(final String uid){
        return preferencesFacadeService.findUtilisateurPreferences(uid);
    }

    @Override
	public Set<TypePreferencesEtab> findEtabPreferences(Integer idEtab) {
		return preferencesFacadeService.findEtabPreferences(idEtab);
	}

    @Override
    public void saveEtabPreferences(Integer idEtab, Set<TypePreferencesEtab> preferences) {
		preferencesFacadeService.saveEtabPreferences(idEtab, preferences);
	}
    
    

}
