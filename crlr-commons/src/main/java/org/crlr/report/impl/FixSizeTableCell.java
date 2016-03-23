/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: FixSizeTableCell.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import org.apache.commons.lang.StringUtils;

/**
 * The Class FixSizeTableCell.
 * @author breytond.
 */
public class FixSizeTableCell {
    /** Largeur utile. */
    private int width;

    /** Valeur. */
    private Object value;

/**
     * The Constructor.
     * 
     * @param width largeur
     * @param value objet
     */
    public FixSizeTableCell(int width, Object value) {
        this.width = width;
        this.value = value;
    }

/**
     * Constructeur. 
     * @param value objet
     */
    public FixSizeTableCell(Object value) {
        final String strValue =
            (value != null) ? StringUtils.trimToEmpty(value.toString()) : "";
        this.value = strValue;
        this.width = strValue.length() + 1;
    }

    /**
     * Returns the width of this FixSizeTableCell.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width attribute of this FixSizeTableCell.
     *
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Returns the value of this FixSizeTableCell.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value attribute of this FixSizeTableCell.
     *
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "FixSizeTableCell[width=" + width + ", value=" + value + "]";
    }
}
