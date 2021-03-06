/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PreferencesFacadeService.java,v 1
 */

package org.crlr.metier.facade;

import java.util.Set;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.PreferencesQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.web.dto.TypePreferencesEtab;


/**
 * Interface de la Façade Preferences.
 *
 * @author carrierej
 * @version $Revision: 1.1 $
  */
public interface PreferencesFacadeService {
    
    /**
     * Sauvegarde des préférences utilisateurs.
     *
     * @param preferencesQO preferencesQo les préférences
     * @return message acquittement.
     * @throws MetierException l'exception potentielle
     */
    public ResultatDTO<Integer> savePreferences(final PreferencesQO preferencesQO)
                         throws MetierException;
    
    /**
     * Retourne la chaine des préférences de l'utilisateur.
     * @param uid l'uid de l'utilisateur connecté
     * @return une chaine contenant les préférences
     */
    public String findUtilisateurPreferences(final String uid);

    /**
     * Recherche les préferences de l'établissement.
     * @param idEtab
     * @return
     */
	Set<TypePreferencesEtab> findEtabPreferences(Integer idEtab);

	/**
	 * Sauvegarde des préferences de l'établissement.
	 * @param idEtab
	 * @param preferences
	 */
	void saveEtabPreferences(Integer idEtab, Set<TypePreferencesEtab> preferences);
}
