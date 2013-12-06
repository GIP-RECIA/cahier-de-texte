/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.dto.application.importEDT;

import java.io.Serializable;
import java.util.Date;

/**
 * QO qui permet d'identifier un emploi du temps pour une periode à supprimer.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public class PeriodeEdtQO implements Serializable {

    /** Serial.  */
    private static final long serialVersionUID = -2681904919250952818L;
    
    /** Id de l'etablissement. */
    private Integer idEtablissement;
    
    /** Date de debut de periode : tous les EDT démarrant à cette date sont supprimés. */
    private Date dateDebut;

    /**
     * Accesseur de idEtablissement {@link #idEtablissement}.
     * @return retourne idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur de idEtablissement {@link #idEtablissement}.
     * @param idEtablissement le idEtablissement to set
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur de dateDebut {@link #dateDebut}.
     * @return retourne dateDebut
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * Mutateur de dateDebut {@link #dateDebut}.
     * @param dateDebut le dateDebut to set
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    
    
}
