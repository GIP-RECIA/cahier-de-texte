/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConsoliderEmpForm.java,v 1.10 2010/05/20 14:42:31 ent_breyton Exp $
 */

package org.crlr.web.application.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.base.Profil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * 
 * @author $author$
 */
public class EnseignantForm extends AbstractForm {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 
     */
    private static final long serialVersionUID = 4236728407366666453L;

    // //////////////////////////////////////////////////////////////////
    // ///Enseignant

    /** enseignement sélectionné dans la popup. */
    private EnseignantDTO enseignantSelectionne;

    /** liste des enseignements dans la popup. */
    private List<EnseignantDTO> listeEnseignant;

    /** Choix pour la popup enseignement. true pour tous ou faux pour un **/
    private Boolean filtreParEnseignant;

    /** Filtre pour la popup enseignement. **/
    private String filtreEnseignant;
    
    private Profil profil;

    public EnseignantForm() {
        reset();
    }
    

    /**
     * @return the listeEnseignant
     */
    public List<EnseignantDTO> getListeEnseignant() {
        return listeEnseignant;
    }

    /**
     * @param listeEnseignant
     *            the listeEnseignant to set
     */
    public void setListeEnseignant(List<EnseignantDTO> listeEnseignant) {
        this.listeEnseignant = listeEnseignant;
    }

    /**
     * @return the filtreParEnseignant
     */
    public Boolean getFiltreParEnseignant() {
        return filtreParEnseignant;
    }

    /**
     * @param filtreParEnseignant
     *            the filtreParEnseignant to set
     */
    public void setFiltreParEnseignant(Boolean filtreParEnseignant) {
        this.filtreParEnseignant = filtreParEnseignant;
    }
    
    /**
     * @return si on filtre par tous ou par un enseignement.
     */
    public Boolean getTous() {
        return BooleanUtils.negate(filtreParEnseignant);
    }
    
    /**
     * @param tous t
     */
    public void setTous(Boolean tous) {
        setFiltreParEnseignant(BooleanUtils.negate(tous));
    }

    /**
     * @return the filtreEnseignant
     */
    public String getFiltreEnseignant() {
        return filtreEnseignant;
    }

    /**
     * @param filtreEnseignant
     *            the filtreEnseignant to set
     */
    public void setFiltreEnseignant(String filtreEnseignant) {
        this.filtreEnseignant = filtreEnseignant;
    }

    /**
     * 
     */
    public void reset() {
        filtreEnseignant = "";

        listeEnseignant = new ArrayList<EnseignantDTO>();

        filtreParEnseignant = false;
    }


    /**
     * @return the enseignantSelectionne
     */
    public EnseignantDTO getEnseignantSelectionne() {
        return enseignantSelectionne;
    }


    /**
     * @param enseignantSelectionne the enseignantSelectionne to set
     */
    public void setEnseignantSelectionne(EnseignantDTO enseignantSelectionne) {
        this.enseignantSelectionne = enseignantSelectionne;
        
        if (enseignantSelectionne == null) {
            
        } else {
            log.debug("ENS ENS {}", enseignantSelectionne);
        }
    }


    /**
     * @return the profile
     */
    public Profil getProfil() {
        return profil;
    }


    /**
     * @param profile the profile to set
     */
    public void setProfil(Profil profile) {
        this.profil = profile;
    }

}