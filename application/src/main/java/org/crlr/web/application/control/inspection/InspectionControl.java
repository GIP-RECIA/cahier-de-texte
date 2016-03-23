/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.web.application.control.inspection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang.BooleanUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.UserDTOForList;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.inspection.DroitInspecteurDTO;
import org.crlr.dto.application.inspection.RechercheDroitInspecteurQO;
import org.crlr.dto.application.inspection.SaveDroitInspecteurQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.InspectionService;
import org.crlr.utils.CollectionUtils;
import org.crlr.web.application.control.AbstractControl;
import org.crlr.web.application.form.inspection.InspectionForm;
import org.crlr.web.contexte.utils.ContexteUtils;

/**
 * AdminControl.
 *
 * @author breytond
 * @version $Revision: 1.13 $
 */
@ManagedBean(name = "inspection")
@ViewScoped
public class InspectionControl extends AbstractControl<InspectionForm> {

    /** Controleur de piece jointe. */
    @ManagedProperty(value = "#{inspectionService}")
    private transient InspectionService inspectionService;

    /**
     * Mutateur de inspectionService {@link #inspectionService}.
     * @param inspectionService le inspectionService to set
     */
    public void setInspectionService(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

/**
     * Constructeur.
     */
    public InspectionControl() {
        super(new InspectionForm());
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    public void onLoad() {

        final UtilisateurDTO utilisateurDTO =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();

        final Profil profilUser = utilisateurDTO.getProfil();
        form.setProfilNavigation(profilUser);

        final Integer idEtablissement = utilisateurDTO.getIdEtablissement();
        if (Profil.DIRECTION_ETABLISSEMENT.equals(profilUser)) {
            form.setInspecteurs(inspectionService.findListeInspecteurs());
            form.setEnseignants(inspectionService.findListeEnseignants(idEtablissement));
        } 
        rechercheDroits();
    }
    
    /**
     * Effectue la recherche des droits disponibles en fonction du profil.
     */
    private void rechercheDroits(){
        
        final UtilisateurDTO utilisateurDTO =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();

        final Profil profilUser = utilisateurDTO.getProfil();
        final Integer idEtablissement = utilisateurDTO.getIdEtablissement();
        
        final RechercheDroitInspecteurQO rechercheDroitInspecteurQO = new RechercheDroitInspecteurQO();
        if (Profil.DIRECTION_ETABLISSEMENT.equals(profilUser)) {
            
            rechercheDroitInspecteurQO.setIdEtablissement(idEtablissement);
            rechercheDroitInspecteurQO.setVraiOuFauxRechercheFromDirecteur(true);
            
            
            
        } else if (Profil.INSPECTION_ACADEMIQUE.equals(profilUser)) {
            rechercheDroitInspecteurQO.setIdEtablissement(idEtablissement);
            rechercheDroitInspecteurQO.setIdInspecteur(utilisateurDTO.getUserDTO().getIdentifiant());
            rechercheDroitInspecteurQO.setVraiOuFauxRechercheFromDirecteur(false);
              
        } 
        
        final ResultatDTO<List<DroitInspecteurDTO>> droitsInspecteurs =
            inspectionService.findDroitsInspection(rechercheDroitInspecteurQO);
        
        form.setDroitsInspecteur(droitsInspecteurs.getValeurDTO());
        form.setDroitsInspecteurInit(CollectionUtils.cloneCollection(form.getDroitsInspecteur()));
    }

    /**
     * Recharge la liste des droits initiale.
     */
    public void reload() {
        form.setDroitsInspecteur(CollectionUtils.cloneCollection(form.getDroitsInspecteurInit()));
    }
    
    /**
     * Supprime / Reactive un droit accordé à un inspecteur sur un enseignant sans sauver la modif (juste cote ihm).
     */
    public void supprimerIHM() {
        if (form.getDroitSelectionne() == null) { return; }
        form.getDroitSelectionne().setEstSupprimee(!form.getDroitSelectionne().getEstSupprimee());
    }
    
    /**
     * Supprime un droit accordé à un inspecteur sur un enseignant.
     */
    public void supprimer() {
        final SaveDroitInspecteurQO saveDroitInspecteurQO = new SaveDroitInspecteurQO();

        final Integer idEtablissement =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getIdEtablissement();
        final EnseignantDTO enseignant =
            form.getDroitSelectionne().getEnseignant();
        final UserDTOForList inspecteur =
            form.getDroitSelectionne().getInspecteur();

        saveDroitInspecteurQO.setIdEtablissement(idEtablissement);
        final List<EnseignantDTO> listeEnseignants = new ArrayList<EnseignantDTO>();
        listeEnseignants.add(enseignant);
        final List<UserDTOForList> listeInspecteurs = new ArrayList<UserDTOForList>();
        listeInspecteurs.add(inspecteur);
        saveDroitInspecteurQO.setEnseignantsSelectionne(listeEnseignants);
        saveDroitInspecteurQO.setInspecteursSelectionne(listeInspecteurs);

        saveDroitInspecteurQO.setVraiOuFauxAjout(false);

        try {
            inspectionService.saveDroit(saveDroitInspecteurQO);

            final List<DroitInspecteurDTO> liste = form.getDroitsInspecteur();
            DroitInspecteurDTO elemToRemove = null;
            for (DroitInspecteurDTO elem : liste) {
                if (elem.getEnseignant().getId().equals(enseignant.getId()) &&
                        elem.getInspecteur().getIdentifiant().equals(inspecteur.getIdentifiant()) &&
                        elem.getIdEtablissement().equals(idEtablissement)) {
                    elemToRemove = elem;
                    break;
                }
            }
            liste.remove(elemToRemove);
        } catch (MetierException e) {
            log.debug("{0}", e.getMessage());
        }
    }

    /**
     * Declenchee lors de l'ajout d'un nouveau inspecteur / enseignant. 
     * Provoque l'ajout de la popup.
     */
    public void initNouveauDroit() {
    }
    
    
    /**
     * Recherche si le couple enseignant / inspecteur est present dans la liste courante.
     * @param inspecteur l'inspecteur
     * @param enseignant l'enseignant
     * @return la ligne de droit correspondante
     */
    private DroitInspecteurDTO chercheCoupleInspecteurEnseignant(UserDTOForList inspecteur, EnseignantDTO enseignant) {
        if (inspecteur==null || enseignant==null) { return null; }
        for (final DroitInspecteurDTO droit : form.getDroitsInspecteur()) {
            if (droit.getInspecteur().getIdentifiant().equals(inspecteur.getIdentifiant()) &&
                droit.getEnseignant().getId().equals(enseignant.getId())) {
                return droit;
            }
        }
        return null;
    }
    
    /**
     * Ajoute les droits pour un enseignant et un inspecteur (sans la sauvegarde).
     */
    public void ajoutDroitIHM() {
        
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();

        final UserDTOForList directeur = new UserDTOForList();
        directeur.setIdentifiant(utilisateurDTO.getUserDTO().getUid());
        directeur.setNom(utilisateurDTO.getUserDTO().getNom() + " " +utilisateurDTO.getUserDTO().getPrenom());
        


        for (EnseignantDTO enseignant : form.getEnseignants()){
            if (BooleanUtils.isTrue(enseignant.getVraiOuFauxSelectionne())) {
                for (UserDTOForList inspecteur : form.getInspecteurs()){
                    if (BooleanUtils.isTrue(inspecteur.getVraiOuFauxSelectionne())){
                        if (chercheCoupleInspecteurEnseignant(inspecteur, enseignant) == null) {
                            
                            final DroitInspecteurDTO nouveauDroit = new DroitInspecteurDTO();
                            nouveauDroit.setDateDebut(new Date());
                            nouveauDroit.setDateFin(null);
                            nouveauDroit.setDirecteur(directeur);
                            nouveauDroit.setEnseignantDTO(enseignant);
                            nouveauDroit.setEstModifiee(true);
                            nouveauDroit.setIdEtablissement(utilisateurDTO.getIdEtablissement());
                            nouveauDroit.setInspecteur(inspecteur);
                            nouveauDroit.setEstAjoute(true);
                            form.getDroitsInspecteur().add(nouveauDroit);
                        }
                    }
                }
            }
        }
        
        for (EnseignantDTO user : form.getEnseignants()){
            user.setVraiOuFauxSelectionne(false);
        }
        
        for (UserDTOForList user : form.getInspecteurs()){
            user.setVraiOuFauxSelectionne(false);
        }
    }   
    
    /**
     * Enregistre toutes les moidification, suppression, ajout effectuee sur la liste depuis le dernier chargement.
     */
    public void sauver() {
        try {
            inspectionService.saveDroitsListe(form.getDroitsInspecteur());
            rechercheDroits();
        } catch (MetierException e) {
            log.debug("{0}", e.getMessage());
        }        
    }
    
    /**
     * Ajoute les droits pour un enseignant et un inspecteur.
     */
    public void ajoutDroit() {
        final SaveDroitInspecteurQO saveDroitInspecteurQO = new SaveDroitInspecteurQO();
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        
        saveDroitInspecteurQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
        
        final List<EnseignantDTO> listeEnseignants = new ArrayList<EnseignantDTO>();
        for (EnseignantDTO user : form.getEnseignants()){
            if (BooleanUtils.isTrue(user.getVraiOuFauxSelectionne())) {
                listeEnseignants.add(user);
            }
        }
        saveDroitInspecteurQO.setEnseignantsSelectionne(listeEnseignants);
        final List<UserDTOForList> listeInspecteurs = new ArrayList<UserDTOForList>();
        for (UserDTOForList user : form.getInspecteurs()){
            if (BooleanUtils.isTrue(user.getVraiOuFauxSelectionne())){
                listeInspecteurs.add(user);
            }
        }
        saveDroitInspecteurQO.setInspecteursSelectionne(listeInspecteurs);
        
        final UserDTOForList directeur = new UserDTOForList();
        directeur.setIdentifiant(utilisateurDTO.getUserDTO().getUid());
        directeur.setNom(utilisateurDTO.getUserDTO().getNom() + " " +utilisateurDTO.getUserDTO().getPrenom());
        saveDroitInspecteurQO.setDirecteur(directeur);
        
        saveDroitInspecteurQO.setVraiOuFauxAjout(true);

        try {
            inspectionService.saveDroit(saveDroitInspecteurQO);
            rechercheDroits();
        } catch (MetierException e) {
            log.debug("{0}", e.getMessage());
        }
        
        for (EnseignantDTO user : form.getEnseignants()){
            user.setVraiOuFauxSelectionne(false);
        }
        
        for (UserDTOForList user : form.getInspecteurs()){
            user.setVraiOuFauxSelectionne(false);
        }
    }
    
    /**
     * Declenchee suite à la modification d'une date de debut ou fin sur une ligne.
     */
    public void refreshDate() {
        if (form.getDroitSelectionne() == null) { return; }
        form.getDroitSelectionne().setEstModifiee(true);
    }
    
    public void refreshListing() {
        
        
    }
}
