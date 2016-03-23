/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConteneurMessage.java,v 1.2 2010/04/29 09:16:41 jerome.carriere Exp $
 */

package org.crlr.message;

import java.io.Serializable;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Conteneur d'objets {@link Message}.
 *
 * @author breytond
 */
public class ConteneurMessage implements Serializable, Iterable<Message> {
    /** DOCUMENTATION INCOMPLETE! */
    private static final long serialVersionUID = -723534017459945960L;
    public static final String ID = "conteneurMessage";
    /** Listes des messages à destination de la couche présentation. */
    private Set<Message> messages = new LinkedHashSet<Message>(1);

    /**
     * Constructeur.
     */
    public ConteneurMessage(){
        
    }
    /**
     * Ajoute un {@link Message} dans le conteneur. Un message <code>null</code>
     * n'est pas accepté.
     *
     * @param message DOCUMENT ME!
     */
    public void add(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("message");
        }
        messages.add(message);
    }

    /**
     * Ajoute une collection de messages dans le conteneur.
     *  @param newMessages liste des messages.
     * @see #add(Message)
     */
    public void addAll(Collection<Message> newMessages) {
        if (messages == null) {
            return;
        }
        for (final Message msg : newMessages) {
            if (msg == null) {
                throw new IllegalArgumentException("Message null");
            }
            this.messages.add(msg);
        }
    }

    /**
     * Retourne la liste d'objets {@link Message} contenus.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public Set<Message> getMessages() {
        return messages;
    }

    /**
     * Mutateur.
     *
     * @param messages le messages pour le setter
     */
    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    /**
     * Itère sur la liste des objets {@link Message} contenus.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public Iterator<Message> iterator() {
        return messages.iterator();
    }

    /**
     * Retourne le nombre d'objets {@link Message} contenus.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public int size() {
        return messages.size();
    }

    /**
     * Supprime les objets {@link Message} contenus.
     */
    public void clear() {
        messages.clear();
    }

    /**
     * Teste si un {@link Message} présent dans le conteneur est de nature {@link
     * Message.Nature#BLOQUANT}.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public boolean contientMessageBloquant() {
        for (final Message msg : messages) {
            if (Message.Nature.BLOQUANT.equals(msg.getNature())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Teste si le conteneur ne contient pas de {@link Message}.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ConteneurMessage[messages=" + messages + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return messages.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ConteneurMessage)) {
            return false;
        }
        return messages.equals(((ConteneurMessage) obj).messages);
    }
}
