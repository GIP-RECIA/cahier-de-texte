/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.EtablissementSchemaQO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.base.TypeAffichage;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.TypeReglesAcquittement;
import org.crlr.dto.application.base.TypeReglesClasse;
import org.crlr.dto.application.base.TypeReglesEnseignement;
import org.crlr.dto.application.base.TypeReglesGroupe;
import org.crlr.dto.application.cycle.TypeReglesCycle;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.remplacement.RechercheRemplacementQO;
import org.crlr.dto.application.remplacement.RemplacementDTO;
import org.crlr.dto.application.remplacement.TypeReglesRemplacement;
import org.crlr.dto.application.seance.ConsulterSeanceDTO;
import org.crlr.dto.application.seance.ConsulterSeanceQO;
import org.crlr.dto.application.seance.PrintSeanceDTO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.dto.application.seance.ResultatRechercheSeanceDTO;
import org.crlr.dto.application.seance.SaveSeanceQO;
import org.crlr.dto.application.seance.TypeReglesSeance;
import org.crlr.dto.application.sequence.PrintSeanceOuSequenceQO;
import org.crlr.dto.application.sequence.PrintSequenceDTO;
import org.crlr.dto.application.sequence.SequenceAffichageQO;
import org.crlr.dto.application.sequence.TypeReglesSequence;
import org.crlr.exception.metier.MetierException;
import org.crlr.intercepteur.hibernate.SchemaInterceptorImpl;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.business.AnneeScolaireHibernateBusinessService;
import org.crlr.metier.business.ClasseHibernateBusinessService;
import org.crlr.metier.business.DevoirHibernateBusinessService;
import org.crlr.metier.business.EnseignantHibernateBusinessService;
import org.crlr.metier.business.EnseignementHibernateBusinessService;
import org.crlr.metier.business.GroupeHibernateBusinessService;
import org.crlr.metier.business.PieceJointeHibernateBusinessService;
import org.crlr.metier.business.SchemaHibernateBusinessService;
import org.crlr.metier.business.SeanceHibernateBusinessService;
import org.crlr.metier.business.SequenceHibernateBusinessService;
import org.crlr.metier.business.VisaHibernateBusinessService;
import org.crlr.metier.entity.ClasseBean;
import org.crlr.metier.entity.EnseignementBean;
import org.crlr.metier.entity.GroupeBean;
import org.crlr.metier.entity.SequenceBean;
import org.crlr.metier.utils.SchemaUtils;
import org.crlr.report.Report;
import org.crlr.report.impl.PdfReportGenerator;
import org.crlr.services.ImagesServlet;
import org.crlr.utils.Assert;
import org.crlr.utils.ClassUtils;
import org.crlr.utils.DateUtils;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.utils.FileUploadUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

/**
 * Façade concernant les fonctionnalités du module séance.
 *
 * @author breytond
 * @version $Revision: 1.43 $
 */
@Component
@Transactional(
        readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
    )
public class SeanceFacade implements SeanceFacadeService {
    
    protected final Logger log = LoggerFactory.getLogger(SeanceFacade.class);
    
    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private SeanceHibernateBusinessService seanceHibernateBusinessService;

    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private SequenceHibernateBusinessService sequenceHibernateBusinessService;
    
    @Autowired
    private SequenceFacadeService sequenceFacadeService;


    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private DevoirHibernateBusinessService devoirHibernateBusinessService;

    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private PieceJointeHibernateBusinessService pieceJointeHibernateBusinessService;

    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private EnseignementHibernateBusinessService enseignementHibernateBusinessService;

    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private GroupeHibernateBusinessService groupeHibernateBusinessService;

    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private ClasseHibernateBusinessService classeHibernateBusinessService;

    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private SchemaHibernateBusinessService schemaHibernateBusinessService;

    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private AnneeScolaireHibernateBusinessService anneeScolaireHibernateBusinessService;

    @Autowired
    private VisaHibernateBusinessService visaHibernateBusinessService;
    
    @Autowired
    private RemplacementFacadeService remplacementFacadeService;
    
    @Autowired
    private EnseignantHibernateBusinessService enseignantHibernateBusinessService;

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
     * Mutateur schemaHibernateBusinessService.
     *
     * @param schemaHibernateBusinessService le schemaHibernateBusinessService à
     *        modifier.
     */
    public void setSchemaHibernateBusinessService(SchemaHibernateBusinessService schemaHibernateBusinessService) {
        this.schemaHibernateBusinessService = schemaHibernateBusinessService;
    }

    /**
     * Mutateur seanceHibernateBusinessService.
     *
     * @param seanceHibernateBusinessService seanceHibernateBusinessService à modifier
     */
    public void setSeanceHibernateBusinessService(SeanceHibernateBusinessService seanceHibernateBusinessService) {
        this.seanceHibernateBusinessService = seanceHibernateBusinessService;
    }

    /**
     * Mutateur sequenceHibernateBusinessService.
     *
     * @param sequenceHibernateBusinessService sequenceHibernateBusinessService à
     *        modifier
     */
    public void setSequenceHibernateBusinessService(SequenceHibernateBusinessService sequenceHibernateBusinessService) {
        this.sequenceHibernateBusinessService = sequenceHibernateBusinessService;
    }

    /**
     * Mutateur devoirHibernateBusinessService.
     *
     * @param devoirHibernateBusinessService Le devoirHibernateBusinessService à modifier
     */
    public void setDevoirHibernateBusinessService(DevoirHibernateBusinessService devoirHibernateBusinessService) {
        this.devoirHibernateBusinessService = devoirHibernateBusinessService;
    }

    /**
     * Mutateur pieceJointeHibernateBusinessService.
     *
     * @param pieceJointeHibernateBusinessService Le pieceJointeHibernateBusinessService
     *        à modifier
     */
    public void setPieceJointeHibernateBusinessService(PieceJointeHibernateBusinessService pieceJointeHibernateBusinessService) {
        this.pieceJointeHibernateBusinessService = pieceJointeHibernateBusinessService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<ResultatRechercheSeanceDTO>> findSeance(RechercheSeanceQO rechercheSeanceQO)
        throws MetierException {
        Assert.isNotNull("rechercheSeanceQO", rechercheSeanceQO);

        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        //On vérifie que l'enseignement sélectionné existe.
        final String codeEnseignement = rechercheSeanceQO.getCodeEnseignement();
        final Integer idEnseignement = this.existEnseignement(codeEnseignement);
        rechercheSeanceQO.setIdEnseignement(idEnseignement);

        //On vérifie que l'on a bien les droits sur l'enseignement sélectionné
        if (!StringUtils.isEmpty(codeEnseignement)) {
            final boolean droitEnseignement =
                enseignementHibernateBusinessService.checkDroitEnseignement(rechercheSeanceQO.getIdEnseignant(),
                                                                            idEnseignement,
                                                                            rechercheSeanceQO.getIdEtablissement());
            if (!droitEnseignement) {
                conteneurMessage.add(new Message(TypeReglesEnseignement.ENSEIGNEMENT_01.name(),
                                                 Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                                          "L'enseignement existe mais vous n'avez pas les droits dessus.");
            }
        }

        //Si le type classe/groupe est sélectionné mais qu'on a pas saisie de code.
        if ((rechercheSeanceQO.getTypeGroupe() != null) &&
                !StringUtils.isEmpty(rechercheSeanceQO.getCodeClasseGroupe())) {
            //On vérifie que le groupe/classe sélectionné existe.
            final boolean existsClasseGroupe =
                null != this.sequenceFacadeService.findClasseGroupeId(rechercheSeanceQO.getGroupeClasseSelectionne(), 
                                       rechercheSeanceQO.getArchive(),
                                       rechercheSeanceQO.getExerciceAnneeScolaire());
            

            //On vérifie que l'on a bien les droits sur le groupe ou la classe sélectionné
            if (TypeGroupe.GROUPE  == (rechercheSeanceQO.getTypeGroupe())) {
                if (existsClasseGroupe) {
                    final boolean droitGroupe =
                        groupeHibernateBusinessService.checkDroitGroupe(rechercheSeanceQO.getIdEnseignant(),
                                                                        rechercheSeanceQO.getIdClasseGroupe(),
                                                                        rechercheSeanceQO.getArchive(),
                                                                        rechercheSeanceQO.getExerciceAnneeScolaire());
                    if (!droitGroupe) {
                        conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_01.name(),
                                                         Nature.BLOQUANT));
                        throw new MetierException(conteneurMessage,
                                                  "Le groupe existe mais vous n'avez pas les droits dessus.");
                    }
                }
            } else if (TypeGroupe.CLASSE == rechercheSeanceQO.getTypeGroupe()) {
                if (existsClasseGroupe) {
                    final boolean droitClasse =
                        classeHibernateBusinessService.checkDroitClasse(rechercheSeanceQO.getIdEnseignant(),
                                                                        rechercheSeanceQO.getIdClasseGroupe(),
                                                                        rechercheSeanceQO.getArchive(),
                                                                        rechercheSeanceQO.getExerciceAnneeScolaire());
                    if (!droitClasse) {
                        conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_01.name(),
                                                         Nature.BLOQUANT));
                        throw new MetierException(conteneurMessage,
                                                  "La classe existe mais vous n'avez pas les droits dessus.");
                    }
                }
            }
        } else {
            rechercheSeanceQO.setIdClasseGroupe(null);
        }

        //Vérifie que le code séquence existe.
        final String codeSequence = rechercheSeanceQO.getCodeSequence();
        final Integer idSequence = this.existSequence(codeSequence);
        rechercheSeanceQO.setIdSequence(idSequence);

        //S'il n'y a pas de sequence ni de classe on filtrera sur l'etablissement
        ResultatDTO<List<ResultatRechercheSeanceDTO>> resultat =
                seanceHibernateBusinessService.findSeance(rechercheSeanceQO);
        
        for(ResultatRechercheSeanceDTO seance : resultat.getValeurDTO()) {
            mettreDroitsAccess(rechercheSeanceQO.getIdEnseignantConnecte(), seance);
        }
        
        return resultat;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<SeanceDTO>> findSeanceForPlanning(
            RechercheSeanceQO rechercheSeanceQO) throws MetierException {
        Assert.isNotNull("rechercheSeanceQO", rechercheSeanceQO);
        if (rechercheSeanceQO.getIdEleve() != null){
            final List<ClasseBean> classes = classeHibernateBusinessService.findClassesEleve(rechercheSeanceQO.getIdEleve());
            rechercheSeanceQO.setListeClasseBean(classes);
            final List<GroupeBean> groupes = groupeHibernateBusinessService.findGroupesEleve(rechercheSeanceQO.getIdEleve());
            rechercheSeanceQO.setListeGroupeBean(groupes);
        }
        
        ResultatDTO<List<SeanceDTO>>  res = seanceHibernateBusinessService.findSeanceForPlanning(rechercheSeanceQO);
        
        for(SeanceDTO seance : res.getValeurDTO()) {
            
            mettreDroitsAccess(rechercheSeanceQO.getIdEnseignantConnecte(), seance);
        }
        
        return res;
    }
   
    /**
     * Complete les infos de la seance.
     * @param consulterSeanceDTO consulterSeanceDTO
     * @param seanceDTO seanceDTO
     * @param exercice exercice
     * @param idSeance idSeance
     * @param archive archive
     * @throws MetierException MetierException
     */
    private void completerInfoSeance(final ConsulterSeanceDTO consulterSeanceDTO, final SeanceDTO seanceDTO, 
            final String exercice, final Integer idSeance, final Boolean archive) throws MetierException  {
        consulterSeanceDTO.setDate(seanceDTO.getDate());
        consulterSeanceDTO.setDescription(seanceDTO.getDescription());
        consulterSeanceDTO.setIntitule(seanceDTO.getIntitule());
        consulterSeanceDTO.setHeureDebut(seanceDTO.getHeureDebut());
        consulterSeanceDTO.setMinuteDebut(seanceDTO.getMinuteDebut());
        consulterSeanceDTO.setHeureFin(seanceDTO.getHeureFin());
        consulterSeanceDTO.setMinuteFin(seanceDTO.getMinuteFin());
        consulterSeanceDTO.setAnnotations(seanceDTO.getAnnotations());

        //La séquence associée à la séance
        final SequenceAffichageQO sequenceAffichageQO = new SequenceAffichageQO();
        sequenceAffichageQO.setId(seanceDTO.getSequence().getId());
        sequenceAffichageQO.setExercice(exercice);
        sequenceAffichageQO.setArchive(archive);
        final SequenceDTO sequenceDTO =
            sequenceHibernateBusinessService.findSequenceAffichage(sequenceAffichageQO);
        consulterSeanceDTO.setSequenceDTO(sequenceDTO);
        
        //La liste des devoirs de la séance
        final ResultatDTO<List<DevoirDTO>> resultatDevoir =
            seanceHibernateBusinessService.trouverDevoir(idSeance, archive, false, exercice);
        consulterSeanceDTO.setListeDevoirDTO(resultatDevoir.getValeurDTO());

        // Complete les pieces jointes de chaque devoir et le id classe/groupe
        for (final DevoirDTO devoirDTO : consulterSeanceDTO.getListeDevoirDTO()) {
            final List<FileUploadDTO> listePieceJointeDTO =
                seanceHibernateBusinessService.trouverPieceJointeDevoir(devoirDTO.getId(),
                                                                     archive, false, exercice);
            devoirDTO.setFiles((ArrayList<FileUploadDTO>) listePieceJointeDTO);
            if (TypeGroupe.CLASSE.equals(sequenceDTO.getTypeGroupe())) {
                devoirDTO.setIdClasse(sequenceDTO.getIdClasseGroupe());
            } else {
                devoirDTO.setIdGroupe(sequenceDTO.getIdClasseGroupe());
            }
            
        }

        //La liste des pièces jointes de la séance
        final ResultatDTO<List<FileUploadDTO>> resultatPieceJointe =
            seanceHibernateBusinessService.trouverPieceJointe(idSeance, archive, false, exercice);
        consulterSeanceDTO.setListePieceJointeDTO(resultatPieceJointe.getValeurDTO());
        
    }
    
    /**
     * Complete les infos de la seance : charge les devoirs et pieces jointes.
     * @param seanceDTO seanceDTO
     * @throws MetierException MetierException
     */
    public void completerInfoSeance(final SeanceDTO seanceDTO, boolean isVisaArchive) throws MetierException  {
        completerInfoSeanceArchive(seanceDTO, isVisaArchive, false, null);
    }
     
    /**
     * Complete les infos de la seance : charge les devoirs et pieces jointes.
     * @param seanceDTO seanceDTO
     * @param isVisaArchive visa de type memo
     * @param archive mode archive ou pas 
     * @param exercice exercice en mode archive
     * @throws MetierException MetierException
     */
    private void completerInfoSeanceArchive(final SeanceDTO seanceDTO, 
            boolean isVisaArchive, Boolean archive, String exercice) throws MetierException  {
        
        if (seanceDTO.getSequence().getId() != null) {
        //La séquence associée à la séance
            final SequenceAffichageQO sequenceAffichageQO = new SequenceAffichageQO();
            sequenceAffichageQO.setId(seanceDTO.getSequence().getId());
            sequenceAffichageQO.setArchive(archive);
            sequenceAffichageQO.setExercice(exercice);
            
            final SequenceDTO sequenceDTO =
                sequenceHibernateBusinessService.findSequenceAffichage(sequenceAffichageQO);
            seanceDTO.setSequenceDTO(sequenceDTO);        
        }
        
        //La liste des devoirs de la séance
        final ResultatDTO<List<DevoirDTO>> resultatDevoir =
            seanceHibernateBusinessService.trouverDevoir(seanceDTO.getId(), archive, isVisaArchive, exercice);
        seanceDTO.setDevoirs(resultatDevoir.getValeurDTO());
        seanceDTO.setDescriptionHTML(ImagesServlet.genererLatexImage(seanceDTO.getDescription()));
        seanceDTO.setAnnotationsHTML(ImagesServlet.genererLatexImage(seanceDTO.getAnnotations()));

        for (final DevoirDTO devoirDTO : seanceDTO.getDevoirs()) {
            final List<FileUploadDTO> listePieceJointeDTO =
                seanceHibernateBusinessService.trouverPieceJointeDevoir(devoirDTO.getId(), archive, isVisaArchive, exercice);
            
            for (final FileUploadDTO fileUploadDTO : listePieceJointeDTO) {                    
                if (FileUploadUtils.checkExistencePieceJointe(fileUploadDTO)) {
                    fileUploadDTO.setActiverLien(true);
                }
            }
            
            devoirDTO.setFiles((ArrayList<FileUploadDTO>) listePieceJointeDTO);
            devoirDTO.setDescriptionHTML(ImagesServlet.genererLatexImage(devoirDTO.getDescription()));
            
            devoirDTO.setIdClasse(seanceDTO.getIdClasse());
            devoirDTO.setIdGroupe(seanceDTO.getIdGroupe());
        }

        //La liste des pièces jointes de la séance
        final ResultatDTO<List<FileUploadDTO>> resultatPieceJointe =
            seanceHibernateBusinessService.trouverPieceJointe(seanceDTO.getId(), archive, isVisaArchive, exercice);
        seanceDTO.setFiles(resultatPieceJointe.getValeurDTO());
        
        for (final FileUploadDTO fileUploadDTO : seanceDTO.getFiles()) {               
            if (FileUploadUtils.checkExistencePieceJointe(fileUploadDTO)) {
                fileUploadDTO.setActiverLien(true);
            } else {
                fileUploadDTO.setActiverLien(false);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public SeanceDTO rechercherSeance(
            ConsulterSeanceQO consulterSeanceQO) throws MetierException {
        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        
        try {
            final SeanceDTO seanceDTO = seanceHibernateBusinessService.findSeanceById(consulterSeanceQO);
            if (seanceDTO == null) {
                conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_12
                        .name(), Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                        "Cette séance n'existe pas.");
            }
            
            // Complete les informations de la seance
            completerInfoSeanceArchive(seanceDTO, false, consulterSeanceQO.getArchive(), consulterSeanceQO.getExercice());
            
            mettreDroitsAccess(consulterSeanceQO.getIdEnseignantConnecte(), seanceDTO);
            return seanceDTO;

        } catch (final MetierException e) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_09.name(),
                    Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                    "Erreur durant la phase de consultation d'une séance.");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public ConsulterSeanceDTO consulterSeance(final ConsulterSeanceQO consulterSeanceQO)
                                       throws MetierException {
        final ConsulterSeanceDTO consulterSeanceDTO = new ConsulterSeanceDTO();
        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        final Integer idSeance = consulterSeanceQO.getId();
        final Boolean archive = consulterSeanceQO.getArchive();
        final String exercice = consulterSeanceQO.getExercice();

        try {
            final SeanceDTO seanceDTO =
                seanceHibernateBusinessService.trouver(idSeance, archive, exercice);
            if (seanceDTO == null) {
                conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_12.name(),
                                                 Nature.BLOQUANT));
                throw new MetierException(conteneurMessage, "Cette séance n'existe pas.");
            }
            // Complete les informations de la seance
            completerInfoSeance(consulterSeanceDTO, seanceDTO,exercice, idSeance, archive);

        } catch (final MetierException e) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_09.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "Erreur durant la phase de consultation d'une séance.");
        }

        return consulterSeanceDTO;
    }

    /**
     * {@inheritDoc}
     */
    public ConsulterSeanceDTO chercherSeancePrecedente(final RechercheSeanceQO chercherSeanceQO)
                                       throws MetierException {
        final ConsulterSeanceDTO consulterSeanceDTO = new ConsulterSeanceDTO();
        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        try {
            final SeanceDTO seanceDTO =
                seanceHibernateBusinessService.findSeancePrecedente(chercherSeanceQO);
            if (seanceDTO != null) {
                completerInfoSeance(consulterSeanceDTO, seanceDTO, null, seanceDTO.getId(), false);
            } 
            
        } catch (final MetierException e) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_09.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "Erreur durant la phase de consultation d'une séance.");
        }

        return consulterSeanceDTO;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<SeanceDTO> chercherListeSeancePrecedente(final RechercheSeanceQO chercherSeanceQO,  final Integer nbrSeance)
                                       throws MetierException {
        final List<SeanceDTO> listeSeanceDTO = seanceHibernateBusinessService.findListeSeancePrecedente(chercherSeanceQO,nbrSeance);
        for (final SeanceDTO seance : listeSeanceDTO) {
            completerInfoSeance(seance, false);
            
            mettreDroitsAccess(chercherSeanceQO.getIdEnseignantConnecte(), seance);
            
        }
        return listeSeanceDTO;
    }
    
    /**
     * @param listeDevoir .
     * @param idSeance .
     * @param saveSeanceQO .
     * @throws MetierException ex
     */
    private void saveSeanceDevoir(List<DevoirDTO> listeDevoir, Integer idSeance) throws MetierException {
        for (final DevoirDTO devoirDTO : listeDevoir) {
            
            // Verifie que le devoir n'est pas vide 
            if (devoirDTO.getDateRemise() == null && 
                StringUtils.trimToNull(devoirDTO.getIntitule()) == null && 
                StringUtils.trimToNull(devoirDTO.getDescription()) == null &&
                org.apache.commons.collections.CollectionUtils.isEmpty(devoirDTO.getFiles())) {  
                continue;
            }
            devoirDTO.setIdSeance(idSeance);
            final Integer idDevoir = devoirHibernateBusinessService.saveDevoir(devoirDTO);
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(devoirDTO.getFiles())) {
                for (final FileUploadDTO fileUploadDTO : devoirDTO.getFiles()) {
                   fileUploadDTO.setIdDevoir(idDevoir);
                   pieceJointeHibernateBusinessService.savePieceJointeDevoir(fileUploadDTO);
                   
                }
            }
        }
    }
    
    /**
     * @param listePieceJointe .
     * @param idSeance .
     * @param saveSeanceQO .
     * @throws MetierException ex
     */
    private void saveSeancePiecesJointes(List<FileUploadDTO> listePieceJointe, Integer idSeance) throws MetierException {
        for (final FileUploadDTO fileUploadDTO : listePieceJointe) {
            
            
            fileUploadDTO.setIdSeance(idSeance);
            pieceJointeHibernateBusinessService.savePieceJointeSeance(fileUploadDTO);
        }  
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveSeance(SeanceDTO saveSeanceQO, String mode)
                                    throws MetierException {
        Assert.isNotNull("saveSeanceQO", saveSeanceQO);

        //Ne traite pas les devoirs vides
        saveSeanceQO.setListeDevoirDTO(retirerDevoirVide(saveSeanceQO.getListeDevoirDTO()));
                
        //Verifier que la séquenceDTO est renseigné
        saveSeanceQO.setSequenceDTO(
                    sequenceHibernateBusinessService.findSequenceDTO(
                            saveSeanceQO.getSequenceDTO()));
        
        
        final ResultatDTO<Integer> resultatDTO = new ResultatDTO<Integer>();

        checkRG(saveSeanceQO);
        

        Date dateMaj = new Date();
        saveSeanceQO.setDateMaj(dateMaj);

        log.debug("Save séance");
        final Integer idSeance = seanceHibernateBusinessService.saveSeance(saveSeanceQO);

        log.debug("updateVisaDateMaj");
        visaHibernateBusinessService.updateVisaDateMaj(idSeance,  dateMaj);
        
        //Supprime avant pour que les devoir / pj actuellement rattachés sont exactement ce qui est sauvegardés.
        deleteSeanceDevoirEtPJ(idSeance);
        
        final List<DevoirDTO> listeDevoir = saveSeanceQO.getListeDevoirDTO();
        if (!CollectionUtils.isEmpty(listeDevoir)) {
            saveSeanceDevoir(listeDevoir, idSeance);
        }

        final List<FileUploadDTO> listePieceJointe =
            saveSeanceQO.getListeFichierJointDTO();
        if ((listePieceJointe != null) && (listePieceJointe.size() > 0)) {
            saveSeancePiecesJointes(listePieceJointe, idSeance);
        }

        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        if (AbstractForm.MODE_DUPLICATE.equals(mode)) {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                                             Nature.INFORMATIF, "La séance", "dupliquée"));
        } else {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                                             Nature.INFORMATIF, "La séance", "ajoutée"));
        }
        resultatDTO.setConteneurMessage(conteneurMessage);
        resultatDTO.setValeurDTO(idSeance);

        return resultatDTO;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveListeSeance(List<SaveSeanceQO> listeSeanceQO)
                       throws MetierException {
        Integer nbrSaved = 0;
        ResultatDTO<Integer> result;
        for (final SaveSeanceQO saveSeanceQO : listeSeanceQO) {
            result = this.saveSeance(saveSeanceQO, saveSeanceQO.getMode());
            nbrSaved++;
        }
        result = new ResultatDTO<Integer>();
        result.setValeurDTO(nbrSaved);
        
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        if (nbrSaved>0) {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_01.name(),
                    Nature.INFORMATIF, nbrSaved.toString() + " séance(s)", "ajoutée(s)"));
        } else {
            conteneurMessage.add(new Message(TypeReglesCycle.CYCLE_04.name(),
                    Nature.AVERTISSANT));
        }
        result.setConteneurMessage(conteneurMessage);
        return result;
    }
    
    /**
     * Controle des RG d'ajout de séance.
     *
     * @param saveSeanceQO saveSeanceQO.
     *
     * @throws MetierException Exception
     */
    private void checkRG(SeanceDTO saveSeanceQO)
                  throws MetierException {
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        final String codeSequence = saveSeanceQO.getSequenceDTO().getCode();
        //Pas du code ni id
        if (StringUtils.isEmpty(codeSequence) && null == saveSeanceQO.getSequenceDTO().getId()) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_00.name(),
                                             Nature.BLOQUANT, DateUtils.format(saveSeanceQO.getDate())));
            throw new MetierException(conteneurMessage,
                                      "La sélection de la séquence est obligatoire.");
        }

        //Code mais pas id
        if (!StringUtils.isEmpty(codeSequence) && null == saveSeanceQO.getSequenceDTO().getId()) {
            //Mieux de passer l'id mais au cas où il n'y a que le code
            final Integer idSequence = this.existSequence(codeSequence);
            saveSeanceQO.getSequenceDTO().setId(idSequence);
        }

        //On recherche l'enseignement associé à la séquence
        final Integer idEnseignement =
            sequenceHibernateBusinessService.findEnseignementSequence(saveSeanceQO.getSequenceDTO().getId());
        saveSeanceQO.getSequenceDTO().setIdEnseignement(idEnseignement);

        final String intitule = saveSeanceQO.getIntitule();
        if (StringUtils.isEmpty(intitule)) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_02.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "L'intitulé de la séance est obligatoire.");
        }

        final Date date = saveSeanceQO.getDate();
        if (date == null) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_03.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "La date de la séance est obligatoire.");
        }

        final SequenceBean sequenceBean =
            this.sequenceHibernateBusinessService.find(saveSeanceQO.getSequenceDTO().getId());
        final Date dateDebutSequence = sequenceBean.getDateDebut();
        if (!this.checkSEANCE03(date, dateDebutSequence)) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_04.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "La date de la séance doit être postèrieure ou égale à la date de début de la séquence.");
        }

        final Date dateFinSequence = sequenceBean.getDateFin();
        if (!this.checkSEANCE04(date, dateFinSequence)) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_05.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "La date de la séance doit être antérieur ou égale à la date de fin de la séquence.");
        }

        final List<DevoirDTO> listeDevoirDTO = saveSeanceQO.getListeDevoirDTO();
        if ((listeDevoirDTO != null) && !this.checkSEANCE05(date, listeDevoirDTO)) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_06.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "La date de la séance doit être antérieur ou égale à toute date de remise de devoir.");
        }

        final AnneeScolaireDTO anneeScolaire = 
                anneeScolaireHibernateBusinessService.findAnneeScolaire(saveSeanceQO.getSequenceDTO().getGroupesClassesDTO()).getValeurDTO();
        final Date dateFinAnneeScolaire =
                anneeScolaire.getDateSortie();
        if ((dateFinAnneeScolaire != null) && (listeDevoirDTO != null)) {
            if (!this.checkSEANCE17(dateFinAnneeScolaire, listeDevoirDTO)) {
                conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_17.name(),
                                                 Nature.BLOQUANT, dateFinAnneeScolaire));
                throw new MetierException(conteneurMessage,
                                          "Les dates de remise des devoir doivent être comprises dans l'année scolaire.");
            }
        }

        final Integer heureDebut = saveSeanceQO.getHeureDebut();
        if (heureDebut == null) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_07.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "L'heure de début de la séance est obligatoire.");
        }

        final Integer minuteDebut = saveSeanceQO.getMinuteDebut();
        if (minuteDebut == null) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_14.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "Les minutes de l'heure de début de la séance sont obligatoires.");
        }

        final Integer heureFin = saveSeanceQO.getHeureFin();
        if (heureFin == null) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_08.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "L'heure de fin de la séance est obligatoire.");
        }

        final Integer minuteFin = saveSeanceQO.getMinuteFin();
        if (minuteFin == null) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_15.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "Les minutes de l'heure de fin de la séance sont obligatoires.");
        }

        if ((heureFin < heureDebut) ||
                (heureDebut.equals(heureFin) && (minuteDebut >= minuteFin))) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_13.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      " L'heure de fin de la séance doit être supérieur à l'heure de début.");
        }

        //On controle l'unicité de la séance
        if (!checkUnicite(saveSeanceQO)) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_11.name(),
                                             Nature.BLOQUANT, DateUtils.format(saveSeanceQO.getDate()), 
                                             saveSeanceQO.getHeureDebut().toString() + ":" + saveSeanceQO.getMinuteDebut().toString(),
                                             saveSeanceQO.getHeureFin().toString() + ":" + saveSeanceQO.getMinuteFin().toString()
                                             ));
            throw new MetierException(conteneurMessage,
                                      "Il existe déjà une séance rattachée à cet enseignement, ce groupe ou cette classe, " +
                                      "cet intitulé et cette date.");
        }
        
        if (saveSeanceQO.getIntitule().length() > 50) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_21.name(),
                    Nature.BLOQUANT));
throw new MetierException(conteneurMessage,
             "L'intitulé est limité à 50 caractères.");
        }
        
        
        //Remplacemant si l'id de la séquence n'est pas le même que celui dans la séance        
        if (!saveSeanceQO.getIdEnseignant().equals(saveSeanceQO.getSequenceDTO().getIdEnseignant())) {
            RechercheRemplacementQO rechercheRemplacementQO = new RechercheRemplacementQO();
            
            //L'id de séquence est l'enseignant absent
            rechercheRemplacementQO.setIdEnseignantAbsent(saveSeanceQO.getSequenceDTO().getIdEnseignant());
            
           // EnseignantBean ensBean = 
                //    enseignantHibernateBusinessService.find(saveSeanceQO.getUidEnseignantRemplacant());
            rechercheRemplacementQO.setIdEnseignantRemplacant(saveSeanceQO.getIdEnseignant());
            
            rechercheRemplacementQO.setIdEtablissement(saveSeanceQO.getSequenceDTO().getIdEtablissement());
            
            rechercheRemplacementQO.setDate(new LocalDate(saveSeanceQO.getDate()));
            
            List<RemplacementDTO> listeRemplacementsDTO = 
                    remplacementFacadeService.findListeRemplacement(rechercheRemplacementQO).getValeurDTO();
            
            if (CollectionUtils.isEmpty(listeRemplacementsDTO)) {
                conteneurMessage.add(new Message(TypeReglesRemplacement.REMPLACEMENT_14.name(),
                        Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                        "Le date de cette séance n'est pas dans une période de remplacement.");
            }
        }
    }
    
    
    private boolean existPeriodDeRemplacement(Integer idEnseignantRemplacant, SeanceDTO seance) {
        RechercheRemplacementQO rechercheRemplacementQO = new RechercheRemplacementQO();
        
        //L'id de séquence est l'enseignant absent
        rechercheRemplacementQO.setIdEnseignantAbsent(seance.getSequenceDTO().getIdEnseignant());
        
       // EnseignantBean ensBean = 
            //    enseignantHibernateBusinessService.find(saveSeanceQO.getUidEnseignantRemplacant());
        rechercheRemplacementQO.setIdEnseignantRemplacant(idEnseignantRemplacant);
        
        rechercheRemplacementQO.setIdEtablissement(seance.getSequenceDTO().getIdEtablissement());
        
        rechercheRemplacementQO.setDate(new LocalDate(seance.getDate()));
        
        List<RemplacementDTO> listeRemplacementsDTO = 
                remplacementFacadeService.findListeRemplacement(rechercheRemplacementQO).getValeurDTO();
        
        if (CollectionUtils.isEmpty(listeRemplacementsDTO)) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * 
     * @param idEnseignantConnecte soit l'id connecté, soit dans le cas de remplaçant, l'id de remplaçant
     * @param seance s
     */
    public void mettreDroitsAccess(Integer idEnseignantConnecte, SeanceDTO seance) {
        
        
        
        Preconditions.checkNotNull(seance.getIdEnseignant());
        Preconditions.checkNotNull(seance.getSequenceDTO().getIdEnseignant());        
        Preconditions.checkNotNull(seance.getSequenceDTO().getIdEtablissement());
        
        log.debug("mettreDroitsAccess id enseignant {} seance id {}", idEnseignantConnecte, seance.getId());
        
        if (idEnseignantConnecte == null) {
            log.debug("Identifiant de l'enseignant pas renseigné, pas d'accès d'écriture sur la séance");
            seance.setAccesEcriture(false);
            return;
        }
        
      //Remplacemant si l'id de la séquence n'est pas le même que celui dans la séance        
        if (!seance.getIdEnseignant().equals(seance.getSequenceDTO().getIdEnseignant())) {
            
            if (idEnseignantConnecte.equals(seance.getSequenceDTO().getIdEnseignant())) {
                log.debug("Id enseignant connecté est celui de l'enseignant absent, pas d'acces");
                seance.setAccesEcriture(false);
                return;
            }
            
            if (!idEnseignantConnecte.equals(seance.getIdEnseignant())) {
                log.debug("id de l'enseignant connecté n'est pas égal au id de la séance (remplaçant), pas d'acces");
                seance.setAccesEcriture(false);
                return;
            }
            
            if (!existPeriodDeRemplacement(seance.getIdEnseignant(), seance)) {
                log.debug("Il n'y a pas de période de remplaçement valide pour l'enseignant remplaçant");
                seance.setAccesEcriture(false);
                return;
            } else {
                log.debug("Il y a une période de remplaçement valide pour l'enseignant remplaçant");
                seance.setAccesEcriture(true);
                return;
            }
        } else {
            log.debug("Séance était créer par l'enseignant de la séquence (il s'agit pas de remplaçement)");
            //id sequence == id seance
            if (seance.getIdEnseignant().equals(idEnseignantConnecte)) {
                log.debug("Id de l'utilisateur connecté == id de la séance, accès écriture");
                seance.setAccesEcriture(true);
            } else {
                
                if (existPeriodDeRemplacement(idEnseignantConnecte, seance)) {
                    log.debug("Il y a une période de remplaçement valide pour la séance saisie en anticipation");
                    seance.setAccesEcriture(true);
                    return;
                }
                log.debug("L'id de l'utilisateur connecté != id de la séance, pas d'accès écriture");
                seance.setAccesEcriture(false);
            }
        }
    }

    /**
     * Regle qui vérifie l'unicité enseignant, enseignement, classe/groupe,
     * intitulé et date.
     *
     * @param saveSeanceQO saveSeanceQO
     *
     * @return true si unique, sinon false
     *
     * @throws MetierException Exception
     */
    public boolean checkUnicite(SeanceDTO saveSeanceQO)
                         throws MetierException {
        final boolean checkResult =
            seanceHibernateBusinessService.checkUnicite(saveSeanceQO);
        return checkResult;
    }

    /**
     * La date de la séance doit être postèrieure ou égale à la date de début de
     * la séquence.
     *
     * @param dateSeance DOCUMENTATION INCOMPLETE!
     * @param dateDebutSequence DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public boolean checkSEANCE03(Date dateSeance, Date dateDebutSequence) {
        boolean checkResult = true;
        if (DateUtils.compare(dateDebutSequence, dateSeance, false) > 0) {
            checkResult = false;
        }
        return checkResult;
    }

    /**
     * La date de la séance doit être antérieur ou égale à la date de fin de la
     * séquence.
     *
     * @param dateSeance DOCUMENTATION INCOMPLETE!
     * @param dateFinSequence DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public boolean checkSEANCE04(Date dateSeance, Date dateFinSequence) {
        boolean checkResult = true;
        if (DateUtils.compare(dateSeance, dateFinSequence, false) > 0) {
            checkResult = false;
        }
        return checkResult;
    }

    /**
     * La date de la séance doit être antérieur ou égale à toute date de remise
     * de devoir.
     *
     * @param dateSeance DOCUMENTATION INCOMPLETE!
     * @param listeDevoirDTO DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public boolean checkSEANCE05(Date dateSeance, List<DevoirDTO> listeDevoirDTO) {
        boolean checkResult = true;
        for (final DevoirDTO devoir : listeDevoirDTO) {
            if (DateUtils.compare(dateSeance, devoir.getDateRemise(), false) > 0) {
                checkResult = false;
            }
        }
        return checkResult;
    }

    /**
     * Les dates de remise des devoir doivent être comprises dans l'année
     * scolaire.
     *
     * @param date DOCUMENTATION INCOMPLETE!
     * @param listeDevoirDTO DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    private boolean checkSEANCE17(Date date, List<DevoirDTO> listeDevoirDTO) {
        boolean checkResult = true;
        for (final DevoirDTO devoir : listeDevoirDTO) {
            if (DateUtils.compare(devoir.getDateRemise(), date, false) > 0) {
                checkResult = false;
            }
        }
        return checkResult;
    }
    
    /**
     * Retrourne la liste des devoirs sans les devoirs vides.
     * @param listeDevoir la liste avec des devoirs vide
     * @return une liste avec que des devoirs remplis.
     */
    public static List<DevoirDTO> retirerDevoirVide(final List<DevoirDTO> listeDevoir) {
        final List<DevoirDTO> listeResultat = new ArrayList<DevoirDTO>();
        if (!CollectionUtils.isEmpty(listeDevoir)) {
            for (final DevoirDTO devoir : listeDevoir) {
                
                // Verifie que le devoir n'est pas vide 
                if (devoir.getDateRemise() == null && 
                    StringUtils.trimToNull(devoir.getIntitule()) == null && 
                    StringUtils.trimToNull(devoir.getDescription()) == null &&
                    org.apache.commons.collections.CollectionUtils.isEmpty(devoir.getFiles())) {
                    continue;
                } 
                listeResultat.add(devoir);
            }
        }
        return listeResultat;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> modifieSeance(SeanceDTO resultatRechercheSeanceDTO)
                                       throws MetierException {
        
        //Ne traite pas les devoirs vides
        resultatRechercheSeanceDTO.setListeDevoirDTO(retirerDevoirVide(resultatRechercheSeanceDTO.getListeDevoirDTO()));
        
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        final ResultatDTO<Integer> resultatDTO = new ResultatDTO<Integer>();

        //Verifier que la séquenceDTO est renseigné
        resultatRechercheSeanceDTO.setSequenceDTO(
                    sequenceHibernateBusinessService.findSequenceDTO(
                            resultatRechercheSeanceDTO.getSequenceDTO()));
        
        //On récupere les infos de la séances que l'on va supprimer.
        //On supprime intégralement la séance
        //resultatRechercheSeanceDTO.setMode(AbstractForm.MODE_MODIF);

        checkRG(resultatRechercheSeanceDTO);
        
        
        //on recrée la séance que l'on a supprimé avec le meme id et les nouvelles infos.
        this.saveSeance(resultatRechercheSeanceDTO, "modif");

        conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                                         Nature.INFORMATIF, "La séance", "modifiée"));
        resultatDTO.setConteneurMessage(conteneurMessage);

        return resultatDTO;
    } 

    private void deleteSeanceDevoirEtPJ(final Integer idSeance)
            throws MetierException {
       
        //Destruction des relations 
        pieceJointeHibernateBusinessService.deletePieceJointeSeance(idSeance);

        final ResultatDTO<List<DevoirDTO>> resultat =
            seanceHibernateBusinessService.trouverDevoir(idSeance, null, false, null);
        final List<DevoirDTO> listeDevoirDTO = resultat.getValeurDTO();

        for (final DevoirDTO devoirDTO : listeDevoirDTO) {
            //destruction des relations
             pieceJointeHibernateBusinessService.deletePieceJointeDevoir(devoirDTO.getId());            
        }

        if (listeDevoirDTO != null) {
            for (final DevoirDTO devoirDTO : listeDevoirDTO) {
                devoirHibernateBusinessService.delete(devoirDTO.getId());
            }
        }
    }
            
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> deleteSeance(SeanceDTO resultatRechercheSeanceDTO)
                                      throws MetierException {
        
        log.debug("deleteSeance {}", resultatRechercheSeanceDTO);
        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        final ResultatDTO<Integer> resultatDTO = new ResultatDTO<Integer>();

        deleteSeanceDevoirEtPJ(resultatRechercheSeanceDTO.getId());

        conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                                         Nature.INFORMATIF, "La séance", "supprimée"));
        resultatDTO.setConteneurMessage(conteneurMessage);
        resultatDTO.setValeurDTO(resultatRechercheSeanceDTO.getId());

        log.debug("deleteSeance supprimerArchiveVisa {}", resultatRechercheSeanceDTO.getId());
        
        visaHibernateBusinessService.supprimerArchiveVisa(resultatRechercheSeanceDTO);
        
        log.debug("deleteSeance supprimerVisaSeance {}", resultatRechercheSeanceDTO.getId());
        
        visaHibernateBusinessService.supprimerVisaSeance(resultatRechercheSeanceDTO);
        
        log.debug("deleteSeance updateVisaDateMaj {}", resultatRechercheSeanceDTO.getId());
        
        visaHibernateBusinessService.updateVisaDateMaj(resultatRechercheSeanceDTO.getId(), new Date());
        seanceHibernateBusinessService.deleteSeance(resultatRechercheSeanceDTO);
        
        return resultatDTO;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<DevoirDTO>> findDevoir(Integer idSeance) {
        return seanceHibernateBusinessService.trouverDevoir(idSeance, null, false, null);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<FileUploadDTO>> findPieceJointe(Integer idSeance) {
        return seanceHibernateBusinessService.trouverPieceJointe(idSeance, null, false, null);
    }

    /**
     * Vérifie qu'une séquence existe (par son code) et retourne son id.
     *
     * @param codeSequence le code de la séquence
     *
     * @return L'id de la séquence
     *
     * @exception MetierException exception
     */
    public Integer existSequence(String codeSequence)
                          throws MetierException {
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        if (!StringUtils.isEmpty(codeSequence)) {
            final SequenceBean sequenceBean =
                sequenceHibernateBusinessService.findByCode(codeSequence);
            if (sequenceBean == null) {
                conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_14.name(),
                                                 Nature.BLOQUANT));
                throw new MetierException(conteneurMessage, "La séquence n'existe pas.");
            }
            return sequenceBean.getId();
        }
        return null;
    }

    /**
     * Vérifie qu'un enseignement existe (par son code) et retourne son id.
     *
     * @param codeEnseignement le code de l'enseignement
     *
     * @return L'id de l'enseignement
     *
     * @exception MetierException exception
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
     * {@inheritDoc}
     */
    public boolean verifieAppartenancePieceJointe(FileUploadDTO fileUploadDTO)
                                           throws MetierException {
        return pieceJointeHibernateBusinessService.verifieAppartenancePieceJointe(fileUploadDTO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<ResultatRechercheSeanceDTO>> listeSeanceAffichage(RechercheSeanceQO rechercheSeanceQO)
        throws MetierException {
        Assert.isNotNull("rechercheSeanceQO", rechercheSeanceQO);

        final Integer idEnseignant = rechercheSeanceQO.getIdEnseignant();
        final Integer idEleve = rechercheSeanceQO.getIdEleve();
        final Integer idInspecteur = rechercheSeanceQO.getIdInspecteur();

        if (idInspecteur != null) {
            return listeSeanceAffichageInspecteur(rechercheSeanceQO);
        } else if (idEleve != null) {
            return listeSeanceAffichageEleve(rechercheSeanceQO);
        } else if (idEnseignant != null){
          return listeSeanceAffichageEnseignant(rechercheSeanceQO);  
        } else {
            return listeSeanceAffichageDirection(rechercheSeanceQO);
        }
    }

    /**
     * Retourne la liste des séances pour un inspecteur.
     *
     * @param rechercheSeanceQO rechercheSeanceQO
     *
     * @return La liste des séances
     *
     * @throws MetierException Exception
     */
    private ResultatDTO<List<ResultatRechercheSeanceDTO>> listeSeanceAffichageInspecteur(
            RechercheSeanceQO rechercheSeanceQO) throws MetierException {
        
        Assert.isNotNull("rechercheSeanceQO", rechercheSeanceQO);

        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        final TypeGroupe typeClasseGroupe = rechercheSeanceQO.getTypeGroupeSelectionne();
        final String codeClasseGroupe =
            rechercheSeanceQO.getGroupeClasseSelectionne().getCode();

        final Integer idClasseGroupe =
                sequenceFacadeService.findClasseGroupeId(rechercheSeanceQO.getGroupeClasseSelectionne(),
                                   rechercheSeanceQO.getArchive(),
                                   rechercheSeanceQO.getExerciceAnneeScolaire());
        if (typeClasseGroupe != null) {
            if (TypeGroupe.CLASSE  == (typeClasseGroupe)) {
                //CLASSE
                if (!StringUtils.isEmpty(codeClasseGroupe)) {
                    final boolean droitClasse =
                        classeHibernateBusinessService.checkDroitClasseInspecteur(rechercheSeanceQO.getIdInspecteur(),
                                                                        idClasseGroupe,
                                                                        rechercheSeanceQO.getArchive(),
                                                                        rechercheSeanceQO.getExerciceAnneeScolaire(),
                                                                        rechercheSeanceQO.getIdEtablissement());
                    if (!droitClasse) {
                        conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_01.name(),
                                                         Nature.BLOQUANT));
                        throw new MetierException(conteneurMessage,
                                                  "La classe existe mais vous n'avez pas les droits dessus.");
                    }
                } else {
                    conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_02.name(),
                                                     Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                                              "Vous devez sélectionner ou saisir une classe.");
                }
            } else if (TypeGroupe.GROUPE  == (typeClasseGroupe)) {
                //GROUPE
                if (!StringUtils.isEmpty(codeClasseGroupe)) {
                    final boolean droitGroupe =
                        groupeHibernateBusinessService.checkDroitGroupeInspecteur(rechercheSeanceQO.getIdInspecteur(),
                                                                        idClasseGroupe,
                                                                        rechercheSeanceQO.getArchive(),
                                                                        rechercheSeanceQO.getExerciceAnneeScolaire(),
                                                                        rechercheSeanceQO.getIdEtablissement());
                    if (!droitGroupe) {
                        conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_01.name(),
                                                         Nature.BLOQUANT));
                        throw new MetierException(conteneurMessage,
                                                  "Le groupe existe mais vous n'avez pas les droits dessus.");
                    }
                } else {
                    conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_02.name(),
                                                     Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                                              "Vous devez sélectionner ou saisir un groupe.");
                }
            }
        }
        return seanceHibernateBusinessService.listeSeanceAffichage(rechercheSeanceQO);
    }

    /**
     * Retourne la liste des séances d'un eleve.
     *
     * @param rechercheSeanceQO rechercheSeanceQO
     *
     * @return La liste des séances
     *
     * @throws MetierException Exception
     */
    private ResultatDTO<List<ResultatRechercheSeanceDTO>> listeSeanceAffichageEleve(RechercheSeanceQO rechercheSeanceQO)
        throws MetierException {
        Assert.isNotNull("rechercheSeanceQO", rechercheSeanceQO);

        final Integer idEleve = rechercheSeanceQO.getIdEleve();
        final List<GroupeBean> listeGroupeBean =
            groupeHibernateBusinessService.findGroupesEleve(idEleve);
        final List<ClasseBean> listeClasseBean =
            classeHibernateBusinessService.findClassesEleve(idEleve);

        rechercheSeanceQO.setListeGroupeBean(listeGroupeBean);
        rechercheSeanceQO.setListeClasseBean(listeClasseBean);

        return seanceHibernateBusinessService.listeSeanceAffichage(rechercheSeanceQO);
    }

    /**
     * Retourne la liste des séances d'un enseignant.
     *
     * @param rechercheSeanceQO rechercheSeanceQO
     *
     * @return La liste des séances
     *
     * @throws MetierException Exception
     */
    private ResultatDTO<List<ResultatRechercheSeanceDTO>> listeSeanceAffichageEnseignant(RechercheSeanceQO rechercheSeanceQO)
        throws MetierException {
        Assert.isNotNull("rechercheSeanceQO", rechercheSeanceQO);

        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        final TypeGroupe typeClasseGroupe = rechercheSeanceQO.getTypeGroupeSelectionne();
       
        ResultatDTO<List<ResultatRechercheSeanceDTO>> resultat = null;
        
        if (typeClasseGroupe == null) {
            resultat = seanceHibernateBusinessService.listeSeanceAffichage(rechercheSeanceQO);
        } else {
        
            final String codeClasseGroupe =
                    rechercheSeanceQO.getGroupeClasseSelectionne().getCode();
    
            final Integer idClasseGroupe =
                sequenceFacadeService.findClasseGroupeId(rechercheSeanceQO.getGroupeClasseSelectionne(),
                                       rechercheSeanceQO.getArchive(),
                                       rechercheSeanceQO.getExerciceAnneeScolaire());
            rechercheSeanceQO.getGroupeClasseSelectionne().setId(idClasseGroupe);
            
            if (TypeGroupe.CLASSE == typeClasseGroupe) {
                // CLASSE
                if (!StringUtils.isEmpty(codeClasseGroupe)) {
                    final boolean droitClasse = classeHibernateBusinessService
                            .checkDroitClasse(rechercheSeanceQO.getIdEnseignant(),
                                    idClasseGroupe, rechercheSeanceQO.getArchive(),
                                    rechercheSeanceQO.getExerciceAnneeScolaire());
                    if (!droitClasse) {
                        conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_01
                                .name(), Nature.BLOQUANT));
                        throw new MetierException(conteneurMessage,
                                "La classe existe mais vous n'avez pas les droits dessus.");
                    }
                } else {
                    conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_02
                            .name(), Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                            "Vous devez sélectionner ou saisir une classe.");
                }
            } else if (TypeGroupe.GROUPE == typeClasseGroupe) {
                // GROUPE
                if (!StringUtils.isEmpty(codeClasseGroupe)) {
                    final boolean droitGroupe = groupeHibernateBusinessService
                            .checkDroitGroupe(rechercheSeanceQO.getIdEnseignant(),
                                    idClasseGroupe, rechercheSeanceQO.getArchive(),
                                    rechercheSeanceQO.getExerciceAnneeScolaire());
                    if (!droitGroupe) {
                        conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_01
                                .name(), Nature.BLOQUANT));
                        throw new MetierException(conteneurMessage,
                                "Le groupe existe mais vous n'avez pas les droits dessus.");
                    }
                } else {
                    conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_02
                            .name(), Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                            "Vous devez sélectionner ou saisir un groupe.");
                }
            }
            
            resultat = seanceHibernateBusinessService.listeSeanceAffichage(rechercheSeanceQO);
        }
        
        for(SeanceDTO seance : resultat.getValeurDTO()) {
            mettreDroitsAccess(rechercheSeanceQO.getIdEnseignantConnecte(), seance);
        }
        
        return resultat;
    }

    /**
     * Retourne la liste des séances pour la direction.
     *
     * @param rechercheSeanceQO rechercheSeanceQO
     *
     * @return La liste des séances
     *
     * @throws MetierException Exception
     */
    private ResultatDTO<List<ResultatRechercheSeanceDTO>> listeSeanceAffichageDirection(RechercheSeanceQO rechercheSeanceQO)
        throws MetierException {
        Assert.isNotNull("rechercheSeanceQO", rechercheSeanceQO);
        Preconditions.checkNotNull(rechercheSeanceQO.getIdEtablissement());

        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        final TypeGroupe typeClasseGroupe = rechercheSeanceQO.getTypeGroupeSelectionne();
        final String codeClasseGroupe =
            rechercheSeanceQO.getGroupeClasseSelectionne().getCode();

        final Boolean vraiOuFauxArchive = rechercheSeanceQO.getArchive();
        final String exercice = rechercheSeanceQO.getExerciceAnneeScolaire();
        final Integer idClasseGroupe =
            sequenceFacadeService.findClasseGroupeId(rechercheSeanceQO.getGroupeClasseSelectionne(), vraiOuFauxArchive,
                                   exercice);
        if (typeClasseGroupe != null) {
            if (TypeGroupe.CLASSE  == (typeClasseGroupe)) {
                //CLASSE
                if (StringUtils.isEmpty(codeClasseGroupe)) {
                    conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_02.name(),
                                                     Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                                              "Vous devez sélectionner ou saisir une classe.");
                }
                final boolean droitClasse =
                    classeHibernateBusinessService.checkDroitClasseDirection(rechercheSeanceQO.getIdEtablissement(),
                                                                             idClasseGroupe,
                                                                             vraiOuFauxArchive,
                                                                             exercice);
                if (!droitClasse) {
                    conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_01.name(),
                                                     Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                                              "La classe existe mais vous n'avez pas les droits dessus.");
                }
            } else if (TypeGroupe.GROUPE  == (typeClasseGroupe)) {
                //GROUPE
                if (StringUtils.isEmpty(codeClasseGroupe)) {
                    conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_02.name(),
                                                     Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                                              "Vous devez sélectionner ou saisir un groupe.");
                }
                final boolean droitGroupe =
                    groupeHibernateBusinessService.checkDroitGroupeDirection(rechercheSeanceQO.getIdEtablissement(),
                                                                             idClasseGroupe,
                                                                             vraiOuFauxArchive,
                                                                             exercice);
                if (!droitGroupe) {
                    conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_01.name(),
                                                     Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                                              "Le groupe existe mais vous n'avez pas les droits dessus.");
                }
            }
        }
        return seanceHibernateBusinessService.listeSeanceAffichage(rechercheSeanceQO);
    }

    /**
     * {@inheritDoc}
     */
    public Integer findIdEtablissementSuivantSchema(EtablissementSchemaQO etablissementSchemaQO) throws MetierException {
        return schemaHibernateBusinessService.findIdEtablissementSuivantSchema(etablissementSchemaQO);
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
     * {@inheritDoc}
     */
    public Report printSeance(PrintSeanceOuSequenceQO printSeanceQO,
            List<PrintSeanceDTO> listSeances) throws MetierException {
        Assert.isNotNull("printSeanceQO", printSeanceQO);

        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        final Map<String, Object> args = new HashMap<String, Object>();

        // Si il y a des résultats
        if (CollectionUtils.isEmpty(listSeances)) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_16.name(),
                    Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                    "Il n'existe aucun résultat pour votre recherche.");
        }

        // On englobe la printSeanceDTO dans printSuperSeanceDTO pour effectuer
        // le regroupement plus facilement dans ireport.
        for (final PrintSeanceDTO printSeanceDTO : listSeances) {

            ImagesServlet.processPrintSeanceDTO(printSeanceDTO);
            
            //Supprimer les balises de font name car cela peut poser un problème si
            //la police n'est pas presente dans le serveur.
            printSeanceDTO.setDescriptionPDF(org.crlr.utils.StringUtils.stripFontStyles(printSeanceDTO.getDescriptionPDF()));
            
            for(DevoirDTO devoir : printSeanceDTO.getDevoirs()) {
                devoir.setDescription(org.crlr.utils.StringUtils.stripFontStyles(devoir.getDescription()));
            }
            // Séance pour la classe xxx OU Séance pour le groupe xxx
            String libelleCLasseGroupe = "";
            if (!StringUtils.isEmpty(printSeanceDTO.getDesignationClasse())) {
                // C'est pour une classe
                libelleCLasseGroupe += ("pour la classe " + printSeanceDTO
                        .getDesignationClasse());
            } else if (!StringUtils.isEmpty(printSeanceDTO
                    .getDesignationGroupe())) {
                // C'est pour un groupe
                libelleCLasseGroupe += ("pour le groupe " + printSeanceDTO
                        .getDesignationGroupe());
            }
            printSeanceDTO.setLibelleClasseGroupe(libelleCLasseGroupe);

        }

        Collections.sort(listSeances, new Comparator<PrintSeanceDTO>() {
            @Override
            public int compare(PrintSeanceDTO o1, PrintSeanceDTO o2) {
                return DateUtils.compare(o1.getDate(), o2.getDate(), true);
            }
        });

        args.put("dateCourante",
                DateUtils.format(printSeanceQO.getDateCourante()));
        args.put("dateDebut", DateUtils.format(printSeanceQO.getDateDebut()));
        args.put("dateFin", DateUtils.format(printSeanceQO.getDateFin()));
        if (null == printSeanceQO.getEnseignement() || StringUtils.trimToNull(printSeanceQO.getEnseignement().getLibelle()) == null) {
            args.put("hasEnseignement", false);
            args.put("enseignements", "Tous");
        } else {
            args.put("hasEnseignement", true);
            args.put("enseignements", printSeanceQO.getEnseignement().getLibelle());
        }
        
        if (printSeanceQO.getEnseignant() == null || StringUtils.trimToNull(printSeanceQO.getEnseignant().getNomComplet()) == null) {
            args.put("hasEnseignants", false);
            args.put("enseignants", "Tous");
        } else {
            args.put("hasEnseignants", true);
            args.put("enseignants", printSeanceQO.getEnseignant().getNomComplet());
        }
        

        // if (printSeanceQO.getIdEnseignement()

        // On formalise l'entete PDF.
        String entetePdf = " ";
        final List<GroupeDTO> listeGroupeDTO = printSeanceQO
                .getListeGroupeDTO();
        final String intituleGoupeOuClasse = StringUtils
                .trimToEmpty(printSeanceQO.getGroupeClasseSelectionne()
                        .getIntitule());
        if (TypeGroupe.CLASSE == printSeanceQO.getGroupeClasseSelectionne().getTypeGroupe()) {
            entetePdf += (" pour la classe " + intituleGoupeOuClasse);
            final int tailleListeGroupe = listeGroupeDTO.size();
            if (tailleListeGroupe > 0) {
                entetePdf += ((tailleListeGroupe > 1) ? " et les groupes "
                        : " et le groupe ");
                for (final GroupeDTO groupe : listeGroupeDTO) {
                    entetePdf += (groupe.getIntitule() + " ");
                }
            }
        } else if (TypeGroupe.GROUPE == printSeanceQO.getGroupeClasseSelectionne().getTypeGroupe()) {
            entetePdf += (" pour le groupe " + intituleGoupeOuClasse);
        }

        args.put("codeClasseGroupe", entetePdf);
        args.put("designationEtablissement",
                printSeanceQO.getDesignationEtablissement());

        if (CollectionUtils.isEmpty(listSeances)) {
            args.put("vraiOuFauxSautPage", false);
            args.put("datePremiereSeance", null);
        } else {
            args.put("vraiOuFauxSautPage",
                    printSeanceQO.getVraiOuFauxSautDePage());
            args.put("datePremiereSeance", listSeances.get(0).getDate());
        }

        // En fonction du mode d'affichage (simple ou détaillé) on génère le
        // pdf.
        final PdfReportGenerator gen = new PdfReportGenerator();
        if (TypeAffichage.SIMPLE == printSeanceQO.getAffichage()) {
            args.put("afficheDevoirs", false);
            return gen.generate(ClassUtils.getPathToReport()
                    + "seanceDetail.jasper", listSeances, args);
        } else if (TypeAffichage.DETAILLE == printSeanceQO.getAffichage()) {
            args.put("afficheDevoirs", true);
            return gen.generate(ClassUtils.getPathToReport()
                    + "seanceDetail.jasper", listSeances, args);
        }

        return null;
    }
    
    
    
    private ConteneurMessage findListeEditionVerification(PrintSeanceOuSequenceQO rechercheSeancePrintQO) throws MetierException {
        
        Assert.isNotNull("rechercheSeancePrintQO", rechercheSeancePrintQO);

        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        Assert.isNotNull("profil", rechercheSeancePrintQO.getProfil());
        Assert.isNotNull("etablissement", rechercheSeancePrintQO.getIdEtablissement());
        
        if (rechercheSeancePrintQO.getIdUtilisateur() == null) {
            rechercheSeancePrintQO.setIdUtilisateur(0);
        }

        Assert.isNotNull("date", rechercheSeancePrintQO.getDateCourante());
        Assert.isNotNull("anneeScolaire", rechercheSeancePrintQO.getAnneeScolaireDTO());

        final GroupesClassesDTO groupeClasseSelectionnee =
            rechercheSeancePrintQO.getGroupeClasseSelectionne();
        Assert.isNotNull("groupeClasseSelectionnee", groupeClasseSelectionnee);

        //On vérifie qu'une classe ou un groupe a bien été sélectionné ou saisie, que celui-ci existe et qu'on a les droits dessus.
        final Integer idClasseGroupe =
            sequenceFacadeService.findClasseGroupeId(groupeClasseSelectionnee,
                                    false, null);
        
        sequenceFacadeService.verifierClasseGroupeSaisieAvecType(conteneurMessage, groupeClasseSelectionnee);
        
        if (groupeClasseSelectionnee.getTypeGroupe() == TypeGroupe.CLASSE) {
            // CLASSE
            final boolean droitClasse;
            if (Profil.ENSEIGNANT.equals(rechercheSeancePrintQO.getProfil())) {
                droitClasse = classeHibernateBusinessService.checkDroitClasse(
                        rechercheSeancePrintQO.getIdUtilisateur(),
                        idClasseGroupe, false, null);

            } else if (Profil.INSPECTION_ACADEMIQUE
                    .equals(rechercheSeancePrintQO.getProfil())) {
                droitClasse = classeHibernateBusinessService
                        .checkDroitClasseInspecteur(
                                rechercheSeancePrintQO.getIdUtilisateur(),
                                idClasseGroupe, false, null,
                                rechercheSeancePrintQO.getIdEtablissement());
            } else {
                // Directeurs
                droitClasse = classeHibernateBusinessService
                        .checkDroitClasseDirection(
                                rechercheSeancePrintQO.getIdEtablissement(),
                                idClasseGroupe, false, null);
            }
            if (!droitClasse) {
                conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_01
                        .name(), Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                        "La classe existe mais vous n'avez pas les droits dessus.");
            }
            
        } else if (groupeClasseSelectionnee.getTypeGroupe() == TypeGroupe.GROUPE){
            //GROUPE
            final boolean droitGroupe;
            // Si ce n'est pas un enseignant, c'est un directeur d'établissement
            // ou d'académie et donc on ne vérifie pas les droits.
            if (Profil.ENSEIGNANT.equals(rechercheSeancePrintQO.getProfil())) {
                droitGroupe = groupeHibernateBusinessService.checkDroitGroupe(
                        rechercheSeancePrintQO.getIdUtilisateur(),
                        idClasseGroupe, false, null);

            } else if (Profil.INSPECTION_ACADEMIQUE
                    .equals(rechercheSeancePrintQO.getProfil())) {
                droitGroupe = groupeHibernateBusinessService
                        .checkDroitGroupeInspecteur(
                                rechercheSeancePrintQO.getIdUtilisateur(),
                                idClasseGroupe, false, null,
                                rechercheSeancePrintQO.getIdEtablissement());
            } else {
                // Directeurs
                droitGroupe = groupeHibernateBusinessService
                        .checkDroitGroupeDirection(
                                rechercheSeancePrintQO.getIdEtablissement(),
                                idClasseGroupe, false, null);

            }
            if (!droitGroupe) {
                conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_01
                        .name(), Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                        "Le groupe existe mais vous n'avez pas les droits dessus.");
            }
            
        }

        //On controle la saisie de la date de début de l'édition.
        final Date dateDebut = rechercheSeancePrintQO.getDateDebut();
        if (dateDebut == null) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_09.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "La date de début de la séquence est obligatoire.");
        }

        //On controle la saisie de la date de fin de l'édition.
        final Date dateFin = rechercheSeancePrintQO.getDateFin();
        if (dateFin == null) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_10.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "La date de fin de la séquence est obligatoire.");
        }

        //On vérifie que les dates de la séquence sont bien comprises dans l'année scolaire du contexte utilisateur.
        final Integer idAnneeScolaire =
            rechercheSeancePrintQO.getAnneeScolaireDTO().getId();
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

        //On vérifie que l'on a bien les droits sur l'enseignement sélectionné et que c'est un enseignant.
        if ((rechercheSeancePrintQO.getEnseignement() != null && rechercheSeancePrintQO.getEnseignement().getId() != null) &&
                Profil.ENSEIGNANT.equals(rechercheSeancePrintQO.getProfil())) {
            final boolean droitEnseignement =
                enseignementHibernateBusinessService.checkDroitEnseignement(rechercheSeancePrintQO.getIdUtilisateur(),
                                                                            rechercheSeancePrintQO.getEnseignement().getId(),
                                                                            rechercheSeancePrintQO.getIdEtablissement());
            if (!droitEnseignement) {
                conteneurMessage.add(new Message(TypeReglesEnseignement.ENSEIGNEMENT_01.name(),
                                                 Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                                          "L'enseignement existe mais vous n'avez pas les droits dessus.");
            }
        }
        
        return conteneurMessage;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<PrintSequenceDTO>> findListeSequencesEdition(PrintSeanceOuSequenceQO rechercheSeancePrintQO) throws MetierException {
        ConteneurMessage conteneurMessage = findListeEditionVerification(rechercheSeancePrintQO);
        
        //On recherche toutes les séances qui correspondent aux critères pour l'édition.
        final ResultatDTO<List<PrintSequenceDTO>> resultatDTO =
            seanceHibernateBusinessService.findListeSeanceEdition(rechercheSeancePrintQO);
                

        if (CollectionUtils.isEmpty(resultatDTO.getValeurDTO())) {
                    
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_16.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "Il n'existe aucun résultat pour votre recherche.");
        }
        
        for (PrintSequenceDTO sequence : resultatDTO.getValeurDTO()) {
            for(PrintSeanceDTO seance : sequence.getListeSeances()) {
                completerInfoSeance(seance, false);
            }            
        }
        
        return resultatDTO;
    }
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<PrintSeanceDTO>> findListeSeanceEdition(PrintSeanceOuSequenceQO rechercheSeancePrintQO)
        throws MetierException {
        
    	boolean  isArchive  = rechercheSeancePrintQO.isInArchive();
    	if (isArchive) {
    		String exercice = rechercheSeancePrintQO.getAnneeScolaireDTO().getExercice();
    		final String schema = SchemaUtils.getSchemaCourantOuArchive(isArchive, exercice);
    		SchemaInterceptorImpl.setSchema(schema);
    	}
    	
        ConteneurMessage conteneurMessage = findListeEditionVerification(rechercheSeancePrintQO);
        
        //On recherche toutes les séances qui correspondent aux critères pour l'édition.
        final ResultatDTO<List<PrintSequenceDTO>> resultatDTO =
            seanceHibernateBusinessService.findListeSeanceEdition(rechercheSeancePrintQO);
        
        final ResultatDTO<List<PrintSeanceDTO>> result = new ResultatDTO<List<PrintSeanceDTO>>();

        //Déjà trié dans les requêtes HQL
        List<PrintSeanceDTO> listResultatRechercheSeanceDTO = new ArrayList<PrintSeanceDTO>();
        
        for (PrintSequenceDTO seq : resultatDTO.getValeurDTO()) {
            listResultatRechercheSeanceDTO.addAll(seq.getListeSeances());
        }

        if (!CollectionUtils.isEmpty(listResultatRechercheSeanceDTO)) {
                       
            for (PrintSeanceDTO seance : listResultatRechercheSeanceDTO) {
                completerInfoSeance(seance, false);
            }
            result.setValeurDTO(listResultatRechercheSeanceDTO);
        } else {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_16.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "Il n'existe aucun résultat pour votre recherche.");
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<FileUploadDTO>> findPieceJointeDevoir(Integer idDevoir) {
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        final ResultatDTO<List<FileUploadDTO>> res =
            new ResultatDTO<List<FileUploadDTO>>();
        res.setConteneurMessage(conteneurMessage);
        
        res.setValeurDTO(seanceHibernateBusinessService.trouverPieceJointeDevoir(idDevoir,
                                                                                  false,
                                                                                  false,
                                                                                  ""));
        return res;
    }

    /**
     * @param remplacementFacadeService the remplacementFacadeService to set
     */
    public void setRemplacementFacadeService(
            RemplacementFacadeService remplacementFacadeService) {
        this.remplacementFacadeService = remplacementFacadeService;
    }


}
