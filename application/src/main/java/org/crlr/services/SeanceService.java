/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SeanceService.java,v 1.18 2009/07/30 15:09:26 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EtablissementSchemaQO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.seance.ConsulterSeanceDTO;
import org.crlr.dto.application.seance.ConsulterSeanceQO;
import org.crlr.dto.application.seance.PrintSeanceDTO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.dto.application.seance.ResultatRechercheSeanceDTO;
import org.crlr.dto.application.seance.SaveSeanceQO;
import org.crlr.dto.application.sequence.PrintSeanceOuSequenceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.report.Report;
import org.crlr.web.dto.FileUploadDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.18 $
  */
public interface SeanceService {
    /**
     * Méthode de recherche d'une séance.
     *
     * @param rechercheSeanceQO DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     *
     * @throws MetierException DOCUMENTATION INCOMPLETE!
     */
    public ResultatDTO<List<ResultatRechercheSeanceDTO>> findSeance(final RechercheSeanceQO rechercheSeanceQO)
        throws MetierException;

    /**
     * Sauvegarder (ajout) d'une séance.
     *
     * @param saveSeanceQO L'ensemble des critères de la séance
     *
     * @return l'id de la séance ajoutée ou modifiée
     *
     * @throws MetierException Exception
     */
    public ResultatDTO<Integer> saveSeance(SaveSeanceQO saveSeanceQO)
                       throws MetierException;

    /**
     * Sauvegarder (ajout) d'une liste de séances.
     *
     * @param listeSeanceQO La liste des séances
     *
     * @return le nombre de seance ajoutée ou modifiée
     *
     * @throws MetierException Exception
     */
    public ResultatDTO<Integer> saveListeSeance(List<SaveSeanceQO> listeSeanceQO)
                       throws MetierException;
    
    /**
     * Suppression d'une séance.
     *
     * @param resultatRechercheSeanceDTO resultatSelectionne
     *
     * @throws MetierException Exception
     * @return le résultat.
     */
    public ResultatDTO<Integer> deleteSeance(SeanceDTO resultatRechercheSeanceDTO)
                      throws MetierException;

    /**
     * Méthode de recherche des devoirs d'une seance.
     *
     * @param idSeance DL'id de la séance
     *
     * @return la liste des devoirs
     */
    public ResultatDTO<List<DevoirDTO>> findDevoir(Integer idSeance);

    /**
     * Méthode de recherche des pièces jointes d'une seance.
     *
     * @param idSeance DL'id de la séance
     *
     * @return la liste des devoirs
     */
    public ResultatDTO<List<FileUploadDTO>> findPieceJointe(Integer idSeance);

    /**
     * Méthode qui retourne l'ensemble des informations nécessaires à la
     * consultation d'une séance.
     *
     * @param consulterSeanceQO consulterSeanceQO.
     *
     * @return Un consulterSeanceDTO
     *
     * @throws MetierException Exception
     */
    public ConsulterSeanceDTO consulterSeance(final ConsulterSeanceQO consulterSeanceQO)
                                       throws MetierException;
    
    /**
     * 
     * 
     * @param consulterSeanceQO c
     * @return Un séance DTO avec devoirs, pièce jointe et description renseignés.
     * @throws MetierException ex
     */
    public SeanceDTO rechercherSeance(
            ConsulterSeanceQO consulterSeanceQO) throws MetierException;

    /**
     * Méthode qui retourne l'ensemble des informations nécessaires à la
     * consultation d'une séance précédente.
     * Recherche la séance précédente :
     * Date antérieure à la date/heure de début, avec le même prof,enseignement,classe/groupe 
     * 
     * @param chercherSeanceQO chercherSeanceQO.
     *
     * @return Un consulterSeanceDTO ou null si pas trouvée 
     *
     * @throws MetierException Exception
     */
    public ConsulterSeanceDTO chercherSeancePrecedente(final RechercheSeanceQO chercherSeanceQO)
                                       throws MetierException;

    /**
     * Méthode qui retourne l'ensemble des informations nécessaires à la
     * consultation de la liste des séances précédentes.
     * Recherche la séance précédente :
     * Date antérieure à la date/heure de début, avec le même prof,enseignement,classe/groupe 
     * 
     * @param chercherSeanceQO chercherSeanceQO.
     * @param nbrSeance : nombre max de seance a chercher.
     * @return Une liste de seances. 
     *
     * @throws MetierException Exception
     */
    public List<SeanceDTO> chercherListeSeancePrecedente(final RechercheSeanceQO chercherSeanceQO, final Integer nbrSeance)
                                       throws MetierException;
    
    /**
     * Méthode de modification d'uner séance.
     *
     * @param resultatRechercheSeanceDTO Contient l'id de la séance à modifier
     * @param saveSeanceQO Les nouvelles modifs de la séance
     *
     * @return L'id de la séance modifiée
     *
     * @throws MetierException Exception
     */
    public ResultatDTO<Integer> modifieSeance(SeanceDTO resultatRechercheSeanceDTO
                                 )
                          throws MetierException;

    /**
     * Permet de vérifier l'appartenance d'une pièce jointe à plusieurs devoirs
     * ou séances afin de ne pas la supprimer.
     *
     * @param fileUploadDTO le fichier à supprimer.
     *
     * @return True ou false
     *
     * @throws MetierException Exception
     */
    public boolean verifieAppartenancePieceJointe(FileUploadDTO fileUploadDTO)
                                           throws MetierException;

    /**
     * Méthode d'affichage des séances.
     *
     * @param rechercheSeanceQO DOCUMENT ME!
     *
     * @return la liste des devoirs à afficher
     *
     * @throws MetierException Exception
     */
    public ResultatDTO<List<ResultatRechercheSeanceDTO>> listeSeanceAffichage(RechercheSeanceQO rechercheSeanceQO)
        throws MetierException;

    /**
     * Recherche de l'identifiant d'un etablissement en fonction de son code et
     * du schéma.
     *
     * @param etablissementSchemaQO etablissementSchemaQO.
     *
     * @return l'identifiant.
     * @throws MetierException .
     */
    public Integer findIdEtablissementSuivantSchema(final EtablissementSchemaQO etablissementSchemaQO) throws MetierException;

    /**
     * Retourne le DTO complet de la séance pour l'édition PDF.
     *
     * @param printSeanceQO Les paramètres de recherche
     * @param list  .
     *
     * @return Report Le report
     *
     * @throws MetierException Exception
     */
    public Report printSeance(PrintSeanceOuSequenceQO printSeanceQO, List<PrintSeanceDTO> list)
                                        throws MetierException;
    
    /**
     * Recherche des seance pour la page d'édition.
     * @param rechercheSeancePrintQO .
     * @return La liste des seances repondant aux critères.
     * @throws MetierException .
     */
    public ResultatDTO<List<PrintSeanceDTO>> findListeSeanceEdition(PrintSeanceOuSequenceQO rechercheSeancePrintQO) throws MetierException;

    /**
     * Recherche les pieces jointes d'un devoir.
     * @param idDevoirSelectione .
     * @return .
     */
    public ResultatDTO<List<FileUploadDTO>> findPieceJointeDevoir(
            Integer idDevoirSelectione);

    /**
     * Recherche les seances pour le planning.
     * @param rechercheSeanceQO le QO.
     * @return La liste des seances.
     * @throws MetierException l'exception potentielle.
     */
    public ResultatDTO<List<SeanceDTO>> findSeanceForPlanning (
            RechercheSeanceQO rechercheSeanceQO) throws MetierException;

    /**
     * donne l'aide de l'ecran saisir seance
     * @return
     */
	public String getAideContextuelle();

}
