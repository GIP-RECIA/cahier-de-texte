/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConfidentialiteFacadeService.java,v 1.8 2010/05/20 08:24:50 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.List;
import java.util.Set;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.CheckSaisieSimplifieeQO;
import org.crlr.dto.application.base.ElevesParentDTO;
import org.crlr.dto.application.base.EtablissementAccessibleQO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.securite.AuthentificationQO;
import org.crlr.exception.metier.MetierException;


/**
 * Interface de la Façade Confidentialité.
 *
 * @author breytond.
 * @version $Revision: 1.8 $
  */
public interface ConfidentialiteFacadeService {    
    
   /**
    * Identification d'un utilisateur en fonction du type d'authentification {@link org.crlr.dto.securite.TypeAuthentification}.
    * Création de l'utilisateur en base données lors de la première connexion de celui-ci.
    * @param authentificationQO les paramètres.
    * @throws MetierException l'exception potentielle.
    * @return {@link ResultatDTO}.
    */
    public ResultatDTO<UtilisateurDTO> initialisationAuthentification(final AuthentificationQO authentificationQO)
    throws MetierException;
    
    /**
     * Recherche des élèves du parent.
     * @param listeUidEleve la liste des uid.
     * @return la liste des élèves du parent.
     */
    public ResultatDTO<ElevesParentDTO> findEleveDuParent(final Set<String> listeUidEleve);
    
    /**
     * Recherche des établissements disponibles pour les inspecteurs académiques.
     * @return la liste des etablissements courants et archivés.
     * @throws MetierException l'exception potentielle.
     */   
    public ResultatDTO<List<EtablissementDTO>> findListeEtablissement() throws MetierException;
    
    /**
     * Recherche des établissements disponibles pour les enseignants.
     * @param etablissementAccessibleQO les paramètres.
     * @return la liste des etablissements courants.
     */  
    public ResultatDTO<List<EtablissementDTO>> findListeEtablissementEnseignant(final EtablissementAccessibleQO etablissementAccessibleQO);
    
    /**
     * Retourne la chaine des préférences de l'utilisateur.
     * @param uid l'uid de l'utilisateur connecté
     * @return une chaine contenant les préférences
     */
    public String findUtilisateurPreferences(final String uid);
    
    /**
     * Recherche des établissements d'un enseignant administrateur de ressources.
     * @param etablissementAccessibleQO les paramètres.
     * @return la liste.
     */
    public ResultatDTO<List<EtablissementDTO>> findListeEtablissementEnseignantAdmRessources(
            final EtablissementAccessibleQO etablissementAccessibleQO);
    
    /**
     * Contrôle puis sauvegarde de la nécessité d'activer par défaut la saisie simplifiée de séquence.
     * @param checkSaisieSimplifieeQO les paramètres.
     * @return la valeur de la saisie simplifiée.
     * @throws MetierException l'exception potentielle.
     */
    public Boolean saveCheckAutiomatisationActivationSaisieSimplifiee(final CheckSaisieSimplifieeQO checkSaisieSimplifieeQO) throws MetierException;
}
