package org.crlr.services;

import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.ArchiveEnseignantDTO;
import org.crlr.dto.application.base.ArchiveEnseignantQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.ArchiveEnseignantFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArchiveEnseignantDelegate implements ArchiveEnseignantService {

	@Autowired
	private ArchiveEnseignantFacadeService archiveEnseignantFacadeService;
	
	@Override 
	public ResultatDTO<List<ArchiveEnseignantDTO>> findAllEtabByUid(
			ArchiveEnseignantQO archiveEnseignantQO)throws MetierException {
		return archiveEnseignantFacadeService.findAllEtabByUid(archiveEnseignantQO);
	}

}
