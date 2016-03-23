/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: TestDTO.java,v 1.1 2009/05/26 07:34:42 ent_breyton Exp $
 */

package org.crlr.jasperreport.impl;

/**
 * TestDTO.
 *
 * @author breytond
 * @version $Revision: 1.1 $
 */
public class TestDTO {
    /** nom. */
    private String nom;

    /** prénom. */
    private String prenom;

    /** âge. */
    private Integer age;

    /**
     * Constructeur.
     * @param nom nom.
     * @param prenom prénom.
     * @param age âge.
     */
    public TestDTO(final String nom, final String prenom, final Integer age) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
    }

    /**
     * Accesseur nom.
     *
     * @return le nom.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Mutateur nom.
     *
     * @param nom le nom à modifier.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Accesseur prenom.
     *
     * @return le prenom.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Mutateur prenom.
     *
     * @param prenom le prenom à modifier.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Accesseur age.
     *
     * @return le age.
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Mutateur age.
     *
     * @param age le age à modifier.
     */
    public void setAge(Integer age) {
        this.age = age;
    }
}
