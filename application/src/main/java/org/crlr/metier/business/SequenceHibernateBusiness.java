/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequenceHibernateBusiness.java,v 1.39 2010/05/20 14:42:30 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.sequence.RechercheSequencePopupQO;
import org.crlr.dto.application.sequence.RechercheSequenceQO;
import org.crlr.dto.application.sequence.ResultatRechercheSequenceDTO;
import org.crlr.dto.application.sequence.SaveSequenceQO;
import org.crlr.dto.application.sequence.SequenceAffichageQO;
import org.crlr.dto.application.sequence.TypeReglesSequence;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.entity.ClasseBean;
import org.crlr.metier.entity.EnseignantsEnseignementsBean;
import org.crlr.metier.entity.EnseignementBean;
import org.crlr.metier.entity.EnseignementLibelleBean;
import org.crlr.metier.entity.GroupeBean;
import org.crlr.metier.entity.SeanceBean;
import org.crlr.metier.entity.SequenceBean;
import org.crlr.metier.utils.SchemaUtils;
import org.crlr.utils.Assert;
import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.dto.TypeCouleur;
import org.springframework.stereotype.Repository;

/**
 * SequenceHibernateBusiness.
 *
 * @author vibertd
 * @version $Revision: 1.39 $
 */
@Repository
public class SequenceHibernateBusiness extends AbstractBusiness
    implements SequenceHibernateBusinessService {
    final static String AJOUTER = "ajouter";
    final static String MODIFIER = "modifier";
    final static String DUPLIQUER = "dupliquer";

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public SequenceBean find(Integer id) {
        Assert.isNotNull("id", id);

        final SequenceBean sequence;

        final String query =
            "SELECT s FROM " + SequenceBean.class.getName() + " s WHERE s.id = :id";

        final List<SequenceBean> liste =
            getEntityManager().createQuery(query).setParameter("id", id).getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            sequence = liste.get(0);
        } else {
            sequence = null;
        }

        return sequence;
    }
    
    public SequenceDTO findSequenceDTO(SequenceDTO sequenceDTO) {
        if (sequenceDTO == null) {
            return null;
        }
        
        if (sequenceDTO.estRenseigne()) {
            return sequenceDTO;
        }
        
        SequenceBean sequenceBean = null;
        
        String codeSequence = sequenceDTO.getCode();

        if (sequenceDTO.getId() != null) {
            sequenceBean = find(sequenceDTO.getId());
        } else if (!StringUtils.isEmpty(codeSequence)) {
            // Mieux de passer l'id mais au cas où il n'y a que le code
            sequenceBean =         findByCode(codeSequence);
        } else {
            log.warn("Pas d'id ni de code");
            return sequenceDTO;
        }
        
        ObjectUtils.copyProperties(sequenceDTO, sequenceBean);
        
        if (sequenceBean.getClasse() != null) {
            sequenceDTO.getGroupesClassesDTO().setTypeGroupe(TypeGroupe.CLASSE);
            ObjectUtils.copyProperties(sequenceDTO.getGroupesClassesDTO(), sequenceBean.getClasse());
        } else {
            sequenceDTO.getGroupesClassesDTO().setTypeGroupe(TypeGroupe.GROUPE);
            ObjectUtils.copyProperties(sequenceDTO.getGroupesClassesDTO(), sequenceBean.getGroupe());
        }
        
        return sequenceDTO;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public SequenceBean findByCode(String code) {
        Assert.isNotNull("code", code);

        final SequenceBean sequence;

        final String query =
            "SELECT s FROM " + SequenceBean.class.getName() + " s WHERE s.code = :code";

        final List<SequenceBean> liste =
            getEntityManager().createQuery(query).setParameter("code", code)
                .getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            sequence = liste.get(0);
        } else {
            sequence = null;
        }

        return sequence;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<SequenceDTO>> findSequencePopup(RechercheSequencePopupQO rechercheSequencePopupQO)
        throws MetierException {
        Assert.isNotNull("rechercheSequencePopupQO", rechercheSequencePopupQO);

        final ResultatDTO<List<SequenceDTO>> resultat =
            new ResultatDTO<List<SequenceDTO>>();

        final Integer idEnseignant = rechercheSequencePopupQO.getIdEnseignant();
        final Integer idEtablissement = rechercheSequencePopupQO.getIdEtablissement();
        final Integer idAnneeScolaire = rechercheSequencePopupQO.getIdAnneeScolaire();
        final Integer idClasseGroupe = rechercheSequencePopupQO.getIdClasseGroupe();
        final Integer idEnseignement = rechercheSequencePopupQO.getIdEnseignement();
        
        String query1 = 
            "SELECT " +
                " new map( " +
                    " SEQ as sequenceBean, " +
                    " CLA as classeBean, " +
                    " GRP as groupeBean, " +
                    " ENS.designation as designationEnseignement" +
                " )" + 
            
            " FROM " + SequenceBean.class.getName() + " SEQ " +
                " INNER JOIN SEQ.enseignement ENS " +
                " LEFT JOIN SEQ.classe CLA " +
                " LEFT JOIN SEQ.groupe GRP, " +
                EnseignantsEnseignementsBean.class.getName() + " EE " + 
            " WHERE " +
                //Soit la classe est la bonne ou la groupe
                " ( (CLA.idAnneeScolaire = :idAnneeScolaire AND " +
                " CLA.idEtablissement = :idEtablissement) OR " +
                "(   GRP.idAnneeScolaire = :idAnneeScolaire AND " +
                " GRP.idEtablissement = :idEtablissement  ) ) AND " +
                " SEQ.idEnseignant = :idEnseignant AND " + 
                " EE.pk.idEnseignant = SEQ.idEnseignant AND " +
                " EE.pk.idEnseignement = ENS.id AND " + 
                " EE.pk.idEtablissement = :idEtablissement ";
        
        if (idClasseGroupe != null && rechercheSequencePopupQO.getTypeGroupeClasse() == TypeGroupe.CLASSE) {
            query1 += " AND CLA.id = :idClasseGroupe";
        } else if (idClasseGroupe != null && rechercheSequencePopupQO.getTypeGroupeClasse() == TypeGroupe.GROUPE) {
            query1 += " AND GRP.id = :idClasseGroupe";
        }

        if (idEnseignement != null ) {
            query1 += " AND ENS.id = :idEnseignement";
        }
        
        query1 += " ORDER BY SEQ.id ASC";
        
        Query query = getEntityManager().createQuery(query1)
                .setParameter("idEnseignant", idEnseignant)
                .setParameter("idAnneeScolaire", idAnneeScolaire)
                .setParameter("idEtablissement", idEtablissement);
        
        if (idClasseGroupe != null && rechercheSequencePopupQO.getTypeGroupeClasse() != null) {
            query.setParameter("idClasseGroupe", idClasseGroupe);
        }
        if (idEnseignement != null) {
            query.setParameter("idEnseignement", idEnseignement);
        }
        
        
        final List<Map<String, ?>> listeSequenceResult = query
            .getResultList();
        
        final List<SequenceDTO> listeSequenceDTO = new ArrayList<SequenceDTO>();

        for (final Map<String, ?> result : listeSequenceResult) {
            final SequenceDTO sequenceDTO = new SequenceDTO();
            final SequenceBean sequenceBean = (SequenceBean) result.get("sequenceBean");
            
            ObjectUtils.copyProperties(sequenceDTO,  sequenceBean);
            
            final ClasseBean classeBean = (ClasseBean) result.get("classeBean");
            final GroupeBean groupeBean = (GroupeBean) result.get("groupeBean");
            
            sequenceDTO.setGroupesClassesDTO(new GroupesClassesDTO());
            if (classeBean != null) {
                ObjectUtils.copyProperties(sequenceDTO.getGroupesClassesDTO(), classeBean);
                sequenceDTO.getGroupesClassesDTO().setTypeGroupe(TypeGroupe.CLASSE);
            } else {
                ObjectUtils.copyProperties(sequenceDTO.getGroupesClassesDTO(), groupeBean);
                sequenceDTO.getGroupesClassesDTO().setTypeGroupe(TypeGroupe.GROUPE);
            }
            
                                    
            sequenceDTO.setDesignationEnseignement((String) result.get("designationEnseignement"));
            listeSequenceDTO.add(sequenceDTO);
        }

        resultat.setValeurDTO(listeSequenceDTO);

        return resultat;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({"unchecked"})
    public ResultatDTO<List<ResultatRechercheSequenceDTO>> findSequence(RechercheSequenceQO rechercheSequenceQO)
        throws MetierException {
        Assert.isNotNull("rechercheSeanceQO", rechercheSequenceQO);

        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        //Paramètres de recherche
        final Integer idEtablissement = rechercheSequenceQO.getIdEtablissement();
        final Integer idEnseignement = rechercheSequenceQO.getIdEnseignement();
        final Integer idClasseGroupe = rechercheSequenceQO.getIdClasseGroupe();
        final TypeGroupe typeGroupe = rechercheSequenceQO.getTypeGroupe();
        final Date dateDebut = rechercheSequenceQO.getDateDebut();
        final Date dateFin = rechercheSequenceQO.getDateFin();

        //Règles de gestion SEQUENCE_04
        if (!this.checkSEQUENCE04(dateDebut, dateFin)) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_04.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "La date de fin de pèriode doit être postèrieure ou égale à la date de début.");
        }

        //Règles de gestion SEQUENCE_05
        if (!this.checkSEQUENCE05(dateDebut, dateFin)) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_05.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,
                                      "Vous devez saisir la date de début de pèriode.");
        }

        final ResultatDTO<List<ResultatRechercheSequenceDTO>> resultat =
            new ResultatDTO<List<ResultatRechercheSequenceDTO>>();
        String requeteWHERE = "";

        if (idEnseignement != null) {
            requeteWHERE = requeteWHERE.concat(" AND SEQ.idEnseignement = :idEnseignement ");
        }
        if (idClasseGroupe != null) {
            if (TypeGroupe.GROUPE  == (typeGroupe)) {
                requeteWHERE = requeteWHERE.concat(" AND SEQ.idGroupe = :idGroupe ");
            } else if (TypeGroupe.CLASSE  == (typeGroupe)) {
                requeteWHERE = requeteWHERE.concat(" AND SEQ.idClasse = :idClasse ");
            }
        } else {          
           //restriction à l'établissement
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
            requeteWHERE = requeteWHERE.concat(" AND SEQ.dateDebut >= :dateDebut ");
        }
        if (dateFin != null) {
            requeteWHERE = requeteWHERE.concat(" AND SEQ.dateDebut <= :dateFin ");
        }
        
        String requete = "";
        
        if (idClasseGroupe == null && typeGroupe != null) {
            if (typeGroupe  == (TypeGroupe.GROUPE)) {
                requete =
                    "SELECT " + 
                        "new map( SEQ.id as idSequence, " + "SEQ.code as codeSequence, " +
                        "GRP.code as codeGroupe, " +
                        "GRP.designation as designationGroupe, " +
                        "ENS.designation as designationEnseignement, " +
                        "SEQ.intitule as intituleSequence, " +
                        "SEQ.description as descriptionSequence, " +
                        "SEQ.dateDebut as dateDebutSequence, " + "SEQ.dateFin as dateFinSequence) " +
                    " FROM " + 
                        SequenceBean.class.getName() + " SEQ " +
                            " INNER JOIN SEQ.enseignement ENS " +
                            " INNER JOIN SEQ.groupe GRP " +
                    " WHERE " +
                        " SEQ.enseignant.id = :idEnseignant " +
                        requeteWHERE +
                    " ORDER BY SEQ.id ASC";
            } else if (typeGroupe  == (TypeGroupe.CLASSE)) {
                requete =
                    "SELECT " + 
                        "new map( SEQ.id as idSequence, " + "SEQ.code as codeSequence, " +
                        "CLA.code as codeClasse, " +
                        "CLA.designation as designationClasse, " +
                        "ENS.designation as designationEnseignement, " +
                        "SEQ.intitule as intituleSequence, " +
                        "SEQ.description as descriptionSequence, " +
                        "SEQ.dateDebut as dateDebutSequence, " + "SEQ.dateFin as dateFinSequence) " +
                    " FROM " + 
                        SequenceBean.class.getName() + " SEQ " +
                            " INNER JOIN SEQ.enseignement ENS " +
                            " INNER JOIN SEQ.classe CLA " +
                    " WHERE " +
                        " SEQ.enseignant.id = :idEnseignant " +
                        requeteWHERE +
                    " ORDER BY SEQ.id ASC";
            }
        } else {
            requete =
                "SELECT " + 
                    "new map( SEQ.id as idSequence, " + "SEQ.code as codeSequence, " +
                    "CLA.code as codeClasse, " + "GRP.code as codeGroupe, " +
                    "CLA.designation as designationClasse, " +
                    "GRP.designation as designationGroupe, " +
                    "ENS.designation as designationEnseignement, " +
                    "SEQ.intitule as intituleSequence, " +
                    "SEQ.description as descriptionSequence, " +
                    "SEQ.dateDebut as dateDebutSequence, " + "SEQ.dateFin as dateFinSequence) " +
                " FROM " + 
                    SequenceBean.class.getName() + " SEQ " +
                        " INNER JOIN SEQ.enseignement ENS " +
                        " LEFT JOIN SEQ.classe CLA " +
                        " LEFT JOIN SEQ.groupe GRP " +
                " WHERE " +
                    " SEQ.enseignant.id = :idEnseignant " +
                    requeteWHERE +
                " ORDER BY SEQ.id ASC";
        }

        final Query query = getEntityManager().createQuery(requete);

        query.setParameter("idEnseignant", rechercheSequenceQO.getIdEnseignant());

        if (idEnseignement != null) {
            query.setParameter("idEnseignement", idEnseignement);
        }
        if (idClasseGroupe != null) {
            if (TypeGroupe.GROUPE  == (typeGroupe)) {
                query.setParameter("idGroupe", idClasseGroupe);
            } else if (TypeGroupe.CLASSE  == (typeGroupe)) {
                query.setParameter("idClasse", idClasseGroupe);
            }
        } else {
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
        final List<ResultatRechercheSequenceDTO> listeSequenceDTO =
            new ArrayList<ResultatRechercheSequenceDTO>();

        for (final Map<String, ?> result : resultatQuery) {
            final ResultatRechercheSequenceDTO resultatRechercheSequenceDTO =
                new ResultatRechercheSequenceDTO();
            resultatRechercheSequenceDTO.setId((Integer) result.get("idSequence"));
            resultatRechercheSequenceDTO.setCode((String) result.get("codeSequence"));

            GroupesClassesDTO groupesClassesDTO = new GroupesClassesDTO();
            
            
            if (idClasseGroupe != null) {
                groupesClassesDTO.setTypeGroupe(typeGroupe);
                
                if (TypeGroupe.GROUPE  == (typeGroupe)) {
                    groupesClassesDTO.setCode((String) result.get("codeGroupe"));
                    groupesClassesDTO.setDesignation((String) result.get("designationGroupe"));
                    
                } else if (TypeGroupe.CLASSE  == (typeGroupe)) {
                    groupesClassesDTO.setCode((String) result.get("codeClasse"));
                    groupesClassesDTO.setDesignation((String) result.get("designationClasse"));
                    
                }
            } else {
                if (result.get("codeClasse") != null) {
                    groupesClassesDTO.setCode((String) result.get("codeClasse"));
                    groupesClassesDTO.setDesignation((String) result.get("designationClasse"));
                    groupesClassesDTO.setTypeGroupe(TypeGroupe.CLASSE);
                } else {
                    groupesClassesDTO.setCode((String) result.get("codeGroupe"));
                    groupesClassesDTO.setDesignation((String) result.get("designationGroupe"));
                    groupesClassesDTO.setTypeGroupe(TypeGroupe.GROUPE);
                }
            }
            
            resultatRechercheSequenceDTO.setGroupesClassesDTO(groupesClassesDTO);

            resultatRechercheSequenceDTO.setDesignationEnseignement((String) result.get("designationEnseignement"));
            resultatRechercheSequenceDTO.setIntitule((String) result.get("intituleSequence"));
            resultatRechercheSequenceDTO.setDescription((String) result.get("descriptionSequence"));
            resultatRechercheSequenceDTO.setDateDebut((Date) result.get("dateDebutSequence"));
            resultatRechercheSequenceDTO.setDateFin((Date) result.get("dateFinSequence"));
            resultatRechercheSequenceDTO.setDateFin((Date) result.get("dateFinSequence"));
            if (typeGroupe != null) {
                resultatRechercheSequenceDTO.getGroupesClassesDTO().setTypeGroupe(typeGroupe);
            }
            listeSequenceDTO.add(resultatRechercheSequenceDTO);
        }
        
        if(listeSequenceDTO.size() == 0) {
            conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_24.name(),
                    Nature.INFORMATIF));
            throw new MetierException(conteneurMessage, "Il n'existe aucun résultat pour votre recherche.");
            
        }

        resultat.setValeurDTO(listeSequenceDTO);

        return resultat;
    }

    /**
     * Regle SEQUENCE_04 La date de fin de pèriode doit être postèrieure ou égale
     * à la date de début.
     *
     * @param dateDebut date de début
     * @param dateFin date de fin
     *
     * @return true si tout est ok, sinon false
     */
    private boolean checkSEQUENCE04(Date dateDebut, Date dateFin) {
        boolean checkResult = true;
        if ((dateDebut != null) && (dateFin != null)) {
            if (DateUtils.compare(dateDebut, dateFin, false) > 0) {
                checkResult = false;
            }
        }
        return checkResult;
    }

    /**
     * Regle SEQUENCE_05 Vous devez saisir la date de début de pèriode.
     *
     * @param dateDebut date de début
     * @param dateFin date de fin
     *
     * @return true si tout est ok, sinon false
     */
    private boolean checkSEQUENCE05(Date dateDebut, Date dateFin) {
        boolean checkResult = true;
        if ((dateDebut == null) && (dateFin != null)) {
            checkResult = false;
        }
        return checkResult;
    }

    /**
     * {@inheritDoc}
     */
    public void deleteSequence(ResultatRechercheSequenceDTO resultatRechercheSequenceDTO)
    throws MetierException {
        final Integer idSequence = resultatRechercheSequenceDTO.getId();
        final String querySequence =
            "DELETE FROM " + SequenceBean.class.getName() + " s WHERE s.id = :id";

        getEntityManager().createQuery(querySequence).setParameter("id", idSequence)
        .executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    public void deleteSequencesVide(RechercheSequenceQO rechercheSequenceQO) throws MetierException {
        Assert.isNotNull("rechercheSequenceQO", rechercheSequenceQO);
        Assert.isNotNull("idEnseignant", rechercheSequenceQO.getIdEnseignant());
        Assert.isNotNull("idEtablissement", rechercheSequenceQO.getIdEtablissement());
        
        final String querySequence =
            "DELETE FROM " + SequenceBean.class.getName() + " s " +
            "WHERE s.idEnseignant = :idEnseignant " +
            "AND   s.idEtablissement = :idEtablissement " +
            "AND not exists( SELECT 1 FROM " + SeanceBean.class.getName() + " SEA " + 
                                " WHERE SEA.idSequence = s.id )";

        getEntityManager().createQuery(querySequence)
            .setParameter("idEnseignant", rechercheSequenceQO.getIdEnseignant())
            .setParameter("idEtablissement", rechercheSequenceQO.getIdEtablissement())
            .executeUpdate();
    }
    
    /**
     * {@inheritDoc}
     */
    public Integer saveSequence(SaveSequenceQO saveSequenceQO)
                         throws MetierException {
        Assert.isNotNull("saveSequenceQO", saveSequenceQO);

        Integer idSequence = saveSequenceQO.getId();
        final String intitule = saveSequenceQO.getIntitule();
        final String description = saveSequenceQO.getDescription();
        final Date dateDebut = saveSequenceQO.getDateDebut();
        final Date dateFin = saveSequenceQO.getDateFin();
        final Integer idEnseignant = saveSequenceQO.getIdEnseignant();
        final Integer idEtablissement = saveSequenceQO.getIdEtablissement();
        final Integer idEnseignement = saveSequenceQO.getIdEnseignement();
        final TypeGroupe typeClasseGroupe = saveSequenceQO.getClasseGroupeSelectionne().getTypeGroupe();
        Integer idGroupe = null;
        Integer idClasse = null;
        if (typeClasseGroupe == TypeGroupe.GROUPE) {
            idGroupe = saveSequenceQO.getIdClasseGroupe();
        } else {
            idClasse = saveSequenceQO.getIdClasseGroupe();
        }

        final SequenceBean sequenceBean;
        final String action;

        if (idSequence != null) {
            action = SequenceHibernateBusiness.MODIFIER;
            sequenceBean = getEntityManager().find(SequenceBean.class, idSequence);

            final ConteneurMessage conteneurMessage = new ConteneurMessage();
            if (sequenceBean == null) {
                conteneurMessage.add(new Message(TypeReglesSequence.SEQUENCE_14.name(),
                                                 Nature.BLOQUANT));
                throw new MetierException(conteneurMessage, "La séquence n'existe pas.");
            }
        } else {
            action = SequenceHibernateBusiness.AJOUTER;
            final BaseHibernateBusiness baseHibernateBusiness = new BaseHibernateBusiness(this.getEntityManager());
            idSequence = baseHibernateBusiness.getIdInsertion("cahier_sequence");
            sequenceBean = new SequenceBean();
            sequenceBean.setId(idSequence);
            sequenceBean.setCode("SEQ" + idSequence);
        }
        sequenceBean.setIntitule(intitule);
        sequenceBean.setDescription(description);
        sequenceBean.setDateDebut(dateDebut);
        sequenceBean.setDateFin(dateFin);
        
        sequenceBean.setIdEnseignant(idEnseignant);
        sequenceBean.setIdEtablissement(idEtablissement);
        sequenceBean.setIdEnseignement(idEnseignement);
        sequenceBean.setIdGroupe(idGroupe);
        sequenceBean.setIdClasse(idClasse);

        if (SequenceHibernateBusiness.MODIFIER.equals(action)) {
            getEntityManager().flush();
        } else if (SequenceHibernateBusiness.AJOUTER.equals(action)) {
            getEntityManager().persist(sequenceBean);
            getEntityManager().flush();
        }

        return sequenceBean.getId();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public SequenceDTO findSequenceAffichage(final SequenceAffichageQO sequenceAffichageQO)
                                      throws MetierException {
        final Integer idSequence = sequenceAffichageQO.getId();
        
        final String schema = 
            SchemaUtils.getSchemaCourantOuArchive(sequenceAffichageQO.getArchive(), sequenceAffichageQO.getExercice());         
        
        final SequenceDTO sequenceDTO = new SequenceDTO();

        final String query =
            "SELECT SEQ.id as seqId, SEQ.code as seqCode, " +
            "CLA.code as codeCla, CLA.designation as desCla, CLA.id as idCla, " +
            "GRP.code as codeGrp, GRP.designation as desGrp, GRP.id as idGrp, " +
            "ENS.id as idEns,  " +
            "coalesce(LIBENS.libelle, ENS.designation) as desEns, " +
            "SEQ.intitule as intSeq, SEQ.description as desSeq, " +
            "SEQ.date_debut as debSeq, SEQ.date_fin as finSeq, " +
            "SEQ.id_enseignant as idEnseignant, " +
            "ENSEIGNANT.nom as nomEns, ENSEIGNANT.prenom as prenomEns, ENSEIGNANT.civilite as civiliteEns, " +
            "SEQ.id_etablissement as idEtablissement, " +
            "(select couleur from " + SchemaUtils.getTableAvecSchema(schema, "cahier_couleur_enseignement_classe") +
            		" c where c.id_enseignant = SEQ.id_enseignant " +
            		" and c.id_etablissement = SEQ.id_etablissement " +
            		" and c.id_enseignement = SEQ.id_enseignement "+
            		" and (c.id_groupe is null OR  c.id_groupe = SEQ.id_groupe ) "+
            		" and (c.id_classe is null OR  c.id_classe = SEQ.id_classe) ) as couleur " +
            " FROM " + SchemaUtils.getTableAvecSchema(schema, "cahier_sequence") + " SEQ " +
            " INNER JOIN " + SchemaUtils.getTableAvecSchema(schema, "cahier_enseignement") + " ENS ON (ENS.id = SEQ.id_enseignement) " +
            " INNER JOIN " + SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant") + 
            " ENSEIGNANT ON (ENSEIGNANT.id = SEQ.id_enseignant) " +
            " LEFT JOIN " + SchemaUtils.getTableAvecSchema(schema, "cahier_libelle_enseignement") + 
                  " LIBENS ON (LIBENS.id_etablissement=SEQ.id_etablissement and LIBENS.id_enseignement=SEQ.id_enseignement) " +
            " LEFT JOIN " + SchemaUtils.getTableAvecSchema(schema, "cahier_classe") + " CLA ON (CLA.id=SEQ.id_classe) " +
            " LEFT JOIN " + SchemaUtils.getTableAvecSchema(schema, "cahier_groupe") + " GRP ON (GRP.id=SEQ.id_groupe) " +
            "WHERE SEQ.id =?";

        final List<Object[]> resultatQuery =
            getEntityManager().createNativeQuery(query).setParameter(1, idSequence)
                .getResultList();
        
        if (!CollectionUtils.isEmpty(resultatQuery)) {
            final Object[] result = resultatQuery.get(0);    
            
            int champ = 0;
            
            sequenceDTO.setId((Integer) result[champ++]);
            sequenceDTO.setCode((String) result[champ++]);
            
            final String codeClasse = (String) result[champ++];
            final String desgClasse = (String) result[champ++];
            final Integer idClasse = (Integer)  result[champ++];
            final String codeGroupe = (String) result[champ++];
            final String desgGroupe = (String) result[champ++];
            final Integer idGroupe = (Integer)  result[champ++];
            
            if (codeClasse != null) {
                sequenceDTO.setGroupesClassesDTO(new GroupesClassesDTO(
                        idClasse,
                        codeClasse,
                        TypeGroupe.CLASSE,
                        desgClasse));
            } else {
                sequenceDTO.setGroupesClassesDTO(new GroupesClassesDTO(
                       idGroupe,
                        codeGroupe,
                        TypeGroupe.GROUPE,
                        desgGroupe));
            }
            sequenceDTO.setIdEnseignement((Integer) result[champ++]);
            sequenceDTO.setDesignationEnseignement((String) result[champ++]);
            sequenceDTO.setIntitule((String) result[champ++]);
            sequenceDTO.setDescription((String) result[champ++]);
            sequenceDTO.setDateDebut((Date) result[champ++]);
            sequenceDTO.setDateFin((Date) result[champ++]);
            sequenceDTO.setIdEnseignant((Integer) result[champ++]);
            sequenceDTO.getEnseignantDTO().setNom((String) result[champ++]);
            sequenceDTO.getEnseignantDTO().setPrenom((String) result[champ++]);
            sequenceDTO.getEnseignantDTO().setCivilite((String) result[champ++]);
            sequenceDTO.setIdEtablissement((Integer) result[champ++]);
            sequenceDTO.setTypeCouleur(TypeCouleur.getTypeCouleurById((String) result [champ++]));
        }

        return sequenceDTO;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean checkUnicite(SaveSequenceQO saveSequenceQO)
                         throws MetierException {
        Assert.isNotNull("saveSequenceQO", saveSequenceQO);
        
        boolean checkResult = false;
        
        final String mode = saveSequenceQO.getMode();
        
        final Integer idEnseignement = saveSequenceQO.getIdEnseignement();
        final Integer idEnseignant = saveSequenceQO.getIdEnseignant();
        final Integer idClasseGroupe = saveSequenceQO.getIdClasseGroupe();
        final String intitule = saveSequenceQO.getIntitule();
        final TypeGroupe typeClasseGroupe = saveSequenceQO.getClasseGroupeSelectionne().getTypeGroupe();
        final String query = "SELECT SEQ FROM " +
                    SequenceBean.class.getName() + " SEQ " +
                    " INNER JOIN SEQ.enseignement ENS, " +
                    EnseignantsEnseignementsBean.class.getName() + " EE " + " WHERE " +
                    " EE.pk.idEnseignement = ENS.id AND " +
                    " ENS.id = :idEnseignement AND " +
                    " EE.pk.idEnseignant = :idEnseignant AND " +
                    " SEQ.intitule = :intitule AND " +
                    (TypeGroupe.GROUPE == typeClasseGroupe ?
                    " SEQ.idGroupe = :idClasseGroupe" :
                        " SEQ.idClasse = :idClasseGroupe")
                        ;
        

        final List<SequenceBean> resultatQuery =
            getEntityManager().createQuery(query)
                .setParameter("idEnseignement", idEnseignement)
                .setParameter("idEnseignant", idEnseignant)
                .setParameter("intitule", intitule)
                .setParameter("idClasseGroupe", idClasseGroupe)
                .getResultList();
        
        if (resultatQuery.size() == 0) {
            checkResult = true;
        } else {
            if (resultatQuery.size() == 1 && AbstractForm.MODE_MODIF.equals(mode)) {
                final SequenceBean sequenceBean = (SequenceBean) resultatQuery.get(0);
                if(sequenceBean.getId().equals(saveSequenceQO.getId())) {
                    checkResult = true;
                }
            }
        }

        return checkResult;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer findEnseignementSequence(Integer idSequence)
            throws MetierException {
        
        Assert.isNotNull("idSequence", idSequence);
        final String query = 
            "SELECT " + 
                " new map(SEQ.idEnseignement as idEnseignement)" + 
            " FROM " +
                SequenceBean.class.getName() + " SEQ " +
            " WHERE " +
                " SEQ.id = :idSequence";
        
        final List<Map<String, ?>> resultatQuery = 
            getEntityManager().createQuery(query).setParameter("idSequence", idSequence).getResultList();
        
        for (final Map<String, ?> result : resultatQuery) {
            return (Integer) result.get("idEnseignement");
        }
        return null;
    }
    
   /**
    * {@inheritDoc} 
    */
    public Boolean checkExistenceSequenceEnseignant(final Integer idEnseignant, final Integer idEtablissement)  {
        Assert.isNotNull("idEnseignant", idEnseignant);
        Assert.isNotNull("idEtablissement", idEtablissement);
        
        //verification 
        final String query = "SELECT 1 FROM " + SequenceBean.class.getName() + 
        " SEQ " + 
        " WHERE SEQ.idEnseignant=:idEnseignant AND SEQ.idEtablissement=:idEtablissement";
        
        final Boolean vraiOuFauxSequence = !CollectionUtils.isEmpty(getEntityManager().createQuery(query)
                .setParameter("idEnseignant", idEnseignant)
                .setParameter("idEtablissement", idEtablissement)
                .getResultList());
        
        return vraiOuFauxSequence;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Set<Integer> findSequenceEtablissement(final Integer idEtablissement, final Integer idEnseignant) {
        Assert.isNotNull("idEtablissement", idEtablissement);
        final String query = 
            "select SEQ.id FROM " + 
            SequenceBean.class.getName() + " SEQ "+
            " WHERE " +
            " SEQ.idEtablissement = :idEtablissement AND SEQ.idEnseignant=:idEnseignant ";
        
        final List<Integer> listeId = 
            getEntityManager().createQuery(query)
            .setParameter("idEtablissement", idEtablissement)
            .setParameter("idEnseignant", idEnseignant)
            .getResultList();
        
        return new HashSet<Integer>(listeId);
    }
    
    
   

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<EnseignantDTO> findEnseignantsEleve(
            Integer idClasse, Set<Integer> idsGroupe, Integer idEnseignement) {
        Assert.isNotNull("idClasse", idClasse);
        Assert.isNotNull("idsGroupe", idsGroupe);

        final Set<Integer> idEnseignantsAjoutes = new HashSet<Integer>();
        final List<EnseignantDTO> res = new ArrayList<EnseignantDTO>();
        
        String requeteEnseignantClasse = 
            " SELECT " + 
                "new map( SEQ.idEnseignant as idEnseignant, ENS.nom as nom, ENS.prenom as prenom, ENS.civilite as civilite )" +
            " FROM " + 
                SequenceBean.class.getName() + " SEQ " +
                " INNER JOIN SEQ.enseignant ENS " +                
            " WHERE " +
                " SEQ.idClasse = :idClasse "
        ;
        
        if (idEnseignement != null){
            requeteEnseignantClasse += "AND SEQ.idEnseignement = :idEnseignement";
        }
        
        final Query queryClasse = getEntityManager().createQuery(requeteEnseignantClasse).setParameter("idClasse", idClasse);
        if (idEnseignement != null){
            queryClasse.setParameter("idEnseignement", idEnseignement);
        }
        List<Map<String, ?>> resultatQuery = queryClasse.getResultList();

        for (final Map<String, ?> result : resultatQuery) {
            final Integer idEnseignant = (Integer) result.get("idEnseignant");
            if (! idEnseignantsAjoutes.contains(idEnseignant)){
                final EnseignantDTO enseignant = new EnseignantDTO();
                enseignant.setId(idEnseignant);
                enseignant.setCivilite( (String) result.get("civilite") );
                enseignant.setNom((String) result.get("nom") );
                enseignant.setPrenom((String) result.get("prenom"));
                
                res.add(enseignant);
                idEnseignantsAjoutes.add(idEnseignant);
            }
        }
        
        String listeGroupe = "(0";
        for (final Integer idGroupe : idsGroupe) {
            listeGroupe += (", " + idGroupe);
        }
        listeGroupe += ")";
        
        String requeteEnseignantGroupe = 
            " SELECT " + 
                "new map( SEQ.idEnseignant as idEnseignant, " + "ENS.nom as nom, ENS.prenom as prenom, ENS.civilite as civilite )" +
            " FROM " + 
                SequenceBean.class.getName() + " SEQ " +
                " INNER JOIN SEQ.enseignant ENS " +
            " WHERE " +
                " SEQ.idGroupe IN " +listeGroupe
        ;
        
        if (idEnseignement != null){
            requeteEnseignantGroupe += "AND SEQ.idEnseignement = :idEnseignement";
        }
        
        final Query queryGroupe = getEntityManager().createQuery(requeteEnseignantGroupe);
        if (idEnseignement != null){
            queryGroupe.setParameter("idEnseignement", idEnseignement);
        }
        resultatQuery = queryGroupe.getResultList();

        
        
        for (final Map<String, ?> result : resultatQuery) {
            final Integer idEnseignant = (Integer) result.get("idEnseignant");
            if (! idEnseignantsAjoutes.contains(idEnseignant)){
                final EnseignantDTO enseignant = new EnseignantDTO();
                enseignant.setId(idEnseignant);
                enseignant.setCivilite( (String) result.get("civilite") );
                enseignant.setNom((String) result.get("nom") );
                enseignant.setPrenom((String) result.get("prenom"));
                
                res.add(enseignant);
                idEnseignantsAjoutes.add(idEnseignant);
            }
        }

        return res;

    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<EnseignementDTO> findEnseignementsEleve(RechercheEnseignementPopupQO rechercheEnseignementPopupQO) {
        final GroupesClassesDTO classe = rechercheEnseignementPopupQO.getGroupeClasseSelectionne();
        final List<GroupeDTO> groupes = rechercheEnseignementPopupQO.getGroupesSelectionne();
        final Integer idEtablissement = rechercheEnseignementPopupQO.getIdEtablissement();
        Assert.isNotNull("classe", classe);
        Assert.isNotNull("idsGroupe", groupes);
        Assert.isNotNull("idEtablissement", idEtablissement);

        final List<EnseignementBean> enseignements = new ArrayList<EnseignementBean>();
        final List<EnseignementDTO> res = new ArrayList<EnseignementDTO>();
        final Set<Integer> enseignementsId = new HashSet<Integer>();
        
        final String requeteEnseignementClasse = 
            " SELECT " + 
                " DISTINCT ENS " +
            " FROM " + 
                SequenceBean.class.getName() + " SEQ " +
                " INNER JOIN SEQ.enseignement ENS " +
            " WHERE " +
                " SEQ.idClasse = :idClasse "
        ;
        
        final Query queryClasse = getEntityManager().createQuery(requeteEnseignementClasse).setParameter("idClasse", classe.getId());
        enseignements.addAll(queryClasse.getResultList());

        String listeGroupe = "(0";
        for (final GroupeDTO groupe : groupes) {
            listeGroupe += (", " + groupe.getId());
        }
        listeGroupe += ")";
        
        final String requeteEnseignantGroupe = 
            " SELECT " + 
                " DISTINCT ENS" +
            " FROM " + 
                SequenceBean.class.getName() + " SEQ " +
                " INNER JOIN SEQ.enseignement ENS " +
            " WHERE " +
                " SEQ.idGroupe IN " +listeGroupe
        ;
        
        
        final Query queryGroupe = getEntityManager().createQuery(requeteEnseignantGroupe);
        enseignements.addAll(queryGroupe.getResultList());
        
        //Conversion du type d'objet + filtre pour eviter les doublons entre ce qui provient des classes et groupes.
        for (EnseignementBean enseignement: enseignements){
            if (! enseignementsId.contains(enseignement.getId())){
                final EnseignementDTO enseignementDTO = new EnseignementDTO();
                enseignementDTO.setId(enseignement.getId());
                enseignementDTO.setIntitule(enseignement.getDesignation());
                enseignementDTO.setCode(enseignement.getCode());
                res.add(enseignementDTO);
                enseignementsId.add(enseignement.getId());
            }
        }
        return res;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer findIdSequenceByEnsEtClasseGroupe(int idEns, int idClasseGroupe){
        List<Integer> idSequence = new ArrayList<Integer>();
        final String requete = "SELECT id FROM " + SequenceBean.class.getName() + 
        " WHERE idEnseignement = :idEns AND (idClasse = :idClasse OR idGroupe = :idGroupe )";
        idSequence = getEntityManager().createQuery(requete).setParameter("idEns", idEns)
        .setParameter("idClasse", idClasseGroupe).setParameter("idGroupe", idClasseGroupe).getResultList();
        if(! CollectionUtils.isEmpty(idSequence)){
            return idSequence.get(0);
        }else{
            return null;
        }
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
     * Recherche des séquences valides entre deux dates (souvent lundi ->
     * dimanche).
     * 
     * @param rechercheSequence r
     * @return la liste
     */
    @SuppressWarnings("unchecked")
    public List<SequenceDTO> chercherSequenceSemaine(RechercheSequenceQO rechercheSequence) {
        Assert.isNotNull("rechercheSequence", rechercheSequence);
        Assert.isNotNull("DateDebut", rechercheSequence.getDateDebut());
        Assert.isNotNull("DateFin", rechercheSequence.getDateFin());
        Assert.isNotNull("IdEnseignant", rechercheSequence.getIdEnseignant());
        Assert.isNotNull("IdEtablissement", rechercheSequence.getIdEtablissement());
        final List<Map<String, ?>> listeSequenceResult;

        final String query = 
            " SELECT new Map(" +
            "   S.code as codeSequence," +
            "   S.id as idSequence," +
            "   COALESCE(C.code, G.code) as codeClasseGroupe," +
            "   S.dateDebut as dateDebut," +
            "   S.dateFin as dateFin," +
            "   S.description as description," +
            "   COALESCE(C.designation, G.designation) as designationClasseGroupe," +
            "   COALESCE(C.id, G.id) as idClasseGroupe," +
            "   S.intitule as intitule," +
            "   CASE WHEN C.id IS NOT NULL THEN 'C' ELSE 'G' END as typeClasseGroupe," +
            "   E.designation as matiere, " +
            "   E.id as idMatiere )" +
            " FROM " +
            SequenceBean.class.getName() + " S " +
            " INNER JOIN S.enseignement E " + 
            " LEFT JOIN S.classe C " + 
            " LEFT JOIN S.groupe G " +  
            " WHERE " + 
            "   S.idEnseignant = :idEnseignant AND " +
            "   S.idEtablissement = :idEtablissement AND " +
            "  (S.dateDebut <= :dateFin AND S.dateFin >= :dateDebut) "; 
        
        final Query requete = getEntityManager().createQuery(query);
        requete.setParameter("idEnseignant", rechercheSequence.getIdEnseignant());
        requete.setParameter("idEtablissement", rechercheSequence.getIdEtablissement());
        requete.setParameter("dateDebut", rechercheSequence.getDateDebut());
        requete.setParameter("dateFin", rechercheSequence.getDateFin());
        listeSequenceResult = requete.getResultList();
            
        final List<SequenceDTO> listeSequenceDTO = new ArrayList<SequenceDTO>();
        final List<Integer> listeIdEnseignement = new ArrayList<Integer>();
        
        for (final Map<String, ?> result : listeSequenceResult) {
            final SequenceDTO sequenceDTO = new SequenceDTO();
            sequenceDTO.setCode((String) result.get("codeSequence"));
            sequenceDTO.setId((Integer) result.get("idSequence"));
            sequenceDTO.setDateDebut((Date) result.get("dateDebut"));
            sequenceDTO.setDateFin((Date) result.get("dateFin"));
            sequenceDTO.setDescription((String) result.get("description"));
            sequenceDTO.setIntitule((String) result.get("intitule"));
            
            //Condition dans la requête
            sequenceDTO.setIdEnseignant(rechercheSequence.getIdEnseignant());
            sequenceDTO.setIdEtablissement(rechercheSequence.getIdEtablissement());
            
            sequenceDTO.setGroupesClassesDTO(new GroupesClassesDTO(
                    (Integer) result.get("idClasseGroupe"),
                    (String) result.get("codeClasseGroupe"),
                    StringUtils.equals((String) result.get("typeClasseGroupe"),
                            "C") ? TypeGroupe.CLASSE : TypeGroupe.GROUPE,
                    (String) result.get("designationClasseGroupe")));
            
            sequenceDTO.setIdEnseignement((Integer) result.get("idMatiere"));
            
            if (!listeIdEnseignement.contains(sequenceDTO.getIdEnseignement())) {
                listeIdEnseignement.add(sequenceDTO.getIdEnseignement());
            }
            sequenceDTO.setDesignationEnseignement((String) result.get("matiere"));
            listeSequenceDTO.add(sequenceDTO);
        }
        
        // Met a jour les libelle des matieres qui peuvent avoir ete redéfinis par l'admin pour cet etablissement
        if (!CollectionUtils.isEmpty(listeSequenceDTO)) {
            final Map<Integer,String> listeLibelleMatiere = findLibelleEnseignement(
                    rechercheSequence.getIdEtablissement(), 
                    listeIdEnseignement);
            for (final SequenceDTO sequence:listeSequenceDTO) {
                final String matiere =  listeLibelleMatiere.get(sequence.getIdEnseignement());
                if (!StringUtils.isEmpty(matiere)) {
                    sequence.setDesignationEnseignement(matiere);
                }
            }
        }
        return listeSequenceDTO;
    }
    
    
    
}
