/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EtablissementsElevesPK.java,v 1.2 2009/04/03 07:04:38 vibertd Exp $
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
public class EtablissementsElevesPK implements Serializable {
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_eleve", unique = true, nullable = false)
    public Integer idEleve;
    @Column(name = "id_etablissement", unique = true, nullable = false)
    public Integer idEtablissement;

    /**
     * Constructeur.
     */
    public EtablissementsElevesPK() {
    }

    /**
     * Constructeur.
     * @param idEleve l'identifiant de l'éléve.
     * @param idEtablissement identifiant d e l'établissement.
     */
    public EtablissementsElevesPK(final Integer idEleve, final Integer idEtablissement) {
        this.idEleve = idEleve;
        this.idEtablissement = idEtablissement;
    }    

    /**
     * Accesseur idEleve.
     * @return le idEleve.
     */
    public Integer getIdEleve() {
        return idEleve;
    }

    /**
     * Mutateur idEleve.
     * @param idEleve le idEleve à modifier.
     */
    public void setIdEleve(Integer idEleve) {
        this.idEleve = idEleve;
    }

    /**
     * Accesseur idEtablissement.
     * @return le idEtablissement.
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur idEtablissement.
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
        if (this.idEleve != null) {
            _hashCode += this.idEleve.hashCode();
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
        if (!(obj instanceof EtablissementsElevesPK)) {
            return false;
        }

        final EtablissementsElevesPK rhs = (EtablissementsElevesPK) obj;
        
        return new EqualsBuilder()
                .append(getIdEleve(), rhs.getIdEleve())
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
        toStringValue.append(this.idEleve).append('.');
        toStringValue.append(this.idEtablissement);
        toStringValue.append(']');
        return toStringValue.toString();
    }

}
