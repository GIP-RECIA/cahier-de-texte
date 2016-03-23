/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConsoliderEmpControl.java,v 1.14 2010/06/01 13:45:24 ent_breyton Exp $
 */

package org.crlr.web.application.control.emploi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections.CollectionUtils;
import org.crlr.dto.Outil;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.ChargeDevoirQO;
import org.crlr.dto.application.devoir.ChargeTravailDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.devoir.RecherchePieceJointeDevoirQO;
import org.crlr.dto.application.devoir.SaveDevoirQO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.dto.application.emploi.JoursMensuelDTO;
import org.crlr.dto.application.emploi.SemaineDTO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.facade.DevoirFacadeService;
import org.crlr.services.ImagesServlet;
import org.crlr.services.SeanceService;
import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.control.AbstractControl;
import org.crlr.web.application.control.PopupPiecesJointesControl;
import org.crlr.web.application.control.seance.AjoutSeanceControl;
import org.crlr.web.application.form.emploi.PlanningMensuelForm;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.BarreMoisDTO;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.utils.FileUploadUtils;
import org.crlr.web.utils.MessageUtils;
import org.crlr.web.utils.NavigationUtils;

/**
 * PlanningMensuelControl.
 *
 * @author carriere
 * @version $Revision: 1.14 $
 */
@ManagedBean(name="planning")
@ViewScoped
public class PlanningMensuelControl extends AbstractControl<PlanningMensuelForm> {
    /** Service . */
    @ManagedProperty(value="#{seanceService}") 
    private SeanceService seanceService;
    /** Service . */
    @ManagedProperty(value="#{devoirFacade}") 
    private DevoirFacadeService devoirService;
    
    /** Le controleur des seances ajout . */
    @ManagedProperty(value="#{ajoutSeance}")
    private transient AjoutSeanceControl ajoutSeance;
    
    /** Controlleur . */
    @ManagedProperty(value="#{popupPiecesJointes}") 
    private PopupPiecesJointesControl popupPiecesJointes;
    
    
    
    /**
     * Mutateur de seanceService.
     *
     * @param seanceService le seanceService à modifier.
     */
    public void setSeanceService(SeanceService seanceService) {
        this.seanceService = seanceService;
    }

    /**
     * Mutateur de devoirService.
     * @param devoirService le devoirService à modifier.
     */
    public void setDevoirService(DevoirFacadeService devoirService) {
        this.devoirService = devoirService;
    }

    /**
     * Mutateur de popupPiecesJointes.
     * @param popupPiecesJointes le popupPiecesJointes à modifier.
     */
    public void setPopupPiecesJointes(PopupPiecesJointesControl popupPiecesJointes) {
        this.popupPiecesJointes = popupPiecesJointes;
    }
    
/**
     * Mutateur de ajoutSeance {@link #ajoutSeance}.
     * @param ajoutSeance le ajoutSeance to set
     */
    public void setAjoutSeance(AjoutSeanceControl ajoutSeance) {
        this.ajoutSeance = ajoutSeance;
    }

/**
     * Constructeur.
     */
    public PlanningMensuelControl() {
        super(new PlanningMensuelForm());
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    public void onLoad() {
        
        /**
         * Action for : Initialisation de la page Effets : - Initialisation de
         * la barre des mois - Selectionne le mois courant de l'année scolaire -
         * Crée le planning pour le mois selectionné
         */

        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();

        chargerTypeDevoir();

        final RechercheSeanceQO rechercheSeanceQO = new RechercheSeanceQO();
        rechercheSeanceQO.setIdEtablissement(utilisateurDTO
                .getIdEtablissement());
        if (Profil.ENSEIGNANT.equals(utilisateurDTO.getProfil())) {
            rechercheSeanceQO.setIdEnseignant(utilisateurDTO.getUserDTO()
                    .getIdentifiant());
        } else if (Profil.ELEVE.equals(utilisateurDTO.getProfil())
                || Profil.PARENT.equals(utilisateurDTO.getProfil())) {
            rechercheSeanceQO.setIdEleve(utilisateurDTO.getUserDTO()
                    .getIdentifiant());

            // Un parent avec plusieurs éléves a besoin de choisir un éléve.
            if (rechercheSeanceQO.getIdEleve() == null) {
                MessageUtils.addMessage(new Message("Il faut choisir un éléve",
                        Nature.BLOQUANT), getClass());
                return;
            }
        }
        form.setProfile(utilisateurDTO.getProfil());

        form.setRechercheSeanceQO(rechercheSeanceQO);

        final AnneeScolaireDTO anneeScolaireDTO = utilisateurDTO
                .getAnneeScolaireDTO();
        Date aujourdhui = DateUtils.getAujourdhui();
        if (aujourdhui.before(anneeScolaireDTO.getDateRentree())) {
            aujourdhui = anneeScolaireDTO.getDateRentree();
        } else if (aujourdhui.after(anneeScolaireDTO.getDateSortie())) {
            aujourdhui = anneeScolaireDTO.getDateSortie();
        }

        form.setListeBarreMois(GenerateurDTO.generateBarreMois(
                anneeScolaireDTO.getDateRentree(),
                anneeScolaireDTO.getDateSortie()));
        final BarreMoisDTO mois = GenerateurDTO.barreMoisCourant(
                form.getListeBarreMois(), aujourdhui);
        form.setMoisSelectionne(mois);

        // Si on est hors de l'année scolaire.
        if (form.getMoisSelectionne() == null) {
            MessageUtils.addMessage(new Message("La date actuelle est hors de l'année scolaire"
            , Nature.BLOQUANT),
                    getClass());
            return; 
        }

        // Gère navigation
        final BarreMoisDTO moisSelectionne = (BarreMoisDTO) ContexteUtils
                .getContexteOutilControl().recupererEtSupprimerObjet(
                        BarreMoisDTO.class.getName());

        if (moisSelectionne != null) {
            form.setMoisSelectionne(moisSelectionne);
        }

        createPlanningForMoisSelectionne();

        // récupération du fichier sélectionné depuis la popup de pièces
        // jointes.
        final FileUploadDTO fichierPopupPjointe = (FileUploadDTO) ContexteUtils
                .getContexteOutilControl().recupererEtSupprimerObjet(
                        PopupPiecesJointesControl.class.getName()
                                + Outil.CAHIER_MENSUEL.name());

        if (fichierPopupPjointe != null) {
            form.getDevoirSelected().getFiles().add(fichierPopupPjointe);
        }

    }
    
    /**
     * Action for : Selection de mois dans la barre des mois.
     * Effets : 
     * - Change la barre des mois pour selectionné le bon mois
     * - Crée le planning pour le mois selectionné
     */
    public void selectMois(){
        
        createPlanningForMoisSelectionne();
    }
    
    /**
     * Action for : Selection du mois suivant dans la barre des mois.
     * Effets : 
     * - Selectionne le mois suivant dans la barre des semaines.
     * - Crée le planning pour le mois selectionné
     */
    public void nextMois(){
        Boolean found = false;
        Boolean lastMois = true;
        for (BarreMoisDTO barreMoisDTO : form.getListeBarreMois()){
            if (found){
                lastMois = false;
                form.setMoisSelectionne(barreMoisDTO);
                selectMois();
                break;
            }
            found = barreMoisDTO.getDebutMois().equals(form.getMoisSelectionne().getDebutMois());
        }
        //Relancer la recherche sur le dernier mois pour actualiser le message d'erreur en cas d'absence de seance/devoir
        if (lastMois){
            selectMois();
        }
    }

    /**
     * Action for : Selection du mois précédent dans la barre des mois.
     * Effets : 
     * - Selectionne le mois précédent dans la barre des semaines.
     * - Crée le planning pour le mois selectionné
     */
    public void prevMois(){
        BarreMoisDTO precedent = null;
        for (BarreMoisDTO barreMoisDTO : form.getListeBarreMois()){
            if (barreMoisDTO.getDebutMois().equals(form.getMoisSelectionne().getDebutMois())){
                if (precedent != null){
                    form.setMoisSelectionne(precedent);
                } 
                //Relancer la recherche sur le premier mois également pour actualiser le message d'erreur en cas d'absence de seance/devoir
                selectMois();
                break;
            }
            precedent = barreMoisDTO;
        }
    }
    
    /**
     * Crée et remplit le planning une fois le mois selectionné.
     */
    public void createPlanningForMoisSelectionne(){
        form.setSemaines(GenerateurDTO.genererListeSemaineMois(form.getMoisSelectionne().getDebutMois()));
        
        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();
        

        List<TypeJour> listeJoursOuvre = GenerateurDTO
                .getListeJourOuvreFromDb(utilisateurDTO
                        .getJoursOuvresEtablissement());
        
        form.setListeJoursOuvre(listeJoursOuvre);
                
        if (! CollectionUtils.isEmpty(form.getSemaines())){
            rechercherSeances();
            rechercherDevoirs();
        }

        if (listeJoursOuvre.size() == 7) {
            return;
        }
        //Parcourt tout les devoirs pour vérifier qu'il n'y a pas de devoir à rendre pour des jours non ouvrés
        
        for (SemaineDTO semaine : form.getSemaines()) {
            for(TypeJour jour : TypeJour.values()) {
                JoursMensuelDTO jourM = semaine.getMap().get(jour.name()); 
                 
                jourM.setVraiOuFauxAfficherJour(listeJoursOuvre.contains(jour));

                if (!CollectionUtils.isEmpty(jourM.getDevoirs())) {
                    for(DevoirDTO devoir : jourM.getDevoirs()) {
                        int dayOfWeek = DateUtils.getChamp(devoir.getDateRemise(), Calendar.DAY_OF_WEEK);
                        if (!listeJoursOuvre.contains(TypeJour.getTypeJour(dayOfWeek))) {
                            listeJoursOuvre.add(TypeJour.getTypeJour(dayOfWeek));
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(jourM.getSeances())) {
                    
                    for(SeanceDTO seance : jourM.getSeances()) {
                        int dayOfWeek = DateUtils.getChamp(seance.getDate(), Calendar.DAY_OF_WEEK);
                        if (!listeJoursOuvre.contains(TypeJour.getTypeJour(dayOfWeek))) {
                            listeJoursOuvre.add(TypeJour.getTypeJour(dayOfWeek));
                        }
                    }
                }
                
            }
            
        }
        
        Collections.sort(listeJoursOuvre);
        
        form.setListeJoursOuvre(listeJoursOuvre);
    }
    
    /**
     * Recherche les seances pour le mois selectionné.
     * Suppose ques les semaines ne sont pas vides.
     */
    private void rechercherSeances(){
        try {
            final RechercheSeanceQO rechercheSeanceQO = form.getRechercheSeanceQO();
            rechercheSeanceQO.setDateDebut(form.getMoisSelectionne().getDebutMois());
            rechercheSeanceQO.setDateFin(form.getMoisSelectionne().getFinMois());
            
            final ContexteUtilisateur contextUtilisateur = ContexteUtils.getContexteUtilisateur();
            
            if (Profil.ENSEIGNANT.equals(contextUtilisateur.getUtilisateurDTO().getProfil())) {
                                
                rechercheSeanceQO.setIdEnseignantConnecte(
                        contextUtilisateur.getUtilisateurDTOConnecte().getUserDTO().getIdentifiant());
            }
            
            final ResultatDTO<List<SeanceDTO>> listeSeanceDTO =
                seanceService.findSeanceForPlanning(rechercheSeanceQO);
    
            final List<SeanceDTO> resultList =
                listeSeanceDTO.getValeurDTO();

            final Iterator<SemaineDTO> iterator = form.getSemaines().iterator();
            SemaineDTO semaineDTO = iterator.next();
            
            for (SeanceDTO seanceDTO : resultList){
                final Date jourSeance = seanceDTO.getDate();
                seanceDTO.setDescriptionHTML(ImagesServlet.genererLatexImage(seanceDTO.getDescription()));
                seanceDTO.setAnnotationsHTML(ImagesServlet.genererLatexImage(seanceDTO.getAnnotations()));

                log.info("seancedto hm {0} {1} debut", seanceDTO.getHeureDebut(), seanceDTO.getHeureFin(), seanceDTO.getIntitule());
                while ( ! DateUtils.isBetween(jourSeance, semaineDTO.getDebutSemaine(), semaineDTO.getFinSemaine())){
                    //La date n'est pas dans la semaine , on passe à la suivante.
                    if (iterator.hasNext()){
                        semaineDTO = iterator.next();
                    } else {
                        break;
                    }
                } 
                //Ici la semaine est la bonne, reste à trouver le bon jour
                for (JoursMensuelDTO jourMensuelDTO : semaineDTO.getMap().values()){
                    if (jourMensuelDTO.getDateJour().equals(jourSeance)){
                        jourMensuelDTO.getSeances().add(seanceDTO);

                        break;
                    }
                }
                if (seanceDTO.getDevoirs() != null) {
                    for (final DevoirDTO devoir : seanceDTO.getDevoirs()) {
                        devoir.setDescriptionHTML(ImagesServlet.genererLatexImage(devoir.getDescription()));
                    }
                }
            }
                
        } catch (MetierException e) {
            log.debug("{0}", e.getMessage());
        }
    }
    
    /**
     * Recherche les devoirs pour le mois selectionné.
     * Suppose ques les semaines ne sont pas vides.
     */
    private void rechercherDevoirs(){
        final ContexteUtilisateur contextUtilisateur = ContexteUtils.getContexteUtilisateur();
        
        try {
            final RechercheSeanceQO rechercheSeanceQO = form.getRechercheSeanceQO();
            rechercheSeanceQO.setDateDebut(form.getMoisSelectionne().getDebutMois());
            rechercheSeanceQO.setDateFin(form.getMoisSelectionne().getFinMois());
            
            rechercheSeanceQO.setIdEnseignantConnecte(
                    contextUtilisateur.getUtilisateurDTOConnecte().getUserDTO().getIdentifiant());
            
            final ResultatDTO<List<DevoirDTO>> listeDevoirDTO =
                devoirService.findDevoirForPlanning(rechercheSeanceQO);
    
            final List<DevoirDTO> resultList =
                listeDevoirDTO.getValeurDTO();

            final Iterator<SemaineDTO> iterator = form.getSemaines().iterator();
            SemaineDTO semaineDTO = iterator.next();
            
            for (DevoirDTO resultatRechercheDevoirDTO : resultList){
                final Date jourRemise = resultatRechercheDevoirDTO.getDateRemise();
                resultatRechercheDevoirDTO.setDescriptionHTML(ImagesServlet.genererLatexImage(resultatRechercheDevoirDTO.getDescription()));
                while ( ! DateUtils.isBetween(jourRemise, semaineDTO.getDebutSemaine(), semaineDTO.getFinSemaine())){
                    //La date n'est pas dans la semaine , on passe à la suivante.
                    if (iterator.hasNext()){
                        semaineDTO = iterator.next();
                    } else {
                        break;
                    }
                } 
                //Ici la semaine est la bonne, reste à trouver le bon jour
                for (JoursMensuelDTO jourMensuelDTO : semaineDTO.getMap().values()){
                    if (jourMensuelDTO.getDateJour().equals(jourRemise)){
                        jourMensuelDTO.getDevoirs().add(resultatRechercheDevoirDTO);
                        break;
                    }
                }
            }
                
        } catch (MetierException e) {
            log.debug("{0}", e.getMessage());
        }
    }
    
    /**
     *  Suppression du devoir depuis le devoir popup.
     */
    public void supprimerDevoir(){
        final DevoirDTO devoir = form.getDevoirSelected();
        form.setDevoirSelected(null);
        
        try {
            devoirService.deleteDevoir(devoir.getId());

            createPlanningForMoisSelectionne();
        } catch (final MetierException e) {
            log.debug("{0}", e.getMessage());
        }
        
    }
    
    /**
     * Modification du devoir au sein de la popup de modif des devoirs.
     */
    public void modifierDevoir(){
        final DevoirDTO devoir = form.getDevoirSelected();
        
        final SaveDevoirQO saveDevoirQO = new SaveDevoirQO();
        saveDevoirQO.setDevoirDTO(devoir);
        saveDevoirQO.setDateFinAnneeScolaire(ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getAnneeScolaireDTO().getDateSortie());
        saveDevoirQO.setDateSeance(devoir.getDateSeance());
        
        
        try {
            devoirService.saveDevoir(saveDevoirQO);
            //La modification a bien eu lieu, on ferme la popup. 
            //Il faut donc recharger les données du planning pour que le devoir apparaisse à la bonne date.
            createPlanningForMoisSelectionne();
            
        } catch (MetierException e) {
            log.debug("erreur de modification du devoir : {0}", e.getMessage());           
        }
    }
    
    /**
     * Action for : clic sur un devoir dans le planning.
     * Effet : 
     * -Charge les pieces jointes
     * -Recherche la charge de travail.
     */
    public void chargerDevoir(){
        //On utilise une copie du devoir dans la popup
        final DevoirDTO devoir = form.getDevoirSelected();
        final DevoirDTO devoirCopie = ObjectUtils.clone(devoir);
        form.setDevoirSelected(devoirCopie);
        
        chercherChargeTravail();
        
        chargerPJDevoir(devoirCopie);
        
    }
    
    /**
     * 
     */
    public void ajouterPieceJointeDevoir() {
    }
    
    /**
     * 
     */
    public void deletePieceJointeDevoir() {
        final DevoirDTO devoir = form.getDevoirSelected();
        if (devoir == null) { return; }
        final FileUploadDTO fichier = form.getPieceJointeASupprimer();
        if (fichier == null) { return; }
        if (!CollectionUtils.isEmpty(devoir.getFiles())) {
            devoir.getFiles().remove(fichier);
        }
    }

    /**
     * 
     */
    public void afterAjouterPieceJointeDevoir() {
        final FileUploadDTO file = popupPiecesJointes.getForm().getFileUploade();
        if (file != null) {
            final DevoirDTO devoir = form.getDevoirSelected();
            if (devoir != null) {
                if (CollectionUtils.isEmpty(devoir.getFiles())) {
                    devoir.setFiles(new ArrayList<FileUploadDTO>());
                }
                devoir.getFiles().add(file);
                //form.setDevoirSelected(null);
            }
        }
    }
    
    /**
     * Charge les PJ d'un devoir.
     * @param devoir le devoir dont on charge les PJ.
     */
    private void chargerPJDevoir(DevoirDTO devoir){
        final RecherchePieceJointeDevoirQO recherchePJDevoir = new RecherchePieceJointeDevoirQO();
        recherchePJDevoir.setIdDevoir(devoir.getId());
        
        try {
            final List<FileUploadDTO> pjs = devoirService.findPieceJointeDevoir(recherchePJDevoir );
            
            for (FileUploadDTO fileUploadDTO : pjs) {
                if (FileUploadUtils.checkExistencePieceJointe(fileUploadDTO)) {
                    fileUploadDTO.setActiverLien(true);
                }                        
            }
            
            devoir.setFiles(pjs);
        } catch (MetierException e) {
            log.debug("erreur de chargement des pieces joints du devoir : {0}", e.getMessage());       
        }
    }

    
    /**
     * Action for : clic sur une seance dans le planning.
     * Effet : 
     * -Charge les pieces jointes de la seance
     * -Charge les devoirs de la seance.
     */
    public void chargerSeance(){
        log.info("CHARGERSEANCE");
        
        // Lance la recherche et alimente dans le form de ajout seance la seance selectionnee.
        //=> la seance dans ajoutSeance.form.seance contient la seanceDTO à afficher dans la popup de detail.
        SeanceDTO seance = form.getSeanceSelectionne();
        final Integer idSeance = seance.getId();
        ajoutSeance.alimenterSeance(idSeance);
    }
    
    /**
     * Recherche la charge de travail pour le jour et le devoir selectionne.
     */
    public void chercherChargeTravail(){
        final DevoirDTO devoir = form.getDevoirSelected();
        
        if (devoir.getDateRemise() == null) {
            devoir.setChargeTravail(new ChargeTravailDTO());
            return;
        }
        
        final ChargeDevoirQO chargeDevoirQO = new ChargeDevoirQO();
        chargeDevoirQO.setDateDevoir(devoir.getDateRemise());
        chargeDevoirQO.setIdClasse(devoir.getIdClasse());
        chargeDevoirQO.setIdGroupe(devoir.getIdGroupe());
        chargeDevoirQO.setIdDevoir(devoir.getId());
        chargeDevoirQO.setIdEtablissement(ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getIdEtablissement());
        chargeDevoirQO.setUidEnseignant(ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO().getUid());

        final ChargeTravailDTO chargeTravail = devoirService.findChargeTravail(chargeDevoirQO);
        devoir.setChargeTravail(chargeTravail);
        
    }
    
    /**
     * Suppression de la piece jointe des devoirs.
     */
    public void supprimerPieceJointeDevoir(){
        log.debug("----------------- SUPPRESSION -----------------");    
        form.getDevoirSelected().getFiles().remove(form.getDevoirSelected().getPieceJointeSelectionne());
    }
    
    /**
     * Permet de charger la liste des types de devoir.
     */
    private void chargerTypeDevoir() {
        final ResultatDTO<List<TypeDevoirDTO>> resultat =
            devoirService.findListeTypeDevoir(ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getIdEtablissement());
        final List<TypeDevoirDTO> listeTypeDevoirDTO = resultat.getValeurDTO();
        
        form.setListeTypeDevoir(listeTypeDevoirDTO);
    }
    
    /**
     * Envoi un message de provenance des devoirs à la fenêtre modale de sélection de pièce jointe.
     */
    public void ouverturePopupPjDevoir() {
        ouverturePopupPj();
    }
    
    /**
     * Charge la popup de piece jointe.
     */
    private void ouverturePopupPj(){
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(PopupPiecesJointesControl.class.getName(), Outil.CAHIER_MENSUEL);
        popupPiecesJointes.loadTreeStockage();
    }

    /**
     * 
     */
    public void resetDevoir() {
        final DevoirDTO devoir = form.getDevoirSelected();
        if (devoir != null) {
            devoir.setIntitule(null);
            devoir.setDateRemise(null);
            devoir.setTypeDevoirDTO(form.getListeTypeDevoir().get(0));
            devoir.setDescription(null);
        }
    }
    
    public static final String CREER_SEANCE_DEPUIS_DEVOIR_CLE = "CREER_SEANCE_DEPUIS_DEVOIR_CLE"; 

    /**
     * @return navigation
     */
    public Object creerSeanceAPartirDevoir() {
        log.info("creerSeanceAPartirDevoir");
        
        SeanceDTO fictifSeance = new SeanceDTO();
        
        DevoirDTO devoir = form.getDevoirSelected();
        
        fictifSeance.setId(devoir.getIdSeance());
        fictifSeance.setDate(devoir.getDateRemise());
        fictifSeance.setDevoirs(new ArrayList<DevoirDTO>());
        
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(BarreMoisDTO.class.getName(), form.getMoisSelectionne());
        
        return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(
                Outil.AJOUT_SEANCE, CREER_SEANCE_DEPUIS_DEVOIR_CLE, fictifSeance);
        //return "AJOUT_SEANCE@menu@arbre";
    }
    
        
    /**
     * @return navigation
     */
    public Object naviguerVersSaisirSeance() {
        SemaineDTO semaine = form.getSemaineSelectionnee();
        
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(BarreMoisDTO.class.getName(), form.getMoisSelectionne());
        
        return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(
                Outil.SAISIR_EMP, SemaineDTO.class.getName(), semaine);
    }

    /**
     * Appelée lors de l'affichage de la popup de charge de travail.
     */
    public void afficherChargeTravail() {
    }

    /**
     * Charge sous forme d'image/html la description du devoir en cours.
     * Cette méthode est utilisée lors du basculement en vue visulation/modification 
     */
    public void chargeImagesLatexDevoir() {
        final DevoirDTO devoirDTO = form.getDevoirSelected();
        devoirDTO.setDescriptionHTML(ImagesServlet.genererLatexImage(devoirDTO.getDescription()));        
    }

    
}

