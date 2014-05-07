/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EmploiSequenceBean.java,v 1.2 2010/04/12 04:30:13 jerome.carriere Exp $
 */

package org.crlr.metier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.crlr.dto.application.base.TypeJour;
import org.hibernate.annotations.Proxy;

/**
 * EmploiBean.
 *
 * @author breytond
 * @version $Revision: 1.2 $
 */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_emploi", schema="cahier_courant")
public class EmploiBean {
    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private Integer id;

    /** Description de l'enregistrement. */
    @Column(name = "description", nullable = true)
    private String description;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "type_semaine", nullable = false)    
    private Character typeSemaine;

    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "jour", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeJour jour;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "heure_debut", nullable = false)
    private Integer heureDebut;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "heure_fin", nullable = false)
    private Integer heureFin;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "minute_debut", nullable = false)
    private Integer minuteDebut;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "minute_fin", nullable = false)
    private Integer minuteFin;
    
    @Column(name = "code_salle", nullable = true)
    private String codeSalle;

    
    
    /** Identifiant de la periode correspondant. */
    @Column(name = "id_periode_emploi", nullable = false)
    private Integer idPeriodeEmploi;
    
    /** Foreign key vers la table cahier_periode_emploi. */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_periode_emploi", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private PeriodeEmploiBean periode;

    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "id_enseignant", nullable = false)
    private Integer idEnseignant;

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
    @Column(name = "id_classe", nullable = true)
    private Integer idClasse;

    /** DOCUMENTATION INCOMPLETE! */
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
    @Column(name = "id_groupe", nullable = true)
    private Integer idGroupe;

    /** DOCUMENTATION INCOMPLETE! */
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
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "id_etablissement", nullable = false)
    private Integer idEtablissement;

    /** DOCUMENTATION INCOMPLETE! */
	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
    @JoinColumns(
            {@JoinColumn(
                name = "id_etablissement", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private EtablissementBean etablissement;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "id_enseignement", nullable = false)
    private Integer idEnseignement;

    /** DOCUMENTATION INCOMPLETE! */
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


/**
     * Constructeur.
     */
    public EmploiBean() {
    }

    /**
     * Accesseur id.
     *
     * @return le id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur id.
     *
     * @param id le id à modifier.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur typeSemaine.
     * @return le typeSemaine
     */
    public Character getTypeSemaine() {
        return typeSemaine;
    }

    /**
     * Mutateur de typeSemaine.
     * @param typeSemaine le typeSemaine à modifier.
     */
    public void setTypeSemaine(Character typeSemaine) {
        this.typeSemaine = typeSemaine;
    }

    /**
     * Accesseur jour.
     * @return le jour
     */
    public TypeJour getJour() {
        return jour;
    }

    /**
     * Mutateur de jour.
     * @param jour le jour à modifier.
     */
    public void setJour(TypeJour jour) {
        this.jour = jour;
    }

    /**
     * Accesseur heureDebut.
     * @return le heureDebut
     */
    public Integer getHeureDebut() {
        return heureDebut;
    }

    /**
     * Mutateur de heureDebut.
     * @param heureDebut le heureDebut à modifier.
     */
    public void setHeureDebut(Integer heureDebut) {
        this.heureDebut = heureDebut;
    }

    /**
     * Accesseur heureFin.
     * @return le heureFin
     */
    public Integer getHeureFin() {
        return heureFin;
    }

    /**
     * Mutateur de heureFin.
     * @param heureFin le heureFin à modifier.
     */
    public void setHeureFin(Integer heureFin) {
        this.heureFin = heureFin;
    }

    /**
     * Accesseur minuteDebut.
     * @return le minuteDebut
     */
    public Integer getMinuteDebut() {
        return minuteDebut;
    }

    /**
     * Mutateur de minuteDebut.
     * @param minuteDebut le minuteDebut à modifier.
     */
    public void setMinuteDebut(Integer minuteDebut) {
        this.minuteDebut = minuteDebut;
    }

    /**
     * Accesseur minuteFin.
     * @return le minuteFin
     */
    public Integer getMinuteFin() {
        return minuteFin;
    }

    /**
     * Mutateur de minuteFin.
     * @param minuteFin le minuteFin à modifier.
     */
    public void setMinuteFin(Integer minuteFin) {
        this.minuteFin = minuteFin;
    }

    /**
     * Accesseur idEnseigant.
     * @return le idEnseigant
     */
    public Integer getIdEnseigant() {
        return idEnseignant;
    }

    /**
     * Accesseur enseignant.
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
     * Accesseur idClasse.
     * @return le idClasse
     */
    public Integer getIdClasse() {
        return idClasse;
    }

    /**
     * Mutateur de idClasse.
     * @param idClasse le idClasse à modifier.
     */
    public void setIdClasse(Integer idClasse) {
        this.idClasse = idClasse;
    }

    /**
     * Accesseur classe.
     * @return le classe
     */
    public ClasseBean getClasse() {
        return classe;
    }

    /**
     * Mutateur de classe.
     * @param classe le classe à modifier.
     */
    public void setClasse(ClasseBean classe) {
        this.classe = classe;
    }

    /**
     * Accesseur idGroupe.
     * @return le idGroupe
     */
    public Integer getIdGroupe() {
        return idGroupe;
    }

    /**
     * Mutateur de idGroupe.
     * @param idGroupe le idGroupe à modifier.
     */
    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    /**
     * Accesseur groupe.
     * @return le groupe
     */
    public GroupeBean getGroupe() {
        return groupe;
    }

    /**
     * Mutateur de groupe.
     * @param groupe le groupe à modifier.
     */
    public void setGroupe(GroupeBean groupe) {
        this.groupe = groupe;
    }

    /**
     * Accesseur idEnseignement.
     * @return le idEnseignement
     */
    public Integer getIdEnseignement() {
        return idEnseignement;
    }

    /**
     * Mutateur de idEnseignement.
     * @param idEnseignement le idEnseignement à modifier.
     */
    public void setIdEnseignement(Integer idEnseignement) {
        this.idEnseignement = idEnseignement;
    }

    /**
     * Accesseur enseignement.
     * @return le enseignement
     */
    public EnseignementBean getEnseignement() {
        return enseignement;
    }

    /**
     * Mutateur de enseignement.
     * @param enseignement le enseignement à modifier.
     */
    public void setEnseignement(EnseignementBean enseignement) {
        this.enseignement = enseignement;
    }

    /**
     * Accesseur idEtablissement.
     * @return le idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur de idEtablissement.
     * @param idEtablissement le idEtablissement à modifier.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur etablissement.
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
     * Accesseur description.
     * @return le description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutateur de description.
     * @param description le description à modifier.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Accesseur idEnseignant.
     * @return le idEnseignant
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur de idEnseignant.
     * @param idEnseignant le idEnseignant à modifier.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }   

    /**
     * Accesseur de codeSalle.
     * @return le codeSalle
     */
    public String getCodeSalle() {
        return codeSalle;
    }

    /**
     * Modificateur de codeSalle.
     * @param codeSalle le codeSalle à modifier
     */
    public void setCodeSalle(String codeSalle) {
        this.codeSalle = codeSalle;
    }

  
    /**
     * Accesseur de idPeriodeEmploi {@link #idPeriodeEmploi}.
     * @return retourne idPeriodeEmploi
     */
    public Integer getIdPeriodeEmploi() {
        return idPeriodeEmploi;
    }

    /**
     * Mutateur de idPeriodeEmploi {@link #idPeriodeEmploi}.
     * @param idPeriodeEmploi le idPeriodeEmploi to set
     */
    public void setIdPeriodeEmploi(Integer idPeriodeEmploi) {
        this.idPeriodeEmploi = idPeriodeEmploi;
    }

    /**
     * Accesseur de periode {@link #periode}.
     * @return retourne periode
     */
    public PeriodeEmploiBean getPeriode() {
        return periode;
    }

    /**
     * Mutateur de periode {@link #periode}.
     * @param periode le periode to set
     */
    public void setPeriode(PeriodeEmploiBean periode) {
        this.periode = periode;
    }  
    
}