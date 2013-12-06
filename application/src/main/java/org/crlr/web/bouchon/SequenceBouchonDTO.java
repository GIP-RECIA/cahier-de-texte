/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequenceBouchonDTO.java,v 1.1 2009/03/25 10:49:26 vibertd Exp $
 */

package org.crlr.web.bouchon;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.base.SequenceDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public final class SequenceBouchonDTO {
/**
     * Constructeur.
     */
    private SequenceBouchonDTO() {
    }

    /**
     * Bouchon resultat recherche séance DTO.
     *
     * @return une liste de séance DTo
     */
    public static List<SequenceDTO> getListeSequenceDTO() {
        final List<SequenceDTO> listeSequenceDTO = new ArrayList<SequenceDTO>();

        final SequenceDTO sequenceDTO1 = new SequenceDTO();
        sequenceDTO1.setId(1);
        sequenceDTO1.setCode("code1");
        sequenceDTO1.setDescription("description1");
        sequenceDTO1.setIntitule("intitule1");
        listeSequenceDTO.add(sequenceDTO1);
        
        final SequenceDTO sequenceDTO2 = new SequenceDTO();
        sequenceDTO2.setId(2);
        sequenceDTO2.setCode("code2");
        sequenceDTO2.setDescription("description2");
        sequenceDTO2.setIntitule("intitule2");
        listeSequenceDTO.add(sequenceDTO2);
        
        final SequenceDTO sequenceDTO3 = new SequenceDTO();
        sequenceDTO3.setId(3);
        sequenceDTO3.setCode("code3");
        sequenceDTO3.setDescription("description3");
        sequenceDTO3.setIntitule("intitule3");
        listeSequenceDTO.add(sequenceDTO3);
        
        final SequenceDTO sequenceDTO4 = new SequenceDTO();
        sequenceDTO4.setId(4);
        sequenceDTO4.setCode("code4");
        sequenceDTO4.setDescription("description4");
        sequenceDTO4.setIntitule("intitule4");
        listeSequenceDTO.add(sequenceDTO4);

        return listeSequenceDTO;
    }
}
