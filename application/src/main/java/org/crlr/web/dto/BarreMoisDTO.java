/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: BarreSemaineDTO.java,v 1.2 2010/04/12 04:30:13 jerome.carriere Exp $
 */

package org.crlr.web.dto;

import java.io.Serializable;
import java.util.Date;

import org.crlr.utils.DateUtils;
import org.apache.commons.lang.StringUtils;

/**
 * BarreMoisDTO.
 *
 * @author breytond.
 * @version $Revision: 1.2 $
 */
public class BarreMoisDTO implements Serializable {    
    /**
     * 
     */
    private static final long serialVersionUID = -5161438115970364348L;
 
    
    /** Date de début de mois. **/
    private Date debutMois;
    
    /** Date de fin de mois. **/
    private Date finMois;

    /**
     * Accesseur labelMois.
     * @return le labelMois.
     */
    public String getLabelMois() {
        return org.crlr.utils.StringUtils.formatagePremiereLettreMajuscule(DateUtils.format(this.debutMois, "MMMMM"));
    }

    /**
     * Retourne le libellé du mois tronqué à 4 caractères.
     * @return une chaine.
     */
    public String getLabelMoisCourt() {        
        return StringUtils.substring(getLabelMois(),  0, 4);         
    }
   

    /**
     * Accesseur debutMois.
     * @return le debutMois.
     */
    public Date getDebutMois() {
        return debutMois;
    }

    /**
     * Mutateur debutMois.
     * @param debutMois le debutMois à modifier.
     */
    public void setDebutMois(Date debutMois) {
        this.debutMois = debutMois;
    }

    /**
     * Accesseur finMois.
     * @return le finMois.
     */
    public Date getFinMois() {
        return finMois;
    }

    /**
     * Mutateur finMois.
     * @param finMois le finMois à modifier.
     */
    public void setFinMois(Date finMois) {
        this.finMois = finMois;
    }
    
   
}
