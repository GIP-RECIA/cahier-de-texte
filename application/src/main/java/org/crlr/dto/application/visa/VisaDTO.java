/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: VisaDTO.java,v 1.2 2009/04/22 13:14:23 ent_breyton Exp $
 */

package org.crlr.dto.application.visa;

import java.util.Date;

import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;

/**
 * Classe DTO d'un visa pour un cahier de texte.
 * Un cahier de texte = Enseignant + Enseignement + Classe (ou groupe) 
 * @author G-SAFIR-FRMP
 */
public class VisaDTO {
    
    /**
     * 
     *
     */
    public enum VisaProfil {
        ENTDirecteur,
        ENTInspecteur;
        
        public static String getLibelleProfilDernierVisa(VisaProfil profil) {
            if (profil == null) {
                return "";
            } else if (profil == VisaProfil.ENTDirecteur) {
                return "Dir.Etab.";
            } else if (profil == VisaProfil.ENTInspecteur) {
                return "Inspection";
            } else {
                return "";
            }
        }
    }
    
    /** id du visa. */
    private Integer id;
    
    /** Id Enseignant. */
    private Integer  idEnseignant;

    /** Id Etablissement. */
    private Integer  idEtablissement;
    
    /** Classe ou groupe. */
    private GroupesClassesDTO classeGroupe;
    
    /** Enseignement. */
    private EnseignementDTO enseignementDTO;
    
    /** Date de dernier mise à jour sur les seances du cahier de texte. */
    private Date dateMaj;
    
    /** Date du visa. */
    private Date dateVisa;
    
    /** Type du visa. */
    private TypeVisa typeVisa;
    
    /** Profil du visa (ENTDirecteur ou ENTInspecteur). */
    private VisaProfil profil;
    
    /** Indique que le visa a ete modifie. */
    private Boolean estModifie; 

    /** Indique que le visa est perime (une modif a eu lieu apres le dernier visa. */
    private Boolean estPerime;

    /**
     * Constructeur.
     */
    public VisaDTO() {
        estPerime = false;
        estModifie = false;
        this.enseignementDTO = new EnseignementDTO();
        this.classeGroupe = new GroupesClassesDTO();
    }

    /**
     * Accesseur de classeGroupe {@link #classeGroupe}.
     * @return retourne classeGroupe
     */
    public GroupesClassesDTO getClasseGroupe() {
        return classeGroupe;
    }

    /**
     * Mutateur de classeGroupe {@link #classeGroupe}.
     * @param classeGroupe le classeGroupe to set
     */
    public void setClasseGroupe(GroupesClassesDTO classeGroupe) {
        this.classeGroupe = classeGroupe;
    }

    
    /**
     * Accesseur de dateMaj {@link #dateMaj}.
     * @return retourne dateMaj
     */
    public Date getDateMaj() {
        return dateMaj;
    }

    /**
     * Mutateur de dateMaj {@link #dateMaj}.
     * @param dateMaj le dateMaj to set
     */
    public void setDateMaj(Date dateMaj) {
        this.dateMaj = dateMaj;
    }

    /**
     * Accesseur de dateVisa {@link #dateVisa}.
     * @return retourne dateVisa
     */
    public Date getDateVisa() {
        return dateVisa;
    }

    /**
     * Mutateur de dateVisa {@link #dateVisa}.
     * @param dateVisa le dateVisa to set
     */
    public void setDateVisa(Date dateVisa) {
        this.dateVisa = dateVisa;
    }

    /**
     * Accesseur de typeVisa {@link #typeVisa}.
     * @return retourne typeVisa
     */
    public TypeVisa getTypeVisa() {
        return typeVisa;
    }

    /**
     * Mutateur de typeVisa {@link #typeVisa}.
     * @param typeVisa le typeVisa to set
     */
    public void setTypeVisa(TypeVisa typeVisa) {
        this.typeVisa = typeVisa;
    }

    /**
     * Accesseur de profil {@link #profil}.
     * @return retourne profil
     */
    public VisaProfil getProfil() {
        return profil;
    }

    /**
     * Mutateur de profil {@link #profil}.
     * @param profil le profil to set
     */
    public void setProfil(VisaProfil profil) {
        this.profil = profil;
    }
    
    /**
     * Accesseur à la version affichable du profil.
     * @return le libelle correspondant au profil
     */
    public String getLibelleProfilVisa() {
        return VisaProfil.getLibelleProfilDernierVisa(this.profil);
    }
    
    /**
     * Vérifie si un VISA a ete pose sur ce cahier de texte. 
     * Par defaut les visa sont cres sans profil, type ou date. 
     * @return true / false
     */
    public Boolean getVraiOuFauxVisaPose() {
        return dateVisa != null;
    }

    /**
     * Accesseur de idVisa {@link #idVisa}.
     * @return retourne idVisa
     */
    public Integer getIdVisa() {
        return id;
    }

    /**
     * Mutateur de idVisa {@link #idVisa}.
     * @param idVisa le idVisa to set
     */
    public void setId(Integer idVisa) {
        this.id = idVisa;
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
     * Accesseur de estModifie {@link #estModifie}.
     * @return retourne estModifie
     */
    public Boolean getEstModifie() {
        return estModifie;
    }

    /**
     * Mutateur de estModifie {@link #estModifie}.
     * @param estModifie le estModifie to set
     */
    public void setEstModifie(Boolean estModifie) {
        this.estModifie = estModifie;
    }

    /**
     * Accesseur de estPerime {@link #estPerime}.
     * @return retourne estPerime
     */
    public Boolean getEstPerime() {
        return estPerime;
    }

    /**
     * Mutateur de estPerime {@link #estPerime}.
     * @param estPerime le estPerime to set
     */
    public void setEstPerime(Boolean estPerime) {
        this.estPerime = estPerime;
    }
    
    /**
     * @return si le visa a été posé.
     */
    public boolean getExists() {
        return getTypeVisa() != null && getDateVisa() != null;
    }
    
    /**
     * @param dateMaj dm
     */
    public void calculerEstPerime(Date dateMaj) {
        setEstPerime(dateMaj != null && dateVisa !=null && dateMaj.getTime() > dateVisa.getTime());
    }

    /**
     * @return the enseignementDTO
     */
    public EnseignementDTO getEnseignementDTO() {
        return enseignementDTO;
    }

    /**
     * @param enseignementDTO the enseignementDTO to set
     */
    public void setEnseignementDTO(EnseignementDTO enseignementDTO) {
        this.enseignementDTO = enseignementDTO;
    }
    
    
}
