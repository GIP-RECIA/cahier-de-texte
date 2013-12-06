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
import org.crlr.dto.application.base.EnseignementDTO;

/**
 * .
 * 
 * @author $author$
 */
public class EnseignementForm extends AbstractForm {
    /**
     * 
     */
    private static final long serialVersionUID = 4236728407366666453L;

    // //////////////////////////////////////////////////////////////////
    // ///Enseignement

    /** enseignement sélectionné dans la popup. */
    private EnseignementDTO enseignementSelectionne;

    /** liste des enseignements dans la popup. */
    private List<EnseignementDTO> listeEnseignement;

    /** Choix pour la popup enseignement. true pour tous ou faux pour un **/
    private Boolean filtreParEnseignement;

    /** Filtre pour la popup enseignement. **/
    private String filtreEnseignement;

    public EnseignementForm() {
    	reset();
    }
    /**
     * @return the enseignementSelectionne
     */
    public EnseignementDTO getEnseignementSelectionne() {
        return enseignementSelectionne;
    }

    /**
     * @param enseignementSelectionne
     *            the enseignementSelectionne to set
     */
    public void setEnseignementSelectionne(
            EnseignementDTO enseignementSelectionne) {
        this.enseignementSelectionne = enseignementSelectionne;
    }

    /**
     * @return the listeEnseignement
     */
    public List<EnseignementDTO> getListeEnseignement() {
        return listeEnseignement;
    }

    /**
     * @param listeEnseignement
     *            the listeEnseignement to set
     */
    public void setListeEnseignement(List<EnseignementDTO> listeEnseignement) {
        this.listeEnseignement = listeEnseignement;
    }

    /**
     * @return the filtreParEnseignement
     */
    public Boolean getFiltreParEnseignement() {
        return filtreParEnseignement;
    }

    /**
     * @param filtreParEnseignement
     *            the filtreParEnseignement to set
     */
    public void setFiltreParEnseignement(Boolean filtreParEnseignement) {
        this.filtreParEnseignement = filtreParEnseignement;
    }
    
    /**
     * @return si on filtre par tous ou par un enseignement.
     */
    public Boolean getTous() {
        return BooleanUtils.negate(filtreParEnseignement);
    }
    
    /**
     * @param tous t
     */
    public void setTous(Boolean tous) {
        setFiltreParEnseignement(BooleanUtils.negate(tous));
    }

    /**
     * @return the filtreEnseignement
     */
    public String getFiltreEnseignement() {
        return filtreEnseignement;
    }

    /**
     * @param filtreEnseignement
     *            the filtreEnseignement to set
     */
    public void setFiltreEnseignement(String filtreEnseignement) {
        this.filtreEnseignement = filtreEnseignement;
    }

    /**
     * 
     */
    public void reset() {
        filtreEnseignement = "";

        listeEnseignement = new ArrayList<EnseignementDTO>();

        filtreParEnseignement = false;
    }

}