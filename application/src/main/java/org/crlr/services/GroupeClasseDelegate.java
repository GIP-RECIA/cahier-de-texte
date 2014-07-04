/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.UserDTO;
import org.crlr.dto.application.base.EnseignantsClasseGroupeQO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.RechercheGroupeClassePopupQO;
import org.crlr.dto.application.base.RechercheGroupeQO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.GroupeClasseFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * GroupeClasseDelegate.
 *
 * @author $author$
 * @version $Revision: 1.6 $
 */
@Service
public class GroupeClasseDelegate implements GroupeClasseService {
    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private GroupeClasseFacadeService groupeClasseFacadeService;

    /**
     * DOCUMENT ME!
     *
     * @param groupeClasseFacadeService DOCUMENTATION INCOMPLETE!
     */
    public void setGroupeClasseFacadeService(GroupeClasseFacadeService groupeClasseFacadeService) {
        this.groupeClasseFacadeService = groupeClasseFacadeService;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rechercheGroupeClassePopupQO DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     *
     * @throws MetierException DOCUMENTATION INCOMPLETE!
     */
    public ResultatDTO<List<GroupesClassesDTO>> findGroupeClassePopup(RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO)
        throws MetierException {
        return this.groupeClasseFacadeService.findGroupeClassePopup(rechercheGroupeClassePopupQO);
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param idClasse DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public GroupesClassesDTO findClasse(Integer idClasse){
        return this.groupeClasseFacadeService.findClasse(idClasse);
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param idGroupe DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public GroupesClassesDTO findGroupe(Integer idGroupe) {
        return this.groupeClasseFacadeService.findGroupe(idGroupe);
    }

    /**
     * {@inheritDoc}
     */
    public List<GroupeDTO> findGroupeByClasse(final RechercheGroupeQO rechercheGroupeQO)
                                       throws MetierException {
        return groupeClasseFacadeService.findGroupeByClasse(rechercheGroupeQO);
    }

    /**
     * {@inheritDoc}
     */
    public Set<Integer> findIdGroupesEleve(final Integer idEleve) {
        return groupeClasseFacadeService.findIdGroupesEleve(idEleve);
    }

    /**
     * {@inheritDoc}
     */
    public Integer findIdClasseEleve(final Integer idEleve) {
        return groupeClasseFacadeService.findIdClasseEleve(idEleve);
    }

    /**
     * {@inheritDoc}
     */
    public List<EnseignantDTO> findEnseignantsClasse(final EnseignantsClasseGroupeQO enseignantsClasseGroupeQO) {
        return groupeClasseFacadeService.findEnseignantsClasse(enseignantsClasseGroupeQO);
    }

    /**
     * {@inheritDoc}
     */
    public List<EnseignantDTO> findEnseignantsGroupe(final EnseignantsClasseGroupeQO enseignantsClasseGroupeQO) {
        return groupeClasseFacadeService.findEnseignantsGroupe(enseignantsClasseGroupeQO);
    }

    /**
     * {@inheritDoc}
     */
    public GroupesClassesDTO findClasseEleve(Integer idEleve) {
        return groupeClasseFacadeService.findClasseEleve(idEleve);
    }

    /**
     * {@inheritDoc}
     */
    public List<GroupeDTO> findGroupesEleve(Integer idEleve) {
        return groupeClasseFacadeService.findGroupesEleve(idEleve);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<UserDTO> findListeEleve(RechercheGroupeQO rechercheGroupeQO) {
        return groupeClasseFacadeService.findListeEleve(rechercheGroupeQO);
    }
    
    
    
    public List<GroupeDTO> findGroupesCollaboratifEnseignant(Integer idEnseignant) {
    	return groupeClasseFacadeService.findGroupesCollaboratifEnseignant(idEnseignant);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<GroupeDTO> findGroupesCollaboratifLocauxEnseignant(Integer idEnseignant) {
    	List<GroupeDTO> groupList = findGroupesCollaboratifEnseignant(idEnseignant);
    	
    	if (groupList != null && !groupList.isEmpty()) {

    		List<GroupeDTO> onlyLocal = new ArrayList<GroupeDTO>(groupList.size());
    		
    		for (GroupeDTO groupe : groupList) {
				if (groupe.getGroupeCollaboratifLocal() ) {
					onlyLocal.add(groupe);
				}
			}
    		
    		return onlyLocal;
    		
    		
    	}
        return groupList;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResultatDTO<GroupeDTO> saveGroupeCollaboratifLocal(GroupeDTO groupe, UtilisateurDTO user){
    	
    	GroupeDTO resultat = null;
    	
    	if (groupe.getId() == null) {
    		// c'est une création
    		Integer idEnseignant = user.getUserDTO().getIdentifiant();
    		
    		try {
				resultat = groupeClasseFacadeService.creerGroupeCollaboratif(
												groupe, 
												user.getAnneeScolaireDTO(), 
												user.getIdEtablissement(), 
												idEnseignant);
				
			} catch (MetierException e) {
				return new ResultatDTO<GroupeDTO>(null, e.getConteneurMessage());
			}
    	} else {
    		// c'est une modification du code ou de l'intitulé
    	}
    	return new ResultatDTO<GroupeDTO>(resultat, null);
    }
    
    @Override
    public void modifieEnseignantGroupe(GroupeDTO groupe, Set<Integer> aSupprimer, Set<Integer> aAjouter ) throws MetierException{
    	if (groupe.getId() == null) {
    		throw new MetierException("groupe avec identifiant null ({0})", groupe.getCode());
    	}
    	groupeClasseFacadeService.updateGroupeCollaboratif(groupe, aSupprimer, aAjouter); 
    }
    
    @Override
    public void deleteEnseignantGroupe(GroupeDTO groupe) throws MetierException {
    	if (groupe.getId() == null) {
    		throw new MetierException("groupe avec identifiant null ({0})", groupe.getCode());
    	}
    	groupeClasseFacadeService.deleteGroupeCollaboratif(groupe);
    }
    
}
