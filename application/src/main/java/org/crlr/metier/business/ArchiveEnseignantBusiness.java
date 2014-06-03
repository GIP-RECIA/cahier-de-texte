package org.crlr.metier.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.ArchiveEnseignantDTO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.utils.SchemaUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ArchiveEnseignantBusiness extends AbstractBusiness implements
		ArchiveEnseignantBusinessService {

	@Override
	public ArchiveEnseignantDTO findArchiveByUid(
			AnneeScolaireDTO anneeScolaire, String uid) throws MetierException {
		// TODO Auto-generated method stub
		String exercice = anneeScolaire.getExercice();
		final String schema = SchemaUtils.getSchemaCourantOuArchive(true, exercice);
		
		final String queryText = "SELECT ENS.id idEns, ETAB.id idEtab, ETAB.designation nomEtab, ETAB.code siren " +
				"FROM  " + 
				SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant ENS, ") + 
				SchemaUtils.getTableAvecSchema(schema, "cahier_etab_enseignant EE, ") +
				SchemaUtils.getTableAvecSchema(schema, "cahier_etablissement ETAB ") +
				"WHERE ENS.uid = :uid " +
				"AND EE.id_enseignant = ENS.id " +
				"AND EE.id_etablissement = ETAB.id " +
				"ORDER BY ETAB.designation ";
				
		Query query = getEntityManager().createNativeQuery(queryText);
		
		query.setParameter("uid", uid);
		
		@SuppressWarnings("unchecked")
		List<Object[]> tuplesList = query.getResultList();
		
		ArchiveEnseignantDTO archive = null;
		
		if (tuplesList != null) {
			
			archive = new ArchiveEnseignantDTO(anneeScolaire);
			List<EtablissementDTO> etabList = new ArrayList<EtablissementDTO>(tuplesList.size());
			List<Integer> ensList = new ArrayList<Integer>(tuplesList.size());
			archive.setEtabList(etabList);
			archive.setIdEnsList(ensList);
		
			for (Object[] tuple : tuplesList) {
				Integer idEns = (Integer) tuple[0];
				Integer idEtab = (Integer) tuple[1];
				String nomEtab = (String) tuple[2];
				String siren = (String) tuple[3];
				
				EtablissementDTO etab = new EtablissementDTO();
				etab.setId(idEtab);
				etab.setCode(siren);
				etab.setDesignation(nomEtab);
				etabList.add(etab);	
				ensList.add(idEns);
				
			}
		}

		return archive;
	}
}
