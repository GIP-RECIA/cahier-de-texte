/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: MathUtils.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.utils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Méthodes utilitaire pour des opérations mathématiques.
 *
 * @author breytond
 * @author romana
 */
public final class MathUtils {
    /** Différence en deça de laquelle on considère que deux nombres sont égaux. */
    public static final Double EPSILON_DOUBLE = 0.001D;

    /** Différence en deça de laquelle on considère que deux nombres sont égaux. */
    public static final Float EPSILON_FLOAT = 0.001F;

    /** Différence en deça de laquelle on considère que deux nombres sont égaux. */
    public static final BigDecimal EPSILON_BIGDECIMAL = new BigDecimal("0.001");

/**
 * The Constructor.
 */
    private MathUtils() {
    }

    /**
     * Teste si deux nombres sont égaux.
     *
     * @param a the a
     * @param b the b
     * @param epsilon the epsilon
     *
     * @return true, if equals
     */
    public static boolean equals(Double a, Double b, Double epsilon) {
        return Math.abs(a - b) <= epsilon;
    }

    /**
     * Teste si deux nombres sont égaux, en utilisant une valeur par défaut pour
     * <code>epsilon</code>.
     *
     * @param a the a
     * @param b the b
     *
     * @return true, if equals
     *
     * @see #EPSILON_DOUBLE
     */
    public static boolean equals(Double a, Double b) {
        return equals(a, b, EPSILON_DOUBLE);
    }

    /**
     * Teste si deux nombres sont égaux.
     *
     * @param a the a
     * @param b the b
     * @param epsilon the epsilon
     *
     * @return true, if equals
     */
    public static boolean equals(Float a, Float b, Float epsilon) {
        return Math.abs(a - b) <= epsilon;
    }

    /**
     * Teste si deux nombres sont égaux, en utilisant une valeur par défaut pour
     * <code>epsilon</code>.
     *
     * @param a the a
     * @param b the b
     *
     * @return true, if equals
     *
     * @see #EPSILON_FLOAT
     */
    public static boolean equals(Float a, Float b) {
        return equals(a, b, EPSILON_FLOAT);
    }

    /**
     * Compare l'égalité stricte de deux nombres.
     *
     * @param a le premier nombre.
     * @param b le second nombre.
     *
     * @return true si les valeurs des deux nombres sont identiques.
     */
    public static Boolean equals(Number a, Number b) {
        final Boolean result;
        if ((a == null) && (b == null)) {
            result = true;
        } else if (((a == null) && (b != null)) || ((a != null) && (b == null))) {
            result = false;
        } else {
            //compare en finalité les valeurs afin d'eviter d'avoir des erreurs de type d'objet :
            //En effet si on compare un Long de valeur 1 et un Integer de valeur 1 --> equals retourne false
            //pourtant le résultat attendu est true.
            result = a.toString().equals(b.toString());
        }

        return result;
    }

    /**
     * Teste si deux nombres sont égaux.
     *
     * @param a the a
     * @param b the b
     * @param epsilon the epsilon
     *
     * @return true, if equals
     */
    public static boolean equals(BigDecimal a, BigDecimal b, BigDecimal epsilon) {
        return a.subtract(b).abs().compareTo(epsilon) < 1;
    }

    /**
     * Teste si deux nombres sont égaux, en utilisant une valeur par défaut pour
     * <code>epsilon</code>.
     *
     * @param a the a
     * @param b the b
     *
     * @return true, if equals
     *
     * @see #EPSILON_BIGDECIMAL
     */
    public static boolean equals(BigDecimal a, BigDecimal b) {
        return equals(a, b, EPSILON_BIGDECIMAL);
    }

    /**
     * Retourne une valeur arrondie vers l'entier supérieur.
     *
     * @param a the a
     *
     * @return the int
     */
    public static int arrondirVersEntierSuperieur(double a) {
        final double arrondi = Math.floor(a);
        if (!equals(a, arrondi)) {
            return (int) arrondi + 1;
        }
        return (int) arrondi;
    }

    /**
     * Retourne une valeur arrondie vers l'entier inférieur.
     *
     * @param a the a
     *
     * @return the int
     */
    public static int arrondirVersEntierInferieur(double a) {
        return (int) Math.floor(a);
    }

    /**
     * Arrondir un nombre avec la précision indiquée.
     *
     * @param valeur nombre à arrondir
     * @param precision nombre de chiffres après la virgule à conserver
     *
     * @return le nombre arrondi
     */
    public static BigDecimal arrondir(BigDecimal valeur, int precision) {
        if (valeur == null) {
            return null;
        }
        final BigDecimal valeurDecalee = valeur.movePointRight(precision);
        final long longArrondi = Math.round(valeurDecalee.doubleValue());
        return new BigDecimal(longArrondi).movePointLeft(precision);
    }

    /**
     * Teste si un {@link Number} vaut zéro ou <code>null</code>.
     *
     * @param n the n
     *
     * @return true, if is zero
     */
    public static boolean isZero(Number n) {
        if (n == null) {
            return true;
        }
        if (n instanceof BigDecimal) {
            return equals((BigDecimal) n, BigDecimal.ZERO);
        }
        if (n instanceof Double) {
            return equals((Double) n, 0d);
        }
        if (n instanceof Float) {
            return equals((Float) n, 0f);
        }
        return n.longValue() == 0;
    }

    /**
     * Retourne le nombre de chiffres après la virgule.
     *
     * @param n le nombre a évaluer.
     *
     * @return le nombre de chiffres après la virgule.
     */
    public static Integer precision(Double n) {
        if (n == null) {
            return null;
        }
        final String valeurChaine = n.toString();
        final List<String> lValeur = StringUtils.split(valeurChaine, "[.,]");
        if (lValeur.size() == 2) {
            final String decimal = lValeur.get(1);
            return (decimal.equals("0")) ? 0 : decimal.length();
        } else {
            return 0;
        }
    }

    /**
     * Tronque le nombre de chiffres après la virgule en fonction de la précision
     * voulue.
     *
     * @param n le nombre.
     * @param precision le nombre de chiffres après la virgule à conserver.
     *
     * @return le nombre tronqué.
     */
    public static Double tronquer(Double n, int precision) {
        if (n == null) {
            return null;
        }
        final String valeurChaine = n.toString();
        final List<String> lValeur = StringUtils.split(valeurChaine, "[.,]");
        if (lValeur.size() == 2) {
            final String decimal = lValeur.get(1);
            if (decimal.length() > precision) {
                return new Double(lValeur.get(0) + "." + decimal.substring(0, precision));
            } else {
                return n;
            }
        } else {
            return n;
        }
    }

    /**
     * Addition de deux chaînes de caractère qui représentent des entiers. Val1
     * et val2 acceptent les "+". si le résultat vaut 0, alors la valeur renvoyée est
     * "".
     *
     * @param val1 valeur 1
     * @param val2 valeur 2
     * @param avecPlus the avec plus
     *
     * @return somme de val1 et val2 ex : val1 = "2", val2 = "" val1 = "2", val2 = "+3"
     *         val1 = null, val2 = "" ...
     */
    public static String addition(String val1, String val2, boolean avecPlus) {
        final String val1Tr = org.apache.commons.lang.StringUtils.trimToNull(val1);
        final String val2Tr = org.apache.commons.lang.StringUtils.trimToNull(val2);
        final Integer val1I =
            (val1Tr == null) ? 0 : Integer.valueOf(val1Tr.replace("+", ""));
        final Integer val2I =
            (val2Tr == null) ? 0 : Integer.valueOf(val2Tr.replace("+", ""));
        final Integer res = val1I.intValue() + val2I.intValue();
        if (res < 0) {
            return String.valueOf(res);
        } else if (res == 0) {
            return "";
        } else {
            return (avecPlus ? '+' : "") + String.valueOf(res);
        }
    }

    /**
     * Soustraction de chaîne de caractère qui représentent des entiers. Val1 et
     * val2 acceptent les "+". si le résultat vaut 0, alors la valeur renvoyée est "".
     *
     * @param val1 valeur 1
     * @param val2 valeur 2
     * @param avecPlus the avec plus
     *
     * @return somme de val1 et val2 ex : val1 = "2", val2 = "" val1 = "2", val2 = "+3"
     *         val1 = null, val2 = "" ...
     */
    public static String soustraction(String val1, String val2, boolean avecPlus) {
        final String val1Tr = org.apache.commons.lang.StringUtils.trimToNull(val1);
        final String val2Tr = org.apache.commons.lang.StringUtils.trimToNull(val2);
        final Integer val1I =
            (val1Tr == null) ? 0 : Integer.valueOf(val1Tr.replace("+", ""));
        final Integer val2I =
            (val2Tr == null) ? 0 : Integer.valueOf(val2Tr.replace("+", ""));
        final Integer res = val1I.intValue() - val2I.intValue();
        if (!avecPlus || (res < 0)) {
            return String.valueOf(res);
        } else if (res == 0) {
            return "";
        } else {
            return (avecPlus ? '+' : "") + String.valueOf(res);
        }
    }

    /**
     * Permet de conserver les zéros après la virgule facultatifs.
     *
     * @param n le double.
     * @param precision la précision à conserver.
     *
     * @return la chaîne.
     */
    public static String formatDouble(final Double n, final int precision) {
        if (n != null) {
            final String valeurChaine = n.toString();
            final List<String> lValeur = StringUtils.split(valeurChaine, "[.,]");
            if (lValeur.size() == 2) {
                final String decimal = lValeur.get(1);
                if (decimal.length() > precision) {
                    final String precisionCalculee = decimal.substring(0, precision);
                    return lValeur.get(0) +
                           (
                               (org.apache.commons.lang.StringUtils.isEmpty(precisionCalculee)) ? ""
                                                                        : (
                                                                            "." +
                                                                            precisionCalculee
                                                                        )
                           );
                } else if (decimal.length() < precision) {
                    return lValeur.get(0) + "." +
                           org.apache.commons.lang.StringUtils.rightPad(decimal, precision, '0');
                } else {
                    return n.toString();
                }
            } else {
                return n.toString();
            }
        } else {
            return null;
        }
    }

    /**
     * Formattage d'un double (valeur arrondie) avec des chiffres après la
     * virgule.
     *
     * @param n nombre à formatter
     * @param precision precision
     *
     * @return nombre formatté
     */
    public static String formatDoubleArrondie(final Double n, final int precision) {
        if (n == null) {
            return "";
        }
        final String formatApresVirgule = org.apache.commons.lang.StringUtils.leftPad("", precision, '0');
        final boolean afficherPoint = precision > 0;
        return StringUtils.formatString("{0,number,0" + (afficherPoint ? "." : "") +
                                        formatApresVirgule + "}", n).replace(",", ".");
    }

    /**
     * Formattage d'un double avec des chiffres après la virgule si chiffres
     * utiles (pas 0). Exemples, pour une précision de 2 : 1.11111 --> 1.11 1.0     -->
     * 1
     *
     * @param n nombre à formatter
     * @param maxPrecision precision maximum
     *
     * @return nombre formatté
     */
    public static String formatDoubleMaxPrecision(final Double n, final int maxPrecision) {
        if (n == null) {
            return "";
        }
        final String formatApresVirgule = org.apache.commons.lang.StringUtils.leftPad("", maxPrecision, '#');
        return StringUtils.formatString("{0,number,0." + formatApresVirgule + "}", n)
                          .replace(",", ".");
    }
}
