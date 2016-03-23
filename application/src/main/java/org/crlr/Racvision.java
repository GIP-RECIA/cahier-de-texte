/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Racvision.java,v 1.3 2009/11/12 15:35:25 weberent Exp $
 */

package org.crlr;

import org.crlr.utils.DateUtils;
import org.crlr.utils.PropertiesUtils;

import org.springframework.ldap.core.DistinguishedName;

import java.net.HttpURLConnection;
import java.net.URL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import javax.servlet.ServletContext;

/**
 * Page racvision pour le test des applications.
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Racvision {
    //Les champs du LDAP
    static String ldapUrl;
    static String ldapBasedn;
    static String ldapUser;
    static String ldapPassword;
    static String ldapBranchePersonne;
    static String ldapBrancheStructure;

    //Les drivers
    static String dbDriver;

    //Les parametres Hibernate
    static String hibernateDialectPostgres;
    static String hibernateShowSql;

    //La BD de Cahier de texte
    static String dbCahierUrl;
    static String dbCahierUser;
    static String dbCahierPassword;
    
    static String version;

    static {
    	final Properties versionProp = PropertiesUtils.load("/version.properties");
    	version = versionProp.getProperty("application.version");
    	        
        final Properties props = PropertiesUtils.load("/config.properties");
        
        ldapUrl = props.getProperty("ldap.url");
        ldapBasedn = props.getProperty("ldap.baseDn");
        ldapUser = props.getProperty("ldap.user");
        ldapPassword = props.getProperty("ldap.password");
        ldapBranchePersonne = props.getProperty("ldap.branchePersonne");
        ldapBrancheStructure = props.getProperty("ldap.brancheStructure");

        dbDriver = props.getProperty("db.driver");

        dbCahierUrl = props.getProperty("db.url");
        dbCahierUser = props.getProperty("db.user");
        dbCahierPassword = props.getProperty("db.password");

        hibernateDialectPostgres = props.getProperty("hibernate.dialect");
        hibernateShowSql = props.getProperty("hibernate.showSql");
    }

    // Les erreurs renvoyé par les différents test.
    private String erreurDriverPostgres = "";
    private String erreurBdCahier = "";
    private String erreurBdCahierInit = "";
    private String erreurLdap = "";
    private String erreurHibernate = "";
    private String erreurLdapReq = "";
    private String erreurLdapReqStruct = "";
    private String erreurSSO = "";

    /**
     * Constructeur.
     */
    public void test() {
    }

    /**
     * Test de connexion au SSO.
     *
     * @param s Le contexte.
     *
     * @return le resultat du test.
     */
    public String getSSO(ServletContext s) {
        final String url =
            s.getInitParameter("edu.yale.its.tp.cas.client.logoutUrl")
             .replace("logout?service=%s", "login");
        String reponse = "OK";
        try {
            final HttpURLConnection conn =
                (HttpURLConnection) new URL(url).openConnection();
            conn.connect();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                erreurSSO = "Le service d’authentification n'est pas accessible.";
                reponse = "CRIT";
            }
            return reponse;
        } catch (Exception e) {
            erreurSSO = "Le service d’authentification n'est pas accessible.";
            return "CRIT";
        }
    }

    /**
     * Test d'existence des drivers Postgres.
     *
     * @return Le resultat du test.
     */
    public String getDriverPostgres() {
        try {
            erreurDriverPostgres = "";
            if (dbDriver.equals("org.postgresql.Driver")) {
                DriverManager.registerDriver(new org.postgresql.Driver());
                return "OK";
            } else {
                erreurDriverPostgres = "Le driver PostgreSQL n'est pas celui prévu (org.postgresql.Driver) mais " +
                                       dbDriver;
                return "WARN";
            }
        } catch (Exception e) {
            erreurDriverPostgres = "Le driver org.postgresql.Driver n'a pas pu être trouvé.";
            return "WARN";
        }
    }

    /**
     * test de configuration d'Hibernate.
     *
     * @return Le resultat du test.
     */
    public String getHibernate() {
        erreurHibernate = "";

        if (!(hibernateShowSql.equals("false"))) {
            erreurHibernate = "La variable Hibernate ShowSql n'est pas positionnée à 'false'.";
            return "WARN";
        }
        if (!(hibernateDialectPostgres.equals("org.hibernate.dialect.PostgreSQLDialect"))) {
            erreurHibernate = "Le champ DialecteHibernate pour PostgreSQL n’a pas la valeur attendue.";
            return "WARN";
        }
        return "OK";
    }

    /**
     * Test de connexion a la BD de Cahier de texte.
     *
     * @return Le resultat du test.
     */
    public String getBdCahier() {
        try {
            erreurBdCahier = "";
            DriverManager.registerDriver(new org.postgresql.Driver());

            final Connection conn =
                DriverManager.getConnection(dbCahierUrl, dbCahierUser,
                                            dbCahierPassword);
            conn.close();
            return "OK";
        } catch (Exception e) {
            erreurBdCahier = "Erreur de connexion à la base de données.";
            return "CRIT";
        }
    }

    /**
     * Test d'initialisation de la BD de Cahier de texte.
     *
     * @return Le resultat du test.
     */
    public String getBdCahierInit() {
        try {
            erreurBdCahierInit = "";
            DriverManager.registerDriver(new org.postgresql.Driver());

            final Connection conn =
                DriverManager.getConnection(dbCahierUrl, dbCahierUser,
                                            dbCahierPassword);

            final Statement stmt = conn.createStatement();

            String resultat = "OK";
            try {
                final ResultSet rs =
                    stmt.executeQuery("SELECT 1 FROM cahier_courant.cahier_annee_scolaire;");
                if (!rs.next()) {
                    erreurBdCahierInit = "La base de données n'a pas été initialisée. La table « cahier_courant.annee_scolaire » est vide.";
                    resultat = "CRIT";
                }
                conn.close();
            } catch (Exception e) {
                erreurBdCahierInit = "La base de données n'a pas été initialisée."
                    +" La table 'cahier_annee_scolaire' ou le schéma 'cahier_courant' n’est pas défini. ";
                return "CRIT";
            }
            return resultat;
        } catch (Exception e) {
            erreurBdCahierInit = "Erreur de connexion à la base de données. La requête sur la base de données n'a pas pu être effectuée. ";
            return "CRIT";
        }
    }

    /**
     * Test de connexion au ldap.
     *
     * @return Le resultat du test.
     */
    public String getLdap() {
        erreurLdap = "";
        final Hashtable<String,String> env = new Hashtable<String,String>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl + "/" + ldapBasedn);
        // Authenticate  
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, ldapUser);
        env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
        try {
            final DirContext ctx = new InitialDirContext(env);
            ctx.close();

            return "OK";
        } catch (NamingException e) {
            erreurLdap = "Erreur de connexion à l'annuaire LDAP.";
            return "CRIT";
        }
    }

    /**
     * Test de requête au ldap sur la branche personne.
     *
     * @return Le resultat du test.
     */
    public String getLdapReq() {
        erreurLdapReq = "";
        final Hashtable<String,String> env = new Hashtable<String,String>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl + "/" + ldapBasedn);
        // Authenticate  
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, ldapUser);
        env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
        final DirContext ctx;
        try {
            ctx = new InitialDirContext(env);
        } catch (NamingException e) {
            erreurLdapReq = "Erreur de connexion à l'annuaire LDAP. La requête sur l'annuaire LDAP n'a pas pu être effectuée.";
            return "CRIT";
        }
        try {
            final SearchControls portee = new SearchControls();
            portee.setSearchScope(SearchControls.SUBTREE_SCOPE);
            final DistinguishedName dn = new DistinguishedName();
            dn.add("ou", ldapBranchePersonne);
            final NamingEnumeration<SearchResult> resultat = ctx.search(dn, "(uid=ADM00000)", portee);
            if (resultat == null) {
                erreurLdapReq = "La branche Personne est mal configurée.";
                ctx.close();
                return "CRIT";
            } else {
                ctx.close();
                return "OK";
            }
        } catch (Exception e) {
            erreurLdapReq = "Le champ 'base_dn' de l’annuaire LDAP ou la branche Personne est mal configuré.";
            return "CRIT";
        }
    }

    /**
     * Test de requête au ldap sur la branche structure.
     *
     * @return Le resultat du test.
     */
    public String getLdapReqStruct() {
        erreurLdapReq = "";
        final Hashtable<String,String> env = new Hashtable<String,String>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl + "/" + ldapBasedn);
        // Authenticate  
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, ldapUser);
        env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
        final DirContext ctx;
        try {
            ctx = new InitialDirContext(env);
        } catch (NamingException e) {
            erreurLdapReqStruct = "Erreur de connexion à l'annuaire LDAP. La requête sur l'annuaire LDAP n'a pas pu être effectuée.";
            return "CRIT";
        }
        try {
            final SearchControls portee = new SearchControls();
            portee.setSearchScope(SearchControls.SUBTREE_SCOPE);
            final DistinguishedName dn = new DistinguishedName();
            dn.add("ou", ldapBrancheStructure);
            final NamingEnumeration<SearchResult> resultat =
                ctx.search(dn, "(ENTStructureSIREN=*)", portee);
            if (resultat == null) {
                erreurLdapReqStruct = "La branche Structure est mal configurée.";
                ctx.close();
                return "CRIT";
            } else {
                ctx.close();
                return "OK";
            }
        } catch (Exception e) {
            erreurLdapReqStruct = "Le champ 'base_dn' de l’annuaire LDAP ou la branche Structure est mal configuré.";
            return "CRIT";
        }
    }

    /**
     * Recherche la date actuelle.
     *
     * @return La date au format voulu par racvision.
     */
    public String getDate() {
        return DateUtils.format(DateUtils.getMaintenant(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * accesseur du erreurDriverPostgres.
     *
     * @return le erreurDriverPostgres
     */
    public String getErreurDriverPostgres() {
        return erreurDriverPostgres;
    }

    /**
     * accesseur du erreurBdCahier.
     *
     * @return le erreurBdCahier
     */
    public String getErreurBdCahier() {
        return erreurBdCahier;
    }

    /**
     * accesseur du erreurBdCahierInit.
     *
     * @return le erreurBdCahierInit
     */
    public String getErreurBdCahierInit() {
        return erreurBdCahierInit;
    }

    /**
     * accesseur du erreurLdap.
     *
     * @return le erreurLdap
     */
    public String getErreurLdap() {
        return erreurLdap;
    }

    /**
     * accesseur du erreurHibernate.
     *
     * @return le erreurHibernate
     */
    public String getErreurHibernate() {
        return erreurHibernate;
    }

    /**
     * accesseur du erreurLdapReq.
     *
     * @return le erreurLdapReq
     */
    public String getErreurLdapReq() {
        return erreurLdapReq;
    }

    /**
     * accesseur du erreurLdapReqStruct.
     *
     * @return le erreurLdapReqStruct
     */
    public String getErreurLdapReqStruct() {
        return erreurLdapReqStruct;
    }

    /**
     * accesseur du erreurSSO.
     *
     * @return le erreurSSO
     */
    public String getErreurSSO() {
        return erreurSSO;
    }
    
    /**
     * Retourne le numéro de version du module.
     * @return le numéro de version lu dans le config.properties
     */
    public String getVersion() {
        return version;
    }
}
