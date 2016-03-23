/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EmploiMultipleJoursDTO.java,v 1.1 2010/04/19 09:32:38 jerome.carriere Exp $
 */

package org.crlr.web.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crlr.dto.application.base.PlageDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class EmploiMultipleJoursDTO extends PlageDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = -6203227489794048777L;

    /** DOCUMENTATION INCOMPLETE! */
    private Map<String, List<DetailJourEmploiDTO>> map =
        new HashMap<String, List<DetailJourEmploiDTO>>();

        

/**
     * Constructeur.
     */
    public EmploiMultipleJoursDTO() {
    }

/**
     * Constructeur.
     * @param heureDebut heure de début.
     * @param minuteDebut minute début.
     * @param heureFin heure de fin.
     * @param minuteFin minute de fin.
     * @param active plage horaire active.
     */
    public EmploiMultipleJoursDTO(final Integer heureDebut, final Integer minuteDebut,
                          final Integer heureFin, final Integer minuteFin, final Boolean active) {
        super(heureDebut, minuteDebut, heureFin, minuteFin, active);
    }

    



    /**
     * Accesseur map.
     * @return le map
     */
    public Map<String, List<DetailJourEmploiDTO>> getMap() {
        return map;
    }

    /**
     * Mutateur de map.
     * @param map le map à modifier.
     */
    public void setMap(Map<String, List<DetailJourEmploiDTO>> map) {
        this.map = map;
    }
}
