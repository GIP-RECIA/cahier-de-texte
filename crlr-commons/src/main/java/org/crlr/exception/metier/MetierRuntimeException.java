/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: MetierRuntimeException.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.exception.metier;


import org.crlr.exception.base.CrlrRuntimeException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;

import org.crlr.utils.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception métier "runtime", liée à un conteneur de messages.
 *
 * @author breytond
 */
public class MetierRuntimeException extends CrlrRuntimeException {
    /** DOCUMENTATION INCOMPLETE! */
    private static final long serialVersionUID = 1092235803979941922L;

    /** Conteneur de message. */
    private final ConteneurMessage conteneurMessage;

/**
     * Construit une nouvelle instance.
     * @param conteneurMessage le conteneur de message.
     * @param msg le message.
     * @param args la liste des arguments.
     */
    public MetierRuntimeException(final ConteneurMessage conteneurMessage,
                                  final String msg, final Object... args) {
        super(msg, args);
        Assert.isNotNull("conteneurMessage", conteneurMessage);
        this.conteneurMessage = conteneurMessage;
    }

    /**
     * Retourne le {@link ConteneurMessage} associé.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public ConteneurMessage getConteneurMessage() {
        return conteneurMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        final String superMessage = super.getMessage();
        if ((superMessage == null) || (conteneurMessage.size() == 0)) {
            return superMessage;
        }
        final List<String> codes = new ArrayList<String>(conteneurMessage.size());
        for (final Message msg : conteneurMessage) {
            codes.add(msg.getCode());
        }
        return superMessage + " " + codes;
    }
}
