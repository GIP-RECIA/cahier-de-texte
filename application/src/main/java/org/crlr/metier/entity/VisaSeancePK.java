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
public class VisaSeancePK implements Serializable {
    /** DOCUMENTATION INCOMPLETE! */
    private static final long serialVersionUID = 1L;
    
    @Column(name = "id_visa", unique = true, nullable = false)
    public Integer idVisa;

    
    @Column(name = "id_seance", unique = true, nullable = false)
    public Integer idSeance;

/**
     * Constructeur.
     */
    public VisaSeancePK() {
    }

/**
     * Constructeur.
     * @param idVisa l'identifiant du visa
     * @param profil le profil
     * @param idSeance identifiant de la seance.
     */
    public VisaSeancePK(final Integer idVisa, final Integer idSeance) {
        this.idVisa = idVisa;
        this.idSeance = idSeance;
    }


    /**
 * Accesseur de idVisa {@link #idVisa}.
 * @return retourne idVisa
 */
public Integer getIdVisa() {
    return idVisa;
}

/**
 * Mutateur de idVisa {@link #idVisa}.
 * @param idVisa le idVisa to set
 */
public void setIdVisa(Integer idVisa) {
    this.idVisa = idVisa;
}

/**
 * Accesseur de idSeance {@link #idSeance}.
 * @return retourne idSeance
 */
public Integer getIdSeance() {
    return idSeance;
}

/**
 * Mutateur de idSeance {@link #idSeance}.
 * @param idSeance le idSeance to set
 */
public void setIdSeance(Integer idSeance) {
    this.idSeance = idSeance;
}


    /**
    *
    *
    * @return DOCUMENTATION INCOMPLETE!
    */
   public int hashCode() {
       int _hashCode = 0;
       if (this.idVisa != null) {
           _hashCode += this.idVisa.hashCode();
       }
       
       if (this.idSeance != null) {
           _hashCode += this.idSeance.hashCode();
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
       if (!(obj instanceof VisaSeancePK)) {
           return false;
       }


       final VisaSeancePK rhs = (VisaSeancePK) obj;
       
       return new EqualsBuilder().append(getIdVisa(), rhs.getIdVisa())
               .append(getIdSeance(), rhs.getIdSeance())
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
       toStringValue.append(this.idVisa).append('.');
       toStringValue.append(this.idSeance);
       toStringValue.append(']');
       return toStringValue.toString();
   }
}
