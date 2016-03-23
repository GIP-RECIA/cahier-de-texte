/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequenceDelegate.java,v 1.13 2010/04/12 01:34:33 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.List;
import java.util.Set;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.CouleurEnseignementClasseDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.sequence.PrintSeanceOuSequenceQO;
import org.crlr.dto.application.sequence.PrintSequenceDTO;
import org.crlr.dto.application.sequence.RechercheSequencePopupQO;
import org.crlr.dto.application.sequence.RechercheCouleurEnseignementClasseQO;
import org.crlr.dto.application.sequence.ResultatRechercheSequenceDTO;
import org.crlr.dto.application.sequence.SaveCouleurEnseignementClasseQO;
import org.crlr.dto.application.sequence.SaveSequenceSimplifieeQO;
import org.crlr.dto.application.sequence.SequenceAffichageQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.CouleurEnseignementClasseFacadeService;
import org.crlr.report.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SequenceDelegate.
 *
 * @author $author$
 * @version $Revision: 1.13 $
 */
@Service
public class CouleurEnseignementClasseDelegate implements CouleurEnseignementClasseService {
    /** Injection spring automatique de la couleurEnseignementClasseFacade. */
    @Autowired
    private CouleurEnseignementClasseFacadeService couleurEnseignementClasseFacadeService;

    /**
     * Mutateur couleurEnseignementClasseFacadeService.
     *
     * @param couleurEnseignementClasseFacadeService couleurEnseignementClasseFacadeService à modifier
     */
    public void setCouleurEnseignementClasseFacadeService(CouleurEnseignementClasseFacadeService couleurEnseignementClasseFacadeService) {
        this.couleurEnseignementClasseFacadeService = couleurEnseignementClasseFacadeService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<CouleurEnseignementClasseDTO> findCouleurEnseignementClasse(RechercheCouleurEnseignementClasseQO rechercheCouleurEnseignementClasseQO)
        throws MetierException {
        return couleurEnseignementClasseFacadeService.findCouleurEnseignementClasse(rechercheCouleurEnseignementClasseQO);
    }

    /**
     * {@inheritDoc}
     */
    public void saveCouleurEnseignementClasse(SaveCouleurEnseignementClasseQO saveCouleurEnseignementClasseQO)
                         throws MetierException {
        couleurEnseignementClasseFacadeService.saveCouleurEnseignementClasse(saveCouleurEnseignementClasseQO);
    }

}
