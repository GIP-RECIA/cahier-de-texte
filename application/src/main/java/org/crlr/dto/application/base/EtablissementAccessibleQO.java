/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EtablissementAccessibleQO.java,v 1.1 2010/04/12 01:34:33 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;
import java.util.List;

/**
 * EtablissementSchemaQO.
 *
 * @author breytond.
 * @version $Revision: 1.1 $
  */
public class EtablissementAccessibleQO implements Serializable {
    /** Serial UID. */
    private static final long serialVersionUID = -3019533643325227412L;

    /** les siren accessibles. */
    private List<String> listeSiren;

    /** l'id de l'enseignant. */
    private Integer idEnseignant;

    /**
     * Accesseur listeSiren.
     * @return le listeSiren
     */
    public List<String> getListeSiren() {
        return listeSiren;
    }

    /**
     * Mutateur de listeSiren.
     * @param listeSiren le listeSiren à modifier.
     */
    public void setListeSiren(List<String> listeSiren) {
        this.listeSiren = listeSiren;
    }

    /**
     * Accesseur idEnseignant.
     * @return le idEnseignant
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur de idEnseignant.
     * @param idEnseignant le idEnseignant à modifier.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }   
}
