/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequenceFacadeService.java,v 1.11 2010/04/12 01:34:33 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.List;
import java.util.Set;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
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
import org.crlr.message.ConteneurMessage;
import org.crlr.metier.entity.SequenceBean;
import org.crlr.report.Report;

/**
 * Interface de la Façade Sequence.
 *
 * @author breytond
 * @version $Revision: 1.11 $
  */
public interface SequenceFacadeService {
    /**
     * Méthode de recherche des séquences pour la popup.
     *
     * @param rechercheSequencePopupQO Les paramètres de recherche
     *
     * @return La liste des séquences
     *
     * @throws MetierException DOCUMENTATION INCOMPLETE!
     */
    public ResultatDTO<List<SequenceDTO>> findSequencePopup(RechercheSequencePopupQO rechercheSequencePopupQO)
        throws MetierException;

    /**
     * Méthode de recherche des séquences pour la liste de résultat.
     *
     * @param rechercheSequenceQO Les critères de recherche
     *
     * @return La liste des séquences
     *
     * @throws MetierException DOCUMENTATION INCOMPLETE!
     */
    public ResultatDTO<List<ResultatRechercheSequenceDTO>> findSequence(RechercheSequenceQO rechercheSequenceQO)
        throws MetierException;

    /**
     * Méthode qui recherche une séquence par id.
     *
     * @param idSequence l'id de la séquence à rechercher
     *
     * @return Le sequenceBean correspondant
     *
     * @throws MetierException DOCUMENTATION INCOMPLETE!
     */
    public SequenceBean find(Integer idSequence) throws MetierException;

    /**
     * Méthode de suppression d'une séquence.
     *
     * @param resultatRechercheSequenceDTOe Contient l'id de la séquence à supprimer.
     *
     * @return Un resultatDTO contenant le conteneur de message.
     *
     * @throws MetierException DOCUMENTATION INCOMPLETE!
     */
    public ResultatDTO<Integer> deleteSequence(ResultatRechercheSequenceDTO resultatRechercheSequenceDTOe)
                                        throws MetierException;

    
    /**
     * Supprime toutes les sequences sans seances de l'enseignant au sein de l etablissement.
     * @param rechercheSequenceQO doit avoir idEtablissement et idEnseignant.
     * @return true si ok
     * @throws MetierException exception.
     */
    public ResultatDTO<Boolean> deleteSequencesVide(RechercheSequenceQO rechercheSequenceQO) throws MetierException;
    
    /**
     * Méthode d'ajout/sauvegarde d'une séquence.
     *
     * @param saveSequenceQO Contient les infos de la séquence
     *
     * @return L'id de la séance créée/modifiée
     *
     * @throws MetierException Exception
     */
    public ResultatDTO<Integer> saveSequence(SaveSequenceQO saveSequenceQO)
                         throws MetierException;

    /**
     * Retourne toutes les infos de la séquence/groupe/classe/enseignement
     * nécessaire à l'affichage.
     *
     * @param sequenceAffichageQO sequenceAffichageQO.
     *
     * @return Le sequenceDTO avec les informations.
     *
     * @throws MetierException Exception
     */
    public SequenceDTO findSequenceAffichage(final SequenceAffichageQO sequenceAffichageQO)
                                      throws MetierException;

    /**
     * Retourne l'ensemble des séances associées à la séquence.
     *
     * @param resultatRechercheSequenceDTO contient l'id de la séquence
     *
     * @return la liste des séances de la séquences
     *
     * @throws MetierException Exception
     */
    public List<SeanceDTO> findSeanceBySequence(ResultatRechercheSequenceDTO resultatRechercheSequenceDTO)
                                         throws MetierException;

    /**
     * Controle l'unicité d'une séquence (enseignant, enseignement,
     * classe/groupe, intitulé).
     *
     * @param saveSequenceQO saveSequenceQO
     *
     * @return True si unique, sinon false.
     *
     * @throws MetierException Exception
     */
    public boolean checkUnicite(SaveSequenceQO saveSequenceQO)
                         throws MetierException;

    /**
     * Retourne le DTO complet de la séquence pour l'édition PDF.
     *
     * @param printSequenceQO Les paramètres de recherche
     * @param seancesDTO les seances.
     *
     * @return Report Le report
     *
     * @throws MetierException Exception
     */
    public Report printSequence(PrintSeanceOuSequenceQO printSequenceQO, List<PrintSequenceDTO> seqDTO)
                         throws MetierException;
    
    /**
     * Création d'autant de séquence qu'il de couple classe/enseignement, groupe/enseignement pour un enseignant.
     * Cette génération automatique de création de séquence est utilisée dans le cadre de la saisie simplifiée au travers l'emploi du temps.
     * Chaque séquence est créée sur la totalité de l'année scolaire.
     * @param saveSequenceSimplifieeQO les paramètres.
     * @throws MetierException l'exception potentielle.
     * @return les messages.
     */
    public ResultatDTO<Integer> saveSequenceSaisieSimplifiee(final SaveSequenceSimplifieeQO saveSequenceSimplifieeQO) throws MetierException;
    
    /**
     * Contrôle de l'existance de séquence.
     * @param idEnseignant l'id de l'enseignant.
     * @param idEtablissement l'id de l'établissement.
     * @return true or false.
     */
    public Boolean checkExistenceSequenceEnseignant(final Integer idEnseignant, final Integer idEtablissement);

    /**
     * Recherche les enseignants qui ont donné ou donneront des sequences à ces classes et groupes.
     * @param idClasse l'id de la classe.
     * @param idsGroupe  les id des groupes.
     * @param idEnseignement l'id de l'enseignement.
     * @return une Liste d'identifiant et nom des enseignants.
     */
    public List<EnseignantDTO> findEnseignantsEleve(
            Integer idClasse, Set<Integer> idsGroupe, Integer idEnseignement);

    /**
     * Recherche les enseignements qui ont rattaché à une classes et des groupes via les sequences.
     * @param rechercheEnseignementPopupQO .
     * @return une Liste d'identifiant et nom des enseignants.
     */
    public List<EnseignementDTO> findEnseignementsEleve(RechercheEnseignementPopupQO rechercheEnseignementPopupQO);

    
    /**
     * @param groupesClassesDTO g
     * @param archive a
     * @param exercice e
     * @return r
     * @throws MetierException ex
     */    
    public Integer findClasseGroupeId(GroupesClassesDTO groupesClassesDTO, final Boolean archive, final String exercice)
            throws MetierException;
    
    /**
     * @param conteneurMessage cm
     * @param groupesClassesDTO gc
     * @throws MetierException ex
     */
    public void verifierClasseGroupeSaisieAvecType(ConteneurMessage conteneurMessage, GroupesClassesDTO groupesClassesDTO) throws MetierException;

    /**
     * pour ajouter les sequences manquantes pour le mode saisie simplifée.
     * @param saveSequenceSimplifieeQO
     * @return
     * @throws MetierException
     */
	ResultatDTO<Integer> ajoutSequenceManquanteSaisieSimplifiee(
			SaveSequenceSimplifieeQO saveSequenceSimplifieeQO)
			throws MetierException;
        
}
