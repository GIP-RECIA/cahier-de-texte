/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 * Devoir d'une Seance d'une sequence pedagogique (carnet de bord)
 * $Id: CycleDevoirBean.java,v 1.6 2009/04/28 12:45:50 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author vibertd
 * @version $Revision: 1.6 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_remplacement", schema="cahier_courant")
public class RemplacementBean {
    
    /** Identifiant. */
    @Id
    private Integer id;

    /** Id de l'etablissement. */
    @Column(name = "id_etablissement", nullable = false)
    private Integer idEtablissement;
    
    /** Id de l'enseignant qui est remplacé. */
    @Column(name = "id_enseignant_absent", nullable = false)
    private Integer idEnseignantAbsent;

    /** Id de l'enseignant qui remplace. */
    @Column(name = "id_enseignant_remplacant", nullable = false)
    private Integer idEnseignantRemplacant;

    /** Date de debut de remplacement. */
    @Column(name = "date_debut", nullable = false)
    private Date dateDebut;
    
    /** Date de fin de remplacement. */
    @Column(name = "date_fin", nullable = false)
    private Date dateFin;

    /** Objet EnseignantAbsent.  */
    @ManyToOne(cascade =  { } )
    @JoinColumns(
            {
                @JoinColumn(name = "id_enseignant_absent", referencedColumnName = "id", insertable = false, updatable = false)
            }
    )
    private EnseignantBean enseignantAbsent;
    
    /** Objet EnseignantRemplacant.  */
    @ManyToOne(cascade =  { } )
    @JoinColumns(
            {
                @JoinColumn(name = "id_enseignant_remplacant", referencedColumnName = "id", insertable = false, updatable = false)
            }
    )
    private EnseignantBean enseignantRemplacant;
    
    
    /**
     * Constructeur.
     */
    public RemplacementBean() {
    }


    /**
     * Accesseur de id {@link #id}.
     * @return retourne id
     */
    public Integer getId() {
        return id;
    }


    /**
     * Mutateur de id {@link #id}.
     * @param id le id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }


    /**
     * Accesseur de idEtablissement {@link #idEtablissement}.
     * @return retourne idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }


    /**
     * Mutateur de idEtablissement {@link #idEtablissement}.
     * @param idEtablissement le idEtablissement to set
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }


    /**
     * Accesseur de idEnseignantAbsent {@link #idEnseignantAbsent}.
     * @return retourne idEnseignantAbsent
     */
    public Integer getIdEnseignantAbsent() {
        return idEnseignantAbsent;
    }


    /**
     * Mutateur de idEnseignantAbsent {@link #idEnseignantAbsent}.
     * @param idEnseignantAbsent le idEnseignantAbsent to set
     */
    public void setIdEnseignantAbsent(Integer idEnseignantAbsent) {
        this.idEnseignantAbsent = idEnseignantAbsent;
    }


    /**
     * Accesseur de idEnseignantRemplacant {@link #idEnseignantRemplacant}.
     * @return retourne idEnseignantRemplacant
     */
    public Integer getIdEnseignantRemplacant() {
        return idEnseignantRemplacant;
    }


    /**
     * Mutateur de idEnseignantRemplacant {@link #idEnseignantRemplacant}.
     * @param idEnseignantRemplacant le idEnseignantRemplacant to set
     */
    public void setIdEnseignantRemplacant(Integer idEnseignantRemplacant) {
        this.idEnseignantRemplacant = idEnseignantRemplacant;
    }


    /**
     * Accesseur de dateDebut {@link #dateDebut}.
     * @return retourne dateDebut
     */
    public Date getDateDebut() {
        return dateDebut;
    }


    /**
     * Mutateur de dateDebut {@link #dateDebut}.
     * @param dateDebut le dateDebut to set
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }


    /**
     * Accesseur de dateFin {@link #dateFin}.
     * @return retourne dateFin
     */
    public Date getDateFin() {
        return dateFin;
    }


    /**
     * Mutateur de dateFin {@link #dateFin}.
     * @param dateFin le dateFin to set
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }


    /**
     * Accesseur de enseignantAbsent {@link #enseignantAbsent}.
     * @return retourne enseignantAbsent
     */
    public EnseignantBean getEnseignantAbsent() {
        return enseignantAbsent;
    }


    /**
     * Mutateur de enseignantAbsent {@link #enseignantAbsent}.
     * @param enseignantAbsent le enseignantAbsent to set
     */
    public void setEnseignantAbsent(EnseignantBean enseignantAbsent) {
        this.enseignantAbsent = enseignantAbsent;
    }


    /**
     * Accesseur de enseignantRemplacant {@link #enseignantRemplacant}.
     * @return retourne enseignantRemplacant
     */
    public EnseignantBean getEnseignantRemplacant() {
        return enseignantRemplacant;
    }


    /**
     * Mutateur de enseignantRemplacant {@link #enseignantRemplacant}.
     * @param enseignantRemplacant le enseignantRemplacant to set
     */
    public void setEnseignantRemplacant(EnseignantBean enseignantRemplacant) {
        this.enseignantRemplacant = enseignantRemplacant;
    }


    

    
}
