/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TypeAuthentification.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.dto.securite;

/**
 * Type de l'authentification.
 * @author breytond.
 *
 */
public enum TypeAuthentification {
/**
     * Authentification sur CAS.
     */
    CAS, 
/**
     * Authentification sur Base de donnnées.
     */
    LDAP;   
}
