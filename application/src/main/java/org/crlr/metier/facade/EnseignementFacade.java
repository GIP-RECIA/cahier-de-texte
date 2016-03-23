/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignementFacade.java,v 1.7 2010/04/12 04:30:13 jerome.carriere Exp $
 */

package org.crlr.metier.facade;

import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.EnseignementQO;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.business.EnseignementHibernateBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * EnseignementFacade.
 *
 * @author $author$
 * @version $Revision: 1.7 $
  */
@Component
@Transactional(
        readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
    )
public class EnseignementFacade implements EnseignementFacadeService {
    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private EnseignementHibernateBusinessService enseignementHibernateBusinessService;
    
    /**
     * Mutateur enseignementHibernateBusinessService.
     *
     * @param enseignementHibernateBusinessService enseignementHibernateBusinessService à modifier
     */
    public void setEnseignementHibernateBusinessService(
            EnseignementHibernateBusinessService enseignementHibernateBusinessService) {
        this.enseignementHibernateBusinessService = enseignementHibernateBusinessService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<EnseignementDTO>> findEnseignementPopup(RechercheEnseignementPopupQO rechercheEnseignementPopupQO)
        throws MetierException {
        return enseignementHibernateBusinessService.findEnseignementPopup(rechercheEnseignementPopupQO);
    }

    /**
     * {@inheritDoc}
     */
    public EnseignementDTO find(Integer idEnseignement) throws MetierException {
        return this.enseignementHibernateBusinessService.find(idEnseignement);
    }

    /**
     * {@inheritDoc}
     */
    public List<EnseignementDTO> findEnseignementEnseignant(
            RechercheEnseignementPopupQO rechercheEnseignementPopupQO) {
        return this.enseignementHibernateBusinessService.findEnseignementEnseignant(rechercheEnseignementPopupQO);
    }
   
    /**
     * {@inheritDoc}
     */
    public List<EnseignementDTO> findEnseignementEtablissement(
            Integer idEtablissement) {        
        return this.enseignementHibernateBusinessService.findEnseignementEtablissement(idEtablissement);
    }

    /**
     * {@inheritDoc}
     */
    public void saveEnseignementLibelle(EnseignementQO enseignementQO)
            throws MetierException {
        this.enseignementHibernateBusinessService.saveEnseignementLibelle(enseignementQO);
    }
}
