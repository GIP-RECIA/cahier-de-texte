/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GenericTroisDTO.java,v 1.1 2010/03/29 09:29:35 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.io.Serializable;

/**
 * DTO générique, permettant de transmettre toute sorte d'information.
 *
 * @author breytond.
 * @version $Revision: 1.1 $
 * @param <T> premiere valeur.
 * @param <V> deuxieme valeur.
 * @param <W> deuxieme valeur.
 */
public class GenericTroisDTO<T,V,W>  implements Serializable {
    /**  */
    private static final long serialVersionUID = 681212726866516721L;
    
    /**
     * premiere valeur.
     */
    private T valeur1;
    /**
     * deuxieme valeur.
     */
    private V valeur2;
    /**
     * troisieme valeur.
     */
    private W valeur3; 
    
    /***
     * 
     * Constructeur.  
     */
    public GenericTroisDTO() {
        super();
    }

    /**
     * 
     * Constructeur. 
     * @param valeur1 la premiere valeur.
     * @param valeur2 la deuxieme valeur.
     * @param valeur3 la troisieme valeur.
     */
    public GenericTroisDTO(final T valeur1, final V valeur2, final W valeur3) {
        super();
        this.valeur1 = valeur1;
        this.valeur2 = valeur2;
        this.valeur3 = valeur3;
    }

    /**
     * Accesseur valeur1.
     * @return le valeur1
     */
    public T getValeur1() {
        return valeur1;
    }

    /**
     * Mutateur de valeur1.
     * @param valeur1 le valeur1 à modifier.
     */
    public void setValeur1(T valeur1) {
        this.valeur1 = valeur1;
    }

    /**
     * Accesseur valeur2.
     * @return le valeur2
     */
    public V getValeur2() {
        return valeur2;
    }

    /**
     * Mutateur de valeur2.
     * @param valeur2 le valeur2 à modifier.
     */
    public void setValeur2(V valeur2) {
        this.valeur2 = valeur2;
    }

    /**
     * Accesseur valeur3.
     * @return le valeur3
     */
    public W getValeur3() {
        return valeur3;
    }

    /**
     * Mutateur de valeur3.
     * @param valeur3 le valeur3 à modifier.
     */
    public void setValeur3(W valeur3) {
        this.valeur3 = valeur3;
    }

}
