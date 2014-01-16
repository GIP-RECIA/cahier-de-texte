package org.crlr.web.application.control.cycle;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.Outil;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignantsClasseGroupeQO;
import org.crlr.dto.application.base.GenericDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.cycle.CycleDTO;
import org.crlr.dto.application.cycle.CycleDevoirDTO;
import org.crlr.dto.application.cycle.CycleSeanceDTO;
import org.crlr.dto.application.cycle.RechercheCycleSeanceQO;
import org.crlr.dto.application.cycle.TypeDateRemise;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.services.CycleService;
import org.crlr.services.DevoirService;
import org.crlr.services.GroupeClasseService;
import org.crlr.services.ImagesServlet;
import org.crlr.web.application.control.AbstractPopupControl;
import org.crlr.web.application.control.PopupPiecesJointesControl;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.utils.MessageUtils;
import org.crlr.web.utils.NavigationUtils;

/**
 * Controleur associe au formulaire de creation/ajout/consultation d'un cycle.
 * @author G-SAFIR-FRMP
 */
@ManagedBean(name = "ajoutCycle")
@ViewScoped
public class AjoutCycleControl extends AbstractPopupControl<AjoutCycleForm> {

    /** Identifiant du controleur. Sert lors du retour d'une navigation vers un sous ecran. */
    private final static String IDFORM = AjoutCycleControl.class.getName() + "form";
    
    /** Controleur de piece jointe. */
    @ManagedProperty(value = "#{popupPiecesJointes}")
    private transient PopupPiecesJointesControl popupPiecesJointesControl;

    /** groupeClasseService. */
    @ManagedProperty(value = "#{groupeClasseService}")
    private transient GroupeClasseService groupeClasseService;

    /** cycleService. */
    @ManagedProperty(value = "#{cycleService}")
    private transient CycleService cycleService;

    /** Service Devoir. */
    @ManagedProperty(value = "#{devoirService}")
    private transient DevoirService devoirService;

    
    /** Le logger. */
    protected static final Log log = LogFactory.getLog(AjoutCycleControl.class);

    /**
     * Mutateur de popupPiecesJointesControl {@link #popupPiecesJointesControl}.
     * @param popupPiecesJointesControl le popupPiecesJointesControl to set
     */
    public void setPopupPiecesJointesControl(
            PopupPiecesJointesControl popupPiecesJointesControl) {
        this.popupPiecesJointesControl = popupPiecesJointesControl;
    }
    
    /**
     * Mutateur de groupeClasseService {@link #groupeClasseService}.
     * @param groupeClasseService le groupeClasseService to set
     */
    public void setGroupeClasseService(GroupeClasseService groupeClasseService) {
        this.groupeClasseService = groupeClasseService;
    }


    /**
     * Mutateur de cycleService {@link #cycleService}.
     * @param cycleService le cycleService to set
     */
    public void setCycleService(CycleService cycleService) {
        this.cycleService = cycleService;
    }

    
    /**
     * Mutateur de devoirService {@link #devoirService}.
     * @param devoirService le devoirService to set
     */
    public void setDevoirService(DevoirService devoirService) {
        this.devoirService = devoirService;
    }

    // *************************************************************************
    // Construction / Initialisation 
    // *************************************************************************
    /**
     * @param form
     */
    public AjoutCycleControl() {
        super(new AjoutCycleForm());
    }

    /**
     * {@inheritDoc}
     */    
    @PostConstruct
    public void onLoad() {
        
       // Gestion du retour depuis un sous ecran
        final AjoutCycleForm formSave =
            (AjoutCycleForm) ContexteUtils.getContexteOutilControl()
                                               .recupererEtSupprimerObjet(IDFORM);
        if (formSave == null) {
            initFormulaire();
        } else {
            form = formSave;
        }
    }

    /**
     * Initialise le formulaire pour une création (ou modification).
     */
    private void initFormulaire() {
        form.reset();
        
        // Recupere l'utilisateur
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        
        // Charge la liste de type de devoir
        final List<TypeDevoirDTO> listeTypeDevoir = devoirService.findListeTypeDevoir(utilisateurDTO.getIdEtablissement()).getValeurDTO();
        form.setListeTypeDevoir(listeTypeDevoir);
        
        // Charge la liste des dates de remise
        final List<TypeDateRemise> listeDateRemise = TypeDateRemise.loadListeTypeDateRemise();
        form.setListeDateRemise(listeDateRemise);
        
        // Verifie le contexte : ajout ou modification/consultation 
        final CycleDTO cycleDTO = (CycleDTO) ContexteUtils
                .getContexteOutilControl().recupererEtSupprimerObjet(
                        RechercheCycleControl.class.getName()); 

        // Contexte d'ajout
        if (cycleDTO == null) {
            
            // Recupere l'utilisateur qui est un enseignant : le createur du cycle
            final EnseignantDTO enseignant = new EnseignantDTO();
            enseignant.setId(utilisateurDTO.getUserDTO().getIdentifiant());
            enseignant.setUid(utilisateurDTO.getUserDTO().getUid());
            form.getCycle().setEnseignantDTO(enseignant);
            form.setAfficheRetour(false);
            
        // Contexte de consultation / modification 
        } else {
            
            // Charge les seances du cycle
            final RechercheCycleSeanceQO rechercheCycleSeanceQO = new RechercheCycleSeanceQO();
            rechercheCycleSeanceQO.setCycleDTO(cycleDTO);
            rechercheCycleSeanceQO.setAvecDetail(false);
            final List<CycleSeanceDTO> listeSeance = cycleService.findListeCycleSeance(rechercheCycleSeanceQO);
            cycleDTO.setListeSeance(listeSeance);
            
            form.setCycle(cycleDTO);
            form.setAfficheRetour(true);
            
            
        }            

        // Charge les groupes collaboratifs
       initialiseListeGroupe(utilisateurDTO, form.getCycle());        
    }
    
    /**
     * Verifie si le groupe fait partie de ceux du cycle.
     * @param groupe le groupe 
     * @param cycleDTO le cycle 
     * @return true / false.
     */
    private Boolean cycleCointainsGroupe(final GroupeDTO groupe, final CycleDTO cycleDTO) {
        for (final GroupeDTO groupeCycle : cycleDTO.getListeGroupe()) {
            if (groupeCycle.getId().equals(groupe.getId())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Initialise la liste des groupes collaboratifs.
     * @param utilisateurDTO l'utilisateur
     * @param cycle le cycle
     */
    private void initialiseListeGroupe(final UtilisateurDTO utilisateurDTO, final CycleDTO cycle) {
        final List<GroupeDTO> listeGroupe = groupeClasseService.findGroupesCollaboratifEnseignant(utilisateurDTO.getUserDTO().getIdentifiant());
        form.setListeGroupe(listeGroupe);
        
        final List<GenericDTO<Boolean, GroupeDTO>> listeGroupeSelection = new ArrayList<GenericDTO<Boolean,GroupeDTO>>();
        for (final GroupeDTO groupe : listeGroupe) {
            final GenericDTO<Boolean,GroupeDTO> groupeSelection = new GenericDTO<Boolean, GroupeDTO>();
            final Boolean utilise = cycleCointainsGroupe(groupe, cycle);
            groupeSelection.setValeur1(utilise);
            groupeSelection.setValeur2(groupe);
            listeGroupeSelection.add(groupeSelection);
        }
        form.setListeGroupeSelection(listeGroupeSelection);
    }
    
    /**
     * Compte le nombre de groupes selectionnes.
     * @return le nombre
     */
    private Integer getNombreGroupeSelected() {
        Integer count = form.getCycle().getListeGroupe().size();
        return count;
    }

    /**
     * Recherche dans une liste l'indice de la seance par son id.
     * @param listeSeance la liste
     * @param idSeance l'id recherche 
     * @return l'indice dans la liste.
     */
    private Integer getIndexSeanceById(final List<CycleSeanceDTO> listeSeance, final Integer idSeance) {
        for (Integer i=0; i< listeSeance.size(); i++) {
            final CycleSeanceDTO seance=listeSeance.get(i); 
            if (seance.getId().equals(idSeance)) {
                return i;
            }
        }
        return null;
    }
    
    /**
     * Charge la liste des enseignants membre d'un groupe.
     * @param idGroupe id du groupe
     * @return une liste d'enseignant.
     */
    private List<EnseignantDTO> chargeListeEnseignantGroupe(final Integer idGroupe) {
        final EnseignantsClasseGroupeQO rechercheQO = new EnseignantsClasseGroupeQO();
        rechercheQO.setIdGroupeClasse(idGroupe);
        final List<EnseignantDTO> liste = groupeClasseService.findEnseignantsGroupe(rechercheQO); 
        return liste;
    }
    
    // *************************************************************************
    // Les getter sur le controleur 
    // *************************************************************************
    /**
     * Retourne le libellé à afficher concernant la collaboration du cycle.
     * @return un libellé.
     */
    public String getLibelleListeGroupe() {
        Integer count = getNombreGroupeSelected();
        if (count.equals(0)) {
            return "Aucun partage";
        } else if (count.equals(1)) {
            return "Séquence pédagogique partagée avec les enseignants d'un groupe";
        } else {
            return "Séquence pédagogique partagée avec les enseignants de " + count.toString() + " groupes";
        }
    }
    
    /**
     * Determine le tool tip a afficher sur le partage.
     * @return le libelle
     */
    public String getLibelleListeGroupeTip() {
        String result = null;
        // for (final GenericDTO<Boolean, GroupeDTO> groupe : form.getListeGroupeSelection()) {
        for (final GroupeDTO groupe : form.getCycle().getListeGroupe()) {
            if (result == null) {
                result = "<b>Partage avec les enseignants des groupes :</b>";
            }
            result += "<br/> - " + groupe.getIntitule();
        }
        if (result == null) {
            result = "<b>Aucun partage avec d'autres enseignants</b>";
        }
        return result;
    }
    
    // *************************************************************************
    // Methode invoquees depuis le formulaire 
    // *************************************************************************
    /**
     * Sauvergarde la sequence pedagogique.
     */
    public void sauver() {
        try {
            final ResultatDTO<Integer> resultat = cycleService.saveCycle(form.getCycle());
            log.debug("Creation du cycle id = {0}", resultat.getValeurDTO());
            form.resetChampsObligatoire();
        } catch (final MetierException e) {
            form.setListeChampsObligatoire(MessageUtils.getAllCodeMessage(e.getConteneurMessage()));
            log.debug("{0}", e.getMessage());
        }
    }
    
    /**
     * Supprime la sequence pedagogique et toutes ses seances.
     */
    public void supprimer() {
        try {
            cycleService.deleteCycle(form.getCycle());
            initFormulaire();
        } catch (final MetierException e) {
            form.setListeChampsObligatoire(MessageUtils.getAllCodeMessage(e.getConteneurMessage()));
            log.debug("{0}", e.getMessage());
        }
    }
    
    /**
     * Initialise les checked des groupes du cycle.
     */
    public void initSelectionGroupe() {
        for (final GenericDTO<Boolean, GroupeDTO> groupe : form.getListeGroupeSelection()) {
            if (cycleCointainsGroupe(groupe.getValeur2(), form.getCycle())) {
                groupe.setValeur1(true); 
            } else {
                groupe.setValeur1(false);
            }
        }
    }
    
    /**
     * Modifie la selection des groupes selectionne dans la liste des groupes du cycle.
     */
    public void ajouterGroupe() {
        final List<GroupeDTO> listeGroupe = new ArrayList<GroupeDTO>();
        for (final GenericDTO<Boolean, GroupeDTO> groupe : form.getListeGroupeSelection()) {
            if (BooleanUtils.isTrue(groupe.getValeur1())) {
                listeGroupe.add(groupe.getValeur2());
            }
        }
        form.getCycle().setListeGroupe(listeGroupe);
    }
    
    /**
     * Prepare l'edition dans la popup d'une nouvelle seance.
     */
    public void ajouterSeance() {
        final CycleSeanceDTO seance = new CycleSeanceDTO();
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        final EnseignantDTO enseignant = new EnseignantDTO();
        enseignant.setId(utilisateurDTO.getUserDTO().getIdentifiant());
        enseignant.setUid(utilisateurDTO.getUserDTO().getUid());
        seance.setEnseignantDTO(enseignant);

        // Creation d'un devoir par defaut 
        final CycleDevoirDTO devoir = new CycleDevoirDTO();
        seance.getListeCycleDevoir().add(devoir);
        
        // Positionne le cycle de cette seance
        seance.setIdCycle(form.getCycle().getId());
        
        form.setCycleSeanceSelected(seance);
    }

    /**
     * Appeler lors de l'affichage de la popup de tri.
     * Initialise une liste temp pour gérer le tri.
     */
    public void initTrierListeSeance() {
        final List<CycleSeanceDTO> listeSeanceTri = new ArrayList<CycleSeanceDTO>();
        for (final CycleSeanceDTO seance : form.getCycle().getListeSeance()) {
            listeSeanceTri.add(seance);
        }
        form.setListeSeanceTri(listeSeanceTri);
    }

    /**
     * Repporte le tri de la liste temp dans la liste des seance du form et 
     * effectue la persistance.
     */
    public void trierListeSeance() {
        final List<CycleSeanceDTO> listeSeanceTri = new ArrayList<CycleSeanceDTO>();
        Integer i = 1;
        for (final CycleSeanceDTO seance : form.getListeSeanceTri()) {
            seance.setIndice(i);
            listeSeanceTri.add(seance);
            i++;
        }
        form.getCycle().setListeSeance(listeSeanceTri);

        // Fait la sauvegarde
        try {
            cycleService.saveCycleOrdreSeance(form.getCycle());
        } catch (MetierException e) {
            log.error("Erreur trierListeSeance: " + e.getMessage());
        }
    }
    
    
    /**
     * Appelle suite a un drop d'element sur une tr de la liste des seances à trier.
     */
    public void deplacerSeance() {
        final String idClassDrag = form.getIdClassDrag();
        final String idClassDrop = form.getIdClassDrop();
        
        if (StringUtils.isEmpty(idClassDrag) || StringUtils.isEmpty(idClassDrop)) {
            return;
        }
        Integer idDrag = null;
        String[] tabDrag = idClassDrag.split(" ");
        for(final String mot : tabDrag) {
            if (mot.startsWith("idDragTri_")) {
                idDrag = Integer.parseInt(mot.substring(10));
            }
        }
        
        Integer idDrop = null;
        String[] tabDrop = idClassDrop.split(" ");
        for(final String mot : tabDrop) {
            if (mot.startsWith("idDropTri_")) {
                idDrop = Integer.parseInt(mot.substring(10));
            }
        }
        
        if (idDrag == null || idDrop == null || idDrag.equals(idDrop)) {
            return;
        }
        
        final Integer indiceDrag = getIndexSeanceById(form.getListeSeanceTri(), idDrag);
        if (indiceDrag != null) {
            final CycleSeanceDTO seance = form.getListeSeanceTri().get(indiceDrag);
            form.getListeSeanceTri().remove(indiceDrag.intValue());
            final Integer indiceDrop = getIndexSeanceById(form.getListeSeanceTri(), idDrop);
            if (indiceDrop != null) {
                int ie = indiceDrop.intValue() + 1;
                form.getListeSeanceTri().add(ie, seance);
            } else {
                form.getListeSeanceTri().add(seance);
            }
        }
    }

    
    /** 
     * Navigation vers les souc-ecran d'application d'un cycle.
     * @return la chaine de navigation.
     */
    public String appliquerCycle() {
        if (form.getCycle() != null)  {
            ContexteUtils.getContexteOutilControl().mettreAJourObjet(IDFORM, form);
            return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(
                    Outil.APPLY_CYCLE,
                    RechercheCycleControl.class.getName(),
                    form.getCycle());            
        } else {
            return null;
        }
    }
    
    /**
     * Prepare l'edition dans la popup d'une nouvelle seance.
     */
    public void afficherSeance() {
        final CycleSeanceDTO seance = form.getCycleSeanceSelected();
        if (seance != null) {
            log.debug("afficherSeance : {0}", seance.getId());
            cycleService.completeInfoSeance(seance);
        }
    }
    
    /** 
     * Supprime une seance. 
     */
    public void deleteSeance() {
        final CycleSeanceDTO seance = form.getCycleSeanceSelected();
        if (seance == null) {
            return;
        }
        try {
            final ResultatDTO<Boolean> resultat = cycleService.deleteCycleSeance(seance);
            form.getCycle().getListeSeance().remove(seance);
            log.debug("Suppression de la seance cycle id = {0}", resultat.getValeurDTO());
        } catch (final MetierException e) {
            log.debug("{0}", e.getMessage());
        }
    }

    /**
     * Sauve la seance affichee dans la popup.
     */
    public void sauverSeance() {
        final CycleSeanceDTO seance = form.getCycleSeanceSelected();
        if (seance == null) {
            return;
        }
        try {
            final Boolean modeAjout = seance.getId() == null;
            if (modeAjout) {
                seance.setIndice(form.getCycle().getListeSeance().size() +1);
            }
            final ResultatDTO<Integer> resultat = cycleService.saveCycleSeance(seance);
            log.debug("Creation de la seance cycle id = {0}", resultat.getValeurDTO());
            form.resetChampsObligatoire();
            
            if (modeAjout) {
                form.getCycle().getListeSeance().add(seance);
                seance.setId(resultat.getValeurDTO());
            }
            seance.setDescriptionHTML(ImagesServlet.genererLatexImage(seance.getDescription()));
            seance.setAnnotationsHTML(ImagesServlet.genererLatexImage(seance.getAnnotations()));
            
        } catch (final MetierException e) {
            form.setListeChampsObligatoire(MessageUtils.getAllCodeMessage(e.getConteneurMessage()));
            log.debug("{0}", e.getMessage());
        }
    }
    
    /**
     * Charge sous forme d'image/html la description de la seance.
     * Cette méthode est utilisée lors du basculement en vue visulation/modification 
     */
    public void chargeImagesLatex() {
        final CycleSeanceDTO seance = form.getCycleSeanceSelected();
        if (seance!=null) {
            seance.setDescriptionHTML(ImagesServlet.genererLatexImage(seance.getDescription()));
        }
    }
    
    /**
     * Charge sous forme d'image/html la description de la seance.
     * Cette méthode est utilisée lors du basculement en vue visulation/modification 
     */
    public void chargeImagesLatexAnnotations() {
        final CycleSeanceDTO seance = form.getCycleSeanceSelected();
        if (seance!=null) {
            seance.setAnnotationsHTML(ImagesServlet.genererLatexImage(seance.getAnnotations()));
        }
    }
    
    /**
     * Charge sous forme d'image/html la description du devoir.
     * Cette méthode est utilisée lors du basculement en vue visulation/modification 
     */
    public void chargeImagesLatexDevoir() {
        final CycleDevoirDTO devoir = form.getCycleDevoirSelected();
        if (devoir!=null) {
            devoir.setDescriptionHTML(ImagesServlet.genererLatexImage(devoir.getDescription()));
        }
    }
    
    /**
     * Ajoute une nouvelle piece jointe. Cette méthode est appelée lors du clic
     * sur l'icone d'affichage de la popup piece jointe.
     */
    public void ajouterPieceJointeSeance() {
        // A a priori, au momen de cliquer sur le lien, il n'y a rien a faire.
        final String raffraichirTabAfterUpload = form.getRaffraichirTabAfterUpload();
        if (raffraichirTabAfterUpload != null) {
            log.debug("Déclenche l'ouverture de la popup des pieces jointe pour seance");
        }
    }

    /**
     * Ajoute une nouvelle piece jointe. Cette méthode est appelée lors du clic
     * sur l'icone d'affichage de la popup piece jointe.
     */
    public void ajouterPieceJointeDevoir() {
        // A a priori, au momen de cliquer sur le lien, il n'y a rien a faire.
        final String raffraichirTabAfterUpload = form.getRaffraichirTabAfterUpload();
        if (raffraichirTabAfterUpload != null) {
            log.debug("Déclenche l'ouverture de la popup des pieces jointe pour devoir");
        }
    }
    
    /**
     * Cette méthode es appellee par la popup piece jointe suite à un upload de
     * fichier.
     */
    public void afterAjouterPieceJointeSeance() {
        final FileUploadDTO file = popupPiecesJointesControl.getForm().getFileUploade();
        if (file != null) {
            final CycleSeanceDTO seance = form.getCycleSeanceSelected(); 
            if (seance != null) {
                if (CollectionUtils.isEmpty(seance.getListePieceJointe())) {
                    seance.setListePieceJointe(new ArrayList<FileUploadDTO>());
                }
                seance.getListePieceJointe().add(file);
            }
        }
    }

    /**
     * Cette méthode es appellee par la popup piece jointe suite à un upload de
     * fichier.
     */
    public void afterAjouterPieceJointeDevoir() {
        final FileUploadDTO file = popupPiecesJointesControl.getForm().getFileUploade();
        if (file != null) {
            final CycleDevoirDTO devoir = form.getCycleDevoirSelected(); 
            if (devoir != null) {
                if (CollectionUtils.isEmpty(devoir.getListePieceJointe())) {
                    devoir.setListePieceJointe(new ArrayList<FileUploadDTO>());
                }
                devoir.getListePieceJointe().add(file);
            }
        }
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
     * Suppression de la piece jointe associee a une seance.
     */
    public void deletePieceJointeSeance() {
        final CycleSeanceDTO seance = form.getCycleSeanceSelected();
        if (seance == null) {
            return;
        }
        final FileUploadDTO fichier = form.getPieceJointeASupprimer();
        if (fichier == null) {
            return;
        }
        if (!CollectionUtils.isEmpty(seance.getListePieceJointe())) {
            seance.getListePieceJointe().remove(fichier);
        }
    }
    
    /**
     * Suppression de la piece jointe associee a un devoir.
     */
    public void deletePieceJointeDevoir() {
        final CycleDevoirDTO devoir = form.getCycleDevoirSelected();
        if (devoir == null) {
            return;
        }
        final FileUploadDTO fichier = form.getPieceJointeASupprimer();
        if (fichier == null) {
            return;
        }
        if (!CollectionUtils.isEmpty(devoir.getListePieceJointe())) {
            devoir.getListePieceJointe().remove(fichier);
        }
    }
    
    /**
     * Ajoute un devoir vide sur la seance en cours d'edition.
     */
    public void ajouterDevoir() {
        final CycleSeanceDTO seance = form.getCycleSeanceSelected();
        if (seance == null) {
            return;
        }
        final CycleDevoirDTO devoir = new CycleDevoirDTO();
        seance.getListeCycleDevoir().add(devoir);
    }
    
    /**
     * Supprime un devoir sur la seance en cours d'edition.
     */
    public void deleteDevoir() {
        final CycleSeanceDTO seance = form.getCycleSeanceSelected();
        if (seance == null) {
            return;
        }
        final CycleDevoirDTO devoir = form.getCycleDevoirSelected();
        seance.getListeCycleDevoir().remove(devoir);
    }

    /**
     * Affiche la liste des enseignant d'un groupe.
     */
    public void afficheEnseignantGroupe() {
        if (form.getGroupeSelected() == null || form.getGroupeSelected().getId()==null) {
            return;
        }
        final List<EnseignantDTO> listeEnseignant = chargeListeEnseignantGroupe(form.getGroupeSelected().getId());
        form.setListeEnseignant(listeEnseignant);
    }
}
