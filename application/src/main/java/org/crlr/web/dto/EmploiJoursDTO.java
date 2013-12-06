/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EmploiJoursDTO.java,v 1.2 2010/04/12 04:30:13 jerome.carriere Exp $
 */

package org.crlr.web.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.crlr.dto.application.base.PlageDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class EmploiJoursDTO extends PlageDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = -6203227489794048777L;

    /** DOCUMENTATION INCOMPLETE! */
    private Map<String, DetailJourEmploiDTO> map =
        new HashMap<String, DetailJourEmploiDTO>();

   

/**
     * Constructeur.
     */
    public EmploiJoursDTO() {}

/**
     * Constructeur.
     * @param heureDebut heure de début.
     * @param minuteDebut minute début.
     * @param heureFin heure de fin.
     * @param minuteFin minute de fin.
     * @param active plage horaire active.
     */
    public EmploiJoursDTO(final Integer heureDebut, final Integer minuteDebut,
                          final Integer heureFin, final Integer minuteFin, final Boolean active) {
        super(heureDebut, minuteDebut, heureFin, minuteFin, active);
    }

    
   

	/**
     * DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public Map<String, DetailJourEmploiDTO> getMap() {
        return map;
    }

    /**
     * DOCUMENT ME!
     *
     * @param map DOCUMENTATION INCOMPLETE!
     */
    public void setMap(Map<String, DetailJourEmploiDTO> map) {
        this.map = map;
    }


}
