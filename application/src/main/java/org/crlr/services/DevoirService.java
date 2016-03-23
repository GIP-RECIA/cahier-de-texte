/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirService.java,v 1.12 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.ChargeDevoirQO;
import org.crlr.dto.application.devoir.ChargeTravailDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.devoir.RechercheDevoirQO;
import org.crlr.dto.application.devoir.RecherchePieceJointeDevoirQO;
import org.crlr.dto.application.devoir.ResultatRechercheDevoirDTO;
import org.crlr.dto.application.devoir.SaveDevoirQO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirQO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.web.dto.FileUploadDTO;

/**
 * DevoirService.
 *
 * @author $author$
 * @version $Revision: 1.12 $
  */
public interface DevoirService {
    /**
     * Permet de retourner la liste des types de devoir.
     * @param idEtablissement l'id.
     * @return la liste des types de devoir
     *    
     */
    public ResultatDTO<List<TypeDevoirDTO>> findListeTypeDevoir(final Integer idEtablissement);

    /**
     * Méthode d'affichage des devoirs.
     *
     * @param rechercheDevoirQO DOCUMENT ME!
     *
     * @return la liste des devoirs à afficher
     *
     * @throws MetierException Exception
     */
    public ResultatDTO<List<ResultatRechercheDevoirDTO>> listeDevoirAffichage(RechercheDevoirQO rechercheDevoirQO)
        throws MetierException;
    
    /**
     * Recherche les devoirs pour le planning mensuel.
     * @param rechercheSeanceQO le QO.
     * @return la liste des devoirs.
     * @throws MetierException Exception
     */
    public ResultatDTO<List<DevoirDTO>> findDevoirForPlanning(
            RechercheSeanceQO rechercheSeanceQO) throws MetierException;

    /**
     * Recherche les pièces jointes d'un devoir.
     *
     * @param recherchePJDevoir contient l'id du devoir et eventuellement 
     * l'exercice s'il s'agit d'une archive.
     *
     * @return La liste des pièces jointes
     *
     * @throws MetierException Exception
     */
    public List<FileUploadDTO> findPieceJointeDevoir(RecherchePieceJointeDevoirQO recherchePJDevoir)
                                              throws MetierException;
    
    /**
     * Création ou mise à jour d'un devoir.
     * @param typeDevoirQO les paramètres.
     * @throws MetierException Exception
     */
    public void saveTypeDevoir(final TypeDevoirQO typeDevoirQO)
                                        throws MetierException;

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
     * @param saveDevoirQO les paramètres.
     * @throws MetierException l'exception potentielle.
     */
    public void saveDevoir(final SaveDevoirQO saveDevoirQO) throws MetierException;
    
    /**
     * Supprime le devoir.
     * @param idDevoir id
     * @throws MetierException exception
     */
    public void deleteDevoir(final Integer idDevoir) throws MetierException;
    
    /**
     * Cherche pour la date de remise du devoir saisie les autres devoirs 
     * prévu à rendre le même jour pour.
     * @param devoir le devoir a tester
     *  @param utilisateurDTO u
     * @return la charge de travail prevu pour ce devoir.
     */
    public ChargeTravailDTO chercherChargeTravailGenerique(final DevoirDTO devoir, final UtilisateurDTO utilisateurDTO);
    
    /**
     * Cherche la liste des autres devoirs que la même classe ou groupe doit retrouner le me même jour. 
     * @param chargeDevoir chargeDevoir
     * @return la liste des devoir a rendre le meme jour pour la meme classe ou groupe
     *    
     */
    public ChargeTravailDTO findChargeTravail(final ChargeDevoirQO chargeDevoir);

   
    
}
