/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.List;
import java.util.Set;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.UserDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.EnseignantsClasseGroupeQO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.RechercheGroupeClassePopupQO;
import org.crlr.dto.application.base.RechercheGroupeQO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.TypeReglesClasse;
import org.crlr.dto.application.sequence.PrintSeanceOuSequenceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.intercepteur.hibernate.SchemaInterceptorImpl;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.business.ClasseHibernateBusinessService;
import org.crlr.metier.business.EleveHibernateBusinessService;
import org.crlr.metier.business.GroupeHibernateBusinessService;
import org.crlr.metier.utils.SchemaUtils;
import org.crlr.utils.Assert;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sun.util.logging.resources.logging;

/**
 * GroupeClasseFacade.
 *
 * @author $author$
 * @version $Revision: 1.11 $
 */
@Component
@Transactional(
        readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
    )
public class GroupeClasseFacade implements GroupeClasseFacadeService {
    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private GroupeHibernateBusinessService groupeHibernateBusinessService;

    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private ClasseHibernateBusinessService classeHibernateBusinessService;

    
    /** Access au hibernate des eleves.  */
    @Autowired
    private EleveHibernateBusinessService eleveHibernateBusinessService;
    
    /**
     * Mutateur de eleveHibernateBusinessService {@link #eleveHibernateBusinessService}.
     * @param eleveHibernateBusinessService le eleveHibernateBusinessService to set
     */
    public void setEleveHibernateBusinessService(
            EleveHibernateBusinessService eleveHibernateBusinessService) {
        this.eleveHibernateBusinessService = eleveHibernateBusinessService;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rechercheGroupeClassePopupQO DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     *
     * @throws MetierException DOCUMENTATION INCOMPLETE!
     */
    public ResultatDTO<List<GroupesClassesDTO>> findGroupeClassePopup(RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO)
        throws MetierException {
        Assert.isNotNull("rechercheGroupeClassePopupQO", rechercheGroupeClassePopupQO);
        ResultatDTO<List<GroupesClassesDTO>> resultat =
            new ResultatDTO<List<GroupesClassesDTO>>();
        
        if (TypeGroupe.CLASSE == rechercheGroupeClassePopupQO.getTypeGroupeClasse()) {
            resultat = classeHibernateBusinessService.findClassePopup(rechercheGroupeClassePopupQO);
        } else if (TypeGroupe.GROUPE == rechercheGroupeClassePopupQO.getTypeGroupeClasse()) {
            resultat = groupeHibernateBusinessService.findGroupePopup(rechercheGroupeClassePopupQO);
        }

        return resultat;
    }

    /**
     * DOCUMENT ME!
     *
     * @param groupeHibernateBusinessService DOCUMENTATION INCOMPLETE!
     */
    public void setGroupeHibernateBusinessService(GroupeHibernateBusinessService groupeHibernateBusinessService) {
        this.groupeHibernateBusinessService = groupeHibernateBusinessService;
    }

    /**
     * DOCUMENT ME!
     *
     * @param classeHibernateBusinessService DOCUMENTATION INCOMPLETE!
     */
    public void setClasseHibernateBusinessService(ClasseHibernateBusinessService classeHibernateBusinessService) {
        this.classeHibernateBusinessService = classeHibernateBusinessService;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param idClasse DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public GroupesClassesDTO findClasse(Integer idClasse){
        return classeHibernateBusinessService.findClasse(idClasse);
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param idGroupe DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public GroupesClassesDTO findGroupe(final Integer idGroupe) {
        return groupeHibernateBusinessService.findGroupe(idGroupe);
    }

    
    
    /** 
     * si la requette porte sur les archives  
     * on change le schema base de données pour toutes les requettes de la transaction.
     * 
     * @param rechercheGroupeQO
     */
    private void initSchemaInterceptor(RechercheGroupeQO rechercheGroupeQO){
    	
    	if (rechercheGroupeQO != null) {
    		Boolean isArchive = rechercheGroupeQO.getArchive();
    		if (isArchive != null && isArchive) {
    			String exercice = rechercheGroupeQO.getExercice();
    			final String schema = SchemaUtils.getSchemaCourantOuArchive(isArchive, exercice);
    			SchemaInterceptorImpl.setSchema(schema);
    		}
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public List<GroupeDTO> findGroupeByClasse(final RechercheGroupeQO rechercheGroupeQO)
                                       throws MetierException {
    	initSchemaInterceptor(rechercheGroupeQO);
    	
        Assert.isNotNull("rechercheGroupeQO", rechercheGroupeQO);
        final String codeClasse =
            StringUtils.trimToNull(rechercheGroupeQO.getCodeClasse());
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        if (codeClasse == null) {
            conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_02.name(),
                                             Nature.BLOQUANT));
            throw new MetierException(conteneurMessage, "Vous de devez saisir une classe.");
        }

        final Integer idEnseignant = rechercheGroupeQO.getIdEnseignant();

        Assert.isNotNull("codeClasse", codeClasse);

        final Integer idClasse =
            this.existClasse(codeClasse, rechercheGroupeQO.getArchive(),
                             rechercheGroupeQO.getExercice());
        rechercheGroupeQO.setIdClasse(idClasse);

        if (rechercheGroupeQO.isVerif()) {
            final boolean droitClasse =
                classeHibernateBusinessService.checkDroitClasse(idEnseignant, idClasse,
                                                                rechercheGroupeQO.getArchive(),
                                                                rechercheGroupeQO.getExercice());
            if (!droitClasse) {
                conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_01.name(),
                                                 Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                                          "La classe existe mais vous n'avez pas les droits dessus.");
            }
        }

        
        return groupeHibernateBusinessService.findGroupeByClasse(rechercheGroupeQO);
    }

    /**
     * Vérifie qu'une classe existe (par son code) et retourne son id.
     *
     * @param codeClasse le code de la classe
     * @param archive l'archive mode (vrai ou faux)
     * @param exercice l'exercice
     *
     * @return L'id de la classe
     *
     * @exception MetierException exception
     */
    private Integer existClasse(String codeClasse, final Boolean archive,
                                final String exercice)
                         throws MetierException {
        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        if (!StringUtils.isEmpty(codeClasse)) {
            final Integer idClasse =
                classeHibernateBusinessService.findByCode(codeClasse, archive, exercice);
            if (idClasse == null) {
                conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_00.name(),
                                                 Nature.BLOQUANT));
                throw new MetierException(conteneurMessage, "La classe n'existe pas.");
            }
            return idClasse;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Set<Integer> findIdGroupesEleve(final Integer idEleve) {
        return groupeHibernateBusinessService.findIdGroupesEleve(idEleve);
    }

    /**
     * {@inheritDoc}
     */
    public Integer findIdClasseEleve(final Integer idEleve) {
        return classeHibernateBusinessService.findIdClasseEleve(idEleve);
    }

    /**
     * {@inheritDoc}
     */
    public List<EnseignantDTO> findEnseignantsClasse(final EnseignantsClasseGroupeQO enseignantsClasseGroupeQO) {
        return classeHibernateBusinessService.findEnseignantsClasse(enseignantsClasseGroupeQO);
    }

    /**
     * {@inheritDoc}
     */
    public List<EnseignantDTO> findEnseignantsGroupe(final EnseignantsClasseGroupeQO enseignantsClasseGroupeQO) {
        return groupeHibernateBusinessService.findEnseignantsGroupe(enseignantsClasseGroupeQO);
    }
    
    /**
     * {@inheritDoc}
     */
    public GroupesClassesDTO findClasseEleve(Integer idEleve) {
        return findClasse(findIdClasseEleve(idEleve));
    }

    /**
     * {@inheritDoc}
     */
    public List<GroupeDTO> findGroupesEleve(Integer idEleve) {
        final Set<Integer>  idsGroupe = findIdGroupesEleve(idEleve);
        return groupeHibernateBusinessService.findGroupes(idsGroupe);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<UserDTO> findListeEleve(RechercheGroupeQO rechercheGroupeQO) {
        return eleveHibernateBusinessService.findListeEleve(rechercheGroupeQO);
    }

    /**
     * {@inheritDoc}
     */
    public List<GroupeDTO> findGroupesCollaboratifEnseignant(Integer idEnseignant) {
        return groupeHibernateBusinessService.findGroupesCollaboratifEnseignant(idEnseignant);
    }
    
    @Override
    public GroupeDTO creerGroupeCollaboratif(
    									GroupeDTO groupe, 
    									AnneeScolaireDTO anneeScolaire,
    									int idEtablissement, 
    									Integer idEnseignant) throws MetierException {
    	try {
    		if (idEnseignant == null) {
    			throw new MetierException("L'enseignant n'est pas identifié" );
    		} else {
				Integer idGroupe = groupeHibernateBusinessService.findByCode(groupe.getCode(), false, anneeScolaire.getExercice());
				if (idGroupe != null ) {
					
					throw new MetierException("Le groupe de code '{0}' existe déjà.", groupe.getCode());
				} else {
					GroupeDTO groupRes = groupeHibernateBusinessService.creerGroupeCollaboratifLocal(
														groupe.getCode(), 
														groupe.getIntitule(), 
														idEtablissement,
														anneeScolaire.getId());
					
					groupeHibernateBusinessService.addEnseignantInGroupeCollaboratifLocal(groupRes, idEnseignant);
					return groupRes;
				}
    		}
		} catch (Exception e) {
			
			throw new MetierException("Échec de la création du groupe: {0}", e.getMessage());
		}
    	
    }
    
   @Override
    public  void updateGroupeCollaboratif( 	GroupeDTO groupe, Set<Integer> idsAsupprimer, Set<Integer> idsAajouter) throws MetierException{
    	// on commence par les ajouts
    	for (Integer id : idsAajouter) {
    		// si la meme personne est a ajouter et a supprimer on ne fait rien
 			if (! idsAsupprimer.remove(id) ){
 				groupeHibernateBusinessService.addEnseignantInGroupeCollaboratifLocal(groupe, id);
 			} 
		}
    	
    	for (Integer id : idsAsupprimer) {
    		groupeHibernateBusinessService.delEnseignantInGroupeCollaboratifLocal(groupe, id);
		}    	
    }
    										
   @Override
   public  void deleteGroupeCollaboratif( 	GroupeDTO groupe) throws MetierException{
	   groupeHibernateBusinessService.deleteGroupeCollaboratifLocal(groupe);
   }
}
