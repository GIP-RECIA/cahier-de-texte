/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: OperationSqlService.java,v 1.9 2010/06/07 07:42:33 weberent Exp $
 */

package org.crlr.alimentation;

import org.crlr.alimentation.DTO.EleveDTO;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.alimentation.DTO.InspecteurDTO;
import org.crlr.dto.application.base.GenericDTO;

import org.crlr.metier.entity.EtablissementBean;

import java.util.Date;
import java.util.List;

/**
 * Interface (proxy de OperationSql).
 *
 * @author breytond.
 * @version $Revision: 1.9 $
 */
public interface OperationSqlService {
    /**
     * Insere les établissements dans la BD.
     *
     * @param etablissements La liste des établissements à inserer
     * @param typesDevoir les type de devoirs par défaut de l'établissement
     */
    public void addEtablissements(List<EtablissementBean> etablissements, String[] typesDevoir);

    /**
     * Insere la liste des enseignants dans la BD, ainsi que les classes, les
     * groupes, les enseignements et les jointures avec établissements, classes et
     * groupes.  et groupes manquants.
     *
     * @param list La liste des enseignants à inserer
     */
    public void addEnseignants(List<EnseignantDTO> list);

    /**
     * Insere l'année scolaire dans la BD.
     *
     * @param exercice L'exercice
     * @param dateRentree La date de début d'année scolaire
     * @param dateSortie La date de fin d'année scolaire
     * @param periodes Les periodes de l'année scolaire.
     */
    public void addAnneeScol(String exercice, Date dateRentree, Date dateSortie, List<GenericDTO<Date, Date>> periodes);

    /**
     * Insere la liste des élèves dans la BD, ainsi que les jointures avec
     * établissements, avec classes et avec groupes. Insere si neccessaires les classes
     * et groupes manquants.
     *
     * @param list La liste des élèves à inserer
     */
    public void addEleves(List<EleveDTO> list); 
    
    /**
     * Insere la liste des inspecteurs dans la BD.
     *
     * @param list La liste des inspecteurs à inserer
     */
    public void addInspecteurs(List<InspecteurDTO> list);

    /**
     * Méthode de recherche de l'exercice de la dernière année scolaire.
     *
     * @return l'exercice.
     */
    public String findExerciceAnneeScolaireArchive();

    /**
     * Méthode permettant de renommer un schéma, afin d'archiver l'année
     * scolaire.
     *
     * @param schemaArchive le nom du schéma archive.
     */
    public void renameSchemaPourArchive(final String schemaArchive);

    /**
     * Méthode permettant de sauvegarder les années.
     *
     * @param schemaArchive schemaArchive.
     */
    public void saveListeAnneeScolaireSurSchemaCourantDeArchive(final String schemaArchive);

    /**
     * Mise à jours des enseignants dans la BD si l'enseignant travaille dans
     * plusieurs établissements.
     *
     * @param resultList La liste des enseignants à inserer
     */
    public void updateEnseignants(List<EnseignantDTO> resultList);

    /** 
     * Recherche s'il existe au moins un établissement dans le schéma courant du cahier de texte.
     * @return vrai sss il existe un établissement.
     */
    public boolean existEtablissement();

    
}
