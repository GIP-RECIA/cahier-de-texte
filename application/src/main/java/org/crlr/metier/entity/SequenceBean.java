/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequenceBean.java,v 1.7 2009/04/28 12:45:50 ent_breyton Exp $
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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Proxy;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author vibertd
 * @version $Revision: 1.7 $
 */
@Proxy(lazy = true)
@Entity
@Table(name = "cahier_sequence", schema="cahier_courant")
public class SequenceBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private Integer id;

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
    @Column(name = "id_enseignant", nullable = false)
    private Integer idEnseignant;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "date_debut", nullable = false)
    private Date dateDebut;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "date_fin", nullable = false)
    private Date dateFin;

    /** Identifiant de l'etablissement. */
    @Column(name = "id_etablissement", nullable = false)
    private Integer idEtablissement;
    
    /** Foreign key vers la table cahier_etablissement. */
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
    
    
    /** Identifiant de l'enseignement. */
    @Column(name = "id_enseignement", nullable = false)
    private Integer idEnseignement;
    
    /** Foreign key vers la table cahier_enseignement. */
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

    /** Libelle propre a l'etablissement . */
    @ManyToOne(cascade = {  } )
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumns( 
        {@JoinColumn( name = "id_etablissement", referencedColumnName = "id_etablissement", insertable = false, updatable = false ),
         @JoinColumn( name = "id_enseignement",  referencedColumnName = "id_enseignement", insertable = false, updatable = false )
        }
    )
    private EnseignementLibelleBean enseignementLibelle;
    

    /** Identifiant du groupe (peut etre null, exclusif avec classe). */
    @Column(name = "id_groupe", nullable = true)
    private Integer idGroupe;
    
    /** Foreign key vers la table cahier_groupe. */
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

    /** Identifiant de la classe (peut etre null, exclusif avec groupe). */
    @Column(name = "id_classe", nullable = true)
    private Integer idClasse;
    
    /** Foreign key vers la table cahier_classe. */
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

    /**
     * Constructeur.
     */
    public SequenceBean() {
    }

    /**
     * Accesseur id.
     * @return le id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur id.
     * @param id le id à modifier.
     */
    public void setId(Integer id) {
        this.id = id;
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
     * Accesseur dateDebut.
     * @return le dateDebut.
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * Mutateur dateDebut.
     * @param dateDebut le dateDebut à modifier.
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Accesseur dateFin.
     * @return le dateFin.
     */
    public Date getDateFin() {
        return dateFin;
    }

    /**
     * Mutateur dateFin.
     * @param dateFin le dateFin à modifier.
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
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
     * Accesseur de enseignementLibelle {@link #enseignementLibelle}.
     * @return retourne enseignementLibelle
     */
    public EnseignementLibelleBean getEnseignementLibelle() {
        return enseignementLibelle;
    }

    /**
     * Mutateur de enseignementLibelle {@link #enseignementLibelle}.
     * @param enseignementLibelle le enseignementLibelle to set
     */
    public void setEnseignementLibelle(EnseignementLibelleBean enseignementLibelle) {
        this.enseignementLibelle = enseignementLibelle;
    }

    
}
