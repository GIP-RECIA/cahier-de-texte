/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EtablissementFacadeService.java,v 1.5 2010/04/29 11:20:51 jerome.carriere Exp $
 */

package org.crlr.metier.facade;

import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EtablissementComplementDTO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.OuvertureQO;

import org.crlr.exception.metier.MetierException;

/**
 * EtablissementFacadeService.
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public interface EtablissementFacadeService {
    /**
     * Sauvegarde des jours ouvrés.
     *
     * @param etablissementQO les paramètres.
     *
     * @return message acquittement.
     *
     * @throws MetierException Exception.
     */
    public ResultatDTO<Integer> saveEtablissementJoursOuvres(final EtablissementDTO etablissementQO)
        throws MetierException;

    /**
     * Recherche des informations complémentaires d'un établissement (durée
     * cours;heure, minute début et fin;les horaires).
     *
     * @param id l'identifiant de l'établissement.
     *
     * @return le dto.
     */
    public EtablissementComplementDTO findDonneeComplementaireEtablissement(final Integer id);

    /**
     * Mise à jours des informations complémentaires d'un établissement (durée
     * cours;heure, minute début et fin;les horaires).
     *
     * @param etablissementQO les paramétres.
     *
     * @return le message d'acquittement.
     *
     * @throws MetierException Exception.
     */
    public ResultatDTO<Integer> saveComplementEtablissement(final EtablissementDTO etablissementQO)
        throws MetierException;

    /**
     * Recherche des alternances des semaines 1 et 2.
     *
     * @param idEtablissement l'identifiant.
     *
     * @return l'alternance des semaines.
     */
    public String findAlternanceSemaine(final Integer idEtablissement);

    /**
     * Mise à jour des alternances des semaines 1 et 2.
     *
     * @param etablissementQO les paramétres.
     *
     * @return le message d'acquittement.
     *
     * @throws MetierException Exception.
     */
    public ResultatDTO<Integer> saveEtablissementAlternance(final EtablissementDTO etablissementQO)
        throws MetierException;

    /**
     * Mise à jour de l'ouverture établissement.
     *
     * @param ouvertureQO les paramétres.
     *
     * @return le message d'acquittement.
     *
     * @throws MetierException Exception.
     */
    public ResultatDTO<Integer> saveEtablissementOuverture(final OuvertureQO ouvertureQO)
        throws MetierException;

    /**
     * Recherche les horaires de cours pour un établissement.
     *
     * @param id identfiant de l'établissement.
     *
     * @return ResultatDTO contenant les horaires de cours et l'éventuel message.
     *
     * @throws MetierException l'exception potentielle.
     */
    public ResultatDTO<String>  findHorairesCoursEtablissement(final Integer id)
      throws MetierException;
    
    /**
     * Recherche les jours ouvrés d'un établissement en fonction de son siren.
     * @param siren le siren de l'etab.    
     * @param archive recherche archivée.
     * @param exercice l'exercice scolaire.
     * @return la liste des jours ouvrés.
     */
    public String findJourOuvreEtablissementParCode(final String siren, final Boolean archive, final String exercice);
    
    /**
     * Recherche l'id d'un établissement en fonction de son siren (et l'annee scolaire consultée).
     * @param siren le siren de l'etab.    
     * @param archive recherche archivée.
     * @param exercice l'exercice scolaire.
     * @return l'id etablissement
     */
    public Integer findIdEtablissementParCode(final String siren, final Boolean archive, final String exercice);
    
    
    
    /**
     * @param id dbId
     * @return l'établissement
     */
    public ResultatDTO<EtablissementDTO> findEtablissement(final Integer id);
    
    /**
     * @param idEtablissement id
     * @param idEnseignant id
     * @return r
     */
    public ResultatDTO<Boolean> checkSaisieSimplifieeEtablissement(final Integer idEtablissement,
            final Integer idEnseignant);
    
    /**
     * Donne tous les enseignants d'un établissement.
     * @param idEtablissement
     * @return
     */
    public List<EnseignantDTO> findAllEnseignant(Integer idEtablissement);
    
}
