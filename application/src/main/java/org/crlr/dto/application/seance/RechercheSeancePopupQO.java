/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheSeancePopupQO.java,v 1.3 2009/04/01 07:07:34 vibertd Exp $
 */

package org.crlr.dto.application.seance;

import java.io.Serializable;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class RechercheSeancePopupQO implements Serializable {
    /**  */
    private static final long serialVersionUID = -2853634389687218412L;

    /** Id enseignement. */
    private Integer idEnseignement;

    /** Type G ou C. */
    private String typeGroupeClasse;

    /** Id du groupe ou de la classe. */
    private Integer idClasseGroupe;

    /** Id de la séquence. */
    private Integer idSequence;

    /**
     * idEnseignement.
     *
     * @return idEnseignement
     */
    public Integer getIdEnseignement() {
        return idEnseignement;
    }

    /**
     * idEnseignement.
     *
     * @param idEnseignement idEnseignement
     */
    public void setIdEnseignement(Integer idEnseignement) {
        this.idEnseignement = idEnseignement;
    }

    /**
     * typeGroupeClasse.
     *
     * @return typeGroupeClasse
     */
    public String getTypeGroupeClasse() {
        return typeGroupeClasse;
    }

    /**
     * typeGroupeClasse.
     *
     * @param typeGroupeClasse typeGroupeClasse
     */
    public void setTypeGroupeClasse(String typeGroupeClasse) {
        this.typeGroupeClasse = typeGroupeClasse;
    }

    /**
     * idClasseGroupe.
     *
     * @return idClasseGroupe
     */
    public Integer getIdClasseGroupe() {
        return idClasseGroupe;
    }

    /**
     * idClasseGroupe.
     *
     * @param idClasseGroupe idClasseGroupe
     */
    public void setIdClasseGroupe(Integer idClasseGroupe) {
        this.idClasseGroupe = idClasseGroupe;
    }

    /**
     * idSequence.
     *
     * @return idSequence
     */
    public Integer getIdSequence() {
        return idSequence;
    }

    /**
     * idSequence.
     *
     * @param idSequence idSequence
     */
    public void setIdSequence(Integer idSequence) {
        this.idSequence = idSequence;
    }
}
