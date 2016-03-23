/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ClasseServiceTest.java,v 1.2 2009/04/16 14:17:06 weberent Exp $
 */

package org.crlr.test.services;

import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.RechercheGroupeClassePopupQO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.GroupeClasseService;
import org.crlr.test.AbstractMetierTest;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class ClasseServiceTest extends AbstractMetierTest {
    
    private GroupeClasseService groupeClasseService;

    /**
     * Mutateur groupeClasseService.
     * @param groupeClasseService Le groupeClasseService à modifier
     */
    public void setGroupeClasseService(GroupeClasseService groupeClasseService) {
        this.groupeClasseService = groupeClasseService;
    }
    
    /**
     * Test la recherche de seance.
     */
    public void testFindClassePopup() {
        final RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO = new RechercheGroupeClassePopupQO();
        rechercheGroupeClassePopupQO.setIdEnseignant(1);
        rechercheGroupeClassePopupQO.setTypeGroupeClasse(TypeGroupe.CLASSE);
        try {
            final ResultatDTO<List<GroupesClassesDTO>> resultat = groupeClasseService.findGroupeClassePopup(rechercheGroupeClassePopupQO);
            for(GroupesClassesDTO groupesClassesDTO : resultat.getValeurDTO()) {
                log.debug("Id classe : {0}", groupesClassesDTO.getId());
                log.debug("Code classe : {0}", groupesClassesDTO.getCode());
            }
        } catch (MetierException e) {
            log.debug("{0}", e.getMessage());
        }
    }
}
