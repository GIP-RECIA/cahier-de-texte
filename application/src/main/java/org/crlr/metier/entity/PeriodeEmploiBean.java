/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PeriodeEmploiBean.java,v 1.2 2010/04/12 04:30:13 jerome.carriere Exp $
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
 * EmploiSequenceBean.
 *
 * @author breytond
 * @version $Revision: 1.2 $
 */
@Proxy(lazy = true)
@Entity
@Table(name = "cahier_periode_emploi", schema="cahier_courant")
public class PeriodeEmploiBean {
    /** Identifiant de la periode.  */
    @Id
    private Integer id;
    
    /** Date de debut de periode. La date de fin est la date de debut de periode suivante moins un jour. */
    @Column(name = "date_debut", nullable = false)
    private Date dateDebut;

    /** Identifiant de l'etablissement. */
    @Column(name = "id_etablissement", nullable = true)
    private Integer idEtablissement;
    
    /** Foreign key vers cahier_etablissement. */
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

    /** Identifiant de l'enseignant. */
    @Column(name = "id_enseignant", nullable = true)
    private Integer idEnseignant;
    
    /** Foreign key vers cahier_enseignant. */
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
    
    
    /**
     * Constructeur.
     */
    public PeriodeEmploiBean() {
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
     * Accesseur de etablissement {@link #etablissement}.
     * @return retourne etablissement
     */
    public EtablissementBean getEtablissement() {
        return etablissement;
    }

    /**
     * Mutateur de etablissement {@link #etablissement}.
     * @param etablissement le etablissement to set
     */
    public void setEtablissement(EtablissementBean etablissement) {
        this.etablissement = etablissement;
    }

    /**
     * Accesseur de idEnseignant {@link #idEnseignant}.
     * @return retourne idEnseignant
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur de idEnseignant {@link #idEnseignant}.
     * @param idEnseignant le idEnseignant to set
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur de enseignant {@link #enseignant}.
     * @return retourne enseignant
     */
    public EnseignantBean getEnseignant() {
        return enseignant;
    }

    /**
     * Mutateur de enseignant {@link #enseignant}.
     * @param enseignant le enseignant to set
     */
    public void setEnseignant(EnseignantBean enseignant) {
        this.enseignant = enseignant;
    }

    

    
}