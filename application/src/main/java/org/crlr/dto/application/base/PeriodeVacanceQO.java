/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PeriodeVacanceQO.java,v 1.1 2010/03/29 09:29:35 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * PeriodeVacanceQO.
 *
 * @author breytond.
 * @version $Revision: 1.1 $
  */
public class PeriodeVacanceQO implements Serializable {
    /** Serial UID. */
    private static final long serialVersionUID = -6236127360926292999L;

    /** id. */
    private Integer id;   
    
    /** les périodes de vacances. */
    private String periodeVacances;    
    
    
   /**
    * Constructeur.
    * @param id identifiant.
    * @param periodeVacances période de vacances et jours fériés.
    */
    public PeriodeVacanceQO(Integer id, String periodeVacances) {        
        this.id = id;
        this.periodeVacances = periodeVacances;
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
     * Accesseur periodeVacances.
     * @return le periodeVacances
     */
    public String getPeriodeVacances() {
        return periodeVacances;
    }

    /**
     * Mutateur de periodeVacances.
     * @param periodeVacances le periodeVacances à modifier.
     */
    public void setPeriodeVacances(String periodeVacances) {
        this.periodeVacances = periodeVacances;
    }
    
}
