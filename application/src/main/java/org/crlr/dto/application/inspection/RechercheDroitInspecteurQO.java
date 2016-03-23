/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.dto.application.inspection;

import java.io.Serializable;



/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class RechercheDroitInspecteurQO implements Serializable {

    private static final long serialVersionUID = 3037131061822110440L;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idEtablissement;
    
    private Integer idInspecteur;
    
    private Boolean vraiOuFauxRechercheFromDirecteur;

    /**
     * Accesseur de idEtablissement.
     * @return le idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur de idEtablissement.
     * @param idEtablissement le idEtablissement à modifier.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    

    
    /**
     * Accesseur de idInspecteur.
     * @return le idInspecteur
     */
    public Integer getIdInspecteur() {
        return idInspecteur;
    }

    /**
     * Mutateur de idInspecteur.
     * @param idInspecteur le idInspecteur à modifier.
     */
    public void setIdInspecteur(Integer idInspecteur) {
        this.idInspecteur = idInspecteur;
    }

    /**
     * Accesseur de vraiOuFauxRechercheFromDirecteur.
     * @return le vraiOuFauxRechercheFromDirecteur
     */
    public Boolean getVraiOuFauxRechercheFromDirecteur() {
        return vraiOuFauxRechercheFromDirecteur;
    }

    /**
     * Mutateur de vraiOuFauxRechercheFromDirecteur.
     * @param vraiOuFauxRechercheFromDirecteur le vraiOuFauxRechercheFromDirecteur à modifier.
     */
    public void setVraiOuFauxRechercheFromDirecteur(
            Boolean vraiOuFauxRechercheFromDirecteur) {
        this.vraiOuFauxRechercheFromDirecteur = vraiOuFauxRechercheFromDirecteur;
    }
    
    

}
