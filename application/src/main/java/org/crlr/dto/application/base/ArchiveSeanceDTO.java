/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AnneeScolaireDTO.java,v 1.3 2010/03/29 09:29:35 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

/**
 * 
 *
 */
public class ArchiveSeanceDTO extends SeanceDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 6680843304097828916L;
    
    //Le champ id dans SeanceDTO est id_archive_seance
    
    private Integer idSeanceOriginale;

    /**
     * @return the idSeanceOriginale
     */
    public Integer getIdSeanceOriginale() {
        return idSeanceOriginale;
    }

    /**
     * @param idSeanceOriginale the idSeanceOriginale to set
     */
    public void setIdSeanceOriginale(Integer idSeanceOriginale) {
        this.idSeanceOriginale = idSeanceOriginale;
    }
    
    
}