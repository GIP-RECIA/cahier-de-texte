/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirFacade.java,v 1.14 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.io.File;
import java.util.List;

import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.business.PieceJointeHibernateBusinessService;
import org.crlr.utils.Assert;
import org.apache.commons.collections.CollectionUtils;
import org.crlr.web.dto.FileUploadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Façade concernant les fonctionnalités du module de gestion des depots.
 *
 * @author durupt
 * @version $Revision: 1.14 $
 */
@Component
@Transactional(
        readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
    )
public class DepotFacade implements DepotFacadeService {
    
    @Autowired
    private PieceJointeHibernateBusinessService pieceJointeHibernateBusinessService;

    /**
     * Mutateur de pieceJointeHibernateBusinessService.
     * @param pieceJointeHibernateBusinessService le pieceJointeHibernateBusinessService à modifier.
     */
    public void setPieceJointeHibernateBusinessService(
            PieceJointeHibernateBusinessService pieceJointeHibernateBusinessService) {
        this.pieceJointeHibernateBusinessService = pieceJointeHibernateBusinessService;
    }

    /**
     * {@inheritDoc}
     */
    public boolean fileIsUsed(final String uid, final String nomDossier, final String nomFichier) 
        throws MetierException {
        return pieceJointeHibernateBusinessService.fileIsUsed(uid, nomDossier, nomFichier);
    }

    /**
     * {@inheritDoc}
     */
    public FileUploadDTO findFile(final String uid, final String nomDossier, final String nomFichier) 
        throws MetierException {
        return pieceJointeHibernateBusinessService.findFile(uid, nomDossier, nomFichier);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean moveFile(final FileUploadDTO fichierSrc, final String dossierDes) {
        return pieceJointeHibernateBusinessService.moveFile(fichierSrc, dossierDes);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean deleteFile(final FileUploadDTO fichier, final File fichierPhysique) throws MetierException {
        
        Boolean result = true;
        Assert.isNotNull("fichier", fichier);
        Assert.isNotNull("fichierPhysique", fichierPhysique);
        
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        
        // Supprime les PJ dans la table des PJ
        final List<Integer> listeIdPJ = fichier.getListeIdPJ();
        if (!CollectionUtils.isEmpty(listeIdPJ)) {
            for (final Integer idPJ : listeIdPJ) {
                try {
                    pieceJointeHibernateBusinessService.deletePieceJointe(idPJ);
                }  catch (Exception e) {
                    result = false;
                }
            }
        }
        
        // Si tout est OK, supprime le fichier physique
        if (result) {
            result = fichierPhysique.delete();
            
            // Teste le resultat de suppression physique
            if (!result) {
                conteneurMessage.add(new Message("Une erreur est survenue lors de la suppression du fichier.", Nature.BLOQUANT));
                throw new MetierException(conteneurMessage, "Erreur de suppression du fichier.");
            }
        }
        
        return result;
    }
    
    
}
