/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RtfReportGenerator.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

/**
 *
 */
package org.crlr.report.impl;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRRtfExporter;

import org.crlr.report.ReportException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.util.Collection;
import java.util.Map;

/**
 * RtfReportGenerator.
 * @author breytond.
 */
public class RtfReportGenerator extends AbstractReportGenerator<RtfReport> {
    /**
     * Génération du RTF.
     *
     * @param templateLocation le template location
     * @param values le values
     * @param parameters le parameters
     *
     * @return the rtf report
     *
     * @throws Exception the exception
     */
    @Override
    protected RtfReport doGenerate(String templateLocation, Collection<Object> values,
                                   Map<String, Object> parameters)
                            throws Exception {
        final InputStream input = getClass()
                                      .getResourceAsStream(templateLocation);
        if (input == null) {
            throw new ReportException("Modele d'edition non trouve : {0}",
                                      templateLocation);
        }

        final JRDataSource ds =
            values.isEmpty() ? new JREmptyDataSource()
                             : new JRBeanCollectionDataSource(values);

        final String name = getReportName(parameters);
        final JasperPrint jp = JasperFillManager.fillReport(input, parameters, ds);

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final JRRtfExporter rtf = new JRRtfExporter();
        rtf.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        rtf.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
        rtf.exportReport();

        return new RtfReport(name, output.toByteArray());
    }
}
