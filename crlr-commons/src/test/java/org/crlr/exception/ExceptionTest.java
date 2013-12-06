/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ExceptionTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.exception;

import junit.framework.TestCase;


import org.crlr.exception.base.CrlrException;
import org.crlr.exception.base.CrlrRuntimeException;


/**
 * Test des classes d'exception.
 *
 * @author breytond
 */
public class ExceptionTest extends TestCase {
    /**
     *
     */
    public void testCrlrRuntimeException() {
        final Exception cause = new Exception();
        final CrlrRuntimeException e =
            new CrlrRuntimeException(cause, "Hello {0}", "world");       
        assertEquals("Hello world", e.getMessage());
        assertSame(cause, e.getCause());
    }

    /**
     *
     */
    public void testUtilisateurCrlrException() {
        final Exception cause = new Exception();
        final CrlrException e = new CrlrException(cause, "Hello {0}", "world");
        assertEquals("Hello world", e.getMessage());
        assertSame(cause, e.getCause());
    }
}
