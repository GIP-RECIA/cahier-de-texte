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
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.sequence.PrintSeanceOuSequenceQO;
import org.crlr.dto.application.sequence.PrintSequenceDTO;
import org.crlr.dto.application.sequence.RechercheSequencePopupQO;
import org.crlr.dto.application.sequence.RechercheSequenceQO;
import org.crlr.dto.application.sequence.ResultatRechercheSequenceDTO;
import org.crlr.dto.application.sequence.SaveSequenceQO;
import org.crlr.dto.application.sequence.SaveSequenceSimplifieeQO;
import org.crlr.dto.application.sequence.SequenceAffichageQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.SequenceFacadeService;
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
public class SequenceDelegate implements SequenceService {
    /** Injection spring automatique de la sequenceFacade. */
    @Autowired
    private SequenceFacadeService sequenceFacadeService;

    /**
     * Mutateur sequenceFacadeService.
     *
     * @param sequenceFacadeService sequenceFacadeService à modifier
     */
    public void setSequenceFacadeService(SequenceFacadeService sequenceFacadeService) {
        this.sequenceFacadeService = sequenceFacadeService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<SequenceDTO>> findSequencePopup(RechercheSequencePopupQO rechercheSequencePopupQO)
        throws MetierException {
        return sequenceFacadeService.findSequencePopup(rechercheSequencePopupQO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<ResultatRechercheSequenceDTO>> findSequence(RechercheSequenceQO rechercheSequenceQO)
        throws MetierException {
        return sequenceFacadeService.findSequence(rechercheSequenceQO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> deleteSequence(ResultatRechercheSequenceDTO resultatRechercheSequenceDTO)
                        throws MetierException {
        return sequenceFacadeService.deleteSequence(resultatRechercheSequenceDTO);
    }

    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Boolean> deleteSequencesVide(RechercheSequenceQO rechercheSequenceQO) throws MetierException {
        return sequenceFacadeService.deleteSequencesVide(rechercheSequenceQO);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveSequence(SaveSequenceQO saveSequenceQO)
                         throws MetierException {
        return sequenceFacadeService.saveSequence(saveSequenceQO);
    }

    /**
     * {@inheritDoc}
     */
    public SequenceDTO findSequenceAffichage(final SequenceAffichageQO sequenceAffichageQO)
            throws MetierException {
        return sequenceFacadeService.findSequenceAffichage(sequenceAffichageQO);
    }

    /**
     * {@inheritDoc}
     */
    public List<SeanceDTO> findSeanceBySequence(
            ResultatRechercheSequenceDTO resultatRechercheSequenceDTO)
            throws MetierException {
        return sequenceFacadeService.findSeanceBySequence(resultatRechercheSequenceDTO);
    }

    /**
     * {@inheritDoc}
     */
    public boolean checkUnicite(SaveSequenceQO saveSequenceQO)
            throws MetierException {
        return sequenceFacadeService.checkUnicite(saveSequenceQO);
    }

    /**
     * {@inheritDoc}
     */
    public Report printSequence(PrintSeanceOuSequenceQO printSequenceQO, List<PrintSequenceDTO> seancesDTO)
            throws MetierException {
        return sequenceFacadeService.printSequence(printSequenceQO, seancesDTO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveSequenceSaisieSimplifiee(
            SaveSequenceSimplifieeQO saveSequenceSimplifieeQO)
            throws MetierException {
        return sequenceFacadeService.saveSequenceSaisieSimplifiee(saveSequenceSimplifieeQO);
    }

    /**
     * {@inheritDoc}
     */
    public List<EnseignantDTO> findEnseignantsEleve(
            Integer idClasse, Set<Integer> idsGroupe, Integer idEnseignement) {
        return sequenceFacadeService.findEnseignantsEleve(idClasse, idsGroupe, idEnseignement);
    }

    /**
     * {@inheritDoc}
     */
    public List<EnseignementDTO> findEnseignementsEleve(RechercheEnseignementPopupQO rechercheEnseignementPopupQO) {
        return sequenceFacadeService.findEnseignementsEleve(rechercheEnseignementPopupQO);
    }


}
