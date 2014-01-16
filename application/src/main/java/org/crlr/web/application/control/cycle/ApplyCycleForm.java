package org.crlr.web.application.control.cycle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.cycle.CycleDTO;
import org.crlr.dto.application.cycle.CycleSeanceFinalDTO;
import org.crlr.web.application.form.AbstractPopupForm;

/**
 * Formulaire de cration auto des seance à partir d'une sequence pedagoqique
 * sur une classe/groupe.
 * @author G-SAFIR-FRMP
 *
 */
public class ApplyCycleForm extends AbstractPopupForm {

    /** Serial. */
    private static final long serialVersionUID = -7585368650985189247L;

    /** Cycle assoicie au forumaire. */
    private CycleDTO cycle;

    /** Variable volatile qui va stocker l'id de la zone a rafraichir suite à un upload de piece jointe.*/
    private String raffraichirTabAfterUpload;
    
    /** Liste des seances. */
    private List<CycleSeanceFinalDTO> listeSeance; 
    
    /** Date de depart. */
    private Date dateDepart;
    
    /** Seance selectionnee dans la liste (pour edition). */
    private CycleSeanceFinalDTO seanceSelected;
    
    /** Enseignant connecté. */
    private EnseignantDTO enseignantDTO;

    /** Liste des enseignements de l'enseignant connecté. */
    private List<EnseignementDTO> listeEnseignement;

    /** Enseignement par défaut à appliquer lors de la génération des séance. */
    private EnseignementDTO enseignementDafaultDTO;
    
    /** Titre de la seance en cours de modification dans la popup. */ 
    private String seanceTitre;
    
    /** Id de la ligne de seance pour lequelle l'enseignement a ete modifie. */ 
    private Integer ligneSeanceId;

    /** Id de la ligne de enseignement pour lequelle l'enseignement a ete modifie. */ 
    private Integer ligneEnseignementId;
    
    /**
     * Reset les champ du formulaire.
     */
    public void reset() {
        raffraichirTabAfterUpload = null;
        dateDepart = null;
        listeSeance = new ArrayList<CycleSeanceFinalDTO>();
        enseignementDafaultDTO = null;
    }
    
    /**
     * Constructeur.
     */
    public ApplyCycleForm() {
        super();
        reset();
    }

    /**
     * Accesseur de cycle {@link #cycle}.
     * @return retourne cycle
     */
    public CycleDTO getCycle() {
        return cycle;
    }

    /**
     * Mutateur de cycle {@link #cycle}.
     * @param cycle le cycle to set
     */
    public void setCycle(CycleDTO cycle) {
        this.cycle = cycle;
    }

    /**
     * Accesseur de raffraichirTabAfterUpload {@link #raffraichirTabAfterUpload}.
     * @return retourne raffraichirTabAfterUpload
     */
    public String getRaffraichirTabAfterUpload() {
        return raffraichirTabAfterUpload;
    }

    /**
     * Mutateur de raffraichirTabAfterUpload {@link #raffraichirTabAfterUpload}.
     * @param raffraichirTabAfterUpload le raffraichirTabAfterUpload to set
     */
    public void setRaffraichirTabAfterUpload(String raffraichirTabAfterUpload) {
        this.raffraichirTabAfterUpload = raffraichirTabAfterUpload;
    }

    /**
     * Accesseur de listeSeance {@link #listeSeance}.
     * @return retourne listeSeance
     */
    public List<CycleSeanceFinalDTO> getListeSeance() {
        return listeSeance;
    }

    /**
     * Mutateur de listeSeance {@link #listeSeance}.
     * @param listeSeance le listeSeance to set
     */
    public void setListeSeance(List<CycleSeanceFinalDTO> listeSeance) {
        this.listeSeance = listeSeance;
    }

    /**
     * Accesseur de dateDepart {@link #dateDepart}.
     * @return retourne dateDepart
     */
    public Date getDateDepart() {
        return dateDepart;
    }

    /**
     * Mutateur de dateDepart {@link #dateDepart}.
     * @param dateDepart le dateDepart to set
     */
    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    /**
     * Accesseur de seanceSelected {@link #seanceSelected}.
     * @return retourne seanceSelected
     */
    public CycleSeanceFinalDTO getSeanceSelected() {
        return seanceSelected;
    }

    /**
     * Mutateur de seanceSelected {@link #seanceSelected}.
     * @param seanceSelected le seanceSelected to set
     */
    public void setSeanceSelected(CycleSeanceFinalDTO seanceSelected) {
        this.seanceSelected = seanceSelected;
    }
    
    /**
     * Retourne le titre de l'écran.
     * @return le titre.
     */
    public String getTitreEcran() {
        if (cycle == null) {
            return "Création automatique des séances";
        } else {
            return "Création automatique des séances pour : " + cycle.getTitre();
        }
        
    }

    /**
     * Accesseur de enseignantDTO {@link #enseignantDTO}.
     * @return retourne enseignantDTO
     */
    public EnseignantDTO getEnseignantDTO() {
        return enseignantDTO;
    }

    /**
     * Mutateur de enseignantDTO {@link #enseignantDTO}.
     * @param enseignantDTO le enseignantDTO to set
     */
    public void setEnseignantDTO(EnseignantDTO enseignantDTO) {
        this.enseignantDTO = enseignantDTO;
    }

    /**
     * Accesseur de listeEnseignement {@link #listeEnseignement}.
     * @return retourne listeEnseignement
     */
    public List<EnseignementDTO> getListeEnseignement() {
        return listeEnseignement;
    }

    /**
     * Mutateur de listeEnseignement {@link #listeEnseignement}.
     * @param listeEnseignement le listeEnseignement to set
     */
    public void setListeEnseignement(List<EnseignementDTO> listeEnseignement) {
        this.listeEnseignement = listeEnseignement;
    }

    /**
     * Accesseur de seanceTitre {@link #seanceTitre}.
     * @return retourne seanceTitre
     */
    public String getSeanceTitre() {
        return seanceTitre;
    }

    /**
     * Mutateur de seanceTitre {@link #seanceTitre}.
     * @param seanceTitre le seanceTitre to set
     */
    public void setSeanceTitre(String seanceTitre) {
        this.seanceTitre = seanceTitre;
    }

    /**
     * Accesseur de ligneSeanceId {@link #ligneSeanceId}.
     * @return retourne ligneSeanceId
     */
    public Integer getLigneSeanceId() {
        return ligneSeanceId;
    }

    /**
     * Mutateur de ligneSeanceId {@link #ligneSeanceId}.
     * @param ligneSeanceId le ligneSeanceId to set
     */
    public void setLigneSeanceId(Integer ligneSeanceId) {
        this.ligneSeanceId = ligneSeanceId;
    }

    /**
     * Accesseur de ligneEnseignementId {@link #ligneEnseignementId}.
     * @return retourne ligneEnseignementId
     */
    public Integer getLigneEnseignementId() {
        return ligneEnseignementId;
    }

    /**
     * Mutateur de ligneEnseignementId {@link #ligneEnseignementId}.
     * @param ligneEnseignementId le ligneEnseignementId to set
     */
    public void setLigneEnseignementId(Integer ligneEnseignementId) {
        this.ligneEnseignementId = ligneEnseignementId;
    }

    /**
     * Accesseur de enseignementDafaultDTO {@link #enseignementDafaultDTO}.
     * @return retourne enseignementDafaultDTO
     */
    public EnseignementDTO getEnseignementDafaultDTO() {
        return enseignementDafaultDTO;
    }

    /**
     * Mutateur de enseignementDafaultDTO {@link #enseignementDafaultDTO}.
     * @param enseignementDafaultDTO le enseignementDafaultDTO to set
     */
    public void setEnseignementDafaultDTO(EnseignementDTO enseignementDafaultDTO) {
        this.enseignementDafaultDTO = enseignementDafaultDTO;
    }

    
}
