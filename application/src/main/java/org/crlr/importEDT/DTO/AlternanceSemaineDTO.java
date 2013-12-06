/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.importEDT.DTO;

import java.io.Serializable;

/**
 * Classe qui permet de contenir un numéro de semaine et son type.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public class AlternanceSemaineDTO implements Serializable {
    
    /**
     * Le serialVersionUid.
     */
    private static final long serialVersionUID  = 1L;
    
    /** Le numéro de la semaine. */
    private int numeroSemaine;

    /** Le type de semaine (PAIR ou IMPAIR). */
    private String typeSemaine;

    /**
     * Accesseur de numeroSemaine.
     *
     * @return le numeroSemaine
     */
    public int getNumeroSemaine() {
        return numeroSemaine;
    }

    /**
     * @param numeroSemaine le numeroSemaine à modifier
     */
    public void setNumeroSemaine(int numeroSemaine) {
        this.numeroSemaine = numeroSemaine;
    }

    /**
     * Accesseur de typeSemaine.
     *
     * @return le typeSemaine
     */
    public String getTypeSemaine() {
        return typeSemaine;
    }

    /**
     * @param typeSemaine le typeSemaine à modifier
     */
    public void setTypeSemaine(String typeSemaine) {
        this.typeSemaine = typeSemaine;
    }
    
    /**
     * Constructeur vide.
     * Constructeur de AlternanceSemaineDTO.
     */
    public AlternanceSemaineDTO(){}
    
    /**
     * Constructeur de AlternanceSemaineDTO avec arguments.
     * @param numSemaine : num de semaine.
     * @param typeSem : type de semaine.
     */
    public AlternanceSemaineDTO(int numSemaine, String typeSem){
        this.numeroSemaine = numSemaine;
        this.typeSemaine = typeSem;
    }
}
