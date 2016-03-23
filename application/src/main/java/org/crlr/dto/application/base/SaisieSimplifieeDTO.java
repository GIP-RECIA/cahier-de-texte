/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AnneeScolaireDTO.java,v 1.3 2010/03/29 09:29:35 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * AnneeScolaireDTO.
 *
 * @author breytond.
 * @version $Revision: 1.3 $
  */
public class SaisieSimplifieeDTO implements Serializable {
    /** Serial UID. */
    private static final long serialVersionUID = -6236127360926292999L;

    /** Vrai ou faux saisie simplifiee. */
    private Boolean vraiOuFauxsaisieSimpliee;

    /** Vrai ou faux existe en base. */
    private Boolean vraiOuFauxExiste;

    /**
     * Accesseur de vraiOuFauxsaisieSimpliee {@link #vraiOuFauxsaisieSimpliee}.
     * @return retourne vraiOuFauxsaisieSimpliee 
     */
    public Boolean getVraiOuFauxsaisieSimpliee() {
        return vraiOuFauxsaisieSimpliee;
    }

    /**
     * Mutateur de vraiOuFauxsaisieSimpliee {@link #vraiOuFauxsaisieSimpliee}.
     * @param vraiOuFauxsaisieSimpliee the vraiOuFauxsaisieSimpliee to set
     */
    public void setVraiOuFauxsaisieSimpliee(Boolean vraiOuFauxsaisieSimpliee) {
        this.vraiOuFauxsaisieSimpliee = vraiOuFauxsaisieSimpliee;
    }

    /**
     * Accesseur de vraiOuFauxExiste {@link #vraiOuFauxExiste}.
     * @return retourne vraiOuFauxExiste 
     */
    public Boolean getVraiOuFauxExiste() {
        return vraiOuFauxExiste;
    }

    /**
     * Mutateur de vraiOuFauxExiste {@link #vraiOuFauxExiste}.
     * @param vraiOuFauxExiste the vraiOuFauxExiste to set
     */
    public void setVraiOuFauxExiste(Boolean vraiOuFauxExiste) {
        this.vraiOuFauxExiste = vraiOuFauxExiste;
    }
    

    
}
