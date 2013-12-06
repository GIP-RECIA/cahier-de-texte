/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PrefCahierTexteControl.java,v 1.9 2009/07/10 15:32:06 vibertd Exp $
 */

package org.crlr.web.application.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.UserDTO;
import org.crlr.dto.application.base.ElevesParentDTO;
import org.crlr.dto.application.base.EnfantDTO;
import org.crlr.dto.application.base.EtablissementAccessibleQO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.TypeReglesCahierArchive;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.Message;
import org.crlr.services.ConfidentialiteService;
import org.crlr.utils.BooleanUtils;
import org.crlr.utils.DateUtils;
import org.crlr.web.application.form.PrefCahierTexteForm;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.utils.MessageUtils;

/**
 * PrefControl.
 * 
 * @author breytond
 * @version $Revision: 1.7 $
 */
@ManagedBean(name = "prefCahierTexte")
@ViewScoped
public class PrefCahierTexteControl extends
        AbstractControl<PrefCahierTexteForm> {

    /** service de confidentialité. */
    @ManagedProperty(value = "#{confidentialiteService}")
    private transient ConfidentialiteService confidentialiteService;

    /**
     * Mutateur de confidentialiteService {@link #confidentialiteService}.
     * 
     * @param confidentialiteService
     *            le confidentialiteService to set
     */
    public void setConfidentialiteService(
            ConfidentialiteService confidentialiteService) {
        this.confidentialiteService = confidentialiteService;
    }
    
    /**
     * Constructeur.
     */
    public PrefCahierTexteControl() {
        super(new PrefCahierTexteForm());
    }
    
    /**
     * Sélectionne un enfant parmis la liste.
     */
    public void selectionnerEnfant() {
        final ContexteUtilisateur contexteUtilisateur = ContexteUtils
                .getContexteUtilisateur();
        contexteUtilisateur.getUtilisateurDTO().getUserDTO().setIdentifiant(
                form.getEnfantSelectionne().getId());
    }
    
    /**
     * Mise à jour et Sélection.
     * 
     * @param listeEtablissement
     *            la liste des établissements.
     * @param ctxUtilisateur
     *            le contexte utilisateur.
     */
    private void miseAjourListeEtablissementEtSelectionInitiale(
            final List<EtablissementDTO> listeEtablissement,
            final UtilisateurDTO ctxUtilisateur) {
        form.setListeEtablissement(listeEtablissement);
        if (!CollectionUtils.isEmpty(form.getListeEtablissement())) {
            if (ctxUtilisateur.getCodeEtablissementSelectionneInspecteur() != null) {
                form.getEtablissementSelectionne().setCode(
                        ctxUtilisateur
                                .getCodeEtablissementSelectionneInspecteur());
            } else {
                // on choisit l'établissement par défaut.
                for (final EtablissementDTO etablissementDTO : form
                        .getListeEtablissement()) {
                    if (ObjectUtils.equals(ctxUtilisateur.getIdEtablissement(),
                            etablissementDTO.getId())) {
                        form.getEtablissementSelectionne().setCode(
                                etablissementDTO.getCode());
                        break;
                    }
                }
            }
            selectionnerEtablissement();
        }
    }
    
    /**
     * Sélectionne un etablissement parmis la liste.
     */
    public String selectionnerEtablissement() {
        Integer idEtablissementSchemaCourant = null;
        String designation = "";
        String joursOuvres = "";
        Boolean vraiOuFauxOuvert = null;
        Boolean vraiOuFauxEtabSaisieSimplifiee = null;
        for (final EtablissementDTO etablissementDTO : form
                .getListeEtablissement()) {
            if (etablissementDTO.getCode().equals(
                    form.getEtablissementSelectionne().getCode())) {
                if (etablissementDTO.getId() != null) {
                    idEtablissementSchemaCourant = etablissementDTO.getId();
                    designation = etablissementDTO.getDesignation();
                    joursOuvres = etablissementDTO.getJoursOuvres();
                    vraiOuFauxOuvert = etablissementDTO.getVraiOuFauxOuvert();
                    
                    // Charge la saisie simplifiée 
                    // (qui n'a pas ete effectuée dans le cas d'un enseignant qui est aussi amdin central Cahier de texte)
                    vraiOuFauxEtabSaisieSimplifiee = etablissementDTO.getVraiOuFauxSaisieSimplifiee();
                    if (vraiOuFauxEtabSaisieSimplifiee == null) {
                        final UtilisateurDTO utilisateur = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
                        if (Profil.ENSEIGNANT.equals(utilisateur.getProfil())) {
                            final EtablissementAccessibleQO etablissementAccessibleQO = new EtablissementAccessibleQO();
                            etablissementAccessibleQO.setIdEnseignant(utilisateur.getUserDTO().getIdentifiant());
                            final List<String> listeSiren = new ArrayList<String>();
                            listeSiren.add(etablissementDTO.getCode());
                            etablissementAccessibleQO.setListeSiren(listeSiren);
                            ResultatDTO<List<EtablissementDTO>> resultat = 
                                confidentialiteService.findListeEtablissementEnseignant(etablissementAccessibleQO);
                            if (resultat.getValeurDTO() != null && !resultat.getValeurDTO().isEmpty()) {
                                vraiOuFauxEtabSaisieSimplifiee = resultat.getValeurDTO().get(0).getVraiOuFauxSaisieSimplifiee();
                                etablissementDTO.setVraiOuFauxSaisieSimplifiee(vraiOuFauxEtabSaisieSimplifiee);
                            }
                        }
                    }

                    form.getEtablissementSelectionne().setDesignation(
                            designation);
                }
                break;
            }
        }

        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();
        final Profil profilUser = utilisateurDTO.getProfil();
        if ((Profil.INSPECTION_ACADEMIQUE.equals(profilUser) || Profil.AUTRE
                .equals(profilUser))
                && (idEtablissementSchemaCourant == null)) {
            final EtablissementDTO etablissementDTO = form
                    .getListeEtablissement().get(0);
            form.setEtablissementSelectionne(etablissementDTO);
            idEtablissementSchemaCourant = etablissementDTO.getId();
            designation = etablissementDTO.getDesignation();
            joursOuvres = etablissementDTO.getJoursOuvres();
            vraiOuFauxOuvert = etablissementDTO.getVraiOuFauxOuvert();
            vraiOuFauxEtabSaisieSimplifiee = etablissementDTO
                    .getVraiOuFauxSaisieSimplifiee();
        }

        if (idEtablissementSchemaCourant != null) {
            utilisateurDTO.setIdEtablissement(idEtablissementSchemaCourant);
            utilisateurDTO.setDesignationEtablissement(designation);
            utilisateurDTO.setJoursOuvresEtablissement(joursOuvres);
            utilisateurDTO.setVraiOuFauxCahierOuvertEtab(vraiOuFauxOuvert);
            utilisateurDTO
                    .setVraiOuFauxEtabSaisieSimplifiee(vraiOuFauxEtabSaisieSimplifiee);
            utilisateurDTO.setArchiveUniquementDisponible(false);
            utilisateurDTO.setSirenEtablissement(form
                    .getEtablissementSelectionne().getCode());
        } else {
            utilisateurDTO.setArchiveUniquementDisponible(true);
            MessageUtils.addMessage(new Message(
                    TypeReglesCahierArchive.ARCHIVE_04.name(), DateUtils
                            .format(DateUtils.getAujourdhui())), this
                    .getClass());
        }

        utilisateurDTO.setCodeEtablissementSelectionneInspecteur(form
                .getEtablissementSelectionne().getCode());
        this.form.setFiltreEtablissement("");
        
        final MenuControl menuControl = ContexteUtils.getMenuControl();

        // réinitialise le menu
        menuControl.init();
        
        return "PREFERENCE_CAHIER@menu@arbre";
    }

    /**
     * Onload.
     */
    @PostConstruct
    public void onLoad() {

        // Charge les preference de l'utilisateur
        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();
        final UtilisateurDTO ctxUtilisateur = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();
        final UserDTO userDTO = ctxUtilisateur.getUserDTO();

        // PARENT : Charge / Sélection de l'enfant en cours
        if (Profil.PARENT.equals(utilisateurDTO.getProfil())) {
            final ResultatDTO<ElevesParentDTO> resultatDTO = confidentialiteService
                    .findEleveDuParent(ctxUtilisateur.getListeUidEnfant());

            final Set<UserDTO> listeEnfant = resultatDTO.getValeurDTO()
                    .getListeEnfant();

            if (!CollectionUtils.isEmpty(listeEnfant)) {
                // Si la liste est supérieure à 1, dans ce cas l'utilisateur a
                // le choix entre les enfants.

                if (listeEnfant.size() > 1) {
                    final ContexteUtilisateur contexteUtilisateur = ContexteUtils
                            .getContexteUtilisateur();
                    Integer id = contexteUtilisateur.getUtilisateurDTO()
                            .getUserDTO().getIdentifiant();

                    for (Iterator<UserDTO> it = resultatDTO.getValeurDTO()
                            .getListeEnfant().iterator(); it.hasNext();) {
                        UserDTO enfant = it.next();
                        EnfantDTO enfantDTO = new EnfantDTO();
                        enfantDTO.setId(enfant.getIdentifiant());
                        enfantDTO.setNom(enfant.getNom());
                        enfantDTO.setPrenom(enfant.getPrenom());
                        form.getListeEnfant().add(enfantDTO);
                        if (enfantDTO.getId().equals(id)) {
                            form.setEnfantSelectionne(enfantDTO);
                        } else {
                            form.setEnfantSelectionne(form.getListeEnfant()
                                    .iterator().next());
                        }

                    }
                } else {
                    final UserDTO enfant = listeEnfant.iterator().next();
                    EnfantDTO enfantDTO = new EnfantDTO();
                    enfantDTO.setId(enfant.getIdentifiant());
                    enfantDTO.setNom(enfant.getNom());
                    enfantDTO.setPrenom(enfant.getPrenom());
                    form.setEnfantSelectionne(enfantDTO);
                }
                selectionnerEnfant();
            }

            // Inspection académique
        } else if (Profil.INSPECTION_ACADEMIQUE.equals(utilisateurDTO
                .getProfil())
                || BooleanUtils
                        .isTrue(ctxUtilisateur.getVraiOuFauxAdmCentral())) {
            try {
                final ResultatDTO<List<EtablissementDTO>> resultatEtablissement = confidentialiteService
                        .findListeEtablissement();
                miseAjourListeEtablissementEtSelectionInitiale(
                        resultatEtablissement.getValeurDTO(), ctxUtilisateur);
            } catch (MetierException e) {
                log
                        .debug("Erreur du changement de la liste des établissement du profil Inspecteur académique");
            }
            // Autres profils
        } else if (Profil.DIRECTION_ETABLISSEMENT.equals(utilisateurDTO
                .getProfil())
                || Profil.DOCUMENTALISTE.equals(utilisateurDTO.getProfil())
                || Profil.ENSEIGNANT.equals(utilisateurDTO.getProfil())
                || Profil.AUTRE.equals(utilisateurDTO.getProfil())) {
            try {
                final ResultatDTO<List<EtablissementDTO>> resultatEtablissement;
                if (BooleanUtils.isTrue(ctxUtilisateur
                        .getVraiOuFauxAdmRessourceENT())) {
                    if (Profil.ENSEIGNANT.equals(utilisateurDTO.getProfil())) {
                        final EtablissementAccessibleQO etablissementAccessibleQO = new EtablissementAccessibleQO();
                        etablissementAccessibleQO
                                .setListeSiren(new ArrayList<String>(
                                        ctxUtilisateur.getSirensEtablissement()));
                        etablissementAccessibleQO.setIdEnseignant(userDTO
                                .getIdentifiant());
                        resultatEtablissement = confidentialiteService
                                .findListeEtablissementEnseignantAdmRessources(etablissementAccessibleQO);
                    } else {
                        resultatEtablissement = confidentialiteService
                                .findListeEtablissement();
                    }
                } else {
                    final EtablissementAccessibleQO etablissementAccessibleQO = new EtablissementAccessibleQO();
                    etablissementAccessibleQO
                            .setListeSiren(new ArrayList<String>(ctxUtilisateur
                                    .getSirensEtablissement()));
                    
                    // Ajoute les siren pour lesquels l'user est admin local
                    for (final Iterator<String> i = ctxUtilisateur.getAdminLocalSiren().iterator(); i.hasNext(); ) {
                        final String siren = i.next(); 
                        if (!etablissementAccessibleQO.getListeSiren().contains(siren)) {
                            etablissementAccessibleQO.getListeSiren().add(siren);
                        }
                    }
                    // Ajoute les siren pour lesquels l'user est admin de ressource cahier de texte
                    for (final Iterator<String> i = ctxUtilisateur.getAdminRessourceSiren().iterator(); i.hasNext(); ) {
                        final String siren = i.next(); 
                        if (!etablissementAccessibleQO.getListeSiren().contains(siren)) {
                            etablissementAccessibleQO.getListeSiren().add(siren);
                        }
                    }
                    
                    etablissementAccessibleQO.setIdEnseignant(Profil.ENSEIGNANT
                            .equals(utilisateurDTO.getProfil()) ? userDTO
                            .getIdentifiant() : null);

                    resultatEtablissement = confidentialiteService
                            .findListeEtablissementEnseignant(etablissementAccessibleQO);
                }

                miseAjourListeEtablissementEtSelectionInitiale(
                        resultatEtablissement.getValeurDTO(), ctxUtilisateur);
            } catch (MetierException e) {
                log
                        .debug("Erreur du changement de la liste des établissement du profil Inspecteur académique");
            }
        }

    }
}
