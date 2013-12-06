/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignementBean.java,v 1.5 2009/04/28 12:45:50 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author vibertd
 * @version $Revision: 1.5 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_enseignement", schema="cahier_courant")
public class EnseignementBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private Integer id;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "code", nullable = false)
    private String code;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "designation", nullable = false)
    private String designation;
    
/**
     * Constructeur.
     */
    public EnseignementBean() {
    }

    /**
     * Retourne l'id.
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Positionne l'id.
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retourne le code.
     *
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * Positionne le code.
     *
     * @param code le code à modifier
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Accesseur designation.
     *
     * @return la designation.
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Mutateur designation.
     *
     * @param designation la designation à modifier.
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
