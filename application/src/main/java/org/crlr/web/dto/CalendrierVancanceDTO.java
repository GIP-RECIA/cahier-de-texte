/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: CalendrierVancanceDTO.java,v 1.1 2010/03/29 09:29:36 ent_breyton Exp $
 */

package org.crlr.web.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * CalendrierVancanceDTO.
 *
 * @author breytond.
 * @version $Revision: 1.1 $
 */
public class CalendrierVancanceDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = -5806342643365229200L;

    /** jour de la semaine. */
    private Date jourLundi;

    /** jour de la semaine. */
    private Date jourMardi;

    /** jour de la semaine. */
    private Date jourMercredi;

    /** jour de la semaine. */
    private Date jourJeudi;

    /** jour de la semaine. */
    private Date jourVendredi;

    /** jour de la semaine. */
    private Date jourSamedi;

    /** jour de la semaine. */
    private Date jourDimanche;

    /** couleur white:ouvré, gray:chomé, red:férié . */
    private String couleurLundi;

    /** couleur white:ouvré, gray:chomé, red:férié . */
    private String couleurMardi;

    /** couleur white:ouvré, gray:chomé, red:férié . */
    private String couleurMercredi;

    /** couleur white:ouvré, gray:chomé, red:férié . */
    private String couleurJeudi;

    /** couleur white:ouvré, gray:chomé, red:férié . */
    private String couleurVendredi;

    /** couleur white:ouvré, gray:chomé, red:férié . */
    private String couleurSamedi;
    
    /** couleur white:ouvré, gray:chomé, red:férié . */
    private String couleurDimanche;

    /**
     * Accesseur jourLundi.
     *
     * @return le jourLundi
     */
    public Date getJourLundi() {
        return jourLundi;
    }

    /**
     * Mutateur de jourLundi.
     *
     * @param jourLundi le jourLundi à modifier.
     */
    public void setJourLundi(Date jourLundi) {
        this.jourLundi = jourLundi;
    }

    /**
     * Accesseur jourMardi.
     *
     * @return le jourMardi
     */
    public Date getJourMardi() {
        return jourMardi;
    }

    /**
     * Mutateur de jourMardi.
     *
     * @param jourMardi le jourMardi à modifier.
     */
    public void setJourMardi(Date jourMardi) {
        this.jourMardi = jourMardi;
    }

    /**
     * Accesseur jourMercredi.
     *
     * @return le jourMercredi
     */
    public Date getJourMercredi() {
        return jourMercredi;
    }

    /**
     * Mutateur de jourMercredi.
     *
     * @param jourMercredi le jourMercredi à modifier.
     */
    public void setJourMercredi(Date jourMercredi) {
        this.jourMercredi = jourMercredi;
    }

    /**
     * Accesseur jourJeudi.
     *
     * @return le jourJeudi
     */
    public Date getJourJeudi() {
        return jourJeudi;
    }

    /**
     * Mutateur de jourJeudi.
     *
     * @param jourJeudi le jourJeudi à modifier.
     */
    public void setJourJeudi(Date jourJeudi) {
        this.jourJeudi = jourJeudi;
    }

    /**
     * Accesseur jourVendredi.
     *
     * @return le jourVendredi
     */
    public Date getJourVendredi() {
        return jourVendredi;
    }

    /**
     * Mutateur de jourVendredi.
     *
     * @param jourVendredi le jourVendredi à modifier.
     */
    public void setJourVendredi(Date jourVendredi) {
        this.jourVendredi = jourVendredi;
    }

    /**
     * Accesseur jourSamedi.
     *
     * @return le jourSamedi
     */
    public Date getJourSamedi() {
        return jourSamedi;
    }

    /**
     * Mutateur de jourSamedi.
     *
     * @param jourSamedi le jourSamedi à modifier.
     */
    public void setJourSamedi(Date jourSamedi) {
        this.jourSamedi = jourSamedi;
    }

    /**
     * Accesseur jourDimanche.
     *
     * @return le jourDimanche
     */
    public Date getJourDimanche() {
        return jourDimanche;
    }

    /**
     * Mutateur de jourDimanche.
     *
     * @param jourDimanche le jourDimanche à modifier.
     */
    public void setJourDimanche(Date jourDimanche) {
        this.jourDimanche = jourDimanche;
    }

    /**
     * Accesseur couleurLundi.
     *
     * @return le couleurLundi
     */
    public String getCouleurLundi() {
        return couleurLundi;
    }

    /**
     * Mutateur de couleurLundi.
     *
     * @param couleurLundi le couleurLundi à modifier.
     */
    public void setCouleurLundi(String couleurLundi) {
        this.couleurLundi = couleurLundi;
    }

    /**
     * Accesseur couleurMardi.
     *
     * @return le couleurMardi
     */
    public String getCouleurMardi() {
        return couleurMardi;
    }

    /**
     * Mutateur de couleurMardi.
     *
     * @param couleurMardi le couleurMardi à modifier.
     */
    public void setCouleurMardi(String couleurMardi) {
        this.couleurMardi = couleurMardi;
    }

    /**
     * Accesseur couleurMercredi.
     *
     * @return le couleurMercredi
     */
    public String getCouleurMercredi() {
        return couleurMercredi;
    }

    /**
     * Mutateur de couleurMercredi.
     *
     * @param couleurMercredi le couleurMercredi à modifier.
     */
    public void setCouleurMercredi(String couleurMercredi) {
        this.couleurMercredi = couleurMercredi;
    }

    /**
     * Accesseur couleurJeudi.
     *
     * @return le couleurJeudi
     */
    public String getCouleurJeudi() {
        return couleurJeudi;
    }

    /**
     * Mutateur de couleurJeudi.
     *
     * @param couleurJeudi le couleurJeudi à modifier.
     */
    public void setCouleurJeudi(String couleurJeudi) {
        this.couleurJeudi = couleurJeudi;
    }

    /**
     * Accesseur couleurVendredi.
     *
     * @return le couleurVendredi
     */
    public String getCouleurVendredi() {
        return couleurVendredi;
    }

    /**
     * Mutateur de couleurVendredi.
     *
     * @param couleurVendredi le couleurVendredi à modifier.
     */
    public void setCouleurVendredi(String couleurVendredi) {
        this.couleurVendredi = couleurVendredi;
    }

    /**
     * Accesseur couleurSamedi.
     *
     * @return le couleurSamedi
     */
    public String getCouleurSamedi() {
        return couleurSamedi;
    }

    /**
     * Mutateur de couleurSamedi.
     *
     * @param couleurSamedi le couleurSamedi à modifier.
     */
    public void setCouleurSamedi(String couleurSamedi) {
        this.couleurSamedi = couleurSamedi;
    }

    /**
     * Accesseur couleurDimanche.
     * @return le couleurDimanche
     */
    public String getCouleurDimanche() {
        return couleurDimanche;
    }

    /**
     * Mutateur de couleurDimanche.
     * @param couleurDimanche le couleurDimanche à modifier.
     */
    public void setCouleurDimanche(String couleurDimanche) {
        this.couleurDimanche = couleurDimanche;
    }
}
