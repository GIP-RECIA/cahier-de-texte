/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignantsClassesPK.java,v 1.1 2009/04/03 07:04:38 vibertd Exp $
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
public class EnseignantsClassesPK implements Serializable {
    /** DOCUMENTATION INCOMPLETE! */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_enseignant", unique = true, nullable = false)
    public Integer idEnseignant;
    @Column(name = "id_classe", unique = true, nullable = false)
    public Integer idClasse;

/**
     * Constructeur.
     * @param idEnseignant l'identifiant de l'enseignant.
     * @param idClasse identifiant de la classe.
     */
    public EnseignantsClassesPK(final Integer idEnseignant, final Integer idClasse) {
        this.idEnseignant = idEnseignant;
        this.idClasse = idClasse;
    }

/**
     * 
     * Constructeur.
     */
    public EnseignantsClassesPK() {
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
     * Accesseur idClasse.
     *
     * @return idClasse
     */
    public Integer getIdClasse() {
        return idClasse;
    }

    /**
     * Mutateur idClasse.
     *
     * @param idClasse Le idClasse à modifier
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
       if (this.idClasse != null) {
           _hashCode += this.idClasse.hashCode();
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
       if (!(obj instanceof EnseignantsClassesPK)) {
           return false;
       }

       final EnseignantsClassesPK rhs = (EnseignantsClassesPK) obj;
        
       return new EqualsBuilder().append(getIdEnseignant(), rhs.getIdEnseignant())
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
       toStringValue.append(this.idClasse).append('.');
       toStringValue.append(this.idEnseignant);
       toStringValue.append(']');
       return toStringValue.toString();
   }
}
