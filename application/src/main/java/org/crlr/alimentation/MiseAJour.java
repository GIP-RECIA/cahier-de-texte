/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: MiseAJour.java,v 1.1 2009/11/04 08:48:14 weberent Exp $
 */

package org.crlr.alimentation;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ldap.control.PagedResult;

/**
 * Application de mise à jours de la base de données du module Cahier de texte suite au multi-fonction.
 *
 * @author breytond.
 * @version $Revision: 1.1 $
 */
public final class MiseAJour {
    /** Fichier de log pour les cas d'erreur. */
    private static final Log log = LogFactory.getLog(MiseAJour.class);

/**
     * Constructeur.
     */
    private MiseAJour() {
    }



    /**
     * programme permettant le redressement des données dans la base de données à
     * partir de celles du LDAP.
     *
     * @param args Il n'y a pas d'argument
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
   
        // Instanciation du contexte Spring à partir d'un fichier XML dans le classpath
        final ApplicationContext applicationContext =
            new ClassPathXmlApplicationContext("spring-alimentation.xml");

        //le bean operationSQL permet d'effectuer des traitements SQL la transaction est positionnée au niveau méthode.
        final OperationSqlService operationSqlService =
            (OperationSqlService) applicationContext.getBean("operationSql");

        // le bean operationLdap permet de rechercher des entrées dans l'annuaire de l'ENT.
        final OperationLdapService operationLdapService =
            (OperationLdapService) applicationContext.getBean("operationLdap");

        log.info("*** Mise à jours des enseignants ***");

        //Mise à jours des enseignants
        //ce qui renseigne la table etab-enseignants
        //les classes - groupes et jointures
        PagedResult enseignants = operationLdapService.getEnseignants(null);
        operationSqlService.updateEnseignants(enseignants.getResultList());
        while (enseignants.getCookie().getCookie() != null) {
            enseignants = operationLdapService.getEnseignants(enseignants.getCookie());
            operationSqlService.updateEnseignants(enseignants.getResultList());
        }


        log.info("*** Fin de la mise à jours ***");
    }
}
