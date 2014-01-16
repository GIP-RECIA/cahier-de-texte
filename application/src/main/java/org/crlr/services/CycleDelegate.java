/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SeanceDelegate.java,v 1.17 2009/07/30 15:09:26 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.cycle.CycleDTO;
import org.crlr.dto.application.cycle.CycleSeanceDTO;
import org.crlr.dto.application.cycle.CycleSeanceFinalDTO;
import org.crlr.dto.application.cycle.RechercheCycleEmploiQO;
import org.crlr.dto.application.cycle.RechercheCycleQO;
import org.crlr.dto.application.cycle.RechercheCycleSeanceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.CycleFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SeanceDelegate.
 *
 * @author $author$
 * @version $Revision: 1.17 $
 */
@Service
public class CycleDelegate implements CycleService {

    /** Injection spring automatique de la seanceFacade. */
    @Autowired
    private CycleFacadeService cycleFacadeService;

    /**
     * Mutateur cycleFacadeService.
     *
     * @param cycleFacadeService cycleFacadeService à modifier
     */
    public void setCycleFacadeService(CycleFacadeService cycleFacadeService) {
        this.cycleFacadeService = cycleFacadeService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveCycle(CycleDTO cycle)
            throws MetierException { 
        return cycleFacadeService.saveCycle(cycle);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveCycleSeance(CycleSeanceDTO cycleSeanceDTO) throws MetierException {
        return cycleFacadeService.saveCycleSeance(cycleSeanceDTO);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Boolean> saveCycleOrdreSeance(CycleDTO cycleDTO) throws MetierException {
        return cycleFacadeService.saveCycleOrdreSeance(cycleDTO);

    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Boolean> deleteCycleSeance(CycleSeanceDTO cycleSeanceDTO) throws MetierException {
        return cycleFacadeService.deleteCycleSeance(cycleSeanceDTO);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Boolean> deleteCycle(CycleDTO cycleDTO) throws MetierException {
        return cycleFacadeService.deleteCycle(cycleDTO);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<CycleDTO>> findListeCycle(RechercheCycleQO rechercheCycleQO) {
        return cycleFacadeService.findListeCycle(rechercheCycleQO);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<CycleSeanceDTO> findListeCycleSeance(RechercheCycleSeanceQO rechercheCycleSeanceQO) {
        return cycleFacadeService.findListeCycleSeance(rechercheCycleSeanceQO);
    }

    /**
     * {@inheritDoc}
     */
    public void completeInfoSeance(CycleSeanceDTO cycleSeanceCycleDTO) {
        cycleFacadeService.completeInfoSeance(cycleSeanceCycleDTO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<CycleSeanceFinalDTO>> findListeEmploiDTO(RechercheCycleEmploiQO rechercheCycleEmploiQO) {
        return cycleFacadeService.findListeEmploiDTO(rechercheCycleEmploiQO);
    }
    

}
