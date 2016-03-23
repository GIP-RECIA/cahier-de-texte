/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: FileUploadDTO.java,v 1.9 2009/07/30 15:09:26 ent_breyton Exp $
 */

package org.crlr.web.dto;

/**
 * @author G-CG34-FRMP
 *
 */
public interface TransformerEnFileUploadDTO {
        /**
         * @return un dto, cette méthode fait ce qui est nécessaire d'avoir le fichier dans le dépot CCN.
         */
        public FileUploadDTO creerFileUploadDTO();
        
        public FileUploadDTO getFileUploadDTO();
        
        public String getNom();
    }