/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PdfReportGenerator.java,v 1.1 2009/05/26 07:33:22 ent_breyton Exp $
 */

package org.crlr.report.impl;

import java.util.Collection;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Générateur d'éditions de type PDF. Génère des éditions de type {@link PdfReport}.
 *
 * @author breytond.
 */
public class PdfReportGenerator extends AbstractReportGenerator<PdfReport> {
    /**
     * {@inheritDoc}
     */
    @Override
    protected PdfReport doGenerate(String templateLocation, Collection<Object> values,
                                   Map<String, Object> parameters)
                            throws Exception {
        //La compilation est effectuée par une tâche ant
        //final JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(templateLocation));
        //JasperCompileManager.compileReport(jasperDesign);
        final JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream(templateLocation));
        
        // Ajout d'une map d'accents
        if (parameters != null) {
            parameters.put("mapAccents", org.crlr.utils.StringUtils.MAP_ACCENTS_REPORT);
        }

        // s'il n'y a aucun élément dans la collection, il faut utiliser
        // un JREmptyDataSource au lieu d'un JRBeanCollectionDataSource
        final JRDataSource ds =
            values.isEmpty() ? new JREmptyDataSource()
                             : new JRBeanCollectionDataSource(values);
            
            
            
        final JasperPrint jp = JasperFillManager.fillReport(jasperReport, parameters, ds);

        final byte[] data = JasperExportManager.exportReportToPdf(jp);
        final String name = getReportName(parameters);

        return new PdfReport(name, data);
    }
    
    protected PdfReport doGenerate(JasperDesign jasperDesign,
			Collection<Object> values, Map<String, Object> parameters)
			throws Exception {
    	final JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        
    	// Ajout d'une map d'accents
        if (parameters != null) {
            parameters.put("mapAccents", org.crlr.utils.StringUtils.MAP_ACCENTS_REPORT);
        }

        // s'il n'y a aucun élément dans la collection, il faut utiliser
        // un JREmptyDataSource au lieu d'un JRBeanCollectionDataSource
        final JRDataSource ds =
            values.isEmpty() ? new JREmptyDataSource()
                             : new JRBeanCollectionDataSource(values);
            
            
            
        final JasperPrint jp = JasperFillManager.fillReport(jasperReport, parameters, ds);

        final byte[] data = JasperExportManager.exportReportToPdf(jp);
        final String name = getReportName(parameters);

        return new PdfReport(name, data);
    }
}
