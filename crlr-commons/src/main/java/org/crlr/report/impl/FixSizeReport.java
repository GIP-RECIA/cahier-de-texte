/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: FixSizeReport.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

/**
 * The Class FixSizeReport.
 *
 * @author breytond.
 */
public class FixSizeReport extends PlainTextReport {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

/**
     * The Constructor.
     */
    public FixSizeReport() {
        super();
    }

/**
     * The Constructor.
     * 
     * @param data
     *            the data
     * @param name
     *            the name
     */
    public FixSizeReport(final String name, final CharSequence data) {
        super(name, data);
        setMimeType("text/txt");
    }

    /**
     * {@inheritDoc}
     */
    public String getExtension() {
        return ".txt";
    }

    /**
     * {@inheritDoc}
     */
    public String getContentType() {
        return "application/vnd.ms-excel";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "TexteLongueurFixeReport[name=" + getName() + ", data=" + getData() +
               ", mimeType=" + getMimeType() + "]";
    }
}
