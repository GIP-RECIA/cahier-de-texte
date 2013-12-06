/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ResultatRechercheSeanceDTO.java,v 1.10 2009/04/29 08:52:22 vibertd Exp $
 */

package org.crlr.dto.application.seance;

import java.io.Serializable;

import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.TypeGroupe;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.10 $
 */
public class ResultatRechercheSeanceDTO extends SeanceDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = 5562208540393560533L;

  

    /** La désignation de l'enseignement. */
    private String designationEnseignement;
   
    /** Mode. */
    private String mode;

    
    /**
     * Uid de l'utilisateur connecté.
     */
    private String uid;
    

    /**
     * Accesseur codeGroupeClasse.
     *
     * @return codeGroupeClasse
     */
    @Deprecated
    public String getCodeGroupeClasse() {
        return getSequenceDTO().getGroupesClassesDTO().getCode();
    }

    /**
     * Mutateur codeGroupeClasse.
     *
     * @param codeGroupeClasse codeGroupeClasse à modifier
     */
    @Deprecated
    public void setCodeGroupeClasse(String codeGroupeClasse) {
        getSequenceDTO().getGroupesClassesDTO().setCode(codeGroupeClasse);
    }
    
    /**
     * Accesseur codeClasse.
     * @return codeClasse
     */
    @Deprecated
    public String getCodeClasse() {
        if (getSequenceDTO().getGroupesClassesDTO().getTypeGroupe() == TypeGroupe.CLASSE) {
            return getSequenceDTO().getGroupesClassesDTO().getCode();
        }
        return "";
    }

    /**
     * Mutateur codeClasse.
     * @param codeClasse Le codeClasse à modifier
     */
    @Deprecated
    public void setCodeClasse(String codeClasse) {
         getSequenceDTO().getGroupesClassesDTO().setCode(codeClasse);        
    }

    /**
     * Accesseur codeGroupe.
     * @return codeGroupe
     */
    public String getCodeGroupe() {
        return getSequenceDTO().getGroupesClassesDTO().getCode();
    }

    /**
     * Mutateur codeGroupe.
     * @param codeGroupe Le codeGroupe à modifier
     * @deprecated
     */
    public void setCodeGroupe(String codeGroupe) {
        getSequenceDTO().getGroupesClassesDTO().setCode(codeGroupe);
    }

    /**
     * Accesseur designationEnseignement.
     *
     * @return designationEnseignement
     */
    public String getDesignationEnseignement() {
        return designationEnseignement;
    }

    /**
     * Mutateur designationEnseignement.
     *
     * @param designationEnseignement designationEnseignement à modifier
     */
    public void setDesignationEnseignement(String designationEnseignement) {
        this.designationEnseignement = designationEnseignement;
    }

    /**
     * Accesseur designationSequence.
     *
     * @return designationSequence
     */
    @Deprecated
    public String getDesignationSequence() {
        return getSequenceDTO().getDescription();
    }

    /**
     * Mutateur designationSequence.
     *
     * @param designationSequence designationSequence à modifier
     */
    @Deprecated
    public void setDesignationSequence(String designationSequence) {
        getSequenceDTO().setDescription(designationSequence);
    }

  
    /**
     * Accesseur mode.
     *
     * @return mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * Mutateur mode.
     *
     * @param mode Le mode à modifier
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

   

    /**
     * Accesseur uid.
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Mutateur uid.
     * @param uid Le uid à modifier
     */
    public void setUid(String uid) {
        this.uid = uid;
    }



   


    /**
     * @return the typeGroupeClasse
     */
    @Deprecated
    public TypeGroupe getTypeGroupeClasse() {
        return getTypeGroupe();
    }

    /**
     * @param typeGroupeClasse the typeGroupeClasse to set
     */
    @Deprecated
    public void setTypeGroupeClasse(TypeGroupe typeGroupeClasse) {
        getSequenceDTO().getGroupesClassesDTO().setTypeGroupe(typeGroupeClasse);
    }



    

}
