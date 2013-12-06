/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AbstractLog.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.log.impl;

import org.crlr.log.Log;

import java.text.MessageFormat;

/**
 * Implémentation abstraite de {@link Log}.
 *
 * @author romana
 */
public abstract class AbstractLog implements Log {
/**
     * The Constructor.
     */
    protected AbstractLog() {
    }

    /**
     * Formate un message avec des arguments. Le nom de l'utilisateur est inséré
     * dans le message.
     *
     * @param msg the msg
     * @param args arguments.
     *
     * @return the string
     */
    protected String formaterMessage(String msg, Object... args) {
        final String finalMsg = insertContext(msg);

        if ((args == null) || (args.length == 0)) {
            return finalMsg;
        }

        // les apostrophes sont doublés pour corriger un comportement non
        // intuitif de MessageFormat : cf MessageFormatTest
        return MessageFormat.format(finalMsg.replaceAll("'", "''"), args);
    }

    /**
     * Insertion du nom de l'utilisateur actuellement connecté et du type
     * d'application. Si aucun utilisateur n'est connecté ou si le type n'est pas connu,
     * le message n'est pas modifié.
     *
     * @param msg the msg
     *
     * @return the string
     */
    private String insertContext(String msg) {
        return msg;
    }

    /**
     * {@inheritDoc}
     */
    public void debug(String msg, Object... args) {
        debug(null, msg, args);
    }

    /**
     * {@inheritDoc}
     */
    public void info(String msg, Object... args) {
        info(null, msg, args);
    }

    /**
     * {@inheritDoc}
     */
    public void warning(String msg, Object... args) {
        warning(null, msg, args);
    }

    /**
     * {@inheritDoc}
     */
    public void error(String msg, Object... args) {
        error(null, msg, args);
    }
}
