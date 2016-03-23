/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: IOUtils.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.utils;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Méthodes utilitaires pour la gestion des flux.
 *
 * @author romana
 */
public final class IOUtils {
/**
     * The Constructor.
     */
    private IOUtils() {
    }

    /**
     * Ferme un flux. Si la fermeture du flux entraîne la levée d'une exception,
     * celle-ci n'est pas renvoyée. Si l'argument <code>stream</code> vaut
     * <code>null</code>, la méthode ne fait rien.
     *
     * @param stream the stream
     */
    public static void close(Closeable stream) {
        if (stream == null) {
            return;
        }
        try {
            stream.close();
        } catch (IOException e) {
            final Log log = LogFactory.getLog(IOUtils.class);
            log.debug(e, "Erreur lors de la fermeture du flux");
        }
    }

    /**
     * Retourne un Reader à partir des infos envoyées.
     *
     * @param infos Informations à lire.
     *
     * @return l'objet Reader.
     *
     * @throws IOException the IO exception
     */
    public static BufferedReader getReader(byte[] infos)
                                    throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(infos);
        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        final BufferedReader bufferReader = new BufferedReader(inputStreamReader);

        return bufferReader;
    }

    /**
     * Combine un répertoire et son répertoire fils en associant correctement le
     * séparateur.
     *
     * @param path1 Répertoire parent
     * @param path2 Répertoire fils
     *
     * @return Association du répertoire parent et du répertoire fils.
     */
    public static String combine(String path1, String path2) {
        return new File(path1.replace('\\', '/'), path2.replace('\\', '/')).getPath();
    }
}
