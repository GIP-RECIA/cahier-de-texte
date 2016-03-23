/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.dto.application.inspection;

import java.io.Serializable;
import java.util.Date;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.base.UserDTOForList;



/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class DroitInspecteurDTO implements Serializable {

    private static final long serialVersionUID = 3037131061822110440L;

    /** le directeur qui a donner les droits (l'identifiant est l'uid). */
    private UserDTOForList directeur;
    
    /** L'inspecteur qui a les droits (l'identifiant est son id). */
    private UserDTOForList inspecteur;

    /** L'enseignant sur qui on a les droits (l'identifiant est l'id). */
    private EnseignantDTO enseignantDTO;

    /** Id de l'établissement. */
    private Integer idEtablissement;

    /** Debut de droits. */
    private Date dateDebut;

    /** Date de fin du droit. */
    private Date dateFin;

    /** Indique si la ligne a ete modifiee. */
    private Boolean estModifiee;

    /** Indique que la ligne a ete supprimee. */
    private Boolean estSupprimee;
    
    /** Indique s'il s'agit d'un nouveau droit ou d'un droit existant. */
    private Boolean estAjoute;
    
    /**
     * Constructeur.
     */
    public DroitInspecteurDTO() {
        super();
        estModifiee = false;
        estSupprimee = false;
        estAjoute = false;
    }

    /**
     * Accesseur de inspecteur.
     * @return le inspecteur
     */
    public UserDTOForList getInspecteur() {
        return inspecteur;
    }

    /**
     * Mutateur de inspecteur.
     * @param inspecteur le inspecteur à modifier.
     */
    public void setInspecteur(UserDTOForList inspecteur) {
        this.inspecteur = inspecteur;
    }

    /**
     * Accesseur de enseignant.
     * @return le enseignant
     */
    public EnseignantDTO getEnseignant() {
        return enseignantDTO;
    }

    /**
     * Mutateur de enseignant.
     * @param enseignant le enseignant à modifier.
     */
    public void setEnseignantDTO(EnseignantDTO enseignant) {
        this.enseignantDTO = enseignant;
    }

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
     * Accesseur de directeur.
     * @return le directeur
     */
    public UserDTOForList getDirecteur() {
        return directeur;
    }

    /**
     * Mutateur de directeur.
     * @param directeur le directeur à modifier.
     */
    public void setDirecteur(UserDTOForList directeur) {
        this.directeur = directeur;
    }

    /**
     * @return the dateDebut
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * @param dateDebut the dateDebut to set
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @return the dateFin
     */
    public Date getDateFin() {
        return dateFin;
    }

    /**
     * @param dateFin the dateFin to set
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * Accesseur de estModifiee {@link #estModifiee}.
     * @return retourne estModifiee
     */
    public Boolean getEstModifiee() {
        return estModifiee;
    }

    /**
     * Mutateur de estModifiee {@link #estModifiee}.
     * @param estModifiee le estModifiee to set
     */
    public void setEstModifiee(Boolean estModifiee) {
        this.estModifiee = estModifiee;
    }

    /**
     * Accesseur de estSupprimee {@link #estSupprimee}.
     * @return retourne estSupprimee
     */
    public Boolean getEstSupprimee() {
        return estSupprimee;
    }

    /**
     * Mutateur de estSupprimee {@link #estSupprimee}.
     * @param estSupprimee le estSupprimee to set
     */
    public void setEstSupprimee(Boolean estSupprimee) {
        this.estSupprimee = estSupprimee;
    }

    /**
     * Accesseur de estAjoute {@link #estAjoute}.
     * @return retourne estAjoute
     */
    public Boolean getEstAjoute() {
        return estAjoute;
    }

    /**
     * Mutateur de estAjoute {@link #estAjoute}.
     * @param estAjoute le estAjoute to set
     */
    public void setEstAjoute(Boolean estAjoute) {
        this.estAjoute = estAjoute;
    }
    
    


}
