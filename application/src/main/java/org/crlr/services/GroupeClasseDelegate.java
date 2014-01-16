/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.services;

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
    
    /**
     * {@inheritDoc}
     */
    public List<GroupeDTO> findGroupesCollaboratifEnseignant(Integer idEnseignant) {
        return groupeClasseFacadeService.findGroupesCollaboratifEnseignant(idEnseignant);
    }
}
