/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequenceServiceTest.java,v 1.11 2009/07/30 15:09:26 ent_breyton Exp $
 */

package org.crlr.test.services;

import java.util.List;

import junit.framework.TestCase;

import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.utils.DateUtils;
import org.crlr.web.dto.BarreMoisDTO;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.dto.TypeCouleurJour;

public class TestDateGenerator extends TestCase  {
    
    public void testGenerateMois() {
        List<BarreMoisDTO> liste = GenerateurDTO.generateBarreMois(
                DateUtils.creer(2012, 1, 1),
                DateUtils.creer(2012, 1, 2));
        
        assertEquals(1, liste.size());
        
        liste = GenerateurDTO.generateBarreMois(
                DateUtils.creer(2011, 12, 31),
                DateUtils.creer(2012, 1, 1));
        
        assertEquals(2, liste.size());
    }
    
    public void testGenerateSemaine() {
        
        AnneeScolaireDTO anneeScolaire = new AnneeScolaireDTO();
        anneeScolaire.setDateRentree(DateUtils.creer(2011, 0, 2));
        anneeScolaire.setDateSortie(DateUtils.creer(2011, 0, 3));
        
        String alternanceSemaine = "52:IMPAIR|1:IMPAIR";
        List<BarreSemaineDTO> liste = GenerateurDTO.generateListeSemaine(
                anneeScolaire, alternanceSemaine, true);
        
        assertEquals(2, liste.size());
        assertEquals("52", liste.get(0).getNumeroSemaine());
        assertEquals("1", liste.get(1).getNumeroSemaine());
        
        assertEquals( TypeCouleurJour.IMPAIR
                .getColor(), liste.get(0).getColor());
        assertEquals( TypeCouleurJour.IMPAIR
                .getColor(), liste.get(1).getColor());
        
        //2
        anneeScolaire.setDateRentree(DateUtils.creer(2010, 11, 29));
        anneeScolaire.setDateSortie(DateUtils.creer(2011, 0, 2));
        
        alternanceSemaine = "52:IMPAIR|1:IMPAIR";
        liste = GenerateurDTO.generateListeSemaine(anneeScolaire,
                alternanceSemaine, true);
        
        assertEquals(1, liste.size());
        assertEquals("52", liste.get(0).getNumeroSemaine());
        
        //3
        
        anneeScolaire.setDateRentree(DateUtils.creer(2011, 0, 1));
        anneeScolaire.setDateSortie(DateUtils.creer(2011, 0, 10));
        
        alternanceSemaine = "52:PAIR|1:PAIR|2:IMPAIR";
        liste = GenerateurDTO.generateListeSemaine(anneeScolaire,
                alternanceSemaine, true);
        
        assertEquals(3, liste.size());
        assertEquals("52", liste.get(0).getNumeroSemaine());
        assertEquals("1", liste.get(1).getNumeroSemaine());
        assertEquals("2", liste.get(2).getNumeroSemaine());
        
        assertEquals(DateUtils.creer(2011, 0, 10), liste.get(2).getLundi());
        assertEquals(DateUtils.creer(2011, 0, 16), liste.get(2).getDimanche());
        
        assertEquals(DateUtils.creer(2011, 0, 3), liste.get(1).getLundi());
        assertEquals(DateUtils.creer(2011, 0, 9), liste.get(1).getDimanche());
        
        assertEquals(DateUtils.creer(2010, 11, 27), liste.get(0).getLundi());
        assertEquals(DateUtils.creer(2011, 0, 2), liste.get(0).getDimanche());

        assertEquals( TypeCouleurJour.PAIR
                .getColor(), liste.get(0).getColor());
        assertEquals( TypeCouleurJour.PAIR
                .getColor(), liste.get(1).getColor());
        assertEquals( TypeCouleurJour.IMPAIR
                .getColor(), liste.get(2).getColor());        
        
    }
}