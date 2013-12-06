/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TypeSkin.java,v 1.3 2009/07/30 15:09:26 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

/**
 * TypeSkin.
 * @author breytond.
 *
 */
public enum TypeSkin {DEFAULT("ruby"), CG48("cg48"), CG30("cg30"), CG66("cg66"), CG34("cg34"), CG11("cg11"), CRC("crc"), AQUITAINE("aquitaine");
/** Valeur du mode. */
private final String valeur;

/**
 * Constructeur.
 * @param valeur la valeur.
 */
private TypeSkin(final String valeur) {
    this.valeur = valeur;
}

/**
 * Accesseur valeur.
 *
 * @return la valeur.
 */
public String getValeur() {
    return valeur;
}
}
