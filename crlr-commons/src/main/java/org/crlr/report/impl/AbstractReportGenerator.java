/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AbstractReportGenerator.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.report.Report;
import org.crlr.report.ReportException;
import org.crlr.report.ReportGenerator;
import org.crlr.utils.Chrono;
import org.crlr.utils.ObjectUtils;

/**
 * Classe abstraite de base pour toutes les implémentations de l'interface
 * {@link ReportGenerator}.
 * 
 * @author breytond
 * 
 * @param <ReportType>
 *            implémentation de l'interface {@link Report} utilisée par le
 *            générateur.
 */
public abstract class AbstractReportGenerator<ReportType extends Report>
		implements ReportGenerator<ReportType> {
	/** The Constant PARAM_REPORT_NAME. */
	public static final String PARAM_REPORT_NAME = "_REPORT_NAME";

	/** The log. */
	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * {@inheritDoc}	 
	 * @see org.crlr.report.ReportGenerator#generate(java.lang.String, java.util.Collection, java.util.Map)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final ReportType generate(String templateLocation,
			Collection<? extends Object> values, Map<String, Object> parameters) {
		final Collection<Object> myValues = ObjectUtils.defaultIfNull(values,
				new ArrayList(0));
		final Map<String, Object> myParameters = ObjectUtils.defaultIfNull(
				parameters, new HashMap<String, Object>(0));

		log.debug(
				"Génération de l'édition : templateLocation={0}, values={1}, parameters={2}",
				templateLocation, myValues, myParameters);

		final Chrono chrono = new Chrono();
		chrono.start();
		try {
			return doGenerate(templateLocation, myValues, myParameters);
		} catch (Exception e) {
			throw new ReportException(e, "Erreur durant la génération "
					+ "de l'édition à partir du module : {0}", templateLocation);
		} finally {
			chrono.stop();
			log.debug(
					"Temps passé pour la génération de l'édition '{0}' : {1} ms",
					templateLocation, chrono.getElapsed());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final ReportType generate(JasperDesign report,
			Collection<? extends Object> values, Map<String, Object> parameters) {
		final Collection<Object> myValues = ObjectUtils.defaultIfNull(values,
				new ArrayList(0));
		final Map<String, Object> myParameters = ObjectUtils.defaultIfNull(
				parameters, new HashMap<String, Object>(0));


		final Chrono chrono = new Chrono();
		chrono.start();
		try {
			return doGenerate(report, myValues, myParameters);
		} catch (Exception e) {
			throw new ReportException(e, "Erreur durant la génération "
					+ "de l'édition à partir du module : ");
		} finally {
			chrono.stop();
			log.debug(
					"Temps passé pour la génération de l'édition : {1} ms",
					 chrono.getElapsed());
		}
	}

	public final JasperDesign getJasperReport(String templateLocation) {
		//La compilation est effectuée par une tâche ant
		
		try {
        final JasperDesign jasperDesign = JRXmlLoader.load(getClass()
                .getResourceAsStream(templateLocation));
        //final JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        
           
            
            return jasperDesign;
		} catch (Exception ex) {
			log.warning(ex, "ex");
			return null;
		}
	}

	/**
	 * Do generate.
	 * 
	 * @param templateLocation
	 *            the template location
	 * @param values
	 *            the values
	 * @param parameters
	 *            the parameters
	 * 
	 * @return the report type
	 * 
	 * @throws Exception
	 *             the exception
	 */
	protected abstract ReportType doGenerate(String templateLocation,
			Collection<Object> values, Map<String, Object> parameters)
			throws Exception;
	
	protected  ReportType doGenerate(JasperDesign templateLocation,
			Collection<Object> values, Map<String, Object> parameters)
			throws Exception {
		return null;
	}

	/**
	 * Retourne le nom d'un rapport. Le nom du rapport est spécifié par la clé
	 * portée par la constante {@link #PARAM_REPORT_NAME}. Si aucun nom n'est
	 * spécifié, un nom aléatoire est utilisé.
	 * 
	 * @param params
	 *            the params
	 * 
	 * @return the report name
	 */
	protected String getReportName(Map<String, Object> params) {
		final String name;
		if (params.containsKey(PARAM_REPORT_NAME)) {
			name = params.get(PARAM_REPORT_NAME).toString();
		} else {
			name = "report-" + System.currentTimeMillis();
		}
		return name;
	}
}
