/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AnneeScolaireBean.java,v 1.5 2010/03/29 09:29:36 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author vibertd
 * @version $Revision: 1.5 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_annee_scolaire", schema="cahier_courant")
public class AnneeScolaireBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private Integer id;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "exercice", nullable = false)
    private String exercice;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "date_rentree", nullable = false)
    private Date dateRentree;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "date_sortie", nullable = false)
    private Date dateSortie;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "periode_vacance", nullable = true)
    private String periodeVacance;
    
    @Column(name = "ouverture_ent", nullable = true)
    private Boolean vraiOuFauxCahierOuvertENT;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "periode_scolaire", nullable = true)
    private String periodeScolaire;

/**
     * Constructeur.
     */
    public AnneeScolaireBean() {
    }

    /**
     * Retourne l'id.
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Positionne l'id.
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur exercice.
     *
     * @return l'exercice.
     */
    public String getExercice() {
        return exercice;
    }

    /**
     * Mutateur exercice.
     *
     * @param exercice l'exercice à modifier.
     */
    public void setExercice(String exercice) {
        this.exercice = exercice;
    }

    /**
     * Accesseur dateRentree.
     *
     * @return la date de rentrée.
     */
    public java.util.Date getDateRentree() {
        return dateRentree;
    }

    /**
     * Mutateur dateRentree.
     *
     * @param dateRentree la date de rentrée à modifier.
     */
    public void setDateRentree(java.util.Date dateRentree) {
        this.dateRentree = dateRentree;
    }

    /**
     * Accesseur dateSortie.
     *
     * @return la date de sortie.
     */
    public java.util.Date getDateSortie() {
        return dateSortie;
    }

    /**
     * Mutateur dateSortie.
     *
     * @param dateSortie la date de sortie à modifier.
     */
    public void setDateSortie(java.util.Date dateSortie) {
        this.dateSortie = dateSortie;
    }

    /**
     * Accesseur periodeVacance.
     * @return le periodeVacance
     */
    public String getPeriodeVacance() {
        return periodeVacance;
    }

    /**
     * Mutateur de periodeVacance.
     * @param periodeVacance le periodeVacance à modifier.
     */
    public void setPeriodeVacance(String periodeVacance) {
        this.periodeVacance = periodeVacance;
    }

    /**
     * Accesseur vraiOuFauxCahierOuvertENT.
     * @return le vraiOuFauxCahierOuvertENT
     */
    public Boolean getVraiOuFauxCahierOuvertENT() {
        return vraiOuFauxCahierOuvertENT;
    }

    /**
     * Mutateur de vraiOuFauxCahierOuvertENT.
     * @param vraiOuFauxCahierOuvertENT le vraiOuFauxCahierOuvertENT à modifier.
     */
    public void setVraiOuFauxCahierOuvertENT(Boolean vraiOuFauxCahierOuvertENT) {
        this.vraiOuFauxCahierOuvertENT = vraiOuFauxCahierOuvertENT;
    }

    /**
     * Accesseur de periodeScolaire.
     * @return le periodeScolaire
     */
    public String getPeriodeScolaire() {
        return periodeScolaire;
    }

    /**
     * Mutateur de periodeScolaire.
     * @param periodeScolaire le periodeScolaire à modifier.
     */
    public void setPeriodeScolaire(String periodeScolaire) {
        this.periodeScolaire = periodeScolaire;
    }
    
    
}
