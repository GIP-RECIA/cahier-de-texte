/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PreferencesBean.java.
 */

package org.crlr.metier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * Preferences Bean.
 *
 * @author carriere
 * @version $Revision: 1.1 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_preferences_utilisateur", schema="cahier_courant")

public class PreferencesBean {

    @Id
    private String uid;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "preferences", nullable = false)
    private String preferences;

     /**
     * Constructeur.
     */
    public PreferencesBean() {
        
    }
    /**
     * Accesseur uid.
     *
     * @return le uid.
     */
    public String getUid() {
        return uid;
    }

    /**
     * Mutateur uid.
     *
     * @param uid le uid à modifier.
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
    /**
     * Accesseur preferences.
     * @return le preferences
     */
    public String getPreferences() {
        return preferences;
    }
    /**
     * Mutateur de preferences.
     * @param preferences le preferences à modifier.
     */
    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}