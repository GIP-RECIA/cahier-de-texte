/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EleveHibernateBusinessService.java,v 1.3 2009/05/26 08:06:30 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.List;

import org.crlr.dto.UserDTO;
import org.crlr.dto.application.base.RechercheGroupeQO;
import org.crlr.exception.metier.MetierException;

import org.crlr.metier.entity.EleveBean;

/**
 * EleveHibernateBusinessService.
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public interface EleveHibernateBusinessService {
    /**
     * Recherche d'un élève.
     *
     * @param uid l'uid.
     *
     * @return l'élève.
     */
    public EleveBean find(String uid);
    
    /**
     * Recherche d'un élève.
     *
     * @param id l'id de l'élève.
     *
     * @return l'élève.
     */
    public EleveBean find(Integer id);

    /**
     * Recherche les eleves qui font partie d'une classe ou d'un groupe.
     * @param rechercheGroupeQO contient l'id de la classe ou du groupe 
     * @return la liste des eleves
     */
    public List<UserDTO> findListeEleve(RechercheGroupeQO rechercheGroupeQO);
    
    /**
     * Recherche l'existence d'un élève.
     *
     * @param uid l'uid
     *
     * @return l'identifiant.
     */
    public Integer exist(String uid);

    /**
     * Mise à jour d'un élève.
     *
     * @param eleve l'élève.
     *
     * @return l'identifiant.
     *
     * @throws MetierException l'exception potentielle.
     */
    public Integer save(EleveBean eleve) throws MetierException;
}
