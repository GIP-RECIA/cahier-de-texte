/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SeanceDelegate.java,v 1.17 2009/07/30 15:09:26 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.remplacement.RechercheRemplacementQO;
import org.crlr.dto.application.remplacement.RemplacementDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.RemplacementFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SeanceDelegate.
 *
 * @author $author$
 * @version $Revision: 1.17 $
 */
@Service
public class RemplacementDelegate implements RemplacementService {

    /** Injection spring automatique de la seanceFacade. */
    @Autowired
    private RemplacementFacadeService remplacementFacadeService;

    /**
     * Mutateur de remplacementFacadeService {@link #remplacementFacadeService}.
     * @param remplacementFacadeService le remplacementFacadeService to set
     */
    public void setRemplacementFacadeService(RemplacementFacadeService remplacementFacadeService) {
        this.remplacementFacadeService = remplacementFacadeService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultatDTO<Boolean> deleteRemplacement(RemplacementDTO remplacementDTO) throws MetierException {
        return remplacementFacadeService.deleteRemplacement(remplacementDTO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveListeRemplacement(List<RemplacementDTO> listeRemplacementDTO) throws MetierException {
        return remplacementFacadeService.saveListeRemplacement(listeRemplacementDTO);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResultatDTO<List<RemplacementDTO>> findListeRemplacement(RechercheRemplacementQO rechercheRemplacementQO) {
        return remplacementFacadeService.findListeRemplacement(rechercheRemplacementQO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultatDTO<List<SeanceDTO>> findListeSeanceRemplacee(RechercheRemplacementQO rechercheRemplacementQO) {
        return remplacementFacadeService.findListeSeanceRemplacee(rechercheRemplacementQO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultatDTO<Integer> saveRemplacement(RemplacementDTO remplacementDTO) throws MetierException {
        return remplacementFacadeService.saveRemplacement(remplacementDTO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<EnseignantDTO>> findListeEnseignant(RechercheRemplacementQO rechercheRemplacementQO) {
        return remplacementFacadeService.findListeEnseignant(rechercheRemplacementQO);
    }
    
}
