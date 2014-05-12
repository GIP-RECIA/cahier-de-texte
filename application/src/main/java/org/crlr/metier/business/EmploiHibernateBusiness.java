/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.DateDTO;
import org.crlr.dto.application.base.PeriodeEmploiDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.base.TypeReglesAcquittement;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.dto.application.emploi.RechercheEmploiQO;
import org.crlr.dto.application.importEDT.PeriodeEdtQO;
import org.crlr.dto.application.sequence.SaveCouleurEnseignementClasseQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.exception.metier.MetierRuntimeException;
import org.crlr.importEDT.DTO.EmploiDTO;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.entity.CouleurEnseignementClasseBean;
import org.crlr.metier.entity.EmploiBean;
import org.crlr.metier.entity.EtablissementBean;
import org.crlr.metier.entity.PeriodeEmploiBean;
import org.crlr.utils.Assert;
import org.crlr.utils.DateUtils;
import org.crlr.web.dto.TypeCouleur;
import org.crlr.web.dto.TypeSemaine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * EmploiHibernateBusiness.
 * 
 * @author carriere
 * @version $Revision: 1.9 $
 */
@Repository
public class EmploiHibernateBusiness extends AbstractBusiness implements
        EmploiHibernateBusinessService {
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    @Autowired
    private EnseignementHibernateBusinessService enseignementHibernateBusinessService;

    @Autowired
    private CouleurEnseignementClasseHibernateBusinessService couleurEnseignementClasseHibernateBusinessService;
         
    /**
     * Mutateur de enseignementHibernateBusinessService.
     * 
     * @param enseignementHibernateBusinessService
     *            le enseignementHibernateBusinessService à modifier.
     */
    public void setEnseignementHibernateBusinessService(
            EnseignementHibernateBusinessService enseignementHibernateBusinessService) {
        this.enseignementHibernateBusinessService = enseignementHibernateBusinessService;
    }

   

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Date> saveEmploiDuTemps(
            final List<DetailJourEmploiDTO> listeDetailJourEmploiDTO)
            throws MetierException {
        Assert.isNotNull("listeDetailJourEmploiDTO", listeDetailJourEmploiDTO);

        final BaseHibernateBusiness baseHibernateBusiness = new BaseHibernateBusiness(
                this.getEntityManager());

        for (DetailJourEmploiDTO element : listeDetailJourEmploiDTO) {
            final Integer idPrecedent = element.getId();

            final EmploiBean emploiBean;

            // gestion de l'emploi sequence
            if (idPrecedent != null) {

                emploiBean = getEntityManager().find(EmploiBean.class,
                        idPrecedent);
                
                if (emploiBean == null) {
                    throw new MetierException("Erreur : emploi de temps id non valide.");
                }

            } else {
                emploiBean = new EmploiBean();
            }

            try {
                IntegerConverter converter = new IntegerConverter(null);
                BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
                beanUtilsBean.getConvertUtils().register(converter,
                        Integer.class);
                // beanUtilsBean.getConvertUtils().register(new
                // ConvertTypeSemaine(), TypeSemaine.class);

                beanUtilsBean.copyProperties(emploiBean, element);
            } catch (Exception ex) {
                log.error("exception", ex);
            }


            if (emploiBean.getId() == null) {
                // récupère le prochains identifiant
                final Integer id = baseHibernateBusiness
                        .getIdInsertion("cahier_emploi");
                emploiBean.setId(id);

                // Ajout à la base de données
                getEntityManager().persist(emploiBean);
            }
            if (element.getTypeCouleur() != null) {
            	SaveCouleurEnseignementClasseQO scecQO = new SaveCouleurEnseignementClasseQO();	
            	scecQO.setClasseGroupe(element.getGroupeOuClasse());
            	scecQO.setIdEtablissement(element.getIdEtablissement());
            	scecQO.setIdEnseignant(element.getIdEnseignant());
            	scecQO.setIdEnseignement(element.getIdEnseignement());
            	scecQO.setTypeCouleur(element.getTypeCouleur());
            	couleurEnseignementClasseHibernateBusinessService.save(scecQO);
            }
        }

        // Commit et libère les ressources
        getEntityManager().flush();

        final ResultatDTO<Date> resultat = new ResultatDTO<Date>();
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        conteneurMessage
                .add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                        Nature.INFORMATIF, "L'emploi du temps", " sauvegardé"));

        resultat.setConteneurMessage(conteneurMessage);
        resultat.setValeurDTO(DateUtils.getAujourdhui());
        return resultat;
    }

    /**
     * Supprime les enregistrement de l'emploi du temps.
     * 
     * @param listeIdsEmplois
     *            les ids
     */
    public void deleteEmploi(final List<Integer> listeIdsEmplois) {
        if (org.apache.commons.collections.CollectionUtils
                .isEmpty(listeIdsEmplois)) {
            return;
        }
        final String query = "DELETE FROM " + EmploiBean.class.getName()
                + " e WHERE e.id IN (:liste)";
        getEntityManager().createQuery(query).setParameter("liste",
                listeIdsEmplois).executeUpdate();
        getEntityManager().flush();
    }

    /**
     * {@inheritDoc}
     */
    public void deleteEmploiSemaine(final Integer idPeriode, final Integer idEnseignant,
            final Integer idEtablissement, final TypeSemaine typeSemaine) {
        final String query = "DELETE FROM " + EmploiBean.class.getName()
                + " e " + "WHERE " + "   e.typeSemaine     = :typeSemaine AND "
                + "   e.idEnseignant    = :idEnseignant AND "
                + "   e.idEtablissement = :idEtablissement AND " 
                + "   e.idPeriodeEmploi = :idPeriode ";

        getEntityManager().createQuery(query)
            .setParameter("typeSemaine",typeSemaine.getValeur())
            .setParameter("idEtablissement", idEtablissement)
            .setParameter("idEnseignant", idEnseignant)
            .setParameter("idPeriode", idPeriode)
            .executeUpdate();

        getEntityManager().flush();
    }

    /**
     * @author G-CG34-FRMP
     *
     */
    public class ConvertTypeSemaine implements Converter {

        /*
         * (non-Javadoc)
         * 
         * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class,
         * java.lang.Object)
         */
        @SuppressWarnings("rawtypes")
        @Override
        public Object convert(Class arg0, Object arg1) {

            if (arg1 == null) {
                return null;
            }
            if (arg1 instanceof String) {
                return TypeSemaine.getTypeSemaine((String) arg1);
            }

            if (arg1 instanceof Character) {
                return TypeSemaine
                        .getTypeSemaine(((Character) arg1).toString());
            }

            return arg1;
        }

    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<DetailJourEmploiDTO>> findEmploi(
            final Integer idEnseignant, final Integer idEtablissement,
            final TypeSemaine typeSemaine, final Integer idPeriodeEmploi) {

        log.info("METIER findEmploi");

        final ResultatDTO<List<DetailJourEmploiDTO>> resultat = new ResultatDTO<List<DetailJourEmploiDTO>>();

        String query = "SELECT e, "
        		+ " (select c.couleur from  " + CouleurEnseignementClasseBean.class.getName() + " c "
        		+ " where e.idEtablissement = c.idEtablissement "
        		+ " AND e.idEnseignant = c.idEnseignant "
        		+ " AND e.idEnseignement = c.idEnseignement " 
        		+ " AND (c.idGroupe is null OR c.idGroupe = e.idGroupe) " 
        		+ " AND (c.idClasse is null OR c.idClasse = e.idClasse) ) "
                + " FROM "
                + EmploiBean.class.getName()
                + " e WHERE e.idEtablissement = :idEtablissement "
                + ""
                + (typeSemaine != null ? " AND e.typeSemaine = :typeSemaine "
                        : "") + " AND e.idEnseignant = :idEnseignant "
                + " AND e.idPeriodeEmploi = :idPeriodeEmploi "
                + " ORDER BY e.heureDebut ASC, e.minuteDebut ASC";

        Query queryObj = getEntityManager().createQuery(query);

        if (typeSemaine != null) {
            queryObj.setParameter("typeSemaine", typeSemaine.getValeur());
        }

        queryObj = queryObj.setParameter("idEtablissement", idEtablissement)
                .setParameter("idPeriodeEmploi", idPeriodeEmploi).setParameter(
                        "idEnseignant", idEnseignant);

        final List<Object[]> resultatQuery = queryObj.getResultList();

        final List<DetailJourEmploiDTO> listeEmploi = new ArrayList<DetailJourEmploiDTO>();
        for (final Object[] objects : resultatQuery) {
        	EmploiBean result = (EmploiBean) objects[0];
        	String couleur = (String) objects[1];
            final DetailJourEmploiDTO detailJourEmploiDTO = new DetailJourEmploiDTO();

            try {
                IntegerConverter converter = new IntegerConverter(null);
                BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
                beanUtilsBean.getConvertUtils().register(converter,
                        Integer.class);
                beanUtilsBean.getConvertUtils().register(
                        new ConvertTypeSemaine(), TypeSemaine.class);

                beanUtilsBean.copyProperties(detailJourEmploiDTO, result);
            } catch (Exception ex) {
                log.error( "Exception", ex);
            }

            Set<Integer> matierIds = new HashSet<Integer>();
            matierIds.add(result.getEnseignement().getId());

            // Prende en compte régle unique de enseignement

            if (detailJourEmploiDTO.getGroupeOuClasse().getVraiOuFauxClasse()) {
                detailJourEmploiDTO.getGroupeOuClasse().setIntitule(
                        result.getClasse().getDesignation());
            } else {
                detailJourEmploiDTO.getGroupeOuClasse().setIntitule(
                        result.getGroupe().getDesignation());
            }

            // TODO pas efficace
            detailJourEmploiDTO.getMatiere().setLibellePerso(
                    enseignementHibernateBusinessService
                            .findEnseignementPersoEtablissement(
                                    idEtablissement, matierIds).get(
                                    result.getEnseignement().getId()));

            detailJourEmploiDTO.getMatiere().setIntitule(
                    result.getEnseignement().getDesignation());

            if (!StringUtils.isEmpty(couleur)) {
            	detailJourEmploiDTO.setTypeCouleur(TypeCouleur.getTypeCouleurById(couleur));
            }

            detailJourEmploiDTO
                    .setDescriptionCourte(org.apache.commons.lang.StringUtils
                            .abbreviate(detailJourEmploiDTO.getDescription(),
                                    16));

            listeEmploi.add(detailJourEmploiDTO);
        }

        resultat.setValeurDTO(listeEmploi);

        return resultat;
    }

     
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<DetailJourEmploiDTO>> findEmploiConsolidation(
            final RechercheEmploiQO rechercheEmploiQO) {
        Assert.isNotNull("rechercheEmploiQO", rechercheEmploiQO);

        Assert.isNotNull("debut", rechercheEmploiQO.getDateDebut());
        Assert.isNotNull("fin", rechercheEmploiQO.getDateFin());

        final ResultatDTO<List<DetailJourEmploiDTO>> resultat = new ResultatDTO<List<DetailJourEmploiDTO>>();

        final TypeSemaine typeSemaine = rechercheEmploiQO.getTypeSemaine();
        
        final Integer idEtablissement = rechercheEmploiQO.getIdEtablissement();
        final Set<Integer> listeGroupes = rechercheEmploiQO.getListeDeGroupes();

        final List<DetailJourEmploiDTO> listeEvents = new ArrayList<DetailJourEmploiDTO>();

        final GregorianCalendar queryCal = new GregorianCalendar();
        queryCal.setTime(rechercheEmploiQO.getDateDebut());

        final GregorianCalendar queryEndCal = new GregorianCalendar();
        queryEndCal.setTime(rechercheEmploiQO.getDateFin());

        // Construire les résultats jour par jour car chaque jour peut être
        // lié à des périodes distinctes
        while (queryCal.compareTo(queryEndCal) <= 0) {
        	try {
            final Date queryDate = queryCal.getTime();
            log.debug("Query date {}", DateUtils.format(queryDate, "yyyy-MM-dd"));

            final TypeJour jour = TypeJour.getTypeJour(queryCal.get(Calendar.DAY_OF_WEEK));

            // Construction de la requete
            String query = "SELECT DISTINCT new Map("
                    + "   e.id as id, "
                    + "   e.jour as jour, "
                    + "   e.heureDebut as hd, "
                    + "   e.heureFin as hf, "
                    + "   e.minuteDebut as md, "
                    + "   e.minuteFin as mf, "
                    + "   e.idEnseignement as idEnseignement, "
                    + "   e.description as desc, "
                    + "   e.codeSalle as codeSalle,"
                    + "   (select couleur from CouleurEnseignementClasseBean cl " 
                    +  " where e.etablissement.id = cl.idEtablissement " 
                    +  " AND e.enseignant.id = cl.idEnseignant " 
                    +  " AND e.enseignement.id = cl.idEnseignement " 
                    +  " AND (cl.idGroupe is null OR cl.idGroupe = e.idGroupe) " 
                    +  " AND (cl.idClasse is null OR cl.idClasse = e.idClasse) "
                    +  "  ) as couleurCase,"
                    + "   en.civilite as civ, "
                    + "   en.nom as nom, "
                    + "   en.prenom as prenom, "
                    + "   mat.designation as matiere, "
                    + "   c.designation as classe, c.id as idClasse, "
                    + "   g.designation as groupe, g.id as idGroupe, " 
                    + "   e.idEnseignant as idEnseignant ) "
                    + " FROM "
                    + EmploiBean.class.getName()
                    + " e "
                    + "   INNER JOIN e.enseignant en "
                    + "   INNER JOIN e.enseignement mat "
                    + "   INNER JOIN e.periode p "
                    + "   LEFT JOIN e.groupe g "
                    + "   LEFT JOIN e.classe c "
                    + " WHERE "
                    + "   e.typeSemaine = :typeSemaine AND "
                    +
                    // Sous requête pour chercher la bonne période, la dérniere
                    // période qui est moins ou égale de jourDate
                    "   p.id IN "
                    + "(Select pebOut.id FROM "
                    + PeriodeEmploiBean.class.getName()
                    + " pebOut where pebOut.dateDebut = (Select MAX(peb.dateDebut) FROM "
                    + PeriodeEmploiBean.class.getName()
                    + " peb WHERE "
                    + "peb.idEtablissement = :idEtablissement AND peb.idEnseignant = pebOut.idEnseignant AND "
                    + " peb.dateDebut <= :jourDate ) )"
                    +
                    // Fini sous requête
                    " AND " + "   e.jour = :jour AND "
                    + "   e.idEtablissement = :idEtablissement";
            
            if (rechercheEmploiQO.getIdEnseignant() != null) {
                query += " AND e.idEnseignant = :idEnseignant";
            }
            
            if (rechercheEmploiQO.getVraiOuFauxClasse() != null) {
                if (Boolean.TRUE.equals(rechercheEmploiQO.getVraiOuFauxClasse())) {
                    query += " AND (e.idClasse = :idClasse";
    
                    // cas pour les élèves et les parents (onrecherche dans la
                    // classe de l'élève et ses groupes)
                    if (!CollectionUtils.isEmpty(listeGroupes)) {
                        query += " OR e.idGroupe IN (:listeGroupes))";
                    } else {
                        query += ")";
                    }
                } else {
                    query += " AND e.idGroupe = :idGroupe";
                }
            }

            query += " ORDER BY e.heureDebut ASC, e.minuteDebut ASC";

            final Query queryObject = getEntityManager().createQuery(query);
            final Character typeSemaineChar = typeSemaine.getValeur();
            queryObject
                .setParameter("typeSemaine", typeSemaineChar)
                .setParameter("idEtablissement", idEtablissement)
                .setParameter("jourDate", queryDate)
                .setParameter("jour", jour);

            // Ajoute les paramètres
            if (rechercheEmploiQO.getIdEnseignant() != null) {
                queryObject.setParameter("idEnseignant", rechercheEmploiQO.getIdEnseignant());
            }
            
            if (rechercheEmploiQO.getVraiOuFauxClasse() != null) {
                if (Boolean.TRUE.equals(rechercheEmploiQO.getVraiOuFauxClasse())) {
                    queryObject.setParameter("idClasse", rechercheEmploiQO.getIdGroupeOuClasse());
    
                    if (!CollectionUtils.isEmpty(listeGroupes)) {
                        queryObject.setParameter("listeGroupes", listeGroupes);
                    }
                } else {
                    queryObject.setParameter("idGroupe", rechercheEmploiQO.getIdGroupeOuClasse());
                }
            }

            // execution de la requete.
            final List<Map<String, ?>> resultatQuery = queryObject
                    .getResultList();
            final List<DetailJourEmploiDTO> celluleDetailJour = new ArrayList<DetailJourEmploiDTO>();

            if (!CollectionUtils.isEmpty(resultatQuery)) {
                // Map des désignations d'enseignement
                final Map<Integer, String> mapIdDesignation = org.crlr.utils.CollectionUtils
                        .formatMapClefValeur(resultatQuery, "idEnseignement",
                                "matiere");

                // recherche des libellés perso d'enseignement.
                final Map<Integer, String> mapEnseignementPerso = enseignementHibernateBusinessService
                        .findEnseignementPersoEtablissement(idEtablissement,
                                mapIdDesignation.keySet());

                for (final Map<String, ?> mapResult : resultatQuery) {
                    final DetailJourEmploiDTO detailJourEmploiDTO = new DetailJourEmploiDTO();
                    detailJourEmploiDTO.setId((Integer) mapResult.get("id"));
                    detailJourEmploiDTO.setJour((TypeJour) mapResult.get("jour"));
                    detailJourEmploiDTO.setHeureDebut((Integer) mapResult.get("hd"));
                    detailJourEmploiDTO.setHeureFin((Integer) mapResult.get("hf"));
                    detailJourEmploiDTO.setMinuteDebut((Integer) mapResult.get("md"));
                    detailJourEmploiDTO.setMinuteFin((Integer) mapResult.get("mf"));
                    final String classe = (String) mapResult.get("classe");
                    final String groupe = (String) mapResult.get("groupe");
                    if (!org.apache.commons.lang.StringUtils.isEmpty(groupe)) {
                        detailJourEmploiDTO.getGroupeOuClasse().setIntitule(groupe);
                        detailJourEmploiDTO.getGroupeOuClasse().setId((Integer) mapResult.get("idGroupe"));
                        detailJourEmploiDTO.getGroupeOuClasse().setTypeGroupe(TypeGroupe.GROUPE);
                    } else {
                        detailJourEmploiDTO.getGroupeOuClasse().setIntitule(classe);
                        detailJourEmploiDTO.getGroupeOuClasse().setId((Integer) mapResult.get("idClasse"));
                        detailJourEmploiDTO.getGroupeOuClasse().setTypeGroupe(TypeGroupe.CLASSE);
                    }

                    detailJourEmploiDTO.setDescription((String) mapResult.get("desc"));
                    detailJourEmploiDTO.setCodeSalle((String) mapResult.get("codeSalle"));
                    detailJourEmploiDTO.setTypeSemaine(typeSemaine);
                    detailJourEmploiDTO.setIdEtablissement(idEtablissement);
                    detailJourEmploiDTO.setIdEnseignant((Integer) mapResult.get("idEnseignant"));

                    final String civNomPrenom = StringUtils
                            .trimToEmpty(StringUtils
                                    .trimToEmpty((String) mapResult.get("civ"))
                                    + " "
                                    + StringUtils
                                            .trimToEmpty((String) mapResult
                                                    .get("nom"))
                                    + " "
                                    + StringUtils
                                            .trimToEmpty((String) mapResult
                                                    .get("prenom")));
                    detailJourEmploiDTO.setCiviliteNomPrenom(civNomPrenom);

                    final Integer idEnseignement = (Integer) mapResult.get("idEnseignement");
                    detailJourEmploiDTO.getMatiere().setIntitule((String) ObjectUtils.defaultIfNull(
                                    mapEnseignementPerso.get(idEnseignement),
                                    mapIdDesignation.get(idEnseignement)));
                    detailJourEmploiDTO.getMatiere().setId(idEnseignement);
                    
                    final String idCouleur = (String) mapResult.get("couleurCase");
                    if (!StringUtils.isEmpty(idCouleur)) {
                        detailJourEmploiDTO.setTypeCouleur(TypeCouleur
                                .getTypeCouleurById(idCouleur));
                    }
                    
                    celluleDetailJour.add(detailJourEmploiDTO);
                }
            }

            log.debug("Ajouter {} résultats", celluleDetailJour.size());
            listeEvents.addAll(celluleDetailJour);

            queryCal.add(Calendar.DATE, 1);
        	} catch (Exception e) {
                  	log.error("Exception : {}", e);
              }
        }

        resultat.setValeurDTO(listeEvents);

        return resultat;
    }

    /*
     * Retourne un id d'enseignement.
     * 
     * @param idEnseignement l'id de l'enseignement
     * 
     * @param idClasse l'id de la classe
     * 
     * @param idGroupe l'id du groupe
     * 
     * @param idEnseignant l'id de l'enseignant.
     * 
     * @return Integer un identifiant de séquence
     */
    /*
     * @SuppressWarnings("unchecked") private Integer findSequenceId(final
     * Integer idEnseignement, final Integer idClasse, final Integer idGroupe,
     * final Integer idEnseignant) { final String query =
     * "SELECT e.pk.idSequence" + " FROM " +
     * EnseignementsSequencesBean.class.getName() +
     * " e INNER JOIN e.sequence seq WHERE e.pk.idEnseignement = :idEnseignement"
     * + " AND seq.idEnseignant = :idEnseignant AND (e.pk.idGroupe = :idGroupe"
     * + " OR e.pk.idClasse = :idClasse)";
     * 
     * final List<Integer> listeOfId = getEntityManager().createQuery(query)
     * .setParameter("idEnseignement", idEnseignement) .setParameter("idGroupe",
     * idGroupe).setParameter("idClasse", idClasse)
     * .setParameter("idEnseignant", idEnseignant).getResultList();
     * 
     * if (!CollectionUtils.isEmpty(listeOfId) && (listeOfId.size() == 1)) {
     * return listeOfId.get(0); } else { return null; } }
     */

    /**
     * {@inheritDoc}
     */
    @Deprecated
    public void saveSequenceEmploi(final Integer idSequence,
            final Integer idEtablissement, final Integer idEnseignant,
            final Integer idEnseignement, final Integer idGroupe,
            final Integer idClasse) {
        Assert.isNotNull("idSequence", idSequence);
        Assert.isNotNull("idEtablissement", idEtablissement);
        Assert.isNotNull("idEnseignant", idEnseignant);
        Assert.isNotNull("idEnseignement", idEnseignement);

        final Integer idGroupeDefinitif = (idGroupe == null) ? (-1) : idGroupe;
        final Integer idClasseDefinitif = (idClasse == null) ? (-1) : idClasse;

        // sauvegarde l'id de la sequence dans l'emploi du temps s'il existe
        final String query = "UPDATE "
                + EmploiBean.class.getName()
                + " e SET e.idSequence=:idSequ WHERE e.idEtablissement = :idEtablissement "
                + " AND e.idEnseignant = :idEnseignant AND e.idEnseignement=:idEnseignement AND"
                + " (e.idClasse=:idClasse or e.idGroupe=:idGroupe)";

        getEntityManager().createQuery(query)
                .setParameter("idSequ", idSequence).setParameter(
                        "idEtablissement", idEtablissement).setParameter(
                        "idEnseignant", idEnseignant).setParameter(
                        "idEnseignement", idEnseignement).setParameter(
                        "idGroupe", idGroupeDefinitif).setParameter("idClasse",
                        idClasseDefinitif).executeUpdate();
    }

    /**
     * Recherche des plages d'emplois du temps en fonction d'identifiant.
     * 
     * @param listeIdEmploi
     *            la liste des identifiants.
     * @return la map (clef id valeur ids classe groupe et enseignements).
     */
    @SuppressWarnings("unchecked")
    public Map<Integer, DetailJourEmploiDTO> findPlageEmploiTemps(
            final List<Integer> listeIdEmploi) {
        final Map<Integer, DetailJourEmploiDTO> mapResultat = new HashMap<Integer, DetailJourEmploiDTO>();

        if (!CollectionUtils.isEmpty(listeIdEmploi)) {
            final String query = "SELECT new Map(e.id as id, e.idClasse as idClasse, e.idGroupe as idGroupe, "
                    + " e.idEnseignement as idEnseignement) "
                    + " FROM "
                    + EmploiBean.class.getName() + " e WHERE e.id IN (:ids) ";

            final List<Map<String, ?>> resultatQuery = getEntityManager()
                    .createQuery(query).setParameter("ids", listeIdEmploi)
                    .getResultList();

            for (final Map<String, ?> map : resultatQuery) {
                final DetailJourEmploiDTO detailJourEmploiDTO = new DetailJourEmploiDTO();
                detailJourEmploiDTO.setIdClasse((Integer) map.get("idClasse"));
                detailJourEmploiDTO.setIdEnseignement((Integer) map
                        .get("idEnseignement"));
                detailJourEmploiDTO.setIdGroupe((Integer) map.get("idGroupe"));

                mapResultat.put((Integer) map.get("id"), detailJourEmploiDTO);
            }
        }

        return mapResultat;
    }

    /**
     * {@inheritDoc}
     */
    public void saveCasesEmploiSansFusion(List<EmploiDTO> mesEmplois, Date dateDebut) {
        final BaseHibernateBusiness baseHibernateBusiness = new BaseHibernateBusiness(this.getEntityManager());
        
        // Liste des periode EDT
        final Map<Integer,Integer> mapPeriode = new HashMap<Integer, Integer>();
        
        // Reserve 50 id de case EDT et autant pour les periodes
        int compteur = 0;
        int idCase = baseHibernateBusiness.getReservationIdInsertion("cahier_emploi", 50);
        int compteurPeriode = 0;
        int idNextPeriode = baseHibernateBusiness.getReservationIdInsertion("cahier_periode_emploi", 50);
        
        // Creation des case EDT
        for (EmploiDTO emploiDTO : mesEmplois) {
            
            // Creation de la periode si necessaire
            Integer idPeriode = mapPeriode.get(emploiDTO.getIdEnseignant());
            if (idPeriode == null) {
                idPeriode = idNextPeriode;
                final PeriodeEmploiBean periode = new PeriodeEmploiBean();
                periode.setDateDebut(dateDebut);
                periode.setIdEnseignant(emploiDTO.getIdEnseignant());
                periode.setIdEtablissement(emploiDTO.getIdEtablissement());
                periode.setId(idPeriode);
                getEntityManager().persist(periode);
                mapPeriode.put(emploiDTO.getIdEnseignant(), periode.getId());
                
                idNextPeriode++;
                compteurPeriode++;
                
                // Reserve 50 id de periodes supplementaire
                if (compteurPeriode == 50) {
                    compteurPeriode = 0;
                    idNextPeriode = baseHibernateBusiness.getReservationIdInsertion("cahier_periode_emploi", 50);
                }                
            } else {
                log.debug("Periode existe pour enseignant {0}", emploiDTO.getIdEnseignant());
            }
            
            // Creation de la case EDT
            final EmploiBean emploiSequenceBean = new EmploiBean();
            emploiSequenceBean.setHeureDebut(Integer.valueOf(emploiDTO.getHeureDebut()));
            emploiSequenceBean.setHeureFin(Integer.valueOf(emploiDTO.getHeureFin()));
            emploiSequenceBean.setMinuteDebut(Integer.valueOf(emploiDTO.getMinuteDebut()));
            emploiSequenceBean.setMinuteFin(Integer.valueOf(emploiDTO.getMinuteFin()));
            emploiSequenceBean.setIdEnseignant(emploiDTO.getIdEnseignant());
            emploiSequenceBean.setIdEnseignement(emploiDTO.getIdEnseignement());
            emploiSequenceBean.setIdEtablissement(emploiDTO.getIdEtablissement());
            emploiSequenceBean.setJour(TypeJour.valueOf(emploiDTO.getJour()));
            emploiSequenceBean.setTypeSemaine(emploiDTO.getTypeSemaine().getValeur());
            emploiSequenceBean.setCodeSalle(emploiDTO.getCodeSalle());
            emploiSequenceBean.setIdPeriodeEmploi(idPeriode);
            if (emploiDTO.getIdClasse() != null) {
                emploiSequenceBean.setIdClasse(emploiDTO.getIdClasse());
            } else if (emploiDTO.getIdGroupe() != null) {
                emploiSequenceBean.setIdGroupe(emploiDTO.getIdGroupe());
            }
            if (emploiSequenceBean.getId() == null) {
                // récupère le prochain identifiant
                emploiSequenceBean.setId(idCase);
            }
            getEntityManager().persist(emploiSequenceBean); // Ajout à la base
                                                            // de données
            compteur++;
            idCase++;
            
            // Flush du paquet de 50 case et reserve les 50 id de case EDT suivantes
            if (compteur == 50) {
                getEntityManager().flush();
                compteur = 0;
                idCase = baseHibernateBusiness.getReservationIdInsertion("cahier_emploi", 50);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void deleteEmploiDuTempsEtablissement(final PeriodeEdtQO periodeEdtQO) {
        Assert.isNotNull("deleteEDTQO", periodeEdtQO);
        Assert.isNotNull("deleteEDTQO.dateDebut", periodeEdtQO.getDateDebut());
        Assert.isNotNull("deleteEDTQO.idEtablissement", periodeEdtQO.getIdEtablissement());
        
        String queryDelete =  
                "delete from cahier_courant.cahier_emploi " + 
                "using cahier_courant.cahier_periode_emploi p " +  
                "where cahier_courant.cahier_emploi.id_periode_emploi = p.id " + 
                "and p.id_etablissement = ? " + 
                "and p.date_debut= ? ";
                
        getEntityManager().createNativeQuery(queryDelete)
            .setParameter(1, periodeEdtQO.getIdEtablissement())
            .setParameter(2, periodeEdtQO.getDateDebut())
            .executeUpdate();
        
        queryDelete = 
            "DELETE FROM " + PeriodeEmploiBean.class.getName() + " p " +
            "WHERE p.dateDebut = :dateDebut and p.idEtablissement = :idEtablissement";
    
        getEntityManager().createQuery(queryDelete)
            .setParameter("dateDebut", periodeEdtQO.getDateDebut())
            .setParameter("idEtablissement", periodeEdtQO.getIdEtablissement())
            .executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    public Integer checkNombreCaseEmploiPourEtablissement(PeriodeEdtQO periodeEdtQO) {
        final String query = "SELECT count(*) FROM "
                + EmploiBean.class.getName() + " e "  
                + " INNER JOIN e.periode p "
                + " WHERE e.idEtablissement = :idEtablissement " 
                + " AND   p.dateDebut = :dateDebut ";
        
        final Integer listeNombreDeResultat = Integer
                .valueOf(getEntityManager().createQuery(query)
                        .setParameter("idEtablissement", periodeEdtQO.getIdEtablissement())
                        .setParameter("dateDebut", periodeEdtQO.getDateDebut())
                        .getSingleResult()
                        .toString());
        return listeNombreDeResultat;
    }

    /** 
     * @param periode p
     */
    public Boolean existePeriode(PeriodeEmploiDTO periode) {
        if (null == periode) {
            return null;
        }

        final String query = "Select peb.id FROM "
                + PeriodeEmploiBean.class.getName()
                + " peb WHERE peb.idEtablissement = :idEtablissement AND"
                + " peb.idEnseignant = :idEnseignant AND "
                + " peb.dateDebut = :dateDebut ";

        @SuppressWarnings("unchecked")
        List<Integer> listeBeans = getEntityManager().createQuery(query)
                .setParameter("idEtablissement", periode.getIdEtablissement())
                .setParameter("idEnseignant", periode.getIdEnseignant())
                .setParameter("dateDebut", periode.getDateDebut())
                .getResultList();

        return listeBeans.size() > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.crlr.metier.business.EmploiHibernateBusinessService#findPeriodes(
     * java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<PeriodeEmploiDTO> findPeriodes(Integer idEnseignant,
            Integer idEtablissement) {

        log.info("findPeriodes");
        final String query = "Select peb FROM "
                + PeriodeEmploiBean.class.getName()
                + " peb WHERE idEtablissement = :idEtablissement AND"
                + " idEnseignant = :idEnseignant ORDER BY peb.dateDebut";

        @SuppressWarnings("unchecked")
        List<PeriodeEmploiBean> listeBeans = getEntityManager().createQuery(
                query).setParameter("idEtablissement", idEtablissement)
                .setParameter("idEnseignant", idEnseignant).getResultList();

        List<PeriodeEmploiDTO> listeDto = new ArrayList<PeriodeEmploiDTO>();

        for (PeriodeEmploiBean bean : listeBeans) {
            PeriodeEmploiDTO dto = new PeriodeEmploiDTO();
            try {
                BeanUtils.copyProperties(dto, bean);
            } catch (InvocationTargetException ex) {
                log.error( "findPeriodes", ex);
            } catch (IllegalAccessException ex) {
                log.error( "findPeriodes", ex);
            }
            listeDto.add(dto);
        }

        return listeDto;
    }

   
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.crlr.metier.business.EmploiHibernateBusinessService#savePeriode(org
     * .crlr.dto.application.base.PeriodeEmploiDTO)
     */
    @Override
    public void creerPeriode(PeriodeEmploiDTO periode, Integer initPeriodeId)
            throws MetierException {

        PeriodeEmploiBean bean = new PeriodeEmploiBean();

        try {
            BeanUtils.copyProperties(bean, periode);
        } catch (Exception ex) {
            log.error( "Exception", ex);
        }

        final BaseHibernateBusiness baseHibernateBusiness = new BaseHibernateBusiness(
                this.getEntityManager());

        final Integer idPeriode = baseHibernateBusiness
                .getIdInsertion("cahier_periode_emploi");

        bean.setId(idPeriode);

        getEntityManager().persist(bean);

        if (initPeriodeId != null) {
            List<DetailJourEmploiDTO> liste = findEmploi(
                    periode.getIdEnseignant(), periode.getIdEtablissement(),
                    null, initPeriodeId).getValeurDTO();

            for (DetailJourEmploiDTO jour : liste) {
                jour.setIdPeriodeEmploi(idPeriode);
                jour.setId(null);
            }

            saveEmploiDuTemps(liste);
        }

        getEntityManager().flush();
        log.info("savePeriode");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.crlr.metier.business.EmploiHibernateBusinessService#deletePeriode
     * (org.crlr.dto.application.base.PeriodeEmploiDTO)
     */
    @Override
    public void deletePeriode(PeriodeEmploiDTO periode) {
        Assert.isNotNull("id", periode.getId());

        log.info("deletePeriode " + periode.getId());

        final String queryEmploi = "DELETE FROM " + EmploiBean.class.getName()
                + " emp WHERE emp.idPeriodeEmploi = :idPeriode ";

        getEntityManager().createQuery(queryEmploi).setParameter("idPeriode",
                periode.getId()).executeUpdate();

        final String query = "DELETE FROM " + PeriodeEmploiBean.class.getName()
                + " pe WHERE pe.id = :idPeriode)";
        getEntityManager().createQuery(query).setParameter("idPeriode",
                periode.getId()).executeUpdate();
        getEntityManager().flush();

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
     * Recherhe pour la periode selectionnee, les jours pour lesquels l'enseignement
     * est dispense aux eleves de la classe/groupe par l'enseignant.
     * @param idPeriode : periode consultée
     * @param rechercheEmploi : spécifie :
     *  - enseignant
     *  - enseignement
     *  - classe/groupe
     * @return uen liste de string au format : JOUR-TYPE_SEMAINE 
     * - ex avec alternance : "LUNDI-1", "MARDI-2"
     * - ex sans alternance : "LUNDI", "JEUDI" 
     * 
     */
    private List<String> findJourEnseignementDispense(final Integer idPeriode, final RechercheEmploiQO rechercheEmploi) {
        String query = "Select edt FROM "
                + EmploiBean.class.getName() +" edt "
                + " WHERE idEtablissement = :idEtablissement "
                + " AND   idEnseignant    = :idEnseignant " 
                + " AND   idPeriodeEmploi = :idPeriode " 
                + " AND   idEnseignement  = :idEnseignement ";
        
        if (rechercheEmploi.getVraiOuFauxClasse()) {
            query += " AND idClasse = :idClasseGroupe ";
        } else {
            query += " AND idGroupe = :idClasseGroupe ";
        }
        
        @SuppressWarnings("unchecked")
        final
        List<EmploiBean> listeBeans = getEntityManager().createQuery(query)
            .setParameter("idEtablissement", rechercheEmploi.getIdEtablissement())
            .setParameter("idEnseignant", rechercheEmploi.getIdEnseignant())
            .setParameter("idPeriode", idPeriode)
            .setParameter("idEnseignement", rechercheEmploi.getIdEnseignement())
            .setParameter("idClasseGroupe", rechercheEmploi.getIdGroupeOuClasse())
            .getResultList();

        final List<String> listeResultat = new ArrayList<String>();

        for (EmploiBean bean : listeBeans) {
            String jourSemaine = bean.getJour().name();
            if (bean.getTypeSemaine() != null) {
                jourSemaine += "-" + bean.getTypeSemaine();
            } else {
                jourSemaine += "-" + TypeSemaine.NEUTRE.getValeur();
            }
            listeResultat.add(jourSemaine);
        }

        return listeResultat;        
    }

    /**
     * Recupere l'aleternance de semaine pour l'etablissement.
     * @param idEtablissement etablissement
     * @return une chaine.
     */
    @SuppressWarnings("unchecked")
    public String findAlternanceSemaine(final Integer idEtablissement) {
        Assert.isNotNull("idEtablissement", idEtablissement);

        final String query =
            "SELECT e.alternanceSemaine FROM " + EtablissementBean.class.getName() +
            " e WHERE e.id = :idEtablissement";

        final List<String> liste =
            getEntityManager().createQuery(query)
                .setParameter("idEtablissement", idEtablissement).getResultList();

        if (!CollectionUtils.isEmpty(liste) && (liste.size() == 1)) {
            return liste.get(0);
        } else {
            throw new MetierRuntimeException(new ConteneurMessage(),
                                             "L'établissement identifié par {0} n'existe pas.",
                                             idEtablissement);
        }
    }    

    /**
     * Determine le type de semaine correspondant à la date du jour passé.
     * @param jour la date testée.
     * @param alternanceSemaine la map d'alternance des semaines.
     * @return si alternance : "-1" ou "-2" 
     * sinon "" 
     */
    private String calculerTypeSemaine(final Date jour, final Map<Integer, TypeSemaine> alternanceSemaine) {
        final Integer numSemaine = DateUtils.getChamp(jour, Calendar.WEEK_OF_YEAR);
        if (alternanceSemaine.containsKey(numSemaine)) {
            return "-" + alternanceSemaine.get(numSemaine).getValeur();
        } else {
            return "-" + TypeSemaine.NEUTRE.getValeur();
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<DateDTO> findProchaineDate(RechercheEmploiQO rechercheEmploi) {
        
        Assert.isNotNull("rechercheEmploi", rechercheEmploi);
        Assert.isNotNull("getDateDebut", rechercheEmploi.getDateDebut());
        Assert.isNotNull("getDateFin", rechercheEmploi.getDateFin());
        Assert.isNotNull("getIdEnseignant", rechercheEmploi.getIdEnseignant());
        Assert.isNotNull("getIdEtablissement", rechercheEmploi.getIdEtablissement());
        Assert.isNotNull("getIdGroupeOuClasse", rechercheEmploi.getIdGroupeOuClasse());
        Assert.isNotNull("getVraiOuFauxClasse", rechercheEmploi.getVraiOuFauxClasse());
        
        // Intialise
        final List<DateDTO> resultat = new ArrayList<DateDTO>();
        Integer nbrResult = 0;
        
        // Charge la liste des périodes à partir de la date de début
        final Integer idEtablissement = rechercheEmploi.getIdEtablissement();
        final Integer idEnseignant = rechercheEmploi.getIdEnseignant();
        final Date dateDebut = rechercheEmploi.getDateDebut();
        final List<PeriodeEmploiDTO> listePeriode = findPeriodes(idEnseignant, idEtablissement);
        
        // Charge le paramétrage de l'etablissement pour l'aleternance des semaines
        final String alternanceSemaine = findAlternanceSemaine(rechercheEmploi.getIdEtablissement());
        final Map<Integer, TypeSemaine> alternance = GenerateurDTO.generateAlternanceSemaineFromDB(alternanceSemaine);
        
        // Date de fin d'annee scolaire
        final Date dateFin = rechercheEmploi.getDateFin();
        
        // Indique le jour qui est en cours de traitement
        Date dateCourant = dateDebut;
        boolean premierPeriodeTraitee = false;
            
        // Boucle sur chaque période 
        for (Integer i=0; i<listePeriode.size(); i++) {
            final PeriodeEmploiDTO periode = listePeriode.get(i);
            
            // Determine la date de fin de la periode
            Date dateFinPeriode;
            if (i<listePeriode.size() -1) {
                final PeriodeEmploiDTO periodeSuiv = listePeriode.get(i+1);
                dateFinPeriode = periodeSuiv.getDateDebut();
            } else {
                dateFinPeriode = dateFin;
            }
            
            // Si la date de fin de la periode est avant le premier jour, on passe a la periode suivante. 
            if (dateCourant.compareTo(dateFinPeriode) > 0) {
                continue;
            }
            
            // Initialise le jour courant pour la periode à traiter
            if (!premierPeriodeTraitee) {
                premierPeriodeTraitee = true;
            } else {
                dateCourant = periode.getDateDebut();
            }
            
            // Charge la liste des <JOUR-Semaine> qui dispense l'enseignement pour cette classe
            final List<String> listeJourDispense = findJourEnseignementDispense(periode.getId(), rechercheEmploi);
            
            // Boucle sur chaque jour de la période pour savoir si un jour correspond dans l'emploi du temps
            while (dateCourant.compareTo(dateFinPeriode) < 0) {
                
                // Determine la semaine correspondant au jour courant
                final String cleJour = getTypeJour(dateCourant) + calculerTypeSemaine(dateCourant, alternance);
                
                // Verifie si le jour courant fait parti de l'emploi du temps
                if (listeJourDispense.contains(cleJour)) {
                    resultat.add(new DateDTO(dateCourant));
                    nbrResult++;
                    
                    // On s'arrete si on atteint 10 dates 
                    if (nbrResult >= 10) {
                        return resultat;
                    }
                }
                
                // Passe au jour suivant
                dateCourant = DateUtils.getDatePlus1(dateCourant);
            }
        }
        
        // Valeur de retour
        return resultat;
    }
    
    public void setCouleurEnseignementClasseHibernateBusinessService(
    			CouleurEnseignementClasseHibernateBusinessService couleurEnseignementClasseHibernateBusinessService) {
    		this.couleurEnseignementClasseHibernateBusinessService = couleurEnseignementClasseHibernateBusinessService;
    }
}
