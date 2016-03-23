/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignantDTO.java,v 1.2 2009/04/22 13:44:51 ent_breyton Exp $
 */

package org.crlr.alimentation.DTO;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.crlr.metier.entity.EnseignantBean;

/**
 * DTO pour transferer les enseignants du ldap vers une BD.
 *
 * @author Aurore
 * @version $Revision: 1.2 $
 */
public class EnseignantDTO {
    /** L'enseignant à inserer. */
    private EnseignantBean ensBean;

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

    /** La listes des chaines contenant les codes et libelles des enseignements. */
    private List<String> enseignements;
    
    private String nom;

    private String prenom;
    
    private String civilite;
    
    private Integer id;
    
    private String uid;
    
    /**
     * Pour l'inspection.
     */
    private Boolean vraiOuFauxSelectionne;

/** 
     * Le constructeur de la classe. Ne fait rien.
     * */
    public EnseignantDTO() {
        vraiOuFauxSelectionne = false;
    }
    
    /**
     * @param civilite p
     * @param prenom p
     * @param nom p
     */
    public EnseignantDTO(String civilite, String prenom, String nom) {
        this.civilite = civilite;
        this.prenom = prenom;
        this.nom = nom;
        vraiOuFauxSelectionne = false;
    }


    /**
     * @return le nom complet
     */
    public String getNomComplet() {
        return StringUtils.trimToEmpty(
                StringUtils.trimToEmpty(civilite) + " " +
                StringUtils.trimToEmpty(prenom) +  " " +
                StringUtils.trimToEmpty(nom));
    }
    
    
    /**
     * Accesseur ensBean.
     *
     * @return le ensBean.
     */
    public EnseignantBean getEnsBean() {
        return ensBean;
    }

    /**
     * Mutateur ensBean.
     *
     * @param ensBean le ensBean à modifier.
     */
    public void setEnsBean(EnseignantBean ensBean) {
        this.ensBean = ensBean;
    }

    /**
     * Accesseur etablissementDNSiren.
     *
     * @return le etablissementDNSiren.
     */
    public String getEtablissementDNSiren() {
        return etablissementDNSiren;
    }

    /**
     * Mutateur etablissementDNSiren.
     *
     * @param etablissementDNSiren le etablissementDNSiren à modifier.
     */
    public void setEtablissementDNSiren(String etablissementDNSiren) {
        this.etablissementDNSiren = etablissementDNSiren;
    }

    /**
     * Accesseur classes.
     *
     * @return le classes.
     */
    public List<String> getClasses() {
        return classes;
    }

    /**
     * Mutateur classes.
     *
     * @param classes le classes à modifier.
     */
    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    /**
     * Accesseur groupes.
     *
     * @return le groupes.
     */
    public List<String> getGroupes() {
        return groupes;
    }

    /**
     * Mutateur groupes.
     *
     * @param groupes le groupes à modifier.
     */
    public void setGroupes(List<String> groupes) {
        this.groupes = groupes;
    }

    /**
     * Accesseur enseignements.
     *
     * @return le enseignements.
     */
    public List<String> getEnseignements() {
        return enseignements;
    }

    /**
     * Mutateur enseignements.
     *
     * @param enseignements le enseignements à modifier.
     */
    public void setEnseignements(List<String> enseignements) {
        this.enseignements = enseignements;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @return the civilite
     */
    public String getCivilite() {
        return civilite;
    }

    /**
     * @param civilite the civilite to set
     */
    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    /**
     * @return the vraiOuFauxSelectionne
     */
    public Boolean getVraiOuFauxSelectionne() {
        return vraiOuFauxSelectionne;
    }

    /**
     * @param vraiOuFauxSelectionne the vraiOuFauxSelectionne to set
     */
    public void setVraiOuFauxSelectionne(Boolean vraiOuFauxSelectionne) {
        this.vraiOuFauxSelectionne = vraiOuFauxSelectionne;
    }

    /**
     * Accesseur de uid {@link #uid}.
     * @return retourne uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Mutateur de uid {@link #uid}.
     * @param uid le uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EnseignantDTO [nom=" + nom + ", prenom=" + prenom
                + ", civilite=" + civilite + ", id=" + id + ", uid=" + uid
                + "]";
    }
    
}
