package org.crlr.dto.application.base;

public class PlageDTO {
	 /** Heure de début de la séance. */
    private Integer heureDebut;

    /** Heure de début de la séance. */
    private Integer heureFin;

    /** Minute de debut de la seance. */
    private Integer minuteDebut;

    /** Minute de fin de la seance. */
    private Integer minuteFin;
    
    private Boolean active;

    public PlageDTO() {
    	
    }
    
    public PlageDTO(final Integer heureDebut, final Integer minuteDebut,
            final Integer heureFin, final Integer minuteFin, final Boolean active) {
    	this.heureDebut = heureDebut;
        this.minuteDebut = minuteDebut;
        this.heureFin = heureFin;
        this.minuteFin = minuteFin;
        this.active = active;
	}
    
    public void copyTempsVers(PlageDTO plage) {
    	plage.heureDebut = heureDebut;
    	plage.minuteDebut = minuteDebut;
    	plage.heureFin = heureFin;
    	plage.minuteFin = minuteFin;
    }
    
    public void mettreTempsValeurs(PlageDTO plage) {
    	this.heureDebut = plage.heureDebut;
    	this.minuteDebut = plage.minuteDebut;
    	this.heureFin = plage.heureFin;
    	this.minuteFin = plage.minuteFin;
    }
    
	/**
	 * @return the heureDebut
	 */
	public Integer getHeureDebut() {
		return heureDebut;
	}

	/**
	 * @param heureDebut the heureDebut to set
	 */
	public void setHeureDebut(Integer heureDebut) {
		this.heureDebut = heureDebut;
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
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}
    
    
}
