/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignementQO.java,v 1.1 2010/03/31 15:05:11 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * EnseignementQO.
 *
 * @author breytond.
 * @version $Revision: 1.1 $
  */
public class EnseignementQO implements Serializable {
    /** Serial UID. */
    private static final long serialVersionUID = 3285590712663801056L;

    /** libellé perso. */
    private String libelle;
    
    /** identifiant de l'enseignement. */
    private Integer idEnseignement;
    
    /** identifiant de l'établissement. */
    private Integer idEtablissement;

    /**
     * Accesseur libelle.
     * @return le libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Mutateur de libelle.
     * @param libelle le libelle à modifier.
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Accesseur idEnseignement.
     * @return le idEnseignement
     */
    public Integer getIdEnseignement() {
        return idEnseignement;
    }

    /**
     * Mutateur de idEnseignement.
     * @param idEnseignement le idEnseignement à modifier.
     */
    public void setIdEnseignement(Integer idEnseignement) {
        this.idEnseignement = idEnseignement;
    }

    /**
     * Accesseur idEtablissement.
     * @return le idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur de idEtablissement.
     * @param idEtablissement le idEtablissement à modifier.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }   
}
