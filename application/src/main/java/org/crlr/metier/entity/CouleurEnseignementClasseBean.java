/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: CouleurEnseignementClasseBean.java,v 1.4 2010/03/29 09:29:36 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.annotations.Proxy;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_couleur_enseignement_classe", schema="cahier_courant")
public class CouleurEnseignementClasseBean implements Serializable {
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5671759340695026053L;

	/** DOCUMENTATION INCOMPLETE! */
    @Id
    private Integer id;

	@Column(name = "id_enseignant",  nullable = false)
    public Integer idEnseignant;
	
    @Column(name = "id_enseignement",  nullable = false)
    public Integer idEnseignement;
    
    @Column(name = "id_etablissement",  nullable = false)
    public Integer idEtablissement;
    
    @Column(name = "id_groupe", nullable = true)
    public Integer idGroupe;
    
    @Column(name = "id_classe",  nullable = true)
    public Integer idClasse;
    
    /** DOCUMENTATION INCOMPLETE! */
    @Column(name = "couleur", nullable = false)
    private String couleur;

    /**
     * 
     * Constructeur.
     */
    public CouleurEnseignementClasseBean() {
    }
    
    
    
    
    /**
     * Constructeur:
     * @param idEnseignant l'identifiant de l'enseignant.
     * @param idEnseignement identifiant de l'enseignement.
     * @param idEtablissement identifiant de l'établissement.
     * @param idGoupe identifiant du groupe.
     * @param idClasse identifiant de la classe.
     */
    public CouleurEnseignementClasseBean(Integer idEnseignant, Integer idEnseignement, Integer idEtablissement, Integer idGroupe, Integer idClasse) {
    	this.idEnseignant = idEnseignant;
        this.idEnseignement = idEnseignement;
        this.idEtablissement = idEtablissement;
        this.idGroupe = idGroupe;
        this.idClasse = idClasse;
    }    

	/**
     * Accesseur id.
     * @return id.
     */
    public Integer getId() {
		return id;
	}
    
    /**
     * Mutateur id.
     * @param id id à modifier.
     */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
     * Accesseur idEnseignant.
     * @return le idEnseignant.
     */
    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    /**
     * Mutateur idEnseignant.
     * @param idEnseignant le idEnseignant à modifier.
     */
    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    /**
     * Accesseur idEnseignement.
     * @return le idEnseignement.
     */
    public Integer getIdEnseignement() {
        return idEnseignement;
    }

    /**
     * Mutateur idEnseignement.
     * @param idEnseignement le idEnseignement à modifier.
     */
    public void setIdEnseignement(Integer idEnseignement) {
        this.idEnseignement = idEnseignement;
    }

    /**
     * Accesseur idEtablissement.
     * @return le idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Mutateur de idEtablissement.
     * @param idEtablissement le idEtablissement à modifier.
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur idGroupe.
     * @return le idGroupe
     */
    public Integer getIdGroupe() {
        return idGroupe;
    }

    /**
     * Mutateur de idGroupe.
     * @param idGroupe le idGroupe à modifier.
     */
    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    /**
     * Accesseur idClasse.
     * @return le idClasse
     */
    public Integer getIdClasse() {
        return idClasse;
    }

    /**
     * Mutateur de idClasse.
     * @param idClasse le idClasse à modifier.
     */
    public void setIdClasse(Integer idClasse) {
        this.idClasse = idClasse;
    }


    /**
     * Couleur.
     *
     * @return the Couleur attribute
     */
    public String getCouleur() {
        return couleur;
    }

    /**
     * Couleur.
     *
     * @param Couleur  de la couleur.
     */
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    /**
    *
    *
    * @return DOCUMENTATION INCOMPLETE!
    */
   public int hashCode() {
   	
       int _hashCode = 0;
       if (this.id!= null) {
           _hashCode += this.id.hashCode();
       }
       if (this.idEnseignement != null) {
           _hashCode += this.idEnseignement.hashCode();
       }
       if (this.idEtablissement != null) {
           _hashCode += this.idEtablissement.hashCode();
       }
       if (this.idEnseignement!= null) {
           _hashCode += this.idEnseignement.hashCode();
       }
       if (this.idGroupe!= null) {
           _hashCode += this.idGroupe.hashCode();
       }
       if (this.idClasse!= null) {
           _hashCode += this.idClasse.hashCode();
       }
       if (this.couleur!= null) {
           _hashCode += this.couleur.hashCode();
       }
       return _hashCode;

   }
   
   /**
   *
   *
   * @param obj DOCUMENTATION INCOMPLETE!
   *
   * @return DOCUMENTATION INCOMPLETE!
   */
  public boolean equals(final Object obj) {
      if (!(obj instanceof CouleurEnseignementClasseBean)) {
          return false;
      }

      final CouleurEnseignementClasseBean rhs = (CouleurEnseignementClasseBean) obj;
      
      return new EqualsBuilder().append(getIdEnseignant(), rhs.getIdEnseignant())
              .append(getIdEnseignement(), rhs.getIdEnseignement())
              .append(getIdEtablissement(), rhs.getIdEtablissement())
              .append(getIdGroupe(), rhs.getIdGroupe())
              .append(getIdClasse(), rhs.getIdClasse())
              .isEquals();
      
  }

	@Override
	public String toString() {
		return "CouleurEnseignementClasseBean [id=" + id + ", idEnseignant="
				+ idEnseignant + ", idEnseignement=" + idEnseignement
				+ ", idEtablissement=" + idEtablissement + ", idGroupe=" + idGroupe
				+ ", idClasse=" + idClasse + ", couleur=" + couleur + "]";
	}

}
