/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: UserBouchonDTO.java,v 1.8 2009/04/22 14:33:30 vibertd Exp $
 */

package org.crlr.web.bouchon;

import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.UtilisateurDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author breytond.
 * @version $Revision: 1.8 $
  */
public final class UserBouchonDTO {
    /**
     * Constructeur.
     */
    private UserBouchonDTO() {
        
    }
    /**
     * Get user bouchon.
     *
     * @return user bouchon.
     */
    public static UtilisateurDTO getUserDTO() {
        
        final UtilisateurDTO utilisateurDTO = new UtilisateurDTO();

        final AnneeScolaireDTO anneeScolaireDTO = new AnneeScolaireDTO();
        anneeScolaireDTO.setId(1);

        utilisateurDTO.getUserDTO().setIdentifiant(54);
        utilisateurDTO.setIdEtablissement(10);
        utilisateurDTO.getUserDTO().setNom("HOLLARD");
        utilisateurDTO.getUserDTO().setPrenom("Yohann");
        utilisateurDTO.setProfil(Profil.ELEVE);
        utilisateurDTO.getUserDTO().setUid("KHY00009");
        
        utilisateurDTO.getUserDTO().setIdentifiant(1);
        utilisateurDTO.setIdEtablissement(10);
        utilisateurDTO.getUserDTO().setNom("ALAMARTINE");
        utilisateurDTO.getUserDTO().setPrenom("FRANCOISE");
        utilisateurDTO.setProfil(Profil.ENSEIGNANT);
        utilisateurDTO.getUserDTO().setUid("KAF0002u");
        
        utilisateurDTO.getUserDTO().setIdentifiant(1);
        utilisateurDTO.setIdEtablissement(1);
        utilisateurDTO.getUserDTO().setNom("DIRECTION");
        utilisateurDTO.getUserDTO().setPrenom("direction");
        utilisateurDTO.setProfil(Profil.DIRECTION_ETABLISSEMENT);
        utilisateurDTO.getUserDTO().setUid("directionuid");
        
        utilisateurDTO.setAnneeScolaireDTO(anneeScolaireDTO);
        
        return utilisateurDTO;
        
    }
}
