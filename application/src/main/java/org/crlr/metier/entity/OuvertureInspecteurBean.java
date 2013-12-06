/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignementsSequencesBean.java,v 1.4 2010/04/12 04:30:13 jerome.carriere Exp $
 */

package org.crlr.metier.entity;

import java.util.Date;

import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_ouverture_inspecteur", schema = "cahier_courant")
public class OuvertureInspecteurBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    OuvertureInspecteurPK pk;

    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_enseignant", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private EnseignantBean enseignant;
    
    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_etablissement", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private EtablissementBean etablissement;
    
    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_inspecteur", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private InspecteurBean inspecteur;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "date_debut", nullable = false)
    private Date dateDebut;
    
    @Column(name = "date_fin")
    private Date dateFin;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "nomDirecteur", nullable = false)
    private String nomDirecteur;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "uidDirecteur", nullable = false)
    private String uidDirecteur;
    
    /**
     * Accesseur de pk.
     * @return le pk
     */
    public OuvertureInspecteurPK getPk() {
        return pk;
    }

    /**
     * Mutateur de pk.
     * @param pk le pk à modifier.
     */
    public void setPk(OuvertureInspecteurPK pk) {
        this.pk = pk;
    }

    /**
     * Accesseur de enseignant.
     * @return le enseignant
     */
    public EnseignantBean getEnseignant() {
        return enseignant;
    }

    /**
     * Mutateur de enseignant.
     * @param enseignant le enseignant à modifier.
     */
    public void setEnseignant(EnseignantBean enseignant) {
        this.enseignant = enseignant;
    }

    /**
     * Accesseur de etablissement.
     * @return le etablissement
     */
    public EtablissementBean getEtablissement() {
        return etablissement;
    }

    /**
     * Mutateur de etablissement.
     * @param etablissement le etablissement à modifier.
     */
    public void setEtablissement(EtablissementBean etablissement) {
        this.etablissement = etablissement;
    }

    /**
     * Accesseur de inspecteur.
     * @return le inspecteur
     */
    public InspecteurBean getInspecteur() {
        return inspecteur;
    }

    /**
     * Mutateur de inspecteur.
     * @param inspecteur le inspecteur à modifier.
     */
    public void setInspecteur(InspecteurBean inspecteur) {
        this.inspecteur = inspecteur;
    }

    /**
     * idEtablissement.
     *
     * @param idEtablissement id de l'établissement.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        if (this.pk == null) {
            this.pk = new OuvertureInspecteurPK();
        }
        this.pk.idEtablissement = idEtablissement;
    }
    
    /**
     * idEtablissement.
     *
     * @return the idEtablissement attribute
     */
    public Integer getIdEtablissement() {
        return (this.pk != null) ? pk.idEtablissement : null;
    }

    /**
     * idEnseignant.
     *
     * @param idEnseignant id de l'enseignant.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        if (this.pk == null) {
            this.pk = new OuvertureInspecteurPK();
        }
        this.pk.idEnseignant = idEnseignant;
    }
    
    
    /**
     * idEnseignant.
     *
     * @return the idEnseignant attribute
     */
    public Integer getIdEnseignant() {
        return (this.pk != null) ? pk.idEnseignant : null;
    }
    
    /**
     * idInspecteur.
     *
     * @param idInspecteur id de l'inspecteur.
     */
    public void setIdInspecteur(Integer idInspecteur) {
        if (this.pk == null) {
            this.pk = new OuvertureInspecteurPK();
        }
        this.pk.idInspecteur = idInspecteur;
    }

    
    /**
     * idInspecteur.
     *
     * @return the idInspecteur attribute
     */
    public Integer getIdInspecteur() {
        return (this.pk != null) ? pk.idInspecteur : null;
    }


    /**
     * Accesseur de nomDirecteur.
     * @return le nomDirecteur
     */
    public String getNomDirecteur() {
        return nomDirecteur;
    }

    /**
     * Mutateur de nomDirecteur.
     * @param nomDirecteur le nomDirecteur à modifier.
     */
    public void setNomDirecteur(String nomDirecteur) {
        this.nomDirecteur = nomDirecteur;
    }

    /**
     * Accesseur de uidDirecteur.
     * @return le uidDirecteur
     */
    public String getUidDirecteur() {
        return uidDirecteur;
    }

    /**
     * Mutateur de uidDirecteur.
     * @param uidDirecteur le uidDirecteur à modifier.
     */
    public void setUidDirecteur(String uidDirecteur) {
        this.uidDirecteur = uidDirecteur;
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

    
    
    
}
