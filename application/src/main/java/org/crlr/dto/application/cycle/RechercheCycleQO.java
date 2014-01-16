package org.crlr.dto.application.cycle;

import java.io.Serializable;

/**
 * Sequence pedagogique. 
 * @author G-SAFIR-FRMP
 *
 */
public class RechercheCycleQO  implements Serializable {

    /**  Serial. */
    private static final long serialVersionUID = -7028883711757860003L;

    /** Titre de sequence pedagogique. */
    private String titre;

    /** Objectif. */
    private String objectif;
    
    /** Enseignement. */
    private String enseignement;

    /** Id de l'enseignant connecte. */
    private Integer idEnseignant;
    
    /**
     * Accesseur de titre {@link #titre}.
     * @return retourne titre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Mutateur de titre {@link #titre}.
     * @param titre le titre to set
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Accesseur de objectif {@link #objectif}.
     * @return retourne objectif
     */
    public String getObjectif() {
        return objectif;
    }

    /**
     * Mutateur de objectif {@link #objectif}.
     * @param objectif le objectif to set
     */
    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    /**
     * Accesseur de enseignement {@link #enseignement}.
     * @return retourne enseignement
     */
    public String getEnseignement() {
        return enseignement;
    }

    /**
     * Mutateur de enseignement {@link #enseignement}.
     * @param enseignement le enseignement to set
     */
    public void setEnseignement(String enseignement) {
        this.enseignement = enseignement;
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

    
}
