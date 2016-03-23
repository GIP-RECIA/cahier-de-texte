/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ClasseBouchonDTO.java,v 1.1 2009/03/25 10:49:26 vibertd Exp $
 */

package org.crlr.web.bouchon;

import org.crlr.dto.application.base.GroupesClassesDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public final class ClasseBouchonDTO {
    
    /**
     * Constrcuteur.
     */
    private ClasseBouchonDTO() {
    }

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public static List<GroupesClassesDTO> getListeClasseDTO() {
        final List<GroupesClassesDTO> listeClasseDTO = new ArrayList<GroupesClassesDTO>();

        final GroupesClassesDTO groupesClassesDTO1 = new GroupesClassesDTO();
        groupesClassesDTO1.setId(1);
        groupesClassesDTO1.setCode("code classe 1");
        groupesClassesDTO1.setIntitule("intitule classe 1");
        listeClasseDTO.add(groupesClassesDTO1);

        final GroupesClassesDTO groupesClassesDTO2 = new GroupesClassesDTO();
        groupesClassesDTO2.setId(2);
        groupesClassesDTO2.setCode("code classe 2");
        groupesClassesDTO2.setIntitule("intitule classe 2");
        listeClasseDTO.add(groupesClassesDTO2);

        final GroupesClassesDTO groupesClassesDTO3 = new GroupesClassesDTO();
        groupesClassesDTO3.setId(3);
        groupesClassesDTO3.setCode("code classe 3");
        groupesClassesDTO3.setIntitule("intitule classe 3");
        listeClasseDTO.add(groupesClassesDTO3);

        return listeClasseDTO;
    }
}
