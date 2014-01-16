package org.crlr.dto.application.cycle;

import java.io.Serializable;

/**
 * Recherche les seances d'un sycle. 
 * @author G-SAFIR-FRMP
 *
 */
public class RechercheCycleSeanceQO  implements Serializable {

    /**  Serial. */
    private static final long serialVersionUID = -1453509916683148126L;

    /** Le cycle. */
    private CycleDTO cycleDTO;
    
    /** Est-ce qu'on charge le complement des seance/devoirs . */
    private Boolean avecDetail;

    /**
     * Accesseur de cycleDTO {@link #cycleDTO}.
     * @return retourne cycleDTO
     */
    public CycleDTO getCycleDTO() {
        return cycleDTO;
    }

    /**
     * Mutateur de cycleDTO {@link #cycleDTO}.
     * @param cycleDTO le cycleDTO to set
     */
    public void setCycleDTO(CycleDTO cycleDTO) {
        this.cycleDTO = cycleDTO;
    }

    /**
     * Accesseur de avecDetail {@link #avecDetail}.
     * @return retourne avecDetail
     */
    public Boolean getAvecDetail() {
        return avecDetail;
    }

    /**
     * Mutateur de avecDetail {@link #avecDetail}.
     * @param avecDetail le avecDetail to set
     */
    public void setAvecDetail(Boolean avecDetail) {
        this.avecDetail = avecDetail;
    }

    
}
