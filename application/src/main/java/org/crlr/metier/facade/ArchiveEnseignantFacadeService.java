package org.crlr.metier.facade;

import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.ArchiveEnseignantDTO;
import org.crlr.dto.application.base.ArchiveEnseignantQO;
import org.crlr.exception.metier.MetierException;

public interface ArchiveEnseignantFacadeService {

	public ResultatDTO<List<ArchiveEnseignantDTO>> findAllEtabByUid(ArchiveEnseignantQO archiveEnseignantQO) throws MetierException;
}
