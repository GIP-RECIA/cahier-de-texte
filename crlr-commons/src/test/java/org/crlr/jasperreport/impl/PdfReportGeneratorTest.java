/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PdfReportGeneratorTest.java,v 1.1 2009/05/26 07:34:42 ent_breyton Exp $
 */

package org.crlr.jasperreport.impl;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.crlr.report.impl.PdfReport;
import org.crlr.report.impl.PdfReportGenerator;
import org.crlr.utils.DateUtils;

/**
 * Test de la classe {@link PdfReportGenerator}.
 *
 * @author breytond.
 */
public class PdfReportGeneratorTest extends AbstractReportTest {
    /**
     *
     *
     * @throws Exception DOCUMENTATION INCOMPLETE!
     */
    public void testGenerate() throws Exception {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("nom", System.getProperty("user.name"));
        params.put("date", new Date());
        
        
       
                log.info(getTemplate("HelloJasperReports.jasper"));
        final PdfReportGenerator gen = new PdfReportGenerator();
        final PdfReport report =
            gen.generate(getTemplate("HelloJasperReports.jasper"), null, params);
        assertNotNull(report);
        assertFalse(report.getData().length == 0);

        final File tmpFile =
            File.createTempFile("crlrreport-HelloJasperReports-", ".pdf");
        dump(report.getData(), tmpFile);
        log.info("Édition générée : {0}", tmpFile.getAbsolutePath());
        
        
    }

    /**
     *
     *
     * @throws Exception DOCUMENTATION INCOMPLETE!
     */
    public void testGenerateListe() throws Exception {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("date", DateUtils.creer(2009, Calendar.MAY, 25, 10, 30, 0));

        final PdfReportGenerator gen = new PdfReportGenerator();
        final PdfReport report =
            gen.generate(getTemplate("HelloJasperReports.jasper"), createListeNom(), params);
        assertNotNull(report);
        assertFalse(report.getData().length == 0);

        final File tmpFile = File.createTempFile("crlrreport-ListePersonnes-", ".pdf");
        dump(report.getData(), tmpFile);
        log.info("Édition générée : {0}", tmpFile.getAbsolutePath());
    }
}
