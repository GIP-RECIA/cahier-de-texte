/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheSeanceQO.java,v 1.14 2010/04/28 16:12:21 jerome.carriere Exp $
 */

package org.crlr.dto.application.seance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.metier.entity.ClasseBean;
import org.crlr.metier.entity.GroupeBean;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.14 $
 */
public class RechercheSeanceQO implements Serializable {
    /**  */
    private static final long serialVersionUID = -7677696083783396037L;

    /** DOCUMENTATION INCOMPLETE! */
    private Date jourCourant;
    
    /** DOCUMENTATION INCOMPLETE! */
    private Date premierJourSemaine;
    
    /** DOCUMENTATION INCOMPLETE! */
    private Date dernierJourSemaine;
    
    /** id de l'établissement. */
    private Integer idEtablissement;
    
    /** id de la sénance. */
    private Integer idSeance;

    /** Id de l'eleve. */
    private Integer idEleve;
    
    /** Id de l'enseignant. */
    private Integer idEnseignant;
    
    /** Id de l'inspecteur. */
    private Integer idInspecteur;
    
    /** Id de l'enseignement. */
    private Integer idEnseignement;
    
    /** Code de l'enseignement. */
    private String codeEnseignement;

    /** Id de la séquence. */
    private Integer idSequence;

    /** Code de la séquence. */
    private String codeSequence;
    
    /** Date de début de la séance. */
    private Date dateDebut;

    /** Date de fin de la séance. */
    private Date dateFin;

    /** Date de début. */
    private Integer heureDebut;
    
    /** Date de début. */
    private Integer minuteDebut;

    /** Date de fin. */
    private Integer heureFin;


    
    
    /** groupe ou classe sélectionnée dans la popup. */
    private GroupesClassesDTO groupeClasseSelectionne;
    
    
    
    /** liste des groupes d'un eleve. */
    private List<GroupeBean> listeGroupeBean;
    
    /** liste des classes d'un eleve. */
    private List<ClasseBean> listeClasseBean;
    
    /**
     * Liste des groupes.
     */
    private List<GroupeDTO> listeGroupeDTO;
    
    /**
     * Id de l'année scolaire.
     */
    private Integer idAnneeScolaire;
    
    /**
     * L'exercice de l'année scolaire.
     */
    private String exerciceAnneeScolaire; 
    
    /**
     * Pour savoir si on est en archivage ou pas.
     */
    private Boolean archive;

    private TypeReglesSeance typeReglesSeance;
    
   
    
    /** Utile dans le cas de l'override .*/
    private String uid;
    
    
    /** Indique si l'etablissment pratique l'alternance de semaine ou non.  */
    private Boolean vraiOuFauxAlternanceSemaine;
    
    /**
     * Constructeur.
     */
    public RechercheSeanceQO() {
        typeReglesSeance = TypeReglesSeance.SEANCE_16;
        groupeClasseSelectionne = new GroupesClassesDTO();
    }

    /**
     * Accesseur typeReglesSeance.
     * @return le typeReglesSeance
     */
    public TypeReglesSeance getTypeReglesSeance() {
        return typeReglesSeance;
    }

    /**
     * Mutateur de typeReglesSeance.
     * @param typeReglesSeance le typeReglesSeance à modifier.
     */
    public void setTypeReglesSeance(TypeReglesSeance typeReglesSeance) {
        this.typeReglesSeance = typeReglesSeance;
    }

    /**
     * Accesseur jourCourant.
     * @return le jourCourant
     */
    public Date getJourCourant() {
        return jourCourant;
    }

    /**
     * Mutateur de jourCourant.
     * @param jourCourant le jourCourant à modifier.
     */
    public void setJourCourant(Date jourCourant) {
        this.jourCourant = jourCourant;
    }

    /**
     * Accesseur premierJourSemaine.
     * @return le premierJourSemaine
     */
    public Date getPremierJourSemaine() {
        return premierJourSemaine;
    }

    /**
     * Mutateur de premierJourSemaine.
     * @param premierJourSemaine le premierJourSemaine à modifier.
     */
    public void setPremierJourSemaine(Date premierJourSemaine) {
        this.premierJourSemaine = premierJourSemaine;
    }

    /**
     * Accesseur dernierJourSemaine.
     * @return le dernierJourSemaine
     */
    public Date getDernierJourSemaine() {
        return dernierJourSemaine;
    }

    /**
     * Mutateur de dernierJourSemaine.
     * @param dernierJourSemaine le dernierJourSemaine à modifier.
     */
    public void setDernierJourSemaine(Date dernierJourSemaine) {
        this.dernierJourSemaine = dernierJourSemaine;
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
     * Accesseur idSeance.
     * @return le idSeance
     */
    public Integer getIdSeance() {
        return idSeance;
    }

    /**
     * Mutateur de idSeance.
     * @param idSeance le idSeance à modifier.
     */
    public void setIdSeance(Integer idSeance) {
        this.idSeance = idSeance;
    }

    /**
     * Accesseur idEleve.
     * @return le idEleve
     */
    public Integer getIdEleve() {
        return idEleve;
    }

    /**
     * Mutateur de idEleve.
     * @param idEleve le idEleve à modifier.
     */
    public void setIdEleve(Integer idEleve) {
        this.idEleve = idEleve;
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
     * Accesseur codeEnseignement.
     * @return le codeEnseignement
     */
    public String getCodeEnseignement() {
        return codeEnseignement;
    }

    /**
     * Mutateur de codeEnseignement.
     * @param codeEnseignement le codeEnseignement à modifier.
     */
    public void setCodeEnseignement(String codeEnseignement) {
        this.codeEnseignement = codeEnseignement;
    }

    /**
     * Accesseur idSequence.
     * @return le idSequence
     */
    public Integer getIdSequence() {
        return idSequence;
    }

    /**
     * Mutateur de idSequence.
     * @param idSequence le idSequence à modifier.
     */
    public void setIdSequence(Integer idSequence) {
        this.idSequence = idSequence;
    }

    /**
     * Accesseur codeSequence.
     * @return le codeSequence
     */
    public String getCodeSequence() {
        return codeSequence;
    }

    /**
     * Mutateur de codeSequence.
     * @param codeSequence le codeSequence à modifier.
     */
    public void setCodeSequence(String codeSequence) {
        this.codeSequence = codeSequence;
    }

    /**
     * Accesseur dateDebut.
     * @return le dateDebut
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * Mutateur de dateDebut.
     * @param dateDebut le dateDebut à modifier.
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Accesseur dateFin.
     * @return le dateFin
     */
    public Date getDateFin() {
        return dateFin;
    }

    /**
     * Mutateur de dateFin.
     * @param dateFin le dateFin à modifier.
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
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
     * Accesseur typeGroupe.
     * @return le typeGroupe
     */
    public TypeGroupe getTypeGroupe() {
        return groupeClasseSelectionne == null ? null : groupeClasseSelectionne.getTypeGroupe();
    }

    /**
     * Mutateur de typeGroupe.
     * @param typeGroupe le typeGroupe à modifier.
     */
    public void setTypeGroupe(TypeGroupe typeGroupe) {
        if (groupeClasseSelectionne == null) {
            groupeClasseSelectionne = new GroupesClassesDTO();
        }
        groupeClasseSelectionne.setTypeGroupe(typeGroupe);
    }

    /**
     * Accesseur idClasseGroupe.
     * @return le idClasseGroupe
     */
    public Integer getIdClasseGroupe() {
        return groupeClasseSelectionne == null ? null : groupeClasseSelectionne.getId();
    }

    /**
     * Mutateur de idClasseGroupe.
     * @param idClasseGroupe le idClasseGroupe à modifier.
     */
    public void setIdClasseGroupe(Integer idClasseGroupe) {
        if (groupeClasseSelectionne == null) {
            groupeClasseSelectionne = new GroupesClassesDTO();
        }
        groupeClasseSelectionne.setId(idClasseGroupe);
    }

    /**
     * Accesseur codeClasseGroupe.
     * @return le codeClasseGroupe
     */
    public String getCodeClasseGroupe() {
        return groupeClasseSelectionne == null ? null : groupeClasseSelectionne.getCode();
    }

    /**
     * Mutateur de codeClasseGroupe.
     * @param codeClasseGroupe le codeClasseGroupe à modifier.
     */
    public void setCodeClasseGroupe(String codeClasseGroupe) {
        if (groupeClasseSelectionne == null) {
            groupeClasseSelectionne = new GroupesClassesDTO();
        }
        groupeClasseSelectionne.setCode(codeClasseGroupe);        
    }

    /**
     * Accesseur groupeClasseSelectionne.
     * @return le groupeClasseSelectionne
     */
    public GroupesClassesDTO getGroupeClasseSelectionne() {
        return groupeClasseSelectionne;
    }

    /**
     * Mutateur de groupeClasseSelectionne.
     * @param groupeClasseSelectionne le groupeClasseSelectionne à modifier.
     */
    public void setGroupeClasseSelectionne(GroupesClassesDTO groupeClasseSelectionne) {
        this.groupeClasseSelectionne = groupeClasseSelectionne;
    }

   

    /**
     * Accesseur listeGroupeBean.
     * @return le listeGroupeBean
     */
    public List<GroupeBean> getListeGroupeBean() {
        return listeGroupeBean;
    }

    /**
     * Mutateur de listeGroupeBean.
     * @param listeGroupeBean le listeGroupeBean à modifier.
     */
    public void setListeGroupeBean(List<GroupeBean> listeGroupeBean) {
        this.listeGroupeBean = listeGroupeBean;
    }

    /**
     * Accesseur listeClasseBean.
     * @return le listeClasseBean
     */
    public List<ClasseBean> getListeClasseBean() {
        return listeClasseBean;
    }

    /**
     * Mutateur de listeClasseBean.
     * @param listeClasseBean le listeClasseBean à modifier.
     */
    public void setListeClasseBean(List<ClasseBean> listeClasseBean) {
        this.listeClasseBean = listeClasseBean;
    }

    /**
     * Accesseur listeGroupeDTO.
     * @return le listeGroupeDTO
     */
    public List<GroupeDTO> getListeGroupeDTO() {
        return listeGroupeDTO;
    }

    /**
     * Mutateur de listeGroupeDTO.
     * @param listeGroupeDTO le listeGroupeDTO à modifier.
     */
    public void setListeGroupeDTO(List<GroupeDTO> listeGroupeDTO) {
        this.listeGroupeDTO = listeGroupeDTO;
    }

    /**
     * Accesseur idAnneeScolaire.
     * @return le idAnneeScolaire
     */
    public Integer getIdAnneeScolaire() {
        return idAnneeScolaire;
    }

    /**
     * Mutateur de idAnneeScolaire.
     * @param idAnneeScolaire le idAnneeScolaire à modifier.
     */
    public void setIdAnneeScolaire(Integer idAnneeScolaire) {
        this.idAnneeScolaire = idAnneeScolaire;
    }

    /**
     * Accesseur exerciceAnneeScolaire.
     * @return le exerciceAnneeScolaire
     */
    public String getExerciceAnneeScolaire() {
        return exerciceAnneeScolaire;
    }

    /**
     * Mutateur de exerciceAnneeScolaire.
     * @param exerciceAnneeScolaire le exerciceAnneeScolaire à modifier.
     */
    public void setExerciceAnneeScolaire(String exerciceAnneeScolaire) {
        this.exerciceAnneeScolaire = exerciceAnneeScolaire;
    }

    /**
     * Accesseur archive.
     * @return le archive
     */
    public Boolean getArchive() {
        return archive;
    }

    /**
     * Mutateur de archive.
     * @param archive le archive à modifier.
     */
    public void setArchive(Boolean archive) {
        this.archive = archive;
    }



  

    /**
     * Accesseur uid.
     * @return le uid.
     */
    public String getUid() {
        return uid;
    }

    /**
     * Mutateur uid.
     * @param uid le uid à modifier.
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Accesseur de idInspecteur.
     * @return le idInspecteur
     */
    public Integer getIdInspecteur() {
        return idInspecteur;
    }

    /**
     * Mutateur de idInspecteur.
     * @param idInspecteur le idInspecteur à modifier.
     */
    public void setIdInspecteur(Integer idInspecteur) {
        this.idInspecteur = idInspecteur;
    }

    /**
     * Accesseur vraiOuFauxAlternanceSemaine.
     * @return vraiOuFauxAlternanceSemaine
     */
    public Boolean getVraiOuFauxAlternanceSemaine() {
        return vraiOuFauxAlternanceSemaine;
    }

    /** Mutateur vraiOuFauxAlternanceSemaine.
     * @param vraiOuFauxAlternanceSemaine vraiOuFauxAlternanceSemaine
     */
    public void setVraiOuFauxAlternanceSemaine(Boolean vraiOuFauxAlternanceSemaine) {
        this.vraiOuFauxAlternanceSemaine = vraiOuFauxAlternanceSemaine;
    }

    /**
     * Accesseur de minuteDebut.
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
     * @return the typeGroupeSelectionne
     */
    public TypeGroupe getTypeGroupeSelectionne() {
        return groupeClasseSelectionne == null ? null : groupeClasseSelectionne.getTypeGroupe();
    }

    /**
     * @param typeGroupeSelectionne the typeGroupeSelectionne to set
     */
    public void setTypeGroupeSelectionne(TypeGroupe typeGroupeSelectionne) {
        if (groupeClasseSelectionne == null) {
            groupeClasseSelectionne = new GroupesClassesDTO();
        }
        groupeClasseSelectionne.setTypeGroupe(typeGroupeSelectionne);
    }
    
    
}
