/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SaveSequenceSimplifieeQO.java,v 1.1 2010/04/12 01:34:33 ent_breyton Exp $
 */

package org.crlr.dto.application.sequence;

import java.io.Serializable;

import org.crlr.dto.application.base.AnneeScolaireDTO;

/**
 * SaveSequenceSimplifieeQO.
 *
 * @author breytond
 * @version $Revision: 1.1 $
 */
public class SaveSequenceSimplifieeQO implements Serializable {
    /** Serail UID. */
    private static final long serialVersionUID = -2429912643601332223L;
    
    /** Activation de la saisie simplifiée. */
    private Boolean vraiOuFauxSaisieSimplifiee;
     
    /** Id de l'établissement. */
    private Integer idEtablissement;

    /** Id de l'enseignant. */
    private Integer idEnseignant;    
    
    /**
     * Année scolaire courante.
     */
    private AnneeScolaireDTO anneeScolaireDTO;

    /**
     * Accesseur idEnseignant.
     * @return le idEnseignant.
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur idEnseignant.
     * @param idEnseignant le idEnseignant à modifier.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur anneeScolaireDTO.
     * @return anneeScolaireDTO
     */
    public AnneeScolaireDTO getAnneeScolaireDTO() {
        return anneeScolaireDTO;
    }

    /**
     * Mutateur anneeScolaireDTO.
     * @param anneeScolaireDTO Le anneeScolaireDTO à modifier
     */
    public void setAnneeScolaireDTO(AnneeScolaireDTO anneeScolaireDTO) {
        this.anneeScolaireDTO = anneeScolaireDTO;
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
}
