/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequenceFacade.java,v 1.33 2010/05/20 08:24:50 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.util.ObjectUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GenericDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.dto.application.base.RechercheGroupeClassePopupQO;
import org.crlr.dto.application.base.SaisieSimplifieeDTO;
import org.crlr.dto.application.base.SaisieSimplifieeQO;
import org.crlr.dto.application.base.SaveEnseignementQO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.base.TypeAffichage;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.TypeReglesAcquittement;
import org.crlr.dto.application.base.TypeReglesAdmin;
import org.crlr.dto.application.base.TypeReglesClasse;
import org.crlr.dto.application.base.TypeReglesEnseignement;
import org.crlr.dto.application.base.TypeReglesGroupe;
import org.crlr.dto.application.sequence.PrintSeanceOuSequenceQO;
import org.crlr.dto.application.sequence.PrintSequenceDTO;
import org.crlr.dto.application.sequence.RechercheSequencePopupQO;
import org.crlr.dto.application.sequence.RechercheSequenceQO;
import org.crlr.dto.application.sequence.ResultatRechercheSequenceDTO;
import org.crlr.dto.application.sequence.SaveCouleurEnseignementClasseQO;
import org.crlr.dto.application.sequence.SaveSequenceQO;
import org.crlr.dto.application.sequence.SaveSequenceSimplifieeQO;
import org.crlr.dto.application.sequence.SequenceAffichageQO;
import org.crlr.dto.application.sequence.TypeReglesSequence;
import org.crlr.exception.metier.MetierException;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.business.AnneeScolaireHibernateBusinessService;
import org.crlr.metier.business.ClasseHibernateBusinessService;
import org.crlr.metier.business.CouleurEnseignementClasseHibernateBusinessService;
import org.crlr.metier.business.EnseignementHibernateBusinessService;
import org.crlr.metier.business.EtablissementHibernateBusinessService;
import org.crlr.metier.business.GroupeHibernateBusinessService;
import org.crlr.metier.business.SeanceHibernateBusinessService;
import org.crlr.metier.business.SequenceHibernateBusinessService;
import org.crlr.metier.entity.EnseignementBean;
import org.crlr.metier.entity.SequenceBean;
import org.crlr.report.Report;
import org.crlr.report.impl.PdfReportGenerator;
import org.crlr.services.ImagesServlet;
import org.crlr.utils.Assert;
import org.crlr.utils.BooleanUtils;
import org.crlr.utils.ClassUtils;
import org.crlr.utils.DateUtils;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.dto.TypeCouleur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Façade concernant les fonctionnalités du module séquence.
 *
 * @author breytond
 * @version $Revision: 1.33 $
 */
@Component
@Transactional(
        readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
    )
public class SequenceFacade implements SequenceFacadeService {
    /** service métier séquence. */
    @Autowired
    private SequenceHibernateBusinessService sequenceHibernateBusinessService;

    /** service métier séance. */
    @Autowired
    private SeanceHibernateBusinessService seanceHibernateBusinessService;

    /** service métier enseignement. */
    @Autowired
    private EnseignementHibernateBusinessService enseignementHibernateBusinessService;

    /** service métier groupe. */
    @Autowired
    private GroupeHibernateBusinessService groupeHibernateBusinessService;

    /** service métier classe. */
    @Autowired
    private ClasseHibernateBusinessService classeHibernateBusinessService;

    /** service métier année scolaire. */
    @Autowired
    private AnneeScolaireHibernateBusinessService anneeScolaireHibernateBusinessService;
    
    /** service métier établissement. */
    @Autowired
    private EtablissementHibernateBusinessService etablissementHibernateBusinessService;
    
    /** service mÃ©tier pour la couleur. */
    @Autowired
    private CouleurEnseignementClasseHibernateBusinessService couleurEnseignementClasseHibernateBusinessService;
    
    
    protected final Log log = LogFactory.getLog(getClass());
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<SequenceDTO>> findSequencePopup(RechercheSequencePopupQO rechercheSequencePopupQO)
        throws MetierException {
        return sequenceHibernateBusinessService.findSequencePopup(rechercheSequencePopupQO);
    }

    /**
     * Mutateur sequenceHibernateBusinessService.
     *
     * @param sequenceHibernateBusinessService Le sequenceHibernateBusinessService à
     *        modifier
     */
    public void setSequenceHibernateBusinessService(SequenceHibernateBusinessService sequenceHibernateBusinessService) {
        this.sequenceHibernateBusinessService = sequenceHibernateBusinessService;
    }

    /**
     * Mutateur enseignementHibernateBusinessService.
     *
     * @param enseignementHibernateBusinessService Le
     *        enseignementHibernateBusinessService à modifier
     */
    public void setEnseignementHibernateBusinessService(EnseignementHibernateBusinessService enseignementHibernateBusinessService) {
        this.enseignementHibernateBusinessService = enseignementHibernateBusinessService;
    }

    /**
     * Mutateur groupeHibernateBusinessService.
     *
     * @param groupeHibernateBusinessService Le groupeHibernateBusinessService à modifier
     */
    public void setGroupeHibernateBusinessService(GroupeHibernateBusinessService groupeHibernateBusinessService) {
        this.groupeHibernateBusinessService = groupeHibernateBusinessService;
    }

    /**
     * Mutateur classeHibernateBusinessService.
     *
     * @param classeHibernateBusinessService Le classeHibernateBusinessService à modifier
     */
    public void setClasseHibernateBusinessService(ClasseHibernateBusinessService classeHibernateBusinessService) {
        this.classeHibernateBusinessService = classeHibernateBusinessService;
    }

    /**
     * Mutateur seanceHibernateBusinessService.
     *
     * @param seanceHibernateBusinessService Le seanceHibernateBusinessService à modifier
     */
    public void setSeanceHibernateBusinessService(SeanceHibernateBusinessService seanceHibernateBusinessService) {
        this.seanceHibernateBusinessService = seanceHibernateBusinessService;
    }

    /**
     * Mutateur anneeScolaireHibernateBusinessService.
     *
     * @param anneeScolaireHibernateBusinessService Le
     *        anneeScolaireHibernateBusinessService à modifier
     */
    public void setAnneeScolaireHibernateBusinessService(AnneeScolaireHibernateBusinessService anneeScolaireHibernateBusinessService) {
        this.anneeScolaireHibernateBusinessService = anneeScolaireHibernateBusinessService;
    }

    /**
     * Mutateur de etablissementHibernateBusinessService.
     * @param etablissementHibernateBusinessService le etablissementHibernateBusinessService à modifier.
     */
    public void setEtablissementHibernateBusinessService(
            EtablissementHibernateBusinessService etablissementHibernateBusinessService) {
        this.etablissementHibernateBusinessService = etablissementHibernateBusinessService;
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<ResultatRechercheSequenceDTO>> findSequence(RechercheSequenceQO rechercheSequenceQO)
        throws MetierException {
        Assert.isNotNull("rechercheSequenceQO", rechercheSequenceQO);

        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        final String codeEnseignement = rechercheSequenceQO.getCodeEnseignement();
        if (!StringUtils.isEmpty(codeEnseignement) && rechercheSequenceQO.getIdEnseignement() == null) {
            final EnseignementBean enseignementBean =
                enseignementHibernateBusinessService.findByCode(codeEnseignement);
            if (enseignementBean == null) {
                conteneurMessage.add(new Message(TypeReglesEnseignement.ENSEIGNEMENT_00.name(),
                                                 Nature.BLOQUANT));
                throw new MetierException(conteneurMessage, "L'enseignement n'existe pas.");
            }
            rechercheSequenceQO.setIdEnseignement(enseignementBean.getId());
        } 

        if (null != rechercheSequenceQO.getIdEnseignement()) {
            final boolean droitEnseignement =
                enseignementHibernateBusinessService.checkDroitEnseignement(rechercheSequenceQO.getIdEnseignant(),
                                                                            rechercheSequenceQO.getIdEnseignement(),
                                                                            rechercheSequenceQO.getIdEtablissement());
            if (!droitEnseignement) {
                conteneurMessage.add(new Message(TypeReglesEnseignement.ENSEIGNEMENT_01.name(),
                                                 Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                                          "L'enseignement existe mais vous n'avez pas les droits dessus.");
            }
        }

        Integer idClasseGroupe = findClasseGroupeId(rechercheSequenceQO.getGroupeClasseSelectionne(), null, null);
        
        //Type saisie mais pas la classe / groupe
        verifierClasseGroupeSaisieAvecType(conteneurMessage, rechercheSequenceQO.getGroupeClasseSelectionne());
        
        //Droits 
        if (idClasseGroupe != null)  {

            if ( TypeGroupe.GROUPE == rechercheSequenceQO.getTypeGroupe()) {
                
                
                final boolean droitGroupe = groupeHibernateBusinessService
                        .checkDroitGroupe(
                                rechercheSequenceQO.getIdEnseignant(),
                                rechercheSequenceQO.getIdClasseGroupe(), false,
                                null);
                if (!droitGroupe) {
                    conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_01
                            .name(), Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                            "Le groupe existe mais vous n'avez pas les droits dessus.");
                }
                    
                
            } else if (TypeGroupe.CLASSE == rechercheSequenceQO.getTypeGroupe()) {
                
                final boolean droitClasse = classeHibernateBusinessService
                        .checkDroitClasse(
                                rechercheSequenceQO.getIdEnseignant(),
                                rechercheSequenceQO.getIdClasseGroupe(), false,
                                null);
                if (!droitClasse) {
                    conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_01
                            .name(), Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                            "La classe existe mais vous n'avez pas les droits dessus.");
                }

            }
        } else {
            rechercheSequenceQO.setIdClasseGroupe(null);
        }
        

        return sequenceHibernateBusinessService.findSequence(rechercheSequenceQO);
    }

    /**
     * {@inheritDoc}
     */
    public List<SeanceDTO> findSeanceBySequence(ResultatRechercheSequenceDTO resultatRechercheSequenceDTO)
                                         throws MetierException {
        Assert.isNotNull("resultatRechercheSequenceDTO", resultatRechercheSequenceDTO);
        return seanceHibernateBusinessService.findSeanceBySequence(resultatRechercheSequenceDTO);
        
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> deleteSequence(ResultatRechercheSequenceDTO resultatRechercheSequenceDTO)
                                        throws MetierException {
        Assert.isNotNull("resultatRechercheSequenceDTO", resultatRechercheSequenceDTO);

        final ResultatDTO<Integer> resultatDTO = new ResultatDTO<Integer>();

        final SequenceBean sequenceBean =
            sequenceHibernateBusinessService.find(resultatRechercheSequenceDTO.getId());

        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        if (sequenceBean == null) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_12.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "La séquence selectionnée n'existe pas.");
        }

        final List<SeanceDTO> listeSeanceDTO =
            seanceHibernateBusinessService.findSeanceBySequence(resultatRechercheSequenceDTO);

        if (listeSeanceDTO.size() > 0) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_13.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "Impossible de supprimer la séquence. Il existe des séances rattachées à cette séquence.");
        } else {
            sequenceHibernateBusinessService.deleteSequence(resultatRechercheSequenceDTO);
        }

        conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                                         Nature.INFORMATIF, "La séquence", "supprimée"));
        resultatDTO.setConteneurMessage(conteneurMessage);

        return resultatDTO;
    }

    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Boolean> deleteSequencesVide(RechercheSequenceQO rechercheSequenceQO) throws MetierException {
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        final ResultatDTO<Boolean> resultatDTO = new ResultatDTO<Boolean>();
        resultatDTO.setConteneurMessage(conteneurMessage);
        
        sequenceHibernateBusinessService.deleteSequencesVide(rechercheSequenceQO);
        resultatDTO.setValeurDTO(true);
        conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                Nature.INFORMATIF, "La suppression des séquences sans séance ", "prise en compte"));
        
        return resultatDTO;
    }
    
    /**
     * {@inheritDoc}
     */
    public SequenceBean find(Integer idSequence) throws MetierException {
        return sequenceHibernateBusinessService.find(idSequence);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveSequence(SaveSequenceQO saveSequenceQO)
                                      throws MetierException {
        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        final ResultatDTO<Integer> resultatDTO = new ResultatDTO<Integer>();

        if (StringUtils.isEmpty(saveSequenceQO.getEnseignementSelectionne().getCode())) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_06.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage, "L'enseignement est obligatoire.");
        }

        //On vérifie que l'enseignement sélectionné existe.
        final String codeEnseignement =
            saveSequenceQO.getEnseignementSelectionne().getCode();
        final Integer idEnseignement = this.existEnseignement(codeEnseignement);
        
        if (!ObjectUtils.equals(idEnseignement, saveSequenceQO.getEnseignementSelectionne().getId())) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_02.name(),
                    Nature.BLOQUANT));
            throw new MetierException(conteneurMessage, "L'enseignement n'est pas valide.");
        }
        

        if (!StringUtils.isEmpty(codeEnseignement)) {
            final boolean droitEnseignement =
                enseignementHibernateBusinessService.checkDroitEnseignement(saveSequenceQO.getIdEnseignant(),
                                                                            saveSequenceQO.getIdEnseignement(),
                                                                            saveSequenceQO.getIdEtablissement());
            if (!droitEnseignement) {
                conteneurMessage.add(new Message(TypeReglesEnseignement.ENSEIGNEMENT_01.name(),
                                                 Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                                          "L'enseignement existe mais vous n'avez pas les droits dessus.");
            }
        }

        if (StringUtils.isEmpty(saveSequenceQO.getClasseGroupeSelectionne().getCode())) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_07.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "La sélection d'un groupe ou d'une classe est obligatoire.");
        }

        //On vérifie que le groupe/classe sélectionné existe.
        final String codeClasseGroupe =
            saveSequenceQO.getClasseGroupeSelectionne().getCode();
        final TypeGroupe typeClasseGroupe = saveSequenceQO.getClasseGroupeSelectionne().getTypeGroupe();
        final boolean existsClasseGroupe =
            null != findClasseGroupeId(saveSequenceQO.getClasseGroupeSelectionne(), false, null);
        
        if (!existsClasseGroupe) {
            conteneurMessage.add(new Message(
                    BooleanUtils.isTrue(saveSequenceQO.getClasseGroupeSelectionne().getVraiOuFauxClasse()) ?
                    TypeReglesSequence.SEQUENCE_02.name() :
                        TypeReglesSequence.SEQUENCE_03.name()    
                        ,
                    Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
             "La classe / groupe sélectionnée n'est pas valide.");
        }

        if (typeClasseGroupe == TypeGroupe.GROUPE) {
            if (!StringUtils.isEmpty(codeClasseGroupe)) {
                final boolean droitGroupe =
                    groupeHibernateBusinessService.checkDroitGroupe(saveSequenceQO.getIdEnseignant(),
                                                                    saveSequenceQO.getIdClasseGroupe(),
                                                                    false, null);
                if (!droitGroupe) {
                    conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_01.name(),
                                                     Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                                              "Le groupe existe mais vous n'avez pas les droits dessus.");
                }
            }
        } else if (typeClasseGroupe == TypeGroupe.CLASSE) {
            if (!StringUtils.isEmpty(codeClasseGroupe)) {
                final boolean droitClasse =
                    classeHibernateBusinessService.checkDroitClasse(saveSequenceQO.getIdEnseignant(),
                                                                    saveSequenceQO.getIdClasseGroupe(),
                                                                    false, null);
                if (!droitClasse) {
                    conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_01.name(),
                                                     Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                                              "La classe existe mais vous n'avez pas les droits dessus.");
                }
            }
        }
        if (StringUtils.isEmpty(saveSequenceQO.getIntitule())) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_08.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "La description est obligatoire.");
        }
        if (StringUtils.isEmpty(saveSequenceQO.getDescription())) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_25.name(),
                    Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
             "L'intitulé de la séquence est obligatoire.");
        }
        final Date dateDebut = saveSequenceQO.getDateDebut();
        if (dateDebut == null) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_09.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "La date de début de la séquence est obligatoire.");
        }

        final Date dateFin = saveSequenceQO.getDateFin();
        if (dateFin == null) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_10.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "La date de fin de la séquence est obligatoire.");
        }

        //On vérifie que les dates de la séquence sont bien comprises dans l'année scolaire du contexte utilisateur.
        final Integer idAnneeScolaire = saveSequenceQO.getClasseGroupeSelectionne().getIdAnneeScolaire();
        final boolean checkAnneeScolaire =
            anneeScolaireHibernateBusinessService.checkDateAnneeScolaire(idAnneeScolaire,
                                                                         dateDebut,
                                                                         dateFin);
        if (!checkAnneeScolaire) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_21.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "Les dates de de début et de fin de la séquence doivent être comprises dans l'année scolaire.");
        }

        //Règles de gestion SEQUENCE11
        if (!this.checkSEQUENCE11(dateDebut, dateFin)) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_11.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "La date de fin de pèriode doit être postèrieure ou égale à la date de début.");
        }

        //Si on a des séances associées (cas de modification) alors on restreint la modification des dates
        if (saveSequenceQO.getSeanceAssociee()) {
            final ResultatRechercheSequenceDTO resultatRechercheSequenceDTO =
                new ResultatRechercheSequenceDTO();
            resultatRechercheSequenceDTO.setId(saveSequenceQO.getId());
            final List<SeanceDTO> listeSeanceDTO =
                seanceHibernateBusinessService.findSeanceBySequence(resultatRechercheSequenceDTO);
            for (final SeanceDTO seance : listeSeanceDTO) {
                if (DateUtils.compare(dateDebut, seance.getDate(), false) > 0) {
                    conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_22.name(),
                                                     Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                                              "Il existe des séances rattachées à cette séquence, la date de début doit être antèrieure " +
                                              "ou égale à toute date de séance.");
                }
                if (DateUtils.compare(seance.getDate(), dateFin, false) > 0) {
                    conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_23.name(),
                                                     Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                                              "Il existe des séances rattachées à cette séquence, la date de fin doit être postèrieure " +
                                              "ou égale à toute date de séance.");
                }
            }
        }

        //On controle l'unicité de la séquence
        if (!checkUnicite(saveSequenceQO)) {
            if (TypeGroupe.CLASSE == typeClasseGroupe) {
                conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_19.name(),
                                                 Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                                          "Il existe déjà une séquence rattachée à cet enseignement, cette classe et cet intitulé.");
            } else if (TypeGroupe.GROUPE == typeClasseGroupe) {
                conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_20.name(),
                                                 Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                                          "Il existe déjà une séquence rattachée à cet enseignement, ce groupe et cet intitulé.");
            }
        }

        final Integer idSequence =
            sequenceHibernateBusinessService.saveSequence(saveSequenceQO);
        
     // Sauvegarde de l'association enseignant / etablissement / enseignement / classe (ou) groupe / couleur
        TypeCouleur couleur = saveSequenceQO.getTypeCouleur();
        if (couleur != null) {
        	SaveCouleurEnseignementClasseQO scecQO = new SaveCouleurEnseignementClasseQO();
        	scecQO.setIdEnseignant(saveSequenceQO.getIdEnseignant());
        	scecQO.setIdEtablissement(saveSequenceQO.getIdEtablissement());
        	scecQO.setIdEnseignement(saveSequenceQO.getIdEnseignement());
        	scecQO.setClasseGroupe(saveSequenceQO.getClasseGroupeSelectionne());
        	scecQO.setTypeCouleur(couleur);
        	couleurEnseignementClasseHibernateBusinessService.save(scecQO);
        }

        final SaveEnseignementQO saveEnseignementQO = new SaveEnseignementQO();
        saveEnseignementQO.setIdSequence(idSequence);
        saveEnseignementQO.setIdEnseignement(idEnseignement);
        if (TypeGroupe.GROUPE == typeClasseGroupe) {
            saveEnseignementQO.setTypeClasseGroupe(TypeGroupe.GROUPE);
            saveEnseignementQO.setIdGroupe(saveSequenceQO.getClasseGroupeSelectionne().getId());
            saveEnseignementQO.setIdClasse(0);
        } else if (TypeGroupe.CLASSE == typeClasseGroupe) {
            saveEnseignementQO.setTypeClasseGroupe(TypeGroupe.CLASSE);
            saveEnseignementQO.setIdClasse(saveSequenceQO.getClasseGroupeSelectionne().getId());
            saveEnseignementQO.setIdGroupe(0);
        }
        if (AbstractForm.MODE_AJOUT.equals(saveSequenceQO.getMode()) ||
                AbstractForm.MODE_DUPLICATE.equals(saveSequenceQO.getMode())) {
            saveEnseignementQO.setMode(AbstractForm.MODE_AJOUT);
        } else if (AbstractForm.MODE_MODIF.equals(saveSequenceQO.getMode())) {
            saveEnseignementQO.setMode(AbstractForm.MODE_MODIF);
            saveEnseignementQO.setOldIdEnseignement(saveSequenceQO.getOldIdEnseignement());
            saveEnseignementQO.setOldIdGroupeClasse(saveSequenceQO.getOldIdGroupeClasse());
            saveEnseignementQO.setOldIdSequence(saveSequenceQO.getOldIdSequence());
            saveEnseignementQO.setOldTypeGroupeSelectionne(saveSequenceQO.getOldTypeGroupeSelectionne());
        }

        resultatDTO.setValeurDTO(idSequence);

        if (AbstractForm.MODE_AJOUT.equals(saveSequenceQO.getMode())) {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                                             Nature.INFORMATIF, "La séquence", "ajoutée"));
        } else if (AbstractForm.MODE_MODIF.equals(saveSequenceQO.getMode())) {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                                             Nature.INFORMATIF, "La séquence", "modifiée"));
        } else if (AbstractForm.MODE_DUPLICATE.equals(saveSequenceQO.getMode())) {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                    Nature.INFORMATIF, "La séquence", "dupliquée"));
}
        resultatDTO.setConteneurMessage(conteneurMessage);

        return resultatDTO;
    }

    /**
     * Regle SEQUENCE_11 La date de fin de pèriode doit être postèrieure ou égale
     * à la date de début.
     *
     * @param dateDebut date de début
     * @param dateFin date de fin
     *
     * @return true si tout est ok, sinon false
     */
    private boolean checkSEQUENCE11(Date dateDebut, Date dateFin) {
        boolean checkResult = true;
        if ((dateDebut != null) && (dateFin != null)) {
            if (DateUtils.compare(dateDebut, dateFin, false) > 0) {
                checkResult = false;
            }
        }
        return checkResult;
    }

    /**
     * Regle qui vérifie l'unicité enseignant, enseignement, classe/groupe et
     * intitulé.
     *
     * @param saveSequenceQO saveSequenceQO
     *
     * @return true si unique, sinon false
     *
     * @throws MetierException Exception
     */
    public boolean checkUnicite(SaveSequenceQO saveSequenceQO)
                         throws MetierException {
        final boolean checkResult =
            sequenceHibernateBusinessService.checkUnicite(saveSequenceQO);
        return checkResult;
    }

    /**
     * {@inheritDoc}
     */
    public SequenceDTO findSequenceAffichage(final SequenceAffichageQO sequenceAffichageQO)
                                      throws MetierException {
        return sequenceHibernateBusinessService.findSequenceAffichage(sequenceAffichageQO);
    }

    /**
     * Vérifie qu'un enseignement existe (par son code) et retourne son id.
     *
     * @param codeEnseignement le code de l'enseignement
     *
     * @return L'id de l'enseignement
     *
     * @exception MetierException Exception
     */
    public Integer existEnseignement(String codeEnseignement)
                              throws MetierException {
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        if (!StringUtils.isEmpty(codeEnseignement)) {
            final EnseignementBean enseignementBean =
                enseignementHibernateBusinessService.findByCode(codeEnseignement);
            if (enseignementBean == null) {
                conteneurMessage.add(new Message(TypeReglesEnseignement.ENSEIGNEMENT_00.name(),
                                                 Nature.BLOQUANT));
                throw new MetierException(conteneurMessage, "L'enseignement n'existe pas.");
            }
            return enseignementBean.getId();
        }
        return null;
    }
    
    /**
     * Si la groupe ou classe type est saisie, verifier qu'il y a une classe / groupe.
     * @param conteneurMessage cm 
     * @param groupesClassesDTO gc
     * @throws MetierException ex
     */
    public void verifierClasseGroupeSaisieAvecType(ConteneurMessage conteneurMessage, GroupesClassesDTO groupesClassesDTO) throws MetierException {
        if (groupesClassesDTO.getId() == null
                && groupesClassesDTO.getTypeGroupe() != null) {
            if (groupesClassesDTO.getTypeGroupe() == TypeGroupe.GROUPE) {
                conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_02
                        .name(), Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                        "Vous devez sélectionner ou saisir un groupe.");
            } else {
                conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_02
                        .name(), Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                        "Vous devez sélectionner ou saisir une classe.");
            }
        }
    }

    /**
     * Vérifie qu'une classe ou qu'un groupe existe (par son id sinon code) et retourne
     * son id.
     *
     * @param groupesClassesDTO le classe/groupe
     *
     * @return l'id si la classe ou du groupe existe, groupeClasseDTO.id sera renseigné également; null sinon 
     *
     * @exception MetierException si l'id ou code ne sont pas valides.
     */
    public Integer findClasseGroupeId(GroupesClassesDTO groupesClassesDTO, final Boolean archive, final String exercice)
                              throws MetierException {
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        
        if (groupesClassesDTO == null) {
            return null;
        }
        
        String codeClasseGroupe = groupesClassesDTO.getCode();
        Integer idClasseGroupe = groupesClassesDTO.getId();
        TypeGroupe typeClasseGroupe = groupesClassesDTO.getTypeGroupe();
        
        if (StringUtils.isEmpty(codeClasseGroupe) && idClasseGroupe == null) {
            return null;
        }
        
        if (typeClasseGroupe == null) {
            return null;
        }
        
        //Besoin du code pour les archives
        if (org.apache.commons.lang.BooleanUtils.isTrue(archive) && StringUtils.trimToNull(codeClasseGroupe) == null) {
            return null;
        }
        
        //Trouve l'id
        if (idClasseGroupe == null && TypeGroupe.GROUPE == typeClasseGroupe) {
            idClasseGroupe = groupeHibernateBusinessService
                    .findByCode(codeClasseGroupe, archive, exercice);
                        if (idClasseGroupe == null) {
                            conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_00
                                    .name(), Nature.BLOQUANT));
                            throw new MetierException(conteneurMessage,
                                    "Le groupe n'existe pas.");
                        }
                        
        } else if (idClasseGroupe == null && TypeGroupe.CLASSE == typeClasseGroupe) {
            
            idClasseGroupe = classeHibernateBusinessService
                    .findByCode(codeClasseGroupe, archive, exercice);
            if (idClasseGroupe == null) {
                conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_00
                        .name(), Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                        "La classe n'existe pas.");
            }
            
        } 
        
        //Verifier l'id (pas pour les archives)
        if (typeClasseGroupe == TypeGroupe.CLASSE && org.apache.commons.lang.BooleanUtils.isNotTrue(archive)) {
            classeHibernateBusinessService.exist(idClasseGroupe);
        } else if (typeClasseGroupe == TypeGroupe.GROUPE && org.apache.commons.lang.BooleanUtils.isNotTrue(archive)) {
            groupeHibernateBusinessService.exist(idClasseGroupe);
        } 
        
        groupesClassesDTO.setId(idClasseGroupe);
        return idClasseGroupe;
    }

    /**
     * {@inheritDoc}
     */
    public Report printSequence(PrintSeanceOuSequenceQO printSequenceQO, List<PrintSequenceDTO> listeSequences)
                         throws MetierException {
        Assert.isNotNull("printSequenceQO", printSequenceQO);

        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        

        final Map<String, Object> args = new HashMap<String, Object>();

        //Si il y a des résultats
        if (CollectionUtils.isEmpty(listeSequences)) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_24.name(),
                    Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
             "Il n'existe aucun résultat pour votre recherche.");

        }
        //On englobe la printSequenceDTO dans printSuperSequenceDTO pour effectuer le regroupement plus facilement dans ireport.
        for (final PrintSequenceDTO printSequenceDTO : listeSequences) {

            //Séance pour la classe xxx OU Séance pour le groupe xxx
            String libelleCLasseGroupe = "";
            if (!StringUtils.isEmpty(printSequenceDTO.getGroupesClassesDTO().getDesignation()) && 
                    printSequenceDTO.getGroupesClassesDTO().getTypeGroupe() == TypeGroupe.CLASSE) {
                //C'est pour une classe
                libelleCLasseGroupe += (
                                           "pour la classe " +
                                                   printSequenceDTO.getGroupesClassesDTO().getDesignation()
                                       );
            } else if (!StringUtils.isEmpty(printSequenceDTO.getGroupesClassesDTO().getDesignation())) {
                //C'est pour un groupe
                libelleCLasseGroupe += (
                                           "pour le groupe " +
                                                   printSequenceDTO.getGroupesClassesDTO().getDesignation()
                                       );
            }
            printSequenceDTO.setLibelleClasseGroupe(libelleCLasseGroupe);
            

            
            ImagesServlet.processPrintSequenceDTO(printSequenceDTO);

        }

        args.put("dateCourante", DateUtils.format(printSequenceQO.getDateCourante()));
        args.put("dateDebut", DateUtils.format(printSequenceQO.getDateDebut()));
        args.put("dateFin", DateUtils.format(printSequenceQO.getDateFin()));
        
        //On formalise l'entete PDF.
        String entetePdf = " ";
        final List<GroupeDTO> listeGroupeDTO = printSequenceQO.getListeGroupeDTO();
        final String intituleGoupeOuClasse = StringUtils.trimToEmpty(printSequenceQO.getGroupeClasseSelectionne().getIntitule());
        if (TypeGroupe.CLASSE == printSequenceQO.getGroupeClasseSelectionne().getTypeGroupe()) {
            entetePdf += (
                             " pour la classe " +
                             intituleGoupeOuClasse
                         );
            final int tailleListeGroupe = listeGroupeDTO.size();
            if (tailleListeGroupe > 0) {
                entetePdf += (tailleListeGroupe > 1) ? " et les groupes " : " et le groupe ";
                for (final GroupeDTO groupe : listeGroupeDTO) {
                    entetePdf += (groupe.getIntitule() + " ");
                }
            }
        } else if (TypeGroupe.GROUPE == printSequenceQO.getGroupeClasseSelectionne().getTypeGroupe()) {
            entetePdf += (
                             " pour le groupe " +
                             intituleGoupeOuClasse
                         );
        }

        args.put("codeClasseGroupe", entetePdf);
        args.put("designationEtablissement",
                printSequenceQO.getDesignationEtablissement());
            
                           
        args.put("vraiOuFauxSautPage", printSequenceQO.getVraiOuFauxSautDePage());
        args.put("codePremiereSequence", listeSequences.get(0).getCode());
             
          
        // En fonction du mode d'affichage (simple ou détaillé) on génère le
        // pdf.
        final PdfReportGenerator gen = new PdfReportGenerator();
        if (TypeAffichage.SIMPLE == printSequenceQO.getAffichage()) {
            args.put("afficheDevoirs", false);

            return gen.generate(ClassUtils.getPathToReport()
                    + "sequenceDetail.jasper", listeSequences, args);

        } else if (TypeAffichage.DETAILLE == printSequenceQO.getAffichage()) {

            args.put("afficheDevoirs", true);
            return gen.generate(ClassUtils.getPathToReport()
                    + "sequenceDetail.jasper", listeSequences, args);
        }
            
        
        return null;
    }
     
    /**
     * {@inheritDoc} 
     */
    public Boolean checkExistenceSequenceEnseignant(final Integer idEnseignant, final Integer idEtablissement) {
        return sequenceHibernateBusinessService.checkExistenceSequenceEnseignant(idEnseignant, idEtablissement);
    }
    
    /**
     * {@inheritDoc}
     */
    //FIXME verifier que le saveSequenceSimplifieeQO.getTitreIntitule est bien setter partout
    public ResultatDTO<Integer> saveSequenceSaisieSimplifiee(final SaveSequenceSimplifieeQO saveSequenceSimplifieeQO) throws MetierException {
        Assert.isNotNull("saveSequenceSimplifieeQO", saveSequenceSimplifieeQO);
        
        final AnneeScolaireDTO anneeScolaireDTO = saveSequenceSimplifieeQO.getAnneeScolaireDTO();
        final Integer idAnneeSco = anneeScolaireDTO.getId();
        final Integer idEnseignant = saveSequenceSimplifieeQO.getIdEnseignant();
        final Integer idEtablissement = saveSequenceSimplifieeQO.getIdEtablissement();
        final Boolean vraiOuFauxSaisieSimplifiee = BooleanUtils.isTrue(saveSequenceSimplifieeQO.getVraiOuFauxSaisieSimplifiee());
        Assert.isNotNull("idEnseignant", idEnseignant);
        Assert.isNotNull("idEtablissement", idEtablissement);
        //Il faut à minima ces deux dates pour les périodes.
        Assert.isNotNull("dateRentree", anneeScolaireDTO.getDateRentree());
        Assert.isNotNull("dateSortie", anneeScolaireDTO.getDateSortie());
        final List<GenericDTO<Date, Date>> periodes = GenerateurDTO.generatePeriodes(anneeScolaireDTO);
        
        final ResultatDTO<Integer> resultatDTO = new ResultatDTO<Integer>();
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        
        //contrôle d'une modification.
        final SaisieSimplifieeDTO vraiOuFauxDejaSaisieSimp = 
            etablissementHibernateBusinessService.findSaisieSimplifieeEtablissement(idEtablissement, idEnseignant);
        
        if (vraiOuFauxSaisieSimplifiee) {            
            if (vraiOuFauxDejaSaisieSimp.getVraiOuFauxExiste() && BooleanUtils.isTrue(vraiOuFauxDejaSaisieSimp.getVraiOuFauxsaisieSimpliee())) {
                conteneurMessage.add(new Message(TypeReglesAdmin.ADMIN_13.name(), "active"));
                throw new MetierException(conteneurMessage, "echec de la sauvegarde de l'activation de la génération automatique des séquences");
            }
            
            if (sequenceHibernateBusinessService.checkExistenceSequenceEnseignant(idEnseignant, idEtablissement)) {
               conteneurMessage.add(new Message(TypeReglesAdmin.ADMIN_15.name()));
               throw new MetierException(conteneurMessage, "echec de la sauvegarde de l'activation de la génération automatique des séquences");
            }
            
            //recherche des classes de l'enseignant
            final RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO = new RechercheGroupeClassePopupQO();
            rechercheGroupeClassePopupQO.setIdAnneeScolaire(idAnneeSco);
            rechercheGroupeClassePopupQO.setExerciceScolaire(anneeScolaireDTO.getExercice());
            rechercheGroupeClassePopupQO.setIdEnseignant(idEnseignant);        
            rechercheGroupeClassePopupQO.setIdEtablissement(idEtablissement);
            rechercheGroupeClassePopupQO.setArchive(false);
            final ResultatDTO<List<GroupesClassesDTO>> resultatClasse = classeHibernateBusinessService.findClassePopup(rechercheGroupeClassePopupQO);
            //recherche des groupes de l'enseignant        
            final ResultatDTO<List<GroupesClassesDTO>> resultatGroupe = groupeHibernateBusinessService.findGroupePopup(rechercheGroupeClassePopupQO);
            //recherche des enseignements
            final RechercheEnseignementPopupQO rechercheEnseignementPopupQO = new RechercheEnseignementPopupQO();
            rechercheEnseignementPopupQO.setIdEnseignant(idEnseignant);
            rechercheEnseignementPopupQO.setIdEtablissement(idEtablissement);

            final ResultatDTO<List<EnseignementDTO>> resultatEnseignement = 
                enseignementHibernateBusinessService.findEnseignementPopup(rechercheEnseignementPopupQO);

            int compteurSequence = 0;
            
            for (GenericDTO<Date, Date> dates : periodes){
                final SaveSequenceQO saveSequenceQO = new SaveSequenceQO();
                saveSequenceQO.setDateDebut(dates.getValeur1());
                saveSequenceQO.setDateFin(dates.getValeur2());
                saveSequenceQO.setIdEnseignant(idEnseignant);
                saveSequenceQO.setIdEtablissement(idEtablissement);
                
                final String prefixDescSequ = "Séquence automatique - ";

                for (final EnseignementDTO enseignementDTO : resultatEnseignement.getValeurDTO()) {
                    saveSequenceQO.setEnseignementSelectionne(enseignementDTO);
                    final String prefixSequ =  StringUtils.abbreviate(enseignementDTO.getIntitule(), 25);
                    //création d'une séquence sur l'enseignement en cours pour chaque classe de l'enseignant.
                    for (final GroupesClassesDTO classe : resultatClasse.getValeurDTO()) {               
                        String desc = prefixDescSequ + enseignementDTO.getIntitule(); 
                        saveSequenceQO.setIntitule(prefixSequ + " / " + StringUtils.abbreviate(classe.getIntitule(),22));
                        if (TypeGroupe.CLASSE.equals(classe.getTypeGroupe())) {
                            desc += " - Classe ";
                        } else {
                            desc += " - Groupe ";
                        }
                        desc += classe.getIntitule();
                        saveSequenceQO.setDescription(desc);
                        saveSequenceQO.setClasseGroupeSelectionne(classe);
                        sequenceHibernateBusinessService.saveSequence(saveSequenceQO);

                        compteurSequence++;
                    }          

                    //création d'une séquence sur l'enseignement en cours pour chaque groupe de l'enseignant.
                    for (final GroupesClassesDTO groupe : resultatGroupe.getValeurDTO()) {                
                        saveSequenceQO.setIntitule(prefixSequ + " / " + StringUtils.abbreviate(groupe.getIntitule(),22));
                        saveSequenceQO.setDescription(prefixDescSequ + enseignementDTO.getIntitule() + " et le groupe " + groupe.getIntitule());
                        saveSequenceQO.setClasseGroupeSelectionne(groupe);
                        sequenceHibernateBusinessService.saveSequence(saveSequenceQO);

                        compteurSequence++;
                    }
                }
            }
            
            //mise à jour des préférences de l'enseignant sur l'établissement.
            etablissementHibernateBusinessService.saveEtablissementSaisieSimplifiee(new SaisieSimplifieeQO(idEtablissement, 
                    idEnseignant, vraiOuFauxSaisieSimplifiee, vraiOuFauxDejaSaisieSimp.getVraiOuFauxExiste()));
            
            if (compteurSequence == 0){
                conteneurMessage.add(new Message(TypeReglesAdmin.ADMIN_21.name()));
                /*throw new MetierException(conteneurMessage, 
                        "Aucune séquence n'a pu être généré car l'enseignant n'a pas d'enseignement ou de classe/groupe.");*/
            } else {
                conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_01.name(),
                        Nature.INFORMATIF, compteurSequence + " séquences", "créées"));
            }

        } else {
            if (vraiOuFauxDejaSaisieSimp.getVraiOuFauxExiste() && BooleanUtils.isFalse(vraiOuFauxDejaSaisieSimp.getVraiOuFauxsaisieSimpliee())) {
                conteneurMessage.add(new Message(TypeReglesAdmin.ADMIN_13.name(), "inactive"));
                throw new MetierException(conteneurMessage, "echec de la sauvegarde de l'activation de la génération automatique des séquences");
            }          
            
            //mise à jour des préférences de l'enseignant sur l'établissement.
            etablissementHibernateBusinessService.saveEtablissementSaisieSimplifiee(new SaisieSimplifieeQO(idEtablissement, 
                    idEnseignant, vraiOuFauxSaisieSimplifiee, vraiOuFauxDejaSaisieSimp.getVraiOuFauxExiste()));
            
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                    Nature.INFORMATIF, "La désactivation de la génération automatique des séquences", "prise en compte"));
        }
        
        resultatDTO.setConteneurMessage(conteneurMessage);
        
        return resultatDTO;
    }
    
    
    /**
     * Ajout des sequences manquantes en cas de gestion automatique (simplifié) .
     * ne modifie pas le flag SaisieSimplifiee de l'enseignant.
     * @param saveSequenceSimplifieeQO
     * @return
     * @throws MetierException
     */
    @Override
    public ResultatDTO<Integer> ajoutSequenceManquanteSaisieSimplifiee(final SaveSequenceSimplifieeQO saveSequenceSimplifieeQO) throws MetierException {
        Assert.isNotNull("saveSequenceSimplifieeQO", saveSequenceSimplifieeQO);
        
        final AnneeScolaireDTO anneeScolaireDTO = saveSequenceSimplifieeQO.getAnneeScolaireDTO();
        final Integer idAnneeSco = anneeScolaireDTO.getId();
        final Integer idEnseignant = saveSequenceSimplifieeQO.getIdEnseignant();
        final Integer idEtablissement = saveSequenceSimplifieeQO.getIdEtablissement();
        final Boolean vraiOuFauxSaisieSimplifiee = BooleanUtils.isTrue(saveSequenceSimplifieeQO.getVraiOuFauxSaisieSimplifiee());
        Assert.isNotNull("idEnseignant", idEnseignant);
        Assert.isNotNull("idEtablissement", idEtablissement);
        //Il faut à minima ces deux dates pour les périodes.
        Assert.isNotNull("dateRentree", anneeScolaireDTO.getDateRentree());
        Assert.isNotNull("dateSortie", anneeScolaireDTO.getDateSortie());
        final List<GenericDTO<Date, Date>> periodes = GenerateurDTO.generatePeriodes(anneeScolaireDTO);
        
        final ResultatDTO<Integer> resultatDTO = new ResultatDTO<Integer>();
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        
        
        if (vraiOuFauxSaisieSimplifiee) {            
           
        		// recherche des sequences deja existantes pour l'enseignant dans l'établissement
         	Set<SequenceDTO> sequencesExistantes = sequenceHibernateBusinessService.findSequenceEnseignant(idEnseignant, idEtablissement);
         		// creattion des  maps enseignement => classes et enseignement => groupes
         	
         	
         	HashMap<Integer, Set<Integer>> enseignement2classes = new HashMap<Integer, Set<Integer>>();
         	HashMap<Integer, Set<Integer>> enseignement2groupes = new HashMap<Integer, Set<Integer>>();
         	
         	for (SequenceDTO sequenceDTO : sequencesExistantes) {
         		if (sequenceDTO != null) {
         			Integer idEnseignement = sequenceDTO.getIdEnseignement();
         			GroupesClassesDTO gcDto = sequenceDTO.getGroupesClassesDTO();
         			if (idEnseignement != null && gcDto != null) {
         				
         				Integer idGroupOrClass =  gcDto.getId();
         				
         				Set<Integer> idSet;
         				
         				HashMap<Integer, Set<Integer>> ens2GroupOrClasse;
         				
         				if (gcDto.getTypeGroupe() == TypeGroupe.CLASSE) {
         					ens2GroupOrClasse = enseignement2classes;
         				} else {
         					ens2GroupOrClasse = enseignement2groupes;
         				}
         				
         				idSet = ens2GroupOrClasse.get(idEnseignement);
         				
         				if (idSet == null) {
         					idSet = new HashSet<Integer>();
         					ens2GroupOrClasse.put(idEnseignement, idSet);
         				}
         				
         				idSet.add(idGroupOrClass);
         			}
         		}
			}
         	
            //recherche des classes de l'enseignant
            final RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO = new RechercheGroupeClassePopupQO();
            rechercheGroupeClassePopupQO.setIdAnneeScolaire(idAnneeSco);
            rechercheGroupeClassePopupQO.setExerciceScolaire(anneeScolaireDTO.getExercice());
            rechercheGroupeClassePopupQO.setIdEnseignant(idEnseignant);        
            rechercheGroupeClassePopupQO.setIdEtablissement(idEtablissement);
            rechercheGroupeClassePopupQO.setArchive(false);
            final ResultatDTO<List<GroupesClassesDTO>> resultatClasse = classeHibernateBusinessService.findClassePopup(rechercheGroupeClassePopupQO);
            //recherche des groupes de l'enseignant        
            final ResultatDTO<List<GroupesClassesDTO>> resultatGroupe = groupeHibernateBusinessService.findGroupePopup(rechercheGroupeClassePopupQO);
            //recherche des enseignements
            final RechercheEnseignementPopupQO rechercheEnseignementPopupQO = new RechercheEnseignementPopupQO();
            rechercheEnseignementPopupQO.setIdEnseignant(idEnseignant);
            rechercheEnseignementPopupQO.setIdEtablissement(idEtablissement);

            final ResultatDTO<List<EnseignementDTO>> resultatEnseignement = 
                enseignementHibernateBusinessService.findEnseignementPopup(rechercheEnseignementPopupQO);

            int compteurSequence = 0;
            
            for (GenericDTO<Date, Date> dates : periodes){
                final SaveSequenceQO saveSequenceQO = new SaveSequenceQO();
                saveSequenceQO.setDateDebut(dates.getValeur1());
                saveSequenceQO.setDateFin(dates.getValeur2());
                saveSequenceQO.setIdEnseignant(idEnseignant);
                saveSequenceQO.setIdEtablissement(idEtablissement);
                
                final String prefixDescSequ = "Séquence automatique - ";

                for (final EnseignementDTO enseignementDTO : resultatEnseignement.getValeurDTO()) {
                    saveSequenceQO.setEnseignementSelectionne(enseignementDTO);
                    final String prefixSequ =  StringUtils.abbreviate(enseignementDTO.getIntitule(), 25);
                    
                     // l'ensemble des classes avec une sequence déjà existante pour cette enseignement.
                    Set<Integer> classeWithSequence = enseignement2classes.get(enseignementDTO.getId());
                    
                    //création d'une séquence sur l'enseignement en cours pour chaque classe de l'enseignant.
                    for (final GroupesClassesDTO classe : resultatClasse.getValeurDTO()) {   
                    	
                    	// test si il existe déjà une sequence pour  cette classe
                    	
                    	if (classeWithSequence == null || !classeWithSequence.contains(classe.getId())) {
                    	
	                        String desc = prefixDescSequ + enseignementDTO.getIntitule(); 
	                        saveSequenceQO.setIntitule(prefixSequ + " / " + StringUtils.abbreviate(classe.getIntitule(),22));
	                      
	                        desc += " - Classe ";
	                     
	                        desc += classe.getIntitule();
	                        saveSequenceQO.setDescription(desc);
	                        saveSequenceQO.setClasseGroupeSelectionne(classe);
	                        sequenceHibernateBusinessService.saveSequence(saveSequenceQO);

	                        compteurSequence++;
                    	}
                    }          
                    	// l'ensemble des classes avec une sequence déjà existante pour cette enseignement.
                    Set<Integer> groupeWithSequence = enseignement2groupes.get(enseignementDTO.getId());
                    
                    //création d'une séquence sur l'enseignement en cours pour chaque groupe de l'enseignant.
                    for (final GroupesClassesDTO groupe : resultatGroupe.getValeurDTO()) {                
                    	if (groupeWithSequence == null || !groupeWithSequence.contains(groupe.getId())) {
	
	                    	saveSequenceQO.setIntitule(prefixSequ + " / " + StringUtils.abbreviate(groupe.getIntitule(),22));
	                        saveSequenceQO.setDescription(prefixDescSequ + enseignementDTO.getIntitule() + " et le groupe " + groupe.getIntitule());
	                        saveSequenceQO.setClasseGroupeSelectionne(groupe);
	                        sequenceHibernateBusinessService.saveSequence(saveSequenceQO);
	
	                        compteurSequence++;
	                    }
                    }
                }
            }
            
            if (compteurSequence == 0){
                conteneurMessage.add(new Message(TypeReglesAdmin.ADMIN_21.name()));
                /*throw new MetierException(conteneurMessage, 
                        "Aucune séquence n'a pu être généré car l'enseignant n'a pas d'enseignement ou de classe/groupe.");*/
            } else {
                conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_01.name(),
                        Nature.INFORMATIF, compteurSequence + " séquences", "créées"));
            }

        } else {
        	conteneurMessage.add(new Message(TypeReglesAdmin.ADMIN_21.name()));
        }
        
        resultatDTO.setConteneurMessage(conteneurMessage);
        
        return resultatDTO;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<EnseignantDTO> findEnseignantsEleve(
            Integer idClasse, Set<Integer> idsGroupe, Integer idEnseignement) {
        return sequenceHibernateBusinessService.findEnseignantsEleve(idClasse,idsGroupe, idEnseignement);
    }

    /**
     * {@inheritDoc}
     */
    public List<EnseignementDTO> findEnseignementsEleve(RechercheEnseignementPopupQO rechercheEnseignementPopupQO) {
        return sequenceHibernateBusinessService.findEnseignementsEleve(rechercheEnseignementPopupQO);
    }
    

    
}
