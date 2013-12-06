/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.dto;

/**
 * WebDavQO.
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class WebDavQO {
    /** uid. */
    private String uid;
   
    /** le dépôt de fichier. */
    private String depot = "";
    
    /** chemin du fichier DAV sélectionné. */
    private String cheminFichierDav;
    
    /**
     * Chemin de destination du fichier en provenance de stockage vers cahier.
     */
    private String cheminDestinationCahier;

    /**
     * Accesseur uid.
     *
     * @return le uid.
     */
    public String getUid() {
        return uid;
    }

    /**
     * Mutateur uid.
     *
     * @param uid le uid à modifier.
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Accesseur depot.
     *
     * @return le depot.
     */
    public String getDepot() {
        return depot;
    }

    /**
     * Mutateur depot.
     *
     * @param depot le depot à modifier.
     */
    public void setDepot(String depot) {
        this.depot = depot;
    }

    /**
     * Accesseur cheminFichierDav.
     * @return le cheminFichierDav.
     */
    public String getCheminFichierDav() {
        return cheminFichierDav;
    }

    /**
     * Mutateur cheminFichierDav.
     * @param cheminFichierDav le cheminFichierDav à modifier.
     */
    public void setCheminFichierDav(String cheminFichierDav) {
        this.cheminFichierDav = cheminFichierDav;
    }

    /**
     * Accesseur cheminDestinationCahier.
     * @return le cheminDestinationCahier.
     */
    public String getCheminDestinationCahier() {
        return cheminDestinationCahier;
    }

    /**
     * Mutateur cheminDestinationCahier.
     * @param cheminDestinationCahier le cheminDestinationCahier à modifier.
     */
    public void setCheminDestinationCahier(String cheminDestinationCahier) {
        this.cheminDestinationCahier = cheminDestinationCahier;
    }
}
