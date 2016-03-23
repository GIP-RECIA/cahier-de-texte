/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GroupeBouchonDTO.java,v 1.1 2009/03/25 10:49:26 vibertd Exp $
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
public final class GroupeBouchonDTO {
/**
     * Constrcuteur.
     */
    private GroupeBouchonDTO() {
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public static List<GroupesClassesDTO> getListeGroupeDTO() {
        final List<GroupesClassesDTO> listeGroupeDTO = new ArrayList<GroupesClassesDTO>();

        final GroupesClassesDTO groupesClassesDTO1 = new GroupesClassesDTO();
        groupesClassesDTO1.setId(1);
        groupesClassesDTO1.setCode("code grp 1");
        groupesClassesDTO1.setIntitule("intitule grp 1");
        listeGroupeDTO.add(groupesClassesDTO1);

        final GroupesClassesDTO groupesClassesDTO2 = new GroupesClassesDTO();
        groupesClassesDTO2.setId(2);
        groupesClassesDTO2.setCode("code grp 2");
        groupesClassesDTO2.setIntitule("intitule grp 2");
        listeGroupeDTO.add(groupesClassesDTO2);

        final GroupesClassesDTO groupesClassesDTO3 = new GroupesClassesDTO();
        groupesClassesDTO3.setId(3);
        groupesClassesDTO3.setCode("code grp 3");
        groupesClassesDTO3.setIntitule("intitule grp 3");
        listeGroupeDTO.add(groupesClassesDTO3);

        return listeGroupeDTO;
    }
}
