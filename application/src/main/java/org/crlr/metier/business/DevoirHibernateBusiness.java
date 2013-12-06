/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirHibernateBusiness.java,v 1.37 2010/06/01 07:25:41 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.crlr.alimentation.Archive;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.TypeCategorieTypeDevoir;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.TypeReglesAdmin;
import org.crlr.dto.application.base.TypeReglesDevoir;
import org.crlr.dto.application.devoir.ChargeDevoirQO;
import org.crlr.dto.application.devoir.ChargeTravailDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.devoir.DevoirEnteteDTO;
import org.crlr.dto.application.devoir.RechercheDevoirQO;
import org.crlr.dto.application.devoir.RecherchePieceJointeDevoirQO;
import org.crlr.dto.application.devoir.ResultatRechercheDevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirQO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.entity.ClasseBean;
import org.crlr.metier.entity.DevoirBean;
import org.crlr.metier.entity.ElevesClassesBean;
import org.crlr.metier.entity.ElevesGroupesBean;
import org.crlr.metier.entity.EnseignantsClassesBean;
import org.crlr.metier.entity.EnseignantsGroupesBean;
import org.crlr.metier.entity.EnseignementLibelleBean;
import org.crlr.metier.entity.GroupeBean;
import org.crlr.metier.entity.GroupesClassesBean;
import org.crlr.metier.entity.OuvertureInspecteurBean;
import org.crlr.metier.entity.PieceJointeBean;
import org.crlr.metier.entity.PiecesJointesDevoirsBean;
import org.crlr.metier.entity.SeanceBean;
import org.crlr.metier.entity.SequenceBean;
import org.crlr.metier.entity.TypeDevoirBean;
import org.crlr.metier.utils.SchemaUtils;
import org.crlr.utils.Assert;
import org.crlr.utils.BooleanUtils;
import org.crlr.utils.ComparateurUtils;
import org.crlr.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.utils.FacesUtils;
import org.crlr.web.utils.FileUploadUtils;
import org.springframework.stereotype.Repository;

/**
 * DevoirHibernateBusiness.
 *
 * @author vibertd
 * @version $Revision: 1.37 $
 */
@Repository
public class DevoirHibernateBusiness extends AbstractBusiness
    implements DevoirHibernateBusinessService {
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public DevoirBean find(String id) {
        final DevoirBean devoir;

        final String query =
            "SELECT d FROM " + DevoirBean.class.getName() + " d WHERE d.id = :id";

        final List<DevoirBean> liste =
            getEntityManager().createQuery(query).setParameter("id", id).getResultList();

        if (!org.apache.commons.collections.CollectionUtils.isEmpty(liste)) {
            devoir = liste.get(0);
        } else {
            devoir = null;
        }

        return devoir;
    }

    /**
     * {@inheritDoc}
     */
    public Integer save(DevoirBean devoir) throws MetierException {
        getEntityManager().persist(devoir);
        getEntityManager().flush();

        return devoir.getId();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public DevoirDTO findDetailDevoir(final Integer idDevoir) {
        Assert.isNotNull("idDevoir", idDevoir);
        final DevoirDTO devoirDTO = new DevoirDTO();

        final String query =
            "SELECT new Map(" +
            "   d.dateRemise as date, " +
            "   d.intitule as intitule, " +
            "   d.description as description, " +
            "   d.idTypeDevoir as idType, " +
            "   d.idSeance as idSea, " +
            "   TD.libelle as libelleTypeDevoir, " +
            "   TD.categorie as categorieTypeDevoir," +
            "   SEA.date as dateSeance, " +
            "   SEQ.idClasse as idClasse, " +
            "   SEQ.idGroupe as idGroupe," +
            "   SEQ.idEnseignant as idEnseignant" +
            "   ) FROM " +
            DevoirBean.class.getName() + 
            " d LEFT JOIN d.typeDevoir as TD LEFT JOIN d.seance SEA " +
            " LEFT JOIN SEA.sequence SEQ " +
            "WHERE " + "d.id = :id";

        final List<Map<String, ?>> liste =
            getEntityManager().createQuery(query).setParameter("id", idDevoir)
                .getResultList();

        if (!org.apache.commons.collections.CollectionUtils.isEmpty(liste)) {
            final Map<String, ?> mapDev = liste.get(0);
            final RecherchePieceJointeDevoirQO recherchePJ = new RecherchePieceJointeDevoirQO();
            recherchePJ.setIdDevoir(idDevoir);
            devoirDTO.setDateRemise((Date) mapDev.get("date"));
            devoirDTO.setIntitule((String) mapDev.get("intitule"));
            devoirDTO.setDescription((String) mapDev.get("description"));
            devoirDTO.setFiles(new ArrayList<FileUploadDTO>(findPieceJointeDevoir(recherchePJ)));
            devoirDTO.setId(idDevoir);
            devoirDTO.setIdClasse((Integer) mapDev.get("idClasse"));
            devoirDTO.setIdGroupe((Integer) mapDev.get("idGroupe"));
            devoirDTO.setIdSeance((Integer) mapDev.get("idSea"));
            devoirDTO.getTypeDevoirDTO().setId((Integer) mapDev.get("idType"));
            devoirDTO.getTypeDevoirDTO().setLibelle((String) mapDev.get("libelleTypeDevoir"));
            devoirDTO.getTypeDevoirDTO().setCategorie(
                    TypeCategorieTypeDevoir.getMapCategorieTypeDevoir().get((String) mapDev.get("categorieTypeDevoir")));
            final SeanceDTO seance = new SeanceDTO();
            seance.setDate((Date) mapDev.get("dateSeance"));
            seance.setId((Integer) mapDev.get("idSea"));
            
            Integer idClasse = (Integer) mapDev.get("idClasse");
            if (idClasse != null) {
                seance.getSequenceDTO().getGroupesClassesDTO().setId(idClasse);
                seance.getSequenceDTO().getGroupesClassesDTO().setTypeGroupe(TypeGroupe.CLASSE);
            } else {
                seance.getSequenceDTO().getGroupesClassesDTO().setId((Integer) mapDev.get("idGroupe"));
                seance.getSequenceDTO().getGroupesClassesDTO().setTypeGroupe(TypeGroupe.GROUPE);
            }
            
            seance.setIdEnseignant((Integer) mapDev.get("idEnseignant"));
            
            
            
            devoirDTO.setSeance(seance);
        }

        return devoirDTO;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<TypeDevoirDTO>> findListeTypeDevoir(final Integer idEtablissement) {
        Assert.isNotNull("idEtablissement", idEtablissement);

        
        final ResultatDTO<List<TypeDevoirDTO>> resultat =
            new ResultatDTO<List<TypeDevoirDTO>>();
        final List<TypeDevoirDTO> listeTypeDevoirDTO = new ArrayList<TypeDevoirDTO>();

        final String query =
            "SELECT new Map(t.id as id, t.code as code, t.libelle as libelle, t.categorie as categorie) FROM " +
            TypeDevoirBean.class.getName() +
            " t WHERE t.idEtablissement=:idEtablissement ORDER BY t.code ASC";

        final List<Map<String, ?>> listeTypeDevoir =
            getEntityManager().createQuery(query)
                .setParameter("idEtablissement", idEtablissement).getResultList();

        for (Map<String, ?> mapTypeDevoir : listeTypeDevoir) {
            final TypeDevoirDTO typeDevoirDTO = new TypeDevoirDTO();
            typeDevoirDTO.setId((Integer) mapTypeDevoir.get("id"));
            typeDevoirDTO.setCode((String) mapTypeDevoir.get("code"));
            typeDevoirDTO.setLibelle((String) mapTypeDevoir.get("libelle"));
            typeDevoirDTO.setCategorie(TypeCategorieTypeDevoir.getMapCategorieTypeDevoir().get((String) mapTypeDevoir.get("categorie")));
            listeTypeDevoirDTO.add(typeDevoirDTO);
        }

        final List<TypeDevoirDTO> listeTypeDevoirDTOSorted =
            ComparateurUtils.sort(listeTypeDevoirDTO, "libelle");
        resultat.setValeurDTO(listeTypeDevoirDTOSorted);

        return resultat;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void saveTypeDevoir(final TypeDevoirQO typeDevoirQO)
                        throws MetierException {
        Assert.isNotNull("typeDevoirQO", typeDevoirQO);
        Assert.isNotNull("categorieTypeDevoir", typeDevoirQO.getCategorie());

        final Integer idEtablissement = typeDevoirQO.getIdEtablissement();
        Assert.isNotNull("idEtablissement", idEtablissement);

        final Integer idTypeDevoir = typeDevoirQO.getId();
        final String libelle = StringUtils.trimToEmpty(typeDevoirQO.getLibelle());

        //contrôle de la non existance du libellé
        String queryCheck =
            "SELECT 1 FROM " + TypeDevoirBean.class.getName() +
            " T WHERE T.idEtablissement=:idEtablissement AND T.libelle=:libelle";
        if (idTypeDevoir != null) {
            queryCheck += " AND T.id != "  + idTypeDevoir.toString();
        }

        final List<Integer> listeCheck =
            getEntityManager().createQuery(queryCheck)
                .setParameter("idEtablissement", idEtablissement)
                .setParameter("libelle", libelle).getResultList();

        if (!org.apache.commons.collections.CollectionUtils.isEmpty(listeCheck)) {
            final ConteneurMessage cm = new ConteneurMessage();
            cm.add(new Message(TypeReglesAdmin.ADMIN_11.name(), "du type de devoir"));
            throw new MetierException(cm,
                                      "Echec de l'ajout ou de la mise à jour d'un type de devoir");
        }

        //création
        if (idTypeDevoir == null) {
            final BaseHibernateBusiness baseHibernateBusiness =
                new BaseHibernateBusiness(this.getEntityManager());
            final Integer id =
                baseHibernateBusiness.getIdInsertion("cahier_type_devoir");
            final TypeDevoirBean typeDevoirBean = new TypeDevoirBean();
            typeDevoirBean.setId(id);
            typeDevoirBean.setCode("TD" + id);
            typeDevoirBean.setIdEtablissement(idEtablissement);
            typeDevoirBean.setLibelle(libelle);
            typeDevoirBean.setCategorie(typeDevoirQO.getCategorie().getId());

            getEntityManager().persist(typeDevoirBean);
        } else {
            //mise à jour
            final String queryUpdate =
                "UPDATE " + TypeDevoirBean.class.getName() +
                " T SET T.libelle=:libelle, T.categorie=:categorie WHERE T.id=:idTypeDevoir";
            getEntityManager().createQuery(queryUpdate)
                .setParameter("libelle", libelle)
                .setParameter("categorie", typeDevoirQO.getCategorie().getId())
                .setParameter("idTypeDevoir", idTypeDevoir)
                .executeUpdate();
        }

        getEntityManager().flush();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void deleteTypeDevoir(final Integer idTypeDevoir)
                          throws MetierException {
        Assert.isNotNull("idTypeDevoir", idTypeDevoir);

        final String queryCheck =
            "SELECT 1 FROM " + DevoirBean.class.getName() +
            " D WHERE D.idTypeDevoir=:idTypeDevoir";

        final List<Integer> listeCheck =
            getEntityManager().createQuery(queryCheck)
                .setParameter("idTypeDevoir", idTypeDevoir).getResultList();

        if (!org.apache.commons.collections.CollectionUtils.isEmpty(listeCheck)) {
            final ConteneurMessage cm = new ConteneurMessage();
            cm.add(new Message(TypeReglesAdmin.ADMIN_12.name()));
            throw new MetierException(cm, "Echec de la suppression d'un type de devoir");
        }

        final String query =
            "DELETE FROM " + TypeDevoirBean.class.getName() + " T WHERE T.id=:id";

        getEntityManager().createQuery(query).setParameter("id", idTypeDevoir)
            .executeUpdate();
        getEntityManager().flush();
    }

    /**
     * {@inheritDoc}
     */
    public Integer saveDevoir(DevoirDTO devoirDTO)
                       throws MetierException {
        final DevoirBean devoirBean = new DevoirBean();
        final BaseHibernateBusiness baseHibernateBusiness =
            new BaseHibernateBusiness(this.getEntityManager());
        final Integer idDevoir =
            baseHibernateBusiness.getIdInsertion("cahier_devoir");
        devoirBean.setId(idDevoir);
        devoirBean.setCode("DEV" + idDevoir);
        devoirBean.setDateRemise(devoirDTO.getDateRemise());
        devoirBean.setIntitule(devoirDTO.getIntitule());
        devoirBean.setDescription(devoirDTO.getDescription());
        devoirBean.setIdTypeDevoir(devoirDTO.getTypeDevoirDTO().getId());
        devoirBean.setIdSeance(devoirDTO.getIdSeance());

        return this.save(devoirBean);
    }

    /**
     * {@inheritDoc}
     */
    public void updateDevoir(final DevoirDTO devoirDTO) {
        Assert.isNotNull("devoirDTO", devoirDTO);

        final String queryUpdate =
            "UPDATE " + DevoirBean.class.getName() +
            " d SET d.dateRemise=:dateRemise, d.intitule=:intitule, " +
            "d.description=:description,d.idTypeDevoir=:idTypeDevoir WHERE " +
            "d.id=:id";

        getEntityManager().createQuery(queryUpdate)
            .setParameter("dateRemise", devoirDTO.getDateRemise())
            .setParameter("intitule", devoirDTO.getIntitule())
            .setParameter("description", devoirDTO.getDescription())
            .setParameter("idTypeDevoir", devoirDTO.getTypeDevoirDTO().getId())
            .setParameter("id", devoirDTO.getId()).executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    public void delete(Integer idDevoir) throws MetierException {

        final String query =
            "DELETE FROM " + DevoirBean.class.getName() + " D WHERE D.id = :id";

        @SuppressWarnings("unused")
        final Integer resultat =
            getEntityManager().createQuery(query).setParameter("id", idDevoir)
                .executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<ResultatRechercheDevoirDTO>> listeDevoirAffichage(RechercheDevoirQO rechercheDevoirQO)
        throws MetierException {
        final Integer idInspecteur = rechercheDevoirQO.getIdInspecteur();
        final Integer idEnseignant = rechercheDevoirQO.getIdEnseignant();
        final Integer idEleve = rechercheDevoirQO.getIdEleve();
        
        if(idInspecteur != null){
            return listeDevoirAffichageInspecteur(rechercheDevoirQO);
        } else if (idEnseignant != null) {
            return listeDevoirAffichageEnseignant(rechercheDevoirQO);
        } else if (idEleve != null) {
            return listeDevoirAffichageEleve(rechercheDevoirQO);
        }

        return null;
    }
    
    /**
     * Recherche des devoirs pour un enseignant.
     *
     * @param rechercheDevoirQO rechercheDevoirQO
     *
     * @return La liste des devoirs
     *
     * @throws MetierException Exception
     */
    @SuppressWarnings("unchecked")  
    private ResultatDTO<List<ResultatRechercheDevoirDTO>> listeDevoirAffichageInspecteur(RechercheDevoirQO rechercheDevoirQO)
        throws MetierException {
        final ResultatDTO<List<ResultatRechercheDevoirDTO>> resultat =
            new ResultatDTO<List<ResultatRechercheDevoirDTO>>();

        final List<ResultatRechercheDevoirDTO> listeResultatRechercheDevoirDTO =
            new ArrayList<ResultatRechercheDevoirDTO>();

        final Integer idInspecteur = rechercheDevoirQO.getIdInspecteur();
        final TypeGroupe typeGroupeClasse = rechercheDevoirQO.getTypeGroupeSelectionne();
        Integer idGroupe = null;
        Integer idClasse = null;

        String requete1 = "";
        String requete2 = "";

        final String listeSelect =
        " SEA.id as idSeance, " + 
        " SEA.code as codeSeance, " +
        " SEA.date as dateSeance, " +
        " SEA.heureDebut as heureDebutSeance, " +
        " SEA.intitule as intituleSeance, " +
        " SEA.heureDebut as heureDebutSeance, " +
        " SEQ.id as idSequence, " + 
        " SEQ.intitule as intituleSequence, " +
        " SEQ.code as codeSequence, " +
        " SEQ.idEnseignement as idEnseignement," +
        " ENS.designation as designationEnseignement," +
        " ENSEI.nom as nomEnseignant ," +
        " ENSEI.prenom as prenomEnseignant ," +
        " ENSEI.civilite as civiliteEnseignant ," +
        " SEA.idEnseignant as idEnseignant, " +
        " D.idTypeDevoir as idTypeDevoir," +
        " D.dateRemise as dateRemise," + 
        " D.intitule as intituleDevoir," +
        " D.description as descriptionDevoir," +
        " TD.libelle as libelleTypeDevoir";
        
        final String clauseCategorie;
        if (rechercheDevoirQO.getTypeCategorie() != null) {
            clauseCategorie = " AND TD.categorie = '" + rechercheDevoirQO.getTypeCategorie().getId() + "'";
        } else {
            clauseCategorie = "";
        }
        final String clauseEnseignement;
        if (rechercheDevoirQO.getIdEnseignement() != null) {
            clauseEnseignement = " AND SEQ.idEnseignement = " + rechercheDevoirQO.getIdEnseignement();
        } else {
            clauseEnseignement = "";
        }
        
        
        final List<Map<String, ?>> resultatQuery = new ArrayList<Map<String, ?>>();

        if (TypeGroupe.CLASSE  == (typeGroupeClasse)) {
            //CLASSE

            //Liste des groupes selectionnés
            final List<GroupeDTO> listeGroupeDTO = rechercheDevoirQO.getListeGroupeDTO();
            String listeGroupe = "(0";
            for (GroupeDTO groupe : listeGroupeDTO) {
                listeGroupe += (", " + groupe.getId());
            }
            listeGroupe += ")";

            idClasse = rechercheDevoirQO.getGroupeClasseSelectionne().getId();

            requete1 = "SELECT " + " distinct new map(D.id as idDevoir, " +
                       " CLA.code as codeClasse, " +
                       " CLA.designation as designationClasse, " +
                       listeSelect +") " + " FROM " +
                       EnseignantsClassesBean.class.getName() + " EC " +
                       " INNER JOIN EC.classe CLA " +
                       " INNER JOIN EC.enseignant ENSEI, " +
                       SequenceBean.class.getName() + " SEQ " +
                       " INNER JOIN SEQ.enseignement ENS, " + 
                       DevoirBean.class.getName() + " D " + 
                       " INNER JOIN D.seance SEA " +
                       " LEFT JOIN D.typeDevoir TD, " +
                       OuvertureInspecteurBean.class.getName() +" OUV " +
                       " WHERE " + 
                           " OUV.inspecteur.id = :idInspecteur AND OUV.etablissement.id = :idEtablissement AND " +
                           " CLA.id = SEQ.idClasse AND " + 
                           " CLA.id = :idClasse AND " +
                           " ENSEI.id = SEQ.idEnseignant AND " +
                           " SEQ.id = SEA.idSequence AND " +
                           " D.dateRemise >= :premierJourSemaine AND " +
                           " D.dateRemise <= :dernierJourSemaine AND" + 
                           " SEQ.idEnseignant = OUV.enseignant.id AND " +
                           " SEA.date <= :jourCourant " + clauseCategorie + clauseEnseignement;

            requete2 = "SELECT " + " distinct new map(D.id as idDevoir, " +
                       " GRP.code as codeGroupe, " +
                       " GRP.designation as designationGroupe, " +
                       listeSelect +") " + " FROM " +
                       EnseignantsGroupesBean.class.getName() + " EG " +
                       " INNER JOIN EG.groupe GRP " +
                       " INNER JOIN EG.enseignant ENSEI, " +
                       SequenceBean.class.getName() + " SEQ " +
                       " INNER JOIN SEQ.enseignement ENS, " + 
                       DevoirBean.class.getName() +
                       " D " + " INNER JOIN D.seance SEA " +
                       " LEFT JOIN D.typeDevoir TD, " + 
                       OuvertureInspecteurBean.class.getName() +" OUV " +
                       " WHERE " +            
                           " OUV.inspecteur.id = :idInspecteur AND OUV.etablissement.id = :idEtablissement AND " +
                           " GRP.id = SEQ.idGroupe AND " + " GRP.id IN " + listeGroupe +
                           " AND " + " ENSEI.id = SEQ.idEnseignant AND " +
                           " SEQ.id = SEA.idSequence AND " +
                           " D.dateRemise >= :premierJourSemaine AND " +
                           " D.dateRemise <= :dernierJourSemaine AND " +
                           " SEQ.idEnseignant = OUV.enseignant.id AND " +
                           " SEA.date <= :jourCourant " + clauseCategorie + clauseEnseignement;
            

            final Query query1 = getEntityManager().createQuery(requete1);
            final Query query2 = getEntityManager().createQuery(requete2);

            query1.setParameter("idClasse", idClasse);
            query1.setParameter("idInspecteur", idInspecteur);
            query1.setParameter("idEtablissement", rechercheDevoirQO.getIdEtablissement());
            query1.setParameter("premierJourSemaine",
                                rechercheDevoirQO.getPremierJourSemaine());
            query1.setParameter("dernierJourSemaine",
                                rechercheDevoirQO.getDernierJourSemaine());
            query1.setParameter("jourCourant", rechercheDevoirQO.getJourCourant());

            query2.setParameter("idInspecteur", idInspecteur);
            query2.setParameter("idEtablissement", rechercheDevoirQO.getIdEtablissement());
            query2.setParameter("premierJourSemaine",
                                rechercheDevoirQO.getPremierJourSemaine());
            query2.setParameter("dernierJourSemaine",
                                rechercheDevoirQO.getDernierJourSemaine());
            query2.setParameter("jourCourant", rechercheDevoirQO.getJourCourant());

            resultatQuery.addAll(query1.getResultList());
            resultatQuery.addAll(query2.getResultList());
            
        } else if (TypeGroupe.GROUPE  == (typeGroupeClasse)) {
            //GROUPE
            idGroupe = rechercheDevoirQO.getGroupeClasseSelectionne().getId();

            requete1 = "SELECT " + " distinct new map(D.id as idDevoir, " +
                       " GRP.code as codeGroupe, " +
                       " GRP.designation as designationGroupe, " +
                       listeSelect +") " + " FROM " +
                       EnseignantsGroupesBean.class.getName() + " EG " +
                       " INNER JOIN EG.groupe GRP " +
                       " INNER JOIN EG.enseignant ENSEI, " +
                       SequenceBean.class.getName() + " SEQ " +
                       " INNER JOIN SEQ.enseignement ENS, " + 
                       DevoirBean.class.getName() +
                       " D " + " INNER JOIN D.seance SEA " +
                       " LEFT JOIN D.typeDevoir TD, " + 
                       OuvertureInspecteurBean.class.getName() +" OUV " +
                       " WHERE " + 
                           " OUV.inspecteur.id = :idInspecteur AND OUV.etablissement.id = :idEtablissement AND " +
                           " GRP.id = SEQ.idGroupe AND " + " GRP.id = :idGroupe AND " +
                           " ENSEI.id = SEQ.idEnseignant AND " +
                           " SEQ.id = SEA.idSequence AND " +
                           " D.dateRemise <= :dernierJourSemaine AND " +
                           " D.dateRemise >= :premierJourSemaine AND " +
                           " SEQ.idEnseignant = OUV.enseignant.id AND " +
                           " SEA.date <= :jourCourant " + clauseCategorie + clauseEnseignement;

            final Query query1 = getEntityManager().createQuery(requete1);

            query1.setParameter("idGroupe", idGroupe);
            query1.setParameter("idInspecteur", idInspecteur);
            query1.setParameter("idEtablissement", rechercheDevoirQO.getIdEtablissement());
            query1.setParameter("premierJourSemaine",
                                rechercheDevoirQO.getPremierJourSemaine());
            query1.setParameter("dernierJourSemaine",
                                rechercheDevoirQO.getDernierJourSemaine());
            query1.setParameter("jourCourant", rechercheDevoirQO.getJourCourant());

            resultatQuery.addAll(query1.getResultList());
        }

        for (Map<String, ?> result : resultatQuery) {
            final ResultatRechercheDevoirDTO resultatRechercheDevoirDTO = extractInfoRechercheEnseignant(result);
            listeResultatRechercheDevoirDTO.add(resultatRechercheDevoirDTO);
        }
        resultat.setValeurDTO(ComparateurUtils.sort(listeResultatRechercheDevoirDTO, "dateRemiseDevoir"));
        return resultat;
    }

    /**
     * Extrait les resultat de la recherche.
     * @param result .
     * @return .
     */
    private ResultatRechercheDevoirDTO extractInfoRechercheEnseignant(
            Map<String, ?> result) {
        final ResultatRechercheDevoirDTO resultatRechercheDevoirDTO =
            new ResultatRechercheDevoirDTO();
        final Integer idDevoir = (Integer) result.get("idDevoir");
        resultatRechercheDevoirDTO.setIdDevoir(idDevoir);
        resultatRechercheDevoirDTO.setIdSequence((Integer) result.get("idSequence"));
        resultatRechercheDevoirDTO.setIdSeance((Integer) result.get("idSeance"));
        resultatRechercheDevoirDTO.setIdClasseGroupe((Integer) result.get("idClasse"));
        resultatRechercheDevoirDTO.setCodeClasse((String) result.get("codeClasse"));
        resultatRechercheDevoirDTO.setIdGroupe((Integer) result.get("idGroupe"));
        resultatRechercheDevoirDTO.setCodeGroupe((String) result.get("codeGroupe"));
        resultatRechercheDevoirDTO.setDesignationGroupe((String) result.get("designationGroupe"));
        resultatRechercheDevoirDTO.setDesignationClasse((String) result.get("designationClasse"));
        resultatRechercheDevoirDTO.setCodeSeance((String) result.get("codeSeance"));
        resultatRechercheDevoirDTO.setIdEnseignement((Integer) result.get("idEnseignement"));
        resultatRechercheDevoirDTO.setDesignationEnseignement((String) result.get("designationEnseignement"));
        resultatRechercheDevoirDTO.setNomEnseignant((String) result.get("nomEnseignant"));
        resultatRechercheDevoirDTO.setPrenomEnseignant((String) result.get("prenomEnseignant"));
        resultatRechercheDevoirDTO.setCiviliteEnseignant((String) result.get("civiliteEnseignant"));
        resultatRechercheDevoirDTO.setIdEnseignant((Integer) result.get("idEnseignant"));
        resultatRechercheDevoirDTO.setIdTypeDevoir((Integer) result.get("idTypeDevoir"));
        resultatRechercheDevoirDTO.setLibelleTypeDevoir((String) result.get("libelleTypeDevoir"));
        resultatRechercheDevoirDTO.setDateRemiseDevoir((Date) result.get("dateRemise"));
        resultatRechercheDevoirDTO.setIntituleDevoir((String) result.get("intituleDevoir"));
        resultatRechercheDevoirDTO.setDescriptionDevoir((String) result.get("descriptionDevoir"));
        return resultatRechercheDevoirDTO;
    }


    /**
     * Recherche des devoirs pour un enseignant.
     *
     * @param rechercheDevoirQO rechercheDevoirQO
     *
     * @return La liste des devoirs
     *
     * @throws MetierException Exception
     */
    @SuppressWarnings("unchecked")
    private ResultatDTO<List<ResultatRechercheDevoirDTO>> listeDevoirAffichageEnseignant(RechercheDevoirQO rechercheDevoirQO)
        throws MetierException {
        final ResultatDTO<List<ResultatRechercheDevoirDTO>> resultat =
            new ResultatDTO<List<ResultatRechercheDevoirDTO>>();

        final List<ResultatRechercheDevoirDTO> listeResultatRechercheDevoirDTO =
            new ArrayList<ResultatRechercheDevoirDTO>();

        final Integer idEnseignant = rechercheDevoirQO.getIdEnseignant();
        final TypeGroupe typeGroupeClasse = rechercheDevoirQO.getTypeGroupeSelectionne();
        Integer idGroupe = null;
        Integer idClasse = null;

        String requete1 = "";
        String requete2 = "";

        final String listeSelect =" SEA.id as idSeance, " + " SEA.code as codeSeance, " +
        " SEA.date as dateSeance, " +
        " SEA.heureDebut as heureDebutSeance, " +
        " SEA.intitule as intituleSeance, " +
        " SEA.heureDebut as heureDebutSeance, " +
        " SEQ.id as idSequence, " + 
        " SEQ.intitule as intituleSequence, " +
        " SEQ.code as codeSequence, " +
        " SEQ.idEnseignement as idEnseignement," +
        " ENS.designation as designationEnseignement," +
        " ENSEI.nom as nomEnseignant ," +
        " ENSEI.prenom as prenomEnseignant ," +
        " ENSEI.civilite as civiliteEnseignant ," +
        " SEA.idEnseignant as idEnseignant, " +
        " D.idTypeDevoir as idTypeDevoir," +
        " D.dateRemise as dateRemise," + 
        " D.intitule as intituleDevoir," +
        " D.description as descriptionDevoir," +
        " TD.libelle as libelleTypeDevoir";
        
        final List<Map<String, ?>> resultatQuery = new ArrayList<Map<String, ?>>();

        final String clauseCategorie;
        if (rechercheDevoirQO.getTypeCategorie() != null) {
            clauseCategorie = " TD.categorie = '" + rechercheDevoirQO.getTypeCategorie().getId() + "' AND ";
        } else {
            clauseCategorie = "";
        }
        final String clauseEnseignement;
        if (rechercheDevoirQO.getIdEnseignement() != null) {
            clauseEnseignement = " SEQ.idEnseignement = " + rechercheDevoirQO.getIdEnseignement() + " AND ";
        } else {
            clauseEnseignement = "";
        }
        
        
        if (TypeGroupe.CLASSE  == (typeGroupeClasse)) {
            //CLASSE

            //Liste des groupes selectionnés
            final List<GroupeDTO> listeGroupeDTO = rechercheDevoirQO.getListeGroupeDTO();
            String listeGroupe = "(0";
            for (GroupeDTO groupe : listeGroupeDTO) {
                listeGroupe += (", " + groupe.getId());
            }
            listeGroupe += ")";

            idClasse = rechercheDevoirQO.getGroupeClasseSelectionne().getId();

            requete1 = "SELECT " + " distinct new map(D.id as idDevoir, " +
                       " CLA.id as idClasse, " +
                       " CLA.code as codeClasse, " +
                       " CLA.designation as designationClasse, " +
                       listeSelect + " ) " + " FROM " +
                       EnseignantsClassesBean.class.getName() + " EC " +
                       " INNER JOIN EC.classe CLA " +
                       " INNER JOIN EC.enseignant ENSEI, " +
                       SequenceBean.class.getName() + " SEQ " +
                       " INNER JOIN SEQ.enseignement ENS, " + DevoirBean.class.getName() +
                       " D " + " INNER JOIN D.seance SEA " +
                       " LEFT JOIN D.typeDevoir TD " + " WHERE " + clauseCategorie + clauseEnseignement + " (( " +
                       " CLA.id = SEQ.idClasse AND " + " CLA.id = :idClasse AND " +
                       " ENSEI.id = SEQ.idEnseignant AND " +
                       " SEQ.id = SEA.idSequence AND " +
                       " SEQ.idEnseignant = :idEnseignant AND " +
                       " D.dateRemise >= :premierJourSemaine AND " +
                       " D.dateRemise <= :dernierJourSemaine " + " ) " + " OR " + " ( " +
                       " CLA.id = SEQ.idClasse AND " + " CLA.id = :idClasse AND " +
                       " ENSEI.id = SEQ.idEnseignant AND " +
                       " SEQ.id = SEA.idSequence AND " +
                       " SEQ.idEnseignant != :idEnseignant AND " +
                       " SEA.date <= :jourCourant AND " +
                       " D.dateRemise >= :premierJourSemaine AND " +
                       " D.dateRemise <= :dernierJourSemaine " + " )) ";

            requete2 = "SELECT " + " distinct new map(D.id as idDevoir, " +
                       " GRP.id as idGroupe, " +
                       " GRP.code as codeGroupe, " +
                       " GRP.designation as designationGroupe, " +
                       listeSelect + " ) " + " FROM " +
                       EnseignantsGroupesBean.class.getName() + " EG " +
                       " INNER JOIN EG.groupe GRP " +
                       " INNER JOIN EG.enseignant ENSEI, " +
                       SequenceBean.class.getName() + " SEQ " +
                       " INNER JOIN SEQ.enseignement ENS, " + DevoirBean.class.getName() +
                       " D " + " INNER JOIN D.seance SEA " +
                       " LEFT JOIN D.typeDevoir TD " + " WHERE " +  clauseCategorie + clauseEnseignement + " (( " +
                       " GRP.id = SEQ.idGroupe AND " + " GRP.id IN " + listeGroupe +
                       " AND " + " ENSEI.id = SEQ.idEnseignant AND " +
                       " SEQ.id = SEA.idSequence AND " +
                       " SEQ.idEnseignant = :idEnseignant AND " +
                       " D.dateRemise >= :premierJourSemaine AND " +
                       " D.dateRemise <= :dernierJourSemaine " + " ) " + " OR " + " ( " +
                       " GRP.id = SEQ.idGroupe AND " + " GRP.id IN " + listeGroupe +
                       " AND " + " ENSEI.id = SEQ.idEnseignant AND " +
                       " SEQ.id = SEA.idSequence AND " +
                       " SEQ.idEnseignant != :idEnseignant AND " +
                       " SEA.date <= :jourCourant AND " +
                       " D.dateRemise >= :premierJourSemaine AND " +
                       " D.dateRemise <= :dernierJourSemaine " + " )) ";

            final Query query1 = getEntityManager().createQuery(requete1);
            final Query query2 = getEntityManager().createQuery(requete2);

            query1.setParameter("idClasse", idClasse);
            query1.setParameter("idEnseignant", idEnseignant);
            query1.setParameter("premierJourSemaine",
                                rechercheDevoirQO.getPremierJourSemaine());
            query1.setParameter("dernierJourSemaine",
                                rechercheDevoirQO.getDernierJourSemaine());
            query1.setParameter("jourCourant", rechercheDevoirQO.getJourCourant());

            query2.setParameter("idEnseignant", idEnseignant);
            query2.setParameter("premierJourSemaine",
                                rechercheDevoirQO.getPremierJourSemaine());
            query2.setParameter("dernierJourSemaine",
                                rechercheDevoirQO.getDernierJourSemaine());
            query2.setParameter("jourCourant", rechercheDevoirQO.getJourCourant());

            resultatQuery.addAll(query1.getResultList());
            resultatQuery.addAll(query2.getResultList());
        } else if (TypeGroupe.GROUPE  == (typeGroupeClasse)) {
            //GROUPE
            idGroupe = rechercheDevoirQO.getGroupeClasseSelectionne().getId();

            requete1 = "SELECT " + " distinct new map(D.id as idDevoir, " +
                       " GRP.code as codeGroupe, " +
                       " GRP.id as idGroupe, " +
                       " GRP.designation as designationGroupe, " +
                       listeSelect + " ) " + " FROM " +
                       EnseignantsGroupesBean.class.getName() + " EG " +
                       " INNER JOIN EG.groupe GRP " +
                       " INNER JOIN EG.enseignant ENSEI, " +
                       SequenceBean.class.getName() + " SEQ " +
                       " INNER JOIN SEQ.enseignement ENS, " + DevoirBean.class.getName() +
                       " D " + " INNER JOIN D.seance SEA " +
                       " LEFT JOIN D.typeDevoir TD " + " WHERE " + clauseCategorie + clauseEnseignement + " (( " +
                       " GRP.id = SEQ.idGroupe AND " + " GRP.id = :idGroupe AND " +
                       " ENSEI.id = SEQ.idEnseignant AND " +
                       " SEQ.id = SEA.idSequence AND " +
                       " SEQ.idEnseignant = :idEnseignant AND " +
                       " D.dateRemise >= :premierJourSemaine AND " +
                       " D.dateRemise <= :dernierJourSemaine " + " ) " + " OR " + " ( " +
                       " GRP.id = SEQ.idGroupe AND " + " GRP.id = :idGroupe AND " +
                       " ENSEI.id = SEQ.idEnseignant AND " +
                       " SEQ.id = SEA.idSequence AND " +
                       " SEQ.idEnseignant != :idEnseignant AND " +
                       " SEA.date <= :jourCourant AND " +
                       " D.dateRemise <= :dernierJourSemaine AND " +
                       " D.dateRemise >= :premierJourSemaine " + " )) ";

            final Query query1 = getEntityManager().createQuery(requete1);

            query1.setParameter("idGroupe", idGroupe);
            query1.setParameter("idEnseignant", idEnseignant);
            query1.setParameter("premierJourSemaine",
                                rechercheDevoirQO.getPremierJourSemaine());
            query1.setParameter("dernierJourSemaine",
                                rechercheDevoirQO.getDernierJourSemaine());
            query1.setParameter("jourCourant", rechercheDevoirQO.getJourCourant());

            resultatQuery.addAll(query1.getResultList());
        }

        for (Map<String, ?> result : resultatQuery) {
            
            final ResultatRechercheDevoirDTO resultatRechercheDevoirDTO =
                extractInfoRechercheEnseignant(result);
            listeResultatRechercheDevoirDTO.add(resultatRechercheDevoirDTO);
        }
        resultat.setValeurDTO(ComparateurUtils.sort(listeResultatRechercheDevoirDTO, "dateRemiseDevoir"));
        return resultat;
    }

    /**
     * Recherche des devoirs pour un eleve.
     *
     * @param rechercheDevoirQO rechercheDevoirQO
     *
     * @return La liste des devoirs
     *
     * @throws MetierException Exception
     */
    @SuppressWarnings("unchecked")
    private ResultatDTO<List<ResultatRechercheDevoirDTO>> listeDevoirAffichageEleve(RechercheDevoirQO rechercheDevoirQO)
        throws MetierException {
        final ResultatDTO<List<ResultatRechercheDevoirDTO>> resultat =
            new ResultatDTO<List<ResultatRechercheDevoirDTO>>();

        final Integer idEleve = rechercheDevoirQO.getIdEleve();
        final List<GroupeBean> listeGroupeBean = rechercheDevoirQO.getListeGroupeBean();
        final List<ClasseBean> listeClasseBean = rechercheDevoirQO.getListeClasseBean();

        final String clauseCategorie;
        if (rechercheDevoirQO.getTypeCategorie() != null) {
            clauseCategorie = " TD.categorie = '" + rechercheDevoirQO.getTypeCategorie().getId() + "' AND ";
        } else {
            clauseCategorie = "";
        }
        final String clauseEnseignement;
        if (rechercheDevoirQO.getIdEnseignement() != null) {
            clauseEnseignement = " SEQ.idEnseignement = " + rechercheDevoirQO.getIdEnseignement() + " AND ";
        } else {
            clauseEnseignement = "";
        }
        
        String listeGroupe = "(0";
        for (GroupeBean groupe : listeGroupeBean) {
            listeGroupe += (", " + groupe.getId());
        }
        listeGroupe += ")";

        String listeClasse = "(0";
        for (ClasseBean classe : listeClasseBean) {
            listeClasse += (", " + classe.getId());
        }
        listeClasse += ")";

        final String requete1 =
            "SELECT " + " distinct new map( " + " D.id as idDevoir, " +
            " CLA.code as codeClasse, " + " CLA.designation as designationClasse, " +
            " SEA.code as codeSeance, " + " SEA.id as idSeance, " +
            " SEA.date as dateSeance, " + " SEA.intitule as intituleSeance, " +
            " SEA.heureDebut as heureDebutSeance, " + " E.nom as nomEnseignant ," +
            " E.prenom as prenomEnseignant ," + " E.civilite as civiliteEnseignant ," +
            " SEQ.id as idSequence, " + " SEQ.intitule as intituleSequence, " +
            " ENS.designation as designationEnseignement, " +
            " ENS.id as idEnseignement, " + " SEQ.idEnseignement as idEnseignement," +
            " SEA.idEnseignant as idEnseignant, " + " D.idTypeDevoir as idTypeDevoir, " +
            " D.dateRemise as dateRemise, " + " D.intitule as intituleDevoir, " +
            " D.description as descriptionDevoir, " +
            " TD.libelle as libelleTypeDevoir) " + " FROM " +
            SequenceBean.class.getName() + " SEQ " +
            " INNER JOIN SEQ.enseignement ENS " + 
            " INNER JOIN SEQ.classe CLA, " + SeanceBean.class.getName() + " SEA " +
            " INNER JOIN SEA.enseignant E, " + ElevesClassesBean.class.getName() + " EC, " + 
            DevoirBean.class.getName() + " D " +
            " LEFT JOIN D.typeDevoir TD " + " WHERE " + clauseCategorie + clauseEnseignement + " D.idSeance = SEA.id AND " +
            " CLA.id = SEQ.idClasse AND " + " EC.pk.idEleve = :idEleve AND " +
            " SEQ.id = SEA.idSequence AND " + " SEQ.idClasse IN " + listeClasse +
            " AND " + " SEA.date <= :jourCourant AND " +
            " D.dateRemise >= :premierJourSemaine AND " +
            " D.dateRemise <= :dernierJourSemaine ";

        final String requete2 =
            "SELECT " + " distinct new map( " + " D.id as idDevoir, " +
            " GRP.code as codeGroupe, " + " GRP.designation as designationGroupe, " +
            " SEA.code as codeSeance, " + " SEA.id as idSeance, " +
            " SEA.date as dateSeance, " + " SEA.intitule as intituleSeance, " +
            " SEA.heureDebut as heureDebutSeance, " + " E.nom as nomEnseignant ," +
            " E.prenom as prenomEnseignant ," + " E.civilite as civiliteEnseignant ," +
            " SEQ.id as idSequence, " + " SEQ.intitule as intituleSequence, " +
            " ENS.designation as designationEnseignement, " +
            " ENS.id as idEnseignement, " + " SEQ.idEnseignement as idEnseignement," +
            " SEA.idEnseignant as idEnseignant, " + " D.idTypeDevoir as idTypeDevoir, " +
            " D.dateRemise as dateRemise, " + " D.intitule as intituleDevoir, " +
            " D.description as descriptionDevoir, " +
            " TD.libelle as libelleTypeDevoir) " + " FROM " +
            SequenceBean.class.getName() + " SEQ " +
            " INNER JOIN SEQ.enseignement ENS " +
            " INNER JOIN SEQ.groupe GRP, " + SeanceBean.class.getName() + " SEA " +
            "INNER JOIN SEA.enseignant E, " + ElevesGroupesBean.class.getName() +
            " EG, " + DevoirBean.class.getName() + " D " +
            " LEFT JOIN D.typeDevoir TD " + " WHERE " + clauseCategorie + clauseEnseignement + " D.idSeance = SEA.id AND " +
            " GRP.id = EG.pk.idGroupe AND " + " EG.pk.idEleve = :idEleve AND " +
            " SEQ.id = SEA.idSequence AND " + " SEQ.idGroupe IN " + listeGroupe +
            " AND " + " SEA.date <= :jourCourant AND " +
            " D.dateRemise >= :premierJourSemaine AND " +
            " D.dateRemise <= :dernierJourSemaine ";

        final Query query1 = getEntityManager().createQuery(requete1);
        final Query query2 = getEntityManager().createQuery(requete2);

        query1.setParameter("idEleve", idEleve);
        query1.setParameter("jourCourant", rechercheDevoirQO.getJourCourant());
        query1.setParameter("premierJourSemaine",
                            rechercheDevoirQO.getPremierJourSemaine());
        query1.setParameter("dernierJourSemaine",
                            rechercheDevoirQO.getDernierJourSemaine());

        query2.setParameter("idEleve", idEleve);
        query2.setParameter("jourCourant", rechercheDevoirQO.getJourCourant());
        query2.setParameter("premierJourSemaine",
                            rechercheDevoirQO.getPremierJourSemaine());
        query2.setParameter("dernierJourSemaine",
                            rechercheDevoirQO.getDernierJourSemaine());

        final List<Map<String, ?>> resultatQuery = new ArrayList<Map<String, ?>>();
        resultatQuery.addAll(query1.getResultList());
        resultatQuery.addAll(query2.getResultList());

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
                .setParameter("idEtablissement", rechercheDevoirQO.getIdEtablissement())
                .setParameter("idEnseignements", mapEnseignement.keySet())
                .getResultList();

            mapLibellePersoEnseignement = org.crlr.utils.CollectionUtils.formatMapClefValeur(resultatLibelleQuery,
                    "idEnseignement", "designation");
        } else {          
            mapLibellePersoEnseignement = new HashMap<Integer, String>();         
        }

        final List<ResultatRechercheDevoirDTO> listeResultatRechercheDevoirDTO =
            new ArrayList<ResultatRechercheDevoirDTO>();
        for (Map<String, ?> result : resultatQuery) {
            final ResultatRechercheDevoirDTO resultatRechercheDevoirDTO =
                new ResultatRechercheDevoirDTO();
            final Integer idDevoir = (Integer) result.get("idDevoir");
            resultatRechercheDevoirDTO.setIdDevoir(idDevoir);
            resultatRechercheDevoirDTO.setIdSequence((Integer) result.get("idSequence"));
            resultatRechercheDevoirDTO.setIdSeance((Integer) result.get("idSeance"));
            resultatRechercheDevoirDTO.setCodeClasse((String) result.get("codeClasse"));
            resultatRechercheDevoirDTO.setCodeGroupe((String) result.get("codeGroupe"));
            resultatRechercheDevoirDTO.setDesignationGroupe((String) result.get("designationGroupe"));
            resultatRechercheDevoirDTO.setDesignationClasse((String) result.get("designationClasse"));
            resultatRechercheDevoirDTO.setCodeSeance((String) result.get("codeSeance"));
            resultatRechercheDevoirDTO.setIdTypeDevoir((Integer) result.get("idTypeDevoir"));
            final Integer idEnseignement = (Integer) result.get("idEnseignement");
            if (mapLibellePersoEnseignement.containsKey(idEnseignement)) {
                resultatRechercheDevoirDTO.setDesignationEnseignement((String) mapLibellePersoEnseignement.get(idEnseignement));
            } else {
                resultatRechercheDevoirDTO.setDesignationEnseignement((String) result.get("designationEnseignement"));
            }
            resultatRechercheDevoirDTO.setNomEnseignant((String) result.get("nomEnseignant"));
            resultatRechercheDevoirDTO.setPrenomEnseignant((String) result.get("prenomEnseignant"));
            resultatRechercheDevoirDTO.setCiviliteEnseignant((String) result.get("civiliteEnseignant"));
            resultatRechercheDevoirDTO.setLibelleTypeDevoir((String) result.get("libelleTypeDevoir"));
            resultatRechercheDevoirDTO.setDateRemiseDevoir((Date) result.get("dateRemise"));
            resultatRechercheDevoirDTO.setIntituleDevoir((String) result.get("intituleDevoir"));
            resultatRechercheDevoirDTO.setDescriptionDevoir((String) result.get("descriptionDevoir"));
            listeResultatRechercheDevoirDTO.add(resultatRechercheDevoirDTO);
        }

        if (org.apache.commons.collections.CollectionUtils.isEmpty(listeResultatRechercheDevoirDTO) &&
                (rechercheDevoirQO.getTypeReglesDevoirMsg() != null)) {
            final ConteneurMessage conteneurMessage = new ConteneurMessage();
            conteneurMessage.add(new Message(rechercheDevoirQO.getTypeReglesDevoirMsg()
                                                              .name(), Nature.INFORMATIF));
            resultat.setConteneurMessage(conteneurMessage);
        }
        resultat.setValeurDTO(ComparateurUtils.sort(listeResultatRechercheDevoirDTO, "dateRemiseDevoir"));
        return resultat;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<FileUploadDTO> findPieceJointeDevoir(RecherchePieceJointeDevoirQO recherchePJDevoir) {
        Assert.isNotNull("recherchePJDevoir", recherchePJDevoir);
        Assert.isNotNull("idDevoir", recherchePJDevoir.getIdDevoir());
        
        final Integer idDevoir = recherchePJDevoir.getIdDevoir();
        final List<FileUploadDTO> listePieceJointeDTO = new ArrayList<FileUploadDTO>();
        
        // En mode Archive on fait une requete native
        if (BooleanUtils.isTrue(recherchePJDevoir.getArchive())) {
            Assert.isNotNull("exercice", recherchePJDevoir.getExercice());
            final String exercice = recherchePJDevoir.getExercice();
            final String schema = Archive.PREFIX_SCHEMA_ARCHIVE + exercice;
            final String requete = 
                "SELECT " +
                "   PJ.id as id, " +
                "   PJ.nom_fichier as nomFichier, " +
                "   PJ.uid as uid, " +
                "   PJ.chemin as chemin " +
                "FROM " +
                SchemaUtils.getTableAvecSchema(schema,"cahier_piece_jointe") + " PJ, " +
                SchemaUtils.getTableAvecSchema(schema,"cahier_piece_jointe_devoir") + " PJD " +
                "WHERE " + 
                "   PJ.id = PJD.id_piece_jointe AND " + 
                "   PJD.id_devoir = ?";
            final Query query = getEntityManager().createNativeQuery(requete);
            query.setParameter(1, idDevoir);
            final List<Object[]> resultatQuery = new ArrayList<Object[]>();
            resultatQuery.addAll(query.getResultList());
            
            for (final Object[] result : resultatQuery) {
                final FileUploadDTO pieceJointeDTO = new FileUploadDTO();
                pieceJointeDTO.setEnBase(true);
                pieceJointeDTO.setId((Integer) result[0]); 
                pieceJointeDTO.setNom((String) result[1]);
                pieceJointeDTO.setUid((String) result[2]);
                pieceJointeDTO.setPathDB((String) result[3]);
                
                //FIXME Dénormalisation Interdit dans le métier attention si séparation des couches
                FacesUtils.pathUploadFile(pieceJointeDTO);
                if (FileUploadUtils.checkExistencePieceJointe(pieceJointeDTO)) {
                    pieceJointeDTO.setActiverLien(true);
                }
                listePieceJointeDTO.add(pieceJointeDTO);
            }
            
        // Sinon en mode cahier courant on fait une requete Bean normale
        } else {
            
            
            final String query =
                "SELECT PJ " + " FROM " + PiecesJointesDevoirsBean.class.getName() + " PJD " +
                " INNER JOIN PJD.pieceJointe PJ " + " WHERE PJD.pk.idDevoir = :idDevoir " +
                " ORDER BY PJ.code ASC";
    
            final List<PieceJointeBean> listePieceJointeBean =
                getEntityManager().createQuery(query).setParameter("idDevoir", idDevoir)
                    .getResultList();

            for (PieceJointeBean pieceJointeBean : listePieceJointeBean) {
                final FileUploadDTO pieceJointeDTO = new FileUploadDTO();
                pieceJointeDTO.setEnBase(true);
                pieceJointeDTO.setId(pieceJointeBean.getId());
                pieceJointeDTO.setNom(pieceJointeBean.getNomFichier());
                pieceJointeDTO.setUid(pieceJointeBean.getUid());
                pieceJointeDTO.setPathDB(pieceJointeBean.getChemin());
                
                //FIXME Dénormalisation Interdit dans le métier attention si séparation des couches
                FacesUtils.pathUploadFile(pieceJointeDTO);
                if (FileUploadUtils.checkExistencePieceJointe(pieceJointeDTO)) {
                    pieceJointeDTO.setActiverLien(true);
                }
                
                listePieceJointeDTO.add(pieceJointeDTO);
            }
        }
        
        return listePieceJointeDTO;
    }
    
    /**
     * Recherche les eventuels classe ou groupe liés à celui/celle dont l'id est passé en entée.
     * @param classeOuGroupe classeOuGroupe
     * @param idClasseGroupe idClasseGroupe
     * @return une liste de id
     */
    @SuppressWarnings("unchecked")
    private Set<Integer> findAutresGroupeOuClasse(final TypeGroupe classeOuGroupe, final Integer idClasseGroupe) {
        Assert.isNotNull("classeOuGroupe", classeOuGroupe);
        Assert.isNotNull("idClasseGroupe", idClasseGroupe);
        String requete; 
        
        if (classeOuGroupe.equals(TypeGroupe.CLASSE)) { 
            requete = 
                "select new Map(" + 
                "  EG.pk.idGroupe as id," +
                "  G.designation as designation) " +
                "from " +
                   GroupesClassesBean.class.getName() + " EG " +
                "  INNER JOIN EG.groupe G " +
                "where " + 
                "  EG.pk.idClasse = :idClasseGroupe";
        } else {
            requete = 
                "select new Map(" + 
                "  EG.pk.idClasse as id," +
                "  C.designation as designation) " +
                "from " +
                   GroupesClassesBean.class.getName() + " EG " +
                "  INNER JOIN EG.classe C " +
                "where " + 
                "  EG.pk.idGroupe = :idClasseGroupe";
        }
        final Query query = getEntityManager().createQuery(requete);
        query.setParameter("idClasseGroupe", idClasseGroupe);
        
        final List<Map<String, ?>> resultatQuery = new ArrayList<Map<String, ?>>();
        resultatQuery.addAll(query.getResultList());   
        
        final Set<Integer> liste = new HashSet<Integer>();
        for (Map<String, ?> result : resultatQuery) {
            final Integer idElement = (Integer) result.get("id");
            liste.add(idElement);
        }
        return liste;
    }

    /**
     * Cherche la liste des autres devoirs que la même classe ou groupe doit retrouner le me même jour. 
     * @param idEtablissement idEtablissemen 
     * @param dateDevoir  dateDevoir
     * @param idDevoir  idDevoir
     * @param classeOuGroupe classeOuGroupe
     * @param idsClasseGroupe liste des id des des idClasseGroupe 
     * @param uidEnseignant l'uid de l'enseignant.
     * @return la liste des devoirs trouvés
     */
    @SuppressWarnings("unchecked")
    private List<DevoirEnteteDTO> findChargeTravailClasseOuGroupe(final Integer idEtablissement, final Date dateDevoir, 
            final Integer idDevoir, final TypeGroupe classeOuGroupe,final Set<Integer> idsClasseGroupe,
            final String uidEnseignant) {
        Assert.isNotNull("DateDevoir", dateDevoir);
        Assert.isNotNull("IdEtablissement", idEtablissement);
        Assert.isNotNull("classeOuGroupe", classeOuGroupe);
        Assert.isNotNull("idsClasseGroupe", idsClasseGroupe);        
        Assert.isNotEmpty("idsClasseGroupe", idsClasseGroupe);
        
        // Recherche les devoirs affectés à la même entite (Groupe ou Classe) 
        // A rendre pour le meme jour
        String requete = 
            " select new map(" +
            "   TD.categorie as categorie," +
            "   D.dateRemise as dateRemise, " +
            "   D.intitule as intitule, " +
            "   E.designation as designation, " +
            "   E.id as idEnseignement," +
            "   PROF.nom as nomEnseignant," +
            "   PROF.uid as uidEnseignant," +
            "   SEA.date as dateSeance," +
            "   CG.designation as designationClasseGroupe) " +
            " from "+  
            DevoirBean.class.getName() + " D LEFT JOIN D.typeDevoir TD, " +
            SeanceBean.class.getName() + " SEA INNER JOIN SEA.enseignant PROF, "; 
        if (TypeGroupe.CLASSE.equals(classeOuGroupe)) {
            requete += ClasseBean.class.getName() + " CG, ";
        } else {
            requete += GroupeBean.class.getName() + " CG, ";
        }
        requete += 
            SequenceBean.class.getName() + " SEQ "+
            " INNER JOIN SEQ.enseignement E "+
            " where  "+
            "    D.idSeance = SEA.id AND "+
            "    SEQ.id = SEA.idSequence AND ";
        
        if (TypeGroupe.CLASSE.equals(classeOuGroupe)) {
            requete +=" SEQ.idClasse = CG.id AND  ";
        } else {
            requete +=" SEQ.idGroupe = CG.id AND  ";
        }
        requete += 
            "    CG.id in (:idsClasseGroupe) AND " +
            "    CG.idEtablissement = :idEtablissement AND " +
            "    D.dateRemise = :dateRemise "; 
        if (idDevoir != null) {
            requete += " AND D.id != :idDevoir ";
        }
        final Query query = getEntityManager().createQuery(requete);
        query.setParameter("idEtablissement", idEtablissement);
        query.setParameter("idsClasseGroupe", idsClasseGroupe);
        query.setParameter("dateRemise", dateDevoir);
        if (idDevoir != null) {
            query.setParameter("idDevoir", idDevoir);
        }
        
        final List<Map<String, ?>> resultatQuery = new ArrayList<Map<String, ?>>();
        resultatQuery.addAll(query.getResultList());
        
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
                    "idEnseignement", "designation");
        } else {          
            mapLibellePersoEnseignement = new HashMap<Integer, String>();         
        }        
        final List<DevoirEnteteDTO> listeChargeTravail = new ArrayList<DevoirEnteteDTO>();
        final Date dateDuJour = DateUtils.getAujourdhui();
        for (Map<String, ?> result : resultatQuery) {
            final DevoirEnteteDTO devoir = new DevoirEnteteDTO();
            devoir.setIntituleDevoir((String) result.get("intitule"));
            final Integer idEnseignement = (Integer) result.get("idEnseignement");
            if (mapLibellePersoEnseignement.containsKey(idEnseignement)) {
                devoir.setLibelleEnseignement((String) mapLibellePersoEnseignement.get(idEnseignement));
            } else {
                final String libDesignation = (String) result.get("designation");
                devoir.setLibelleEnseignement(libDesignation.substring(0,Math.min(libDesignation.length(),8)));
            }            
            devoir.setNomEnseignant(org.crlr.utils.StringUtils.formatageSeulementPremiereLettreMajuscule((String) result.get("nomEnseignant")));
            final String uid = (String) result.get("uidEnseignant");
            final Date dateSeance = (Date) result.get("dateSeance");
            devoir.setDateSeance(dateSeance);
            if (uid.equals(uidEnseignant) || (dateDuJour.after(dateSeance))) {
                devoir.setSeanceFuture(false);
            } else {
                devoir.setSeanceFuture(true);
            }
            devoir.setDesignationClasseGroupe((String) result.get("designationClasseGroupe"));
            final String categorie = ((String) result.get("categorie"));
            devoir.setVraiOuFauxDevoir(TypeCategorieTypeDevoir.DEVOIR.getId().equals(categorie));
            listeChargeTravail.add(devoir);
        }
        return listeChargeTravail;
    }
        
    
    
    /**
     * {@inheritDoc}
     */
    public ChargeTravailDTO findChargeTravail(final ChargeDevoirQO chargeDevoir) {
        Assert.isNotNull("ChargeDevoirQO", chargeDevoir);
        Assert.isNotNull("DateDevoir", chargeDevoir.getDateDevoir());
        Assert.isNotNull("IdEtablissement", chargeDevoir.getIdEtablissement());
        
        // Recherche les devoirs a rendre le meme jour pour la classe ou le groupe dont le id est passe en entree
        final TypeGroupe classeOuGroupe;
        final TypeGroupe classeOuGroupeComplement;
        final Set<Integer> idsClasseGroupe = new HashSet<Integer>();
        if (chargeDevoir.getIdClasse() != null) {
            classeOuGroupe = TypeGroupe.CLASSE;
            idsClasseGroupe.add(chargeDevoir.getIdClasse());
            classeOuGroupeComplement = TypeGroupe.GROUPE;
        } else  {
            Assert.isNotNull("idGroupe or IdClasse", chargeDevoir.getIdGroupe());
            classeOuGroupe = TypeGroupe.GROUPE;
            idsClasseGroupe.add(chargeDevoir.getIdGroupe());
            classeOuGroupeComplement = TypeGroupe.CLASSE;
        }
        final List<DevoirEnteteDTO> listeChargeTravailPrincipale = findChargeTravailClasseOuGroupe(
                chargeDevoir.getIdEtablissement(), 
                chargeDevoir.getDateDevoir(), 
                chargeDevoir.getIdDevoir(), 
                classeOuGroupe, 
                idsClasseGroupe,
                chargeDevoir.getUidEnseignant()
                );
        
        final ChargeTravailDTO chargeTravail = new ChargeTravailDTO();
        chargeTravail.setGroupeOuClasse(classeOuGroupe);
        chargeTravail.setListeDevoirPrincipal(listeChargeTravailPrincipale);
        
        // Recherche les autres classes ou groupes liés à l'id passe en entrée
        Set<Integer> listeIdAutres = null;
        for (final Integer idClasseGroupe : idsClasseGroupe) {
            listeIdAutres = findAutresGroupeOuClasse(classeOuGroupe, idClasseGroupe);
        }
        
        // Recherche les devoirs a rendre le meme jour pour les autres groupement (groupe ou classe)
        // Associés à l'id passe en entrée.
        final List<DevoirEnteteDTO> listeChargeTravailComplement;
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(listeIdAutres)) {
            listeChargeTravailComplement = findChargeTravailClasseOuGroupe(
                    chargeDevoir.getIdEtablissement(), 
                    chargeDevoir.getDateDevoir(), 
                    chargeDevoir.getIdDevoir(), 
                    classeOuGroupeComplement, 
                    listeIdAutres,
                    chargeDevoir.getUidEnseignant()        
                    );
            chargeTravail.setListeDevoirComplement(listeChargeTravailComplement);
        } else {
            listeChargeTravailComplement = null; 
        }
        chargeTravail.setAffichageCharge(!org.apache.commons.collections.CollectionUtils.isEmpty(listeChargeTravailPrincipale) 
                || !org.apache.commons.collections.CollectionUtils.isEmpty(listeChargeTravailComplement));
        
        return chargeTravail;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<DevoirDTO>> findDevoirForPlanning(RechercheSeanceQO rechercheSeanceQO)
        throws MetierException {
        
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        
        final ResultatDTO<List<DevoirDTO>> resultat =
            new ResultatDTO<List<DevoirDTO>>();

        final List<DevoirDTO> listeResultatRechercheDevoirDTO =
            new ArrayList<DevoirDTO>();

        final Integer idEnseignant = rechercheSeanceQO.getIdEnseignant();
        final Integer idEtablissement = rechercheSeanceQO.getIdEtablissement();
        final Date dateDebut = rechercheSeanceQO.getDateDebut();
        final Date dateFin = rechercheSeanceQO.getDateFin();

        Assert.isNotNull("dateDebut", dateDebut);
        Assert.isNotNull("dateFin", dateFin);
        Assert.isNotNull("idEtablissement", idEtablissement);

        final List<Map<String, ?>> resultatQuery;
        if (idEnseignant != null){
            resultatQuery = findDevoirForPlanningEnseignant(idEnseignant,
                    idEtablissement, dateDebut, dateFin);
        } else {
            Assert.isNotNull("classes", rechercheSeanceQO.getListeClasseBean());
            Assert.isNotNull("groupes", rechercheSeanceQO.getListeGroupeBean());
            resultatQuery = findDevoirForPlanningEleve(idEtablissement, 
                    rechercheSeanceQO.getListeClasseBean(), rechercheSeanceQO.getListeGroupeBean(),
                    dateDebut, dateFin);
            
        }

        //Recherche les libelles court des enseignements
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

        for (Map<String, ?> result : resultatQuery) {
            
            final DevoirDTO devoirDTO =
                new DevoirDTO();
            final Integer idDevoir = (Integer) result.get("idDevoir");
            devoirDTO.setId(idDevoir);
            devoirDTO.setIdSeance((Integer) result.get("idSeance"));
            devoirDTO.getSeance().setIdEnseignant((Integer) result.get("idEnseignant"));
            devoirDTO.setDateSeance((Date) result.get("dateSeance"));
            devoirDTO.setIdClasse((Integer) result.get("idClasse"));
            devoirDTO.setIdGroupe((Integer) result.get("idGroupe"));
            devoirDTO.setDesignationGroupe((String) result.get("designationGroupe"));
            devoirDTO.setDesignationClasse((String) result.get("designationClasse"));
            final Integer idEnseignement = (Integer) result.get("idEnseignement");
            if (mapLibellePersoEnseignement.containsKey(idEnseignement)) {
                devoirDTO.setMatiere((String) mapLibellePersoEnseignement.get(idEnseignement));
            } else {
                devoirDTO.setMatiere((String) result.get("designationEnseignement"));
            }
            
            devoirDTO.getTypeDevoirDTO().setId((Integer) result.get("idTypeDevoir"));
            devoirDTO.getTypeDevoirDTO().setLibelle((String) result.get("libelleTypeDevoir"));
            //N'écrase pas les défaut présents sur type devoir
            if (result.get("categorieTypeDevoir") != null) {
                devoirDTO.getTypeDevoirDTO().setCategorie(TypeCategorieTypeDevoir.valueOf((String) result.get("categorieTypeDevoir")));
            }
            devoirDTO.setDateRemise((Date) result.get("dateRemise"));
            devoirDTO.setIntitule((String) result.get("intituleDevoir"));
            devoirDTO.setDescription((String) result.get("descriptionDevoir"));
            
            devoirDTO.setCiviliteEnseignant((String) result.get("civilite"));
            devoirDTO.setNomEnseignant((String) result.get("nom"));
            
            listeResultatRechercheDevoirDTO.add(devoirDTO);
        }
        
        if (listeResultatRechercheDevoirDTO.size() == 0) {
            conteneurMessage.add(new Message(TypeReglesDevoir.DEVOIR_08.name(),
                    Nature.INFORMATIF));
            throw new MetierException(conteneurMessage,
                    "Il n'existe aucun devoir pour votre recherche.");
        }
        
        resultat.setValeurDTO(listeResultatRechercheDevoirDTO);
        return resultat;
    }

    final static String DEVOIR_PLANNING_REQUETE_SELECT_CHAMPS =
            "DEV.id as idDevoir, " +
                    "DEV.code as codeDevoir, DEV.intitule as intituleDevoir, " +
                    "DEV.dateRemise as dateRemise, " + "DEV.description as descriptionDevoir, " +
                    "DEV.idTypeDevoir as idTypeDevoir, "+
                    "TD.libelle as libelleTypeDevoir, " +
                    "TD.categorie as categorieTypeDevoir, " +
                    "GRP.code as codeGroupe, " + "GRP.id as idGroupe, " +
                    "GRP.designation as designationGroupe, " + "CLA.code as codeClasse, " + "CLA.id as idClasse, " +
                    "CLA.designation as designationClasse, " + "ENS.id as idEnseignement, " +
                    "ENS.designation as designationEnseignement" +
                    ", SEA.date as dateSeance, SEA.id as idSeance, SEA.idEnseignant as idEnseignant, SEA.idSequence as idSequence" 
                    ;
            
    /**
     * Recherche les devoirs pour le planning de l'enseignant.
     * @param idEnseignant l'id de l'enseignang.
     * @param idEtablissement l'id de l'établissement.
     * @param dateDebut la date de debut de recherche.
     * @param dateFin la date de fin de recherche. 
     * @return la liste des devoirs.
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, ?>> findDevoirForPlanningEnseignant(
            final Integer idEnseignant, final Integer idEtablissement,
            final Date dateDebut, final Date dateFin) {
        final String requete =
            "SELECT " + " new map(" + DEVOIR_PLANNING_REQUETE_SELECT_CHAMPS +
            " )" + " FROM " +
            SequenceBean.class.getName() + " SEQ " +
            " INNER JOIN SEQ.enseignement ENS " + 
            " LEFT JOIN SEQ.classe CLA " + 
            " LEFT JOIN SEQ.groupe GRP, " +
            DevoirBean.class.getName() + " DEV INNER JOIN DEV.seance as SEA " +
            " LEFT JOIN DEV.typeDevoir as TD " +
            " WHERE " +
            " SEA.idSequence = SEQ.id AND " + " SEQ.enseignant.id = :idEnseignant AND " +
            " (CLA.idEtablissement = :idEtab  OR GRP.idEtablissement = :idEtab) AND " +
            " DEV.dateRemise >= :dateDebut AND DEV.dateRemise <= :dateFin " +
            " ORDER BY DEV.dateRemise ASC";
      
                final Query query = getEntityManager().createQuery(requete);
        query.setParameter("idEnseignant", idEnseignant);
        query.setParameter("idEtab", idEtablissement);
        query.setParameter("dateDebut",
                                dateDebut);
        query.setParameter("dateFin",
                                dateFin);
        return query.getResultList();
    }
    
    /**
     * Recherche les devoirs pour l'élève.
     * @param idEtablissement l'id de l'établissement.
     * @param classes la liste des classes de l'élève.
     * @param groupes la liste des groupes de l'élève.
     * @param dateDebut la date de debut de la recherche.
     * @param dateFin la date de fin de la recherche.
     * @return  la liste des devoirs visible à l'élève pour le mois séléctionné.
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, ?>> findDevoirForPlanningEleve(
            final Integer idEtablissement,
            final List<ClasseBean> classes, final List<GroupeBean> groupes, 
            final Date dateDebut, final Date dateFin) {
        String requete =
            "SELECT " + " new map(" + DEVOIR_PLANNING_REQUETE_SELECT_CHAMPS + ","+
            "PROF.civilite as civilite, PROF.nom as nom )" + " FROM " +
            SequenceBean.class.getName() + " SEQ " +
            " INNER JOIN SEQ.enseignement ENS " + 
            " INNER JOIN SEQ.enseignant PROF " +
            " LEFT JOIN SEQ.classe CLA " + 
            " LEFT JOIN SEQ.groupe GRP, " +
            DevoirBean.class.getName() + " DEV INNER JOIN DEV.seance as SEA " +
            " LEFT JOIN DEV.typeDevoir as TD " +
            " WHERE " +
            " SEA.idSequence = SEQ.id AND" ; 
        
        if (! org.apache.commons.collections.CollectionUtils.isEmpty(classes)){
            if (! org.apache.commons.collections.CollectionUtils.isEmpty(groupes)){
                //Des classes et des groupes 
                requete += 
                    " ( ( CLA.idEtablissement = :idEtab AND CLA in (:classes)) OR ( GRP.idEtablissement = :idEtab AND GRP in (:groupes))) AND";
            } else {
                //Des classes uniquement
                requete += " CLA.idEtablissement = :idEtab AND CLA in (:classes) AND";
            }
        } else if (! org.apache.commons.collections.CollectionUtils.isEmpty(groupes)){
            //Des groupes uniquement 
            requete += " GRP.idEtablissement = :idEtab AND GRP in (:groupes) AND ";
        } else {
            //Impossible en theorie
            throw new RuntimeException("Pas de classe ou de groupe pour l'élève");
        }

        requete += " DEV.dateRemise >= :dateDebut AND DEV.dateRemise <= :dateFin AND SEA.date <= :today" +
        " ORDER BY DEV.dateRemise ASC";

        final Query query = getEntityManager().createQuery(requete);

        if (! org.apache.commons.collections.CollectionUtils.isEmpty(classes)){
            query.setParameter("classes", classes);
        }
        if (! org.apache.commons.collections.CollectionUtils.isEmpty(groupes)){
            query.setParameter("groupes", groupes);
        }
        query.setParameter("idEtab", idEtablissement);
        query.setParameter("dateDebut",
                                dateDebut);
        query.setParameter("dateFin",
                                dateFin);
        query.setParameter("today",
                DateUtils.getAujourdhui());
        return query.getResultList();
    }
}
