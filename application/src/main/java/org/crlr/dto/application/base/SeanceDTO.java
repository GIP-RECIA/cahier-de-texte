/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SeanceDTO.java,v 1.2 2009/05/26 08:06:30 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.dto.application.visa.VisaDTO;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.dto.TypeCouleur;

/**
 * Un DTO pour contenir une seance.
 * 
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class SeanceDTO implements Serializable {
    protected static final Log log = LogFactory.getLog(SeanceDTO.class);

    /**  */
    private static final long serialVersionUID = -826585374240054242L;

    /** Id de la séance. */
    private Integer id;

    /** Code de la séance. */
    private String code;

    /** Intitulé de la séance. */
    private String intitule;

    /** Date de la séance. */
    private Date date;

    /** Heure de début de la séance. */
    private Integer heureDebut;

    /** Minute de l'heure de début. */
    private Integer minuteDebut;

    /** Heure de fin de la séance. */
    private Integer heureFin;

    /** Minute de l'heure de fin. */
    private Integer minuteFin;

    /** Description de la séance. */
    private String description;

    /** La description en HTML avec des images dragmath. */
    private String descriptionHTML;

    /** La description pour le pdf avec des renvoies. */
    private String descriptionPDF;

    /** Annotations personnelles de l'enseignant. */
    private String annotations;

    /** La annotations en HTML avec des images dragmath. */
    private String annotationsHTML;

    private SequenceDTO sequenceDTO;

    /** Identifiant de l'enseignant. */
    private EnseignantDTO enseignantDTO;
        
    
    /** Libelle de la matiere correspondant à la seance. */
    private String matiere;

    /**
     * Couleur de la seance dans l'emploi du temps.
     */
    private TypeCouleur typeCouleur;

    private String emploiDeTempsDescription;

    private List<FileUploadDTO> files;

    private FileUploadDTO pieceJointeSelectionne;

    private List<DevoirDTO> devoirs;

    /** Date de dernière modification de la seance. */
    private Date dateMaj;

    /** Visa directeur. */
    private VisaDTO visaDirecteur;

    /** Visa inspecteur. */
    private VisaDTO visaInspecteur;
        
    private Boolean accesEcriture;

    /** Couleur selectionnee */
        private String selectedCouleur;
    /**
     * Consturcteur.
     */
    public SeanceDTO() {
        devoirs = new ArrayList<DevoirDTO>();
        files = new ArrayList<FileUploadDTO>();
        enseignantDTO = new EnseignantDTO();
        sequenceDTO = new SequenceDTO();
    }

    /**
     * Copie depuis seanceSrc les champs dans la seance : - annotations -
     * description - intitule
     * 
     * @param seanceSrc
     *            la seance source
     */
    public void copieFrom(final SeanceDTO seanceSrc) {
        this.description = seanceSrc.getDescription();
        this.annotations = seanceSrc.getAnnotations();
        
        Integer enseignantId = seanceSrc.getIdEnseignant();
        Integer idUtilisateurConnecte = ContexteUtils.getContexteUtilisateur().getUtilisateurDTOConnecte().getUserDTO().getIdentifiant();
        
        if (enseignantId == null || !enseignantId.equals(idUtilisateurConnecte)) {
            log.debug("Ne pas copier les annotations personnel en mode remplaçant");
            this.annotations = "";
        }
            
        
        this.intitule = seanceSrc.getIntitule();

        this.devoirs = new ArrayList<DevoirDTO>();

        for (DevoirDTO devoirSrc : seanceSrc.getDevoirs()) {
            DevoirDTO devoirCopie = ObjectUtils.clone(devoirSrc);
            devoirCopie.setId(null);

            for (FileUploadDTO pjSrc : devoirCopie.getFiles()) {
                pjSrc.setIdDevoir(null);
            }

            devoirs.add(devoirCopie);
        }

        this.files = new ArrayList<FileUploadDTO>();

        for (FileUploadDTO pjSrc : seanceSrc.getFiles()) {
            FileUploadDTO pjCopie = ObjectUtils.clone(pjSrc);
            pjCopie.setIdDevoir(null);
            files.add(pjCopie);
        }
    }

    /**
     * Acceusseur id.
     * 
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur id.
     * 
     * @param id
     *            l'id à modifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Acceusseur code.
     * 
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * Mutateur code.
     * 
     * @param code
     *            le code à modifier
     */
    public void setCode(String code) {
        this.code = code;
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
     * Accesseur intituleSeance.
     * 
     * @return le intituleSeance
     */
    public String getIntituleSeanceCourt() {
        return org.apache.commons.lang.StringUtils
                .abbreviate(getIntitule(), 25);
    }

    /**
     * Mutateur intitule.
     * 
     * @param intitule
     *            L'intitule à modifier
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
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
     * @param date
     *            La date à modifier
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Setter de la date a partir d'une Long (nombre de jours depuis ...).
     * 
     * @param date
     *            la date
     */
    public void setDateLong(Long date) {
        this.date = new Date(date);
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
     * @param heureDebut
     *            heureDebut à modifier
     */
    public void setHeureDebut(Integer heureDebut) {
        this.heureDebut = heureDebut;
    }

    /**
     * Accesseur heureFin.
     * 
     * @return heureFin .
     */
    public Integer getHeureFin() {
        return heureFin;
    }

    /**
     * Retourne l'heure de la seance au format texte lisible.
     * 
     * @return xxhxx
     */
    public String getHoraireDebut() {
        if (heureDebut >= 0 && minuteDebut >= 0) {
            return String.format("%02dh%02d", heureDebut, minuteDebut);
        } else {
            return "";
        }
    }

    /**
     * Retourne l'heure de la seance au format texte lisible.
     * 
     * @return xxhxx
     */
    public String getHoraireFin() {
        if (heureFin >= 0 && minuteFin >= 0) {
            return String.format("%02dh%02d", heureFin, minuteFin);
        } else {
            return "";
        }
    }

    /**
     * Mutateur heureFin.
     * 
     * @param heureFin
     *            heureFin à modifier
     */
    public void setHeureFin(Integer heureFin) {
        this.heureFin = heureFin;
    }

    /**
     * Accesseur description.
     * 
     * @return description .
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutateur description. Transforme le contenu de la description au cas où
     * il y aurait des liens pour s'assurer qu'ils s'ouvrent dans une autre
     * page.
     * 
     * @param description
     *            La description à modifier
     */
    public void setDescription(String description) {
        if (description != null) {
            String[] monSplit = description.split("<a");
            if (monSplit.length > 1) {
                try {
                    String maDescModifie = monSplit[0];
                    // il y a un lien dans la description.
                    for (int i = 0; i < monSplit.length; i++) {
                        if (i > 0) {
                            if (monSplit[i]
                                    .contains("onclick=\"window.open(this.href); return false;")) {
                                // Une sauvegarde a été effectué avec ce contenu. On
                                // le garde et on rajoute que le <a devant.
                                maDescModifie += "<a " + monSplit[i];
                            } else {
                                maDescModifie += "<a onclick=\"window.open(this.href); return false;\" "
                                        + monSplit[i];
                            }
                        }
                    }
                    this.description = maDescModifie;
                } catch (Exception e) {
                    this.description = description;
                    log.error(e, "exception");
                }
            } else {
                this.description = description;
            }
        } else {
            this.description = description;
        }
    }

    /**
     * Accesseur minuteDebut.
     * 
     * @return le minuteDebut.
     */
    public Integer getMinuteDebut() {
        return minuteDebut;
    }

    /**
     * Accesseur minuteDebut.
     * 
     * @return le minuteDebut.
     */
    public String getMinuteDebutAffichage() {
        return org.crlr.utils.StringUtils.formatNumber(2, minuteDebut);
    }

    /**
     * Mutateur minuteDebut.
     * 
     * @param minuteDebut
     *            le minuteDebut à modifier.
     */
    public void setMinuteDebut(Integer minuteDebut) {
        this.minuteDebut = minuteDebut;
    }

    /**
     * Accesseur minuteFin.
     * 
     * @return le minuteFin.
     */
    public Integer getMinuteFin() {
        return minuteFin;
    }

    /**
     * Mutateur minuteFin.
     * 
     * @param minuteFin
     *            le minuteFin à modifier.
     */
    public void setMinuteFin(Integer minuteFin) {
        this.minuteFin = minuteFin;
    }

    /**
     * Accesseur idEnseignant.
     * 
     * @return le idEnseignant.
     */
    public Integer getIdEnseignant() {
        return getEnseignantDTO().getId();
    }

    /**
     * Mutateur idEnseignant.
     * 
     * @param idEnseignant
     *            le idEnseignant à modifier.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        getEnseignantDTO().setId(idEnseignant);
    }

    /**
     * Accesseur idGroupe.
     * 
     * @return le idGroupe.
     */
    public Integer getIdGroupe() {
        if (getSequenceDTO() != null
                && TypeGroupe.GROUPE == getSequenceDTO().getTypeGroupe()) {
            return getSequenceDTO().getGroupesClassesDTO().getId();
        }
        return null;
    }

    /**
     * 
     * @param id
     *            id
     */
    @Deprecated
    public void setIdGroupe(Integer id) {
        getSequenceDTO().getGroupesClassesDTO().setId(id);
    }

    /**
     * Accesseur idClasse.
     * 
     * @return le idClasse.
     */
    public Integer getIdClasse() {
        if (getSequenceDTO() != null
                && TypeGroupe.CLASSE == getSequenceDTO().getGroupesClassesDTO()
                        .getTypeGroupe()) {
            return getSequenceDTO().getGroupesClassesDTO().getId();
        }
        return null;
    }

    /**
     * @param id
     *            id
     */
    @Deprecated
    public void setIdClasse(Integer id) {
        getSequenceDTO().getGroupesClassesDTO().setId(id);
    }

    /**
     * Accesseur de designationClasse {@link #designationClasse}.
     * 
     * @return retourne designationClasse
     */
    public String getDesignationClasse() {
        if (getSequenceDTO() != null
                && TypeGroupe.CLASSE == getSequenceDTO().getGroupesClassesDTO()
                        .getTypeGroupe()) {
            return getSequenceDTO().getGroupesClassesDTO().getDesignation();
        }
        return null;

    }

    /**
     * Utilise plutôt groupeClasseDTO dans la séquence.
     * 
     * @param designation
     *            d
     */
    @Deprecated
    public void setDesignationGroupe(String designation) {
        getSequenceDTO().getGroupesClassesDTO().setDesignation(designation);
    }

    /**
     * Utilise plutôt groupeClasseDTO dans la séquence.
     * 
     * @param designation
     *            d
     */
    @Deprecated
    public void setDesignationClasse(String designation) {
        getSequenceDTO().getGroupesClassesDTO().setDesignation(designation);
    }

    /**
     * Accesseur de designationGroupe {@link #designationGroupe}.
     * 
     * @return retourne designationGroupe
     */
    public String getDesignationGroupe() {
        if (getSequenceDTO() != null
                && TypeGroupe.GROUPE == getSequenceDTO().getGroupesClassesDTO()
                        .getTypeGroupe()) {
            return getSequenceDTO().getGroupesClassesDTO().getDesignation();
        }
        return null;
    }

    /**
     * Accesseur de matiere {@link #matiere}.
     * 
     * @return retourne matiere
     */
    public String getMatiere() {
        return matiere;
    }

    /**
     * Mutateur de matiere {@link #matiere}.
     * 
     * @param matiere
     *            the matiere to set
     */
    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    /**
     * Accesseur de typeCouleur.
     * 
     * @return le typeCouleur
     */
    public TypeCouleur getTypeCouleur() {
    	if (typeCouleur == null && sequenceDTO != null) {
    		return sequenceDTO.getTypeCouleur();
    	}
        return typeCouleur;
    }

    /**
     * Mutateur de typeCouleur.
     * 
     * @param typeCouleur
     *            le typeCouleur à modifier.
     */
    public void setTypeCouleur(TypeCouleur typeCouleur) {
        this.typeCouleur = typeCouleur;
    }

    /**
     * Accesseur de files.
     * 
     * @return le files
     */
    public List<FileUploadDTO> getFiles() {
        return files;
    }

    /**
     * Mutateur de files.
     * 
     * @param files
     *            le files à modifier.
     */
    public void setFiles(List<FileUploadDTO> files) {
        this.files = files;
    }

    /**
     * Accesseur de files.
     * 
     * @return le files
     */
    public List<FileUploadDTO> getListeFichierJointDTO() {
        return files;
    }

    /**
     * Mutateur de files.
     * 
     * @param files
     *            le files à modifier.
     */
    public void setListeFichierJointDTO(List<FileUploadDTO> files) {
        this.files = files;
    }

    /**
     * Accesseur de pieceJointeSelectionne.
     * 
     * @return le pieceJointeSelectionne
     */
    public FileUploadDTO getPieceJointeSelectionne() {
        return pieceJointeSelectionne;
    }

    /**
     * Mutateur de pieceJointeSelectionne.
     * 
     * @param pieceJointeSelectionne
     *            le pieceJointeSelectionne à modifier.
     */
    public void setPieceJointeSelectionne(FileUploadDTO pieceJointeSelectionne) {
        this.pieceJointeSelectionne = pieceJointeSelectionne;
    }

    /**
     * Accesseur de devoirs.
     * 
     * @return le devoirs
     */
    public List<DevoirDTO> getDevoirs() {
        return devoirs;
    }

    /**
     * Mutateur de devoirs.
     * 
     * @param devoirs
     *            le devoirs à modifier.
     */
    public void setDevoirs(List<DevoirDTO> devoirs) {
        this.devoirs = devoirs;
    }

    /**
     * Accesseur de devoirs.
     * 
     * @return le devoirs
     */
    public List<DevoirDTO> getListeDevoirDTO() {
        return devoirs;
    }

    /**
     * Mutateur de devoirs.
     * 
     * @param devoirs
     *            le devoirs à modifier.
     */
    public void setListeDevoirDTO(List<DevoirDTO> devoirs) {
        this.devoirs = devoirs;
    }

    /**
     * Valide qu'il existe un fond à utiliser.
     * 
     * @return vrai sss il existe une couleur.
     */
    public Boolean getHasBackground() {
        return this.typeCouleur != null;
    }

    /**
     * Retourne le type de jour correspondant à la date de la seance.
     * 
     * @return un TypeJour
     */
    public TypeJour getTypeJour() {
        if (this.date == null) {
            return null;
        }
        final GregorianCalendar queryCal = new GregorianCalendar();
        queryCal.setTime(date);
        final TypeJour jour = TypeJour.getTypeJour(queryCal
                .get(Calendar.DAY_OF_WEEK));
        return jour;
    }

    /**
     * Retourne le TypeGroupe (classe ou groupe).
     * 
     * @return groupe ou classe.
     */
    public TypeGroupe getTypeGroupe() {
        if (getSequenceDTO() != null
                && getSequenceDTO().getGroupesClassesDTO() != null) {
            return getSequenceDTO().getGroupesClassesDTO().getTypeGroupe();
        } else {
            log.error("Null type");
            return null;
        }

    }

    /**
     * Initialise une seance avec tout ce qui peut être recupere de la case de
     * l'EDT.
     * 
     * @param detail
     *            : une case de l'emploi du temps.
     */
    public void initFromEDT(final DetailJourEmploiDTO detail) {
        if (detail != null) {
            this.setHeureDebut(detail.getHeureDebut());
            this.setHeureFin(detail.getHeureFin());
            this.setMinuteDebut(detail.getMinuteDebut());
            this.setMinuteFin(detail.getMinuteFin());
            
            SequenceDTO sequence = this.getSequenceDTO();
          
            this.getEnseignantDTO().setCivilite(detail.getCiviliteNomPrenom());
            sequence.setGroupesClassesDTO(detail.getGroupeOuClasse());
            sequence.setIdEnseignement(detail.getMatiere().getId());
            sequence.setIdEtablissement(detail.getIdEtablissement());
            sequence.setIdEnseignant(detail.getIdEnseignant());
            sequence.setTypeCouleur(detail.getTypeCouleur());
            this.typeCouleur = detail.getTypeCouleur();
            
        }
    }

    /**
     * Initialise les champ de la seance qui peuvent l'etre a partir d'une
     * sequence. Les champs initialises sont : - code sequence - id sequence
     * 
     * @param sequence
     *            la séquence
     */
    public void initFromSequence(final SequenceDTO sequence) {
        if (sequence == null) {
            return;
        }
        this.setSequenceDTO(sequence);

    }

    /**
     * Accesseur de annotations {@link #annotations}.
     * 
     * @return retourne annotations
     */
    public String getAnnotations() {
        return annotations;
    }

    /**
     * Mutateur de annotations {@link #annotations}.
     * 
     * @param annotations
     *            le annotations to set
     */
    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }

    /**
     * @return the emploiDeTempsDescription
     */
    public String getEmploiDeTempsDescription() {
        return emploiDeTempsDescription;
    }

    /**
     * @param emploiDeTempsDescription
     *            the emploiDeTempsDescription to set
     */
    public void setEmploiDeTempsDescription(String emploiDeTempsDescription) {
        this.emploiDeTempsDescription = emploiDeTempsDescription;
    }

    /**
     * Accesseur de descriptionHTML {@link #descriptionHTML}.
     * 
     * @return retourne descriptionHTML
     */
    public String getDescriptionHTML() {
        return descriptionHTML;
    }

    /**
     * Mutateur de descriptionHTML {@link #descriptionHTML}.
     * 
     * @param descriptionHTML
     *            le descriptionHTML to set
     */
    public void setDescriptionHTML(String descriptionHTML) {
        this.descriptionHTML = descriptionHTML;
    }

    /**
     * Accesseur de annotationsHTML {@link #annotationsHTML}.
     * 
     * @return retourne annotationsHTML
     */
    public String getAnnotationsHTML() {
        return annotationsHTML;
    }

    /**
     * Mutateur de annotationsHTML {@link #annotationsHTML}.
     * 
     * @param annotationsHTML
     *            le annotationsHTML to set
     */
    public void setAnnotationsHTML(String annotationsHTML) {
        this.annotationsHTML = annotationsHTML;
    }

    /**
     * @return the descriptionPDF
     */
    public String getDescriptionPDF() {
        return descriptionPDF;
    }

    /**
     * @param descriptionPDF
     *            the descriptionPDF to set
     */
    public void setDescriptionPDF(String descriptionPDF) {
        this.descriptionPDF = descriptionPDF;
    }

    /**
     * @return the enseignant
     */
    public EnseignantDTO getEnseignantDTO() {
        return enseignantDTO;
    }

    /**
     * @param enseignant
     *            the enseignant to set
     */
    public void setEnseignantDTO(EnseignantDTO enseignant) {
        this.enseignantDTO = enseignant;
    }

    /**
     * @return the sequenceDTO
     */
    public SequenceDTO getSequenceDTO() {
        return sequenceDTO;
    }

    public SequenceDTO getSequence() {
        return sequenceDTO;
    }

    /**
     * @param sequenceDTO
     *            the sequenceDTO to set
     */
    public void setSequenceDTO(SequenceDTO sequenceDTO) {
        this.sequenceDTO = sequenceDTO;
    }

    /**
     * Accesseur de dateMaj {@link #dateMaj}.
     * 
     * @return retourne dateMaj
     */
    public Date getDateMaj() {
        return dateMaj;
    }

    /**
     * Mutateur de dateMaj {@link #dateMaj}.
     * 
     * @param dateMaj
     *            le dateMaj to set
     */
    public void setDateMaj(Date dateMaj) {
        this.dateMaj = dateMaj;
    }

    /**
     * Accesseur de visaDirecteur {@link #visaDirecteur}.
     * 
     * @return retourne visaDirecteur
     */
    public VisaDTO getVisaDirecteur() {
        return visaDirecteur;
    }

    /**
     * Mutateur de visaDirecteur {@link #visaDirecteur}.
     * 
     * @param visaDirecteur
     *            le visaDirecteur to set
     */
    public void setVisaDirecteur(VisaDTO visaDirecteur) {
        this.visaDirecteur = visaDirecteur;
    }

    /**
     * Accesseur de visaInspecteur {@link #visaInspecteur}.
     * 
     * @return retourne visaInspecteur
     */
    public VisaDTO getVisaInspecteur() {
        return visaInspecteur;
    }

    /**
     * Mutateur de visaInspecteur {@link #visaInspecteur}.
     * 
     * @param visaInspecteur
     *            le visaInspecteur to set
     */
    public void setVisaInspecteur(VisaDTO visaInspecteur) {
        this.visaInspecteur = visaInspecteur;
    }

    /**
     * @return the accesEcriture
     */
    public Boolean getAccesEcriture() {
        return accesEcriture;
    }

    /**
     * @param accesEcriture the accesEcriture to set
     */
    public void setAccesEcriture(Boolean accesEcriture) {
        this.accesEcriture = accesEcriture;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SeanceDTO [id=" + id + ", code=" + code + ", intitule="
                + intitule + ", date=" + date + ", heureDebut=" + heureDebut
                + ", minuteDebut=" + minuteDebut + ", heureFin=" + heureFin
                + ", minuteFin=" + minuteFin + ", description=" + description
                + ", descriptionHTML=" + descriptionHTML + ", descriptionPDF="
                + descriptionPDF + ", annotations=" + annotations
                + ", annotationsHTML=" + annotationsHTML + ", sequenceDTO="
                + sequenceDTO + ", enseignantDTO=" + enseignantDTO
                + ", matiere=" + matiere + ", typeCouleur=" + typeCouleur
                + ", emploiDeTempsDescription=" + emploiDeTempsDescription
                + ", files=" + files + ", pieceJointeSelectionne="
                + pieceJointeSelectionne + ", devoirs=" + devoirs
                + ", dateMaj=" + dateMaj + ", visaDirecteur=" + visaDirecteur
                + ", visaInspecteur=" + visaInspecteur + ", accesEcriture="
                + accesEcriture + "] dateMaj " + (dateMaj == null ? '-' : dateMaj.getTime());
    }

	public String getSelectedCouleur() {
		return selectedCouleur;
	}

	public void setSelectedCouleur(String selectedCouleur) {
		this.selectedCouleur = selectedCouleur;
	}

    

  
    
   

}
