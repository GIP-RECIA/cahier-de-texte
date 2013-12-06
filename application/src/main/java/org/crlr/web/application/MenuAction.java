/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * 
 */
 
package org.crlr.web.application;

import java.io.Serializable;
import java.util.List;

/**
 * Classe utilisee pour stocker les information correspondant a une action. 
 *
 * @author durupt
 */
public class MenuAction implements Serializable {
    
    /**  Serial. */
    private static final long serialVersionUID = -1331102203256073845L;

    /** url de l'image associee a cette action. */
    private String image;
    
    /** Url de l'action a executer. */
    private String action;
    
    /** Libelle du texte associee a cette action. */
    private String libelle;

    /** Liste des sous menu. */ 
    private List<MenuAction> listeSousAction;
    
    /** The Constant ACTION_SUFFIXE pour un outil du menu. */
    public static final String ACTION_SUFFIXE = "@menu";

    /**
     * The Constant ACTION_SUFFIXE pour un outil du menu liée à une action dans
     * l'arbre.
     */
    public static final String ACTION_ARBRE_SUFFIXE = "@arbre";
    
    /** The Constant ACTION_SUFFIXE pour un outil considéré comme un souos écran. */
    public static final String ACTION_SOUS_ECRAN_SUFFIXE = "@sousEcran";

    
    public static final String ACTION_CLIQUE_ARBRE = ACTION_SUFFIXE + ACTION_ARBRE_SUFFIXE;;

    
    
    /** 
     * Constructeur de la classe MenuAction.
     * @param image : url de l'image
     * @param action : url de l'action a executer
     * @param libelle : libelle de l'action
     * @param listeSousAction : liste des sous actions
     */
    public MenuAction(String image, String action, String libelle, List<MenuAction> listeSousAction) {
        super();
        this.image = image;
        this.action = action;
        this.libelle = libelle;
        this.listeSousAction = listeSousAction;
    }

    /**
     * Accesseur de image {@link #image}.
     * @return retourne image
     */
    public String getImage() {
        return image;
    }

    /**
     * Mutateur de image {@link #image}.
     * @param image le image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Accesseur de action {@link #action}.
     * @return retourne action
     */
    public String getAction() {
        return action;
    }

    /**
     * Mutateur de action {@link #action}.
     * @param action le action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Accesseur de libelle {@link #libelle}.
     * @return retourne libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Mutateur de libelle {@link #libelle}.
     * @param libelle le libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Accesseur de serialVersionUID {@link #serialVersionUID}.
     * @return retourne serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * Action en provenance d'un lien de l'arbre (une branche).
     *
     * @return l'identifiant de l'outil avec les suffixes adéquates.
     */
    public String actionArbre() {
        return this.action + ACTION_CLIQUE_ARBRE;
    }

    /**
     * Accesseur de listeSousAction {@link #listeSousAction}.
     * @return retourne listeSousAction
     */
    public List<MenuAction> getListeSousAction() {
        return listeSousAction;
    }

    /**
     * Mutateur de listeSousAction {@link #listeSousAction}.
     * @param listeSousAction le listeSousAction to set
     */
    public void setListeSousAction(List<MenuAction> listeSousAction) {
        this.listeSousAction = listeSousAction;
    }
}
