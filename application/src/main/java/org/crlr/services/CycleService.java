/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GroupeClasseService.java,v 1.8 2010/04/26 14:03:08 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.cycle.CycleDTO;
import org.crlr.dto.application.cycle.CycleSeanceDTO;
import org.crlr.dto.application.cycle.CycleSeanceFinalDTO;
import org.crlr.dto.application.cycle.RechercheCycleEmploiQO;
import org.crlr.dto.application.cycle.RechercheCycleQO;
import org.crlr.dto.application.cycle.RechercheCycleSeanceQO;
import org.crlr.exception.metier.MetierException;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.8 $
  */
public interface CycleService {

    /**
     * Sauvegarder (ajout / modif) d'un cycle.
     *
     * @param cycleDTO L'ensemble des critères de la sequence pedagogiqeu
     * @return l'id de la sequence pédagogique ajoutée ou modifiée
     *
     * @throws MetierException Exception
     */
    public ResultatDTO<Integer> saveCycle(CycleDTO cycleDTO) throws MetierException;

    /**
     * Sauvegarder (ajout / modif) d'une seance de cycle.
     *
     * @param cycleSeanceDTO L'ensemble des critères de la seance. Doit contenir l'objet cycle parent. 
     * @return l'id de la séance ajoutée ou modifiée
     * @throws MetierException Exception
     */
    public ResultatDTO<Integer> saveCycleSeance(CycleSeanceDTO cycleSeanceDTO) throws MetierException;
    
    /**
     * Enregistre en base l'odre de tri des seances dans une sequence pedagogique.
     * @param cycleDTO le cycle avec ses seances/
     * @return true/false
     * @throws MetierException l'exception
     */
    public ResultatDTO<Boolean> saveCycleOrdreSeance(CycleDTO cycleDTO) throws MetierException;
    
    /**
     * Suppression d'une seance de cycle.
     *
     * @param cycleSeanceDTO L'ensemble des critères de la seance.  
     * @return true/false
     * @throws MetierException Exception
     */
    public ResultatDTO<Boolean> deleteCycleSeance(CycleSeanceDTO cycleSeanceDTO) throws MetierException;
    
    /**
     * Suppression d'un cycle complet.
     *
     * @param cycleDTO Le cycle a virer.  
     * @return true/false
     * @throws MetierException Exception
     */
    public ResultatDTO<Boolean> deleteCycle(CycleDTO cycleDTO) throws MetierException;
    
    /**
     * Recherche une liste de cycle en fonction des criteres saisis.
     * @param rechercheCycleQO les criteres. 
     * @return une liste.
     */
    public ResultatDTO<List<CycleDTO>> findListeCycle(RechercheCycleQO rechercheCycleQO);
    
    /**
     * Recherche la liste de Seance du cycle.
     * @param rechercheCycleSeanceQO les criteres. 
     * @return une liste.
     */
    public List<CycleSeanceDTO> findListeCycleSeance(RechercheCycleSeanceQO rechercheCycleSeanceQO); 
    
    /**
     * Complete les infos de la seance : devoir et PJ. 
     * @param cycleSeanceDTO la seance a completer
     */
    public void completeInfoSeance(CycleSeanceDTO cycleSeanceDTO);
    
    /**
     * Retourne la liste des seances correspondant a l'emploi du temps de la classe/enseignant.
     * @param rechercheCycleEmploiQO les criteres de selection des seances.
     * @return une liste de seance.
     */
    public ResultatDTO<List<CycleSeanceFinalDTO>> findListeEmploiDTO(RechercheCycleEmploiQO rechercheCycleEmploiQO);
    
    
}
