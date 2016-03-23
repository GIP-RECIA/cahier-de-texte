/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TypeDevoirDTO.java,v 1.2 2009/04/22 13:44:51 ent_breyton Exp $
 */

package org.crlr.dto.application.devoir;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.crlr.dto.application.base.Identifiable;
import org.crlr.dto.application.base.TypeCategorieTypeDevoir;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class TypeDevoirDTO implements Serializable, Identifiable {
    /**  */
    private static final long serialVersionUID = 2369509274195473314L;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer id;

    /** DOCUMENTATION INCOMPLETE! */
    private String code;

    /** DOCUMENTATION INCOMPLETE! */
    private String libelle;

    /** Categorie du type de devoir : 'NORMAL' ou 'MAISON'.*/
    private TypeCategorieTypeDevoir categorie;
    
    
    /**
     * Constructeur par defaut.
     */
    public TypeDevoirDTO() {
        super();
        categorie = TypeCategorieTypeDevoir.NORMAL;
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

    /**
     *  @return implemente pour le SelectOneDtoConverter. 
     */
    @Override
    public int hashCode(){
        return new HashCodeBuilder()
            .append(id)
            .toHashCode();
    }
    
    /** 
     * Nécesaire pour SelectOneDtoConverter.
     * @param obj : autre obj a comparer avec this.
     * @return true / false selon que les objets sont identiques.
     */
    @Override
    public boolean equals(final Object obj){
        if(obj instanceof TypeDevoirDTO){
            final TypeDevoirDTO other = (TypeDevoirDTO) obj;
            return new EqualsBuilder()
                .append(id, other.id)
                .isEquals();
        } else{
            return false;
        }
    }        
}
