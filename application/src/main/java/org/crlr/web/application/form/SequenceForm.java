/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConsoliderEmpForm.java,v 1.10 2010/05/20 14:42:31 ent_breyton Exp $
 */

package org.crlr.web.application.form;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.base.SequenceDTO;

/**
 * .
 * 
 * @author $author$
 */
public class SequenceForm extends AbstractForm {
    /**
     * 
     */
    private static final long serialVersionUID = 5896802240880502630L;

    /** séquence sélectionnée dans la popup. */
    private SequenceDTO sequenceSelectionnee;

    /** liste des séquences dans la popup. */
    private List<SequenceDTO> listeSequence;
    

    /** Radio bouton choisi pour les sequences (un ou tous). */
    private Boolean tousSelectionne;
    
    /**
     * 
     */
    public void reset() {
        sequenceSelectionnee = new SequenceDTO();
        listeSequence = new ArrayList<SequenceDTO>();
        tousSelectionne = true;
        
    }
    
    /**
     * 
     */
    public SequenceForm() {
        reset();
    }

    /**
     * @return the sequenceSelectionnee
     */
    public SequenceDTO getSequenceSelectionnee() {
        return sequenceSelectionnee;
    }

    /**
     * @param sequenceSelectionnee the sequenceSelectionnee to set
     */
    public void setSequenceSelectionnee(SequenceDTO sequenceSelectionnee) {
        this.sequenceSelectionnee = sequenceSelectionnee;
    }

    /**
     * @return the listeSequence
     */
    public List<SequenceDTO> getListeSequence() {
        return listeSequence;
    }

    /**
     * @param listeSequence the listeSequence to set
     */
    public void setListeSequence(List<SequenceDTO> listeSequence) {
        this.listeSequence = listeSequence;
    }

    /**
     * @return the tousSelectionne
     */
    public Boolean getTousSelectionne() {
        return tousSelectionne;
    }

    /**
     * @param tousSelectionne the tousSelectionne to set
     */
    public void setTousSelectionne(Boolean tousSelectionne) {
        this.tousSelectionne = tousSelectionne;
    }
}