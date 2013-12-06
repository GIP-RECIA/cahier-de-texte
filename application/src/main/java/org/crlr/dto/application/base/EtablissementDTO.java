/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EtablissementDTO.java,v 1.3 2010/04/12 01:34:33 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * EtablissementDTO.
 *
 * @author breytond.
 * @version $Revision: 1.3 $
  */
public class EtablissementDTO implements Serializable {
    /** Seiral UID. */
    private static final long serialVersionUID = 1348049082089927540L;

    /** Identifiant de l'établissement. */
    private Integer id;

    /** code de l'établissement. */
    private String code;

    /** Désignation de l'établissement. */
    private String designation;
    
    /** jours ouvrés de l'établissement. */
    private String joursOuvres;
    
    private String alternanceSemaine;
    
    /** ouvert. */
    private Boolean vraiOuFauxOuvert; 
    
    /** Activation de la saisie simplifiee. */
    private Boolean vraiOuFauxSaisieSimplifiee;
    
    private Integer fractionnement;
    
    private Integer dureeCours;
    
    private Integer heureDebut;
    
    private Integer heureFin;
    
    private Integer minuteDebut;
    
    private String horaireCours;

    /**
     * Accesseur id.
     *
     * @return le id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur id.
     *
     * @param id le id à modifier.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur code.
     *
     * @return le code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Mutateur code.
     *
     * @param code le code à modifier.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Accesseur designation.
     *
     * @return le designation.
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Mutateur designation.
     *
     * @param designation le designation à modifier.
     */
    public void setDesignation(String designation) {
        this.designation = designation;
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
     * Accesseur vraiOuFauxOuvert.
     * @return le vraiOuFauxOuvert
     */
    public Boolean getVraiOuFauxOuvert() {
        return vraiOuFauxOuvert;
    }

    /**
     * Mutateur de vraiOuFauxOuvert.
     * @param vraiOuFauxOuvert le vraiOuFauxOuvert à modifier.
     */
    public void setVraiOuFauxOuvert(Boolean vraiOuFauxOuvert) {
        this.vraiOuFauxOuvert = vraiOuFauxOuvert;
    }

    /**
     * Accesseur vraiOuFauxSaisieSimplifiee.
     * @return le vraiOuFauxSaisieSimplifiee
     */
    public Boolean getVraiOuFauxSaisieSimplifiee() {
        return vraiOuFauxSaisieSimplifiee;
    }

    /**
     * Mutateur de vraiOuFauxSaisieSimplifiee.
     * @param vraiOuFauxSaisieSimplifiee le vraiOuFauxSaisieSimplifiee à modifier.
     */
    public void setVraiOuFauxSaisieSimplifiee(Boolean vraiOuFauxSaisieSimplifiee) {
        this.vraiOuFauxSaisieSimplifiee = vraiOuFauxSaisieSimplifiee;
    }

    /**
     * @return the fractionnement
     */
    public Integer getFractionnement() {
        return fractionnement;
    }

    /**
     * @param fractionnement the fractionnement to set
     */
    public void setFractionnement(Integer fractionnement) {
        this.fractionnement = fractionnement;
    }

    /**
     * @return the dureeCours
     */
    public Integer getDureeCours() {
        return dureeCours;
    }

    /**
     * @param dureeCours the dureeCours to set
     */
    public void setDureeCours(Integer dureeCours) {
        this.dureeCours = dureeCours;
    }

    /**
     * @return the heureDebut
     */
    public Integer getHeureDebut() {
        return heureDebut;
    }

    /**
     * @param heureDebut the heureDebut to set
     */
    public void setHeureDebut(Integer heureDebut) {
        this.heureDebut = heureDebut;
    }

    /**
     * @return the heureFin
     */
    public Integer getHeureFin() {
        return heureFin;
    }

    /**
     * @param heureFin the heureFin to set
     */
    public void setHeureFin(Integer heureFin) {
        this.heureFin = heureFin;
    }

    /**
     * @return the minuteDebut
     */
    public Integer getMinuteDebut() {
        return minuteDebut;
    }

    /**
     * @param minuteDebut the minuteDebut to set
     */
    public void setMinuteDebut(Integer minuteDebut) {
        this.minuteDebut = minuteDebut;
    }

    /**
     * @return the horaireCours
     */
    public String getHoraireCours() {
        return horaireCours;
    }

    /**
     * @param horaireCours the horaireCours to set
     */
    public void setHoraireCours(String horaireCours) {
        this.horaireCours = horaireCours;
    }

    /**
     * @return the alternanceSemaine
     */
    public String getAlternanceSemaine() {
        return alternanceSemaine;
    }

    /**
     * @param alternanceSemaine the alternanceSemaine to set
     */
    public void setAlternanceSemaine(String alternanceSemaine) {
        this.alternanceSemaine = alternanceSemaine;
    }
}
