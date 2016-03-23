package org.crlr.dto.application.cycle;

import java.io.Serializable;
import java.util.Date;

import org.crlr.dto.application.base.EnseignementDTO;

/**
 * Plage horodatee pour la création d'une seance correspondant a l'emploi du temps de la classe/enseignant. 
 * @author G-SAFIR-FRMP
 *
 */
public class CycleEmploiDTO  implements Serializable {

    /**  Serial. */
    private static final long serialVersionUID = 6481337708849886343L;
    
    /** Date de la plage. */
    private Date date;

    /** Heure de debut. */
    private Integer heureDebut;
    
    /** Minute debut. */
    private Integer minuteDebut;
    
    /** Heure de fin.*/
    private Integer heureFin;
    
    /** Minute fin. */
    private Integer minuteFin;
    
    /** Enseignement. */
    private EnseignementDTO enseignementDTO;

    /**
     * Accesseur de date {@link #date}.
     * @return retourne date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Mutateur de date {@link #date}.
     * @param date le date to set
     */
    public void setDate(Date date) {
        this.date = date;
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
     * Accesseur de enseignementDTO {@link #enseignementDTO}.
     * @return retourne enseignementDTO
     */
    public EnseignementDTO getEnseignementDTO() {
        return enseignementDTO;
    }

    /**
     * Mutateur de enseignementDTO {@link #enseignementDTO}.
     * @param enseignementDTO le enseignementDTO to set
     */
    public void setEnseignementDTO(EnseignementDTO enseignementDTO) {
        this.enseignementDTO = enseignementDTO;
    }

    
}
