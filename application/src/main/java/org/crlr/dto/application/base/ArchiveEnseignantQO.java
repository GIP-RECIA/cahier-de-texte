package org.crlr.dto.application.base;

import java.io.Serializable;
import java.util.List;

public class ArchiveEnseignantQO  implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9133615752035675752L;

	private List<AnneeScolaireDTO> anneeScolaireList;
	
	private String uid;

	public List<AnneeScolaireDTO> getAnneeScolaireList() {
		return anneeScolaireList;
	}

	public void setAnneeScolaireList(List<AnneeScolaireDTO> anneeScolaireList) {
		this.anneeScolaireList = anneeScolaireList;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	} 
	
}
