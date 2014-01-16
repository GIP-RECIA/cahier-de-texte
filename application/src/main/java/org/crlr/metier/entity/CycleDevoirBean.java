/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 * Devoir d'une Seance d'une sequence pedagogique (carnet de bord)
 * $Id: CycleDevoirBean.java,v 1.6 2009/04/28 12:45:50 ent_breyton Exp $
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
@Table(name = "cahier_cycle_devoir", schema="cahier_courant")
public class CycleDevoirBean {
    
    /** Identifiant. */
    @Id
    private Integer id;

    /** Id du cycle. */
    @Column(name = "id_cycle_seance", nullable = false)
    private Integer idCycleSeance;

    /** Intitule. */
    @Column(name = "intitule", nullable = true)
    private String intitule;
    
    /** Description. */
    @Column(name = "description", nullable = true)
    private String description;
    
    /** Date de remise : choix parmi un enum. */
    @Column(name = "date_remise", nullable = true)
    private Integer dateRemise;
    
    /** Type du devoir. */
    @Column(name = "id_type_devoir", nullable = true)
    private Integer idTypeDevoir;

    /** Objet CycleSeance.  */
    @ManyToOne(cascade =  { } )
    @JoinColumns(
            {
                @JoinColumn(name = "id_cycle_seance", referencedColumnName = "id", insertable = false, updatable = false)
            }
    )
    private CycleSeanceBean cycleSeance;
    
    /** Objet Type devoir.  */
    @ManyToOne(cascade =  { } )
    @JoinColumns(
            {
                @JoinColumn(name = "id_type_devoir", referencedColumnName = "id", insertable = false, updatable = false)
            }
    )
    private TypeDevoirBean typeDevoir;
    
    /**
     * Constructeur.
     */
    public CycleDevoirBean() {
        
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
     * Accesseur de idCycleSeance {@link #idCycleSeance}.
     * @return retourne idCycleSeance
     */
    public Integer getIdCycleSeance() {
        return idCycleSeance;
    }

    /**
     * Mutateur de idCycleSeance {@link #idCycleSeance}.
     * @param idCycleSeance le idCycleSeance to set
     */
    public void setIdCycleSeance(Integer idCycleSeance) {
        this.idCycleSeance = idCycleSeance;
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
     * Accesseur de dateRemise {@link #dateRemise}.
     * @return retourne dateRemise
     */
    public Integer getDateRemise() {
        return dateRemise;
    }

    /**
     * Mutateur de dateRemise {@link #dateRemise}.
     * @param dateRemise le dateRemise to set
     */
    public void setDateRemise(Integer dateRemise) {
        this.dateRemise = dateRemise;
    }

    /**
     * Accesseur de idTypeDevoir {@link #idTypeDevoir}.
     * @return retourne idTypeDevoir
     */
    public Integer getIdTypeDevoir() {
        return idTypeDevoir;
    }

    /**
     * Mutateur de idTypeDevoir {@link #idTypeDevoir}.
     * @param idTypeDevoir le idTypeDevoir to set
     */
    public void setIdTypeDevoir(Integer idTypeDevoir) {
        this.idTypeDevoir = idTypeDevoir;
    }


    /**
     * Accesseur de cycleSeance {@link #cycleSeance}.
     * @return retourne cycleSeance
     */
    public CycleSeanceBean getCycleSeance() {
        return cycleSeance;
    }

    /**
     * Mutateur de cycleSeance {@link #cycleSeance}.
     * @param cycleSeance le cycleSeance to set
     */
    public void setCycleSeance(CycleSeanceBean cycleSeance) {
        this.cycleSeance = cycleSeance;
    }

    /**
     * Accesseur de typeDevoir {@link #typeDevoir}.
     * @return retourne typeDevoir
     */
    public TypeDevoirBean getTypeDevoir() {
        return typeDevoir;
    }

    /**
     * Mutateur de typeDevoir {@link #typeDevoir}.
     * @param typeDevoir le typeDevoir to set
     */
    public void setTypeDevoir(TypeDevoirBean typeDevoir) {
        this.typeDevoir = typeDevoir;
    }
    
    

    
}
