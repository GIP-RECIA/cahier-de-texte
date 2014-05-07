/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequenceService.java,v 1.11 2010/04/12 01:34:33 ent_breyton Exp $
 */

package org.crlr.services;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.CouleurEnseignementClasseDTO;
import org.crlr.dto.application.sequence.RechercheCouleurEnseignementClasseQO;
import org.crlr.dto.application.sequence.SaveCouleurEnseignementClasseQO;
import org.crlr.exception.metier.MetierException;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.11 $
  */
public interface CouleurEnseignementClasseService {
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
