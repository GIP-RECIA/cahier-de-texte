/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GroupeBean.java,v 1.7 2009/04/28 12:45:50 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
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
 * @version $Revision: 1.7 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_groupe", schema="cahier_courant")
public class GroupeBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private Integer id;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "code", nullable = false)
    private String code;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "designation", nullable = false)
    private String designation;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "id_annee_scolaire", nullable = false)
    private Integer idAnneeScolaire;

    /** Indique s'il s'agit d'un groupe collaboratif ou scolaire. */
    @Column(name = "groupe_collaboratif", nullable = true)
    private Boolean groupeCollaboratif;
    
    /** Indique s'il s'agit d'un groupe collaboratif local ou non. 
     * 
     * ne doit être vrais que si groupe_collaboratif est vrai.
     * */
    @Column(name = "groupe_collaboratif_local", nullable = true)
    private Boolean groupeCollaboratifLocal;
    
    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_annee_scolaire", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private AnneeScolaireBean anneeScolaire;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "id_etablissement", nullable = false)
    private Integer idEtablissement;

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
    public GroupeBean() {
    }

    /**
     * Accesseur id.
     *
     * @return le id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur id.
     *
     * @param id le id à modifier.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur code.
     *
     * @return le code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Mutateur code.
     *
     * @param code le code à modifier.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Accesseur designation.
     *
     * @return le designation.
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Mutateur designation.
     *
     * @param designation le designation à modifier.
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * Accesseur idAnneeScolaire.
     *
     * @return le idAnneeScolaire.
     */
    public Integer getIdAnneeScolaire() {
        return idAnneeScolaire;
    }

    /**
     * Mutateur idAnneeScolaire.
     *
     * @param idAnneeScolaire le idAnneeScolaire à modifier.
     */
    public void setIdAnneeScolaire(Integer idAnneeScolaire) {
        this.idAnneeScolaire = idAnneeScolaire;
    }

    /**
     * Accesseur anneeScolaire.
     *
     * @return le anneeScolaire.
     */
    public AnneeScolaireBean getAnneeScolaire() {
        return anneeScolaire;
    }

    /**
     * Mutateur anneeScolaire.
     *
     * @param anneeScolaire le anneeScolaire à modifier.
     */
    public void setAnneeScolaire(AnneeScolaireBean anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    /**
     * Accesseur idEtablissement.
     *
     * @return le idEtablissement.
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur idEtablissement.
     *
     * @param idEtablissement le idEtablissement à modifier.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
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
     * Accesseur de groupeCollaboratif {@link #groupeCollaboratif}.
     * @return retourne groupeCollaboratif
     */
    public Boolean getGroupeCollaboratif() {
        return groupeCollaboratif;
    }

    /**
     * Mutateur de groupeCollaboratif {@link #groupeCollaboratif}.
     * @param groupeCollaboratif le groupeCollaboratif to set
     */
    public void setGroupeCollaboratif(Boolean groupeCollaboratif) {
        this.groupeCollaboratif = groupeCollaboratif;
    }

	public Boolean getGroupeCollaboratifLocal() {
		return groupeCollaboratifLocal;
	}

	public void setGroupeCollaboratifLocal(Boolean groupeCollaboratifLocal) {
		this.groupeCollaboratifLocal = groupeCollaboratifLocal;
	}

    
}
