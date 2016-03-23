/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Log4jLog.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.log.impl;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implémentation de l'interface {@link fr.gouv.defense.marine.dpmm.siad.log.Log}
 * basée sur Log4j.
 *
 * @author romana
 */
public class Slf4jLog extends AbstractLog implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -347741512336786359L;

    /** The log. */
    private Logger log;

/**
     * Constructeur requis pour la sérialisation. Ne pas utiliser.
     */
    public Slf4jLog() {
        this(LoggerFactory.getLogger(Slf4jLog.class));
    }

/**
     * The Constructor.
     * 
     * @param log
     *            the log
     */
    public Slf4jLog(final Logger log) {
        super();
        if (log == null) {
            throw new NullPointerException("log est requis");
        }
        this.log = log;
    }

    /**
     * {@inheritDoc}
     */
    public void debug(Throwable cause, String msg, Object... args) {
        log.debug(formaterMessage(msg, args), cause);
    }

    /**
     * {@inheritDoc}
     */
    public void info(Throwable cause, String msg, Object... args) {
        log.info(formaterMessage(msg, args), cause);
    }

    /**
     * {@inheritDoc}
     */
    public void warning(Throwable cause, String msg, Object... args) {
        log.warn(formaterMessage(msg, args), cause);
    }

    /**
     * {@inheritDoc}
     */
    public void error(Throwable cause, String msg, Object... args) {
        log.error(formaterMessage(msg, args), cause);
    }

    /**
     * Write object.
     *
     * @param out the out
     *
     * @throws IOException the IO exception
     */
    private void writeObject(ObjectOutputStream out)
                      throws IOException {
        out.defaultWriteObject();
        out.writeObject(log);
    }

    /**
     * Read object.
     *
     * @param in the in
     *
     * @throws IOException the IO exception
     * @throws ClassNotFoundException the class not found exception
     */
    private void readObject(ObjectInputStream in)
                     throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        log = (Logger) in.readObject();
    }
}
