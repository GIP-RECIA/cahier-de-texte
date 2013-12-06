/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.dto.application.emploi;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.crlr.dto.application.base.TypeJour;
import org.crlr.utils.DateUtils;

/**
 * Un DTO qui represente une semaine dans le planning mensuel.
 *
 * @author aurore.weber
 * @version $Revision: 1.1 $
  */
public class SemaineDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Map<String, JoursMensuelDTO> map;
    private Date debutSemaine;
    private Date finSemaine;
    
    /**
     * Constructeur de la semaine à partir du lundi.
     * @param debutSemaine la date du debut de la semaine.
     * @param dateDebutMois la date de debut du mois.
     * @param dateFinMois la date de fin du mois.
     */
    public SemaineDTO(Date debutSemaine, Date dateDebutMois, Date dateFinMois) {
        this.debutSemaine = debutSemaine; 
        this.finSemaine = DateUtils.ajouter(debutSemaine, Calendar.DAY_OF_WEEK, 6);
        
        Date jourCourant = (Date) debutSemaine.clone();
        map = new HashMap<String, JoursMensuelDTO>();
        for (TypeJour jour : TypeJour.values()) {
            final JoursMensuelDTO jourMensuelDTO = new JoursMensuelDTO(jourCourant, DateUtils.isBetween(jourCourant, dateDebutMois, dateFinMois)); 
            map.put(jour.name(), jourMensuelDTO);
            jourCourant = DateUtils.ajouter(jourCourant, Calendar.DAY_OF_WEEK, 1);
        }
        
        
    }

    /**
     * Accesseur de map.
     * @return le map
     */
    public Map<String, JoursMensuelDTO> getMap() {
        return map;
    }

    /**
     * Mutateur de map.
     * @param map le map à modifier.
     */
    public void setMap(Map<String, JoursMensuelDTO> map) {
        this.map = map;
    }

    /**
     * Accesseur de debutSemaine.
     * @return le debutSemaine
     */
    public Date getDebutSemaine() {
        return debutSemaine;
    }

    /**
     * Accesseur de finSemaine.
     * @return le finSemaine
     */
    public Date getFinSemaine() {
        return finSemaine;
    }

        
    
    
}
