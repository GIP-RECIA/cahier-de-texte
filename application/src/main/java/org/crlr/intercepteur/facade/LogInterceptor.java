/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: LogInterceptor.java,v 1.6 2010/03/29 09:29:35 ent_breyton Exp $
 */

package org.crlr.intercepteur.facade;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.message.ConteneurMessage;
import org.crlr.utils.Chrono;
import org.crlr.utils.ObjectUtils;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author breytond
 * @version $Revision: 1.6 $
 */
@Aspect
public class LogInterceptor {
    /** DOCUMENTATION INCOMPLETE! */
    private static final Log log = LogFactory.getLog(LogInterceptor.class);  

    /**
     * Constructeur de l'Aspect.
     */
    public LogInterceptor() {      
    }

    /**
     * Times repository method invocations and outputs performance results to a
     * Log4J logger.
     *
     * @param repositoryMethod The join point representing the intercepted repository
     *        method
     *
     * @return The object returned by the target method
     *
     * @throws Throwable if thrown by the target method
     */
    @Around("anyFacadeMethods()")
    public Object monitor(ProceedingJoinPoint repositoryMethod)
                   throws Throwable {
        final Chrono chrono = new Chrono();
        final Object[] args = repositoryMethod.getArgs();

        // Nom de la méthode interceptée
        final String name = repositoryMethod.getSignature().toLongString();
        final StringBuffer sb = new StringBuffer(name + " called with: [");

        // Liste des valeurs des arguments reçus par la méthode
        for (int i = 0; i < args.length; i++) {
            final Object o = args[i];
            sb.append("'" + o + "'");
            sb.append((i == (args.length - 1)) ? "" : ", ");
        }
        sb.append("]");
        
        chrono.start();
        
        log.debug("Entrée : {1}", chrono.getDureeHumain(), sb.toString());
        ConteneurMessage conteneurMessage = null;
        Object result = null;
        try {
            result = repositoryMethod.proceed();
        } catch (Exception e) {
            //log.error(e, "Erreur : {0}", sb.toString());
            conteneurMessage = ObjectUtils.getFieldFromObject(e, ConteneurMessage.ID);
            // l'exception est relevée
            throw e;
        } finally {
            chrono.stop();
            
            log.debug("Sortie : {0} => {1}", sb.toString(), result);
            log.debug("Temps passé dans la méthode {0} : {1}", name,
                      chrono.getDureeHumain());
          
            if (conteneurMessage == null && result != null) {
                conteneurMessage = ObjectUtils.getFieldFromObject(result, ConteneurMessage.ID);
            }      
            log.debug("Messages à la fin de l'appel métier : {0}", conteneurMessage);
            
            //GarbageUtils.verifierChargeGarbageCollector(0);
        }

        return result;
    }

    /**
     * 
     */
    @Pointcut("execution(* org.crlr.metier.facade.*FacadeService.*(..))")
    public void anyFacadeMethods() {
    }
}
