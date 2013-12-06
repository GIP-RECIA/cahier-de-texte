/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TypeDevoirQO.java,v 1.1 2010/04/01 11:06:15 ent_breyton Exp $
 */

package org.crlr.dto.application.devoir;

import java.io.Serializable;

import org.crlr.dto.application.base.TypeCategorieTypeDevoir;

/**
 * TypeDevoirQO.
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class TypeDevoirQO implements Serializable {
    /** SERIAL UID. */
    private static final long serialVersionUID = -454768589731024355L;
    
    /** id. */
    private Integer id;
    
    /** libellé. */
    private String libelle;

    /** Categorie. */
    private TypeCategorieTypeDevoir categorie;
    
    /** identifiant de l'établissement. */
    private Integer idEtablissement;

    /**
     * Accesseur id.
     * @return le id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur de id.
     * @param id le id à modifier.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur libelle.
     * @return le libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Mutateur de libelle.
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
     * Accesseur de categorie {@link #categorie}.
     * @return retourne categorie
     */
    public TypeCategorieTypeDevoir getCategorie() {
        return categorie;
    }

    /**
     * Mutateur de categorie {@link #categorie}.
     * @param categorie le categorie to set
     */
    public void setCategorie(TypeCategorieTypeDevoir categorie) {
        this.categorie = categorie;
    }
    
    
}
