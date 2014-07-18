/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: MenuControl.java,v 1.27 2010/05/19 08:44:25 ent_breyton Exp $
 */

package org.crlr.web.application.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedProperty;

import org.apache.commons.collections.CollectionUtils;
import org.crlr.dto.Outil;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.UserDTO;
import org.crlr.dto.application.base.ElevesParentDTO;
import org.crlr.dto.application.base.EtablissementAccessibleQO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.exception.base.CrlrRuntimeException;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.ConfidentialiteService;
import org.crlr.utils.BooleanUtils;
import org.crlr.web.application.MenuAction;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.contexte.utils.ContexteUtils;

/**
 * Contrôleur du menu de l'application personnalisé selon les droits de
 * l'utilisateur.
 *
 * @author breytond
 */
public class MenuControl extends AbstractControl<AbstractForm> {
    /** The Constant ID. */
    public static final String ID = "menu";

     /** service de confidentialité. */
    @ManagedProperty(value="#{confidentialiteService}")
    private transient ConfidentialiteService confidentialiteService;
    
    /**
     * Mutateur de confidentialiteService {@link #confidentialiteService}.
     * @param confidentialiteService le confidentialiteService to set
     */
    public void setConfidentialiteService(
            ConfidentialiteService confidentialiteService) {
        this.confidentialiteService = confidentialiteService;
    }

    /** Liste des actions habilite pour l'utilisateur. */
    private List<MenuAction> listeAction = new ArrayList<MenuAction>(); 

    /**
     * Instantiates a new menu control.
     */
    public MenuControl() {
        super(null);
    }

    /**
     * Charger la menu item .
     * @param utilisateurDTO u
     */
    private void chargerAccueil(UtilisateurDTO utilisateurDTO) {
        
        if (!BooleanUtils.isTrue(utilisateurDTO.getVraiOuFauxAdmCentral())) {
            this.listeAction.add(new MenuAction("accueil.png", Outil.ACCUEIL.name(), "Accueil", null));
        }
    }
    
    /**
     * Charger la menu.
     * @param utilisateurDTO u
     */
    private void chargerMenu(UtilisateurDTO utilisateurDTO) {
        
        switch (ContexteUtils.getContexteApplication().getEnvironnement()) {
        case CRLR:
        case Aquitaine:
        case CRC:
            chargerMenuCRC(utilisateurDTO);
            break;
        default:
            new CrlrRuntimeException(
                    "Cet environnement d'exécution '{0}' est inconnu");
        }
    }
    
    /**
     * Charger la menu item .
     * @param utilisateurDTO u
     */
    private void chargerPref(UtilisateurDTO utilisateurDTO) {
        final ContexteUtilisateur contexteUtilisateur = ContexteUtils.getContexteUtilisateur();
        final UtilisateurDTO utilisateurDTOOrigine = contexteUtilisateur.getUtilisateurDTOOrigine();
        
        //En mode remplaçant ou simulation éleve, nous pouvons pas modifier les préfèrences
        if (utilisateurDTOOrigine != null) {
            return;
        }
        
        if (Profil.ENSEIGNANT.equals(utilisateurDTO.getProfil())
                || Profil.PARENT.equals(utilisateurDTO.getProfil())
                || Profil.ELEVE.equals(utilisateurDTO.getProfil())) {
            this.listeAction.add(new MenuAction("preferences.png",
                    Outil.PREFERENCE.name(), "Mes préférences", null));
        }
    }
    
    /**
     * Charger la menu item .
     * @param utilisateurDTO u
     */
    private void chargerChangeEleve(UtilisateurDTO utilisateurDTO) {
        if(Profil.PARENT != utilisateurDTO.getProfil()) {
            return;
        }
            
        final ResultatDTO<ElevesParentDTO> resultatDTO =
                confidentialiteService.findEleveDuParent(utilisateurDTO.getListeUidEnfant());

        final Set<UserDTO> listeEnfant =
                resultatDTO.getValeurDTO().getListeEnfant();
        
        if (!CollectionUtils.isEmpty(listeEnfant)) {
            if (listeEnfant.size() > 1) {
                this.listeAction.add(new MenuAction("changeEleve.png", 
                        Outil.PREFERENCE_CAHIER.name(), "Changer l'enfant courant", null));
            }
        }
    
    }
    
    /**
     * Charger la menu item Change établissement.
     * @param utilisateurDTO u
     */
    private void chargerChangerEtablissement(UtilisateurDTO utilisateurDTO) {
        if(Profil.PARENT == utilisateurDTO.getProfil()){
            return;
        }
            
        // Verifie si l'utilisateur est attache a plusieurs etablissements
        final UserDTO userDTO = utilisateurDTO.getUserDTO(); 
        ResultatDTO<List<EtablissementDTO>> resultatEtablissement = new ResultatDTO<List<EtablissementDTO>>();
        try {
            if (BooleanUtils.isTrue(utilisateurDTO.getVraiOuFauxAdmRessourceENT())) {
                if (Profil.ENSEIGNANT.equals(utilisateurDTO.getProfil())) {
                    final EtablissementAccessibleQO etablissementAccessibleQO =
                            new EtablissementAccessibleQO();
                    etablissementAccessibleQO.setListeSiren(
                            new ArrayList<String>(utilisateurDTO.getSirensEtablissement()));
                    etablissementAccessibleQO.setIdEnseignant(userDTO.getIdentifiant());
                    resultatEtablissement = confidentialiteService.
                            findListeEtablissementEnseignantAdmRessources(etablissementAccessibleQO);
                }else {
                    resultatEtablissement = confidentialiteService.findListeEtablissement();
                }
            }else {
                final EtablissementAccessibleQO etablissementAccessibleQO = new EtablissementAccessibleQO();
                etablissementAccessibleQO.setListeSiren( new ArrayList<String>(utilisateurDTO.getSirensEtablissement()));
                
                // Ajoute les siren pour lesquels l'user est admin local
                for (final Iterator<String> i = utilisateurDTO.getAdminLocalSiren().iterator(); i.hasNext(); ) {
                    final String siren = i.next(); 
                    if (!etablissementAccessibleQO.getListeSiren().contains(siren)) {
                        etablissementAccessibleQO.getListeSiren().add(siren);
                    }
                }
                // Ajoute les siren pour lesquels l'user est admin de ressource cahier de texte
                for (final Iterator<String> i = utilisateurDTO.getAdminRessourceSiren().iterator(); i.hasNext(); ) {
                    final String siren = i.next(); 
                    if (!etablissementAccessibleQO.getListeSiren().contains(siren)) {
                        etablissementAccessibleQO.getListeSiren().add(siren);
                    }
                }
                etablissementAccessibleQO.setIdEnseignant(
                        Profil.ENSEIGNANT.equals(utilisateurDTO.getProfil()) ? userDTO.getIdentifiant() : null);

                resultatEtablissement = confidentialiteService.findListeEtablissementEnseignant(
                        etablissementAccessibleQO);
            }
        } catch (MetierException e) {
            log.debug("Erreur du changement de la liste des établissement du profil Inspecteur académique");
        }
        // Si l'utilisateur est associe a plusieurs etablissement, on ajoute l'entree pour changer d'etablissement
        if(resultatEtablissement.getValeurDTO().size()>1 || 
           BooleanUtils.isTrue(utilisateurDTO.getVraiOuFauxAdmCentral()) || 
           Profil.INSPECTION_ACADEMIQUE.equals(utilisateurDTO.getProfil())   
        ){
            this.listeAction.add(new MenuAction("changeEtablissement.png", 
                    Outil.PREFERENCE_CAHIER.name(), "Changer l'établissement courant", null));
        }
    
    }

    /**
     * Initialisation du menu après authentification en fonction des droits de
     * l'utilisateur.
     */
    public void init() {
        final ContexteUtilisateur contexteUtilisateur = ContexteUtils.getContexteUtilisateur();
        final UtilisateurDTO utilisateurDTO = contexteUtilisateur.getUtilisateurDTO();
        
        // Initialise la liste des actions
        this.listeAction = new ArrayList<MenuAction>();
        
        chargerAccueil(utilisateurDTO);
        
        chargerMenu(utilisateurDTO);
        
        chargerPref(utilisateurDTO);
        
        chargerChangeEleve(utilisateurDTO);
        
        chargerChangerEtablissement(utilisateurDTO);

        Set<Profil> profilsDisponibles = utilisateurDTO.getProfilsDisponibles();
        
        /* modif pl */
        if (profilsDisponibles != null && profilsDisponibles.size() > 1) {
        	this.listeAction.add(new MenuAction("changeRole.png",
					Outil.CHANGE_PROFIL.name(),
					"Changer de rôle utilisateur", null));
        }
        /*
		if (!CollectionUtils.isEmpty(profilsDisponibles)
				&& profilsDisponibles.size() > 1) {
			profilsDisponibles.remove(utilisateurDTO.getProfil());
			for (Profil profilDisponible : profilsDisponibles) {
				this.listeAction.add(new MenuAction("changeRole.png",
						Outil.CHANGE_PROFIL.name(),
						"Changer de rôle utilisateur", null));
			}
		}
		*/
    }
    
    /**
     * Charge le menu de l'environnement CRC.
     * @param utilisateurDTO le dto de l'utilisateur connecté.
     */
    private void chargerMenuCRC(final UtilisateurDTO utilisateurDTO) {
        switch (utilisateurDTO.getProfil()) {
        case ELEVE:
            chargerArbreCRCParentEleve(utilisateurDTO);
            break;
        case PARENT:
            chargerArbreCRCParentEleve(utilisateurDTO);
            break;
        case ENSEIGNANT:               
            chargerArbreCRCEnseignant(utilisateurDTO);                               
            break;
        case DIRECTION_ETABLISSEMENT:
            chargerArbreCRCDirecteur(utilisateurDTO);
            break;
        case INSPECTION_ACADEMIQUE:
            chargerArbreCRCInspection(utilisateurDTO);
            break;
        case DOCUMENTALISTE:
            chargerArbreCRCDocumentaliste(utilisateurDTO);
            break;
        default:
            break;
        }
        addNodeAdmin(utilisateurDTO);
    }
    
    /**
     * Charger arbre Profil Documentaliste.
     * @param utilisateurDTO les paramétres.
     */
    private void chargerArbreCRCDocumentaliste(UtilisateurDTO utilisateurDTO) {
        
        if (isCahierAccessible(utilisateurDTO)) {
            
            // Consultation - Impression
            List<MenuAction> listeSousMenu = new ArrayList<MenuAction>();
            this.listeAction.add(new MenuAction("rechercheEdition.png", "", "Consultation-Impression par classe/groupe", listeSousMenu));
            listeSousMenu.add(new MenuAction("cahierTexteConsultation.png", Outil.SEANCE_SEMAINE.name(), "Séances par classe", null));
            listeSousMenu.add(new MenuAction("cahierTexteEditionSeance.png", Outil.PRINT_SEANCE.name(), "Détail/PDF des séances par classe", null));
            listeSousMenu.add(new MenuAction("cahierTexteEditionSequence.png", Outil.PRINT_SEQUENCE.name(), 
                    "Détail/PDF des séquences par classe",null));

            // Emploi du temps / Import EDT
            final Set<String> listeSirenAdm = utilisateurDTO.getAdminLocalSiren();
            final Set<String> listeSirenRes = utilisateurDTO.getAdminRessourceSiren();
            final String sirenSel = utilisateurDTO.getSirenEtablissement();
            if (listeSirenAdm.contains(sirenSel) || listeSirenRes.contains(sirenSel) ||
                    BooleanUtils.isTrue(utilisateurDTO.getVraiOuFauxAdmRessourceENT()) ||
                    BooleanUtils.isTrue(utilisateurDTO.getVraiOuFauxAdmCentral())) {
                
                listeSousMenu = new ArrayList<MenuAction>();
                this.listeAction.add(new MenuAction("emploiTemps.png", "", "Emploi du temps", listeSousMenu));
                    listeSousMenu.add(new MenuAction("emploiTempsClasse.png", Outil.CONSOLIDER_EMP.name(), "Par classe", null));
                    listeSousMenu.add(new MenuAction("importEDT.png", Outil.IMPORTEDT.name(), "Importer", null));
            } else {
                this.listeAction.add(new MenuAction("emploiTempsClasse.png", Outil.CONSOLIDER_EMP.name(), "Emploi du temps", null));
            }
        }
    }
    
    /**
     * Charger arbre Profil Inspection Académique.
     * @param utilisateurDTO les paramétres.
     */
    private void chargerArbreCRCInspection(UtilisateurDTO utilisateurDTO) {
        // Mes droits
        this.listeAction.add(new MenuAction("inspection.png", Outil.INSPECTION.name(), "Mes droits", null));

        // Visa
        List<MenuAction> listeSousMenu = new ArrayList<MenuAction>();
        this.listeAction.add(new MenuAction("visa.png", "", "Visas", listeSousMenu));
            listeSousMenu.add(new MenuAction("visaListe.png", Outil.VISA_LISTE.name(), "Liste des enseignants", null));
            listeSousMenu.add(new MenuAction("visaSeance.png", Outil.VISA_SEANCE.name(), "Liste des séances par enseignant", null));
        
        // Consultation impression
        listeSousMenu = new ArrayList<MenuAction>();
        this.listeAction.add(new MenuAction("rechercheEdition.png", "", "Consultation-Impression par classe/groupe", listeSousMenu));
        listeSousMenu.add(new MenuAction("cahierTexteConsultation.png", Outil.SEANCE_SEMAINE.name(), "Séances par classe", null));
        listeSousMenu.add(new MenuAction("cahierTexteEditionSeance.png", Outil.PRINT_SEANCE.name(), "Détail/PDF des séances par classe", null));
        listeSousMenu.add(new MenuAction("cahierTexteEditionSequence.png", Outil.PRINT_SEQUENCE.name(), 
                "Détail/PDF des séquences par classe",null));
        
        // Emploi du temps
        this.listeAction.add(new MenuAction("emploiTempsClasse.png", Outil.CONSOLIDER_EMP.name(), "Emploi du temps", null));
        
        // Travail a faire / Devoirs
        this.listeAction.add(new MenuAction("devoir.png", Outil.DEVOIRS.name(), "Travail à faire / Devoirs", null));
    }
    
    /**
     * Charger arbre Profil Eleve ou Parent pour CRC.
     * @param utilisateurDTO les paramétres.
     */
    private void chargerArbreCRCParentEleve(final UtilisateurDTO utilisateurDTO) {
        if (isCahierAccessible(utilisateurDTO)) {

            // Emploi du temps
            this.listeAction.add(new MenuAction("emploiTempsClasse.png", Outil.CONSOLIDER_EMP.name(), "Emploi du temps", null));
            
            // Travail à faire / Devoirs
            this.listeAction.add(new MenuAction("devoir.png", Outil.DEVOIRS.name(), "Travail à faire / Devoirs",null));

            // Cahier de texte mensuel
            this.listeAction.add(new MenuAction("cahierTexteMensuel.png", Outil.CAHIER_MENSUEL.name(), "Cahier de texte mensuel", null));
            
            // Consultation Impression
            List<MenuAction> listeSousMenu = new ArrayList<MenuAction>();
            this.listeAction.add(new MenuAction("rechercheEdition.png", "", "Consultation-Impression par classe/groupe", listeSousMenu));
            listeSousMenu.add(new MenuAction("cahierTexteConsultation.png", Outil.SEANCE_SEMAINE.name(), "Séances par classe", null));
            listeSousMenu.add(new MenuAction("cahierTexteEditionSeance.png", Outil.PRINT_SEANCE.name(), "Détail/PDF des séances par classe", null));
            listeSousMenu.add(new MenuAction("cahierTexteEditionSequence.png", Outil.PRINT_SEQUENCE.name(), 
                    "Détail/PDF des séquences par classe",null));
            
            // Notes et absences
            /*listeSousMenu = new ArrayList<MenuAction>();
            this.listeAction.add(new MenuAction("noteAbsence.png", "", "Notes et absences", listeSousMenu));
            listeSousMenu.add(new MenuAction("note.png", Outil.CONSULTER_NOTE.name(), "Carnet de notes", null));
            listeSousMenu.add(new MenuAction("absence.png", Outil.CONSULTER_ABSENCE.name(), "Carnet des absences", null));*/
            
            // Mode simulation
            final ContexteUtilisateur contextUtilisateur = ContexteUtils.getContexteUtilisateur();
            if (contextUtilisateur.getUtilisateurDTOOrigine() != null) {
                this.listeAction.add(new MenuAction("changeEleve.png", Outil.SIMULATION_ELEVE.name(), "Mode simulation élève", null));
            }
        }
    }
    
    /**
     * Charger arbre Profil Enseignant pour CRC.
     * @param utilisateurDTO les paramétres.
     */
    private void chargerArbreCRCEnseignant(final UtilisateurDTO utilisateurDTO) {
        if (isCahierAccessible(utilisateurDTO)) {

            //1. Mon cahier de texte mensuel
            this.listeAction.add(new MenuAction("cahierTexteMensuel.png", Outil.CAHIER_MENSUEL.name(), "Cahier de texte mensuel", null));
            
            // 2. Remplir le cahier de texte
            this.listeAction.add(new MenuAction("saisirCahierTexte.png", Outil.SAISIR_EMP.name(), "Remplir le cahier de texte", null));
            List<MenuAction> listeSousMenu = new ArrayList<MenuAction>();

            // 3. Consultation Impression
            listeSousMenu = new ArrayList<MenuAction>();
            this.listeAction.add(new MenuAction("rechercheEdition.png", "", "Consultation-Impression par classe/groupe", listeSousMenu));
            listeSousMenu.add(new MenuAction("cahierTexteConsultation.png", Outil.SEANCE_SEMAINE.name(), "Séances par classe", null));
            // Ajout de ce sous menu qui était à l'origine avec le bouton "Séances" qui a été retiré (ce sous menu s'appelait "Rechercher/Modifier") :
            listeSousMenu.add(new MenuAction("seanceRecherche.png", Outil.RECH_SEANCE.name(), "Recherche de séances", null)); 
            listeSousMenu.add(new MenuAction("cahierTexteEditionSeance.png", Outil.PRINT_SEANCE.name(), "Détail/PDF des séances par classe", null));
            listeSousMenu.add(new MenuAction("cahierTexteEditionSequence.png", Outil.PRINT_SEQUENCE.name(), 
                    "Détail/PDF des séquences par classe",null));
            
            // 4. Travail a faire / Devoirs
            this.listeAction.add(new MenuAction("devoir.png", Outil.DEVOIRS.name(), "Travail à faire / Devoirs", null));
            
            // Notes et absences
            /*listeSousMenu = new ArrayList<MenuAction>();
            this.listeAction.add(new MenuAction("noteAbsence.png", "", "Notes et absences", listeSousMenu));
            listeSousMenu.add(new MenuAction("note.png", Outil.AJOUTER_NOTE.name(), "Carnet de notes", null));
            listeSousMenu.add(new MenuAction("absence.png", Outil.AJOUTER_ABSENCE.name(), "Carnet des absences", null));*/

            
            // Emploi du temps
            listeSousMenu = new ArrayList<MenuAction>();
            this.listeAction.add(new MenuAction("emploiTemps.png", "", "Emploi du temps", listeSousMenu));
                listeSousMenu.add(new MenuAction("emploiTempsConstituer.png", Outil.CONSTITUER_EMP.name(), "Constituer", null));
                listeSousMenu.add(new MenuAction("emploiTempsClasse.png", Outil.CONSOLIDER_EMP.name(), "Par classe", null));
            final Set<String> listeSirenAdm = utilisateurDTO.getAdminLocalSiren();
            final Set<String> listeSirenRes = utilisateurDTO.getAdminRessourceSiren();
            final String sirenSel = utilisateurDTO.getSirenEtablissement();
            if (listeSirenAdm.contains(sirenSel) || listeSirenRes.contains(sirenSel) ||
                    BooleanUtils.isTrue(utilisateurDTO.getVraiOuFauxAdmRessourceENT()) ||
                    BooleanUtils.isTrue(utilisateurDTO.getVraiOuFauxAdmCentral())) {
                listeSousMenu.add(new MenuAction("importEDT.png", Outil.IMPORTEDT.name(), "Importer", null));
                
            }

            // 5. Séquence
            if (org.apache.commons.lang.BooleanUtils.isFalse(utilisateurDTO.getVraiOuFauxEtabSaisieSimplifiee())) {
               listeSousMenu = new ArrayList<MenuAction>();
               this.listeAction.add(new MenuAction("sequence.png", "", "Séquences", listeSousMenu));
               listeSousMenu.add(new MenuAction("sequenceAjout.png", Outil.AJOUT_SEQUENCE.name(), "Ajout de séquence", null));
               listeSousMenu.add(new MenuAction("sequenceRecherche.png", Outil.RECH_SEQUENCE.name(), "Recherche/Modification", null));
            } /*
            inutile pour les utilisateurs qui utilisent les séquences automatiques. cd le 18/06/2014
            else {
               this.listeAction.add(new MenuAction("sequenceRecherche.png",Outil.RECH_SEQUENCE.name() , "Recherche de séquences", null));
            }
           */
            // 6. Seance
 /*           listeSousMenu = new ArrayList<MenuAction>();
            this.listeAction.add(new MenuAction("seance.png", "", "Séance", listeSousMenu));
            listeSousMenu.add(new MenuAction("seanceAjout.png", Outil.AJOUT_SEANCE.name(), "Ajout de séance", null));
            listeSousMenu.add(new MenuAction("seanceRecherche.png", Outil.RECH_SEANCE.name(), "Recherche/Modification", null));
*/
            // 7. Carnet de bord
            listeSousMenu = new ArrayList<MenuAction>();
            this.listeAction.add(new MenuAction("carnet.png", "", "Carnet de bord", listeSousMenu));
            listeSousMenu.add(new MenuAction("carnetAdd.png", Outil.AJOUT_CYCLE.name(), "Ajout d'une séquence pédagogique", null));
            listeSousMenu.add(new MenuAction("carnetFind.png", Outil.RECH_CYCLE.name(), "Recherche/Modification", null));
            listeSousMenu.add(new MenuAction("GestionGrp32.png", Outil.GROUPES_COL_LOCAUX.name(), "Groupes Collaboratifs Locaux", null));

            // 8. Mes pièces jointes
            this.listeAction.add(new MenuAction("monDepot.png", Outil.DEPOT.name(), "Mes pièces jointes", null));
            
            // 9. Gestion des remplacements
            this.listeAction.add(new MenuAction("changeEnseignant.png", Outil.REMPLACEMENT_ENSEIGNANT.name(), "Gestion des remplacements", null));
            
            // Archives
       //     this.listeAction.add(new MenuAction("archive.png", Outil.CAHIER_ARCHIVE.name(), "Archives", null));  
            listeSousMenu = new ArrayList<MenuAction>();
            this.listeAction.add(new MenuAction("archiveMenu.png","", "Archives",listeSousMenu));
            listeSousMenu.add(new MenuAction("cahierTexteEditionSeance.png", Outil.ARCHIVE_SEANCE.name(), "Détail/PDF des séances par classe", null));
            listeSousMenu.add(new MenuAction("cahierTexteEditionSequence.png", Outil.ARCHIVE_SEQUENCE.name(), "Détail/PDF des séquences par classe",null));
         }
    }

    /**
     * Charger arbre Profil Directeur pour CRC.
     * @param utilisateurDTO les paramétres.
     */
    private void chargerArbreCRCDirecteur(final UtilisateurDTO utilisateurDTO) {
        if (isCahierAccessible(utilisateurDTO)) {

            // Visa
            List<MenuAction> listeSousMenu = new ArrayList<MenuAction>();
            this.listeAction.add(new MenuAction("visa.png", "", "Visas", listeSousMenu));
                listeSousMenu.add(new MenuAction("visaListe.png", Outil.VISA_LISTE.name(), "Liste des enseignants", null));
                listeSousMenu.add(new MenuAction("visaSeance.png", Outil.VISA_SEANCE.name(), "Liste des séances par enseignant", null));
            
            // Emploi du temps
            listeSousMenu = new ArrayList<MenuAction>();
            this.listeAction.add(new MenuAction("emploiTemps.png", "", "Emploi du temps", listeSousMenu));
                listeSousMenu.add(new MenuAction("emploiTempsClasse.png", Outil.CONSOLIDER_EMP.name(), "Par classe", null));
                listeSousMenu.add(new MenuAction("importEDT.png", Outil.IMPORTEDT.name(), "Importer", null));
            
            // Consultation Impression
            listeSousMenu = new ArrayList<MenuAction>();
            this.listeAction.add(new MenuAction("rechercheEdition.png", "", "Consultation-Impression par classe/groupe", listeSousMenu));
            listeSousMenu.add(new MenuAction("cahierTexteConsultation.png", Outil.SEANCE_SEMAINE.name(), "Séances par classe", null));
            listeSousMenu.add(new MenuAction("cahierTexteEditionSeance.png", Outil.PRINT_SEANCE.name(), "Détail/PDF des séances par classe", null));
            listeSousMenu.add(new MenuAction("cahierTexteEditionSequence.png", Outil.PRINT_SEQUENCE.name(), 
                    "Détail/PDF des séquences par classe",null));
                
            // Se mettre comme un eleve
            this.listeAction.add(new MenuAction("changeEleve.png", Outil.SIMULATION_ELEVE.name(), "Mode simulation élève", null));
            
            // Droit d'inspection
            this.listeAction.add(new MenuAction("inspection.png", Outil.INSPECTION.name(), "Droits d'inspection", null));
            
            // Gestion des remplacements
            this.listeAction.add(new MenuAction("changeEnseignant.png", Outil.REMPLACEMENT_ENSEIGNANT.name(), "Gestion des remplacements", null));
            
            // Archives
            this.listeAction.add(new MenuAction("archive.png", Outil.CAHIER_ARCHIVE.name(), "Archives", null));
            
        }
    }

    /**
     * Le cahier est-il accessile.
     *
     * @param utilisateurDTO les paramètres.
     *
     * @return true ou false.
     */
    private Boolean isCahierAccessible(final UtilisateurDTO utilisateurDTO) {
        return BooleanUtils.isTrue(utilisateurDTO.getAnneeScolaireDTO()
                                                 .getVraiOuFauxCahierOuvertENT()) &&
               BooleanUtils.isTrue(utilisateurDTO.getVraiOuFauxCahierOuvertEtab());
    }

    /**
     * Accès au cahier en administrateur.
     * @param utilisateurDTO les paramétres.
     */
    private void addNodeAdmin(final UtilisateurDTO utilisateurDTO) {
        final Set<String> listeSirenAdm = utilisateurDTO.getAdminLocalSiren();
        final Set<String> listeSirenRes = utilisateurDTO.getAdminRessourceSiren();
        final String sirenSel = utilisateurDTO.getSirenEtablissement();
        
        //Si l'utilisateur est administrateur
        if (listeSirenAdm.contains(sirenSel) || listeSirenRes.contains(sirenSel) ||
                BooleanUtils.isTrue(utilisateurDTO.getVraiOuFauxAdmRessourceENT()) ||
                BooleanUtils.isTrue(utilisateurDTO.getVraiOuFauxAdmCentral())) {
            
            //Si le cahier de l'établissement est ouvert
            if (isCahierAccessible(utilisateurDTO)) {
                if((!utilisateurDTO.getProfil().equals(Profil.ENSEIGNANT)) &&
                        (!utilisateurDTO.getProfil().equals(Profil.DIRECTION_ETABLISSEMENT)) &&
                        (!utilisateurDTO.getProfil().equals(Profil.DOCUMENTALISTE))){
                    
                    final List<MenuAction> listeSousMenu = new ArrayList<MenuAction>();
                    this.listeAction.add(new MenuAction("emploiTemps.png", "", "Emploi du temps", listeSousMenu));
                    listeSousMenu.add(new MenuAction("importEDT.png", Outil.IMPORTEDT.name(), "Importer", null));
                }
            }
            this.listeAction.add(new MenuAction("admin.png", Outil.ADMIN.name(), "Administration", null));
        }
    }

    /**
     * Reset.
     */
    public void reset() {
        this.listeAction = new ArrayList<MenuAction>();
    }

    /**
     * Retourne le profil de l'utilisateur en chaine.
     * @return le profil de l'utilisateur en chaine.
     */
    public String getProfilChaine() {
        final Profil profil = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getProfil();
        return (profil != null) ? profil.name() : null;
    }



    /**
     * Accesseur de listeAction {@link #listeAction}.
     * @return retourne listeAction
     */
    public List<MenuAction> getListeAction() {
        return listeAction;
    }

    /**
     * Mutateur de listeAction {@link #listeAction}.
     * @param listeAction le listeAction to set
     */
    public void setListeAction(List<MenuAction> listeAction) {
        this.listeAction = listeAction;
    }
    
    

}
