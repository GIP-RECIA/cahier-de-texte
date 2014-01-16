/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Alimentation.java,v 1.13 2010/06/07 09:20:44 ent_breyton Exp $
 */

package org.crlr.alimentation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.crlr.dto.Environnement;
import org.crlr.dto.application.base.GenericDTO;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.utils.PropertiesUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ldap.control.PagedResult;
import org.springframework.ldap.control.PagedResultsCookie;

/**
 * Application d'alimentation de la base de données du module Cahier de texte, à
 * partir de données de l'annuaire ENT.
 *
 * @author breytond.
 * @version $Revision: 1.13 $
 */
public final class Alimentation {
    /** Fichier de log pour les cas d'erreur. */
    private static final Log log = LogFactory.getLog(Alimentation.class);

    /** The Constant DROIT_PROFIL. */
    private static final String TYPES_DEVOIR_PROPERTIES = "/typeDevoir.properties";
    
    private static final String CONFIG_PROPERTIES = "/config.properties";

/**
     * Constructeur.
     */
    private Alimentation() {
    }

    /**
     * Parse une date.
     *
     * @param sDate La date a parser
     * @param sFormat le format.
     *
     * @return La date.
     *
     * @throws Exception .
     */
    private static Date stringToDate(String sDate, String sFormat)
                             throws Exception {
        final SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
        return sdf.parse(sDate);
    }
    
    @SuppressWarnings("unchecked")
    private static void getEnseignants(final OperationSqlService operationSqlService, final OperationLdapService operationLdapService) {

        log.info("*** Insertion des enseignants ***");

        //Insertion des enseignants
        //ce qui renseigne la table etab-enseignants
        //les classes - groupes et jointures
        PagedResultsCookie cookie = null;
        PagedResult enseignants;
        do {
            enseignants = operationLdapService.getEnseignants(cookie);
            cookie=enseignants.getCookie();
            operationSqlService.addEnseignants(enseignants.getResultList());
        } while (cookie.getCookie() != null);

    }
    
    @SuppressWarnings("unchecked")
    private static void getEtablissements(
            final OperationSqlService operationSqlService,
            final OperationLdapService operationLdapService,
            final String[] typesDevoir) {
        log.info("*** Insertion des établissements et des types de devoirs ***");

        // Insertion des etablissements
        PagedResultsCookie cookie = null;
        PagedResult etablissements;
        do {
            etablissements = operationLdapService.getEtablissements(cookie);
            cookie = etablissements.getCookie();
            operationSqlService.addEtablissements(
                    etablissements.getResultList(), typesDevoir);
        } while (cookie.getCookie() != null);
    }
    
    @SuppressWarnings("unchecked")
    private static void getEleves(final OperationSqlService operationSqlService, final OperationLdapService operationLdapService) {
        log.info("*** Insertion des élèves ***");

        PagedResultsCookie cookie = null;
        PagedResult eleves;
        
        do {
          //Insertion des eleves
            //ce qui renseigne la table etab-eleve
            //les jointures avec groupe et classe
            eleves = operationLdapService.getEleves(cookie);
            cookie = eleves.getCookie();
            operationSqlService.addEleves(eleves.getResultList());
        } while (cookie.getCookie() != null);
        
    }
    
    @SuppressWarnings("unchecked")
    private static void getInsecteurs(final OperationSqlService operationSqlService, final OperationLdapService operationLdapService) {
        log.info("*** Insertion des inspecteurs ***");

        //Insertion des inspecteurs
        PagedResult inspecteurs = operationLdapService.getInspecteurs(null);
        operationSqlService.addInspecteurs(inspecteurs.getResultList());
        while (inspecteurs.getCookie().getCookie() != null) {
            inspecteurs = operationLdapService.getInspecteurs(inspecteurs.getCookie());
            operationSqlService.addInspecteurs(inspecteurs.getResultList());
        }
    }

    /**
     * programme permettant l'insertion des données dans la base de données à
     * partir de celles du LDAP.
     *
     * @param args Il doit y avoir 3 arguments, l'exercice, la date de rentrée et la date
     *        de fin d'année. Par exemple:"2009-2010" "2/09/2009" "31/06/2010"
     */
    public static void main(String[] args) {
        if (args.length < 3 ) {
            log.info("Veuillez entrer au moins 3 arguments :");
            log.info("l'exercice, la date de rentree et la date de fin d'année");
            log.info("Par exemple : '2009-2010' '2/09/2009' '31/06/2010'");
            log.info("L'argument suivant s'il est fourni est utilisé pour le calcul des périodes scolaires");
            log.info("Par exemple : '2009-2010' '2/09/2009' '31/06/2010' '2/09/2009 1/11/20091/02/2010'");
            return;
        }

        final String exercice = args[0];

        final Pattern p1 = Pattern.compile("20[0-9][0-9]-20[0-9][0-9]");
        final Matcher m1 = p1.matcher(exercice);
        if (!m1.matches()) {
            log.info("le premier parametre doit correspondre à l'exercice");
            log.info("il doit être de la forme AAAA-AAAA");
            return;
        }

        java.util.Date dateRentree;
        final Pattern p2 =
            Pattern.compile("(0[1-9]|[1-2][0-9]|30|31)/(0[1-9]|1[0-2])/20([0-9]{2})");
        final Matcher m2 = p2.matcher(args[1]);
        if (!m2.matches()) {
            log.info("le second parametre doit correspondre à la date de rentrée");
            log.info("il doit être de la forme dd/MM/yyyy");
            log.info(args[1]);
            return;
        }
        try {
            dateRentree = stringToDate(args[1], "dd/MM/yyyy");
        } catch (Exception e) {
            log.info("le second parametre doit correspondre à la date de rentrée");
            log.info("il doit être de la forme dd/MM/yyyy");
            return;
        }

        final Matcher m3 = p2.matcher(args[2]);
        if (!m3.matches()) {
            log.info("le troisieme parametre doit correspondre à la date de sortie");
            log.info("il doit être de la forme dd/MM/yyyy");
            log.info(args[2]);
            return;
        }
        java.util.Date dateSortie;

        try {
            dateSortie = stringToDate(args[2], "dd/MM/yyyy");
        } catch (Exception e) {
            log.info("le seconde parametre doit correspondre à la date de rentrée");
            log.info("il doit être de la forme dd/MM/yyyy");
            return;
        }
        if (dateRentree.after(dateSortie)) {
            log.info("la date de rentrée est située après la date de sortie");
            return;
        }
        
        //Prise en compte des periodes scolaires
        final List<GenericDTO<Date, Date>> periodes = new ArrayList<GenericDTO<Date,Date>>();
        /*Date dateDebutPeriode =null ;
        Boolean firstDate = true;
        if (args[3] != null){
            final String[] periodesT = args[3].split(" ");
            for (String periode : periodesT){
                try {
                    final Date datePeriode = stringToDate(periode, "dd/MM/yyyy");
                    if (firstDate){
                        if( datePeriode.before(dateRentree)){
                            log.info("le premier argument de periode doit être posterieur ou égal à la date de rentrée: {0} {1}", 
                                    datePeriode, dateRentree);
                            return;
                        }
                        firstDate = false;
                        dateDebutPeriode = datePeriode;
                    } else {
                        final Date dateFinPeriode = DateUtils.ajouter(datePeriode, Calendar.DAY_OF_YEAR, -1);
                        if (dateDebutPeriode.before(dateFinPeriode)){
                            periodes.add(new GenericDTO<Date, Date>(dateDebutPeriode,dateFinPeriode));
                        } else {
                            log.info("les dates de periodes doivent être fournies dans l'ordre chronologique");
                            return;
                        }
                    }
                    dateDebutPeriode = datePeriode;
                    
                } catch (Exception e) {
                    log.info("les parametres de periodes doivent correspondre aux dates des périodes");
                    log.info("ils doivent être de la forme dd/MM/yyyy");
                    return;
                }  
            }
        }
        if (dateDebutPeriode != null){
            if (dateDebutPeriode.after(dateSortie)){
                log.info("les parametres de periodes doivent correspondre aux dates des périodes");
                log.info("ils doivent être de la forme dd/MM/yyyy");
                return;
            } else {
                periodes.add(new GenericDTO<Date, Date>(dateDebutPeriode,dateSortie));
            }
        }*/
        
        // Instanciation du contexte Spring à partir d'un fichier XML dans le classpath
        final ApplicationContext applicationContext =
            new ClassPathXmlApplicationContext("spring-alimentation.xml");

        //le bean operationSQL permet d'effectuer des traitements SQL la transaction est positionnée au niveau méthode.
        final OperationSqlService operationSqlService =
            (OperationSqlService) applicationContext.getBean("operationSql");

        // le bean operationLdap permet de rechercher des entrées dans l'annuaire de l'ENT.
        final OperationLdapService operationLdapService =
            (OperationLdapService) applicationContext.getBean("operationLdap");

        
        //Recherche des années scolaires précédentes.
        log.info("*** Recherche des années scolaires existantes dans les archives ***");        
        final String[] listeAnnee = StringUtils.split(exercice, "-");
        if (listeAnnee.length == 2) {
            final Integer anneeDepart = Integer.valueOf(listeAnnee[0]);
            final Integer anneeFin = Integer.valueOf(listeAnnee[1]);
            
            final String compositionDuSchemaPrecedent = Archive.PREFIX_SCHEMA_ARCHIVE + 
                (anneeDepart -1) + "-" + (anneeFin-1);
            
            operationSqlService.saveListeAnneeScolaireSurSchemaCourantDeArchive(compositionDuSchemaPrecedent);
        }
        
        log.info("*** Insertion de l'année scolaire ***");
        //Insertion de l'année scolaire en cours
        operationSqlService.addAnneeScol(exercice, dateRentree, dateSortie, periodes);

        
        log.info("*** Vérification de la base de données ***");
        if ( operationSqlService.existEtablissement()){
            log.info("Des établissements existent déjà dans la base de données - " +
                   "la base de données devrait avoir été initialisée au préalable.");
            
            return;
        }
        

        final Properties configProperties = PropertiesUtils.load(CONFIG_PROPERTIES);
        final String envStr = configProperties.getProperty("application.env");
        
        Environnement env = Environnement.valueOf(envStr);
        
        //Garde pour Récia / Région centre 
        if (Environnement.CRC == env) {
            //Dans l'aquitain et la CRLR, la syncronisation des établissemens 
            //  ,des enseignants, des élèves, des inspecteurs s'éffectuent par la propagation.
            
            log.info("*** Insertion des types de devoir ***");

            //Insertion des types de devoir
            final Properties propsTypeDevoir = PropertiesUtils.load(TYPES_DEVOIR_PROPERTIES);
            
            final String chaine = propsTypeDevoir.getProperty("typesDevoir");
            final String[] typesDevoir = chaine.split(",");        

            //Dans la CRLR et l'aquitain, les établissements sont synchronisées par la tache changementAnnee dans la module propagation
            getEtablissements(operationSqlService, operationLdapService, typesDevoir);

            getEnseignants(operationSqlService, operationLdapService);
        
            getEleves(operationSqlService, operationLdapService);
            
            getInsecteurs(operationSqlService, operationLdapService);
        }
        
        
       
        if (Environnement.CRC == env) {
            final String pattern = "yyyyMMddHHmmss";
            final String nouvelleDateDerniereMiseAJour = new SimpleDateFormat(
                    pattern).format(Calendar.getInstance().getTime()) + "Z";
            log.info("Date de mise à jour à inscrire {0}",
                    nouvelleDateDerniereMiseAJour);
        }

        log.info("*** Fin de l'insertion ***");
    }
}
