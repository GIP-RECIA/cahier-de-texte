/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: LogFactory.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.log;

import org.crlr.log.impl.Slf4jLogFactory;

/**
 * Fabrique d'objets {@link Log}. Pour obtenir une instance {@link Log}, appelez la
 * méthode {@link #getLog(Class)} ou la méthode {@link #getLog(String)}.<p>Exemple :
 * <code>Log log = LogFactory.getLog(UneClasse.class);</code>.</p>
 *
 * @author romana
 */
public abstract class LogFactory {
    /** The log factory impl. */
    private static LogFactory logFactoryImpl;

    static {
        
        logFactoryImpl = new Slf4jLogFactory();
        
    }

/**
     * The Constructor.
     */
    protected LogFactory() {
    }

    /**
     * Retourne un {@link Log} à partir d'un nom.
     *
     * @param nom the nom
     *
     * @return the log
     */
    public static Log getLog(String nom) {
        return logFactoryImpl.getLogInternal(nom);
    }

    /**
     * Retourne un {@link Log} à partir d'une classe.
     *
     * @param clazz the clazz
     *
     * @return the log
     */
    @SuppressWarnings("rawtypes")
    public static Log getLog(Class clazz) {
        return logFactoryImpl.getLogInternal(clazz);
    }

    /**
     * Une implémentation concrète fournit une instance de {@link Log}.
     *
     * @param nom the nom
     *
     * @return the log internal
     */
    protected abstract Log getLogInternal(String nom);

    /**
     * Une implémentation concrète fournit une instance de {@link Log}.
     *
     * @param clazz the clazz
     *
     * @return the log internal
     */
    @SuppressWarnings("rawtypes")
    protected abstract Log getLogInternal(Class clazz);
}
