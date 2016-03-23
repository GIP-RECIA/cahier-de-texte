/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EleveDTO.java,v 1.1 2009/04/08 08:54:26 weberent Exp $
 */

package org.crlr.alimentation.DTO;

import org.crlr.metier.entity.InspecteurBean;

/**
 * DTO pour transferer les inspecteurs du ldap vers une BD.
 *
 * @author Aurore
 * @version $Revision: 1.1 $
 */
public class InspecteurDTO {
    /** L'eleve à inserer. */
    private InspecteurBean insBean;

    
/**
    * Constructeur de la classe. Ne fait rien.
    */
    public InspecteurDTO() {
    }


/**
 * Accesseur de insBean.
 * @return le insBean
 */
public InspecteurBean getInsBean() {
    return insBean;
}


/**
 * Mutateur de insBean.
 * @param insBean le insBean à modifier.
 */
public void setInsBean(InspecteurBean insBean) {
    this.insBean = insBean;
}

    
}
