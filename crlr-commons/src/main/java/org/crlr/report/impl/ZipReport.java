/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ZipReport.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Édition de type ZIP. Le <code>mimetype</code> de cette édition est
 * <code>application/zip</code>.
 *
 * @author breytond
 */
public class ZipReport extends AbstractByteArrayReport implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

/**
     * Constructeur requis pour la sérialisation. Ne pas utiliser.
     */
    public ZipReport() {
    }

/**
     * The Constructor.
     * 
     * @param data
     *            the data
     * @param name
     *            the name
     */
    public ZipReport(final String name, final byte[] data) {
        super(name, data);
    }

    /**
     * {@inheritDoc}
     */
    public String getMimeType() {
        return "application/zip";
    }

    /**
     * {@inheritDoc}
     */
    public String getContentType() {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    public String getModeContentDisposition() {
        return "inline";
    }

    /**
     * {@inheritDoc}
     */
    public String getExtension() {
        return ".zip";
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
