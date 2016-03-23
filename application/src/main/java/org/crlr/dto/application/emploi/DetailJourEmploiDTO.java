/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DetailJourEmploiDTO.java,v 1.9 2010/04/29 09:17:42 jerome.carriere Exp $
 */

package org.crlr.dto.application.emploi;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.utils.DateUtils;
import org.crlr.web.dto.TypeCouleur;
import org.crlr.web.dto.TypeSemaine;



/**
 * DetailJourEmploiDTO.
 * 
 * Corresponde à un emploi de temps ; table : cahier_emploi
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
public class DetailJourEmploiDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = 357002530568002370L;   
    
    /** id de la ligne. */
    private Integer id;
    
    /** Date du jour. */
    private Date date;

    private Integer idPeriodeEmploi;
    
    /**Identifiant de l'enseignant. */
    private Integer idEnseignant;
    
    /**Identifiant de l'établissement. */
    private Integer idEtablissement;
    
    /** Identifiant de la séance. */
    private Integer idSeance;
        
    /** Code de la classe. */
    private GroupesClassesDTO groupeOuClasse;

    /** Désignation de l'enseignement du jour. */
    private EnseignementDTO matiere;     
    
    private String descriptionCourte;
    
    private String description;
    
    
    private Integer heureDebut;    
    private Integer heureFin;
    
    private Integer minuteDebut;    
    private Integer minuteFin;
    
    /** Jours correspondant. */
    private TypeJour jour;

    /** Jours vaqués. **/
    private Boolean vraiOuFauxVaque;
    
    /** Type de semaine. **/
    private TypeSemaine typeSemaine;
   
    /** le code de la séquence. */
    private String codeSequence;
    
    /** utiliser uniquement dans la consolidation la civ, nom , prénom de l'enseignant. */
    private String civiliteNomPrenom;
    
    /** utiliser uniquement dans la consolidation. */
    private String matiereCourt;
    
    /** Permet de savoir si le DTO permet d'initialiser lemploi du temp. **/
    private Boolean vraiOuFauxInitialisationEmp;
    
    /** Libelle utilisé en premiere ligne dans les pdf : classe ou groupe pour un enseignant, nom de l'enseignant pour une classe ou un groupe. */
    private String libellePremiereLigne;
    
    
    
    /** Indique s'il y a plusieurs séances enregistrées sur la plage. */ 
    private Boolean vraiOuFauxEnConflit;
    
    /** Affiche qu'une cellule est en cours de modification par la valeur *.
     * 
     */
    private String chaineExistModif;
    
    /** La couleur de la cellule. */
    private TypeCouleur typeCouleur = TypeCouleur.Gris;
    
 
    
    /** Code de la salle. */
    private String codeSalle;
    
    /**
     * 
     * Constructeur.
     */
    public DetailJourEmploiDTO() {
        vraiOuFauxInitialisationEmp = false;
        chaineExistModif = "";
        vraiOuFauxEnConflit = false;
        
        groupeOuClasse = new GroupesClassesDTO();
        matiere = new EnseignementDTO();
        
    }
    
  
    

    /**
     * Accesseur vraiOuFauxInitialisationEmp.
     * @return le vraiOuFauxInitialisationEmp
     */
    public Boolean getVraiOuFauxInitialisationEmp() {
        return vraiOuFauxInitialisationEmp;
    }

    /**
     * Mutateur de vraiOuFauxInitialisationEmp.
     * @param vraiOuFauxInitialisationEmp le vraiOuFauxInitialisationEmp à modifier.
     */
    public void setVraiOuFauxInitialisationEmp(Boolean vraiOuFauxInitialisationEmp) {
        this.vraiOuFauxInitialisationEmp = vraiOuFauxInitialisationEmp;
    }
    
    /**
     * 
    DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public Date getDate() {
        return date;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param date DOCUMENTATION INCOMPLETE!
     */
    public void setDate(Date date) {
        this.date = date;
    }


    /**
     * Accesseur descriptionCourte.
     * @return le descriptionCourte
     */
    public String getDescriptionCourte() {
        return descriptionCourte;
    }

    /**
     * Mutateur de descriptionCourte.
     * @param descriptionCourte le descriptionCourte à modifier.
     */
    public void setDescriptionCourte(String descriptionCourte) {
        this.descriptionCourte = descriptionCourte;
    }

    /**
     * Accesseur descriptionLongue.
     * @return le descriptionLongue
     */
    public String getDescription() {
        return description;
    }
    
   
    /**
     * @return le tooltip pour une case d'emploi de temps.
     */
    public String getCaseDescription() {
        StringBuilder sb = new StringBuilder();
        if (this.getGroupeOuClasse() != null
                && StringUtils.trimToNull(getGroupeOuClasse().getIntitule()) != null) {
            sb.append(getGroupeOuClasse().getIntitule());
            sb.append("\n");
        }

        if (StringUtils.trimToNull(getMatiere().getLibellePerso()) != null) {
            sb.append(getMatiere().getLibellePerso());
        } else {
            sb.append(getMatiere().getIntitule());
        }
        sb.append("\n");

        if (StringUtils.trimToNull(getCodeSalle()) != null) {
            sb.append("\nSalle ");
            sb.append(getCodeSalle());
            sb.append("\n");
        }

        if (StringUtils.trimToNull(getDescription()) != null) {
            sb.append(getDescription());
            sb.append("\n");
        }

        return sb.toString();
    }
    
    /**
     * @return description pour les éditions.
     */
    public String getPrintDescription() {
    	StringBuilder sb = new StringBuilder();
    	
    	if (heureDebut != null && heureFin != null && minuteFin != null && minuteDebut != null) {
    	
	    	sb.append( DateUtils.formatTime(heureDebut, minuteDebut ) );
	    	sb.append(" - ");
	    	sb.append( DateUtils.formatTime(heureFin, minuteFin ) );
    	}
    	
    	final int maxWidth = 25;
    	
    	if (StringUtils.trimToNull(getCiviliteNomPrenom()) != null) {
    	    sb.append("<br/>"); 
            sb.append(StringUtils.abbreviate(this.getCiviliteNomPrenom(), maxWidth));
    	}
    	
    	if (StringUtils.trimToNull(this.getGroupeOuClasse().getIntitule()) != null) {
    	    sb.append("<br/>");
    	    sb.append(StringUtils.abbreviate(this.getGroupeOuClasse().getIntitule(), maxWidth));
    	}
    	
    	
    	sb.append("<br/>");
    	if (StringUtils.trimToNull(getMatiere().getLibellePerso()) == null) {
    	    sb.append(StringUtils.abbreviate(this.getMatiere().getIntitule(), maxWidth));
    	} else {
    	    sb.append(StringUtils.abbreviate(this.getMatiere().getLibellePerso(), maxWidth));
    	}
    	
    	if (StringUtils.trimToNull(this.getDescription()) != null) {
    		sb.append("<br/>");
    		sb.append(StringUtils.abbreviate(this.getDescription(), maxWidth));
    	}
    	
    	if (StringUtils.trimToNull(this.getCodeSalle()) != null) {
    	    sb.append("<br/>");
    	    final String salle = "Salle : "; 
            sb.append(salle);
            sb.append(StringUtils.abbreviate(this.getCodeSalle(), maxWidth - salle.length()));
    	}
    	
    	
    	return sb.toString();
    }

    /**
     * Mutateur de descriptionLongue.
     * @param descriptionLongue le descriptionLongue à modifier.
     */
    public void setDescription(String descriptionLongue) {
        this.description = descriptionLongue;
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
     * Accesseur idClasse.
     * @return le idClasse
     */
    public Integer getIdClasse() {
        if (null == this.getGroupeOuClasse()) {
            return null;
        }
        
    	if ( Boolean.TRUE.equals(this.getGroupeOuClasse().getVraiOuFauxClasse())) {
    		return getGroupeOuClasse().getId(); 
    	} else {
    		return null;
    	} 
    }

    /**
     * Mutateur de idClasse.
     * @param idClasse le idClasse à modifier.
     */
    public void setIdClasse(Integer idClasse) {
    	setIdClasseOuGroup(idClasse, true);
    	
    }
    
    /**
     * Sélon l'état, affecte l'id dans groupeOuClasse.
     * @param idClasseOuGroupe l'id de BDD
     * @param isClass vrai si une classe, faux si une groupe
     */
    private void setIdClasseOuGroup(Integer idClasseOuGroupe, boolean isClass) {
        if (idClasseOuGroupe != null) {
            this.groupeOuClasse = new GroupesClassesDTO();
            groupeOuClasse.setId(idClasseOuGroupe);
            groupeOuClasse.setTypeGroupe(isClass ? TypeGroupe.CLASSE
                    : TypeGroupe.GROUPE);
        } else if (idClasseOuGroupe == null
                && ObjectUtils.equals(isClass, this.getGroupeOuClasse()
                        .getVraiOuFauxClasse())) {
            // L'id est null est la configuration actuel correspond à l'id
            this.groupeOuClasse = new GroupesClassesDTO();
        } 
        // else Nous avons une groupe, alors fais rien
        

    }

    /**
     * Accesseur idEnseignement.
     * @return le idEnseignement
     */
    public Integer getIdEnseignement() {
        return getMatiere().getId();
    }

    /**
     * Mutateur de idEnseignement.
     * @param idEnseignement le idEnseignement à modifier.
     */
    public void setIdEnseignement(Integer idEnseignement) {
        this.getMatiere().setId(idEnseignement);
    }

    /**
     * Accesseur idGroupe.
     * @return le idGroupe
     */
    public Integer getIdGroupe() {
        if (this.getGroupeOuClasse() == null) {
            return null;
        }
        
    	if ( Boolean.FALSE.equals(this.getGroupeOuClasse().getVraiOuFauxClasse())) {
    		return getGroupeOuClasse().getId(); 
    	} else {
    		return null;
    	} 
    }

    /**
     * Mutateur de idGroupe.
     * @param idGroupe le idGroupe à modifier.
     */
    public void setIdGroupe(Integer idGroupe) {
    	setIdClasseOuGroup(idGroupe, false);        
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
     * Accesseur typeSemaine.
     * @return le typeSemaine
     */
    public TypeSemaine getTypeSemaine() {
        return typeSemaine;
    }

    /**
     * Mutateur de typeSemaine.
     * @param typeSemaine le typeSemaine à modifier.
     */
    public void setTypeSemaine(TypeSemaine typeSemaine) {
        this.typeSemaine = typeSemaine;
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
     * @return le jour en chaîne
     */
    public String getJourAsString() {
        return jour.toString();
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
     * Accesseur vraiOuFauxVaque.
     * @return le vraiOuFauxVaque
     */
    public Boolean getVraiOuFauxVaque() {
        return vraiOuFauxVaque;
    }

    /**
     * Mutateur de vraiOuFauxVaque.
     * @param vraiOuFauxVaque le vraiOuFauxVaque à modifier.
     */
    public void setVraiOuFauxVaque(Boolean vraiOuFauxVaque) {
        this.vraiOuFauxVaque = vraiOuFauxVaque;
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
     * Accesseur civiliteNomPrenom.
     * @return le civiliteNomPrenom
     */
    public String getCiviliteNomPrenom() {
        return civiliteNomPrenom;
    }

    /**
     * Mutateur de civiliteNomPrenom.
     * @param civiliteNomPrenom le civiliteNomPrenom à modifier.
     */
    public void setCiviliteNomPrenom(String civiliteNomPrenom) {
        this.civiliteNomPrenom = civiliteNomPrenom;
    }

    /**
     * Accesseur matiereCourt.
     * @return le matiereCourt
     */
    public String getMatiereCourt() {
        return matiereCourt;
    }

    /**
     * Mutateur de matiereCourt.
     * @param matiereCourt le matiereCourt à modifier.
     */
    public void setMatiereCourt(String matiereCourt) {
        this.matiereCourt = matiereCourt;
    }

    /**
     * Accesseur de libellePremiereLigne.
     * @return le libellePremiereLigne
     */
    public String getLibellePremiereLigne() {
        return libellePremiereLigne;
    }

    /**
     * Mutateur de libellePremiereLigne.
     * @param libellePremiereLigne le libellePremiereLigne à modifier.
     */
    public void setLibellePremiereLigne(String libellePremiereLigne) {
        this.libellePremiereLigne = libellePremiereLigne;
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
     * Accesseur chaineExistModif.
     * @return le chaineExistModif.
     */
    public String getChaineExistModif() {
        return chaineExistModif;
    }

    /**
     * Mutateur chaineExistModif.
     * @param chaineExistModif le chaineExistModif à modifier.
     */
    public void setChaineExistModif(String chaineExistModif) {
        this.chaineExistModif = chaineExistModif;
    }

    /**
     * Accesseur typeCouleur.
     * @return le typeCouleur.
     */
    public TypeCouleur getTypeCouleur() {
        return typeCouleur;
    }

    /**
     * Mutateur typeCouleur.
     * @param typeCouleur le typeCouleur à modifier.
     */
    public void setTypeCouleur(TypeCouleur typeCouleur) {
        if (typeCouleur != null){
            this.typeCouleur = typeCouleur;
        }
    }
    

    /**
     * Accesseur de vraiOuFauxEnConflit {@link #vraiOuFauxEnConflit}.
     * @return retourne vraiOuFauxEnConflit 
     */
    public Boolean getVraiOuFauxEnConflit() {
        return vraiOuFauxEnConflit;
    }

    /**
     * Mutateur de vraiOuFauxEnConflit {@link #vraiOuFauxEnConflit}.
     * @param vraiOuFauxEnConflit the vraiOuFauxEnConflit to set
     */
    public void setVraiOuFauxEnConflit(Boolean vraiOuFauxEnConflit) {
        this.vraiOuFauxEnConflit = vraiOuFauxEnConflit;
    }

    /**
     * Accesseur de codeSalle {@link #codeSalle}.
     * @return retourne codeSalle 
     */
    public String getCodeSalle() {
        return codeSalle;
    }

    /**
     * Mutateur de codeSalle {@link #codeSalle}.
     * @param codeSalle the codeSalle to set
     */
    public void setCodeSalle(String codeSalle) {
        this.codeSalle = codeSalle;
    }

	

	/**
	 * @return the matiere
	 */
	public EnseignementDTO getMatiere() {
		return matiere;
	}

	/**
	 * @param matiere the matiere to set
	 */
	public void setMatiere(EnseignementDTO matiere) {
		this.matiere = matiere;
	}

	/**
	 * @return the groupeOuClasse
	 */
	public GroupesClassesDTO getGroupeOuClasse() {
		return groupeOuClasse;
	}

	/**
	 * @param groupeOuClasse the groupeOuClasse to set
	 */
	public void setGroupeOuClasse(GroupesClassesDTO groupeOuClasse) {
		this.groupeOuClasse = groupeOuClasse;
	}




	/**
	 * @return the idPeriodeEmploi
	 */
	public Integer getIdPeriodeEmploi() {
		return idPeriodeEmploi;
	}




	/**
	 * @param idPeriodeEmploi the idPeriodeEmploi to set
	 */
	public void setIdPeriodeEmploi(Integer idPeriodeEmploi) {
		this.idPeriodeEmploi = idPeriodeEmploi;
	}
   
}
