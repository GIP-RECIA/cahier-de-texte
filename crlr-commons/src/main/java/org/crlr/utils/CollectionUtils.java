/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: CollectionUtils.java,v 1.1 2009/03/17 16:02:48 ent_breyton Exp $
 */

package org.crlr.utils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

/**
 * Méthodes utilitaires pour des objets {@link Collection}.
 *
 * @author breytond
 * @author romana
 */
public final class CollectionUtils {
    /**
     * Constructeur privé pour interdire l'instanciation.
     */
    private CollectionUtils() {
    }

    /** The log. */
    private static final Log log = LogFactory.getLog(CollectionUtils.class);
    
    /**
     * Teste si une {@link Collection} est vide ou <code>null</code>.
     *
     * @param <T> type générique
     * @param col
     *            La collection à vérifier
     *
     * @return vrai si la collection est null ou vide.
     * @deprecated
     */
    public static <T> boolean isEmpty(Collection<T> col) {
        return org.apache.commons.collections.CollectionUtils.isEmpty(col);
    }

    /**
     * Retourne le nombre d'éléments de la collection.
     * @param <T> type générique
     * @param col collection
     * @return nombre d'éléments
     */
    public static <T> int size(Collection<T> col) {
        if (col == null) {
            return 0;
        } else {
            return col.size();
        }
    }

    /**
     * Découpe une liste d'éléments en plusieurs listes.
     *
     * @param <T> type générique
     * @param liste La liste à découper
     * @param tailleMax Le nombre d'éléments maximum à mettre dans les listes de
     *        retour
     *
     * @return Les listes composées des éléments de la liste de départ. Chaque liste
     *         a une taille inférieure à tailleMax.
     */
    public static <T> List<List<T>> split(List<T> liste, int tailleMax) {
        if (liste == null) {
            return null;
        }
        final int tailleListe = liste.size();
        if (tailleListe == 0) {
            return Collections.emptyList();
        }
        if (tailleListe <= tailleMax) {
            return Collections.singletonList(liste);
        }

        final BigDecimal t = new BigDecimal(tailleListe);
        final BigDecimal tMax = new BigDecimal(tailleMax);
        final BigDecimal nbSousListe = t.divide(tMax, RoundingMode.UP);

        final List<List<T>> listeRetour = new ArrayList<List<T>>();

        for (int i = 1; i <= nbSousListe.intValue(); i++) {
            listeRetour.add(CollectionUtils.sublist(liste, i, tailleMax));
        }

        return listeRetour;
    }

    /**
     * Découpe une liste d'éléments en plusieurs listes plus petites pour que
     * les requêtes HQL s'exécutent correctement.
     *
     * @param <T> type générique
     * @param liste La liste à découper
     *
     * @return Les listes composées des éléments de la liste de départ. Chaque liste
     *         a une taille permettant une requête HQL.
     */
    public static <T> List<List<T>> split(List<T> liste) {
        return split(liste, 800);
    }

    /**
     * Récupération du dernier élément d'une liste.
     * @param <T> type générique.
     * @param liste la liste.
     * @return le dernier élément.
     */
    public static <T> T getLastElement(List<T> liste) {
        if (isEmpty(liste)) {
            return null;
        } else {
            return liste.get(liste.size() - 1);
        }
    }

 

    /**
     * Permet de diposer d'une liste d'identifiant unique provenant d'une requête de type
     * "SELECT new Map()" par l'intermédiare d'un alias passé en paramètre.
     * @param liste la liste provenant d'une requête où autres.
     * @param identifiant l'alias servant de clef afin de rechercher la valeur adéquate dans la map.
     * @return la liste d'identifiant recherché.
     *
     */
    public static Set<Long> getListeIdentifiantFromMapHql(final List<Map<String,?>> liste, final String identifiant) {
        Assert.isNotNull("identifiant", identifiant);
        final Set<Long> listeIdentifiant = new HashSet<Long>();
        if (!isEmpty(liste)) {
            for (final Map<String,?> map : liste) {
                final Long id = (Long) map.get(identifiant);
                if (id != null) {
                    listeIdentifiant.add(id);
                }
            }
        }
        return listeIdentifiant;
    }

    /**
     * Formatte une liste de Map en Map(key,T).
     * l'identifiant passé en paramètre permet la reconnaissance de la clef de la map générée.
     * @param <T> type des éléments de retour.
     * @param <U> type de la clé des éléments de retour.
     * @param liste la liste contenant un ensemble de données non formattées.
     * @param identifiant la chaîne de caractères décrivant la clef d'accès à la key de la Map retournée
     * @param valeur la chaîne de caractères décrivant la clef d'accès à la valeur mise en correspondance avec la key dans la Map retournée.
     * Si la valeur est nulle, la map sera considéré comme valeur.
     * @return la map formattée.
     */
    @SuppressWarnings("unchecked")
    public static <T,U> Map<U, T> formatMapClefValeur(List<Map<String, ?>> liste,
                                                       String identifiant, String valeur) {
        Assert.isNotNull("identifiant", identifiant);
        final Map<U, T> mapKeyValue = new HashMap<U, T>();
        if (!isEmpty(liste)) {
            final boolean ligneComplete = org.apache.commons.lang.StringUtils.isEmpty(valeur);
            for (final Map<String, ?> map : liste) {
                final U key = (U) map.get(identifiant);
                if (ligneComplete) {
                    mapKeyValue.put(key, (T) map);
                } else {
                    final T value = (T) map.get(valeur);
                    mapKeyValue.put(key, value);
                }
            }
        }
        return mapKeyValue;
    }
    
    /**
     * Supprime les doublons du champ identifiant de la Liste de map.
     * @param <T> type des éléments de retour
     * @param <U> type de la clé
     * @param liste la liste à dédoublonner
     * @param identifiant le champ sur lequel dédoublonner
     * @return la liste sans doublons
     */
    public static <T,U> List<T> formatMapListeClefValeur(List<Map<String, ?>> liste, 
                                                        String identifiant) {
        Assert.isNotNull("identifiant", identifiant);
        Assert.isNotEmpty("liste", liste);
        final Map<U, T> mapKeyValue = formatMapClefValeur(liste, identifiant, null);
        final List<T> listKeyValue = new ArrayList<T>();
        for (final U mapKey : mapKeyValue.keySet()) {
            listKeyValue.add(mapKeyValue.get(mapKey));
        }
        
        return listKeyValue;
    }

    /**
     * Formatte une liste de Map en Map(key,T).
     * l'identifiant passé en paramètre permet la reconnaissance de la clef de la map générée.
     * Elle permet de concaténer n valeurs pour une clef.
     * @param <T> type des éléments de retour une liste de valeur.
     * @param <U> type de la clé des éléments de retour.
     * @param liste la liste contenant un ensemble de données non formattées.
     * @param identifiant la chaîne de caractères décrivant la clef d'accès à la key de la Map retournée
     * @param valeur la chaîne de caractères décrivant la clef d'accès à la valeur mise en correspondance avec la key dans la Map retournée.
     * Si la valeur est nulle, la map sera considéré comme valeur.
     * @return la map formattée.
     */
    public static <T,U> Map<U, List<T>> formatMapClefEnsembleValeurs(List<Map<String, ?>> liste,
                                                       String identifiant, String valeur) {
        return formatMapClefEnsembleValeursAvecOuSansLimitateur(liste, identifiant, valeur, 0);
    }

    /**
     * Formatte une liste de Map en Map(key,T).
     * l'identifiant passé en paramètre permet la reconnaissance de la clef de la map générée.
     * Elle permet de concaténer n valeurs pour une clef avec une limitation.
     * @param <T> type des éléments de retour une liste de valeur.
     * @param <U> type de la clé des éléments de retour.
     * @param liste la liste contenant un ensemble de données non formattées.
     * @param identifiant la chaîne de caractères décrivant la clef d'accès à la key de la Map retournée
     * @param valeur la chaîne de caractères décrivant la clef d'accès à la valeur mise en correspondance avec la key dans la Map retournée.
     * @param limitation la limitation du nombre de valeur de la liste liée à une clef.
     * Si la valeur est nulle, la map sera considéré comme valeur.
     * @return la map formattée.
     */
    public static <T,U> Map<U, List<T>> formatMapClefEnsembleValeursAvecLimitateur(List<Map<String, ?>> liste,
            String identifiant, String valeur, Integer limitation) {
        return formatMapClefEnsembleValeursAvecOuSansLimitateur(liste, identifiant, valeur, limitation);
    }

    /**
     * Utilisé par formatMapClefEnsembleValeurs et par formatMapClefEnsembleValeursAvecOuSansLimitateur.
     * @param <T> type des éléments de retour une liste de valeur.
     * @param <U> type de la clé des éléments de retour.
     * @param liste la liste contenant un ensemble de données non formattées.
     * @param identifiant la chaîne de caractères décrivant la clef d'accès à la key de la Map retournée
     * @param valeur la chaîne de caractères décrivant la clef d'accès à la valeur mise en correspondance avec la key dans la Map retournée.
     * @param limitation la limitation du nombre de valeur de la liste liée à une clef.
     * Si la valeur est nulle, la map sera considéré comme valeur.
     * @return la map formattée.
     */
    @SuppressWarnings("unchecked")
    private static <T,U> Map<U, List<T>> formatMapClefEnsembleValeursAvecOuSansLimitateur(List<Map<String, ?>> liste,
            String identifiant, String valeur, Integer limitation) {
        Assert.isNotNull("identifiant", identifiant);
        Assert.isNotNull("limitation", limitation);
        final Map<U, List<T>> mapKeyValue = new HashMap<U, List<T>>();
        final Map<U, Integer> mapLimitation = new HashMap<U, Integer>();
        if (!isEmpty(liste)) {
            final boolean ligneComplete = org.apache.commons.lang.StringUtils.isEmpty(valeur);
            for (final Map<String, ?> map : liste) {
                final U key = (U) map.get(identifiant);
                if (mapKeyValue.containsKey(key)) {
                    final Integer limitationEnCours = mapLimitation.get(key);
                    if (limitationEnCours < limitation || limitation == 0) {
                        final List<T> listeResult = mapKeyValue.get(key);
                        if (ligneComplete) {
                            listeResult.add((T)map);
                        } else {
                            listeResult.add((T)map.get(valeur));
                        }
                        mapKeyValue.put(key, listeResult);
                        mapLimitation.put(key, limitationEnCours  + 1);
                    }
                } else {
                    final List<T> listeResult = new ArrayList<T>();
                    if (ligneComplete) {
                        listeResult.add((T)map);
                    } else {
                        listeResult.add((T)map.get(valeur));
                    }
                    mapKeyValue.put(key, listeResult);
                    mapLimitation.put(key, 1);
                }
            }
        }
        return mapKeyValue;

    }

    /**
     * Formatte une liste de Map en Map(key,T).
     * l'identifiant passé en paramètre permet la reconnaissance de la clef de la map générée.
     * Elle permet de concaténer n valeurs uniques pour une clef.
     * @param <T> type des éléments de retour un set de valeur.
     * @param <U> type de la clé des éléments de retour.
     * @param liste la liste contenant un ensemble de données non formattées.
     * @param identifiant la chaîne de caractères décrivant la clef d'accès à la key de la Map retournée
     * @param valeur la chaîne de caractères décrivant la clef d'accès à la valeur mise en correspondance avec la key dans la Map retournée.
     * Si la valeur est nulle, la map sera considéré comme valeur.
     * @return la map formattée.
     */
    @SuppressWarnings("unchecked")
    public static <T,U> Map<U, Set<T>> formatMapClefSetValeurs(List<Map<String, ?>> liste,
                                                       String identifiant, String valeur) {
        Assert.isNotNull("identifiant", identifiant);
        final Map<U, Set<T>> mapKeyValue = new HashMap<U, Set<T>>();
        if (!isEmpty(liste)) {
            final boolean ligneComplete = org.apache.commons.lang.StringUtils.isEmpty(valeur);
            for (final Map<String, ?> map : liste) {
                final U key = (U) map.get(identifiant);
                if (mapKeyValue.containsKey(key)) {
                    final Set<T> setResult = mapKeyValue.get(key);
                    if (ligneComplete) {
                        setResult.add((T)map);
                    } else {
                        setResult.add((T)map.get(valeur));
                    }
                    mapKeyValue.put(key, setResult);
                } else {
                    final Set<T> setResult = new HashSet<T>();
                    if (ligneComplete) {
                        setResult.add((T)map);
                    } else {
                        setResult.add((T)map.get(valeur));
                    }
                    mapKeyValue.put(key, setResult);
                }
            }
        }
        return mapKeyValue;
    }


    /**
     * Formate une Map(String, T) en List(T).
     * @param <T> type des éléments de retour.
     * @param map la map contenant un ensemble de données non formattées.
     * @param identifiant la chaîne de caractères décrivant la clef à utiliser pour remplir la liste,
     * si l'identifiant est nul, toutes les valeurs de la map, pour toutes les clefs seront contenues dans la liste.
     * @return la liste.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> formatMapEnliste(Map<String, ?> map, String identifiant) {
        final List<T> liste = new ArrayList<T>();
        if (map != null) {
            for (final String key : map.keySet()) {
                if (!org.apache.commons.lang.StringUtils.isEmpty(identifiant)) {
                    if (identifiant.equals(key)) {
                        liste.add((T)map.get(key));
                    }
                } else {
                    liste.add((T)map.get(key));
                }
            }
        }
        return liste;
    }

    /**
     * Retourne l'ensemble des valeurs (avec un DISTINCT) de la colonne 'nomChamp' dans
     * la liste des résultats de la requête 'liste'.
     * @param <T> Type des éléments de retour.
     * @param liste Liste des résultats de la requête.
     * @param nomChamp Nom du champ des données à récupérer.
     * @return L'ensemble des valeurs (avec un DISTINCT) de la colonne.
     */
    public static <T> List<T> findEnsemble(List<Map<String, ?>> liste, String nomChamp) {
        final Map<T, ?> mapInfos = formatMapClefValeur(liste, nomChamp, null);
        return new ArrayList<T>(mapInfos.keySet());
    }

    /**
     * clone un objet itérable de tout type ({@link Map}, {@link Collection}, Object[]) en profondeur.
     * @version 1.0
     * @param <T> le type de retour.
     * @param iterateObject l'objet itérable à cloner.
     * @return l'objet itérable cloné.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T cloneCollection(T iterateObject) {
        try {
            if (iterateObject == null) {
                return null;
            } else if (iterateObject.getClass().isArray()) {
                //clonage du tableau.
                return (T)cloneTableau((Object[])iterateObject);
            } else if (iterateObject instanceof Collection) {
                //clonage de la collection.
                return (T)cloneCollectionPrivate((Collection) iterateObject);
            } else if (iterateObject instanceof Map) {
                //clonage de la map.
                return (T)cloneMap((Map) iterateObject);
            } else {
                throw new CloneNotSupportedException("L'objet passé en paramètre n'est pas un objet itérable.");
            }
        } catch (final Exception e) {
            throw new RuntimeException("Erreur durant l'opération de clonage, " +
                    "vérifiez que tout les objets disposent d'un constructeur par défaut " +
                    ": " + e.getMessage(), e);
        }
    }

    /**
     * clone une {@link Collection} en profondeur.
     * @param collectionAcloner la collection à cloner.
     * @param <T> le type de retour.
     * @return la collection clonée.
     * @throws Exception l'exception possible durant le clonage.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <T> T cloneCollectionPrivate(Collection collectionAcloner)
                                  throws Exception {
        //invocation de la méthode clone de cette collection (on la clone naturellement)
        final Collection collectionClone = (Collection) invokeClone(collectionAcloner);

        //si la collection contient des elements
        if (!collectionAcloner.isEmpty()) {
            //on vide la collection puisqu'on va la remplir avec des références clonées si les objets sont non immuables
            collectionClone.clear();

            //Création d'un iterateur sur la collection afin de cloner les
            //elements complexes de la collection
            final Iterator it = collectionAcloner.iterator();

            //parcours des éléments
            while (it.hasNext()) {
                final Object objetAcloner = it.next();

                //              vérification de la nature des objets contenus dans la collection
                if (ObjectUtils.isDeepClonableObject(objetAcloner)) {
                    try {
                        //ajout de l'élément cloné par l'intermédaire de la méthode
                        //clone de l'objet à la collection clonée.
                        collectionClone.add(invokeClone(objetAcloner));
                    } catch (final NoSuchMethodException e) {
//                      si la méthode clone n'existe pas on utilise alors ObjectUtils.clone();
                        collectionClone.add(ObjectUtils.clone(objetAcloner));
                    }
                } else if (ObjectUtils.isIterableObject(objetAcloner)) {
                    //                  l'objet est itérable
                    collectionClone.add(cloneCollection(objetAcloner));
                } else {
                    //l'objet doit être immuable
                    collectionClone.add(objetAcloner);
                }
            }
        }
        return (T) collectionClone;
    }

    /**
     * clone une {@link Map} en profondeur.
     * @param mapAcloner la map à cloner.
     * @param <T> le type de retour.
     * @return la map clonée.
     * @throws Exception l'exception possible durant le clonage.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <T> T cloneMap(Map mapAcloner)
                           throws Exception {
        //invocation de la méthode clone de cette map (on la clone naturellement)
        final Map mapClone = (Map) invokeClone(mapAcloner);

        //si la map contient des elements
        if (!mapAcloner.isEmpty()) {
            //on vide la map puisqu'on va la remplir avec des références clonées si les objets sont non immuables
            mapClone.clear();

            //Création d'un iterateur sur les clefs de la map afin de cloner les
            //elements
            final Iterator it = mapAcloner.keySet().iterator();

            //parcours des éléments
            while (it.hasNext()) {
                final Object key = it.next();
                final Object objetAcloner = mapAcloner.get(key);

                //              vérification de la nature des objets contenus dans la map
                if (ObjectUtils.isDeepClonableObject(objetAcloner)) {
                    try {
                        //ajout de l'élément cloné par l'intermédaire de la méthode
                        //clone de l'objet à la map clonée.
                        mapClone.put(key, invokeClone(objetAcloner));
                    } catch (final NoSuchMethodException e) {
//                      si la méthode clone n'existe pas on utilise alors ObjectUtils.clone();
                        mapClone.put(key, ObjectUtils.clone(objetAcloner));
                    }
                } else if (ObjectUtils.isIterableObject(objetAcloner)) {
                    //                  l'objet est itérable
                    mapClone.put(key, cloneCollection(objetAcloner));
                } else {
                    //                  l'objet doit être immuable
                    mapClone.put(key, objetAcloner);
                }
            }
        }
        return (T) mapClone;
    }

    /**
     * clone un Tableau d'objet en profondeur.
     * @param tableauAcloner la tableau à cloner.
     * @param <T> le type de retour.
     * @param <U> le type des elements du tableau.
     * @return le tableau cloné.
     * @throws Exception l'exception possible durant le clonage.
     */
    @SuppressWarnings("unchecked")
    private static <T, U> T cloneTableau(U[] tableauAcloner)
                               throws Exception {
        //clone naturellement le tableau
        if (tableauAcloner == null) {
            return null;
        }
        return (T) cloneCollection(CollectionUtils.creerList(tableauAcloner)).toArray(tableauAcloner.clone());
    }

    /**
     * Invoque la méthode clone d'un objet.
     * @param objetAcloner l'objet.
     * @return l'objet cloné.
     * @throws Exception l'exception possible lors de l'appel de la méthode.
     */
    private static Object invokeClone(Object objetAcloner)
                               throws Exception {
        //      récupération de la méthode clone de l'objet par réflexion
        final Method m1 = objetAcloner.getClass().getMethod("clone");

        //invocation de la méthode clone de cet objet
        return m1.invoke(objetAcloner);
    }

    /**
     * Permet de mettre les valeurs d'une collection sans accent (les tableaux, List, et Set).
     * @param <T> le type.
     * @param iterateObject l'objet à traiter.
     * @return la collection dont les valeurs ont été mises en majuscule.
     */
    public static <T> T elementSansAccens(T iterateObject) {
        return elementSansAccensToUpperCase(iterateObject, Boolean.FALSE);
    }

    /**
     * Permet de mettre les valeurs d'une collection en majuscule sans accent (les tableaux, List, et Set).
     * @param <T> le type.
     * @param iterateObject l'objet à traiter.
     * @return la collection dont les valeurs ont été mises en majuscule.
     */
    public static <T> T elementSansAccensMajuscule(T iterateObject) {
        return elementSansAccensToUpperCase(iterateObject, Boolean.TRUE);
    }

    /**
     * Permet de mettre les valeurs d'une collection en majuscule sans accent (les tableaux, List, et Set).
     * @param <T> le type.
     * @param isUpperCase true en majuscule, si non false.
     * @param iterateObject l'objet à traiter.
     * @return la collection dont les valeurs ont été mises en majuscule.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <T> T elementSansAccensToUpperCase(T iterateObject, Boolean isUpperCase) {
        if (ObjectUtils.isIterableObject(iterateObject)) {
            if (iterateObject instanceof Object[]) {
                return elementTableauPourSansAccens(iterateObject, isUpperCase);
            } else if (iterateObject instanceof Collection) {
                final Object[] liste = ((Collection)iterateObject).toArray();
                return (T)((Collection)Arrays.asList(elementTableauPourSansAccens(liste, isUpperCase)));
            }
            return iterateObject;
        } else {
            return iterateObject;
        }
    }


    /**
     * remplace chacune des chaines contenues dans le tableau par des chaines en majuscule sans accent.
     * @param <T> le type.
     * @param isUpperCase true en majuscule, si non false.
     * @param iterateObject le tableau.
     * @return le tableau.
     */
    @SuppressWarnings("unchecked")
    private static <T> T elementTableauPourSansAccens(T iterateObject, Boolean isUpperCase) {
        final Object[] tableau = (Object[])iterateObject;
        for (int i=0;i<tableau.length;i++) {
            if (tableau[i] instanceof String) {
                if (isUpperCase) {
                    tableau[i] = StringUtils.sansAccentEnMajuscule(tableau[i].toString());
                } else {
                    tableau[i] = StringUtils.sansAccent(tableau[i].toString());
                }
            }
        }

        return (T)tableau;
    }

    /**
     * Renvoie l'intersection de deux listes d'objets de type T.
     * Le champ identifiant l'objet doit posséder un getter.
     * @param <T> le type.
     * @param liste1 la première liste.
     * @param liste2 la seconde liste.
     * @param nomChampIdentifiant la chaîne correspondant à l'identifiant.
     * @return Liste de T, l'intersection des 2 listes.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> intersect(List<T> liste1, List<T> liste2, String nomChampIdentifiant) {
        Assert.isNotNull("nomChampIdentifiant", nomChampIdentifiant);
        Assert.isNotNull("liste1", liste1);
        Assert.isNotNull("liste2", liste2);
        String getterChampIdentifiant = "get" + nomChampIdentifiant.substring(0,1).toUpperCase();
        if(nomChampIdentifiant.length()>1){
            getterChampIdentifiant+= nomChampIdentifiant.substring(1);
        }

        final List<T> listeInter = new ArrayList<T>();

        final Set<Object> idsListe1 = new HashSet<Object>();

        for(final T objetT : liste1){
            Object idObjetT;
            try {
                idObjetT = objetT.getClass().getMethod(getterChampIdentifiant, new Class[]{}).invoke(objetT, new Object[]{});
            } catch (final Exception e){
                log.error(e, e.getMessage());
                return Collections.EMPTY_LIST;
            }
            idsListe1.add(idObjetT);
        }
        for(final T objetT : liste2){
            try{
                final Object idObjetT = objetT.getClass().getMethod(getterChampIdentifiant, new Class[]{}).invoke(objetT, new Object[]{});
                if(idsListe1.contains(idObjetT)){
                    listeInter.add(objetT);
                }
            } catch(final Exception e){
                log.error(e, e.getMessage());
                return Collections.EMPTY_LIST;
            }
        }

        return listeInter;
    }
    
    /**
     * Renvoie la difference de deux listes d'objets de type T.
     * Le champ identifiant l'objet doit posséder un getter si nomChampIdentifiant est renseigné.
     * @param <T> le type.
     * @param liste1 la première liste.
     * @param liste2 la seconde liste.
     * @param nomChampIdentifiant la chaîne correspondant à l'identifiant ou null pour des listes simples.
     * @return Liste de T, la différence entre les 2 listes.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> List<T> difference(List<T> liste1, List<T> liste2, String nomChampIdentifiant) {        
        Assert.isNotNull("liste1", liste1);
        Assert.isNotNull("liste2", liste2);
        String getterChampIdentifiant = null;
        if (nomChampIdentifiant != null) {
            getterChampIdentifiant = "get" + nomChampIdentifiant.substring(0,1).toUpperCase();
            if(nomChampIdentifiant.length()>1){
                getterChampIdentifiant+= nomChampIdentifiant.substring(1);
            }
        }
        
        final List<T> listeDiff = new ArrayList<T>();
        
        final Set<Object> idsListe1 = new HashSet<Object>();
        if (getterChampIdentifiant != null) {
            for(final T objetT : liste1){
                Object idObjetT;
                try {
                    idObjetT = objetT.getClass().getMethod(getterChampIdentifiant, new Class[]{}).invoke(objetT, new Object[]{});
                } catch (final Exception e){
                    log.error(e, e.getMessage());
                    return Collections.EMPTY_LIST;
                }
                idsListe1.add(idObjetT);
            }
        } else {
            idsListe1.addAll(liste1);
        }
        
        final Set<Object> idsListe2 = new HashSet<Object>();
        if (getterChampIdentifiant != null) {
            for(final T objetT : liste2){
                Object idObjetT;
                try {
                    idObjetT = objetT.getClass().getMethod(getterChampIdentifiant, new Class[]{}).invoke(objetT, new Object[]{});
                } catch (final Exception e){
                    log.error(e, e.getMessage());
                    return Collections.EMPTY_LIST;
                }
                idsListe2.add(idObjetT);
            }
        } else {
            idsListe2.addAll(liste2);
        }
        
        int i =0;
        final Iterator it1 = idsListe1.iterator();

        //parcours des identifiants des éléments de la liste 1
        while (it1.hasNext()) {
            final Object o = it1.next();              
            if (!idsListe2.contains(o)) {
                listeDiff.add(liste1.get(i));
            }                
            i++;    
        }
        
        final Iterator it2 = idsListe2.iterator();
        i = 0;
        
        //parcours des identifiants des éléments de la liste 2
        while (it2.hasNext()) {
            final Object o = it2.next();              
            if (!idsListe1.contains(o) && !listeDiff.contains(o)) {
                listeDiff.add(liste2.get(i));
            }                
            i++;    
        }        
        
        return listeDiff;
    }

    /**
     * Concatène tous les éléments d'une collection en une chaîne de caractères séparés par un espace.
     *
     * @param liste Liste à formatter
     *
     * @return Chaîne de caractères non nulle
     */
    public static String collectionToString(Collection<String> liste) {
        return collectionToString(liste, " ", Boolean.TRUE);
    }

    /**
     * Concatène tous les éléments d'une collection en une chaîne de caractères séparés par un espace.
     *
     * @param liste Liste à formatter
     * @param withNull Conserve ou non les valeurs nulles
     *
     * @return Chaîne de caractères non nulle
     */
    public static String collectionToString(Collection<String> liste, Boolean withNull) {
        return collectionToString(liste, " ", withNull);
    }

    /**
     * Concatène tous les éléments d'une collection en une chaîne de caractères séparés par un espace.
     *
     * @param liste Liste à formatter
     * @param separateur le séparateur des éléments de la liste
     *
     * @return Chaîne de caractères non nulle
     */
    public static String collectionToString(Collection<String> liste, String separateur) {
        return collectionToString(liste, separateur, Boolean.TRUE);
    }

    /**
     * Concatène tous les éléments d'une collection en une chaîne de caractères
     * séparés par un séparateur désiré.
     *
     * @param liste Liste à formatter
     * @param separateur le séparateur des éléments de la liste
     * @param withNull Conserve ou non les valeurs nulles
     *
     * @return Chaîne de caractères non nulle
     */
    public static String collectionToString(Collection<String> liste, String separateur, Boolean withNull) {
        final String sep = ObjectUtils.defaultIfNull(separateur, " ");
        final StringBuilder stringBuilder = new StringBuilder("");
        if (!isEmpty(liste)) { 
            for (final String string : liste) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(sep);
                }
                stringBuilder.append(!org.apache.commons.lang.StringUtils.isEmpty(string) ? string : (withNull ? " null " : " "));
            }
        }
        return stringBuilder.toString().trim();

    }

    /**
     * Vérifie si les éléments d'une collection apparaissent une et une seule fois dans la collection.
     *
     * @param <T> le type.
     * @param c l'instance de collection
     * @return un booléen, True si les éléments apparaissent une et une seule fois, False sinon
     */
    public static <T> Boolean hasNoDuplicateElement(Collection<T> c) {
        if(org.apache.commons.collections.CollectionUtils.isEmpty(c)) {
            return Boolean.TRUE;
        }
        final Set<T> set = new HashSet<T>(c);
        for (final T t : set) {
            if(Collections.frequency(c, t) != 1) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * Récupère les éléments d'une collection apparaissant plus d'une fois dans la collection.
     *
     * @param <T> le type.
     * @param c l'instance de collection
     * @return un Set des éléments apparaissant plus d'une fois dans la collection.
     */
    public static <T> Set<T> getDuplicateElement(Collection<T> c) {
        if(org.apache.commons.collections.CollectionUtils.isEmpty(c)) {
            return Collections.emptySet();
        }
        final Set<T> set = new HashSet<T>();
        for (final T t : c) {
            if(Collections.frequency(c, t) != 1) {
                set.add(t);
            }
        }
        return set;
    }

    /**
     * Vérifie si 2 collections sont totalement disjointes.
     *
     * @param c1 la premiere instance de collection
     * @param c2 la seconde instance de collection
     * @return un Booléen, True si les collections sont disjointes, False sinon
     */
    public static Boolean disjoint(Collection<?> c1, Collection<?> c2) {
        if(c1 == null && c2 == null) {
            return Boolean.FALSE;
        }
        if(c1 == null || c2 == null) {
            return Boolean.TRUE;
        }
        return Collections.disjoint(c1, c2);
    }

    /**
     * Créer une sous liste à partir de la liste passée en paramètre.
     *
     * @param <T> Le type des éléments de la liste.
     * @param list La liste totale
     * @param indiceSousListe l'indice de sous liste a récupérer
     * @param nombreElementsSousListe le nombre d'éléments à mettre par sous liste.
     *
     * @return La sous liste contenant uniquement les éléments de l'indice de sous liste spécifié.
     */
    public static <T> List<T> sublist(List<T> list, Integer indiceSousListe, Integer nombreElementsSousListe) {
        if(org.apache.commons.collections.CollectionUtils.isEmpty(list)) {
            return list;
        }
        Integer indiceSousListeTMP = ObjectUtils.defaultIfNull(indiceSousListe, 0);
        indiceSousListeTMP = (indiceSousListeTMP < 0) ? 0 : indiceSousListeTMP;
        Integer nombreElementsSousListeTMP = ObjectUtils.defaultIfNull(nombreElementsSousListe, 200);
        nombreElementsSousListeTMP = (nombreElementsSousListeTMP < 1) ? 1 : nombreElementsSousListeTMP;

        if(nombreElementsSousListeTMP > list.size()) {
            return list;
        }

        final int indiceDebut = nombreElementsSousListeTMP * (indiceSousListeTMP - 1) ;
        final int indiceFin = Math.min(indiceDebut + nombreElementsSousListeTMP, list.size());

        return new ArrayList<T>(list.subList(indiceDebut, indiceFin));
    }

    /**
     * {@inheritDoc}
     */
    public static <T> List<T> sublist(List<T> list, Integer indiceSousListe) {
        return CollectionUtils.sublist(list, indiceSousListe, 200);
    }

    /**
     * {@inheritDoc}
     */
    public static <T> List<T> sublist(List<T> list) {
        return CollectionUtils.sublist(list, 0);
    }

    /**
     * Ajoute le contenu de la liste à inserer dans la liste destination.
     *
     * @param <T> le type.
     * @param listeDestination la liste destination
     * @param listeAInserer la liste a inserer
     *
     * @return the list<T>
     */
    public static <T> List<T> addAll(List<T> listeDestination, List<T> listeAInserer){
        if(listeDestination == null && listeAInserer == null) {
            return new ArrayList<T>();
        }
        if(listeDestination == null && listeAInserer != null) {
            return new ArrayList<T>(listeAInserer);
        }
        if(listeDestination != null && listeAInserer == null) {
            return new ArrayList<T>(listeDestination);
        }
        final List<T> listeRetour = new ArrayList<T>(listeDestination);
        listeRetour.addAll(listeAInserer);
        return listeRetour;
    }

    /**
     * Création d'un Set à partir d'une collection.
     * @param <T> type des éléments de la liste
     * @param col collection contenant les éléments à ajouter dans le set
     * @return Le set avec les éléments de la colllection
     */
    public static <T> Set<T> creerSet(Collection<T> col) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(col)) {
            return new HashSet<T>();
        } else {
            return new HashSet<T>(col);
        }
    }

    /**
     * Création d'un Set à partir d'un tableau.
     * @param <T> type des éléments de la liste
     * @param objets éléments à ajouter dans le set
     * @return Le set avec les éléments de la colllection
     */
    public static <T> Set<T> creerSet(T... objets) {
        if (objets == null) {
            return new HashSet<T>();
        } else {
            final Set<T> s = new HashSet<T>(objets.length);
            for (final T o : objets) {
                s.add(o);
            }
            return s;
        }
    }

    /**
     * Création d'une List à partir d'une collection.
     * @param <T> type des éléments de la liste
     * @param col collection contenant les éléments à ajouter dans la liste
     * @return La liste avec les éléments de la colllection
     */
    public static <T> List<T> creerList(Collection<T> col) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(col)) {
            return new ArrayList<T>();
        } else {
            return new ArrayList<T>(col);
        }
    }


    /**
     * Création d'une List à partir d'un tableau.
     * @param <T> type des éléments de la liste
     * @param objets éléments à ajouter dans la liste
     * @return La liste avec les éléments de la colllection
     */
    public static <T> List<T> creerList(T... objets) {
        if (objets == null) {
            return new ArrayList<T>();
        } else {
            final List<T> l = new ArrayList<T>(objets.length);
            for (final T o : objets) {
                l.add(o);
            }
            return l;
        }
    }


    /**
     * Regroupement d'une collection splittée. Doublons possibles.
     * @param <U> type des elements de la collection
     * @param <T> type de la collection
     * @param colCol collection de collection
     * @return liste avec les elements
     */
    public static <U, T extends Collection<U>> List<U> join(T... colCol) {
        final List<U> liste = new ArrayList<U>();
        for (final Collection<U> col : colCol) {
            if (col != null) {
                liste.addAll(col);
            }
        }
        return liste;
    }
    
    /**
     * Regroupement d'une collection splittée sans doublons possibles.
     * @param <U> type des elements de la collection
     * @param <T> type de la collection
     * @param colCol collection de collection
     * @return set avec les elements
     */
    public static <U, T extends Collection<U>> Set<U> joinSansDoublons(T... colCol) {
        final Set<U> set = new HashSet<U>();
        for (final Collection<U> col : colCol) {
            set.addAll(col);
        }
        return set;
    }


    /**
     * Regroupement d'une collection splittée. Doublons possibles.
     * @param <T> type des elements de la collection
     * @param colCol collection de collection
     * @return liste avec les elements
     */
    public static <T> List<T> join(Collection<Collection<T>> colCol) {
        final List<T> liste = new ArrayList<T>();
        for (final Collection<T> col : colCol) {
            liste.addAll(col);
        }
        return liste;
    }

    /**
     * Regroupement d'une collection splittée. Doublons impossibles.
     * @param <T> type des elements de la collection
     * @param colCol collection de collection
     * @return ensemble avec les elements
     */
    public static <T> Set<T> joinSansDoublons(Collection<Collection<T>> colCol) {
        final Set<T> ensemble = new HashSet<T>();
        for (final Collection<T> col : colCol) {
            ensemble.addAll(col);
        }
        return ensemble;
    }

    /**
     * Retourne l'élément "i" d'une liste.
     *
     * @param l la liste
     * @param i l'indice
     * @param <T> type des éléments de la liste.
     *
     * @return the element
     */
    public static <T> T getElement(List<T> l, Integer i) {
        Assert.isNotNull("i", i);

        if(i < 0 || CollectionUtils.size(l) <= i) {
            return null;
        }
        return l.get(i);
    }

    /**
     * Ajoute la valeur à la liste des enregistrements classés à la clé dans la map.
     * @param <K> type de la cle
     * @param <V> Type des valeurs
     * @param map map contenant les infos
     * @param cle cle de la valeur a ajouter
     * @param valeur valeur a ajouter
     */
    public static <K, V> void ajouteValeurDansListeMap(Map<K, List<V>> map, K cle, V valeur) {
        List<V> listePourCle = map.get(cle);
        if (listePourCle == null) {
            listePourCle = new ArrayList<V>();
            map.put(cle, listePourCle);
        }
        listePourCle.add(valeur);
    }
    

    /**
     * Conçoit une liste du type T d'une taille absolue définie par nb.
     * Renvoi donc les n occurrences de la liste de départ, et des lignes vides d'instances distinctes,
     * si besoin est. 
     * @param <T> le type
     * @param liste la liste de départ.
     * @param nb le nombre d'élément que doit faire la liste de retour.
     * @param obj une instance d'un objet du type T initialisée par le constructeur par défaut.
     * @return la liste avec la taille absolue.
     */
    public static <T> List<T> obtenirListeAbsolue(final List<T> liste, final Integer nb,
            final T obj) {
        final List<T> listeRetour = new ArrayList<T>();
        int cpt = 0;
        for (final T t : liste) {
            listeRetour.add(t);
            cpt++;
            if (cpt == nb) {break;}
        }
        
        int tailleFinale = listeRetour.size();
        
        while(tailleFinale != nb) {            
            listeRetour.add(ObjectUtils.clone(obj));
            tailleFinale++;
        }
        
        return (List<T>)listeRetour;
    }
    
    /**
     * Ajoute un objet dans une map avec plusieurs clés.
     * @param <T> type des objets de la clé
     * @param map map dans laquelle il faut ajouter des données
     * @param objets données à ajouter
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> void addInMap(Map map, T... objets) {
        Map mapBoucle = map;
        final int nbObjets = objets.length;
        
        for (int i=0; i<nbObjets - 2; i++) {
            Map map2 = (Map)mapBoucle.get(objets[i]);
            if (map2 == null) {
                map2 = new HashMap();
                mapBoucle.put(objets[i], map2);
            }
            mapBoucle = map2;
        }
        
        mapBoucle.put(objets[nbObjets - 2], objets[nbObjets - 1]);
    }
    

    /**
     * Ajoute un objet dans une map avec plusieurs clés.
     * @param <T> type des objets de la clé
     * @param map map dans laquelle il faut ajouter des données
     * @param objets données à ajouter
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> void addInListMap(Map map, T... objets) {
        Map mapBoucle = map;
        final int nbObjets = objets.length;
        
        
        for (int i=0; i<nbObjets - 2; i++) {
            Map map2 = (Map)mapBoucle.get(objets[i]);
            if (map2 == null) {
                map2 = new HashMap();
                mapBoucle.put(objets[i], map2);
            }
            mapBoucle = map2;
        }
        
        final T cleResultat = objets[nbObjets - 2];
        List listeResultats = (List)mapBoucle.get(cleResultat);
        if (listeResultats == null) {
            listeResultats = new ArrayList();
            mapBoucle.put(cleResultat, listeResultats);
        }
        listeResultats.add(objets[nbObjets - 1]);
    }
    
    /**
     * Retourne l'objet associé à l'ensemble des clés indiquées.
     * @param <T> Type de retour
     * @param <U> Type des cles
     * @param map map contenant les informations
     * @param cles cles dans l'ordre à utiliser
     * @return Valeur contenue en utilisant les clés
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T, U> T findInMap(Map map, U... cles) {
        Map mapBoucle = map;
        for (int i=0; i<cles.length - 1; i++) {
            final Map map2 = (Map)mapBoucle.get(cles[i]);
            if (map2 == null) {
                return null;
            }
            mapBoucle = map2;
        }
        
        final T obj = (T)mapBoucle.get(cles[cles.length - 1]);
        return obj;
    }
}
