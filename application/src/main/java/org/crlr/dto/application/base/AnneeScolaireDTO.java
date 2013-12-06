/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AnneeScolaireDTO.java,v 1.3 2010/03/29 09:29:35 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

import java.util.Date;

/**
 * AnneeScolaireDTO.
 *
 * @author breytond.
 * @version $Revision: 1.3 $
  */
public class AnneeScolaireDTO implements Serializable {
    /** Serial UID. */
    private static final long serialVersionUID = -6236127360926292999L;

    /** id. */
    private Integer id;

    /** date de rentrée. */
    private Date dateRentree;

    /** date de sortie. */
    private Date dateSortie;
    
    /** les périodes de vacances. */
    private String periodeVacances;
    
    /** les périodes scolaires. */
    private String periodeScolaires;
    
    /** Cahier de texte ouvert aux établissements. */
    private Boolean vraiOuFauxCahierOuvertENT;
    
    /**
     * Exercice.
     */
    private String exercice;

    /**
     * Accesseur.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur.
     *
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur.
     *
     * @return the dateRentree
     */
    public Date getDateRentree() {
        return dateRentree;
    }

    /**
     * Mutateur.
     *
     * @param dateRentree the dateRentree to set
     */
    public void setDateRentree(Date dateRentree) {
        this.dateRentree = dateRentree;
    }

    /**
     * Accesseur.
     *
     * @return the dateSortie
     */
    public Date getDateSortie() {
        return dateSortie;
    }

    /**
     * Mutateur.
     *
     * @param dateSortie the dateSortie to set
     */
    public void setDateSortie(Date dateSortie) {
        this.dateSortie = dateSortie;
    }

    /**
     * Accesseur exercice.
     * @return exercice
     */
    public String getExercice() {
        return exercice;
    }

    /**
     * Mutateur exercice.
     * @param exercice Le exercice à modifier
     */
    public void setExercice(String exercice) {
        this.exercice = exercice;
    }

    /**
     * Accesseur periodeVacances.
     * @return le periodeVacances
     */
    public String getPeriodeVacances() {
        return periodeVacances;
    }

    /**
     * Mutateur de periodeVacances.
     * @param periodeVacances le periodeVacances à modifier.
     */
    public void setPeriodeVacances(String periodeVacances) {
        this.periodeVacances = periodeVacances;
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
     * Accesseur de periodeScolaires.
     * @return le periodeScolaires
     */
    public String getPeriodeScolaires() {
        return periodeScolaires;
    }

    /**
     * Mutateur de periodeScolaires.
     * @param periodeScolaires le periodeScolaires à modifier.
     */
    public void setPeriodeScolaires(String periodeScolaires) {
        this.periodeScolaires = periodeScolaires;
    }
    
    
    
}
