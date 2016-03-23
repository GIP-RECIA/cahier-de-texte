/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.application.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.crlr.dto.Outil;
import org.crlr.dto.application.base.FichierStockageDTO;
import org.crlr.web.application.control.AbstractFichierTreeNode;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.dto.TypeOngletPopup;
import org.crlr.web.utils.FileSystemNode;
import org.richfaces.model.TreeNode;

/**
 * Popup pièces jointes.
 *
 * @author j.carriere
 */
public class PopupPiecesJointesForm extends AbstractForm {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2935299928004717958L;
    
        
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private String pathDepotCahier;

    /** Nom du dossier dans lequel un fichier a été déposé depuis l'espace de stockage. */ 
    private String pathCahierRelatif;
    
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private String pathRepertoireEnCoursCahier;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private List<FileSystemNode> sourceRootsDepotCahier;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private List<FileSystemNode> sourceRootsDepotCahierFiles;

    
    
    /** DOCUMENTATION INCOMPLETE! */
    private Integer uploadsAvailable;
    
    private String ongletSelectionne;
    
    private String nameRepSel;
    
    private Outil outilAppelant;
    
    private TreeNode rootNode;

    /**
     * Indique si l'espace de stockage a été activé pour l'user connecté.
     */
    private Boolean espaceStockageActive;

    /** Indique si le dossier sélectionné est modifiable ou non. */
    private Boolean isDossierModifiable;
    
    /** Nom du dossier parent hors root contenant le dossier sélectionné : "Mes documents". */
    private String nameDossierParentSel;
    
    /** Nom du sous dossier sélectionné hors root et dossier parent : "Chris". */
    private String nameDossierSel;
    
    /** Liste des fichiers contenus dans le dossier selectionnés.  */
    private ArrayList<FileUploadDTO> files;
    
    /** Les fichiers contenus dans le CTN dossier sélectionnés. */
    private List<FileUploadDTO> ctnFichiers;
    
    private FileUploadDTO ctnFichierSelectionne;
    
    /** Objet qui a été sélectionné depuis le CTN. */  
    private FileUploadDTO fileUploade;
    
    //////////////////////////////////////////////////////////////////////////////////////////
    /// Stockage 
    //////////////////////////////////////////////////////////////////////////////////////////
    /** fichier de stockage selectionnee. */
    private AbstractFichierTreeNode<FichierStockageDTO> nodeStockageSelected;

    /** key du fichier de stockage selectionnee. */
    private String nodeStockageSelectedKey;
    
    
    //////////////////////////////////////////////////////////////////////////////////////////
    /// CMIS 
    //////////////////////////////////////////////////////////////////////////////////////////
    
    /** fichier de stockage selectionnee. */
    private AbstractFichierTreeNode<CmisObject> nodeCmisSelected;

    /** key du fichier de stockage selectionnee. */
    private String nodeCmisSelectedKey;
    
    
    /**
     * Constructeur.
     */
    public PopupPiecesJointesForm() {
        ongletSelectionne = TypeOngletPopup.DEPOT.name();
        uploadsAvailable = 99;     
        nameRepSel = null;
        outilAppelant = null;
        espaceStockageActive = false;
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
     * Accesseur ongletSelectionne.
     * @return le ongletSelectionne.
     */
    public String getOngletSelectionne() {
        return ongletSelectionne;
    }

    /**
     * Mutateur ongletSelectionne.
     * @param ongletSelectionne le ongletSelectionne à modifier.
     */
    public void setOngletSelectionne(String ongletSelectionne) {
        this.ongletSelectionne = ongletSelectionne;
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
     * Accesseur outilAppelant.
     * @return le outilAppelant.
     */
    public Outil getOutilAppelant() {
        return outilAppelant;
    }

    /**
     * Mutateur outilAppelant.
     * @param outilAppelant le outilAppelant à modifier.
     */
    public void setOutilAppelant(Outil outilAppelant) {
        this.outilAppelant = outilAppelant;
    }

    /**
     * Accesseur espaceStockageActive.
     * @return espaceStockageActive
     */
    public Boolean getEspaceStockageActive() {
        return espaceStockageActive;
    }

    /**
     * Mutateur espaceStockageActive.
     * @param espaceStockageActive espaceStockageActive
     */
    public void setEspaceStockageActive(Boolean espaceStockageActive) {
        this.espaceStockageActive = espaceStockageActive;
    }

    /**
     * Accesseur rootNode.
     * @return le rootNode.
     */
    public TreeNode getRootNode() {
        return rootNode;
    }

    /**
     * Mutateur rootNode.
     * @param rootNode le rootNode à modifier.
     */
    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * Accesseur de pathCahierRelatif {@link #pathCahierRelatif}.
     * @return retourne pathCahierRelatif 
     */
    public String getPathCahierRelatif() {
        return pathCahierRelatif;
    }

    /**
     * Mutateur de pathCahierRelatif {@link #pathCahierRelatif}.
     * @param pathCahierRelatif the pathCahierRelatif to set
     */
    public void setPathCahierRelatif(String pathCahierRelatif) {
        this.pathCahierRelatif = pathCahierRelatif;
    }

  

    /**
     * Accesseur de isDossierModifiable {@link #isDossierModifiable}.
     * @return retourne isDossierModifiable 
     */
    public Boolean getIsDossierModifiable() {
        return isDossierModifiable;
    }

    /**
     * Mutateur de isDossierModifiable {@link #isDossierModifiable}.
     * @param isDossierModifiable the isDossierModifiable to set
     */
    public void setIsDossierModifiable(Boolean isDossierModifiable) {
        this.isDossierModifiable = isDossierModifiable;
    }

    /**
     * Accesseur de nameDossierParentSel {@link #nameDossierParentSel}.
     * @return retourne nameDossierParentSel 
     */
    public String getNameDossierParentSel() {
        return nameDossierParentSel;
    }

    /**
     * Mutateur de nameDossierParentSel {@link #nameDossierParentSel}.
     * @param nameDossierParentSel the nameDossierParentSel to set
     */
    public void setNameDossierParentSel(String nameDossierParentSel) {
        this.nameDossierParentSel = nameDossierParentSel;
    }

    /**
     * Accesseur de nameDossierSel {@link #nameDossierSel}.
     * @return retourne nameDossierSel 
     */
    public String getNameDossierSel() {
        return nameDossierSel;
    }

    /**
     * Mutateur de nameDossierSel {@link #nameDossierSel}.
     * @param nameDossierSel the nameDossierSel to set
     */
    public void setNameDossierSel(String nameDossierSel) {
        this.nameDossierSel = nameDossierSel;
    }

    /**
     * Accesseur de files {@link #files}.
     * @return retourne files 
     */
    public ArrayList<FileUploadDTO> getFiles() {
        return files;
    }

    /**
     * Mutateur de files {@link #files}.
     * @param files the files to set
     */
    public void setFiles(ArrayList<FileUploadDTO> files) {
        this.files = files;
    }

    /**
     * Accesseur de fileUploade {@link #fileUploade}.
     * @return retourne fileUploade
     */
    public FileUploadDTO getFileUploade() {
        return fileUploade;
    }

    /**
     * Mutateur de fileUploade {@link #fileUploade}.
     * @param fileUploade le fileUploade to set
     */
    public void setFileUploade(FileUploadDTO fileUploade) {
        this.fileUploade = fileUploade;
    }

    /**
     * Accesseur de nodeStockageSelected {@link #nodeStockageSelected}.
     * @return retourne nodeStockageSelected
     */
    public AbstractFichierTreeNode<FichierStockageDTO> getNodeStockageSelected() {
        return nodeStockageSelected;
    }

    /**
     * Mutateur de nodeStockageSelected {@link #nodeStockageSelected}.
     * @param nodeStockageSelected le nodeStockageSelected to set
     */
    public void setNodeStockageSelected(AbstractFichierTreeNode<FichierStockageDTO> nodeStockageSelected) {
        this.nodeStockageSelected = nodeStockageSelected;
    }

    /**
     * Accesseur de nodeStockageSelectedKey {@link #nodeStockageSelectedKey}.
     * @return retourne nodeStockageSelectedKey
     */
    public String getNodeStockageSelectedKey() {
        return nodeStockageSelectedKey;
    }

    /**
     * Mutateur de nodeStockageSelectedKey {@link #nodeStockageSelectedKey}.
     * @param nodeStockageSelectedKey le nodeStockageSelectedKey to set
     */
    public void setNodeStockageSelectedKey(String nodeStockageSelectedKey) {
        this.nodeStockageSelectedKey = nodeStockageSelectedKey;
    }


    /**
     * @return the ctnFichiers
     */
    public List<FileUploadDTO> getCtnFichiers() {
        return ctnFichiers;
    }

    /**
     * @param ctnFichiers the ctnFichiers to set
     */
    public void setCtnFichiers(List<FileUploadDTO> ctnFichiers) {
        this.ctnFichiers = ctnFichiers;
    }

    /**
     * @return the ctnFichierSelectionne
     */
    public FileUploadDTO getCtnFichierSelectionne() {
        return ctnFichierSelectionne;
    }

    /**
     * @param ctnFichierSelectionne the ctnFichierSelectionne to set
     */
    public void setCtnFichierSelectionne(FileUploadDTO ctnFichierSelectionne) {
        this.ctnFichierSelectionne = ctnFichierSelectionne;
    }

    

    /**
     * @return the nodeCmisSelectedKey
     */
    public String getNodeCmisSelectedKey() {
        return nodeCmisSelectedKey;
    }

    /**
     * @param nodeCmisSelectedKey the nodeCmisSelectedKey to set
     */
    public void setNodeCmisSelectedKey(String nodeCmisSelectedKey) {
        this.nodeCmisSelectedKey = nodeCmisSelectedKey;
    }

    /**
     * @return the nodeCmisSelected
     */
    public AbstractFichierTreeNode<CmisObject> getNodeCmisSelected() {
        return nodeCmisSelected;
    }

    /**
     * @param nodeCmisSelected the nodeCmisSelected to set
     */
    public void setNodeCmisSelected(AbstractFichierTreeNode<CmisObject> nodeCmisSelected) {
        this.nodeCmisSelected = nodeCmisSelected;
    }

 
    
}
