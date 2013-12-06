/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: CsvReport.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

/**
 * The Class CsvReport.
 * @author breytond.
 */
public class CsvReport extends PlainTextReport {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

/**
     * The Constructor.
     */
    public CsvReport() {
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
    public CsvReport(final String name, final CharSequence data) {
        super(name, data);
        setMimeType("text/csv");
    }

    /**
     * {@inheritDoc}
     */
    public String getExtension() {
        return ".csv";
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
        return "CsvReport[name=" + getName() + ", data=" + getData() + ", mimeType=" +
               getMimeType() + "]";
    }
}
