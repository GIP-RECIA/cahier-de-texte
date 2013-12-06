/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ClasseHibernateBusinessService.java,v 1.14 2010/04/26 14:03:08 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignantsClasseGroupeQO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.RechercheGroupeClassePopupQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.entity.ClasseBean;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author vibertd
 * @version $Revision: 1.14 $
  */
public interface ClasseHibernateBusinessService {
    /**
     * Recherche une classe à partir de son id.
     *
     * @param id Id de la classe à rechercher
     *
     * @return Une classeBean
     */
    public ClasseBean find(Integer id);

    /**
     * Méthode de sauvegarde d'une classe.
     *
     * @param classe Contient les infos à sauvegarder
     *
     * @return L'id de la classe sauvegardée
     *
     * @throws MetierException Exception
     */
    public Integer save(ClasseBean classe) throws MetierException;

    /**
     * Retourne l'ensemble des classes pour la popup.
     *
     * @param rechercheGroupeClassePopupQO Contient les paramètres de recherche
     *
     * @return La liste des séances qui correspondent aux paramètres
     *
     * @throws MetierException Exception
     */
    public ResultatDTO<List<GroupesClassesDTO>> findClassePopup(RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO)
        throws MetierException;
    
    /**
     * Vérifie qu'une classe existe.
     *
     * @param idClasse l'id de la classe à tester
     *
     * @return l'id de la classe s'il existe sinon null
     *
     * @throws MetierException Exception
     */
    public Integer exist(Integer idClasse) throws MetierException;

    /**
     * Recherche une classe par son id et retourne ses informations.
     *
     * @param idClasse L'id de la classe à rechercher
     *
     * @return Les informations de la classe
     */
    public GroupesClassesDTO findClasse(Integer idClasse);

    /**
     * Méthode de recherche d'une classe par code.
     *
     * @param codeClasseGroupe Le code de la classe à rechercher.
     * @param archive archive.
     * @param exercice exercice.
     *
     * @return Un classeBean
     *
     * @throws MetierException Exception
     */
    public Integer findByCode(final String codeClasseGroupe, final Boolean archive, final String exercice)
                          throws MetierException;

    /**
     * Permet de vérifier les droits sur une classe.
     *
     * @param idEnseignant id l'enseignant.
     * @param idClasse id de la idClasse.
     * @param archive archive si true.
     * @param exercice l'exercice.
     * @return true ou false
     * 
     * @throws MetierException Exception
     */
    public boolean checkDroitClasse(final Integer idEnseignant, final Integer idClasse, 
            final Boolean archive, final String exercice) throws MetierException;
    
    /**
     * Permet de vérifier les droits sur une classe pour un inspecteur.
     *
     * @param idInspecteur id l'inspecteur.
     * @param idClasse id de la idClasse.
     * @param archive archive si true.
     * @param exercice l'exercice.
     * @param idEtablissement l'id de l'etablissment.
     * @return true ou false
     * 
     * @throws MetierException Exception
     */
    public boolean checkDroitClasseInspecteur(final Integer idInspecteur, final Integer idClasse, 
            final Boolean archive, final String exercice, final Integer idEtablissement) throws MetierException;
    
    /**
     * Permet de vérifier les droits sur une classe pour la direction.
     *
     * @param idEtablissement id l'établissement de rattachement.
     * @param idClasse id de la idClasse.
     * @param archive archive si true.
     * @param exercice l'exercice.
     * @return true ou false
     * 
     * @throws MetierException Exception
     */
    public boolean checkDroitClasseDirection(final Integer idEtablissement, final Integer idClasse, 
            final Boolean archive, final String exercice) throws MetierException;

    /**
     * Retourne la liste des classes de l'eleve.
     *
     * @param idEleve L'id de l'eleve
     *
     * @return La liste des classes
     * 
     * @throws MetierException Exception
     */
    public List<ClasseBean> findClassesEleve(
            Integer idEleve) throws MetierException;
    
    /**
     * Recherche de l'id de la classe de l'élève. L'élève peut être rattaché a une et une seule classe.
     * @param idEleve l'id de l'élève.
     * @return l'id de la classe.
     */
    public Integer findIdClasseEleve(Integer idEleve);
    
    /**
     * Recherche des enseignants d'une classe.
     * @param enseignantsClasseGroupeQO les paramètres.
     * @return la liste des enseignants
     */
    public List<EnseignantDTO> findEnseignantsClasse(final EnseignantsClasseGroupeQO enseignantsClasseGroupeQO);

    /**
     * Permet de récupérer l'id de la classe.
     * @param codeClasse : code de la classe.
     * @param idEtab : établissement de la classe.
     * @return l'id de la classe.
     */
    public int findIdClasseByCodeAndEtab(String codeClasse, int idEtab);
    
    /**
     * Récupère la liste des classes qui appartiennent à un établissement.
     * @param idEtab : id de l'établissement.
     * @return la liste des classes de l'établissement.
     */
    public List<Integer> findListeClassePourEtab(Integer idEtab);
    

}
