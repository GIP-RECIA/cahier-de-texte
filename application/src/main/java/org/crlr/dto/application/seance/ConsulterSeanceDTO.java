/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConsulterSeanceDTO.java,v 1.2 2009/04/22 13:14:23 ent_breyton Exp $
 */

package org.crlr.dto.application.seance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.web.dto.FileUploadDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class ConsulterSeanceDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = 5062544339713241466L;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Date date;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private String description;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private String annotations;
    
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private String intitule;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Integer heureDebut;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Integer heureFin;
    
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Integer minuteDebut;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Integer minuteFin;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private List<DevoirDTO> listeDevoirDTO;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private SequenceDTO sequenceDTO;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private List<FileUploadDTO> listePieceJointeDTO;

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
     * Accesseur listeDevoirDTO.
     *
     * @return listeDevoirDTO
     */
    public List<DevoirDTO> getListeDevoirDTO() {
        return listeDevoirDTO;
    }

    /**
     * Mutateur listeDevoirDTO.
     *
     * @param listeDevoirDTO Le listeDevoirDTO à modifier
     */
    public void setListeDevoirDTO(List<DevoirDTO> listeDevoirDTO) {
        this.listeDevoirDTO = listeDevoirDTO;
    }

    /**
     * Accesseur sequenceDTO.
     *
     * @return sequenceDTO
     */
    public SequenceDTO getSequenceDTO() {
        return sequenceDTO;
    }

    /**
     * Mutateur sequenceDTO.
     *
     * @param sequenceDTO Le sequenceDTO à modifier
     */
    public void setSequenceDTO(SequenceDTO sequenceDTO) {
        this.sequenceDTO = sequenceDTO;
    }

    /**
     * Accesseur listePieceJointeDTO.
     *
     * @return listePieceJointeDTO
     */
    public List<FileUploadDTO> getListePieceJointeDTO() {
        return listePieceJointeDTO;
    }

    /**
     * Mutateur listePieceJointeDTO.
     *
     * @param listePieceJointeDTO Le listePieceJointeDTO à modifier
     */
    public void setListePieceJointeDTO(List<FileUploadDTO> listePieceJointeDTO) {
        this.listePieceJointeDTO = listePieceJointeDTO;
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
     * Accesseur heureDebut.
     * @return le heureDebut.
     */
    public Integer getHeureDebut() {
        return heureDebut;
    }

    /**
     * Mutateur heureDebut.
     * @param heureDebut le heureDebut à modifier.
     */
    public void setHeureDebut(Integer heureDebut) {
        this.heureDebut = heureDebut;
    }

    /**
     * Accesseur heureFin.
     * @return le heureFin.
     */
    public Integer getHeureFin() {
        return heureFin;
    }

    /**
     * Mutateur heureFin.
     * @param heureFin le heureFin à modifier.
     */
    public void setHeureFin(Integer heureFin) {
        this.heureFin = heureFin;
    }

    /**
     * Accesseur de annotations {@link #annotations}.
     * @return retourne annotations
     */
    public String getAnnotations() {
        return annotations;
    }

    /**
     * Mutateur de annotations {@link #annotations}.
     * @param annotations le annotations to set
     */
    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }
    
}
