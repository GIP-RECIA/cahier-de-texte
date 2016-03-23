/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PiecesJointesDevoirsBean.java,v 1.2 2009/04/28 12:45:50 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_cycle_groupe", schema="cahier_courant")
public class CycleGroupeBean implements Serializable {
    
    /** Serial.   */
    private static final long serialVersionUID = 1039662851323471542L;

    /** Identifiant. */
    @Id
    private CycleGroupePK pk;

    /** Objet Cycle. */
    @ManyToOne(cascade =  { } )
    @JoinColumns(
            {
                @JoinColumn(name = "id_cycle", referencedColumnName = "id", insertable = false, updatable = false)
            }
    )
    private CycleBean cycle;
    
    /** Objet Groupe. */
    @ManyToOne(cascade =  { } )
    @JoinColumns(
            {
                @JoinColumn(name = "id_groupe", referencedColumnName = "id", insertable = false, updatable = false)
            }
    )
    private GroupeBean groupe;
    
    /**
     * 
     * Constructeur.
     */
    public CycleGroupeBean() {
    }

    /**
     * Accesseur de pk {@link #pk}.
     * @return retourne pk
     */
    public CycleGroupePK getPk() {
        return pk;
    }

    /**
     * Mutateur de pk {@link #pk}.
     * @param pk le pk to set
     */
    public void setPk(CycleGroupePK pk) {
        this.pk = pk;
    }



    /**
     * Accesseur cycle.
     * @return cycle
     */
    public CycleBean getCycle() {
        return cycle;
    }

    /**
     * Mutateur cycle.
     * @param cycle Le cycle à modifier
     */
    public void setCycle(CycleBean cycle) {
        this.cycle = cycle;
    }

    /**
     * Accesseur de groupe {@link #groupe}.
     * @return retourne groupe
     */
    public GroupeBean getGroupe() {
        return groupe;
    }

    /**
     * Mutateur de groupe {@link #groupe}.
     * @param groupe le groupe to set
     */
    public void setGroupe(GroupeBean groupe) {
        this.groupe = groupe;
    }

    /**
     * idCycle.
     *
     * @return the idCycle attribute
     */
    public Integer getIdCycle() {
        return (this.pk != null) ? pk.idCycle : null;
    }

    /**
     * idCycle.
     *
     * @param idCycle id du cycle.
     */
    public void setIdCycle(Integer idCycle) {
        if (this.pk == null) {
            this.pk = new CycleGroupePK();
        }
        this.pk.idCycle = idCycle;
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
            this.pk = new CycleGroupePK();
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

        str.append("{[AvisAptitudeAptitudeClasseCritere], ");
        str.append("pk=" + getIdCycle() + ", ");
        str.append("pk=" + getIdGroupe() + ", ");

        str.append("{[CycleGroupe]");
        return (str.toString());
    }
}
