/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SeanceHibernateBusinessService.java,v 1.17 2010/04/21 15:39:48 jerome.carriere Exp $
 */

package org.crlr.metier.business;

import java.util.List;

import org.crlr.dto.application.cycle.CycleDTO;
import org.crlr.dto.application.cycle.CycleDevoirDTO;
import org.crlr.dto.application.cycle.CyclePlageEmploiDTO;
import org.crlr.dto.application.cycle.CycleSeanceDTO;
import org.crlr.dto.application.cycle.RechercheCycleEmploiQO;
import org.crlr.dto.application.cycle.RechercheCycleQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.web.dto.FileUploadDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author vibertd
 * @version $Revision: 1.17 $
  */
public interface CycleHibernateBusinessService {

    /**
     * Sauvegarde une sequence pedagogique.
     *
     * @param cycle CycleDTO : l'id est eventuellement mis a jour dans le cas d'un ajout.
     * @return True / False selon que la sauvegarde s'est bien passe. 
     */
    public Boolean saveCycle(final CycleDTO cycle);

    
    /**
     * Sauvegarder (ajout / modif) d'une seance de cycle.
     *
     * @param cycleSeanceDTO L'ensemble des critères de la seance. Doit contenir l'objet cycle parent.
     * L'id de la seance en mode ajout est mis à jour dans l'objet. 
     * @return true / false selon que la sauvegarde s'est bien passee.
     * @throws MetierException Exception
     */
    public Integer saveCycleSeance(CycleSeanceDTO cycleSeanceDTO) throws MetierException;

    /**
     * Enregistre en base l'odre de tri des seances dans une sequence pedagogique.
     * @param cycleDTO le cycle avec ses seances/
     * @return le nombre de seances maj
     * @throws MetierException l'exception
     */
    public Integer saveCycleOrdreSeance(CycleDTO cycleDTO) throws MetierException;
    
    /**
     * Sauvegarder (ajout / modif) d'un devoir de cycle.
     *
     * @param cycleDevoirDTO L'ensemble des critères du devoir.
     * @param  idCycleSeance : id de la seance 
     * @return Integer id du cycledevoir
     * @throws MetierException Exception
     */
    public Integer saveCycleDevoir(CycleDevoirDTO cycleDevoirDTO,  Integer idCycleSeance) throws MetierException;
    
    /**
     * Recherche une liste de cycle en fonction des criteres saisis.
     * @param rechercheCycleQO les criteres. 
     * @return une liste.
     */
    public List<CycleDTO> findListeCycle(RechercheCycleQO rechercheCycleQO); 
    
    /**
     * Recherche la liste de Seance du cycle.
     * @param cycleDTO les criteres. 
     * @return une liste.
     */
    public List<CycleSeanceDTO> findListeCycleSeance(CycleDTO cycleDTO); 

    /**
     * Charge la liste des devoir d'une piece jointe cycle.
     * @param idCycleSeance l'id de la seance.
     * @return la liste.
     */
    public List<CycleDevoirDTO> findListeDevoirSeance(final Integer idCycleSeance);
    
    /**
     * Charge les PJ d'une seance de cycle.
     * @param idCycleSeance l'id de la seance.
     * @return la liste des PJ
     */
    public List<FileUploadDTO> findListePieceJointeSeance(final Integer idCycleSeance);
    
    /**
     * Charge les PJ d'un devoir de cycle.
     * @param idCycleDevoir l'id de la seance.
     * @return la liste des PJ
     */
    public List<FileUploadDTO> findListePieceJointeDevoir(final Integer idCycleDevoir);
    
    /**
     * Supprime les liens entre une seance et ses PJ.
     * @param idCycleSeance id de la seance.
     */
    public void deletePieceJointeSeance(final Integer idCycleSeance);

    /**
     * Supprime les liens entre un devoir et ses PJ.
     * @param idCycleDevoir id du devoir.
     */
    public void deletePieceJointeDevoir(final Integer idCycleDevoir);
    
    /**
     * Supprime un devoir.
     * @param idCycleDevoir id du devoir.
     */
    public void deleteDevoir(final Integer idCycleDevoir);
    
    /**
     * Supprime une seance.
     * @param idCycleSeance id de la seance.
     */
    public void deleteCycleSeance(final Integer idCycleSeance);

    /**
     * Supprime une séquence pédagogique.
     * @param idCycle id de la seance.
     */
    public void deleteCycle(final Integer idCycle);
    
    /**
     * Recherche les plages EDT completées des infos sur les périodes pour 
     * un enseignant et une classe.
     * @param rechercheCycleEmploiQO les crieteres de recherche
     * @return une liste de CyclePlageEmploiDTO
     */
    public List<CyclePlageEmploiDTO> findListePlageEmploi(final RechercheCycleEmploiQO rechercheCycleEmploiQO);
    
}
