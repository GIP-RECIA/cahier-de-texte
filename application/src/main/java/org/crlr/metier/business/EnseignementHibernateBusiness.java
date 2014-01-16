/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignementHibernateBusiness.java,v 1.23 2010/04/26 07:45:49 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.Query;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.EnseignementQO;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.TypeReglesAdmin;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.metier.entity.ClasseBean;
import org.crlr.metier.entity.EnseignantsEnseignementsBean;
import org.crlr.metier.entity.EnseignementBean;
import org.crlr.metier.entity.EnseignementLibelleBean;
import org.crlr.metier.entity.GroupeBean;
import org.crlr.metier.entity.OuvertureInspecteurBean;
import org.crlr.metier.entity.SequenceBean;
import org.crlr.metier.utils.SchemaUtils;
import org.crlr.utils.Assert;
import org.crlr.utils.ComparateurUtils;
import org.crlr.utils.ObjectUtils;
import org.springframework.stereotype.Repository;

/**
 * EnseignementHibernateBusiness.
 *
 * @author vibertd
 * @version $Revision: 1.23 $
 */
@Repository
public class EnseignementHibernateBusiness extends AbstractBusiness
    implements EnseignementHibernateBusinessService {
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public EnseignementDTO find(Integer idEnseignement) {
        Assert.isNotNull("idEnseignement", idEnseignement);
        final EnseignementDTO enseignement;

        final String query =
            "SELECT new map(E.id as id, E.code as code, E.designation as designation) " +
            " FROM " + EnseignementBean.class.getName() +
            " e WHERE e.id = :idEnseignement";

        final List<Map<String, ?>> resultatQuery =
            getEntityManager().createQuery(query)
                .setParameter("idEnseignement", idEnseignement).getResultList();

        final List<EnseignementDTO> liste = new ArrayList<EnseignementDTO>();

        for (Map<String, ?> result : resultatQuery) {
            final EnseignementDTO enseignementDTO = new EnseignementDTO();
            enseignementDTO.setId((Integer) result.get("id"));
            enseignementDTO.setCode((String) result.get("code"));
            enseignementDTO.setIntitule((String) result.get("designation"));
            liste.add(enseignementDTO);
        }

        if (!org.apache.commons.collections.CollectionUtils.isEmpty(liste)) {
            enseignement = liste.get(0);
        } else {
            enseignement = null;
        }

        return enseignement;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public EnseignementBean findByCode(String code) {
        Assert.isNotNull("code", code);
        final EnseignementBean enseignement;

        final String query =
            "SELECT new map(E.id as id, E.code as code, E.designation as designation) " +
            " FROM " + EnseignementBean.class.getName() + " e WHERE e.code = :code";

        final List<Map<String, ?>> resultatQuery =
            getEntityManager().createQuery(query).setParameter("code", code)
                .getResultList();

        final List<EnseignementBean> liste = new ArrayList<EnseignementBean>();

        for (Map<String, ?> result : resultatQuery) {
            final EnseignementBean enseignementBean = new EnseignementBean();
            enseignementBean.setId((Integer) result.get("id"));
            enseignementBean.setCode((String) result.get("code"));
            enseignementBean.setDesignation((String) result.get("designation"));
            liste.add(enseignementBean);
        }

        if (!org.apache.commons.collections.CollectionUtils.isEmpty(liste)) {
            enseignement = liste.get(0);
        } else {
            enseignement = null;
        }

        return enseignement;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer exist(Integer id) {
        final Integer idEnseignement;

        final String query =
            "SELECT e.id FROM " + EnseignementBean.class.getName() +
            " e WHERE e.id = :id";

        final List<Integer> liste =
            getEntityManager().createQuery(query).setParameter("id", id).getResultList();

        if (!org.apache.commons.collections.CollectionUtils.isEmpty(liste)) {
            idEnseignement = liste.get(0);
        } else {
            idEnseignement = null;
        }

        return idEnseignement;
    }

    /**
     * {@inheritDoc}
     */
    public Integer save(EnseignementBean enseignement)
                 throws MetierException {
        getEntityManager().persist(enseignement);
        getEntityManager().flush();

        return enseignement.getId();
    }

    /**
     * @param rechercheEnseignementPopupQO r
     * @return r
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<EnseignementDTO>> findEnseignementPopupEnseignant(
            RechercheEnseignementPopupQO rechercheEnseignementPopupQO) {

        final Integer idEnseignant = rechercheEnseignementPopupQO
                .getIdEnseignant();
        final Integer idEtablissement = rechercheEnseignementPopupQO
                .getIdEtablissement();
       
        final ResultatDTO<List<EnseignementDTO>> enseignementPopup = new ResultatDTO<List<EnseignementDTO>>();

        final String query = "SELECT new map(E.id as id, E.code as code, E.designation as designation)"
                + " FROM "
                + EnseignantsEnseignementsBean.class.getName()
                + " EE INNER JOIN EE.enseignement E "
                + " WHERE EE.pk.idEnseignant = :idEnseignant AND EE.pk.idEtablissement = :idEtablissement"
                + " ORDER BY E.designation ASC";

        final List<Map<String, ?>> resultatQuery = getEntityManager()
                .createQuery(query).setParameter("idEnseignant", idEnseignant)
                .setParameter("idEtablissement", idEtablissement)
                .getResultList();

        final List<EnseignementDTO> resultat = new ArrayList<EnseignementDTO>();

        for (Map<String, ?> result : resultatQuery) {
            final EnseignementDTO enseignementDTO = new EnseignementDTO();
            enseignementDTO.setId((Integer) result.get("id"));
            enseignementDTO.setCode((String) result.get("code"));
            enseignementDTO.setIntitule((String) result.get("designation"));
            resultat.add(enseignementDTO);
        }

        enseignementPopup.setValeurDTO(resultat);

        return enseignementPopup;
    }

    /**
     * @param rechercheEnseignementPopupQO r
     * @return r
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<EnseignementDTO>> findEnseignementPopupDirecteur(
            RechercheEnseignementPopupQO rechercheEnseignementPopupQO) {
        List<Map<String, ?>> resultatQuery = new ArrayList<Map<String, ?>>();
        final String strWhereRequete;
        final String strFromRequete;

        final Integer idEtablissement = rechercheEnseignementPopupQO
                .getIdEtablissement();
        
        final ResultatDTO<List<EnseignementDTO>> enseignementPopup = new ResultatDTO<List<EnseignementDTO>>();

        // Dans le cas de recherche par classe.
        if (TypeGroupe.CLASSE.equals(rechercheEnseignementPopupQO
                .getTypeGroupeSelectionne())) {
            final Integer idClasse = rechercheEnseignementPopupQO
                    .getGroupeClasseSelectionne().getId();
            strFromRequete = ", " + ClasseBean.class.getName() + " CG ";
            strWhereRequete = " CG.id = " + idClasse
                    + " AND CG.id = SEQ.idClasse AND ";
        } else if (TypeGroupe.GROUPE.equals(rechercheEnseignementPopupQO
                .getTypeGroupeSelectionne())) {
            final Integer idGroupe = rechercheEnseignementPopupQO
                    .getGroupeClasseSelectionne().getId();
            strFromRequete = ", " + GroupeBean.class.getName() + " CG ";
            strWhereRequete = " CG.id = " + idGroupe
                    + " AND CG.id = SEQ.idGroupe AND ";
        } else {
            strWhereRequete = "";
            strFromRequete = "";
        }

        final String requete = " SELECT distinct new map(" + "    E.id as id, "
                + "    E.designation as designation, "
                + "    E.code as code) FROM  " + SequenceBean.class.getName()
                + " SEQ " + " INNER JOIN SEQ.enseignement E " + strFromRequete
                + " WHERE " + strWhereRequete
                + " SEQ.idEtablissement = :idEtablissement ";

        final Query query = getEntityManager().createQuery(requete);
        query.setParameter("idEtablissement", idEtablissement);
        resultatQuery = query.getResultList();

        final List<EnseignementDTO> resultat = new ArrayList<EnseignementDTO>();

        for (Map<String, ?> result : resultatQuery) {
            final EnseignementDTO enseignementDTO = new EnseignementDTO();
            enseignementDTO.setId((Integer) result.get("id"));
            enseignementDTO.setCode((String) result.get("code"));
            enseignementDTO.setIntitule((String) result.get("designation"));
            resultat.add(enseignementDTO);
        }

        enseignementPopup.setValeurDTO(resultat);

        return enseignementPopup;
    }

    /**
     * @param rechercheEnseignementPopupQO r
     * @return r
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<EnseignementDTO>> findEnseignementPopupInspecteur(
            RechercheEnseignementPopupQO rechercheEnseignementPopupQO) {
        List<Map<String, ?>> resultatQuery = new ArrayList<Map<String, ?>>();

        final Integer idEtablissement = rechercheEnseignementPopupQO.getIdEtablissement();
        final Integer idInspecteur = rechercheEnseignementPopupQO.getIdInspecteur();
        final Integer idEnseignant = rechercheEnseignementPopupQO.getIdEnseignant();

        final ResultatDTO<List<EnseignementDTO>> enseignementPopup = new ResultatDTO<List<EnseignementDTO>>();

        final Integer idClasseGroupe = rechercheEnseignementPopupQO
                .getGroupeClasseSelectionne() != null ? rechercheEnseignementPopupQO
                .getGroupeClasseSelectionne().getId() : null;

        String strWhereRequete = "";
        if (idClasseGroupe != null
                && (TypeGroupe.CLASSE == (rechercheEnseignementPopupQO
                        .getTypeGroupeSelectionne()))) {
            strWhereRequete = " C.id = :idClasse AND ";
        }

        if (idClasseGroupe != null
                && (TypeGroupe.GROUPE == (rechercheEnseignementPopupQO
                        .getTypeGroupeSelectionne()))) {
            strWhereRequete = " G.id = :idGroupe AND ";
        }
        
        if (idEnseignant != null) {
            strWhereRequete = " SEQ.idEnseignant = :idEnseignant AND ";
        }
        
        final String query = "SELECT distinct new map(E.id as id, E.code as code, E.designation as designation)"
                + " FROM "
                + EnseignantsEnseignementsBean.class.getName()
                + " EE "
                + " INNER JOIN EE.enseignement E , "
                + SequenceBean.class.getName()
                + " SEQ "
                + " LEFT JOIN SEQ.classe C " 
                + " LEFT JOIN SEQ.groupe G, "
                + OuvertureInspecteurBean.class.getName()
                + " OUV  "
                + " WHERE "
                + strWhereRequete
                + " C.idEtablissement = :idEtablissement AND "
                + " OUV.inspecteur.id = :idInspecteur AND OUV.etablissement.id = :idEtablissement AND "
                + " EE.pk.idEnseignant = OUV.enseignant.id AND EE.pk.idEtablissement = :idEtablissement AND "
                + " SEQ.idEnseignement = EE.pk.idEnseignement "
                + " ORDER BY E.id ASC";

        final Query queryQ = getEntityManager().createQuery(query)
                .setParameter("idInspecteur", idInspecteur)
                .setParameter("idEtablissement", idEtablissement);

        if (idClasseGroupe != null
                && (TypeGroupe.CLASSE == (rechercheEnseignementPopupQO
                        .getTypeGroupeSelectionne()))) {
            queryQ.setParameter("idClasse", idClasseGroupe);
        }

        if (idClasseGroupe != null
                && (TypeGroupe.GROUPE == (rechercheEnseignementPopupQO
                        .getTypeGroupeSelectionne()))) {
            queryQ.setParameter("idClasse", idClasseGroupe);
        }
        
        if (idEnseignant != null) {
            queryQ.setParameter("idEnseignant", idEnseignant);
        }
        
        resultatQuery = queryQ.getResultList();

        final List<EnseignementDTO> resultat = new ArrayList<EnseignementDTO>();

        for (Map<String, ?> result : resultatQuery) {
            final EnseignementDTO enseignementDTO = new EnseignementDTO();
            enseignementDTO.setId((Integer) result.get("id"));
            enseignementDTO.setCode((String) result.get("code"));
            enseignementDTO.setIntitule((String) result.get("designation"));
            resultat.add(enseignementDTO);
        }

        enseignementPopup.setValeurDTO(resultat);

        return enseignementPopup;
    }
    
    
    /**
     * {@inheritDoc}
     */
    
    public ResultatDTO<List<EnseignementDTO>> findEnseignementPopup(
            RechercheEnseignementPopupQO rechercheEnseignementPopupQO)
            throws MetierException {

        // En mode archive, on fait appelle a cette methode
        if (rechercheEnseignementPopupQO.getArchive() != null
                && rechercheEnseignementPopupQO.getArchive()) {
            return findEnseignementArchivePopup(rechercheEnseignementPopupQO);
        }
 
        
        Assert.isNotNull("rechercheEnseignementPopupQO",
                rechercheEnseignementPopupQO);

        final Integer idEnseignant = rechercheEnseignementPopupQO
                .getIdEnseignant();
        final Integer idInspecteur = rechercheEnseignementPopupQO
                .getIdInspecteur();

        if (idInspecteur != null) {
            return findEnseignementPopupInspecteur(rechercheEnseignementPopupQO);
        } else if (idEnseignant != null) {
           return findEnseignementPopupEnseignant(rechercheEnseignementPopupQO);
        } else { 
            return findEnseignementPopupDirecteur(rechercheEnseignementPopupQO);
            
        }

    }

    
    /**
     * Effectue la recherche des enseignement dans un contexte d'archive.
     * @param rechercheEnseignementPopupQO les criteres de recherche.
     * @return la liste des enseignement
     */
    @SuppressWarnings("unchecked")
    private ResultatDTO<List<EnseignementDTO>> findEnseignementArchivePopup(RechercheEnseignementPopupQO rechercheEnseignementPopupQO)
        throws MetierException {
        final ResultatDTO<List<EnseignementDTO>> enseignementPopup =
            new ResultatDTO<List<EnseignementDTO>>();

        Assert.isNotNull("rechercheEnseignementPopupQO", rechercheEnseignementPopupQO);

        final Integer idEnseignant = rechercheEnseignementPopupQO.getIdEnseignant();
        final Integer idEtablissement = rechercheEnseignementPopupQO.getIdEtablissement();
        final Integer idInspecteur = rechercheEnseignementPopupQO.getIdInspecteur();
        final String schema = SchemaUtils.getSchemaCourantOuArchive(true, rechercheEnseignementPopupQO.getExercice());
        final Integer idClasseGroupe = rechercheEnseignementPopupQO.getGroupeClasseSelectionne().getId();         
        String strWhereRequete = "";
        String strFrom = "";
        String query = "";
        List<Object[]> resultatQuery = new ArrayList<Object[]>();

        // Si la classe/groupe est renseignee
        if (idClasseGroupe != null) {
            strWhereRequete = " CG.id = " + idClasseGroupe + " AND CG.id_Etablissement = EE.id_etablissement AND ";
        } 

        //Dans le cas de recherche par classe ou groupe.
        if (rechercheEnseignementPopupQO.getTypeGroupeSelectionne() != null) {
            if (TypeGroupe.CLASSE.equals(rechercheEnseignementPopupQO.getTypeGroupeSelectionne())) {
                strFrom = " INNER JOIN " + SchemaUtils.getTableAvecSchema(schema, "cahier_classe") + " CG on (CG.id = SEQ.id_classe) ";
            } else {
                strFrom = " INNER JOIN " + SchemaUtils.getTableAvecSchema(schema, "cahier_groupe") + " CG on (CG.id = SEQ.id_groupe) ";
            }
        } 
        
        // Pour les inspecteur
        if (idInspecteur != null){
            if (!strFrom.equals("")) {
                strFrom += ",";
            }
            query =
                "SELECT distinct E.id as id, E.code as code, E.designation as designation" +
                " FROM " + 
                SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant_enseignement") + " EE "+
                " INNER JOIN " + SchemaUtils.getTableAvecSchema(schema, "cahier_enseignement") + " E on (EE.id_enseignement = E.id), " +
                SchemaUtils.getTableAvecSchema(schema, "cahier_sequence") + " SEQ " +
                strFrom +
                SchemaUtils.getTableAvecSchema(schema, "cahier_ouverture_inspecteur") + " OUV  " +
                " WHERE " + strWhereRequete +
                " OUV.id_inspecteur = " + idInspecteur + " AND OUV.id_etablissement = EE.id_Etablissement AND " +
                " EE.id_Enseignant = OUV.id_enseignant AND EE.id_Etablissement = " + idEtablissement + " AND " +
                " SEQ.id_Enseignement = EE.id_Enseignement " +
                " ORDER BY E.id ASC";

        // Pour les enseignants
        } else if (idEnseignant != null) {
            query =
                "SELECT distinct E.id as id, E.code as code, E.designation as designation" +
                " FROM " + 
                SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant_enseignement") + " EE "+
                " INNER JOIN " + SchemaUtils.getTableAvecSchema(schema, "cahier_enseignement") + " E on (EE.id_enseignement = E.id), " +
                " WHERE EE.id_Enseignant = " + idEnseignant + " AND EE.id_Etablissement = " + idEtablissement + 
                " ORDER BY E.id ASC";
            
        //Cas d'un directeur   
        } else { 
            query =
                "SELECT distinct E.id as id, E.code as code, E.designation as designation" +
                " FROM " + 
                SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant_enseignement") + " EE "+
                " INNER JOIN " + SchemaUtils.getTableAvecSchema(schema, "cahier_enseignement") + " E on (EE.id_enseignement = E.id), " +
                SchemaUtils.getTableAvecSchema(schema, "cahier_sequence") + " SEQ " +
                strFrom + 
                " WHERE " + strWhereRequete +
                " EE.id_Etablissement = " + idEtablissement + " AND " +
                " SEQ.id_Enseignement = EE.id_Enseignement " +
                " ORDER BY E.id ASC";
        }

        final Query queryQ = getEntityManager().createNativeQuery(query);
        resultatQuery = queryQ.getResultList();

        final List<EnseignementDTO> resultat = new ArrayList<EnseignementDTO>();

        for (Object[] result : resultatQuery) {
            final EnseignementDTO enseignementDTO = new EnseignementDTO();
            enseignementDTO.setId((Integer) result[0]);
            enseignementDTO.setCode((String) result[1]);
            enseignementDTO.setIntitule((String) result[2]);
            resultat.add(enseignementDTO);
        }
        enseignementPopup.setValeurDTO(resultat);
        return enseignementPopup;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean checkDroitEnseignement(Integer idEnseignant, Integer idEnseignement, Integer idEtablissement)
                                   throws MetierException {
        boolean verif = false;
        
        final String query =
            "SELECT 1 FROM " + EnseignantsEnseignementsBean.class.getName() + " EE " +
            " WHERE EE.pk.idEnseignant = :idEnseignant AND " +
                    " EE.pk.idEnseignement = :idEnseignement AND " +
                    " EE.pk.idEtablissement = :idEtablissement";

        final List<Integer> listeEnseignantsEnseignements =
            getEntityManager().createQuery(query).setParameter("idEnseignant", idEnseignant)
            .setParameter("idEnseignement", idEnseignement)
            .setParameter("idEtablissement", idEtablissement)
            .getResultList();
        
        if(listeEnseignantsEnseignements.size() > 0) {
            verif = true;
        }
        
        return verif;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<EnseignementDTO> findEnseignementEtablissement(final Integer idEtablissement) {
        final List<EnseignementDTO> liste = new ArrayList<EnseignementDTO>();

        final String queryEnseignement =
            "SELECT DISTINCT E.id, E.designation FROM " + EnseignantsEnseignementsBean.class.getName() + 
            " EE INNER JOIN EE.enseignement E " +
            " WHERE EE.pk.idEtablissement = :idEtablissement";

        final List<Object[]> listeEnseignementsEtablissement =
            getEntityManager().createQuery(queryEnseignement)
            .setParameter("idEtablissement", idEtablissement)
            .getResultList();

        final String queryLibellePerso = 
            "SELECT new Map(EL.pk.idEnseignement as id, EL.libelle as lib) FROM " + 
            EnseignementLibelleBean.class.getName() +
            " EL WHERE EL.pk.idEtablissement=:idEtablissement";

        final List<Map<String, ?>> listeEnseignementsLibPerso =
            getEntityManager().createQuery(queryLibellePerso)
            .setParameter("idEtablissement", idEtablissement)
            .getResultList();    

        final Map<Integer, String> mapIdEnseignementLibPerso = 
            org.crlr.utils.CollectionUtils.formatMapClefValeur(listeEnseignementsLibPerso, "id", "lib");

        for (final Object[] ligne : listeEnseignementsEtablissement) {
            final EnseignementDTO enseignementDTO = new EnseignementDTO();
            final Integer id = (Integer)ligne[0];
            enseignementDTO.setId(id);
            enseignementDTO.setIntitule((String)ligne[1]);
            enseignementDTO.setLibellePerso(mapIdEnseignementLibPerso.get(id));
            liste.add(enseignementDTO);
        }

        return ComparateurUtils.sort(liste, "intitule");
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<EnseignementDTO> findEnseignementEnseignant(final RechercheEnseignementPopupQO rechercheEnseignementPopupQO) {
        Assert.isNotNull("rechercheEnseignementPopupQO", rechercheEnseignementPopupQO);

        final Integer idEnseignant = rechercheEnseignementPopupQO.getIdEnseignant();
        Assert.isNotNull("idEnseignant", idEnseignant);

        final Integer idEtablissement = rechercheEnseignementPopupQO.getIdEtablissement();
        Assert.isNotNull("idEtablissement", idEtablissement);

        final List<EnseignementDTO> listeEnseignements = new ArrayList<EnseignementDTO>();

        //Identifiant et libellé de l'enseignant
        final String query =
            "SELECT new map(E.id as id, E.designation as designation)" + " FROM " +
            EnseignantsEnseignementsBean.class.getName() +
            " EE INNER JOIN EE.enseignement E " +
            " WHERE EE.pk.idEnseignant = :idEnseignant AND EE.pk.idEtablissement = :idEtablissement";

        final List<Map<String, ?>> resultatQuery =
            getEntityManager().createQuery(query)
                .setParameter("idEnseignant", idEnseignant)
                .setParameter("idEtablissement", idEtablissement).getResultList();

        final Map<Integer, String> mapIdDesignation =
            org.crlr.utils.CollectionUtils.formatMapClefValeur(resultatQuery, "id", "designation");

        final Map<Integer, String> mapIdEnseignementLibellePers =
            new HashMap<Integer, String>();

        if (!mapIdDesignation.isEmpty()) {
            mapIdEnseignementLibellePers.putAll(findEnseignementPersoEtablissement(idEtablissement, mapIdDesignation.keySet()));
        }

        for (final Entry<Integer, String> element : mapIdDesignation.entrySet()) {
            final EnseignementDTO enseignementDTO = new EnseignementDTO();
            //Ajoute l'id
            enseignementDTO.setId(element.getKey());

            //Ajoute le libellé de correspondance ou le libellé
            enseignementDTO.setIntitule(ObjectUtils.defaultIfNull(mapIdEnseignementLibellePers.get(element.getKey()),
                                                                  element.getValue()));

            listeEnseignements.add(enseignementDTO);
        }
        return ComparateurUtils.sort(listeEnseignements, "intitule");
    }    
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Map<Integer, String> findEnseignementPersoEtablissement(final Integer idEtablissement, Set<Integer> listeIdEnseignement) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(listeIdEnseignement)) {
            //Evite la problématique d'un IN vide dans la requête
            listeIdEnseignement.add(0);
        }
        //libellé personnalisé
        final String queryLibellePerso =
            "SELECT new map(L.pk.idEnseignement AS idEnseignement, L.libelle AS designation)" +
            " FROM " + EnseignementLibelleBean.class.getName() +
            " L WHERE L.pk.idEtablissement = :idEtablissement AND L.pk.idEnseignement IN (:idEnseignements)";

        final List<Map<String, ?>> resultatLibelleQuery =
            getEntityManager().createQuery(queryLibellePerso)
            .setParameter("idEtablissement", idEtablissement)
            .setParameter("idEnseignements", listeIdEnseignement)
            .getResultList();

        return
        org.crlr.utils.CollectionUtils.formatMapClefValeur(resultatLibelleQuery,
                "idEnseignement", "designation");
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void saveEnseignementLibelle(final EnseignementQO enseignementQO) throws MetierException {
        Assert.isNotNull("enseignementQO", enseignementQO);
        
        final Integer idEtablissement = enseignementQO.getIdEtablissement(); 
        final Integer idEnseignement = enseignementQO.getIdEnseignement();
        
        Assert.isNotNull("idEtablissement", idEtablissement);
        Assert.isNotNull("idEnseignement", idEnseignement);
        
        final String libelle = org.apache.commons.lang.StringUtils.trimToNull(enseignementQO.getLibelle());
        
        checkUniciteLibelleEnseignement(libelle, idEtablissement);
        
        final String query = "SELECT E FROM " + EnseignementLibelleBean.class.getName() + 
        " E WHERE E.pk.idEtablissement = :idEtablissement AND E.pk.idEnseignement = :idEnseignement";
        
        final List<EnseignementLibelleBean> listeEnseignement = 
            getEntityManager().createQuery(query)
            .setParameter("idEtablissement", idEtablissement)
            .setParameter("idEnseignement", idEnseignement)
            .getResultList();
        
        //mise à jour
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(listeEnseignement)) {
            final EnseignementLibelleBean enseignementLibelleBean = listeEnseignement.get(0);
            if (libelle == null) {
                //suppression
                getEntityManager().remove(enseignementLibelleBean);
            } else {
                //mise à jour
                enseignementLibelleBean.setLibelle(libelle);
            }
        } else {
            if (libelle == null) {
                final ConteneurMessage cm = new ConteneurMessage();
                cm.add(new Message(TypeReglesAdmin.ADMIN_10.name()));
                throw new MetierException(cm, "Echec de la création du libellé d'enseignement");
            }
            
            //création
            final EnseignementLibelleBean enseignementLibelleBean = new EnseignementLibelleBean();            
            enseignementLibelleBean.setIdEnseignement(idEnseignement);
            enseignementLibelleBean.setIdEtablissement(idEtablissement);
            enseignementLibelleBean.setLibelle(libelle);
            
            getEntityManager().persist(enseignementLibelleBean);
        }
            
        getEntityManager().flush();
    }
    
    /**
     * Contrôle de l'unicité du libellé de l'enseignement.
     * @param libelle le libellé.
     * @param idEtablissement l'identifiant de l'établissement.
     * @throws MetierException l'exception potentielle.
     */
    @SuppressWarnings("unchecked")
    private void checkUniciteLibelleEnseignement(final String libelle, final Integer idEtablissement) throws MetierException {
        if (!org.apache.commons.lang.StringUtils.isEmpty(libelle)) {
          //contrôle de la non existance du libellé
            final String queryCheck = 
                "SELECT 1 FROM " + EnseignementLibelleBean.class.getName() + 
                " E WHERE E.pk.idEtablissement=:idEtablissement AND E.libelle=:libelle";
            
            final List<Integer> listeCheck = 
                getEntityManager().createQuery(queryCheck)
                .setParameter("idEtablissement", idEtablissement)
                .setParameter("libelle", libelle)
                .getResultList();
            
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(listeCheck)) {
                final ConteneurMessage cm = new ConteneurMessage();
                cm.add(new Message(TypeReglesAdmin.ADMIN_11.name(), "court de l'enseignement"));
                throw new MetierException(cm, "Echec de l'ajout ou de la mise à jour d'un type de devoir");
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public int findIdEnseignementByLibelle(String libelleMatiere){
        List<Integer> idEnseignement = new ArrayList<Integer>();
        final String requete = "SELECT id FROM " + EnseignementBean.class.getName() + " WHERE designation = :libelleMatiere ";
        idEnseignement = getEntityManager().createQuery(requete).setParameter("libelleMatiere", libelleMatiere).getResultList();
        if(! org.apache.commons.collections.CollectionUtils.isEmpty(idEnseignement)){
        return idEnseignement.get(0);
        }else{
            return 0;
        }
    }
}
