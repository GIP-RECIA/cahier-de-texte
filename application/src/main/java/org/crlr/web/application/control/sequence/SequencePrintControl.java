/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SequencePrintControl.java,v 1.10 2010/06/01 13:45:24 ent_breyton Exp $
 */

package org.crlr.web.application.control.sequence;

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
import org.crlr.dto.application.sequence.PrintSequenceDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.web.application.control.AbstractPrintControl;
import org.crlr.web.application.form.sequence.SequencePrintForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.utils.ReportUtils;
import org.springframework.util.CollectionUtils;

/**
 * SequencePrintControl.
 * 
 * @author $author$
 * @version $Revision: 1.10 $
 */
@ManagedBean(name = "printSequence")
@ViewScoped
public class SequencePrintControl extends
        AbstractPrintControl<SequencePrintForm> {
    
    
    /**
     * 
     * Constructeur.
     * 
     */
    public SequencePrintControl() {
        super(new SequencePrintForm());
        usePopupSequence = false;
        usePopupGroupeClasse = true;
        usePopupSeance = false;
    }

    /**
     * 
     */
    @Override
    @PostConstruct
    public void onLoad() {

        super.onLoad();
        
        
        /*
         * BOUCHON 
         * classeGroupeControl.getForm().setGroupeClasseSelectionne(
         * classeGroupeControl.getForm().getListeGroupeClasse().get(2));
         * classeGroupeControl.classeGroupeSelectionnee(); rechercher();
         */

    }
    
   

  

    /**
     * Vide la liste des sequences après une modification de la classe/groupe
     * selectionner.
     */
    public void resetListeSequence() {
        form.getListeSequences().clear();
    }

    /**
     * @see org.crlr.web.application.control.AbstractPrintControl#resetDonnees()
     */
    public void resetDonnees() {
        resetListeSequence();
    }

    /**
     * Appel métier pour l'impression pdf.
     * 
     * @throws IOException
     *             l'exception potentielle.
     */
    public void print() throws IOException {
        log.debug("----------------- PRINT PDF -----------------");
        if (form.getReport() != null) {
            ReportUtils.stream(form.getReport());
        }
    }

    /**
     * Lance l'édition PDF.
     */
    public void printSequence() {
        log.debug("----------------- PDF -----------------");

        final PrintSeanceOuSequenceQO printSequenceQO = new PrintSeanceOuSequenceQO();
        printSequenceQO.setIsPrintSeance(false);
        printSequenceQO.setDateCourante(Calendar.getInstance().getTime());
        printSequenceQO.setAffichage(form.getTypeAffichageSelectionne());
        printSequenceQO.setGroupeClasseSelectionne(classeGroupeControl.getForm()
                .getGroupeClasseSelectionne());

        printSequenceQO.setDateDebut(form.getDateDebut());
        printSequenceQO.setDateFin(form.getDateFin());
        
        
        
        if(null != enseignementControl.getForm().getEnseignementSelectionne()) {
            printSequenceQO.setEnseignement(enseignementControl.getForm().getEnseignementSelectionne());
        }

        printSequenceQO.setVraiOuFauxSautDePage(form.getVraiOuFauxSautPage());

        final List<GroupeDTO> listeGroupeDTOSelectionne = new ArrayList<GroupeDTO>();
        final List<GroupeDTO> listeGroupeDTO = classeGroupeControl.getForm().getListeGroupe();
        // Correction de l'anomalie 0000568 si aucun groupe n'est sélectionné
        // alors la recherche ne doit pas s'effectuer,
        // sur l'ensemble des groupes.
        for (GroupeDTO groupe : listeGroupeDTO) {
            if (groupe.getSelectionner()) {
                listeGroupeDTOSelectionne.add(groupe);
            }
        }
        printSequenceQO.setListeGroupeDTO(listeGroupeDTOSelectionne);

        final UtilisateurDTO utilisateurDTO = ContexteUtils
                .getContexteUtilisateur().getUtilisateurDTO();
        printSequenceQO.setDesignationEtablissement(utilisateurDTO
                .getDesignationEtablissement());

        if (CollectionUtils.isEmpty(form.getListeSequences())) {
            rechercher();
        }
        
        try {
            form.setReport(sequenceService.printSequence(printSequenceQO,
                    form.getListeSequences()));
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
        
        if(null != enseignementControl.getForm().getEnseignementSelectionne()) {
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
            final ResultatDTO<List<PrintSequenceDTO>> res = seanceFacade.findListeSequencesEdition(rechercheSeancePrintQO);
            final List<PrintSequenceDTO> listeSequence = res.getValeurDTO();
            
            for (PrintSequenceDTO sequence : listeSequence) {
                final List<PrintSeanceDTO> listeSeance = sequence.getListeSeances();
                
                //On regroupe par sequences & on force l'ouverture des seances
                for (PrintSeanceDTO seance : listeSeance){
                    seance.setOpen(false);                                       
                }
            }
            
            form.setListeSequences(listeSequence);
            
            log.info("Liste seq, size {0}", listeSequence.size());
        } catch (MetierException e) {
            log.debug("{0}", e.getMessage());
        }
        
        //Ouvre la première sequence
        
        if (!CollectionUtils.isEmpty(form.getListeSequences()))  {
            form.setSequenceSelectionne(form.getListeSequences().get(0));
            open();
        }
        
        log.info("Liste seq, size {0}", form.getListeSequences().size());
    }

    /**
     * Ouverture d'une sequence.
     */
    public void open() {
        final Boolean nouvelEtat = !form.getSequenceSelectionne().getOpen();
        form.getSequenceSelectionne().setOpen(nouvelEtat);
                    
        for (PrintSeanceDTO seance : form.getSequenceSelectionne()
                .getListeSeances()) {
            
            seance.setOpen(nouvelEtat);
            for (DevoirDTO devoir : seance.getDevoirs()) {
                devoir.setOpen(nouvelEtat);
            }
        }
       
    }

    /**
     * Ouverture d'une seance.
     */
    public void openSeance() {
        final Boolean nouvelEtat = !form.getSeanceSelectionne().getOpen();
        form.getSeanceSelectionne().setOpen(nouvelEtat);

        for (DevoirDTO devoir : form.getSeanceSelectionne().getDevoirs()) {
            devoir.setOpen(nouvelEtat);
        }

    }

    


    /**
     * ouverture/fermeture de la zone du devoir.
     */
    public void openDevoir() {
        form.getDevoirSelectionne().setOpen(
                !form.getDevoirSelectionne().getOpen());
    }

  
}
