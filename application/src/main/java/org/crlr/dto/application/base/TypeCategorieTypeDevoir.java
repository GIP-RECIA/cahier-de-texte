/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TypeGroupe.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Normal --> Travail à faire
 * Devoir --> Devoir
 * @author breytond.
 *
 */
public enum TypeCategorieTypeDevoir {DEVOIR("DEVOIR", "Devoir"), NORMAL("NORMAL", "Normal");

    /** Id stocke en base. */
    private final String id;
    
    /** Libelle utilisé pour l'affichage. */
    private final String libelle;

    /** Map de correspondances entre le code / objet. */
    private static Map<String, TypeCategorieTypeDevoir> mapCategorieTypeDevoir; 
    static {
        mapCategorieTypeDevoir = new HashMap<String, TypeCategorieTypeDevoir>();
        for (TypeCategorieTypeDevoir t : TypeCategorieTypeDevoir.values()) {
            mapCategorieTypeDevoir.put(t.getId(), t);
        }
    }
    
    
    /**
    * Constructeur.
    * @param id l'id.
    * @param libelle : le libelle
    */
    private TypeCategorieTypeDevoir(final String id, final String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    /**
     * Accesseur id.
     *
     * @return le id.
     */
    public String getId() {
        return id;
    }

    /**
     * Accesseur de libelle {@link #libelle}.
     * @return retourne libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Accesseur de mapCategorieTypeDevoir {@link #mapCategorieTypeDevoir}.
     * @return retourne mapCategorieTypeDevoir
     */
    public static Map<String, TypeCategorieTypeDevoir> getMapCategorieTypeDevoir() {
        return mapCategorieTypeDevoir;
    }
}


