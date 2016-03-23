/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.importEDT.DTO;

/**
 * Enum répertoriant les jours de la semaine.
 * @author jp.mandrick
 *
 */
public enum EnumJourSemaine {LUNDI("1", "LUNDI"), MARDI("2", "MARDI"), 
    MERCREDI("3", "MERCREDI"), JEUDI("4", "JEUDI"), VENDREDI("5", "VENDREDI"), 
    SAMEDI("6", "SAMEDI"), DIMANCHE("7", "DIMANCHE");
    /**
     * Le numéro du jour.
     */
    private String numero;

    /**
     * Le libellé du jour.
     */
    private String jour;

    /**
     * Accesseur de numero.
     *
     * @return le numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Modificateur de numero.
     *
     * @param numero le numero à modifier
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * Accesseur de jour.
     *
     * @return le jour
     */
    public String getJour() {
        return jour;
    }

    /**
     * Modificateur de jour.
     *
     * @param jour le jour à modifier
     */
    public void setJour(String jour) {
        this.jour = jour;
    }

    /**
     * Constructeur de l'enum.
     * Constructeur de @param num : le numéro du jour.
     * Constructeur de @param jour : le libellé du jour.
     */
    private EnumJourSemaine(String num, String jour) {
        this.jour = jour;
        this.numero = num;
    }
}
