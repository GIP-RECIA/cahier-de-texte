/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GenericFilter.java,v 1.1 2009/05/26 08:08:15 ent_breyton Exp $
 */

package org.crlr.web.servlet.filter;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * The Class GenericFilter.
 */
public class GenericFilter implements Filter {
    /** The log. */
    protected final Log log = LogFactory.getLog(getClass());

    /** The filter config. */
    private FilterConfig filterConfig;

    /**
     * {@inheritDoc}
     */
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         FilterChain chain)
                  throws java.io.IOException, javax.servlet.ServletException {
        chain.doFilter(request, response);
    }

    /**
     * Gets the filter config.
     *
     * @return the filter config
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * {@inheritDoc}
     */
    public void init(FilterConfig config) {
        this.filterConfig = config;
    }

    /**
     * {@inheritDoc}
     */
    public void destroy() {
        this.filterConfig = null;
    }
}
