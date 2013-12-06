/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AdminForm.java,v 1.8 2010/05/10 11:32:25 jerome.carriere Exp $
 */

package org.crlr.web.application.form.inspection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.UserDTOForList;
import org.crlr.dto.application.inspection.DroitInspecteurDTO;
import org.crlr.web.application.form.AbstractForm;

/**
 * AdminForm.
 *
 * @author $author$
 * @version $Revision: 1.8 $
  */
public class InspectionForm extends AbstractForm {
    
    private static final long serialVersionUID = -673216117072840792L;
    private Profil profilNavigation;
    private List<UserDTOForList> inspecteurs;
    private List<EnseignantDTO> enseignants;
    private List<DroitInspecteurDTO> droitsInspecteur;
    private List<DroitInspecteurDTO> droitsInspecteurInit;
    private DroitInspecteurDTO droitSelectionne;
    
    private String filterInspecteur;
    private String filterEnseignant;
    
    /**
     * Constructeur par defaut.
     */
    public InspectionForm(){
        super();
        inspecteurs = new ArrayList<UserDTOForList>();
        enseignants = new ArrayList<EnseignantDTO>();
        droitsInspecteur = new ArrayList<DroitInspecteurDTO>();
        droitsInspecteurInit = new ArrayList<DroitInspecteurDTO>();
    }


    /**
     * Accesseur de profilNavigation.
     * @return le profilNavigation
     */
    public Profil getProfilNavigation() {
        return profilNavigation;
    }


    /**
     * Mutateur de profilNavigation.
     * @param profilNavigation le profilNavigation à modifier.
     */
    public void setProfilNavigation(Profil profilNavigation) {
        this.profilNavigation = profilNavigation;
    }


    /**
     * Accesseur de inspecteurs.
     * @return le inspecteurs
     */
    public List<UserDTOForList> getInspecteurs() {
        return inspecteurs;
    }


    /**
     * Mutateur de inspecteurs.
     * @param inspecteurs le inspecteurs à modifier.
     */
    public void setInspecteurs(List<UserDTOForList> inspecteurs) {
        this.inspecteurs = inspecteurs;
    }


    /**
     * Accesseur de droitsInspecteur.
     * @return le droitsInspecteur
     */
    public List<DroitInspecteurDTO> getDroitsInspecteur() {
        return droitsInspecteur;
    }


    /**
     * Mutateur de droitsInspecteur.
     * @param droitsInspecteur le droitsInspecteur à modifier.
     */
    public void setDroitsInspecteur(List<DroitInspecteurDTO> droitsInspecteur) {
        this.droitsInspecteur = droitsInspecteur;
    }


    /**
     * Accesseur de droitSelectionne.
     * @return le droitSelectionne
     */
    public DroitInspecteurDTO getDroitSelectionne() {
        return droitSelectionne;
    }


    /**
     * Mutateur de droitSelectionne.
     * @param droitSelectionne le droitSelectionne à modifier.
     */
    public void setDroitSelectionne(DroitInspecteurDTO droitSelectionne) {
        this.droitSelectionne = droitSelectionne;
    }


    /**
     * Accesseur de enseignants.
     * @return le enseignants
     */
    public List<EnseignantDTO> getEnseignants() {
        return enseignants;
    }


    /**
     * Mutateur de enseignants.
     * @param enseignants le enseignants à modifier.
     */
    public void setEnseignants(List<EnseignantDTO> enseignants) {
        this.enseignants = enseignants;
    }


    
    
    /**
     * Accesseur de filterInspecteur.
     * @return le filterInspecteur
     */
    public String getFilterInspecteur() {
        return filterInspecteur;
    }


    /**
     * Mutateur de filterInspecteur.
     * @param filterInspecteur le filterInspecteur à modifier.
     */
    public void setFilterInspecteur(String filterInspecteur) {
        this.filterInspecteur = filterInspecteur;
    }


    /**
     * Accesseur de filterEnseignant.
     * @return le filterEnseignant
     */
    public String getFilterEnseignant() {
        return filterEnseignant;
    }


    /**
     * Mutateur de filterEnseignant.
     * @param filterEnseignant le filterEnseignant à modifier.
     */
    public void setFilterEnseignant(String filterEnseignant) {
        this.filterEnseignant = filterEnseignant;
    }


    /**
     * Filtre pour les enseignants.
     * @param value .
     * @return .
     */
    public Boolean doFilterEnseignant(Object value ){
        if (StringUtils.isEmpty(getFilterEnseignant())) {
            return (true);
        }
        final UserDTOForList enseignant = (UserDTOForList) value;

        if (enseignant.getNom().toLowerCase().contains((getFilterEnseignant().toLowerCase()))) {
            return (true);
        }
        return (false);
    }
    
    /**
     * Filtre pour les inspecteurs.
     * @param value .
     * @return .
     */
    public Boolean doFilterInspecteur(Object value ){
        if (StringUtils.isEmpty(getFilterEnseignant())) {
            return (true);
        }
        final UserDTOForList inspecteur = (UserDTOForList) value;

        if (inspecteur.getNom().toLowerCase().contains((getFilterInspecteur().toLowerCase()))) {
            return (true);
        }
        return (false);
    }


    /**
     * Accesseur de droitsInspecteurInit {@link #droitsInspecteurInit}.
     * @return retourne droitsInspecteurInit
     */
    public List<DroitInspecteurDTO> getDroitsInspecteurInit() {
        return droitsInspecteurInit;
    }


    /**
     * Mutateur de droitsInspecteurInit {@link #droitsInspecteurInit}.
     * @param droitsInspecteurInit le droitsInspecteurInit to set
     */
    public void setDroitsInspecteurInit(
            List<DroitInspecteurDTO> droitsInspecteurInit) {
        this.droitsInspecteurInit = droitsInspecteurInit;
    }


   
    


        
    
    
    
    
}
