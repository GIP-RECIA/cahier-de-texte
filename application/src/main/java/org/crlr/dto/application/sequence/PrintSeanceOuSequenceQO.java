/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PrintSequenceQO.java,v 1.6 2010/04/26 07:45:49 ent_breyton Exp $
 */

package org.crlr.dto.application.sequence;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.TypeAffichage;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.6 $
  */
public class PrintSeanceOuSequenceQO implements Serializable {
    /**  */
    private static final long serialVersionUID = 648563826983661014L;

    
    private Boolean isPrintSeance;

   

	/** Date de début de la séance. */
    private Date dateDebut;

    /** Date de fin de la séance. */
    private Date dateFin;

    
    /** groupe ou classe sélectionnée dans la popup. */
    private GroupesClassesDTO groupeClasseSelectionne = new GroupesClassesDTO();

    /** Liste des groupes sélectionnés. */
    private List<GroupeDTO> listeGroupeDTO;

    /** Affichage simple ou détaillé. */
    private TypeAffichage affichage;
    
    
    /** Profil de la personne qui effectue l'édition. */
    private Profil profil;
    
    /** Id de l'établissement de la personne. */
    private Integer idEtablissement;
    
    /** Désignation de l'établissement. */
    private String designationEtablissement;
    
    /** Date du jour. */
    private Date dateCourante;
    
    /**
     * Année scolaire courante.
     */
    private AnneeScolaireDTO anneeScolaireDTO;
    
    /** Avec saut de page. */
    private Boolean vraiOuFauxSautDePage;
    
    
    /** Id de l'utilisateur qui navigue. */
    private Integer idUtilisateur;
    
    /** Id de l'enseignement séléctionné. */
    private EnseignementDTO enseignement;

    /** id de l'enseignant sélectionné. */
    private EnseignantDTO enseignant;

    
    
    /**
     * 
     */
    public PrintSeanceOuSequenceQO() {
        enseignant = new EnseignantDTO();
        enseignement = new EnseignementDTO();
        groupeClasseSelectionne = new GroupesClassesDTO();
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
        this.dateDebut = DateUtils.round(dateDebut, Calendar.DATE);
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
        this.dateFin = DateUtils.round(dateFin, Calendar.DATE);
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
     * Accesseur affichage.
     * @return le affichage
     */
    public TypeAffichage getAffichage() {
        return affichage;
    }

    /**
     * Mutateur de affichage.
     * @param affichage le affichage à modifier.
     */
    public void setAffichage(TypeAffichage affichage) {
        this.affichage = affichage;
    }

    

    /**
     * Accesseur profil.
     * @return le profil
     */
    public Profil getProfil() {
        return profil;
    }

    /**
     * Mutateur de profil.
     * @param profil le profil à modifier.
     */
    public void setProfil(Profil profil) {
        this.profil = profil;
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
     * Accesseur designationEtablissement.
     * @return le designationEtablissement
     */
    public String getDesignationEtablissement() {
        return designationEtablissement;
    }

    /**
     * Mutateur de designationEtablissement.
     * @param designationEtablissement le designationEtablissement à modifier.
     */
    public void setDesignationEtablissement(String designationEtablissement) {
        this.designationEtablissement = designationEtablissement;
    }

    /**
     * Accesseur dateCourante.
     * @return le dateCourante
     */
    public Date getDateCourante() {
        return dateCourante;
    }

    /**
     * Mutateur de dateCourante.
     * @param dateCourante le dateCourante à modifier.
     */
    public void setDateCourante(Date dateCourante) {
        this.dateCourante = dateCourante;
    }

    /**
     * Accesseur anneeScolaireDTO.
     * @return le anneeScolaireDTO
     */
    public AnneeScolaireDTO getAnneeScolaireDTO() {
        return anneeScolaireDTO;
    }

    /**
     * Mutateur de anneeScolaireDTO.
     * @param anneeScolaireDTO le anneeScolaireDTO à modifier.
     */
    public void setAnneeScolaireDTO(AnneeScolaireDTO anneeScolaireDTO) {
        this.anneeScolaireDTO = anneeScolaireDTO;
    }

    /**
     * Accesseur vraiOuFauxSautDePage.
     * @return le vraiOuFauxSautDePage
     */
    public Boolean getVraiOuFauxSautDePage() {
        return vraiOuFauxSautDePage;
    }

    /**
     * Mutateur de vraiOuFauxSautDePage.
     * @param vraiOuFauxSautDePage le vraiOuFauxSautDePage à modifier.
     */
    public void setVraiOuFauxSautDePage(Boolean vraiOuFauxSautDePage) {
        this.vraiOuFauxSautDePage = vraiOuFauxSautDePage;
    }

   

    /**
     * @return the idUtilisateur
     */
    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    /**
     * @param idUtilisateur the idUtilisateur to set
     */
    public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    /**
     * @return the enseignement
     */
    public EnseignementDTO getEnseignement() {
        return enseignement;
    }

    /**
     * @param enseignement the enseignement to set
     */
    public void setEnseignement(EnseignementDTO enseignement) {
        this.enseignement = enseignement;
    }

    /**
     * @return the enseignant
     */
    public EnseignantDTO getEnseignant() {
        return enseignant;
    }

    /**
     * @param enseignant the enseignant to set
     */
    public void setEnseignant(EnseignantDTO enseignant) {
        this.enseignant = enseignant;
    }



	public Boolean getIsPrintSeance() {
		return isPrintSeance;
	}



	public void setIsPrintSeance(Boolean isPrintSeance) {
		this.isPrintSeance = isPrintSeance;
	}   
}
