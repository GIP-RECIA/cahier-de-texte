/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SchemaUtils.java,v 1.1 2009/05/26 08:06:30 ent_breyton Exp $
 */

package org.crlr.metier.utils;

import org.crlr.alimentation.Archive;
import org.crlr.utils.BooleanUtils;

/**
 * Classe utilitaitre de gestion des schémas de la base de données.
 *
 * @author breytond
 * @version $Revision: 1.1 $
  */
public final class SchemaUtils {
    /** Schéma par défaut. */
    private static String defaultSchema = "cahier_courant";
    /**
     * Constructeur protégé.
     */
    private SchemaUtils() {
    }

    /**
     * Retourne la table prefixée du schéma.
     *
     * @param schema le schéma.
     * @param table la table
     *
     * @return la table prefixée du schéma.
     */
    public static String getTableAvecSchema(final String schema, final String table) {
        return "\"" + schema + "\"." + table;
    }
    
    /**
     * Accesseur au schéma par défaut.
     * @return le schéma courant.
     */
    public static String getDefaultSchema() {
        return defaultSchema;
    }
    
    /**
     * Retourne le schéma en cours ou un schéma archivé.
     * @param archive true pour un schéma archivé
     * @param exercice l'exercice du schéma archivé.
     * @return le schéma.
     */
    public static String getSchemaCourantOuArchive(final Boolean archive, final String exercice) {
        final String schema;
        if (BooleanUtils.isTrue(archive)) {
            schema = Archive.PREFIX_SCHEMA_ARCHIVE + exercice;
        } else {
            schema = defaultSchema;
        }
        return schema;
    }
}
