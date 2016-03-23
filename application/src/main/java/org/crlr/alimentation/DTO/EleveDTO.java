/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EleveDTO.java,v 1.1 2009/04/08 08:54:26 weberent Exp $
 */

package org.crlr.alimentation.DTO;

import org.crlr.metier.entity.EleveBean;

import java.util.List;

/**
 * DTO pour transferer les eleves du ldap vers une BD.
 *
 * @author Aurore
 * @version $Revision: 1.1 $
 */
public class EleveDTO {
    /** L'eleve à inserer. */
    private EleveBean elBean;

    /**
     * Le dn contenant le siren de l'etablissement de rattachement, directement
     * issus du ldap.
     */
    private String etablissementDNSiren;

    /**
     * La liste des dn contenant les noms des classes et des etablissements
     * auxquels l'eleve appartient.
     */
    private List<String> classes;

    /**
     * La listes des dn contenant les noms des groupes et des etablissements
     * auxquels l'eleve appartient.
     */
    private List<String> groupes;

/**
    * Constructeur de la classe. Ne fait rien.
    */
    public EleveDTO() {
    }

    /**
     * Mutateur de l'attribut etablissementDNSiren.
     *
     * @param etablissementDNSiren Nouvelle valeur de etablissementDNSiren
     */
    public void setEtablissementDNSiren(String etablissementDNSiren) {
        this.etablissementDNSiren = etablissementDNSiren;
    }

    /**
     * Accesseur de l'attribut etablissementDNSiren.
     *
     * @return la valeur de etablissementDNSiren
     */
    public String getEtablissementDNSiren() {
        return etablissementDNSiren;
    }

    /**
     * Mutateur de l'attribut classes.
     *
     * @param classes La nouvelle valeur de classes
     */
    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    /**
     * Accesseur de l'attribut classes.
     *
     * @return La valeur de classes
     */
    public List<String> getClasses() {
        return classes;
    }

    /**
     * Mutateur de l'attribut groupe.
     *
     * @param groupes La nouvelle valeur de classes
     */
    public void setGroupes(List<String> groupes) {
        this.groupes = groupes;
    }

    /**
     * Accesseur de l'attribut groupes.
     *
     * @return La valeur de groupes
     */
    public List<String> getGroupes() {
        return groupes;
    }

    /**
     * Mutateur de l'attribut elBean.
     *
     * @param elBean La nouvelle valeur de elBean
     */
    public void setElBean(EleveBean elBean) {
        this.elBean = elBean;
    }

    /**
     * Accesseur de l'attribut elBean.
     *
     * @return La valeur de elBean
     */
    public EleveBean getElBean() {
        return elBean;
    }
}
