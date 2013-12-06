/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequenceDTO.java,v 1.5 2009/04/28 08:02:29 vibertd Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.crlr.utils.DateUtils;

/**
 * Un DOT pour contenir une sequence.
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
public class DateDTO implements Serializable, Identifiable {

    /**  Serial.  */
    private static final long serialVersionUID = -1571516101450767821L;

    /** Id. */
    private Integer id;

    /** Libelle. */
    private String libelle;

    /** Date. */
    private Date date;

    /**
     * Contstruceur.
     * @param date la date
     */
    public DateDTO(final Date date) {
        super();
        if (date == null) {
            this.date = null;
            this.id = 0;
            this.libelle = "";
        } else {
            this.date = date;
            this.id = Integer.parseInt(DateUtils.format(date, "yyyyMMdd"));
            this.libelle = DateUtils.format(date, "dd/MM/yyyy");
        }
    }

    /**
     * Contstructeur par defaut.
     */
    public DateDTO() {
        super();
        this.date = null;
        this.id = 0;
        this.libelle = "";
    }
    
    

    /**
     * Accesseur de id {@link #id}.
     * @return retourne id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur de id {@link #id}.
     * @param id le id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur de libelle {@link #libelle}.
     * @return retourne libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Mutateur de libelle {@link #libelle}.
     * @param libelle le libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Accesseur de date {@link #date}.
     * @return retourne date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Mutateur de date {@link #date}.
     * @param date le date to set
     */
    public void setDate(Date date) {
        this.date = date;
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
        if(obj instanceof DateDTO){
            final DateDTO other = (DateDTO) obj;
            return new EqualsBuilder()
                .append(id, other.id)
                .isEquals();
        } else{
            return false;
        }
    }
    
    /**
     * Verifie si la date passée en parametre est contenue dans la liste d'objet de type DateDTO.
     * @param listeDateDTO la liste 
     * @param date la date  
     * @return true / false selon que la date ai été trouvée ou non.
     */
    public static Boolean containsDateDTO(List<DateDTO> listeDateDTO, String date) {
        if (listeDateDTO == null || date==null) {
            return false;
        }
        for (final DateDTO dateDTO : listeDateDTO) {
            if (date.equals(dateDTO.getLibelle())) {
                return true;
            }
        }
        return false;
    }
}
