/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.importEDT.DTO;

import java.io.Serializable;

/**
 * Classe permettant de contenir les informations d'horaires d'un cours (début et fin).
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public class HoraireDTO implements Serializable{
    /**
     * Le serialVersionUid.
     */
    private static final long serialVersionUID  = 1L;
    
    /** heure de début. */
    private String heureDebut;

    /** heure de fin. */
    private String heureFin;

    /** minute de début. */
    private String minuteDebut;

    /** minute de fin. */
    private String minuteFin;

    /**
     * Constructeur vide de HoraireDTO.
     */
    public HoraireDTO(){}
    
    /**
     * Constructeur de HoiraireDTO avec paramètres.
     * @param hd : heure Début.
     * @param hf : heure Fin.
     * @param md : minute début.
     * @param mf : minute fin.
     */
    public HoraireDTO(Integer hd, Integer hf, Integer md, Integer mf){
        if(hd<10){ this.setHeureDebut("0"+hd.toString()); }else{ this.setHeureDebut(hd.toString()); }
        if(hf<10){ this.setHeureFin("0"+hf.toString()); }else{ this.setHeureFin(hf.toString()); }
        if(md<10){ this.setMinuteDebut("0"+md.toString()); }else{ this.setMinuteDebut(md.toString()); }
        if(mf<10){ this.setMinuteFin("0"+mf.toString()); }else{ this.setMinuteFin(mf.toString()); }
    }
    
    /**
     * Accesseur de heureDebut.
     *
     * @return le heureDebut
     */
    public String getHeureDebut() {
        return heureDebut;
    }

    /**
     * Modificateur de heureDebut.
     *
     * @param heureDebut le heureDebut à modifier
     */
    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    /**
     * Accesseur de heureFin.
     *
     * @return le heureFin
     */
    public String getHeureFin() {
        return heureFin;
    }

    /**
     * Modificateur de heureFin.
     *
     * @param heureFin le heureFin à modifier
     */
    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    /**
     * Accesseur de minuteDebut.
     *
     * @return le minuteDebut
     */
    public String getMinuteDebut() {
        return minuteDebut;
    }

    /**
     * Modificateur de minuteDebut.
     *
     * @param minuteDebut le minuteDebut à modifier
     */
    public void setMinuteDebut(String minuteDebut) {
        this.minuteDebut = minuteDebut;
    }

    /**
     * Accesseur de minuteFin.
     *
     * @return le minuteFin
     */
    public String getMinuteFin() {
        return minuteFin;
    }

    /**
     * Modificateur de minuteFin.
     *
     * @param minuteFin le minuteFin à modifier
     */
    public void setMinuteFin(String minuteFin) {
        this.minuteFin = minuteFin;
    }
    
    /**
     * Méthode hashCode.
     * @return un int.
     */
    public int hashCode(){
        return heureDebut.hashCode()+heureFin.hashCode()+minuteDebut.hashCode()+minuteFin.hashCode();
    }
    
    /**
     * Méthode equals.
     * @param other : l'objet à comparer.
     * @return booléen true si egal, false sinon.
     */
    public boolean equals(Object other){
        if(other!=null){
            if(((HoraireDTO) other).getHeureDebut() != null){
                if(((HoraireDTO) other).getHeureDebut().equals(this.getHeureDebut())){
                    if(((HoraireDTO) other).getHeureFin() != null){
                        if(((HoraireDTO) other).getHeureFin().equals(this.getHeureFin())){
                            if(((HoraireDTO) other).getMinuteDebut() != null){
                                if(((HoraireDTO) other).getMinuteDebut().equals(this.getMinuteDebut())){
                                    if(((HoraireDTO) other).getMinuteFin() != null){
                                        if(((HoraireDTO) other).getMinuteFin().equals(this.getMinuteFin())){
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else{
            return false; 
        }
        return false;
    }
}
