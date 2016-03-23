/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheSequenceControl.java,v 1.24 2010/05/19 09:10:20 jerome.carriere Exp $
 */

package org.crlr.web.application.control.sequence;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.crlr.dto.Outil;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.sequence.ResultatRechercheSequenceDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.web.application.control.AbstractPopupControl;
import org.crlr.web.application.control.ClasseGroupeControl;
import org.crlr.web.application.control.EnseignementControl;
import org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener;
import org.crlr.web.application.control.EnseignementControl.EnseignementListener;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.application.form.sequence.RechercheSequenceForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.utils.MessageUtils;
import org.crlr.web.utils.NavigationUtils;

/**
 * RechercheSequenceControl.
 *
 * @author breytond
 * @version $Revision: 1.24 $
 */
@ManagedBean(name="rechSequence")
@ViewScoped
public class RechercheSequenceControl extends
AbstractPopupControl<RechercheSequenceForm> implements EnseignementListener, ClasseGroupeListener {
    /** DOCUMENTATION INCOMPLETE! */
    private final static String IDFORM =
        RechercheSequenceControl.class.getName() + "form";
    

    @ManagedProperty(value = "#{classeGroupe}")
    private transient ClasseGroupeControl classeGroupeControl;

    @ManagedProperty(value = "#{enseignement}")
    private transient EnseignementControl enseignementControl;
    
    
/**
     * Constructeur.
     */
    public RechercheSequenceControl() {
        super(new RechercheSequenceForm());
        usePopupSequence = false;
        usePopupGroupeClasse = true;
        usePopupSeance = false;
        usePopupEnseignement = true;
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    public void onLoad() {
        //permet de faire un seule appel métier durant le cycle de l'outil.
        //valable pour les outils dont les paramètres ne sont pas saisies par l'utilisateur.
        
        enseignementControl.setListener(this);
        classeGroupeControl.setListener(this);
        
        final RechercheSequenceForm formSave =
            (RechercheSequenceForm) ContexteUtils.getContexteOutilControl()
                                                 .recupererEtSupprimerObjet(IDFORM);
        if (formSave == null) {
            rechercherEnseignement();
            reset();
        } else {
            form = formSave;
            classeGroupeControl.getForm().setGroupeClasseSelectionne(form.getCriteres().getGroupeClasseSelectionne());
            enseignementControl.chargerListeEnseignement(form.getCriteres().getGroupeClasseSelectionne(),
                    null, false, false, null, null);
            
            enseignementControl.setForm(form.getEnseignementFormSave());
            this.rechercher();
        }
    
        final ConteneurMessage conteneurMessageAcquittement =
            (ConteneurMessage) ContexteUtils.getContexteOutilControl()
                                            .recupererEtSupprimerObjet(AbstractForm.RETOUR_ACQUITTEMENT);
        if ((conteneurMessageAcquittement != null) &&
                (conteneurMessageAcquittement.size() > 0)) {
            MessageUtils.addMessages(conteneurMessageAcquittement, null, getClass());
        }
    }

    /**
     * Appel métier de recherche des enseignements.
     */
    private void rechercherEnseignement() {
        enseignementControl.chargerListeEnseignement(new GroupesClassesDTO(),
                null, false, false, null, null);
    }

   


    /**
     * Réinitialisation.
     */
    public void reset() {
        form.reset();
        classeGroupeControl.getForm().setTous(true);
        enseignementControl.getForm().setTous(true);
        classeGroupeControl.getForm().setGroupeClasseSelectionne(null);
        classeGroupeControl.getForm().setTypeGroupeSelectionne(null);
    }
    
    /**
     * 
     */
    private void resetListeSequence() {
        form.setListeSequence(new ArrayList<ResultatRechercheSequenceDTO>());
    }

    /**
     * Appel métier de recherche des séquences en fonction de potentiels
     * critères.
     */
    public void rechercher() {
        
        if (null != classeGroupeControl.getForm().getGroupeClasseSelectionne()) {
            form.getCriteres().setGroupeClasseSelectionne(classeGroupeControl.getForm().getGroupeClasseSelectionne());
        } else {
            form.getCriteres().setGroupeClasseSelectionne(new GroupesClassesDTO());
            form.getCriteres().getGroupeClasseSelectionne().setTypeGroupe(classeGroupeControl.getForm().getTypeGroupeSelectionne());
        }

        final Integer idEnseignant =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO()
                         .getIdentifiant();

        final Integer idEtablissement =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getIdEtablissement();
        
        if (null != enseignementControl.getForm().getEnseignementSelectionne()) {
            form.getCriteres().setCodeEnseignement(enseignementControl.getForm().getEnseignementSelectionne().getCode());
        }
        
        form.getCriteres().setIdEnseignant(idEnseignant);
        form.getCriteres().setIdEtablissement(idEtablissement);

        try {
            final ResultatDTO<List<ResultatRechercheSequenceDTO>> listeSequenceDTO =
                sequenceService.findSequence(form.getCriteres());

            final List<ResultatRechercheSequenceDTO> resultList =
                listeSequenceDTO.getValeurDTO();
            form.setListeSequence(resultList);
        } catch (MetierException e) {
            this.form.setListeSequence(new ArrayList<ResultatRechercheSequenceDTO>());
            log.debug("{0}", e.getMessage());
        }
    }

    /**
     * Sélectionne la ligne et redirection vers le sous écran de l'édition d'une
     * séquence.
     *
     * @return outcome.
     */
    public String selectionner() {
        log.debug("----------------- MODIFIER -----------------");
        form.getResultatSelectionne().setMode(AbstractForm.MODE_MODIF);
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(IDFORM, form);
        form.setEnseignementFormSave(enseignementControl.getForm());
        return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(Outil.EDIT_SEQUENCE,
                                                                            RechercheSequenceControl.class.getName(),
                                                                            form.getResultatSelectionne());
    }

  

    /**
     * Appel métier pour supprimer une séquence.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String supprimer() {
        log.debug("----------------- SUPPRESSION -----------------");
        form.getResultatSelectionne().setMode(AbstractForm.MODE_DELETE);
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(IDFORM, form);       
        form.setEnseignementFormSave(enseignementControl.getForm());
        return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(Outil.DELETE_SEQUENCE,
                                                                            RechercheSequenceControl.class.getName(),
                                                                            form.getResultatSelectionne());
    }

    /**
     * Appel métier pour dupliquer une séquence.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String dupliquer() {
        log.debug("----------------- DUPLIQUER -----------------");
        form.getResultatSelectionne().setMode(AbstractForm.MODE_DUPLICATE);
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(IDFORM, form);
        form.setEnseignementFormSave(enseignementControl.getForm());
        return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(Outil.DUPLIQUER_SEQUENCE,
                                                                            RechercheSequenceControl.class.getName(),
                                                                            form.getResultatSelectionne());
    }
    
    
    /**
     * Clone la valeur de l'objet enseignement de la recherche.
     */
    public void selectionnerEnseignement() {
    }

    /**
     * @param classeGroupeControl the classeGroupeControl to set
     */
    public void setClasseGroupeControl(ClasseGroupeControl classeGroupeControl) {
        this.classeGroupeControl = classeGroupeControl;
    }

    /**
     * @param enseignementControl the enseignementControl to set
     */
    public void setEnseignementControl(EnseignementControl enseignementControl) {
        this.enseignementControl = enseignementControl;
    }
    
    /* (non-Javadoc)
     * @see org.crlr.web.application.control.EnseignementControl.EnseignementListener#enseignementTousOuUnSelectionne(boolean)
     */
    @Override
    public void enseignementTousOuUnSelectionne(boolean tous) {
        if (tous) {
            form.getCriteres().setCodeEnseignement(null);
            form.getCriteres().setIdEnseignement(null);
        }
        resetListeSequence();        
    }

    /* (non-Javadoc)
     * @see org.crlr.web.application.control.EnseignementControl.EnseignementListener#enseignementSelectionnee()
     */
    @Override
    public void enseignementSelectionnee() {
        resetListeSequence();        
    }

    /* (non-Javadoc)
     * @see org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener#classeGroupeTypeSelectionne()
     */
    @Override
    public void classeGroupeTypeSelectionne() {
        resetListeSequence();        
    }

    /* (non-Javadoc)
     * @see org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener#classeGroupeSelectionnee()
     */
    @Override
    public void classeGroupeSelectionnee() {
        resetListeSequence();
        
    }
    
}
