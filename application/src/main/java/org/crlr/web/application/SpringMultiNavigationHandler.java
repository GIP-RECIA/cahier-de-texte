/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SpringMultiNavigationHandler.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.application;

import org.springframework.context.ApplicationContext;

import org.springframework.web.jsf.FacesContextUtils;

import java.util.Collection;
import java.util.Map;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

/**
 * Gestion de la navigation JSF, qui obtient la collection d'objets {@link
 * SubNavigationHandler} à partir du contexte Spring. Tous les <code>beans</code>
 * déclarés dans la configuration Spring qui sont de type {@link SubNavigationHandler}
 * seront consultés pour traiter la navigation.
 *
 * @author romana
 */
public class SpringMultiNavigationHandler extends AbstractMultiNavigationHandler {
/**
     * The Constructor.
     * 
     * @param base
     *            the base
     */
    public SpringMultiNavigationHandler(final NavigationHandler base) {
        super(base);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleNavigation(FacesContext ctx, String actionMethod, String outcome) {
        final ApplicationContext appCtx =
            FacesContextUtils.getRequiredWebApplicationContext(ctx);
        final Map<String, SubNavigationHandler> handlerMap =
            appCtx.getBeansOfType(SubNavigationHandler.class);
        final Collection<SubNavigationHandler> handlers = handlerMap.values();
        setSubNavigationHandlers(handlers);

        super.handleNavigation(ctx, actionMethod, outcome);
    }
}
