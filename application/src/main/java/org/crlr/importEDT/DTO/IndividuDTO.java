/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.importEDT.DTO;

import java.io.Serializable;
import java.util.List;

/**
 * Classe qui permet de représenter un individu décrit par le fichier XML.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public class IndividuDTO implements Serializable{
    /**
     * Le serialVersionUid.
     */
    private static final long serialVersionUID  = 1L;
    
    /**
     * id de l'individu.
     */
    private String id;

    /**
     * type de l'individu.
     */
    private String type;

    /**
     * civilité de l'individu.
     */
    private String civilite;

    /**
     * nom de l'individu.
     */
    private String nom;

    /**
     * prénom de l'individu.
     */
    private String prenom;

    /**
     * fonction de l'individu.
     */
    private String fonction;

    /**
     * les disciplines exercées par l'individu.
     */
    private List<DisciplineDTO> disciplines;

    /**
     * la liste des cours de l'individu.
     */
    private List<CoursDTO> cours;

    /**
     * Accesseur de id.
     *
     * @return le id
     */
    public String getId() {
        return id;
    }

    /**
     * Modificateur de id.
     *
     * @param id le id à modifier
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Accesseur de type.
     *
     * @return le type
     */
    public String getType() {
        return type;
    }

    /**
     * Modificateur de type.
     *
     * @param type le type à modifier
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Accesseur de civilite.
     *
     * @return le civilite
     */
    public String getCivilite() {
        return civilite;
    }

    /**
     * Modificateur de civilite.
     *
     * @param civilite le civilite à modifier
     */
    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    /**
     * Accesseur de nom.
     *
     * @return le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modificateur de nom.
     *
     * @param nom le nom à modifier
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Accesseur de prenom.
     *
     * @return le prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Modificateur de prenom.
     *
     * @param prenom le prenom à modifier
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Accesseur de fonction.
     *
     * @return le fonction
     */
    public String getFonction() {
        return fonction;
    }

    /**
     * Modificateur de fonction.
     *
     * @param fonction le fonction à modifier
     */
    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    /**
     * Accesseur de disciplines.
     *
     * @return le disciplines
     */
    public List<DisciplineDTO> getDisciplines() {
        return disciplines;
    }

    /**
     * Modificateur de disciplines.
     *
     * @param disciplines le disciplines à modifier
     */
    public void setDisciplines(List<DisciplineDTO> disciplines) {
        this.disciplines = disciplines;
    }

    /**
     * Accesseur de cours.
     *
     * @return le cours
     */
    public List<CoursDTO> getCours() {
        return cours;
    }

    /**
     * Modificateur de cours.
     *
     * @param cours le cours à modifier
     */
    public void setCours(List<CoursDTO> cours) {
        this.cours = cours;
    }
}
