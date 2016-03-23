/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ReportException.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report;

import org.crlr.exception.base.CrlrRuntimeException;

/**
 * Exception levée en cas d'erreur lors de la génération d'une édition.
 *
 * @author breytond
 */
public class ReportException extends CrlrRuntimeException {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

/**
     * The Constructor.
     * 
     * @param args
     *            the args
     * @param msg
     *            the msg
     * @param cause
     *            the cause
     */
    public ReportException(final Throwable cause, final String msg, final Object... args) {
        super(cause, msg, args);
    }

/**
     * The Constructor.
     * 
     * @param args
     *            the args
     * @param msg
     *            the msg
     */
    public ReportException(final String msg, final Object... args) {
        super(msg, args);
    }
}
