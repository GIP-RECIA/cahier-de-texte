/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PrintSuperSeanceDTO.java,v 1.2 2009/07/30 15:09:26 ent_breyton Exp $
 */

package org.crlr.dto.application.seance;

import java.io.File;
import java.io.InputStream;

/**
 * 
 *
 */
public class ImageDTO {
    private InputStream inputStream;
    
    private String fileName;
    
    private File file;
    
    private byte[] fileContents;
    
    private String fileKey;

    private String label;
    
    /**
     * @return the inputStream
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * @param inputStream the inputStream to set
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @return the fileContents
     */
    public byte[] getFileContents() {
        return fileContents;
    }

    /**
     * @param fileContents the fileContents to set
     */
    public void setFileContents(byte[] fileContents) {
        this.fileContents = fileContents;
    }

    /**
     * @return the fileKey
     */
    public String getFileKey() {
        return fileKey;
    }

    /**
     * @param fileKey the fileKey to set
     */
    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    
}