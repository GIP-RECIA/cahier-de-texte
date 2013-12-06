/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ObjectUtils.java,v 1.5 2010/04/29 09:16:41 jerome.carriere Exp $
 */

package org.crlr.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.lang.StringUtils;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;

/**
 * Méthodes utilitaires pour objets.
 *
 * @author breytond
 */
public final class ObjectUtils {

    private static final Log log = LogFactory.getLog(ObjectUtils.class);
    
    /** Le premier element est le plus petit. */
    public static final int COMPARE_PLUS_PETIT_EN_PREMIER = -1;

    /** Le premier element est le plus grand. */
    public static final int COMPARE_PLUS_GRAND_EN_PREMIER = 1;

    /** Les 2 elements sont egaux. */
    public static final int COMPARE_EGALITE = 0;

/**
     * Constructeur privé pour interdire l'instanciation.
     */
    private ObjectUtils() {
    }

    /**
     * Retourne une valeur par défaut pour un objet si celui-ci est
     * <code>null</code>.
     *
     * @param <T> type générique
     * @param obj the obj
     * @param def the def
     *
     * @return the T
     */
    public static <T> T defaultIfNull(T obj, T def) {
        return (obj == null) ? def : obj;
    }

    /**
     * Concat?ne deux cha?nes. Une cha?ne <code>null</code> est remplac?e par une
     * cha?ne vide.
     *
     * @param a the a
     * @param b the b
     *
     * @return the string
     */
    public static String prefixIfNotNull(String a, Object b) {
        if (b != null) {
            final String trimToBlank = b.toString();
            if (!"".equals(trimToBlank)) {
                return a + trimToBlank;
            }
        }
        return "";
    }

    /**
     * Retourne Vrai si les 2 objets sont identiques ou tous les 2 nuls.
     *
     * @param <T> type générique
     * @param o1 Premier objet à comparer
     * @param o2 Second objet à comparer
     *
     * @return Vrai si les 2 objets sont identiques ou tous les 2 nuls.
     */
    public static <T> boolean equals(T o1, T o2) {
        if (o1 == null) {
            return (o2 == null);
        } else {
            if (o2 == null) {
                return false;
            } else {
                return o1.equals(o2);
            }
        }
    }

    /**
     * Retourne vrai si les 2 objets sont identiques et qu'ils ne sont pas nuls.
     *
     * @param <T> type générique
     * @param o1 Premier objet à comparer
     * @param o2 Second objet à comparer
     *
     * @return vrai si les 2 objets sont identiques et qu'ils ne sont pas nuls.
     */
    public static <T> boolean equalsAndNotNull(T o1, T o2) {
        if ((o1 == null) && (o2 == null)) {
            return false;
        }

        return ObjectUtils.equals(o1, o2);
    }

    /**
     * Retourne l'ensemble des champs d'un objet, y compris ceux qui sont
     * hérités.
     *
     * @param obj the obj
     *
     * @return the fields
     */
    public static Set<Field> getFields(Object obj) {
        if (obj == null) {
            return new HashSet<Field>();
        }
        return getFields(obj.getClass());
    }

    /**
     * Retourne l'ensemble des champs d'une classe, y compris ceux hérités.
     *
     * @param clazz the clazz
     *
     * @return the fields
     */
    public static Set<Field> getFields(Class<?> clazz) {
        final Set<Field> fields = new HashSet<Field>();
        if (clazz == null) {
            return fields;
        }

        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            final Field[] fieldsArray = c.getDeclaredFields();
            fields.addAll(Arrays.asList(fieldsArray));
        }

        return fields;
    }

    /**
     * Retourne la comparaison entre 2 booleans. Permet de trier une liste, en
     * indiquant la valeur par défaut du boolean et en indiquant si les valeurs Vrai
     * doivent être en premier dans la liste.
     *
     * @param <T> type générique
     * @param valeur1 Première valeur à comparer.
     * @param valeur2 Seconde valeur à comparer.
     * @param nullEnPremier Valeur par défaut à utiliser en cas de valeur null
     *
     * @return inférieur, égal ou supérieur à 0 selon la règle des méthodes standard
     *         "compareTo".
     */
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T extends Comparable> int compare(T valeur1, T valeur2,
                                                     boolean nullEnPremier) {
        if (valeur1 == null) {
            if (valeur2 == null) {
                return COMPARE_EGALITE;
            } else {
                if (nullEnPremier) {
                    return COMPARE_PLUS_PETIT_EN_PREMIER;
                } else {
                    return COMPARE_PLUS_GRAND_EN_PREMIER;
                }
            }
        } else {
            if (valeur2 == null) {
                if (nullEnPremier) {
                    return COMPARE_PLUS_GRAND_EN_PREMIER;
                } else {
                    return COMPARE_PLUS_PETIT_EN_PREMIER;
                }
            } else {
                return valeur1.compareTo(valeur2);
            }
        }
    }

    /**
     * <p>Retourne l'objet cloné en profondeur (toutes les propriétés de l'objet
     * sont clonées en profondeur).</p>
     *  <p>Ne pas utiliser cette méthode pour des objets clonés en profondeur par
     * l'API, essentiellement des types "Simple" comme java.util.Date, ou pour des
     * objets immuables (Long, Double, String ... dont le clonage est inutile).</p>
     *
     * @param <T> type générique
     * @param o l'objet
     *
     * @return Objet l'objet totalement cloné
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> T clone(T o) {
        Assert.isNotNull("o", o);

        if (isIterableObject(o)) {
            return CollectionUtils.cloneCollection(o);
        } else if (!isDeepClonableObject(o)) {
            throw new RuntimeException("vous devez utiliser directement la méthode clone de l'api. clone()");
        }

        //Recupération de l'ensemble des données membres de l'objet
        final Set<Field> fields = getFields(o);

        try {
            //récupération de la classe de l'objet 
            final Class clazz = o.getClass();

            //récupération du constructeur par defaut
            final Constructor<T> constr = clazz.getDeclaredConstructor();
            //rend le constructeur par défaut accessible (si il est private ou protected)
            constr.setAccessible(true);

            //instanciation de l'objet
            final T newObject = (T) constr.newInstance();

            for (final Field f : fields) {
                //si la donnee membre n'est pas final ni static
                if (!Modifier.isStatic(f.getModifiers()) &&
                        (!Modifier.isFinal(f.getModifiers()))) {
                    //rend la donnée membre accessible (il y a de grande chance qu'elle soit privée)
                    //l'Exception IllegalAccessException ne devrait jamais être levée.
                    f.setAccessible(true);

                    //récupération de l'instance de la donnée membre à cloner 
                    //si un type primitif est récupéré il devient objet via le principe de l'autoboxing
                    final Object objetAcloner = f.get(o);
                    if (objetAcloner != null) {
                        /* si la donnée membre est d'un type clonable (non immuable (String, Integer, Double..))
                         * alors on va tenter de la cloner.
                         * Les types ArrayList, HashSet impémentante Collection sont clonés naturellement mais
                         * en faisant une copie de surface(la référence de la Collection uniquement est clonée), tout comme
                         * les Map
                         */
                        if (isDeepClonableObject(objetAcloner) &&
                                !(objetAcloner instanceof Boolean)) { // Correction provisoire
                                                                      //Visualisation de la méthode clone de l'objet, 
                                                                      //si elle n'existe pas on fait un appel recursif à clone de ObjectUtils
                            try {
                                //                              récupération de la méthode clone de l'objet par réflexion
                                final Method m =
                                    objetAcloner.getClass().getMethod("clone");

                                //invocation de la méthode clone de cet objet
                                final Object oClone = (Object) m.invoke(objetAcloner);
                                //modifie la reference de l'objet complexe présent au sein de l'objet source.
                                f.set(newObject, oClone);
                            } catch (NoSuchMethodException e) {
                                f.set(newObject, clone(objetAcloner));
                            }
                        } else if (isIterableObject(objetAcloner)) {
                            //on modifie l'objet source cloné afin qu'il dispose de l'instance cloné du type itérable. 
                            f.set(newObject, CollectionUtils.cloneCollection(objetAcloner));
                        } else {
                            //l'objet doit être immuable.
                            f.set(newObject, objetAcloner);
                        }
                    }
                }
            }
            return newObject;
        } catch (Exception e) {
            throw new RuntimeException("Erreur durant l'opération de clonage, " +
                                       "vérifiez que tout les objets disposent d'un constructeur par défaut" +
                                       " : " + e.getMessage(), e);
        }
    }

    /**
     * Permet de connaître si l'objet est itérable.
     *
     * @param o l'objet à vérifier.
     *
     * @return true si l'objet est une "collection", false dans le cas contraire.
     */  
    public static Boolean isIterableObject(Object o) {
        return (o instanceof Object[] || o instanceof Collection || o instanceof Map);
    }

    /**
     * Vérification que la nature de l'objet, est un objet clonable en
     * profondeur, et bien évidement non immuable.
     *
     * @param o l'objet à vérifier.
     *
     * @return true si l'objet est dit clonable en profondeur, false dans l'autre cas.
     */
    public static Boolean isDeepClonableObject(Object o) {
        //les objets immuables (ne peuvent être modifiés après création) sont omis
        //le type Double, Integer, Long, BigDecimal sont des objets immuables et héritent tous de number
        return ((o != null) && !(o instanceof Number) && !(o instanceof String) &&
               !(o instanceof Enum) && !(o instanceof Boolean) && !(o instanceof Character) && !isIterableObject(o));
    }

    /**
     * Sérialisation d'un objet.
     *
     * @param object the object
     *
     * @return tableau de byte.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static byte[] storeObject(final Serializable object)
                              throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
            return baos.toByteArray();
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (baos != null) {
                baos.close();
            }
        }
    }

    /**
     * Désérialisation d'un objet.
     *
     * @param bytes the bytes
     *
     * @return object
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException the class not found exception
     */
    public static Object readObject(final byte[] bytes)
                             throws IOException, ClassNotFoundException {
        if (bytes == null) {
            return null;
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        final ObjectInputStream ois = new ObjectInputStream(bais);
        try {
            return ois.readObject();
        } finally {
            if (ois != null) {
                ois.close();
            }
            if (bais != null) {
                bais.close();
            }
        }
    }

    /**
     * Methode : testNullAlorsBlanc.
     *
     * @param obj : valeur a tester.
     *
     * @return : valeur ou blanc.
     */
    public static String testNullAlorsBlanc(final Object obj) {
        return (obj != null) ? obj.toString() : "";
    }

    /**
     * Permet de modifier les données membres d'un objet.
     *
     * @param objet l'objet.
     * @param mapNomChampValeur en clef le nom du champ et en valeur la valeur a
     *        affecter.
     */
    public static void setFieldsFromObject(final Object objet,
                                           final Map<String, Object> mapNomChampValeur) {
        Assert.isNotNull("objet", objet);
        Assert.isNotNull("mapNomChampValeur", mapNomChampValeur);

        final Set<Field> fields = getFields(objet);

        try {
            for (final Field f : fields) {
                final String name = f.getName();
                if (mapNomChampValeur.containsKey(name)) {
                    //l'Exception IllegalAccessException ne devrait jamais être levée.
                    f.setAccessible(true);
                    f.set(objet, mapNomChampValeur.get(name));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur durant l'opération de modification des données membres : " +
                                       e.getMessage());
        }
    }
    
    /**
     * Permet de récupérer une donnée membre d'un objet.
     *
     * @param objet l'objet.
     * @param nameFiled le nom de la donnée membre.
     * @param <T> le type de retour.
     * @return l'objet
     
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldFromObject(final Object objet,
            final String nameFiled) {
        Assert.isNotNull("nameFiled", nameFiled);
        T donneeMembre = null; 

        if (objet != null) {

            final Set<Field> fields = getFields(objet);

            try {
                for (final Field f : fields) {
                    final String name = f.getName();
                    if (StringUtils.equals(nameFiled, name)) {
                        //l'Exception IllegalAccessException ne devrait jamais être levée.
                        f.setAccessible(true);
                        donneeMembre = (T)f.get(objet);
                        break;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Erreur durant l'opération d'accès à la donnée membre : " +
                        nameFiled + " , " +
                        e.getMessage());
            }
        }
        
        return donneeMembre;
    }

    /**
     * Retourne Vrai si l'objet o1 appartient à la liste o2.
     *
     * @param <T> type générique
     * @param o1 l'objet à trouver
     * @param o2 liste dans laquelle rechercher
     *
     * @return Vrai si l'objet o1 appartient à la liste o2.
     */
    public static <T> boolean isIn(T o1, List<T> o2) {
        if (o2 == null) {
            return false;
        }
        return o2.contains(o1);
    }

    /**
     * Retourne Vrai si l'objet o1 appartient à la liste d'objets objets.
     *
     * @param <T> type générique
     * @param o1 l'objet à trouver
     * @param objets la liste d'objet.
     *
     * @return Vrai si l'objet o1 appartient à la liste d'objets.
     */
    public static <T> boolean isIn(T o1, T... objets) {
        return isIn(o1, Arrays.asList(objets));
    }
    
    private static class EnumConverter implements Converter {

        /* (non-Javadoc)
         * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
         */
        @Override
        public Object convert(Class type, Object value) {
            if (value == null) {
                return null;
            }
            if ( !(value instanceof String) ) {
                return value;
            }
            return Enum.valueOf(type, (String) value);
            
        
        }
        
    }
    
    private static class EnumConvertUtils extends ConvertUtilsBean { 

        public EnumConvertUtils() { 
        } 

        /*
        @Override 
        public Object convert(String value, Class clazz) { 
            if(clazz.isEnum()) 
                return Enum.valueOf(clazz, value); 

            return super.convert(value, clazz); 
        } */
        
        @Override
        public Converter lookup(Class clazz) {
            if (clazz.isEnum()) {
                return super.lookup(Enum.class);
            }
            return super.lookup(clazz);
        }
    } 
    
    public static void copyProperties(Object dest, Object source) {
        try {
            IntegerConverter converter = new IntegerConverter(null); 
            DateConverter converterdate = new DateConverter(null);
            EnumConvertUtils enumConvertUtils = new EnumConvertUtils();
            
            BeanUtilsBean beanUtilsBean = new BeanUtilsBean(enumConvertUtils);
            
            beanUtilsBean.getConvertUtils().register(converter, Integer.class);
            beanUtilsBean.getConvertUtils().register(converterdate, Date.class);
            beanUtilsBean.getConvertUtils().register(new EnumConverter(), Enum.class);
           // beanUtilsBean.getConvertUtils().register(new EnumConverter(), Object.class);
            //beanUtilsBean.getConvertUtils().register(new EnumConverter(), VisaDTO.);
            
            beanUtilsBean.copyProperties(dest, source);
        } catch (Exception ex) {
            log.error(ex, "exception");
        }
    }
}
