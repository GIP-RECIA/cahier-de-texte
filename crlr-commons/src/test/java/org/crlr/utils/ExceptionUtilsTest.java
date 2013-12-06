/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ExceptionUtilsTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils;

import java.sql.SQLException;

import junit.framework.TestCase;

/**
 * Test de la classe {@link ExceptionUtils}.
 *
 * @author romana
 */
public class ExceptionUtilsTest extends TestCase {
    /** DOCUMENTATION INCOMPLETE! */
    //private final Log log = LogFactory.getLog(ExceptionUtilsTest.class);

    /**
     * 
     */
    public void testGetCauseNull() {
        assertNull(ExceptionUtils.getCause(null));
        assertNull(ExceptionUtils.getCause(new Exception()));
    }

    /**
     * 
     */
    public void testGetCauseError() {
        final Exception e = new Exception(new Error());
        assertNull(ExceptionUtils.getCause(e));
    }

    /**
     * 
     */
    public void testGetCauseClasseSpecifique() {
        final Exception e4 = new NumberFormatException();
        final Exception e3 = new SQLException();
        e3.initCause(e4);
        final Exception e2 = new RuntimeException(e3);
        final Exception e1 = new Exception(e2);

        final Exception cause = ExceptionUtils.getCause(e1, SQLException.class);
        assertEquals(e3, cause);
        assertNull(ExceptionUtils.getCause(cause, SQLException.class));

        assertNull(ExceptionUtils.getCause(null, SQLException.class));
    }

    /**
     * 
     */
    public void testGetStackTrace() {
        final String stackTrace = ExceptionUtils.getStackTrace();
        assertNotNull(stackTrace);
        assertFalse(stackTrace.length() == 0);
        //log.info("Stack trace :\n{0}", stackTrace);
    }
}
