/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: CrlrException.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.exception.base;

import java.text.MessageFormat;

/**
 * Exception de base. Toutes les exceptions de type "checked" (non "runtime") doivent
 * hériter de cette classe.
 *
 * @author breytond
 */
public class CrlrException extends Exception {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5909735339240772767L;

/**
     * The Constructor.
     * 
     * @param msg
     *            the msg
     * @param args
     *            the args
     * @param cause
     *            the cause
     */
    public CrlrException(final Throwable cause, final String msg, final Object... args) {
        super(MessageFormat.format(msg.replaceAll("'", "''"), args), cause);
    }

/**
     * The Constructor.
     * 
     * @param msg
     *            the msg
     * @param args
     *            the args
     */
    public CrlrException(final String msg, final Object... args) {
        this(null, msg, args);
    }
}
