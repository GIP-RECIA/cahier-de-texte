/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConsoliderEmpForm.java,v 1.10 2010/05/20 14:42:31 ent_breyton Exp $
 */

package org.crlr.web.application.form.emploi;

import java.util.List;

import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.dto.application.emploi.SemaineDTO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.web.application.form.AbstractPopupForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.BarreMoisDTO;
import org.crlr.web.dto.FileUploadDTO;

/**
 * PlanningMensuelForm.
 *
 * @author $author$
 * @version $Revision: 1.10 $
 */
public class PlanningMensuelForm extends AbstractPopupForm {
    /** Identidiant de sérialisation. */
    private static final long serialVersionUID = 4100040089281471925L;

    
    private List<BarreMoisDTO> listeBarreMois;
    
    private List<SemaineDTO> semaines;
    
    private SemaineDTO semaineSelectionnee;
    
    private List<TypeJour> listeJoursOuvre;
    
    private BarreMoisDTO moisSelectionne;
    
    private RechercheSeanceQO rechercheSeanceQO;
    
    private DevoirDTO devoirSelected;

    private SeanceDTO seanceSelectionne;
    
    private FileUploadDTO pieceJointeASupprimer;
    
    //Profile d'utilisateur connécté
    private Profil profile;
    
   
    
    //Type devoir / devoir / tous / séance radio 
    private String choixCategorie;
    
    private List<TypeDevoirDTO> listeTypeDevoir;
    
    /** Utilise par la popup de piece jointe. */
    private String raffraichirTabAfterUpload;
    /**
     * Constructeur.
     */
    public PlanningMensuelForm() {
        reset();
    }

    /**
     * Méthode de réinitialisation des données du formulaire saisie par
     * l'utilisateur.
     */
    public void reset() {
        choixCategorie = "T";
    }
    
    /**
     * Accesseur de listeBarreMois.
     * @return le listeBarreMois
     */
    public List<BarreMoisDTO> getListeBarreMois() {
        return listeBarreMois;
    }

    /**
     * Mutateur de listeBarreMois.
     * @param listeBarreMois le listeBarreMois à modifier.
     */
    public void setListeBarreMois(List<BarreMoisDTO> listeBarreMois) {
        this.listeBarreMois = listeBarreMois;
    }

    /**
     * Accesseur de semaines.
     * @return le semaines
     */
    public List<SemaineDTO> getSemaines() {
        return semaines;
    }

    /**
     * Mutateur de semaines.
     * @param semaines le semaines à modifier.
     */
    public void setSemaines(List<SemaineDTO> semaines) {
        this.semaines = semaines;
    }

    /**
     * Accesseur de moisSelectionne.
     * @return le moisSelectionne
     */
    public BarreMoisDTO getMoisSelectionne() {
        return moisSelectionne;
    }

    /**
     * Mutateur de moisSelectionne.
     * @param moisSelectionne le moisSelectionne à modifier.
     */
    public void setMoisSelectionne(BarreMoisDTO moisSelectionne) {
        this.moisSelectionne = moisSelectionne;
    }

    /**
     * Accesseur de rechercheSeanceQO.
     * @return le rechercheSeanceQO
     */
    public RechercheSeanceQO getRechercheSeanceQO() {
        return rechercheSeanceQO;
    }

    /**
     * Mutateur de rechercheSeanceQO.
     * @param rechercheSeanceQO le rechercheSeanceQO à modifier.
     */
    public void setRechercheSeanceQO(RechercheSeanceQO rechercheSeanceQO) {
        this.rechercheSeanceQO = rechercheSeanceQO;
    }

    /**
     * Accesseur de devoirSelected.
     * @return le devoirSelectionne
     */
    public DevoirDTO getDevoirSelected() {
        return devoirSelected;
    }

    /**
     * Mutateur de devoirSelectionne.
     * @param devoirSelectionne le devoirSelectionne à modifier.
     */
    public void setDevoirSelected(DevoirDTO devoirSelected) {
        this.devoirSelected = devoirSelected;
        
    }

    /**
     * Accesseur de listeTypeDevoir.
     * @return le listeTypeDevoir
     */
    public List<TypeDevoirDTO> getListeTypeDevoir() {
        return listeTypeDevoir;
    }

    /**
     * Mutateur de listeTypeDevoir.
     * @param listeTypeDevoir le listeTypeDevoir à modifier.
     */
    public void setListeTypeDevoir(List<TypeDevoirDTO> listeTypeDevoir) {
        this.listeTypeDevoir = listeTypeDevoir;
    }

    /**
     * @return the pieceJointeASupprimer
     */
    public FileUploadDTO getPieceJointeASupprimer() {
        return pieceJointeASupprimer;
    }

    /**
     * @param pieceJointeASupprimer the pieceJointeASupprimer to set
     */
    public void setPieceJointeASupprimer(FileUploadDTO pieceJointeASupprimer) {
        this.pieceJointeASupprimer = pieceJointeASupprimer;
    }

    /**
     * @return the listeJoursOuvre
     */
    public List<TypeJour> getListeJoursOuvre() {
        return listeJoursOuvre;
    }

    /**
     * @param listeJoursOuvre the listeJoursOuvre to set
     */
    public void setListeJoursOuvre(List<TypeJour> listeJoursOuvre) {
        this.listeJoursOuvre = listeJoursOuvre;
    }

    /**
     * @return the profile
     */
    public Profil getProfile() {
        return profile;
    }

    /**
     * @param profile the profile to set
     */
    public void setProfile(Profil profile) {
        this.profile = profile;
    }

    /**
     * @return the choixCategorie
     */
    public String getChoixCategorie() {
        return choixCategorie;
    }

    /**
     * @param choixCategorie the choixCategorie to set
     */
    public void setChoixCategorie(String choixCategorie) {
        this.choixCategorie = choixCategorie;
    }

    /**
     * @return the semaineSelectionnee
     */
    public SemaineDTO getSemaineSelectionnee() {
        return semaineSelectionnee;
    }

    /**
     * @param semaineSelectionnee the semaineSelectionnee to set
     */
    public void setSemaineSelectionnee(SemaineDTO semaineSelectionnee) {
        this.semaineSelectionnee = semaineSelectionnee;
    }

    /**
     * Accesseur de seanceSelectionne {@link #seanceSelectionne}.
     * @return retourne seanceSelectionne
     */
    public SeanceDTO getSeanceSelectionne() {
        return seanceSelectionne;
    }

    /**
     * Mutateur de seanceSelectionne {@link #seanceSelectionne}.
     * @param seanceSelectionne le seanceSelectionne to set
     */
    public void setSeanceSelectionne(SeanceDTO seanceSelectionne) {
        this.seanceSelectionne = seanceSelectionne;
        
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

   

    
    
}
