/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ExceptionUtils.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.utils;

import java.util.Map;

/**
 * Méthodes utilitaires pour {@link Exception}.
 *
 * @author romana
 */
public final class ExceptionUtils {
/**
 * The Constructor.
 */
    private ExceptionUtils() {
    }

    /**
     * Retourne la cause d'une {@link Exception}. La cause renvoyée sera de type
     * {@link Exception} : si ce n'est pas le cas (par exemple dans le cas d'une cause
     * avec une {@link Error}), la valeur <code>null</code> est renvoyée.
     *
     * @param e the e
     *
     * @return the cause
     */
    public static Exception getCause(Exception e) {
        if (e == null) {
            return null;
        }
        final Throwable cause = e.getCause();
        if ((cause == null) || !(cause instanceof Exception)) {
            return null;
        }
        return (Exception) cause;
    }

    /**
     * Retourne parmi la liste des causes la première {@link Exception} qui est
     * une instance de la classe <code>causeClass</code>. Si aucun élément n'est trouvé,
     * la valeur <code>null</code> est renvoyée.
     *
     * @param <T> type générique
     * @param e the e
     * @param causeClass the cause class
     *
     * @return the cause
     */
    @SuppressWarnings("unchecked")
    public static <T extends Exception> T getCause(Exception e, Class<T> causeClass) {
        Assert.isNotNull("causeClass", causeClass);
        if (e == null) {
            return null;
        }
        final Exception cause = getCause(e);
        if (cause == null) {
            return null;
        }
        if (!causeClass.isAssignableFrom(cause.getClass())) {
            return getCause(cause, causeClass);
        }
        return (T) cause;
    }

    /**
     * Retourne le contenu de la pile d'appel.
     *
     * @return the stack trace
     */
    public static String getStackTrace() {
        // piles d'appel de tous les thread
        final Map<Thread, StackTraceElement[]> threadStackTraces =
            Thread.getAllStackTraces();

        // récupération de la pile d'appel associé au thread courant
        final StackTraceElement[] elems = threadStackTraces.get(Thread.currentThread());

        // recherche du premier indice dans la pile d'appel correspondant au
        // code appelant
        // (l'indice qui suit l'indice de l'élément correspondant à l'appel de cette méthode)
        int first = 0;
        for (int i = 0; i < elems.length; ++i) {
            final StackTraceElement elem = elems[i];
            if (ExceptionUtils.class.getName().equals(elem.getClassName())) {
                first = i + 1;
                break;
            }
        }

        final StringBuilder buf = new StringBuilder();
        final int last = elems.length - 1;
        for (int i = first; i < elems.length; ++i) {
            final StackTraceElement elem = elems[i];
            buf.append(elem.toString());
            if (i != last) {
                buf.append('\n');
            }
        }
        return buf.toString();
    }
}
