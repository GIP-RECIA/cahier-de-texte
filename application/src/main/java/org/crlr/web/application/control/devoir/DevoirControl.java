/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirControl.java,v 1.44 2010/06/02 12:41:27 ent_breyton Exp $
 */

package org.crlr.web.application.control.devoir;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.TypeCategorieTypeDevoir;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.ChargeDevoirQO;
import org.crlr.dto.application.devoir.ChargeTravailDTO;
import org.crlr.dto.application.devoir.DetailJourDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.devoir.RechercheDevoirQO;
import org.crlr.dto.application.devoir.ResultatRechercheDevoirDTO;
import org.crlr.dto.application.devoir.SaveDevoirQO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.dto.application.seance.ConsulterSeanceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.DevoirService;
import org.crlr.services.EtablissementService;
import org.crlr.services.ImagesServlet;
import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.control.AbstractPopupControl;
import org.crlr.web.application.control.ClasseGroupeControl;
import org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener;
import org.crlr.web.application.control.EnseignementControl;
import org.crlr.web.application.control.EnseignementControl.EnseignementListener;
import org.crlr.web.application.control.PopupPiecesJointesControl;
import org.crlr.web.application.control.remplacement.GestionRemplacementControl;
import org.crlr.web.application.form.devoir.DevoirForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.dto.JoursDTO;
import org.crlr.web.dto.MoisDTO;
import org.crlr.web.utils.ConverteurDTOUtils;

import com.google.common.collect.Lists;

/**
 * Controleur de l'ecran de visualisation des devoirs. 
 * @author G-SAFIR-FRMP
 *
 */
@ManagedBean(name="devoir")
@ViewScoped
public class DevoirControl extends
AbstractPopupControl<DevoirForm> implements ClasseGroupeListener,
EnseignementListener {

    /** Service Devoir. */
    @ManagedProperty(value="#{devoirService}")
    private transient DevoirService devoirService;

    /** Service etablissement. */
    @ManagedProperty(value="#{etablissementService}")
    private transient EtablissementService etablissementService; 

    /** Controleur de piece jointe. */
    @ManagedProperty(value="#{popupPiecesJointes}")
    private transient PopupPiecesJointesControl popupPiecesJointesControl; 
    

    @ManagedProperty(value = "#{classeGroupe}")
    private transient ClasseGroupeControl classeGroupeControl;
    
    @ManagedProperty(value = "#{enseignement}")
    protected transient EnseignementControl enseignementControl;
    
    @ManagedProperty(value = "#{gestionRemplacement}")
    private transient GestionRemplacementControl gestionRemplacementControl;
    
    /**
     * Mutateur de devoirService {@link #devoirService}.
     * @param devoirService le devoirService to set
     */
    public void setDevoirService(DevoirService devoirService) {
        this.devoirService = devoirService;
    }
    
    /**
     * Mutateur de etablissementService {@link #etablissementService}.
     * @param etablissementService le etablissementService to set
     */
    public void setEtablissementService(EtablissementService etablissementService) {
        this.etablissementService = etablissementService;
    }

    /**
     * Mutateur de popupPiecesJointesControl {@link #popupPiecesJointesControl}.
     * @param popupPiecesJointesControl le popupPiecesJointesControl to set
     */
    public void setPopupPiecesJointesControl(
            PopupPiecesJointesControl popupPiecesJointesControl) {
        this.popupPiecesJointesControl = popupPiecesJointesControl;
    }

    /**
     * Constructeur.
     */
    public DevoirControl() {
        super(new DevoirForm());
    }

    /**
     * Methode de chargement du controleur.
     */
    @PostConstruct
    public void onLoad() {
        
        classeGroupeControl.setListener(this);
        enseignementControl.setListener(this);
        
        form.reset();
        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();

        // Charge les jours ouvrés de l'etablissement
        chargerJoursOuvres();

        try {
            alimenteBarreSemaine();

            if (Profil.ELEVE.equals(utilisateurDTO.getProfil())
                    || Profil.PARENT.equals(utilisateurDTO.getProfil())) {
                form.setTypeAffichage("LISTE");
                final Date debut = new Date();
                final Date fin = DateUtils.ajouter(debut, Calendar.DATE, 14);
                form.setDateDebut(debut);
                form.setDateFin(fin);
                form.setAffichageClasseGroupe(false);
            } else {
                form.setTypeAffichage("SEMAINE");
                form.setAffichageClasseGroupe(true);
            }

            // Charge la liste des type de devoir configures pour
            // l'etablissement
            final List<TypeDevoirDTO> listeTypeDevoir = devoirService
                    .findListeTypeDevoir(utilisateurDTO.getIdEtablissement())
                    .getValeurDTO();
            form.setListeTypeDevoir(listeTypeDevoir);

            // Charge la liste de enseignements proposes dans le filtre
            rechercherEnseignement();

            // Declenche la recherche
            if (Profil.ELEVE.equals(utilisateurDTO.getProfil())
                    || Profil.PARENT.equals(utilisateurDTO.getProfil())) {
                rechercher();
            }

        } catch (Exception e) {
            log.error( "Exception", e);
        }
           
        
        /*
         * 
         * Bouchon pour presélectionner la formulaire 
         */
        /*
        for (GroupesClassesDTO gc : classeGroupeControl.getForm().getListeGroupeClasse()) {
            if (!gc.getDesignation().equals("17")) {
                continue;
            }
            classeGroupeControl.getForm().setGroupeClasseSelectionne(gc);
            classeGroupeControl.classeGroupeSelectionnee();
            
            form.setTypeAffichage("LISTE");
            form.setDateDebut(DateUtils.creer(2012, 11, 1));
            form.setDateFin(DateUtils.creer(2013, 2, 7));
            rechercher();
            break;
        }
           */
    }
    
    /**
     * Charge les jours ouvré de l'etablissement.
     */
    private void chargerJoursOuvres() {
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        final Map<TypeJour,Boolean> jourOuvreAccessible = GenerateurDTO.generateMapJourOuvreFromDb(utilisateurDTO.getJoursOuvresEtablissement());
        form.setJourOuvreAccessible(jourOuvreAccessible);
        
        List<TypeJour> typeJoursListe = Lists.newArrayList();
        
        for (final TypeJour jour : TypeJour.values()) {
            if (jourOuvreAccessible.get(jour)) {
                typeJoursListe.add(jour);
            }
        }
        
        form.setListeJoursOuvre(typeJoursListe);
    }
    

    
    /**
     * Declenchee lors du changement de choix d'affichage. 
     */
    public void resetTypeAffichage() {
        if (form.getTypeAffichage().equals("LISTE")) {
            form.setDateDebut(form.getSemaineSelectionne().getLundi());
            form.setDateFin(form.getSemaineSelectionne().getDimanche());
        } 
        getDevoir();
    }
        
    /**
     * Appel metier de recherche des devoirs en fonction de potentiels criteres.
     */
    public void rechercher() {
        log.debug("----------------- RECHERCHE -----------------");
        form.setListe(new ArrayList<JoursDTO>());
        form.setVraiOuFauxRechercheActive(true);
        getDevoir();
    }
    
   

    /**
     * Fait appel a la fct en fonction du profil.
     */
    public void getDevoir() {
        if (form.getVraiOuFauxRechercheActive()) {
            final Profil profil = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getProfil();
            if (Profil.ELEVE.equals(profil)) {
                getDevoirEleveFamille();
            } else if (Profil.PARENT.equals(profil)) {
                getDevoirEleveFamille();
            } else if (Profil.ENSEIGNANT.equals(profil)) {
                getDevoirEnseignant();
            } else if (Profil.DIRECTION_ETABLISSEMENT.equals(profil)) {
                getDevoirEnseignant();
            } else if (Profil.DOCUMENTALISTE.equals(profil)) {
                getDevoirEnseignant();
            } else if (Profil.INSPECTION_ACADEMIQUE.equals(profil)) {
                getDevoirEnseignant();
            }
        }
    }
 
    /**
     * Retourne les devoirs pour les Ã©lÃ¨ves et la famille en fonction des date de
     * la semaine affichÃ©e.
     */
    private void getDevoirEleveFamille() {
        final RechercheDevoirQO rechercheDevoirQO = new RechercheDevoirQO();

        final Integer idEleve =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO()
                         .getIdentifiant();
        rechercheDevoirQO.setIdEleve(idEleve);

        if (form.getTypeAffichage().equals("SEMAINE")) {
            rechercheDevoirQO.setPremierJourSemaine(form.getSemaineSelectionne().getLundi());
            rechercheDevoirQO.setDernierJourSemaine(form.getSemaineSelectionne().getDimanche());
        } else {
            rechercheDevoirQO.setPremierJourSemaine(form.getDateDebut());
            rechercheDevoirQO.setDernierJourSemaine(form.getDateFin());
        }
        rechercheDevoirQO.setJourCourant(DateUtils.getAujourdhui());

        if (TypeCategorieTypeDevoir.DEVOIR.getId().equals(form.getCategorieSelectionne())) {
            rechercheDevoirQO.setTypeCategorie(TypeCategorieTypeDevoir.getMapCategorieTypeDevoir().get(form.getCategorieSelectionne()));
        }
        if (enseignementControl.getForm().getEnseignementSelectionne() != null && 
                BooleanUtils.isTrue(enseignementControl.getForm().getFiltreParEnseignement())) {
            rechercheDevoirQO.setIdEnseignement(enseignementControl.getForm().getEnseignementSelectionne().getId());
        }
        
        ResultatDTO<List<ResultatRechercheDevoirDTO>> resultat =
            new ResultatDTO<List<ResultatRechercheDevoirDTO>>();
        try {
            resultat = devoirService.listeDevoirAffichage(rechercheDevoirQO);
            
            final List<DetailJourDTO> liste = new ArrayList<DetailJourDTO>();
            
            if(resultat != null) {
                for (final ResultatRechercheDevoirDTO resultatRechercheDevoirDTO : resultat.getValeurDTO()) {
                    final DetailJourDTO dev = new DetailJourDTO();
                   
                    ObjectUtils.copyProperties(dev, resultatRechercheDevoirDTO);
                    dev.setMatiere(resultatRechercheDevoirDTO.getSeance().getSequence().getDesignationEnseignement());
                   
                    if (StringUtils.isBlank(dev.getDenomination())) {
                    	dev.setDenomination(dev.getCiviliteEnseignant() + ".");
                    }
                    if (StringUtils.isBlank(dev.getNom())) {
                    	dev.setNom(dev.getNomEnseignant());
                    }
                    generateDescriptionAbrege(dev);
                    liste.add(dev);
                   
                }
            }
            form.setListe(ConverteurDTOUtils.convertWeekCalendar(liste, false, false));
            form.setListeDevoir(liste);
        } catch (final MetierException e) {
            log.debug("{0}", e.getMessage());
            form.setVraiOuFauxRechercheActive(false);
        }
    }

    /**
     * Retourne les devoirs pour les enseignants en fonction des date de la
     * semaine affichÃ©e.
     */
    private void getDevoirEnseignant() {
        final RechercheDevoirQO rechercheDevoirQO = new RechercheDevoirQO();

        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        final Integer idUtilisateur = utilisateurDTO.getUserDTO()
        .getIdentifiant();
        
        final Boolean vraiOuFauxEnseignant = Profil.ENSEIGNANT.equals(ContexteUtils.getContexteUtilisateur()
        .getUtilisateurDTO().getProfil());
        
        if (vraiOuFauxEnseignant) {
            rechercheDevoirQO.setIdEnseignantConnecte(
                    ContexteUtils.getContexteUtilisateur().getUtilisateurDTOConnecte().getUserDTO().getIdentifiant());
        }
        
        if (utilisateurDTO.getProfil().equals(Profil.INSPECTION_ACADEMIQUE)){
            rechercheDevoirQO.setIdInspecteur(idUtilisateur);
            rechercheDevoirQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
        } 
                
        rechercheDevoirQO.setIdEnseignant(idUtilisateur);
        rechercheDevoirQO.setTypeGroupeSelectionne(classeGroupeControl.getForm().getTypeGroupeSelectionne());
        if (form.getTypeAffichage().equals("SEMAINE")) {
            rechercheDevoirQO.setPremierJourSemaine(form.getSemaineSelectionne().getLundi());
            rechercheDevoirQO.setDernierJourSemaine(form.getSemaineSelectionne().getDimanche());
        } else {
            rechercheDevoirQO.setPremierJourSemaine(form.getDateDebut());
            rechercheDevoirQO.setDernierJourSemaine(form.getDateFin());
        }
        rechercheDevoirQO.setJourCourant(DateUtils.getAujourdhui());
        
        final List<GroupeDTO> listeGroupeDTOSelectionne = new ArrayList<GroupeDTO>();
        final List<GroupeDTO> listeGroupeDTO = classeGroupeControl.getForm().getListeGroupe();
        for (final GroupeDTO groupe : listeGroupeDTO) {
            if (groupe.getSelectionner()) {
                listeGroupeDTOSelectionne.add(groupe);
            }
        }
        rechercheDevoirQO.setListeGroupeDTO(listeGroupeDTOSelectionne);
        rechercheDevoirQO.setGroupeClasseSelectionne(classeGroupeControl.getForm().getGroupeClasseSelectionne());

        if (TypeCategorieTypeDevoir.DEVOIR.getId().equals(form.getCategorieSelectionne())) {
            rechercheDevoirQO.setTypeCategorie(TypeCategorieTypeDevoir.getMapCategorieTypeDevoir().get(form.getCategorieSelectionne()));
        }
        if (enseignementControl.getForm().getEnseignementSelectionne() != null &&
                BooleanUtils.isTrue(enseignementControl.getForm().getFiltreParEnseignement())) {
            rechercheDevoirQO.setIdEnseignement(enseignementControl.getForm().getEnseignementSelectionne().getId());
        }

        ResultatDTO<List<ResultatRechercheDevoirDTO>> resultat =
            new ResultatDTO<List<ResultatRechercheDevoirDTO>>();
        try {
            resultat = devoirService.listeDevoirAffichage(rechercheDevoirQO);

            final List<DetailJourDTO> liste = new ArrayList<DetailJourDTO>();

            
            if(resultat != null) {
                for (final ResultatRechercheDevoirDTO resultatRechercheDevoirDTO : resultat.getValeurDTO()) {
                    final DetailJourDTO dev = new DetailJourDTO();
                    ObjectUtils.copyProperties(dev, resultatRechercheDevoirDTO);
                    
                    dev.setMatiere(resultatRechercheDevoirDTO.getSeance().getSequence().getDesignationEnseignement());
                    dev.setDenomination(resultatRechercheDevoirDTO.getCiviliteEnseignant());
                    dev.setNom(resultatRechercheDevoirDTO.getNomEnseignant());
                    generateDescriptionAbrege(dev);                    
                    
                    
                    liste.add(dev);
                }
            }
            form.setListeDevoir(liste);
            form.setListe(ConverteurDTOUtils.convertWeekCalendar(liste, false));
        } catch (final MetierException e) {
            log.debug("{0}", e.getMessage());
            form.setVraiOuFauxRechercheActive(false);
        }
    }

    /**
     * Génère la description abrégée visible dans le listing de devoir.
     * @param devoirDTO le devoir courant.
     */
    private void generateDescriptionAbrege(final DetailJourDTO devoirDTO) {
        if (devoirDTO.getDescription()==null) {
            return;
        }
        final String descriptionSansBalise = org.crlr.utils.StringUtils.removeBalise(devoirDTO.getDescription()).trim();
        String s = org.crlr.utils.StringUtils.truncate(descriptionSansBalise,70);
        if (descriptionSansBalise != null && descriptionSansBalise.length()>70) {
            s += "...";
        }
        
        devoirDTO.setDescriptionSansBaliseAbrege(s);
        devoirDTO.setDescriptionSansBalise(s);
    }
    
    /**
     * Alimente la liste des semaines et mois.
     * @throws MetierException exception
     */
    private void alimenteBarreSemaine() throws MetierException {
        // Charge la barre des semaines / mois
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        final AnneeScolaireDTO anneeScolaireDTO = ObjectUtils.clone(utilisateurDTO.getAnneeScolaireDTO());
        final String alternanceSemaine =  etablissementService.findAlternanceSemaine(utilisateurDTO.getIdEtablissement());
        final Boolean vraiOuFauxAlternance = !StringUtils.isEmpty(alternanceSemaine);
        final List<BarreSemaineDTO> listeSemaine = 
            GenerateurDTO.generateListeSemaine(anneeScolaireDTO, alternanceSemaine, vraiOuFauxAlternance); 
        form.setListeBarreSemaine(listeSemaine);
        form.setListeMois( 
                GenerateurDTO.genererListeMois(anneeScolaireDTO.getDateRentree(),
                        anneeScolaireDTO.getDateSortie()));        
        form.setDecalageDebutAnnee(MoisDTO.getDecalageDebutAnnee(anneeScolaireDTO.getDateRentree()));
        form.setFinAnneeScolaire(anneeScolaireDTO.getDateSortie());
        
        form.setSemaineSelectionne(GenerateurDTO.selectionnerSemaineJour(form.getListeBarreSemaine()));
    }
 
   
    
    /**
     * Methode appeles lors de la selection d'une semaine dans la barre des semaines.
     */
    public void selectionnerSemaine() {
        getDevoir();
    }
    
    /**
     * Voir le detail d'un devoir.
     * Charge le devoir grace a son id.
     */
    public void naviguerDetailDevoir() {
        final Integer idDevoir = form.getSelectDevoir().getId();
        
        final DevoirDTO devoir = devoirService.findDetailDevoir(idDevoir);
        
        //Remettre si nous avions accès dans le nouveau devoirDTO
        devoir.getSeance().setAccesEcriture(form.getSelectDevoir().getSeance().getAccesEcriture());
        
        devoir.setDescriptionHTML(ImagesServlet.genererLatexImage(devoir.getDescription()));
        chercherChargeTravailGenerique(devoir);
        
        form.setDevoirSelected(devoir);
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
    
    public void resetListeDevoir() {
        form.setListeDevoir(new ArrayList<DetailJourDTO>());
        form.setListe(ConverteurDTOUtils.convertWeekCalendar(form.getListeDevoir(), false));
    }
    
    /**
     * Supprime la piece jointe selected du devoir selected.
     */
    public void deletePieceJointe() {
        final DevoirDTO devoir = form.getDevoirSelected();
        if (devoir == null) { return; }
        final FileUploadDTO fichier = form.getPieceJointeASupprimer();
        if (fichier == null) { return; }
        if (!CollectionUtils.isEmpty(devoir.getFiles())) {
            devoir.getFiles().remove(fichier);
        }
    }

    /**
     * Ajoute une nouvelle piece jointe sur le devoir selected.
     * Cette méthode est appelée lors du clic sur l'icone d'affichage de la popup piece jointe.
     */
    public void ajouterPieceJointe() {
        // A a priori, au moment de cliquer sur le lien, il n'y a rien a faire.
        final DevoirDTO devoir = form.getDevoirSelected();
        if (devoir != null) {
            final String raffraichirTabAfterUpload = form.getRaffraichirTabAfterUpload();
            if (raffraichirTabAfterUpload != null) {
                log.debug("Déclenche l'ouverture de la popup des pieces jointe pour devoir");
            }
        }

    }

    /**
     * Cette méthode est appellee par la popup piece jointe suite à un upload de fichier.
     */
    public void afterAjouterPieceJointe() {
        final FileUploadDTO file = popupPiecesJointesControl.getForm().getFileUploade();
        if (file != null) {
            final DevoirDTO devoir = form.getDevoirSelected();
            if (devoir != null) {
                if (CollectionUtils.isEmpty(devoir.getFiles())) {
                    devoir.setFiles(new ArrayList<FileUploadDTO>());
                }
                devoir.getFiles().add(file);
            }
        }
    }    

    /**
     * Appelée lors de l'affichage de la popup de charge de travail.
     */
    public void afficherChargeTravail() {
        
    }

    /**
     * Sauvegarde le devoir qui est modifie dans la popup.
     */
    public void sauverDevoir()  {
       final DevoirDTO devoir = form.getDevoirSelected();
       if (devoir != null) {
           final SaveDevoirQO saveDevoirQO = new SaveDevoirQO();
           saveDevoirQO.setDevoirDTO(devoir);
           saveDevoirQO.setDateSeance(devoir.getSeance().getDate());
           saveDevoirQO.setDateFinAnneeScolaire(form.getFinAnneeScolaire());
           
           final UtilisateurDTO utilisateurDTO = ContexteUtils
                   .getContexteUtilisateur().getUtilisateurDTOConnecte();
           final Profil profilUser = utilisateurDTO.getProfil();

           //Dans le cas d'un remplaçement, mettre l'id d'enseignant pour eventuellement changer
           //l'id de séance saisie par anticipation
           if (profilUser == Profil.ENSEIGNANT && ContexteUtils.getContexteUtilisateur().getUtilisateurDTOOrigine() != null) {
               
               ConsulterSeanceQO cSeance = new ConsulterSeanceQO();
               cSeance.setId(devoir.getSeance().getId());
               
               try {
                   SeanceDTO seance = seanceService.rechercherSeance(cSeance);
                   Integer idEnseignantConnecte = utilisateurDTO.getUserDTO().getIdentifiant();
                   if (
                           !seance.getIdEnseignant().equals(idEnseignantConnecte)
                           ) {
                       
                       seance.setIdEnseignant(idEnseignantConnecte);
                       seanceService.modifieSeance(seance);
                   }
               } catch (MetierException ex) {
                   log.error("ex",ex);
               }
               
           }
           
           try {
            devoirService.saveDevoir(saveDevoirQO);
           } catch (MetierException e) {
            log.debug("erreur de modification du devoir : {0}", e.getMessage());      
           }
           
           // Recharge toute la semaine.
           getDevoir();
       }
    }

    /**
     * Sauvegarde le devoir qui est modifie dans la popup.
     * @throws MetierException  exception.
     */
    public void deleteDevoir() throws MetierException {
       final DevoirDTO devoir = form.getDevoirSelected();
       if (devoir != null) {
           devoirService.deleteDevoir(devoir.getId());
           form.setDevoirSelected(null);
           
           // Recharge toute la semaine.
           getDevoir();
       }
    }
    
    /**
     * Appel métier de recherche des enseignements.
     */
    private void rechercherEnseignement() {
        
        
        enseignementControl.chargerListeEnseignement(
                classeGroupeControl.getForm().getGroupeClasseSelectionne(), 
                classeGroupeControl.getForm().getTypeGroupeSelectionne(), 
                true, false, null, classeGroupeControl.getForm().getListeGroupe());
    }
    
    /**
     * Cherche pour la date de remise du devoir saisie les autres devoirs 
     * prévu à rendre le même jour pour.
     * @param devoir le devoir a tester
     * @return la charge de travail prevu pour ce devoir.
     */
    private ChargeTravailDTO chercherChargeTravailGenerique(final DevoirDTO devoir) {
        ChargeTravailDTO chargeTravail = new ChargeTravailDTO();
        if (devoir != null) {
            final Date dateRemise = devoir.getDateRemise();
            final Integer idClasse = devoir.getIdClasse();
            final Integer idGroupe = devoir.getIdGroupe();
            final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
            
            if (dateRemise != null && (idClasse != null || idGroupe != null)) {
                final ChargeDevoirQO chargeDevoirQO = new ChargeDevoirQO();
                chargeDevoirQO.setDateDevoir(dateRemise);
                chargeDevoirQO.setIdClasse(idClasse);
                chargeDevoirQO.setIdGroupe(idGroupe);
                chargeDevoirQO.setIdDevoir(devoir.getId());
                chargeDevoirQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
                chargeDevoirQO.setUidEnseignant(utilisateurDTO.getUserDTO().getUid());
                
                chargeTravail = devoirService.findChargeTravail(chargeDevoirQO);
            }
        }
        devoir.setChargeTravail(chargeTravail);
        return chargeTravail;
    }

    /**
     * Verifie la charge de travail prévue pour le devoir en cours d'edition.
     */
    public void chercherChargeTravail() {
        chercherChargeTravailGenerique(form.getDevoirSelected());
    }

    /**
     * @param classeGroupeControl the classeGroupeControl to set
     */
    public void setClasseGroupeControl(ClasseGroupeControl classeGroupeControl) {
        this.classeGroupeControl = classeGroupeControl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener
     * #classeGroupeTypeSelectionne()
     */
    @Override
    public void classeGroupeTypeSelectionne() {
        form.setListe(new ArrayList<JoursDTO>());
        form.setListeDevoir(new ArrayList<DetailJourDTO>());
        form.setVraiOuFauxRechercheActive(false);
        enseignementControl.getForm().setEnseignementSelectionne(null);
        enseignementControl.getForm().setFiltreParEnseignement(false);
        
        resetListeDevoir();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener
     * #classeGroupeSelectionnee()
     */
    @Override
    public void classeGroupeSelectionnee() {

        // lance la recherche
        form.setListe(new ArrayList<JoursDTO>());
        form.setListeDevoir(new ArrayList<DetailJourDTO>());

        // Rafraichit la liste des enseignement dispo
        rechercherEnseignement();
        enseignementControl.getForm().setEnseignementSelectionne(null);
        enseignementControl.getForm().setFiltreParEnseignement(false);
        
        resetListeDevoir();
        form.setVraiOuFauxRechercheActive(false);
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
     * @param enseignementControl the enseignementControl to set
     */
    public void setEnseignementControl(EnseignementControl enseignementControl) {
        this.enseignementControl = enseignementControl;
    }

    /* (non-Javadoc)
     * @see org.crlr.web.application.control.EnseignementControl.EnseignementListener#enseignementTousOuUnSelectionne(boolean)
     */
    @Override
    public void enseignementTousOuUnSelectionne(boolean tous) {
        resetListeDevoir();
        form.setVraiOuFauxRechercheActive(false);
        
    }

    /* (non-Javadoc)
     * @see org.crlr.web.application.control.EnseignementControl.EnseignementListener#enseignementSelectionnee()
     */
    @Override
    public void enseignementSelectionnee() {
        resetListeDevoir();
        form.setVraiOuFauxRechercheActive(false);
    }
    
    /**
     * Declenche suite la selection du filtre de type de devoirs.
     */
    public void filtreParTypeDevoir() {
        resetListeDevoir();
        form.setVraiOuFauxRechercheActive(false);
    }

    /**
     * @param gestionRemplacementControl the gestionRemplacementControl to set
     */
    public void setGestionRemplacementControl(
            GestionRemplacementControl gestionRemplacementControl) {
        this.gestionRemplacementControl = gestionRemplacementControl;
    }
}
