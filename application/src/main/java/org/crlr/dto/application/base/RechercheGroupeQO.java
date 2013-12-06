/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheGroupeQO.java,v 1.4 2009/05/26 08:06:30 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * RechercheGroupeQO.
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class RechercheGroupeQO implements Serializable {
    /**  */
    private static final long serialVersionUID = -3304397775286509291L;

    /**
     * Le code du groupe.
     */
    private String codeGroupe;
    
    /**
     * Le code de la classe associé au groupe.
     */
    private String codeClasse;

    /**
     * L'id du groupe.
     */
    private Integer idGroupe;
    
    /**
     *L'id de la classe associé au groupe.
     */
    private Integer idClasse;

    /**
     * L'id de l'enseignant.
     */
    private Integer idEnseignant;
    
    /**
     * Pour savoir si on doit faire la vérification ou pas.
     */
    private boolean verif;
    
    /** true si la consultation est de type archive. */
    private Boolean archive;
    
    /** L'exercice scolaire. */
    private String exercice;

    /**
     * 
     * Constructeur.
     */
    public RechercheGroupeQO() {
        verif = true;
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
     * Accesseur codeClasse.
     * @return codeClasse
     */
    public String getCodeClasse() {
        return codeClasse;
    }

    /**
     * Mutateur codeClasse.
     * @param codeClasse Le codeClasse à modifier
     */
    public void setCodeClasse(String codeClasse) {
        this.codeClasse = codeClasse;
    }

    /**
     * Accesseur idClasse.
     * @return idClasse
     */
    public Integer getIdClasse() {
        return idClasse;
    }

    /**
     * Mutateur idClasse.
     * @param idClasse Le idClasse à modifier
     */
    public void setIdClasse(Integer idClasse) {
        this.idClasse = idClasse;
    }

    /**
     * Accesseur verif.
     * @return verif
     */
    public boolean isVerif() {
        return verif;
    }

    /**
     * Mutateur verif.
     * @param verif Le verif à modifier
     */
    public void setVerif(boolean verif) {
        this.verif = verif;
    }

    /**
     * Accesseur archive.
     * @return le archive.
     */
    public Boolean getArchive() {
        return archive;
    }

    /**
     * Mutateur archive.
     * @param archive le archive à modifier.
     */
    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    /**
     * Accesseur exercice.
     * @return le exercice.
     */
    public String getExercice() {
        return exercice;
    }

    /**
     * Mutateur exercice.
     * @param exercice le exercice à modifier.
     */
    public void setExercice(String exercice) {
        this.exercice = exercice;
    }
    
}
