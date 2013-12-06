/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: MetierException.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.exception.metier;


import org.crlr.exception.base.CrlrException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception métier. Toutes les exceptions liées au métier doivent hériter de cette
 * classe.
 *
 * @author breytond
 */
public class MetierException extends CrlrException {
    /** serial version. */
    private static final long serialVersionUID = -299693403198216860L;

    /** Le conteneur de message. */
    private final ConteneurMessage conteneurMessage;

    /**
     * Constructeur.
     * @param conteneurMessage le conteneur de message.
     * @param msg le message.
     * @param args les arguments.
     */
    public MetierException(final ConteneurMessage conteneurMessage, final String msg,
                           final Object... args) {
        super(msg, args);
        this.conteneurMessage = conteneurMessage;
    }

    /**
     * Constructeur.
     * @param msg le message.
     * @param args les arguments.
     */
    public MetierException(final String msg, final Object... args) {
        this(null, msg, args);
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
        if ((superMessage == null) || (conteneurMessage == null || conteneurMessage.size() == 0)) {
            return superMessage;
        }
        final List<String> codes = new ArrayList<String>(conteneurMessage.size());
        for (final Message msg : conteneurMessage) {
            codes.add(msg.getCode());
        }
        return superMessage + " " + codes;
    }
}
