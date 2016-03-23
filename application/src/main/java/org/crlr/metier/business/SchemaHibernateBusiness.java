/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SchemaHibernateBusiness.java,v 1.2 2009/06/19 06:49:27 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.List;

import org.crlr.dto.application.base.EtablissementSchemaQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.utils.SchemaUtils;
import org.crlr.utils.Assert;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

/**
 * SchemaHibernateBusiness.
 *
 * @author breytond.
 * @version $Revision: 1.2 $
 */
@Repository
public class SchemaHibernateBusiness extends AbstractBusiness
    implements SchemaHibernateBusinessService {
    
    /**
     * {@inheritDoc}
     */
    public Boolean checkExisenceSchema(final String schema) {
        Assert.isNotNull("schema", schema);
        final BaseHibernateBusiness baseHibernateBusiness =
            new BaseHibernateBusiness(this.getEntityManager());

        return baseHibernateBusiness.existSchemaIntoDataBase(schema);
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer findIdEtablissementSuivantSchema(final EtablissementSchemaQO etablissementSchemaQO) throws MetierException {
        final String schema = 
            SchemaUtils.getSchemaCourantOuArchive(true, 
                    etablissementSchemaQO.getExercice());
        final String queryEtablissement =
            "SELECT e.id FROM " + 
            SchemaUtils.getTableAvecSchema(schema, "cahier_etablissement") +
            " e WHERE e.code =?";
        
        final List<Integer> liste =
            getEntityManager().createNativeQuery(queryEtablissement)
            .setParameter(1, etablissementSchemaQO.getCodeEtablissement())
            .getResultList();
        
        final Integer id;
        
        if (CollectionUtils.isEmpty(liste) || liste.size() > 1) {
            throw new MetierException("Impossible de retrouver l'établissement dans les archives");
        }
        id = liste.get(0);
        return id;
    }
}
