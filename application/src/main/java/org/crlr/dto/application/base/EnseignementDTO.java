/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignementDTO.java,v 1.4 2010/03/31 15:05:11 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * EnseignementDTO.
 *
 * @author breytond.
 * @version $Revision: 1.4 $
  */
public class EnseignementDTO implements Serializable, Identifiable {
    /**
     * 
     */
    
    private static final long serialVersionUID = -5387221285491548921L;
    
    /** code de l'enseignement. */
    private String code;

    /** Intitulé de l'enseignement. */
    private String intitule;
    
    /** Le libellé  de l'enseignement personnalisé par établissement. */
    private String libellePerso;

    /** identifiant du groupe ou de la classe. */
    private Integer id;

    /**
     * Accesseur code.
     * @return le code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Mutateur code.
     * @param code le code à modifier.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Accesseur intitule.
     * @return le intitule.
     */
    public String getIntitule() {
        return intitule;
    }

    /**
     * Mutateur intitule.
     * @param intitule le intitule à modifier.
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

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
     * Accesseur libellePerso.
     * @return le libellePerso
     */
    public String getLibellePerso() {
        return libellePerso;
    }

    /**
     * Mutateur de libellePerso.
     * @param libellePerso le libellePerso à modifier.
     */
    public void setLibellePerso(String libellePerso) {
        this.libellePerso = libellePerso;
    }
    
    public String getLibelle() {
        if (StringUtils.trimToNull(libellePerso) != null) {
            return libellePerso;
        }
        
        return intitule;
    }


}
