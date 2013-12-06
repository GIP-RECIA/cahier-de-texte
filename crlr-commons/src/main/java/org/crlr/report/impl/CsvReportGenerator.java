/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: CsvReportGenerator.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;

/**
 * Générateur d'édition de type CSV. Génère des éditions de type {@link CsvReport}.
 *
 * @author breytond
 *
 * @see http://www.shaftek.org/publications/drafts/mime-csv/
 * @see RFC 4180
 */
public class CsvReportGenerator extends PlainTextReportGenerator {
    /** The Constant PARAM_REPORT_HEADER. */
    public static final String PARAM_REPORT_HEADER = "_REPORT_HEADER";

    /** The Constant COLUMN_SEPARATOR. */
    private static final String COLUMN_SEPARATOR = ",";

    /** The Constant LINE_SEPARATOR. */
    private static final String LINE_SEPARATOR = "\n";

    /** Separateur à utiliser. */
    private String currentSeparator;

    /** Faut-il supprimer les accents. Oui par défaut. */
    private boolean supprimerAccents = true;

/**
     * Constructeur.
     */
    public CsvReportGenerator() {
        currentSeparator = COLUMN_SEPARATOR;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected PlainTextReport doGenerate(String templateLocation,
                                         Collection<Object> values,
                                         Map<String, Object> parameters)
                                  throws Exception {
        if (templateLocation != null) {
            log.warning("Le paramètre {0} n'est pas pris en compte", "templateLocation");
        }

        final Collection<String> columnHeads =
            (Collection<String>) ObjectUtils.defaultIfNull(parameters.get(PARAM_REPORT_HEADER),
                                                           Collections.emptyList());
        final TableHeader header = new TableHeader();
        for (final String columnHead : columnHeads) {
            header.addColumn(columnHead);
        }

        final List<TableRow> rows = new ArrayList<TableRow>();
        for (final Object obj : values) {
            if (obj instanceof TableRow) {
                rows.add((TableRow) obj);
            }
        }

        return generate(rows, header);
    }

    /**
     * {@inheritDoc}
     */
    public CsvReport generate(List<TableRow> rows, TableHeader header) {
        return generate(rows, header, null);
    }

    /**
     * Génère une édition CSV.
     *
     * @param rows the rows
     * @param header the header
     * @param title the title
     *
     * @return the csv report
     */
    public CsvReport generate(List<TableRow> rows, TableHeader header, TableTitle title) {
        final Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("header", header);

        final StringBuilder buf = new StringBuilder();

        // ajout des titres
        if (title != null) {
            if (title.getTitleCount() != 0) {
                for (final Iterator<String> i = title.iterator(); i.hasNext();) {
                    String name = i.next();
                    name = ObjectUtils.defaultIfNull(name, "");
                    buf.append(escapeCsvValue(supprimerAccents(name)));
                    if (i.hasNext()) {
                        buf.append(LINE_SEPARATOR);
                    }
                }

                buf.append(LINE_SEPARATOR);
            }
        }

        // ajout de l'entête
        if (header != null) {
            if (header.getColumnCount() != 0) {
                for (final Iterator<String> i = header.iterator(); i.hasNext();) {
                    String name = i.next();
                    name = ObjectUtils.defaultIfNull(name, "");
                    buf.append(escapeCsvValue(supprimerAccents(name)));
                    if (i.hasNext()) {
                        buf.append(currentSeparator);
                    }
                }

                buf.append(LINE_SEPARATOR);
            }
        }

        // ajout du corps
        if (rows != null) {
            for (final Iterator<TableRow> i = rows.iterator(); i.hasNext();) {
                final TableRow row = i.next();
                final int c = row.getColumnCount();
                if (c != 0) {
                    for (final Iterator<Object> j = row.iterator(); j.hasNext();) {
                        final Object cellValue = j.next();
                        String value;
                        if (cellValue instanceof Date) {
                            value = ObjectUtils.defaultIfNull(DateUtils.format((Date) cellValue),
                                                              "");
                        } else {
                            value = ObjectUtils.defaultIfNull(cellValue,
                                                              "")
                                               .toString();
                        }
                        buf.append(escapeCsvValue(supprimerAccents(value)));
                        if (j.hasNext()) {
                            buf.append(currentSeparator);
                        }
                    }

                    if (i.hasNext()) {
                        buf.append(LINE_SEPARATOR);
                    }
                }
            }
        }

        final String name = getReportName(params);
        return new CsvReport(name, buf);
    }

    /**
     * Supprimer les accents.
     *
     * @param value value
     *
     * @return chaine sans accent si le parametre est fixé.
     */
    private String supprimerAccents(String value) {
        if (supprimerAccents) {
            return org.crlr.utils.StringUtils.sansAccent(value);
        } else {
            return value;
        }
    }

    /**
     * Escape csv value.
     *
     * @param value the value
     *
     * @return the char sequence
     */
    private CharSequence escapeCsvValue(String value) {
        if (value == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder(value.length() + 2);
        final boolean addQuotes =
            (value.indexOf('"') != -1) || (value.indexOf('\n') != -1) ||
            (value.indexOf(currentSeparator) != -1);
        if (addQuotes) {
            buf.append('"');
            buf.append(value.replaceAll("\"", "\"\""));
            buf.append('"');
        } else {
            buf.append(value);
        }

        return buf;
    }

    /**
     * Returns the currentSeparator of this CsvReportGenerator.
     *
     * @return the currentSeparator
     */
    public String getCurrentSeparator() {
        return currentSeparator;
    }

    /**
     * Sets the currentSeparator attribute of this CsvReportGenerator.
     *
     * @param currentSeparator the currentSeparator to set
     */
    public void setCurrentSeparator(String currentSeparator) {
        this.currentSeparator = currentSeparator;
    }

    /**
     * Returns the supprimerAccents of this CsvReportGenerator.
     *
     * @return the supprimerAccents
     */
    public boolean getSupprimerAccents() {
        return supprimerAccents;
    }

    /**
     * Sets the supprimerAccents attribute of this CsvReportGenerator.
     *
     * @param supprimerAccents the supprimerAccents to set
     */
    public void setSupprimerAccents(boolean supprimerAccents) {
        this.supprimerAccents = supprimerAccents;
    }
}
