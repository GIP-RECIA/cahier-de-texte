/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignantHibernateBusiness.java,v 1.2 2009/05/26 08:06:30 ent_breyton Exp $
 */

package org.crlr.metier.business;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.exception.metier.MetierException;

import org.crlr.metier.entity.EnseignantBean;
import org.crlr.metier.entity.EnseignantsClassesBean;
import org.crlr.metier.entity.EnseignantsEnseignementsBean;
import org.crlr.metier.entity.EnseignantsGroupesBean;
import org.crlr.metier.entity.EtablissementsEnseignantsBean;

import org.springframework.stereotype.Repository;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EnseignantHibernateBusiness.
 *
 * @author vibertd
 * @version $Revision: 1.2 $
 */
@Repository
public class EnseignantHibernateBusiness extends AbstractBusiness
    implements EnseignantHibernateBusinessService {
   
    /**
    * {@inheritDoc}
    */
    @SuppressWarnings("unchecked")
    public EnseignantBean find(String uid) {
        final EnseignantBean enseignant;

        final String query =
            "SELECT e FROM " + EnseignantBean.class.getName() + " e WHERE e.uid = :uid";

        final List<EnseignantBean> liste =
            getEntityManager().createQuery(query).setParameter("uid", uid).getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            enseignant = liste.get(0);
        } else {
            enseignant = null;
        }

        return enseignant;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> exist(String uid) {
        final Map<String, Object> map = new HashMap<String, Object>();        

        final String query =
            "SELECT e.id, e.depotStockage FROM " + EnseignantBean.class.getName() +
            " e WHERE e.uid = :uid";

        final List<Object[]> liste =
            getEntityManager().createQuery(query).setParameter("uid", uid).getResultList();

        if (!CollectionUtils.isEmpty(liste)) { 
            final Object[] res = liste.get(0);
            map.put("id", res[0]);
            map.put("depot", res[1]);
        }

        return map;
    }

    /**
     * {@inheritDoc}
     */
    public Integer save(EnseignantBean enseignant)
                 throws MetierException {
        getEntityManager().persist(enseignant);
        getEntityManager().flush();

        return enseignant.getId();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public int findIdEnseignantParEtabNomPrenom(int idEtab, String nom, String prenom){
        List<Integer> idEnseignant = new ArrayList<Integer>();
        final String requete = "SELECT E.id FROM " + EtablissementsEnseignantsBean.class.getName()+" F " 
        + " INNER JOIN F.enseignant E " 
        + " WHERE E.nom = :nom AND E.prenom = :prenom AND F.pk.idEtablissement = :idEtab";
        idEnseignant = getEntityManager().createQuery(requete).setParameter("nom", nom).setParameter("prenom", prenom)
        .setParameter("idEtab", idEtab).getResultList();
        if(! CollectionUtils.isEmpty(idEnseignant)){
            return idEnseignant.get(0);
        }else{
            return 0;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean verifieAutorisationEnseignement(Integer idEtab, Integer idEns, Integer idMatiere){
        final String requete = "SELECT count(*) FROM " + EnseignantsEnseignementsBean.class.getName()+" E " +
        " WHERE  E.pk.idEtablissement = :idEtab AND E.pk.idEnseignant = :idEns AND E.enseignement.id = :idMatiere";
        final Integer i = Integer.valueOf(getEntityManager().createQuery(requete).setParameter("idEns", idEns).setParameter("idMatiere", idMatiere)
                .setParameter("idEtab", idEtab).getSingleResult().toString()) ;
        return i > 0;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean verifieEnseignantClasse(Integer idEns, Integer idClasse){
        final String requete = "SELECT count(*) FROM " + EnseignantsClassesBean.class.getName() + " E " +
        " WHERE E.pk.idEnseignant = :idEns AND E.pk.idClasse = :idClasse";
        final Integer i = Integer.valueOf(getEntityManager().createQuery(requete).setParameter("idEns", idEns)
        .setParameter("idClasse", idClasse).getSingleResult().toString()) ;
        return i > 0;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean verifieEnseignantGroupe(Integer idEns, Integer idGroupe){
        final String requete = "SELECT count(*) FROM " + EnseignantsGroupesBean.class.getName() + " E " +
        " WHERE E.pk.idEnseignant = :idEns AND E.pk.idGroupe = :idGroupe";
        final Integer i = Integer.valueOf(getEntityManager().createQuery(requete).setParameter("idEns", idEns)
        .setParameter("idGroupe", idGroupe).getSingleResult().toString()) ;
        return i > 0;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<EnseignantDTO> findEnseignantsByEtab(Integer idEtab){
      
        final String requete = "SELECT E FROM " + EtablissementsEnseignantsBean.class.getName()+" F " 
        + " INNER JOIN F.enseignant E " 
        + " WHERE F.pk.idEtablissement = :idEtab " +
        " order by e.nom, e.prenom ";
        
        List<EnseignantBean> enseignants =  getEntityManager().createQuery(requete).setParameter("idEtab", idEtab).getResultList();
        if (enseignants != null) {
        	
        	List<EnseignantDTO> dtoList = new ArrayList<EnseignantDTO>(enseignants.size());
        	
        	for (EnseignantBean ens : enseignants) {
				if (ens != null) {
					EnseignantDTO ensDTO = new EnseignantDTO(ens.getCivilite(), ens.getPrenom(), ens.getNom());
					ensDTO.setUid(ens.getUid());
					ensDTO.setId(ens.getId());
					dtoList.add(ensDTO);
				}
			}
        	return dtoList;
        }
        return null;
    }
    
}
