/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.importEDT.DTO;

import java.io.Serializable;

/**
 * Classe qui permet de représenter un cours.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
 */
public class CoursDTO implements Serializable{
    
    /**
     * Le serialVersionUid.
     */
    private static final long serialVersionUID  = 1L;
    
    /** le code alternance du cours. */
    private String codeAlternance;

    /** Le jour où a lieu ce cours. */
    private String jour;

    /** L'heure à laquelle commence le cours. */
    private String heureDebut;

    /** La durée de ce cours. */
    private String duree;
    
    /** Le code de la salle. */
    private String codeSalle;
    
    /**
     * Constructeur vide de CoursDTO.
     */
    public CoursDTO(){}
    
    /**
     * Clone un CoursDTO.
     * Constructeur de @param unCours le CoursDTO à cloner.
     */
    public CoursDTO(CoursDTO unCours){
        this.codeAlternance = unCours.getCodeAlternance();
        this.duree = unCours.getDuree();
        this.heureDebut = unCours.getHeureDebut();
        this.jour = unCours.getJour();
        this.codeSalle = unCours.getCodeSalle();
    }
    
    
    
    /**
     * Accesseur de codeAlternance.
     *
     * @return le codeAlternance
     */
    public String getCodeAlternance() {
        return codeAlternance;
    }

    /**
     * Modificateur de codeAlternance.
     *
     * @param codeAlternance le codeAlternance à modifier
     */
    public void setCodeAlternance(String codeAlternance) {
        this.codeAlternance = codeAlternance;
    }

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
     * Accesseur de duree.
     *
     * @return le duree
     */
    public String getDuree() {
        return duree;
    }

    /**
     * Modificateur de duree.
     *
     * @param duree le duree à modifier
     */
    public void setDuree(String duree) {
        this.duree = duree;
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
