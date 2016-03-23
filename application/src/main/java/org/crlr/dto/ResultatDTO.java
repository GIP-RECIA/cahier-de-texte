/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ResultatDTO.java,v 1.3 2009/07/28 13:32:00 weberent Exp $
 */

package org.crlr.dto;

import java.io.Serializable;

import org.crlr.message.ConteneurMessage;

/**
 * DTO générique, permettant de transmettre du métier vers la couche présentation le conteneur de message, 
 * ainsi que les resultats du service métier.
 *
 * @author breytond.
 * @version $Revision: 1.3 $
 * @param <T> les résultats du service métier.
 */
public class ResultatDTO<T>  implements Serializable {
    /**  */
    private static final long serialVersionUID = 8689869581559876557L;
    /**
     * les résultats du service métier.
     */
    private T valeurDTO;
    /**
     * le conteneur de message (avertissant ou informatif).
     */
    private ConteneurMessage conteneurMessage;
    
    /***
     * 
     * Constructeur.  
     */
    public ResultatDTO() {
        super();
    }

    /**
     * 
     * Constructeur. 
     * @param valeurDTO les résultats du service métier.
     * @param conteneurMessage le conteneur de message (avertissant ou informatif).
     */
    public ResultatDTO(final T valeurDTO, final ConteneurMessage conteneurMessage) {
        super();
        this.valeurDTO = valeurDTO;
        this.conteneurMessage = conteneurMessage;
    }

    /**
     * Accesseur.
     * @return  les résultats du service métier
     */
    public T getValeurDTO() {
        return valeurDTO;
    }

    /**
     * Mutateur.
     * @param valeurDTO  les résultats du service métier
     */
    public void setValeurDTO(T valeurDTO) {
        this.valeurDTO = valeurDTO;
    }

    /**
     * Accesseur.
     * @return le conteneur de message
     */
    public ConteneurMessage getConteneurMessage() {
        return conteneurMessage;
    }

    /**
     * Mutateur.
     * @param conteneurMessage le conteneur de message
     */
    public void setConteneurMessage(ConteneurMessage conteneurMessage) {
        this.conteneurMessage = conteneurMessage;
    }
    
    
}
