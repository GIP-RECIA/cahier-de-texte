/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: FileUploadDTO.java,v 1.9 2009/07/30 15:09:26 ent_breyton Exp $
 */

package org.crlr.web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Un DTO pour l'upload de fichier.
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
public class FileUploadDTO implements Serializable {
    /** uid. */
    private static final long serialVersionUID = 5467973494817204050L;

    /** Id de la pièce jointe. */
    private Integer id;

    /** Le nom de la PJ. */
    private String nom;

    /** L'id de seance correspondant. */
    private Integer idSeance;

    /** L'id de devoir correspondant. */
    private Integer idDevoir;

    /** Le chemin pour le téléchargement. 
     * ex : /uploadFile/kac0013r/Documents du CTN/abc/README.mimetex*/
    private String pathFullDownload;
    
    /** Le chemin absolu. */
    private String pathAbsolute;
    
    /** Le chemin stock" en BDD.  C'est le chemin 'locale' sans le dossier fix uploadFile ni l'uid
     * /Documents du CTN/abc/README.mimetex */
    private String pathDB;

    /** True si la pièce jointe existe en base sinon false. */
    private boolean enBase;

    /** Uid utilisateur utilisé dans le path du fichier. */
    private String uid;

    /** Pour activer le lien sur la pièce jointe ou pas. */
    private Boolean activerLien;
    
    /**
     * Liste des id de PJ correspondant au meme fichier physisque.
     */
    private List<Integer> listeIdPJ;

/**
     * 
     * Constructeur.
     */
    public FileUploadDTO() {       
        enBase = false;
        activerLien = false;
        listeIdPJ = new ArrayList<Integer>();
    }


    /**
     * Accesseur de nom.
     * @return le nom
     */
    public String getNom() {
        return nom;
    }


    /**
     * Accesseur de nom raccourci à 60 caractère.
     * @return le nom raccourci.
     */
    public String getNomCourt() {
        return StringUtils.abbreviate(nom, 60);
    }


    /**
     * Mutateur de nom.
     * @param nom le nom à modifier.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }


    /**
     * Accesseur idSeance.
     *
     * @return idSeance
     */
    public Integer getIdSeance() {
        return idSeance;
    }

    /**
     * Mutateur idSeance.
     *
     * @param idSeance Le idSeance à modifier
     */
    public void setIdSeance(Integer idSeance) {
        this.idSeance = idSeance;
    }

    /**
     * Accesseur idDevoir.
     *
     * @return idDevoir
     */
    public Integer getIdDevoir() {
        return idDevoir;
    }

    /**
     * Mutateur idDevoir.
     *
     * @param idDevoir Le idDevoir à modifier
     */
    public void setIdDevoir(Integer idDevoir) {
        this.idDevoir = idDevoir;
    }

    /**
     * Accesseur enBase.
     *
     * @return enBase
     */
    public boolean getEnBase() {
        return enBase;
    }

    /**
     * Mutateur enBase.
     *
     * @param enBase Le enBase à modifier
     */
    public void setEnBase(boolean enBase) {
        this.enBase = enBase;
    }

    /**
     * Accesseur uid.
     *
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Mutateur uid.
     *
     * @param uid Le uid à modifier
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Accesseur activerLien.
     *
     * @return activerLien
     */
    public Boolean getActiverLien() {
        return activerLien;
    }

    /**
     * Mutateur activerLien.
     *
     * @param activerLien Le activerLien à modifier
     */
    public void setActiverLien(Boolean activerLien) {
        this.activerLien = activerLien;
    }

    /**
     * Accesseur id.
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur id.
     *
     * @param id Le id à modifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur pathFullDownload.
     * @return le pathFullDownload.
     */
    public String getPathFullDownload() {
        return pathFullDownload;
    }

    /**
     * Mutateur pathFullDownload.
     * @param pathFullDownload le pathFullDownload à modifier.
     */
    public void setPathFullDownload(String pathFullDownload) {
        this.pathFullDownload = pathFullDownload;
    }

    /**
     * Accesseur pathAbsolute.
     * @return le pathAbsolute.
     */
    public String getPathAbsolute() {
        return pathAbsolute;
    }

    /**
     * Mutateur pathAbsolute.
     * @param pathAbsolute le pathAbsolute à modifier.
     */
    public void setPathAbsolute(String pathAbsolute) {
        this.pathAbsolute = pathAbsolute;
    }

    /**
     * Accesseur pathDB.
     * @return le pathDB.
     */
    public String getPathDB() {
        return pathDB;
    }

    /**
     * Mutateur pathDB.
     * @param pathDB le pathDB à modifier.
     */
    public void setPathDB(String pathDB) {
        this.pathDB = pathDB;
    }
    
    /**
     * Accesseur à la liste des ID de PJ.
     * @return listeIdPJ
     */
    public List<Integer> getListeIdPJ() {
        return listeIdPJ;
    }

    /**
     * Mutateur listeIdPJ.
     * @param listeIdPJ listeIdPJ
     */
    public void setListeIdPJ(List<Integer> listeIdPJ) {
        this.listeIdPJ = listeIdPJ;
    }
}
