/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PdfReportGeneratorTest.java,v 1.1 2009/05/26 07:34:42 ent_breyton Exp $
 */

package org.crlr.test.services;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.crlr.importEDT.DTO.ErreurEDT;
import org.crlr.importEDT.DTO.PrintEDTDTO;
import org.crlr.report.impl.PdfReport;
import org.crlr.services.ImportEDTService;
import org.crlr.test.AbstractMetierTest;

/**
 * Test de la classe {@link PdfReportGenerator}.
 *
 * @author breytond.
 */
public class ImportEDTServiceTest extends AbstractMetierTest {
    private ImportEDTService importEDTService;
    /**
     * Modificateur de importEDTService.
     * @param importEDTService le importEDTService à modifier
     */
    public void setImportEDTService(ImportEDTService importEDTService) {
        this.importEDTService = importEDTService;
    }

    /**
     * Méthode de test.
     *
     * @throws Exception DOCUMENTATION INCOMPLETE!
     */
    public void testImportPdf() throws Exception {
        final PrintEDTDTO printEDTDTO = new PrintEDTDTO(); 
        printEDTDTO.setTotalCase(1100);
        final Set<ErreurEDT> errClasses = new HashSet<ErreurEDT>(); 
        errClasses.add(new ErreurEDT("4B"));
        final Set<ErreurEDT> errGroupes = new HashSet<ErreurEDT>(); 
        errGroupes.add(new ErreurEDT("2S_EURO"));
        final Set<ErreurEDT> errEnseignant = new HashSet<ErreurEDT>(); 
        errEnseignant.add(new ErreurEDT("Jean Test"));
        final Set<ErreurEDT> errEnseignement = new HashSet<ErreurEDT>(); 
        errEnseignement.add(new ErreurEDT("Maths Spécialité"));
        final Set<ErreurEDT> errHoraires = new HashSet<ErreurEDT>(); 
        errHoraires.add(new ErreurEDT("10:20 à 11:20"));
        final Set<ErreurEDT> errEnsXml = new HashSet<ErreurEDT>(); 
        errEnsXml.add(new ErreurEDT("15548"));
        printEDTDTO.setClasseErreur(errClasses);
        printEDTDTO.setGroupeErreur(errGroupes);
        printEDTDTO.setEnseignantErreurBD(errEnseignant);
        printEDTDTO.setEnseignantErreurEDT(errEnsXml);
        printEDTDTO.setEnseignementErreur(errEnseignement);
        printEDTDTO.setHoraireErreur(errHoraires);
        final PdfReport pdfReport = importEDTService.printEmploiDuTemps(printEDTDTO);
        
        
        final File tmpFile = File.createTempFile("crlrreport-jp-", ".pdf");
        dump(pdfReport.getData(), tmpFile);
    }
   
}
