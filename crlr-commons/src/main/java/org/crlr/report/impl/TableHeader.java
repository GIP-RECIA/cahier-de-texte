/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TableHeader.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The Class TableHeader.
 * @author breytond.
 */
public class TableHeader implements Iterable<String> {
    /** The names. */
    private final List<String> names = new ArrayList<String>();

/**
     * The Constructor.
     * 
     * @param names
     *            the names
     */
    public TableHeader(final String... names) {
        if (names != null) {
            this.names.addAll(Arrays.asList(names));
        }
    }

/**
     * Constructeur.
     * @param names liste des noms.
     */
    public TableHeader(List<String> names) {
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(names)) {
            this.names.addAll(names);
        }
    }

    /**
     * Adds the column.
     *
     * @param name the name
     */
    public void addColumn(String name) {
        names.add(name);
    }

    /**
     * Adds the columns.
     * @param names liste de noms.
     */
    public void addColumns(String... names) {
        this.names.addAll(Arrays.asList(names));
    }

    /**
     * Retourne column.
     *
     * @param i the i
     *
     * @return the column
     */
    public String getColumn(int i) {
        return names.get(i);
    }

    /**
     * Retourne column count.
     *
     * @return the column count
     */
    public int getColumnCount() {
        return names.size();
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<String> iterator() {
        return Collections.unmodifiableList(names).iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "TableHeader[names=" + names + "]";
    }
}
