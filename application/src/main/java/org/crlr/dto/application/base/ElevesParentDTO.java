/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ElevesParentDTO.java,v 1.1 2009/04/21 09:15:55 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import org.crlr.dto.UserDTO;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;

/**
 * ElevesParentDTO.
 *
 * @author breytond.
 */
public class ElevesParentDTO implements Serializable {
    /** Serial UID. */
    private static final long serialVersionUID = 6144329884670695777L;

    /**
     * liste des enfants.
     */
    private Set<UserDTO> listeEnfant = new HashSet<UserDTO>();

    /**
     * Accesseur.   
     *
     * @return the listeEnfant
     */
    public Set<UserDTO> getListeEnfant() {
        return listeEnfant;
    }

    /**
     * Mutateur.     
     *
     * @param listeEnfant the listeEnfant to set
     */
    public void setListeEnfant(Set<UserDTO> listeEnfant) {
        this.listeEnfant = listeEnfant;
    }
}
