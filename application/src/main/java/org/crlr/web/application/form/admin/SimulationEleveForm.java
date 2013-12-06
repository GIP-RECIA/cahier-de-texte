/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SimulationEleveForm.java,v 1.8 2010/05/10 11:32:25 jerome.carriere Exp $
 */

package org.crlr.web.application.form.admin;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.UserDTO;
import org.crlr.web.application.form.AbstractForm;

/**
 * AdminForm.
 *
 * @author $author$
 * @version $Revision: 1.8 $
  */
public class SimulationEleveForm extends AbstractForm {

    /**  Serial. */
    private static final long serialVersionUID = -7057556698078479174L;
    
    /** Eleve qui est sélectionné. */
    private UserDTO eleveSelected;
    
    /** Liste des eleves correspondant a la classe / groupe selectionnee. */
    private List<UserDTO> listeEleve;
    
    /** Filtre applique sur le nom de l'eleve dans la popup de choix eleve. */
    private String filtreEleveNom; 
    
    /** Filtre applique sur le prénom de l'eleve dans la popup de choix eleve. */
    private String filtreElevePrenom;

    /**
     * Constructeur.
     */
    public SimulationEleveForm() {
        super();
        filtreEleveNom = "";
        filtreElevePrenom = "";
        eleveSelected = null;
        listeEleve = new ArrayList<UserDTO>();
    }

    /**
     * Accesseur de eleveSelected {@link #eleveSelected}.
     * @return retourne eleveSelected
     */
    public UserDTO getEleveSelected() {
        return eleveSelected;
    }

    /**
     * Mutateur de eleveSelected {@link #eleveSelected}.
     * @param eleveSelected le eleveSelected to set
     */
    public void setEleveSelected(UserDTO eleveSelected) {
        this.eleveSelected = eleveSelected;
    }

    /**
     * Accesseur de listeEleve {@link #listeEleve}.
     * @return retourne listeEleve
     */
    public List<UserDTO> getListeEleve() {
        return listeEleve;
    }

    /**
     * Mutateur de listeEleve {@link #listeEleve}.
     * @param listeEleve le listeEleve to set
     */
    public void setListeEleve(List<UserDTO> listeEleve) {
        this.listeEleve = listeEleve;
    }

    /**
     * Accesseur de filtreEleveNom {@link #filtreEleveNom}.
     * @return retourne filtreEleveNom
     */
    public String getFiltreEleveNom() {
        return filtreEleveNom;
    }

    /**
     * Mutateur de filtreEleveNom {@link #filtreEleveNom}.
     * @param filtreEleveNom le filtreEleveNom to set
     */
    public void setFiltreEleveNom(String filtreEleveNom) {
        this.filtreEleveNom = filtreEleveNom;
    }

    /**
     * Accesseur de filtreElevePrenom {@link #filtreElevePrenom}.
     * @return retourne filtreElevePrenom
     */
    public String getFiltreElevePrenom() {
        return filtreElevePrenom;
    }

    /**
     * Mutateur de filtreElevePrenom {@link #filtreElevePrenom}.
     * @param filtreElevePrenom le filtreElevePrenom to set
     */
    public void setFiltreElevePrenom(String filtreElevePrenom) {
        this.filtreElevePrenom = filtreElevePrenom;
    } 
    
    
}
