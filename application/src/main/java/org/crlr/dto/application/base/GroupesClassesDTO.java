/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GroupesClassesDTO.java,v 1.3 2010/04/19 13:42:15 jerome.carriere Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * GroupesClassesDTO.
 *
 * @author breytond.
 * @version $Revision: 1.3 $
  */
public class GroupesClassesDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = -4287704845359407065L;

    /** code du groupe ou de la classe. */
    private String code;

    /** Intitulé du groupe ou de la classe. */
    private String designation;

    /** identifiant du groupe ou de la classe. */
    private Integer id;
    
    /** Vrai ou faux est une classe. **/
    private TypeGroupe typeGroupe; 
    
    private Integer idAnneeScolaire;

    
    public GroupesClassesDTO() {
        
    }
    
    public GroupesClassesDTO(Integer id, String code, TypeGroupe typeGroupe, String designation) {
        this.id = id;
        this.code = code;
        this.typeGroupe = typeGroupe;
        this.designation = designation;
    }

    public GroupesClassesDTO(
            Integer idClasse, String codeClasse, String designationClasse, 
            Integer idGroupe, String codeGroupe, String designationGroupe) {
        
        if (idClasse != null) {
            this.typeGroupe = TypeGroupe.CLASSE;
            this.id = idClasse;
            this.designation = designationClasse;
            this.code = codeClasse;
        } else {
            this.typeGroupe = TypeGroupe.GROUPE;
            this.id = idGroupe;
            this.designation = designationGroupe;
            this.code = codeGroupe;
        }
    }
    
    /**
     * Accesseur code.
     * @return le code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Mutateur code.
     * @param code le code à modifier.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Accesseur intitule.
     * @return le intitule.
     */
    public String getIntitule() {
        return designation;
    }

    /**
     * Mutateur intitule.
     * @param intitule le intitule à modifier.
     */
    public void setIntitule(String intitule) {
        this.designation = intitule;
    }

    /**
     * Accesseur id.
     * @return le id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur id.
     * @param id le id à modifier.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur vraiOuFauxClasse.
     * @return vrai si classe, faux autrement
     */
    public Boolean getVraiOuFauxClasse() {
        if (TypeGroupe.CLASSE == getTypeGroupe()) {
            return true;
        }
        
        if (TypeGroupe.GROUPE == getTypeGroupe()) {
            return false;
        }
    
        return false;
    }
    
    /**
     * @return typegroup
     */
    public TypeGroupe getTypeGroupe() {
        
        return this.typeGroupe;
    }
    
    /**
     * @param typeGroupe tg
     */
    public void setTypeGroupe(TypeGroupe typeGroupe) {
        this.typeGroupe = typeGroupe;
    }

 

    /**
     * @return the idAnneeScolaire
     */
    public Integer getIdAnneeScolaire() {
        return idAnneeScolaire;
    }

    /**
     * @param idAnneeScolaire the idAnneeScolaire to set
     */
    public void setIdAnneeScolaire(Integer idAnneeScolaire) {
        this.idAnneeScolaire = idAnneeScolaire;
    }

    /**
     * @return the designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * @param designation the designation to set
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
