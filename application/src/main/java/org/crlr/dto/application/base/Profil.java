/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Profil.java,v 1.2 2010/04/19 08:59:44 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

/**
 * Profil possible pour une entité.
 * @author breytond
 *
 */
public enum Profil {PARENT, ELEVE, ENSEIGNANT, INSPECTION_ACADEMIQUE, 
    DIRECTION_ETABLISSEMENT, AUTRE, DOCUMENTALISTE;
//Le profil Autre correspond uniquement aux administrateurs
//Le profil Documentaliste ne se restreint pas aux utilisateurs documentalistes (CPE, conseiller orientation, ...).
}
