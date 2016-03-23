/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EnumUtils.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.utils;

import org.apache.commons.lang.ArrayUtils;

/**
 * Méthodes utilitaires pour des enums.
 */
public final class EnumUtils {
/**
     * Constructeur privé pour interdire l'instanciation.
     */
    private EnumUtils() {
    }

/**
     * Interface IdStringEnum.
     *
     */
    public interface IdStringEnum {
        /**
         * Getter de l'id.
         *
         * @return id
         */
        String getId();
    }

    /**
     * Retourne la valeur de l'énum selon l'id.
     *
     * @param <T> type de l'enum.
     * @param tableau liste des valeurs de l'enum
     * @param id identifiant recherche
     *
     * @return enum correspondant
     */
    public static <T extends IdStringEnum> T find(T[] tableau, String id) {
        int index = ArrayUtils.indexOf(tableau, id);
        
        if (index == ArrayUtils.INDEX_NOT_FOUND) {
            return null;
        }
        
        return tableau[index];
         
    }
}
