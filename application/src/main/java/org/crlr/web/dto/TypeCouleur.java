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
public enum TypeCouleur {
    Bleu_azur("#5484ED"), 
    Bleu("#A4BDFC"),
    Turquoise("#46D6DB"),
    
    Vert("#7AE7BF"),
    Vert_vif("#51B749"),
    Jaune("#FBD75B"),
    
    Orange("#FFB878"),
    Rouge("#FF887C"),
    Rouge_vif("#DC2127"),
    
    Violet("#DBADFF"),
    Gris("#E1E1E1"),
    Blanc("#FFFFFF");
 
    /** Valeur de la couleur. */
    private final String id;
    
    
    
    

    /**
     * @return la couleur associée.
     */
    public Color getColor() {
        if (id.startsWith("#")) {
            return new Color(Integer.parseInt(id.substring(1, 3), 16),
                    Integer.parseInt(id.substring(3, 5), 16),
                    Integer.parseInt(id.substring(5, 7), 16));
        } else if ("red".equalsIgnoreCase(id)) {
            return Color.RED;
        } else if ("gray".equalsIgnoreCase(id)) {
            return Color.GRAY;
        }
        
        return Color.WHITE;
    }
    
    
    
    /**
     * Recherche le type de couleur en fonction de l'id de la couleur.
     * @param hexString l'id de la couleur recherchée.
     * @return le TypeCouleur associé ou null si le TypeCouleur n'existe pas.
     */
    public static TypeCouleur getTypeCouleurById(final String hexString){
        for(TypeCouleur tc : TypeCouleur.values()) {
            if (tc.id.equalsIgnoreCase(hexString)) {
                return tc;
            }            
        }
        
        return null;
    }
    
/**
        * Constructeur.
        * @param id l'id.
        */
    private TypeCouleur(final String id) {
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
    
    /**
     * Accesseur id.
     *
     * @return le id.
     */
    public String getName() {
        return this.name();
    }
    
    /**
     * Indique s'il s'agit d'une couleur claire ou foncée.
     * @return vrais si la couleur est claire
     */
    public Boolean getVraiOuFauxCouleurClaire() {
        final Color color = this.getColor();  
        final Integer poid = color.getRed() + color.getGreen() + color.getBlue();
        return (poid > 3 * 0x7E); 
    }
}
