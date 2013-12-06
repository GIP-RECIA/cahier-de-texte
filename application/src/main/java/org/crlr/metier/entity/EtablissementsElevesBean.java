/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EtablissementsElevesBean.java,v 1.3 2009/04/28 12:45:50 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author breytond
 * @version $Revision: 1.3 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_etab_eleve", schema="cahier_courant")
public class EtablissementsElevesBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private EtablissementsElevesPK pk;

    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_eleve", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private EleveBean eleve;

    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_etablissement", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private EtablissementBean etablissement;

/**
     * Constructeur.
     */
    public EtablissementsElevesBean() {
    }

    /**
     * Accesseur etablissementsElevesPK.
     *
     * @return le etablissementsElevesPK.
     */
    public EtablissementsElevesPK getEtablissementsElevesPK() {
        return pk;
    }

    /**
     * Mutateur etablissementsElevesPK.
     *
     * @param pk le etablissementsElevesPK à modifier.
     */
    public void setEtablissementsElevesPK(EtablissementsElevesPK pk) {
        this.pk = pk;
    }

    /**
     * Accesseur eleve.
     *
     * @return le eleve.
     */
    public EleveBean getEleve() {
        return eleve;
    }

    /**
     * Mutateur eleve.
     *
     * @param eleve le eleve à modifier.
     */
    public void setEleve(EleveBean eleve) {
        this.eleve = eleve;
    }

    /**
     * Accesseur etablissement.
     *
     * @return le etablissement.
     */
    public EtablissementBean getEtablissement() {
        return etablissement;
    }

    /**
     * Mutateur etablissement.
     *
     * @param etablissement le etablissement à modifier.
     */
    public void setEtablissement(EtablissementBean etablissement) {
        this.etablissement = etablissement;
    }

    /**
     * idEleve.
     *
     * @return the idEleve attribute
     */
    public Integer getIdEleve() {
        return (this.pk != null) ? pk.idEleve : null;
    }

    /**
     * idEleve.
     *
     * @param idEleve id de l'eleve.
     */
    public void setIdEleve(Integer idEleve) {
        if (this.pk == null) {
            this.pk = new EtablissementsElevesPK();
        }
        this.pk.idEleve = idEleve;
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
     * @param idEtablissement id de l'idEtablissement.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        if (this.pk == null) {
            this.pk = new EtablissementsElevesPK();
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

        str.append("{[AvisAptitudeAptitudeGroupeCritere], ");
        str.append("pk=" + getIdEleve() + ", ");
        str.append("pk=" + getIdEtablissement() + ", ");

        str.append("{[EtablissementsEleves]");
        return (str.toString());
    }
}
