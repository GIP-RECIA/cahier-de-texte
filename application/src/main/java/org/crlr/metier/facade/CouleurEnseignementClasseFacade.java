/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: CouleurEnseignementClasseFacade.java,v 1.33 2010/05/20 08:24:50 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.CouleurEnseignementClasseDTO;
import org.crlr.dto.application.sequence.RechercheCouleurEnseignementClasseQO;
import org.crlr.dto.application.sequence.SaveCouleurEnseignementClasseQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.metier.business.CouleurEnseignementClasseHibernateBusinessService;
import org.crlr.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Façade concernant les fonctionnalités du module séquence.
 *
 * @author breytond
 * @version $Revision: 1.33 $
 */
@Component
@Transactional(
        readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
    )
public class CouleurEnseignementClasseFacade implements CouleurEnseignementClasseFacadeService {
    /** service métier association enseignant / etablissement / enseignement / grioupe (ou) classe / couleur. */
    private CouleurEnseignementClasseHibernateBusinessService couleurEnseignementClasseHibernateBusinessService;
    
    protected final Log log = LogFactory.getLog(getClass());
    
    /**
     * Mutateur couleurEnseignementClasseHibernateBusinessService.
     *
     * @param couleurEnseignementClasseHibernateBusinessService Le couleurEnseignementClasseHibernateBusinessService à
     *        modifier
     */
    @Autowired
    public void setCouleurEnseignementClasseHibernateBusinessService(CouleurEnseignementClasseHibernateBusinessService couleurEnseignementClasseHibernateBusinessService) {
        this.couleurEnseignementClasseHibernateBusinessService = couleurEnseignementClasseHibernateBusinessService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<CouleurEnseignementClasseDTO> findCouleurEnseignementClasse(RechercheCouleurEnseignementClasseQO rechercheCouleurEnseignementClasseQO)
        throws MetierException {
        Assert.isNotNull("rechercheCouleurEnseignementClasseQO", rechercheCouleurEnseignementClasseQO);
        return couleurEnseignementClasseHibernateBusinessService.findCouleurEnseignementClasse(rechercheCouleurEnseignementClasseQO);
    }

    /**
     * {@inheritDoc}
     */
    public void saveCouleurEnseignementClasse(SaveCouleurEnseignementClasseQO saveCouleurEnseignementClasseQO)
                         throws MetierException {
        Assert.isNotNull("saveCouleurEnseignementClasseQO", saveCouleurEnseignementClasseQO);
        couleurEnseignementClasseHibernateBusinessService.save(saveCouleurEnseignementClasseQO);
    }

}
