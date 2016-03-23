/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ReportUtils.java,v 1.1 2009/05/26 08:08:15 ent_breyton Exp $
 */

package org.crlr.web.utils;

import org.crlr.dto.application.base.TypeEdition;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import org.crlr.report.Report;

import org.crlr.utils.Assert;
import org.crlr.utils.IOUtils;

import java.io.IOException;

import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Méthodes utilitaires pour la gestion des éditions.
 *
 * @author breytond
 */
public final class ReportUtils {
    /** The Constant log. */
    protected final static Log log = LogFactory.getLog(ReportUtils.class);

/**
 * The Constructor.
 */
    private ReportUtils() {
    }

    /**
     * Envoie une édition au client.
     *
     * @param report the report
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void stream(Report report) throws IOException {
        Assert.isNotNull("report", report);

        final HttpServletRequest req = FacesUtils.getRequest();
        Assert.isNotNull("req", req);

        final HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute(Report.class.getSimpleName());
            session.setAttribute(Report.class.getSimpleName(), report);
        }
        final FacesContext fc = FacesContext.getCurrentInstance();
        final StateManager sm = fc.getApplication().getStateManager();
        sm.saveView(fc);
        fc.responseComplete();

        FacesUtils.redirect(ReportUtils.getUrlEdition(report));
    }

    /**
     * Envoie un fichier ZIP au client. Ce fichier contient un ou plusieurs
     * rapports d'édition.
     *
     * @param reports la liste des rapports à générer et à zipper au sein d'un seul
     *        fichier
     * @param name le nom du fichier zip
     *
     * @throws IOException en cas d'erreur d'écriture du zip
     */
    public static void zip(List<Report> reports, String name)
                    throws IOException {
        zip(reports, name, false);
    }

    /**
     * Envoie un fichier ZIP au client. Ce fichier contient un ou plusieurs
     * rapports d'édition.
     *
     * @param reports la liste des rapports à générer et à zipper au sein d'un seul
     *        fichier
     * @param name le nom du fichier zip
     * @param useReportName utilise le nom défini dans le report
     *
     * @throws IOException en cas d'erreur d'écriture du zip
     */
    public static void zip(List<Report> reports, String name, boolean useReportName)
                    throws IOException {
        Assert.isNotNull("reports", reports);
        Assert.isNotNull("nom", name);
        Assert.isTrue("reportsSize", reports.size() > 0);
        final String realName = name.replace(" ", "");
        final String nameZip = realName + ".zip";

        final HttpServletResponse resp = FacesUtils.getResponse();
        Assert.isNotNull("resp", resp);

        final ZipOutputStream out = new ZipOutputStream(resp.getOutputStream());
        int size = 0;
        int count = 1;
        final String reportName =
            ((realName.length() >= 6) ? realName.substring(0, 6) : realName) + "_";
        for (Report report : reports) {
            final String fileName =
                useReportName ? (report.getName() + report.getExtension())
                              : (reportName + count + report.getExtension());
            final ZipEntry entry = new ZipEntry(fileName);
            out.putNextEntry(entry);
            out.write(report.getData());
            size += entry.getSize();
            count++;
        }

        resp.setContentLength(size);
        resp.setContentType("application/zip");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-disposition",
                       "attachment" + "; name=" + nameZip + "; filename=" + nameZip);
        resp.setHeader("Pragma", "PRIVATE");
        resp.setHeader("Cache-control", "PRIVATE");
        resp.setHeader("Expires", "60");

        out.flush();
        IOUtils.close(out);

        final FacesContext fc = FacesContext.getCurrentInstance();
        final StateManager sm = fc.getApplication().getStateManager();
        sm.saveView(fc);

        fc.responseComplete();
    }

    /**
     * Cette méthode renvoie l'url de fichier edition.
     *
     * @param report the report
     *
     * @return the url edition
     */
    public static String getUrlEdition(Report report) {
        final String logoff = "/logoff.jsp";
        final String mimeType =
            (report.getMimeType() != null) ? report.getMimeType() : null;
        if (mimeType == null) {
            return logoff;
        }
        if (TypeEdition.CSV.equals(getTypeEdtion(report))) {
            return "/edition";
        }
        if (TypeEdition.PDF.equals(getTypeEdtion(report))) {
            return "/edition.pdf";
        }
        if (TypeEdition.RTF.equals(getTypeEdtion(report))) {
            return "/edition";
        }
        if (TypeEdition.TXT.equals(getTypeEdtion(report))) {
            return "/edition";
        }

        return logoff;
    }

    /**
     * Gets the type edtion.
     *
     * @param report the report
     *
     * @return the type edtion
     */
    public static TypeEdition getTypeEdtion(Report report) {
        final String mimeType =
            (report.getMimeType() != null) ? report.getMimeType() : "";
        if (mimeType.endsWith("csv")) {
            return TypeEdition.CSV;
        }
        if (mimeType.endsWith("pdf")) {
            return TypeEdition.PDF;
        }
        if (mimeType.startsWith("text/rtf")) {
            return TypeEdition.RTF;
        }
        if (mimeType.startsWith("text/plain")) {
            return TypeEdition.TXT;
        }
        return null;
    }
}
