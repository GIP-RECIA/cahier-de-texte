/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DatePeriodeQO.java,v 1.1 2010/03/31 08:08:44 ent_breyton Exp $
 */

package org.crlr.dto.application.admin;

import java.io.Serializable;
import java.util.Date;

/**
 * DatePeriodeQO.
 *
 * @author breytond.
 * @version $Revision: 1.1 $
  */
public class DatePeriodeQO implements Serializable {
    /** SERIAL UID. */
    private static final long serialVersionUID = -8778272689354206847L;
    
    private Date dateRentree;
    
    private Date dateSortie;
    
    private Date dateDebutPlage;
    
    private Date dateFinPlage;
    
    private String plageExistante;

    /**
     * Accesseur dateRentree.
     * @return le dateRentree
     */
    public Date getDateRentree() {
        return dateRentree;
    }

    /**
     * Mutateur de dateRentree.
     * @param dateRentree le dateRentree à modifier.
     */
    public void setDateRentree(Date dateRentree) {
        this.dateRentree = dateRentree;
    }

    /**
     * Accesseur dateSortie.
     * @return le dateSortie
     */
    public Date getDateSortie() {
        return dateSortie;
    }

    /**
     * Mutateur de dateSortie.
     * @param dateSortie le dateSortie à modifier.
     */
    public void setDateSortie(Date dateSortie) {
        this.dateSortie = dateSortie;
    }

    /**
     * Accesseur dateDebutPlage.
     * @return le dateDebutPlage
     */
    public Date getDateDebutPlage() {
        return dateDebutPlage;
    }

    /**
     * Mutateur de dateDebutPlage.
     * @param dateDebutPlage le dateDebutPlage à modifier.
     */
    public void setDateDebutPlage(Date dateDebutPlage) {
        this.dateDebutPlage = dateDebutPlage;
    }

    /**
     * Accesseur dateFinPlage.
     * @return le dateFinPlage
     */
    public Date getDateFinPlage() {
        return dateFinPlage;
    }

    /**
     * Mutateur de dateFinPlage.
     * @param dateFinPlage le dateFinPlage à modifier.
     */
    public void setDateFinPlage(Date dateFinPlage) {
        this.dateFinPlage = dateFinPlage;
    }

    /**
     * Accesseur plageExistante.
     * @return le plageExistante
     */
    public String getPlageExistante() {
        return plageExistante;
    }

    /**
     * Mutateur de plageExistante.
     * @param plageExistante le plageExistante à modifier.
     */
    public void setPlageExistante(String plageExistante) {
        this.plageExistante = plageExistante;
    }
}
