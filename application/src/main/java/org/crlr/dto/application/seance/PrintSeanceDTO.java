/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PrintSeanceDTO.java,v 1.7 2010/04/19 09:32:38 jerome.carriere Exp $
 */

package org.crlr.dto.application.seance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.sequence.PrintSequenceDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.7 $
  */
public class PrintSeanceDTO extends SeanceDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = 4133591058364531403L;
    
    
    
    private PrintSequenceDTO printSequenceDTO;
        
    /** Date courante. */
    private Date dateCourante;
    
    /** Permet d'afficher "Séance pour la classe xxx" OU "Séance pour le groupe xxx". */
    private String libelleClasseGroupe;
    
    /** Si la séance est déplié dans l'IHM. */
    private boolean open;
    
    private List<ImageDTO> listeImages;
        
    /**
     * Constructeur.
     */
    public PrintSeanceDTO() {
        
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
     * Accesseur de open.
     * @return le open
     */
    public Boolean getOpen() {
        return open;
    }

    /**
     * Mutateur de open.
     * @param open le open à modifier.
     */
    public void setOpen(Boolean open) {
        this.open = open;
    }


    /**
     * @return the listeImages
     */
    public List<ImageDTO> getListeImages() {
        return listeImages;
    }

    /**
     * @param listeImages the listeImages to set
     */
    public void setListeImages(List<ImageDTO> listeImages) {
        this.listeImages = listeImages;
    }

    /**
     * @return the printSequenceDTO
     */
    public PrintSequenceDTO getPrintSequenceDTO() {
        return printSequenceDTO;
    }

    /**
     * @param printSequenceDTO the printSequenceDTO to set
     */
    public void setPrintSequenceDTO(PrintSequenceDTO printSequenceDTO) {
        this.printSequenceDTO = printSequenceDTO;
        setSequenceDTO(printSequenceDTO);
    }




    
}
