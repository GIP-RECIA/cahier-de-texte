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
 * @author p.legay 
 * 
 *  P.legay ajout des roles et fixe l'ordre
 *
 * 
 */
public enum Profil {
		ENSEIGNANT("Enseignant"), 			
		DIRECTION_ETABLISSEMENT("Direction établissement"), 
		DOCUMENTALISTE("Vie scolaire"),
		INSPECTION_ACADEMIQUE("Inspecteur académique"), 
		ELEVE("Eleve"), 
		PARENT("Parent"),
		AUTRE("Autre"), 
		;
		
// l'ordre des profils est important si une personne a 2 profils on prendra par défaut le premier.
//Le profil Autre correspond uniquement aux administrateurs
//Le profil Documentaliste ne se restreint pas aux utilisateurs documentalistes (CPE, conseiller orientation, ...).
	
	private String role;
	
	private Profil(String role) {
		setRole(role);
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
