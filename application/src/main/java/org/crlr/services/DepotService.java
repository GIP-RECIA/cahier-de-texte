/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirService.java,v 1.12 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.services;

import java.io.File;

import org.crlr.exception.metier.MetierException;
import org.crlr.web.dto.FileUploadDTO;

/**
 * DevoirService.
 *
 * @author $author$
 * @version $Revision: 1.12 $
  */
public interface DepotService {

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
    
    /**
     * Supprime le fichier indiqué dans la base de données PJ. 
     * Cette methode supprime uniquemenet les fichiers qui ne sont pas référencés 
     * par des seance ou devoir. Si la suppression en base est faite, la methode 
     * supprime le fichier physique.
     * @param fichier FileUploadDTO à déplacer,
     * @param fichierPhysique fichier physique a supprimer (correspond au FileUploadDTO)
     * @return TRUE ou FALSE selon que le déplacement s'est correctement déroulé
     * @throws MetierException exception en cas d'erreur
     */
    public boolean deleteFile(final FileUploadDTO fichier, final File fichierPhysique) throws MetierException;

    
}
