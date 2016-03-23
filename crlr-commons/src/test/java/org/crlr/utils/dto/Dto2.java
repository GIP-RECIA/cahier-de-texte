/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Dto2.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils.dto;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Dto de tests des méthodes de clonages.
 *
 * @author breytond
 */
public class Dto2 implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;

    //objets immuables
    /** DOCUMENTATION INCOMPLETE! */
    private String chaine;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer entier;

    /** DOCUMENTATION INCOMPLETE! */
    private Long longue;

    //collections complexes
    /** DOCUMENTATION INCOMPLETE! */
    private List<Map<Long, ?>> listeMapLongObject = new ArrayList<Map<Long, ?>>();

    /**
     * Accesseur de chaine.
     *
     * @return chaine
     */
    public String getChaine() {
        return chaine;
    }

    /**
     * Mutateur chaine.
     *
     * @param chaine le chaine
     */
    public void setChaine(String chaine) {
        this.chaine = chaine;
    }

    /**
     * Accesseur de entier.
     *
     * @return entier
     */
    public Integer getEntier() {
        return entier;
    }

    /**
     * Mutateur entier.
     *
     * @param entier le entier
     */
    public void setEntier(Integer entier) {
        this.entier = entier;
    }

    /**
     * Accesseur de listeMapLongObject.
     *
     * @return listeMapLongObject
     */
    public List<Map<Long, ?>> getListeMapLongObject() {
        return listeMapLongObject;
    }

    /**
     * Mutateur listeMapLongObject.
     *
     * @param listeMapLongObject le listeMapLongObject
     */
    public void setListeMapLongObject(List<Map<Long, ?>> listeMapLongObject) {
        this.listeMapLongObject = listeMapLongObject;
    }

    /**
     * Accesseur de longue.
     *
     * @return longue
     */
    public Long getLongue() {
        return longue;
    }

    /**
     * Mutateur longue.
     *
     * @param longue le longue
     */
    public void setLongue(Long longue) {
        this.longue = longue;
    }
}
