/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConfidentialiteDelegate.java,v 1.8 2010/05/20 08:24:50 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.List;
import java.util.Set;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.CheckSaisieSimplifieeQO;
import org.crlr.dto.application.base.ElevesParentDTO;
import org.crlr.dto.application.base.EtablissementAccessibleQO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.securite.AuthentificationQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.ConfidentialiteFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ConfidentialiteDelegate.
 *
 * @author breytond.
 * @version $Revision: 1.8 $
 */
@Service
public class ConfidentialiteDelegate implements ConfidentialiteService {
    /** Injection. */
    @Autowired
    private ConfidentialiteFacadeService confidentialiteFacadeService;

    /**
     * Positionne le service de données.
     *
     * @param confidentialiteFacadeService confidentialiteFacadeService
     */
    public void setConfidentialiteFacadeService(ConfidentialiteFacadeService confidentialiteFacadeService) {
        this.confidentialiteFacadeService = confidentialiteFacadeService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<UtilisateurDTO> initialisationAuthentification(final AuthentificationQO authentificationQO)
        throws MetierException {
        return confidentialiteFacadeService.initialisationAuthentification(authentificationQO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<ElevesParentDTO> findEleveDuParent(final Set<String> listeUidEleve) {
        return confidentialiteFacadeService.findEleveDuParent(listeUidEleve);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<EtablissementDTO>> findListeEtablissement()
        throws MetierException {
        return confidentialiteFacadeService.findListeEtablissement();
    }

    /**
     * {@inheritDoc} 
     */
    public ResultatDTO<List<EtablissementDTO>> findListeEtablissementEnseignant(final EtablissementAccessibleQO etablissementAccessibleQO) {
        return confidentialiteFacadeService.findListeEtablissementEnseignant(etablissementAccessibleQO);
    }
    
    /**
     * {@inheritDoc}
     */
    public String findUtilisateurPreferences(final String uid){
        return confidentialiteFacadeService.findUtilisateurPreferences(uid);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<EtablissementDTO>> findListeEtablissementEnseignantAdmRessources(
            EtablissementAccessibleQO etablissementAccessibleQO) {       
        return confidentialiteFacadeService.findListeEtablissementEnseignantAdmRessources(etablissementAccessibleQO);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean saveCheckAutiomatisationActivationSaisieSimplifiee(
            CheckSaisieSimplifieeQO checkSaisieSimplifieeQO)
            throws MetierException {        
        return confidentialiteFacadeService.saveCheckAutiomatisationActivationSaisieSimplifiee(checkSaisieSimplifieeQO);
    }
}
