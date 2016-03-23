/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.business;

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
public interface InspectionHibernateBusinessService {
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
     * Sauvegarde le QO.
     *
     * @param saveDroitInspecteurQO .
     */
    void saveDroit(SaveDroitInspecteurQO saveDroitInspecteurQO);

    /**
     * Recherche l'id de l'inspecteur.
     *
     * @param uid uid de l'inspecteur.
     *
     * @return l'id de l'inspecteur.
     */
    Integer exist(String uid);
    
    /**
     * Ajout un nouveau droit. 
     * @param droit le droit a ajouter.
     * @throws MetierException e
     */
    void ajouter(DroitInspecteurDTO droit) throws MetierException;

    /**
     * Modifie un droit existant. 
     * @param droit le droit a modifier.
     * @throws MetierException  e
     */
    void modifier(DroitInspecteurDTO droit) throws MetierException;
    
    
    /**
     * Supprime un droit existant. 
     * @param droit le droit a supprimer.
     */
    void supprimer(DroitInspecteurDTO droit);
    
}
