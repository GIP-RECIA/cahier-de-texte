/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SchemaHibernateBusinessService.java,v 1.1 2009/05/26 08:06:30 ent_breyton Exp $
 */

package org.crlr.metier.business;

import org.crlr.dto.application.base.EtablissementSchemaQO;
import org.crlr.exception.metier.MetierException;

/**
 * SchemaHibernateBusinessService.
 *
 * @author breytond.
 * @version $Revision: 1.1 $
  */
public interface SchemaHibernateBusinessService {
    /**
     * Retourne true si le schéma existe dans la base de données.
     *
     * @param schema le schéma à tester.
     *
     * @return true ou false.
     */
    public Boolean checkExisenceSchema(final String schema);
    
    /**
     * Recherche de l'identifiant d'un etablissement en fonction de son code et du schéma.
     * @param etablissementSchemaQO etablissementSchemaQO.
     * @return l'identifiant.
     * @throws MetierException .
     */
    public Integer findIdEtablissementSuivantSchema(final EtablissementSchemaQO etablissementSchemaQO) throws MetierException;
}
