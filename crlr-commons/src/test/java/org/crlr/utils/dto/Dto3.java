/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Dto3.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils.dto;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Dto de tests des méthodes de clonages.
 *
 * @author breytond
 */
public class Dto3 extends Dto2 implements Serializable {
    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private List<Map<String, Dto2>> listeMapStringTestDto2 =
        new ArrayList<Map<String, Dto2>>();

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Date date;

    /**
     * Accesseur de date.
     *
     * @return date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Mutateur date.
     *
     * @param date le date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Accesseur de listeMapStringTestDto2.
     *
     * @return listeMapStringTestDto2
     */
    public List<Map<String, Dto2>> getListeMapStringTestDto2() {
        return listeMapStringTestDto2;
    }

    /**
     * Mutateur listeMapStringTestDto2.
     *
     * @param listeMapStringTestDto2 le listeMapStringTestDto2
     */
    public void setListeMapStringTestDto2(List<Map<String, Dto2>> listeMapStringTestDto2) {
        this.listeMapStringTestDto2 = listeMapStringTestDto2;
    }
}
