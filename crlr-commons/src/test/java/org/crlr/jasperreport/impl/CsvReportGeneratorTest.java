/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: CsvReportGeneratorTest.java,v 1.1 2009/05/26 07:34:42 ent_breyton Exp $
 */

package org.crlr.jasperreport.impl;

import org.crlr.report.impl.CsvReport;
import org.crlr.report.impl.CsvReportGenerator;
import org.crlr.report.impl.TableHeader;
import org.crlr.report.impl.TableRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Test de la classe {@link CsvReportGenerator}.
 *
 * @author breytond
 */
public class CsvReportGeneratorTest extends AbstractReportTest {
    /**
     *
     *
     * @throws Exception DOCUMENTATION INCOMPLETE!
     */
    public void testGenerate() throws Exception {
        final TableHeader header = new TableHeader("Nom", "Age", "Classe");
        final List<TableRow> rows = new ArrayList<TableRow>();
        rows.add(new TableRow("DUPO\"ND;", 30, "CLA1"));
        rows.add(new TableRow("MAR,TIN", 16, "CLA2"));
        rows.add(new TableRow("DUMOU\nLIN", 15, "CLA3"));

        final CsvReportGenerator gen = new CsvReportGenerator();
        final CsvReport report = gen.generate(rows, header);
        assertNotNull(report);
        assertEquals("Nom,Age,Classe\n\"DUPO\"\"ND;\",30,CLA1\n\"MAR,TIN\",16,CLA2\n\"DUMOU\nLIN\",15,CLA3",
                     new String(report.getData()));

        log.info("Edition CSV générée :\n{0}", new String(report.getData()));
    }
}
