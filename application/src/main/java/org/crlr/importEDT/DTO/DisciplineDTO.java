/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.importEDT.DTO;

import java.io.Serializable;

/**
 * Classe qui permet de représenter une discipline.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public class DisciplineDTO implements Serializable{
    /**
     * Le serialVersionUid.
     */
    private static final long serialVersionUID  = 1L;
    
    /**
     * id de la displine.
     */
    private int id;

    /**
     * code de la discipline.
     */
    private String code;

    /**
     * libellé de la displine.
     */
    private String libelle;

    /**
     * nombre d'heures que la discipline est exercée.
     */
    private String nbHeures;

    /**
     * Accesseur de id.
     *
     * @return le id
     */
    public int getId() {
        return id;
    }

    /**
     * Modificateur de id.
     *
     * @param id le id à modifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Accesseur de code.
     *
     * @return le code
     */
    public String getCode() {
        return code;
    }

    /**
     * Modificateur de code.
     *
     * @param code le code à modifier
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Accesseur de libelle.
     *
     * @return le libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Modificateur de libelle.
     *
     * @param libelle le libelle à modifier
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Accesseur de nbHeures.
     *
     * @return le nbHeures
     */
    public String getNbHeures() {
        return nbHeures;
    }

    /**
     * Modificateur de nbHeures.
     *
     * @param nbHeures le nbHeures à modifier
     */
    public void setNbHeures(String nbHeures) {
        this.nbHeures = nbHeures;
    }
}
