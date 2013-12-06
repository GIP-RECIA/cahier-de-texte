/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SecuritePhaseListener.java,v 1.2 2009/04/21 09:08:27 ent_breyton Exp $
 */

package org.crlr.web.servlet.listener;

import java.util.Map;
import java.util.Set;

import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.crlr.dto.Outil;
import org.crlr.dto.securite.TypeReglesSecurite;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.message.Message;
import org.apache.commons.collections.CollectionUtils;
import org.crlr.web.application.MenuNodeAction;
import org.crlr.web.contexte.ContexteApplication;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.utils.FacesUtils;

/**
 * Gestion de l'authentification dans le cycle de vie JSF.
 *
 * @author breytond
 */
public class SecuritePhaseListener implements PhaseListener {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Les logs.
     */
    private final transient Log log = LogFactory.getLog(getClass());

    /**
     * LogonURL.
     */
    private static final String LOGONURL = "/index.jsp"; 

    /**
     * 
    DOCUMENT ME!
     *
     * @param evt paramat�re
     */
    public void afterPhase(PhaseEvent evt) {
        final FacesContext fCtx = evt.getFacesContext();
        final ExternalContext eCtx = fCtx.getExternalContext();        
        
        //Problématique : les css dans richfaces sont considérées comme des vues faces,
        //il faut donc contrôler que les éléments ne sont pas des ressources de la vue.
        //TODO Eric toujours besoin?
        /*if (WebXml.getInstance(fCtx).getFacesResourceKey((HttpServletRequest) eCtx.getRequest()) != null) {
            return;
        }*/
        
        
        final Map<String, Object> sessionMap = eCtx.getSessionMap();

        final ViewHandler viewHandler = fCtx.getApplication().getViewHandler();
        final String url = viewHandler.getActionURL(fCtx, FacesUtils.getViewId());

        final ContexteUtilisateur contexteUtilisateur =
            (ContexteUtilisateur) sessionMap.get("contexteUtilisateur");
        final boolean logged =
            (contexteUtilisateur != null) && contexteUtilisateur.isAuthentifie();

        if (logged) {
            log.debug("Utilisateur authentifié : {0}",
                      contexteUtilisateur.getUtilisateurDTO().getUserDTO().getUid());

            // Vérification que l'url saisie correspond bien à un outil autorisé
            final String namespace = eCtx.getRequestContextPath();

            // Transformation de l'url : (enlever /cahier-texte du début)
            if (url.startsWith(namespace)) {
                final String uri = url.substring(namespace.length());

                final ContexteApplication contexteApplication =
                    ContexteUtils.getContexteApplication(fCtx);
                final Set<Outil> outilsDemandes =
                    contexteApplication.getOutilsParUrl(uri);
                if (!CollectionUtils.isEmpty(outilsDemandes)) {
                    for (Outil outilDemande : outilsDemandes) {
                        if (contexteUtilisateur.getMapOutilMode().containsKey(outilDemande) ||
                                outilDemande.equals(Outil.ACCUEIL) || outilDemande.equals(Outil.CONNEXION)) {
                            // un outil autorisé -> Url autorisée
                            if ((contexteUtilisateur.getOutil() == null) ||
                                    (
                                            !contexteUtilisateur.getOutil()
                                                                    .equals(outilDemande)
                                        )) {
                                // --> Appel d'un outil différent que l'outil actuel
                                // --> Simulation d'un clic sur menu pour initialiser les variables.
                                final NavigationHandler navigationHandler =
                                    fCtx.getApplication().getNavigationHandler();
                                final String actionMethod = "#{node.action}";
                                final String outcome =
                                    outilDemande.name() + MenuNodeAction.ACTION_CLIQUE_ARBRE;
                                navigationHandler.handleNavigation(fCtx, actionMethod,
                                                                   outcome);
                            }
                            return;
                        }
                    }
                    // Pas d'outil autorisé --> page de connexion.
                    log.debug("Outil non autorisé. {0}", outilsDemandes);
                    FacesUtils.deconnexionApplication(FacesContext.getCurrentInstance());
                    FacesUtils.redirect(LOGONURL);
                }
            }
        } else {
            //Eclusion de la page public
            if (!url.contains("connexion.xhtml")) {
                log.debug("Pas d'utilisateur authentifié");
                log.debug("Redirection vers la page d'authentification : {0}", LOGONURL);                
                FacesUtils.redirectWithMessage(LOGONURL, new Message(TypeReglesSecurite.SECU_00.name()));             
            }
        }
    }

   /**
    * {@inheritDoc}
    */
    public void beforePhase(PhaseEvent evt) {
  
    }

    /**
     * Accesseur à l'identifiant de la phase JSF. 
     *
     * @return id.
     */
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }   
}
