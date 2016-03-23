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
 * QO des critères de recherche de couleur.
 *
 * @author breytond.
 * @version $Revision: 1.6 $
 */
public class RechercheCouleurEnseignementClasseQO implements Serializable {
    /**  */
    private static final long serialVersionUID = -3088774567416299025L;

    /** Id de l'enseignant. */
    private Integer idEnseignant;
    
    /** Id de l'etablissement. */
    private Integer idEtablissement;
    
    /** Id de l'enseignement. */
    private Integer idEnseignement;
    
    private GroupesClassesDTO groupeClasse;


    public RechercheCouleurEnseignementClasseQO() {
    }

    /**
     * Accesseur codeClasseGroupe.
     *
     * @return le codeClasseGroupe.
     */
    public String getCodeClasseGroupe() {
        return groupeClasse == null ? null : groupeClasse.getCode();
    }

    /**
     * Mutateur codeClasseGroupe.
     *
     * @param codeClasseGroupe le codeClasseGroupe à modifier.
     */
    public void setCodeClasseGroupe(String codeClasseGroupe) {
        if (groupeClasse == null) {
            groupeClasse = new GroupesClassesDTO();
        }
        groupeClasse.setCode(codeClasseGroupe);  
    }

    /**
     * Accesseur typeGroupe.
     *
     * @return le typeGroupe.
     */
    public TypeGroupe getTypeGroupe() {
        return groupeClasse == null ? null : groupeClasse.getTypeGroupe();
    }

    /**
     * Mutateur typeGroupe.
     *
     * @param typeGroupe le typeGroupe à modifier.
     */
    public void setTypeGroupe(TypeGroupe typeGroupe) {
        if (groupeClasse == null) {
            groupeClasse = new GroupesClassesDTO();
        }
        groupeClasse.setTypeGroupe(typeGroupe);
    }

    /**
     * Accesseur idClasseGroupe.
     * @return idClasseGroupe
     */
    public Integer getIdClasseGroupe() {
        return groupeClasse == null ? null : groupeClasse.getId();
    }

    /**
     * Mutateur idClasseGroupe.
     * @param idClasseGroupe Le idClasseGroupe à modifier
     */
    public void setIdClasseGroupe(Integer idClasseGroupe) {
        if (groupeClasse == null) {
            groupeClasse = new GroupesClassesDTO();
        }
        groupeClasse.setId(idClasseGroupe);
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
     * @return the groupeClasse
     */
    public GroupesClassesDTO getGroupeClasse() {
        return groupeClasse;
    }

    /**
     * @param groupeClasse the groupeClasse to set
     */
    public void setGroupeClasse(GroupesClassesDTO groupeClasse) {
        this.groupeClasse = groupeClasse;
    }
    
}
