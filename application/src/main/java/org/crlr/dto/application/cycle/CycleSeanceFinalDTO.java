package org.crlr.dto.application.cycle;

import java.util.ArrayList;
import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.web.dto.FileUploadDTO;

/**
 * Objet Seance de cycle sur lequel est ajoute un objet SeanceDTO représentant l'objet
 * Seance qui va etre créée sur le cahier de texte.
 * @author G-SAFIR-FRMP
 *
 */
public class CycleSeanceFinalDTO extends CycleSeanceDTO {

    /** Serial. */
    private static final long serialVersionUID = 2204508341222381283L;
    
    /** Objet seance DTO final. */
    private SeanceDTO seanceDTO;

    /** Indique que cette seance est cochee : on la garde lors de la sauvegarde. */
    private Boolean vraiOuFauxChecked;

    /** Enseignement DTO sélectionné. */
    private EnseignementDTO enseignementDTO;
    
    /**
     * Constructeur.
     */
    public CycleSeanceFinalDTO() {
        super();
        seanceDTO = new SeanceDTO();
    }

    /**
     * Mutateur de seanceDTO {@link #seanceDTO}.
     * @param seanceDTO le seanceDTO to set
     */
    public void setSeanceDTO(SeanceDTO seanceDTO) {
        this.seanceDTO = seanceDTO;
    }

    /**
     * Accesseur de seanceDTO {@link #seanceDTO}.
     * @return retourne seanceDTO
     */
    public SeanceDTO getSeanceDTO() {
        return seanceDTO;
    }

    /**
     * Accesseur de vraiOuFauxChecked {@link #vraiOuFauxChecked}.
     * @return retourne vraiOuFauxChecked
     */
    public Boolean getVraiOuFauxChecked() {
        return vraiOuFauxChecked;
    }

    /**
     * Mutateur de vraiOuFauxChecked {@link #vraiOuFauxChecked}.
     * @param vraiOuFauxChecked le vraiOuFauxChecked to set
     */
    public void setVraiOuFauxChecked(Boolean vraiOuFauxChecked) {
        this.vraiOuFauxChecked = vraiOuFauxChecked;
    }

    
    /**
     * Accesseur de enseignementDTO {@link #enseignementDTO}.
     * @return retourne enseignementDTO
     */
    public EnseignementDTO getEnseignementDTO() {
        return enseignementDTO;
    }

    /**
     * Mutateur de enseignementDTO {@link #enseignementDTO}.
     * @param enseignementDTO le enseignementDTO to set
     */
    public void setEnseignementDTO(EnseignementDTO enseignementDTO) {
        this.enseignementDTO = enseignementDTO;
    }

    /**
     * Initialise les champs de la seanceDTO à partir de ceux de l'objet.
     *  @param enseignantDTO enseignant associé à la seance.
     *  @param enseignementDTO enseignement sélectionné pour la seance.
     */
    public void initSeanceDTO(final EnseignantDTO enseignantDTO, final EnseignementDTO enseignementDTO) {
        seanceDTO = new SeanceDTO();
        seanceDTO.setAnnotations(getAnnotations());
        seanceDTO.setAnnotationsHTML(getAnnotationsHTML());
        seanceDTO.setDescription(getDescription());
        seanceDTO.setDescriptionHTML(getDescriptionHTML());
        seanceDTO.setEnseignantDTO(enseignantDTO);
        seanceDTO.setIntitule(getIntitule());
        
        setEnseignementDTO(enseignementDTO);
        
        // Liste des PJ
        final List<FileUploadDTO> listePJ = new ArrayList<FileUploadDTO>();
        for (final FileUploadDTO pj : getListePieceJointe()) {
            listePJ.add(pj);
        }
        seanceDTO.setFiles(listePJ);
        
        // Liste des devoirs
        final List<DevoirDTO> listeDevoir = new ArrayList<DevoirDTO>();
        for (final CycleDevoirDTO cycleDevoir : getListeCycleDevoir()) {
            final DevoirDTO devoirDTO = cycleDevoir.initDevoirDTO();
            listeDevoir.add(devoirDTO);
        }
        seanceDTO.setDevoirs(listeDevoir);
    }
    
    
}
