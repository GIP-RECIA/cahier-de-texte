/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: VisaListeForm.java,v 1.9 2009/07/10 15:32:06 vibertd Exp $
 */
package org.crlr.web.application.form.visa;

import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.visa.TriDTO;
import org.crlr.dto.application.visa.VisaDTO;
import org.crlr.dto.application.visa.VisaDTO.VisaProfil;
import org.crlr.dto.application.visa.VisaEnseignantDTO;
import org.crlr.web.application.form.AbstractPopupForm;

/**
 * @author $author$
 * @version $Revision: 1.9 $
 */
public class VisaListeForm extends AbstractPopupForm {

    /**  Serial. */
    private static final long serialVersionUID = 1138970425743858663L;

    /** Liste des id des enseignants. */
    private List<EnseignantDTO> listeEnseignant;
    
    /** Liste des enseignant avec leurs visas. */
    private List<VisaEnseignantDTO> listeVisaEnseignant;
    
    /** Liste originale (synchro avec la bdd) des enseignants avec leurs visas. */
    private List<VisaEnseignantDTO> listeVisaEnseignantBdd;

    /** Ligne enseignant qui a ete selectionnee. */
    private VisaEnseignantDTO visaEnseignantSelected;

    /** Ligne visa d'un enseignant selectionnee. */
    private VisaDTO visaSelected;

    /** Valeur du profil visa de l'utilisateur connecte. */
    private VisaProfil profilVisaUser;
    
    /** Indique la colonne sur laquelle la liste est triee. */
    private TriDTO ordreTri = new TriDTO();

    /** Texte d'aide affiché dans la popup d'aide. */
    private String texteAide;
    
    /**
     * Accesseur de listeVisaEnseignant {@link #listeVisaEnseignant}.
     * @return retourne listeVisaEnseignant
     */
    public List<VisaEnseignantDTO> getListeVisaEnseignant() {
        return listeVisaEnseignant;
    }


    /**
     * Mutateur de listeVisaEnseignant {@link #listeVisaEnseignant}.
     * @param listeVisaEnseignant le listeVisaEnseignant to set
     */
    public void setListeVisaEnseignant(List<VisaEnseignantDTO> listeVisaEnseignant) {
        this.listeVisaEnseignant = listeVisaEnseignant;
    }


    /**
     * Accesseur de visaEnseignantSelected {@link #visaEnseignantSelected}.
     * @return retourne visaEnseignantSelected
     */
    public VisaEnseignantDTO getVisaEnseignantSelected() {
        return visaEnseignantSelected;
    }


    /**
     * Mutateur de visaEnseignantSelected {@link #visaEnseignantSelected}.
     * @param visaEnseignantSelected le visaEnseignantSelected to set
     */
    public void setVisaEnseignantSelected(VisaEnseignantDTO visaEnseignantSelected) {
        this.visaEnseignantSelected = visaEnseignantSelected;
    }

    /**
     * Accesseur de visaSelected {@link #visaSelected}.
     * @return retourne visaSelected
     */
    public VisaDTO getVisaSelected() {
        return visaSelected;
    }

    /**
     * Mutateur de visaSelected {@link #visaSelected}.
     * @param visaSelected le visaSelected to set
     */
    public void setVisaSelected(VisaDTO visaSelected) {
        this.visaSelected = visaSelected;
    }


    /**
     * Accesseur de listeVisaEnseignantBdd {@link #listeVisaEnseignantBdd}.
     * @return retourne listeVisaEnseignantBdd
     */
    public List<VisaEnseignantDTO> getListeVisaEnseignantBdd() {
        return listeVisaEnseignantBdd;
    }


    /**
     * Mutateur de listeVisaEnseignantBdd {@link #listeVisaEnseignantBdd}.
     * @param listeVisaEnseignantBdd le listeVisaEnseignantBdd to set
     */
    public void setListeVisaEnseignantBdd(
            List<VisaEnseignantDTO> listeVisaEnseignantBdd) {
        this.listeVisaEnseignantBdd = listeVisaEnseignantBdd;
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
     * Accesseur de listeEnseignant {@link #listeEnseignant}.
     * @return retourne listeEnseignant
     */
    public List<EnseignantDTO> getListeEnseignant() {
        return listeEnseignant;
    }


    /**
     * Mutateur de listeEnseignant {@link #listeEnseignant}.
     * @param listeEnseignant le listeEnseignant to set
     */
    public void setListeEnseignant(List<EnseignantDTO> listeEnseignant) {
        this.listeEnseignant = listeEnseignant;
    }

    /**
     * Retourne le titre de la popup agenda. 
     * @return le titre.
     */
    public String getTitreAgenda() {
       if (visaEnseignantSelected == null) {
           return ""; 
       } else {
           return 
               visaEnseignantSelected.getEnseignant().getNom() + " " + 
               visaEnseignantSelected.getEnseignant().getPrenom();
       }
    }


    /**
     * Accesseur de ordreTri {@link #ordreTri}.
     * @return retourne ordreTri
     */
    public TriDTO getOrdreTri() {
        return ordreTri;
    }


    /**
     * Mutateur de ordreTri {@link #ordreTri}.
     * @param ordreTri le ordreTri to set
     */
    public void setOrdreTri(TriDTO ordreTri) {
        this.ordreTri = ordreTri;
    }

    /**
     * Modifie l'ordre de tri.
     * @param colonne le nom de la colonne.
     */
    public void setColonneTri(String colonne) {
        if (colonne.equals(ordreTri.getColonne())) {
            ordreTri.setAscendant(!ordreTri.getAscendant());
        } else {
            ordreTri.setColonne(colonne);
            ordreTri.setAscendant(true);
        }
    }


    /**
     * Accesseur de texteAide {@link #texteAide}.
     * @return retourne texteAide
     */
    public String getTexteAide() {
        return texteAide;
    }


    /**
     * Mutateur de texteAide {@link #texteAide}.
     * @param texteAide le texteAide to set
     */
    public void setTexteAide(String texteAide) {
        this.texteAide = texteAide;
    }
    
}
