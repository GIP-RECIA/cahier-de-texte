/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AbstractPopupControl.java,v 1.10 2009/11/04 08:54:10 weberent Exp $
 */

package org.crlr.web.application.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedProperty;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.dto.application.base.RechercheGroupeClassePopupQO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.sequence.RechercheSequencePopupQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.CouleurEnseignementClasseService;
import org.crlr.services.EnseignementService;
import org.crlr.services.GroupeClasseService;
import org.crlr.services.SeanceService;
import org.crlr.services.SequenceService;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.form.AbstractPopupForm;
import org.crlr.web.contexte.utils.ContexteUtils;

/**
 * Classe abstraite qui permet d'effectuer les recherches sur les popups.
 *
 * @author $author$
 * @version $Revision: 1.10 $
 *
 * @param <F> DOCUMENT ME!
 */
public abstract class AbstractPopupControl<F extends AbstractPopupForm>
    extends AbstractControl<F> {
    
    /** seanceService. */
    @ManagedProperty(value="#{seanceService}")
    protected transient SeanceService seanceService;

    /** sequenceService. */
    @ManagedProperty(value="#{sequenceService}")
    protected transient SequenceService sequenceService;

    /** groupeClasseService. */
    @ManagedProperty(value="#{groupeClasseService}")
    protected transient GroupeClasseService groupeClasseService;
    
    /** groupeClasseService. */
    @ManagedProperty(value="#{enseignementService}")
    protected transient EnseignementService enseignementService;
    
    /** couleurEnseignementClasseService. */
    @ManagedProperty(value="#{couleurEnseignementClasseService}")
    protected transient CouleurEnseignementClasseService couleurEnseignementClasseService;

    /** True si la popup séquence est utilisée dans la page sinon false. */
    protected boolean usePopupSequence;

    /** True si la popup groupe classe est utilisée dans la page sinon false. */
    protected boolean usePopupGroupeClasse;

    /** True si la popup séance est utilisée dans la page sinon false. */
    protected boolean usePopupSeance;
    
    /** True si la popup enseignement est utilisée dans la page sinon false. */
    protected boolean usePopupEnseignement;
    

/**
     * Contructeur d'AbstractControl qui valorise l'attribut form du controleur. 
     * @param form le formulaire
     */
    public AbstractPopupControl(F form) {
        super(form);
    }

    /**
     * Mutateur seanceService.
     *
     * @param seanceService seanceService à modifier
     */
    public void setSeanceService(SeanceService seanceService) {
        this.seanceService = seanceService;
    }

    /**
     * Mutateur sequenceService.
     *
     * @param sequenceService sequenceService à modifier
     */
    public void setSequenceService(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    /**
     * Mutateur groupeClasseService.
     *
     * @param groupeClasseService groupeClasseService à modifier
     */
    public void setGroupeClasseService(GroupeClasseService groupeClasseService) {
        this.groupeClasseService = groupeClasseService;
    }

    /**
     * Accesseur usePopupSequence.
     *
     * @return usePopupSequence
     */
    public boolean isUsePopupSequence() {
        return usePopupSequence;
    }

    /**
     * Mutateur usePopupSequence.
     *
     * @param usePopupSequence usePopupSequence
     */
    public void setUsePopupSequence(boolean usePopupSequence) {
        this.usePopupSequence = usePopupSequence;
    }

    /**
     * Accesseur usePopupGroupeClasse.
     *
     * @return usePopupGroupeClasse
     */
    public boolean isUsePopupGroupeClasse() {
        return usePopupGroupeClasse;
    }

    /**
     * Mutateur usePopupGroupeClasse.
     *
     * @param usePopupGroupeClasse usePopupGroupeClasse
     */
    public void setUsePopupGroupeClasse(boolean usePopupGroupeClasse) {
        this.usePopupGroupeClasse = usePopupGroupeClasse;
    }

    /**
     * Accesseur usePopupSeance.
     *
     * @return usePopupSeance
     */
    public boolean isUsePopupSeance() {
        return usePopupSeance;
    }

    /**
     * Mutateur usePopupSeance.
     *
     * @param usePopupSeance usePopupSeance
     */
    public void setUsePopupSeance(boolean usePopupSeance) {
        this.usePopupSeance = usePopupSeance;
    }

    /**
     * Mutateur enseignementService.
     * @param enseignementService Le enseignementService à modifier
     */
    public void setEnseignementService(EnseignementService enseignementService) {
        this.enseignementService = enseignementService;
    }

    /**
     * Retourne la liste des séquences pour la popup en fonction des paramètres.
     *
     * @param rechercheSequencePopupQO DOCUMENT ME!
     *
     * @return La liste
     */
    public List<SequenceDTO> getListeSequencePopup(RechercheSequencePopupQO rechercheSequencePopupQO) {
        List<SequenceDTO> liste = new ArrayList<SequenceDTO>();
        if (usePopupSequence) {
            try {
                
                final UtilisateurDTO utilisateurDTO =
                    ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
                rechercheSequencePopupQO.setIdEnseignant(utilisateurDTO.getUserDTO()
                                                                       .getIdentifiant());
                rechercheSequencePopupQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
                rechercheSequencePopupQO.setIdAnneeScolaire(utilisateurDTO.getAnneeScolaireDTO()
                                                                          .getId());
                
                final ResultatDTO<List<SequenceDTO>> listeSequenceDTO =
                    this.sequenceService.findSequencePopup(rechercheSequencePopupQO);
                liste = ObjectUtils.clone(listeSequenceDTO.getValeurDTO());
            } catch (MetierException e) {
                log.debug("{0}", e.getMessage());
            }
        }
        return liste;
    }

    /**
     * Retourne la liste des groupes/classes pour la popup en fonction des
     * paramètres.
     *
     * @param rechercheGroupeClassePopupQO RechercheGroupeClassePopupQO
     *
     * @return La liste
     */
    public List<GroupesClassesDTO> getListeGroupeClassePopup(RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO) {
        List<GroupesClassesDTO> liste = new ArrayList<GroupesClassesDTO>();
        if (usePopupGroupeClasse) {
            try {
                
                final UtilisateurDTO utilisateurDTO =
                    ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
                
                if (utilisateurDTO.getProfil().equals(Profil.INSPECTION_ACADEMIQUE)){
                    rechercheGroupeClassePopupQO.setIdInspecteur(utilisateurDTO.getUserDTO().getIdentifiant());
                } 
                
                rechercheGroupeClassePopupQO.setIdEnseignant(utilisateurDTO.getUserDTO()
                                                                          .getIdentifiant());

                rechercheGroupeClassePopupQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());

                rechercheGroupeClassePopupQO.setIdAnneeScolaire(utilisateurDTO.getAnneeScolaireDTO().getId());
                rechercheGroupeClassePopupQO.setIdEnseignement(rechercheGroupeClassePopupQO.getIdEnseignement());
                
                final ResultatDTO<List<GroupesClassesDTO>> listeGroupeClasseDTO =
                    this.groupeClasseService.findGroupeClassePopup(rechercheGroupeClassePopupQO);
                liste = ObjectUtils.clone(listeGroupeClasseDTO.getValeurDTO());
            } catch (MetierException e) {
                log.debug("{0}", e.getMessage());
            }
        }
        return liste;
    }

 
    
    /**
     * Retourne la liste des enseignement pour la popup en fonction des paramètres.
     *
     * @param rechercheEnseignementPopupQO RechercheEnseignementPopupQO
     *
     * @return La liste
     */
    public List<EnseignementDTO> getListeEnseignementPopup(RechercheEnseignementPopupQO rechercheEnseignementPopupQO) {
        List<EnseignementDTO> liste = new ArrayList<EnseignementDTO>();
        if (usePopupEnseignement) {
            try {
                final UtilisateurDTO utilisateurDTO =
                    ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
                if (utilisateurDTO.getProfil().equals(Profil.INSPECTION_ACADEMIQUE)){
                    rechercheEnseignementPopupQO.setIdInspecteur(utilisateurDTO.getUserDTO().getIdentifiant());
                } 
                
                final ResultatDTO<List<EnseignementDTO>> listeEnseignementDTO =
                    this.enseignementService.findEnseignementPopup(rechercheEnseignementPopupQO);
                liste = ObjectUtils.clone(listeEnseignementDTO.getValeurDTO());
            } catch (MetierException e) {
                log.debug("{0}", e.getMessage());
            }
        }
        return liste;
    }

	public CouleurEnseignementClasseService getCouleurEnseignementClasseService() {
		return couleurEnseignementClasseService;
	}

	public void setCouleurEnseignementClasseService(
			CouleurEnseignementClasseService couleurEnseignementClasseService) {
		this.couleurEnseignementClasseService = couleurEnseignementClasseService;
	}
    
}
