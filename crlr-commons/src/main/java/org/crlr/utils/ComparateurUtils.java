/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ComparateurUtils.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.utils;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ComparateurUtils, classe utilitaire permettant de générer des instances de
 * Comparator et d'effectuer automatiquement le tri d'une List sur un ensemble de champs
 * des objets à comparer.
 *
 * @author dethillotn
 */
public final class ComparateurUtils {
    /** The log. */
    private static final Log log = LogFactory.getLog(CollectionUtils.class);

/**
     * The Constructor.
     */
    private ComparateurUtils() {
    }

    /**
     * Tri une liste d'objets, suivant un champ de l'objet. Pour le champ, le tri
     * est appliqué suivant le paramètre passé.
     *
     * @param <T> Objet à trier
     * @param list la liste d'objets à trier
     * @param nomChamp le nom du champ sur lequel trié
     * @param triComparateur the tri comparateur
     * @param nullEnPremier Valeur par défaut à utiliser en cas de valeur null
     *
     * @return la list<T> triée
     */
    public static <T> List<T> sort(List<T> list, String nomChamp,
                                   TriComparateur triComparateur, boolean nullEnPremier) {
        Assert.isNotNull("nomChamp", nomChamp);
        Assert.isNotNull("triComparateur", triComparateur);

        final LinkedHashMap<String, TriComparateur> map =
            new LinkedHashMap<String, TriComparateur>();
        map.put(nomChamp, triComparateur);

        return sort(list, map, nullEnPremier);
    }

    /**
     * Tri une liste d'objets, suivant un champ de l'objet. Pour le champ, le tri
     * est appliqué par défaut à croissant.
     *
     * @param <T> Objet à trier
     * @param list la liste d'objets à trier
     * @param nomChamp le nom du champ sur lequel trié
     * @param triComparateur the tri comparateur
     *
     * @return la list<T> triée
     */
    public static <T> List<T> sort(List<T> list, String nomChamp,
                                   TriComparateur triComparateur) {
        Assert.isNotNull("nomChamp", nomChamp);
        Assert.isNotNull("triComparateur", triComparateur);

        return sort(list, nomChamp, triComparateur, false);
    }

    /**
     * Tri une liste d'objets, suivant une liste de champs de l'objet. Pour
     * chaque champ on peut définir l'ordre de tri à appliquer sur le champ, soit un tri
     * croissant ou decroissant.
     *
     * @param <T> Objet à trier
     * @param list la liste d'objets à trier
     * @param mapNomsChampEtTri map ( clé : nom du champ / value : tri )
     *
     * @return la list<T> triée
     */
    public static <T> List<T> sort(List<T> list,
                                   LinkedHashMap<String, TriComparateur> mapNomsChampEtTri) {
        return sort(list, mapNomsChampEtTri, false);
    }

    /**
     * Tri une liste d'objets, suivant une liste de champs de l'objet. Pour
     * chaque champ on peut définir l'ordre de tri à appliquer sur le champ, soit un tri
     * croissant ou decroissant.
     *
     * @param <T> Objet à trier
     * @param list la liste d'objets à trier
     * @param mapNomsChampEtTri map ( clé : nom du champ / value : tri )
     * @param nullEnPremier Valeur par défaut à utiliser en cas de valeur null
     *
     * @return la list<T> triée
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> sort(List<T> list,
                                   LinkedHashMap<String, TriComparateur> mapNomsChampEtTri,
                                   boolean nullEnPremier) {
        Assert.isNotNull("mapNomsChampEtTri", mapNomsChampEtTri);
        Assert.isFalse("mapNomsChampEtTri.isEmpty", mapNomsChampEtTri.isEmpty());

        if (org.apache.commons.collections.CollectionUtils.isEmpty(list)) {
            return new ArrayList<T>();
        }

        final Class<?> c = org.apache.commons.collections.CollectionUtils.get(list, 0).getClass();

        final List<T> listeRetour = new ArrayList<T>(list);

        Collections.sort(listeRetour,
                         getComparateur((Class<T>) c, mapNomsChampEtTri, nullEnPremier));

        return listeRetour;
    }

    /**
     * Tri une liste d'objets, suivant une liste de champs de l'objet. Pour
     * chaque champ, le tri est appliqué par défaut à croissant.
     *
     * @param <T> Objet à trier
     * @param list la liste d'objets à trier
     * @param listeNomsChamp la liste des noms champ de l'objet
     *
     * @return la list<T> triée
     */
    public static <T> List<T> sort(List<T> list, List<String> listeNomsChamp) {
        return sort(list, listeNomsChamp, false);
    }

    /**
     * Tri une liste d'objets, suivant une liste de champs de l'objet. Pour
     * chaque champ, le tri est appliqué par défaut à croissant.
     *
     * @param <T> Objet à trier
     * @param list la liste d'objets à trier
     * @param listeNomsChamp la liste des noms champ de l'objet
     * @param nullEnPremier Valeur par défaut à utiliser en cas de valeur null
     *
     * @return la list<T> triée
     */
    public static <T> List<T> sort(List<T> list, List<String> listeNomsChamp,
                                   boolean nullEnPremier) {
        Assert.isNotEmpty("listeNomsChamp", listeNomsChamp);

        final LinkedHashMap<String, TriComparateur> mapNomsChampEtTri =
            buildMapChampEtTri(listeNomsChamp);

        return sort(list, mapNomsChampEtTri, nullEnPremier);
    }

    /**
     * Tri une liste d'objets, suivant une liste de champs de l'objet. Pour
     * chaque champ, le tri est appliqué par défaut à croissant.
     *
     * @param <T> Objet à trier
     * @param list la liste d'objets à trier
     * @param nomsChamp la liste des noms des champs.
     * @return la list<T> triée
     */
    public static <T> List<T> sort(List<T> list, String... nomsChamp) {
        Assert.isNotNull("nomsChamp", nomsChamp);

        return sort(list, Arrays.asList(nomsChamp));
    }

    /**
     * Tri une liste d'objets, suivant une liste de champs de l'objet. Pour
     * chaque champ, le tri est appliqué par défaut à croissant.
     *
     * @param <T> Objet à trier
     * @param list la liste d'objets à trier
     * @param nullEnPremier Valeur par défaut à utiliser en cas de valeur null
     * @param nomsChamp la liste des noms des champs.
     * @return la list<T> triée
     */
    public static <T> List<T> sort(List<T> list, boolean nullEnPremier,
                                   String... nomsChamp) {
        Assert.isNotNull("nomsChamp", nomsChamp);

        return sort(list, Arrays.asList(nomsChamp), nullEnPremier);
    }

    /**
     * Tri une liste d'objets, suivant un champ de l'objet. Pour le champ, le tri
     * est appliqué par défaut à croissant.
     *
     * @param <T> Objet à trier
     * @param list la liste d'objets à trier
     * @param nomChamp le nom du champ sur lequel trié
     * @param nullEnPremier Valeur par défaut à utiliser en cas de valeur null
     *
     * @return la list<T> triée
     */
    public static <T> List<T> sort(List<T> list, String nomChamp, boolean nullEnPremier) {
        Assert.isNotNull("nomChamp", nomChamp);

        return sort(list, Collections.singletonList(nomChamp), nullEnPremier);
    }

    /**
     * Tri une liste d'objets, suivant un champ de l'objet. Pour le champ, le tri
     * est appliqué par défaut à croissant.
     *
     * @param <T> Objet à trier
     * @param list la liste d'objets à trier
     * @param nomChamp le nom du champ sur lequel trié
     *
     * @return la list<T> triée
     */
    public static <T> List<T> sort(List<T> list, String nomChamp) {
        return sort(list, nomChamp, false);
    }

    /**
     * Génère un comparateur pour une classe et une liste de champs.
     *
     * @param <T> Objet à comparer
     * @param <V> V
     * @param c la classe d'objet à comparer
     * @param mapNomsChampEtTri map ( clé : nom du champ / value : tri )
     *
     * @return the comparateur
     */
    public static <T, V extends Comparable<?>> Comparator<T> getComparateur(final Class<T> c,
                                                                            final LinkedHashMap<String, TriComparateur> mapNomsChampEtTri) {
        return getComparateur(c, mapNomsChampEtTri, false);
    }

    /**
     * Génère un comparateur pour une classe et une liste de champs.
     *
     * @param <T> Objet à comparer
     * @param <V> V
     * @param c la classe d'objet à comparer
     * @param mapNomsChampEtTri map ( clé : nom du champ / value : tri )
     * @param nullEnPremier Valeur par défaut à utiliser en cas de valeur null
     *
     * @return the comparateur
     */
    public static <T, V extends Comparable<?>> Comparator<T> getComparateur(final Class<T> c,
                                                                            final LinkedHashMap<String, TriComparateur> mapNomsChampEtTri,
                                                                            final boolean nullEnPremier) {
        Assert.isNotNull("mapNomsChampEtTri", mapNomsChampEtTri);
        Assert.isFalse("mapNomsChampEtTri.isEmpty", mapNomsChampEtTri.isEmpty());
        final Comparator<T> comparator = new Comparator<T>() {
                @SuppressWarnings("unchecked")
                public int compare(T o1, T o2) {
                    int retour = 0;
                    for (String nomChamp : mapNomsChampEtTri.keySet()) {
                        try {
                            final V object1 = (V) invokeMethod(o1, StringUtils.split(nomChamp, "[.]"));
                            final V object2 = (V) invokeMethod(o2, StringUtils.split(nomChamp, "[.]"));
                            retour = ObjectUtils.compare(object1, object2, nullEnPremier);
                        } catch (Exception e) {
                            log.error(e, e.getMessage());
                        }
                        if (retour != 0) {
                            return mapNomsChampEtTri.get(nomChamp).getValue() * retour;
                        }
                    }
                    return retour;
                }
            };
        return comparator;
    }

    /**
     * Méthode permettant d'invoquer sur un objet une méthode à partir de son
     * nom, en construisant un accesseur sur le champ voulu. Le nom de la méthode est
     * récupéré depuis une liste de chaines comprenant les champs à lire. Tant que la
     * liste n'est pas vide, un appel récursif est effectué.
     *
     * @param o the o
     * @param lNomsChamp the l noms champ
     *
     * @return the object
     *
     * @throws IllegalAccessException the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws NoSuchMethodException the no such method exception
     */
    private static Object invokeMethod(Object o, List<String> lNomsChamp)
                                throws IllegalAccessException, InvocationTargetException,
                                       NoSuchMethodException {
        if (o == null) {
            return null;
        }
        final List<String> l = new ArrayList<String>(lNomsChamp);
        final Getter getter = buildGetterAvecChampsIndexes(l.remove(0));

        if (!org.apache.commons.collections.CollectionUtils.isEmpty(l)) {
            if (getter.getIndex() >= 0) {
                final Object oo = o.getClass().getMethod(getter.getNomChamp()).invoke(o);
                if (oo != null) {
                    final Object ooo =
                        oo.getClass().getMethod("get", new Class[] { int.class })
                          .invoke(oo, new Object[] { getter.getIndex() });
                    if (ooo != null) {
                        return invokeMethod(ooo, l);
                    }
                }
            } else {
                return invokeMethod(o.getClass().getMethod(getter.getNomChamp()).invoke(o),
                                    l);
            }
        } else {
            if (getter.getIndex() >= 0) {
                final Object oo = o.getClass().getMethod(getter.getNomChamp()).invoke(o);
                if (oo != null) {
                    return oo.getClass().getMethod("get", new Class[] { int.class })
                             .invoke(oo, new Object[] { getter.getIndex() });
                }
            } else {
                return o.getClass().getMethod(getter.getNomChamp()).invoke(o);
            }
        }
        return null;
    }

    /**
     * Génère un comparateur pour une classe et une liste de champs.
     *
     * @param <T> T
     * @param <V> V
     * @param c la classe d'objet à comparer
     * @param listeNomsChamp la liste des noms de champs de l'objet
     *
     * @return the comparateur
     */
    public static <T, V extends Comparable<?>> Comparator<T> getComparateur(final Class<T> c,
                                                                            final List<String> listeNomsChamp) {
        return getComparateur(c, listeNomsChamp, false);
    }

    /**
     * Génère un comparateur pour une classe et une liste de champs.
     *
     * @param <T> T
     * @param <V> V
     * @param c la classe d'objet à comparer
     * @param listeNomsChamp la liste des noms de champs de l'objet
     * @param nullEnPremier Valeur par défaut à utiliser en cas de valeur null
     *
     * @return the comparateur
     */
    public static <T, V extends Comparable<?>> Comparator<T> getComparateur(final Class<T> c,
                                                                            final List<String> listeNomsChamp,
                                                                            final boolean nullEnPremier) {
        Assert.isNotEmpty("listeNomsChamp", listeNomsChamp);

        final LinkedHashMap<String, TriComparateur> mapNomsChampEtTri =
            buildMapChampEtTri(listeNomsChamp);

        return getComparateur(c, mapNomsChampEtTri);
    }

    /**
     * Génère un comparateur pour une classe et un champ de comparaison.
     *
     * @param <T> T
     * @param <V> V
     * @param c la classe d'objet à comparer
     * @param nomChamp the nom champ
     *
     * @return the comparateur
     */
    public static <T, V extends Comparable<?>> Comparator<T> getComparateur(final Class<T> c,
                                                                            String nomChamp) {
        return getComparateur(c, nomChamp, false);
    }

    /**
     * Génère un comparateur pour une classe et un champ de comparaison.
     *
     * @param <T> T
     * @param <V> V
     * @param c la classe d'objet à comparer
     * @param nomChamp the nom champ
     * @param nullEnPremier Valeur par défaut à utiliser en cas de valeur null
     *
     * @return the comparateur
     */
    public static <T, V extends Comparable<?>> Comparator<T> getComparateur(final Class<T> c,
                                                                            String nomChamp,
                                                                            boolean nullEnPremier) {
        Assert.isNotNull("nomChamp", nomChamp);

        return getComparateur(c, Collections.singletonList(nomChamp), nullEnPremier);
    }

    /**
     * Construit le libelle de l'accesseur en ecriture à partir du nom d'un
     * attribut.
     *
     * @param nomChamp le nom du champ sur lequel trié
     *
     * @return le getter
     */
    private static String buildGetter(String nomChamp) {
        Assert.isNotNull("nomChampComparateur", nomChamp);

        String getter = "get" + nomChamp.substring(0, 1).toUpperCase();
        if (nomChamp.length() > 1) {
            getter += nomChamp.substring(1);
        }
        return getter;
    }

    /**
     * Construction le libellé de l'accesseur en lecture à partir d'un nom
     * d'attribut. ce nom peut être suffixé
     *
     * @param nomChamp nom du champ comportant éventuellement un indice (ex : champ[4])
     *
     * @return the getter
     */
    private static Getter buildGetterAvecChampsIndexes(String nomChamp) {
        Assert.isNotNull("nomChampComparateur", nomChamp);

        final Getter getter = new Getter();
        String nomChampExtrait = null;
        final int debutCrochet = nomChamp.indexOf("[");
        int index = -1;
        if (debutCrochet > 0) {
            nomChampExtrait = nomChamp.substring(0, debutCrochet);
            final String indiceChamp =
                nomChamp.substring(debutCrochet).replace("[", "").replace("]", "");
            try {
                index = Integer.valueOf(indiceChamp);
            } catch (Exception e) {
                log.debug("Le getter fourni est invalide : {0}", nomChamp);
            }
        } else {
            nomChampExtrait = nomChamp;
        }

        final String getterStr = buildGetter(nomChampExtrait);

        getter.setIndex(index);
        getter.setNomChamp(getterStr);
        return getter;
    }

    /**
     * Construit une map ( clé : nom du champ / value : tri ) avec une valeur de
     * tri par défaut à croissant.
     *
     * @param listeNomsChamp la liste des noms de champ
     *
     * @return la map ( clé : nom du champ / value : tri )
     */
    private static LinkedHashMap<String, TriComparateur> buildMapChampEtTri(List<String> listeNomsChamp) {
        Assert.isNotEmpty("listeNomsChamp", listeNomsChamp);

        final LinkedHashMap<String, TriComparateur> mapNomsChampEtTri =
            new LinkedHashMap<String, TriComparateur>();
        for (String string : listeNomsChamp) {
            mapNomsChampEtTri.put(string, TriComparateur.CROISSANT);
        }

        return mapNomsChampEtTri;
    }
}


/**
 * The Class Getter.
 */
class Getter {
    /** The nom champ. */
    private String nomChamp;

    /** The index. */
    private int index;

    /** The tri. */
    private TriComparateur tri;

    /**
     * DOCUMENT ME!
     *
     * @return the nomChamp
     */
    public String getNomChamp() {
        return nomChamp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param nomChamp the nomChamp to set
     */
    public void setNomChamp(String nomChamp) {
        this.nomChamp = nomChamp;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the tri
     */
    public TriComparateur getTri() {
        return tri;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tri the tri to set
     */
    public void setTri(TriComparateur tri) {
        this.tri = tri;
    }
}
