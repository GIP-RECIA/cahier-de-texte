/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.design.JasperDesign;

import org.apache.commons.lang.ObjectUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.UserDTO;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.DateDTO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.PeriodeEmploiDTO;
import org.crlr.dto.application.base.PrintEmploiQO;
import org.crlr.dto.application.base.PrintEmploiQO.EcranOrigin;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.base.TypeEditionEmploiTemps;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.base.TypeReglesAcquittement;
import org.crlr.dto.application.base.TypeReglesEmploi;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.dto.application.emploi.RechercheEmploiQO;
import org.crlr.dto.application.emploi.SaveEmploiQO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.dto.application.sequence.RechercheSequenceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.business.AnneeScolaireHibernateBusinessService;
import org.crlr.metier.business.EleveHibernateBusinessService;
import org.crlr.metier.business.EmploiHibernateBusinessService;
import org.crlr.metier.business.EtablissementHibernateBusinessService;
import org.crlr.metier.business.SeanceHibernateBusinessService;
import org.crlr.metier.business.SequenceHibernateBusinessService;
import org.crlr.metier.entity.EleveBean;
import org.crlr.metier.utils.EmploiDeTempsReport;
import org.crlr.report.impl.PdfReport;
import org.crlr.report.impl.PdfReportGenerator;
import org.crlr.utils.Assert;
import org.crlr.utils.BooleanUtils;
import org.crlr.utils.DateUtils;
import org.crlr.web.dto.GrilleHoraireDTO;
import org.crlr.web.dto.TypeSemaine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Façade concernant les fonctionnalités du module devoir.
 * 
 * @author breytond
 * @version $Revision: 1.6 $
 */
@Component
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class EmploiFacade implements EmploiFacadeService {

    protected final Log log = LogFactory.getLog(getClass());

    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private EmploiHibernateBusinessService emploiHibernateBusinessService;

    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private SeanceHibernateBusinessService seanceHibernateBusinessService;

    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private SequenceHibernateBusinessService sequenceHibernateBusinessService;

    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private EleveHibernateBusinessService eleveHibernateBusinessService;

    @Autowired
    private AnneeScolaireHibernateBusinessService anneeScolaireHibernateBusinessService;
    
    @Autowired
    private EtablissementHibernateBusinessService etablissementHibernateBusinessService;

    /**
     * Mutateur de sequenceHibernateBusinessService.
     * 
     * @param sequenceHibernateBusinessService
     *            le sequenceHibernateBusinessService à modifier.
     */
    public void setSequenceHibernateBusinessService(
            SequenceHibernateBusinessService sequenceHibernateBusinessService) {
        this.sequenceHibernateBusinessService = sequenceHibernateBusinessService;
    }

    /**
     * Mutateur de emploiHibernateBusinessService.
     * 
     * @param emploiHibernateBusinessService
     *            le emploiHibernateBusinessService à modifier.
     */
    public void setEmploiHibernateBusinessService(
            EmploiHibernateBusinessService emploiHibernateBusinessService) {
        this.emploiHibernateBusinessService = emploiHibernateBusinessService;
    }

    /**
     * Mutateur de seanceHibernateBusinessService.
     * 
     * @param seanceHibernateBusinessService
     *            le seanceHibernateBusinessService à modifier.
     */
    public void setSeanceHibernateBusinessService(
            SeanceHibernateBusinessService seanceHibernateBusinessService) {
        this.seanceHibernateBusinessService = seanceHibernateBusinessService;
    }
    
    /**
     * @param conteneurMessage c
     * @param listeDetailJourEmploiDTO listeDetailJourEmploiDTO
     */
    public static void verifierEmploiDeTempsDebutFin(
            ConteneurMessage conteneurMessage,
            List<DetailJourEmploiDTO> listeDetailJourEmploiDTO) {
        for (final DetailJourEmploiDTO detailJourEmploiDTO : listeDetailJourEmploiDTO) {
            
                
            if ((detailJourEmploiDTO.getHeureDebut() * 60 + detailJourEmploiDTO
                    .getMinuteDebut()) >= (detailJourEmploiDTO
                    .getHeureFin() * 60 + detailJourEmploiDTO
                    .getMinuteFin())) {
                conteneurMessage.add(new Message(TypeReglesEmploi.EMPLOI_11.name(),
                        detailJourEmploiDTO.getJour(), 
                        DateUtils.formatTime(detailJourEmploiDTO.getHeureDebut(), detailJourEmploiDTO.getMinuteDebut()),
                        DateUtils.formatTime(detailJourEmploiDTO.getHeureFin(), detailJourEmploiDTO.getMinuteFin())));
            }
        }
        
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Date> saveEmploiDuTemps(final SaveEmploiQO saveEmploiQO)
            throws MetierException {
        Assert.isNotNull("saveEmploiQO", saveEmploiQO);

        final List<DetailJourEmploiDTO> listeDetailJourEmploiDTO = saveEmploiQO
                .getListeDetailJourEmploiDTO();
       

        Assert.isNotNull("listeDetailJourEmploiDTO", listeDetailJourEmploiDTO);
        
        // recherche des plages en base concernant les cellules à supprimer
        final List<Integer> listeIdEmploiAsupprimer = saveEmploiQO
                .getListeIdEmploiTempsAsupprimer();

        emploiHibernateBusinessService.deleteEmploi(listeIdEmploiAsupprimer);

        final ConteneurMessage cm = new ConteneurMessage();
        
        verifierEmploiDeTempsDebutFin(cm, listeDetailJourEmploiDTO);
        
        EtablissementDTO etablissement = new EtablissementDTO();

        // Parcours les cellules
        for (final DetailJourEmploiDTO detailJourEmploiDTO : listeDetailJourEmploiDTO) {

            // Si l'on a trouvé des incohérences
            if (cm.contientMessageBloquant()) {
                break;
            }
            // Tests qui sont liés uniquement au nouvelle cellules à sauvegarder
            // (création, mofication et non destruction.)
            if (BooleanUtils.isFalse(detailJourEmploiDTO
                    .getVraiOuFauxInitialisationEmp())) {
                
                
                if (!ObjectUtils.equals(etablissement.getId(), detailJourEmploiDTO.getIdEtablissement())) {
                    etablissement = etablissementHibernateBusinessService.findEtablissement(detailJourEmploiDTO.getIdEtablissement());
                }
                
                final Integer idClasse = detailJourEmploiDTO.getIdClasse();
                final Integer idGroupe = detailJourEmploiDTO.getIdGroupe();
                final Integer idEnseignement = detailJourEmploiDTO
                        .getIdEnseignement();

                if (idEnseignement == null) {
                    cm.add(new Message(TypeReglesEmploi.EMPLOI_00.name(),
                            detailJourEmploiDTO.getJour(), 
                            DateUtils.formatTime(detailJourEmploiDTO.getHeureDebut(), detailJourEmploiDTO.getMinuteDebut()),
                            DateUtils.formatTime(detailJourEmploiDTO.getHeureFin(), detailJourEmploiDTO.getMinuteFin()), "un enseignement"));
                }
                if (idClasse == null && idGroupe == null) {
                    cm.add(new Message(TypeReglesEmploi.EMPLOI_00.name(),
                            detailJourEmploiDTO.getJour(), 
                            DateUtils.formatTime(detailJourEmploiDTO.getHeureDebut(), detailJourEmploiDTO.getMinuteDebut()),
                            DateUtils.formatTime(detailJourEmploiDTO.getHeureFin(), detailJourEmploiDTO.getMinuteFin()), "une classe ou un groupe"));
                }
                if (detailJourEmploiDTO.getIdPeriodeEmploi() == null) {
                    cm.add(new Message(TypeReglesEmploi.EMPLOI_00.name(),
                            detailJourEmploiDTO.getJour(), 
                            DateUtils.formatTime(detailJourEmploiDTO.getHeureDebut(), detailJourEmploiDTO.getMinuteDebut()),
                            DateUtils.formatTime(detailJourEmploiDTO.getHeureFin(), detailJourEmploiDTO.getMinuteFin()), "une periode"));
                }
                
                List<TypeJour> listeJours = GenerateurDTO.getListeJourOuvreFromDb(etablissement.getJoursOuvres());
                
                if (!listeJours.contains(detailJourEmploiDTO.getJour())) {
                    cm.add(new Message(TypeReglesEmploi.EMPLOI_13.name(),
                            detailJourEmploiDTO.getJour(), 
                            DateUtils.formatTime(detailJourEmploiDTO.getHeureDebut(), detailJourEmploiDTO.getMinuteDebut()),
                            DateUtils.formatTime(detailJourEmploiDTO.getHeureFin(), detailJourEmploiDTO.getMinuteFin())));
                }
                
                int minutesEmpDebut = detailJourEmploiDTO.getHeureDebut() * 60 + detailJourEmploiDTO.getMinuteDebut();
                int minutesCoursDebut = etablissement.getHeureDebut() * 60 + etablissement.getMinuteDebut();
                
                if( minutesCoursDebut > minutesEmpDebut) {
                    cm.add(new Message(TypeReglesEmploi.EMPLOI_14.name(),
                            detailJourEmploiDTO.getJour(), 
                            DateUtils.formatTime(detailJourEmploiDTO.getHeureDebut(), detailJourEmploiDTO.getMinuteDebut()),
                            DateUtils.formatTime(detailJourEmploiDTO.getHeureFin(), detailJourEmploiDTO.getMinuteFin()), 
                                    DateUtils.formatTime(etablissement.getHeureDebut(), 
                                            etablissement.getMinuteDebut())));
                }
                
                if( detailJourEmploiDTO.getHeureFin() > etablissement.getHeureFin()) {
                    cm.add(new Message(TypeReglesEmploi.EMPLOI_15.name(),
                            detailJourEmploiDTO.getJour(), 
                            DateUtils.formatTime(detailJourEmploiDTO.getHeureDebut(), detailJourEmploiDTO.getMinuteDebut()),
                            DateUtils.formatTime(detailJourEmploiDTO.getHeureFin(), detailJourEmploiDTO.getMinuteFin()), 
                                    DateUtils.formatTime(etablissement.getHeureFin(), 
                                            0)));
                }
                
                if( ! org.apache.commons.lang.StringUtils.isEmpty(detailJourEmploiDTO.getCodeSalle()) && detailJourEmploiDTO.getCodeSalle().length() > 15) {
                    cm.add(new Message(TypeReglesEmploi.EMPLOI_16.name(),
                    		detailJourEmploiDTO.getJour(), 
                            DateUtils.formatTime(detailJourEmploiDTO.getHeureDebut(), detailJourEmploiDTO.getMinuteDebut()),
                            DateUtils.formatTime(detailJourEmploiDTO.getHeureFin(), detailJourEmploiDTO.getMinuteFin()) ));
                }
            }
        }

        if (cm.contientMessageBloquant()) {
            throw new MetierException(cm,
                    "La sauvegarde de l'emploi du temps à échouée.");
        }

        return emploiHibernateBusinessService
                .saveEmploiDuTemps(listeDetailJourEmploiDTO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Date> dupliquerEmploiSemaine(
            final TypeSemaine origineTypeSemaine,
            final TypeSemaine cibleTypeSemaine, final PeriodeEmploiDTO periode)
            throws MetierException {
        Assert.isNotNull("periode", periode);

        // Détruit la semaine cible
        emploiHibernateBusinessService.deleteEmploiSemaine(periode.getId(), periode
                .getIdEnseignant(), periode.getIdEtablissement(),
                cibleTypeSemaine);

        List<DetailJourEmploiDTO> listeEmp = emploiHibernateBusinessService
                .findEmploi(periode.getIdEnseignant(),
                        periode.getIdEtablissement(), origineTypeSemaine,
                        periode.getId()).getValeurDTO();

        for (DetailJourEmploiDTO jour : listeEmp) {
            jour.setId(null);
            jour.setTypeSemaine(cibleTypeSemaine);
        }

        // enregistre la semaine
        final ResultatDTO<Date> result = emploiHibernateBusinessService
                .saveEmploiDuTemps(listeEmp);

        // retourne le resultat du save
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<DetailJourEmploiDTO>> findEmploi(
            final Integer idEnseignant, final Integer idEtablissement,
            final TypeSemaine typeSemaine, final Integer idPeriode) {
        return emploiHibernateBusinessService.findEmploi(idEnseignant,
                idEtablissement, typeSemaine, idPeriode);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<DetailJourEmploiDTO>> findEmploiConsolidation(
            RechercheEmploiQO rechercheEmploiQO) throws MetierException {
        Assert.isNotNull("rechercheEmploiQO", rechercheEmploiQO);

        return emploiHibernateBusinessService
                .findEmploiConsolidation(rechercheEmploiQO);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Integer> findSeanceEmploiSemaine(
            RechercheSeanceQO rechercheSeanceQO) {
        Assert.isNotNull("rechercheSeanceQO", rechercheSeanceQO);

        final Integer idEtablissement = rechercheSeanceQO.getIdEtablissement();
        final Integer idEnseignant = rechercheSeanceQO.getIdEnseignant();
        final Date dateDebut = rechercheSeanceQO.getDateDebut();
        final Date dateFin = rechercheSeanceQO.getDateFin();

        Assert.isNotNull("idEtablissement", idEtablissement);
        Assert.isNotNull("idEnseignant", idEnseignant);
        Assert.isNotNull("dateDebut", dateDebut);
        Assert.isNotNull("dateFin", dateFin);

        // recherche des
        return seanceHibernateBusinessService.findSeanceSemaine(dateDebut,
                dateFin, idEnseignant, idEtablissement);
    }

    /**
     * {@inheritDoc}
     */
    public List<SeanceDTO> findListeSeanceSemaine(
            RechercheSeanceQO rechercheSeanceQO) {
        Assert.isNotNull("rechercheSeanceQO", rechercheSeanceQO);

        final Integer idEtablissement = rechercheSeanceQO.getIdEtablissement();
        final Integer idEnseignant = rechercheSeanceQO.getIdEnseignant();
        final Date dateDebut = rechercheSeanceQO.getDateDebut();
        final Date dateFin = rechercheSeanceQO.getDateFin();

        Assert.isNotNull("idEtablissement", idEtablissement);
        Assert.isNotNull("idEnseignant", idEnseignant);
        Assert.isNotNull("dateDebut", dateDebut);
        Assert.isNotNull("dateFin", dateFin);

        // recherche des id des sequences de l'enseignant sur cet etablissement
        final Set<Integer> listeIdSequenceEtab = sequenceHibernateBusinessService
                .findSequenceEtablissement(idEtablissement, idEnseignant);

        // Cherche parmi ces sequences, les seances inervenant sur la période
        return seanceHibernateBusinessService.findListeSeanceSemaine(dateDebut,
                dateFin, idEnseignant, idEtablissement, listeIdSequenceEtab);
    }

    /**
     * @param printEmploiQO
     *            p
     * @return le titre
     */
    private String getEmploiDuTempsReportTitre(PrintEmploiQO printEmploiQO) {
        String titrePDF = "Emploi du temps ";

        final TypeEditionEmploiTemps typeEmploiTemps = printEmploiQO
                .getTypeEditionEmploiTemps();

        // Génération du titre
        switch (typeEmploiTemps) {
        case Enseignant:
        case Eleve:
            final UtilisateurDTO utilisateurDTO = printEmploiQO
                    .getUtilisateurDTO();
            Assert.isNotNull("utilisateurDTO", utilisateurDTO);

            final UserDTO userDTO = utilisateurDTO.getUserDTO();
            Assert.isNotNull("userDTO", userDTO);
            titrePDF += ("de " + utilisateurDTO.getCivilite() + " "
                    + userDTO.getNom() + " " + userDTO.getPrenom());
            titrePDF += (" - " + utilisateurDTO.getDesignationEtablissement());
            break;
        case Parent:
            final UtilisateurDTO utilisateurParentDTO = printEmploiQO
                    .getUtilisateurDTO();
            Assert.isNotNull("utilisateurParentDTO", utilisateurParentDTO);

            final Integer idEnfant = utilisateurParentDTO.getUserDTO()
                    .getIdentifiant();
            Assert.isNotNull("idEnfant", idEnfant);

            final EleveBean eleveBean = eleveHibernateBusinessService
                    .find(idEnfant);

            titrePDF += ("de " + eleveBean.getNom() + " " + eleveBean
                    .getPrenom());
            titrePDF += (" - " + utilisateurParentDTO
                    .getDesignationEtablissement());
            break;
        case ClasseOuGroupe:
            final TypeGroupe typeGroupe = printEmploiQO
                    .getTypeGroupeSelectionne();
            final GroupesClassesDTO groupesClassesDTO = printEmploiQO
                    .getGroupeClasseDTO();

            Assert.isNotNull("typeGroupe", typeGroupe);
            Assert.isNotNull("groupesClassesDTO", groupesClassesDTO);
            if (TypeGroupe.CLASSE == typeGroupe) {
                titrePDF += "de la classe ";
            } else {
                titrePDF += "du groupe ";
            }

            titrePDF += groupesClassesDTO.getIntitule();

            if (TypeGroupe.CLASSE == typeGroupe
                    && !org.apache.commons.collections.CollectionUtils
                            .isEmpty(printEmploiQO.getListeGroupe())) {
                final List<GroupeDTO> groupes = printEmploiQO.getListeGroupe();
                String suiteTitre = "";
                int i = 0;
                for (GroupeDTO groupe : groupes) {
                    if (groupe.getSelectionner()) {
                        i++;
                        if (i == 1) {
                            suiteTitre += groupe.getIntitule();
                        } else {
                            suiteTitre += ("," + groupe.getIntitule());
                        }
                    }
                }
                if (i == 1) {
                    titrePDF += (" et du groupe " + suiteTitre);
                } else if (i > 1) {
                    titrePDF += (" et des groupes " + suiteTitre);
                }
            }

            final UtilisateurDTO utilisateurClasseDTO = printEmploiQO
                    .getUtilisateurDTO();
            Assert.isNotNull("utilisateurDTO", utilisateurClasseDTO);
            titrePDF += (" - " + utilisateurClasseDTO
                    .getDesignationEtablissement());

            break;
        default:
        }

        return titrePDF;
    }

    /**
     * {@inheritDoc}
     */
    public PdfReport printEmploiDuTemps(PrintEmploiQO printEmploiQO) {
        Assert.isNotNull("printEmploiQO", printEmploiQO);

        final TypeEditionEmploiTemps typeEmploiTemps = printEmploiQO
                .getTypeEditionEmploiTemps();
        Assert.isNotNull("typeEmploiTemps", typeEmploiTemps);

        // Les parametres que l'on passe au report.
        final Map<String, Object> args = new HashMap<String, Object>();

        // Les jours utilisés/
        Assert.isNotNull("jours", printEmploiQO.getJourOuvreAccessible());
        Assert.isNotNull("debut", printEmploiQO.getHeureDebut());
        Assert.isNotNull("fin", printEmploiQO.getHeureFin());

        String titrePDF = getEmploiDuTempsReportTitre(printEmploiQO);

        args.put("titre", titrePDF);

        args.put("typeSemaine", (Boolean) printEmploiQO.getTypeSemaine());

        // generation effective du report.
        final PdfReportGenerator gen = new PdfReportGenerator();

        JasperDesign jasperDesign = null;
        
        if (printEmploiQO.getEcranOrigin() == EcranOrigin.CONSOLIDATION) {
            args.put("datesLabel", "De " + DateUtils.format(printEmploiQO.getDateDebut())
                    + " au " + DateUtils.format(printEmploiQO.getDateFin()));
            jasperDesign = gen
                    .getJasperReport("/org/crlr/report/emploiTempsConsolidation.jrxml");
        } else {
            jasperDesign = gen
                .getJasperReport("/org/crlr/report/emploiTempsEns.jrxml");
        }

        new EmploiDeTempsReport(jasperDesign, printEmploiQO.getHeureDebut(),
                printEmploiQO.getHeureFin(), printEmploiQO
                        .getJourOuvreAccessible(), printEmploiQO
                        .getListeEmploiDeTemps());

        return gen.generate(jasperDesign, null, args);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, SeanceDTO> findSeanceEmploiSemainePassee(
            RechercheSeanceQO rechercheSeanceQO,
            final List<GrilleHoraireDTO> listeHoraire) {
        Assert.isNotNull("rechercheSeanceQO", rechercheSeanceQO);
        Assert.isNotNull("listeHoraire", listeHoraire);

        final Integer idEtablissement = rechercheSeanceQO.getIdEtablissement();
        final Integer idEnseignant = rechercheSeanceQO.getIdEnseignant();
        final Date dateDebut = rechercheSeanceQO.getDateDebut();
        final Date dateFin = rechercheSeanceQO.getDateFin();

        Assert.isNotNull("idEtablissement", idEtablissement);
        Assert.isNotNull("idEnseignant", idEnseignant);
        Assert.isNotNull("dateDebut", dateDebut);
        Assert.isNotNull("dateFin", dateFin);

        final Set<Integer> listeIdSequenceEtab = sequenceHibernateBusinessService
                .findSequenceEtablissement(idEtablissement, idEnseignant);
        return seanceHibernateBusinessService.findSeanceSemainePassee(
                dateDebut, dateFin, idEnseignant, listeIdSequenceEtab,
                listeHoraire);
    }

   

    /**
     * {@inheritDoc}
     */
    public List<SequenceDTO> chercherSequenceSemaine(
            RechercheSequenceQO rechercheSequence) {
        return this.sequenceHibernateBusinessService
                .chercherSequenceSemaine(rechercheSequence);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.crlr.services.EmploiService#findPeriodes(java.lang.Integer,
     * java.lang.Integer)
     */
    @Override
    public List<PeriodeEmploiDTO> findPeriodes(Integer enseignantId,
            Integer etablissementId) {
        return this.emploiHibernateBusinessService.findPeriodes(enseignantId,
                etablissementId);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.crlr.metier.facade.EmploiFacadeService#savePeriode(org.crlr.dto.
     * application.base.PeriodeEmploiDTO)
     */
    @Override
    public ResultatDTO<PeriodeEmploiDTO> creerPeriode(PeriodeEmploiDTO periode,
            Integer idAnneeScolaire, Integer initPeriodeId)
            throws MetierException {

        ConteneurMessage conteneurMessage = new ConteneurMessage();

        if (emploiHibernateBusinessService.existePeriode(periode)) {
            conteneurMessage.add(new Message(TypeReglesEmploi.EMPLOI_10.name(),
                    DateUtils.format(periode.getDateDebut())));
        }

        final boolean checkAnneeScolaire = anneeScolaireHibernateBusinessService
                .checkDateAnneeScolaire(idAnneeScolaire,
                        periode.getDateDebut(), periode.getDateDebut());

        if (!checkAnneeScolaire) {

            AnneeScolaireDTO anneeScolaire = anneeScolaireHibernateBusinessService
                    .findAnneeScolaire();

            if (!anneeScolaire.getId().equals(idAnneeScolaire)) {
                log.warning("Invalide annee scolaire id {0}", idAnneeScolaire);
            }

            conteneurMessage.add(new Message(TypeReglesEmploi.EMPLOI_12.name(),
                    Nature.BLOQUANT, DateUtils.format(periode.getDateDebut()),
                    DateUtils.format(anneeScolaire.getDateRentree()), DateUtils
                            .format(anneeScolaire.getDateSortie())));
            throw new MetierException(conteneurMessage, "Le date de début.");
        }

        if (conteneurMessage.contientMessageBloquant()) {
            throw new MetierException(conteneurMessage,
                    "La sauvegarde de la période à échouée.");
        }

        conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00
                .name(), Nature.INFORMATIF, "La période", "créée"));

        emploiHibernateBusinessService.creerPeriode(periode, initPeriodeId);

        ResultatDTO<PeriodeEmploiDTO> resultat = new ResultatDTO<PeriodeEmploiDTO>();
        resultat.setValeurDTO(periode);
        resultat.setConteneurMessage(conteneurMessage);
        return resultat;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.crlr.metier.facade.EmploiFacadeService#deletePeriode(org.crlr.dto
     * .application.base.PeriodeEmploiDTO)
     */
    @Override
    public ResultatDTO<PeriodeEmploiDTO> deletePeriode(PeriodeEmploiDTO periode) {
        emploiHibernateBusinessService.deletePeriode(periode);

        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00
                .name(), Nature.INFORMATIF, "La période", "supprimée"));

        ResultatDTO<PeriodeEmploiDTO> resultat = new ResultatDTO<PeriodeEmploiDTO>();
        resultat.setValeurDTO(periode);
        resultat.setConteneurMessage(conteneurMessage);
        return resultat;

    }

    /**
     * {@inheritDoc}
     */
    public List<DateDTO> findProchaineDate(RechercheEmploiQO rechercheEmploi) {
        return emploiHibernateBusinessService.findProchaineDate(rechercheEmploi);
    }
}
