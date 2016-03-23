/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignantsEnseignementsPK.java,v 1.3 2010/04/12 01:34:33 ent_breyton Exp $
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
public class EnseignantsEnseignementsPK implements Serializable {
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_enseignant", unique = true, nullable = false)
    public Integer idEnseignant;
    @Column(name = "id_enseignement", unique = true, nullable = false)
    public Integer idEnseignement;
    @Column(name = "id_etablissement", unique = true, nullable = false)
    public Integer idEtablissement;

    /**
     * Constructeur.
     */
    public EnseignantsEnseignementsPK() {
    }

    /**
     * Constructeur.
     * @param idEnseignant l'identifiant de l'enseignant.
     * @param idEnseignement identifiant de l'enseignement.
     * @param idEtablissement identifiant de l'établissement.
     */
    public EnseignantsEnseignementsPK(final Integer idEnseignant, Integer idEnseignement, Integer idEtablissement) {
        this.idEnseignant = idEnseignant;
        this.idEnseignement = idEnseignement;
        this.idEtablissement = idEtablissement;
    }    

    /**
     * Accesseur idEnseignant.
     * @return le idEnseignant.
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur idEnseignant.
     * @param idEnseignant le idEnseignant à modifier.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur idEnseignement.
     * @return le idEnseignement.
     */
    public Integer getIdEnseignement() {
        return idEnseignement;
    }

    /**
     * Mutateur idEnseignement.
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
        if (this.idEnseignant != null) {
            _hashCode += this.idEnseignant.hashCode();
        }
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
        if (!(obj instanceof EnseignantsEnseignementsPK)) {
            return false;
        }

        final EnseignantsEnseignementsPK rhs = (EnseignantsEnseignementsPK) obj;
        
        return new EqualsBuilder().append(getIdEnseignant(), rhs.getIdEnseignant())
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
        toStringValue.append(this.idEnseignant).append('.');
        toStringValue.append(this.idEnseignement).append('.');
        toStringValue.append(this.idEtablissement);
        toStringValue.append(']');
        return toStringValue.toString();
    }

}
