/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirBean.java,v 1.4 2009/04/28 12:45:50 ent_breyton Exp $
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
 * @version $Revision: 1.4 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_archive_devoir", schema = "cahier_courant")
public class ArchiveDevoirBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    @Column(name = "id_archive_devoir", unique = true, nullable = false)
    private Integer idArchiveDevoir;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "date_remise", nullable = false)
    private Date dateRemise;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "code", nullable = false)
    private String code;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "intitule", nullable = false)
    private String intitule;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "description", nullable = false)
    private String description;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "id_type_devoir", nullable = true)
    private Integer idTypeDevoir;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "id_archive_seance", nullable = false)
    private Integer idArchiveSeance;

    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade = {})
    @JoinColumns({ @JoinColumn(name = "id_archive_seance", 
    referencedColumnName = "id_archive_seance", insertable = false, updatable = false)
    })
    private ArchiveSeanceBean archiveSeance;

    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade = {})
    @JoinColumns({ @JoinColumn(name = "id_type_devoir", referencedColumnName = "id", insertable = false, updatable = false) })
    private TypeDevoirBean typeDevoir;

    /**
     * Constructeur.
     */
    public ArchiveDevoirBean() {
    }

    /**
     * Accesseur dateRemise.
     * 
     * @return la date de remise d'un devoir.
     */
    public Date getDateRemise() {
        return dateRemise;
    }

    /**
     * Mutateur dateRemise.
     * 
     * @param dateRemise Le dateRemise à modifier
     */
    public void setDateRemise(Date dateRemise) {
        this.dateRemise = dateRemise;
    }

    /**
     * Mutateur code.
     * 
     * @param code le code.
     */
    public void setCode(String code) {
        this.code = code;
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
     * Accesseur intitule.
     * 
     * @return l'intitule.
     */
    public String getIntitule() {
        return intitule;
    }

    /**
     * Mutateur intitule.
     * 
     * @param intitule l'intitule.
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    /**
     * Accesseur description.
     * 
     * @return la description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutateur description.
     * 
     * @param description la description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Accesseur idTypeDevoir.
     * 
     * @return l'id du type de devoir.
     */
    public Integer getIdTypeDevoir() {
        return idTypeDevoir;
    }

    /**
     * Mutateur idTypeDevoir.
     * 
     * @param idTypeDevoir l'id du type de devoir à modifier.
     */
    public void setIdTypeDevoir(Integer idTypeDevoir) {
        this.idTypeDevoir = idTypeDevoir;
    }

    /**
     * Accesseur archiveSeance.
     * 
     * @return la séance.
     */
    public ArchiveSeanceBean getArchiveSeance() {
        return archiveSeance;
    }

    /**
     * Mutateur archiveSeance.
     * 
     * @param seance la seance à modifier.
     */
    public void setArchiveSeance(ArchiveSeanceBean seance) {
        this.archiveSeance = seance;
    }

    /**
     * Accesseur typeDevoir.
     * 
     * @return le type de devoir.
     */
    public TypeDevoirBean getTypeDevoir() {
        return typeDevoir;
    }

    /**
     * Mutateur typeDevoir.
     * 
     * @param typeDevoir le type de devoir à modifier.
     */
    public void setTypeDevoir(TypeDevoirBean typeDevoir) {
        this.typeDevoir = typeDevoir;
    }

    /**
     * @return the idArchiveDevoir
     */
    public Integer getIdArchiveDevoir() {
        return idArchiveDevoir;
    }

    /**
     * @param idArchiveDevoir the idArchiveDevoir to set
     */
    public void setIdArchiveDevoir(Integer idArchiveDevoir) {
        this.idArchiveDevoir = idArchiveDevoir;
    }

    /**
     * @return the idArchiveSeance
     */
    public Integer getIdArchiveSeance() {
        return idArchiveSeance;
    }

    /**
     * @param idArchiveSeance the idArchiveSeance to set
     */
    public void setIdArchiveSeance(Integer idArchiveSeance) {
        this.idArchiveSeance = idArchiveSeance;
    }
}
