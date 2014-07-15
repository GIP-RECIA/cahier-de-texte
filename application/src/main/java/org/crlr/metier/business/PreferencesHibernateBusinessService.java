/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PreferencesHibernateBusinessService.java,v 1.1 2010/04/28 16:12:21 jerome.carriere Exp $
 */

package org.crlr.metier.business;

import java.util.Set;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.PreferencesQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.web.dto.TypePreferencesEtab;

/**
 * PreferenceHibernateBusinessService.
 *
 * @author carrierej
 * @version $Revision: 1.1 $
  */
public interface PreferencesHibernateBusinessService {
    /**
     * Retourne la chaine de préférence d'un utilisateur.
     *
     * @param uid l'uid de l'utilisateur
     *
     * @return string contenant les préférences
     *
     * @throws MetierException
     */
    public String findUtilisateurPreferences(final String uid);

    /**
     * Sauvegarde les préférences de l'utilisateur.
     *
     * @param preferencesQO les préférences
     *
     * @return le message d'acquittement
     *
     * @throws MetierException l'exception potentielle
     */
    public ResultatDTO<Integer> savePreferences(PreferencesQO preferencesQO)
                                         throws MetierException;

    
    /**
     * Cherche les preferences de niveau établissement.
     * @param idEtab
     * @return
     */
    Set<TypePreferencesEtab> findEtabPreferences(Integer idEtab);
    
    /**
     * Sauvegarde les préferences de niveau établissement.
     * @param idEtab
     * @param preferences
     */
	void saveEtabPreferences(Integer idEtab, Set<TypePreferencesEtab> preferences);

	
}
