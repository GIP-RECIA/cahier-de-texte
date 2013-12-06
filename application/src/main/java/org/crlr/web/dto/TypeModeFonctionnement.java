/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TypeModeFonctionnement.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.dto;
/**
 * Les type de mode de fonctionnement disponible.
 * L -> Lecture seule, E -> écriture.
 * @author breytond.
 *
 */
public enum TypeModeFonctionnement {L("L"), E("E");
    /** Valeur du mode. */
    private final String id;

    /**
     * Constructeur.
     * @param id l'id.
     */
    private TypeModeFonctionnement(final String id) {
        this.id = id;
    }

    /**
     * Accesseur id.
     *
     * @return le id.
     */
    public String getId() {
        return id;
    }
}
