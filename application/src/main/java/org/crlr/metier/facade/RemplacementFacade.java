/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.remplacement.RechercheRemplacementQO;
import org.crlr.dto.application.remplacement.RemplacementDTO;
import org.crlr.dto.application.remplacement.TypeReglesRemplacement;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.business.RemplacementHibernateBusinessService;
import org.crlr.utils.Assert;
import org.crlr.utils.DateUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade de gestion des remplacements.
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
@Component
@Transactional(
        readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
    )
public class RemplacementFacade implements RemplacementFacadeService {
    
    /** Service de manipulation des remplacements. */
    @Autowired
    private RemplacementHibernateBusinessService remplacementHibernateBusinessService;
    
    
    /**
     * Mutateur de remplacementHibernateBusinessService {@link #remplacementHibernateBusinessService}.
     * @param remplacementHibernateBusinessService le remplacementHibernateBusinessService to set
     */
    public void setRemplacementHibernateBusinessService(
            RemplacementHibernateBusinessService remplacementHibernateBusinessService) {
        this.remplacementHibernateBusinessService = remplacementHibernateBusinessService;
    }


    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<RemplacementDTO>> findListeRemplacement(final RechercheRemplacementQO rechercheRemplacementQO) {
        return remplacementHibernateBusinessService.findListeRemplacement(rechercheRemplacementQO);
    }
    
    
    
    


    /**
     * {@inheritDoc}
     * @throws MetierException  
     */
    public ResultatDTO<Integer> saveRemplacement(RemplacementDTO remplacementDTO) throws MetierException {
        
        Assert.isNotNull("remplacementDTO", remplacementDTO);
        Assert.isNotNull("getIdEtablissement", remplacementDTO.getIdEtablissement());        
        Assert.isNotNull("getEnseignantAbsent", remplacementDTO.getEnseignantAbsent());
        Assert.isNotNull("getEnseignantRemplacant", remplacementDTO.getEnseignantRemplacant());
        Assert.isNotNull("getEnseignantAbsent().getId", remplacementDTO.getEnseignantAbsent().getId());
        Assert.isNotNull("getEnseignantRemplacant().getId", remplacementDTO.getEnseignantRemplacant().getId());
        
        // Verifie que la date de debut est bien renseignee
        if (remplacementDTO.getDateDebut() == null) {
            final ConteneurMessage conteneurMessage = new ConteneurMessage();
            conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_04.name()));
            throw new MetierException(conteneurMessage, "La date de début de remplacement est obligatoire.");
        }
        // Verifie que la date de fin est bien renseignee
        if (remplacementDTO.getDateDebut() == null) {
            final ConteneurMessage conteneurMessage = new ConteneurMessage();
            conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_05.name()));
            throw new MetierException(conteneurMessage, "La date de fin de remplacement est obligatoire.");
        }
        // Verifie que la date de debut est avant la date de fin
        if (DateUtils.lessStrict(remplacementDTO.getDateFin(),remplacementDTO.getDateDebut())) {
            final ConteneurMessage conteneurMessage = new ConteneurMessage();
            conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_03.name()));
            throw new MetierException(conteneurMessage, "La date de fin de remplacement doit est postérieure à la date de début.");
        }
        
        // Si nouveau,  verifie qu'un autre remplacement pour le meme absent/remplacant n'existe pas
        if (remplacementDTO.getId() == null) {
            
            // Charge un remplacement par sa cle fonctionnelle (etab/absent/remplacant)
            final RechercheRemplacementQO rechercheRemplacementQO = new RechercheRemplacementQO();
            rechercheRemplacementQO.setIdEnseignantAbsent(remplacementDTO.getEnseignantAbsent().getId());
            rechercheRemplacementQO.setIdEnseignantRemplacant(remplacementDTO.getEnseignantRemplacant().getId());
            rechercheRemplacementQO.setIdEtablissement(remplacementDTO.getIdEtablissement());
            final ResultatDTO<List<RemplacementDTO>> listeRemplacement = 
                remplacementHibernateBusinessService.findListeRemplacement(rechercheRemplacementQO);
            
            // Pas vide : on interdit la creation
            if (CollectionUtils.isNotEmpty(listeRemplacement.getValeurDTO())) {
                final ConteneurMessage conteneurMessage = new ConteneurMessage();
                conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_00.name(),
                                                 Nature.BLOQUANT,listeRemplacement.getValeurDTO().size()));
                throw new MetierException(conteneurMessage, "Un seul remplacement par absent/remplaçant.");
                
            }
        // Si maj, verifie que la periode de remplacement n'exclue pas des seances remplacee existantes
        } else {
            // Charge la liste des seances enregistrees dans le cadre de ce remplacement
            final RechercheRemplacementQO rechercheRemplacementQO = new RechercheRemplacementQO();
            rechercheRemplacementQO.setIdEnseignantAbsent(remplacementDTO.getEnseignantAbsent().getId());
            rechercheRemplacementQO.setIdEnseignantRemplacant(remplacementDTO.getEnseignantRemplacant().getId());
            rechercheRemplacementQO.setIdEtablissement(remplacementDTO.getIdEtablissement());
            final ResultatDTO<List<SeanceDTO>> listeSeance = 
                remplacementHibernateBusinessService.findListeSeanceRemplacee(rechercheRemplacementQO);
            
            // Si des seances existent, on verifie les date de la periode
            if (CollectionUtils.isNotEmpty(listeSeance.getValeurDTO())) {
                if (DateUtils.lessStrict(listeSeance.getValeurDTO().get(0).getDate(), remplacementDTO.getDateDebut())) {
                    final ConteneurMessage conteneurMessage = new ConteneurMessage();
                    conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_01.name(),
                                                     Nature.BLOQUANT,
                                                     DateUtils.format(listeSeance.getValeurDTO().get(0).getDate())));
                   throw new MetierException(conteneurMessage, "Séances de remplacement enregistrée avant la période de remplacement.");
                } else if (DateUtils.lessStrict(remplacementDTO.getDateFin(),
                                                listeSeance.getValeurDTO().get(listeSeance.getValeurDTO().size()).getDate())) {
                    final ConteneurMessage conteneurMessage = new ConteneurMessage();
                    conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_02.name(),
                                                     Nature.BLOQUANT,
                                                     DateUtils.format(listeSeance.getValeurDTO().get(listeSeance.getValeurDTO().size()).getDate())));
                   throw new MetierException(conteneurMessage, "Séances de remplacement enregistrée après la période de remplacement.");
                }
            }
            
        }
        
        // Effectue la sauvegarde
        return remplacementHibernateBusinessService.saveRemplacement(remplacementDTO);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Boolean> deleteRemplacement(RemplacementDTO remplacementDTO) throws MetierException {
        
        // Verifie qu'aucune seance de remplacement n'a été saisie
        // Charge la liste des seances enregistrees dans le cadre de ce remplacement
        final RechercheRemplacementQO rechercheRemplacementQO = new RechercheRemplacementQO();
        rechercheRemplacementQO.setIdEnseignantAbsent(remplacementDTO.getEnseignantAbsent().getId());
        rechercheRemplacementQO.setIdEnseignantRemplacant(remplacementDTO.getEnseignantRemplacant().getId());
        rechercheRemplacementQO.setIdEtablissement(remplacementDTO.getIdEtablissement());
        final ResultatDTO<List<SeanceDTO>> listeSeance = 
            remplacementHibernateBusinessService.findListeSeanceRemplacee(rechercheRemplacementQO);
        
        // Si des seances existent, on interdit la suppression
        if (CollectionUtils.isNotEmpty(listeSeance.getValeurDTO())) {
            final ConteneurMessage conteneurMessage = new ConteneurMessage();
            conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_06.name(),
                                             Nature.BLOQUANT,
                                             listeSeance.getValeurDTO().size()));
            throw new MetierException(conteneurMessage, "Séances de remplacement enregistrées pour ce remplacement : suppression impossible.");
        }        
        
        // Effectue la suppression du remplacement
        return remplacementHibernateBusinessService.deleteRemplacement(remplacementDTO);
    }
    
    
    /**
     * Verifie l'integrite d'un remplacement avant d'appeler la methode de sauvegarde.
     * @param remplacement le remplacement a verifier
     * @param conteneurMessage : contient les erreurs eventuelles
     * @return true si tout est ok.
     * @throws MetierException un exception
     */
    private Boolean verifieCoherenceRemplacement(final ConteneurMessage conteneurMessage, final RemplacementDTO remplacement) throws MetierException {
        
        // L'absent doit etre renseigne
        if (remplacement.getEnseignantAbsent() == null) {
            conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_11.name(), 
                    Nature.BLOQUANT, remplacement.getEnseignantAbsent().getNomComplet()));
            throw new MetierException(conteneurMessage, "L'enseignant absent est obligatoire.");
        }

        // Remplacant doit etre renseigne
        if (remplacement.getEnseignantRemplacant() == null) {
            conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_12.name(), 
                    Nature.BLOQUANT, remplacement.getEnseignantAbsent().getNomComplet()));
            throw new MetierException(conteneurMessage, "L'enseignant absent est obligatoire.");
        }
        
        // Le remplacant ne peut pas etre l'absent
        if (remplacement.getEnseignantAbsent() != null && remplacement.getEnseignantRemplacant() != null 
        && remplacement.getEnseignantAbsent().getUid().equals(remplacement.getEnseignantRemplacant().getUid())) {
            
            conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_10.name(), 
                Nature.BLOQUANT, remplacement.getEnseignantAbsent().getNomComplet()));
            
            throw new MetierException(conteneurMessage, "L'enseignant absent doit etre different du remplacant.");
        }
        
        // Verifie que la date de debut est bien renseignee
        if (remplacement.getDateDebut() == null) {
            conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_04.name()));
            throw new MetierException(conteneurMessage, "La date de début de remplacement est obligatoire.");
        }
        // Verifie que la date de fin est bien renseignee
        if (remplacement.getDateDebut() == null) {
            conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_05.name()));
            throw new MetierException(conteneurMessage, "La date de fin de remplacement est obligatoire.");
        }
        // Verifie que la date de debut est avant la date de fin
        if (DateUtils.lessStrict(remplacement.getDateFin(),remplacement.getDateDebut())) {
            conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_03.name()));
            throw new MetierException(conteneurMessage, "La date de fin de remplacement doit est postérieure à la date de début.");
        }        
        // Un remplacement existant sur la meme periode pour le meme absent/remplacant
        RechercheRemplacementQO rechercheRemplacementQO = new RechercheRemplacementQO();
        rechercheRemplacementQO.setDateDebut(new LocalDate(remplacement.getDateDebut()));
        rechercheRemplacementQO.setDateFin(new LocalDate(remplacement.getDateFin()));
        rechercheRemplacementQO.setIdEnseignantAbsent(remplacement.getEnseignantAbsent().getId());
        rechercheRemplacementQO.setIdEnseignantRemplacant(remplacement.getEnseignantRemplacant().getId());
        rechercheRemplacementQO.setIdEtablissement(remplacement.getIdEtablissement());
        final ResultatDTO<List<RemplacementDTO>> listeRemplacement = 
            remplacementHibernateBusinessService.findListeRemplacement(rechercheRemplacementQO);
        if (CollectionUtils.isNotEmpty(listeRemplacement.getValeurDTO())) {
            for (final RemplacementDTO r : listeRemplacement.getValeurDTO()) {
                if (remplacement.getId() == null || !remplacement.getId().equals(r.getId())) {
                    conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_13.name(), 
                            Nature.BLOQUANT, 
                            remplacement.getEnseignantAbsent().getNomComplet(),
                            remplacement.getEnseignantRemplacant().getNomComplet(),
                            DateUtils.format(r.getDateDebut()),
                            DateUtils.format(r.getDateFin())
                            ));
                        
                    throw new MetierException(conteneurMessage, "Un remplacement pour le meme absent/remplacant sur la meme periode existe deja.");
                }
            }
        }
        
        // Des seances hors periodes de remplacement existent deja
        // Charge la liste des seances enregistrees dans le cadre de ce remplacement
        if (remplacement.getId() != null) {
            rechercheRemplacementQO = new RechercheRemplacementQO();
            rechercheRemplacementQO.setIdRemplacement(remplacement.getId());
            final ResultatDTO<List<SeanceDTO>> listeSeance = 
                remplacementHibernateBusinessService.findListeSeanceRemplacee(rechercheRemplacementQO);
            
            // Si des seances existent, on verifie les date de la periode
            if (CollectionUtils.isNotEmpty(listeSeance.getValeurDTO())) {
                
                Date dateMaximum = listeSeance.getValeurDTO().get(listeSeance.getValeurDTO().size()-1).getDate();
                
                if (DateUtils.lessStrict(listeSeance.getValeurDTO().get(0).getDate(), remplacement.getDateDebut())) {
                    conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_01.name(),
                                                     Nature.BLOQUANT,
                                                     DateUtils.format(listeSeance.getValeurDTO().get(0).getDate())));
                   throw new MetierException(conteneurMessage, "Séances de remplacement enregistrée avant la période de remplacement.");
                } else if (DateUtils.lessStrict(remplacement.getDateFin(),dateMaximum)) {
                    conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_02.name(),
                                                     Nature.BLOQUANT,
                                                     DateUtils.format(dateMaximum)));
                   throw new MetierException(conteneurMessage, "Séances de remplacement enregistrée après la période de remplacement.");
                }
            }        
        }
        return true;
    }
    
   
    
    
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveListeRemplacement(List<RemplacementDTO> listeRemplacementDTO) throws MetierException {
        
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        ResultatDTO<Integer> resultat = new ResultatDTO<Integer>();
        
        int nombreModifications = 0;
        if (CollectionUtils.isNotEmpty(listeRemplacementDTO)) {
        
            // On supprime en 1er les elements à virer
            for (final RemplacementDTO remplacement : listeRemplacementDTO) {
                if (BooleanUtils.isTrue(remplacement.getEstSupprimee())) {
                    if (BooleanUtils.isTrue(deleteRemplacement(remplacement).getValeurDTO())) {
                        nombreModifications++;
                    }
                }
            }
            
            // On modifie les elements existants à modifier
            for (final RemplacementDTO remplacement : listeRemplacementDTO) {
                if (BooleanUtils.isTrue(remplacement.getEstModifiee())) {
                    if (verifieCoherenceRemplacement(conteneurMessage, remplacement)) {
                        if (remplacementHibernateBusinessService.saveRemplacement(remplacement).getValeurDTO() != null) {
                            nombreModifications++;
                        }
                    }
                }
            }
            
            // On ajoute les nouveaux elements
            for (final RemplacementDTO remplacement : listeRemplacementDTO) {
                if (BooleanUtils.isTrue(remplacement.getEstAjoute())) {
                    if (verifieCoherenceRemplacement(conteneurMessage, remplacement)) {  
                        if (remplacementHibernateBusinessService.saveRemplacement(remplacement).getValeurDTO() != null) {
                            nombreModifications++;
                        }
                    }
                }
            }
        }
        if (nombreModifications == 0) {
            conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_09.name(), Nature.INFORMATIF));    
        } else if (nombreModifications == 1) {
            conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_07.name(), Nature.INFORMATIF));
        } else {
            conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_08.name(), Nature.INFORMATIF, nombreModifications));
        }
        resultat.setConteneurMessage(conteneurMessage);
        resultat.setValeurDTO(nombreModifications);
        return resultat;
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<SeanceDTO>> findListeSeanceRemplacee(RechercheRemplacementQO rechercheRemplacementQO) {
        return remplacementHibernateBusinessService.findListeSeanceRemplacee(rechercheRemplacementQO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<EnseignantDTO>> findListeEnseignant(RechercheRemplacementQO rechercheRemplacementQO) {
        return remplacementHibernateBusinessService.findListeEnseignant(rechercheRemplacementQO);
    }
    
}

