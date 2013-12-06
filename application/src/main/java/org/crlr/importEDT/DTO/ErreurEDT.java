/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.importEDT.DTO;

import java.io.Serializable;

/**
 * Classe qui permet de passer des messages au rapport jasper de l'import EDT.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public class ErreurEDT implements Serializable {
    /**  */
    private static final long serialVersionUID = -3581184115367215486L;

    /** constructeur avec en paramètre le message. 
     * @param message : le message de l'erreur.    */
    public ErreurEDT(final String message) {
        if (message == null) {
            this.message = "";
        } else {
            this.message = message;
        }
    }
    
    /**
     * Le message d'erreur à mettre dans le rapport.
     */
    private String message;

    /**
     * Accesseur de message.
     *
     * @return le message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Modificateur de message.
     *
     * @param message le message à modifier
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Méthode hashCode.
     * @return un int.
     */
    public int hashCode(){
        return message.hashCode();
    }
    
    /**
     * Méthode equals.
     * @param other : l'objet à comparer.
     * @return booléen true si egal, false sinon.
     */
    public boolean equals(Object other){
        if(other!=null){
            if(((ErreurEDT) other).getMessage() != null){
                if(((ErreurEDT) other).getMessage().equals(this.getMessage())){
                    return true;
                }
            }
        }else{
            return false; 
        }
        return false;
    }
}
