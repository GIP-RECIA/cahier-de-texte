/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.Date;
import java.util.List;

import org.crlr.dto.application.importEDT.CaracEtabImportDTO;
import org.crlr.dto.application.importEDT.PeriodeEdtQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.importEDT.DTO.EmploiDTO;
import org.crlr.importEDT.DTO.PrintEDTDTO;
import org.crlr.report.impl.PdfReport;


/**
 * Classe qui contient toutes les méthodes d'accès aux données pour l'importEDT.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public interface ImportEDTService {

    /**
     * effectue le traitement d'import EDT.
     * @param caracEtabImportDTO : les caractéristiques de l'établissement pour l'import.
     * @param pathSTS le chemin du fichier STS uploadé.
     * @param pathEDT le chemin du fichier EDT uploadé.
     * @param pathAppli le chemin de l'application.
     * @param importStrict : vrai si l'import est strict, faux s'il se base uniquement sur la grille horaire.
     * @return CaracEtabImportDTO : le DTO qui contient les informations pour le rapport d'import et le traitement d'insertion.
     * @throws MetierException : exception qui peut être levée en cas d'erreur.
     */
   public CaracEtabImportDTO saveTraitementEDTSTS(CaracEtabImportDTO caracEtabImportDTO, 
           String pathSTS, String pathEDT, String pathAppli) throws MetierException; 
   
   /**
    * Edition PDF.
    * @param printEDTDTO le DTO avec les infos à afficher.
    * @return the report
    */
   public PdfReport printEmploiDuTemps(PrintEDTDTO printEDTDTO);
   
   /**
    * Supprime toutes les cases d'emploi du temps de l'établissement.
    * @param periodeEdtQO 
    *   idEtab : id de l'établissement.
    *   dateDebut de periode
    */
   public void deleteEmploiDuTempsEtablissement(PeriodeEdtQO periodeEdtQO);
   
   /**
    * Traitement qui insère les cases emplois en BDD.
    * @param casesSimples : les cases emplois simples à insérer.
    * @param dateDebut date de debut
    */
   public void insertionCases(List<EmploiDTO> casesSimples, Date dateDebut);
   
   /**
    * Compte le nombre d'entrées dans cahier_emploi pour l'établissement.
    * @param periodeEdtQO
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
   
   /**
    * Recupere le texte d'aide contextuelle dans le fichier de propriété.
    * @return le texte d'aide contextuelle.
    */
   public String getAideContextuelle();
}
