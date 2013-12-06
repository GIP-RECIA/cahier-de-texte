/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AbstractForm.java,v 1.8 2010/04/19 13:35:00 ent_breyton Exp $
 */

package org.crlr.web.application.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

/**
 * Formulaire de base. Tous les formulaires de l'application <strong>doivent</strong>
 * hériter de cette classe.
 *
 * @author breytond
 */
public abstract class AbstractForm implements Serializable {
    protected final Log log = LogFactory.getLog(getClass());

    /** serial version. */
    private static final long serialVersionUID = 7139503488321073449L;

    /** The Constant MODE_DEFAUT. */
    public static final String MODE_DEFAUT = "mode_defaut";

    /** The Constant MODE_SELECTION. */
    public static final String MODE_SELECTION = "mode_selection";
    
    /** The Constant MODE_CONSULTATION. */
    public static final String MODE_CONSULTATION = "mode_consulter";

    /** The Constant MODE_AJOUT. */
    public static final String MODE_AJOUT = "mode_ajout";
    
    /** The Constant MODE_DELETE. */
    public static final String MODE_DELETE = "mode_suppression";
    
    /** The Constant MODE_DUPLICATE. */
    public static final String MODE_DUPLICATE = "mode_dupliquer";
    
    /** The Constant MODE_DELETE. */
    public static final String MODE_MODIF = "mode_modifier";

    /** The Constant MODE_SOUS_ECRAN. */
    public static final String MODE_SOUS_ECRAN = "mode_sous_ecran";
    
    /** Permet de tester les heures des séances et les groupes. */
    public static final Integer ZERO = 0;

    /** Gestion des messages d'acquitement : ajout. */
    public final static String RETOUR_ACQUITTEMENT = "conteneur_acquittement";
    
    /** The mode. */
    protected String mode;

  
    
    private static List<SelectItem> listeHeures = new ArrayList<SelectItem>();
    private static List<SelectItem> listeMinutes = new ArrayList<SelectItem>();
    
    private Set<String> listeChampsObligatoire;
    
    static {
        
        for (int h=7;h<=9; ++h) {
            listeHeures.add(new SelectItem(h, "0" + String.valueOf(h)));
        }
        
        for (int h=10; h<=22; ++h) {
            listeHeures.add(new SelectItem(h, String.valueOf(h)));
        }        
                
        for (int m=0;m<=9;m+=1) {
            listeMinutes.add(new SelectItem(m, "0" + m));
        }
        
        for (int m=10;m<60;m+=1) {
            listeMinutes.add(new SelectItem(m, String.valueOf(m)));
        }  
    }

    /**
     * The Constructor.
     */
    public AbstractForm() {
        this.mode = MODE_DEFAUT;
        resetChampsObligatoire();
    }
    
    /** Réinitialisation de la liste des champs obligatoires. */
    public final void resetChampsObligatoire() {
        listeChampsObligatoire = new HashSet<String>();
    }

    public Set<String> getListeChampsObligatoire() {
        return listeChampsObligatoire;
    }

    /**
     * Retourne mode.
     *
     * @return the mode
     */
    public final String getMode() {
        return mode;
    }

    /**
     * Affecte mode.
     *
     * @param mode the mode
     */
    public final void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Affecte mode ajout.
     */
    public final void setModeAjout() {
        setMode(MODE_AJOUT);
    }

    /**
     * Affecte mode selection.
     */
    public final void setModeSelection() {
        setMode(MODE_SELECTION);
    }

    /**
     * Affecte mode defaut.
     */
    public final void setModeDefaut() {
        setMode(MODE_DEFAUT);
    }

    /**
     * Checks if is initialise.
     *
     * @return true, if is initialise
     * Avec JSF 2 ScopeView, plus d'utilité
     */
    @Deprecated
    public final boolean isInitialise() {
        return false;
    }

    /**
     * Affectation de initialise.
     *
     * @param initialise the initialise
     */
    @Deprecated
    public final void setInitialise(boolean initialise) {
        
    }
    
    /**
     * Accesseur listeHeures.
     * @return le listeHeures.
     */
    public List<SelectItem> getListeHeures() {
        return listeHeures;
    }

    /**
     * Accesseur listeMinutes.
     * @return le listeMinutes.
     */
    public List<SelectItem> getListeMinutes() {
        return listeMinutes;
    }

    public void setListeChampsObligatoire(Set<String> listeChampsObligatoire) {
        this.listeChampsObligatoire = listeChampsObligatoire;
    }
}
