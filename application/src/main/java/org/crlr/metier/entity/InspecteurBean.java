/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
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
 * @author $author$
 * @version $Revision: 1.1 $
  */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_inspecteur", schema = "cahier_courant")
public class InspecteurBean {
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

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "civilite", nullable = true)
    private String civilite;

/**
     * Constructeur.
     */
    public InspecteurBean() {
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
     * Accesseur civilite.
     *
     * @return la civilite.
     */
    public String getCivilite() {
        return civilite;
    }

    /**
     * Mutateur civilite.
     *
     * @param civilite la civilite à modifier.
     */
    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }
}
