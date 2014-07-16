package org.crlr.services;

import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.ArchiveEnseignantDTO;
import org.crlr.dto.application.base.ArchiveEnseignantQO;
import org.crlr.exception.metier.MetierException;

public interface ArchiveEnseignantService {
	/**
	 * Return la liste des établissements d'un enseignant par année scolaire.
	 * 
	 * @param archiveEnseignantQO
	 * @return
	 * @throws MetierException 
	 */
	public ResultatDTO<List<ArchiveEnseignantDTO>> findAllEtabByUid(ArchiveEnseignantQO archiveEnseignantQO) throws MetierException;

	
	
}
