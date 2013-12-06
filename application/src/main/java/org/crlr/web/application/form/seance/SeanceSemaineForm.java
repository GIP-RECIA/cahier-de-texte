/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SeanceSemaineForm.java,v 1.9 2009/07/10 15:32:06 vibertd Exp $
 */

package org.crlr.web.application.form.seance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.TypeCategorieTypeDevoir;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.devoir.DetailJourDTO;
import org.crlr.utils.DateUtils;
import org.crlr.web.application.form.AbstractPopupForm;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.dto.JoursDTO;
import org.crlr.web.dto.MoisDTO;

/**
 * Form de l'ecran de visualisation des seances par semaine.
 * @author G-SAFIR-FRMP
 *
 */
public class SeanceSemaineForm extends AbstractPopupForm {

    /**  Serial. */
    private static final long serialVersionUID = -8492253951449643311L;

    /** Indique si la recherche a ete lancee ou non. Utilise pour savoir s'il faut relancer la recherche
     * ou bien attendre que l'utilisateur clique sur recherche.*/
    private Boolean vraiOuFauxRechercheActive;

    /** liste des années scolaires. */
    private List<AnneeScolaireDTO> listeAnneeScolaire; 
    
    /** Id de l'annee scolaire selectionnee dans la liste des archives. */
    private Integer idAnneeScolaire;
    
    /** Indique si on est en mode visu d'une archive ou pas. */
    private Boolean modeArchive; 
    
    /** Liste des jours qui sont ouvres pour l etablissement. */
    private List<TypeJour> listeJoursOuvre;
    
    /** La barre de semaine pour l'alternance des semaines. */ 
    private List<BarreSemaineDTO> listeBarreSemaine;

    /** Liste des mois. */ 
    private List<MoisDTO> listeMois;

    /** Indique le decalage de debut d'annee pour afficher le premier mois. */
    private Float decalageDebutAnnee;
    
    /** Numero de la semaine selectionnee. */
    private BarreSemaineDTO semaineSelectionne;
    
    /** Liste resultat des jours contenant des seance. */
    private List<JoursDTO> liste;
    
    /** Liste des seance non map .*/
    private List<DetailJourDTO> listeSeance;
      
    /** Jour / Seance selectionnee. */
    private DetailJourDTO selectSeance;
    
    /** Seance correspondant au DetailJourSeanceDTO selectionnee. */
    private SeanceDTO seanceSelected;
    
    /** Piece jointe qui va etre supprimée d'un devoir.*/
    private FileUploadDTO pieceJointeASupprimer;
    
    /** Variable volatile qui va stocker l'id de la zone a rafraichir suite à un upload de piece jointe.*/
    private String raffraichirTabAfterUpload;

    /** Date de fin d'annee scolaire. */
    private Date finAnneeScolaire;

    /** Categorie du type de travail a faire recherche. */
    private String categorieSelectionne;
    
    /** Type d'affichage choisi : SEMAINE ou LISTE. */
    private String typeAffichage;
    
    /** Date de debut de recherche. */
    private Date dateDebut;
    
    /** Date de fin de recherche. */
    private Date dateFin;
    

    /**
     * Initialisation des valeurs par defaut.
     */
    public void reset() {
        listeBarreSemaine = new ArrayList<BarreSemaineDTO>();
        semaineSelectionne = null;
        categorieSelectionne = TypeCategorieTypeDevoir.NORMAL.getId();
        typeAffichage = "SEMAINE";
        listeSeance = new ArrayList<DetailJourDTO>();
        vraiOuFauxRechercheActive = false;
    }
    
   

    /**
     * Accesseur de liste {@link #liste}.
     * @return retourne liste
     */
    public List<JoursDTO> getListe() {
        return liste;
    }

    /**
     * Mutateur de liste {@link #liste}.
     * @param liste le liste to set
     */
    public void setListe(List<JoursDTO> liste) {
        this.liste = liste;
    }

    /**
     * Accesseur de listeBarreSemaine {@link #listeBarreSemaine}.
     * @return retourne listeBarreSemaine
     */
    public List<BarreSemaineDTO> getListeBarreSemaine() {
        return listeBarreSemaine;
    }

    /**
     * Mutateur de listeBarreSemaine {@link #listeBarreSemaine}.
     * @param listeBarreSemaine le listeBarreSemaine to set
     */
    public void setListeBarreSemaine(List<BarreSemaineDTO> listeBarreSemaine) {
        this.listeBarreSemaine = listeBarreSemaine;
    }

    /**
     * Accesseur de listeMois {@link #listeMois}.
     * @return retourne listeMois
     */
    public List<MoisDTO> getListeMois() {
        return listeMois;
    }

    /**
     * Mutateur de listeMois {@link #listeMois}.
     * @param listeMois le listeMois to set
     */
    public void setListeMois(List<MoisDTO> listeMois) {
        this.listeMois = listeMois;
    }

    /**
     * Accesseur de decalageDebutAnnee {@link #decalageDebutAnnee}.
     * @return retourne decalageDebutAnnee
     */
    public Float getDecalageDebutAnnee() {
        return decalageDebutAnnee;
    }

    /**
     * Mutateur de decalageDebutAnnee {@link #decalageDebutAnnee}.
     * @param decalageDebutAnnee le decalageDebutAnnee to set
     */
    public void setDecalageDebutAnnee(Float decalageDebutAnnee) {
        this.decalageDebutAnnee = decalageDebutAnnee;
    }

    /**
     * Accesseur de semaineSelectionne {@link #semaineSelectionne}.
     * @return retourne semaineSelectionne
     */
    public BarreSemaineDTO getSemaineSelectionne() {
        return semaineSelectionne;
    }

    /**
     * Mutateur de semaineSelectionne {@link #semaineSelectionne}.
     * @param semaineSelectionne le semaineSelectionne to set
     */
    public void setSemaineSelectionne(BarreSemaineDTO semaineSelectionne) {
        this.semaineSelectionne = semaineSelectionne;
    }

    /**
     * Faux getteur du numero de semaine. 
     * @return le numero de la semaine selectionnee
     */
    public String getNumeroSemaineSelectionne() {
        if (semaineSelectionne == null) {
            return "";
        } else {
            return semaineSelectionne.getNumeroSemaine();
        }
    }

    /**
     * Faux setter du numero de semaine. 
     * @param numero : recherche dans la barre de semaine, celle avec ce numero. 
     * Coche cette semaine et décoche toutes les autres. Et positionne dans le form cette semaine.
     */
    public void setNumeroSemaineSelectionne(final String numero) {
        semaineSelectionne = null;
        for(final BarreSemaineDTO semaine : listeBarreSemaine) {
            if (semaine.getNumeroSemaine().equals(numero)) {
                semaine.setVraiOuFauxSelection(true);
                semaineSelectionne = semaine;
            } else {
                semaine.setVraiOuFauxSelection(false);
            }
        }
    }
    
    /**
     * Accesseur de titreNavigateurSemaine {@link #titreNavigateurSemaine}.
     * @return retourne titreNavigateurSemaine
     */
    public String getTitreNavigateurSemaine() {
        if (semaineSelectionne!=null) {
            final String titreNavigateurSemaine = "Semaine " + semaineSelectionne.getNumeroSemaine() + " - du " +
                DateUtils.format(semaineSelectionne.getLundi(), "d") +
                " au " + DateUtils.format(semaineSelectionne.getDimanche(), "d MMMM yyyy");
            return titreNavigateurSemaine;
        } else {
            return "";
        }
    }
    
    /**
     * Accesseur de selectSeance {@link #selectSeance}.
     * @return retourne selectSeance
     */
    public DetailJourDTO getSelectSeance() {
        return selectSeance;
    }

    /**
     * Mutateur de selectSeance {@link #selectSeance}.
     * @param selectSeance le selectSeance to set
     */
    public void setSelectSeance(DetailJourDTO selectSeance) {
        this.selectSeance = selectSeance;
    }

    /**
     * Accesseur de seanceSelected {@link #seanceSelected}.
     * @return retourne seanceSelected
     */
    public SeanceDTO getSeanceSelected() {
        return seanceSelected;
    }

    /**
     * Mutateur de seanceSelected {@link #seanceSelected}.
     * @param seanceSelected le seanceSelected to set
     */
    public void setSeanceSelected(SeanceDTO seanceSelected) {
        this.seanceSelected = seanceSelected;
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
     * Accesseur de finAnneeScolaire {@link #finAnneeScolaire}.
     * @return retourne finAnneeScolaire
     */
    public Date getFinAnneeScolaire() {
        return finAnneeScolaire;
    }

    /**
     * Mutateur de finAnneeScolaire {@link #finAnneeScolaire}.
     * @param finAnneeScolaire le finAnneeScolaire to set
     */
    public void setFinAnneeScolaire(Date finAnneeScolaire) {
        this.finAnneeScolaire = finAnneeScolaire;
    }

    /**
     * Accesseur de categorieSelectionne {@link #categorieSelectionne}.
     * @return retourne categorieSelectionne
     */
    public String getCategorieSelectionne() {
        return categorieSelectionne;
    }

    /**
     * Mutateur de categorieSelectionne {@link #categorieSelectionne}.
     * @param categorieSelectionne le categorieSelectionne to set
     */
    public void setCategorieSelectionne(String categorieSelectionne) {
        this.categorieSelectionne = categorieSelectionne;
    }

    /**
     * Accesseur de typeAffichage {@link #typeAffichage}.
     * @return retourne typeAffichage
     */
    public String getTypeAffichage() {
        return typeAffichage;
    }

    /**
     * Mutateur de typeAffichage {@link #typeAffichage}.
     * @param typeAffichage le typeAffichage to set
     */
    public void setTypeAffichage(String typeAffichage) {
        this.typeAffichage = typeAffichage;
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
     * Accesseur de listeSeance {@link #listeSeance}.
     * @return retourne listeSeance
     */
    public List<DetailJourDTO> getListeSeance() {
        return listeSeance;
    }

    /**
     * Mutateur de listeSeance {@link #listeSeance}.
     * @param listeSeance le listeSeance to set
     */
    public void setListeSeance(List<DetailJourDTO> listeSeance) {
        this.listeSeance = listeSeance;
    }

    

    /**
     * Accesseur de vraiOuFauxRechercheActive {@link #vraiOuFauxRechercheActive}.
     * @return retourne vraiOuFauxRechercheActive
     */
    public Boolean getVraiOuFauxRechercheActive() {
        return vraiOuFauxRechercheActive;
    }

    /**
     * Mutateur de vraiOuFauxRechercheActive {@link #vraiOuFauxRechercheActive}.
     * @param vraiOuFauxRechercheActive le vraiOuFauxRechercheActive to set
     */
    public void setVraiOuFauxRechercheActive(Boolean vraiOuFauxRechercheActive) {
        this.vraiOuFauxRechercheActive = vraiOuFauxRechercheActive;
    }

   
   
    /**
     * Accesseur de listeAnneeScolaire {@link #listeAnneeScolaire}.
     * @return retourne listeAnneeScolaire
     */
    public List<AnneeScolaireDTO> getListeAnneeScolaire() {
        return listeAnneeScolaire;
    }

    /**
     * Mutateur de listeAnneeScolaire {@link #listeAnneeScolaire}.
     * @param listeAnneeScolaire le listeAnneeScolaire to set
     */
    public void setListeAnneeScolaire(List<AnneeScolaireDTO> listeAnneeScolaire) {
        this.listeAnneeScolaire = listeAnneeScolaire;
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
     * Accesseur de idAnneeScolaire {@link #idAnneeScolaire}.
     * @return retourne idAnneeScolaire
     */
    public Integer getIdAnneeScolaire() {
        return idAnneeScolaire;
    }



    /**
     * Mutateur de idAnneeScolaire {@link #idAnneeScolaire}.
     * @param idAnneeScolaire le idAnneeScolaire to set
     */
    public void setIdAnneeScolaire(Integer idAnneeScolaire) {
        this.idAnneeScolaire = idAnneeScolaire;
    }

    /**
     * Retourne l'exercice correspondant à l'ID annee scolaire selectionnee.
     * @return exercice
     */
    public String getExercice() {
        if (this.idAnneeScolaire == null) {
            return null;
        }
        for (final AnneeScolaireDTO annee : listeAnneeScolaire) {
            if (annee.getId().equals(idAnneeScolaire)) {
                return annee.getExercice();
            }
        }
        return null;
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

    
}
