/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.remplacement.RechercheRemplacementQO;
import org.crlr.dto.application.remplacement.RemplacementDTO;
import org.crlr.metier.entity.EnseignantBean;
import org.crlr.metier.entity.EtablissementsEnseignantsBean;
import org.crlr.metier.entity.RemplacementBean;
import org.crlr.metier.entity.SeanceBean;
import org.crlr.utils.Assert;
import org.crlr.utils.ObjectUtils;
import org.springframework.stereotype.Repository;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
@Repository
public class RemplacementHibernateBusiness extends AbstractBusiness
    implements RemplacementHibernateBusinessService {
    
    /**  
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<RemplacementDTO>> findListeRemplacement(final RechercheRemplacementQO rechercheRemplacementQO) {
        
        Assert.isNotNull("rechercheRemplacementQO", rechercheRemplacementQO);
        Assert.isNotNull("idEtablissement", rechercheRemplacementQO.getIdEtablissement());
        
        final ResultatDTO<List<RemplacementDTO>> resultat = new ResultatDTO<List<RemplacementDTO>>();
        final List<RemplacementDTO> listeRemplacement = new ArrayList<RemplacementDTO>(); 
        resultat.setValeurDTO(listeRemplacement);
        
        String requete =
            "SELECT new map(R as remplacement) FROM " + 
                RemplacementBean.class.getName() + " R, " +
                EtablissementsEnseignantsBean.class.getName() + " E " + 
            "WHERE " +
                "E.pk.idEnseignant = R.idEnseignantAbsent AND " +
                "E.pk.idEtablissement = :idEtablissement ";
        
        if (rechercheRemplacementQO.getIdEnseignant() != null) {
            requete += 
                " AND (R.idEnseignantRemplacant = :idEnseignant OR R.idEnseignantAbsent = :idEnseignant) ";
        }
        if (rechercheRemplacementQO.getIdEnseignantAbsent() != null) {
            requete += 
                " AND R.idEnseignantAbsent = :idEnseignantAbsent ";
        }
        if (rechercheRemplacementQO.getIdEnseignantRemplacant() != null) {
            requete += 
                " AND R.idEnseignantRemplacant = :idEnseignantRemplacant ";
        }
        if (rechercheRemplacementQO.getDateDebut() != null && rechercheRemplacementQO.getDateFin() != null) {
            requete += " AND ("; 
            requete += "(R.dateDebut <= :dateDebut AND R.dateFin >= :dateDebut) OR ";
            requete += "(R.dateDebut <= :dateFin AND R.dateFin >= :dateFin) OR ";
            requete += "(R.dateDebut > :dateDebut AND R.dateFin < :dateFin)) ";
        }
        
        if (rechercheRemplacementQO.getDate() != null) {
            requete += " AND (R.dateDebut <= :date AND R.dateFin >= :date) ";
        }
        
        final Query query = 
             getEntityManager().createQuery(requete)
             .setParameter("idEtablissement", rechercheRemplacementQO.getIdEtablissement());
        if (rechercheRemplacementQO.getIdEnseignant() != null) {
             query.setParameter("idEnseignant", rechercheRemplacementQO.getIdEnseignant());
        }
        if (rechercheRemplacementQO.getIdEnseignantAbsent() != null) {
            query.setParameter("idEnseignantAbsent", rechercheRemplacementQO.getIdEnseignantAbsent());
        }
        if (rechercheRemplacementQO.getIdEnseignantRemplacant() != null) {
            query.setParameter("idEnseignantRemplacant", rechercheRemplacementQO.getIdEnseignantRemplacant());
        }
        if (rechercheRemplacementQO.getDateDebut() != null && rechercheRemplacementQO.getDateFin() != null) {
            query.setParameter("dateDebut", rechercheRemplacementQO.getDateDebut().toDate());
            query.setParameter("dateFin", rechercheRemplacementQO.getDateFin().toDate());
        }
        
        if (rechercheRemplacementQO.getDate() != null) {
            query.setParameter("date", rechercheRemplacementQO.getDate().toDate());
        }
        
        final List<Map<String, ?>> resultatQuery = query.getResultList();   

        for (final Map<String, ?> result : resultatQuery) {
            RemplacementDTO remplacementDTO = new RemplacementDTO();
            RemplacementBean remplacementBean = (RemplacementBean) result.get("remplacement");
            
            remplacementDTO.setDateDebut(remplacementBean.getDateDebut());
            remplacementDTO.setDateFin(remplacementBean.getDateFin());
            remplacementDTO.setId(remplacementBean.getId());
            remplacementDTO.setIdEtablissement(remplacementBean.getIdEtablissement());
            
            EnseignantDTO absent = new EnseignantDTO();
            ObjectUtils.copyProperties(absent, remplacementBean.getEnseignantAbsent());
            
            EnseignantDTO remplacant = new EnseignantDTO();
            ObjectUtils.copyProperties(remplacant, remplacementBean.getEnseignantRemplacant());
            
            remplacementDTO.setEnseignantAbsent(absent);
            remplacementDTO.setEnseignantRemplacant(remplacant);
            
            listeRemplacement.add(remplacementDTO);
        }
        
        return resultat;
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveRemplacement(RemplacementDTO remplacementDTO){
        
        Assert.isNotNull("remplacementDTO", remplacementDTO);
        Assert.isNotNull("getIdEtablissement", remplacementDTO.getIdEtablissement());
        Assert.isNotNull("getDateDebut", remplacementDTO.getDateDebut());
        Assert.isNotNull("getDateFin", remplacementDTO.getDateFin());
        Assert.isNotNull("getEnseignantAbsent", remplacementDTO.getEnseignantAbsent());
        Assert.isNotNull("getEnseignantRemplacant", remplacementDTO.getEnseignantRemplacant());
        Assert.isNotNull("getEnseignantAbsent().getId", remplacementDTO.getEnseignantAbsent().getId());
        Assert.isNotNull("getEnseignantRemplacant().getId", remplacementDTO.getEnseignantRemplacant().getId());

        final ResultatDTO<Integer> resultat = new ResultatDTO<Integer>();        
        RemplacementBean remplacementBean = null;
        Integer idRemplacement = remplacementDTO.getId();
        
        if (idRemplacement != null) {
            //Modif
            remplacementBean = getEntityManager().find(RemplacementBean.class, idRemplacement);
        } else {
            //Ajoute
            remplacementBean = new RemplacementBean();
        }
        remplacementBean.setIdEtablissement(remplacementDTO.getIdEtablissement());
        remplacementBean.setDateDebut(remplacementDTO.getDateDebut());
        remplacementBean.setDateFin(remplacementDTO.getDateFin());
        remplacementBean.setIdEnseignantAbsent(remplacementDTO.getEnseignantAbsent().getId());
        remplacementBean.setIdEnseignantRemplacant(remplacementDTO.getEnseignantRemplacant().getId());
        
        //Si l'id  est null c'est qu'on est en ajout.
        if (idRemplacement == null) {
            final BaseHibernateBusiness baseHibernateBusiness = new BaseHibernateBusiness(this.getEntityManager());
            idRemplacement = baseHibernateBusiness.getIdInsertion("cahier_remplacement");
            

            remplacementBean.setId(idRemplacement);

            // Ajout à la base de données
            getEntityManager().persist(remplacementBean);
            
        }         
        getEntityManager().flush();
        resultat.setValeurDTO(remplacementBean.getId());
        return resultat;
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Boolean> deleteRemplacement(RemplacementDTO remplacementDTO) {
        Assert.isNotNull("remplacementDTO", remplacementDTO);
        Assert.isNotNull("remplacementDTO.getId", remplacementDTO.getId());

        final ResultatDTO<Boolean> resultat = new ResultatDTO<Boolean>();
        
        RemplacementBean remplacementBean = null;
        Integer idRemplacement = remplacementDTO.getId();
        remplacementBean = getEntityManager().find(RemplacementBean.class, idRemplacement);
        
        // Supprime simplement l'objet remplacement. 
        // Les seances crees dans le cadre de ce remplacement ne sont pas supprimees
        getEntityManager().remove(remplacementBean);
        resultat.setValeurDTO(true);
        return resultat;

    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<SeanceDTO>> findListeSeanceRemplacee(RechercheRemplacementQO rechercheRemplacementQO) {
        Assert.isNotNull("rechercheRemplacementQO", rechercheRemplacementQO);
        
        final ResultatDTO<List<SeanceDTO>> resultat = new ResultatDTO<List<SeanceDTO>>();
        final List<SeanceDTO> listeSeance = new ArrayList<SeanceDTO>();
        resultat.setValeurDTO(listeSeance);
        
        // Recherche les seances crees par l'enseignant remplacant pour l'enseignant absent
        String requete = 
            "SELECT new map(SEA as seance) " + 
            "FROM " +
                SeanceBean.class.getName() + " SEA " +
                "INNER JOIN SEA.sequence SEQ, " +
                RemplacementBean.class.getName() + " R " + 
            "WHERE " + 
                "R.idEnseignantRemplacant = SEA.idEnseignant AND " + 
                "R.idEnseignantAbsent = SEQ.idEnseignant AND " + 
                "R.idEtablissement = SEQ.idEtablissement AND ";
        if (rechercheRemplacementQO.getIdRemplacement() != null) {
            requete +=
                "R.id = :idRemplacement ";
        } else {
            requete +=
                "R.idEnseignantRemplacant = :idEnseignantRemplacant AND " +
                "R.idEnseignantAbsent = :idEnseignantAbsent AND " + 
                "SEQ.idEtablissement = :idEtablissement "; 
        }
        requete +=
            "ORDER BY SEA.date ASC";
        
        
        final Query query = getEntityManager().createQuery(requete);
        if (rechercheRemplacementQO.getIdRemplacement() != null) {
            query
            .setParameter("idRemplacement", rechercheRemplacementQO.getIdRemplacement());
        } else {
            query
            .setParameter("idEtablissement", rechercheRemplacementQO.getIdEtablissement())
            .setParameter("idEnseignantRemplacant", rechercheRemplacementQO.getIdEnseignantRemplacant())
            .setParameter("idEnseignantAbsent", rechercheRemplacementQO.getIdEnseignantAbsent());
        }
        final List<Map<String, ?>> resultatQuery = query.getResultList();   

        for (final Map<String, ?> result : resultatQuery) {
           SeanceDTO seanceDTO = new SeanceDTO();
           SeanceBean seanceBean = (SeanceBean) result.get("seance");
           ObjectUtils.copyProperties(seanceDTO, seanceBean);
           
           listeSeance.add(seanceDTO);
       }
        
        return resultat;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<EnseignantDTO>> findListeEnseignant(RechercheRemplacementQO rechercheRemplacementQO) {
        Assert.isNotNull("rechercheRemplacementQO", rechercheRemplacementQO);
        Assert.isNotNull("rechercheRemplacementQO.getIdEtablissement", rechercheRemplacementQO.getIdEtablissement());

        final ResultatDTO<List<EnseignantDTO>> resultat = new ResultatDTO<List<EnseignantDTO>>();
        final List<EnseignantDTO> listeEnseignant = new ArrayList<EnseignantDTO>();
        resultat.setValeurDTO(listeEnseignant);
        
        // Recherche les enseignant qui exercent dans l'etablissement
        String requete = 
            "SELECT new map(ENS as enseignant) " + 
            "FROM " +
                EnseignantBean.class.getName() + " ENS, " +
                EtablissementsEnseignantsBean.class.getName() + " ETA " + 
            "WHERE " + 
                "ETA.pk.idEnseignant = ENS.id AND " + 
                "ETA.pk.idEtablissement = :idEtablissement " +   
            "ORDER BY ENS.nom ASC, ENS.prenom ASC ";
        
        
        final Query query = getEntityManager().createQuery(requete)
            .setParameter("idEtablissement", rechercheRemplacementQO.getIdEtablissement());
        final List<Map<String, ?>> resultatQuery = query.getResultList();   

        for (final Map<String, ?> result : resultatQuery) {
           EnseignantDTO enseignantDTO = new EnseignantDTO();
           EnseignantBean enseignantBean = (EnseignantBean) result.get("enseignant");
           ObjectUtils.copyProperties(enseignantDTO, enseignantBean);
           
           listeEnseignant.add(enseignantDTO);
       }
        
        return resultat;
    }
    
    
}
