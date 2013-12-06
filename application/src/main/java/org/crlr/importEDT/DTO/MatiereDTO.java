/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.importEDT.DTO;

import java.io.Serializable;

/**
 * Classe qui permet de représenter une Matière décrite dans le XML de l'import EDT.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public class MatiereDTO implements Serializable{
    /**
     * Le serialVersionUid.
     */
    private static final long serialVersionUID  = 1L;
    
    /**
     * Code de la matière.
     */
    private String code;

    /**
     * code de gestion de la matière.
     */
    private String codeGestion;

    /**
     * Libellé court de la matière.
     */
    private String libelleCourt;

    /**
     * Libellé long de la matière.
     */
    private String libelleLong;

    /**
     * Libellé d'édition de la matière.
     */
    private String libelleEdition;

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
     * Accesseur de codeGestion.
     *
     * @return le codeGestion
     */
    public String getCodeGestion() {
        return codeGestion;
    }

    /**
     * Modificateur de codeGestion.
     *
     * @param codeGestion le codeGestion à modifier
     */
    public void setCodeGestion(String codeGestion) {
        this.codeGestion = codeGestion;
    }

    /**
     * Accesseur de libelleCourt.
     *
     * @return le libelleCourt
     */
    public String getLibelleCourt() {
        return libelleCourt;
    }

    /**
     * Modificateur de libelleCourt.
     *
     * @param libelleCourt le libelleCourt à modifier
     */
    public void setLibelleCourt(String libelleCourt) {
        this.libelleCourt = libelleCourt;
    }

    /**
     * Accesseur de libelleLong.
     *
     * @return le libelleLong
     */
    public String getLibelleLong() {
        return libelleLong;
    }

    /**
     * Modificateur de libelleLong.
     *
     * @param libelleLong le libelleLong à modifier
     */
    public void setLibelleLong(String libelleLong) {
        this.libelleLong = libelleLong;
    }

    /**
     * Accesseur de libelleEdition.
     *
     * @return le libelleEdition
     */
    public String getLibelleEdition() {
        return libelleEdition;
    }

    /**
     * Modificateur de libelleEdition.
     *
     * @param libelleEdition le libelleEdition à modifier
     */
    public void setLibelleEdition(String libelleEdition) {
        this.libelleEdition = libelleEdition;
    }
}
