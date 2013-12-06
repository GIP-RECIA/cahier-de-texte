/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ClasseBean.java,v 1.8 2009/04/28 12:45:50 ent_breyton Exp $
 */

package org.crlr.metier.entity;

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
 * @author vibertd
 * @version $Revision: 1.8 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_classe", schema="cahier_courant")
public class ClasseBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private Integer id;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "code", nullable = false)
    private String code;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "designation", nullable = false)
    private String designation;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "id_etablissement", nullable = false)
    private Integer idEtablissement;

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
    @Column(name = "id_annee_scolaire", nullable = false)
    private Integer idAnneeScolaire;

    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_annee_scolaire", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private AnneeScolaireBean anneeScolaire;

/**
     * Constructeur.
     */
    public ClasseBean() {
    }

    /**
     * Retourne l'id.
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Positionne l'id.
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur code.
     *
     * @return le code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Mutateur code.
     *
     * @param code Le code à modifier
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Accesseur idAnneeScolaire.
     *
     * @return l'id de l'année scolaire.
     */
    public Integer getIdAnneeScolaire() {
        return idAnneeScolaire;
    }

    /**
     * Mutateur idAnneeScolaire.
     *
     * @param idAnneeScolaire l'id de l'année scolaire à modifier.
     */
    public void setIdAnneeScolaire(Integer idAnneeScolaire) {
        this.idAnneeScolaire = idAnneeScolaire;
    }

    /**
     * Accesseur anneeScolaire.
     *
     * @return l'année scolaire.
     */
    public AnneeScolaireBean getAnneeScolaire() {
        return anneeScolaire;
    }

    /**
     * Mutateur anneeScolaire.
     *
     * @param anneeScolaire l'année scolaire à modifier.
     */
    public void setAnneeScolaire(AnneeScolaireBean anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    /**
     * Mutateur designation.
     *
     * @param designation la designation à modifier.
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * Accesseur designation.
     *
     * @return le nom du groupe
     */
    public String getDesignation() {
        return designation;
    }

    /**
     *
     *
     * @param idEtablissement DOCUMENTATION INCOMPLETE!
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     *
     *
     * @param etablissement DOCUMENTATION INCOMPLETE!
     */
    public void setEtablissement(EtablissementBean etablissement) {
        this.etablissement = etablissement;
    }

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public EtablissementBean getEtablissement() {
        return etablissement;
    }
}
