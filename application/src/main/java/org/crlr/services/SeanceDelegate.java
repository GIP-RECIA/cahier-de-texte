/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SeanceDelegate.java,v 1.17 2009/07/30 15:09:26 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EtablissementSchemaQO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.seance.ConsulterSeanceDTO;
import org.crlr.dto.application.seance.ConsulterSeanceQO;
import org.crlr.dto.application.seance.PrintSeanceDTO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.dto.application.seance.ResultatRechercheSeanceDTO;
import org.crlr.dto.application.seance.SaveSeanceQO;
import org.crlr.dto.application.sequence.PrintSeanceOuSequenceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.SeanceFacadeService;
import org.crlr.report.Report;
import org.crlr.web.dto.FileUploadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SeanceDelegate.
 *
 * @author $author$
 * @version $Revision: 1.17 $
 */
@Service
public class SeanceDelegate implements SeanceService {
    /** Injection spring automatique de la seanceFacade. */
    @Autowired
    private SeanceFacadeService seanceFacadeService;

    /**
     * Mutateur seanceFacadeService.
     *
     * @param seanceFacadeService seanceFacadeService à modifier
     */
    public void setSeanceFacadeService(SeanceFacadeService seanceFacadeService) {
        this.seanceFacadeService = seanceFacadeService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<ResultatRechercheSeanceDTO>> findSeance(final RechercheSeanceQO rechercheSeanceQO)
        throws MetierException {
        return seanceFacadeService.findSeance(rechercheSeanceQO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<SeanceDTO>> findSeanceForPlanning(
            RechercheSeanceQO rechercheSeanceQO) throws MetierException {
        return seanceFacadeService.findSeanceForPlanning(rechercheSeanceQO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveSeance(SaveSeanceQO saveSeanceQO)
                       throws MetierException {
        return seanceFacadeService.saveSeance(saveSeanceQO, saveSeanceQO.getMode());
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> deleteSeance(SeanceDTO resultatRechercheSeanceDTO)
                      throws MetierException {
        return seanceFacadeService.deleteSeance(resultatRechercheSeanceDTO);
    }


    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<DevoirDTO>> findDevoir(Integer idSeance){
        return seanceFacadeService.findDevoir(idSeance);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<FileUploadDTO>> findPieceJointe(Integer idSeance){
        return seanceFacadeService.findPieceJointe(idSeance);
    }

    /**
     * {@inheritDoc}
     */
    public ConsulterSeanceDTO consulterSeance(final ConsulterSeanceQO consulterSeanceQO)
            throws MetierException {
        return seanceFacadeService.consulterSeance(consulterSeanceQO);
    }
    
    /**
     * {@inheritDoc}
     */
    public SeanceDTO rechercherSeance(
            ConsulterSeanceQO consulterSeanceQO) throws MetierException {
        return seanceFacadeService.rechercherSeance(consulterSeanceQO);
    }

    /**
     * {@inheritDoc}
     */
    public ConsulterSeanceDTO chercherSeancePrecedente(final RechercheSeanceQO chercherSeanceQO)
    throws MetierException {
        return seanceFacadeService.chercherSeancePrecedente(chercherSeanceQO);
    }

    /**
     * {@inheritDoc}
     */
    public List<SeanceDTO> chercherListeSeancePrecedente(final RechercheSeanceQO chercherSeanceQO, final Integer nbrSeance)
    throws MetierException {
        return seanceFacadeService.chercherListeSeancePrecedente(chercherSeanceQO, nbrSeance);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> modifieSeance(
            SeanceDTO resultatRechercheSeanceDTO) throws MetierException {
        return seanceFacadeService.modifieSeance(resultatRechercheSeanceDTO);
    }

    /**
     * {@inheritDoc}
     */
    public boolean verifieAppartenancePieceJointe(FileUploadDTO fileUploadDTO)
            throws MetierException {
        return seanceFacadeService.verifieAppartenancePieceJointe(fileUploadDTO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<ResultatRechercheSeanceDTO>> listeSeanceAffichage(
            RechercheSeanceQO rechercheSeanceQO) throws MetierException {
        return seanceFacadeService.listeSeanceAffichage(rechercheSeanceQO);
    }

    /**
     * {@inheritDoc}
     */
    public Integer findIdEtablissementSuivantSchema(
            EtablissementSchemaQO etablissementSchemaQO) throws MetierException {
        return seanceFacadeService.findIdEtablissementSuivantSchema(etablissementSchemaQO);
    }

    /**
     * {@inheritDoc}
     */
    public Report printSeance(PrintSeanceOuSequenceQO printSeanceQO, List<PrintSeanceDTO> list)
         throws MetierException {
        return seanceFacadeService.printSeance(printSeanceQO, list);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<PrintSeanceDTO>> findListeSeanceEdition(
            PrintSeanceOuSequenceQO rechercheSeancePrintQO)
            throws MetierException {
        return seanceFacadeService.findListeSeanceEdition(rechercheSeancePrintQO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<FileUploadDTO>> findPieceJointeDevoir(
            Integer idDevoir) {
        return seanceFacadeService.findPieceJointeDevoir(idDevoir);
    }

    

}
