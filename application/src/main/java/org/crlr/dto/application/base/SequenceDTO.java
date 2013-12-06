/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequenceDTO.java,v 1.5 2009/04/28 08:02:29 vibertd Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Un DOT pour contenir une sequence.
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
public class SequenceDTO implements Serializable, Identifiable {

    /**  Serial.  */
    private static final long serialVersionUID = 171686109419960683L;

    /** Id de la séquence. */
    private Integer id;

    /** Code de la séquence. */
    private String code;

    /** Date de début. */
    private Date dateDebut;

    /** Date de fin. */
    private Date dateFin;

    /** Intitulé de la séquence. */
    private String intitule;

    /** Description de la séquence. */
    private String description;

    private GroupesClassesDTO groupesClassesDTO;

    /**
     * id de l'enseignement.
     */
    private Integer idEnseignement;
    
    /**
     * Désignation de l'enseignement.
     */
    private String designationEnseignement;
    
    /** Id de l'enseignant. */
    private Integer idEnseignant;

    /**
     * Constructeur par defaut.
     */
    public SequenceDTO() {
        groupesClassesDTO = new GroupesClassesDTO();
    }
    
    public boolean estRenseigne() {

        return dateDebut != null &&
                dateFin != null && 
                groupesClassesDTO != null && 
                groupesClassesDTO.getId() != null &&
                code != null;
    }

    /**
     * Accesseur code.
     *
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * Mutateur code.
     *
     * @param code Le code à modifier
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Accesseur intitule.
     *
     * @return intitule
     */
    public String getIntitule() {
        return intitule;
    }

    /**
     * Mutateur intitule.
     *
     * @param intitule L'intitule à modifier
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    /**
     * Accesseur description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutateur description.
     *
     * @param description la description à modifier
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Accesseur id de la séquence.
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur id.
     *
     * @param id id à modifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur dateDebut.
     *
     * @return dateDebut
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * Mutateur dateDebut.
     *
     * @param dateDebut Le dateDebut à modifier
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Accesseur dateFin.
     *
     * @return dateFin
     */
    public Date getDateFin() {
        return dateFin;
    }

    /**
     * Mutateur dateFin.
     *
     * @param dateFin Le dateFin à modifier
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

   

    /**
     * Accesseur designationEnseignement.
     * @return designationEnseignement
     */
    public String getDesignationEnseignement() {
        return designationEnseignement;
    }

    /**
     * Mutateur designationEnseignement.
     * @param designationEnseignement Le designationEnseignement à modifier
     */
    public void setDesignationEnseignement(String designationEnseignement) {
        this.designationEnseignement = designationEnseignement;
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
     *  @return implemente pour le SelectOneDtoConverter. 
     */
    @Override
    public int hashCode(){
        return new HashCodeBuilder()
            .append(id)
            .toHashCode();
    }
    
    /** 
     * Nécesaire pour SelectOneDtoConverter.
     * @param obj : autre obj a comparer avec this.
     * @return true / false selon que les objets sont identiques.
     */
    @Override
    public boolean equals(final Object obj){
        if(obj instanceof SequenceDTO){
            final SequenceDTO other = (SequenceDTO) obj;
            return new EqualsBuilder()
                .append(id, other.id)
                .isEquals();
        } else{
            return false;
        }
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
     * @return d
     */
    public String getDesignationClasseGroupe() {
        return groupesClassesDTO.getDesignation();
    }
    
    /**
     * @return id
     */
    public Integer getIdClasseGroupe() {
        return groupesClassesDTO.getId();
    }
    
    /**
     * @return t
     */
    public TypeGroupe getTypeGroupe() {
        return groupesClassesDTO == null ? null : groupesClassesDTO.getTypeGroupe();
    }
    
    /**
     * @return cg
     */
    @Deprecated
    public String getCodeClasseGroupe() {
        return groupesClassesDTO.getCode();
    }
    
    
}
