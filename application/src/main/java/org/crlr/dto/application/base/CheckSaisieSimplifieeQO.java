/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SaisieSimplifieeQO.java,v 1.1 2010/04/12 01:34:33 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * CheckSaisieSimplifieeQO.
 *
 * @author breytond
 * @version $Revision: 1.1 $
 */
public class CheckSaisieSimplifieeQO implements Serializable {
    /** Serail UID. */
    private static final long serialVersionUID = -970356955927989798L;

    /** Identifiant de l'établissement. */
    private Integer idEtablissement;
    
    /** Identifiant de l'enseignant. */
    private Integer idEnseignant;
           
    /** Année scolaire. */
    private AnneeScolaireDTO anneeScolaireDTO;
    
    /** accessibilité de l'établissement. */
    final Boolean vraiOuFauxEtabAccessible;
    
    /** environnement CRLR. */
    final Boolean vraiOuFauxEnvironnementCRLR;
    
    
    private boolean forceAjoutSequence = false;
    
    /**
     * Constructeur.
     * @param idEtablissement l'id de l'établissement.
     * @param idEnseignant l'id de l'enseignant.
     * @param anneeScolaireDTO anneeScolaireDTO.
     * @param vraiOuFauxEtabAccessible vraiOuFauxEtabAccessible.
     * @param vraiOuFauxEnvCRLR un booleen qui indique si on est en environnement CRLR
     */
    public CheckSaisieSimplifieeQO(final Integer idEtablissement, final Integer idEnseignant, final AnneeScolaireDTO anneeScolaireDTO,
            final Boolean vraiOuFauxEtabAccessible, Boolean vraiOuFauxEnvCRLR) {
        this.idEtablissement = idEtablissement;
        this.idEnseignant = idEnseignant;
        this.anneeScolaireDTO = anneeScolaireDTO;
        this.vraiOuFauxEtabAccessible = vraiOuFauxEtabAccessible;
        this.vraiOuFauxEnvironnementCRLR = vraiOuFauxEnvCRLR;
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
     * Accesseur anneeScolaireDTO.
     * @return le anneeScolaireDTO.
     */
    public AnneeScolaireDTO getAnneeScolaireDTO() {
        return anneeScolaireDTO;
    }

    /**
     * Mutateur anneeScolaireDTO.
     * @param anneeScolaireDTO le anneeScolaireDTO à modifier.
     */
    public void setAnneeScolaireDTO(AnneeScolaireDTO anneeScolaireDTO) {
        this.anneeScolaireDTO = anneeScolaireDTO;
    }

    /**
     * Accesseur vraiOuFauxEtabAccessible.
     * @return le vraiOuFauxEtabAccessible.
     */
    public Boolean getVraiOuFauxEtabAccessible() {
        return vraiOuFauxEtabAccessible;
    }

    /**
     * Accesseur de vraiOuFauxEnvironnementCRLR.
     * @return le vraiOuFauxEnvironnementCRLR
     */
    public Boolean getVraiOuFauxEnvironnementCRLR() {
        return vraiOuFauxEnvironnementCRLR;
    }

	public boolean isForceAjoutSequence() {
		return forceAjoutSequence;
	}

	public void setForceAjoutSequence(boolean forceAjoutSequence) {
		this.forceAjoutSequence = forceAjoutSequence;
	}
    
    

}
