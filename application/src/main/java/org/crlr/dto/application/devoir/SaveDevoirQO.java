/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SaveDevoirQO.java,v 1.1 2010/04/21 09:05:54 ent_breyton Exp $
 */

package org.crlr.dto.application.devoir;

import java.io.Serializable;
import java.util.Date;

/**
 * SaveDevoirQO.
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class SaveDevoirQO implements Serializable {
    /** Serail uid. */
    private static final long serialVersionUID = 572121141415702461L;

    /** les infos du devoirs. */
    private DevoirDTO devoirDTO;

    /** la date de la séance. */
    private Date dateSeance;

    /** La date de fin de l'année scolaire. */
    private Date dateFinAnneeScolaire;

    /**
     * Accesseur devoirDTO.
     * @return le devoirDTO
     */
    public DevoirDTO getDevoirDTO() {
        return devoirDTO;
    }

    /**
     * Mutateur de devoirDTO.
     * @param devoirDTO le devoirDTO à modifier.
     */
    public void setDevoirDTO(DevoirDTO devoirDTO) {
        this.devoirDTO = devoirDTO;
    }

    /**
     * Accesseur dateSeance.
     * @return le dateSeance
     */
    public Date getDateSeance() {
        return dateSeance;
    }

    /**
     * Mutateur de dateSeance.
     * @param dateSeance le dateSeance à modifier.
     */
    public void setDateSeance(Date dateSeance) {
        this.dateSeance = dateSeance;
    }

    /**
     * Accesseur dateFinAnneeScolaire.
     * @return le dateFinAnneeScolaire
     */
    public Date getDateFinAnneeScolaire() {
        return dateFinAnneeScolaire;
    }

    /**
     * Mutateur de dateFinAnneeScolaire.
     * @param dateFinAnneeScolaire le dateFinAnneeScolaire à modifier.
     */
    public void setDateFinAnneeScolaire(Date dateFinAnneeScolaire) {
        this.dateFinAnneeScolaire = dateFinAnneeScolaire;
    }
}
