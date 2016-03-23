/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: OperationLdapService.java,v 1.4 2009/04/22 07:43:20 weberent Exp $
 */

package org.crlr.alimentation;


import org.springframework.ldap.control.PagedResultsCookie;
import org.springframework.ldap.control.PagedResult;


/**
 * @author breytond.
 *
 */
public interface OperationLdapService {
    
    /** 
     * Recherche la liste des etablissements de l'annuaire.
     *
     * @param pagedResultsCookie Le cookie pour la requete paginée
     *
     * @return Un PagedResult qui contient la liste des resultats et un cookie pour al
     *         prochaine recherche
     */
    public PagedResult getEtablissements(PagedResultsCookie pagedResultsCookie);

    /**
     * Recherche la liste des enseignants de l'annuaire.
     *
     * @param pagedResultsCookie Le cookie pour la requete paginée
     *
     * @return Un PagedResult qui contient la liste des resultats et un cookie pour al
     *         prochaine recherche
     */
    public PagedResult getEnseignants(PagedResultsCookie pagedResultsCookie);

    /**
     * Recherche la liste des élèves de l'annuaire.
     *
     * @param pagedResultsCookie Le cookie pour la requete paginée
     *
     * @return Un PagedResult qui contient la liste des resultats et un cookie pour al
     *         prochaine recherche
     */
    public PagedResult getEleves(PagedResultsCookie pagedResultsCookie);

    /**
     * Recherche la liste des inspecteurs de l'annuaire.
     *
     * @param pagedResultsCookie Le cookie pour la requete paginée
     *
     * @return Un PagedResult qui contient la liste des resultats et un cookie pour al
     *         prochaine recherche
     */
    public PagedResult getInspecteurs(PagedResultsCookie pagedResultsCookie);
}
