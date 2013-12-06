/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AjoutDevoirForm.java,v 1.9 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.web.application.form.devoir;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.crlr.dto.application.devoir.ChargeTravailDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.FileUploadDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
public class AjoutDevoirForm extends AbstractForm {
    /**  */
    private static final long serialVersionUID = 1519109657998968215L;

    /** Id du devoir. */
    private Integer idDevoir;
    
    /** DOCUMENTATION INCOMPLETE! */
    private String intitule;

    private List<SelectItem> listeTypeDevoir;
    
    /** DOCUMENTATION INCOMPLETE! */
    private Integer idTypeDevoir;
    
    /** DOCUMENTATION INCOMPLETE! */
    private String typeDevoir;

    /** DOCUMENTATION INCOMPLETE! */
    private Date dateRemise;

    /** DOCUMENTATION INCOMPLETE! */
    private String description;

    /** DOCUMENTATION INCOMPLETE! */
    private List<FileUploadDTO> files;
    
    /** la ligne de resultat séléctionnée dans la liste des pièces jointes. */
    private FileUploadDTO resultatSelectionnePieceJointe;
    
    /** uid de l'utilisateur. */
    private String uid;
    
    /** Detail du jour selectionne par l'utilisateur. */
    private DetailJourEmploiDTO detailJourEmploiDTO;
    
    private ChargeTravailDTO chargeTravail;
    
    /**
     * Mode ajout ou  modif.
     */
    private String modeDevoir;   
    
    /**
     * Boolean indiquant si la mise à jour du devoir s'est correctement déroulée ou non.
     */
    private Boolean vraixFauxDevoirMaj;

    /**
     * Boolean indiquant si on enregistre le devoir simultanement avec la seance.
     */
    private Boolean vraixFauxMajDevoirEtSeance;
    
    /**
     * Constructeur.
     */
    public AjoutDevoirForm() {
        uid = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO().getUid();
        reset();
    }

    /**
     * Méthode de réinitialisation des données du formulaire saisie par
     * l'utilisateur.
     */
    public void reset() {
        modeDevoir = null;
        resetChamp();
    }
    
    /**
     * Réinitialisation des champs.
     */
    public void resetChamp() {
        intitule = null;
        idTypeDevoir = null;
        dateRemise = null;
        description = null;
        files = new ArrayList<FileUploadDTO>();
        resultatSelectionnePieceJointe = new FileUploadDTO();
        typeDevoir = null;     
        vraixFauxDevoirMaj = Boolean.FALSE;
        vraixFauxMajDevoirEtSeance = Boolean.FALSE;
        idDevoir = null;
        chargeTravail = new ChargeTravailDTO();
        
    }

    /**
     * Accesseur idDevoir.
     * @return the idDevoir
     */
    public Integer getIdDevoir() {
        return idDevoir;
    }

    /**
     * Mutateur idDevoir.
     * @param idDevoir the idDevoir to set
     */
    public void setIdDevoir(Integer idDevoir) {
        this.idDevoir = idDevoir;
    }

    /**
     * Accesseur intitule.
     * @return intitule
     */
    public String getIntitule() {
        return intitule;
    }

    /**
     * Mutateur intitule.
     * @param intitule Le intitule à modifier
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    /**
     * Accesseur idTypeDevoir.
     * @return idTypeDevoir
     */
    public Integer getIdTypeDevoir() {
        if (this.idTypeDevoir != null && !this.idTypeDevoir.equals(0)) {
            return this.idTypeDevoir;
        }
        return null;
    }

    /**
     * Mutateur idTypeDevoir.
     * @param idTypeDevoir Le idTypeDevoir à modifier
     */
    public void setIdTypeDevoir(Integer idTypeDevoir) {
       this.idTypeDevoir = idTypeDevoir;
    }

    /**
     * Accesseur dateRemise.
     * @return dateRemise
     */
    public Date getDateRemise() {
        return dateRemise;
    }

    /**
     * Mutateur dateRemise.
     * @param dateRemise Le dateRemise à modifier
     */
    public void setDateRemise(Date dateRemise) {
        this.dateRemise = dateRemise;
    }

    /**
     * Accesseur description.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutateur description.
     * @param description Le description à modifier
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Accesseur files.
     * @return files
     */
    public List<FileUploadDTO> getFiles() {
        return files;
    }

    /**
     * Mutateur files.
     * @param files Le files à modifier
     */
    public void setFiles(List<FileUploadDTO> files) {
        this.files = files;
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
     * Accesseur vraixFauxDevoirMaj.
     * @return vraixFauxDevoirMaj
     */
    public Boolean getVraixFauxDevoirMaj() {
        return vraixFauxDevoirMaj;
    }
    
    /**
     * Mutateur vraixFauxDevoirMaj.
     * @param vraixFauxDevoirMaj valeur à setter
     */
    public void setVraixFauxDevoirMaj(Boolean vraixFauxDevoirMaj) {
        this.vraixFauxDevoirMaj = vraixFauxDevoirMaj;
    }

    
    /**
     * Accesseur vraixFauxMajDevoirEtSeance.
     * @return vraixFauxMajDevoirEtSeance
     */
    public Boolean getVraixFauxMajDevoirEtSeance() {
        return vraixFauxMajDevoirEtSeance;
    }

    /**
     * Mutateur vraixFauxMajDevoirEtSeance.
     * @param vraixFauxMajDevoirEtSeance vraixFauxMajDevoirEtSeance 
     */
    public void setVraixFauxMajDevoirEtSeance(Boolean vraixFauxMajDevoirEtSeance) {
        this.vraixFauxMajDevoirEtSeance = vraixFauxMajDevoirEtSeance;
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
     * Accesseur modeDevoir.
     * @return modeDevoir
     */
    public String getModeDevoir() {
        return modeDevoir;
    }

    /**
     * Mutateur modeDevoir.
     * @param modeDevoir Le modeDevoir à modifier
     */
    public void setModeDevoir(String modeDevoir) {
        this.modeDevoir = modeDevoir;
    }
    
    /**
     * Accesseur uid.
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Accesseur typeDevoir.
     * @return typeDevoir
     */
    public String getTypeDevoir() {
        return typeDevoir;
    }

    /**
     * Mutateur typeDevoir.
     * @param typeDevoir Le typeDevoir à modifier
     */
    public void setTypeDevoir(String typeDevoir) {
        this.typeDevoir = typeDevoir;
    }

    /**
     * Mutateur uid.
     * @param uid Le uid à modifier
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Accesseur de chargeTravail.
     * @return the chargeTravail
     */
    public ChargeTravailDTO getChargeTravail() {
        return chargeTravail;
    }

    /**
     * Mutator de chargeTravail.
     * @param chargeTravail the chargeTravail to set
     */
    public void setChargeTravail(ChargeTravailDTO chargeTravail) {
        this.chargeTravail = chargeTravail;
    }

    /**
     * Accesseur detailJourEmploiDTO.
     * @return the detailJourEmploiDTO
     */
    public DetailJourEmploiDTO getDetailJourEmploiDTO() {
        return detailJourEmploiDTO;
    }

    /**
     * Mutateur detailJourEmploiDTO.
     * @param detailJourEmploiDTO the detailJourEmploiDTO to set
     */
    public void setDetailJourEmploiDTO(DetailJourEmploiDTO detailJourEmploiDTO) {
        this.detailJourEmploiDTO = detailJourEmploiDTO;
    }
    
    
    
    
}
