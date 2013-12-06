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
public class PiecesJointesDevoirsPK implements Serializable {
    /** DOCUMENTATION INCOMPLETE! */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_piece_jointe", unique = true, nullable = false)
    public Integer idPieceJointe;
    @Column(name = "id_devoir", unique = true, nullable = false)
    public Integer idDevoir;

/**
     * Constructeur.
     */
    public PiecesJointesDevoirsPK() {
    }

/**
     * Constructeur.
     * @param idPieceJointe l'identifiant de la piece jointe.
     * @param idDevoir identifiant du devoir.
     */
    public PiecesJointesDevoirsPK(final Integer idPieceJointe, final Integer idDevoir) {
        this.idPieceJointe = idPieceJointe;
        this.idDevoir = idDevoir;
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
     * Accesseur idDevoir.
     *
     * @return idDevoir
     */
    public Integer getIdDevoir() {
        return idDevoir;
    }

    /**
     * Mutateur idDevoir.
     *
     * @param idDevoir Le idDevoir à modifier
     */
    public void setIdDevoir(Integer idDevoir) {
        this.idDevoir = idDevoir;
    }
    
    /**
    *
    *
    * @return DOCUMENTATION INCOMPLETE!
    */
   public int hashCode() {
       int _hashCode = 0;
       if (this.idDevoir != null) {
           _hashCode += this.idDevoir.hashCode();
       }
       if (this.idPieceJointe != null) {
           _hashCode += this.idPieceJointe.hashCode();
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
       if (!(obj instanceof PiecesJointesDevoirsPK)) {
           return false;
       }

       final PiecesJointesDevoirsPK rhs = (PiecesJointesDevoirsPK) obj;
       

       return new EqualsBuilder().append(getIdDevoir(), rhs.getIdDevoir())
               .append(getIdPieceJointe(), rhs.getIdPieceJointe())
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
       toStringValue.append(this.idDevoir).append('.');
       toStringValue.append(this.idPieceJointe);
       toStringValue.append(']');
       return toStringValue.toString();
   }
}
