/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PrefForm.java,v 1.3 2010/04/28 16:12:21 jerome.carriere Exp $
 */

package org.crlr.web.application.form;

/**
 * Formulaire d'accueil.
 *
 * @author breytond
 */
public class PrefForm extends AbstractForm {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Activation de la saisie simplifiée. */
    private Boolean vraiOuFauxSaisieSimplifiee;
    
    /** Valeur de l'activation de la saisie simplifiee telle qu'elle est en base de données. */
    private Boolean vraiOuFauxSaisieSimplifieeBdd;
    
    /** Type de préférences. **/
    private String typePreferences;
    
    /**
     * Constructeur.
     */
    public PrefForm() {
        super();
        this.vraiOuFauxSaisieSimplifiee = false;
        this.vraiOuFauxSaisieSimplifieeBdd = false;
    }

    /**
     * Accesseur vraiOuFauxSaisieSimplifiee.
     * @return le vraiOuFauxSaisieSimplifiee
     */
    public Boolean getVraiOuFauxSaisieSimplifiee() {
        return vraiOuFauxSaisieSimplifiee;
    }

    /**
     * Mutateur de vraiOuFauxSaisieSimplifiee.
     * @param vraiOuFauxSaisieSimplifiee le vraiOuFauxSaisieSimplifiee à modifier.
     */
    public void setVraiOuFauxSaisieSimplifiee(Boolean vraiOuFauxSaisieSimplifiee) {
        this.vraiOuFauxSaisieSimplifiee = vraiOuFauxSaisieSimplifiee;
    }

    /**
     * Accesseur typePreferences.
     * @return le typePreferences
     */
    public String getTypePreferences() {
        return typePreferences;
    }

    /**
     * Mutateur de typePreferences.
     * @param typePreferences le typePreferences à modifier.
     */
    public void setTypePreferences(String typePreferences) {
        this.typePreferences = typePreferences;
    }

    /**
     * Accesseur de vraiOuFauxSaisieSimplifieeBdd {@link #vraiOuFauxSaisieSimplifieeBdd}.
     * @return retourne vraiOuFauxSaisieSimplifieeBdd
     */
    public Boolean getVraiOuFauxSaisieSimplifieeBdd() {
        return vraiOuFauxSaisieSimplifieeBdd;
    }

    /**
     * Mutateur de vraiOuFauxSaisieSimplifieeBdd {@link #vraiOuFauxSaisieSimplifieeBdd}.
     * @param vraiOuFauxSaisieSimplifieeBdd le vraiOuFauxSaisieSimplifieeBdd to set
     */
    public void setVraiOuFauxSaisieSimplifieeBdd(
            Boolean vraiOuFauxSaisieSimplifieeBdd) {
        this.vraiOuFauxSaisieSimplifieeBdd = vraiOuFauxSaisieSimplifieeBdd;
    }
    
    
}
