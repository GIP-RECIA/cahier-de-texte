/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SeanceBean.java,v 1.6 2009/04/28 12:45:50 ent_breyton Exp $
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
@Table(name = "cahier_archive_seance", schema="cahier_courant")
public class ArchiveSeanceBean {
    
    /** Id du la seance : identique a id de la table cahier_seance. */
    
    @Id
    @Column(name = "id_archive_seance", unique=true, nullable = false)
    private Integer idArchiveSeance;
    
    @Column(name = "id_seance", nullable = false)
    private Integer idSeance;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "code", nullable = false)
    private String code;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "intitule", nullable = false)
    private String intitule;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "annotations", nullable = true)
    private String annotations;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "date", nullable = false)
    private Date date;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "heure_debut", nullable = false)
    private Integer heureDebut;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "minute_debut", nullable = false)
    private Integer minuteDebut;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "heure_fin", nullable = false)
    private Integer heureFin;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "minute_fin", nullable = false)
    private Integer minuteFin;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "description", nullable = true)
    private String description;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "id_sequence", nullable = false)
    private Integer idSequence;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "id_enseignant", nullable = false)
    private Integer idEnseignant;
    
    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_sequence", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private SequenceBean sequence;
    
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
    
   

    /**
     * Constructeur.
     */
    public ArchiveSeanceBean() {
        
    }

    


    /**
     * Accesseur code.
     * @return le code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Mutateur code.
     * @param code le code à modifier.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Accesseur intitule.
     * @return le intitule.
     */
    public String getIntitule() {
        return intitule;
    }

    /**
     * Mutateur intitule.
     * @param intitule le intitule à modifier.
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    /**
     * Accesseur date.
     * @return le date.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Mutateur date.
     * @param date le date à modifier.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Accesseur heureDebut.
     * @return le heureDebut.
     */
    public Integer getHeureDebut() {
        return heureDebut;
    }

    /**
     * Mutateur heureDebut.
     * @param heureDebut le heureDebut à modifier.
     */
    public void setHeureDebut(Integer heureDebut) {
        this.heureDebut = heureDebut;
    }

    /**
     * Accesseur minuteDebut.
     * @return le minuteDebut.
     */
    public Integer getMinuteDebut() {
        return minuteDebut;
    }

    /**
     * Mutateur minuteDebut.
     * @param minuteDebut le minuteDebut à modifier.
     */
    public void setMinuteDebut(Integer minuteDebut) {
        this.minuteDebut = minuteDebut;
    }

    /**
     * Accesseur heureFin.
     * @return le heureFin.
     */
    public Integer getHeureFin() {
        return heureFin;
    }

    /**
     * Mutateur heureFin.
     * @param heureFin le heureFin à modifier.
     */
    public void setHeureFin(Integer heureFin) {
        this.heureFin = heureFin;
    }

    /**
     * Accesseur minuteFin.
     * @return le minuteFin.
     */
    public Integer getMinuteFin() {
        return minuteFin;
    }

    /**
     * Mutateur minuteFin.
     * @param minuteFin le minuteFin à modifier.
     */
    public void setMinuteFin(Integer minuteFin) {
        this.minuteFin = minuteFin;
    }

    /**
     * Accesseur description.
     * @return le description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutateur description.
     * @param description le description à modifier.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Accesseur idSequence.
     * @return le idSequence.
     */
    public Integer getIdSequence() {
        return idSequence;
    }

    /**
     * Mutateur idSequence.
     * @param idSequence le idSequence à modifier.
     */
    public void setIdSequence(Integer idSequence) {
        this.idSequence = idSequence;
    }

    /**
     * Accesseur idEnseignant.
     * @return le idEnseignant.
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur idEnseignant.
     * @param idEnseignant le idEnseignant à modifier.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur sequence.
     * @return le sequence.
     */
    public SequenceBean getSequence() {
        return sequence;
    }

    /**
     * Mutateur sequence.
     * @param sequence le sequence à modifier.
     */
    public void setSequence(SequenceBean sequence) {
        this.sequence = sequence;
    }

    /**
     * Accesseur enseignant.
     * @return le enseignant.
     */
    public EnseignantBean getEnseignant() {
        return enseignant;
    }

    /**
     * Mutateur enseignant.
     * @param enseignant le enseignant à modifier.
     */
    public void setEnseignant(EnseignantBean enseignant) {
        this.enseignant = enseignant;
    }

    /**
     * Accesseur de annotations {@link #annotations}.
     * @return retourne annotations
     */
    public String getAnnotations() {
        return annotations;
    }

    /**
     * Mutateur de annotations {@link #annotations}.
     * @param annotations le annotations to set
     */
    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }



    /**
     * @return the visa
     */
    public VisaBean getVisa() {
        return visa;
    }



    /**
     * @param visa the visa to set
     */
    public void setVisa(VisaBean visa) {
        this.visa = visa;
    }







    /**
     * @return the idSeance
     */
    public Integer getIdSeance() {
        return idSeance;
    }




    /**
     * @param idSeance the idSeance to set
     */
    public void setIdSeance(Integer idSeance) {
        this.idSeance = idSeance;
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
