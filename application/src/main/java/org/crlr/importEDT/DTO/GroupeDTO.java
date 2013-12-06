/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.importEDT.DTO;

import java.util.List;

/**
 * Classe qui permet de représenter un groupe.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public class GroupeDTO extends DivisionDTO {
    
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
     * Libellé du groupe.
     */
    private String libelle;

    /**
     * Effectif prévu du groupe.
     */
    private int effectifPrevu;

    /**
     * Effectif max EDT.
     */
    private int effectifMaxEDT;

    /**
     * Liste des codes des divisions auxquelles le groupe est rattaché.
     */
    private List<String> codeDivAppartenance;

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
     * Accesseur de libelle.
     *
     * @return le libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Modificateur de libelle.
     *
     * @param libelle le libelle à modifier
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Accesseur de effectifPrevu.
     *
     * @return le effectifPrevu
     */
    public int getEffectifPrevu() {
        return effectifPrevu;
    }

    /**
     * Modificateur de effectifPrevu.
     *
     * @param effectifPrevu le effectifPrevu à modifier
     */
    public void setEffectifPrevu(int effectifPrevu) {
        this.effectifPrevu = effectifPrevu;
    }

    /**
     * Accesseur de effectifMaxEDT.
     *
     * @return le effectifMaxEDT
     */
    public int getEffectifMaxEDT() {
        return effectifMaxEDT;
    }

    /**
     * Modificateur de effectifMaxEDT.
     *
     * @param effectifMaxEDT le effectifMaxEDT à modifier
     */
    public void setEffectifMaxEDT(int effectifMaxEDT) {
        this.effectifMaxEDT = effectifMaxEDT;
    }

    /**
     * Accesseur de codeDivAppartenance.
     *
     * @return le codeDivAppartenance
     */
    public List<String> getCodeDivAppartenance() {
        return codeDivAppartenance;
    }

    /**
     * Modificateur de codeDivAppartenance.
     *
     * @param codeDivAppartenance le codeDivAppartenance à modifier
     */
    public void setCodeDivAppartenance(List<String> codeDivAppartenance) {
        this.codeDivAppartenance = codeDivAppartenance;
    }
}
