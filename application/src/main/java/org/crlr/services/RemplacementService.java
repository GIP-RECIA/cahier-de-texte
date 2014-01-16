/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GroupeClasseService.java,v 1.8 2010/04/26 14:03:08 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.remplacement.RechercheRemplacementQO;
import org.crlr.dto.application.remplacement.RemplacementDTO;
import org.crlr.exception.metier.MetierException;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.8 $
  */
public interface RemplacementService {

    /**
     * Recherche la liste des periodes de remplacements prevue sur un etablissement.
     * Si le champ idEnseignant est renseignee sur le QO, la recherche se 
     * restreint aux remplacements dans lesquels intervient l'enseignant en 
     * tant que remplacant ou absent. 
     * @param rechercheRemplacementQO : contient l'id de l'etablissement et eventutellement celui de l'enseignant.
     * @return une liste d'ojets RemplacementDTO.
     */
    public ResultatDTO<List<RemplacementDTO>> findListeRemplacement(final RechercheRemplacementQO rechercheRemplacementQO);


    /**
     * Enregistre un remplacement d'un enseignant pour un autre dans un etablissement 
     * pour une periode donnees.
     * @param remplacementDTO : contient l'absent, le remplacant, l'etablissement la periode. 
     * @return l'identifiant du remplacement qui a ete cree.
     * @throws MetierException exception
     */
    public ResultatDTO<Integer> saveRemplacement(RemplacementDTO remplacementDTO) throws MetierException;
    
    /**
     * Supprime un remplacement. 
     * @param remplacementDTO doit contenir le id du remplacement.
     * @return true ou false selon que ca s'est bien passe ou non  
     * @throws MetierException exception
     */
    public ResultatDTO<Boolean> deleteRemplacement(RemplacementDTO remplacementDTO) throws MetierException;

    /**
     * Persiste une liste de remplacements : fait des ajouts, modif ou delete. 
     * @param listeRemplacementDTO contient les remplacements à persister.
     * @return Le nombre de remplacements enregistres/supprimes  
     * @throws MetierException exception
     */
    public ResultatDTO<Integer> saveListeRemplacement(List<RemplacementDTO> listeRemplacementDTO) throws MetierException;
    
    
    /**
     * Recherche les seances qui ont ete enregistrees par un remplacant dans le cadre d'un remplacement.
     * @param rechercheRemplacementQO : doit contenir l'id de l'etablissement, du prof absent et du prof remplacant
     * @return une liste de seance ordonnee par date de seance croissante.
     */
    public ResultatDTO<List<SeanceDTO>> findListeSeanceRemplacee(RechercheRemplacementQO rechercheRemplacementQO);

    /**
     * Charge la liste des enseignants qui exercent dans un etablissement.
     * @param rechercheRemplacementQO : contient l'id de l'etablissement.
     * @return une liste d'enseignants. 
     */
    public ResultatDTO<List<EnseignantDTO>> findListeEnseignant(RechercheRemplacementQO rechercheRemplacementQO);
    
}
