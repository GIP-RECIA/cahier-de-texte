/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EtablissementsEnseignantsBean.java,v 1.4 2010/04/12 01:34:33 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import javax.persistence.Column;
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
 * @author breytond
 * @version $Revision: 1.4 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_etab_enseignant", schema="cahier_courant")
public class EtablissementsEnseignantsBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private EtablissementsEnseignantsPK pk;
    
    @Column(name = "saisie_simplifiee", nullable = true)    
    private Boolean vraiOuFauxSaisieSimplifiee;
    

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

/**
     * Constructeur.
     */
    public EtablissementsEnseignantsBean() {
    }

    /**
     * Accesseur etablissementsEnseignantsPK.
     *
     * @return le etablissementsEnseignantsPK.
     */
    public EtablissementsEnseignantsPK getEtablissementsEnseignantsPK() {
        return pk;
    }

    /**
     * Mutateur etablissementsEnseignantsPK.
     *
     * @param pk le etablissementsEnseignantsPK à modifier.
     */
    public void setEtablissementsEnseignantsPK(EtablissementsEnseignantsPK pk) {
        this.pk = pk;
    }

    /**
     * Accesseur etablissement.
     *
     * @return l' etablissement.
     */
    public EtablissementBean getEtablissement() {
        return etablissement;
    }

    /**
     * Mutateur etablissement.
     *
     * @param etablissement l'etablissement à modifier.
     */
    public void setEtablissement(EtablissementBean etablissement) {
        this.etablissement = etablissement;
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
     * @param idEtablissement id de l'etablissement.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        if (this.pk == null) {
            this.pk = new EtablissementsEnseignantsPK();
        }
        this.pk.idEtablissement = idEtablissement;
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
            this.pk = new EtablissementsEnseignantsPK();
        }
        this.pk.idEnseignant = idEnseignant;
    }

    /**
     * ToString.
     *
     * @return chaîne.
     */
    public String toString() {
        final StringBuffer str = new StringBuffer("{");

        str.append("{[AvisAptitudeAptitudeGroupeCritere], ");
        str.append("pk=" + getIdEtablissement() + ", ");
        str.append("pk=" + getIdEnseignant() + ", ");

        str.append("{[EtablissementsEnseignants]");
        return (str.toString());
    }

    /**
     * Accesseur vraiOuFauxSaisieSimplifiee.
     * @return le vraiOuFauxSaisieSimplifiee
     */
    public Boolean getVraiOuFauxSaisieSimplifiee() {
        return vraiOuFauxSaisieSimplifiee;
    }

    /**
     * Mutateur de vraiOuFauxSaisieSimplifiee.
     * @param vraiOuFauxSaisieSimplifiee le vraiOuFauxSaisieSimplifiee à modifier.
     */
    public void setVraiOuFauxSaisieSimplifiee(Boolean vraiOuFauxSaisieSimplifiee) {
        this.vraiOuFauxSaisieSimplifiee = vraiOuFauxSaisieSimplifiee;
    }
    
   
}