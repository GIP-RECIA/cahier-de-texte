/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignantsClasseGroupeQO.java,v 1.1 2010/04/26 14:03:08 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * EnseignantsClasseGroupeQO.
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class EnseignantsClasseGroupeQO implements Serializable {

    /**
     * SERIAL UID.
     */
    private static final long serialVersionUID = -902425874601247210L;
    
    /** id du groupe ou de la classe. */
    private Integer idGroupeClasse;
    
    /** id de l'établissement. */
    private Integer idEtablissement;
    
    /** id de l'enseignement. */
    private Integer idEnseignement;
    
    /** id de l'inspecteur. */
    private Integer idInspecteur;

    /**
     * Accesseur idGroupeClasse.
     * @return le idGroupeClasse
     */
    public Integer getIdGroupeClasse() {
        return idGroupeClasse;
    }

    /**
     * Mutateur de idGroupeClasse.
     * @param idGroupeClasse le idGroupeClasse à modifier.
     */
    public void setIdGroupeClasse(Integer idGroupeClasse) {
        this.idGroupeClasse = idGroupeClasse;
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
     * Accesseur de idInspecteur.
     * @return le idInspecteur
     */
    public Integer getIdInspecteur() {
        return idInspecteur;
    }

    /**
     * Mutateur de idInspecteur.
     * @param idInspecteur le idInspecteur à modifier.
     */
    public void setIdInspecteur(Integer idInspecteur) {
        this.idInspecteur = idInspecteur;
    }
    
    
    
}
