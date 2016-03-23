/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.DateDTO;
import org.crlr.dto.application.base.PeriodeEmploiDTO;
import org.crlr.dto.application.base.PrintEmploiQO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.dto.application.emploi.RechercheEmploiQO;
import org.crlr.dto.application.emploi.SaveEmploiQO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.dto.application.sequence.RechercheSequenceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.report.impl.PdfReport;
import org.crlr.web.dto.GrilleHoraireDTO;
import org.crlr.web.dto.TypeSemaine;

/**
 * EtablissementService.
 *
 * @author $carriere$
 * @version $Revision: 1.6 $
  */
public interface EmploiService {
    /**
     * Sauvegarde de l'emploi du temps.
     *
     * @param saveEmploiQO les paramètres.
     *
     * @return message acquittement.
     *
     * @throws MetierException Exception.
     */
    public ResultatDTO<Date> saveEmploiDuTemps(final SaveEmploiQO saveEmploiQO)
                                           throws MetierException;

 
    
    /**
     * Retourne les enregistrement pour la constitution de l'emploi du temps.
     *
     * @param idEnseignant l'id de l'enseignant
     * @param idEtablissement l'id de l'établissement
     * @param typeSemaine le type de semaine
     * @param date 
     *
     * @return une liste de DetailJoursEmploiDTO contenant les info nécessaire à la
     *         constitution de l'emploi du temps
     */
    public ResultatDTO<List<DetailJourEmploiDTO>> findEmploi(final Integer idEnseignant,
                                                             final Integer idEtablissement,
                                                             final TypeSemaine typeSemaine,
                                                             final Integer idPeriode ) ;

    /**
     * Retourne les enregistrement pour la consolidation de l'emploi du temps par
     * classe.
     *
     * @param rechercheEmploiQO les paramètres de recherche
     *
     * @return une liste de DetailJoursEmploiDTO contenant les info nécessaire à la
     *         constitution de l'emploi du temps
     *
     * @throws MetierException l'exception potentielle.
     */
    public ResultatDTO<List<DetailJourEmploiDTO>> findEmploiConsolidation(final RechercheEmploiQO rechercheEmploiQO)
        throws MetierException;

    /**
     * Recherche des seances de l'enseignant pour une semaine de l'emploi du
     * temps. La map contient une clef composée du code de la sequence avec la date et
     * les heures,minutes de début puis les heures, minutes de fin, et en valeur l'id de
     * la séance associée.
     *
     * @param rechercheSeanceQO les paramètres.
     *
     * @return la map.
     */
    public Map<String, Integer> findSeanceEmploiSemaine(final RechercheSeanceQO rechercheSeanceQO);

    /**
     * Recherche des seances de l'enseignant pour une semaine de l'emploi du
     * temps. 
     *
     * @param rechercheSeanceQO les paramètres.
     * @return la map.
     */
    public List<SeanceDTO> findListeSeanceSemaine(final RechercheSeanceQO rechercheSeanceQO);
    
    /**
     * Recherche des seances de l'enseignant pour une semaine de l'emploi du
     * temps antérieure à la date de dernier enregistrement. La map contient une clef
     * composée du code de la sequence avec la date et les heures,minutes de début puis
     * les heures, minutes de fin, et en valeur l'id de la séance associée.
     *
     * @param rechercheSeanceQO les paramètres.
     * @param listeHoraire DOCUMENT ME!
     *
     * @return la map.
     */
    public Map<String, SeanceDTO> findSeanceEmploiSemainePassee(final RechercheSeanceQO rechercheSeanceQO,
                                                                final List<GrilleHoraireDTO> listeHoraire);

    
    /**
     * Edition de l'emploi du temps.
     *
     * @param printEmploiQO les paramètres.
     *
     * @return le pdf.
     */
    public PdfReport printEmploiDuTemps(final PrintEmploiQO printEmploiQO);
    
    
   
    /**
     * Recherche les sequence active pour une semaine. 
     * @param rechercheSequence les champs suivants doivent être alimentés :
     *  - dateDebut, dateFin, dEnseignant, idEtablissement.
     * @return la liste des sequence actives correspondant aux criteres
     */
    public List<SequenceDTO> chercherSequenceSemaine(RechercheSequenceQO rechercheSequence);
    
    /**
     * Recupere le texte d'aide contextuelle dans le fichier de propriété.
     * @return le texte d'aide contextuelle.
     */
    public String getAideContextuelle();
    
    public List<PeriodeEmploiDTO> findPeriodes(final Integer enseignantId, final Integer etablissementId);
    
    /**
     * Recherche les prochaines dates d'emploi du temps correspondant aux memes : 
     * - classe (ou groupe)
     * - enseignant
     * - enseignement
     * à partir d'une date de début (non inclue) dans la limite de 10. 
     * 
     * @param rechercheEmploi : les criteres 
     * @return une liste de Date
     */
    public List<DateDTO> findProchaineDate(RechercheEmploiQO rechercheEmploi);
    
}
