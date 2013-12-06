/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ContexteApplication.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.contexte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.crlr.dto.Environnement;
import org.crlr.dto.Outil;
import org.crlr.dto.application.base.Profil;
import org.crlr.exception.base.CrlrRuntimeException;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.utils.PropertiesUtils;

//import fr.gouv.defense.marine.dpmm.siad.web.servlet.listener.ContexteServiceListener;
/**
 * ContexteApplication contient un ensemble de propri?t?s sur l'application courante.
 *
 * @author breytond
 */
public class ContexteApplication implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant ID. */
    public static final String ID = "contexteApplication";

    /** The log. */
    private final Log log = LogFactory.getLog(getClass());  
    
    /** The Constant OUTILS_PROPERTIES_URL pour les outils du menu. */
    private static final String OUTILS_PROPERTIES_URL =
        "/ecrans.properties";

    /** The Constant OUTILS_PROPERTIES_URL. */
    private static final String OUTILS_PROPERTIES_SOUS_ECRANS_URL =
        "/sousEcrans.properties";
    
    /** The Constant OUTILS_PROPERTIES_URL. */
    private static final String CONFIG_PROPERTIES_URL =
        "/config.properties";
    
    /** The Constant VERSION_PROPERTIES_URL. */
    private static final String VERSION_PROPERTIES_URL =
        "/version.properties";
    
    /** valeur par defaut pour l'environnement. */
    private static final String DEFAULT_ENV = Environnement.CRLR.name();

    /** The map outils URL. */
    private Map<Outil, String> mapOutilsURL; 
    
    /** Environnement. */
    private Environnement environnement;
    
    /** Paramétrage du menu (replie automatique). */
    private Boolean vraiOuFauxMenuAuto;
    
    /** Les profils applicatifs avec leurs correspondances. */
    private Map<String, Profil> mapProfil;
    
    /** Les groupes pour l'ADMCentral. */
    private List<String> groupsADMCentral;
    
    /** Les expressios regulieres pour l'admin local. */
    private String regexpAdmLocal;
    
    /** Version de l'application. */
    private String version;
    
    /**
     * The Constructor.
     */
    public ContexteApplication() {
        mapOutilsURL = new HashMap<Outil, String>();
        initOutils();
        initConfiguration();
    }

    private final static String VERSION_CLE = "application.version";
    
    /**
     * Innitialisation du paramétrage de l'application.
     */
    private void initConfiguration() {
        //lecture du fichier.
        final Properties props = PropertiesUtils.load(CONFIG_PROPERTIES_URL);
        
        changementEnvironnement(props);
        
        // Gestion du paramétrage du menu.
        final String menuAuto = StringUtils.trimToNull(props.getProperty("application.menu.auto"));
        if (StringUtils.isEmpty(menuAuto)){
            //Comportement par défaut : true
            this.vraiOuFauxMenuAuto = Boolean.TRUE;
        } else {
            this.vraiOuFauxMenuAuto = org.crlr.utils.BooleanUtils.isTrue(menuAuto);
        }
        
        final Properties versionProps = PropertiesUtils.load(VERSION_PROPERTIES_URL);
        version = versionProps.getProperty(VERSION_CLE);
    }
    
    /**
     * Changement de l'environnement d'exécution.
     * Valeur par défaut CRLR.
     * @param props le fichier de propriétés.
     */
    private void changementEnvironnement(final Properties props) {
        this.environnement = Environnement.valueOf(StringUtils.trimToNull(props.getProperty("application.env", DEFAULT_ENV)));
        
        mapProfil = new HashMap<String, Profil>();
        
        if (Environnement.CRLR == environnement){
            //L'ordre est important : si un utilisateur est à la fois enseignant et parent.
            //C'est le profil enseignant qui doit être pris en compte.
            mapProfil.put("ENTAuxEnseignant", Profil.ENSEIGNANT);
            mapProfil.put("ENTEleve", Profil.ELEVE);
            mapProfil.put("ENTAuxPersRelEleve", Profil.PARENT);
            mapProfil.put("ENTDirecteur", Profil.DIRECTION_ETABLISSEMENT);
            mapProfil.put("ENTDocumentaliste", Profil.DOCUMENTALISTE);
            mapProfil.put("ENTChefDeTravaux", Profil.DOCUMENTALISTE);
            mapProfil.put("ENTMedical", Profil.DOCUMENTALISTE);
            mapProfil.put("ENTAssistantEducation", Profil.DOCUMENTALISTE);
            mapProfil.put("ENTAdministratif", Profil.DOCUMENTALISTE);
            mapProfil.put("ENTConseillerEducation", Profil.DOCUMENTALISTE);
            mapProfil.put("ENTConseillerOrientation", Profil.DOCUMENTALISTE);
            mapProfil.put("ENTInspecteur", Profil.INSPECTION_ACADEMIQUE);
        } else {
            
            final String enseignantList = StringUtils.trimToNull(props.getProperty("profil.enseignant"));
            if (!StringUtils.isEmpty(enseignantList)){
                final String[] enseignantTab = enseignantList.split(",");
                for (String enseignant : enseignantTab){
                    mapProfil.put(enseignant, Profil.ENSEIGNANT);
                }
            } else {
                throw new CrlrRuntimeException("Echec de l'initialisation du LdapBusiness : La configuration " +
                "de 'profil.enseignant' n'a pas été renseignée");
            }
            
            final String eleveList = StringUtils.trimToNull(props.getProperty("profil.eleve"));
            if (!StringUtils.isEmpty(eleveList)){
                final String[] eleveTab = eleveList.split(",");
                for (String eleve : eleveTab){
                    mapProfil.put(eleve, Profil.ELEVE);
                }
            } else {
                throw new CrlrRuntimeException("Echec de l'initialisation du LdapBusiness : La configuration " +
                "de 'profil.eleve' n'a pas été renseignée");
            }
            
            final String parentList = StringUtils.trimToNull(props.getProperty("profil.parent"));
            if (!StringUtils.isEmpty(parentList)){
                final String[] parentTab = parentList.split(",");
                for (String parent : parentTab){
                    mapProfil.put(parent, Profil.PARENT);
                }
            } else {
                throw new CrlrRuntimeException("Echec de l'initialisation du LdapBusiness : La configuration " +
                "de 'profil.parent' n'a pas été renseignée");
            }
            
            final String directeurList = StringUtils.trimToNull(props.getProperty("profil.directeur"));
            if (!StringUtils.isEmpty(directeurList)){
                final String[] directeurTab = directeurList.split(",");
                for (String directeur : directeurTab){
                    mapProfil.put(directeur, Profil.DIRECTION_ETABLISSEMENT);
                }
            } else {
                throw new CrlrRuntimeException("Echec de l'initialisation du LdapBusiness : La configuration " +
                "de 'profil.directeur' n'a pas été renseignée");
            }
            
            final String persEducNatList = StringUtils.trimToNull(props.getProperty("profil.persEducNat"));
            if (!StringUtils.isEmpty(persEducNatList)){
                final String[] persEducNatTab = persEducNatList.split(",");
                for (String persEducNat : persEducNatTab){
                    mapProfil.put(persEducNat, Profil.DOCUMENTALISTE);
                }
            } else {
                throw new CrlrRuntimeException("Echec de l'initialisation du LdapBusiness : La configuration " +
                "de 'profil.persEducNat' n'a pas été renseignée");
            }
            
            final String inspecteurList = StringUtils.trimToNull(props.getProperty("profil.inspecteur"));
            if (!StringUtils.isEmpty(inspecteurList)){
                final String[] inspecteurTab = inspecteurList.split(",");
                for (String inspecteur : inspecteurTab){
                    mapProfil.put(inspecteur, Profil.INSPECTION_ACADEMIQUE);
                }
            } else {
                throw new CrlrRuntimeException("Echec de l'initialisation du LdapBusiness : La configuration " +
                "de 'profil.inspecteur' n'a pas été renseignée");
            }
            
            groupsADMCentral = new ArrayList<String>();
            final String adminCentralList = StringUtils.trimToNull(props.getProperty("profil.adminCentral"));
            if (!StringUtils.isEmpty(adminCentralList)){
                final String[] adminCentralTab = adminCentralList.split(",");
                for (String adminCentral : adminCentralTab){
                    groupsADMCentral.add(adminCentral);
                }
            } else {
                throw new CrlrRuntimeException("Echec de l'initialisation du LdapBusiness : La configuration " +
                "de 'profil.adminCentral' n'a pas été renseignée");
            }
            
            regexpAdmLocal = StringUtils.trimToNull(props.getProperty("profil.adminLocal"));
            if (StringUtils.isEmpty(regexpAdmLocal)){
                throw new CrlrRuntimeException("Echec de l'initialisation du LdapBusiness : La configuration " +
                "de 'profil.adminLocal' n'a pas été renseignée") ;
            }
        }
    }
    
    /**
     * Initialisation du menu - Chargement des diff?rents liens vers les outils,
     * ? partir d'un fichier de properties. - R?cup?ration de l'ensemble des outils qui
     * ont un contexte marin (information indispensable lorsque l'on bascule d'un ?cran
     * ? un autre et que l'on souhaite ou pas vider le contexte marin selon la
     * population).
     */
    private void initOutils() {
        //initialisation des url des outils du menu.
        initOutilMenu();
        //initialisation des utl des sous écrans.
        initOutilSousEcran();
        log.debug("MAP OUTILS = {0}", mapOutilsURL);
    }    
    

    /**
     * initialise les urls des outils du menu.
     */
    private void initOutilMenu() {
        log.debug("Initialisation du menu - chargement des liens vers les outils");
        ajouteUrl(OUTILS_PROPERTIES_URL);
    }

    /**
     * initialise les urls des sous écrans.
     */
    private void initOutilSousEcran() {
        log.debug("Initialisation des sous écrans - chargement des liens vers ces outils");
        ajouteUrl(OUTILS_PROPERTIES_SOUS_ECRANS_URL);
    }    

    /**
     * Lit le fichier properties dont le nom est passé en paramètre, afin de
     * remplir la map des URLs.
     *
     * @param propertiesNameFile le nom du fichier properties.
     */
    private void ajouteUrl(final String propertiesNameFile) {
        //lecture du fichier.
        final Properties props = PropertiesUtils.load(propertiesNameFile);

        // On renseigne les map de correspondance outil / url
        final Enumeration<?> enumeration = props.propertyNames();
        while (enumeration.hasMoreElements()) {
            final String cle = (String) enumeration.nextElement();
            mapOutilsURL.put(Outil.valueOf(cle), props.getProperty(cle));
        }
    }

    /**
     * Retourne outil url.
     *
     * @param outil the outil
     *
     * @return the outil url
     */
    public String getOutilUrl(Outil outil) {
        return mapOutilsURL.get(outil);
    }

    /**
     * Retourne outils par url.
     *
     * @param url the url
     *
     * @return the outils par url
     */
    public Set<Outil> getOutilsParUrl(String url) {
        final Set<Outil> listeOutils = new HashSet<Outil>();
        if (url == null) {
            return listeOutils;
        }

        for (Outil outil : mapOutilsURL.keySet()) {
            if (url.equals(mapOutilsURL.get(outil))) {
                listeOutils.add(outil);
            }
        }

        return listeOutils;
    }

    /**
     * Accesseur environnement.
     * @return le environnement.
     */
    public Environnement getEnvironnement() {
        return environnement;
    }
    
    /**
     * Accesseur vers le nom de l'environnement.
     * @return l'environnement
     */
    public String getNameEnvironnement() {
        return environnement.name();
    }

    /**
     * Accesseur vraiOuFauxMenuAuto.
     * @return le vraiOuFauxMenuAuto.
     */
    public Boolean getVraiOuFauxMenuAuto() {
        return vraiOuFauxMenuAuto;
    }

    /**
     * Accesseur de mapProfil.
     * @return le mapProfil
     */
    public Map<String, Profil> getMapProfil() {
        return mapProfil;
    }

    /**
     * Accesseur de groupsADMCentral.
     * @return le groupsADMCentral
     */
    public List<String> getGroupsADMCentral() {
        return groupsADMCentral;
    }

    /**
     * Accesseur de regexpAdmLocal.
     * @return le regexpAdmLocal
     */
    public String getRegexpAdmLocal() {
        return regexpAdmLocal;
    }

    /**
     * Accesseur de version {@link #version}.
     * @return retourne version
     */
    public String getVersion() {
        return version;
    }
}
