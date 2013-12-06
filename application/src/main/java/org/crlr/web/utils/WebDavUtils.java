/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.webdav.DavConstants;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.client.methods.DavMethod;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.apache.jackrabbit.webdav.property.DavProperty;
import org.apache.jackrabbit.webdav.property.DavPropertyName;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.crlr.dto.application.base.FichierStockageDTO;
import org.crlr.dto.application.base.TypeFichier;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.utils.Assert;
import org.crlr.utils.ComparateurUtils;
import org.crlr.utils.PropertiesUtils;
import org.crlr.web.dto.WebDavQO;
import org.springframework.util.CollectionUtils;

/**
 * WebDavUtils.
 *
 * @author breytond
 * @version $Revision: 1.1 $
  */
public final class WebDavUtils {
    
    private static Log log = LogFactory.getLog(WebDavUtils.class);
    
    /**
     * Constructeur protection.
     */
    private WebDavUtils() {
        
    }
    /**
     * Permet de créer une connexion sur le Webdav.
     *
     * @param login le login d'authentification webdav.
     * @param password le mot de passe de l'authentification webdav.
     *
     * @return la connexion http client.
     */
    private static HttpClient getWebDavConnexion(final String login, final String password) {
        final HttpClient client = new HttpClient();
        //utiliser un seul utilisateur DAV configuré en properties
        final Credentials creds =
            new UsernamePasswordCredentials(login, password);
        client.getState().setCredentials(AuthScope.ANY, creds);

        return client;
    }

   
    
    /**
     * Recherche des documents.
     *
     * @param webDavQO les paramètres.
     * @return Map (clef logique, valeur fichier).
     */
   public static Map<String, FichierStockageDTO> findDocuments(final WebDavQO webDavQO) {
       log.debug("!!!!!!!!!!!!! CHARGEMENT DE L'ESPACE DE STOCKAGE");
       Assert.isNotNull("webDavQO", webDavQO);

       final String depot = webDavQO.getDepot();
       final String uid = webDavQO.getUid();

       Assert.isNotNull("depot", depot);
       Assert.isNotNull("uid", uid);       
       
       final Properties config = PropertiesUtils.load("/config.properties");
       final String baseUrl = config.getProperty("webdav.url");
       final String loginDav = config.getProperty("webdav.login");
       final String passwordDav = config.getProperty("webdav.password");    
       String contexteUrl = config.getProperty("webdav.context");
       contexteUrl += depot + "/" +  uid + "/mes_documents/";
    
       final String url = baseUrl + contexteUrl;
       
       
       final HttpClient client = getWebDavConnexion(loginDav, passwordDav);
       
       
       List<FichierStockageDTO> listeFichier = 
           new ArrayList<FichierStockageDTO>();
       DavMethod pFind = null;
       try {
           pFind = new PropFindMethod(url, DavConstants.PROPFIND_ALL_PROP,
                                      DavConstants.DEPTH_INFINITY);
           client.executeMethod(pFind);

           final MultiStatus multiStatus = pFind.getResponseBodyAsMultiStatus();

           final MultiStatusResponse[] responses = multiStatus.getResponses();
           MultiStatusResponse currResponse;
           
           for (int i = 1; i < responses.length; i++) {
               currResponse = responses[i];

               final DavPropertySet props = currResponse.getProperties(200);
               final DavProperty<?> davProperties =
                   props.get(DavPropertyName.GETCONTENTTYPE);

               if (!(currResponse.getHref().equals("/")) &&
                       !(
                           currResponse.getHref()
                                           .equals(contexteUrl)
                       )) {
                                  
                   final String href = currResponse.getHref();
                   final FichierStockageDTO fichierDTO = new FichierStockageDTO();
                   fichierDTO.setCheminComplet(href);
                   final String hrefRelatif = href.replaceFirst(contexteUrl, "");
                   fichierDTO.setCheminRelatif(hrefRelatif);
                   
                   final List<String> contexteSplit = Arrays.asList(StringUtils.split(hrefRelatif, "/"));
                   if (CollectionUtils.isEmpty(contexteSplit)) {
                       fichierDTO.setNom(hrefRelatif);
                   } else {
                       final int nbParent = contexteSplit.size();
                       fichierDTO.setNom(contexteSplit.get(nbParent -1));
                       if (nbParent > 1) {
                           String repertoireParent = "";
                           for (int y=0;y<(nbParent-1);y++ ) {
                               repertoireParent += contexteSplit.get(y) + "/";
                           }
                           fichierDTO.setRepertoireParent(repertoireParent);
                       }
                       
                   }
                   
                   if (davProperties != null && (StringUtils.trimToEmpty((String) davProperties.getValue())).contains("directory")) {
                       fichierDTO.setType(TypeFichier.DIRECTORY);
                   } else {
                       fichierDTO.setType(TypeFichier.FILE);
                   }
                   listeFichier.add(fichierDTO);  
               }
           }    
          
       } catch (HttpException he) {
           throw new RuntimeException("Echec de la recherche de l'espace de stockage : " +
                                      he.getMessage());
       } catch (IOException io) {
           throw new RuntimeException("Echec de la recherche de l'espace de stockage : " +
                                      io.getMessage());
       } catch (DavException de) {
           throw new RuntimeException("Echec de la recherche de l'espace de stockage : " +
                                      de.getMessage());
       } finally {
           if (pFind != null) {
               //relache les connexions (si non attente)
               pFind.releaseConnection();
           }
       }
       
       final Map<String, String> mapclefcompteur = new HashMap<String, String>();
       
       listeFichier = ComparateurUtils.sort(listeFichier, "cheminComplet","type");
       Integer compteur= 0;      
       
       final Map<String, FichierStockageDTO> map = new LinkedHashMap<String, FichierStockageDTO>();
       
       //root node
       final FichierStockageDTO rootFichier = new FichierStockageDTO();
       rootFichier.setNom("Mes documents");
       rootFichier.setCheminComplet(contexteUrl);
       rootFichier.setType(TypeFichier.DIRECTORY);
       map.put(compteur.toString(), rootFichier);      
      
       for (final FichierStockageDTO dto : listeFichier) {
          /*Sytem.out.println(dto.getCheminComplet() +
                   " || " +  dto.getCheminRelatif() + 
                   " || " + dto.getNom() + " || " +  dto.getRepertoireParent() + " || " + dto.getType().getId());*/
           //cas du fichier racine
           if (!dto.getCheminRelatif().contains("/") && TypeFichier.FILE.equals(dto.getType())) {
               map.put("0." + compteur.toString(), dto);
               compteur++;
           } else if (StringUtils.isEmpty(dto.getRepertoireParent())) {
               map.put("0." + compteur.toString(), dto);
               mapclefcompteur.put(dto.getCheminRelatif(), "0." + compteur + ".0");
               compteur++;
           } else {
               if (mapclefcompteur.containsKey(dto.getRepertoireParent())) {
                   if (TypeFichier.FILE.equals(dto.getType())) {
                       final String valeur = mapclefcompteur.get(dto.getRepertoireParent());
                       final List<String> valeurSplit = Arrays.asList(StringUtils.split(valeur, "\\."));
                       map.put(valeur, dto);
                       final int nbSep = valeurSplit.size();       
                       String nouveauCompteur= "";
                       for (int y=0;y<(nbSep -1);y++ ) {
                           nouveauCompteur += valeurSplit.get(y) + ".";
                       }
                       
                       mapclefcompteur.put(dto.getRepertoireParent(), nouveauCompteur + (Integer.valueOf(valeurSplit.get(nbSep -1)) +1));
                   } else {
                       final String valeur = mapclefcompteur.get(dto.getRepertoireParent());
                       final List<String> valeurSplit = Arrays.asList(StringUtils.split(valeur, "\\."));
                       map.put(valeur, dto);
                       final int nbSep = valeurSplit.size();       
                       String nouveauCompteur= "";
                       for (int y=0;y<(nbSep -1);y++ ) {
                           nouveauCompteur += valeurSplit.get(y) + ".";
                       }
                       
                       mapclefcompteur.put(dto.getRepertoireParent(), nouveauCompteur + (Integer.valueOf(valeurSplit.get(nbSep -1)) +1));
                       //ajout du nouveau compteur
                       mapclefcompteur.put(dto.getCheminRelatif(), valeur + "." + 0);
                   }
               }
          }       
          
       }
       /**for (final Entry<String,FichierDTO> ent : map.entrySet()) {
           ystem.out.println("clef : " + ent.getKey() + " || val : " + ent.getValue().getCheminComplet());
       }*/
       
       return map;
   }

    /**
     *
     *
     * @param webDavQO paramètres.
     */
    public static void downloadFile(final WebDavQO webDavQO) {         
        final String source = webDavQO.getCheminFichierDav();
        final String destination = webDavQO.getCheminDestinationCahier();
        
        Assert.isNotNull("source", source);
        Assert.isNotNull("destination", destination);
        
        final Properties config = PropertiesUtils.load("/config.properties");
        final String baseUrl = config.getProperty("webdav.url");
        final String loginDav = config.getProperty("webdav.login");
        final String passwordDav = config.getProperty("webdav.password");    
        
        final String cheminDavSelectionne = baseUrl + source;         

        final HttpClient client = getWebDavConnexion(loginDav, passwordDav);
        
        final File destinationFile = new File(destination); 
        final GetMethod method = new GetMethod(cheminDavSelectionne);

        try {
            client.executeMethod(method);
            final InputStream stream = method.getResponseBodyAsStream();          
            FileUtils.copyInputStreamToFile(stream, destinationFile);
            
        } catch (HttpException he) {
            throw new RuntimeException("Echec du téléchargement du fichier : " +
                                       he.getMessage());
        } catch (IOException io) {
            throw new RuntimeException("Echec du téléchargement du fichier : " +
                                       io.getMessage());
        } finally {
            if (method != null) {
                //relache les connexions (si non attente)
                method.releaseConnection();
            }
        }
    }

   
}
