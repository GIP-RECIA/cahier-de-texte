/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirDelegate.java,v 1.11 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.services;

import java.io.File;

import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.DepotFacadeService;
import org.crlr.web.dto.FileUploadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DevoirDelegate.
 *
 * @author $author$
 * @version $Revision: 1.11 $
 */
@Service
public class DepotDelegate implements DepotService {

    @Autowired
    private DepotFacadeService depotFacadeService;

    /**
     * Mutateur depotFacadeService.
     *
     * @param depotFacadeService depotFacadeService à modifier
     */
    public void setDepotFacadeServicee(DepotFacadeService depotFacadeService) {
        this.depotFacadeService = depotFacadeService;
    }

    /**
     * {@inheritDoc}
     */
    public boolean fileIsUsed(final String uid, final String nomDossier, final String nomFichier) 
        throws MetierException {
        return this.depotFacadeService.fileIsUsed(uid,nomDossier,nomFichier);
    }

    /**
     * {@inheritDoc}
     */
    public FileUploadDTO findFile(final String uid, final String nomDossier, final String nomFichier) 
        throws MetierException {
        return this.depotFacadeService.findFile(uid,nomDossier,nomFichier);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean moveFile(final FileUploadDTO fichierSrc, final String dossierDes) {
        return this.depotFacadeService.moveFile(fichierSrc, dossierDes);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean deleteFile(final FileUploadDTO fichier, final File fichierPhysique) 
        throws MetierException {
        return this.depotFacadeService.deleteFile(fichier,fichierPhysique);
    }

    
}
