/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: MessageFormatTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.log;

import junit.framework.TestCase;

import java.text.MessageFormat;

/**
 * Test de {@link MessageFormat}.
 *
 * @author romana
 */
public class MessageFormatTest extends TestCase {
    /**
     * 
     */
    public void testFormat() {
        assertEquals("Hello world", MessageFormat.format("Hello {0}", "world"));

        // tests sur les apostrophes
        assertEquals("l'URL", MessageFormat.format("{0}", "l'URL"));
        // Tout apostrophe dans le pattern doit être doublé, autrement le
        // MessageFormat ne fonctionne pas.
        assertFalse("Redirection d'URL : index.html".equals(MessageFormat.format("Redirection d'URL : {0}",
                                                                                 "index.html")));
        // Attention : ceci n'est pas un BUG. Ce comportement fait partie de la
        // documentation JavaDoc de MessageFormat. Le fonctionnement est
        // simplement non intuitif.

        // En remplaçant les apostrophes simples par deux apostrophes, le
        // formatage de MessageFormat fonctionne.
        assertEquals("Redirection d''URL", "Redirection d'URL".replaceAll("'", "''"));
        assertEquals("Redirection d'URL : index.html => index.jsf",
                     MessageFormat.format("Redirection d'URL : {0} => {1}".replaceAll("'",
                                                                                      "''"),
                                          "index.html", "index.jsf"));
    }
}
