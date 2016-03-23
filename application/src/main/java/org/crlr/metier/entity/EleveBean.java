/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EleveBean.java,v 1.4 2009/04/28 12:45:50 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author breytond
 * @version $Revision: 1.4 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_eleve", schema="cahier_courant")
public class EleveBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private Integer id;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "uid", nullable = false)
    private String uid;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "nom", nullable = false)
    private String nom;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "prenom", nullable = false)
    private String prenom;

/**
     * Constructeur.
     */
    public EleveBean() {
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
     * Accesseur uid.
     *
     * @return le uid.
     */
    public String getUid() {
        return uid;
    }

    /**
     * Mutateur uid.
     *
     * @param uid le uid à modifier.
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Accesseur nom.
     *
     * @return le nom.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Mutateur nom.
     *
     * @param nom le nom à modifier.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Accesseur prenom.
     *
     * @return le prenom.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Mutateur prenom.
     *
     * @param prenom le prenom à modifier.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
