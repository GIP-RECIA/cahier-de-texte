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
import javax.faces.event.AjaxBehaviorEvent;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.RechercheGroupeClassePopupQO;
import org.crlr.dto.application.base.RechercheGroupeQO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.EtablissementService;
import org.crlr.services.GroupeClasseService;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.form.ClasseGroupeForm;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.contexte.utils.ContexteUtils;

/**
 * @author G-CG34-FRMP
 * 
 */
@ManagedBean(name = "classeGroupe")
@ViewScoped
public class ClasseGroupeControl extends AbstractControl<ClasseGroupeForm> {
    /**
     * 
     */
    public ClasseGroupeControl() {
        super(new ClasseGroupeForm());
        listener = null;
    }

    /** groupeClasseService. */
    @ManagedProperty(value = "#{groupeClasseService}")
    protected transient GroupeClasseService groupeClasseService;

    /** Service etablissement. */
    @ManagedProperty(value="#{etablissementService}")
    private transient EtablissementService etablissementService; 
    
    private ClasseGroupeListener listener;

    
    
    /**
     * Mutateur de etablissementService {@link #etablissementService}.
     * @param etablissementService le etablissementService to set
     */
    public void setEtablissementService(EtablissementService etablissementService) {
        this.etablissementService = etablissementService;
    }

    /**
     * @author G-CG34-FRMP
     * 
     */
    public static interface ClasseGroupeListener {
        /**
         * Déclencher après la radio bouton classe / groupe est cliqué.
         *  Methode declenchee quand on clique sur le type class ou groupe pour
         * reinitialiser l'input.
         */
        public void classeGroupeTypeSelectionne();

        /**
         * Déclencher après une groupe ou une classe est selectionnée (ou une
         * groupe lié à une classe dans le listeCheckBox).
         */
        public void classeGroupeSelectionnee();
    }

    /**
     * 
     */
    @PostConstruct
    public void onLoad() {
    	final ContexteUtilisateur ctxUser = ContexteUtils.getContexteUtilisateur();
        final UtilisateurDTO utilisateurDTO = ctxUser.getUtilisateurDTO();
            
        
        form.setProfil(utilisateurDTO.getProfil());
        form.setTypeGroupeSelectionne(TypeGroupe.CLASSE);
        
        //Pour un élévé, l'IHM n'est pas visible.  Cherche les valeurs 
        if (form.getProfil() == Profil.ELEVE || form.getProfil() == Profil.PARENT) {
          //Profile éléve / parent, pas possible sélectionner la classe / ou la groupe.
            final Integer idEleve = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO().getIdentifiant();
            final GroupesClassesDTO classe = groupeClasseService.findClasseEleve(idEleve);
            final List<GroupeDTO> groupes = groupeClasseService.findGroupesEleve(idEleve);
            for (GroupeDTO groupe : groupes){
                groupe.setSelectionner(true);
            }
            
            form.setGroupeClasseSelectionne(classe);
            form.setListeGroupe(groupes);  
        } else {
            //Sinon, simuler un clique de type pour chercher les choix
        	// sauf en mode archive ou il faut selectioner une annee
        	if (! ctxUser.isOutilArchive()) {
        		classeGroupeTypeSelectionne(null);
        	}
        }
    }

    /**
     * Recherche dans l'archive le id de l'etablissement. Si on est pas 
     * en mode archive, on retourne l'id courant.
     * @param archive si on est en mode archive
     * @param exercice exercice 
     * @return l'id 
     */
    private Integer getIdEtablissementArchive(Boolean archive, String exercice, Integer idEtablissement) {
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        if (archive==null || !archive || exercice==null) {
            return utilisateurDTO.getIdEtablissement();
        }
        if (idEtablissement != null) return idEtablissement;
        return etablissementService.findIdEtablissementParCode(utilisateurDTO.getSirenEtablissement(), archive, exercice);
    }
    
    /**
     * Generer le RechercheGroupeClassePopupQO pour charger les classes/groupes.
     * Si idEnseignant est renseigné, on limite les classes à l'enseignant. 
     * Sinon on applique les règles standard dependante du profil.
     * @param idEnseignant id de l'enseignant (peut etre null)
     * @return le QO
     */
    private RechercheGroupeClassePopupQO genererRechercherQO(final Integer idEnseignant) { 
        final RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO = new RechercheGroupeClassePopupQO();

        rechercheGroupeClassePopupQO.setTypeGroupeClasse(this.form.getTypeGroupeSelectionne());
        rechercheGroupeClassePopupQO.setArchive(form.getArchive());
        rechercheGroupeClassePopupQO.setExerciceScolaire(form.getExercice());
        rechercheGroupeClassePopupQO.setIdEnseignant(idEnseignant);

        // En mode archive, le id etablissement peut etre different
        final Integer idEtablissement = getIdEtablissementArchive(form.getArchive(), form.getExercice(), form.getIdEtablissementFiltre());
        rechercheGroupeClassePopupQO.setIdEtablissement(idEtablissement);
        
        return rechercheGroupeClassePopupQO;
    }
    
    /**
     * @param event
     *            e
     */
    public void classeGroupeTypeSelectionne(AjaxBehaviorEvent event) {
        this.form.setGroupeClasseSelectionne(null);
        this.form.setFiltreClasseGroupe("");

        RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO = genererRechercherQO(form.getIdEnseignantFiltre()); 
        final List<GroupesClassesDTO> liste;
        if (TypeGroupe.GROUPE == (form.getTypeGroupeSelectionne())) {
            form.setTous(false);
            liste = this.getListeGroupeClassePopup(rechercheGroupeClassePopupQO);
        } else if (TypeGroupe.CLASSE == (form.getTypeGroupeSelectionne())) {
            form.setTous(false);
            liste = this.getListeGroupeClassePopup(rechercheGroupeClassePopupQO);
        } else {
            form.setTous(true);
            liste = new ArrayList<GroupesClassesDTO>();
        }
        form.setListeGroupeClasse(liste);
        // reset la liste des groupes d'une classe
        form.setListeGroupe(new ArrayList<GroupeDTO>());

        if (listener != null) {
            listener.classeGroupeTypeSelectionne();
        }
    }
    
    /**
     * Methode appelee suite a un changement d'enseignant dans la fenetre hebergeant le controleur.
     * @param idEnseignant id de l'enseignant.
     */
    public void chargerGroupeClasseEnseignant(final Integer idEnseignant) {
        form.setIdEnseignantFiltre(idEnseignant);
        classeGroupeTypeSelectionne(null);
    }
    
    /**
     * Tous.
     */
    public void resetClasseGroupeSelectionne() {
        form.setGroupeClasseSelectionne(null);
        form.setTypeGroupeSelectionne(null);
        classeGroupeTypeSelectionne(null);
    }
    
    /**
     * 
     */
    public void reset() {
        form.setTous(true);
        form.setGroupeClasseSelectionne(null);
        form.setTypeGroupeSelectionne(null);
    }

    /**
     * Sélectionner une classe ou un groupe.
     */
    public void classeGroupeSelectionnee() {
        this.form.setFiltreClasseGroupe("");
        form.setGroupeClasseSelectionne(ObjectUtils.clone(form
                .getGroupeClasseSelectionne()));

        // si c'est une classe on charge les groupes de la classes
        chargerGroupe();

        if (listener != null) {
            listener.classeGroupeSelectionnee();
        }
    }

    /**
     * Sélectionnée une groupe associée à une classe.
     * 
     * @param e
     *            e
     */
    public void groupeDeClasseSelectionnee(AjaxBehaviorEvent e) {
        if (listener != null) {
            listener.classeGroupeSelectionnee();
        }
    }

    /**
     * Retourne la liste des groupes rattachées à la classe selectionnée.
     */
    public void chargerGroupe() {
        if (TypeGroupe.CLASSE == (form.getTypeGroupeSelectionne())) {
            final RechercheGroupeQO rechercheGroupeQO = new RechercheGroupeQO();
            rechercheGroupeQO.setCodeClasse(form.getGroupeClasseSelectionne().getCode());
            rechercheGroupeQO.setVerif(false);
            
            rechercheGroupeQO.setArchive(form.getArchive());
            rechercheGroupeQO.setExercice(form.getExercice());
            
            /*
             * rechercheGroupeQO.setIdEnseignant(ContexteUtils.
             * getContexteUtilisateur() .getUtilisateurDTO()
             * .getUserDTO().getIdentifiant());
             */

            try {
                final List<GroupeDTO> listeGroupes = groupeClasseService
                        .findGroupeByClasse(rechercheGroupeQO);
                form.setListeGroupe(ObjectUtils.clone(listeGroupes));
            } catch (final MetierException e) {
                log.debug("{0}", e.getMessage());
            }
        } else {
            form.setListeGroupe(new ArrayList<GroupeDTO>());
        }
    }

    /**
     * Retourne la liste des groupes/classes pour la popup en fonction des
     * paramètres.
     * 
     * @param rechercheGroupeClassePopupQO
     *            RechercheGroupeClassePopupQO
     * 
     * @return La liste
     */
    public List<GroupesClassesDTO> getListeGroupeClassePopup(RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO) {
        List<GroupesClassesDTO> liste = new ArrayList<GroupesClassesDTO>();

        try {
            
            if (rechercheGroupeClassePopupQO.getArchive()==null || !rechercheGroupeClassePopupQO.getArchive()) {
                final UtilisateurDTO utilisateurDTO = ContexteUtils
                        .getContexteUtilisateur().getUtilisateurDTO();
    
                if (utilisateurDTO.getProfil() == Profil.INSPECTION_ACADEMIQUE) {
                    rechercheGroupeClassePopupQO.setIdInspecteur(utilisateurDTO
                            .getUserDTO().getIdentifiant());
                }
                
                if (utilisateurDTO.getProfil() == Profil.ENSEIGNANT) {
                    rechercheGroupeClassePopupQO.setIdEnseignant(utilisateurDTO
                            .getUserDTO().getIdentifiant());
                }
    
                rechercheGroupeClassePopupQO.setIdEtablissement(utilisateurDTO
                        .getIdEtablissement());
    
                rechercheGroupeClassePopupQO.setIdAnneeScolaire(utilisateurDTO
                        .getAnneeScolaireDTO().getId());
            }

            final ResultatDTO<List<GroupesClassesDTO>> listeGroupeClasseDTO = groupeClasseService.findGroupeClassePopup(rechercheGroupeClassePopupQO);
            liste = ObjectUtils.clone(listeGroupeClasseDTO.getValeurDTO());
        } catch (MetierException e) {
            log.debug("{0}", e.getMessage());
        }

        return liste;
    }

    /**
     * @return the listener
     */
    public ClasseGroupeListener getListener() {
        return listener;
    }

    /**
     * @param listener
     *            the listener to set
     */
    public void setListener(ClasseGroupeListener listener) {
        this.listener = listener;
    }

    /**
     * @param groupeClasseService
     *            the groupeClasseService to set
     */
    public void setGroupeClasseService(GroupeClasseService groupeClasseService) {
        this.groupeClasseService = groupeClasseService;
    }

}