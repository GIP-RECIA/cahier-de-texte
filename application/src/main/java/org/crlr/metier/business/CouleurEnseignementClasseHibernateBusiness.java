/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: CouleurEnseignementClasseHibernateBusiness.java,v 1.2 2009/05/26 08:06:30 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.CouleurEnseignementClasseDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.sequence.RechercheCouleurEnseignementClasseQO;
import org.crlr.dto.application.sequence.SaveCouleurEnseignementClasseQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.metier.entity.CouleurEnseignementClasseBean;
import org.crlr.utils.Assert;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
/**
 * CouleurEnseignementClasseHibernateBusiness.
 *
 * @author vibertd
 * @version $Revision: 1.2 $
 */
@Repository
public class CouleurEnseignementClasseHibernateBusiness extends AbstractBusiness
    implements CouleurEnseignementClasseHibernateBusinessService {
   
	protected final Log log = LogFactory.getLog(getClass());
	
    /**
    * {@inheritDoc}
    */
	@Deprecated
    @SuppressWarnings("unchecked")
    public Integer findId(Integer idEnseignant, Integer idEtablissement, Integer idEnseignement, Integer idGroupe, Integer idClasse) {
        String queryParams =
            "SELECT cec.id FROM " + CouleurEnseignementClasseBean.class.getName() + " cec"
            + " WHERE cec.idEnseignant = :id_enseignant"
            + " AND cec.idEtablissement = :id_etablissement"
            + " AND cec.idEnseignement = :id_enseignement";
        
        // Si le groupe et / ou la classe sont definis
        // Ajout des clauses WHERE
        if(idGroupe != null) {
        	queryParams += " AND cec.idGroupe = :id_groupe";
        }
        if(idClasse != null) {
        	queryParams += " AND cec.idClasse = :id_classe";
        }

		Query query = getEntityManager().createQuery(queryParams)
			.setParameter("id_enseignant", idEnseignant)
        	.setParameter("id_etablissement", idEtablissement)
        	.setParameter("id_enseignement", idEnseignement);
        
		// Si le groupe et / ou la classe sont definis
        if(idGroupe != null) {
        	query = query.setParameter("id_groupe", idGroupe);
        }
        if(idClasse!= null) {
        	query = query.setParameter("id_classe", idClasse);
        }
		
		final List<Integer> liste = query.getResultList();

		Integer id =null;
        if (!CollectionUtils.isEmpty(liste)) {
            id = liste.get(0);
            log.debug("Id de la relation CouleurEnseignementClasse : " + id);
        }

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public boolean exist(Integer idEnseignant, Integer idEtablissement, Integer idEnseignement, Integer idGroupe, Integer idClasse) {
        
        Integer id = findId(idEnseignant, idEtablissement, idEnseignement, idGroupe, idClasse);

		boolean exist = (id != null ? true : false);

        if(exist) {
        	log.debug("L'association (id : " + id + ", idEnseignant : " + idEnseignant + ", idEtablissement : " 
        			+ idEtablissement + ", idEnseignement : " + idEnseignement 
        			+ ", idGroupe : " + idGroupe + ", idClasse : " + idClasse + ") existe deja" );
        } else {
        	log.debug("L'association (id : " + id + ",idEnseignant : " + idEnseignant + ", idEtablissement : " 
        			+ idEtablissement + ", idEnseignement : " + idEnseignement 
        			+ ", idGroupe : " + idGroupe + ", idClasse : " + idClasse + ") n'existe pas" );
        }
        return exist;
    }

    /**
     * Recupere un unique Co... correspondant au critères.
     * @param idEnseignant
     * @param idEtablissement
     * @param idEnseignement
     * @param idGroupe
     * @param idClasse
     * @return CouleurEnseignementClasseBean , null si non trouvé
     */
    private CouleurEnseignementClasseBean find(Integer idEnseignant, Integer idEtablissement, Integer idEnseignement, Integer idGroupe, Integer idClasse) {
    	String queryText =
                "SELECT c FROM " + CouleurEnseignementClasseBean.class.getName() + " c"
                + " WHERE c.idEnseignant = :id_enseignant"
                + " AND c.idEtablissement = :id_etablissement"
                + " AND c.idEnseignement = :id_enseignement"
                + " AND ( c.idGroupe = :id_groupe OR c.idClasse = :id_classe ) ";
    	
    	Query query = getEntityManager().createQuery(queryText)
    					.setParameter("id_enseignant", idEnseignant)
    					.setParameter("id_etablissement", idEtablissement)
    					.setParameter("id_enseignement", idEnseignement)
    					.setParameter("id_groupe", idGroupe != null ? idGroupe : 0)
    					.setParameter("id_classe", idClasse != null ? idClasse : 0);
    	try {
    		return (CouleurEnseignementClasseBean) query.getSingleResult();
    	} catch (NoResultException e ) {
    		return null;
    	} catch (RuntimeException e) {
    		log.error("DAO ERROR in CouleurEnseignementClasseBean {} " , e);
    		throw e;
    	}
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void save(SaveCouleurEnseignementClasseQO scecQO) throws MetierException {
        
    	// Recuperation des informations
    	Integer idEnseignant = scecQO.getIdEnseignant();
    	Integer idEtablissement = scecQO.getIdEtablissement();
    	Integer idEnseignement = scecQO.getIdEnseignement();
    	GroupesClassesDTO gcDTO = scecQO.getClasseGroupe();
    	Integer idGroupe =null;
    	Integer idClasse = null;
    	
    	if (idEnseignant == null || idEtablissement == null || idEnseignement == null || gcDTO == null) {
    		log.error("id NUll : enseignant={}, etablissement={}, enseignement={}, goupeClasse={}",  
    				idEnseignant,  idEtablissement, idEnseignement, gcDTO);
    		return ;
    	}
    	String couleur = scecQO.getTypeCouleur().getId();
    
    	
    	if (!StringUtils.isEmpty(couleur)) {
	        if(gcDTO.getVraiOuFauxClasse()) {
	            idClasse = scecQO.getIdClasseGroupe();
	        } else {
	            idGroupe = scecQO.getIdClasseGroupe();
	        }
	        
	        // Recherche d'un association existante
	        CouleurEnseignementClasseBean cecb = find(idEnseignant, idEtablissement, idEnseignement, idGroupe, idClasse);
	    	
	    	// Si l'association existe deja
	    	if(cecb != null) {
	    		if (!couleur.equals(cecb.getCouleur())) {
	    			cecb.setCouleur(couleur);
	    		//	cecb = etEntityManager().merge(cecb);
	    			log.debug("Mise a jour de l'association");
	    		}
	    	} else {
	    		cecb = new CouleurEnseignementClasseBean();
	            cecb.setIdEnseignant(idEnseignant);
	            cecb.setIdEtablissement(idEtablissement);
	            cecb.setIdEnseignement(idEnseignement);
	            cecb.setIdClasse(idClasse);
	            cecb.setIdGroupe(idGroupe);
	            cecb.setCouleur(couleur);
	            final BaseHibernateBusiness baseHibernateBusiness = new BaseHibernateBusiness(this.getEntityManager());
	            Integer id = baseHibernateBusiness.getIdInsertion("cahier_couleur_enseignement");
	            cecb.setId(id);
	            getEntityManager().persist(cecb);
	          
	            log.debug("Insertion de l'association");
	    	}
	        
	    /*	// Si la mise a jour est necessaire
	    	if(!exist) {
	    		log.debug("Insertion de l'association");
	    		
	    	} else {
	    		log.debug("Mise a jour de l'association");
	    		cecb = getEntityManager().find(CouleurEnseignementClasseBean.class, idCouleurEnseignementClasse);
	    		cecb.setCouleur(couleur);
	    		
	    	}
	    */
	        getEntityManager().flush();
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<CouleurEnseignementClasseDTO> findCouleurEnseignementClasse(RechercheCouleurEnseignementClasseQO rcecQO) {
        Assert.isNotNull("rechercheCouleurEnseignementClasseQO", rcecQO);
        
        final ResultatDTO<CouleurEnseignementClasseDTO> resultat = new ResultatDTO<CouleurEnseignementClasseDTO>();
        CouleurEnseignementClasseDTO couleurEnseignementClasseDTO = new CouleurEnseignementClasseDTO();

        
        final Integer idEnseignant = rcecQO.getIdEnseignant();
        final Integer idEtablissement = rcecQO.getIdEtablissement();
        final Integer idEnseignement = rcecQO.getIdEnseignement();
        final GroupesClassesDTO cgDTO = rcecQO.getGroupeClasse();
        
         Integer idGroupe = null;
         Integer idClasse = null;
         if (cgDTO.getVraiOuFauxClasse()) {
         	idClasse = cgDTO.getId();
         } else {
        	 idGroupe = cgDTO.getId();
         }
         
        CouleurEnseignementClasseBean cecb = find(idEnseignant, idEtablissement, idEnseignement, idGroupe, idClasse);
        		
        couleurEnseignementClasseDTO.setIdEnseignant(idEnseignant); 
        couleurEnseignementClasseDTO.setIdEnseignement(idEnseignement);
        couleurEnseignementClasseDTO.setIdEtablissement(idEtablissement);
        couleurEnseignementClasseDTO.setGroupesClassesDTO(cgDTO);
        
        if (cecb != null) {
        	couleurEnseignementClasseDTO.setCouleur(cecb.getCouleur());
        } /*
        else {
        	couleurEnseignementClasseDTO.setCouleur(TypeCouleur.Blanc.getId());
        }
        */
       
        resultat.setValeurDTO(couleurEnseignementClasseDTO);

        return resultat;
    }

}
