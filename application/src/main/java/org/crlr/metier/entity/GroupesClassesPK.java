/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GroupesClassesPK.java,v 1.2 2009/04/03 07:04:38 vibertd Exp $
 */

package org.crlr.metier.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Composite PK.
 */
@Embeddable
public class GroupesClassesPK implements Serializable {
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_groupe", unique = true, nullable = false)
    public Integer idGroupe;
    @Column(name = "id_classe", unique = true, nullable = false)
    public Integer idClasse;

    /**
     * Constructeur.
     */
    public GroupesClassesPK() {
    }

    /**
     * Constructeur.
     * @param idGroupe le groupe.
     * @param idClasse la classe.
     */
    public GroupesClassesPK(final Integer idGroupe, final Integer idClasse) {
        this.idGroupe = idGroupe;
        this.idClasse = idClasse;
    }    

    /**
     * Accesseur idGroupe.
     * @return le idGroupe.
     */
    public Integer getIdGroupe() {
        return idGroupe;
    }

    /**
     * Mutateur idGroupe.
     * @param idGroupe le idGroupe à modifier.
     */
    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    /**
     * Accesseur idClasse.
     * @return le idClasse.
     */
    public Integer getIdClasse() {
        return idClasse;
    }

    /**
     * Mutateur idClasse.
     * @param idClasse le idClasse à modifier.
     */
    public void setIdClasse(Integer idClasse) {
        this.idClasse = idClasse;
    }

   

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public int hashCode() {
        int _hashCode = 0;
        if (this.idGroupe != null) {
            _hashCode += this.idGroupe.hashCode();
        }
        if (this.idClasse != null) {
            _hashCode += this.idClasse.hashCode();
        }
        return _hashCode;
    }

    /**
     *
     *
     * @param obj DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public boolean equals(final Object obj) {
        if (!(obj instanceof GroupesClassesPK)) {
            return false;
        }

        final GroupesClassesPK rhs = (GroupesClassesPK) obj;
        return new EqualsBuilder().append(getIdGroupe(), rhs.getIdGroupe())
                .append(getIdClasse(), rhs.getIdClasse())
                .isEquals();
        
        
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return String representation of this pk in the form of [.field1.field2].
     */
    public String toString() {
        final StringBuffer toStringValue = new StringBuffer("[.");
        toStringValue.append(this.idGroupe).append('.');
        toStringValue.append(this.idClasse);
        toStringValue.append(']');
        return toStringValue.toString();
    }

}
