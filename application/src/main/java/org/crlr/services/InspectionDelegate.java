/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.UserDTOForList;
import org.crlr.dto.application.inspection.DroitInspecteurDTO;
import org.crlr.dto.application.inspection.RechercheDroitInspecteurQO;
import org.crlr.dto.application.inspection.SaveDroitInspecteurQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.InspectionFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
@Service
public class InspectionDelegate implements InspectionService {
    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private InspectionFacadeService inspectionFacadeService;

    /**
     * Mutateur de inspectionFacadeService.
     *
     * @param inspectionFacadeService le inspectionFacadeService à modifier.
     */
    public void setInspectionFacadeService(InspectionFacadeService inspectionFacadeService) {
        this.inspectionFacadeService = inspectionFacadeService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<DroitInspecteurDTO>> findDroitsInspection(RechercheDroitInspecteurQO rechercheDroitInspecteurQO) {
        return inspectionFacadeService.findDroitsInspection(rechercheDroitInspecteurQO);
    }

    /**
     * {@inheritDoc}
     */
    public List<UserDTOForList> findListeInspecteurs() {
        return inspectionFacadeService.findListeInspecteurs();
    }

    /**
     * {@inheritDoc}
     */
    public List<EnseignantDTO> findListeEnseignants(Integer idEtablissement) {
        return inspectionFacadeService.findListeEnseignants(idEtablissement);
    }

    /**
     * {@inheritDoc}
     */
    public void saveDroit(SaveDroitInspecteurQO saveDroitInspecteurQO)
                                 throws MetierException {
        inspectionFacadeService.saveDroit(saveDroitInspecteurQO);
    }
    
    /**
     * {@inheritDoc}
     */
    public void saveDroitsListe(List<DroitInspecteurDTO> listeDroit) throws MetierException {
        inspectionFacadeService.saveDroitsListe(listeDroit);
    }
}
