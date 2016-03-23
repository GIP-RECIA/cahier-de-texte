/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: FixSizeReportGeneratorTest.java,v 1.1 2009/05/26 07:34:42 ent_breyton Exp $
 */

package org.crlr.jasperreport.impl;

import java.util.ArrayList;
import java.util.List;

import org.crlr.report.impl.FixSizeReport;
import org.crlr.report.impl.FixSizeReportGenerator;
import org.crlr.report.impl.FixSizeTableCell;
import org.crlr.report.impl.FixSizeTableRow;

/**
 * Test de la classe {@link FixSizeReportGenerator}.
 */
public class FixSizeReportGeneratorTest extends AbstractReportTest {
    /**
     *
     *
     * @throws Exception DOCUMENTATION INCOMPLETE!
     */
    public void testGenerate() throws Exception {
        final FixSizeTableRow header =
            new FixSizeTableRow(new FixSizeTableCell(10, "Nom"),
                                new FixSizeTableCell(6, "Prénom"),
                                new FixSizeTableCell(3, "Age"));

        final List<FixSizeTableRow> rows = new ArrayList<FixSizeTableRow>();
        rows.add(new FixSizeTableRow(new FixSizeTableCell(10, "DUPOND"),
                                     new FixSizeTableCell(6, "Henri"),
                                     new FixSizeTableCell(3, 38)));
        rows.add(new FixSizeTableRow(new FixSizeTableCell(10, "MARTIN_MARTIN"),
                                     new FixSizeTableCell(6, "AL_AL"),
                                     new FixSizeTableCell(3, 101)));
        rows.add(new FixSizeTableRow(new FixSizeTableCell(10, "DUMOULIN"),
                                     new FixSizeTableCell(6, "Jean"),
                                     new FixSizeTableCell(3, 17)));

        final FixSizeReportGenerator gen = new FixSizeReportGenerator();
        final FixSizeReport report = gen.generate(rows, header);
        assertNotNull(report);
        assertEquals("Nom       PrénomAge\nDUPOND    Henri 038\nMARTIN_MARAL_AL 101\nDUMOULIN  Jean  017\n",
                     new String(report.getData()));

        log.info("Edition Texte largeur fixe générée :\n{0}", new String(report.getData()));
    }
}
