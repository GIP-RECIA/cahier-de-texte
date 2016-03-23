/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: StringUtilsTest.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.utils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.engine.util.JEditorPaneHtmlMarkupProcessor;

import org.crlr.exception.base.CrlrRuntimeException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test de {@link StringUtils}.
 *
 * @author romana
 * @author breytond
 */
public class StringUtilsTest  {
    /** DOCUMENTATION INCOMPLETE! */
    private final Logger log = LoggerFactory.getLogger(getClass());

   
    @Test
    public void testStripFontStyles() {
        String test1 = "<div class=\"page\" title=\"Page 9\">\n\t<div class=\"layoutArea\">\n\t\t<div class=\"column\">\n\t\t\t<ul>\n\t\t\t\t<li>\n\t\t\t\t\t<span style=\"font-size: 10.000000pt; font-family: 'Times'\">Correction <strong><span style=\"background-color:#ffd700;\">DSM2</span></strong></span></li>\n\t\t\t\t<li>\n\t\t\t\t\t<span style=\"font-size: 10.000000pt; font-family: 'Times'\">&nbsp;Fin de </span><span style=\"font-size: 10.000000pt; font-family: 'Times'; background-color: rgb(100.000000%, 100.000000%, 100.000000%)\">Act. 2 p.209: <span style=\"color:#808080;\"><em>La distance de freinage croit-elle plus vite que la vitesse </em></span></span><span style=\"color:#808080;\"><em><span style=\"font-size: 10pt; font-family: 'Times';\">?</span></em></span></li>\n\t\t\t\t<li>\n\t\t\t\t\tCours:</li>\n\t\t\t</ul>\n\t\t\t<p style=\"margin-left: 40px;\">\n\t\t\t\t<span style=\"color:#808080;\">II. Distances de freinage</span></p>\n\t\t\t<p style=\"margin-left: 80px;\">\n\t\t\t\t<span style=\"color:#808080;\">1. Vocabulaire</span></p>\n\t\t\t<p>\n\t\t\t\t&nbsp;</p>\n\t\t</div>\n\t</div>\n</div>\n<p>\n\t&nbsp;</p>\n";

        String fontStriped1 = "<div class=\"page\" title=\"Page 9\">\n\t<div class=\"layoutArea\">\n\t\t<div class=\"column\">\n\t\t\t<ul>\n\t\t\t\t<li>\n\t\t\t\t\t<span style=\"font-size: 10.000000pt; \">Correction <strong><span style=\"background-color:#ffd700;\">DSM2</span></strong></span></li>\n\t\t\t\t<li>\n\t\t\t\t\t<span style=\"font-size: 10.000000pt; \">&nbsp;Fin de </span><span style=\"font-size: 10.000000pt;  background-color: rgb(100.000000%, 100.000000%, 100.000000%)\">Act. 2 p.209: <span style=\"color:#808080;\"><em>La distance de freinage croit-elle plus vite que la vitesse </em></span></span><span style=\"color:#808080;\"><em><span style=\"font-size: 10pt; ;\">?</span></em></span></li>\n\t\t\t\t<li>\n\t\t\t\t\tCours:</li>\n\t\t\t</ul>\n\t\t\t<p style=\"margin-left: 40px;\">\n\t\t\t\t<span style=\"color:#808080;\">II. Distances de freinage</span></p>\n\t\t\t<p style=\"margin-left: 80px;\">\n\t\t\t\t<span style=\"color:#808080;\">1. Vocabulaire</span></p>\n\t\t\t<p>\n\t\t\t\t&nbsp;</p>\n\t\t</div>\n\t</div>\n</div>\n<p>\n\t&nbsp;</p>\n";

        test1 = StringUtils.stripFontStyles(test1);
        log.info(test1);
        log.info(fontStriped1);
        //assertEquals(fontStriped1, test1);
        String test2 = "\n    •  fontName=\"doNotDelet\" Correction ex. 18\n    •  Ex. 24 p.201\n    •  Act.2 p.195: <style forecolor=\"#A9A9A9\"> Pourquoi l&apos;eau acquiert-elle de la vitesse lors de sa chute ?</style>\n    •  Cours:\n<style fontName=\"verdana\" forecolor=\"#A9A9A9\">II. Énergies mécaniques</style>\n<style fontName=\"verdana\" forecolor=\"#A9A9A9\">1. Vocabulaire</style> / <style backcolor=\"#DDA0DD\">2.3</style>\n \n\n    •  Correction DSM1\n\n<style backcolor=\"#DDA0DD\">Compétences traitées:</style> 2.3\n\n";

        String fontStriped2 = "\n    •  fontName=\"doNotDelet\" Correction ex. 18\n    •  Ex. 24 p.201\n    •  Act.2 p.195: <style forecolor=\"#A9A9A9\"> Pourquoi l&apos;eau acquiert-elle de la vitesse lors de sa chute ?</style>\n    •  Cours:\n<style  forecolor=\"#A9A9A9\">II. Énergies mécaniques</style>\n<style  forecolor=\"#A9A9A9\">1. Vocabulaire</style> / <style backcolor=\"#DDA0DD\">2.3</style>\n \n\n    •  Correction DSM1\n\n<style backcolor=\"#DDA0DD\">Compétences traitées:</style> 2.3\n\n";

        test2 = StringUtils.stripFontStyles(test2);
        log.info(test2);
        log.info(fontStriped2);
        
        assertEquals(fontStriped2, test2);
        
        String test3 = "<ul>\n\t<li>\n\t\tCorrection ex. 18</li>\n\t<li>\n\t\tEx. 24 p.201</li>\n\t<li>\n\t\tAct.2 p.195:&nbsp;<span style=\"color:#a9a9a9;\"><em> Pourquoi l&#39;eau acquiert-elle de la vitesse lors de sa chute ?</em></span></li>\n\t<li>\n\t\tCours:</li>\n</ul>\n<p style=\"margin-left: 40px;\">\n\t<span style=\"font-family:verdana,geneva,sans-serif;\"><span style=\"color: rgb(169, 169, 169);\">II. &Eacute;nergies m&eacute;caniques</span></span></p>\n<p style=\"margin-left: 80px;\">\n\t<span style=\"font-family:verdana,geneva,sans-serif;\"><span style=\"color: rgb(169, 169, 169);\">1. Vocabulaire</span></span> / <span style=\"background-color:#dda0dd;\">2.3</span></p>\n<p style=\"margin-left: 40px;\">\n\t&nbsp;</p>\n<ul>\n\t<li>\n\t\tCorrection DSM1</li>\n</ul>\n<p style=\"text-align: right;\">\n\t<br />\n\t<span style=\"background-color:#dda0dd;\">Comp&eacute;tences trait&eacute;es:</span> 2.3</p>\n";
        
        String fontStriped3 = "<ul>\n\t<li>\n\t\tCorrection ex. 18</li>\n\t<li>\n\t\tEx. 24 p.201</li>\n\t<li>\n\t\tAct.2 p.195:&nbsp;<span style=\"color:#a9a9a9;\"><em> Pourquoi l&#39;eau acquiert-elle de la vitesse lors de sa chute ?</em></span></li>\n\t<li>\n\t\tCours:</li>\n</ul>\n<p style=\"margin-left: 40px;\">\n\t<span style=\"font-family:verdana,geneva,sans-serif;\"><span style=\"color: rgb(169, 169, 169);\">II. &Eacute;nergies m&eacute;caniques</span></span></p>\n<p style=\"margin-left: 80px;\">\n\t<span style=\"font-family:verdana,geneva,sans-serif;\"><span style=\"color: rgb(169, 169, 169);\">1. Vocabulaire</span></span> / <span style=\"background-color:#dda0dd;\">2.3</span></p>\n<p style=\"margin-left: 40px;\">\n\t&nbsp;</p>\n<ul>\n\t<li>\n\t\tCorrection DSM1</li>\n</ul>\n<p style=\"text-align: right;\">\n\t<br />\n\t<span style=\"background-color:#dda0dd;\">Comp&eacute;tences trait&eacute;es:</span> 2.3</p>\n";
        
        
    }

    
    /**
     * 
     */
    @Test
    public void testConcat() {
        assertEquals("", StringUtils.concat("", ""));
        assertEquals("", StringUtils.concat("", null));
        assertEquals("", StringUtils.concat(null, ""));
        assertEquals("", StringUtils.concat(null, null));
        assertEquals("Helloworld", StringUtils.concat("Hello", "world"));
    }

    /**
     * 
     */
    public void testSplit() {
        final String regex = "[ ]*[,;][ ]*";
        final List<String> l = new ArrayList<String>();
        l.add("didi");
        l.add("dudu");
        l.add("dada");
        final List<String> l2 = StringUtils.split("didi, dudu  , dada", regex);
        assertNotNull(l2);
        assertEquals(l.size(), l2.size());
        assertEquals(l.get(0), l2.get(0));
        assertEquals(l.get(1), l2.get(1));
        assertEquals(l.get(1), l2.get(1));

        final List<String> l3 = StringUtils.split("ddd", regex);
        assertEquals(l3.size(), 1);
        assertEquals(l3.get(0), "ddd");
    }

    /**
     * 
     */
    public void testSplitLimit() {
        final String regex = ":";
        final List<String> l = new ArrayList<String>();
        l.add("didi");
        l.add("dudu:dada");
        final List<String> l2 = StringUtils.split("didi:dudu:dada", regex, 2);
        assertNotNull(l2);
        assertEquals(l.size(), l2.size());
        assertEquals(l.get(0), l2.get(0));
        assertEquals(l.get(1), l2.get(1));

        final List<String> l3 = StringUtils.split("ddd", regex, 1);
        assertEquals(l3.size(), 1);
        assertEquals(l3.get(0), "ddd");

        final String regex2 = "\\D+";
        final List<String> l4 = StringUtils.split("ddd01", regex2, 2);
        assertEquals(2, l4.size());
        assertEquals("", l4.get(0));
        assertEquals("01", l4.get(1));
    }

    /**
     * 
     */
    public void testEncodeDecode() {
        final String str = "Hello si@d! é^à$";
        final String charset = "UTF-8";
        assertEquals(str, StringUtils.decode(StringUtils.encode(str, charset), charset));
    }

    /**
     * 
     */
    public void testEncodeCharsetInconnu() {
        try {
            StringUtils.encode("hello", "abc");
            fail("CrlrRuntimeException attendue");
        } catch (final CrlrRuntimeException e) {
            log.debug("Exception capturée", e);
        }
    }

    /**
     * 
     */
    public void testEncodeCharsetNull() {
        assertNotNull(StringUtils.encode("hello", null));
    }

    /**
     * 
     */
    public void testDecodeCharsetInconnu() {
        try {
            StringUtils.decode(new byte[] { 65 }, "abc");
            fail("CrlrRuntimeException attendue");
        } catch (final CrlrRuntimeException e) {
            log.debug("Exception capturée", e);
        }
    }

    /**
     * 
     */
    public void testDecodeCharsetNull() {
        assertNotNull(StringUtils.decode(new byte[] { 65 }, null));
    }

    /**
     * 
     */
    public void testEncodeNull() {
        final byte[] data = StringUtils.encode(null, "UTF-8");
        assertNotNull(data);
        assertEquals(0, data.length);
    }

    /**
     * 
     */
    public void testEncodeEmpty() {
        final byte[] data = StringUtils.encode("", "UTF-8");
        assertNotNull(data);
        assertEquals(0, data.length);
    }

    /**
     * 
     */
    public void testDecodeNull() {
        assertNull(StringUtils.decode(null, "UTF-8"));
    }

    /**
     * 
     */
    public void testDecodeEmpty() {
        assertEquals("",
                     StringUtils.decode(new byte[] {  }, "UTF-8"));
    }

    /**
     * 
     */
    public void testValueOf() {
        assertNull(StringUtils.valueOf(null));
        assertNotNull(StringUtils.valueOf("null"));
        assertEquals("hello", StringUtils.valueOf("hello"));
    }

    /**
     * 
     */
    public void testContains() {
        assertTrue(StringUtils.contains("banane, pomme; orange", "[ ]*[,;][ ]*", "pomme"));
    }

 

    /**
     * 
     */
    public void testEndWith() {
        assertTrue(StringUtils.endWith("COCOX", "X"));
    }

    /**
     * 
     */
    public void testStripSpaces() {
        assertEquals(null, StringUtils.stripSpaces(null));
        assertEquals("", StringUtils.stripSpaces(""));
        assertEquals("Hello", StringUtils.stripSpaces("Hello"));
        assertEquals("Hello", StringUtils.stripSpaces(" Hello "));
        assertEquals("Helloworld", StringUtils.stripSpaces("Hello world"));
        assertEquals("Helloworld", StringUtils.stripSpaces("    Hello \t world   \t  "));
    }

    /**
     * 
     */
    public void testToLong() {
        final String[] values = { "1", "-1", "0" };
        final long[] longValues = { 1L, -1L, 0L };
        final long[] testLongValues = StringUtils.toLong(values);
        assertEquals(longValues.length, testLongValues.length);
        for (int i = 0; i < longValues.length; ++i) {
            assertEquals(longValues[i], testLongValues[i]);
        }

        assertTrue(StringUtils.toLong(null).length == 0);
    }

    /**
     * 
     */
    public void testToInt() {
        final String[] values = { "1", "-1", "0" };
        final int[] intValues = { 1, -1, 0 };
        final int[] testIntValues = StringUtils.toInt(values);
        assertEquals(intValues.length, testIntValues.length);
        for (int i = 0; i < intValues.length; ++i) {
            assertEquals(intValues[i], testIntValues[i]);
        }

        assertTrue(StringUtils.toInt(null).length == 0);
    }

    /**
     * 
     */
    public void testJoin() {
        final List<String> liste = Arrays.asList(new String[] { "AB", "CD", "EFGHI" });
        final String total = StringUtils.join(liste, ",");
        assertNotNull(total);
        assertEquals("AB,CD,EFGHI", total);
    }

    /**
     * 
     */
    public void testCompare() {
        assertTrue(StringUtils.compare("", null, false) == 0);
    }

    /**
     * 
     */
    public void testSansAccent() {
        String result = StringUtils.sansAccent(null);
        assertEquals(null, result);

        result = StringUtils.sansAccent("");
        assertEquals("", result);

        String chaine = "éèæàùúöÖÏïëËêôîûüÜäÄâÿýòóõöñ";
        String chaineResultat = "eeaeauuoOIieEeoiuuUaAayyoooon";
        result = StringUtils.sansAccent(chaine);
        assertEquals(chaineResultat, result);

        chaine = "éèçàîêùàÏ";
        chaineResultat = "eecaieuaI";
        result = StringUtils.sansAccent(chaine);
        assertEquals(chaineResultat, result);

        chaine = "Le java orienté objet à une mv";
        chaineResultat = "Le java oriente objet a une mv";
        result = StringUtils.sansAccent(chaine);
        assertEquals(chaineResultat, result);
    }

    /**
     * 
     */
    public void testSansAccentMajuscule() {
        String result = StringUtils.sansAccentEnMajuscule(null);
        assertEquals(null, result);

        result = StringUtils.sansAccentEnMajuscule("");
        assertEquals("", result);

        String chaine = "éèàù/*\\- java";
        String chaineResultat = "EEAU/*\\- JAVA";
        result = StringUtils.sansAccentEnMajuscule(chaine);
        assertEquals(chaineResultat, result);

        chaine = "éèçàîêùàÏïô";
        chaineResultat = "EECAIEUAIIO";
        result = StringUtils.sansAccentEnMajuscule(chaine);
        assertEquals(chaineResultat, result);
    }

   

    /**
     * 
     */
    public void testSubString() {
        assertEquals(StringUtils.subString("blatliplu", 3, 1), "t");
        assertEquals(StringUtils.subString("blatliplu", 3, 2), "tl");
        assertEquals(StringUtils.subString("blatliplu", 3, 10), "tliplu");
        assertEquals(StringUtils.subString("blatliplu", -1, 1), "b");
        assertEquals(StringUtils.subString("blatliplu", -1, 10), "blatliplu");
        assertEquals(StringUtils.subString("blatliplu", 0, 0), "b");
        assertEquals(StringUtils.subString("blatliplu", 8, -2), "u");
        assertEquals(StringUtils.subString("blatliplu", 8, 3), "u");
        assertEquals(StringUtils.subString("", 20, 20), "");
        assertEquals(StringUtils.subString(" ", 20, 20), "");
    }

    /**
     * 
     */
    public void testConcatArrays() {
        final String[] s1 = new String[] { "aaa", "bbb", };
        final String[] s2 = new String[] { "ccc", "ddd", };

        final String[] s1s2 = new String[] { "aaa", "bbb", "ccc", "ddd", };
        final String[] s2s1 = new String[] { "ccc", "ddd", "aaa", "bbb", };
        final String[] s1s1 = new String[] { "aaa", "bbb", "aaa", "bbb", };
        final String[] nullnull = new String[] {  };

        final String[] s1s2Array = StringUtils.concatArrays(s1, s2);
        final String[] s2s1Array = StringUtils.concatArrays(s2, s1);
        final String[] s1s1Array = StringUtils.concatArrays(s1, s1);
        final String[] nullnullArray = StringUtils.concatArrays(null, null);

        assertTrue(testArrays(s1s2, s1s2Array));
        assertTrue(testArrays(s2s1, s2s1Array));
        assertTrue(testArrays(s1s1, s1s1Array));
        assertTrue(testArrays(nullnull, nullnullArray));
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param s1 DOCUMENTATION INCOMPLETE!
     * @param s2 DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    private boolean testArrays(String[] s1, String[] s2) {
        if ((s1 == null) && (s2 == null)) {
            return true;
        }
        if ((s1 == null) || (s2 == null)) {
            return false;
        }
        if (s1.length != s2.length) {
            return false;
        }
        for (int i = 0; i < s1.length; i++) {
            final String ss1 = s1[i];
            final String ss2 = s2[i];
            if (!org.apache.commons.lang.StringUtils.equals(ss1, ss2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     */
    public void testFormatageSeulementPremiereLettreMajuscule() {
        assertEquals("Toto", StringUtils.formatageSeulementPremiereLettreMajuscule("toto"));
        assertEquals("Toto", StringUtils.formatageSeulementPremiereLettreMajuscule("toTO"));
        assertEquals("T", StringUtils.formatageSeulementPremiereLettreMajuscule("t"));
    }

    /**
     * 
     */
    public void testFormatagePremiereLettreMajuscule() {
        assertEquals("Toto", StringUtils.formatagePremiereLettreMajuscule("toto"));
        assertEquals("ToTO", StringUtils.formatagePremiereLettreMajuscule("toTO"));
        assertEquals("T", StringUtils.formatagePremiereLettreMajuscule("t"));
    }

    /**
     * 
     */
    public void testMapAccents() {
        assertEquals("è", StringUtils.MAP_ACCENTS_REPORT.get("e_grave"));
    }
}
