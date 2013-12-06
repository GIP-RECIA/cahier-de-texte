/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AnneeScolaireServiceTest,v 1.0 2011/02/21 12:20:58 $
 */

/**/
package org.crlr.test.services;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.admin.DatePeriodeQO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.DateAnneeScolaireQO;
import org.crlr.dto.application.base.OuvertureQO;
import org.crlr.dto.application.base.PeriodeVacanceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.services.AnneeScolaireService;

import org.crlr.test.AbstractMetierTest;
import org.crlr.utils.AssertionException;
import org.crlr.utils.DateUtils;

/**
 * tests réalisés pour la classe AnneeScolaireService.
 *
 * @author FMOULIN
 * @version $Revision: 1.1 $
 */
public class AnneeScolaireServiceTest extends AbstractMetierTest {
    AnneeScolaireService anneeScolaireService;

    /**
     * 
    DOCUMENT ME!
     *
     * @param anneeScolaireService the anneeScolaireService to set
     */
    public void setAnneeScolaireService(AnneeScolaireService anneeScolaireService) {
        this.anneeScolaireService = anneeScolaireService;
    }

    /**
     *  testFindListeAnneeScolaire().
     *  
     *  test l'appel à la fonction AnneeScolaireService.findListeAnneeScolaire().
     *  
     *  fonctionne mais parcontre ne renvoie pas l'exercice courant,
     *  ainsi que les informations sur les vacances, et l'ouverture du cahier!
     */
    public void testFindListeAnneeScolaire() {
        log.debug("METHODE : testFindListeAnneeScolaire");
        try {
            final ResultatDTO<List<AnneeScolaireDTO>> resultListeAnneeScolaire =
                anneeScolaireService.findListeAnneeScolaire();
            //fonctionne mais ne renvoie pas l'information Vacances et CahierOuvert.
            for(AnneeScolaireDTO annee : resultListeAnneeScolaire.getValeurDTO()){
                log.debug("exercice : {0}, dateEntree : {1}, dateSortie : {2}, vacances : {3}, cahierOuvert : {4} ",
                        annee.getExercice(), annee.getDateRentree(), annee.getDateSortie(),
                        annee.getPeriodeVacances(), annee.getVraiOuFauxCahierOuvertENT());
            }

        } catch (MetierException e) {
            log.debug("{0}", e.getMessage());
        }
    }

    /**
     * testSaveAnneeScolaireNull().
     * 
     * teste l'appel à la méthode anneeScolaireService.saveAnneeScolaire(DateAnneeScolaireQO).
     * 
     * avec comme parametre null.
     * 
     * ne fonctionne pas --> aucune vérification de non nullité.
     * 
     */
    public void testSaveAnneeScolaireNull(){
        log.debug("METHODE : testSaveAnneeScolaireNull");
        try {
            final ResultatDTO<Integer> resultatEntier = anneeScolaireService.saveAnneeScolaire(null);
            log.debug("valeur : {0}", resultatEntier.getValeurDTO());
        } catch (AssertionException e) {
            log.debug("ERREUR : {0}, \n {1}", e.getMessage(), e.getStackTrace());
        } catch (MetierException e) {
            log.debug("ERREUR : {0}, \n {1}", e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * testSaveAnneeScolaireNonInit().
     * 
     * teste l'appel à la méthode anneeScolaireService.saveAnneeScolaire(DateAnneeScolaireQO).
     * 
     * avec un parametre non null mais non initialisé.
     * 
     * fonctionne et renvoie les erreurs (métier exception) liées à la nullité des données membres.
     * 
     */
    public void testSaveAnneeScolaireNonInit(){
        log.debug("METHODE : testSaveAnneeScolaireNonInit");
        try {
            //eclate car dateRentree et dateSortie sont à null !
            final DateAnneeScolaireQO anneeScolaireQo = new DateAnneeScolaireQO();
            final ResultatDTO<Integer> resultatEntier = anneeScolaireService.saveAnneeScolaire(anneeScolaireQo);
            log.debug("valeur : {0}", resultatEntier.getValeurDTO());
        } catch (MetierException e) {
            log.debug("ERREUR : {0}", e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * testSaveAnneeScolaireChampNull().
     * 
     * teste l'appel à la méthode anneeScolaireService.saveAnneeScolaire(DateAnneeScolaireQO).
     * 
     * avec un parametre non null.
     * les champs de ce parametre sont initialisé à null.
     * fonctionne et renvoie les erreurs (métier exception) liées à la nullité des données membres.
     */
    public void testSaveAnneeScolaireChampNull(){
        log.debug("METHODE : testSaveAnneeScolaireChampNull");
        try {
            //eclate car dateRentree et dateSortie sont à null !
            final DateAnneeScolaireQO anneeScolaireQo = new DateAnneeScolaireQO();
            anneeScolaireQo.setDateRentree(null);
            anneeScolaireQo.setDateSortie(null);
            anneeScolaireQo.setId(null);
            final ResultatDTO<Integer> resultatEntier = anneeScolaireService.saveAnneeScolaire(anneeScolaireQo);
            log.debug("valeur : {0}", resultatEntier.getValeurDTO());
        } catch (MetierException e) {
            log.debug("ERREUR : {0}", e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * testSaveAnneeScolaireIdNull().
     * 
     * teste l'appel à la méthode anneeScolaireService.saveAnneeScolaire(DateAnneeScolaireQO).
     * 
     * avec un parametre non null.
     * les champs de ce parametre initialisé et non null sauf pour Id qui est mit à null.
     * 
     * fonction, Assert.isNotNull sur le champ ID genere bien l'erreur.
     * 
     * @throws MetierException l'exception potentielle qui ne doit pas apparitre dans ce cas.
     *          dans le cas ou elle apparait, elle doit faire échoué le test.
     */
    //Ne fonctionne que si le systeme est dans l'année en cours (2010-2011)
    public void testSaveAnneeScolaireIdNull() throws MetierException{
        log.debug("METHODE : testSaveAnneeScolaireIdNull");
        try {
            //eclate car id est à null !
            final DateAnneeScolaireQO anneeScolaireQo = new DateAnneeScolaireQO();
            final Date dateDebut = DateUtils.creer(2010, Calendar.AUGUST, 30);
            final Date dateFin = DateUtils.creer(2011, Calendar.JULY, 1);
            anneeScolaireQo.setDateRentree(dateDebut);
            anneeScolaireQo.setDateSortie(dateFin);
            anneeScolaireQo.setId(null);
            final ResultatDTO<Integer> resultatEntier = anneeScolaireService.saveAnneeScolaire(anneeScolaireQo);
            log.debug("valeur : {0}", resultatEntier.getConteneurMessage());
        } catch(AssertionException e){
            log.debug("AssertionProbleme : {0}", e.getMessage(), e.getStackTrace());
        } 
    }

    /**
     * testSaveAnneeScolaireIncoheranceDate().
     * 
     * teste l'appel à la méthode anneeScolaireService.saveAnneeScolaire(DateAnneeScolaireQO).
     * 
     * avec un parametre non null.
     * les champs de ce parametre sont initialisé à des dates cohérentes mais inversées.
     * fonctionne et renvoie un erreur (métier exception) d'incohérence des dates entrée et de sortie.
     */
    public void testSaveAnneeScolaireIncoheranceDate(){
        log.debug("METHODE : testSaveAnneeScolaireIncoheranceDate");
        try {
            //eclate car dateSortie<dateRentree !
            final DateAnneeScolaireQO anneeScolaireQo = new DateAnneeScolaireQO();
            final Date dateDebut = DateUtils.creer(2010, Calendar.AUGUST, 30);
            final Date dateFin = DateUtils.creer(2011, Calendar.JULY, 1);
            anneeScolaireQo.setDateRentree(dateFin);
            anneeScolaireQo.setDateSortie(dateDebut);
            anneeScolaireQo.setId(47474747);
            final ResultatDTO<Integer> resultatEntier = anneeScolaireService.saveAnneeScolaire(anneeScolaireQo);
            log.debug("valeur : {0}", resultatEntier.getValeurDTO());
        } catch (MetierException e) {
            log.debug("ERREUR : {0}", e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * testSaveAnneeScolaireIdNegatif()
     * 
     * teste l'appel à la méthode anneeScolaireService.saveAnneeScolaire(DateAnneeScolaireQO)
     * 
     * avec un parametre non null.
     * les champs de ce parametre sont initialisés avec des valeurs (dates) cohérentes,
     * sauf l'id qui est setté avec un nombre négatif
     * 
     * fonctionne, aucune modification n'est apporté à la BDD car cet id n'est pas connu
     * 
     */
    public void testSaveAnneeScolaireIdNegatif(){
        log.debug("METHODE : testSaveAnneeScolaireIdNegatif");
        try {
            final DateAnneeScolaireQO anneeScolaireQo = new DateAnneeScolaireQO();
            final Date dateDebut = DateUtils.creer(2010, Calendar.AUGUST, 30);
            final Date dateFin = DateUtils.creer(2011, Calendar.JULY, 1);
            anneeScolaireQo.setDateRentree(dateDebut);
            anneeScolaireQo.setDateSortie(dateFin);
            anneeScolaireQo.setId(-47474747);
            final ResultatDTO<Integer> resultatEntier = anneeScolaireService.saveAnneeScolaire(anneeScolaireQo);
            log.debug("valeur : {0}", resultatEntier.getValeurDTO());
        } catch (MetierException e) {
            log.debug("ERREUR : {0}", e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * testSaveAnneeScolaireDateDebutAvancee()
     * 
     * teste l'appel à la méthode anneeScolaireService.saveAnneeScolaire(DateAnneeScolaireQO)
     * 
     * avec un parametre non null.
     * les champs de ce parametre sont initialisés,
     * la date de début est mise deux ans avant (2008),
     * la date de fin est normale (2011),
     * l'id est setté avec un nombre cohérent mais pas en base.
     * 
     * fonctionne, l'erreur (Métier Exception) est levé pour dire que la date de début n'est pas dans la fenetre de date possible.
     * 
     */
    public void testSaveAnneeScolaireDateDebutAvancee(){
        log.debug("METHODE : testSaveAnneeScolaireDateDebutAvancee");
        try {
            final DateAnneeScolaireQO anneeScolaireQo = new DateAnneeScolaireQO();
            final Date dateDebut = DateUtils.creer(2008, Calendar.AUGUST, 30);
            final Date dateFin = DateUtils.creer(2011, Calendar.JULY, 1);
            anneeScolaireQo.setDateRentree(dateDebut);
            anneeScolaireQo.setDateSortie(dateFin);
            anneeScolaireQo.setId(4);
            final ResultatDTO<Integer> resultatEntier = anneeScolaireService.saveAnneeScolaire(anneeScolaireQo);
            log.debug("valeur : {0}", resultatEntier.getValeurDTO());
        } catch (MetierException e) {
            log.debug("ERREUR : {0}", e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * testSaveAnneeScolaireDateFinReculee()
     * 
     * teste l'appel à la méthode anneeScolaireService.saveAnneeScolaire(DateAnneeScolaireQO)
     * 
     * avec un parametre non null.
     * les champs de ce parametre sont initialisés,
     * la date de début est normale (2010),
     * la date de fin est reculée (2013),
     * l'id est setté avec un nombre cohérent mais pas en base.
     * 
     * fonctionne, l'erreur (Métier Exception) est levé pour dire que la date de fin n'est pas dans la fenetre de date possible.
     * 
     */
    public void testSaveAnneeScolaireDateFinReculee(){
        log.debug("METHODE : testSaveAnneeScolaireDateFinReculee");
        try {
            final DateAnneeScolaireQO anneeScolaireQo = new DateAnneeScolaireQO();
            final Date dateDebut = DateUtils.creer(2010, Calendar.AUGUST, 30);
            final Date dateFin = DateUtils.creer(2013, Calendar.JULY, 1);
            anneeScolaireQo.setDateRentree(dateDebut);
            anneeScolaireQo.setDateSortie(dateFin);
            anneeScolaireQo.setId(4);
            final ResultatDTO<Integer> resultatEntier = anneeScolaireService.saveAnneeScolaire(anneeScolaireQo);
            log.debug("valeur : {0}", resultatEntier.getValeurDTO());
        } catch (MetierException e) {
            log.debug("ERREUR : {0},\n {1}", e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * testCheckDatesPeriodeNull().
     * 
     * teste l'appel à la méthode anneeScolaireService.checkDatesPeriode(DatePeriodeQO) 
     * 
     * avec comme parametre null.
     * 
     * ne fonctionne pas --> aucune vérification de non nulité du parametre.
     */    
    public void testCheckDatesPeriodeNull(){
        log.debug("METHODE : testCheckDatesPeriodeNull");
        try{
            anneeScolaireService.checkDatesPeriode(null);
        }catch (AssertionException e) {
            log.debug("ERREUR - ASSERT : {0}", e.getMessage());
        }catch (MetierException e) {
            log.debug("ERREUR - ME : {0}", e.getMessage());
        }
    }

    /**
     * testCheckDatesPeriodeChampNull().
     * 
     * teste l'appel à la méthode anneeScolaireService.checkDatesPeriode(DatePeriodeQO). 
     * 
     * avec un parametre non null, mais les données membres nulles.
     * 
     * fonctionne --> execption Assert.isNotNull sur dateRentree.
     * 
     */    
    public void testCheckDatesPeriodeChampNull(){
        log.debug("METHODE : testCheckDatesPeriodeChampNull");
        try{
            final DatePeriodeQO datePeriodeQO = new DatePeriodeQO();
            datePeriodeQO.setDateDebutPlage(null);
            datePeriodeQO.setDateFinPlage(null);
            datePeriodeQO.setDateRentree(null);
            datePeriodeQO.setDateSortie(null);
            datePeriodeQO.setPlageExistante(null);
            anneeScolaireService.checkDatesPeriode(datePeriodeQO);
        }catch (AssertionException e) {
            log.debug("ERREUR - ASSERT : {0}", e.getMessage());
        }catch (MetierException e) {
            log.debug("ERREUR - ME : {0}", e.getMessage());
        }
    }

    /**
     * testCheckDatesPeriodeChampNullSaufRentreeSortieDebutPlage().
     * 
     * teste l'appel à la méthode anneeScolaireService.checkDatesPeriode(DatePeriodeQO) 
     * 
     * avec un parametre non null, mais les données membres nulles,
     * sauf dateRentree - 1/09/2010
     * sauf dateSortie - 1/07/2011
     * sauf dateDebutPlage - 1/01/2011
     * 
     * fonctionne --> MetierException --> Echec contrôle nouvelle plage jours chomés 
     * 
     */    
    public void testCheckDatesPeriodeChampNullSaufRentreeSortieDebutPlage(){
        log.debug("METHODE : testCheckDatesPeriodeChampNullSaufRentreeSortieDebutPlage");
        try{
            final DatePeriodeQO datePeriodeQO = new DatePeriodeQO();
            datePeriodeQO.setDateDebutPlage(DateUtils.creer(2011, Calendar.JANUARY, 1));
            datePeriodeQO.setDateFinPlage(null);
            datePeriodeQO.setDateRentree(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
            datePeriodeQO.setDateSortie(DateUtils.creer(2011, Calendar.JULY, 1));
            datePeriodeQO.setPlageExistante(null);
            anneeScolaireService.checkDatesPeriode(datePeriodeQO);
        }catch (AssertionException e) {
            log.debug("ERREUR - ASSERT : {0}", e.getMessage());
        }catch (MetierException e) {
            log.debug("ERREUR - ME : {0}", e.getMessage());
        }
    }

    /**
     * testCheckDatesPeriodeNonNullSaufPlageExistante().
     * 
     * teste l'appel à la méthode anneeScolaireService.checkDatesPeriode(DatePeriodeQO) 
     * 
     * avec un parametre non null, mais les données membres nulles,
     * sauf dateRentree - 1/09/2010
     * sauf dateSortie - 1/07/2011
     * sauf dateDebutPlage - 1/01/2011
     * sauf dateFinPlage - 1/07/2011
     * 
     * fonctionne --> pas d'erreur liée à la string plageExistante
     */
    public void testCheckDatesPeriodeNonNullSaufPlageExistante(){
        log.debug("METHODE : testCheckDatesPeriodeNonNullSaufPlageExistante");
        try{
            final DatePeriodeQO datePeriodeQO = new DatePeriodeQO();
            datePeriodeQO.setDateDebutPlage(DateUtils.creer(2011, Calendar.JANUARY, 1));
            datePeriodeQO.setDateFinPlage(DateUtils.creer(2011, Calendar.JULY, 1));
            datePeriodeQO.setDateRentree(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
            datePeriodeQO.setDateSortie(DateUtils.creer(2011, Calendar.JULY, 1));
            datePeriodeQO.setPlageExistante(null);
            anneeScolaireService.checkDatesPeriode(datePeriodeQO);
        }catch (AssertionException e) {
            log.debug("ERREUR - ASSERT : {0}", e.getMessage());
        }catch (MetierException e) {
            log.debug("ERREUR - ME : {0}", e.getMessage());
        }
    }

    /**
     * testCheckDatesPeriodeChampNullSaufRentree().
     * 
     * teste l'appel à la méthode anneeScolaireService.checkDatesPeriode(DatePeriodeQO) 
     * 
     * avec un parametre non null, mais les données membres nulles,
     * 
     * sauf dateRentree - 1/09/2010
     * 
     * fonctionne --> execption Assert.isNotNull sur dateSortie.
     */
    public void testCheckDatesPeriodeChampNullSaufRentree(){
        log.debug("METHODE : testCheckDatesPeriodeChampNullSaufRentree");
        try{
            final DatePeriodeQO datePeriodeQO = new DatePeriodeQO();
            datePeriodeQO.setDateDebutPlage(null);
            datePeriodeQO.setDateFinPlage(null);
            datePeriodeQO.setDateRentree(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
            datePeriodeQO.setDateSortie(null);
            datePeriodeQO.setPlageExistante(null);
            anneeScolaireService.checkDatesPeriode(datePeriodeQO);
        }catch (AssertionException e) {
            log.debug("ERREUR - ASSERT : {0}", e.getMessage());
        }catch (MetierException e) {
            log.debug("ERREUR - ME : {0}", e.getMessage());
        }
    }

    /**
     * testCheckDatesPeriodeChampNullSaufRentreeSortie().
     * 
     * teste l'appel à la méthode anneeScolaireService.checkDatesPeriode(DatePeriodeQO) 
     * 
     * avec un parametre non null, mais les données membres nulles,
     * 
     * sauf dateRentree - 1/09/2010
     * sauf dateSortie - 1/07/2011
     * 
     * fonctionne --> MetierException --> Echec contrôle nouvelle plage jours chomés 
     * 
     */
    public void testCheckDatesPeriodeChampNullSaufRentreeSortie(){
        log.debug("METHODE : testCheckDatesPeriodeChampNullSaufRentreeSortie");
        try{
            final DatePeriodeQO datePeriodeQO = new DatePeriodeQO();
            datePeriodeQO.setDateDebutPlage(null);
            datePeriodeQO.setDateFinPlage(null);
            datePeriodeQO.setDateRentree(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
            datePeriodeQO.setDateSortie(DateUtils.creer(2011, Calendar.JULY, 1));
            datePeriodeQO.setPlageExistante(null);
            anneeScolaireService.checkDatesPeriode(datePeriodeQO);
        }catch (AssertionException e) {
            log.debug("ERREUR - ASSERT : {0}", e.getMessage());
        }catch (MetierException e) {
            log.debug("ERREUR - ME : {0}", e.getMessage());
        }
    }

    /**
     * testCheckDatesPeriodeNonNull().
     * 
     * teste l'appel à la méthode anneeScolaireService.checkDatesPeriode(DatePeriodeQO) 
     * 
     * avec un parametre non null, aucune données membres nulles,
     * 
     *  dateRentree - 1/09/2010
     *  dateSortie - 1/07/2011
     *  dateDebutPlage - 1/01/2011
     *  dateFinPlage - 1/07/2011
     *  plageExistante - "true"
     * 
     * Fonctionne --> pas d'erreur liée à la plageExistante.
     * 
     */
    public void testCheckDatesPeriodeNonNull(){
        log.debug("METHODE : testCheckDatesPeriodeNonNull");
        try{
            final DatePeriodeQO datePeriodeQO = new DatePeriodeQO();
            datePeriodeQO.setDateDebutPlage(DateUtils.creer(2011, Calendar.JANUARY, 1));
            datePeriodeQO.setDateFinPlage(DateUtils.creer(2011, Calendar.JULY, 1));
            datePeriodeQO.setDateRentree(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
            datePeriodeQO.setDateSortie(DateUtils.creer(2011, Calendar.JULY, 1));
            datePeriodeQO.setPlageExistante("true");
            anneeScolaireService.checkDatesPeriode(datePeriodeQO);
        }catch (MetierException e) {
            log.debug("ERREUR - ME : {0}", e.getMessage());
        }
    }

    /**
     * testCheckDatesPeriodeEncoheranceRentreeSortie().
     * 
     * teste l'appel à la méthode anneeScolaireService.checkDatesPeriode(DatePeriodeQO) 
     * 
     * avec un parametre non null, aucune données membres nulles,
     * et incohérance sur les dates d'entrée et de sortie.
     *  dateRentree -1/07/2011
     *  dateSortie - 1/09/2010 
     *  dateDebutPlage - 1/01/2011
     *  dateFinPlage - 1/07/2011
     *  plageExistante - "true"
     * 
     * fonctionne --> MetierException --> Echec contrôle nouvelle plage jours chomés
     * 
     */
    public void testCheckDatesPeriodeEncoheranceRentreeSortie(){
        log.debug("METHODE : testCheckDatesPeriodeEncoheranceRentreeSortie");
        try{
            final DatePeriodeQO datePeriodeQO = new DatePeriodeQO();
            datePeriodeQO.setDateDebutPlage(DateUtils.creer(2011, Calendar.JANUARY, 1));
            datePeriodeQO.setDateFinPlage(DateUtils.creer(2011, Calendar.JULY, 1));
            datePeriodeQO.setDateSortie(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
            datePeriodeQO.setDateRentree(DateUtils.creer(2011, Calendar.JULY, 1));
            datePeriodeQO.setPlageExistante("true");
            anneeScolaireService.checkDatesPeriode(datePeriodeQO);
        }catch (MetierException e) {
            log.debug("ERREUR - ME : {0}", e.getMessage());
        }
    }

    /**
     * testCheckDatesPeriodeEncoheranceDebutFinPlage().
     * 
     * teste l'appel à la méthode anneeScolaireService.checkDatesPeriode(DatePeriodeQO) 
     * 
     * avec un parametre non null, aucune données membres nulles,
     * et incohérance sur les dates de début et fin de plage.
     *  dateSortie -1/07/2011
     *  dateRentree - 1/09/2010
     *  dateDebutPlage - 1/07/2011
     *  dateFinPlage - 1/01/2011
     *  plageExistante - "true"
     * 
     * fonctionne --> MetierException --> Echec contrôle nouvelle plage jours chomés
     * 
     */
    public void testCheckDatesPeriodeEncoheranceDebutFinPlage(){
        log.debug("METHODE : testCheckDatesPeriodeEncoheranceDebutFinPlage");
        try{
            final DatePeriodeQO datePeriodeQO = new DatePeriodeQO();
            datePeriodeQO.setDateDebutPlage(DateUtils.creer(2011, Calendar.JULY, 1));
            datePeriodeQO.setDateFinPlage(DateUtils.creer(2011, Calendar.JANUARY, 1));
            datePeriodeQO.setDateRentree(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
            datePeriodeQO.setDateSortie(DateUtils.creer(2011, Calendar.JULY, 1));
            datePeriodeQO.setPlageExistante("true");
            anneeScolaireService.checkDatesPeriode(datePeriodeQO);
        }catch (MetierException e) {
            log.debug("ERREUR - ME : {0}", e.getMessage());
        }
    }

    /**
     * testCheckDatesPeriodeEncoheranceRentreeDebutPlage().
     * 
     * teste l'appel à la méthode anneeScolaireService.checkDatesPeriode(DatePeriodeQO) 
     * 
     * avec un parametre non null, aucune données membres nulles,
     * et incohérance sur les dates d'entrée et début de plage.
     *  dateSortie -1/07/2011
     *  dateRentree - 1/09/2010
     *  dateDebutPlage - 1/01/2010
     *  dateFinPlage - 1/07/2011
     *  plageExistante - "true"
     * 
     * fonctionne --> MetierException --> Echec contrôle nouvelle plage jours chomés
     * 
     */
    public void testCheckDatesPeriodeEncoheranceRentreeDebutPlage(){
        log.debug("METHODE : testCheckDatesPeriodeEncoheranceRentreeDebutPlage");
        try{
            final DatePeriodeQO datePeriodeQO = new DatePeriodeQO();
            datePeriodeQO.setDateDebutPlage(DateUtils.creer(2010, Calendar.JANUARY, 1));
            datePeriodeQO.setDateFinPlage(DateUtils.creer(2011, Calendar.JULY, 1));
            datePeriodeQO.setDateRentree(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
            datePeriodeQO.setDateSortie(DateUtils.creer(2011, Calendar.JULY, 1));
            datePeriodeQO.setPlageExistante("true");
            anneeScolaireService.checkDatesPeriode(datePeriodeQO);
        }catch (MetierException e) {
            log.debug("ERREUR - ME : {0}", e.getMessage());
        }
    }

    /**
     * testCheckDatesPeriodeEncoheranceSortieFinPlage().
     * 
     * teste l'appel à la méthode anneeScolaireService.checkDatesPeriode(DatePeriodeQO) 
     * 
     * avec un parametre non null, aucune données membres nulles,
     * et incohérance sur les dates de sortie et fin de plage.
     *  dateSortie -1/07/2011
     *  dateRentree - 1/09/2010
     *  dateDebutPlage - 1/01/2011
     *  dateFinPlage - 1/07/2012
     *  plageExistante - "true"
     * 
     * fonctionne --> MetierException --> Echec contrôle nouvelle plage jours chomés
     * 
     */
    public void testCheckDatesPeriodeEncoheranceSortieFinPlage(){
        log.debug("METHODE : testCheckDatesPeriodeEncoheranceSortieFinPlage");
        try{
            final DatePeriodeQO datePeriodeQO = new DatePeriodeQO();
            datePeriodeQO.setDateDebutPlage(DateUtils.creer(2011, Calendar.JANUARY, 1));
            datePeriodeQO.setDateFinPlage(DateUtils.creer(2012, Calendar.JULY, 1));
            datePeriodeQO.setDateRentree(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
            datePeriodeQO.setDateSortie(DateUtils.creer(2011, Calendar.JULY, 1));
            datePeriodeQO.setPlageExistante("true");
            anneeScolaireService.checkDatesPeriode(datePeriodeQO);
        }catch (MetierException e) {
            log.debug("ERREUR - ME : {0}", e.getMessage());
        }
    }

    /**
     * testCheckDatesPeriodeEncoheranceSortieRentreeDebutFinPlage().
     * 
     * teste l'appel à la méthode anneeScolaireService.checkDatesPeriode(DatePeriodeQO) 
     * 
     * avec un parametre non null, aucune données membres nulles,
     * et incohérance sur toutes les dates.
     *  dateSortie -1/09/2011
     *  dateRentree - 3/09/2011
     *  dateDebutPlage - 1/01/2013
     *  dateFinPlage - 1/07/2009
     *  plageExistante - "true"
     * 
     * fonctionne --> MetierException --> Echec contrôle nouvelle plage jours chomés
     * 
     */
    public void testCheckDatesPeriodeEncoheranceSortieRentreeDebutFinPlage(){
        log.debug("METHODE : testCheckDatesPeriodeEncoheranceSortieRentreeDebutFinPlage");
        try{
            final DatePeriodeQO datePeriodeQO = new DatePeriodeQO();
            datePeriodeQO.setDateDebutPlage(DateUtils.creer(2013, Calendar.JANUARY, 1));
            datePeriodeQO.setDateFinPlage(DateUtils.creer(2009, Calendar.JULY, 1));
            datePeriodeQO.setDateRentree(DateUtils.creer(2011, Calendar.SEPTEMBER, 3));
            datePeriodeQO.setDateSortie(DateUtils.creer(2011, Calendar.SEPTEMBER, 1));
            datePeriodeQO.setPlageExistante("true");
            anneeScolaireService.checkDatesPeriode(datePeriodeQO);
        }catch (MetierException e) {
            log.debug("ERREUR - ME : {0}", e.getMessage());
        }
    }

    /**
     * testCheckDatesPeriodeSortieRentreeProcheDebutFinPlageJournee().
     * 
     * teste l'appel à la méthode anneeScolaireService.checkDatesPeriode(DatePeriodeQO) 
     * 
     * avec un parametre non null, aucune données membres nulles,
     * et incohérance sur toutes les dates.
     *  dateSortie -3/09/2011
     *  dateRentree - 1/09/2011
     *  dateDebutPlage - 2/09/2011
     *  dateFinPlage - 2/09/2011
     *  plageExistante - "true"
     * 
     * fonctionne --> MetierException --> Echec contrôle nouvelle plage jours chomés
     * 
     */
    public void testCheckDatesPeriodeSortieRentreeProcheDebutFinPlageJournee(){
        log.debug("METHODE : testCheckDatesPeriodeSortieRentreeProcheDebutFinPlageJournee");
        try{
            final DatePeriodeQO datePeriodeQO = new DatePeriodeQO();
            datePeriodeQO.setDateDebutPlage(DateUtils.creer(2011, Calendar.SEPTEMBER, 2));
            datePeriodeQO.setDateFinPlage(DateUtils.creer(2011, Calendar.SEPTEMBER, 2));
            datePeriodeQO.setDateRentree(DateUtils.creer(2011, Calendar.SEPTEMBER, 1));
            datePeriodeQO.setDateSortie(DateUtils.creer(2011, Calendar.SEPTEMBER, 3));
            datePeriodeQO.setPlageExistante("true");
            anneeScolaireService.checkDatesPeriode(datePeriodeQO);
        }catch (MetierException e) {
            log.debug("ERREUR - ME : {0}", e.getMessage());
        }
    }

    /**
     * testSavePeriodeVacanceNull().
     * 
     * teste l'appel à la méthode anneeScolaireService.savePeriodeVacance(PeriodeVacanceQO).
     * 
     * avec le parametre null.
     * 
     * fonctionne --> Assert.isNotNull sur le parametre.
     * 
     */
    public void testSavePeriodeVacanceNull(){
        log.debug("METHODE : testSavePeriodeVacanceNull");
        try {
            anneeScolaireService.savePeriodeVacance(null);
        }catch (AssertionException e) {
            log.debug("ERREUR ASSERT - {0}", e.getMessage());
        }catch (MetierException e) {
            log.debug("ERREUR ME - {0}", e.getMessage());
        }
    }

    /**
     * testSavePeriodeVacanceChampsNull().
     * 
     * teste l'appel à la méthode anneeScolaireService.savePeriodeVacance(PeriodeVacanceQO).
     * 
     * avec le parametre non null, mais les champs null.
     * 
     * fonctionne --> Assert.isNotNull sur id.
     */
    public void testSavePeriodeVacanceChampsNull(){
        log.debug("METHODE : testSavePeriodeVacanceChampsNull");
        final PeriodeVacanceQO vac = new PeriodeVacanceQO(null, null);
        try {
            anneeScolaireService.savePeriodeVacance(vac);
        }catch (AssertionException e) {
            log.debug("ERREUR ASSERT - {0}", e.getMessage());
        }catch (MetierException e) {
            log.debug("ERREUR ME - {0}", e.getMessage());
        }
    }

    /**
     * testSavePeriodeVacanceIdNull().
     * 
     * teste l'appel à la méthode anneeScolaireService.savePeriodeVacance(PeriodeVacanceQO).
     * 
     * avec le parametre non null,
     * id = null.
     * periodeVacances = "haha y'a pas de vacances cette année !!!!!!"
     * 
     * @throws MetierException exception potentielle qui doit etre bloquante dans ce cas
     * 
     * fonctionne --> Assert.isNotNull sur id.
     * 
     */
    public void testSavePeriodeVacanceIdNull() throws MetierException{
        log.debug("METHODE : testSavePeriodeVacanceIdNull");
        final PeriodeVacanceQO vac = new PeriodeVacanceQO(null, "haha y'a pas de vacances cette année !!!!!!");
        try {
            anneeScolaireService.savePeriodeVacance(vac);
        }catch (AssertionException e) {
            log.debug("ERREUR ASSERT - {0}", e.getMessage());
        }
    }

    /**
     * testSavePeriodeVacancePeriodeVacancesNull().
     * 
     * teste l'appel à la méthode anneeScolaireService.savePeriodeVacance(PeriodeVacanceQO).
     * 
     * avec le parametre non null,
     * id = 3.
     * periodeVacances = null
     * @throws MetierException exception potentielle qui doit etre bloquante dans ce cas
     *  
     *  fonction --> pas d'erreur.
     *  
     */
    public void testSavePeriodeVacancePeriodeVacancesNull() throws MetierException{
        log.debug("METHODE : testSavePeriodeVacancePeriodeVacancesNull");
        final PeriodeVacanceQO vac = new PeriodeVacanceQO(3, null);
            anneeScolaireService.savePeriodeVacance(vac);
    }
    /**
     * testSavePeriodeVacancePeriodeVacancesVide().
     * 
     * teste l'appel à la méthode anneeScolaireService.savePeriodeVacance(PeriodeVacanceQO).
     * 
     * avec le parametre non null,
     * id = 3.
     * periodeVacances = ""
     * 
     * @throws MetierException exception potentielle qui doit etre bloquante dans ce cas
     *    
     *  fonction --> pas d'erreur.
     */
    public void testSavePeriodeVacancePeriodeVacancesVide() throws MetierException{
        log.debug("METHODE : testSavePeriodeVacancePeriodeVacancesVide");
        final PeriodeVacanceQO vac = new PeriodeVacanceQO(3, "");
            anneeScolaireService.savePeriodeVacance(vac);
    }
    
    /**
     * testSavePeriodeVacanceNonNull().
     * 
     * teste l'appel à la méthode anneeScolaireService.savePeriodeVacance(PeriodeVacanceQO).
     * 
     * avec le parametre non null,
     * id = 2.
     * periodeVacances = "haha y'a pas de vacances cette année !!!!!!"
     * 
     * @throws MetierException exception potentielle qui doit etre bloquante dans ce cas.
     * 
     */
    public void testSavePeriodeVacanceNonNull() throws MetierException{
        log.debug("METHODE : testSavePeriodeVacanceNonNull");
        Boolean genereException = true;
        final PeriodeVacanceQO vac = new PeriodeVacanceQO(2, "haha y'a pas de vacances cette année !!!!!!");
        
            try {
                anneeScolaireService.savePeriodeVacance(vac);
                genereException = false;
            } catch (MetierException e) {
                log.debug("ERROR ME - {0}", e.getMessage());
            } catch (AssertionException e){
                log.debug("ERROR Assert - {0}", e.getMessage());
            }
            if(! genereException){
                log.debug("BIG ERROR : Le code n'est pas bon, le controle de la string de la période de vacances n'est pas fait.");
                throw new MetierException(new ConteneurMessage(),
                        "Le code n'est pas bon, le controle de la string de la période de vacances n'est pas fait.");
            }
    }
    
    /**
     * testSaveOuvertureENTNull().
     * 
     * teste l'appel à la méthode anneeScolaireService.saveOuvertureENT(OuvertureQO),
     * avec le parametre null.
     * 
     * @throws MetierException exception bloquante pour ce cas.
     * 
     * fonctionne --> Assert.isNotNull --> sur le parametre.
     */
    public void testSaveOuvertureENTNull() throws MetierException{
        log.debug("testSaveOuvertureENTNull");
        
        try{
            anneeScolaireService.saveOuvertureENT(null);
        }catch (AssertionException e) {
            log.debug("ERROR - ASSERT : {0}", e.getMessage());
        }        
    }
    
    /**
     * testSaveOuvertureENTChampsNull().
     * 
     * teste l'appel à la méthode anneeScolaireService.saveOuvertureENT(OuvertureQO),
     * avec le parametre non null, mais ses champs null.
     * 
     * @throws MetierException exception bloquante pour ce cas.
     * 
     * fonctionne --> Assert.isNotNull --> sur le parametre.id
     */
    public void testSaveOuvertureENTChampsNull() throws MetierException{
        log.debug("testSaveOuvertureENTChampsNull");
        final OuvertureQO op = new OuvertureQO(null, null);
        try{
            anneeScolaireService.saveOuvertureENT(op);
        }catch (AssertionException e) {
            log.debug("ERROR - ASSERT : {0}", e.getMessage());
        }        
    }
    
    /**
     * testSaveOuvertureENTOuvertureNull().
     * 
     * teste l'appel à la méthode anneeScolaireService.saveOuvertureENT(OuvertureQO),
     * avec le parametre non null,
     * id = 3,
     * vraiOuFauxOuvert = null.
     * 
     * @throws MetierException exception bloquante pour ce cas.
     * 
     * fonctionne --> pas d'erreur transformation bool=null --> false
     */
    public void testSaveOuvertureENTOuvertureNull() throws MetierException{
        log.debug("testSaveOuvertureENTOuvertureNull");
        final OuvertureQO op = new OuvertureQO(3, null);
        try{
            anneeScolaireService.saveOuvertureENT(op);
        }catch (AssertionException e) {
            log.debug("ERROR - ASSERT : {0}", e.getMessage());
        }        
    }
    
    /**
     * testSaveOuvertureENTNonNull().
     * 
     * teste l'appel à la méthode anneeScolaireService.saveOuvertureENT(OuvertureQO),
     * avec le parametre non null,
     * id = 2,
     * vraiOuFauxOuvert = null.
     * 
     * @throws MetierException exception bloquante pour ce cas.
     * 
     * fonctionne --> pas d'erreur
     */
    public void testSaveOuvertureENTNonNull() throws MetierException{
        log.debug("testSaveOuvertureENTNonNull");
        final OuvertureQO op = new OuvertureQO(2, false);
        try{
            anneeScolaireService.saveOuvertureENT(op);
        }catch (AssertionException e) {
            log.debug("ERROR - ASSERT : {0}", e.getMessage());
        }        
    }
    
    
    
}
