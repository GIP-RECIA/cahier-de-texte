/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.crlr.dto.application.importEDT.CaracEtabImportDTO;
import org.crlr.dto.application.importEDT.PeriodeEdtQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.importEDT.DTO.EmploiDTO;
import org.crlr.importEDT.DTO.PrintEDTDTO;
import org.crlr.metier.facade.ImportEDTFacadeService;
import org.crlr.report.impl.PdfReport;
import org.crlr.utils.PropertiesUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe qui contient le corps des méthodes décrites dans ImportEDTService.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
 */
@Service
public class ImportEDTDelegate implements ImportEDTService {
    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private ImportEDTFacadeService importEDTFacadeService;
    
    private static String aideContextuelle;

    /**
     * Modificateur de importEDTFacadeService.
     *
     * @param importEDTFacadeService le importEDTFacadeService à modifier
     */
    public void setImportEDTFacadeService(ImportEDTFacadeService importEDTFacadeService) {
        this.importEDTFacadeService = importEDTFacadeService;
    }

    /**
     * {@inheritDoc}
     */
    public PdfReport printEmploiDuTemps(PrintEDTDTO printEDTDTO) {
        return importEDTFacadeService.printEmploiDuTemps(printEDTDTO);
    }

    /**
     * {@inheritDoc}
     */
    public CaracEtabImportDTO saveTraitementEDTSTS(CaracEtabImportDTO caracEtabImportDTO, String pathSTS, 
            String pathEDT, String pathAppli) throws MetierException {
      return importEDTFacadeService.saveTraitementEDTSTS(caracEtabImportDTO, pathSTS, pathEDT, pathAppli);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteEmploiDuTempsEtablissement(PeriodeEdtQO periodeEdtQO) {
        importEDTFacadeService.deleteEmploiDuTempsEtablissement(periodeEdtQO);
    }

    /**
     * {@inheritDoc}
     */
    public void insertionCases(List<EmploiDTO> casesSimples, Date dateDebut) {
        importEDTFacadeService.insertionCases(casesSimples, dateDebut);
    }

    /**
     * {@inheritDoc}
     */
    public Integer checkNombreCaseEmploiPourEtablissement(PeriodeEdtQO periodeEdtQO) {
        return importEDTFacadeService.checkNombreCaseEmploiPourEtablissement(periodeEdtQO);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean findEtatImportEtablissement(Integer idEtablissement) {
        return importEDTFacadeService.findEtatImportEtablissement(idEtablissement);
    }

    /**
     * {@inheritDoc}
     */
    public void modifieStatutImportEtablissement(Integer idEtablissement,
            Boolean statut) {
        importEDTFacadeService.modifieStatutImportEtablissement(idEtablissement, statut);
    }

    /**
     * {@inheritDoc}
     */
    public String checkDateImportEtablissement(Integer idEtablissement) {
        return importEDTFacadeService.checkDateImportEtablissement(idEtablissement);
    }
    
    /**
     * Accesseur de aideContextuelle.
     * @return le aideContextuelle
     */
    public String getAideContextuelle() {
        if (StringUtils.isEmpty(aideContextuelle)){
            final Properties properties= PropertiesUtils.load("/aideContextuelle.properties");
            aideContextuelle =properties.getProperty("emploi.importer"); 
        }
        return aideContextuelle;
    }
}
