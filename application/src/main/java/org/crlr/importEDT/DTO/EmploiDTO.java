/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.importEDT.DTO;

import java.io.Serializable;

import org.crlr.web.dto.TypeSemaine;

/**
 * Classe qui permet de traiter les informations de l'emploi du temps.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public class EmploiDTO implements Serializable{
    /**
     * Le serialVersionUid.
     */
    private static final long serialVersionUID  = 1L;
    
    /**
     * Le jour.
     */
    private String jour;

    /**
     * Le type de semaine.!
     */
    private TypeSemaine typeSemaine;

    /**
     * L'heure de début.
     */
    private String heureDebut;

    /**
     * L'heure de fin.
     */
    private String heureFin;

    /**
     * La minute de début.
     */
    private String minuteDebut;

    /**
     * La minute de fin.!
     */
    private String minuteFin;

    /**
     * L'id de la séquence.
     */
    private Integer idSequence;

    /**
     * L'id de l'enseignant.
     */
    private int idEnseignant;

    /**
     * L'id de l'enseignement.
     */
    private int idEnseignement;

    /**
     * L'id de la classe.
     */
    private Integer idClasse;

    /**
     * L'id du groupe.
     */
    private Integer idGroupe;

    /**
     * L'id de l'établissement.
     */
    private int idEtablissement;

    /** Le code de la salle où a lieu le cours. */
    private String codeSalle;
    

    /**
     * Accesseur de jour.
     *
     * @return le jour
     */
    public String getJour() {
        return jour;
    }

    /**
     * Modificateur de jour.
     *
     * @param jour le jour à modifier
     */
    public void setJour(String jour) {
        this.jour = jour;
    }

    /**
     * Accesseur de typeSemaine.
     *
     * @return le typeSemaine
     */
    public TypeSemaine getTypeSemaine() {
        return typeSemaine;
    }

    /**
     * Modificateur de typeSemaine.
     *
     * @param typeSemaine le typeSemaine à modifier
     */
    public void setTypeSemaine(TypeSemaine typeSemaine) {
        this.typeSemaine = typeSemaine;
    }

    /**
     * Accesseur de heureDebut.
     *
     * @return le heureDebut
     */
    public String getHeureDebut() {
        return heureDebut;
    }

    /**
     * Modificateur de heureDebut.
     *
     * @param heureDebut le heureDebut à modifier
     */
    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    /**
     * Accesseur de heureFin.
     *
     * @return le heureFin
     */
    public String getHeureFin() {
        return heureFin;
    }

    /**
     * Modificateur de heureFin.
     *
     * @param heureFin le heureFin à modifier
     */
    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    /**
     * Accesseur de minuteDebut.
     *
     * @return le minuteDebut
     */
    public String getMinuteDebut() {
        return minuteDebut;
    }

    /**
     * Modificateur de minuteDebut.
     *
     * @param minuteDebut le minuteDebut à modifier
     */
    public void setMinuteDebut(String minuteDebut) {
        this.minuteDebut = minuteDebut;
    }

    /**
     * Accesseur de minuteFin.
     *
     * @return le minuteFin
     */
    public String getMinuteFin() {
        return minuteFin;
    }

    /**
     * Modificateur de minuteFin.
     *
     * @param minuteFin le minuteFin à modifier
     */
    public void setMinuteFin(String minuteFin) {
        this.minuteFin = minuteFin;
    }

    /**
     * Accesseur de idSequence.
     *
     * @return le idSequence
     */
    public Integer getIdSequence() {
        return idSequence;
    }

    /**
     * Modificateur de idSequence.
     *
     * @param idSequence le idSequence à modifier
     */
    public void setIdSequence(Integer idSequence) {
        this.idSequence = idSequence;
    }

    /**
     * Accesseur de idEnseignant.
     *
     * @return le idEnseignant
     */
    public int getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Modificateur de idEnseignant.
     *
     * @param idEnseignant le idEnseignant à modifier
     */
    public void setIdEnseignant(int idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur de idEnseignement.
     *
     * @return le idEnseignement
     */
    public int getIdEnseignement() {
        return idEnseignement;
    }

    /**
     * Modificateur de idEnseignement.
     *
     * @param idEnseignement le idEnseignement à modifier
     */
    public void setIdEnseignement(int idEnseignement) {
        this.idEnseignement = idEnseignement;
    }

    /**
     * Accesseur de idClasse.
     *
     * @return le idClasse
     */
    public Integer getIdClasse() {
        return idClasse;
    }

    /**
     * Modificateur de idClasse.
     *
     * @param idClasse le idClasse à modifier
     */
    public void setIdClasse(Integer idClasse) {
        this.idClasse = idClasse;
    }

    /**
     * Accesseur de idGroupe.
     *
     * @return le idGroupe
     */
    public Integer getIdGroupe() {
        return idGroupe;
    }

    /**
     * Modificateur de idGroupe.
     *
     * @param idGroupe le idGroupe à modifier
     */
    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    /**
     * Accesseur de idEtablissement.
     *
     * @return le idEtablissement
     */
    public int getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Modificateur de idEtablissement.
     *
     * @param idEtablissement le idEtablissement à modifier
     */
    public void setIdEtablissement(int idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur de codeSalle.
     * @return le codeSalle
     */
    public String getCodeSalle() {
        return codeSalle;
    }

    /**
     * Modificateur de codeSalle.
     * @param codeSalle le codeSalle à modifier
     */
    public void setCodeSalle(String codeSalle) {
        this.codeSalle = codeSalle;
    }
}
