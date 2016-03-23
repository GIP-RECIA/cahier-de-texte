/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ElevesClassesBean.java,v 1.3 2009/04/28 12:45:50 ent_breyton Exp $
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
@Table(name = "cahier_eleve_classe", schema="cahier_courant")
public class ElevesClassesBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private ElevesClassesPK pk;

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
                name = "id_classe", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private ClasseBean classe;

/**
     * Constructeur.
     */
    public ElevesClassesBean() {
    }

    /**
     * Accesseur ElevesClassesPK.
     *
     * @return la elevesClassesPK.
     */
    public ElevesClassesPK getElevesClassesPK() {
        return pk;
    }

    /**
     * Mutateur ElevesClassesPK.
     *
     * @param pk la ElevesClassesPK à modifier.
     */
    public void setElevesClassesPK(ElevesClassesPK pk) {
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
     * Accesseur classe.
     *
     * @return la classe.
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
            this.pk = new ElevesClassesPK();
        }
        this.pk.idEleve = idEleve;
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
            this.pk = new ElevesClassesPK();
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
        str.append("pk=" + getIdEleve() + ", ");
        str.append("pk=" + getIdClasse() + ", ");

        str.append("{[ElevesClasses]");
        return (str.toString());
    }
}
