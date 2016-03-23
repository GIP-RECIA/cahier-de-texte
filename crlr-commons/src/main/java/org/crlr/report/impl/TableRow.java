/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TableRow.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The Class TableRow.
 *
 * @author breytond.
 */
public class TableRow implements Iterable<Object> {
    /** The values. */
    private final List<Object> values = new ArrayList<Object>();

/**
     * The Constructor.
     * 
     * @param values
     *            the values
     */
    public TableRow(final Object... values) {
        if (values != null) {
            this.values.addAll(Arrays.asList(values));
        }
    }

    /**
     * Adds the column.
     *
     * @param value the value
     */
    public void addColumn(Object value) {
        values.add(value);
    }

    /**
     * Adds the columns.
     * @param values liste de valeurs.
     */
    public void addColumns(Object... values) {
        this.values.addAll(Arrays.asList(values));
    }

    /**
     * Retourne column.
     *
     * @param i the i
     *
     * @return the column
     */
    public Object getColumn(int i) {
        return values.get(i);
    }

    /**
     * Retourne column count.
     *
     * @return the column count
     */
    public int getColumnCount() {
        return values.size();
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<Object> iterator() {
        return Collections.unmodifiableList(values).iterator();
    }

    /**
     * Retourne columns.
     *
     * @return values
     */
    public List<Object> getColumns() {
        return values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "TableRow[values=" + values + "]";
    }
}
