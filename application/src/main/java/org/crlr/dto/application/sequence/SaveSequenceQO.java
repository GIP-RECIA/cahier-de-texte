/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SaveSequenceQO.java,v 1.10 2010/04/12 01:34:33 ent_breyton Exp $
 */

package org.crlr.dto.application.sequence;

import java.io.Serializable;
import java.util.Date;

import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.TypeGroupe;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.10 $
 */
public class SaveSequenceQO implements Serializable {
    /**  */
    private static final long serialVersionUID = -2429912643601332223L;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer id;

    /** DOCUMENTATION INCOMPLETE! */
    private String code;

    /** DOCUMENTATION INCOMPLETE! */
    private String intitule;

    /** DOCUMENTATION INCOMPLETE! */
    private Date dateDebut;

    /** DOCUMENTATION INCOMPLETE! */
    private Date dateFin;

    /** Code de classe ou de groupe. */
    private GroupesClassesDTO classeGroupeSelectionne;
    

    /** DOCUMENTATION INCOMPLETE! */
    private String description;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idEnseignant;

    
    /** DOCUMENTATION INCOMPLETE! */
    private EnseignementDTO enseignementSelectionne;
    
    /** DOCUMENTATION INCOMPLETE! */
    private String mode;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer oldIdSequence;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer oldIdGroupeClasse;

    /** DOCUMENTATION INCOMPLETE! */
    private TypeGroupe oldTypeGroupeSelectionne;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer oldIdEnseignement;
    
    /** DOCUMENTATION INCOMPLETE! */
    private Boolean seanceAssociee;
    
    /** id de l'établissement. */
    private Integer idEtablissement;
    

    
    /**
     * 
     * Constructeur.
     */
    public SaveSequenceQO() {
        seanceAssociee = false;
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
        return intitule;
    }

    /**
     * Mutateur intitule.
     * @param intitule le intitule à modifier.
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    /**
     * Accesseur dateDebut.
     * @return le dateDebut.
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * Mutateur dateDebut.
     * @param dateDebut le dateDebut à modifier.
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Accesseur dateFin.
     * @return le dateFin.
     */
    public Date getDateFin() {
        return dateFin;
    }

    /**
     * Mutateur dateFin.
     * @param dateFin le dateFin à modifier.
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }



    /**
     * Accesseur classeGroupeSelectionne.
     * @return le classeGroupeSelectionne.
     */
    public GroupesClassesDTO getClasseGroupeSelectionne() {
        return classeGroupeSelectionne;
    }

    /**
     * Mutateur classeGroupeSelectionne.
     * @param classeGroupeSelectionne le classeGroupeSelectionne à modifier.
     */
    public void setClasseGroupeSelectionne(GroupesClassesDTO classeGroupeSelectionne) {
        this.classeGroupeSelectionne = classeGroupeSelectionne;
    }

    /**
     * Accesseur idClasseGroupe.
     * @return le idClasseGroupe.
     */
    public Integer getIdClasseGroupe() {
        return getClasseGroupeSelectionne().getId();
    }


    /**
     * Accesseur description.
     * @return le description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutateur description.
     * @param description le description à modifier.
     */
    public void setDescription(String description) {
        this.description = description;
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
     * Accesseur idEnseignement.
     * @return le idEnseignement.
     */
    public Integer getIdEnseignement() {
        return getEnseignementSelectionne().getId();
    }

    

    /**
     * Accesseur enseignementSelectionne.
     * @return le enseignementSelectionne.
     */
    public EnseignementDTO getEnseignementSelectionne() {
        return enseignementSelectionne;
    }

    /**
     * Mutateur enseignementSelectionne.
     * @param enseignementSelectionne le enseignementSelectionne à modifier.
     */
    public void setEnseignementSelectionne(EnseignementDTO enseignementSelectionne) {
        this.enseignementSelectionne = enseignementSelectionne;
    }

    /**
     * Accesseur mode.
     * @return le mode.
     */
    public String getMode() {
        return mode;
    }

    /**
     * Mutateur mode.
     * @param mode le mode à modifier.
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Accesseur oldIdSequence.
     * @return le oldIdSequence.
     */
    public Integer getOldIdSequence() {
        return oldIdSequence;
    }

    /**
     * Mutateur oldIdSequence.
     * @param oldIdSequence le oldIdSequence à modifier.
     */
    public void setOldIdSequence(Integer oldIdSequence) {
        this.oldIdSequence = oldIdSequence;
    }

    /**
     * Accesseur oldIdGroupeClasse.
     * @return le oldIdGroupeClasse.
     */
    public Integer getOldIdGroupeClasse() {
        return oldIdGroupeClasse;
    }

    /**
     * Mutateur oldIdGroupeClasse.
     * @param oldIdGroupeClasse le oldIdGroupeClasse à modifier.
     */
    public void setOldIdGroupeClasse(Integer oldIdGroupeClasse) {
        this.oldIdGroupeClasse = oldIdGroupeClasse;
    }

  

    /**
     * Accesseur oldIdEnseignement.
     * @return le oldIdEnseignement.
     */
    public Integer getOldIdEnseignement() {
        return oldIdEnseignement;
    }

    /**
     * Mutateur oldIdEnseignement.
     * @param oldIdEnseignement le oldIdEnseignement à modifier.
     */
    public void setOldIdEnseignement(Integer oldIdEnseignement) {
        this.oldIdEnseignement = oldIdEnseignement;
    }

    /**
     * Accesseur seanceAssociee.
     * @return le seanceAssociee.
     */
    public Boolean getSeanceAssociee() {
        return seanceAssociee;
    }

    /**
     * Mutateur seanceAssociee.
     * @param seanceAssociee le seanceAssociee à modifier.
     */
    public void setSeanceAssociee(Boolean seanceAssociee) {
        this.seanceAssociee = seanceAssociee;
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
     * @return the oldTypeGroupeSelectionne
     */
    public TypeGroupe getOldTypeGroupeSelectionne() {
        return oldTypeGroupeSelectionne;
    }

    /**
     * @param oldTypeGroupeSelectionne the oldTypeGroupeSelectionne to set
     */
    public void setOldTypeGroupeSelectionne(TypeGroupe oldTypeGroupeSelectionne) {
        this.oldTypeGroupeSelectionne = oldTypeGroupeSelectionne;
    }
    
}
