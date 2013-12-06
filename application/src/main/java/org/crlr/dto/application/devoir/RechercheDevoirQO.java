/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheDevoirQO.java,v 1.9 2010/05/20 10:08:46 jerome.carriere Exp $
 */

package org.crlr.dto.application.devoir;

import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.TypeCategorieTypeDevoir;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.TypeReglesDevoir;

import org.crlr.metier.entity.ClasseBean;
import org.crlr.metier.entity.GroupeBean;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

/**
 * RechercheDevoirQO.
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
public class RechercheDevoirQO implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;

    /** DOCUMENTATION INCOMPLETE! */
    private Date jourCourant;

    /** DOCUMENTATION INCOMPLETE! */
    private Date premierJourSemaine;

    /** DOCUMENTATION INCOMPLETE! */
    private Date dernierJourSemaine;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idEnseignant;
    
    /** DOCUMENTATION INCOMPLETE! */
    private Integer idInspecteur;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idEleve;

    /** critère temporaire de selection du type de groupe. */
    private TypeGroupe typeGroupeSelectionne;

    /** groupe ou classe sélectionnée dans la popup. */
    private GroupesClassesDTO groupeClasseSelectionne;

    /** liste des groupes ou classes. */
    private List<GroupesClassesDTO> listeGroupeClasse;

    /** liste des groupes selectionnés qui sont associés à la classe. */
    private List<GroupeDTO> listeGroupeDTO;

    /** liste des groupes d'un eleve. */
    private List<GroupeBean> listeGroupeBean;

    /** liste des classes d'un eleve. */
    private List<ClasseBean> listeClasseBean;

    /** Type règles, le message de retour. */
    private TypeReglesDevoir typeReglesDevoirMsg;
    
    /** DOCUMENTATION INCOMPLETE! */
    private Integer idEtablissement;

    /** Categorie du type de travail a faire recherche. */
    private TypeCategorieTypeDevoir typeCategorie;
    
    /** Enseignement selectionee. */
    private Integer idEnseignement;
    
    /**
     * 
     * Constructeur.
     */
    public RechercheDevoirQO() {
    }

    /**
     * Accesseur typeReglesDevoirMsg.
     *
     * @return le typeReglesDevoirMsg
     */
    public TypeReglesDevoir getTypeReglesDevoirMsg() {
        return typeReglesDevoirMsg;
    }

    /**
     * Mutateur de typeReglesDevoirMsg.
     *
     * @param typeReglesDevoirMsg le typeReglesDevoirMsg à modifier.
     */
    public void setTypeReglesDevoirMsg(TypeReglesDevoir typeReglesDevoirMsg) {
        this.typeReglesDevoirMsg = typeReglesDevoirMsg;
    }

    /**
     * Accesseur jourCourant.
     *
     * @return le jourCourant.
     */
    public Date getJourCourant() {
        return jourCourant;
    }

    /**
     * Mutateur jourCourant.
     *
     * @param jourCourant le jourCourant à modifier.
     */
    public void setJourCourant(Date jourCourant) {
        this.jourCourant = jourCourant;
    }

    /**
     * Accesseur premierJourSemaine.
     *
     * @return le premierJourSemaine.
     */
    public Date getPremierJourSemaine() {
        return premierJourSemaine;
    }

    /**
     * Mutateur premierJourSemaine.
     *
     * @param premierJourSemaine le premierJourSemaine à modifier.
     */
    public void setPremierJourSemaine(Date premierJourSemaine) {
        this.premierJourSemaine = premierJourSemaine;
    }

    /**
     * Accesseur dernierJourSemaine.
     *
     * @return le dernierJourSemaine.
     */
    public Date getDernierJourSemaine() {
        return dernierJourSemaine;
    }

    /**
     * Mutateur dernierJourSemaine.
     *
     * @param dernierJourSemaine le dernierJourSemaine à modifier.
     */
    public void setDernierJourSemaine(Date dernierJourSemaine) {
        this.dernierJourSemaine = dernierJourSemaine;
    }

    /**
     * Accesseur idEnseignant.
     *
     * @return le idEnseignant.
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur idEnseignant.
     *
     * @param idEnseignant le idEnseignant à modifier.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur idEleve.
     *
     * @return le idEleve.
     */
    public Integer getIdEleve() {
        return idEleve;
    }

    /**
     * Mutateur idEleve.
     *
     * @param idEleve le idEleve à modifier.
     */
    public void setIdEleve(Integer idEleve) {
        this.idEleve = idEleve;
    }

    

    /**
     * Accesseur groupeClasseSelectionne.
     *
     * @return le groupeClasseSelectionne.
     */
    public GroupesClassesDTO getGroupeClasseSelectionne() {
        return groupeClasseSelectionne;
    }

    /**
     * Mutateur groupeClasseSelectionne.
     *
     * @param groupeClasseSelectionne le groupeClasseSelectionne à modifier.
     */
    public void setGroupeClasseSelectionne(GroupesClassesDTO groupeClasseSelectionne) {
        this.groupeClasseSelectionne = groupeClasseSelectionne;
    }

    /**
     * Accesseur listeGroupeClasse.
     *
     * @return le listeGroupeClasse.
     */
    public List<GroupesClassesDTO> getListeGroupeClasse() {
        return listeGroupeClasse;
    }

    /**
     * Mutateur listeGroupeClasse.
     *
     * @param listeGroupeClasse le listeGroupeClasse à modifier.
     */
    public void setListeGroupeClasse(List<GroupesClassesDTO> listeGroupeClasse) {
        this.listeGroupeClasse = listeGroupeClasse;
    }

    /**
     * Accesseur listeGroupeDTO.
     *
     * @return le listeGroupeDTO.
     */
    public List<GroupeDTO> getListeGroupeDTO() {
        return listeGroupeDTO;
    }

    /**
     * Mutateur listeGroupeDTO.
     *
     * @param listeGroupeDTO le listeGroupeDTO à modifier.
     */
    public void setListeGroupeDTO(List<GroupeDTO> listeGroupeDTO) {
        this.listeGroupeDTO = listeGroupeDTO;
    }

    /**
     * Accesseur listeGroupeBean.
     *
     * @return le listeGroupeBean.
     */
    public List<GroupeBean> getListeGroupeBean() {
        return listeGroupeBean;
    }

    /**
     * Mutateur listeGroupeBean.
     *
     * @param listeGroupeBean le listeGroupeBean à modifier.
     */
    public void setListeGroupeBean(List<GroupeBean> listeGroupeBean) {
        this.listeGroupeBean = listeGroupeBean;
    }

    /**
     * Accesseur listeClasseBean.
     *
     * @return le listeClasseBean.
     */
    public List<ClasseBean> getListeClasseBean() {
        return listeClasseBean;
    }

    /**
     * Mutateur listeClasseBean.
     *
     * @param listeClasseBean le listeClasseBean à modifier.
     */
    public void setListeClasseBean(List<ClasseBean> listeClasseBean) {
        this.listeClasseBean = listeClasseBean;
    }

    /**
     * Accesseur idEtablissement.
     * @return le idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur de idEtablissement.
     * @param idEtablissement le idEtablissement à modifier.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur de idInspecteur.
     * @return le idInspecteur
     */
    public Integer getIdInspecteur() {
        return idInspecteur;
    }

    /**
     * Mutateur de idInspecteur.
     * @param idInspecteur le idInspecteur à modifier.
     */
    public void setIdInspecteur(Integer idInspecteur) {
        this.idInspecteur = idInspecteur;
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
     * Accesseur de typeCategorie {@link #typeCategorie}.
     * @return retourne typeCategorie
     */
    public TypeCategorieTypeDevoir getTypeCategorie() {
        return typeCategorie;
    }

    /**
     * Mutateur de typeCategorie {@link #typeCategorie}.
     * @param typeCategorie le typeCategorie to set
     */
    public void setTypeCategorie(TypeCategorieTypeDevoir typeCategorie) {
        this.typeCategorie = typeCategorie;
    }

    /**
     * Accesseur de idEnseignement {@link #idEnseignement}.
     * @return retourne idEnseignement
     */
    public Integer getIdEnseignement() {
        return idEnseignement;
    }

    /**
     * Mutateur de idEnseignement {@link #idEnseignement}.
     * @param idEnseignement le idEnseignement to set
     */
    public void setIdEnseignement(Integer idEnseignement) {
        this.idEnseignement = idEnseignement;
    }
    
    
}
