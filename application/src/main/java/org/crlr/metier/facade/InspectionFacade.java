/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.UserDTOForList;
import org.crlr.dto.application.inspection.DroitInspecteurDTO;
import org.crlr.dto.application.inspection.RechercheDroitInspecteurQO;
import org.crlr.dto.application.inspection.SaveDroitInspecteurQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.business.InspectionHibernateBusinessService;
import org.crlr.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
@Component
@Transactional(
        readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
    )
public class InspectionFacade implements InspectionFacadeService {
    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private InspectionHibernateBusinessService inspectionHibernateBusinessService;


    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<DroitInspecteurDTO>> findDroitsInspection(RechercheDroitInspecteurQO rechercheDroitInspecteurQO) {
        Assert.isNotNull("rechercheDroitInspecteurQO", rechercheDroitInspecteurQO);
        
        final ResultatDTO<List<DroitInspecteurDTO>> droits = inspectionHibernateBusinessService.findDroitsInspection(rechercheDroitInspecteurQO);
        
        return droits;
    }

    /**
     * {@inheritDoc}
     */
    public List<UserDTOForList> findListeInspecteurs() {
        return inspectionHibernateBusinessService.findListeInspecteurs();
    }

    /**
     * {@inheritDoc}
     */
    public List<EnseignantDTO> findListeEnseignants(Integer idEtablissement) {
        return inspectionHibernateBusinessService.findListeEnseignants(idEtablissement);
    }

    /**
     * {@inheritDoc}
     */
    public void saveDroit(SaveDroitInspecteurQO saveDroitInspecteurQO)
            throws MetierException {
        
        
        
        Assert.isNotNull("saveDroitInspecteurQO", saveDroitInspecteurQO);
        Assert.isNotNull("vraiOufauxAjout", saveDroitInspecteurQO.getVraiOuFauxAjout());
        
        if (saveDroitInspecteurQO.getVraiOuFauxAjout()){
        
            final List<EnseignantDTO> enseignants = saveDroitInspecteurQO.getEnseignantsSelectionne();
            if (CollectionUtils.isEmpty(enseignants)){
                throw new MetierException("Il faut impérativement choisir un enseignant.");
            }
            final List<Integer> idsEnseignant = new ArrayList<Integer>();
            for (EnseignantDTO enseignant : enseignants){
                idsEnseignant.add(enseignant.getId());
            }
            saveDroitInspecteurQO.setIdsEnseignant(idsEnseignant);
            
            final List<UserDTOForList> inspecteurs = saveDroitInspecteurQO.getInspecteursSelectionne();
            if (CollectionUtils.isEmpty(inspecteurs)){
                throw new MetierException("Il faut impérativement choisir un utilisateur Inspection pédagogique.");
            }
            final List<Integer> idsInspecteurs = new ArrayList<Integer>();
            for (UserDTOForList inspecteur : inspecteurs){
                idsInspecteurs.add(Integer.decode(inspecteur.getIdentifiant()));
            }
            saveDroitInspecteurQO.setIdsInspecteur(idsInspecteurs);
            
            
        } else {
            final List<EnseignantDTO> enseignants = saveDroitInspecteurQO.getEnseignantsSelectionne();
            if (CollectionUtils.isEmpty(enseignants) || enseignants.size()>1 ){
                throw new MetierException("Un problème est survenu lors de la supression des droits.");
            }
            final List<Integer> idsEnseignant = new ArrayList<Integer>();
            idsEnseignant.add((enseignants.get(0).getId()));
            saveDroitInspecteurQO.setIdsEnseignant(idsEnseignant);
            
            final List<UserDTOForList> inspecteurs = saveDroitInspecteurQO.getInspecteursSelectionne();
            if (CollectionUtils.isEmpty(inspecteurs) || inspecteurs.size() > 1){
                throw new MetierException("Un problème est survenu lors de la supression des droits.");
            }
            final List<Integer> idsInspecteurs = new ArrayList<Integer>();
            idsInspecteurs.add(Integer.decode(inspecteurs.get(0).getIdentifiant()));
            saveDroitInspecteurQO.setIdsInspecteur(idsInspecteurs);
        }
       
        
        inspectionHibernateBusinessService.saveDroit(saveDroitInspecteurQO);
        
    }

    /**
     * {@inheritDoc}
     */
    public void saveDroitsListe(List<DroitInspecteurDTO> listeDroit) throws MetierException {
        Assert.isNotNull("saveDroitInspecteurQO", listeDroit);

        // Perssite successivement les droits contenus dans la liste
        for (final DroitInspecteurDTO droit : listeDroit) {
            
            if (droit.getEstAjoute()) {
               // Ajoute un nouveau droit
                inspectionHibernateBusinessService.ajouter(droit);
            
            } else if (droit.getEstSupprimee()) {
               // Supprime un droit qui existait
                inspectionHibernateBusinessService.supprimer(droit);
            
            } else if (droit.getEstModifiee()) {
               // Modifie un droit qui existait
                inspectionHibernateBusinessService.modifier(droit);
            }
        }
        
    }
    
   
}
