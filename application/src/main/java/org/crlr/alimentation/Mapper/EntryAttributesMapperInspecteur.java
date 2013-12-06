/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EntryAttributesMapperEleve.java,v 1.4 2009/06/17 10:31:53 ent_breyton Exp $
 */

package org.crlr.alimentation.Mapper;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.crlr.alimentation.DTO.InspecteurDTO;
import org.crlr.dto.Environnement;
import org.crlr.metier.entity.InspecteurBean;
import org.springframework.ldap.core.AttributesMapper;

/**
 * Classe pour effectuer le mapping entre les attributs d'un inspecteur et un InspecteurDTO.
 *
 * @author Aurore
 * @version $Revision: 1.4 $
 */
public class EntryAttributesMapperInspecteur implements AttributesMapper {
    
    
    /** Environnement d'exécution. */
    private Environnement environnement ;
    
    /**
     * Contructeur de la classe. Ne fait rien.
     * @param environnement l'environnement de l'application.
     */
    public EntryAttributesMapperInspecteur(Environnement environnement) {
        this.environnement = environnement;
    }

    /**
     * Effectue le mapping entre les attributs d'un inspecteur et un InspecteurDTO.
     *
     * @param attrs La liste des attributs
     *
     * @return Le DTO créé à partir des élèments du ldap
     *
     * @throws NamingException Arrive si les attributs ne sont pas trouvé. Ne devrait
     *         jamais arriver.
     */
    public InspecteurDTO mapFromAttributes(final Attributes attrs)
                               throws NamingException {
        if (Environnement.CRC.equals(environnement)){
            final Attribute passwordAtt = attrs.get("userPassword");
            final String password = passwordAtt.get(0).toString();
            if (password.startsWith("{SCRIPT}")){
                return null;
            }
        }
        final Attribute nom = attrs.get("sn");
        final Attribute prenom = attrs.get("givenName");
        final Attribute uid = attrs.get("UID");
        final Attribute titre = attrs.get("personalTitle");

        final InspecteurBean insBean = new InspecteurBean();
        insBean.setNom(nom.get().toString());
        insBean.setPrenom(prenom.get().toString());
        insBean.setUid(uid.get().toString());
        if(titre != null){
            insBean.setCivilite(titre.get().toString());
        }

        final InspecteurDTO insDTO = new InspecteurDTO();
        insDTO.setInsBean(insBean);
        return insDTO;
    }
}
