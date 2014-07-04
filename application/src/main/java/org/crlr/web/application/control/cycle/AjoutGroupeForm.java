package org.crlr.web.application.control.cycle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.application.form.AbstractPopupForm;

import com.opensymphony.oscache.util.StringUtil;

/**
 * Formulaire de d'ajout/consultation des groupes collaboratifs locaux.
 * @author G-SAFIR-FRMP
 *
 */
public class AjoutGroupeForm extends AbstractPopupForm {

   
   
    
  
    /**
	 * 
	 */
	private static final long serialVersionUID = -5189940066681660871L;


	/** Liste de tous les groupes collaboratif. */
    private List<GroupeDTO> listeGroupe; 
    
    
    /** id de Groupe selectionné dans la liste des groupes. */
    private Integer selectedIdGroup;
    
    private String prefix;
    /**
     * Groupe en cours d'édition 
     */
    private GroupeDTO groupEdited;
    
    /** Liste des enseignants du groupe selectionnee.*/
    private List<String> membresUid;
    
    private boolean membresChange;
    /**
     * Liste des enseignant de l'établissement.
     */
    private List<EnseignantDTO> enseignants;
    
    /** Filtre applique sur la designation groupe dans la popupGroupe. */
    private String filtreGroupe;
    
   
    /** Indique si on est un sous ecran ou en entree directe. */
    private Boolean afficheRetour; 
    
    
    /**
     * indique si on doit afficher l'alerte de suppression.
     * Cas ou on supprime tous les enseignants d'un groupes.
     */
    private boolean alertSuppression;
    
    /**
     * Reset les champ du formulaire.
     */
    public void reset() {
        mode = AbstractForm.MODE_DEFAUT;
    }
    
    /**
     * Constructeur.
     */
    public AjoutGroupeForm() {
        super();
        reset();
    }


    
    
    /**
     * Retourne le libelle a afficher pour le titre de l'ecran en fonction
     * du mode (ajout/modif/consultation).
     * @return un titre.
     */
    public String getTitreEcran() {
        if (mode == AbstractForm.MODE_AJOUT) {
            return "Ajout d'un groupe collaboratif local";
        } else {
            return "Administration des groupes collaboratifs locaux";
        }
    }
    
    /**
     * Accesseur de listeGroupe {@link #listeGroupe}.
     * @return retourne listeGroupe
     */
    public List<GroupeDTO> getListeGroupe() {
        return listeGroupe;
    }

    /**
     * Mutateur de listeGroupe {@link #listeGroupe}.
     * @param listeGroupe le listeGroupe to set
     */
    public void setListeGroupe(List<GroupeDTO> listeGroupe) {
        this.listeGroupe = listeGroupe;
    }

   
    /**
     * Accesseur de filtreGroupe {@link #filtreGroupe}.
     * @return retourne filtreGroupe
     */
    public String getFiltreGroupe() {
        return filtreGroupe;
    }

    /**
     * Mutateur de filtreGroupe {@link #filtreGroupe}.
     * @param filtreGroupe le filtreGroupe to set
     */
    public void setFiltreGroupe(String filtreGroupe) {
        this.filtreGroupe = filtreGroupe;
    }

  


    
   

    
    /**
     * Accesseur de afficheRetour {@link #afficheRetour}.
     * @return retourne afficheRetour
     */
    public Boolean getAfficheRetour() {
        return afficheRetour;
    }

    /**
     * Mutateur de afficheRetour {@link #afficheRetour}.
     * @param afficheRetour le afficheRetour to set
     */
    public void setAfficheRetour(Boolean afficheRetour) {
        this.afficheRetour = afficheRetour;
    }

   
	public GroupeDTO getGroupEdited() {
		return groupEdited;
	}

	public void setGroupEdited(GroupeDTO groupEdited) {
		this.groupEdited = groupEdited;
	}



	public List<EnseignantDTO> getEnseignants() {
		return enseignants;
	}

	public void setEnseignants(List<EnseignantDTO> enseignants) {
		this.enseignants = enseignants;
	}

	public List<String> getMembresUid() {
		return membresUid;
	}

	public void setMembresUid(List<String> membresUid) {
		membresChange = membresChange || 
						this.membresUid == null ||
						! membresUid.containsAll(this.membresUid) ||
						! this.membresUid.containsAll(membresUid);
		this.membresUid = membresUid;
		
	}

	public Integer getSelectedIdGroup() {
		return selectedIdGroup;
	}

	public void setSelectedIdGroup(Integer selectedIdGroup) {
		this.selectedIdGroup = selectedIdGroup;
	}

	public List<EnseignantDTO> getFilteredList() {        
	    List<EnseignantDTO> filteredList = new ArrayList<EnseignantDTO>();
	    
	    String prefix = getPrefix();
	    
	    if (enseignants != null) {
	    	
	    	if (prefix == null) {
	    		return enseignants;
	    	}
	    	prefix = prefix.toUpperCase();
	    	
		    for (EnseignantDTO ens : enseignants) {
		    	String s = ens.getNom().toUpperCase();
		    	String uid = ens.getUid();
		    	
		        if (s.startsWith(prefix)) {
		            filteredList.add(ens);
		        } else {
		        	if (membresUid.contains(uid)) {
		        		filteredList.add(ens);
		        	}
		        }
		    }
	    }
	    return filteredList;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public boolean isAlertSuppression() {
		return alertSuppression;
	}

	public void setAlertSuppression(boolean alertSuppression) {
		this.alertSuppression = alertSuppression;
	}

	public boolean isMembresChange() {
		return membresChange;
	}

	public void setMembresChange(boolean membresChange) {
		this.membresChange = membresChange;
	}

    
}
