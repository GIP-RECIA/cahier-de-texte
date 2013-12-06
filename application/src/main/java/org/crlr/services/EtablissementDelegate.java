/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EtablissementDelegate.java,v 1.5 2010/04/29 11:20:51 jerome.carriere Exp $
 */

package org.crlr.services;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EtablissementComplementDTO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.OuvertureQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.EtablissementFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * EtablissementDelegate.
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
@Service
public class EtablissementDelegate implements EtablissementService {
    /** Injection spring automatique de la seanceFacade. */
    @Autowired
    private EtablissementFacadeService etablissementFacadeService;   

    /**
     * Mutateur de etablissementFacadeService.
     * @param etablissementFacadeService le etablissementFacadeService à modifier.
     */
    public void setEtablissementFacadeService(
            EtablissementFacadeService etablissementFacadeService) {
        this.etablissementFacadeService = etablissementFacadeService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveEtablissementJoursOuvres(
            EtablissementDTO etablissementQO) throws MetierException {       
        return etablissementFacadeService.saveEtablissementJoursOuvres(etablissementQO);
    }

    /**
     * {@inheritDoc}
     */
    public EtablissementComplementDTO findDonneeComplementaireEtablissement(
            Integer id) {        
        return etablissementFacadeService.findDonneeComplementaireEtablissement(id);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveComplementEtablissement(
            EtablissementDTO etablissementQO) throws MetierException {      
        return etablissementFacadeService.saveComplementEtablissement(etablissementQO);
    }

    /**
     * {@inheritDoc}
     */
    public String findAlternanceSemaine(Integer idEtablissement) {       
        return etablissementFacadeService.findAlternanceSemaine(idEtablissement);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveEtablissementAlternance(
            EtablissementDTO etablissementQO) throws MetierException {        
        return etablissementFacadeService.saveEtablissementAlternance(etablissementQO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveEtablissementOuverture(
            OuvertureQO ouvertureQO) throws MetierException {        
        return etablissementFacadeService.saveEtablissementOuverture(ouvertureQO);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<String> findHorairesCoursEtablissement(final Integer id)
            throws MetierException {
        return etablissementFacadeService.findHorairesCoursEtablissement(id);
    }
    
    /**
     * {@inheritDoc}
     */
    public String findJourOuvreEtablissementParCode(final String siren, final Boolean archive, final String exercice){
        return etablissementFacadeService.findJourOuvreEtablissementParCode(siren,archive, exercice);
    }
    
    /**
     * {@inheritDoc}
     */
    public Integer findIdEtablissementParCode(final String siren, final Boolean archive, final String exercice) {
        return etablissementFacadeService.findIdEtablissementParCode(siren, archive, exercice);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<EtablissementDTO> findEtablissement(final Integer id) {
        return etablissementFacadeService.findEtablissement(id);
    }
    
    public ResultatDTO<Boolean> checkSaisieSimplifieeEtablissement(final Integer idEtablissement,
            final Integer idEnseignant) {
        return etablissementFacadeService.checkSaisieSimplifieeEtablissement(idEtablissement, idEnseignant);
    }
}

