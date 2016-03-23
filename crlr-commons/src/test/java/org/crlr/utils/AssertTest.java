/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AssertTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils;

import junit.framework.TestCase;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Test de {@link Assert}.
 *
 * @author breytond
 */
public class AssertTest extends TestCase {
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private final Log log = LogFactory.getLog(getClass());

    /**
     *
     */
    public void testIsNotNull() {
        final String a = null;

        try {
            Assert.isNotNull("a", a);
            fail("AssertionException attendue");
        } catch (AssertionException e) {
            log.debug(e, "Erreur capturée");
        }

        Assert.isNotNull("b", "b");
    }

    /**
     *
     */
    public void testIsFalse() {
        final boolean a = true;

        try {
            Assert.isFalse("a", a);
            fail("AssertionException attendue");
        } catch (AssertionException e) {
            log.debug(e, "Erreur capturée");
        }

        Assert.isFalse("b", false);
        Assert.isFalse("c", Boolean.FALSE);
    }

    /**
     *
     */
    public void testIsTrue() {
        final boolean a = false;

        try {
            Assert.isTrue("a", a);
            fail("AssertionException attendue");
        } catch (AssertionException e) {
            log.debug(e, "Erreur capturée");
        }

        Assert.isTrue("b", true);
        Assert.isTrue("c", Boolean.TRUE);
    }

    /**
     *
     */
    public void testEquals() {
        Assert.equals("a", "a", "a");
        Assert.equals("b", 1, 1);
        Assert.equals("c", true, true);
        Assert.equals("b", 1, 1);
        Assert.equals("e", true, Boolean.TRUE);
        Assert.equals("f", null, null);

        try {
            Assert.equals("g", null, "hello");
            fail("AssertionException attendue");
        } catch (AssertionException e) {
            log.debug(e, "Erreur capturée");
        }

        try {
            Assert.equals("h", "hello", null);
            fail("AssertionException attendue");
        } catch (AssertionException e) {
            log.debug(e, "Erreur capturée");
        }
    }

    /**
     *
     */
    @SuppressWarnings("rawtypes")
    public void testIsNotEmpty() {
        Assert.isNotEmpty("a", "abc");

        try {
            Assert.isNotEmpty("b", (String) null);
            fail("AssertionException attendue");
        } catch (AssertionException e) {
            log.debug(e, "Erreur capturée");
        }
        try {
            Assert.isNotEmpty("c", "  ");
            fail("AssertionException attendue");
        } catch (AssertionException e) {
            log.debug(e, "Erreur capturée");
        }

        Assert.isNotEmpty("d", Collections.singleton("abc"));

        try {
            Assert.isNotEmpty("e", (Collection) null);
            fail("AssertionException attendue");
        } catch (AssertionException e) {
            log.debug(e, "Erreur capturée");
        }

        try {
            Assert.isNotEmpty("f", new ArrayList());
            fail("AssertionException attendue");
        } catch (AssertionException e) {
            log.debug(e, "Erreur capturée");
        }
    }
}
