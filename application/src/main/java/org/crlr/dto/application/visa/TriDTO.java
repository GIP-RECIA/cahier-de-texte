/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TriDTO.java,v 1.2 2009/04/22 13:14:23 ent_breyton Exp $
 */

package org.crlr.dto.application.visa;


/**
 * Classe DTO ordre de tri.
 * @author G-SAFIR-FRMP
 */
public class TriDTO {
    
    /** Colonne sur laquelle est appliquee le tri. */
    private String colonne;
    
    /** Indique l'ordre de tri ascendant. */
    private Boolean ascendant;

    
    /**
     * Constructeur.
     */
    public TriDTO() {
        colonne = null;
        ascendant = true;
    }

    /**
     * Accesseur de colonne {@link #colonne}.
     * @return retourne colonne
     */
    public String getColonne() {
        return colonne;
    }

    /**
     * Mutateur de colonne {@link #colonne}.
     * @param colonne le colonne to set
     */
    public void setColonne(String colonne) {
        this.colonne = colonne;
    }

    /**
     * Accesseur de ascendant {@link #ascendant}.
     * @return retourne ascendant
     */
    public Boolean getAscendant() {
        return ascendant;
    }

    /**
     * Mutateur de ascendant {@link #ascendant}.
     * @param ascendant le ascendant to set
     */
    public void setAscendant(Boolean ascendant) {
        this.ascendant = ascendant;
    }

    
}
