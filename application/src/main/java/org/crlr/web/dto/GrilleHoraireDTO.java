/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GrilleHoraireDTO.java,v 1.2 2010/03/31 08:08:44 ent_breyton Exp $
 */

package org.crlr.web.dto;

import java.io.Serializable;

/**
 * GrilleHoraireDTO.
 *
 * @author breytond.
 * @version $Revision: 1.2 $
 */
public class GrilleHoraireDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = -4652222292308853637L;
    
    /** identifiant. */
    private Integer id;
   
    /** heure de debut. */
    private Integer heureDebut;
    
    /** minute de debut. */
    private Integer minuteDebut;

    /** heure de debut. */
    private Integer heureFin;
    
    /** minute de debut. */
    private Integer minuteFin;
        
    private String titre;
    
    /**
     * Constructeur.
     */
    public GrilleHoraireDTO() {
        
    }

  

    /**
	 * @return the heureDebut
	 */
	public Integer getHeureDebut() {
		return heureDebut;
	}
	
	/**
	 * @return # de minutes 
	 */
	public int getDebutMinutes() {
	    return (heureDebut == null ? 0 : heureDebut) * 60 +
	            ( (minuteDebut == null) ? 0 : minuteDebut);
	}
	
	/**
	 * @return # de minutes 
	 */
	public int getFinMinutes() {
        return (heureFin == null ? 0 : heureFin) * 60 +
                ( (minuteFin == null) ? 0 : minuteFin);
    }



	/**
	 * @param heureDebut the heureDebut to set
	 */
	public void setHeureDebut(Integer heureDebut) {
		this.heureDebut = heureDebut;
	}



	/**
	 * @return the minuteDebut
	 */
	public Integer getMinuteDebut() {
		return minuteDebut;
	}



	/**
	 * @param minuteDebut the minuteDebut to set
	 */
	public void setMinuteDebut(Integer minuteDebut) {
		this.minuteDebut = minuteDebut;
	}



	/**
	 * @return the heureFin
	 */
	public Integer getHeureFin() {
		return heureFin;
	}



	/**
	 * @param heureFin the heureFin to set
	 */
	public void setHeureFin(Integer heureFin) {
		this.heureFin = heureFin;
	}



	/**
	 * @return the minuteFin
	 */
	public Integer getMinuteFin() {
		return minuteFin;
	}



	/**
	 * @param minuteFin the minuteFin to set
	 */
	public void setMinuteFin(Integer minuteFin) {
		this.minuteFin = minuteFin;
	}


    /**
     * Accesseur id.
     * @return le id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur de id.
     * @param id le id à modifier.
     */
    public void setId(Integer id) {
        this.id = id;
    }



    /**
     * @return the titre
     */
    public String getTitre() {
        return titre;
    }



    /**
     * @param titre the titre to set
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

}
