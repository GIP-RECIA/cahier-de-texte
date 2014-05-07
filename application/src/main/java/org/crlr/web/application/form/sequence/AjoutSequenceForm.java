/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AjoutSequenceForm.java,v 1.6 2010/05/19 09:10:20 jerome.carriere Exp $
 */

package org.crlr.web.application.form.sequence;

import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.TypeGroupe;

import org.apache.commons.lang.StringUtils;
import org.crlr.web.application.form.AbstractPopupForm;
import org.crlr.web.dto.TypeCouleur;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.6 $
  */
public class AjoutSequenceForm extends AbstractPopupForm {
    /**  */
    private static final long serialVersionUID = 6247931318863172732L;

    /** liste des séquences dans la popup. */
    private List<EnseignementDTO> listeEnseignementSelectionne;

    /** DOCUMENTATION INCOMPLETE! */
    private EnseignementDTO enseignementSelectionne;

    /** critère temporaire de selection du type de groupe. */
    private TypeGroupe typeGroupeSelectionne;

    /** groupe ou classe sélectionnée dans la popup. */
    private GroupesClassesDTO groupeClasseSelectionne;

    /** liste des groupes ou classes. */
    private List<GroupesClassesDTO> listeGroupeClasse;

    /** DOCUMENTATION INCOMPLETE! */
    private String code;

    /** DOCUMENTATION INCOMPLETE! */
    private String intitule;

    /** DOCUMENTATION INCOMPLETE! */
    private Date dateDebut;

    /** DOCUMENTATION INCOMPLETE! */
    private Date dateFin;

    /** DOCUMENTATION INCOMPLETE! */
    private String description;
    
    /** Filtre pour la popup classe / groupe. **/
    private String filtreClasseGroupe;
    
    /** Filtre pour la popup enseignement. **/
    private String filtreEnseignement;

    /** Liste des couleur possibles dans le popup d'emploi de temps. */
    private List<TypeCouleur> listeCouleur = new ArrayList<TypeCouleur>();
 
	/** Type couleur sélectionné. */
    private TypeCouleur typeCouleur;    
/**
     * Constructeur.
     */
    public AjoutSequenceForm() {
        listeEnseignementSelectionne = new ArrayList<EnseignementDTO>();
        reset();
    }

    /**
     * Méthode de réinitialisation des données du formulaire saisie par
     * l'utilisateur.
     */
    public void reset() {       
        super.resetChampsObligatoire();
        enseignementSelectionne = new EnseignementDTO();
        typeGroupeSelectionne = null;
        groupeClasseSelectionne = new GroupesClassesDTO();
        listeGroupeClasse = new ArrayList<GroupesClassesDTO>();
        intitule = null;
        dateDebut = null;
        dateFin = null;
        description = null;
        code = null;
        filtreClasseGroupe = "";
        filtreEnseignement = "";
        listeCouleur = GenerateurDTO.generateBarreCouleur();  
    }
    

    /**
     * Filtre les valeurs de la popup des classes et groupes.
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public Boolean doFilterClasseGroupe(final Object value) {
        if (StringUtils.isEmpty(this.filtreClasseGroupe)) {
            return true;
        }
        final GroupesClassesDTO groupesClassesDTO = (GroupesClassesDTO) value;

        return groupesClassesDTO.getIntitule().toLowerCase()
                                .contains(this.filtreClasseGroupe.toLowerCase());
    }
    
    /**
     * Filtre les valeurs de la popup des enseignements.
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public Boolean doFilterEnseignement(final Object value) {
        if (StringUtils.isEmpty(this.filtreEnseignement)) {
            return true;
        } 
        final EnseignementDTO enseignementDTO = (EnseignementDTO) value;

        return enseignementDTO.getIntitule().toLowerCase()
                                .contains(this.filtreEnseignement.toLowerCase());
    }

    /**
     * Accesseur listeEnseignementSelectionne.
     * @return le listeEnseignementSelectionne.
     */
    public List<EnseignementDTO> getListeEnseignementSelectionne() {
        return listeEnseignementSelectionne;
    }

    /**
     * Mutateur listeEnseignementSelectionne.
     * @param listeEnseignementSelectionne le listeEnseignementSelectionne à modifier.
     */
    public void setListeEnseignementSelectionne(
            List<EnseignementDTO> listeEnseignementSelectionne) {
        this.listeEnseignementSelectionne = listeEnseignementSelectionne;
    }

    /**
     * Accesseur enseignementSelectionne.
     * @return le enseignementSelectionne.
     */
    public EnseignementDTO getEnseignementSelectionne() {
        return enseignementSelectionne;
    }

    /**
     * Mutateur enseignementSelectionne.
     * @param enseignementSelectionne le enseignementSelectionne à modifier.
     */
    public void setEnseignementSelectionne(EnseignementDTO enseignementSelectionne) {
        this.enseignementSelectionne = enseignementSelectionne;
    }

   

    /**
     * Accesseur groupeClasseSelectionne.
     * @return le groupeClasseSelectionne.
     */
    public GroupesClassesDTO getGroupeClasseSelectionne() {
        return groupeClasseSelectionne;
    }

    /**
     * Mutateur groupeClasseSelectionne.
     * @param groupeClasseSelectionne le groupeClasseSelectionne à modifier.
     */
    public void setGroupeClasseSelectionne(GroupesClassesDTO groupeClasseSelectionne) {
        this.groupeClasseSelectionne = groupeClasseSelectionne;
    }

    /**
     * Accesseur listeGroupeClasse.
     * @return le listeGroupeClasse.
     */
    public List<GroupesClassesDTO> getListeGroupeClasse() {
        return listeGroupeClasse;
    }

    /**
     * Mutateur listeGroupeClasse.
     * @param listeGroupeClasse le listeGroupeClasse à modifier.
     */
    public void setListeGroupeClasse(List<GroupesClassesDTO> listeGroupeClasse) {
        this.listeGroupeClasse = listeGroupeClasse;
    }

    /**
     * Accesseur code.
     * @return le code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Mutateur code.
     * @param code le code à modifier.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Accesseur intitule.
     * @return le intitule.
     */
    public String getIntitule() {
        return intitule;
    }

    /**
     * Mutateur intitule.
     * @param intitule le intitule à modifier.
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    /**
     * Accesseur dateDebut.
     * @return le dateDebut.
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * Mutateur dateDebut.
     * @param dateDebut le dateDebut à modifier.
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Accesseur dateFin.
     * @return le dateFin.
     */
    public Date getDateFin() {
        return dateFin;
    }

    /**
     * Mutateur dateFin.
     * @param dateFin le dateFin à modifier.
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * Accesseur description.
     * @return le description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutateur description.
     * @param description le description à modifier.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Accesseur filtreClasseGroupe.
     * @return le filtreClasseGroupe
     */
    public String getFiltreClasseGroupe() {
        return filtreClasseGroupe;
    }

    /**
     * Mutateur de filtreClasseGroupe.
     * @param filtreClasseGroupe le filtreClasseGroupe à modifier.
     */
    public void setFiltreClasseGroupe(String filtreClasseGroupe) {
        this.filtreClasseGroupe = filtreClasseGroupe;
    }

    /**
     * Accesseur filtreEnseignement.
     * @return le filtreEnseignement
     */
    public String getFiltreEnseignement() {
        return filtreEnseignement;
    }

    /**
     * Mutateur de filtreEnseignement.
     * @param filtreEnseignement le filtreEnseignement à modifier.
     */
    public void setFiltreEnseignement(String filtreEnseignement) {
        this.filtreEnseignement = filtreEnseignement;
    }

    /**
     * @return the typeGroupeSelectionne
     */
    public TypeGroupe getTypeGroupeSelectionne() {
        return typeGroupeSelectionne;
    }

    /**
     * @param typeGroupeSelectionne the typeGroupeSelectionne to set
     */
    public void setTypeGroupeSelectionne(TypeGroupe typeGroupeSelectionne) {
        this.typeGroupeSelectionne = typeGroupeSelectionne;
    }

	public List<TypeCouleur> getListeCouleur() {
		return listeCouleur;
	}

	public void setListeCouleur(List<TypeCouleur> listeCouleur) {
		this.listeCouleur = listeCouleur;
	}

	public TypeCouleur getTypeCouleur() {
		return typeCouleur;
	}

	public void setTypeCouleur(TypeCouleur typeCouleur) {
		this.typeCouleur = typeCouleur;
	}
}
