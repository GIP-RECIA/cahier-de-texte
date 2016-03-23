/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AccueilForm.java,v 1.8 2010/05/20 08:35:09 jerome.carriere Exp $
 */

package org.crlr.web.application.form.devoir;

import org.crlr.dto.UserDTO;
import org.crlr.dto.application.devoir.DetailJourDTO;
import org.crlr.web.application.form.AbstractForm;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Formulaire d'accueil.
 *
 * @author breytond
 */
public class ProchainDevoirForm extends AbstractForm {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** le Profil de l'utilisateur connecte. */
    private String profil;
    
    /** Nom prénom Enfant selectionné. */
    private String enfantSelectionne;

    /** liste des enfants d'un parent, pour un profil Parent. */
    private Set<UserDTO> listeEnfant;

    /** Id Enfant selectionné. */
    private Integer idEnfantSelectionne;

    /** liste des prochains devoirs. */
    private List<DetailJourDTO> listeDevoir;

    /** Détails du devoir. */
    private DetailJourDTO selectDevoir;


    /**
     * Constructeur.
     */
    public ProchainDevoirForm() {
        super();
        this.listeEnfant = new HashSet<UserDTO>();
        this.listeDevoir = new ArrayList<DetailJourDTO>();
        this.selectDevoir = new DetailJourDTO();
    }


    /**
     * Accesseur de profil {@link #profil}.
     * @return retourne profil 
     */
    public String getProfil() {
        return profil;
    }


    /**
     * Mutateur de profil {@link #profil}.
     * @param profil the profil to set
     */
    public void setProfil(String profil) {
        this.profil = profil;
    }


    /**
     * Accesseur de enfantSelectionne {@link #enfantSelectionne}.
     * @return retourne enfantSelectionne 
     */
    public String getEnfantSelectionne() {
        return enfantSelectionne;
    }


    /**
     * Mutateur de enfantSelectionne {@link #enfantSelectionne}.
     * @param enfantSelectionne the enfantSelectionne to set
     */
    public void setEnfantSelectionne(String enfantSelectionne) {
        this.enfantSelectionne = enfantSelectionne;
    }


    /**
     * Accesseur de listeEnfant {@link #listeEnfant}.
     * @return retourne listeEnfant 
     */
    public Set<UserDTO> getListeEnfant() {
        return listeEnfant;
    }


    /**
     * Mutateur de listeEnfant {@link #listeEnfant}.
     * @param listeEnfant the listeEnfant to set
     */
    public void setListeEnfant(Set<UserDTO> listeEnfant) {
        this.listeEnfant = listeEnfant;
    }


    /**
     * Accesseur de idEnfantSelectionne {@link #idEnfantSelectionne}.
     * @return retourne idEnfantSelectionne 
     */
    public Integer getIdEnfantSelectionne() {
        return idEnfantSelectionne;
    }


    /**
     * Mutateur de idEnfantSelectionne {@link #idEnfantSelectionne}.
     * @param idEnfantSelectionne the idEnfantSelectionne to set
     */
    public void setIdEnfantSelectionne(Integer idEnfantSelectionne) {
        this.idEnfantSelectionne = idEnfantSelectionne;
    }


    /**
     * Accesseur de listeDevoir {@link #listeDevoir}.
     * @return retourne listeDevoir 
     */
    public List<DetailJourDTO> getListeDevoir() {
        return listeDevoir;
    }


    /**
     * Mutateur de listeDevoir {@link #listeDevoir}.
     * @param listeDevoir the listeDevoir to set
     */
    public void setListeDevoir(List<DetailJourDTO> listeDevoir) {
        this.listeDevoir = listeDevoir;
    }


    /**
     * Accesseur de selectDevoir {@link #selectDevoir}.
     * @return retourne selectDevoir 
     */
    public DetailJourDTO getSelectDevoir() {
        return selectDevoir;
    }


    /**
     * Mutateur de selectDevoir {@link #selectDevoir}.
     * @param selectDevoir the selectDevoir to set
     */
    public void setSelectDevoir(DetailJourDTO selectDevoir) {
        this.selectDevoir = selectDevoir;
    }

    
}
