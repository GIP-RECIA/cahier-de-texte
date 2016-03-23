/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnumUtilsTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils;

import junit.framework.TestCase;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import org.crlr.utils.EnumUtils.IdStringEnum;

/**
 * Test de la classe EnumUtils.
 */
public class EnumUtilsTest extends TestCase {
    protected final Log log = LogFactory.getLog(getClass());
    /** MonIdEnumTest. */
    public enum MonIdEnumTest implements IdStringEnum {
/**
         * Type Choix 1.
         */
        CHOIX_1("1"), 
/**
         * Type Choix 2.
         */
        CHOIX_2("2"), 
/**
         * Type Choix 3.
         */
        CHOIX_3("3"), 
/**
         * Type Choix 4.
         */
        CHOIX_4("4"), 
/**
         * Type Choix 5.
         */
        CHOIX_5("5");
        /** Id. */
        private final String id;

/**
         * Constructeur.
         * @param id id
         */
        private MonIdEnumTest(String id) {
            this.id = id;
        }

        /**
         * Retourne id.
         *
         * @return id
         */
        public String getId() {
            return id;
        }
    }

    /**
     * TestFind.
     */
    public void testFind() {
        assertEquals(MonIdEnumTest.CHOIX_2, EnumUtils.find(MonIdEnumTest.values(), "2"));
        assertEquals(MonIdEnumTest.CHOIX_3, EnumUtils.find(MonIdEnumTest.values(), "3"));
        assertEquals(null, EnumUtils.find(MonIdEnumTest.values(), "EXISTE PAS"));
        assertEquals(null, EnumUtils.find(MonIdEnumTest.values(), null));
    }
}
