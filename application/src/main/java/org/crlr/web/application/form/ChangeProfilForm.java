package org.crlr.web.application.form;

import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.crlr.dto.application.base.Profil;


/**
 * Formulaire de changement de profil.
 *
 */
public class ChangeProfilForm extends AbstractForm{
    
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Type de profil. **/
     
    private Profil profil;
    
    List<SelectItem> profilList;
    /**
     * Constructeur.
     */
    public ChangeProfilForm() {
        super();
    }

	public boolean isMultiProfil(){
		return profilList != null && profilList.size() > 1;
	}

	public List<SelectItem> getProfilList() {
		return profilList;
	}

	public void setProfilList(List<SelectItem> profilList) {
		this.profilList = profilList;
	}

	public Profil getProfil() {
		return profil;
	}

	public void setProfil(Profil profil) {
		this.profil = profil;
	}
}
