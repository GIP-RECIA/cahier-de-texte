/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.Date;
import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.ArchiveSeanceDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.seance.RechercheVisaSeanceQO;
import org.crlr.dto.application.visa.DateListeVisaSeanceDTO;
import org.crlr.dto.application.visa.RechercheVisaQO;
import org.crlr.dto.application.visa.VisaDTO;
import org.crlr.dto.application.visa.VisaEnseignantDTO;
import org.crlr.exception.metier.MetierException;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface VisaHibernateBusinessService {

    /**
     * Charge pour chaque enseignant la liste des visa / cahiers de texte.
     * @param listeEnseignant liste des id des enseignants à charger. 
     * @return une liste de VisaEnseignantDTO pour chacun des enseignants passes en entree.
     */
    public ResultatDTO<List<VisaEnseignantDTO>> findListeVisaEnseignant(final List<EnseignantDTO> listeEnseignant);
    
    /**
     * Charge pour un enseignant la liste des visa / cahiers de texte.
     * @param rechercheVisa enseignant, profil, etablissement à charger. 
     * @return une liste de VisaDTO
     */
    public ResultatDTO<List<VisaDTO>> findListeVisa(final RechercheVisaQO rechercheVisa);
    
    
    /**
     * Sauve en base un visa dans la table cahier_visa.
     * @param visa le visa a sauver.
     */
    public void saveVisa(final VisaDTO visa);

    /**
     * Supprime toute la branche archive d'un visa.
     * Les tables suivantes sont supprimees : 
     *  - cahier_archive_piece_jointe_devoir
     *  - cahier_archive_piece_jointe_seance
     *  - cahier_archive_devoir
     *  - cahier_archive_seance
     *  - cahier_visa_seance
     *  @param visa le visa dont on veut supprimer les archives. 
     */
    public void supprimerArchiveVisa(final VisaDTO visa);
    
    /**
     * Cree pour le visa une archive de toutes les seances, devoirs et PJ 
     * ciblees par le cahier de texte correspondant au visa. 
     * @param visa le visa
     */
    public void archiverVisa(final VisaDTO visa);
    
    /**
     * {@inheritDoc}
     */
    public void supprimerArchiveVisa(final SeanceDTO seanceDTO);
    
    /**
     * {@inheritDoc}
     */
    public void supprimerVisaSeance(final SeanceDTO seanceDTO);

    
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
     * {@inheritDoc}
     */
    public ResultatDTO<ArchiveSeanceDTO> findArchiveSeance(final Integer idSeance, final Integer idVisa);
        
    
    /**
     * Mise a jour du visa. 
     * @param idVisa id du visa
     * @param profil profil  
     * @param typeVisa le type au format string
     * @param dateVisa date de maj
     */
    public void updateVisa(final Integer idVisa, final String profil, final String typeVisa, final String dateVisa);
 
   
    
    /**
     * @param idSeance c
     * @param dateMaj d 
     * @return r
     */
    public ResultatDTO<Integer> updateVisaDateMaj(Integer idSeance, Date dateMaj);
}
