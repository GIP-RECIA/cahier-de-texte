/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.business;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EtablissementAccessibleQO;
import org.crlr.dto.application.base.EtablissementComplementDTO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.GenericDetailDTO;
import org.crlr.dto.application.base.OuvertureQO;
import org.crlr.dto.application.base.SaisieSimplifieeDTO;
import org.crlr.dto.application.base.SaisieSimplifieeQO;

import org.crlr.exception.metier.MetierException;

import java.util.List;
import java.util.Map;

/**
 * EtablissementHibernateBusiness.
 *
 * @author vibertd
 * @version $Revision: 1.10 $
  */
public interface EtablissementHibernateBusinessService {
    /**
     * Recherche de l'identifiant de l'établissement en fonction de son numéro
     * SIREN.
     *
     * @param siren le numéro SIREN
     *
     * @return l'identifiant.
     */
    public Integer findIdEtablissementParCode(final String siren);

    /**
     * Recherche de la désignation de l'établissement en fonction de son numéro
     * SIREN.
     *
     * @param siren le numéro SIREN
     *
     * @return la désignation de l'établissement.
     */
    public String findDesignationEtablissementParCode(final String siren);

    /**
     * Recherche la liste des établissements en fonction du schéma de la base de
     * données.
     *
     * @param schema le schema (si null le schéma courant est alors sélectionné).
     *
     * @return la map dont la clef est le code de l'établissement, et la valeur est
     *         composé de la designation et uniquement pour le schéma courant de
     *         l'identifiant.
     */
    public Map<String, Object[]> executeQueryListeEtablissement(final String schema);

    /**
     * Recherche de l'établissement de rattachement de l'utilisateur
     * (identifiant, designation, jours ouvres).
     *
     * @param siren le numéro SIREN
     * @param archive le schema utilisé pour les archives, null si schema courant.
     * @param exercice DOCUMENT ME!
     *
     * @return le dto.
     */
    public GenericDetailDTO<Integer, String, String, Boolean> findIdDescJourOuvreEtablissementParCode(final String siren,
                                                                                                      final Boolean archive,
                                                                                                      final String exercice);

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
     * Sauvegarde de la saisie simplifiée pour un enseignant dans un
     * établissement.
     *
     * @param saisieSimplifieeQO les paramètres.
     */
    public void saveEtablissementSaisieSimplifiee(SaisieSimplifieeQO saisieSimplifieeQO);

    /**
     * Recherche la liste des établissements d'un inspecteur.
     *
     * @param listeSiren la liste des siren.
     *
     * @return la liste.
     */
    public List<EtablissementDTO> findListeEtablissementInspecteur(final List<String> listeSiren);

    /**
     * Recherche la liste des établissements d'un enseignant.
     *
     * @param etablissementAccessibleQO les paramètres.
     *
     * @return la liste.
     */
    public List<EtablissementDTO> findListeEtablissementEnseignant(final EtablissementAccessibleQO etablissementAccessibleQO);

    /**
     * Recherche de l'activation de la saisie simplifiée sur un établissement
     * pour un enseignant.
     *
     * @param idEtablissement l'id de l'établissement.
     * @param idEnseignant l'id de l'enseignant.
     *
     * @return l'activation.
     */
    public SaisieSimplifieeDTO findSaisieSimplifieeEtablissement(final Integer idEtablissement,
                                                     final Integer idEnseignant);

    /**
     * Contrôle de l'activation de la saisie simplifiée sur un établissement pour
     * un enseignant.
     *
     * @param idEtablissement l'id de l'établissement.
     * @param idEnseignant l'id de l'enseignant.
     *
     * @return l'activation (null == première connexion de l'utilisateur sur
     *         l'établissement).
     */
    public Boolean checkSaisieSimplifieeEtablissement(final Integer idEtablissement,
                                                      final Integer idEnseignant);

    /**
     * Recherche les horaires de cours pour un établissement.
     *
     * @param id identfiant de l'établissement.
     *
     * @return ResultatDTO contenant les horaires de cours et l'eventuel message.
     *
     * @throws MetierException l'exception potentielle.
     */
    public ResultatDTO<String> findHorairesCoursEtablissement(final Integer id)
        throws MetierException;
    
    /**
     * Permet de renvoyer un booléen qui indique si on a une configuration en horaire scindé.
     * @param idEtablissement : id de l'établissement.
     * @return vrai ou faux pour l'horaire scindé.
     */
    public Integer findFractionnementEtablissement(final Integer idEtablissement);
    
    /**
     * Récupère la durée normale des cours de l'établissement.
     * @param idEtablissement : id de l'établissement
     * @return la durée du cours (50, 55, 60)
     */
    public Integer findDureeCoursEtablissement(final Integer idEtablissement);
    
    /**
     * Retourne l'état de l'import indiqué en base de données.
     * @param idEtablissement : l'id de l'établissement.
     * @return le booléen qui indique si le traitement est en cours.
     */
    public Boolean findEtatImportEtablissement(final Integer idEtablissement);
    
    /**
     * Met à jour le champs import de la base de données pour l'établissement.
     * @param idEtablissement : id de l'établissement.
     * @param statut : true ou false pour dire si on démarre l'import ou s'il est fini.
     */
    public void modifieStatutImportEtablissement(Integer idEtablissement, Boolean statut);
    
    /**
     * Retourne une string contenant le getTime du début de l'import.
     * @param idEtablissement : id de l'établissement.
     * @return le string du getTime.
     */
    public String checkDateImportEtablissement(final Integer idEtablissement);
    
    /**
     * @param idEtablissement l'id
     * @return l'établissement
     */
    public EtablissementDTO findEtablissement(final Integer idEtablissement);
}
