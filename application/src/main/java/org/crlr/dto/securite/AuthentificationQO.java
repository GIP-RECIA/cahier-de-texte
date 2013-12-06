/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AuthentificationQO.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.dto.securite;

import java.util.List;
import java.util.Map;

import org.crlr.dto.Environnement;
import org.crlr.dto.application.base.Profil;

/**
 * AuthentificationQO.
 *
 * @author breytond
 * @version $Revision: 1.1 $
 */
public class AuthentificationQO {
    /** uid ou login. */
    private String identifiant;

    /** mot de passe. */
    private String password;

    /** type authentification. */
    private TypeAuthentification typeAuthentification;
    
    /** Environnement d'exécution.*/
    private Environnement environnement;
    
    /** Les profils applicatifs avec leurs correspondances. */
    private Map<String, Profil> mapProfil;
    
    /** Les groupes pour l'ADMCentral. */
    private List<String> groupsADMCentral;
    
    /** Les expressios regulieres pour l'admin local. */
    private String regexpAdmLocal;
    

/**
     * Constructeur.
     */
    public AuthentificationQO() {
    }

    /**
     * Accesseur.
     *
     * @return uid ou le login.
     */
    public String getIdentifiant() {
        return identifiant;
    }

    /**
     * Mutateur.
     *
     * @param identifiant uid ou login.
     */
    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    /**
     * Accesseur.
     *
     * @return mot de passe.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Mutateur.
     *
     * @param password mot de passe.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Accesseur.
     *
     * @return le type.
     */
    public TypeAuthentification getTypeAuthentification() {
        return typeAuthentification;
    }

    /**
     * Mutateur.
     *
     * @param typeAuthentification le type.
     */
    public void setTypeAuthentification(TypeAuthentification typeAuthentification) {
        this.typeAuthentification = typeAuthentification;
    }

    /**
     * Accesseur environnement.
     * @return le environnement.
     */
    public Environnement getEnvironnement() {
        return environnement;
    }

    /**
     * Mutateur environnement.
     * @param environnement le environnement à modifier.
     */
    public void setEnvironnement(Environnement environnement) {
        this.environnement = environnement;
    }

    /**
     * Accesseur de mapProfil.
     * @return le mapProfil
     */
    public Map<String, Profil> getMapProfil() {
        return mapProfil;
    }

    /**
     * Mutateur de mapProfil.
     * @param mapProfil le mapProfil à modifier.
     */
    public void setMapProfil(Map<String, Profil> mapProfil) {
        this.mapProfil = mapProfil;
    }

    /**
     * Accesseur de groupsADMCentral.
     * @return le groupsADMCentral
     */
    public List<String> getGroupsADMCentral() {
        return groupsADMCentral;
    }

    /**
     * Mutateur de groupsADMCentral.
     * @param groupsADMCentral le groupsADMCentral à modifier.
     */
    public void setGroupsADMCentral(List<String> groupsADMCentral) {
        this.groupsADMCentral = groupsADMCentral;
    }

    /**
     * Accesseur de regexpAdmLocal.
     * @return le regexpAdmLocal
     */
    public String getRegexpAdmLocal() {
        return regexpAdmLocal;
    }

    /**
     * Mutateur de regexpAdmLocal.
     * @param regexpAdmLocal le regexpAdmLocal à modifier.
     */
    public void setRegexpAdmLocal(String regexpAdmLocal) {
        this.regexpAdmLocal = regexpAdmLocal;
    }
    
    
    
}
