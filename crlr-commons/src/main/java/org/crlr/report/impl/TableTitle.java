/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TableTitle.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The Class TableTitle.
 *
 * @author breytond.
 */
public class TableTitle implements Iterable<String> {
    /** The titles. */
    private final List<String> titles = new ArrayList<String>();

/**
     * Instantiates a new table title.
     * 
     * @param titles the titles
     */
    public TableTitle(final String... titles) {
        if (titles != null) {
            this.titles.addAll(Arrays.asList(titles));
        }
    }

    /**
     * Ajoute un titre.
     *
     * @param title title
     */
    public void addTitle(String title) {
        titles.add(title);
    }

    /**
     * Retourne le titre.
     *
     * @param i i
     *
     * @return titre
     */
    public String getTitle(int i) {
        return titles.get(i);
    }

    /**
     * Retourne le nombre de titres.
     *
     * @return nombre de titres.
     */
    public int getTitleCount() {
        return titles.size();
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<String> iterator() {
        return Collections.unmodifiableList(titles).iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "TableTitle[titles=" + titles + "]";
    }
}
