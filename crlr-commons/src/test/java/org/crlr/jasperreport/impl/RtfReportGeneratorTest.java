/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RtfReportGeneratorTest.java,v 1.1 2009/05/26 07:34:42 ent_breyton Exp $
 */

package org.crlr.jasperreport.impl;

import org.crlr.report.impl.RtfReport;
import org.crlr.report.impl.RtfReportGenerator;

import org.crlr.utils.DateUtils;

import java.io.File;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Test de la classe {@link RtfReportGenerator}.
 *
 * @author breytond.
 */
public class RtfReportGeneratorTest extends AbstractReportTest {
    /**
     *
     *
     * @throws Exception DOCUMENTATION INCOMPLETE!
     */
    public void testGenerate() throws Exception {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("nom", System.getProperty("user.name"));
        params.put("date", new Date());

        final RtfReportGenerator gen = new RtfReportGenerator();
        final RtfReport report =
            gen.generate(getTemplate("HelloJasperReports.jasper"), null, params);
        assertNotNull(report);
        assertFalse(report.getData().length == 0);

        final File tmpFile =
            File.createTempFile("crlrreport-HelloJasperReports-", ".rtf");
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
        params.put("date", DateUtils.creer(2006, Calendar.OCTOBER, 18, 10, 30, 0));

        final RtfReportGenerator gen = new RtfReportGenerator();
        final RtfReport report =
            gen.generate(getTemplate("ListePersonnes.jasper"), createListeNom(), params);
        assertNotNull(report);
        assertFalse(report.getData().length == 0);

        final File tmpFile = File.createTempFile("crlrreport-ListePersonnes-", ".rtf");
        dump(report.getData(), tmpFile);
        log.info("Édition générée : {0}", tmpFile.getAbsolutePath());
    }
}
