/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Composite PK.
 */
@Embeddable
public class EtablissementsEnseignantsPK implements Serializable {
    /** DOCUMENTATION INCOMPLETE! */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_etablissement", unique = true, nullable = false)
    public Integer idEtablissement;
    @Column(name = "id_enseignant", unique = true, nullable = false)
    public Integer idEnseignant;

/**
     * Constructeur.
     */
    public EtablissementsEnseignantsPK() {
    }

/**
     * Constructeur.
     * @param idEtablissement l'identifiant de l'établissement.
     * @param idEnseignant identifiant de l'enseignant.
     */
    public EtablissementsEnseignantsPK(final Integer idEtablissement,
                                       final Integer idEnseignant) {
        this.idEtablissement = idEtablissement;
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur idEtablissement.
     *
     * @return le idEtablissement.
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur idEtablissement.
     *
     * @param idEtablissement le idEtablissement à modifier.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur idEnseignant.
     *
     * @return le idEnseignant.
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur idEnseignant.
     *
     * @param idEnseignant le idEnseignant à modifier.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public int hashCode() {
        int _hashCode = 0;
        if (this.idEtablissement != null) {
            _hashCode += this.idEtablissement.hashCode();
        }
        if (this.idEnseignant != null) {
            _hashCode += this.idEnseignant.hashCode();
        }
        return _hashCode;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param obj DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public boolean equals(final Object obj) {
        if ((obj == null) || !(obj instanceof EtablissementsEnseignantsPK)) {
            return false;
        }

        final EtablissementsEnseignantsPK pk = (EtablissementsEnseignantsPK) obj;
        boolean eq = true;

        if (this.idEtablissement != null) {
            eq = eq && this.idEtablissement.equals(pk.getIdEtablissement());
        } else {
            eq = eq && (pk.getIdEtablissement() == null);
        }
        if (this.idEnseignant != null) {
            eq = eq && this.idEnseignant.equals(pk.getIdEnseignant());
        } else {
            eq = eq && (pk.getIdEnseignant() == null);
        }

        return eq;
    }

    /**
     * DOCUMENT ME!
     *
     * @return String representation of this pk in the form of [.field1.field2].
     */
    public String toString() {
        final StringBuffer toStringValue = new StringBuffer("[.");
        toStringValue.append(this.idEtablissement).append('.');
        toStringValue.append(this.idEnseignant);
        toStringValue.append(']');
        return toStringValue.toString();
    }
}
