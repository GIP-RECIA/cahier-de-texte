/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PlainTextGeneratorTest.java,v 1.1 2009/05/26 07:34:42 ent_breyton Exp $
 */

package org.crlr.jasperreport.impl;

import org.crlr.report.impl.PlainTextReport;
import org.crlr.report.impl.PlainTextReportGenerator;
import org.crlr.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test de la classe {@link PlainTextReportGenerator}.
 *
 * @author breytond
 */
public class PlainTextGeneratorTest extends AbstractReportTest {
    /**
     *
     */
    public void testVelocityGenerate() {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("nom", "david");

        final PlainTextReportGenerator gen = new PlainTextReportGenerator();
        final PlainTextReport report =
            gen.generate(getTemplate("hello.txt.vm"), null, params);
        assertNotNull(report);
        assertEquals("Hello david!", report.getDataAsCharSequence().toString());
        assertEquals("text/plain", report.getMimeType());
    }

    /**
     *
     */
    public void testGroovyGenerate() {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("nom", "david");

        final String formattedDate =
            DateUtils.format(DateUtils.creer(2009, Calendar.MAY, 25));

        final PlainTextReportGenerator gen = new PlainTextReportGenerator();
        final PlainTextReport report =
            gen.generate(getTemplate("hello.txt.groovy"), null, params);
        assertNotNull(report);
        assertEquals("Hello david!\nDate = " + formattedDate, new String(report.getData()));
        assertEquals("text/plain", report.getMimeType());
    }

    /**
     *
     */
    public void testCsvGroovyTemplate() {
        final List<TestDTO> listePersonne = new ArrayList<TestDTO>();
        listePersonne.add(new TestDTO("DUPOND", "Jean", 40));
        listePersonne.add(new TestDTO("MARTIN", "Philippe", 35));

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("listePersonne", listePersonne);

        final PlainTextReportGenerator gen = new PlainTextReportGenerator();
        final PlainTextReport report =
            gen.generate(getTemplate("listePersonne.csv.groovy"), null, params);
        report.setMimeType("text/csv");
        assertNotNull(report);
        assertEquals("# Nom;Prénom;Age\nDUPOND;Jean;40\nMARTIN;Philippe;35\n",
                     new String(report.getData()));
        assertEquals("text/csv", report.getMimeType());
    }
}
