/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: CollectionUtilsTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.utils.dto.Dto1;
import org.crlr.utils.dto.Dto2;
import org.crlr.utils.dto.Dto3;

/**
 * Test de la classe {@link CollectionUtils}.
 *
 * @author romana
 * @author breytond
 */
public class CollectionUtilsTest extends TestCase {
    protected final Log log = LogFactory.getLog(getClass());

    

    /**
     *
     */
    public void testSplit() {
        // test sur collection vide (et null)
        assertEquals(0, CollectionUtils.split(new ArrayList<Integer>(), 10).size());
        assertNull(CollectionUtils.split(null, 10));

        // test sur petite collection
        List<Integer> liste = Arrays.asList(new Integer[] { 1, 2, 3 });
        List<List<Integer>> splitListe = CollectionUtils.split(liste, 4);
        assertNotNull(splitListe);
        assertEquals(1, splitListe.size());
        assertEquals(Arrays.asList(new Integer[] { 1, 2, 3 }), splitListe.get(0));

        // test sur grande collection 
        liste = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 });
        splitListe = CollectionUtils.split(liste, 4);
        assertNotNull(splitListe);
        assertEquals(4, splitListe.size());
        assertEquals(Arrays.asList(new Integer[] { 1, 2, 3, 4 }), splitListe.get(0));
        assertEquals(Arrays.asList(new Integer[] { 5, 6, 7, 8 }), splitListe.get(1));
        assertEquals(Arrays.asList(new Integer[] { 9, 10, 11, 12 }), splitListe.get(2));
        assertEquals(Arrays.asList(new Integer[] { 13 }), splitListe.get(3));

        // Test avec split de 1
        liste = Arrays.asList(new Integer[] { 1, 2, 3 });
        splitListe = CollectionUtils.split(liste, 1);
        assertEquals(3, splitListe.size());
        assertEquals(Arrays.asList(new Integer[] { 1 }), splitListe.get(0));
        assertEquals(Arrays.asList(new Integer[] { 2 }), splitListe.get(1));
        assertEquals(Arrays.asList(new Integer[] { 3 }), splitListe.get(2));
    }

    /**
     *
     */
    public void testSplit2() {
        final List<Integer> liste = new ArrayList<Integer>();
        for (int i = 0; i < 2000; i++) {
            liste.add(i);
        }

        final List<List<Integer>> splitListe = CollectionUtils.split(liste);
        for (List<Integer> smallList : splitListe) {
            assertNotNull(smallList);
            assertTrue(smallList.size() > 0);
            assertTrue(smallList.size() < 1024);
        }
    }

    /**
     *
     */
    public void testSplit3() {
        final List<Integer> liste = new ArrayList<Integer>();
        for (int i = 0; i < 1521; i++) {
            liste.add(i);
        }

        final List<List<Integer>> splitListe = CollectionUtils.split(liste, 311);
        assertTrue(liste.size() == 1521);
        assertTrue(splitListe.size() == 5);
        for (List<Integer> smallList : splitListe) {
            assertNotNull(smallList);
            assertTrue((smallList.size() == 311) || (smallList.size() == 277));
        }
    }

    /**
     *
     */
    public void testCloneCollectionEchec() {
        final String chaine = "";
        try {
            CollectionUtils.cloneCollection(chaine);
            fail();
        } catch (Exception e) {
            log.debug(e, "Exception capturée");
        }

        final long l = 1;
        try {
            CollectionUtils.cloneCollection(l);
            fail();
        } catch (Exception e) {
            log.debug(e, "Exception capturée");
        }

        final Date date = null;
        try {
            assertNull(CollectionUtils.cloneCollection(date));
        } catch (Exception e) {
            log.debug(e, "Exception capturée");
        }
    }

    /**
     *
     *
     * @throws CloneNotSupportedException DOCUMENTATION INCOMPLETE!
     */
    @SuppressWarnings({ "rawtypes" })
    public void testCloneCollectionTableau() throws CloneNotSupportedException {
        //      test de la méthode clone d'une map
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("TestDto1", new Dto1());
        map.put("TestDto2", new Dto3());

        final ArrayList<Object> collection = new ArrayList<Object>();
        collection.add(new Dto2());
        collection.add(new Dto1());

        final Object[] tableauObject = new Object[2];
        tableauObject[0] = map;
        tableauObject[1] = collection;

        final String[] tableauO = new String[2];
        final Object t = tableauO;
        if (t.getClass().isArray()) {
            final Object[] oArr = Object[].class.cast(t);
            Arrays.toString(oArr);
            log.debug("la aaa");
        }
        final Object[] tableauObjectCloner =
            CollectionUtils.cloneCollection(tableauObject);

        //test des references
        assertNotSame(((Map) tableauObject[0]).get("TestDto1"),
                      ((Map) tableauObjectCloner[0]).get("TestDto1"));

        assertNotSame(((Dto1) ((Map) tableauObject[0]).get("TestDto1")).getDto2(),
                      ((Dto1) ((Map) tableauObjectCloner[0]).get("TestDto1")).getDto2());

        assertNotSame(((Map) tableauObject[0]).get("TestDto2"),
                      ((Map) tableauObjectCloner[0]).get("TestDto2"));

        assertNotSame(((ArrayList) tableauObject[1]).get(0),
                      ((ArrayList) tableauObjectCloner[1]).get(0));

        assertNotSame(((ArrayList) tableauObject[1]).get(1),
                      ((ArrayList) tableauObjectCloner[1]).get(1));
    }

    /**
     *
     *
     * @throws CloneNotSupportedException DOCUMENTATION INCOMPLETE!
     */
    @SuppressWarnings({ "rawtypes" })
    public void testCloneCollectionMap() throws CloneNotSupportedException {
        //      test de la méthode clone d'une map
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("TestDto1", new Dto1());
        map.put("TestDto2", new Dto3());

        final ArrayList<Object> collection = new ArrayList<Object>();
        collection.add(new Dto2());
        collection.add(new Dto1());

        final Map<String, Object> mapAcloner = new HashMap<String, Object>();
        mapAcloner.put("element1", map);
        mapAcloner.put("element2", collection);

        final Map<String, Object> mapCloner = CollectionUtils.cloneCollection(mapAcloner);

        //test des references
        assertNotSame(((Map) mapAcloner.get("element1")).get("TestDto1"),
                      ((Map) mapCloner.get("element1")).get("TestDto1"));

        assertNotSame(((Dto1) ((Map) mapAcloner.get("element1")).get("TestDto1")).getDto2(),
                      ((Dto1) ((Map) mapCloner.get("element1")).get("TestDto1")).getDto2());

        assertNotSame(((Map) mapAcloner.get("element1")).get("TestDto2"),
                      ((Map) mapCloner.get("element1")).get("TestDto2"));

        assertNotSame(((ArrayList) mapAcloner.get("element2")).get(0),
                      ((ArrayList) mapCloner.get("element2")).get(0));

        assertNotSame(((ArrayList) mapAcloner.get("element2")).get(1),
                      ((ArrayList) mapCloner.get("element2")).get(1));
    }

    /**
     *
     *
     * @throws CloneNotSupportedException DOCUMENTATION INCOMPLETE!
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testCloneCollectionCollection() throws CloneNotSupportedException {
        //      test de la méthode clone d'une collection
        final ArrayList<Map<String, Object>> collectionMap =
            new ArrayList<Map<String, Object>>();

        final Map<String, Object> mapElement1 = new HashMap<String, Object>();
        mapElement1.put("TestDto1", new Dto1());
        mapElement1.put("TestDto2", new Dto2());

        final ArrayList<Map<String, Object>> collectionMap2 =
            new ArrayList<Map<String, Object>>();
        collectionMap2.add(mapElement1);

        final Map<String, Object> mapElement2 = new HashMap<String, Object>();
        mapElement2.put("element1", collectionMap2);
        mapElement2.put("element2", 4);

        collectionMap.add(mapElement1);
        collectionMap.add(mapElement2);

        final ArrayList<Map<String, Object>> collectionMapClone =
            CollectionUtils.cloneCollection(collectionMap);

        //test des references
        assertNotSame(collectionMap.get(0).get("TestDto1"),
                      collectionMapClone.get(0).get("TestDto1"));

        assertNotSame(((Dto1) collectionMap.get(0).get("TestDto1")).getDto2(),
                      ((Dto1) collectionMapClone.get(0).get("TestDto1")).getDto2());

        assertNotSame(collectionMap.get(0).get("TestDto2"),
                      collectionMapClone.get(0).get("TestDto2"));

        assertNotSame(collectionMap.get(1).get("element1"),
                      collectionMapClone.get(1).get("element1"));

        assertEquals(collectionMap.get(1).get("element2"),
                     collectionMapClone.get(1).get("element2"));

        assertNotSame(((Map<String, Object>) ((Collection) collectionMap.get(1)
                                                                        .get("element1")).toArray()[0]).get("TestDto1"),
                      ((Map<String, Object>) ((Collection) collectionMapClone.get(1)
                                                                             .get("element1")).toArray()[0]).get("TestDto1"));
    }

    /**
     *
     */
    public void testElementToUpperCaseTypeTableau() {
        final String[] tableau = new String[] { "aa", "àà" };
        final String[] result =
            (String[]) CollectionUtils.elementSansAccensMajuscule(tableau);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("AA", result[0]);
        assertEquals("AA", result[1]);
    }

    /**
     *
     */
    public void testElementToUpperCaseTypeCollection() {
        final Set<String> set = new HashSet<String>();
        set.add("aa");
        set.add("àà");

        final HashSet<String> resultSet =
            new HashSet<String>(CollectionUtils.elementSansAccensMajuscule(set));
        assertNotNull(resultSet);
        assertEquals(1, resultSet.size());
        assertEquals("AA", resultSet.toArray()[0]);

        final List<String> liste = new ArrayList<String>();
        liste.add("aa");
        liste.add("àà");

        final ArrayList<String> resultListe =
            new ArrayList<String>(CollectionUtils.elementSansAccensMajuscule(liste));
        assertNotNull(resultListe);
        assertEquals(2, resultListe.size());
        assertEquals("AA", resultListe.get(0));
        assertEquals("AA", resultListe.get(1));
    }

    /**
     *
     */
    public void testElementSansAccentTypeCollection() {
        final Set<String> set = new HashSet<String>();
        set.add("aa");
        set.add("àà");

        final HashSet<String> resultSet =
            new HashSet<String>(CollectionUtils.elementSansAccens(set));
        assertNotNull(resultSet);
        assertEquals(1, resultSet.size());
        assertEquals("aa", resultSet.toArray()[0]);

        final List<String> liste = new ArrayList<String>();
        liste.add("aa");
        liste.add("àà");

        final ArrayList<String> resultListe =
            new ArrayList<String>(CollectionUtils.elementSansAccens(liste));
        assertNotNull(resultListe);
        assertEquals(2, resultListe.size());
        assertEquals("aa", resultListe.get(0));
        assertEquals("aa", resultListe.get(1));
    }

    /**
     *
     */
    public void testCollectionToString() {
        final Collection<String> collection = new ArrayList<String>();
        collection.add("test1");
        collection.add("test2");
        collection.add("test3");
        final String result = CollectionUtils.collectionToString(collection);
        assertEquals("test1 test2 test3", result);

        final Collection<String> collectionVide = new ArrayList<String>();
        final String resultVide = CollectionUtils.collectionToString(collectionVide);
        assertEquals("", resultVide);
    }

    /**
     *
     */
    public void testCollectionToStringSeparateur() {
        final Collection<String> collection = new ArrayList<String>();
        collection.add("test1");
        collection.add("test2");
        collection.add("test3");
        final String result = CollectionUtils.collectionToString(collection, ",");
        assertEquals("test1,test2,test3", result);

        final Collection<String> collectionVide = new ArrayList<String>();
        final String resultVide = CollectionUtils.collectionToString(collectionVide, ",");
        assertEquals("", resultVide);
    }

    /**
     *
     */
    public void testIsUniqueList() {
        final Collection<Object> collection = new ArrayList<Object>();
        collection.add("test1");
        collection.add("test2");
        collection.add("test3");
        collection.add("test1");
        collection.add("test3");
        final Boolean result1 = CollectionUtils.hasNoDuplicateElement(collection);
        assertEquals(Boolean.FALSE, result1);

        collection.clear();
        collection.add("test1");
        collection.add("test2");
        collection.add("test3");
        collection.add("test4");
        collection.add("test5");
        final Boolean result2 = CollectionUtils.hasNoDuplicateElement(collection);
        assertEquals(Boolean.TRUE, result2);

        collection.clear();
        collection.add("test1");
        collection.add("test2");
        collection.add("test3");
        collection.add(null);
        collection.add(null);
        final Boolean result3 = CollectionUtils.hasNoDuplicateElement(collection);
        assertEquals(Boolean.FALSE, result3);

        final Dto1 dto11 = new Dto1();
        dto11.setBigDecimal(new BigDecimal(5));
        dto11.setChaineO("test");
        dto11.setDoubleO(new Double(0D));
        dto11.setDoubleP(1D);
        dto11.setEntierO(2);
        dto11.setLongO(3L);
        dto11.setLongP(4L);

        final Dto1 dto12 = new Dto1();
        dto12.setBigDecimal(new BigDecimal(5));
        dto12.setChaineO("test");
        dto12.setDoubleO(new Double(0D));
        dto12.setDoubleP(1D);
        dto12.setEntierO(2);
        dto12.setLongO(3L);
        dto12.setLongP(4L);

        final Dto1 dto13 = new Dto1();
        dto13.setBigDecimal(new BigDecimal(5));
        dto13.setChaineO(null);
        dto13.setDoubleO(new Double(0D));
        dto13.setDoubleP(1D);
        dto13.setEntierO(2);
        dto13.setLongO(3L);
        dto13.setLongP(4L);

        collection.clear();
        collection.add(dto11);
        collection.add(dto11);
        final Boolean result4 = CollectionUtils.hasNoDuplicateElement(collection);
        assertEquals(Boolean.FALSE, result4);

        collection.clear();
        collection.add(dto11);
        collection.add(dto12);
        final Boolean result5 = CollectionUtils.hasNoDuplicateElement(collection);
        assertEquals(Boolean.TRUE, result5);

        collection.clear();
        collection.add(dto11);
        collection.add(dto12);
        collection.add(dto13);
        final Boolean result6 = CollectionUtils.hasNoDuplicateElement(collection);
        assertEquals(Boolean.TRUE, result6);
    }

    /**
     *
     */
    public void testIsDisjoint() {
        final Collection<Object> collection1 = new ArrayList<Object>();
        collection1.add("test1");
        collection1.add("test2");
        collection1.add("test3");
        collection1.add("test4");
        collection1.add("test5");

        final Collection<Object> collection2 = new ArrayList<Object>();
        collection2.add("test6");
        collection2.add("test7");
        collection2.add("test8");
        collection2.add("test9");
        collection2.add("test10");

        final Collection<Object> collection3 = new ArrayList<Object>();
        collection3.add("test1");
        collection3.add("test2");
        collection3.add("test3");
        collection3.add("test4");
        collection3.add("test5");

        final Collection<Object> collection4 = new ArrayList<Object>();
        collection4.add("test1");
        collection4.add("test10");

        final Boolean result1 = CollectionUtils.disjoint(collection1, collection1);
        assertEquals(Boolean.FALSE, result1);

        final Boolean result2 = CollectionUtils.disjoint(collection1, collection2);
        assertEquals(Boolean.TRUE, result2);

        final Boolean result3 = CollectionUtils.disjoint(collection1, collection3);
        assertEquals(Boolean.FALSE, result3);

        final Boolean result4 = CollectionUtils.disjoint(collection1, collection4);
        assertEquals(Boolean.FALSE, result4);

        final Boolean result5 = CollectionUtils.disjoint(collection2, collection4);
        assertEquals(Boolean.FALSE, result5);
    }

    /**
     *
     */
    public void testSublist() {
        final List<Object> list = new ArrayList<Object>();
        list.add("test1");
        list.add("test2");
        list.add("test3");

        list.add("test4");
        list.add("test5");
        list.add("test6");

        list.add("test7");
        list.add("test8");
        list.add("test9");

        final List<Object> resultList = CollectionUtils.sublist(list);
        assertEquals(list, resultList);

        final List<Object> resultList2 = CollectionUtils.sublist(list, 1, 3);
        assertEquals(list.subList(0, 3), resultList2);

        final List<Object> resultList3 = CollectionUtils.sublist(list, 2, 3);
        assertEquals(list.subList(3, 6), resultList3);

        final List<Object> resultList4 = CollectionUtils.sublist(list, 2);
        assertEquals(list, resultList4);

        final List<Object> resultList5 = CollectionUtils.sublist(list, 4, 3);
        assertEquals(Collections.emptyList(), resultList5);

        final List<Object> resultList6 = CollectionUtils.sublist(list, 2, 6);
        assertEquals(list.subList(6, 9), resultList6);
    }

    /**
     *
     */
    public void testAddAll() {
        final List<Object> list1 = new ArrayList<Object>();
        list1.add("test1");
        list1.add("test2");
        list1.add("test3");

        final List<Object> list2 = new ArrayList<Object>();
        list2.add("test1");
        list2.add("test2");
        list2.add("test3");

        final List<Object> list3 = new ArrayList<Object>();
        list3.add("test1");
        list3.add("test2");
        list3.add("test3");
        list3.add("test1");
        list3.add("test2");
        list3.add("test3");

        final List<Object> list4 = null;

        final List<Object> list5 = Collections.emptyList();

        assertEquals(CollectionUtils.addAll(list1, list2), list3);
        assertEquals(CollectionUtils.addAll(list1, list4), list1);
        assertEquals(CollectionUtils.addAll(list1, list5), list1);
    }  

    /**
     *
     */
    public void testCreerSet() {
        final Collection<Integer> liste1 = Arrays.asList(new Integer[] { 1, 2, 3 });
        final Set<Integer> set1 = CollectionUtils.creerSet(liste1);
        assertEquals(3, set1.size());

        final Collection<Integer> liste0 = Arrays.asList(new Integer[] {  });
        final Set<Integer> set0 = CollectionUtils.creerSet(liste0);
        assertEquals(0, set0.size());

        final Collection<Integer> listeNull = null;
        final Set<Integer> setNull = CollectionUtils.creerSet(listeNull);
        assertEquals(0, setNull.size());

        final Collection<Integer> listeDoublons =
            Arrays.asList(new Integer[] { 1, 1, 2, 3 });
        
        final Set<Integer> setDoublons = CollectionUtils.creerSet(listeDoublons);
        assertEquals(3, setDoublons.size());
    }

    /**
     *
     */
    public void testCreerSetTableau() {
        final Set<Integer> set1 = CollectionUtils.creerSet(1, 2, 3);
        assertEquals(3, set1.size());

        final Set<Integer> set0 = CollectionUtils.creerSet(new Integer[] {  });
        assertEquals(0, set0.size());

        final Set<Integer> setNull = CollectionUtils.creerSet();
        assertEquals(0, setNull.size());

        final Set<Integer> setDoublons = CollectionUtils.creerSet(1, 1, 2, 3);
        assertEquals(3, setDoublons.size());
    }

    /**
     *
     */
    public void testCreerList() {
        final Collection<Integer> col1 = Arrays.asList(new Integer[] { 1, 2, 3 });
        final List<Integer> liste1 = CollectionUtils.creerList(col1);
        assertEquals(3, liste1.size());

        final Collection<Integer> col2 = Arrays.asList(new Integer[] {  });
        final List<Integer> liste2 = CollectionUtils.creerList(col2);
        assertEquals(0, liste2.size());

        final List<Integer> liste3 =
            CollectionUtils.creerList((Collection<Integer>) null);
        assertEquals(0, liste3.size());
    }

    /**
     *
     */
    public void testCreerListTableau() {
        final List<Integer> liste1 = CollectionUtils.creerList(1, 2, 3);
        assertEquals(3, liste1.size());

        final List<Integer> liste2 = CollectionUtils.creerList();
        assertEquals(0, liste2.size());

        final List<Integer> liste3 = CollectionUtils.creerList(new Integer[] {  });
        assertEquals(0, liste3.size());
    }

    /**
     *
     */
    public void testJoin() {
        final Collection<Integer> col1 = Arrays.asList(new Integer[] { 1, 2, 3 });
        final Collection<Integer> col2 = Arrays.asList(new Integer[] { 1, 11, 12, 13 });
        final Collection<Collection<Integer>> colCol = new ArrayList<Collection<Integer>>();
        colCol.add(col1);
        colCol.add(col2);

        final List<Integer> join = CollectionUtils.join(colCol);
        assertNotNull(join);
        assertEquals(7, join.size());
    }

    /**
     *
     */
    public void testJoinSansDoublons() {
        final Collection<Integer> col1 = Arrays.asList(new Integer[] { 1, 2, 3 });
        final Collection<Integer> col2 = Arrays.asList(new Integer[] { 1, 11, 12, 13 });
        final Collection<Collection<Integer>> colCol = new ArrayList<Collection<Integer>>();
        colCol.add(col1);
        colCol.add(col2);

        final Set<Integer> join = CollectionUtils.joinSansDoublons(colCol);
        assertNotNull(join);
        assertEquals(6, join.size());
    }

    /**
     *
     */
    public void testGetElement() {
        final List<String> list1 = new ArrayList<String>();
        list1.add("test1");
        list1.add("test2");
        list1.add("test3");

        final List<Object> list2 = null;

        final List<Object> list3 = Collections.emptyList();

        assertEquals(null, CollectionUtils.getElement(list1, -1));
        assertEquals("test1", CollectionUtils.getElement(list1, 0));
        assertEquals("test2", CollectionUtils.getElement(list1, 1));
        assertEquals("test3", CollectionUtils.getElement(list1, 2));
        assertEquals(null, CollectionUtils.getElement(list1, 3));
        assertEquals(null, CollectionUtils.getElement(list1, 4));

        assertEquals(null, CollectionUtils.getElement(list2, -1));
        assertEquals(null, CollectionUtils.getElement(list2, 0));
        assertEquals(null, CollectionUtils.getElement(list2, 1));
        assertEquals(null, CollectionUtils.getElement(list2, 2));
        assertEquals(null, CollectionUtils.getElement(list2, 3));
        assertEquals(null, CollectionUtils.getElement(list2, 4));

        assertEquals(null, CollectionUtils.getElement(list3, -1));
        assertEquals(null, CollectionUtils.getElement(list3, 0));
        assertEquals(null, CollectionUtils.getElement(list3, 1));
        assertEquals(null, CollectionUtils.getElement(list3, 2));
        assertEquals(null, CollectionUtils.getElement(list3, 3));
        assertEquals(null, CollectionUtils.getElement(list3, 4));
    }

    /**
     *
     */
    public void testAjouteValeurDansListeMap() {
        final Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        CollectionUtils.ajouteValeurDansListeMap(map, "A", 1);
        CollectionUtils.ajouteValeurDansListeMap(map, "A", 2);
        CollectionUtils.ajouteValeurDansListeMap(map, "B", 10);
        CollectionUtils.ajouteValeurDansListeMap(map, "B", 10);
        CollectionUtils.ajouteValeurDansListeMap(map, "C", 100);

        assertEquals(2, map.get("A").size());
        assertEquals(2, map.get("B").size());
        assertEquals(1, map.get("C").size());
        assertNull(map.get("D"));
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public void testJoin2() {
        final List<String> listeLettres = CollectionUtils.creerList("A", "B");
        final Set<String> setChiffres = CollectionUtils.creerSet("1", "2");
        final List<String> listeTotale = CollectionUtils.join(listeLettres, setChiffres);
        assertEquals(4, listeTotale.size());
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public void testAddInMap() {
        final Map<Integer, Map<Integer, Map<String, Integer>>> map =
            new HashMap<Integer, Map<Integer, Map<String, Integer>>>();

        CollectionUtils.addInMap(map, 1, 2, "124", 4);
        CollectionUtils.addInMap(map, 1, 2, "123", 3);
        CollectionUtils.addInMap(map, 1, 4, "143", 3);

        final Map<Integer, Map<String, Integer>> map2 = map.get(1);
        final Map<String, Integer> map3 = map2.get(2);
        assertEquals(4, map3.get("124").intValue());
        assertEquals(3, map3.get("123").intValue());

        final Map<String, Integer> map4 = map2.get(4);
        assertEquals(3, map4.get("143").intValue());
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public void testAddInListMap() {
        final Map<Integer, Map<Integer, Map<String, List<Integer>>>> map =
            new HashMap<Integer, Map<Integer, Map<String, List<Integer>>>>();

        CollectionUtils.addInListMap(map, 1, 2, "124", 4);
        CollectionUtils.addInListMap(map, 1, 2, "124", 5);
        CollectionUtils.addInListMap(map, 1, 2, "124", 6);
        CollectionUtils.addInListMap(map, 1, 2, "124", 7);
        CollectionUtils.addInListMap(map, 1, 2, "123", 3);
        CollectionUtils.addInListMap(map, 1, 4, "143", 3);

        final Map<Integer, Map<String, List<Integer>>> map2 = map.get(1);
        final Map<String, List<Integer>> map3 = map2.get(2);
        assertTrue(map3.get("124").contains(4));
        assertTrue(map3.get("124").contains(5));
        assertTrue(map3.get("124").contains(6));
        assertTrue(map3.get("124").contains(7));

        assertTrue(map3.get("123").contains(3));

        final Map<String, List<Integer>> map4 = map2.get(4);
        assertTrue(map4.get("143").contains(3));

        final Map<Integer, List<String>> mapA = new HashMap<Integer, List<String>>();
        CollectionUtils.addInListMap(mapA, 1, "AB");
        CollectionUtils.addInListMap(mapA, 1, "CD");
        CollectionUtils.addInListMap(mapA, 1, "EF");
        CollectionUtils.addInListMap(mapA, 2, "Z");

        assertTrue(mapA.get(1).contains("AB"));
        assertTrue(mapA.get(1).contains("CD"));
        assertTrue(mapA.get(1).contains("EF"));
        assertFalse(mapA.get(1).contains("Z"));

        assertTrue(mapA.get(2).contains("Z"));
    }

    /**
     *
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testAddInMapEtFind() {
        final Map<Integer, Map<Integer, Map<String, Integer>>> map =
            new HashMap<Integer, Map<Integer, Map<String, Integer>>>();

        CollectionUtils.addInMap(map, 1, 2, "124", 4);
        CollectionUtils.addInMap(map, 1, 2, "123", 3);
        CollectionUtils.addInMap(map, 1, 4, "143", 3);

        assertEquals(4, CollectionUtils.findInMap(map, 1, 2, "124"));
        final Map map12 = CollectionUtils.findInMap(map, 1, 2);
        assertTrue(map12.containsKey("124"));
        assertEquals(4, map12.get("124"));
    }
}
