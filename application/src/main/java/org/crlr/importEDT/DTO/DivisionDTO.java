/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.importEDT.DTO;

import java.io.Serializable;
import java.util.List;

/**
 * Classe qui permet de représenter une division.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public class DivisionDTO implements Serializable{
    /**
     * Le serialVersionUid.
     */
    private static final long serialVersionUID  = 1L;
    
    /**
     * Id du groupe.
     */
    private Integer id;
    
    /**
     * Id de l'établissement auquel appartien le groupe.
     */
    private Integer idEtablissement;
    
    /**
     * Code de la division.
     */
    private String code;

    /**
     * Liste des services associés à la division.
     */
    private List<ServiceDTO> services;

    /**
     * Accesseur de id.
     * @return le id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Modificateur de id.
     * @param id le id à modifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur de idEtablissement.
     * @return le idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Modificateur de idEtablissement.
     * @param idEtablissement le idEtablissement à modifier
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur de code.
     *
     * @return le code
     */
    public String getCode() {
        return code;
    }

    /**
     * Modificateur de code.
     *
     * @param code le code à modifier
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Accesseur de services.
     *
     * @return le services
     */
    public List<ServiceDTO> getServices() {
        return services;
    }

    /**
     * Modificateur de services.
     *
     * @param services le services à modifier
     */
    public void setServices(List<ServiceDTO> services) {
        this.services = services;
    }
}
