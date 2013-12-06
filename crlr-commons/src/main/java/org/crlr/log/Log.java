/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Log.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.log;

/**
 * Interface de <code>log</code>. Chaque méthode supporte un nombre variable
 * d'arguments utilisés pour formater un message. Le format du message respecte
 * les conventions établies dans la classe {@link java.text.MessageFormat}
 * (excepté que les apostrophes sont doublés).
 * <p>
 * Le nom de l'utilisateur disponible dans le {@link ContexteService}, s'il est
 * disponible, est automatiquement inséré dans le message.
 * </p>
 * <p>
 * Exemple d'utilisation :
 * <code>log.info(e, "Erreur d'accès pour {0}", utilisateur.getIdentifiant());</code>
 * </p>
 * <p>
 * Utiliser la classe {@link LogFactory} pour obtenir une instance de
 * {@link Log}.
 * </p>
 * 
 * @author romana
 */
public interface Log {
    /**
     * Niveau <code>DEBUG</code>. Utilisé pour tracer du code.<p>Exemple :</p>
     *
     * @param cause the cause
     * @param msg the msg
     * @param args arguments.
     */
    void debug(Throwable cause, String msg, Object... args);

    /**
     * Debug.
     *
     * @param msg the msg
     * @param args arguments.
     *
     * @see #debug(Throwable, String, Object[])
     */
    void debug(String msg, Object... args);

    /**
     * Niveau <code>INFO</code>. Utilisé pour des messages d'information à
     * destination d'un administrateur ou d'un développeur.<p>Exemple : création
     * d'un cache pour l'objet X, initialisation de la classe Y, etc...</p>
     *
     * @param cause the cause
     * @param msg the msg
     * @param args arguments.
     */
    void info(Throwable cause, String msg, Object... args);

    /**
     * Info.
     *
     * @param msg the msg
     * @param args arguments.
     *
     * @see #info(Throwable, String, Object[])
     */
    void info(String msg, Object... args);

    /**
     * Niveau <code>WARNING</code>. Utilisé pour des messages importants qui ne
     * remettent pas en cause le déroulement d'un processus.<p>Exemple : objet
     * non trouvé dans un cache, etc...</p>
     *
     * @param cause the cause
     * @param msg the msg
     * @param args arguments.
     */
    void warning(Throwable cause, String msg, Object... args);

    /**
     * Warning.
     *
     * @param msg the msg
     * @param args arguments.
     *
     * @see #warning(Throwable, String, Object[])
     */
    void warning(String msg, Object... args);

    /**
     * Niveau <code>ERROR</code> Utilisé pour notifier une erreur, qui a pour
     * conséquence d'interrompre ou de corrompre le processus en cours.<p>Exemple
     * : erreur de cohérence dans la base de donnèes, élément requis introuvable, etc...</p>
     *
     * @param cause the cause
     * @param msg the msg
     * @param args arguments.
     */
    void error(Throwable cause, String msg, Object... args);

    /**
     * Error.
     *
     * @param msg the msg
     * @param args arguments.
     *
     * @see #error(Throwable, String, Object[])
     */
    void error(String msg, Object... args);
}
