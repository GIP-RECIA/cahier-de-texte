/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.dto.application.emploi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.utils.DateUtils;


/**
 * Un DTO qui reprensente les jours dans le planning mensuel.
 *
 * @author aurore.weber
 @version $Revision: 1.1 $
 */
public class JoursMensuelDTO implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Date dateJour;
    
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private List<SeanceDTO> seances;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private List<DevoirDTO> devoirs;
    
    /**
     * Un boolean qui permet de savoir s'il faut afficher le jour ou non (hors mois courant).
     */
    private Boolean vraiOuFauxAfficherJour;
    
    /**
     * Un boolean qui indique si c'est le jour actuel.
     */
    private Boolean vraiOuFauxJourCourant ;

    /**
     * Constructure d'un jour au sein d'un mois.
     * @param jourCourant le jour à créer.
     * @param vraiOuFauxAfficherJour vraiOuFauxAfficherJour.
     */
    public JoursMensuelDTO(Date jourCourant, Boolean vraiOuFauxAfficherJour) {
        this.dateJour = jourCourant;
        seances = new ArrayList<SeanceDTO>();
        devoirs = new ArrayList<DevoirDTO>();
        vraiOuFauxJourCourant = DateUtils.getAujourdhui().equals(jourCourant);
        this.vraiOuFauxAfficherJour = vraiOuFauxAfficherJour;
    }

    /**
     * Accesseur de dateJour.
     * @return le dateJour
     */
    public Date getDateJour() {
        return dateJour;
    }



    /**
     * Mutateur de dateJour.
     * @param dateJour le dateJour à modifier.
     */
    public void setDateJour(Date dateJour) {
        this.dateJour = dateJour;
    }



    /**
     * Accesseur de seances.
     *
     * @return le seances
     */
    public List<SeanceDTO> getSeances() {
        return seances;
    }

    /**
     * Mutateur de seances.
     *
     * @param seances le seances à modifier.
     */
    public void setSeances(List<SeanceDTO> seances) {
        this.seances = seances;
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
     * @param devoirs le devoirs à modifier.
     */
    public void setDevoirs(List<DevoirDTO> devoirs) {
        this.devoirs = devoirs;
    }

    
    
    /**
     * Accesseur de vraiOuFauxJourCourant.
     * @return le vraiOuFauxJourCourant
     */
    public Boolean getVraiOuFauxJourCourant() {
        return vraiOuFauxJourCourant;
    }



    /**
     * Mutateur de vraiOuFauxJourCourant.
     * @param vraiOuFauxJourCourant le vraiOuFauxJourCourant à modifier.
     */
    public void setVraiOuFauxJourCourant(Boolean vraiOuFauxJourCourant) {
        this.vraiOuFauxJourCourant = vraiOuFauxJourCourant;
    }



    /**
     * Accesseur de numeroJour.
     * @return le numeroJour
     */
    public Integer getNumeroJour() {
        return DateUtils.getChamp(dateJour, Calendar.DAY_OF_MONTH);
    }



    /**
     * Accesseur de vraiOuFauxAfficherJour.
     * @return le vraiOuFauxAfficherJour
     */
    public Boolean getVraiOuFauxAfficherJour() {
        return vraiOuFauxAfficherJour;
    }



    /**
     * Mutateur de vraiOuFauxAfficherJour.
     * @param vraiOuFauxAfficherJour le vraiOuFauxAfficherJour à modifier.
     */
    public void setVraiOuFauxAfficherJour(Boolean vraiOuFauxAfficherJour) {
        this.vraiOuFauxAfficherJour = vraiOuFauxAfficherJour;
    }

    

    
    
    
}
