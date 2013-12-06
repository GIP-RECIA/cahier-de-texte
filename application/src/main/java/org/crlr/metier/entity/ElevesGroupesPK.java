/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ElevesGroupesPK.java,v 1.2 2009/04/03 07:04:38 vibertd Exp $
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
public class ElevesGroupesPK implements Serializable {
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_eleve", unique = true, nullable = false)
    public Integer idEleve;
    @Column(name = "id_groupe", unique = true, nullable = false)
    public Integer idGroupe;

    /**
     * Constructeur.
     */
    public ElevesGroupesPK() {
    }

    /**
     * Constructeur.
     * @param idEleve l'identifiant de l'éléve.
     * @param idGroupe identifiant du groupe.
     */
    public ElevesGroupesPK(final Integer idEleve, final Integer idGroupe) {
        this.idEleve = idEleve;
        this.idGroupe = idGroupe;
    }    

    /**
     * Accesseur idEleve.
     * @return le idEleve.
     */
    public Integer getIdEleve() {
        return idEleve;
    }

    /**
     * Mutateur idEleve.
     * @param idEleve le idEleve à modifier.
     */
    public void setIdEleve(Integer idEleve) {
        this.idEleve = idEleve;
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
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public int hashCode() {
        int _hashCode = 0;
        if (this.idEleve != null) {
            _hashCode += this.idEleve.hashCode();
        }
        if (this.idGroupe != null) {
            _hashCode += this.idGroupe.hashCode();
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
        if (!(obj instanceof ElevesGroupesPK)) {
            return false;
        }

        final ElevesGroupesPK rhs = (ElevesGroupesPK) obj;
        
        return new EqualsBuilder().append(getIdEleve(), rhs.getIdEleve())
                .append(getIdGroupe(), rhs.getIdGroupe())
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
        toStringValue.append(this.idEleve).append('.');
        toStringValue.append(this.idGroupe);
        toStringValue.append(']');
        return toStringValue.toString();
    }

}
