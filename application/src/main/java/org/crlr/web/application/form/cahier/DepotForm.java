/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.application.form.cahier;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.base.FichierStockageDTO;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.utils.FileSystemNode;

/**
 * Popup pièces jointes.
 *
 * @author j.carriere
 */
public class DepotForm extends AbstractForm {
    /** The Constant serialVersionUID. */    
    
    private static final long serialVersionUID = -1122279909185538695L;

    /** Nom du fichier deplace. */
    private String nomFichierDrag;
    
    /** Nom du dossier dans lequel on drop le fichier. */
    private String nomDossierDrop;
    
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private List<FileSystemNode> sourceRootsDepotCahier;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private List<FileSystemNode> sourceRootsDepotCahierFiles;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private FichierStockageDTO fichierStockageSelectionne = new FichierStockageDTO(); 
    
    /** DOCUMENTATION INCOMPLETE! */
    private Integer uploadsAvailable;

    /**
     * Dossier root du depot de cahier de texte : "/uploadFile//uploadFile/kbj0027f/
     * .
     */
    private String pathDepotCahier;
    
    /**
     * Dossier complet du dossier selectionné : "/uploadFile/kbj0027f/Mes Documents/Chris"
     * .
     */
    private String pathRepertoireEnCoursCahier;
    
    /**
     * Nom du dossier sélectionné hors root : "Mes documents/Chris" 
     * .
     */
    private String nameRepSel;   
    
    /**
     * Nom du dossier parent hors root contenant le dossier sélectionné : "Mes documents".
     */
    private String nameDossierParentSel;  
    
    /**
     * Nom du sous dossier sélectionné hors root et dossier parent : "Chris"
     * .
     */
    private String nameDossierSel;
    
    /**
     * Indique si l'espace de stockage a été activé pour l'user connecté.
     */
    private Boolean espaceStockageActive;
    
    /**
     * Indique si le dossier actif peut être modifier/supprimer.
     */
    private Boolean isDossierModifiable;

    /**
     * Liste des fichiers contenus dans le dossier selectionnés.
     */
    private ArrayList<FileUploadDTO> files;
    
    
    private FileUploadDTO fichierSelectionne;
    
    /** Indique si la gestion de l'espace de stockage est active dans cet environnement. */
    private Boolean vraixOuFauxAffichageStockage;

    /**
     * Constructeur.
     */
    public DepotForm() {
        uploadsAvailable = 99;     
        nameRepSel = null;
        espaceStockageActive = false;
        files = new ArrayList<FileUploadDTO>();
    }

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String getPathDepotCahier() {
        return pathDepotCahier;
    }

    /**
     *
     *
     * @param pathDepotCahier DOCUMENTATION INCOMPLETE!
     */
    public void setPathDepotCahier(String pathDepotCahier) {
        this.pathDepotCahier = pathDepotCahier;
    }

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public List<FileSystemNode> getSourceRootsDepotCahier() {
        return sourceRootsDepotCahier;
    }

    /**
     *
     *
     * @param sourceRootsDepotCahier DOCUMENTATION INCOMPLETE!
     */
    public void setSourceRootsDepotCahier(List<FileSystemNode> sourceRootsDepotCahier) {
        this.sourceRootsDepotCahier = sourceRootsDepotCahier;
    }

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public List<FileSystemNode> getSourceRootsDepotCahierFiles() {
        return sourceRootsDepotCahierFiles;
    }

    /**
     *
     *
     * @param sourceRootsDepotCahierFiles DOCUMENTATION INCOMPLETE!
     */
    public void setSourceRootsDepotCahierFiles(List<FileSystemNode> sourceRootsDepotCahierFiles) {
        this.sourceRootsDepotCahierFiles = sourceRootsDepotCahierFiles;
    }

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String getPathRepertoireEnCoursCahier() {
        return pathRepertoireEnCoursCahier;
    }

    /**
     *
     *
     * @param pathRepertoireEnCoursCahier DOCUMENTATION INCOMPLETE!
     */
    public void setPathRepertoireEnCoursCahier(String pathRepertoireEnCoursCahier) {
        this.pathRepertoireEnCoursCahier = pathRepertoireEnCoursCahier;
    }

    /**
     * Accesseur fichierStockageSelectionne.
     *
     * @return le fichierStockageSelectionne.
     */
    public FichierStockageDTO getFichierStockageSelectionne() {
        return fichierStockageSelectionne;
    }

    /**
     * Mutateur fichierStockageSelectionne.
     *
     * @param fichierStockageSelectionne le fichierStockageSelectionne à modifier.
     */
    public void setFichierStockageSelectionne(FichierStockageDTO fichierStockageSelectionne) {
        this.fichierStockageSelectionne = fichierStockageSelectionne;
    }
    
    /**
     * Accesseur uploadsAvailable.
     * @return le uploadsAvailable.
     */
    public Integer getUploadsAvailable() {
        return uploadsAvailable;
    }

    /**
     * Mutateur uploadsAvailable.
     * @param uploadsAvailable le uploadsAvailable à modifier.
     */
    public void setUploadsAvailable(Integer uploadsAvailable) {
        this.uploadsAvailable = uploadsAvailable;
    }

    /**
     * Accesseur nameRepSel.
     * @return le nameRepSel.
     */
    public String getNameRepSel() {
        return nameRepSel;
    }

    /**
     * Mutateur nameRepSel.
     * @param nameRepSel le nameRepSel à modifier.
     */
    public void setNameRepSel(String nameRepSel) {
        this.nameRepSel = nameRepSel;
    }

    /**
     * Accesseur espaceStockageActive.
     * @return espaceStockageActive
     */
    public Boolean getEspaceStockageActive() {
        return espaceStockageActive;
    }

    /**
     * Accesseur nameDossierParentSel.
     * @return nameDossierParentSel
     */
    public String getNameDossierParentSel() {
        return nameDossierParentSel;
    }

    /**
     * Mutateur nameDossierParentSel.
     * @param nameDossierParentSel nameDossierParentSel
     */
    public void setNameDossierParentSel(String nameDossierParentSel) {
        this.nameDossierParentSel = nameDossierParentSel;
    }

    /**
     * Accesseur nameDossierSel.
     * @return nameDossierSel
     */
    public String getNameDossierSel() {
        return nameDossierSel;
    }

    /**
     * Mutateur nameDossierSel.
     * @param nameDossierSel nameDossierSel
     */
    public void setNameDossierSel(String nameDossierSel) {
        this.nameDossierSel = nameDossierSel;
    }

    
    /**
     * Mutateur espaceStockageActive.
     * @param espaceStockageActive espaceStockageActive
     */
    public void setEspaceStockageActive(Boolean espaceStockageActive) {
        this.espaceStockageActive = espaceStockageActive;
    }
    
    
    /**
     * Accesseur isDossierModifiable.
     * @return isDossierModifiable 
     */
    public Boolean getIsDossierModifiable() {
        return isDossierModifiable;
    }

    /**
     * Mutateur isDossierModifiable.
     * @param isDossierModifiable isDossierModifiable
     */ 
    public void setIsDossierModifiable(Boolean isDossierModifiable) {
        this.isDossierModifiable = isDossierModifiable;
    }
    
    /**
     * Muttateur files. 
     * @param files liste des fichiers 
     */
    public void setFiles(ArrayList<FileUploadDTO> files) {
        this.files = files;
    }

    /**
     * Retourne la liste des fichiers contenus dans le dossier courant.
     * @return liste de fichiers FileUploadDTO
     */
    public ArrayList<FileUploadDTO> getFiles() {
        return this.files;
    }

    /**
     * Accesseur fichierSelectionne.
     * @return fichierSelectionne
     */
    public FileUploadDTO getFichierSelectionne() {
        return fichierSelectionne;
    }

    /**
     * Mutateur fichierSelectionne.
     * @param fichierSelectionne fichierSelectionne
     */
    public void setFichierSelectionne(FileUploadDTO fichierSelectionne) {
        this.fichierSelectionne = fichierSelectionne;
    }

    /**
     * Accesseur de vraixOuFauxAffichageStockage {@link #vraixOuFauxAffichageStockage}.
     * @return retourne vraixOuFauxAffichageStockage 
     */
    public Boolean getVraixOuFauxAffichageStockage() {
        return vraixOuFauxAffichageStockage;
    }

    /**
     * Mutateur de vraixOuFauxAffichageStockage {@link #vraixOuFauxAffichageStockage}.
     * @param vraixOuFauxAffichageStockage the vraixOuFauxAffichageStockage to set
     */
    public void setVraixOuFauxAffichageStockage(Boolean vraixOuFauxAffichageStockage) {
        this.vraixOuFauxAffichageStockage = vraixOuFauxAffichageStockage;
    }

    /**
     * Accesseur de nomFichierDrag {@link #nomFichierDrag}.
     * @return retourne nomFichierDrag
     */
    public String getNomFichierDrag() {
        return nomFichierDrag;
    }

    /**
     * Mutateur de nomFichierDrag {@link #nomFichierDrag}.
     * @param nomFichierDrag le nomFichierDrag to set
     */
    public void setNomFichierDrag(String nomFichierDrag) {
        this.nomFichierDrag = nomFichierDrag;
    }

    /**
     * Accesseur de nomDossierDrop {@link #nomDossierDrop}.
     * @return retourne nomDossierDrop
     */
    public String getNomDossierDrop() {
        return nomDossierDrop;
    }

    /**
     * Mutateur de nomDossierDrop {@link #nomDossierDrop}.
     * @param nomDossierDrop le nomDossierDrop to set
     */
    public void setNomDossierDrop(String nomDossierDrop) {
        this.nomDossierDrop = nomDossierDrop;
    }

    
}
