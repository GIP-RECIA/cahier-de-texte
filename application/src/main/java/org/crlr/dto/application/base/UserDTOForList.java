/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: UserDTO.java,v 1.3 2009/04/21 09:02:28 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

import org.crlr.metier.entity.InspecteurBean;


/**
 * Dto représentant les données importantes d'un utilisateur.
 *
 * @author breytond.
 * @version $Revision: 1.3 $
 */
public class UserDTOForList implements Serializable {
    /**
     * Serial uid. 
     */
    private static final long serialVersionUID = -4698578843440335909L;

    /** Identifiant en base de données. */
    private String identifiant;    
    
    /** Nom de l'individu à afficher (civilite nom prenom). */
    private String nom;
    
    private Boolean vraiOuFauxSelectionne;

    /**
     * Constructeur.
     */
    public UserDTOForList() {
        vraiOuFauxSelectionne = false;
    }

    /**
     * Constructeur à partir de l'inspecteur Bean.
     * @param inspecteurBean .
     */
    public UserDTOForList(InspecteurBean inspecteurBean) {
        identifiant = inspecteurBean.getId().toString();
        nom = inspecteurBean.getCivilite()+" "+inspecteurBean.getNom()+" "+inspecteurBean.getPrenom();
    }

   

    /**
     * Accesseur de identifiant.
     * @return le identifiant
     */
    public String getIdentifiant() {
        return identifiant;
    }

    /**
     * Mutateur de identifiant.
     * @param identifiant le identifiant à modifier.
     */
    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    /**
     * Accesseur de nom.
     * @return le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Mutateur de nom.
     * @param nom le nom à modifier.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Accesseur de vraiOuFauxSelectionne.
     * @return le vraiOuFauxSelectionne
     */
    public Boolean getVraiOuFauxSelectionne() {
        return vraiOuFauxSelectionne;
    }

    /**
     * Mutateur de vraiOuFauxSelectionne.
     * @param vraiOuFauxSelectionne le vraiOuFauxSelectionne à modifier.
     */
    public void setVraiOuFauxSelectionne(Boolean vraiOuFauxSelectionne) {
        this.vraiOuFauxSelectionne = vraiOuFauxSelectionne;
    }

    
}
