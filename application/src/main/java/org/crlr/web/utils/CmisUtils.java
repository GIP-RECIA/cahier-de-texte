/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.utils;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.utils.BooleanUtils;
import org.crlr.utils.PropertiesUtils;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.CommonUtils;

/**
 * WebDavUtils.
 * 
 * @author egroning
 * 
 */
public final class CmisUtils {
    protected final static Log log = LogFactory.getLog(CmisUtils.class);

    private static final String CMS_URL_PROPERTY_KEY = "cmis.url";
    private static final String CMS_USERS_DIR_PROPERTY_KEY = "cmis.usersDir"; 
    private static final String CMS_TICKET_URL_PROPERTY_KEY = "cmis.ticketUrl";
    private static final String CMS_ACTIVE_PROPERTY_KEY = "cmis.active";
    
    private static Properties config;
    
    static {
        config = PropertiesUtils.load("/config.properties");
    }
    
    /**
     * Classe utilitaire.
     */
    private CmisUtils() {
        
    }
    
    /**
     * 
     * @param cmisSession la session CMIS
     * @return Le dossier CMIS de l'utilisateur.
     */
    public static Folder getUserFolder(Session cmisSession) {
                
        String uid = ContexteUtils.getContexteUtilisateur()
                .getUtilisateurDTO().getUserDTO().getUid();
        
        //TODO Bouchon
        //uid = "F1100001";
        
        final String baseDir = config.getProperty(CMS_USERS_DIR_PROPERTY_KEY);
                
        Folder userFolder = (Folder) cmisSession.getObjectByPath(baseDir + uid);
        
        return userFolder;
    }
    
    /**
     * @return la session ou null si il y a un problème.
     */
    public static Session openCmisSession() {

        Map<String, String> parameters = new HashMap<String, String>();

        parameters.put(SessionParameter.BINDING_TYPE,
                BindingType.ATOMPUB.value());

  
        final Properties config = PropertiesUtils.load("/config.properties");
        final String baseUrl = config.getProperty(CMS_URL_PROPERTY_KEY);

        parameters.put(SessionParameter.ATOMPUB_URL, baseUrl);
        parameters.put(SessionParameter.AUTH_HTTP_BASIC, "true");
        parameters.put(SessionParameter.COOKIES, "true");

        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();

        parameters.put(SessionParameter.USER, "");
        parameters.put(SessionParameter.PASSWORD,
                utilisateurDTO.getTicketAlfresco());

        try {
            for (String key : parameters.keySet()) {
                log.info(MessageFormat.format("Le clé est \n{0} = {1}", key,
                        parameters.get(key)));
            }

            SessionFactory factory = SessionFactoryImpl.newInstance();
            List<Repository> repositories = factory.getRepositories(parameters);
            return repositories.get(0).createSession();
        } catch (CmisConnectionException ce) {
            log.warning(
                    ce,
                    "failed to retriev cmisSession : , repository is not accessible or simply not started ?",
                    ce);
        } catch (Exception ce) {
            log.warning(
                    ce,
                    "failed to retriev cmisSession :  , repository is not accessible or simply not started ?",
                    ce);
            
        }
        
        return null;
    }
    
    public static String getAlfrescoTicket() {

        try {
        	
        	final Properties config = PropertiesUtils.load("/config.properties");
            final String targetUrl = config.getProperty(CMS_TICKET_URL_PROPERTY_KEY);

            final String cmisActiveStr = config.getProperty(CMS_ACTIVE_PROPERTY_KEY);

            Boolean cmisActive = BooleanUtils.isTrue(cmisActiveStr);
            
            if (!org.apache.commons.lang.BooleanUtils.isTrue(cmisActive)
            		|| org.apache.commons.lang.StringUtils.trimToNull(targetUrl) == null) {
            	log.info("CMIS non activé dans config.properties");
            	return null;
            }
            
            final ExternalContext g = FacesContext.getCurrentInstance()
                    .getExternalContext();

            final AttributePrincipal principal = (AttributePrincipal) g
                    .getUserPrincipal();

            final String casUser = principal.getName();

            
            final String proxyTicket = principal.getProxyTicketFor(targetUrl);

            log.info("Proxy ticket " + proxyTicket + " |||||| {0}"
                    + proxyTicket);

            if (proxyTicket == null) {
                return null;
            }

            final String serviceUrl = targetUrl + "/wcs/api/logincas?u="

            + URLEncoder.encode(casUser, "UTF-8") + "&t="
                    + URLEncoder.encode(proxyTicket, "UTF-8");

            final String ticketAlfresco = CommonUtils.getResponseFromServer(
                    serviceUrl, "UTF-8");

            // ServletAjaxController.proxyTicket = proxyResponseTicketAlfresco;

            log.info("proxyResponseTicketAlfresco" + ticketAlfresco
                    + " |||||| {0}" + ticketAlfresco);

            Pattern p = Pattern.compile(Pattern.quote("<ticket>") + "(.*)"
                    + Pattern.quote("</ticket>"));
            Matcher m = p.matcher(ticketAlfresco);

            if (m.find()) {
                return m.group(1);
            }

        } catch (Exception ex) {
            log.warning(ex, "ex");
        }

        return null;

    }

}