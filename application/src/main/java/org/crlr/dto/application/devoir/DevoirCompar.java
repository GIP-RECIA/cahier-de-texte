package org.crlr.dto.application.devoir;

import java.util.Date;

public class DevoirCompar {
	Date dateRemise;
	String intitule;
	String description;
	Integer idTypeDevoir;
	Integer idSeance;

	public DevoirCompar(DevoirDTO devoirDTO) {
		dateRemise= devoirDTO.getDateRemise();
		intitule = devoirDTO.getIntitule();
		description = devoirDTO.getDescription();
		idTypeDevoir = devoirDTO.getTypeDevoirDTO().getId();
		idSeance = devoirDTO.getSeance().getId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateRemise == null) ? 0 : dateRemise.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((idSeance == null) ? 0 : idSeance.hashCode());
		result = prime * result
				+ ((idTypeDevoir == null) ? 0 : idTypeDevoir.hashCode());
		result = prime * result
				+ ((intitule == null) ? 0 : intitule.hashCode());
		return result;
	}

	public boolean equiv(Object obj) {
		if (obj == null)
			return false;
		if (! (obj instanceof DevoirDTO))
			return equals(obj);
		DevoirDTO other = (DevoirDTO) obj;
		
		if (idSeance == null) {
			if (other.getSeance().getId() != null)
				return false;
		
		if (dateRemise == null) {
			if (other.getDateRemise() != null)
				return false;
		} else if (!dateRemise.equals(other.getDateRemise()))
			return false;
		if (description == null) {
			if (other.getDescription() != null)
				return false;
		} else if (!description.equals(other.getDescription()))
			return false;
		
		} else if (!idSeance.equals(other.getSeance().getId()))
			return false;
		if (idTypeDevoir == null) {
			if (other.getTypeDevoirDTO().getId() != null)
				return false;
		} else if (!idTypeDevoir.equals(other.getTypeDevoirDTO().getId()))
			return false;
		if (intitule == null) {
			if (other.getIntitule() != null)
				return false;
		} else if (!intitule.equals(other.getIntitule()))
			return false;
		return true;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevoirCompar other = (DevoirCompar) obj;
		if (idSeance == null) {
			if (other.idSeance != null) return false;
		} else if (!idSeance.equals(other.idSeance)) return false;
		
		if (idTypeDevoir == null) {
			if (other.idTypeDevoir != null) return false;
		} else if (!idTypeDevoir.equals(other.idTypeDevoir)) return false;
		
		
		if (dateRemise == null) {
			if (other.dateRemise != null) return false;
		} else if (!dateRemise.equals(other.dateRemise)) return false;

		if (intitule == null) {
			if (other.intitule != null) return false;
		} else if (!intitule.equals(other.intitule)) return false;
		
		if (description == null) {
			if (other.description != null) return false;
		} else if (!description.equals(other.description))	return false;
		
		return true;
	}

	
}
