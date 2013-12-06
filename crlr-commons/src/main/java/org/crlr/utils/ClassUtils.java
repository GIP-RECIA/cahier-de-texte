/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ClassUtils.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.utils;

import java.io.File;

/**
 * Méthodes utilitaires pour des classes.
 *
 * @author breytond
 */
public final class ClassUtils {
    
    private static String pathToReport = "/org/crlr/report/";
/**
     * Accesseur pathToReport.
     * @return le pathToReport.
     */
    public static String getPathToReport() {
        return pathToReport;
    }

/**
     * The Constructor.
     */
    private ClassUtils() {
    }

    /**
     * Retourne le chemin vers une ressource, par rapport à un paquetage d'une
     * classe.
     *
     * @param instance the instance
     * @param name the name
     *
     * @return the resource path
     */
    public static String getResourcePath(Object instance, String name) {
        return '/' + instance.getClass().getPackage().getName().replace('.', '/') + '/' +
               name;
    }

    /**
     * Retourne le chemin absolu d'une instance d'un objet.
     *
     * @param instance l'instance.
     *
     * @return le chemin.
     */
    public static String getAbsolutePath(Object instance) {
        final java.security.ProtectionDomain pd = instance.getClass().getProtectionDomain();
        if (pd == null) {
            return null;
        }
        final java.security.CodeSource cs = pd.getCodeSource();
        if (cs == null) {
            return null;
        }
        final java.net.URL url = cs.getLocation();
        if (url == null) {
            return null;
        }
        final java.io.File f = new File(url.getFile());
        

        return f.getAbsolutePath();
    }
}
