/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ObjectUtilsTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils;

import junit.framework.TestCase;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import org.crlr.utils.dto.Dto1;
import org.crlr.utils.dto.Dto2;
import org.crlr.utils.dto.Dto3;
import org.crlr.utils.dto.Enum;

import java.lang.reflect.Field;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Test de la classe {@link ObjectUtils}.
 *
 * @author breytond
 */
public class ObjectUtilsTest extends TestCase {
    /** DOCUMENTATION INCOMPLETE! */
    private final Log log = LogFactory.getLog(getClass());

    /**
     * 
     */
    public void testDefaultIfNull() {
        assertEquals("hello", ObjectUtils.defaultIfNull("hello", "world"));
        assertEquals("hello", ObjectUtils.defaultIfNull("hello", null));
        assertEquals("world", ObjectUtils.defaultIfNull(null, "world"));
        assertNull(ObjectUtils.defaultIfNull(null, null));
    }

    /**
     * 
     */
    public void testEquals() {
        assertTrue(ObjectUtils.equals(null, null));
        assertTrue(ObjectUtils.equals(Integer.valueOf(1), Integer.valueOf(1)));
        assertFalse(ObjectUtils.equals(Integer.valueOf(1), Integer.valueOf(2)));
        assertFalse(ObjectUtils.equals(null, Integer.valueOf(1)));
        assertFalse(ObjectUtils.equals(Integer.valueOf(1), null));
    }

    /**
     * 
     */
    public void testEqualsAndNotNull() {
        assertFalse(ObjectUtils.equalsAndNotNull(null, null));
        assertTrue(ObjectUtils.equalsAndNotNull(Integer.valueOf(1), Integer.valueOf(1)));
        assertFalse(ObjectUtils.equalsAndNotNull(Integer.valueOf(1), Integer.valueOf(2)));
        assertFalse(ObjectUtils.equalsAndNotNull(null, Integer.valueOf(1)));
        assertFalse(ObjectUtils.equalsAndNotNull(Integer.valueOf(1), null));
    }

    /**
     * 
     */
    public void testCompare() {
        assertTrue(ObjectUtils.compare("TOTO", "TATA", true) > 0);
        assertTrue(ObjectUtils.compare(" TOTO", "TATA", true) < 0);
        assertTrue(ObjectUtils.compare(null, null, true) == 0);
        assertTrue(ObjectUtils.compare(null, "TOTO", true) < 0);
        assertTrue(ObjectUtils.compare(null, "TOTO", false) > 0);
        assertTrue(ObjectUtils.compare("TOTO", null, true) > 0);
        assertTrue(ObjectUtils.compare("TOTO", null, false) < 0);
    }

    /**
     * 
     */
    public void testGetFieldsNull() {
        assertTrue(ObjectUtils.getFields(null).isEmpty());
    }

    /**
     * 
     */
    public void testGetFields() {
        final Employe employe = new Employe();
        employe.setNom("DUPOND");
        employe.setPrenom("Michel");
        employe.setDateEntree(DateUtils.creer(2006, Calendar.JULY, 10));
        employe.setSexe(Sexe.MASCULIN);

        final Employeur employeur = new Employeur();
        employeur.getEmployes().add(employe);

        final Set<Field> employeurFields = ObjectUtils.getFields(employeur);
        assertNotNull(employeurFields);
        assertEquals(1, employeurFields.size());

        final Field employesField = employeurFields.iterator().next();
        assertEquals("employes", employesField.getName());

        final Set<Field> employeFields = ObjectUtils.getFields(employe);
        assertNotNull(employeFields);
        assertEquals(4, employeFields.size());

        final Set<String> employeFieldNames = new HashSet<String>(4);
        employeFieldNames.add("dateEntree");
        employeFieldNames.add("prenom");
        employeFieldNames.add("nom");
        employeFieldNames.add("sexe");

        for (final Field field : employeFields) {
            if (!employeFieldNames.contains(field.getName())) {
                fail("Champ inconnu : " + field.getName());
            }
        }
    }

    /**
     * 
     */
    public void testCloneObjetEchec() {
        final String chaine = "";
        try {
            ObjectUtils.clone(chaine);
            fail();
        } catch (final Exception e) {
            log.debug(e, "Exception capturée");
        }

        final long l = 1;
        try {
            ObjectUtils.clone(l);
            fail();
        } catch (final Exception e) {
            log.debug(e, "Exception capturée");
        }

        final List<Long> liste = new ArrayList<Long>();
        try {
            final List<Long> listeClonee = ObjectUtils.clone(liste);
            assertNotSame(liste, listeClonee);
        } catch (final Exception e) {
            fail();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENTATION INCOMPLETE!
     */
    public void testCloneObjetComplexe() throws CloneNotSupportedException {
        //      test de la méthode clone 
        final Dto3 testDto3 = new Dto3();
        //      objets immuables
        testDto3.setChaine("Le clonage");
        testDto3.setEntier(10);
        testDto3.setLongue(10L);
        testDto3.setDate(DateUtils.creer(2007, Calendar.MAY, 9));

        //test de collections complexes parfois héritées
        final List<Map<Long, ?>> listeMapLongObject = new ArrayList<Map<Long, ?>>();
        final Map<Long, Object> mapPourListeMapLongObject = new HashMap<Long, Object>();
        mapPourListeMapLongObject.put(1L, 2);
        mapPourListeMapLongObject.put(2L, new Dto1());
        listeMapLongObject.add(mapPourListeMapLongObject);
        testDto3.setListeMapLongObject(listeMapLongObject);

        final List<Map<String, Dto2>> listeMapStringTestDto2 =
            new ArrayList<Map<String, Dto2>>();
        final Map<String, Dto2> mapPourListeMapStringTestDto2 =
            new HashMap<String, Dto2>();
        final Dto2 testDto2 = new Dto2();
        testDto2.setChaine("Le clonage bis");
        testDto2.setEntier(30);

        final List<Map<Long, ?>> listeMapPourDto2 = new ArrayList<Map<Long, ?>>();
        final Map<Long, Object> mapPourListeMapPourDto2 = new HashMap<Long, Object>();
        mapPourListeMapPourDto2.put(2L, 80);
        listeMapPourDto2.add(mapPourListeMapPourDto2);
        testDto2.setListeMapLongObject(listeMapPourDto2);
        mapPourListeMapStringTestDto2.put("unique dto 2", testDto2);
        listeMapStringTestDto2.add(mapPourListeMapStringTestDto2);

        testDto3.setListeMapStringTestDto2(listeMapStringTestDto2);

        //      clonage
        final Dto3 testDto3Clone = ObjectUtils.clone(testDto3);

        //      test d'égalités
        assertEquals(testDto3.getChaine(), testDto3Clone.getChaine());
        assertEquals(testDto3.getEntier(), testDto3Clone.getEntier());
        assertEquals(testDto3.getLongue(), testDto3Clone.getLongue());
        assertEquals(testDto3.getDate(), testDto3Clone.getDate());

        assertEquals(testDto3.getListeMapLongObject().get(0).get(1L),
                     testDto3Clone.getListeMapLongObject().get(0).get(1L));

        assertEquals(testDto3.getListeMapStringTestDto2().get(0).get("unique dto 2")
                             .getEntier(),
                     testDto3Clone.getListeMapStringTestDto2().get(0).get("unique dto 2")
                                  .getEntier());

        //tests des références
        assertNotSame(testDto3.getDate(), testDto3Clone.getDate());
        assertNotSame(testDto3, testDto3Clone);
        assertNotSame(testDto3.getListeMapLongObject(),
                      testDto3Clone.getListeMapLongObject());
        assertNotSame(testDto3.getListeMapLongObject().get(0).get(2L),
                      testDto3Clone.getListeMapLongObject().get(0).get(2L));

        assertNotSame(testDto3.getListeMapStringTestDto2(),
                      testDto3Clone.getListeMapStringTestDto2());
        assertNotSame(testDto3.getListeMapStringTestDto2().get(0),
                      testDto3Clone.getListeMapStringTestDto2().get(0));
        assertNotSame(testDto3.getListeMapStringTestDto2().get(0).get("unique dto 2"),
                      testDto3Clone.getListeMapStringTestDto2().get(0).get("unique dto 2"));
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENTATION INCOMPLETE!
     */
    @SuppressWarnings("unchecked")
    public void testCloneObjetComplexeBis() throws CloneNotSupportedException {
        //      test de la méthode clone 
        final Dto1 testDto1 = new Dto1();
        //      objets immuables
        testDto1.setBigDecimal(new BigDecimal(55.8899));
        testDto1.setChaineO("Le clonage");
        testDto1.setDoubleO(10.22D);
        testDto1.setEntierO(10);
        testDto1.setLongO(10L);

        //primitifs
        testDto1.setDoubleP(20.22d);
        testDto1.setEntierP(20);
        testDto1.setLongP(20L);

        //      objet complexe
        final Dto2 testDto2 = new Dto2();
        testDto2.setChaine("Le clonage bis");
        testDto2.setEntier(30);
        final List<Map<Long, ?>> listeMapPourDto2 = new ArrayList<Map<Long, ?>>();
        final Map<Long, Object> mapPourListeMapPourDto2 = new HashMap<Long, Object>();
        mapPourListeMapPourDto2.put(2L, 80);
        listeMapPourDto2.add(mapPourListeMapPourDto2);
        testDto2.setListeMapLongObject(listeMapPourDto2);
        testDto1.setDto2(testDto2);

        //      collection les plus utilisées
        final List<Long> listeLong = new ArrayList<Long>();
        listeLong.add(10L);
        listeLong.add(11L);
        testDto1.setListeLong(listeLong);

        final Set<Long> setLong = new HashSet<Long>();
        setLong.add(10L);
        setLong.add(11L);
        testDto1.setSetLong(setLong);

        //table de hachage
        final Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("dto2", testDto2);
        testDto1.setHashMap(hashMap);

        final Map<String, Object> hashTable = new Hashtable<String, Object>();
        hashTable.put("dto2", testDto2);
        testDto1.setHashTable(hashTable);

        //collections complexes
        final List<Dto2> listeDto2 = new ArrayList<Dto2>();
        listeDto2.add(testDto2);
        testDto1.setListeDto2(listeDto2);

        //type enumératif
        final Enum enumTest = Enum.TEST;
        testDto1.setEnumTest(enumTest);

        //clonage
        final Dto1 testDto1Clone = ObjectUtils.clone(testDto1);

        //test d'égalité
        assertEquals(testDto1.getBigDecimal(), testDto1Clone.getBigDecimal());
        assertEquals(testDto1.getChaineO(), testDto1Clone.getChaineO());
        assertEquals(testDto1.getDoubleO(), testDto1Clone.getDoubleO());
        assertEquals(testDto1.getEntierO(), testDto1Clone.getEntierO());
        assertEquals(testDto1.getLongO(), testDto1Clone.getLongO());
        assertEquals(testDto1.getDoubleP(), testDto1Clone.getDoubleP());
        assertEquals(testDto1.getEntierP(), testDto1Clone.getEntierP());
        assertEquals(testDto1.getLongP(), testDto1Clone.getLongP());

        final Dto2 testDto2Clone = testDto1Clone.getDto2();
        assertEquals(testDto1.getDto2().getChaine(), testDto2Clone.getChaine());
        assertEquals(testDto1.getDto2().getEntier(), testDto2Clone.getEntier());
        final List<Map<Long, ?>> listeMapPourDto2Clone =
            testDto2Clone.getListeMapLongObject();
        assertEquals(testDto1.getDto2().getListeMapLongObject().get(0).get(2L),
                     listeMapPourDto2Clone.get(0).get(2L));

        //      collection les plus utilisées
        final List<Long> listeLongClone = testDto1Clone.getListeLong();
        assertEquals(testDto1.getListeLong().get(0), listeLongClone.get(0));

        final Set<Long> setLongClone = testDto1Clone.getSetLong();
        assertEquals(testDto1.getSetLong().toArray()[0], setLongClone.toArray()[0]);

        //      collections complexes
        final List<Dto2> listeDto2Clone = testDto1Clone.getListeDto2();
        assertEquals(testDto1.getListeDto2().get(0).getChaine(),
                     listeDto2Clone.get(0).getChaine());

        //type enumératif
        assertEquals(testDto1.getEnumTest(), testDto1Clone.getEnumTest());

        /*test des references*/
        //table de hachage 
        final Map<String, Object> hashMapClone = testDto1Clone.getHashMap();
        assertNotSame(testDto1.getHashMap(), hashMapClone);
        assertNotSame(testDto1.getHashMap().get("dto2"), hashMapClone.get("dto2"));

        final Map<String, Object> hashTableClone = testDto1Clone.getHashTable();
        assertNotSame(testDto1.getHashTable(), hashTableClone);
        assertNotSame(testDto1.getHashTable().get("dto2"), hashTableClone.get("dto2"));

        //Opérations sur l'objet cloné qui ne doivent pas se répercuter sur l'objet source
        //test des objets immuables est inutile comme pour les types primitifs (mais bon ...)
        testDto1Clone.setBigDecimal(new BigDecimal(32.8899));
        assertFalse(testDto1.getBigDecimal().equals(testDto1Clone.getBigDecimal()));
        testDto1Clone.setChaineO(" A reussie ");
        assertFalse(testDto1.getChaineO().equals(testDto1Clone.getChaineO()));
        testDto1Clone.setDoubleO(100D);
        assertFalse(testDto1.getDoubleO().equals(testDto1Clone.getDoubleO()));
        testDto1Clone.setEntierO(100);
        assertFalse(testDto1.getEntierO().equals(testDto1Clone.getEntierO()));
        testDto1Clone.setLongO(100L);
        assertFalse(testDto1.getLongO().equals(testDto1Clone.getLongO()));
        //test des primitifs 
        testDto1Clone.setDoubleP(100d);
        assertTrue(testDto1.getDoubleP() != (testDto1Clone.getDoubleP()));
        testDto1Clone.setEntierP(100);
        assertTrue(testDto1.getEntierP() != (testDto1Clone.getEntierP()));
        testDto1Clone.setLongP(100L);
        assertTrue(testDto1.getLongP() != (testDto1Clone.getLongP()));

        //test de l'objet complexe
        assertNotSame(testDto1.getDto2(), testDto2Clone);
        testDto2Clone.setChaine(" A reussie ");
        testDto2Clone.setEntier(100);
        assertFalse(testDto1.getDto2().getChaine().equals(testDto2Clone.getChaine()));
        assertFalse(testDto1.getDto2().getEntier().equals(testDto2Clone.getEntier()));

        ((Map<Long, Object>) (testDto2Clone.getListeMapLongObject().get(0))).put(2L, 100);
        assertFalse(testDto1.getDto2().getListeMapLongObject().get(0).get(2L)
                            .equals(testDto2Clone.getListeMapLongObject().get(0).get(2L)));

        //      collection les plus utilisées
        assertNotSame(testDto1.getListeLong(), listeLongClone);
        listeLongClone.set(0, 100L);
        assertFalse(testDto1.getListeLong().get(0).equals(listeLongClone.get(0)));

        assertNotSame(testDto1.getSetLong(), setLongClone);
        setLongClone.remove(11L);
        assertFalse(testDto1.getSetLong().size() == setLongClone.size());
        assertFalse(testDto1.getSetLong().toArray()[0].equals(setLongClone.toArray()[0]));

        //      collections complexes
        listeDto2Clone.get(0).setChaine("le clonage a reussie");
        assertNotSame(testDto1.getListeDto2(), listeDto2Clone);
        assertNotSame(testDto1.getListeDto2().get(0), listeDto2Clone.get(0));
        assertFalse(testDto1.getListeDto2().get(0).getChaine()
                            .equals(listeDto2Clone.get(0).getChaine()));

        //type enumératif
        assertSame(testDto1.getEnumTest(), testDto1Clone.getEnumTest());
        testDto1Clone.setEnumTest(Enum.TEST1);
        assertFalse(testDto1.getEnumTest().equals(testDto1Clone.getEnumTest()));
    }

    /**
     * 
     */
    public void testDeepClonableObject() {
        assertFalse(ObjectUtils.isDeepClonableObject(null));
        assertFalse(ObjectUtils.isDeepClonableObject("Je suis une chaine"));
        assertFalse(ObjectUtils.isDeepClonableObject(1));
        assertFalse(ObjectUtils.isDeepClonableObject(1L));
        assertFalse(ObjectUtils.isDeepClonableObject(1.0D));
        assertFalse(ObjectUtils.isDeepClonableObject(new BigDecimal(1)));
        assertFalse(ObjectUtils.isDeepClonableObject(Enum.TEST));
        assertFalse(ObjectUtils.isDeepClonableObject(new String[] { "essai" }));
        assertFalse(ObjectUtils.isDeepClonableObject(new ArrayList<Long>()));
        assertFalse(ObjectUtils.isDeepClonableObject(new HashSet<Long>()));
        assertFalse(ObjectUtils.isDeepClonableObject(new TreeSet<Long>()));
        assertFalse(ObjectUtils.isDeepClonableObject(new HashMap<String, Object>()));
        assertFalse(ObjectUtils.isDeepClonableObject(new Hashtable<String, Object>()));

        assertTrue(ObjectUtils.isDeepClonableObject(DateUtils.creer(2007, Calendar.MAY, 9)));
        assertTrue(ObjectUtils.isDeepClonableObject(new Dto1()));
    }

    /**
     * 
     */
    public void testIterableObject() {
        assertFalse(ObjectUtils.isIterableObject(null));
        assertFalse(ObjectUtils.isIterableObject("Je suis une chaine"));
        assertTrue(ObjectUtils.isIterableObject(Collections.emptyList()));
        assertTrue(ObjectUtils.isIterableObject(new ArrayList<Long>()));
        assertTrue(ObjectUtils.isIterableObject(new Hashtable<String, Object>()));
        assertTrue(ObjectUtils.isIterableObject(new String[] { "essai" }));
    }

    /**
     * 
     */
    public void testIsIn() {
        final String[] obj = { "1", "2", "3" };
        assertTrue(ObjectUtils.isIn("2", obj));
        assertFalse(ObjectUtils.isIn("2", "", null, "1", "3"));
        List<String> l = null;
        assertFalse(ObjectUtils.isIn("2", l));
        l = new ArrayList<String>();
        l.add(null);
        assertTrue(ObjectUtils.isIn(null, l));
    }

    /**
     * Classe de test.
     *
     * @author breytond
     */
    public static class Employeur {
        private Set<Employe> employes = new HashSet<Employe>();

        /**
         * Acesseur.
         *
         * @return objet.
         */
        public Set<Employe> getEmployes() {
            return employes;
        }

        /**
         * Mutateur.
         *
         * @param employes employes.
         */
        public void setEmployes(Set<Employe> employes) {
            this.employes = employes;
        }
    }

    /**
     * Empoye.        
     *
     * @author breytond.
     */
    public static class Employe extends Personne {
        private Date dateEntree;

        /**
         * Acesseur.
         *
         * @return objet.
         */
        public Date getDateEntree() {
            return dateEntree;
        }

        /**
         * Mutateur.
         *
         * @param dateEntree dateEntree.
         */
        public void setDateEntree(Date dateEntree) {
            this.dateEntree = dateEntree;
        }
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @author breytond.
     */
    public static class Personne {
        private String nom;
        private String prenom;
        private Sexe sexe;

        /**
         * Acesseur.
         *
         * @return sexe.
         */
        public Sexe getSexe() {
            return sexe;
        }

        /**
         * Mutateur.
         *
         * @param sexe sexe.
         */
        public void setSexe(Sexe sexe) {
            this.sexe = sexe;
        }

        /**
         * Acesseur.
         *
         * @return nom.
         */
        public String getNom() {
            return nom;
        }

        /**
         * Mutateur.
         *
         * @param nom nom.
         */
        public void setNom(String nom) {
            this.nom = nom;
        }

        /**
         * Acesseur.
         *
         * @return prenom.
         */
        public String getPrenom() {
            return prenom;
        }

        /**
         * Mutateur.
         *
         * @param prenom prenom.
         */
        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }
    }

/**
     * 
     * @author breytond.
     *
     */
    public static enum Sexe { //
        MASCULIN, FEMININ;
    }
}
