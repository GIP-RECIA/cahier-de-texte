/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EtablissementSchemaQO.java,v 1.1 2009/05/26 08:06:30 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * EtablissementSchemaQO.
 *
 * @author breytond.
 * @version $Revision: 1.1 $
  */
public class EtablissementSchemaQO implements Serializable {
    /** Serial UID. */
    private static final long serialVersionUID = -3019533643325227412L;

    /** l'exercice scolaire. */
    private String exercice;

    /** le code de l'établissement. */
    private String codeEtablissement;

    /**
     * Accesseur exercice.
     *
     * @return le exercice.
     */
    public String getExercice() {
        return exercice;
    }

    /**
     * Mutateur exercice.
     *
     * @param exercice le exercice à modifier.
     */
    public void setExercice(String exercice) {
        this.exercice = exercice;
    }

    /**
     * Accesseur codeEtablissement.
     *
     * @return le codeEtablissement.
     */
    public String getCodeEtablissement() {
        return codeEtablissement;
    }

    /**
     * Mutateur codeEtablissement.
     *
     * @param codeEtablissement le codeEtablissement à modifier.
     */
    public void setCodeEtablissement(String codeEtablissement) {
        this.codeEtablissement = codeEtablissement;
    }
}
