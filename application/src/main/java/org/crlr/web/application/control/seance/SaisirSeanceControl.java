/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AdminControl.java,v 1.13 2010/06/08 12:24:06 ent_breyton Exp $
 */

package org.crlr.web.application.control.seance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.AgendaSeanceDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.dto.application.emploi.RechercheEmploiQO;
import org.crlr.dto.application.emploi.SemaineDTO;
import org.crlr.dto.application.remplacement.RechercheRemplacementQO;
import org.crlr.dto.application.remplacement.RemplacementDTO;
import org.crlr.dto.application.remplacement.TypeReglesRemplacement;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.dto.application.seance.ResultatRechercheSeanceDTO;
import org.crlr.dto.application.sequence.RechercheSequenceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.services.EmploiService;
import org.crlr.services.EtablissementService;
import org.crlr.services.ImagesServlet;
import org.crlr.services.RemplacementService;
import org.crlr.services.SeanceService;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.control.AbstractControl;
import org.crlr.web.application.form.seance.SaisirSeanceForm;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.dto.GrilleHoraireDTO;
import org.crlr.web.dto.MoisDTO;
import org.crlr.web.utils.MessageUtils;
import org.crlr.web.utils.NavigationUtils;
import org.joda.time.LocalDate;

import com.google.gson.Gson;

/**
 * AdminControl.
 *
 * @author breytond
 * @version $Revision: 1.13 $
 */
@ManagedBean(name="saisirSeance")
@ViewScoped
public class SaisirSeanceControl extends AbstractControl<SaisirSeanceForm> {

    
    /** Service etablissement. */
    @ManagedProperty(value="#{etablissementService}")
    private transient EtablissementService etablissementService; 
    
    /** Service emploi du temps. */
    @ManagedProperty(value="#{emploiService}")
    private transient EmploiService emploiService;
    
    /** Service Seance. */
    @ManagedProperty(value="#{seanceService}")
    private transient SeanceService seanceService;
    
    
    @ManagedProperty(value = "#{ajoutSeance}")
    private transient AjoutSeanceControl ajoutSeanceControl;
    
    /** Service Remplacement. */
    @ManagedProperty(value = "#{remplacementService}")
    private transient RemplacementService remplacementService;

    
    private static final String ERREUR_GENERIQUE = "Une erreur est survenue.";

    private static final String ERREUR_PAS_SEQUENCE = "Il n'y a pas de séquence valide pour cette date.";

    /**
     * Injection Spring de etablissementService.
     * @param etablissementService le etablissementService à modifier.
     */
    public void setEtablissementService(EtablissementService etablissementService) {
        this.etablissementService = etablissementService;
    }
    
    
    /**
     * Mutateur de seanceService {@link #seanceService}.
     * @param seanceService le seanceService to set
     */
    public void setSeanceService(SeanceService seanceService) {
        this.seanceService = seanceService;
    }


    /**
     * Mutateur de emploiService {@link #emploiService}.
     * @param emploiService le emploiService to set
     */
    public void setEmploiService(EmploiService emploiService) {
        this.emploiService = emploiService;
    }




    /**
     * Constructeur.
     */
    public SaisirSeanceControl() {
        super(new SaisirSeanceForm());        
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    public void onLoad() {
        
        form.reset();
        try {
            
            // Gere l'acces depuis l'ecran des visa : l'idEnseignant n'est pas l'utilisateur connecte.
            final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
            final Integer idEnseignant = utilisateurDTO.getUserDTO().getIdentifiant();
            form.setIdEnseignant(idEnseignant);
            
            // Gère navigation en provenance de planning mensule, charge la semaine sélectionnée
            final SemaineDTO semaineSelectionnee = (SemaineDTO) ContexteUtils
                    .getContexteOutilControl().recupererEtSupprimerObjet(
                            SemaineDTO.class.getName());
            
            form.setAfficheRetour(semaineSelectionnee != null);
            
            alimenteBarreSemaine(semaineSelectionnee);
        } catch (Exception e) {
            log.error( "Exception", e);
        }
        
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        try {
            // Charge la grille horaire pour l'etablissement
            final List<GrilleHoraireDTO> listeHoraires = 
                    GenerateurDTO.generateGrilleHoraireFromDb(
                            etablissementService.findHorairesCoursEtablissement(utilisateurDTO.getIdEtablissement()).getValeurDTO());
            final Gson gson = new Gson();
            final String json = gson.toJson(listeHoraires);
            form.setHorairesJSON(json);
            
            form.setEtablissement(etablissementService.findEtablissement(utilisateurDTO.getIdEtablissement()).getValeurDTO());


        } catch (MetierException e) {
            log.debug("Echec de l'initialisation de la page de saisie des seances par semaine. : {0}",e.getMessage());
        } 
       
    }
    
    /**
     * Alimente la liste des semaines et mois.
     * @param semaineSelectionnee s
     * @throws MetierException exception
     */
    private void alimenteBarreSemaine(SemaineDTO semaineSelectionnee) throws MetierException {
        // Charge la barre des semaines / mois
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        final AnneeScolaireDTO anneeScolaireDTO = utilisateurDTO.getAnneeScolaireDTO();
        
        
        final String alternanceSemaine =  etablissementService.findAlternanceSemaine(utilisateurDTO.getIdEtablissement());
        final Boolean vraiOuFauxAlternance = !org.apache.commons.lang.StringUtils.isEmpty(alternanceSemaine);
        final List<BarreSemaineDTO> listeSemaine = 
            GenerateurDTO.generateListeSemaine(anneeScolaireDTO, alternanceSemaine, vraiOuFauxAlternance); 
        form.setListeBarreSemaine(listeSemaine);
        form.setListeMois( 
                GenerateurDTO.genererListeMois(anneeScolaireDTO.getDateRentree(),
                        anneeScolaireDTO.getDateSortie()));        
        form.setDecalageDebutAnnee(MoisDTO.getDecalageDebutAnnee(anneeScolaireDTO.getDateRentree()));
        
        if (semaineSelectionnee != null) {
            selectionnerSemaineParDate(semaineSelectionnee.getDebutSemaine());
        } else {
            //pas de sélection, utilise date d'aujourd'hui
            form.setSemaineSelectionne(GenerateurDTO.selectionnerSemaineJour(form.getListeBarreSemaine()));

            chargerEmploiDuTempsSemaine(null);
        }
    }
    
    
    
    /**
     * @param lundiDate le début d'une semaine.
     * @throws MetierException ex
     */
    private void selectionnerSemaineParDate(Date lundiDate) throws MetierException {
        for(final BarreSemaineDTO semaine : form.getListeBarreSemaine()) {
            if (semaine.getLundi().equals(lundiDate)) {
                semaine.setVraiOuFauxSelection(true);
                form.setSemaineSelectionne(semaine);
            } else {
                semaine.setVraiOuFauxSelection(false);
            }
        }
        chargerEmploiDuTempsSemaine(null);
    }

    /**
     * Appelee lors de la selection d'une semaine. 
     * @throws MetierException : exception
     */
    public void selectionnerSemaine() throws MetierException {
        chargerEmploiDuTempsSemaine(null);

        // Reset la plage selectionnee
        form.setPlageSelected(null);
    }
 
  
 
    /**
     * Charge l'emploi du temps prevu pour la semaine selectionne.
     * Stocke le resultat dans le formulaire.
     * @param idSeanceSelected id de la seance qui est en cours de selection 
     * @return 
     * @throws MetierException exception
     */
    private void chargerEmploiDuTempsSemaine(Integer idSeanceSelected) throws MetierException {
        final RechercheEmploiQO rechercheEmploiQO = new RechercheEmploiQO();
        final BarreSemaineDTO semaineSelectionne = form.getSemaineSelectionne();
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        final Integer idEnseignant = form.getIdEnseignant();
        final Integer idEtablissement = utilisateurDTO.getIdEtablissement();
        
        // One ne charge pas l'EDT sur le onLoad quand l'user est n'est pas un enseignant 
        if ((idEnseignant==null)||(idEnseignant.equals(ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO().getIdentifiant()) && 
            !Profil.ENSEIGNANT.equals(ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getProfil()))) {
            return ;
        }
        
        if (semaineSelectionne != null ) {

            // Charge les sequences correspondante pour chacune des case de l'EDT
            ajoutSeanceControl.getForm().setListeSequence(chercherSequenceSemaine());
            
            // Charge les cases de l'EDT pour la semaine selectionnee
            rechercheEmploiQO.setDateDebut(semaineSelectionne.getLundi());
            rechercheEmploiQO.setDateFin(semaineSelectionne.getDimanche());
            rechercheEmploiQO.setIdEtablissement(idEtablissement);
            rechercheEmploiQO.setIdEnseignant(idEnseignant);
            rechercheEmploiQO.setTypeSemaine(semaineSelectionne.getTypeJourEmploi());
            
            final ResultatDTO<List<DetailJourEmploiDTO>> resultat = emploiService.findEmploiConsolidation(rechercheEmploiQO);
            final List<DetailJourEmploiDTO> listeEmploiDuTemps = resultat.getValeurDTO();
            form.setListeEmploiDuTemps(listeEmploiDuTemps);
            
            // Charge les seances enregistrees pour cette semaine
            chargerListeSeance();
            
            // Met a jour la liste agenda pour la semaine selectionnee 
            final Integer idSelected;
            if (idSeanceSelected != null) {
                idSelected = idSeanceSelected;
            } else if (form.getPlageSelected() != null && form.getPlageSelected().getSeance()!=null) {
                idSelected = form.getPlageSelected().getSeance().getId();
            } else {
                idSelected = null;
            }
            form.majListeAgenda(idSelected);
            
            // Verifie pour chaque case, si il y a une ou plusiuers sequence possible
            calculerListeSequenceParJour();
            
            // maj les infos JSON utilise par l'objet JS  
            form.recalculerDataAgendaJSON();
        }
    }
    
    /**
     * Methode qui va chercher et alimenter la listeSeance sur le formulaire 
     * correspondant aux seances de la semaine en cours.
     */
    private void chargerListeSeance() {

        // Initialise les criteres de recherche des seances pour la semaine selectionnee
        final RechercheSeanceQO rechercheSeanceQO = new RechercheSeanceQO();
        final Integer idEnseignant = form.getIdEnseignant();
        final Integer idEtablissement = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getIdEtablissement();
        rechercheSeanceQO.setIdEnseignant(idEnseignant);
        rechercheSeanceQO.setIdEtablissement(idEtablissement);
        rechercheSeanceQO.setDateDebut(form.getSemaineSelectionne().getLundi());
        rechercheSeanceQO.setDateFin(form.getSemaineSelectionne().getDimanche());
        rechercheSeanceQO.setIdEnseignantConnecte(ContexteUtils.getContexteUtilisateur().getUtilisateurDTOConnecte().getUserDTO().getIdentifiant());
        try {
            final ResultatDTO<List<ResultatRechercheSeanceDTO>> resultat = seanceService.findSeance(rechercheSeanceQO);
            final List<ResultatRechercheSeanceDTO> listeResultat = resultat.getValeurDTO();
            final List<SeanceDTO> listeSeance = convertirListeResultatRechercheSeanceDTO(listeResultat);
            form.setListeSeance(listeSeance);
        } catch (MetierException e) {
            this.form.setListeSeance(new ArrayList<SeanceDTO>());
            log.debug("{0}", e.getMessage());
        }
    }

    /**
     * Construit une liste de SeanceDTO à partir d'une liste de ResultatRechercheSeanceDTO.
     * Seul le champ id de la seance est positionne dans le SeanceDTO.
     * LEs autres champs de SeanceDTO sont charges lors de l'acces au detail de la seance.
     * @param listeResultat la liste issue de la recherche de seance 
     * @return une liste de SeanceDTO
     */
    private List<SeanceDTO> convertirListeResultatRechercheSeanceDTO(final List<ResultatRechercheSeanceDTO> listeResultat) {
        final List<SeanceDTO> listeSeance = new ArrayList<SeanceDTO>();
        if (!CollectionUtils.isEmpty(listeResultat)) {
            for(final ResultatRechercheSeanceDTO resultat : listeResultat) {
                final SeanceDTO seance = ObjectUtils.clone(resultat);
                listeSeance.add(seance);
            }
        }
        return listeSeance;
    }
    
    
    
    /**
     * Retourne les sequences qui correspondent à la case agenda : Enseignement + Classe/groupe identiques + Date incluse.
     * @param caseAgenda la case testéé
     * @return la liste des sequences.
     */
    private List<SequenceDTO> getListeSequenceCorrespondantDetail(final AgendaSeanceDTO caseAgenda) {
        final List<SequenceDTO> listeSequence = new ArrayList<SequenceDTO>();
        for (final SequenceDTO sequence :  ajoutSeanceControl.getForm().getListeSequence()) {
            // Les dates ne correspondent pas 
            if (caseAgenda.getDate().before(sequence.getDateDebut()) || caseAgenda.getDate().after(sequence.getDateFin())) {
                continue;
            }
            // Case EDT 
            if (caseAgenda.getDetail() != null) {
                // Matiere differentes
                if (!caseAgenda.getDetail().getMatiere().getId().equals(sequence.getIdEnseignement())) {
                    continue;
                }
                // Type Groupe / Classe different
                if (!caseAgenda.getDetail().getGroupeOuClasse().getTypeGroupe().equals(sequence.getGroupesClassesDTO().getTypeGroupe())) {
                    continue;
                }
                // Classe differente
                if (caseAgenda.getDetail().getIdClasse() != null &&
                   TypeGroupe.CLASSE.equals(sequence.getGroupesClassesDTO().getTypeGroupe()) && 
                   !caseAgenda.getDetail().getIdClasse().equals(sequence.getIdClasseGroupe())) {
                    continue;
                } 
                // Groupe different
                if (caseAgenda.getDetail().getIdGroupe() != null &&
                    TypeGroupe.GROUPE.equals(sequence.getGroupesClassesDTO().getTypeGroupe()) && 
                    !caseAgenda.getDetail().getIdGroupe().equals(sequence.getIdClasseGroupe())) {
                     continue;
                } 
                // Tout correspond a la case EDT, on ajoute la sequence
                listeSequence.add(sequence);
            } 
        }
        return listeSequence;
    }
    
  
        
    /** 
     * Parcours les cases de l'agenda, et determine pour chacune les sequences disponible.
     */
    private void calculerListeSequenceParJour() {
        for (final AgendaSeanceDTO caseAgenda : form.getListeAgenda()) {
            List<SequenceDTO> listeSequence;
            if (caseAgenda.getDetail() != null) { 
                listeSequence = getListeSequenceCorrespondantDetail(caseAgenda);
            } else if (caseAgenda.getSeance()!=null) {
                listeSequence = new ArrayList<SequenceDTO>();
                listeSequence = AjoutSeanceControl
                        .getListeSequenceCorrespondantSeance(caseAgenda
                                .getSeance(), ajoutSeanceControl.getForm()
                                .getListeSequence());
                
            } else {
                listeSequence = new ArrayList<SequenceDTO>();
            }
            caseAgenda.setListeSequence(listeSequence);
            final ContexteUtilisateur contextUtilisateur = ContexteUtils.getContexteUtilisateur(); 
            caseAgenda.setAfficheSelectionSequence(
                    BooleanUtils.isNotTrue(contextUtilisateur.getUtilisateurDTO().getVraiOuFauxEtabSaisieSimplifiee()) || 
                    listeSequence.size()>1);
        }
    }
    
    /**
     * Recherche les sequences actives correspondant a la semaine sélectionnée. 
     * @return la liste des sequences active pour la semaine.
     */
    private List<SequenceDTO> chercherSequenceSemaine() {
        
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        final RechercheSequenceQO rechercheSequence = new RechercheSequenceQO();
        rechercheSequence.setDateDebut(form.getSemaineSelectionne().getLundi());
        rechercheSequence.setDateFin(form.getSemaineSelectionne().getDimanche());
        rechercheSequence.setIdEnseignant(form.getIdEnseignant());
        rechercheSequence.setIdEtablissement(utilisateurDTO.getIdEtablissement());

        // charge les sequence actives pour la semaine 
        final List<SequenceDTO> listeSequence = this.emploiService.chercherSequenceSemaine(rechercheSequence);
        
        // Supprime le champ description qui n'est pas utilise et qui pose des pb lors du JSON parse s'il contient des retour chariot (#13) 
        for (final SequenceDTO sequence : listeSequence) {
            sequence.setDescription(null);
        }
        return listeSequence;
    }    
    
    /**
     * Methode appelee lors de la selection d'une seance existante dans l'agenda affiche pour la semaine courante.
     */
    public void afficherSeanceSelected() {
        final Integer indice = form.getPlageSelectedIndex();
        if (indice != null) {
            final AgendaSeanceDTO caseAgenda = form.getListeAgenda().get(indice);
            final Integer idSeance = caseAgenda.getSeance().getId();
            
            // Charge les info de la seance selectionnee et la positionne dans la case selectionnee
            
            ajoutSeanceControl.alimenterSeance(idSeance);
            
            form.setPlageSelected(caseAgenda);
        } else {
            form.setPlageSelected(null);
        }
    }
    
    /**
     * Methode appelee lors de l'ajout d'une nouvelle seance sur une case de l'emploi du temps vide
     * pour laquelle il n'y a qu'une seul séquence possible.
     * 
     * Aussi une fois la séquence est sélectionnée
     * 
     * jsFunction : afficherSelectionSequence ; appeler quand une case EDT est cliquée
     */
    public void ajouterNouvelleSeance() {
        final Integer indice = form.getPlageSelectedIndex();
        
        // Positionne la plage en cours d'edition
        final AgendaSeanceDTO caseAgenda = form.getListeAgenda().get(indice);
        if (caseAgenda == null) {
            form.setPlageSelected(null);
            return;
        }
        
        // Met a jour les champs de la seance en correspondance avec la case de l'EDT
        final SeanceDTO nouvelleSeance = new SeanceDTO();
        nouvelleSeance.initFromEDT(caseAgenda.getDetail());
        nouvelleSeance.setAccesEcriture(true);
        nouvelleSeance.setDate(caseAgenda.getDate());
        
        final ContexteUtilisateur contexteUtilisateur = ContexteUtils.getContexteUtilisateur();
        final UtilisateurDTO utilisateur = contexteUtilisateur.getUtilisateurDTO();
        final UtilisateurDTO utilisateurConnecte = contexteUtilisateur.getUtilisateurDTOConnecte();
        
        //Mode remplaçement
        if (!utilisateur.getUserDTO().getIdentifiant().equals(utilisateurConnecte.getUserDTO().getIdentifiant())) {
            

            //Verification si l'enseignant est en mode remplaçaent
            RechercheRemplacementQO rechercheRemplacementQO = new RechercheRemplacementQO();
            
            //L'id de séquence est l'enseignant absent
            rechercheRemplacementQO.setIdEnseignantAbsent(utilisateur.getUserDTO().getIdentifiant());
            
            rechercheRemplacementQO.setIdEnseignantRemplacant(utilisateurConnecte.getUserDTO().getIdentifiant());
            
            rechercheRemplacementQO.setIdEtablissement(utilisateur.getIdEtablissement());
            
            rechercheRemplacementQO.setDate(new LocalDate(nouvelleSeance.getDate()));
            
            List<RemplacementDTO> listeRemplacementsDTO = 
                    remplacementService.findListeRemplacement(rechercheRemplacementQO).getValeurDTO();
            
            if (CollectionUtils.isEmpty(listeRemplacementsDTO)) {
                MessageUtils.addMessage(new Message(TypeReglesRemplacement.REMPLACEMENT_14.name(), Nature.BLOQUANT), getClass());
                return;
            }
        
        }
       
        //Intialise le choix de séquences
        List<SequenceDTO> listeSequences = 
                AjoutSeanceControl.getListeSequenceCorrespondantSeance(nouvelleSeance, ajoutSeanceControl.getForm().getListeSequence());
        
        ajoutSeanceControl.getForm().setListeSequenceSeance(listeSequences);
        
        log.info("AjouterNouvelleSeance taille de la liste sequences total {0}, filtrée {1}", 
                ajoutSeanceControl.getForm().getListeSequence().size(), 
                ajoutSeanceControl.getForm().getListeSequenceSeance().size());
        
        // Si une seule sequence possible, on la postionne dans le formulaire
        if (caseAgenda.getListeSequence()!= null && caseAgenda.getListeSequence().size() == 1) {
           nouvelleSeance.setSequenceDTO(caseAgenda.getListeSequence().get(0));
        }
        
        // Prepare le minimum de devoirs affichés            
        ajoutSeanceControl.completeListeDevoir(nouvelleSeance);
        
        if (caseAgenda.getSeance() != null) {
            log.error("Seance id n'est pas null mais on est en train de créer une séance...");
        }
        
        ajoutSeanceControl.getForm().setSeance(nouvelleSeance);
        
        if (CollectionUtils.isEmpty(caseAgenda.getListeSequence())) {
            MessageUtils.addMessage(new Message(ERREUR_PAS_SEQUENCE,
                    Nature.BLOQUANT), getClass());
            return;
            
        }
        
        // Selectionne la case
        form.setPlageSelected(caseAgenda);
        
        // Si on affiche la selection de sequence (= plusieurs sequence) on stocke la plage dans le prepare
        // Sinon on positionne directement la plage dans le selected
        // Si on passe par la selection de sequence
        if (caseAgenda.getAfficheSelectionSequence()) {
            final AgendaSeanceDTO caseAgendaPrepare = ObjectUtils.clone(caseAgenda);
            form.setPlagePrepare(caseAgendaPrepare);
            
        // Met la seance dans la case de l'EDT direct si pas de selection de sequence 
        } else if (caseAgenda.getListeSequence().size() == 1){
            form.setPlageSelected(caseAgenda);
                            
            ajoutSeanceControl.chargerSequence();
        }
        
    }
    
    
    /**
     * Methode appelee lors de l'ajout d'une nouvelle seance libre (sans plage EDT correspondante.
     * 
     * Le but est d'initialiser les objets, mais pas pour faire des contrôls quel qu'ils soient
     */
    public void ajouterNouvelleSeanceLibre() {
        
        //  Ajoute une nouvelle plage pour la creation d'une seance libre
        // La plage n'est pas ajoutee dans la liste agenda pour le moment.
        final SeanceDTO seance = new SeanceDTO();
        seance.setAccesEcriture(true);
        final AgendaSeanceDTO plageAgenda = new AgendaSeanceDTO();
        ajoutSeanceControl.getForm().setSeance(seance);
        
       
        form.setPlagePrepare(plageAgenda);
        form.setPlageSelectedIndex(null);
        
    }    

    /**
     * Methode appelle apres que l'ajout d'une nouvelle seance libre (sans plage EDT correspondante)
     * ai été ajoutée dans la liste agenda.
     */
    public void afficherNouvelleSeanceLibre() {
        final AgendaSeanceDTO plageAgenda = form.getPlagePrepare();
        
        if (plageAgenda == null) {
            log.error("Plage agenda est null!");
            MessageUtils.addMessage(new Message(ERREUR_GENERIQUE,
                    Nature.BLOQUANT), getClass());
            return;
        }
        
        //Les données de la case ont été affecté par la <a4j:jsFunction balise
        final SeanceDTO seance = ajoutSeanceControl.getForm().getSeance();
        if (seance == null) {
            MessageUtils.addMessage(new Message(ERREUR_GENERIQUE, Nature.BLOQUANT), getClass());
            return;
        }
        
        plageAgenda.setDate(seance.getDate()); 
        
        final ContexteUtilisateur contexteUtilisateur = ContexteUtils.getContexteUtilisateur();
        final UtilisateurDTO utilisateur = contexteUtilisateur.getUtilisateurDTO();
        final UtilisateurDTO utilisateurConnecte = contexteUtilisateur.getUtilisateurDTOConnecte();
        
        //Mode remplaçement
        if (!utilisateur.getUserDTO().getIdentifiant().equals(utilisateurConnecte.getUserDTO().getIdentifiant())) {
            

            //Verification si l'enseignant est en mode remplaçaent
            RechercheRemplacementQO rechercheRemplacementQO = new RechercheRemplacementQO();
            
            //L'id de séquence est l'enseignant absent
            rechercheRemplacementQO.setIdEnseignantAbsent(utilisateur.getUserDTO().getIdentifiant());
            
            rechercheRemplacementQO.setIdEnseignantRemplacant(utilisateurConnecte.getUserDTO().getIdentifiant());
            
            rechercheRemplacementQO.setIdEtablissement(utilisateur.getIdEtablissement());
            
            rechercheRemplacementQO.setDate(new LocalDate(seance.getDate()));
            
            List<RemplacementDTO> listeRemplacementsDTO = 
                    remplacementService.findListeRemplacement(rechercheRemplacementQO).getValeurDTO();
            
            if (CollectionUtils.isEmpty(listeRemplacementsDTO)) {
                MessageUtils.addMessage(new Message(TypeReglesRemplacement.REMPLACEMENT_14.name(), Nature.BLOQUANT), getClass());
                return;
            }
        
        }
    
        // Positionne toutes les sequences possible sur cette seance libre valides pour la date
        List<SequenceDTO> listeSequence = AjoutSeanceControl
                .getListeSequenceCorrespondantSeance(seance, ajoutSeanceControl.getForm()
                        .getListeSequence());
        
        //Pour le popup choix de séquence
        plageAgenda.setListeSequence( listeSequence );
        
        
        plageAgenda.setAfficheSelectionSequence(
                BooleanUtils.isNotTrue(utilisateur.getVraiOuFauxEtabSaisieSimplifiee()) || 
                listeSequence.size()>1);
    
        //Pour la liste dans la formulaire de modif (saisieSeance)
        ajoutSeanceControl.getForm().setListeSequenceSeance( listeSequence );
        
        if (CollectionUtils.isEmpty(listeSequence)) {
            MessageUtils.addMessage(new Message(ERREUR_PAS_SEQUENCE, Nature.BLOQUANT), getClass());
        }
            

    }
    
    /**
     * Methode appele par le JS suite au souhait de crééer une seance sur plage vide (hors EDT) 
     * alors que des modif sont en cours sur la séance active. 
     */
    public void conserverInfoNouvelleSeancelibre() {
        
    }
    
    /**
     * Methode appelle suite a la selection d'une sequence en vue de creer une nouvelle seance sur une case vide
     * de l'emploi du temps. 
     */
    public void selectionnerSequenceJour() {
        
        final SeanceDTO seance = ajoutSeanceControl.getForm().getSeance();
        
        // La sequence choisie par l'utilisateur est déjà stocké dans séance
       
        // Positionne dans la plage selectionnee la sequence choisie par l'utilisateur
        final AgendaSeanceDTO plageAgenda = form.getPlagePrepare();
        
        if (plageAgenda.getDetail()!= null) {
            seance.initFromEDT(plageAgenda.getDetail());
        }
        
        
        // Prepare le minimum de devoirs affichés
        ajoutSeanceControl.completeListeDevoir(seance);
        
        // Supprime l'ancienne plage et positionne celle qui a ete preparee
        // si on est passe par l'ajout libre, il n'y a pas de maj dans la liste a faire
        if (form.getPlageSelectedIndex() != null) {
            form.getListeAgenda().remove(form.getPlageSelectedIndex().intValue());
            form.getListeAgenda().add(form.getPlageSelectedIndex().intValue(), plageAgenda);
        }
        form.setPlageSelected(plageAgenda);
        form.setPlagePrepare(null);
        
        
        ajoutSeanceControl.chargerSequence();
        
       
    }
    

    
    /**
     * Methode appelee lors de la sauvegarde d'une seance en cours d'edition .
     * @throws MetierException  l'exception
     */
    public void sauver() throws MetierException {
        ajoutSeanceControl.sauver();
        
        if (BooleanUtils.isTrue(MessageUtils.hasMessageBloquant())) {
            //Saute la rafraîchissement
            return;
        }
        // Recupere le id de la seance qui a ete sauver
        final Integer idSeance = ajoutSeanceControl.getForm().getSeance().getId();
        
        // Refresh l'EDT
        chargerEmploiDuTempsSemaine(idSeance);
    
    }
    

    /**
     * Methode appelee lors de la sauvegarde d'une seance précédente en cours d'edition .
     * @throws MetierException  l'exception
     */
    public void sauverSeancePrecedente() throws MetierException {
        //Déléguer le travail au sous controleur
        ajoutSeanceControl.sauverSeancePrecedente();
        
        // Refresh l'EDT car une séance précédente peut être afficher dans la semaine sélectionnée
        chargerEmploiDuTempsSemaine(null);
    }

     
    /**
     * Methode appelee lors de la suppression d'une seance en cours d'edition.
     * @throws MetierException  l'exception
     */
    public void delete() throws MetierException {
        final SeanceDTO seance = ajoutSeanceControl.getForm().getSeance();
        
        if (seance == null) {
            log.error("Seance null");
            return;
        }
        
        try {
            final ResultatRechercheSeanceDTO resultatRechercheSeanceDTO = new ResultatRechercheSeanceDTO();
            resultatRechercheSeanceDTO.setId(seance.getId());
            final ResultatDTO<Integer> resultatDTO = this.seanceService.deleteSeance(resultatRechercheSeanceDTO);
            log.debug("Modification de la seance result = {0}", resultatDTO.getValeurDTO());
        } catch (final MetierException e) {
            log.debug("{0}", e.getMessage());
        }
        
        
        // Refresh l'EDT
        chargerEmploiDuTempsSemaine(null);
        
        // Reset la plage selectionnee
        form.setPlageSelected(null);
    }    
    
    
    
    /**
     * Declenche si on clique sur une plage alors que des modifs sont en cours.
     */
    public void clickSurPlage() {
         
    }
    
    /**
     * Navigue vers écran précedent.
     * @return ma chaine de navigation.
     */
    public String retour() {
        form.reset();
        return NavigationUtils.retourOutilPrecedentEnDepilant();
    }
    


    
    /**
     * Charge sous forme d'image/html la description du devoir en cours.
     * Cette méthode est utilisée lors du basculement en vue visulation/modification 
     */
    public void chargeImagesLatexDevoir() {
        final DevoirDTO devoirDTO = form.getDevoirSelected();
        devoirDTO.setDescriptionHTML(ImagesServlet.genererLatexImage(devoirDTO.getDescription()));        
    }


    /**
     * @param ajoutSeanceControl the ajoutSeanceControl to set
     */
    public void setAjoutSeanceControl(AjoutSeanceControl ajoutSeanceControl) {
        this.ajoutSeanceControl = ajoutSeanceControl;
    }


    /**
     * @param remplacementService the remplacementService to set
     */
    public void setRemplacementService(RemplacementService remplacementService) {
        this.remplacementService = remplacementService;
    }
    
}

