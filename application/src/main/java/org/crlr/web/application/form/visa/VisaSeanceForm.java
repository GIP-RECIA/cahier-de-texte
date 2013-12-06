/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheSeanceForm.java,v 1.5 2010/05/19 09:10:20 jerome.carriere Exp $
 */

package org.crlr.web.application.form.visa;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.seance.ResultatRechercheSeanceDTO;
import org.crlr.dto.application.visa.DateListeVisaSeanceDTO;
import org.crlr.dto.application.visa.ResultatRechercheVisaSeanceDTO;
import org.crlr.dto.application.visa.VisaDTO;
import org.crlr.dto.application.visa.VisaEnseignantDTO;
import org.crlr.dto.application.visa.VisaDTO.VisaProfil;
import org.crlr.web.application.form.AbstractPopupForm;

/**
 * Formulaire .
 *
 * @author breytond
 * @version $Revision: 1.5 $
 */
public class VisaSeanceForm extends AbstractPopupForm {
    /** Serial. */
    private static final long serialVersionUID = -301376493558631608L;

    /** Affichage du bouton retour. */
    private Boolean afficheRetour;

    /** Affichage du bouton de sauvegarde. */
    private Boolean afficheSauvegarde;
    
    private Boolean afficheVisualiserArchiveSeance;
    
    private Boolean afficheSequenceTitre;
    
    /** liste des séances recherchées. */
    private List<DateListeVisaSeanceDTO> listeSeance;

    /** Ligne de visa seance selectionne. */
    private ResultatRechercheVisaSeanceDTO visaSeanceSelected;
    
    /** la ligne de resultat séléctionnée dans la liste des séances. */
    private ResultatRechercheSeanceDTO resultatSelectionne;

    private String texteAide;
    
    /** Visa provenant de l'ecran visaListe. */
    private VisaDTO visaOrigine;

    /** Visa enseignant provenant de l'ecran visaListe. */
    private VisaEnseignantDTO visaEnseignantOrigine;
    
    /**
     * Indique si la resultat de recherche est visible.
     */
    private Boolean vraiOuFauxRechercheActive;
    
    /** Check box "non visées". */
    private Boolean vraiOuFauxNonVisee;
    
    /** Check box "visées". */
    private Boolean vraiOuFauxVisee;
    
    /** Check box "perimees". */
    private Boolean vraiOuFauxPerimee;
    
    
    /**
     * 
     */
    public static enum Affichage {
        LISTE,
        DETAIL
    }
    /** Radio bouton du choix d'affichage liste ou detail. */
    private Affichage modeAffichage;
    
     
    /** Profil de l'utilisateur connecte. */
    private VisaProfil profilVisaUser;
    
    /**
     * Constructeur.
     */
    public VisaSeanceForm() {
        reset();
    }

    /**
     * Méthode de réinitialisation des données du formulaire saisie par
     * l'utilisateur.
     */
    public void reset() {
        listeSeance = new ArrayList<DateListeVisaSeanceDTO>();
        resultatSelectionne = new ResultatRechercheSeanceDTO();
        
        //visaOrigine reste la même, ce n'est pas une critère.
        //visaOrigine = null;
        vraiOuFauxNonVisee = true;
        vraiOuFauxVisee = false;
        vraiOuFauxPerimee = true;
        modeAffichage = Affichage.LISTE;
        afficheSauvegarde = false;
        
        vraiOuFauxRechercheActive = false;
    }

    

   

    /**
     * Accesseur resultatSelectionne.
     *
     * @return resultatSelectionne
     */
    public ResultatRechercheSeanceDTO getResultatSelectionne() {
        return resultatSelectionne;
    }

    /**
     * Mutateur resultatSelectionne.
     *
     * @param resultatSelectionne resultatSelectionne à modifier
     */
    public void setResultatSelectionne(ResultatRechercheSeanceDTO resultatSelectionne) {
        this.resultatSelectionne = resultatSelectionne;
    }

   
   

   
    
    /**
     * Accesseur de visaOrigine {@link #visaOrigine}.
     * @return retourne visaOrigine
     */
    public VisaDTO getVisaOrigine() {
        return visaOrigine;
    }

    /**
     * Mutateur de visaOrigine {@link #visaOrigine}.
     * @param visaOrigine le visaOrigine to set
     */
    public void setVisaOrigine(VisaDTO visaOrigine) {
        this.visaOrigine = visaOrigine;
    }

    


    /**
     * Accesseur de vraiOuFauxNonVisee {@link #vraiOuFauxNonVisee}.
     * @return retourne vraiOuFauxNonVisee
     */
    public Boolean getVraiOuFauxNonVisee() {
        return vraiOuFauxNonVisee;
    }

    /**
     * Mutateur de vraiOuFauxNonVisee {@link #vraiOuFauxNonVisee}.
     * @param vraiOuFauxNonVisee le vraiOuFauxNonVisee to set
     */
    public void setVraiOuFauxNonVisee(Boolean vraiOuFauxNonVisee) {
        this.vraiOuFauxNonVisee = vraiOuFauxNonVisee;
    }

    /**
     * Accesseur de vraiOuFauxVisee {@link #vraiOuFauxVisee}.
     * @return retourne vraiOuFauxVisee
     */
    public Boolean getVraiOuFauxVisee() {
        return vraiOuFauxVisee;
    }

    /**
     * Mutateur de vraiOuFauxVisee {@link #vraiOuFauxVisee}.
     * @param vraiOuFauxVisee le vraiOuFauxVisee to set
     */
    public void setVraiOuFauxVisee(Boolean vraiOuFauxVisee) {
        this.vraiOuFauxVisee = vraiOuFauxVisee;
    }

    /**
     * Accesseur de vraiOuFauxPerimee {@link #vraiOuFauxPerimee}.
     * @return retourne vraiOuFauxPerimee
     */
    public Boolean getVraiOuFauxPerimee() {
        return vraiOuFauxPerimee;
    }

    /**
     * Mutateur de vraiOuFauxPerimee {@link #vraiOuFauxPerimee}.
     * @param vraiOuFauxPerimee le vraiOuFauxPerimee to set
     */
    public void setVraiOuFauxPerimee(Boolean vraiOuFauxPerimee) {
        this.vraiOuFauxPerimee = vraiOuFauxPerimee;
    }

    /**
     * Accesseur de modeAffichage {@link #modeAffichage}.
     * @return retourne modeAffichage
     */
    public Affichage getModeAffichage() {
        return modeAffichage;
    }

    /**
     * Mutateur de modeAffichage {@link #modeAffichage}.
     * @param modeAffichage le modeAffichage to set
     */
    public void setModeAffichage(Affichage modeAffichage) {
        this.modeAffichage = modeAffichage;
    }

    


    /**
     * Accesseur de profilVisaUser {@link #profilVisaUser}.
     * @return retourne profilVisaUser
     */
    public VisaProfil getProfilVisaUser() {
        return profilVisaUser;
    }

    /**
     * Mutateur de profilVisaUser {@link #profilVisaUser}.
     * @param profilVisaUser le profilVisaUser to set
     */
    public void setProfilVisaUser(VisaProfil profilVisaUser) {
        this.profilVisaUser = profilVisaUser;
    }

    /**
     * Accesseur de visaSeanceSelected {@link #visaSeanceSelected}.
     * @return retourne visaSeanceSelected
     */
    public ResultatRechercheVisaSeanceDTO getVisaSeanceSelected() {
        return visaSeanceSelected;
    }

    /**
     * Mutateur de visaSeanceSelected {@link #visaSeanceSelected}.
     * @param visaSeanceSelected le visaSeanceSelected to set
     */
    public void setVisaSeanceSelected(
            ResultatRechercheVisaSeanceDTO visaSeanceSelected) {
        this.visaSeanceSelected = visaSeanceSelected;
    }

    /**
     * Accesseur de listeSeance {@link #listeSeance}.
     * @return retourne listeSeance
     */
    public List<DateListeVisaSeanceDTO> getListeSeance() {
        return listeSeance;
    }

    /**
     * Mutateur de listeSeance {@link #listeSeance}.
     * @param listeSeance le listeSeance to set
     */
    public void setListeSeance(List<DateListeVisaSeanceDTO> listeSeance) {
        this.listeSeance = listeSeance;
    }

    /**
     * Accesseur de afficheRetour {@link #afficheRetour}.
     * @return retourne afficheRetour
     */
    public Boolean getAfficheRetour() {
        return afficheRetour;
    }

    /**
     * Mutateur de afficheRetour {@link #afficheRetour}.
     * @param afficheRetour le afficheRetour to set
     */
    public void setAfficheRetour(Boolean afficheRetour) {
        this.afficheRetour = afficheRetour;
    }

    

    /**
     * Accesseur de afficheSauvegarde {@link #afficheSauvegarde}.
     * @return retourne afficheSauvegarde
     */
    public Boolean getAfficheSauvegarde() {
        return afficheSauvegarde;
    }

    /**
     * Mutateur de afficheSauvegarde {@link #afficheSauvegarde}.
     * @param afficheSauvegarde le afficheSauvegarde to set
     */
    public void setAfficheSauvegarde(Boolean afficheSauvegarde) {
        this.afficheSauvegarde = afficheSauvegarde;
    }

    /**
     * @return the afficheSequenceTitre
     */
    public Boolean getAfficheSequenceTitre() {
        return afficheSequenceTitre;
    }

    /**
     * @param afficheSequenceTitre the afficheSequenceTitre to set
     */
    public void setAfficheSequenceTitre(Boolean afficheSequenceTitre) {
        this.afficheSequenceTitre = afficheSequenceTitre;
    }

    /**
     * @return the afficheVisualiserArchiveSeance
     */
    public Boolean getAfficheVisualiserArchiveSeance() {
        return afficheVisualiserArchiveSeance;
    }

    /**
     * @param afficheVisualiserArchiveSeance the afficheVisualiserArchiveSeance to set
     */
    public void setAfficheVisualiserArchiveSeance(
            Boolean afficheVisualiserArchiveSeance) {
        this.afficheVisualiserArchiveSeance = afficheVisualiserArchiveSeance;
    }

    /**
     * @return the texteAide
     */
    public String getTexteAide() {
        return texteAide;
    }

    /**
     * @param texteAide the texteAide to set
     */
    public void setTexteAide(String texteAide) {
        this.texteAide = texteAide;
    }

    /**
     * Accesseur de vraiOuFauxRechercheActive {@link #vraiOuFauxRechercheActive}.
     * @return retourne vraiOuFauxRechercheActive
     */
    public Boolean getVraiOuFauxRechercheActive() {
        return vraiOuFauxRechercheActive;
    }

    /**
     * Mutateur de vraiOuFauxRechercheActive {@link #vraiOuFauxRechercheActive}.
     * @param vraiOuFauxRechercheActive le vraiOuFauxRechercheActive to set
     */
    public void setVraiOuFauxRechercheActive(Boolean vraiOuFauxRechercheActive) {
        this.vraiOuFauxRechercheActive = vraiOuFauxRechercheActive;
    }

    /**
     * Accesseur de visaEnseignantOrigine {@link #visaEnseignantOrigine}.
     * @return retourne visaEnseignantOrigine
     */
    public VisaEnseignantDTO getVisaEnseignantOrigine() {
        return visaEnseignantOrigine;
    }

    /**
     * Mutateur de visaEnseignantOrigine {@link #visaEnseignantOrigine}.
     * @param visaEnseignantOrigine le visaEnseignantOrigine to set
     */
    public void setVisaEnseignantOrigine(VisaEnseignantDTO visaEnseignantOrigine) {
        this.visaEnseignantOrigine = visaEnseignantOrigine;
    }

 
   
}
