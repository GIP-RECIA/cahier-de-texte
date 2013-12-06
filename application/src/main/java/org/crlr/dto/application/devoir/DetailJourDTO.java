/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DetailJourDTO.java,v 1.11 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.dto.application.devoir;

import java.io.Serializable;

import java.util.Date;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.11 $
 */
public class DetailJourDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = 357002530568002370L;

    /** Id du devoir du jour. */
    private Integer idDevoir;
    
    /** Date du jour. */
    private Date date;

    /** Code de la classe. */
    private String classe;
    
    /** Code du groupe. */
    private String groupe;
    
    /** Id de la classe. */
    private Integer idClasse;
    
    /** Id du groupe . */
    private Integer idGroupe; 

    /** Désignation de l'enseignement du jour. */
    private String matiere;

    /** Id de la séquence auquelle est rattachée la séance ou le devoir. */
    private Integer idSequence;
    
    /** Minute de début de la séance. */
    private Integer minuteDebutSeance;
    
    /** Heure de début de la séance. */
    private Integer heureDebutSeance;
    
    /** Heure de début de la séance (concaténation de heureDebutSeance+"h"+minuteDebutSeance). */
    private String heureSeance;
    
    /** Intitulé de la séquence. */
    private String intituleSequence;

    /** Id de la séance. */
    private Integer idSeance;
    
    /** Code de la séance. */
    private String codeSeance;

    /** Type de devoir (libellé). */
    private String typeDevoir;

    /** Intitulé du devoir. */
    private String intituleDevoir;
    
    /** L'intitulé de la séance. */
    private String intituleSeance;
    
    /** La description du devoir. */
    private String descriptionDevoir;

    /** La description abregée sans balise pour un affichage en colonne (tableau). */
    private String descriptionSansBaliseAbrege;

    /** La description sans balise pour un affichage info bulle. */
    private String descriptionSansBalise;
    
    /** Le nom de l'enseignant. */
    private String nom;

    /** La civilité de l'enseignant. */
    private String denomination;
    
    /** DOCUMENTATION INCOMPLETE! */
    private Boolean actif;
    
    /** permet dns le calendrier des devoirs de savoir si l'enseignant peut modifier ou uniquement consulter. */
    private Boolean vraiOuFauxModifiable;
    
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
        this.vraiOuFauxModifiable = false;
    }
    
    /**
     * 
     * Constructeur.
     * @param actif actif.
     */
    public DetailJourDTO(final Boolean actif) {
        this.actif = actif;
        this.vraiOuFauxModifiable = false;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public Date getDate() {
        return date;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param date DOCUMENTATION INCOMPLETE!
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String getClasse() {
        return classe;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param classe DOCUMENTATION INCOMPLETE!
     */
    public void setClasse(String classe) {
        this.classe = classe;
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
    public Integer getIdSequence() {
        return idSequence;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param idSequence DOCUMENTATION INCOMPLETE!
     */
    public void setIdSequence(Integer idSequence) {
        this.idSequence = idSequence;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public Integer getIdSeance() {
        return idSeance;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param idSeance DOCUMENTATION INCOMPLETE!
     */
    public void setIdSeance(Integer idSeance) {
        this.idSeance = idSeance;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String getTypeDevoir() {
        return typeDevoir;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param typeDevoir DOCUMENTATION INCOMPLETE!
     */
    public void setTypeDevoir(String typeDevoir) {
        this.typeDevoir = typeDevoir;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String getIntituleDevoir() {
        return intituleDevoir;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param intituleDevoir DOCUMENTATION INCOMPLETE!
     */
    public void setIntituleDevoir(String intituleDevoir) {
        this.intituleDevoir = intituleDevoir;
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
     * Accesseur idDevoir.
     * @return idDevoir
     */
    public Integer getIdDevoir() {
        return idDevoir;
    }

    /**
     * Mutateur idDevoir.
     * @param idDevoir Le idDevoir à modifier
     */
    public void setIdDevoir(Integer idDevoir) {
        this.idDevoir = idDevoir;
    }

    /**
     * Accesseur descriptionDevoir.
     * @return descriptionDevoir
     */
    public String getDescriptionDevoir() {
        return descriptionDevoir;
    }

    /**
     * Mutateur descriptionDevoir.
     * @param descriptionDevoir Le descriptionDevoir à modifier
     */
    public void setDescriptionDevoir(String descriptionDevoir) {
        this.descriptionDevoir = descriptionDevoir;
    }

    /**
     * Accesseur intituleSeance.
     * @return intituleSeance
     */
    public String getIntituleSeance() {
        return intituleSeance;
    }

    /**
     * Mutateur intituleSeance.
     * @param intituleSeance Le intituleSeance à modifier
     */
    public void setIntituleSeance(String intituleSeance) {
        this.intituleSeance = intituleSeance;
    }

    /**
     * Accesseur codeSeance.
     * @return codeSeance
     */
    public String getCodeSeance() {
        return codeSeance;
    }

    /**
     * Mutateur codeSeance.
     * @param codeSeance Le codeSeance à modifier
     */
    public void setCodeSeance(String codeSeance) {
        this.codeSeance = codeSeance;
    }

    /**
     * Accesseur heureDebutSeance.
     * @return heureDebutSeance
     */
    public Integer getHeureDebutSeance() {
        return heureDebutSeance;
    }

    /**
     * Mutateur heureDebutSeance.
     * @param heureDebutSeance Le heureDebutSeance à modifier
     */
    public void setHeureDebutSeance(Integer heureDebutSeance) {
        this.heureDebutSeance = heureDebutSeance;
    }

    /**
     * Accesseur intituleSequence.
     * @return intituleSequence
     */
    public String getIntituleSequence() {
        return intituleSequence;
    }

    /**
     * Mutateur intituleSequence.
     * @param intituleSequence Le intituleSequence à modifier
     */
    public void setIntituleSequence(String intituleSequence) {
        this.intituleSequence = intituleSequence;
    }

    /**
     * Accesseur groupe.
     * @return groupe
     */
    public String getGroupe() {
        return groupe;
    }

    /**
     * Mutateur groupe.
     * @param groupe Le groupe à modifier
     */
    public void setGroupe(String groupe) {
        this.groupe = groupe;
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
     * Accesseur minuteDebutSeance.
     * @return minuteDebutSeance
     */
    public Integer getMinuteDebutSeance() {
        return minuteDebutSeance;
    }

    /**
     * Mutateur minuteDebutSeance.
     * @param minuteDebutSeance Le minuteDebutSeance à modifier
     */
    public void setMinuteDebutSeance(Integer minuteDebutSeance) {
        this.minuteDebutSeance = minuteDebutSeance;
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
     * Accesseur de vraiOuFauxModifiable {@link #vraiOuFauxModifiable}.
     * @return retourne vraiOuFauxModifiable
     */
    public Boolean getVraiOuFauxModifiable() {
        return vraiOuFauxModifiable;
    }

    /**
     * Mutateur de vraiOuFauxModifiable {@link #vraiOuFauxModifiable}.
     * @param vraiOuFauxModifiable le vraiOuFauxModifiable to set
     */
    public void setVraiOuFauxModifiable(Boolean vraiOuFauxModifiable) {
        this.vraiOuFauxModifiable = vraiOuFauxModifiable;
    }

    /** 
     * Accesseur de idClasse.
     * @return the idClasse
     */
    public Integer getIdClasse() {
        return idClasse;
    }

    /**
     * Mutateur de idClasse.
     * @param idClasse the idClasse to set
     */
    public void setIdClasse(Integer idClasse) {
        this.idClasse = idClasse;
    }

    /**
     * Accesseur de idGroupe.
     * @return the idGroupe
     */
    public Integer getIdGroupe() {
        return idGroupe;
    }

    /**
     * Mutateur de idGroupe.
     * @param idGroupe the idGroupe to set
     */
    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
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

    /**
     * Accesseur de descriptionSansBaliseAbrege {@link #descriptionSansBaliseAbrege}.
     * @return retourne descriptionSansBaliseAbrege
     */
    public String getDescriptionSansBaliseAbrege() {
        return descriptionSansBaliseAbrege;
    }

    /**
     * Mutateur de descriptionSansBaliseAbrege {@link #descriptionSansBaliseAbrege}.
     * @param descriptionSansBaliseAbrege le descriptionSansBaliseAbrege to set
     */
    public void setDescriptionSansBaliseAbrege(String descriptionSansBaliseAbrege) {
        this.descriptionSansBaliseAbrege = descriptionSansBaliseAbrege;
    }

    /**
     * Accesseur de descriptionSansBalise {@link #descriptionSansBalise}.
     * @return retourne descriptionSansBalise
     */
    public String getDescriptionSansBalise() {
        return descriptionSansBalise;
    }

    /**
     * Mutateur de descriptionSansBalise {@link #descriptionSansBalise}.
     * @param descriptionSansBalise le descriptionSansBalise to set
     */
    public void setDescriptionSansBalise(String descriptionSansBalise) {
        this.descriptionSansBalise = descriptionSansBalise;
    }
    
}
