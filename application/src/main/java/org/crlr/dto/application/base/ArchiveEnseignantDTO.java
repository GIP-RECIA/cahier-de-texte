package org.crlr.dto.application.base;

import java.io.Serializable;
import java.util.List;

/**
 * Les informations de base sur les enseignants pour une annee scolaire.
 * ex: id d'un enseignant pour une année donnée
 */
public class ArchiveEnseignantDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6701713297407565392L;

	private AnneeScolaireDTO anneeScolaire;
	
	/**
	 * List des identifiants de l'enseignant pour l'annee scolaire
	 * Bizarement l'uid n'est pas définit unique par annee dans la base
	 *  
	 */
	private List<Integer> idEnsList ;
	
	/**
	 * list des etablissements ordonnée comme les uid
	 */
	private List<EtablissementDTO> etabList;
	
	private int idxSelectedEtab = 0;

	public ArchiveEnseignantDTO(AnneeScolaireDTO anneeScolaire) {
		this.anneeScolaire = anneeScolaire;
		
	}

	public List<EtablissementDTO> getEtabList() {
		return etabList;
	}

	public void setEtabList(List<EtablissementDTO> etabList) {
		this.etabList = etabList;
	}

	public AnneeScolaireDTO getAnneeScolaire() {
		return anneeScolaire;
	}

	public List<Integer> getIdEnsList() {
		return idEnsList;
	}

	public void setIdEnsList(List<Integer> idEnsList) {
		this.idEnsList = idEnsList;
	}

	/**
	 * Etablissement selectionné
	 * @return
	 */
	public EtablissementDTO getEtablissementSelected() {
		return (etabList == null || etabList.isEmpty() ) ? null : etabList.get(idxSelectedEtab);
	}
	
	public Integer getIdEtablissementSelected(){
		EtablissementDTO etab = getEtablissementSelected();
		return etab != null ? etab.getId() : null;
	}
	
	/**
	 * Identifiant de l'enseignant pour l'etablissement sélectionné
	 * @return
	 */
	public Integer getIdEnseignantSelected () {
		return (idEnsList == null || idEnsList.isEmpty()) ? null : idEnsList.get(idxSelectedEtab);
	}
	
	/**
	 * Selectionne un établissement
	 * @param idEtablissement
	 */
	public void selectedEtablissement(Integer idEtablissement) {
		
		if (etabList != null && idEtablissement != null) {
			int aux = 0;
			for (EtablissementDTO etab : etabList) {
				if (idEtablissement.equals(etab.getId())){
					idxSelectedEtab = aux;
					return ;
				}
				aux++;
			}
		}
		idxSelectedEtab = 0;
	}
	
}
