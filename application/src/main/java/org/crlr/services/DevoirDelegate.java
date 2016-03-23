/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirDelegate.java,v 1.11 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.services;

import java.util.Date;
import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.ChargeDevoirQO;
import org.crlr.dto.application.devoir.ChargeTravailDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.devoir.RechercheDevoirQO;
import org.crlr.dto.application.devoir.RecherchePieceJointeDevoirQO;
import org.crlr.dto.application.devoir.ResultatRechercheDevoirDTO;
import org.crlr.dto.application.devoir.SaveDevoirQO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirQO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.DevoirFacadeService;
import org.crlr.web.dto.FileUploadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DevoirDelegate.
 *
 * @author $author$
 * @version $Revision: 1.11 $
 */
@Service
public class DevoirDelegate implements DevoirService {
    /**
     * DOCUMENTATION INCOMPLETE!
     */
    @Autowired
    private DevoirFacadeService devoirFacadeService;

    /**
     * Mutateur devoirFacadeService.
     *
     * @param devoirFacadeService devoirFacadeService à modifier
     */
    public void setDevoirFacadeServicee(DevoirFacadeService devoirFacadeService) {
        this.devoirFacadeService = devoirFacadeService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<TypeDevoirDTO>> findListeTypeDevoir(final Integer idEtablissement) {
        return this.devoirFacadeService.findListeTypeDevoir(idEtablissement);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<ResultatRechercheDevoirDTO>> listeDevoirAffichage(RechercheDevoirQO rechercheDevoirQO)
    throws MetierException {
        return this.devoirFacadeService.listeDevoirAffichage(rechercheDevoirQO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<DevoirDTO>> findDevoirForPlanning(RechercheSeanceQO rechercheSeanceQO)
    throws MetierException {
        return this.devoirFacadeService.findDevoirForPlanning(rechercheSeanceQO);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<FileUploadDTO> findPieceJointeDevoir(RecherchePieceJointeDevoirQO recherchePJDevoir)
            throws MetierException {
        return devoirFacadeService.findPieceJointeDevoir(recherchePJDevoir);
    }
    
    /**
     * {@inheritDoc}
     */
    public void deleteTypeDevoir(Integer idTypeDevoir) throws MetierException {
        this.devoirFacadeService.deleteTypeDevoir(idTypeDevoir);
    }

    /**
     * {@inheritDoc}
     */
    public void saveTypeDevoir(TypeDevoirQO typeDevoirQO)
            throws MetierException {
        this.devoirFacadeService.saveTypeDevoir(typeDevoirQO);
    }

    /**
     * {@inheritDoc}
     */
    public DevoirDTO findDetailDevoir(Integer idDevoir) {
        return this.devoirFacadeService.findDetailDevoir(idDevoir);
    }

    /**
     * {@inheritDoc}
     */
    public void saveDevoir(final SaveDevoirQO saveDevoirQO) throws MetierException {
        this.devoirFacadeService.saveDevoir(saveDevoirQO);
    }
    
    /**
     * {@inheritDoc}
     */
    public void deleteDevoir(final Integer idDevoir) throws MetierException {
        devoirFacadeService.deleteDevoir(idDevoir);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public ChargeTravailDTO findChargeTravail(final ChargeDevoirQO chargeDevoir) {
        return devoirFacadeService.findChargeTravail(chargeDevoir);
    }
    
    public ChargeTravailDTO chercherChargeTravailGenerique(final DevoirDTO devoir, final UtilisateurDTO utilisateurDTO) {
        ChargeTravailDTO chargeTravail = new ChargeTravailDTO();
        if (devoir != null) {
            final Date dateRemise = devoir.getDateRemise();
            final Integer idClasse = devoir.getIdClasse();
            final Integer idGroupe = devoir.getIdGroupe();
                        
            if (dateRemise != null && (idClasse != null || idGroupe != null)) {
                final ChargeDevoirQO chargeDevoirQO = new ChargeDevoirQO();
                chargeDevoirQO.setDateDevoir(dateRemise);
                chargeDevoirQO.setIdClasse(idClasse);
                chargeDevoirQO.setIdGroupe(idGroupe);
                chargeDevoirQO.setIdDevoir(devoir.getId());
                chargeDevoirQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
                chargeDevoirQO.setUidEnseignant(utilisateurDTO.getUserDTO().getUid());
                
                chargeTravail = devoirFacadeService.findChargeTravail(chargeDevoirQO);
            }
        }
        devoir.setChargeTravail(chargeTravail);
        return chargeTravail;
    }
    
}
