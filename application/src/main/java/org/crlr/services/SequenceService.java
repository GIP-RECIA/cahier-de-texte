/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequenceService.java,v 1.11 2010/04/12 01:34:33 ent_breyton Exp $
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
import org.crlr.report.Report;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.11 $
  */
public interface SequenceService {
    /**
     * Méthode de recherche des séquences pour la popup.
     *
     * @param rechercheSequencePopupQO Les paramètres de recherche
     *
     * @return La liste des séquences
     *
     * @throws MetierException DOCUMENTATION INCOMPLETE!
     */
    public ResultatDTO<List<SequenceDTO>> findSequencePopup(final RechercheSequencePopupQO rechercheSequencePopupQO)
        throws MetierException;

    /**
     * Méthode de recherche des séquences pour la liste de résultat.
     *
     * @param criteres Les critères de recherche
     *
     * @return La liste des séquences
     *
     * @throws MetierException DOCUMENTATION INCOMPLETE!
     */
    public ResultatDTO<List<ResultatRechercheSequenceDTO>> findSequence(RechercheSequenceQO criteres)
        throws MetierException;

    /**
     * Méthode de suppression d'une séquence.
     *
     * @param resultatRechercheSequenceDTO Contient l'id de la séquence à supprimer.
     *
     * @return Un resultatDTO contenant le conteneur de message.
     *
     * @throws MetierException DOCUMENTATION INCOMPLETE!
     */
    public ResultatDTO<Integer> deleteSequence(ResultatRechercheSequenceDTO resultatRechercheSequenceDTO)
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
     * @return L'id de la séquence créée/modifiée
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
     * @param list la liste complete des seances.
     *
     * @return Report Le report
     *
     * @throws MetierException Exception
     */
    public Report printSequence(PrintSeanceOuSequenceQO printSequenceQO, List<PrintSequenceDTO> list)
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
     * Recherche les enseignants qui ont donné ou donneront des sequences à ces classes et groupes.
     * @param idClasse l'id de la classe.
     * @param idsGroupe  les id des groupes.
     * @param idEnseignement l'id de l'enseignement.
     * @return une Liste d'identifiant et nom des enseignants.
     */
    public List<EnseignantDTO> findEnseignantsEleve(Integer idClasse, Set<Integer> idsGroupe, Integer idEnseignement);

    /**
     * Recherche les enseignements qui ont rattaché à une classes et des groupes via les sequences.
     * @param rechercheEnseignementPopupQO .
     * @return une Liste d'identifiant et nom des enseignants.
     */
    public List<EnseignementDTO> findEnseignementsEleve(RechercheEnseignementPopupQO rechercheEnseignementPopupQO);




}
