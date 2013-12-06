/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PlainTextReportGenerator.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import java.util.Collection;
import java.util.Map;

/**
 * Générateur d'éditions simple de type texte. Génère des éditions de type {@link
 * PlainTextReport}.
 *
 * @author breytond
 */
public class PlainTextReportGenerator extends AbstractReportGenerator<PlainTextReport> {
    /**
     * {@inheritDoc}
     */
    @Override
    protected PlainTextReport doGenerate(String templateLocation,
                                         Collection<Object> values,
                                         Map<String, Object> parameters)
                                  throws Exception {
        final String name = getReportName(parameters);
        final CharSequence data =
            TemplateUtils.mergeTemplate(templateLocation, values, parameters);

        return new PlainTextReport(name, data);
    }
}
