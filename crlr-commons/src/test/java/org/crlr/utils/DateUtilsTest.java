/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DateUtilsTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils;

import junit.framework.TestCase;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Test de {@link DateUtils}.
 *
 * @author breytond
 * @author romana
 */
public class DateUtilsTest extends TestCase {
    protected final Log log = LogFactory.getLog(getClass());

    /**
     *
     */
    public void testCreer() {
        final Date date = DateUtils.creer(2006, Calendar.APRIL, 19, 11, 47, 05);
        final Calendar cal = new GregorianCalendar(2006, Calendar.APRIL, 19, 11, 47, 05);
        assertEquals(cal.getTime(), date);
    }

   

    /**
     *
     */
    public void testCalculerNombreJours() {
        final Date d1 = DateUtils.creer(2006, Calendar.APRIL, 19);
        final Date d2 = DateUtils.creer(2006, Calendar.APRIL, 20);
        final Date d3 = DateUtils.creer(2006, Calendar.APRIL, 21);
        final Date d4 = DateUtils.creer(2004, Calendar.JANUARY, 21);
        final Date d5 = DateUtils.creer(2005, Calendar.JANUARY, 21);
        final Date d6 = DateUtils.creer(2006, Calendar.JANUARY, 21);

        assertEquals(0, DateUtils.calculerNombreJours(d1, d1));
        assertEquals(1, DateUtils.calculerNombreJours(d1, d2));
        assertEquals(2, DateUtils.calculerNombreJours(d1, d3));
        assertEquals(2, DateUtils.calculerNombreJours(d3, d1));

        // cas d'une année bissextile
        assertEquals(366, DateUtils.calculerNombreJours(d4, d5));

        // cas d'une année normale
        assertEquals(365, DateUtils.calculerNombreJours(d5, d6));
    }

    /**
     *
     */
    public void testCalculerNombreAnnees() {
        final Date d1 = DateUtils.creer(2001, Calendar.APRIL, 19);
        final Date d2 = DateUtils.creer(2002, Calendar.APRIL, 20);
        final Date d3 = DateUtils.creer(2003, Calendar.APRIL, 21);
        final Date d4 = DateUtils.creer(2004, Calendar.JANUARY, 21);
        final Date d5 = DateUtils.creer(2005, Calendar.JANUARY, 21);
        final Date d6 = DateUtils.creer(2006, Calendar.DECEMBER, 31);
        final Date d7 = DateUtils.creer(2007, Calendar.JANUARY, 1);

        assertEquals(0, DateUtils.calculerNombreAnnees(d1, d1));
        assertEquals(1, DateUtils.calculerNombreAnnees(d1, d2));
        assertEquals(2, DateUtils.calculerNombreAnnees(d1, d3));
        assertEquals(2, DateUtils.calculerNombreAnnees(d3, d1));
        assertEquals(3, DateUtils.calculerNombreAnnees(d1, d4));
        assertEquals(4, DateUtils.calculerNombreAnnees(d1, d5));
        assertEquals(1, DateUtils.calculerNombreAnnees(d5, d6));
        assertEquals(1, DateUtils.calculerNombreAnnees(d7, d6));
    }

    /**
     *
     */
    public void testCalculerNombreMois() {
        final Date d1 = DateUtils.creer(2001, Calendar.JANUARY, 1);
        final Date d2 = DateUtils.creer(2001, Calendar.JANUARY, 29);
        final Date d3 = DateUtils.creer(2001, Calendar.DECEMBER, 31);
        final Date d4 = DateUtils.creer(2002, Calendar.JANUARY, 1);
        final Date d5 = DateUtils.creer(2005, Calendar.JANUARY, 21);
        final Date d6 = DateUtils.creer(2006, Calendar.DECEMBER, 31);
        final Date d7 = DateUtils.creer(2007, Calendar.JANUARY, 1);

        assertEquals(0, DateUtils.calculerNombreMois(d1, d1));
        assertEquals(0, DateUtils.calculerNombreMois(d1, d2));
        assertEquals(11, DateUtils.calculerNombreMois(d1, d3));
        assertEquals(11, DateUtils.calculerNombreMois(d3, d1));
        assertEquals(12, DateUtils.calculerNombreMois(d1, d4));
        assertEquals(48, DateUtils.calculerNombreMois(d1, d5));
        assertEquals(23, DateUtils.calculerNombreMois(d5, d6));
        assertEquals(1, DateUtils.calculerNombreMois(d7, d6));
    }

    /**
     *
     */
    public void testNombreJours1() {
        final Calendar c1 = Calendar.getInstance();
        c1.set(2006, Calendar.JANUARY, 1);
        final Date date1 = c1.getTime();

        final Calendar c2 = Calendar.getInstance();
        c2.set(2006, Calendar.JANUARY, 5);
        final Date date2 = c2.getTime();

        final int nbJours = DateUtils.calculerNombreJours(date1, date2);
        assertNotNull(nbJours);
        assertEquals(4, nbJours);
    }

    /**
     *
     */
    public void testNombreJours3() {
        final Calendar c1 = Calendar.getInstance();
        c1.set(2006, Calendar.JANUARY, 1, 17, 34);
        final Date date1 = c1.getTime();

        final Calendar c2 = Calendar.getInstance();
        c2.set(2006, Calendar.JANUARY, 1, 18, 0);
        final Date date2 = c2.getTime();

        final int nbJours = DateUtils.calculerNombreJours(date1, date2);
        assertNotNull(nbJours);
        assertEquals(0, nbJours);
    }

    /**
     *
     */
    public void testNombreJours4() {
        final Calendar c1 = Calendar.getInstance();
        c1.set(2006, Calendar.JANUARY, 5);
        final Date date1 = c1.getTime();

        final Calendar c2 = Calendar.getInstance();
        c2.set(2006, Calendar.JANUARY, 1);
        final Date date2 = c2.getTime();

        final int nbJours = DateUtils.calculerNombreJours(date1, date2);
        assertNotNull(nbJours);
        assertEquals(4, nbJours);
    }

    /**
     *
     */
    public void testNombreJours5() {
        final Calendar c1 = Calendar.getInstance();
        c1.set(2001, Calendar.JANUARY, 1);
        final Date date1 = c1.getTime();

        final Calendar c2 = Calendar.getInstance();
        c2.set(2006, Calendar.JANUARY, 1);
        final Date date2 = c2.getTime();

        final int nbJours = DateUtils.calculerNombreJours(date1, date2);
        assertNotNull(nbJours);

        assertEquals(365 + 365 + 365 + 366 + 365, nbJours);
    }

    /**
     *
     */
    public void testNombreJours6() {
        final Calendar c1 = Calendar.getInstance();
        c1.set(2004, Calendar.FEBRUARY, 28);
        final Date date1 = c1.getTime();

        final Calendar c2 = Calendar.getInstance();
        c2.set(2004, Calendar.MARCH, 1);
        final Date date2 = c2.getTime();

        final int nbJours = DateUtils.calculerNombreJours(date1, date2);
        assertNotNull(nbJours);

        assertEquals(2, nbJours);
    }

    /**
     *
     */
    public void testNombreJours7() {
        final Calendar c1 = Calendar.getInstance();
        c1.set(2003, Calendar.FEBRUARY, 28);
        final Date date1 = c1.getTime();

        final Calendar c2 = Calendar.getInstance();
        c2.set(2003, Calendar.MARCH, 1);
        final Date date2 = c2.getTime();

        final int nbJours = DateUtils.calculerNombreJours(date1, date2);
        assertNotNull(nbJours);

        assertEquals(1, nbJours);
    }

    /**
     *
     */
    public void testNombreJours8() {
        final Calendar c1 = Calendar.getInstance();
        c1.set(1803, Calendar.JANUARY, 1);
        final Date date1 = c1.getTime();

        final Calendar c2 = Calendar.getInstance();
        c2.set(2003, Calendar.JANUARY, 1);
        final Date date2 = c2.getTime();

        final int nbJours = DateUtils.calculerNombreJours(date1, date2);
        assertNotNull(nbJours);

        assertEquals((int) (200 * 365.249), nbJours);
    }

    /**
     *
     */
    public void testAjouter() {
        final Date date = DateUtils.creer(2006, Calendar.MAY, 23);
        final Date dateNextWeek = DateUtils.ajouter(date, Calendar.DATE, 7);
        final Date dateNextWeekTest = DateUtils.creer(2006, Calendar.MAY, 30);
        assertEquals(dateNextWeekTest.getTime(), dateNextWeek.getTime());
    }

    /**
     *
     */
    public void testGetChamp() {
        final int annee = 2006;
        final int mois = Calendar.JUNE;
        final int jour = 23;
        final Date date = DateUtils.creer(annee, mois, jour);

        assertEquals(annee, DateUtils.getChamp(date, Calendar.YEAR));
        assertEquals(mois, DateUtils.getChamp(date, Calendar.MONTH));
        assertEquals(jour, DateUtils.getChamp(date, Calendar.DATE));
    }

    /**
     *
     */
    public void testFormat() {
        final GregorianCalendar cal = new GregorianCalendar(2006, Calendar.APRIL, 1);
        final Date date = cal.getTime();
        assertEquals("01/04/2006", DateUtils.format(date));
        assertEquals("2006 - 04 - 01", DateUtils.format(date, "yyyy - MM - dd"));
        assertEquals("1 avril 2006", DateUtils.format(date, Locale.FRANCE, "d MMMM yyyy"));
    }

    /**
     *
     */
    public void testParse() {
        assertEquals(DateUtils.creer(2006, Calendar.NOVEMBER, 24),
                     DateUtils.parse("24/11/2006"));
        assertNull(DateUtils.parse(null));
        assertNull(DateUtils.parse("charabia"));
    }

    /**
     *
     */
    public void testGetDatePlusProche() {
        final Date dateRepere = DateUtils.creer(2002, Calendar.JULY, 2);
        final Date dateAttendue = DateUtils.creer(2002, Calendar.MAY, 31);
        final List<Date> listeDates =
            Arrays.asList(new Date[] {
                              DateUtils.creer(2005, Calendar.JANUARY, 1),
                              DateUtils.creer(2006, Calendar.APRIL, 27), dateAttendue,
                              DateUtils.creer(2006, Calendar.JULY, 12)
                          });

        final Date dateTrouvee = DateUtils.getDatePlusProche(dateRepere, listeDates);
        assertEquals(dateAttendue, dateTrouvee);
    }

    /**
     *
     */
    public void testGetAujourdhui() {
        final Date aujourdhui = DateUtils.getAujourdhui();
        assertNotNull(aujourdhui);
        final Calendar c = Calendar.getInstance();
        c.setTime(aujourdhui);
        assertEquals(0, c.get(Calendar.HOUR));
        assertEquals(0, c.get(Calendar.MINUTE));
        assertEquals(0, c.get(Calendar.SECOND));
        assertEquals(0, c.get(Calendar.MILLISECOND));
    }

    /**
     *
     */
    public void testGetMaintenant() {
        final Date maintenant = DateUtils.getMaintenant();
        assertNotNull(maintenant);
    }

    /**
     *
     */
    public void testMin1() {
        final Date d1 = DateUtils.creer(2005, Calendar.JANUARY, 10);
        final Date d2 = DateUtils.creer(2006, Calendar.AUGUST, 15);
        final Date minDate = DateUtils.min(d1, d2);
        assertNotNull(minDate);
        assertEquals(minDate, d1);
    }

    /**
     *
     */
    public void testMin2() {
        final Date d1 = DateUtils.creer(2005, Calendar.JANUARY, 10);
        final Date d2 = DateUtils.creer(2006, Calendar.AUGUST, 15);
        final Date minDate = DateUtils.min(d2, d1);
        assertNotNull(minDate);
        assertEquals(minDate, d1);
    }

    /**
     *
     */
    public void testMin3() {
        final Date d1 = DateUtils.creer(2005, Calendar.JANUARY, 10);
        Date minDate = DateUtils.min(null, d1);
        assertNotNull(minDate);
        assertEquals(minDate, d1);

        minDate = DateUtils.min(d1, null);
        assertNotNull(minDate);
        assertEquals(minDate, d1);

        minDate = DateUtils.min(null, null);
        assertNull(minDate);
    }

    /**
     *
     */
    public void testIsBeetween() {
        final Date d1 = DateUtils.creer(2003, Calendar.JANUARY, 1);
        final Date d2 = DateUtils.creer(2001, Calendar.JANUARY, 1);
        final Date d3 = DateUtils.creer(2007, Calendar.JANUARY, 1);
        final Boolean isBeetween1 = DateUtils.isBetween(d1, d2, d3);
        assertTrue(isBeetween1);
        final Boolean isBeetween2 = DateUtils.isBetween(d3, d2, d1);
        assertFalse(isBeetween2);
        final Boolean isBeetween3 = DateUtils.isBetween(d2, d1, d3);
        assertFalse(isBeetween3);
        final Boolean isBeetween4 = DateUtils.isBetween(d2, d2, d3);
        assertTrue(isBeetween4);
        final Boolean isBeetween5 = DateUtils.isBetween(d1, d1, d3);
        assertTrue(isBeetween5);
        final Boolean isBeetween6 = DateUtils.isBetween(d3, d1, d3);
        assertTrue(isBeetween6);
    }

    /**
     *
     */
    public void testCompare() {
        final Date dateMax = DateUtils.creer(2010, Calendar.JANUARY, 1);
        final Date dateMin = DateUtils.creer(2000, Calendar.JANUARY, 1);
        assertTrue(DateUtils.compare(dateMax, dateMin, true) > 0);
        assertTrue(DateUtils.compare(dateMax, dateMin, false) > 0);

        assertTrue(DateUtils.compare(dateMin, dateMax, true) < 0);
        assertTrue(DateUtils.compare(dateMin, dateMax, false) < 0);

        assertTrue(DateUtils.compare(dateMin, dateMin, true) == 0);
        assertTrue(DateUtils.compare(dateMin, dateMin, false) == 0);

        assertTrue(DateUtils.compare(dateMax, null, true) > 0);
        assertTrue(DateUtils.compare(dateMax, null, false) < 0);

        assertTrue(DateUtils.compare(null, dateMax, true) < 0);
        assertTrue(DateUtils.compare(null, dateMax, false) > 0);

        assertTrue(DateUtils.compare(null, null, true) == 0);
        assertTrue(DateUtils.compare(null, null, false) == 0);
    }
}
