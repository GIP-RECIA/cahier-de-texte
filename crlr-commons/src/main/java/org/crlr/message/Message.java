/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Message.java,v 1.2 2010/04/29 09:16:41 jerome.carriere Exp $
 */

package org.crlr.message;

import java.io.Serializable;

import java.util.Arrays;

/**
 * Représentation d'un message par un code (clé d'un message), des paramètres (du
 * message), une nature. Le code et la nature du message sont obligatoires : une valeur
 * <code>null</code> n'est pas acceptée.
 *
 * @author breytond
 *
 * @see Nature
 */
public class Message implements Serializable, Comparable<Message> {
    /** DOCUMENTATION INCOMPLETE! */
    private static final long serialVersionUID = -604398828955359396L;

    /** DOCUMENTATION INCOMPLETE! */
    private final long timestamp = System.currentTimeMillis();

    /** le code du message. */
    private  String code;

    /** La nature (Informatif, Avertissant, Bloquant). */
    private  Nature nature;

    /** les paramétres. */
    private  Object[] parametres;

    /**
     * Contructeur.
     */
    public Message(){
        
    }
    
    /**
     * Construit une instance de {@link Message}. La nature du message est
     * initialisée à {@link Nature#BLOQUANT}.
     * @param code utilisé pour le code du message.
     * @param parametres les objets.
     * @see #Message(String, Nature, Object[])
     */
    public Message(final String code, final Object... parametres) {
        this(code, Nature.BLOQUANT, parametres);
    }

/**
     * Construit une instance de {@link Message}.
     * @param code du message
     * @param nature du message
     * @param parametres du message
     */
    public Message(final String code, final Nature nature, final Object... parametres) {
        if ((code == null) || (code.trim().length() == 0)) {
            throw new IllegalArgumentException("code");
        }
        if (nature == null) {
            throw new IllegalArgumentException("nature");
        }
        this.nature = nature;
        this.code = code;
        this.parametres = (parametres == null) ? new Object[0] : parametres;
    }

/**
     * Construit une instance de {@link Message}.
     * @param code utilisé pour le code du message.
     * @param nature nature du message. 
     * @param parametres les objets.
     * @see #Message(String, Nature, Object[])
     */
    public Message(final Code code, final Nature nature, final Object... parametres) {
        this(code.getId(), nature, parametres);
    }

/**
     * Construit une instance de {@link Message}. La nature du message est
     * initialisée à {@link Nature#BLOQUANT}.
     * @param code utilisé pour le code du message.
     * @param parametres les objets.
     * @see #Message(String, Nature, Object[])
     */
    public Message(final Code code, final Object... parametres) {
        this(code, Nature.BLOQUANT, parametres);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Message[code=" + code + ", nature=" + nature + ", parametres=" +
               Arrays.asList(parametres) + ", timestamp=" + timestamp + ']';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof Message)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return hashCode() == obj.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(Message that) {
        if (!this.nature.equals(that.nature)) {
            return this.nature.compareTo(that.nature);
        }
        if (this.timestamp == that.timestamp) {
            return 0;
        }
        return (this.timestamp < that.timestamp) ? (-1) : 1;
    }

/**
     * Nature d'un message.
     *
     * @author romana
     */
    public static enum Nature {BLOQUANT, AVERTISSANT, INFORMATIF;
    }

/**
     * Code d'un message. La liste des codes contenus dans ce type énumératif
     * n'est nullement exhaustive.
     *
     * @author breytond
     */
    public enum Code { //
        ERREUR_INCONNUE("erreur.inconnue");
        //
        private final String id;

/**
         * Constructeur.
         * @param id l'identifiant.
         */
        private Code(final String id) {
            this.id = id;
        }

        /**
         * Accesseur.
         *
         * @return l'id.
         */
        public String getId() {
            return id;
        }
    }

    /**
     * Accesseur timestamp.
     *
     * @return le timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Accesseur code.
     *
     * @return le code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Accesseur nature.
     *
     * @return le nature.
     */
    public Nature getNature() {
        return nature;
    }

    /**
     * Accesseur parametres.
     *
     * @return le parametres.
     */
    public Object[] getParametres() {
        return parametres;
    }
}
