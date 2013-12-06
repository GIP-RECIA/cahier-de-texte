/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirHibernateBusinessService.java,v 1.11 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.devoir.ChargeDevoirQO;
import org.crlr.dto.application.devoir.ChargeTravailDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.devoir.RechercheDevoirQO;
import org.crlr.dto.application.devoir.RecherchePieceJointeDevoirQO;
import org.crlr.dto.application.devoir.ResultatRechercheDevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirQO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.entity.DevoirBean;
import org.crlr.web.dto.FileUploadDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author vibertd
 * @version $Revision: 1.11 $
  */
public interface DevoirHibernateBusinessService {
    /**
     * Recherche un devoir à partir de son id.
     *
     * @param id L'id du devoir à rechercher.
     *
     * @return Un devoirBean
     */
    public DevoirBean find(String id);

    /**
     * Sauvegarde un devoir.
     *
     * @param devoir Contient les informations du devoir à sauvegarder
     *
     * @return le devoir sauvegardé
     *
     * @throws MetierException Exception
     */
    public Integer save(DevoirBean devoir) throws MetierException;

    /**
     * Permet de retourner la liste des types de devoir.
     * @param idEtablissement l'id.
     * @return la liste des types de devoir    
     */
    public ResultatDTO<List<TypeDevoirDTO>> findListeTypeDevoir(final Integer idEtablissement);

    /**
     * Permet de sauvegarder un devoir à partir d'un DTO.
     *
     * @param devoirDTO Le devoirDTO qui contient les infos à sauvegarder
     *
     * @return L'id du devoir crée
     *
     * @throws MetierException Exception
     */
    public Integer saveDevoir(DevoirDTO devoirDTO)
                       throws MetierException;

    /**
     * Supprime un devoir par son id.
     *
     * @param id L'id du devoir à supprimer
     *
     * @throws MetierException Exception
     */
    public void delete(Integer id) throws MetierException;

    /**
     * Méthode d'affichage des devoirs.
     *
     * @param rechercheDevoirQO Contient les paramètres de recherche
     *
     * @return la liste des devoirs à afficher
     *
     * @throws MetierException Exception
     */
    public ResultatDTO<List<ResultatRechercheDevoirDTO>> listeDevoirAffichage(RechercheDevoirQO rechercheDevoirQO)
        throws MetierException;

    
    /**
     * Méthode d'affichage des devoirs pour le planning.
     *
     * @param rechercheSeanceQO Contient les paramètres de recherche
     *
     * @return la liste des devoirs à afficher
     *
     * @throws MetierException Exception
     */
    public ResultatDTO<List<DevoirDTO>> findDevoirForPlanning(RechercheSeanceQO rechercheSeanceQO)
        throws MetierException;
    
    /**
     * Recherche les pièces jointes d'un devoir.
     *
     * @param recherchePJDevoir QO de recherche des PJ d'un devoir. 
     *
     * @return La liste des pièces jointes   
     */
    public List<FileUploadDTO> findPieceJointeDevoir(RecherchePieceJointeDevoirQO recherchePJDevoir);
    
    /**
     * Création ou mise à jour d'un devoir.
     * @param typeDevoirQO les paramètres.
     * @throws MetierException Exception
     */
    public void saveTypeDevoir(final TypeDevoirQO typeDevoirQO) throws MetierException;

    /**
     * Destruction d'un type de devoir.
     * @param idTypeDevoir l'identifiant du devoir.
     * @throws MetierException Exception
     */
    public void deleteTypeDevoir(final Integer idTypeDevoir) throws MetierException;
    
    
    /**
     * Recherche d'un devoir et de ses pièces jointes.
     * @param idDevoir l'identifiant du devoir
     * @return le devoir.
     */    
    public DevoirDTO findDetailDevoir(final Integer idDevoir);
    
    /**
     * Mise à jour d'un devoir.
     * @param devoirDTO les paramètres.
     */
    public void updateDevoir(final DevoirDTO devoirDTO);
    
    
    /**
     * Cherche la liste des autres devoirs que la même classe ou groupe doit retrouner le me même jour. 
     * @param chargeDevoir chargeDevoir
     * @return la liste des devoir a rendre le meme jour pour la meme classe ou groupe
     *    
     */
    public ChargeTravailDTO findChargeTravail(final ChargeDevoirQO chargeDevoir);
    
}
