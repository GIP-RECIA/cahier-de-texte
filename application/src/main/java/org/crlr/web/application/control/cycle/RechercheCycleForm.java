package org.crlr.web.application.control.cycle;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.cycle.CycleDTO;
import org.crlr.dto.application.cycle.RechercheCycleQO;
import org.crlr.web.application.form.AbstractPopupForm;
import org.crlr.web.contexte.utils.ContexteUtils;

/**
 * Formulaire de creation/modification/consultation d'une sequence pedagoqique.
 * @author G-SAFIR-FRMP
 *
 */
public class RechercheCycleForm extends AbstractPopupForm {

    
    /** Serial. */
    private static final long serialVersionUID = -5159321685989740994L;

    /** Filtre.*/
    private RechercheCycleQO filtre;
    
    /** Liste des cycle resultat. */
    private List<CycleDTO> listeCycle;
    
    /** Cycle selectionne dans la liste. */
    private CycleDTO cycleSelected;
    
    /**
     * Reset les champ du formulaire.
     */
    public void reset() {
        // Recupere l'utilisateur ; même dans le cadre d'un remplaçement, on veux l'utilisateur connecté (le remplacé)
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTOConnecte();
        
        filtre = new RechercheCycleQO();
        filtre.setIdEnseignant(utilisateurDTO.getUserDTO().getIdentifiant());
        listeCycle = new ArrayList<CycleDTO>();
        cycleSelected = null;
        

        
    }
    
    /**
     * Constructeur.
     */
    public RechercheCycleForm() {
        super();
        reset();
    }

    /**
     * Accesseur de listeCycle {@link #listeCycle}.
     * @return retourne listeCycle
     */
    public List<CycleDTO> getListeCycle() {
        return listeCycle;
    }

    /**
     * Mutateur de listeCycle {@link #listeCycle}.
     * @param listeCycle le listeCycle to set
     */
    public void setListeCycle(List<CycleDTO> listeCycle) {
        this.listeCycle = listeCycle;
    }

    /**
     * Accesseur de cycleSelected {@link #cycleSelected}.
     * @return retourne cycleSelected
     */
    public CycleDTO getCycleSelected() {
        return cycleSelected;
    }

    /**
     * Mutateur de cycleSelected {@link #cycleSelected}.
     * @param cycleSelected le cycleSelected to set
     */
    public void setCycleSelected(CycleDTO cycleSelected) {
        this.cycleSelected = cycleSelected;
    }

    /**
     * Accesseur de filtre {@link #filtre}.
     * @return retourne filtre
     */
    public RechercheCycleQO getFiltre() {
        return filtre;
    }

    /**
     * Mutateur de filtre {@link #filtre}.
     * @param filtre le filtre to set
     */
    public void setFiltre(RechercheCycleQO filtre) {
        this.filtre = filtre;
    }

    
}
