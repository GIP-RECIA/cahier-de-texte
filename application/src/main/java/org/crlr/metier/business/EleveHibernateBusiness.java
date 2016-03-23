/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EleveHibernateBusiness.java,v 1.3 2009/05/26 08:06:30 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.UserDTO;
import org.crlr.dto.application.base.RechercheGroupeQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.entity.EleveBean;
import org.crlr.metier.entity.ElevesClassesBean;
import org.crlr.metier.entity.ElevesGroupesBean;
import org.crlr.utils.Assert;
import org.crlr.utils.ObjectUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * EleveHibernateBusiness.
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
@Repository
public class EleveHibernateBusiness extends AbstractBusiness
    implements EleveHibernateBusinessService {
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public EleveBean find(String uid) {
        final EleveBean eleve;

        final String query =
            "SELECT e FROM " + EleveBean.class.getName() + " e WHERE e.uid = :uid";

        final List<EleveBean> liste =
            getEntityManager().createQuery(query).setParameter("uid", uid).getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            eleve = liste.get(0);
        } else {
            eleve = null;
        }

        return eleve;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public EleveBean find(Integer id) {
        final EleveBean eleve;

        final String query =
            "SELECT e FROM " + EleveBean.class.getName() + " e WHERE e.id = :id";

        final List<EleveBean> liste =
            getEntityManager().createQuery(query).setParameter("id", id).getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            eleve = liste.get(0);
        } else {
            eleve = null;
        }

        return eleve;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer exist(String uid) {
        final Integer id;

        final String query =
            "SELECT e.id FROM " + EleveBean.class.getName() + " e WHERE e.uid = :uid";

        final List<Integer> liste =
            getEntityManager().createQuery(query).setParameter("uid", uid).getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            id = liste.get(0);
        } else {
            id = null;
        }

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<UserDTO> findListeEleve(RechercheGroupeQO rechercheGroupeQO) {
        Assert.isNotNull("rechercheGroupeQO", rechercheGroupeQO);
        Assert.isNotNull("Classe ou groupe", (rechercheGroupeQO.getIdClasse()==null && rechercheGroupeQO.getIdGroupe()==null ? null : true));
        
        String query = "SELECT e FROM " + EleveBean.class.getName() + " e ";
        if (rechercheGroupeQO.getIdClasse()!=null) {
            query += 
                ", " + ElevesClassesBean.class.getName() + " CG "  + 
                "WHERE CG.pk.idEleve = e.id AND CG.pk.idClasse = " + rechercheGroupeQO.getIdClasse();
        } else {
            query += 
                ", " + ElevesGroupesBean.class.getName() + " CG " + 
                "WHERE CG.pk.idEleve = e.id AND CG.pk.idGroupe = " + rechercheGroupeQO.getIdGroupe();
        }
        query += " ORDER BY e.nom ASC, e.prenom ASC";
        final List<EleveBean> liste = getEntityManager().createQuery(query).getResultList();
        
        final List<UserDTO> listeEleve = new ArrayList<UserDTO>();
        if (!CollectionUtils.isEmpty(liste)) {
            for (final EleveBean eleve : liste) {
                final UserDTO user = new UserDTO();
                ObjectUtils.copyProperties(user, eleve);
                user.setIdentifiant(eleve.getId());
                listeEleve.add(user);
            }
        }
        return listeEleve;
    }
    
   /**
    * {@inheritDoc}
    */
    public Integer save(EleveBean eleve) throws MetierException {
        getEntityManager().persist(eleve);
        getEntityManager().flush();

        return eleve.getId();
    }


}
