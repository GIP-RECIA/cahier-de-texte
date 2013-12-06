/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AnneeScolaireDelegate.java,v 1.4 2010/03/31 08:08:44 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.admin.DatePeriodeQO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.DateAnneeScolaireQO;
import org.crlr.dto.application.base.OuvertureQO;
import org.crlr.dto.application.base.PeriodeVacanceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.AnneeScolaireFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
@Service
public class AnneeScolaireDelegate implements AnneeScolaireService {
    /** Injection spring automatique de la seanceFacade. */
    @Autowired
    private AnneeScolaireFacadeService anneeScolaireFacadeService;

    /**
     * Mutateur anneeScolaireFacadeService.
     * @param anneeScolaireFacadeService Le anneeScolaireFacadeService à modifier
     */
    public void setAnneeScolaireFacadeService(
            AnneeScolaireFacadeService anneeScolaireFacadeService) {
        this.anneeScolaireFacadeService = anneeScolaireFacadeService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<AnneeScolaireDTO>> findListeAnneeScolaire()
            throws MetierException {
        return this.anneeScolaireFacadeService.findListeAnneeScolaire();
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveAnneeScolaire(final DateAnneeScolaireQO dateAnneeScolaireQO)
            throws MetierException {
         return this.anneeScolaireFacadeService.saveAnneeScolaire(dateAnneeScolaireQO);        
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> savePeriodeVacance(final PeriodeVacanceQO periodeVacanceQO)
            throws MetierException {
         return this.anneeScolaireFacadeService.savePeriodeVacance(periodeVacanceQO);        
    }
    
    /**
     * {@inheritDoc}
     */
    public void checkDatesPeriode(final DatePeriodeQO datePeriodeQO)
            throws MetierException {
        this.anneeScolaireFacadeService.checkDatesPeriode(datePeriodeQO);        
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveOuvertureENT(OuvertureQO ouvertureQO) throws MetierException {       
        return this.anneeScolaireFacadeService.saveOuvertureENT(ouvertureQO);
    }
}
