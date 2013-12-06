/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AnneeScolaireDTO.java,v 1.3 2010/03/29 09:29:35 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * Fichier de stockage.
 *
 * @author breytond.
 * @version $Revision: 1.3 $
  */
public class FichierStockageDTO implements Serializable {
    /** Serial UID. */
    private static final long serialVersionUID = -6236127360926292999L; 

    /** Exemple :.
     * Dossier : /webdav/shared/userfiles/depot_7/kac0013r/mes_documents/Mes_cours/Mon_dossier_aa_partager/
     * Fichier : /webdav/shared/userfiles/depot_7/kac0013r/mes_documents/Mes_cours/ColonisationTS_documents.doc
     */
    private String cheminComplet;
    
    /** Exemple :.
     * Dossier : Mes_cours/Mon_dossier_aa_partager/
     * Fichier : Mes_cours/ColonisationTS_documents.doc
     */
    private String cheminRelatif;
    
    private TypeFichier type;
    
    /** Exemple :.
     * Dossier : Mon_dossier_aa_partager
     * Fichier : ColonisationTS_documents.doc
     */
    private String nom;
    
    private String repertoireParent;

    /**
     * Accesseur cheminComplet.
     * @return le cheminComplet.
     */
    public String getCheminComplet() {
        return cheminComplet;
    }

    /**
     * Mutateur cheminComplet.
     * @param cheminComplet le cheminComplet à modifier.
     */
    public void setCheminComplet(String cheminComplet) {
        this.cheminComplet = cheminComplet;
    }

    /**
     * Accesseur cheminRelatif.
     * @return le cheminRelatif.
     */
    public String getCheminRelatif() {
        return cheminRelatif;
    }

    /**
     * Mutateur cheminRelatif.
     * @param cheminRelatif le cheminRelatif à modifier.
     */
    public void setCheminRelatif(String cheminRelatif) {
        this.cheminRelatif = cheminRelatif;
    }

    /**
     * Accesseur type.
     * @return le type.
     */
    public TypeFichier getType() {
        return type;
    }

    /**
     * Mutateur type.
     * @param type le type à modifier.
     */
    public void setType(TypeFichier type) {
        this.type = type;
    }

    /**
     * Accesseur nom.
     * @return le nom.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Mutateur nom.
     * @param nom le nom à modifier.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Accesseur repertoireParent.
     * @return le repertoireParent.
     */
    public String getRepertoireParent() {
        return repertoireParent;
    }

    /**
     * Mutateur repertoireParent.
     * @param repertoireParent le repertoireParent à modifier.
     */
    public void setRepertoireParent(String repertoireParent) {
        this.repertoireParent = repertoireParent;
    }
    
    /**
     * Methode d'affichage.
     * @return le nom du fichier ou dossier.
     */
    public String toString() {
        return nom;
    }
    
    
}
