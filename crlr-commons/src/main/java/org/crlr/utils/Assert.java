/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Assert.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.utils;

import java.util.Collection;

/**
 * Méthodes d'assertion. Pour toutes les méthodes de cette classe, si une assertion
 * n'est pas vérifiée, une exception {@link AssertionException} est levée.
 *
 * @author romana
 */
public final class Assert {
/**
     * The Constructor.
     */
    private Assert() {
    }

    /**
     * Vérifie qu'une valeur ne vaut pas <code>null</code>.
     *
     * @param nom the nom
     * @param valeur the valeur
     */
    public static void isNotNull(String nom, Object valeur) {
        if (valeur == null) {
            throw new AssertionException(nom, "la valeur ne doit pas être null");
        }
    }

    /**
     * Vérifie qu'une valeur vaut <code>null</code>.
     *
     * @param nom the nom
     * @param valeur the valeur
     */
    public static void isNull(String nom, Object valeur) {
        if (valeur != null) {
            throw new AssertionException(nom, "la valeur n'est pas nulle");
        }
    }

    /**
     * Vérifie qu'une valeur vaut <code>false</code>.
     *
     * @param nom the nom
     * @param valeur the valeur
     */
    public static void isFalse(String nom, boolean valeur) {
        if (valeur) {
            throw new AssertionException(nom, "la valeur n'est pas égale à false");
        }
    }

    /**
     * Vérifie qu'une valeur vaut <code>true</code>.
     *
     * @param nom the nom
     * @param valeur the valeur
     */
    public static void isTrue(String nom, boolean valeur) {
        if (!valeur) {
            throw new AssertionException(nom, "la valeur n'est pas égale à true");
        }
    }

    /**
     * Vérifie qu'une valeur est égale à une valeur attendue.
     *
     * @param nom the nom
     * @param valeurAttendue the valeur attendue
     * @param valeurObtenue the valeur obtenue
     */
    public static void equals(String nom, Object valeurAttendue, Object valeurObtenue) {
        if (valeurObtenue == null) {
            if (valeurAttendue == null) {
                return;
            }
            throw new AssertionException(nom, "<valeur>", null);
        }

        if (!valeurObtenue.equals(valeurAttendue)) {
            throw new AssertionException(nom, valeurAttendue, valeurObtenue);
        }
    }

    /**
     * Vérifie qu'une chaîne de caractères n'est pas <code>null</code> ou vide,
     * au sens de {@link StringUtils#isEmpty(String)}.
     *
     * @param nom the nom
     * @param valeur the valeur
     */
    public static void isNotEmpty(String nom, String valeur) {
        if (org.apache.commons.lang.StringUtils.isEmpty(valeur)) {
            throw new AssertionException(nom,
                                         "la chaîne de caractères ne doit pas être vide ou null");
        }
    }

    /**
     * Vérifie qu'une collection d'objets n'est pas <code>null</code> ou vide.
     *
     * @param nom the nom
     * @param collection the collection
     */
    @SuppressWarnings("rawtypes")
    public static void isNotEmpty(String nom, Collection collection) {
        Assert.isNotNull(nom, collection);
        if (collection.isEmpty()) {
            throw new AssertionException(nom, "la collection ne doit pas être vide");
        }
    }
}
