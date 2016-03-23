/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AnneeScolaireFacadeService.java,v 1.4 2010/03/31 08:08:44 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.Date;
import java.util.List;

import org.crlr.dto.application.importEDT.CaracEtabImportDTO;
import org.crlr.dto.application.importEDT.PeriodeEdtQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.importEDT.DTO.EmploiDTO;


/**
 * Classe à laquelle fait appel ImportEDTDelegate pour les méthodes d'accès aux données.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public interface ImportEDTBusinessService {
    
    /**
     * Méthode principale de l'import.
     * @param caracEtabImportDTO : les caractéristiques de l'établissements pour cet import.
     * @param pathSTS : le path du fichier STS uploadé.
     * @param pathEDT : le path du fichier EDT uploadé.
     * @param pathAppli : le chemin de l'application.
     * @return CaracEtabImportDTO : le DTO qui contient ce qu'il faut pour le rapport et le traitement d'insertion.
     * @throws MetierException : l'exception levée en cas d'erreur.
     */
    public CaracEtabImportDTO saveTraitementEDTSTS(CaracEtabImportDTO caracEtabImportDTO, 
            String pathSTS, String pathEDT, String pathAppli) throws MetierException;  
    
    /**
     * Méthode qui va permettre de vider tous les enregistrements d'emploi du temps de l'établissement.
     * @param periodeEdtQO : 
     *    l'id de l'établissement dont on souhaite vider l'emploi du temps.
     *    dateDebut de la periode
     */
    public void deleteEDTEtablissement(PeriodeEdtQO periodeEdtQO);
   
    /**
     * Méthode qui insère massivement toutes les cases emplois générées par l'import EDT.
     * @param casesSimples : les cases qui ne font pas parties d'une fusion.
     * @param dateDebut date de debut de periode
     */
    public void insertionCases(List<EmploiDTO> casesSimples, Date dateDebut);
    
    /**
     * Compte le nombre d'entrées dans cahier_emploi pour l'établissement.
     * @param periodeEdtQO :
     *    idEtablissement : l'id de l'établissement.
     *    dateDebut de la periode
     * @return le nombre de cases emplois en bdd pour l'établissement.
     */
    public Integer checkNombreCaseEmploiPourEtablissement(PeriodeEdtQO periodeEdtQO);
    
    /**
     * Retourne l'état de l'import indiqué en base de données.
     * @param idEtablissement : l'id de l'établissement.
     * @return le booléen qui indique si le traitement est en cours.
     */
    public Boolean findEtatImportEtablissement(final Integer idEtablissement);
    
    /**
     * Met à jour le champs import de la base de données pour l'établissement.
     * @param idEtablissement : id de l'établissement.
     * @param statut : true ou false pour dire si on démarre l'import ou s'il est fini.
     */
    public void modifieStatutImportEtablissement(Integer idEtablissement, Boolean statut);
    
    /**
     * Retourne une string contenant le getTime du début de l'import.
     * @param idEtablissement : id de l'établissement.
     * @return le string du getTime.
     */
    public String checkDateImportEtablissement(final Integer idEtablissement);
}
