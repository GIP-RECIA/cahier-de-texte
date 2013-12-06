/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SaveEnseignementQO.java,v 1.2 2009/04/20 07:04:33 vibertd Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * Un QO pour sauvegarder les noms d'enseignements.
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class SaveEnseignementQO implements Serializable {
    /**  */
    private static final long serialVersionUID = -1604942651960544060L;

    /** Id de l'enseignement. */
    private Integer id;

    /** Code de l'enseignement. */
    private String code;

    /** Désignation de l'enseignement. */
    private String designation;

    /** Id de l'enseignement. */
    private Integer idEnseignement;

    /** Id classe. */
    private Integer idClasse;

    /** Id groupe. */
    private Integer idGroupe;

    /** id séquence. */
    private Integer idSequence;
    
    /** Mode. */
    private String mode;
    
    /** Type selectionné. */
    private TypeGroupe typeClasseGroupe;
    
    /**
     * Sauvegarde de l'ancien id séquence.
     */
    private Integer oldIdSequence;

    /**
     *Sauvegarde de l'ancien id groupe ou classe.
     */
    private Integer oldIdGroupeClasse;

    /**
     * Sauvegarde de l'ancien type selectionné.
     */
    private TypeGroupe oldTypeGroupeSelectionne;

    /**
     * Sauvegarde de l'ancien id enseignement.
     */
    private Integer oldIdEnseignement;

    /**
     * Accesseur id.
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur id.
     *
     * @param id Le id à modifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur code.
     *
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * Mutateur code.
     *
     * @param code Le code à modifier
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Accesseur designation.
     *
     * @return designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Mutateur designation.
     *
     * @param designation Le designation à modifier
     */
    public void setDesignation(String designation) {
        this.designation = designation;
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
     * Accesseur idClasse.
     *
     * @return idClasse
     */
    public Integer getIdClasse() {
        return idClasse;
    }

    /**
     * Mutateur idClasse.
     *
     * @param idClasse Le idClasse à modifier
     */
    public void setIdClasse(Integer idClasse) {
        this.idClasse = idClasse;
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
     * Accesseur mode.
     * @return mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * Mutateur mode.
     * @param mode Le mode à modifier
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Accesseur typeClasseGroupe.
     * @return typeClasseGroupe
     */
    public TypeGroupe getTypeClasseGroupe() {
        return typeClasseGroupe;
    }

    /**
     * Mutateur typeClasseGroupe.
     * @param typeClasseGroupe Le typeClasseGroupe à modifier
     */
    public void setTypeClasseGroupe(TypeGroupe typeClasseGroupe) {
        this.typeClasseGroupe = typeClasseGroupe;
    }

    /**
     * Accesseur oldIdSequence.
     * @return oldIdSequence
     */
    public Integer getOldIdSequence() {
        return oldIdSequence;
    }

    /**
     * Mutateur oldIdSequence.
     * @param oldIdSequence Le oldIdSequence à modifier
     */
    public void setOldIdSequence(Integer oldIdSequence) {
        this.oldIdSequence = oldIdSequence;
    }

    /**
     * Accesseur oldIdGroupeClasse.
     * @return oldIdGroupeClasse
     */
    public Integer getOldIdGroupeClasse() {
        return oldIdGroupeClasse;
    }

    /**
     * Mutateur oldIdGroupeClasse.
     * @param oldIdGroupeClasse Le oldIdGroupeClasse à modifier
     */
    public void setOldIdGroupeClasse(Integer oldIdGroupeClasse) {
        this.oldIdGroupeClasse = oldIdGroupeClasse;
    }

    /**
     * Accesseur oldIdEnseignement.
     * @return oldIdEnseignement
     */
    public Integer getOldIdEnseignement() {
        return oldIdEnseignement;
    }

    /**
     * Mutateur oldIdEnseignement.
     * @param oldIdEnseignement Le oldIdEnseignement à modifier
     */
    public void setOldIdEnseignement(Integer oldIdEnseignement) {
        this.oldIdEnseignement = oldIdEnseignement;
    }

    /**
     * @param oldTypeGroupeSelectionne the oldTypeGroupeSelectionne to set
     */
    public void setOldTypeGroupeSelectionne(TypeGroupe oldTypeGroupeSelectionne) {
        this.oldTypeGroupeSelectionne = oldTypeGroupeSelectionne;
    }

    /**
     * @return the oldTypeGroupeSelectionne
     */
    public TypeGroupe getOldTypeGroupeSelectionne() {
        return oldTypeGroupeSelectionne;
    }
    
}
