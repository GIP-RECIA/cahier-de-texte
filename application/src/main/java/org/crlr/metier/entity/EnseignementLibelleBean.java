/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignementLibelleBean.java,v 1.2 2010/03/31 15:05:11 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author breytond
 * @version $Revision: 1.2 $
  */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_libelle_enseignement", schema="cahier_courant")
public class EnseignementLibelleBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private EnseignementLibellePK pk;

    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(fetch=FetchType.LAZY, cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_enseignement", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private EnseignementBean enseignement;

    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(fetch=FetchType.LAZY, cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_etablissement", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private EtablissementBean etablissement;
    
    @Column(name = "libelle", nullable = false)
    private String libelle;

    /**
     * 
     * Constructeur.
     */
    public EnseignementLibelleBean() {
    }

    /**
     * Accesseur pk.
     * @return pk
     */
    public EnseignementLibellePK getPk() {
        return pk;
    }

    /**
     * Mutateur pk.
     * @param pk Le pk à modifier
     */
    public void setPk(EnseignementLibellePK pk) {
        this.pk = pk;
    }   
    
    /**
     * Accesseur enseignement.
     * @return le enseignement
     */
    public EnseignementBean getEnseignement() {
        return enseignement;
    }

    /**
     * Mutateur de enseignement.
     * @param enseignement le enseignement à modifier.
     */
    public void setEnseignement(EnseignementBean enseignement) {
        this.enseignement = enseignement;
    }

    /**
     * Accesseur etablissement.
     * @return le etablissement
     */
    public EtablissementBean getEtablissement() {
        return etablissement;
    }

    /**
     * Mutateur de etablissement.
     * @param etablissement le etablissement à modifier.
     */
    public void setEtablissement(EtablissementBean etablissement) {
        this.etablissement = etablissement;
    }

    /**
     * Accesseur libelle.
     * @return le libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Mutateur de libelle.
     * @param libelle le libelle à modifier.
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * idEnseignement.
     *
     * @return the idEnseignement attribute
     */
    public Integer getIdEnseignement() {
        return (this.pk != null) ? pk.idEnseignement : null;
    }

    /**
     * idEnseignement.
     *
     * @param idEnseignement id.
     */
    public void setIdEnseignement(Integer idEnseignement) {
        if (this.pk == null) {
            this.pk = new EnseignementLibellePK();
        }
        this.pk.idEnseignement = idEnseignement;
    }

    /**
     * idEtablissement.
     *
     * @return the idEtablissement attribute
     */
    public Integer getIdEtablissement() {
        return (this.pk != null) ? pk.idEtablissement : null;
    }

    /**
     * idEtablissement.
     *
     * @param idEtablissement id.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        if (this.pk == null) {
            this.pk = new EnseignementLibellePK();
        }
        this.pk.idEtablissement = idEtablissement;
    }

    /**
     * ToString.
     *
     * @return chaîne.
     */
    public String toString() {
        final StringBuffer str = new StringBuffer("{");

        str.append("{[EnseignementLibellePk], ");
        str.append("pk=" + getIdEnseignement() + ", ");
        str.append("pk=" + getIdEtablissement() + ", ");

        str.append("{[EnseignementLibelle]");
        return (str.toString());
    }
}
