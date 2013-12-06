/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.seance.RechercheVisaSeanceQO;
import org.crlr.dto.application.visa.DateListeVisaSeanceDTO;
import org.crlr.dto.application.visa.RechercheVisaQO;
import org.crlr.dto.application.visa.ResultatRechercheVisaSeanceDTO;
import org.crlr.dto.application.visa.VisaDTO;
import org.crlr.dto.application.visa.VisaEnseignantDTO;
import org.crlr.exception.metier.MetierException;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface VisaService {

    /**
     * Charge pour chaque enseignant la liste des derniers visa .
     * @param listeEnseignant liste des id des enseignants à charger. 
     * @return une liste de VisaEnseignantDTO pour chacun des enseignants passes en entree.
     */
    public ResultatDTO<List<VisaEnseignantDTO>> findListeVisaEnseignant(final List<EnseignantDTO> listeEnseignant);

    /**
     * Charge pour un enseignant la liste des visa / cahiers de texte.
     * @param rechercheVisa enseignant, etablissement et profil. 
     * @return une liste de VisaDTO
     */
    public ResultatDTO<List<VisaDTO>> findListeVisa(final RechercheVisaQO rechercheVisa);

    
    /**
     * Sauvegarde en base la liste des visas transmis.
     * @param listeVisa la liste des visas.
     * @return true / false si tout est ok.
     */
    public ResultatDTO<Boolean> saveListeVisa(final List<VisaDTO> listeVisa) throws MetierException;
    
    /**
     * Recherche un ensemble de seances correspondant au minimum a un enseignant en 
     * completant avec les info de visa.
     * @param rechercheSeanceQO les criteres.
     * @return une map classee par date de seance.
     * @throws MetierException une erreur
     */
    public ResultatDTO<List<DateListeVisaSeanceDTO>> findVisaSeance(RechercheVisaSeanceQO rechercheSeanceQO) 
        throws MetierException;
    
    /**
     * Enregistre la liste des visa pour les seances specifiees et un profil. 
     * @param listeVisaSeance la liste 
     * @return true / false.
     */
    public ResultatDTO<Boolean> saveListeVisaSeance(final List<ResultatRechercheVisaSeanceDTO> listeVisaSeance) throws MetierException;

    /**
     * Retourne le texte d'aide pour l'écran visaListe.
     * @return le texte au format html
     */
    public String getAideContextuelleListe();
    
    /**
     * Retourne le texte d'aide pour l'écran visaSeance.
     * @return le texte au format html
     */
    public String getAideContextuelleSeance();
}
