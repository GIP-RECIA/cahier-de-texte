/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DateAnneeScolaireQO.java,v 1.1 2010/03/29 09:29:35 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

import java.util.Date;

/**
 * DateAnneeScolaireQO.
 *
 * @author breytond.
 * @version $Revision: 1.1 $
  */
public class DateAnneeScolaireQO implements Serializable {
    /** Serial UID. */
    private static final long serialVersionUID = -6236127360926292999L;

    /** id. */
    private Integer id;

    /** date de rentrée. */
    private Date dateRentree;

    /** date de sortie. */
    private Date dateSortie;       

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
}
