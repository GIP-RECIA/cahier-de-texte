/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: MenuNodeAction.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.application;

import org.apache.myfaces.custom.tree2.TreeNodeBase;

import java.io.Serializable;

/**
 * The Class MenuNodeAction.
 *
 * @author breytond
 */
public class MenuNodeAction extends TreeNodeBase implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 344053990014120671L;

    /** The Constant ACTION_SUFFIXE pour un outil du menu. */
    public static final String ACTION_SUFFIXE = "@menu";

    /**
     * The Constant ACTION_SUFFIXE pour un outil du menu liée à une action dans
     * l'arbre.
     */
    public static final String ACTION_ARBRE_SUFFIXE = "@arbre";

    /** The Constant ACTION_SUFFIXE pour un outil considéré comme un souos écran. */
    public static final String ACTION_SOUS_ECRAN_SUFFIXE = "@sousEcran";
    
    public static final String ACTION_CLIQUE_ARBRE = ACTION_SUFFIXE + ACTION_ARBRE_SUFFIXE;;

    /**
     * Action.
     *
     * @return the string
     */
    public String action() {
        return this.getIdentifier() + ACTION_SUFFIXE;
    }

    /**
     * Action en provenance d'un lien de l'arbre (une branche).
     *
     * @return l'identifiant de l'outil avec les suffixes adéquates.
     */
    public String actionArbre() {
        return this.getIdentifier() + ACTION_CLIQUE_ARBRE;
    }

    /**
     * Action en provenance d'une redirection d'un outil maître vers un sous
     * écran.
     *
     * @return l'identifiant de l'outil avec le suffixe sous écran adéquate.
     */
    public String actionSousEcran() {
        return this.getIdentifier() + ACTION_SOUS_ECRAN_SUFFIXE;
    }

/**
     * The Constructor.
     */
    public MenuNodeAction() {
        super();
    }

/**
     * The Constructor.
     * 
     * @param arg1
     *            the arg1
     * @param arg0
     *            the arg0
     * @param arg2
     *            the arg2
     */
    public MenuNodeAction(String arg0, String arg1, boolean arg2) {
        super(arg0, arg1, arg2);
    }

/**
     * The Constructor.
     * 
     * @param arg3
     *            the arg3
     * @param arg1
     *            the arg1
     * @param arg0
     *            the arg0
     * @param arg2
     *            the arg2
     */
    public MenuNodeAction(String arg0, String arg1, String arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }
}
