/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.UserDTOForList;
import org.crlr.dto.application.inspection.DroitInspecteurDTO;
import org.crlr.dto.application.inspection.RechercheDroitInspecteurQO;
import org.crlr.dto.application.inspection.SaveDroitInspecteurQO;
import org.crlr.exception.metier.MetierException;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface InspectionFacadeService {

    /**
     * Recherche la liste des droits d'inspection donnés sur un établissement.
     * @param rechercheDroitInspecteurQO le qo.
     * @return la liste des droits.
     */
    ResultatDTO<List<DroitInspecteurDTO>> findDroitsInspection(RechercheDroitInspecteurQO rechercheDroitInspecteurQO);

    /**
     * Recherche la liste des inspecteurs.
     * @return la liste.
     */
    List<UserDTOForList> findListeInspecteurs();

    /**
     * Recherche la liste des enseignants.
     * @param idEtablissement .
     * @return la liste.
     */
    List<EnseignantDTO> findListeEnseignants(Integer idEtablissement);

    /**
     * Sauvegarde/supprime un droit d'accès pour un inspecteur, un enseignant et un etablissement. 
     * @param saveDroitInspecteurQO .
     * @throws MetierException .
     */
    void saveDroit(SaveDroitInspecteurQO saveDroitInspecteurQO) throws MetierException;

    /**
     * Sauvegarde (ajout, modif, supprime) la liste des droits.
     * @param listeDroit la liste a persister
     * @throws MetierException .
     */
    void saveDroitsListe(List<DroitInspecteurDTO> listeDroit) throws MetierException;


  
}
