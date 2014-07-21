/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AdminForm.java,v 1.8 2010/05/10 11:32:25 jerome.carriere Exp $
 */

package org.crlr.web.application.form.seance;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.AgendaSeanceDTO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.dto.MoisDTO;
import org.crlr.web.dto.TypeCouleur;

import com.google.gson.Gson;

/**
 * AdminForm.
 *
 * @author $author$
 * @version $Revision: 1.8 $
  */
public class SaisirSeanceForm extends AbstractForm {

    /**  Serial Key.*/
    private static final long serialVersionUID = 1238336429879110955L;
    
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");

    /** Identifiant de l'enseignant sur lequel on travaille. */
    private Integer idEnseignant;
    
    /** La barre de semaine pour l'alternance des semaines. */ 
    private List<BarreSemaineDTO> listeBarreSemaine;

    /** Liste des mois. */ 
    private List<MoisDTO> listeMois;
    
    /** Indique le decalage de debut d'annee pour afficher le premier mois. */
    private Float decalageDebutAnnee;
    
    /** Numero de la semaine selectionnee. */
    private BarreSemaineDTO semaineSelectionne;

    /** Liste des seances. */
    private List<SeanceDTO> listeSeance;
    
    /** Liste de l'emploi du temps (defini dans emploi du temps). */
    private List<DetailJourEmploiDTO> listeEmploiDuTemps;
    
    /** Liste des case affichees dans l'agenda : melange de seance et edt). */
    private List<AgendaSeanceDTO> listeAgenda;
    
    /** Chaine utilisee pour stocker les data utilisee par le composant agenda.*/
    private String agendaJSON;

    /** Cahine représentant la grille horaire de l'etablissement. */
    private String horairesJSON; 
    
    private EtablissementDTO etablissement;
    private String etablissementJSON;

    /** Style appliquer sur le panel contenant l'agenda. */
    private String stylePanelAgenda;
    
    /** Style appliquer sur le panel contenant la saisie d'une seance. */
    private String stylePanelEdition;
    
    /** Indice de la case selectionne dans la liste listeAgenda. */
    private Integer plageSelectedIndex;
    
    /** Plage qui est en cours d'edition. */
    private AgendaSeanceDTO plageSelected;
    
    /** Plage preparee pour l'edition d'une nouvelle seance avec selection de la sequence. 
     * Ce champ sert à gerer le cas d'annulation lors du choix de la sequence. */
    private AgendaSeanceDTO plagePrepare;
    
 
    
    /** Le devoir qui est en cours d'edition. */
    private DevoirDTO devoirSelected;
    
    
    
   
    /** Variable volatile qui va stocker l'id de la zone a rafraichir suite à un upload de piece jointe.*/
    private String raffraichirTabAfterUpload;
    
  
   
  
    /** Index de la plage selectionnee alors que des modif sont en cours.*/
    private Integer indexPlageCible;
    
    /** Date au format Long prévue pour une seance sur plage vide. */
    private Long dateLongVide;
    
    /** Heure de debut prévue pour une seance sur plage vide. */
    private Integer heureDebutVide;
    
    /** Minute de debut prévue pour une seance sur plage vide. */
    private Integer minuteDebutVide;
    
    
    /** Minute de fin prévue pour une seance sur plage vide. */
    private Integer heureFinVide;
    
    /** Minute de fin prévue pour une seance sur plage vide. */
    private Integer minuteFinVide;
    
    /** Affiche le bouton de retour ou pas. */
    private Boolean afficheRetour;
    /** Liste des couleur possibles dans le popup d'emploi de temps. */
    private List<TypeCouleur> listeCouleur = new ArrayList<TypeCouleur>();
 
	/** Type couleur sélectionné. */
    private TypeCouleur typeCouleur;    
    
    /**
     * la doc 
     */
    private String texteAide;
    /** Reinitialise les donnees du formulaire. */
    public void reset() {
        listeBarreSemaine = new ArrayList<BarreSemaineDTO>();
        semaineSelectionne = null;
        listeSeance = new ArrayList<SeanceDTO>();
        stylePanelAgenda = "width: 30%;";
        stylePanelEdition = "width: 70%;";
        afficheRetour = false;
        listeCouleur = GenerateurDTO.generateBarreCouleur();
    }
    
    /**
     * Indique si une saisie de seance est en cours.
     * @return retourne true / false
     */
    public Boolean getVraiFauxSaisieEnCours() {
        return plageSelected != null;
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
     * Accesseur de listeSeance {@link #listeSeance}.
     * @return retourne listeSeance
     */
    public List<SeanceDTO> getListeSeance() {
        return listeSeance;
    }

    /**
     * Mutateur de listeSeance {@link #listeSeance}.
     * @param listeSeance le listeSeance to set
     */
    public void setListeSeance(List<SeanceDTO> listeSeance) {
        this.listeSeance = listeSeance;
    }

    /**
     * Accesseur de listeEmploiDuTemps {@link #listeEmploiDuTemps}.
     * @return retourne listeEmploiDuTemps
     */
    public List<DetailJourEmploiDTO> getListeEmploiDuTemps() {
        return listeEmploiDuTemps;
    }

    /** 
     * Retourne la date correspondant au lundi.
     * @return le lundi
     */
    public long getDateLundiOrg() {
        if (semaineSelectionne != null) {
            return semaineSelectionne.getLundi().getTime();
        }
        return 0; 
    }
    /** 
         * Retourne la date correspondant au lundi sous forme de texte ex 1/1/2001.
         * @return le lundi
         */
        public String getDateLundi() {
            if (semaineSelectionne != null) {
                return dateFormat.format(semaineSelectionne.getLundi());
            }
            return ""; 
        }
    /**
     * Accesseur de grilleJSON {@link #grilleJSON}.
     * @return retourne grilleJSON
     */
    public String getAgendaJSON() {
        return agendaJSON;
    }

    /**
     * Mutateur de agendaJSON {@link #agendaJSON}.
     * @param agendaJSON le agendaJSON to set
     */
    public void setAgendaJSON(String agendaJSON) {
        this.agendaJSON = agendaJSON;
    }

    /**
     * Accesseur de horairesJSON {@link #horairesJSON}.
     * @return retourne horairesJSON
     */
    public String getHorairesJSON() {
        return horairesJSON;
    }

    /**
     * Mutateur de horairesJSON {@link #horairesJSON}.
     * @param horairesJSON le horairesJSON to set
     */
    public void setHorairesJSON(String horairesJSON) {
        this.horairesJSON = horairesJSON;
    }

    /**
     * Mutateur de listeEmploiDuTemps {@link #listeEmploiDuTemps}.
     * @param listeEmploiDuTemps le listeEmploiDuTemps to set
     */
    public void setListeEmploiDuTemps(List<DetailJourEmploiDTO> listeEmploiDuTemps) {
        this.listeEmploiDuTemps = listeEmploiDuTemps;
    }
    
    /**
     * Accesseur de listeAgenda {@link #listeAgenda}.
     * @return retourne listeAgenda
     */
    public List<AgendaSeanceDTO> getListeAgenda() {
        return listeAgenda;
    }

    /**
     * Mutateur de listeAgenda {@link #listeAgenda}.
     * @param listeAgenda le listeAgenda to set
     */
    public void setListeAgenda(List<AgendaSeanceDTO> listeAgenda) {
        this.listeAgenda = listeAgenda;
    }

    /**
     * Verifie si la seance match exactement (jour, heure debut, heure fin identiques). 
     * @param seance la seance
     * @param detail la case de l'EDT
     * @return true / false selon que les deux element correspondent exactement
     */
    private Boolean seanceMatchDetailJour(final SeanceDTO seance, final DetailJourEmploiDTO detail) {
        final TypeJour jour = seance.getTypeJour();
        if (!jour.equals(detail.getJour())) {
            return false;
        }
        if (!detail.getHeureDebut().equals(seance.getHeureDebut()) ||
            !detail.getHeureFin().equals(seance.getHeureFin()) ||
            !detail.getMinuteDebut().equals(seance.getMinuteDebut()) ||
            !detail.getMinuteFin().equals(seance.getMinuteFin())) {
            return false;
        }
        return true;
    }
    
    /**
     * Chererche dans la liste des seances si une matche exactement avec le detail de jour de l'EDT.
     * @param detail : la case EDT recherchee
     * @return la seance correspondante exactement ou null sinon
     */
    private AgendaSeanceDTO findSeance(final DetailJourEmploiDTO detail) {
        for (final AgendaSeanceDTO plage : listeAgenda) {
            final SeanceDTO seance = plage.getSeance();
            if (seance != null) {
                if (seanceMatchDetailJour(seance, detail)) {
                    return plage;
                }
            }
        }
        return null;
    }
    
   
    
    /**
     * Met a jour la liste agenda utilisee pour representer l'agendat de la semaine en cours.
     *
     * @param idSeanceSelected  La seance en cours d'edition
     */
    public void majListeAgenda(Integer idSeanceSelected) {
        listeAgenda = new ArrayList<AgendaSeanceDTO>();

        //
        
        // Ajoute toutes les seances
        for (final SeanceDTO seance : listeSeance) {
            final AgendaSeanceDTO agenda = new AgendaSeanceDTO(seance);
            listeAgenda.add(agenda);
            
            // Met a jour la seance selectionne 
            if (idSeanceSelected!=null && idSeanceSelected.equals(seance.getId())) {
                agenda.setSelected(true);
            }
        }
        
        
        // Ajoute les case EDT qui ne matchent pas exactement avec un seance
        for (final DetailJourEmploiDTO detail : listeEmploiDuTemps) {
            final AgendaSeanceDTO plage = findSeance(detail);  
            // Aucune seance exacte
            if (plage == null) {
                final AgendaSeanceDTO agenda = new AgendaSeanceDTO(this.semaineSelectionne.getLundi(), detail);
                listeAgenda.add(agenda);

            // Cette case EDT match parfaitement avec une seance
            } else {
                plage.completeWithDetail(detail);
            }
              
        }
    }

    /**
     * Recalcul la chaine JSON contenant la grille (edt + seance).
     * Cette methode est appellée dès qu'une liste (seance ou edt) change 
     * ou qu'un de ses elements est modifié.
     * Contruit une copie de la listeAgenda en mode "reduit" pour eviter le flux qui est envoyé vers le JS
     */
    public void recalculerDataAgendaJSON() {
        
        //TODO Utilise Gson mechanisme pour réduire le flux
        
        final Gson gson = new Gson();
        final String json = gson.toJson(listeAgenda);
        this.agendaJSON = json;
    }
    
    /**
     * Accesseur de stylePanelAgenda {@link #stylePanelAgenda}.
     * @return retourne stylePanelAgenda
     */
    public String getStylePanelAgenda() {
        return stylePanelAgenda;
    }

    /**
     * Mutateur de stylePanelAgenda {@link #stylePanelAgenda}.
     * @param stylePanelAgenda le stylePanelAgenda to set
     */
    public void setStylePanelAgenda(String stylePanelAgenda) {
        this.stylePanelAgenda = stylePanelAgenda;
    }

    /**
     * Accesseur de stylePanelEdtion {@link #stylePanelEdition}.
     * @return retourne stylePanelEdtion
     */
    public String getStylePanelEdition() {
        return stylePanelEdition;
    }

    /**
     * Mutateur de stylePanelEdition {@link #stylePanelEdition}.
     * @param stylePanelEdition le stylePanelEdtion to set
     */
    public void setStylePanelEdition(String stylePanelEdition) {
        this.stylePanelEdition = stylePanelEdition;
    }

    /**
     * Accesseur de plageSelectedIndex {@link #plageSelectedIndex}.
     * @return retourne plageSelectedIndex
     */
    public Integer getPlageSelectedIndex() {
        return plageSelectedIndex;
    }

    /**
     * Mutateur de plageSelectedIndex {@link #plageSelectedIndex}.
     * @param plageSelectedIndex le plageSelectedIndex to set
     */
    public void setPlageSelectedIndex(Integer plageSelectedIndex) {
        this.plageSelectedIndex = plageSelectedIndex;
    }

  

    /**
     * Accesseur de plageSelected {@link #plageSelected}.
     * @return retourne plageSelected
     */
    public AgendaSeanceDTO getPlageSelected() {
        return plageSelected;
    }

 
    
    /**
     * Mutateur de plageSelected {@link #plageSelected}.
     * @param plageSelected le plageSelected to set
     */
    public void setPlageSelected(AgendaSeanceDTO plageSelected) {
        // Reset la selection de toutes les plages.
        for (final AgendaSeanceDTO plage : this.listeAgenda) {
            plage.setSelected(false);
        }
        if (plageSelected != null) {
            plageSelected.setSelected(true);
        }
        this.plageSelected = plageSelected;
        
        // maj les infos JSON utilise par l'objet JS  
        recalculerDataAgendaJSON();
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
     * Accesseur de indexPlageCible {@link #indexPlageCible}.
     * @return retourne indexPlageCible
     */
    public Integer getIndexPlageCible() {
        return indexPlageCible;
    }

    /**
     * Mutateur de indexPlageCible {@link #indexPlageCible}.
     * @param indexPlageCible le indexPlageCible to set
     */
    public void setIndexPlageCible(Integer indexPlageCible) {
        this.indexPlageCible = indexPlageCible;
    }

    /**
     * Accesseur de dateLongVide {@link #dateLongVide}.
     * @return retourne dateLongVide
     */
    public Long getDateLongVide() {
        return dateLongVide;
    }

    /**
     * Mutateur de dateLongVide {@link #dateLongVide}.
     * @param dateLongVide le dateLongVide to set
     */
    public void setDateLongVide(Long dateLongVide) {
        this.dateLongVide = dateLongVide;
    }

    /**
     * Accesseur de heureDebutVide {@link #heureDebutVide}.
     * @return retourne heureDebutVide
     */
    public Integer getHeureDebutVide() {
        return heureDebutVide;
    }

    /**
     * Mutateur de heureDebutVide {@link #heureDebutVide}.
     * @param heureDebutVide le heureDebutVide to set
     */
    public void setHeureDebutVide(Integer heureDebutVide) {
        this.heureDebutVide = heureDebutVide;
    }

    /**
     * Accesseur de minuteDebutVide {@link #minuteDebutVide}.
     * @return retourne minuteDebutVide
     */
    public Integer getMinuteDebutVide() {
        return minuteDebutVide;
    }

    /**
     * Mutateur de minuteDebutVide {@link #minuteDebutVide}.
     * @param minuteDebutVide le minuteDebutVide to set
     */
    public void setMinuteDebutVide(Integer minuteDebutVide) {
        this.minuteDebutVide = minuteDebutVide;
    }

    /**
     * Accesseur de heureFinVide {@link #heureFinVide}.
     * @return retourne heureFinVide
     */
    public Integer getHeureFinVide() {
        return heureFinVide;
    }

    /**
     * Mutateur de heureFinVide {@link #heureFinVide}.
     * @param heureFinVide le heureFinVide to set
     */
    public void setHeureFinVide(Integer heureFinVide) {
        this.heureFinVide = heureFinVide;
    }

    /**
     * Accesseur de minuteFinVide {@link #minuteFinVide}.
     * @return retourne minuteFinVide
     */
    public Integer getMinuteFinVide() {
        return minuteFinVide;
    }

    /**
     * Mutateur de minuteFinVide {@link #minuteFinVide}.
     * @param minuteFinVide le minuteFinVide to set
     */
    public void setMinuteFinVide(Integer minuteFinVide) {
        this.minuteFinVide = minuteFinVide;
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
     * Accesseur de plagePrepare {@link #plagePrepare}.
     * @return retourne plagePrepare
     */
    public AgendaSeanceDTO getPlagePrepare() {
        return plagePrepare;
    }

    /**
     * Mutateur de plagePrepare {@link #plagePrepare}.
     * @param plagePrepare le plagePrepare to set
     */
    public void setPlagePrepare(AgendaSeanceDTO plagePrepare) {
        this.plagePrepare = plagePrepare;
    }



    /**
     * @return the etablissement
     */
    public EtablissementDTO getEtablissement() {
        return etablissement;
    }

    /**
     * @param etablissement the etablissement to set
     */
    public void setEtablissement(EtablissementDTO etablissement) {
        this.etablissement = etablissement;
        
        Gson gson = new Gson();
        String json = gson.toJson(etablissement);
        setEtablissementJSON(json);
    }

    /**
     * @return the etablissementJSON
     */
    public String getEtablissementJSON() {
        return etablissementJSON;
    }

    /**
     * @param etablissementJSON the etablissementJSON to set
     */
    public void setEtablissementJSON(String etablissementJSON) {
        this.etablissementJSON = etablissementJSON;
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

	public List<TypeCouleur> getListeCouleur() {
		return listeCouleur;
	}

	public void setListeCouleur(List<TypeCouleur> listeCouleur) {
		this.listeCouleur = listeCouleur;
	}

	public TypeCouleur getTypeCouleur() {
		return typeCouleur;
	}

	public void setTypeCouleur(TypeCouleur typeCouleur) {
		this.typeCouleur = typeCouleur;
	}

	public String getTexteAide() {
		return texteAide;
	}

	public void setTexteAide(String texteAide) {
		this.texteAide = texteAide;
	}
    
    
    
}
