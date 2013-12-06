/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SeancePrintControl.java,v 1.15 2010/06/01 13:45:24 ent_breyton Exp $
 */

package org.crlr.web.application.control.seance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.seance.PrintSeanceDTO;
import org.crlr.dto.application.sequence.PrintSeanceOuSequenceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.web.application.control.AbstractPrintControl;
import org.crlr.web.application.form.seance.SeancePrintForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.utils.ReportUtils;
import org.springframework.util.CollectionUtils;

/**
 * SeancePrintControl.
 *
 * @author $author$
 * @version $Revision: 1.15 $
 */
@ManagedBean(name = "printSeance")
@ViewScoped
public class SeancePrintControl extends AbstractPrintControl<SeancePrintForm> {
    /**
     * 
     * Constructeur.
     *
     */
    public SeancePrintControl() {
        super(new SeancePrintForm());
        usePopupSequence = false;
        usePopupGroupeClasse = true;
        usePopupSeance = false;
        usePopupEnseignement = true;
    }

    /**
     * 
     */
    @Override
    @PostConstruct
    public void onLoad() {
        
        super.onLoad();
        
        /*
        for (GroupesClassesDTO gc : classeGroupeControl.getForm().getListeGroupeClasse()) {
            if (!gc.getDesignation().equals("2CIRA")) {
                continue;
            }
            classeGroupeControl.getForm().setGroupeClasseSelectionne(gc);
            classeGroupeControl.classeGroupeSelectionnee();
            rechercher();
            break;
        }
           */ 
        
        
    }
    
    
   
    
    
    
    /**
     * Vide la liste des seances après une modification de la classe/groupe selectionner.
     */
    public void resetListeSeances(){
        form.setListeSeances(new ArrayList<PrintSeanceDTO>());
    }
    
    /**
     * @see org.crlr.web.application.control.AbstractPrintControl#resetDonnees()
     */
    public void resetDonnees() {
        resetListeSeances();
        
    }
    
    /**
     * Appel métier pour l'impression pdf.
     * 
     * @throws IOException l'exception potentielle.
     */
    public void print() throws IOException {
        log.debug("----------------- PRINT PDF -----------------");
        if( form.getReport() != null    ) {
            ReportUtils.stream(form.getReport());
        }
    }

    /**
     * Lance l'édition PDF.
     */
    public void printSeance() {   
        log.debug("----------------- PDF -----------------");
        
        final PrintSeanceOuSequenceQO printSeanceQO = new PrintSeanceOuSequenceQO();
        printSeanceQO.setIsPrintSeance(true);
        printSeanceQO.setDateCourante(Calendar.getInstance().getTime());
        printSeanceQO.setAffichage(form.getTypeAffichageSelectionne());
        printSeanceQO.setGroupeClasseSelectionne(classeGroupeControl.getForm().getGroupeClasseSelectionne());
        
        printSeanceQO.setDateDebut(form.getDateDebut());
        printSeanceQO.setDateFin(form.getDateFin());
        printSeanceQO.setEnseignement(enseignementControl.getForm().getEnseignementSelectionne());
        
        printSeanceQO.setEnseignant(enseignantControl.getForm().getEnseignantSelectionne());
                
        printSeanceQO.setVraiOuFauxSautDePage(form.getVraiOuFauxSautPage());
        
        final List<GroupeDTO> listeGroupeDTOSelectionne = new ArrayList<GroupeDTO>();
        final List<GroupeDTO> listeGroupeDTO = classeGroupeControl.getForm().getListeGroupe();
        //Correction de l'anomalie 0000568 si aucun groupe n'est sélectionné alors la recherche ne doit pas s'effectuer,
        //sur l'ensemble des groupes.
        for(final GroupeDTO groupe : listeGroupeDTO) {
            if(groupe.getSelectionner()) {
                listeGroupeDTOSelectionne.add(groupe);
            }
        }
        printSeanceQO.setListeGroupeDTO(listeGroupeDTOSelectionne);

        final UtilisateurDTO utilisateurDTO = 
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        printSeanceQO.setDesignationEtablissement(utilisateurDTO.getDesignationEtablissement());
        
        if(CollectionUtils.isEmpty(form.getListeSeances())){
            rechercher();
        }
       
        try {
            form.setReport(seanceService.printSeance(printSeanceQO, form.getListeSeances()));
        } catch (final MetierException e) {
            form.setReport(null);
            log.debug("{0}", e.getMessage());
        }

    }
    
    /**
     * Rechercher des seance avec le detail.
     */
    public void rechercher(){

        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        final PrintSeanceOuSequenceQO rechercheSeancePrintQO = new PrintSeanceOuSequenceQO();
        
        rechercheSeancePrintQO.setAnneeScolaireDTO(utilisateurDTO.getAnneeScolaireDTO());
        rechercheSeancePrintQO.setIdUtilisateur(utilisateurDTO.getUserDTO().getIdentifiant());
        rechercheSeancePrintQO.setProfil(utilisateurDTO.getProfil());
        rechercheSeancePrintQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
        rechercheSeancePrintQO.setDateCourante(form.getDateCourante());
        
        rechercheSeancePrintQO.setDateDebut(form.getDateDebut());
        rechercheSeancePrintQO.setDateFin(form.getDateFin());
        
        if (null != enseignementControl.getForm().getEnseignementSelectionne()) {
            rechercheSeancePrintQO.setEnseignement(enseignementControl.getForm().getEnseignementSelectionne());
        }
        
        if (null != enseignantControl.getForm().getEnseignantSelectionne()) {
            rechercheSeancePrintQO.setEnseignant(enseignantControl.getForm().getEnseignantSelectionne());
        }

        if (null != classeGroupeControl.getForm().getGroupeClasseSelectionne()) {
            rechercheSeancePrintQO.setGroupeClasseSelectionne(classeGroupeControl.getForm().getGroupeClasseSelectionne());
        } else {
            rechercheSeancePrintQO.setGroupeClasseSelectionne(new GroupesClassesDTO());
            rechercheSeancePrintQO.getGroupeClasseSelectionne().setTypeGroupe(classeGroupeControl.getForm().getTypeGroupeSelectionne());
        }
        
        final List<GroupeDTO> listeGroupeDTOSelectionne = new ArrayList<GroupeDTO>();
        final List<GroupeDTO> listeGroupeDTO = classeGroupeControl.getForm().getListeGroupe();        
        for(final GroupeDTO groupe : listeGroupeDTO) {
            if(groupe.getSelectionner()) {
                listeGroupeDTOSelectionne.add(groupe);
            }
        }
        rechercheSeancePrintQO.setListeGroupeDTO(listeGroupeDTOSelectionne);
        
        try {
            final ResultatDTO<List<PrintSeanceDTO>> res = seanceService.findListeSeanceEdition(rechercheSeancePrintQO);
            //on complete les rechercher car il faut connaître tous les devoirs dès le debut
            form.setListeSeances(res.getValeurDTO());
            
            
        } catch (MetierException e) {
            log.debug("{0}", e.getMessage());
        }


    }
    
    /**
     * Ouverture d'une seance.
     */
    public void open(){
        final Boolean nouvelEtat = ! form.getSeanceSelectionne().getOpen();
        form.getSeanceSelectionne().setOpen(nouvelEtat);
        
        for (DevoirDTO devoir : form.getSeanceSelectionne().getDevoirs()){
            devoir.setOpen(nouvelEtat);
        }        
    }
    
  
    
    /**
     * Replier / Déplier les séances.
     */
    public void openTout() {
        
        boolean deplier = form.getExisteSeancePlier();
        
        for (PrintSeanceDTO seance : form.getListeSeances()) {
            seance.setOpen(deplier);
            
            for (DevoirDTO devoir : seance.getDevoirs()){
                devoir.setOpen(deplier);
            } 
        }
    
    }
    
    
    
    /**
     * ouverture/fermeture de la zone du devoir.
     */
    public void openDevoir(){
        form.getDevoirSelectionne().setOpen(! form.getDevoirSelectionne().getOpen());
    }
   
    
    
}
