package org.crlr.metier.facade;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.ArchiveEnseignantDTO;
import org.crlr.dto.application.base.ArchiveEnseignantQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.metier.business.AnneeScolaireHibernateBusinessService;
import org.crlr.metier.business.ArchiveEnseignantBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional(
        readOnly = true     
)
public class ArchiveEnseignantFacade implements ArchiveEnseignantFacadeService {

	@Autowired
	private AnneeScolaireHibernateBusinessService anneeScolaireService;

	@Autowired
	private ArchiveEnseignantBusinessService archiveEnseignantService;
	
	
	@Override
	public ResultatDTO<List<ArchiveEnseignantDTO>> findAllEtabByUid(
			ArchiveEnseignantQO archiveEnseignantQO) throws MetierException {
		
		String uid = archiveEnseignantQO.getUid();
		List<AnneeScolaireDTO> anneesScolaireList = archiveEnseignantQO.getAnneeScolaireList();
		if (uid == null) {
			throw new MetierException("findAllEtabByUid with uid null");
		}
		if (anneesScolaireList == null) {
			anneesScolaireList =anneeScolaireService.findListeAnneeScolaire(true).getValeurDTO();	
		}
		
		
		List<ArchiveEnseignantDTO> listRes = new ArrayList<ArchiveEnseignantDTO>(anneesScolaireList.size());
		for (AnneeScolaireDTO anneeScolaireDTO : anneesScolaireList) {
			ArchiveEnseignantDTO archiveEns = archiveEnseignantService.findArchiveByUid(anneeScolaireDTO, uid);
			if (archiveEns != null) {
				listRes.add(archiveEns);
			}
		}
		
		return new ResultatDTO<List<ArchiveEnseignantDTO>>(listRes, null);
	}

}
