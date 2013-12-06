/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.ArchiveSeanceDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.TypeReglesAcquittement;
import org.crlr.dto.application.seance.RechercheVisaSeanceQO;
import org.crlr.dto.application.visa.DateListeVisaSeanceDTO;
import org.crlr.dto.application.visa.RechercheVisaQO;
import org.crlr.dto.application.visa.ResultatRechercheVisaSeanceDTO;
import org.crlr.dto.application.visa.TypeVisa;
import org.crlr.dto.application.visa.VisaDTO;
import org.crlr.dto.application.visa.VisaEnseignantDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.business.VisaHibernateBusinessService;
import org.crlr.web.dto.TypeCouleur;
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
public class VisaFacade implements VisaFacadeService {
    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private VisaHibernateBusinessService visaHibernateBusinessService;
    
    @Autowired
    private SeanceFacadeService seanceFacade;

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<VisaEnseignantDTO>> findListeVisaEnseignant(final List<EnseignantDTO> listeEnseignant) {
        ResultatDTO<List<VisaEnseignantDTO>> resultat = visaHibernateBusinessService.findListeVisaEnseignant(listeEnseignant);
        return resultat;
        
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<VisaDTO>> findListeVisa(final RechercheVisaQO rechercheVisa) {
        return visaHibernateBusinessService.findListeVisa(rechercheVisa);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Boolean> saveListeVisa(final Collection<VisaDTO> listeVisa) throws MetierException {

        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        final ResultatDTO<Boolean> resultatDTO = new ResultatDTO<Boolean>();
        resultatDTO.setValeurDTO(false);
        
        // Traite la sauvegarde de tous les visa de la liste
        for (final VisaDTO visa : listeVisa) {
            
            /*
            if (null != visa.getTypeVisa() && null == visa.getDateVisa()) {
                conteneurMessage.add(new Message(Message.Code.ERREUR_INCONNUE.getId(),
                        Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                        "Une visa crée sans une date de création");
                
            }*/
            if (null == visa.getIdVisa()) {
                conteneurMessage.add(new Message(Message.Code.ERREUR_INCONNUE.getId(),
                        Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                        "Une erreur interne");
            }
        
            // Supprime les eventuelle seances archive précédentes
            visaHibernateBusinessService.supprimerArchiveVisa(visa);
            
            // Si MEMO, archive les seances correspondante au visa
            if (TypeVisa.MEMO == visa.getTypeVisa()) {
                visaHibernateBusinessService.archiverVisa(visa);
            }
            
            // Persiste l'objet VISA
            visaHibernateBusinessService.saveVisa(visa);
        }
        if (listeVisa.size()>0) {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_01.name(),
                    Nature.INFORMATIF, "Tous les modifications effectuées sur les visas", "enregistrées"));
        } else {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_02.name(),
                    Nature.INFORMATIF, "Aucune modification", "enregistrée"));
        }
        resultatDTO.setConteneurMessage(conteneurMessage);
        resultatDTO.setValeurDTO(true);
        return resultatDTO;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<DateListeVisaSeanceDTO>> findVisaSeance(RechercheVisaSeanceQO rechercheSeanceQO) 
        throws MetierException {
        ResultatDTO<List<DateListeVisaSeanceDTO>> resultat = visaHibernateBusinessService.findVisaSeance(rechercheSeanceQO);
        
        Map<Integer, TypeCouleur> sequenceCouleurMap = new HashMap<Integer, TypeCouleur>();
        
        List<TypeCouleur> listeCouleur = new ArrayList<TypeCouleur>(Arrays.asList(TypeCouleur.values()));
        
        int listeCouleurIndex = 0;
        
        for (DateListeVisaSeanceDTO dlvSeance : resultat.getValeurDTO()) {
            for(ResultatRechercheVisaSeanceDTO seanceDTO : dlvSeance.getListeVisaSeance()) {
                seanceFacade.completerInfoSeance(seanceDTO,  false);
                
                Integer sequenceId = seanceDTO.getSequenceDTO().getId();
                if (!sequenceCouleurMap.containsKey(sequenceId)) {
                    listeCouleurIndex++;
                    if (listeCouleurIndex >= listeCouleur.size()) {
                        listeCouleurIndex = 0;
                    }
                    sequenceCouleurMap.put(sequenceId, listeCouleur.get(listeCouleurIndex));                    
                }
                
                seanceDTO.setTypeCouleur(sequenceCouleurMap.get(sequenceId));
            }
        }
        
        return resultat;
    }
    
    /**
     * {@inheritDoc}
     * @return null si il n'y a pas d'archive correspondant
     */
    public ResultatDTO<ArchiveSeanceDTO> findArchiveSeance(final Integer idSeance, final Integer idVisa) throws MetierException {
        ResultatDTO<ArchiveSeanceDTO> resultat = visaHibernateBusinessService.findArchiveSeance(idSeance, idVisa);
        
        if (resultat.getValeurDTO() != null) {
            seanceFacade.completerInfoSeance(resultat.getValeurDTO(), true);
        }
        
        return resultat;
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Boolean> saveListeVisaSeance(final List<ResultatRechercheVisaSeanceDTO> listeVisaSeance) throws MetierException {

        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        final ResultatDTO<Boolean> resultatDTO = new ResultatDTO<Boolean>();
        resultatDTO.setValeurDTO(false);
        
        
        
        Map<Integer, VisaDTO> visaASauvegarder = new HashMap<Integer, VisaDTO>();
        
        for(ResultatRechercheVisaSeanceDTO visaSeance : listeVisaSeance) {
            Profil profil = visaSeance.getProfil();
            if (profil == null) {
                ConteneurMessage cm = new ConteneurMessage();
                cm.add(new Message(Message.Code.ERREUR_INCONNUE.getId(), Nature.BLOQUANT));
                throw new MetierException(cm, "Erreur interne");
            }
            
            VisaDTO visa = profil == Profil.DIRECTION_ETABLISSEMENT ? visaSeance.getVisaDirecteur() : visaSeance.getVisaInspecteur();
            
            visaASauvegarder.put(visa.getIdVisa(), visa);
        }
        
        saveListeVisa(visaASauvegarder.values());
        
        
        if (listeVisaSeance.size()>0) {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_01.name(),
                    Nature.INFORMATIF, "Tous les modifications effectuées sur les visas", "enregistrées"));
        } else {
            
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_02.name(),
                    Nature.INFORMATIF, "Aucune modification", "enregistrée"));
        }
        resultatDTO.setConteneurMessage(conteneurMessage);
        resultatDTO.setValeurDTO(true);
        return resultatDTO;
    }    
    
    
    
}

