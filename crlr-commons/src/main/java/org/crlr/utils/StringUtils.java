/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: StringUtils.java,v 1.6 2010/06/01 13:45:24 ent_breyton Exp $
 */

package org.crlr.utils;

import java.awt.Color;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.util.JEditorPaneHtmlMarkupProcessor;

import org.apache.commons.lang.StringEscapeUtils;
import org.crlr.exception.base.CrlrRuntimeException;

/**
 * Méthodes utilitaires pour {@link String}.
 *
 * @author breytond
 * @author burnichonj
 * @author reted
 * @author parachy
 * @author romana
 */
public final class StringUtils {
    /** Constante de chaîne vide. */
    public static final String EMPTY_STRING = "";

    /** Index du 1er caractère accentué. * */
    private static final int MIN = 192;

    /** Index du dernier caractère accentué. * */
    private static final int MAX = 255;

    /** Liste de correspondance entre accent / sans accent. * */
    private static final ArrayList<String> LIST_CORRESPONDANCE = initCorrespondance();
    
    /** Map des correspondances d'accents pour les éditions PDF. */
    public static final Map<String, String> MAP_ACCENTS_REPORT = initMapAccentsReport();
    
    /**
     * The Constructor.
     */
    private StringUtils() {
    }


    /**
     * Supprime les caractéres d'espacement (espace, tabulation, etc...) en
     * début et en fin de chaîne. Si le résultat est une chaîne vide, ou si la
     * chaîne en entrée vaut <code>null</code>, la valeur <code>null</code>
     * est renvoyée.
     * 
     * @param str the str
     * 
     * @return the string
     */
    @Deprecated
    private static String trimToNull(String str) {
        return org.apache.commons.lang.StringUtils.trimToNull(str);
    }

    /**
     * Teste si une chaîne est <code>null</code>, vide ou ne contient que des
     * espaces.
     * 
     * @param str the str
     * 
     * @return true, if is empty
     */
    @Deprecated
    private static boolean isEmpty(String str) {
        return org.apache.commons.lang.StringUtils.isEmpty(str);
    }

    /**
     * Supprime les caractères d'espacement (espace, tabulation, etc...) en
     * début et en fin de chaîne. Si le résultat est une chaîne vide, ou si la
     * chaîne en entrée vaut <code>null</code>, une chaîne vide est renvoyée.
     * 
     * @param str the str
     * 
     * @return the string
     */
    private static String trimToBlank(String str) {
        final String newStr = trimToNull(str);
        return (newStr != null) ? newStr : EMPTY_STRING;
    }

    /**
     * Concatène deux chaînes. Une chaîne <code>null</code> est remplacée par
     * une chaîne vide.
     * 
     * @param a the a
     * @param b the b
     * 
     * @return the string
     */
    public static String concat(String a, String b) {
        return trimToBlank(a) + trimToBlank(b);
    }
    
    /**
     * Concatène deux tableaux. Deux tableaux <code>null</code> sont remplacées par une tableau vide.
     * 
     * @param s1 the s1
     * @param s2 the s2
     * 
     * @return the string[]
     */
    public static String[] concatArrays(String[] s1, String[] s2) {
        String[] s = new String[0];
        if(s1 == null && s2 == null) {
            return s;
        }
        final int taille1 = (s1 == null) ? 0 : s1.length;
        final int taille2 = (s2 == null) ? 0 : s2.length;
        final int taille = taille1 + taille2;
        s = new String[taille];
        if(s1 != null) {
            for (int i = 0; i < s1.length; i++) {
                s[i] = s1[i];
            }
        }
        if(s2 != null) {
            for (int i = 0; i < s2.length; i++) {
                s[taille1 + i] = s2[i];
            }
        }
        return s;
    }

    /**
     * Découpe une chaîne de caractères suivant un délimiteur.
     * 
     * @param regexDelim the regex delim
     * @param str the str
     * 
     * @return the list< string>
     */
    public static List<String> split(String str, String regexDelim) {
        return Arrays.asList(org.apache.commons.lang.StringUtils.split(str, regexDelim));
    }
    
    /**
     * Découpe une chaîne de caractères suivant un délimiteur, avec une utilisation du délimiteur limitée à "limitUseRegex" fois.
     * 
     * @param limitUseRegex limit use regex
     * @param regexDelim the regex delim
     * @param str the str
     * 
     * @return the list< string>
     */
    public static List<String> split(String str, String regexDelim, int limitUseRegex) {
        return Arrays.asList(str.split(regexDelim, limitUseRegex));
    }
    
    /**
     * Retourne la concaténation des chaines d'une liste en utilisant le
     * délimiteur indiqué. Exemple : join(["A", "B", "CD"], "/") --> "A/B/CD"
     * 
     * @param liste la liste des chaines à concaténer.
     * @param delim le délimiteur à insérer entre chaque valeur.
     * 
     * @return la chaine contenant les valeurs concaténées.
     */
    public static String join(List<String> liste, String delim) {
        boolean mettreSeparateur = false;
        final StringBuilder builder = new StringBuilder();
        if (liste != null) {
            for (String val : liste) {
                if (mettreSeparateur) {
                    builder.append(delim);
                }
                builder.append(val);
                mettreSeparateur = true;
            }
        }
        return builder.toString();
    }

    /**
     * Recherche si une chaîne de mots séparés par un délimiteur contient un
     * élément donné. le délimiteur est donné à l'aide d'une expression
     * réguliére. Par exemple :
     * <code>contains("banane, pomme; orange","[ ]*[,;][ ]*","pomme") = true</code>
     * 
     * @param searchString the search string
     * @param regexDelim the regex delim
     * @param str the str
     * 
     * @return true, if contains
     */
    public static boolean contains(String str, String regexDelim, String searchString) {
        final List<String> tokens = split(str, regexDelim);
        if ((tokens != null) && (tokens.size() > 0)) {
            return tokens.contains(searchString);
        }
        return false;
    }

    /**
     * Encode une chaîne de caractères dans un tableau d'octets en suivant
     * l'encodage spécifié. Si aucun encodage n'est spécifié (<code>null</code>),
     * l'encodage par défaut de la plateforme est utilisé.
     * 
     * @param charset nom de l'encodage à utiliser
     * @param str chaîne à encoder
     * 
     * @return the byte[]
     */
    public static byte[] encode(String str, String charset) {
        if (charset == null) {
            return str.getBytes();
        }
        try {
            return (str != null) ? str.getBytes(charset) : new byte[0];
        } catch (UnsupportedEncodingException e) {
            throw new CrlrRuntimeException("Encodage non supporté : {0}", charset);
        }
    }

    /**
     * Décode une chaîne de caractères à partir d'un tableau d'octets en suivant
     * l'encodage spécifié. Si aucun encodage n'est spécifié (<code>null</code>),
     * l'encodage par défaut de la plateforme est utilisé.
     * 
     * @param data tableau d'octets
     * @param charset nom de l'encodage à utiliser
     * 
     * @return the string
     */
    public static String decode(byte[] data, String charset) {
        if (charset == null) {
            return new String(data);
        }
        try {
            return (data != null) ? new String(data, charset) : null;
        } catch (UnsupportedEncodingException e) {
            throw new CrlrRuntimeException("Encodage non supporté : {0}", charset);
        }
    }

    /**
     * Retourne la valeur de la méthode <code>toString()</code> d'un objet. Si
     * l'objet vaut <code>null</code>, la valeur <code>null</code> est
     * renvoyée.
     * 
     * @param obj the obj
     * 
     * @return the string
     */
    public static String valueOf(Object obj) {
        return (obj != null) ? obj.toString() : null;
    }

    /**
     * Suppression de tous les espaces dans la chaine de caractères fournie.
     * 
     * @param chaine the chaine
     * 
     * @return the string
     */
    public static String stripSpaces(String chaine) {
        return (chaine != null) ? chaine.replaceAll("\\s", "") : null;
    }
    
    /**
     * Suppression de tous les espaces superflus dans la chaîne de caractères fournie, représentant une phrase.
     * 
     * @param chaine la chaîne
     * 
     * @return une phrase où une chaîne vide(si la chqîne est vide ou nulle).
     */
    public static String stripSpacesSentence(String chaine) {
        return (chaine != null) ? trimToBlank(chaine.replaceAll("\\s+", " ")) : "";
    }

    /**
     * Transforme un tableau de chaînes de caractères en un tableau de
     * <code>long</code>. Si le paramètre <code>values</code> vaut
     * <code>null</code>, un tableau vide est renvoyé.
     * 
     * @param values the values
     * 
     * @return the long[]
     */
    public static long[] toLong(String[] values) {
        if (values == null) {
            return new long[0];
        }
        final int len = values.length;
        final long[] longValues = new long[len];
        for (int i = 0; i < len; ++i) {
            longValues[i] = Long.parseLong(values[i]);
        }
        return longValues;
    }

    /**
     * Transforme un tableau de chaînes de caractères en un tableau de
     * <code>int</code>. Si le paramètre <code>values</code> vaut
     * <code>null</code>, un tableau vide est renvoyé.
     * 
     * @param values the values
     * 
     * @return the int[]
     */
    public static int[] toInt(String[] values) {
        if (values == null) {
            return new int[0];
        }
        final int len = values.length;
        final int[] intValues = new int[len];
        for (int i = 0; i < len; ++i) {
            intValues[i] = Integer.parseInt(values[i]);
        }
        return intValues;
    }

    /**
     * Retourne la comparaison entre 2 Strings. Les Strings vides et Null
     * peuvent être mises en début ou en fin de liste.
     * 
     * @param valeur1 Première valeur à comparer.
     * @param valeur2 Seconde valeur à comparer.
     * @param nullEtVideEnPremier Faut-il mettre les null et vide en premier ?
     * 
     * @return infèrieur, égal ou supérieur à 0 selon la règle des méthodes
     * standard "compareTo".
     */
    public static int compare(String valeur1, String valeur2, boolean nullEtVideEnPremier) {
        return ObjectUtils.compare(StringUtils.trimToNull(valeur1),
                                   StringUtils.trimToNull(valeur2), nullEtVideEnPremier);
    }

    /**
     * Génère n espaces HTML non "breakable".
     * 
     * @param nb the nb
     * 
     * @return the string
     */
    public static String nbsp(Integer nb) {
        Integer nbr = nb;
        if (nbr == null) {
            nbr = 1;
        }
        String retour = "";
        for (int i = 1; i <= nbr.intValue(); i++) {
            retour += "&#160;";
        }
        return retour;
    }

    /**
     * Formatage d'un numéro sur n caractères,
     * les premiers contenant des zéros.
     * 
     * @param nbCars le nombre de caractères 0.
     * @param numero le chiffre
     * 
     * @return le numéro formaté.
     */
    public static String formatNumber(int nbCars, int numero) {
        final String numStr = Integer.valueOf(numero).toString();
        final int numSize = numStr.length();
        if (numSize >= nbCars) {
            return numStr.substring(0, nbCars);
        }
        String newStr = "";
        for (int i = 1; i <= (nbCars - numSize); i++) {
            newStr += "0";
        }
        newStr += numStr;
        return newStr;
    }

    /**
     * Initialisation de la liste de correspondance entre les caractères accentués
     * et leur homologues non accentués.
     * 
     * @return liste de correspondances
     */
    private static ArrayList<String> initCorrespondance() {
        final ArrayList<String> result = new ArrayList<String>();
        String car = null;

        car = "A";
        result.add(car); /* '\u00C0'   À   alt-0192  */
        result.add(car); /* '\u00C1'   Á   alt-0193  */
        result.add(car); /* '\u00C2'   Â   alt-0194  */
        result.add(car); /* '\u00C3'   Ã   alt-0195  */
        result.add(car); /* '\u00C4'   Ä   alt-0196  */
        result.add(car); /* '\u00C5'   Å   alt-0197  */
        car = "AE";
        result.add(car); /* '\u00C6'   Æ   alt-0198  */
        car = "C";
        result.add(car); /* '\u00C7'   Ç   alt-0199  */
        car = "E";
        result.add(car); /* '\u00C8'   È   alt-0200  */
        result.add(car); /* '\u00C9'   É   alt-0201  */
        result.add(car); /* '\u00CA'   Ê   alt-0202  */
        result.add(car); /* '\u00CB'   Ë   alt-0203  */
        car = "I";
        result.add(car); /* '\u00CC'   Ì   alt-0204  */
        result.add(car); /* '\u00CD'   Í   alt-0205  */
        result.add(car); /* '\u00CE'   Î   alt-0206  */
        result.add(car); /* '\u00CF'   Ï   alt-0207  */
        car = "D";
        result.add(car); /* '\u00D0'   Ð   alt-0208  */
        car = "N";
        result.add(car); /* '\u00D1'   Ñ   alt-0209  */
        car = "O";
        result.add(car); /* '\u00D2'   Ò   alt-0210  */
        result.add(car); /* '\u00D3'   Ó   alt-0211  */
        result.add(car); /* '\u00D4'   Ô   alt-0212  */
        result.add(car); /* '\u00D5'   Õ   alt-0213  */
        result.add(car); /* '\u00D6'   Ö   alt-0214  */
        car = "*";
        result.add(car); /* '\u00D7'   ×   alt-0215  */
        car = "0";
        result.add(car); /* '\u00D8'   Ø   alt-0216  */
        car = "U";
        result.add(car); /* '\u00D9'   Ù   alt-0217  */
        result.add(car); /* '\u00DA'   Ú   alt-0218  */
        result.add(car); /* '\u00DB'   Û   alt-0219  */
        result.add(car); /* '\u00DC'   Ü   alt-0220  */
        car = "Y";
        result.add(car); /* '\u00DD'   Ý   alt-0221  */
        car = "Þ";
        result.add(car); /* '\u00DE'   Þ   alt-0222  */
        car = "B";
        result.add(car); /* '\u00DF'   ß   alt-0223  */
        car = "a";
        result.add(car); /* '\u00E0'   à   alt-0224  */
        result.add(car); /* '\u00E1'   á   alt-0225  */
        result.add(car); /* '\u00E2'   â   alt-0226  */
        result.add(car); /* '\u00E3'   ã   alt-0227  */
        result.add(car); /* '\u00E4'   ä   alt-0228  */
        result.add(car); /* '\u00E5'   å   alt-0229  */
        car = "ae";
        result.add(car); /* '\u00E6'   æ   alt-0230  */
        car = "c";
        result.add(car); /* '\u00E7'   ç   alt-0231  */
        car = "e";
        result.add(car); /* '\u00E8'   è   alt-0232  */
        result.add(car); /* '\u00E9'   é   alt-0233  */
        result.add(car); /* '\u00EA'   ê   alt-0234  */
        result.add(car); /* '\u00EB'   ë   alt-0235  */
        car = "i";
        result.add(car); /* '\u00EC'   ì   alt-0236  */
        result.add(car); /* '\u00ED'   í   alt-0237  */
        result.add(car); /* '\u00EE'   î   alt-0238  */
        result.add(car); /* '\u00EF'   ï   alt-0239  */
        car = "d";
        result.add(car); /* '\u00F0'   ð   alt-0240  */
        car = "n";
        result.add(car); /* '\u00F1'   ñ   alt-0241  */
        car = "o";
        result.add(car); /* '\u00F2'   ò   alt-0242  */
        result.add(car); /* '\u00F3'   ó   alt-0243  */
        result.add(car); /* '\u00F4'   ô   alt-0244  */
        result.add(car); /* '\u00F5'   õ   alt-0245  */
        result.add(car); /* '\u00F6'   ö   alt-0246  */
        car = "/";
        result.add(car); /* '\u00F7'   ÷   alt-0247  */
        car = "0";
        result.add(car); /* '\u00F8'   ø   alt-0248  */
        car = "u";
        result.add(car); /* '\u00F9'   ù   alt-0249  */
        result.add(car); /* '\u00FA'   ú   alt-0250  */
        result.add(car); /* '\u00FB'   û   alt-0251  */
        result.add(car); /* '\u00FC'   ü   alt-0252  */
        car = "y";
        result.add(car); /* '\u00FD'   ý   alt-0253  */
        car = "þ";
        result.add(car); /* '\u00FE'   þ   alt-0254  */
        car = "y";
        result.add(car); /* '\u00FF'   ÿ   alt-0255  */
        result.add(car); /* '\u00FF'       alt-0255  */

        return result;
    }
    
   

    
    
    /** 
     * Coupe une chaine contenant des caractères html accentués ("&eacute;").  
     * Si la chaine est coupée au milieu d'un caractère spécial html, 
     * la valeur retrounée est tronquée à la fin du code html de ce caractère pour 
     * que la resitution dans la page qui affiche la valeur sont complète.
     * @param buffer : chaine pouvant contenir des caractères accentués au format html.
     * @param size : position souhaité pour tronquer la chaine.
     * @return Retour la chaine tronquée sans coupure au milieu d'un code de caractère accentué html.  
     */
    public static String truncateHTMLString(final String buffer, final Integer size) {
        
        final String s = StringEscapeUtils.unescapeHtml(buffer);
        return org.apache.commons.lang.StringUtils.abbreviate(s, size);
        
    }
        
    /**
     * Creation de Map des correspondances d'accents pour les éditions PDF.
     * @return Map des correspondances d'accents pour les éditions PDF.
     */
    private static Map<String, String> initMapAccentsReport() {
        final Map<String, String> mapRetour = new HashMap<String, String>();
        mapRetour.put("a_grave", "à");
        mapRetour.put("a_circonflexe", "â");
        mapRetour.put("ae", "æ");
        mapRetour.put("c_cedille", "ç");
        mapRetour.put("e_grave", "è");
        mapRetour.put("e_aigu", "é");
        mapRetour.put("e_circonflexe", "ê");
        mapRetour.put("e_trema", "ë");
        mapRetour.put("i_circonflexe", "î");
        mapRetour.put("i_trema", "ï");
        mapRetour.put("o_circonflexe", "ô");
        mapRetour.put("u_grave", "ù");
        mapRetour.put("u_circonflexe", "û");
        
        return mapRetour;
    }

    /**
     * Transforme une chaine pouvant contenir des accents dans une version sans accent.
     * 
     * @param chaine Chaine à convertir sans accent.
     * 
     * @return Chaine en majuscule dont les accents ont été supprimé.
     */
    public static String sansAccent(final String chaine) {
        if (StringUtils.isEmpty(chaine)) {
            return chaine;
        }
        return effectueSansAccent(chaine);
    }

    /**
     * Transforme une chaine pouvant contenir des accents dans une version sans accent en majuscule.
     * 
     * @param chaine Chaine a convertir sans accent.
     * 
     * @return Chaine dont les accents ont été supprimé.
     */
    public static String sansAccentEnMajuscule(final String chaine) {
        if (StringUtils.isEmpty(chaine)) {
            return chaine;
        }
        return effectueSansAccent(chaine).toUpperCase();
    }
    
    /**
     * Effectue le traitement de remplacement des caractères accentués en caractères non accentués.
     * 
     * @param chaine Chaine à convertir sans accent.
     * 
     * @return Chaine dont les accents ont été supprimé.
     */
    private static String effectueSansAccent(final String chaine) {
        final StringBuilder result = new StringBuilder();
        for (int i=0; i<chaine.length(); i++) {
            final char carVal = chaine.charAt(i);
            ajouteSansAccent(result, carVal);
        }
        return result.toString();
    }

    /**
     * Ajoute le caractère sans l'accent dans le builder.
     * @param builder builder
     * @param c caractere
     */
    private static void ajouteSansAccent(StringBuilder builder, char c) {
        if ((c >= MIN) && (c <= MAX)) {
            // ajout du caractère sans accent.
            final String newVal = LIST_CORRESPONDANCE.get(c - MIN);
            builder.append(newVal);
        } else {
            builder.append(c);
        }
    }
    
    /**
     * Indique si la chaine1 commence par la chaine2.
     * 
     * @param chaine1 Chaine à tester
     * @param chaine2 Chaine qui doit être contenu
     * 
     * @return Si la chaine1 commence par la chaine2
     * @deprecated
     */
    public static Boolean startWith(String chaine1, String chaine2) {
        return org.apache.commons.lang.StringUtils.startsWith(chaine1, chaine2);
    }
    
    /**
     * Indique si la chaine1 finit par la chaine 2.
     * 
     * @param chaine1 the chaine1
     * @param chaine2 the chaine2
     * 
     * @return the boolean
     */
    public static Boolean endWith(String chaine1, String chaine2) {
        return (chaine1 != null) ? chaine1.endsWith(chaine2) : false;
    }
   
    
    /**
     * Retourne Vrai si les 2 chaînes sont identiques ou tous les 2 nuls.
     * Les chaînes étant traitées avec trimToNull pour enlever les espaces inutiles.
     * 
     * @param o1 Première chaîne à comparer
     * @param o2 Seconde chaîne à comparer
     * 
     * @return Vrai si les 2 chaînes ajustées sont identiques ou tous les 2 nuls.
     * @deprecated
     */
    public static boolean equals(String o1, String o2) {
        return org.apache.commons.lang.StringUtils.equals(trimToNull(o1), trimToNull(o2));
    }
    
    /**
     * Retourne Vrai si les une des chaines est identique a o1.
     * Les chaînes étant traitées avec trimToNull pour enlever les espaces inutiles.
     * 
     * @param o1 Première chaîne à comparer
     * @param o2 les autres chaines à comparer
     * 
     * @return Vrai si une des chaines correspond.
     */
    public static boolean in(String o1, String... o2) {
        for (String o:o2) {
            if (ObjectUtils.equals(trimToNull(o1), trimToNull(o))) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Methode : isIn.
     * 
     * @param name : la chaine a trouver.
     * @param valeurs : la chaine où l'on cherche.
     * @param separateur : le séparateur.
     * 
     * @return : vrai ou faux
     */
    public static boolean isIn(final String name, String valeurs, String separateur){
        if(StringUtils.trimToNull(name)!= null && StringUtils.trimToNull(valeurs)!= null){
            final StringTokenizer st = new StringTokenizer(valeurs,separateur);
            while (st.hasMoreTokens()) {
                if(st.nextToken().equals(name)){
                    return true;
                }
            }
        }

        return false;
    }
    
    /**
     * Retourne la liste des String contenues dans la configuration.
     *
     * @param cle La clé de configuration
     * @param separateur Le séparateur
     *
     * @return la liste des String contenues dans la configuration
     */
    public static List<String> getListeString(String cle, String separateur) {
        Assert.isNotNull("cle", cle);
        Assert.isNotNull("separateur", separateur);
        final String valeur = cle;
        final List<String> listeString = StringUtils.split(valeur, separateur);
        if (listeString == null) {
            return Collections.emptyList();
        }
        return listeString;
    }
    
    /**
     * Truncate.
     * 
     * @param string the string
     * @param length the length
     * 
     * @return the string
     */
    public static String truncate(String string, Integer length) {
        return truncate(string, length, EMPTY_STRING);
    }    
    
    /**
     * insert un espace après n charateres.
     * 
     * @param string the string
     * @param length the length
     * 
     * @return the string
     */
    public static String insertSpace(String string, Integer length) {
        final String valeur = trimToBlank(string);
        final int index = valeur.length()>length?length:-1;
        return index!=-1?(valeur.substring(0, index)+" "+valeur.substring(index,valeur.length())):valeur;
    } 

    /**
     * Truncate.
     * 
     * @param string the string
     * @param length the length
     * @param truncCharacter the trunc character
     * 
     * @return the string
     */
    public static String truncate(String string, Integer length, String truncCharacter) {
        final String newString;
        if(StringUtils.isEmpty(string)) {
            newString = EMPTY_STRING;
        } else {
            if(length == null || length.intValue() == 0 || string.length() < length.intValue()) {
                newString = string;
            } else {
                newString = StringUtils.concat(string.substring(0, length.intValue()), truncCharacter);
            }
        }
        return newString;
    }     
    
    /**
     * Retourne une nouvelle chaîne tronquée à partir de l'indice "beginIndex" de la chaîne,
     * d'une longueur définie par "lenght".
     * Si l'indice est inférieure à 0, l'indice est remplacé par 0.
     * Si la longueur est inférieure à 1, la longueur est remplacée par 1.
     * Si l'indice est supérieur au dernier indice de la chaîne, l'indice est remplacé par indice.length().
     * Si la longueur est supérieure à la longueur entre l'indice et la longueur de la chaîne,
     * la longueur est remplacée par "longueur de la chaîne - indice).
     * 
     * @param string the string
     * @param beginIndex the begin index
     * @param length the length
     * 
     * @return the string
     */
    public static String subString(String string, Integer beginIndex, Integer length) {
        String newString = string;
        Integer newBeginIndex = beginIndex;
        Integer newLength = length;
        if(StringUtils.isEmpty(string)) {
            newString = EMPTY_STRING;
        } else {
            if(beginIndex == null || beginIndex.intValue() < 0) {
                newBeginIndex = 0;
            } else if(beginIndex.intValue() > string.length()) {
                newBeginIndex = string.length();
            }
            if(length == null || length.intValue() < 1) {
                newLength = 1;
            } else if((newBeginIndex.intValue() + length) > string.length()) {
                newLength = string.length() - newBeginIndex.intValue();
            }
            newString = newString.substring(newBeginIndex, newBeginIndex + newLength);
        }
        return newString;
    }
    
    /**
     * Retourne les length caracteres de string en partant de la droite.
     * Ex: rigth("christophe", 3) => "phe". 
     * Si length est plus grand que la laongueur de string, string est retourné.
     * Si string est null ou vide, la chaine "" est retournée.
     * Si length est <=0, la chaine "" est retournée. 
     * @param string la chaine a decouper
     * @param length le nombre de caractères a extraire
     * @return la sous-chaine de string.
     * @deprecated
     */
    public static String right(final String string, final Integer length) {
       return org.apache.commons.lang.StringUtils.right(string,  length);
    }
    
    /**
     * Retourne une nouvelle chaîne tronquée de l'index 0 à l'index d'indice "nombreCharMax".
     * complétée par la string "endString".
     * 
     * @param laChaine la chaine à découper
     * @param nombreCharMax le nombre max de caractères
     * @param endString le complément à ajouter
     * 
     * @return the string
     */
    public static String cutAndCompleteString(final String laChaine, final Integer nombreCharMax, final String endString){
    	
    	return truncate(laChaine, nombreCharMax, endString);
    	
        
    }
    
    /**
     * Retourne une nouvelle chaîne de "nombreCharMax" charactères 
     * complétée par la string "endString" en cas de troncature.
     * 
     * @param laChaine la chaine à découper
     * @param nombreCharMax le nombre max de caractères
     * @param endString le complément à ajouter
     * 
     * @return the string
     */
    public static String cutAndCompleteStringBis(final String laChaine, final Integer nombreCharMax, final String endString){
        String nouvelleChaine;
        
        if (isEmpty(laChaine)){
            nouvelleChaine = EMPTY_STRING;
        } else if (laChaine.length() <= nombreCharMax) {
            nouvelleChaine = laChaine;            
        } else {
            final String complement = StringUtils.isEmpty(endString) ? EMPTY_STRING : endString;
            nouvelleChaine = StringUtils.subString(laChaine, 0, nombreCharMax- endString.length()) + complement;
        }
        return nouvelleChaine;
    }
    
    
    /**
     * Vérifie si la chaîne de caractères fournie contient
     * uniquement des caractères alphanumériques non
     * accentués avec acceptation ou non des espaces.
     * 
     * @param valeur the valeur
     * @param accepterEspaces the accepter espaces
     * 
     * @return true, if checks if is alpha numeric non accentue
     */
    public static Boolean isAlphaNumericNonAccentue(String valeur, Boolean accepterEspaces){
        final String val = StringUtils.trimToNull(valeur);
        if(val==null){
            return true;
        } else {
            String matcher = "abcdefghijklmnopqrstuvwxyz0123456789";
            if(BooleanUtils.isTrue(accepterEspaces)){                
                matcher += ' ';
            }            
            matcher = "[" + matcher + "]*";
            return val.toLowerCase().matches(matcher);
        }
    }   
    
    /**
     * Retourne la valeur avec une majuscule au début.
     * @param valeur valeur à formater.
     * @return valeur formattée.
     */
    public static String formatagePremiereLettreMajuscule(String valeur) {
        if (StringUtils.isEmpty(valeur)) {
            return valeur;
        }
        if (valeur.length()>1) {
            return valeur.substring(0, 1).toUpperCase() + valeur.substring(1);
        } else { //if (valeur.length()>0) {
            return valeur.toUpperCase();
        }
    } 
    

    /**
     * Retourne la valeur avec une majuscule au début.
     * @param valeur valeur à formater.
     * @return valeur formattée.
     */
    public static String formatageSeulementPremiereLettreMajuscule(String valeur) {
        if (StringUtils.isEmpty(valeur)) {
            return valeur;
        }
        if (valeur.length()>1) {
            return valeur.substring(0, 1).toUpperCase() + valeur.substring(1).toLowerCase();
        } else { //if (valeur.length()>0) {
            return valeur.toUpperCase();
        }
    } 
    
    /**
     * Formattage d'une chaine (comme dans les logs).
     * @param msg msg
     * @param args args
     * @return chaine formatée.
     */
    public static String formatString(String msg, Object... args) {
        return MessageFormat.format(msg.replaceAll("'", "''"), args);
    }
    
    /**
     * Retourne une chaine en formattant les symboles de retour chariot (\n) par des vrais retour chariot.
     * @param valeur valeur
     * @return valeur avec retour chariot.
     */
    public static String formatRetourChariot(String valeur) {
        if (StringUtils.isEmpty(valeur)) {
            return valeur;
        }
        return valeur.replace("\\n", "\n");
    }
    
    /**
     * Remplace ces caractères spéciaux :
     * ’ en '
     * ‘ en '
     * « en "
     * » en ".
     * 
     * @param param the chaine
     * 
     * @return the string
     */
    public static String replaceSpecialsChars(String param) {
        String chaine = param;
        chaine = chaine.replaceAll("’", "'");
        chaine = chaine.replaceAll("‘", "'");
        chaine = chaine.replaceAll("«", "\"");
        chaine = chaine.replaceAll("»", "\"");
        return chaine;   
    }    
    
    /**
     * 
     * Methode : stringToMap.
     * @param valeurs .
     * @param separateur1 .
     * @param separateur2 .
     * @return :
     */
    public static Map<String, String> stringToMap(String valeurs, String separateur1, String separateur2) {
        final Map<String,String> mapFormation = new HashMap<String, String>();
        final List<String> listeFormation = StringUtils.split(valeurs,separateur1);
        for(String formation: listeFormation){
            final String[] tab = formation.split(separateur2);
            if(tab.length>1){
                mapFormation.put(tab[0],tab[1]);
            }
        }
        return mapFormation;
    }
    
    /**
     * Supprimpe l'ensemble des balises de la chaîne passée en paramétre.
     * @param valeur la chaîne.
     * @return la chaîne formatée. 
     */
    public static String removeBalise(final String valeur) {
       return trimToBlank(valeur).replaceAll("<[^>]*>", "");        
    }
    
    /**
     * Ajoute un br tout les nbCaractere.
     * @param valeur la valeur.
     * @param nbCaractere le nombre de caractère.
     * @return la chaîne.
     */
    public static String addRetourChariot(final String valeur, final Integer nbCaractere) {       
        if (!isEmpty(valeur) && valeur.length() > nbCaractere) {
           final StringBuilder retour = new StringBuilder();
          
           final char[] lettres = valeur.toCharArray();
           int cpt = 0;
           for (final char l : lettres) {
               if (cpt == 4 ) {
                   retour.append("\n");
                   cpt=0;
               }
               retour.append(l);
               cpt++;
           }
           
           return retour.toString();
        }
        
        return valeur;
    }
    
    /**
     * Retourne une couleur au format Web  : "#FF00DD" .
     * @param color la couleur 
     * @return la chaine représentant la couleur.
     */
    public static String colorToString(final Color color) {
        if (color == null) { return ""; } 
        final Integer r =  color.getRed();
        final Integer g =  color.getGreen();
        final Integer b =  color.getBlue();
        
        final String colorStr = String.format("#%02x%02x%02x", r,g,b);
        return colorStr;
    }
    
    /**
     * Génère la description abrégée visible dans le listing de devoir.
     * @param description String la description.
     * @return la description
     */
    public static String generateDescriptionSansBalise(final String description) {
        final String descriptionInfoBulle = StringEscapeUtils
            .unescapeHtml(StringUtils.truncate(StringUtils.removeBalise(description), 200, ".."));
        
        return descriptionInfoBulle;
    }

    /**
     * Génère la description abrégée visible dans le listing de devoir.
     * @param description la description.
     * @return une description 
     */
    public static String generateDescriptionSansBaliseAbrege(final String description) {
        final String descriptionFinale = 
            StringUtils.truncateHTMLString(StringUtils.removeBalise(description), 25);                            
        return descriptionFinale;
    }
    
    
    public static String stripFontStyles(String html) {
        if (html == null) {
            return null;
        }
        JEditorPaneHtmlMarkupProcessor jp = new JEditorPaneHtmlMarkupProcessor();
        
        //Normalizer le text
        html = jp.convert(html);

        //Match <style ... fontName="blah"  mais pas fontName="blah".  Il faut que ça soit dans une balise style
        html = html.replaceAll("(<style[^>]*)fontName\\s*=\\s*[\'\"][^\'\"]*[\'\"]", "$1");
        return html;
    }
}
