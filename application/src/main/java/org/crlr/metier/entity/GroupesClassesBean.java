/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GroupesClassesBean.java,v 1.3 2009/04/28 12:45:50 ent_breyton Exp $
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
 * @version $Revision: 1.3 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_groupe_classe", schema="cahier_courant")
public class GroupesClassesBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private GroupesClassesPK pk;

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

    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_classe", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private ClasseBean classe;

/**
     * Constructeur.
     */
    public GroupesClassesBean() {
    }

    /**
     * Accesseur GroupesClassesPK.
     *
     * @return le GroupesClassesPK.
     */
    public GroupesClassesPK getGroupesClassesPK() {
        return pk;
    }

    /**
     * Mutateur GroupesClassesPK.
     *
     * @param pk le GroupesClassesPK à modifier.
     */
    public void setGroupesClassesPK(GroupesClassesPK pk) {
        this.pk = pk;
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
     * Accesseur classe.
     *
     * @return la classet.
     */
    public ClasseBean getClasse() {
        return classe;
    }

    /**
     * Mutateur classe.
     *
     * @param classe la classe à modifier.
     */
    public void setClasse(ClasseBean classe) {
        this.classe = classe;
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
            this.pk = new GroupesClassesPK();
        }
        this.pk.idGroupe = idGroupe;
    }

    /**
     * idClasse.
     *
     * @return the idClasse attribute
     */
    public Integer getIdClasse() {
        return (this.pk != null) ? pk.idClasse : null;
    }

    /**
     * idClasse.
     *
     * @param idClasse id de la classe.
     */
    public void setIdClasse(Integer idClasse) {
        if (this.pk == null) {
            this.pk = new GroupesClassesPK();
        }
        this.pk.idClasse = idClasse;
    }

    /**
     * ToString.
     *
     * @return chaîne.
     */
    public String toString() {
        final StringBuffer str = new StringBuffer("{");

        str.append("{[AvisAptitudeAptitudeGroupeCritere], ");
        str.append("pk=" + getIdGroupe() + ", ");
        str.append("pk=" + getIdClasse() + ", ");

        str.append("{[GroupesClasses]");
        return (str.toString());
    }
}
