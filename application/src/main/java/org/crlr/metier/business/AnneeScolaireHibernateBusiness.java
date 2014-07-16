/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AnneeScolaireHibernateBusiness.java,v 1.12 2010/06/04 07:25:50 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.persistence.Query;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.alimentation.Archive;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.DateAnneeScolaireQO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.OuvertureQO;
import org.crlr.dto.application.base.PeriodeVacanceQO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.TypeReglesAcquittement;
import org.crlr.dto.application.base.TypeReglesCahierArchive;
import org.crlr.exception.base.CrlrRuntimeException;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.entity.AnneeScolaireBean;
import org.crlr.metier.entity.ClasseBean;
import org.crlr.metier.entity.GroupeBean;
import org.crlr.metier.utils.SchemaUtils;
import org.crlr.utils.Assert;
import org.crlr.utils.BooleanUtils;
import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.springframework.stereotype.Repository;

/**
 * AnneeScolaireHibernateBusiness.
 *
 * @author vibertd
 * @version $Revision: 1.12 $
 */
@Repository
public class AnneeScolaireHibernateBusiness extends AbstractBusiness
    implements AnneeScolaireHibernateBusinessService {
    
    /**
     * {@inheritDoc}     
     */
    public ResultatDTO<Integer> saveAnneeScolaire(final DateAnneeScolaireQO dateAnneeScolaireQO) {
        Assert.isNotNull("dateAnneeScolaireQO", dateAnneeScolaireQO);

        final Integer idAnneeScolaire = dateAnneeScolaireQO.getId();
        Assert.isNotNull("idAnneeScolaire", idAnneeScolaire);
        
        final Date dateRentree = dateAnneeScolaireQO.getDateRentree();
        final Date dateSortie = dateAnneeScolaireQO.getDateSortie();
        
        Assert.isNotNull("dateRentree", dateRentree);
        Assert.isNotNull("dateSortie", dateSortie);        
        
        final String queryUpdate =
            "UPDATE " + AnneeScolaireBean.class.getName() + 
            " A SET A.dateRentree=:dateRentree, A.dateSortie=:dateSortie WHERE A.id = :id";

        getEntityManager().createQuery(queryUpdate)
            .setParameter("id", idAnneeScolaire)
            .setParameter("dateRentree", dateRentree)
            .setParameter("dateSortie", dateSortie)
            .executeUpdate();

        getEntityManager().flush();
        
        final ResultatDTO<Integer> resultat = new ResultatDTO<Integer>();
        
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                Nature.INFORMATIF, "La nouvelle année scolaire", "prise en compte"));
        
        resultat.setConteneurMessage(conteneurMessage);
        
        return resultat;
    }
    
    /**
     * {@inheritDoc}     
     */
    public ResultatDTO<Integer> savePeriodeVacance(final PeriodeVacanceQO periodeVacanceQO) {
        Assert.isNotNull("periodeVacanceQO", periodeVacanceQO);

        final Integer idAnneeScolaire = periodeVacanceQO.getId();
        Assert.isNotNull("idAnneeScolaire", idAnneeScolaire);
        
        final String periodeVacances = periodeVacanceQO.getPeriodeVacances();
        if (! StringUtils.isEmpty(periodeVacances)){
            Assert.isTrue("periodeVacances", Pattern.matches("([0-9]{2}/[0-9]{2}/[0-9]{4}:[0-9]{2}/[0-9]{2}/[0-9]{4}\\|)*", periodeVacances));
        }
                
        final String queryUpdate =
            "UPDATE " + AnneeScolaireBean.class.getName() + 
            " A SET A.periodeVacance=:periodeVacances WHERE A.id = :id";

        getEntityManager().createQuery(queryUpdate)
            .setParameter("id", idAnneeScolaire)
            .setParameter("periodeVacances", periodeVacances)           
            .executeUpdate();

        getEntityManager().flush();
        
        final ResultatDTO<Integer> resultat = new ResultatDTO<Integer>();
        
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_01.name(),
                Nature.INFORMATIF, "Les nouvelles plages d'inactivitées", "prises en compte"));
        
        resultat.setConteneurMessage(conteneurMessage);
        
        return resultat;
    }   
    
    /**
     * {@inheritDoc}     
     */
    public ResultatDTO<Integer> saveOuvertureENT(final OuvertureQO ouvertureQO) {
        Assert.isNotNull("ouvertureQO", ouvertureQO);

        final Integer idAnneeScolaire = ouvertureQO.getId();
        Assert.isNotNull("idAnneeScolaire", idAnneeScolaire);
        
        final Boolean vraiOuFauxCahierOuvertENT = BooleanUtils.isTrue(ouvertureQO.getVraiOuFauxOuvert());
                
        final String queryUpdate =
            "UPDATE " + AnneeScolaireBean.class.getName() + 
            " A SET A.vraiOuFauxCahierOuvertENT=:vraiOuFauxCahierOuvertENT WHERE A.id = :id";

        getEntityManager().createQuery(queryUpdate)
            .setParameter("id", idAnneeScolaire)
            .setParameter("vraiOuFauxCahierOuvertENT", vraiOuFauxCahierOuvertENT)           
            .executeUpdate();

        getEntityManager().flush();
        
        final ResultatDTO<Integer> resultat = new ResultatDTO<Integer>();
        
        final String message = vraiOuFauxCahierOuvertENT ? "L'ouverture" : "La fermeture" ;
        
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_00.name(),
                Nature.INFORMATIF, message + " du cahier de texte aux établissements", "prise en compte"));
        
        resultat.setConteneurMessage(conteneurMessage);
        
        return resultat;
    }    
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public AnneeScolaireDTO findAnneeScolaire() {        
        final AnneeScolaireDTO anneeScolaireDTO = new AnneeScolaireDTO();
        
        final String query =
            "SELECT new Map(a.id as id, a.dateRentree as dateRentree, a.dateSortie as dateSortie, " +
            "a.exercice as exercice, a.periodeVacance as periodeVacance, a.vraiOuFauxCahierOuvertENT as ouvert, "+
            "a.periodeScolaire as periodeScolaire) FROM " 
            + AnneeScolaireBean.class.getName() +
            " a ORDER BY a.dateRentree DESC, a.dateSortie DESC";

        final List<Map<String,?>> liste =
            getEntityManager().createQuery(query)
           .getResultList();

        if (!org.apache.commons.collections.CollectionUtils.isEmpty(liste)) {
            final Map<String, ?> mapAnnee = liste.get(0);
            anneeScolaireDTO.setId((Integer) mapAnnee.get("id"));
            anneeScolaireDTO.setDateRentree((Date) mapAnnee.get("dateRentree"));
            anneeScolaireDTO.setDateSortie((Date) mapAnnee.get("dateSortie"));
            anneeScolaireDTO.setExercice((String) mapAnnee.get("exercice"));
            anneeScolaireDTO.setPeriodeVacances((String) mapAnnee.get("periodeVacance"));
            anneeScolaireDTO.setVraiOuFauxCahierOuvertENT((Boolean) mapAnnee.get("ouvert"));
            anneeScolaireDTO.setPeriodeScolaires((String) mapAnnee.get("periodeScolaire"));
        } else {
            throw new CrlrRuntimeException("Echec de la recherche de l'année scolaire");
        }
        
        return anneeScolaireDTO;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean checkDateAnneeScolaire(Integer idAnneeScolaire, Date dateDebut,
                                          Date dateFin) {
        Assert.isNotNull("idAnneeScolaire", idAnneeScolaire);
        Assert.isNotNull("dateDebut", dateDebut);
        Assert.isNotNull("dateFin", dateFin);
        boolean checkResult = false;

        log.debug("210 checkDateAnneeScolaire idAnneeScolaire {}, dateDebut {}, dateFin {}",idAnneeScolaire,  dateDebut, dateFin );
        final String requete =
            " SELECT 1 FROM " + AnneeScolaireBean.class.getName() +
            " ANNEE " + " WHERE " + " ANNEE.dateRentree <= :dateDebut AND " +
            " ANNEE.dateSortie >= :dateFin AND " + " ANNEE.id = :idAnneeScolaire ";

        final List<Integer> liste =
            getEntityManager().createQuery(requete).setParameter("dateDebut", dateDebut)
                .setParameter("dateFin", dateFin)
                .setParameter("idAnneeScolaire", idAnneeScolaire).getResultList();

        if (!org.apache.commons.collections.CollectionUtils.isEmpty(liste)) {
            checkResult = true;
        }

        return checkResult;
    }
    
    @SuppressWarnings("unchecked")
    public ResultatDTO<AnneeScolaireDTO> findAnneeScolaire(final GroupesClassesDTO groupeClasseDTO)
            throws MetierException {
        
        final ResultatDTO<AnneeScolaireDTO> resultat = new ResultatDTO<AnneeScolaireDTO>();
        
        final String requete = 
            "SELECT" +
                
                    " ANNEE " +                    
            " FROM " +
                AnneeScolaireBean.class.getName() + " ANNEE WHERE " +
            ( (groupeClasseDTO.getTypeGroupe() == TypeGroupe.CLASSE) ?
                    "EXISTS (Select 1 FROM " + ClasseBean.class.getName() + " c where ANNEE.id = c.idAnneeScolaire and c.id = :idClasseGroupe)"
                    :
             " EXISTS (Select 1 FROM " + GroupeBean.class.getName() + " g where ANNEE.id = g.idAnneeScolaire and g.id = :idClasseGroupe)" 
             		);
        
        List<AnneeScolaireBean> resultats = (List<AnneeScolaireBean>)
                getEntityManager().createQuery(requete)
                .setParameter("idClasseGroupe", groupeClasseDTO.getId()).getResultList();
        
        if (!CollectionUtils.isEmpty(resultats)) {
            AnneeScolaireDTO ret = new AnneeScolaireDTO();
            ObjectUtils.copyProperties(ret,  resultats.get(0));
            resultat.setValeurDTO(ret);
            
        }
        
        return resultat;
    }

    /**
     * recherche tout les shémas de base de donnée
     * @return
     */
    public List<String> findAllArchiveSchemaNames (){
    	String queryText = 	"SELECT schema_name " +
    						"FROM information_schema.schemata " +
    						"WHERE schema_name like '" + Archive.PREFIX_SCHEMA_ARCHIVE + "%'" ;
    	
    	Query query = getEntityManager().createNativeQuery(queryText);
    	
    	@SuppressWarnings("unchecked")
		List<String> schemaList =  query.getResultList();
    	return schemaList;
    }
    
    
    
    /**
     * Trouve les anciennes années scolaire
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<AnneeScolaireDTO>> findListeAnneeScolaire(final Boolean vraiOuFauxMessageBloquant)
            throws MetierException {

        final ResultatDTO<List<AnneeScolaireDTO>> resultat = new ResultatDTO<List<AnneeScolaireDTO>>();
        final List<AnneeScolaireDTO> listeAnneeScolaire = new ArrayList<AnneeScolaireDTO>();
        final Date dateDuJour = DateUtils.getAujourdhui(); 
        
        final String requete = 
            "SELECT" +
                " new map(" +
                    " ANNEE.id as idAnneeScolaire, " +
                    " ANNEE.dateRentree as dateRentree, " +
                    " ANNEE.dateSortie as dateSortie, " +
                    " ANNEE.exercice as exercice) " +
            " FROM " +
                AnneeScolaireBean.class.getName() + " ANNEE WHERE " +
             " ANNEE.dateRentree < :dateDuJour AND ANNEE.dateSortie <= :dateDuJour " +
            " ORDER BY ANNEE.id DESC";
        
        final List<Map<String, ?>> liste = 
            getEntityManager().createQuery(requete)
            .setParameter("dateDuJour", dateDuJour)
            .getResultList();
        
        if (liste != null && ! liste.isEmpty()) {
        	
        	List<String> schemaList = findAllArchiveSchemaNames();
        	if (schemaList != null && !schemaList.isEmpty()) {
		        for(Map<String, ?> result : liste) {
		        	String exercice = (String) result.get("exercice");
		        	String schema = SchemaUtils.getSchemaCourantOuArchive(true, exercice);
		        	
		        	if (schemaList.contains(schema)) {
			            final AnneeScolaireDTO anneeScolaireDTO = new AnneeScolaireDTO();
			            anneeScolaireDTO.setId((Integer) result.get("idAnneeScolaire"));
			            anneeScolaireDTO.setDateRentree((Date) result.get("dateRentree"));
			            anneeScolaireDTO.setDateSortie((Date) result.get("dateSortie"));
			            anneeScolaireDTO.setExercice(exercice);
			            listeAnneeScolaire.add(anneeScolaireDTO);
		        	}
		        }        
        	}
	        if (org.apache.commons.collections.CollectionUtils.isEmpty(listeAnneeScolaire) && BooleanUtils.isTrue(vraiOuFauxMessageBloquant)) {
	            final ConteneurMessage conteneurMessage = new ConteneurMessage();
	            conteneurMessage.add(new Message(TypeReglesCahierArchive.ARCHIVE_02.name()));
	            throw new MetierException(conteneurMessage, "Aucune archive disponible.");
	        }
	        
	        }
        resultat.setValeurDTO(listeAnneeScolaire);
        
        return resultat;
        
    }
}
