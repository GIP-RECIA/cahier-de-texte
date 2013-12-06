/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequenceAffichageQO.java,v 1.1 2009/05/26 08:06:30 ent_breyton Exp $
 */

package org.crlr.dto.application.sequence;

import java.io.Serializable;

/**
 * SequenceAffichageQO.
 *
 * @author breytond.
 * @version $Revision: 1.1 $
  */
public class SequenceAffichageQO implements Serializable {
    /** Serial version. */
    private static final long serialVersionUID = -7635392938785276930L;
    
    /** Identifiant de la séquence. */
    private Integer id;
    
    /** Drapeau permettant de connaître le mode de consultation. */
    private Boolean archive;
    
    /** l'exercice. */
    private String exercice;

    /**
     * Accesseur id.
     * @return le id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur id.
     * @param id le id à modifier.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur archive.
     * @return le archive.
     */
    public Boolean getArchive() {
        return archive;
    }

    /**
     * Mutateur archive.
     * @param archive le archive à modifier.
     */
    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    /**
     * Accesseur exercice.
     * @return le exercice.
     */
    public String getExercice() {
        return exercice;
    }

    /**
     * Mutateur exercice.
     * @param exercice le exercice à modifier.
     */
    public void setExercice(String exercice) {
        this.exercice = exercice;
    }
}
