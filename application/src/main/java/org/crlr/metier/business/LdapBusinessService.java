/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: LdapBusinessService.java,v 1.2 2009/04/21 09:08:27 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.crlr.dto.Environnement;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.UtilisateurDTO;

/**
 * @author breytond.
 *
 */
public interface LdapBusinessService {
    /**
     * Retourne un utilisateur en fonction de son uid.
     *
     * @param id uid.
     * @param environnement l'environnement d'exécution.
     * @param mapProfil la map qui associe une chaine de caractère à un profil.
     * @param grougsADMCentral les groupes qui identifient l'admin central.
     * @param regexADMLocal l'expression regulière qui identifie les admin locaux.
     *
     * @return le dto.
     *
     * @throws Exception l'exception potentielle.
     */
    public UtilisateurDTO getUser(final String id, final Environnement environnement, final Map<String, Profil> mapProfil, 
            final List<String> grougsADMCentral, final String regexADMLocal) throws Exception;

    /**
     * Retourne un utilisateur en fonction de son login et mot de passe.
     *
     * @param login login.
     * @param motDePasse mot de passe.
     *
     * @return le dto.
     *
     * @throws Exception l'exception potentielle.
     */
    public UtilisateurDTO getUser(final String login, final String motDePasse)
                    throws Exception;
    
    /**
     * Retourne le siren d'un établissement.
     * @param filtre : le filtre LDAP à appliquer contenant l'UAI.
     * @return siren.
     */
    public String getSirenEtablissement(String filtre);

    /**
     * Retourne la liste des siren correspondant aux UAI passés en paramètres.
     * @param uais la liste des uais.
     * @return une map qui associe les uai aux sirens correspondants.
     */
    public Map<String, String> findSirenByUAI(Set<String> uais);

    /**
     * Retourne la listes des eleves qui sont sous la responsabilité du parent.
     * @param uidParent l'uid du parent. 
     * @param uidsEnfant les uids des personnes en relation avec le parent.
     * @return les uids des enfants qui sont sous la responsabilité du parent, choisit au sein des uidsEnfant.
     */
    public Set<String> getAutoriteParentale(String uidParent, Set<String> uidsEnfant);
}
