/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.seance.RechercheVisaSeanceQO;
import org.crlr.dto.application.visa.DateListeVisaSeanceDTO;
import org.crlr.dto.application.visa.RechercheVisaQO;
import org.crlr.dto.application.visa.ResultatRechercheVisaSeanceDTO;
import org.crlr.dto.application.visa.VisaDTO;
import org.crlr.dto.application.visa.VisaEnseignantDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.VisaFacadeService;
import org.crlr.utils.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
@Service
public class VisaDelegate implements VisaService {
    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private VisaFacadeService visaFacadeService;
 
    /**
     * Mutateur de visaFacadeService.
     * @param visaFacadeService le visaFacadeService à modifier.
     */
    public void setVisaFacadeService(VisaFacadeService visaFacadeService) {
        this.visaFacadeService = visaFacadeService;
    }

  
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<VisaDTO>> findListeVisa(final RechercheVisaQO rechercheVisa) {
        return visaFacadeService.findListeVisa(rechercheVisa);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Boolean> saveListeVisa(final List<VisaDTO> listeVisa) throws MetierException {
        return visaFacadeService.saveListeVisa(listeVisa);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<DateListeVisaSeanceDTO>> findVisaSeance(RechercheVisaSeanceQO rechercheSeanceQO) 
        throws MetierException {
        return visaFacadeService.findVisaSeance(rechercheSeanceQO);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Boolean> saveListeVisaSeance(final List<ResultatRechercheVisaSeanceDTO> listeVisaSeance) throws MetierException {
        return visaFacadeService.saveListeVisaSeance(listeVisaSeance);
    }
    
  
}
