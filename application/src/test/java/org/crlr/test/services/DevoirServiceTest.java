/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirServiceTest.java,v 1.10 2010/04/01 11:06:15 ent_breyton Exp $
 */

package org.crlr.test.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.DevoirService;
import org.crlr.test.AbstractMetierTest;
import org.crlr.web.application.control.EnseignementControl;
import org.crlr.web.application.control.devoir.DevoirControl;
import org.crlr.web.contexte.ContexteApplication;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.BarreSemaineDTO;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * Test Junit pour DevoirService.
 *
 * @author $author$
 * @version $Revision: 1.10 $
 */
public class DevoirServiceTest extends AbstractMetierTest {
    /** Le service à utiliser. */
    @Autowired
    private DevoirService devoirService;
    
    static {
        ContexteApplication.CONFIG_PROPERTIES_URL = "test-config.properties";
    }
    
    @org.junit.Test
    public void testDevoirParSemaine() {
        UtilisateurDTO testUser = new UtilisateurDTO();
       // ContexteUtils.isTest = true;
        testUser.getUserDTO().setUid("z130000x");
        testUser.setProfil(Profil.ELEVE);
        testUser.getUserDTO().setIdentifiant(18);
        ContexteUtils.getContexteUtilisateur().setUtilisateurDTO(testUser);
        
        DevoirControl dc = new DevoirControl();
        dc.getForm().setVraiOuFauxRechercheActive(true);
        dc.getForm().setTypeAffichage("SEMAINE");
        
        Date dateDebut = org.crlr.utils.DateUtils.creer(2013, 10-1, 7);
        Date dateFin = org.crlr.utils.DateUtils.creer(2013, 10-1, 13);
        
        dc.getForm().setSemaineSelectionne(new BarreSemaineDTO());
        dc.getForm().getSemaineSelectionne().setLundi(dateDebut);
        dc.getForm().getSemaineSelectionne().setDimanche(dateFin);
        
        dc.setEnseignementControl(new EnseignementControl()); 
        
        dc.setDevoirService(devoirService);
        
        dc.getDevoir();
        
        /*
        select seq.id_classe, seq.id_groupe, d.date_remise, * 
        from cahier_seance s 
        inner join cahier_devoir d on s.id = d.id_seance
        inner join cahier_sequence seq on seq.id = s.id_sequence
        where seq.id_classe = 8
        order by seq.id_classe, d.date_remise
        ;*/
        
        assertEquals((Integer)36, dc.getForm().getListe().get(0).getMap().get("LUNDI").getId());
        assertEquals((Integer)37, dc.getForm().getListe().get(0).getMap().get("MARDI").getId());
        assertEquals((Integer)null, dc.getForm().getListe().get(0).getMap().get(TypeJour.MERCREDI.name()).getId());
        
    }

    /**
     * Test sans erreur de getListeTypeDevoir.
     *
     * @throws MetierException .
     * 
     * 
     */
    @org.junit.Test
    public void testFindListeTypeDevoir() throws MetierException {
        
        final ResultatDTO<List<TypeDevoirDTO>> resultat =
                devoirService.findListeTypeDevoir(4);
        
        final List<TypeDevoirDTO> typeDevoirDTOs = resultat.getValeurDTO();

        assertEquals(3, typeDevoirDTOs.size());
        
        boolean[] found = new boolean[3];
        
        for (TypeDevoirDTO typeDevoirDTO : typeDevoirDTOs) {
            final int idTypeDevoir = typeDevoirDTO.getId();
            final String libelleDevoir = typeDevoirDTO.getLibelle();
            switch (idTypeDevoir) {
                case 10:
                    assertFalse(found[0]);
                    found[0] = true;
                    assertEquals(libelleDevoir, "Devoir maison");
                    break;
                case 11:
                    assertFalse(found[1]);
                    found[1] = true;
                    
                    assertEquals(libelleDevoir, "Exercice(s)");
                    break;
                case 12:
                    assertFalse(found[2]);
                    found[2] = true;
                    
                    assertEquals(libelleDevoir, "Autre");
                    break;
                        
                    
                default:
                    assertTrue("testgetListeTypeDevoir NOK : A renvoyer un type de devoir incorrect", false);
            }
        }
        
    }
    
    

     

    /**
     * Mutateur devoirService.
     *
     * @param devoirService Le devoirService à modifier
     */
    public void setDevoirService(DevoirService devoirService) {
        this.devoirService = devoirService;
    }

    

   
}
