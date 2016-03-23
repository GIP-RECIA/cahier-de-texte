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
 * ajout des objectclass correspondant au profils pour xiti
 * 
 */
public enum Profil {
		ENSEIGNANT("Enseignant","ENTAuxEnseignant"), 			
		DIRECTION_ETABLISSEMENT("Direction établissement","ENTAuxNonEnsEtab"), 
		DOCUMENTALISTE("Vie scolaire","ENTAuxNonEnsEtab"),
		INSPECTION_ACADEMIQUE("Inspecteur académique",""), 
		ELEVE("Eleve","ENTEleve"), 
		PARENT("Parent","ENTAuxPersRelEleve"),
		AUTRE("Autre",""), 
		;
		
// l'ordre des profils est important si une personne a 2 profils on prendra par défaut le premier.
//Le profil Autre correspond uniquement aux administrateurs
//Le profil Documentaliste ne se restreint pas aux utilisateurs documentalistes (CPE, conseiller orientation, ...).
	
	private String role;
	private String objectClassDeterminant;
	
	private Profil(String role, String ojectClass) {
		setRole(role);
		setObjectClassDeterminant(ojectClass);
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getObjectClassDeterminant() {
		return objectClassDeterminant;
	}

	public void setObjectClassDeterminant(String objectClassDeterminant) {
		this.objectClassDeterminant = objectClassDeterminant;
	}

}
