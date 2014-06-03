package org.crlr.metier.business;

import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.ArchiveEnseignantDTO;
import org.crlr.exception.metier.MetierException;

public interface ArchiveEnseignantBusinessService {
	
	/**
	 * Donne pour une annee scolaire l'archiveEnseigantDTO correspondant a l'uid.
	 * Peut être null.
	 * @param anneeScolaire
	 * @param uid
	 * @return
	 * @throws MetierException 
	 */
	public ArchiveEnseignantDTO findArchiveByUid(AnneeScolaireDTO anneeScolaire, String uid) throws MetierException;
}
