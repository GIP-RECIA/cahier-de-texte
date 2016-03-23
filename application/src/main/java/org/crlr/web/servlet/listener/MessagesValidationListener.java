/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: MessagesValidationListener.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.servlet.listener;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import org.crlr.utils.PropertiesUtils;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Initialisation des messages de validation. Ces messages sont chargés à partir d'un
 * fichier {@link Properties}.
 *
 * @author breytond
 */
public class MessagesValidationListener implements ServletContextListener {
    /** properties des messages de validation dans le {@link ServletContext}. */    
    private static Properties properties;    

    /** The log. */
    private final Log log = LogFactory.getLog(this.getClass());

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    /**
     * Context initialized.
     *
     * @param sce parameter
     */
    public void contextInitialized(ServletContextEvent sce) {
        final String path = "/validation.properties";
        this.log.info("Chargement des messages de validation : {0}", path);

        properties = PropertiesUtils.load(path);           
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    /**
     * Context destroyed.
     *
     * @param sce parameter
     */
    public void contextDestroyed(ServletContextEvent sce) {
        properties = null;
    }
    
    /**
     * Accesseur.
     * @return le fichier properties.
     */
    public static Properties getProperties() {
        return properties;
    }
}
