/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnteteEmploiJoursDTO.java,v 1.1 2010/04/15 14:00:30 ent_breyton Exp $
 */

package org.crlr.web.dto;

import java.io.Serializable;

import java.util.Date;

/**
 * EnteteEmploiJoursDTO.
 *
 * @author breytond
 * @version $Revision: 1.1 $
 */
public class EnteteEmploiJoursDTO implements Serializable {
    /** Serail UID. */
    private static final long serialVersionUID = -6203227489794048777L;

    /** jour actif c'est à dire non vaqué. */
    private Boolean vraiOuFauxActif;

    /** date du jour de la semaine. */
    private Date date;

    /** date du jour de la semaine formatee. */
    private String dateFormatee;

    /**
     * Constructeur.
     * @param vraiOuFauxActif actif oui non.
     * @param date la date du jour de la semaine.
     * @param dateFormatee la date du jour de la semaine formatée.
     */
    public EnteteEmploiJoursDTO(final Boolean vraiOuFauxActif, final Date date,
                                final String dateFormatee) {
        this.vraiOuFauxActif = vraiOuFauxActif;
        this.date = date;
        this.dateFormatee = dateFormatee;
    }

    /**
     * Accesseur vraiOuFauxActif.
     *
     * @return le vraiOuFauxActif
     */
    public Boolean getVraiOuFauxActif() {
        return vraiOuFauxActif;
    }

    /**
     * Mutateur de vraiOuFauxActif.
     *
     * @param vraiOuFauxActif le vraiOuFauxActif à modifier.
     */
    public void setVraiOuFauxActif(Boolean vraiOuFauxActif) {
        this.vraiOuFauxActif = vraiOuFauxActif;
    }

    /**
     * Accesseur date.
     *
     * @return le date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Mutateur de date.
     *
     * @param date le date à modifier.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Accesseur dateFormatee.
     *
     * @return le dateFormatee
     */
    public String getDateFormatee() {
        return dateFormatee;
    }

    /**
     * Mutateur de dateFormatee.
     *
     * @param dateFormatee le dateFormatee à modifier.
     */
    public void setDateFormatee(String dateFormatee) {
        this.dateFormatee = dateFormatee;
    }
}
