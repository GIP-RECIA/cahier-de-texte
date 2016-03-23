/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: FixSizeTableRow.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The Class FixSizeTableRow.
 * @author breytond.
 */
public class FixSizeTableRow implements Iterable<FixSizeTableCell> {
    /** The values. */
    private final List<FixSizeTableCell> values = new ArrayList<FixSizeTableCell>();

/**
     * The Constructor.
     * 
     * @param values
     *            the values
     */
    public FixSizeTableRow(final FixSizeTableCell... values) {
        if (values != null) {
            this.values.addAll(Arrays.asList(values));
        }
    }

    /**
     * Adds the column.
     *
     * @param value the value
     */
    public void addColumn(FixSizeTableCell value) {
        values.add(value);
    }

    /**
     * Adds the columns.
     * @param values liste de valeur.
     */
    public void addColumns(FixSizeTableCell... values) {
        this.values.addAll(Arrays.asList(values));
    }

    /**
     * Retourne column.
     *
     * @param i the i
     *
     * @return the column
     */
    public FixSizeTableCell getColumn(int i) {
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
    public Iterator<FixSizeTableCell> iterator() {
        return Collections.unmodifiableList(values).iterator();
    }

    /**
     * Retourne columns.
     *
     * @return values
     */
    public List<FixSizeTableCell> getColumns() {
        return values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "FixSizeTableRow[values=" + values + "]";
    }
}
