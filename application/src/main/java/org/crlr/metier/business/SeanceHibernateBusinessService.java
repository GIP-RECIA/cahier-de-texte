/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SeanceHibernateBusinessService.java,v 1.17 2010/04/21 15:39:48 jerome.carriere Exp $
 */

package org.crlr.metier.business;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.seance.ConsulterSeanceQO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.dto.application.seance.ResultatRechercheSeanceDTO;
import org.crlr.dto.application.sequence.PrintSeanceOuSequenceQO;
import org.crlr.dto.application.sequence.PrintSequenceDTO;
import org.crlr.dto.application.sequence.ResultatRechercheSequenceDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.web.dto.FileUploadDTO;
import org.crlr.web.dto.GrilleHoraireDTO;
import org.crlr.web.dto.TypeSemaine;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author vibertd
 * @version $Revision: 1.17 $
  */
public interface SeanceHibernateBusinessService {
    /**
     * Recherche une séance à partir de son id.
     *
     * @param idSeance L'id de la séance à rechercher.
     * @param archive archive.
     * @param exercice l'exercice.
     *
     * @return Un seance DTO
     */
    public SeanceDTO trouver(final Integer idSeance, final Boolean archive,
                          final String exercice);
    
    /**
     * Recherche une seance.
     * @param consulterSeanceQO l'id de la seance et eventuellement l'archive 
     * @return
     */
    public SeanceDTO findSeanceById(ConsulterSeanceQO consulterSeanceQO);

    /**
     * Recherche une séance antérieur à une date/heure de début pour 
     * le même prof, enseignement et classe/groupe.
     * 
     * @param rechercheSeanceQO le QO.
     * @return SeanceDTO
     */
    public SeanceDTO findSeancePrecedente(final RechercheSeanceQO rechercheSeanceQO);

    /**
     * Recherche une liste de séance antérieur à une date/heure de début pour 
     * le même prof, enseignement et classe/groupe.
     * 
     * @param rechercheSeanceQO le QO.
     * @param nbrSeance : nombre de seance précédente à retourner au maximum
     * @return SeanceDTO
     */
    public List<SeanceDTO> findListeSeancePrecedente(final RechercheSeanceQO rechercheSeanceQO, final Integer nbrSeance);
    
    
    /**
     * Méthode de recherche d'une séance.
     *
     * @param rechercheSeanceQO Contient les paramètres de recherche.
     *
     * @return Une liste de séances
     *
     * @throws MetierException Exception
     */
    public ResultatDTO<List<ResultatRechercheSeanceDTO>> findSeance(RechercheSeanceQO rechercheSeanceQO)
        throws MetierException;

    /**
     * Méthode de recherche d'une séance pourle planning.
     *
     * @param rechercheSeanceQO Contient les paramètres de recherche.
     *
     * @return Une liste de séances
     *
     * @throws MetierException Exception
     */
    public ResultatDTO<List<SeanceDTO>> findSeanceForPlanning(
            RechercheSeanceQO rechercheSeanceQO) throws MetierException;
    
    /**
     * Sauvegarder (ajout/modif) d'une séance.
     *
     * @param saveSeanceQO L'ensemble des critères de la séance
     *
     * @return l'id de la séance ajoutée ou modifiée
     *
     */
    public Integer saveSeance(SeanceDTO saveSeanceQO);

    /**
     * Suppression d'une séance.
     *
     * @param resultatRechercheSeanceDTO Contient l'id de la séance à supprimer
     *
     */
    public void deleteSeance(SeanceDTO resultatRechercheSeanceDTO);
    
    /**
     * Suppression de plusieurs seances.
     *
     * @param listeSeances liste des seances DTO à supprimer.
     *
     */
    public void deleteListeSeances(final List<SeanceDTO> listeSeances);

    /**
     * Méthode de recherche des devoirs d'une seance.
     *
     * @param idSeance L'id de la séance.
     * @param isSchemaArchive archive.
     * @param isVisaArchive a.
     * @param exercice l'exercice.
     *
     * @return la liste des devoirs
     */
    public ResultatDTO<List<DevoirDTO>> trouverDevoir(final Integer idSeance,
            final Boolean isSchemaArchive, final boolean isVisaArchive,
            final String exercice);

    /**
     * Méthode de recherche des pièces jointes d'une seance.
     *
     * @param idSeance L'id de la séance.
     * @param archive archive.
     * @param isVisaArchive is.
     * @param exercice l'exercice.
     *
     * @return la liste des devoirs
     */
    public ResultatDTO<List<FileUploadDTO>> trouverPieceJointe(final Integer idSeance,
                                                            final Boolean archive,
                                                            final boolean isVisaArchive,
                                                            final String exercice);

    /**
     * Recherche toutes les séances d'une séquence.
     *
     * @param resultatRechercheSequenceDTO Contient l'id de la séquence
     *
     * @return Une liste de séance DTO
     */
    public List<SeanceDTO> findSeanceBySequence(ResultatRechercheSequenceDTO resultatRechercheSequenceDTO);

    /**
     * Retourne la liste des pièces jointes d'un devoir.
     *
     * @param idDevoir L'id du devoir.
     * @param archive archive.
     * @param isVisaArchive is.
     * @param exercice l'exercice.
     *
     * @return La lsite des pièces jointes
     *
     */
    public List<FileUploadDTO> trouverPieceJointeDevoir(final Integer idDevoir,
                                                     final Boolean archive,
                                                     final boolean isVisaArchive,
                                                     final String exercice);

    /**
     * Controle l'unicité d'une séance (enseignant, enseignement, classe/groupe,
     * intitulé, date).
     *
     * @param saveSeanceQO saveSeanceQO
     *
     * @return True si unique, sinon false.
     */
    public boolean checkUnicite(SeanceDTO saveSeanceQO);

    /**
     * Méthode d'affichage des séances.
     *
     * @param rechercheSeanceQO DOCUMENT ME!
     *
     * @return la liste des devoirs à afficher
     */
    public ResultatDTO<List<ResultatRechercheSeanceDTO>> listeSeanceAffichage(RechercheSeanceQO rechercheSeanceQO);


    
    /**
     * Recherche des seances de l'enseignant pour une semaine de l'emploi du temps.
     * La map contient une clef composée du code de la sequence avec la date et les heures,minutes de début puis les heures, minutes de fin,
     * et en valeur l'id de la séance associée.
     * @param dateDebut date de debut de la semaine.
     * @param dateFin date de fin de la semaine.
     * @param idEnseignant l'id de l'enseignant.
     * @param idSequence les id de sequence.
     * @return la map.   
     */
    public Map<String, Integer> findSeanceSemaine(final Date dateDebut,  final Date dateFin, 
            final Integer idEnseignant, final Set<Integer> idSequence);

    /**
     * Recherche toutes les seances de l'enseignant planifiées sur une plage donnée.
     * @param dateDebut date de début de plage
     * @param dateFin date de fin de plage
     * @param idEnseignant id de l'enseignant
     * @param idEtablissement id de l'etablissement
     * @param idSequence liste des id sequences 
     * @return la liste des seances trouvées
     */
    public List<SeanceDTO> findListeSeanceSemaine(final Date dateDebut,
            final Date dateFin,
            final Integer idEnseignant,
            final Integer idEtablissement,
            final Set<Integer> idSequence
            );
    
    
    /**
     * Recherche des seances future de l'enseignant.
     * @param dateDebut date de debut de la semaine.
     * @param dateFin date de fin de la semaine.
     * @param idEnseignant l'id de l'enseignant.
     * @param idSequence les id de sequence.
     * @return list de SeanceDTO.   
     */
    public List<SeanceDTO> findSeanceDTOSemaine(final Date dateDebut,  final Date dateFin, 
            final Integer idEnseignant, final Set<Integer> idSequence);
    
    /**
     * Recherche des seances de l'enseignant pour une semaine de l'emploi du temps antérieure à la date de modifiation.
     * La map contient une clef composée du code de la sequence avec la date et les heures,minutes de début puis les heures, minutes de fin,
     * et en valeur l'id de la séance associée.
     * @param dateDebut date de debut de la semaine.
     * @param dateFin date de fin de la semaine.
     * @param idEnseignant l'id de l'enseignant.
     * @param idSequence les id de sequence.
     * @param listeHoraire la grille horaire.
     * @return la map.   
     */
    public Map<String, SeanceDTO> findSeanceSemainePassee(final Date dateDebut,  final Date dateFin, 
            final Integer idEnseignant, final Set<Integer> idSequence, final List<GrilleHoraireDTO> listeHoraire);

    /**
     * Rechercher des seances pour un utilisateur quelquonque pour l'affichage dans la rechecherche des pages d'edition (seance et sequence).
     * @param rechercheSeancePrintQO le qo.
     * @return la liste des seances.
     */
    public ResultatDTO<List<PrintSequenceDTO>> findListeSeanceEdition(
            PrintSeanceOuSequenceQO rechercheSeancePrintQO);

    /**
     * Recherche l'alternance des semaines pour un établissment.
     * @param idEtablissement l'id de l'établissement.
     * @return une map qui associe à un numéro de semaine le type de semaine.
     */
    public Map<Integer, TypeSemaine> findAlternanceSemaines(Integer idEtablissement);

}
