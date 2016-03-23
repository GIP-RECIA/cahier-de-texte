/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: FacesUtils.java,v 1.3 2009/07/09 13:24:47 ent_breyton Exp $
 */

package org.crlr.web.utils;

import java.io.IOException;
import java.util.Iterator;

import javax.el.ELResolver;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.TypeRepertoireStockage;
import org.crlr.exception.base.CrlrRuntimeException;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.message.Message;
import org.crlr.utils.Assert;
import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.FileUploadDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Méthodes utilitaires pour JSF.
 *
 * @author breytond
 * @author romana
 */
public final class FacesUtils {
    /**
     * Génère un identifiant pour un objet qui sera forcément unique grâce à un
     * compteur interne. Utile par exemple pour définir les ids des lignes dans une
     * table (utilisé de manière transparente)
     */
    private static int compteur;
    
    protected static final Logger log = LoggerFactory.getLogger(FacesUtils.class);
    
/**
     * The Constructor.
     */
    private FacesUtils() {
    }

    /**
     * Retourne l'instance {@link HttpServletRequest} courante.
     *
     * @return une instance {@link HttpServletRequest}, ou <code>null</code>
     */
    public static HttpServletRequest getRequest() {
        final FacesContext fc = FacesContext.getCurrentInstance();
        if (fc == null) {
            return null;
        }

        final Object req = fc.getExternalContext().getRequest();
        if (req instanceof HttpServletRequest) {
            return (HttpServletRequest) req;
        }
        return null;
    }

    /**
     * Retourne l'instance {@link HttpServletResponse} courante.
     *
     * @return une instance {@link HttpServletResponse}, ou <code>null</code>
     */
    public static HttpServletResponse getResponse() {
        final FacesContext fc = FacesContext.getCurrentInstance();
        if (fc == null) {
            return null;
        }

        final Object resp = fc.getExternalContext().getResponse();
        if (resp instanceof HttpServletResponse) {
            return (HttpServletResponse) resp;
        }
        return null;
    }

    /**
     * Retourne un paramètre de la requête.
     *
     * @param nom du paramètre
     *
     * @return valeur du paramètre
     */
    public static String getParametre(String nom) {
        Assert.isNotNull("nom", nom);
        final HttpServletRequest req = FacesUtils.getRequest();
        final String valeur = StringUtils.trimToNull(req.getParameter(nom));
        if (valeur == null) {
            throw new CrlrRuntimeException("Paramètre de la requête requis : {0}", nom);
        }
        return valeur;
    }

    /**
     * Efface les valeurs d'un composant et de ses enfants.
     *
     * @param comp the comp
     */
    private static void reset(UIComponent comp) {
        if (comp == null) {
            return;
        }
        if (comp instanceof EditableValueHolder) {
            ((EditableValueHolder) comp).setSubmittedValue(null);
        }
        for (final Iterator<UIComponent> i = comp.getFacetsAndChildren(); i.hasNext();) {
            final UIComponent child = i.next();
            reset(child);
        }
    }

    /**
     * Efface les valeurs du formulaire attaché à la requête.
     */
    public static void resetForm() {
        final UIComponent form = FacesContext.getCurrentInstance().getViewRoot();
        reset(form);
    }

    /**
     * Retourne le <code>viewId</code> de la vue courante.
     *
     * @return <code>viewId</code>, ou <code>null</code> s'il n'existe aucune vue
     */
    public static String getViewId() {
        final UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
        return (root != null) ? root.getViewId() : null;
    }

    /**
     * Retourne l'instance {@link ServletContext} courante.
     *
     * @return une instance {@link ServletContext}, ou <code>null</code>
     */
    public static ServletContext getServletContext() {
        final FacesContext fc = FacesContext.getCurrentInstance();
        if (fc == null) {
            return null;
        }

        final Object ctx = fc.getExternalContext().getContext();
        if (ctx instanceof ServletContext) {
            return (ServletContext) ctx;
        }
        return null;
    }

    /**
     * Retourne une URL dont le chemin est relatif au contexte de l'application.
     *
     * @param url relative au contexte
     *
     * @return url comprenant le chemin du contexte
     */
    public static String getContextURL(String url) {
        if ((url == null) || !url.startsWith("/")) {
            return url;
        }

        final HttpServletRequest req = FacesUtils.getRequest();
        if (req == null) {
            return url;
        }

        return req.getContextPath() + url;
    }

    /**
     * Génère un identifiant pour un objet.
     *
     * @param obj the obj
     *
     * @return un identifiant pour l'objet, ou la chaîne <code>"null"</code> si l'objet
     *         est <code>null</code>
     */
    public static String generateId(Object obj) {
        return (obj == null) ? "null" : ("id" + String.valueOf(obj.hashCode()));
    }

    /**
     * Generate id counter.
     *
     * @param obj the obj
     *
     * @return the string
     */
    public static String generateIdCounter(Object obj) {
        compteur++;
        return (obj == null) ? ("null" + "__" + compteur)
                             : ("id" + String.valueOf(obj.hashCode()) + "__" + compteur);
    }

    /**
     * Permet de connaître l'état du conteneur de messages.
     *
     * @return <code>true</code> si le conteneur de messages n'est pas vide,
     *         <code>false</code> dans le cas contraire.
     */
    public static Boolean haveMessages() {
        Boolean isMessages = Boolean.FALSE;
        final FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null) {
            final Iterator<?> it = fc.getMessages();
            isMessages = it.hasNext();
        }
        return isMessages;
    }

    /**
     * Have one message.
     *
     * @return the boolean
     */
    public static Boolean haveOneMessage() {
        
        final FacesContext fc = FacesContext.getCurrentInstance();
        if (fc == null) {
            return false;
        }
        
        final Iterator<?> it = fc.getMessages();
        
        if (it.hasNext()) {
            return true;
        }
            
        return false;
    }

    /**
     * Permet de connaître si le conteneur de messages dispose de messages
     * bloquants ou avertissants.
     *
     * @return <code>true</code> si le conteneur de messages disposent de messages
     *         bloquants ou avertissants, <code>false</code> dans le cas contraire.
     */
    public static Boolean haveMessagesError() {
        Boolean isMessages = Boolean.FALSE;
        final FacesContext fc = FacesContext.getCurrentInstance();
        if (fc == null) {
            return isMessages;
        }
        
        final Iterator<?> it = fc.getMessages();
        log.debug("haveMessagesError.  # messages {}", fc.getMessageList().size());
        
        while (it.hasNext()) {
            final FacesMessage obj = (FacesMessage) it.next();
            log.debug("haveMessagesError msg {}", obj);
            if (!obj.getSeverity().equals(FacesMessage.SEVERITY_INFO)) {
                isMessages = true;
                break;
            }
        }
    
        return isMessages;
    }

    /**
     * Permet de connaître si le conteneur de messages dispose de messages
     * bloquants.
     *
     * @return <code>true</code> si le conteneur de messages disposent de messages
     *         bloquants, <code>false</code> dans le cas contraire.
     */
    public static Boolean haveMessagesBloquant() {
        Boolean isMessages = Boolean.FALSE;
        final FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null) {
            final Iterator<?> it = fc.getMessages();
            while (it.hasNext()) {
                final FacesMessage obj = (FacesMessage) it.next();
                if (obj.getSeverity().equals(FacesMessage.SEVERITY_ERROR) || obj.getSeverity().equals(FacesMessage.SEVERITY_FATAL)) {
                    isMessages = true;
                    break;
                }
            }
        }
        return isMessages;
    }
    
    /**
     * Messages statut.
     *
     * @return the string
     */
    public static String messagesStatut() {
        int cpt = 0;
        if (!haveMessagesError()) {
            return "";
        }
        final FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null) {
            final Iterator<?> it = fc.getMessages();
            while (it.hasNext()) {
                it.next();
                cpt++;
            }
        }
        final String statut =
            (cpt == 0) ? "" : (
                           Integer.valueOf(cpt).toString() + " message(s) reçu(s) : "
                       );
        return statut;
    }

    /**
     * Redirection vers l'URL demandée.
     *
     * @param url url ne contenant pas le context path, celui ci sera ajouté
     *        automatiquement
     */
    public static void redirect(final String url) {
        
        final Log log = LogFactory.getLog(FacesUtils.class);
        
        final String urlAvecContexte = FacesUtils.getContextURL(url);
        try {
            //FIXME Eric ne fonctionne pas avec les pdfs  
            if (FacesContext.getCurrentInstance().getResponseComplete() && url != null && !url.contains(".pdf")) {
                log.warning("Redirection déjà fait.  Url {0}", url);
                return;
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect(urlAvecContexte);
            
            log.debug("La redirection vers la page : {0} a ", urlAvecContexte);
        } catch (IOException e) {
            log.error("La redirection vers la page : {0} a échoué", urlAvecContexte);
        }
    }
    
    /**
     * Redirection vers l'URL demandée avec un message associé.
     *
     * @param url url ne contenant pas le context path, celui ci sera ajouté
     *        automatiquement.
     * @param message le message.
     */
    public static void redirectWithMessage(final String url, final Message message) {
        FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put("Message", message);
        redirect(url);
    }

    /**
     * Récupére la valeur d'un paramètre de l'application web initialisé dans le
     * fichier de configuration web.xml. Si aucune valeur n'est trouvée, c'est la valeur
     * par défaut fournie en paramètre qui est renvoyée.
     *
     * @param parametre le nom du paramètre
     * @param valeurDefaut the valeur defaut
     *
     * @return la valeur du paramètre
     */
    public static String getInitParameter(String parametre, String valeurDefaut) {
        Assert.isNotNull("parametre", parametre);
        final String valeurParam =
            StringUtils.trimToNull(FacesContext.getCurrentInstance().getExternalContext()
                                               .getInitParameter(parametre));
        return ObjectUtils.defaultIfNull(valeurParam, valeurDefaut);
    }
    
    /**
     * Permet d'évaluer une variable jsf en fonction d'un contexte faces.
     * @param fc le contexte faces.
     * @param variable la variable à évaluer.
     * @return l'évaluation.
     */
    public static Object resolveVariable(final FacesContext fc, final String variable) {
        return resolver(fc, variable);
    }
    
    /**
     * Permet d'évaluer une variable jsf.
     * @param variable la variable à évaluer.
     * @return l'évaluation.
     */
    public static Object resolveVariable(final String variable) {
        final FacesContext fc = FacesContext.getCurrentInstance();
        
        if (fc == null && variable.equals( ContexteUtilisateur.ID) ) {
            ContexteUtilisateur util = new ContexteUtilisateur();
            util.getUtilisateurDTO().getAnneeScolaireDTO().setDateRentree(DateUtils.creer(2011, 8, 1));
            util.getUtilisateurDTO().getAnneeScolaireDTO().setDateSortie(DateUtils.creer(2012, 5, 31));
            util.getUtilisateurDTO().getAnneeScolaireDTO().setId(2);
            util.getUtilisateurDTO().setDesignationEtablissement("Bull-Test");
            util.getUtilisateurDTO().setProfil(Profil.ENSEIGNANT);
            util.getUtilisateurDTO().setIdEtablissement(114);
            util.getUtilisateurDTO().getUserDTO().setIdentifiant(536);
            return util;
        }
        return resolver(fc,variable);
    }
    
    /**
     * Permet d'évaluer une variable jsf en fonction d'un contexte faces.
     * @param fc le contexte faces.
     * @param variable la variable à évaluer.
     * @return l'évaluation.
     */
    private static Object resolver(final FacesContext fc, final String variable) {
        final ELResolver resolver = fc.getApplication().getELResolver();
        return resolver.getValue(fc.getELContext(), null, variable);        
    }
    
    /**
     * Deconnexion d'un utilisateur.
     * @param fCtx le faces contexte.
     * @param contexteUtilisateur le contexte utilisateur.
     */
    public static void deconnexionApplication(final FacesContext fCtx, final ContexteUtilisateur contexteUtilisateur) {
        deconnexion(fCtx, contexteUtilisateur);     
    }
    
    /**
     * Deconnexion d'un utilisateur.
     * @param fCtx le faces contexte.   
     */
    public static void deconnexionApplication(final FacesContext fCtx) {
        deconnexion(fCtx, ContexteUtils.getContexteUtilisateur());
    }
    
    /**
     * Deconnexion d'un utilisateur.
     * @param fCtx le faces contexte.
     * @param contexteUtilisateur le contexte utilisateur.
     */
    private static void deconnexion(final FacesContext fCtx, final ContexteUtilisateur contexteUtilisateur) {
     // Déconnexion
        final HttpServletRequest req = FacesUtils.getRequest();
        if (req != null) {
            final HttpSession session = req.getSession(false);
            if (session != null) {
                HttpSessionUtils.invalidate(session);
                contexteUtilisateur.reset();
            }
        }
        
        // reset du menu.
        ContexteUtils.getMenuControl(fCtx).reset();
        
        // reset du Contexte outil
        ContexteUtils.getContexteOutil().reset();          
    }
    
    /**
     * Déconnexion.
     */
    public static void deconnexionCas() {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            final HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
    }

    /**
     * .
     * @param fileUploadDTO .
     */
    public static void pathUploadFile(final FileUploadDTO fileUploadDTO) {
        
        if (null == FacesContext.getCurrentInstance()) {
            fileUploadDTO.setPathAbsolute("/path/test");
            return;
        }
        String pathAbsolute =
            ((ServletContext) FacesContext.getCurrentInstance()
                                          .getExternalContext().getContext()).getRealPath("/");
        final String pathDownload =
            ((ServletContext) FacesContext.getCurrentInstance()
                                          .getExternalContext().getContext()).getContextPath();
        
        //TODO 
        //constitution des chemins.
        final String pathRelatif = "/" + TypeRepertoireStockage.UPLOADFILE.getId() + "/" + fileUploadDTO.getUid() + fileUploadDTO.getPathDB();
        
        for (final String itemChemin : StringUtils.split(pathRelatif, "/")) {
            if (!StringUtils.isEmpty(itemChemin)) {
                pathAbsolute += itemChemin + java.io.File.separator;
            }
        }            
        fileUploadDTO.setPathAbsolute(pathAbsolute);
        
        final String cheminRelatifDownload =
            pathDownload + pathRelatif + "/" + fileUploadDTO.getNom();
        
        fileUploadDTO.setPathFullDownload(cheminRelatifDownload);
    }
}
