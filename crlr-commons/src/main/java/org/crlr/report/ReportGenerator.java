/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ReportGenerator.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report;

import java.util.Collection;
import java.util.Map;

/**
 * Générateur d'édition.
 * 
 * @param <ReportType>
 *            implémentation de l'interface {@link Report} utilisée par le
 *            générateur
 * 
 * @author breytond
 */
public interface ReportGenerator<ReportType extends Report> {
    /**
     * Génère une édition.
     *
     * @param templateLocation the template location (un ficher *.jasper!)
     * @param values the values
     * @param parameters the parameters
     *
     * @return the report type
     */
    ReportType generate(String templateLocation, Collection<?extends Object> values,
                        Map<String, Object> parameters);
}
