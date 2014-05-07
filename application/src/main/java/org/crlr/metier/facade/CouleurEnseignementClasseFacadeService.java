/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequenceFacadeService.java,v 1.11 2010/04/12 01:34:33 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.List;
import java.util.Set;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.CouleurEnseignementClasseDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.RechercheEnseignementPopupQO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.sequence.PrintSeanceOuSequenceQO;
import org.crlr.dto.application.sequence.PrintSequenceDTO;
import org.crlr.dto.application.sequence.RechercheCouleurEnseignementClasseQO;
import org.crlr.dto.application.sequence.RechercheSequencePopupQO;
import org.crlr.dto.application.sequence.RechercheSequenceQO;
import org.crlr.dto.application.sequence.ResultatRechercheSequenceDTO;
import org.crlr.dto.application.sequence.SaveCouleurEnseignementClasseQO;
import org.crlr.dto.application.sequence.SaveSequenceQO;
import org.crlr.dto.application.sequence.SaveSequenceSimplifieeQO;
import org.crlr.dto.application.sequence.SequenceAffichageQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.metier.entity.SequenceBean;
import org.crlr.report.Report;

/**
 * Interface de la Façade Sequence.
 *
 * @author breytond
 * @version $Revision: 1.11 $
  */
public interface CouleurEnseignementClasseFacadeService {
    /**
     * Méthode de recherche des associations enseignant / etablissement / enseignement / groupe (ou) classe / couleur.
     *
     * @param rechercheCouleurEnseignementClasseQO Les paramètres de recherche
     *
     * @return L'association enseignant / etablissement / enseignement / classe (ou) groupe / couleur. 
     *
     * @throws MetierException DOCUMENTATION INCOMPLETE!
     */
    public ResultatDTO<CouleurEnseignementClasseDTO> findCouleurEnseignementClasse(final RechercheCouleurEnseignementClasseQO rechercheCouleurEnseignementClasseQO)
        throws MetierException;

    /**
     * Méthode d'ajout/sauvegarde d'une couleur pour une association enseignant / etablissement / classe (ou) groupe.
     *
     * @param saveSequenceQO Contient les infos de la séquence
     *
     * @throws MetierException Exception
     */
    public void saveCouleurEnseignementClasse(SaveCouleurEnseignementClasseQO saveCouleurEnseignementClasseQO)
                         throws MetierException;

}
