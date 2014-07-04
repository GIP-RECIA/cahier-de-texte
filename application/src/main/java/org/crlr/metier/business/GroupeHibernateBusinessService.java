/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GroupeHibernateBusinessService.java,v 1.13 2010/04/26 14:03:08 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.List;
import java.util.Set;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignantsClasseGroupeQO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.RechercheGroupeClassePopupQO;
import org.crlr.dto.application.base.RechercheGroupeQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.entity.GroupeBean;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author vibertd
 * @version $Revision: 1.13 $
  */
public interface GroupeHibernateBusinessService {
    /**
     * Recherche un groupe par son id.
     *
     * @param id L'id du gorupe à rechercher
     *
     * @return Exception
     */
    public GroupeBean find(Integer id);

    /**
     * Sauvegarde un groupe et retourne son identifiant.
     *
     * @param groupe Les informations du groupes à sauvegarder
     *
     * @return L'id du groupe sauvegardé
     *
     * @throws MetierException Exception
     */
    public Integer save(GroupeBean groupe) throws MetierException;

    /**
     * Retourne l'ensemble des groupes pour la popup en fonction des paramètres.
     *
     * @param rechercheGroupeClassePopupQO Contient l'ensemble des paramètres pour la recherche.
     *
     * @return La liste des groupes
     *
     * @throws MetierException Exception
     */
    public ResultatDTO<List<GroupesClassesDTO>> findGroupePopup(RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO)
        throws MetierException;
    
       /**
     * Vérifie qu'un groupe existe.
     *
     * @param idGroupe l'id du groupe à tester
     *
     * @return l'id de du groupe s'il existe sinon null
     *
     * @throws MetierException Exception
     */
    public Integer exist(Integer idGroupe) throws MetierException;

    /**
     * Recherche un groupe par son id et retourne ses informations.
     *
     * @param idGroupe L'id du groupe a rechercher
     *
     * @return Les informations du groupe
     */
    public GroupesClassesDTO findGroupe(Integer idGroupe)
                                 ;

    /**
     * Méthode de recherche d'un groupe par code.
     *
     * @param codeClasseGroupe Le code du groupe à rechercher.
     * @param archive archive.
     * @param exercice exercice.
     *
     * @return Un groupeBean
     *
     * @throws MetierException Exception
     */
    public Integer findByCode(final String codeClasseGroupe, final Boolean archive, final String exercice)
                          throws MetierException;

    /**
     * Permet de vérifier les droits sur un groupe.
     *
     * @param idEnseignant id l'enseignant.
     * @param idGroupe id du groupe.
     * @param archive true archive.
     * @param exercice l'exercice.
     * 
     * @return true oi false
     *
     * @throws MetierException Exception
     */
    public boolean checkDroitGroupe(final Integer idEnseignant, 
            final Integer idGroupe, final Boolean archive, final String exercice)
                             throws MetierException;
    
    /**
     * Permet de vérifier les droits sur un groupe pour un inspecteur.
     *
     * @param idInspecteur id l'inspecteur.
     * @param idGroupe id du groupe.
     * @param archive true archive.
     * @param exercice l'exercice.
     * @param idEtablissement l'id de l'etablissement.
     * 
     * @return true oi false
     *
     * @throws MetierException Exception
     */
    public boolean checkDroitGroupeInspecteur(final Integer idInspecteur, 
            final Integer idGroupe, final Boolean archive, final String exercice, final Integer idEtablissement)
                             throws MetierException;
    
    /**
     * Permet de vérifier les droits sur un groupe pour la direction.
     *
     * @param idEtablissement id l'établissement de rattachement.
     * @param idGroupe id du groupe.
     * @param archive true archive.
     * @param exercice l'exercice.
     * 
     * @return true oi false
     *
     * @throws MetierException Exception
     */
    public boolean checkDroitGroupeDirection(final Integer idEtablissement, 
            final Integer idGroupe, final Boolean archive, final String exercice)
                             throws MetierException;

    /**
     * Retourne la liste des groupes de l'eleve.
     *
     * @param idEleve L'id de l'eleve
     *
     * @return La liste des groupes
     *
     * @throws MetierException Exception
     */
    public List<GroupeBean> findGroupesEleve(Integer idEleve)
                                      throws MetierException;

    /**
     * Retourne la liste des groupes associés à la classe.
     *
     * @param rechercheGroupeQO L'id de la classe et l'id de l'enseignant
     *
     * @return La liste des groupes.
     *
     * @throws MetierException Exception
     */
    public List<GroupeDTO> findGroupeByClasse(RechercheGroupeQO rechercheGroupeQO)
                                               throws MetierException;
    
    /**
     * Recherche des id de groupes d'un élève. 
     * @param idEleve l'id de l'élève.
     * @return un set des id de groupes.
     */
    public Set<Integer> findIdGroupesEleve(final Integer idEleve);
    
    /**
     * Recherche des enseignants d'un groupe.
     * @param enseignantsClasseGroupeQO les paramètres.
     * @return la liste des enseignants
     */
    public List<EnseignantDTO> findEnseignantsGroupe(final EnseignantsClasseGroupeQO enseignantsClasseGroupeQO);

    /**
     * Recherche une liste de groupes DTO à partir de leurs identifiants.
     * @param idsGroupe les ids des groupes.
     * @return la liste des groupesDTO
     */
    public List<GroupeDTO> findGroupes(Set<Integer> idsGroupe);
    
    /**
     * Permet de récupérer l'id du groupe.
     * @param codeGroupe : le code du groupe.
     * @param idEtab : l'établissement.
     * @return l'id du groupe.
     */
    public int findIdGroupeByCodeAndEtab(String codeGroupe, int idEtab);

    /**
     * Récupère la liste des groupes qui appartiennent à un établissement.
     * @param idEtab : id de l'établissement.
     * @return la liste des id de groupes de l'établissement.
     */
    public List<Integer> findListeGroupePourEtab(Integer idEtab);
    
    /**
     * Recherche les groupes collaboratif dans lesquels intervient un enseignant.
     * @param idEnseignant l'identifiant de l'enseignant
     * @return Les groupes collaboratifs.
     */
    public List<GroupeDTO> findGroupesCollaboratifEnseignant(Integer idEnseignant);

    /**
     * Creation d'un groupe collaboratif local en base. 
     * aucun control n'est effectué.
     * @param code
     * @param nom
     * @param idEtab
     * @param idAnnee
     * @return GroupDTO du groupe créé
     */
	GroupeDTO creerGroupeCollaboratifLocal(String code, String nom,
			Integer idEtab, Integer idAnnee);

	/**
	 * Ajout d'un enseignant dans un groupe a l'aide de leurs ids.
	 * Ne doit être utilisée que pour ajouter des enseignants à des groupes collaboratif locaux
	 * @param idGroupe
	 * @param idEnseignant
	 * @throws MetierException 
	 */
	void addEnseignantInGroupeCollaboratifLocal(GroupeDTO idGroupe, Integer idEnseignant) throws MetierException;

	/**
	 * Suppression d'un enseignant d'un groupe collaboratif.
	 * @param groupe
	 * @param idEnseignant
	 * @throws MetierException
	 */
	void delEnseignantInGroupeCollaboratifLocal(GroupeDTO groupe, Integer idEnseignant) throws MetierException;
	
	/**
	 * Suppression d'un groupe collaboratif local ainsi que des entitées le référençant.
	 * @param groupe
	 * @throws MetierException
	 */
	void deleteGroupeCollaboratifLocal(GroupeDTO groupe) throws MetierException;    
   
}
