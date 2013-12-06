/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PropertiesUtils.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Properties;

import org.crlr.exception.base.CrlrRuntimeException;

/**
 * Méthodes utilitaires pour objets {@link Properties}.
 *
 * @author breytond
 * @author romana
 */
public final class PropertiesUtils {   

/**
     * The Constructor.
     */
    private PropertiesUtils() {
    }

    /**
     * Retourne un objet {@link Properties} initialisé à partir d'une ressource
     * chargée depuis le <code>ClassPath</code>.
     *
     * @param chemin de la ressource à charger
     * @param propertiesParDefaut utilisé pour les valeurs par défaut
     *
     * @return the properties
     */
    public static Properties load(String chemin, Properties propertiesParDefaut) {
        Assert.isNotNull("chemin", chemin);

        final Properties props = new Properties();
        final InputStream input = PropertiesUtils.class.getResourceAsStream(chemin);
        
        if (input == null) {
            throw new CrlrRuntimeException("Fichier .properties non disponible : {0}",
                    chemin);
        }
        
        final Reader reader; 
        try {
        	reader = new InputStreamReader(input, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
        	throw new CrlrRuntimeException("Fichier .properties non disponible : {0}",
                    chemin); 
        }
         
        try {
            // chargement du fichier Properties
            props.load(reader);
        } catch (IOException e) {
            throw new CrlrRuntimeException(e,
                    "Fichier .properties non disponible {0}",
                    chemin);
        } finally {
            IOUtils.close(input);
        }
        

        // traitement des valeurs par défaut
        if (propertiesParDefaut != null) {
            for (final Enumeration<?> noms = propertiesParDefaut.propertyNames();
                     noms.hasMoreElements();) {
                final String nom = (String) noms.nextElement();
                if (props.getProperty(nom) == null) {
                    props.setProperty(nom, propertiesParDefaut.getProperty(nom));
                }
            }
        }

        return props;
    }

    /**
     * Retourne un objet {@link Properties} initialisé à partir d'une ressource
     * chargée depuis le <code>ClassPath</code>.
     *
     * @param chemin de la ressource à charger
     *
     * @return the properties
     */
    public static Properties load(String chemin) {
        return load(chemin, null);
    }
}
