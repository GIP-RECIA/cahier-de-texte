/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TemplateUtils.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import groovy.lang.Writable;

import groovy.text.GStringTemplateEngine;
import groovy.text.Template;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import org.crlr.report.ReportException;

import org.crlr.utils.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.io.StringWriter;

import java.net.URL;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Méthodes utilitaires pour la gestion des modèles d'édition.
 *
 * @author breytond
 */

public final class TemplateUtils {
/**
     * The Constructor.
     */
    private TemplateUtils() {
    }

    /**
     * Applique un modèle d'édition avec des paramètres. Le type de modèle
     * utilisé est déterminé par l'extension du fichier.<p>Types de modèles
     * supportés :</p>
     *  <ul>
     *      <li>Groovy (extension <code>.groovy</code>),</li>
     *      <li>Velocity (extension <code>.vm</code>).</li>
     *  </ul>
     *
     * @param templateLocation the template location
     * @param values the values
     * @param parameters the parameters
     *
     * @return the char sequence
     *
     * @throws Exception the exception
     */
    public static CharSequence mergeTemplate(String templateLocation,
                                             Collection<Object> values,
                                             Map<String, Object> parameters)
                                      throws Exception {
        final String myTemplateLocation = StringUtils.trimToNull(templateLocation);
        if (myTemplateLocation == null) {
            return null;
        }

        final Collection<Object> myModel =
            ObjectUtils.defaultIfNull(values, Collections.emptyList());
        final Map<String, Object> myParameters =
            ObjectUtils.defaultIfNull(parameters, new HashMap<String, Object>(0));

        if (isVelocityTemplate(myTemplateLocation)) {
            return mergeVelocityTemplate(myTemplateLocation, myModel, myParameters);
        }
        if (isGroovyTemplate(myTemplateLocation)) {
            return mergeGroovyTemplate(myTemplateLocation, myModel, myParameters);
        }

        throw new ReportException("Type de modèle d'édition non supporté : {0}",
                                  templateLocation);
    }

    /**
     * Checks if is velocity template.
     *
     * @param templateLocation the template location
     *
     * @return true, if is velocity template
     */
    private static boolean isVelocityTemplate(String templateLocation) {
        return templateLocation.toLowerCase().endsWith(".vm");
    }

    /**
     * Merge velocity template.
     *
     * @param templateLocation the template location
     * @param model the model
     * @param parameters the parameters
     *
     * @return the char sequence
     *
     * @throws Exception the exception
     */
    private static CharSequence mergeVelocityTemplate(String templateLocation,
                                                      Collection<Object> model,
                                                      Map<String, Object> parameters)
                                               throws Exception {
        // Velocity ne peut pas charger de script si le chemin commence par
        // un slash '/'
        final String myTemplateLocation =
            templateLocation.startsWith("/") ? templateLocation.substring(1)
                                             : templateLocation;

        final VelocityEngine engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath.resource.loader.class",
                           ClasspathResourceLoader.class.getName());
        engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
                NullLogChute.class.getName());
        engine.init();

        final Map<String, Object> templateModel =
            new HashMap<String, Object>(model.size() + parameters.size());
        templateModel.putAll(parameters);
        templateModel.put("values", model);

        final VelocityContext ctx = new VelocityContext(templateModel);
        final StringWriter writer = new StringWriter();
        engine.mergeTemplate(myTemplateLocation, "UTF-8", ctx, writer);
        return writer.getBuffer();
    }

    /**
     * Checks if is groovy template.
     *
     * @param templateLocation the template location
     *
     * @return true, if is groovy template
     */
    private static boolean isGroovyTemplate(String templateLocation) {
        return templateLocation.toLowerCase().endsWith(".groovy");
    }

    /**
     * Merge groovy template.
     *
     * @param templateLocation the template location
     * @param model the model
     * @param parameters the parameters
     *
     * @return the char sequence
     *
     * @throws Exception the exception
     */
    private static CharSequence mergeGroovyTemplate(String templateLocation,
                                                    Collection<Object> model,
                                                    Map<String, Object> parameters)
                                             throws Exception {
        final URL url = TemplateUtils.class.getResource(templateLocation);
        if (url == null) {
            throw new ReportException("Modèle d'édition introuvable : {0}",
                                      templateLocation);
        }

        final GStringTemplateEngine engine = new GStringTemplateEngine();
        final Template template = engine.createTemplate(url);

        final Map<String, Object> templateModel =
            new HashMap<String, Object>(model.size() + parameters.size());
        templateModel.putAll(parameters);
        templateModel.put("values", model);

        final Writable writable = template.make(templateModel);
        final StringWriter writer = new StringWriter();
        writable.writeTo(writer);
        return writer.getBuffer();
    }
}
