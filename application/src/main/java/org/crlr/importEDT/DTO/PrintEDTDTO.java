/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.importEDT.DTO;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * PrintEDTDTO.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public class PrintEDTDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = 4885650510403505015L;
    
    private Set<ErreurEDT> enseignantErreurEDT = new HashSet<ErreurEDT>();
    
    private Set<ErreurEDT> enseignantErreurBD = new HashSet<ErreurEDT>();
    
    private Set<ErreurEDT> enseignementErreur = new HashSet<ErreurEDT>();
    
    private Set<ErreurEDT> classeErreur = new HashSet<ErreurEDT>();
    
    private Set<ErreurEDT> groupeErreur = new HashSet<ErreurEDT>();
    
    private Set<ErreurEDT> horaireErreur = new HashSet<ErreurEDT>();
    
    private Integer totalCase = 0;
    
    private Date dateDebutPeriode;

    /**
     * Accesseur de enseignantErreurEDT.
     * @return le enseignantErreurEDT
     */
    public Set<ErreurEDT> getEnseignantErreurEDT() {
        return enseignantErreurEDT;
    }

    /**
     * Modificateur de enseignantErreurEDT.
     * @param enseignantErreurEDT le enseignantErreurEDT à modifier
     */
    public void setEnseignantErreurEDT(Set<ErreurEDT> enseignantErreurEDT) {
        this.enseignantErreurEDT = enseignantErreurEDT;
    }

    /**
     * Accesseur de enseignantErreurBD.
     * @return le enseignantErreurBD
     */
    public Set<ErreurEDT> getEnseignantErreurBD() {
        return enseignantErreurBD;
    }

    /**
     * Modificateur de enseignantErreurBD.
     * @param enseignantErreurBD le enseignantErreurBD à modifier
     */
    public void setEnseignantErreurBD(Set<ErreurEDT> enseignantErreurBD) {
        this.enseignantErreurBD = enseignantErreurBD;
    }

    /**
     * Accesseur de enseignementErreur.
     * @return le enseignementErreur
     */
    public Set<ErreurEDT> getEnseignementErreur() {
        return enseignementErreur;
    }

    /**
     * Modificateur de enseignementErreur.
     * @param enseignementErreur le enseignementErreur à modifier
     */
    public void setEnseignementErreur(Set<ErreurEDT> enseignementErreur) {
        this.enseignementErreur = enseignementErreur;
    }

    /**
     * Accesseur de classeErreur.
     * @return le classeErreur
     */
    public Set<ErreurEDT> getClasseErreur() {
        return classeErreur;
    }

    /**
     * Modificateur de classeErreur.
     * @param classeErreur le classeErreur à modifier
     */
    public void setClasseErreur(Set<ErreurEDT> classeErreur) {
        this.classeErreur = classeErreur;
    }

    /**
     * Accesseur de groupeErreur.
     * @return le groupeErreur
     */
    public Set<ErreurEDT> getGroupeErreur() {
        return groupeErreur;
    }

    /**
     * Modificateur de groupeErreur.
     * @param groupeErreur le groupeErreur à modifier
     */
    public void setGroupeErreur(Set<ErreurEDT> groupeErreur) {
        this.groupeErreur = groupeErreur;
    }

    /**
     * Accesseur de horaireErreur.
     * @return le horaireErreur
     */
    public Set<ErreurEDT> getHoraireErreur() {
        return horaireErreur;
    }

    /**
     * Modificateur de horaireErreur.
     * @param horaireErreur le horaireErreur à modifier
     */
    public void setHoraireErreur(Set<ErreurEDT> horaireErreur) {
        this.horaireErreur = horaireErreur;
    }

    /**
     * Accesseur de totalCase.
     * @return le totalCase
     */
    public Integer getTotalCase() {
        return totalCase;
    }

    /**
     * Modificateur de totalCase.
     * @param totalCase le totalCase à modifier
     */
    public void setTotalCase(Integer totalCase) {
        this.totalCase = totalCase;
    }

    /**
     * Accesseur de dateDebutPeriode {@link #dateDebutPeriode}.
     * @return retourne dateDebutPeriode
     */
    public Date getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    /**
     * Mutateur de dateDebutPeriode {@link #dateDebutPeriode}.
     * @param dateDebutPeriode le dateDebutPeriode to set
     */
    public void setDateDebutPeriode(Date dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }
    
}
