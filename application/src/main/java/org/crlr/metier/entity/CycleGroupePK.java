/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PiecesJointesDevoirsPK.java,v 1.1 2009/04/03 07:04:38 vibertd Exp $
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
public class CycleGroupePK implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_cycle", unique = true, nullable = false)
    public Integer idCycle;
    @Column(name = "id_groupe", unique = true, nullable = false)
    public Integer idGroupe;

    /**
     * Constructeur.
     */
    public CycleGroupePK() {
    }

/**
     * Constructeur.
     * @param idCycle l'identifiant de la sequence pedagogique.
     * @param idGroupe identifiant du groupe (collaboratif).
     */
    public CycleGroupePK(final Integer idCycle, final Integer idGroupe) {
        this.idCycle = idCycle;
        this.idGroupe = idGroupe;
    }

    /**
     * Accesseur idCycle.
     * @return idCycle
     */
    public Integer getIdCycle() {
        return idCycle;
    }

    /**
     * Mutateur idCycle.
     *
     * @param idCycle à modifier
     */
    public void setIdCycle(Integer idCycle) {
        this.idCycle = idCycle;
    }

    /**
     * Accesseur de idGroupe {@link #idGroupe}.
     * @return retourne idGroupe
     */
    public Integer getIdGroupe() {
        return idGroupe;
    }

    /**
     * Mutateur de idGroupe {@link #idGroupe}.
     * @param idGroupe le idGroupe to set
     */
    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    /**
    * @return le hashCode
    */
   public int hashCode() {
       int _hashCode = 0;
       if (this.idCycle != null) {
           _hashCode += this.idCycle.hashCode();
       }
       if (this.idGroupe != null) {
           _hashCode += this.idGroupe.hashCode();
       }
       return _hashCode;
   }

   /**
    * @param obj DOCUMENTATION INCOMPLETE!
    * @return DOCUMENTATION INCOMPLETE!
    */
   public boolean equals(final Object obj) {
       if (!(obj instanceof CycleGroupePK)) {
           return false;
       }

       final CycleGroupePK rhs = (CycleGroupePK) obj;

       return new EqualsBuilder().append(getIdCycle(), rhs.getIdCycle())
               .append(getIdGroupe(), rhs.getIdGroupe())
               .isEquals();
   }

   /**
    * @return String representation of this pk in the form of [.field1.field2].
    */
   public String toString() {
       final StringBuffer toStringValue = new StringBuffer("[.");
       toStringValue.append(this.idCycle).append('.');
       toStringValue.append(this.idGroupe);
       toStringValue.append(']');
       return toStringValue.toString();
   }
}
