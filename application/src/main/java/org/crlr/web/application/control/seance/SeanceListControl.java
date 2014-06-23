package org.crlr.web.application.control.seance;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.crlr.dto.application.seance.PrintSeanceDTO;
import org.crlr.dto.application.sequence.PrintSequenceDTO;

/**
 * 
 * Maintient une liste de séances ou de séquences
 * pour partage avec les éditions pdf.
 * 
 * @author legay
 *
 */
@ManagedBean(name = "listeSeances")
@SessionScoped
public class SeanceListControl implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4579366193519880230L;

	private List<PrintSeanceDTO> listSeances;
	
	private List<PrintSequenceDTO> listSequences;

	public List<PrintSeanceDTO> getListSeances() {
		return listSeances;
	}

	public void setListSeances(List<PrintSeanceDTO> listSeances) {
		this.listSeances = listSeances;
	}

	public List<PrintSequenceDTO> getListSequences() {
		return listSequences;
	}

	public void setListSequences(List<PrintSequenceDTO> listSequences) {
		this.listSequences = listSequences;
	}
	
	
}
