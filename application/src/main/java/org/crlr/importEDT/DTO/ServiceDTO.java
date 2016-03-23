/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.importEDT.DTO;

import java.io.Serializable;
import java.util.List;

/**
 * Classe qui permet de représenter la balise service du XML.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public class ServiceDTO implements Serializable{
    /**
     * Le serialVersionUid.
     */
    private static final long serialVersionUID  = 1L;
    
    /**
     * Code de la matière.
     */
    private String codeMatiere;

    /**
     * Code du type de cours.
     */
    private String codeModCours;

    /**
     * Nombre d'heures structures.
     */
    private String heuresStructures;

    /**
     * Co enseignement ou pas.
     */
    private boolean coEns;

    /**
     * Liste des enseignants.
     */
    private List<IndividuDTO> enseignants;

    /**
     * Accesseur de codeMatiere.
     *
     * @return le codeMatiere
     */
    public String getCodeMatiere() {
        return codeMatiere;
    }

    /**
     * Modificateur de codeMatiere.
     *
     * @param codeMatiere le codeMatiere à modifier
     */
    public void setCodeMatiere(String codeMatiere) {
        this.codeMatiere = codeMatiere;
    }

    /**
     * Accesseur de codeModCours.
     *
     * @return le codeModCours
     */
    public String getCodeModCours() {
        return codeModCours;
    }

    /**
     * Modificateur de codeModCours.
     *
     * @param codeModCours le codeModCours à modifier
     */
    public void setCodeModCours(String codeModCours) {
        this.codeModCours = codeModCours;
    }

    /**
     * Accesseur de heuresStructures.
     *
     * @return le heuresStructures
     */
    public String getHeuresStructures() {
        return heuresStructures;
    }

    /**
     * Modificateur de heuresStructures.
     *
     * @param heuresStructures le heuresStructures à modifier
     */
    public void setHeuresStructures(String heuresStructures) {
        this.heuresStructures = heuresStructures;
    }

    /**
     * Accesseur de coEns.
     *
     * @return le coEns
     */
    public boolean isCoEns() {
        return coEns;
    }

    /**
     * Modificateur de coEns.
     *
     * @param coEns le coEns à modifier
     */
    public void setCoEns(boolean coEns) {
        this.coEns = coEns;
    }

    /**
     * Accesseur de enseignants.
     *
     * @return le enseignants
     */
    public List<IndividuDTO> getEnseignants() {
        return enseignants;
    }

    /**
     * Modificateur de enseignants.
     *
     * @param enseignants le enseignants à modifier
     */
    public void setEnseignants(List<IndividuDTO> enseignants) {
        this.enseignants = enseignants;
    }
}
