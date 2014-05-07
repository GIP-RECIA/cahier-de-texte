/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: CouleurEnseignementClasseDTO.java,v 1.5 2009/04/28 08:02:29 vibertd Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Un DTO pour contenir une associtation :
 *  enseignant / etablissement / enseignement / goupe (ou) classe / couleur.
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
public class CouleurEnseignementClasseDTO implements Serializable {

    /**  Serial.  */
    private static final long serialVersionUID = 281686109419960684L;

    /**
     * id
     */
    private Integer id;
    
    /**
     * id de l'enseignant.
     */
    private Integer idEnseignant;

    /**
     * id de l'etablissement.
     */
    private Integer idEtablissement;

    /**
     * id de l'enseignement.
     */
    private Integer idEnseignement;
    
    private GroupesClassesDTO groupesClassesDTO;
    
    /** 
     * couleur 
     */
    private String couleur;

    /**
     * Constructeur par defaut.
     */
    public CouleurEnseignementClasseDTO() {
        groupesClassesDTO = new GroupesClassesDTO();
    }
    
    public boolean estRenseigne() {

        return idEnseignant != null &&
                idEtablissement != null && 
                idEnseignement != null && 
                groupesClassesDTO != null && 
                groupesClassesDTO.getId() != null &&
                couleur != null;
    }

    /**
     * Accesseur de idEnseignant {@link #idEnseignant}.
     * @return retourne idEnseignant
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur de idEnseignant {@link #idEnseignant}.
     * @param idEnseignant le idEnseignant to set
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur de idEtablissement {@link #idEtablissement}.
     * @return retourne idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur de idEtablissement {@link #idEtablissement}.
     * @param idEtablissement le idEtablissement to set
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur idEnseignement.
     * @return idEnseignement
     */
    public Integer getIdEnseignement() {
        return idEnseignement;
    }

    /**
     * Mutateur idEnseignement.
     * @param idEnseignement Le idEnseignement à modifier
     */
    public void setIdEnseignement(Integer idEnseignement) {
        this.idEnseignement = idEnseignement;
    }

    /**
     * @return the groupesClassesDTO
     */
    public GroupesClassesDTO getGroupesClassesDTO() {
        return groupesClassesDTO;
    }

    /**
     * @param groupesClassesDTO the groupesClassesDTO to set
     */
    public void setGroupesClassesDTO(GroupesClassesDTO groupesClassesDTO) {
        this.groupesClassesDTO = groupesClassesDTO;
    }    
    
    /**
     * Accesseur de couleur.
     * @return le couleur
     */
    public String getCouleur() {
        return couleur;
    }

    /**
     * Mutateur de couleur.
     * @param couleur la couleur à modifier.
     */
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
    
    
}
