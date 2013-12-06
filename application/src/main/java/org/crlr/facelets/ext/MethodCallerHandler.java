/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: MethodCallerHandler.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.facelets.ext;

import java.io.IOException;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.apache.commons.lang.StringUtils;

/**
 * The Class MethodCallerHandler.
 */
public class MethodCallerHandler extends TagHandler {
    /** The log. */
    protected final transient Log log = LogFactory.getLog(getClass());

    /** The action. */
    private final TagAttribute action;

    /** The action. */
    private final TagAttribute rendered;

/**
     * The Constructor.
     * 
     * @param cfg
     *            the cfg
     */
    public MethodCallerHandler(final TagConfig cfg) {
        super(cfg);
        action = getRequiredAttribute("action");
        rendered = getAttribute("rendered");
    }

    /**
     * {@inheritDoc}
     */
    public void apply(FaceletContext ctx, UIComponent parent)
               throws IOException, FacesException, FaceletException, ELException {
        boolean doExecute = true;

        if (rendered != null) {
            final String renderedValue = StringUtils.trimToNull(rendered.getValue(ctx));
            doExecute = "true".equals(renderedValue);
            log.debug("Method caller Handler - apply - rendered (execute la méthode) = {0}",
                      doExecute);
        } else {
            log.debug("Method caller Handler - apply - rendered non renseigné");
        }

        if (doExecute) {
            final MethodExpression methode =
                action.getMethodExpression(ctx, Void.TYPE, new Class[] {  });
            
            methode.invoke( FacesContext.getCurrentInstance().getELContext(), new Object[] {  });
            //TODO Eric -- à vérifier
            //methode.invoke(ELAdaptor.getELContext(ctx.getFacesContext()),                           new Object[] {  });
        }
    }
}
