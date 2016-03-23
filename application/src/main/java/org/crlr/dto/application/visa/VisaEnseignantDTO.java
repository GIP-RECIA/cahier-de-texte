/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: VisaEnseignantDTO.java,v 1.2 2009/04/22 13:14:23 ent_breyton Exp $
 */

package org.crlr.dto.application.visa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.visa.VisaDTO.VisaProfil;

/**
 */
public class VisaEnseignantDTO implements Serializable {

    /** Serial. */
    private static final long serialVersionUID = -4422344525717181275L;
    
    /** Enseignant. */
    private EnseignantDTO enseignant;
    
    /** Date de dernier mise à jour sur les seances du cahier de texte. */
    private Date dateDernierMaj;
    
    /** Id du visa sur lequel porte la dernière modification de seance. */
    private Integer idDernierMaj;
    
    /** Date du dernier visa. */
    private Date dateDernierVisa;
    
    /** Type du dernier visa. */
    private TypeVisa typeDernierVisa;
    
    /** Profil du dernier visa (ENTDirecteur ou ENTInspecteur). */
    private VisaProfil profilDernierVisa;
    
    /** liste des visa de l'enseignant. */
    private List<VisaDTO> listeVisa;

    /** Indique si la ligne enseignant est deplie ou non. */
    private Boolean vraiOuFauxCollapse;

    /** Indique si un des visa de l'enseignant est perime. */
    private Boolean vraiOuFauxExisteVisaPerime;
    
    /**
     * Constructeur.
     */
    public VisaEnseignantDTO() {
    }

    /**
     * Accesseur de enseignant {@link #enseignant}.
     * @return retourne enseignant
     */
    public EnseignantDTO getEnseignant() {
        return enseignant;
    }

    /**
     * Mutateur de enseignant {@link #enseignant}.
     * @param enseignant le enseignant to set
     */
    public void setEnseignant(EnseignantDTO enseignant) {
        this.enseignant = enseignant;
    }

    /**
     * Accesseur de listeVisa {@link #listeVisa}.
     * @return retourne listeVisa
     */
    public List<VisaDTO> getListeVisa() {
        return listeVisa;
    }

    /**
     * Mutateur de listeVisa {@link #listeVisa}.
     * @param listeVisa le listeVisa to set
     */
    public void setListeVisa(List<VisaDTO> listeVisa) {
        this.listeVisa = listeVisa;
    }

    /**
     * Accesseur de vraiOuFauxCollapse {@link #vraiOuFauxCollapse}.
     * @return retourne vraiOuFauxCollapse
     */
    public Boolean getVraiOuFauxCollapse() {
        return vraiOuFauxCollapse;
    }

    /**
     * Mutateur de vraiOuFauxCollapse {@link #vraiOuFauxCollapse}.
     * @param vraiOuFauxCollapse le vraiOuFauxCollapse to set
     */
    public void setVraiOuFauxCollapse(Boolean vraiOuFauxCollapse) {
        this.vraiOuFauxCollapse = vraiOuFauxCollapse;
    }

    /**
     * Accesseur de dateDernierMaj {@link #dateDernierMaj}.
     * @return retourne dateDernierMaj
     */
    public Date getDateDernierMaj() {
        return dateDernierMaj;
    }

    /**
     * Mutateur de dateDernierMaj {@link #dateDernierMaj}.
     * @param dateDernierMaj le dateDernierMaj to set
     */
    public void setDateDernierMaj(Date dateDernierMaj) {
        this.dateDernierMaj = dateDernierMaj;
    }

    /**
     * Accesseur de dateDernierVisa {@link #dateDernierVisa}.
     * @return retourne dateDernierVisa
     */
    public Date getDateDernierVisa() {
        return dateDernierVisa;
    }

    /**
     * Mutateur de dateDernierVisa {@link #dateDernierVisa}.
     * @param dateDernierVisa le dateDernierVisa to set
     */
    public void setDateDernierVisa(Date dateDernierVisa) {
        this.dateDernierVisa = dateDernierVisa;
    }

    /**
     * Accesseur de typeDernierVisa {@link #typeDernierVisa}.
     * @return retourne typeDernierVisa
     */
    public TypeVisa getTypeDernierVisa() {
        return typeDernierVisa;
    }

    /**
     * Mutateur de typeDernierVisa {@link #typeDernierVisa}.
     * @param typeDernierVisa le typeDernierVisa to set
     */
    public void setTypeDernierVisa(TypeVisa typeDernierVisa) {
        this.typeDernierVisa = typeDernierVisa;
    }

    /**
     * Accesseur de profilDernierVisa {@link #profilDernierVisa}.
     * @return retourne profilDernierVisa
     */
    public VisaProfil getProfilDernierVisa() {
        return profilDernierVisa;
    }

    /**
     * Mutateur de profilDernierVisa {@link #profilDernierVisa}.
     * @param profilDernierVisa le profilDernierVisa to set
     */
    public void setProfilDernierVisa(VisaProfil profilDernierVisa) {
        this.profilDernierVisa = profilDernierVisa;
    }
    
    /**
     * Accesseur à la version affichable du profil correspondant au dernier visa.
     * @return le libelle correspondant au profil
     */
    public String getLibelleProfilDernierVisa() {
        return VisaProfil.getLibelleProfilDernierVisa(this.profilDernierVisa);
    }

    /**
     * Accesseur de vraiOuFauxExisteVisaPerime {@link #vraiOuFauxExisteVisaPerime}.
     * @return retourne vraiOuFauxExisteVisaPerime
     */
    public Boolean getVraiOuFauxExisteVisaPerime() {
        return vraiOuFauxExisteVisaPerime;
    }

    /**
     * Mutateur de vraiOuFauxExisteVisaPerime {@link #vraiOuFauxExisteVisaPerime}.
     * @param vraiOuFauxExisteVisaPerime le vraiOuFauxExisteVisaPerime to set
     */
    public void setVraiOuFauxExisteVisaPerime(Boolean vraiOuFauxExisteVisaPerime) {
        this.vraiOuFauxExisteVisaPerime = vraiOuFauxExisteVisaPerime;
    }

    /**
     * Accesseur de idDernierMaj {@link #idDernierMaj}.
     * @return retourne idDernierMaj
     */
    public Integer getIdDernierMaj() {
        return idDernierMaj;
    }

    /**
     * Mutateur de idDernierMaj {@link #idDernierMaj}.
     * @param idDernierMaj le idDernierMaj to set
     */
    public void setIdDernierMaj(Integer idDernierMaj) {
        this.idDernierMaj = idDernierMaj;
    }

    /**
     * Indique si la ligne est modifiée ou non. 
     * @return true / false
     */
    public Boolean getEstModifie() {
        if (listeVisa == null) { 
            return false;
        }
        for (final VisaDTO visa : listeVisa) {
            if (visa.getEstModifie()) {
                return true;
            }
        }
        return false;
    }
}
