/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PrintSequenceDTO.java,v 1.4 2009/07/30 15:09:26 ent_breyton Exp $
 */

package org.crlr.dto.application.sequence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.seance.PrintSeanceDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class PrintSequenceDTO extends SequenceDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = 4133591058364531403L;
    
    /** Code de la séquence. */
    private String codeSequence;
    
    
    private EnseignantDTO enseignantDTO;
   
    private List<PrintSeanceDTO> listeSeances;
   

    /** La désignation de l'enseignement. */
    private String designationEnseignement;
   
    

    /** Date courante. */
    private Date dateCourante;
    
    /** Permet d'afficher "Séance pour la classe xxx" OU "Séance pour le groupe xxx". */
    private String libelleClasseGroupe;
    
    private Boolean open;
    
    /**
     * Constructeur.
     */
    public PrintSequenceDTO() {
        listeSeances = new ArrayList<PrintSeanceDTO>();
        enseignantDTO = new EnseignantDTO();
        open = false;
    }

  

    /**
     * Accesseur codeSequence.
     * @return le codeSequence
     */
    public String getCodeSequence() {
        return codeSequence;
    }

    /**
     * Mutateur de codeSequence.
     * @param codeSequence le codeSequence à modifier.
     */
    public void setCodeSequence(String codeSequence) {
        this.codeSequence = codeSequence;
    }

  

    /**
     * Accesseur dateCourante.
     * @return le dateCourante
     */
    public Date getDateCourante() {
        return dateCourante;
    }

    /**
     * Mutateur de dateCourante.
     * @param dateCourante le dateCourante à modifier.
     */
    public void setDateCourante(Date dateCourante) {
        this.dateCourante = dateCourante;
    }

    /**
     * Accesseur libelleClasseGroupe.
     * @return le libelleClasseGroupe
     */
    public String getLibelleClasseGroupe() {
        return libelleClasseGroupe;
    }

    /**
     * Mutateur de libelleClasseGroupe.
     * @param libelleClasseGroupe le libelleClasseGroupe à modifier.
     */
    public void setLibelleClasseGroupe(String libelleClasseGroupe) {
        this.libelleClasseGroupe = libelleClasseGroupe;
    }

   



    /**
     * @return the enseignantDTO
     */
    public EnseignantDTO getEnseignantDTO() {
        return enseignantDTO;
    }



    /**
     * @param enseignantDTO the enseignantDTO to set
     */
    public void setEnseignantDTO(EnseignantDTO enseignantDTO) {
        this.enseignantDTO = enseignantDTO;
    }



    /**
     * @return the listeSeances
     */
    public List<PrintSeanceDTO> getListeSeances() {
        return listeSeances;
    }



    /**
     * @param listeSeances the listeSeances to set
     */
    public void setListeSeances(List<PrintSeanceDTO> listeSeances) {
        this.listeSeances = listeSeances;
    }



    /**
     * @return the open
     */
    public Boolean getOpen() {
        return open;
    }



    /**
     * @param open the open to set
     */
    public void setOpen(Boolean open) {
        this.open = open;
    }



    /**
     * @return the designationEnseignement
     */
    public String getDesignationEnseignement() {
        return designationEnseignement;
    }



    /**
     * @param designationEnseignement the designationEnseignement to set
     */
    public void setDesignationEnseignement(String designationEnseignement) {
        this.designationEnseignement = designationEnseignement;
    }




    
}
