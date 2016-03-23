/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Enum.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils.dto;

/**
 * Enumération de tests des méthodes de clonages.
 *
 */
public enum Enum {TEST("test clonage"), TEST1("test clonage bis");final String id;

/**
    * Constructeur.
    * @param id id
    */
    private Enum(final String id) {
        this.id = id;
    }

    /**
     * 
     * Accesseur.
     *
     * @return id.
     */
    public String getId() {
        return id;
    }
}
