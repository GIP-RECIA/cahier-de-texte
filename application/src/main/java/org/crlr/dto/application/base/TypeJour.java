/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TypeJour.java,v 1.1 2010/03/29 09:29:35 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Enumération des des types jours.
 * 
 * @author breytond.
 * 
 */
public enum TypeJour {
    LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI, DIMANCHE;

    /**
     * position dans une semaine.
     * @return 0-6
     */
    public int getJourNum() {
        switch (this) {
        case LUNDI:
            return 0;
        case MARDI:
            return 1;
        case MERCREDI:
            return 2;
        case JEUDI:
            return 3;
        case VENDREDI:
            return 4;
        case SAMEDI:
            return 5;
        case DIMANCHE:
            return 6;
        default: return 0;
        }
    }
    
    /**
     * Getter name.
     * @return name
     */
    public String getJourName() {
        return name();
    }
    
    /**
     * @return label
     */
    public String getDisplayName() {
        switch (this) {
        case LUNDI:
            return "Lundi";
        case MARDI:
            return "Mardi";
        case MERCREDI:
            return "Mercredi";
        case JEUDI:
            return "Jeudi";
        case VENDREDI:
            return "Vendredi";
        case SAMEDI:
            return "Samedi";
        case DIMANCHE:
            return "Dimanche";
        default: return "";
        }
    }
    

    /**
     * @return Calendar day of week
     */
    public int getDayOfWeek() {
        switch (this) {
        case LUNDI:
            return Calendar.MONDAY;
        case MARDI:
            return Calendar.TUESDAY;
        case MERCREDI:
            return Calendar.WEDNESDAY;
        case JEUDI:
            return Calendar.THURSDAY;
        case VENDREDI:
            return Calendar.FRIDAY;
        case SAMEDI:
            return Calendar.SATURDAY;
        case DIMANCHE:
            return Calendar.SUNDAY;
        default: return Calendar.MONDAY;
        }
    }
    
    /**
     * @param dayOfWeek Calendar champ DAY_OF_WEEK
     * @return TypeJour
     */
    public static TypeJour getTypeJour(int dayOfWeek) {
        switch (dayOfWeek) {
        case Calendar.MONDAY:
            return TypeJour.LUNDI;
        case Calendar.TUESDAY:
            return TypeJour.MARDI;
        case Calendar.WEDNESDAY:
            return TypeJour.MERCREDI;
        case Calendar.THURSDAY:
            return TypeJour.JEUDI;
        case Calendar.FRIDAY:
            return TypeJour.VENDREDI;
        case Calendar.SATURDAY:
            return TypeJour.SAMEDI;
        case Calendar.SUNDAY:
            return TypeJour.DIMANCHE;
        default: return null;
        }
    }
    
   /**
    * Retourne le type de jour correspondant à la date de la seance.
    * @param date la date
    * @return un TypeJour
    */
    public static TypeJour getTypeJourFromDate(final Date date) {
       if (date == null) {
           return null;
       }
       final GregorianCalendar queryCal = new GregorianCalendar();
       queryCal.setTime(date);
       final TypeJour jour = TypeJour.getTypeJour(queryCal.get(Calendar.DAY_OF_WEEK));
       return jour; 
    }
}
