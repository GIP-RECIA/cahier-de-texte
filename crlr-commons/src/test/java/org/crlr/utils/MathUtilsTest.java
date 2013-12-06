/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: MathUtilsTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils;

import junit.framework.TestCase;

import java.math.BigDecimal;

/**
 * Test de la classe {@link MathUtils}.
 *
 * @author breytond
 * @author romana
 */
public class MathUtilsTest extends TestCase {
    /**
     * 
     */
    public void testEqualsDouble() {
        assertTrue(MathUtils.equals(1.1, 1.1));
        assertTrue(MathUtils.equals(1.1001, 1.1, 0.001));
        assertFalse(MathUtils.equals(1.11, 1.1, 0.001));
        assertFalse(MathUtils.equals(1.1, 1.11, 0.001));
    }

    /**
     * 
     */
    public void testEqualsFloat() {
        assertTrue(MathUtils.equals(1.1f, 1.1f));
        assertTrue(MathUtils.equals(1.1001f, 1.1f, 0.001f));
        assertFalse(MathUtils.equals(1.11f, 1.1f, 0.001f));
        assertFalse(MathUtils.equals(1.1f, 1.11f, 0.001f));
    }

    /**
     * 
     */
    public void testEqualsBigDecimal() {
        assertTrue(MathUtils.equals(new BigDecimal("1.1"), new BigDecimal("1.1")));
        assertTrue(MathUtils.equals(new BigDecimal("1.1001"), new BigDecimal("1.1"),
                                    new BigDecimal("0.001")));
        assertFalse(MathUtils.equals(new BigDecimal("1.11"), new BigDecimal("1.1"),
                                     new BigDecimal("0.001")));
        assertFalse(MathUtils.equals(new BigDecimal("1.1"), new BigDecimal("1.11"),
                                     new BigDecimal("0.001")));
    }

    /**
     * 
     */
    public void testArrondirVersEntierSuperieur() {
        assertEquals(2, MathUtils.arrondirVersEntierSuperieur(1.2));
        assertEquals(2, MathUtils.arrondirVersEntierSuperieur(1.5));
        assertEquals(2, MathUtils.arrondirVersEntierSuperieur(1.7));
        assertEquals(2, MathUtils.arrondirVersEntierSuperieur(2));
    }

    /**
     * 
     */
    public void testArrondirVersEntierInferieur() {
        assertEquals(1, MathUtils.arrondirVersEntierInferieur(1.2));
        assertEquals(1, MathUtils.arrondirVersEntierInferieur(1.5));
        assertEquals(1, MathUtils.arrondirVersEntierInferieur(1.7));
        assertEquals(1, MathUtils.arrondirVersEntierInferieur(1));
    }

    /**
     * 
     */
    public void testIsZero() {
        assertFalse(MathUtils.isZero(1));
        assertTrue(MathUtils.isZero(0));
        assertTrue(MathUtils.isZero(null));
        assertTrue(MathUtils.isZero(BigDecimal.ZERO));
        assertTrue(MathUtils.isZero(0d));
        assertTrue(MathUtils.isZero(0f));
        assertFalse(MathUtils.isZero(new BigDecimal("0.01")));
        assertFalse(MathUtils.isZero(0.01d));
        assertFalse(MathUtils.isZero(0.01f));
    }

    /**
     * 
     */
    public void testArrondir() {
        assertEquals(new BigDecimal("0.1").toPlainString(),
                     MathUtils.arrondir(new BigDecimal(0.1), 1).toPlainString());
    }

    /**
     * 
     */
    public void testArrondir2() {
        final BigDecimal bigDecimal = new BigDecimal("112.606012");
        final BigDecimal arrondi = MathUtils.arrondir(bigDecimal, 2);
        assertEquals("112.61", arrondi.toPlainString());
    }

    /**
     * 
     */
    public void testPrecision() {
        final Double nb = 10D;
        final Double nb1 = 14.5D;
        final Double nb2 = 0.52D;
        final Double nb3 = 1.623D;

        assertEquals("0", MathUtils.precision(nb).toString());
        assertEquals("1", MathUtils.precision(nb1).toString());
        assertEquals("2", MathUtils.precision(nb2).toString());
        assertEquals("3", MathUtils.precision(nb3).toString());
    }

    /**
     * 
     */
    public void testTronquer() {
        final Double nb = 10D;
        final Double nb1 = 14.5D;
        final Double nb2 = 0.52D;
        final Double nb3 = 1.623D;

        assertEquals("10.0", MathUtils.tronquer(nb, 1).toString());
        assertEquals("14.5", MathUtils.tronquer(nb1, 1).toString());
        assertEquals("0.5", (MathUtils.tronquer(nb2, 1)).toString());
        assertEquals("1.62", MathUtils.tronquer(nb3, 2).toString());
    }

    /**
     * 
     */
    public void testFormatDoubleMaxPrecision() {
        assertEquals("0", MathUtils.formatDoubleMaxPrecision(0.0, 2));
        assertEquals("1", MathUtils.formatDoubleMaxPrecision(1.0, 2));
        assertEquals("1.11", MathUtils.formatDoubleMaxPrecision(1.111111, 2));
    }

    /**
     * 
     */
    public void testFormatDoubleArrondie() {
        assertEquals("1.00", MathUtils.formatDoubleArrondie(1.0, 2));
        assertEquals("1.11", MathUtils.formatDoubleArrondie(1.111111, 2));
        assertEquals("1.12", MathUtils.formatDoubleArrondie(1.1199, 2));
        assertEquals("1", MathUtils.formatDoubleArrondie(1.1199, 0));
    }
}
