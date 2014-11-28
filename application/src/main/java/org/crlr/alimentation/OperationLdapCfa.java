/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: OperationLdap.java,v 1.11 2010/06/04 14:40:15 jerome.carriere Exp $
 */

package org.crlr.alimentation;

import java.util.List;

import javax.naming.directory.SearchControls;

import org.crlr.alimentation.Mapper.EntryAttributesMapperEleve;
import org.crlr.alimentation.Mapper.EntryAttributesMapperEnseignant;
import org.crlr.alimentation.Mapper.EntryAttributesMapperEtablissement;
import org.crlr.alimentation.Mapper.EntryAttributesMapperInspecteur;
import org.crlr.dto.Environnement;
import org.crlr.exception.base.CrlrRuntimeException;
import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.control.PagedResult;
import org.springframework.ldap.control.PagedResultsCookie;
import org.springframework.ldap.control.PagedResultsDirContextProcessor;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;

/**
 * Classe permettant d'effectuer des opérations sur l'annuaire ENT.
 * Juste on copie de OperationLdap pas encore utilise
 * 
 * @author breytond.
 */
public class OperationLdapCfa implements OperationLdapService {
    /** Injection du ldapTemplate. */
    private LdapTemplate ldapTemplate;

    /** Injection de la branche structure. */
    private String brancheStructure;

    /** Injection de la branche personne. */
    private String branchePersonne;
    
    /** Injection du nom de l'attribut LDAP représentant le profil. */
    private String nomProfil;
    
    /** Injection de l'environnement et des prodils acceptés. */
    private Environnement environnement;
    private String profilsEnseignants;
    private String profilsEleves;
    private String profilsInspecteurs;

    /**
     * Le constructeur de la classe. ne fait rien.
     */
    public OperationLdapCfa() {
        
    }  

    /**
     * Positionne le service LdapTemplate.
     * 
     * @param ldapTemplate
     *            ldapTemplate.
     */
    public void setLdapTemplate(final LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    /**
     * Positionne l'attribut structure.
     *
     * @param brancheStructure brancheStructure.
     */
    public void setBrancheStructure(String brancheStructure) {
        this.brancheStructure = brancheStructure;
    }

    /**
     * Positionne l'attribut personne.
     *
     * @param branchePersonne branchePersonne.
     */
    public void setBranchePersonne(String branchePersonne) {
        this.branchePersonne = branchePersonne;
    }
    
    /**
     * Mutateur nomProfil.
     * @param nomProfil le nomProfil à modifier.
     */
    public void setNomProfil(String nomProfil) {
        this.nomProfil = nomProfil;
    }
    
    /**
     * Mutateur de profilsEnseignants.
     * @param profilsEnseignants le profilsEnseignants à modifier.
     */
    public void setProfilsEnseignants(String profilsEnseignants) {
        this.profilsEnseignants = profilsEnseignants;
    }

    /**
     * Mutateur de profilsEleves.
     * @param profilsEleves le profilsEleves à modifier.
     */
    public void setProfilsEleves(String profilsEleves) {
        this.profilsEleves = profilsEleves;
    }

    /**
     * Mutateur de profilsInspecteurs.
     * @param profilsInspecteurs le profilsInspecteurs à modifier.
     */
    public void setProfilsInspecteurs(String profilsInspecteurs) {
        this.profilsInspecteurs = profilsInspecteurs;
    }

    /**
     * Mutateur de environnement.
     * @param environnement le environnement à modifier.
     */
    public void setEnvironnement(String environnement) {
        if (StringUtils.isEmpty(environnement)){
            this.environnement = Environnement.CRLR;
        } else {
            this.environnement = Environnement.valueOf(environnement);
        }

    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    public PagedResult getEtablissements(PagedResultsCookie pagedResultsCookie) {
        final DistinguishedName dn = new DistinguishedName();
        dn.add("ou", brancheStructure);

        final String filter = "(&(ENTStructureTypeStruct=CFA)(ObjectClass=ENTEtablissement))";


        final PagedResultsDirContextProcessor control =
            new MyPagedResultsDirContextProcessor(100, pagedResultsCookie);


        final SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        final List etab = ldapTemplate.search(dn, filter, searchControls,
                new EntryAttributesMapperEtablissement(), control);

        return new PagedResult(etab, control.getCookie());
    }

    /**
     * {@inheritDoc}
     */
    public PagedResult getEnseignants(PagedResultsCookie pagedResultsCookie) {
        final DistinguishedName dn = new DistinguishedName();
        dn.add("ou", branchePersonne);

        String filter ;
        if (Environnement.CRLR.equals(environnement)){
            filter = "(&(ObjectClass=ENTAuxEnseignant)(Actif=1))";
        } else {
	  if (Environnement.CRC.equals(environnement)){
		filter = "(&(ESCODomaines=cfa.netocentre.fr)(ObjectClass=ENTAuxEnseignant)(!(ESCOPersonEtatCompte=DELETE)))";
          } else {
            final String enseignantList = StringUtils.trimToNull(profilsEnseignants);
            if (!StringUtils.isEmpty(enseignantList)){
                filter = "(|";
                final String[] enseignantTab = enseignantList.split(",");
                for (String enseignant : enseignantTab){
                    filter += "("+this.nomProfil+"="+enseignant+")";
                }
                filter += ")";
            } else {
                throw new CrlrRuntimeException("Echec de la recherche des enseignants : La configuration " +
                "de 'profil.enseignant' n'a pas été renseignée");
            }
          } 
        }


        final PagedResultsDirContextProcessor control =
            new MyPagedResultsDirContextProcessor(50, pagedResultsCookie);


        final SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        
        @SuppressWarnings("rawtypes")
        final List persons = ldapTemplate.search(dn, filter, searchControls,
                new EntryAttributesMapperEnseignant(environnement), control);

        return new PagedResult(persons, control.getCookie());
    }

    /**
     * {@inheritDoc}
     */
    public PagedResult getEleves(PagedResultsCookie pagedResultsCookie) {
        final DistinguishedName dn = new DistinguishedName();
        dn.add("ou", branchePersonne);

        String filter ;
        if (Environnement.CRLR.equals(environnement)){
            filter = "(&(ObjectClass=ENTEleve)(Actif=1))";
        } else {
	  if (Environnement.CRC.equals(environnement)){
		filter = "(&(ESCODomaines=cfa.netocentre.fr)(ObjectClass=ENTEleve)(!(ESCOPersonEtatCompte=DELETE)))";
          } else {
            final String eleveList = StringUtils.trimToNull(profilsEleves);
            if (!StringUtils.isEmpty(eleveList)){
                filter = "(|";
                final String[] eleveTab = eleveList.split(",");
                for (String eleve : eleveTab){
                    filter += "("+this.nomProfil+"="+eleve+")";
                }
                filter += ")";
            } else {
                throw new CrlrRuntimeException("Echec de la recherche des élèves : La configuration " +
                "de 'profil.eleve' n'a pas été renseignée");
            }
	  }
        }


        final PagedResultsDirContextProcessor control =
            new MyPagedResultsDirContextProcessor(50, pagedResultsCookie);


        final SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        @SuppressWarnings("rawtypes")
        final List persons = ldapTemplate.search(dn, filter, searchControls,
                new EntryAttributesMapperEleve(environnement), control);

        return new PagedResult(persons, control.getCookie());
    }

    /**
     * {@inheritDoc}
     */
    public PagedResult getInspecteurs(PagedResultsCookie pagedResultsCookie) {
        final DistinguishedName dn = new DistinguishedName();
        dn.add("ou", branchePersonne);

        String filter ;
        if (Environnement.CRLR == environnement){
            filter = "(&(ObjectClass=ENTInspecteur)(Actif=1))";
        } else {
	  if (Environnement.CRC.equals(environnement)){
		filter = "(&(ESCODomaines=cfa.netocentre.fr)(|(ESCOPersonProfils=INS)(ESCOPersonProfils=IGN)(ESCOPersonProfils=IPE))(!(ESCOPersonEtatCompte=DELETE)))";
          } else {
            final String inspecteurList = StringUtils.trimToNull(profilsInspecteurs);
            if (!StringUtils.isEmpty(inspecteurList)){
                filter = "(|";
                final String[] inspecteurTab = inspecteurList.split(",");
                for (String inspecteur : inspecteurTab){
                    filter += "("+this.nomProfil+"="+inspecteur+")";
                }
                filter += ")";
            } else {
                throw new CrlrRuntimeException("Echec de la recherche des inspecteurs : La configuration " +
                "de 'profil.inspecteur' n'a pas été renseignée");
            }
	  }
        }

        final PagedResultsDirContextProcessor control =
            new MyPagedResultsDirContextProcessor(50, pagedResultsCookie);


        final SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        @SuppressWarnings("rawtypes")
        final List persons = ldapTemplate.search(dn, filter, searchControls,
                new EntryAttributesMapperInspecteur(environnement), control);

        return new PagedResult(persons, control.getCookie());
    }    
}
