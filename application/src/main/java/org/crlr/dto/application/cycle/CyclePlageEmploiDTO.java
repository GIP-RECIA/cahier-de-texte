package org.crlr.dto.application.cycle;

import java.io.Serializable;
import java.util.Date;

import org.crlr.dto.application.base.TypeJour;
import org.crlr.web.dto.TypeSemaine;

/**
 * Plage EDT reduite avec des infos periode. 
 * @author G-SAFIR-FRMP
 *
 */
public class CyclePlageEmploiDTO  implements Serializable {

    /**  Serial. */
    private static final long serialVersionUID = 744918314761289133L;

    /** Date de debut de la periode. */
    private Date dateDebutPeriode;
    
    /** Date de fin de pediode. */
    private Date dateFinPeriode;
    
    /** Heure de debut. */
    private Integer heureDebut;
    
    /** Minute debut. */
    private Integer minuteDebut;
    
    /** Heure de fin.*/
    private Integer heureFin;
    
    /** Minute fin. */
    private Integer minuteFin;

    /** Enseignement. */
    private Integer idEnseignement;

    /** Numero du jour. */
    private TypeJour typeJour;
    
    /** Type de semaine. */
    private TypeSemaine typeSemaine;
    
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

    /**
     * Accesseur de dateFinPeriode {@link #dateFinPeriode}.
     * @return retourne dateFinPeriode
     */
    public Date getDateFinPeriode() {
        return dateFinPeriode;
    }

    /**
     * Mutateur de dateFinPeriode {@link #dateFinPeriode}.
     * @param dateFinPeriode le dateFinPeriode to set
     */
    public void setDateFinPeriode(Date dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    /**
     * Accesseur de heureDebut {@link #heureDebut}.
     * @return retourne heureDebut
     */
    public Integer getHeureDebut() {
        return heureDebut;
    }

    /**
     * Mutateur de heureDebut {@link #heureDebut}.
     * @param heureDebut le heureDebut to set
     */
    public void setHeureDebut(Integer heureDebut) {
        this.heureDebut = heureDebut;
    }

    /**
     * Accesseur de minuteDebut {@link #minuteDebut}.
     * @return retourne minuteDebut
     */
    public Integer getMinuteDebut() {
        return minuteDebut;
    }

    /**
     * Mutateur de minuteDebut {@link #minuteDebut}.
     * @param minuteDebut le minuteDebut to set
     */
    public void setMinuteDebut(Integer minuteDebut) {
        this.minuteDebut = minuteDebut;
    }

    /**
     * Accesseur de heureFin {@link #heureFin}.
     * @return retourne heureFin
     */
    public Integer getHeureFin() {
        return heureFin;
    }

    /**
     * Mutateur de heureFin {@link #heureFin}.
     * @param heureFin le heureFin to set
     */
    public void setHeureFin(Integer heureFin) {
        this.heureFin = heureFin;
    }

    /**
     * Accesseur de minuteFin {@link #minuteFin}.
     * @return retourne minuteFin
     */
    public Integer getMinuteFin() {
        return minuteFin;
    }

    /**
     * Mutateur de minuteFin {@link #minuteFin}.
     * @param minuteFin le minuteFin to set
     */
    public void setMinuteFin(Integer minuteFin) {
        this.minuteFin = minuteFin;
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

    /**
     * Accesseur de typeJour {@link #typeJour}.
     * @return retourne typeJour
     */
    public TypeJour getTypeJour() {
        return typeJour;
    }

    /**
     * Mutateur de typeJour {@link #typeJour}.
     * @param typeJour le typeJour to set
     */
    public void setTypeJour(TypeJour typeJour) {
        this.typeJour = typeJour;
    }

    /**
     * Accesseur de typeSemaine {@link #typeSemaine}.
     * @return retourne typeSemaine
     */
    public TypeSemaine getTypeSemaine() {
        return typeSemaine;
    }

    /**
     * Mutateur de typeSemaine {@link #typeSemaine}.
     * @param typeSemaine le typeSemaine to set
     */
    public void setTypeSemaine(TypeSemaine typeSemaine) {
        this.typeSemaine = typeSemaine;
    }

    
}
