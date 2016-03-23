/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignementLibellePK.java,v 1.1 2010/03/29 09:29:36 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author breytond
 * @version $Revision: 1.1 $
 */
@Embeddable
public class EnseignementLibellePK implements Serializable {
    /** DOCUMENTATION INCOMPLETE! */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_enseignement", unique = true, nullable = false)
    public Integer idEnseignement;
    @Column(name = "id_etablissement", unique = true, nullable = false)
    public Integer idEtablissement;
    
/**
     * Constructeur.
     * @param idEnseignement l'identifiant de l'enseignement.
     * @param idEtablissement identifiant de l'établissement.
     */
    public EnseignementLibellePK(final Integer idEnseignement, final Integer idEtablissement) {
        this.idEnseignement = idEnseignement;
        this.idEtablissement = idEtablissement;
    }

/**
     * 
     * Constructeur.
     */
    public EnseignementLibellePK() {
    }
    
    /**
     * Accesseur idEnseignement.
     * @return le idEnseignement
     */
    public Integer getIdEnseignement() {
        return idEnseignement;
    }

    /**
     * Mutateur de idEnseignement.
     * @param idEnseignement le idEnseignement à modifier.
     */
    public void setIdEnseignement(Integer idEnseignement) {
        this.idEnseignement = idEnseignement;
    }

    /**
     * Accesseur idEtablissement.
     * @return le idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur de idEtablissement.
     * @param idEtablissement le idEtablissement à modifier.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
    *
    *
    * @return DOCUMENTATION INCOMPLETE!
    */
   public int hashCode() {
       int _hashCode = 0;
       if (this.idEnseignement != null) {
           _hashCode += this.idEnseignement.hashCode();
       }
       if (this.idEtablissement != null) {
           _hashCode += this.idEtablissement.hashCode();
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
       if (!(obj instanceof EnseignementLibellePK)) {
           return false;
       }

       final EnseignementLibellePK rhs = (EnseignementLibellePK) obj;
       
       return new EqualsBuilder()
               .append(getIdEnseignement(), rhs.getIdEnseignement())
               .append(getIdEtablissement(), rhs.getIdEtablissement())
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
       toStringValue.append(this.idEnseignement).append('.');
       toStringValue.append(this.idEtablissement);
       toStringValue.append(']');
       return toStringValue.toString();
   }
}
