/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GroupeDTO.java,v 1.2 2009/04/17 13:52:17 vibertd Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * GroupeDTO.
 *
 * @author breytond.
 * @version $Revision: 1.2 $
  */
public class GroupeDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = -4287704845359407065L;

    /** code du groupe ou de la classe. */
    private String code;

    /** Intitulé du groupe ou de la classe. */
    private String intitule;

    /** identifiant du groupe ou de la classe. */
    private Integer id;
    
    /** Indique si le groupe est collaboratif ou non. */
    private Boolean groupeCollaboratif;
    
    /** Indique si le groupe est selectionner. */
    private Boolean selectionner;
    
    /**
     * Constructeur.
     */
    public GroupeDTO() {
        this.selectionner = false;
    }

    /**
     * Accesseur code.
     *
     * @return le code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Mutateur code.
     *
     * @param code le code à modifier.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Accesseur intitule.
     *
     * @return le intitule.
     */
    public String getIntitule() {
        return intitule;
    }

    /**
     * Mutateur intitule.
     *
     * @param intitule le intitule à modifier.
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
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
     * Accesseur selectionner.
     *
     * @return le selectionner.
     */
    public Boolean getSelectionner() {
        return selectionner;
    }

    /**
     * Mutateur selectionner.
     *
     * @param selectionner le selectionner à modifier.
     */
    public void setSelectionner(Boolean selectionner) {
        this.selectionner = selectionner;
    }

    /**
     * Accesseur de groupeCollaboratif {@link #groupeCollaboratif}.
     * @return retourne groupeCollaboratif
     */
    public Boolean getGroupeCollaboratif() {
        return groupeCollaboratif;
    }

    /**
     * Mutateur de groupeCollaboratif {@link #groupeCollaboratif}.
     * @param groupeCollaboratif le groupeCollaboratif to set
     */
    public void setGroupeCollaboratif(Boolean groupeCollaboratif) {
        this.groupeCollaboratif = groupeCollaboratif;
    }
    
}
