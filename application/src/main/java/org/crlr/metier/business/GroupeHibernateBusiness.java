/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GroupeHibernateBusiness.java,v 1.29 2010/06/08 10:51:06 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.EnseignantsClasseGroupeQO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.RechercheGroupeClassePopupQO;
import org.crlr.dto.application.base.RechercheGroupeQO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.entity.CycleGroupeBean;
import org.crlr.metier.entity.ElevesGroupesBean;
import org.crlr.metier.entity.EnseignantsEnseignementsBean;
import org.crlr.metier.entity.EnseignantsGroupesBean;
import org.crlr.metier.entity.GroupeBean;
import org.crlr.metier.entity.GroupesClassesBean;
import org.crlr.metier.entity.OuvertureInspecteurBean;
import org.crlr.metier.utils.SchemaUtils;
import org.crlr.utils.Assert;
import org.crlr.utils.ComparateurUtils;
import org.crlr.utils.ObjectUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;



/**
 * GroupeHibernateBusiness.
 *
 * @author vibertd
 * @version $Revision: 1.29 $
 */
@Repository
public class GroupeHibernateBusiness extends AbstractBusiness
    implements GroupeHibernateBusinessService {
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public GroupeBean find(Integer id) {
        Assert.isNotNull("id", id);
        final GroupeBean groupe;

        final String query =
            "SELECT g FROM " + GroupeBean.class.getName() + " g WHERE g.id = :id";

        final List<GroupeBean> liste =
            getEntityManager().createQuery(query).setParameter("id", id).getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            groupe = liste.get(0);
        } else {
            groupe = null;
        }

        return groupe;
    }

    /**
     * {@inheritDoc}
     */
    public Integer save(GroupeBean groupe) throws MetierException {
        getEntityManager().persist(groupe);
        getEntityManager().flush();

        return groupe.getId();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<GroupesClassesDTO>> findGroupePopup(RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO)
        throws MetierException {
        final ResultatDTO<List<GroupesClassesDTO>> listeGroupePopup =
            new ResultatDTO<List<GroupesClassesDTO>>();

        Assert.isNotNull("rechercheGroupeClassePopupQO", rechercheGroupeClassePopupQO);

        final Integer idInspecteur = rechercheGroupeClassePopupQO.getIdInspecteur();
        final Integer idEnseignant = rechercheGroupeClassePopupQO.getIdEnseignant();
        final Integer idEtablissement = rechercheGroupeClassePopupQO.getIdEtablissement();
         Integer idAnneeScolaire = rechercheGroupeClassePopupQO.getIdAnneeScolaire();

        final String exercice = rechercheGroupeClassePopupQO.getExerciceScolaire();
        boolean inArchive = false;
        
        
        
        final String schema = 
            SchemaUtils.getSchemaCourantOuArchive(rechercheGroupeClassePopupQO.getArchive(), 
                    exercice);        
        
        if (!StringUtils.isBlank(exercice)) {
        	idAnneeScolaire = AnneeScolaireDTO.id(exercice);
        	inArchive = true;
        }
        
        
        String requete = "";
        
        if (idInspecteur != null){
            requete =
                "SELECT DISTINCT G.id, G.code, G.designation " + 
                " FROM " + SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant_groupe")
                    + " EG INNER JOIN " + SchemaUtils.getTableAvecSchema(schema, "cahier_groupe") + " G ON (G.id = EG.id_groupe)," +
                    SchemaUtils.getTableAvecSchema(schema, "cahier_ouverture_inspecteur") + " OUV " +
                " WHERE " +
                    " OUV.id_etablissement =? AND" +
                    " OUV.id_inspecteur =? AND" +
                    " G.id_etablissement =? AND " +
                    " G.id_annee_scolaire =? AND " +
                    " EG.id_enseignant = OUV.id_enseignant ";
            if (idEnseignant != null) {
                requete += 
                    " AND EG.id_enseignant = ? ";
            }
            requete += 
                    " ORDER BY G.id ASC";
        } else if(idEnseignant != null) {
            requete =
                "SELECT DISTINCT G.id, G.code, G.designation " + 
                " FROM " + 
                SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant_groupe")
                + " EG INNER JOIN " + SchemaUtils.getTableAvecSchema(schema, "cahier_groupe") + 
                " G ON (G.id=EG.id_groupe)" +
                " WHERE " +
                    " G.id_etablissement =? AND " +
                    " G.id_annee_scolaire =? AND " +
                    " EG.id_enseignant =? " +
                    " ORDER BY G.id ASC";
        } else {
            requete =
                "SELECT DISTINCT G.id, G.code, G.designation " + 
                " FROM " + 
                    SchemaUtils.getTableAvecSchema(schema, "cahier_groupe") + " G " +
                " WHERE " +
                    " G.id_etablissement =? " +
                    " ORDER BY G.id ASC";
        }
        
        final Query query = getEntityManager().createNativeQuery(requete);
        
        if (idInspecteur != null){
            query.setParameter(1, idEtablissement);
            query.setParameter(2, idInspecteur);
            query.setParameter(3, idEtablissement);
            query.setParameter(4, idAnneeScolaire);
            if (idEnseignant != null) {
                query.setParameter(5, idEnseignant);
            }
        }else if(idEnseignant != null) {
            query.setParameter(1, idEtablissement);
            query.setParameter(2, idAnneeScolaire);
            query.setParameter(3, idEnseignant);
        } else {
            query.setParameter(1, idEtablissement);
        }
        
        final List<Object[]> liste = query.getResultList();

        final List<GroupesClassesDTO> resultat = new ArrayList<GroupesClassesDTO>();

        for (final Object[] groupe : liste) {
            final GroupesClassesDTO groupeDTO = new GroupesClassesDTO();
            groupeDTO.setId((Integer)groupe[0]);
            groupeDTO.setCode((String)groupe[1]);
            groupeDTO.setIntitule((String)groupe[2]);
            groupeDTO.setTypeGroupe(TypeGroupe.GROUPE);
            resultat.add(groupeDTO);
        }
        listeGroupePopup.setValeurDTO(ComparateurUtils.sort(resultat, "intitule"));

        return listeGroupePopup;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer exist(Integer id) throws MetierException {
        Assert.isNotNull("id", id);
        final Integer idGroupe;

        final String query =
            "SELECT g.id FROM " + GroupeBean.class.getName() + " g WHERE g.id = :id";

        final List<Integer> liste =
            getEntityManager().createQuery(query).setParameter("id", id).getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            idGroupe = liste.get(0);
        } else {
            idGroupe = null;
        }

        return idGroupe;
    }

    /**
     * {@inheritDoc}
     */
    public GroupesClassesDTO findGroupe(Integer idGroupe) {
        final GroupeBean groupeBean = this.find(idGroupe);
        if (groupeBean == null) {
            return null;
        }
        
        final GroupesClassesDTO groupesClassesDTO = new GroupesClassesDTO();

        ObjectUtils.copyProperties(groupesClassesDTO, groupeBean);
        
        groupesClassesDTO.setIntitule(groupeBean.getDesignation());
        groupesClassesDTO.setTypeGroupe(TypeGroupe.GROUPE);

        return groupesClassesDTO;
   
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer findByCode(final String code, final Boolean archive, final String exercice) throws MetierException {
        Assert.isNotNull("code", code);
        
        final Integer id;
        
        final String schema = 
            SchemaUtils.getSchemaCourantOuArchive(archive, exercice);         

        final String query =
            "SELECT g.id FROM " + SchemaUtils.getTableAvecSchema(schema, "cahier_groupe") + 
            " g WHERE g.code =?";

        final List<Integer> liste =
            getEntityManager().createNativeQuery(query).setParameter(1, code)
                .getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            id = liste.get(0);
        } else {
            id = null;
        }

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean checkDroitGroupe(final Integer idEnseignant, 
            final Integer idGroupe, final Boolean archive, final String exercice)
                             throws MetierException {
        boolean verif = false;
        
        final String schema = 
            SchemaUtils.getSchemaCourantOuArchive(archive, exercice);         

        final String query =
            "SELECT 1 FROM " + 
            SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant_groupe") + 
            " EG WHERE EG.id_enseignant =? AND " +
            " EG.id_groupe =?";

        final List<Integer> listeEnseignantsGroupes=
            getEntityManager().createNativeQuery(query)
                .setParameter(1, idEnseignant)
                .setParameter(2, idGroupe).getResultList();

        if (listeEnseignantsGroupes.size() > 0) {
            verif = true;
        }

        return verif;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean checkDroitGroupeInspecteur(final Integer idInspecteur, 
            final Integer idGroupe, final Boolean archive, final String exercice, final Integer idEtablissement)
                             throws MetierException {
        boolean verif = false;
        
        final String schema = 
            SchemaUtils.getSchemaCourantOuArchive(archive, exercice);         

        final String query =
            "SELECT 1 FROM " + 
            SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant_groupe") + " EG, " + 
            SchemaUtils.getTableAvecSchema(schema, "cahier_ouverture_inspecteur") + " OUV " +
            "WHERE EG.id_enseignant =OUV.id_enseignant AND " +
            " EG.id_groupe =? AND" +
            " OUV.id_inspecteur = ? AND " +
            " OUV.id_etablissement = ?";

        final List<Integer> listeEnseignantsGroupes=
            getEntityManager().createNativeQuery(query)
                .setParameter(1, idGroupe)
                .setParameter(2, idInspecteur).setParameter(3, idEtablissement)
                .getResultList();

        if (listeEnseignantsGroupes.size() > 0) {
            verif = true;
        }

        return verif;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean checkDroitGroupeDirection(final Integer idEtablissement, 
            final Integer idGroupe, final Boolean archive, final String exercice)
                             throws MetierException {
        boolean verif = false;
        
        final String schema = 
            SchemaUtils.getSchemaCourantOuArchive(archive, exercice);         

        final String query =
            "SELECT 1 FROM " + 
            SchemaUtils.getTableAvecSchema(schema, "cahier_groupe") + 
            " G WHERE G.id_etablissement =? AND " +
            " G.id =?";

        final List<Integer> listeEnseignantsGroupes=
            getEntityManager().createNativeQuery(query)
                .setParameter(1, idEtablissement)
                .setParameter(2, idGroupe).getResultList();

        if (listeEnseignantsGroupes.size() > 0) {
            verif = true;
        }

        return verif;
    }

    /**
     * {@inheritDoc}
     */
    public List<GroupeBean> findGroupesEleve(Integer idEleve)
                                      throws MetierException {
        final List<GroupeBean> listeGroupeBean = new ArrayList<GroupeBean>();       

        for (final Integer result : getIdGroupesEleve(idEleve)) {
            final GroupeBean groupeBean = new GroupeBean();
            groupeBean.setId(result);
            listeGroupeBean.add(groupeBean);
        }

        return listeGroupeBean;
    }
    
    /**
     * {@inheritDoc}
     */
    public Set<Integer> findIdGroupesEleve(final Integer idEleve) {
        Assert.isNotNull("idEleve", idEleve);
        return new HashSet<Integer> (getIdGroupesEleve(idEleve));
    }
    
    /**
     * Recherche des id de groupes d'un élève.
     * @param idEleve l'id de l'élève.
     * @return les id de groupe.
     */
    @SuppressWarnings("unchecked")
    private List<Integer> getIdGroupesEleve(final Integer idEleve) {
        final String requete =
            "SELECT EG.pk.idGroupe FROM " +
            ElevesGroupesBean.class.getName() + 
            " EG WHERE EG.pk.idEleve = :idEleve";

        final Query query = getEntityManager().createQuery(requete);
        return
            query.setParameter("idEleve", idEleve).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<GroupeDTO> findGroupeByClasse(RechercheGroupeQO rechercheGroupeQO)
                                       throws MetierException {
        final List<GroupeDTO> listeGroupeDTO = new ArrayList<GroupeDTO>();
        final Integer idClasse = rechercheGroupeQO.getIdClasse();
        
        final List<Map<String, ?>> resultatQuery = new ArrayList<Map<String,?>>();
        String requete = "";

        requete =
            " SELECT " +
            " new map( " +
            " G.id as idGroupe, " +
            " G.code as codeGroupe, " +
            " G.designation as intituleGroupe," +
            " G.groupeCollaboratif as groupeCollaboratif" +
            " )" +
            " FROM " + 
            GroupesClassesBean.class.getName() + " GC " +
            " INNER JOIN GC.groupe G " + 
            " WHERE " +
            " GC.pk.idClasse = :idClasse " +
            " ORDER BY G.id ASC";

        final Query query = getEntityManager().createQuery(requete);
        resultatQuery.addAll(query.setParameter("idClasse", idClasse).getResultList());

        for (Map<String, ?> result : resultatQuery) {
            final GroupeDTO groupeDTO = new GroupeDTO();
            groupeDTO.setId((Integer) result.get("idGroupe"));
            groupeDTO.setCode((String) result.get("codeGroupe"));
            groupeDTO.setIntitule((String) result.get("intituleGroupe"));
            groupeDTO.setGroupeCollaboratif((Boolean) result.get("groupeCollaboratif"));
            listeGroupeDTO.add(groupeDTO);
        }

        return listeGroupeDTO;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<EnseignantDTO> findEnseignantsGroupe(
            final EnseignantsClasseGroupeQO enseignantsClasseGroupeQO) {
        Assert.isNotNull("enseignantsClasseGroupeQO", enseignantsClasseGroupeQO);
        //Assert.isNotNull("etablissement", enseignantsClasseGroupeQO.getIdEtablissement());
        
        final Integer idGroupe = enseignantsClasseGroupeQO.getIdGroupeClasse();
        Assert.isNotNull("idGroupe", idGroupe);
        
        final List<EnseignantDTO> listeEnseignant = new ArrayList<EnseignantDTO>();
        
        final Integer idInspecteur = enseignantsClasseGroupeQO.getIdInspecteur();
       
        final String requete ;
        if (idInspecteur != null){
            requete = "SELECT " +
            " new map(EG.pk.idEnseignant as idEnseignant, E.civilite as civ, E.nom as nom, E.prenom as prenom, E.uid as uid) " +
            " FROM " + EnseignantsGroupesBean.class.getName() +
            " EG INNER JOIN EG.enseignant E , " +
            OuvertureInspecteurBean.class.getName() + " OUV " +
            " WHERE " +
            " OUV.inspecteur.id = :idInspecteur AND " +
            " OUV.etablissement.id = :idEtablissement AND " +
            " OUV.enseignant.id = E.id AND " +
            " EG.pk.idGroupe = :idGroupe";
        } else {
            requete = "SELECT " +
                " new map(EG.pk.idEnseignant as idEnseignant, E.civilite as civ, E.nom as nom, E.prenom as prenom, E.uid as uid) " +
            " FROM " +
                EnseignantsGroupesBean.class.getName() + " EG INNER JOIN EG.enseignant E " +
            " WHERE " +
                " EG.pk.idGroupe = :idGroupe " +
            " ORDER BY E.nom, E.prenom";
        }
            
        final Query queryEnseignant = getEntityManager().createQuery(requete).setParameter("idGroupe", idGroupe);
        if (idInspecteur != null){
            queryEnseignant.setParameter("idInspecteur", enseignantsClasseGroupeQO.getIdInspecteur());
            queryEnseignant.setParameter("idEtablissement", enseignantsClasseGroupeQO.getIdEtablissement());
        }
        
        final List<Map<String, ?>> resultatQuery =
            queryEnseignant.getResultList();
        
        //recherche des enseignants de l'enseignement si besoin.
        final Integer idEnseignement = (Integer) enseignantsClasseGroupeQO.getIdEnseignement();
        final Boolean checkEnseignant;
        final List<Integer> listeIdEnseignant; 
        
        if (idEnseignement != null) {
            final String query = "SELECT ee.pk.idEnseignant FROM " + EnseignantsEnseignementsBean.class.getName() + 
            " ee WHERE ee.pk.idEtablissement=:idEtab AND ee.pk.idEnseignement=:idEnseignement";
            
            listeIdEnseignant = 
                getEntityManager().createQuery(query).setParameter("idEtab", enseignantsClasseGroupeQO.getIdEtablissement())
                .setParameter("idEnseignement", idEnseignement).getResultList();
            
            checkEnseignant = false;
        } else {
            listeIdEnseignant = new ArrayList<Integer>();
            checkEnseignant = true;
        }
        
        for(Map<String, ?> result : resultatQuery) {
            final Integer idEnseignant = (Integer) result.get("idEnseignant");

            if (checkEnseignant || listeIdEnseignant.contains(idEnseignant)) {

                final EnseignantDTO enseignantDTO = new EnseignantDTO();
                
                enseignantDTO.setId(idEnseignant);
                enseignantDTO.setCivilite(StringUtils.trimToEmpty((String) result.get("civ")));
                enseignantDTO.setNom(StringUtils.trimToEmpty((String) result.get("nom")));
                enseignantDTO.setPrenom(StringUtils.trimToEmpty((String) result.get("prenom")));
                enseignantDTO.setUid(StringUtils.trimToEmpty((String) result.get("uid")));

                
                

                listeEnseignant.add(enseignantDTO);
            }
        }
        
        return listeEnseignant;
    }

    /**
     * {@inheritDoc}
     */
    public List<GroupeDTO> findGroupes(Set<Integer> idsGroupe) {
        final List<GroupeDTO> groupesDTO = new ArrayList<GroupeDTO>();
        for (Integer idGroupe : idsGroupe){
            final GroupeBean groupeBean = this.find(idGroupe);
            final GroupeDTO groupeDTO = new GroupeDTO();
            groupeDTO.setCode(groupeBean.getCode());
            groupeDTO.setId(groupeBean.getId());
            groupeDTO.setIntitule(groupeBean.getDesignation());
            groupeDTO.setGroupeCollaboratif(groupeBean.getGroupeCollaboratif());
            groupesDTO.add(groupeDTO);
        }
        return groupesDTO;
    }   
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public int findIdGroupeByCodeAndEtab(String codeGroupe, int idEtab){
        List<Integer> idGroupe = new ArrayList<Integer>();
        final String requete = "SELECT id FROM " + GroupeBean.class.getName() + " WHERE designation = :codeGroupe " +
        " AND id_etablissement = :idEtab";
        idGroupe = getEntityManager().createQuery(requete).
        setParameter("codeGroupe", codeGroupe).setParameter("idEtab", idEtab).getResultList();
        if(! CollectionUtils.isEmpty(idGroupe)){
            return idGroupe.get(0);
        }else{
            return 0;
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Integer> findListeGroupePourEtab(Integer idEtab){
        final String requete = "SELECT id FROM " + GroupeBean.class.getName() + " WHERE id_etablissement = :idEtab";
        final List<Integer> lesIdGroupe = getEntityManager().createQuery(requete)
            .setParameter("idEtab", idEtab).getResultList();
        return lesIdGroupe;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<GroupeDTO> findGroupesCollaboratifEnseignant(Integer idEnseignant) {
        Assert.isNotNull("idEnseignant", idEnseignant);
        final List<GroupeDTO> listeGroupe = new ArrayList<GroupeDTO>();
        final String query =
                " SELECT G " +
                " FROM " + 
                EnseignantsGroupesBean.class.getName() + " EG " +
                " INNER JOIN EG.groupe G " + 
                " WHERE " +
                " EG.pk.idEnseignant = :idEnseignant and (G.groupeCollaboratif = true)" +
                " ORDER BY G.designation ASC";

        final List<GroupeBean> liste =
            getEntityManager().createQuery(query).setParameter("idEnseignant", idEnseignant).getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            for (final GroupeBean groupe : liste) {
                final GroupeDTO groupeDTO = new GroupeDTO();
                ObjectUtils.copyProperties(groupeDTO, groupe);
                groupeDTO.setIntitule(groupe.getDesignation());
                listeGroupe.add(groupeDTO);
            }
            
        }
        return listeGroupe;        
    }
    
    @Override
    public GroupeDTO creerGroupeCollaboratifLocal(String code, String  nom, Integer idEtab, Integer idAnnee) {
    	
    	String queryText = 	"insert into cahier_courant.cahier_groupe " +
    						"(id, code, designation, id_etablissement, id_annee_scolaire, groupe_collaboratif, groupe_collaboratif_local) values "+
    						" ( nextval('cahier_groupe'), :code, :nom, :idEtab, :idAnnee, true, true)" +
    						" returning id";
    	
    	Query query = getEntityManager().createNativeQuery(queryText);
    	query.setParameter("code", code);
    	query.setParameter("nom", nom);
    	query.setParameter("idEtab", idEtab);
    	query.setParameter("idAnnee", idAnnee);
    	
     	@SuppressWarnings("unchecked")
		List<Object> tab = query.getResultList();
     	
     	
     	if (tab != null && tab.size() == 1) {
     		Integer id = (Integer) tab.get(0);
     		GroupeDTO  res = new GroupeDTO();
     		res.setId(id);
     		res.setCode(code);
     		res.setIntitule(nom);
     		res.setGroupeCollaboratif(true);
     		res.setGroupeCollaboratifLocal(true);
     		return res;
     	}
    	
    	return null;	
    						
    }
    
    @Override
    public void addEnseignantInGroupeCollaboratifLocal(GroupeDTO groupe , Integer idEnseignant) throws MetierException{
    	if (groupe.getGroupeCollaboratif() && groupe.getGroupeCollaboratifLocal()) {
    	
    		final EnseignantsGroupesBean enseignantsGroupesBean = new EnseignantsGroupesBean();
            	enseignantsGroupesBean.setIdGroupe(groupe.getId());
            	enseignantsGroupesBean.setIdEnseignant(idEnseignant);
            	getEntityManager().persist(enseignantsGroupesBean);
    	} else {
    		throw new MetierException("Le groupe n'est pas du type collaboratif local {0}", groupe.getCode());
    	}
    }
    
    @Override
    public void delEnseignantInGroupeCollaboratifLocal(GroupeDTO groupe ,Integer idEnseignant) throws MetierException {
    	if (groupe.getGroupeCollaboratif() && groupe.getGroupeCollaboratifLocal()) {
    		String querytext =  "delete from " +  EnseignantsGroupesBean.class.getName() + " eg " +
    							" where eg.pk.idEnseignant = :idEns " +
    							" and eg.pk.idGroupe = :idGrp ";
    		Integer idGroupe = groupe.getId();
    		try {
    			
	    		Query query = getEntityManager().createQuery(querytext);
	    		query.setParameter("idEns", idEnseignant);
	    		query.setParameter("idGrp", idGroupe);
    		
    		
				query.executeUpdate();
			} catch (Exception e) {
			//	log.error("sql error : {}", e.getMessage());
				throw new MetierException("La suppression de '{0}' du  groupe '{1}' a echoué" , idEnseignant, idGroupe);
			}
    	}
    }
    
    @Override
    public void deleteGroupeCollaboratifLocal(GroupeDTO groupe ) throws MetierException {
    	if (groupe.getGroupeCollaboratif() && groupe.getGroupeCollaboratifLocal()) {
    		
    		
    	
    		
    		String queryDelEns =  "delete from " +  EnseignantsGroupesBean.class.getName() + " eg " +
								" where  eg.pk.idGroupe = :idGrp ";
    		String queryDelCycle = 	"delete from " + CycleGroupeBean.class.getName() + " c " +
									" where  c.pk.idGroupe = :idGrp ";
    		
    		String queryDelGroupe = "delete from " + GroupeBean.class.getName() + " g " +
    								" where g.id = :idGrp " +
    								" and g.groupeCollaboratifLocal = true "+
    								" and g.groupeCollaboratif = true ";
    		
    		Integer idGroupe = groupe.getId();
try {
    			
	    		Query query = getEntityManager().createQuery(queryDelEns);
	    		
	    		query.setParameter("idGrp", idGroupe);
				query.executeUpdate();
				
				query = getEntityManager().createQuery(queryDelCycle);
	    		
	    		query.setParameter("idGrp", idGroupe);
				query.executeUpdate();
				
				query = getEntityManager().createQuery(queryDelGroupe);
	    		
	    		query.setParameter("idGrp", idGroupe);
				int nbdel = query.executeUpdate();
				
				if (nbdel != 1) {
					
					throw new MetierException("Echec suppression groupe : reslutat delete {0} != 1", nbdel);
				}
				
				
				
			} catch (Exception e) {
			//	log.error("sql error : {}", e.getMessage());
				throw new MetierException("La suppression  du  groupe '{0}' a echoué: {1}" ,  idGroupe, e.getMessage());
			}
    	}
    	
    	
    }
}
