/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequencePrintForm.java,v 1.4 2010/05/19 09:10:20 jerome.carriere Exp $
 */

package org.crlr.web.application.form.sequence;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.seance.PrintSeanceDTO;
import org.crlr.dto.application.sequence.PrintSequenceDTO;
import org.crlr.web.application.form.AbstractPrintForm;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class SequencePrintForm extends AbstractPrintForm {
    /**  */
    private static final long serialVersionUID = -2359805802125106218L;

    
    
    
    
    private List<PrintSequenceDTO> listeSequences;
    
    
    
    /** La sequence selectionne. */
    private PrintSequenceDTO sequenceSelectionne;
    /** La seances selectionne. */
    private PrintSeanceDTO seanceSelectionne;
    /** Le devoir selectionne. */
    private DevoirDTO devoirSelectionne;
    
/**
     * Constructeur.
     */
    public SequencePrintForm() {
        reset();
    }

    /**
     * Méthode de réinitialisation des données du formulaire saisie par
     * l'utilisateur.
     */
    public void reset() {
        super.reset();
        
        
        
        
        listeSequences = new ArrayList<PrintSequenceDTO>();
        
        
    }

    
    
  

  


    /**
     * @return the listeSequences
     */
    public List<PrintSequenceDTO> getListeSequences() {
        return listeSequences;
    }

    /**
     * @param listeSequences the listeSequences to set
     */
    public void setListeSequences(List<PrintSequenceDTO> listeSequences) {
        this.listeSequences = listeSequences;
    }


   

    /**
     * @return the sequenceSelectionne
     */
    public PrintSequenceDTO getSequenceSelectionne() {
        return sequenceSelectionne;
    }

    /**
     * @param sequenceSelectionne the sequenceSelectionne to set
     */
    public void setSequenceSelectionne(PrintSequenceDTO sequenceSelectionne) {
        this.sequenceSelectionne = sequenceSelectionne;
    }

    /**
     * @return the seanceSelectionne
     */
    public PrintSeanceDTO getSeanceSelectionne() {
        return seanceSelectionne;
    }

    /**
     * @param seanceSelectionne the seanceSelectionne to set
     */
    public void setSeanceSelectionne(PrintSeanceDTO seanceSelectionne) {
        this.seanceSelectionne = seanceSelectionne;
    }

    /**
     * @return the devoirSelectionne
     */
    public DevoirDTO getDevoirSelectionne() {
        return devoirSelectionne;
    }

    /**
     * @param devoirSelectionne the devoirSelectionne to set
     */
    public void setDevoirSelectionne(DevoirDTO devoirSelectionne) {
        this.devoirSelectionne = devoirSelectionne;
    }
}
