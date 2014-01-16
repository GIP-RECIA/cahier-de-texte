/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: UserDTO.java,v 1.3 2009/04/21 09:02:28 ent_breyton Exp $
 */

package org.crlr.dto;

import java.io.Serializable;


/**
 * Dto représentant les données importantes d'un utilisateur.
 *
 * @author breytond.
 * @version $Revision: 1.3 $
 */
public class UserDTO implements Serializable {
    /**
     * Serial uid. 
     */
    private static final long serialVersionUID = -4698578843440335909L;

    /** Identifiant en base de données. */
    private Integer identifiant;    
    
    /** Nom de l'individu. */
    private String nom;

    /** Prénom de l'individu. */
    private String prenom;  
    
    /** Uid de l'utilisateur. */
    private String uid;  
    
    private String depotStockage;

    /**
     * Constructeur.
     */
    public UserDTO() {
    }

    /**
     * toString.   
     *
     * @return repésentation de l'objet.
     */
    public String toString() {
        return "UserDTO[uid = " + uid + ", identifiant=" + identifiant + ", nom=" + nom + ", prenom=" +
               prenom + "]";
    }

    /**
     * Accesseur identifiant.
     *
     * @return le identifiant.
     */
    public Integer getIdentifiant() {
        return identifiant;
    }

    /**
     * Mutateur identifiant.
     *
     * @param identifiant le identifiant à modifier.
     */
    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    /**
     * Accesseur nom.
     *
     * @return le nom.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Mutateur nom.
     *
     * @param nom le nom à modifier.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Accesseur prenom.
     *
     * @return le prenom.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Mutateur prenom.
     *
     * @param prenom le prenom à modifier.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }    

    /**
     * Accesseur uid.
     *
     * @return le uid.
     */
    public String getUid() {
        return uid;
    }

    /**
     * Mutateur uid.
     *
     * @param uid le uid à modifier.
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @return the depotStockage
     */
    public String getDepotStockage() {
        return depotStockage;
    }

    /**
     * @param depotStockage the depotStockage to set
     */
    public void setDepotStockage(String depotStockage) {
        this.depotStockage = depotStockage;
    }    
}
