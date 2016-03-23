package org.crlr.web.application.form.remplacement;

import java.util.Date;
import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.remplacement.RemplacementDTO;
import org.crlr.web.application.form.AbstractPopupForm;

/**
 * Donnees saisies / affichees sur le formulaire de gestion des remplacements. 
 * @author DURUPT-C
 *
 */
public class GestionRemplacementForm extends AbstractPopupForm {

    /** Serial. */
    private static final long serialVersionUID = 7939404444568645729L;

    /** Liste des remplacements affiches. */
    private List<RemplacementDTO> listeRemplacement;
 
    /** Remplacement selectionne dans la liste. */
    private RemplacementDTO remplacementSelected;
    
    /** Liste des enseignants qui peuvent etre choisis comme remplacant (ou absents). */
    private List<EnseignantDTO> listeEnseignant;
    
    /** Indique si on choisit l'enseignant absent (true) ou le remplacant (false). */
    private Boolean vraiOuFauxChoixAbsent; 
    
    /** Filtre Nom utilise dans la popup des enseignants. */
    private String filtreEnseignantNom;
    
    /** Filtre Prenom utilise dans la popup des enseignants. */
    private String filtreEnseignantPrenom;
    
    /** La Date à utiliser dans la controle de getVraiOuFauxHabiliteSeanceEnCours. */
    private Date dateEffet;
    
    /**
     * Reset les donnees du formulaire.
     */
    public void reset() {
        remplacementSelected = null;
        vraiOuFauxChoixAbsent = false;
    }
    
    /**
     * Accesseur de listeRemplacement {@link #listeRemplacement}.
     * @return retourne listeRemplacement
     */
    public List<RemplacementDTO> getListeRemplacement() {
        return listeRemplacement;
    }

    /**
     * Mutateur de listeRemplacement {@link #listeRemplacement}.
     * @param listeRemplacement le listeRemplacement to set
     */
    public void setListeRemplacement(List<RemplacementDTO> listeRemplacement) {
        this.listeRemplacement = listeRemplacement;
    }

    /**
     * Accesseur de remplacementSelected {@link #remplacementSelected}.
     * @return retourne remplacementSelected
     */
    public RemplacementDTO getRemplacementSelected() {
        return remplacementSelected;
    }

    /**
     * Mutateur de remplacementSelected {@link #remplacementSelected}.
     * @param remplacementSelected le remplacementSelected to set
     */
    public void setRemplacementSelected(RemplacementDTO remplacementSelected) {
        this.remplacementSelected = remplacementSelected;
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
     * Accesseur de vraiOuFauxChoixAbsent {@link #vraiOuFauxChoixAbsent}.
     * @return retourne vraiOuFauxChoixAbsent
     */
    public Boolean getVraiOuFauxChoixAbsent() {
        return vraiOuFauxChoixAbsent;
    }

    /**
     * Mutateur de vraiOuFauxChoixAbsent {@link #vraiOuFauxChoixAbsent}.
     * @param vraiOuFauxChoixAbsent le vraiOuFauxChoixAbsent to set
     */
    public void setVraiOuFauxChoixAbsent(Boolean vraiOuFauxChoixAbsent) {
        this.vraiOuFauxChoixAbsent = vraiOuFauxChoixAbsent;
    }

    /**
     * Accesseur de filtreEnseignantNom {@link #filtreEnseignantNom}.
     * @return retourne filtreEnseignantNom
     */
    public String getFiltreEnseignantNom() {
        return filtreEnseignantNom;
    }

    /**
     * Mutateur de filtreEnseignantNom {@link #filtreEnseignantNom}.
     * @param filtreEnseignantNom le filtreEnseignantNom to set
     */
    public void setFiltreEnseignantNom(String filtreEnseignantNom) {
        this.filtreEnseignantNom = filtreEnseignantNom;
    }

    /**
     * Accesseur de filtreEnseignantPrenom {@link #filtreEnseignantPrenom}.
     * @return retourne filtreEnseignantPrenom
     */
    public String getFiltreEnseignantPrenom() {
        return filtreEnseignantPrenom;
    }

    /**
     * Mutateur de filtreEnseignantPrenom {@link #filtreEnseignantPrenom}.
     * @param filtreEnseignantPrenom le filtreEnseignantPrenom to set
     */
    public void setFiltreEnseignantPrenom(String filtreEnseignantPrenom) {
        this.filtreEnseignantPrenom = filtreEnseignantPrenom;
    }

    /**
     * @return the dateEffet
     */
    public Date getDateEffet() {
        return dateEffet;
    }

    /**
     * @param dateEffet the dateEffet to set
     */
    public void setDateEffet(Date dateEffet) {
        this.dateEffet = dateEffet;
    }
}
