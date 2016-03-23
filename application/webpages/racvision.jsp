<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet href="http://racvision.orion.education.fr/page-de-test/racvision.css" ?>
<!DOCTYPE apptest SYSTEM "http://racvision.orion.education.fr/page-de-test/apptest-1.7.dtd">
<apptest>
<%@ page language="java" contentType="text/xml; charset=UTF-8"
    pageEncoding="UTF-8" session="false" isErrorPage="false"%>
<jsp:useBean id="test" class="org.crlr.Racvision" />
<%

String version = test.getVersion();

String date = test.getDate();

String ldap = test.getLdap();
String erreurLdap = test.getErreurLdap();

String ldapReq = test.getLdapReq();
String erreurLdapReq = test.getErreurLdapReq();

String ldapReqStruct = test.getLdapReqStruct();
String erreurLdapReqStruct = test.getErreurLdapReqStruct();

String sso = test.getSSO(getServletContext());
String erreurSso = test.getErreurSSO();

String driverPostgres = test.getDriverPostgres();
String erreurDriverPostgres = test.getErreurDriverPostgres();

String hibernate = test.getHibernate();
String erreurHibernate = test.getErreurHibernate();

String bdCahier = test.getBdCahier();
String erreurBdCahier = test.getErreurBdCahier();

String bdCahierInit = test.getBdCahierInit();
String erreurBdCahierInit = test.getErreurBdCahierInit();

%>

    <application id="ENT-CAHIER-TEXTE">
        <name>CAHIER DE TEXTE</name>
        <version><%= version %></version>
        <test id="LDAP">
            <description>Test de connexion au LDAP</description>
            <state val='<%= ldap %>'><%= erreurLdap %></state>  
        </test>
        <test id="LDAPREQPERS">
            <description>Test de requête sur la branche Personne du LDAP</description>
            <state val='<%= ldapReq %>'><%= erreurLdapReq %></state>    
        </test>     
        <test id="LDAPREQSTRUCT">
            <description>Test de requête sur la branche Structure du LDAP</description>
            <state val='<%= ldapReqStruct %>'><%= erreurLdapReqStruct %></state>    
        </test>
        <test id="SSO">
            <description>Test de connexion au service d’authentification</description>
            <state val='<%= sso %>'><%= erreurSso %></state>    
        </test>
        <test id="DRIVERPOSTGRESQL">
            <description>Test d'existence des drivers PostgreSQL</description>
            <state val='<%= driverPostgres %>'><%= erreurDriverPostgres %></state>  
        </test>
        <test id="HIBERNATE">
            <description>Test de configuration Hibernate</description>
            <state val='<%= hibernate %>'><%= erreurHibernate %></state>    
        </test>
        <test id="BD">
            <description>Test de connexion à la base de données de Cahier de texte</description>
            <state val='<%= bdCahier %>'><%= erreurBdCahier %></state>     
        </test>
        <test id="BDINIT">
            <description>Test d'initialisation de la base de données de Cahier de texte</description>
            <state val='<%= bdCahierInit %>'><%= erreurBdCahierInit %></state>     
        </test>
    </application>
    <date><%= date  %></date>
</apptest>
