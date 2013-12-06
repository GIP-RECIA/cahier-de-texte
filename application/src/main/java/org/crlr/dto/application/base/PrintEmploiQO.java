/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.util.Date;
import java.util.List;

import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.web.dto.EmploiJoursDTO;

/**
 * Un QO pour l'affichage de l'emploi du temps à imprimer.
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class PrintEmploiQO {
    
    
    /**
     * Le DTO de l'utilisateur courant.
     */
    private UtilisateurDTO utilisateurDTO;

    /**
     * Les jours à afficher.
     */
    private List<TypeJour> jourOuvreAccessible;

    /**
     * La liste des horaires avec les cours prévus.
     */
    private List<EmploiJoursDTO> emploiJourDTO;
    
    
    
    private Integer heureDebut;
    
    private Integer heureFin;
    
    /**
     * La liste des horaires avec les cours prévus.
     */
    private List<DetailJourEmploiDTO> listeEmploiDeTemps;

    /**
     * Le type d'édition de l'emploi du temps.
     */
    private TypeEditionEmploiTemps typeEditionEmploiTemps;
    
    private TypeGroupe typeGroupeSelectionne;
    private GroupesClassesDTO groupeClasseDTO;
    private List<GroupeDTO> listeGroupe;
    
    private Boolean typeSemaine;
    
    //uniquement pour consolidation
    private Date dateDebut;
    private Date dateFin;
    
    /**
     * @author G-CG34-FRMP
     *
     */
    public enum EcranOrigin {
        CONSOLIDATION,
        CONSTITUER
    } ;
    
    EcranOrigin ecranOrigin;
    
/**
     * Constructeur de PrintEmploiQO.
     */
    public PrintEmploiQO() {
    }

    /**
     * Accesseur de utilisateurDTO.
     *
     * @return le utilisateurDTO
     */
    public UtilisateurDTO getUtilisateurDTO() {
        return utilisateurDTO;
    }

    /**
     * Mutateur de utilisateurDTO.
     *
     * @param utilisateurDTO le utilisateurDTO à modifier.
     */
    public void setUtilisateurDTO(UtilisateurDTO utilisateurDTO) {
        this.utilisateurDTO = utilisateurDTO;
    }

 

    /**
     * Accesseur de emploiJourDTO.
     *
     * @return le emploiJourDTO
     */
    public List<EmploiJoursDTO> getEmploiJourDTO() {
        return emploiJourDTO;
    }

    /**
     * Mutateur de emploiJourDTO.
     *
     * @param emploiJourDTO le emploiJourDTO à modifier.
     */
    public void setEmploiJourDTO(List<EmploiJoursDTO> emploiJourDTO) {
        this.emploiJourDTO = emploiJourDTO;
    }



    /**
     * Accesseur de typeEditionEmploiTemps.
     * @return le typeEditionEmploiTemps
     */
    public TypeEditionEmploiTemps getTypeEditionEmploiTemps() {
        return typeEditionEmploiTemps;
    }

    /**
     * Mutateur de typeEditionEmploiTemps.
     * @param typeEditionEmploiTemps le typeEditionEmploiTemps à modifier.
     */
    public void setTypeEditionEmploiTemps(
            TypeEditionEmploiTemps typeEditionEmploiTemps) {
        this.typeEditionEmploiTemps = typeEditionEmploiTemps;
    }

  


    /**
     * Accesseur de groupeClasseDTO.
     * @return le groupeClasseDTO
     */
    public GroupesClassesDTO getGroupeClasseDTO() {
        return groupeClasseDTO;
    }

    /**
     * Mutateur de groupeClasseDTO.
     * @param groupeClasseDTO le groupeClasseDTO à modifier.
     */
    public void setGroupeClasseDTO(GroupesClassesDTO groupeClasseDTO) {
        this.groupeClasseDTO = groupeClasseDTO;
    }

    /**
     * Accesseur de listeGroupe.
     * @return le listeGroupe
     */
    public List<GroupeDTO> getListeGroupe() {
        return listeGroupe;
    }

    /**
     * Mutateur de listeGroupe.
     * @param listeGroupe le listeGroupe à modifier.
     */
    public void setListeGroupe(List<GroupeDTO> listeGroupe) {
        this.listeGroupe = listeGroupe;
    }

    /**
     * Mutateur de typeSemaine.
     * @param typeSemaine le typeSemaine à modifier.
     */
    public void setTypeSemaine(Boolean typeSemaine) {
        this.typeSemaine = typeSemaine;
    }

    /**
     * Accesseur de typeSemaine.
     * @return le typeSemaine
     */
    public Boolean getTypeSemaine() {
        return typeSemaine;
    }

	/**
	 * @return the listeEmploiDeTemps
	 */
	public List<DetailJourEmploiDTO> getListeEmploiDeTemps() {
		return listeEmploiDeTemps;
	}

	/**
	 * @param listeEmploiDeTemps the listeEmploiDeTemps to set
	 */
	public void setListeEmploiDeTemps(List<DetailJourEmploiDTO> listeEmploiDeTemps) {
		this.listeEmploiDeTemps = listeEmploiDeTemps;
	}

	/**
	 * @return the heureDebut
	 */
	public Integer getHeureDebut() {
		return heureDebut;
	}

	/**
	 * @param heureDebut the heureDebut to set
	 */
	public void setHeureDebut(Integer heureDebut) {
		this.heureDebut = heureDebut;
	}

	/**
	 * @return the heureFin
	 */
	public Integer getHeureFin() {
		return heureFin;
	}

	/**
	 * @param heureFin the heureFin to set
	 */
	public void setHeureFin(Integer heureFin) {
		this.heureFin = heureFin;
	}

	/**
	 * @return the jourOuvreAccessible
	 */
	public List<TypeJour> getJourOuvreAccessible() {
		return jourOuvreAccessible;
	}

	/**
	 * @param jourOuvreAccessible the jourOuvreAccessible to set
	 */
	public void setJourOuvreAccessible(List<TypeJour> jourOuvreAccessible) {
		this.jourOuvreAccessible = jourOuvreAccessible;
	}

    /**
     * @param typeGroupeSelectionne the typeGroupeSelectionne to set
     */
    public void setTypeGroupeSelectionne(TypeGroupe typeGroupeSelectionne) {
        this.typeGroupeSelectionne = typeGroupeSelectionne;
    }

    /**
     * @return the typeGroupeSelectionne
     */
    public TypeGroupe getTypeGroupeSelectionne() {
        return typeGroupeSelectionne;
    }

    /**
     * @return the dateDebut
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * @param dateDebut the dateDebut to set
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @return the dateFin
     */
    public Date getDateFin() {
        return dateFin;
    }

    /**
     * @param dateFin the dateFin to set
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @return the ecranOrigin
     */
    public EcranOrigin getEcranOrigin() {
        return ecranOrigin;
    }

    /**
     * @param ecranOrigin the ecranOrigin to set
     */
    public void setEcranOrigin(EcranOrigin ecranOrigin) {
        this.ecranOrigin = ecranOrigin;
    }

    
    
    
}
