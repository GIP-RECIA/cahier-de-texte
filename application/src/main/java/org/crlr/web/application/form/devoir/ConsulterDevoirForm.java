/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConsulterDevoirForm.java,v 1.5 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.web.application.form.devoir;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.crlr.dto.application.devoir.ChargeTravailDTO;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.FileUploadDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public class ConsulterDevoirForm extends AbstractForm {
    /**  */
    private static final long serialVersionUID = -4908799015673831058L;

    /** DOCUMENTATION INCOMPLETE! */
    private String intitule;

    /** DOCUMENTATION INCOMPLETE! */
    private String libelleTypeDevoir;

    /** DOCUMENTATION INCOMPLETE! */
    private Date dateRemise;

    /** DOCUMENTATION INCOMPLETE! */
    private String description;

    /** DOCUMENTATION INCOMPLETE! */
    private List<FileUploadDTO> files;
    
    /** Code de la séquence. */
    private String codeSequence;

    /** Date de début. */
    private Date dateDebut;

    /** Date de fin. */
    private Date dateFin;

    /** Intitulé de la séquence. */
    private String intituleSequence;

    /** Description de la séquence. */
    private String descriptionSequence;

    /**
     * Code de la classe ou du groupe.
     */
    private String codeClasseGroupe;

    /** Id de la classe. */
    private Integer idClasse;
    
    /** Id du groupe. */
    private Integer idGroupe; 
    
    /**
     * Désignation de la classe ou du groupe.
     */
    private String designationClasseGroupe;
    
    /**
     * Désignation de l'enseignement.
     */
    private String designationEnseignement;
    
    /**
     * Nom de l'enseignant.
     */
    private String nom;

    /**
     * civilite de l'enseignant.
     */
    private String civilite;
    
    /**
     * heure de début de la séance.
     */
    private Integer heureDebutSeance;
    
    /**
     * heure de fin de la séance.
     */
    private Integer heureFinSeance;
    
    /**
     * minute de début de la séance.
     */
    private Integer minuteDebutSeance;
    
    /**
     * minute de fin de la séance.
     */
    private Integer minuteFinSeance;
    
    /**
     * Date de la séance.
     */
    private Date dateSeance;
    
    
    //champs Spécifiques à la modification d'un devoir
        
    /** l'id du type de devoir. */
    private Integer idTypeDevoir;
    
    /** l'id du devoir. */
    private Integer idDevoir;
    
    /** uid de l'utilisateur. */
    private String uid;
    
    /** la ligne de resultat séléctionnée dans la liste des pièces jointes. */
    private FileUploadDTO resultatSelectionnePieceJointe;
    
    /** liste des type de devoir. */
    private List<SelectItem> listeTypeDevoir;

    /** Indication sur la charge de travail prévue pour le meme jour. */
    private ChargeTravailDTO chargeTravail;
    
    /** Indique s'il s'agit d'une archive.*/
    private Boolean archive;
    
    /** Exercice s'il s'agit d'une archive.*/
    private String exercice;
    
/**
     * Constructeur.
     */
    public ConsulterDevoirForm() {
       
        intitule = null;
        libelleTypeDevoir = null;
        dateRemise = null;
        description = null;
        files = new ArrayList<FileUploadDTO>();
        dateDebut = null;
        dateFin = null;
        intituleSequence = null;
        codeSequence = null;
        descriptionSequence = null;
        codeClasseGroupe = null;
        designationEnseignement = null;
        nom = null;
        civilite = null;
        minuteDebutSeance = null;
        heureDebutSeance = null;
        minuteFinSeance = null;
        heureFinSeance = null;
        dateSeance = null;
        designationClasseGroupe = null;
        idTypeDevoir = null;
        
        //champs Spécifiques à la modification d'un devoir
        uid = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO().getUid();
        resultatSelectionnePieceJointe = new FileUploadDTO();
        listeTypeDevoir = new ArrayList<SelectItem>();     
        chargeTravail = new ChargeTravailDTO();        
    }

    /**
     * Accesseur intitule.
     *
     * @return intitule
     */
    public String getIntitule() {
        return intitule;
    }

    /**
     * Mutateur intitule.
     *
     * @param intitule Le intitule à modifier
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    /**
     * Accesseur libelleTypeDevoir.
     *
     * @return libelleTypeDevoir
     */
    public String getLibelleTypeDevoir() {
        return libelleTypeDevoir;
    }

    /**
     * Mutateur libelleTypeDevoir.
     *
     * @param libelleTypeDevoir Le libelleTypeDevoir à modifier
     */
    public void setLibelleTypeDevoir(String libelleTypeDevoir) {
        this.libelleTypeDevoir = libelleTypeDevoir;
    }

    /**
     * Accesseur dateRemise.
     *
     * @return dateRemise
     */
    public Date getDateRemise() {
        return dateRemise;
    }

    /**
     * Mutateur dateRemise.
     *
     * @param dateRemise Le dateRemise à modifier
     */
    public void setDateRemise(Date dateRemise) {
        this.dateRemise = dateRemise;
    }

    /**
     * Accesseur description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutateur description.
     *
     * @param description Le description à modifier
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Accesseur files.
     *
     * @return files
     */
    public List<FileUploadDTO> getFiles() {
        return files;
    }

    /**
     * Mutateur files.
     *
     * @param files Le files à modifier
     */
    public void setFiles(List<FileUploadDTO> files) {
        this.files = files;
    }

    /**
     * Accesseur codeSequence.
     * @return codeSequence
     */
    public String getCodeSequence() {
        return codeSequence;
    }

    /**
     * Mutateur codeSequence.
     * @param codeSequence Le code à modifier
     */
    public void setCodeSequence(String codeSequence) {
        this.codeSequence = codeSequence;
    }

    /**
     * Accesseur dateDebut.
     * @return dateDebut
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * Mutateur dateDebut.
     * @param dateDebut Le dateDebut à modifier
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Accesseur dateFin.
     * @return dateFin
     */
    public Date getDateFin() {
        return dateFin;
    }

    /**
     * Mutateur dateFin.
     * @param dateFin Le dateFin à modifier
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * Accesseur intituleSequence.
     * @return intituleSequence
     */
    public String getIntituleSequence() {
        return intituleSequence;
    }

    /**
     * Mutateur intituleSequence.
     * @param intituleSequence Le intituleSequence à modifier
     */
    public void setIntituleSequence(String intituleSequence) {
        this.intituleSequence = intituleSequence;
    }

    /**
     * Accesseur descriptionSequence.
     * @return descriptionSequence
     */
    public String getDescriptionSequence() {
        return descriptionSequence;
    }

    /**
     * Mutateur descriptionSequence.
     * @param descriptionSequence Le descriptionSequence à modifier
     */
    public void setDescriptionSequence(String descriptionSequence) {
        this.descriptionSequence = descriptionSequence;
    }

    /**
     * Accesseur codeClasseGroupe.
     * @return codeClasseGroupe
     */
    public String getCodeClasseGroupe() {
        return codeClasseGroupe;
    }

    /**
     * Mutateur codeClasseGroupe.
     * @param codeClasseGroupe Le codeClasseGroupe à modifier
     */
    public void setCodeClasseGroupe(String codeClasseGroupe) {
        this.codeClasseGroupe = codeClasseGroupe;
    }

    /**
     * Accesseur designationEnseignement.
     * @return designationEnseignement
     */
    public String getDesignationEnseignement() {
        return designationEnseignement;
    }

    /**
     * Mutateur designationEnseignement.
     * @param designationEnseignement Le designationEnseignement à modifier
     */
    public void setDesignationEnseignement(String designationEnseignement) {
        this.designationEnseignement = designationEnseignement;
    }

    /**
     * Accesseur nom.
     * @return nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Mutateur nom.
     * @param nom Le nom à modifier
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Accesseur civilite.
     * @return civilite
     */
    public String getCivilite() {
        return civilite;
    }

    /**
     * Mutateur civilite.
     * @param civilite Le civilite à modifier
     */
    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    /**
     * Accesseur heureDebutSeance.
     * @return heureDebutSeance
     */
    public Integer getHeureDebutSeance() {
        return heureDebutSeance;
    }

    /**
     * Mutateur heureDebutSeance.
     * @param heureDebutSeance Le heureDebutSeance à modifier
     */
    public void setHeureDebutSeance(Integer heureDebutSeance) {
        this.heureDebutSeance = heureDebutSeance;
    }

    /**
     * Accesseur heureFinSeance.
     * @return heureFinSeance
     */
    public Integer getHeureFinSeance() {
        return heureFinSeance;
    }

    /**
     * Mutateur heureFinSeance.
     * @param heureFinSeance Le heureFinSeance à modifier
     */
    public void setHeureFinSeance(Integer heureFinSeance) {
        this.heureFinSeance = heureFinSeance;
    }

    /**
     * Accesseur dateSeance.
     * @return dateSeance
     */
    public Date getDateSeance() {
        return dateSeance;
    }

    /**
     * Mutateur dateSeance.
     * @param dateSeance Le dateSeance à modifier
     */
    public void setDateSeance(Date dateSeance) {
        this.dateSeance = dateSeance;
    }

    /**
     * Accesseur minuteDebutSeance.
     * @return le minuteDebutSeance.
     */
    public Integer getMinuteDebutSeance() {
        return minuteDebutSeance;
    }

    /**
     * Mutateur minuteDebutSeance.
     * @param minuteDebutSeance le minuteDebutSeance à modifier.
     */
    public void setMinuteDebutSeance(Integer minuteDebutSeance) {
        this.minuteDebutSeance = minuteDebutSeance;
    }

    /**
     * Accesseur minuteFinSeance.
     * @return le minuteFinSeance.
     */
    public Integer getMinuteFinSeance() {
        return minuteFinSeance;
    }

    /**
     * Mutateur minuteFinSeance.
     * @param minuteFinSeance le minuteFinSeance à modifier.
     */
    public void setMinuteFinSeance(Integer minuteFinSeance) {
        this.minuteFinSeance = minuteFinSeance;
    }

    /**
     * Accesseur designationClasseGroupe.
     * @return le designationClasseGroupe.
     */
    public String getDesignationClasseGroupe() {
        return designationClasseGroupe;
    }

    /**
     * Mutateur designationClasseGroupe.
     * @param designationClasseGroupe le designationClasseGroupe à modifier.
     */
    public void setDesignationClasseGroupe(String designationClasseGroupe) {
        this.designationClasseGroupe = designationClasseGroupe;
    }

    /**
     * Accesseur idTypeDevoir.
     * @return le idTypeDevoir
     */
    public Integer getIdTypeDevoir() {
        if (this.idTypeDevoir != null && !this.idTypeDevoir.equals(0)) {
            return this.idTypeDevoir;
        }
        return null;
    }

    /**
     * Mutateur de idTypeDevoir.
     * @param idTypeDevoir le idTypeDevoir à modifier.
     */
    public void setIdTypeDevoir(Integer idTypeDevoir) {
        this.idTypeDevoir = idTypeDevoir;
    }

    /**
     * Accesseur uid.
     * @return le uid
     */
    public String getUid() {
        return uid;
    }
    
    /**
     * Accesseur resultatSelectionnePieceJointe.
     * @return resultatSelectionnePieceJointe
     */
    public FileUploadDTO getResultatSelectionnePieceJointe() {
        return resultatSelectionnePieceJointe;
    }

    /**
     * Mutateur resultatSelectionnePieceJointe.
     * @param resultatSelectionnePieceJointe Le resultatSelectionnePieceJointe à modifier
     */
    public void setResultatSelectionnePieceJointe(
            FileUploadDTO resultatSelectionnePieceJointe) {
        this.resultatSelectionnePieceJointe = resultatSelectionnePieceJointe;
    }
    
    /**
     * Accesseur listeTypeDevoir.
     * @return listeTypeDevoir
     */
    public List<SelectItem> getListeTypeDevoir() {
        return listeTypeDevoir;
    }

    /**
     * Mutateur listeTypeDevoir.
     * @param listeTypeDevoir Le listeTypeDevoir à modifier
     */
    public void setListeTypeDevoir(List<SelectItem> listeTypeDevoir) {
        this.listeTypeDevoir = listeTypeDevoir;
    }

    /**
     * Accesseur idDevoir.
     * @return le idDevoir
     */
    public Integer getIdDevoir() {
        return idDevoir;
    }

    /**
     * Mutateur de idDevoir.
     * @param idDevoir le idDevoir à modifier.
     */
    public void setIdDevoir(Integer idDevoir) {
        this.idDevoir = idDevoir;
    }

    /**
     * @return the chargeTravail
     */
    public ChargeTravailDTO getChargeTravail() {
        return chargeTravail;
    }

    /**
     * @param chargeTravail the chargeTravail to set
     */
    public void setChargeTravail(ChargeTravailDTO chargeTravail) {
        this.chargeTravail = chargeTravail;
    }

    /**
     * Accesseur de idClasse.
     * @return the idClasse
     */
    public Integer getIdClasse() {
        return idClasse;
    }

    /**
     * Mutateur de idClasse.
     * @param idClasse the idClasse to set
     */
    public void setIdClasse(Integer idClasse) {
        this.idClasse = idClasse;
    }

    /**
     * Accesseur de idGroupe.
     * @return the idGroupe
     */
    public Integer getIdGroupe() {
        return idGroupe;
    }

    /**
     * Mutateur de idGroupe.
     * @param idGroupe the idGroupe to set
     */
    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
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
     * @param archive the archive to set
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
     * @param exercice the exercice to set
     */
    public void setExercice(String exercice) {
        this.exercice = exercice;
    }

}
