package org.crlr.dto.application.cycle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.SequenceDTO;

/**
 * Recherche les case EDT pour creation auto des seances a partir d'un cycle. 
 * @author G-SAFIR-FRMP
 *
 */
public class RechercheCycleEmploiQO  implements Serializable {

    /**  Serial. */
    private static final long serialVersionUID = -6495518392858658162L;
    
    /** Date de depart pour la recherche. */
    private Date dateDepart;

    /** Id de l'etablissement. */
    private Integer idEtablissement;
    
    /** Annee scolaire. */
    private AnneeScolaireDTO anneeScolaire;
    
    /** Id de l'enseignant. */
    private Integer idEnseignant;
    
    /** Id de la classe ou du groupe. */
    private GroupesClassesDTO groupeClasse;

    /** Liste des CycleSeanceFinal. */
    private List<CycleSeanceFinalDTO> listeSeance;
    
    /** Liste des sequences de l'enseignant concernant cette classe/groupe. */
    private List<SequenceDTO> listeSequence;
    
    /**
     * Accesseur de dateDepart {@link #dateDepart}.
     * @return retourne dateDepart
     */
    public Date getDateDepart() {
        return dateDepart;
    }

    /**
     * Mutateur de dateDepart {@link #dateDepart}.
     * @param dateDepart le dateDepart to set
     */
    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
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
     * Accesseur de groupeClasse {@link #groupeClasse}.
     * @return retourne groupeClasse
     */
    public GroupesClassesDTO getGroupeClasse() {
        return groupeClasse;
    }

    /**
     * Mutateur de groupeClasse {@link #groupeClasse}.
     * @param groupeClasse le groupeClasse to set
     */
    public void setGroupeClasse(GroupesClassesDTO groupeClasse) {
        this.groupeClasse = groupeClasse;
    }

    /**
     * Accesseur de listeSeance {@link #listeSeance}.
     * @return retourne listeSeance
     */
    public List<CycleSeanceFinalDTO> getListeSeance() {
        return listeSeance;
    }

    /**
     * Mutateur de listeSeance {@link #listeSeance}.
     * @param listeSeance le listeSeance to set
     */
    public void setListeSeance(List<CycleSeanceFinalDTO> listeSeance) {
        this.listeSeance = listeSeance;
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
     * Accesseur de anneeScolaire {@link #anneeScolaire}.
     * @return retourne anneeScolaire
     */
    public AnneeScolaireDTO getAnneeScolaire() {
        return anneeScolaire;
    }

    /**
     * Mutateur de anneeScolaire {@link #anneeScolaire}.
     * @param anneeScolaire le anneeScolaire to set
     */
    public void setAnneeScolaire(AnneeScolaireDTO anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    /**
     * Accesseur de listeSequence {@link #listeSequence}.
     * @return retourne listeSequence
     */
    public List<SequenceDTO> getListeSequence() {
        return listeSequence;
    }

    /**
     * Mutateur de listeSequence {@link #listeSequence}.
     * @param listeSequence le listeSequence to set
     */
    public void setListeSequence(List<SequenceDTO> listeSequence) {
        this.listeSequence = listeSequence;
    }

}
