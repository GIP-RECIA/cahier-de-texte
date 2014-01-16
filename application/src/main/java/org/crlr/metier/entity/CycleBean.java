/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 * Sequence pedagogique (carnet de bord)
 * $Id: Cycle.java,v 1.7 2009/04/28 12:45:50 ent_breyton Exp $
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
 * @author vibertd
 * @version $Revision: 1.7 $
 */
@Proxy(lazy = true)
@Entity
@Table(name = "cahier_cycle", schema="cahier_courant")
public class CycleBean {
    /** Identfiant. */
    @Id
    private Integer id;

    /** Enseignant createur de la sequence pedagogique. */
    @Column(name = "id_enseignant", nullable = false)
    private Integer idEnseignant;

    /** Uid de l'enseignant createur. */
    @Column(name = "uid_enseignant", nullable = false)
    private String uidEnseignant;

    /** Titre de sequence pedagogique. */
    @Column(name = "titre", nullable = false)
    private String titre;

    /** Objectif. */
    @Column(name = "objectif", nullable = true)
    private String objectif;

    /** Pre-requis. */
    @Column(name = "prerequis", nullable = true)
    private String prerequis;

    /** Description. */
    @Column(name = "description", nullable = true)
    private String description;

    /** Foreign key vers la table enseignant. */
    @ManyToOne(cascade =  {} )
    @JoinColumns(
            {
                @JoinColumn(name = "id_enseignant", referencedColumnName = "id", insertable = false, updatable = false)
            }
    )
    private EnseignantBean enseignant;

    /**
     * Constructeur.
     */
    public CycleBean() {
    }

    /**
     * Accesseur de id {@link #id}.
     * @return retourne id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur de id {@link #id}.
     * @param id le id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur de idEnseignant {@link #idEnseignant}.
     * @return retourne idEnseignant
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur de idEnseignant {@link #idEnseignant}.
     * @param idEnseignant le idEnseignant to set
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur de uidEnseignant {@link #uidEnseignant}.
     * @return retourne uidEnseignant
     */
    public String getUidEnseignant() {
        return uidEnseignant;
    }

    /**
     * Mutateur de uidEnseignant {@link #uidEnseignant}.
     * @param uidEnseignant le uidEnseignant to set
     */
    public void setUidEnseignant(String uidEnseignant) {
        this.uidEnseignant = uidEnseignant;
    }

    /**
     * Accesseur de titre {@link #titre}.
     * @return retourne titre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Mutateur de titre {@link #titre}.
     * @param titre le titre to set
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Accesseur de objectif {@link #objectif}.
     * @return retourne objectif
     */
    public String getObjectif() {
        return objectif;
    }

    /**
     * Mutateur de objectif {@link #objectif}.
     * @param objectif le objectif to set
     */
    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    /**
     * Accesseur de prerequis {@link #prerequis}.
     * @return retourne prerequis
     */
    public String getPrerequis() {
        return prerequis;
    }

    /**
     * Mutateur de prerequis {@link #prerequis}.
     * @param prerequis le prerequis to set
     */
    public void setPrerequis(String prerequis) {
        this.prerequis = prerequis;
    }

    /**
     * Accesseur de description {@link #description}.
     * @return retourne description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutateur de description {@link #description}.
     * @param description le description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Accesseur de enseignant {@link #enseignant}.
     * @return retourne enseignant
     */
    public EnseignantBean getEnseignant() {
        return enseignant;
    }

    /**
     * Mutateur de enseignant {@link #enseignant}.
     * @param enseignant le enseignant to set
     */
    public void setEnseignant(EnseignantBean enseignant) {
        this.enseignant = enseignant;
    }


    
}
