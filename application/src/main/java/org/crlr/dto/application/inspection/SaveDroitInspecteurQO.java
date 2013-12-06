/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.dto.application.inspection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.base.UserDTOForList;



/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class SaveDroitInspecteurQO implements Serializable {

    private static final long serialVersionUID = 3037131061822110440L;

    /** DOCUMENTATION INCOMPLETE! */
    private Integer idEtablissement;
    
    private List<EnseignantDTO> enseignantsSelectionne;
    private List<UserDTOForList> inspecteursSelectionne;
    
    private List<Integer> idsEnseignant;
    private List<Integer> idsInspecteur;

    private Boolean vraiOuFauxAjout; 
    
    private UserDTOForList directeur;
    
    /**
     * Constructeur.
     */
    public SaveDroitInspecteurQO(){
        enseignantsSelectionne = new ArrayList<EnseignantDTO>();
        inspecteursSelectionne = new ArrayList<UserDTOForList>();
        idsEnseignant = new ArrayList<Integer>();
        idsInspecteur = new ArrayList<Integer>();
    }
    
    /**
     * Accesseur de idEtablissement.
     * @return le idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur de idEtablissement.
     * @param idEtablissement le idEtablissement à modifier.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }
    

    /**
     * Accesseur de enseignantsSelectionne.
     * @return le enseignantsSelectionne
     */
    public List<EnseignantDTO> getEnseignantsSelectionne() {
        return enseignantsSelectionne;
    }

    /**
     * Mutateur de enseignantsSelectionne.
     * @param enseignantsSelectionne le enseignantsSelectionne à modifier.
     */
    public void setEnseignantsSelectionne(
            List<EnseignantDTO> enseignantsSelectionne) {
        this.enseignantsSelectionne = enseignantsSelectionne;
    }

    /**
     * Accesseur de inspecteursSelectionne.
     * @return le inspecteursSelectionne
     */
    public List<UserDTOForList> getInspecteursSelectionne() {
        return inspecteursSelectionne;
    }

    /**
     * Mutateur de inspecteursSelectionne.
     * @param inspecteursSelectionne le inspecteursSelectionne à modifier.
     */
    public void setInspecteursSelectionne(
            List<UserDTOForList> inspecteursSelectionne) {
        this.inspecteursSelectionne = inspecteursSelectionne;
    }

    

    /**
     * Accesseur de idsEnseignant.
     * @return le idsEnseignant
     */
    public List<Integer> getIdsEnseignant() {
        return idsEnseignant;
    }

    /**
     * Mutateur de idsEnseignant.
     * @param idsEnseignant le idsEnseignant à modifier.
     */
    public void setIdsEnseignant(List<Integer> idsEnseignant) {
        this.idsEnseignant = idsEnseignant;
    }

    /**
     * Accesseur de idsInspecteur.
     * @return le idsInspecteur
     */
    public List<Integer> getIdsInspecteur() {
        return idsInspecteur;
    }

    /**
     * Mutateur de idsInspecteur.
     * @param idsInspecteur le idsInspecteur à modifier.
     */
    public void setIdsInspecteur(List<Integer> idsInspecteur) {
        this.idsInspecteur = idsInspecteur;
    }

    /**
     * Accesseur de vraiOuFauxAjout.
     * @return le vraiOuFauxAjout
     */
    public Boolean getVraiOuFauxAjout() {
        return vraiOuFauxAjout;
    }

    /**
     * Mutateur de vraiOuFauxAjout.
     * @param vraiOuFauxAjout le vraiOuFauxAjout à modifier.
     */
    public void setVraiOuFauxAjout(Boolean vraiOuFauxAjout) {
        this.vraiOuFauxAjout = vraiOuFauxAjout;
    }

    /**
     * Accesseur de directeur.
     * @return le directeur
     */
    public UserDTOForList getDirecteur() {
        return directeur;
    }

    /**
     * Mutateur de directeur.
     * @param directeur le directeur à modifier.
     */
    public void setDirecteur(UserDTOForList directeur) {
        this.directeur = directeur;
    }

    
    
    
    


}
