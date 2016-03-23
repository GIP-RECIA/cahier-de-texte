/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DeconnexionControl.java,v 1.1 2009/07/09 13:24:47 ent_breyton Exp $
 */

package org.crlr.web.application.control;


import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.crlr.web.application.form.SecuriteForm;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.utils.FacesUtils;

/**
 * Gestion de la déconnexion.
 *
 * @author breytond.
 */
public class DeconnexionControl extends AbstractControl<SecuriteForm> {
   
    /** The contexte utilisateur. */
    private ContexteUtilisateur contexteUtilisateur;  

/**
     * Instantiates a new securite control.
     */
    public DeconnexionControl() {
        super(new SecuriteForm());
    }

    /**
     * Affecte contexte utilisateur.
     *
     * @param contexteUtilisateur the contexte utilisateur
     */
    public void setContexteUtilisateur(ContexteUtilisateur contexteUtilisateur) {
        this.contexteUtilisateur = contexteUtilisateur;
    }

   
    /**
     * {@inheritDoc}
     */
    @PostConstruct
    public void onLoad() {
        //Contrôle que l'application fonctionne avec cas
        final ExternalContext g = FacesContext.getCurrentInstance().getExternalContext();
        final HttpSession session = (HttpSession) g.getSession(false);
        
        if (session != null) {
            session.invalidate();
        }
        
        contexteUtilisateur.reset();
        
        FacesUtils.redirect("/deconnexionCas.jsp");
    }   
 }
