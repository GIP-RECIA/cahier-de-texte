/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequencePrintControl.java,v 1.10 2010/06/01 13:45:24 ent_breyton Exp $
 */

package org.crlr.web.application.control;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang.BooleanUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.base.EnseignantsClasseGroupeQO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.services.GroupeClasseService;
import org.crlr.services.SequenceService;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.form.ClasseGroupeForm;
import org.crlr.web.application.form.EnseignantForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.springframework.util.CollectionUtils;

/**
 * Sous controleur pour l'IHM de sélection de classe / groupe.
 * 
 */
@ManagedBean(name = "enseignant")
@ViewScoped
public class EnseignantControl extends AbstractControl<EnseignantForm> {
    
    @ManagedProperty(value="#{sequenceService}")
    protected transient SequenceService sequenceService;
    
    @ManagedProperty(value="#{groupeClasseService}")
    protected transient GroupeClasseService groupeClasseService;
    
    /**
     * 
     */
    public EnseignantControl() {
        super(new EnseignantForm());
        listener = null;
    }

    


    
    private EnseignantListener listener;

    
 
    /**
     * @author G-CG34-FRMP
     *
     */
    public static interface EnseignantListener {
        /**
         * Déclencher après la radio bouton classe / groupe est cliqué.
         *  Methode declenchee quand on clique sur le type class ou groupe pour
         * reinitialiser l'input.
         * @param tous vrai si tous est sélectionné
         */
        public void enseignantTousOuUnSelectionne(boolean tous);

        /**
         * Déclencher après un enseignant est selectionné.
         */
        public void enseignantSelectionnee();
    }
    
    /**
     * Réinitialisation.
     * @param ev ev
     */
    public void filtreParEnseignantSelectionne(javax.faces.event.AjaxBehaviorEvent ev) {
        log.info("filtreParEnseignantSelectionne {0}", form.getFiltreParEnseignant());
        
        if (BooleanUtils.isFalse(getForm().getFiltreParEnseignant())) {
            //Tous
            getForm().setEnseignantSelectionne(null);
            
        } else {
            //Un enseignant
            final List<EnseignantDTO> liste = form.getListeEnseignant();
            if(!org.apache.commons.collections.CollectionUtils.isEmpty(liste)) {
                form.setEnseignantSelectionne(liste.get(0));
                selectionnerEnseignant();
            } 
            
            final Integer idUser =
                    ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO().getIdentifiant();

                
                if(!CollectionUtils.isEmpty(liste)) {
                    boolean trouve = false;

                    for (final EnseignantDTO dto : liste) {
                        if (dto.getId().equals(idUser)) {
                            form.setEnseignantSelectionne(dto);
                            trouve = true;
                            break;
                        }
                    }

                    if (!trouve) {
                        form.setEnseignantSelectionne(liste.get(0));
                    }
                } 
        }
        if (listener != null) {
            listener.enseignantTousOuUnSelectionne(
                    BooleanUtils.isFalse(getForm().getFiltreParEnseignant()));
        }
    }

    /**
     * Clone la valeur de l'objet enseignant de la recherche.
     */
    public void selectionnerEnseignant() {
        form.setEnseignantSelectionne(ObjectUtils.clone(form.getEnseignantSelectionne()));
        form.setFiltreEnseignant("");
                
        if (listener != null) {
            listener.enseignantSelectionnee();
        }
        

    }
    
    /**
     * Appel métier de recherche des enseignants.
     
     * @param classeGroupeForm c
     * @param enseignement peut être null
     */
    public void chargerListeEnseignant(ClasseGroupeForm classeGroupeForm,
            EnseignementDTO enseignement
            ) {
       
        GroupesClassesDTO groupeClasseSelectionne = classeGroupeForm.getGroupeClasseSelectionne();
        List<GroupeDTO> listeGroupes = classeGroupeForm.getListeGroupe();
        TypeGroupe typeGroupeSelectionne = classeGroupeForm.getTypeGroupeSelectionne();
        
        Integer idEnseignement = enseignement == null ? null : enseignement.getId();
        
        Profil profil = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getProfil();
        
        boolean affichageParentEleve = Profil.ELEVE.equals(profil) || Profil.PARENT.equals(profil);
        
        if (groupeClasseSelectionne == null) {
            form.setListeEnseignant(new ArrayList<EnseignantDTO>());
            return;
        }
        
        if (affichageParentEleve){
            final Integer idClasse = groupeClasseSelectionne.getId();
            
            final Set<Integer> idsGroupe = new HashSet<Integer>();
            for  (GroupeDTO groupe : listeGroupes) {
                idsGroupe.add(groupe.getId());
            }
            
           
            
            final List<EnseignantDTO> liste = sequenceService.findEnseignantsEleve(idClasse,idsGroupe, idEnseignement);
            form.setListeEnseignant(liste);

        } else {
            
            final Integer idGroupeClasse = groupeClasseSelectionne.getId();
            
            List<EnseignantDTO> liste = new ArrayList<EnseignantDTO>();
            
            if (idGroupeClasse != null) {
                final EnseignantsClasseGroupeQO enseignantsClasseGroupeQO = new EnseignantsClasseGroupeQO();
                
                final UtilisateurDTO utilisateurDTO =
                    ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
                enseignantsClasseGroupeQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
                
                if( idEnseignement != null ) {
                    enseignantsClasseGroupeQO.setIdEnseignement(idEnseignement);
                }
                
                if (profil == Profil.INSPECTION_ACADEMIQUE){
                    enseignantsClasseGroupeQO.setIdInspecteur(utilisateurDTO.getUserDTO().getIdentifiant());
                 } 
                
                enseignantsClasseGroupeQO.setIdGroupeClasse(idGroupeClasse);
    
                if (TypeGroupe.CLASSE  == typeGroupeSelectionne) {
                    liste = groupeClasseService.findEnseignantsClasse(enseignantsClasseGroupeQO);
                } else {
                    liste = groupeClasseService.findEnseignantsGroupe(enseignantsClasseGroupeQO);
                }
    
                form.setListeEnseignant(liste);
            }
            
            log.info("Enseignants trouvé {0}", liste.size());
        }
    
    }
    
    
    
    
    /**
     * 
     */
    @PostConstruct
    public void onLoad() {
        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();

        form.setProfil(utilisateurDTO.getProfil());
    }

    
    /**
     * @return the listener
     */
    public EnseignantListener getListener() {
        return listener;
    }

    /**
     * @param listener
     *            the listener to set
     */
    public void setListener(EnseignantListener listener) {
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
     * @param groupeClasseService the groupeClasseService to set
     */
    public void setGroupeClasseService(GroupeClasseService groupeClasseService) {
        this.groupeClasseService = groupeClasseService;
    }

  

   

}