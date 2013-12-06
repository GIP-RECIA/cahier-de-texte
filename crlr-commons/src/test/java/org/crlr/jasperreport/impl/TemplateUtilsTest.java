/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TemplateUtilsTest.java,v 1.1 2009/05/26 07:34:42 ent_breyton Exp $
 */

package org.crlr.jasperreport.impl;

import org.crlr.report.impl.TemplateUtils;

import org.crlr.utils.DateUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Test de la classe {@link TemplateUtils}.
 *
 * @author breytond
 */
public class TemplateUtilsTest extends AbstractReportTest {
    /**
     *
     *
     * @throws Exception DOCUMENTATION INCOMPLETE!
     */
    public void testVelocityMergeTemplate() throws Exception {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("nom", "david");

        final CharSequence report =
            TemplateUtils.mergeTemplate(getTemplate("hello.txt.vm"), null, params);
        assertEquals("Hello david!", report.toString());
    }

    /**
     *
     *
     * @throws Exception DOCUMENTATION INCOMPLETE!
     */
    public void testGroovyMergeTemplate() throws Exception {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("nom", "david");

        final String formattedDate =
            DateUtils.format(DateUtils.creer(2009, Calendar.MAY, 25));

        final CharSequence report =
            TemplateUtils.mergeTemplate(getTemplate("hello.txt.groovy"), null, params);
        assertEquals("Hello david!\nDate = " + formattedDate, report.toString());
    }
}
