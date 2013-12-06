/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheEnseignementPopupQO.java,v 1.5 2010/04/12 04:30:13 jerome.carriere Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;
import java.util.List;

/**
 * Un QO pour la recherche des enseignements à présenter au sein d'une popup.
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
public class RechercheEnseignementPopupQO implements Serializable {
    /**  */
    private static final long serialVersionUID = -2853634389687218412L;

    /** critère temporaire de selection du type de groupe. */
    private TypeGroupe typeGroupeSelectionne;

    /** groupe ou classe sélectionnée dans la popup. */
    private GroupesClassesDTO groupeClasseSelectionne;
    
    /** groupes sélectionnés. */
    private List<GroupeDTO> groupesSelectionne;

    /** Id enseignant. */
    private Integer idEnseignant;
    
    /** Id inspecteur. */
    private Integer idInspecteur;


    /** Id de l'établissement de l'enseignant. */
    private Integer idEtablissement;

    /** Fonctionnement en mode archive. */
    private Boolean archive;
    
    /** Id de l'annee archive .*/ 
    private String exercice;
    
    /**
     * 
     * Constructeur.
     *
     */
    public RechercheEnseignementPopupQO() {
        groupeClasseSelectionne = new GroupesClassesDTO();
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
     * Accesseur idEtablissement.
     *
     * @return idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur idEtablissement.
     *
     * @param idEtablissement Le idEtablissement à modifier
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    

    /**
     * Accesseur groupeClasseSelectionne.
     *
     * @return groupeClasseSelectionne
     */
    public GroupesClassesDTO getGroupeClasseSelectionne() {
        return groupeClasseSelectionne;
    }

    /**
     * Mutateur groupeClasseSelectionne.
     *
     * @param groupeClasseSelectionne groupeClasseSelectionne à modifier
     */
    public void setGroupeClasseSelectionne(GroupesClassesDTO groupeClasseSelectionne) {
        this.groupeClasseSelectionne = groupeClasseSelectionne;
    }

    /**
     * Accesseur de groupesSelectionne.
     * @return le groupesSelectionne
     */
    public List<GroupeDTO> getGroupesSelectionne() {
        return groupesSelectionne;
    }

    /**
     * Mutateur de groupesSelectionne.
     * @param groupesSelectionne le groupesSelectionne à modifier.
     */
    public void setGroupesSelectionne(List<GroupeDTO> groupesSelectionne) {
        this.groupesSelectionne = groupesSelectionne;
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

    /**
     * @param typeGroupeSelectionne the typeGroupeSelectionne to set
     */
    public void setTypeGroupeSelectionne(TypeGroupe typeGroupeSelectionne) {
        this.typeGroupeSelectionne = typeGroupeSelectionne;
    }

    /**
     * @return the typeGroupeSelectionne
     */
    public TypeGroupe getTypeGroupeSelectionne() {
        return typeGroupeSelectionne;
    }

    /**
     * Accesseur de archive {@link #archive}.
     * @return retourne archive
     */
    public Boolean getArchive() {
        return archive;
    }

    /**
     * Mutateur de archive {@link #archive}.
     * @param archive le archive to set
     */
    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    /**
     * Accesseur de exercice {@link #exercice}.
     * @return retourne exercice
     */
    public String getExercice() {
        return exercice;
    }

    /**
     * Mutateur de exercice {@link #exercice}.
     * @param exercice le exercice to set
     */
    public void setExercice(String exercice) {
        this.exercice = exercice;
    }


    

    
    
}
