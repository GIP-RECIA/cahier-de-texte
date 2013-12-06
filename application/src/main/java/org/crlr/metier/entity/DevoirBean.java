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
@Table(name = "cahier_devoir", schema="cahier_courant")
public class DevoirBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private Integer id;

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
    @Column(name = "id_seance", nullable = false)
    private Integer idSeance;
    
    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_seance", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private SeanceBean seance;
    
    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_type_devoir", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private TypeDevoirBean typeDevoir;

/**
     * Constructeur.
     */
    public DevoirBean() {
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
     * Accesseur dateRemise.
     *
     * @return la date de remise d'un devoir.
     */
    public Date getDateRemise() {
        return dateRemise;
    }

    /**
     * Mutateur dateRemise.
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
    * Accesseur idSeance.
    *
    * @return l'id de la seance.
    */
   public Integer getIdSeance() {
       return idSeance;
   }

   /**
    * Mutateur idSeance.
    *
    * @param idSeance l'id de la seance à modifier.
    */
   public void setIdSeance(Integer idSeance) {
       this.idSeance = idSeance;
   }
   
   /**
    * Accesseur seance.
    *
    * @return la séance.
    */
   public SeanceBean getSeance() {
       return seance;
   }

   /**
    * Mutateur seance.
    *
    * @param seance la seance à modifier.
    */
   public void setSeance(SeanceBean seance) {
       this.seance = seance;
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
}
