/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ContexteOutil.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.contexte;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * ContexteOutil représente le contexte des données utilisées dans un outil. Ces
 * données, stockées manuellement grâce à un contrôleur spécifique, sont alors
 * réutilisables au délà d'une simple portée 'request', jusqu'à leur demande de
 * suppression. Une donnée est répérable par une clé.
 *
 * @author breytond
 */
@ManagedBean(name="contexteOutil")
@SessionScoped
public class ContexteOutil implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant ID. */
    public static final String ID = "contexteOutil";

    /** The liste donnees. */
    private Map<String, Object> listeDonnees;

/**
     * The Constructor.
     */
    public ContexteOutil() {
        reset();
    }

    /**
     * (Ré)initialisation de la liste des données contenues dans le contexte
     * d'outil.
     */
    public void reset() {
        if (listeDonnees == null) {
            listeDonnees = new HashMap<String, Object>();
        } else {
            listeDonnees.clear();
        }
    }

    /**
     * Ajout d'une donnée dans le contexte outil.
     *
     * @param cle the cle
     * @param objet the objet
     */
    public void add(String cle, Object objet) {
        if (objet != null) {
            listeDonnees.put(cle, objet);
        }
    }

    /**
     * Suppression d'une donnée dans le contexte outil.
     *
     * @param cle the cle
     */
    public void remove(String cle) {
        if (listeDonnees.containsKey(cle)) {
            listeDonnees.remove(cle);
        }
    }

    /**
     * Récupération d'une donnée dans le contexte outil.
     *
     * @param cle the cle
     *
     * @return the object
     */
    public Object get(String cle) {
        if (listeDonnees.containsKey(cle)) {
            return listeDonnees.get(cle);
        } else {
            return null;
        }
    }

    /**
     * Indique si le contexte possède un objet associé à la clé fournie en
     * paramètre.
     *
     * @param cle clé associé à l'objet
     *
     * @return true, if contains
     */
    public boolean contains(String cle) {
        return listeDonnees.containsKey(cle);
    }
}
