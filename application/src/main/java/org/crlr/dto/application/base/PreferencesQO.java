/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PreferencesQO.java
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * PreferencesQO.
 *
 * @author carrierej
 * @version $Revision: 1.1 $
  */
public class PreferencesQO implements Serializable {
    
    /** Serial UID. */
    private static final long serialVersionUID = 2541264937130820580L;

    /** uid. */
    private String uid;
    
    /** la préférence. */
    private String preferences;

    /**
     * Accesseur uid.
     * @return le uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Mutateur de uid.
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
