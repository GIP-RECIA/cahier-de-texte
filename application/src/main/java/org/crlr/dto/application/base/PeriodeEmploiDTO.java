/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PeriodeEmploiBean.java,v 1.2 2010/04/12 04:30:13 jerome.carriere Exp $
 */
package org.crlr.dto.application.base;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.crlr.utils.DateUtils;

public class PeriodeEmploiDTO  implements Serializable, Identifiable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7691467319675633439L;

	/** Identifiant de la periode. */

	private Integer id;

	/**
	 * Date de debut de periode. La date de fin est la date de debut de periode
	 * suivante moins un jour.
	 */
	private Date dateDebut;

	/** Identifiant de l'etablissement. */
	private Integer idEtablissement;

	private Integer idEnseignant;

	/**
	 * Constructeur.
	 */
	public PeriodeEmploiDTO() {
	}

	/**
	 * Accesseur de id {@link #id}.
	 * 
	 * @return retourne id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Mutateur de id {@link #id}.
	 * 
	 * @param id
	 *            le id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Accesseur de dateDebut {@link #dateDebut}.
	 * 
	 * @return retourne dateDebut
	 */
	public Date getDateDebut() {
		return dateDebut;
	}

	/**
	 * Mutateur de dateDebut {@link #dateDebut}.
	 * 
	 * @param dateDebut
	 *            le dateDebut to set
	 */
	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	/**
	 * Accesseur de idEtablissement {@link #idEtablissement}.
	 * 
	 * @return retourne idEtablissement
	 */
	public Integer getIdEtablissement() {
		return idEtablissement;
	}

	/**
	 * Mutateur de idEtablissement {@link #idEtablissement}.
	 * 
	 * @param idEtablissement
	 *            le idEtablissement to set
	 */
	public void setIdEtablissement(Integer idEtablissement) {
		this.idEtablissement = idEtablissement;
	}

	/**
	 * Accesseur de idEnseignant {@link #idEnseignant}.
	 * 
	 * @return retourne idEnseignant
	 */
	public Integer getIdEnseignant() {
		return idEnseignant;
	}

	/**
	 * Mutateur de idEnseignant {@link #idEnseignant}.
	 * 
	 * @param idEnseignant
	 *            le idEnseignant to set
	 */
	public void setIdEnseignant(Integer idEnseignant) {
		this.idEnseignant = idEnseignant;
	}
	
	@Override
	public int hashCode(){
	    return new HashCodeBuilder()
	        .append(id)
	        .toHashCode();
	}

	//Nécesaire pour SelectOneDtoConverter
	@Override
	public boolean equals(final Object obj){
	    if(obj instanceof PeriodeEmploiDTO){
	        final PeriodeEmploiDTO other = (PeriodeEmploiDTO) obj;
	        return new EqualsBuilder()
	            .append(id, other.id)
	            .isEquals();
	    } else{
	        return false;
	    }
	}
	
	/**
	 * @param listePeriode une liste EN ORDRE plus tot vers plus tard.
	 * @param date d
	 * @return la période ou null
	 */
	public static PeriodeEmploiDTO findPeriodePourDate(List<PeriodeEmploiDTO> listePeriode, Date date) {
        
        if (CollectionUtils.isEmpty(listePeriode)) {
            return null;
        }
        for(int i = listePeriode.size() - 1; i >= 0; --i) {
            if (DateUtils.lessOrEquals(listePeriode.get(i).getDateDebut(), date)) {
                return listePeriode.get(i);
            }            
        }   
        
        return listePeriode.get(0);
	}

}
