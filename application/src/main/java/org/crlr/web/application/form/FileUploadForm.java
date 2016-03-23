/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: FileUploadForm.java,v 1.2 2009/04/03 14:12:29 vibertd Exp $
 */

package org.crlr.web.application.form;

import org.crlr.web.dto.FileUploadDTO;

import java.util.ArrayList;

/**
 * Formulaire d'authentification.
 *
 * @author breytond
 */
public class FileUploadForm extends AbstractForm {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -374438749702632361L;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private ArrayList<FileUploadDTO> files;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Integer uploadsAvailable;

    /**
     * 
     * Constructeur.
     */
    public FileUploadForm() {
        reset();
    }

    /**
     *
     */
    public void reset() {
        files = new ArrayList<FileUploadDTO>();
        uploadsAvailable = 5;
    }

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public ArrayList<FileUploadDTO> getFiles() {
        return files;
    }

    /**
     *
     *
     * @param files DOCUMENTATION INCOMPLETE!
     */
    public void setFiles(ArrayList<FileUploadDTO> files) {
        this.files = files;
    }

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public Integer getUploadsAvailable() {
        return uploadsAvailable;
    }

    /**
     *
     *
     * @param uploadsAvailable DOCUMENTATION INCOMPLETE!
     */
    public void setUploadsAvailable(Integer uploadsAvailable) {
        this.uploadsAvailable = uploadsAvailable;
    }
}
