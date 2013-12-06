/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AbstractReport.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import org.crlr.report.Report;

import org.crlr.utils.Assert;

/**
 * Classe abstraite de base pour toutes les implémentations de l'interface {@link
 * Report}.
 *
 * @author breytond
 */
public abstract class AbstractReport implements Report {
    /** The name. */
    protected String name;

    /** The mime type. */
    protected String mimeType;

/**
     * Constructeur requis pour la sérialisation. Ne pas utiliser.
     */
    public AbstractReport() {
    }

/**
     * The Constructor.
     * 
     * @param name
     *            the name
     */
    public AbstractReport(final String name) {
        Assert.isNotNull("name", name);
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public String getNameWithExtension() {
        return getName() + getExtension();
    }

    /**
     * {@inheritDoc}
     */
    public String getMimeType() {
        return mimeType;
    }
}
