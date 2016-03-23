/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DetailJourDTO.java,v 1.11 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.dto.application.devoir;

import java.io.Serializable;

import java.util.Date;

import org.crlr.dto.application.base.GroupesClassesDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.11 $
 */
public class DetailJourDTO extends DevoirDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = 357002530568002370L;

   

    /** Désignation de l'enseignement du jour. */
    private String matiere;

   
    
    /** Heure de début de la séance (concaténation de heureDebutSeance+"h"+minuteDebutSeance). */
    private String heureSeance;
    
       
       
    /** Le nom de l'enseignant. */
    private String nom;

    /** La civilité de l'enseignant. */
    private String denomination;
    
    /** DOCUMENTATION INCOMPLETE! */
    private Boolean actif;
    
       
    /** Indique si le jour concerne une archive ou non.*/
    private Boolean archive;
    
    /** Specifie l'exercice du jour. renseigne uniquement dans le cadre d'une archive. */
    private String exercice;
    
    
    
    /**
     * 
     * Constructeur.
     */
    public DetailJourDTO() {
        actif = true;
        
    }
    
    /**
     * 
     * Constructeur.
     * @param actif actif.
     */
    public DetailJourDTO(final Boolean actif) {
        this.actif = actif;
    }

    public GroupesClassesDTO getGroupesClassesDTO() {
        return getSeance().getSequence().getGroupesClassesDTO();
    }
    
   
   

    /**
     * 
    DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String getMatiere() {
        return matiere;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param matiere DOCUMENTATION INCOMPLETE!
     */
    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

   
    /**
     * 
    DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String getNom() {
        return nom;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param nom DOCUMENTATION INCOMPLETE!
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String getDenomination() {
        return denomination;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param denomination DOCUMENTATION INCOMPLETE!
     */
    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

  

    /**
     * Accesseur actif.
     * @return le actif.
     */
    public Boolean getActif() {
        return actif;
    }

    /**
     * Mutateur actif.
     * @param actif le actif à modifier.
     */
    public void setActif(Boolean actif) {
        this.actif = actif;
    }

  

    /**
     * Accesseur heureSeance.
     * @return heureSeance
     */
    public String getHeureSeance() {
        return heureSeance;
    }

    /**
     * Mutateur heureSeance.
     * @param heureSeance Le heureSeance à modifier
     */
    public void setHeureSeance(String heureSeance) {
        this.heureSeance = heureSeance;
    }



    /**
     * Accesseur de archive {@link #archive}.
     * @return retourne archive 
     */
    public Boolean getArchive() {
        return archive;
    }

    /**
     * Mutateur de archive {@link #archive}.
     * @param archive the archive to set
     */
    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    /**
     * Accesseur de exercice {@link #exercice}.
     * @return retourne exercice 
     */
    public String getExercice() {
        return exercice;
    }

    /**
     * Mutateur de exercice {@link #exercice}.
     * @param exercice the exercice to set
     */
    public void setExercice(String exercice) {
        this.exercice = exercice;
    }

    
}
