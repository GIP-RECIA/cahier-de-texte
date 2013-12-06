/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.importEDT.DTO;

import java.io.Serializable;

/**
 * Classe permettant de stocker un nom et un prénom.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public class NomPrenomDTO implements Serializable{
    /**
     * Le serialVersionUid.
     */
    private static final long serialVersionUID  = 1L;
    
    /** nom. */
    private String nom;

    /** prénom. */
    private String prenom;
    
    /**
     * Constructeur vide de NomPrenomDTO.
     */
    public NomPrenomDTO(){}
    
    /**
     * Constructeur de NomPrenomDTO.
     * @param nom : le nom.
     * @param prenom : le prénom.
     */
    public NomPrenomDTO(String nom, String prenom){
        this.nom = nom;
        this.prenom = prenom;
    }

    /**
     * Accesseur de nom.
     *
     * @return le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modificateur de nom.
     *
     * @param nom le nom à modifier
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Accesseur de prenom.
     *
     * @return le prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Modificateur de prenom.
     *
     * @param prenom le prenom à modifier
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
