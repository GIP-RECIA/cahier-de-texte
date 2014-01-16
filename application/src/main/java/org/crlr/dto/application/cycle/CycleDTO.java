package org.crlr.dto.application.cycle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.utils.StringUtils;

/**
 * Sequence pedagogique. 
 * @author G-SAFIR-FRMP
 *
 */
public class CycleDTO  implements Serializable {

    /**  Serial. */
    private static final long serialVersionUID = -2206952699339613728L;

    /** Identfiant. */
    private Integer id;

    /** Enseignant createur de la sequence pedagogique. */
    private EnseignantDTO enseignantDTO;

    /** Titre de sequence pedagogique. */
    private String titre;

    /** Objectif. */
    private String objectif;
    
    /** Pre-requis. */
    private String prerequis;

    /** Description. */
    private String description;

    /** Liste des seances. */
    private List<CycleSeanceDTO> listeSeance;
    
    /** Liste des groupes associes. */
    private List<GroupeDTO> listeGroupe;

    /**
     * Constructeur par defaut.
     */
    public CycleDTO() {
        super();
        listeGroupe = new ArrayList<GroupeDTO>();
        listeSeance = new ArrayList<CycleSeanceDTO>();
    }

    /**
     * Accesseur de id {@link #id}.
     * @return retourne id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur de id {@link #id}.
     * @param id le id to set
     */
    public void setId(Integer id) {
        this.id = id;
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
     * Accesseur de titre {@link #titre}.
     * @return retourne titre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Mutateur de titre {@link #titre}.
     * @param titre le titre to set
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Accesseur de objectif {@link #objectif}.
     * @return retourne objectif
     */
    public String getObjectif() {
        return objectif;
    }
    /**
     * Accesseur de objectif {@link #objectif} en version courte pour etre affichée
     * dans une liste.
     * @return retourne objectif
     */
    public String getObjectifCourt() {
        if (objectif != null) {
            return StringUtils.truncateHTMLString(objectif.replace("\n", "<br/>"),100);
        } else {
            return "";
        }
    }

    /**
     * Mutateur de objectif {@link #objectif}.
     * @param objectif le objectif to set
     */
    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    /**
     * Accesseur de prerequis {@link #prerequis}.
     * @return retourne prerequis
     */
    public String getPrerequis() {
        return prerequis;
    }

    /**
     * Accesseur de prerequis {@link #prerequis} en version courte pour etre affichée
     * dans une liste.
     * @return retourne prerequis
     */
    public String getPrerequisCourt() {
        if (prerequis != null) {
            return StringUtils.truncateHTMLString(prerequis.replace("\n", "<br/>"),100);
        } else {
            return "";
        }
            
    }
    
    /**
     * Mutateur de prerequis {@link #prerequis}.
     * @param prerequis le prerequis to set
     */
    public void setPrerequis(String prerequis) {
        this.prerequis = prerequis;
    }

    /**
     * Accesseur de description {@link #description}.
     * @return retourne description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutateur de description {@link #description}.
     * @param description le description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Accesseur de listeSeance {@link #listeSeance}.
     * @return retourne listeSeance
     */
    public List<CycleSeanceDTO> getListeSeance() {
        return listeSeance;
    }

    /**
     * Mutateur de listeSeance {@link #listeSeance}.
     * @param listeSeance le listeSeance to set
     */
    public void setListeSeance(List<CycleSeanceDTO> listeSeance) {
        this.listeSeance = listeSeance;
    }

    /**
     * Accesseur de listeGroupe {@link #listeGroupe}.
     * @return retourne listeGroupe
     */
    public List<GroupeDTO> getListeGroupe() {
        return listeGroupe;
    }

    /**
     * Mutateur de listeGroupe {@link #listeGroupe}.
     * @param listeGroupe le listeGroupe to set
     */
    public void setListeGroupe(List<GroupeDTO> listeGroupe) {
        this.listeGroupe = listeGroupe;
    }
    
    
    
    
}
