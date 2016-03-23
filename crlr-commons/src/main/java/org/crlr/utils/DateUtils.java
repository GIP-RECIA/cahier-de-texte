/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DateUtils.java,v 1.3 2010/03/31 15:05:11 ent_breyton Exp $
 */

package org.crlr.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;

/**
 * Méthodes utilitaires pour {@link Date}.
 *
 * @author breytond
 * @author romana
 */
public final class DateUtils {
    /** The Constant DEFAULT_DATE_PATTERN. */
    private static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";

    /** The Constant DEFAULT_DATE_LOCAL. */
    private static final Locale DEFAULT_DATE_LOCAL = Locale.FRENCH;

    /** The Constant NB_MILLISECONDS_PAR_JOUR. */
    private static final BigDecimal NB_MILLISECONDS_PAR_JOUR =
        new BigDecimal(24 * 60 * 60 * 1000);

/**
 * The Constructor.
 */
    private DateUtils() {
    }

    /**
     * Crée une date.
     *
     * @param annee the annee
     * @param mois constante de {@link Calendar} : <code>Calendar.APRIL</code>, etc...
     * @param jour the jour
     * @param heures the heures
     * @param minutes the minutes
     * @param secondes the secondes
     *
     * @return the date
     */
    public static Date creer(int annee, int mois, int jour, int heures, int minutes,
                             int secondes) {
        return new GregorianCalendar(annee, mois, jour, heures, minutes, secondes).getTime();
    }

    /**
     * Crée une date, initialisée à minuit.
     *
     * @param annee the annee
     * @param mois the mois
     * @param jour the jour
     *
     * @return the date
     *
     * @see #creer(int, int, int, int, int, int)
     */
    public static Date creer(int annee, int mois, int jour) {
        return creer(annee, mois, jour, 0, 0, 0);
    }

    /**
     * Arrondit une date sur un type de champ de {@link Calendar}. Par exemple,
     * si l'on arrondit une date sur le champ {@link Calendar#DATE}, les composantes
     * heures, minutes, secondes et millisecondes sont initialisées à zéro. Si l'on
     * arrondit sur le champ {@link Calendar#MONTH}, les composantes précédentes sont
     * réinitialisées <b>et</b> le jour est initialisé à 1. Avec le champ {@link
     * Calendar#YEAR}, la date est en plus réinitialisée au 1er janvier de l'année.
     *
     * @param date the date
     * @param champ une valeur parmi : {@link Calendar#YEAR}, {@link Calendar#MONTH},
     *        {@link Calendar#DATE}, {@link Calendar#DAY_OF_MONTH}, {@link
     *        Calendar#HOUR}, {@link Calendar#HOUR_OF_DAY}, {@link Calendar#MINUTE},
     *        {@link Calendar#SECOND}, {@link Calendar#MILLISECOND}
     *
     * @return the date
     * @deprecated
     */
    
    public static Date arrondir(Date date, int champ) {
        return org.apache.commons.lang.time.DateUtils.truncate(date, champ);
        
    }

    /**
     * compare deux dates.
     *
     * @param date1 une date.
     * @param date2 une date.
     *
     * @return true si les deux dates sont exactes.
     */
    public static Boolean equalsDate(final Date date1, final Date date2) {
        if ((date1 == null) && (date2 == null)) {
            return Boolean.TRUE;
        } else if ((date1 == null) || (date2 == null)) {
            return Boolean.FALSE;
        }
        final int result =
            arrondir(date1, Calendar.DATE).compareTo(arrondir(date2, Calendar.DATE));
        if (result == 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * renvoie l'année de la date.
     *
     * @param d une date
     *
     * @return l'année
     */
    public static Integer getYear(Date d) {
        final Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.YEAR);
    }

    /**
     * Calcule le nombre de jours entre deux dates.
     *
     * @param date1 Date
     * @param date2 Date
     *
     * @return nombre de jours de différence entre les deux dates saisies
     */
    public static int calculerNombreJours(Date date1, Date date2) {
        return calculerNombreJours(date1, date2, false);
    }

    /**
     * Calcule le nombre de jours entre deux dates.
     *
     * @param date1 Date
     * @param date2 Date
     * @param isStrict Si vrai, le calcul du nombre de jour respecte l'ordre de saisie
     *        des dates. Autrement, le résultat sera toujours positif.
     *
     * @return nombre de jours de différence entre les deux dates saisies
     */
    public static int calculerNombreJours(Date date1, Date date2, boolean isStrict) {
        Assert.isNotNull("date1", date1);
        Assert.isNotNull("date2", date2);

        final Date dateMin;
        final Date dateMax;

        if (isStrict) {
            dateMin = date2;
            dateMax = date1;
        } else {
            if (date1.before(date2)) {
                dateMin = date1;
                dateMax = date2;
            } else {
                dateMin = date2;
                dateMax = date1;
            }
        }

        final BigDecimal nbMilliseconds =
            new BigDecimal(dateMax.getTime() - dateMin.getTime() - 10000);
        final BigDecimal nbJours =
            nbMilliseconds.divide(NB_MILLISECONDS_PAR_JOUR, RoundingMode.HALF_UP);
        return nbJours.intValue();
    }

    /**
     * Calcule le nombre de mois entre deux dates.
     *
     * @param date1 Date
     * @param date2 Date
     *
     * @return nombre de mois de différence entre les deux dates saisies
     */
    public static int calculerNombreMois(Date date1, Date date2) {
        return calculerNombreMois(date1, date2, false);
    }

    /**
     * Calcule le nombre de mois entre deux dates.
     *
     * @param date1 Date
     * @param date2 Date
     * @param isStrict Si vrai, le calcul du nombre de mois respecte l'ordre de saisie
     *        des dates. Autrement, le résultat sera toujours positif.
     *
     * @return nombre de mois de différence entre les deux dates saisies
     */
    public static int calculerNombreMois(Date date1, Date date2, boolean isStrict) {
        Assert.isNotNull("date1", date1);
        Assert.isNotNull("date2", date2);

        final Date dateMin;
        final Date dateMax;

        if (isStrict) {
            dateMin = date2;
            dateMax = date1;
        } else {
            if (date1.before(date2)) {
                dateMin = date1;
                dateMax = date2;
            } else {
                dateMin = date2;
                dateMax = date1;
            }
        }

        final int moisMax = DateUtils.getChamp(dateMax, Calendar.MONTH);
        final int moisMin = DateUtils.getChamp(dateMin, Calendar.MONTH);

        final int nbAnnees = calculerNombreAnnees(date1, date2, isStrict);
        final int nbMois = (nbAnnees * 12) + (moisMax - moisMin);

        return nbMois;
    }

    /**
     * Calcule le nombre d'années entre deux dates.
     *
     * @param date1 Date
     * @param date2 Date
     *
     * @return nombre d'annees de différence entre les deux dates saisies
     */
    public static int calculerNombreAnnees(Date date1, Date date2) {
        return calculerNombreAnnees(date1, date2, false);
    }

    /**
     * Calcule le nombre d'années entre deux dates. ATTENTION : Cette méthode
     * renvoie la différence entre les années de chaque dates, et non le nombre exact
     * d'années réelles entre les deux. Par exemple, dans le cas  28/12/2005 -
     * 05/01/2006 elle renverra 1 an ! Pour plus de précision (Age, durée exacte, etc.)
     * utiliser la methode getJoursMoisAnneesEntre().
     *
     * @param date1 Date
     * @param date2 Date
     * @param isStrict Si vrai, le calcul du nombre d'annee respecte l'ordre de saisie
     *        des dates. Autrement, le résultat sera toujours positif.
     *
     * @return nombre d'annees de différence entre les deux dates saisies
     */
    public static int calculerNombreAnnees(Date date1, Date date2, boolean isStrict) {
        Assert.isNotNull("date1", date1);
        Assert.isNotNull("date2", date2);

        final Date dateMin;
        final Date dateMax;

        if (isStrict) {
            dateMin = date2;
            dateMax = date1;
        } else {
            if (date1.before(date2)) {
                dateMin = date1;
                dateMax = date2;
            } else {
                dateMin = date2;
                dateMax = date1;
            }
        }

        final int anneeMax = DateUtils.getChamp(dateMax, Calendar.YEAR);
        final int anneeMin = DateUtils.getChamp(dateMin, Calendar.YEAR);

        final int nbAnnees = anneeMax - anneeMin;
        return nbAnnees;
    }

    /**
     * Ajoute une valeur à l'un des composants d'une date. Les composants sont
     * définis par les constantes de {@link Calendar}. Exemple :<pre><code>
     * ajouter(new Date(), Calendar.DATE, 1);</code></pre>.
     *
     * @param date the date
     * @param champ the champ
     * @param valeur the valeur
     *
     * @return the date
     */
    public static Date ajouter(Date date, int champ, int valeur) {
        if (date != null) {
            final Calendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.add(champ, valeur);
            return cal.getTime();
        }

        return null;
    }

    /**
     * Retourne un champ d'une {@link Date}. Cette méthode est un raccourci pour
     * :<pre><code>Calendar cal = new GregorianCalendar();cal.setTime(date);
     * return cal.get(champ);</code></pre>
     *
     * @param date the date
     * @param champ the champ
     *
     * @return the champ
     */
    public static int getChamp(Date date, int champ) {
        final Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(champ);
    }

    /**
     * Formate une date selon un motif défini.
     *
     * @param d date à formater
     * @param pattern motif de formatage
     *
     * @return une chaîne de caractères avec la date formatée
     */
    public static String format(Date d, String pattern) {
        if (d == null) {
            return null;
        }
        final String myPattern = ObjectUtils.defaultIfNull(pattern, DEFAULT_DATE_PATTERN);
        return new SimpleDateFormat(myPattern).format(d);
    }
    
    /**
     * Formate une date en la faisant passer à dimanche.
     *
     * @param d date à formater
     * @return une chaîne de caractères avec la date formatée
     */
    public static String formatDateAdimanche(Date d) {
        if (d == null) {
            return null;
        }
        return format(setJourSemaine(d, Calendar.SUNDAY));
    }

    /**
     * Formate une date selon un motif défini.
     *
     * @param d date à formater
     * @param local the local
     * @param pattern motif de formatage
     *
     * @return une chaîne de caractères avec la date formatée
     */
    public static String format(Date d, Locale local, String pattern) {
        if (d == null) {
            return null;
        }
        final String myPattern = ObjectUtils.defaultIfNull(pattern, DEFAULT_DATE_PATTERN);
        final Locale myLocal = (local != null) ? local : DEFAULT_DATE_LOCAL;
        return new SimpleDateFormat(myPattern, myLocal).format(d);
    }

    /**
     * Formate une date selon un motif par défaut : <code>dd/MM/yyyy</code>.
     *
     * @param d the d
     *
     * @return the string
     *
     * @see #format(Date, String)
     */
    public static String format(Date d) {
        return format(d, DEFAULT_DATE_PATTERN);
    }
    
    /**
     * Formate une date selon un motif par défaut : <code>dd/MM/yyyy</code>.
     * Et initialise la date au premier jour du mois.
     * @param d la date
     * @return la date au premier jour du mois formatée.
     */
    public static String formatDateAuPremierJourDuMois(Date d) {
        if (d == null) {
            return null;
        }
        return format(DateUtils.creer(DateUtils.getChamp(d, Calendar.YEAR), 
                DateUtils.getChamp(d, Calendar.MONTH) , 1));
    }

    /**
     * Formate une date selon un motif par défaut : <code>yyyy</code>.
     *
     * @param d the d
     *
     * @return the string
     *
     * @see #format(Date, String)
     */
    public static String formatAnnee(Date d) {
        return format(d, "yyyy");
    }

    /**
     * Retourne la date au format String avec un formatage court (ex 01/01/08).
     *
     * @param d date à formater.
     *
     * @return date formatee
     */
    public static String formatCourt(Date d) {
        return format(d, "dd/MM/yy");
    }

    /**
     * Retourne la date au format String avec un formatage mini (ex 311208).
     *
     * @param d date à formater.
     *
     * @return date formatee
     */
    public static String formatMini(Date d) {
        return format(d, "ddMMyy");
    }

    /**
     * Retourne la date au format String avec un formatage court (ex 01 janvier
     * 2008).
     *
     * @param d date à formater.
     *
     * @return date formatee
     */
    public static String formatText(Date d) {
        return format(d, "d MMMMM yyyy");
    }

    /**
     * Lit une date à partir d'une chaîne de caractères, selon un motif défini.
     *
     * @param date the date
     * @param pattern the pattern
     *
     * @return the date
     */
    public static Date parse(String date, String pattern) {
        final String myDate = StringUtils.trimToNull(date);
        if (myDate == null) {
            return null;
        }
        final String myPattern = ObjectUtils.defaultIfNull(pattern, DEFAULT_DATE_PATTERN);
        try {
            return new SimpleDateFormat(myPattern).parse(date);
        } catch (final ParseException e) {
            final Log log = LogFactory.getLog(DateUtils.class);
            log.debug(e, "Erreur durant la conversion de la date au format {1} : {0}",
                      date, myPattern);
            return null;
        }
    }

    /**
     * Lit une date à partir d'une chaîne de caractères, de la forme
     * <code>dd/MM/yyyy</code>.
     *
     * @param date la chaîne à parser.
     *
     * @return la date.
     *
     * @see #parse(String, String)
     */
    public static Date parse(String date) {
        return parse(date, DEFAULT_DATE_PATTERN);
    }

    /**
     * Retourne la date la plus proche (parmi une liste) d'une date de référence.
     *
     * @param dateRepere la date servant de repère.
     * @param dates la collection de dates à comparer
     *
     * @return la date la plus proche de dateRepere
     */
    public static Date getDatePlusProche(Date dateRepere, Collection<Date> dates) {
        final long timeRepere = dateRepere.getTime();
        long diffTimePlusProche = Long.MAX_VALUE;
        Date datePlusProche = null;
        for (Date d : dates) {
            if (d != null) {
                final long diff = Math.abs(timeRepere - d.getTime());
                if (diff < diffTimePlusProche) {
                    datePlusProche = d;
                    diffTimePlusProche = diff;
                }
            }
        }
        return datePlusProche;
    }
    
    /**
     * Retourne la date du jour (sans les heures).
     *
     * @return la date du jour.
     */
    public static Date getAujourdhui() {
        return arrondir(new Date(), Calendar.DATE);
    }

    /**
     * Retourne la date actuelle (avec les heures).
     *
     * @return la date actuelle (avec les heures).
     */
    public static Date getMaintenant() {
        return new Date();
    }

    /**
     * Retourne la comparaison entre 2 Dates. Les Dates Null peuvent être mises
     * en début ou en fin de liste. La comparaison de date de type java.sql.Date et
     * java.util.date est transparente.
     *
     * @param date1 Première date à comparer.
     * @param date2 Seconde date à comparer.
     * @param nullEtVideEnPremier Faut-il mettre les null et vide en premier ?
     *
     * @return inférieur, égal ou supérieur à 0 selon la règle des mèthodes standard
     *         "compareTo".
     */
    public static int compare(Date date1, Date date2, boolean nullEtVideEnPremier) {
        return ObjectUtils.compare((date1 != null) ? date1.getTime() : null,
                                   (date2 != null) ? date2.getTime() : null,
                                   nullEtVideEnPremier);
    }

    /**
     * Renvoie la plus petite date deux dates fournies en paramètre.
     *
     * @param date1 la première date
     * @param date2 la deuxième date
     *
     * @return la plus petite date
     */
    public static Date min(Date date1, Date date2) {
        return (compare(date1, date2, false) < 0) ? date1 : date2;
    }

    /**
     * Renvoie la plus grande date deux dates fournies en paramètre.
     *
     * @param date1 la première date
     * @param date2 la deuxième date
     *
     * @return la plus grande date
     */
    public static Date max(Date date1, Date date2) {
        return (compare(date1, date2, false) > 0) ? date1 : date2;
    }   

    /**
     * Renvoie True si "dateBetween" est comprise entre "dateMin" et "dateMax".
     *
     * @param dateBetween the date between
     * @param dateMin the date min
     * @param dateMax the date max
     *
     * @return the boolean
     */
    public static Boolean isBetween(Date dateBetween, Date dateMin, Date dateMax) {
        return ((compare(dateBetween, dateMin, false) >= 0) &&
               (compare(dateMax, dateBetween, false) >= 0));
    }
    
    /**
     * Renvoie True si "dateBetween" est comprise entre "dateMin" et "dateMax".
     *
     * @param dateBetween the date between
     * @param dateMin the date min
     * @param dateMax the date max
     *
     * @return the boolean
     */
    public static Boolean isBetweenDateMinNonCompris(Date dateBetween, Date dateMin, Date dateMax) {
        return ((compare(dateBetween, dateMin, false) > 0) &&
               (compare(dateMax, dateBetween, false) >= 0));
    }
    
    /**
     * Renvoie True si "dateBetween" est comprise entre "dateMin" et "dateMax".
     *
     * @param dateBetween the date between
     * @param dateMin the date min
     * @param dateMax the date max
     *
     * @return the boolean
     */
    public static Boolean isBetweenDateMaxNonCompris(Date dateBetween, Date dateMin, Date dateMax) {
        return ((compare(dateBetween, dateMin, false) >= 0) &&
               (compare(dateMax, dateBetween, false) > 0));
    }

    /**
     * Récupération de la date + 1. Est un raccourci pour DateUtils.ajouter(date,
     * Calendar.DAY_OF_MONTH, 1).
     *
     * @param d the d
     *
     * @return the date plus1
     */
    public static Date getDatePlus1(Date d) {
        return DateUtils.ajouter(d, Calendar.DAY_OF_MONTH, 1);
    }

    /**
     * Récupération de la date - 1. Est un raccourci pour DateUtils.ajouter(date,
     * Calendar.DAY_OF_MONTH, -1).
     *
     * @param d the d
     *
     * @return the date moins1
     */
    public static Date getDateMoins1(Date d) {
        return DateUtils.ajouter(d, Calendar.DAY_OF_MONTH, -1);
    }

    /**
     * Indique si une date d1 est inférieure ou égale à une date d2. Arrondi au
     * jour près.
     *
     * @param d1 date 1
     * @param d2 date 2
     *
     * @return true, if less or equals
     */
    public static Boolean lessOrEquals(Date d1, Date d2) {
        if ((d1 == null) || (d2 == null)) {
            return false;
        }
        final Date d1Arr = arrondir(d1, Calendar.DATE);
        final Date d2Arr = arrondir(d2, Calendar.DATE);
        return (d1Arr.before(d2) || d1Arr.equals(d2Arr));
    }
    
    /**
     * Indique si une date d1 est inférieure strictement à une date d2. Arrondi au
     * jour près.
     *
     * @param d1 date 1
     * @param d2 date 2
     *
     * @return true, if less or equals
     */
    public static Boolean lessStrict(Date d1, Date d2) {
        if ((d1 == null) || (d2 == null)) {
            return false;
        }
        final Date d1Arr = arrondir(d1, Calendar.DATE);
        return (d1Arr.before(d2));
    }

    /**
     * Methode : getDate. Récupérer la date courante. Entre janvier et juin
     * inclus, mettre date = jour/mois/anneeCourante Entre juillet et décembre inclus
     * mettre date = jour/mois/anneeCourante + plusNbAnnee
     *
     * @param date : la date d'entree.
     * @param jour : le jour de la date de retour.
     * @param mois : le mois de la date de retour.
     * @param plusNbAnne : le nombre d'annee en plus.
     *
     * @return : date de sortie.
     */
    public static Date getDate(Date date, int jour, int mois, int plusNbAnne) {
        final int moisDate = DateUtils.getChamp(date, Calendar.MONTH);
        final int annee = DateUtils.getChamp(date, Calendar.YEAR);
        if ((Calendar.JANUARY < moisDate) && (Calendar.JUNE >= moisDate)) {
            return DateUtils.creer(annee, mois, jour);
        } else {
            return DateUtils.creer((annee + plusNbAnne), mois, jour);
        }
    }

    /**
     * Retourne le nombre de jours du mois.
     *
     * @param moisDateDebut moisDateDebut le nombre 1 represente janvier.
     * @param anneeDateDebut anneeDateDebut
     *
     * @return nbJours du mois
     */
    public static int getNbJoursMois(int moisDateDebut, int anneeDateDebut) {
        int jours = 0;
        switch (moisDateDebut) {
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                jours = 30;
                break;
            case Calendar.FEBRUARY:
                jours = (((anneeDateDebut % 4) == 0) ? 29 : 28);
                break;
            default:
                jours = 31;
                break;
        }
        return jours;
    }
    
    /**
     * Retourne la semaine suivante au jour du lundi de la date passée en paramètre.
     * @param date la date.
     * @return la semaine suivante à lundi.
     */
    public static Date getSemaineSuivante(final Date date) {        
        return getSemaineSuivanteOuPrecedente(date, true);    
    }
    
    /**
     * Retourne la semaine précédente au jour du lundi de la date passée en paramètre.
     * @param date la date.
     * @return la semaine précédente à lundi.
     */
    public static Date getSemainePrecedente(final Date date) {        
        return getSemaineSuivanteOuPrecedente(date, false);    
    }
    
    /**
     * Retourne la date au jour spécifié passée en paramètre (champ).
     * @param date la date.
     * @param champ le jour de la semaine.
     * @return la date initialisé au jour de la semaine.
     */
    public static Date setJourSemaine(final Date date, int champ) {
        final Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, champ);
        return cal.getTime();
    }
    
    /**
     * Retourne la valeur du jour de la date.
     * @param date la date.
     * @param champ le champ.
     * @return la valeur du jour.
     */
    public static int getValeurJour(final Date date, int champ) {        
        return DateUtils.getChamp(setJourSemaine(date, champ), Calendar.DATE);
    }
    
    /**
     * Retourn ala semaine suivante ou précédente en fonction des paramétres.   
     * @param date la date.
     * @param suivante true pour suivante, false pour précédente.
     * @return la date suivante ou précédente.
     */
    private static Date getSemaineSuivanteOuPrecedente(final Date date, final Boolean suivante) {       
        final Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        // On se positionne sur le Lundi de la semaine courante :
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        // Puis on ajoute 7 jours
        cal.add(Calendar.DATE, (suivante) ? 7 : -7);
        return cal.getTime();        
    }
    
    
    /**
     * @param heure
     * @param minute
     * @return
     */
    public static String formatTime(Integer heure, Integer minutes) {
        return org.apache.commons.lang.StringUtils
                .leftPad(String.valueOf(heure), 2, '0') + 'H' + org.apache.commons.lang.StringUtils
        .leftPad(String.valueOf(minutes), 2, '0');
    }
    
}
