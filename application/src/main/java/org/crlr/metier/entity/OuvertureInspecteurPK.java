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

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
@Embeddable
public class OuvertureInspecteurPK implements Serializable {
    /** DOCUMENTATION INCOMPLETE! */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_etablissement", unique = true, nullable = false)
    public Integer idEtablissement;
    @Column(name = "id_enseignant", unique = true, nullable = false)
    public Integer idEnseignant;
    @Column(name = "id_inspecteur", unique = true, nullable = false)
    public Integer idInspecteur;

    /**
     * .
     * @param idEnseignant .
     * @param idEtablissement .
     * @param idInspecteur .
     */
    public OuvertureInspecteurPK(final Integer idEnseignant,
                                 final Integer idEtablissement, final Integer idInspecteur) {
        this.idEtablissement = idEtablissement;
        this.idEnseignant = idEnseignant;
        this.idInspecteur = idInspecteur;
    }

/**
     * 
     * Constructeur.
     */
    public OuvertureInspecteurPK() {
    }

    /**
     * Accesseur de idEtablissement.
     *
     * @return le idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur de idEtablissement.
     *
     * @param idEtablissement le idEtablissement à modifier.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur de idEnseignant.
     *
     * @return le idEnseignant
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur de idEnseignant.
     *
     * @param idEnseignant le idEnseignant à modifier.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur de idInspecteur.
     *
     * @return le idInspecteur
     */
    public Integer getIdInspecteur() {
        return idInspecteur;
    }

    /**
     * Mutateur de idInspecteur.
     *
     * @param idInspecteur le idInspecteur à modifier.
     */
    public void setIdInspecteur(Integer idInspecteur) {
        this.idInspecteur = idInspecteur;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public int hashCode() {
        int _hashCode = 0;
        if (this.idEnseignant != null) {
            _hashCode += this.idEnseignant.hashCode();
        }
        if (this.idEtablissement != null) {
            _hashCode += this.idEtablissement.hashCode();
        }
        if (this.idInspecteur != null) {
            _hashCode += this.idInspecteur.hashCode();
        }
        return _hashCode;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public boolean equals(final Object obj) {
        if (!(obj instanceof OuvertureInspecteurPK)) {
            return false;
        }

        final OuvertureInspecteurPK rhs = (OuvertureInspecteurPK) obj;
        
        return new EqualsBuilder().append(getIdEtablissement(), rhs.getIdEtablissement())
                .append(getIdEnseignant(), rhs.getIdEnseignant())
                .append(getIdInspecteur(), rhs.getIdInspecteur())
                .isEquals();
        
        
    }

    /**
     * DOCUMENT ME!
     *
     * @return String representation of this pk in the form of [.field1.field2].
     */
    public String toString() {
        final StringBuffer toStringValue = new StringBuffer("[.");
        toStringValue.append(this.idEnseignant).append('.');
        toStringValue.append(this.idInspecteur).append('.');
        toStringValue.append(this.idEtablissement).append('.');
        toStringValue.append(']');
        return toStringValue.toString();
    }
}
