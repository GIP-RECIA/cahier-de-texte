/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AjoutSequenceForm.java,v 1.6 2010/05/19 09:10:20 jerome.carriere Exp $
 */

package org.crlr.web.application.form.stockage;

import org.crlr.web.application.form.AbstractForm;

/**
 * StockageForm.
 *
 * @author $author$
 * @version $Revision: 1.6 $
  */
public class StockageForm extends AbstractForm {
    /**  */
    private static final long serialVersionUID = 6247931318863172732L;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private String nomRepertoireACreerCahier;

/**
     * Constructeur.
     */
    public StockageForm() {       
        reset();
    }

    /**
     * Méthode de réinitialisation des données du formulaire saisie par
     * l'utilisateur.
     */
    public void reset() {  
        nomRepertoireACreerCahier= null;
    }

    /**
     * Accesseur nomRepertoireACreerCahier.
     * @return le nomRepertoireACreerCahier.
     */
    public String getNomRepertoireACreerCahier() {
        return nomRepertoireACreerCahier;
    }

    /**
     * Mutateur nomRepertoireACreerCahier.
     * @param nomRepertoireACreerCahier le nomRepertoireACreerCahier à modifier.
     */
    public void setNomRepertoireACreerCahier(String nomRepertoireACreerCahier) {
        this.nomRepertoireACreerCahier = nomRepertoireACreerCahier;
    }
    

}
