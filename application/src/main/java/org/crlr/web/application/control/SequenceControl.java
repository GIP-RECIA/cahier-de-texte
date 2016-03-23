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
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.sequence.RechercheSequencePopupQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.SequenceService;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.form.SequenceForm;
import org.crlr.web.contexte.utils.ContexteUtils;

/**
 * Sous controleur pour l'IHM de sélection de classe / groupe.
 * 
 */
@ManagedBean(name = "sequenceControl")
@ViewScoped
public class SequenceControl extends AbstractControl<SequenceForm> {
    
    @ManagedProperty(value="#{sequenceService}")
    protected transient SequenceService sequenceService;
    
    
    /**
     * 
     */
    public SequenceControl() {
        super(new SequenceForm());
        listener = null;
    }



    
    private SequenceListener listener;

    
 
    /**
     * @author G-CG34-FRMP
     *
     */
    public static interface SequenceListener {
        /**
         * Déclencher après la radio bouton classe / groupe est cliqué.
         *  Methode declenchee quand on clique sur le type class ou groupe pour
         * reinitialiser l'input.
         * @param tous vrai si tous est sélectionné
         */
        public void sequenceTousOuUnSelectionne(boolean tous);

        /**
         * Déclencher après un sequence est selectionné.
         */
        public void sequenceSelectionnee();
    }
    
    /**
     * Réinitialisation.
     * @param ev ev
     */
    public void filtreParSequenceSelectionne(javax.faces.event.AjaxBehaviorEvent ev) {
        
        if (BooleanUtils.isTrue(getForm().getTousSelectionne())) {
            //Tous
            getForm().setSequenceSelectionnee(null);
            
        } else {
            //Un sequence
            final List<SequenceDTO> liste = form.getListeSequence();
            if(!org.apache.commons.collections.CollectionUtils.isEmpty(liste)) {
                form.setSequenceSelectionnee(liste.get(0));
                selectionnerSequence();
            } 
        }
        if (listener != null) {
            listener.sequenceTousOuUnSelectionne(
                    BooleanUtils.isTrue(getForm().getTousSelectionne()));
        }
    }

    /**
     * Clone la valeur de l'objet sequence de la recherche.
     */
    public void selectionnerSequence() {
        form.setSequenceSelectionnee(ObjectUtils.clone(form.getSequenceSelectionnee()));
                
        if (listener != null) {
            listener.sequenceSelectionnee();
        }
        

    }
    
    /**
     * Appel métier de recherche des sequences.
     *
     * @param enseignement p
     * @param enseignant p
     * @param classeGroupe p
     */
    public void chargerListeSequence(
            EnseignementDTO enseignement,
            EnseignantDTO enseignant,
            GroupesClassesDTO classeGroupe
            
            ) {
       
        final RechercheSequencePopupQO rechercheSequencePopupQO = new RechercheSequencePopupQO(); 
        List<SequenceDTO> liste = new ArrayList<SequenceDTO>();
            try {
                
                final UtilisateurDTO utilisateurDTO =
                    ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
                rechercheSequencePopupQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
                rechercheSequencePopupQO.setIdAnneeScolaire(utilisateurDTO.getAnneeScolaireDTO().getId());

                // Positionne dans les critères de recherche l'id de l'enseignement choisi
                if (enseignement != null) {
                    rechercheSequencePopupQO.setIdEnseignement(enseignement.getId());
                }
                
                if (enseignant != null ) {
                    rechercheSequencePopupQO.setIdEnseignant(enseignant.getId());
                }
                
                if (classeGroupe != null) {
                    rechercheSequencePopupQO.setIdClasseGroupe(classeGroupe.getId());
                    rechercheSequencePopupQO.setTypeGroupeClasse(classeGroupe.getTypeGroupe());
                }
                
                
                final ResultatDTO<List<SequenceDTO>> listeSequenceDTO =
                    this.sequenceService.findSequencePopup(rechercheSequencePopupQO);
                liste = ObjectUtils.clone(listeSequenceDTO.getValeurDTO());
            } catch (MetierException e) {
                log.debug("{0}", e.getMessage());
            }
        
            form.setListeSequence(liste);
        
    
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
    public SequenceListener getListener() {
        return listener;
    }

    /**
     * @param listener
     *            the listener to set
     */
    public void setListener(SequenceListener listener) {
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

    

}