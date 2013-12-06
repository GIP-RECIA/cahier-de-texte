/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AbstractMetierTest.java,v 1.3 2010/04/19 09:34:08 jerome.carriere Exp $
 */

package org.crlr.test;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.utils.Chrono;
import org.crlr.utils.IOUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Classe de base pour tests unitaires des services métier.
 *
 * @author breytond
 */
public abstract class AbstractMetierTest
    extends AbstractDependencyInjectionSpringContextTests {
    /** Chemin du package de la classe. */
    private static final String PACKAGE_ROOT_PATH =
        '/' + AbstractMetierTest.class.getPackage().getName().replace('.', '/');

    /** log. */
    protected final Log log = LogFactory.getLog(getClass());

    /** jdbcTemplate permettant d'effectuer des requêtes. */
    protected SimpleJdbcTemplate jdbcTemplate;

    /**
     * Variable utilisée par les tests pour demander ou non le temps d'exécution
     * d'un test. Ce temps comprend les différentes assertions effectuées à la suite du
     * service. Cette variable doit rester à la valeur faux.
     */
    protected final boolean useChrono = true;

    /** Chrono. */
    private final Chrono chrono = new Chrono();

/**
     * Constructeur.
     *
     */
    protected AbstractMetierTest() {
        super();
        setAutowireMode(AUTOWIRE_BY_NAME);
    }

    /**
     * Chargement de la configuration Spring.
     *
     * @return le tableau de configuration.
     */
    @Override
    protected String[] getConfigLocations() {
        return new String[] {
                   "classpath:/spring-dao.xml", "classpath:/spring-domain.xml",
                   PACKAGE_ROOT_PATH + "/test-config.xml",
               };
    }

    /**
     * Initialisation de l'instance de la classe de test avant les appels
     * métiers.
     *
     * @throws Exception exception.
     */
    @Override
    protected final void onSetUp() throws Exception {
        // Lancement du chrono si utilisé
        if (useChrono) {
            chrono.start();
        }
    }

    /**
     * Traitement effectué aprés chaque test.
     *
     * @throws Exception exception.
     */
    @Override
    protected final void onTearDown() throws Exception {
        doTearDown();

        jdbcTemplate = null;

        // Arret du chrono si utilisé et affichage du temps passé
        if (useChrono) {
            chrono.stop();
            log.debug("Temps passé dans le service : {0} ms", chrono.getElapsed());
            chrono.reset();
        }
    }

    /**
     * Injection via Spring.
     *
     * @param dataSource le dataSource.
     */
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
    }

    /**
     * Méthode exécutée aprés chaque test, avant l'arrêt du conteneur Spring. Les
     * sous-classes sont invitées à surcharger cette méthode.
     *
     * @throws Exception DOCUMENT ME!
     */
    protected void doTearDown() throws Exception {
       
    }

    /**
     * Vérifie que deux dates sont équivalentes (les dates sont arrondies au
     * jour).
     *
     * @param expected DOCUMENT ME!
     * @param test DOCUMENT ME!
     */
    protected void assertEquals(Date expected, Date test) {
        super.assertEquals(org.apache.commons.lang.time.DateUtils.round(expected, Calendar.DATE),
                org.apache.commons.lang.time.DateUtils.round(test, Calendar.DATE));
    }
    
    /**
     * Vérifie que le {@link ConteneurMessage} associé au {@link ContexteService}
     * contient au moins un {@link Message} avec le code spécifié.
     *
     * @param code code.
     * @param cm le conteneur de message.
     */
    protected void assertMessagePresent(final ConteneurMessage cm, final String code) {
        assertNotNull("Argument code obligatoire", code);

        for (final Message msg : cm) {
            if (code.equals(msg.getCode())) {
                return;
            }
        }
        fail("Aucun Message avec le code '" + code +
             "' présent dans le ConteneurMessage");
    }
    
    /**
     * Afficher le descriptif de la liste.
     * @param <U> type des éléments de la liste.
     * @param liste la liste à afficher.
     */
    protected <U> void afficherListeSousFormeListe(List<U> liste) {
        log.debug("Taille de la liste : {0}", liste.size());

        final StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (int i=0; i<liste.size(); i++) {
            final U obtenu = liste.get(i);
            sb.append(obtenu.getClass().getSimpleName());
            sb.append(" : ");
            final Field[] declaredFields = obtenu.getClass().getDeclaredFields();
            for (Field f : declaredFields) {
                try {
                    f.setAccessible(true);
                    final Object valeur = f.get(obtenu);
                    sb.append(f.getName());
                    sb.append("='");
                    sb.append(valeur);
                    sb.append("',\t");
                } catch (IllegalArgumentException e) {
                    log.error(e, "Erreur à la récupération de l'attribut");
                } catch (IllegalAccessException e) {
                    log.error(e, "Erreur à la récupération de l'attribut");
                }
            }
            sb.append("\n");
        }
        
        sb.append("Taille de la liste : ").append(liste.size());

        log.debug(sb.toString());
    }
    
    /**
     * Afficher la liste des objets.
     * @param <U> Type des objets de la liste.
     * @param <T> Type classe.
     * @param liste La liste à afficher
     */
    @SuppressWarnings("unchecked")
    public <U,T> void afficherListe(List<U> liste) {
        if (liste == null) {
            log.debug("\nListe NULL");
            return ;
        }
        if (liste.isEmpty()) {
            log.debug("\nListe Vide");
            return;
        }
        final int tailleLimite = 50;

        final int tailleListe;
        if (tailleLimite > 0) {
            tailleListe = Math.min(liste.size(), tailleLimite);
        } /*else {
            tailleListe = liste.size();
        }*/

        // Lecture des données
        final Map<Class<T>, Map<Field, String[]>> donnees = new HashMap<Class<T>, Map<Field, String[]>>();
        final Map<Integer, String> nomClasseParIndice = new HashMap<Integer, String>();
        for (int i = 0; i < tailleListe; i++) {
            final U o = liste.get(i);
            final Class<? extends Object> classeObjet = o.getClass();
            final Collection<Field> fields;
            final Map<Field, String[]> donneesClasse;
            if (donnees.containsKey(classeObjet)) {
                donneesClasse = donnees.get(classeObjet);
                fields = donneesClasse.keySet();
            } else {
                final List<Field> fieldsTemp = Arrays.asList(classeObjet.getDeclaredFields());
                donneesClasse = new HashMap<Field, String[]>();
                for (Field field : fieldsTemp) {
                    if (! "serialVersionUID".equals(field.getName())) {
                        field.setAccessible(true);
                        donneesClasse.put(field, new String[tailleListe]);
                    }
                }
                donnees.put((Class<T>)classeObjet, donneesClasse);
                fields = donneesClasse.keySet();
            }
            nomClasseParIndice.put(i, classeObjet.getSimpleName());
            for (Field field : fields) {
                try {
                    final Object valeur = field.get(o);
                    final String valeurString;
                    if (valeur == null) {
                        valeurString = "NULL";
                    } else {
                        valeurString = valeur.toString();
                    }
                    donneesClasse.get(field)[i] = valeurString;
                } catch (IllegalArgumentException e) {
                    log.error(e, "afficherListe");
                } catch (IllegalAccessException e) {
                   log.error(e, "afficherListe");
                }
            }
        }

        // Calcul des largeur de colonnes
        final Map<Integer, Integer> tailleColonnes = new HashMap<Integer, Integer>();
        final Map<Object, Integer> infosNumeroColonne = new HashMap<Object, Integer>();

        int maxLengthClasse = 0;
        for (String nomClasse : nomClasseParIndice.values()) {
            maxLengthClasse = Math.max(maxLengthClasse, nomClasse.length());
        }
        final int indiceColonneClasse = infosNumeroColonne.size();
        infosNumeroColonne.put("NomClasse", indiceColonneClasse);
        tailleColonnes.put(indiceColonneClasse, maxLengthClasse + 1);

        for (Entry<Class<T>, Map<Field, String[]>> donneesClasse : donnees.entrySet()) {
            for (Entry<Field, String[]> donneesChamp : donneesClasse.getValue()
                    .entrySet()) {
                final Field field = donneesChamp.getKey();
                int maxLength = field.getName().length();
                for (String valeur : donneesChamp.getValue()) {
                    if (valeur != null) {
                        maxLength = Math.max(maxLength, valeur.length());
                    }
                }
                final int indiceColonne = infosNumeroColonne.size();
                infosNumeroColonne.put(field, indiceColonne);
                tailleColonnes.put(indiceColonne, maxLength + 1);
            }
        }
        final int nbColonnes = infosNumeroColonne.size();

        // Initialisation de la structure en lignes
        final List<String[]> donneesLignes = new ArrayList<String[]>(tailleListe + 1);
        for (int i = 0; i < tailleListe + 1; i++) {
            donneesLignes.add(new String[nbColonnes]);
        }

        // Remplissage de la structure en lignes
        donneesLignes.get(0)[0] = "Classe";
        for (int i = 0; i < tailleListe; i++) {
            donneesLignes.get(i + 1)[0] = nomClasseParIndice.get(i);
        }
        for (Entry<Class<T>, Map<Field, String[]>> donneesClasse : donnees.entrySet()) {
            for (Entry<Field, String[]> donneesChamp : donneesClasse.getValue().entrySet()) {
                final Field field = donneesChamp.getKey();
                final Integer numeroColonne = infosNumeroColonne.get(field);
                final String[] donneesColonnes = donneesChamp.getValue();
                donneesLignes.get(0)[numeroColonne] = field.getName();
                for (int i = 0; i < tailleListe; i++) {
                    donneesLignes.get(i + 1)[numeroColonne] = donneesColonnes[i];
                }
            }
        }

        // Ecriture du résultat
        String vide = "                                                            ";
        vide = vide + vide + vide + vide + vide + vide + vide + vide + vide + vide
        + vide + vide + vide + vide + vide + vide;
        String tirets = "----------------------------------------------------------";
        tirets = tirets + tirets + tirets + tirets + tirets + tirets + tirets + tirets
        + tirets + tirets + tirets + tirets + tirets + tirets + tirets + tirets;
        final int largeurIndice = String.valueOf(tailleListe).length();
        final StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (int indiceLigne = 0; indiceLigne < donneesLignes.size(); indiceLigne++) {
            final String[] donneesLigne = donneesLignes.get(indiceLigne);
            if (indiceLigne == 0) {
                sb.append(vide, 0, largeurIndice + 1);
                sb.append("#");
            } else {
                sb.append(vide, String.valueOf(indiceLigne - 1).length(), largeurIndice);
                sb.append(indiceLigne - 1);
                sb.append(" #");
            }
            for (int indiceColonne = 0; indiceColonne < nbColonnes; indiceColonne++) {
                final String valeur = donneesLigne[indiceColonne];
                sb.append(" ");
                if (valeur != null) {
                    try{
                        sb.append(valeur);
                        if (tailleColonnes.get(indiceColonne) > valeur.length()) {
                            sb.append(vide, valeur.length(), tailleColonnes.get(indiceColonne));
                        }
                    }catch(Exception e){
                        sb.append("ERREUR");
                    }
                } else {
                    sb.append(vide, 0, tailleColonnes.get(indiceColonne));
                }
                sb.append("|");
            }
            sb.append("\n");
            if (indiceLigne == 0) {
                sb.append(tirets, 0, largeurIndice + 1);
                sb.append("#");
                for (int indiceColonne = 0; indiceColonne < nbColonnes; indiceColonne++) {
                    try{
                    sb.append(tirets, 0, tailleColonnes.get(indiceColonne) + 1);
                    } catch(Exception e){
                        sb.append("[*]");
                    }
                    sb.append("|");
                }
                sb.append("\n");
            }
        }
        sb.append("Taille de la liste : ").append(liste.size());

       log.debug(sb.toString());
    }

    /**
     * Affiche le détail d'un objet.
     *
     * @param <U> Type des objets de la liste.
     * @param objet Objet à afficher
     */
    @SuppressWarnings("rawtypes")
    public <U> void afficherObjet(U objet) {
        if (objet == null) {
            log.debug("\nObjet NULL");
            return;
        }
    
        final Class<?extends Object> classeObjet = objet.getClass();
        final Collection<Field> fields = new ArrayList<Field>();
        final List<Field> fieldsTemp = Arrays.asList(classeObjet.getDeclaredFields());
        for (Field field : fieldsTemp) {
            if (!"serialVersionUID".equals(field.getName())) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(objet.getClass().getSimpleName());
        sb.append("\n");
    
        for (Field field : fields) {
            try {
                final Object valeur = field.get(objet);
                final String valeurString;
                if (valeur == null) {
                    valeurString = "NULL";
                } else {
                    
                    if (valeur instanceof Collection) {
                        valeurString = "[Collection (" + valeur.getClass().getSimpleName() 
                        + ") de taille " + ((Collection) valeur).size() +  "]";
                    } else {
                        valeurString = valeur.toString();
                    }
                }
                sb.append("- ");
                sb.append(field.getName());
                sb.append(" : ");
                sb.append(valeurString);
                sb.append("\n");
            } catch (IllegalArgumentException e) {
               log.error(e, "afficherObjet");
            } catch (IllegalAccessException e) {
                log.error(e, "afficherObjet");
            }
        }
        log.debug(sb.toString());
    }

    /**
     * Afficher la liste des messages contenus dans le conteneur de messages.
     * @param messages liste des messages.
     */
    protected void afficherMessages(final Set<Message> messages) {
       final String vide = "                          ";
    
        final StringBuilder sb = new StringBuilder();
        if (messages.size() == 0) {
            sb.append("\nConteneur de messages vide\n");
        } else {
            sb.append("\nConteneur de messages\n  # Code      | Nature        |\n--#-----------|---------------|\n");
            int i = 0;
            for (Message message : messages) {
                sb.append(i);
                sb.append(" # ");
                sb.append(message.getCode());
                sb.append(" | ");
                sb.append(message.getNature().name());
                sb.append(vide, message.getNature().name().length(), 13);
                sb.append(" | ");
                sb.append("\n");
                ++i;
            }
        }
       log.debug(sb.toString());        
    }
    
    /**
     * Copie le report.
     * @param data les données.
     * @param file le fichier.
     * @throws IOException l'exception potentielle.
     */
    protected void dump(byte[] data, File file) throws IOException {
        final ReadableByteChannel buffer =
            Channels.newChannel(new ByteArrayInputStream(data));
        final FileChannel chan = new FileOutputStream(file).getChannel();
        try {
            long bytesWritten = 0;
            while (bytesWritten != data.length) {
                bytesWritten += chan.transferFrom(buffer, bytesWritten, 1024);
            }
        } finally {
            IOUtils.close(chan);
        }
    }
}
