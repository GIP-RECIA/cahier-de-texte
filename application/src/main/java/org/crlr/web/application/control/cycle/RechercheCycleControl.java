/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheSeanceControl.java,v 1.23 2010/05/19 09:10:20 jerome.carriere Exp $
 */

package org.crlr.web.application.control.cycle;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.crlr.dto.Outil;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.cycle.CycleDTO;
import org.crlr.services.CycleService;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.control.AbstractPopupControl;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.utils.NavigationUtils;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.23 $
 */
@ManagedBean(name="rechCycle")
@ViewScoped
public class RechercheCycleControl extends
AbstractPopupControl<RechercheCycleForm> {

    /** Identifiant du controleur. Sert lors du retour d'une navigation vers un sous ecran. */
    private final static String IDFORM = RechercheCycleControl.class.getName() + "form";
    
    /** seanceService. */
    @ManagedProperty(value="#{cycleService}")
    protected transient CycleService cycleService;
    
    /**
     * Constructeur.
     */
    public RechercheCycleControl() {
        super(new RechercheCycleForm());
    }

    /**
     * Mutateur de cycleService {@link #cycleService}.
     * @param cycleService le cycleService to set
     */
    public void setCycleService(CycleService cycleService) {
        this.cycleService = cycleService;
    }



    /**
     * 
     */
    @PostConstruct
    public void onLoad() {
        
        // Gestion du retour depuis un sous ecran
        final RechercheCycleForm formSave =
            (RechercheCycleForm) ContexteUtils.getContexteOutilControl()
                                               .recupererEtSupprimerObjet(IDFORM);
        if (formSave == null) {
            reset();
        } else {
            form = formSave;
            rechercher();
        }
    }

    // ************************************************************************
    // Methodes invoquees par le formulaire 
    // ************************************************************************
    
    /**
     * Recherche la liste des cycles correspondant aux criteres saisis.
     */
    public void rechercher() {
        final ResultatDTO<List<CycleDTO>> listeCycle = cycleService.findListeCycle(form.getFiltre());
        form.setListeCycle(listeCycle.getValeurDTO());
    }
    
    /**
     * Reset les criteres de recherche ainsi que la liste resultat.
     */
    public void reset() {
        form.reset();
    }
    
    /**
     * Reset la liste resultat.
     * Appelee lors d'un changement de valeur pour un des criteres de recherche.
     */
    public void resetListeCycle () {
        final List<CycleDTO> liste = new ArrayList<CycleDTO>();
        form.setListeCycle(liste);
    }
    
    /** 
     * Selectionner un cycle dans la liste des resultats.
     * Navigation vers les souc-ecran de modification/consultation d'un cycle.
     * @return la chaine de navigation.
     */
    public String selectionner() {
        if (form.getCycleSelected() != null)  {
            ContexteUtils.getContexteOutilControl().mettreAJourObjet(IDFORM, form);
            return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(
                    Outil.AJOUT_CYCLE,
                    RechercheCycleControl.class.getName(),
                    ObjectUtils.clone(form.getCycleSelected()));            
        } else {
            return null;
        }
    }
    
    /** 
     * Selectionner un cycle dans la liste des resultats pour l'appliquer à un cahier de texte.
     * Navigation vers les souc-ecran d'application d'un cycle.
     * @return la chaine de navigation.
     */
    public String appliquer() {
        if (form.getCycleSelected() != null)  {
            ContexteUtils.getContexteOutilControl().mettreAJourObjet(IDFORM, form);
            return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(
                    Outil.APPLY_CYCLE,
                    RechercheCycleControl.class.getName(),
                    ObjectUtils.clone(form.getCycleSelected()));            
        } else {
            return null;
        }
    }
    
}
