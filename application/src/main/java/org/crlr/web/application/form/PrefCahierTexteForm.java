package org.crlr.web.application.form;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.base.EnfantDTO;
import org.crlr.dto.application.base.EtablissementDTO;

/**
 * Formulaire de preference.
 * 
 * @author mcaze
 */
public class PrefCahierTexteForm extends AbstractForm {

    /** Nom prénom Enfant selectionné. */
    private EnfantDTO enfantSelectionne;

    /** liste des enfants d'un parent, pour un profil Parent. */
    private List<EnfantDTO> listeEnfant;
    
    /** liste des établissements disponible. */
    private List<EtablissementDTO> listeEtablissement;

    /** le code de l'etablissement selectionne. */
    private EtablissementDTO etablissementSelectionne;
    
    /** Flitre pour la popup d'établissement. */
    private String filtreEtablissement;
    
    /**
     *  Serial.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur.
     */
    public PrefCahierTexteForm() {
        super();
        this.listeEnfant = new ArrayList<EnfantDTO>();
        this.listeEtablissement = new ArrayList<EtablissementDTO>();
        this.etablissementSelectionne = new EtablissementDTO();
        this.filtreEtablissement = "";
    }

    /**
     * Accesseur de listeEnfant {@link #listeEnfant}.
     * 
     * @return retourne listeEnfant
     */
    public List<EnfantDTO> getListeEnfant() {
        return listeEnfant;
    }

    /**
     * Mutateur de listeEnfant {@link #listeEnfant}.
     * 
     * @param listeEnfant
     *            le listeEnfant to set
     */
    public void setListeEnfant(List<EnfantDTO> listeEnfant) {
        this.listeEnfant = listeEnfant;
    }

    /**
     * Accesseur de listeEtablissement {@link #listeEtablissement}.
     * 
     * @return retourne listeEtablissement
     */
    public List<EtablissementDTO> getListeEtablissement() {
        return listeEtablissement;
    }

    /**
     * Mutateur de listeEtablissement {@link #listeEtablissement}.
     * 
     * @param listeEtablissement
     *            le listeEtablissement to set
     */
    public void setListeEtablissement(List<EtablissementDTO> listeEtablissement) {
        this.listeEtablissement = listeEtablissement;
    }

    /**
     * Accesseur de etablissementSelectionne {@link #etablissementSelectionne}.
     * 
     * @return retourne etablissementSelectionne
     */
    public EtablissementDTO getEtablissementSelectionne() {
        return etablissementSelectionne;
    }

    /**
     * Mutateur de etablissementSelectionne {@link #etablissementSelectionne}.
     * 
     * @param etablissementSelectionne
     *            le etablissementSelectionne to set
     */
    public void setEtablissementSelectionne(
            EtablissementDTO etablissementSelectionne) {
        this.etablissementSelectionne = etablissementSelectionne;
    }

    /**
     * Accesseur de filtreEtablissement {@link #filtreEtablissement}.
     * 
     * @return retourne filtreEtablissement
     */
    public String getFiltreEtablissement() {
        return filtreEtablissement;
    }

    /**
     * Mutateur de filtreEtablissement {@link #filtreEtablissement}.
     * 
     * @param filtreEtablissement
     *            le filtreEtablissement to set
     */
    public void setFiltreEtablissement(String filtreEtablissement) {
        this.filtreEtablissement = filtreEtablissement;
    }

    /**
     * Accesseur de enfantSelectionne {@link #enfantSelectionne}.
     * 
     * @return retourne enfantSelectionne
     */
    public EnfantDTO getEnfantSelectionne() {
        return enfantSelectionne;
    }

    /**
     * Mutateur de enfantSelectionne {@link #enfantSelectionne}.
     * 
     * @param enfantSelectionne
     *            le enfantSelectionne to set
     */
    public void setEnfantSelectionne(EnfantDTO enfantSelectionne) {
        this.enfantSelectionne = enfantSelectionne;
    }
	
}
