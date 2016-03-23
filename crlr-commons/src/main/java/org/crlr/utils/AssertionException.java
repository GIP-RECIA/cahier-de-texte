/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AssertionException.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.utils;

import org.crlr.exception.base.CrlrRuntimeException;

/**
 * Exception levée lorsqu'une assertion n'est pas vérifiée.
 *
 * @author breytond
 *
 * @see Assert
 */
public class AssertionException extends CrlrRuntimeException {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2945270113468611707L;

    /** The nom valeur. */
    private final String nomValeur;

    /** The valeur attendue. */
    private final Object valeurAttendue;

    /** The valeur obtenue. */
    private final Object valeurObtenue;

/**
     * The Constructor.
     * 
     * @param valeurObtenue
     *            the valeur obtenue
     * @param nomValeur
     *            the nom valeur
     * @param valeurAttendue
     *            the valeur attendue
     */
    public AssertionException(final String nomValeur, final Object valeurAttendue,
                              final Object valeurObtenue) {
        super("Valeur interdite : '{0}' == {2} au lieu de '{0}' == {1}", nomValeur,
              valeurAttendue, valeurObtenue);
        this.nomValeur = nomValeur;
        this.valeurAttendue = valeurAttendue;
        this.valeurObtenue = valeurObtenue;
    }

/**
     * The Constructor.
     * 
     * @param nomValeur
     *            the nom valeur
     * @param msg
     *            the msg
     */
    public AssertionException(final String nomValeur, final String msg) {
        super("Valeur '{0}' erronée : {1}", nomValeur, msg);
        this.nomValeur = nomValeur;
        this.valeurAttendue = null;
        this.valeurObtenue = null;
    }

    /**
     * Retourne nom valeur.
     *
     * @return the nom valeur
     */
    public String getNomValeur() {
        return nomValeur;
    }

    /**
     * Retourne valeur attendue.
     *
     * @return the valeur attendue
     */
    public Object getValeurAttendue() {
        return valeurAttendue;
    }

    /**
     * Retourne valeur obtenue.
     *
     * @return the valeur obtenue
     */
    public Object getValeurObtenue() {
        return valeurObtenue;
    }
}
