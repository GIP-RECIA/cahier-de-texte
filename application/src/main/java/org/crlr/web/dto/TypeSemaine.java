/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TypeJourEmploi.java,v 1.4 2010/04/19 13:35:00 ent_breyton Exp $
 */

package org.crlr.web.dto;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

/**
 * TypeJourEmploi. Neutre vaut 1 car lors d'une utilisation sans alternance de
 * l'emploi du temps le type neutre est utilisé.
 * 
 * @author breytond.
 * 
 */
public enum TypeSemaine {
    PAIR('1'), IMPAIR('2'), VAQUE('3'), NEUTRE('1');

    private static final Log log = LogFactory.getLog(TypeSemaine.class);

    /** Valeur. */
    private final Character valeur;

    /**
     * Constructeur.
     * 
     * @param valeur
     *            la valeur.
     */
    private TypeSemaine(final Character valeur) {
        this.valeur = valeur;
    }

    public static TypeSemaine getTypeSemaine(String s) {
        if (s.equals("1")) {
            return TypeSemaine.PAIR;
        } else if (s.equals("2")) {
            return TypeSemaine.IMPAIR;
        } else if (s.equals("3")) {
            return TypeSemaine.VAQUE;
        }

        log.warning("TypeSemaine pas trouvé");
        return TypeSemaine.NEUTRE;
    }

    public static TypeSemaine getTypeSemaine(Character c) {
        if (c == null) {
            return null;
        }
        return TypeSemaine.getTypeSemaine(c.toString());
    }

    /**
     * Accesseur valeur.
     * 
     * @return la valeur.
     */
    public Character getValeur() {
        return valeur;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.getValeur().toString();
    }

}
