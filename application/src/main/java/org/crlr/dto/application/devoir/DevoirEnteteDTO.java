/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirEnteteDTO.java,v 1.8 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.dto.application.devoir;

import java.io.Serializable;
import java.util.Date;

import org.crlr.utils.DateUtils;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.8 $
 */
public class DevoirEnteteDTO implements Serializable {
    

    /** Serial de la classe.      */
    private static final long serialVersionUID = 8277307364246732144L;

    /** Libelle Enseignement. */
    private String libelleEnseignement;

    /** L'intitule du devoir. */
    private String intituleDevoir;

    /** Nom de l'enseignant ayant donne le devoir à faire. */
    private String nomEnseignant;
    
    /** Indique si la seance ne s'est pas encore passée ET que la seance n'est pas la même que l'enseignant courant. */
    private Boolean seanceFuture;
    
    /** Date de la seance du devoir. */
    private Date dateSeance;
    
    /** Designation de la classe ou du groupe. */
    private String designationClasseGroupe;
    
    /** Indique s'il s'agit d'un devoir ou d'une travail normal. */
    private Boolean vraiOuFauxDevoir;
    
    /**
     * Constructeur.
     */
    public DevoirEnteteDTO() {
        libelleEnseignement = "";
        intituleDevoir = "";
    }

    /**
     * Accesseur de libelleEnseignement.
     * @return the libelleEnseignement
     */
    public String getLibelleEnseignement() {
        return libelleEnseignement;
    }

    /**
     * Mutator de libelleEnseignement.
     * @param libelleEnseignement the libelleEnseignement to set
     */
    public void setLibelleEnseignement(String libelleEnseignement) {
        this.libelleEnseignement = libelleEnseignement;
    }

    /**
     * Accesseur de intituleDevoir.
     * @return the intituleDevoir
     */
    public String getIntituleDevoir() {
        return intituleDevoir;
    }

    /**
     * Mutator de intituleDevoir.
     * @param intituleDevoir the intituleDevoir to set
     */
    public void setIntituleDevoir(String intituleDevoir) {
        this.intituleDevoir = intituleDevoir;
    }

    /**
     * Accesseur nomEnseignant.
     * @return the nomEnseignant
     */
    public String getNomEnseignant() {
        return nomEnseignant;
    }

    /**
     * Mutateur nomEnseignant.
     * @param nomEnseignant the nomEnseignant to set
     */
    public void setNomEnseignant(String nomEnseignant) {
        this.nomEnseignant = nomEnseignant;
    }

    /**
     * Indique TRUE si le devoir concerne une seance pas encore passee pour un autre enseignant que celui connecte,
     * Indique FALSE dans les autres cas.
     * @return the seanceFuture
     */
    public Boolean getSeanceFuture() {
        return seanceFuture;
    }

    /**
     * Mutateur seanceFuture.
     * @param seanceFuture the seanceFuture to set
     */
    public void setSeanceFuture(Boolean seanceFuture) {
        this.seanceFuture = seanceFuture;
    }

    /**
     * @return the dateSeance
     */
    public Date getDateSeance() {
        return dateSeance;
    }

    /**
     * @param dateSeance the dateSeance to set
     */
    public void setDateSeance(Date dateSeance) {
        this.dateSeance = dateSeance;
    }
    
    /**
     * Retourne au format date (sans l'heure) la date de la seance.
     * @return JJ/MM/AAAA
     */
    public String getLibelleDateSeance() {
        return DateUtils.format(dateSeance);
    }

    /**
     * Accesseur de designationClasseGroupe.
     * @return the designationClasseGroupe 
     */
    public String getDesignationClasseGroupe() {
        return designationClasseGroupe;
    }

    /**
     * Mutateur de designationClasseGroupe.
     * @param designationClasseGroupe the designationClasseGroupe to set
     */
    public void setDesignationClasseGroupe(String designationClasseGroupe) {
        this.designationClasseGroupe = designationClasseGroupe;
    }

    /**
     * Accesseur de vraiOuFauxDevoir {@link #vraiOuFauxDevoir}.
     * @return retourne vraiOuFauxDevoir
     */
    public Boolean getVraiOuFauxDevoir() {
        return vraiOuFauxDevoir;
    }

    /**
     * Mutateur de vraiOuFauxDevoir {@link #vraiOuFauxDevoir}.
     * @param vraiOuFauxDevoir le vraiOuFauxDevoir to set
     */
    public void setVraiOuFauxDevoir(Boolean vraiOuFauxDevoir) {
        this.vraiOuFauxDevoir = vraiOuFauxDevoir;
    }

    
}
