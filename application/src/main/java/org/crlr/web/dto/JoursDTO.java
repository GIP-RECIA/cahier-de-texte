/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: JoursDTO.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.dto;

import org.crlr.dto.application.devoir.DetailJourDTO;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class JoursDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = -6203227489794048777L;

    /**
     * Map qui associe une chaine de carcactère (jour) à un detail par jours.
     */
    private Map<String, DetailJourDTO> map = new HashMap<String, DetailJourDTO>();

    /**
     * Accesseur de map.
     * @return le map
     */
    public Map<String, DetailJourDTO> getMap() {
        return map;
    }

    /**
     * Mutateur de map.
     * @param map le map à modifier.
     */
    public void setMap(Map<String, DetailJourDTO> map) {
        this.map = map;
    }

    
}
