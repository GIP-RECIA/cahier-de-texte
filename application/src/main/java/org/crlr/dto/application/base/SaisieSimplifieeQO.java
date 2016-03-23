/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SaisieSimplifieeQO.java,v 1.1 2010/04/12 01:34:33 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * SaisieSimplifieeQO.
 *
 * @author breytond
 * @version $Revision: 1.1 $
 */
public class SaisieSimplifieeQO implements Serializable {
    /** Serail UID. */
    private static final long serialVersionUID = -970356955927989798L;

    /** Identifiant de l'établissement. */
    private Integer idEtablissement;
    
    /** Identifiant de l'enseignant. */
    private Integer idEnseignant;
             
    /** Activation de la saisie simplifiée. */
    private Boolean vraiOuFauxSaisieSimplifiee;
    
    /** Indique si la ligne existe en base ou non. */
    private Boolean vraiOuFauxExiste;
    
    /**
     * Constructeur.
     * @param idEtablissement l'id de l'établissement.
     * @param idEnseignant l'id de l'enseignant.
     * @param vraiOuFauxSaisieSimplifiee activation de la saisie simplifiée.
     */
    public SaisieSimplifieeQO(final Integer idEtablissement, final Integer idEnseignant, final Boolean vraiOuFauxSaisieSimplifiee,
            final Boolean vraiOuFauxExiste) {
        this.idEtablissement = idEtablissement;
        this.idEnseignant = idEnseignant;
        this.vraiOuFauxSaisieSimplifiee = vraiOuFauxSaisieSimplifiee;
        this.vraiOuFauxExiste = vraiOuFauxExiste;
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
     * Accesseur idEnseignant.
     * @return le idEnseignant
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur de idEnseignant.
     * @param idEnseignant le idEnseignant à modifier.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur vraiOuFauxSaisieSimplifiee.
     * @return le vraiOuFauxSaisieSimplifiee
     */
    public Boolean getVraiOuFauxSaisieSimplifiee() {
        return vraiOuFauxSaisieSimplifiee;
    }

    /**
     * Mutateur de vraiOuFauxSaisieSimplifiee.
     * @param vraiOuFauxSaisieSimplifiee le vraiOuFauxSaisieSimplifiee à modifier.
     */
    public void setVraiOuFauxSaisieSimplifiee(Boolean vraiOuFauxSaisieSimplifiee) {
        this.vraiOuFauxSaisieSimplifiee = vraiOuFauxSaisieSimplifiee;
    }

    /**
     * Accesseur de vraiOuFauxExiste {@link #vraiOuFauxExiste}.
     * @return retourne vraiOuFauxExiste 
     */
    public Boolean getVraiOuFauxExiste() {
        return vraiOuFauxExiste;
    }

    /**
     * Mutateur de vraiOuFauxExiste {@link #vraiOuFauxExiste}.
     * @param vraiOuFauxExiste the vraiOuFauxExiste to set
     */
    public void setVraiOuFauxExiste(Boolean vraiOuFauxExiste) {
        this.vraiOuFauxExiste = vraiOuFauxExiste;
    }
    
}
