/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.remplacement.RechercheRemplacementQO;
import org.crlr.dto.application.remplacement.RemplacementDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface RemplacementHibernateBusinessService {

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
     */
    public ResultatDTO<Integer> saveRemplacement(RemplacementDTO remplacementDTO);
    
    /**
     * Supprime un remplacement. 
     * @param remplacementDTO doit contenir le id du remplacement.
     * @return true ou false selon que ca s'est bien passe ou non  
     */
    public ResultatDTO<Boolean> deleteRemplacement(RemplacementDTO remplacementDTO);
    
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
