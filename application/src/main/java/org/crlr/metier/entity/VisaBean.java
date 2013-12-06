/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: VisaBean.java,v 1.2 2010/04/12 04:30:13 jerome.carriere Exp $
 */

package org.crlr.metier.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * EmploiSequenceBean.
 *
 * @author breytond
 * @version $Revision: 1.2 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_visa", schema="cahier_courant")
public class VisaBean implements Serializable {

    /**  Serial.*/
    private static final long serialVersionUID = 3954868978730003404L;

    /** Identifiant du visa.  */
    @Id
    private Integer id;
    
    /** Date de creation du visa. */
    @Column(name = "date_visa", nullable = true)
    private Date dateVisa;

    /** Profil qui a apposé le visa (correspond au porfil lu dans le LDAP). */
    @Column(name = "profil", nullable = false)
    private String profil;

    /** Type du visa : MEMO ou SIMPLE. */
    @Column(name = "type_visa", nullable = true)
    private String typeVisa;
    
    /** Identifiant etablissement. */
    @Column(name = "id_etablissement", nullable = false)
    private Integer idEtablissement;

    /** Identifiant enseignant. */
    @Column(name = "id_enseignant", nullable = false)
    private Integer idEnseignant;

    /** Identifiant enseignement. */
    @Column(name = "id_enseignement", nullable = false)
    private Integer idEnseignement;
    
    /** Identifiant classe. */
    @Column(name = "id_classe", nullable = true)
    private Integer idClasse;
    
    /** Identifiant groupe. */
    @Column(name = "id_groupe", nullable = true)
    private Integer idGroupe;
    
    /** Date de derniere modification enregistre sur le cahier de texte correspondant (classe/enseignement/enseignant). */
    @Column(name = "date_dernier_maj", nullable = true)
    private Date dateMaj;

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
    
    /** Jointure vers visaSeance. */
    @OneToMany(cascade =  {   })
    @JoinColumns(
            {
             @JoinColumn(name = "id_visa", referencedColumnName = "id", insertable = false, updatable = false)
            }
    )
    private List<VisaSeanceBean> listeVisaSeance;
    
    
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

    /** Foreign key vers cahier_enseignement. */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_enseignement", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
    )
    private EnseignementBean enseignement;

    /** Foreign key vers cahier_classe. */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_classe", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
    )
    private ClasseBean classe;

    /** Foreign key vers cahier_groupe. */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_groupe", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
    )
    private GroupeBean groupe;
    
    /**
     * Constructeur.
     */
    public VisaBean() {
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
     * Accesseur de dateVisa {@link #dateVisa}.
     * @return retourne dateVisa
     */
    public Date getDateVisa() {
        return dateVisa;
    }

    /**
     * Mutateur de dateVisa {@link #dateVisa}.
     * @param dateVisa le dateVisa to set
     */
    public void setDateVisa(Date dateVisa) {
        this.dateVisa = dateVisa;
    }

    /**
     * Accesseur de profil {@link #profil}.
     * @return retourne profil
     */
    public String getProfil() {
        return profil;
    }

    /**
     * Mutateur de profil {@link #profil}.
     * @param profil le profil to set
     */
    public void setProfil(String profil) {
        this.profil = profil;
    }

    /**
     * Accesseur de typeVisa {@link #typeVisa}.
     * @return retourne typeVisa
     */
    public String getTypeVisa() {
        return typeVisa;
    }

    /**
     * Mutateur de typeVisa {@link #typeVisa}.
     * @param typeVisa le typeVisa to set
     */
    public void setTypeVisa(String typeVisa) {
        this.typeVisa = typeVisa;
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
     * Accesseur de idEnseignement {@link #idEnseignement}.
     * @return retourne idEnseignement
     */
    public Integer getIdEnseignement() {
        return idEnseignement;
    }

    /**
     * Mutateur de idEnseignement {@link #idEnseignement}.
     * @param idEnseignement le idEnseignement to set
     */
    public void setIdEnseignement(Integer idEnseignement) {
        this.idEnseignement = idEnseignement;
    }

    /**
     * Accesseur de idClasse {@link #idClasse}.
     * @return retourne idClasse
     */
    public Integer getIdClasse() {
        return idClasse;
    }

    /**
     * Mutateur de idClasse {@link #idClasse}.
     * @param idClasse le idClasse to set
     */
    public void setIdClasse(Integer idClasse) {
        this.idClasse = idClasse;
    }

    /**
     * Accesseur de idGroupe {@link #idGroupe}.
     * @return retourne idGroupe
     */
    public Integer getIdGroupe() {
        return idGroupe;
    }

    /**
     * Mutateur de idGroupe {@link #idGroupe}.
     * @param idGroupe le idGroupe to set
     */
    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    /**
     * Accesseur de dateMaj {@link #dateMaj}.
     * @return retourne dateMaj
     */
    public Date getDateMaj() {
        return dateMaj;
    }

    /**
     * Mutateur de dateMaj {@link #dateMaj}.
     * @param dateMaj le dateMaj to set
     */
    public void setDateMaj(Date dateMaj) {
        this.dateMaj = dateMaj;
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

    /**
     * Accesseur de enseignement {@link #enseignement}.
     * @return retourne enseignement
     */
    public EnseignementBean getEnseignement() {
        return enseignement;
    }

    /**
     * Mutateur de enseignement {@link #enseignement}.
     * @param enseignement le enseignement to set
     */
    public void setEnseignement(EnseignementBean enseignement) {
        this.enseignement = enseignement;
    }

    /**
     * Accesseur de classe {@link #classe}.
     * @return retourne classe
     */
    public ClasseBean getClasse() {
        return classe;
    }

    /**
     * Mutateur de classe {@link #classe}.
     * @param classe le classe to set
     */
    public void setClasse(ClasseBean classe) {
        this.classe = classe;
    }

    /**
     * Accesseur de groupe {@link #groupe}.
     * @return retourne groupe
     */
    public GroupeBean getGroupe() {
        return groupe;
    }

    /**
     * Mutateur de groupe {@link #groupe}.
     * @param groupe le groupe to set
     */
    public void setGroupe(GroupeBean groupe) {
        this.groupe = groupe;
    }

    /**
     * Accesseur de listeVisaSeance {@link #listeVisaSeance}.
     * @return retourne listeVisaSeance
     */
    public List<VisaSeanceBean> getListeVisaSeance() {
        return listeVisaSeance;
    }

    /**
     * Mutateur de listeVisaSeance {@link #listeVisaSeance}.
     * @param listeVisaSeance le listeVisaSeance to set
     */
    public void setListeVisaSeance(List<VisaSeanceBean> listeVisaSeance) {
        this.listeVisaSeance = listeVisaSeance;
    }


    
}