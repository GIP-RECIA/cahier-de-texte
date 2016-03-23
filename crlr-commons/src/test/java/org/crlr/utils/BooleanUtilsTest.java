/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: BooleanUtilsTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils;

import junit.framework.TestCase;

/**
 * Test de la classe {@link BooleanUtils}.
 *
 * @author romana
 * @author breytond
 */
public class BooleanUtilsTest extends TestCase {
    /**
     *
     */
    public void testIsTrue() {
        final String[] trueValues =
            { "yes", "oui", "1", "TRue", "true", "ON", "vraI", "V", };
        for (final String value : trueValues) {
            assertTrue("La valeur aurait dû être considérée comme true : " + value,
                       BooleanUtils.isTrue(value));
        }

        final String[] falseValues = { null, "", "    ", "false", "F", };
        for (final String value : falseValues) {
            assertFalse("La valeur aurait dû être considérée comme false : " + value,
                        BooleanUtils.isTrue(value));
        }
    }

    /**
     *
     */
    public void testCompare() {
        assertEquals(0, BooleanUtils.compare(null, Boolean.TRUE, true, true));
        assertEquals(-1, BooleanUtils.compare(null, Boolean.FALSE, true, true));
        assertEquals(-1, BooleanUtils.compare(Boolean.TRUE, Boolean.FALSE, false, true));
        assertEquals(1, BooleanUtils.compare(Boolean.TRUE, Boolean.FALSE, false, false));
    }

    /**
     *
     */
    public void testIsTrueBoolean() {
        assertEquals(true, BooleanUtils.isTrue(Boolean.TRUE));
        assertEquals(false, BooleanUtils.isTrue(Boolean.FALSE));
        assertEquals(false, BooleanUtils.isTrue((Boolean) null));
    }

    /**
     *
     */
    public void testIsFalseBoolean() {
        assertEquals(true, BooleanUtils.isFalse(Boolean.FALSE));
        assertEquals(false, BooleanUtils.isFalse(Boolean.TRUE));
        assertEquals(false, BooleanUtils.isFalse((Boolean) null));
    }

    /**
     *
     */
    public void testAnd() {
        assertEquals(true, BooleanUtils.and(null, null));
        assertEquals(true, BooleanUtils.and(null, null, Boolean.TRUE));
        assertEquals(false, BooleanUtils.and(null, null, Boolean.FALSE));
        assertEquals(true, BooleanUtils.and(true, null));
        assertEquals(true, BooleanUtils.and(true, null, Boolean.TRUE));
        assertEquals(true, BooleanUtils.and(true, null, Boolean.FALSE));
        assertEquals(false, BooleanUtils.and(false, null));
        assertEquals(false, BooleanUtils.and(false, null, Boolean.TRUE));
        assertEquals(false, BooleanUtils.and(false, null, Boolean.FALSE));
        assertEquals(true, BooleanUtils.and(null, true));
        assertEquals(true, BooleanUtils.and(null, true, Boolean.TRUE));
        assertEquals(true, BooleanUtils.and(null, true, Boolean.FALSE));
        assertEquals(false, BooleanUtils.and(null, false));
        assertEquals(false, BooleanUtils.and(null, false, Boolean.TRUE));
        assertEquals(false, BooleanUtils.and(null, false, Boolean.FALSE));
        assertEquals(true, BooleanUtils.and(true, true));
        assertEquals(true, BooleanUtils.and(true, true, Boolean.TRUE));
        assertEquals(true, BooleanUtils.and(true, true, Boolean.FALSE));
        assertEquals(false, BooleanUtils.and(false, false));
        assertEquals(false, BooleanUtils.and(false, false, Boolean.TRUE));
        assertEquals(false, BooleanUtils.and(false, false, Boolean.FALSE));
        assertEquals(false, BooleanUtils.and(true, false));
        assertEquals(false, BooleanUtils.and(true, false, Boolean.TRUE));
        assertEquals(false, BooleanUtils.and(true, false, Boolean.FALSE));
        assertEquals(false, BooleanUtils.and(false, true));
        assertEquals(false, BooleanUtils.and(false, true, Boolean.TRUE));
        assertEquals(false, BooleanUtils.and(false, true, Boolean.FALSE));
    }

    /**
     *
     */
    public void testOr() {
        assertEquals(true, BooleanUtils.or(null, null));
        assertEquals(true, BooleanUtils.or(null, null, Boolean.TRUE));
        assertEquals(false, BooleanUtils.or(null, null, Boolean.FALSE));
        assertEquals(true, BooleanUtils.or(true, null));
        assertEquals(true, BooleanUtils.or(true, null, Boolean.TRUE));
        assertEquals(true, BooleanUtils.or(true, null, Boolean.FALSE));
        assertEquals(false, BooleanUtils.or(false, null));
        assertEquals(false, BooleanUtils.or(false, null, Boolean.TRUE));
        assertEquals(false, BooleanUtils.or(false, null, Boolean.FALSE));
        assertEquals(true, BooleanUtils.or(null, true));
        assertEquals(true, BooleanUtils.or(null, true, Boolean.TRUE));
        assertEquals(true, BooleanUtils.or(null, true, Boolean.FALSE));
        assertEquals(false, BooleanUtils.or(null, false));
        assertEquals(false, BooleanUtils.or(null, false, Boolean.TRUE));
        assertEquals(false, BooleanUtils.or(null, false, Boolean.FALSE));
        assertEquals(true, BooleanUtils.or(true, true));
        assertEquals(true, BooleanUtils.or(true, true, Boolean.TRUE));
        assertEquals(true, BooleanUtils.or(true, true, Boolean.FALSE));
        assertEquals(false, BooleanUtils.or(false, false));
        assertEquals(false, BooleanUtils.or(false, false, Boolean.TRUE));
        assertEquals(false, BooleanUtils.or(false, false, Boolean.FALSE));
        assertEquals(true, BooleanUtils.or(true, false));
        assertEquals(true, BooleanUtils.or(true, false, Boolean.TRUE));
        assertEquals(true, BooleanUtils.or(true, false, Boolean.FALSE));
        assertEquals(true, BooleanUtils.or(false, true));
        assertEquals(true, BooleanUtils.or(false, true, Boolean.TRUE));
        assertEquals(true, BooleanUtils.or(false, true, Boolean.FALSE));
    }
}
