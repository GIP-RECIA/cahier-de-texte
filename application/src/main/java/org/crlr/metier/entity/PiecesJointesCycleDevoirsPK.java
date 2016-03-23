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
public class PiecesJointesCycleDevoirsPK implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_piece_jointe", unique = true, nullable = false)
    public Integer idPieceJointe;
    @Column(name = "id_cycle_devoir", unique = true, nullable = false)
    public Integer idCycleDevoir;

/**
     * Constructeur.
     */
    public PiecesJointesCycleDevoirsPK() {
    }

/**
     * Constructeur.
     * @param idPieceJointe l'identifiant de la piece jointe.
     * @param idCycleDevoir identifiant du devoir.
     */
    public PiecesJointesCycleDevoirsPK(final Integer idPieceJointe, final Integer idCycleDevoir) {
        this.idPieceJointe = idPieceJointe;
        this.idCycleDevoir = idCycleDevoir;
    }

    /**
     * Accesseur idPieceJointe.
     *
     * @return idPieceJointe
     */
    public Integer getIdPieceJointe() {
        return idPieceJointe;
    }

    /**
     * Mutateur idPieceJointe.
     *
     * @param idPieceJointe Le idPieceJointe à modifier
     */
    public void setIdPieceJointe(Integer idPieceJointe) {
        this.idPieceJointe = idPieceJointe;
    }

    /**
     * Accesseur de idCycleDevoir {@link #idCycleDevoir}.
     * @return retourne idCycleDevoir
     */
    public Integer getIdCycleDevoir() {
        return idCycleDevoir;
    }

    /**
     * Mutateur de idCycleDevoir {@link #idCycleDevoir}.
     * @param idCycleDevoir le idCycleDevoir to set
     */
    public void setIdCycleDevoir(Integer idCycleDevoir) {
        this.idCycleDevoir = idCycleDevoir;
    }

    /**
    * @return le hashCode
    */
   public int hashCode() {
       int _hashCode = 0;
       if (this.idCycleDevoir != null) {
           _hashCode += this.idCycleDevoir.hashCode();
       }
       if (this.idPieceJointe != null) {
           _hashCode += this.idPieceJointe.hashCode();
       }
       return _hashCode;
   }

   /**
    * @param obj DOCUMENTATION INCOMPLETE!
    * @return DOCUMENTATION INCOMPLETE!
    */
   public boolean equals(final Object obj) {
       if (!(obj instanceof PiecesJointesCycleDevoirsPK)) {
           return false;
       }

       final PiecesJointesCycleDevoirsPK rhs = (PiecesJointesCycleDevoirsPK) obj;
       

       return new EqualsBuilder().append(getIdCycleDevoir(), rhs.getIdCycleDevoir())
               .append(getIdPieceJointe(), rhs.getIdPieceJointe())
               .isEquals();
   }

   /**
    * @return String representation of this pk in the form of [.field1.field2].
    */
   public String toString() {
       final StringBuffer toStringValue = new StringBuffer("[.");
       toStringValue.append(this.idCycleDevoir).append('.');
       toStringValue.append(this.idPieceJointe);
       toStringValue.append(']');
       return toStringValue.toString();
   }
}
