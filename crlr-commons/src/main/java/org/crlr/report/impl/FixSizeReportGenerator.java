/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: FixSizeReportGenerator.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Générateur d'édition de type Texte à largeur fixe. Génère des éditions de type
 * {@link FixSizeReport}.
 *
 * @author breytond.
 */
public class FixSizeReportGenerator extends PlainTextReportGenerator {
    /** The Constant PARAM_REPORT_HEADER. */
    public static final String PARAM_REPORT_HEADER = "_REPORT_HEADER";

    /** The Constant LINE_SEPARATOR. */
    private static final String LINE_SEPARATOR = "\n";

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Boolean uniquementEspace = false;

/**
     * Constructeur.
     */
    public FixSizeReportGenerator() {
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

        final Collection<Map<Integer, String>> columnHeads =
            (Collection<Map<Integer, String>>) ObjectUtils.defaultIfNull(parameters.get(PARAM_REPORT_HEADER),
                                                                         Collections.emptyList());
        final FixSizeTableRow header = new FixSizeTableRow();
        for (final Map<Integer, String> head : columnHeads) {
            for (Map.Entry<Integer, String> entry : head.entrySet()) {
                header.addColumn(new FixSizeTableCell(entry.getKey(), entry.getValue()));
            }
        }

        final List<FixSizeTableRow> rows = new ArrayList<FixSizeTableRow>();
        for (final Object obj : values) {
            if (obj instanceof FixSizeTableRow) {
                rows.add((FixSizeTableRow) obj);
            }
        }

        return generate(rows, header);
    }

    /**
     * Génère une édition texte avec un fichier de largeur fixe.
     *
     * @param rows the rows
     * @param header the header
     *
     * @return the csv report
     */
    public FixSizeReport generate(List<FixSizeTableRow> rows, FixSizeTableRow header) {
        final Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("header", header);

        final StringBuilder buf = new StringBuilder();

        // ajout de l'entête
        if (header != null) {
            addRow(buf, header);
        }

        // ajout du corps
        if (rows != null) {
            for (final Iterator<FixSizeTableRow> i = rows.iterator(); i.hasNext();) {
                addRow(buf, i.next());
            }
        }

        final String name = getReportName(params);
        return new FixSizeReport(name, buf);
    }

    /**
     * Ajoute la ligne dans le buffer.
     *
     * @param buffer buffer
     * @param row ligne
     */
    private void addRow(final StringBuilder buffer, FixSizeTableRow row) {
        if (row.getColumnCount() != 0) {
            for (final Iterator<FixSizeTableCell> j = row.iterator(); j.hasNext();) {
                final FixSizeTableCell cell = j.next();
                final Object cellValue = cell.getValue();

                String value;
                if (cellValue instanceof Number && !uniquementEspace) {
                    value = StringUtils.leftPad(cellValue.toString(), cell.getWidth(), '0');
                } else if (cellValue instanceof Date) {
                    value = ObjectUtils.defaultIfNull(DateUtils.format((Date) cellValue),
                                                      "");
                } else {
                    value = ObjectUtils.defaultIfNull(cellValue, "")
                                       .toString();
                }
                buffer.append(resize(cell.getWidth(), value));
            }
            buffer.append(LINE_SEPARATOR);
        }
    }

    /**
     * Retourne la string redimensionnée.
     *
     * @param width width
     * @param value value
     *
     * @return value à la taille width
     */
    private String resize(int width, String value) {
        return StringUtils.rightPad(value, width, ' ');
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param uniquementEspace the uniquementEspace to set
     */
    public void setUniquementEspace(Boolean uniquementEspace) {
        this.uniquementEspace = uniquementEspace;
    }
}
