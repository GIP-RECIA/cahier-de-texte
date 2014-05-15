/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SeancePrintForm.java,v 1.9 2010/06/01 13:45:24 ent_breyton Exp $
 */

package org.crlr.web.application.form.seance;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.crlr.dto.application.base.ArchiveSeanceDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.seance.PrintSeanceDTO;
import org.crlr.dto.application.sequence.PrintSequenceDTO;
import org.crlr.dto.application.visa.VisaDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.web.application.form.AbstractPrintForm;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
public class SeancePrintForm extends AbstractPrintForm {
    /**  */
    private static final long serialVersionUID = -2359805802125106218L;


    
   /** La liste des sequences. */
    private List<PrintSequenceDTO> listeSequences;
    /** La liste des seances. */
    private List<PrintSeanceDTO> listeSeances;
    /** La sequence selectionne. */
    private PrintSequenceDTO sequenceSelectionne;
    /** La seances selectionne. */
    private PrintSeanceDTO seanceSelectionne;
    /** Le devoir selectionne. */
    private DevoirDTO devoirSelectionne;
    
    private boolean visualiseArchiveDirecteur;
    private boolean visualiseArchiveInspecteur;
    private boolean visualiseBack;
    
/**
     * Constructeur.
     */
    public SeancePrintForm() {
        reset();
    }
    
   

    /**
     * Méthode de réinitialisation des données du formulaire saisie par
     * l'utilisateur.
     */
    public void reset() {
        super.reset();
        

        
        
        this.listeSeances= new ArrayList<PrintSeanceDTO>();
        this.listeSequences = new ArrayList<PrintSequenceDTO>();
        this.sequenceSelectionne = null;
        this.seanceSelectionne = null;
        this.devoirSelectionne = null;
    }

    
   
 


    /**
     * Accesseur de listeSeances.
     * @return le listeSeances
     */
    public List<PrintSeanceDTO> getListeSeances() {
        return listeSeances;
    }

    /**
     * Mutateur de listeSeances.
     * @param listeSeances le listeSeances à modifier.
     */
    public void setListeSeances(List<PrintSeanceDTO> listeSeances) {
        this.listeSeances = listeSeances;
    }
    
    /**
     * @return vrai si'l y a une séance qui est pliée.
     */
    public boolean getExisteSeancePlier() {
        if (CollectionUtils.isEmpty(listeSeances)) {
            return true;
        }
        
        for (PrintSeanceDTO seance : listeSeances) {
            if (!seance.getOpen()) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Accesseur de seanceSelectionne.
     * @return le seanceSelectionne
     */
    public PrintSeanceDTO getSeanceSelectionne() {
        return seanceSelectionne;
    }

    /**
     * Mutateur de seanceSelectionne.
     * @param seanceSelectionne le seanceSelectionne à modifier.
     */
    public void setSeanceSelectionne(PrintSeanceDTO seanceSelectionne) {
        this.seanceSelectionne = seanceSelectionne;
    }

    /**
     * Accesseur de devoirSelectionne.
     * @return le devoirSelectionne
     */
    public DevoirDTO getDevoirSelectionne() {
        return devoirSelectionne;
    }

    /**
     * Mutateur de devoirSelectionne.
     * @param devoirSelectionne le devoirSelectionne à modifier.
     */
    public void setDevoirSelectionne(DevoirDTO devoirSelectionne) {
        this.devoirSelectionne = devoirSelectionne;
    }

   

    

    /**
     * Accesseur de sequenceSelectionne.
     * @return le sequenceSelectionne
     */
    public PrintSequenceDTO getSequenceSelectionne() {
        return sequenceSelectionne;
    }

    /**
     * Mutateur de sequenceSelectionne.
     * @param sequenceSelectionne le sequenceSelectionne à modifier.
     */
    public void setSequenceSelectionne(PrintSequenceDTO sequenceSelectionne) {
        this.sequenceSelectionne = sequenceSelectionne;
    }

    /**
     * Accesseur de listeSequences.
     * @return le listeSequences
     */
    public List<PrintSequenceDTO> getListeSequences() {
        return listeSequences;
    }

    /**
     * Mutateur de listeSequences.
     * @param listeSequences le listeSequences à modifier.
     */
    public void setListeSequences(
            List<PrintSequenceDTO> listeSequences) {
        this.listeSequences = listeSequences;
    }



	public boolean isVisualiseArchiveInspecteur() {
		return visualiseArchiveInspecteur;
	}



	public void setVisualiseArchiveInspecteur(boolean visualiseArchiveInspecteur) {
		this.visualiseArchiveInspecteur = visualiseArchiveInspecteur;
	}



	public boolean isVisualiseArchiveDirecteur() {
		return visualiseArchiveDirecteur;
	}



	public void setVisualiseArchiveDirecteur(boolean visualiseArchiveDirecteur) {
		this.visualiseArchiveDirecteur = visualiseArchiveDirecteur;
	}



	public boolean isVisualiseBack() {
		return visualiseBack;
	}



	public void setVisualiseBack(boolean visualiseBack) {
		this.visualiseBack = visualiseBack;
	}

    
   
    
}
