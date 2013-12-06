/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.application.control.importEDT;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.crlr.dto.application.base.GenericDTO;
import org.crlr.dto.application.base.TypeReglesImport;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.importEDT.CaracEtabImportDTO;
import org.crlr.dto.application.importEDT.PeriodeEdtQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.importEDT.DTO.PrintEDTDTO;
import org.crlr.message.Message;
import org.crlr.report.impl.PdfReport;
import org.crlr.services.ImportEDTService;
import org.crlr.web.application.control.AbstractPopupControl;
import org.crlr.web.application.form.importEDT.ImportEDTForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.utils.MessageUtils;
import org.crlr.web.utils.ReportUtils;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

/**
 * Classe Control de l'import EDT.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
 */
@ManagedBean(name = "importEDT")
@ViewScoped
public class ImportEDTControl extends AbstractPopupControl<ImportEDTForm> {

    @ManagedProperty(value = "#{importEDTService}")
    private transient ImportEDTService importEDTService;

    private Boolean pretImport = true;

    private Boolean supprNecessaire = false;
    

    /**
     * Accesseur de pretImport.
     * @return le pretImport
     */
    public Boolean getPretImport() {
        return pretImport;
    }

    /**
     * Modificateur de pretImport.
     * @param pretImport le pretImport à modifier
     */
    public void setPretImport(Boolean pretImport) {
        this.pretImport = pretImport;
    }

    /**
     * Accesseur de supprNecessaire.
     * @return le supprNecessaire
     */
    public Boolean getSupprNecessaire() {
        return supprNecessaire;
    }

    /**
     * Modificateur de supprNecessaire.
     * @param supprNecessaire le supprNecessaire à modifier
     */
    public void setSupprNecessaire(Boolean supprNecessaire) {
        this.supprNecessaire = supprNecessaire;
    }

    /**
     * Modificateur de importEDTService.
     * @param importEDTService le importEDTService à modifier
     */
    public void setImportEDTService(ImportEDTService importEDTService) {
        this.importEDTService = importEDTService;
    }

    /** Constructeur du Control.
     * @param form : le ImportEDTForm correspondant. */
    public ImportEDTControl(ImportEDTForm form) {
        super(form);
    }

    /**
     * Constructeur vide de ImportEDTControl.
     */
    public ImportEDTControl(){
        super(new ImportEDTForm());
    }

    /**
     * Ce qui est effectué au chargement de la page.
     */
    @PostConstruct
    public void onLoad() {
     
        form.setDateDebutPeriode(new Date());
        form.setTexteAide(importEDTService.getAideContextuelle());

        form.setPath(((ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext()).getRealPath("/"));
        final Integer idEtablissement = ContexteUtils.getContexteUtilisateur()
                .getUtilisateurDTO().getIdEtablissement();
        form.setIdEtablissement(idEtablissement);
        final Boolean statut = importEDTService
                .findEtatImportEtablissement(idEtablissement);
        verifieDate();
        if (statut) {
            final String dateImport = importEDTService
                    .checkDateImportEtablissement(idEtablissement);
            if (!StringUtils.isEmpty(dateImport)) {
                if ((new Date().getTime()) - (Long.valueOf(dateImport)) > 5400000) {
                    // si l'import a commencer il y a plus d'1h30, on considère
                    // qu'un incident est survenu
                    this.importEDTService.modifieStatutImportEtablissement(
                            form.getIdEtablissement(), false);
                    this.refresh();
                } else {
                    form.setStatutImport(2); // L'import est en cours.
                    form.setPretImport(false);
                    form.setSupprNecessaire(false);
                }
            } else {
                form.setStatutImport(2); // L'import est en cours.
                form.setPretImport(false);
                form.setSupprNecessaire(false);
            }
        } else {
            if (form.getNbEmploiEnBdd() > 0) {
                form.setStatutImport(1); // Il y a déja des cases dans la BDD.
                                         // Suppression nécessaire avant de
                                         // commencer.
                form.setPretImport(false);
                form.setSupprNecessaire(true);
            } else {
                form.setStatutImport(0); // La BDD est vide, on est prêt pour
                                         // l'import.
                form.setPretImport(true);
                form.setSupprNecessaire(false);
            }
        }
        form.setStsCharge(false);
        form.setEdtCharge(false);
        form.setReadyImport(false);
        form.setVraiOuFauxAfficheMessage(false);
        form.setVraiOuFauxRapportTermine(false);
        form.setGoInsertion(false);

       if (form.getVraiOuFauxErreurDouble()) {
            MessageUtils.addMessage(new Message(TypeReglesImport.IMPORT_00.name(), Message.Nature.AVERTISSANT), getClass());
        }
       if (form.getVraiOuFauxErreurFichier()) {
           MessageUtils.addMessage(new Message(TypeReglesImport.IMPORT_01.name(), Message.Nature.AVERTISSANT), getClass());
       }
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
     * Fonction d'upload du fichier XML 1, est appelé lors du clic sur "upload".
     * @param event event
     */
    public synchronized void listener1(FileUploadEvent event){
        
        final UploadedFile item = event.getUploadedFile();
        final String uid = ContexteUtils.getContexteUtilisateur().getUidLdapCas();
        
        
        // Pour IE8, le getFileName contient le path complet du fichier,
        // pour les autres navigateurs, il contient que le nom du fichier
        // sans le path)
        String fileName = new String(item.getName());
        final String sep;
        if (fileName.contains("\\")) {
            sep = "\\\\";

        } else {
            sep = "/";
        }
        final List<String> tabFileName = Arrays.asList(StringUtils.split(fileName, sep));

        // fileName contient que le nom du fichier (sans dossier compris)
        fileName = tabFileName.get(tabFileName.size() - 1);

        // Retire les caractères spéciaux du nom du fichier
        final String nomFichierSansCaractereSpeciaux = enleverCaractereSpeciaux(fileName);
        fileName = uid + nomFichierSansCaractereSpeciaux;
        
        
          //chemin du fichier + nom du fichier horodaté
        final File fileToWrite = new java.io.File(form.getPath() + "importEDT" + java.io.File.separator + fileName);  
        
        //remplir le fichier avec le flux.
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(fileToWrite.getAbsolutePath());
            fos.write(item.getData());
            fos.close();
        } catch(FileNotFoundException fnfe) {
            log.debug("FileNotFoundException : {0}", fileName);
        } catch(IOException ioe) {
            log.debug("IOException : {0}", ioe.getMessage());
        }        
        
        final Integer resultatType = analyseFichierCharge(fileToWrite);
        if(resultatType == 1){
            if(form.getStsCharge()){
                form.setVraiOuFauxErreurDouble(true);
            }else{
                form.setStsCharge(true);
                form.setNomFichierSTS(fileToWrite.getPath());
                form.setNomOrigineFicSts(nomFichierSansCaractereSpeciaux);
                log.info("le fichier uploadé : {0}", form.getNomFichierSTS());
            }
        }else if(resultatType == 2){
            if(form.getEdtCharge()){
                form.setVraiOuFauxErreurDouble(true);
            }else{
                form.setEdtCharge(true);
                form.setNomFichierEDT(fileToWrite.getPath());
                form.setNomOrigineFicEdt(nomFichierSansCaractereSpeciaux);
                log.info("le fichier uploadé : {0}", form.getNomFichierEDT());
            }
        }else{
            //là c'est mal, c'est pas un STS ni un EDT.
            form.setVraiOuFauxErreurFichier(true);
        }
        if(form.getEdtCharge() && form.getStsCharge()){ 
            form.setReadyImport(true); 
        }
    }

    /**
     * Méthode appelée lorsque l'utilisateur clique sur "lancer l'import".
     */
    public void demarrageImport(){
        log.info("Début de l'application importEDT ------------------");
        CaracEtabImportDTO caracEtabImportDTO = new CaracEtabImportDTO();
        
        // Stoque l'utilisateur (qui contient entre autre les semaines de vacances
        final UtilisateurDTO utilisateurDTO =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        caracEtabImportDTO.setUtilisateurDTO(utilisateurDTO);
        caracEtabImportDTO.setIdEtablissement(form.getIdEtablissement());
        caracEtabImportDTO.setDateDebutPeriode(form.getDateDebutPeriode());
        
        try {
            form.setReadyImport(false);
            // Le traitement qui permet de récupérer les listes de cases emploi à insérer et les infos pour le rapport.
            caracEtabImportDTO = this.importEDTService.saveTraitementEDTSTS(
                    caracEtabImportDTO, 
                    form.getNomFichierSTS(), 
                    form.getNomFichierEDT(), 
                    form.getPath());
            form.setPrintEDTDTO(caracEtabImportDTO.getPrintEDTDTO());
            form.setVraiOuFauxRapportTermine(true);
            form.setGoInsertion(true);
            form.setCasesSimples(caracEtabImportDTO.getCasesSimples());
            form.setPretImport(false);
            final List<GenericDTO<String, String>> infosFicXML = new ArrayList<GenericDTO<String,String>>();
            infosFicXML.add(new GenericDTO<String, String>("STS_EDT", form.getNomOrigineFicSts()));
            infosFicXML.add(new GenericDTO<String, String>("EDT_STS", form.getNomOrigineFicEdt()));
            form.setInfosFicXML(infosFicXML);
        } catch (MetierException me) {
            log.debug("Echec d'import EDT : {0}", me.getMessage());
            form.setStsCharge(false); form.setEdtCharge(false); form.setReadyImport(false);
        }
    }

    /**
     * Méthode qui vide les emplois présents en BDD pour l'établissement.
     */
    public void viderEmploiDuTempsEtab(){
        final PeriodeEdtQO periodeEdtQO = new PeriodeEdtQO(); 
        periodeEdtQO.setIdEtablissement(form.getIdEtablissement());
        periodeEdtQO.setDateDebut(form.getDateDebutPeriode());
        this.importEDTService.deleteEmploiDuTempsEtablissement(periodeEdtQO);
        form.setNbEmploiEnBdd(this.importEDTService.checkNombreCaseEmploiPourEtablissement(periodeEdtQO));
        form.setPretImport(true);
        form.setSupprNecessaire(false);
    }

    /**
     * effectue l'édition PDF à partir des résultats de l'import.
     * @throws IOException : exception dans l'édition.
     */
    public void print() throws IOException {
        final PrintEDTDTO printEDT = form.getPrintEDTDTO();
        final PdfReport pdf = this.importEDTService.printEmploiDuTemps(printEDT);
        ReportUtils.stream(pdf);        
    }

    /**
     * Réinitialise la page avec les valeurs par défaut.
     */
    public void refresh(){
        clear1();
        this.onLoad();
    }

    /** L'action clear met à jour l'état de chargement du fichier 1 et annule le lancement de l'import s'il était prêt. */
    public void clear1(){
        form.setVraiOuFauxErreurFichier(false);
        form.setInsertionTerminee(false);
        form.setEdtCharge(false);
        form.setStsCharge(false);
        form.setVraiOuFauxErreurDouble(false);
        form.setReadyImport(false);
        form.setGoInsertion(false);
        form.setNomFichierEDT(null);
        form.setNomFichierSTS(null);
        form.setNomOrigineFicEdt(null);
        form.setNomOrigineFicSts(null);
    }

    /** Démarre l'insertion des cases d'emplois du temps en base de données. */
    public void demarreInsertion(){
        form.setGoInsertion(false);
        this.importEDTService.modifieStatutImportEtablissement(form.getIdEtablissement(), true);
        this.importEDTService.insertionCases(form.getCasesSimples(), form.getDateDebutPeriode());
        clear1();
        form.setInsertionTerminee(true);
    }
    
    /**
     * Analyse le fichier chargé afin de savoir si c'est le STS ou l'EDT.
     * @param upFichier : le fichier que vient d'uploadé l'utilisateur.
     * @return 0 : fichier invalide, 1 : fichier STS, 2 : fichier EDT.
     */
    private Integer analyseFichierCharge(File upFichier){
        BufferedReader buff = null;
        try{
            final InputStreamReader isr = new InputStreamReader(new FileInputStream(upFichier));
            buff = new BufferedReader(isr);
            String maLigne = "";
            while ((maLigne = buff.readLine()) != null) {
                if((maLigne.contains("<STS_EDT>")) || (maLigne.contains("<EDT_STS>"))){
                    if(maLigne.contains("<STS_EDT>")){
                        return 1;
                    }else if(maLigne.contains("<EDT_STS>")){
                        return 2;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            log.info("Exception dans l'analyse du fichier : {0}", e.toString());
        } catch (IOException e) {
            log.info("Exception dans l'analyse du fichier : {0}", e.toString());
        }finally{
            if(buff != null){
                try {
                    buff.close();
                } catch (IOException e) {
                    log.info("Exception dans l'analyse du fichier : {0}", e.toString());
                }
            }
        }
        return 0;
    }
    
    /**
     * Methode declenchee suite au changement de date de periode.
     */
    public void verifieDate() {
        final Integer idEtablissement = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getIdEtablissement();
        final PeriodeEdtQO periodeEdtQO = new PeriodeEdtQO();
        periodeEdtQO.setIdEtablissement(idEtablissement);
        periodeEdtQO.setDateDebut(form.getDateDebutPeriode());
        form.setNbEmploiEnBdd(importEDTService.checkNombreCaseEmploiPourEtablissement(periodeEdtQO));
        form.setSupprNecessaire(form.getNbEmploiEnBdd()>0);
        form.setInsertionTerminee(false);
    }
    
}
