/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PrefControl.java,v 1.7 2010/05/20 10:08:46 jerome.carriere Exp $
 */

package org.crlr.web.application.control;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.crlr.dto.Outil;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.PreferencesQO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.sequence.RechercheSequenceQO;
import org.crlr.dto.application.sequence.SaveSequenceSimplifieeQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.PreferencesService;
import org.crlr.services.SequenceService;
import org.apache.commons.lang.StringUtils;
import org.crlr.web.application.MenuAction;
import org.crlr.web.application.form.PrefForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.TypePreferences;

/**
 * PrefControl.
 *
 * @author breytond
 * @version $Revision: 1.7 $
 */
@ManagedBean(name="pref")
@ViewScoped
public class PrefControl extends AbstractControl<PrefForm> {

    /** Injection spring du service des séquences. */
    @ManagedProperty(value="#{sequenceService}")
    private transient SequenceService sequenceService;

    /** service des préférences. */
    @ManagedProperty(value="#{preferencesService}")
    private transient PreferencesService preferencesService;
    
    @ManagedProperty(value="#{changeProfil}")
    private transient ChangeProfilControl changerProfileCrontrol;
    
    /**
     * Mutateur de sequenceService.
     *
     * @param sequenceService le sequenceService à modifier.
     */
    public void setSequenceService(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    /**
     * Mutateur de preferencesService.
     *
     * @param preferencesService le preferencesService à modifier.
     */
    public void setPreferencesService(PreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }

    /**
     * Constructeur.
     */
    public PrefControl() {
        super(new PrefForm());
    }
    
    /**
     * Onload.
     */
    @PostConstruct
    public void onLoad() {
        
        
        // Charge les preference de l'utilisateur
        final UtilisateurDTO utilisateurDTO =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        form.setVraiOuFauxSaisieSimplifiee(utilisateurDTO.getVraiOuFauxEtabSaisieSimplifiee());
        form.setVraiOuFauxSaisieSimplifieeBdd(utilisateurDTO.getVraiOuFauxEtabSaisieSimplifiee());
        

        String userPreferences =
            preferencesService.findUtilisateurPreferences(utilisateurDTO.getUserDTO()
                                                                        .getUid());
        if (StringUtils.isEmpty(userPreferences)) {
            if (Profil.ENSEIGNANT.equals(utilisateurDTO.getProfil())) {
                userPreferences = TypePreferences.EDTMENSUEL.name();
            } else if (Profil.ELEVE.equals(utilisateurDTO.getProfil()) ||
                           Profil.PARENT.equals(utilisateurDTO.getProfil())) {
                userPreferences = TypePreferences.DEVOIRS.name();
            }
        }
        form.setTypePreferences(userPreferences); 
    //    getChangerProfileCrontrol().onLoad();
    }

     /**
      * Supprime toutes les sequences vides, puis enregistre l'activation de la saisie simplifiée.   
      */
     public void saveSaisieSimplifieeAvecSuppression() {
         supprimerSequenceLibre();
         saveSaisieSimplifiee();
     }
            
    /**
     * Appel métier du service d'activation de la saisie simplifiée.
     */
    public void saveSaisieSimplifiee() {
        final Boolean vraiOuFauxSaisieSimplifiee = form.getVraiOuFauxSaisieSimplifiee();
        final UtilisateurDTO utilisateurDTO =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        
        final AnneeScolaireDTO anneeScolaireDTO = utilisateurDTO.getAnneeScolaireDTO();
        
        try {
            final SaveSequenceSimplifieeQO saveSequenceSimplifieeQO =
                new SaveSequenceSimplifieeQO();
            saveSequenceSimplifieeQO.setAnneeScolaireDTO(anneeScolaireDTO);
            saveSequenceSimplifieeQO.setIdEnseignant(utilisateurDTO.getUserDTO()
                    .getIdentifiant());
            saveSequenceSimplifieeQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
            saveSequenceSimplifieeQO.setVraiOuFauxSaisieSimplifiee(vraiOuFauxSaisieSimplifiee);
            
            sequenceService.saveSequenceSaisieSimplifiee(saveSequenceSimplifieeQO);
            utilisateurDTO.setVraiOuFauxEtabSaisieSimplifiee(vraiOuFauxSaisieSimplifiee);
            form.setVraiOuFauxSaisieSimplifieeBdd(vraiOuFauxSaisieSimplifiee);
            
            //réinitialise le menu
            final MenuControl menuControl = ContexteUtils.getMenuControl();
            menuControl.init();
        } catch (MetierException e) {
            log.debug("Erreur de sauvegarde de l'ouverture établissement : {0}",
                      e.getMessage());
        }

    }

    /**
     * Sauvegarde des préférences utilisateurs.
     * @return renvoie la chaine vide qui va permettre de naviguer vers l'écran par défaut.
     */
    public String savePreferences() {
        final String uid =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getUserDTO()
                         .getUid();
        final PreferencesQO preferencesQO = new PreferencesQO();
        preferencesQO.setPreferences(form.getTypePreferences());
        preferencesQO.setUid(uid);
        log.debug("TEST {0}", uid);
        try {
            preferencesService.savePreferences(preferencesQO);
            final Outil outil;
            if (form.getTypePreferences() == null) {
                return null;
            } else if (form.getTypePreferences().equals(TypePreferences.EMPLOI.name())) {
                outil = Outil.CONSOLIDER_EMP;
            } else if (form.getTypePreferences().equals(TypePreferences.DEVOIRS.name())) {
                outil = Outil.DEVOIRS;
            } else if (form.getTypePreferences().equals(TypePreferences.EDTMENSUEL.name())) {
                outil = Outil.CAHIER_MENSUEL;
            } else if (form.getTypePreferences().equals(TypePreferences.SEANCES.name())) {
                outil = Outil.SEANCE_SEMAINE;
            } else if (form.getTypePreferences().equals(TypePreferences.SAISIR_EMP.name())) {
                outil = Outil.SAISIR_EMP;
            } else {
                return null;
            }
            
            final MenuAction action = new MenuAction("",outil.name(),"",null);
            return action.actionArbre();
        } catch (MetierException e) {
            log.debug("Erreur de sauvegarde des préférences : {0}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Supprime pour un enseignant/etablissement toutes les sequence qui sont pas avec une seance.
     */
    public void supprimerSequenceLibre() {
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        final RechercheSequenceQO rechercheSequenceQO = new RechercheSequenceQO();
        rechercheSequenceQO.setIdEnseignant(utilisateurDTO.getUserDTO().getIdentifiant());
        rechercheSequenceQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
        try {
            sequenceService.deleteSequencesVide(rechercheSequenceQO);
        } catch (MetierException e) {
            log.debug("Erreur de suppression des sequence vides : {0}", e.getMessage());
        }  
    }

	public ChangeProfilControl getChangerProfileCrontrol() {
		return changerProfileCrontrol;
	}

	public void setChangerProfileCrontrol(ChangeProfilControl changerProfileCrontrol) {
		this.changerProfileCrontrol = changerProfileCrontrol;
	}
}
