/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EntryAttributesMapperEtablissement.java,v 1.2 2009/04/20 11:52:53 ent_breyton Exp $
 */

package org.crlr.alimentation.Mapper;

import org.crlr.metier.entity.EtablissementBean;
import org.apache.commons.lang.StringUtils;

import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

/**
 * Classe pour effectuer le mapping entre les attributs d'un etablissement et un
 * etablissementBean.
 *
 * @author Aurore
 * @version $Revision: 1.2 $
 */
public class EntryAttributesMapperEtablissement implements AttributesMapper {
/**
     * Contructeur de la classe. Ne fait rien.
     */
    public EntryAttributesMapperEtablissement() {
    }

    /**
     * Effectue le mapping entre les attributs d'un etablissement et un
     * etablissementBean.
     *
     * @param attrs La liste des attributs
     *
     * @return Le Bean créé à partir des élèments du ldap
     *
     * @throws NamingException Arrive si les attributs ne sont pas trouvé. Ne devrait
     *         jamais arriver.
     */
    public EtablissementBean mapFromAttributes(final Attributes attrs)
                                        throws NamingException {
        final Attribute siren = attrs.get("ENTStructureSIREN");
        final Attribute nom = attrs.get("ENTStructureNomCourant");
        final Attribute nomCourt = attrs.get("ESCOStructureNomCourt");

        final EtablissementBean etablissementBean = new EtablissementBean();
        try {
            etablissementBean.setCode(siren.get().toString());
            
            String shortName;
            if (nomCourt != null && ! StringUtils.isEmpty(StringUtils.trimToNull(nomCourt.get(0).toString()))){
                shortName = StringUtils.trimToNull(nomCourt.get(0).toString());
            } else {
                shortName = nom.get().toString();
                if (shortName.contains("-ac-ORL._TOURS")) {
                    final int beginIndex = shortName.indexOf("-")+1;
                    final int endIndex = shortName.indexOf("-ac-ORL._TOURS");
                    shortName = shortName.substring(beginIndex,endIndex);
                }
            }
            etablissementBean.setDesignation(shortName);
            
            return etablissementBean;
        } catch (NamingException e) {
            throw new NamingException(e.getMessage());
        }
    }
}
