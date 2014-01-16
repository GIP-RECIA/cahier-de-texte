/**
 * 
 */
package org.crlr.dto.application.remplacement;

import java.io.Serializable;

import org.joda.time.LocalDate;

/**
 * @author G-SAFIR-FRMP
 *
 */
public class RechercheRemplacementQO implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = -8735670558389590717L;

    /** Id remplacement. */
    private Integer idRemplacement;
    
    /** Id de l'etablissement. */
    private Integer idEtablissement;
    
    /** Id de l'enseignant qu'il soit absent ou remplacant. */
    private Integer idEnseignant;
    
    /** Id Enseignant remplacant. */
    private Integer idEnseignantRemplacant;
    
    /** Id Enseignant absent. */
    private Integer idEnseignantAbsent;

    /** Date de debut de periode. */
    private LocalDate dateDebut;
    
    /** Date de fin de periode. */
    private LocalDate dateFin; 
    
    /**  
     * Date qui est contenu par la période
     */
    private LocalDate date;
    
    /**
     * Constructeur par defaut.
     */
    public RechercheRemplacementQO() {
    }

    
    /**
     * Accesseur de idEtablissement {@link #idEtablissement}.
     * @return retourne idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur de idEtablissement {@link #idEtablissement}.
     * @param idEtablissement le idEtablissement to set
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }


    /**
     * Accesseur de idEnseignantRemplacant {@link #idEnseignantRemplacant}.
     * @return retourne idEnseignantRemplacant
     */
    public Integer getIdEnseignantRemplacant() {
        return idEnseignantRemplacant;
    }


    /**
     * Mutateur de idEnseignantRemplacant {@link #idEnseignantRemplacant}.
     * @param idEnseignantRemplacant le idEnseignantRemplacant to set
     */
    public void setIdEnseignantRemplacant(Integer idEnseignantRemplacant) {
        this.idEnseignantRemplacant = idEnseignantRemplacant;
    }


    /**
     * Accesseur de idEnseignantAbsent {@link #idEnseignantAbsent}.
     * @return retourne idEnseignantAbsent
     */
    public Integer getIdEnseignantAbsent() {
        return idEnseignantAbsent;
    }


    /**
     * Mutateur de idEnseignantAbsent {@link #idEnseignantAbsent}.
     * @param idEnseignantAbsent le idEnseignantAbsent to set
     */
    public void setIdEnseignantAbsent(Integer idEnseignantAbsent) {
        this.idEnseignantAbsent = idEnseignantAbsent;
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
     * Accesseur de dateDebut {@link #dateDebut}.
     * @return retourne dateDebut
     */
    public LocalDate getDateDebut() {
        return dateDebut;
    }


    /**
     * Mutateur de dateDebut {@link #dateDebut}.
     * @param dateDebut le dateDebut to set
     */
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }


    /**
     * Accesseur de dateFin {@link #dateFin}.
     * @return retourne dateFin
     */
    public LocalDate getDateFin() {
        return dateFin;
    }


    /**
     * Mutateur de dateFin {@link #dateFin}.
     * @param dateFin le dateFin to set
     */
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }


    /**
     * Accesseur de idRemplacement {@link #idRemplacement}.
     * @return retourne idRemplacement
     */
    public Integer getIdRemplacement() {
        return idRemplacement;
    }


    /**
     * Mutateur de idRemplacement {@link #idRemplacement}.
     * @param idRemplacement le idRemplacement to set
     */
    public void setIdRemplacement(Integer idRemplacement) {
        this.idRemplacement = idRemplacement;
    }


    /**
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }


    /**
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }


}
