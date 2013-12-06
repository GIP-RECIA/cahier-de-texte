/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: BaseHibernateBusiness.java,v 1.4 2009/05/26 08:06:30 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;

import org.crlr.exception.base.CrlrRuntimeException;
import org.apache.commons.collections.CollectionUtils;

/**
 * BaseHibernateBusiness.
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class BaseHibernateBusiness {
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private EntityManager entityManager;

/**
     * 
     * Constructeur.
     */
    @SuppressWarnings("unused")
    private BaseHibernateBusiness() {
    }

/**
     * 
     * Constructeur.
     * @param entityManager entityManager
     */
    public BaseHibernateBusiness(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Cette méthode permet de retourner le dernier Id pour l'insertion.
     * La gestion des identitfiants se fait via des séquences (une par table) en BDD. 
     * @param nomSequence le nom réel de la séquence
     *
     * @return le nouvel id.
     */
    public Integer getIdInsertion(String nomSequence) {
        final String query = "SELECT nextval('"+ nomSequence + "')";
        BigInteger newId;

        newId = (BigInteger) entityManager.createNativeQuery(query).getSingleResult();
        
        if (newId == null) {
            throw new CrlrRuntimeException("Aucune séquence nommée {0} n'est présente dans la base de données", nomSequence);
        }

        return newId.intValue();
    }
    
    /**
     * Reserve dans la sequence un nombre d'id et retourne le premier id qui a ete reserve. 
     * @param nomSequence la sequence 
     * @param nbrReservation nombre de id a reserver
     * @return le premier id qui a ete reserve.
     */
    public Integer getReservationIdInsertion(String nomSequence, Integer nbrReservation) {
        
        String query = "select nextval('" + nomSequence + "')";
        BigInteger newId = (BigInteger) entityManager.createNativeQuery(query).getSingleResult();
        
        query = 
            "select setval('" + nomSequence + "', " + (nbrReservation.intValue() + newId.intValue()) + ")";
        
        newId = (BigInteger) entityManager.createNativeQuery(query).getSingleResult();
        if (newId == null) {
            throw new CrlrRuntimeException("Aucune séquence nommée {0} n'est présente dans la base de données", nomSequence);
        }
        return newId.intValue() - nbrReservation;
    }
    
    /**
     * Cette méthode permet de vérifier l'existence d'un schéma au sein de la base de données.
     * @param schema le schéma.
     * @return true si le schema existe, false dans le cas contraire.
     */
    @SuppressWarnings("unchecked")
    public Boolean existSchemaIntoDataBase(final String schema) {
        final String checkSchemaExiste = "SELECT tablename FROM pg_tables WHERE schemaname = ?";
        final List<Object[]> listeTableSchema = 
            entityManager.createNativeQuery(checkSchemaExiste)
            .setParameter(1, schema).setMaxResults(1).getResultList();

        return !CollectionUtils.isEmpty(listeTableSchema);
    }
}
