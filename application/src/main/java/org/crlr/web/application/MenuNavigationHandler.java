/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: MenuNavigationHandler.java,v 1.2 2009/04/24 14:55:14 ent_breyton Exp $
 */

package org.crlr.web.application;


import javax.faces.context.FacesContext;

import org.crlr.dto.Outil;
import org.crlr.exception.base.CrlrException;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.utils.Assert;
import org.crlr.web.contexte.ContexteApplication;
import org.crlr.web.contexte.ContexteOutil;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.utils.FacesUtils;

/**
 * MenuNavigationHandler permet la gestion de la navigation dans l'application
 * ENT-CRLR via l'arbre de navigation.
 *
 * @author breytond
 */
public class MenuNavigationHandler implements SubNavigationHandler {
    /** The log. */
    protected final Log log = LogFactory.getLog(this.getClass());

    /**
     * Handle navigation.
     *
     * @param fc fc
     * @param actionMethod actionMethod
     * @param outcome outcome
     *
     * @throws Exception exception
     */
    public void handleNavigation(FacesContext fc, String actionMethod, String outcome)
                          throws Exception {
        final ContexteUtilisateur ctxUtilisateur =
            ContexteUtils.getContexteUtilisateur(fc);
       
        final ContexteApplication ctxApp = ContexteUtils.getContexteApplication(fc);
      
        //this.log.debug("HandleNavigation {0} {1}", actionMethod, outcome);
        Assert.isNotNull("contexteApplication", ctxApp);

        final int realOutcomePos = outcome.indexOf(MenuNodeAction.ACTION_SUFFIXE);
        final int realOutcomePosSousEcran =
            outcome.indexOf(MenuNodeAction.ACTION_SOUS_ECRAN_SUFFIXE);
        final int outcomePosValideArbre =
            outcome.indexOf(MenuNodeAction.ACTION_ARBRE_SUFFIXE);
        final Boolean vraiOuFauxNavigationViaArbre = outcomePosValideArbre != -1;
        final String realOutcome;

        if (realOutcomePos != -1) {
            realOutcome = outcome.substring(0, realOutcomePos);
            this.log.debug("Outcome du menu : {0}", realOutcome);
        } else {
            realOutcome = outcome.substring(0, realOutcomePosSousEcran);
            this.log.debug("Outcome du sous écran : {0}", realOutcome);
        }
        
        final Outil outil;

        try {
            outil = Outil.valueOf(realOutcome);
        } catch (final IllegalArgumentException e) {
            throw new CrlrException(e,
                                    "La cible du menu {0} n'est pas un outil identifié " +
                                    "(faut-il ajouter une nouvelle constante {1} dans le type {2} ?)",
                                    realOutcome, realOutcome.toUpperCase(),
                                    Outil.class.getSimpleName());
        }

        // Stockage de l'outil utilisé dans la liste des x derniers outils
        // utilisés
        final MenuNavigationItem navigation = new MenuNavigationItem();
        navigation.setOutil(outil);

        //vide les objets en session si l'on navigue via l'arbre.
        final ContexteOutil ctxOut = ContexteUtils.getContexteOutil(fc);
        if (vraiOuFauxNavigationViaArbre) {
            log.debug("****Destruction des objets en session du contexte outil lors de l'accès à {0}",
                      outcome);
            ctxOut.reset();
            //Il faut enlever le suffixe @arbre de l'outcome original afin de pas détruire les données du
            //contexte outil lors d'un retour sur cet écran.
            navigation.setOutcome(outcome.substring(0, outcomePosValideArbre));
        } else {
            navigation.setOutcome(outcome);
            log.debug("****Conservation des objets en session du contexte outil lors de l'accès à {0}",
                      outcome);
        }

        ctxUtilisateur.addNavigationItem(navigation);
        ctxUtilisateur.setOutil(outil);

        // Recherche de l'url de l'outil
        final String urlOutil = ctxApp.getOutilUrl(outil);
        if (urlOutil == null) {
            throw new CrlrException("Aucune URL définie pour l'outil {0}", outil);
        }

        this.log.debug("URL de l'outil : {0}", urlOutil);
        
    

        // Enfin redirection vers la page principale, ou le sous écran, de l'outil.
        fc.getExternalContext().redirect(FacesUtils.getContextURL(urlOutil));
    }

    /**
     * Can handle navigation.
     *
     * @param ctx ctx
     * @param actionMethod actionMethod
     * @param outcome outcome
     *
     * @return valeur
     */
    public boolean canHandleNavigation(FacesContext ctx, String actionMethod,
                                       String outcome) {
        if (outcome == null) {
            return false;
        }

        // Les actions du menus possèdent la chaîne @menu dans leur outcome
        // il est accessible uniquement lorsque l'utilisateur est authentifié
        final ContexteUtilisateur ctxUtilisateur =
            ContexteUtils.getContexteUtilisateur(ctx);
        if ((ctxUtilisateur != null) && ctxUtilisateur.isAuthentifie()) {
            final int outcomePos = outcome.indexOf(MenuNodeAction.ACTION_SUFFIXE);
            final int outcomeSousEcran =
                outcome.indexOf(MenuNodeAction.ACTION_SOUS_ECRAN_SUFFIXE);
            if ((outcomePos != -1) || (outcomeSousEcran != -1)) {
                return true;
            }
        }
        return false;
    }
}
