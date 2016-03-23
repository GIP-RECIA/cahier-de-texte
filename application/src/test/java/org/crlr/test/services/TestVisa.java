/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: InspectionServiceTest,v 1.0 2011/02/24 10:31:30  $
 */
package org.crlr.test.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.ArchiveSeanceDTO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.seance.ConsulterSeanceQO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.dto.application.seance.RechercheVisaSeanceQO;
import org.crlr.dto.application.seance.SaveSeanceQO;
import org.crlr.dto.application.sequence.RechercheSequenceQO;
import org.crlr.dto.application.visa.DateListeVisaSeanceDTO;
import org.crlr.dto.application.visa.RechercheVisaQO;
import org.crlr.dto.application.visa.ResultatRechercheVisaSeanceDTO;
import org.crlr.dto.application.visa.TypeVisa;
import org.crlr.dto.application.visa.VisaDTO;
import org.crlr.dto.application.visa.VisaDTO.VisaProfil;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.VisaFacadeService;
import org.crlr.services.AnneeScolaireService;
import org.crlr.services.ConfidentialiteService;
import org.crlr.services.DevoirService;
import org.crlr.services.EtablissementService;
import org.crlr.services.SeanceService;
import org.crlr.services.SequenceService;
import org.crlr.services.VisaService;
import org.crlr.test.AbstractMetierTest;
import org.crlr.web.application.control.EnseignementControl;
import org.crlr.web.application.control.devoir.DevoirControl;
import org.crlr.web.contexte.ContexteApplication;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.dto.FileUploadDTO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test JUnit 
 * @author egroning
 * @version 1.0
 */
/**
 * @author G-CG34-FRMP
 * 
 */
public class TestVisa extends AbstractMetierTest {

    @Autowired
    EtablissementService etablissementService;
    
    @Autowired
    VisaService visaService;

    @Autowired
    VisaFacadeService visaFacade;

    @Autowired
    SequenceService sequenceService;

    @Autowired
    SeanceService seanceService;

    @Autowired
    AnneeScolaireService anneeScolaireService;
    
    @Autowired
    ConfidentialiteService confidentialiteService;
    
    @Autowired
    DevoirService devoirService;

    //Données viennent de CRLR
    
    //select * from cahier_enseignant where nom = 'FAUCHIER';
    private static final int ID_ENSEIGNANT = 7438;
    private static final String UID = "kfj000vo";
    
    //select * from cahier_enseignant ens inner join cahier_etab_enseignant etabEns on etabEns.id_enseignant = ens.id
    //where ens.nom = 'FAUCHIER';
    private static final int ID_ETAB = 38;

    private static final String PJ_PATH = "/Documents du CTN";

    // select * from cahier_classe where id=393;
    private static final int ID_CLASSE = 1109;

    // select * from cahier_enseignement where id = 131;
    private static final int ID_ENSEIGNEMENT = 8;

    private static final int ID_GROUPE_2 = 1113;
    private static final int ID_ENSEIGNEMENT_2 = 8;

    private static final int ID_GROUPE_3 = 14889;
    private static final int ID_ENSEIGNEMENT_3 = 8;

    private static final String LONG_TEXT = "&lt;Latex&gt; \\lim_{ \\prod_{{8}={7}}^{9} \\sum_{{7}={6}}^{8}7\\to8} " +
    		"\\tanh^{-1} \\left(8\\right)&lt;/Latex&gt; Lorsque dans le cours " +
    		"des événements humains, il devient nécessaire pour un peuple de dissoudre les liens politiques qui l'ont attaché à un autre et de" +
    		" prendre, parmi les puissances de la Terre, la place séparée et égale à " +
    		"laquelle les lois de la nature et du Dieu de la nature lui donnent droit," +
    		" le respect dû à l'opinion de l'humanité oblige à déclarer les causes qui" +
    		" le déterminent à la séparation.Nous tenons ces vérités comme allant d'elles-mêmes" +
    		" : tous les hommes sont créés égaux ; ils sont dotés par le Créateur de certains " +
    		"droits inaliénables ; parmi ces droits se trouvent la vie, la liberté et la recherche " +
    		"du bonheur. Les gouvernements sont établis parmi les hommes pour garantir ces droits, " +
    		"et leur juste pouvoir émane du consentement des gouvernés. Toutes les fois qu'une forme " +
    		"de gouvernement devient destructive de ce but, le peuple a le droit de la changer ou de " +
    		"l'abolir et d'établir un nouveau gouvernement, en le fondant sur les " +
    		"principes et en l'organisant en la forme qui lui paraîtront les plus propres à lui donner " +
    		"la sûreté et le bonheur. La prudence enseigne, à la vérité, que les " +
    		"gouvernements établis depuis longtemps ne doivent pas être changés pour des causes légères et " +
    		"passagères, et l'expérience de tous les temps a montré, en effet, " +
    		"que les hommes sont plus disposés à tolérer des maux supportables qu'à se faire justice à" +
    		" eux-mêmes en abolissant les formes auxquelles ils sont " +
    		"accoutumés. Mais lorsqu'une longue suite d'abus et d'usurpations, tendant invariablement au même " +
    		"but, marque le dessein de les soumettre au despotisme " +
    		"absolu, il est de leur droit, il est de leur devoir de rejeter un tel gouvernement et de pourvoir, " +
    		"par de nouvelles sauvegardes, à leur sécurité future. " +
    		"Telle a été la patience de ces Colonies, et telle est aujourd'hui la nécessité qui les force à changer " +
    		"leurs anciens systèmes de gouvernement. L'histoire du roi " +
    		"actuel de Grande-Bretagne est l'histoire d'une série d'injustices et d'usurpations " +
    		"répétées, qui toutes avaient pour but direct l'établissement d'une tyrannie " +
    		"absolue sur ces États. Pour le prouver, soumettons les faits au monde impartial : JE SUIS LA FIN";

    
    private static final String ARCHIVAGE_TEST_DESC_1 = "Modifée 1 desc";
    private static final String ARCHIVAGE_TEST_DESC_2 = "Modifée 2 desc";
    
    private static final String SEANCE_TEST_DESC_1 = "Crée 1 déscription" ;
    private static final String SEANCE_TEST_DESC_2 = "Crée 2 déscription";
    
    static {
        ContexteApplication.CONFIG_PROPERTIES_URL = "test-config.properties";
    }
    
    @org.junit.Test
    public void test() throws Exception {
        log.info("Test");
        
        List<EtablissementDTO> list = 
                confidentialiteService.findListeEtablissement().getValeurDTO();
        
        log.debug("list {}", list.size());
        
        
    }
    
    public void testCreerSeanceApresVisa() throws MetierException {
        deleteVisas();

        deleteSeances();
        
        AnneeScolaireDTO anneeScolaire = new AnneeScolaireDTO();
        anneeScolaire
                .setDateSortie(org.crlr.utils.DateUtils.creer(2013, 10, 1));
        anneeScolaire.setId(1);

        // visas pour 3 cahiers de texte (enseignement / enseigant /
        // class-groupe)
        List<VisaDTO> listeVisas = null;
        VisaDTO visaDir = null;
        VisaDTO visaIns = null;
        

        listeVisas = getVisas(VisaProfil.ENTDirecteur);
        visaDir = findVisa(listeVisas, VisaProfil.ENTDirecteur);
        listeVisas = getVisas(VisaProfil.ENTInspecteur);
        visaIns = findVisa(listeVisas, VisaProfil.ENTInspecteur);

        assertFalse(visaDir.getEstPerime());
        assertFalse(visaIns.getEstPerime());
        assertNull(visaDir.getDateMaj());
        assertNull(visaIns.getDateMaj());
        assertNull(visaDir.getDateVisa());
        assertNull(visaIns.getDateVisa());


        // 1. créer une séances

        Integer idSeance1 = creerSeance(anneeScolaire);
        modifierSeancePourArchivage1(idSeance1);
        
        
        // 2.  Créer visa type mémo
        visaDir.setTypeVisa(TypeVisa.MEMO);
        visaIns.setTypeVisa(TypeVisa.MEMO);
        
        listeVisas.clear();
        listeVisas.add(visaDir);
        listeVisas.add(visaIns);
        visaService.saveListeVisa(listeVisas);
        
        
        // 3.  une requete
        RechercheVisaSeanceQO rechercheVisaSeanceQO = new RechercheVisaSeanceQO();

        rechercheVisaSeanceQO.setIdAnneeScolaire(anneeScolaire.getId());
        rechercheVisaSeanceQO.setIdEnseignant(ID_ENSEIGNANT);
        rechercheVisaSeanceQO.setIdEnseignement(ID_ENSEIGNEMENT);
        rechercheVisaSeanceQO.setAfficheNonVisees(true);
        rechercheVisaSeanceQO.setAfficheVisees(true);
        rechercheVisaSeanceQO.setAffichePerimees(true);
        rechercheVisaSeanceQO.setProfil(Profil.INSPECTION_ACADEMIQUE);
        rechercheVisaSeanceQO.setIdEtablissement(ID_ETAB);

        List<DateListeVisaSeanceDTO> listeSeances = new ArrayList<DateListeVisaSeanceDTO>();

        try {
            listeSeances = visaService.findVisaSeance(rechercheVisaSeanceQO)
                    .getValeurDTO();
        } catch (MetierException ex) {
            assertNotNull(ex);
        }

        assertEquals(1, listeSeances.size());
        assertEquals(1, listeSeances.get(0).getListeVisaSeance().size());
        
        //4. créer une deuxième séance
        
        Integer idSeance2 = creerSeancePourTestCreerSeanceApresVisa(anneeScolaire);
        
            listeSeances = visaService.findVisaSeance(rechercheVisaSeanceQO)
                    .getValeurDTO();
        

        assertEquals(1, listeSeances.size());
        assertEquals(2, listeSeances.get(0).getListeVisaSeance().size());
        assertEquals(idSeance2, listeSeances.get(0).getListeVisaSeance().get(0).getId());
        assertEquals(idSeance1, listeSeances.get(0).getListeVisaSeance().get(1).getId());
        
        //Verifier seance 1 est visée ; seance 2 n'est pas visée
        
        rechercheVisaSeanceQO.setAfficheNonVisees(false);
        rechercheVisaSeanceQO.setAfficheVisees(true);
        rechercheVisaSeanceQO.setAffichePerimees(false);
        
        listeSeances = visaService.findVisaSeance(rechercheVisaSeanceQO)
                .getValeurDTO();
        
        assertEquals(1, listeSeances.size());
        assertEquals(1, listeSeances.get(0).getListeVisaSeance().size());
        assertEquals(idSeance1, listeSeances.get(0).getListeVisaSeance().get(0).getId());
        
        rechercheVisaSeanceQO.setAfficheNonVisees(true);
        rechercheVisaSeanceQO.setAfficheVisees(false);
        rechercheVisaSeanceQO.setAffichePerimees(false);
        
        listeSeances = visaService.findVisaSeance(rechercheVisaSeanceQO)
                .getValeurDTO();
        
        assertEquals(1, listeSeances.size());
        assertEquals(1, listeSeances.get(0).getListeVisaSeance().size());
        ResultatRechercheVisaSeanceDTO visaSeanceDTO = listeSeances.get(0).getListeVisaSeance().get(0);
        
        assertEquals(idSeance2, visaSeanceDTO.getId());
        assertTrue(visaSeanceDTO.getAlerteDateVisaInspecteur());
        assertNull(visaSeanceDTO.getVisaInspecteur().getDateVisa());
        assertEquals(visaSeanceDTO.getVisaInspecteur().getDateMaj().getTime(), visaSeanceDTO.getDateMaj().getTime());
        assertFalse(visaSeanceDTO.getAlerteDateMaj());
        
    }
    
    
    public void testSupprimerSeances() throws MetierException {
        deleteVisas();

        deleteSeances();
        
        AnneeScolaireDTO anneeScolaire = new AnneeScolaireDTO();
        anneeScolaire
                .setDateSortie(org.crlr.utils.DateUtils.creer(2013, 10, 1));
        anneeScolaire.setId(1);

        // visas pour 3 cahiers de texte (enseignement / enseigant /
        // class-groupe)
        List<VisaDTO> listeVisas = null;
        VisaDTO visaDir = null;
        VisaDTO visaIns = null;
        

        listeVisas = getVisas(VisaProfil.ENTDirecteur);
        visaDir = findVisa(listeVisas, VisaProfil.ENTDirecteur);
        listeVisas = getVisas(VisaProfil.ENTInspecteur);
        visaIns = findVisa(listeVisas, VisaProfil.ENTInspecteur);

        assertFalse(visaDir.getEstPerime());
        assertFalse(visaIns.getEstPerime());
        assertNull(visaDir.getDateMaj());
        assertNull(visaIns.getDateMaj());
        assertNull(visaDir.getDateVisa());
        assertNull(visaIns.getDateVisa());


        // 1. créer une séances

        Integer idSeance1 = creerSeance(anneeScolaire);
        modifierSeancePourArchivage1(idSeance1);
        
        
        // 2.  Créer visa type mémo
        visaDir.setTypeVisa(TypeVisa.MEMO);
        visaIns.setTypeVisa(TypeVisa.MEMO);
        
        listeVisas.clear();
        listeVisas.add(visaDir);
        listeVisas.add(visaIns);
        visaService.saveListeVisa(listeVisas);
        
        //3.  vérifier archives sont là
        
        ArchiveSeanceDTO archSeanceVisaDir = visaFacade.findArchiveSeance(idSeance1, visaDir.getIdVisa()).getValeurDTO();
        ArchiveSeanceDTO archSeanceVisaIns = visaFacade.findArchiveSeance(idSeance1, visaIns.getIdVisa()).getValeurDTO();
        
        assertNotNull(archSeanceVisaDir.getId());
        assertNotNull(archSeanceVisaIns.getId());
        assertEquals(archSeanceVisaDir.getIdSeanceOriginale(), archSeanceVisaIns.getIdSeanceOriginale());
        assertEquals(idSeance1, archSeanceVisaIns.getIdSeanceOriginale());
        
        SeanceDTO seance = new SeanceDTO();
        seance.setId(idSeance1);
        this.seanceService.deleteSeance(seance);
    }

    public void testSeancesParEnseignant() throws MetierException {
        deleteVisas();

        deleteSeances();
        
        AnneeScolaireDTO anneeScolaire = new AnneeScolaireDTO();
        anneeScolaire
                .setDateSortie(org.crlr.utils.DateUtils.creer(2013, 10, 1));
        anneeScolaire.setId(1);

        // visas pour 3 cahiers de texte (enseignement / enseigant /
        // class-groupe)
        List<VisaDTO> listeVisas = null;
        VisaDTO visaDir = null;
        VisaDTO visaIns = null;
        VisaDTO visaDir2 = null;
        VisaDTO visaIns2 = null;
        VisaDTO visaDir3 = null;
        VisaDTO visaIns3 = null;

        listeVisas = getVisas(VisaProfil.ENTDirecteur);
        visaDir = findVisa(listeVisas, VisaProfil.ENTDirecteur);
        visaDir2 = findVisa2(listeVisas, VisaProfil.ENTDirecteur);
        visaDir3 = findVisa3(listeVisas, VisaProfil.ENTDirecteur);
        listeVisas = getVisas(VisaProfil.ENTInspecteur);
        visaIns = findVisa(listeVisas, VisaProfil.ENTInspecteur);
        visaIns2 = findVisa2(listeVisas, VisaProfil.ENTInspecteur);
        visaIns3 = findVisa3(listeVisas, VisaProfil.ENTInspecteur);

        assertFalse(visaDir.getEstPerime());
        assertFalse(visaIns.getEstPerime());
        assertNull(visaDir.getDateMaj());
        assertNull(visaIns.getDateMaj());
        assertNull(visaDir.getDateVisa());
        assertNull(visaIns.getDateVisa());

        assertNull(visaDir2.getDateMaj());
        assertNull(visaIns2.getDateMaj());
        assertNull(visaDir2.getDateVisa());
        assertNull(visaIns2.getDateVisa());

        // 1. créer trois séances

        Integer idSeance1 = creerSeance(anneeScolaire);

        modifierSeancePourSeancesParEnseignant(idSeance1);

        Integer idSeance2 = creerSeance2(anneeScolaire);
        // Ajoute quelques PJ
        modifierSeancePourSeancesParEnseignant(idSeance2);

        Integer idSeance3 = creerSeance3(anneeScolaire);

        modifierSeancePourSeancesParEnseignant(idSeance3);

        RechercheVisaSeanceQO rechercheVisaSeanceQO = new RechercheVisaSeanceQO();

        rechercheVisaSeanceQO.setIdAnneeScolaire(anneeScolaire.getId());
        rechercheVisaSeanceQO.setIdEnseignant(ID_ENSEIGNANT);
        // rechercheVisaSeanceQO.setIdEnseignement(ID_ENSEIGNEMENT);
        rechercheVisaSeanceQO.setAfficheNonVisees(true);
        rechercheVisaSeanceQO.setAfficheVisees(false);
        rechercheVisaSeanceQO.setAffichePerimees(false);
        rechercheVisaSeanceQO.setProfil(Profil.INSPECTION_ACADEMIQUE);
        rechercheVisaSeanceQO.setIdEtablissement(ID_ETAB);

        List<DateListeVisaSeanceDTO> listeSeances = new ArrayList<DateListeVisaSeanceDTO>();

        try {
            listeSeances = visaService.findVisaSeance(rechercheVisaSeanceQO)
                    .getValeurDTO();
        } catch (MetierException ex) {
            assertNotNull(ex);
        }

        assertEquals(1, listeSeances.size());
        assertEquals(3, listeSeances.get(0).getListeVisaSeance().size());

        ResultatRechercheVisaSeanceDTO ligneSeance1 = listeSeances.get(0)
                .getListeVisaSeance().get(2);
        // Plus récent est le première ligne
        ResultatRechercheVisaSeanceDTO ligneSeance2 = listeSeances.get(0)
                .getListeVisaSeance().get(1);
        ResultatRechercheVisaSeanceDTO ligneSeance3 = listeSeances.get(0)
                .getListeVisaSeance().get(0);

        assertNotNull(ligneSeance1.getVisaDirecteur().getIdVisa());
        assertNotNull(ligneSeance1.getVisaInspecteur().getIdVisa());
        assertNotNull(ligneSeance2.getVisaDirecteur().getIdVisa());
        assertNotNull(ligneSeance2.getVisaInspecteur().getIdVisa());

        assertNotNull(ligneSeance1.getVisaDirecteur().getClasseGroupe()
                .getTypeGroupe());
        assertNotNull(ligneSeance1.getVisaInspecteur().getClasseGroupe()
                .getTypeGroupe());
        assertNotNull(ligneSeance2.getVisaDirecteur().getClasseGroupe()
                .getTypeGroupe());
        assertNotNull(ligneSeance2.getVisaInspecteur().getClasseGroupe()
                .getTypeGroupe());

        assertEquals(ligneSeance1.getId(), idSeance1);
        assertEquals(ligneSeance2.getId(), idSeance2);
        assertEquals(ligneSeance3.getId(), idSeance3);

        assertEquals(3, ligneSeance2.getFiles().size());
        assertNotNull(ligneSeance1.getDescriptionHTML());
        assertNotNull(ligneSeance2.getDescriptionHTML());

        // Créer la suite
        // Seance 1 -- perimé inspecteur (simple) ; non visées directeur (memo)
        // Seance 2 -- non visé inspecteur (simple) ; visé directeur (simple)
        // Séance 3 -- visé inspecteur (memo); perimé directeur (memo)

        SeanceDTO seance2DTO = findSeance(idSeance2);
        seance2DTO.setIntitule("Séance2");
        seance2DTO
                .setDescription("non visé inspecteur ; visé directeur (simple)");
        seanceService.modifieSeance(seance2DTO);

        // Créer les visas qui seront perimés
        visaIns.setTypeVisa(TypeVisa.SIMPLE);
        visaDir3.setTypeVisa(TypeVisa.MEMO);

        listeVisas.clear();
        listeVisas.add(visaIns);
        listeVisas.add(visaDir3);
        visaService.saveListeVisa(listeVisas);

        // Modifier séance 1 et 3

        SeanceDTO seance1DTO = findSeance(idSeance1);
        SeanceDTO seance3DTO = findSeance(idSeance3);

        seance1DTO.setIntitule("Séance 1");
        seance1DTO
                .setDescription("Séance actuel.  perimé inspecteur (simple) ; non visées directeur " + LONG_TEXT);
        seance3DTO.setIntitule("Séance 3");
        seance3DTO
                .setDescription("Séance actuel.  visé inspecteur (memo); perimé directeur (memo) " + LONG_TEXT );

        seance3DTO.getDevoirs().remove(0);
        seance3DTO.getFiles().remove(0);
        
        seanceService.modifieSeance(seance1DTO);
        seanceService.modifieSeance(seance3DTO);

        // Créer les visas non perimés
        visaDir2.setTypeVisa(TypeVisa.SIMPLE);
        visaIns3.setTypeVisa(TypeVisa.MEMO);

        listeVisas.clear();
        listeVisas.add(visaDir2);
        listeVisas.add(visaIns3);
        visaService.saveListeVisa(listeVisas);

        // Verification
        rechercheVisaSeanceQO.setAfficheNonVisees(true);
        rechercheVisaSeanceQO.setAfficheVisees(true);
        rechercheVisaSeanceQO.setAffichePerimees(true);
        rechercheVisaSeanceQO.setProfil(Profil.INSPECTION_ACADEMIQUE);

        try {
            listeSeances = visaService.findVisaSeance(rechercheVisaSeanceQO)
                    .getValeurDTO();
        } catch (MetierException ex) {
            assertNotNull(ex);
        }

        assertEquals(1, listeSeances.size());
        assertEquals(3, listeSeances.get(0).getListeVisaSeance().size());

        ligneSeance1 = listeSeances.get(0).getListeVisaSeance().get(2);
        // Plus récent est le première ligne
        ligneSeance2 = listeSeances.get(0).getListeVisaSeance().get(1);
        ligneSeance3 = listeSeances.get(0).getListeVisaSeance().get(0);

        assertEquals(ligneSeance1.getId(), idSeance1);
        assertEquals(ligneSeance2.getId(), idSeance2);
        assertEquals(ligneSeance3.getId(), idSeance3);

        // Verification direction
        rechercheVisaSeanceQO.setAfficheNonVisees(true);
        rechercheVisaSeanceQO.setAfficheVisees(true);
        rechercheVisaSeanceQO.setAffichePerimees(true);
        rechercheVisaSeanceQO.setProfil(Profil.DIRECTION_ETABLISSEMENT);

        try {
            listeSeances = visaService.findVisaSeance(rechercheVisaSeanceQO)
                    .getValeurDTO();
        } catch (MetierException ex) {
            assertNotNull(ex);
        }

        assertEquals(1, listeSeances.size());
        assertEquals(3, listeSeances.get(0).getListeVisaSeance().size());

        ligneSeance1 = listeSeances.get(0).getListeVisaSeance().get(2);
        // Plus récent est le première ligne
        ligneSeance2 = listeSeances.get(0).getListeVisaSeance().get(1);
        ligneSeance3 = listeSeances.get(0).getListeVisaSeance().get(0);

        assertEquals(ligneSeance1.getId(), idSeance1);
        assertEquals(ligneSeance2.getId(), idSeance2);
        assertEquals(ligneSeance3.getId(), idSeance3);

        listeVisas = getVisas(VisaProfil.ENTDirecteur);
        visaDir = findVisa(listeVisas, VisaProfil.ENTDirecteur);
        visaDir2 = findVisa2(listeVisas, VisaProfil.ENTDirecteur);
        visaDir3 = findVisa3(listeVisas, VisaProfil.ENTDirecteur);

        assertTrue(ligneSeance3.getAlerteDateMaj());
        assertEquals(TypeVisa.MEMO, ligneSeance3.getVisaDirecteur()
                .getTypeVisa());

        // Uniquement non visées
        rechercheVisaSeanceQO.setAfficheNonVisees(true);
        rechercheVisaSeanceQO.setAfficheVisees(false);
        rechercheVisaSeanceQO.setAffichePerimees(false);
        rechercheVisaSeanceQO.setProfil(Profil.DIRECTION_ETABLISSEMENT);

        try {
            listeSeances = visaService.findVisaSeance(rechercheVisaSeanceQO)
                    .getValeurDTO();
        } catch (MetierException ex) {
            assertNotNull(ex);
        }

        assertEquals(1, listeSeances.size());
        assertEquals(1, listeSeances.get(0).getListeVisaSeance().size());

        ligneSeance1 = listeSeances.get(0).getListeVisaSeance().get(0);

        assertEquals(idSeance1, ligneSeance1.getId());

    }

    /**
     * Même si'l n'y a pas de visas (date visa et type visa == null), la date de
     * maj devrait être mis à jour. Ce teste est pour le cas plus compliqué de
     * changement de séquence
     * 
     * @throws MetierException
     */
    public void testDateMajChangerSequenceSurUneSeanceSansVisas()
            throws MetierException {
        deleteVisas();

        deleteSeances(); // verifierMultVisaSurMemeSeance();
        
        
        AnneeScolaireDTO anneeScolaire = new AnneeScolaireDTO();
        anneeScolaire
                .setDateSortie(org.crlr.utils.DateUtils.creer(2013, 10, 1));
        anneeScolaire.setId(1);

        List<VisaDTO> listeVisas = null;
        VisaDTO visaDir = null;
        VisaDTO visaIns = null;
        VisaDTO visaDir2 = null;
        VisaDTO visaIns2 = null;

        listeVisas = getVisas(VisaProfil.ENTDirecteur);
        visaDir = findVisa(listeVisas, VisaProfil.ENTDirecteur);
        visaDir2 = findVisa2(listeVisas, VisaProfil.ENTDirecteur);
        listeVisas = getVisas(VisaProfil.ENTInspecteur);
        visaIns = findVisa(listeVisas, VisaProfil.ENTInspecteur);
        visaIns2 = findVisa2(listeVisas, VisaProfil.ENTInspecteur);

        assertFalse(visaDir.getEstPerime());
        assertFalse(visaIns.getEstPerime());
        assertNull(visaDir.getDateMaj());
        assertNull(visaIns.getDateMaj());
        assertNull(visaDir.getDateVisa());
        assertNull(visaIns.getDateVisa());

        assertNull(visaDir2.getDateMaj());
        assertNull(visaIns2.getDateMaj());
        assertNull(visaDir2.getDateVisa());
        assertNull(visaIns2.getDateVisa());

        // 1. créer séance
        Integer idSeance = creerSeance(anneeScolaire);

        listeVisas = getVisas(VisaProfil.ENTDirecteur);
        visaDir = findVisa(listeVisas, VisaProfil.ENTDirecteur);
        visaDir2 = findVisa2(listeVisas, VisaProfil.ENTDirecteur);
        listeVisas = getVisas(VisaProfil.ENTInspecteur);
        visaIns = findVisa(listeVisas, VisaProfil.ENTInspecteur);
        visaIns2 = findVisa2(listeVisas, VisaProfil.ENTInspecteur);
        SeanceDTO seanceDTO = findSeance(idSeance);

        assertFalse(visaDir.getEstPerime());
        assertFalse(visaIns.getEstPerime());
        assertNotNull(visaDir.getDateMaj());
        assertNotNull(visaIns.getDateMaj());
        assertEquals(seanceDTO.getDateMaj(), visaDir.getDateMaj());
        assertEquals(seanceDTO.getDateMaj(), visaIns.getDateMaj());
        assertNull(visaDir.getDateVisa());
        assertNull(visaIns.getDateVisa());

        assertNull(visaDir2.getDateMaj());
        assertNull(visaIns2.getDateMaj());
        assertNull(visaDir2.getDateVisa());
        assertNull(visaIns2.getDateVisa());

        Date seance1DateMaj = seanceDTO.getDateMaj();

        // 2. Modification séance ver séquence '2'

        modifierSequenceDeSeance(idSeance);

        listeVisas = getVisas(VisaProfil.ENTDirecteur);
        visaDir = findVisa(listeVisas, VisaProfil.ENTDirecteur);
        visaDir2 = findVisa2(listeVisas, VisaProfil.ENTDirecteur);
        listeVisas = getVisas(VisaProfil.ENTInspecteur);
        visaIns = findVisa(listeVisas, VisaProfil.ENTInspecteur);
        visaIns2 = findVisa2(listeVisas, VisaProfil.ENTInspecteur);
        seanceDTO = findSeance(idSeance);

        assertFalse(visaDir.getEstPerime());
        assertFalse(visaIns.getEstPerime());

        assertNotNull(visaDir.getDateMaj());
        assertNotNull(visaIns.getDateMaj());
        assertEquals(seanceDTO.getDateMaj(), visaDir.getDateMaj());
        assertEquals(seanceDTO.getDateMaj(), visaIns.getDateMaj());
        assertNull(visaDir.getDateVisa());
        assertNull(visaIns.getDateVisa());

        assertNotNull(visaDir2.getDateMaj());
        assertNotNull(visaIns2.getDateMaj());
        assertEquals(seanceDTO.getDateMaj(), visaDir2.getDateMaj());
        assertEquals(seanceDTO.getDateMaj(), visaIns2.getDateMaj());
        assertNull(visaDir2.getDateVisa());
        assertNull(visaIns2.getDateVisa());

        assertTrue(seanceDTO.getDateMaj().getTime() > seance1DateMaj.getTime());

    }
    
    public void testPlusieursSeancesArchivage() throws MetierException {
        deleteVisas();

        deleteSeances();
        
        AnneeScolaireDTO anneeScolaire = new AnneeScolaireDTO();
        anneeScolaire
                .setDateSortie(org.crlr.utils.DateUtils.creer(2013, 10, 1));
        anneeScolaire.setId(1);

        List<VisaDTO> listeVisas = null;
        VisaDTO visaDir = null;
        
        listeVisas = getVisas(VisaProfil.ENTDirecteur);
        visaDir = findVisa(listeVisas, VisaProfil.ENTDirecteur);
        
        assertFalse(visaDir.getEstPerime());
        assertNull(visaDir.getDateMaj());
        assertNull(visaDir.getDateVisa());

       //Créer séance 1 et 2 avant le visa, ils seront non visées
        Integer idSeance1 = creerSeance(anneeScolaire, SEANCE_TEST_DESC_1, 1, 0);
        Integer idSeance2 = creerSeance(anneeScolaire, SEANCE_TEST_DESC_2, 1, 1);

        visaDir.setTypeVisa(TypeVisa.MEMO);
        listeVisas.clear();
        listeVisas.add(visaDir);
        visaService.saveListeVisa(listeVisas);

        //Séances 3 et 4
        Integer idSeance3 = creerSeance(anneeScolaire, SEANCE_TEST_DESC_1, 1,2);
        Integer idSeance4 = creerSeance(anneeScolaire, SEANCE_TEST_DESC_2, 1,3);

        RechercheVisaSeanceQO rechercheVisaSeanceQO = new RechercheVisaSeanceQO();

        rechercheVisaSeanceQO.setIdAnneeScolaire(anneeScolaire.getId());
        rechercheVisaSeanceQO.setIdEnseignant(ID_ENSEIGNANT);
        rechercheVisaSeanceQO.setAfficheNonVisees(true);
        rechercheVisaSeanceQO.setAfficheVisees(true);
        rechercheVisaSeanceQO.setAffichePerimees(true);
        rechercheVisaSeanceQO.setProfil(Profil.DIRECTION_ETABLISSEMENT);
        rechercheVisaSeanceQO.setIdEtablissement(ID_ETAB);

        List<DateListeVisaSeanceDTO> listeSeances = new ArrayList<DateListeVisaSeanceDTO>();

        try {
            listeSeances = visaService.findVisaSeance(rechercheVisaSeanceQO)
                    .getValeurDTO();
        } catch (MetierException ex) {
            assertNotNull(ex);
        }

        assertEquals(1, listeSeances.size());
        assertEquals(4, listeSeances.get(0).getListeVisaSeance().size());
        
        ResultatRechercheVisaSeanceDTO ligneSeance1 = listeSeances.get(0)
                .getListeVisaSeance().get(3);
        ResultatRechercheVisaSeanceDTO ligneSeance2 = listeSeances.get(0)
                .getListeVisaSeance().get(2);
        ResultatRechercheVisaSeanceDTO ligneSeance3 = listeSeances.get(0)
                .getListeVisaSeance().get(1);
        ResultatRechercheVisaSeanceDTO ligneSeance4 = listeSeances.get(0)
                .getListeVisaSeance().get(0);
        
        assertEquals(idSeance1, ligneSeance1.getId());
        assertEquals(idSeance2, ligneSeance2.getId());
        assertEquals(idSeance3, ligneSeance3.getId());
        assertEquals(idSeance4, ligneSeance4.getId());
        
        //visa 3 et 4 non visé
        assertNull(ligneSeance3.getVisaDirecteur().getDateVisa());
        assertNull(ligneSeance4.getVisaDirecteur().getDateVisa());
        assertNotNull(ligneSeance3.getDateMaj());
        assertNotNull(ligneSeance4.getDateMaj());
        assertTrue(ligneSeance3.getDateMaj().getTime() < ligneSeance4.getDateMaj().getTime());
        assertEquals(Boolean.FALSE, ligneSeance3.getEstVisee());
        assertEquals(Boolean.FALSE, ligneSeance4.getEstVisee());
        
        //visa 1 et 2 visé non perimé
        assertTrue(ligneSeance1.getDateMaj().getTime() < ligneSeance1.getVisaDirecteur().getDateVisa().getTime());
        assertTrue(ligneSeance2.getDateMaj().getTime() < ligneSeance2.getVisaDirecteur().getDateVisa().getTime());
        assertEquals(Boolean.TRUE, ligneSeance1.getEstVisee());
        assertEquals(Boolean.TRUE, ligneSeance2.getEstVisee());
        
        assertEquals(visaDir.getIdVisa(), ligneSeance1.getVisaDeUtilisateur().getIdVisa());
        assertEquals(visaDir.getIdVisa(), ligneSeance2.getVisaDeUtilisateur().getIdVisa());
        assertEquals(visaDir.getIdVisa(), ligneSeance3.getVisaDeUtilisateur().getIdVisa());
        assertEquals(visaDir.getIdVisa(), ligneSeance4.getVisaDeUtilisateur().getIdVisa());
        
        
        
        //Modifier séances 1, 3        
        modifierSeancePourArchivage1(idSeance1);
        
        modifierSeancePourArchivage1(idSeance3);
        
        try {
            listeSeances = visaService.findVisaSeance(rechercheVisaSeanceQO)
                    .getValeurDTO();
        } catch (MetierException ex) {
            assertNotNull(ex);
        }

        assertEquals(1, listeSeances.size());
        assertEquals(4, listeSeances.get(0).getListeVisaSeance().size());
        
        ligneSeance1 = listeSeances.get(0)
                .getListeVisaSeance().get(3);
        // Plus récent est le première ligne
        ligneSeance2 = listeSeances.get(0)
                .getListeVisaSeance().get(2);
        ligneSeance3 = listeSeances.get(0)
                .getListeVisaSeance().get(1);
        ligneSeance4 = listeSeances.get(0)
                .getListeVisaSeance().get(0);
        
        assertEquals(idSeance1, ligneSeance1.getId());
        assertEquals(idSeance2, ligneSeance2.getId());
        assertEquals(idSeance3, ligneSeance3.getId());
        assertEquals(idSeance4, ligneSeance4.getId());
        
        //visa 3 et 4 non visé  (3 modifé mais toujours non visé)
        assertNull(ligneSeance3.getVisaDirecteur().getDateVisa());
        assertNull(ligneSeance4.getVisaDirecteur().getDateVisa());
        assertNotNull(ligneSeance3.getDateMaj());
        assertNotNull(ligneSeance4.getDateMaj());
        assertTrue(ligneSeance3.getDateMaj().getTime() > ligneSeance4.getDateMaj().getTime());
        assertEquals(Boolean.FALSE, ligneSeance3.getEstVisee());
        assertEquals(Boolean.FALSE, ligneSeance4.getEstVisee());
        assertEquals(Boolean.FALSE, ligneSeance3.getIsVisibleCadena());
        assertEquals(Boolean.FALSE, ligneSeance4.getIsVisibleCadena());
        
        //visa 1 perimé, visa 2 non perimé
        assertTrue(ligneSeance1.getDateMaj().getTime() > ligneSeance1.getVisaDirecteur().getDateVisa().getTime());
        assertTrue(ligneSeance2.getDateMaj().getTime() < ligneSeance2.getVisaDirecteur().getDateVisa().getTime());
        assertEquals(Boolean.TRUE, ligneSeance1.getEstVisee());
        assertEquals(Boolean.TRUE, ligneSeance2.getEstVisee());
                
        assertEquals(visaDir.getIdVisa(), ligneSeance1.getVisaDeUtilisateur().getIdVisa());
        assertEquals(visaDir.getIdVisa(), ligneSeance2.getVisaDeUtilisateur().getIdVisa());
        assertEquals(visaDir.getIdVisa(), ligneSeance3.getVisaDeUtilisateur().getIdVisa());
        assertEquals(visaDir.getIdVisa(), ligneSeance4.getVisaDeUtilisateur().getIdVisa());
        assertEquals(Boolean.TRUE, ligneSeance1.getIsVisibleCadena());
        assertEquals(Boolean.FALSE, ligneSeance2.getIsVisibleCadena());
        
        assertEquals(ARCHIVAGE_TEST_DESC_1, ligneSeance1.getDescription());
        assertEquals(SEANCE_TEST_DESC_2, ligneSeance2.getDescription());
        assertEquals(ARCHIVAGE_TEST_DESC_1, ligneSeance3.getDescription());
        assertEquals(SEANCE_TEST_DESC_2, ligneSeance4.getDescription());
        
        ArchiveSeanceDTO archiveSeance = 
                visaFacade.findArchiveSeance(ligneSeance1.getId(), ligneSeance1.getVisaDeUtilisateur().getIdVisa()).getValeurDTO();
        
        assertNotNull(archiveSeance);
        assertEquals(SEANCE_TEST_DESC_1, archiveSeance.getDescription());
        
        archiveSeance = 
                visaFacade.findArchiveSeance(ligneSeance2.getId(), ligneSeance2.getVisaDeUtilisateur().getIdVisa()).getValeurDTO();
        //assertNull(archiveSeance);
        
        
        //ajoute visa mémo
        listeVisas.clear();
        listeVisas.add(visaDir);
        visaService.saveListeVisa(listeVisas);
        
        try {
            listeSeances = visaService.findVisaSeance(rechercheVisaSeanceQO)
                    .getValeurDTO();
        } catch (MetierException ex) {
            assertNotNull(ex);
        }

        assertEquals(1, listeSeances.size());
        assertEquals(4, listeSeances.get(0).getListeVisaSeance().size());
        
        ligneSeance1 = listeSeances.get(0)
                .getListeVisaSeance().get(3);
        // Plus récent est le première ligne
        ligneSeance2 = listeSeances.get(0)
                .getListeVisaSeance().get(2);
        ligneSeance3 = listeSeances.get(0)
                .getListeVisaSeance().get(1);
        ligneSeance4 = listeSeances.get(0)
                .getListeVisaSeance().get(0);
        
        assertEquals(idSeance1, ligneSeance1.getId());
        assertEquals(idSeance2, ligneSeance2.getId());
        assertEquals(idSeance3, ligneSeance3.getId());
        assertEquals(idSeance4, ligneSeance4.getId());
        
        //visa 3 et 4 visés 
        assertNotNull(ligneSeance3.getVisaDirecteur().getDateVisa());
        assertNotNull(ligneSeance4.getVisaDirecteur().getDateVisa());
        assertNotNull(ligneSeance3.getDateMaj());
        assertNotNull(ligneSeance4.getDateMaj());
        assertTrue(ligneSeance3.getDateMaj().getTime() > ligneSeance4.getDateMaj().getTime());
        assertEquals(Boolean.TRUE, ligneSeance3.getEstVisee());
        assertEquals(Boolean.TRUE, ligneSeance4.getEstVisee());
        assertEquals(Boolean.FALSE, ligneSeance3.getIsVisibleCadena());
        assertEquals(Boolean.FALSE, ligneSeance4.getIsVisibleCadena());
        
        //visa 1 non perimé, visa 2 non perimé
        assertTrue(ligneSeance1.getDateMaj().getTime() < ligneSeance3.getVisaDirecteur().getDateVisa().getTime());
        assertTrue(ligneSeance2.getDateMaj().getTime() < ligneSeance4.getVisaDirecteur().getDateVisa().getTime());
        assertEquals(Boolean.TRUE, ligneSeance1.getEstVisee());
        assertEquals(Boolean.TRUE, ligneSeance2.getEstVisee());
                
        assertEquals(visaDir.getIdVisa(), ligneSeance1.getVisaDeUtilisateur().getIdVisa());
        assertEquals(visaDir.getIdVisa(), ligneSeance2.getVisaDeUtilisateur().getIdVisa());
        assertEquals(visaDir.getIdVisa(), ligneSeance3.getVisaDeUtilisateur().getIdVisa());
        assertEquals(visaDir.getIdVisa(), ligneSeance4.getVisaDeUtilisateur().getIdVisa());
        assertEquals(Boolean.FALSE, ligneSeance1.getIsVisibleCadena());
        assertEquals(Boolean.FALSE, ligneSeance2.getIsVisibleCadena());
        
        assertEquals(ARCHIVAGE_TEST_DESC_1, ligneSeance1.getDescription());
        assertEquals(SEANCE_TEST_DESC_2, ligneSeance2.getDescription());
        assertEquals(ARCHIVAGE_TEST_DESC_1, ligneSeance3.getDescription());
        assertEquals(SEANCE_TEST_DESC_2, ligneSeance4.getDescription());
        
        archiveSeance = 
                visaFacade.findArchiveSeance(ligneSeance1.getId(), ligneSeance1.getVisaDeUtilisateur().getIdVisa()).getValeurDTO();
        
        //assertNull(archiveSeance);
        
        archiveSeance = 
                visaFacade.findArchiveSeance(ligneSeance2.getId(), ligneSeance2.getVisaDeUtilisateur().getIdVisa()).getValeurDTO();
        //assertNull(archiveSeance);
        
        
      //Modifier séances 2, 4
        modifierSeancePourArchivage2(idSeance2);
        
        modifierSeancePourArchivage2(idSeance4);
        
        try {
            listeSeances = visaService.findVisaSeance(rechercheVisaSeanceQO)
                    .getValeurDTO();
        } catch (MetierException ex) {
            assertNotNull(ex);
        }

        assertEquals(1, listeSeances.size());
        assertEquals(4, listeSeances.get(0).getListeVisaSeance().size());
        
        ligneSeance1 = listeSeances.get(0)
                .getListeVisaSeance().get(3);
        // Plus récent est le première ligne
        ligneSeance2 = listeSeances.get(0)
                .getListeVisaSeance().get(2);
        ligneSeance3 = listeSeances.get(0)
                .getListeVisaSeance().get(1);
        ligneSeance4 = listeSeances.get(0)
                .getListeVisaSeance().get(0);
        
        assertEquals(idSeance1, ligneSeance1.getId());
        assertEquals(idSeance2, ligneSeance2.getId());
        assertEquals(idSeance3, ligneSeance3.getId());
        assertEquals(idSeance4, ligneSeance4.getId());
        
        //visa 3 et 4 visés ; 4 perimé 
        assertNotNull(ligneSeance3.getVisaDirecteur().getDateVisa());
        assertNotNull(ligneSeance4.getVisaDirecteur().getDateVisa());
        assertNotNull(ligneSeance3.getDateMaj());
        assertNotNull(ligneSeance4.getDateMaj());
        assertTrue(ligneSeance3.getDateMaj().getTime() < ligneSeance4.getDateMaj().getTime());
        assertEquals(Boolean.TRUE, ligneSeance3.getEstVisee());
        assertEquals(Boolean.TRUE, ligneSeance4.getEstVisee());
        assertEquals(Boolean.FALSE, ligneSeance3.getIsVisibleCadena());
        assertEquals(Boolean.TRUE, ligneSeance4.getIsVisibleCadena());
        
        //visa 1 non perimé, visa 2 perimé
        assertTrue(ligneSeance1.getDateMaj().getTime() < ligneSeance3.getVisaDirecteur().getDateVisa().getTime());
        assertTrue(ligneSeance2.getDateMaj().getTime() > ligneSeance4.getVisaDirecteur().getDateVisa().getTime());
        assertEquals(Boolean.TRUE, ligneSeance3.getEstVisee());
        assertEquals(Boolean.TRUE, ligneSeance4.getEstVisee());
                
        assertEquals(visaDir.getIdVisa(), ligneSeance1.getVisaDeUtilisateur().getIdVisa());
        assertEquals(visaDir.getIdVisa(), ligneSeance2.getVisaDeUtilisateur().getIdVisa());
        assertEquals(visaDir.getIdVisa(), ligneSeance3.getVisaDeUtilisateur().getIdVisa());
        assertEquals(visaDir.getIdVisa(), ligneSeance4.getVisaDeUtilisateur().getIdVisa());
        assertEquals(Boolean.FALSE, ligneSeance1.getIsVisibleCadena());
        assertEquals(Boolean.TRUE, ligneSeance2.getIsVisibleCadena());
        
        assertEquals(ARCHIVAGE_TEST_DESC_1, ligneSeance1.getDescription());
        assertEquals(ARCHIVAGE_TEST_DESC_2, ligneSeance2.getDescription());
        assertEquals(ARCHIVAGE_TEST_DESC_1, ligneSeance3.getDescription());
        assertEquals(ARCHIVAGE_TEST_DESC_2, ligneSeance4.getDescription());
        
        archiveSeance = 
                visaFacade.findArchiveSeance(ligneSeance1.getId(), ligneSeance1.getVisaDeUtilisateur().getIdVisa()).getValeurDTO();
        
        //assertNull(archiveSeance);
        
        archiveSeance = 
                visaFacade.findArchiveSeance(ligneSeance2.getId(), ligneSeance2.getVisaDeUtilisateur().getIdVisa()).getValeurDTO();
        assertNotNull(archiveSeance);
        assertEquals(SEANCE_TEST_DESC_2, archiveSeance.getDescription());
        assertEquals(idSeance2, archiveSeance.getIdSeanceOriginale());
        
        archiveSeance = 
                visaFacade.findArchiveSeance(ligneSeance3.getId(), ligneSeance1.getVisaDeUtilisateur().getIdVisa()).getValeurDTO();
        
        //assertNull(archiveSeance);
        
        archiveSeance = 
                visaFacade.findArchiveSeance(ligneSeance4.getId(), ligneSeance2.getVisaDeUtilisateur().getIdVisa()).getValeurDTO();
        assertNotNull(archiveSeance);
        assertEquals(SEANCE_TEST_DESC_2, archiveSeance.getDescription());
        assertEquals(idSeance4, archiveSeance.getIdSeanceOriginale());
        
    }

    /**
     * Test archivage fonctionnement quand le devoir et pj changent.
     * @throws MetierException ex
     */
    public void testArchivageDevoirPJ() throws MetierException {
        deleteVisas();

        deleteSeances();
        
        AnneeScolaireDTO anneeScolaire = new AnneeScolaireDTO();
        anneeScolaire
                .setDateSortie(org.crlr.utils.DateUtils.creer(2013, 10, 1));
        anneeScolaire.setId(1);

        List<VisaDTO> listeVisas = null;
        VisaDTO visaDir = null;
        VisaDTO visaIns = null;

        listeVisas = getVisas(VisaProfil.ENTDirecteur);
        visaDir = findVisa(listeVisas, VisaProfil.ENTDirecteur);
        listeVisas = getVisas(VisaProfil.ENTInspecteur);
        visaIns = findVisa(listeVisas, VisaProfil.ENTInspecteur);

        assertFalse(visaDir.getEstPerime());
        assertFalse(visaIns.getEstPerime());
        assertNull(visaDir.getDateMaj());
        assertNull(visaIns.getDateMaj());
        assertNull(visaDir.getDateVisa());
        assertNull(visaIns.getDateVisa());

        // 1 Sauvegarder une visa memo pour dir et ins
        visaIns.setTypeVisa(TypeVisa.MEMO);

        listeVisas.clear();
        listeVisas.add(visaIns);
        visaService.saveListeVisa(listeVisas);

        visaDir.setTypeVisa(TypeVisa.MEMO);

        listeVisas.clear();
        listeVisas.add(visaDir);
        visaService.saveListeVisa(listeVisas);

        List<DateListeVisaSeanceDTO> listeSeances = rechercheSeancesParEnseignant(
                anneeScolaire, Profil.DIRECTION_ETABLISSEMENT);
        assertEquals(0, listeSeances.size());

        listeVisas = getVisas(VisaProfil.ENTDirecteur);
        visaDir = findVisa(listeVisas, VisaProfil.ENTDirecteur);
        listeVisas = getVisas(VisaProfil.ENTInspecteur);
        visaIns = findVisa(listeVisas, VisaProfil.ENTInspecteur);

        assertFalse(visaDir.getEstPerime());
        assertFalse(visaIns.getEstPerime());
        assertNull(visaDir.getDateMaj());
        assertNull(visaIns.getDateMaj());
        assertNotNull(visaDir.getDateVisa());
        assertNotNull(visaIns.getDateVisa());

        // 1 Créer une séance
        Integer idSeance = creerSeance(anneeScolaire);

        listeVisas = getVisas(VisaProfil.ENTDirecteur);
        visaDir = findVisa(listeVisas, VisaProfil.ENTDirecteur);
        listeVisas = getVisas(VisaProfil.ENTInspecteur);
        visaIns = findVisa(listeVisas, VisaProfil.ENTInspecteur);

        if (!visaDir.getEstPerime()) {
            log.info("onteu {0}",
                    visaDir.getDateMaj().after(visaDir.getDateVisa()));
        }

        assertTrue(visaDir.getEstPerime());
        assertTrue(visaIns.getEstPerime());

        ResultatRechercheVisaSeanceDTO visaSeance = rechercheVisaSeanceUnitaireParEnseignant(
                anneeScolaire, Profil.DIRECTION_ETABLISSEMENT);
        ArchiveSeanceDTO archiveSeanceDir = null;
        archiveSeanceDir = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaDirecteur().getIdVisa()).getValeurDTO();
        ArchiveSeanceDTO archiveSeanceIns = null;
        archiveSeanceIns = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaInspecteur().getIdVisa()).getValeurDTO();

        assertNull(archiveSeanceDir);
        assertNull(archiveSeanceIns);
        assertTrue(visaDir.getDateMaj().compareTo(visaDir.getDateVisa()) > 0);
        assertTrue(visaIns.getDateMaj().compareTo(visaIns.getDateVisa()) > 0);

        // 2. Modif séance
        modifierSeancePourArchivage1(idSeance);

        // 3. Pose visa INS
        listeVisas.clear();
        listeVisas.add(visaIns);
        visaService.saveListeVisa(listeVisas);

        listeVisas = getVisas(VisaProfil.ENTDirecteur);
        visaDir = findVisa(listeVisas, VisaProfil.ENTDirecteur);
        listeVisas = getVisas(VisaProfil.ENTInspecteur);
        visaIns = findVisa(listeVisas, VisaProfil.ENTInspecteur);

        visaSeance = rechercheVisaSeanceUnitaireParEnseignant(anneeScolaire,
                Profil.DIRECTION_ETABLISSEMENT);
        archiveSeanceDir = null;
        archiveSeanceDir = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaDirecteur().getIdVisa()).getValeurDTO();
        archiveSeanceIns = null;
        archiveSeanceIns = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaInspecteur().getIdVisa()).getValeurDTO();
        SeanceDTO seanceDTO = findSeance(idSeance);

        assertNull(archiveSeanceDir);
        assertNotNull(archiveSeanceIns);
        assertEquals(idSeance, archiveSeanceIns.getIdSeanceOriginale());
        assertTrue(visaDir.getDateMaj().compareTo(visaDir.getDateVisa()) > 0);
        // Visa créer après séance
        assertTrue(visaIns.getDateMaj().compareTo(visaIns.getDateVisa()) < 0);
        assertTrue(visaIns.getDateVisa().compareTo(visaDir.getDateVisa()) > 0);
        assertEquals(visaIns.getDateMaj(), seanceDTO.getDateMaj());
        assertEquals(visaDir.getDateMaj(), seanceDTO.getDateMaj());

        assertEquals(2, seanceDTO.getListeDevoirDTO().size());
        assertEquals(3, seanceDTO.getListeFichierJointDTO().size());
        assertEquals("1pj", seanceDTO.getListeDevoirDTO().get(0)
                .getDescription());
        assertEquals(1, seanceDTO.getListeDevoirDTO().get(0).getFiles().size());
        assertEquals("2pj", seanceDTO.getListeDevoirDTO().get(1)
                .getDescription());
        assertEquals(2, seanceDTO.getListeDevoirDTO().get(1).getFiles().size());

        assertEquals(2, archiveSeanceIns.getListeDevoirDTO().size());
        assertEquals(3, archiveSeanceIns.getListeFichierJointDTO().size());
        assertEquals("1pj", archiveSeanceIns.getListeDevoirDTO().get(0)
                .getDescription());
        assertEquals(1, archiveSeanceIns.getListeDevoirDTO().get(0).getFiles()
                .size());
        assertEquals("2pj", archiveSeanceIns.getListeDevoirDTO().get(1)
                .getDescription());
        assertEquals(2, archiveSeanceIns.getListeDevoirDTO().get(1).getFiles()
                .size());
        
        // Recherche séances visées

        // 4. deuxième modif
        modifierSeancePourArchivage2(idSeance);

        listeVisas = getVisas(VisaProfil.ENTDirecteur);
        visaDir = findVisa(listeVisas, VisaProfil.ENTDirecteur);
        listeVisas = getVisas(VisaProfil.ENTInspecteur);
        visaIns = findVisa(listeVisas, VisaProfil.ENTInspecteur);

        visaSeance = rechercheVisaSeanceUnitaireParEnseignant(anneeScolaire,
                Profil.DIRECTION_ETABLISSEMENT);
        archiveSeanceDir = null;
        archiveSeanceDir = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaDirecteur().getIdVisa()).getValeurDTO();
        archiveSeanceIns = null;
        archiveSeanceIns = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaInspecteur().getIdVisa()).getValeurDTO();
        seanceDTO = findSeance(idSeance);

        assertNull(archiveSeanceDir);
        assertNotNull(archiveSeanceIns);
        assertEquals(idSeance, archiveSeanceIns.getIdSeanceOriginale());
        assertTrue(visaDir.getDateMaj().compareTo(visaDir.getDateVisa()) > 0);
        assertTrue(visaIns.getDateMaj().compareTo(visaIns.getDateVisa()) > 0);
        assertTrue(visaIns.getDateVisa().compareTo(visaDir.getDateVisa()) > 0);
        assertEquals(visaIns.getDateMaj(), seanceDTO.getDateMaj());
        assertEquals(visaDir.getDateMaj(), seanceDTO.getDateMaj());

        assertEquals(2, archiveSeanceIns.getListeDevoirDTO().size());
        assertEquals(3, archiveSeanceIns.getListeFichierJointDTO().size());
        assertEquals("1pj", archiveSeanceIns.getListeDevoirDTO().get(0)
                .getDescription());
        assertEquals(1, archiveSeanceIns.getListeDevoirDTO().get(0).getFiles()
                .size());
        assertEquals("2pj", archiveSeanceIns.getListeDevoirDTO().get(1)
                .getDescription());
        assertEquals(2, archiveSeanceIns.getListeDevoirDTO().get(1).getFiles()
                .size());

        // 5. Créer une visa ins

        listeVisas.clear();
        listeVisas.add(visaIns);
        visaService.saveListeVisa(listeVisas);

        visaSeance = rechercheVisaSeanceUnitaireParEnseignant(anneeScolaire,
                Profil.DIRECTION_ETABLISSEMENT);
        archiveSeanceDir = null;
        archiveSeanceDir = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaDirecteur().getIdVisa()).getValeurDTO();
        archiveSeanceIns = null;
        archiveSeanceIns = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaInspecteur().getIdVisa()).getValeurDTO();
        seanceDTO = findSeance(idSeance);

        assertNull(archiveSeanceDir);
        assertNotNull(archiveSeanceIns);
        assertEquals(idSeance, archiveSeanceIns.getIdSeanceOriginale());
        assertTrue(visaDir.getDateMaj().compareTo(visaDir.getDateVisa()) > 0);
        assertTrue(visaIns.getDateMaj().compareTo(visaIns.getDateVisa()) > 0);
        assertTrue(visaIns.getDateVisa().compareTo(visaDir.getDateVisa()) > 0);
        assertEquals(visaIns.getDateMaj(), seanceDTO.getDateMaj());
        assertEquals(visaDir.getDateMaj(), seanceDTO.getDateMaj());

        assertEquals(1, archiveSeanceIns.getListeDevoirDTO().size());
        assertEquals(1, archiveSeanceIns.getListeFichierJointDTO().size());
        assertEquals("0pj", archiveSeanceIns.getListeDevoirDTO().get(0)
                .getDescription());
        assertEquals(0, archiveSeanceIns.getListeDevoirDTO().get(0).getFiles()
                .size());

    }

    public void deleteSeances() throws MetierException {
        RechercheSeanceQO rechercheSeance = new RechercheSeanceQO();
        rechercheSeance.setIdEnseignant(ID_ENSEIGNANT);
        rechercheSeance.setIdEtablissement(ID_ETAB);

        try {
            List<? extends SeanceDTO> listeSeance = seanceService.findSeance(
                    rechercheSeance).getValeurDTO();
            for (SeanceDTO seance : listeSeance) {
                seanceService.deleteSeance(seance);
            }

        } catch (MetierException ex) {
            assertFalse(ex.getConteneurMessage().contientMessageBloquant());
        }

    }

    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss SSS";

    /**
     * Tester plusieurs mémo sur la même séance Résume de test :
     * 
     * 1. Créer séance 2. memo dir 3. modif séance 4. memo ins 5. modif séance
     * 6. memo dir 7. simple ins
     * 
     * @throws MetierException
     */
    public void verifierMultVisaSurMemeSeance() throws MetierException {

        AnneeScolaireDTO anneeScolaire = new AnneeScolaireDTO();
        anneeScolaire
                .setDateSortie(org.crlr.utils.DateUtils.creer(2013, 10, 1));
        anneeScolaire.setId(1);

        // 1 Créer une séance
        creerSeance(anneeScolaire);

        // Verifier que la date de mise en jour est toujours null

        List<VisaDTO> listeVisas = getVisas(VisaProfil.ENTDirecteur);
        VisaDTO visaDir = findVisa(listeVisas, VisaProfil.ENTDirecteur);
        listeVisas = getVisas(VisaProfil.ENTInspecteur);
        VisaDTO visaIns = findVisa(listeVisas, VisaProfil.ENTInspecteur);

        // 2 Sauvegarder une visa memo
        visaDir.setProfil(VisaProfil.ENTDirecteur);
        visaDir.setTypeVisa(TypeVisa.MEMO);
        visaDir.setDateVisa(new Date());

        listeVisas.clear();
        listeVisas.add(visaDir);

        visaService.saveListeVisa(listeVisas);

        // Recherche séances visées

        ResultatRechercheVisaSeanceDTO visaSeance = rechercheVisaSeanceUnitaireParEnseignant(
                anneeScolaire, Profil.DIRECTION_ETABLISSEMENT);

        ArchiveSeanceDTO archiveSeance = null;

        archiveSeance = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaDirecteur().getIdVisa()).getValeurDTO();

        assertNotNull(archiveSeance);
        assertEquals(visaSeance.getId(), archiveSeance.getIdSeanceOriginale());

        // Date visa > date modif séance
        log.info("date visa {0} date seance {1}", org.crlr.utils.DateUtils
                .format(visaSeance.getVisaDirecteur().getDateVisa(),
                        DATE_TIME_FORMAT), org.crlr.utils.DateUtils.format(
                visaSeance.getDateMaj(), DATE_TIME_FORMAT));
        assertTrue(visaSeance.getVisaDirecteur().getDateVisa()
                .compareTo(visaSeance.getDateMaj()) > 0);

        // 3. Modififaction séance
        modifierSeance1(visaSeance.getId());

        visaSeance = rechercheVisaSeanceUnitaireParEnseignant(anneeScolaire,
                Profil.DIRECTION_ETABLISSEMENT);

        // Date visa est maintenant < date modif séance
        log.info("date visa {0} date seance {1}", org.crlr.utils.DateUtils
                .format(visaSeance.getVisaDirecteur().getDateVisa(),
                        DATE_TIME_FORMAT), org.crlr.utils.DateUtils.format(
                visaSeance.getDateMaj(), DATE_TIME_FORMAT));
        assertTrue(visaSeance.getVisaDirecteur().getDateVisa()
                .compareTo(visaSeance.getDateMaj()) < 0);

        archiveSeance = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaDirecteur().getIdVisa()).getValeurDTO();
        // Archive directeur ne devrait pas avoir les modifications
        assertEquals(SEANCE_TITRE_ORIGINAL, archiveSeance.getIntitule());
        assertEquals(0, archiveSeance.getListeDevoirDTO().size());

        // Verifier la partie inspecteur

        // Date visa inspecteur -- null
        assertNull(visaSeance.getVisaInspecteur().getDateVisa());

        archiveSeance = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaInspecteur().getIdVisa()).getValeurDTO();
        assertNull(archiveSeance);

        // 4. Création d'un memo pour l'inspecteur
        visaIns.setProfil(VisaProfil.ENTInspecteur);
        visaIns.setTypeVisa(TypeVisa.MEMO);
        visaIns.setDateVisa(new Date());

        listeVisas.clear();
        listeVisas.add(visaIns);

        visaService.saveListeVisa(listeVisas);

        // Verifier la partie direction
        visaSeance = rechercheVisaSeanceUnitaireParEnseignant(anneeScolaire,
                Profil.DIRECTION_ETABLISSEMENT);

        // Date visa est maintenant < date modif séance
        log.info("date visa {0} date seance {1}", org.crlr.utils.DateUtils
                .format(visaSeance.getVisaDirecteur().getDateVisa(),
                        DATE_TIME_FORMAT), org.crlr.utils.DateUtils.format(
                visaSeance.getDateMaj(), DATE_TIME_FORMAT));
        assertTrue(visaSeance.getVisaDirecteur().getDateVisa()
                .compareTo(visaSeance.getDateMaj()) < 0);

        archiveSeance = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaDirecteur().getIdVisa()).getValeurDTO();
        // Archive directeur ne devrait pas avoir les modifications
        assertEquals(SEANCE_TITRE_ORIGINAL, archiveSeance.getIntitule());
        assertEquals(0, archiveSeance.getListeDevoirDTO().size());
        assertEquals(0, archiveSeance.getListeFichierJointDTO().size());

        // Verifier la partie inspecteur

        // Date visa inspecteur > date modif séance
        assertTrue(visaSeance.getVisaInspecteur().getDateVisa()
                .compareTo(visaSeance.getDateMaj()) > 0);

        archiveSeance = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaInspecteur().getIdVisa()).getValeurDTO();
        assertEquals(SEANCE_TITRE_MODIF_1, archiveSeance.getIntitule());
        assertEquals(1, archiveSeance.getListeDevoirDTO().size());
        assertEquals(1, archiveSeance.getListeDevoirDTO().get(0).getFiles()
                .size());
        assertEquals(0, archiveSeance.getListeFichierJointDTO().size());

        // 5. 2 pj à la séances
        modifierSeance2(visaSeance.getId());

        // 6. memo dir
        visaDir.setProfil(VisaProfil.ENTDirecteur);
        visaDir.setTypeVisa(TypeVisa.MEMO);

        listeVisas.clear();
        listeVisas.add(visaDir);

        visaService.saveListeVisa(listeVisas);

        // une deuxième fois pour véfier que ça marche
        visaDir.setProfil(VisaProfil.ENTDirecteur);
        visaDir.setTypeVisa(TypeVisa.MEMO);

        listeVisas.clear();
        listeVisas.add(visaDir);

        visaService.saveListeVisa(listeVisas);

        // Verifier la partie direction
        visaSeance = rechercheVisaSeanceUnitaireParEnseignant(anneeScolaire,
                Profil.DIRECTION_ETABLISSEMENT);

        assertTrue(visaSeance.getVisaDirecteur().getDateVisa()
                .compareTo(visaSeance.getDateMaj()) > 0);

        archiveSeance = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaDirecteur().getIdVisa()).getValeurDTO();
        // Archive directeur ne devrait pas avoir les modifications
        assertEquals(SEANCE_TITRE_MODIF_1, archiveSeance.getIntitule());
        assertEquals(1, archiveSeance.getListeDevoirDTO().size());
        assertEquals(1, archiveSeance.getListeDevoirDTO().get(0).getFiles()
                .size());
        assertEquals(2, archiveSeance.getListeFichierJointDTO().size());

        // Verifier la partie inspecteur

        assertTrue(visaSeance.getVisaInspecteur().getDateVisa()
                .compareTo(visaSeance.getDateMaj()) < 0);

        archiveSeance = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaInspecteur().getIdVisa()).getValeurDTO();
        assertEquals(SEANCE_TITRE_MODIF_1, archiveSeance.getIntitule());
        assertEquals(1, archiveSeance.getListeDevoirDTO().size());
        assertEquals(1, archiveSeance.getListeDevoirDTO().get(0).getFiles()
                .size());
        assertEquals(0, archiveSeance.getListeFichierJointDTO().size());

        // 7. Memo ins simple

        visaIns.setProfil(VisaProfil.ENTInspecteur);
        visaIns.setTypeVisa(TypeVisa.SIMPLE);
        visaIns.setDateVisa(new Date());

        listeVisas.clear();
        listeVisas.add(visaIns);

        visaService.saveListeVisa(listeVisas);

        // Verifier la partie direction
        visaSeance = rechercheVisaSeanceUnitaireParEnseignant(anneeScolaire,
                Profil.DIRECTION_ETABLISSEMENT);

        assertTrue(visaSeance.getVisaDirecteur().getDateVisa()
                .compareTo(visaSeance.getDateMaj()) > 0);

        archiveSeance = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaDirecteur().getIdVisa()).getValeurDTO();
        // Archive directeur ne devrait pas avoir les modifications
        assertEquals(SEANCE_TITRE_MODIF_1, archiveSeance.getIntitule());
        assertEquals(1, archiveSeance.getListeDevoirDTO().size());
        assertEquals(1, archiveSeance.getListeDevoirDTO().get(0).getFiles()
                .size());
        assertEquals(2, archiveSeance.getListeFichierJointDTO().size());

        // Verifier la partie inspecteur

        assertTrue(visaSeance.getVisaInspecteur().getDateVisa()
                .compareTo(visaSeance.getDateMaj()) > 0);

        archiveSeance = visaFacade.findArchiveSeance(visaSeance.getId(),
                visaSeance.getVisaInspecteur().getIdVisa()).getValeurDTO();
        assertNull(archiveSeance);

    }

    private final static String SEANCE_TITRE_ORIGINAL = "'uoeuth 'oeu\\a \\e \\t \\f é ó";

    public Integer creerSeancePourSeancesEnseignant1(
            AnneeScolaireDTO anneeScolaire) throws MetierException {
        // Sequence
        RechercheSequenceQO rechercheSequence = new RechercheSequenceQO();
        GroupesClassesDTO gc = new GroupesClassesDTO();
        gc.setTypeGroupe(TypeGroupe.CLASSE);
        gc.setId(ID_CLASSE);
        rechercheSequence.setGroupeClasseSelectionne(gc);

        rechercheSequence.setIdEtablissement(ID_ETAB);
        rechercheSequence.setIdEnseignement(ID_ENSEIGNEMENT);
        rechercheSequence.setIdEnseignant(ID_ENSEIGNANT);

        List<? extends SequenceDTO> listeSeq = sequenceService.findSequence(
                rechercheSequence).getValeurDTO();

        assertEquals(1, listeSeq.size());
        // I. Créer séance

        SaveSeanceQO seance = new SaveSeanceQO();
        seance.setIdSequence(listeSeq.get(0).getId());
        seance.setIntitule(SEANCE_TITRE_ORIGINAL);
        seance.setHeureDebut(10);
        seance.setHeureFin(11);
        seance.setMinuteDebut(59);
        seance.setMinuteFin(33);
        seance.setDate(DateUtils.addDays(listeSeq.get(0).getDateDebut(), 1));
        seance.setDescription("desc" + seance.getIntitule() + " DESCRIPTION");
        seance.setIdEnseignant(ID_ENSEIGNANT);
        seance.setAnnotations("Anno");

        seance.setAnneeScolaireDTO(anneeScolaire);
        return seanceService.saveSeance(seance).getValeurDTO();
    }

    public Integer creerSeance(AnneeScolaireDTO anneeScolaire) throws MetierException {
        return creerSeance(anneeScolaire, SEANCE_TEST_DESC_1, 1, 0);
    }
    
    public Integer creerSeance(AnneeScolaireDTO anneeScolaire, 
            String description,
            int dateOffset,
            int heureOffset)
            throws MetierException {
        // Sequence
        RechercheSequenceQO rechercheSequence = new RechercheSequenceQO();
        GroupesClassesDTO gc = new GroupesClassesDTO();
        gc.setTypeGroupe(TypeGroupe.CLASSE);
        gc.setId(ID_CLASSE);
        rechercheSequence.setGroupeClasseSelectionne(gc);

        rechercheSequence.setIdEtablissement(ID_ETAB);
        rechercheSequence.setIdEnseignement(ID_ENSEIGNEMENT);
        rechercheSequence.setIdEnseignant(ID_ENSEIGNANT);

        List<? extends SequenceDTO> listeSeq = sequenceService.findSequence(
                rechercheSequence).getValeurDTO();

        assertEquals(1, listeSeq.size());
        // I. Créer séance

        SaveSeanceQO seance = new SaveSeanceQO();
        seance.setIdSequence(listeSeq.get(0).getId());
        seance.setIntitule(SEANCE_TITRE_ORIGINAL + " date offset " + dateOffset);
        seance.setHeureDebut(9+heureOffset);
        seance.setHeureFin(10+heureOffset);
        seance.setMinuteDebut(59);
        seance.setMinuteFin(33);
        seance.setDate(DateUtils.addDays(listeSeq.get(0).getDateDebut(), dateOffset));
        seance.setDescription(description);
        seance.setIdEnseignant(ID_ENSEIGNANT);
        seance.setAnnotations("Anno");

        seance.setAnneeScolaireDTO(anneeScolaire);
        return seanceService.saveSeance(seance).getValeurDTO();
    }
    
    public Integer creerSeancePourTestCreerSeanceApresVisa(AnneeScolaireDTO anneeScolaire)
            throws MetierException {
        // Sequence
        RechercheSequenceQO rechercheSequence = new RechercheSequenceQO();
        GroupesClassesDTO gc = new GroupesClassesDTO();
        gc.setTypeGroupe(TypeGroupe.CLASSE);
        gc.setId(ID_CLASSE);
        rechercheSequence.setGroupeClasseSelectionne(gc);

        rechercheSequence.setIdEtablissement(ID_ETAB);
        rechercheSequence.setIdEnseignement(ID_ENSEIGNEMENT);
        rechercheSequence.setIdEnseignant(ID_ENSEIGNANT);

        List<? extends SequenceDTO> listeSeq = sequenceService.findSequence(
                rechercheSequence).getValeurDTO();

        assertEquals(1, listeSeq.size());
        // I. Créer séance

        SaveSeanceQO seance = new SaveSeanceQO();
        seance.setIdSequence(listeSeq.get(0).getId());
        seance.setIntitule(SEANCE_TITRE_ORIGINAL);
        seance.setHeureDebut(13);
        seance.setHeureFin(14);
        seance.setMinuteDebut(59);
        seance.setMinuteFin(33);
        seance.setDate(DateUtils.addDays(listeSeq.get(0).getDateDebut(), 1));
        seance.setDescription("desc" + seance.getIntitule() + " DESCRIPTION");
        seance.setIdEnseignant(ID_ENSEIGNANT);
        seance.setAnnotations("Anno");

        seance.setAnneeScolaireDTO(anneeScolaire);
        return seanceService.saveSeance(seance).getValeurDTO();
    }

    public Integer creerSeance3(AnneeScolaireDTO anneeScolaire)
            throws MetierException {
        // Sequence
        RechercheSequenceQO rechercheSequence = new RechercheSequenceQO();
        GroupesClassesDTO gc = new GroupesClassesDTO();
        gc.setTypeGroupe(TypeGroupe.GROUPE);
        gc.setId(ID_GROUPE_3);
        rechercheSequence.setGroupeClasseSelectionne(gc);

        rechercheSequence.setIdEtablissement(ID_ETAB);
        rechercheSequence.setIdEnseignement(ID_ENSEIGNEMENT_3);
        rechercheSequence.setIdEnseignant(ID_ENSEIGNANT);

        List<? extends SequenceDTO> listeSeq = sequenceService.findSequence(
                rechercheSequence).getValeurDTO();

        assertEquals(1, listeSeq.size());
        // I. Créer séance

        SaveSeanceQO seance = new SaveSeanceQO();
        seance.setIdSequence(listeSeq.get(0).getId());
        seance.setIntitule("Séance 3");
        seance.setHeureDebut(10);
        seance.setHeureFin(11);
        seance.setMinuteDebut(59);
        seance.setMinuteFin(33);
        seance.setDate(DateUtils.addDays(listeSeq.get(0).getDateDebut(), 1));
        seance.setDescription("desc" + seance.getIntitule() + " DESCRIPTION");
        seance.setIdEnseignant(ID_ENSEIGNANT);
        seance.setAnnotations("Anno");

        seance.setAnneeScolaireDTO(anneeScolaire);
        return seanceService.saveSeance(seance).getValeurDTO();
    }
    
    public Integer creerSeance4(AnneeScolaireDTO anneeScolaire)
            throws MetierException {
        // Sequence
        RechercheSequenceQO rechercheSequence = new RechercheSequenceQO();
        GroupesClassesDTO gc = new GroupesClassesDTO();
        gc.setTypeGroupe(TypeGroupe.GROUPE);
        gc.setId(ID_GROUPE_3);
        rechercheSequence.setGroupeClasseSelectionne(gc);

        rechercheSequence.setIdEtablissement(ID_ETAB);
        rechercheSequence.setIdEnseignement(ID_ENSEIGNEMENT_3);
        rechercheSequence.setIdEnseignant(ID_ENSEIGNANT);

        List<? extends SequenceDTO> listeSeq = sequenceService.findSequence(
                rechercheSequence).getValeurDTO();

        assertEquals(1, listeSeq.size());
        // I. Créer séance

        SaveSeanceQO seance = new SaveSeanceQO();
        seance.setIdSequence(listeSeq.get(0).getId());
        seance.setIntitule("Séance 3");
        seance.setHeureDebut(10);
        seance.setHeureFin(11);
        seance.setMinuteDebut(59);
        seance.setMinuteFin(33);
        seance.setDate(DateUtils.addDays(listeSeq.get(0).getDateDebut(), 1));
        seance.setDescription("desc" + seance.getIntitule() + " DESCRIPTION");
        seance.setIdEnseignant(ID_ENSEIGNANT);
        seance.setAnnotations("Anno");

        seance.setAnneeScolaireDTO(anneeScolaire);
        return seanceService.saveSeance(seance).getValeurDTO();
    }

    public Integer creerSeance2(AnneeScolaireDTO anneeScolaire)
            throws MetierException {
        // Sequence
        RechercheSequenceQO rechercheSequence = new RechercheSequenceQO();
        GroupesClassesDTO gc = new GroupesClassesDTO();
        gc.setTypeGroupe(TypeGroupe.GROUPE);
        gc.setId(ID_GROUPE_2);
        rechercheSequence.setGroupeClasseSelectionne(gc);

        rechercheSequence.setIdEtablissement(ID_ETAB);
        rechercheSequence.setIdEnseignement(ID_ENSEIGNEMENT_2);
        rechercheSequence.setIdEnseignant(ID_ENSEIGNANT);

        List<? extends SequenceDTO> listeSeq = sequenceService.findSequence(
                rechercheSequence).getValeurDTO();

        assertEquals(1, listeSeq.size());
        // I. Créer séance

        SaveSeanceQO seance = new SaveSeanceQO();
        seance.setIdSequence(listeSeq.get(0).getId());
        seance.setIntitule("Séance 2");
        seance.setHeureDebut(10);
        seance.setHeureFin(11);
        seance.setMinuteDebut(59);
        seance.setMinuteFin(33);
        seance.setDate(DateUtils.addDays(listeSeq.get(0).getDateDebut(), 1));
        seance.setDescription(SEANCE_TEST_DESC_2);
        seance.setIdEnseignant(ID_ENSEIGNANT);
        seance.setAnnotations("Anno");

        seance.setAnneeScolaireDTO(anneeScolaire);
        return seanceService.saveSeance(seance).getValeurDTO();
    }

    private final static String SEANCE_TITRE_MODIF_1 = "'uoecr.c9903903uth 'oeu\\a \\e \\t \\f é ó";

    private SeanceDTO findSeance(Integer idSeance) throws MetierException {

        final ConsulterSeanceQO consulterSeanceQO = new ConsulterSeanceQO();
        consulterSeanceQO.setId(idSeance);
        final SeanceDTO seanceDTO = seanceService.rechercherSeance(consulterSeanceQO);

        return seanceDTO;
    }

    /**
     * Ajoute un devoir, change le titre
     * 
     * @param idSeance
     * @throws MetierException
     */
    private void modifierSeance1(Integer idSeance) throws MetierException {

        final SeanceDTO seanceDTO = findSeance(idSeance);

        DevoirDTO devoir = new DevoirDTO();
        devoir.setDateRemise(DateUtils.addDays(seanceDTO.getDate(), 1));
        devoir.setCode("boo");
        devoir.setIntitule("Devoir");
        devoir.setIdSeance(seanceDTO.getId());
        devoir.setDescription("sontehusnoehunsotheusnoehunsoh");
        seanceDTO.getDevoirs().add(devoir);

        FileUploadDTO pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc.txt");
        devoir.getFiles().add(pj);

        seanceDTO.setIntitule(SEANCE_TITRE_MODIF_1);

        seanceService.modifieSeance(seanceDTO);
    }

    private void modifierSequenceDeSeance(Integer idSeance)
            throws MetierException {
        // Sequence
        RechercheSequenceQO rechercheSequence = new RechercheSequenceQO();
        GroupesClassesDTO gc = new GroupesClassesDTO();
        gc.setTypeGroupe(TypeGroupe.GROUPE);
        gc.setId(ID_GROUPE_2);
        rechercheSequence.setGroupeClasseSelectionne(gc);

        rechercheSequence.setIdEtablissement(ID_ETAB);
        rechercheSequence.setIdEnseignement(ID_ENSEIGNEMENT_2);
        rechercheSequence.setIdEnseignant(ID_ENSEIGNANT);

        List<? extends SequenceDTO> listeSeq = sequenceService.findSequence(
                rechercheSequence).getValeurDTO();
        final ConsulterSeanceQO consulterSeanceQO = new ConsulterSeanceQO();
        consulterSeanceQO.setId(idSeance);
        final SeanceDTO seanceDTO = seanceService.rechercherSeance(consulterSeanceQO);

        assertEquals(1, listeSeq.size());

        seanceDTO.setSequenceDTO(listeSeq.get(0));

        seanceService.modifieSeance(seanceDTO);
    }

    /**
     * 3 pj séances 2 devoirs , 1 et 2 pj
     * 
     * @param idSeance
     * @throws MetierException
     */
    private void modifierSeancePourArchivage1(Integer idSeance)
            throws MetierException {

        final ConsulterSeanceQO consulterSeanceQO = new ConsulterSeanceQO();
        consulterSeanceQO.setId(idSeance);
        final SeanceDTO seanceDTO = seanceService.rechercherSeance(consulterSeanceQO);

        seanceDTO.setDescription(ARCHIVAGE_TEST_DESC_1);
        seanceDTO.setAnnotations(LONG_TEXT);
        DevoirDTO devoir = new DevoirDTO();
        devoir.setDateRemise(DateUtils.addDays(seanceDTO.getDate(), 1));
        devoir.setCode("boo");
        devoir.setIntitule("Devoir");
        devoir.setIdSeance(seanceDTO.getId());
        devoir.setDescription("1pj");
        seanceDTO.getDevoirs().add(devoir);

        FileUploadDTO pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc.txt");
        devoir.getFiles().add(pj);

        devoir = new DevoirDTO();
        devoir.setDateRemise(DateUtils.addDays(seanceDTO.getDate(), 1));
        devoir.setCode("boo");
        devoir.setIntitule("Devoir");
        devoir.setIdSeance(seanceDTO.getId());
        devoir.setDescription("2pj");
        seanceDTO.getDevoirs().add(devoir);

        pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc1.txt");
        devoir.getFiles().add(pj);

        pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc2.txt");
        devoir.getFiles().add(pj);

        pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc1.txt");

        seanceDTO.getListeFichierJointDTO().add(pj);

        pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc2.txt");

        seanceDTO.getListeFichierJointDTO().add(pj);

        pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc3.txt");

        seanceDTO.getListeFichierJointDTO().add(pj);

        seanceService.modifieSeance(seanceDTO);
    }
    
    /**
     * 3 pj séances 2 devoirs , 1 et 2 pj
     * 
     * @param idSeance
     * @throws MetierException
     */
    private void modifierSeancePourSeancesParEnseignant(Integer idSeance)
            throws MetierException {

        final ConsulterSeanceQO consulterSeanceQO = new ConsulterSeanceQO();
        consulterSeanceQO.setId(idSeance);
        final SeanceDTO seanceDTO = seanceService.rechercherSeance(consulterSeanceQO);

        seanceDTO.setDescription("Séance orignale.  " + seanceDTO.getDescription());
        seanceDTO.setAnnotations(LONG_TEXT);
        DevoirDTO devoir = new DevoirDTO();
        devoir.setDateRemise(DateUtils.addDays(seanceDTO.getDate(), 1));
        devoir.setCode("boo");
        devoir.setIntitule("Devoir");
        devoir.setIdSeance(seanceDTO.getId());
        devoir.setDescription("1pj");
        seanceDTO.getDevoirs().add(devoir);

        FileUploadDTO pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc.txt");
        devoir.getFiles().add(pj);

        devoir = new DevoirDTO();
        devoir.setDateRemise(DateUtils.addDays(seanceDTO.getDate(), 1));
        devoir.setCode("boo");
        devoir.setIntitule("Devoir");
        devoir.setIdSeance(seanceDTO.getId());
        devoir.setDescription("2pj");
        seanceDTO.getDevoirs().add(devoir);

        pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc1.txt");
        devoir.getFiles().add(pj);

        pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc2.txt");
        devoir.getFiles().add(pj);

        pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc1.txt");

        seanceDTO.getListeFichierJointDTO().add(pj);

        pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc2.txt");

        seanceDTO.getListeFichierJointDTO().add(pj);

        pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc3.txt");

        seanceDTO.getListeFichierJointDTO().add(pj);

        seanceService.modifieSeance(seanceDTO);
    }

    /**
     * 1 pj séance 1 devoir - 0 pj
     * 
     * @param idSeance
     * @throws MetierException
     */
    private void modifierSeancePourArchivage2(Integer idSeance)
            throws MetierException {

        final ConsulterSeanceQO consulterSeanceQO = new ConsulterSeanceQO();
        consulterSeanceQO.setId(idSeance);
        final SeanceDTO seanceDTO = seanceService.rechercherSeance(consulterSeanceQO);

        seanceDTO.setDescription(ARCHIVAGE_TEST_DESC_2);
        
        DevoirDTO devoir = new DevoirDTO();
        devoir.setDateRemise(DateUtils.addDays(seanceDTO.getDate(), 1));
        devoir.setCode("boo");
        devoir.setIntitule("Devoir");
        devoir.setIdSeance(seanceDTO.getId());
        devoir.setDescription("0pj");
        seanceDTO.getDevoirs().add(devoir);

        seanceDTO.getDevoirs().clear();
        seanceDTO.getDevoirs().add(devoir);

        FileUploadDTO pj = new FileUploadDTO();

        pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc2.txt");

        seanceDTO.getListeFichierJointDTO().clear();
        seanceDTO.getListeFichierJointDTO().add(pj);

        seanceService.modifieSeance(seanceDTO);
    }

    /**
     * Ajoute deux pj à la séance
     * 
     * @param idSeance
     * @throws MetierException
     */
    private void modifierSeance2(Integer idSeance) throws MetierException {

        final ConsulterSeanceQO consulterSeanceQO = new ConsulterSeanceQO();
        consulterSeanceQO.setId(idSeance);
        final SeanceDTO seanceDTO = seanceService.rechercherSeance(consulterSeanceQO);

        FileUploadDTO pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc1.txt");

        seanceDTO.getListeFichierJointDTO().add(pj);

        pj = new FileUploadDTO();
        pj.setUid(UID);
        pj.setPathDB(PJ_PATH);
        pj.setNom("abc2.txt");

        seanceDTO.getListeFichierJointDTO().add(pj);

        seanceService.modifieSeance(seanceDTO);
    }

    private List<DateListeVisaSeanceDTO> rechercheSeancesParEnseignant(
            AnneeScolaireDTO anneeScolaire, Profil profil) {
        RechercheVisaSeanceQO rechercheVisaSeanceQO = new RechercheVisaSeanceQO();

        rechercheVisaSeanceQO.setIdAnneeScolaire(anneeScolaire.getId());
        rechercheVisaSeanceQO.setIdEnseignant(ID_ENSEIGNANT);
        rechercheVisaSeanceQO.setIdEnseignement(ID_ENSEIGNEMENT);
        rechercheVisaSeanceQO.setAfficheVisees(true);
        rechercheVisaSeanceQO.setProfil(profil);
        rechercheVisaSeanceQO.setIdEtablissement(ID_ETAB);

        List<DateListeVisaSeanceDTO> listeSeances = new ArrayList<DateListeVisaSeanceDTO>();

        try {
            listeSeances = visaService.findVisaSeance(rechercheVisaSeanceQO)
                    .getValeurDTO();
        } catch (MetierException ex) {
            assertNotNull(ex);
        }

        return listeSeances;
    }

    private ResultatRechercheVisaSeanceDTO rechercheVisaSeanceUnitaireParEnseignant(
            AnneeScolaireDTO anneeScolaire, Profil profil) {
        List<DateListeVisaSeanceDTO> listeSeances = rechercheSeancesParEnseignant(
                anneeScolaire, profil);

        assertEquals(1, listeSeances.size());

        DateListeVisaSeanceDTO visaSeanceDate = listeSeances.get(0);
        assertEquals(1, visaSeanceDate.getListeVisaSeance().size());

        ResultatRechercheVisaSeanceDTO visaSeance = visaSeanceDate
                .getListeVisaSeance().get(0);

        return visaSeance;
    }

    public void creerSeanceAvantVisa() {
        // Date MAJ visa doit rester null
        // TODO
    }

    public void deleteVisas() throws MetierException {

        List<VisaDTO> listeVisa = getVisas(VisaProfil.ENTDirecteur);

        for (VisaDTO visa : listeVisa) {
            visa.setDateVisa(null);
            visa.setTypeVisa(null);
            visa.setDateMaj(null);
        }

        visaService.saveListeVisa(listeVisa);

        listeVisa = getVisas(VisaProfil.ENTInspecteur);

        for (VisaDTO visa : listeVisa) {
            visa.setDateVisa(null);
            visa.setTypeVisa(null);
            visa.setDateMaj(null);
        }

        visaService.saveListeVisa(listeVisa);

        List<VisaDTO> listeVisas = getVisas(VisaProfil.ENTDirecteur);

        VisaDTO visaDir = findVisa(listeVisas, VisaProfil.ENTDirecteur);

        listeVisas = getVisas(VisaProfil.ENTInspecteur);

        VisaDTO visaIns = findVisa(listeVisas, VisaProfil.ENTInspecteur);

        assertFalse(visaDir.getEstPerime());
        assertFalse(visaIns.getEstPerime());
        assertNull(visaDir.getDateMaj());
        assertNull(visaIns.getDateMaj());
        assertNull(visaDir.getDateVisa());
        assertNull(visaIns.getDateVisa());
    }

    public List<VisaDTO> getVisas(VisaProfil profil) {
        // La liste de visa n'est pas null
        final RechercheVisaQO rechercheVisa = new RechercheVisaQO();
        rechercheVisa.setIdEnseignant(ID_ENSEIGNANT);

        rechercheVisa.setIdEtablissement(ID_ETAB);
        rechercheVisa.setProfil(profil);

        final ResultatDTO<List<VisaDTO>> resultat = visaService
                .findListeVisa(rechercheVisa);
        final List<VisaDTO> listeVisa = resultat.getValeurDTO();

        assertTrue(!listeVisa.isEmpty());
        return listeVisa;
    }

    public void doCreateSimpleVisas() throws MetierException {

        final List<VisaDTO> listeVisa = getVisas(VisaProfil.ENTDirecteur);

        // Sauvegarder une visa simple
        final List<VisaDTO> listeASauver = new ArrayList<VisaDTO>();
        listeASauver.add(listeVisa.get(0));
        listeASauver.get(0).setDateVisa(new Date());
        listeASauver.get(0).setTypeVisa(TypeVisa.SIMPLE);
        listeASauver.get(0).setEstModifie(true);

        visaService.saveListeVisa(listeVisa);

        // Sauvegarder une visa memo
        VisaDTO memoVisa = new VisaDTO();
        memoVisa.setClasseGroupe(new GroupesClassesDTO());
        memoVisa.getClasseGroupe().setTypeGroupe(TypeGroupe.CLASSE);
        memoVisa.getClasseGroupe().setId(393);
        memoVisa.getEnseignementDTO().setId(131);
        memoVisa.setIdEnseignant(ID_ENSEIGNANT);
        memoVisa.setIdEtablissement(ID_ETAB);
        memoVisa.setId(63);
        memoVisa.setProfil(VisaProfil.ENTDirecteur);
        memoVisa.setTypeVisa(TypeVisa.MEMO);

        listeASauver.clear();
        listeASauver.add(memoVisa);

        visaService.saveListeVisa(listeASauver);

    }

    private VisaDTO findVisa2(List<VisaDTO> listeVisas, VisaProfil visaProfile) {
        boolean found = false;

        VisaDTO visa = null;
        for (VisaDTO visaDansListe : listeVisas) {
            if (visaDansListe.getIdEnseignant() == ID_ENSEIGNANT
                    && visaDansListe.getIdEtablissement() == ID_ETAB
                    && visaDansListe.getEnseignementDTO().getId() == ID_ENSEIGNEMENT_2
                    && visaDansListe.getClasseGroupe().getTypeGroupe() == TypeGroupe.GROUPE
                    && visaDansListe.getClasseGroupe().getId() == ID_GROUPE_2
                    && visaDansListe.getProfil() == visaProfile) {
                found = true;
                visa = visaDansListe;
                // TODO pas vrai
                // assertNull(visa.getDateMaj());
            }
        }

        assertTrue(found);

        return visa;
    }

    private VisaDTO findVisa3(List<VisaDTO> listeVisas, VisaProfil visaProfile) {
        boolean found = false;

        VisaDTO visa = null;
        for (VisaDTO visaDansListe : listeVisas) {
            if (visaDansListe.getIdEnseignant() == ID_ENSEIGNANT
                    && visaDansListe.getIdEtablissement() == ID_ETAB
                    && visaDansListe.getEnseignementDTO().getId() == ID_ENSEIGNEMENT_3
                    && visaDansListe.getClasseGroupe().getTypeGroupe() == TypeGroupe.GROUPE
                    && visaDansListe.getClasseGroupe().getId() == ID_GROUPE_3
                    && visaDansListe.getProfil() == visaProfile) {
                found = true;
                visa = visaDansListe;
                // TODO pas vrai
                // assertNull(visa.getDateMaj());
            }
        }

        assertTrue(found);

        return visa;
    }

    private VisaDTO findVisa(List<VisaDTO> listeVisas, VisaProfil visaProfile) {
        boolean found = false;

        VisaDTO visa = null;
        for (VisaDTO visaDansListe : listeVisas) {
            if (visaDansListe.getIdEnseignant() == ID_ENSEIGNANT
                    && visaDansListe.getIdEtablissement() == ID_ETAB
                    && visaDansListe.getEnseignementDTO().getId() == ID_ENSEIGNEMENT
                    && visaDansListe.getClasseGroupe().getTypeGroupe() == TypeGroupe.CLASSE
                    && visaDansListe.getClasseGroupe().getId() == ID_CLASSE
                    && visaDansListe.getProfil() == visaProfile) {
                found = true;
                visa = visaDansListe;
                // TODO pas vrai
                // assertNull(visa.getDateMaj());
            }
        }

        assertTrue(found);

        return visa;
    }
}