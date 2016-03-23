/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TypeDevoirBean.java,v 1.5 2010/03/29 09:29:36 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name = "cahier_type_devoir", schema="cahier_courant")
public class TypeDevoirBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private Integer id;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "code", nullable = false)
    private String code;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "libelle", nullable = false)
    private String libelle;

    /** Categrorie du devoir : 'NORMAL' ou 'DEVOIR'. */
    @Column(name = "categorie", nullable = false)
    private String categorie;
    
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "id_etablissement", nullable = false)
    private Integer idEtablissement;

    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_etablissement", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private EtablissementBean etablissement;

    /**
     * Constructeur.
     */
    public TypeDevoirBean() {
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
     * Accesseur libelle.
     * @return le libelle.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Mutateur libelle.
     * @param libelle le libelle à modifier.
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Accesseur idEtablissement.
     * @return le idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur de idEtablissement.
     * @param idEtablissement le idEtablissement à modifier.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur etablissement.
     * @return le etablissement
     */
    public EtablissementBean getEtablissement() {
        return etablissement;
    }

    /**
     * Mutateur de etablissement.
     * @param etablissement le etablissement à modifier.
     */
    public void setEtablissement(EtablissementBean etablissement) {
        this.etablissement = etablissement;
    }

    /**
     * Accesseur de categorie {@link #categorie}.
     * @return retourne categorie
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * Mutateur de categorie {@link #categorie}.
     * @param categorie le categorie to set
     */
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

   
}
