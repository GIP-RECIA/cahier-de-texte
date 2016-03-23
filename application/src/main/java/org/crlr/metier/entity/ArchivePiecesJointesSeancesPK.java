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
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
@Embeddable
public class ArchivePiecesJointesSeancesPK implements Serializable {
    /** DOCUMENTATION INCOMPLETE! */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_piece_jointe", unique = true, nullable = false)
    public Integer idPieceJointe;
    @Column(name = "id_archive_seance", unique = true, nullable = false)
    public Integer idArchiveSeance;
    

/**
     * Constructeur.
     */
    public ArchivePiecesJointesSeancesPK() {
    }

/**
     * Constructeur.
     * @param idPieceJointe l'identifiant de la piece jointe.
     * @param idDevoir identifiant du devoir.
     */
    public ArchivePiecesJointesSeancesPK(final Integer idPieceJointe, final Integer idSeance) {
        this.idPieceJointe = idPieceJointe;
        this.idArchiveSeance = idSeance;
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
    *
    *
    * @return DOCUMENTATION INCOMPLETE!
    */
   public int hashCode() {
       
       return new HashCodeBuilder().append(idArchiveSeance).append(this.idPieceJointe).hashCode();
       
   }

   /**
    *
    *
    * @param obj DOCUMENTATION INCOMPLETE!
    *
    * @return DOCUMENTATION INCOMPLETE!
    */
   public boolean equals(final Object obj) {
       if (!(obj instanceof ArchivePiecesJointesSeancesPK)) {
           return false;
       }

       final ArchivePiecesJointesSeancesPK rhs = (ArchivePiecesJointesSeancesPK) obj;
       
       return new EqualsBuilder().append(getIdArchiveSeance(), rhs.getIdArchiveSeance())
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
       toStringValue.append(this.getIdArchiveSeance()).append('.');
       toStringValue.append(this.idPieceJointe);
       toStringValue.append(']');
       return toStringValue.toString();
   }

/**
 * @return the idArchiveSeance
 */
public Integer getIdArchiveSeance() {
    return idArchiveSeance;
}

/**
 * @param idArchiveSeance the idArchiveSeance to set
 */
public void setIdArchiveSeance(Integer idArchiveSeance) {
    this.idArchiveSeance = idArchiveSeance;
}
}
