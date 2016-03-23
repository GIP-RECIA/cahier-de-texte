/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GarbageUtils.java,v 1.1 2010/03/09 14:29:13 ent_breyton Exp $
 */

package org.crlr.utils;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

/**
 * GarbageUtils.
 * 
 * @author $author$
 * @version $Revision: 1.1 $
 */
public final class GarbageUtils {    
    /**
     * Fixe la mémoire maxi à atteindre en méga octets.
     */
    private static final int LIMITEMEMOIRE = 4;
    
    private static final Log log = LogFactory.getLog(GarbageUtils.class);

    /**
     * The Constructor.
     */
    private GarbageUtils() {
    }

    /**
     * fonction qui permet de vider le garbage collector en fonction d'une taille.
     *
     * @param chargeMemoire la charge mémoire.
     */
    public static void verifierChargeGarbageCollector(double chargeMemoire) {
        //récupérer les informations du système
        final Runtime r = Runtime.getRuntime();

        //mémoire
        final double memoireLibre = (r.freeMemory() / 1000000d);
        
        log.debug("***************************** MEMOIRE LIBRE {0} MO", memoireLibre);
        
        //limite
        final double limiteMemoire;

        //utiliser notre limite ou celle par défaut
        if (chargeMemoire != 0) {
            limiteMemoire = chargeMemoire;
        } else {
            limiteMemoire = LIMITEMEMOIRE;
        }

        //si moins de mémoire libre que notre limite, alors vider la mémoire
        if (memoireLibre < limiteMemoire) {
            try {
                //appeler les méthodes finalize() des objets
                r.runFinalization();
                //vider la mémoire
                r.gc();
                System.gc();
                
                log.debug("***************************** MEMOIRE LIBERE {0} MO", (r.freeMemory() / 1000000d));
            } catch (Exception e) {     
                log.debug("Erreur lors du vidage de la mémoire en mode automatique");
            }
        }
    }

    /**
     * fonction qui permet de vider le garbage collector de manière forcée.
     */
    public static void viderGarbageCollector() {
        //récupérer les informations du système
        final Runtime r = Runtime.getRuntime();
        try {
            //appeler les méthodes finalize() des objets
            try {
                //appeler les méthodes finalize() des objets
                r.runFinalization();
                //vider la mémoire
                r.gc();
                System.gc();
            } catch (Exception e) {         
                log.debug("Erreur lors du vidage de la mémoire en mode forcé runFinalization");
            }
        } catch (Exception e) {    
            log.debug("Erreur lors du vidage de la mémoire en mode forcé viderGarbageCollector");
        }
    }
}
