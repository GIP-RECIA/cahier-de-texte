/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: IOUtilsTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils;

import junit.framework.TestCase;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test de la classe {@link IOUtils}.
 *
 * @author romana
 */
public class IOUtilsTest extends TestCase {
    /** The log. */
    private final Log log = LogFactory.getLog(getClass());

    /**
     * Test close.
     *
     * @throws Exception the exception
     */
    public void testClose() throws Exception {
        IOUtils.close(null);

        final String path = "test.txt";
        final InputStream input = getClass().getResourceAsStream(path);
        assertNotNull(input);

        assertEquals('H', input.read());
        assertTrue(input.available() > 0);
        IOUtils.close(input);

        try {
            input.read();
            fail("IOException attendue");
        } catch (IOException e) {
            log.debug(e, "Exception capturée");
        }

        IOUtils.close(input);
    }

    /**
     * Test get reader.
     */
    public void testGetReader() {
        final String infos = "BONJOUR\nTOUT LE MONDE";

        try {
            final BufferedReader reader = IOUtils.getReader(infos.getBytes());
            assertNotNull(reader);
            assertEquals("BONJOUR", reader.readLine());
            assertEquals("TOUT LE MONDE", reader.readLine());
            assertNull(reader.readLine());
            IOUtils.close(reader);
        } catch (IOException e) {
            log.debug(e, "Exception capturée");
        }
    }

    /**
     * Test combine.
     */
    public void testCombine() {
        assertEquals("toto" + File.separator + "tata", IOUtils.combine("toto", "tata"));
        assertEquals("toto" + File.separator + "tata", IOUtils.combine("toto/", "tata"));
        assertEquals("toto" + File.separator + "tata", IOUtils.combine("toto\\", "tata"));
        assertEquals("toto" + File.separator + "tata", IOUtils.combine("toto", "/tata"));
        assertEquals("toto" + File.separator + "tata", IOUtils.combine("toto", "\\tata"));
        assertEquals("toto" + File.separator + "tata", IOUtils.combine("toto/", "/tata"));
        assertEquals("toto" + File.separator + "tata", IOUtils.combine("toto\\", "\\tata"));
        assertEquals("toto" + File.separator + "tata", IOUtils.combine("toto/", "\\tata"));
        assertEquals("toto" + File.separator + "tata", IOUtils.combine("toto\\", "/tata"));
    }
}
