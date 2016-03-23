/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TypeAffichage.java,v 1.1 2009/07/08 09:01:08 vibertd Exp $
 */

package org.crlr.dto.application.base;

/**
* Les type d'affichage des fichiers.
* F -> Fichier, D -> Repertoire.
* @author vibertd.
*
*/
public enum TypeFichier {FILE("F"), DIRECTORY("D");
    /** Valeur du mode. */
    private final String id;

/**
        * Constructeur.
        * @param id l'id.
        */
    private TypeFichier(final String id) {
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
