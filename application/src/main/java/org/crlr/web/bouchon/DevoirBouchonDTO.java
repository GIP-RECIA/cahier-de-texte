/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirBouchonDTO.java,v 1.2 2009/04/03 14:12:29 vibertd Exp $
 */

package org.crlr.web.bouchon;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.utils.DateUtils;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public final class DevoirBouchonDTO {
    
    /**
     * 
     * Constructeur.
     */
    private DevoirBouchonDTO() {
        
    }
    
    /**
     * Bouchon resultat recherche séance DTO.
     *
     * @return une liste de séance DTo
     */
    public static List<DevoirDTO> getListeDevoirDTO() {
        final List<DevoirDTO> listeDevoirDTO =
            new ArrayList<DevoirDTO>();
        
        final DevoirDTO devoirDTO1 = new DevoirDTO();
        devoirDTO1.setId(1);
        devoirDTO1.setCode("code 1");
        devoirDTO1.setDateRemise(DateUtils.creer(2009, 03, 21));
        devoirDTO1.setDescription("description 1");
        devoirDTO1.setIntitule("intitulé 1");
        listeDevoirDTO.add(devoirDTO1);
        
        final DevoirDTO devoirDTO2 = new DevoirDTO();
        devoirDTO2.setId(2);
        devoirDTO2.setCode("code 2");
        devoirDTO2.setDateRemise(DateUtils.creer(2009, 05, 22));
        devoirDTO2.setDescription("description 2");
        devoirDTO2.setIntitule("intitulé 2");
        listeDevoirDTO.add(devoirDTO2);
        
        return listeDevoirDTO;
    }
}
