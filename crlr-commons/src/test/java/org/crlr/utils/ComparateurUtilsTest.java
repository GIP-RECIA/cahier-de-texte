/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ComparateurUtilsTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils;

import junit.framework.TestCase;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import org.crlr.utils.dto.Dto1;
import org.crlr.utils.dto.Dto2;
import org.crlr.utils.dto.DtoComparateur;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Test de {@link ComparateurUtils}.
 *
 * @author dethillotn
 */
public class ComparateurUtilsTest extends TestCase {
    protected final Log log = LogFactory.getLog(getClass());

    /**
     *
     */
    public void testSort1() {
        final List<DtoComparateur> list = getListeDtoComparateur();

        this.print(list);

        final List<DtoComparateur> sortList = ComparateurUtils.sort(list, "string1");

        this.print(sortList);
    }

    /**
     *
     */
    public void testSort2() {
        final List<DtoComparateur> list = getListeDtoComparateur();

        this.print(list);

        final LinkedHashMap<String, TriComparateur> map =
            new LinkedHashMap<String, TriComparateur>();
        map.put("string1", TriComparateur.CROISSANT);
        final List<DtoComparateur> sortList = ComparateurUtils.sort(list, map);

        this.print(sortList);
    }

    /**
     *
     */
    public void testSort3() {
        final List<DtoComparateur> list = getListeDtoComparateur();

        this.print(list);

        final LinkedHashMap<String, TriComparateur> map =
            new LinkedHashMap<String, TriComparateur>();
        map.put("string1", TriComparateur.DECROISSANT);
        final List<DtoComparateur> sortList = ComparateurUtils.sort(list, map);

        this.print(sortList);
    }

    /**
     *
     */
    public void testSort4() {
        final List<DtoComparateur> list = getListeDtoComparateur();

        this.print(list);

        final LinkedHashMap<String, TriComparateur> map =
            new LinkedHashMap<String, TriComparateur>();
        map.put("string1", TriComparateur.DECROISSANT);
        map.put("date2", TriComparateur.CROISSANT);
        map.put("date1", TriComparateur.DECROISSANT);
        map.put("string2", TriComparateur.CROISSANT);
        final List<DtoComparateur> sortList = ComparateurUtils.sort(list, map);

        this.print(sortList);
    }

    /**
     *
     */
    public void testSort5() {
        final Dto1 dto11 = new Dto1();
        dto11.setDto2(new Dto2());
        dto11.getDto2().setChaine("aaa");

        final Dto1 dto12 = new Dto1();
        dto12.setDto2(new Dto2());
        dto12.getDto2().setChaine("bbb");

        final Dto1 dto13 = new Dto1();
        dto13.setDto2(new Dto2());
        dto13.getDto2().setChaine("ccc");

        final List<Dto1> l1 = new ArrayList<Dto1>();
        l1.add(dto13);
        l1.add(dto12);
        l1.add(dto11);

        Assert.equals("dto13.dto2.chaine", "ccc", l1.get(0).getDto2().getChaine());
        Assert.equals("dto12.dto2.chaine", "bbb", l1.get(1).getDto2().getChaine());
        Assert.equals("dto11.dto2.chaine", "aaa", l1.get(2).getDto2().getChaine());

        final List<Dto1> l2 = ComparateurUtils.sort(l1, "dto2.chaine");

        Assert.equals("dto11.dto2.chaine", "aaa", l2.get(0).getDto2().getChaine());
        Assert.equals("dto12.dto2.chaine", "bbb", l2.get(1).getDto2().getChaine());
        Assert.equals("dto13.dto2.chaine", "ccc", l2.get(2).getDto2().getChaine());
    }

    /**
     *
     */
    public void testSort6() {
        final Dto1 dto11 = new Dto1();
        dto11.setDto2(null);

        final Dto1 dto12 = new Dto1();
        dto12.setDto2(new Dto2());
        dto12.getDto2().setChaine("bbb");

        final Dto1 dto13 = new Dto1();
        dto13.setDto2(null);

        final List<Dto1> l1 = new ArrayList<Dto1>();
        l1.add(dto13);
        l1.add(dto12);
        l1.add(dto11);

        Assert.equals("dto13.dto2", null, l1.get(0).getDto2());
        Assert.equals("dto12.dto2.chaine", "bbb", l1.get(1).getDto2().getChaine());
        Assert.equals("dto11.dto2", null, l1.get(2).getDto2());

        final List<Dto1> l2 = ComparateurUtils.sort(l1, "dto2.chaine");

        Assert.equals("dto12.dto2.chaine", "bbb", l2.get(0).getDto2().getChaine());
        Assert.equals("dto11.dto2", null, l2.get(1).getDto2());
        Assert.equals("dto13.dto2", null, l2.get(2).getDto2());
    }

    /**
     *
     */
    public void testSort7() {
        final Dto1 dto11 = new Dto1();
        dto11.setListeLong(new ArrayList<Long>());
        dto11.getListeLong().add(5L);
        dto11.getListeLong().add(3L);
        dto11.getListeLong().add(2L);

        final Dto1 dto21 = new Dto1();
        dto21.setListeLong(new ArrayList<Long>());
        dto21.getListeLong().add(5L);
        dto21.getListeLong().add(1L);
        dto21.getListeLong().add(2L);

        final List<Dto1> l1 = new ArrayList<Dto1>();
        l1.add(dto11);
        l1.add(dto21);

        final LinkedHashMap<String, TriComparateur> lElemsComparaison =
            new LinkedHashMap<String, TriComparateur>();
        lElemsComparaison.put("listeLong[1]", TriComparateur.CROISSANT);
        Collections.sort(l1,
                         ComparateurUtils.getComparateur(Dto1.class, lElemsComparaison));
        assertEquals(1L, l1.get(0).getListeLong().get(1).longValue());
    }

    /**
     *
     */
    public void testSort8() {
        final Dto1 dto11 = new Dto1();
        dto11.setListeDto2(new ArrayList<Dto2>());
        final Dto2 d21 = new Dto2();
        d21.setChaine("rrre");
        dto11.getListeDto2().add(d21);

        final Dto1 dto21 = new Dto1();
        dto21.setListeDto2(new ArrayList<Dto2>());
        final Dto2 d22 = new Dto2();
        d22.setChaine("aaaaddd");
        dto21.getListeDto2().add(d22);

        final List<Dto1> l1 = new ArrayList<Dto1>();
        l1.add(dto11);
        l1.add(dto21);

        final LinkedHashMap<String, TriComparateur> lElemsComparaison =
            new LinkedHashMap<String, TriComparateur>();
        lElemsComparaison.put("listeDto2[0].chaine", TriComparateur.CROISSANT);
        Collections.sort(l1,
                         ComparateurUtils.getComparateur(Dto1.class, lElemsComparaison));
        assertEquals("aaaaddd", l1.get(0).getListeDto2().get(0).getChaine());
    }

    /**
     *
     *
     * @param list DOCUMENTATION INCOMPLETE!
     */
    private void print(List<DtoComparateur> list) {
        log.debug(" --- --- --- ");
        for (final DtoComparateur dtoComparateur : list) {
            log.debug(" --- " + dtoComparateur.toString());
        }
        log.debug(" --- --- --- ");
    }

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    private List<DtoComparateur> getListeDtoComparateur() {
        final List<DtoComparateur> list = new ArrayList<DtoComparateur>();
        list.add(new DtoComparateur("aaa", "aaa",
                                    DateUtils.creer(2000, Calendar.JANUARY, 1),
                                    DateUtils.creer(2001, Calendar.JANUARY, 1)));
        list.add(new DtoComparateur("aaa", "aaa",
                                    DateUtils.creer(2000, Calendar.JANUARY, 1),
                                    DateUtils.creer(2002, Calendar.JANUARY, 1)));
        list.add(new DtoComparateur("ccc", "ccc",
                                    DateUtils.creer(2003, Calendar.JANUARY, 1),
                                    DateUtils.creer(2001, Calendar.JANUARY, 1)));
        list.add(new DtoComparateur("eee", "eee",
                                    DateUtils.creer(1900, Calendar.JANUARY, 1),
                                    DateUtils.creer(2001, Calendar.JANUARY, 1)));
        list.add(new DtoComparateur("bbb", "bbb",
                                    DateUtils.creer(3000, Calendar.JANUARY, 1),
                                    DateUtils.creer(2001, Calendar.JANUARY, 1)));
        list.add(new DtoComparateur("ccc", "ccc",
                                    DateUtils.creer(2000, Calendar.JANUARY, 1),
                                    DateUtils.creer(2007, Calendar.JANUARY, 1)));
        return list;
    }
}
