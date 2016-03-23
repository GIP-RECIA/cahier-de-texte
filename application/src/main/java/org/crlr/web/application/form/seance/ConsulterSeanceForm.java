/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConsulterSeanceForm.java,v 1.8 2010/05/20 06:34:48 ent_breyton Exp $
 */

package org.crlr.web.application.form.seance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.seance.ResultatRechercheSeanceDTO;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.dto.FileUploadDTO;

/**
 * ConsulterSeanceForm.
 *
 * @author $author$
 * @version $Revision: 1.8 $
 */
public class ConsulterSeanceForm extends AbstractForm {
    /**  */
    private static final long serialVersionUID = 4784572425017177192L;

    /** DOCUMENTATION INCOMPLETE! */
    private SequenceDTO sequenceSelectionnee;

    /** DOCUMENTATION INCOMPLETE! */
    private String intitule;

    /** DOCUMENTATION INCOMPLETE! */
    private Date date;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer heureDebut;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer heureFin;
    
    /** DOCUMENTATION INCOMPLETE! */
    private Integer minuteDebut;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer minuteFin;

    /** DOCUMENTATION INCOMPLETE! */
    private String description;
    
    private String latexImageFilename;

    /** liste des devoirs associés à la séance. */
    private List<DevoirDTO> listeDevoir;

    /** Liste des pièces jointes. */
    private ArrayList<FileUploadDTO> files;

    /** liste des séquences dans la popup. */
    private List<SequenceDTO> listeSequenceSelectionnee;

    /** DOCUMENTATION INCOMPLETE! */
    private boolean modifiable;

    /** DOCUMENTATION INCOMPLETE! */
    private boolean renderedSupprimer;

    /** DOCUMENTATION INCOMPLETE! */
    private String modeSeance;

    /** DOCUMENTATION INCOMPLETE! */
    private ResultatRechercheSeanceDTO resultatRechercheSeanceDTO;

    /** Titre de la page. */
    private String titreDePage;
    
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
    
    /** Devoir sélectionné . */
    private DevoirDTO devoirSelDTO;
    
    /** Indique s'il s'agit d'une archive. */
    private Boolean archive;
    
    /** Exercie concerné s'il s'agit d'une archive. */
    private String exercice;

/**
     * Constructeur.
     */
    public ConsulterSeanceForm() {
        reset();
    }

    /**
     * Méthode de réinitialisation des données du formulaire saisie par
     * l'utilisateur.
     */
    public void reset() {
        sequenceSelectionnee = new SequenceDTO();
        intitule = null;
        date = null;
        heureDebut = null;
        heureFin = null;
        minuteDebut = null;
        minuteFin = null;
        description = null;
        latexImageFilename = null;
        files = new ArrayList<FileUploadDTO>();
        listeDevoir = new ArrayList<DevoirDTO>();
        listeSequenceSelectionnee = new ArrayList<SequenceDTO>();
        modifiable = false;
        renderedSupprimer = false;
        mode = null;
        resultatRechercheSeanceDTO = new ResultatRechercheSeanceDTO();
        designationEnseignement = null;
        nom = null;
        civilite = null;
        devoirSelDTO = new DevoirDTO();
        archive = false;
        exercice = null;
    }

    /**
     * Accesseur sequenceSelectionnee.
     *
     * @return sequenceSelectionnee
     */
    public SequenceDTO getSequenceSelectionnee() {
        return sequenceSelectionnee;
    }

    /**
     * Mutateur sequenceSelectionnee.
     *
     * @param sequenceSelectionnee Le sequenceSelectionnee à modifier
     */
    public void setSequenceSelectionnee(SequenceDTO sequenceSelectionnee) {
        this.sequenceSelectionnee = sequenceSelectionnee;
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
	 * @return the latexImageFilename
	 */
	public String getLatexImageFilename() {
		return latexImageFilename;
	}

	/**
	 * @param latexImageFilename the latexImageFilename to set
	 */
	public void setLatexImageFilename(String latexImageFilename) {
		this.latexImageFilename = latexImageFilename;
	}

	/**
     * Accesseur date.
     *
     * @return date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Mutateur date.
     *
     * @param date Le date à modifier
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Accesseur heureDebut.
     *
     * @return heureDebut
     */
    public Integer getHeureDebut() {
        return heureDebut;
    }

    /**
     * Mutateur heureDebut.
     *
     * @param heureDebut Le heureDebut à modifier
     */
    public void setHeureDebut(Integer heureDebut) {
        this.heureDebut = heureDebut;
    }

    /**
     * Accesseur heureFin.
     *
     * @return heureFin
     */
    public Integer getHeureFin() {
        return heureFin;
    }

    /**
     * Mutateur heureFin.
     *
     * @param heureFin Le heureFin à modifier
     */
    public void setHeureFin(Integer heureFin) {
        this.heureFin = heureFin;
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
     * Accesseur listeDevoir.
     *
     * @return listeDevoir
     */
    public List<DevoirDTO> getListeDevoir() {
        return listeDevoir;
    }

    /**
     * Mutateur listeDevoir.
     *
     * @param listeDevoir Le listeDevoir à modifier
     */
    public void setListeDevoir(List<DevoirDTO> listeDevoir) {
        this.listeDevoir = listeDevoir;
    }

    /**
     * Accesseur files.
     *
     * @return files
     */
    public ArrayList<FileUploadDTO> getFiles() {
        return files;
    }

    /**
     * Mutateur files.
     *
     * @param files Le files à modifier
     */
    public void setFiles(ArrayList<FileUploadDTO> files) {
        this.files = files;
    }

    /**
     * Accesseur listeSequenceSelectionnee.
     *
     * @return listeSequenceSelectionnee
     */
    public List<SequenceDTO> getListeSequenceSelectionnee() {
        return listeSequenceSelectionnee;
    }

    /**
     * Mutateur listeSequenceSelectionnee.
     *
     * @param listeSequenceSelectionnee Le listeSequenceSelectionnee à modifier
     */
    public void setListeSequenceSelectionnee(List<SequenceDTO> listeSequenceSelectionnee) {
        this.listeSequenceSelectionnee = listeSequenceSelectionnee;
    }

    /**
     * Accesseur modifiable.
     *
     * @return modifiable
     */
    public boolean isModifiable() {
        return modifiable;
    }

    /**
     * Mutateur modifiable.
     *
     * @param modifiable Le modifiable à modifier
     */
    public void setModifiable(boolean modifiable) {
        this.modifiable = modifiable;
    }

    /**
     * Accesseur renderedSupprimer.
     *
     * @return renderedSupprimer
     */
    public boolean isRenderedSupprimer() {
        return renderedSupprimer;
    }

    /**
     * Mutateur renderedSupprimer.
     *
     * @param renderedSupprimer Le renderedSupprimer à modifier
     */
    public void setRenderedSupprimer(boolean renderedSupprimer) {
        this.renderedSupprimer = renderedSupprimer;
    }

    /**
     * Accesseur modeSeance.
     *
     * @return modeSeance
     */
    public String getModeSeance() {
        return modeSeance;
    }

    /**
     * Mutateur modeSeance.
     *
     * @param modeSeance Le modeSeance à modifier
     */
    public void setModeSeance(String modeSeance) {
        this.modeSeance = modeSeance;
    }

    /**
     * Accesseur resultatRechercheSeanceDTO.
     *
     * @return resultatRechercheSeanceDTO
     */
    public ResultatRechercheSeanceDTO getResultatRechercheSeanceDTO() {
        return resultatRechercheSeanceDTO;
    }

    /**
     * Mutateur resultatRechercheSeanceDTO.
     *
     * @param resultatRechercheSeanceDTO Le resultatRechercheSeanceDTO à modifier
     */
    public void setResultatRechercheSeanceDTO(ResultatRechercheSeanceDTO resultatRechercheSeanceDTO) {
        this.resultatRechercheSeanceDTO = resultatRechercheSeanceDTO;
    }

    /**
     * Accesseur titreDePage.
     *
     * @return titreDePage
     */
    public String getTitreDePage() {
        return titreDePage;
    }

    /**
     * Mutateur titreDePage.
     *
     * @param titreDePage Le titreDePage à modifier
     */
    public void setTitreDePage(String titreDePage) {
        this.titreDePage = titreDePage;
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
     * Accesseur minuteDebut.
     * @return le minuteDebut.
     */
    public Integer getMinuteDebut() {
        return minuteDebut;
    }

    /**
     * Mutateur minuteDebut.
     * @param minuteDebut le minuteDebut à modifier.
     */
    public void setMinuteDebut(Integer minuteDebut) {
        this.minuteDebut = minuteDebut;
    }

    /**
     * Accesseur minuteFin.
     * @return le minuteFin.
     */
    public Integer getMinuteFin() {
        return minuteFin;
    }

    /**
     * Mutateur minuteFin.
     * @param minuteFin le minuteFin à modifier.
     */
    public void setMinuteFin(Integer minuteFin) {
        this.minuteFin = minuteFin;
    }

    /**
     * Accesseur devoirSelDTO.
     * @return le devoirSelDTO
     */
    public DevoirDTO getDevoirSelDTO() {
        return devoirSelDTO;
    }

    /**
     * Mutateur de devoirSelDTO.
     * @param devoirSelDTO le devoirSelDTO à modifier.
     */
    public void setDevoirSelDTO(DevoirDTO devoirSelDTO) {
        this.devoirSelDTO = devoirSelDTO;
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
