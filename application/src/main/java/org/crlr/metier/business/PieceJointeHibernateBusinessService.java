/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PieceJointeHibernateBusinessService.java,v 1.7 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.List;

import org.crlr.exception.metier.MetierException;
import org.crlr.metier.entity.PieceJointeBean;
import org.crlr.web.dto.FileUploadDTO;

/**
 * PieceJointeHibernateBusinessService.
 *
 * @author vibertd
 * @version $Revision: 1.7 $
  */
public interface PieceJointeHibernateBusinessService {
    /**
     * Recherche d'une pièce jointe.
     *
     * @param id l'identifiant
     *
     * @return la pièce jointe.
     */
    public PieceJointeBean find(Integer id);

    /**
     * Recherche l'utilisattion d'une pice jointe dans devoir ou seance.
     *
     * @param id l'identifiant
     *
     * @return TRUE si la piece est utilisée par un devoir ou seance
     */
    public Boolean findUsed(Integer id);

    
    /**
     * Recherche de l'existance d'une pièce jointe.    
     *
     * @param id l'identifiant.
     *
     * @return identifiant.
     */
    public Integer exist(String id);

    /**
     * Mise à jour d'une pièce jointe.    
     *
     * @param pieceJointe la pièce jointe à mettre à jour.
     *
     * @return son identifiant.
     *
     * @throws MetierException l'exception métier potentielle.
     */
    public Integer save(PieceJointeBean pieceJointe)
                 throws MetierException;

    /**
     * Permet de sauvegarder une pièce jointe pour une séance.
     *
     * @param fileUploadDTO La pièce jointe
     *
     * @return l'id de la piece jointe créée
     *
     * @throws MetierException Exception
     */
    public Integer savePieceJointeSeance(FileUploadDTO fileUploadDTO)
                                  throws MetierException;

    
    /**
     * Permet de sauvegarder une pièce jointe pour une séance de sequence pedagogique.
     * @param fileUploadDTO La pièce jointe
     * @return l'id de la piece jointe créée
     * @throws MetierException Exception
     */
    public Integer savePieceJointeCycleSeance(FileUploadDTO fileUploadDTO)
                                  throws MetierException;

    /**
     * Permet de sauvegarder une pièce jointe pour un devoir de sequence pedagogique.
     * @param fileUploadDTO La pièce jointe
     * @return l'id de la piece jointe créée
     * @throws MetierException Exception
     */
    public Integer savePieceJointeCycleDevoir(FileUploadDTO fileUploadDTO)
                                  throws MetierException;
    
    /**
     * Permet de sauvegarder une pièce jointe pour un devoir.
     *
     * @param fileUploadDTO La pièce jointe
     *
     * @return l'id de la piece jointe créée
     * @throws MetierException Exception
     */
    public Integer savePieceJointeDevoir(FileUploadDTO fileUploadDTO) throws MetierException;
    
   
    /**
     * Permet de supprimer les entrées dans la table cahier_piece_jointe_seance.
     *
     * @param idSeance L'id de la séance
     * @throws MetierException Exception
     */
    public void deletePieceJointeSeance(Integer idSeance)
                                          throws MetierException;

    /**
     * Permet de supprimer les entrées dans la table cahier_piece_jointe_devoir.
     *
     * @param idDevoir L'id du devoir
     *
     * @return La liste des id des pieces jointes à supprimer
     *
     */
    public List<Integer> deletePieceJointeDevoir(Integer idDevoir);

    /**
     * permet de supprimer les pièces jointes dans la table cahier_piece_jointe.
     *
     * @param idPieceJointe Id de la pièce jointe.
     *
     * @throws MetierException Exception
     */
    public void deletePieceJointe(Integer idPieceJointe)
                           throws MetierException;

    /**
     * Permet de vérifier l'appartenance d'une pièce jointe à plusieurs devoirs
     * ou séances afin de ne pas la supprimer.
     *
     * @param fileUploadDTO le fichier à supprimer.
     *
     * @return True ou false
     *
     * @throws MetierException Exception
     */
    public boolean verifieAppartenancePieceJointe(FileUploadDTO fileUploadDTO) throws MetierException;
    
    /**
     * Verifie si le nom du fichier passé en paramètre est utilisé pour une pièce jointe de l'utilisateur.
     * @param uid uid de l'utilisateur connecté
     * @param nomDossier nom du dossier contenant le fichier a rechercher ex: "/Mes documents/SousDossier" 
     * @param nomFichier nom du fichier a rechercher ex: "monFichier.doc" 
     * @return TRUE si le fichier a été trouvé dans la table cahier_piece_jointe
     * @throws MetierException Exception 
     */
    public boolean fileIsUsed(final String uid, final String nomDossier, final String nomFichier) 
        throws MetierException;
    
    /**
     * Recherche un FileUploadDTO à parir des infos permettant de l'identifier.
     * @param uid uid de l'utilisateur connecté
     * @param nomDossier nom du dossier contenant le fichier a rechercher ex: "/Mes documents/SousDossier" 
     * @param nomFichier nom du fichier a rechercher ex: "monFichier.doc" 
     * @return FileUploadDTO si le fichier a été trouvé dans la table cahier_piece_jointe sinon NULL
     * @throws MetierException Exception 
     */
    public FileUploadDTO findFile(final String uid, final String nomDossier, final String nomFichier) 
        throws MetierException;
    
    /**
     * Déplace le fichier dans le dossier indiqué. 
     * @param fichierSrc FileUploadDTO à déplacer 
     * @param dossierDes dossier destination ex: "/Mes document/Dossier"
     * @return TRUE ou FALSE selon que le déplacement s'est correctement déroulé
     */
    public boolean moveFile(final FileUploadDTO fichierSrc, final String dossierDes);    
}
