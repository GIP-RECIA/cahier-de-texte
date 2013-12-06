/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirForm.java,v 1.9 2009/07/10 15:32:06 vibertd Exp $
 */

package org.crlr.web.application.form.devoir;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.crlr.dto.application.base.TypeCategorieTypeDevoir;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.devoir.DetailJourDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.utils.DateUtils;
import org.crlr.web.application.form.AbstractPopupForm;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.dto.JoursDTO;
import org.crlr.web.dto.MoisDTO;

/**
 * Form de l'ecran de visualisation des devoirs.
 * @author G-SAFIR-FRMP
 *
 */
public class DevoirForm extends AbstractPopupForm {

    /**  Serial. */
    private static final long serialVersionUID = -8492253951449643311L;

    /** Indique si la recherche a ete lancee ou non. Utilise pour savoir s'il faut relancer la recherche
     * ou bien attendre que l'utilisateur clique sur recherche.*/
    private Boolean vraiOuFauxRechercheActive;
    
    /** Liste des jours qui sont ouvres pour l etablissement. */
    private Map<TypeJour,Boolean> jourOuvreAccessible;
    
    /** La barre de semaine pour l'alternance des semaines. */ 
    private List<BarreSemaineDTO> listeBarreSemaine;

    /** Liste des mois. */ 
    private List<MoisDTO> listeMois;

    /** Liste des types de devoir configurés sur l'etablissement. */
    private List<TypeDevoirDTO> listeTypeDevoir; 
    
    /** Indique le decalage de debut d'annee pour afficher le premier mois. */
    private Float decalageDebutAnnee;
    
    /** Numero de la semaine selectionnee. */
    private BarreSemaineDTO semaineSelectionne;
    
    /** Liste resultat des jours contenant des devoirs. */
    private List<JoursDTO> liste;
    
    /** Liste des devoirs non map .*/
    private List<DetailJourDTO> listeDevoir;
    
    /** Jour / Devoir selectionnee. */
    private DetailJourDTO selectDevoir;
    
    /** Devoir correspondant au DetailJourDTO selectionnee. */
    private DevoirDTO devoirSelected;
    
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
    
    
    
    /** affichage de la selection de la Classe ou Groupe. */
    private Boolean affichageClasseGroupe;

    /**
     * Initialisation des valeurs par defaut.
     */
    public void reset() {
        listeBarreSemaine = new ArrayList<BarreSemaineDTO>();
        semaineSelectionne = null;
        categorieSelectionne = TypeCategorieTypeDevoir.NORMAL.getId();
        listeDevoir = new ArrayList<DetailJourDTO>();
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
        if (semaineSelectionne == null) {
            return "";
        }
        final String titreNavigateurSemaine = "Semaine " + semaineSelectionne.getNumeroSemaine() + " - du " +
            DateUtils.format(semaineSelectionne.getLundi(), "d") +
            " au " + DateUtils.format(semaineSelectionne.getDimanche(), "d MMMM yyyy");
        return titreNavigateurSemaine;
    }

    /**
     * Accesseur de selectDevoir {@link #selectDevoir}.
     * @return retourne selectDevoir
     */
    public DetailJourDTO getSelectDevoir() {
        return selectDevoir;
    }

    /**
     * Mutateur de selectDevoir {@link #selectDevoir}.
     * @param selectDevoir le selectDevoir to set
     */
    public void setSelectDevoir(DetailJourDTO selectDevoir) {
        this.selectDevoir = selectDevoir;
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
     * Accesseur de listeDevoir {@link #listeDevoir}.
     * @return retourne listeDevoir
     */
    public List<DetailJourDTO> getListeDevoir() {
        return listeDevoir;
    }

    /**
     * Mutateur de listeDevoir {@link #listeDevoir}.
     * @param listeDevoir le listeDevoir to set
     */
    public void setListeDevoir(List<DetailJourDTO> listeDevoir) {
        this.listeDevoir = listeDevoir;
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
     * Accesseur de affichageClasseGroupe {@link #affichageClasseGroupe}.
     * @return retourne affichageClasseGroupe
     */
    public Boolean getAffichageClasseGroupe() {
        return affichageClasseGroupe;
    }

    /**
     * Mutateur de affichageClasseGroupe {@link #affichageClasseGroupe}.
     * @param affichageClasseGroupe le affichageClasseGroupe to set
     */
    public void setAffichageClasseGroupe(Boolean affichageClasseGroupe) {
        this.affichageClasseGroupe = affichageClasseGroupe;
    }

    /**
     * Accesseur de jourOuvreAccessible {@link #jourOuvreAccessible}.
     * @return retourne jourOuvreAccessible
     */
    public Map<TypeJour, Boolean> getJourOuvreAccessible() {
        return jourOuvreAccessible;
    }

    /**
     * Mutateur de jourOuvreAccessible {@link #jourOuvreAccessible}.
     * @param jourOuvreAccessible le jourOuvreAccessible to set
     */
    public void setJourOuvreAccessible(Map<TypeJour, Boolean> jourOuvreAccessible) {
        this.jourOuvreAccessible = jourOuvreAccessible;
    }
    
    /**
     * Indique si le jour est visible ou non. 
     * @param jour le type de jour a tester
     * @return true / false selon que le jour est visible ou non (ouvert ou ferme avec une seance dessus.
     */
    private Boolean getJourVisible(final TypeJour jour) {
        if (jourOuvreAccessible.get(jour)) {
            return true;
        } else {
            for (final DetailJourDTO detail : listeDevoir) {
                final TypeJour jourDevoir = TypeJour.getTypeJourFromDate(detail.getDate());
                if (jourDevoir != null && jourDevoir.equals(jour)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Indique si le lundi est visible ou non. 
     * @return true / false selon que le jour est visible ou non (ouvert ou ferme avec une seance dessus.
     */
    public Boolean getLundiVisible() {
        return getJourVisible(TypeJour.LUNDI);
    }

    /**
     * Indique si le lundi est visible ou non. 
     * @return true / false selon que le jour est visible ou non (ouvert ou ferme avec une seance dessus.
     */
    public Boolean getMardiVisible() {
        return getJourVisible(TypeJour.MARDI);
    }

    /**
     * Indique si le lundi est visible ou non. 
     * @return true / false selon que le jour est visible ou non (ouvert ou ferme avec une seance dessus.
     */
    public Boolean getMercrediVisible() {
        return getJourVisible(TypeJour.MERCREDI);
    }

    /**
     * Indique si le lundi est visible ou non. 
     * @return true / false selon que le jour est visible ou non (ouvert ou ferme avec une seance dessus.
     */
    public Boolean getJeudiVisible() {
        return getJourVisible(TypeJour.JEUDI);
    }

    /**
     * Indique si le lundi est visible ou non. 
     * @return true / false selon que le jour est visible ou non (ouvert ou ferme avec une seance dessus.
     */
    public Boolean getVendrediVisible() {
        return getJourVisible(TypeJour.VENDREDI);
    }

    /**
     * Indique si le lundi est visible ou non. 
     * @return true / false selon que le jour est visible ou non (ouvert ou ferme avec une seance dessus.
     */
    public Boolean getSamediVisible() {
        return getJourVisible(TypeJour.SAMEDI);
    }

    /**
     * Indique si le lundi est visible ou non. 
     * @return true / false selon que le jour est visible ou non (ouvert ou ferme avec une seance dessus.
     */
    public Boolean getDimancheVisible() {
        return getJourVisible(TypeJour.DIMANCHE);
    }
        
    
}
