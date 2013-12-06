package org.crlr.test.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.design.JasperDesign;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.TypeAffichage;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.metier.facade.EmploiFacadeService;
import org.crlr.metier.facade.SeanceFacadeService;
import org.crlr.metier.utils.EmploiDeTempsReport;
import org.crlr.report.Report;
import org.crlr.report.impl.PdfReport;
import org.crlr.report.impl.PdfReportGenerator;
import org.crlr.services.SeanceService;
import org.crlr.services.SequenceService;
import org.crlr.test.AbstractMetierTest;
import org.crlr.utils.IOUtils;
import org.crlr.web.application.control.ClasseGroupeControl;
import org.crlr.web.application.control.seance.SeancePrintControl;
import org.crlr.web.application.control.sequence.SequencePrintControl;
import org.crlr.web.dto.TypeSemaine;
import org.springframework.beans.factory.annotation.Autowired;



public class TestEmploiDeTempsReportTest extends AbstractMetierTest {

	@Autowired
	private EmploiFacadeService emploiFacade;
	
	@Autowired 
	private transient SeanceFacadeService seanceFacade;
	
	@Autowired 
    private transient SeanceService seanceService;
	
	@Autowired 
    private transient SequenceService sequenceService;
	
	//@Autowired 
	protected transient ClasseGroupeControl classeGroupeControl;
	
	

	protected String getTemplate(String name) {
		final String prefix = "/org/crlr/report/";
		return prefix + name;
	}
	
	/**
	 * @throws Exception ex
	 */
	public void testPrintSeances() throws Exception {

        classeGroupeControl = new ClasseGroupeControl();
        classeGroupeControl.getForm().setTypeGroupeSelectionne(TypeGroupe.CLASSE);
        classeGroupeControl.getForm().setGroupeClasseSelectionne(new GroupesClassesDTO());
        classeGroupeControl.getForm().getGroupeClasseSelectionne().setId(85);
        classeGroupeControl.getForm().getGroupeClasseSelectionne().setTypeGroupe(TypeGroupe.CLASSE);
        classeGroupeControl.getForm().getGroupeClasseSelectionne().setCode("CLA85");
        classeGroupeControl.getForm().getGroupeClasseSelectionne().setDesignation("202");
        classeGroupeControl.getForm().setListeGroupe(new ArrayList<GroupeDTO>());
        
        GroupeDTO groupe = new GroupeDTO();
        groupe.setId(1421);
        groupe.setIntitule("2ACCPEGR");
        groupe.setCode("GRP1421");
        groupe.setSelectionner(true);
        
        classeGroupeControl.getForm().getListeGroupe().add(groupe);
        
        SeancePrintControl printSeance = new SeancePrintControl();
        printSeance.setClasseGroupeControl(classeGroupeControl);
        printSeance.setSeanceFacade(seanceFacade);
        printSeance.setSeanceService(seanceService);
        printSeance.setSequenceService(sequenceService);
        
        printSeance.getForm().setTypeAffichageSelectionne(TypeAffichage.DETAILLE);
        //printSeance.getForm().setTypeAffichageSelectionne(TypeAffichage.SIMPLE);
        
        EnseignementDTO enseignement = new EnseignementDTO();
        enseignement.setIntitule("FRANCAIS");
        enseignement.setLibellePerso("Français");
        enseignement.setId(7);
        enseignement.setCode("7");
        
        
        EnseignantDTO enseignant = new EnseignantDTO();
        enseignant.setCivilite("M.");
        enseignant.setNom("FAUCHIER");
        enseignant.setPrenom("Joel");
        enseignant.setId(536);
        
       // printSeance.getForm().setEnseignantSelectionne(enseignant);
        
        printSeance.rechercher();
        printSeance.printSeance();
        
        
        Report report = printSeance.getForm().getReport();
        
        final File tmpFile = File.createTempFile("crlrreport-seance",
                ".pdf");
        dump(report.getData(), tmpFile);
        log.info("Édition générée : {0}", tmpFile.getAbsolutePath());
        log.info("Édition générée : {0}", tmpFile.getAbsolutePath());
    }
	
	/**
	 * @throws Exception ex
	 */
	public void testPrintSequences() throws Exception {

	    classeGroupeControl = new ClasseGroupeControl();
	    classeGroupeControl.getForm().setTypeGroupeSelectionne(TypeGroupe.CLASSE);
	    classeGroupeControl.getForm().setGroupeClasseSelectionne(new GroupesClassesDTO());
	    classeGroupeControl.getForm().getGroupeClasseSelectionne().setId(85);
	    classeGroupeControl.getForm().getGroupeClasseSelectionne().setTypeGroupe(TypeGroupe.CLASSE);
	    classeGroupeControl.getForm().getGroupeClasseSelectionne().setCode("CLA85");
	    classeGroupeControl.getForm().getGroupeClasseSelectionne().setDesignation("202");
	    classeGroupeControl.getForm().setListeGroupe(new ArrayList<GroupeDTO>());
	    
	    GroupeDTO groupe = new GroupeDTO();
        groupe.setId(1421);
        groupe.setIntitule("2ACCPEGR");
        groupe.setCode("GRP1421");
        groupe.setSelectionner(true);
        classeGroupeControl.getForm().getListeGroupe().add(groupe);
	    
	    SequencePrintControl printSequence;
	    
	    printSequence = new SequencePrintControl();
	    printSequence.setClasseGroupeControl(classeGroupeControl);
	    printSequence.setSeanceFacade(seanceFacade);
	    printSequence.setSeanceService(seanceService);
	    printSequence.setSequenceService(sequenceService);
	    
	    printSequence.getForm().setTypeAffichageSelectionne(TypeAffichage.DETAILLE);
	    
	    printSequence.rechercher();
	    printSequence.printSequence();
	    
        
	    
	    Report report = printSequence.getForm().getReport();
	    
	    final File tmpFile = File.createTempFile("crlrreport-sequences",
                ".pdf");
        dump(report.getData(), tmpFile);
        log.info("Édition générée : {0}", tmpFile.getAbsolutePath());
        log.info("Édition générée : {0}", tmpFile.getAbsolutePath());
	}

	public void aoeutestGenerate() throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("nom", System.getProperty("user.name"));
		params.put("date", new Date());


		

		final List<DetailJourEmploiDTO> listeFromDatabase = emploiFacade
				.findEmploi(311, 169, TypeSemaine.PAIR, 1825).getValeurDTO();

		// calculer debut et fin en terme de mintues total du début et fin de la
		// grille

		params.put("titre", "L'emploi de temps");


		log.info(getTemplate("emploiTempsEns.jasper"));

		final PdfReportGenerator gen = new PdfReportGenerator();

		JasperDesign jr = gen
				.getJasperReport(getTemplate("emploiTempsEns.jrxml"));

		List<TypeJour> listeJours = new ArrayList<TypeJour>();
		listeJours.add(TypeJour.LUNDI);
		listeJours.add(TypeJour.MARDI);
		listeJours.add(TypeJour.MERCREDI);
		listeJours.add(TypeJour.JEUDI);
		listeJours.add(TypeJour.VENDREDI);

		EmploiDeTempsReport empReport = new EmploiDeTempsReport(jr,
				8, 18, listeJours, listeFromDatabase);

		final PdfReport report = gen.generate(jr, null, params);

		// final PdfReport report =
		// emploiFacade.printEmploiDuTemps(printEmploiQO);

		assertNotNull(empReport);
		assertNotNull(report);
		assertFalse(report.getData().length == 0);

		final File tmpFile = File.createTempFile("crlrreport-emploiTempsEns-",
				".pdf");
		dump(report.getData(), tmpFile);
		log.info("Édition générée : {0}", tmpFile.getAbsolutePath());

	}

	
	/* (non-Javadoc)
	 * @see org.crlr.test.AbstractMetierTest#dump(byte[], java.io.File)
	 */
	protected void dump(byte[] data, File file) throws IOException {
		final ReadableByteChannel buffer = Channels
				.newChannel(new ByteArrayInputStream(data));
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

	protected final Log log = LogFactory.getLog(getClass());

}
