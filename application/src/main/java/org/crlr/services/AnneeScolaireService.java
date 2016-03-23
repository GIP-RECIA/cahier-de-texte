/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AnneeScolaireService.java,v 1.4 2010/03/31 08:08:44 ent_breyton Exp $
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

/**
 * AnneeScolaireService.
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public interface AnneeScolaireService {
    /**
     * Retourne la liste des années scolaires.
     * @return la liste des années scolaires
     * @throws MetierException Exception
     */
    public ResultatDTO<List<AnneeScolaireDTO>> findListeAnneeScolaire()
        throws MetierException;
    
    /**
     * Sauvegarde de l'année scolaire. (date de rentrée et de sortie)
     * @param dateAnneeScolaireQO les paramètres.  
     * @return message acquittement.
     * @throws MetierException Exception.
     */
    public ResultatDTO<Integer> saveAnneeScolaire(final DateAnneeScolaireQO dateAnneeScolaireQO) 
        throws MetierException;
    
    /**
     * Contrôle de la plage à ajouter aux jours chomés.
     * @param datePeriodeQO la nouvelle plage et les plages existantes.
     * @throws MetierException la règle.
     */
    public void checkDatesPeriode(final DatePeriodeQO datePeriodeQO) 
        throws MetierException;
    
    /**
     * Sauvegarde des périodes de vacance. (y compris les jours fériés)
     * @param periodeVacanceQO les paramètres.
     * @return message acquittement.
     * @throws MetierException Exception.
     */
    public ResultatDTO<Integer> savePeriodeVacance(final PeriodeVacanceQO periodeVacanceQO) 
        throws MetierException;
    
    /**
     * Sauvegarde de l'ouverture du cahier de texte.
     * @param ouvertureQO les paramètres.
     * @return message acquittement. 
     * @throws MetierException Exception. 
     */
    public ResultatDTO<Integer> saveOuvertureENT(final OuvertureQO ouvertureQO)
        throws MetierException;
}
