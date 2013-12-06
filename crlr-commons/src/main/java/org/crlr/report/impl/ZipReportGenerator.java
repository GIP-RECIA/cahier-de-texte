/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ZipReportGenerator.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import org.crlr.report.Report;
import org.crlr.report.ReportException;

import org.crlr.utils.Assert;

import java.io.ByteArrayOutputStream;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Générateur d'éditions de type ZIP. Génère des éditions de type {@link ZipReport}.
 *
 * @author breytond
 */
public class ZipReportGenerator extends AbstractReportGenerator<ZipReport> {
    /**
     * Generation du ZipReport.
     *
     * @param name nom du zip
     * @param report report à ajouter dans le zip.
     *
     * @return zipReport
     */
    public ZipReport generate(String name, Report report) {
        final Map<String, Report> map = new HashMap<String, Report>();
        map.put(name, report);
        return generate(name, map);
    }

    /**
     * Generation du ZipReport.
     *
     * @param name name
     * @param mapReports map des reports à ajouter dans le zip (la cle est le nom du
     *        report dans le zip).
     *
     * @return ZipReport
     */
    public ZipReport generate(String name, Map<String, Report> mapReports) {
        try {
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            final ZipOutputStream out = new ZipOutputStream(os);
            for (Map.Entry<String, Report> mapEntry : mapReports.entrySet()) {
                final Report report = mapEntry.getValue();
                Assert.isNotNull("report", report);

                final String nomReport = mapEntry.getKey();
                Assert.isNotNull("nomReport", nomReport);

                final ZipEntry zipEntry = new ZipEntry(nomReport);
                out.putNextEntry(zipEntry);
                out.write(report.getData());
            }
            out.flush();
            out.finish();
            return new ZipReport(name, os.toByteArray());
        } catch (Exception e) {
            throw new ReportException(e,
                                      "Erreur durant la génération " +
                                      "du zip nomme {0}", name);
        }
    }

    /**
     * Do generate.
     *
     * @param templateLocation the template location
     * @param values the values
     * @param parameters the parameters
     *
     * @return the report type
     *
     * @throws Exception the exception
     */
    @Override
    protected ZipReport doGenerate(String templateLocation, Collection<Object> values,
                                   Map<String, Object> parameters)
                            throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
