/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EtablissementAccessibleQO;
import org.crlr.dto.application.base.EtablissementComplementDTO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.GenericDetailDTO;
import org.crlr.dto.application.base.OuvertureQO;
import org.crlr.dto.application.base.SaisieSimplifieeDTO;
import org.crlr.dto.application.base.SaisieSimplifieeQO;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.base.TypeReglesAcquittement;
import org.crlr.dto.application.base.TypeReglesAdmin;
import org.crlr.exception.metier.MetierException;
import org.crlr.exception.metier.MetierRuntimeException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.entity.EtablissementBean;
import org.crlr.metier.entity.EtablissementsEnseignantsBean;
import org.crlr.metier.utils.SchemaUtils;
import org.crlr.utils.Assert;
import org.crlr.utils.BooleanUtils;
import org.crlr.utils.ObjectUtils;
import org.springframework.stereotype.Repository;

/**
 * EtablissementHibernateBusiness.
 *
 * @author vibertd
 * @version $Revision: 1.12 $
 */
@Repository
public class EtablissementHibernateBusiness extends AbstractBusiness
    implements EtablissementHibernateBusinessService {
    /** Les jours ouvrés par défaut. */
    private static final String JOURS_OUVRES_DEFAUT =
        TypeJour.LUNDI.name() + "|" + TypeJour.MARDI.name() + "|" +
        TypeJour.MERCREDI.name() + "|" + TypeJour.JEUDI.name() + "|" +
        TypeJour.VENDREDI.name();

    /** Grille horaire par défault. */
    private static final String GRILLE_HORAIRE_DEFAUT =
        "08:00#09:00|09:00#10:00|10:00#11:00|11:00#12:00|" +
        "12:00#13:00|13:00#14:00|14:00#15:00|15:00#16:00|" +
        "16:00#17:00|17:00#18:00|";
    
    public static final int HEURE_DEBUT_DEFAUT = 8;
    public static final int HEURE_FIN_DEFAUT = 18;
    
    public static final int DUREE_COURS_DEFAULT = 60;

    /** code siren de l'établissement personnel non rattaché. */
    private static final String SIREN_NON_RATTACH = "00000000000000";

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public GenericDetailDTO<Integer, String, String, Boolean> findIdDescJourOuvreEtablissementParCode(final String siren,
                                                                                                      final Boolean archive,
                                                                                                      final String exercice) {
        Assert.isNotEmpty("siren", siren);

        final String schemaUtilise =
            SchemaUtils.getSchemaCourantOuArchive(archive, exercice);

        final String query =
            "SELECT e.id, e.designation, e.jours_ouvres, e.ouverture FROM " +
            SchemaUtils.getTableAvecSchema(schemaUtilise,
                                           "cahier_etablissement") +
            " e WHERE e.code = ?";

        final List<Object[]> liste =
            getEntityManager().createNativeQuery(query).setParameter(1, siren)
                .getResultList();

        if (!CollectionUtils.isEmpty(liste) && (liste.size() == 1)) {
            final Object[] result = liste.get(0);
            String joursOuvres = (String) result[2];

            //si les jours ouvrés sont vides on considère que la semaine est de 5 jours. 
            if (org.apache.commons.lang.StringUtils.isEmpty(joursOuvres)) {
                joursOuvres = JOURS_OUVRES_DEFAUT;
            }

            return new GenericDetailDTO<Integer, String, String, Boolean>((Integer) result[0],
                                                                          (String) result[1],
                                                                          joursOuvres,
                                                                          (Boolean) result[3]);
        } else {
            // Dans le cadre d'une archive l'etablissement pouvait ne pas existe dans les années 
            // passées. Cela ne doit pas provoquer d'exception.
            if (archive) {
                return null;
            } else {
                throw new MetierRuntimeException(new ConteneurMessage(),
                                             "L'établissement {0} n'existe pas.", siren);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public EtablissementComplementDTO findDonneeComplementaireEtablissement(final Integer id) {
        Assert.isNotNull("id", id);

        final EtablissementComplementDTO etablissementComplementDTO =
            new EtablissementComplementDTO();

        final String query =
            "SELECT e.horaireCours, e.dureeCours, e.heureDebut, e.heureFin, e.minuteDebut, " +
            "e.fractionnement FROM " + EtablissementBean.class.getName() +
            " e WHERE e.id = :id";

        final List<Object[]> liste =
            getEntityManager().createQuery(query).setParameter("id", id).getResultList();

        if (!CollectionUtils.isEmpty(liste) && (liste.size() == 1)) {
            final Object[] result = liste.get(0);
            etablissementComplementDTO.setHoraireCours((String) result[0]);
            etablissementComplementDTO.setDureeCours((Integer) result[1]);
            etablissementComplementDTO.setHeureDebutCours((Integer) result[2]);
            etablissementComplementDTO.setHeureFinCours((Integer) result[3]);
            etablissementComplementDTO.setMinuteDebutCours((Integer) result[4]);
            etablissementComplementDTO.setFractionnement((Integer) result[5]);

            return etablissementComplementDTO;
        } else {
            throw new MetierRuntimeException(new ConteneurMessage(),
                                             "L'établissement {0} n'existe pas.", id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<String> findHorairesCoursEtablissement(final Integer id) {
        Assert.isNotNull("id", id);
        final ResultatDTO<String> resultat = new ResultatDTO<String>();
        final String query =
            "SELECT horaireCours" + " FROM " + EtablissementBean.class.getName() +
            " WHERE id = :id";

        final List<String> liste =
            getEntityManager().createQuery(query).setParameter("id", id).getResultList();

        if (!CollectionUtils.isEmpty(liste) && (liste.size() == 1)) {
            final String strHoraire =
                (!org.apache.commons.lang.StringUtils.isEmpty(liste.get(0))) ? liste.get(0) : GRILLE_HORAIRE_DEFAUT;
            resultat.setValeurDTO(strHoraire);
            return resultat;
        } else {
            throw new MetierRuntimeException(new ConteneurMessage(),
                                             "L'établissement {0} n'existe pas.", id);
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveComplementEtablissement(final EtablissementDTO etablissementQO)
        throws MetierException {
        Assert.isNotNull("etablissementQO", etablissementQO);

        final Integer idEtablissement = etablissementQO.getId();
        Assert.isNotNull("idEtablissement", idEtablissement);

        final String queryUpdate =
            "UPDATE " + EtablissementBean.class.getName() +
            " e SET e.horaireCours=:horaireCours, e.dureeCours=:dureeCours, e.heureDebut=:heureDebut, " +
            "e.heureFin=:heureFin, e.minuteDebut=:minuteDebut, e.fractionnement=:fractionnement WHERE e.id = :id";

        getEntityManager().createQuery(queryUpdate).setParameter("id", idEtablissement)
            .setParameter("horaireCours", etablissementQO.getHoraireCours())
            .setParameter("dureeCours", etablissementQO.getDureeCours())
            .setParameter("heureDebut", etablissementQO.getHeureDebut())
            .setParameter("heureFin", etablissementQO.getHeureFin())
            .setParameter("minuteDebut", etablissementQO.getMinuteDebut())
            .setParameter("fractionnement", etablissementQO.getFractionnement())
            .executeUpdate();

        getEntityManager().flush();

        final ResultatDTO<Integer> resultat = new ResultatDTO<Integer>();

        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        conteneurMessage.add(new Message("traitement.enregistrement.reussi",
                                         Nature.INFORMATIF));

        resultat.setConteneurMessage(conteneurMessage);

        return resultat;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer findIdEtablissementParCode(final String siren) {
        Assert.isNotEmpty("siren", siren);

        final String query =
            "SELECT e.id FROM " + EtablissementBean.class.getName() +
            " e WHERE e.code = :siren";

        final List<Integer> liste =
            getEntityManager().createQuery(query).setParameter("siren", siren)
                .getResultList();

        if (!CollectionUtils.isEmpty(liste) && (liste.size() == 1)) {
            return liste.get(0);
        } else {
            throw new MetierRuntimeException(new ConteneurMessage(),
                                             "L'établissement {0} n'existe pas.", siren);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public String findDesignationEtablissementParCode(final String siren) {
        Assert.isNotEmpty("siren", siren);

        final String query =
            "SELECT e.designation FROM " + EtablissementBean.class.getName() +
            " e WHERE e.code = :siren";

        final List<String> liste =
            getEntityManager().createQuery(query).setParameter("siren", siren)
                .getResultList();

        if (!CollectionUtils.isEmpty(liste) && (liste.size() == 1)) {
            return liste.get(0);
        } else {
            throw new MetierRuntimeException(new ConteneurMessage(),
                                             "L'établissement {0} n'existe pas.", siren);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object[]> executeQueryListeEtablissement(final String schema) {
        final Map<String, Object[]> mapCodeDesignation = new HashMap<String, Object[]>();

        final boolean vraiOuFauxSchemaCourant = org.apache.commons.lang.StringUtils.isEmpty(schema);
        final String schemaQuery =
            (vraiOuFauxSchemaCourant) ? SchemaUtils.getDefaultSchema() : schema;

        //on charge les identifiants uniquements pour le schéma courant.
        final String querySelect =
            "SELECT e.code, e.designation, e.jours_ouvres, e.ouverture " +
            ((vraiOuFauxSchemaCourant) ? ", e.id" : "");
        final String queryEtablissement =
            querySelect + " FROM " +
            SchemaUtils.getTableAvecSchema(schemaQuery, "cahier_etablissement") +
            " e WHERE e.code != :codeNonRattach ";

        final List<Object[]> liste =
            getEntityManager().createNativeQuery(queryEtablissement)
                .setParameter("codeNonRattach", SIREN_NON_RATTACH).getResultList();

        for (final Object[] result : liste) {
            final Object[] tabObj = new Object[4];
            tabObj[0] = (String) result[1];
            String joursOuvres = (String) result[2];

            //si les jours ouvrés sont vides on considère que la semaine est de 5 jours. 
            if (org.apache.commons.lang.StringUtils.isEmpty(joursOuvres)) {
                joursOuvres = JOURS_OUVRES_DEFAUT;
            }
            tabObj[1] = joursOuvres;
            tabObj[2] = result[3];
            tabObj[3] = (vraiOuFauxSchemaCourant) ? (Integer) result[4] : null;
            mapCodeDesignation.put((String) result[0], tabObj);
        }

        return mapCodeDesignation;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<EtablissementDTO> findListeEtablissementEnseignant(final EtablissementAccessibleQO etablissementAccessibleQO) {
        Assert.isNotNull("etablissementAccessibleQO", etablissementAccessibleQO);
        final Set<String> listeSirenSet =
            new HashSet<String>(etablissementAccessibleQO.getListeSiren());

        //ajout d'un élément par defaut afin d'éviter les listes vides.
        listeSirenSet.add("DEFAUT");

        final Integer idEnseignant = etablissementAccessibleQO.getIdEnseignant();
        Assert.isNotNull("idEnseignant", idEnseignant);

        final String querySelect =
            "SELECT new Map (e.id as id, e.code as code , e.designation as desc, " +
            "e.joursOuvres as ouvre, e.vraiOuFauxCahierOuvert as ouvert, ee.vraiOuFauxSaisieSimplifiee as simp) FROM " +
            EtablissementsEnseignantsBean.class.getName() +
            " ee INNER JOIN ee.etablissement e " +
            "WHERE ee.pk.idEnseignant = :idEnseignant AND e.code IN (:liste)";

        final List<Map<String, ?>> liste =
            getEntityManager().createQuery(querySelect)
                .setParameter("idEnseignant", idEnseignant)
                .setParameter("liste", listeSirenSet).getResultList();

        return genereListeEtablissementFromMap(liste);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<EtablissementDTO> findListeEtablissementInspecteur(final List<String> listeSiren) {
        final Set<String> listeSirenSet = new HashSet<String>(listeSiren);
        
        //ajout d'un élément par defaut afin d'éviter les listes vides.
        listeSirenSet.add("DEFAUT");

        final String querySelect =
            "SELECT new Map (e.id as id, e.code as code , e.designation as desc, " +
            "e.joursOuvres as ouvre, e.vraiOuFauxCahierOuvert as ouvert) FROM " +
            EtablissementBean.class.getName() +
            " e WHERE e.code IN (:liste) AND e.code != :codeNonRattach";

        final List<Map<String, ?>> liste =
            getEntityManager().createQuery(querySelect)
                .setParameter("liste", listeSirenSet)
                .setParameter("codeNonRattach", SIREN_NON_RATTACH).getResultList();

        return genereListeEtablissementFromMap(liste);
    }

    /**
     * Génére une liste d'établissement en fonction des résultats de la base de
     * données.
     *
     * @param liste les résultat de la base.
     *
     * @return la liste des établissements.
     */
    private List<EtablissementDTO> genereListeEtablissementFromMap(final List<Map<String, ?>> liste) {
        final List<EtablissementDTO> listeEtablissement =
            new ArrayList<EtablissementDTO>();

        for (final Map<String, ?> map : liste) {
            final EtablissementDTO etablissementDTO = new EtablissementDTO();
            etablissementDTO.setId((Integer) map.get("id"));
            etablissementDTO.setCode((String) map.get("code"));
            etablissementDTO.setDesignation((String) map.get("desc"));
            etablissementDTO.setVraiOuFauxOuvert((Boolean) map.get("ouvert"));
            etablissementDTO.setVraiOuFauxSaisieSimplifiee((Boolean) map.get("simp"));
            String joursOuvres = (String) map.get("ouvre");

            //si les jours ouvrés sont vides on considère que la semaine est de 5 jours. 
            if (org.apache.commons.lang.StringUtils.isEmpty(joursOuvres)) {
                joursOuvres = JOURS_OUVRES_DEFAUT;
            }
            etablissementDTO.setJoursOuvres(joursOuvres);

            listeEtablissement.add(etablissementDTO);
        }

        return listeEtablissement;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveEtablissementJoursOuvres(final EtablissementDTO etablissementQO)
        throws MetierException {
        Assert.isNotNull("etablissementQO", etablissementQO);

        final Integer idEtablissement = etablissementQO.getId();
        Assert.isNotNull("idEtablissement", idEtablissement);

        final String joursOuvres = etablissementQO.getJoursOuvres();
        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        if (org.apache.commons.lang.StringUtils.isEmpty(joursOuvres)) {
            conteneurMessage.add(new Message(TypeReglesAdmin.ADMIN_20.name()));
            throw new MetierException(conteneurMessage,
                                      "Echec de la mise à jour des jours ouvrés");
        } else {
            Assert.isTrue("Jours ouvrés valides", 
                    Pattern.matches("(((LUNDI)|(MARDI)|(MERCREDI)|(JEUDI)|(VENDREDI)|(SAMEDI)|(DIMANCHE))\\|)*", joursOuvres));
        }

        final String queryUpdate =
            "UPDATE " + EtablissementBean.class.getName() +
            " E SET E.joursOuvres=:joursOuvres WHERE E.id = :id";

        final int i = getEntityManager().createQuery(queryUpdate).setParameter("id", idEtablissement)
            .setParameter("joursOuvres", joursOuvres).executeUpdate();

        getEntityManager().flush();

        final ResultatDTO<Integer> resultat = new ResultatDTO<Integer>();

        if (i == 1 ){
            conteneurMessage.add(new Message("traitement.enregistrement.reussi",
                                         Nature.INFORMATIF));
        } else {
            conteneurMessage.add(new Message("traitement enregistrement non effectué",
                    Nature.BLOQUANT));
            throw new MetierException("Enregistrement des jours ouvrés non effectué"); 
        }
        resultat.setConteneurMessage(conteneurMessage);

        return resultat;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveEtablissementOuverture(final OuvertureQO ouvertureQO)
        throws MetierException {
        Assert.isNotNull("ouvertureQO", ouvertureQO);

        final Integer idEtablissement = ouvertureQO.getId();
        Assert.isNotNull("idEtablissement", idEtablissement);

        final Boolean vraiOuFauxEtabOuvert =
            BooleanUtils.isTrue(ouvertureQO.getVraiOuFauxOuvert());

        final String queryUpdate =
            "UPDATE " + EtablissementBean.class.getName() +
            " e SET e.vraiOuFauxCahierOuvert=:vraiOuFauxEtabOuvert WHERE e.id = :id";

        final int i = getEntityManager().createQuery(queryUpdate).setParameter("id", idEtablissement)
            .setParameter("vraiOuFauxEtabOuvert", vraiOuFauxEtabOuvert).executeUpdate();

        getEntityManager().flush();

        final ResultatDTO<Integer> resultat = new ResultatDTO<Integer>();

        final String message = vraiOuFauxEtabOuvert ? "L'ouverture" : "La fermeture";

        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        if (i == 1){
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                    Nature.INFORMATIF,
                    message + " du cahier de texte",
                    "prise en compte"));

        } else {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_02.name(),
                    Nature.AVERTISSANT,
                    message + " du cahier de texte",
                    "prise en compte"));
            throw new MetierException(message+ " du cahier de texten n'a pas été prise en comptes.");
        }

        resultat.setConteneurMessage(conteneurMessage);

        return resultat;
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveEtablissementAlternance(final EtablissementDTO etablissementQO)
        throws MetierException {
        Assert.isNotNull("etablissementQO", etablissementQO);

        final Integer idEtablissement = etablissementQO.getId();
        Assert.isNotNull("idEtablissement", idEtablissement);

        final String alternanceSemaine = etablissementQO.getAlternanceSemaine();

        final String queryUpdate =
            "UPDATE " + EtablissementBean.class.getName() +
            " E SET E.alternanceSemaine=:alternanceSemaine WHERE E.id = :id";

        getEntityManager().createQuery(queryUpdate).setParameter("id", idEtablissement)
            .setParameter("alternanceSemaine", alternanceSemaine).executeUpdate();

        getEntityManager().flush();

        final ResultatDTO<Integer> resultat = new ResultatDTO<Integer>();

        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        conteneurMessage.add(new Message("traitement.enregistrement.reussi",
                                         Nature.INFORMATIF));

        resultat.setConteneurMessage(conteneurMessage);

        return resultat;
    }

    /**
     * {@inheritDoc}
     */
    public void saveEtablissementSaisieSimplifiee(SaisieSimplifieeQO saisieSimplifieeQO) {
        Assert.isNotNull("saisieSimplifieeQO", saisieSimplifieeQO);
        final Integer idEtablissement = saisieSimplifieeQO.getIdEtablissement();
        final Integer idEnseignant = saisieSimplifieeQO.getIdEnseignant();
        final Boolean vraiOuFauxSaisieSimplifiee = saisieSimplifieeQO.getVraiOuFauxSaisieSimplifiee();
        final Boolean vraiOuFauxExiste = saisieSimplifieeQO.getVraiOuFauxExiste();

        Assert.isNotNull("idEtablissement", idEtablissement);
        Assert.isNotNull("idEnseignant", idEnseignant);
        Assert.isNotNull("vraiOuFauxSaisieSimplifiee", vraiOuFauxSaisieSimplifiee);
        Assert.isNotNull("vraiOuFauxExiste", vraiOuFauxExiste);

        if (vraiOuFauxExiste) {
            final String queryUpdate =
                "UPDATE " + EtablissementsEnseignantsBean.class.getName() +
                " E SET E.vraiOuFauxSaisieSimplifiee=:vraiOuFauxSaisieSimplifiee " +
                " WHERE E.pk.idEtablissement=:idEtablissement AND E.pk.idEnseignant=:idEnseignant";
    
            getEntityManager().createQuery(queryUpdate)
                .setParameter("idEtablissement", idEtablissement)
                .setParameter("idEnseignant", idEnseignant)
                .setParameter("vraiOuFauxSaisieSimplifiee", vraiOuFauxSaisieSimplifiee)
                .executeUpdate();
        } else {
            final EtablissementsEnseignantsBean etablissementEnseignant = new EtablissementsEnseignantsBean();
            etablissementEnseignant.setIdEnseignant(idEnseignant);
            etablissementEnseignant.setIdEtablissement(idEtablissement);
            etablissementEnseignant.setVraiOuFauxSaisieSimplifiee(vraiOuFauxSaisieSimplifiee);
            getEntityManager().persist(etablissementEnseignant);
        }

        getEntityManager().flush();
    }
    
    /**
     * {@inheritDoc}   
     */
    public EtablissementDTO findEtablissement(final Integer idEtablissement) {
        Assert.isNotNull("idEtablissement", idEtablissement);

        final String query =
            "SELECT e FROM " + EtablissementBean.class.getName() +
            " e WHERE e.id = :idEtablissement";

        @SuppressWarnings("unchecked")
        final List<EtablissementBean> liste =
            getEntityManager().createQuery(query)
                .setParameter("idEtablissement", idEtablissement).getResultList();

        if (!CollectionUtils.isEmpty(liste) && (liste.size() == 1)) {
            EtablissementDTO retour = new EtablissementDTO();
            ObjectUtils.copyProperties(retour, liste.get(0));
            
            //Mettre defauts horaires 
            if (org.apache.commons.lang.StringUtils.trimToNull(retour.getHoraireCours()) == null) {
                log.info("Mettre defaut grille et les defauts heuere debut / fin");
                retour.setHoraireCours(GRILLE_HORAIRE_DEFAUT);
                retour.setHeureDebut(HEURE_DEBUT_DEFAUT);
                retour.setMinuteDebut(0);
                retour.setHeureFin(HEURE_FIN_DEFAUT);
            }
            
            if (org.apache.commons.lang.StringUtils.trimToNull(retour.getJoursOuvres()) == null) {
                retour.setJoursOuvres(JOURS_OUVRES_DEFAUT);
            }
            
            if (retour.getDureeCours() == null || retour.getDureeCours() <= 0) {
                retour.setDureeCours(DUREE_COURS_DEFAULT);
            }
            
            return retour;
        } else {
            throw new MetierRuntimeException(new ConteneurMessage(),
                                             "L'établissement identifié par {0} n'existe pas.",
                                             idEtablissement);
        }
    }

    /**
     * {@inheritDoc}
     */
    public SaisieSimplifieeDTO findSaisieSimplifieeEtablissement(final Integer idEtablissement,
                                                     final Integer idEnseignant) {
        final List<Boolean> liste =
            querySaisieSimplifieeEtablissement(idEtablissement, idEnseignant);
        
        final SaisieSimplifieeDTO result = new SaisieSimplifieeDTO();
        
        if (CollectionUtils.isEmpty(liste)) {
            result.setVraiOuFauxExiste(false);
        } else {
            result.setVraiOuFauxExiste(true);
            result.setVraiOuFauxsaisieSimpliee(liste.get(0));
        }
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean checkSaisieSimplifieeEtablissement(final Integer idEtablissement,
                                                      final Integer idEnseignant) {
        final List<Boolean> liste =
            querySaisieSimplifieeEtablissement(idEtablissement, idEnseignant);

        Boolean saisieSimplifiee = null;

        if (!CollectionUtils.isEmpty(liste)) {
            saisieSimplifiee = liste.get(0);
        }
        return saisieSimplifiee;
    }

    /**
     * Recherche de la valeur de la saisie simplifiée d'un enseignant sur un
     * établissement.
     *
     * @param idEtablissement id de l'établissement.
     * @param idEnseignant id de l'enseignant.
     *
     * @return la liste.
     */
    @SuppressWarnings("unchecked")
    private List<Boolean> querySaisieSimplifieeEtablissement(final Integer idEtablissement,
                                                             final Integer idEnseignant) {
        Assert.isNotNull("idEtablissement", idEtablissement);
        Assert.isNotNull("idEnseignant", idEnseignant);

        final String querySelect =
            "SELECT ee.vraiOuFauxSaisieSimplifiee FROM " +
            EtablissementsEnseignantsBean.class.getName() + " ee " +
            "WHERE ee.pk.idEnseignant = :idEnseignant AND ee.pk.idEtablissement = :idEtablissement";

        return getEntityManager().createQuery(querySelect)
                   .setParameter("idEnseignant", idEnseignant)
                   .setParameter("idEtablissement", idEtablissement).getResultList();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer findFractionnementEtablissement(final Integer idEtablissement){
        Assert.isNotNull("idEtablissement", idEtablissement);
        final String querySelect = "SELECT e.fractionnement FROM " + EtablissementBean.class.getName() + 
        " e WHERE e.id = :idEtablissement";
        final List<Integer> results = getEntityManager().createQuery(querySelect)
            .setParameter("idEtablissement", idEtablissement).getResultList();
        if(! CollectionUtils.isEmpty(results)){
            if(results.get(0) == null){
                return 1;
            }else{
                return results.get(0);
            }
        }else{
            return 1;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer findDureeCoursEtablissement(final Integer idEtablissement){
        Assert.isNotNull("idEtablissement", idEtablissement);
        final String querySelect = "SELECT e.dureeCours FROM " + EtablissementBean.class.getName() + 
        " e WHERE e.id = :idEtablissement";
        final List<Integer> results = getEntityManager().createQuery(querySelect)
            .setParameter("idEtablissement", idEtablissement).getResultList();
        if(! CollectionUtils.isEmpty(results)){
            if(results.get(0) == null){
                return 60;
            }else{
                return results.get(0);
            }
        }else{
            return 60;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Boolean findEtatImportEtablissement(final Integer idEtablissement){
        Assert.isNotNull("idEtablissement", idEtablissement);
        final String querySelect = "SELECT e.vraiOuFauxImportEnCours FROM " + EtablissementBean.class.getName() + 
        " e WHERE e.id = :idEtablissement";
        final List<Boolean> results = getEntityManager().createQuery(querySelect)
            .setParameter("idEtablissement", idEtablissement).getResultList();
        if(! CollectionUtils.isEmpty(results)){
            if(results.get(0) == null){
                return false;
            }else{
                return results.get(0);
            }
        }else{
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void modifieStatutImportEtablissement(Integer idEtablissement,
            Boolean statut) {
        if(statut){
            final String dateImport = String.valueOf((new Date().getTime()));
            final String queryUpdate =
                "UPDATE " + EtablissementBean.class.getName() +
                " E SET E.vraiOuFauxImportEnCours = :statut , E.dateImport = :dateImport WHERE E.id = :idEtablissement";
            getEntityManager().createQuery(queryUpdate).setParameter("idEtablissement", idEtablissement)
            .setParameter("statut", statut).setParameter("dateImport", dateImport).executeUpdate();
        }else{
            final String queryUpdate =
                "UPDATE " + EtablissementBean.class.getName() +
                " E SET E.vraiOuFauxImportEnCours = :statut WHERE E.id = :idEtablissement";
            getEntityManager().createQuery(queryUpdate).setParameter("idEtablissement", idEtablissement)
            .setParameter("statut", statut).executeUpdate();
        }
        getEntityManager().flush();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public String checkDateImportEtablissement(final Integer idEtablissement){
        Assert.isNotNull("idEtablissement", idEtablissement);
        final String querySelect = "SELECT e.dateImport FROM " + EtablissementBean.class.getName() + 
        " e WHERE e.id = :idEtablissement";
        final List<String> results = getEntityManager().createQuery(querySelect)
        .setParameter("idEtablissement", idEtablissement).getResultList();
        if(! CollectionUtils.isEmpty(results)){
            return results.get(0);
        }else{
            return "";
        }
    }
}
