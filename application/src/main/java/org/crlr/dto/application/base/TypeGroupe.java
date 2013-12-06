/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TypeGroupe.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

/**
 * Les type de groupe.
 * G -> Groupe, C -> Classe.
 * @author breytond.
 *
 */
public enum TypeGroupe {GROUPE("G"), CLASSE("C");
    /** Valeur du mode. */
    private final String id;

/**
        * Constructeur.
        * @param id l'id.
        */
    private TypeGroupe(final String id) {
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
