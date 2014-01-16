/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ResultatRechercheDevoirDTO.java,v 1.7 2010/04/28 16:12:21 jerome.carriere Exp $
 */

package org.crlr.dto.application.devoir;

import java.io.Serializable;
import java.util.Date;

import org.crlr.dto.application.base.SeanceDTO;

import com.google.common.collect.ComparisonChain;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.7 $
 */
public class ResultatRechercheDevoirDTO extends DevoirDTO implements Serializable, 
Comparable<ResultatRechercheDevoirDTO> {
    /**  */
    private static final long serialVersionUID = -3964041334794393505L;

    


    /** DOCUMENTATION INCOMPLETE! */
    private String codeEnseignement;


  
    

    /**
     * Contructeur.
     */
    public ResultatRechercheDevoirDTO() {
    }

  

    /**
     * Accesseur idSeance.
     *
     * @return idSeance
     */
    public Integer getIdSeance() {
        return getSeance().getId();
    }

   
    

    /**
     * Accesseur codeEnseignement.
     *
     * @return codeEnseignement
     */
    public String getCodeEnseignement() {
        return codeEnseignement;
    }

    /**
     * Mutateur codeEnseignement.
     *
     * @param codeEnseignement Le codeEnseignement à modifier
     */
    public void setCodeEnseignement(String codeEnseignement) {
        this.codeEnseignement = codeEnseignement;
    }

 
   


    @Override
    public int compareTo(ResultatRechercheDevoirDTO o) {
        return ComparisonChain.start().compare(getDateRemise(), o.getDateRemise()).result();
    }

   
    
}
