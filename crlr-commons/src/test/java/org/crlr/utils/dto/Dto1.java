/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Dto1.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils.dto;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Dto de tests des méthodes de clonages.
 *
 * @author breytond
 */
public class Dto1 implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;

    //objets immuables
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private String chaineO;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Integer entierO;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Long longO;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Double doubleO;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private BigDecimal bigDecimal;

    //primitifs
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private int entierP;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private long longP;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private double doubleP;

    //objet complexe
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Dto2 dto2 = new Dto2();

    //collection les plus utilisées
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private List<Long> listeLong = new ArrayList<Long>();

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Set<Long> setLong = new HashSet<Long>();

    //table de hachage
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Map<String, Object> hashMap = new HashMap<String, Object>();

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Map<String, Object> hashTable = new Hashtable<String, Object>();

    //collections complexes
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private List<Dto2> listeDto2 = new ArrayList<Dto2>();

    //type enumératif
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Enum enumTest;

    /**
     * Accesseur de bigDecimal.
     *
     * @return bigDecimal
     */
    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    /**
     * Mutateur bigDecimal.
     *
     * @param bigDecimal le bigDecimal
     */
    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    /**
     * Accesseur de chaineO.
     *
     * @return chaineO
     */
    public String getChaineO() {
        return chaineO;
    }

    /**
     * Mutateur chaineO.
     *
     * @param chaineO le chaineO
     */
    public void setChaineO(String chaineO) {
        this.chaineO = chaineO;
    }

    /**
     * Accesseur de doubleO.
     *
     * @return doubleO
     */
    public Double getDoubleO() {
        return doubleO;
    }

    /**
     * Mutateur doubleO.
     *
     * @param doubleO le doubleO
     */
    public void setDoubleO(Double doubleO) {
        this.doubleO = doubleO;
    }

    /**
     * Accesseur de doubleP.
     *
     * @return doubleP
     */
    public double getDoubleP() {
        return doubleP;
    }

    /**
     * Mutateur doubleP.
     *
     * @param doubleP le doubleP
     */
    public void setDoubleP(double doubleP) {
        this.doubleP = doubleP;
    }

    /**
     * Accesseur de dto2.
     *
     * @return dto2
     */
    public Dto2 getDto2() {
        return dto2;
    }

    /**
     * Mutateur dto2.
     *
     * @param dto2 le dto2
     */
    public void setDto2(Dto2 dto2) {
        this.dto2 = dto2;
    }

    /**
     * Accesseur de entierO.
     *
     * @return entierO
     */
    public Integer getEntierO() {
        return entierO;
    }

    /**
     * Mutateur entierO.
     *
     * @param entierO le entierO
     */
    public void setEntierO(Integer entierO) {
        this.entierO = entierO;
    }

    /**
     * Accesseur de entierP.
     *
     * @return entierP.
     */
    public int getEntierP() {
        return entierP;
    }

    /**
     * Mutateur entierP.
     *
     * @param entierP le entierP
     */
    public void setEntierP(int entierP) {
        this.entierP = entierP;
    }

    /**
     * Accesseur de enumTest.
     *
     * @return enumTest
     */
    public Enum getEnumTest() {
        return enumTest;
    }

    /**
     * Mutateur enumTest.
     *
     * @param enumTest le enumTest
     */
    public void setEnumTest(Enum enumTest) {
        this.enumTest = enumTest;
    }

    /**
     * Accesseur de hashMap.
     *
     * @return hashMap
     */
    public Map<String, Object> getHashMap() {
        return hashMap;
    }

    /**
     * Mutateur hashMap.
     *
     * @param hashMap le hashMap
     */
    public void setHashMap(Map<String, Object> hashMap) {
        this.hashMap = hashMap;
    }

    /**
     * Accesseur de hashTable.
     *
     * @return hashTable
     */
    public Map<String, Object> getHashTable() {
        return hashTable;
    }

    /**
     * Mutateur hashTable.
     *
     * @param hashTable le hashTable
     */
    public void setHashTable(Map<String, Object> hashTable) {
        this.hashTable = hashTable;
    }

    /**
     * Accesseur de listeDto2.
     *
     * @return listeDto2
     */
    public List<Dto2> getListeDto2() {
        return listeDto2;
    }

    /**
     * Mutateur listeDto2.
     *
     * @param listeDto2 le listeDto2
     */
    public void setListeDto2(List<Dto2> listeDto2) {
        this.listeDto2 = listeDto2;
    }

    /**
     * Accesseur de listeLong.
     *
     * @return listeLong
     */
    public List<Long> getListeLong() {
        return listeLong;
    }

    /**
     * Mutateur listeLong.
     *
     * @param listeLong le listeLong
     */
    public void setListeLong(List<Long> listeLong) {
        this.listeLong = listeLong;
    }

    /**
     * Accesseur de longO.
     *
     * @return longO
     */
    public Long getLongO() {
        return longO;
    }

    /**
     * Mutateur longO.
     *
     * @param longO le longO
     */
    public void setLongO(Long longO) {
        this.longO = longO;
    }

    /**
     * Accesseur de longP.
     *
     * @return longP
     */
    public long getLongP() {
        return longP;
    }

    /**
     * Mutateur longP.
     *
     * @param longP le longP
     */
    public void setLongP(long longP) {
        this.longP = longP;
    }

    /**
     * Accesseur de setLong.
     *
     * @return setLong
     */
    public Set<Long> getSetLong() {
        return setLong;
    }

    /**
     * Mutateur setLong.
     *
     * @param setLong le setLong
     */
    public void setSetLong(Set<Long> setLong) {
        this.setLong = setLong;
    }
}
