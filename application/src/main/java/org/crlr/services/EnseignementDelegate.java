/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignementDelegate.java,v 1.6 2010/04/12 04:30:12 jerome.carriere Exp $
 */

package org.crlr.services;

import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.EnseignementQO;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.EnseignementFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * EnseignementDelegate.
 *
 * @author $author$
 * @version $Revision: 1.6 $
  */
@Service
public class EnseignementDelegate implements EnseignementService {
    /** Injection spring automatique de la enseignementFacade. */
    @Autowired
    private EnseignementFacadeService enseignementFacadeService;

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<EnseignementDTO>> findEnseignementPopup(RechercheEnseignementPopupQO rechercheEnseignementPopupQO)
        throws MetierException {
        return enseignementFacadeService.findEnseignementPopup(rechercheEnseignementPopupQO);
    }

    /**
     * Mutateur enseignementFacadeService.
     *
     * @param enseignementFacadeService Le enseignementFacadeService à modifier
     */
    public void setEnseignementFacadeService(EnseignementFacadeService enseignementFacadeService) {
        this.enseignementFacadeService = enseignementFacadeService;
    }

    /**
     * {@inheritDoc}
     */
    public EnseignementDTO find(Integer idEnseignement) throws MetierException {
        return this.enseignementFacadeService.find(idEnseignement);
    }

    /**
     * {@inheritDoc}
     */
    public List<EnseignementDTO> findEnseignementEnseignant(
            RechercheEnseignementPopupQO rechercheEnseignementPopupQO) {
        return this.enseignementFacadeService.findEnseignementEnseignant(rechercheEnseignementPopupQO);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public List<EnseignementDTO> findEnseignementEtablissement(
            Integer idEtablissement) {       
        return this.enseignementFacadeService.findEnseignementEtablissement(idEtablissement);
    }

    /**
     * {@inheritDoc}
     */
    public void saveEnseignementLibelle(EnseignementQO enseignementQO)
            throws MetierException {
        this.enseignementFacadeService.saveEnseignementLibelle(enseignementQO);
    }
}
