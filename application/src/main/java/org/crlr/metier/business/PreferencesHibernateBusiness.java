/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PreferencesHibernateBusiness.java,v 1.2 2010/04/29 11:20:51 jerome.carriere Exp $
 */

package org.crlr.metier.business;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.PreferencesQO;
import org.crlr.dto.application.base.TypeReglesAcquittement;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.entity.PreferencesBean;
import org.crlr.metier.entity.PreferencesEtabBean;
import org.crlr.web.dto.TypePreferencesEtab;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * PreferenceHibernateBusiness.
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
@Repository
public class PreferencesHibernateBusiness extends AbstractBusiness
    implements PreferencesHibernateBusinessService {
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public String findUtilisateurPreferences(final String uid) {
        final String strOfPreferences;

        final String query =
            "SELECT p.preferences FROM " + PreferencesBean.class.getName() +
            " p WHERE p.uid = :uid";

        final List<String> liste =
            getEntityManager().createQuery(query).setParameter("uid", uid).getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            strOfPreferences = liste.get(0);
        } else {
            strOfPreferences = null;
        }

        return strOfPreferences;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> savePreferences(final PreferencesQO preferencesQO)
                                         throws MetierException {
        final String updateQuery =
            "UPDATE " + PreferencesBean.class.getName() +
            " p SET p.preferences=:preferences WHERE p.uid=:uid";

        final Integer resultatRequete =
            getEntityManager().createQuery(updateQuery)
                .setParameter("uid", preferencesQO.getUid())
                .setParameter("preferences", preferencesQO.getPreferences())
                .executeUpdate();

        if (resultatRequete == 0) {
            final PreferencesBean preferencesBean = new PreferencesBean();
            preferencesBean.setPreferences(preferencesQO.getPreferences());
            preferencesBean.setUid(preferencesQO.getUid());
			getEntityManager().persist(preferencesBean);
            getEntityManager().flush();
        }

        final ResultatDTO<Integer> result = new ResultatDTO<Integer>();
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                                         Nature.INFORMATIF, "La modification ",
                                         " sauvegardée"));

        result.setConteneurMessage(conteneurMessage);
        return result;
    }
    
    private PreferencesEtabBean getEtabPreferences(Integer idEtab){
    	PreferencesEtabBean bean = null;
    	if (idEtab != null) {
    		try {
        		bean = getEntityManager().find(PreferencesEtabBean.class, idEtab);    			
    		} catch (Exception e) {
    			log.debug("exception {}" , e.getClass());
    		}
    	}
    	return bean;
    }
    
    @Override
    public Set<TypePreferencesEtab> findEtabPreferences(Integer idEtab){
    	EnumSet<TypePreferencesEtab> res = EnumSet.noneOf(TypePreferencesEtab.class);
    	
    	PreferencesEtabBean bean = getEtabPreferences(idEtab);
    	
    	if (bean != null) {
    		String prefText = bean.getPreferences();
    		String [] preferences = prefText.split("\\s+");
    		for (String pref : preferences) {
				if (!StringUtils.isBlank(pref)) {
					log.debug("Add preference :{}" , pref);
					res.add(TypePreferencesEtab.valueOf(pref));
				}
			}
    	}
    	return res;
    }
   
    @Override
    public void saveEtabPreferences(Integer idEtab , Set<TypePreferencesEtab> preferences) {
    	
    	if (idEtab != null && preferences != null) {
	    	PreferencesEtabBean bean = getEtabPreferences(idEtab);
	    	
	    	String pref = StringUtils.join(preferences, " ");
	    	log.debug("sauvegarde des preferences : {}", pref);
	    	if (bean == null) {
	    		log.debug("creation des préferences pour l'etablissement ");
	    		bean = new PreferencesEtabBean();
	    		bean.setIdEtab(idEtab);
	    		bean.setPreferences(pref);
	    		
	    		getEntityManager().persist(bean);
	    	} else {
	    		
	    		bean.setPreferences(pref);
	    	}
	    	getEntityManager().flush();
    	}
    }
    
}
