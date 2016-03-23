/* 
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.base.TypeReglesAcquittement;
import org.crlr.dto.application.cycle.CycleDTO;
import org.crlr.dto.application.cycle.CycleDevoirDTO;
import org.crlr.dto.application.cycle.CyclePlageEmploiDTO;
import org.crlr.dto.application.cycle.CycleSeanceDTO;
import org.crlr.dto.application.cycle.CycleSeanceFinalDTO;
import org.crlr.dto.application.cycle.RechercheCycleEmploiQO;
import org.crlr.dto.application.cycle.RechercheCycleQO;
import org.crlr.dto.application.cycle.RechercheCycleSeanceQO;
import org.crlr.dto.application.cycle.TypeDateRemise;
import org.crlr.dto.application.cycle.TypeReglesCycle;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.business.CycleHibernateBusinessService;
import org.crlr.metier.business.EmploiHibernateBusinessService;
import org.crlr.metier.business.PieceJointeHibernateBusinessService;
import org.crlr.services.ImagesServlet;
import org.crlr.utils.Assert;
import org.crlr.utils.BooleanUtils;
import org.crlr.utils.DateUtils;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.dto.TypeSemaine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
public class CycleFacade implements CycleFacadeService {

    /** servive metier de persistence des cycle. */
    @Autowired
    private CycleHibernateBusinessService cycleHibernateBusinessService;

    /** servive metier des emploi du temps. */
    @Autowired
    private EmploiHibernateBusinessService emploiHibernateBusinessService;
    
    /** servive metier de persistence des PJ. */
    @Autowired
    private PieceJointeHibernateBusinessService pieceJointeHibernateBusinessService;

    /**
     * Mutateur de cycleHibernateBusinessService {@link #cycleHibernateBusinessService}.
     * @param cycleHibernateBusinessService le cycleHibernateBusinessService to set
     */
    public void setCycleHibernateBusinessService(
            CycleHibernateBusinessService cycleHibernateBusinessService) {
        this.cycleHibernateBusinessService = cycleHibernateBusinessService;
    }

    /**
     * Mutateur de emploiHibernateBusinessService {@link #emploiHibernateBusinessService}.
     * @param emploiHibernateBusinessService le emploiHibernateBusinessService to set
     */
    public void setEmploiHibernateBusinessService(
            EmploiHibernateBusinessService emploiHibernateBusinessService) {
        this.emploiHibernateBusinessService = emploiHibernateBusinessService;
    }

    /**
     * Mutateur de pieceJointeHibernateBusinessService {@link #pieceJointeHibernateBusinessService}.
     * @param pieceJointeHibernateBusinessService le pieceJointeHibernateBusinessService to set
     */
    public void setPieceJointeHibernateBusinessService(
            PieceJointeHibernateBusinessService pieceJointeHibernateBusinessService) {
        this.pieceJointeHibernateBusinessService = pieceJointeHibernateBusinessService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveCycle(CycleDTO cycle) throws MetierException {
        Assert.isNotNull("cycleDTO", cycle);

        final ResultatDTO<Integer>  resultatDTO = new ResultatDTO<Integer>(); 
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        
        // Controles des champs obligatoires
        if (StringUtils.isEmpty(cycle.getTitre())) {
            conteneurMessage.add(new Message(TypeReglesCycle.CYCLE_05.name(), Nature.BLOQUANT));
            throw new MetierException(conteneurMessage, "Le titre du cycle est obligatoire.");
        }
        
        // mode ajout ou modif
        final Boolean modeAjout = cycle.getId() == null;
        
        // Persiste l'objet et les lien avec classe / groupe
        if (cycleHibernateBusinessService.saveCycle(cycle)) {
            if (modeAjout) {
                conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                                                 Nature.INFORMATIF, "La séquence pédagogique", "ajoutée"));
            } else {
                conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                                                 Nature.INFORMATIF, "La séquence pédagogique", "modifiée"));
            }
            
            resultatDTO.setConteneurMessage(conteneurMessage);
            resultatDTO.setValeurDTO(cycle.getId());
        } else {
            conteneurMessage.add(new Message(TypeReglesCycle.CYCLE_01.name(), Nature.BLOQUANT));
            
        }
        return resultatDTO;
    }

    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveCycleSeance(CycleSeanceDTO cycleSeanceDTO) throws MetierException {
        Assert.isNotNull("cycleDTO", cycleSeanceDTO);

        // mode ajout ou modif
        final Boolean modeAjout = cycleSeanceDTO.getId() == null;
        
        final ResultatDTO<Integer>  resultatDTO = new ResultatDTO<Integer>(); 
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        
        Integer idCycleSeance;
        
        // Verifie les champs obligatoire
        checkSaveSeance(cycleSeanceDTO);
        
        // Persiste l'objet 
        if ((idCycleSeance = cycleHibernateBusinessService.saveCycleSeance(cycleSeanceDTO)) != null) {

            // Sauvegarde les PJ de la seance
            saveSeancePiecesJointes(cycleSeanceDTO.getListePieceJointe(), idCycleSeance);

            // Liste des devoirs initiaux
            final List<CycleDevoirDTO> listeDevoirBdd = cycleHibernateBusinessService.findListeDevoirSeance(idCycleSeance);
            
            // Sauvegarde les devoirs
            final Set<Integer> listeIdDevoir = new HashSet<Integer>();
            for (final CycleDevoirDTO cycleDevoir : cycleSeanceDTO.getListeCycleDevoir()) {
                if (cycleDevoir.getId() != null || cycleDevoir.getDateRemise()!=null || 
                !StringUtils.isEmpty(cycleDevoir.getDescription()) || 
                !StringUtils.isEmpty(cycleDevoir.getIntitule()) || 
                !CollectionUtils.isEmpty(cycleDevoir.getListePieceJointe())) {
                    Integer idCycleDevoir = saveSeanceDevoir(cycleDevoir, idCycleSeance);
                    listeIdDevoir.add(idCycleDevoir);
                }
            }
            // Supprime les devoirs initiaux qui ne sont pas dans la liste sauvegardee
            for (final CycleDevoirDTO cycleDevoirDTO : listeDevoirBdd) {
                if (!listeIdDevoir.contains(cycleDevoirDTO.getId())) {

                    // Supprime les liens vers les PJ / Devoir
                    cycleHibernateBusinessService.deletePieceJointeDevoir(cycleDevoirDTO.getId());
                    
                    // Supprime le devoir
                    cycleHibernateBusinessService.deleteDevoir(cycleDevoirDTO.getId());
                }
            }
            
            if (modeAjout) {
                conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                                                 Nature.INFORMATIF, "La séance pédagogique", "ajoutée"));
            } else {
                conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                                                 Nature.INFORMATIF, "La séance pédagogique", "modifiée"));
            }
            
            resultatDTO.setConteneurMessage(conteneurMessage);
            resultatDTO.setValeurDTO(idCycleSeance);
            
        } else {
            conteneurMessage.add(new Message(TypeReglesCycle.CYCLE_01.name(), Nature.BLOQUANT));
            
        }
        return resultatDTO;
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Boolean> saveCycleOrdreSeance(CycleDTO cycleDTO) throws MetierException {
        final Integer count = cycleHibernateBusinessService.saveCycleOrdreSeance(cycleDTO);
        ResultatDTO<Boolean> result = new ResultatDTO<Boolean>();
        
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        if (count > 0) {
            conteneurMessage.add(new Message(TypeReglesCycle.CYCLE_08.name(), Nature.INFORMATIF, count));
        } else {
            conteneurMessage.add(new Message(TypeReglesCycle.CYCLE_09.name(), Nature.INFORMATIF));
        }
        result.setConteneurMessage(conteneurMessage);
        result.setValeurDTO(true);
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Boolean> deleteCycleSeance(CycleSeanceDTO cycleSeanceDTO) throws MetierException {
        Assert.isNotNull("cycleSeanceDTO", cycleSeanceDTO);
        Assert.isNotNull("cycleSeanceDTO.id", cycleSeanceDTO.getId());

        final ResultatDTO<Boolean>  resultatDTO = new ResultatDTO<Boolean>(); 
        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        // Supprime les PJ / Seance
        cycleHibernateBusinessService.deletePieceJointeSeance(cycleSeanceDTO.getId());
        
        // Charge les devoirs a supprimer
        completeInfoSeance(cycleSeanceDTO);
        
        // Supprime les devoirs 
        for (final CycleDevoirDTO cycleDevoirDTO : cycleSeanceDTO.getListeCycleDevoir()) {

            // Supprime les liens vers les PJ / Devoir
            cycleHibernateBusinessService.deletePieceJointeDevoir(cycleDevoirDTO.getId());
            
            // Supprime le devoir
            cycleHibernateBusinessService.deleteDevoir(cycleDevoirDTO.getId());
        }
        
        // Suppr l'objet Seance
        cycleHibernateBusinessService.deleteCycleSeance(cycleSeanceDTO.getId());
        
        conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(), Nature.INFORMATIF, "La séance pédagogique", "Supprimée"));
        resultatDTO.setConteneurMessage(conteneurMessage);
        resultatDTO.setValeurDTO(true);
        return resultatDTO;        
    }
    
    /**
     * Suppression d'un cycle complet.
     *
     * @param cycleDTO Le cycle a virer.  
     * @return true/false
     * @throws MetierException Exception
     */
    public ResultatDTO<Boolean> deleteCycle(CycleDTO cycleDTO) throws MetierException {
        Assert.isNotNull("cycleDTO", cycleDTO);
        ResultatDTO<Boolean> result = new ResultatDTO<Boolean>();
        
        // Supprime toutes les seances
        Integer count = 0;
        for (final CycleSeanceDTO cycleSeance : cycleDTO.getListeSeance()) {
            result = deleteCycleSeance(cycleSeance);
            if (BooleanUtils.isTrue(result.getValeurDTO())) {
                count++;
            }
        }
        
        // Supprime le cycle
        cycleHibernateBusinessService.deleteCycle(cycleDTO.getId());
        result.setValeurDTO(true);
        
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        if (count.equals(0)) {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                    Nature.INFORMATIF, "La séquence pédagogique", "Supprimée"));
        } else if (count.equals(1)) {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_01.name(), 
                    Nature.INFORMATIF, "La séquence pédagogique et sa séance ", "Supprimées"));
        } else {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_01.name(), 
                    Nature.INFORMATIF, "La séquence pédagogique et ses " + count.toString() + " séances ", "Supprimées"));
        }
        result.setConteneurMessage(conteneurMessage);
        return result;
    }
    
    /**
     * @param listePieceJointe en mode annule et remplace.
     * @param idCycleSeance .
     * @throws MetierException ex
     */
    private void saveSeancePiecesJointes(List<FileUploadDTO> listePieceJointe, Integer idCycleSeance) throws MetierException {
        
        // Efface les liens Seance / PJ 
        cycleHibernateBusinessService.deletePieceJointeSeance(idCycleSeance);
        
        // Cree les lien Seance / PJ
        for (final FileUploadDTO fileUploadDTO : listePieceJointe) {
            fileUploadDTO.setIdSeance(idCycleSeance);
            pieceJointeHibernateBusinessService.savePieceJointeCycleSeance(fileUploadDTO);
        }  
    }

    /**
     * @param listePieceJointe  en mode annule et remplace.
     * @param idCycleDevoir .
     * @throws MetierException ex
     */
    private void saveDevoirPiecesJointes(List<FileUploadDTO> listePieceJointe, Integer idCycleDevoir) throws MetierException {

        // Efface les liens Devoir / PJ 
        cycleHibernateBusinessService.deletePieceJointeDevoir(idCycleDevoir);
        
        // Cree les lien Devoir / PJ
        for (final FileUploadDTO fileUploadDTO : listePieceJointe) {
            fileUploadDTO.setIdDevoir(idCycleDevoir);
            pieceJointeHibernateBusinessService.savePieceJointeCycleDevoir(fileUploadDTO);
        }  
    }
    
    /**
     * Sauvegarde les CycleDevoir d'une CycleSeance.
     * @param cycleDevoir le devoir a sauvegarder.
     * @param idCycleSeance : id de la seance
     * @throws MetierException exception
     * @return id du devoir
     */
    private Integer saveSeanceDevoir(CycleDevoirDTO cycleDevoir, Integer idCycleSeance) throws MetierException  {
            
        // Verification de controle
        checkSaveDevoir(cycleDevoir);
        
        Integer idCycleDevoir = null;
        
        // Sauvegarde le devoir
        if ((idCycleDevoir = cycleHibernateBusinessService.saveCycleDevoir(cycleDevoir, idCycleSeance)) != null) {

            // Sauvegarde les PJ du devoir
            saveDevoirPiecesJointes(cycleDevoir.getListePieceJointe(), idCycleDevoir);
        }
        return idCycleDevoir;
    }

    /**
     * Verifie que le devoir est bien alimente.
     * @param cycleDevoir le devoir a verifier
     * @throws MetierException une exception
     */
    private void checkSaveDevoir(CycleDevoirDTO cycleDevoir) throws MetierException {
        
        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        if (StringUtils.isEmpty(cycleDevoir.getIntitule())) {
            conteneurMessage.add(new Message(TypeReglesCycle.CYCLE_13.name(), Nature.BLOQUANT)); 
            throw new MetierException(conteneurMessage, "L'intitulé du devoir est obligatoire.");
        }
        if (cycleDevoir.getDateRemise() == null || cycleDevoir.getDateRemise().getId()==null) {
            conteneurMessage.add(new Message(TypeReglesCycle.CYCLE_02.name(), Nature.BLOQUANT)); 
            throw new MetierException(conteneurMessage, "La date de remise est obligatoire.");
        }
    }
    
    /**
     * Verifie que la seance est bien renseignée.
     * @param cycleSeance la seance 
     * @throws MetierException l'exception
     */
    private void checkSaveSeance(CycleSeanceDTO cycleSeance) throws MetierException {
        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        if (StringUtils.isEmpty(cycleSeance.getIntitule())) {
            conteneurMessage.add(new Message(TypeReglesCycle.CYCLE_12.name(), Nature.BLOQUANT)); 
            throw new MetierException(conteneurMessage, "L'intitulé de la séance pédagogique est obligatoire.");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<CycleDTO>> findListeCycle(RechercheCycleQO rechercheCycleQO) {
        List<CycleDTO> liste = cycleHibernateBusinessService.findListeCycle(rechercheCycleQO);
        
        final ResultatDTO<List<CycleDTO>> result = new ResultatDTO<List<CycleDTO>>();
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        result.setValeurDTO(liste);
        if (liste.isEmpty()) {
            conteneurMessage.add(new Message(TypeReglesCycle.CYCLE_06.name(),Nature.INFORMATIF));
        } else {
            conteneurMessage.add(new Message(TypeReglesCycle.CYCLE_07.name(),Nature.INFORMATIF, liste.size()));
        }
        result.setConteneurMessage(conteneurMessage);
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<CycleSeanceDTO> findListeCycleSeance(RechercheCycleSeanceQO rechercheCycleSeanceQO) {
        Assert.isNotNull("rechercheCycleSeanceQO", rechercheCycleSeanceQO);
        Assert.isNotNull("rechercheCycleSeanceQO.cycleDTO", rechercheCycleSeanceQO.getCycleDTO());
        
        final List<CycleSeanceDTO> listeSeance = cycleHibernateBusinessService.findListeCycleSeance(rechercheCycleSeanceQO.getCycleDTO());
        
        // Positionne dans chaque seance le lien avec le cycle
        for(final CycleSeanceDTO cycleSeanceDTO : listeSeance) {
            cycleSeanceDTO.setIdCycle(rechercheCycleSeanceQO.getCycleDTO().getId());
            cycleSeanceDTO.setDescriptionHTML(ImagesServlet.genererLatexImage(cycleSeanceDTO.getDescription()));
            cycleSeanceDTO.setAnnotationsHTML(ImagesServlet.genererLatexImage(cycleSeanceDTO.getAnnotations()));
            
            if (BooleanUtils.isTrue(rechercheCycleSeanceQO.getAvecDetail())) {
                completeInfoSeance(cycleSeanceDTO);
            }
        }
        return listeSeance;
    }
    
    /**
     * {@inheritDoc}
     */
    public void completeInfoSeance(CycleSeanceDTO cycleSeanceDTO) {
        
        // Charge les PJ
        final List<FileUploadDTO> listePieceJointe = cycleHibernateBusinessService.findListePieceJointeSeance(cycleSeanceDTO.getId());
        cycleSeanceDTO.setListePieceJointe(listePieceJointe);
        
        // Charge les devoirs
        final List<CycleDevoirDTO> listeDevoir = cycleHibernateBusinessService.findListeDevoirSeance(cycleSeanceDTO.getId());
        cycleSeanceDTO.setListeCycleDevoir(listeDevoir);
        
        // Boucle sur chaque devoir pour positionner le lien avec la seance
        // Et pour charger les PJ de chaque devoir.
        for (final CycleDevoirDTO cycleDevoirDTO : listeDevoir) {

            // Charge les PJ
            final List<FileUploadDTO> listePieceJointeDevoir = cycleHibernateBusinessService.findListePieceJointeDevoir(cycleDevoirDTO.getId());
            cycleDevoirDTO.setListePieceJointe(listePieceJointeDevoir);
        }
    }
    
    /**
     * Determine le type de semaine correspondant à la date du jour passé.
     * @param jour la date testée.
     * @param alternanceSemaine la map d'alternance des semaines.
     * @return si alternance : "-1" ou "-2" 
     * sinon "" 
     */
    private TypeSemaine calculerTypeSemaine(final Date jour, final Map<Integer, TypeSemaine> alternanceSemaine) {
        final Integer numSemaine = DateUtils.getChamp(jour, Calendar.WEEK_OF_YEAR);
        if (alternanceSemaine.containsKey(numSemaine)) {
            return alternanceSemaine.get(numSemaine);
        } else {
            return TypeSemaine.NEUTRE;
        }
    }
    
    /**
     * Retourne le type de jour correspondant à la date de la seance.
     * @param date date
     * @return un TypeJour
     */
    public static TypeJour getTypeJour(final Date date) {
        if (date == null) {
            return null;
        }
        final GregorianCalendar queryCal = new GregorianCalendar();
        queryCal.setTime(date);
        final TypeJour jour = TypeJour.getTypeJour(queryCal.get(Calendar.DAY_OF_WEEK));
        return jour; 
    }

    /**
     * Extrait de la liste totale les plage correspondante à l'enseignement sur le jour sélectionné.
     * @param listePlage la liste de toutes les plages
     * @param idEnseignement id enseignement
     * @param dateCourante date courante
     * @param alternance map d'alternance
     * @param heureDeb heure de debut (peut etre null)
     * @param minuteDeb heure de debut (peut etre null) 
     * @return un extrait de la liste.
     */
    private List<CyclePlageEmploiDTO> getListePlageJour(
            final List<CyclePlageEmploiDTO> listePlage, 
            final Integer idEnseignement, 
            final Date dateCourante,
            final Map<Integer, TypeSemaine> alternance,
            final Integer heureDeb,
            final Integer minuteDeb
    ) {
        
        // Détermine la semaine en fonction de la date courante
        final TypeSemaine typeSemaine = calculerTypeSemaine(dateCourante, alternance);
        
        // Détermine le numero du jour
        final TypeJour typeJour = getTypeJour(dateCourante);

        // Boucle sur la liste est extrait les plage correspondante au jour/enseignement
        final List<CyclePlageEmploiDTO> listePlageResult = new ArrayList<CyclePlageEmploiDTO>();
        for (final CyclePlageEmploiDTO plage : listePlage) {
            if (plage.getTypeJour().equals(typeJour)) {
                if (plage.getTypeSemaine().getValeur().equals(typeSemaine.getValeur())) {
                    if (plage.getIdEnseignement().equals(idEnseignement)) {
                        if ( DateUtils.isBetween(dateCourante, plage.getDateDebutPeriode(), plage.getDateFinPeriode())) {
                            if (heureDeb == null || minuteDeb == null || 
                            plage.getHeureDebut()*60 + plage.getMinuteDebut() > heureDeb * 60 + minuteDeb ) {
                                listePlageResult.add(plage);
                            }
                        }
                    }
                }
            }
        }
        
        return listePlageResult;

        
    }
    
    /**
     * Recupere la 1er sequence active en dateCourante pour l'enseignement specifie.
     * @param listeSequence liste de SequenceDTO ne concernant que l'enseignant et la classe traitee. 
     * @param dateCourante la date
     * @param idEnseignement id de l'enseignement
     * @return la sequence
     */
    private SequenceDTO getSequenceActive(final List<SequenceDTO> listeSequence, final Date dateCourante, final Integer idEnseignement) {
        for (final SequenceDTO sequenceDTO : listeSequence) {
            if (idEnseignement.equals(sequenceDTO.getIdEnseignement()) 
                    && DateUtils.isBetween(dateCourante, sequenceDTO.getDateDebut(), sequenceDTO.getDateFin())) {
                return sequenceDTO;
            }
        }
        return null;
    }
    
    /**
     * Determine la date de remise de devoir par raport au numero de seance courante 
     * et le type de date de remise de devoir.
     * @param listeSeance liste des seances.
     * @param listePlage liste des plage EDT correspondant à l'enseignant et classe.
     * @param indiceSeance indice de la seance de reference
     * @param typeDateRemise date de remise verbale
     * @param dateSortie date de sortie de l'année scolaire
     * @param alternance liste d'alternance des semaine
     * @param setVacance liste des semaines de vacance
     * @return une date
     */
    private Date calculerDateRemise(
            final List<CycleSeanceFinalDTO> listeSeance, 
            final List<CyclePlageEmploiDTO> listePlage, 
            final Integer indiceSeance, 
            final TypeDateRemise typeDateRemise,
            final Date dateSortie,
            final Map<Integer, TypeSemaine> alternance,
            final Set<Integer> setVacance) {
        Integer nbrSeance = null;
        Integer nbrSemaine = null;
        Integer i;
        final SeanceDTO seanceCourante = listeSeance.get(indiceSeance).getSeanceDTO();
        Integer idEnseignement = seanceCourante.getSequence().getIdEnseignement();
        
        
        if (TypeDateRemise.SEANCE_SUIVANTE_1.equals(typeDateRemise)) {
            nbrSeance = 1;
        } else if (TypeDateRemise.SEANCE_SUIVANTE_2.equals(typeDateRemise)) {
            nbrSeance = 2;
        } else if (TypeDateRemise.SEANCE_SUIVANTE_3.equals(typeDateRemise)) {
            nbrSeance = 3;
        } else if (TypeDateRemise.SEANCE_SUIVANTE_4.equals(typeDateRemise)) {
            nbrSeance = 4;
        } else if (TypeDateRemise.SEMAINE_SUIVANTE_1.equals(typeDateRemise)) {
            nbrSemaine = 1;
        } else if (TypeDateRemise.SEMAINE_SUIVANTE_2.equals(typeDateRemise)) {
            nbrSemaine = 2;
        } else if (TypeDateRemise.SEMAINE_SUIVANTE_3.equals(typeDateRemise)) {
            nbrSemaine = 3;
        } else if (TypeDateRemise.SEMAINE_SUIVANTE_4.equals(typeDateRemise)) {
            nbrSemaine = 4;
        }  
        
        // Compte en nombre de seance
        if (nbrSeance != null) {
            Integer count = 0;
            i = indiceSeance + 1;
            while (!count.equals(nbrSeance) && i<listeSeance.size()) {
                if (listeSeance.get(i).getSeanceDTO().getSequence()!=null && 
                        listeSeance.get(i).getSeanceDTO().getSequence().getIdEnseignement() != null &&  
                        listeSeance.get(i).getSeanceDTO().getSequence().getIdEnseignement().equals(idEnseignement)) {
                    count++;
                    if (count >= nbrSeance) {
                        return listeSeance.get(i).getSeanceDTO().getDate();
                    }
                }
                i++;
            }
        
        // Compte en semaine : meme enseignement, et meme jour 
        } else if (nbrSemaine != null) {
            i = indiceSeance + 1;
            Integer numSemaine = DateUtils.getChamp(seanceCourante.getDate(), Calendar.WEEK_OF_YEAR);
            Integer numJour = DateUtils.getChamp(seanceCourante.getDate(), Calendar.DAY_OF_WEEK);
            Date result = null;
            Integer numSemaineI = null;
            Integer countSemaine = 0;
            while (i<listeSeance.size()) {
                final SeanceDTO aSeance = listeSeance.get(i).getSeanceDTO();
                if (aSeance.getSequence()!=null && aSeance.getSequence().getIdEnseignement()!=null && 
                        aSeance.getSequence().getIdEnseignement().equals(idEnseignement)) {
                    numSemaineI = DateUtils.getChamp(aSeance.getDate(), Calendar.DAY_OF_WEEK);
                    if (!numSemaineI.equals(numSemaine)) {
                        countSemaine++;
                        numSemaine = numSemaineI;
                    }
                    if (countSemaine.equals(nbrSemaine)) {
                        Integer numJourI = DateUtils.getChamp(aSeance.getDate(), Calendar.DAY_OF_WEEK);
                        if (numJourI >= numJour) {
                            result = aSeance.getDate();
                            if (aSeance.getHeureDebut().equals(seanceCourante.getHeureDebut())) {
                                return result;
                            } else if (aSeance.getHeureDebut() > seanceCourante.getHeureDebut()) {
                                return result;
                            }
                        } else {
                            if (result == null) {
                                result = aSeance.getDate();
                            }
                        }
                    } else if (countSemaine > nbrSemaine) {
                        return result;
                    }
                }
                i++;
            }
            if (result != null) {
                return result;
            }
        }
        
        // On n'a pas trouve de seance existante correspondant pour la remise du devoir.
        // Il faut se baser sur l'emploi du temps 
        
        
        // En nombre de seance
        if (nbrSeance != null) {
            Integer countSeance = 0;
            Date dateCourante = DateUtils.getDatePlus1(seanceCourante.getDate());
            while (dateCourante.before(dateSortie)) {
                if (!setVacance.contains(DateUtils.getChamp(dateCourante, Calendar.WEEK_OF_YEAR))) {  
                    List<CyclePlageEmploiDTO> listePlageJour = getListePlageJour(
                            listePlage, seanceCourante.getSequence().getIdEnseignement(), dateCourante, alternance, null, null);
                    for (i=0; i<listePlageJour.size(); i++) {
                        countSeance++;
                        if (countSeance >= nbrSeance) {
                            return dateCourante;
                        }
                    }
                }
                // Passe au jour suivant
                dateCourante = DateUtils.getDatePlus1(dateCourante);
            }
            return dateSortie;
            
        } else if (nbrSemaine != null) {

            // On saute le nombre de semaines
            Date dateCourante = DateUtils.ajouter(seanceCourante.getDate(),Calendar.WEEK_OF_YEAR, nbrSemaine);
            while (dateCourante.before(dateSortie)) {

                // Liste des plages à ce jour
                if (!setVacance.contains(DateUtils.getChamp(dateCourante, Calendar.WEEK_OF_YEAR))) { 
                    List<CyclePlageEmploiDTO> listePlageJour = getListePlageJour(
                            listePlage, seanceCourante.getSequence().getIdEnseignement(), dateCourante, alternance, null, null);
                    
                    // S'il y a une plage EDT ce jour, on retourne cette date
                    if (!listePlageJour.isEmpty()) {
                        return dateCourante;
                    }
                }
                // Passe au jour suivant
                dateCourante = DateUtils.getDatePlus1(dateCourante);
            }
            return dateSortie;
        }
        return dateSortie;
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<CycleSeanceFinalDTO>> findListeEmploiDTO(RechercheCycleEmploiQO rechercheCycleEmploiQO) {

        // Recupere les plages pour la classe/groupe, l'enseignant 
        final List<CyclePlageEmploiDTO> listePlage = cycleHibernateBusinessService.findListePlageEmploi(rechercheCycleEmploiQO);
        
        // Charge le paramétrage de l'etablissement pour l'aleternance des semaines
        final String alternanceSemaine = emploiHibernateBusinessService.findAlternanceSemaine(rechercheCycleEmploiQO.getIdEtablissement());
        final Map<Integer, TypeSemaine> alternance = GenerateurDTO.generateAlternanceSemaineFromDB(alternanceSemaine);
        
        // Calcule les numero de semaine vacances
        final Set<Integer> setVacance = GenerateurDTO.generateSetPeriodeVaqueFromDb(rechercheCycleEmploiQO.getAnneeScolaire().getPeriodeVacances());
        
        // Complete les date de fin de chaque période/plage par rapport à l'annee scolaire et les periode suivantes.
        if (listePlage != null && !listePlage.isEmpty()) {
            Date debutPeriodeCourant = listePlage.get(listePlage.size()-1).getDateDebutPeriode();
            Date finPeriodeCourant = rechercheCycleEmploiQO.getAnneeScolaire().getDateSortie();
            for (Integer i=listePlage.size()-1; i>=0; i--) {
                CyclePlageEmploiDTO plage = listePlage.get(i);
                
                // On change de periode 
                if (!DateUtils.equalsDate(debutPeriodeCourant, plage.getDateDebutPeriode())) {
                    finPeriodeCourant = DateUtils.getDateMoins1(debutPeriodeCourant);
                    debutPeriodeCourant = plage.getDateDebutPeriode();
                }
                
                // Positionne la date de fin
                plage.setDateFinPeriode(finPeriodeCourant);
            }
        }
        
        // Construit la liste resultat à partir de la date de debut
        final List<CycleSeanceFinalDTO> listeSeance = new ArrayList<CycleSeanceFinalDTO>();
        Integer i= 0;
        Integer indiceDeb = null;
        while (i < rechercheCycleEmploiQO.getListeSeance().size()) {
            
            final CycleSeanceFinalDTO cycleSeance = rechercheCycleEmploiQO.getListeSeance().get(i);
            
            // Cherche la seance qui correspond à la date de debut : 
            // Attention : on ne part de cette séance que si elle est sélectionnée. (checked)
            if (cycleSeance.getVraiOuFauxChecked() && (
                cycleSeance.getSeanceDTO().getDate() == null ||
                !rechercheCycleEmploiQO.getDateDepart().after(cycleSeance.getSeanceDTO().getDate()))
            ) {
                // On commence à cette indice
                indiceDeb = i;
                break;
            
            // Pour les seances avant la date de depart (deja calculee ou modifiee par l'utilisateur)
            // On les ajoute telle que
            } else {
                listeSeance.add(cycleSeance);
                i++;
            }
        }
        
        // Si on a trouve le premier indice
        if (indiceDeb != null) {
            
            // On boucle sur chaque seance a partir de cet indice pour trouver les prochaines dates
            Integer indiceDansJour = null;
            Integer idEnseignementCourant = null;
            List<CyclePlageEmploiDTO> listePlageJourEnseignement = new ArrayList<CyclePlageEmploiDTO>();
            Date dateCourante = rechercheCycleEmploiQO.getDateDepart();
            Integer heureDebCourante = null;
            Integer minuteDebCourante = null;
            
            for (i = indiceDeb; i < rechercheCycleEmploiQO.getListeSeance().size(); i++) {
                
                // Recupere l'objet seance
                final CycleSeanceFinalDTO cycleSeanceFinal = rechercheCycleEmploiQO.getListeSeance().get(i);
                
                // Si la séance est n'est pas sélectionnée (checked) on ne la traite pas : on la laisse telle que dans la liste
                if (!cycleSeanceFinal.getVraiOuFauxChecked()) {
                    listeSeance.add(cycleSeanceFinal);
                } else {
                
                    // Enseignement de la seance
                    final Integer idEnseignement =cycleSeanceFinal.getEnseignementDTO().getId();
                    
                    // Si on n'est pas sur le meme enseignement que l'enseignement courant
                    if (!idEnseignement.equals(idEnseignementCourant)) {
                        
                        // On recharge les plages de la journee pour cet enseignement
                        listePlageJourEnseignement = getListePlageJour(listePlage, idEnseignement, dateCourante, alternance, 
                                heureDebCourante, minuteDebCourante);
                        indiceDansJour = 0;
                        idEnseignementCourant = idEnseignement;
                    }
                    
                    // Incremente les jours jusqu'à etre dans une journee avec des plages correspondant à l'enseignement
                    while ((listePlageJourEnseignement.isEmpty() || indiceDansJour >= listePlageJourEnseignement.size()) 
                    && dateCourante.before(rechercheCycleEmploiQO.getAnneeScolaire().getDateSortie())) {
                        dateCourante = DateUtils.ajouter(dateCourante, Calendar.DAY_OF_YEAR, 1);
                        
                        // On saute le jour si on est dans les vacances
                        if (!setVacance.contains(DateUtils.getChamp(dateCourante, Calendar.WEEK_OF_YEAR))) { 
                            listePlageJourEnseignement = getListePlageJour(listePlage, idEnseignement, dateCourante, alternance, null, null);
                            indiceDansJour = 0;
                        }
                        // On a change de jour : on reset l'heure de debut
                        heureDebCourante = null;
                        minuteDebCourante = null;
                    }
                    
                    // Cherche dans la journee courante la prochaine plage 
                    if (indiceDansJour < listePlageJourEnseignement.size()) {
                        CyclePlageEmploiDTO plageSeance = listePlageJourEnseignement.get(indiceDansJour);
                        indiceDansJour++;
                        
                        // Positionne la plage dans la seance
                        cycleSeanceFinal.getSeanceDTO().setDate(dateCourante);
                        cycleSeanceFinal.getSeanceDTO().setHeureDebut(plageSeance.getHeureDebut());
                        cycleSeanceFinal.getSeanceDTO().setHeureFin(plageSeance.getHeureFin());
                        cycleSeanceFinal.getSeanceDTO().setMinuteDebut(plageSeance.getMinuteDebut());
                        cycleSeanceFinal.getSeanceDTO().setMinuteFin(plageSeance.getMinuteFin());
                        
                        // Positionne la sequence dans la seance
                        final SequenceDTO sequenceDTO = getSequenceActive(rechercheCycleEmploiQO.getListeSequence(), dateCourante, idEnseignement);
                        if (sequenceDTO != null) {
                            cycleSeanceFinal.getSeanceDTO().setSequenceDTO(sequenceDTO);
                            cycleSeanceFinal.setVraiOuFauxChecked(true); 
                        } 
                        
                        // Indique l'heure de debut pour que la prochaine plage si changement d'enseignement demarre après cette plage
                        // et ne recommence pas au debut de la journee.
                        heureDebCourante = plageSeance.getHeureDebut();
                        minuteDebCourante = plageSeance.getMinuteDebut();
                        
                        // Ajout la seance a la liste resultat
                        listeSeance.add(cycleSeanceFinal);
                    }
                    
                    heureDebCourante = cycleSeanceFinal.getSeanceDTO().getHeureDebut();
                    minuteDebCourante = cycleSeanceFinal.getSeanceDTO().getMinuteDebut();
                }
            }
            
            // Rafraichit les dates de remise des devoirs en fonction des dates des seances 
            // qui viennent d'etre calculees
            for (i = indiceDeb; i < listeSeance.size(); i++) {
                
                // Recupere l'objet seance
                final CycleSeanceFinalDTO cycleSeanceFinal = listeSeance.get(i);

                if (cycleSeanceFinal.getVraiOuFauxChecked()) {
                    // pour chaque devoir de la seance on détermine la date de remise des devoir
                    for (Integer j=0; j<cycleSeanceFinal.getSeanceDTO().getDevoirs().size(); j++) {
                        final DevoirDTO devoirDTO = cycleSeanceFinal.getSeanceDTO().getDevoirs().get(j);
                        final CycleDevoirDTO cycleDevoir = cycleSeanceFinal.getListeCycleDevoir().get(j);
                        
                        final Date dateRemise = calculerDateRemise(
                                listeSeance, 
                                listePlage, 
                                i, 
                                cycleDevoir.getDateRemise(), 
                                rechercheCycleEmploiQO.getAnneeScolaire().getDateSortie(),
                                alternance,
                                setVacance
                                );
                        devoirDTO.setDateRemise(dateRemise);
                    }
                }
            }
        }
        
        // Valeur de retour
        final ResultatDTO<List<CycleSeanceFinalDTO>> result = new ResultatDTO<List<CycleSeanceFinalDTO>>();
        result.setValeurDTO(listeSeance);
        if (listeSeance.isEmpty()) {
            final ConteneurMessage conteneurMessage = new ConteneurMessage();
            conteneurMessage.add(new Message(TypeReglesCycle.CYCLE_11.name(), Nature.BLOQUANT, 
                    DateUtils.format(rechercheCycleEmploiQO.getDateDepart())));
            result.setConteneurMessage(conteneurMessage);
        }
        return result;
        
    }
    
}
