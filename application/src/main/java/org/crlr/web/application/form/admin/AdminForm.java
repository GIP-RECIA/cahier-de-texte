/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AdminForm.java,v 1.8 2010/05/10 11:32:25 jerome.carriere Exp $
 */

package org.crlr.web.application.form.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.DateAnneeScolaireQO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GenericDTO;
import org.crlr.dto.application.base.GenericTroisDTO;
import org.crlr.dto.application.base.OuvertureQO;
import org.crlr.dto.application.base.PeriodeVacanceQO;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.dto.CalendrierVancanceDTO;
import org.crlr.web.dto.GrilleHoraireDTO;
import org.crlr.web.dto.MoisDTO;
import org.crlr.web.dto.TypeCouleurJour;
import org.crlr.web.dto.TypeOngletAdmin;

import com.google.gson.Gson;

/**
 * AdminForm.
 *
 * @author $author$
 * @version $Revision: 1.8 $
  */
public class AdminForm extends AbstractForm {
    
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = -9119316429383300695L;
    
    //gestion des onglets
    /** id de l'onglet sélectionné. */
    private String ongletSelectionne;   
    
    /** Nom de la methode a invoquer pour la sauvegarde auto lors du changement d'onglet / page.*/
    private String nomMethodeSauvegarde;
    
    /** les onglets accessibles en fonction du profil. */
    private Map<String, Boolean> mapAccesOnglet;
    
    //Onglet Année scolaire
    /** l'année scolaire à persister. */
    private DateAnneeScolaireQO dateAnneeScolaireQO;
    
    /** Période de jours vaqués. */
    private PeriodeVacanceQO periodeVacanceQO;
    
    /** Barre des mois de l'année scolaire. */
    private List<GenericTroisDTO<String, String, Boolean>> listeMoisAnneeSco;
    
    /** le mois sélectionné dans la barre des mois. */
    private String dateMoisSelectionne;
    
    /** le calendrier des jours ouvrés et vaqués. */
    private List<CalendrierVancanceDTO> listeVancance;
    
    /** La date de début du jour vaqué. */
    private Date dateDebutFerie;
    
    /** La date de fin du jour vaqué. */
    private Date dateFinFerie;
    
    /** La liste des plages des jours vaqués. */
    private List<GenericDTO<Date, Date>>  listeJourFerie;
    
    /** La plage vaqué sélectionnée. */
    private GenericDTO<Date, Date> plageJourFerieSelectionne;
   
    /** Fractionnement sélectionnée .*/
    private Integer fractionnement; 
     
    private static final Set<String> LISTE_JOUR_OUVRE_GLOBALE;
    static {
        LISTE_JOUR_OUVRE_GLOBALE = new HashSet<String>();
        LISTE_JOUR_OUVRE_GLOBALE.add(TypeJour.LUNDI.name());
        LISTE_JOUR_OUVRE_GLOBALE.add(TypeJour.MARDI.name());
        LISTE_JOUR_OUVRE_GLOBALE.add(TypeJour.MERCREDI.name());
        LISTE_JOUR_OUVRE_GLOBALE.add(TypeJour.JEUDI.name());
        LISTE_JOUR_OUVRE_GLOBALE.add(TypeJour.VENDREDI.name());
    }
    
    /** Ouverture ENT. */
    private OuvertureQO ouvertureQO;
    
    /** Copie du boolean vraiOuFauxOuvert ENT. */
    private Boolean vraiOuFauxOuvertEntSource;
    
    //onglet grille horaire
    
    /** l'heure de début des cours. */
    private Integer heureDebut;    
    
    /** les mitutes de début des cours. */
    private Integer minuteDebut;
    
    /** l'heure de fin des cours. */
    private Integer heureFin; 

    /** La durée classqiue d'un cours. */
    private Integer dureeCours;
    
    /** La liste des duree de cours. */
    private List<SelectItem> listeDureeCours = new ArrayList<SelectItem>();
    
    /** Liste des fractionnement possible. */
    private List<SelectItem> listeFractionnement;
    
    /** Liste des plages horaires. */
    private List<GrilleHoraireDTO> horairesCours;

    /** Chaines JSON. */
    private String horairesCoursJSON;
    
    /** Index de la plage selectionnee. */
    private Integer plageSelectedIndex;
    

    /** Plage sélectionnée (pour la modification / suppression). */
    private GrilleHoraireDTO plageEditee;
    
    //onglet jours ouvres et alternance
    /** la barre de jours ouvrés. */
    private List<GenericTroisDTO<String, String, Boolean>> listeOuvre;
    
    /** le libellé du jour ouvré sélectionné. */
    private String libelleJourOuvreSel;
    
    /** La barre de semaine pour l'alternance des semaines. */ 
    private List<BarreSemaineDTO> listeBarreSemaine;
    
    private List<MoisDTO> listeMois;
    private Float decalageDebutAnnee;
    
    private String numeroSemaineSelectionne;
    
    private Boolean repercutionModificationAlternance;
    
    private Boolean vraiOuFauxAlternanceSemaine;
    
    //onglet libellé
    private List<EnseignementDTO> listeEnseignement;
    private EnseignementDTO enseignementSel;
    private String libelleEnseignement;
    
    private List<TypeDevoirDTO> listeTypeDevoir;
    private TypeDevoirDTO typeDevoirSel;
    private String libelleTypeDevoir;
    private Boolean vraiOuFauxDevoir;
    private List<SelectItem> listeCategorieTypeDevoir;
    
    //onglet Ouverture
    private String libelleEtab;
    
    private Boolean vraiOuFauxOuvertEtab;
    
    /** Valeur initiale de vraiOuFauxOuvertEtab. */
    private Boolean vraiOuFauxOuvertEtabSource;
    
    /** Le filtre sur le libelle long de l'enseignement. */
    private String filtreEnseignementLong;
    
    /** Le filtre sur le libelle cout de l'enseignement. */
    private String filtreEnseignementCourt;
    
    /**
     * Constructeur.
     */
    public AdminForm() {
        reset();
    }

    /**
     * Méthode de réinitialisation des données du formulaire saisie par
     * l'utilisateur.
     */
    public void reset() {
        
        
        // Charge la liste des mois
        
        
        //Gestion des onglets
        this.ongletSelectionne = null;
        this.mapAccesOnglet = new HashMap<String, Boolean>();
        for (TypeOngletAdmin typeOngletAdmin : TypeOngletAdmin.values()) {
            mapAccesOnglet.put(typeOngletAdmin.name(), false);
        }
        
        
        resetOnglet();
    }
    
    /**
     * 
     */
    public void resetOnglet() {
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        
        //ONGLET année scolaire
        final AnneeScolaireDTO anneeScolaireDTO = ObjectUtils.clone(utilisateurDTO.getAnneeScolaireDTO());
        this.dateAnneeScolaireQO = new DateAnneeScolaireQO();
        this.dateAnneeScolaireQO.setId(anneeScolaireDTO.getId());
        this.dateAnneeScolaireQO.setDateRentree(anneeScolaireDTO.getDateRentree());
        this.dateAnneeScolaireQO.setDateSortie(anneeScolaireDTO.getDateSortie());                       
        this.periodeVacanceQO = new PeriodeVacanceQO(anneeScolaireDTO.getId(), null);
         
        //calendrier de présentation des jours ouvrés, chomés.
        this.listeVancance = new ArrayList<CalendrierVancanceDTO>();
        //barre de navigation mensuelle
        this.listeMoisAnneeSco = new ArrayList<GenericTroisDTO<String,String,Boolean>>();
        //date du mois selectionne dans la barre de mois
        final Date dateEntree = anneeScolaireDTO.getDateRentree();
        this.dateMoisSelectionne = DateUtils.formatDateAuPremierJourDuMois(dateEntree);      
        //les champs d'ajout d'une plage chomée.
        this.dateDebutFerie = null;
        this.dateFinFerie = null;
        //la liste qui présente le tableau des périodes fériés
        this.listeJourFerie = GenerateurDTO.generateListePeriodeVancanceFromDb(anneeScolaireDTO.getPeriodeVacances());
        this.plageJourFerieSelectionne = new GenericDTO<Date, Date>();
        final Boolean vraiOuFauxEntOuvert = anneeScolaireDTO.getVraiOuFauxCahierOuvertENT();
        this.ouvertureQO = new OuvertureQO(anneeScolaireDTO.getId(), vraiOuFauxEntOuvert);
        this.vraiOuFauxOuvertEntSource = vraiOuFauxEntOuvert;
       
        this.setListeMois( 
                GenerateurDTO.genererListeMois(anneeScolaireDTO.getDateRentree(),
                        anneeScolaireDTO.getDateSortie()));        
        this.setDecalageDebutAnnee(MoisDTO.getDecalageDebutAnnee(anneeScolaireDTO.getDateRentree()));
        
        //ONGLET grille horaire
        this.heureDebut = 8;
        this.heureFin = 17;
        this.minuteDebut = 0;      
        this.dureeCours = 60;
        
        this.listeFractionnement = new ArrayList<SelectItem>();
        this.listeFractionnement.add(new SelectItem(1, "Pas de fractionnement"));
        this.listeFractionnement.add(new SelectItem(2, "Demi cours"));
        this.listeFractionnement.add(new SelectItem(3, "Quart de cours"));
        
        this.listeDureeCours = new ArrayList<SelectItem>();
        
        for (int m=1;m<=60;m+=1) {
            this.listeDureeCours.add(new SelectItem(m, StringUtils.leftPad(String.valueOf(m), 2, '0')));
        }  
        
        //onglet jours ouvres alternance
        this.listeOuvre = new ArrayList<GenericTroisDTO<String,String, Boolean>>();
        this.libelleJourOuvreSel = null;
        //alternance
        this.listeBarreSemaine = new ArrayList<BarreSemaineDTO>();
        this.numeroSemaineSelectionne = null;
        this.repercutionModificationAlternance = false;
        
        //onglet Libellé
        this.listeEnseignement = new ArrayList<EnseignementDTO>();
        this.enseignementSel = new EnseignementDTO();
        this.libelleEnseignement = null;
        
        this.listeTypeDevoir = new ArrayList<TypeDevoirDTO>();
        this.typeDevoirSel = new TypeDevoirDTO();
        this.libelleTypeDevoir = null;
        
        //onglet Ouverture
        this.libelleEtab = utilisateurDTO.getDesignationEtablissement();        
        this.vraiOuFauxOuvertEtab = utilisateurDTO.getVraiOuFauxCahierOuvertEtab();
        this.vraiOuFauxOuvertEtabSource = this.vraiOuFauxOuvertEtab;        
    }
    
    /**
     * Accesseur.
     * @return la liste.
     */
    public Set<String> getListeJourOuvreGlobale() {
        return LISTE_JOUR_OUVRE_GLOBALE;
    }

    /**
     * Accesseur listeOuvre.
     * @return le listeOuvre
     */
    public List<GenericTroisDTO<String, String, Boolean>> getListeOuvre() {
        return listeOuvre;
    }

    /**
     * Mutateur de listeOuvre.
     * @param listeOuvre le listeOuvre à modifier.
     */
    public void setListeOuvre(List<GenericTroisDTO<String, String, Boolean>> listeOuvre) {
        this.listeOuvre = listeOuvre;
    }

    /**
     * Accesseur libelleJourOuvreSel.
     * @return le libelleJourOuvreSel
     */
    public String getLibelleJourOuvreSel() {
        return libelleJourOuvreSel;
    }

    /**
     * Mutateur de libelleJourOuvreSel.
     * @param libelleJourOuvreSel le libelleJourOuvreSel à modifier.
     */
    public void setLibelleJourOuvreSel(String libelleJourOuvreSel) {
        this.libelleJourOuvreSel = libelleJourOuvreSel;
    }
    
    /**
     * Accesseur heureDebut.
     * @return le heureDebut
     */
    public Integer getHeureDebut() {
        return heureDebut;
    }

    /**
     * Mutateur de heureDebut.
     * @param heureDebut le heureDebut à modifier.
     */
    public void setHeureDebut(Integer heureDebut) {
        this.heureDebut = heureDebut;
    }

    /**
     * Accesseur minuteDebut.
     * @return le minuteDebut
     */
    public Integer getMinuteDebut() {
        return minuteDebut;
    }

    /**
     * Mutateur de minuteDebut.
     * @param minuteDebut le minuteDebut à modifier.
     */
    public void setMinuteDebut(Integer minuteDebut) {
        this.minuteDebut = minuteDebut;
    }

    /**
     * Accesseur dureeCours.
     * @return le dureeCours
     */
    public Integer getDureeCours() {
        return dureeCours;
    }

    /**
     * Mutateur de dureeCours.
     * @param dureeCours le dureeCours à modifier.
     */
    public void setDureeCours(Integer dureeCours) {
        this.dureeCours = dureeCours;
    }
    
    /**
     * Accesseur heureFin.
     * @return le heureFin
     */
    public Integer getHeureFin() {
        return heureFin;
    }

    /**
     * Mutateur de heureFin.
     * @param heureFin le heureFin à modifier.
     */
    public void setHeureFin(Integer heureFin) {
        this.heureFin = heureFin;
    }
   
    /**
     * Accesseur listeVancance.
     * @return le listeVancance
     */
    public List<CalendrierVancanceDTO> getListeVancance() {
        return listeVancance;
    }

    /**
     * Mutateur de listeVancance.
     * @param listeVancance le listeVancance à modifier.
     */
    public void setListeVancance(List<CalendrierVancanceDTO> listeVancance) {
        this.listeVancance = listeVancance;
    }

    /**
     * Accesseur listeMoisAnneeSco.
     * @return le listeMoisAnneeSco
     */
    public List<GenericTroisDTO<String, String, Boolean>> getListeMoisAnneeSco() {
        return listeMoisAnneeSco;
    }

    /**
     * Mutateur de listeMoisAnneeSco.
     * @param listeMoisAnneeSco le listeMoisAnneeSco à modifier.
     */
    public void setListeMoisAnneeSco(
            List<GenericTroisDTO<String, String, Boolean>> listeMoisAnneeSco) {
        this.listeMoisAnneeSco = listeMoisAnneeSco;
    }

    /**
     * Accesseur dateMoisSelectionne.
     * @return le dateMoisSelectionne
     */
    public String getDateMoisSelectionne() {
        return dateMoisSelectionne;
    }

    /**
     * Mutateur de dateMoisSelectionne.
     * @param dateMoisSelectionne le dateMoisSelectionne à modifier.
     */
    public void setDateMoisSelectionne(String dateMoisSelectionne) {
        this.dateMoisSelectionne = dateMoisSelectionne;
    }

    /**
     * Accesseur dateDebutFerie.
     * @return le dateDebutFerie
     */
    public Date getDateDebutFerie() {
        return dateDebutFerie;
    }

    /**
     * Mutateur de dateDebutFerie.
     * @param dateDebutFerie le dateDebutFerie à modifier.
     */
    public void setDateDebutFerie(Date dateDebutFerie) {
        this.dateDebutFerie = dateDebutFerie;
    }

    /**
     * Accesseur dateFinFerie.
     * @return le dateFinFerie
     */
    public Date getDateFinFerie() {
        return dateFinFerie;
    }

    /**
     * Mutateur de dateFinFerie.
     * @param dateFinFerie le dateFinFerie à modifier.
     */
    public void setDateFinFerie(Date dateFinFerie) {
        this.dateFinFerie = dateFinFerie;
    }

    /**
     * Accesseur listeJourFerie.
     * @return le listeJourFerie
     */
    public List<GenericDTO<Date, Date>> getListeJourFerie() {
        return listeJourFerie;
    }

    /**
     * Mutateur de listeJourFerie.
     * @param listeJourFerie le listeJourFerie à modifier.
     */
    public void setListeJourFerie(List<GenericDTO<Date, Date>> listeJourFerie) {
        this.listeJourFerie = listeJourFerie;
    }
   
    /**
     * Accesseur dateAnneeScolaireQO.
     * @return le dateAnneeScolaireQO
     */
    public DateAnneeScolaireQO getDateAnneeScolaireQO() {
        return dateAnneeScolaireQO;
    }

    /**
     * Mutateur de dateAnneeScolaireQO.
     * @param dateAnneeScolaireQO le dateAnneeScolaireQO à modifier.
     */
    public void setDateAnneeScolaireQO(DateAnneeScolaireQO dateAnneeScolaireQO) {
        this.dateAnneeScolaireQO = dateAnneeScolaireQO;
    }

    /**
     * Accesseur periodeVacanceQO.
     * @return le periodeVacanceQO
     */
    public PeriodeVacanceQO getPeriodeVacanceQO() {
        return periodeVacanceQO;
    }

    /**
     * Mutateur de periodeVacanceQO.
     * @param periodeVacanceQO le periodeVacanceQO à modifier.
     */
    public void setPeriodeVacanceQO(PeriodeVacanceQO periodeVacanceQO) {
        this.periodeVacanceQO = periodeVacanceQO;
    }

    /**
     * Accesseur plageJourFerieSelectionne.
     * @return le plageJourFerieSelectionne
     */
    public GenericDTO<Date, Date> getPlageJourFerieSelectionne() {
        return plageJourFerieSelectionne;
    }

    /**
     * Mutateur de plageJourFerieSelectionne.
     * @param plageJourFerieSelectionne le plageJourFerieSelectionne à modifier.
     */
    public void setPlageJourFerieSelectionne(
            GenericDTO<Date, Date> plageJourFerieSelectionne) {
        this.plageJourFerieSelectionne = plageJourFerieSelectionne;
    }

    /**
     * Accesseur ongletSelectionne.
     * @return le ongletSelectionne
     */
    public String getOngletSelectionne() {
        return ongletSelectionne;
    }

    /**
     * Mutateur de ongletSelectionne.
     * @param ongletSelectionne le ongletSelectionne à modifier.
     */
    public void setOngletSelectionne(String ongletSelectionne) {
        this.ongletSelectionne = ongletSelectionne;
    }

    /**
     * Accesseur mapAccesOnglet.
     * @return le mapAccesOnglet
     */
    public Map<String, Boolean> getMapAccesOnglet() {
        return mapAccesOnglet;
    }

    /**
     * Mutateur de mapAccesOnglet.
     * @param mapAccesOnglet le mapAccesOnglet à modifier.
     */
    public void setMapAccesOnglet(Map<String, Boolean> mapAccesOnglet) {
        this.mapAccesOnglet = mapAccesOnglet;
    }

    /**
     * Accesseur listeBarreSemaine.
     * @return le listeBarreSemaine
     */
    public List<BarreSemaineDTO> getListeBarreSemaine() {
        return listeBarreSemaine;
    }

    /**
     * Mutateur de listeBarreSemaine.
     * @param listeBarreSemaine le listeBarreSemaine à modifier.
     */
    public void setListeBarreSemaine(List<BarreSemaineDTO> listeBarreSemaine) {
        this.listeBarreSemaine = listeBarreSemaine;
    }

    /**
     * Accesseur numeroSemaineSelectionne.
     * @return le numeroSemaineSelectionne
     */
    public String getNumeroSemaineSelectionne() {
        return numeroSemaineSelectionne;
    }

    /**
     * Mutateur de numeroSemaineSelectionne.
     * @param numeroSemaineSelectionne le numeroSemaineSelectionne à modifier.
     */
    public void setNumeroSemaineSelectionne(String numeroSemaineSelectionne) {
        this.numeroSemaineSelectionne = numeroSemaineSelectionne;
    }

    /**
     * Accesseur repercutionModificationAlternance.
     * @return le repercutionModificationAlternance
     */
    public Boolean getRepercutionModificationAlternance() {
        return repercutionModificationAlternance;
    }

    /**
     * Mutateur de repercutionModificationAlternance.
     * @param repercutionModificationAlternance le repercutionModificationAlternance à modifier.
     */
    public void setRepercutionModificationAlternance(
            Boolean repercutionModificationAlternance) {
        this.repercutionModificationAlternance = repercutionModificationAlternance;
    }

    /**
     * Accesseur libelleEtab.
     * @return le libelleEtab
     */
    public String getLibelleEtab() {
        return libelleEtab;
    }

    /**
     * Mutateur de libelleEtab.
     * @param libelleEtab le libelleEtab à modifier.
     */
    public void setLibelleEtab(String libelleEtab) {
        this.libelleEtab = libelleEtab;
    }

    /**
     * Accesseur vraiOuFauxOuvertEtab.
     * @return le vraiOuFauxOuvertEtab
     */
    public Boolean getVraiOuFauxOuvertEtab() {
        return vraiOuFauxOuvertEtab;
    }

    /**
     * Mutateur de vraiOuFauxOuvertEtab.
     * @param vraiOuFauxOuvertEtab le vraiOuFauxOuvertEtab à modifier.
     */
    public void setVraiOuFauxOuvertEtab(Boolean vraiOuFauxOuvertEtab) {
        this.vraiOuFauxOuvertEtab = vraiOuFauxOuvertEtab;
    }

    /**
     * Accesseur ouvertureQO.
     * @return le ouvertureQO
     */
    public OuvertureQO getOuvertureQO() {
        return ouvertureQO;
    }

    /**
     * Mutateur de ouvertureQO.
     * @param ouvertureQO le ouvertureQO à modifier.
     */
    public void setOuvertureQO(OuvertureQO ouvertureQO) {
        this.ouvertureQO = ouvertureQO;
    }

    /**
     * Accesseur vraiOuFauxAlternanceSemaine.
     * @return le vraiOuFauxAlternanceSemaine
     */
    public Boolean getVraiOuFauxAlternanceSemaine() {
        return vraiOuFauxAlternanceSemaine;
    }

    /**
     * Mutateur de vraiOuFauxAlternanceSemaine.
     * @param vraiOuFauxAlternanceSemaine le vraiOuFauxAlternanceSemaine à modifier.
     */
    public void setVraiOuFauxAlternanceSemaine(Boolean vraiOuFauxAlternanceSemaine) {
        this.vraiOuFauxAlternanceSemaine = vraiOuFauxAlternanceSemaine;
    }

    /**
     * Accesseur listeEnseignement.
     * @return le listeEnseignement
     */
    public List<EnseignementDTO> getListeEnseignement() {
        return listeEnseignement;
    }

    /**
     * Mutateur de listeEnseignement.
     * @param listeEnseignement le listeEnseignement à modifier.
     */
    public void setListeEnseignement(List<EnseignementDTO> listeEnseignement) {
        this.listeEnseignement = listeEnseignement;
    }

    /**
     * Accesseur enseignementSel.
     * @return le enseignementSel
     */
    public EnseignementDTO getEnseignementSel() {
        return enseignementSel;
    }

    /**
     * Mutateur de enseignementSel.
     * @param enseignementSel le enseignementSel à modifier.
     */
    public void setEnseignementSel(EnseignementDTO enseignementSel) {
        this.enseignementSel = enseignementSel;
    }

    /**
     * Accesseur libelleEnseignement.
     * @return le libelleEnseignement
     */
    public String getLibelleEnseignement() {
        return libelleEnseignement;
    }

    /**
     * Mutateur de libelleEnseignement.
     * @param libelleEnseignement le libelleEnseignement à modifier.
     */
    public void setLibelleEnseignement(String libelleEnseignement) {
        this.libelleEnseignement = libelleEnseignement;
    }

    /**
     * Accesseur listeTypeDevoir.
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
     * Accesseur typeDevoirSel.
     * @return le typeDevoirSel
     */
    public TypeDevoirDTO getTypeDevoirSel() {
        return typeDevoirSel;
    }

    /**
     * Mutateur de typeDevoirSel.
     * @param typeDevoirSel le typeDevoirSel à modifier.
     */
    public void setTypeDevoirSel(TypeDevoirDTO typeDevoirSel) {
        this.typeDevoirSel = typeDevoirSel;
    }

    /**
     * Accesseur libelleTypedevoir.
     * @return le libelleTypedevoir
     */
    public String getLibelleTypeDevoir() {
        return libelleTypeDevoir;
    }

    /**
     * Mutateur de libelleTypeDevoir.
     * @param libelleTypeDevoir le libelleTypeDvoir à modifier.
     */
    public void setLibelleTypeDevoir(String libelleTypeDevoir) {
        this.libelleTypeDevoir = libelleTypeDevoir;
    }

    /**
     * Accesseur vraiOuFauxOuvertEntSource.
     * @return le vraiOuFauxOuvertEntSource
     */
    public Boolean getVraiOuFauxOuvertEntSource() {
        return vraiOuFauxOuvertEntSource;
    }

    /**
     * Mutateur de vraiOuFauxOuvertEntSource.
     * @param vraiOuFauxOuvertEntSource le vraiOuFauxOuvertEntSource à modifier.
     */
    public void setVraiOuFauxOuvertEntSource(Boolean vraiOuFauxOuvertEntSource) {
        this.vraiOuFauxOuvertEntSource = vraiOuFauxOuvertEntSource;
    }

    /**
     * Accesseur vraiOuFauxOuvertEtabSource.
     * @return le vraiOuFauxOuvertEtabSource
     */
    public Boolean getVraiOuFauxOuvertEtabSource() {
        return vraiOuFauxOuvertEtabSource;
    }

    /**
     * Mutateur de vraiOuFauxOuvertEtabSource.
     * @param vraiOuFauxOuvertEtabSource le vraiOuFauxOuvertEtabSource à modifier.
     */
    public void setVraiOuFauxOuvertEtabSource(Boolean vraiOuFauxOuvertEtabSource) {
        this.vraiOuFauxOuvertEtabSource = vraiOuFauxOuvertEtabSource;
    }

    /**
     * Accesseur de filtreEnseignementLong.
     * @return le filtreEnseignementLong
     */
    public String getFiltreEnseignementLong() {
        return filtreEnseignementLong;
    }

    /**
     * Mutateur de filtreEnseignementLong.
     * @param filtreEnseignementLong le filtreEnseignementLong à modifier.
     */
    public void setFiltreEnseignementLong(String filtreEnseignementLong) {
        this.filtreEnseignementLong = filtreEnseignementLong;
    }

    /**
     * Accesseur de filtreEnseignementCourt.
     * @return le filtreEnseignementCourt
     */
    public String getFiltreEnseignementCourt() {
        return filtreEnseignementCourt;
    }

    /**
     * Mutateur de filtreEnseignementCourt.
     * @param filtreEnseignementCourt le filtreEnseignementCourt à modifier.
     */
    public void setFiltreEnseignementCourt(String filtreEnseignementCourt) {
        this.filtreEnseignementCourt = filtreEnseignementCourt;
    }  
    
   
    /**
     * Accesseur de listeFractionnement {@link #listeFractionnement}.
     * @return retourne listeFractionnement
     */
    public List<SelectItem> getListeFractionnement() {
        return listeFractionnement;
    }

    /**
     * Mutateur de listeFractionnement {@link #listeFractionnement}.
     * @param listeFractionnement le listeFractionnement to set
     */
    public void setListeFractionnement(List<SelectItem> listeFractionnement) {
        this.listeFractionnement = listeFractionnement;
    }

    /**
     * Accesseur de fractionnement {@link #fractionnement}.
     * @return retourne fractionnement
     */
    public Integer getFractionnement() {
        return fractionnement;
    }

    /**
     * Mutateur de fractionnement {@link #fractionnement}.
     * @param fractionnement le fractionnement to set
     */
    public void setFractionnement(Integer fractionnement) {
        this.fractionnement = fractionnement;
    }

    /**
     * @return the horairesCours
     */
    public List<GrilleHoraireDTO> getHorairesCours() {
        return horairesCours;
    }
    
    /**
     * @param horairesCours the horairesCours to set
     */
    public void setHorairesCours(List<GrilleHoraireDTO> horairesCours) {
        this.horairesCours = horairesCours;
        
        recalculerHorairesCoursJSON();
    }
    
    /**
     * Recalcul la chaine JSON contenant la grille horaire.
     * Cette methode est appellée dès que la liste de la grille horaire est modifiée
     * ou qu'un de ses elements est modifié.
     */
    public void recalculerHorairesCoursJSON() {
        
        //Avant de créer le JSON, trie la liste pour mettre les titres Plage X en ordre
        List<GrilleHoraireDTO> listeTriee = GenerateurDTO.trierGrilleHoraire(horairesCours);

        GenerateurDTO.mettreTitreGrilleHoraire(listeTriee);
        
        final Gson gson = new Gson();
        final String json = gson.toJson(horairesCours);
        this.horairesCoursJSON = json;
    }
    
    /**
     * @return the horairesCoursJSON
     */
    public String getHorairesCoursJSON() {
        return horairesCoursJSON;
    }
    
    /**
     * @param horairesCoursJSON the horairesCoursJSON to set
     */
    public void setHorairesCoursJSON(String horairesCoursJSON) {
        this.horairesCoursJSON = horairesCoursJSON;
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
     * Accesseur de plageEditee {@link #plageEditee}.
     * @return retourne plageEditee
     */
    public GrilleHoraireDTO getPlageEditee() {
        return plageEditee;
    }

    /**
     * Mutateur de plageEditee {@link #plageEditee}.
     * @param plageEditee le plageEditee to set
     */
    public void setPlageEditee(GrilleHoraireDTO plageEditee) {
        this.plageEditee = plageEditee;
    }

    /**
     * Accesseur de vraiOuFauxDevoir {@link #vraiOuFauxDevoir}.
     * @return retourne vraiOuFauxDevoir
     */
    public Boolean getVraiOuFauxDevoir() {
        return vraiOuFauxDevoir;
    }

    /**
     * Mutateur de vraiOuFauxDevoir {@link #vraiOuFauxDevoir}.
     * @param vraiOuFauxDevoir le vraiOuFauxDevoir to set
     */
    public void setVraiOuFauxDevoir(Boolean vraiOuFauxDevoir) {
        this.vraiOuFauxDevoir = vraiOuFauxDevoir;
    }

    /**
     * Accesseur de listeCategorieTypeDevoir {@link #listeCategorieTypeDevoir}.
     * @return retourne listeCategorieTypeDevoir
     */
    public List<SelectItem> getListeCategorieTypeDevoir() {
        return listeCategorieTypeDevoir;
    }

    /**
     * Mutateur de listeCategorieTypeDevoir {@link #listeCategorieTypeDevoir}.
     * @param listeCategorieTypeDevoir le listeCategorieTypeDevoir to set
     */
    public void setListeCategorieTypeDevoir(
            List<SelectItem> listeCategorieTypeDevoir) {
        this.listeCategorieTypeDevoir = listeCategorieTypeDevoir;
    }

    /**
     * Accesseur de listeDureeCours {@link #listeDureeCours}.
     * @return retourne listeDureeCours
     */
    public List<SelectItem> getListeDureeCours() {
        return listeDureeCours;
    }

    /**
     * Mutateur de listeDureeCours {@link #listeDureeCours}.
     * @param listeDureeCours le listeDureeCours to set
     */
    public void setListeDureeCours(List<SelectItem> listeDureeCours) {
        this.listeDureeCours = listeDureeCours;
    }

    /**
     * Accesseur de nomMethodeSauvegarde {@link #nomMethodeSauvegarde}.
     * @return retourne nomMethodeSauvegarde
     */
    public String getNomMethodeSauvegarde() {
        return nomMethodeSauvegarde;
    }

    /**
     * Mutateur de nomMethodeSauvegarde {@link #nomMethodeSauvegarde}.
     * @param nomMethodeSauvegarde le nomMethodeSauvegarde to set
     */
    public void setNomMethodeSauvegarde(String nomMethodeSauvegarde) {
        this.nomMethodeSauvegarde = nomMethodeSauvegarde;
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
     * Accesseur de couleurSemainePair. 
     * @return retourne couleurSemainePair
     */
    public String getCouleurSemainePair() {
        return TypeCouleurJour.PAIR.getId();
    }

    /**
     * Accesseur de couleurSemaineImpair.
     * @return retourne couleurSemaineImpair
     */
    public String getCouleurSemaineImpair() {
        return TypeCouleurJour.IMPAIR.getId();
    }

}
