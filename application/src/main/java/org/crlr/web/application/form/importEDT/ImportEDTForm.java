/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.application.form.importEDT;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.crlr.dto.application.base.GenericDTO;
import org.crlr.importEDT.DTO.EmploiDTO;
import org.crlr.importEDT.DTO.PrintEDTDTO;
import org.crlr.web.application.form.AbstractPopupForm;

/**
 * La classe FORM lié à l'interface de l'import EDT.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public class ImportEDTForm extends AbstractPopupForm {

    /** serial ID généré. */
    private static final long serialVersionUID = 4202461789536792235L;
    
    /** DTO qui contient les listes d'anomalies. */
    private PrintEDTDTO printEDTDTO;
    
    /** Chemin du fichier STS chargé. */
    private String nomFichierSTS = "";
    
    /** nom qu'a le fichier STS sur l'ordinateur de l'utilisateur. Utilisé pour l'affichage. */
    private String nomOrigineFicSts = "";
    
    /** nom qu'a le fichier EDT sur l'ordinateur de l'utilisateur. Utilisé pour l'affichage. */
    private String nomOrigineFicEdt = "";
    
    /** Chemin du fichier EDT chargé. */
    private String nomFichierEDT = "";
    
    /** paramètre du fileUpload : upload automatique ou manuel. */
    private Boolean autoUpload = false;
    /** paramètre du fileUpload : utilise le flash. */
    private Boolean useFlash = false;
    /** paramètre indiquant si le rapport est terminé. */
    private Boolean vraiOuFauxRapportTermine = false;
    /** id de l'établissement sur lequel l'utilisateur consulte le cahier de texte. */
    private Integer idEtablissement = 1;
    
    /** Date de debut de periode. */
    private Date dateDebutPeriode;
    
    /** indique le nombre d'enregistrements de cases d'emploi du temps déja en BDD. */
    private Integer nbEmploiEnBdd = 0;
    /** indique si le STS est chargé. */
    private Boolean stsCharge = false;
    /** indique si le EDT est chargé. */
    private Boolean edtCharge = false;
    /** indique si on autorise à appuyer sur le démarrage de l'import. */
    private Boolean readyImport = false;
    /** Affichage du message d'information. */
    private Boolean vraiOuFauxAfficheMessage = false;
    /** texte informatif qui s'affiche dans la popup de confirmation au déclenchement de l'import. */
    private String texteInfoImport = "La première étape du traitement de l\\'import d\\'emploi du temps va démarrer. "+
    "Souhaitez-vous continuer ?";
    
    /** Permet de savoir si on affiche l'icone de confirmation d'insertion en BDD. */
    private Boolean goInsertion = false;
    
    /** Indique que l'insertion est achevée. */
    private Boolean insertionTerminee;
    
    /** Liste des listes de cases à fusionner. */
    private List<List<EmploiDTO>> casesFusion = new ArrayList<List<EmploiDTO>>();
    /** Liste des cases à insérer sans fusion. */
    private List<EmploiDTO> casesSimples = new ArrayList<EmploiDTO>();
    /** Entier qui permet de connaitre l'état de l'établissement par rapport à l'import EDT
     * 0 = vide, 1 = contient des cases, 2 = import en cours. */
    private Integer statutImport = 0;
    /** indique si on affiche l'interface pour faire un nouvel import. */
    private Boolean pretImport = true;
    /** indique si on affiche l'interface demandant le vidage de la bdd pour cet établissement avant un import. */
    private Boolean supprNecessaire = false;
    /** Le chemin où est stockée l'application. */
    private String path = "";
    /** contenu de l'aide contextuelle. */
    private String texteAide;
    
    /** les 2 infos sur les fichiers xml chargés. */
    private List<GenericDTO<String, String>> infosFicXML = new ArrayList<GenericDTO<String,String>>();

    /** Indique si on a une erreur pour cause de saisie 2x du même type de fichier XML. */
    private Boolean vraiOuFauxErreurDouble = false;
    /** indique que ce n'est pas le bon fichier XML qui a été chargé. */
    private Boolean vraiOuFauxErreurFichier = false;
    /** Texte de la popup de confirmation pour lancer l'insertion en base de données. */
    private String confirmInsert = "L\\'insertion des données de l\\'emploi du temps peut durer plusieurs minutes," +
    " cependant, cette action est invisible pour vous.";

    /**
     * Accesseur de idEtablissement.
     * @return le idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Modificateur de idEtablissement.
     * @param idEtablissement le idEtablissement à modifier
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur de printEDTDTO.
     * @return le printEDTDTO
     */
    public PrintEDTDTO getPrintEDTDTO() {
        return printEDTDTO;
    }

    /**
     * Modificateur de printEDTDTO.
     * @param printEDTDTO le printEDTDTO à modifier
     */
    public void setPrintEDTDTO(PrintEDTDTO printEDTDTO) {
        this.printEDTDTO = printEDTDTO;
    }

    /**
     * Accesseur de nomFichierSTS.
     * @return le nomFichierSTS
     */
    public String getNomFichierSTS() {
        return nomFichierSTS;
    }

    /**
     * Modificateur de pathFichierSTS.
     * @param nomFichierSTS le pathFichierSTS à modifier
     */
    public void setNomFichierSTS(String nomFichierSTS) {
        this.nomFichierSTS = nomFichierSTS;
    }

    /**
     * Accesseur de nomFichierEDT.
     * @return le nomFichierEDT
     */
    public String getNomFichierEDT() {
        return nomFichierEDT;
    }

    /**
     * Modificateur de nomFichierEDT.
     * @param nomFichierEDT le nomFichierEDT à modifier
     */
    public void setNomFichierEDT(String nomFichierEDT) {
        this.nomFichierEDT = nomFichierEDT;
    }

    /**
     * Accesseur de autoUpload.
     * @return le autoUpload
     */
    public Boolean getAutoUpload() {
        return autoUpload;
    }

    /**
     * Modificateur de autoUpload.
     * @param autoUpload le autoUpload à modifier
     */
    public void setAutoUpload(Boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

    /**
     * Accesseur de useFlash.
     * @return le useFlash
     */
    public Boolean getUseFlash() {
        return useFlash;
    }

    /**
     * Modificateur de useFlash.
     * @param useFlash le useFlash à modifier
     */
    public void setUseFlash(Boolean useFlash) {
        this.useFlash = useFlash;
    }

    /**
     * Accesseur de vraiOuFauxRapportTermine.
     * @return le vraiOuFauxRapportTermine
     */
    public Boolean getVraiOuFauxRapportTermine() {
        return vraiOuFauxRapportTermine;
    }

    /**
     * Modificateur de vraiOuFauxRapportTermine.
     * @param vraiOuFauxRapportTermine le vraiOuFauxRapportTermine à modifier
     */
    public void setVraiOuFauxRapportTermine(Boolean vraiOuFauxRapportTermine) {
        this.vraiOuFauxRapportTermine = vraiOuFauxRapportTermine;
    }

    /**
     * Accesseur de nbEmploiEnBdd.
     * @return le nbEmploiEnBdd
     */
    public Integer getNbEmploiEnBdd() {
        return nbEmploiEnBdd;
    }

    /**
     * Modificateur de nbEmploiEnBdd.
     * @param nbEmploiEnBdd le nbEmploiEnBdd à modifier
     */
    public void setNbEmploiEnBdd(Integer nbEmploiEnBdd) {
        this.nbEmploiEnBdd = nbEmploiEnBdd;
    }

    /**
     * Accesseur de stsCharge.
     * @return le stsCharge
     */
    public Boolean getStsCharge() {
        return stsCharge;
    }

    /**
     * Modificateur de stsCharge.
     * @param stsCharge le stsCharge à modifier
     */
    public void setStsCharge(Boolean stsCharge) {
        this.stsCharge = stsCharge;
    }

    /**
     * Accesseur de edtCharge.
     * @return le edtCharge
     */
    public Boolean getEdtCharge() {
        return edtCharge;
    }

    /**
     * Modificateur de edtCharge.
     * @param edtCharge le edtCharge à modifier
     */
    public void setEdtCharge(Boolean edtCharge) {
        this.edtCharge = edtCharge;
    }

    /**
     * Accesseur de readyImport.
     * @return le readyImport
     */
    public Boolean getReadyImport() {
        return readyImport;
    }

    /**
     * Modificateur de readyImport.
     * @param readyImport le readyImport à modifier
     */
    public void setReadyImport(Boolean readyImport) {
        this.readyImport = readyImport;
    }

    /**
     * Accesseur de vraiOuFauxAfficheMessage.
     * @return le vraiOuFauxAfficheMessage
     */
    public Boolean getVraiOuFauxAfficheMessage() {
        return vraiOuFauxAfficheMessage;
    }

    /**
     * Modificateur de vraiOuFauxAfficheMessage.
     * @param vraiOuFauxAfficheMessage le vraiOuFauxAfficheMessage à modifier
     */
    public void setVraiOuFauxAfficheMessage(Boolean vraiOuFauxAfficheMessage) {
        this.vraiOuFauxAfficheMessage = vraiOuFauxAfficheMessage;
    }

    /**
     * Accesseur de texteInfoImport.
     * @return le texteInfoImport
     */
    public String getTexteInfoImport() {
        return texteInfoImport;
    }

    /**
     * Modificateur de texteInfoImport.
     * @param texteInfoImport le texteInfoImport à modifier
     */
    public void setTexteInfoImport(String texteInfoImport) {
        this.texteInfoImport = texteInfoImport;
    }

    /**
     * Accesseur de goInsertion.
     * @return le goInsertion
     */
    public Boolean getGoInsertion() {
        return goInsertion;
    }

    /**
     * Modificateur de goInsertion.
     * @param goInsertion le goInsertion à modifier
     */
    public void setGoInsertion(Boolean goInsertion) {
        this.goInsertion = goInsertion;
    }

    /**
     * Accesseur de casesFusion.
     * @return le casesFusion
     */
    public List<List<EmploiDTO>> getCasesFusion() {
        return casesFusion;
    }

    /**
     * Modificateur de casesFusion.
     * @param casesFusion le casesFusion à modifier
     */
    public void setCasesFusion(List<List<EmploiDTO>> casesFusion) {
        this.casesFusion = casesFusion;
    }

    /**
     * Accesseur de casesSimples.
     * @return le casesSimples
     */
    public List<EmploiDTO> getCasesSimples() {
        return casesSimples;
    }

    /**
     * Modificateur de casesSimples.
     * @param casesSimples le casesSimples à modifier
     */
    public void setCasesSimples(List<EmploiDTO> casesSimples) {
        this.casesSimples = casesSimples;
    }

    /**
     * Accesseur de nomOrigineFicSts.
     * @return le nomOrigineFicSts
     */
    public String getNomOrigineFicSts() {
        return nomOrigineFicSts;
    }

    /**
     * Modificateur de nomOrigineFicSts.
     * @param nomOrigineFicSts le nomOrigineFicSts à modifier
     */
    public void setNomOrigineFicSts(String nomOrigineFicSts) {
        this.nomOrigineFicSts = nomOrigineFicSts;
    }

    /**
     * Accesseur de nomOrigineFicEdt.
     * @return le nomOrigineFicEdt
     */
    public String getNomOrigineFicEdt() {
        return nomOrigineFicEdt;
    }

    /**
     * Modificateur de nomOrigineFicEdt.
     * @param nomOrigineFicEdt le nomOrigineFicEdt à modifier
     */
    public void setNomOrigineFicEdt(String nomOrigineFicEdt) {
        this.nomOrigineFicEdt = nomOrigineFicEdt;
    }

    /**
     * Accesseur de statutImport.
     * @return le statutImport
     */
    public Integer getStatutImport() {
        return statutImport;
    }

    /**
     * Modificateur de statutImport.
     * @param statutImport le statutImport à modifier
     */
    public void setStatutImport(Integer statutImport) {
        this.statutImport = statutImport;
    }

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
     * Accesseur de path.
     * @return le path
     */
    public String getPath() {
        return path;
    }

    /**
     * Modificateur de path.
     * @param path le path à modifier
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Accesseur de texteAide.
     * @return le texteAide
     */
    public String getTexteAide() {
        return texteAide;
    }

    /**
     * Modificateur de texteAide.
     * @param texteAide le texteAide à modifier
     */
    public void setTexteAide(String texteAide) {
        this.texteAide = texteAide;
    }

    /**
     * Accesseur de infosFicXML.
     * @return le infosFicXML
     */
    public List<GenericDTO<String, String>> getInfosFicXML() {
        return infosFicXML;
    }

    /**
     * Modificateur de infosFicXML.
     * @param infosFicXML le infosFicXML à modifier
     */
    public void setInfosFicXML(List<GenericDTO<String, String>> infosFicXML) {
        this.infosFicXML = infosFicXML;
    }

    /**
     * Accesseur de vraiOuFauxErreurDouble.
     * @return le vraiOuFauxErreurDouble
     */
    public Boolean getVraiOuFauxErreurDouble() {
        return vraiOuFauxErreurDouble;
    }

    /**
     * Modificateur de vraiOuFauxErreurDouble.
     * @param vraiOuFauxErreurDouble le vraiOuFauxErreurDouble à modifier
     */
    public void setVraiOuFauxErreurDouble(Boolean vraiOuFauxErreurDouble) {
        this.vraiOuFauxErreurDouble = vraiOuFauxErreurDouble;
    }

    /**
     * Accesseur de vraiOuFauxErreurFichier.
     * @return le vraiOuFauxErreurFichier
     */
    public Boolean getVraiOuFauxErreurFichier() {
        return vraiOuFauxErreurFichier;
    }

    /**
     * Modificateur de vraiOuFauxErreurFichier.
     * @param vraiOuFauxErreurFichier le vraiOuFauxErreurFichier à modifier
     */
    public void setVraiOuFauxErreurFichier(Boolean vraiOuFauxErreurFichier) {
        this.vraiOuFauxErreurFichier = vraiOuFauxErreurFichier;
    }

    /**
     * Accesseur de confirmInsert.
     * @return le confirmInsert
     */
    public String getConfirmInsert() {
        return confirmInsert;
    }

    /**
     * Modificateur de confirmInsert.
     * @param confirmInsert le confirmInsert à modifier
     */
    public void setConfirmInsert(String confirmInsert) {
        this.confirmInsert = confirmInsert;
    }

    /**
     * Accesseur de DateDebutPeriode {@link #DateDebutPeriode}.
     * @return retourne dateDebutPeriode
     */
    public Date getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    /**
     * Mutateur de DateDebutPeriode {@link #DateDebutPeriode}.
     * @param dateDebutPeriode le dateDebutPeriode to set
     */
    public void setDateDebutPeriode(Date dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    /**
     * Accesseur de insertionTerminee {@link #insertionTerminee}.
     * @return retourne insertionTerminee
     */
    public Boolean getInsertionTerminee() {
        return insertionTerminee;
    }

    /**
     * Mutateur de insertionTerminee {@link #insertionTerminee}.
     * @param insertionTerminee le insertionTerminee to set
     */
    public void setInsertionTerminee(Boolean insertionTerminee) {
        this.insertionTerminee = insertionTerminee;
    }


}
