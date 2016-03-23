/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EtablissementComplementDTO.java,v 1.1 2010/03/29 09:29:35 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * EtablissementComplementDTO.
 *
 * @author breytond.
 * @version $Revision: 1.1 $
  */
public class EtablissementComplementDTO implements Serializable {
    /** Seiral UID. */   
    private static final long serialVersionUID = 5866366359751879407L;

    /** Plage horaire des cours d'une journée. */
    private String horaireCours;
    
    /** Durée classique d'un cours. */
    private Integer dureeCours;
    
    /** Heure de début du premier cours. */
    private Integer heureDebutCours;  
    
    /** Minute de début du premier cours. */
    private Integer minuteDebutCours;  
      
    /** Heure de fin du dernier cours. */
    private Integer heureFinCours;    
     
    /** Rapport de fractionnement des horaires (détermine le pas dans la constitution d'un emploi du temps) . */
    private Integer fractionnement;

    /**
     * Accesseur horaireCours.
     * @return le horaireCours
     */
    public String getHoraireCours() {
        return horaireCours;
    }

    /**
     * Mutateur de horaireCours.
     * @param horaireCours le horaireCours à modifier.
     */
    public void setHoraireCours(String horaireCours) {
        this.horaireCours = horaireCours;
    }

    /**
     * Accesseur dureeCours.
     * @return le dureeCours
     */
    public Integer getDureeCours() {
        return dureeCours;
    }

    /**
     * Mutateur de dureeCours.
     * @param dureeCours le dureeCours à modifier.
     */
    public void setDureeCours(Integer dureeCours) {
        this.dureeCours = dureeCours;
    }

    /**
     * Accesseur heureDebutCours.
     * @return le heureDebutCours
     */
    public Integer getHeureDebutCours() {
        return heureDebutCours;
    }

    /**
     * Mutateur de heureDebutCours.
     * @param heureDebutCours le heureDebutCours à modifier.
     */
    public void setHeureDebutCours(Integer heureDebutCours) {
        this.heureDebutCours = heureDebutCours;
    }

    /**
     * Accesseur minuteDebutCours.
     * @return le minuteDebutCours
     */
    public Integer getMinuteDebutCours() {
        return minuteDebutCours;
    }

    /**
     * Mutateur de minuteDebutCours.
     * @param minuteDebutCours le minuteDebutCours à modifier.
     */
    public void setMinuteDebutCours(Integer minuteDebutCours) {
        this.minuteDebutCours = minuteDebutCours;
    }

    /**
     * Accesseur heureFinCours.
     * @return le heureFinCours
     */
    public Integer getHeureFinCours() {
        return heureFinCours;
    }

    /**
     * Mutateur de heureFinCours.
     * @param heureFinCours le heureFinCours à modifier.
     */
    public void setHeureFinCours(Integer heureFinCours) {
        this.heureFinCours = heureFinCours;
    }

    /**
     * Accesseur de fractionnement {@link #fractionnement}.
     * @return retourne fractionnement
     */
    public Integer getFractionnement() {
        return fractionnement;
    }

    /**
     * Mutateur de fractionnement {@link #fractionnement}.
     * @param fractionnement le fractionnement to set
     */
    public void setFractionnement(Integer fractionnement) {
        this.fractionnement = fractionnement;
    }
    
}
