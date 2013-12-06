/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ChronoTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils;

import junit.framework.TestCase;

import org.crlr.exception.base.CrlrRuntimeException;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;

/**
 * Test de la classe {@link Chrono}.
 *
 * @author romana
 */
public class ChronoTest extends TestCase {
    /** DOCUMENTATION INCOMPLETE! */
    private final Log log = LogFactory.getLog(getClass());

    /**
     * 
     */
    public void testStartStop() {
        final Chrono chrono = new Chrono();
        assertEquals(0, chrono.getElapsed());
        assertFalse(chrono.isRunning());
        chrono.start();
        assertTrue(chrono.isRunning());
        sleep();
        chrono.stop();
        assertFalse(chrono.isRunning());
        assertFalse(chrono.getElapsed() == 0);
    }

    /**
     * 
     */
    public void testStartRunning() {
        final Chrono chrono = new Chrono();
        chrono.start();
        try {
            chrono.start();
            fail("CrlrRuntimeException attendue");
        } catch (CrlrRuntimeException e) {
            log.debug(e, "Exception capturée");
        }
    }

    /**
     * 
     */
    public void testStopNotRunning() {
        final Chrono chrono = new Chrono();
        try {
            chrono.stop();
            fail("CrlrRuntimeException attendue");
        } catch (CrlrRuntimeException e) {
            log.debug(e, "Exception capturée");
        }
    }

    /**
     * 
     */
    public void testGetElapsedRunning() {
        final Chrono chrono = new Chrono();
        chrono.start();
        try {
            chrono.getElapsed();
            fail("CrlrRuntimeException attendue");
        } catch (CrlrRuntimeException e) {
            log.debug(e, "Exception capturée");
        }
    }

    /**
     * 
     */
    public void testRun() {
        final Chrono chrono = new Chrono();
        assertFalse(chrono.isRunning());
        assertEquals(0, chrono.getElapsed());
        chrono.run(new Runnable() {
                public void run() {
                    sleep();
                }
            });
        assertFalse(chrono.isRunning());
        assertFalse(chrono.getElapsed() == 0);
        assertNotNull(chrono.getDureeHumain());
    }

    /**
     * 
     */
    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.debug(e, "Exception capturée");
        }
    }
}
