/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SaveSeanceQO.java,v 1.9 2009/04/29 08:52:22 vibertd Exp $
 */

package org.crlr.dto.application.seance;

import java.io.Serializable;

import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.SeanceDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
public class SaveSeanceQO extends SeanceDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = -2429912643601332223L;

    

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idEnseignement;
    
    /** DOCUMENTATION INCOMPLETE! */
    private String codeEnseignement;


    
    /** Mode. */
    private String mode;
    
    /** l'id de la séance sauvegardée. */
    private Integer oldIdSeance;
    
    /**
     * Année scolaire courante.
     */
    private AnneeScolaireDTO anneeScolaireDTO;

/**
     * Constructeur.
     */
    public SaveSeanceQO() {
    }


    /**
     * Accesseur idSequence.
     *
     * @return l'id de la séquence
     */
    public Integer getIdSequence() {
        return getSequenceDTO().getId();
    }

    /**
     * Mutateur idSequence.
     *
     * @param idSequence l'id de la séquence à modifier.
     */
    public void setIdSequence(Integer idSequence) {
        getSequenceDTO().setId(idSequence);
    }

    /**
     * Accesseur idEnseignant.
     *
     * @return l'id de l'enseignant
     */
    public Integer getIdEnseignant() {
        return getEnseignantDTO().getId();
    }

    /**
     * Mutateur idEnseignant.
     *
     * @param idEnseignant l'id de l'enseignant à modifier.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        getEnseignantDTO().setId(idEnseignant);
    }

    


    /**
     * Accesseur idEnseignement.
     *
     * @return idEnseignement
     */
    public Integer getIdEnseignement() {
        return idEnseignement;
    }

    /**
     * Mutateur idEnseignement.
     *
     * @param idEnseignement Le idEnseignement à modifier
     */
    public void setIdEnseignement(Integer idEnseignement) {
        this.idEnseignement = idEnseignement;
    }

    /**
     * Accesseur codeSequence.
     * @return codeSequence
     */
    public String getCodeSequence() {
        return getSequenceDTO().getCode();
    }

    /**
     * Mutateur codeSequence.
     * @param codeSequence Le codeSequence à modifier
     */
    public void setCodeSequence(String codeSequence) {
        getSequenceDTO().setCode(codeSequence);
    }

    /**
     * Accesseur codeEnseignement.
     * @return codeEnseignement
     */
    public String getCodeEnseignement() {
        return codeEnseignement;
    }

    /**
     * Mutateur codeEnseignement.
     * @param codeEnseignement Le codeEnseignement à modifier
     */
    public void setCodeEnseignement(String codeEnseignement) {
        this.codeEnseignement = codeEnseignement;
    }

    

    /**
     * Accesseur anneeScolaireDTO.
     * @return anneeScolaireDTO
     */
    public AnneeScolaireDTO getAnneeScolaireDTO() {
        return anneeScolaireDTO;
    }

    /**
     * Mutateur anneeScolaireDTO.
     * @param anneeScolaireDTO Le anneeScolaireDTO à modifier
     */
    public void setAnneeScolaireDTO(AnneeScolaireDTO anneeScolaireDTO) {
        this.anneeScolaireDTO = anneeScolaireDTO;
    }

    /**
     * Accesseur mode.
     * @return mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * Mutateur mode.
     * @param mode Le mode à modifier
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Accesseur oldIdSeance.
     * @return oldIdSeance
     */
    public Integer getOldIdSeance() {
        return oldIdSeance;
    }

    /**
     * Mutateur oldIdSeance.
     * @param oldIdSeance Le oldIdSeance à modifier
     */
    public void setOldIdSeance(Integer oldIdSeance) {
        this.oldIdSeance = oldIdSeance;
    }

    
        
}
