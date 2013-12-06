/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EmploiDelegate.java,v 1.4 2010/04/21 15:39:48 jerome.carriere Exp $
 */

package org.crlr.services;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.DateDTO;
import org.crlr.dto.application.base.PeriodeEmploiDTO;
import org.crlr.dto.application.base.PrintEmploiQO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.SequenceDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.dto.application.emploi.RechercheEmploiQO;
import org.crlr.dto.application.emploi.SaveEmploiQO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.dto.application.sequence.RechercheSequenceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.EmploiFacadeService;
import org.crlr.report.impl.PdfReport;
import org.crlr.utils.PropertiesUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.web.dto.GrilleHoraireDTO;
import org.crlr.web.dto.TypeSemaine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * EtablissementDelegate.
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
@Service
public class EmploiDelegate implements EmploiService {
    /** Injection spring automatique de la seanceFacade. */
    @Autowired
    private EmploiFacadeService emploiFacadeService;
    
    
    private static String aideContextuelle;

    /**
     * Mutateur de emploiFacadeService.
     *
     * @param emploiFacadeService le emploiFacadeService à modifier.
     */
    public void setEmploiFacadeService(EmploiFacadeService emploiFacadeService) {
        this.emploiFacadeService = emploiFacadeService;
    }
    
    /**
     * Accesseur de aideContextuelle.
     * @return le aideContextuelle
     */
    public String getAideContextuelle() {
        if (StringUtils.isEmpty(aideContextuelle)){
            final Properties properties= PropertiesUtils.load("/aideContextuelle.properties");
            aideContextuelle =properties.getProperty("emploi.constituer"); 
        }
        return aideContextuelle;
    }





    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Date> saveEmploiDuTemps(final SaveEmploiQO saveEmploiQO)
                                           throws MetierException {
        return emploiFacadeService.saveEmploiDuTemps(saveEmploiQO);
    }

    
        
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<DetailJourEmploiDTO>> findEmploi(final Integer idEnseignant,
                                                             final Integer idEtablissement,
                                                             final TypeSemaine typeSemaine,
                                                             final Integer idPeriode) {
        return emploiFacadeService.findEmploi(idEnseignant, idEtablissement, typeSemaine, idPeriode);
    }
   
    /**
     * {@inheritDoc}
     */
    public Map<String, SeanceDTO> findSeanceEmploiSemainePassee(final RechercheSeanceQO rechercheSeanceQO, final List<GrilleHoraireDTO> listeHoraire){
        return emploiFacadeService.findSeanceEmploiSemainePassee(rechercheSeanceQO, listeHoraire);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<DetailJourEmploiDTO>> findEmploiConsolidation(final RechercheEmploiQO rechercheEmploiQO)
                                              throws MetierException {
        return emploiFacadeService.findEmploiConsolidation(rechercheEmploiQO);
    }
    
    /**
     * {@inheritDoc}
     */
    public Map<String, Integer> findSeanceEmploiSemaine(
            RechercheSeanceQO rechercheSeanceQO) {
        return emploiFacadeService.findSeanceEmploiSemaine(rechercheSeanceQO);
    }

    /**
     * {@inheritDoc}
     */
    public List<SeanceDTO> findListeSeanceSemaine(
            RechercheSeanceQO rechercheSeanceQO) {
        return emploiFacadeService.findListeSeanceSemaine(rechercheSeanceQO);
    }
    
    
 
    
    /**
     * {@inheritDoc}
     */
    public PdfReport printEmploiDuTemps(final PrintEmploiQO printEmploiQO) {
        return this.emploiFacadeService.printEmploiDuTemps(printEmploiQO);
    }

   
    
    /**
     * {@inheritDoc}
     */
    public List<SequenceDTO> chercherSequenceSemaine(RechercheSequenceQO rechercheSequence) {
        return this.emploiFacadeService.chercherSequenceSemaine(rechercheSequence);

    }
    
    
    public List<PeriodeEmploiDTO> findPeriodes(final Integer enseignantId, final Integer etablissementId) {
    	return this.emploiFacadeService.findPeriodes(enseignantId, etablissementId);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<DateDTO> findProchaineDate(RechercheEmploiQO rechercheEmploi) {
        return emploiFacadeService.findProchaineDate(rechercheEmploi);
    }
    
}
