/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PlainTextReport.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import org.crlr.utils.ObjectUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Édition simple de type texte. Le <code>mimetype</code> par défaut de cette édition
 * est <code>text/plain</code>, mais il peut être redéfini par des sous-classes.
 *
 * @author breytond.
 */
public class PlainTextReport extends AbstractCharSequenceReport implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant DEFAULT_MIMETYPE. */
    private static final String DEFAULT_MIMETYPE = "text/plain";

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private String extension = ".txt";

/**
     * Constructeur requis pour la sérialisation. Ne pas utiliser.
     */
    public PlainTextReport() {
        super();
        setMimeType(DEFAULT_MIMETYPE);
    }

/**
     * The Constructor.
     * 
     * @param data the data
     * @param name the name
     */
    public PlainTextReport(final String name, final CharSequence data) {
        super(name, data);
        setMimeType(DEFAULT_MIMETYPE);
    }

    /**
     * {@inheritDoc}
     */
    public String getMimeType() {
        return mimeType;
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
        return "attachment";
    }

    /**
     * {@inheritDoc}
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Methode : setExtension.
     *
     * @param extension :
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Affecte mime type.
     *
     * @param mimeType the mime type
     */
    public void setMimeType(String mimeType) {
        this.mimeType = ObjectUtils.defaultIfNull(mimeType, DEFAULT_MIMETYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "PlainTextReport[name=" + getName() + ", data=" + getData() +
               ", mimeType=" + getMimeType() + "]";
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
        data = (CharSequence) s.readObject();
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
