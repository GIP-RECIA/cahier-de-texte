/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SecuriteForm.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.application.form;

/**
 * Formulaire d'authentification.
 *
 * @author breytond
 */
public class SecuriteForm extends AbstractForm {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The identifiant. */
    private String identifiant;

    /** The mot de passe. */
    private String motDePasse;

    /**
     * Retourne identifiant.
     *
     * @return the identifiant
     */
    public String getIdentifiant() {
        return identifiant;
    }

    /**
     * Affecte identifiant.
     *
     * @param identifiant the identifiant to set
     */
    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    /**
     * Retourne mot de passe.
     *
     * @return the motDePasse
     */
    public String getMotDePasse() {
        return motDePasse;
    }

    /**
     * Affecte mot de passe.
     *
     * @param motDePasse the motDePasse to set
     */
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}
