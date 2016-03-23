/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConsulterSeanceQO.java,v 1.1 2009/05/26 08:06:30 ent_breyton Exp $
 */

package org.crlr.dto.application.seance;

import java.io.Serializable;

/**
 * ConsulterSeanceQO.
 *
 * @authorbreytond.
 * @version $Revision: 1.1 $
  */
public class ConsulterSeanceQO implements Serializable {
    /** Serial UID. */
    private static final long serialVersionUID = -892937330157750159L;

    /** Identifiant de la séance. */
    private Integer id;

    /** Drapeau permettant de connaître le mode de consultation. */
    private Boolean archive;

    /** l'exercice. */
    private String exercice;    

    /** Charge egalement les infos visa ? .*/
    private Boolean avecInfoVisa;
    
    /** Id enseigannt réelment connecté.  Id de l'enseigant remplacé*/
    private Integer idEnseignantConnecte;
    
    public ConsulterSeanceQO() {
        super();
        avecInfoVisa = false;
    }

    /**
     * Accesseur id.
     *
     * @return le id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur id.
     *
     * @param id le id à modifier.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur archive.
     *
     * @return le archive.
     */
    public Boolean getArchive() {
        return archive;
    }

    /**
     * Mutateur archive.
     *
     * @param archive le archive à modifier.
     */
    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    /**
     * Accesseur exercice.
     *
     * @return le exercice.
     */
    public String getExercice() {
        return exercice;
    }

    /**
     * Mutateur exercice.
     *
     * @param exercice le exercice à modifier.
     */
    public void setExercice(String exercice) {
        this.exercice = exercice;
    }

    /**
     * Accesseur de avecInfoVisa {@link #avecInfoVisa}.
     * @return retourne avecInfoVisa
     */
    public Boolean getAvecInfoVisa() {
        return avecInfoVisa;
    }

    /**
     * Mutateur de avecInfoVisa {@link #avecInfoVisa}.
     * @param avecInfoVisa le avecInfoVisa to set
     */
    public void setAvecInfoVisa(Boolean avecInfoVisa) {
        this.avecInfoVisa = avecInfoVisa;
    }

    /**
     * @return the idEnseignantConnecte
     */
    public Integer getIdEnseignantConnecte() {
        return idEnseignantConnecte;
    }

    /**
     * @param idEnseignantConnecte the idEnseignantConnecte to set
     */
    public void setIdEnseignantConnecte(Integer idEnseignantConnecte) {
        this.idEnseignantConnecte = idEnseignantConnecte;
    }
    
}
