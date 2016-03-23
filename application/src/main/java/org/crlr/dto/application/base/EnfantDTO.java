package org.crlr.dto.application.base;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * Dto représentant les données importantes d'un enfant.
 *
 */
public class EnfantDTO implements Serializable, Identifiable {

	 /**
	 * 
	 */
	private static final long serialVersionUID = -4166254748739987971L;

	/** Identifiant en base de données. */
	private Integer id;    
    
    /** Nom de l'individu. */
    private String nom;

    /** Prénom de l'individu. */
    private String prenom;  
    
    /**
     * Accesseur identifiant.
     *
     * @return le identifiant.
     */
	public Integer getId() {
		return id;
	}  
	
	/**
     * Mutateur id.
     * @param id le id à modifier.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur nom.
     *
     * @return le nom.
     */
	public String getNom() {
		return nom;
	}

	/**
     * Mutateur nom.
     *
     * @param nom le nom à modifier.
     */
	public void setNom(String nom) {
		this.nom = nom;
	}

	 /**
     * Accesseur prenom.
     *
     * @return le prenom.
     */
	public String getPrenom() {
		return prenom;
	}

	/**
     * Mutateur prenom.
     *
     * @param prenom le prenom à modifier.
     */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	/**
     *  @return implemente pour le SelectOneDtoConverter. 
     */
    @Override
    public int hashCode(){
        return new HashCodeBuilder()
            .append(id)
            .toHashCode();
    }
    
    /** 
     * Nécesaire pour SelectOneDtoConverter.
     * @param obj : autre obj a comparer avec this.
     * @return true / false selon que les objets sont identiques.
     */
    @Override
    public boolean equals(final Object obj){
        if(obj instanceof EnfantDTO){
            final EnfantDTO other = (EnfantDTO) obj;
            return new EqualsBuilder()
                .append(id, other.id)
                .isEquals();
        } else{
            return false;
        }
    }     
}
