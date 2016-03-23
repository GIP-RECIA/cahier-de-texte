/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignantsGroupesPK.java,v 1.1 2009/04/03 07:04:38 vibertd Exp $
 */

package org.crlr.metier.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
@Embeddable
public class EnseignantsGroupesPK implements Serializable {
    /** DOCUMENTATION INCOMPLETE! */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_enseignant", unique = true, nullable = false)
    public Integer idEnseignant;
    @Column(name = "id_groupe", unique = true, nullable = false)
    public Integer idGroupe;

/**
     * Constructeur.
     * @param idEnseignant l'identifiant de l'enseignant.
     * @param idGroupe identifiant du groupe.
     */
    public EnseignantsGroupesPK(final Integer idEnseignant, final Integer idGroupe) {
        this.idEnseignant = idEnseignant;
        this.idGroupe = idGroupe;
    }

    /**
     * 
     * Constructeur.
     */
    public EnseignantsGroupesPK() {
    }

    /**
     * Accesseur idEnseignant.
     *
     * @return idEnseignant
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur idEnseignant.
     *
     * @param idEnseignant Le idEnseignant à modifier
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur idGroupe.
     *
     * @return idGroupe
     */
    public Integer getIdGroupe() {
        return idGroupe;
    }

    /**
     * Mutateur idGroupe.
     *
     * @param idGroupe Le idGroupe à modifier
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
       if (this.idGroupe != null) {
           _hashCode += this.idGroupe.hashCode();
       }
       if (this.idEnseignant != null) {
           _hashCode += this.idEnseignant.hashCode();
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
       if (!(obj instanceof EnseignantsGroupesPK)) {
           return false;
       }

       final EnseignantsGroupesPK rhs = (EnseignantsGroupesPK) obj;
       
       return new EqualsBuilder().append(getIdGroupe(), rhs.getIdGroupe())
               .append(getIdEnseignant(), rhs.getIdEnseignant())
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
       toStringValue.append(this.idEnseignant);
       toStringValue.append(']');
       return toStringValue.toString();
   }
}
