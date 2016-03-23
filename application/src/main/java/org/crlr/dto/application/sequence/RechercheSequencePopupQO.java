/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheSequencePopupQO.java,v 1.3 2009/04/22 13:24:49 vibertd Exp $
 */

package org.crlr.dto.application.sequence;

import java.io.Serializable;

import org.crlr.dto.application.base.TypeGroupe;

/**
 * QO générique pour la recherche de séquence dans la popup.
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class RechercheSequencePopupQO implements Serializable {
    /**  */
    private static final long serialVersionUID = -8941029536959758428L;
    
    /**
     * Id de l'année scolaire courante.
     */
    private Integer idAnneeScolaire;
    
    /**
     * Id enseignement.
     */
    private Integer idEnseignement;
    
    /** Id de l'établissement de l'enseignant. */
    private Integer idEtablissement;

    /**
     * Type G ou C.
     */
    private TypeGroupe typeGroupeClasse;
    
    /**
     * Id enseignant.
     */
    private Integer idEnseignant;

    /**
     * Id du groupe ou de la classe.
     */
    private Integer idClasseGroupe;

    /**
     * idEnseignement.
     *
     * @return idEnseignement
     */
    public Integer getIdEnseignement() {
        return idEnseignement;
    }

    /**
     * idEnseignement.
     *
     * @param idEnseignement idEnseignement
     */
    public void setIdEnseignement(Integer idEnseignement) {
        this.idEnseignement = idEnseignement;
    }

    /**
     * typeGroupeClasse.
     *
     * @return typeGroupeClasse
     */
    public TypeGroupe getTypeGroupeClasse() {
        return typeGroupeClasse;
    }

    /**
     * typeGroupeClasse.
     *
     * @param typeGroupeClasse typeGroupeClasse
     */
    public void setTypeGroupeClasse(TypeGroupe typeGroupeClasse) {
        this.typeGroupeClasse = typeGroupeClasse;
    }

    /**
     * idClasseGroupe.
     *
     * @return idClasseGroupe
     */
    public Integer getIdClasseGroupe() {
        return idClasseGroupe;
    }

    /**
     * idClasseGroupe.
     *
     * @param idClasseGroupe idClasseGroupe
     */
    public void setIdClasseGroupe(Integer idClasseGroupe) {
        this.idClasseGroupe = idClasseGroupe;
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
     * Accesseur idEtablissement.
     * @return idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur idEtablissement.
     * @param idEtablissement Le idEtablissement à modifier
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur idAnneeScolaire.
     * @return idAnneeScolaire
     */
    public Integer getIdAnneeScolaire() {
        return idAnneeScolaire;
    }

    /**
     * Mutateur idAnneeScolaire.
     * @param idAnneeScolaire Le idAnneeScolaire à modifier
     */
    public void setIdAnneeScolaire(Integer idAnneeScolaire) {
        this.idAnneeScolaire = idAnneeScolaire;
    }
    
}
