/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheSeanceControl.java,v 1.23 2010/05/19 09:10:20 jerome.carriere Exp $
 */

package org.crlr.web.application.control.seance;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.Outil;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.seance.ResultatRechercheSeanceDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.control.AbstractPopupControl;
import org.crlr.web.application.control.ClasseGroupeControl;
import org.crlr.web.application.control.EnseignementControl;
import org.crlr.web.application.control.SequenceControl;
import org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener;
import org.crlr.web.application.control.EnseignementControl.EnseignementListener;
import org.crlr.web.application.control.SequenceControl.SequenceListener;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.application.form.ClasseGroupeForm;
import org.crlr.web.application.form.EnseignementForm;
import org.crlr.web.application.form.seance.RechercheSeanceForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.utils.MessageUtils;
import org.crlr.web.utils.NavigationUtils;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.23 $
 */
@ManagedBean(name="rechSeance")
@ViewScoped
public class RechercheSeanceControl extends
AbstractPopupControl<RechercheSeanceForm> implements EnseignementListener, ClasseGroupeListener, SequenceListener {
    /** DOCUMENTATION INCOMPLETE! */
    private final static String IDFORM = RechercheSeanceControl.class.getName() + "form";

    @ManagedProperty(value = "#{classeGroupe}")
    private transient ClasseGroupeControl classeGroupeControl;
    
    @ManagedProperty(value = "#{enseignement}")
    private transient EnseignementControl enseignementControl;

    @ManagedProperty(value = "#{sequenceControl}")
    protected transient SequenceControl sequenceControl;
    
    
/**
 * 
 * 
     * Constructeur.
     */
    public RechercheSeanceControl() {
        super(new RechercheSeanceForm());
        usePopupSequence = true;
        usePopupGroupeClasse = true;
        usePopupSeance = false;
        usePopupEnseignement = true;
    }

    /**
     * 
     */
    @PostConstruct
    public void onLoad() {
        //permet de faire un seule appel métier durant le cycle de l'outil.
        //valable pour les outils dont les paramètres ne sont pas saisies par l'utilisateur.
        
        enseignementControl.setListener(this);
        classeGroupeControl.setListener(this);
        sequenceControl.setListener(this);
        
        final RechercheSeanceForm formSave =
            (RechercheSeanceForm) ContexteUtils.getContexteOutilControl()
                                               .recupererEtSupprimerObjet(IDFORM);
        if (formSave == null) {
            rechercherEnseignement();
            reset();
        } else {
            form = formSave;
            enseignementControl.setForm((EnseignementForm) ContexteUtils
                    .getContexteOutilControl().recupererEtSupprimerObjet(
                            EnseignementForm.class.getName()));
            classeGroupeControl.setForm((ClasseGroupeForm) ContexteUtils
                    .getContexteOutilControl().recupererEtSupprimerObjet(
                            ClasseGroupeControl.class.getName()));

            this.rechercher();
        }
    
        final ConteneurMessage conteneurMessageAcquittement =
            (ConteneurMessage) ContexteUtils.getContexteOutilControl()
                                            .recupererEtSupprimerObjet(AbstractForm.RETOUR_ACQUITTEMENT);
        if ((conteneurMessageAcquittement != null) &&
                (conteneurMessageAcquittement.size() > 0)) {
            MessageUtils.addMessages(conteneurMessageAcquittement, null, getClass());
        }
        
        getListeSequenceSelectionnee();
    }

    /**
     * Réinitialisation.
     */
    public void reset() {
        form.reset();
        classeGroupeControl.getForm().setTous(true);
        enseignementControl.getForm().setFiltreParEnseignement(false);
        classeGroupeControl.getForm().setGroupeClasseSelectionne(null);
        classeGroupeControl.getForm().setTypeGroupeSelectionne(null);
        getListeSequenceSelectionnee();
    }

    
    /**
     * Sélectionne la ligne et redirection vers le sous écran de l'édition d'une
     * séance.
     *
     * @return outcome.
     */
    public String selectionner() {
        log.debug("----------------- MODIFIER -----------------");
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(IDFORM, form);
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(EnseignementForm.class.getName(), enseignementControl.getForm());
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(ClasseGroupeControl.class.getName(), classeGroupeControl.getForm());
        form.getResultatSelectionne().setMode(AbstractForm.MODE_MODIF);
        return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(Outil.AJOUT_SEANCE,
                                                                            RechercheSeanceControl.class.getName(),
                                                                            ObjectUtils.clone(form.getResultatSelectionne()));
    }
    /**
     * Appel métier de recherche des séquences en fonction de potentiels
     * critères.
     */
    public void rechercher() {
        log.debug("----------------- RECHERCHE -----------------");
        if (null != enseignementControl.getForm().getEnseignementSelectionne()) {
            form.getCriteres().setCodeEnseignement(enseignementControl.getForm().getEnseignementSelectionne().getCode());
        }
        
        if (sequenceControl.getForm().getSequenceSelectionnee()!=null) {
            form.getCriteres().setCodeSequence(sequenceControl.getForm().getSequenceSelectionnee().getCode());
        }
        final Integer idEnseignant =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO()
                         .getIdentifiant();
        form.getCriteres().setIdEnseignant(idEnseignant);
        final Integer idEtablissement =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getIdEtablissement();
        form.getCriteres().setIdEtablissement(idEtablissement);

        form.getCriteres().setGroupeClasseSelectionne(classeGroupeControl.getForm().getGroupeClasseSelectionne());
        
        try {
            final ResultatDTO<List<ResultatRechercheSeanceDTO>> listeSeanceDTO =
                seanceService.findSeance(form.getCriteres());

            final List<ResultatRechercheSeanceDTO> resultList =
                listeSeanceDTO.getValeurDTO();
            form.setListeSeance(org.crlr.utils.CollectionUtils.cloneCollection(resultList));
        } catch (MetierException e) {
            resetListeSeance();
            log.debug("{0}", e.getMessage());
        }
    }
    
    /**
     * 
     */
    public void resetListeSeance() {
        form.setListeSeance(new ArrayList<ResultatRechercheSeanceDTO>());
    }

    /**
     * Appel métier pour supprimer une séance.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String supprimer() {
        log.debug("----------------- SUPPRESSION -----------------");
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(IDFORM, form);
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(EnseignementForm.class.getName(), enseignementControl.getForm());
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(ClasseGroupeControl.class.getName(), classeGroupeControl.getForm());
        
        form.getResultatSelectionne().setMode(AbstractForm.MODE_DELETE);
        return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(Outil.AJOUT_SEANCE,
                                                                            RechercheSeanceControl.class.getName(),
                                                                            ObjectUtils.clone(form.getResultatSelectionne()));
    }

   

    /**
     * Appel métier pour dupliquer une séance.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String dupliquer() {
        log.debug("----------------- DUPLIQUER -----------------");
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(IDFORM, form);
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(EnseignementForm.class.getName(), enseignementControl.getForm());
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(ClasseGroupeControl.class.getName(), classeGroupeControl.getForm());
        
        form.getResultatSelectionne().setMode(AbstractForm.MODE_DUPLICATE);
        return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(Outil.AJOUT_SEANCE,
                                                                            RechercheSeanceControl.class.getName(),
                                                                            ObjectUtils.clone(form.getResultatSelectionne()));
    }

    /**
     * Appel métier de recherche des enseignements.
     */
    private void rechercherEnseignement() {
        enseignementControl.chargerListeEnseignement(new GroupesClassesDTO(),
                null, false, false, null, null);
    }

    

    /**
     * Charge la liste des séquences.
     */
    public void getListeSequenceSelectionnee() {
        
        final UtilisateurDTO utilisateurDTO =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        EnseignantDTO enseignant = new EnseignantDTO();
        enseignant.setId(utilisateurDTO.getUserDTO().getIdentifiant());
        
        sequenceControl.chargerListeSequence(
               enseignementControl.getForm().getEnseignementSelectionne(), 
               enseignant,
               classeGroupeControl.getForm().getGroupeClasseSelectionne());

    }

    /**
     * @param classeGroupeControl the classeGroupeControl to set
     */
    public void setClasseGroupeControl(ClasseGroupeControl classeGroupeControl) {
        this.classeGroupeControl = classeGroupeControl;
    }

    /**
     * @return the enseignementControl
     */
    public EnseignementControl getEnseignementControl() {
        return enseignementControl;
    }

    /**
     * @param enseignementControl the enseignementControl to set
     */
    public void setEnseignementControl(EnseignementControl enseignementControl) {
        this.enseignementControl = enseignementControl;
    }

    
    
    /**
     * Mutateur de sequenceControl {@link #sequenceControl}.
     * @param sequenceControl le sequenceControl to set
     */
    public void setSequenceControl(SequenceControl sequenceControl) {
        this.sequenceControl = sequenceControl;
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void enseignementTousOuUnSelectionne(boolean tous) {
        if (tous) {
            form.getCriteres().setCodeEnseignement(null);
            form.getCriteres().setIdEnseignement(null);
        }
        getListeSequenceSelectionnee();
        sequenceControl.getForm().setSequenceSelectionnee(null);
        sequenceControl.getForm().setTousSelectionne(true);
        resetListeSeance();        
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void enseignementSelectionnee() {
        getListeSequenceSelectionnee();
        sequenceControl.getForm().setSequenceSelectionnee(null);
        sequenceControl.getForm().setTousSelectionne(true);
        resetListeSeance();        
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void classeGroupeTypeSelectionne() {
        getListeSequenceSelectionnee();
        sequenceControl.getForm().setSequenceSelectionnee(null);
        sequenceControl.getForm().setTousSelectionne(true);
        resetListeSeance();        
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void classeGroupeSelectionnee() {
        getListeSequenceSelectionnee();
        sequenceControl.getForm().setSequenceSelectionnee(null);
        sequenceControl.getForm().setTousSelectionne(true);
        resetListeSeance();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void sequenceSelectionnee() {
        resetListeSeance();
        
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void sequenceTousOuUnSelectionne(boolean tous) {
        resetListeSeance();
    }
}
