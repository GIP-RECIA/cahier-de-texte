/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.utils;

import java.io.File;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.web.dto.FileUploadDTO;

/**
 * FileUploadUtils.
 *
 * @author aurore.weber
 * @version $Revision: 1.1 $
  */
public final class FileUploadUtils {
    
    private static Log log = LogFactory.getLog(FileUploadUtils.class);
    
    /**
     * Constructeur protection.
     */
    private FileUploadUtils() {
        
    }
    
    /**
     * Vérifie si une pièce jointe existe pour éviter les plantages à
     * l'ouverture.
     *
     * @param fileUploadDTO le fichier.
     *
     * @return true si elle existe ou false
     */
    public static Boolean checkExistencePieceJointe(FileUploadDTO fileUploadDTO) {       
        final File checkFile =
            new java.io.File(fileUploadDTO.getPathAbsolute() + 
                             fileUploadDTO.getNom());
        
        log.debug("chemin REEL complet : {0}", checkFile.getAbsolutePath());
        if (!checkFile.exists()) {
            return false;
        }
        return true;
    }
   
}
