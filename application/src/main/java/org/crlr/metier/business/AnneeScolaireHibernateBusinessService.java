/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AnneeScolaireHibernateBusinessService.java,v 1.9 2010/03/29 09:29:35 ent_breyton Exp $
 */

package org.crlr.metier.business;


import java.util.Date;
import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.DateAnneeScolaireQO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.OuvertureQO;
import org.crlr.dto.application.base.PeriodeVacanceQO;
import org.crlr.exception.metier.MetierException;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author vibertd
 * @version $Revision: 1.9 $
  */
public interface AnneeScolaireHibernateBusinessService {
    
    /**
     * Recherche l'année scolaire en fonction de la date du jour.
     * @return le DTO.
     */
    public AnneeScolaireDTO findAnneeScolaire();


    /**
     * Vérifie que la date de la séquence est comprise dans l'année scolaire courante.
     *
     * @param idAnneeScolaire Id de l'année scolaire
     * @param dateDebut Date de début de la séquence
     * @param dateFin Date de fin de la séquence
     *
     * @return True si les dates de la séquence sont comprises dans l'année scolaire, sinon false.
     */
    public boolean checkDateAnneeScolaire(Integer idAnneeScolaire, Date dateDebut,
                                          Date dateFin);

    /**
     * Retourne la liste des années scolaires.
     * @param vraiOuFauxMessageBloquant true pour gérer une liste vide.
     * @return la liste des années scolaires
     * @throws MetierException Exception
     */
    public ResultatDTO<List<AnneeScolaireDTO>> findListeAnneeScolaire(final Boolean vraiOuFauxMessageBloquant) throws MetierException;
    
    /**
     * Sauvegarde de l'année scolaire. (date de rentrée et de sortie)
     * @param dateAnneeScolaireQO les paramètres.  
     * @return message acquittement. 
     */
    public ResultatDTO<Integer> saveAnneeScolaire(final DateAnneeScolaireQO dateAnneeScolaireQO);
    
    /**
     * Sauvegarde des périodes de vacance. (y compris les jours fériés)
     * @param periodeVacanceQO les paramètres.
     * @return message acquittement.  
     */
    public ResultatDTO<Integer> savePeriodeVacance(final PeriodeVacanceQO periodeVacanceQO);
    
    /**
     * Sauvegarde de l'ouverture du cahier de texte.
     * @param ouvertureQO les paramètres.
     * @return message acquittement.  
     */
    public ResultatDTO<Integer> saveOuvertureENT(final OuvertureQO ouvertureQO);
    
    /**
     * @param groupeClasseDTO g
     * @return l'année scolaire
     * @throws MetierException ex
     */
    public ResultatDTO<AnneeScolaireDTO> findAnneeScolaire(final GroupesClassesDTO groupeClasseDTO)
            throws MetierException;
}
