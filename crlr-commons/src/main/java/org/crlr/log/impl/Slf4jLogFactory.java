
/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Log4jLogFactory.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.log.impl;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.slf4j.LoggerFactory;

/**
 * Implémentation de {@link LogFactory} basée sur Slf4j.
 *
 * @author egroning
 */
public class Slf4jLogFactory extends LogFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    protected Log getLogInternal(String nom) {
        return new Slf4jLog(LoggerFactory.getLogger(nom));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected Log getLogInternal(Class clazz) {
        return new Slf4jLog(LoggerFactory.getLogger(clazz));
    }
}
