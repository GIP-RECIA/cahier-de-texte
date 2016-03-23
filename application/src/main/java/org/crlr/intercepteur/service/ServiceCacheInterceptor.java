/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ServiceCacheInterceptor.java,v 1.4 2010/06/01 13:45:24 ent_breyton Exp $
 */

package org.crlr.intercepteur.service;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.crlr.dto.ResultatDTO;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.message.ConteneurMessage;
import org.crlr.report.Report;
import org.crlr.report.impl.CsvReport;
import org.crlr.report.impl.PdfReport;
import org.crlr.utils.Assert;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.utils.MessageUtils;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import com.opensymphony.oscache.base.AbstractCacheAdministrator;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * Intercepteur de service avec cache. Les résultats des appels aux services sont mis
 * en cache.
 *
 * @author breytond
 */
@Aspect
public class ServiceCacheInterceptor {
    /** The log. */
    private final Log log = LogFactory.getLog(this.getClass());

    /** The cache. */
    private final GeneralCacheAdministrator cache;

    /** The refresh period. */
    private int refreshPeriod = 120;

    /** The parameters. */
    private ServiceCacheParam[] parameters;

    /**
     * Constructeur par défaut. Initialise un cache d'une capacité maximale
     * illimitée en mémoire.
     */
    public ServiceCacheInterceptor() {
        final Properties props = new Properties();
        props.put(AbstractCacheAdministrator.CACHE_CAPACITY_KEY, 0);
        props.put(AbstractCacheAdministrator.CACHE_MEMORY_KEY, true);
        this.cache = new GeneralCacheAdministrator(props);
    }

    /**
     * The Constructor.
     * 
     * @param cache the cache
     */
    public ServiceCacheInterceptor(final GeneralCacheAdministrator cache) {
        Assert.isNotNull("cache", cache);
        this.cache = cache;
    }

    /**
     * Retourne la liste des paramètres.
     *
     * @return the parameters
     */
    public ServiceCacheParam[] getParameters() {
        return this.parameters;
    }

    /**
     * Affecte parameters.
     *
     * @param parameters the parameters
     */
    public void setParameters(ServiceCacheParam[] parameters) {
        this.parameters = parameters;
    }

    /**
     * Retourne le temps de validité (en secondes) d'un objet placé dans le
     * cache.
     *
     * @return the refresh period
     */
    public int getRefreshPeriod() {
        return this.refreshPeriod;
    }

    /**
     * Affecte refresh period.
     *
     * @param refreshPeriod the refresh period
     */
    public void setRefreshPeriod(int refreshPeriod) {
        this.refreshPeriod = Math.max(refreshPeriod, 0);
    }

    /**
     * Teste si la session de l'utilisateur est disponible.
     *
     * @return true, if is session available
     */
    private boolean isSessionAvailable() {
        final FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx != null) {
            final ExternalContext eCtx = ctx.getExternalContext();
            if (eCtx != null) {
                final Object session = eCtx.getSession(false);
                return (session != null) && session instanceof HttpSession;
            }
        }
        return false;
    }
    
    /**
     * Gestion du cache des services métiers.
     *
     * @param proceedingJoinPoint proceedingJoinPoint.
     *
     * @return le résultat du service métier ou le résultat du cache.
     *
     * @throws Throwable l'exception.
     */
    @Around("anyServicesMethods()")
    public Object monitor(ProceedingJoinPoint proceedingJoinPoint)
                   throws Throwable {   
        if (!this.isSessionAvailable()) {
            // si la session de l'utilisateur n'est pas disponible, le cache
            // de service ne pourra pas être utilisé :
            // le service est donc directement appelé
            // ce test permet de gérer le cas où la session de l'utilisateur
            // arrive en "timeout"
            return proceedingJoinPoint.proceed();
        }      
        
        // signature de la méthode.
        final Signature signature = proceedingJoinPoint.getSignature();
        final String signatureMethode = signature.toLongString();
        final String nomMethode = signature.getName();
        
        // arguement de la méthode.
        final Object[] args = proceedingJoinPoint.getArgs();
        
        // calcul d'une clé représentative de l'appel au service
        final HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(signatureMethode);
        for (final Object arg : args) {
            if (arg != null) {
                hcb.append(HashCodeBuilder.reflectionHashCode(arg));
            }
        }
        final String key = String.valueOf(hcb.toHashCode());
        this.log.debug("Calcul de la clé pour le cache du service : {0}{1} => {2}",
                signatureMethode, args, key);

        // est-ce que la méthode appelée fait partie du groupe des méthodes
        // qui réinitialise le cache ?
        final boolean clearCache = this.isClearCacheMethod(nomMethode);
        if (clearCache) {
            this.log.debug("Effacement du cache des résultats des services ({0})",
                    nomMethode);
            this.cache.flushAll();
        }

        ServiceCacheItem item;
        try {
            // récupération de la valeur à partir du cache
            final ServiceCacheItem itemTmp = (ServiceCacheItem) this.cache.getFromCache(key, this.refreshPeriod);
           
            if (itemTmp.value instanceof PdfReport || itemTmp.value instanceof CsvReport || itemTmp.value instanceof Report) {
                item = itemTmp;
            } else {
                item = ObjectUtils.clone(itemTmp);
            }           
            
            final Object objectResultat = item.value;
            if (objectResultat != null && objectResultat instanceof ResultatDTO) {
                @SuppressWarnings("rawtypes")
                final ResultatDTO resultatDTO = (ResultatDTO) objectResultat;
                final ConteneurMessage conteneurMessage =
                    ObjectUtils.getFieldFromObject(resultatDTO, ConteneurMessage.ID);
                MessageUtils.addMessages(conteneurMessage, null, this.getClass());
            }
            
            this.log.debug("Résultat du service chargé à partir du cache : {0}{1}",
                    signatureMethode, args);
        } catch (final NeedsRefreshException e) {
            this.log.debug("Appel du service car le cache n'est pas à jour : {0}{1}",
                    signatureMethode, args);

            try {
                // le cache n'est pas à jour : le service est appelé
                final Object value = proceedingJoinPoint.proceed();
                // mise à jour du cache
                item = new ServiceCacheItem(value);
                this.cache.putInCache(key, item);
            } catch (final Exception e2) {
                // si une exception est levée lors de l'appel au service,
                // la mise à jour du cache est annulée
                this.cache.cancelUpdate(key);

                // l'exception levée par le service est relevée
                throw e2;
            }
        }

        return item.value;
    }
    
    /**
     * Exécution de l'intercepteur pour toutes méthodes du package
     * org.crlr.services.
     */
    @Pointcut("execution(* org.crlr.services.*Service.*(..)) || execution(* org.crlr.metier.facade.*Facade.*(..))")    
    public void anyServicesMethods() {
    }

    /**
     * Teste si la méthode appelée doit provoquer l'effacement du cache.
     *
     * @param nomMethode nom de la méthode.
     *
     * @return true, si la méthode doit vider le cache.
     */
    private boolean isClearCacheMethod(final String nomMethode) {
        if (this.parameters == null) {
            return false;
        }

        for (final ServiceCacheParam param : this.parameters) {
            if (PatternMatchUtils.simpleMatch(param.getMethode(), nomMethode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The Class ServiceCacheItem.
     */
    private static class ServiceCacheItem implements Serializable {
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value. */
        public Object value;

/**
         * The Constructor.
         * 
         * @param value
         *            the value
         */
        public ServiceCacheItem(final Object value) {
            this.value = value;
        }
        
        /**
         * The Constructor.
         */
        @SuppressWarnings("unused")
        public ServiceCacheItem() {
            this.value = null;
        }
    }

    /**
     * The Class ServiceCacheParam.
     */
    public static class ServiceCacheParam implements Serializable {
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The methode. */
        private String methode;

        /**
         * Retourne methode.
         *
         * @return the methode
         */
        public String getMethode() {
            return this.methode;
        }

        /**
         * Affecte methode.
         *
         * @param methode the methode
         */
        public void setMethode(String methode) {
            this.methode = methode;
        }
    }

    /**
     * The Class ServiceCacheParamEditor.
     */
    public static class ServiceCacheParamEditor extends PropertyEditorSupport {
        /** The Constant SEPARATOR. */
        private static final String SEPARATOR = "[,]";

        /**
         * Affecte as text.
         *
         * @param text the as text
         *
         * @throws IllegalArgumentException the illegal argument exception
         */
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if (!StringUtils.hasText(text)) {
                this.setValue(null);
                return;
            }

            final String[] tokens = text.split(SEPARATOR);
            final List<ServiceCacheParam> params = new ArrayList<ServiceCacheParam>();
            for (final String token : tokens) {
                final String trimmedToken = StringUtils.trimWhitespace(token);
                if (trimmedToken.length() == 0) {
                    continue;
                }

                final ServiceCacheParam param = new ServiceCacheParam();
                param.setMethode(trimmedToken);
                params.add(param);
            }

            this.setValue(params.toArray(new ServiceCacheParam[params.size()]));
        }

        /**
         * Retourne as text.
         *
         * @return the as text
         */
        @Override
        public String getAsText() {
            final ServiceCacheParam[] params = (ServiceCacheParam[]) this.getValue();
            if (params == null) {
                return "";
            }

            final StringBuilder buf = new StringBuilder();
            for (final ServiceCacheParam param : params) {
                buf.append(param.getMethode()).append(SEPARATOR);
            }

            return buf.toString();
        }
    }
}
