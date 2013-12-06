/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignementFacadeService.java,v 1.5 2010/04/12 04:30:13 jerome.carriere Exp $
 */

package org.crlr.metier.facade;

import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.EnseignementQO;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.exception.metier.MetierException;

/**
 * EnseignementFacadeService.
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public interface EnseignementFacadeService {
    /**
     * Retourne l'ensemble des enseignements pour la popup.
     *
     * @param rechercheEnseignementPopupQO paramètres de recherche de séance
     *
     * @return La liste des enseignements pour la popup
     *
     * @throws MetierException Exception
     */
    public ResultatDTO<List<EnseignementDTO>> findEnseignementPopup(RechercheEnseignementPopupQO rechercheEnseignementPopupQO)
        throws MetierException;

    /**
     * Méthode de recherche d'enseignement par id.
     *
     * @param idEnseignement Contient l'id de l'enseignement à rechercher.
     *
     * @return Un EnseignementDTO
     *
     * @throws MetierException DOCUMENTATION INCOMPLETE!
     */
    public EnseignementDTO find(Integer idEnseignement) throws MetierException;
    
    /**
     * Recherche des enseignements liés à un enseignant.
     * @param rechercheEnseignementPopupQO les paramètres de recherches.
     * @return la liste.
     */
    public List<EnseignementDTO> findEnseignementEnseignant(final RechercheEnseignementPopupQO rechercheEnseignementPopupQO);
    
    /**
     * Recherche des enseignements de l'établissement.
     * @param idEtablissement l'identifiant de l'établissement.
     * @return la liste des enseignements.
     */   
    public List<EnseignementDTO> findEnseignementEtablissement(final Integer idEtablissement);
    
    /**
     * Création, mise à jour, suppression d'un libellé d'enseignement court d'un établissement.
     * @param enseignementQO les parmètres.
     * @throws MetierException l'exception.
     */   
    public void saveEnseignementLibelle(final EnseignementQO enseignementQO) throws MetierException;
}
