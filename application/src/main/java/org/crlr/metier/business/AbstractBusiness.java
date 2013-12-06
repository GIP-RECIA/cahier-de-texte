/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AbstractBusiness.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.metier.business;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

/**
 * Classe abstraite définissant l'entityManager de la couche métier.
 *
 * @author breytond.
 */
public abstract class AbstractBusiness {
    /** Le persistence contexte injecté par Spring. */
    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    protected final Log log = LogFactory.getLog(getClass());
/**
     * Constructeur de l'objet GenericEntityManagerDao.java.
     */
    public AbstractBusiness() {
        super();
    }

    /**
     * Initialise le flushMode.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */

    /*public void init() {
       ((Session) entityManager.getDelegate()).setFlushMode(FlushMode.MANUAL);
       }*/
    /**
     * Getter du membre entityManager.
     *
     * @return <code>EntityManager</code> le membre entityManager.
     */
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Setter du membre entityManager.
     *
     * @param entityManager la nouvelle valeur du membre entityManager.
     */
    public void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
