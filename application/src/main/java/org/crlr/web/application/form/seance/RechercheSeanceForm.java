/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheSeanceForm.java,v 1.5 2010/05/19 09:10:20 jerome.carriere Exp $
 */

package org.crlr.web.application.form.seance;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.dto.application.seance.ResultatRechercheSeanceDTO;
import org.crlr.web.application.form.AbstractPopupForm;

/**
 * Formulaire.
 *
 * @author breytond
 * @version $Revision: 1.5 $
 */
public class RechercheSeanceForm extends AbstractPopupForm {
    /**  */
    private static final long serialVersionUID = -301376493558631608L;

    /** liste des séances recherchées. */
    private List<ResultatRechercheSeanceDTO> listeSeance;

    /** la ligne de resultat séléctionnée dans la liste des séances. */
    private ResultatRechercheSeanceDTO resultatSelectionne;

    /** les critères de recherche. */
    private RechercheSeanceQO criteres;

    /**
     * Constructeur.
     */
    public RechercheSeanceForm() {
        reset();
    }

    /**
     * Méthode de réinitialisation des données du formulaire saisie par
     * l'utilisateur.
     */
    public void reset() {
        criteres = new RechercheSeanceQO();
        listeSeance = new ArrayList<ResultatRechercheSeanceDTO>();
        resultatSelectionne = new ResultatRechercheSeanceDTO();
    }

    

    /**
     * Accesseur criteres.
     *
     * @return criteres
     */
    public RechercheSeanceQO getCriteres() {
        return criteres;
    }

    /**
     * Mutateur criteres.
     *
     * @param criteres criteres à modifier
     */
    public void setCriteres(RechercheSeanceQO criteres) {
        this.criteres = criteres;
    }

    /**
     * Accesseur listeSeance.
     *
     * @return listeSeance la liste des séances
     */
    public List<ResultatRechercheSeanceDTO> getListeSeance() {
        return listeSeance;
    }

    /**
     * Mutateur listeSeance.
     *
     * @param listeSeance listeSeance à modifier
     */
    public void setListeSeance(List<ResultatRechercheSeanceDTO> listeSeance) {
        this.listeSeance = listeSeance;
    }

    /**
     * Accesseur resultatSelectionne.
     *
     * @return resultatSelectionne
     */
    public ResultatRechercheSeanceDTO getResultatSelectionne() {
        return resultatSelectionne;
    }

    /**
     * Mutateur resultatSelectionne.
     *
     * @param resultatSelectionne resultatSelectionne à modifier
     */
    public void setResultatSelectionne(ResultatRechercheSeanceDTO resultatSelectionne) {
        this.resultatSelectionne = resultatSelectionne;
    }

   
   
}
