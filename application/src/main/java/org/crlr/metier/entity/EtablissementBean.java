/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EtablissementBean.java,v 1.9 2010/03/31 15:05:11 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author breytond
 * @version $Revision: 1.9 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_etablissement", schema="cahier_courant")
public class EtablissementBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private Integer id;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "code", nullable = false)
    private String code;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "designation", nullable = false)
    private String designation;
    
    @Column(name = "horaire_cours", nullable = true)
    private String horaireCours;
    
    @Column(name = "alternance_semaine", nullable = true)
    private String alternanceSemaine;
    
    @Column(name = "jours_ouvres", nullable = true)
    private String joursOuvres;
    
    @Column(name = "duree_cours", nullable = true)
    private Integer dureeCours;
    
    @Column(name = "heure_debut", nullable = true)
    private Integer heureDebut;
    
    @Column(name = "heure_fin", nullable = true)
    private Integer heureFin;
    
    @Column(name = "minute_debut", nullable = true)
    private Integer minuteDebut;
    
    @Column(name = "ouverture", nullable = true)
    private Boolean vraiOuFauxCahierOuvert;   
    
    @Column(name = "import", nullable = false)
    private Boolean vraiOuFauxImportEnCours = false; 
    
    @Column(name = "date_import", nullable = true)
    private String dateImport; 
    
    @Column(name = "fractionnement", nullable = true)
    private Integer fractionnement; 

    /**
     * Constructeur. 
     */
    public EtablissementBean() {
    }

    /**
     * Accesseur id.
     * @return le id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur id.
     * @param id le id à modifier.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur code.
     * @return le code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Mutateur code.
     * @param code le code à modifier.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Accesseur designation.
     * @return le designation.
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Mutateur designation.
     * @param designation le designation à modifier.
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * Accesseur horaireCours.
     * @return le horaireCours
     */
    public String getHoraireCours() {
        return horaireCours;
    }

    /**
     * Mutateur de horaireCours.
     * @param horaireCours le horaireCours à modifier.
     */
    public void setHoraireCours(String horaireCours) {
        this.horaireCours = horaireCours;
    }

    /**
     * Accesseur alternanceSemaine.
     * @return le alternanceSemaine
     */
    public String getAlternanceSemaine() {
        return alternanceSemaine;
    }

    /**
     * Mutateur de alternanceSemaine.
     * @param alternanceSemaine le alternanceSemaine à modifier.
     */
    public void setAlternanceSemaine(String alternanceSemaine) {
        this.alternanceSemaine = alternanceSemaine;
    }

    /**
     * Accesseur joursOuvres.
     * @return le joursOuvres
     */
    public String getJoursOuvres() {
        return joursOuvres;
    }

    /**
     * Mutateur de joursOuvres.
     * @param joursOuvres le joursOuvres à modifier.
     */
    public void setJoursOuvres(String joursOuvres) {
        this.joursOuvres = joursOuvres;
    }

    /**
     * Accesseur dureeCours.
     * @return le dureeCours
     */
    public Integer getDureeCours() {
        return dureeCours;
    }

    /**
     * Mutateur de dureeCours.
     * @param dureeCours le dureeCours à modifier.
     */
    public void setDureeCours(Integer dureeCours) {
        this.dureeCours = dureeCours;
    }

    /**
     * Accesseur heureDebut.
     * @return le heureDebut
     */
    public Integer getHeureDebut() {
        return heureDebut;
    }

    /**
     * Mutateur de heureDebut.
     * @param heureDebut le heureDebut à modifier.
     */
    public void setHeureDebut(Integer heureDebut) {
        this.heureDebut = heureDebut;
    }

    /**
     * Accesseur heureFin.
     * @return le heureFin
     */
    public Integer getHeureFin() {
        return heureFin;
    }

    /**
     * Mutateur de heureFin.
     * @param heureFin le heureFin à modifier.
     */
    public void setHeureFin(Integer heureFin) {
        this.heureFin = heureFin;
    }

    /**
     * Accesseur minuteDebut.
     * @return le minuteDebut
     */
    public Integer getMinuteDebut() {
        return minuteDebut;
    }

    /**
     * Mutateur de minuteDebut.
     * @param minuteDebut le minuteDebut à modifier.
     */
    public void setMinuteDebut(Integer minuteDebut) {
        this.minuteDebut = minuteDebut;
    }

    /**
     * Accesseur vraiOuFauxCahierOuvert.
     * @return le vraiOuFauxCahierOuvert
     */
    public Boolean getVraiOuFauxCahierOuvert() {
        return vraiOuFauxCahierOuvert;
    }

    /**
     * Mutateur de vraiOuFauxCahierOuvert.
     * @param vraiOuFauxCahierOuvert le vraiOuFauxCahierOuvert à modifier.
     */
    public void setVraiOuFauxCahierOuvert(Boolean vraiOuFauxCahierOuvert) {
        this.vraiOuFauxCahierOuvert = vraiOuFauxCahierOuvert;
    }

    /**
     * Accesseur de vraiOuFauxImportEnCours.
     * @return le vraiOuFauxImportEnCours
     */
    public Boolean getVraiOuFauxImportEnCours() {
        return vraiOuFauxImportEnCours;
    }

    /**
     * Modificateur de vraiOuFauxImportEnCours.
     * @param vraiOuFauxImportEnCours le vraiOuFauxImportEnCours à modifier
     */
    public void setVraiOuFauxImportEnCours(Boolean vraiOuFauxImportEnCours) {
        this.vraiOuFauxImportEnCours = vraiOuFauxImportEnCours;
    }

    /**
     * Accesseur de dateImport.
     * @return le dateImport
     */
    public String getDateImport() {
        return dateImport;
    }

    /**
     * Modificateur de dateImport.
     * @param dateImport le dateImport à modifier
     */
    public void setDateImport(String dateImport) {
        this.dateImport = dateImport;
    }

    /**
     * Accesseur de fractionnement {@link #fractionnement}.
     * @return retourne fractionnement
     */
    public Integer getFractionnement() {
        return fractionnement;
    }

    /**
     * Mutateur de fractionnement {@link #fractionnement}.
     * @param fractionnement le fractionnement to set
     */
    public void setFractionnement(Integer fractionnement) {
        this.fractionnement = fractionnement;
    }
    
    
}
