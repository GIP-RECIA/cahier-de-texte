/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Report.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report;

/**
 * Édition. Une édition ne contient que des données statiques : le contenu de
 * l'édition a été généré par un {@link ReportGenerator}.
 * 
 * @author breytond
 */
public interface Report {
    /**
     * Nom de l'édition. Peut servir de nom de fichier.
     *
     * @return the name
     */
    String getName();

    /**
     * Extension par défaut d'un fichier.
     *
     * @return the extension
     */
    String getExtension();

    /**
     * Nom du fichier avec l'extension.
     *
     * @return the name with extension
     */
    String getNameWithExtension();

    /**
     * Type MIME de l'édition.
     *
     * @return the mime type
     */
    String getMimeType();

    /**
     * Retourne le Content Type du report.
     *
     * @return le content-type du report.
     */
    String getContentType();

    /**
     * Retourne le mode pour le contentDisposition.
     *
     * @return le mode pour content-disposition.
     */
    String getModeContentDisposition();

    /**
     * Données de l'édition.
     *
     * @return the data
     */
    byte[] getData();
}
