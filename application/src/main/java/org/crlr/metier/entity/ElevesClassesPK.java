/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ElevesClassesPK.java,v 1.2 2009/04/03 07:04:38 vibertd Exp $
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
public class ElevesClassesPK implements Serializable {
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_eleve", unique = true, nullable = false)
    public Integer idEleve;
    @Column(name = "id_classe", unique = true, nullable = false)
    public Integer idClasse;

    /**
     * Constructeur.
     */
    public ElevesClassesPK() {
    }

    /**
     * Constructeur.
     * @param idEleve l'identifiant de l'éléve.
     * @param idClasse identifiant de la classe.
     */
    public ElevesClassesPK(final Integer idEleve, final Integer idClasse) {
        this.idEleve = idEleve;
        this.idClasse = idClasse;
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
        if (this.idEleve != null) {
            _hashCode += this.idEleve.hashCode();
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
        
        final ElevesClassesPK rhs = (ElevesClassesPK) obj;
                
        if(!(obj instanceof ElevesClassesPK)) {
            return false;
        }
            
        return new EqualsBuilder().append(getIdEleve(), rhs.getIdEleve())
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
        toStringValue.append(this.idEleve).append('.');
        toStringValue.append(this.idClasse);
        toStringValue.append(']');
        return toStringValue.toString();
    }

}
