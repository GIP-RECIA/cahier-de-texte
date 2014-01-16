/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequenceServiceTest.java,v 1.11 2009/07/30 15:09:26 ent_breyton Exp $
 */

package org.crlr.test.services;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.sequence.RechercheSequencePopupQO;
import org.crlr.dto.application.sequence.RechercheSequenceQO;
import org.crlr.dto.application.sequence.ResultatRechercheSequenceDTO;
import org.crlr.dto.application.sequence.SaveSequenceQO;
import org.crlr.dto.application.sequence.SaveSequenceSimplifieeQO;
import org.crlr.dto.application.sequence.SequenceAffichageQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.SequenceService;
import org.crlr.test.AbstractMetierTest;
import org.crlr.utils.AssertionException;
import org.crlr.utils.DateUtils;

/**
 * Test Junit pour SequenceService.
 *
 * @author $author$
 * @version $Revision: 1.11 $
 */
public class SequenceServiceTest extends AbstractMetierTest {
    /** Service séquence. */
    private SequenceService sequenceService;

    /**
     * set le service.
     *
     * @param sequenceService le service
     */
    public void setSequenceService(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    /**
     * test de checkUnicite.
     * avec parametre null.
     * @throws MetierException bloquante.
     */
    public void testCheckUniciteNull() throws MetierException{
        try{
            sequenceService.checkUnicite(null);
        }catch(AssertionException e){
            log.debug("testCheckUniciteNull OK : AssertionException : {0}", e.getMessage());
        }catch(NullPointerException e){
            log.debug("testCheckUniciteNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
            throw new MetierException("testCheckUniciteNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
        }
    }
    
    /**
     * test de checkUnicite.
     * avec les champs du parametre null.
     * @throws MetierException bloquante.
     * 
     * Pas d'assertion sur le typeClasseGroupe ==> ici cela entraine createQuery("").setParameter()...;
     * 
     */
    public void testCheckUniciteChampsNull() throws MetierException{
        final SaveSequenceQO saveSequenceQO = new SaveSequenceQO();
        try{
            sequenceService.checkUnicite(saveSequenceQO);
        }catch(AssertionException e){
            log.debug("testCheckUniciteChampsNull OK : AssertionException : {0}", e.getMessage());
        }catch(NullPointerException e){
            log.debug("testCheckUniciteChampsNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
            throw new MetierException("testCheckUniciteNull NOK : NullPointerException : {0}", (Object) e.getStackTrace());
        }
    }
    
    /**
     * test de checkUnicite.
     * avec typeClasseGroupe.
     * @throws MetierException bloquante.
     * 
     * Pas d'assertion sur le typeClasseGroupe ==> ici cela entraine createQuery("").setParameter()...;
     * 
     */
    public void testCheckUnicitetypeClasseGroupe() throws MetierException{
        final SaveSequenceQO saveSequenceQO = new SaveSequenceQO();
        try{
            sequenceService.checkUnicite(saveSequenceQO);
            //assertNull(retour);
            
        }catch(AssertionException e){
            log.debug("testCheckUnicitetypeClasseGroupe OK : AssertionException : {0}", e.getMessage());
        }catch(NullPointerException e){
            log.debug("testCheckUnicitetypeClasseGroupe NOK : NullPointerException : {0}", (Object) e.getStackTrace());
            throw new MetierException("testCheckUnicitetypeClasseGroupe NOK : NullPointerException : {0}", (Object) e.getStackTrace());
        }
        throw new MetierException("testCheckUnicitetypeClasseGroupe NOK : sans aucune information il arrive à checker l'unicité !");
    }  



    /**
     * Methode pour créer un RechercheSequenceQO. Tous les paramètres sont setter
     * dans le RechercheSequenceQO.
     *
     * @param codeEnseignement .
     * @param codeClasseGroupe .
     * @param typeGroupe .
     * @param dateDebut .
     * @param dateFin .
     * @param idEnseignant .
     *
     * @return Le nouveau RechercheSequenceQO.
     */
    private RechercheSequenceQO createRechercheSequenceQO(String codeEnseignement,
                                                          String codeClasseGroupe,
                                                          TypeGroupe typeGroupe,
                                                          Date dateDebut, Date dateFin,
                                                          Integer idEnseignant) {
        final RechercheSequenceQO rechercheSequenceQO = new RechercheSequenceQO();

        rechercheSequenceQO.setCodeEnseignement(codeEnseignement);
        rechercheSequenceQO.setIdEnseignant(idEnseignant);
        rechercheSequenceQO.setCodeClasseGroupe(codeClasseGroupe);
        rechercheSequenceQO.setTypeGroupe(typeGroupe);
        rechercheSequenceQO.setDateDebut(dateDebut);
        rechercheSequenceQO.setDateFin(dateFin);
        return rechercheSequenceQO;
    }

    
    /**
     * Test de findSequence si l'enseignement n'existe pas.
     *
     * @throws MetierException .
     */
    public void testFindSequenceEnseignementExistePas()
                                               throws MetierException {
        final RechercheSequenceQO rechercheSequenceQO =
            createRechercheSequenceQO("ENS20", "CLA2760", TypeGroupe.CLASSE,
                    DateUtils.creer(2010, Calendar.SEPTEMBER, 01),
                    DateUtils.creer(2011, Calendar.JULY, 1), 1335);
        rechercheSequenceQO.setIdEtablissement(98);
        try {
            sequenceService.findSequence(rechercheSequenceQO);
        } catch (final MetierException e) {
            return;
        }
        throw new MetierException("testFindSequenceEnseignementExistePas NOK: l'enseignement 20 n'existe pas");
    }

    /**
     * Test de findSequence si la classe n'existe pas.
     *
     * @throws MetierException .
     */
    public void testFindSequenceClasseExistePas()
                                         throws MetierException {
        final RechercheSequenceQO rechercheSequenceQO =
            createRechercheSequenceQO("L1000", "HEHEHE1", TypeGroupe.CLASSE,
                    DateUtils.creer(2010, Calendar.SEPTEMBER, 01),
                    DateUtils.creer(2011, Calendar.JULY, 1), 1335);
        rechercheSequenceQO.setIdEtablissement(98);
        try {
            sequenceService.findSequence(rechercheSequenceQO);
        } catch (final MetierException e) {
            return;
        }
        throw new MetierException("testFindSequenceClasseExistePas NOK: la classe CLA9 n'existe pas");
    }

    /**
     * Test de findSequence si le groupe n'existe pas.
     *
     * @throws MetierException .
     */
    public void testFindSequenceGroupeExistePas()
                                         throws MetierException {
        final RechercheSequenceQO rechercheSequenceQO =
            createRechercheSequenceQO("L1000", "HOHO1", TypeGroupe.GROUPE,
                    DateUtils.creer(2010, Calendar.SEPTEMBER, 01),
                    DateUtils.creer(2011, Calendar.JULY, 1), 1335);
        rechercheSequenceQO.setIdEtablissement(98);
        try {
            sequenceService.findSequence(rechercheSequenceQO);
        } catch (final MetierException e) {
            return;
        }
        throw new MetierException("testFindSequenceGroupeExistePas NOK: le groupe CLA9 n'existe pas");
    }

    /**
     * Test de findSequence si l'enseignant n'existe pas.
     *
     * @throws MetierException .
     */
    public void testFindSequenceEnseignantExistePas()
                                             throws MetierException {
        final RechercheSequenceQO rechercheSequenceQO =
            createRechercheSequenceQO("L1000", "CLA2760", TypeGroupe.CLASSE,
                    DateUtils.creer(2010, Calendar.SEPTEMBER, 01),
                    DateUtils.creer(2011, Calendar.JULY, 1), 987654321);

        rechercheSequenceQO.setIdEtablissement(98);
        try {
            sequenceService.findSequence(rechercheSequenceQO);
        } catch (final MetierException e) {
            return;
        }
        throw new MetierException("testFindSequenceEnseignantExistePas NOK: l'enseignant n'existe pas");
    }

    /**
     * Test de findSequence s'il y a une date de fin sans date de debut.
     *
     * @throws MetierException .
     */
    public void testFindSequenceDateFinSansDateDebut()
                                              throws MetierException {
        final RechercheSequenceQO rechercheSequenceQO =
            createRechercheSequenceQO("L1000", "CLA2760", TypeGroupe.CLASSE,null,
                DateUtils.creer(2011, Calendar.JULY, 1), 1335);
        rechercheSequenceQO.setIdEtablissement(98);
        try {
            sequenceService.findSequence(rechercheSequenceQO);
        } catch (final MetierException e) {
            return;
        }
        throw new MetierException("testFindSequenceDateFinSansDateDebut NOK:Il n'y a pas de date de debut mais une date de fin");
    }

    /**
     * Test de findSequence si la date de fin est > à l a date de debut.
     *
     * @throws MetierException .
     */
    public void testFindSequenceDateFinAntDateDebut()
                                             throws MetierException {
        final RechercheSequenceQO rechercheSequenceQO =
            createRechercheSequenceQO("L1000", "CLA2760", TypeGroupe.CLASSE,
                    DateUtils.creer(2011, Calendar.JULY, 1),
                    DateUtils.creer(2010, Calendar.SEPTEMBER, 1), 1335);
            rechercheSequenceQO.setIdEtablissement(98);
        try {
            sequenceService.findSequence(rechercheSequenceQO);
        } catch (final MetierException e) {
            return;
        }
        throw new MetierException("testFindSequenceDateFinAntDateDebut NOK:La date de fin est anterieur à la date de debut");
    }

    /**
     * Test de findSequence si aucune Date n'est passée.
     *
     * @throws MetierException .
     */
    public void testFindSequenceSansDate() throws MetierException {
        final RechercheSequenceQO rechercheSequenceQO =
            createRechercheSequenceQO("L1000", "CLA2760", TypeGroupe.CLASSE, null, null, 1335);
        rechercheSequenceQO.setIdEtablissement(98);
        final ResultatDTO<List<ResultatRechercheSequenceDTO>> resultat =
            sequenceService.findSequence(rechercheSequenceQO);
        log.debug("nb ret : {0}", resultat.getValeurDTO().size());
        assertEquals(resultat.getValeurDTO().size(), 1);
    }

    /**
     * Test de findSequence si l'enseignant n'a pas les droits sur l'enseignement.
     *
     * @throws MetierException .
     * 
     * juste pas besoin de précisé l'etablissement ! d'ailleurs bizare ce message :
     * 
     * ce prof est mono établissement et mono enseignement !
     * 
     */
    public void testFindSequenceEnseignementPasDroits()
                                               throws MetierException {
        final RechercheSequenceQO rechercheSequenceQO =
            createRechercheSequenceQO("L1000", "CLA2760", TypeGroupe.CLASSE,
                DateUtils.creer(2010, Calendar.SEPTEMBER, 01),
                DateUtils.creer(2011, Calendar.JULY, 1), 1335);

        try {
            sequenceService.findSequence(rechercheSequenceQO);
        } catch (final MetierException e) {
            return;
        }
        throw new MetierException("testFindSequenceEnseignementPasDroits NOK:L'enseignant n'a pas les droits sur l'enseignement 4");
    }

    /**
     * Test de findSequence si l'enseignant n'a pas les droits sur la classe.
     *
     * @throws MetierException .
     */
    public void testFindSequenceClassePasDroits()
                                         throws MetierException {
        final RechercheSequenceQO rechercheSequenceQO =
                createRechercheSequenceQO("L1000", "CLA4000", TypeGroupe.CLASSE,
                    DateUtils.creer(2010, Calendar.SEPTEMBER, 01),
                    DateUtils.creer(2011, Calendar.JULY, 1), 1335);
        rechercheSequenceQO.setIdEtablissement(98);
        try {
            sequenceService.findSequence(rechercheSequenceQO);
        } catch (final MetierException e) {
            return;
        }
        throw new MetierException("testFindSequenceClassePasDroits NOK:L'enseignant 2 n'a pas les droits sur le groupe 1");
    }

    /**
     * Test sans erreur de findSequencePopup.
     *
     * @throws MetierException .
     */
    public void testfindSequencePopup() throws MetierException {
        final RechercheSequencePopupQO rechercheSequencePopupQO =
            new RechercheSequencePopupQO();
        rechercheSequencePopupQO.setIdEnseignant(1335);
        rechercheSequencePopupQO.setIdAnneeScolaire(1);
        rechercheSequencePopupQO.setIdEtablissement(98);

        final ResultatDTO<List<SequenceDTO>> resultat =
            sequenceService.findSequencePopup(rechercheSequencePopupQO);
        final List<SequenceDTO> listSequenceDTO = resultat.getValeurDTO();
        log.debug("nb ret : {0}",listSequenceDTO.size());
        assertEquals(listSequenceDTO.size(), 3);

        Boolean un = false;
        Boolean deux = false;
        Boolean trois = false;
        for (final SequenceDTO sequenceDTO : listSequenceDTO) {
            final int idSeq = sequenceDTO.getId();
            final String codeSeq = sequenceDTO.getCode();
            final String descrSeq = sequenceDTO.getDescription();
            final String intSeq = sequenceDTO.getIntitule();
            final Date dateDebut = sequenceDTO.getDateDebut();
            final Date dateFin = sequenceDTO.getDateFin();
            switch (idSeq) {
                case 1:
                    assertFalse(un);
                    un = true;
                    assertEquals(codeSeq, "SEQ1");
                    assertEquals(descrSeq, "Séquence générée automatiquement " +
                            "pour la saisie simplifiée de séance sur l'enseignement " +
                            "HISTOIRE GEOGRAPHIE et la classe 5G1");
                    assertEquals(intSeq, "Séquence HISTOIRE GEOGRAPHIE / 5G1");
                    assertEquals(dateDebut, DateUtils.creer(2010, Calendar.SEPTEMBER, 01));
                    assertEquals(dateFin, DateUtils.creer(2011, Calendar.JULY, 01));
                    break;
                case 2:
                    assertFalse(deux);
                    deux = true;
                    assertEquals(codeSeq, "SEQ2");
                    assertEquals(descrSeq, "Séquence générée automatiquement "+
                    "pour la saisie simplifiée de séance sur l'enseignement "+
                    "HISTOIRE GEOGRAPHIE et la classe 5G2");
                    assertEquals(intSeq, "Séquence HISTOIRE GEOGRAPHIE / 5G2");
                    assertEquals(dateDebut, DateUtils.creer(2010, Calendar.SEPTEMBER, 01));
                    assertEquals(dateFin, DateUtils.creer(2011, Calendar.JULY, 01));
                    break;
                case 3:
                    assertFalse(trois);
                    trois = true;
                    assertEquals(codeSeq, "SEQ3");
                    assertEquals(descrSeq, "Séquence générée automatiquement"+
                    " pour la saisie simplifiée de séance sur l'enseignement"+
                    " HISTOIRE GEOGRAPHIE et la classe 5G3");
                    assertEquals(intSeq, "Séquence HISTOIRE GEOGRAPHIE / 5G3");
                    assertEquals(dateDebut, DateUtils.creer(2010, Calendar.SEPTEMBER, 01));
                    assertEquals(dateFin, DateUtils.creer(2011, Calendar.JULY, 01));
                    break;
                default:
                    throw new MetierException("testfindSequencePopup NOK:Renvoie une sequence qui n'existe pas");
            }
        }
    }

    /**
     * Test de findSequencePopup s'il n'y a pas de sequences associées à l'enseignant.
     *
     * @throws MetierException .
     */
    public void testfindSequencePopup1() throws MetierException {
        final RechercheSequencePopupQO rechercheSequencePopupQO =
            new RechercheSequencePopupQO();
        rechercheSequencePopupQO.setIdEnseignant(1);

        final ResultatDTO<List<SequenceDTO>> resultat =
            sequenceService.findSequencePopup(rechercheSequencePopupQO);
        final List<SequenceDTO> listSequenceDTO = resultat.getValeurDTO();
        assertEquals(listSequenceDTO.size(), 0);
    }

    /**
     * Test de findSequencePopup si l'enseignant n'existe pas.
     *
     * @throws MetierException .
     */
    public void testfindSequencePopupEnseignantExistePas() throws MetierException {
        final RechercheSequencePopupQO rechercheSequencePopupQO =
            new RechercheSequencePopupQO();
        rechercheSequencePopupQO.setIdEnseignant(987654321);

        final ResultatDTO<List<SequenceDTO>> resultat =
            sequenceService.findSequencePopup(rechercheSequencePopupQO);
        final List<SequenceDTO> listSequenceDTO = resultat.getValeurDTO();
        assertEquals(listSequenceDTO.size(), 0);
    }
    
   

   

    /**
     * Test de findSequenceAffichage avec une sequence qui n'existe pas.
     *
     * @throws MetierException .
     * 
     * Je ne sais pas se qu'il est attendu, mais comme la valeur 
     * 
     * de retour de la fonction findSequenceAffichage est initialisée,
     * 
     * le NULL ne peut etre pris comme comparaison !
     * 
     * sinon cela retourne un objet vide.
     */
    public void testfindSequenceAffichageFausseSequence()
                                                 throws MetierException {
        final SequenceAffichageQO sequenceAffichageQO = new SequenceAffichageQO();
        sequenceAffichageQO.setId(1000);
        try {
            final SequenceDTO resultat = sequenceService.findSequenceAffichage(sequenceAffichageQO);
            if (resultat == null ) {
                return;
            }else{
                log.debug("resultat {0} : {1},\n {2}",resultat, resultat.getId(), resultat.getCode());
            }
        } catch (final MetierException e) {
            return;
        }
        log.debug("testfindSequenceAffichageFausseSequence NOK");
        throw new MetierException("testfindSequenceAffichageFausseSequence NOK: La sequence n'existe pas");
    }

    /**
     * test saveSequenceSaisieSimplifiee.
     * 
     * @throws MetierException l'exception bloquante.
     */
    public void testSaveSequenceSaisieSimplifiee() throws MetierException{
        final SaveSequenceSimplifieeQO saveSequenceSimplifieeQO = new SaveSequenceSimplifieeQO();
        saveSequenceSimplifieeQO.setIdEnseignant(829);
        final AnneeScolaireDTO anneeScolaireDTO =new AnneeScolaireDTO();
        anneeScolaireDTO.setId(1);
        anneeScolaireDTO.setDateRentree(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
        anneeScolaireDTO.setDateSortie(DateUtils.creer(2011, Calendar.JULY, 1));
        saveSequenceSimplifieeQO.setAnneeScolaireDTO(anneeScolaireDTO);
        saveSequenceSimplifieeQO.setIdEtablissement(98);
        saveSequenceSimplifieeQO.setVraiOuFauxSaisieSimplifiee(true);
        
        sequenceService.saveSequenceSaisieSimplifiee(saveSequenceSimplifieeQO);
    }
    
    /**
     * test de saveSequenceSaisieSimplifiee avec la saisie simplifié déjà activée.
     * 
     * fonctionne
     * 
     * @throws MetierException .
     */
    public void testSaveSequenceSaisieSimplifieeExistante() throws MetierException{
        final SaveSequenceSimplifieeQO saveSequenceSimplifieeQO = new SaveSequenceSimplifieeQO();
        saveSequenceSimplifieeQO.setIdEnseignant(1335);
        final AnneeScolaireDTO anneeScolaireDTO =new AnneeScolaireDTO();
        anneeScolaireDTO.setId(1);
        anneeScolaireDTO.setDateRentree(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
        anneeScolaireDTO.setDateSortie(DateUtils.creer(2011, Calendar.JULY, 1));
        saveSequenceSimplifieeQO.setAnneeScolaireDTO(anneeScolaireDTO);
        saveSequenceSimplifieeQO.setIdEtablissement(98);
        saveSequenceSimplifieeQO.setVraiOuFauxSaisieSimplifiee(true);
        try{
            sequenceService.saveSequenceSaisieSimplifiee(saveSequenceSimplifieeQO);
        }catch (MetierException e) {
            log.debug("testSaveSequenceSaisieSimplifieeExistante OK");
            return;
        }
        throw new MetierException("testSaveSequenceSaisieSimplifieeExistante NOK : {0}",
                "la saisie simplifié est déjà active");
    }
    
    /**
     * test de la saveSequenceSaisieSimplifiee() 
     * avec un enseignant pas rattaché à un établissement
     * 
     * fonction car ne rajoute rien ne base, genere un message comme quoi la modification à été faite
     * or ne devrait !
     * aucune information sur le fait que l'enseignant n'est pas rattaché
     * 
     * @throws MetierException .
     */
    public void testSaveSequenceSaisieSimplifieeEnsPasEtab() throws MetierException{
        final SaveSequenceSimplifieeQO saveSequenceSimplifieeQO = new SaveSequenceSimplifieeQO();
        saveSequenceSimplifieeQO.setIdEnseignant(1335);
        final AnneeScolaireDTO anneeScolaireDTO =new AnneeScolaireDTO();
        anneeScolaireDTO.setId(1);
        anneeScolaireDTO.setDateRentree(DateUtils.creer(2010, Calendar.SEPTEMBER, 1));
        anneeScolaireDTO.setDateSortie(DateUtils.creer(2011, Calendar.JULY, 1));
        saveSequenceSimplifieeQO.setAnneeScolaireDTO(anneeScolaireDTO);
        saveSequenceSimplifieeQO.setIdEtablissement(99);
        saveSequenceSimplifieeQO.setVraiOuFauxSaisieSimplifiee(true);
            final ResultatDTO<Integer> ret = sequenceService.saveSequenceSaisieSimplifiee(saveSequenceSimplifieeQO);
            log.debug("retour : {0}", ret.getValeurDTO());
            if(ret.getValeurDTO() == null){
                log.debug("testSaveSequenceSaisieSimplifieeExistante OK");
                return;
            }else{
                    log.debug("testSaveSequenceSaisieSimplifieeExistante NOK");
                    throw new MetierException("testSaveSequenceSaisieSimplifieeExistante NOK : {0}",
                            "l'enseignant n'est pas rattaché a cet établissement");
            }
    }
    
}
