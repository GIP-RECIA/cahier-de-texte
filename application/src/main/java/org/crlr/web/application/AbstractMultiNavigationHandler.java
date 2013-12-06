/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AbstractMultiNavigationHandler.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.application;

import org.crlr.exception.base.CrlrRuntimeException;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import org.crlr.utils.Assert;

import java.util.Collection;
import java.util.HashSet;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

/**
 * Gestion de la navigation JSF, capable de traiter plusieurs stratégies de
 * navigation à travers une collection d'objets {@link SubNavigationHandler}. Pour
 * l'utiliser, créer une sous-classe dont le constructeur appelle la méthode {@link
 * #setSubNavigationHandlers(Collection)} avec une collection d'objets {@link
 * SubNavigationHandler}, puis déclarer la classe dans le fichier
 * <code>faces-config.xml</code>.
 *
 * @author romana
 */
public abstract class AbstractMultiNavigationHandler extends NavigationHandler {
    /** The log. */
    private final Log log = LogFactory.getLog(getClass());

    /** The base. */
    private final NavigationHandler base;

    /** The sub navigation handlers. */
    private Collection<SubNavigationHandler> subNavigationHandlers =
        new HashSet<SubNavigationHandler>();

/**
     * Construit une nouvelle instance. Le constructeur est appelé
     * automatiquement par JSF.
     * 
     * @param base
     *            {@link NavigationHandler} par défaut fourni par JSF
     */
    public AbstractMultiNavigationHandler(final NavigationHandler base) {
        super();
        Assert.isNotNull("base", base);
        this.base = base;
    }

    /**
     * Interroge la collection de {@link SubNavigationHandler} pour déléguer la
     * gestion de la navigation. Si aucun {@link SubNavigationHandler} n'a été capable
     * de traiter la navigation, celle-ci est traitée par la méthode {@link
     * #handleDefaultNavigation(FacesContext, String, String)}.
     *
     * @param ctx the ctx
     * @param actionMethod the action method
     * @param outcome the outcome
     */
    @Override
    public void handleNavigation(FacesContext ctx, String actionMethod, String outcome) {
        log.debug("Traitement de la navigation JSF : " + "actionMethod={0}, outcome={1}",
                  actionMethod, outcome);

        if (subNavigationHandlers != null) {
            for (final SubNavigationHandler handler : subNavigationHandlers) {
                if (handler == null) {
                    continue;
                }
                if (handler.canHandleNavigation(ctx, actionMethod, outcome)) {
                    log.debug("Délégation de la navigation JSF : " +
                              "{0}, actionMethod={1}, outcome={2}", handler,
                              actionMethod, outcome);
                    try {
                        handler.handleNavigation(ctx, actionMethod, outcome);
                    } catch (Exception e) {
                        log.error(e, "nav");
                        throw new CrlrRuntimeException(e,
                                                       "Erreur dans la gestion " +
                                                       "de la navigation");
                    }
                    return;
                }
            }

            log.debug("Navigation JSF par défaut : actionMethod={0}, outcome={1}",
                      actionMethod, outcome);
            handleDefaultNavigation(ctx, actionMethod, outcome);
        }
    }

    /**
     * Traite la navigation JSF, si aucun {@link SubNavigationHandler} n'a été
     * capable de le faire.
     *
     * @param ctx the ctx
     * @param actionMethod the action method
     * @param outcome the outcome
     */
    public void handleDefaultNavigation(FacesContext ctx, String actionMethod,
                                        String outcome) {
        base.handleNavigation(ctx, actionMethod, outcome);
    }

    /**
     * Retourne sub navigation handlers.
     *
     * @return the sub navigation handlers
     */
    public Collection<SubNavigationHandler> getSubNavigationHandlers() {
        return subNavigationHandlers;
    }

    /**
     * Affecte sub navigation handlers.
     *
     * @param subNavigationHandlers the sub navigation handlers
     */
    public void setSubNavigationHandlers(Collection<SubNavigationHandler> subNavigationHandlers) {
        this.subNavigationHandlers = subNavigationHandlers;
    }
}
