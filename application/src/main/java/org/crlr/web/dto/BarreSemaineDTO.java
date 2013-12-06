/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: BarreSemaineDTO.java,v 1.2 2010/04/12 04:30:13 jerome.carriere Exp $
 */

package org.crlr.web.dto;

import java.awt.Color;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.crlr.utils.DateUtils;

/**
 * CalendrierVancanceDTO.
 *
 * @author breytond.
 * @version $Revision: 1.2 $
 */
public class BarreSemaineDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = -5806342643365229200L;

    /** Numéro de la semaine. */
    private String numeroSemaine;   
    
    /** Couleur de la semaine.*/
    private Color color;
    
    /** le type de jour. */
    private TypeSemaine typeJourEmploi;
    
    /** dto selectionnable. */    
    private Boolean vraiOuFauxSelectionActive;
    
    /** dto sélectionné .*/
    private Boolean vraiOuFauxSelection;
    
    /** Date lundi. **/
    private Date lundi;
    
    /** Date du dimanche. */
    private Date dimanche;

    /**
     * Accesseur numeroSemaine.
     * @return le numeroSemaine
     */
    public String getNumeroSemaine() {
        return numeroSemaine;
    }

    /**
     * Mutateur de numeroSemaine.
     * @param numeroSemaine le numeroSemaine à modifier.
     */
    public void setNumeroSemaine(String numeroSemaine) {
        this.numeroSemaine = numeroSemaine;
    }  

    /**
     * Accesseur couleur au format String.
     * @return le couleur
     */
    public String getCouleur() {
        final String colorStr = org.crlr.utils.StringUtils.colorToString(this.getColor());
        return colorStr;
    }

    /**
     * Mutateur de couleur au format String.
     * @param couleur la couleur
     */
    public void setCouleur(final String couleur) {
        if (couleur == null || !couleur.substring(0,1).equals("#")) {
            return;
        }
        final Integer c = Integer.parseInt(couleur.substring(1,couleur.length()),16);
        final Color color = new Color(c);
        this.color = color;
    }
    
    /**
     * Couleur de la bordure.
     * @return la couleur de la bordure de la case
     */
    public String getCouleurBordureTop() {
        if (color==null) { return ""; }
        return org.crlr.utils.StringUtils.colorToString(getColor().brighter());
    }

    /**
     * Couleur de la bordure.
     * @return la couleur de la bordure de la case
     */
    public String getCouleurBordureBottom() {
        if (color==null) { return ""; }
        return org.crlr.utils.StringUtils.colorToString(getColor().darker());
    }
    
    /**
     * Accesseur typeJourEmploi.
     * @return le typeJourEmploi
     */
    public TypeSemaine getTypeJourEmploi() {
        return typeJourEmploi;
    }

    /**
     * Mutateur de typeJourEmploi.
     * @param typeJourEmploi le typeJourEmploi à modifier.
     */
    public void setTypeJourEmploi(TypeSemaine typeJourEmploi) {
        this.typeJourEmploi = typeJourEmploi;
    }

    /**
     * Accesseur vraiOuFauxSelectionActive.
     * @return le vraiOuFauxSelectionActive
     */
    public Boolean getVraiOuFauxSelectionActive() {
        return vraiOuFauxSelectionActive;
    }

    /**
     * Mutateur de vraiOuFauxSelectionActive.
     * @param vraiOuFauxSelectionActive le vraiOuFauxSelectionActive à modifier.
     */
    public void setVraiOuFauxSelectionActive(Boolean vraiOuFauxSelectionActive) {
        this.vraiOuFauxSelectionActive = vraiOuFauxSelectionActive;
    }

    /**
     * Accesseur lundi.
     * @return le lundi
     */
    public Date getLundi() {
        return lundi;
    }

    /**
     * Mutateur de lundi.
     * @param lundi le lundi à modifier.
     */
    public void setLundi(Date lundi) {
        this.lundi = lundi;
    }

    /**
     * Accesseur vraiOuFauxSelection.
     * @return le vraiOuFauxSelection
     */
    public Boolean getVraiOuFauxSelection() {
        return vraiOuFauxSelection;
    }

    /**
     * Mutateur de vraiOuFauxSelection.
     * @param vraiOuFauxSelection le vraiOuFauxSelection à modifier.
     */
    public void setVraiOuFauxSelection(Boolean vraiOuFauxSelection) {
        this.vraiOuFauxSelection = vraiOuFauxSelection;

    }

    /**
     * Methode appelée pour définir la couleur du texte.
     * @return la couleur du texte au format string.
     */
    public String getTextColor() {
        final Date today = DateUtils.getAujourdhui();
        final Date lundiToday = DateUtils.setJourSemaine(today, Calendar.MONDAY);
        if (Boolean.TRUE.equals(this.vraiOuFauxSelection)) {
            return "#000000";
        } else if (lundiToday.equals(lundi)) { 
            return "#ffffff";
        } else if (TypeSemaine.NEUTRE.equals(typeJourEmploi)) {
            return "#000000"; 
        } else {
            return "#ffffff";
        }
    }
    
    /**
     * Accesseur de color {@link #color}.
     * @return retourne color
     */
    public Color getColor() {
        final Date today = DateUtils.getAujourdhui();
        final Date lundiToday = DateUtils.setJourSemaine(today, Calendar.MONDAY);
        if (Boolean.TRUE.equals(this.vraiOuFauxSelection)) {
            if (TypeSemaine.NEUTRE.equals(typeJourEmploi)) { 
                return TypeCouleurJour.SILVERSELECTED.getColor(); 
            } else if (TypeSemaine.PAIR.equals(typeJourEmploi)) { 
                return TypeCouleurJour.PAIRSELECTED.getColor(); 
            } else if (TypeSemaine.IMPAIR.equals(typeJourEmploi)) { 
                return TypeCouleurJour.IMPAIRSELECTED.getColor(); 
            } else { 
                return TypeCouleurJour.SILVERSELECTED.getColor(); 
            } 
        } else if (lundiToday.equals(lundi)) {
            if (TypeSemaine.NEUTRE.equals(typeJourEmploi)) { 
                return TypeCouleurJour.SILVERTODAY.getColor(); 
            } else if (TypeSemaine.PAIR.equals(typeJourEmploi)) { 
                return TypeCouleurJour.PAIRTODAY.getColor(); 
            } else if (TypeSemaine.IMPAIR.equals(typeJourEmploi)) { 
                return TypeCouleurJour.IMPAIRTODAY.getColor(); 
            } else { 
                return color; 
            } 
        } else {
            return color;
        }
    }

    /**
     * Mutateur de color {@link #color}.
     * @param color le color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Accesseur de dimanche {@link #dimanche}.
     * @return retourne dimanche
     */
    public Date getDimanche() {
        return dimanche;
    }

    /**
     * Mutateur de dimanche {@link #dimanche}.
     * @param dimanche le dimanche to set
     */
    public void setDimanche(Date dimanche) {
        this.dimanche = dimanche;
    }
    
    
}
