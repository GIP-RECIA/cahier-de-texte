/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Chrono.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.utils;

import org.apache.commons.lang.StringUtils;
import org.crlr.exception.base.CrlrRuntimeException;

import java.io.Serializable;

/**
 * Chronomètre. Permet de mesurer le temps entre deux actions, en millisecondes.
 * Cette classe est <i>thread-safe</i>.
 *
 * @author romana
 */
public class Chrono implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1869954178875988397L;

    /** The start time. */
    private long startTime;

    /** The elapsed. */
    private long elapsed;

    /** The running. */
    private boolean running;

/**
     * The Constructor.
     */
    public Chrono() {
        reset();
    }

    /**
     * Remet à zéro le chronomètre.
     */
    public synchronized void reset() {
        startTime = 0;
        elapsed = 0;
        running = false;
    }

    /**
     * Retourne si le chronomètre a été démarré.
     *
     * @return true, if is running
     */
    public synchronized boolean isRunning() {
        return running;
    }

    /**
     * Démarre le chronomètre.
     */
    public synchronized void start() {
        if (running) {
            throw new CrlrRuntimeException("Le chrono a déjà été démarré");
        }
        startTime = System.currentTimeMillis();
        running = true;
    }

    /**
     * Arrête le chronomètre.
     */
    public synchronized void stop() {
        if (!running) {
            throw new CrlrRuntimeException("Le chrono n'a pas été démarré");
        }
        final long stopTime = System.currentTimeMillis();
        elapsed = stopTime - startTime;
        running = false;
    }

    /**
     * Retourne le temps intermédiaire.
     *
     * @return the last time
     */
    public synchronized long getLastTime() {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * Retourne le temps mesuré par le chronomètre.
     *
     * @return the elapsed
     */
    public synchronized long getElapsed() {
        if (running) {
            throw new CrlrRuntimeException("Le chrono n'a pas été arrêté");
        }
        return elapsed;
    }

    /**
     * Exécute une interface {@link Runnable}, en mesurant le temps passé.
     *
     * @param runnable the runnable
     */
    public synchronized void run(Runnable runnable) {
        start();
        try {
            runnable.run();
        } catch (Exception e) {
            throw new CrlrRuntimeException(e, "Erreur durant l'exécution");
        } finally {
            stop();
        }
    }

    /**
     * Retourne la duree en XXh XXm XXs.
     *
     * @return duree au format humain.
     */
    public String getDureeHumain() {
        long nbMinutes = elapsed / 60000;
        long nbHeures = 0;
        if (nbMinutes >= 60) {
            nbHeures = nbMinutes / 60;
            nbMinutes = nbMinutes % 60;
        }
        final long nbSecondes = (elapsed % 60000) / 1000;
        final long nbMillisecondes = elapsed % 1000;
        String retour = "";
        if (nbHeures > 0) {
            retour += (nbHeures + "h ");
        }
        if ((nbHeures > 0) || (nbMinutes > 0)) {
            retour += (StringUtils.leftPad("" + nbMinutes, 2, '0') + "m ");
        }
        if ((nbHeures > 0) || (nbMinutes > 0) || (nbSecondes > 0)) {
            retour += (StringUtils.leftPad("" + nbSecondes, 2, '0') + "s ");
        }
        return retour + StringUtils.leftPad("" + nbMillisecondes, 3, '0') + "ms";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return elapsed + " ms";
    }
}
