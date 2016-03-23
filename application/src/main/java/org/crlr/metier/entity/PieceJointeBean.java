/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PieceJointeBean.java,v 1.6 2009/04/28 12:45:50 ent_breyton Exp $
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
 * @version $Revision: 1.6 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_piece_jointe", schema="cahier_courant")
public class PieceJointeBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private Integer id;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "code", nullable = false)
    private String code;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "nom_fichier", nullable = false)
    private String nomFichier;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "uid", nullable = false)
    private String uid;
    
    @Column(name = "chemin", nullable = false)
    private String chemin;

    /**
     * Constructeur.
     */
    public PieceJointeBean() {
        
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
     * Accesseur nomFichier.
     *
     * @return le nom du fichier.
     */
    public String getNomFichier() {
        return nomFichier;
    }

    /**
     * Mutateur nomFichier.
     *
     * @param nomFichier le nom du fichier à modifier.
     */
    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    /**
     * Accesseur uid.
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Mutateur uid.
     * @param uid Le uid à modifier
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Accesseur chemin.
     * @return le chemin.
     */
    public String getChemin() {
        return chemin;
    }

    /**
     * Mutateur chemin.
     * @param chemin le chemin à modifier.
     */
    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

}
