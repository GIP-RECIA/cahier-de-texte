/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: MessageUtils.java,v 1.3 2009/04/28 12:45:50 ent_breyton Exp $
 */

package org.crlr.web.utils;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.utils.Assert;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.servlet.listener.MessagesValidationListener;
import org.springframework.util.CollectionUtils;

/**
 * Méthodes utilitaires pour la gestion d'objets {@link Message}.
 *
 * @author breytond
 */
public final class MessageUtils {
    protected static final Log log = LogFactory.getLog(MessageUtils.class);
    
/**
 * 
     * The Constructor.
     */
    private MessageUtils() {
    }

    /**
     * Convertit un objet {@link Message} en message JSF {@link FacesMessage}.
     *
     * @param message the message
     *
     * @return the faces message
     */
    public static FacesMessage convert(Message message) {
        Assert.isNotNull("message", message);

        final Properties patterns = getFichierValidation();

        final FacesMessage facesMsg = new FacesMessage();

        if (Message.Nature.BLOQUANT.equals(message.getNature())) {
            facesMsg.setSeverity(FacesMessage.SEVERITY_ERROR);
        } else if (Message.Nature.AVERTISSANT.equals(message.getNature())) {
            facesMsg.setSeverity(FacesMessage.SEVERITY_WARN);
        } else {
            facesMsg.setSeverity(FacesMessage.SEVERITY_INFO);
        }

        final String msgPattern = patterns.getProperty(message.getCode());
        final String texte;
        if (msgPattern != null) {
            texte = MessageFormat.format(msgPattern.replaceAll("'", "''"),
                                         message.getParametres());
        } else {
            texte = message.getCode();
        }
        facesMsg.setDetail(texte);
        facesMsg.setSummary(texte);

        return facesMsg;
    }
    
    /**
     * Convertit un objet {@link Message} en message JSF {@link FacesMessage}.
     *
     * @return true, if has message
     *
     * @see #convert(Message, FacesContext)
     */
    public static Boolean hasMessage() {
        final Iterator<?> msg = FacesContext.getCurrentInstance().getMessages();
        if (msg != null) {
            return msg.hasNext();
        }
        return false;
    }
    
    public static Boolean hasMessageBloquant() {
        final Iterator<FacesMessage> msg = FacesContext.getCurrentInstance().getMessages();
        if (msg == null) {
            return null;
        }
        
        while(msg.hasNext()) {
            FacesMessage message = msg.next();
            if (FacesMessage.SEVERITY_INFO.compareTo(message.getSeverity()) < 0) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Teste si le message est présent.
     *
     * @param cm DOCUMENT ME!
     * @param code code du message
     *
     * @return true si le conteneur de messages contient ce message.
     */
    public static boolean contientMessage(final ConteneurMessage cm, String code) {
        for (final Message msg : cm) {
            if (code.equals(msg.getCode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Récupération du fichier properties contenant les messages de validation.
     *
     * @return the fichier validation
     */
    public static Properties getFichierValidation() {
        return 
            ObjectUtils.defaultIfNull(MessagesValidationListener.getProperties(), new Properties());
    }

    /**
     * Récupération du texte d'une règle dont on fournit le code.
     *
     * @param code the code
     *
     * @return the texte regle by code
     */
    public static String getTexteRegleByCode(final String code) {
        final Properties fichierValidation =
            getFichierValidation();
        if (fichierValidation == null) {
            return null;
        } else {
            return fichierValidation.getProperty(code);
        }
    }
    
    /**
     * Retourne le texte représentant la règle du message.
     * @param code le code.
     * @param parametres les paramètres.
     * @return le texte.
     */
    public static String getTexteRegleByCode(final String code, final Object[] parametres) {
        final Properties fichierValidation =
            getFichierValidation();
        if (fichierValidation == null) {
            return null;
        } else {
            return MessageFormat.format(ObjectUtils.defaultIfNull(fichierValidation.getProperty(code), "").replaceAll("'", "''"),
                    parametres);
        }
        
    }


    
    /**
     * Ajoute un objet {@link Message} dans la zone de messages.
     * @param clazz la classe.
     * @param <T> le type de la classe.
     * @param msg le message à ajouter.
     */
    public static <T> void  addMessage(Message msg, final Class<T> clazz) {   
        final FacesContext fc = FacesContext.getCurrentInstance();
        final FacesMessage facesMessage = generateMessage(msg, clazz);
        if (facesMessage != null) {
            fc.addMessage(null, facesMessage);
            log.error("adding faces message ", fc.toString());
        }
    }
    
    /**
     * Génération d'un message.
     * @param <T> le type.
     * @param msg le message.
     * @param clazz la classe.
     * @return le message.
     */
    private static <T> FacesMessage generateMessage(final Message msg, final Class<T> clazz) {
        final Log log = LogFactory.getLog(clazz);
        final FacesMessage facesMsg = convert(msg);
        if (facesMsg == null) {
            log.warning("Message non traité : {0}", msg);
            return facesMsg;
        }

        log.debug("Ajout du message : {0}", msg);
        final FacesContext fc = FacesContext.getCurrentInstance();
        for (final Iterator<FacesMessage> i = fc.getMessages(); i.hasNext();) {
            final FacesMessage tmpFacesMsg = i.next();
            if (equals(tmpFacesMsg, facesMsg)) {
                log.debug("Le message a déjà été traité " +
                               "(il ne sera pas ajouté une seconde fois) : {0}", msg);
                return null;
            }
        }
        
        return facesMsg;
    }    

    /**
     * Teste si deux objets {@link FacesMessage} sont égaux.
     *
     * @param a the a
     * @param b the b
     *
     * @return true, if equals
     */
    private static boolean equals(FacesMessage a, FacesMessage b) {
        return EqualsBuilder.reflectionEquals(a, b);
    }  
    
    /**
     * Ajouts des messages.
     * @param <T> le type.
     * @param cm le conteneur de message.
     * @param e l'exception.
     * @param clazz la classe.
     */
    public static <T> void addMessages(final ConteneurMessage cm, final Exception e, final Class<T> clazz) {
        final Set<FacesMessage> messages = handleMessages(cm, e, clazz);
        final FacesContext fc = FacesContext.getCurrentInstance();
        if (!CollectionUtils.isEmpty(messages)) {
            for (final FacesMessage facesMessage : messages) {
                
                fc.addMessage(null, facesMessage);
            }
        }
    }
    
    /**
     * Handle messages.
     *
     * @param <T> le type.
     * @param cm the cm.
     * @param e the e.
     * @param clazz la classe.
     * 
     * @return la liste de messages.
     */
    private static <T> Set<FacesMessage> handleMessages(final ConteneurMessage cm, final Exception e, final Class<T> clazz) {
        final Set<FacesMessage> messages = new HashSet<FacesMessage>();
        final Log log = LogFactory.getLog(clazz);
        if ((cm == null) || (cm.size() == 0)) {
            if (e != null) {
                final Message msg =
                    new Message(Message.Code.ERREUR_INCONNUE.getId(),
                                ObjectUtils.defaultIfNull(e.getMessage(),
                                                          e.getClass().getName()));
                final FacesMessage facesMessage = generateMessage(msg, clazz);
                if (facesMessage != null) {
                    messages.add(facesMessage);
                }
            }
            return messages;
        }

        log.info("Traitement des messages du conteneur : {0}", cm);

        for (final Message msg : cm) {
            final FacesMessage facesMessage = generateMessage(msg, clazz);
            if (facesMessage != null) {
                messages.add(facesMessage);
            }
        }
        return messages;
    }    
    
    /**
     * Retourne la liste de tout les codes contenus dans le conteneur de message.
     * @param cm le conteneur.
     * @return la liste des messages.
     */
    public static Set<String> getAllCodeMessage(final ConteneurMessage cm) {
        final Set<String> liste = new HashSet<String>();
        
        if (cm != null && cm.contientMessageBloquant()) {
            for (final Message msg : cm) {
                liste.add(msg.getCode());
            }
        }
        
        return liste;
    }
}
