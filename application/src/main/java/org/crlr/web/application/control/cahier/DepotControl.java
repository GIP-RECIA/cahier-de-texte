/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.application.control.cahier;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.crlr.dto.Environnement;
import org.crlr.dto.UserDTO;
import org.crlr.dto.application.base.TypeRepertoireStockage;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.services.DepotService;
import org.crlr.web.application.control.AbstractControl;
import org.crlr.web.application.form.cahier.DepotForm;
import org.crlr.web.contexte.ContexteApplication;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.utils.FileSystemNode;
import org.crlr.web.utils.MessageUtils;
import org.richfaces.event.DropEvent;

/**
 * PopupPiecesJointesControl.
 *
 * @author dbreyton, jcarriere 
 */
@ManagedBean(name = "depot")
@ViewScoped
public class DepotControl extends AbstractControl<DepotForm> {
    
    /** depotService. */
    @ManagedProperty(value = "#{depotService}")
    protected transient DepotService depotService;
    
    
    /**
     * Mutateur depotService.
     *
     * @param depotService Le depotService à modifier
     */
    public void setDepotService(DepotService depotService) {
        this.depotService = depotService;
    }  
    
    /**
     * Instantiates a new popupPiecesJointes.
     */
    public DepotControl() {
        super(new DepotForm());
    }

    /**
     *
     */
    public void init() {
    }

    /**
     * Reset.
     */
    public void reset() {
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    public void onLoad() {

       
        final String path =
            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                                          .getContext()).getRealPath("/");

        final String uid =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO()
                         .getUid();

        final File dir = new File(path + TypeRepertoireStockage.UPLOADFILE.getId() + java.io.File.separator + 
                uid + java.io.File.separator + TypeRepertoireStockage.MESDOCUMENTS.getId());

        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        final ContexteApplication contexteApplication = ContexteUtils.getContexteApplication();
        final Environnement environnement = contexteApplication.getEnvironnement();
        form.setVraixOuFauxAffichageStockage(environnement.equals(Environnement.CRLR));
        
        //path web / et non système java.io.File.separator
        form.setPathDepotCahier("/"+ TypeRepertoireStockage.UPLOADFILE.getId() + "/" + uid);
        form.setPathRepertoireEnCoursCahier("/" + TypeRepertoireStockage.UPLOADFILE.getId() + 
                "/" + uid + "/" + TypeRepertoireStockage.MESDOCUMENTS.getId());
        
        createRootsDepotCahier();
        selectionnerRepertoireCahier();
        

        /**
         * Verifie si l'espace de stockage a été activé ou non.
         */
        final UserDTO userDTO =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO();
        
        if (!StringUtils.isEmpty(userDTO.getDepotStockage())) {
            form.setEspaceStockageActive(true);
        } else {
            form.setEspaceStockageActive(false);
        }    
       
    }
    
    /**
     *
     */
    public void createRootsDepotCahier() {
        final List<FileSystemNode> srcRoots =
            new FileSystemNode(false,form.getPathDepotCahier()).getNodes();      
        
        
        form.setSourceRootsDepotCahier(srcRoots);
    }

    /**
     * Retourne la liste des fichiers contenus dans le dossier courant.
     */
    private void listerFiles() {
        final ArrayList<FileUploadDTO> files = new ArrayList<FileUploadDTO>();
        
        final String pathDownload =
            ((ServletContext) FacesContext.getCurrentInstance()
                                          .getExternalContext().getContext()).getContextPath() + "/";
        
        final List<FileSystemNode> nodes = this.form.getSourceRootsDepotCahierFiles();

        final String uid =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO()
                         .getUid();
        
        for (FileSystemNode node : nodes) {
            FileUploadDTO file;
            try {
                String dossier = node.getPath().replaceFirst(form.getPathDepotCahier() + "/", "");
                dossier = "/" + dossier.substring(0,dossier.length() - node.getName().length() - 1);
            
                file = depotService.findFile(uid, dossier, node.getName());
                if (file == null) {
                    file = new FileUploadDTO();
                    file.setNom(node.getName());
                    file.setEnBase(Boolean.FALSE);
                }
                file.setPathFullDownload(pathDownload + node.getPath());
                file.setActiverLien(true);
                
            } catch (Exception e) {
                file = new FileUploadDTO();
                file.setNom(node.getName());
                file.setEnBase(Boolean.TRUE);
            }
            files.add(file);
        }
        form.setFiles(files);
    }

    

    /**
     * Selectionne le dossier courant.
     * @param nomDossierSelectionne nom du dossier à selectionner  ex:"/uploadFile/kbj0027f/Mes Documents/papa"
     */
    private void selectionnerDossier(final String nomDossierSelectionne) {
        final List<FileSystemNode> srcRoots =
            new FileSystemNode(false,nomDossierSelectionne).getFilesOfNodes();
        form.setSourceRootsDepotCahierFiles(srcRoots);     
        
        form.setPathRepertoireEnCoursCahier(nomDossierSelectionne);
        form.setNameRepSel(form.getPathRepertoireEnCoursCahier().replaceFirst(form.getPathDepotCahier() + "/", ""));
        
        final List<String> listeDossier = Arrays.asList(StringUtils.split(form.getNameRepSel(), "/"));
        String dossierParent = "";
        final Integer nbDossier =  listeDossier.size();
        final String sousDossier = listeDossier.get(nbDossier -1); 
        for (int i = 0; i < nbDossier -1; i++) {
            if (!StringUtils.isEmpty(dossierParent)) {
                dossierParent += "/";
            }
             dossierParent += listeDossier.get(i);   
        } 
        if (StringUtils.isEmpty(dossierParent)) {
            form.setNameDossierSel("");
            form.setNameDossierParentSel(sousDossier);
            form.setIsDossierModifiable(Boolean.FALSE);
        } else { 
            form.setNameDossierSel(sousDossier);
            form.setNameDossierParentSel(dossierParent);
            
            final String path =
                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                                              .getContext()).getRealPath("/");
            final String dossiserAbsolu = path + form.getPathRepertoireEnCoursCahier();
            
            if (existeFichierUsedDansDossier(dossiserAbsolu)) {
                form.setIsDossierModifiable(Boolean.FALSE);
            } else {
                form.setIsDossierModifiable(Boolean.TRUE);
            }
        }
        
        // Parcours la liste des fichiers du dossier pour setter le files 
        listerFiles();
    }
    
    /**
     *
     */
    public synchronized void selectionnerRepertoireCahier() {
        selectionnerDossier(form.getPathRepertoireEnCoursCahier());
    }
    
    
    /**
     * Recharge l'arbre complet.
     */
    public void reloadTree() {
        createRootsDepotCahier();
    }
    
    /**
     * Créer un nouveau dossier dans le dossier en cours. Le nom du dossier créé est "Nouveau" par défaut.
     */
    public void nouveauDossier() {
        final String path =
            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                                          .getContext()).getRealPath("/");

        final String masqueNouveau = "Nouveau dossier";
        String nomNouveauDossiser = masqueNouveau;
        File dir = new File(path + form.getPathRepertoireEnCoursCahier() +  java.io.File.separator + nomNouveauDossiser);
        Integer i = 1;
        while (dir.exists()) {
            nomNouveauDossiser = masqueNouveau + " " + i.toString();
            dir = new File(path + form.getPathRepertoireEnCoursCahier() +  java.io.File.separator + nomNouveauDossiser);
            i++;
        }
        if (!dir.exists()) {
            dir.mkdirs();
            selectionnerDossier(form.getPathRepertoireEnCoursCahier() +"/" + nomNouveauDossiser);
            reloadTree();
        }        
    }
    
    /**
     * Vérifie si le fichier passé en argument est utilisé dans les pièces jointes de cahier de texte.
     * @param nomDossier nom du dossier contenant le fichier à tester
     * @param nomFichier nom du fichier à tester
     * @return TRUE ou FALSE selon que le fichier est utilisé dans les pièces jointes ou non
     * @throws MetierException  MetierException
     */
    private Boolean isFichierUsed(final String nomDossier, final String nomFichier) throws MetierException {
        
        final String uid =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO()
                         .getUid();

        final Boolean existe = depotService.fileIsUsed(uid, nomDossier, nomFichier);
        
        return existe;
    }
    
    /** 
     * Verifie si le fichier dont le nom complet passé en paramètre est utilisé dans les pièces jointes.
     * @param nomFichier nom du fichier complet ex: "/uploadFile/kbj0027f/Mes Documents/fichier.doc"  
     * @return TRUE ou FALSE selon que le fichier est utilisé dans les pièces jointes
     */
    public Boolean isFichierFullPathUsed(final String nomFichier) {
        return Boolean.TRUE;
    }
    
    /**
     * Vérifie si l'arborescence contient un fichier utilisé dans les pièces jointes ou non.
     * @param nomDossier nom du dossier complet à parcourir en profondeur 
     * @return TRUE ou FALSE si un fichier utilisé dans pièces jointes à été trouvé dans un 
     * des dossiers de l'arborescence
     */
    private Boolean existeFichierUsedDansDossier(final String nomDossier) {

        final String path =
            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                                          .getContext()).getRealPath("/");
        final File dir = new File(nomDossier);
        if (!dir.exists()) { return Boolean.FALSE; }
        
        final String[] liste = dir.list();  
        
        for (final String nomFichier : liste) {
            final File fichier = new File(nomDossier + java.io.File.separator + nomFichier);
            
            // C'est un fichier, on vérifie s'il est utilisé
            if (fichier.isFile()) {
                try {
                     final String nomDossierBase = "/" + nomDossier.replace(java.io.File.separator,"/")
                                                   .replaceFirst(path.replace(java.io.File.separator,"/"), "")
                                                   .replaceFirst(form.getPathDepotCahier() + "/", "");
                    
                    if (isFichierUsed(nomDossierBase,nomFichier)) { return Boolean.TRUE; }
                    
                } catch (Exception e) {
                    // Il y a eu une erreur on bloque tout
                    return Boolean.TRUE;
                } 
            // C'est un dossier, on vérifie s'il contient un fichier utilisé
            } else {
                if (existeFichierUsedDansDossier(nomDossier + java.io.File.separator + nomFichier)) {
                    return Boolean.TRUE;
                }
            }
        }
        // On n'a rien trouvé, on renvoie FALSE
        return Boolean.FALSE;
    }

   
    /**
     * Retourne l'objet fileuploadDTO correspondant au fichier passé en paramètre.
     * @param dossier nom complet du dossier contenant le fichier 
     * ex: "D:/apache-tomcat-6.0.18/webapps/cahier-texte/uploadFile/kbj0027f/Mes Documents/Nouveau dossier"
     * @param nomFichier nom du fichier
     * @return FileuploadDTO completé eventuellement des infos lues en base 
     */
    private FileUploadDTO obtenirFileUploadDTO(final String dossier, final String nomFichier) {
        
        final String pathDownload =
            ((ServletContext) FacesContext.getCurrentInstance()
                                          .getExternalContext().getContext()).getContextPath() + "/";
        //=>  "/cahier-texte/"
        
        final String path =
            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                                          .getContext()).getRealPath("/");
        //=> "D:\apache-tomcat-6.0.18\webapps\cahier-texte\"
        
        final String pathDepotCahier = form.getPathDepotCahier();
        //=> "/uploadFile/kbj0027f"

        final String nomDossierCourt = dossier.substring(path.length() + pathDepotCahier.length());
        //=> "/Mes documents/Chris" 
        
        final String uid =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO()
                         .getUid();
        FileUploadDTO fileUploadDTO;
        
        try {
            fileUploadDTO = depotService.findFile(uid, nomDossierCourt, nomFichier);
            if (fileUploadDTO == null) {
                fileUploadDTO = new FileUploadDTO();
                fileUploadDTO.setNom(nomFichier);
                fileUploadDTO.setEnBase(Boolean.FALSE);
            }
            fileUploadDTO.setPathFullDownload(pathDownload + pathDepotCahier + nomDossierCourt + "/" + nomFichier);
            fileUploadDTO.setActiverLien(true);
            
        } catch (Exception e) {
            fileUploadDTO = new FileUploadDTO();
            fileUploadDTO.setNom(nomFichier);
            fileUploadDTO.setEnBase(Boolean.TRUE);
        }
        return fileUploadDTO;
    }
    
    /**
     * Supprime recursiement toute une arbrorescence de fichier.
     * @param nomDossier nom du dossier complet à supprimer en profondeur avec tout ce qu'il contient 
     * @return TRUE ou FALSE si pas d'erreur rencontrée 
     */
    private Boolean supprimerArborescence(final String nomDossier) {
        
        final File dir = new File(nomDossier);
        if (!dir.exists()) { return Boolean.FALSE; }
        
        final String[] liste = dir.list();  
        
        for (final String nomFichier : liste) {
            
            final File fichier = new File(nomDossier + java.io.File.separator + nomFichier);
            
            // C'est un fichier, on le supprime
            if (fichier.isFile()) {
                
                final FileUploadDTO fichierDTO = obtenirFileUploadDTO(nomDossier, nomFichier);
                if (!supprimerUnFichier(nomDossier, fichierDTO)) { 
                    return Boolean.FALSE; 
                }
                
            // C'est un dossier, on vérifie supprime ce qu'il contient et lui même
            } else {
                if (!supprimerArborescence(nomDossier + java.io.File.separator + nomFichier)) { return Boolean.FALSE; }
            }
        }
        // Le dossier ne doit plus rien contenir, on peut le supprimer
        if (!dir.delete()) { return Boolean.FALSE; }
        
        // il n'y a pas du avoir d'erreur
        return Boolean.TRUE;
    }
    
    /**
     * Supprime le doussier en cours. 
     */
    public void deleteDossier() {
        final String path =
            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                                          .getContext()).getRealPath("/");

        final String dossiserASupprimer = path + form.getPathRepertoireEnCoursCahier();
        final File dir = new File(dossiserASupprimer);
        if (dir.exists()) {
            if (!existeFichierUsedDansDossier(dossiserASupprimer)) {
                if (supprimerArborescence(dossiserASupprimer)) { 
            
                    // Se positionne sur le parent
                    selectionnerDossier(form.getPathDepotCahier() + "/" +
                            form.getNameDossierParentSel());
                    
                    reloadTree();
                }
            }
        }        
    }
    
    /**
     * Renome le dossier en cours par le nom du dossier saisi.
     */
    public void renomerDossier() {

        if (StringUtils.isEmpty(form.getNameDossierSel())) {
            MessageUtils.addMessage(new Message("Veuillez saisir un nom de dossier.", Nature.BLOQUANT),getClass());
            return;
        }
        
        final String path =
            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                                          .getContext()).getRealPath("/");

        final String dossiserAModifier = path + form.getPathRepertoireEnCoursCahier();
        final String nouveauNomDossier = path + form.getPathDepotCahier() + java.io.File.separator + 
                                         form.getNameDossierParentSel() + java.io.File.separator +  
                                         form.getNameDossierSel();

        final File dir = new File(dossiserAModifier);
        final File nouveau = new File(nouveauNomDossier);
        if (dir.exists()) {
            if (nouveau.exists()) {
                MessageUtils.addMessage(new Message(
                        "Un dossier existe déjà avec ce même nom.",
                        Nature.BLOQUANT), getClass());
            } else { 
                if (!existeFichierUsedDansDossier(dossiserAModifier)) {
                    if (dir.renameTo(nouveau)) {
                        // maj du nom du dossier modifié
                        selectionnerDossier(form.getPathDepotCahier() + "/" + 
                                                     form.getNameDossierParentSel() + "/" +  
                                                     form.getNameDossierSel());
                        this.reloadTree();
                    } else {
                        MessageUtils.addMessage(new Message(
                                "Une erreur est survenue lors du renommage du dossier.",
                                Nature.BLOQUANT), getClass());
                    }
                } else {
                    MessageUtils.addMessage(new Message(
                            "Impossible de renommer ce dossier. Il contient des fichiers référencés par des pièces jointes.",
                            Nature.BLOQUANT), getClass());

                }
            }
        }
    }
    
    /**
     * Supprime le fichier du dossier spécifié.
     * @param dossier : nom du dossier complet contenant le fichier 
     * ex :"D:/apache-tomcat-6.0.18/webapps/cahier-texte/uploadFile/kbj0027f/Mes Documents/Chris"
     * @param  fichierASupprimer : fichier à supprimer
     * @return true ou false selon que la suppression s'est correctement déroulée.
     */
    private boolean supprimerUnFichier(final String dossier, final FileUploadDTO fichierASupprimer) {
        final File fichier = new File( 
                dossier + java.io.File.separator +  
                fichierASupprimer.getNom());
        
        if (fichier.exists()) {
            
            try {
                // Supprime le fichier physisquement et en base
                if (this.depotService.deleteFile(fichierASupprimer, fichier)) {
                    return true;
                } else {
                    MessageUtils.addMessage(new Message(
                            "Une erreur est survenue lors de la suppression du fichier.",
                            Nature.BLOQUANT), getClass());
                }
            } catch (Exception e) {
                MessageUtils.addMessage(new Message(
                        "Une exception est survenue lors de la suppression du fichier.",
                        Nature.BLOQUANT), getClass());
            }
        }
        return false;
    }

    /**
     * Supprime le fichier qui a été selectionné.
     */
    public void supprimerFichier() {
        
        final String path =
            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                                          .getContext()).getRealPath("/");
        //=> "D:\apache-tomcat-6.0.18\webapps\cahier-texte\"
        
        final String dossier = 
            path + java.io.File.separator + 
            form.getPathRepertoireEnCoursCahier();
        
        
        if (supprimerUnFichier(dossier, form.getFichierSelectionne())) {
            // Raffraichit le contenu du dossier
            selectionnerRepertoireCahier();
        }
    }
    
    /**
    * Gestion du Drop de fichiers dans l'arbre des dossiers.
    * @param dropEvent evenement 
    */
   public void dropListener(DropEvent dropEvent) {
       
       final String dossierDes = (String) dropEvent.getDropValue();
       final FileUploadDTO fileUploadDTO = (FileUploadDTO) dropEvent.getDragValue(); 
       
       if (dossierDes != null && fileUploadDTO != null) {
           
           final String path =
               ((ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                                             .getContext()).getRealPath("/");
           
           final String nomSrc = 
               path + java.io.File.separator + 
               form.getPathRepertoireEnCoursCahier() + java.io.File.separator + 
               fileUploadDTO.getNom(); 
           
           final String nomDes = 
               path + java.io.File.separator + 
               dossierDes + java.io.File.separator + 
               fileUploadDTO.getNom();  

           final File fichierSrc = new File(nomSrc);
           if (fichierSrc.exists()) {
               final File fichierDes = new File(nomDes);
               if (!fichierDes.exists()) {
                   if (fichierSrc.renameTo(fichierDes)) {
                       
                       // Deplacement du chemin correspondant dans la base de données (si déjà en base)
                       if (fileUploadDTO.getEnBase()) {
                           if (!depotService.moveFile(fileUploadDTO, dossierDes.replaceFirst(form.getPathDepotCahier(), ""))) { 
                           
                               // Le déplacement coté base de données a échoué, on remet en place coté file system
                               fichierDes.renameTo(fichierSrc);
                               MessageUtils.addMessage(new Message(
                                       "Une erreur est survenue lors de la mise à jour de l'emplacement du fichier en base de données.",
                                       Nature.BLOQUANT), getClass());
                               return;
                           }
                       }
                       
                       // Retire le fichier de la liste
                       final ArrayList<FileUploadDTO> fichiers = form.getFiles();
                       for (final FileUploadDTO ficARemove : fichiers) {
                           if (ficARemove.getNom().equalsIgnoreCase(fileUploadDTO.getNom())) {
                               fichiers.remove(ficARemove);
                               form.setFiles(fichiers);
                               break;
                           }
                       }
                   } else {
                       MessageUtils.addMessage(new Message(
                               "Une erreur est survenue lors du déplacement du fichier.", Nature.BLOQUANT), getClass());
                       return;
                   }
                   
               } else {
                   MessageUtils.addMessage(new Message(
                           "Le dossier de destination contient déjà un fichier du même nom.", Nature.BLOQUANT), getClass());
                   return;
               }
           }
       }
   }
   
   /**
    * Recherche dans la liste files de FileupLoadDTO l'element correspondant au nom passe en parametre.
    * @param nomFichier le nom recherche
    * @return un FileUploadDTO ou null.
    */
   private FileUploadDTO trouverFichier(final String nomFichier) {
       if (nomFichier == null || nomFichier.isEmpty()) { 
           return null;
       }
       for (final FileUploadDTO fichier : form.getFiles()) {
           if (nomFichier.equals(fichier.getNom())) {
               return fichier;
           }
       }
       return null;
   }

   /** 
    * Recherche un dossier dans l'arborescence qui correspond au nom complet fourni.
    * @param nomDossier le nom complet du dossier recheche.
    * @return le nom correspondant au dossier.
    */
   private FileSystemNode trouverDossier(final String nomDossier) {
       if (nomDossier == null || nomDossier.isEmpty()) {
           return null;
       }
       for (final FileSystemNode fils : form.getSourceRootsDepotCahier()) {
           final FileSystemNode result = trouverDossier(nomDossier, fils); 
           if (result != null) {
               return result;
           }
       }
       return null;
   }
   
   /** 
    * Recherche un dossier dans l'arborescence qui correspond au nom complet fourni.
    * @param nomDossier le nom complet du dossier recheche.
    * @param parent le noeud parent a partir duquel est faite la recherche.
    * @return le nom correspondant au dossier.
    */
   private FileSystemNode trouverDossier(final String nomDossier, final FileSystemNode parent) {
       if (nomDossier == null || nomDossier.isEmpty()) {
           return null;
       }
       if (nomDossier.equals(parent.getPath())) {
           return parent;
       }
       for (final FileSystemNode fils : parent.getNodes()) {
           final FileSystemNode result = trouverDossier(nomDossier, fils);
           if (result != null) {
               return result;
           }
       }
       return null;
   }
   
   
   /**
    * Methode invoquee en javascript/jQuery.
    */
   public void deplacerFichier() {
       final FileUploadDTO fichierDeplace = trouverFichier(form.getNomFichierDrag());
       final FileSystemNode dossier = trouverDossier(form.getNomDossierDrop());
       final String nomDossierDes = form.getNomDossierDrop().substring(form.getPathDepotCahier().length());
       
       if (dossier != null && fichierDeplace != null) {
           
           try {           
               // On deplace l'emplacement physique du fichier dans le nouveau dossier
               final String path = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/");
               
               final File fichierSrc = new File( 
                       path + java.io.File.separator + 
                       form.getPathRepertoireEnCoursCahier() + java.io.File.separator + 
                       form.getNomFichierDrag());
    
               final String nomDes = path + java.io.File.separator + form.getNomDossierDrop() + java.io.File.separator + fichierDeplace.getNom();
               File fichierDes = new File(nomDes);
               
               // Verifie que le fichier existe pas et que la source existe bien
               if (!fichierDes.exists()) {
                   if ( fichierSrc.exists()) {
                       
                       fichierSrc.renameTo(fichierDes);
                       depotService.moveFile(fichierDeplace, nomDossierDes);
                       
                       // On rafraichit le dossier selectionne
                       selectionnerRepertoireCahier();
                   // Le fichier source n'existe pas
                   } else {
                       MessageUtils.addMessage(
                               new Message("Le fichier a déplacé n'a pas été trouvé sur le serveur.", Nature.BLOQUANT), getClass());
                   }
               // Le fichier existe deja dans ce dossier, on peut pas faire le deplacement
               } else {
                   MessageUtils.addMessage(
                           new Message("Un fichier portant le même nom est présent sur le dossier cible.", Nature.BLOQUANT), getClass());
               }
           } catch (Exception e) { 
               MessageUtils.addMessage(new Message(  "Une erreur est survenue lors du déplacement du fichier.", Nature.BLOQUANT), getClass());
           }
       }
           
   }
   
   
   
}
