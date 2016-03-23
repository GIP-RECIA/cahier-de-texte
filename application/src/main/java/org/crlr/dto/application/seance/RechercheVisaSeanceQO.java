/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheSeanceQO.java,v 1.14 2010/04/28 16:12:21 jerome.carriere Exp $
 */

package org.crlr.dto.application.seance;

import java.io.Serializable;

import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.UtilisateurDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.14 $
 */
public class RechercheVisaSeanceQO extends RechercheSeanceQO implements Serializable {
    
    /**  Serial. */
    private static final long serialVersionUID = 9212186448887386629L;

    /** Affichage des non visees. */
    private Boolean afficheNonVisees;
    
    /** Affichage des visees. */
    private Boolean afficheVisees;
    
    /** Affichage des perimees. */
    private Boolean affichePerimees;
        
    UtilisateurDTO utilisateurConnecte;
    
    private Integer idEnseignantRemplacant;
    
    /**
     * Constructeur par defaut.
     */
    public RechercheVisaSeanceQO() {
        super();
        afficheNonVisees = true;
        afficheVisees = false;
        affichePerimees = true;
    }

    /**
     * Accesseur de afficheNonVisees {@link #afficheNonVisees}.
     * @return retourne afficheNonVisees
     */
    public Boolean getAfficheNonVisees() {
        return afficheNonVisees;
    }

    /**
     * Mutateur de afficheNonVisees {@link #afficheNonVisees}.
     * @param afficheNonVisees le afficheNonVisees to set
     */
    public void setAfficheNonVisees(Boolean afficheNonVisees) {
        this.afficheNonVisees = afficheNonVisees;
    }

    /**
     * Accesseur de afficheVisees {@link #afficheVisees}.
     * @return retourne afficheVisees
     */
    public Boolean getAfficheVisees() {
        return afficheVisees;
    }

    /**
     * Mutateur de afficheVisees {@link #afficheVisees}.
     * @param afficheVisees le afficheVisees to set
     */
    public void setAfficheVisees(Boolean afficheVisees) {
        this.afficheVisees = afficheVisees;
    }

    /**
     * Accesseur de affichePerimees {@link #affichePerimees}.
     * @return retourne affichePerimees
     */
    public Boolean getAffichePerimees() {
        return affichePerimees;
    }

    /**
     * Mutateur de affichePerimees {@link #affichePerimees}.
     * @param affichePerimees le affichePerimees to set
     */
    public void setAffichePerimees(Boolean affichePerimees) {
        this.affichePerimees = affichePerimees;
    }
    
    /**
     * @return the utilisateurConnecte
     */
    public UtilisateurDTO getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    /**
     * @param utilisateurConnecte the utilisateurConnecte to set
     */
    public void setUtilisateurConnecte(UtilisateurDTO utilisateurConnecte) {
        this.utilisateurConnecte = utilisateurConnecte;
    }

    /**
     * @return the idEnseignantRemplacant
     */
    public Integer getIdEnseignantRemplacant() {
        return idEnseignantRemplacant;
    }

    /**
     * @param idEnseignantRemplacant the idEnseignantRemplacant to set
     */
    public void setIdEnseignantRemplacant(Integer idEnseignantRemplacant) {
        this.idEnseignantRemplacant = idEnseignantRemplacant;
    }
    
    /**
     * @deprecated
     * @param profil
     */
    public void setProfil(Profil profil) {
        if (utilisateurConnecte == null) {
            utilisateurConnecte = new UtilisateurDTO();
        }
        
        utilisateurConnecte.setProfil(profil);
    }
    
}
