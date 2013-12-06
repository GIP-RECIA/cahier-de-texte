/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TypeCouleurJour.java,v 1.2 2010/03/31 08:08:44 ent_breyton Exp $
 */

package org.crlr.web.dto;

import java.awt.Color;

/**
 * TypeCouleurJour.
 * @author G-CRLR-ENT-FRMP
 *
 */
public enum TypeCouleurJour {
        RED("#ff0000"), GRAY("#999999"), YELLOW("#ffff00"), VAQUE("#ff0000"), VAQUETODAY("#FF7100"), 
        BLUE("#110091"), GREEN("#00ff00"), BROWN("#726237"), SILVER("#dededc"), SILVERTODAY("#F747E4"), SILVERSELECTED("#FFEB78"),
        PAIR("#4F5E87"),  PAIRTODAY("#F747E4"), PAIRSELECTED("#FFEB78"), MOIS("#9FBDEB"),
        IMPAIR("#59877E"),  IMPAIRTODAY("#F747E4"), IMPAIRSELECTED("#FFEB78")
        ;
    
    /** Valeur de la couleur. */
    private final String id;
    
    /** Valeur Color. */
    private final Color color;

/**
        * Constructeur.
        * @param id l'id.
        */
    private TypeCouleurJour(final String id) {
        this.id = id;
        final Integer c = Integer.parseInt(id.substring(1,id.length()),16);
        final Color color = new Color(c);
        this.color = color;        
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
     * Accesseur de color {@link #color}.
     * @return retourne color
     */
    public Color getColor() {
        return color;
    }
    
    
}
