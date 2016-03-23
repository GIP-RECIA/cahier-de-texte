/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: VisaDTO.java,v 1.2 2009/04/22 13:14:23 ent_breyton Exp $
 */

package org.crlr.dto.application.visa;

import org.crlr.dto.application.visa.VisaDTO.VisaProfil;


/**
 * Classe QO pour la recherche d'un liste de VisaDTO.
 * @author G-SAFIR-FRMP
 */
public class RechercheVisaQO {
    
    /** Id Enseignant. */
    private Integer  idEnseignant;

    /** Id Etablissement. */
    private Integer  idEtablissement;
    
    /** Profil du visa (ENTDirecteur ou ENTInspecteur). */
    private VisaProfil profil;

    /**
     * Accesseur de idEnseignant {@link #idEnseignant}.
     * @return retourne idEnseignant
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur de idEnseignant {@link #idEnseignant}.
     * @param idEnseignant le idEnseignant to set
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

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
     * Accesseur de profil {@link #profil}.
     * @return retourne profil
     */
    public VisaProfil getProfil() {
        return profil;
    }

    /**
     * Mutateur de profil {@link #profil}.
     * @param profil le profil to set
     */
    public void setProfil(VisaProfil profil) {
        this.profil = profil;
    }

    
}
