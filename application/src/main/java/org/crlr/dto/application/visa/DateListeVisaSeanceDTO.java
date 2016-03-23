/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TriDTO.java,v 1.2 2009/04/22 13:14:23 ent_breyton Exp $
 */

package org.crlr.dto.application.visa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Classe DTO de type DateListeVisaSeanceDTO.
 * @author G-SAFIR-FRMP
 */
public class DateListeVisaSeanceDTO {
    
    /** Date de seance. */
    private Date date;
    
    /** Liste des visaSeance correspondant a cette date. */
    private List<ResultatRechercheVisaSeanceDTO> listeVisaSeance;

    
    /**
     * Constructeur par defaut.
     */
    public DateListeVisaSeanceDTO() {
        listeVisaSeance = new ArrayList<ResultatRechercheVisaSeanceDTO>();
    }

    /**
     * Accesseur de date {@link #date}.
     * @return retourne date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Mutateur de date {@link #date}.
     * @param date le date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Accesseur de listeVisaSeance {@link #listeVisaSeance}.
     * @return retourne listeVisaSeance
     */
    public List<ResultatRechercheVisaSeanceDTO> getListeVisaSeance() {
        return listeVisaSeance;
    }

    /**
     * Mutateur de listeVisaSeance {@link #listeVisaSeance}.
     * @param listeVisaSeance le listeVisaSeance to set
     */
    public void setListeVisaSeance(
            List<ResultatRechercheVisaSeanceDTO> listeVisaSeance) {
        this.listeVisaSeance = listeVisaSeance;
    }


    
    
    
}
