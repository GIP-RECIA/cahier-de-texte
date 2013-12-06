/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AccueilForm.java,v 1.8 2010/05/20 08:35:09 jerome.carriere Exp $
 */

package org.crlr.web.application.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.crlr.dto.UserDTO;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.DetailJourDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.utils.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.dto.EmploiJoursDTO;
import org.crlr.web.dto.EmploiMultipleJoursDTO;
import org.crlr.web.dto.EnteteEmploiJoursDTO;
import org.crlr.web.dto.GrilleHoraireDTO;
import org.crlr.web.dto.TypeSemaine;

/**
 * Formulaire d'accueil.
 *
 * @author breytond
 */
public class AccueilForm extends AbstractForm {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** civilité, nom et prénom de l'utilisateur concaténé. */
    private String civiliteNomPrenom;

    /** liste des enfants d'un parent, pour un profil Parent. */
    private Set<UserDTO> listeEnfant;

    /** Nom prénom Enfant selectionné. */
    private String enfantSelectionne;

    /** Id Enfant selectionné. */
    private Integer idEnfantSelectionne;

    /** cellule sélectionnée pour une matiere/prof. */
    private DetailJourEmploiDTO celluleSelectionnee;

    /**
     * Liste des prochaines séances. (peut etre visualisé par les enseingants qui
     * on fait ce choix dans les préférences).
     */
    private List<SeanceDTO> listeSeances;

    /**
     * liste des établissements disponible en v1.1 uniquement pour le profil
     * inspecteur académique.
     */
    private List<EtablissementDTO> listeEtablissement;

    /** le code de l'etablissement selectionne. */
    private EtablissementDTO etablissementSelectionne;
    
    /** le code de l'etablissement selectionne. */
    private String profil;

    /** liste des prochains devoirs. */
    private List<DetailJourDTO> listeDevoir;

    /** Détails du devoir. */
    private DetailJourDTO selectDevoir;

    /** Détails de la séance. */
    private SeanceDTO selectSeance;

    /** position de la cellule sélectionnée. */
    private Integer celluleSelection;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private List<GrilleHoraireDTO> horairesCours;

    /** liste des jours des vaqués. */
    private Set<Date> listeVaque;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Map<String, EnteteEmploiJoursDTO> enteteEmploi;

    /** Pour la génération de l'emploi du temps des parents et enfants. */
    private List<EmploiMultipleJoursDTO> listeJourConsolide;

    /** Pour la génération de l'emploi du temps des enseignants. */
    private List<EmploiJoursDTO> listeJour;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private BarreSemaineDTO semaineSelectionne;

    /** horaire de la cellule sélectionnée. */
    private String horaireSelectionnee;

    /** Map des jours ouvrés accessible. */
    private Map<TypeJour, Boolean> jourOuvreAccessible;

    /** Liste des classes de l'enseignant. */
    private List<GroupesClassesDTO> listeClasses;

    /** Liste des groupes de l'enseignant. */
    private List<GroupesClassesDTO> listeGroupes;

    /** Liste des enseignements de l'enseignant. */
    private List<EnseignementDTO> listeEnseignements;

    /** Identifiant de l'établissement en cours de consultation. */
    private Integer idEtabEnCours;

    /** Préférences de l'utilisateur. */
    private String userPreferences;

    /** Type de semaine. */
    private TypeSemaine typeSemaine;

    /** Vrai ou faux alternance de semaine. */
    private Boolean vraiOuFauxAlternance;

    /** Vrai ou faux affichage des préférences. */
    private Boolean vraiOuFauxDisplayPreferences;
    
    /** Flitre pour la popup d'établissement. */
    private String filtreEtablissement;
    
    /** Indique si on affiche l'EDT mensuel ou non.*/
    private Boolean vraiOuFauxAfficheEDTMensuel; 

/**
     * Constructeur.
     */
    public AccueilForm() {
        super();
        final UtilisateurDTO utilisateurDTO =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        this.listeEnfant = new HashSet<UserDTO>();
        this.listeEtablissement = new ArrayList<EtablissementDTO>();
        this.idEtabEnCours = null;
        this.listeDevoir = new ArrayList<DetailJourDTO>();
        this.selectDevoir = new DetailJourDTO();
        this.selectSeance = new SeanceDTO();
        this.celluleSelectionnee = new DetailJourEmploiDTO();
        this.celluleSelection = -1;
        this.horairesCours = new ArrayList<GrilleHoraireDTO>();
        this.enteteEmploi = new HashMap<String, EnteteEmploiJoursDTO>();
        this.listeJour = new ArrayList<EmploiJoursDTO>();
        this.listeJourConsolide = new ArrayList<EmploiMultipleJoursDTO>();
        this.semaineSelectionne = new BarreSemaineDTO();
        this.jourOuvreAccessible = GenerateurDTO.generateMapJourOuvreFromDb(utilisateurDTO.getJoursOuvresEtablissement());
        this.listeClasses = new ArrayList<GroupesClassesDTO>();
        this.listeGroupes = new ArrayList<GroupesClassesDTO>();
        this.listeEnseignements = new ArrayList<EnseignementDTO>();
        this.listeSeances = new ArrayList<SeanceDTO>();
        this.userPreferences = "";
        this.etablissementSelectionne = new EtablissementDTO();
        this.filtreEtablissement = "";
        
        this.vraiOuFauxDisplayPreferences = BooleanUtils.isTrue(utilisateurDTO.getVraiOuFauxCahierOuvertEtab()) &&
                                                BooleanUtils.isTrue(utilisateurDTO.getAnneeScolaireDTO()
                                                              .getVraiOuFauxCahierOuvertENT());
        
        this.vraiOuFauxAfficheEDTMensuel = Boolean.FALSE;
    }

    /**
     * Filtre les valeurs de la popup des etablissements.
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public Boolean doFilterEtablissement(final Object value) {
        if (StringUtils.isEmpty(this.filtreEtablissement)) {
            return true;
        } 
        final EtablissementDTO etablissementDTO = (EtablissementDTO) value;

        return etablissementDTO.getDesignation().toLowerCase()
                                .contains(this.filtreEtablissement.toLowerCase());
    }
    
    /**
     * Accesseur.
     *
     * @return the civiliteNomPrenom
     */
    public String getCiviliteNomPrenom() {
        return civiliteNomPrenom;
    }

    /**
     * Mutateur.
     *
     * @param civiliteNomPrenom the civiliteNomPrenom to set
     */
    public void setCiviliteNomPrenom(String civiliteNomPrenom) {
        this.civiliteNomPrenom = civiliteNomPrenom;
    }

    /**
     * Accesseur.
     *
     * @return the listeEnfant
     */
    public Set<UserDTO> getListeEnfant() {
        return listeEnfant;
    }

    /**
     * Mutateur.
     *
     * @param listeEnfant the listeEnfant to set
     */
    public void setListeEnfant(Set<UserDTO> listeEnfant) {
        this.listeEnfant = listeEnfant;
    }

    /**
     * Accesseur.
     *
     * @return the enfantSelectionne
     */
    public String getEnfantSelectionne() {
        return enfantSelectionne;
    }

    /**
     * Mutateur.
     *
     * @param enfantSelectionne the enfantSelectionne to set
     */
    public void setEnfantSelectionne(String enfantSelectionne) {
        this.enfantSelectionne = enfantSelectionne;
    }

    /**
     * Accesseur.
     *
     * @return the idEnfantSelectionne
     */
    public Integer getIdEnfantSelectionne() {
        return idEnfantSelectionne;
    }

    /**
     * Mutateur.
     *
     * @param idEnfantSelectionne the idEnfantSelectionne to set
     */
    public void setIdEnfantSelectionne(Integer idEnfantSelectionne) {
        this.idEnfantSelectionne = idEnfantSelectionne;
    }

    /**
     * Accesseur listeEtablissement.
     *
     * @return le listeEtablissement.
     */
    public List<EtablissementDTO> getListeEtablissement() {
        return listeEtablissement;
    }

    /**
     * Mutateur listeEtablissement.
     *
     * @param listeEtablissement le listeEtablissement à modifier.
     */
    public void setListeEtablissement(List<EtablissementDTO> listeEtablissement) {
        this.listeEtablissement = listeEtablissement;
    }

    /**
     * Accesseur profil.
     *
     * @return le profil.
     */
    public String getProfil() {
        return profil;
    }

    /**
     * Mutateur profil.
     *
     * @param profil le profil à modifier.
     */
    public void setProfil(String profil) {
        this.profil = profil;
    }

    /**
     * Accesseur listeDevoir.
     *
     * @return la listeDevoir.
     */
    public List<DetailJourDTO> getListeDevoir() {
        return listeDevoir;
    }

    /**
     * Mutateur listeDevoir.
     *
     * @param listeDevoir la liste des devoirs à modifier.
     */
    public void setListeDevoir(List<DetailJourDTO> listeDevoir) {
        this.listeDevoir = listeDevoir;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public DetailJourDTO getSelectDevoir() {
        return selectDevoir;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param selectDevoir DOCUMENTATION INCOMPLETE!
     */
    public void setSelectDevoir(DetailJourDTO selectDevoir) {
        this.selectDevoir = selectDevoir;
    }

    /**
     * Accesseur selectSeance.
     *
     * @return le selectSeance
     */
    public SeanceDTO getSelectSeance() {
        return selectSeance;
    }

    /**
     * Mutateur de selectSeance.
     *
     * @param selectSeance le selectSeance à modifier.
     */
    public void setSelectSeance(SeanceDTO selectSeance) {
        this.selectSeance = selectSeance;
    }

    /**
     * Accesseur celluleSelectionnee.
     *
     * @return le celluleSelectionnee
     */
    public DetailJourEmploiDTO getCelluleSelectionnee() {
        return celluleSelectionnee;
    }

    /**
     * Mutateur de celluleSelectionnee.
     *
     * @param celluleSelectionnee le celluleSelectionnee à modifier.
     */
    public void setCelluleSelectionnee(DetailJourEmploiDTO celluleSelectionnee) {
        this.celluleSelectionnee = celluleSelectionnee;
    }

    /**
     * Accesseur celluleSelection.
     *
     * @return le celluleSelection
     */
    public Integer getCelluleSelection() {
        return celluleSelection;
    }

    /**
     * Mutateur de celluleSelection.
     *
     * @param celluleSelection le celluleSelection à modifier.
     */
    public void setCelluleSelection(Integer celluleSelection) {
        this.celluleSelection = celluleSelection;
    }

    /**
     * Accesseur enteteEmploi.
     *
     * @return le enteteEmploi
     */
    public Map<String, EnteteEmploiJoursDTO> getEnteteEmploi() {
        return enteteEmploi;
    }

    /**
     * Mutateur de enteteEmploi.
     *
     * @param enteteEmploi le enteteEmploi à modifier.
     */
    public void setEnteteEmploi(Map<String, EnteteEmploiJoursDTO> enteteEmploi) {
        this.enteteEmploi = enteteEmploi;
    }

    /**
     * Accesseur listeVaque.
     *
     * @return le listeVaque
     */
    public Set<Date> getListeVaque() {
        return listeVaque;
    }

    /**
     * Mutateur de listeVaque.
     *
     * @param listeVaque le listeVaque à modifier.
     */
    public void setListeVaque(Set<Date> listeVaque) {
        this.listeVaque = listeVaque;
    }

    /**
     * Accesseur horairesCours.
     *
     * @return le horairesCours
     */
    public List<GrilleHoraireDTO> getHorairesCours() {
        return horairesCours;
    }

    /**
     * Mutateur de horairesCours.
     *
     * @param horairesCours le horairesCours à modifier.
     */
    public void setHorairesCours(List<GrilleHoraireDTO> horairesCours) {
        this.horairesCours = horairesCours;
    }

    /**
     * Accesseur semaineSelectionne.
     *
     * @return le semaineSelectionne
     */
    public BarreSemaineDTO getSemaineSelectionne() {
        return semaineSelectionne;
    }

    /**
     * Mutateur de semaineSelectionne.
     *
     * @param semaineSelectionne le semaineSelectionne à modifier.
     */
    public void setSemaineSelectionne(BarreSemaineDTO semaineSelectionne) {
        this.semaineSelectionne = semaineSelectionne;
    }

    /**
     * Accesseur horaireSelectionnee.
     *
     * @return le horaireSelectionnee
     */
    public String getHoraireSelectionnee() {
        return horaireSelectionnee;
    }

    /**
     * Mutateur de horaireSelectionnee.
     *
     * @param horaireSelectionnee le horaireSelectionnee à modifier.
     */
    public void setHoraireSelectionnee(String horaireSelectionnee) {
        this.horaireSelectionnee = horaireSelectionnee;
    }

    /**
     * Accesseur jourOuvreAccessible.
     *
     * @return le jourOuvreAccessible
     */
    public Map<TypeJour, Boolean> getJourOuvreAccessible() {
        return jourOuvreAccessible;
    }

    /**
     * Mutateur de jourOuvreAccessible.
     *
     * @param jourOuvreAccessible le jourOuvreAccessible à modifier.
     */
    public void setJourOuvreAccessible(Map<TypeJour, Boolean> jourOuvreAccessible) {
        this.jourOuvreAccessible = jourOuvreAccessible;
    }

    /**
     * Accesseur listeJourConsolide.
     *
     * @return le listeJourConsolide
     */
    public List<EmploiMultipleJoursDTO> getListeJourConsolide() {
        return listeJourConsolide;
    }

    /**
     * Mutateur de listeJourConsolide.
     *
     * @param listeJourConsolide le listeJourConsolide à modifier.
     */
    public void setListeJourConsolide(List<EmploiMultipleJoursDTO> listeJourConsolide) {
        this.listeJourConsolide = listeJourConsolide;
    }

    /**
     * Accesseur listeJour.
     *
     * @return le listeJour
     */
    public List<EmploiJoursDTO> getListeJour() {
        return listeJour;
    }

    /**
     * Mutateur de listeJour.
     *
     * @param listeJour le listeJour à modifier.
     */
    public void setListeJour(List<EmploiJoursDTO> listeJour) {
        this.listeJour = listeJour;
    }

    /**
     * Accesseur listeClasses.
     *
     * @return le listeClasses
     */
    public List<GroupesClassesDTO> getListeClasses() {
        return listeClasses;
    }

    /**
     * Mutateur de listeClasses.
     *
     * @param listeClasses le listeClasses à modifier.
     */
    public void setListeClasses(List<GroupesClassesDTO> listeClasses) {
        this.listeClasses = listeClasses;
    }

    /**
     * Accesseur listeGroupes.
     *
     * @return le listeGroupes
     */
    public List<GroupesClassesDTO> getListeGroupes() {
        return listeGroupes;
    }

    /**
     * Mutateur de listeGroupes.
     *
     * @param listeGroupes le listeGroupes à modifier.
     */
    public void setListeGroupes(List<GroupesClassesDTO> listeGroupes) {
        this.listeGroupes = listeGroupes;
    }

    /**
     * Accesseur listeEnseignements.
     *
     * @return le listeEnseignements
     */
    public List<EnseignementDTO> getListeEnseignements() {
        return listeEnseignements;
    }

    /**
     * Mutateur de listeEnseignements.
     *
     * @param listeEnseignements le listeEnseignements à modifier.
     */
    public void setListeEnseignements(List<EnseignementDTO> listeEnseignements) {
        this.listeEnseignements = listeEnseignements;
    }

    /**
     * Accesseur idEtabEnCours.
     *
     * @return le idEtabEnCours
     */
    public Integer getIdEtabEnCours() {
        return idEtabEnCours;
    }

    /**
     * Mutateur de idEtabEnCours.
     *
     * @param idEtabEnCours le idEtabEnCours à modifier.
     */
    public void setIdEtabEnCours(Integer idEtabEnCours) {
        this.idEtabEnCours = idEtabEnCours;
    }

    /**
     * Accesseur userPreferences.
     *
     * @return le userPreferences
     */
    public String getUserPreferences() {
        return userPreferences;
    }

    /**
     * Mutateur de userPreferences.
     *
     * @param userPreferences le userPreferences à modifier.
     */
    public void setUserPreferences(String userPreferences) {
        this.userPreferences = userPreferences;
    }

    /**
     * Accesseur listeSeances.
     *
     * @return le listeSeances
     */
    public List<SeanceDTO> getListeSeances() {
        return listeSeances;
    }

    /**
     * Mutateur de listeSeances.
     *
     * @param listeSeances le listeSeances à modifier.
     */
    public void setListeSeances(List<SeanceDTO> listeSeances) {
        this.listeSeances = listeSeances;
    }

    /**
     * Accesseur typeSemaine.
     *
     * @return le typeSemaine
     */
    public TypeSemaine getTypeSemaine() {
        return typeSemaine;
    }

    /**
     * Mutateur de typeSemaine.
     *
     * @param typeSemaine le typeSemaine à modifier.
     */
    public void setTypeSemaine(TypeSemaine typeSemaine) {
        this.typeSemaine = typeSemaine;
    }

    /**
     * Accesseur vraiOuFauxAlternance.
     *
     * @return le vraiOuFauxAlternance
     */
    public Boolean getVraiOuFauxAlternance() {
        return vraiOuFauxAlternance;
    }

    /**
     * Mutateur de vraiOuFauxAlternance.
     *
     * @param vraiOuFauxAlternance le vraiOuFauxAlternance à modifier.
     */
    public void setVraiOuFauxAlternance(Boolean vraiOuFauxAlternance) {
        this.vraiOuFauxAlternance = vraiOuFauxAlternance;
    }

    /**
     * Retourne une chaine du type de semaine.
     *
     * @return Character avec le type de semaine
     */
    public Character getTypeJourEmploiChaine() {
        final Character result;
        if (BooleanUtils.isTrue(this.vraiOuFauxAlternance) && (typeSemaine != null)) {
            result = typeSemaine.getValeur();
        } else {
            result = ' ';
        }
        return result;
    }

    /**
     * Accesseur vraiOuFauxDisplayPreferences.
     * @return le vraiOuFauxDisplayPreferences
     */
    public Boolean getVraiOuFauxDisplayPreferences() {
        return vraiOuFauxDisplayPreferences;
    }

    /**
     * Mutateur de vraiOuFauxDisplayPreferences.
     * @param vraiOuFauxDisplayPreferences le vraiOuFauxDisplayPreferences à modifier.
     */
    public void setVraiOuFauxDisplayPreferences(Boolean vraiOuFauxDisplayPreferences) {
        this.vraiOuFauxDisplayPreferences = vraiOuFauxDisplayPreferences;
    }

    /**
     * Accesseur filtreEtablissement.
     * @return le filtreEtablissement
     */
    public String getFiltreEtablissement() {
        return filtreEtablissement;
    }

    /**
     * Mutateur de filtreEtablissement.
     * @param filtreEtablissement le filtreEtablissement à modifier.
     */
    public void setFiltreEtablissement(String filtreEtablissement) {
        this.filtreEtablissement = filtreEtablissement;
    }

    /**
     * Accesseur etablissementSelectionne.
     * @return le etablissementSelectionne
     */
    public EtablissementDTO getEtablissementSelectionne() {
        return etablissementSelectionne;
    }

    /**
     * Mutateur de etablissementSelectionne.
     * @param etablissementSelectionne le etablissementSelectionne à modifier.
     */
    public void setEtablissementSelectionne(
            EtablissementDTO etablissementSelectionne) {
        this.etablissementSelectionne = etablissementSelectionne;
    }

    /**
     * Accesseur de vraiOuFauxAfficheEDTMensuel : {@link #vraiOuFauxAfficheEDTMensuel}.
     *  @return retourne vraiOuFauxAfficheEDTMensuel
     */
    public Boolean getVraiOuFauxAfficheEDTMensuel() {
        return vraiOuFauxAfficheEDTMensuel;
    }

    /**
     * Mutateur de vraiOuFauxAfficheEDTMensuel : {@link #vraiOuFauxAfficheEDTMensuel}.
     * @param vraiOuFauxAfficheEDTMensuel le vraiOuFauxAfficheEDTMensuel à modifier
     */
    public void setVraiOuFauxAfficheEDTMensuel(Boolean vraiOuFauxAfficheEDTMensuel) {
        this.vraiOuFauxAfficheEDTMensuel = vraiOuFauxAfficheEDTMensuel;
    }
    
    
}
