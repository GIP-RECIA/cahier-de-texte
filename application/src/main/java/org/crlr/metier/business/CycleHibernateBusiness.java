/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.TypeCategorieTypeDevoir;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.cycle.CycleDTO;
import org.crlr.dto.application.cycle.CycleDevoirDTO;
import org.crlr.dto.application.cycle.CyclePlageEmploiDTO;
import org.crlr.dto.application.cycle.CycleSeanceDTO;
import org.crlr.dto.application.cycle.RechercheCycleEmploiQO;
import org.crlr.dto.application.cycle.RechercheCycleQO;
import org.crlr.dto.application.cycle.TypeDateRemise;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.entity.CycleBean;
import org.crlr.metier.entity.CycleDevoirBean;
import org.crlr.metier.entity.CycleGroupeBean;
import org.crlr.metier.entity.CycleSeanceBean;
import org.crlr.metier.entity.EmploiBean;
import org.crlr.metier.entity.EnseignantsGroupesBean;
import org.crlr.metier.entity.GroupeBean;
import org.crlr.metier.entity.PieceJointeBean;
import org.crlr.metier.entity.PiecesJointesCycleDevoirsBean;
import org.crlr.metier.entity.PiecesJointesCycleSeancesBean;
import org.crlr.metier.entity.TypeDevoirBean;
import org.crlr.utils.Assert;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.dto.TypeSemaine;
import org.crlr.web.utils.FacesUtils;
import org.crlr.web.utils.FileUploadUtils;
import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;

/**
 * cycleHibernateBusiness.
 * 
 * @author vibertd
 * @version $Revision: 1.56 $
 */
@Repository
public class CycleHibernateBusiness extends AbstractBusiness implements
        CycleHibernateBusinessService {

    /**
     * {@inheritDoc}
     */
    public Boolean saveCycle(final CycleDTO cycle) {
        Assert.isNotNull("cycleDTO", cycle);
        Integer idCycle = cycle.getId();
        final Integer idEnseignant = cycle.getEnseignantDTO().getId();
        final String uidEnseignant = cycle.getEnseignantDTO().getUid();
        final String titre = cycle.getTitre();
        final String objectif = cycle.getObjectif();
        final String prerequis = cycle.getPrerequis();
        final String description = cycle.getDescription();
        final List<GroupeDTO> listeGroupe = cycle.getListeGroupe();
        final Boolean modeAjout = idCycle == null;

        CycleBean cycleBean = null;
        if (!modeAjout) {
            cycleBean = getEntityManager().find(CycleBean.class, idCycle);
        } else {
            cycleBean = new CycleBean();
        }
        cycleBean.setDescription(description);
        cycleBean.setIdEnseignant(idEnseignant);
        cycleBean.setObjectif(objectif);
        cycleBean.setPrerequis(prerequis);
        cycleBean.setTitre(titre);
        cycleBean.setUidEnseignant(uidEnseignant);

        // Si l'id est null c'est qu'on est en ajout.
        if (modeAjout) {
            final BaseHibernateBusiness baseHibernateBusiness = new BaseHibernateBusiness(
                    this.getEntityManager());
            idCycle = baseHibernateBusiness.getIdInsertion("cahier_cycle");

            cycleBean.setId(idCycle);

            // Ajout à la base de données
            getEntityManager().persist(cycleBean);

            // Met a jour dans l'objet le id du cycle
            cycle.setId(idCycle);
        }

        // Supprime les anciens liens
        if (!modeAjout) {
            final String query = "DELETE FROM "
                    + CycleGroupeBean.class.getName()
                    + " CG WHERE CG.pk.idCycle = :id";
            getEntityManager().createQuery(query).setParameter("id",
                    idCycle).executeUpdate();
        }
        
        // Met a jour les liens avec les groupes
        for (final GroupeDTO groupe : listeGroupe) {
            final Integer idGroupe = groupe.getId();

            // Ajoute les liens
            final CycleGroupeBean cycleGroupeBean = new CycleGroupeBean();
            cycleGroupeBean.setIdCycle(idCycle);
            cycleGroupeBean.setIdGroupe(idGroupe);
            getEntityManager().persist(cycleGroupeBean);
        }
        getEntityManager().flush();

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public Integer saveCycleSeance(CycleSeanceDTO cycleSeanceDTO) {
        Assert.isNotNull("cycleSeanceDTO", cycleSeanceDTO);
        Assert.isNotNull("cycleDTO", cycleSeanceDTO.getIdCycle());

        Integer idCycleSeance = cycleSeanceDTO.getId();
        final Integer idCycle = cycleSeanceDTO.getIdCycle();
        final Integer idEnseignant = cycleSeanceDTO.getEnseignantDTO().getId();
        final String uidEnseignant = cycleSeanceDTO.getEnseignantDTO().getUid();
        final String intitule = cycleSeanceDTO.getIntitule();
        final String objectif = cycleSeanceDTO.getObjectif();
        final String description = cycleSeanceDTO.getDescription();
        final String annotations = cycleSeanceDTO.getAnnotations();
        final Boolean annotationsVisible = cycleSeanceDTO.getAnnotationsVisible();
        final String enseignement = cycleSeanceDTO.getEnseignement();
        final Integer indice = cycleSeanceDTO.getIndice();

        final Boolean modeAjout = idCycleSeance == null;

        CycleSeanceBean cycleSeanceBean = null;
        if (!modeAjout) {
            cycleSeanceBean = getEntityManager().find(CycleSeanceBean.class,
                    idCycleSeance);
        } else {
            cycleSeanceBean = new CycleSeanceBean();
        }
        cycleSeanceBean.setAnnotations(annotations);
        cycleSeanceBean.setAnnotationsVisible(annotationsVisible);
        cycleSeanceBean.setDescription(description);
        cycleSeanceBean.setEnseignement(enseignement);
        cycleSeanceBean.setIdCycle(idCycle);
        cycleSeanceBean.setIdEnseignant(idEnseignant);
        cycleSeanceBean.setIntitule(intitule);
        cycleSeanceBean.setObjectif(objectif);
        cycleSeanceBean.setUidEnseignant(uidEnseignant);
        cycleSeanceBean.setIndice(indice);

        // Si l'id est null c'est qu'on est en ajout.
        if (modeAjout) {
            final BaseHibernateBusiness baseHibernateBusiness = new BaseHibernateBusiness(
                    this.getEntityManager());
            idCycleSeance = baseHibernateBusiness
                    .getIdInsertion("cahier_cycle_seance");

            cycleSeanceBean.setId(idCycleSeance);

            // Ajout à la base de données
            getEntityManager().persist(cycleSeanceBean);
        }
        getEntityManager().flush();

        return idCycleSeance;
    }

    /**
     * {@inheritDoc}
     */
    public Integer saveCycleOrdreSeance(CycleDTO cycleDTO) throws MetierException {
        Assert.isNotNull("cycleSeanceDTO", cycleDTO);
        Assert.isNotNull("cycleSeanceDTO.id", cycleDTO.getId());
        
        Integer i = 0;
        Integer count = 0;
        for (final CycleSeanceDTO seanceDTO : cycleDTO.getListeSeance()) {
            i++;
            final CycleSeanceBean cycleSeanceBean = getEntityManager().find(CycleSeanceBean.class, seanceDTO.getId());
            if (cycleSeanceBean != null) {
                if (seanceDTO.getIndice() != null) {
                    if (!seanceDTO.getIndice().equals(cycleSeanceBean.getIndice())) {
                        cycleSeanceBean.setIndice(seanceDTO.getIndice());
                        count++;
                    }
                } else {
                    if (!i.equals(cycleSeanceBean.getIndice())) {
                        cycleSeanceBean.setIndice(i);
                        count++;
                    }
                }
            }
        }
        getEntityManager().flush();
        return count;
    }
    
    /**
     * {@inheritDoc}
     */
    public Integer saveCycleDevoir(CycleDevoirDTO cycleDevoirDTO, Integer idCycleSeance) {
        Assert.isNotNull("cycleDevoirDTO", cycleDevoirDTO);
        Assert.isNotNull("idCycleSeance", idCycleSeance);
        Assert.isNotNull("dateRemise", cycleDevoirDTO.getDateRemise());
        Assert.isNotNull("typeDevoir", cycleDevoirDTO.getTypeDevoirDTO());

        Integer idCycleDevoir = cycleDevoirDTO.getId();
        final String intitule = cycleDevoirDTO.getIntitule();
        final String description = cycleDevoirDTO.getDescription();
        final Integer dateRemise = cycleDevoirDTO.getDateRemise().getId();
        final Integer idTypeDevoir = cycleDevoirDTO.getTypeDevoirDTO().getId();

        final Boolean modeAjout = idCycleDevoir == null;

        CycleDevoirBean cycleDevoirBean = null;
        if (!modeAjout) {
            cycleDevoirBean = getEntityManager().find(CycleDevoirBean.class,
                    idCycleDevoir);
        } else {
            cycleDevoirBean = new CycleDevoirBean();
        }
        cycleDevoirBean.setDateRemise(dateRemise);
        cycleDevoirBean.setDescription(description);
        cycleDevoirBean.setIdCycleSeance(idCycleSeance);
        cycleDevoirBean.setIdTypeDevoir(idTypeDevoir);
        cycleDevoirBean.setIntitule(intitule);

        // Si l'id est null c'est qu'on est en ajout.
        if (modeAjout) {
            final BaseHibernateBusiness baseHibernateBusiness = new BaseHibernateBusiness(this.getEntityManager());
            idCycleDevoir = baseHibernateBusiness.getIdInsertion("cahier_cycle_devoir");
            cycleDevoirBean.setId(idCycleDevoir);
        
            // Ajout à la base de données
            getEntityManager().persist(cycleDevoirBean);
        }
        getEntityManager().flush();

        return idCycleDevoir;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<CycleDTO> findListeCycle(RechercheCycleQO rechercheCycleQO) {
        Assert.isNotNull("cycleDTO", rechercheCycleQO);
        final List<CycleDTO> listeCycle = new ArrayList<CycleDTO>();

        String requete;
        String where = null;

        // Les criteres
        String enseignement = rechercheCycleQO.getEnseignement();
        String objectif = rechercheCycleQO.getObjectif();
        String titre = rechercheCycleQO.getTitre();
        if (!Strings.isNullOrEmpty(enseignement)) {
            enseignement = "%" + enseignement + "%";
        }
        if (!Strings.isNullOrEmpty(objectif)) {
            objectif = "%" + objectif + "%";
        }
        if (!Strings.isNullOrEmpty(titre)) {
            titre = "%" + titre + "%";
        }
        
        // Avec des criteres sur les seances attachees
        if (!Strings.isNullOrEmpty(enseignement)) {
            requete = "select distinct C as cycle from "
                    + CycleSeanceBean.class.getName() + " CS "
                    + "INNER JOIN CS.cycle C ";

            if (where == null) {
                where = "where ";
            } else {
                where += "and ";
            }
            where += "upper(CS.enseignement) like upper(:enseignement) ";

            // Uniquement des critere sur le cycle
        } else {
            requete = "select C from " + CycleBean.class.getName() + " C ";
        }
        
        // Ajoute les criteres sur Cycle
        if (!Strings.isNullOrEmpty(objectif)) {
            if (where == null) {
                where = "where ";
            } else {
                where += "and ";
            }
            where += "upper(C.objectif) like upper(:objectif) ";
        }
        if (!Strings.isNullOrEmpty(titre)) {
            if (where == null) {
                where = "where ";
            } else {
                where += "and ";
            }
            where += "upper(C.titre) like upper(:titre) ";
        }

        // Confidentialite
        if (rechercheCycleQO.getIdEnseignant() != null) {
            if (where == null) {
                where = "where ";
            } else {
                where += "and ";
            }
            where += "(C.idEnseignant = :idEnseignant OR (exists(SELECT 1 from " + 
                     CycleSeanceBean.class.getName() + 
                     " CS2 where CS2.idCycle = C.id and CS2.idEnseignant = :idEnseignant)) " +
                     "OR (exists (select 1 from " + CycleGroupeBean.class.getName() + 
                     " CG2 inner join CG2.groupe G2, " + EnseignantsGroupesBean.class.getName() + " EG2 " +
                     " where CG2.pk.idCycle = C.id and G2.id = EG2.pk.idGroupe and EG2.pk.idEnseignant = :idEnseignant ) ) )";
        }
        
        // Requete totale
        if (where != null) {
            requete += where;
        }
        final Query query = getEntityManager().createQuery(requete);

        if (!Strings.isNullOrEmpty(enseignement)) {
            query.setParameter("enseignement", enseignement);
        }
        if (!Strings.isNullOrEmpty(objectif)) {
            query.setParameter("objectif", objectif);
        }
        if (!Strings.isNullOrEmpty(titre)) {
            query.setParameter("titre", titre);
        }
        if (rechercheCycleQO.getIdEnseignant() != null) {
            query.setParameter("idEnseignant", rechercheCycleQO.getIdEnseignant());
        }
        final List<CycleBean> resultatQuery = query.getResultList();

        for (final CycleBean cycleBean : resultatQuery) {
            final CycleDTO cycleDTO = new CycleDTO();
            ObjectUtils.copyProperties(cycleDTO, cycleBean);
            final EnseignantDTO enseignantDTO = new EnseignantDTO();
            enseignantDTO.setId(cycleBean.getIdEnseignant());
            enseignantDTO.setUid(cycleBean.getUidEnseignant());
            cycleDTO.setEnseignantDTO(enseignantDTO);

            // charge la list des groupes associes
            final List<GroupeDTO> listeGroupe = findListeGroupeByCycle(cycleBean
                    .getId());
            cycleDTO.setListeGroupe(listeGroupe);
            listeCycle.add(cycleDTO);
        }
        return listeCycle;

    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<CycleSeanceDTO> findListeCycleSeance(CycleDTO cycleDTO) {
        Assert.isNotNull("cycleDTO", cycleDTO);
        Assert.isNotNull("cycleDTO.id", cycleDTO.getId());

        final List<CycleSeanceDTO> listeCycleSeance = new ArrayList<CycleSeanceDTO>();

        String requete;

        // Les criteres
        final Integer idCycle = cycleDTO.getId();

        requete = "select distinct CS from " + CycleSeanceBean.class.getName() +
                  " CS " + "where CS.idCycle = :idCycle " +
                  " order by CS.indice, CS.idCycle ";

        final Query query = getEntityManager().createQuery(requete);
        query.setParameter("idCycle", idCycle);
        final List<CycleSeanceBean> resultatQuery = query.getResultList();

        for (final CycleSeanceBean cycleSeanceBean : resultatQuery) {
            final CycleSeanceDTO cycleSeanceDTO = new CycleSeanceDTO();
            ObjectUtils.copyProperties(cycleSeanceDTO, cycleSeanceBean);

            final EnseignantDTO enseignantDTO = new EnseignantDTO();
            enseignantDTO.setId(cycleSeanceBean.getIdEnseignant());
            enseignantDTO.setUid(cycleSeanceBean.getUidEnseignant());
            cycleSeanceDTO.setEnseignantDTO(enseignantDTO);

            listeCycleSeance.add(cycleSeanceDTO);
        }
        return listeCycleSeance;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<CycleDevoirDTO> findListeDevoirSeance(
            final Integer idCycleSeance) {
        Assert.isNotNull("idCycleSeance", idCycleSeance);
        final List<CycleDevoirDTO> listeDevoir = new ArrayList<CycleDevoirDTO>();

        String requete = "select new map(D as devoir, TD as typeDevoir) "
                + "from " + CycleDevoirBean.class.getName() + " D "
                + "left join D.typeDevoir TD "
                + "where D.idCycleSeance = :idCycleSeance ";

        // Ajoute les criteres
        final Query query = getEntityManager().createQuery(requete);
        query.setParameter("idCycleSeance", idCycleSeance);
        final List<Map<String, ?>> resultatQuery = query.getResultList();
        for (final Map<String, ?> result : resultatQuery) {
            final CycleDevoirBean cycleDevoirBean = (CycleDevoirBean) result
                    .get("devoir");
            final TypeDevoirBean typeDevoirBean = (TypeDevoirBean) result
                    .get("typeDevoir");

            final CycleDevoirDTO cycleDevoirDTO = new CycleDevoirDTO();
            cycleDevoirDTO.setDateRemise(TypeDateRemise
                    .getTypeDateRemise(cycleDevoirBean.getDateRemise()));
            cycleDevoirDTO.setDescription(cycleDevoirBean.getDescription());
            cycleDevoirDTO.setId(cycleDevoirBean.getId());
            cycleDevoirDTO.setIntitule(cycleDevoirBean.getIntitule());

            if (typeDevoirBean != null && typeDevoirBean.getId() != null) {
                final TypeDevoirDTO typeDevoirDTO = new TypeDevoirDTO();
                typeDevoirDTO.setCode(typeDevoirBean.getCode());
                typeDevoirDTO.setId(typeDevoirBean.getId());
                typeDevoirDTO.setLibelle(typeDevoirBean.getLibelle());
                typeDevoirDTO.setCategorie(TypeCategorieTypeDevoir
                        .getMapCategorieTypeDevoir().get(
                                typeDevoirBean.getCategorie()));
                cycleDevoirDTO.setTypeDevoirDTO(typeDevoirDTO);
            }
            listeDevoir.add(cycleDevoirDTO);
        }

        return listeDevoir;
    }

    /**
     * Charge la liste des groupes collaboratifs d'un cycle.
     * 
     * @param idCycle
     *            le cycle
     * @return une liste.
     */
    @SuppressWarnings("unchecked")
    private List<GroupeDTO> findListeGroupeByCycle(final Integer idCycle) {
        Assert.isNotNull("idCycle", idCycle);
        final List<GroupeDTO> listeGroupe = new ArrayList<GroupeDTO>();

        String requete = "select G from " + CycleGroupeBean.class.getName()
                + " CG " + "INNER JOIN CG.groupe G "
                + "WHERE CG.pk.idCycle = :idCycle";

        // Ajoute les criteres
        final Query query = getEntityManager().createQuery(requete);
        query.setParameter("idCycle", idCycle);
        final List<GroupeBean> resultatQuery = query.getResultList();

        for (final GroupeBean groupeBean : resultatQuery) {
            final GroupeDTO groupeDTO = new GroupeDTO();
            groupeDTO.setCode(groupeBean.getCode());
            groupeDTO.setGroupeCollaboratif(groupeBean.getGroupeCollaboratif());
            groupeDTO.setId(groupeBean.getId());
            groupeDTO.setIntitule(groupeBean.getDesignation());
            listeGroupe.add(groupeDTO);
        }
        return listeGroupe;

    }

    /**
     * Alimente un objet FileUploadDTO à partir d'un PieceJointeBean.
     * 
     * @param pieceJointeBean
     *            la PJ
     * @return un FileUploadDTO
     */
    private FileUploadDTO alimentePieceJointe(
            final PieceJointeBean pieceJointeBean) {
        final FileUploadDTO fileUploadDTO = new FileUploadDTO();
        fileUploadDTO.setId(pieceJointeBean.getId());
        fileUploadDTO.setEnBase(true);
        fileUploadDTO.setNom(pieceJointeBean.getNomFichier());
        fileUploadDTO.setUid(pieceJointeBean.getUid());
        fileUploadDTO.setPathDB(pieceJointeBean.getChemin());
        fileUploadDTO.setActiverLien(true);

        // FIXME Dénormalisation Interdit dans le métier attention si séparation
        // des couches
        FacesUtils.pathUploadFile(fileUploadDTO);
        if (FileUploadUtils.checkExistencePieceJointe(fileUploadDTO)) {
            fileUploadDTO.setActiverLien(true);
        }
        return fileUploadDTO;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<FileUploadDTO> findListePieceJointeSeance(
            final Integer idCycleSeance) {
        Assert.isNotNull("idCycleSeance", idCycleSeance);
        final List<FileUploadDTO> listePieceJointe = new ArrayList<FileUploadDTO>();

        String requete = "select distinct PJ " + "from "
                + PiecesJointesCycleSeancesBean.class.getName() + " PJS "
                + "inner join PJS.pieceJointe PJ "
                + "where PJS.pk.idCycleSeance = :idCycleSeance ";

        // Ajoute les criteres
        final Query query = getEntityManager().createQuery(requete);
        query.setParameter("idCycleSeance", idCycleSeance);
        final List<PieceJointeBean> resultatQuery = query.getResultList();

        for (final PieceJointeBean pieceJointeBean : resultatQuery) {
            final FileUploadDTO fileUploadDTO = alimentePieceJointe(pieceJointeBean);
            listePieceJointe.add(fileUploadDTO);
        }

        return listePieceJointe;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<FileUploadDTO> findListePieceJointeDevoir(
            final Integer idCycleDevoir) {
        Assert.isNotNull("idCycleDevoir", idCycleDevoir);
        final List<FileUploadDTO> listePieceJointe = new ArrayList<FileUploadDTO>();

        String requete = "select distinct PJ " + "from "
                + PiecesJointesCycleDevoirsBean.class.getName() + " PJD "
                + "inner join PJD.pieceJointe PJ "
                + "where PJD.pk.idCycleDevoir = :idCycleDevoir ";

        // Ajoute les criteres
        final Query query = getEntityManager().createQuery(requete);
        query.setParameter("idCycleDevoir", idCycleDevoir);
        final List<PieceJointeBean> resultatQuery = query.getResultList();

        for (final PieceJointeBean pieceJointeBean : resultatQuery) {
            final FileUploadDTO fileUploadDTO = alimentePieceJointe(pieceJointeBean);
            listePieceJointe.add(fileUploadDTO);
        }

        return listePieceJointe;
    }

    /**
     * {@inheritDoc}
     */
    public void deletePieceJointeSeance(final Integer idCycleSeance) {
        Assert.isNotNull("idCycleSeance", idCycleSeance);
        final String query =
                "DELETE FROM " + PiecesJointesCycleSeancesBean.class.getName() +
                " CS WHERE CS.pk.idCycleSeance = :idCycleSeance";
        getEntityManager().createQuery(query).setParameter("idCycleSeance", idCycleSeance).executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    public void deletePieceJointeDevoir(final Integer idCycleDevoir) {
        Assert.isNotNull("idCycleDevoir", idCycleDevoir);
        final String query =
                "DELETE FROM " + PiecesJointesCycleDevoirsBean.class.getName() +
                " CD WHERE CD.pk.idCycleDevoir = :idCycleDevoir";
        getEntityManager().createQuery(query).setParameter("idCycleDevoir", idCycleDevoir).executeUpdate();
    }
    
    /**
     * {@inheritDoc}
     */
    public void deleteDevoir(final Integer idCycleDevoir) {
        Assert.isNotNull("idCycleDevoir", idCycleDevoir);
        final String query =
                "DELETE FROM " + CycleDevoirBean.class.getName() +
                " CD WHERE CD.id = :idCycleDevoir";
        getEntityManager().createQuery(query).setParameter("idCycleDevoir", idCycleDevoir).executeUpdate();
    }
    
    /**
     * {@inheritDoc} 
     */
    public void deleteCycleSeance(final Integer idCycleSeance) {
        Assert.isNotNull("idCycleSeance", idCycleSeance);
        final String query =
                "DELETE FROM " + CycleSeanceBean.class.getName() +
                " S WHERE S.id = :idCycleSeance";
        getEntityManager().createQuery(query).setParameter("idCycleSeance", idCycleSeance).executeUpdate();
    }

    /**
     * {@inheritDoc} 
     */
    public void deleteCycle(final Integer idCycle) {
        Assert.isNotNull("idCycle", idCycle);
        String query =
            "DELETE FROM " + CycleGroupeBean.class.getName() +
            " CG WHERE CG.pk.idCycle = :idCycle";
        getEntityManager().createQuery(query).setParameter("idCycle", idCycle).executeUpdate();
        
        query =
                "DELETE FROM " + CycleBean.class.getName() +
                " C WHERE C.id = :idCycle";
        getEntityManager().createQuery(query).setParameter("idCycle", idCycle).executeUpdate();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<CyclePlageEmploiDTO> findListePlageEmploi(final RechercheCycleEmploiQO rechercheCycleEmploiQO) {
        Assert.isNotNull("rechercheCycleEmploiQO", rechercheCycleEmploiQO);
        Assert.isNotNull("rechercheCycleEmploiQO.GroupeClasse", rechercheCycleEmploiQO.getGroupeClasse());
        Assert.isNotNull("rechercheCycleEmploiQO.IdEnseignant", rechercheCycleEmploiQO.getIdEnseignant());
        
        String requete = 
            "SELECT new map(" +
                "PE.dateDebut as dateDebut," +
                "E.typeSemaine as typeSemaine," +
                "E.jour as jour," +
                "E.heureDebut as heureDebut," +
                "E.heureFin as heureFin," +
                "E.minuteDebut as minuteDebut," +
                "E.minuteFin as minuteFin," +
                "E.idEnseignement as idEnseignement) " +
            "FROM " + 
                EmploiBean.class.getName() + " E " +
                "INNER JOIN E.periode PE " +
            "WHERE " + 
                "E.idEnseignant = :idEnseignant AND "; 
        if (rechercheCycleEmploiQO.getGroupeClasse().getVraiOuFauxClasse()) {
            requete += 
                "E.idClasse = :idClasseGroupe ";
        } else {
            requete += 
                "E.idGroupe = :idClasseGroupe ";
        }
        requete += 
            "ORDER BY PE.dateDebut ASC, E.jour ASC, E.heureDebut ASC";
        
        final Query query = getEntityManager().createQuery(requete);
        query.setParameter("idEnseignant", rechercheCycleEmploiQO.getIdEnseignant());
        query.setParameter("idClasseGroupe", rechercheCycleEmploiQO.getGroupeClasse().getId());
        final List<Map<String,Object>> resultatQuery = query.getResultList();

        final List<CyclePlageEmploiDTO> listePlage = new ArrayList<CyclePlageEmploiDTO>();
        for (final Map<String,Object> result : resultatQuery) {
            CyclePlageEmploiDTO plage = new CyclePlageEmploiDTO();
            
            plage.setDateDebutPeriode((Date) result.get("dateDebut"));
            plage.setHeureDebut((Integer) result.get("heureDebut"));
            plage.setHeureFin((Integer) result.get("heureFin"));
            plage.setMinuteDebut((Integer) result.get("minuteDebut"));
            plage.setMinuteFin((Integer) result.get("minuteFin"));
            plage.setIdEnseignement((Integer) result.get("idEnseignement"));
            
            final Character ctypeSemaine = (Character) result.get("typeSemaine");
            final TypeSemaine typeSemaine = TypeSemaine.getTypeSemaine(ctypeSemaine);
            final TypeJour jour = (TypeJour) result.get("jour");
            plage.setTypeSemaine(typeSemaine);
            plage.setTypeJour(jour);
            
            listePlage.add(plage);
        }
        return listePlage;
    }
}
