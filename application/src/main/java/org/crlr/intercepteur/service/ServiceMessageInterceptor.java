/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ServiceMessageInterceptor.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.intercepteur.service;


import javax.faces.context.FacesContext;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.crlr.exception.metier.MetierException;
import org.crlr.exception.metier.MetierRuntimeException;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.message.ConteneurMessage;

import org.crlr.utils.ObjectUtils;
import org.crlr.web.utils.FacesUtils;
import org.crlr.web.utils.MessageUtils;

/**
 * Intercepteur de service avec gestion du {@link ContexteService} et du {@link
 * ConteneurMessage} associé. Transformation d'un {@link Message} métier en message
 * {@link FacesMessage}. Redirection vers une page d'erreur pour des exceptions
 * impromptues.
 *
 * @author breytond
 */
@Aspect
public class ServiceMessageInterceptor {
    /** The log. */
    private final Log log = LogFactory.getLog(this.getClass());
    
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private static final String LOGOUTURL = "/deconnexion.jsp";
    
    /**
     * Gestion des exceptions et messages métiers.
     *
     * @param proceedingJoinPoint proceedingJoinPoint.
     *
     * @return le résultat du service métier.
     *
     * @throws Throwable l'exception.
     */
    @Around("anyServicesMethods()")
    public Object monitor(ProceedingJoinPoint proceedingJoinPoint)
                   throws Throwable {
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
            final ConteneurMessage conteneurMessage =
                ObjectUtils.getFieldFromObject(result, ConteneurMessage.ID);
            MessageUtils.addMessages(conteneurMessage, null, this.getClass());
        } catch (final MetierRuntimeException e) {
            redirectUnknowException(e);
            throw e;
        } catch (final MetierException e) {
            MessageUtils.addMessages(e.getConteneurMessage(), e, this.getClass());
            throw e;
        } catch (final Exception e) {
            redirectUnknowException(e);
            throw e;
        } finally {
            log.debug("Traitement des messages et exceptions de la couche métier terminé.");
        }

        return result;
    }

    /**
     * Deconnexion de l'application et redirection vers la page de déconnexion.
     *
     * @param e l'exception.
     */
    private void redirectUnknowException(final Exception e) {
        
        
        if (FacesContext.getCurrentInstance().getResponseComplete()) {
            log.warning(e, "Redirection déjà fait");
            return;
        }
    
        
        // Redirection vers la page de déconnexion de l'application
        FacesUtils.deconnexionApplication(FacesContext.getCurrentInstance());

                
        log.error(e, "unknown exception");
        FacesUtils.redirect(LOGOUTURL);
        
    }

    /**
     * Exécution de l'intercepteur pour toutes méthodes du package
     * org.crlr.services.
     */
    @Pointcut("execution(* org.crlr.services.*Service.*(..)) || execution(* org.crlr.metier.facade.*Facade.*(..))")
    public void anyServicesMethods() {
    }
}
