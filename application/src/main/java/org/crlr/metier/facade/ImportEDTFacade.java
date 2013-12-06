/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AnneeScolaireFacade.java,v 1.8 2010/06/04 07:25:50 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crlr.dto.application.importEDT.CaracEtabImportDTO;
import org.crlr.dto.application.importEDT.PeriodeEdtQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.importEDT.DTO.EmploiDTO;
import org.crlr.importEDT.DTO.PrintEDTDTO;
import org.crlr.metier.business.ImportEDTBusinessService;
import org.crlr.report.impl.PdfReport;
import org.crlr.report.impl.PdfReportGenerator;
import org.crlr.utils.ClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe qui contient les corps des méthodes d'ImportEDTFacadeService et fait appel aux classes HibernateBusinessService 
 * qui correspondent aux méthodes à exécuter.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
 */
@Component
@Transactional(
        readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
    )
public class ImportEDTFacade implements ImportEDTFacadeService {
        
    @Autowired
    private ImportEDTBusinessService importEDTBusinessService;
    
      
    /**
     * Modificateur de importEDTBusinessService.
     * @param importEDTBusinessService le importEDTBusinessService à modifier
     */
    public void setImportEDTBusinessService(
            ImportEDTBusinessService importEDTBusinessService) {
        this.importEDTBusinessService = importEDTBusinessService;
    }

    /**
     * {@inheritDoc}
     */
    public CaracEtabImportDTO saveTraitementEDTSTS(
            CaracEtabImportDTO caracEtabImportDTO, 
            String pathSTS, 
            String pathEDT, 
            String pathAppli) throws MetierException {
        return this.importEDTBusinessService.saveTraitementEDTSTS(caracEtabImportDTO, pathSTS, pathEDT, pathAppli);
    }

    /**
     * {@inheritDoc}
     */
    public PdfReport printEmploiDuTemps(PrintEDTDTO printEDTDTO) {
        
        final Map<String, Object> args = new HashMap<String, Object>();
              
        args.put("printEDTDTO", printEDTDTO);
        args.put("nbTotal", printEDTDTO.getTotalCase());
        args.put("dateDebutPeriode", printEDTDTO.getDateDebutPeriode());
     
        //generation effective du report.
        final PdfReportGenerator gen = new PdfReportGenerator();
        return gen.generate(ClassUtils.getPathToReport() + "rapportAnomaliesImportEDT.jasper", org.crlr.utils.CollectionUtils.creerList(1), args);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteEmploiDuTempsEtablissement(PeriodeEdtQO periodeEdtQO) {
        importEDTBusinessService.deleteEDTEtablissement(periodeEdtQO);
    }

    /**
     * {@inheritDoc}
     */
    public void insertionCases(List<EmploiDTO> casesSimples, Date dateDebut) {
        importEDTBusinessService.insertionCases(casesSimples, dateDebut);
    }

    /**
     * {@inheritDoc}
     */
    public Integer checkNombreCaseEmploiPourEtablissement(PeriodeEdtQO periodeEdtQO) {
        return importEDTBusinessService.checkNombreCaseEmploiPourEtablissement(periodeEdtQO);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean findEtatImportEtablissement(Integer idEtablissement) {
        return importEDTBusinessService.findEtatImportEtablissement(idEtablissement);
    }

    /**
     * {@inheritDoc}
     */
    @Async
    public void modifieStatutImportEtablissement(Integer idEtablissement,
            Boolean statut) {
        importEDTBusinessService.modifieStatutImportEtablissement(idEtablissement, statut);        
    }

    /**
     * {@inheritDoc}
     */
    public String checkDateImportEtablissement(Integer idEtablissement) {
        return importEDTBusinessService.checkDateImportEtablissement(idEtablissement);
    }

    
}
