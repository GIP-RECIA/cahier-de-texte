/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: LogTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.log;

import junit.framework.TestCase;

/**
 * Test de {@link Log}.
 *
 * @author romana
 */
public class LogTest extends TestCase {
    /** DOCUMENTATION INCOMPLETE! */
    private Log log;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        log = LogFactory.getLog(getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() throws Exception {
        log = null;
    }

    /**
     * 
     */
    public void testDebug() {
        try {
            new Exception("Test");
        } catch (Exception e) {
            log.debug(e, "Hello {0}", "SIAD");
        }
    }

    /**
     * 
     */
    public void testInfo() {
        try {
            new Exception("Test");
        } catch (Exception e) {
            log.info(e, "Hello {0}", "SIAD");
        }
    }

    /**
     * 
     */
    public void testWarning() {
        try {
            new Exception("Test");
        } catch (Exception e) {
            log.warning(e, "Hello {0}", "SIAD");
        }
    }

    /**
     * 
     */
    public void testError() {
        try {
            new Exception("Test");
        } catch (Exception e) {
            log.error(e, "Hello {0}", "SIAD");
        }
    }
}
