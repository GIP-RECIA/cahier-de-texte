package org.crlr.test.services;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.dto.application.seance.ResultatRechercheSeanceDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.SeanceService;
import org.crlr.test.AbstractMetierTest;
import org.crlr.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;
public class TestSeance extends AbstractMetierTest {

    @Autowired
    SeanceService seanceService;
    
    public void testSeanceAffichage() throws MetierException {
        RechercheSeanceQO rs = new RechercheSeanceQO();
        
        rs.setIdEnseignant(1505);
        rs.setPremierJourSemaine(DateUtils.creer(2011, 5, 14));
        rs.setDernierJourSemaine(DateUtils.creer(2011, 5, 27));
        
        rs.setJourCourant(DateUtils.creer(2011, 9, 27));
        rs.setCodeClasseGroupe("CLA662");
        rs.setTypeGroupe(TypeGroupe.CLASSE);
        GroupeDTO grp = new GroupeDTO();
        grp.setId(2007);
        rs.setListeGroupeDTO(new ArrayList<GroupeDTO>());
        rs.getListeGroupeDTO().add(grp);
        
        List<ResultatRechercheSeanceDTO> seq =
                seanceService.listeSeanceAffichage(rs).getValeurDTO();
        
        assertTrue(seq.size() == 3);
        
        rs.setCodeClasseGroupe("GRP2007");
        rs.setIdClasseGroupe(null);
        rs.setListeGroupeDTO(null);
        rs.setTypeGroupe(TypeGroupe.GROUPE);
        
        seq =
                seanceService.listeSeanceAffichage(rs).getValeurDTO();
        
        assertTrue(seq.size() == 2);
        
        //Inspecteur
        rs = new RechercheSeanceQO();
        
        rs.setPremierJourSemaine(DateUtils.creer(2011, 5, 14));
        rs.setDernierJourSemaine(DateUtils.creer(2011, 5, 27));
        
        rs.setJourCourant(DateUtils.creer(2011, 9, 27));
        
        rs.setIdEnseignant(null);
        rs.setIdInspecteur(20);
        rs.setIdClasseGroupe(null);
        rs.setCodeClasseGroupe("CLA662");
        rs.setTypeGroupe(TypeGroupe.CLASSE);
        rs.setListeGroupeDTO(new ArrayList<GroupeDTO>());
        rs.getListeGroupeDTO().add(grp);
        rs.setIdEtablissement(83);
        
        seq =
                seanceService.listeSeanceAffichage(rs).getValeurDTO();
        
        assertTrue(seq.size() > 0);
        
        rs.setCodeClasseGroupe("GRP2007");
        rs.setIdClasseGroupe(null);
        rs.setListeGroupeDTO(null);
        rs.setTypeGroupe(TypeGroupe.GROUPE);
        
        seq =
                seanceService.listeSeanceAffichage(rs).getValeurDTO();
        
        assertTrue(seq.size() > 0);
        
        //eleve
        rs = new RechercheSeanceQO();
        
        rs.setPremierJourSemaine(DateUtils.creer(2011, 5, 14));
        rs.setDernierJourSemaine(DateUtils.creer(2011, 5, 27));
        
        rs.setJourCourant(DateUtils.creer(2011, 9, 27));
        
        rs.setIdClasseGroupe(null);
        rs.setListeGroupeDTO(null);
        rs.setIdEleve(4);
        rs.setIdInspecteur(null);
        rs.setTypeGroupe(null);
        rs.setCodeClasseGroupe(null);
        
        seq =
                seanceService.listeSeanceAffichage(rs).getValeurDTO();
        
        assertTrue(seq.size() > 0);
        
        rs = new RechercheSeanceQO();
        
        rs.setCodeClasseGroupe("CLA662");
        rs.setTypeGroupe(TypeGroupe.CLASSE);
        rs.setListeGroupeDTO(new ArrayList<GroupeDTO>());
        rs.getListeGroupeDTO().add(grp);
        rs.setIdEtablissement(83);
        
        rs.setPremierJourSemaine(DateUtils.creer(2011, 5, 14));
        rs.setDernierJourSemaine(DateUtils.creer(2011, 5, 27));
        
        rs.setJourCourant(DateUtils.creer(2011, 9, 27));
        
        seq =
                seanceService.listeSeanceAffichage(rs).getValeurDTO();
        
        assertTrue(seq.size() > 0);
        rs.setIdClasseGroupe(null);
        rs.setCodeClasseGroupe("GRP2007");
        rs.setTypeGroupe(TypeGroupe.GROUPE);
        rs.setListeGroupeDTO(new ArrayList<GroupeDTO>());
        rs.getListeGroupeDTO().add(grp);
        
        seq =
                seanceService.listeSeanceAffichage(rs).getValeurDTO();
        
        assertTrue(seq.size() > 0);
    }
    
}
