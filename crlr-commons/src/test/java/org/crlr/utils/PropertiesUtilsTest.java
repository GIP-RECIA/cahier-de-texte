/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PropertiesUtilsTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils;

import junit.framework.TestCase;

import java.util.Properties;

/**
 * Test de la classe {@link PropertiesUtils}.
 *
 * @author romana
 */
public class PropertiesUtilsTest extends TestCase {
    /**
     * 
     */
    public void testLoad() {
        final String chemin = "/org/crlr/utils/test.properties";
        final Properties props = PropertiesUtils.load(chemin);
        assertNotNull(props);
        assertEquals("salut", props.get("hello"));
    }

    /**
     * 
     */
    public void testLoadAvecPropertiesParDefaut() {
        final Properties propsDefaut = new Properties();
        propsDefaut.put("goodbye", "au revoir");

        final String chemin = "/org/crlr/utils/test.properties";
        final Properties props = PropertiesUtils.load(chemin, propsDefaut);
        assertNotNull(props);
        assertEquals("salut", props.get("hello"));
        assertEquals("au revoir", props.get("goodbye"));
    }

    /**
     * 
     
    public void testLoadFichierInconnu() {
        assertTrue(PropertiesUtils.load("hello.txt").isEmpty());
    }*/
}
