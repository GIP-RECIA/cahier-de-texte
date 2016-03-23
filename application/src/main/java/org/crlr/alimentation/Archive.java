/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Archive.java,v 1.1 2009/04/28 12:51:28 ent_breyton Exp $
 */

package org.crlr.alimentation;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Classe principale permettant d'effectuer les opérations d'archive d'une année scolaire.
 *
 * @author breytond.
 * @version $Revision: 1.1 $
  */
public final class Archive {
    /** Fichier de log pour les cas d'erreur. */
    private static final Log log = LogFactory.getLog(Archive.class);
    
    /** prefix schéma archive. */
    public static final String PREFIX_SCHEMA_ARCHIVE = "cahier_archive_";
/**
     * Constructeur.
     */
    private Archive() {
    }

    /**
     * programme permettant l'archivage du schéma courant.
     *
     * @param args Il ne doit pas y avoir d'arguments.
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            log.info("Aucun paramètre doit être saisi");            
            return;
        }
        
     // Instanciation du contexte Spring à partir d'un fichier XML dans le classpath
        final ApplicationContext applicationContext =
            new ClassPathXmlApplicationContext("spring-alimentation.xml");
        
      //le bean operationSQL permet d'effectuer des traitements SQL la transaction est positionnée au niveau méthode.
        final OperationSqlService operationSqlService =
            (OperationSqlService) applicationContext.getBean("operationSql");
        
        final String exercice = operationSqlService.findExerciceAnneeScolaireArchive();
        
        if (exercice != null) {
            final String schemaArchive = PREFIX_SCHEMA_ARCHIVE + exercice;            
            operationSqlService.renameSchemaPourArchive(schemaArchive);            
        } else {
            return;
        }
    }
}
