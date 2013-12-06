/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnseignementHibernateBusinessService.java,v 1.12 2010/04/26 07:45:49 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.EnseignementQO;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.entity.EnseignementBean;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author vibertd
 * @version $Revision: 1.12 $
  */
public interface EnseignementHibernateBusinessService {
    /**
     * Vérifie qu'un enseignement existe.
     *
     * @param id l'id de l'enseignement à tester
     *
     * @return l'id de l'enseignement s'il existe sinon null
     *
     * @throws MetierException Exception
     */
    public Integer exist(Integer id) throws MetierException;

    /**
     * Sauvegarde d'un enseignantBean.
     *
     * @param enseignement L'enseignement à sauvegarder
     *
     * @return L'id de l'enseignement sauvegardé
     *
     * @throws MetierException Exception
     */
    public Integer save(EnseignementBean enseignement)
                 throws MetierException;

    /**
     * Retourne l'ensemble des enseignements pour la popup.
     *
     * @param rechercheEnseignementPopupQO paramètres de recherche de séance
     *
     * @return La liste des enseignements pour la popup
     *
     * @throws MetierException Exception
     */
    public ResultatDTO<List<EnseignementDTO>> findEnseignementPopup(RechercheEnseignementPopupQO rechercheEnseignementPopupQO)
        throws MetierException;

    /**
     * Retourne un enseignement a partir de son id.
     *
     * @param idEnseignement Id de l'enseignement à rechercher
     *
     * @return EnseignementDTO
     *
     * @throws MetierException Exception
     */
    public EnseignementDTO find(Integer idEnseignement)
                          throws MetierException;

    /**
     * méthode de recherche d'un enseignement par code.
     *
     * @param codeEnseignement le code de l'enseignement à rechercher
     *
     * @return Un enseignementBean
     *
     * @throws MetierException Exception
     */
    public EnseignementBean findByCode(String codeEnseignement)
                                throws MetierException;

    /**
     * Permet de vérifier les droits sur un enseignement.
     *
     * @param idEnseignant id l'enseignant.
     * @param idEnseignement id de l'enseignement.
     * @param idEtablissement id de l'établissement.
     *
     * @return true oi false
     *
     * @throws MetierException Règles en cas d'erreur.
     */
    public boolean checkDroitEnseignement(Integer idEnseignant, Integer idEnseignement, Integer idEtablissement)
                                   throws MetierException;
    
    /**
     * Recherche des enseignements liés à un enseignant.
     * @param rechercheEnseignementPopupQO les paramètres de recherches.
     * @return la liste.
     */
    public List<EnseignementDTO> findEnseignementEnseignant(final RechercheEnseignementPopupQO rechercheEnseignementPopupQO);
    
    /**
     * Recherche des enseignements de l'établissement.
     * @param idEtablissement l'identifiant de l'établissement.
     * @return la liste des enseignements.
     */   
    public List<EnseignementDTO> findEnseignementEtablissement(final Integer idEtablissement);
    
    /**
     * Création, mise à jour, suppression d'un libellé d'enseignement court d'un établissement.
     * @param enseignementQO les parmètres.
     * @throws MetierException l'exception.
     */   
    public void saveEnseignementLibelle(final EnseignementQO enseignementQO) throws MetierException;
    
    /**
     * Recherche des libelles personnalisés des enseignements d'un établissement.
     * @param idEtablissement l'id de l'établissement.
     * @param listeIdEnseignement la liste des id d'enseignement.
     * @return la map ayant pour clef l'id d'enseignement et en valeur le libellé personnalisé.
     */    
    public Map<Integer, String> findEnseignementPersoEtablissement(final Integer idEtablissement, Set<Integer> listeIdEnseignement);
    
    /**
     * Permet de récupérer l'id de l'enseignement.
     * @param libelleMatiere : le libelle_long de la balise matière.
     * @return l'id de l'enseignement.
     */
    public int findIdEnseignementByLibelle(String libelleMatiere);
}
