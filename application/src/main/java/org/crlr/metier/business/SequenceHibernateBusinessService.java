/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequenceHibernateBusinessService.java,v 1.17 2010/04/21 15:08:37 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.List;
import java.util.Set;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.sequence.RechercheSequencePopupQO;
import org.crlr.dto.application.sequence.RechercheSequenceQO;
import org.crlr.dto.application.sequence.ResultatRechercheSequenceDTO;
import org.crlr.dto.application.sequence.SaveSequenceQO;
import org.crlr.dto.application.sequence.SequenceAffichageQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.entity.SequenceBean;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author vibertd
 * @version $Revision: 1.17 $
  */
public interface SequenceHibernateBusinessService {
    /**
     * Méthode qui recherche une séquence par id.
     *
     * @param id l'id de la séquence à rechercher
     *
     * @return Le sequenceBean correspondant
     *
     * @throws MetierException Exception
     */
    public SequenceBean find(Integer id) throws MetierException;
    
    /**
     * Trouve la séquence en utilisant l'id si il est présent, sinon, le code.
     * @param sequence s
     * @return le paramètre renseigné si la séquence était trouvée 
     */
    public SequenceDTO findSequenceDTO(SequenceDTO sequence);

    /**
     * Méthode de recherche des séquences pour la popup.
     *
     * @param rechercheSequencePopupQO Les paramètres de recherche
     *
     * @return La liste des séquences
     *
     * @throws MetierException Exception
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
     * @throws MetierException Exception
     */
    public ResultatDTO<List<ResultatRechercheSequenceDTO>> findSequence(RechercheSequenceQO rechercheSequenceQO)
        throws MetierException;

    /**
     * Méthode de suppression d'une séquence.
     *
     * @param resultatRechercheSequenceDTO Contient l'id de la séquence à supprimer.
     *
     * @throws MetierException Exception
     */
    public void deleteSequence(ResultatRechercheSequenceDTO resultatRechercheSequenceDTO)
                        throws MetierException;

    /**
     * Supprime toutes les sequences sans seances de l'enseignant au sein de l etablissement.
     * @param rechercheSequenceQO doit avoir idEtablissement et idEnseignant.
     * @throws MetierException exception.
     */
    public void deleteSequencesVide(RechercheSequenceQO rechercheSequenceQO) throws MetierException;
    
    /**
     * Méthode d'ajout/sauvegarde d'une séquence.
     *
     * @param saveSequenceQO Contient les infos de la séquence
     *
     * @return L'id de la séance créée/modifiée
     *
     * @throws MetierException Exception
     */
    public Integer saveSequence(SaveSequenceQO saveSequenceQO)
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
     * méthode de recherche d'une séquence par code.
     *
     * @param codeSequence le code de la séquence à rechercher
     *
     * @return Un SequenceBean
     *
     * @throws MetierException Exception
     */
    public SequenceBean findByCode(String codeSequence)
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
     * Permet de trouver l'enseignement associé à la séquence.
     *
     * @param idSequence L'id de la séquence
     *
     * @return l'id de l'enseignement
     *
     * @throws MetierException Exception
     */
    public Integer findEnseignementSequence(Integer idSequence)
                                     throws MetierException;
    
    /**
     * Contrôle de l'existance de séquence pour l'enseignant.
     * @param idEnseignant l'id de l'enseignant.
     * @param idEtablissement l'id de l'établissement.
     * @return true si des séquences existes.
     */    
    public Boolean checkExistenceSequenceEnseignant(final Integer idEnseignant, final Integer idEtablissement); 
    
    /**
     * Recherche des id de séquence d'un établissement en fonction de l'enseignant.
     * @param idEtablissement l'id de l'établissement
     * @param idEnseignant l'id de l'enseignant
     * @return les id.
     */
    public Set<Integer> findSequenceEtablissement(final Integer idEtablissement, final Integer idEnseignant);

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
     * Recherche les sequence active pour une semaine. 
     * @param rechercheSequence les champs suivants doivent être alimentés :
     *  - dateDebut, dateFin, dEnseignant, idEtablissement.
     * @return la liste des sequence actives correspondant aux criteres
     */
    public List<SequenceDTO> chercherSequenceSemaine(RechercheSequenceQO rechercheSequence);
    
    /**
     * Permet de récupérer l'id de la séquence.
     * @param idEns : id de l'enseignement.
     * @param idClasseGroupe : id de la classe ou du groupe.
     * @return l'id de la séquence.
     */
    public Integer findIdSequenceByEnsEtClasseGroupe(int idEns, int idClasseGroupe);

    /**
     * Trouve toutes les sequences d'un enseignant dans un établissement.
     * @param idEnseignant
     * @param idEtablissement
     * @return l'ensemble des séquences de l'enseignant dans l'étab.
     */
	Set<SequenceDTO> findSequenceEnseignant(Integer idEnseignant, Integer idEtablissement);
   
}
