/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ResultatRechercheVisaSeanceDTO.java,v 1.10 2009/04/29 08:52:22 vibertd Exp $
 */

package org.crlr.dto.application.visa;

import java.io.Serializable;
import java.util.Date;

import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.SeanceDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.10 $
 */
public class ResultatRechercheVisaSeanceDTO extends SeanceDTO implements Serializable {
    /** Serial. */
    private static final long serialVersionUID = -6761783692529724228L;

    
    /** Indique que la ligne seance / visa a ete modifiee. */
    private Boolean vraiOuFauxModifiee;
                    
     
    /** Id du visa directeur correspondant. */
    private VisaDTO visaDirecteur;
    
    /** Id du visa inspecteur correspondant. */
    private VisaDTO visaInspecteur;
    
   
    /** Profil de l'utilisateur consultant cette seance / visa. */
    private Profil profil;
    
    /** Indique que la ligne visaSeance existe pour le directeur. */
    private Boolean vraiOuFauxVisaSeanceExisteDirecteur; 
    
    /** Indique que la ligne visaSeance existe pour le inspecteur. */
    private Boolean vraiOuFauxVisaSeanceExisteInspecteur; 
    
    /**
     * Constructeur par defaut.
     */
    public ResultatRechercheVisaSeanceDTO() {
        super();
        vraiOuFauxModifiee = false;
        visaDirecteur = new VisaDTO();
        visaInspecteur = new VisaDTO();
        
    }

    /**
     * Accesseur designationEnseignement.
     *
     * @return designationEnseignement
     * @deprecated
     */
    public String getDesignationEnseignement() {
        return getSequenceDTO().getDesignationEnseignement();
    }

    /**
     * Mutateur designationEnseignement.
     *
     * @param designationEnseignement designationEnseignement à modifier
     * @deprecated
     */
    public void setDesignationEnseignement(String designationEnseignement) {
        this.getSequenceDTO().setDesignationEnseignement(designationEnseignement);
    }

    /**
     * Accesseur de dateVisaDirecteur {@link #dateVisaDirecteur}.
     * @return retourne dateVisaDirecteur
     * @deprecated
     */
    public Date getDateVisaDirecteur() {
        return getVisaDirecteur().getDateVisa();
    }

    /**
     * Mutateur de dateVisaDirecteur {@link #dateVisaDirecteur}.
     * @param dateVisaDirecteur le dateVisaDirecteur to set
     * @deprecated
     */    
    public void setDateVisaDirecteur(Date dateVisaDirecteur) {
        this.getVisaDirecteur().setDateVisa(dateVisaDirecteur);
    }

    /**
     * Accesseur de dateVisaInspecteur {@link #dateVisaInspecteur}.
     * @return retourne dateVisaInspecteur
     * @deprecated
     */
    public Date getDateVisaInspecteur() {
        return getVisaInspecteur().getDateVisa();
    }

    /**
     * Mutateur de dateVisaInspecteur {@link #dateVisaInspecteur}.
     * @param dateVisaInspecteur le dateVisaInspecteur to set
     * @deprecated
     */
    public void setDateVisaInspecteur(Date dateVisaInspecteur) {
        this.getVisaInspecteur().setDateVisa(dateVisaInspecteur);
    }

    /**
     * Verifie la validite de la date de maj / visa.
     * @return true si la date de maj est en alerte. 
     */
    public Boolean getAlerteDateMaj() {
        final Date dateVisa = getVisaDeUtilisateur().getDateVisa();
        return (getDateMaj()!=null && dateVisa!=null && getDateMaj().getTime() > dateVisa.getTime());
    }
    
    public Boolean getIsVisibleCadena() {
        VisaDTO visa = getVisaDeUtilisateur();
        if (visa.getTypeVisa() == TypeVisa.MEMO && getAlerteDateMaj()) {
            return true;
        }
        return false;
    }
    
    
    public VisaDTO getVisaDeUtilisateur() {
        if (Profil.DIRECTION_ETABLISSEMENT == profil) {
            return getVisaDirecteur();
        } else {
            return getVisaInspecteur();
        }
    }
    
    /**
     * Le visa existe (crée avec une date) et
     * la séance était créee avant le visa (entrée dans table visa_seance).
     * @return t/f
     */
    public Boolean getEstVisee() {
        
        /*Il faut deux chose, une date de visa (le visa existe)
         * et l'entrée existe dans visaSeance.  
         */
        if (getVisaDeUtilisateur().getDateVisa() == null) {
            return false;
        }
        
        if (Profil.DIRECTION_ETABLISSEMENT == profil) {
            return vraiOuFauxVisaSeanceExisteDirecteur;
        } else if (Profil.INSPECTION_ACADEMIQUE == profil) {
            return vraiOuFauxVisaSeanceExisteInspecteur;
        }
        
        return null;
    }
    
    /**
     * Verifie la validite de la date du visa directeur.
     * @return true si la date de visa directeur est en alerte. 
     */
    public Boolean getAlerteDateVisaDirecteur() {
        if (Profil.DIRECTION_ETABLISSEMENT != profil) {
            return false;
        } else {
            return (getDateMaj()!=null && getVisaDirecteur().getDateVisa()==null);
        }
    }

    /**
     * Verifie la validite de la date du visa inspecteur.
     * @return true si la date de visa directeur est en alerte. 
     */
    public Boolean getAlerteDateVisaInspecteur() {
        if (Profil.INSPECTION_ACADEMIQUE != profil) {
            return false;
        } else {
            return (getDateMaj()!=null && getVisaInspecteur().getDateVisa()==null);
        }
        
    }

    /**
     * Accesseur de typeVisaDirecteur {@link #typeVisaDirecteur}.
     * @return retourne typeVisaDirecteur
     * @deprecated
     */
    public TypeVisa getTypeVisaDirecteur() {
        return getVisaDirecteur().getTypeVisa();
    }

    /**
     * Mutateur de typeVisaDirecteur {@link #typeVisaDirecteur}.
     * @param typeVisaDirecteur le typeVisaDirecteur to set
     * @deprecated
     */
    public void setTypeVisaDirecteur(TypeVisa typeVisaDirecteur) {
        this.getVisaDirecteur().setTypeVisa(typeVisaDirecteur);
    }

    /**
     * Accesseur de typeVisaInspecteur {@link #typeVisaInspecteur}.
     * @return retourne typeVisaInspecteur
     * @deprecated
     */
    public TypeVisa getTypeVisaInspecteur() {
        return getVisaInspecteur().getTypeVisa();
    }

    /**
     * Mutateur de typeVisaInspecteur {@link #typeVisaInspecteur}.
     * @param typeVisaInspecteur le typeVisaInspecteur to set
     * @deprecated
     */
    public void setTypeVisaInspecteur(TypeVisa typeVisaInspecteur) {
        this.getVisaInspecteur().setTypeVisa(typeVisaInspecteur);
    }

    /**
     * Accesseur de vraiOuFauxModifiee {@link #vraiOuFauxModifiee}.
     * @return retourne vraiOuFauxModifiee
     */
    public Boolean getVraiOuFauxModifiee() {
        return vraiOuFauxModifiee;
    }

    /**
     * Mutateur de vraiOuFauxModifiee {@link #vraiOuFauxModifiee}.
     * @param vraiOuFauxModifiee le vraiOuFauxModifiee to set
     */
    public void setVraiOuFauxModifiee(Boolean vraiOuFauxModifiee) {
        this.vraiOuFauxModifiee = vraiOuFauxModifiee;
    }

    /**
     * Accesseur de idVisaDirecteur {@link #idVisaDirecteur}.
     * @return retourne idVisaDirecteur
     * @deprecated
     */
    public Integer getIdVisaDirecteur() {
        return getVisaDirecteur().getIdVisa();
    }

    /**
     * Mutateur de idVisaDirecteur {@link #idVisaDirecteur}.
     * @param idVisaDirecteur le idVisaDirecteur to set
     * @deprecated
     */
    public void setIdVisaDirecteur(Integer idVisaDirecteur) {
        this.getVisaDirecteur().setId(idVisaDirecteur);
    }

    /**
     * Accesseur de idVisaInspecteur {@link #idVisaInspecteur}.
     * @return retourne idVisaInspecteur
     * @deprecated
     */
    public Integer getIdVisaInspecteur() {
        return getVisaInspecteur().getIdVisa();
    }

    /**
     * Mutateur de idVisaInspecteur {@link #idVisaInspecteur}.
     * @param idVisaInspecteur le idVisaInspecteur to set
     * @deprecated
     */
    public void setIdVisaInspecteur(Integer idVisaInspecteur) {
        this.getVisaInspecteur().setId(idVisaInspecteur);
    }

    /**
     * Accesseur de profil {@link #profil}.
     * @return retourne profil
     */
    public Profil getProfil() {
        return profil;
    }

    /**
     * Mutateur de profil {@link #profil}.
     * @param profil le profil to set
     */
    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    /**
     * Accesseur de vraiOuFauxVisaSeanceExisteDirecteur {@link #vraiOuFauxVisaSeanceExisteDirecteur}.
     * @return retourne vraiOuFauxVisaSeanceExisteDirecteur
     */
    public Boolean getVraiOuFauxVisaSeanceExisteDirecteur() {
        return vraiOuFauxVisaSeanceExisteDirecteur;
    }

    /**
     * Mutateur de vraiOuFauxVisaSeanceExisteDirecteur {@link #vraiOuFauxVisaSeanceExisteDirecteur}.
     * @param vraiOuFauxVisaSeanceExisteDirecteur le vraiOuFauxVisaSeanceExisteDirecteur to set
     */
    public void setVraiOuFauxVisaSeanceExisteDirecteur(
            Boolean vraiOuFauxVisaSeanceExisteDirecteur) {
        this.vraiOuFauxVisaSeanceExisteDirecteur = vraiOuFauxVisaSeanceExisteDirecteur;
    }

    /**
     * Accesseur de vraiOuFauxVisaSeanceExisteInspecteur {@link #vraiOuFauxVisaSeanceExisteInspecteur}.
     * @return retourne vraiOuFauxVisaSeanceExisteInspecteur
     */
    public Boolean getVraiOuFauxVisaSeanceExisteInspecteur() {
        return vraiOuFauxVisaSeanceExisteInspecteur;
    }

    /**
     * Mutateur de vraiOuFauxVisaSeanceExisteInspecteur {@link #vraiOuFauxVisaSeanceExisteInspecteur}.
     * @param vraiOuFauxVisaSeanceExisteInspecteur le vraiOuFauxVisaSeanceExisteInspecteur to set
     */
    public void setVraiOuFauxVisaSeanceExisteInspecteur(
            Boolean vraiOuFauxVisaSeanceExisteInspecteur) {
        this.vraiOuFauxVisaSeanceExisteInspecteur = vraiOuFauxVisaSeanceExisteInspecteur;
    }

   

    /**
     * @return the visaDirecteur
     */
    public VisaDTO getVisaDirecteur() {
        return visaDirecteur;
    }

    /**
     * @param visaDirecteur the visaDirecteur to set
     */
    public void setVisaDirecteur(VisaDTO visaDirecteur) {
        this.visaDirecteur = visaDirecteur;
    }

    /**
     * @return the visaInspecteur
     */
    public VisaDTO getVisaInspecteur() {
        return visaInspecteur;
    }

    /**
     * @param visaInspecteur the visaInspecteur to set
     */
    public void setVisaInspecteur(VisaDTO visaInspecteur) {
        this.visaInspecteur = visaInspecteur;
    }

   
    
    
}
