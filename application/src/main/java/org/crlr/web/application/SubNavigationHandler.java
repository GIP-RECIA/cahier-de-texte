/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SubNavigationHandler.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.application;

import javax.faces.context.FacesContext;

/**
 * Extension de {@link AbstractMultiNavigationHandler}.
 * 
 * @author breytond
 */
public interface SubNavigationHandler {
    /**
     * Teste si cette classe est capable de traiter la navigation.
     *
     * @param ctx the ctx
     * @param actionMethod the action method
     * @param outcome the outcome
     *
     * @return true, if can handle navigation
     */
    boolean canHandleNavigation(FacesContext ctx, String actionMethod, String outcome);

    /**
     * Traite la navigation.
     *
     * @param ctx the ctx
     * @param actionMethod the action method
     * @param outcome the outcome
     *
     * @throws Exception L'exception.
     */
    void handleNavigation(FacesContext ctx, String actionMethod, String outcome)
                   throws Exception;
}
