/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConsulterSequenceForm.java,v 1.11 2010/06/01 07:25:41 ent_breyton Exp $
 */

package org.crlr.web.application.form.sequence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.sequence.ResultatRechercheSequenceDTO;
import org.apache.commons.lang.StringUtils;
import org.crlr.web.application.form.AbstractPopupForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.TypeCouleur;

/**
 * Classe associée a la fenêtre de consultation / modification / suppressio de séquence.
 *
 * @author $author$
 * @version $Revision: 1.11 $
 */
public class ConsulterSequenceForm extends AbstractPopupForm {
    /**  */
    private static final long serialVersionUID = 4784572425017177192L;

    /** DTO de resultat de recherche de sequence. */
    private ResultatRechercheSequenceDTO resultatRechercheSequenceDTO;

    /** Code de la sequence selectionnée. */
    private String code;

    /** Intitulé de la séquence. */
    private String intitule;

    /** Date de début. */
    private Date dateDebut;

    /** Date de fin. */
    private Date dateFin;

    /** Description. */
    private String description;

    /** Indique si la sequence est modifiable ou non. */
    private Boolean modifiable;

    /** Indique s'il y a des seances associée. */
    private Boolean seancesAssociees;

    /** Indicateur de suppression. */
    private boolean renderedSupprimer;

    /** Indicateur de duplication en cours. */
    private boolean renderedDupliquer;

    /** Indicateur de modification en cours. */
    private boolean renderedModifier;

    /** Enseignement sélectionné. */
    private EnseignementDTO enseignementSelectionne;

    /** Liste des enseignement disponible pour l'enseignant. */
    private List<EnseignementDTO> listeEnseignement;

    /** critère temporaire de selection du type de groupe. */
    private TypeGroupe typeGroupeSelectionne;

    /** groupe ou classe sélectionnée dans la popup. */
    private GroupesClassesDTO groupeClasseSelectionne;

    /** liste des groupes ou classes. */
    private List<GroupesClassesDTO> listeGroupeClasse;

    /** Id de la séquence dans le cas de modification. */
    private Integer idSequence;

    /** Titre de la page. */
    private String titreDePate;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer oldIdSequence;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer oldIdGroupeClasse;

    /** DOCUMENTATION INCOMPLETE! */
    private TypeGroupe oldTypeGroupeSelectionne;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer oldIdEnseignement;
    
    /** Filtre pour la popup classe / groupe. **/
    private String filtreClasseGroupe;

    /** Filtre pour la selection enseignement. */
    private String filtreEnseignement;
    
    /** Contrôle de l'activation de la saisie simplifiée. */
    private Boolean vraiOuFauxSaisieSimplifiee;

    /** Liste des couleur possibles dans le popup d'emploi de temps. */
    private List<TypeCouleur> listeCouleur = new ArrayList<TypeCouleur>();
 
	/** Type couleur sélectionné. */
    private TypeCouleur typeCouleur;    
/**
     * Constructeur.
     */
    public ConsulterSequenceForm() {
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        this.vraiOuFauxSaisieSimplifiee = utilisateurDTO.getVraiOuFauxEtabSaisieSimplifiee();
        reset();
    }

    /**
     * Méthode de réinitialisation des données du formulaire saisie par
     * l'utilisateur.
     */
    public void reset() {
        resultatRechercheSequenceDTO = new ResultatRechercheSequenceDTO();
        code = null;
        intitule = null;
        dateDebut = null;
        dateFin = null;
        description = null;
        modifiable = false;
        seancesAssociees = false;
        renderedModifier = false;
        renderedSupprimer = false;
        renderedDupliquer = false;
        enseignementSelectionne = new EnseignementDTO();
        listeEnseignement = new ArrayList<EnseignementDTO>();
        typeGroupeSelectionne = null;
        groupeClasseSelectionne = new GroupesClassesDTO();
        listeGroupeClasse = new ArrayList<GroupesClassesDTO>();
        oldIdSequence = null;
        oldIdGroupeClasse = null;
        oldIdEnseignement = null;
        oldTypeGroupeSelectionne = null;
        filtreClasseGroupe = "";
        listeCouleur = GenerateurDTO.generateBarreCouleur();
    }

    /**
     * Accesseur resultatRechercheSequenceDTO.
     * @return le resultatRechercheSequenceDTO.
     */
    public ResultatRechercheSequenceDTO getResultatRechercheSequenceDTO() {
        return resultatRechercheSequenceDTO;
    }
    
    /**
     * Filtre les valeurs de la popup des classes et groupes.
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public Boolean doFilterClasseGroupe(final Object value) {
        if (StringUtils.isEmpty(this.filtreClasseGroupe)) {
            return true;
        }
        final GroupesClassesDTO groupesClassesDTO = (GroupesClassesDTO) value;

        return groupesClassesDTO.getIntitule().toLowerCase()
                                .contains(this.filtreClasseGroupe.toLowerCase());
    }

    /**
     * Filtre les valeurs de la popup des enseignements. Methode appelée en itération sur 
     * chaque élement de la liste pour vérifier s'il faut ou non l'affichée. 
     * Si l'element ne matche pas (return false) l'élément n'est pas affiché.
     * @param value element de la liste de type EnseignementDTO
     * @return true si l'element value matche avec le filtre saisi
     */
    public Boolean doFilterEnseignement(final Object value) {
        if (StringUtils.isEmpty(this.filtreEnseignement)) {
            return true;
        }
        final EnseignementDTO enseignementDTO = (EnseignementDTO) value;

        return enseignementDTO.getIntitule().toLowerCase()
                                .contains(this.filtreEnseignement.toLowerCase());
    }
    
    /**
     * Mutateur resultatRechercheSequenceDTO.
     * @param resultatRechercheSequenceDTO le resultatRechercheSequenceDTO à modifier.
     */
    public void setResultatRechercheSequenceDTO(
            ResultatRechercheSequenceDTO resultatRechercheSequenceDTO) {
        this.resultatRechercheSequenceDTO = resultatRechercheSequenceDTO;
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
     * Accesseur modifiable.
     * @return le modifiable.
     */
    public Boolean getModifiable() {
        return modifiable;
    }

    /**
     * Mutateur modifiable.
     * @param modifiable le modifiable à modifier.
     */
    public void setModifiable(Boolean modifiable) {
        this.modifiable = modifiable;
    }

    /**
     * Accesseur seancesAssociees.
     * @return le seancesAssociees.
     */
    public Boolean getSeancesAssociees() {
        return seancesAssociees;
    }

    /**
     * Mutateur seancesAssociees.
     * @param seancesAssociees le seancesAssociees à modifier.
     */
    public void setSeancesAssociees(Boolean seancesAssociees) {
        this.seancesAssociees = seancesAssociees;
    }

    /**
     * Accesseur renderedSupprimer.
     * @return le renderedSupprimer.
     */
    public boolean isRenderedSupprimer() {
        return renderedSupprimer;
    }

    /**
     * Mutateur renderedSupprimer.
     * @param renderedSupprimer le renderedSupprimer à modifier.
     */
    public void setRenderedSupprimer(boolean renderedSupprimer) {
        this.renderedSupprimer = renderedSupprimer;
    }

    /**
     * Accesseur renderedDupliquer.
     * @return le renderedDupliquer.
     */
    public boolean isRenderedDupliquer() {
        return renderedDupliquer;
    }

    /**
     * Mutateur renderedDupliquer.
     * @param renderedDupliquer le renderedDupliquer à modifier.
     */
    public void setRenderedDupliquer(boolean renderedDupliquer) {
        this.renderedDupliquer = renderedDupliquer;
    }

    /**
     * Accesseur renderedModifier.
     * @return le renderedModifier.
     */
    public boolean isRenderedModifier() {
        return renderedModifier;
    }

    /**
     * Mutateur renderedModifier.
     * @param renderedModifier le renderedModifier à modifier.
     */
    public void setRenderedModifier(boolean renderedModifier) {
        this.renderedModifier = renderedModifier;
    }

    /**
     * Accesseur enseignementSelectionne.
     * @return le enseignementSelectionne.
     */
    public EnseignementDTO getEnseignementSelectionne() {
        return enseignementSelectionne;
    }

    /**
     * Mutateur enseignementSelectionne.
     * @param enseignementSelectionne le enseignementSelectionne à modifier.
     */
    public void setEnseignementSelectionne(EnseignementDTO enseignementSelectionne) {
        this.enseignementSelectionne = enseignementSelectionne;
    }

    /**
     * Accesseur listeEnseignement.
     * @return le listeEnseignement.
     */
    public List<EnseignementDTO> getListeEnseignement() {
        return listeEnseignement;
    }

    /**
     * Mutateur listeEnseignement.
     * @param listeEnseignement le listeEnseignement à modifier.
     */
    public void setListeEnseignement(List<EnseignementDTO> listeEnseignement) {
        this.listeEnseignement = listeEnseignement;
    }


    /**
     * Accesseur groupeClasseSelectionne.
     * @return le groupeClasseSelectionne.
     */
    public GroupesClassesDTO getGroupeClasseSelectionne() {
        return groupeClasseSelectionne;
    }

    /**
     * Mutateur groupeClasseSelectionne.
     * @param groupeClasseSelectionne le groupeClasseSelectionne à modifier.
     */
    public void setGroupeClasseSelectionne(GroupesClassesDTO groupeClasseSelectionne) {
        this.groupeClasseSelectionne = groupeClasseSelectionne;
    }

    /**
     * Accesseur listeGroupeClasse.
     * @return le listeGroupeClasse.
     */
    public List<GroupesClassesDTO> getListeGroupeClasse() {
        return listeGroupeClasse;
    }

    /**
     * Mutateur listeGroupeClasse.
     * @param listeGroupeClasse le listeGroupeClasse à modifier.
     */
    public void setListeGroupeClasse(List<GroupesClassesDTO> listeGroupeClasse) {
        this.listeGroupeClasse = listeGroupeClasse;
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
     * Accesseur titreDePate.
     * @return le titreDePate.
     */
    public String getTitreDePate() {
        return titreDePate;
    }

    /**
     * Mutateur titreDePate.
     * @param titreDePate le titreDePate à modifier.
     */
    public void setTitreDePate(String titreDePate) {
        this.titreDePate = titreDePate;
    }

    /**
     * Accesseur oldIdSequence.
     * @return le oldIdSequence.
     */
    public Integer getOldIdSequence() {
        return oldIdSequence;
    }

    /**
     * Mutateur oldIdSequence.
     * @param oldIdSequence le oldIdSequence à modifier.
     */
    public void setOldIdSequence(Integer oldIdSequence) {
        this.oldIdSequence = oldIdSequence;
    }

    /**
     * Accesseur oldIdGroupeClasse.
     * @return le oldIdGroupeClasse.
     */
    public Integer getOldIdGroupeClasse() {
        return oldIdGroupeClasse;
    }

    /**
     * Mutateur oldIdGroupeClasse.
     * @param oldIdGroupeClasse le oldIdGroupeClasse à modifier.
     */
    public void setOldIdGroupeClasse(Integer oldIdGroupeClasse) {
        this.oldIdGroupeClasse = oldIdGroupeClasse;
    }



    /**
     * Accesseur oldIdEnseignement.
     * @return le oldIdEnseignement.
     */
    public Integer getOldIdEnseignement() {
        return oldIdEnseignement;
    }

    /**
     * Mutateur oldIdEnseignement.
     * @param oldIdEnseignement le oldIdEnseignement à modifier.
     */
    public void setOldIdEnseignement(Integer oldIdEnseignement) {
        this.oldIdEnseignement = oldIdEnseignement;
    }

    /**
     * Accesseur vraiOuFauxSaisieSimplifiee.
     * @return le vraiOuFauxSaisieSimplifiee
     */
    public Boolean getVraiOuFauxSaisieSimplifiee() {
        return vraiOuFauxSaisieSimplifiee;
    }

    /**
     * Accesseur filtreClasseGroupe.
     * @return le filtreClasseGroupe
     */
    public String getFiltreClasseGroupe() {
        return filtreClasseGroupe;
    }

    /**
     * Mutateur de filtreClasseGroupe.
     * @param filtreClasseGroupe le filtreClasseGroupe à modifier.
     */
    public void setFiltreClasseGroupe(String filtreClasseGroupe) {
        this.filtreClasseGroupe = filtreClasseGroupe;
    }

    /**
     * Accesseur de filtreEnseignement {@link #filtreEnseignement}.
     * @return retourne filtreEnseignement 
     */
    public String getFiltreEnseignement() {
        return filtreEnseignement;
    }

    /**
     * Mutateur de filtreEnseignement {@link #filtreEnseignement}.
     * @param filtreEnseignement the filtreEnseignement to set
     */
    public void setFiltreEnseignement(String filtreEnseignement) {
        this.filtreEnseignement = filtreEnseignement;
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
     * @return the oldTypeGroupeSelectionne
     */
    public TypeGroupe getOldTypeGroupeSelectionne() {
        return oldTypeGroupeSelectionne;
    }

    /**
     * @param oldTypeGroupeSelectionne the oldTypeGroupeSelectionne to set
     */
    public void setOldTypeGroupeSelectionne(TypeGroupe oldTypeGroupeSelectionne) {
        this.oldTypeGroupeSelectionne = oldTypeGroupeSelectionne;
    }

	public List<TypeCouleur> getListeCouleur() {
		return listeCouleur;
	}

	public void setListeCouleur(List<TypeCouleur> listeCouleur) {
		this.listeCouleur = listeCouleur;
	}

	public TypeCouleur getTypeCouleur() {
		return typeCouleur;
	}

	public void setTypeCouleur(TypeCouleur typeCouleur) {
		this.typeCouleur = typeCouleur;
	}

    
    


}
