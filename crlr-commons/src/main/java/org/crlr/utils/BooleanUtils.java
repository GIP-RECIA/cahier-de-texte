/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: BooleanUtils.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Méthodes utilitaires pour le type {@link Boolean}.
 *
 * @author breytond
 * @author romana
 */
public final class BooleanUtils {
    /** The Constant TRUE_VALUES. */
    private static final Set<String> TRUE_VALUES;

    static {
        final String[] values =
            { "true", "yes", "y", "vrai", "v", "1", "on", "oui", "o" };
        TRUE_VALUES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(values)));
    }

/**
 * The Constructor.
 */
    private BooleanUtils() {
    }

    /**
     * Teste si une chaîne de caractères représente la valeur <code>true</code>.<p>Une
     * chaîne représente la valeur <code>true</code> si celle-ci fait partie de
     * l'ensemble suivant :</p>
     *  <ul>
     *      <li><code>true</code>,</li>
     *      <li><code>yes</code>,</li>
     *      <li><code>y</code>,</li>
     *      <li><code>vrai</code>,</li>
     *      <li><code>v</code>,</li>
     *      <li><code>1</code>,</li>
     *      <li><code>oui</code>,</li>
     *      <li><code>o</code>,</li>
     *      <li><code>on</code></li>
     *  </ul>
     *  <p>Si la chaîne ne fait pas partie de cette ensemble, la chaîne est
     * représentée avec la valeur <code>false</code>. Une chaîne <code>null</code> a
     * pour valeur <code>false</code>.</p>
     *
     * @param str the str
     *
     * @return true, if is true
     */
    public static boolean isTrue(String str) {
        return (str != null) && TRUE_VALUES.contains(str.toLowerCase());
    }

    /**
     * Retourne true si les val1 et val2 sont identique. 
     * Si une valeur passée est nulle, sa valeur est considérée False
     * @param val1 valeur 1
     * @param val2 valeur 2
     * @return true si val1 et val2 représentent la meme valeur boolean.
     */
    public static boolean equals(final Boolean val1, final Boolean val2) {
        final Boolean v1 = BooleanUtils.isTrue(val1);
        final Boolean v2 =  BooleanUtils.isTrue(val2);
        return v1.equals(v2);
    }
    
    /**
     * Retourne la valeur du boolean en String.
     *
     * @param value valeur du boolean
     *
     * @return la valeur du boolean sous forme de String.
     */
    public static String convertOuiNon(Boolean value) {
        return ((value != null) && value) ? "Oui" : "Non";
    }

    /**
     * Retourne Vrai si et seulement si la valeur n'est pas null et est true.
     *
     * @param value la valeur à vérifier.
     *
     * @return Vrai si et seulement si la valeur n'est pas null et est true.
     */
    public static boolean isTrue(Boolean value) {
        return ((value != null) && (value));
    }

    /**
     * Retourne Vrai si et seulement si la valeur n'est pas null et est false.
     *
     * @param value la valeur à vérifier.
     *
     * @return Vrai si et seulement si la valeur n'est pas null et est false.
     */
    public static boolean isFalse(Boolean value) {
        return ((value != null) && (!value));
    }

    /**
     * Retourne la comparaison entre 2 booleans. Permet de trier une liste, en
     * indiquant la valeur par défaut du boolean et en indiquant si les valeurs Vrai
     * doivent être en premier dans la liste.
     *
     * @param valeur1 Première valeur à comparer.
     * @param valeur2 Seconde valeur à comparer.
     * @param valeurParDefaut Valeur par défaut à utiliser en cas de valeur null
     * @param vraiEnPremier Mettre les Vrai en premier.
     *
     * @return inférieur, égal ou supérieur à 0 selon la règle des méthodes standard
     *         "compareTo".
     */
    public static int compare(Boolean valeur1, Boolean valeur2, boolean valeurParDefaut,
                              boolean vraiEnPremier) {
        final boolean val1 = ObjectUtils.defaultIfNull(valeur1, valeurParDefaut);
        final boolean val2 = ObjectUtils.defaultIfNull(valeur2, valeurParDefaut);
        if (vraiEnPremier) {
            return -1 * ObjectUtils.compare(val1, val2, true);
        } else {
            return ObjectUtils.compare(val1, val2, true);
        }
    }

    /**
     * Retourne une coche (X) si la valeur est vraie.
     *
     * @param value valeur à vérifier.
     *
     * @return "X" si vrai, rien sinon.
     */
    public static String convertCoche(Boolean value) {
        if ((value != null) && value) {
            return "X";
        }
        return "";
    }

    /**
     * And.
     *
     * @param boolean1 the boolean1
     * @param boolean2 the boolean2
     * @param defaut the defaut
     *
     * @return true, if successful
     */
    public static boolean and(Boolean boolean1, Boolean boolean2, Boolean defaut) {
        Assert.isNotNull("defaut", defaut);
        if ((boolean1 == null) && (boolean2 == null)) {
            return defaut.booleanValue();
        } else if ((boolean1 == null) && (boolean2 != null)) {
            return boolean2.booleanValue();
        } else if ((boolean1 != null) && (boolean2 == null)) {
            return boolean1.booleanValue();
        } else {
            return (boolean1 && boolean2);
        }
    }

    /**
     * And.
     *
     * @param boolean1 the boolean1
     * @param boolean2 the boolean2
     *
     * @return true, if successful
     */
    public static boolean and(Boolean boolean1, Boolean boolean2) {
        return and(boolean1, boolean2, Boolean.TRUE);
    }

    /**
     * Or.
     *
     * @param boolean1 the boolean1
     * @param boolean2 the boolean2
     * @param defaut the defaut
     *
     * @return true, if successful
     */
    public static boolean or(Boolean boolean1, Boolean boolean2, Boolean defaut) {
        Assert.isNotNull("defaut", defaut);
        if ((boolean1 == null) && (boolean2 == null)) {
            return defaut.booleanValue();
        } else if ((boolean1 == null) && (boolean2 != null)) {
            return boolean2.booleanValue();
        } else if ((boolean1 != null) && (boolean2 == null)) {
            return boolean1.booleanValue();
        } else {
            return (boolean1 || boolean2);
        }
    }

    /**
     * Or.
     *
     * @param boolean1 the boolean1
     * @param boolean2 the boolean2
     *
     * @return true, if successful
     */
    public static boolean or(Boolean boolean1, Boolean boolean2) {
        return or(boolean1, boolean2, Boolean.TRUE);
    }
}
