/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DtoComparateur.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils.dto;

import java.io.Serializable;

import java.util.Date;

/**
 * Dto de tests.
 */
public class DtoComparateur implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The string1. */
    private String string1;

    /** The string2. */
    private String string2;

    /** The date1. */
    private Date date1;

    /** The date2. */
    private Date date2;

/**
     * Instantiates a new dto comparateur.
     */
    public DtoComparateur() {
        super();
    }

/**
     * Instantiates a new dto comparateur.
     * 
     * @param string1 the string1
     * @param string2 the string2
     * @param date1 the date1
     * @param date2 the date2
     */
    public DtoComparateur(String string1, String string2, Date date1, Date date2) {
        super();
        this.string1 = (string1 == null) ? "" : string1;
        this.string2 = (string2 == null) ? "" : string2;
        this.date1 = (date1 == null) ? new Date() : date1;
        this.date2 = (date2 == null) ? new Date() : date2;
    }

    /**
     * Gets the string1.
     *
     * @return the string1
     */
    public String getString1() {
        return string1;
    }

    /**
     * Sets the string1.
     *
     * @param string1 the new string1
     */
    public void setString1(String string1) {
        this.string1 = string1;
    }

    /**
     * Gets the string2.
     *
     * @return the string2
     */
    public String getString2() {
        return string2;
    }

    /**
     * Sets the string2.
     *
     * @param string2 the new string2
     */
    public void setString2(String string2) {
        this.string2 = string2;
    }

    /**
     * Gets the date1.
     *
     * @return the date1
     */
    public Date getDate1() {
        return date1;
    }

    /**
     * Sets the date1.
     *
     * @param date1 the new date1
     */
    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    /**
     * Gets the date2.
     *
     * @return the date2
     */
    public Date getDate2() {
        return date2;
    }

    /**
     * Sets the date2.
     *
     * @param date2 the new date2
     */
    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "TestDTO[string1:" + string1 + "/string2:" + string2 + "/date1:" +
               date1.toString() + "/date2:" + date2.toString() + "]";
    }
}
