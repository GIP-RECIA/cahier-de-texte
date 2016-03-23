/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DateConverterHandler.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.facelets.ext;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.convert.Converter;
import javax.faces.view.facelets.ConverterConfig;
import javax.faces.view.facelets.ConverterHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.apache.commons.lang.StringUtils;

/**
 * Implémentation d'une balise utilisant {@link DateConverter}.
 *
 * @author burnichonj
 * @author romana
 */
public class DateConverterHandler extends ConverterHandler  {
    /** The log. */
    protected final transient Log log = LogFactory.getLog(getClass());

    /** The pattern attr. */
    private final TagAttribute patternAttr;

/**
     * The Constructor.
     * 
     * @param config
     *            the config
     */
   /* public DateConverterHandler(TagConfig config) {
        // ce constructeur sera supprimé lorsque Facelets sera mis à jour :
        // le super constructeur est déprécié, mais il est tout de même requis
        super(config);
        this.patternAttr = this.getAttribute("pattern");
    }*/

/**
     * The Constructor.
     * 
     * @param config
     *            the config
     */
    public DateConverterHandler(ConverterConfig config) {
        // lorsque l'autre constructeur sera supprimé, celui-ci deviendra le
        // constructeur principal
        //this((TagConfig) config);
        super(config);
        this.patternAttr = this.getAttribute("pattern");
    }

    /**
     * {@inheritDoc}
     */
    protected Converter createConverter(FaceletContext ctx)
                                 throws FacesException, FaceletException {
        log.debug("Création d'un DateConverter");
        final Application app = ctx.getFacesContext().getApplication();
        log.debug("Application : {0}", app);
        if (app == null) {
            return null;
        }

        DateConverter converter;
        try {
            converter = (DateConverter) app.createConverter(DateConverter.CONVERTER_ID);
        } catch (Exception e) {
            // une exception est levée si le DateConverter n'est pas déclaré
            // dans la configuration JSF
            converter = null;
        }
        if (converter == null) {
            log.debug("Ajout du DateConverter : id={0}, class={1}",
                      DateConverter.CONVERTER_ID, DateConverter.class.getName());
            app.addConverter(DateConverter.CONVERTER_ID, DateConverter.class.getName());
            converter = new DateConverter();
        }

        final String pattern =
            (patternAttr != null) ? StringUtils.trimToNull(patternAttr.getValue()) : null;
        if (pattern != null) {
            converter.setPattern(pattern);
        }
        log.debug("DateConverter : {0}", converter);
        return converter;
    }
}
