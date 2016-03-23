/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EntryAttributesMapperEnseignant.java,v 1.3 2009/06/16 09:24:37 weberent Exp $
 */

package org.crlr.alimentation.Mapper;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.Environnement;
import org.crlr.exception.base.CrlrRuntimeException;

import org.crlr.metier.entity.EnseignantBean;

import org.springframework.ldap.core.AttributesMapper;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Vector;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

/**
 * Classe pour effectuer le mapping entre les attributs d'un enseignant et un
 * EnseignantDTO.
 *
 * @author Aurore
 * @version $Revision: 1.3 $
 */
public class EntryAttributesMapperEnseignant implements AttributesMapper {
    /** Environnement d'exécution. */
    private Environnement environnement ;
    
    /**
     * Contructeur de la classe. 
     * @param environnement l'environnement.
     */
    public EntryAttributesMapperEnseignant(Environnement environnement) {
        this.environnement = environnement;
    }

    /**
     * Effectue le mapping entre les attributs d'un enseignant et un
     * EnseignantDTO.
     *
     * @param attrs La liste des attributs
     *
     * @return Le DTO créé à partir des élèments du ldap
     *
     * @throws NamingException Arrive si les attributs ne sont pas trouvé. Ne devrais
     *         jamais arriver.
     */
    @SuppressWarnings("unchecked")
    public EnseignantDTO mapFromAttributes(final Attributes attrs)
                                    throws NamingException {
        if (Environnement.CRC.equals(environnement)){
            final Attribute passwordAtt = attrs.get("userPassword");
            final byte[] t = (byte[]) passwordAtt.get(0);
            try {
                final String password = new String(t, "UTF-8");
                if (password.startsWith("{SCRIPT}")){
                    return null;
                }
            } catch (UnsupportedEncodingException e) {
               throw new CrlrRuntimeException("Encodage du password LDAP non supporté.");
            }
        }
        
        //Attributs pour l'enseignant
        final Attribute nom = attrs.get("sn");
        final Attribute prenom = attrs.get("givenName");
        final Attribute uid = attrs.get("UID");
        final Attribute titre = attrs.get("personalTitle");

        //Attribut pour la jointure avec etablissement
        final Attribute struct = attrs.get("ENTPersonStructRattach");

        //Attribut pour la jointure avec les classes
        final Attribute classesA = attrs.get("ENTAuxEnsClasses");

        //Attribut pour la jointure avec les classes
        final Attribute groupesA = attrs.get("ENTAuxEnsGroupes");

        //Attribut pour la jointure avec les enseignements
        final Attribute enseignementsA = attrs.get("ENTAuxEnsMatiereEnseignEtab");

        //L'enseignant
        final EnseignantBean enseignantBean = new EnseignantBean();
        enseignantBean.setNom(nom.get().toString());
        enseignantBean.setPrenom(prenom.get().toString());
        enseignantBean.setUid(uid.get().toString());
        if (titre != null) {
            enseignantBean.setCivilite(titre.get().toString());
        }

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
                    classe = classe.substring(beginIndex,endIndex);
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

        //Les enseignements
        final List<String> enseignements = new Vector<String>();
        if (enseignementsA != null) {
            final NamingEnumeration<String> test =
                (NamingEnumeration<String>) enseignementsA.getAll();
            while (test.hasMore()) {
                enseignements.add(test.next().toString());
            }
        }

        //La structure finale
        final EnseignantDTO ensDTO = new EnseignantDTO();
        ensDTO.setEnsBean(enseignantBean);
        ensDTO.setEtablissementDNSiren(struct.get().toString());
        ensDTO.setClasses(classes);
        ensDTO.setGroupes(groupes);
        ensDTO.setEnseignements(enseignements);
        return ensDTO;
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
