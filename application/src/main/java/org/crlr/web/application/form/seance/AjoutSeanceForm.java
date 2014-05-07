/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AjoutSeanceForm.java,v 1.9 2009/07/10 15:32:06 vibertd Exp $
 */

package org.crlr.web.application.form.seance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.crlr.dto.application.base.DateDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.dto.application.seance.ResultatRechercheSeanceDTO;
import org.crlr.utils.DateUtils;
import org.crlr.web.application.form.AbstractPopupForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.dto.TypeCouleur;

/**
 * @author $author$
 * @version $Revision: 1.9 $
 */
public class AjoutSeanceForm extends AbstractPopupForm {
    /** Serial.  */
    private static final long serialVersionUID = -8937663948104208816L;

    /** Positionne si on vient de la recherche seance (modif / supp).*/
    private ResultatRechercheSeanceDTO resultatRechercheSeanceDTO;
    
    /** Liste des sequence qui sont valide à une date donnée. */
    private List<SequenceDTO> listeSequence;

    /** Liste des sequence qui sont valide à une date donnée pour la matiere/classe. */
    private List<SequenceDTO> listeSequenceSeance;
    
    /** Liste des types de devoir. */ 
    private List<TypeDevoirDTO> listeTypeDevoir;
    
    /** Nombre de devoir par defaut pour une seance. */
    private Integer nombreDevoirParDefaut;
    
    /** Nombre de seances precedentes par defaut affiche. */
    private Integer nombreSeancePrecedenteParDefaut;
    
    /** Liste des seances precedentes. */
    private List<SeanceDTO> listeSeancePrecedente;
    
    /** Liste des sequences dispo pour la seance precedente. */
    private List<SequenceDTO> listeSequenceSeancePrecedente;
    
    /** Sequence de la seance precedente. */
    private SequenceDTO sequenceSeancePrecedenteSelected;
    
    /** Liste des dates de remises de devoir proposees. */
    private List<DateDTO> listeDateRemiseDevoir;
    
    /** Liste des dates des prochaines remises de devoirs proposé pour la seance precedente. */
    private List<DateDTO> listeDateRemiseDevoirSeancePrecedente;
    
    /** La seance qui est en cours d'edition. */
    private SeanceDTO seance;

    /** La seance precedente qui est en cours d'edition. */
    private SeanceDTO seancePrecedente;
    
    /** La seance qui a ete copiee.  Stocké dans la session pour pouvoire copier / coller à travers des écrans */
   // private SeanceDTO seanceCopie;
    private static final String SEANCE_COPIE_CLE = "seanceCopie";
    
    /** Le devoir qui est modifiee. */
    private DevoirDTO devoirSelected;
    
    /** Piece jointe qui doit etre supprimee. */
    private FileUploadDTO pieceJointeASupprimer;
    
    /** Variable volatile qui va stocker l'id de la zone a rafraichir suite à un upload de piece jointe.*/
    private String raffraichirTabAfterUpload;
    
    /** Affiche le bouton de retour ou pas. */
    private Boolean afficheRetour;
    
    /** Affichage du bouton de fleche bas pour charger la suite des seance precedentes. */
    private Boolean afficheSuiteSeancePrecedente;

    /** Gestion du mode archive. */
    private Boolean modeArchive;
    
    /** Exercice du mode archive. */
    private String exercice;
    
    /** Type couleur sélectionné. */
    private TypeCouleur typeCouleur;
    /**
     * Reset les donnees du formulaire.
     */
    public void reset() {
        listeTypeDevoir = new ArrayList<TypeDevoirDTO>();
        seance = new SeanceDTO();
        seance.setDate(new Date());
        seance.setHeureDebut(-1);
        seance.setMinuteDebut(-1);
        seance.setHeureFin(-1);
        seance.setMinuteFin(-1);
        afficheRetour = false;
        listeSequenceSeance = new ArrayList<SequenceDTO>();
        listeSequence = new ArrayList<SequenceDTO>();
    }
    
    /**
     * Indique si la seance en cours d'edition existe en bas ou non.
     * @return true/false
     */
    public Boolean getVraixOuFauxSeanceExiste() {
        final SeanceDTO seance = this.getSeance();
        return (!(seance==null || seance.getId()==null));
    }


    /**
     * Accesseur de listeTypeDevoir {@link #listeTypeDevoir}.
     * @return retourne listeTypeDevoir
     */
    public List<TypeDevoirDTO> getListeTypeDevoir() {
        return listeTypeDevoir;
    }

    /**
     * Mutateur de listeTypeDevoir {@link #listeTypeDevoir}.
     * @param listeTypeDevoir le listeTypeDevoir to set
     */
    public void setListeTypeDevoir(List<TypeDevoirDTO> listeTypeDevoir) {
        this.listeTypeDevoir = listeTypeDevoir;
    }

    /**
     * Accesseur de nombreDevoirParDefaut {@link #nombreDevoirParDefaut}.
     * @return retourne nombreDevoirParDefaut
     */
    public Integer getNombreDevoirParDefaut() {
        return nombreDevoirParDefaut;
    }

    /**
     * Mutateur de nombreDevoirParDefaut {@link #nombreDevoirParDefaut}.
     * @param nombreDevoirParDefaut le nombreDevoirParDefaut to set
     */
    public void setNombreDevoirParDefaut(Integer nombreDevoirParDefaut) {
        this.nombreDevoirParDefaut = nombreDevoirParDefaut;
    }

    /**
     * Accesseur de nombreSeancePrecedenteParDefaut {@link #nombreSeancePrecedenteParDefaut}.
     * @return retourne nombreSeancePrecedenteParDefaut
     */
    public Integer getNombreSeancePrecedenteParDefaut() {
        return nombreSeancePrecedenteParDefaut;
    }

    /**
     * Mutateur de nombreSeancePrecedenteParDefaut {@link #nombreSeancePrecedenteParDefaut}.
     * @param nombreSeancePrecedenteParDefaut le nombreSeancePrecedenteParDefaut to set
     */
    public void setNombreSeancePrecedenteParDefaut(
            Integer nombreSeancePrecedenteParDefaut) {
        this.nombreSeancePrecedenteParDefaut = nombreSeancePrecedenteParDefaut;
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

    /**
     * Accesseur de listeSeancePrecedente {@link #listeSeancePrecedente}.
     * @return retourne listeSeancePrecedente
     */
    public List<SeanceDTO> getListeSeancePrecedente() {
        return listeSeancePrecedente;
    }

    /**
     * Mutateur de listeSeancePrecedente {@link #listeSeancePrecedente}.
     * @param listeSeancePrecedente le listeSeancePrecedente to set
     */
    public void setListeSeancePrecedente(List<SeanceDTO> listeSeancePrecedente) {
        this.listeSeancePrecedente = listeSeancePrecedente;
    }

    /**
     * Accesseur de listeDateRemiseDevoir {@link #listeDateRemiseDevoir}.
     * @return retourne listeDateRemiseDevoir
     */
    public List<DateDTO> getListeDateRemiseDevoir() {
        return listeDateRemiseDevoir;
    }

    /**
     * Mutateur de listeDateRemiseDevoir {@link #listeDateRemiseDevoir}.
     * @param listeDateRemiseDevoir le listeDateRemiseDevoir to set
     */
    public void setListeDateRemiseDevoir(List<DateDTO> listeDateRemiseDevoir) {
        this.listeDateRemiseDevoir = listeDateRemiseDevoir;
    }

    /**
     * Accesseur de seance {@link #seance}.
     * @return retourne seance
     */
    public SeanceDTO getSeance() {
        return seance;
    }

    /**
     * Mutateur de seance {@link #seance}.
     * @param seance le seance to set
     */
    public void setSeance(SeanceDTO seance) {
        this.seance = seance;
    }

    /**
     * Accesseur de listeSequenceSeance {@link #listeSequenceSeance}.
     * @return retourne listeSequenceSeance
     */
    public List<SequenceDTO> getListeSequenceSeance() {
        return listeSequenceSeance;
    }

    /**
     * Mutateur de listeSequenceSeance {@link #listeSequenceSeance}.
     * @param listeSequenceSeance le listeSequenceSeance to set
     */
    public void setListeSequenceSeance(List<SequenceDTO> listeSequenceSeance) {
        this.listeSequenceSeance = listeSequenceSeance;
    }

    /**
     * Accesseur de seanceCopie {@link #seanceCopie}.
     * @return retourne seanceCopie
     */
    public SeanceDTO getSeanceCopie() {
        return (SeanceDTO) ContexteUtils
        .getContexteOutilControl().recupererObjet(SEANCE_COPIE_CLE);
    }

    /**
     * Mutateur de seanceCopie {@link #seanceCopie}.
     * @param seanceCopie le seanceCopie to set
     */
    public void setSeanceCopie(SeanceDTO seanceCopie) {
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(SEANCE_COPIE_CLE, seanceCopie);
    }

    /**
     * Accesseur de devoirSelected {@link #devoirSelected}.
     * @return retourne devoirSelected
     */
    public DevoirDTO getDevoirSelected() {
        return devoirSelected;
    }

    /**
     * Mutateur de devoirSelected {@link #devoirSelected}.
     * @param devoirSelected le devoirSelected to set
     */    
    public void setDevoirSelected(DevoirDTO devoirSelected) {
        this.devoirSelected = devoirSelected;
    }

    /**
     * Accesseur de seancePrecedente {@link #seancePrecedente}.
     * @return retourne seancePrecedente
     */
    public SeanceDTO getSeancePrecedente() {
        return seancePrecedente;
    }

    /**
     * Mutateur de seancePrecedente {@link #seancePrecedente}.
     * @param seancePrecedente le seancePrecedente to set
     */
    public void setSeancePrecedente(SeanceDTO seancePrecedente) {
        this.seancePrecedente = seancePrecedente;
    }

    /**
     * Accesseur de pieceJointeASupprimer {@link #pieceJointeASupprimer}.
     * @return retourne pieceJointeASupprimer
     */
    public FileUploadDTO getPieceJointeASupprimer() {
        return pieceJointeASupprimer;
    }

    /**
     * Mutateur de pieceJointeASupprimer {@link #pieceJointeASupprimer}.
     * @param pieceJointeASupprimer le pieceJointeASupprimer to set
     */
    public void setPieceJointeASupprimer(FileUploadDTO pieceJointeASupprimer) {
        this.pieceJointeASupprimer = pieceJointeASupprimer;
    }

    /**
     * Accesseur de raffraichirTabAfterUpload {@link #raffraichirTabAfterUpload}.
     * @return retourne raffraichirTabAfterUpload
     */
    public String getRaffraichirTabAfterUpload() {
        return raffraichirTabAfterUpload;
    }

    /**
     * Mutateur de raffraichirTabAfterUpload {@link #raffraichirTabAfterUpload}.
     * @param raffraichirTabAfterUpload le raffraichirTabAfterUpload to set
     */
    public void setRaffraichirTabAfterUpload(String raffraichirTabAfterUpload) {
        this.raffraichirTabAfterUpload = raffraichirTabAfterUpload;
    }

    /**
     * Accesseur de listeSequenceSeancePrecedente {@link #listeSequenceSeancePrecedente}.
     * @return retourne listeSequenceSeancePrecedente
     */
    public List<SequenceDTO> getListeSequenceSeancePrecedente() {
        return listeSequenceSeancePrecedente;
    }

    /**
     * Mutateur de listeSequenceSeancePrecedente {@link #listeSequenceSeancePrecedente}.
     * @param listeSequenceSeancePrecedente le listeSequenceSeancePrecedente to set
     */
    public void setListeSequenceSeancePrecedente(
            List<SequenceDTO> listeSequenceSeancePrecedente) {
        this.listeSequenceSeancePrecedente = listeSequenceSeancePrecedente;
    }

    /**
     * Accesseur de sequenceSeancePrecedenteSelected {@link #sequenceSeancePrecedenteSelected}.
     * @return retourne sequenceSeancePrecedenteSelected
     */
    public SequenceDTO getSequenceSeancePrecedenteSelected() {
        return sequenceSeancePrecedenteSelected;
    }

    /**
     * Mutateur de sequenceSeancePrecedenteSelected {@link #sequenceSeancePrecedenteSelected}.
     * @param sequenceSeancePrecedenteSelected le sequenceSeancePrecedenteSelected to set
     */
    public void setSequenceSeancePrecedenteSelected(
            SequenceDTO sequenceSeancePrecedenteSelected) {
        this.sequenceSeancePrecedenteSelected = sequenceSeancePrecedenteSelected;
    }

    /**
     * Accesseur de listeDateRemiseDevoirSeancePrecedente {@link #listeDateRemiseDevoirSeancePrecedente}.
     * @return retourne listeDateRemiseDevoirSeancePrecedente
     */
    public List<DateDTO> getListeDateRemiseDevoirSeancePrecedente() {
        return listeDateRemiseDevoirSeancePrecedente;
    }

    /**
     * Mutateur de listeDateRemiseDevoirSeancePrecedente {@link #listeDateRemiseDevoirSeancePrecedente}.
     * @param listeDateRemiseDevoirSeancePrecedente le listeDateRemiseDevoirSeancePrecedente to set
     */
    public void setListeDateRemiseDevoirSeancePrecedente(
            List<DateDTO> listeDateRemiseDevoirSeancePrecedente) {
        this.listeDateRemiseDevoirSeancePrecedente = listeDateRemiseDevoirSeancePrecedente;
    }
 
    /**
     * Getter du libelle affichée pour le type classe ou groupe.
     * @return Classe ou Groupe
     */
    public String getClasseGroupeType() {
        if (getSeance() != null && getSeance().getSequence() != null) {
            if (TypeGroupe.CLASSE.equals(getSeance().getSequence().getTypeGroupe())) {
                return "Classe";
            } else {
                return "Groupe";
            }
        } else {
            return "Classe";
        }
    }

    /**
     * Getter de la designation de la classe ou groupe.
     * @return la designation de la Classe ou Groupe
     */
    public String getClasseGroupeDesignation() {
        if (getSeance() != null && getSeance().getSequence() != null) {
            return getSeance().getSequence().getDesignationClasseGroupe();
        } else {
            return "";
        }
    }

    /**
     * Getter de la designation de l'enseignement.
     * @return le libelle de l'enseignement
     */
    public String getEnseignementDesignation() {
        if (getSeance() != null && getSeance().getSequence() != null) {
            return getSeance().getSequence().getDesignationEnseignement();
        } else {
            return "";
        }
    }

    /**
     * Retourne le titre de l'onget Seance a afficher. 
     * @return le titre de l'onglet seance
     */
    private String getSeanceTitreGenerique(final SequenceDTO sequence, final Date date) {
       String result = "Séance";
       if (sequence != null && date != null) {
           result += " - " + DateUtils.format(date);
           if (sequence.getDesignationClasseGroupe() != null) {
               result += " - " + sequence.getDesignationClasseGroupe();
           }
           if (sequence.getDesignationEnseignement() != null) {
               result += " - " + sequence.getDesignationEnseignement();
           }
       }
       return result;
    }
    
    @Deprecated
    public SequenceDTO getSequenceSelected() {
        if (null != getSeance()) {
            return getSeance().getSequence();
        }
        
        return null;
    }
    
    /**
     * Retourne le titre de l'onget Seance a afficher. 
     * @return le titre de l'onglet seance
     */
    public String getSeanceTitre() {
        final SeanceDTO seance = getSeance();
        if (seance != null) {
            return getSeanceTitreGenerique( getSeance().getSequence(), seance.getDate());
        } else {
            return "Séance";
        }
    }

    /**
     * Retourne le titre de la seance precedente en cours d'edition.
     * @return le titre
     */
    public String getSeancePrecenteTitre() {
        final SeanceDTO seance = getSeancePrecedente();
        if (seance != null) {
            return getSeanceTitreGenerique(sequenceSeancePrecedenteSelected, seance.getDate());
        } else {
            return "Séance";
        }
    }
    
    /**
     * Verifie si la selection de sequence est necessaire pour la saisie de la seance precedente.
     * @return true / false
     */
    public Boolean getAfficheSelectionSequenceSeancePrecedente() {
        return (!CollectionUtils.isEmpty(listeSequenceSeancePrecedente) && listeSequenceSeancePrecedente.size()>1);
    }

    /**
     * Verifie si la selection de sequence est necessaire pour la saisie de la seance.
     * @return true / false
     */
    public Boolean getAfficheSelectionSequenceSeance() {
        return (!CollectionUtils.isEmpty(listeSequenceSeance) && listeSequenceSeance.size()>1);
    }
    
    /**
     * Accesseur de resultatRechercheSeanceDTO {@link #resultatRechercheSeanceDTO}.
     * @return retourne resultatRechercheSeanceDTO
     */
    public ResultatRechercheSeanceDTO getResultatRechercheSeanceDTO() {
        return resultatRechercheSeanceDTO;
    }

    /**
     * Mutateur de resultatRechercheSeanceDTO {@link #resultatRechercheSeanceDTO}.
     * @param resultatRechercheSeanceDTO le resultatRechercheSeanceDTO to set
     */
    public void setResultatRechercheSeanceDTO(
            ResultatRechercheSeanceDTO resultatRechercheSeanceDTO) {
        this.resultatRechercheSeanceDTO = resultatRechercheSeanceDTO;
    }

    /**
     * @return the afficheRetour
     */
    public Boolean getAfficheRetour() {
        return afficheRetour;
    }

    /**
     * @param afficheRetour the afficheRetour to set
     */
    public void setAfficheRetour(Boolean afficheRetour) {
        this.afficheRetour = afficheRetour;
    }

    /**
     * Accesseur de modeArchive {@link #modeArchive}.
     * @return retourne modeArchive
     */
    public Boolean getModeArchive() {
        return modeArchive;
    }

    /**
     * Mutateur de modeArchive {@link #modeArchive}.
     * @param modeArchive le modeArchive to set
     */
    public void setModeArchive(Boolean modeArchive) {
        this.modeArchive = modeArchive;
    }

    /**
     * Accesseur de exercice {@link #exercice}.
     * @return retourne exercice
     */
    public String getExercice() {
        return exercice;
    }

    /**
     * Mutateur de exercice {@link #exercice}.
     * @param exercice le exercice to set
     */
    public void setExercice(String exercice) {
        this.exercice = exercice;
    }

    /**
     * Accesseur de afficheSuiteSeancePrecedente {@link #afficheSuiteSeancePrecedente}.
     * @return retourne afficheSuiteSeancePrecedente
     */
    public Boolean getAfficheSuiteSeancePrecedente() {
        return afficheSuiteSeancePrecedente;
    }

    /**
     * Mutateur de afficheSuiteSeancePrecedente {@link #afficheSuiteSeancePrecedente}.
     * @param afficheSuiteSeancePrecedente le afficheSuiteSeancePrecedente to set
     */
    public void setAfficheSuiteSeancePrecedente(Boolean afficheSuiteSeancePrecedente) {
        this.afficheSuiteSeancePrecedente = afficheSuiteSeancePrecedente;
    }

	public TypeCouleur getTypeCouleur() {
		return typeCouleur;
	}

	public void setTypeCouleur(TypeCouleur typeCouleur) {
		this.typeCouleur = typeCouleur;
	}

    
}

