/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheGroupeClassePopupQO.java,v 1.6 2009/07/30 15:09:26 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * Un QO pour la recherche des classes et groupes à presenter au sein d'une popup.
 *
 * @author $author$
 * @version $Revision: 1.6 $
 */
public class RechercheGroupeClassePopupQO implements Serializable {
    /**  */
    private static final long serialVersionUID = 8959768231994274190L;

    /** Type G ou C. */
    private TypeGroupe typeGroupeClasse;

    /** Id idEnseignant. */
    private Integer idEnseignant;

    /** Id de l'établissement de l'enseignant. */
    private Integer idEtablissement;

    /** Id de l'année scolaire courante. */
    private Integer idAnneeScolaire;

    /** Id de l'enseignement si sélectionné. */
    private Integer idEnseignement;

    /** Exercice. */
    private String exerciceScolaire;

    /** Archive. */
    private Boolean archive;
    
    /** Recherche à partir d'un inspecteur.*/
    private Integer idInspecteur;

    /**
     * idEnseignant.
     *
     * @return idEnseignant
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * idEnseignant.
     *
     * @param idEnseignant idEnseignant
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
     * Accesseur idAnneeScolaire.
     *
     * @return idAnneeScolaire
     */
    public Integer getIdAnneeScolaire() {
        return idAnneeScolaire;
    }

    /**
     * Mutateur idAnneeScolaire.
     *
     * @param idAnneeScolaire Le idAnneeScolaire à modifier
     */
    public void setIdAnneeScolaire(Integer idAnneeScolaire) {
        this.idAnneeScolaire = idAnneeScolaire;
    }

    /**
     * Accesseur archive.
     *
     * @return le archive.
     */
    public Boolean getArchive() {
        return archive;
    }

    /**
     * Mutateur archive.
     *
     * @param archive le archive à modifier.
     */
    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    /**
     * Accesseur exerciceScolaire.
     *
     * @return le exerciceScolaire.
     */
    public String getExerciceScolaire() {
        return exerciceScolaire;
    }

    /**
     * Mutateur exerciceScolaire.
     *
     * @param exerciceScolaire le exerciceScolaire à modifier.
     */
    public void setExerciceScolaire(String exerciceScolaire) {
        this.exerciceScolaire = exerciceScolaire;
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
     * @param idEnseignement idEnseignement à modifier
     */
    public void setIdEnseignement(Integer idEnseignement) {
        this.idEnseignement = idEnseignement;
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
     * @return the typeGroupeClasse
     */
    public TypeGroupe getTypeGroupeClasse() {
        return typeGroupeClasse;
    }

    /**
     * @param typeGroupeClasse the typeGroupeClasse to set
     */
    public void setTypeGroupeClasse(TypeGroupe typeGroupeClasse) {
        this.typeGroupeClasse = typeGroupeClasse;
    }
    
    
}
