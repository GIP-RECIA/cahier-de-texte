/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AbstractByteArrayReport.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import org.crlr.utils.Assert;

/**
 * Édition, basée sur un tableau d'octets.
 *
 * @author breytond
 */
public abstract class AbstractByteArrayReport extends AbstractReport {
    /** The data. */
    protected byte[] data;

/**
     * Constructeur requis pour la sérialisation. Ne pas utiliser.
     */
    public AbstractByteArrayReport() {
    }

/**
     * The Constructor.
     * 
     * @param data
     *            the data
     * @param name
     *            the name
     */
    public AbstractByteArrayReport(final String name, final byte[] data) {
        super(name);
        Assert.isNotNull("data", data);
        this.data = data;
    }

    /**
     * Données statiques de l'édition.
     *
     * @return the data
     */
    public byte[] getData() {
        return data;
    }
}
