/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequencePrintControl.java,v 1.10 2010/06/01 13:45:24 ent_breyton Exp $
 */

package org.crlr.web.application.control;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang.BooleanUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.ArchiveEnseignantDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.ArchiveEnseignantService;
import org.crlr.services.EnseignementService;
import org.crlr.services.SequenceService;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.form.EnseignementForm;
import org.crlr.web.contexte.utils.ContexteUtils;

/**
 * Sous controleur pour l'IHM de sélection de classe / groupe.
 * 
 */
@ManagedBean(name = "enseignement")
@ViewScoped
public class EnseignementControl extends AbstractControl<EnseignementForm> {
    
    @ManagedProperty(value="#{sequenceService}")
    protected transient SequenceService sequenceService;
    
    /** groupeClasseService. */
    @ManagedProperty(value="#{enseignementService}")
    protected transient EnseignementService enseignementService;
    
   
    
    private ArchiveEnseignantDTO archiveEnseignantDTO;
    /**
     * 
     */
    public EnseignementControl() {
        super(new EnseignementForm());
        listener = null;
    }



    
    private EnseignementListener listener;

    
 
    /**
     * @author G-CG34-FRMP
     *
     */
    public static interface EnseignementListener {
        /**
         * Déclencher après la radio bouton classe / groupe est cliqué.
         *  Methode declenchee quand on clique sur le type class ou groupe pour
         * reinitialiser l'input.
         * @param tous vrai si tous est sélectionné
         */
        public void enseignementTousOuUnSelectionne(boolean tous);

        /**
         * Déclencher après un enseignement est selectionné.
         */
        public void enseignementSelectionnee();
    }
    
    /**
     * Réinitialisation.
     * @param ev ev
     */
    public void filtreParEnseignementSelectionne(javax.faces.event.AjaxBehaviorEvent ev) {
        log.info("filtreParEnseignementSelectionne {}", form.getFiltreParEnseignement());
        
        if (BooleanUtils.isFalse(getForm().getFiltreParEnseignement())) {
            //Tous
            getForm().setEnseignementSelectionne(null);
            
        } else {
            //Un enseignement
            final List<EnseignementDTO> liste = form.getListeEnseignement();
            if(!org.apache.commons.collections.CollectionUtils.isEmpty(liste)) {
                form.setEnseignementSelectionne(liste.get(0));
                selectionnerEnseignement();
            } 
        }
        if (listener != null) {
            listener.enseignementTousOuUnSelectionne(
                    BooleanUtils.isFalse(getForm().getFiltreParEnseignement()));
        }
    }

    /**
     * Clone la valeur de l'objet enseignement de la recherche.
     */
    public void selectionnerEnseignement() {
        form.setEnseignementSelectionne(ObjectUtils.clone(form.getEnseignementSelectionne()));
        form.setFiltreEnseignement("");
                
        if (listener != null) {
            listener.enseignementSelectionnee();
        }
        

    }
    
    /**
     * Appel métier de recherche des enseignements.
     *
     * @param groupeClasseSelectionne le groupe / classe
     * @param typeGroupeSelectionne le choix de radio, classe ou groupe
     * @param affichageParentEleve true si le profile est parent ou éléve
     * @param modeArchive si c'est de la mode archive
     * @param exercice l'exercice pour modeArchive == true, peut import la valeur sinon
     * @param listeGroupes la liste de groupe sélectionné avec la classe.
     */   
    public void chargerListeEnseignement(GroupesClassesDTO groupeClasseSelectionne,
            TypeGroupe typeGroupeSelectionne,
            boolean affichageParentEleve,
            boolean modeArchive,
            String exercice,
            List<GroupeDTO> listeGroupes
            ) {
       
        if (groupeClasseSelectionne == null) {
            form.setListeEnseignement(new ArrayList<EnseignementDTO>());
            return;
        }
        
        if (affichageParentEleve){
            final Integer idEtablissement = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getIdEtablissement();
            final RechercheEnseignementPopupQO rechercheEnseignementPopupQO =
                new RechercheEnseignementPopupQO();
            rechercheEnseignementPopupQO.setGroupeClasseSelectionne(groupeClasseSelectionne);
            rechercheEnseignementPopupQO.setGroupesSelectionne(listeGroupes);
            rechercheEnseignementPopupQO.setIdEtablissement(idEtablissement);
            
            final List<EnseignementDTO> liste = sequenceService.findEnseignementsEleve(rechercheEnseignementPopupQO);
            form.setListeEnseignement(liste);
        } else {
            form.setEnseignementSelectionne(new EnseignementDTO());
            List<EnseignementDTO> liste = new ArrayList<EnseignementDTO>();
    
            final RechercheEnseignementPopupQO rechercheEnseignementPopupQO =
                new RechercheEnseignementPopupQO();
    
            final UtilisateurDTO utilisateurDTO =
                ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
            
            
            //On set l'id pour les enseignants uniquement.
            if (Profil.ENSEIGNANT.equals(utilisateurDTO.getProfil())) {
            	
            	Integer idEns = null;
              
               if (modeArchive) {
            	   idEns = archiveEnseignantDTO.getIdEnseignantSelected();
               } else {
            	   idEns = utilisateurDTO.getUserDTO().getIdentifiant();
               }
              
            	   rechercheEnseignementPopupQO.setIdEnseignant(idEns);
              
            }
            rechercheEnseignementPopupQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
            rechercheEnseignementPopupQO.setTypeGroupeSelectionne(typeGroupeSelectionne);
            rechercheEnseignementPopupQO.setGroupeClasseSelectionne(groupeClasseSelectionne);
    
            if (modeArchive) {
            	rechercheEnseignementPopupQO.setArchive(true);
                rechercheEnseignementPopupQO.setExercice(exercice);
                log.debug("idEtablissement = {}" ,rechercheEnseignementPopupQO.getIdEtablissement() );
                if (archiveEnseignantDTO == null) {
                	//archiveEnseignantDTO = archiveEnseignantService.
                	
                	log.debug("archiveEnseignantDTO est null");
                	
                } 
               // findIdEtablissementParCode
                else {
                	rechercheEnseignementPopupQO.setIdEtablissement(archiveEnseignantDTO.getIdEtablissementSelected());
                }
            }
            
            liste = this.getListeEnseignementPopup(rechercheEnseignementPopupQO);      
    
            form.setListeEnseignement(liste);
        }
    
    }
    
    public void chargerListeEnseignementDeEnseignant(
            Integer idEtablissement,
            Integer idEnseignant
            ) {
        final RechercheEnseignementPopupQO rechercheEnseignementPopupQO =
                new RechercheEnseignementPopupQO();

            final UtilisateurDTO utilisateurDTO =
                ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
            rechercheEnseignementPopupQO.setIdEnseignant(idEnseignant);

            rechercheEnseignementPopupQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());

            form.setListeEnseignement(this.getListeEnseignementPopup(rechercheEnseignementPopupQO));
    }
    
    /**
     * Retourne la liste des enseignement pour la popup en fonction des paramètres.
     *
     * @param rechercheEnseignementPopupQO RechercheEnseignementPopupQO
     *
     * @return La liste
     */
    public List<EnseignementDTO> getListeEnseignementPopup(RechercheEnseignementPopupQO rechercheEnseignementPopupQO) {
        List<EnseignementDTO> liste = new ArrayList<EnseignementDTO>();
            try {
                final UtilisateurDTO utilisateurDTO =
                    ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
                if (utilisateurDTO.getProfil().equals(Profil.INSPECTION_ACADEMIQUE)){
                    rechercheEnseignementPopupQO.setIdInspecteur(utilisateurDTO.getUserDTO().getIdentifiant());
                } 
                log.debug("243 getListeEnseignementPopup >");
                final ResultatDTO<List<EnseignementDTO>> listeEnseignementDTO =
                    this.enseignementService.findEnseignementPopup(rechercheEnseignementPopupQO);
                liste = ObjectUtils.clone(listeEnseignementDTO.getValeurDTO());
                log.debug("< 247 getListeEnseignementPopup ");
            } catch (MetierException e) {
                log.debug("{}", e.getMessage());
            }
        
        return liste;
    }
    /**
     * 
     */
    @PostConstruct
    public void onLoad() {

       
    }

    
    /**
     * @return the listener
     */
    public EnseignementListener getListener() {
        return listener;
    }

    /**
     * @param listener
     *            the listener to set
     */
    public void setListener(EnseignementListener listener) {
        this.listener = listener;
    }



    /**
     * @return the sequenceService
     */
    public SequenceService getSequenceService() {
        return sequenceService;
    }

    /**
     * @param sequenceService the sequenceService to set
     */
    public void setSequenceService(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    /**
     * @return the enseignementService
     */
    public EnseignementService getEnseignementService() {
        return enseignementService;
    }

    /**
     * @param enseignementService the enseignementService to set
     */
    public void setEnseignementService(EnseignementService enseignementService) {
        this.enseignementService = enseignementService;
    }

	public ArchiveEnseignantDTO getArchiveEnseignantDTO() {
		return archiveEnseignantDTO;
	}

	public void setArchiveEnseignantDTO(ArchiveEnseignantDTO archiveEnseignantDTO) {
		this.archiveEnseignantDTO = archiveEnseignantDTO;
	}

}