/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GroupeClasseFacadeService.java,v 1.6 2010/04/26 14:03:08 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.List;
import java.util.Set;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.UserDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.EnseignantsClasseGroupeQO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.RechercheGroupeClassePopupQO;
import org.crlr.dto.application.base.RechercheGroupeQO;
import org.crlr.exception.metier.MetierException;

/**
 * GroupeClasseFacadeService.
 *
 * @author $author$
 * @version $Revision: 1.6 $
  */
public interface GroupeClasseFacadeService {
    /**
     * findGroupeClassePopup.
     *
     * @param rechercheGroupeClassePopupQO findGroupeClassePopup
     *
     * @return Une liste de GroupesClassesDTO
     *
     * @throws MetierException MetierException
     */
    public ResultatDTO<List<GroupesClassesDTO>> findGroupeClassePopup(RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO)
        throws MetierException;

    /**
     * 
    DOCUMENT ME!
     *
     * @param idClasse DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public GroupesClassesDTO findClasse(Integer idClasse);

    /**
     * 
    DOCUMENT ME!
     *
     * @param idGroupe DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public GroupesClassesDTO findGroupe(Integer idGroupe);

    /**
     * Retourne la liste des groupes associés à la classe.
     *
     * @param rechercheGroupeQO Le code de la classe et l'id de l'enseignant
     *
     * @return La liste des groupes.
     *
     * @throws MetierException DOCUMENTATION INCOMPLETE!
     */
    public List<GroupeDTO> findGroupeByClasse(RechercheGroupeQO rechercheGroupeQO)
                                               throws MetierException;
    
    /**
     * Retourne la liste identifiants de groupes associés à un élève.
     *
     * @param idEleve identifiant de l'élève
     *
     * @return La liste des groupes.
     * 
     */
    public Set<Integer> findIdGroupesEleve(final Integer idEleve);
    
    /**
     * Retourne l'identifiant de la classe d'un élève.
     *
     * @param idEleve identifiant de l'élève
     *
     * @return L'identifiant de l'élève
     * 
     */
    public Integer findIdClasseEleve(final Integer idEleve);
    
    /**
     * Recherche des enseignants d'une classe.
     * @param enseignantsClasseGroupeQO les paramètres.
     * @return la liste des enseignants
     */
    public List<EnseignantDTO> findEnseignantsClasse(final EnseignantsClasseGroupeQO enseignantsClasseGroupeQO);
    
    /**
     * Recherche des enseignants d'un groupe.
     * @param enseignantsClasseGroupeQO les paramètres.
     * @return la liste des enseignants
     */
    public List<EnseignantDTO> findEnseignantsGroupe(final EnseignantsClasseGroupeQO enseignantsClasseGroupeQO);
    
    /**
     * Recherche la classe d'un élève.
     * @param idEleve l'identifiant de l'élève
     * @return La classe de l'élève.
     */
    public GroupesClassesDTO findClasseEleve(Integer idEleve);

    /**
     * Recherche les groupes d'un élève.
     * @param idEleve l'identifiant de l'élève
     * @return Les groupes de l'élève.
     */
    public List<GroupeDTO> findGroupesEleve(Integer idEleve);
    
    /**
     * Recherche les eleves qui font partie d'une classe ou d'un groupe.
     * @param rechercheGroupeQO contient l'id de la classe ou du groupe 
     * @return la liste des eleves
     */
    public List<UserDTO> findListeEleve(RechercheGroupeQO rechercheGroupeQO);
 
    /**
     * Recherche les groupes collaboratif dans lesquels intervient un enseignant.
     * @param idEnseignant l'identifiant de l'enseignant
     * @return Les groupes collaboratifs.
     */
    public List<GroupeDTO> findGroupesCollaboratifEnseignant(Integer idEnseignant);

    /**
     * Creation d'un nouveau groupe collaboratif local.
     * Pour un établissement et une année scolaire.
     * @param groupe
     * @param anneeScolaire
     * @param idEtablissement
     * @param idEnseignant TODO
     * @return
     * @throws MetierException 
     */
	GroupeDTO creerGroupeCollaboratif(GroupeDTO groupe,
			AnneeScolaireDTO anneeScolaire, int idEtablissement, Integer idEnseignant) throws MetierException;
	
	/**
	 * Mise a jour des membres d'un groupe collaboratif local
	 * @param groupe
	 * @param idsAsupprimer id des membres a supprimer
	 * @param idsAajouter id des membres a ajouter
	 * @throws MetierException
	 */
	void updateGroupeCollaboratif(GroupeDTO groupe, Set<Integer> idsAsupprimer,
			Set<Integer> idsAajouter) throws MetierException;
	
	/**
	 * Suppression d'un groupe collaboratif local et toutes entitées le référençant.
	 * @param groupe
	 * @throws MetierException
	 */
	void deleteGroupeCollaboratif(GroupeDTO groupe) throws MetierException;    
}

