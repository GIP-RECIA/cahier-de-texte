/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EditionFilter.java,v 1.2 2010/01/05 16:40:59 ent_breyton Exp $
 */

package org.crlr.web.servlet.filter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.crlr.dto.application.base.TypeEdition;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.report.Report;
import org.crlr.utils.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.web.utils.ReportUtils;

/**
 * The Class EditionFilter.
 */
public class EditionFilter extends GenericFilter {
    protected final Log log = LogFactory.getLog(getClass());
    
    /**
     * Cette méthode renvoie le contenu de l'édition.
     *
     * @param request the request
     * @param response the response
     * @param chain the chain
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final HttpServletRequest httpRequest = (HttpServletRequest) request;

        chain.doFilter(request, response);

        log.info("Edition doFilter");
        final HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            final Object o = session.getAttribute(Report.class.getSimpleName());
            if (o != null) {
                session.removeAttribute(Report.class.getSimpleName());
                final OutputStream out = httpResponse.getOutputStream();
                if (o instanceof Report) {
                    final Report report = (Report) o;
                    final byte[] data = report.getData();
                    log.info("Edition data taille {0}", data.length);
                    httpResponse.setContentLength(data.length);
                    httpResponse.setContentType(report.getMimeType());
                    httpResponse.setCharacterEncoding("UTF-8");

                    final String header =  httpRequest.getHeader("User-Agent");
                    final boolean msie =
                        header.matches("(.*)MSIE(.*)");
                    
                    final boolean chrome =
                        header.matches("(.*)Chrome(.*)");

                    if (!msie) {
                        // Tout autre navigateur
                        if (chrome || !TypeEdition.PDF.equals(ReportUtils.getTypeEdtion(report))) {
                            httpResponse.setContentType("application/download");                           
                        }
                        responseWithAttachment(httpResponse, report, false);
                        responseWithNoCache(httpResponse);
                    } else {
                        // IE
                        if (TypeEdition.PDF.equals(ReportUtils.getTypeEdtion(report))) {                    
                            responseWithNoCache(httpResponse);
                            responseWithAttachment(httpResponse, report, true);
                        } else {
                            if (TypeEdition.TXT.equals(ReportUtils.getTypeEdtion(report))) {
                                httpResponse.setContentType("application/octet-stream");
                            } else {
                                httpResponse.setContentType("application/unknow");
                            }
                            responseWithAttachment(httpResponse, report, false);

                            httpResponse.setHeader("Pragma", "public");
                            httpResponse.setHeader("Cache-control", "max-age=30");
                            httpResponse.setHeader("Expires", "0");
                        }
                    }

                    out.write(data);
                }
                out.flush();
                out.close();
                httpResponse.flushBuffer();
            }
        }
    }

    /**
     * Response with no cache.
     *
     * @param httpResponse the http response
     */
    private void responseWithNoCache(HttpServletResponse httpResponse) {
        httpResponse.setHeader("Cache-control",
                               "private,must-revalidate,no-store,max-age=0");
        httpResponse.setHeader("Expires", "Mon, 31 Dec 2007 13:00:00 GMT");
    }

    /**
     * Response with attachment.
     *
     * @param httpResponse the http response
     * @param report the report
     * @param ie vrai ou faux ie pdf.
     */
    private void responseWithAttachment(HttpServletResponse httpResponse, Report report, final Boolean ie) {
        final String fileName =
            ObjectUtils.defaultIfNull(StringUtils.trimToNull(report.getName() +
                                                             report.getExtension()),
                                      "report" + report.getExtension());

        httpResponse.setHeader("Content-disposition",
                               ((ie) ? "attachment" : report.getModeContentDisposition()) + "; filename=" +
                               fileName);
    }
}