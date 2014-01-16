/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EntryAttributesMapperEleve.java,v 1.4 2009/06/17 10:31:53 ent_breyton Exp $
 */

package org.crlr.alimentation.Mapper;

import java.util.List;
import java.util.Vector;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.crlr.alimentation.DTO.EleveDTO;
import org.crlr.dto.Environnement;
import org.crlr.metier.entity.EleveBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.AttributesMapper;

/**
 * Classe pour effectuer le mapping entre les attributs d'un élève et un EleveDTO.
 *
 * @author Aurore
 * @version $Revision: 1.4 $
 */
public class EntryAttributesMapperEleve implements AttributesMapper {
    
    /** Environnement d'exécution. */
    private Environnement environnement ;
    
    private static final Logger log = LoggerFactory.getLogger(EntryAttributesMapperEleve.class);
    
    /**
     * Contructeur de la classe.
     * @param environnement l'environnement.
     */
    public EntryAttributesMapperEleve(Environnement environnement) {
        this.environnement = environnement;
    }

    /**
     * Effectue le mapping entre les attributs d'un élève et un EleveDTO.
     *
     * @param attrs La liste des attributs
     *
     * @return Le DTO créé à partir des élèments du ldap
     *
     * @throws NamingException Arrive si les attributs ne sont pas trouvé. Ne devrait
     *         jamais arriver.
     */
    @SuppressWarnings("unchecked")
    public EleveDTO mapFromAttributes(final Attributes attrs)
                               throws NamingException {
        
        
        if (Environnement.CRC.equals(environnement)){
            final Attribute passwordAtt = attrs.get("userPassword");
            final String password = passwordAtt.get(0).toString();
            if (password.startsWith("{SCRIPT}")){
                return null;
            }
        }
        //Attributs pour l'eleve
        final Attribute nom = attrs.get("sn");
        final Attribute prenom = attrs.get("givenName");
        final Attribute uid = attrs.get("UID");
        
        log.debug("Traitement d'élève uid={} nom={} prénom={}", uid, nom, prenom);

        //Attribut pour la jointure avec etablissement
        final Attribute struct = attrs.get("ENTPersonStructRattach");

        //Attribut pour la jointure avec les classes
        final Attribute classesA = attrs.get("ENTEleveClasses");

        //Attribut pour la jointure avec les classes
        final Attribute groupesA = attrs.get("ENTEleveGroupes");

        //L'enseignant
        final EleveBean eleveBean = new EleveBean();
        eleveBean.setNom(nom.get().toString());
        eleveBean.setPrenom(prenom.get().toString());
        eleveBean.setUid(uid.get().toString());

        //Les classes
        final List<String> classes = new Vector<String>();
        if (classesA != null) {
            final NamingEnumeration<String> test =
                (NamingEnumeration<String>) classesA.getAll();
            while (test.hasMore()) {
                String classe = test.next().toString();
                if (Environnement.CRLR.equals(environnement)){
                    //cn=19300021300010$1S7,ou=classes,ou=groups,dc=example,dc=org$SCONET
                    final int beginIndex = "cn=".length();
                    final int endIndex = classe.indexOf(",");
                    log.debug("begin {} end {} classe {}", beginIndex, endIndex, classe);
                    try {
                    classe = classe.substring(beginIndex,endIndex);
                    } catch (StringIndexOutOfBoundsException ex) {
                        log.error("ex", ex);
                    }
                } else {
                    //ENTStructureSIREN=19370001000013,ou=structures,dc=esco-centre,dc=fr$602
                    final String[] classeT = classe.split("\\$");
                    classe = extractSirenEtablissement(classeT[0])+"$"+classeT[1];
                }
                classes.add(classe);
            }
        }

        //Les groupes
        final List<String> groupes = new Vector<String>();
        if (groupesA != null) {
            final NamingEnumeration<String> test =
                (NamingEnumeration<String>) groupesA.getAll();
            while (test.hasMore()) {
                String groupe = test.next().toString();
                if (Environnement.CRLR.equals(environnement)){
                    final int beginIndex = "cn=".length();
                    final int endIndex = groupe.indexOf(",");
                    groupe = groupe.substring(beginIndex,endIndex);
                } else {
                    //ENTStructureSIREN=19370001000013,ou=structures,dc=esco-centre,dc=fr$602
                    final String[] groupeT = groupe.split("\\$");
                    groupe = extractSirenEtablissement(groupeT[0])+"$"+groupeT[1];
                }
                groupes.add(groupe);
            }
        }

        //La structure finale
        final EleveDTO elDTO = new EleveDTO();
        elDTO.setElBean(eleveBean);
        elDTO.setEtablissementDNSiren(struct.get().toString());
        elDTO.setClasses(classes);
        elDTO.setGroupes(groupes);
        return elDTO;
    }
    
    /**
     * Extrait un SIREN d'un dn d'établissement.
     * @param dn le dn de l'établissement.
     * @return le siren.
     */
    private String extractSirenEtablissement(String dn) {

        final int endIndex = dn.indexOf(",ou=");
        final String siren = dn.substring(dn.indexOf("ENTStructureSIREN=") + "ENTStructureSIREN=".length(), endIndex);
        return siren;
    }
}
