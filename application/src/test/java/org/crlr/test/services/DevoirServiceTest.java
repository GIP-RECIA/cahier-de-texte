/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirServiceTest.java,v 1.10 2010/04/01 11:06:15 ent_breyton Exp $
 */

package org.crlr.test.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.devoir.RechercheDevoirQO;
import org.crlr.dto.application.devoir.ResultatRechercheDevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.DevoirService;
import org.crlr.test.AbstractMetierTest;
import org.crlr.utils.Assert;
import org.crlr.utils.AssertionException;
import org.apache.commons.collections.CollectionUtils;
import org.crlr.utils.DateUtils;

/**
 * Test Junit pour DevoirService.
 *
 * @author $author$
 * @version $Revision: 1.10 $
 */
public class DevoirServiceTest extends AbstractMetierTest {
    /** Le service à utiliser. */
    private DevoirService devoirService;

    /**
     * Test sans erreur de getListeTypeDevoir.
     *
     * @throws MetierException .
     * 
     * 
     */
    public void testFindListeTypeDevoir() throws MetierException {
        try {
            final ResultatDTO<List<TypeDevoirDTO>> resultat =
                devoirService.findListeTypeDevoir(null);
            final List<TypeDevoirDTO> typeDevoirDTOs = resultat.getValeurDTO();

            assertEquals(2, typeDevoirDTOs.size());
            Boolean un = false;
            Boolean deux = false;
            for (TypeDevoirDTO typeDevoirDTO : typeDevoirDTOs) {
                final int idDevoir = typeDevoirDTO.getId();
                final String codeDevoir = typeDevoirDTO.getCode();
                final String libelleDevoir = typeDevoirDTO.getLibelle();
                switch (idDevoir) {
                    case 1:
                        assertFalse(un);
                        un = true;
                        assertEquals(codeDevoir, "TDEV1");
                        assertEquals(libelleDevoir, "DM");
                        break;
                    case 2:
                        assertFalse(deux);
                        deux = true;
                        assertEquals(codeDevoir, "TDEV2");
                        assertEquals(libelleDevoir, "Leçon");
                        break;
                    default:
                        throw new MetierException("testgetListeTypeDevoir NOK : A renvoyer un type de devoir incorrect");
                }
            }
        } catch (MetierException e) {
            throw new MetierException("testgetListeTypeDevoir NOK : a renvoyer une exception",
                                      e);
        } catch (AssertionException e){
            log.debug("Erreur ok : assert.isNotNull sur idEtablissement \n {0}", e.getMessage());
        }
    }
    
    /**
     * testFindListeTypeDevoirIdEtabNonNull()
     * 
     * Test l'appel à la fonction getListeTypeDevoir(int idEtablissement).
     *
     * idEntablissement = 987654321
     *
     * @throws MetierException .
     * 
     * 
     */
    public void testFindListeTypeDevoirIdEtabNonNull() throws MetierException {
        log.debug("*-*-* METHODE *-*-* : testFindListeTypeDevoirIdEtabNonNull");
        try {
            final ResultatDTO<List<TypeDevoirDTO>> resultat =
                devoirService.findListeTypeDevoir(987654321);
            final List<TypeDevoirDTO> typeDevoirDTOs = resultat.getValeurDTO();

            if(CollectionUtils.isEmpty(typeDevoirDTOs)){
                log.debug("renvois de liste vide pour un etablissement inexistant");
            }else{
                throw new MetierException("Une erreur est apparut : renvoie de données pour un établissement inexistant !");
            }
        } catch (MetierException e) {
            throw new MetierException("testgetListeTypeDevoir NOK : a renvoyer une exception",
                                      e);
        }
    }
    
    /**
     * testFindListeTypeDevoirIdEtabExistant()
     * 
     * Test l'appel à la fonction getListeTypeDevoir(int idEtablissement).
     *
     * idEntablissement = 37
     *
     * @throws MetierException .
     * 
     * 
     */
    public void testFindListeTypeDevoirIdEtabExistant() throws MetierException {
        log.debug("*-*-* METHODE *-*-* : testFindListeTypeDevoirIdEtabExistant");
        try {
            final ResultatDTO<List<TypeDevoirDTO>> resultat =
                devoirService.findListeTypeDevoir(37);
            final List<TypeDevoirDTO> typeDevoirDTOs = resultat.getValeurDTO();

            assertEquals(4, typeDevoirDTOs.size());
            Boolean id109 = false;
            Boolean id110 = false;
            Boolean id111 = false;
            Boolean id443 = false;
            for(TypeDevoirDTO dev : typeDevoirDTOs){
                final int idDevoir = dev.getId();
                final String codeDevoir = dev.getCode();
                final String libelleDevoir = dev.getLibelle();
                switch(idDevoir){
                case 109 : 
                    assertFalse(id109);
                    id109 = true;
                    assertEquals(codeDevoir, "TD109");
                    assertEquals(libelleDevoir, "Devoir maison");
                    break;
                case 110 : 
                    assertFalse(id110);
                    id110 = true;
                    assertEquals(codeDevoir, "TD110");
                    assertEquals(libelleDevoir, "Exercice(s)");
                    break;
                case 111 : 
                    assertFalse(id111);
                    id111 = true;
                    assertEquals(codeDevoir, "TD111");
                    assertEquals(libelleDevoir, "Autre");
                    break;
                case 443 : 
                    assertFalse(id443);
                    id443 = true;
                    assertEquals(codeDevoir, "TD443");
                    assertEquals(libelleDevoir, "contrôle");
                    break;
                default :
                    throw new MetierException("la recherche a renvoyé un devoir inconnu");
                }
            }
            
        } catch (MetierException e) {
            throw new MetierException("testgetListeTypeDevoir NOK : a renvoyer une exception",
                                      e);
        }
    }
    
    /**
     * testListeDevoirAffichageNull().
     * 
     * test de la méthode listeDevoirAffichage(RechercheDevoirQO rechercheDevoirQO),
     * 
     * avec un parametre NULL.
     * @throws MetierException l'exception bloquante dans ce cas.
     */
    public void testListeDevoirAffichageNull() throws MetierException{
        log.debug("*-*-* Méthode *-*-* : testListeDevoirAffichageNull");
        try{
        devoirService.listeDevoirAffichage(null);
        }catch (AssertionException e) {
            log.debug("ERROR ok : verif Assert.isNotNull : ", e.getMessage());
        }
    }
    
    /**
     * testListeDevoirAffichageChampsNull().
     * 
     * test de la méthode listeDevoirAffichage(RechercheDevoirQO rechercheDevoirQO),
     * 
     * avec un parametre non NULL,
     * mais les champs null.
     * 
     * @throws MetierException l'exception bloquante dans ce cas.
     */
    public void testListeDevoirAffichageChampsNull() throws MetierException{
        log.debug("*-*-* Méthode *-*-* : testListeDevoirAffichageChampsNull");
        final RechercheDevoirQO rechercheDevoir = new RechercheDevoirQO();
        rechercheDevoir.setGroupeClasseSelectionne(null);
        rechercheDevoir.setIdEleve(null);
        rechercheDevoir.setIdEnseignant(null);
        rechercheDevoir.setIdEtablissement(null);
        rechercheDevoir.setIdInspecteur(null);
        try{
            final ResultatDTO<List<ResultatRechercheDevoirDTO>> result = devoirService.listeDevoirAffichage(rechercheDevoir);
            if(result == null){
                log.debug("ok retour null");
            }else{
                throw new MetierException("ERROR bloquante, avec null, y'a un retour !");
            }
        }catch (AssertionException e) {
            log.debug("ERROR ok : verif Assert.isNotNull : ", e.getMessage());
        }
    }
    
    /**
     * testListeDevoirAffichageIdInspecteurNonNull().
     * 
     * test de la méthode listeDevoirAffichage(RechercheDevoirQO rechercheDevoirQO),
     * 
     * avec un parametre non NULL,
     * les champs null, sauf idInspecteur.
     * 
     * @throws MetierException l'exception bloquante dans ce cas.
     * 
     *  test pas ok ==> pas de vérification de nullité sur rechercheDevoirQO.getGroupeClasseSelectionne()
     *  dans DevoirFacade
     */
    public void testListeDevoirAffichageIdInspecteurNonNull() throws MetierException{
        log.debug("*-*-* Méthode *-*-* : testListeDevoirAffichageIdInspecteurNonNull");
        final RechercheDevoirQO rechercheDevoir = new RechercheDevoirQO();
        rechercheDevoir.setGroupeClasseSelectionne(null);
        rechercheDevoir.setIdEleve(null);
        rechercheDevoir.setIdEnseignant(null);
        rechercheDevoir.setIdEtablissement(null);
        rechercheDevoir.setIdInspecteur(1);
        try{
            devoirService.listeDevoirAffichage(rechercheDevoir);
                throw new MetierException("ERROR bloquante, la fonction arrive jusque là," +
                        " alors quelle a besoin de chose qui sont mis à null");
        }catch (AssertionException e) {
            log.debug("ERROR ok : verif Assert.isNotNull : ", e.getMessage());
        }
    }
    
    /**
     * testListeDevoirAffichageIdEleveNonNull().
     * 
     * test de la méthode listeDevoirAffichage(RechercheDevoirQO rechercheDevoirQO),
     * 
     * avec un parametre non NULL,
     * les champs null, sauf idEleve.
     * 
     * @throws MetierException l'exception bloquante dans ce cas.
     * 
     * test pas ok ==> pas de vérification de nullité sur rechercheDevoirQO.getGroupeClasseSelectionne()
     *  dans DevoirFacade
     */
    public void testListeDevoirAffichageIdEleveNonNull() throws MetierException{
        log.debug("*-*-* Méthode *-*-* : testListeDevoirAffichageIdEleveNonNull");
        final RechercheDevoirQO rechercheDevoir = new RechercheDevoirQO();
        rechercheDevoir.setGroupeClasseSelectionne(null);
        rechercheDevoir.setIdEleve(1);
        rechercheDevoir.setIdEnseignant(null);
        rechercheDevoir.setIdEtablissement(null);
        rechercheDevoir.setIdInspecteur(null);
        try{
            devoirService.listeDevoirAffichage(rechercheDevoir);
                throw new MetierException("ERROR bloquante, la fonction arrive jusque là," +
                        " alors quelle a besoin de chose qui sont mis à null");
        }catch (AssertionException e) {
            log.debug("ERROR ok : verif Assert.isNotNull : ", e.getMessage());
        }
    }

    /**
     * testListeDevoirAffichageIdEnseignantNonNull().
     * 
     * test de la méthode listeDevoirAffichage(RechercheDevoirQO rechercheDevoirQO),
     * 
     * avec un parametre non NULL,
     * les champs null, sauf idEnseignant.
     * 
     * @throws MetierException l'exception bloquante dans ce cas.
     * 
     * test pas ok ==> pas de vérification de nullité sur rechercheDevoirQO.getGroupeClasseSelectionne()
     *  dans DevoirFacade
     */
    public void testListeDevoirAffichageIdEnseignantNonNull() throws MetierException{
        log.debug("*-*-* Méthode *-*-* : testListeDevoirAffichageIdEnseignantNonNull");
        final RechercheDevoirQO rechercheDevoir = new RechercheDevoirQO();
        rechercheDevoir.setGroupeClasseSelectionne(null);
        rechercheDevoir.setIdEleve(null);
        rechercheDevoir.setIdEnseignant(1);
        rechercheDevoir.setIdEtablissement(null);
        rechercheDevoir.setIdInspecteur(null);
        try{
            devoirService.listeDevoirAffichage(rechercheDevoir);
                throw new MetierException("ERROR bloquante, la fonction arrive jusque là," +
                        " alors quelle a besoin de chose qui sont mis à null");
        }catch (AssertionException e) {
            log.debug("ERROR ok : verif Assert.isNotNull : ", e.getMessage());
        }
    }

    
    /**
     * Methode de creation d'un RechercheDevoirQO pour un enseignant. Tous les
     * parametres sont setter dans le nouveau RechercheDevoirQO.
     *
     * @param idEnseigant .
     * @param typeGroupe .
     * @param code .
     * @param jourCourant .
     * @param list .
     *
     * @return Le nouveau RechercheDevoirQO.
     */
    
     private RechercheDevoirQO createQoEnseignant(int idEnseigant, TypeGroupe typeGroupe,
                                                 String code, Date jourCourant,
                                                 List<GroupeDTO> list) {
        final RechercheDevoirQO rechercheDevoirQO = new RechercheDevoirQO();

        //idEns
        rechercheDevoirQO.setIdEnseignant(idEnseigant);

        //Dates
        final Date premierJour = DateUtils.setJourSemaine(jourCourant, Calendar.MONDAY);
        final Date dernierJour = DateUtils.setJourSemaine(jourCourant, Calendar.SUNDAY);
        rechercheDevoirQO.setPremierJourSemaine(premierJour);
        rechercheDevoirQO.setDernierJourSemaine(dernierJour);
        rechercheDevoirQO.setJourCourant(jourCourant);

        //Classe-Groupe
        rechercheDevoirQO.setTypeGroupeSelectionne(typeGroupe);
        final GroupesClassesDTO groupesClassesDTO = new GroupesClassesDTO();
        groupesClassesDTO.setCode(code);
        rechercheDevoirQO.setGroupeClasseSelectionne(groupesClassesDTO);

        rechercheDevoirQO.setListeGroupeDTO(list);

        return rechercheDevoirQO;
    }
     
     /**
      * testAffichageDevoirEnsOk().
      * @throws MetierException .
      * 
      * peut pas tester, pas de données en base 
      */
      public void testAffichageDevoirEnsOk() throws MetierException{
         log.debug("*-*-* METHODE *-*-* : testAffichageDevoirEnsOk");
         
         final List<GroupeDTO> list = new ArrayList<GroupeDTO>();
         final GroupeDTO groupe1 = new GroupeDTO();
         groupe1.setCode("GRP2311");
         groupe1.setId(2311);
         groupe1.setIntitule("5 B GROU");
         list.add(groupe1);
         final Date jourCourant = DateUtils.creer(2010, Calendar.JANUARY, 32);
         final RechercheDevoirQO rechercheDevoirQO = createQoEnseignant(396, TypeGroupe.CLASSE, "CLA1246", jourCourant, list);
         
         final ResultatDTO<List<ResultatRechercheDevoirDTO>> resultat =
             devoirService.listeDevoirAffichage(rechercheDevoirQO);
         final List<ResultatRechercheDevoirDTO> resultatRechercheDevoirDTOs =
             resultat.getValeurDTO();
         
         log.debug("nombre de remonté de la base : {0}", resultatRechercheDevoirDTOs.size());
     }

    /**
     * Methode de creation d'un RechercheDevoirQO pour un eleve. Tous les
     * parametres sont setter dans le nouveau RechercheDevoirQO.
     *
     * @param idEleve .
     * @param jourCourant .
     *
     * @return Le nouveau RechercheDevoirQO.
     */
    private RechercheDevoirQO createQoEleve(int idEleve, Date jourCourant) {
        final RechercheDevoirQO rechercheDevoirQO = new RechercheDevoirQO();

        //idEns
        rechercheDevoirQO.setIdEleve(idEleve);

        //Dates
        final Date premierJour = DateUtils.setJourSemaine(jourCourant, Calendar.MONDAY);
        final Date dernierJour = DateUtils.setJourSemaine(jourCourant, Calendar.SUNDAY);
        rechercheDevoirQO.setPremierJourSemaine(premierJour);
        rechercheDevoirQO.setDernierJourSemaine(dernierJour);
        rechercheDevoirQO.setJourCourant(jourCourant);

        return rechercheDevoirQO;
    }

    /**
     * Mutateur devoirService.
     *
     * @param devoirService Le devoirService à modifier
     */
    public void setDevoirService(DevoirService devoirService) {
        this.devoirService = devoirService;
    }

    

    /**
     * Test sans erreur de AffichageDevoir pour un eleve.
     *
     * @throws MetierException .
     * 
     * erreur sur assertEquals(3, resultatRechercheDevoirDTOs.size());
     * 
     * il n'y as pas d'infos en base.
     */
    public void testAffichageElDevoirOk() throws MetierException {
        final RechercheDevoirQO rechercheDevoirQO =
            createQoEleve(1, DateUtils.creer(2008, Calendar.OCTOBER, 31));
        final ResultatDTO<List<ResultatRechercheDevoirDTO>> resultat =
            devoirService.listeDevoirAffichage(rechercheDevoirQO);
        final List<ResultatRechercheDevoirDTO> resultatRechercheDevoirDTOs =
            resultat.getValeurDTO();

        assertEquals(3, resultatRechercheDevoirDTOs.size());
        Boolean un = false;
        Boolean deux = false;
        Boolean trois = false;

        for (ResultatRechercheDevoirDTO resultatRechercheDevoirDTO : resultatRechercheDevoirDTOs) {
            final int idDevoir = resultatRechercheDevoirDTO.getIdDevoir();
            final String codeSeance = resultatRechercheDevoirDTO.getCodeSeance();
            switch (idDevoir) {
                case 1:
                    assertFalse(un);
                    un = true;
                    assertEquals("SEA1", codeSeance);
                    break;
                case 2:
                    assertFalse(deux);
                    deux = true;
                    assertEquals("SEA1", codeSeance);
                    break;
                case 3:
                    assertFalse(trois);
                    trois = true;
                    assertEquals("SEA2", codeSeance);
                    break;
                default:
                    throw new MetierException("testAffichageElDevoirOk NOK: A renvoyer un devoir incorrect");
            }
        }
    }

    /**
     * Test de AffichageDevoir pour un eleve qui n'existe pas.
     *
     * @throws MetierException .
     */
    public void testAffichageElDevoirEleveExistePas()
                                             throws MetierException {
        final RechercheDevoirQO rechercheDevoirQO =
            createQoEleve(100, DateUtils.creer(2008, Calendar.OCTOBER, 31));

        try {
            final ResultatDTO<List<ResultatRechercheDevoirDTO>> resultat =
                devoirService.listeDevoirAffichage(rechercheDevoirQO);
            if (resultat.getValeurDTO().size() == 0) {
                return;
            }
        } catch (Exception e) {
            log.debug("testAffichageElDevoirEleveExistePas OK: l'eleve n'existe pas");
            return;
        }
        throw new MetierException("testAffichageElDevoirEleveExistePas NOK: l'eleve n'existe pas");
    }

    /**
     * Test de AffichageDevoir pour un eleve sans classe.
     *
     * @throws MetierException .
     */
    public void testAffichageElDevoirEleveSansClasse()
                                              throws MetierException {
        final RechercheDevoirQO rechercheDevoirQO =
            createQoEleve(3, DateUtils.creer(2008, Calendar.OCTOBER, 31));

        try {
            final ResultatDTO<List<ResultatRechercheDevoirDTO>> resultat =
                devoirService.listeDevoirAffichage(rechercheDevoirQO);
            if (resultat.getValeurDTO().size() == 0) {
                return;
            }
        } catch (Exception e) {
            log.debug("testAffichageElDevoirEleveSansClasse OK: l'eleve n'a pas de classe");
            return;
        }
        throw new MetierException("testAffichageElDevoirEleveSansClasse NOK: l'eleve n'a pas de classe");
    }

    /**
     * Test de AffichageDevoir pour un eleve sans groupe.
     *
     * @throws MetierException .
     */
    public void testAffichageElDevoirEleveSansGroupe()
                                              throws MetierException {
        final RechercheDevoirQO rechercheDevoirQO =
            createQoEleve(2, DateUtils.creer(2008, Calendar.OCTOBER, 31));

        try {
            final ResultatDTO<List<ResultatRechercheDevoirDTO>> resultat =
                devoirService.listeDevoirAffichage(rechercheDevoirQO);
            if (resultat.getValeurDTO().size() == 0) {
                return;
            }
        } catch (Exception e) {
            log.debug("testAffichageElDevoirEleveSansGroupe OK: l'eleve n'a pas de groupe");
            return;
        }
        throw new MetierException("testAffichageElDevoirEleveSansGroupe NOK: l'eleve n'a pas de groupe");
    }

    /**
     * testSaveTypeDevoirNull().
     * 
     * teste l'appel à la fonction saveTypeDevoir(final TypeDevoirQO typeDevoirQO).
     * 
     * avec parametre null;
     * @throws MetierException l'exception bloquante !
     */
    public void testSaveTypeDevoirNull() throws MetierException{
        log.debug("*-*-* METHODE *-*-* : testSaveTypeDevoirNull");
        try{
            devoirService.saveTypeDevoir(null);
        }catch (AssertionException e) {
            log.debug("Error ok : Assert.isNotNull sur le parametre");
        }
    }
    
    /**
     * testSaveTypeDevoirLibelleTypeDevoirNull().
     * 
     * teste l'appel à la fonction saveTypeDevoir(final TypeDevoirQO typeDevoirQO).
     * 
     * avec parametre non null;
     * libelle = null
     * idEtab = 37
     * 
     * @throws MetierException l'exception bloquante !
     * 
     *  fonctionne ==> ne peut pas inseré de libelle vide/null !
     */
    public void testSaveTypeDevoirLibelleTypeDevoirNull() throws MetierException{
        log.debug("*-*-* METHODE *-*-* : testSaveTypeDevoirLibelleTypeDevoirNull");
        final TypeDevoirQO typeDev = new TypeDevoirQO();
        typeDev.setIdEtablissement(37);
        typeDev.setLibelle(null);
        
        try{
            devoirService.saveTypeDevoir(typeDev);
            log.debug("ERREUR BLOQUANTE : le libelle est vide et a été inseré");
            throw new MetierException("ERREUR BLOQUANTE : le libelle est vide et a été inseré");
        }catch (AssertionException e) {
            log.debug("Error ok : Assert.isNotNull sur le parametre");
        }catch (MetierException e) {
            log.debug("{0}", e.getMessage());
        }
    }
    
    /**
     * testSaveTypeDevoirIdEtabInexistant().
     * 
     * teste l'appel à la fonction saveTypeDevoir(final TypeDevoirQO typeDevoirQO).
     * 
     * avec parametre non null;
     * 
     * idEtab = 987654321 ==> inexistant
     * libelle = "libelle a la noix"
     * 
     * @throws MetierException l'exception bloquante !
     * 
     * ==> gros crash ==> insertion idEtab inexistant.
     */
    public void testSaveTypeDevoirIdEtabInexistant() throws MetierException{
        log.debug("*-*-* METHODE *-*-* : testSaveTypeDevoirIdEtabInexistant");
        final TypeDevoirQO typeDev = new TypeDevoirQO();
        typeDev.setIdEtablissement(987654321);
        typeDev.setLibelle("libelle a la noix");
            devoirService.saveTypeDevoir(typeDev);
            log.debug("ERREUR BLOQUANTE : l'etablissement n'existe pas");
            throw new MetierException("ERREUR BLOQUANTE : l'etablissement n'existe pas");
    }

    /**
     * testSaveDevoirNull().
     * 
     * teste l'appel à la méthode : saveDevoir(final SaveDevoirQO saveDevoirQO).
     * 
     * parametre = null;
     * 
     * @throws MetierException exception bloquante.
     * 
     * Ne fonctionne pas ==> pas de vérification sur le parametre !
     */
    public void testSaveDevoirNull() throws MetierException{
        log.debug("*-*-* METHODE *-*-* : testSaveDevoirNull");
        try{
            devoirService.saveDevoir(null);
        }catch (NullPointerException e) {
            log.debug("Gros probleme ==> NullPointerException ==> sur parametre");
            throw new MetierException("ERROR BLOQUANTE ==> NullPointerException sur parametre");  
        }
                  
    }
    
    /**
     * teste l'appel à la fonction findDetailDevoir(id).
     * id = null
     */
    public void testFindDetailDevoirNull(){
        try{
            devoirService.findDetailDevoir(null);
        }catch (AssertionException e) {
            log.debug("testFindDetailDevoirNull OK - AssertionExcpetion : {0}", e.getMessage());
        }
    }
    
    /**
     * teste l'appel à la fonction findDetailDevoir(id).
     * id = 987654321
     */
    public void testFindDetailDevoirIdInexistant(){
            final DevoirDTO dev = devoirService.findDetailDevoir(987654321);
            Assert.isNull("id", dev.getId());
    }
    
    /**
     * teste l'appel à la fonction findDetailDevoir(id).
     * id = 1
     */
    public void testFindDetailDevoirIdExistant(){
            final DevoirDTO dev = devoirService.findDetailDevoir(4);
            Assert.isNotNull("id", dev.getId());
            assertEquals((Integer)4, dev.getId());
            assertEquals((Integer)2, dev.getIdSeance());
           // assertEquals((Integer)292, dev.getIdTypeDevoir());
            assertEquals("", dev.getDescription());
            assertEquals("Devoir 1", dev.getIntitule());   
    }
}
