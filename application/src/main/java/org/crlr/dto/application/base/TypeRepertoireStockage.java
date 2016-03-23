/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TypeJour.java,v 1.1 2010/03/29 09:29:35 ent_breyton Exp $
 */
package org.crlr.dto.application.base;


/**
 * Enumération des types de répertoires de stockage.
 * @author christophe durupt.
 *
 */
public enum TypeRepertoireStockage {UPLOADFILE("uploadFile"), MESDOCUMENTS("Documents du CTN");
    /** Valeur du mode. */
    private final String id;
    
    /**
        * Constructeur.
        * @param id l'id.
        */
    private TypeRepertoireStockage(final String id) {
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