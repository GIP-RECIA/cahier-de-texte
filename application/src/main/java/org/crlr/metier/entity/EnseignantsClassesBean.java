/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignantsClassesBean.java,v 1.4 2010/03/29 09:29:36 ent_breyton Exp $
 */

package org.crlr.metier.entity;

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
 * @version $Revision: 1.4 $
  */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_enseignant_classe", schema="cahier_courant")
public class EnseignantsClassesBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private EnseignantsClassesPK pk;

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
                name = "id_classe", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private ClasseBean classe;

    /**
     * 
     * Constructeur.
     */
    public EnseignantsClassesBean() {
    }

    /**
     * Accesseur pk.
     * @return pk
     */
    public EnseignantsClassesPK getPk() {
        return pk;
    }

    /**
     * Mutateur pk.
     * @param pk Le pk à modifier
     */
    public void setPk(EnseignantsClassesPK pk) {
        this.pk = pk;
    }

    /**
     * Accesseur enseignant.
     * @return enseignant
     */
    public EnseignantBean getEnseignant() {
        return enseignant;
    }

    /**
     * Mutateur enseignant.
     * @param enseignant Le enseignant à modifier
     */
    public void setEnseignant(EnseignantBean enseignant) {
        this.enseignant = enseignant;
    }

    /**
     * Accesseur classe.
     * @return classe
     */
    public ClasseBean getClasse() {
        return classe;
    }

    /**
     * Mutateur classe.
     * @param classe Le classe à modifier
     */
    public void setClasse(ClasseBean classe) {
        this.classe = classe;
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
            this.pk = new EnseignantsClassesPK();
        }
        this.pk.idEnseignant = idEnseignant;
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
     * @param idClasse id du groupe.
     */
    public void setIdClasse(Integer idClasse) {
        if (this.pk == null) {
            this.pk = new EnseignantsClassesPK();
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

        str.append("{[EnseignantsClassesPk], ");
        str.append("pk=" + getIdEnseignant() + ", ");
        str.append("pk=" + getIdClasse() + ", ");

        str.append("{[EnseignantsClasses]");
        return (str.toString());
    }
}
