/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConsoliderEmpForm.java,v 1.10 2010/05/20 14:42:31 ent_breyton Exp $
 */

package org.crlr.web.application.form;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.TypeGroupe;


/**
 * .
 *
 * @author $author$
 */
public class ClasseGroupeForm extends AbstractForm {
    /**
     * 
     */
    private static final long serialVersionUID = 8236728407366666453L;

    /**Filtre classe/groupe dans le popup. */
    private String filtreClasseGroupe;
    
    /** critère temporaire de selection du type de groupe. */
    private TypeGroupe typeGroupeSelectionne;
    
    private Boolean tous; 
    
    /** groupe ou classe sélectionnée dans la popup. */
    private GroupesClassesDTO groupeClasseSelectionne;

    /** liste des groupes ou classes. */
    private List<GroupesClassesDTO> listeGroupeClasse;
    
    /** Filtre sur l'enseignant (utilisee pour les profil comme directeur ou inspecteur 
     * sur des ecran comme visa qui proposent en premier une selection de l'enseignan.).
     */
    private Integer idEnseignantFiltre; 
    
    /**
     * Liste des groupes associé à une classe via cahier_groupe_classe.
     */
    private List<GroupeDTO> listeGroupe;
    
  //Profile d'utilisateur connécté
    private Profil profil;

    /** Indique si on est en mode archive ou pas. */
    private Boolean archive;
    
    /** Exercice (en mode archive).*/ 
    private String exercice;
    
    /**
     * @return the filtreClasseGroupe
     */
    public String getFiltreClasseGroupe() {
        return filtreClasseGroupe;
    }

    /**
     * @param filtreClasseGroupe the filtreClasseGroupe to set
     */
    public void setFiltreClasseGroupe(String filtreClasseGroupe) {
        this.filtreClasseGroupe = filtreClasseGroupe;
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

    /**
     * @return the groupeClasseSelectionne
     */
    public GroupesClassesDTO getGroupeClasseSelectionne() {
        return groupeClasseSelectionne;
    }

    /**
     * @param groupeClasseSelectionne the groupeClasseSelectionne to set
     */
    public void setGroupeClasseSelectionne(GroupesClassesDTO groupeClasseSelectionne) {
        this.groupeClasseSelectionne = groupeClasseSelectionne;
    }

    /**
     * @return the listeGroupeClasse
     */
    public List<GroupesClassesDTO> getListeGroupeClasse() {
        return listeGroupeClasse;
    }

    /**
     * @param listeGroupeClasse the listeGroupeClasse to set
     */
    public void setListeGroupeClasse(List<GroupesClassesDTO> listeGroupeClasse) {
        this.listeGroupeClasse = listeGroupeClasse;
    } 
    
    /**
     * 
     */
    public void reset() {
        this.typeGroupeSelectionne = TypeGroupe.CLASSE;
        this.listeGroupeClasse = new ArrayList<GroupesClassesDTO>();
        this.groupeClasseSelectionne = null;
        

        this.listeGroupe = new ArrayList<GroupeDTO>();

        filtreClasseGroupe = "";
    }

    /**
     * @return the listeGroupe
     */
    public List<GroupeDTO> getListeGroupe() {
        return listeGroupe;
    }

    /**
     * @param listeGroupe the listeGroupe to set
     */
    public void setListeGroupe(List<GroupeDTO> listeGroupe) {
        this.listeGroupe = listeGroupe;
    }

    /**
     * @return the profil
     */
    public Profil getProfil() {
        return profil;
    }

    /**
     * @param profil the profil to set
     */
    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    /**
     * @return the tous
     */
    public Boolean getTous() {
        return tous;
    }

    /**
     * @param tous the tous to set
     */
    public void setTous(Boolean tous) {
        this.tous = tous;
    }

    /**
     * Accesseur de archive {@link #archive}.
     * @return retourne archive
     */
    public Boolean getArchive() {
        return archive;
    }

    /**
     * Mutateur de archive {@link #archive}.
     * @param archive le archive to set
     */
    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    /**
     * Accesseur de exercice {@link #exercice}.
     * @return retourne exercice
     */
    public String getExercice() {
        return exercice;
    }

    /**
     * Mutateur de exercice {@link #exercice}.
     * @param exercice le exercice to set
     */
    public void setExercice(String exercice) {
        this.exercice = exercice;
    }

    /**
     * Accesseur de idEnseignantFiltre {@link #idEnseignantFiltre}.
     * @return retourne idEnseignantFiltre
     */
    public Integer getIdEnseignantFiltre() {
        return idEnseignantFiltre;
    }

    /**
     * Mutateur de idEnseignantFiltre {@link #idEnseignantFiltre}.
     * @param idEnseignantFiltre le idEnseignantFiltre to set
     */
    public void setIdEnseignantFiltre(Integer idEnseignantFiltre) {
        this.idEnseignantFiltre = idEnseignantFiltre;
    }
    
    
}