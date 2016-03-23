/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: OuvertureQO.java,v 1.2 2010/04/29 11:20:51 jerome.carriere Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * OuvertureQO.
 *
 * @author breytond.
 * @version $Revision: 1.2 $
  */
public class OuvertureQO implements Serializable {
    /** Serial UID. */   
    private static final long serialVersionUID = -6079907530027148698L;

    /** id. */
    private Integer id;   
    
    /** les périodes de vacances. */
    private Boolean vraiOuFauxOuvert;    
    
    
   /**
    * Constructeur.
    * @param id identifiant.
    * @param vraiOuFauxOuvert ouvert ou fermé.
    */
    public OuvertureQO(Integer id, Boolean vraiOuFauxOuvert) {        
        this.id = id;
        if (vraiOuFauxOuvert != null) {
            this.vraiOuFauxOuvert = vraiOuFauxOuvert;
        } else {
            this.vraiOuFauxOuvert = false;
        }
    }

    /**
     * Accesseur.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur.
     *
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur vraiOuFauxOuvert.
     * @return le vraiOuFauxOuvert
     */
    public Boolean getVraiOuFauxOuvert() {
        return vraiOuFauxOuvert;
    }

    /**
     * Mutateur de vraiOuFauxOuvert.
     * @param vraiOuFauxOuvert le vraiOuFauxOuvert à modifier.
     */
    public void setVraiOuFauxOuvert(Boolean vraiOuFauxOuvert) {
        this.vraiOuFauxOuvert = vraiOuFauxOuvert;
    }
}
