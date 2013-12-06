/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PreferencesFacade.java,v 1
 */

package org.crlr.metier.facade;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.PreferencesQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.business.PreferencesHibernateBusinessService;
import org.crlr.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Façade concernant les fonctionnalités du module devoir.
 *
 * @author breytond
 * @version $Revision: 1.1 $
 */
@Component
@Transactional(
        readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
    )
public class PreferencesFacade implements PreferencesFacadeService {
  
    @Autowired
    private PreferencesHibernateBusinessService preferencesHibernateBusinessService;
 
    /**
     * Mutateur de preferencesHibernateBusinessService.
     * @param preferencesHibernateBusinessService le preferencesHibernateBusinessService à modifier.
     */
    public void setPreferencesHibernateBusinessService(
            PreferencesHibernateBusinessService preferencesHibernateBusinessService) {
        this.preferencesHibernateBusinessService = preferencesHibernateBusinessService;
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> savePreferences(final PreferencesQO preferencesQO)
                                           throws MetierException {
        Assert.isNotNull("preferencesQO", preferencesQO);
        return preferencesHibernateBusinessService.savePreferences(preferencesQO);
    }
    
    /**
     * {@inheritDoc}
     */
    public String findUtilisateurPreferences(final String uid){
        Assert.isNotNull("uid", uid);
        return preferencesHibernateBusinessService.findUtilisateurPreferences(uid);
    }
    
}
