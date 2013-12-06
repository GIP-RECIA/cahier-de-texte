/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AjoutSequenceControl.java,v 1.22 2010/05/19 09:10:20 jerome.carriere Exp $
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
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.sequence.SaveSequenceQO;
import org.crlr.exception.metier.MetierException;
import org.apache.commons.collections.CollectionUtils;
import org.crlr.utils.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.web.application.control.AbstractPopupControl;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.application.form.sequence.AjoutSequenceForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.utils.MessageUtils;

/**
 * Contôleur de l'outil ajout de séquence.
 *
 * @author breytond.
 * @version $Revision: 1.22 $
 */
@ManagedBean(name="ajoutSequence")
@ViewScoped
public class AjoutSequenceControl extends AbstractPopupControl<AjoutSequenceForm> {
    /**
     * 
     * Constructeur.
     */
    public AjoutSequenceControl() {
        super(new AjoutSequenceForm());
        usePopupSequence = false;
        usePopupGroupeClasse = true;
        usePopupSeance = false;
        usePopupEnseignement = true;
    }

    /**
     *
     */
    
    @PostConstruct
    public void onLoad() {

        log.debug("AjoutSequenceControl : initialisation form recherche");
        rechercherEnseignement();
        chargerEnseignement();

    }

    /**
     * Réinitialisation.
     */
    public void reset() {
        form.reset();
        chargerEnseignement();
    }

    /**
     * Fonction d'ajout de séance.
     *
     * @return L'id de la séance créée
     */
    public String ajouter() {
        log.debug("----------------- AJOUT -----------------");

        //MessageUtils.addMessage(new Message("sd"), getClass());
        final SaveSequenceQO saveSequenceQO = new SaveSequenceQO();      
        
        saveSequenceQO.setMode(AbstractForm.MODE_AJOUT);
        saveSequenceQO.setEnseignementSelectionne(form.getEnseignementSelectionne());
        saveSequenceQO.setClasseGroupeSelectionne(form.getGroupeClasseSelectionne());
        saveSequenceQO.setDescription(form.getDescription());
        saveSequenceQO.setDateDebut(form.getDateDebut());
        saveSequenceQO.setDateFin(form.getDateFin());
        saveSequenceQO.setIntitule(form.getIntitule());
        saveSequenceQO.setSeanceAssociee(false);
        
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        saveSequenceQO.getClasseGroupeSelectionne().setIdAnneeScolaire(utilisateurDTO.getAnneeScolaireDTO().getId());
        saveSequenceQO.setIdEnseignant(utilisateurDTO.getUserDTO().getIdentifiant());
        saveSequenceQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
        try {           
           final ResultatDTO<Integer> resultatDTO = this.sequenceService.saveSequence(saveSequenceQO);
            ContexteUtils.getContexteOutilControl().mettreAJourObjet(AbstractForm.RETOUR_ACQUITTEMENT, resultatDTO.getConteneurMessage());
            
            if (resultatDTO.getValeurDTO() != null && resultatDTO.getValeurDTO()>0) {
                form.reset();
            }
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
        this.form.setFiltreClasseGroupe("");
    }
    
    /**
     * Clone la valeur de l'objet enseignement de la recherche.
     */
    public void selectionnerEnseignement() {
        this.form.setEnseignementSelectionne(ObjectUtils.clone(form.getEnseignementSelectionne()));
        this.form.setFiltreEnseignement("");
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
    private void rechercherEnseignement() {
        final RechercheEnseignementPopupQO rechercheEnseignementPopupQO =
            new RechercheEnseignementPopupQO();
        
        final UtilisateurDTO utilisateurDTO =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        rechercheEnseignementPopupQO.setIdEnseignant(utilisateurDTO.getUserDTO()
                                                                   .getIdentifiant());

        rechercheEnseignementPopupQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
        
        form.setListeEnseignementSelectionne(this.getListeEnseignementPopup(rechercheEnseignementPopupQO));
    }
    
    /**
     * Charge l'enseignement directement dans le champ de saisie ssi, un seul est présent.
     */
    private void chargerEnseignement() {
        final List<EnseignementDTO> liste = form.getListeEnseignementSelectionne();
        if(!CollectionUtils.isEmpty(liste) && liste.size() == 1) {
            form.setEnseignementSelectionne(ObjectUtils.clone(liste.get(0)));
        }
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
            final List<EnseignementDTO> listeEnseignementDTO = form.getListeEnseignementSelectionne();
            for(final EnseignementDTO enseignementDTO : listeEnseignementDTO) {
                if(enseignementDTO.getCode().equals(codeEnseignement)) {
                    form.setEnseignementSelectionne(ObjectUtils.clone(enseignementDTO));
                }
            }
        }
    }
}
