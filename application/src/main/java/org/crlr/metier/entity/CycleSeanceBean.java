/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 * Seance d'une sequence pedagogique (carnet de bord)
 * $Id: CycleSeanceBean.java,v 1.6 2009/04/28 12:45:50 ent_breyton Exp $
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
 * @version $Revision: 1.6 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_cycle_seance", schema="cahier_courant")
public class CycleSeanceBean {
    
    /** Identifiant. */
    @Id
    private Integer id;

    /** Id du cycle. */
    @Column(name = "id_cycle", nullable = false)
    private Integer idCycle;

    /** Enseignant createur de la seance pedagogique. */
    @Column(name = "id_enseignant", nullable = false)
    private Integer idEnseignant;

    /** Uid de l'enseignant createur. */
    @Column(name = "uid_enseignant", nullable = false)
    private String uidEnseignant;
    
    /** Libelle de l'enseignement. */
    @Column(name = "enseignement", nullable = true)
    private String enseignement;

    /** Intitule. */
    @Column(name = "intitule", nullable = true)
    private String intitule;
    
    /** Objectif. */
    @Column(name = "objectif", nullable = true)
    private String objectif;
    
    /** Description. */
    @Column(name = "description", nullable = true)
    private String description;
    
    /** Annotations personnelles. */
    @Column(name = "annotations", nullable = true)
    private String annotations;
    
    /** Annotations visible ? */
    @Column(name = "annotations_visible", nullable = true)
    private Boolean annotationsVisible;

    /** Indice. */
    @Column(name = "indice", nullable = false)
    private Integer indice;
    
    /** Objet Cycle.  */
    @ManyToOne(cascade =  { } )
    @JoinColumns(
            {
                @JoinColumn(name = "id_cycle", referencedColumnName = "id", insertable = false, updatable = false)
            }
    )
    private CycleBean cycle;
    
    /** Objet Enseignant.  */
    @ManyToOne(cascade =  { } )
    @JoinColumns(
            {
                @JoinColumn(name = "id_enseignant", referencedColumnName = "id", insertable = false, updatable = false)
            }
    )
    private EnseignantBean enseignant;
    
    /**
     * Constructeur.
     */
    public CycleSeanceBean() {
        
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
     * Accesseur de idCycle {@link #idCycle}.
     * @return retourne idCycle
     */
    public Integer getIdCycle() {
        return idCycle;
    }

    /**
     * Mutateur de idCycle {@link #idCycle}.
     * @param idCycle le idCycle to set
     */
    public void setIdCycle(Integer idCycle) {
        this.idCycle = idCycle;
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
     * Accesseur de enseignement {@link #enseignement}.
     * @return retourne enseignement
     */
    public String getEnseignement() {
        return enseignement;
    }

    /**
     * Mutateur de enseignement {@link #enseignement}.
     * @param enseignement le enseignement to set
     */
    public void setEnseignement(String enseignement) {
        this.enseignement = enseignement;
    }

    /**
     * Accesseur de intitule {@link #intitule}.
     * @return retourne intitule
     */
    public String getIntitule() {
        return intitule;
    }

    /**
     * Mutateur de intitule {@link #intitule}.
     * @param intitule le intitule to set
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
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
     * Accesseur de annotations {@link #annotations}.
     * @return retourne annotations
     */
    public String getAnnotations() {
        return annotations;
    }

    /**
     * Mutateur de annotations {@link #annotations}.
     * @param annotations le annotations to set
     */
    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }

    /**
     * Accesseur de annotationsVisible {@link #annotationsVisible}.
     * @return retourne annotationsVisible
     */
    public Boolean getAnnotationsVisible() {
        return annotationsVisible;
    }

    /**
     * Mutateur de annotationsVisible {@link #annotationsVisible}.
     * @param annotationsVisible le annotationsVisible to set
     */
    public void setAnnotationsVisible(Boolean annotationsVisible) {
        this.annotationsVisible = annotationsVisible;
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

    /**
     * Accesseur de cycle {@link #cycle}.
     * @return retourne cycle
     */
    public CycleBean getCycle() {
        return cycle;
    }

    /**
     * Mutateur de cycle {@link #cycle}.
     * @param cycle le cycle to set
     */
    public void setCycle(CycleBean cycle) {
        this.cycle = cycle;
    }

    /**
     * Accesseur de indice {@link #indice}.
     * @return retourne indice
     */
    public Integer getIndice() {
        return indice;
    }

    /**
     * Mutateur de indice {@link #indice}.
     * @param indice le indice to set
     */
    public void setIndice(Integer indice) {
        this.indice = indice;
    }


    
}
