/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ContexteUtils.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.contexte.utils;

import org.crlr.web.application.control.ContexteOutilControl;
import org.crlr.web.application.control.MenuControl;
import org.crlr.web.contexte.ContexteApplication;
import org.crlr.web.contexte.ContexteOutil;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.utils.FacesUtils;

import javax.faces.context.FacesContext;

/**
 * Classe utilitaire permettant d'accèder aux diverses contextes.
 *
 * @author breytond.
 * @version $Revision: 1.1 $
 */
public final class ContexteUtils {
/**
     * Le Constructeur.
     */
    private ContexteUtils() {
    }

    /**
     * Retourne l'instance  du contrôleur d'outil permettant la gestion du
     * contexte d'outil. celui-ci autorise la gestion manuelle des données de l'outil en
     * session (stockage, récupération, suppression).
     *
     * @return l'instance du contrôleur du contexte outil.
     */
    public static ContexteOutilControl getContexteOutilControl() {
        return (ContexteOutilControl) FacesUtils.resolveVariable(ContexteOutilControl.ID);
    }

    /**
     * Retourne l'instance  du contrôleur d'outil permettant la gestion du
     * contexte d'outil. celui-ci autorise la gestion manuelle des données de l'outil en
     * session (stockage, récupération, suppression).
     *
     * @param fc le contexte faces.
     *
     * @return l'instance du contrôleur du contexte outil.
     */
    public static ContexteOutilControl getContexteOutilControl(final FacesContext fc) {
        return (ContexteOutilControl) FacesUtils.resolveVariable(fc,
                                                                 ContexteOutilControl.ID);
    }

    /**
     * Retourne l'instance  du contexte d'outil.  celui-ci autorise la gestion
     * manuelle des données de l'outil en session (stockage, récupération, suppression).
     *
     * @return l'instance du contexte outil.
     */
    public static ContexteOutil getContexteOutil() {
        return getContexteOutilControl().getContexteOutil();
    }

    /**
     * Retourne l'instance  du contexte d'outil.  celui-ci autorise la gestion
     * manuelle des données de l'outil en session (stockage, récupération, suppression).
     *
     * @param fc le contexte faces.
     *
     * @return l'instance du contexte outil.
     */
    public static ContexteOutil getContexteOutil(final FacesContext fc) {
        return getContexteOutilControl(fc).getContexteOutil();
    }

    /**
     * Retourne le contexte utilisateur.
     *
     * @return le contexte utilisateur.
     */
    public static ContexteUtilisateur getContexteUtilisateur() {
        return (ContexteUtilisateur) FacesUtils.resolveVariable(ContexteUtilisateur.ID);
    }

    /**
     * Retourne le contexte utilisateur.
     *
     * @param fc le contexte faces.
     *
     * @return le contexte utilisateur.
     */
    public static ContexteUtilisateur getContexteUtilisateur(final FacesContext fc) {
        return (ContexteUtilisateur) FacesUtils.resolveVariable(fc, ContexteUtilisateur.ID);
    }
    
    /**
     * Retourne le menu control.
     *
     * @return le menu control.
     */
    public static MenuControl getMenuControl() {
        return (MenuControl) FacesUtils.resolveVariable(MenuControl.ID);
    }
    
    /**
     * Retourne le menu control.
     *
     * @param fc le contexte faces.
     *
     * @return le menu control.
     */
    public static MenuControl getMenuControl(final FacesContext fc) {
        return (MenuControl) FacesUtils.resolveVariable(fc, MenuControl.ID);
    }
    
    /**
     * Retourne le contexte application.
     *
     * @param fc le contexte faces.
     *
     * @return le contexte application.
     */
    public static ContexteApplication getContexteApplication(final FacesContext fc) {
        return (ContexteApplication) FacesUtils.resolveVariable(fc, ContexteApplication.ID);
    }
    
    /**
     * Retourne le contexte application.     
     *
     * @return le contexte application.
     */
    public static ContexteApplication getContexteApplication() {
        return (ContexteApplication) FacesUtils.resolveVariable(ContexteApplication.ID);
    }
}
