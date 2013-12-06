/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RtfReport.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Édition de type RTF. Le <code>mimetype</code> de cette édition est
 * <code>text/rtf</code>.
 *
 * @author breytond
 */
public class RtfReport extends AbstractByteArrayReport implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

/**
     * Constructeur requis pour la sérialisation. Ne pas utiliser.
     */
    public RtfReport() {
    }

/**
     * The Constructor.
     *
     * @param data
     *            the data
     * @param name
     *            the name
     */
    public RtfReport(final String name, final byte[] data) {
        super(name, data);
    }

    /**
     * Renvoie le mime type.
     *
     * @return mimeType text/rtf
     */
    public String getMimeType() {
        return "text/rtf";
    }

    /**
     * Renvoie le content type.
     *
     * @return le content type
     */
    public String getContentType() {
        return "";
    }

    /**
     * Renvoie le mode content disposition.
     *
     * @return le mode content disposition
     */
    public String getModeContentDisposition() {
        return "inline";
    }

    /**
     * Renvoie l'extension RTF.
     *
     * @return l'extension
     */
    public String getExtension() {
        return ".rtf";
    }

    /**
     * Read object.
     *
     * @param s the s
     *
     * @throws IOException the IO exception
     * @throws ClassNotFoundException the class not found exception
     */
    private void readObject(ObjectInputStream s)
                     throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        name = (String) s.readObject();
        mimeType = (String) s.readObject();
        data = (byte[]) s.readObject();
    }

    /**
     * Write object.
     *
     * @param s the s
     *
     * @throws IOException the IO exception
     */
    private void writeObject(ObjectOutputStream s)
                      throws IOException {
        s.defaultWriteObject();
        s.writeObject(getName());
        s.writeObject(getMimeType());
        s.writeObject(data);
    }
}
