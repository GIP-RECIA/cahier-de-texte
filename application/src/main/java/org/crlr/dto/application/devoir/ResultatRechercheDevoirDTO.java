/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ResultatRechercheDevoirDTO.java,v 1.7 2010/04/28 16:12:21 jerome.carriere Exp $
 */

package org.crlr.dto.application.devoir;

import java.io.Serializable;
import java.util.Date;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.7 $
 */
public class ResultatRechercheDevoirDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = -3964041334794393505L;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idClasseGroupe;

    /** DOCUMENTATION INCOMPLETE! */
    private String codeClasse;

    /** DOCUMENTATION INCOMPLETE! */
    private String designationClasse;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idGroupe;

    /** DOCUMENTATION INCOMPLETE! */
    private String codeGroupe;

    /** DOCUMENTATION INCOMPLETE! */
    private String designationGroupe;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idSeance;

    /** DOCUMENTATION INCOMPLETE! */
    private String codeSeance;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idEnseignement;

    /** DOCUMENTATION INCOMPLETE! */
    private String codeEnseignement;

    /** DOCUMENTATION INCOMPLETE! */
    private String designationEnseignement;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idSequence;

    /** DOCUMENTATION INCOMPLETE! */
    private String codeSequence;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idEnseignant;

    /** DOCUMENTATION INCOMPLETE! */
    private String nomEnseignant;

    /** DOCUMENTATION INCOMPLETE! */
    private String prenomEnseignant;

    /** DOCUMENTATION INCOMPLETE! */
    private String civiliteEnseignant;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idTypeDevoir;

    /** DOCUMENTATION INCOMPLETE! */
    private String libelleTypeDevoir;

    /** DOCUMENTATION INCOMPLETE! */
    private Date dateRemiseDevoir;

    /** DOCUMENTATION INCOMPLETE! */
    private String intituleDevoir;

    /** DOCUMENTATION INCOMPLETE! */
    private String descriptionDevoir;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idDevoir;

    /** DOCUMENTATION INCOMPLETE! */
    private String codeDevoir;
    
    

    /**
     * Contructeur.
     */
    public ResultatRechercheDevoirDTO() {
    }

    /**
     * Accesseur idClasseGroupe.
     *
     * @return idClasseGroupe
     */
    public Integer getIdClasseGroupe() {
        return idClasseGroupe;
    }

    /**
     * Mutateur idClasseGroupe.
     *
     * @param idClasseGroupe Le idClasseGroupe à modifier
     */
    public void setIdClasseGroupe(Integer idClasseGroupe) {
        this.idClasseGroupe = idClasseGroupe;
    }

    /**
     * Accesseur codeGroupe.
     *
     * @return codeGroupe
     */
    public String getCodeGroupe() {
        return codeGroupe;
    }

    /**
     * Mutateur codeGroupe.
     *
     * @param codeGroupe Le codeGroupe à modifier
     */
    public void setCodeGroupe(String codeGroupe) {
        this.codeGroupe = codeGroupe;
    }

    /**
     * Accesseur codeClasse.
     *
     * @return codeClasse
     */
    public String getCodeClasse() {
        return codeClasse;
    }

    /**
     * Mutateur codeClasse.
     *
     * @param codeClasse Le codeClasse à modifier
     */
    public void setCodeClasse(String codeClasse) {
        this.codeClasse = codeClasse;
    }

    /**
     * Accesseur idSeance.
     *
     * @return idSeance
     */
    public Integer getIdSeance() {
        return idSeance;
    }

    /**
     * Mutateur idSeance.
     *
     * @param idSeance Le idSeance à modifier
     */
    public void setIdSeance(Integer idSeance) {
        this.idSeance = idSeance;
    }

    /**
     * Accesseur codeSeance.
     *
     * @return codeSeance
     */
    public String getCodeSeance() {
        return codeSeance;
    }

    /**
     * Mutateur codeSeance.
     *
     * @param codeSeance Le codeSeance à modifier
     */
    public void setCodeSeance(String codeSeance) {
        this.codeSeance = codeSeance;
    }

    /**
     * Accesseur idEnseignement.
     *
     * @return idEnseignement
     */
    public Integer getIdEnseignement() {
        return idEnseignement;
    }

    /**
     * Mutateur idEnseignement.
     *
     * @param idEnseignement Le idEnseignement à modifier
     */
    public void setIdEnseignement(Integer idEnseignement) {
        this.idEnseignement = idEnseignement;
    }

    /**
     * Accesseur codeEnseignement.
     *
     * @return codeEnseignement
     */
    public String getCodeEnseignement() {
        return codeEnseignement;
    }

    /**
     * Mutateur codeEnseignement.
     *
     * @param codeEnseignement Le codeEnseignement à modifier
     */
    public void setCodeEnseignement(String codeEnseignement) {
        this.codeEnseignement = codeEnseignement;
    }

    /**
     * Accesseur designationEnseignement.
     *
     * @return designationEnseignement
     */
    public String getDesignationEnseignement() {
        return designationEnseignement;
    }

    /**
     * Mutateur designationEnseignement.
     *
     * @param designationEnseignement Le designationEnseignement à modifier
     */
    public void setDesignationEnseignement(String designationEnseignement) {
        this.designationEnseignement = designationEnseignement;
    }

    /**
     * Accesseur idSequence.
     *
     * @return idSequence
     */
    public Integer getIdSequence() {
        return idSequence;
    }

    /**
     * Mutateur idSequence.
     *
     * @param idSequence Le idSequence à modifier
     */
    public void setIdSequence(Integer idSequence) {
        this.idSequence = idSequence;
    }

    /**
     * Accesseur codeSequence.
     *
     * @return codeSequence
     */
    public String getCodeSequence() {
        return codeSequence;
    }

    /**
     * Mutateur codeSequence.
     *
     * @param codeSequence Le codeSequence à modifier
     */
    public void setCodeSequence(String codeSequence) {
        this.codeSequence = codeSequence;
    }

    /**
     * Accesseur idEnseignant.
     *
     * @return idEnseignant
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur idEnseignant.
     *
     * @param idEnseignant Le idEnseignant à modifier
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur nomEnseignant.
     *
     * @return nomEnseignant
     */
    public String getNomEnseignant() {
        return nomEnseignant;
    }

    /**
     * Mutateur nomEnseignant.
     *
     * @param nomEnseignant Le nomEnseignant à modifier
     */
    public void setNomEnseignant(String nomEnseignant) {
        this.nomEnseignant = nomEnseignant;
    }

    /**
     * Accesseur prenomEnseignant.
     *
     * @return prenomEnseignant
     */
    public String getPrenomEnseignant() {
        return prenomEnseignant;
    }

    /**
     * Mutateur prenomEnseignant.
     *
     * @param prenomEnseignant Le prenomEnseignant à modifier
     */
    public void setPrenomEnseignant(String prenomEnseignant) {
        this.prenomEnseignant = prenomEnseignant;
    }

    /**
     * Accesseur idTypeDevoir.
     *
     * @return idTypeDevoir
     */
    public Integer getIdTypeDevoir() {
        return idTypeDevoir;
    }

    /**
     * Mutateur idTypeDevoir.
     *
     * @param idTypeDevoir Le idTypeDevoir à modifier
     */
    public void setIdTypeDevoir(Integer idTypeDevoir) {
        this.idTypeDevoir = idTypeDevoir;
    }

    /**
     * Accesseur dateRemiseDevoir.
     *
     * @return dateRemiseDevoir
     */
    public Date getDateRemiseDevoir() {
        return dateRemiseDevoir;
    }

    /**
     * Mutateur dateRemiseDevoir.
     *
     * @param dateRemiseDevoir Le dateRemiseDevoir à modifier
     */
    public void setDateRemiseDevoir(Date dateRemiseDevoir) {
        this.dateRemiseDevoir = dateRemiseDevoir;
    }

    /**
     * Accesseur intituleDevoir.
     *
     * @return intituleDevoir
     */
    public String getIntituleDevoir() {
        return intituleDevoir;
    }

    /**
     * Mutateur intituleDevoir.
     *
     * @param intituleDevoir Le intituleDevoir à modifier
     */
    public void setIntituleDevoir(String intituleDevoir) {
        this.intituleDevoir = intituleDevoir;
    }

    /**
     * Accesseur idDevoir.
     *
     * @return idDevoir
     */
    public Integer getIdDevoir() {
        return idDevoir;
    }

    /**
     * Mutateur idDevoir.
     *
     * @param idDevoir Le idDevoir à modifier
     */
    public void setIdDevoir(Integer idDevoir) {
        this.idDevoir = idDevoir;
    }

    /**
     * Accesseur codeDevoir.
     *
     * @return codeDevoir
     */
    public String getCodeDevoir() {
        return codeDevoir;
    }

    /**
     * Mutateur codeDevoir.
     *
     * @param codeDevoir Le codeDevoir à modifier
     */
    public void setCodeDevoir(String codeDevoir) {
        this.codeDevoir = codeDevoir;
    }

    /**
     * Accesseur libelleTypeDevoir.
     *
     * @return libelleTypeDevoir
     */
    public String getLibelleTypeDevoir() {
        return libelleTypeDevoir;
    }

    /**
     * Mutateur libelleTypeDevoir.
     *
     * @param libelleTypeDevoir Le libelleTypeDevoir à modifier
     */
    public void setLibelleTypeDevoir(String libelleTypeDevoir) {
        this.libelleTypeDevoir = libelleTypeDevoir;
    }

    /**
     * Accesseur civiliteEnseignant.
     *
     * @return civiliteEnseignant
     */
    public String getCiviliteEnseignant() {
        return civiliteEnseignant;
    }

    /**
     * Mutateur civiliteEnseignant.
     *
     * @param civiliteEnseignant Le civiliteEnseignant à modifier
     */
    public void setCiviliteEnseignant(String civiliteEnseignant) {
        this.civiliteEnseignant = civiliteEnseignant;
    }

    /**
     * Accesseur descriptionDevoir.
     *
     * @return descriptionDevoir
     */
    public String getDescriptionDevoir() {
        return descriptionDevoir;
    }

    /**
     * Mutateur descriptionDevoir.
     *
     * @param descriptionDevoir Le descriptionDevoir à modifier
     */
    public void setDescriptionDevoir(String descriptionDevoir) {
        this.descriptionDevoir = descriptionDevoir;
    }

    /**
     * Accesseur idGroupe.
     *
     * @return idGroupe
     */
    public Integer getIdGroupe() {
        return idGroupe;
    }

    /**
     * Mutateur idGroupe.
     *
     * @param idGroupe Le idGroupe à modifier
     */
    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    /**
     * Accesseur designationClasse.
     *
     * @return designationClasse
     */
    public String getDesignationClasse() {
        return designationClasse;
    }

    /**
     * Mutateur designationClasse.
     *
     * @param designationClasse Le designationClasse à modifier
     */
    public void setDesignationClasse(String designationClasse) {
        this.designationClasse = designationClasse;
    }

    /**
     * Accesseur designationGroupe.
     *
     * @return designationGroupe
     */
    public String getDesignationGroupe() {
        return designationGroupe;
    }

    /**
     * Mutateur designationGroupe.
     *
     * @param designationGroupe Le designationGroupe à modifier
     */
    public void setDesignationGroupe(String designationGroupe) {
        this.designationGroupe = designationGroupe;
    }

    
    
}
