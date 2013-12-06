/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignantsEnseignementsBean.java,v 1.6 2010/06/09 10:10:50 ent_breyton Exp $
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
 * @version $Revision: 1.6 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_enseignant_enseignement", schema="cahier_courant")
public class EnseignantsEnseignementsBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private EnseignantsEnseignementsPK pk;

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
                name = "id_enseignement", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private EnseignementBean enseignement;
    
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
    public EnseignantsEnseignementsBean() {
    }

    /**
     * Accesseur EnseignantsEnseignementsPK.
     *
     * @return le EnseignantsEnseignementsPK.
     */
    public EnseignantsEnseignementsPK getEnseignantsEnseignementsPK() {
        return pk;
    }

    /**
     * Mutateur EnseignantsEnseignementsPK.
     *
     * @param pk le EnseignantsEnseignementsPK à modifier.
     */
    public void setEnseignantsEnseignementsPK(EnseignantsEnseignementsPK pk) {
        this.pk = pk;
    }

    /**
     * Accesseur enseignant.
     *
     * @return l'enseignant.
     */
    public EnseignantBean getEnseignant() {
        return enseignant;
    }

    /**
     * Mutateur enseignant.
     *
     * @param enseignant l'enseignant à modifier.
     */
    public void setEnseignant(EnseignantBean enseignant) {
        this.enseignant = enseignant;
    }

    /**
     * Accesseur Enseignement.
     *
     * @return l'enseignement.
     */
    public EnseignementBean getEnseignement() {
        return enseignement;
    }

    /**
     * Mutateur enseignement.
     *
     * @param enseignement l'enseignement à modifier.
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
     * @param idEnseignant id de l'enseignant.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        if (this.pk == null) {
            this.pk = new EnseignantsEnseignementsPK();
        }
        this.pk.idEnseignant = idEnseignant;
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
     * idEtablissement.
     *
     * @param idEtablissement id de l'établissement.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        if (this.pk == null) {
            this.pk = new EnseignantsEnseignementsPK();
        }
        this.pk.idEtablissement = idEtablissement;
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
     * idEnseignement.
     *
     * @param idEnseignement id de l'enseignement.
     */
    public void setIdEnseignement(Integer idEnseignement) {
        if (this.pk == null) {
            this.pk = new EnseignantsEnseignementsPK();
        }
        this.pk.idEnseignement = idEnseignement;
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
        str.append("pk=" + getIdEnseignement() + ", ");

        str.append("{[EnseignantsEnseignements]");
        return (str.toString());
    }
}
