/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Outil.java,v 1.16 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.dto;

/**
 * Outils disponible.
 * @author breytond
 *
 */
//Autre
public enum Outil {
    /** main. */
    CONNEXION, ACCUEIL, PREFERENCE, PREFERENCE_CAHIER,
    /** Emploi du temps. */
    CONSTITUER_EMP, SAISIR_EMP, CONSOLIDER_EMP, IMPORTEDT,
    /** cahier de texte. */
    CAHIER_ARCHIVE, DEPOT, CAHIER_MENSUEL,  
    /** Devoir. */
    DEVOIRS, AJOUT_DEVOIR, EDIT_DEVOIR, CONSULTER_DEVOIR, MODIFIER_DEVOIR, PROCHAIN_DEVOIR, 
    /** Séquences. */
    AJOUT_SEQUENCE, RECH_SEQUENCE, EDIT_SEQUENCE, DELETE_SEQUENCE, DUPLIQUER_SEQUENCE, PRINT_SEQUENCE, ARCHIVE_SEQUENCE,
    /** Séances. */
    AJOUT_SEANCE, RECH_SEANCE, DELETE_SEANCE, CONSULTER_SEANCE, PRINT_SEANCE, SEANCE_SEMAINE, ARCHIVE_SEANCE,
    /** Carnet de bord (cycle). */
    AJOUT_CYCLE, RECH_CYCLE, APPLY_CYCLE,
    /** Admin. */
    ADMIN,
    /** Inspection. */
    INSPECTION, 
    /** Visa. */
    VISA_LISTE, VISA_SEANCE,
    /** Simulation eleve. */
    SIMULATION_ELEVE,
    /** Changement de profil en cas de multi-profils */
    CHANGE_PROFIL,
    /** Gestion des remplacements enseignants. */
    REMPLACEMENT_ENSEIGNANT,

    /** Notes et absence. */
    AJOUTER_NOTE, AJOUTER_ABSENCE, CONSULTER_NOTE, CONSULTER_ABSENCE
    ;
    
    public boolean isArchive(){
    	switch (this) {
		case ARCHIVE_SEANCE:
		case CAHIER_ARCHIVE:
		case ARCHIVE_SEQUENCE:
			return true;
		default:
			return false;
		}
  
    }
}
