/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: MenuNavigationItem.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.application;

import java.io.Serializable;
import java.util.Date;

import org.crlr.dto.Outil;

/**
 * Item dans la file de navigation.
 *
 * @author breytond
 */
public class MenuNavigationItem implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Date d'utilisation de l'outil. */
    private Date date;

    /** Outil utilisé. */
    private Outil outil;

    /** Outcome fourni pour y parvenir. */
    private String outcome;

/**
     * The Constructor.
     */
    public MenuNavigationItem() {
        this.date = new Date();
    }

    /**
     * Retourne outil.
     *
     * @return the outil
     */
    public Outil getOutil() {
        return this.outil;
    }

    /**
     * Affecte outil.
     *
     * @param outil the outil
     */
    public void setOutil(Outil outil) {
        this.outil = outil;
    }

    /**
     * Retourne date.
     *
     * @return the date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Affecte date.
     *
     * @param date the date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Retourne outcome.
     *
     * @return the outcome
     */
    public String getOutcome() {
        return this.outcome;
    }

    /**
     * Affecte outcome.
     *
     * @param outcome the outcome
     */
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    /**
     * 
    DOCUMENT ME!
     *
     * @param obj paramat�re
     *
     * @return valeur
     */
    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof MenuNavigationItem)) {
            return false;
        }

        //return toString().equals(obj.toString());
        final MenuNavigationItem item = (MenuNavigationItem) obj;
        return item.getOutil().name().equals(this.getOutil().name());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    /**
     * 
    DOCUMENT ME!
     *
     * @return valeur
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    /**
     * 
    DOCUMENT ME!
     *
     * @return valeur
     */
    @Override
    public String toString() {
         return "MenuNavigationItem[outil=" + this.outil + ", date=" + this.date +
               ", outcome=" + this.outcome + "]";
    }
}
