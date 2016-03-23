/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheEmploiQO.java,v 1.3 2010/04/21 15:39:48 jerome.carriere Exp $
 */

package org.crlr.dto.application.emploi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * saveEmploiQO.
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class SaveEmploiQO implements Serializable {
    /**
     * Identifiant de sérialisation. 
     */   
    private static final long serialVersionUID = -7790523760467889571L;

    /**
     * La liste des plages d'emploi du temps mises à jour.
     */
    private List<DetailJourEmploiDTO> listeDetailJourEmploiDTO = new ArrayList<DetailJourEmploiDTO>();
    
    
    /**
     * la liste des ids d'emploi du temps à supprimer.
     */
    private List<Integer> listeIdEmploiTempsAsupprimer = new ArrayList<Integer>();

    
    /**
     * Accesseur listeDetailJourEmploiDTO.
     * @return le listeDetailJourEmploiDTO.
     */
    public List<DetailJourEmploiDTO> getListeDetailJourEmploiDTO() {
        return listeDetailJourEmploiDTO;
    }

    /**
     * Mutateur listeDetailJourEmploiDTO.
     * @param listeDetailJourEmploiDTO le listeDetailJourEmploiDTO à modifier.
     */
    public void setListeDetailJourEmploiDTO(
            List<DetailJourEmploiDTO> listeDetailJourEmploiDTO) {
        this.listeDetailJourEmploiDTO = listeDetailJourEmploiDTO;
    }

  

 

    /**
     * Accesseur listeIdEmploiTempsAsupprimer.
     * @return le listeIdEmploiTempsAsupprimer.
     */
    public List<Integer> getListeIdEmploiTempsAsupprimer() {
        return listeIdEmploiTempsAsupprimer;
    }

    /**
     * Mutateur listeIdEmploiTempsAsupprimer.
     * @param listeIdEmploiTempsAsupprimer le listeIdEmploiTempsAsupprimer à modifier.
     */
    public void setListeIdEmploiTempsAsupprimer(
            List<Integer> listeIdEmploiTempsAsupprimer) {
        this.listeIdEmploiTempsAsupprimer = listeIdEmploiTempsAsupprimer;
    }


}
