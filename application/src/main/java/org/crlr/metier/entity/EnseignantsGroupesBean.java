/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignantsGroupesBean.java,v 1.4 2009/04/28 12:45:50 ent_breyton Exp $
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
 * @author $author$
 * @version $Revision: 1.4 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_enseignant_groupe", schema="cahier_courant")
public class EnseignantsGroupesBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private EnseignantsGroupesPK pk;

    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_enseignant", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private EnseignantBean enseignant;

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
     * 
     * Constructeur.
     */
    public EnseignantsGroupesBean() {
    }

    /**
     * Accesseur pk.
     *
     * @return pk
     */
    public EnseignantsGroupesPK getPk() {
        return pk;
    }

    /**
     * Mutateur pk.
     *
     * @param pk Le pk à modifier
     */
    public void setPk(EnseignantsGroupesPK pk) {
        this.pk = pk;
    }

    /**
     * Accesseur enseignant.
     *
     * @return enseignant
     */
    public EnseignantBean getEnseignant() {
        return enseignant;
    }

    /**
     * Mutateur enseignant.
     *
     * @param enseignant Le enseignant à modifier
     */
    public void setEnseignant(EnseignantBean enseignant) {
        this.enseignant = enseignant;
    }

    /**
     * Accesseur groupe.
     *
     * @return groupe
     */
    public GroupeBean getGroupe() {
        return groupe;
    }

    /**
     * Mutateur groupe.
     *
     * @param groupe Le groupe à modifier
     */
    public void setGroupe(GroupeBean groupe) {
        this.groupe = groupe;
    }

    /**
     * idEnseignant.
     *
     * @return the idEnseignant attribute
     */
    public Integer getIdEnseignant() {
        return (this.pk != null) ? pk.idEnseignant : null;
    }

    /**
     * idEnseignant.
     *
     * @param idEnseignant id de l'eleve.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        if (this.pk == null) {
            this.pk = new EnseignantsGroupesPK();
        }
        this.pk.idEnseignant = idEnseignant;
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
            this.pk = new EnseignantsGroupesPK();
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
        str.append("pk=" + getIdEnseignant() + ", ");
        str.append("pk=" + getIdGroupe() + ", ");

        str.append("{[EnseignantsGroupes]");
        return (str.toString());
    }
}
