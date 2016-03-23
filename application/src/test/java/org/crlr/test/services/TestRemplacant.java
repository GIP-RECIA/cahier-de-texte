package org.crlr.test.services;

import org.crlr.metier.facade.VisaFacadeService;
import org.crlr.services.AnneeScolaireService;
import org.crlr.services.SeanceService;
import org.crlr.services.SequenceService;
import org.crlr.services.VisaService;
import org.crlr.test.AbstractMetierTest;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRemplacant extends AbstractMetierTest {

        @Autowired
        VisaService visaService;

        @Autowired
        VisaFacadeService visaFacade;

        @Autowired
        SequenceService sequenceService;

        @Autowired
        SeanceService seanceService;

        @Autowired
        AnneeScolaireService anneeScolaireService;
        
        //inspecteur de jean-luc.gerbeau BLANC FREDERIC
        //"B1100i23"  frederic.blanc eEhbscGo
        
        //inspecteur de david boyer de marie-pierre.bourdillon 
        //"B1100i2s"  david.boyer    e6gwjk5A
        
        //admin
        //B1103bh7
        //jean-jacques.simonini
        //jbh2d2uU
        
        //marie-pierre.bourdillon
        //marie-pierre.bourdillon test fbu2TmtU fbu2TmtU
        private static final int ID_ENSEIGNANT_REMPLACE = 4877;
        
        //jean-luc.gerbeau  test
        private static final int ID_ENSEIGNANT_REMPLANT = 4898;
        
        //private static final String UID = "kfj000vo";
        
        /*
         * select * 
    from cahier_enseignant ens 
    inner join cahier_etab_enseignant ee ON
    ee.id_enseignant = ens.id
    inner join cahier_etablissement etab ON
    etab.id = ee.id_etablissement
    where
    id_etablissement = 69
    ;
         */
        private static final int ID_ETAB = 69;
}
