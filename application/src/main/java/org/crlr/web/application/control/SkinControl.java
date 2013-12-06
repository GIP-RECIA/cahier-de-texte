/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SkinControl.java,v 1.1 2009/06/19 06:49:27 ent_breyton Exp $
 */

package org.crlr.web.application.control;

import org.crlr.dto.application.base.TypeSkin;

/**
 * Mutateur de skin au runtime.
 *
 * @author breytond.
 */
public class SkinControl {
    /** La skin courante. */
    private String skin;

    /**
     * Retourne la skin sélectionné.
     *
     * @return la skin.
     */
    public String getSkin() {
        return skin;
    }

    /**
     * Mutateur la skin sélectionné.
     *
     * @param skin la skin.
     */
    public void setSkin(String skin) {
        this.skin = skin;
    }
    
    /**
     * Initialisation runtime du skin.
     * @param skin le type de skin.
     */
    public void init(final TypeSkin skin) {
        this.skin = skin.getValeur();
    }
}
