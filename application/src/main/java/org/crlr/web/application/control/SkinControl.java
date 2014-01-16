/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SkinControl.java,v 1.1 2009/06/19 06:49:27 ent_breyton Exp $
 */

package org.crlr.web.application.control;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.crlr.dto.application.base.TypeSkin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mutateur de skin au runtime.
 *
 * @author breytond.
 */
@ManagedBean(name = "skin")
@SessionScoped
public class SkinControl {
    
    private static Logger log = LoggerFactory.getLogger(SkinControl.class);
    
    /** La skin courante. */
    @ManagedProperty(value = "ruby")
    private String skin;

    /**
     * Retourne la skin sélectionné.
     *
     * @return la skin.
     */
    public String getSkin() {
        log.debug("Retourne skin {}", skin);
        
        return skin;
    }

    /**
     * Mutateur la skin sélectionné.
     *
     * @param skin la skin.
     */
    public void setSkin(String skin) {
        log.debug("setSkin {}", skin);
        this.skin = skin;
    }
    
    /**
     * Initialisation runtime du skin.
     * @param skin le type de skin.
     */
    public void init(final TypeSkin skin) {
        log.debug("init {}", skin);
        this.skin = skin.getValeur();
    }
}
