/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: RechercheEmploiQO.java,v 1.3 2010/04/21 15:39:48 jerome.carriere Exp $
 */

package org.crlr.dto.application.emploi;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.crlr.web.dto.TypeSemaine;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class RechercheEmploiQO implements Serializable {
    /**
     * Identifiant de sérialisation. 
     */
    private static final long serialVersionUID = 4593265835614149416L;

    /** Type de semaine. **/
    private TypeSemaine typeSemaine;

    /** Id de la classe ou du groupe. */
        
    private Integer idGroupeOuClasse;
    
    /** Vrai ou faux est une classe. **/
    private Boolean vraiOuFauxClasse;
   
    /** liste des identifiant des groupes pour les parents et élèves. **/
    private Set<Integer> listeDeGroupes; 
    
    /** Id de l'enseignant. */
    private Integer idEnseignant;

    /** Id de l'enseignement. */
    private Integer idEnseignement;
    
    /** Identifiant de l'etablissement. */
    private Integer idEtablissement;
    
    /** Date de debut.*/ 
    private Date dateDebut;
    
    /** Date de fin. */
    private Date dateFin;
        
    /**
     * Accesseur typeSemaine.
     * @return le typeSemaine
     */
    public TypeSemaine getTypeSemaine() {
        return typeSemaine;
    }

    /**
     * Mutateur de typeSemaine.
     * @param typeSemaine le typeSemaine à modifier.
     */
    public void setTypeSemaine(TypeSemaine typeSemaine) {
        this.typeSemaine = typeSemaine;
    }

    

    /**
     * Accesseur listeDeGroupes.
     * @return le listeDeGroupes
     */
    public Set<Integer> getListeDeGroupes() {
        return listeDeGroupes;
    }

    /**
     * Mutateur de listeDeGroupes.
     * @param listeDeGroupes le listeDeGroupes à modifier.
     */
    public void setListeDeGroupes(Set<Integer> listeDeGroupes) {
        this.listeDeGroupes = listeDeGroupes;
    }

    

    /**
     * Accesseur idEtablissement.
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
     * @return the idGroupeOuClasse
     */
    public Integer getIdGroupeOuClasse() {
        return idGroupeOuClasse;
    }

    /**
     * @param idGroupeOuClasse the idGroupeOuClasse to set
     */
    public void setIdGroupeOuClasse(Integer idGroupeOuClasse) {
        this.idGroupeOuClasse = idGroupeOuClasse;
    }

    /**
     * @return the vraiOuFauxClasse
     */
    public Boolean getVraiOuFauxClasse() {
        return vraiOuFauxClasse;
    }

    /**
     * @param vraiOuFauxClasse the vraiOuFauxClasse to set
     */
    public void setVraiOuFauxClasse(Boolean vraiOuFauxClasse) {
        this.vraiOuFauxClasse = vraiOuFauxClasse;
    }

    /**
     * @return the dateDebut
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * @param dateDebut the dateDebut to set
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @return the dateFin
     */
    public Date getDateFin() {
        return dateFin;
    }

    /**
     * @param dateFin the dateFin to set
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * Accesseur de idEnseignant {@link #idEnseignant}.
     * @return retourne idEnseignant
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur de idEnseignant {@link #idEnseignant}.
     * @param idEnseignant le idEnseignant to set
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur de idEnseignement {@link #idEnseignement}.
     * @return retourne idEnseignement
     */
    public Integer getIdEnseignement() {
        return idEnseignement;
    }

    /**
     * Mutateur de idEnseignement {@link #idEnseignement}.
     * @param idEnseignement le idEnseignement to set
     */
    public void setIdEnseignement(Integer idEnseignement) {
        this.idEnseignement = idEnseignement;
    }
    
    
}
