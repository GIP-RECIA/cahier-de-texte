/**
 * 
 */
package org.crlr.dto.application.remplacement;

import java.io.Serializable;
import java.util.Date;

import org.crlr.alimentation.DTO.EnseignantDTO;

/**
 * @author G-SAFIR-FRMP
 *
 */
public class RemplacementDTO implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = 5474700624785454649L;

    /** Id du devoir. */
    private Integer id;
    
    /** Id de l'etablissement. */
    private Integer idEtablissement;
    
    /** Enseignant absent. */
    private EnseignantDTO enseignantAbsent;

    /** Enseignant remplacant. */
    private EnseignantDTO enseignantRemplacant;
    
    /** Date de debut de remplacement. */
    private Date dateDebut;
    
    /** Date de fin de remplacement. */
    private Date dateFin;
    
    /** Indique si la ligne a ete modifiee. */
    private Boolean estModifiee;

    /** Indique que la ligne a ete supprimee. */
    private Boolean estSupprimee;
    
    /** Indique s'il s'agit d'un nouveau droit ou d'un droit existant. */
    private Boolean estAjoute;
    
    /**
     * Constructeur par defaut.
     */
    public RemplacementDTO() {
        estModifiee = false;
        estSupprimee = false;
        estAjoute = false;
    }

    /**
     * Accesseur de id {@link #id}.
     * @return retourne id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur de id {@link #id}.
     * @param id le id to set
     */
    public void setId(Integer id) {
        this.id = id;
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
     * Accesseur de dateDebut {@link #dateDebut}.
     * @return retourne dateDebut
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * Mutateur de dateDebut {@link #dateDebut}.
     * @param dateDebut le dateDebut to set
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Accesseur de dateFin {@link #dateFin}.
     * @return retourne dateFin
     */
    public Date getDateFin() {
        return dateFin;
    }

    /**
     * Mutateur de dateFin {@link #dateFin}.
     * @param dateFin le dateFin to set
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * Accesseur de enseignantAbsent {@link #enseignantAbsent}.
     * @return retourne enseignantAbsent
     */
    public EnseignantDTO getEnseignantAbsent() {
        return enseignantAbsent;
    }

    /**
     * Mutateur de enseignantAbsent {@link #enseignantAbsent}.
     * @param enseignantAbsent le enseignantAbsent to set
     */
    public void setEnseignantAbsent(EnseignantDTO enseignantAbsent) {
        this.enseignantAbsent = enseignantAbsent;
    }

    /**
     * Accesseur de enseignantRemplacant {@link #enseignantRemplacant}.
     * @return retourne enseignantRemplacant
     */
    public EnseignantDTO getEnseignantRemplacant() {
        return enseignantRemplacant;
    }

    /**
     * Mutateur de enseignantRemplacant {@link #enseignantRemplacant}.
     * @param enseignantRemplacant le enseignantRemplacant to set
     */
    public void setEnseignantRemplacant(EnseignantDTO enseignantRemplacant) {
        this.enseignantRemplacant = enseignantRemplacant;
    }

    /**
     * Accesseur de estModifiee {@link #estModifiee}.
     * @return retourne estModifiee
     */
    public Boolean getEstModifiee() {
        return estModifiee;
    }

    /**
     * Mutateur de estModifiee {@link #estModifiee}.
     * @param estModifiee le estModifiee to set
     */
    public void setEstModifiee(Boolean estModifiee) {
        this.estModifiee = estModifiee;
    }

    /**
     * Accesseur de estSupprimee {@link #estSupprimee}.
     * @return retourne estSupprimee
     */
    public Boolean getEstSupprimee() {
        return estSupprimee;
    }

    /**
     * Mutateur de estSupprimee {@link #estSupprimee}.
     * @param estSupprimee le estSupprimee to set
     */
    public void setEstSupprimee(Boolean estSupprimee) {
        this.estSupprimee = estSupprimee;
    }

    /**
     * Accesseur de estAjoute {@link #estAjoute}.
     * @return retourne estAjoute
     */
    public Boolean getEstAjoute() {
        return estAjoute;
    }

    /**
     * Mutateur de estAjoute {@link #estAjoute}.
     * @param estAjoute le estAjoute to set
     */
    public void setEstAjoute(Boolean estAjoute) {
        this.estAjoute = estAjoute;
    }

}
