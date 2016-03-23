/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ResultatRechercheSequenceDTO.java,v 1.5 2010/02/26 13:45:02 jerome.carriere Exp $
 */

package org.crlr.dto.application.sequence;

import java.io.Serializable;

import org.crlr.dto.application.base.SequenceDTO;

/**
 * Dto de résultat de la recherche pour une séquence.
 *
 * @author breytond
 * @version $Revision: 1.5 $
 */
public class ResultatRechercheSequenceDTO extends SequenceDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = 732141344762838224L;

   

    /** Désignation de l'enseignement. */
    private String designationEnseignement;


   

    /** Mode. */
    private String mode;


    /**
     * Accesseur codeSequence.
     *
     * @return le codeSequence.
     * @deprecated
     */    
    public String getCodeSequence() {
        return getCode();
    }

    /**
     * Mutateur codeSequence.
     *
     * @param codeSequence le codeSequence à modifier.
     * @deprecated
     */
    public void setCodeSequence(String codeSequence) {
        setCode(codeSequence);
    }

    /**
     * Accesseur codeClasseGroupe.
     *
     * @return le codeClasseGroupe.
     * @deprecated
     */
    public String getCodeClasseGroupe() {
        return getGroupesClassesDTO().getCode();
    }

    /**
     * Mutateur codeClasseGroupe.
     *
     * @param codeClasseGroupe le codeClasseGroupe à modifier.
     * @deprecated
     */
    public void setCodeClasseGroupe(String codeClasseGroupe) {
        getGroupesClassesDTO().setCode(codeClasseGroupe);
    }

    /**
     * Accesseur designationEnseignement.
     *
     * @return le designationEnseignement.
     */
    public String getDesignationEnseignement() {
        return designationEnseignement;
    }

    /**
     * Mutateur designationEnseignement.
     *
     * @param designationEnseignement le designationEnseignement à modifier.
     */
    public void setDesignationEnseignement(String designationEnseignement) {
        this.designationEnseignement = designationEnseignement;
    }

    /**
     * Accesseur intituleSequence.
     *
     * @return le intituleSequence.
     * @deprecated
     */
    public String getIntituleSequence() {
        return getIntitule();
    }

    /**
     * Mutateur intituleSequence.
     *
     * @param intituleSequence le intituleSequence à modifier.
     * @deprecated
     */
    public void setIntituleSequence(String intituleSequence) {
        setIntitule(intituleSequence);
    }



   

    /**
     * Accesseur idSequence.
     *
     * @return idSequence
     * @deprecated
     */
    public Integer getIdSequence() {
        return getId();
    }

    /**
     * Mutateur idSequence.
     *
     * @param idSequence Le idSequence à modifier
     * @deprecated
     */
    public void setIdSequence(Integer idSequence) {
        setId(idSequence);
    }

    /**
     * Accesseur mode.
     *
     * @return mode
     * 
     */
    public String getMode() {
        return mode;
    }

    /**
     * Mutateur mode.
     *
     * @param mode Le mode à modifier
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Accesseur descriptionSequence.
     *
     * @return descriptionSequence
     * @deprecated
     */
    public String getDescriptionSequence() {
        return getDescription();
    }

    /**
     * Mutateur descriptionSequence.
     *
     * @param descriptionSequence Le descriptionSequence à modifier
     * @deprecated
     */
    public void setDescriptionSequence(String descriptionSequence) {
        setDescription(descriptionSequence);
    }

    /**
     * Accesseur designationClasse.
     *
     * @return designationClasse
     * @deprecated
     */
    public String getDesignationClasse() {
        return getGroupesClassesDTO().getDesignation();
    }

    /**
     * Mutateur designationClasse.
     *
     * @param designationClasse Le designationClasse à modifier
     * @deprecated
     */
    public void setDesignationClasse(String designationClasse) {
        getGroupesClassesDTO().setDesignation(designationClasse);
    }

    /**
     * Accesseur designationGroupe.
     *
     * @return designationGroupe
     * @deprecated
     */
    public String getDesignationGroupe() {
        return getGroupesClassesDTO().getDesignation();
    }

    /**
     * Mutateur designationGroupe.
     *
     * @param designationGroupe Le designationGroupe à modifier
     * @deprecated
     */
    public void setDesignationGroupe(String designationGroupe) {
        this.getGroupesClassesDTO().setDesignation(designationGroupe);
    }
}
