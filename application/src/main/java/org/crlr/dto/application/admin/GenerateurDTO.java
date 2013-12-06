/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: GenerateurDTO.java,v 1.6 2010/04/28 16:12:21 jerome.carriere Exp $
 */

package org.crlr.dto.application.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.GenericDTO;
import org.crlr.dto.application.base.GenericTroisDTO;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.emploi.SemaineDTO;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.web.dto.BarreMoisDTO;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.dto.CalendrierVancanceDTO;
import org.crlr.web.dto.GrilleHoraireDTO;
import org.crlr.web.dto.MoisDTO;
import org.crlr.web.dto.TypeCouleur;
import org.crlr.web.dto.TypeCouleurJour;
import org.crlr.web.dto.TypeSemaine;

/**
 * GenerateurDTO.
 * 
 * @author breytond.
 * @version $Revision: 1.6 $
 */
public final class GenerateurDTO {

    private final static Map<String, String> MAP_LIBELLE_JOUR;

    private final static Map<TypeSemaine, String> MAP_ALTERNANCE_COULEUR;

    protected static final Log log = LogFactory.getLog(GenerateurDTO.class);

    static {
        MAP_LIBELLE_JOUR = new LinkedHashMap<String, String>();
        for (final TypeJour typeJour : TypeJour.values()) {
            MAP_LIBELLE_JOUR.put(typeJour.name(), typeJour.name().toLowerCase()
                    .substring(0, 3)
                    + ".");
        }

        MAP_ALTERNANCE_COULEUR = new HashMap<TypeSemaine, String>();
        MAP_ALTERNANCE_COULEUR.put(TypeSemaine.IMPAIR, TypeCouleurJour.IMPAIR
                .getId());
        MAP_ALTERNANCE_COULEUR.put(TypeSemaine.PAIR, TypeCouleurJour.PAIR
                .getId());
        MAP_ALTERNANCE_COULEUR.put(TypeSemaine.VAQUE, TypeCouleurJour.VAQUE
                .getId());
        MAP_ALTERNANCE_COULEUR.put(TypeSemaine.NEUTRE, TypeCouleurJour.SILVER
                .getId());
    }

    /**
     * Constructeur.
     */
    private GenerateurDTO() {
    }

    /**
     * Constitution d'un Set de date représentant toutes les journées chômées.
     * 
     * @param periodeVancance
     *            les périodes.
     * @return la liste.
     */
    public static final Set<Date> generateSetPeriodeVancanceFromDb(
            final String periodeVancance) {
        final Set<Date> jourFerie = new HashSet<Date>();
        if (!org.apache.commons.lang.StringUtils.isEmpty(periodeVancance)) {
            final String[] listePeriode = StringUtils.split(
                    periodeVancance, "\\|");
            
                for (final String periode : listePeriode) {
                    final String[] listeDate = StringUtils.split(periode,
                            ":");
                    if (listeDate.length == 2) {
                        Date dateDebut = DateUtils.parse(listeDate[0]);
                        final Date dateFin = DateUtils.parse(listeDate[1]);
                        while (!DateUtils.equalsDate(dateDebut, dateFin)) {
                            jourFerie.add(dateDebut);
                            dateDebut = DateUtils.getDatePlus1(dateDebut);
                        }
                        jourFerie.add(dateFin);
                    }
                }
            
        }

        return jourFerie;
    }

    /**
     * Constitution d'une liste de période de date.
     * 
     * @param periodeVancance
     *            les périodes.
     * @return la liste.
     */
    
    public static final List<GenericDTO<Date, Date>> generateListePeriodeVancanceFromDb(
            final String periodeVancance) {
        final List<GenericDTO<Date, Date>> liste = new ArrayList<GenericDTO<Date, Date>>();

        // on diminut d'un jour la date de début puisque le début des
        // vacances est effectif aprés les cours du jour mentionné.
        // on augmente d'une journée la date de fin puisque la date de fin
        // indique la reprise des cours
        final String[] listePeriode = StringUtils.split(
                StringUtils.trimToEmpty(periodeVancance), "\\|");
        for (final String periode : listePeriode) {
            final String[] listeDate = StringUtils.split(periode, ":");
            if (listeDate.length == 2) {
                liste.add(new GenericDTO<Date, Date>(DateUtils
                        .getDateMoins1(DateUtils.parse(listeDate[0])),
                        DateUtils.getDatePlus1(DateUtils.parse(listeDate[1]))));
            }
        }

        return liste;
    }

    /**
     * Constitution de la chaine de période de vacance à persister.
     * 
     * @param listePeriode
     *            la liste de période.
     * @return la chaîne représentant cette liste.
     */
    public static final String generatePeriodeVacanceToDb(
            final List<GenericDTO<Date, Date>> listePeriode) {
        String chainePeriodeEnBase = "";
        // on augmente d'un jour la date de début puisque le début des vacances
        // est effectif aprés les cours du jour mentionné.
        // on réduit d'une journée la date de fin puisque la date de fin indique
        // la reprise des cours
        for (final GenericDTO<Date, Date> periodeDTO : listePeriode) {
            chainePeriodeEnBase += DateUtils.format(DateUtils
                    .getDatePlus1(periodeDTO.getValeur1()))
                    + ":"
                    + DateUtils.format(DateUtils.getDateMoins1(periodeDTO
                            .getValeur2())) + "|";
        }

        return chainePeriodeEnBase;
    }

    /**
     * Constitution de la barre de jours ouvrés ou vaquant.
     * 
     * @param joursFeries
     *            la liste des jours vaqués sous forme de chaîne.
     * @return la liste des jours ouvrés ou vaqués.
     */
    public static final List<GenericTroisDTO<String, String, Boolean>> generateListeJourOuvreFerieFromDb(
            final String joursFeries) {
        final List<GenericTroisDTO<String, String, Boolean>> liste = new ArrayList<GenericTroisDTO<String, String, Boolean>>();

        final Set<String> listeJourFerie = org.apache.commons.lang.StringUtils.isEmpty(joursFeries) ? new HashSet<String>()
                : new HashSet<String>(Arrays.asList(StringUtils.split(joursFeries, "\\|")));

        for (final Entry<String, String> elementJour : MAP_LIBELLE_JOUR
                .entrySet()) {
            liste.add(new GenericTroisDTO<String, String, Boolean>(elementJour
                    .getKey(), elementJour.getValue(), listeJourFerie
                    .contains(elementJour.getKey())));
        }

        return liste;
    }

    /**
     * Constitution de la map des jours ouvrés.
     * 
     * @param joursOuvres
     *            la liste des jours ouvrés sous forme de chaîne.
     * @return la map des jours ouvrés.
     */
    public static final Map<TypeJour, Boolean> generateMapJourOuvreFromDb(
            final String joursOuvres) {
        final Map<TypeJour, Boolean> mapResultat = new HashMap<TypeJour, Boolean>();

        if (!org.apache.commons.lang.StringUtils.isEmpty(joursOuvres)) {
            final List<String> liste = Arrays.asList(StringUtils.split(joursOuvres, "\\|"));
            for (final TypeJour typeJour : TypeJour.values()) {
                mapResultat.put(typeJour, liste.contains(typeJour.name()));
            }
        }

        return mapResultat;
    }

    /**
     * @param joursOuvres la chaine qui vient de la BDD.
     * @return la liste des jours ouvrés 
     */
    public static final List<TypeJour> getListeJourOuvreFromDb(
            final String joursOuvres) {
        final List<TypeJour> listeResultat = new ArrayList<TypeJour>();

        if (org.apache.commons.lang.StringUtils.isEmpty(joursOuvres)) {
            return listeResultat;
        }

        final String[] liste = StringUtils.split(joursOuvres, "\\|");
        for (final String strJour : liste) {
            try {
                listeResultat.add(TypeJour.valueOf(strJour));
            } catch (Exception ex) {
                log.error("jour pas valid {0}", strJour);
            }
        }

        return listeResultat;
    }

    /**
     * Constitution de la chaine de jours ouvrés à persister.
     * 
     * @param listeJour
     *            la liste des jours ouvrés ou vaqués.
     * @return la chaîne représentant cette liste.
     */
    public static final String generateJourOuvreToDb(
            final List<GenericTroisDTO<String, String, Boolean>> listeJour) {
        String chaineJourEnBase = "";
        for (final GenericTroisDTO<String, String, Boolean> jourDTO : listeJour) {
            if (jourDTO.getValeur3()) {
                chaineJourEnBase += jourDTO.getValeur1() + "|";
            }
        }

        return chaineJourEnBase;
    }
    
    /**
     * @param listeGrilleHoraire la liste.
     * @return une nouvelle liste
     */
    public static List<GrilleHoraireDTO> trierGrilleHoraire(final List<GrilleHoraireDTO> listeGrilleHoraire) {
        List<GrilleHoraireDTO> listeTriee = new ArrayList<GrilleHoraireDTO>(listeGrilleHoraire);
        // Trier
        Collections.sort(listeTriee,
            new Comparator<GrilleHoraireDTO>() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see java.util.Comparator#compare(java.lang.Object,
                 * java.lang.Object)
                 */
                @Override
                public int compare(GrilleHoraireDTO o1, GrilleHoraireDTO o2) {
                    Integer o1Min = o1.getHeureDebut() * 60
                            + o1.getMinuteDebut();

                    return o1Min.compareTo((o2.getHeureDebut() * 60 + o2
                            .getMinuteDebut()));
                }
            });
        
        return listeTriee;
    }
    
    /**
     * Mettre le titre Plage X dans la grille horaire.
     * @param listeGrilleHoraire la liste
     */
    public static void mettreTitreGrilleHoraire(final List<GrilleHoraireDTO> listeGrilleHoraire) {
        int i = 0;

        for (final GrilleHoraireDTO grilleHoraireDTO : listeGrilleHoraire) {
            i++;
            grilleHoraireDTO.setTitre("Plage " + i);
        }
    }

    /**
     * Constitution de la chaine de grille horaire à persister.
     * 
     * @param listeGrilleHoraire
     *            la liste des plages horaires.
     * @return la chaîne représentant cette liste.
     */
    public static final String generateGrilleHoraireToDb(
            final List<GrilleHoraireDTO> listeGrilleHoraire) {

        final List<GrilleHoraireDTO> listeTriee = trierGrilleHoraire(listeGrilleHoraire);
        
        StringBuffer chaineGrilleEnBase = new StringBuffer();
        for (final GrilleHoraireDTO gHDTO : listeTriee) {
            chaineGrilleEnBase
                    .append(gHDTO.getHeureDebut())
                    .append(":")
                    .append(gHDTO.getMinuteDebut())
                    .append("#")
                    .append(gHDTO.getHeureFin())
                    .append(":")
                    .append(gHDTO.getMinuteFin())
                    .append("|");
        }

        return chaineGrilleEnBase.toString();
    }

    /**
     * Constitution de la liste de plage horaire.
     * 
     * @param horairesCours
     *            les plages horaires sous forme d'une chaîne.
     * @return la liste.
     */
    public static final List<GrilleHoraireDTO> generateGrilleHoraireFromDb(
            final String horairesCours) {
        final List<GrilleHoraireDTO> liste = new ArrayList<GrilleHoraireDTO>();
        
        //9:0#10:0|10:0#11:0|11:15#12:15|14:0#15:0|15:0#16:0|
        
        //Valider la chaine
        final String regex = "(\\d+:\\d+#\\d+:\\d+\\|?)+";
        
        if (horairesCours==null || !horairesCours.matches(regex)) {
            log.error("Grille horaire chaine {0} n'est pas dans le format h:m#h:m|h:m#h:m|h:m#h:m|...", horairesCours);
            return liste;
        }

        if (org.apache.commons.lang.StringUtils.isEmpty(horairesCours)) {
            return liste;
        }

        final String[] listePlage = StringUtils.split(StringUtils.trimToEmpty(horairesCours), "\\|");


        for (final String plage : listePlage) {
            final List<String> listeHoraire = Arrays.asList(StringUtils.split(plage, "#"));
            
            if (listeHoraire.size() != 2) {
                continue;
            }
            
            final GrilleHoraireDTO grilleHoraireDTO = new GrilleHoraireDTO();
            final List<String> horaireDebut = Arrays.asList(StringUtils.split(
                    listeHoraire.get(0), ":"));
            final List<String> horaireFin = Arrays.asList(StringUtils.split(
                    listeHoraire.get(1), ":"));

            grilleHoraireDTO.setHeureDebut(Integer.parseInt(horaireDebut
                    .get(0)));
            grilleHoraireDTO.setMinuteDebut(Integer.parseInt(horaireDebut
                    .get(1)));
            grilleHoraireDTO
                    .setHeureFin(Integer.parseInt(horaireFin.get(0)));
            grilleHoraireDTO.setMinuteFin(Integer.parseInt(horaireFin
                    .get(1)));
            liste.add(grilleHoraireDTO);            
        }

        mettreTitreGrilleHoraire(liste);
        return liste;
    }

    /**
     * Recherche du type de semaine (1 ou 2) vis-à-vis d'une date donnée.
     * 
     * @param date
     *            le dimanche de la semaine.
     * @param alternanceSemaine
     *            l'alternanace des semaines.
     * @return 1 ou 2
     */
    public static final TypeSemaine findTypeSemaineParDate(final Date date,
            final String alternanceSemaine) {
        if (date != null) {
            final Map<Integer, TypeSemaine> map = generateAlternanceSemaineFromDB(alternanceSemaine);
            final Integer numeroSemaine = DateUtils.getChamp(date,
                    Calendar.WEEK_OF_YEAR);
            final TypeSemaine typeJourEmploi = map.get(numeroSemaine);
            if (typeJourEmploi != null) {
                return typeJourEmploi;
            }
        }
        return TypeSemaine.NEUTRE;
    }

 

    /**
     * Génére une barre de couleur.
     * 
     * @return la liste.
     */
    public static final List<TypeCouleur> generateBarreCouleur() {
        final List<TypeCouleur> liste = new ArrayList<TypeCouleur>();

        liste.addAll(Arrays.asList(TypeCouleur.values()));

        return liste;
    }

   

    /**
     * Constitution de la chaine d'alternance des semaines paires et impaires à
     * persister.
     * 
     * @param listeBarreSemaine
     *            la liste des alternances.
     * @return la chaîne représentant cette liste.
     */
    public static final String generateAlternanceSemaineToDb(
            final List<BarreSemaineDTO> listeBarreSemaine) {
        String chaineAlternanceEnBase = "";
        for (final BarreSemaineDTO barreDTO : listeBarreSemaine) {
            final TypeSemaine typeJourEmploi = barreDTO.getTypeJourEmploi();
            if (!TypeSemaine.VAQUE.equals(typeJourEmploi)
                    && !TypeSemaine.NEUTRE.equals(typeJourEmploi)) {
                chaineAlternanceEnBase += barreDTO.getNumeroSemaine() + ":"
                        + barreDTO.getTypeJourEmploi().name() + "|";
            }
        }

        return chaineAlternanceEnBase;
    }

    /**
     * Accesseur mAP_ALTERNANCE_COULEUR.
     * 
     * @return le mAP_ALTERNANCE_COULEUR
     */
    public static Map<TypeSemaine, String> getMapAlternanceCouleur() {
        return MAP_ALTERNANCE_COULEUR;
    }
    
    
    /**
     * Génére le calendrier des jours vaqués.
     * @param dateRentree la date de rentrée de l'année scolaire.
     * @param dateSortie la date de sortie.
     * @param premierJour le premier jour de la seamine.
     * @param jourOuvre la liste des jours ouvrés dans une semaine.
     * @param jourFerie la liste des jours vaqués annuelle.
     * @return le calendrier.
     */
    public static  List<CalendrierVancanceDTO> genererVacanceCalendar(final Date dateRentree, final Date dateSortie, 
            final Date premierJour, final Set<String> jourOuvre, final Set<Date> jourFerie) {
        final List<CalendrierVancanceDTO> liste = new ArrayList<CalendrierVancanceDTO>();

        final Integer moisAffiche = DateUtils.getChamp(premierJour, Calendar.MONTH);

        Date debutSemaine = null;

        for (int i=0 ; i<6; i++) {
            if (i==0) {
                debutSemaine = premierJour;
                final Date finSemaine = DateUtils.setJourSemaine(premierJour, Calendar.SUNDAY);
                liste.add(genererCalendrierVacanceDTO(debutSemaine, finSemaine, dateRentree, 
                        dateSortie, jourOuvre, jourFerie, moisAffiche));                
            } else {                
                final Date finSemaine = DateUtils.setJourSemaine(debutSemaine, Calendar.SUNDAY);
                liste.add(genererCalendrierVacanceDTO(debutSemaine, finSemaine, dateRentree, dateSortie, jourOuvre, jourFerie, moisAffiche));   
            }

            debutSemaine = DateUtils.getSemaineSuivante(debutSemaine);

            //gestion de la fin du mois
            if (!moisAffiche.equals(DateUtils.getChamp(debutSemaine, Calendar.MONTH))) {
                break;
            }
        }        

        return liste;
    }

    /**
     * Génére une ligne du calendrier des jours vaqués.
     * @param debutSemaine la date du début de la semaine.
     * @param finSemaine la date de fin de la seamine.
     * @param dateRentree la date de la rentrée scolaire.
     * @param dateSortie la date fin.
     * @param jourOuvre la liste des jours ouvrés
     * @param jourFerie la liste des jours vaqués.
     * @param moisAffiche le mois affiché.
     * @return la ligne du calendrier.
     */
    private static CalendrierVancanceDTO genererCalendrierVacanceDTO(final Date debutSemaine, final Date finSemaine, 
            final Date dateRentree, final Date dateSortie, final Set<String> jourOuvre, final Set<Date> jourFerie, final Integer moisAffiche) {
        final CalendrierVancanceDTO calendrierVancanceDTO = new CalendrierVancanceDTO();  
        
        Date dateDepart = (Date)debutSemaine.clone();
        
        //Est ce que la semaine est bien comprise dans l'année scolaire
        if (dateDepart.before(dateRentree) || finSemaine.after(dateSortie)) {
            if (!finSemaine.after(dateRentree)) {
                return calendrierVancanceDTO;
            } else {
                //la semaine valide ne fait pas 7 jours
                dateDepart = (Date)dateRentree.clone();
            }
        }
        

        final Date finSemaineEffective = DateUtils.getDatePlus1(finSemaine);

        while (dateDepart.before(finSemaineEffective)) {
            //gestion de la dernière semaine du mois qui peut chevaucher sur le mois d'après 
            if (!moisAffiche.equals(DateUtils.getChamp(dateDepart, Calendar.MONTH))) {
                break;
            }

            switch (DateUtils.getChamp(dateDepart, Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:                
                calendrierVancanceDTO.setJourLundi(dateDepart);
                calendrierVancanceDTO.setCouleurLundi(genererCouleurJour(TypeJour.LUNDI, dateDepart, jourOuvre, jourFerie));
                break;
            case Calendar.TUESDAY:
                calendrierVancanceDTO.setJourMardi(dateDepart);
                calendrierVancanceDTO.setCouleurMardi(genererCouleurJour(TypeJour.MARDI, dateDepart, jourOuvre, jourFerie));
                break;
            case Calendar.WEDNESDAY:
                calendrierVancanceDTO.setJourMercredi(dateDepart);
                calendrierVancanceDTO.setCouleurMercredi(genererCouleurJour(TypeJour.MERCREDI, dateDepart, jourOuvre, jourFerie));
                break;
            case Calendar.THURSDAY:
                calendrierVancanceDTO.setJourJeudi(dateDepart);
                calendrierVancanceDTO.setCouleurJeudi(genererCouleurJour(TypeJour.JEUDI, dateDepart, jourOuvre, jourFerie));
                break;
            case Calendar.FRIDAY:
                calendrierVancanceDTO.setJourVendredi(dateDepart);
                calendrierVancanceDTO.setCouleurVendredi(genererCouleurJour(TypeJour.VENDREDI, dateDepart, jourOuvre, jourFerie));
                break;
            case Calendar.SATURDAY:
                calendrierVancanceDTO.setJourSamedi(dateDepart);
                calendrierVancanceDTO.setCouleurSamedi(genererCouleurJour(TypeJour.SAMEDI, dateDepart, jourOuvre, jourFerie));
                break;
            case Calendar.SUNDAY:
                calendrierVancanceDTO.setJourDimanche(dateDepart);
                calendrierVancanceDTO.setCouleurDimanche(genererCouleurJour(TypeJour.DIMANCHE, dateDepart, jourOuvre, jourFerie));
                break;
            default:
                break;            
            }
            dateDepart = DateUtils.getDatePlus1(dateDepart);
        }

        return calendrierVancanceDTO;
    }

    /**
     * Positionne la couleur.
     * @param typeJour le type de jour.
     * @param jour la date.
     * @param jourOuvre la liste des jours ouvrés de la semaine.
     * @param jourFerie la liste annuelle des jours fériés ou vaqués.
     * @return la couleur.
     */
    private static String genererCouleurJour (final TypeJour typeJour, final Date jour, 
            final Set<String> jourOuvre, final Set<Date> jourFerie) {
        String couleur = null;
        //gestion des couleurs des jours ouvres
        couleur = (!jourOuvre.contains(typeJour.name())) ? TypeCouleurJour.SILVER.getId() : null;
        //gestion des jours fériés
        couleur = (jourFerie.contains(jour)) ? TypeCouleurJour.RED.getId() : couleur;

        return couleur;
    }
    
    
    /**
     * Génére une barre de navigation des mois de l'année scolaire.
     * @param dateEntree la date de départ de l'année.
     * @param dateSortie la date de fin.
     * @param dateSelectionne la date sélectionnée.
     * @return la liste
     */
    public static List<GenericTroisDTO<String, String, Boolean>> genererBarreMois(final Date dateEntree, 
            final Date dateSortie, final String dateSelectionne) {        
        final List<GenericTroisDTO<String, String, Boolean>> listeMois = new ArrayList<GenericTroisDTO<String, String, Boolean>>();
        
        String departMoisAnnee =  String.valueOf(DateUtils.getChamp(dateEntree, Calendar.MONTH)) + 
        DateUtils.getYear(dateEntree).toString();
        
        final String sortiMoisAnnee = String.valueOf(DateUtils.getChamp(dateSortie, Calendar.MONTH) + 1) + 
            DateUtils.getYear(dateSortie).toString();
        
        Date dateMoisCourante = DateUtils.creer(DateUtils.getChamp(dateEntree, Calendar.YEAR), 
                DateUtils.getChamp(dateEntree, Calendar.MONTH) , 1);
        
        while (!departMoisAnnee.equals(sortiMoisAnnee)) {
            
            final String dateCouranteChaine = DateUtils.format(dateMoisCourante);
            
            final GenericTroisDTO<String, String, Boolean> genericDTO = 
                new GenericTroisDTO<String, String, Boolean>(DateUtils.format(dateMoisCourante, "MMMMM"), 
                        dateCouranteChaine, StringUtils.equals(dateSelectionne, dateCouranteChaine));
            
            listeMois.add(genericDTO);
            
            //incremente le mois
            dateMoisCourante = DateUtils.ajouter(dateMoisCourante, Calendar.MONTH, 1);
            
            departMoisAnnee =  String.valueOf(DateUtils.getChamp(dateMoisCourante, Calendar.MONTH)) + 
            DateUtils.getYear(dateMoisCourante).toString();
            
            
        }
        
        return listeMois;
    }
    
    /**
     * Génère la liste des mois qui se place sous la barre des semaines. 
     * Le mois courant est distingué par une couleur spécifique.
     * @param dateEntree la date de départ de l'année.
     * @param dateSortie la date de fin.
     * @return la liste des mois, objets utilisés par le composant cr:barreMois
     */
    public static List<MoisDTO> genererListeMois(final Date dateEntree, final Date dateSortie) {        
        final List<MoisDTO> listeMois = new ArrayList<MoisDTO>();
        
        String departMoisAnnee =  String.valueOf(DateUtils.getChamp(dateEntree, Calendar.MONTH)) + 
        DateUtils.getYear(dateEntree).toString();
        
        final String sortiMoisAnnee = String.valueOf(DateUtils.getChamp(dateSortie, Calendar.MONTH) + 1) + 
            DateUtils.getYear(dateSortie).toString();
        
        //Date dateMoisCourante = DateUtils.creer(DateUtils.getChamp(dateEntree, Calendar.YEAR), 
        //        DateUtils.getChamp(dateEntree, Calendar.MONTH) , 1);
        Date dateMoisCourante = DateUtils.setJourSemaine(dateEntree,2);
        
        // Calcule le premier decalage
        final Float decalageDebutAnnee =  MoisDTO.getDecalageDebutAnnee(dateMoisCourante);
        final MoisDTO moisDecalage = new MoisDTO(decalageDebutAnnee);
        
        listeMois.add(moisDecalage);
        Float nbrTotalSemaine = decalageDebutAnnee;
        // Boucle sur les mois de l'annee scolaire
        while (!departMoisAnnee.equals(sortiMoisAnnee)) {
            
            final MoisDTO moisDTO = new MoisDTO(dateMoisCourante);
            listeMois.add(moisDTO);
            
            //incremente le mois
            Date date;
            final Calendar cal = new GregorianCalendar();
            cal.setTime(dateMoisCourante);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            date = cal.getTime();
            dateMoisCourante = DateUtils.ajouter(date, Calendar.MONTH, 1);
            
            departMoisAnnee =  String.valueOf(DateUtils.getChamp(dateMoisCourante, Calendar.MONTH)) + 
            DateUtils.getYear(dateMoisCourante).toString();
            
            // Gestion particuliere pour le dernier mois : il ne comporte pas forcement le meme nombre de semaines que le calendrier
            if (departMoisAnnee.equals(sortiMoisAnnee)) {
                Date finSemaine = DateUtils.setJourSemaine(dateSortie, Calendar.SUNDAY);
                moisDTO.setFin(finSemaine);
            }
            
            nbrTotalSemaine += moisDTO.getNbrSemaine();
        }
        
        Float cumul = 0.0f;
        for (final MoisDTO mois : listeMois) {
            mois.setPositionLeft(cumul);
            mois.setPositionRight(cumul + (mois.getNbrSemaine() * 100) / nbrTotalSemaine );
            
            cumul = mois.getPositionRight();
        }
        
        return listeMois;
    }
    
    /**
     * @param listeBarreSemaine la liste
     * @return la semaine qui contient la date d'aujourd'hui
     */
    public static BarreSemaineDTO selectionnerSemaineJour(List<BarreSemaineDTO> listeBarreSemaine) {
        final Date today = DateUtils.getAujourdhui();
        final Date lundiToday = DateUtils.setJourSemaine(today, Calendar.MONDAY);
        
        BarreSemaineDTO semaineSelectionnee = null;
        
        if (CollectionUtils.isEmpty(listeBarreSemaine)) {
            log.error("Liste barre semaine est vide");
            return new BarreSemaineDTO();
        }
        
        for(final BarreSemaineDTO semaine : listeBarreSemaine) {
            if (semaine.getLundi().equals(lundiToday)) {
                semaine.setVraiOuFauxSelection(true);
                semaineSelectionnee = semaine;
            } else {
                semaine.setVraiOuFauxSelection(false);
            }
        }
        
        if (null == semaineSelectionnee) {
            semaineSelectionnee = listeBarreSemaine.get(0);
        }
        
        return semaineSelectionnee;
    }
    
    /**
     * Génére une barre d'alternance de semaine.
     * @param anneeScolaireDTO contient la date de rentré et sortie, les jours vaqués.
     * @param alternanceSemaine les alternances des semaines 1 et 2.
     * @param vraiOuFauxAlternance indique s'il y a ou non alternance
     * @return la liste des semaines.
     */
    public static final List<BarreSemaineDTO> generateListeSemaine(
            final AnneeScolaireDTO anneeScolaireDTO, final String alternanceSemaine, final Boolean vraiOuFauxAlternance) {

        final Map<Integer, TypeSemaine> mapAlternance = generateAlternanceSemaineFromDB(alternanceSemaine);
        final Set<Integer> listeSemaineVaque = generateSetPeriodeVaqueFromDb(anneeScolaireDTO.getPeriodeVacances());
        final List<BarreSemaineDTO> liste = new ArrayList<BarreSemaineDTO>();
        
        //pré-sélection de la semaine du jour
        Date dateCourante = org.apache.commons.lang.time.DateUtils.truncate(
                anneeScolaireDTO.getDateRentree(), Calendar.DAY_OF_MONTH); 
        Date dateFin = org.apache.commons.lang.time.DateUtils.truncate(anneeScolaireDTO.getDateSortie(), Calendar.DAY_OF_MONTH);
          
        
        Date finSemaine = null;
        
        boolean vraiOuFauxPair = true;
        do {
            final BarreSemaineDTO barreSemaineDTO = new BarreSemaineDTO();
            //dateCourante = (Date) dateCourante.clone();            

            Date debutSemaine = DateUtils.setJourSemaine(dateCourante, Calendar.MONDAY);
            finSemaine = DateUtils.setJourSemaine(dateCourante, Calendar.SUNDAY);
            
            final Integer numSemaine = DateUtils.getChamp(debutSemaine, Calendar.WEEK_OF_YEAR);   
            barreSemaineDTO.setNumeroSemaine(numSemaine.toString());
            barreSemaineDTO.setLundi(debutSemaine);
            barreSemaineDTO.setDimanche(finSemaine);
            
            // Semaines de vacances
            if (listeSemaineVaque.contains(numSemaine)) {
                barreSemaineDTO.setTypeJourEmploi(TypeSemaine.VAQUE);
                barreSemaineDTO.setCouleur(MAP_ALTERNANCE_COULEUR.get(TypeSemaine.VAQUE));
                barreSemaineDTO.setVraiOuFauxSelectionActive(false);
                
            // Semaines de travail
            } else {
                barreSemaineDTO.setVraiOuFauxSelectionActive(true);
                
                //si l'alternance a déjà été sauvegardé.
                if (mapAlternance.containsKey(numSemaine)) {
                    final TypeSemaine typeJourEmploi = mapAlternance.get(numSemaine);
                    barreSemaineDTO.setTypeJourEmploi(typeJourEmploi);
                    barreSemaineDTO.setCouleur(MAP_ALTERNANCE_COULEUR.get(typeJourEmploi));
                    
                } else if (vraiOuFauxAlternance) {
                    //si non initialisation.
                    if (vraiOuFauxPair) {
                        //pair
                        barreSemaineDTO.setTypeJourEmploi(TypeSemaine.PAIR);
                        barreSemaineDTO.setCouleur(MAP_ALTERNANCE_COULEUR.get(TypeSemaine.PAIR));
                    } else {
                        //impair
                        barreSemaineDTO.setTypeJourEmploi(TypeSemaine.IMPAIR);
                        barreSemaineDTO.setCouleur(MAP_ALTERNANCE_COULEUR.get(TypeSemaine.IMPAIR));                        
                    }
                    
                    vraiOuFauxPair = !vraiOuFauxPair;
                } else {
                    //par défaut
                    barreSemaineDTO.setTypeJourEmploi(TypeSemaine.NEUTRE);
                    barreSemaineDTO.setCouleur(MAP_ALTERNANCE_COULEUR.get(TypeSemaine.NEUTRE));   
                }
            }
            
            liste.add(barreSemaineDTO);
            dateCourante = org.apache.commons.lang.time.DateUtils.addWeeks(dateCourante, 1);

        } while (DateUtils.lessStrict(finSemaine, dateFin));
        
                  
        return liste;
    }
        
    
    /**
     * Genere un planning pour un mois donné.
     * @param date le mois en cours.
     * @return le planning (ie la liste des semaines du mois complété pour un affichage complet à la semaine).
     */
    public static List<SemaineDTO> genererListeSemaineMois(final Date date) {
        final Integer monthCal = DateUtils.getChamp(date, Calendar.MONTH);
        final Integer yearCal = DateUtils.getChamp(date, Calendar.YEAR);
        
        //Le mois suivant 
        final Date debutMoisSuivant = DateUtils.creer(yearCal, monthCal+1,1);
        
        //Trouver le premier lundi à afficher (eventuellement du mois précédent).
        final Date debutMois = DateUtils.creer(yearCal, monthCal,1);
        final Date finMois = DateUtils.creer(yearCal, monthCal,DateUtils.getNbJoursMois(monthCal, yearCal));
        
        Date debutSemaine = DateUtils.creer(yearCal, monthCal,1);
        while(DateUtils.getChamp(debutSemaine, Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            debutSemaine = DateUtils.ajouter(debutSemaine, Calendar.DAY_OF_YEAR, -1);
        }
        
        final List<SemaineDTO> semaines = new ArrayList<SemaineDTO>();
        while(DateUtils.getChamp(debutSemaine, Calendar.MONTH) != DateUtils.getChamp(debutMoisSuivant, Calendar.MONTH) ){
            semaines.add(new SemaineDTO(debutSemaine, debutMois, finMois));
            debutSemaine = DateUtils.ajouter(debutSemaine, Calendar.WEEK_OF_YEAR, 1);
        }
        
        return semaines;
    }
    
    /**
     * @param liste l 
     * @param date d 
     * @return barre mois correspondante
     */
    public static BarreMoisDTO barreMoisCourant(List<BarreMoisDTO> liste, Date date) {
        for (BarreMoisDTO mois : liste) {
            if (DateUtils.isBetween(date, mois.getDebutMois(), mois.getFinMois())) {
                return mois;
            }            
        }
        log.error("Mois pas trouvé");
        return null;
    }
    
    /**
     * Génére une barre d'alternance de mois.
     * @param dateRentreeScolaire la date de rentré scolaire.
     * @param dateSortieScolaire la date de fin scolaire.
     * @return la liste.
     */
    public static final List<BarreMoisDTO> generateBarreMois(final Date dateRentreeScolaire, 
            final Date dateSortieScolaire) {
        final List<BarreMoisDTO> liste = new ArrayList<BarreMoisDTO>();
                
        //Enlève tout sauf l'année et les moins des dates
        
        final Date dateDebut = org.apache.commons.lang.time.DateUtils.truncate(dateRentreeScolaire, Calendar.MONTH);
        final Date dateFin = org.apache.commons.lang.time.DateUtils.truncate(dateSortieScolaire, Calendar.MONTH);
        
        if (!dateSortieScolaire.after(dateRentreeScolaire)) {
            log.warning("La date de rentrée {0} est après date sortie {1}", dateRentreeScolaire, dateSortieScolaire);
            return liste;
        }
        
        Date dateCourante = dateDebut;
                
        //Génération de la barre de mois        
        while(!dateCourante.after(dateFin)) {
            final BarreMoisDTO barreMoisDTO = new BarreMoisDTO();
                      
            Calendar curCalendar = Calendar.getInstance();
            curCalendar.setTime(dateCourante);
            
            curCalendar.set(Calendar.DATE, curCalendar.getActualMinimum(Calendar.DATE));
            barreMoisDTO.setDebutMois(curCalendar.getTime());
            
            curCalendar.set(Calendar.DATE, curCalendar.getActualMaximum(Calendar.DATE));
            barreMoisDTO.setFinMois(curCalendar.getTime());
                      
            curCalendar.add(Calendar.MONTH, 1);
            
            dateCourante = org.apache.commons.lang.time.DateUtils.truncate(curCalendar, Calendar.MONTH).getTime();
                        
            liste.add(barreMoisDTO);
        }
        
        return liste;        
    }

    /**
     * Génére une liste de périodes à partir des données de la BD.
     * @param anneeScolaireDTO l'année scolaire.
     * @return la liste des periodes qui contient à minina la période de l'année scolaire.
     */
    public static List<GenericDTO<Date, Date>> generatePeriodes(
            AnneeScolaireDTO anneeScolaireDTO) {
        final String periodes = anneeScolaireDTO.getPeriodeScolaires();
        
        final List<GenericDTO<Date, Date>> res = new ArrayList<GenericDTO<Date,Date>>();
        if (! org.apache.commons.lang.StringUtils.isEmpty(periodes)){
           final String[] listePeriode = StringUtils.split(periodes, "\\|");
            
           for (final String periode : listePeriode) {
               final String[] listeDate = StringUtils.split(StringUtils.trimToEmpty(periode), ":");
               if (listeDate.length == 2) {
                   final Date dateDebut = DateUtils.parse(listeDate[0]);
                   final Date dateFin = DateUtils.parse(listeDate[1]);
                   res.add(new GenericDTO<Date, Date>(dateDebut,dateFin));
               }
           
           }
        }
        
        if (CollectionUtils.isEmpty(res)){
            final GenericDTO<Date, Date> annee = new GenericDTO<Date, Date>(anneeScolaireDTO.getDateRentree(), anneeScolaireDTO.getDateSortie());
            res.add(annee);
        }
        
        return res;
    }
    
    /**
     * Constitution d'un Set des numéros de semaine vaquée.
     * @param periodeVancance les périodes.
     * @return la liste.
     */
    public static final Set<Integer> generateSetPeriodeVaqueFromDb(final String periodeVancance) {
        final Set<Integer> jourFerie = new HashSet<Integer>();
        
        final String[] listePeriode = StringUtils.split(
                StringUtils.trimToEmpty(periodeVancance), "\\|");

        for (final String periode : listePeriode) {
            final String[] listeDate = StringUtils.split(StringUtils.trimToEmpty(periode), ":");
            if (listeDate.length == 2) {
                Date dateDebut = DateUtils.parse(listeDate[0]);
                final Date dateFin = DateUtils.getDatePlus1(DateUtils
                        .parse(listeDate[1]));

                boolean vraiOuFauxLundi = false;
                while (!DateUtils.equalsDate(dateDebut, dateFin)) {
                    if (DateUtils.getChamp(dateDebut, Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                        vraiOuFauxLundi = true;
                    } else if (vraiOuFauxLundi
                            && DateUtils.getChamp(dateDebut,
                                    Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        // si la semaine est complète
                        jourFerie.add(DateUtils.getChamp(dateDebut,
                                Calendar.WEEK_OF_YEAR));
                        vraiOuFauxLundi = false;
                    }

                    dateDebut = DateUtils.getDatePlus1(dateDebut);
                }

            }
        }
            
        
        
        return jourFerie;
    }
    
    /**
     * Génére une map de l'aternance des semaines 1 et 2. 
     * @param alternanceSemaine la chaîne.
     * @return la map.
     */
    public static final Map<Integer, TypeSemaine> generateAlternanceSemaineFromDB(final String alternanceSemaine) {
        final Map<Integer, TypeSemaine> map = new HashMap<Integer, TypeSemaine>();
        
        final String[] listeAlternance = StringUtils.split(StringUtils.trimToEmpty(alternanceSemaine), "\\|");
        
        for (final String alternance : listeAlternance) {
            final String[] listeNumType = StringUtils.split(alternance, ":");
            if (listeNumType.length == 2) {
                final Integer numSem = Integer.parseInt(listeNumType[0]);
                final TypeSemaine type = TypeSemaine.valueOf(listeNumType[1]);
                map.put(numSem, type);
            }
        }
    
          
        
        return map;
    }    
    
}
