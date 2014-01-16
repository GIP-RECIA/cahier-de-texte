/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: UtilisateurDTO.java,v 1.10 2010/05/19 08:44:25 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.crlr.dto.UserDTO;
import org.crlr.dto.application.remplacement.RemplacementDTO;

/**
 * DTO contenant les informations d'un utilisateur de l'ENT.
 *
 * @author breytond.
 */
public class UtilisateurDTO implements Serializable {
    /** Seiral UID. */
    private static final long serialVersionUID = 4417093029473134917L;

    /** DTO d'information de base concernant l'utilisateur. */
    private UserDTO userDTO = new UserDTO();

    /** Civilité de l'individu. */
    private String civilite;

    /** l'identifiant de l'établissement. */
    private Integer idEtablissement;

    /** Désignation de l'établissement. */
    private String designationEtablissement;
    
    /** Jours ouvrés de l'établissement. */
    private String joursOuvresEtablissement;   
    
    /** le cahier est accessible pour les utilisateurs de l'établissement. */
    private Boolean vraiOuFauxCahierOuvertEtab;
    
    /** le cahier est ouvert à la saisie simplifiée pour l'établissement sélectionné par l'enseignant. */
    private Boolean vraiOuFauxEtabSaisieSimplifiee;

    /** Information sur l'année scolaire. */
    private AnneeScolaireDTO anneeScolaireDTO = new AnneeScolaireDTO();

    /** SIREN de l'établissement. */
    private String sirenEtablissement;

    /** Liste des uid des enfants (uniquement valable pour un profil parent). */
    private Set<String> listeUidEnfant = new HashSet<String>();

    /** Profil de l'utilisateur. */
    private Profil profil;

    /**
     * Drapeau permetant de savoir si un inspecteur académique peut faire une
     * recherche dans le cahier de texte.
     */
    private Boolean archiveUniquementDisponible;

    /** code de l'établissement selectionné par l'inspecteur. */
    private String codeEtablissementSelectionneInspecteur;

    /** La skin de l'utilisateur. */
    private TypeSkin typeSkin;

    /** SIREN des établissement. */
    private Set<String> sirensEtablissement = new HashSet<String>();
    
    /** Siren des établissement ou l'acteur est administrateur de ressources. */
    private Set<String> adminRessourceSiren = new HashSet<String>();
    
    /** Spécifie si l'utilisateur est administrateur de ressources de tout les établissemennts. */
    private Boolean vraiOuFauxAdmRessourceENT;
    
    /** Spécifie si l'acteur est l'administrateur central. */
    private Boolean vraiOuFauxAdmCentral;
    
    /** Siren des établissement ou l'acteur est administrateur local. */
    private Set<String> adminLocalSiren = new HashSet<String>();
    
    private String ticketAlfresco;
    
    private RemplacementDTO periodeRemplacement;
    
    /**
     * Accesseur profil.
     *
     * @return le profil.
     */
    public Profil getProfil() {
        return profil;
    }

    /**
     * Mutateur profil.
     *
     * @param profil le profil à modifier.
     */
    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    /**
     * Accesseur sirenEtablissement.
     *
     * @return le sirenEtablissement.
     */
    public String getSirenEtablissement() {
        return sirenEtablissement;
    }

    /**
     * Mutateur SirenEtablissement.
     *
     * @param sirenEtablissement le sirenEtablissement à modifier.
     */
    public void setSirenEtablissement(String sirenEtablissement) {
        this.sirenEtablissement = sirenEtablissement;
    }

    /**
     * Accesseur listeUidEnfant.
     *
     * @return le listeUidEnfant.
     */
    public Set<String> getListeUidEnfant() {
        return listeUidEnfant;
    }

    /**
     * Mutateur listeUidEnfant.
     *
     * @param listeUidEnfant le listeUidEnfant à modifier.
     */
    public void setListeUidEnfant(Set<String> listeUidEnfant) {
        this.listeUidEnfant = listeUidEnfant;
    }

    /**
     * Accesseur civilite.
     *
     * @return le civilite.
     */
    public String getCivilite() {
        return civilite;
    }

    /**
     * Mutateur civilite.
     *
     * @param civilite le civilite à modifier.
     */
    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    /**
     * Accesseur idEtablissement.
     *
     * @return le idEtablissement.
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur idEtablissement.
     *
     * @param idEtablissement le idEtablissement à modifier.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur.
     *
     * @return the userDTO
     */
    public UserDTO getUserDTO() {
        return userDTO;
    }

    /**
     * Mutateur.
     *
     * @param userDTO the userDTO to set
     */
    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    /**
     * Accesseur.
     *
     * @return the anneeScolaireDTO
     */
    public AnneeScolaireDTO getAnneeScolaireDTO() {
        return anneeScolaireDTO;
    }

    /**
     * Mutateur.
     *
     * @param anneeScolaireDTO the anneeScolaireDTO to set
     */
    public void setAnneeScolaireDTO(AnneeScolaireDTO anneeScolaireDTO) {
        this.anneeScolaireDTO = anneeScolaireDTO;
    }

    /**
     * Accesseur archiveUniquementDisponible.
     *
     * @return le archiveUniquementDisponible.
     */
    public Boolean getArchiveUniquementDisponible() {
        return archiveUniquementDisponible;
    }

    /**
     * Mutateur archiveUniquementDisponible.
     *
     * @param archiveUniquementDisponible le archiveUniquementDisponible à modifier.
     */
    public void setArchiveUniquementDisponible(Boolean archiveUniquementDisponible) {
        this.archiveUniquementDisponible = archiveUniquementDisponible;
    }

    /**
     * Accesseur codeEtablissementSelectionneInspecteur.
     *
     * @return le codeEtablissementSelectionneInspecteur.
     */
    public String getCodeEtablissementSelectionneInspecteur() {
        return codeEtablissementSelectionneInspecteur;
    }

    /**
     * Mutateur codeEtablissementSelectionneInspecteur.
     *
     * @param codeEtablissementSelectionneInspecteur le
     *        codeEtablissementSelectionneInspecteur à modifier.
     */
    public void setCodeEtablissementSelectionneInspecteur(String codeEtablissementSelectionneInspecteur) {
        this.codeEtablissementSelectionneInspecteur = codeEtablissementSelectionneInspecteur;
    }

    /**
     * Accesseur typeSkin.
     *
     * @return le typeSkin.
     */
    public TypeSkin getTypeSkin() {
        return typeSkin;
    }

    /**
     * Mutateur typeSkin.
     *
     * @param typeSkin le typeSkin à modifier.
     */
    public void setTypeSkin(TypeSkin typeSkin) {
        this.typeSkin = typeSkin;
    }

    /**
     * Accesseur designationEtablissement.
     *
     * @return designationEtablissement
     */
    public String getDesignationEtablissement() {
        return designationEtablissement;
    }

    /**
     * Mutateur designationEtablissement.
     *
     * @param designationEtablissement designationEtablissement à modifier
     */
    public void setDesignationEtablissement(String designationEtablissement) {
        this.designationEtablissement = designationEtablissement;
    }

    /**
     * Accesseur joursOuvresEtablissement.
     * @return le joursOuvresEtablissement
     */
    public String getJoursOuvresEtablissement() {
        return joursOuvresEtablissement;
    }

    /**
     * Mutateur de joursOuvresEtablissement.
     * @param joursOuvresEtablissement le joursOuvresEtablissement à modifier.
     */
    public void setJoursOuvresEtablissement(String joursOuvresEtablissement) {
        this.joursOuvresEtablissement = joursOuvresEtablissement;
    }
   
    /**
     * Accesseur vraiOuFauxCahierOuvertEtab.
     * @return le vraiOuFauxCahierOuvertEtab
     */
    public Boolean getVraiOuFauxCahierOuvertEtab() {
        return vraiOuFauxCahierOuvertEtab;
    }

    /**
     * Mutateur de vraiOuFauxCahierOuvertEtab.
     * @param vraiOuFauxCahierOuvertEtab le vraiOuFauxCahierOuvertEtab à modifier.
     */
    public void setVraiOuFauxCahierOuvertEtab(Boolean vraiOuFauxCahierOuvertEtab) {
        this.vraiOuFauxCahierOuvertEtab = vraiOuFauxCahierOuvertEtab;
    }

    /**
     * Accesseur vraiOuFauxEtabSaisieSimplifiee.
     * @return le vraiOuFauxEtabSaisieSimplifiee
     */
    public Boolean getVraiOuFauxEtabSaisieSimplifiee() {
        return vraiOuFauxEtabSaisieSimplifiee;
    }

    /**
     * Mutateur de vraiOuFauxEtabSaisieSimplifiee.
     * @param vraiOuFauxEtabSaisieSimplifiee le vraiOuFauxEtabSaisieSimplifiee à modifier.
     */
    public void setVraiOuFauxEtabSaisieSimplifiee(
            Boolean vraiOuFauxEtabSaisieSimplifiee) {
        this.vraiOuFauxEtabSaisieSimplifiee = vraiOuFauxEtabSaisieSimplifiee;
    }

    /**
     * Accesseur adminRessourceSiren.
     * @return le adminRessourceSiren
     */
    public Set<String> getAdminRessourceSiren() {
        return adminRessourceSiren;
    }

    /**
     * Mutateur de adminRessourceSiren.
     * @param adminRessourceSiren le adminRessourceSiren à modifier.
     */
    public void setAdminRessourceSiren(Set<String> adminRessourceSiren) {
        this.adminRessourceSiren = adminRessourceSiren;
    }

    /**
     * Accesseur adminLocalSiren.
     * @return le adminLocalSiren
     */
    public Set<String> getAdminLocalSiren() {
        return adminLocalSiren;
    }

    /**
     * Mutateur de adminLocalSiren.
     * @param adminLocalSiren le adminLocalSiren à modifier.
     */
    public void setAdminLocalSiren(Set<String> adminLocalSiren) {
        this.adminLocalSiren = adminLocalSiren;
    }

    /**
     * Accesseur vraiOuFauxAdmRessourceENT.
     * @return le vraiOuFauxAdmRessourceENT
     */
    public Boolean getVraiOuFauxAdmRessourceENT() {
        return vraiOuFauxAdmRessourceENT;
    }

    /**
     * Mutateur de vraiOuFauxAdmRessourceENT.
     * @param vraiOuFauxAdmRessourceENT le vraiOuFauxAdmRessourceENT à modifier.
     */
    public void setVraiOuFauxAdmRessourceENT(Boolean vraiOuFauxAdmRessourceENT) {
        this.vraiOuFauxAdmRessourceENT = vraiOuFauxAdmRessourceENT;
    }

    /**
     * Accesseur vraiOuFauxAdmCentral.
     * @return le vraiOuFauxAdmCentral
     */
    public Boolean getVraiOuFauxAdmCentral() {
        return vraiOuFauxAdmCentral;
    }

    /**
     * Mutateur de vraiOuFauxAdmCentral.
     * @param vraiOuFauxAdmCentral le vraiOuFauxAdmCentral à modifier.
     */
    public void setVraiOuFauxAdmCentral(Boolean vraiOuFauxAdmCentral) {
        this.vraiOuFauxAdmCentral = vraiOuFauxAdmCentral;
    }

    /**
     * Accesseur sirensEtablissement.
     * @return le sirensEtablissement
     */
    public Set<String> getSirensEtablissement() {
        return sirensEtablissement;
    }

    /**
     * Mutateur de sirensEtablissement.
     * @param sirensEtablissement le sirensEtablissement à modifier.
     */
    public void setSirensEtablissement(Set<String> sirensEtablissement) {
        this.sirensEtablissement = sirensEtablissement;
    }

    /**
     * @return the ticketAlfresco
     */
    public String getTicketAlfresco() {
        return ticketAlfresco;
    }

    /**
     * @param ticketAlfresco the ticketAlfresco to set
     */
    public void setTicketAlfresco(String ticketAlfresco) {
        this.ticketAlfresco = ticketAlfresco;
    }

    /**
     * @return the periodeRemplacement
     */
    public RemplacementDTO getPeriodeRemplacement() {
        return periodeRemplacement;
    }

    /**
     * @param periodeRemplacement the periodeRemplacement to set
     */
    public void setPeriodeRemplacement(RemplacementDTO periodeRemplacement) {
        this.periodeRemplacement = periodeRemplacement;
    }
}
