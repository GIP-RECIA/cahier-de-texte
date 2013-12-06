/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignantsGroupesBean.java,v 1.4 2009/04/28 12:45:50 ent_breyton Exp $
 */

package org.crlr.metier.entity;

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
 * @author $author$
 * @version $Revision: 1.4 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_visa_seance", schema="cahier_courant")
public class VisaSeanceBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private VisaSeancePK pk;

   
    
    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_visa", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
    )
    private VisaBean visa;

    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_Seance", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private SeanceBean seance;

    /**
     * 
     * Constructeur.
     */
    public VisaSeanceBean() {
    }

    /**
     * Accesseur pk.
     *
     * @return pk
     */
    public VisaSeancePK getPk() {
        return pk;
    }

    /**
     * Mutateur pk.
     *
     * @param pk Le pk à modifier
     */
    public void setPk(VisaSeancePK pk) {
        this.pk = pk;
    }



    /**
     * Accesseur de visa {@link #visa}.
     * @return retourne visa
     */
    public VisaBean getVisa() {
        return visa;
    }

    /**
     * Mutateur de visa {@link #visa}.
     * @param visa le visa to set
     */
    public void setVisa(VisaBean visa) {
        this.visa = visa;
    }

    /**
     * Accesseur de seance {@link #seance}.
     * @return retourne seance
     */
    public SeanceBean getSeance() {
        return seance;
    }

    /**
     * Mutateur de seance {@link #seance}.
     * @param seance le seance to set
     */
    public void setSeance(SeanceBean seance) {
        this.seance = seance;
    }

    /**
     * idVisa.
     *
     * @return the idVisa attribute
     */
    public Integer getIdVisa() {
        return (this.pk != null) ? pk.idVisa : null;
    }

    /**
     * idVisa.
     *
     * @param idVisa id 
     */
    public void setIdVisa(Integer idVisa) {
        if (this.pk == null) {
            this.pk = new VisaSeancePK();
        }
        this.pk.idVisa = idVisa;
    }
    
   

    /**
     * idSeance.
     *
     * @return the idSeance attribute
     */
    public Integer getIdSeance() {
        return (this.pk != null) ? pk.idSeance : null;
    }

    /**
     * idSeance.
     *
     * @param idSeance id .
     */
    public void setIdSeance(Integer idSeance) {
        if (this.pk == null) {
            this.pk = new VisaSeancePK();
        }
        this.pk.idSeance = idSeance;
    }

    
    
}
