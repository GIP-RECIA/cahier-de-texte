/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.business;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.alimentation.Archive;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.base.TypeCategorieTypeDevoir;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.dto.application.seance.ConsulterSeanceQO;
import org.crlr.dto.application.seance.PrintSeanceDTO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.dto.application.seance.ResultatRechercheSeanceDTO;
import org.crlr.dto.application.seance.TypeReglesSeance;
import org.crlr.dto.application.sequence.PrintSeanceOuSequenceQO;
import org.crlr.dto.application.sequence.PrintSequenceDTO;
import org.crlr.dto.application.sequence.ResultatRechercheSequenceDTO;
import org.crlr.dto.application.visa.VisaDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.entity.ClasseBean;
import org.crlr.metier.entity.CouleurEnseignementClasseBean;
import org.crlr.metier.entity.ElevesClassesBean;
import org.crlr.metier.entity.ElevesGroupesBean;
import org.crlr.metier.entity.EnseignantBean;
import org.crlr.metier.entity.EnseignantsClassesBean;
import org.crlr.metier.entity.EnseignantsGroupesBean;
import org.crlr.metier.entity.EnseignementLibelleBean;
import org.crlr.metier.entity.EtablissementBean;
import org.crlr.metier.entity.GroupeBean;
import org.crlr.metier.entity.OuvertureInspecteurBean;
import org.crlr.metier.entity.SeanceBean;
import org.crlr.metier.entity.SequenceBean;
import org.crlr.metier.entity.VisaBean;
import org.crlr.metier.entity.VisaSeanceBean;
import org.crlr.metier.utils.SchemaUtils;
import org.crlr.utils.Assert;
import org.crlr.utils.ComparateurUtils;
import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.dto.GrilleHoraireDTO;
import org.crlr.web.dto.TypeCouleur;
import org.crlr.web.dto.TypeSemaine;
import org.crlr.web.utils.FacesUtils;
import org.crlr.web.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.base.Preconditions;

/**
 * SeanceHibernateBusiness.
 *
 * @author vibertd
 * @version $Revision: 1.56 $
 */
@Repository
public class SeanceHibernateBusiness extends AbstractBusiness
    implements SeanceHibernateBusinessService {
    
    @Autowired
    private EmploiHibernateBusinessService emploiHibernateBusinessService;

    
    @Autowired
    private EtablissementHibernateBusinessService etablissementHibernateBusinessService;
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public SeanceDTO trouver(final Integer idSeance, final Boolean archive,
                          final String exercice) {
        final SeanceDTO seance;

        final String schema = SchemaUtils.getSchemaCourantOuArchive(archive, exercice);

        final String query =
            "SELECT s.id, s.code, s.intitule, s.date, " +
            " s.heure_debut, s.minute_debut, s.heure_fin, s.minute_fin," +
            " s.description, s.id_sequence, s.id_enseignant, s.annotations FROM " +
            SchemaUtils.getTableAvecSchema(schema, "cahier_seance") + " s WHERE s.id =?";

        final List<Object[]> liste =
            getEntityManager().createNativeQuery(query).setParameter(1, idSeance)
                .getResultList();

        if (! org.apache.commons.collections.CollectionUtils.isEmpty(liste)) {
            final Object[] result = liste.get(0);
            seance = new SeanceDTO();
            seance.setId((Integer) result[0]);
            seance.setCode((String) result[1]);
            seance.setIntitule((String) result[2]);
            seance.setDate((Date) result[3]);
            seance.setHeureDebut((Integer) result[4]);
            seance.setMinuteDebut((Integer) result[5]);
            seance.setHeureFin((Integer) result[6]);
            seance.setMinuteFin((Integer) result[7]);
            seance.setDescription((String) result[8]);
            seance.getSequence().setId((Integer) result[9]);
            seance.getEnseignantDTO().setId((Integer) result[10]);
            seance.setAnnotations((String) result[11]);
        } else {
            seance = null;
        }

        return seance;
    }

    /**
     * {@inheritDoc}
     */
    public SeanceDTO findSeancePrecedente(final RechercheSeanceQO rechercheSeanceQO) {
        final List<SeanceDTO> listeSeance = findListeSeancePrecedente(rechercheSeanceQO, 1);
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(listeSeance)) {
            return listeSeance.get(0);
        }
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<SeanceDTO> findListeSeancePrecedente(final RechercheSeanceQO rechercheSeanceQO, final Integer nbrSeance) {
        final TypeGroupe typeGroupe = rechercheSeanceQO.getTypeGroupe();
        
        String queryClasseOuGroupe;
        if (typeGroupe.equals(TypeGroupe.CLASSE)) {
            queryClasseOuGroupe = "SEQ.idClasse = :idClasseGroupe ";
        } else {
            queryClasseOuGroupe = "SEQ.idGroupe = :idClasseGroupe ";
        }
        String requeteIdSeance = "";
        if (rechercheSeanceQO.getIdSeance() != null){
            requeteIdSeance = " AND SEA.id != :idSeanceCible ";
        }

        /**
         * Recherche la deniere seance antérieure à la date passée pour le
         * même enseignant, enseignement et même classe/groupe
         */
        final String query =
            "SELECT new map(" + " SEA.id as idSeance," +
            " SEA.idSequence as idSequence, " + " SEA.code as code, " +
            " SEA.intitule as intitule, " + " SEA.date as date, " +
            " SEA.heureDebut as heureDebut, " + " SEA.minuteDebut as minuteDebut, " +
            " SEA.heureFin as heureFin," + " SEA.minuteFin as minuteFin, " +
            " SEA.description as description, " + " SEA.annotations as annotations, " +
            " SEA.idEnseignant as idEnseignantSea, " +
            " SEQ.idEnseignant as idEnseignantSeq, " +
            " SEQ.idEtablissement as idEtablissement, " +
            " SEA.idSequence as idSequence, " +            
            " SEQ.idEnseignement as idEnseignement) " + " FROM " +
            SequenceBean.class.getName() + " SEQ, " +
            SeanceBean.class.getName() + " SEA " +
            " WHERE SEQ.id = SEA.idSequence AND " +
            " SEQ.idEnseignement = :idEnseignement AND " + queryClasseOuGroupe +
            " AND SEQ.idEnseignant = :idEnseignant AND " +
            " ((SEA.date < :date) OR (SEA.date = :date AND SEA.heureDebut < :heureDebut) "+
            " OR (SEA.date = :date AND SEA.heureDebut = :heureDebut AND SEA.minuteDebut < :minuteDebut))" +
            requeteIdSeance +
            " ORDER BY SEA.date DESC, SEA.heureDebut DESC, SEA.minuteDebut DESC LIMIT 10 ";

        final Query q = getEntityManager().createQuery(query)
        .setParameter("idEnseignement", rechercheSeanceQO.getIdEnseignement())
        .setParameter("idEnseignant", rechercheSeanceQO.getIdEnseignant())
        .setParameter("idClasseGroupe", rechercheSeanceQO.getIdClasseGroupe())
        .setParameter("date", rechercheSeanceQO.getDateDebut())
        .setParameter("heureDebut", rechercheSeanceQO.getHeureDebut())
        .setParameter("minuteDebut", rechercheSeanceQO.getMinuteDebut());
        
        if (rechercheSeanceQO.getIdSeance() != null){
            q.setParameter("idSeanceCible", rechercheSeanceQO.getIdSeance());
        }
        
        final List<Map<String, ?>> resultatQuery = q.getResultList();
        Integer nbrResult = 1;
        
        // La liste doit contenir au moins 1 element
        final List<SeanceDTO> listeSeance = new ArrayList<SeanceDTO>();
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(resultatQuery)) {
            for (final Map<String, ?> result : resultatQuery) {
                nbrResult++;
                final SeanceDTO seance = new SeanceDTO();
                seance.setId((Integer) result.get("idSeance"));
                seance.setCode((String) result.get("code"));
                seance.setIntitule((String) result.get("intitule"));
                seance.setDate((Date) result.get("date"));
                seance.setHeureDebut((Integer) result.get("heureDebut"));
                seance.setMinuteDebut((Integer) result.get("minuteDebut"));
                seance.setHeureFin((Integer) result.get("heureFin"));
                seance.setMinuteFin((Integer) result.get("minuteFin"));
                seance.setDescription((String) result.get("description"));
                seance.getSequenceDTO().setId((Integer) result.get("idSequence"));
                seance.getSequenceDTO().setIdEnseignement((Integer) result.get("idEnseignement"));
                seance.setAnnotations((String) result.get("annotations"));
                seance.setIdEnseignant((Integer) result.get("idEnseignantSea"));
                seance.getSequenceDTO().setIdEnseignant((Integer) result.get("idEnseignantSeq"));
                seance.getSequenceDTO().setIdEtablissement((Integer) result.get("idEtablissement"));
                seance.getSequenceDTO().getGroupesClassesDTO().setId(rechercheSeanceQO.getIdClasseGroupe());
                seance.getSequenceDTO().getGroupesClassesDTO().setTypeGroupe(typeGroupe);
                
                
                listeSeance.add(seance);
                
                if (nbrResult.compareTo(nbrSeance) > 0) {
                    return listeSeance; 
                }
            }
        }
        return listeSeance;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<ResultatRechercheSeanceDTO>> findSeance(RechercheSeanceQO rechercheSeanceQO)
        throws MetierException {
        Assert.isNotNull("rechercheSeanceQO", rechercheSeanceQO);

        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        //Paramètres de recherche
        final Integer idEtablissement = rechercheSeanceQO.getIdEtablissement();
        final Integer idEnseignement = rechercheSeanceQO.getIdEnseignement();
        final Integer idEnseignant = rechercheSeanceQO.getIdEnseignant();
        final Integer idClasseGroupe = rechercheSeanceQO.getIdClasseGroupe();
        final Integer idSequence = rechercheSeanceQO.getIdSequence();
        final Date dateDebut = rechercheSeanceQO.getDateDebut();
        final Date dateFin = rechercheSeanceQO.getDateFin();
        TypeGroupe typeGroupe = rechercheSeanceQO.getTypeGroupe();

        //Règles de gestion SEANCE_00
        if (!this.checkSEANCE00(dateDebut, dateFin)) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_00.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "La date de fin de pèriode doit être postèrieure ou égale à la date de début.");
        }

        //Règles de gestion SEANCE_01
        if (!this.checkSEANCE01(dateDebut, dateFin)) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_01.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "Vous devez saisir la date de début de période.");
        }

        final ResultatDTO<List<ResultatRechercheSeanceDTO>> resultat =
            new ResultatDTO<List<ResultatRechercheSeanceDTO>>();

        String requeteWHERE = "";

        if (idEnseignement != null) {
            requeteWHERE = requeteWHERE.concat(" AND SEQ.idEnseignement = :idEnseignement ");
        }
        if (idClasseGroupe != null) {
            if (typeGroupe  == (TypeGroupe.GROUPE)) {
                requeteWHERE = requeteWHERE.concat(" AND SEQ.idGroupe = :idGroupe ");
            } else if (typeGroupe  == (TypeGroupe.CLASSE)) {
                requeteWHERE = requeteWHERE.concat(" AND SEQ.idClasse = :idClasse ");
            }
        }

        if (idSequence != null) {
            requeteWHERE = requeteWHERE.concat(" AND SEQ.id = :idSequence ");
        }
        if ((idClasseGroupe == null) && (idSequence == null)) {
            //Filtrer sur l'etablissement
            if (typeGroupe != null) {
                if (TypeGroupe.GROUPE  == (typeGroupe)) {
                    requeteWHERE = requeteWHERE.concat(" AND GRP.idEtablissement = :idEtabGrp ");
                } else if (TypeGroupe.CLASSE  == (typeGroupe)) {
                    requeteWHERE = requeteWHERE.concat(" AND CLA.idEtablissement = :idEtabCla ");
                }
            } else {
                requeteWHERE = requeteWHERE.concat(" AND (CLA.idEtablissement = :idEtabCla ");
                requeteWHERE = requeteWHERE.concat(" OR GRP.idEtablissement = :idEtabGrp) ");
            }
        }

        if (dateDebut != null) {
            requeteWHERE = requeteWHERE.concat(" AND SEA.date >= :dateDebut ");
        }
        if (dateFin != null) {
            requeteWHERE = requeteWHERE.concat(" AND SEA.date <= :dateFin ");
        }

        final String champsCommons = 
              "ENS.designation as designationEnseignement, " +
              "ENS.id as idEnseignement," +
              "SEQ.idEnseignant as idEnseignantSeq, " +
              "SEQ.idEtablissement as idEtablissement, " +
              "SEA.idEnseignant as idEnseignantSea, " +
              "SEQ.description as designationSequence";
        
        String requete = "";

        if ((idClasseGroupe == null) && (typeGroupe != null)) {
            if (typeGroupe  == (TypeGroupe.GROUPE)) {
                requete = "SELECT " + " new map(" + "SEA as seance, " +
                          "GRP.code as codeGroupe, GRP.id as idGroupe, " +
                          "GRP.designation as designationGroupe, " +
                          champsCommons +
                          ") " +
                          " FROM " + SequenceBean.class.getName() + " SEQ " +
                          " INNER JOIN SEQ.enseignement ENS " +
                          " INNER JOIN SEQ.groupe GRP, " +
                          SeanceBean.class.getName() + " SEA WHERE " +
                          " SEQ.idEnseignant = :idEnseignant AND " +
                          " SEA.idSequence = SEQ.id " + requeteWHERE +
                          " ORDER BY SEA.date DESC, SEA.code DESC";
            } else if (typeGroupe  == (TypeGroupe.CLASSE)) {
                requete = "SELECT " + " new map(" + "SEA as seance, " +
                           "CLA.code as codeClasse, CLA.id as idClasse, " +
                          "CLA.designation as designationClasse, " +
                          champsCommons +
                          ") " +
                          " FROM " + SequenceBean.class.getName() + " SEQ " +
                          " INNER JOIN SEQ.enseignement ENS " +
                          " INNER JOIN SEQ.classe CLA, " +
                          SeanceBean.class.getName() + " SEA WHERE " +
                          " SEQ.idEnseignant = :idEnseignant AND " +
                          " SEA.idSequence = SEQ.id " + requeteWHERE +
                          " ORDER BY SEA.date DESC, SEA.code DESC";
            }
        } else {
            requete = "SELECT " + " new map(" + "SEA as seance, GRP.code as codeGroupe, " +
                      "GRP.id as idGroupe, " +
                      "GRP.designation as designationGroupe, " +
                      "CLA.code as codeClasse, CLA.id as idClasse, " +
                      "CLA.designation as designationClasse, " +
                      champsCommons +
                      ") " +
                      " FROM " + SequenceBean.class.getName() + " SEQ " +
                      " INNER JOIN SEQ.enseignement ENS " +
                      " LEFT JOIN SEQ.classe CLA " +
                      " LEFT JOIN SEQ.groupe GRP, " + SeanceBean.class.getName() +
                      " SEA " + " WHERE " + " SEQ.idEnseignant = :idEnseignant AND " +
                      " SEA.idSequence = SEQ.id " + requeteWHERE +
                      " ORDER BY SEA.date DESC, SEA.code DESC";
        }

        final Query query = getEntityManager().createQuery(requete);

        query.setParameter("idEnseignant", idEnseignant);

        if (idEnseignement != null) {
            query.setParameter("idEnseignement", idEnseignement);
        }
        if (idClasseGroupe != null) {
            if (typeGroupe  == (TypeGroupe.GROUPE)) {
                query.setParameter("idGroupe", idClasseGroupe);
            } else if (typeGroupe  == (TypeGroupe.CLASSE)) {
                query.setParameter("idClasse", idClasseGroupe);
            }
        }
        if (idSequence != null) {
            query.setParameter("idSequence", idSequence);
        }
        if ((idClasseGroupe == null) && (idSequence == null)) {
            if (typeGroupe != null) {
                if (TypeGroupe.GROUPE  == (typeGroupe)) {
                    query.setParameter("idEtabGrp", idEtablissement);
                } else if (TypeGroupe.CLASSE  == (typeGroupe)) {
                    query.setParameter("idEtabCla", idEtablissement);
                }
            } else {
                query.setParameter("idEtabCla", idEtablissement);
                query.setParameter("idEtabGrp", idEtablissement);
            }
        }

        if (dateDebut != null) {
            query.setParameter("dateDebut", dateDebut);
        }
        if (dateFin != null) {
            query.setParameter("dateFin", dateFin);
        }

        final List<Map<String, ?>> resultatQuery = query.getResultList();
        final List<ResultatRechercheSeanceDTO> listeSeanceDTO =
            new ArrayList<ResultatRechercheSeanceDTO>();

        for (final Map<String, ?> result : resultatQuery) {
            final ResultatRechercheSeanceDTO resultatRechercheSeanceDTO =
                new ResultatRechercheSeanceDTO();
            
            SeanceBean seance = (SeanceBean) result.get("seance");
            
            ObjectUtils.copyProperties(resultatRechercheSeanceDTO, seance);
           
            if (idClasseGroupe == null) {
                if (result.get("codeClasse") != null) {
                    typeGroupe = TypeGroupe.CLASSE;
                } else {
                    typeGroupe = TypeGroupe.GROUPE;
                }
            }
           
            
            resultatRechercheSeanceDTO.getSequenceDTO().getGroupesClassesDTO().setTypeGroupe(typeGroupe);
            
            if (typeGroupe  == (TypeGroupe.GROUPE)) {
                resultatRechercheSeanceDTO.getSequenceDTO().getGroupesClassesDTO().setCode((String) result.get("codeGroupe"));
                resultatRechercheSeanceDTO.getSequenceDTO().getGroupesClassesDTO().setDesignation((String) result.get("designationGroupe"));
                resultatRechercheSeanceDTO.getSequenceDTO().getGroupesClassesDTO().setId((Integer) result.get("idGroupe"));
                
            } else if (typeGroupe  == (TypeGroupe.CLASSE)) {
                resultatRechercheSeanceDTO.getSequenceDTO().getGroupesClassesDTO().setCode((String) result.get("codeClasse"));
                resultatRechercheSeanceDTO.getSequenceDTO().getGroupesClassesDTO().setDesignation((String) result.get("designationClasse"));
                resultatRechercheSeanceDTO.getSequenceDTO().getGroupesClassesDTO().setId((Integer) result.get("idClasse"));
            }
            
            resultatRechercheSeanceDTO.setDesignationEnseignement((String) result.get("designationEnseignement"));
            resultatRechercheSeanceDTO.getSequenceDTO().setDescription((String) result.get("designationSequence"));
            resultatRechercheSeanceDTO.getSequence().setIdEnseignement((Integer) result.get("idEnseignement"));
            resultatRechercheSeanceDTO.getSequenceDTO().setIdEtablissement((Integer) result.get("idEtablissement"));
            resultatRechercheSeanceDTO.getSequenceDTO().setIdEnseignant((Integer) result.get("idEnseignantSeq"));
            resultatRechercheSeanceDTO.setIdEnseignant((Integer) result.get("idEnseignantSea"));
            
            listeSeanceDTO.add(resultatRechercheSeanceDTO);
        }

        if (listeSeanceDTO.size() == 0) {
            conteneurMessage.add(new Message(rechercheSeanceQO.getTypeReglesSeance().name(),
                                             Nature.INFORMATIF));
            throw new MetierException(conteneurMessage,
                                      "Il n'existe aucun résultat pour votre recherche.");
        }

        resultat.setValeurDTO(listeSeanceDTO);

        return resultat;
    }
    
    

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<SeanceDTO>> findSeanceForPlanning(RechercheSeanceQO rechercheSeanceQO)
    throws MetierException {
        Assert.isNotNull("rechercheSeanceQO", rechercheSeanceQO);

        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        //Paramètres de recherche
        final Integer idEtablissement = rechercheSeanceQO.getIdEtablissement();
        final Integer idEnseignant = rechercheSeanceQO.getIdEnseignant();
        final Date dateDebut = rechercheSeanceQO.getDateDebut();
        final Date dateFin = rechercheSeanceQO.getDateFin();

        Assert.isNotNull("dateDebut", dateDebut);
        Assert.isNotNull("dateFin", dateFin);
        Assert.isNotNull("idEtablissement", idEtablissement);

        final ResultatDTO<List<SeanceDTO>> resultat =
            new ResultatDTO<List<SeanceDTO>>();

        final List<Map<String, ?>> resultatQuery;
        if (idEnseignant != null){
            resultatQuery = requeteFindSeanceForPlanningEnseignant(
                idEtablissement, idEnseignant, dateDebut, dateFin);
        } else {
            Assert.isNotNull("classes", rechercheSeanceQO.getListeClasseBean());
            Assert.isNotNull("groupes", rechercheSeanceQO.getListeGroupeBean());
            resultatQuery = requeteFindSeanceForPlanningEleve(
                    idEtablissement, rechercheSeanceQO.getListeClasseBean(), rechercheSeanceQO.getListeGroupeBean(), dateDebut, dateFin);
        }

        //Recherche les libelles court des enseignements
        final Map<Integer, String> mapLibellePersoEnseignement = findLibelleCourtEnseignement(
                idEtablissement, resultatQuery);

        
        
        
        //Pour faire le pseudo lien avec emploi de temps, il nous faut le type de semaine et la période
        //pour trouver l'emploi de temps qui correspond....
        final String alternanceSemaine =
                etablissementHibernateBusinessService.findAlternanceSemaine(idEtablissement);
        
        
        
        
        String sql = "";
        
        try { 
            Reader reader = new BufferedReader(new InputStreamReader(
                    getClass().getResourceAsStream(
                        "/org/crlr/metier/business/seanceEmploiLien.sql")));
            
            sql = org.apache.commons.io.IOUtils.toString(reader);
        } catch (Exception ex) {
            log.warn("ex", ex);
            throw new MetierException("Erreur interne");
        }
        

        //Transforme les informations en une liste utilisable.
        final List<SeanceDTO> listeSeanceDTO =
            new ArrayList<SeanceDTO>();

        for (final Map<String, ?> result : resultatQuery) {
            final SeanceDTO seanceDTO =
                new SeanceDTO();
            seanceDTO.setId((Integer) result.get("idSeance"));
            seanceDTO.setCode((String) result.get("codeSeance"));
            seanceDTO.setIntitule((String) result.get("intituleSeance"));
            seanceDTO.setDate((Date) result.get("dateSeance"));
            seanceDTO.setHeureDebut((Integer) result.get("heureDebut"));
            seanceDTO.setMinuteDebut((Integer) result.get("minuteDebut"));
            seanceDTO.setHeureFin((Integer) result.get("heureFin"));
            seanceDTO.setMinuteFin((Integer) result.get("minuteFin"));
            seanceDTO.setDescription((String) result.get("descriptionSeance"));
            seanceDTO.setIdEnseignant((Integer) result.get("idEnseignantSea"));
            seanceDTO.getSequenceDTO().setIdEnseignant((Integer) result.get("idEnseignantSeq"));
            seanceDTO.getSequenceDTO().setIdEtablissement((Integer) result.get("idEtablissement"));
            
            seanceDTO.getSequence().setCode((String) result.get("codeSequence"));
            
            GroupesClassesDTO groupeClasse = new GroupesClassesDTO();
            
            
            if (result.get("codeClasse") != null) {
                groupeClasse.setId((Integer) result.get("idClasse"));
                groupeClasse.setDesignation((String) result.get("designationClasse"));
                groupeClasse.setTypeGroupe(TypeGroupe.CLASSE);                
            } else {
                groupeClasse.setId((Integer) result.get("idGroupe"));
                groupeClasse.setDesignation((String) result.get("designationGroupe"));
                groupeClasse.setTypeGroupe(TypeGroupe.GROUPE);
            }
            
            seanceDTO.getSequenceDTO().setGroupesClassesDTO(groupeClasse);

            final Integer idEnseignement = (Integer) result.get("idEnseignement");
            if (mapLibellePersoEnseignement.containsKey(idEnseignement)) {
                seanceDTO.setMatiere((String) mapLibellePersoEnseignement.get(idEnseignement));
            } else {
                seanceDTO.setMatiere((String) result.get("designationEnseignement"));
            }

            seanceDTO.setEnseignantDTO(new EnseignantDTO(
            (String) result.get("civilite"),
            "",
            (String) result.get("nom")));
            seanceDTO.getEnseignantDTO().setId((Integer) result.get("idEnseignant"));
                                           
            
            
            final TypeJour jour = 
                    TypeJour.getTypeJour( DateUtils.getChamp(seanceDTO.getDate(), Calendar.DAY_OF_WEEK) );
            
            
            ///Trouver un emploi de temps eventuel qui correspond
            
                
            TypeSemaine typeSemaine =
                    GenerateurDTO.findTypeSemaineParDate(seanceDTO.getDate(), alternanceSemaine);
            
            if(typeSemaine == null){
                typeSemaine = TypeSemaine.PAIR;
            }
                
            @SuppressWarnings("unchecked")
            List<Object> listEmpTemps = getEntityManager().createNativeQuery(sql)
                    .setParameter(1, seanceDTO.getId())
                    .setParameter(2, typeSemaine.getValeur())
                    .setParameter(3, jour.getJourName())                    
                    .getResultList();
            
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(listEmpTemps)) {
                Object[] empResult = (Object[]) listEmpTemps.get(0); 
                //Minimum nécesaire pour type couleur et déscription de case
                DetailJourEmploiDTO emploi = new DetailJourEmploiDTO();
                emploi.setTypeCouleur(TypeCouleur.getTypeCouleurById((String) empResult[0]));
                if (seanceDTO.getIdClasse() != null) {
                    emploi.getGroupeOuClasse().setTypeGroupe(TypeGroupe.CLASSE);
                    emploi.getGroupeOuClasse().setIntitule(seanceDTO.getDesignationClasse());
                } else {
                    emploi.getGroupeOuClasse().setTypeGroupe(TypeGroupe.GROUPE);
                    emploi.getGroupeOuClasse().setIntitule(seanceDTO.getDesignationClasse());
                }
                emploi.getMatiere().setLibellePerso((String) empResult[1]);
                emploi.getMatiere().setIntitule((String) empResult[2]);
                emploi.setCodeSalle((String) empResult[3]);
                emploi.setDescription((String) empResult[4]);
                
                TypeCouleur couleurSeance = seanceDTO.getTypeCouleur();
                if(couleurSeance == null || couleurSeance == TypeCouleur.Blanc) {
                        seanceDTO.setTypeCouleur(emploi.getTypeCouleur());
                }
                seanceDTO.setEmploiDeTempsDescription(
                        StringUtils.trimToEmpty(emploi.getCaseDescription()).replaceAll("\\n", "<br/>"));
            }
            
            listeSeanceDTO.add(seanceDTO);
            
        }

        if (listeSeanceDTO.size() == 0) {
            conteneurMessage.add(new Message(TypeReglesSeance.SEANCE_19.name(),
                    Nature.INFORMATIF));
            throw new MetierException(conteneurMessage,
                    "Il n'existe aucune séance pour votre recherche.");
        }

        resultat.setValeurDTO(listeSeanceDTO);

        return resultat;
    }

    /**
     * {@inheritDoc}
     */
    public Map<Integer, TypeSemaine> findAlternanceSemaines(
            final Integer idEtablissement) {
        final String queryAlternanceSemaine =
            "SELECT  E.alternanceSemaine" +
            " FROM " + EtablissementBean.class.getName() + " E " +
            " WHERE E.id= :idEtablissement";
        final String alternanceSemaine = 
            (String) getEntityManager().createQuery(queryAlternanceSemaine).setParameter("idEtablissement", idEtablissement).getSingleResult();
        final Map<Integer, TypeSemaine> mapAlternance;
        if (!StringUtils.isEmpty(alternanceSemaine)){
            mapAlternance = GenerateurDTO.generateAlternanceSemaineFromDB(alternanceSemaine);
        } else {
            mapAlternance = new HashMap<Integer, TypeSemaine>();
        }
        return mapAlternance;
    }

    
    

    /**
     * Recherche les libelles courts des enseignements pour le résultat d'une requete.
     * @param idEtablissement l'id de l'établissement.
     * @param resultatQuery le resultat de la requete précédente.
     * @return la map qui associe le libelle court à l'id de l'enseignement.
     */
    @SuppressWarnings("unchecked")
    private Map<Integer, String> findLibelleCourtEnseignement(
            final Integer idEtablissement,
            final List<Map<String, ?>> resultatQuery) {
        final Map<Integer, Integer> mapEnseignement =
            org.crlr.utils.CollectionUtils.formatMapClefValeur(resultatQuery, "idEnseignement",
            "idEnseignement");

        final Map<Integer, String> mapLibellePersoEnseignement;
        if (!mapEnseignement.isEmpty()) {
            final String queryLibellePerso =
                "SELECT new map(L.pk.idEnseignement AS idEnseignement, L.libelle AS designation)" +
                " FROM " + EnseignementLibelleBean.class.getName() +
                " L WHERE L.pk.idEtablissement = :idEtablissement AND L.pk.idEnseignement IN (:idEnseignements)";

            final List<Map<String, ?>> resultatLibelleQuery =
                getEntityManager().createQuery(queryLibellePerso)
                .setParameter("idEtablissement", idEtablissement)
                .setParameter("idEnseignements", mapEnseignement.keySet())
                .getResultList();

            mapLibellePersoEnseignement = org.crlr.utils.CollectionUtils.formatMapClefValeur(resultatLibelleQuery,
                    "idEnseignement",
            "designation");
        } else {
            mapLibellePersoEnseignement = new HashMap<Integer, String>();
        }
        return mapLibellePersoEnseignement;
    }
    
    /**
     * Le debut de la requete pour le planning avec tous les champs indispensables.
     * @return le debut de la requete.
     */
    private static String debutRequeteFindSeanceForPlanning = 
            "SELECT " + " new map(" + 
            "SEA.id as idSeance, SEA.idEnseignant as idEnseignant, " +
            "SEA.code as codeSeance, " + 
            "SEA.heureDebut as heureDebut, " +
            "SEA.minuteDebut as minuteDebut, " +
            "SEA.heureFin as heureFin, " +
            "SEA.minuteFin as minuteFin, " +
            "SEA.intitule as intituleSeance, " + 
            "SEA.date as dateSeance, " +
            "SEA.description as descriptionSeance, " +
            " SEA.idEnseignant as idEnseignantSea, " +
            " SEQ.idEnseignant as idEnseignantSeq, " +
            " SEQ.idEtablissement as idEtablissement, " +
            "SEQ.code as codeSequence, " +
            "GRP.code as codeGroupe, " + 
            "GRP.id as idGroupe, " + 
            "GRP.designation as designationGroupe, " + 
            "CLA.code as codeClasse, " +
            "CLA.id as idClasse, " + 
            "CLA.designation as designationClasse, " + 
            "ENS.id as idEnseignement, " +
            "ENS.designation as designationEnseignement " 
            ;
        
    

    /**
     * Requete de recherche des seances pour le planning d'un enseignant.
     * @param idEtablissement l'id de l'etablissement.
     * @param idEnseignant l'id de l'enseignant.
     * @param dateDebut la date de debut du planning.
     * @param dateFin la date de fin du planning.
     * @return le resultat de la requete.
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, ?>> requeteFindSeanceForPlanningEnseignant(
            final Integer idEtablissement, final Integer idEnseignant,
            final Date dateDebut, final Date dateFin) {
        final String requete =
                debutRequeteFindSeanceForPlanning + ")" + " FROM " +
            SequenceBean.class.getName() + " SEQ " +
            " INNER JOIN SEQ.enseignement ENS " + 
            " LEFT JOIN SEQ.classe CLA " + " LEFT JOIN SEQ.groupe GRP, " +
            SeanceBean.class.getName() + " SEA " + " WHERE " +
            " SEA.idSequence = SEQ.id AND " + 
            " SEQ.enseignant.id = :idEnseignant AND " +
            " ( CLA.idEtablissement = :idEtab  OR "+
            " GRP.idEtablissement = :idEtab ) AND " +
            " SEA.date >= :dateDebut AND SEA.date <= :dateFin " +
            " ORDER BY SEA.date ASC, SEA.heureDebut ASC, SEA.minuteDebut ASC";
        
        final Query query = getEntityManager().createQuery(requete);

        query.setParameter("idEnseignant", idEnseignant);
        query.setParameter("idEtab", idEtablissement);
        query.setParameter("dateDebut", dateDebut);
        query.setParameter("dateFin", dateFin);

        return query.getResultList();
    }
    
    /**
     * Requete de recherche des seances pour le planning d'un élève.
     * @param idEtablissement l'id de l'etablissement.
     * @param classes les classes de l'élève.
     * @param groupes les groupes de l'élève.
     * @param dateDebut la date de debut du planning.
     * @param dateFin la date de fin du planning.
     * @return le resultat de la requete.
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, ?>> requeteFindSeanceForPlanningEleve(
            final Integer idEtablissement, final List<ClasseBean> classes,
            final List<GroupeBean> groupes,
            final Date dateDebut, final Date dateFin) {
        
        String requete =
            debutRequeteFindSeanceForPlanning +", "+
            "PROF.civilite as civilite, PROF.nom as nom)" + " FROM " +
            SequenceBean.class.getName() + " SEQ " +
            " INNER JOIN SEQ.enseignement ENS " + 
            " INNER JOIN SEQ.enseignant PROF " +
            " LEFT JOIN SEQ.classe CLA " + " LEFT JOIN SEQ.groupe GRP, " +
            SeanceBean.class.getName() + " SEA " +
            " WHERE " +
            " SEA.idSequence = SEQ.id ";

        if (! org.apache.commons.collections.CollectionUtils.isEmpty(classes)){
            if (! org.apache.commons.collections.CollectionUtils.isEmpty(groupes)){
                //Des classes et des groupes 
                requete += 
                    " AND ( ( CLA.idEtablissement = :idEtab AND CLA in (:classes)) OR ( GRP.idEtablissement = :idEtab AND GRP in (:groupes))) ";
            } else {
                //Des classes uniquement
                requete += " AND CLA.idEtablissement = :idEtab AND CLA in (:classes)  ";
            }
        } else if (! org.apache.commons.collections.CollectionUtils.isEmpty(groupes)){
            //Des groupes uniquement 
            requete += " AND GRP.idEtablissement = :idEtab AND GRP in (:groupes) ";
        } else {
            //Impossible en theorie
            throw new RuntimeException("Pas de classe ou de groupe pour l'élève");
        }
        
        requete += " AND " +
        " SEA.date >= :dateDebut AND SEA.date <= :dateFin AND SEA.date <= :today " +
        " ORDER BY SEA.date ASC, SEA.heureDebut ASC, SEA.minuteDebut ASC";

        final Query query = getEntityManager().createQuery(requete);

        if (! org.apache.commons.collections.CollectionUtils.isEmpty(classes)){
            query.setParameter("classes", classes);
        }
        if (! org.apache.commons.collections.CollectionUtils.isEmpty(groupes)){
            query.setParameter("groupes", groupes);
        }
        
        query.setParameter("idEtab", idEtablissement);
        query.setParameter("dateDebut", dateDebut);
        query.setParameter("dateFin", dateFin);
        query.setParameter("today", DateUtils.getAujourdhui());

        return query.getResultList();
    }

    /**
     * Regle SEANCE_00 La date de fin de pèriode doit être postèrieure ou égale à
     * la date de début.
     *
     * @param dateDebut date de début
     * @param dateFin date de fin
     *
     * @return true si tout est ok, sinon false
     */
    private boolean checkSEANCE00(Date dateDebut, Date dateFin) {
        boolean checkResult = true;
        if ((dateDebut != null) && (dateFin != null)) {
            if (DateUtils.compare(dateDebut, dateFin, false) > 0) {
                checkResult = false;
            }
        }
        return checkResult;
    }

    /**
     * Regle SEANCE_01 Vous devez saisir la date de début de pèriode.
     *
     * @param dateDebut date de début
     * @param dateFin date de fin
     *
     * @return true si tout est ok, sinon false
     */
    private boolean checkSEANCE01(Date dateDebut, Date dateFin) {
        boolean checkResult = true;
        if ((dateDebut == null) && (dateFin != null)) {
            checkResult = false;
        }
        return checkResult;
    }

    /**
     * {@inheritDoc}
     */
    public Integer saveSeance(SeanceDTO saveSeanceQO){
        Assert.isNotNull("saveSeanceQO", saveSeanceQO);
        final Integer idSequence = saveSeanceQO.getSequenceDTO().getId();
        final Date date = saveSeanceQO.getDate();
        final String description = saveSeanceQO.getDescription();
        final String annotations = saveSeanceQO.getAnnotations();
        final Integer heureDebut = saveSeanceQO.getHeureDebut();
        final Integer minuteDebut = saveSeanceQO.getMinuteDebut();
        final Integer heureFin = saveSeanceQO.getHeureFin();
        final Integer minuteFin = saveSeanceQO.getMinuteFin();
        final String intitule = saveSeanceQO.getIntitule();
        final Integer idEnseignant = saveSeanceQO.getIdEnseignant();

        SeanceBean seanceBean = null;
        Integer idSeance = saveSeanceQO.getId();
        
        if (idSeance != null) {
            //Modif
            seanceBean = getEntityManager().find(SeanceBean.class, idSeance);
        } else {
            //Ajoute
            seanceBean = new SeanceBean();
        }
        
        seanceBean.setDate(date);
        seanceBean.setDescription(description);
        seanceBean.setAnnotations(annotations);
        seanceBean.setHeureDebut(heureDebut);
        seanceBean.setMinuteDebut(minuteDebut);
        seanceBean.setHeureFin(heureFin);
        seanceBean.setMinuteFin(minuteFin);
        seanceBean.setIdEnseignant(idEnseignant);
        seanceBean.setIdSequence(idSequence);
        seanceBean.setIntitule(intitule);
        seanceBean.setDateMaj(saveSeanceQO.getDateMaj());
        
      //Si l'id de la séance est null c'est qu'on est en ajout.
        if (idSeance == null) {
            final BaseHibernateBusiness baseHibernateBusiness =
                new BaseHibernateBusiness(this.getEntityManager());
            idSeance = baseHibernateBusiness.getIdInsertion("cahier_seance");
            

            seanceBean.setId(idSeance);
            seanceBean.setCode("SEA" + idSeance);

            // Ajout à la base de données
            getEntityManager().persist(seanceBean);
            
        }         
        
        getEntityManager().flush();

        return seanceBean.getId();
    }

    /**
     * {@inheritDoc}
     */
    public void deleteSeance(SeanceDTO resultatRechercheSeanceDTO) {
        final Integer idSeance = resultatRechercheSeanceDTO.getId();

        final String query = "DELETE FROM " + SeanceBean.class.getName()
                + " s WHERE s.id = :id";

        @SuppressWarnings("unused")
        final Integer liste = getEntityManager().createQuery(query)
                .setParameter("id", idSeance).executeUpdate();

    }

    /**
     * {@inheritDoc}
     */
    public void deleteListeSeances(final List<SeanceDTO> listeSeances){
        Assert.isNotNull("listeSeances", listeSeances);

        final List<Integer> listeId = new ArrayList<Integer>();

        for (SeanceDTO seance : listeSeances) {
            listeId.add(seance.getId());
        }

        if (!org.apache.commons.collections.CollectionUtils.isEmpty(listeId)) {
            final String query =
                "DELETE FROM " + SeanceBean.class.getName() +
                " s WHERE s.id IN (:listeId)";

            getEntityManager().createQuery(query).setParameter("listeId", listeId)
                .executeUpdate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<FileUploadDTO> trouverPieceJointeDevoir(final Integer idDevoir,
                                                     final Boolean archive,
                                                     final boolean isVisaArchive,
                                                     final String exercice){
        final List<FileUploadDTO> listePieceJointeDTO = new ArrayList<FileUploadDTO>();

        final String schema = SchemaUtils.getSchemaCourantOuArchive(archive, exercice);
        
        final String pjDevoirTableNom = isVisaArchive ? "cahier_archive_piece_jointe_devoir" : "cahier_piece_jointe_devoir";
        final String idDevoirChamp = isVisaArchive ? "id_archive_devoir" : "id_devoir";

        final String query =
            "SELECT PJ.id, PJ.nom_fichier, PJ.uid, PJ.chemin FROM " +
            SchemaUtils.getTableAvecSchema(schema, pjDevoirTableNom) +
            " PJD " + " INNER JOIN " +
            SchemaUtils.getTableAvecSchema(schema, "cahier_piece_jointe") +
            " PJ ON (PJ.id=PJD.id_piece_jointe) " +
            " WHERE PJD." + idDevoirChamp + " =? ORDER BY PJ.code ASC";

        final List<Object[]> listePieceJointe =
            getEntityManager().createNativeQuery(query).setParameter(1, idDevoir)
                .getResultList();

        for (final Object[] pieceJointe : listePieceJointe) {
            final FileUploadDTO pieceJointeDTO = new FileUploadDTO();
            pieceJointeDTO.setId((Integer) pieceJointe[0]);
            pieceJointeDTO.setEnBase(true);
            pieceJointeDTO.setNom((String) pieceJointe[1]);
            pieceJointeDTO.setUid((String) pieceJointe[2]);
            pieceJointeDTO.setPathDB((String) pieceJointe[3]);

            //FIXME Dénormalisation Interdit dans le métier attention si séparation des couches
            FacesUtils.pathUploadFile(pieceJointeDTO);
            if (FileUploadUtils.checkExistencePieceJointe(pieceJointeDTO)) {
                pieceJointeDTO.setActiverLien(true);
            }
            
            listePieceJointeDTO.add(pieceJointeDTO);
        }

        return listePieceJointeDTO;
    }

    /**
     * @param isVisaArchive si vrai, idSeance = id_archive_seance
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<DevoirDTO>> trouverDevoir(final Integer idSeance,
                                                   final Boolean isSchemaArchive,
                                                   final boolean isVisaArchive,
                                                   final String exercice) {
        final ResultatDTO<List<DevoirDTO>> resultat = new ResultatDTO<List<DevoirDTO>>();

        final String schema = SchemaUtils.getSchemaCourantOuArchive(isSchemaArchive, exercice);

        final String idSeanceChamp = isVisaArchive ? "id_archive_seance" : "id_seance";
        final String devoirTableNom = isVisaArchive ? "cahier_archive_devoir" : "cahier_devoir";
        final String devoirIdChamp = isVisaArchive ? "id_archive_devoir" : "id";
        
        final String query =
            "SELECT D." + devoirIdChamp + ", D.code, D.date_remise, " + " D.intitule, D.description, " +
            " D." + idSeanceChamp + ", D.id_type_devoir, " + " TP.libelle, TP.categorie" + " FROM " +
            SchemaUtils.getTableAvecSchema(schema, devoirTableNom) + " D LEFT JOIN " +
            SchemaUtils.getTableAvecSchema(schema, "cahier_type_devoir") +
            " TP ON (TP.id=D.id_type_devoir)" + " WHERE " + " D." + idSeanceChamp +" =? " +
            " ORDER BY D.code ASC";

        final List<Object[]> resultatQuery =
            getEntityManager().createNativeQuery(query).setParameter(1, idSeance)
                .getResultList();

        List<DevoirDTO> listeDevoirDTO = new ArrayList<DevoirDTO>();
        for (final Object[] result : resultatQuery) {
            final DevoirDTO devoirDTO = new DevoirDTO();
            devoirDTO.setId((Integer) result[0]);
            devoirDTO.setCode((String) result[1]);
            devoirDTO.setDateRemise((Date) result[2]);
            devoirDTO.setIntitule((String) result[3]);
            devoirDTO.setDescription((String) result[4]);
            devoirDTO.setIdSeance((Integer) result[5]);
            
            devoirDTO.generateDescriptionAbrege();
            
            if (result[8] != null) {
                final TypeDevoirDTO typeDevoir = new TypeDevoirDTO();
                typeDevoir.setCategorie(TypeCategorieTypeDevoir.valueOf((String) result[8]));
                typeDevoir.setLibelle((String) result[7]);
                typeDevoir.setId((Integer) result[6]);
                devoirDTO.setTypeDevoirDTO(typeDevoir);
            } 
            listeDevoirDTO.add(devoirDTO);
        }

        listeDevoirDTO = ComparateurUtils.sort(listeDevoirDTO, "dateRemise");

        resultat.setValeurDTO(listeDevoirDTO);

        return resultat;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<FileUploadDTO>> trouverPieceJointe(final Integer idSeance,
                                                            final Boolean archive,
                                                            final boolean isVisaArchive,
                                                            final String exercice) {
        final ResultatDTO<List<FileUploadDTO>> resultat =
            new ResultatDTO<List<FileUploadDTO>>();

        final String schema = SchemaUtils.getSchemaCourantOuArchive(archive, exercice);
        
        final String pjSeanceTableNom = isVisaArchive ? "cahier_archive_piece_jointe_seance" : "cahier_piece_jointe_seance";
        final String idSeanceChamp = isVisaArchive ? "id_archive_seance" : "id_seance";

        final String query =
            "SELECT pj.id, pj.nom_fichier, pj.uid, pj.chemin FROM " +
            SchemaUtils.getTableAvecSchema(schema, pjSeanceTableNom) +
            " pjs INNER JOIN " +
            SchemaUtils.getTableAvecSchema(schema, "cahier_piece_jointe") +
            " pj ON (pj.id = pjs.id_piece_jointe) " + "WHERE pjs." + idSeanceChamp + " =?" +
            " ORDER BY pj.code ASC";

        final List<Object[]> listePieceJointe =
            getEntityManager().createNativeQuery(query).setParameter(1, idSeance)
                .getResultList();

        final List<FileUploadDTO> listePieceJointeDTO = new ArrayList<FileUploadDTO>();

        for (final Object[] pieceJointe : listePieceJointe) {
            final FileUploadDTO fileUploadDTO = new FileUploadDTO();
            fileUploadDTO.setId((Integer) pieceJointe[0]);
            fileUploadDTO.setEnBase(true);
            fileUploadDTO.setNom((String) pieceJointe[1]);
            fileUploadDTO.setUid((String) pieceJointe[2]);
            fileUploadDTO.setPathDB((String) pieceJointe[3]);
            fileUploadDTO.setActiverLien(true);

            //FIXME Dénormalisation Interdit dans le métier attention si séparation des couches
            FacesUtils.pathUploadFile(fileUploadDTO);
            if (FileUploadUtils.checkExistencePieceJointe(fileUploadDTO)) {
                fileUploadDTO.setActiverLien(true);
            }

            listePieceJointeDTO.add(fileUploadDTO);
        }

        resultat.setValeurDTO(listePieceJointeDTO);

        return resultat;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<SeanceDTO> findSeanceBySequence(ResultatRechercheSequenceDTO resultatRechercheSequenceDTO){
        Assert.isNotNull("resultatRechercheSequenceDTO", resultatRechercheSequenceDTO);

        final List<SeanceDTO> listeSeanceDTO = new ArrayList<SeanceDTO>();
        final Integer idSequence = resultatRechercheSequenceDTO.getId();

        final String query =
            "SELECT SEA FROM " + SeanceBean.class.getName() +
            " SEA WHERE SEA.idSequence = :idSequence " +
            " ORDER BY SEA.code ASC";

        final List<SeanceBean> listeSeanceBean =
            getEntityManager().createQuery(query).setParameter("idSequence", idSequence)
                .getResultList();

        for (final SeanceBean seance : listeSeanceBean) {
            final SeanceDTO seanceDTO = new SeanceDTO();
            seanceDTO.setId(seance.getId());
            seanceDTO.setDate(seance.getDate());
            listeSeanceDTO.add(seanceDTO);
        }

        return listeSeanceDTO;
    }
    
    /**
     * {@inheritDoc}
     */
    public SeanceDTO findSeanceById(ConsulterSeanceQO consulterSeanceQO){

        Assert.isNotNull("consulterSeanceQO", consulterSeanceQO);
        Assert.isNotNull("consulterSeanceQO.idSeance", consulterSeanceQO.getId());
        
        final SeanceDTO seanceDTO;
        if (BooleanUtils.isTrue(consulterSeanceQO.getArchive())) {
            seanceDTO = findSeanceByIdArchive(consulterSeanceQO);
        } else {
            seanceDTO = findSeanceByIdCahierCourant(consulterSeanceQO.getId());
            if (BooleanUtils.isTrue(consulterSeanceQO.getAvecInfoVisa())) {
                findInfoVisa(seanceDTO);
            }
        }
        return seanceDTO;
        
    }

    /**
     * Charge les infos visa poru la seance.
     * @param seanceDTO la seance a completer.
     */
    @SuppressWarnings("unchecked")
    private void findInfoVisa(final SeanceDTO seanceDTO ) {
        final String query =
            "SELECT visa " +
            "FROM " + 
                VisaBean.class.getName() + " visa, " + 
                VisaSeanceBean.class.getName() + " VS " + 
            "WHERE " +
                "VISA.id = VS.pk.idVisa AND " +
                "VS.pk.idSeance = :idSeance " +
            " ORDER BY VISA.profil ASC";

        final List<VisaBean> listeVisaBean = getEntityManager()
            .createQuery(query)
            .setParameter("idSeance", seanceDTO.getId())
            .getResultList();

        for (final VisaBean visa : listeVisaBean) {
            final VisaDTO visaDTO = new VisaDTO();
            
            ObjectUtils.copyProperties(visaDTO, visa);
            if (StringUtils.equals(visa.getProfil(),"ENTDirecteur")) {
                seanceDTO.setVisaDirecteur(visaDTO);
            } else if (StringUtils.equals(visa.getProfil(),"ENTInspecteur")) {
                seanceDTO.setVisaInspecteur(visaDTO);
            }
            visaDTO.calculerEstPerime(seanceDTO.getDateMaj());
        }
    }

    /**
     * Recherche dans le schema cahier_courant la seance dont l'id est passé en parametre.
     * @param idSeance id de la seance 
     * @return une SeanceDTO
     */
    private SeanceDTO findSeanceByIdCahierCourant(final Integer idSeance) {
        SeanceBean seance = getEntityManager().find(SeanceBean.class, idSeance);
        
        SeanceDTO seanceDTO = new SeanceDTO();
        ObjectUtils.copyProperties(seanceDTO,  seance);
        
        SequenceDTO sequenceDTO = new SequenceDTO();
        
        ObjectUtils.copyProperties(sequenceDTO, seance.getSequence());
        
        seanceDTO.setSequenceDTO(sequenceDTO);
        
        return seanceDTO;
    }

    /**
     * Recherche dans le schema archive la seance dont l'id est passé en parametre.
     * @param consulterSeanceQO contient l'id et l exercice
     * @return une SeanceDTO
     */
    @SuppressWarnings("unchecked")
    private SeanceDTO findSeanceByIdArchive(final ConsulterSeanceQO consulterSeanceQO) {
        final SeanceDTO seanceDTO = new SeanceDTO();
        final String schema;
        schema = Archive.PREFIX_SCHEMA_ARCHIVE + consulterSeanceQO.getExercice();

        final List<Object[]> resultatQuery = new ArrayList<Object[]>();

        String nativeRequete = 
            "SELECT " +
            "  sea.id ,  " +
            "  sea.code, " +
            "  sea.intitule, " +
            "  sea.date, " +
            "  sea.heure_debut, " +
            "  sea.heure_fin, " +
            "  sea.minute_debut , " +
            "  sea.minute_fin, " +
            "  sea.description, " +
            "  sea.id_sequence, " +
            "  sea.id_enseignant, " +
            "  sea.annotations, " +
            //"  sea.date_maj, " +
            
            "  seq.id  seq_id, " +
            "  seq.code seq_code, " +
            "  seq.intitule seq_intitule, " +
            "  seq.description seq_description, " +
            "  seq.date_debut seq_date_debut, " +
            "  seq.date_fin seq_date_fin, " +
            "  seq.id_enseignant seq_id_enseignant, " +
            "  seq.id_etablissement seq_id_etablissement, " +
            "  seq.id_enseignement seq_id_enseignement, " +
            "  seq.id_groupe seq_id_groupe, " +
            "  seq.id_classe seq_id_classe " +
            "FROM " + 
                SchemaUtils.getTableAvecSchema(schema, "cahier_seance") + " sea " +
                "inner join " + SchemaUtils.getTableAvecSchema(schema, "cahier_sequence") + " seq on (sea.id_sequence = seq.id) " +
            "WHERE " + 
            "  sea.id = ?";

            final Query query = getEntityManager().createNativeQuery(nativeRequete);
            query.setParameter(1, consulterSeanceQO.getId());
            resultatQuery.addAll(query.getResultList());
            log.debug("id seance = {} ", consulterSeanceQO.getId());
            
        for (final Object[] result : resultatQuery) {
        	log.debug("9 &  12  , 18 = {} {} {} ", result[9], result[12], result[18]);
            seanceDTO.setId((Integer) result[0]);
            seanceDTO.setCode((String) result[1]);
            seanceDTO.setIntitule((String) result[2]);
            seanceDTO.setDate((Date) result[3]);
            seanceDTO.setHeureDebut((Integer) result[4]);
            seanceDTO.setHeureFin((Integer) result[5]);
            seanceDTO.setMinuteDebut((Integer) result[6]);
            seanceDTO.setMinuteFin((Integer) result[7]);
            seanceDTO.setDescription((String) result[8]);
            //  seanceDTO.setIdSquence((Integer) result[9]); 
            seanceDTO.setIdEnseignant((Integer) result[10]);
            seanceDTO.setAnnotations((String) result[11]);
            //seanceDTO.setDateMaj((Date) result[12]);
            
            seanceDTO.getSequenceDTO().setId((Integer) result[12]);
            seanceDTO.getSequenceDTO().setCode((String) result[13]);
            seanceDTO.getSequenceDTO().setIntitule((String) result[14]);
            seanceDTO.getSequenceDTO().setDescription((String) result[15]);
            seanceDTO.getSequenceDTO().setDateDebut((Date) result[16]);
            seanceDTO.getSequenceDTO().setDateFin((Date) result[17]);
            seanceDTO.getSequenceDTO().setIdEnseignant((Integer) result[18]);
            //seanceDTO.getSequenceDTO().setIdEtablissement((Integer) result[19]);
            seanceDTO.getSequenceDTO().setIdEnseignement((Integer) result[20]);
            if (result[21] != null) {
                seanceDTO.getSequenceDTO().getGroupesClassesDTO().setTypeGroupe(TypeGroupe.GROUPE);
                seanceDTO.getSequenceDTO().getGroupesClassesDTO().setId((Integer) result[21]);
            } else {
                seanceDTO.getSequenceDTO().getGroupesClassesDTO().setTypeGroupe(TypeGroupe.CLASSE);
                seanceDTO.getSequenceDTO().getGroupesClassesDTO().setId((Integer) result[22]);
            }
            break;
        }
        return seanceDTO;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean checkUnicite(SeanceDTO saveSeanceQO){
        Assert.isNotNull("saveSequenceQO", saveSeanceQO);

        //final String mode = saveSeanceQO.getMode();

        boolean checkResult = false;

        final Integer idEnseignement = saveSeanceQO.getSequenceDTO().getIdEnseignement();
        final Integer idEnseignant = saveSeanceQO.getIdEnseignant();

        //final String intitule = saveSeanceQO.getIntitule();
        final Date date = saveSeanceQO.getDate();
        final Integer idSequence = saveSeanceQO.getSequenceDTO().getId();
        final String query =
            "SELECT DISTINCT new Map(SEA.id as id, SEA.heureDebut as hd, " +
            "SEA.heureFin as hf, SEA.minuteDebut as md, SEA.minuteFin as mf) FROM " +
            SequenceBean.class.getName() + " SEQ, " +
            SeanceBean.class.getName() + " SEA " +
            " WHERE SEQ.id = :idSequence AND SEQ.id = SEA.idSequence AND" +
            " SEQ.idEnseignement = :idEnseignement AND " +
            " SEQ.idEnseignant = :idEnseignant AND " + // " SEA.intitule = :intitule AND " + 
            " SEA.date = :date ";

        final List<Map<String, ?>> resultatQuery =
            getEntityManager().createQuery(query)
                .setParameter("idEnseignement", idEnseignement)
                .setParameter("idEnseignant", idEnseignant)
                .setParameter("idSequence", idSequence).setParameter("date", date)
                .getResultList();

        if (resultatQuery.size() == 0) {
            checkResult = true;
        } else {
            final Integer annee = DateUtils.getYear(date);
            final Integer mois = DateUtils.getChamp(date, Calendar.MONTH);
            final Integer jour = DateUtils.getChamp(date, Calendar.DATE);

            final Date dateNouvelleDeb =
                DateUtils.creer(annee, mois, jour, saveSeanceQO.getHeureDebut(),
                                saveSeanceQO.getMinuteDebut(), 0);

            final Date dateNouvelleFin =
                DateUtils.creer(annee, mois, jour, saveSeanceQO.getHeureFin(),
                                saveSeanceQO.getMinuteFin(), 0);

            int cptEchec = 0;

            //contrôle des chevauchements des plages horaires.
            for (final Map<String, ?> map : resultatQuery) {
                final Integer idSeance = (Integer) map.get("id");
                if (idSeance.equals(saveSeanceQO.getId())) {
                    continue;
                }
                        
                final Date dateDebutEx =
                    DateUtils.creer(annee, mois, jour, (Integer) map.get("hd"),
                                    (Integer) map.get("md"), 0);
                final Date dateFinEx =
                    DateUtils.creer(annee, mois, jour, (Integer) map.get("hf"),
                                    (Integer) map.get("mf"), 0);

                if (DateUtils.isBetweenDateMaxNonCompris(dateNouvelleDeb,
                                                             dateDebutEx, dateFinEx)) {
                    cptEchec++;
                } else if (DateUtils.isBetweenDateMinNonCompris(dateNouvelleFin,
                                                                    dateDebutEx,
                                                                    dateFinEx)) {
                    cptEchec++;
                } else if (DateUtils.isBetweenDateMaxNonCompris(dateDebutEx,
                                                                    dateNouvelleDeb,
                                                                    dateNouvelleFin) ||
                               DateUtils.isBetweenDateMinNonCompris(dateFinEx,
                                                                        dateNouvelleDeb,
                                                                        dateNouvelleFin)) {
                    cptEchec++;
                }
            
            }

            if (cptEchec == 0) {
                checkResult = true;
            }
        }

        return checkResult;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<ResultatRechercheSeanceDTO>> listeSeanceAffichage(RechercheSeanceQO rechercheSeanceQO){
        final Integer idEnseignant = rechercheSeanceQO.getIdEnseignant();
        final Integer idEleve = rechercheSeanceQO.getIdEleve();
        final Integer idInspecteur = rechercheSeanceQO.getIdInspecteur();

        if (idInspecteur != null) {
            return listeSeanceAffichageInspecteur(rechercheSeanceQO);
        } else if (idEnseignant != null) {
            return listeSeanceAffichageEnseignant(rechercheSeanceQO);
        } else if (idEleve != null) {
            return listeSeanceAffichageEleve(rechercheSeanceQO);
        } else {
            return listeSeanceAffichageDirection(rechercheSeanceQO);
        }
    }

    /**
     * Recherche des séances pour un inspecteur.
     *
     * @param rechercheSeanceQO rechercheSeanceQO
     *
     * @return La liste des séances
     */
    @SuppressWarnings("unchecked")
    private ResultatDTO<List<ResultatRechercheSeanceDTO>> listeSeanceAffichageInspecteur(RechercheSeanceQO rechercheSeanceQO) {
        final ResultatDTO<List<ResultatRechercheSeanceDTO>> resultat =
            new ResultatDTO<List<ResultatRechercheSeanceDTO>>();
        final List<ResultatRechercheSeanceDTO> listeResultatRechercheSeanceDTO =
            new ArrayList<ResultatRechercheSeanceDTO>();

        final Integer idInspecteur = rechercheSeanceQO.getIdInspecteur();
        final TypeGroupe typeGroupeClasse = rechercheSeanceQO.getTypeGroupeSelectionne();
        Integer idGroupe = null;
        Integer idClasse = null;

        String clauseEnseignement = "";
        if (rechercheSeanceQO.getIdEnseignement() != null) {
            clauseEnseignement = " SEQ.idEnseignement=" + rechercheSeanceQO.getIdEnseignement() + " AND ";
        }
        
        String requete1 = "";
        String requete2 = "";
        
        final String requeteWhereCommun =
                " INS.id = :idInspecteur AND " +
                " OUV.enseignant.id = ENSEI.id AND " +
                " ENSEI.id = SEA.idEnseignant AND " + clauseEnseignement +
                " SEQ.id = SEA.idSequence AND " +
                " SEA.date <= :jourCourant AND " +
                " SEA.date >= :premierJourSemaine AND " +
                " SEA.date <= :dernierJourSemaine ";
                
        final List<Map<String, ?>> resultatQuery = new ArrayList<Map<String, ?>>();

        if (typeGroupeClasse.equals(TypeGroupe.CLASSE)) {
            //CLASSE

            //Liste des groupes selectionnés
            final List<GroupeDTO> listeGroupeDTO = rechercheSeanceQO.getListeGroupeDTO();
            String listeGroupe = "(0";
            for (final GroupeDTO groupe : listeGroupeDTO) {
                listeGroupe += (", " + groupe.getId());
            }
            listeGroupe += ")";

            idClasse = rechercheSeanceQO.getGroupeClasseSelectionne().getId();
            
            requete1 = "SELECT " + " distinct new map(" + " CLA.code as codeClasse, " +
                       " CLA.designation as designationClasse, " +
                       LISTE_SEANCE_AFFICHAGE_CHAMPS_SELECT_COMMON +
                       ") FROM " + EnseignantsClassesBean.class.getName() + " EC " +
                       " INNER JOIN EC.classe CLA " +
                       " INNER JOIN EC.enseignant ENSEI, " +
                       SequenceBean.class.getName() + " SEQ " +
                       " INNER JOIN SEQ.enseignement ENS, " + SeanceBean.class.getName() +
                       " SEA, " + OuvertureInspecteurBean.class.getName() +
                       " OUV INNER JOIN OUV.inspecteur INS " + " WHERE " +
                       " CLA.id = SEQ.idClasse AND " + 
                       " CLA.id = :idClasse AND " +
                       requeteWhereCommun;

            requete2 = "SELECT " + " distinct new map(" + " GRP.code as codeGroupe, " +
                       " GRP.designation as designationGroupe, " + 
                       LISTE_SEANCE_AFFICHAGE_CHAMPS_SELECT_COMMON +
                       ") FROM " + EnseignantsGroupesBean.class.getName() + " EG " +
                       " INNER JOIN EG.groupe GRP " +
                       " INNER JOIN EG.enseignant ENSEI, " +
                       SequenceBean.class.getName() + " SEQ " +
                       " INNER JOIN SEQ.enseignement ENS, " + SeanceBean.class.getName() +
                       " SEA, " + OuvertureInspecteurBean.class.getName() +
                       " OUV INNER JOIN OUV.inspecteur INS " + " WHERE " +
                       " GRP.id = SEQ.idGroupe AND " + 
                       " GRP.id IN " + listeGroupe + " AND " + 
                       requeteWhereCommun;

            final Query query1 = getEntityManager().createQuery(requete1);
            final Query query2 = getEntityManager().createQuery(requete2);

            query1.setParameter("idClasse", idClasse);
            query1.setParameter("idInspecteur", idInspecteur);
            query1.setParameter("premierJourSemaine",
                                rechercheSeanceQO.getPremierJourSemaine());
            query1.setParameter("dernierJourSemaine",
                                rechercheSeanceQO.getDernierJourSemaine());
            query1.setParameter("jourCourant", rechercheSeanceQO.getJourCourant());

            query2.setParameter("idInspecteur", idInspecteur);
            query2.setParameter("premierJourSemaine",
                                rechercheSeanceQO.getPremierJourSemaine());
            query2.setParameter("dernierJourSemaine",
                                rechercheSeanceQO.getDernierJourSemaine());
            query2.setParameter("jourCourant", rechercheSeanceQO.getJourCourant());

            resultatQuery.addAll(query1.getResultList());
            resultatQuery.addAll(query2.getResultList());
        } else if (typeGroupeClasse.equals(TypeGroupe.GROUPE)) {
            //GROUPE
            idGroupe = rechercheSeanceQO.getGroupeClasseSelectionne().getId();

            requete1 = "SELECT " + " distinct new map(" + " GRP.code as codeGroupe, " +
                       " GRP.designation as designationGroupe, " + 
                       LISTE_SEANCE_AFFICHAGE_CHAMPS_SELECT_COMMON +
                       ") FROM " + EnseignantsGroupesBean.class.getName() + " EG " +
                       " INNER JOIN EG.groupe GRP " +
                       " INNER JOIN EG.enseignant ENSEI, " +
                       SequenceBean.class.getName() + " SEQ " +
                       " INNER JOIN SEQ.enseignement ENS, " + SeanceBean.class.getName() +
                       " SEA, " + OuvertureInspecteurBean.class.getName() +
                       " OUV INNER JOIN OUV.inspecteur INS " + " WHERE " +
                       " GRP.id = SEQ.idGroupe AND " +
                       " GRP.id = :idGroupe AND " +
                       requeteWhereCommun ;

            final Query query1 = getEntityManager().createQuery(requete1);

            query1.setParameter("idGroupe", idGroupe);
            query1.setParameter("idInspecteur", idInspecteur);
            query1.setParameter("premierJourSemaine",
                                rechercheSeanceQO.getPremierJourSemaine());
            query1.setParameter("dernierJourSemaine",
                                rechercheSeanceQO.getDernierJourSemaine());
            query1.setParameter("jourCourant", rechercheSeanceQO.getJourCourant());

            resultatQuery.addAll(query1.getResultList());
        }

        for (final Map<String, ?> result : resultatQuery) {
            final ResultatRechercheSeanceDTO resultatRechercheSeanceDTO = getRechercheSeanceDTO(result);
                new ResultatRechercheSeanceDTO();
            listeResultatRechercheSeanceDTO.add(resultatRechercheSeanceDTO);
        }

        resultat.setValeurDTO(this.sortSeancesHeureMinute(listeResultatRechercheSeanceDTO));
        return resultat;
    }

    /**
     * Recherche des séances pour un eleve ou une famille.
     *
     * @param rechercheSeanceQO rechercheSeanceQO
     *
     * @return La liste des séances
     *
     */
    @SuppressWarnings("unchecked")
    private ResultatDTO<List<ResultatRechercheSeanceDTO>> listeSeanceAffichageEleve(RechercheSeanceQO rechercheSeanceQO){
        final ResultatDTO<List<ResultatRechercheSeanceDTO>> resultat =
            new ResultatDTO<List<ResultatRechercheSeanceDTO>>();

        final Integer idEleve = rechercheSeanceQO.getIdEleve();
        final List<GroupeBean> listeGroupeBean = rechercheSeanceQO.getListeGroupeBean();
        final List<ClasseBean> listeClasseBean = rechercheSeanceQO.getListeClasseBean();

        String listeGroupe = "(0";
        for (final GroupeBean groupe : listeGroupeBean) {
            listeGroupe += (", " + groupe.getId());
        }
        listeGroupe += ")";

        String listeClasse = "(0";
        for (final ClasseBean classe : listeClasseBean) {
            listeClasse += (", " + classe.getId());
        }
        listeClasse += ")";
        
        String clauseEnseignement = "";
        if (rechercheSeanceQO.getIdEnseignement() != null) {
            clauseEnseignement = " SEQ.idEnseignement=" + rechercheSeanceQO.getIdEnseignement() + " AND ";
        }
        
        final String requete1 =
            "SELECT " + " distinct new map( " + 
                    " CLA.code as codeClasse, " +
            " CLA.designation as designationClasse, " +
            LISTE_SEANCE_AFFICHAGE_CHAMPS_SELECT_COMMON +
            " ) FROM " +
            SequenceBean.class.getName() + " SEQ " +
            " INNER JOIN SEQ.enseignement ENS " + 
            " INNER JOIN SEQ.classe CLA, " + SeanceBean.class.getName() + " SEA " +
            "INNER JOIN SEA.enseignant ENSEI, " + ElevesClassesBean.class.getName() + " EC " +
            " WHERE " +
            " CLA.id = EC.pk.idClasse AND " +
            " EC.pk.idEleve = :idEleve AND " + 
            " SEQ.id = SEA.idSequence AND " +
            " SEQ.idClasse IN " + listeClasse + " AND " 
            + clauseEnseignement + 
            " SEA.date >= :premierJourSemaine AND " +
            " SEA.date <= :dernierJourSemaine AND " + 
            " SEA.date <= :jourCourant ";

        final String requete2 =
            "SELECT " + " distinct new map( " + " GRP.code as codeGroupe, " +
            " GRP.designation as designationGroupe, " + 
            LISTE_SEANCE_AFFICHAGE_CHAMPS_SELECT_COMMON +
            " ) FROM " +
            SequenceBean.class.getName() + " SEQ " +
            " INNER JOIN SEQ.enseignement ENS " + 
            " INNER JOIN SEQ.groupe GRP, " + 
            SeanceBean.class.getName() + " SEA " +
            " INNER JOIN SEA.enseignant ENSEI, " +
            ElevesGroupesBean.class.getName() + " EG " +
            " WHERE " +
            " GRP.id = EG.pk.idGroupe AND " + clauseEnseignement + 
            " EG.pk.idEleve = :idEleve AND " + " SEQ.id = SEA.idSequence AND " +
            " SEQ.idGroupe IN " + listeGroupe + " AND " +
            " SEA.date >= :premierJourSemaine AND " +
            " SEA.date <= :dernierJourSemaine AND " + " SEA.date <= :jourCourant ";

        final Query query1 = getEntityManager().createQuery(requete1);
        final Query query2 = getEntityManager().createQuery(requete2);

        query1.setParameter("idEleve", idEleve);
        query1.setParameter("premierJourSemaine",
                            rechercheSeanceQO.getPremierJourSemaine());
        query1.setParameter("jourCourant", rechercheSeanceQO.getJourCourant());
        query1.setParameter("dernierJourSemaine",
                            rechercheSeanceQO.getDernierJourSemaine());

        query2.setParameter("idEleve", idEleve);
        query2.setParameter("premierJourSemaine",
                            rechercheSeanceQO.getPremierJourSemaine());
        query2.setParameter("jourCourant", rechercheSeanceQO.getJourCourant());
        query2.setParameter("dernierJourSemaine",
                            rechercheSeanceQO.getDernierJourSemaine());

        final List<Map<String, ?>> resultatQuery = new ArrayList<Map<String, ?>>();
        resultatQuery.addAll(query1.getResultList());
        resultatQuery.addAll(query2.getResultList());

        final List<ResultatRechercheSeanceDTO> listeResultatRechercheSeanceDTO =
            new ArrayList<ResultatRechercheSeanceDTO>();
        for (final Map<String, ?> result : resultatQuery) {
            final ResultatRechercheSeanceDTO resultatRechercheSeanceDTO = getRechercheSeanceDTO(result);
                
            listeResultatRechercheSeanceDTO.add(resultatRechercheSeanceDTO);
        }
        resultat.setValeurDTO(this.sortSeancesHeureMinute(listeResultatRechercheSeanceDTO));

        return resultat;
    }

    /**
     * @param rechercheSeanceQO r
     * @return les séances liées aux 'sous' groupes associés à la classe
     */
    private Query listeSeanceAffichageEnseignantHelperClasseGroupe(RechercheSeanceQO rechercheSeanceQO) {

        final Integer idEnseignant = rechercheSeanceQO.getIdEnseignant();
        
        String clauseEnseignement = "";
        if (rechercheSeanceQO.getIdEnseignement() != null) {
            clauseEnseignement = " SEQ.idEnseignement=" + rechercheSeanceQO.getIdEnseignement() + " AND ";
        }
        
        //Liste des groupes selectionnés
        final List<GroupeDTO> listeGroupeDTO = rechercheSeanceQO.getListeGroupeDTO();
        final String listeGroupe = constuireListeGroupeChaine(listeGroupeDTO);
        

        String requete2 = "";
        
        requete2 = "SELECT " + " distinct new map(" + " GRP.code as codeGroupe, " +
                   " GRP.designation as designationGroupe, " +
                   LISTE_SEANCE_AFFICHAGE_CHAMPS_SELECT_COMMON +
                   " ) FROM " +
                   EnseignantsGroupesBean.class.getName() + " EG " +
                   " INNER JOIN EG.groupe GRP, " +
                   EnseignantBean.class.getName() + " ENSEI, " +
                   SequenceBean.class.getName() + " SEQ " +
                   " INNER JOIN SEQ.enseignement ENS, " + 
                   SeanceBean.class.getName() + " SEA " + 
                   " WHERE " + 
                   " GRP.id = SEQ.idGroupe AND " +
                   " GRP.id IN " + listeGroupe + " AND " +
                   " ENSEI.id = SEA.idEnseignant AND " +
                   " SEQ.id = SEA.idSequence AND " +
                   clauseEnseignement + 
                   LISTE_SEANCE_AFFICHAGE_WHERE_DATE_SEANCE;
                   
        
        final Query query2 = getEntityManager().createQuery(requete2);

        

        query2.setParameter("idEnseignant", idEnseignant);
        query2.setParameter("premierJourSemaine",
                            rechercheSeanceQO.getPremierJourSemaine());
        query2.setParameter("dernierJourSemaine",
                            rechercheSeanceQO.getDernierJourSemaine());
        query2.setParameter("jourCourant", rechercheSeanceQO.getJourCourant());
        
        return query2;
    }
    
    /**
     * 
     * @param rechercheSeanceQO r
     * @return Les séances liées au groupe
     */
    private Query listeSeanceAffichageEnseignantHelperGroupe(RechercheSeanceQO rechercheSeanceQO) {
        final Integer idEnseignant = rechercheSeanceQO.getIdEnseignant();
        
        String clauseEnseignement = "";
        if (rechercheSeanceQO.getIdEnseignement() != null) {
            clauseEnseignement = " SEQ.idEnseignement=" + rechercheSeanceQO.getIdEnseignement() + " AND ";
        }
        
        Integer idGroupe = rechercheSeanceQO.getGroupeClasseSelectionne().getId();

        String requete1 = "SELECT " + " distinct new map(" + " GRP.code as codeGroupe, " +
                   " GRP.designation as designationGroupe, " +
                   LISTE_SEANCE_AFFICHAGE_CHAMPS_SELECT_COMMON +
                   " ) FROM " +
                   EnseignantsGroupesBean.class.getName() + " EG " +
                   " INNER JOIN EG.groupe GRP, " +
                   EnseignantBean.class.getName() + " ENSEI, " +
                   SequenceBean.class.getName() + " SEQ " +
                   " INNER JOIN SEQ.enseignement ENS, " + 
                   SeanceBean.class.getName() + " SEA " + 
                   " WHERE " + 
                   " GRP.id = SEQ.idGroupe AND " +
                   " GRP.id = :idGroupe AND " + 
                   " ENSEI.id = SEA.idEnseignant AND " +
                   " SEQ.id = SEA.idSequence AND " + 
                   clauseEnseignement +  
                   LISTE_SEANCE_AFFICHAGE_WHERE_DATE_SEANCE;

        final Query query1 = getEntityManager().createQuery(requete1);

        query1.setParameter("idGroupe", idGroupe);
        query1.setParameter("idEnseignant", idEnseignant);
        query1.setParameter("premierJourSemaine",
                            rechercheSeanceQO.getPremierJourSemaine());
        query1.setParameter("dernierJourSemaine",
                            rechercheSeanceQO.getDernierJourSemaine());
        query1.setParameter("jourCourant", rechercheSeanceQO.getJourCourant());

        return query1;
    }

    private static final String LISTE_SEANCE_AFFICHAGE_CHAMPS_SELECT_COMMON =
            " SEA.id as idSeance, " + 
            " SEA.code as codeSeance, " +
            " SEA.date as dateSeance, " + 
            " SEA.intitule as intituleSeance, " +
            " SEA.heureDebut as heureDebutSeance, " +
            " SEA.minuteDebut as minuteDebutSeance, " +
            " SEA.heureFin as heureFinSeance, " +
            " SEA.minuteFin as minuteFinSeance, " +
            " SEA.idEnseignant as idEnseignantSea, " +
            " SEQ.id as idSequence, " +
            " SEQ.intitule as intituleSequence, " +
            " SEQ.code as codeSequence, " +
            " SEQ.idGroupe as rattachement, " +
            " SEQ.idEnseignement as idEnseignement, " +
            " SEQ.idEtablissement as idEtablissement, " +
            " SEQ.idEnseignant as idEnseignantSeq, " +
            " ENS.designation as designationEnseignement," +
            " ENSEI.nom as nomEnseignant ," +
            " ENSEI.prenom as prenomEnseignant ," +
            " ENSEI.civilite as civiliteEnseignant";
    
    private static final String LISTE_SEANCE_AFFICHAGE_WHERE_DATE_SEANCE =
            " ( ( " + 
                    " SEQ.idEnseignant = :idEnseignant AND " +
                    " SEA.date >= :premierJourSemaine AND " +
                    " SEA.date <= :dernierJourSemaine " + " ) " + 
                    " OR " + 
                    " ( " +                   
                    " SEQ.idEnseignant != :idEnseignant AND " +
                    " SEA.date <= :jourCourant AND " + 
                    " SEA.date <= :dernierJourSemaine AND " +
                    " SEA.date >= :premierJourSemaine " + " ) ) ";
    
    /**
     * @param rechercheSeanceQO r
     * @return les séances liées à la classe
     */
    private Query listeSeanceAffichageEnseignantHelperClasse(RechercheSeanceQO rechercheSeanceQO) {
        final Integer idEnseignant = rechercheSeanceQO.getIdEnseignant();
        Integer idClasse = null;

        

        String clauseEnseignement = "";
        if (rechercheSeanceQO.getIdEnseignement() != null) {
            clauseEnseignement = " SEQ.idEnseignement=" + rechercheSeanceQO.getIdEnseignement() + " AND ";
        }
        
        idClasse = rechercheSeanceQO.getGroupeClasseSelectionne().getId();

        String requete1 = "";
        
        requete1 = "SELECT " + " distinct new map(" +
                   " CLA.code as codeClasse, " +
                   " CLA.designation as designationClasse, " +
                    LISTE_SEANCE_AFFICHAGE_CHAMPS_SELECT_COMMON +          
                   ") " + " FROM " +
                   EnseignantsClassesBean.class.getName() + " EC " +
                   " INNER JOIN EC.classe CLA, " +
                   EnseignantBean.class.getName() + " ENSEI, " +
                   SequenceBean.class.getName() + " SEQ " +
                   " INNER JOIN SEQ.enseignement ENS, " + 
                   SeanceBean.class.getName() + " SEA " + 
                   " WHERE " + 
                   " CLA.id = SEQ.idClasse AND " +
                   " CLA.id = :idClasse AND " +
                   " ENSEI.id = SEA.idEnseignant AND " +
                   " SEQ.id = SEA.idSequence AND " +
                   clauseEnseignement + 
                   LISTE_SEANCE_AFFICHAGE_WHERE_DATE_SEANCE;
        
        final Query query1 = getEntityManager().createQuery(requete1);
        
        query1.setParameter("idClasse", idClasse);
        query1.setParameter("idEnseignant", idEnseignant);
        query1.setParameter("premierJourSemaine",
                            rechercheSeanceQO.getPremierJourSemaine());
        query1.setParameter("dernierJourSemaine",
                            rechercheSeanceQO.getDernierJourSemaine());
        query1.setParameter("jourCourant", rechercheSeanceQO.getJourCourant());
        
        return query1;
    }
    
    /**
     * Recherche des séances pour un enseignant.
     *
     * @param rechercheSeanceQO rechercheSeanceQO
     *
     * @return La liste des séances
     *
     */
    @SuppressWarnings("unchecked")
    private ResultatDTO<List<ResultatRechercheSeanceDTO>> listeSeanceAffichageEnseignant(RechercheSeanceQO rechercheSeanceQO){
        final ResultatDTO<List<ResultatRechercheSeanceDTO>> resultat =
            new ResultatDTO<List<ResultatRechercheSeanceDTO>>();
        final List<ResultatRechercheSeanceDTO> listeResultatRechercheSeanceDTO =
            new ArrayList<ResultatRechercheSeanceDTO>();

        final TypeGroupe typeGroupeClasse = rechercheSeanceQO.getTypeGroupeSelectionne();
        final List<Map<String, ?>> resultatQuery = new ArrayList<Map<String, ?>>();

        if (typeGroupeClasse == TypeGroupe.CLASSE) {
            Query classQuery = listeSeanceAffichageEnseignantHelperClasse(rechercheSeanceQO);
            Query classGroupsQuery = listeSeanceAffichageEnseignantHelperClasseGroupe(rechercheSeanceQO);
            
            resultatQuery.addAll(classQuery.getResultList());
            resultatQuery.addAll(classGroupsQuery.getResultList());
        } else if (typeGroupeClasse == TypeGroupe.GROUPE) {
            Query groupQuery = listeSeanceAffichageEnseignantHelperGroupe(rechercheSeanceQO);
            resultatQuery.addAll(groupQuery.getResultList());
        }

        for (final Map<String, ?> result : resultatQuery) {
            final ResultatRechercheSeanceDTO resultatRechercheSeanceDTO =
                    getRechercheSeanceDTO(result);
                listeResultatRechercheSeanceDTO.add(resultatRechercheSeanceDTO);
        }

        resultat.setValeurDTO(this.sortSeancesHeureMinute(listeResultatRechercheSeanceDTO));
        return resultat;
    }
    
    /**
     * @param result un résultat d'une requête
     * @return le DTO construit
     */
    private static ResultatRechercheSeanceDTO getRechercheSeanceDTO(Map<String, ?> result) {
        ResultatRechercheSeanceDTO resultatRechercheSeanceDTO =    new ResultatRechercheSeanceDTO();
        
        GroupesClassesDTO groupeClasseDTO = new GroupesClassesDTO();
        
        final String codeClass = StringUtils.trimToNull((String) result.get("codeClasse"));
        
        if (codeClass != null) {
            groupeClasseDTO.setTypeGroupe(TypeGroupe.CLASSE);
            groupeClasseDTO.setCode(codeClass);
            groupeClasseDTO.setDesignation((String) result.get("designationClasse"));
        } else {
            groupeClasseDTO.setTypeGroupe(TypeGroupe.GROUPE);
            groupeClasseDTO.setId((Integer) result.get("rattachement"));
            groupeClasseDTO.setCode((String) result.get("codeGroupe"));
            groupeClasseDTO.setDesignation((String) result.get("designationGroupe"));            
        }
        
        resultatRechercheSeanceDTO.getSequence().setGroupesClassesDTO(groupeClasseDTO);
        
        
        resultatRechercheSeanceDTO.setId((Integer) result.get("idSeance"));
        resultatRechercheSeanceDTO.getSequence().setId((Integer) result.get("idSequence"));
        resultatRechercheSeanceDTO.getSequence().setIntitule((String) result.get("intituleSequence"));
        resultatRechercheSeanceDTO.getSequence().getEnseignantDTO().setId((Integer) result.get("idEnseignantSeq"));
        resultatRechercheSeanceDTO.getSequence().setIdEtablissement((Integer) result.get("idEtablissement"));
        resultatRechercheSeanceDTO.getSequence().setIdEnseignement((Integer) result.get("idEnseignement"));
        
        resultatRechercheSeanceDTO.setCode((String) result.get("codeSeance"));
        resultatRechercheSeanceDTO.setDate((Date) result.get("dateSeance"));
        resultatRechercheSeanceDTO.setIntitule((String) result.get("intituleSeance"));
        resultatRechercheSeanceDTO.setHeureDebut((Integer) result.get("heureDebutSeance"));
        
        resultatRechercheSeanceDTO.setMinuteDebut((Integer) result.get("minuteDebutSeance"));
        resultatRechercheSeanceDTO.setHeureFin((Integer) result.get("heureFinSeance"));
        resultatRechercheSeanceDTO.setMinuteFin((Integer) result.get("minuteFinSeance"));
                
        resultatRechercheSeanceDTO.setDesignationEnseignement((String) result.get("designationEnseignement"));
        resultatRechercheSeanceDTO.getEnseignantDTO().setNom((String) result.get("nomEnseignant"));
        resultatRechercheSeanceDTO.getEnseignantDTO().setPrenom((String) result.get("prenomEnseignant"));
        resultatRechercheSeanceDTO.getEnseignantDTO().setCivilite((String) result.get("civiliteEnseignant"));
        resultatRechercheSeanceDTO.getEnseignantDTO().setId((Integer) result.get("idEnseignantSea"));
        
        return resultatRechercheSeanceDTO;
    }

    /**
     * Recherche des séances pour la direction.
     *
     * @param rechercheSeanceQO rechercheSeanceQO
     *
     * @return La liste des séances
     *
     */
    @SuppressWarnings("unchecked")
    private ResultatDTO<List<ResultatRechercheSeanceDTO>> listeSeanceAffichageDirection(RechercheSeanceQO rechercheSeanceQO){
        
        Preconditions.checkArgument(rechercheSeanceQO.getTypeGroupe() != null);
        
        final ResultatDTO<List<ResultatRechercheSeanceDTO>> resultat =
            new ResultatDTO<List<ResultatRechercheSeanceDTO>>();
        final List<ResultatRechercheSeanceDTO> listeResultatRechercheSeanceDTO =
            new ArrayList<ResultatRechercheSeanceDTO>();

        final Integer idEtablissement = rechercheSeanceQO.getIdEtablissement();

        final TypeGroupe typeGroupeClasse = rechercheSeanceQO.getTypeGroupeSelectionne();
        Integer idGroupe = null;
        Integer idClasse = null;

        final Integer idAnneeScolaire = rechercheSeanceQO.getIdAnneeScolaire();
        final String schema;

        if (idAnneeScolaire != null) {
            schema = Archive.PREFIX_SCHEMA_ARCHIVE +
                     rechercheSeanceQO.getExerciceAnneeScolaire();
        } else {
            schema = SchemaUtils.getDefaultSchema();
        }

        String clauseEnseignement = "";
        if (rechercheSeanceQO.getIdEnseignement() != null) {
            clauseEnseignement = " SEQ.id_enseignement=" + rechercheSeanceQO.getIdEnseignement() + " AND ";
        }
        
        final String CHAMPS_SELECT_COMMUNS
        = 
                "SEA.id as idSea, " +
                "SEA.code as codeSea, " +
                "SEA.date as dateSea, " +
                "SEA.intitule as intSea, " +
                "SEA.heure_debut as hSea, " +
                "SEA.minute_debut as mSea, " +
                "SEA.heure_fin as hSeaFin, " +
                "SEA.minute_fin as mSeaFin, " +
                "SEQ.id as idSeq, " +
                "SEQ.intitule as intSeq, " +
                "SEQ.id_groupe as idGrp, " +
                "SEQ.id_enseignement as idEns," +
                "ENS.designation as desEns, " +
                "ENSEI.nom as nomEnsei, " +
                "ENSEI.prenom as preEnsei," +
                "ENSEI.civilite as civEnsei, " +
                "SEA.id_enseignant as idEnseign ";
        
        String nativeRequete1 = "";
        String nativeRequete2 = "";

        final List<Object[]> resultatQuery = new ArrayList<Object[]>();

        //Nous utilisons des requêtes natives afin de jongler facilement entre les schémas.
        if (typeGroupeClasse.equals(TypeGroupe.CLASSE)) {
            //CLASSE

            //Liste des groupes selectionnés
            final List<GroupeDTO> listeGroupeDTO = rechercheSeanceQO.getListeGroupeDTO();
            String listeGroupe = "(0";
            for (final GroupeDTO groupe : listeGroupeDTO) {
                listeGroupe += (", " + groupe.getId());
            }
            listeGroupe += ")";

            idClasse = rechercheSeanceQO.getGroupeClasseSelectionne().getId();

            nativeRequete1 = "SELECT " + " distinct " +
                             " CLA.code as codeClasse, '' as codeGrp, CLA.designation as desCla, " +
                             " '' as desGrp, " 
                             + CHAMPS_SELECT_COMMUNS + " FROM " +
                             SchemaUtils.getTableAvecSchema(schema,
                                                            "cahier_enseignant_classe") +
                             " EC INNER JOIN " +
                             SchemaUtils.getTableAvecSchema(schema, "cahier_classe") +
                             " CLA ON (CLA.id = EC.id_classe) " +
                             " INNER JOIN " + 
                             SchemaUtils.getTableAvecSchema(schema, "cahier_sequence") +
                             " SEQ ON (CLA.id = SEQ.id_classe) " +
                             " INNER JOIN " +
                             SchemaUtils.getTableAvecSchema(schema, "cahier_enseignement") +
                             " ENS ON (SEQ.id_enseignement = ENS.id) " +
                             " INNER JOIN " +
                             SchemaUtils.getTableAvecSchema(schema, "cahier_seance") +
                             " SEA ON (SEQ.id = SEA.id_sequence) " +
                             " INNER JOIN " +
                             SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant") +
                             " ENSEI ON (ENSEI.id = SEA.id_enseignant) " + 
                             " WHERE " + clauseEnseignement +
                             " CLA.id =? AND " + 
                             " CLA.id_etablissement =? AND " +
                             " SEA.date >=? AND " +
                             " SEA.date <=? ";

            nativeRequete2 = "SELECT " + " distinct " +
                             " '' as codeClasse, GRP.code as codeGrp, '' as desCla, GRP.designation as desGrp, " +
                             CHAMPS_SELECT_COMMUNS +
                             " FROM " +
                             SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant_groupe") +
                             " EG " + " INNER JOIN " +
                             SchemaUtils.getTableAvecSchema(schema, "cahier_groupe") +
                             " GRP ON (GRP.id = EG.id_groupe) " + 
                             " INNER JOIN " + 
                             SchemaUtils.getTableAvecSchema(schema, "cahier_sequence") +
                             " SEQ ON (GRP.id = SEQ.id_groupe) " +
                             " INNER JOIN " +
                             SchemaUtils.getTableAvecSchema(schema, "cahier_enseignement") +
                             " ENS ON (SEQ.id_enseignement = ENS.id) " +
                             " INNER JOIN " +
                             SchemaUtils.getTableAvecSchema(schema, "cahier_seance") +
                             " SEA ON (SEQ.id = SEA.id_sequence) " +
                             " INNER JOIN " +
                             SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant") +
                             " ENSEI ON (ENSEI.id = SEA.id_enseignant) " +  
                             " WHERE " + clauseEnseignement +
                             " GRP.id IN " + listeGroupe + " AND " +
                             " GRP.id_etablissement =? AND " +
                             " SEA.date >=? AND " +
                             " SEA.date <=? ";

            final Query query1 = getEntityManager().createNativeQuery(nativeRequete1);
            final Query query2 = getEntityManager().createNativeQuery(nativeRequete2);

            query1.setParameter(1, idClasse);
            query1.setParameter(2, idEtablissement);
            query1.setParameter(3, rechercheSeanceQO.getPremierJourSemaine());
            query1.setParameter(4, rechercheSeanceQO.getDernierJourSemaine());

            query2.setParameter(1, idEtablissement);
            query2.setParameter(2, rechercheSeanceQO.getPremierJourSemaine());
            query2.setParameter(3, rechercheSeanceQO.getDernierJourSemaine());

            resultatQuery.addAll(query1.getResultList());
            resultatQuery.addAll(query2.getResultList());
        } else if (typeGroupeClasse.equals(TypeGroupe.GROUPE)) {
            //GROUPE
            idGroupe = rechercheSeanceQO.getGroupeClasseSelectionne().getId();

            nativeRequete1 = "SELECT " + " distinct " +
                             " '' as codeCla, GRP.code as codeGrp, '' as desCla, GRP.designation as desGrp, " +
                             CHAMPS_SELECT_COMMUNS + " FROM " +
                             SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant_groupe") +
                             " EG " + " INNER JOIN " +
                             SchemaUtils.getTableAvecSchema(schema, "cahier_groupe") +
                             " GRP ON (GRP.id = EG.id_groupe) " + 
                             " INNER JOIN " + 
                             SchemaUtils.getTableAvecSchema(schema, "cahier_sequence") +
                             " SEQ ON (GRP.id = SEQ.id_groupe) " +
                             " INNER JOIN " +
                             SchemaUtils.getTableAvecSchema(schema, "cahier_enseignement") +
                             " ENS ON (SEQ.id_enseignement = ENS.id) " +
                             " INNER JOIN " +
                             SchemaUtils.getTableAvecSchema(schema, "cahier_seance") +
                             " SEA ON (SEQ.id = SEA.id_sequence) " +
                             " INNER JOIN " +
                             SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant") +
                             " ENSEI ON (ENSEI.id = SEA.id_enseignant) " +  
                             " WHERE " + 
                             clauseEnseignement +
                             " GRP.id =? AND " + 
                             " GRP.id_etablissement =? AND " +
                             " SEA.date >=? AND " +
                             " SEA.date <=? ";

            final Query query1 = getEntityManager().createNativeQuery(nativeRequete1);

            query1.setParameter(1, idGroupe);
            query1.setParameter(2, idEtablissement);
            query1.setParameter(3, rechercheSeanceQO.getPremierJourSemaine());
            query1.setParameter(4, rechercheSeanceQO.getDernierJourSemaine());

            resultatQuery.addAll(query1.getResultList());
        }

        for (final Object[] result : resultatQuery) {
            final ResultatRechercheSeanceDTO resultatRechercheSeanceDTO =
                new ResultatRechercheSeanceDTO();
            
            TypeGroupe typeGroupe = StringUtils.trimToNull((String) result[0]) != null ? TypeGroupe.CLASSE : TypeGroupe.GROUPE;
            
            GroupesClassesDTO groupesClassesDTO = new GroupesClassesDTO();
            groupesClassesDTO.setTypeGroupe(typeGroupe);
            if (typeGroupe == TypeGroupe.CLASSE) {
                groupesClassesDTO.setCode(StringUtils.trimToNull((String) result[0]));
                groupesClassesDTO.setDesignation(StringUtils.trimToNull((String) result[2]));

                
            } else {
                groupesClassesDTO.setCode(StringUtils.trimToNull((String) result[1]));
                groupesClassesDTO.setDesignation(StringUtils.trimToNull((String) result[3]));
                groupesClassesDTO.setId((Integer) result[14]);
            }
            resultatRechercheSeanceDTO.getSequenceDTO().setGroupesClassesDTO(groupesClassesDTO);
            
            resultatRechercheSeanceDTO.setId((Integer) result[4]);
            resultatRechercheSeanceDTO.setCode((String) result[5]);
            resultatRechercheSeanceDTO.setDate((Date) result[6]);
            resultatRechercheSeanceDTO.setIntitule((String) result[7]);
            resultatRechercheSeanceDTO.setHeureDebut((Integer) result[8]);
            resultatRechercheSeanceDTO.setMinuteDebut((Integer) result[9]);

            resultatRechercheSeanceDTO.setHeureFin((Integer) result[10]);
            resultatRechercheSeanceDTO.setMinuteFin((Integer) result[11]);
            
            resultatRechercheSeanceDTO.getSequence().setId((Integer) result[12]);
            resultatRechercheSeanceDTO.getSequence().setIntitule((String) result[13]);


            resultatRechercheSeanceDTO.getSequence().setIdEnseignement((Integer) result[15]);
            resultatRechercheSeanceDTO.setDesignationEnseignement((String) result[16]);
            resultatRechercheSeanceDTO.getEnseignantDTO().setNom((String) result[17]);
            resultatRechercheSeanceDTO.getEnseignantDTO().setPrenom((String) result[18]);
            resultatRechercheSeanceDTO.getEnseignantDTO().setCivilite((String) result[19]);
            resultatRechercheSeanceDTO.getEnseignantDTO().setId((Integer) result[20]);

            listeResultatRechercheSeanceDTO.add(resultatRechercheSeanceDTO);
        }
        resultat.setValeurDTO(this.sortSeancesHeureMinute(listeResultatRechercheSeanceDTO));
        return resultat;
    }

    /**
     * Trie de la liste des séances par heure et min de début croissant.
     *
     * @param listeNonTriee la liste non triée.
     *
     * @return la liste triée.
     */
    private List<ResultatRechercheSeanceDTO> sortSeancesHeureMinute(final List<ResultatRechercheSeanceDTO> listeNonTriee) {
        Assert.isNotNull("listeNonTriee", listeNonTriee);

        final List<String> criteresDeTrie = new ArrayList<String>();
        criteresDeTrie.add("heureDebut");
        criteresDeTrie.add("minuteDebut");
        return ComparateurUtils.sort(listeNonTriee, criteresDeTrie);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Map<String, Integer> findSeanceSemaine(final Date dateDebut,
                                                  final Date dateFin,
                                                  final Integer idEnseignant,
                                                  final Integer idEtablissement) {
        Assert.isNotNull("dateDebut", dateDebut);
        Assert.isNotNull("dateFin", dateFin);
        Assert.isNotNull("idEnseignant", idEnseignant);
        Assert.isNotNull("idEtablissement", idEtablissement);

        final String query =
            "SELECT new Map(s.id as id, s.date as date, s.heureDebut as heureDebutSeance, " +
            " s.minuteDebut as minuteDebutSeance, s.heureFin as heureFinSeance, s.minuteFin as minuteFinSeance, " +
            " sequ.idEnseignement as idEnseignement, coalesce(sequ.idClasse,0) as idClasse, coalesce(sequ.idGroupe,0) as idGroupe) " +
            " FROM " +
            SeanceBean.class.getName() + " s INNER JOIN s.sequence as sequ " +
            "WHERE s.idEnseignant =:idEnseignant AND s.idEtablissement = :idEtablissement AND " +
            " s.date >= :dateDebut AND s.date <= :dateFin ";

        final List<Map<String, ?>> result =
            getEntityManager().createQuery(query)
                .setParameter("idEnseignant", idEnseignant)
                .setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin)
                .setParameter("idEtablissement", idEtablissement).getResultList();

        //constitution de la map de code de sequence id de seance
        final Map<String, Integer> mapSequPuisDateSeaPuisHo =
            new HashMap<String, Integer>();

        for (final Map<String, ?> map : result) {
            final String idClasse = ((Integer) map.get("idClasse")).toString();
            final String idGroupe = ((Integer) map.get("idGroupe")).toString();
            final String idEnseignement =
                ((Integer) map.get("idEnseignement")).toString();

            final String dateSeance = DateUtils.format((Date) map.get("date"));

            final String heureDebut =
                org.apache.commons.lang.StringUtils.leftPad(((Integer) map.get("heureDebutSeance")).toString(),
                                    2, '0');
            final String minuteDebut =
                org.apache.commons.lang.StringUtils.leftPad(((Integer) map.get("minuteDebutSeance")).toString(),
                                    2, '0');
            final String heureFin =
                org.apache.commons.lang.StringUtils.leftPad(((Integer) map.get("heureFinSeance")).toString(), 2,
                                    '0');
            final String minuteFin =
                org.apache.commons.lang.StringUtils.leftPad(((Integer) map.get("minuteFinSeance")).toString(), 2,
                                    '0');

            final String clef =
                idClasse + "-" + 
                idGroupe + "-" + 
                idEnseignement + 
                dateSeance + 
                heureDebut + 
                minuteDebut + 
                heureFin +
                minuteFin;
            
            mapSequPuisDateSeaPuisHo.put(clef, (Integer) map.get("id"));
        }

        return mapSequPuisDateSeaPuisHo;
    }

    /**
     * Cherche les libelles associés aux matieres dont les id sont passes dans la liste 
     * qui ont été redefinis pour l'etablissement. 
     * @param idEtablissement id de l'etablissement 
     * @param listeIdEnseignement liste des id des enseignements
     * @return une map key=idEnseignement, valeur=libelle matiere
     */
    @SuppressWarnings("unchecked")
    private Map<Integer, String> findLibelleEnseignement(
            final Integer idEtablissement,
            final List<Integer> listeIdEnseignement) {
        Boolean isFirst = true;
        String requete = 
            " SELECT new Map( " + 
            "    LE.pk.idEnseignement as idEnseignement, LE.libelle as libelle) " + 
            " FROM " + 
            EnseignementLibelleBean.class.getName() + " LE " + 
            " WHERE " + 
            "    LE.pk.idEtablissement  = :idEtablissement AND " +  
            "    LE.pk.idEnseignement in ( ";
        for (final Integer idEnseignement:listeIdEnseignement) {
            if (!isFirst) {
                requete += ",";
            } else {
                isFirst = false;
            }
            requete += idEnseignement.toString(); 
        } 
        requete += "  )";
        
        final List<Map<String, ?>> listeEnseignement =
            getEntityManager().createQuery(requete)
                .setParameter("idEtablissement", idEtablissement)
                .getResultList();
        
        final Map<Integer, String> listeLibelleEnseignement = new Hashtable<Integer, String>();
        for (final Map<String, ?> result:listeEnseignement) {
            listeLibelleEnseignement.put((Integer) result.get("idEnseignement"), (String) result.get("libelle"));
        }
        return listeLibelleEnseignement;
    }   

    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<SeanceDTO> findListeSeanceSemaine(final Date dateDebut,
                                                  final Date dateFin,
                                                  final Integer idEnseignant,
                                                  final Integer idEtablissement,
                                                  final Set<Integer> idSequence) {
        Assert.isNotNull("dateDebut", dateDebut);
        Assert.isNotNull("dateFin", dateFin);
        Assert.isNotNull("idEnseignant", idEnseignant);
        Assert.isNotNull("idEtablissement", idEtablissement);
        Assert.isNotNull("idSequence", idSequence);
        
        //constitution de la liste resultat 
        final List<SeanceDTO> listeSeance = new ArrayList<SeanceDTO>();
        final List<Integer> listeIdEnseignement = new ArrayList<Integer>();
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(idSequence)){
            final String query =
                "SELECT new Map(" +
                "  s.id as id, " +
                "  s.date as date, " +
                "  s.heureDebut as heureDebutSeance, " +
                "  s.minuteDebut as minuteDebutSeance, " +
                "  s.heureFin as heureFinSeance, " +
                "  s.minuteFin as minuteFinSeance, " +
                "  sequ.idEnseignement as idEnseignement, " +
                "  coalesce(sequ.idClasse,0) as idClasse, " +
                "  coalesce(sequ.idGroupe,0) as idGroupe," +
                "  C.designation as designationClasse," +
                "  G.designation as designationGroupe, " +
                "  E.designation as matiere) " +
                " FROM " +
                SeanceBean.class.getName() +
                " s INNER JOIN s.sequence as sequ " +
                " LEFT JOIN sequ.groupe as G " +
                " LEFT JOIN sequ.classe as C " +
                " INNER JOIN sequ.enseignement as E " + 
                "WHERE " +
                "  s.idEnseignant =:idEnseignant AND " +
                "  s.idSequence IN (:idSequence) AND " +
                "  s.date >= :dateDebut AND " +
                "  s.date <= :dateFin ";

            final List<Map<String, ?>> result =
                getEntityManager().createQuery(query)
                .setParameter("idEnseignant", idEnseignant)
                .setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin)
                .setParameter("idSequence", idSequence)
                .getResultList();




            for (final Map<String, ?> map : result) {
                final SeanceDTO seance = new SeanceDTO();
                seance.setId((Integer) map.get("id"));
                
                TypeGroupe typeGroupe = map.get("idClasse") != null ? TypeGroupe.CLASSE : TypeGroupe.GROUPE;
                GroupesClassesDTO groupesClassesDTO = new GroupesClassesDTO();
                groupesClassesDTO.setTypeGroupe(typeGroupe);
                if (typeGroupe == TypeGroupe.CLASSE) {
                    groupesClassesDTO.setId((Integer) map.get("idClasse"));
                    groupesClassesDTO.setDesignation((String) map.get("designationClasse"));
                } else {
                    groupesClassesDTO.setId((Integer) map.get("idGroupe"));
                    groupesClassesDTO.setDesignation((String) map.get("designationGroupe"));
                }
                seance.getSequenceDTO().setGroupesClassesDTO(groupesClassesDTO);
                seance.getSequence().setIdEnseignement((Integer) map.get("idEnseignement"));
                seance.setDate((Date) map.get("date"));
                seance.setHeureDebut((Integer) map.get("heureDebutSeance"));
                seance.setMinuteDebut((Integer) map.get("minuteDebutSeance"));
                seance.setHeureFin((Integer) map.get("heureFinSeance"));
                seance.setMinuteFin((Integer) map.get("minuteFinSeance"));
                seance.setMatiere((String) map.get("matiere"));

                if (!listeIdEnseignement.contains(seance.getSequence().getIdEnseignement())) {
                    listeIdEnseignement.add(seance.getSequence().getIdEnseignement());
                }

                listeSeance.add(seance);
            }


            // Met a jour les libelle des matieres qui peuvent avoir ete redéfinis par l'admin pour cet etablissement
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(listeSeance)) {
                final Map<Integer,String> listeLibelleMatiere = findLibelleEnseignement(
                        idEtablissement, 
                        listeIdEnseignement);
                for (final SeanceDTO seance:listeSeance) {
                    final String matiere =  listeLibelleMatiere.get(seance.getSequence().getIdEnseignement());
                    if (!StringUtils.isEmpty(matiere)) {
                        seance.setMatiere(matiere);
                    }
                }
            }        
        }
        return listeSeance;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<SeanceDTO> findSeanceDTOSemaine(final Date dateDebut, final Date dateFin,
                                                final Integer idEnseignant,
                                                final Set<Integer> idSequence) {
        Assert.isNotNull("dateDebut", dateDebut);
        Assert.isNotNull("dateFin", dateFin);
        Assert.isNotNull("idEnseignant", idEnseignant);
        Assert.isNotNull("idSequence", idSequence);

        if (org.apache.commons.collections.CollectionUtils.isEmpty(idSequence)) {
            idSequence.add(0);
        }

        final String querySequence =
            "SELECT new Map(s.id as idSequence, s.idEnseignement as idEnseingnement, " +
            " s.idGroupe as idGroupe, s.idClasse as idClasse) " + "FROM " +
            SequenceBean.class.getName() +
            " s WHERE s.id IN (:idSequence)";

        final List<Map<String, ?>> resultSequence =
            getEntityManager().createQuery(querySequence)
                .setParameter("idSequence", idSequence).getResultList();

        final Map<Integer, Integer> mapEnseignement = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> mapGroupe = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> mapClasse = new HashMap<Integer, Integer>();

        for (final Map<String, ?> mapSequence : resultSequence) {
            final Integer idSequenceFromMap = (Integer) mapSequence.get("idSequence");
            mapEnseignement.put(idSequenceFromMap,
                                (Integer) mapSequence.get("idEnseingnement"));
            mapGroupe.put(idSequenceFromMap, (Integer) mapSequence.get("idGroupe"));
            mapClasse.put(idSequenceFromMap, (Integer) mapSequence.get("idClasse"));
        }

        final String query =
            "SELECT new Map(s.id as id, s.idSequence as idSequenceSeance, s.date as date, s.heureDebut as heureDebutSeance, " +
            " s.minuteDebut as minuteDebutSeance, s.heureFin as heureFinSeance, s.minuteFin as minuteFinSeance) FROM " +
            SeanceBean.class.getName() + " s " + 
            " WHERE s.idEnseignant =:idEnseignant AND s.idSequence IN (:idSequence)" +
            " AND s.date >= :dateDebut AND s.date <= :dateFin " + " ORDER BY s.date";

        final List<Map<String, ?>> result =
            getEntityManager().createQuery(query)
                .setParameter("idEnseignant", idEnseignant)
                .setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin)
                .setParameter("idSequence", idSequence).getResultList();

        final List<SeanceDTO> listSeancesDTO = new ArrayList<SeanceDTO>();

        for (final Map<String, ?> map : result) {
            final SeanceDTO seanceDTO = new SeanceDTO();
            seanceDTO.setHeureDebut((Integer) map.get("heureDebutSeance"));
            seanceDTO.setMinuteDebut((Integer) map.get("minuteDebutSeance"));
            seanceDTO.setHeureFin((Integer) map.get("heureFinSeance"));
            seanceDTO.setMinuteFin((Integer) map.get("minuteFinSeance"));
            seanceDTO.setDate((Date) map.get("date"));
            seanceDTO.getEnseignantDTO().setId(idEnseignant);
            final Integer idSequenceSeance = (Integer) map.get("idSequenceSeance");
            seanceDTO.getSequence().setId(idSequenceSeance);
            seanceDTO.setId((Integer) map.get("id"));
            
            TypeGroupe typeGroupe = mapClasse.get(idSequenceSeance) != null ? TypeGroupe.CLASSE : TypeGroupe.GROUPE;
            GroupesClassesDTO groupesClassesDTO = new GroupesClassesDTO();
            groupesClassesDTO.setTypeGroupe(typeGroupe);
            if (typeGroupe == TypeGroupe.CLASSE) {
                groupesClassesDTO.setId((Integer) mapClasse.get(idSequenceSeance));
            } else {
                groupesClassesDTO.setId((Integer) mapGroupe.get(idSequenceSeance));
            }
            
            seanceDTO.getSequenceDTO().setGroupesClassesDTO(groupesClassesDTO);
            seanceDTO.getSequence().setIdEnseignement((Integer) mapEnseignement.get(idSequenceSeance));

            listSeancesDTO.add(seanceDTO);
        }

        return listSeancesDTO;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Map<String, SeanceDTO> findSeanceSemainePassee(final Date dateDebut,
                                                          final Date dateFin,
                                                          final Integer idEnseignant,
                                                          final Set<Integer> idSequence,
                                                          final List<GrilleHoraireDTO> listeHoraire) {
        Assert.isNotNull("dateDebut", dateDebut);
        Assert.isNotNull("dateFin", dateFin);
        Assert.isNotNull("idEnseignant", idEnseignant);
        Assert.isNotNull("idSequence", idSequence);

        final Map<String, SeanceDTO> mapSeance = new HashMap<String, SeanceDTO>();

        if (org.apache.commons.collections.CollectionUtils.isEmpty(idSequence)) {
            idSequence.add(0);
        }

        final String query =
            "SELECT new Map(s.id as id, s.date as date, s.heureDebut as heureDebutSeance, " +
            " s.minuteDebut as minuteDebutSeance, s.heureFin as heureFinSeance, s.minuteFin as minuteFinSeance, s.idSequence as idSequence) FROM " +
            SeanceBean.class.getName() +
            " s INNER JOIN s.sequence as sequ WHERE s.idEnseignant =:idEnseignant AND s.idSequence IN (:idSequence)" +
            "AND s.date >= :dateDebut AND s.date <= :dateFin ";

        final List<Map<String, ?>> result =
            getEntityManager().createQuery(query)
                .setParameter("idEnseignant", idEnseignant)
                .setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin)
                .setParameter("idSequence", idSequence).getResultList();

        //Recherche des libellés des classes ou groupes des séquences.
        final String queryLibelle =
            "SELECT new Map(s.id as id, c.designation as classe, g.designation as groupe, " +
            "ens.designation as enseignementLibelle)" + " FROM " +
            SequenceBean.class.getName() +
            " s LEFT JOIN s.classe c LEFT JOIN s.groupe g " +
            " INNER JOIN s.enseignement ens" +
            " WHERE s.id IN (:idSequenceListe)";

        final List<Map<String, ?>> resultLibelle =
            getEntityManager().createQuery(queryLibelle)
                .setParameter("idSequenceListe", idSequence).getResultList();

        final Map<Integer, String> mapIdSequenceEnseignement =
            org.crlr.utils.CollectionUtils.formatMapClefValeur(resultLibelle, "id", "enseignementLibelle");

        final Map<Integer, String> mapLibelle = new HashMap<Integer, String>();

        for (final Map<String, ?> map : resultLibelle) {
            mapLibelle.put((Integer) map.get("id"),
                           ObjectUtils.defaultIfNull((String) map.get("classe"),
                                                     (String) map.get("groupe")));
        }

        for (final Map<String, ?> map : result) {
            final Date dateSeance = (Date) map.get("date");

            final Integer anneeSeance = DateUtils.getChamp(dateSeance, Calendar.YEAR);
            final Integer moisSeance = DateUtils.getChamp(dateSeance, Calendar.MONTH);
            final Integer jourSeance =
                DateUtils.getChamp(dateSeance, Calendar.DAY_OF_MONTH);

            for (final GrilleHoraireDTO grilleHoraireDTO : listeHoraire) {
                final Integer heureDebutGrille =
                    grilleHoraireDTO.getHeureDebut();
                final Integer minDebutGrille =
                    grilleHoraireDTO.getMinuteDebut();
                final Integer heureFinGrille =
                    grilleHoraireDTO.getHeureFin();
                final Integer minFinGrille =
                    grilleHoraireDTO.getMinuteFin();

                final Date dateHeureGrilleDebut =
                    DateUtils.creer(anneeSeance, moisSeance, jourSeance,
                                    heureDebutGrille, minDebutGrille, 0);

                

                final Integer heureDebutSeance = (Integer) map.get("heureDebutSeance");
                final Integer heureFinSeance = (Integer) map.get("heureFinSeance");
                final Integer minDebutSeance = (Integer) map.get("minuteDebutSeance");
                final Integer minFinSeance = (Integer) map.get("minuteFinSeance");

                final Date dateHeureSeanceDebut =
                    DateUtils.creer(anneeSeance, moisSeance, jourSeance,
                                    heureDebutSeance, minDebutSeance, 0);

                final Date dateHeureSeanceFin =
                    DateUtils.creer(anneeSeance, moisSeance, jourSeance, heureFinSeance,
                                    minFinSeance, 0);

                if (DateUtils.isBetweenDateMaxNonCompris(dateHeureGrilleDebut,
                                                             dateHeureSeanceDebut,
                                                             dateHeureSeanceFin)) {
                    final SeanceDTO seance = new SeanceDTO();

                    

                    seance.setDate(dateSeance);
                    seance.setHeureDebut(heureDebutSeance);
                    seance.setHeureFin(heureFinSeance);
                    seance.setMinuteDebut(minDebutSeance);
                    seance.setMinuteFin(minFinSeance);
                    seance.setDescription((String) mapIdSequenceEnseignement.get(map.get("idSequence")));

                    //Place le libelle du groupe ou de la classe dans l'intitulé
                    seance.setIntitule(mapLibelle.get(map.get("idSequence")));

                    seance.setId((Integer) map.get("id"));

                    //Génération de la clé
                    final String heureDebutString = org.apache.commons.lang.StringUtils
                            .leftPad(heureDebutGrille.toString(), 2, '0');
                    final String minuteDebutString = org.apache.commons.lang.StringUtils
                            .leftPad(minDebutGrille.toString(), 2, '0');
                    final String heureFinString = org.apache.commons.lang.StringUtils
                            .leftPad(heureFinGrille.toString(), 2, '0');
                    final String minuteFinString = org.apache.commons.lang.StringUtils
                            .leftPad(minFinGrille.toString(), 2, '0');
                    final String clef = DateUtils
                            .format((Date) map.get("date"))
                            + heureDebutString
                            + minuteDebutString
                            + heureFinString
                            + minuteFinString;

                    mapSeance.put(clef, seance);
                }
            }
        }
        return mapSeance;
    }

    /**
     * @param listeGroupeDTO la liste
     * @return la chaine pour usage dans une requête hql
     */
    private static String constuireListeGroupeChaine(List<GroupeDTO> listeGroupeDTO) {
        //Liste des groupes selectionnés
        String listeGroupe = "(0";
        for (final GroupeDTO groupe : listeGroupeDTO) {
            listeGroupe += (", " + groupe.getId());
        }
        listeGroupe += ")";
        
        return listeGroupe;
    }
  
    /**
     * @param idClasse .
     * @param idEnseignant .
     * @param idUtilisateur .
     * @param idEnseignement .
     * @param rechercheSeancePrintQO .
     * @param dateCourante .
     * @param strWhereRequete .
     * @return .
     */
    private Query findListeSeanceEditionHelperClass(final Integer idClasse,
            final Integer idEnseignant, final Integer idUtilisateur,
            final Integer idEnseignement,
            PrintSeanceOuSequenceQO rechercheSeancePrintQO,
            final Date dateCourante, 
            String strWhereRequete) {

        final Profil profil = rechercheSeancePrintQO.getProfil();
        
        final String listeGroupe = constuireListeGroupeChaine(rechercheSeancePrintQO
                .getListeGroupeDTO());
        
        // TODO : CDU : revoir cette requete pour gerer le cas d'un enseignant remplacant : different sur seance et sequence mais droit d'inspection sur le remplacant (et pas le remplace qui est positionne sur la sequence)
        String requete1 = "SELECT " + " distinct new map("  
                + " GRP.code as codeGroupe, " 
                + " GRP.designation as designationGroupe, "
                + " CLA.code as codeClasse, "
                + " CLA.designation as designationClasse, " 
                + " (select c.couleur from " + CouleurEnseignementClasseBean.class.getName() 
                +		" c where c.idEnseignant = SEQ.idEnseignant " 
                +		" and c.idEtablissement = SEQ.idEtablissement " 
                +		" and c.idEnseignement = SEQ.idEnseignement "
                +		" and c.idClasse = SEQ.idClasse ) as couleur, " 
                + CREER_REQUETE_FIND_LISTE_SEANCE_EDITION
                + " FROM " + SequenceBean.class.getName() + " SEQ "
                + " INNER JOIN SEQ.enseignement ENS "
                + " LEFT JOIN SEQ.classe CLA " + " LEFT JOIN SEQ.groupe GRP "
                + " INNER JOIN SEQ.enseignant TEA, "
                + SeanceBean.class.getName() + " SEA ";
        if (Profil.INSPECTION_ACADEMIQUE == profil) {
            requete1 += (", " + OuvertureInspecteurBean.class.getName() + " OUV ");
        }
        requete1 += (" WHERE "
                + // Jointure
                " SEA.idSequence = SEQ.id AND "
                + // Filtres a appliquer dans tous les cas
                " (CLA.id = :idClasse OR GRP.id IN " + listeGroupe + ") AND " + strWhereRequete
                + " SEA.date >= :dateDebut AND "
                + " SEA.date <= :dateFin AND (" + // Filtres sur les dates de
                                                  // seances - dependent de
                                                  // l'utilisateur en cours
                " ( " + " SEA.date <= :jourCourant ");
        if (idEnseignant != null) {
            requete1 += " AND SEQ.idEnseignant = :idEnseignant ";
        }
        requete1 += " ) ";
        if (Profil.INSPECTION_ACADEMIQUE == profil) {
            requete1 += (" AND OUV.inspecteur.id = :idInspecteur AND "
                    + " OUV.etablissement.id = :idEtablissement AND"
                    + " OUV.enseignant.id = SEQ.idEnseignant");
        }
        if ((idEnseignant == null) || idEnseignant.equals(idUtilisateur)) {
            requete1 += " OR (  SEQ.idEnseignant = :idUtilisateur  ) ";
        }
        requete1 += " ) ";
        
        if (BooleanUtils.isTrue(rechercheSeancePrintQO.getIsPrintSeance())) {
            requete1 += FIND_LISTE_SEANCE_EDITION_ORDER_BY;
        } else {
            requete1 += FIND_LISTE_SEQUENCE_EDITION_ORDER_BY;
        }
        

        final Query query1 = getEntityManager().createQuery(requete1);
        query1.setParameter("idClasse", idClasse);
        query1.setParameter("dateDebut", rechercheSeancePrintQO.getDateDebut());
        query1.setParameter("dateFin", rechercheSeancePrintQO.getDateFin());
        query1.setParameter("jourCourant", dateCourante);
        if (idEnseignement != null) {
            query1.setParameter("idEnseignement", idEnseignement);
        }
        if (idEnseignant != null) {
            query1.setParameter("idEnseignant", idEnseignant);
        }
        if ((idEnseignant == null) || idEnseignant.equals(idUtilisateur)) {
            query1.setParameter("idUtilisateur", idUtilisateur);
        }
        if (Profil.INSPECTION_ACADEMIQUE == profil) {
            query1.setParameter("idInspecteur", idUtilisateur);
            query1.setParameter("idEtablissement",
                    rechercheSeancePrintQO.getIdEtablissement());
        }
        
        return query1;
    }
    
    
    
    
    /**
     * @param idEnseignant .
     * @param idUtilisateur .
     * @param idEnseignement .
     * @param rechercheSeancePrintQO .
     * @param dateCourante .
     * @param strWhereRequete .
     * @return .
     */
    private Query findListeSeanceEditionHelperGroupe(
            final Integer idEnseignant, final Integer idUtilisateur,
            final Integer idEnseignement, PrintSeanceOuSequenceQO rechercheSeancePrintQO,
            final Date dateCourante, String strWhereRequete) { 
        final Integer idGroupe =
                rechercheSeancePrintQO.getGroupeClasseSelectionne().getId();
           
           // TODO : CDU : revoir cette requete pour gerer le cas d'un enseignant remplacant : different sur seance et sequence mais droit d'inspection sur le remplacant (et pas le remplace qui est positionne sur la sequence)
        
            String requete1 =
                "SELECT " + " distinct new map(" + " GRP.code as codeGroupe, " +
                " GRP.designation as designationGroupe, "
                + " (select c.couleur from " + CouleurEnseignementClasseBean.class.getName() 
                +		" c where c.idEnseignant = SEQ.idEnseignant " 
                +		" and c.idEtablissement = SEQ.idEtablissement " 
                +		" and c.idEnseignement = SEQ.idEnseignement "
                +		" and c.idGroupe = SEQ.idGroupe ) as couleur, " 		
                + CREER_REQUETE_FIND_LISTE_SEANCE_EDITION + " FROM " +
                SequenceBean.class.getName() + " SEQ " +
                " INNER JOIN SEQ.enseignement ENS " +
                " INNER JOIN SEQ.groupe GRP " +
                " LEFT JOIN SEQ.classe CLA " +
                " INNER JOIN SEQ.enseignant TEA, " +
                SeanceBean.class.getName() + " SEA ";

            if (Profil.INSPECTION_ACADEMIQUE.equals(rechercheSeancePrintQO.getProfil())) {
                requete1 += (", " + OuvertureInspecteurBean.class.getName() + " OUV ");
            }

            requete1 += (
                " WHERE " + //Jointure
                " SEA.idSequence = SEQ.id AND " + //Filtres a appliquer dans tous les cas
                " GRP.id = :idGroupe AND " + strWhereRequete +
                " SEA.date >= :dateDebut AND " +
                " SEA.date <= :dateFin AND (" + //Filtres sur les dates de seances - dependent de l'utilisateur en cours
                " ( " + " SEA.date <= :jourCourant "
                        );
            if (idEnseignant != null) {
                requete1 += " AND SEQ.idEnseignant = :idEnseignant ";
            }
            requete1 += " ) ";

            if ((idEnseignant == null) || idEnseignant.equals(idUtilisateur)) {
                requete1 += (
                                " OR " + " ( " + " SEQ.idEnseignant = :idUtilisateur " +
                                " ) "
                            );
            }
            requete1 += ")";

            if (Profil.INSPECTION_ACADEMIQUE.equals(rechercheSeancePrintQO.getProfil())) {
                requete1 += (
                                " AND OUV.inspecteur.id = :idInspecteur AND " +
                                " OUV.etablissement.id = :idEtablissement AND" +
                                " OUV.enseignant.id = SEQ.idEnseignant"
                            );
            }
            
            if (BooleanUtils.isTrue(rechercheSeancePrintQO.getIsPrintSeance())) {
                requete1 += FIND_LISTE_SEANCE_EDITION_ORDER_BY;
            } else {
                requete1 += FIND_LISTE_SEQUENCE_EDITION_ORDER_BY;
            }

            final Query query1 = getEntityManager().createQuery(requete1);
            query1.setParameter("idGroupe", idGroupe);
            query1.setParameter("dateDebut", rechercheSeancePrintQO.getDateDebut());
            query1.setParameter("dateFin", rechercheSeancePrintQO.getDateFin());
            query1.setParameter("jourCourant", dateCourante);
            if (idEnseignement != null) {
                query1.setParameter("idEnseignement", idEnseignement);
            }
            if (idEnseignant != null) {
                query1.setParameter("idEnseignant", idEnseignant);
            }
            if ((idEnseignant == null) || idEnseignant.equals(idUtilisateur)) {
                query1.setParameter("idUtilisateur", idUtilisateur);
            }
            if (Profil.INSPECTION_ACADEMIQUE.equals(rechercheSeancePrintQO.getProfil())) {
                query1.setParameter("idInspecteur", idUtilisateur);
                query1.setParameter("idEtablissement",
                                    rechercheSeancePrintQO.getIdEtablissement());
            }
            
            return query1;
    }
    
    /**
     * @param result r 
     * @param vraiOufauxClasse v
     * @return r
     */
    private PrintSequenceDTO findListeSeanceEditionBuildPrintSequenceDTO(final Map<String, ?> result, Boolean vraiOufauxClasse) {
      //Une nouvelle séquence
        PrintSequenceDTO printSequenceDTO = new PrintSequenceDTO();
        
        
        printSequenceDTO.setDateDebut((Date) result.get("dateDebutSequence"));
        printSequenceDTO.setDateFin((Date) result.get("dateFinSequence"));
        printSequenceDTO.setCode((String) result.get("codeSequence"));
        printSequenceDTO.setIntitule((String) result.get("intituleSequence"));
        printSequenceDTO.setDescription((String) result.get("descriptionSequence"));

        //Classe/Groupe
        
        final String codeClasse = (String) result.get("codeClasse");
        if (StringUtils.isEmpty(codeClasse) || !vraiOufauxClasse) {
            GroupeBean gb = (GroupeBean) result.get("groupe");
            ObjectUtils.copyProperties(printSequenceDTO.getGroupesClassesDTO(), gb);
            printSequenceDTO.getGroupesClassesDTO().setTypeGroupe(TypeGroupe.GROUPE);
        } else {
            ClasseBean cb = (ClasseBean) result.get("classe");
            ObjectUtils.copyProperties(printSequenceDTO.getGroupesClassesDTO(), cb);            
            printSequenceDTO.getGroupesClassesDTO().setTypeGroupe(TypeGroupe.CLASSE);
        }
        
        printSequenceDTO.getEnseignantDTO().setNom((String) result.get("nomEnseignant"));
        printSequenceDTO.getEnseignantDTO().setPrenom((String) result.get("prenomEnseignant"));
        printSequenceDTO.getEnseignantDTO().setCivilite((String) result.get("civiliteEnseignant"));
        
        printSequenceDTO.setDesignationEnseignement((String) result.get("designationEnseignement"));
        printSequenceDTO.setIdEnseignement((Integer) result.get("idEnseignement"));
        printSequenceDTO.setTypeCouleur(TypeCouleur.getTypeCouleurById((String) result.get("couleur")));
        printSequenceDTO.setId((Integer) result.get("idSequence"));
      
        return printSequenceDTO;
    }
    
    
    final private static String FIND_LISTE_SEANCE_EDITION_ORDER_BY = "ORDER BY SEA.date DESC, SEA.id DESC"; 
    
    final private static String FIND_LISTE_SEQUENCE_EDITION_ORDER_BY = "ORDER BY SEQ.intitule ASC, SEA.date DESC, SEA.id DESC";
    
    /**
     * {@inheritDoc}
     * !!Important trier par séances!!
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<PrintSequenceDTO>> findListeSeanceEdition(PrintSeanceOuSequenceQO rechercheSeancePrintQO) {
        final ResultatDTO<List<PrintSequenceDTO>> resultatDTO =
            new ResultatDTO<List<PrintSequenceDTO>>();
        final List<PrintSequenceDTO> listePrintSequenceDTO =
            new ArrayList<PrintSequenceDTO>();

        final Date dateCourante = DateUtils.getAujourdhui();

         Integer idEnseignant = rechercheSeancePrintQO.getEnseignant().getId();
       
        
        final Boolean vraiOufauxClasse =
            rechercheSeancePrintQO.getGroupeClasseSelectionne().getVraiOuFauxClasse();
        final Integer idEnseignement = rechercheSeancePrintQO.getEnseignement().getId();        

        if (idEnseignant == null) {
        	if (rechercheSeancePrintQO.isInArchive() && rechercheSeancePrintQO.getProfil() == Profil.ENSEIGNANT) {
        		idEnseignant = rechercheSeancePrintQO.getIdUtilisateur();
        	}
        }
        
        
        //Filtre sur un enseignement
        String strWhereRequete = "";
        if (idEnseignement != null) {
            strWhereRequete += " ENS.id = :idEnseignement AND ";
        }
        final List<Map<String, ?>> resultatQuery = new ArrayList<Map<String, ?>>();

       
        
        if (vraiOufauxClasse) {
            final Query query1 = findListeSeanceEditionHelperClass(
                    rechercheSeancePrintQO.getGroupeClasseSelectionne().getId(),
                    idEnseignant, rechercheSeancePrintQO.getIdUtilisateur(), 
                    idEnseignement, rechercheSeancePrintQO, dateCourante, 
                    strWhereRequete);

            resultatQuery.addAll(query1.getResultList());
        } else {
            //GROUPE
            final Query query1 = findListeSeanceEditionHelperGroupe(
                    idEnseignant, rechercheSeancePrintQO.getIdUtilisateur(), 
                    idEnseignement, rechercheSeancePrintQO, dateCourante, 
                    strWhereRequete);

            resultatQuery.addAll(query1.getResultList());
        }
        
        PrintSequenceDTO printSequenceDTO = null;

        for (final Map<String, ?> result : resultatQuery) {
            
            String codeSequence = (String) result.get("codeSequence");
            if (printSequenceDTO == null || !printSequenceDTO.getCode().equals(codeSequence)) {
                printSequenceDTO = findListeSeanceEditionBuildPrintSequenceDTO(result, vraiOufauxClasse);
                listePrintSequenceDTO.add(printSequenceDTO);
                
                
            }
             
            final PrintSeanceDTO printSeanceDTO = new PrintSeanceDTO();
            printSequenceDTO.getListeSeances().add(printSeanceDTO);
            printSeanceDTO.setPrintSequenceDTO(printSequenceDTO);
          

            //Seance 
            printSeanceDTO.setId((Integer) result.get("idSeance"));
            printSeanceDTO.setCode((String) result.get("codeSeance"));
            printSeanceDTO.setIntitule((String) result.get("intituleSeance"));
            printSeanceDTO.setDescription((String) result.get("descriptionSeance"));

            printSeanceDTO.setHeureDebut((Integer) result.get("heureDebutSeance"));
            printSeanceDTO.setMinuteDebut((Integer) result.get("minuteDebutSeance"));
            printSeanceDTO.setHeureFin((Integer) result.get("heureFinSeance"));
            printSeanceDTO.setMinuteFin((Integer) result.get("minuteFinSeance"));
            
            printSeanceDTO.getEnseignantDTO().setPrenom((String) result.get("prenomEnseignant"));
            printSeanceDTO.getEnseignantDTO().setNom((String) result.get("nomEnseignant"));
            printSeanceDTO.getEnseignantDTO().setCivilite((String) result.get("civiliteEnseignant"));
            
            printSeanceDTO.setMatiere((String) result.get("designationEnseignement"));
            printSeanceDTO.setDateMaj((Date) result.get("dateMaj"));
            printSeanceDTO.setTypeCouleur(TypeCouleur.getTypeCouleurById((String)result.get("couleur")));
        
            
            
            final Date dateSeance = (Date) result.get("dateSeance");
            final Calendar c = Calendar.getInstance();
            c.setTime(dateSeance);
            c.set(Calendar.HOUR, (Integer) result.get("heureDebutSeance"));
            c.set(Calendar.MINUTE, (Integer) result.get("minuteDebutSeance"));
            printSeanceDTO.setDate(c.getTime());
            findInfoVisa(printSeanceDTO);

            

        }

        resultatDTO.setValeurDTO(listePrintSequenceDTO);

        return resultatDTO;
    }

    /**
     * Constitution de la requete pour trouver les seances en vue de l'édition.
     * @return le debut de la requête.
     */
    private final static String CREER_REQUETE_FIND_LISTE_SEANCE_EDITION = 
            " SEA.id as idSeance, "
            + " SEA.code as codeSeance, "
            + " SEA.date as dateSeance, "
            + " SEA.intitule as intituleSeance, "
            + " SEA.description as descriptionSeance, "
            + " SEA.heureDebut as heureDebutSeance, "
            + " SEA.minuteDebut as minuteDebutSeance, "
            + " SEA.heureFin as heureFinSeance, "
            + " SEA.minuteFin as minuteFinSeance, "
            + " SEA.dateMaj as dateMaj, "
            + " SEQ.id as idSequence, "
            + " SEQ.intitule as intituleSequence, "
            + " SEQ.description as descriptionSequence, "
            + " SEQ.dateDebut as dateDebutSequence, "
            + " SEQ.dateFin as dateFinSequence, "
            + " SEQ.code as codeSequence, SEQ.groupe as groupe, SEQ.classe as classe, "
            + " ENS.designation as designationEnseignement,"
            + " ENS.id as idEnseignement,"
            + " TEA.nom as nomEnseignant ,"
            + " TEA.prenom as prenomEnseignant ,"
            + " TEA.civilite as civiliteEnseignant) ";    
    

    /**
     * @return the emploiHibernateBusinessService
     */
    public EmploiHibernateBusinessService getEmploiHibernateBusinessService() {
        return emploiHibernateBusinessService;
    }

    /**
     * @param emploiHibernateBusinessService the emploiHibernateBusinessService to set
     */
    public void setEmploiHibernateBusinessService(
            EmploiHibernateBusinessService emploiHibernateBusinessService) {
        this.emploiHibernateBusinessService = emploiHibernateBusinessService;
    }
}
