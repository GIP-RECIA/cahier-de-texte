/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignantHibernateBusinessService.java,v 1.3 2009/05/26 08:06:30 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.List;
import java.util.Map;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.exception.metier.MetierException;

import org.crlr.metier.entity.EnseignantBean;

/**
 * EnseignantHibernateBusinessService.
 *
 * @author vibertd
 * @version $Revision: 1.3 $
  */
public interface EnseignantHibernateBusinessService {
    /**
     * Recherche d'un enseignant.
     *
     * @param uid l'uid de l'enseignant.
     *
     * @return l'enseignant.
     */
    public EnseignantBean find(String uid);

    /**
     * Recherche de l'existence d'un enseignant.
     *
     * @param uid l'uid
     *
     * @return l'identifiant.
     */
    public Map<String, Object> exist(String uid);

    /**
     * Mise à jour d'un enseignant.
     *
     * @param enseignant l'enseignant.
     *
     * @return l'identifiant.
     *
     * @throws MetierException l'exception potentielle.
     */
    public Integer save(EnseignantBean enseignant)
                 throws MetierException;
    
    /**
     * Permet de récupérer l'id de l'enseignant à partir des paramètres.
     * @param idEtab : l'établissement.
     * @param nom : nom de l'enseignant.
     * @param prenom : prénom de l'enseignant.
     * @return l'id de l'enseignant.
     */
    public int findIdEnseignantParEtabNomPrenom(int idEtab, String nom, String prenom);
    
    /**
     * Vérifie si l'enseignant est autorisé à enseigner la matière dans l'établissement.
     * @param idEtab : id de l'établissement.
     * @param idEns : id de l'enseignant.
     * @param idMatiere : id de l'enseignement.
     * @return vrai s'il a le droit, faux sinon.
     */
    public boolean verifieAutorisationEnseignement(Integer idEtab, Integer idEns, Integer idMatiere);
    
    /**
     * Vérifie si une correspondance enseignant Classe existe.
     * @param idEns : id de l'enseignant.
     * @param idClasse : id de la classe.
     * @return vrai si on trouve une correspondance.
     */
    public boolean verifieEnseignantClasse(Integer idEns, Integer idClasse);
    
    /**
     * Permet de savoir si un enseignant est bien autorisé à enseigner à une classe.
     * @param idEns : id de l'enseignant.
     * @param idGroupe : id du groupe.
     * @return vrai si la liaison est trouvé, faux sinon.
     */
    public boolean verifieEnseignantGroupe(Integer idEns, Integer idGroupe);

    /**
     * Rechercher de la liste des enseignants d'un établissement;
     * @param idEtab
     * @return list des enseignants triée sur le nom et prénom.
     */
	List<EnseignantDTO> findEnseignantsByEtab(Integer idEtab);
}
