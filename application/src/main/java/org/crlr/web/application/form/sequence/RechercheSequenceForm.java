/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheSequenceForm.java,v 1.13 2010/05/19 09:10:20 jerome.carriere Exp $
 */

package org.crlr.web.application.form.sequence;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.sequence.RechercheSequenceQO;
import org.crlr.dto.application.sequence.ResultatRechercheSequenceDTO;
import org.crlr.web.application.form.AbstractPopupForm;
import org.crlr.web.application.form.EnseignementForm;
import org.crlr.web.contexte.utils.ContexteUtils;

/**
 * Formulaire RechercheSequenceForm.
 *
 * @author breytond
 * @version $Revision: 1.13 $
 */
public class RechercheSequenceForm extends AbstractPopupForm {
    /**  */
    private static final long serialVersionUID = -301376493558631608L;

    /** liste des séquences recherchées. */
    private List<ResultatRechercheSequenceDTO> listeSequence;

    /** la ligne de resultat séléctionnée dans la liste des sequences. */
    private ResultatRechercheSequenceDTO resultatSelectionne;

    /** les critères de recherche. */
    private RechercheSequenceQO criteres;

    /** Enseignement selectionne lors d'un appel vers un sous ecran. */
    private EnseignementForm enseignementFormSave;
   

    /** Contrôle de l'activation de la saisie simplifiée. */
    private Boolean vraiOuFauxSaisieSimplifiee;

    
    
    /**
     * Constructeur.
     */
    public RechercheSequenceForm() {  
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        this.vraiOuFauxSaisieSimplifiee = utilisateurDTO.getVraiOuFauxEtabSaisieSimplifiee();
        reset();     
    }

    

    
    /**
     * Méthode de réinitialisation des données du formulaire saisie par
     * l'utilisateur.
     */
    public void reset() {
        this.criteres = new RechercheSequenceQO();
        this.listeSequence = new ArrayList<ResultatRechercheSequenceDTO>();
        this.resultatSelectionne = new ResultatRechercheSequenceDTO();
        
    }

    /**
     * Accesseur listeSequence.
     *
     * @return le listeSequence.
     */
    public List<ResultatRechercheSequenceDTO> getListeSequence() {
        return listeSequence;
    }

    /**
     * Mutateur listeSequence.
     *
     * @param listeSequence le listeSequence à modifier.
     */
    public void setListeSequence(List<ResultatRechercheSequenceDTO> listeSequence) {
        this.listeSequence = listeSequence;
    }

    /**
     * Accesseur criteres.
     *
     * @return le criteres.
     */
    public RechercheSequenceQO getCriteres() {
        return criteres;
    }

    /**
     * Mutateur criteres.
     *
     * @param criteres le criteres à modifier.
     */
    public void setCriteres(RechercheSequenceQO criteres) {
        this.criteres = criteres;
    }

    /**
     * Accesseur resultatSelectionne.
     *
     * @return le resultatSelectionne.
     */
    public ResultatRechercheSequenceDTO getResultatSelectionne() {
        return resultatSelectionne;
    }

    /**
     * Mutateur resultatSelectionne.
     *
     * @param resultatSelectionne le resultatSelectionne à modifier.
     */
    public void setResultatSelectionne(ResultatRechercheSequenceDTO resultatSelectionne) {
        this.resultatSelectionne = resultatSelectionne;
    }

    /**
     * Accesseur vraiOuFauxSaisieSimplifiee.
     * @return le vraiOuFauxSaisieSimplifiee
     */
    public Boolean getVraiOuFauxSaisieSimplifiee() {
        return vraiOuFauxSaisieSimplifiee;
    }




    /**
     * Accesseur de enseignementFormSave {@link #enseignementFormSave}.
     * @return retourne enseignementFormSave
     */
    public EnseignementForm getEnseignementFormSave() {
        return enseignementFormSave;
    }




    /**
     * Mutateur de enseignementFormSave {@link #enseignementFormSave}.
     * @param enseignementFormSave le enseignementFormSave to set
     */
    public void setEnseignementFormSave(EnseignementForm enseignementFormSave) {
        this.enseignementFormSave = enseignementFormSave;
    }





    
}
