/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: LogFactoryTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.log;

import junit.framework.TestCase;

/**
 * Test de {@link LogFactory}.
 *
 * @author romana
 */
public class LogFactoryTest extends TestCase {
    /**
     * 
     */
    public void testGetLog() {
        final Log log1 = LogFactory.getLog(getClass());
        assertNotNull(log1);

        final Log log2 = LogFactory.getLog("hello");
        assertNotNull(log2);
    }
}
