/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConsulterSequenceControl.java,v 1.25 2010/04/19 13:35:00 ent_breyton Exp $
 */

package org.crlr.web.application.control.sequence;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.dto.application.base.RechercheGroupeClassePopupQO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.sequence.ResultatRechercheSequenceDTO;
import org.crlr.dto.application.sequence.SaveSequenceQO;
import org.crlr.dto.application.sequence.SequenceAffichageQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.utils.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.web.application.control.AbstractPopupControl;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.application.form.sequence.ConsulterSequenceForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.TypeCouleur;
import org.crlr.web.utils.MessageUtils;
import org.crlr.web.utils.NavigationUtils;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.25 $
 */
@ManagedBean(name="consulterSequence")
@ViewScoped
public class ConsulterSequenceControl extends AbstractPopupControl<ConsulterSequenceForm> {

    
/**
     * 
     * Constructeur.
     */
    public ConsulterSequenceControl() {
        super(new ConsulterSequenceForm());
        usePopupEnseignement = true;
        usePopupGroupeClasse = true;
        usePopupSeance = false;
        usePopupSequence = false;
    }
    
    /**
     * Réinitialisation.
     */
    public void reset() {
        form.reset();
        chargerEnseignement();
    }

    /**
     * 
     */ 
    @PostConstruct
    public void onLoad() {
           final ResultatRechercheSequenceDTO resultatRechercheSequenceDTO =
                (ResultatRechercheSequenceDTO) ContexteUtils.getContexteOutilControl()
                .recupererEtSupprimerObjet(RechercheSequenceControl.class.getName());
            if (resultatRechercheSequenceDTO != null) {
                final Integer idSequence =
                    resultatRechercheSequenceDTO.getId();
                try {
                    final SequenceAffichageQO sequenceAffichageQO = new SequenceAffichageQO();
                    sequenceAffichageQO.setId(idSequence);
                    final SequenceDTO sequenceDTO = sequenceService.findSequenceAffichage(sequenceAffichageQO);
                    final EnseignementDTO enseignementDTO = enseignementService.find(sequenceDTO.getIdEnseignement());
                    GroupesClassesDTO groupesClassesDTO;
                    if(sequenceDTO.getTypeGroupe() != null && TypeGroupe.GROUPE  == (sequenceDTO.getTypeGroupe())) {
                        groupesClassesDTO = groupeClasseService.findGroupe(sequenceDTO.getIdClasseGroupe());
                        form.setGroupeClasseSelectionne(groupesClassesDTO);
                    } else if(sequenceDTO.getTypeGroupe() != null && TypeGroupe.CLASSE  == (sequenceDTO.getTypeGroupe())) {
                        groupesClassesDTO = groupeClasseService.findClasse(sequenceDTO.getIdClasseGroupe());
                        form.setGroupeClasseSelectionne(groupesClassesDTO);
                    }
                    
                    TypeCouleur typeCouleur = sequenceDTO.getTypeCouleur();
                         if(typeCouleur == null) {
                         typeCouleur = TypeCouleur.Blanc;
                     }
                    form.setTypeGroupeSelectionne(sequenceDTO.getTypeGroupe());
                    form.setEnseignementSelectionne(enseignementDTO);
                    form.setCode(sequenceDTO.getCode());
                    form.setDescription(sequenceDTO.getDescription());
                    form.setIntitule(sequenceDTO.getIntitule());
                    form.setDateDebut(sequenceDTO.getDateDebut());
                    form.setDateFin(sequenceDTO.getDateFin());
                    form.setResultatRechercheSequenceDTO(resultatRechercheSequenceDTO);
                    form.setTypeCouleur(typeCouleur);          
					form.setResultatRechercheSequenceDTO(resultatRechercheSequenceDTO);

                    if(AbstractForm.MODE_DELETE.equals(resultatRechercheSequenceDTO.getMode())) {
                        form.setTitreDePate("Supprimer une séquence");
                        form.setModifiable(false);
                        form.setRenderedSupprimer(true);
                        form.setSeancesAssociees(true);
                        form.setTypeGroupeSelectionne(sequenceDTO.getTypeGroupe());
                    } else if(AbstractForm.MODE_CONSULTATION.equals(resultatRechercheSequenceDTO.getMode())) {
                        form.setTitreDePate("Consulter une séquence");
                        form.setModifiable(false);
                        form.setSeancesAssociees(true);
                        form.setTypeGroupeSelectionne(sequenceDTO.getTypeGroupe());
                    } else if (AbstractForm.MODE_MODIF.equals(resultatRechercheSequenceDTO.getMode())) {
                        form.setTitreDePate("Modifier une séquence");
                        final List<SeanceDTO> listeSeanceDTO = sequenceService.findSeanceBySequence(resultatRechercheSequenceDTO);
                        if(listeSeanceDTO.size() == 0) {
                            form.setSeancesAssociees(false);
                        } else {
                            form.setSeancesAssociees(true);
                        }
                        form.setModifiable(true);
                        form.setRenderedModifier(true);
                        form.setIdSequence(resultatRechercheSequenceDTO.getId());
                        form.setTypeGroupeSelectionne(sequenceDTO.getTypeGroupe());
                        form.setOldIdEnseignement(form.getEnseignementSelectionne().getId());
                        form.setOldIdGroupeClasse(form.getGroupeClasseSelectionne().getId());
                        form.setOldIdSequence(form.getIdSequence());
                        form.setOldTypeGroupeSelectionne(form.getTypeGroupeSelectionne());
                        form.setOldTypeGroupeSelectionne(sequenceDTO.getTypeGroupe());
                    } else if (AbstractForm.MODE_DUPLICATE.equals(resultatRechercheSequenceDTO.getMode())) {
                        form.setTitreDePate("Dupliquer une séquence");
                        form.setCode(null);
                        form.setModifiable(true);
                        form.setRenderedDupliquer(true);
                    }

                    chargerEnseignement();


                } catch (final MetierException e) {
                    log.debug("{0}", e.getMessage());
                }
                
        }
    }
    /**
     * Appel métier pour supprimer une séquence.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String supprimer() {
        log.debug("----------------- SUPPRESSION -----------------");
        try {
            final ResultatDTO<Integer> resultatDTO = sequenceService.deleteSequence(form.getResultatRechercheSequenceDTO());
            ContexteUtils.getContexteOutilControl().mettreAJourObjet(AbstractForm.RETOUR_ACQUITTEMENT, resultatDTO.getConteneurMessage());
            return NavigationUtils.retourOutilPrecedentEnDepilant();
        } catch (final MetierException e) {
            log.debug("{0}", e.getMessage());
        }
        return null;
    }
    
    /**
     * Appel métier pour modifier une séquence.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String modifier() {
        log.debug("----------------- MODIFIER -----------------");
        final SaveSequenceQO saveSequenceQO = new SaveSequenceQO();
        saveSequenceQO.setOldIdEnseignement(form.getOldIdEnseignement());
        saveSequenceQO.setOldIdGroupeClasse(form.getOldIdGroupeClasse());
        saveSequenceQO.setOldIdSequence(form.getOldIdSequence());
        saveSequenceQO.setOldTypeGroupeSelectionne(form.getOldTypeGroupeSelectionne());
        saveSequenceQO.setEnseignementSelectionne(form.getEnseignementSelectionne());
        saveSequenceQO.setCode(form.getCode());
        saveSequenceQO.setDescription(form.getDescription());
        saveSequenceQO.setDateDebut(form.getDateDebut());
        saveSequenceQO.setDateFin(form.getDateFin());
        saveSequenceQO.setIntitule(form.getIntitule());
        saveSequenceQO.setTypeCouleur(form.getTypeCouleur());
        
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
         
        saveSequenceQO.setClasseGroupeSelectionne(form.getGroupeClasseSelectionne());
        saveSequenceQO.setIdEnseignant(utilisateurDTO.getUserDTO().getIdentifiant());
        saveSequenceQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());

        saveSequenceQO.setEnseignementSelectionne(form.getEnseignementSelectionne());
        saveSequenceQO.setId(form.getIdSequence());
        saveSequenceQO.setMode(AbstractForm.MODE_MODIF);
        if(form.getSeancesAssociees()) {
            saveSequenceQO.setSeanceAssociee(true);
        }
        try {
            final ResultatDTO<Integer> resultatDTO = sequenceService.saveSequence(saveSequenceQO);
            ContexteUtils.getContexteOutilControl().mettreAJourObjet(AbstractForm.RETOUR_ACQUITTEMENT, resultatDTO.getConteneurMessage());
            form.resetChampsObligatoire();
            return NavigationUtils.retourOutilPrecedentEnDepilant();
        } catch (final MetierException e) {
            form.setListeChampsObligatoire(MessageUtils.getAllCodeMessage(e.getConteneurMessage()));
            log.debug("{0}", e.getMessage());
        }
        return null;
    }
    
    /**
     * Appel métier pour dupliquer une séquence.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String dupliquer() {
        log.debug("----------------- DUPLIQUER -----------------");

        final SaveSequenceQO saveSequenceQO = new SaveSequenceQO();
        saveSequenceQO.setEnseignementSelectionne(form.getEnseignementSelectionne());
        saveSequenceQO.setDescription(form.getDescription());
        saveSequenceQO.setDateDebut(form.getDateDebut());
        saveSequenceQO.setDateFin(form.getDateFin());
        saveSequenceQO.setIntitule(form.getIntitule());
        saveSequenceQO.setTypeCouleur(form.getTypeCouleur());
        
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        
        
        saveSequenceQO.setIdEnseignant(utilisateurDTO.getUserDTO().getIdentifiant());
        saveSequenceQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
        
        saveSequenceQO.setClasseGroupeSelectionne(form.getGroupeClasseSelectionne());
        saveSequenceQO.getClasseGroupeSelectionne().setIdAnneeScolaire(utilisateurDTO.getAnneeScolaireDTO().getId());
        saveSequenceQO.setEnseignementSelectionne(form.getEnseignementSelectionne());
        saveSequenceQO.setMode(AbstractForm.MODE_DUPLICATE);
        saveSequenceQO.setSeanceAssociee(false);
        try {
            final ResultatDTO<Integer> resultatDTO = this.sequenceService.saveSequence(saveSequenceQO);
            ContexteUtils.getContexteOutilControl().mettreAJourObjet(AbstractForm.RETOUR_ACQUITTEMENT, resultatDTO.getConteneurMessage());
            form.resetChampsObligatoire();
            return NavigationUtils.retourOutilPrecedentEnDepilant();
        } catch (final MetierException e) {
            form.setListeChampsObligatoire(MessageUtils.getAllCodeMessage(e.getConteneurMessage()));
            log.debug("{0}", e.getMessage());
        }
        return null;
    }
    
    /**
     * Méthode déclenchée quand on clique sur le type class ou groupe pour réinitialiser l'input.
     */
    public void resetClasseGroupeSelectionne() {
        this.form.setGroupeClasseSelectionne(new GroupesClassesDTO());
    }
    
    /**
     * Retour.
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String retour() {
        log.debug("----------------- RETOUR -----------------");
        return NavigationUtils.retourOutilPrecedentEnDepilant();
    }
    
    /**
     * Clone la valeur de l'objet enseignement de la recherche.
     */
    public void selectionnerEnseignement() {
        form.setEnseignementSelectionne(ObjectUtils.clone(form.getEnseignementSelectionne()));
    }

    /**
     * Clone la valeur de l'objet groupeClasse de la recherche.
     */
    public void selectionnerGroupeClasse() {
        form.setGroupeClasseSelectionne(ObjectUtils.clone(form.getGroupeClasseSelectionne()));
    }
    
    /**
     * Appel métier de recherche des enseignements.
     */
    private void chargerEnseignement() {
        List<EnseignementDTO> liste = new ArrayList<EnseignementDTO>();

        final RechercheEnseignementPopupQO rechercheEnseignementPopupQO =
            new RechercheEnseignementPopupQO();

        final UtilisateurDTO utilisateurDTO =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        rechercheEnseignementPopupQO.setIdEnseignant(utilisateurDTO.getUserDTO()
                                                                   .getIdentifiant());

        rechercheEnseignementPopupQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());

        liste = this.getListeEnseignementPopup(rechercheEnseignementPopupQO);

        form.setListeEnseignement(liste);
    }
    
    
 
    /**
     * Retourne la liste des groupes ou des classes en fonction du selectbox.
     *
     * @return la liste
     */
    public List<GroupesClassesDTO> getListeGroupeClasse() {
        List<GroupesClassesDTO> liste = new ArrayList<GroupesClassesDTO>();
        final RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO =
            new RechercheGroupeClassePopupQO();

        rechercheGroupeClassePopupQO.setTypeGroupeClasse(this.form.getTypeGroupeSelectionne());

        if (TypeGroupe.GROUPE  == (form.getTypeGroupeSelectionne())) {
            liste = this.getListeGroupeClassePopup(rechercheGroupeClassePopupQO);
        } else if (TypeGroupe.CLASSE  == (form.getTypeGroupeSelectionne())) {
            liste = this.getListeGroupeClassePopup(rechercheGroupeClassePopupQO);
        }
        form.setListeGroupeClasse(liste);
        return form.getListeGroupeClasse();
    }

    /**
     * Affiche les informations de l'enseignement saisie grace a l'évènement onblur.
     */
    public void chargerInfosEnseignement() {
        final String codeEnseignement = form.getEnseignementSelectionne().getCode();
        form.getEnseignementSelectionne().setIntitule("");
        if(!StringUtils.isEmpty(codeEnseignement)) {
            final List<EnseignementDTO> listeEnseignementDTO = form.getListeEnseignement();
            for(final EnseignementDTO enseignementDTO : listeEnseignementDTO) {
                if(enseignementDTO.getCode().equals(codeEnseignement)) {
                    form.setEnseignementSelectionne(ObjectUtils.clone(enseignementDTO));
                }
            }
        }
    }
    

}
