/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.application.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.dto.Environnement;
import org.crlr.dto.Outil;
import org.crlr.dto.UserDTO;
import org.crlr.dto.application.base.FichierStockageDTO;
import org.crlr.dto.application.base.TypeReglesPieceJointe;
import org.crlr.dto.application.base.TypeRepertoireStockage;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.services.DepotService;
import org.crlr.utils.DateUtils;
import org.crlr.web.application.form.PopupPiecesJointesForm;
import org.crlr.web.contexte.ContexteApplication;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.dto.WebDavQO;
import org.crlr.web.utils.CmisUtils;
import org.crlr.web.utils.FacesUtils;
import org.crlr.web.utils.FileSystemNode;
import org.crlr.web.utils.MessageUtils;
import org.crlr.web.utils.WebDavUtils;
import org.richfaces.event.DropEvent;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.event.ItemChangeEvent;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;
import org.richfaces.model.UploadedFile;

/**
 * PopupPiecesJointesControl.
 * 
 * @author dbreyton, jcarriere
 */
@ManagedBean(name = "popupPiecesJointes")
@ViewScoped
public class PopupPiecesJointesControl extends
        AbstractControl<PopupPiecesJointesForm> {

    /** depotService. */
    @ManagedProperty(value = "#{depotService}")
    protected transient DepotService depotService;

    /**
     * Mutateur de depotService {@link #depotService}.
     * 
     * @param depotService
     *            the depotService to set
     */
    public void setDepotService(DepotService depotService) {
        this.depotService = depotService;
    }

    /** DOCUMENTATION INCOMPLETE! */
    private boolean autoUpload;

    /** DOCUMENTATION INCOMPLETE! */
    private boolean uploadCompleted;
    
    protected Session cmisSession;

    /**
     * Instantiates a new popupPiecesJointes.
     */
    public PopupPiecesJointesControl() {
        super(new PopupPiecesJointesForm());
        // Variable pour l'upload
        autoUpload = true;
        uploadCompleted = false;
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
        log.info("PopupPieceJointesControl charger!");

        final String path = ((ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext()).getRealPath("/");

        final UtilisateurDTO utilisateur = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();
        final String uid = utilisateur.getUserDTO().getUid();
        final ContexteApplication contexteApplication = ContexteUtils
                .getContexteApplication();
        final Environnement environnement = contexteApplication
                .getEnvironnement();

        final File dir = new File(path
                + TypeRepertoireStockage.UPLOADFILE.getId()
                + java.io.File.separator + uid + java.io.File.separator
                + TypeRepertoireStockage.MESDOCUMENTS.getId());

        if (!dir.exists()) {
            dir.mkdirs();
        }
        // path web / et non système java.io.File.separator
        form.setPathDepotCahier("/" + TypeRepertoireStockage.UPLOADFILE.getId()
                + "/" + uid);
        form.setPathRepertoireEnCoursCahier("/"
                + TypeRepertoireStockage.UPLOADFILE.getId() + "/" + uid + "/"
                + TypeRepertoireStockage.MESDOCUMENTS.getId());

        log.info(
                "Dépôt path cahier: {0}, path répertoire en cours cahier: {1}",
                form.getPathDepotCahier(),
                form.getPathRepertoireEnCoursCahier());

        createRootsDepotCahier();
        selectionnerRepertoireCahier();

        /**
         * Verifie si l'espace de stockage a été activé ou non.
         */
        final UserDTO userDTO = ContexteUtils.getContexteUtilisateur()
                .getUtilisateurDTO().getUserDTO();

        // Stockage seulement pour CRLR
        if (environnement.equals(Environnement.CRLR)
                && !StringUtils.isEmpty(userDTO.getDepotStockage())) {
            form.setEspaceStockageActive(true);
        } else {
            form.setEspaceStockageActive(false);
        }

        final Outil outilAppelant = (Outil) ContexteUtils
                .getContexteOutilControl().recupererEtSupprimerObjet(
                        PopupPiecesJointesControl.class.getName());
        if (outilAppelant != null) {
            form.setOutilAppelant(outilAppelant);
        }
    }

    /**
     * Listener du changement d'onglet.
     * 
     * @param valueChangeEvent
     *            l'événement de changement.
     */
    public void navigationOnlget(ItemChangeEvent valueChangeEvent) {
        final String tab = (String) valueChangeEvent.getNewItemName();
        form.setOngletSelectionne(tab);
        form.setPathRepertoireEnCoursCahier(form.getPathDepotCahier() + "/"
                + TypeRepertoireStockage.MESDOCUMENTS.getId());
        createRootsDepotCahier();
        selectionnerRepertoireCahier();
        
        if (tab!=null && tab.equals("MON_STOCKAGE")) {
            loadTreeStockage(); 
        }
    }

    /**
     *
     */
    public synchronized void createRootsDepotCahier() {
        final List<FileSystemNode> srcRoots = new FileSystemNode(false,form
                .getPathDepotCahier()).getNodes();
        form.setSourceRootsDepotCahier(srcRoots);
    }

    /**
     *
     */
    public void selectionnerRepertoireCahier() {
        log.info("selectionnerRepertoireCahier.  form.getPathRepertoireEnCoursCahier() = {0}", form.getPathRepertoireEnCoursCahier());
        selectionnerDossier(form.getPathRepertoireEnCoursCahier());
    }

    /**
     * 
     * 
     * @param dropEvent
     *            DOCUMENTATION INCOMPLETE!
     */
    public void dropListener(DropEvent dropEvent) {
        form.setPathCahierRelatif((String) dropEvent.getDropValue());
    }
    
    
    /**
     * @param pathCahierRelatif le chemin relatif
     * @return le chemin complèt de CTN (dans le même système de fichiers du serveur cahier de texte)
     */
    private File getCTNCheminComplet(String pathCahierRelatif) {
        final String path = ((ServletContext) FacesContext
                .getCurrentInstance().getExternalContext().getContext())
                .getRealPath("/");
        
        File baseDir = new File(path);
        
        File cahierUploadPath = new File(baseDir, pathCahierRelatif);
        
        return cahierUploadPath;
    }

    /** Charge un fichier depuis le poste de travail. 
     * @param fichierStockage f
     */
    private void effectueUploadStockage(FichierStockageDTO fichierStockage) {
        final String pathCahierRelatif = form.getPathRepertoireEnCoursCahier();
        
        if (StringUtils.isEmpty(pathCahierRelatif)) {
            log.error("Null pathCahierRelatif {0}", pathCahierRelatif);
            return;
        }
        
        File cahierUploadPath = getCTNCheminComplet(pathCahierRelatif);
                
        final String nomFichierDestSansAccent = enleverCaractereSpeciaux(fichierStockage.getNom());
        
        File cahierUploadFileCheminComplet = new File(cahierUploadPath, nomFichierDestSansAccent);
                
        final WebDavQO webDavQO = new WebDavQO();
        webDavQO.setCheminDestinationCahier(cahierUploadFileCheminComplet.getAbsolutePath());
        webDavQO.setCheminFichierDav(fichierStockage.getCheminComplet());
        WebDavUtils.downloadFile(webDavQO);

        final FileUploadDTO file = generateFileUploadDTOPourEcran(pathCahierRelatif + "/" + nomFichierDestSansAccent);

        // Stocke dans le formulaire le fichier qui a ete uploade
        form.setFileUploade(file);
    

        form.setPathCahierRelatif(null);
    }
    
    
    
    /**
     * Transférer le fichier depuis CMIS ver le dépôt CTN.
     * @param fichierCmis l'objet CMIS
     * @return vrai si succès
     */    
    private boolean effectueUploadCmis(CmisObject fichierCmis) {
        
        
        if (fichierCmis == null) {
            log.error("Fichier cmis est null");
            return false;
        }
        
        Document cmisDocument = (Document) fichierCmis;
                
        ContentStream contentStream = cmisDocument.getContentStream();
        
        if (contentStream == null) {
            log.error("Null stream pour object CMIS");
            return false;
        }
        
        InputStream stream = contentStream.getStream();
        
        if (stream == null) {
            log.error("Null stream pour object CMIS");
            return false;
        }
        
        final String pathCahierRelatif = form.getPathRepertoireEnCoursCahier();
        
        if (StringUtils.isEmpty(pathCahierRelatif)) {
            log.error("Null pathCahierRelatif {0}", pathCahierRelatif);
            return false;
        }
        
        File cahierUploadPath = getCTNCheminComplet(pathCahierRelatif);
                
        final String nomFichierDestSansAccent = enleverCaractereSpeciaux(fichierCmis.getProperty(PropertyIds.NAME).getValueAsString());
        
        File cahierUploadFileCheminComplet = new File(cahierUploadPath, nomFichierDestSansAccent);
                
        try {
            FileUtils.copyInputStreamToFile(stream, cahierUploadFileCheminComplet);
        } catch (IOException ex) {
            log.error(ex, "Ex");
            return false;
        }
        
        final FileUploadDTO file = generateFileUploadDTOPourEcran(pathCahierRelatif + "/" + nomFichierDestSansAccent);

        // Stocke dans le formulaire le fichier qui a ete uploade
        form.setFileUploade(file);
    

        form.setPathCahierRelatif(null);
        
        return true;
    }

    /**
     * Supprime les accents et remplace les caracteres spéciaux des fichiers
     * pour construire un nom de fichier propre à injecter dans le dépot de
     * cahier de texte. Les caractères spcéicaux suivants sont remplacés par "_"
     * espace [ ] ( ) ' ^ , \ / " * ? : < > |
     * 
     * @param nomFichier
     *            : nom du fichier a traiter (sans le dossier du fichier, juste
     *            le nom et extension)
     * @return La chaine passée en entrée sans les accents et caractère
     *         spéciaux.
     */
    private String enleverCaractereSpeciaux(final String nomFichier) {
        String nomFichierSansCaractereSpeciaux = org.crlr.utils.StringUtils
                .sansAccent(nomFichier);
        nomFichierSansCaractereSpeciaux = nomFichierSansCaractereSpeciaux
                .replaceAll("[, \\[\\]()\\^\\'\\/\\:\\*\\?\\\"\\<\\>\\|\\\\]",
                        "_");
        return nomFichierSansCaractereSpeciaux;
    }

    /**
     * Verifie si le noeud correspondand à la cle key possede des enfants ou non.
     * @param key la cle
     * @param mapFichier la map
     * @return true/false
     */
    private boolean nodeAvecFils(String key, Map<String, ?> mapFichier) {
        final String sousKey = key + ".0";
        return mapFichier.containsKey(sousKey);
    }
    
    /**
     * 
     * 
     * @param path
     *            DOCUMENTATION INCOMPLETE!
     * @param node
     *            DOCUMENTATION INCOMPLETE!
     * @param mapFichier
     *            DOCUMENTATION INCOMPLETE!
     */
    private void addNodesStockage(String path, TreeNode node, Map<String, FichierStockageDTO> mapFichier) {
        boolean end = false;
        int counter = 0;

        while (!end) {
            final String key = (path != null) ? (path + '.' + counter) : String.valueOf(counter);

            final FichierStockageDTO value = mapFichier.get(key);
            
            

            if (value != null) {
                log.info("Stockage fichierDTO \nvalue.getCheminComplet() {0},\n " +
                        " value.getCheminRelatif() {1},\n value.getRepertoireParent() {2},\n" +
                        "value.getType() {3},\n value.getNom() {4}", 
                        value.getCheminComplet(),
                        value.getCheminRelatif(),
                        value.getRepertoireParent(),
                        value.getType(), value.getNom());
                
                try {
                    // permet d'avoir les accents en provenances du webDav
                    value.setNom(java.net.URLDecoder.decode(value.getNom(),
                            "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    log.warning("Erreur de convertion de l'url {0}", value
                            .getNom());
                }
                
                final boolean avecFils = nodeAvecFils(key, mapFichier) || path==null;
                final AbstractFichierTreeNode<FichierStockageDTO> nodeImpl =
                        new AbstractFichierTreeNode<FichierStockageDTO>(!avecFils){

                            /**
                             * 
                             */
                            private static final long serialVersionUID = 926896603197239077L;

                            /* (non-Javadoc)
                             * @see org.crlr.web.application.control.AbstractFichierTreeNode#toLabel(java.lang.Object)
                             */
                            @Override
                            public String toLabel(FichierStockageDTO noeud) {
                                return noeud.getNom();
                            }
                    
                };
                nodeImpl.setFichierDTO(value);
                nodeImpl.setKey(key);
                node.addChild(Integer.valueOf(counter), nodeImpl);
                addNodesStockage(key, nodeImpl, mapFichier);
                counter++;
            } else {
                end = true;
            }
        }
    }
    
    /**
     * @param baseNode le noeud
     * @param cmisObject l'object correspondant
     */
    private void addNodesCmis(TreeNode baseNode, Folder cmisObject) {
        
        log.info("Add nodes cmis {0}", cmisObject.getId());
        Iterator<CmisObject> children = cmisObject.getChildren().iterator();
        
        while(children.hasNext()) {
            CmisObject child = children.next();
            
            AbstractFichierTreeNode<CmisObject> childNoeud = 
                    new AbstractFichierTreeNode<CmisObject>(child.getBaseTypeId() == BaseTypeId.CMIS_DOCUMENT) {
                
                /**
                         * 
                         */
                        private static final long serialVersionUID = 1L;

                /**
                 * 
                 */

                @Override
                public String toLabel(CmisObject noeud) {
                    return noeud.getProperty(PropertyIds.NAME).getValueAsString();
                }
            };
            
            childNoeud.setKey(child.getId());
            childNoeud.setFichierDTO(child);
            baseNode.addChild(child.getId(), childNoeud);
            
            if (child.getBaseTypeId() == BaseTypeId.CMIS_FOLDER) {
                addNodesCmis(childNoeud, (Folder) child);
            }
        }
            
           
        
    }

    /**
     * Cherche dans l'arbre si un des fils a pour cle celle en parametre. 
     * @param node le noeud de depart
     * @param key la cle 
     * @return le noeud trouve ou null
     */
    private AbstractFichierTreeNode<?> recupererNodeByKey(TreeNode node, String key) {
        if (node.isLeaf()) {
            if (((AbstractFichierTreeNode<?>) node).getKey().equals(key)) {
                return (AbstractFichierTreeNode<?>) node;
            } else {
                return null;
            }
        }
        AbstractFichierTreeNode<?> result = null;
        final java.util.Iterator<Object> iterator = node.getChildrenKeysIterator();
        while (iterator.hasNext()) {
            final Object suivant = iterator.next();
            final TreeNode fils = (TreeNode) node.getChild(suivant);
            result = recupererNodeByKey(fils, key);
            if (result!=null) {
                return result;
            }
        }
        return null;
    }
    
  
    
    /**
     * Declenche lors de la selection d'un fichier depuis stockage.
     */
    public void selectionnerFichierStockage() {
        final String key = form.getNodeStockageSelectedKey();
        final TreeNode tree = form.getRootNode();
        @SuppressWarnings("unchecked")
        final AbstractFichierTreeNode<FichierStockageDTO> node = (AbstractFichierTreeNode<FichierStockageDTO>) recupererNodeByKey(tree, key); 
        if (node != null) {
            effectueUploadStockage(node.getFichierDTO());
        } 
    }
    
    /**
     * 
     */
    public void selectionnerFichierCmis() {
        final String key = form.getNodeCmisSelectedKey();
        final TreeNode tree = form.getRootNode();
        @SuppressWarnings("unchecked")
        final AbstractFichierTreeNode<CmisObject> node = (AbstractFichierTreeNode<CmisObject>) recupererNodeByKey(tree, key); 
         
        if (node == null) {
            MessageUtils.addMessage(new Message(
                    "Une erreur est survenue lors du téléchargement du fichier.",
                    Nature.BLOQUANT), getClass());
        }
        
        
        boolean success = effectueUploadCmis(node.getFichierDTO());
        
        if (!success) {
            MessageUtils.addMessage(new Message(
                    "Une erreur est survenue lors du téléchargement du fichier.",
                    Nature.BLOQUANT), getClass());
        }
         
    }
    
    
    /**
     *
     */
    public void loadTreeStockage() {
        final WebDavQO webDavQO = new WebDavQO();

        final UserDTO userDTO = ContexteUtils.getContexteUtilisateur()
                .getUtilisateurDTO().getUserDTO();
        webDavQO.setUid(userDTO.getUid());
        webDavQO.setDepot(userDTO.getDepotStockage());

        if (!StringUtils.isEmpty(userDTO.getDepotStockage())) {
            final Map<String, FichierStockageDTO> mapFichier = WebDavUtils
                    .findDocuments(webDavQO);
            form.setRootNode(new TreeNodeImpl());
            addNodesStockage(null, form.getRootNode(), mapFichier);
        } else {
            form.setRootNode(new TreeNodeImpl());
        }
    }
    
    /**
     * Charger l'arborescence CMIS.
     */
    public void loadTreeCmis() {
        
        form.setRootNode(new TreeNodeImpl());
        
        if (cmisSession == null) {
            cmisSession = CmisUtils.openCmisSession();
            
        }
        
        if (cmisSession == null) { 
            log.error("Pas de connection CMIS");
            MessageUtils
            .addMessage(
                    new Message(
                            "Une erreur est survenue lors de la connexion au serveur CMIS.",
                            Nature.BLOQUANT), getClass());
            return;
        }
        
        Folder userFolder =  CmisUtils.getUserFolder(cmisSession);        
        
        addNodesCmis(form.getRootNode(), userFolder);                
    }

   
    /**
     * 
     * 
     * @return DOCUMENTATION INCOMPLETE!
     */
    public TreeNode getTreeNode() {
        if (form.getRootNode() == null) {
            form.setRootNode(new TreeNodeImpl());
        }
        return form.getRootNode();
    }

    /** Sélectionne un fichier. */
    public void selectionnerFichierPourCahier() {
        // Positione dans le form le fichier choisi
        form.setFileUploade(form.getCtnFichierSelectionne());       
    }

    /**
     * Construit le nom du fichier selectionne.
     * 
     * @param cheminDownload
     *            dossier courant
     * @return le fichier dans la structure FileUploadDTO
     * */
    private FileUploadDTO generateFileUploadDTOPourEcran(
            final String cheminDownload) {
        final FileUploadDTO file = new FileUploadDTO();

        final String[] chemin = StringUtils.split(cheminDownload, "/");
        file.setNom(chemin[chemin.length - 1]);
        final String uid = ContexteUtils.getContexteUtilisateur()
                .getUtilisateurDTO().getUserDTO().getUid();
        file.setUid(uid);
        file.setActiverLien(true);

        final String pathDB = cheminDownload.replaceFirst(
                form.getPathDepotCahier(), "").replaceFirst(
                "/" + file.getNom(), "");
        file.setPathDB(pathDB);

        FacesUtils.pathUploadFile(file);

        return file;
    }

    // ///////////////////////////////////////////////////////////////DEBUT Mon
    // poste de travail ///////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////// composant
    // UPLOAD ///////////////////////////////////////////////

    /**
     * Fonction d'upload des pièces jointes.
     * 
     * @param event
     *            event
     * 
     * @throws Exception
     *             exception
     */
    public synchronized void listener(FileUploadEvent event) throws Exception {
        if (event == null) {
            log.debug("Invalid upload event");
        } else {
            // todo Renommer en emps que getPathContextSelDirMonPoste
            final String cheminCibleRelatif = form
                    .getPathRepertoireEnCoursCahier();

            // on recupere l'item envoye
            final UploadedFile uploadItemTemp = event.getUploadedFile();

            // Pour IE8, le getFileName contient le path complet du fichier,
            // pour les autres navigateurs, il contient que le nom du fichier
            // sans le path)
            String fileName = new String(uploadItemTemp.getName());
            final String sep;
            if (fileName.contains("\\")) {
                sep = "\\\\";

            } else {
                sep = "/";
            }
            final String[] tabFileName = StringUtils.split(fileName, sep);

            // fileName contient que le nom du fichier (sans dossier compris)
            fileName = tabFileName[tabFileName.length - 1];

            // Retire les caractères spéciaux du nom du fichier
            final String nomFichierSansCaractereSpeciaux = enleverCaractereSpeciaux(fileName);

            // constitution du chemin réel d'upload
            String path = ((ServletContext) FacesContext.getCurrentInstance()
                    .getExternalContext().getContext()).getRealPath("/");

            log.debug("path {0}", path);

            for (final String itemChemin : StringUtils.split(
                    cheminCibleRelatif, "/")) {
                if (!StringUtils.isEmpty(itemChemin)) {
                    path += itemChemin + java.io.File.separator;
                }
            }

            log.debug("path absolute complet {0}", path);

            // constitution du nom de fichier horodaté
            fileName = nomFichierSansCaractereSpeciaux; // new
                                                        // String(uploadItem.getFileName());
            final String fileNameWithoutExt = fileName.substring(0, fileName
                    .lastIndexOf("."));
            final String extension = fileName.substring(fileName
                    .lastIndexOf("."));

            fileName = fileNameWithoutExt
                    + "_"
                    + DateUtils.format(DateUtils.getMaintenant(),
                            Locale.FRENCH, "ddMMyyyy-Hms") + extension;

            // chemin du fichier + nom du fichier horodaté
            final File fileToWrite = new java.io.File(path + fileName);

            try {
                final FileOutputStream fos = new FileOutputStream(fileToWrite
                        .getAbsolutePath());
                fos.write(uploadItemTemp.getData());
                fos.close();

                final String uid = ContexteUtils.getContexteUtilisateur()
                        .getUtilisateurDTO().getUserDTO().getUid();

                final FileUploadDTO file = new FileUploadDTO();

                file.setNom(fileName);
                file.setUid(uid);
                file.setActiverLien(true);
                final String pathDownload = ((ServletContext) FacesContext
                        .getCurrentInstance().getExternalContext().getContext())
                        .getContextPath();

                final String cheminRelatifDownload = pathDownload
                        + cheminCibleRelatif + "/" + fileName;
                file.setPathFullDownload(cheminRelatifDownload);
                file.setPathAbsolute(path);
                file.setPathDB(cheminCibleRelatif.replaceFirst(form
                        .getPathDepotCahier(), ""));

                // Stocke dans le formulaire le fichier qui a ete uploade
                form.setFileUploade(file);
                selectionnerRepertoireCahier();
                
                /*!!
                 * ContexteUtils.getContexteOutilControl().mettreAJourObjet(                        
                 *  PopupPiecesJointesControl.class.getName()                                
                 *  + form.getOutilAppelant().name(), file);
                 */
                log.debug("ajout de la PJ");
                form.setUploadsAvailable(form.getUploadsAvailable() - 1);
            } catch (FileNotFoundException fnfe) {
                log.debug("FileNotFoundException : {0}", fileName);
            } catch (IOException ioe) {
                log.debug("IOException : {0}", ioe.getMessage());
            }
        }
    }

    /**
     * Fonction onComplete pour vérifier qu'un type de fichier est permis.
     */
    public void checkMessageTypeFileUpload() {
        MessageUtils.addMessage(new Message(
                TypeReglesPieceJointe.PIECEJOINTE_02.name(), Nature.BLOQUANT),
                getClass());
    }

    /**
     * Fonction onComplete pour vérifier qu'un type de fichier est permis.
     */
    public void checkMessageSizeFileUpload() {
        MessageUtils.addMessage(new Message(
                TypeReglesPieceJointe.PIECEJOINTE_03.name(), Nature.BLOQUANT),
                getClass());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String clearUploadData() {
        log.debug("clear event");
        return null;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String uploadComplete() {
        uploadCompleted = true;
        return null;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENTATION INCOMPLETE!
     */
    public boolean isAutoUpload() {
        return autoUpload;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENTATION INCOMPLETE!
     */
    public boolean isUploadCompleted() {
        return uploadCompleted;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param uploadCompleted
     *            DOCUMENTATION INCOMPLETE!
     */
    public void setUploadCompleted(boolean uploadCompleted) {
        this.uploadCompleted = uploadCompleted;
    }

    /**
     * Retourne la liste des fichiers contenus dans le dossier courant.
     */
    private void listerFiles() {
        final ArrayList<FileUploadDTO> files = new ArrayList<FileUploadDTO>();

        final List<FileSystemNode> nodes = this.form
                .getSourceRootsDepotCahierFiles();


        for (FileSystemNode node : nodes) {
            FileUploadDTO file;
            try {
                String dossier = node.getPath().replaceFirst(
                        form.getPathDepotCahier() + "/", "");
                dossier = "/"
                        + dossier.substring(0, dossier.length()
                                - node.getName().length() - 1);

                file = obtenirFileUploadDTO(dossier,  node.getName());
                /*
                        FilenameUtils.getFullPathNoEndSeparator(file.getPathFullDownload().replaceFirst(form
                        .getPathDepotCahier(), "")));*/
                

            } catch (Exception ex) {
                log.error("ex", ex);
                continue;
            }
            log.info("File\n file.getActiverLien() {0},\n" +
            		" file.getEnBase() {1}, file.getNom() {2}, \n" +
            		"file.getNomCourt() {3}, \n " +
            		"file.getPathFullDownload() {4}, \n " +
            		"file.getPathDB() {5}, \n, " +
            		"file.getPathAbsolute() {6}, \n, " +
            		"file.getUid() {7}", 
            		file.getActiverLien(), file.getEnBase(), 
            		file.getNom(), file.getNomCourt(), 
            		file.getPathFullDownload(), file.getPathDB(), 
            		file.getPathAbsolute(), file.getUid());
            files.add(file);
        }
        form.setFiles(files);
        form.setCtnFichiers(files);
    }

    /**
     * Vérifie si l'arborescence contient un fichier.
     * 
     * @param dir
     *            nom du dossier complet à parcourir en profondeur
     * @return TRUE ou FALSE si un fichier a été trouvé dans un des dossiers de
     *         l'arborescence
     */
    private boolean existeFichierDansDossier(File dir) {

        if (!dir.exists()) {
            log.warning("Dir {0} n'existe pas", dir.getAbsolutePath());
            return Boolean.FALSE;
        }
        
        if (!dir.isDirectory()) {
            log.warning("Dir {0} n'est pas un dossier", dir.getAbsolutePath());
            return false;
        }

        final String[] liste = dir.list();

        for (final String nomFichier : liste) {
            final File fichier = new File(dir, nomFichier);

            // C'est un fichier, on vérifie s'il est utilisé
            if (fichier.isFile()) {
                return Boolean.TRUE;
                // C'est un dossier, on vérifie s'il contient un fichier utilisé
            } else {
                if (existeFichierDansDossier(new File(dir, nomFichier))) {
                    return Boolean.TRUE;
                }
            }
        }
        // On n'a rien trouvé, on renvoie FALSE
        return Boolean.FALSE;
    }

    /**
     * Selectionne le dossier courant.
     * 
     * @param nomDossierSelectionne
     *            nom du dossier à selectionner
     *            ex:"/uploadFile/kbj0027f/Mes Documents/papa"
     */
    private void selectionnerDossier(final String nomDossierSelectionne) {
        final List<FileSystemNode> srcRoots = new FileSystemNode(false,
                nomDossierSelectionne).getFilesOfNodes();
        form.setSourceRootsDepotCahierFiles(srcRoots);

        form.setPathRepertoireEnCoursCahier(nomDossierSelectionne);
        form.setNameRepSel(form.getPathRepertoireEnCoursCahier().replaceFirst(
                form.getPathDepotCahier() + "/", ""));

        final String[] listeDossier = StringUtils.split(form
                .getNameRepSel(), '/');
        String dossierParent = "";
        final Integer nbDossier = listeDossier.length;
        final String sousDossier = listeDossier[nbDossier - 1];
        for (int i = 0; i < nbDossier - 1; i++) {
            if (!StringUtils.isEmpty(dossierParent)) {
                dossierParent += "/";
            }
            dossierParent += listeDossier[i];
        }
        if (StringUtils.isEmpty(dossierParent)) {
            form.setNameDossierSel("");
            form.setNameDossierParentSel(sousDossier);
            form.setIsDossierModifiable(Boolean.FALSE);
        } else {
            form.setNameDossierSel(sousDossier);
            form.setNameDossierParentSel(dossierParent);

            final File path = getCTNCheminComplet(form.getPathRepertoireEnCoursCahier());

            if (existeFichierDansDossier(path)) {
                form.setIsDossierModifiable(Boolean.FALSE);
            } else {
                form.setIsDossierModifiable(Boolean.TRUE);
            }
        }

        // Parcours la liste des fichiers du dossier pour setter le files
        listerFiles();
    }

    /**
     * Recharge l'arbre complet.
     */
    public void reloadTree() {
        createRootsDepotCahier();
    }

    /**
     * Créer un nouveau dossier dans le dossier en cours. Le nom du dossier créé
     * est "Nouveau" par défaut.
     */
    public void nouveauDossier() {
        final String path = ((ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext()).getRealPath("/");

        final String masqueNouveau = "Nouveau dossier";
        String nomNouveauDossiser = masqueNouveau;
        File dir = new File(path + form.getPathRepertoireEnCoursCahier()
                + java.io.File.separator + nomNouveauDossiser);
        Integer i = 1;
        while (dir.exists()) {
            nomNouveauDossiser = masqueNouveau + " " + i.toString();
            dir = new File(path + form.getPathRepertoireEnCoursCahier()
                    + java.io.File.separator + nomNouveauDossiser);
            i++;
        }
        if (!dir.exists()) {
            dir.mkdirs();
            selectionnerDossier(form.getPathRepertoireEnCoursCahier() + "/"
                    + nomNouveauDossiser);
            reloadTree();
        }
    }

    /**
     * Renome le dossier en cours par le nom du dossier saisi.
     */
    public void renomerDossier() {

        if (StringUtils.isEmpty(form.getNameDossierSel())) {
            MessageUtils.addMessage(new Message(
                    "Veuillez saisir un nom de dossier.", Nature.BLOQUANT),
                    getClass());
            return;
        }

        final File dossiserAModifier = getCTNCheminComplet(form
                .getPathRepertoireEnCoursCahier());

        final File nouveauNomDossier = new File(
                dossiserAModifier.getParentFile(),
                form.getNameDossierSel());

        if (!dossiserAModifier.exists()) {
            log.error("Dossier à renommé n'existe pas", dossiserAModifier.getAbsolutePath());
            return;
        }
        
        if (nouveauNomDossier.exists()) {
            MessageUtils.addMessage(new Message(
                    "Un dossier existe déjà avec ce même nom.",
                    Nature.BLOQUANT), getClass());
            return;
            
        } 
        
        if (existeFichierDansDossier(dossiserAModifier)) {
            log.error("Fichier dans le dossier");
            MessageUtils
            .addMessage(
                    new Message(
                            "Impossible de renommer ce dossier. Il contient des fichiers.",
                            Nature.BLOQUANT), getClass());
            return;
        }
        
        
        if (dossiserAModifier.renameTo(nouveauNomDossier)) {
            // maj du nom du dossier modifié
            selectionnerDossier(form.getPathDepotCahier() + "/"
                    + form.getNameDossierParentSel() + "/"
                    + form.getNameDossierSel());
            this.reloadTree();
        } else {
            MessageUtils
                    .addMessage(
                            new Message(
                                    "Une erreur est survenue lors du renommage du dossier.",
                                    Nature.BLOQUANT), getClass());
        }        
    }
    
    /**
     * @param file un fichier
     * @return le pathDB correspondant
     */
    private String getPathDb(File file) {
        File pathAbsoluteCTNDir = getCTNCheminComplet(form.getPathDepotCahier());
        try {
        String r = file.getCanonicalPath().replace(pathAbsoluteCTNDir.getCanonicalPath(), "");
        r = FilenameUtils.separatorsToUnix(r);
        return r;
        } catch (IOException ex) {
            log.error(ex, "ex");
            return "";
        }
    }
    
    /**
     * @param pathDB champ dans la BDD
     * @param nomFichier nom du fichier
     * @return le DTO
     */
    private FileUploadDTO obtenirFileUploadDTO(final String pathDB,
            final String nomFichier)  {
               
        final String uid = ContexteUtils.getContexteUtilisateur()
                .getUtilisateurDTO().getUserDTO().getUid();
        
        FileUploadDTO file = new FileUploadDTO();
        
        File pathAbsoluteCTNDir = getCTNCheminComplet(form.getPathDepotCahier());
        
        File pathAbsolute = new File(pathAbsoluteCTNDir, pathDB);
        pathAbsolute = new File(pathAbsolute, nomFichier);
        
        file.setPathFullDownload(
                FilenameUtils.separatorsToUnix(
                        FilenameUtils.concat(
                                FilenameUtils.concat( form.getPathDepotCahier(), pathDB),
                                nomFichier)));
        file.setActiverLien(pathAbsolute.exists());
        file.setPathDB(pathDB);
        file.setNom(nomFichier);
        file.setPathAbsolute(pathAbsolute.getAbsolutePath());
        /*
                FilenameUtils.getFullPathNoEndSeparator(file.getPathFullDownload().replaceFirst(form
                .getPathDepotCahier(), "")));*/
        file.setUid(uid);
        
        log.info("File\n file.getActiverLien() {0},\n" +
                " file.getEnBase() {1}, file.getNom() {2}, \n" +
                "file.getNomCourt() {3}, \n " +
                "file.getPathFullDownload() {4}, \n " +
                "file.getPathDB() {5}, \n, " +
                "file.getPathAbsolute() {6}, \n, " +
                "file.getUid() {7}", 
                file.getActiverLien(), file.getEnBase(), 
                file.getNom(), file.getNomCourt(), 
                file.getPathFullDownload(), file.getPathDB(), 
                file.getPathAbsolute(), file.getUid());
        
        return file;
    }

   

    /**
     * Supprime le fichier du dossier spécifié.
     * 
     * @param fichier le fichier à supprimer
     * @param fichierASupprimer
     *            : fichier à supprimer
     * @return true ou false selon que la suppression s'est correctement
     *         déroulée.
     */
    private boolean supprimerUnFichier(final File fichier,
            final FileUploadDTO fichierASupprimer) {
        
        if (fichier.exists()) {

            try {
                // Supprime le fichier physisquement et en base
                if (this.depotService.deleteFile(fichierASupprimer, fichier)) {
                    return true;
                } else {
                    MessageUtils
                            .addMessage(
                                    new Message(
                                            "Une erreur est survenue lors de la suppression du fichier.",
                                            Nature.BLOQUANT), getClass());
                }
            } catch (Exception e) {
                MessageUtils
                        .addMessage(
                                new Message(
                                        "Une exception est survenue lors de la suppression du fichier.",
                                        Nature.BLOQUANT), getClass());
            }
        }
        return false;
    }

    /**
     * Supprime recursiement toute une arbrorescence de fichier.
     * 
     * @param dir
     *            nom du dossier complet à supprimer en profondeur avec tout ce
     *            qu'il contient
     * @return TRUE ou FALSE si pas d'erreur rencontrée
     */
    private Boolean supprimerArborescence(File dir) {

        if (!dir.exists()) {
            return Boolean.FALSE;
        }

        final String[] liste = dir.list();

        for (final String nomFichier : liste) {

            final File fichier = new File(dir, nomFichier);

            // C'est un fichier, on le supprime
            if (fichier.isFile()) {

                final FileUploadDTO fichierDTO = obtenirFileUploadDTO(
                        getPathDb(dir), nomFichier);
                if (!supprimerUnFichier(fichier, fichierDTO)) {
                    return Boolean.FALSE;
                }

                // C'est un dossier, on vérifie supprime ce qu'il contient et
                // lui même
            } else {
                if (!supprimerArborescence(new File(dir, nomFichier))) {
                    return Boolean.FALSE;
                }
            }
        }
        // Le dossier ne doit plus rien contenir, on peut le supprimer
        if (!dir.delete()) {
            return Boolean.FALSE;
        }

        // il n'y a pas du avoir d'erreur
        return Boolean.TRUE;
    }

    /**
     * Supprime le doussier en cours.
     */
    public void deleteDossier() {
        
        final File dossierASupprimer = getCTNCheminComplet(form.getPathRepertoireEnCoursCahier());
        if (!dossierASupprimer.exists()) {
            log.warning("Dir à supprimé {0} n'existe pas", dossierASupprimer.getAbsolutePath());
            return;
        }
        
        if (existeFichierDansDossier(dossierASupprimer)) {
            log.warning("Fichiers existe dans le dossier {0}", dossierASupprimer.getAbsolutePath());
            return;
        }
        
        if (supprimerArborescence(dossierASupprimer)) {

            // Se positionne sur le parent
            selectionnerDossier(form.getPathDepotCahier() + "/"
                    + form.getNameDossierParentSel());

            reloadTree();
        }
        
        
    }

    /**
     * L'onglet stockage.
     */
    public void afficherStockage() {
        form.setOngletSelectionne("STOCKAGE");
        //Rafraîchir les données
        loadTreeStockage();
    }
    
    public void afficherCmis() {
        form.setOngletSelectionne("CMIS");
        //Rafraîchir les données
        loadTreeCmis();
    }
    
    
    /**
     * l'onglet CTN.
     */
    public void afficherDepot() {
        form.setOngletSelectionne("DEPOT");
        //Rafraîchir les données
        selectionnerRepertoireCahier();
    }
    
}
