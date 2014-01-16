/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AjoutSeanceControl.java,v 1.44 2010/06/02 12:41:27 ent_breyton Exp $
 */

package org.crlr.web.application.control.seance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.DateDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.dto.application.emploi.RechercheEmploiQO;
import org.crlr.dto.application.seance.ConsulterSeanceQO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.dto.application.seance.ResultatRechercheSeanceDTO;
import org.crlr.dto.application.seance.SaveSeanceQO;
import org.crlr.dto.application.seance.TypeReglesSeance;
import org.crlr.dto.application.sequence.RechercheSequenceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.Message;
import org.crlr.services.DevoirService;
import org.crlr.services.EmploiService;
import org.crlr.services.ImagesServlet;
import org.crlr.services.SeanceService;
import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.crlr.utils.PropertiesUtils;
import org.crlr.web.application.control.AbstractPopupControl;
import org.crlr.web.application.control.PopupPiecesJointesControl;
import org.crlr.web.application.control.emploi.PlanningMensuelControl;
import org.crlr.web.application.control.remplacement.GestionRemplacementControl;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.application.form.seance.AjoutSeanceForm;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.utils.MessageUtils;
import org.crlr.web.utils.NavigationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AjoutSeanceControl.
 * 
 * @author $author$
 * @version $Revision: 1.44 $
 */
@ManagedBean(name = "ajoutSeance")
@ViewScoped
public class AjoutSeanceControl extends AbstractPopupControl<AjoutSeanceForm> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3869826187160906763L;

    /** Controleur de piece jointe. */
    @ManagedProperty(value = "#{popupPiecesJointes}")
    private transient PopupPiecesJointesControl popupPiecesJointesControl;

    /** Service Seance. */
    @ManagedProperty(value = "#{seanceService}")
    private transient SeanceService seanceService;

    /** Service Devoir. */
    @ManagedProperty(value = "#{devoirService}")
    private transient DevoirService devoirService;

    /** Service emploi du temps. */
    @ManagedProperty(value = "#{emploiService}")
    private transient EmploiService emploiService;
    
    @ManagedProperty(value = "#{gestionRemplacement}")
    private transient GestionRemplacementControl gestionRemplacementControl;

    protected static final Logger log = LoggerFactory.getLogger(AjoutSeanceControl.class);
    
    /**
     * Mutateur de popupPiecesJointesControl {@link #popupPiecesJointesControl}.
     * 
     * @param popupPiecesJointesControl
     *            le popupPiecesJointesControl to set
     */
    public void setPopupPiecesJointesControl(
            PopupPiecesJointesControl popupPiecesJointesControl) {
        this.popupPiecesJointesControl = popupPiecesJointesControl;
    }

    /**
     * Mutateur de seanceService {@link #seanceService}.
     * 
     * @param seanceService
     *            le seanceService to set
     */
    public void setSeanceService(SeanceService seanceService) {
        this.seanceService = seanceService;
    }

    /**
     * Mutateur de devoirService {@link #devoirService}.
     * 
     * @param devoirService
     *            le devoirService to set
     */
    public void setDevoirService(DevoirService devoirService) {
        this.devoirService = devoirService;
    }

    /**
     * Mutateur de emploiService {@link #emploiService}.
     * 
     * @param emploiService
     *            le emploiService to set
     */
    public void setEmploiService(EmploiService emploiService) {
        this.emploiService = emploiService;
    }

    /**
     * Constructeur.
     */
    public AjoutSeanceControl() {
        super(new AjoutSeanceForm());
    }

    private void chargerTypeDevoirs() {
        // Charge la liste des type de devoir configures pour
        // l'etablissement
        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();
        final List<TypeDevoirDTO> listeTypeDevoir = devoirService
                .findListeTypeDevoir(utilisateurDTO.getIdEtablissement())
                .getValeurDTO();
        form.setListeTypeDevoir(listeTypeDevoir);
    }
    
    private void initialiseNombreDevoirEtSeancePrecedente() {
     // Initialise le nombre de devoir et seance precedente par defaut
        // qu'il faut créer avec une seance
        final Properties properties = PropertiesUtils.load("/config.properties");
        final Integer nombreDevoirParDefaut;
        final Integer nombreSeancePrecedenteParDefaut;
        String valeurPropriete = properties.getProperty("devoir.count.default");
        if (valeurPropriete != null) {
            nombreDevoirParDefaut = Integer.parseInt(valeurPropriete);
        } else {
            nombreDevoirParDefaut = 1;
        }
        valeurPropriete = properties.getProperty("seance.precedente.count.default");
        if (valeurPropriete != null) {
            nombreSeancePrecedenteParDefaut = Integer.parseInt(valeurPropriete);
        } else {
            nombreSeancePrecedenteParDefaut = 1;
        }
        form.setNombreDevoirParDefaut(nombreDevoirParDefaut);
        form.setNombreSeancePrecedenteParDefaut(nombreSeancePrecedenteParDefaut);
    }
    
    /**
     * 
     * @return true si une séance a été prise en compte
     */
    private boolean gererSeanceDepuisEcranRechercheSeance() {
     // Modification d'une seance depuis la recherche de seance.
        final ResultatRechercheSeanceDTO resultatRechercheSeanceDTO = (ResultatRechercheSeanceDTO) ContexteUtils
                .getContexteOutilControl().recupererEtSupprimerObjet(
                        RechercheSeanceControl.class.getName());

        // Cas de duplication ou de modification
        if (resultatRechercheSeanceDTO == null) {
            return false;
        }
        
        form.setAfficheRetour(true);

        form.setResultatRechercheSeanceDTO(resultatRechercheSeanceDTO);
        alimenterSeance(resultatRechercheSeanceDTO.getId());

        // si on duplique, on vide le ID de la seance pour transformer en
        // ajout
        if (AbstractForm.MODE_DUPLICATE.equals(resultatRechercheSeanceDTO
                .getMode())) {
            form.getSeance().setId(null);
            form.getSeance().setVisaDirecteur(null);
            form.getSeance().setVisaInspecteur(null);
            form.getSeance().setAccesEcriture(true);
        }
        
        Integer enseignantId = form.getSeance().getIdEnseignant();
        Integer idUtilisateurConnecte = ContexteUtils.getContexteUtilisateur().getUtilisateurDTOConnecte().getUserDTO().getIdentifiant();
        
        if (enseignantId == null || !enseignantId.equals(idUtilisateurConnecte)) {
            log.debug("Effacter les annotations personnel du remplaçé en mode remplaçant");
            form.getSeance().setAnnotations("");
        }
        
        return true;
    
    }
    
    /**
     * 
     * @return true si une séance a été prise en compte
     */
    private boolean gererSeanceDepuisEcranPlanningDevoir() {
        final SeanceDTO ajouteSeanceDTO = (SeanceDTO) ContexteUtils
                .getContexteOutilControl().recupererEtSupprimerObjet(
                        PlanningMensuelControl.CREER_SEANCE_DEPUIS_DEVOIR_CLE);

        if (ajouteSeanceDTO == null) {
            return false;
        }
        
        form.setAfficheRetour(true);
        alimenterSeanceAjoute(ajouteSeanceDTO);
        return true;
            
        
    }
    
    /**
     * {@inheritDoc}
     */    
    @PostConstruct
    public void onLoad() {

        
        form.reset();

        chargerTypeDevoirs();

        initialiseNombreDevoirEtSeancePrecedente();
        
        completeListeDevoir(form.getSeance());

        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();
        
        if (Profil.ENSEIGNANT == utilisateurDTO.getProfil()) {
            // Charge les seance qui peuvent etre selectionne pour la date du
            // jour.
            final List<SequenceDTO> listeSequence = chercherSequenceActive(form.getSeance().getDate());
            form.setListeSequence(listeSequence);
        } else {
            log.info("Profil pas enseignant, ne cherche pas les séquences");
        }
       

        if (gererSeanceDepuisEcranRechercheSeance()) {
            return;
        }

        if (gererSeanceDepuisEcranPlanningDevoir()) {
            return;
        }
        
        
        //Une nouvelle séance a toujours l'accès d'écriture
        form.getSeance().setAccesEcriture(true);
        

        //BOUCHON
        //form.getSeance().setDate(DateUtils.creer(2012, 11, 15));
    }

    /**
     * Recherche les sequences actives correspondant a la date sélectionnée.
     * 
     * @param date
     *            : date de validité.
     * @return la liste des sequences active pour la date.
     */
    private List<SequenceDTO> chercherSequenceActive(final Date date) {

        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();
        final RechercheSequenceQO rechercheSequence = new RechercheSequenceQO();
        rechercheSequence.setDateDebut(date);
        rechercheSequence.setDateFin(date);
        rechercheSequence.setIdEnseignant(utilisateurDTO.getUserDTO().getIdentifiant());
        rechercheSequence.setIdEtablissement(utilisateurDTO.getIdEtablissement());

        // charge les sequence actives pour la semaine
        final List<SequenceDTO> listeSequence = this.emploiService.chercherSequenceSemaine(rechercheSequence);

        // Supprime le champ description qui n'est pas utilise et qui pose des
        // pb lors du JSON parse s'il contient des retour chariot (#13)
        for (final SequenceDTO sequence : listeSequence) {
            sequence.setDescription(null);
        }
        return listeSequence;
    }

    /**
     * Retourne les sequences qui correspondent à la case agenda : Enseignement
     * + Classe/groupe identiques + Date incluse.
     * 
     * @param seance
     *            la seance testee
     * @param listeSequences liste
     * @return une nouvelle la liste des sequences.
     */
    public static List<SequenceDTO> getListeSequenceCorrespondantSeance(
            final SeanceDTO seance, List<SequenceDTO> listeSequences) {
        final List<SequenceDTO> listeSequenceARetour = new ArrayList<SequenceDTO>();
        
        if (seance == null) {
            log.warn("Seance null");
            return listeSequenceARetour;
        }
        
        final Date seanceDateJour = 
                org.apache.commons.lang.time.DateUtils.truncate(seance.getDate(), Calendar.DAY_OF_MONTH);   
        //final List<SequenceDTO> listeSequence = chercherSequenceActive(seance.getDate());
        //form.setListeSequence(listeSequence);
        
        for (final SequenceDTO sequence : listeSequences) {
            

            // Les dates ne correspondent pas
            if (seanceDateJour.before(sequence.getDateDebut())
                    || seanceDateJour.after(sequence.getDateFin())) {
                continue;
            }

            if (seance.getSequence() != null && seance.getSequence().getId() != null) {
                // Matiere differentes
                if (seance.getSequence() != null && seance.getSequence().getIdEnseignement()!= null && 
                        !seance.getSequence().getIdEnseignement().equals(sequence.getIdEnseignement())) {
                    continue;
                }
                // Type Groupe / Classe different
                if (seance.getSequence() != null &&  !seance.getTypeGroupe().equals(sequence.getGroupesClassesDTO().getTypeGroupe())) {
                    continue;
                }
                // Classe / groupe id differente
                if (seance.getSequence() != null && seance.getSequenceDTO().getGroupesClassesDTO().getId() != null
                        && !
                        (seance.getSequenceDTO().getGroupesClassesDTO().getId().equals
                                (sequence.getGroupesClassesDTO().getId()))) {
                    continue;
                }
            }
            
            // Tout correspond a la case EDT, on ajoute la sequence
            listeSequenceARetour.add(sequence);
        }
        return listeSequenceARetour;
    }

    /**
     * Methode appelee lors d'un changement de date de seance. Rafraichit la
     * liste des sequence actives à cette date. Repositionne eventuellement la
     * sequence active et les champs classe/matiere dans la seance.
     */
    public void changeDateSeance() {
        
        final List<SequenceDTO> listeSequence = chercherSequenceActive(form.getSeance().getDate());
        form.setListeSequence(listeSequence);
        
        form.setListeSequenceSeance(getListeSequenceCorrespondantSeance(form
                .getSeance(), listeSequence));

        // La sequence selectionnee n'existe pas dans les sequences actives
        if (form.getSeance().getSequence() == null
                || form.getListeSequenceSeance().indexOf(
                        form.getSeance().getSequence()) < 0) {
            if (form.getListeSequenceSeance().size() > 0) {

                // Selectionne la premiere sequence par defaut
                final SequenceDTO sequence = form.getListeSequenceSeance().get(
                        0);
                form.getSeance().setSequenceDTO(sequence);
            }
        }

        // Charge les seance precedente
        form.setListeSeancePrecedente(chargerListeSeancePrecedente(form.getSeance()));
        form.setAfficheSuiteSeancePrecedente(form.getListeSeancePrecedente().size()>0);
        
        // Charge la liste des date proposees pour la remise d'un devoir
        form.setListeDateRemiseDevoir(chargerListeDateRemiseDevoir(form.getSeance()));
        
                
        
    }

    /**
     * Alimente tout ce qui faut dans le form pour la modification d'une seance
     * existante.
     * 
     * @param idSeance
     *            id de la seance.
     */
    public void alimenterSeance(final Integer idSeance) {

        // Charge les info de la seance selectionnee et la positionne dans la
        // case selectionnee
        final UtilisateurDTO utilisateur = ContexteUtils.getContexteUtilisateur().getUtilisateurDTOConnecte();
        final SeanceDTO seance = chargerConsulterSeanceDTO(idSeance);
       
        // Ce partie complémentaire n'est pas fait si on en en mode archive ou pas enseignant 
        if (BooleanUtils.isNotTrue(form.getModeArchive()) && Profil.ENSEIGNANT.equals(utilisateur.getProfil())) {
            final List<SequenceDTO> listeSequence = chercherSequenceActive(seance.getDate());
            form.setListeSequence(listeSequence);
            
            
            
            // charge la liste des seance precedente et les positionne dans le form
            if (Profil.ENSEIGNANT.equals(utilisateur.getProfil())) {
                form.setListeSeancePrecedente(chargerListeSeancePrecedente(seance));
                form.setAfficheSuiteSeancePrecedente(form.getListeSeancePrecedente().size()>0);
            }
    
            // Charge la liste des date proposees pour la remise d'un devoir
            form.setListeDateRemiseDevoir(chargerListeDateRemiseDevoir(seance));
    
            // Prepare le minimum de devoirs affichés
            if (Profil.ENSEIGNANT == utilisateur.getProfil()
                    && utilisateur.getUserDTO().getIdentifiant().equals(seance.getEnseignantDTO().getId())) {
                completeListeDevoir(seance);
            }
    
            // Charge les sequence qui peuvent etre utilisee pour la seance. */
            form.setListeSequenceSeance(getListeSequenceCorrespondantSeance(seance, listeSequence));
        }
       
        // Positionne la seance dans le form
        form.setSeance(ObjectUtils.clone(seance));
        
        //Pour que l'IHM est desactivé dans le cas où un remplaçant n'a pas le droit d'accés
        gestionRemplacementControl.getForm().setDateEffet(form.getSeance().getDate());
    }

    /**
     * Pre-alimentation des champs pour créer une séance à partir d'un autre
     * écran (comme planningMensuel).
     * 
     * @param seancePropose
     *            séance
     */
    private void alimenterSeanceAjoute(SeanceDTO seancePropose) {

        alimenterSeance(seancePropose.getId());

        final SeanceDTO seance = form.getSeance();

        seance.setId(null);

        // Utilise des valeurs si ils éxistent
        if (seancePropose.getDate() != null) {
            seance.setDate(seancePropose.getDate());
        }

        if (seancePropose.getDevoirs() != null) {
            seance.setDevoirs(seancePropose.getDevoirs());
        }

        // Positionne la seance dans le form
        form.setSeance(seance);
    }

    /**
     * Complete l'objet SeanceDTO avec les infos issu de la base de donnees.
     * 
     * @param idSeance
     *            : l'id seance
     * @return l'objet SeanceDTO cree
     */
    private SeanceDTO chargerConsulterSeanceDTO(final Integer idSeance) {
        
        final ConsulterSeanceQO consulterSeanceQO = new ConsulterSeanceQO();
        consulterSeanceQO.setId(idSeance);
        consulterSeanceQO.setArchive(form.getModeArchive());
        consulterSeanceQO.setExercice(form.getExercice());
        
        final ContexteUtilisateur contextUtilisateur = ContexteUtils.getContexteUtilisateur();
        
        consulterSeanceQO.setIdEnseignantConnecte(
                contextUtilisateur.getUtilisateurDTOConnecte().getUserDTO().getIdentifiant());
        
        
        if (BooleanUtils.isNotTrue(form.getModeArchive())) {
            consulterSeanceQO.setAvecInfoVisa(true);
        }
        
        try {
            final SeanceDTO consulterSeanceDTO = seanceService
                    .rechercherSeance(consulterSeanceQO);

            chargerSeanceInfoSupplementaire(consulterSeanceDTO);
            
            return consulterSeanceDTO;
        } catch (final MetierException e) {
            log.debug("{0}", e.getMessage());
            return null;
        }
    }

    /**
     * Complete l'objet SeanceDTO avec les infos issu de la base de donnees.
     * 
     * @param consulterSeanceDTO
     *            l'objet consulter seance qui a ete retournee par le service
     *            metier.
     */
    private void chargerSeanceInfoSupplementaire(
            final SeanceDTO consulterSeanceDTO) {
        
        

        // Traitement de la liste des devoirs :
        // - la description ne doit pas contenir les tags html
        // - la description doit être reduite a 50 caractères
        final List<DevoirDTO> listeDevoirs = consulterSeanceDTO
                .getDevoirs();

        if (!CollectionUtils.isEmpty(listeDevoirs)) {
            for (final DevoirDTO devoirDTO : listeDevoirs) {
                devoirDTO.generateDescriptionAbrege();
                
                // Charge de travail prévue pour le meme jour
                devoirService.chercherChargeTravailGenerique(devoirDTO, ContexteUtils
                        .getContexteUtilisateur().getUtilisateurDTO());                
            }
        }
        
    }

    /**
     * Declenchee quand l'utilisateur veut charger la suite des seances precedentes.
     */
    public void chargerSuiteSeancePrecendente() {
        if (form.getListeSeancePrecedente() == null || form.getListeSeancePrecedente().size()==0) {
            return;
        }
        // Recupere la seance précédente la plus ancienne dans la liste des seance precédentes.
        final SeanceDTO seance = form.getListeSeancePrecedente().get(form.getListeSeancePrecedente().size()-1);
        final List<SeanceDTO> listeSuite = chargerListeSeancePrecedente(seance);
        form.getListeSeancePrecedente().addAll(listeSuite);
        form.setAfficheSuiteSeancePrecedente(listeSuite.size()>0);
    }

    /**
     * Charge les seances qui precedent la seances avec le meme info sequence.
     * 
     * @param seance la seance qui a ete chargee
     * @return la liste des sance precedent la seance.
     */
    private List<SeanceDTO> chargerListeSeancePrecedente(final SeanceDTO seance) {

        SequenceDTO sequence = seance.getSequence();
        
        if (sequence == null) {
            return new ArrayList<SeanceDTO>();
        } else {
        
            // Charge la liste des seances precedentes
            final RechercheSeanceQO rechercheSeanceQO = new RechercheSeanceQO();
            final UtilisateurDTO utilisateurDTO = ContexteUtils
                    .getContexteUtilisateur().getUtilisateurDTO();
            
            rechercheSeanceQO.setIdEnseignantConnecte(ContexteUtils
                    .getContexteUtilisateur().getUtilisateurDTOConnecte().getUserDTO().getIdentifiant());
            
            rechercheSeanceQO.setTypeGroupe(sequence.getTypeGroupe());
            rechercheSeanceQO.setIdClasseGroupe(sequence.getIdClasseGroupe());
            rechercheSeanceQO.setIdEnseignant(utilisateurDTO.getUserDTO()
                    .getIdentifiant());
            rechercheSeanceQO.setIdEnseignement(sequence.getIdEnseignement());
            rechercheSeanceQO.setDateDebut(seance.getDate());
            rechercheSeanceQO.setHeureDebut(seance.getHeureDebut());
            rechercheSeanceQO.setMinuteDebut(seance.getMinuteDebut());
            rechercheSeanceQO.setIdSequence(sequence.getId());
            rechercheSeanceQO.setIdSeance(seance.getId());
            rechercheSeanceQO.setIdEtablissement(utilisateurDTO
                    .getIdEtablissement());
            List<SeanceDTO> listeSeancePrecedente;
            try {
                listeSeancePrecedente = seanceService
                        .chercherListeSeancePrecedente(rechercheSeanceQO, form
                                .getNombreSeancePrecedenteParDefaut());
            } catch (MetierException e) {
                listeSeancePrecedente = new ArrayList<SeanceDTO>();
            }
            return listeSeancePrecedente;
        }
    }

    /**
     * Détermine les dates proposées pour les remises de devoir.
     * @param seance : la seance
     * @return une liste de date
     */
    private List<DateDTO> chargerListeDateRemiseDevoir(final SeanceDTO seance) {
        
        final SequenceDTO sequence = seance.getSequence();
        
        List<DateDTO> listeDateRemiseDevoir = new ArrayList<DateDTO>();
        if (sequence == null) {
            return listeDateRemiseDevoir; 
        } 
        
        // Charge la liste des prochaines dates proposées pour la remise des
        // devoirs
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        final RechercheEmploiQO rechercheEmploi = new RechercheEmploiQO();
        rechercheEmploi.setDateDebut(DateUtils.getDatePlus1(seance.getDate()));
        rechercheEmploi.setDateFin(utilisateurDTO.getAnneeScolaireDTO().getDateSortie());
        rechercheEmploi.setIdEnseignant(sequence.getIdEnseignant());
        rechercheEmploi.setIdEtablissement(utilisateurDTO.getIdEtablissement());
        rechercheEmploi.setIdEnseignement(seance.getSequence().getIdEnseignement());
        rechercheEmploi.setIdGroupeOuClasse(sequence.getIdClasseGroupe());
        rechercheEmploi.setVraiOuFauxClasse(TypeGroupe.CLASSE.equals(sequence
                .getTypeGroupe()));
        listeDateRemiseDevoir = emploiService
                .findProchaineDate(rechercheEmploi);
    
        return listeDateRemiseDevoir;
    }

    /**
     * Ajoute sur la seance des devoirs vides si necessaire.
     * 
     * @param seance
     *            la seance a compléter
     */
    public void completeListeDevoir(final SeanceDTO seance) {

        // Cree la liste si null
        if (seance.getDevoirs() == null) {
            seance.setDevoirs(new ArrayList<DevoirDTO>());
        }

        // Ajoute des devoir vides si pas assez de devoir rempli
        final int nbrManquant = form.getNombreDevoirParDefaut()
                - seance.getDevoirs().size();
        for (int i = 0; i < nbrManquant; i++) {
            final DevoirDTO devoir = new DevoirDTO();
            devoir.setIdClasse(seance.getIdClasse());
            devoir.setIdGroupe(seance.getIdGroupe());
            seance.getDevoirs().add(devoir);
        }
    }

    /**
     * Methode appelee lors de la sauvegarde d'une seance en cours d'edition .
     */
    public void sauver() {
        final SeanceDTO seance = form.getSeance();
        
        
        if (seance == null) {
            log.error("Problème : séance est null");
            return;
        }
        
        if (seance.getSequence() == null) {
            MessageUtils.addMessage(new Message(TypeReglesSeance.SEANCE_20.name()), this.getClass());
            form.setListeChampsObligatoire(new HashSet<String>(org.crlr.utils.CollectionUtils.creerList(TypeReglesSeance.SEANCE_20.name())));
            return;
        } else {        
            form.resetChampsObligatoire();
        }
        
        if (seance.getSequence() == null) {
            MessageUtils.addMessage(new Message(TypeReglesSeance.SEANCE_20.name()), this.getClass());
            form.setListeChampsObligatoire(new HashSet<String>(org.crlr.utils.CollectionUtils.creerList(TypeReglesSeance.SEANCE_20.name())));
        }
                
        if (seance.getId() == null) {
            ajouterSeance(seance);
        } else {
            modifierSeance(seance);
        }
    }
    
    
   

    /**
     * Methode appelee lors de la sauvegarde d'une seance en cours d'edition.
     * 
     * @throws MetierException
     *             l'exception
     */
    public void sauverSeancePrecedente() throws MetierException {
        SeanceDTO seance = form.getSeancePrecedente();
        if (seance == null
                || form.getSequenceSeancePrecedenteSelected() == null) {
            return;
        }
        
        seance.setSequenceDTO(form.getSequenceSeancePrecedenteSelected());
        
        modifierSeance(seance);

        //Charge ce qui est vraiment dans la base, aussi avec les info supplémentaires (comme la déscriptionHTML)
        seance = chargerConsulterSeanceDTO(seance.getId());
        
        // Met a jour la seance precedente dans la liste des seances
        // precedentes
        final List<SeanceDTO> listeSeance = form.getListeSeancePrecedente();
                   
        for (int i = 0; i < listeSeance.size(); i++) {
            final SeanceDTO seancetmp = listeSeance.get(i);
            if (seancetmp.getId().equals(seance.getId())) {
                listeSeance.remove(i);
                listeSeance.add(i, seance);
                break;
            }
        }
        form.setListeSeancePrecedente(listeSeance);
        return;
    }

    /**
     * Construit un objet SaveSeanceQO a partir d'un SeanceDTO.
     * @param seance le seance DTO
     * @return un SaveSeanceQO
     */
    public SaveSeanceQO construitAjoutSaveSeanceQO(final SeanceDTO seance) {
        final SequenceDTO sequence = seance.getSequence();
        
        final SaveSeanceQO saveSeanceQO = new SaveSeanceQO();
        saveSeanceQO.setCodeSequence(sequence.getCode());
        saveSeanceQO.setDate(seance.getDate());
        saveSeanceQO.setDescription(seance.getDescription());

        saveSeanceQO.setHeureDebut(seance.getHeureDebut() < 0 ? null : seance
                .getHeureDebut());
        saveSeanceQO.setMinuteDebut(seance.getMinuteDebut() < 0 ? null : seance
                .getMinuteDebut());
        saveSeanceQO.setHeureFin(seance.getHeureFin() < 0 ? null : seance
                .getHeureFin());
        saveSeanceQO.setMinuteFin(seance.getMinuteFin() < 0 ? null : seance
                .getMinuteFin());

        if (org.apache.commons.lang.StringUtils.isEmpty(seance.getIntitule())) {
            saveSeanceQO.setIntitule("Séance "
                    + StringUtils.substring(sequence.getDesignationEnseignement(),0, 25) 
                    + " du " + DateUtils.format(seance.getDate(), "dd/MM/yyyy"));
        } else {
            saveSeanceQO.setIntitule(seance.getIntitule());
        }
        saveSeanceQO.setAnnotations(seance.getAnnotations());
        saveSeanceQO.setMode(AbstractForm.MODE_AJOUT);

        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTOConnecte();
        
        saveSeanceQO.setIdEnseignant(utilisateurDTO.getUserDTO().getIdentifiant());
        
        
        
        saveSeanceQO.setAnneeScolaireDTO(utilisateurDTO.getAnneeScolaireDTO());
        saveSeanceQO.setListeDevoirDTO(retirerDevoirVide(seance.getDevoirs()));
        saveSeanceQO.setListeFichierJointDTO(seance.getFiles());
        
        return saveSeanceQO;
    }
    
    /**
     * Enregistre une nouvelle seance.
     * 
     * @param seance
     *            la seance.     
     */
    private void ajouterSeance(final SeanceDTO seance) {
        final SaveSeanceQO saveSeanceQO = construitAjoutSaveSeanceQO(seance);
        try {
            final ResultatDTO<Integer> resultatDTO = this.seanceService
                    .saveSeance(saveSeanceQO);
            final Integer idSeance = resultatDTO.getValeurDTO();
            seance.setId(idSeance);
            seance.setIntitule(saveSeanceQO.getIntitule());
            log.debug("Creation de la seance id = {0}", idSeance);
            form.resetChampsObligatoire();
        } catch (final MetierException e) {
            form.setListeChampsObligatoire(MessageUtils.getAllCodeMessage(e.getConteneurMessage()));
            log.debug("{0}", e.getMessage());
        }
    }

    /**
     * Enregistre une seance existante.
     * 
     * @param seance
     *            la seance
     */
    private void modifierSeance(final SeanceDTO seance) {
        
        final SequenceDTO sequence = seance.getSequence();
        
        final SeanceDTO saveSeanceQO = new SeanceDTO();
        saveSeanceQO.getSequenceDTO().setCode(sequence.getCode());
        saveSeanceQO.setDate(seance.getDate());
        saveSeanceQO.setDescription(seance.getDescription());

        saveSeanceQO.setHeureDebut(seance.getHeureDebut() < 0 ? null : seance
                .getHeureDebut());
        saveSeanceQO.setMinuteDebut(seance.getMinuteDebut() < 0 ? null : seance
                .getMinuteDebut());
        saveSeanceQO.setHeureFin(seance.getHeureFin() < 0 ? null : seance
                .getHeureFin());
        saveSeanceQO.setMinuteFin(seance.getMinuteFin() < 0 ? null : seance
                .getMinuteFin());

        if (org.apache.commons.lang.StringUtils.isEmpty(seance.getIntitule())) {
            saveSeanceQO.setIntitule("Séance "
                    + org.crlr.utils.StringUtils.truncate(sequence.getDesignationEnseignement(),25) + " du "
                    + DateUtils.format(seance.getDate(), "dd/MM/yyyy"));
        } else {
            saveSeanceQO.setIntitule(seance.getIntitule());
        }

        saveSeanceQO.setAnnotations(seance.getAnnotations());
        
        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTOConnecte();
        saveSeanceQO.setIdEnseignant(utilisateurDTO.getUserDTO()
                .getIdentifiant());
        saveSeanceQO.setListeDevoirDTO(retirerDevoirVide(seance.getDevoirs()));
        saveSeanceQO.setListeFichierJointDTO(seance.getFiles());
        
        try {
            saveSeanceQO.setId(seance.getId());
            final ResultatDTO<Integer> resultatDTO = this.seanceService
                    .modifieSeance(saveSeanceQO);
            seance.setIntitule(saveSeanceQO.getIntitule());
            log.debug("Modification de la seance result = {}", resultatDTO
                    .getValeurDTO());
            form.resetChampsObligatoire();
        } catch (final MetierException e) {
            form.setListeChampsObligatoire(MessageUtils.getAllCodeMessage(e.getConteneurMessage()));
            log.debug("{}", e);
        }
    }

    /**
     * Retrourne la liste des devoirs sans les devoirs vides.
     * 
     * @param listeDevoir
     *            la liste avec des devoirs vide
     * @return une liste avec que des devoirs remplis.
     */
    private List<DevoirDTO> retirerDevoirVide(final List<DevoirDTO> listeDevoir) {
        final List<DevoirDTO> listeResultat = new ArrayList<DevoirDTO>();
        if (!CollectionUtils.isEmpty(listeDevoir)) {
            for (final DevoirDTO devoir : listeDevoir) {

                // Verifie que le devoir n'est pas vide
                if (devoir.getDateRemise() == null
                        && org.apache.commons.lang.StringUtils.trimToNull(devoir.getIntitule()) == null
                        && org.apache.commons.lang.StringUtils.trimToNull(devoir.getDescription()) == null
                        && org.apache.commons.collections.CollectionUtils
                                .isEmpty(devoir.getFiles())) {
                    continue;
                }
                listeResultat.add(devoir);
            }
        }
        return listeResultat;
    }

    /**
     * Methode appelée lors du copier d'une seance.
     */
    public void copier() {
        final SeanceDTO seanceSrc = form.getSeance();
        if (seanceSrc != null) {
            final SeanceDTO seanceNew = new SeanceDTO();
            
            seanceNew.copieFrom(seanceSrc);
            
            //Pour faire le contrôl de remplaçement / annotations, il nous faut l'id d'enseinant
            seanceNew.setIdEnseignant(seanceSrc.getIdEnseignant());
            
            form.setSeanceCopie(seanceNew);
        }
    }

    /**
     * Methode appelée lors du coller d'une seance initialement copiée sur une
     * autre case.
     */
    public void coller() {
        final SeanceDTO seanceCopie = form.getSeanceCopie();
        final SeanceDTO seanceSelected = form.getSeance();
        if (seanceCopie != null && seanceSelected != null) {
            seanceSelected.copieFrom(seanceCopie);
        }
    }

    /**
     * Reinit le devoir selected.
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

    /**
     * Reinit la seance.
     * 
     * @param seance
     *            la seance
     */
    private void resetSeanceGenerique(final SeanceDTO seance) {
        if (seance != null) {
            seance.setIntitule(null);
            seance.setDescription(null);
            seance.setAnnotations(null);
            seance.setFiles(new ArrayList<FileUploadDTO>());
        }
    }

    /**
     * Reinit la seance en cours d'edition.
     */
    public void resetSeance() {
        resetSeanceGenerique(form.getSeance());
    }

    /**
     * Reinit la seance precedente en cours d'edition.
     */
    public void resetSeancePrecedente() {
        resetSeanceGenerique(form.getSeancePrecedente());
    }

    /**
     * Reset les annotations personnelles sur la seance.
     * 
     * @param seance
     *            la seance
     */
    private void resetSeanceAnnotationsGenerique(final SeanceDTO seance) {
        if (seance != null) {
            seance.setAnnotations(null);
        }
    }

    /**
     * Reset les annotations personnelles sur la seance.
     */
    public void resetSeanceAnnotations() {
        resetSeanceAnnotationsGenerique(form.getSeance());
    }

    /**
     * Reset les annotations personnelles sur la seance precedente.
     */
    public void resetSeanceAnnotationsPrecedente() {
        resetSeanceAnnotationsGenerique(form.getSeancePrecedente());
    }

    /**
     * Supprime de la liste le devoir selected. La suppression en base n'est pas
     * faite à ce moment.
     * 
     * @param seance
     *            la seance.
     */
    private void deleteDevoirGenerique(final SeanceDTO seance) {
        if (seance == null) {
            return;
        }
        final DevoirDTO devoir = form.getDevoirSelected();
        if (devoir == null) {
            return;
        }
        seance.getDevoirs().remove(devoir);
    }

    /**
     * Supprime de la liste le devoir selected. La suppression en base n'est pas
     * faite à ce moment.
     */
    public void deleteDevoir() {
        deleteDevoirGenerique(form.getSeance());
    }

    /**
     * Supprime de la liste le devoir selected. La suppression en base n'est pas
     * faite à ce moment.
     */
    public void deleteDevoirSeancePrecedente() {
        deleteDevoirGenerique(form.getSeancePrecedente());
    }

    /**
     * Supprime la piece jointe selected du devoir selected.
     */
    public void deletePieceJointe() {
        final DevoirDTO devoir = form.getDevoirSelected();
        if (devoir == null) {
            return;
        }
        final FileUploadDTO fichier = form.getPieceJointeASupprimer();
        if (fichier == null) {
            return;
        }
        if (!CollectionUtils.isEmpty(devoir.getFiles())) {
            devoir.getFiles().remove(fichier);
        }
    }

    /**
     * Supprime la piece jointe selected de la seance en cours d'edition.
     * 
     * @param seance
     *            la seance
     */
    private void deletePieceJointeSeanceGenerique(final SeanceDTO seance) {
        if (seance == null) {
            return;
        }
        final FileUploadDTO fichier = form.getPieceJointeASupprimer();
        if (fichier == null) {
            return;
        }
        if (!CollectionUtils.isEmpty(seance.getFiles())) {
            seance.getFiles().remove(fichier);
        }
    }

    /**
     * Supprime la piece jointe selected de la seance en cours d'edition.
     */
    public void deletePieceJointeSeance() {
        deletePieceJointeSeanceGenerique(form.getSeance());
    }

    /**
     * Supprime la piece jointe selected de la seance precedente en cours
     * d'edition.
     */
    public void deletePieceJointeSeancePrecedente() {
        deletePieceJointeSeanceGenerique(form.getSeancePrecedente());
    }

    /**
     * Ajoute une nouvelle piece jointe sur le devoir selected. Cette méthode
     * est appelée lors du clic sur l'icone d'affichage de la popup piece
     * jointe.
     */
    public void ajouterPieceJointe() {
        // A a priori, au moment de cliquer sur le lien, il n'y a rien a faire.
        log.info("ajouterPieceJointe. raffraichirTabAfterUpload : {0}",
                form.getRaffraichirTabAfterUpload());

    }

    /**
     * Cette méthode est appellee par la popup piece jointe suite à un upload de
     * fichier.
     */
    public void afterAjouterPieceJointe() {
        final FileUploadDTO file = popupPiecesJointesControl.getForm()
                .getFileUploade();
        if (file != null) {
            final DevoirDTO devoir = form.getDevoirSelected();
            if (devoir != null) {
                if (CollectionUtils.isEmpty(devoir.getFiles())) {
                    devoir.setFiles(new ArrayList<FileUploadDTO>());
                }
                devoir.getFiles().add(file);
                form.setDevoirSelected(null);
            }
        }
    }

    
    
    /**
     * Ajoute une nouvelle piece jointe. Cette méthode est appelée lors du clic
     * sur l'icone d'affichage de la popup piece jointe.
     */
    public void ajouterPieceJointeSeance() {
        // A a priori, au momen de cliquer sur le lien, il n'y a rien a faire.
        final String raffraichirTabAfterUpload = form
                .getRaffraichirTabAfterUpload();
        if (raffraichirTabAfterUpload != null) {
            log
                    .debug("Déclenche l'ouverture de la popup des pieces jointe pour seance");
        }
    }

    /**
     * Cette méthode es appellee par la popup piece jointe suite à un upload de
     * fichier.
     * 
     * @param seance
     *            la seance
     */
    private void afterAjouterPieceJointeSeanceGenerique(final SeanceDTO seance) {
        final FileUploadDTO file = popupPiecesJointesControl.getForm()
                .getFileUploade();
        if (file != null) {
            if (seance != null) {
                if (CollectionUtils.isEmpty(seance.getFiles())) {
                    seance.setFiles(new ArrayList<FileUploadDTO>());
                }
                seance.getFiles().add(file);
            }
        }
    }

    /**
     * Cette méthode es appellee par la popup piece jointe suite à un upload de
     * fichier.
     */
    public void afterAjouterPieceJointeSeance() {
        afterAjouterPieceJointeSeanceGenerique(form.getSeance());
    }

    /**
     * Cette méthode es appellee par la popup piece jointe suite à un upload de
     * fichier.
     */
    public void afterAjouterPieceJointeSeancePrecedente() {
        afterAjouterPieceJointeSeanceGenerique(form.getSeancePrecedente());
    }

    /**
     * Cette methode est appele lors de l'ajout d'un devoir en cliquant sur le
     * lien d'ajout du panel des devoirs.
     * 
     * @param seance
     *            la seance
     */
    private void ajouterDevoirGenerique(final SeanceDTO seance) {
        if (seance != null) {
            final DevoirDTO devoir = new DevoirDTO();
            if (CollectionUtils.isEmpty(seance.getDevoirs())) {
                seance.setDevoirs(new ArrayList<DevoirDTO>());
            }
            seance.getDevoirs().add(devoir);
        }
    }

    /**
     * Cette methode est appele lors de l'ajout d'un devoir en cliquant sur le
     * lien d'ajout du panel des devoirs.
     */
    public void ajouterDevoir() {
        ajouterDevoirGenerique(form.getSeance());
    }

    /**
     * Cette methode est appele lors de l'ajout d'un devoir en cliquant sur le
     * lien d'ajout du panel des devoirs.
     */
    public void ajouterDevoirSeancePrecedente() {
        ajouterDevoirGenerique(form.getSeancePrecedente());
    }

    /**
     * Methode invoquee quand on veut modifier une seance precedente.
     */
    public void modifierSeancePrecedente() {
        // Recupere l'objet seance prec qui a ete selectionnee dans la liste des
        // seances precedentes.
        final Integer idSeance = form.getSeancePrecedente().getId();

        // Recharge l'objet seance de la base de donnees et la positionne dans
        // le form
        final SeanceDTO seance = chargerConsulterSeanceDTO(idSeance);
        if (seance == null) {
            return;
        }
        
        form.setSeancePrecedente(seance);

        // Recherche les sequence pouvant etre selectionnees pour la seance
        final List<SequenceDTO> listeSequence = getListeSequenceCorrespondantSeance(seance, form.getListeSequence());
        form.setListeSequenceSeancePrecedente(listeSequence);

        // Recherche l'objet Sequence correspondant à la seance
        form.setSequenceSeancePrecedenteSelected(seance.getSequence());

        // Charge la liste des date proposees pour la remise d'un devoir
        form.setListeDateRemiseDevoirSeancePrecedente(chargerListeDateRemiseDevoir(seance));
    
    }

    /**
     * Verifie la charge de travail prévue pour le devoir en cours d'edition.
     */
    public void chercherChargeTravail() {
        devoirService.chercherChargeTravailGenerique(form.getDevoirSelected(), ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO());
    }

    /**
     * Appelée lors de l'affichage de la popup de charge de travail.
     */
    public void afficherChargeTravail() {
    }

    /**
     * Cette méthode est apprlée lors de la sélection d'une sequence dans la
     * saisie d'une seance precedente.
     */
    public void chargerSequenceSeancePrecedente() {
        form.getSequenceSeancePrecedenteSelected();
    }

    /**
     * Cette méthode est apprlée lors de la sélection d'une sequence dans la
     * saisie d'une seance precedente.
     */
    public void chargerSequence() {
        form.setListeSeancePrecedente(chargerListeSeancePrecedente(form.getSeance()));
        form.setAfficheSuiteSeancePrecedente(form.getListeSeancePrecedente().size()>0);
        final List<DateDTO> listeDateRemiseDevoir = chargerListeDateRemiseDevoir(form.getSeance());
        form.setListeDateRemiseDevoir(listeDateRemiseDevoir);
    }

    /**
     * Navigue vers l'ecran precedent.
     * 
     * @return ma chaine de navigation.
     */
    public String retour() {
        form.reset();
        return NavigationUtils.retourOutilPrecedentEnDepilant();
    }

    /**
     * Appel métier pour supprimer une séance.
     * 
     * @return la chaine de navigation qui permet de retourner a l ecran
     *         precedent.
     */
    public String supprimer() {
        try {
            final String uid = ContexteUtils.getContexteUtilisateur()
                    .getUtilisateurDTO().getUserDTO().getUid();
            form.getResultatRechercheSeanceDTO().setUid(uid);

            final ResultatDTO<Integer> resultatDTO = seanceService
                    .deleteSeance(form.getResultatRechercheSeanceDTO());
            ContexteUtils.getContexteOutilControl().mettreAJourObjet(
                    AbstractForm.RETOUR_ACQUITTEMENT,
                    resultatDTO.getConteneurMessage());
            return NavigationUtils.retourOutilPrecedentEnDepilant();
        } catch (final MetierException e) {
            log.debug("{0}", e.getMessage());
        }
        return null;
    }

    /**
     * Appel métier pour supprimer une séance.
     */
    public void deleteSeance() {
        try {
            final String uid = ContexteUtils.getContexteUtilisateur()
                    .getUtilisateurDTO().getUserDTO().getUid();
            final ResultatRechercheSeanceDTO resultatRechercheSeanceDTO = new ResultatRechercheSeanceDTO();
            resultatRechercheSeanceDTO.setUid(uid);
            resultatRechercheSeanceDTO.setId(form.getSeance().getId());
            seanceService.deleteSeance(resultatRechercheSeanceDTO);
        } catch (final MetierException e) {
            log.debug("{0}", e.getMessage());
        }
    }
    
    /**
     * Charge sous forme d'image/html la description de la seance.
     * Cette méthode est utilisée lors du basculement en vue visulation/modification 
     */
    public void chargeImagesLatex() {
        final SeanceDTO seanceDTO = form.getSeance();
        seanceDTO.setDescriptionHTML(ImagesServlet.genererLatexImage(seanceDTO.getDescription()));        
    }

    /**
     * Charge sous forme d'image/html la description de la seance.
     * Cette méthode est utilisée lors du basculement en vue visulation/modification 
     */
    public void chargeImagesLatexAnnotations() {
        final SeanceDTO seanceDTO = form.getSeance();
        seanceDTO.setAnnotationsHTML(ImagesServlet.genererLatexImage(seanceDTO.getAnnotations()));        
    }

    /**
     * Charge sous forme d'image/html la description de la seance précédente en cours d'édition.
     * Cette méthode est utilisée lors du basculement en vue visulation/modification 
     */
    public void chargeImagesLatexSeancePrecedente() {
        final SeanceDTO seanceDTO = form.getSeancePrecedente();
        seanceDTO.setDescriptionHTML(ImagesServlet.genererLatexImage(seanceDTO.getDescription()));        
    }
    
    /**
     * Charge sous forme d'image/html la description de la seance précédente en cours d'édition.
     * Cette méthode est utilisée lors du basculement en vue visulation/modification 
     */
    public void chargeImagesLatexAnnotationsSeancePrecedente() {
        final SeanceDTO seanceDTO = form.getSeancePrecedente();
        seanceDTO.setAnnotationsHTML(ImagesServlet.genererLatexImage(seanceDTO.getAnnotations()));        
    }
    
    /**
     * Charge sous forme d'image/html la description du devoir de la seance en cours.
     * Cette méthode est utilisée lors du basculement en vue visulation/modification 
     */
    public void chargeImagesLatexDevoir() {
        final DevoirDTO devoirDTO = form.getDevoirSelected();
        devoirDTO.setDescriptionHTML(ImagesServlet.genererLatexImage(devoirDTO.getDescription()));        
    }

    /**
     * Declencher sur les ecran qui veulent afficher le detail d'un devoir.
     */
    public void afficherDevoir() {
        
    }

    /**
     * @param gestionRemplacementControl the gestionRemplacementControl to set
     */
    public void setGestionRemplacementControl(
            GestionRemplacementControl gestionRemplacementControl) {
        this.gestionRemplacementControl = gestionRemplacementControl;
    }
}
