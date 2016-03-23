/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TriComparateur.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.utils;

/**
 * TriComparateur, enum définissant 2 ordres de tri, croissant ou décroissant.
 * Enum utilisé dans le cadre du ComparateurUtils.
 * 
 * @author dethillotn
 */
public enum TriComparateur {
/** CROISSANT. */
    CROISSANT(1), 
/** DECROISSANT. */
    DECROISSANT(-1);
    /** The value. */
    private int value;

    /**
     * Gets the value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

/**
     * Instantiates a new tri comparateur.
     * 
     * @param value the value
     */
    private TriComparateur(int value) {
        this.value = value;
    }
}
