/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SaveCouleurEnseignementClasseQO.java,v 1.10 2010/04/12 01:34:33 ent_breyton Exp $
 */

package org.crlr.dto.application.sequence;

import java.io.Serializable;
import java.util.Date;

import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.web.dto.TypeCouleur;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.10 $
 */
public class SaveCouleurEnseignementClasseQO implements Serializable {
    /**  */
    private static final long serialVersionUID = -3529912643601332234L;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idEnseignant;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idEtablissement;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idEnseignement;

    /** Code de classe ou de groupe. */
    private GroupesClassesDTO classeGroupe;
    
    /** Type couleur */
    private TypeCouleur typeCouleur;
    
    /**
     * 
     * Constructeur.
     */
    public SaveCouleurEnseignementClasseQO() {
    }
    
    /**
     * Accesseur classeGroupe.
     * @return le classeGroupe.
     */
    public GroupesClassesDTO getClasseGroupe() {
        return classeGroupe;
    }

    /**
     * Mutateur classeGroupe.
     * @param classeGroupe le classeGroupe à modifier.
     */
    public void setClasseGroupe(GroupesClassesDTO classeGroupe) {
        this.classeGroupe = classeGroupe;
    }

    /**
     * Accesseur idClasseGroupe.
     * @return le idClasseGroupe.
     */
    public Integer getIdClasseGroupe() {
        return getClasseGroupe().getId();
    }


    /**
     * Accesseur idEnseignant.
     * @return le idEnseignant.
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur idEnseignant.
     * @param idEnseignant le idEnseignant à modifier.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur idEtablissement.
     * @return le idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur de idEtablissement.
     * @param idEtablissement le idEtablissement à modifier.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur enseignement.
     * @return le enseignement.
     */
    public Integer getIdEnseignement() {
        return idEnseignement;
    }

    /**
     * Mutateur enseignement.
     * @param enseignement le enseignement à modifier.
     */
    public void setIdEnseignement(Integer idEnseignement) {
        this.idEnseignement = idEnseignement;
    }

    /**
     * @return the typeCouleur
     */
    public TypeCouleur getTypeCouleur() {
        return typeCouleur;
    }

    /**
     * @param typeCouleur the typeCouleur to set
     */
    public void setTypeCouleur(TypeCouleur typeCouleur) {
        this.typeCouleur = typeCouleur;
    }
    
}
