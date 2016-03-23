/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.DateDTO;
import org.crlr.dto.application.base.PeriodeEmploiDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.dto.application.emploi.RechercheEmploiQO;
import org.crlr.dto.application.importEDT.PeriodeEdtQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.importEDT.DTO.EmploiDTO;
import org.crlr.web.dto.TypeSemaine;

/**
 * EleveHibernateBusinessService.
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public interface EmploiHibernateBusinessService {
    /**
     * Enregistrement de l'emploi du temps.
     *
     * @param listeDetailJourEmploiDTO la liste à ajouter.
     * 
     * @param rechercheDateModificationQO les paramètres pour la date de dernière modification.
     *
     * @return ResultatDTO
     *
     * @throws MetierException l'exception potentielle.
     */
    public ResultatDTO<Date> saveEmploiDuTemps(final List<DetailJourEmploiDTO> listeDetailJourEmploiDTO)
                                           throws MetierException;
    
    public void deleteEmploi(final List<Integer> listeIdsEmplois);

    /**
     * Retourne les enregistrement pour la constitution de l'emploi du temps.
     *
     * @param idEnseignant l'id de l'enseignant
     * @param idEtablissement l'id de l'établissement
     * @param typeSemaine le type de semaine
     *
     * @return une liste de DetailJoursEmploiDTO contenant les info nécessaire à la
     *         constitution de l'emploi du temps
     */
    public ResultatDTO<List<DetailJourEmploiDTO>> findEmploi(final Integer idEnseignant,
                                                             final Integer idEtablissement,
                                                             final TypeSemaine typeSemaine,
                                                             final Integer idPeriode);

    /**
     * Supprime les enregistrements d'un emploi du temps pour une semaine (1 ou 2),
     * un enseignant et un établissement.
     * @param idPeriode l'id de la periode
     * @param idEnseignant l'id de l'enseignant
     * @param idEtablissement l'id de l'établissement
     * @param typeSemaine le type de semaine
     */
    public void deleteEmploiSemaine(final Integer idPeriode,
                                    final Integer idEnseignant,
                                    final Integer idEtablissement,
                                    final TypeSemaine typeSemaine);

    /**
     * Retourne les enregistrement pour la consolidation de l'emploi du temps.
     *
     * @param rechercheEmploiQO les critères de recherche
     *
     * @return une liste de DetailJourEmploiDTO contenant les info nécessaire à la
     *         constitution de l'emploi du temps
     */
    public ResultatDTO<List<DetailJourEmploiDTO>> findEmploiConsolidation(final RechercheEmploiQO rechercheEmploiQO);

    /**
     * Mise à jour des identifiants de séquences dans l'emploi du temps.
     * Note : les séquences ne sont plus utilisées dans l'emploi du temps.
     *
     * @param idSequence l'id de la séquence.
     * @param idEtablissement l'id de l'établissement.
     * @param idEnseignant l'id de l'enseignant.
     * @param idEnseignement l'id de l'enseignement.
     * @param idGroupe l'id du groupe.
     * @param idClasse l'id de la classe.
     */
    @Deprecated
    public void saveSequenceEmploi(final Integer idSequence,
                                   final Integer idEtablissement,
                                   final Integer idEnseignant,
                                   final Integer idEnseignement, final Integer idGroupe,
                                   final Integer idClasse);

    /**
     * Recherche des plages d'emplois du temps en fonction d'identifiant.
     * @param listeIdEmploi la liste des identifiants.
     * @return la map (clef id valeur ids classe groupe et enseignements).
     */
    public Map<Integer,DetailJourEmploiDTO> findPlageEmploiTemps(final List<Integer> listeIdEmploi);
    
    /**
     * Permet d'insérer le contenu de la liste des cases d'emploi du temps sans fusion. (Pour import EDT).
     * @param mesEmplois : la liste des cases d'emploi du temps.
     * @param dateDebut date de debut de periode.
     */
    public void saveCasesEmploiSansFusion(List<EmploiDTO> mesEmplois, Date dateDebut);
    
    /**
     * Vide la table emploi pour les enregistrements de l'établissement passé en paramètre.
     * @param periodeEdtQO : 
     *     idEtablissement : l'id de l'établissement à vider.
     *     dateDebut : date de debut de la periode.
     */
    public void deleteEmploiDuTempsEtablissement(PeriodeEdtQO periodeEdtQO);
    
    /**
     * Compte le nombre de cases d'emploi du temps présentes en bdd pour cet établissement.
     * @param periodeEdtQO :
     *    idEtablissement : id de l'établissement.
     *    dateDebut de la periode
     * @return le nombre d'enregistrements trouvés.
     */
    public Integer checkNombreCaseEmploiPourEtablissement(PeriodeEdtQO periodeEdtQO);
    
    public List<PeriodeEmploiDTO> findPeriodes(Integer enseignantId,
			Integer etablissementId);
    
    public Boolean existePeriode(PeriodeEmploiDTO periode);
    
    public void creerPeriode(PeriodeEmploiDTO periode, Integer initPeriodeId) throws MetierException;
    
    public void deletePeriode(PeriodeEmploiDTO periode);
    
    /**
     * Recherche les prochaines dates d'emploi du temps correspondant aux memes : 
     * - classe (ou groupe)
     * - enseignant
     * - enseignement
     * à partir d'une date de début (non inclue) dans la limite de 10. 
     * 
     * @param rechercheEmploi : les criteres 
     * @return une liste de Date
     */
    public List<DateDTO> findProchaineDate(RechercheEmploiQO rechercheEmploi);
    
    /**
     * Recupere l'aleternance de semaine pour l'etablissement.
     * @param idEtablissement etablissement
     * @return une chaine.
     */
    public String findAlternanceSemaine(final Integer idEtablissement);
    
}
