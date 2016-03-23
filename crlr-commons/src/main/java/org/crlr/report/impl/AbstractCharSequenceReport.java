/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AbstractCharSequenceReport.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import org.crlr.utils.Assert;

/**
 * Édition, basée sur une représentation sous forme de chaîne de caractères.
 *
 * @author breytond
 */
public abstract class AbstractCharSequenceReport extends AbstractReport {
    /** The data. */
    protected CharSequence data;

/**
     * Constructeur requis pour la sérialisation. Ne pas utiliser.
     */
    public AbstractCharSequenceReport() {
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
    public AbstractCharSequenceReport(final String name, final CharSequence data) {
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
        return data.toString().getBytes();
    }

    /**
     * Retourne les données statiques de l'édition sous forme de CharSequence.
     *
     * @return Les données statiques de l'édition sous forme de CharSequence.
     */
    public CharSequence getDataAsCharSequence() {
        return data;
    }
}
