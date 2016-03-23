/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * 
 */

package org.crlr.dto.application.devoir;

import java.io.Serializable;

/**
 * QO de recherche des PJ pour un devoir. Permet également un recheche dans 
 * un exerice s'il s'agit d'une archive.
 * si 
 * @author G-SAFIR-FRMP
 *
 */
public class RecherchePieceJointeDevoirQO implements Serializable { 
    
    /** Serial . */
    private static final long serialVersionUID = -5792149985736814474L;

    /** Id du devoir recherche. */
    private Integer idDevoir;
    
    /** Indique s'il s'agit d'une archive .*/
    private Boolean archive;
    
    /** Exercice s'il s'agit d'une archive .*/
    private String exercice;

    /** Constructeur. */
    public RecherchePieceJointeDevoirQO() {
        
    }

    /**
     * Accesseur de idDevoir {@link #idDevoir}.
     * @return retourne idDevoir 
     */
    public Integer getIdDevoir() {
        return idDevoir;
    }

    /**
     * Mutateur de idDevoir {@link #idDevoir}.
     * @param idDevoir the idDevoir to set
     */
    public void setIdDevoir(Integer idDevoir) {
        this.idDevoir = idDevoir;
    }

    /**
     * Accesseur de archive {@link #archive}.
     * @return retourne archive 
     */
    public Boolean getArchive() {
        return archive;
    }

    /**
     * Mutateur de archive {@link #archive}.
     * @param archive the archive to set
     */
    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    /**
     * Accesseur de exercice {@link #exercice}.
     * @return retourne exercice 
     */
    public String getExercice() {
        return exercice;
    }

    /**
     * Mutateur de exercice {@link #exercice}.
     * @param exercice the exercice to set
     */
    public void setExercice(String exercice) {
        this.exercice = exercice;
    }
    
    
    
}
