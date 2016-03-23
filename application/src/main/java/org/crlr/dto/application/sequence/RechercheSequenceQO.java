/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheSequenceQO.java,v 1.6 2009/11/04 08:50:44 weberent Exp $
 */

package org.crlr.dto.application.sequence;

import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.TypeGroupe;

import java.io.Serializable;

import java.util.Date;

/**
 * QO des critères de recherche de séquences.
 *
 * @author breytond.
 * @version $Revision: 1.6 $
 */
public class RechercheSequenceQO implements Serializable {
    /**  */
    private static final long serialVersionUID = -2088774567416299024L;

    /** Code de la séquence. */
    private String codeSequence;

    /** Id de l'enseignant. */
    private Integer idEnseignant;
    
    /** Id de l'etablissement. */
    private Integer idEtablissement;
    
    /** Id de l'enseignement. */
    private Integer idEnseignement;
    
    /** Code de l'enseignement. */
    private String codeEnseignement;

    /** Date de début. */
    private Date dateDebut;

    /** Date de fin. */
    private Date dateFin;

    private GroupesClassesDTO groupeClasseSelectionne;

    /**
     * Accesseur codeSequence.
     *
     * @return le codeSequence.
     */
    public String getCodeSequence() {
        return codeSequence;
    }

    /**
     * Mutateur codeSequence.
     *
     * @param codeSequence le codeSequence à modifier.
     */
    public void setCodeSequence(String codeSequence) {
        this.codeSequence = codeSequence;
    }

    /**
     * Accesseur dateDebut.
     *
     * @return le dateDebut.
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * Mutateur dateDebut.
     *
     * @param dateDebut le dateDebut à modifier.
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Accesseur dateFin.
     *
     * @return le dateFin.
     */
    public Date getDateFin() {
        return dateFin;
    }

    /**
     * Mutateur dateFin.
     *
     * @param dateFin le dateFin à modifier.
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * Accesseur codeClasseGroupe.
     *
     * @return le codeClasseGroupe.
     */
    public String getCodeClasseGroupe() {
        return groupeClasseSelectionne == null ? null : groupeClasseSelectionne.getCode();
    }

    /**
     * Mutateur codeClasseGroupe.
     *
     * @param codeClasseGroupe le codeClasseGroupe à modifier.
     */
    public void setCodeClasseGroupe(String codeClasseGroupe) {
        if (groupeClasseSelectionne == null) {
            groupeClasseSelectionne = new GroupesClassesDTO();
        }
        groupeClasseSelectionne.setCode(codeClasseGroupe);  
    }

    /**
     * Accesseur typeGroupe.
     *
     * @return le typeGroupe.
     */
    public TypeGroupe getTypeGroupe() {
        return groupeClasseSelectionne == null ? null : groupeClasseSelectionne.getTypeGroupe();
    }

    /**
     * Mutateur typeGroupe.
     *
     * @param typeGroupe le typeGroupe à modifier.
     */
    public void setTypeGroupe(TypeGroupe typeGroupe) {
        if (groupeClasseSelectionne == null) {
            groupeClasseSelectionne = new GroupesClassesDTO();
        }
        groupeClasseSelectionne.setTypeGroupe(typeGroupe);
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
     * Accesseur idClasseGroupe.
     * @return idClasseGroupe
     */
    public Integer getIdClasseGroupe() {
        return groupeClasseSelectionne == null ? null : groupeClasseSelectionne.getId();
    }

    /**
     * Mutateur idClasseGroupe.
     * @param idClasseGroupe Le idClasseGroupe à modifier
     */
    public void setIdClasseGroupe(Integer idClasseGroupe) {
        if (groupeClasseSelectionne == null) {
            groupeClasseSelectionne = new GroupesClassesDTO();
        }
        groupeClasseSelectionne.setId(idClasseGroupe);
    }

    /**
     * Accesseur codeEnseignement.
     * @return codeEnseignement
     */
    public String getCodeEnseignement() {
        return codeEnseignement;
    }

    /**
     * Mutateur codeEnseignement.
     * @param codeEnseignement Le codeEnseignement à modifier
     */
    public void setCodeEnseignement(String codeEnseignement) {
        this.codeEnseignement = codeEnseignement;
    }

    /**
     * Accesseur idEnseignant.
     * @return idEnseignant
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur idEnseignant.
     * @param idEnseignant Le idEnseignant à modifier
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Mutateur idEtablissement.
     * @param idEtablissement Le idEtablissement à modifier
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur idEtablissement.
     * @return idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * @return the groupeClasseSelectionne
     */
    public GroupesClassesDTO getGroupeClasseSelectionne() {
        return groupeClasseSelectionne;
    }

    /**
     * @param groupeClasseSelectionne the groupeClasseSelectionne to set
     */
    public void setGroupeClasseSelectionne(GroupesClassesDTO groupeClasseSelectionne) {
        this.groupeClasseSelectionne = groupeClasseSelectionne;
    }
    
}
