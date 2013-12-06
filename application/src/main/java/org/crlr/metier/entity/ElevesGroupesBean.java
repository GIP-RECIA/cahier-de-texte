/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ElevesGroupesBean.java,v 1.4 2009/04/28 12:45:50 ent_breyton Exp $
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
 * @author vibertd
 * @version $Revision: 1.4 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_eleve_groupe", schema="cahier_courant")
public class ElevesGroupesBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private ElevesGroupesPK pk;

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
                name = "id_groupe", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private GroupeBean groupe;

/**
     * Constructeur.
     */
    public ElevesGroupesBean() {
    }

    /**
     * Accesseur ElevesGroupesPK.
     *
     * @return la ElevesGroupesPK.
     */
    public ElevesGroupesPK getElevesGroupesPK() {
        return pk;
    }

    /**
     * Mutateur ElevesGroupesPK.
     *
     * @param pk la ElevesGroupesPK à modifier.
     */
    public void setElevesGroupesPK(ElevesGroupesPK pk) {
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
     * @param eleve l'eleve à modifier.
     */
    public void setEleve(EleveBean eleve) {
        this.eleve = eleve;
    }

    /**
     * Accesseur groupe.
     *
     * @return le groupe.
     */
    public GroupeBean getGroupe() {
        return groupe;
    }

    /**
     * Mutateur groupe.
     *
     * @param groupe le groupe à modifier.
     */
    public void setGroupe(GroupeBean groupe) {
        this.groupe = groupe;
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
            this.pk = new ElevesGroupesPK();
        }
        this.pk.idEleve = idEleve;
    }

    /**
     * idGroupe.
     *
     * @return the idGroupe attribute
     */
    public Integer getIdGroupe() {
        return (this.pk != null) ? pk.idGroupe : null;
    }

    /**
     * idGroupe.
     *
     * @param idGroupe id du groupe.
     */
    public void setIdGroupe(Integer idGroupe) {
        if (this.pk == null) {
            this.pk = new ElevesGroupesPK();
        }
        this.pk.idGroupe = idGroupe;
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
        str.append("pk=" + getIdGroupe() + ", ");

        str.append("{[ElevesGroupes]");
        return (str.toString());
    }
}
