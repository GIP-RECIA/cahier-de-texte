/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: HttpSessionUtils.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.utils;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import javax.servlet.http.HttpSession;

/**
 * Méthodes utilitaires pour des objets {@link HttpSession}.
 *
 * @author romana
 */
public final class HttpSessionUtils {
/**
     * The Constructor.
     */
    private HttpSessionUtils() {
    }

    /**
     * Invalide une session. Si la session est déjà invalidée, l'exception {@link
     * IllegalStateException} est capturée automatiquement.
     *
     * @param session the session
     */
    public static void invalidate(HttpSession session) {
        if (session == null) {
            return;
        }
        try {
            session.invalidate();
        } catch (IllegalStateException e) {
            // la session était déjà invalidée
            final Log log = LogFactory.getLog(HttpSessionUtils.class);
            log.debug(e, "Erreur lors de l'invalidation de la session");
        }
    }
}
