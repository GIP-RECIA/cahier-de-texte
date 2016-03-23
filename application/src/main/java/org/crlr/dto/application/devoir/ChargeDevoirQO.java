/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ChargeDevoirQO.java,v 1.1 2010/04/01 11:06:15 ent_breyton Exp $
 */

package org.crlr.dto.application.devoir;

import java.io.Serializable;
import java.util.Date;

/**
 * ChargeDevoirQO.
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class ChargeDevoirQO implements Serializable {
   
    /** Serial de la classe . */
    private static final long serialVersionUID = -2302340228470847340L;

    /** Id de la classe. */
    private Integer idClasse;
    
    /** Id du groupe. */
    private Integer idGroupe;

    /** Identifiant du devoir. Valeur nulle dans le cas d'un ajout de devoir. */
    private Integer idDevoir;
    
    /** Date de remise du devoir. */
    private Date dateDevoir;
    
    /** Identifiant de l'établissement. */
    private Integer idEtablissement;

    /** Identifiant de l'enseignant connecte. */
    private String uidEnseignant;
    
    /** Constructeur de la classe .*/
    public ChargeDevoirQO() {
    }

    /**
     * Accesseur de idClasse.
     * @return the idClasse
     */
    public Integer getIdClasse() {
        return idClasse;
    }

    /**
     * Mutator de idClasse. 
     * @param idClasse the idClasse to set
     */
    public void setIdClasse(Integer idClasse) {
        this.idClasse = idClasse;
    }
    
    /**
     * Accesseur de idGroupe.
     * @return the idGroupe
     */
    public Integer getIdGroupe() {
        return idGroupe;
    }

    /**
     * Mutator de idGroupe. 
     * @param idGroupe the idGroupe to set
     */
    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }
    
    /**
     * Accesseur de idDevoir.
     * @return the idDevoir
     */
    public Integer getIdDevoir() {
        return idDevoir;
    }

    /**
     * Mutateur de idDevoir.
     * @param idDevoir the idDevoir to set
     */
    public void setIdDevoir(Integer idDevoir) {
        this.idDevoir = idDevoir;
    }

    /**
     * Accesseur de dateDevoir.
     * @return the dateDevoir
     */
    public Date getDateDevoir() {
        return dateDevoir;
    }

    /**
     * Mutateur de dateDevoir.
     * @param dateDevoir the dateDevoir to set
     */
    public void setDateDevoir(Date dateDevoir) {
        this.dateDevoir = dateDevoir;
    }

    /**
     * Accesseur de idEtablissement.
     * @return the idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur de idEtablissement.
     * @param idEtablissement the idEtablissement to set
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Retourne le uidEnseignant de l'enseignant connecte.
     * @return the uidEnseignant
     */
    public String getUidEnseignant() {
        return uidEnseignant;
    }

    /**
     * Mutateur uidEnseignant.
     * @param uidEnseignant the uidEnseignant to set
     */
    public void setUidEnseignant(String uidEnseignant) {
        this.uidEnseignant = uidEnseignant;
    }
    
    

    
    
    
}
