/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AjoutSeanceControl.java,v 1.44 2010/06/02 12:41:27 ent_breyton Exp $
 */

package org.crlr.web.application.control.visa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang.BooleanUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.Outil;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.inspection.DroitInspecteurDTO;
import org.crlr.dto.application.inspection.RechercheDroitInspecteurQO;
import org.crlr.dto.application.visa.RechercheVisaQO;
import org.crlr.dto.application.visa.TypeVisa;
import org.crlr.dto.application.visa.VisaDTO;
import org.crlr.dto.application.visa.VisaDTO.VisaProfil;
import org.crlr.dto.application.visa.VisaEnseignantDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.VisaFacadeService;
import org.crlr.services.InspectionService;
import org.crlr.services.VisaService;
import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.control.AbstractPopupControl;
import org.crlr.web.application.control.seance.SaisirSeanceControl;
import org.crlr.web.application.form.visa.VisaListeForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.utils.NavigationUtils;

/**
 * AjoutSeanceControl.
 * 
 * @author $author$
 * @version $Revision: 1.44 $
 */
@ManagedBean(name = "visaListe")
@ViewScoped
public class VisaListeControl extends AbstractPopupControl<VisaListeForm>
        implements Comparator<VisaEnseignantDTO> {

    /** Id de la classe. */
    public final static String IDFORM = VisaListeControl.class.getName()
            + "form";

    /** Controleur de inspectionService. */
    @ManagedProperty(value = "#{inspectionService}")
    private transient InspectionService inspectionService;

    /**
     * Mutateur de inspectionService {@link #inspectionService}.
     * @param inspectionService le inspectionService to set
     */
    public void setInspectionService(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    /** Controleur de visaService. */
    @ManagedProperty(value = "#{visaFacade}")
    private transient VisaFacadeService visaService;

    /**
     * Mutateur de visaService {@link #visaService}.
     * @param visaService le visaService to set
     */
    public void setVisaService(VisaFacadeService visaService) {
        this.visaService = visaService;
    }

    /**
     * Controleur de la saisie par semaine pour acceder aux fonctionnalités
     * agenda d'un enseignant.
     */
    @ManagedProperty(value = "#{saisirSeance}")
    private transient SaisirSeanceControl saisirSeanceControl;

    /**
     * Mutateur de saisirSeanceControl {@link #saisirSeanceControl}.
     * @param saisirSeanceControl le saisirSeanceControl to set
     */
    public void setSaisirSeanceControl(SaisirSeanceControl saisirSeanceControl) {
        this.saisirSeanceControl = saisirSeanceControl;
    }

    /**
     * Constructeur.
     */
    public VisaListeControl() {
        super(new VisaListeForm());
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    public void onLoad() {

        form.setTexteAide(visaService.getAideContextuelleListe());
        
        // permet de faire un seule appel métier durant le cycle de l'outil.
        // valable pour les outils dont les paramètres ne sont pas saisies par
        // l'utilisateur.
        final VisaListeForm formSave = (VisaListeForm) ContexteUtils
                .getContexteOutilControl().recupererEtSupprimerObjet(IDFORM);
        if (formSave == null) {
            reset();
        } else {
            form = formSave;
        }
    }

    /**
     * Methode appelee une seule fois lors du premier arrivee sur l'outil.
     */
    private void reset() {

        // Charge la liste des enseignants de l'utilisateur connecte
        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();
        final Profil profilUser = utilisateurDTO.getProfil();

        final Integer idEtablissement = utilisateurDTO.getIdEtablissement();
        
        // Le directeur voit touts les enseignants de son etablissement
        if (Profil.DIRECTION_ETABLISSEMENT == profilUser) {            
            form.setProfilVisaUser(VisaProfil.ENTDirecteur); 
        } else if (Profil.INSPECTION_ACADEMIQUE.equals(profilUser)) {
            form.setProfilVisaUser(VisaProfil.ENTInspecteur);             
        } 
        
        final List<EnseignantDTO> listeEnseignant =
                visaService.findListeEnseignant(utilisateurDTO).getValeurDTO();

        // Charge pour chaque enseignant la liste des visa / cahiers de texte
        form.setListeEnseignant(listeEnseignant);
        final ResultatDTO<List<VisaEnseignantDTO>> resultat = visaService
                .findListeVisaEnseignant(profilUser, idEtablissement,listeEnseignant);
        final List<VisaEnseignantDTO> listeVisaEnseignant = resultat
                .getValeurDTO();
        form.setListeVisaEnseignant(ObjectUtils.clone(listeVisaEnseignant));
        form.setListeVisaEnseignantBdd(listeVisaEnseignant);

        trierListe();
    }

    /**
     * Appelee lors du clic sur le plus d'une ligne enseignant. Deplie la zone
     * correspondant a l'enseignant.
     */
    public void deplierEnseignant() {
        final VisaEnseignantDTO visaEnseignant = form
                .getVisaEnseignantSelected();
        if (visaEnseignant != null) {
            visaEnseignant.setVraiOuFauxCollapse(false);
            visaEnseignant.getEnseignant();
            if (visaEnseignant.getListeVisa() == null
                    || visaEnseignant.getListeVisa().isEmpty()) {
                final RechercheVisaQO rechercheVisa = new RechercheVisaQO();
                rechercheVisa.setIdEnseignant(visaEnseignant.getEnseignant()
                        .getId());
                final UtilisateurDTO utilisateurDTO = ContexteUtils
                        .getContexteUtilisateur().getUtilisateurDTO();
                final Integer idEtablissement = utilisateurDTO
                        .getIdEtablissement();
                rechercheVisa.setIdEtablissement(idEtablissement);
                if (Profil.DIRECTION_ETABLISSEMENT.equals(utilisateurDTO
                        .getProfil())) {
                    rechercheVisa.setProfil(VisaProfil.ENTDirecteur);
                } else if (Profil.INSPECTION_ACADEMIQUE.equals(utilisateurDTO
                        .getProfil())) {
                    rechercheVisa.setProfil(VisaProfil.ENTInspecteur);
                }
                final ResultatDTO<List<VisaDTO>> resultat = visaService
                        .findListeVisa(rechercheVisa);
                final List<VisaDTO> listeVisa = resultat.getValeurDTO();
                visaEnseignant.setListeVisa(ObjectUtils.clone(listeVisa));
            }
        }
    }

    /**
     * Coquille vide utilise par le lien de idRefreshListe.
     */
    public void refresh() {
    }

    /**
     * Appelee lors du clic sur le moins d'une ligne enseignant. Remplie la zone
     * correspondant a l'enseignant.
     */
    public void replierEnseignant() {
        final VisaEnseignantDTO visaEnseignant = form
                .getVisaEnseignantSelected();
        if (visaEnseignant != null) {
            visaEnseignant.setVraiOuFauxCollapse(true);
        }
    }

    /**
     * Appelee pour l'ajout d'un visa de type simple sur un cahier de texte
     * d'enseignant.
     */
    public void addVisaSimple() {
        final VisaDTO visa = form.getVisaSelected();
        visa.setDateVisa(new Date());
        visa.setTypeVisa(TypeVisa.SIMPLE);
        visa.setEstModifie(true);
        caluleDernierVisa();
    }

    /**
     * Appelee pour l'ajout d'un visa de type memo sur un cahier de texte
     * d'enseignant.
     */
    public void addVisaMemo() {
        final VisaDTO visa = form.getVisaSelected();
        visa.setDateVisa(new Date());
        visa.setTypeVisa(TypeVisa.MEMO);
        visa.setEstModifie(true);
        caluleDernierVisa();
    }

    /**
     * Appelee pour la suppression d'un visa sur un cahier de texte
     * d'enseignant.
     */
    public void delVisa() {
        final VisaDTO visa = form.getVisaSelected();
        visa.setDateVisa(null);
        visa.setTypeVisa(null);
        visa.setEstModifie(true);
        caluleDernierVisa();
    }

    /**
     * Rafraichit suite a un ajout ou suppression de visa les infos concernant
     * le dernier visa dans la liste principale.
     */
    private void caluleDernierVisa() {
        final VisaEnseignantDTO visaEnseignant = form
                .getVisaEnseignantSelected();
        if (visaEnseignant != null) {
            VisaDTO dernierVisa = null;
            for (final VisaDTO visa : visaEnseignant.getListeVisa()) {
                if (visa.getDateVisa() == null) {
                    continue;
                }
                if (dernierVisa == null
                        || (visa.getDateVisa().after(dernierVisa.getDateVisa()))) {
                    dernierVisa = visa;
                }
            }
            if (dernierVisa != null) {
                visaEnseignant.setDateDernierVisa(dernierVisa.getDateVisa());
                visaEnseignant.setTypeDernierVisa(dernierVisa.getTypeVisa());
                visaEnseignant.setProfilDernierVisa(dernierVisa.getProfil());
            } else {
                visaEnseignant.setDateDernierVisa(null);
                visaEnseignant.setTypeDernierVisa(null);
                visaEnseignant.setProfilDernierVisa(null);
            }
        }

    }

    /**
     * Appellée lors de la consultation de l'agenda d'un enseignant.
     * @throws MetierException l'exception  
     */
    public void consulterEnseignantAgenda() throws MetierException {

        final VisaEnseignantDTO visaEnseignant = form
                .getVisaEnseignantSelected();
        if (visaEnseignant != null) {
            saisirSeanceControl.getForm().setIdEnseignant(
                    visaEnseignant.getEnseignant().getId());
            saisirSeanceControl.selectionnerSemaine();
        }
    }
    
    public void processAjaxBehavior(javax.faces.event.AjaxBehaviorEvent event) throws javax.faces.event.AbortProcessingException {
        try {
        consulterEnseignantAgenda();
        } catch (MetierException ex) {
            log.error( "ex", ex);
        }
    }

    /**
     * Appelee pour la consultation du detail des seance d'un visa sur un cahier
     * de texte d'enseignant.
     * 
     * @return la cle de navigation vers le sous outil
     */
    public String afficheDetailVisa() {
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(VisaDTO.class.getName(), form.getVisaSelected());
        
        //ContexteUtils.getContexteOutilControl().mettreAJourObjet(IDFORM, form);
        
        return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(
                Outil.VISA_SEANCE, VisaDTO.class.getName(),
                ObjectUtils.clone(form.getVisaSelected()));
    }

    
    /**
     * Appelee pour la consultation du detail des seance d'un visa pour un enseignant.
     * 
     * @return la cle de navigation vers le sous outil
     */
    public String afficheEnseignantVisa() {
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(VisaEnseignantDTO.class.getName(), form.getVisaEnseignantSelected());
        
        return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(
                Outil.VISA_SEANCE, VisaEnseignantDTO.class.getName(),
                ObjectUtils.clone(form.getVisaEnseignantSelected()));
    }
    
    /**
     * Recupere dans la liste des VisaEnseignant du formulaire, la liste des visa 
     * correspondant au VisaEnseignant passé en parametre.
     * @param visaEnseignantSearch le VisaEnseignant recherche
     * @return la Liste de VisaDTO de cet enseignant.
     */
    private List<VisaDTO> recupererlisteVisaForm(
            VisaEnseignantDTO visaEnseignantSearch) {
        if (visaEnseignantSearch == null
                || visaEnseignantSearch.getEnseignant() == null
                || visaEnseignantSearch.getEnseignant().getId() == null) {
            return new ArrayList<VisaDTO>();
        }
        final Integer idSearch = visaEnseignantSearch.getEnseignant().getId();
        for (final VisaEnseignantDTO visaEnseignantDTO : form
                .getListeVisaEnseignant()) {
            if (visaEnseignantDTO.getEnseignant() != null
                    && idSearch.equals(visaEnseignantDTO.getEnseignant()
                            .getId())) {
                return visaEnseignantDTO.getListeVisa();
            }
        }
        return new ArrayList<VisaDTO>();
    }

    /**
     * Appelée pour la sauvegarde generale.
     */
    public void sauver() {
        final List<VisaDTO> listeVisa = new ArrayList<VisaDTO>();
        for (final VisaEnseignantDTO visaEnseignant : form
                .getListeVisaEnseignant()) {
            if (visaEnseignant.getListeVisa() == null) {
                continue;
            }
            
            for (final VisaDTO visa : visaEnseignant.getListeVisa()) {
                if (BooleanUtils.isTrue(visa.getEstModifie())) {
                    listeVisa.add(visa);
                }
            }
        
        }
        if (listeVisa.isEmpty()) {
            return;
        }
        
        try {
            final ResultatDTO<Boolean> resultatSauve = visaService
                    .saveListeVisa(listeVisa);

            if (BooleanUtils.isNotTrue(resultatSauve.getValeurDTO())) {
                return;
            }
            
            // Repositionne a "enregistre" tous les visas
            for (final VisaEnseignantDTO visaEnseignant : form
                    .getListeVisaEnseignant()) {
                if (visaEnseignant.getListeVisa() == null) {
                    continue;
                }
                
                for (final VisaDTO visa : visaEnseignant
                        .getListeVisa()) {
                    if (visa.getEstModifie()) {
                        visa.setEstModifie(false);
                        visa.setEstPerime(false);
                    }
                }
            
            }

            final UtilisateurDTO utilisateurDTO = ContexteUtils
                    .getContexteUtilisateur().getUtilisateurDTO();
            final Profil profilUser = utilisateurDTO.getProfil();

            final Integer idEtablissement = utilisateurDTO.getIdEtablissement();
            
            // Recharge la liste des visaEnseignant.
            final ResultatDTO<List<VisaEnseignantDTO>> resultat = visaService
                    .findListeVisaEnseignant(profilUser, idEtablissement, form.getListeEnseignant());
            final List<VisaEnseignantDTO> listeVisaEnseignant = resultat
                    .getValeurDTO();

            // Repositionne sur cette liste les visas déjà chargés
            for (final VisaEnseignantDTO visaEnseignant : listeVisaEnseignant) {
                visaEnseignant
                        .setListeVisa(recupererlisteVisaForm(visaEnseignant));
            }
            form.setListeVisaEnseignant(listeVisaEnseignant);

            // Positionne les visa courant en visa BDD
            form.setListeVisaEnseignantBdd(ObjectUtils
                    .clone(listeVisaEnseignant));
        
        } catch (MetierException ex) {
            log.debug( "ex", ex);
        }
    
    }

    /**
     * Appelée pour le recherchement de la page.
     */
    public void reload() {
        form.setListeVisaEnseignant(ObjectUtils.clone(form
                .getListeVisaEnseignantBdd()));
    }

    /**
     * Affichage la semaine precedente.
     * @throws MetierException  execept
     */
    public void naviguerSemainePrecedent() throws MetierException {
        BarreSemaineDTO semaine = saisirSeanceControl.getForm()
                .getSemaineSelectionne();
        if (semaine == null) {
            return;
        }
        if (saisirSeanceControl.getForm().getListeBarreSemaine() == null
                || saisirSeanceControl.getForm().getListeBarreSemaine()
                        .isEmpty()) {
            return;
        }
        final Integer i = saisirSeanceControl.getForm().getListeBarreSemaine()
                .indexOf(semaine);
        if (i != null && i > 0) {
            semaine = saisirSeanceControl.getForm().getListeBarreSemaine()
                    .get(i - 1);
            saisirSeanceControl.getForm().setSemaineSelectionne(semaine);
            saisirSeanceControl.selectionnerSemaine();
        }
    }

    /**
     * Affichage la semaine suivante.
     * @throws MetierException  execept
     */
    public void naviguerSemaineSuivant() throws MetierException {
        BarreSemaineDTO semaine = saisirSeanceControl.getForm()
                .getSemaineSelectionne();
        if (semaine == null) {
            return;
        }
        if (saisirSeanceControl.getForm().getListeBarreSemaine() == null
                || saisirSeanceControl.getForm().getListeBarreSemaine()
                        .isEmpty()) {
            return;
        }
        final Integer i = saisirSeanceControl.getForm().getListeBarreSemaine()
                .indexOf(semaine);
        if (i != null
                && i < saisirSeanceControl.getForm().getListeBarreSemaine()
                        .size() - 1) {
            semaine = saisirSeanceControl.getForm().getListeBarreSemaine()
                    .get(i + 1);
            saisirSeanceControl.getForm().setSemaineSelectionne(semaine);
            saisirSeanceControl.selectionnerSemaine();
        }
    }

    /**
     * Retourne le titre de la semaine courante.
     * 
     * @return le titr.
     */
    public String getTitreSemaine() {
        final BarreSemaineDTO semaine = saisirSeanceControl.getForm()
                .getSemaineSelectionne();
        if (semaine == null) {
            return "";
        }
        return "du " + DateUtils.format(semaine.getLundi()) + " au "
                + DateUtils.format(semaine.getDimanche());
    }

    /**
     * Verifie si la semaine precedente peut etre affichee ou non.
     * 
     * @return true / false
     */
    public Boolean getAfficheSemainePrecedente() {
        final BarreSemaineDTO semaine = saisirSeanceControl.getForm()
                .getSemaineSelectionne();
        if (semaine == null) {
            return false;
        }
        if (saisirSeanceControl.getForm().getListeBarreSemaine() == null
                || saisirSeanceControl.getForm().getListeBarreSemaine()
                        .isEmpty()) {
            return false;
        }
        if (semaine.equals(saisirSeanceControl.getForm().getListeBarreSemaine()
                .get(0))) {
            return false;
        }
        return true;
    }

    /**
     * Verifie si la semaine suivante peut etre affichee ou non.
     * 
     * @return true / false
     */
    public Boolean getAfficheSemaineSuivante() {
        final BarreSemaineDTO semaine = saisirSeanceControl.getForm()
                .getSemaineSelectionne();
        if (semaine == null) {
            return false;
        }
        if (saisirSeanceControl.getForm().getListeBarreSemaine() == null
                || saisirSeanceControl.getForm().getListeBarreSemaine()
                        .isEmpty()) {
            return false;
        }
        if (semaine.equals(saisirSeanceControl
                .getForm()
                .getListeBarreSemaine()
                .get(saisirSeanceControl.getForm().getListeBarreSemaine()
                        .size() - 1))) {
            return false;
        }
        return true;
    }

    /**
     * Declenche pour trier l'ordre de la liste des visa enseignant selon le tri
     * actuel.
     */
    public void trierListe() {
        final ArrayList<VisaEnseignantDTO> listeVisa = (ArrayList<VisaEnseignantDTO>) form
                .getListeVisaEnseignant();
        Collections.sort(listeVisa, this);

    }

    /**
     * Compare deux elements de la liste des visas selon l'ordre de tri en
     * cours.
     * 
     * @param o1
     *            visaEnseignantDTO
     * @param o2
     *            visaEnseignantDTO
     * @return -1 ou 0 ou 1
     */
    @Override
    public int compare(VisaEnseignantDTO o1, VisaEnseignantDTO o2) {
        final VisaEnseignantDTO visaEns1 = (VisaEnseignantDTO) o1;
        final VisaEnseignantDTO visaEns2 = (VisaEnseignantDTO) o2;

        String colonne = form.getOrdreTri().getColonne();
        final Boolean ascendant = form.getOrdreTri().getAscendant();
        String valeur1;
        String valeur2;
        if (colonne == null) {
            colonne = "nom";
        }
        if (colonne.equals("nom")) {
            valeur1 = visaEns1.getEnseignant().getNom()
                    + visaEns1.getEnseignant().getPrenom();
            valeur2 = visaEns2.getEnseignant().getNom()
                    + visaEns2.getEnseignant().getPrenom();
        } else if (colonne.equals("prenom")) {
            valeur1 = visaEns1.getEnseignant().getPrenom()
                    + visaEns1.getEnseignant().getNom();
            valeur2 = visaEns2.getEnseignant().getPrenom()
                    + visaEns2.getEnseignant().getNom();
        } else if (colonne.equals("dateDernierMaj")) {
            valeur1 = DateUtils.format(visaEns1.getDateDernierMaj(),
                    "yyyy-MM-dd");
            valeur2 = DateUtils.format(visaEns2.getDateDernierMaj(),
                    "yyyy-MM-dd");
        } else if (colonne.equals("dateDernierVisa")) {
            valeur1 = DateUtils.format(visaEns1.getDateDernierVisa(),
                    "yyyy-MM-dd");
            valeur2 = DateUtils.format(visaEns2.getDateDernierVisa(),
                    "yyyy-MM-dd");
        } else if (colonne.equals("type")) {
            valeur1 = visaEns1.getLibelleProfilDernierVisa();
            valeur2 = visaEns2.getLibelleProfilDernierVisa();
        } else {
            valeur1 = visaEns1.getEnseignant().getNom()
                    + visaEns1.getEnseignant().getPrenom();
            valeur2 = visaEns2.getEnseignant().getNom()
                    + visaEns2.getEnseignant().getPrenom();
        }
        if (valeur1 == null) {
            valeur1 = "";
        }
        if (valeur2 == null) {
            valeur2 = "";
        }
        final int result;
        if (ascendant) {
            result = valeur1.compareToIgnoreCase(valeur2);
        } else {
            result = valeur2.compareToIgnoreCase(valeur1);
        }

        return result;
    }

}
