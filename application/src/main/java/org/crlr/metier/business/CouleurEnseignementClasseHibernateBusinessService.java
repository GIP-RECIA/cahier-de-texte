/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: CouleurEnseignementClasseHibernateBusinessService.java,v 1.3 2009/05/26 08:06:30 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.Map;

import org.crlr.dto.ResultatDTO;

import org.crlr.exception.metier.MetierException;

import org.crlr.dto.application.base.CouleurEnseignementClasseDTO;
import org.crlr.dto.application.sequence.RechercheCouleurEnseignementClasseQO;
import org.crlr.dto.application.sequence.SaveCouleurEnseignementClasseQO;

import org.crlr.metier.entity.CouleurEnseignementClasseBean;

/**
 * CouleurEnseignementClasseHibernateBusinessService.
 *
 * @author vibertd
 * @version $Revision: 1.3 $
  */
public interface CouleurEnseignementClasseHibernateBusinessService {
    /**
     * Recherche d'une association enseignant / etablissement / enseignement / groupe (ou) classe / couleur.
     *
     * @param idEnseignant id de l'enseignant.
     * @param idEtablissement id de l'etablissement.
     * @param idEnseignement id de l'enseignement.
     * @param idGroupe id du groupe.
     * @param idClasse id du classe.
     *
     * @return l'id de lassociation.
     */
    public Integer findId(Integer idEnseignant, Integer idEtablissement, Integer idEnseignement, Integer idGroupe, Integer idClasse);

    /**
     * Recherche de l'existence d'une association enseignant / etablissement / enseignement / groupe (ou) classe.
     *
     * @param idEnseignant id de l'enseignant.
     * @param idEtablissement id de l'etablissement.
     * @param idEnseignement id de l'enseignement.
     * @param idGroupe id du groupe.
     * @param idClasse id du classe.
     *
     * @return booleen, existant ou non.
     */
    public boolean exist(Integer idEnseignant, Integer idEtablissement, Integer idEnseignement, Integer idGroupe, Integer idClasse);

    /**
     * Mise a jour de la couleur pour une association enseignant / etablissement / enseignement / groupe (ou) classe.
     * Ne fait la mise a jour qu'en cas de changement de couleur.
     * @param idEnseignant id de l'enseignant.
     * @param idEtablissement id de l'etablissement.
     * @param idEnseignement id de l'enseignement.
     * @param idGroupe id du groupe.
     * @param idClasse id du classe.
     * @param couleur couleur de l'association.
     *
     * @throws MetierException l'exception potentielle.
     */
    public void save(SaveCouleurEnseignementClasseQO saveCouleurEnseignementQO)
                 throws MetierException;
    
    /**
     * Recherche d'une association enseignant / etablissement / enseignement / groupe (ou) classe / couleur.
     *
     * @param rechercheCouleurEnseignementClasse criteres de recherche.
     *
     * @return l'association.
     */
    public ResultatDTO<CouleurEnseignementClasseDTO> findCouleurEnseignementClasse(RechercheCouleurEnseignementClasseQO rechercheCouleurEnseignementClasseQO);
}
