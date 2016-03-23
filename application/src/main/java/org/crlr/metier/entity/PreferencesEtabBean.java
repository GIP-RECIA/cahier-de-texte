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
 * @author legay
 * @version $Revision: 1.1 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_preferences_etab", schema="cahier_courant")

public class PreferencesEtabBean {

    @Id
    @Column(name = "id_etablissement")
    private Integer idEtab;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "preferences", nullable = false)
    private String preferences;

     /**
     * Constructeur.
     */
    public PreferencesEtabBean() {
        
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

	public Integer getIdEtab() {
		return idEtab;
	}

	public void setIdEtab(Integer idEtab) {
		this.idEtab = idEtab;
	}
}