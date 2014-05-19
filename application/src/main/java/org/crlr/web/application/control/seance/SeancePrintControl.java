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
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.ArchiveSeanceDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.DevoirCompar;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.seance.PrintSeanceDTO;
import org.crlr.dto.application.sequence.PrintSeanceOuSequenceQO;
import org.crlr.dto.application.visa.VisaDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.VisaFacadeService;
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
 
	 /** Le controleur des seances ajout . */
    @ManagedProperty(value="#{ajoutSeance}")
    private transient AjoutSeanceControl ajoutSeance;
    
    @ManagedProperty(value = "#{visaFacade}")
    private transient VisaFacadeService visaFacade;
    
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
     * Rechercher des seances avec les details.
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
            //on complete les recherches car il faut connaître tous les devoirs dès le debut
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
    public void refreshList(){
    	log.debug("refresh list de séance");
    	
    	// on mémorise les listes des seances et devoirs ouverts
    	HashSet<Integer> openedSceance = new HashSet<Integer>();
    	HashSet<DevoirCompar> closedDevoir = new HashSet<DevoirCompar> ();
    	
    	for (PrintSeanceDTO psDTO : form.getListeSeances()){
    		if (psDTO.getOpen()) {
    			openedSceance.add(psDTO.getId());
    			
    			// on memorise les devoirs fermés ainsi les nouveaux devoirs seront ouvert par défaut
	    		for (DevoirDTO devoir : psDTO.getDevoirs()) {
	    			if (!devoir.getOpen()) {
	    				closedDevoir.add(new DevoirCompar(devoir));
	    			}
	    		}
    		}
    	}
    	// on recalcule tout
    	rechercher();
    	// on ouvre celle qui doivent l'êtres
    	for (PrintSeanceDTO psDTO : form.getListeSeances()){
    		boolean open = openedSceance.contains(psDTO.getId());
    		psDTO.setOpen(open);
    		if (open) {
    			for (DevoirDTO devoir : psDTO.getDevoirs()) {
    				devoir.setOpen(! closedDevoir.contains(new DevoirCompar(devoir)));
    			}
    		}
    	}
     	
    };
  
    
    /**
     * Replier / Déplier les séances.
     */
    public void openTout() {
        
        boolean deplier = form.getExisteSeancePlier();
        
        for (PrintSeanceDTO seance : form.getListeSeances()) {
            seance.setOpen(deplier);
            openAllDevoirs(seance, deplier);
        }
    
    }
    /**
     * Ouverture de tous les devoirs de la séance sélectionée
     */
    public void openAllDevoirs(){
    	openAllDevoirs(form.getSeanceSelectionne(), true);
    }
    /**
     * Fermeture des tous les devoirs de la séance sélectionée
     */
    public void closeAllDevoirs(){
    	openAllDevoirs(form.getSeanceSelectionne(),false);
    	
    }
    
    /**
     * Ouverture/fermeture de tous les devoirs d'une séance. 
     * @param seance
     * @param open
     */
    private  void openAllDevoirs(PrintSeanceDTO seance, boolean open) {
    	if (seance != null) {
    		for (DevoirDTO devoir : seance.getDevoirs()){
    			devoir.setOpen(open);
    		}
    	}
    }
    /**
     * ouverture/fermeture du devoir sélectionné.
     */
    public void openDevoir(){
        form.getDevoirSelectionne().setOpen(! form.getDevoirSelectionne().getOpen());
    }
    
    //Declenchee lors du click sur la loupe d'une seance.
    public void chargerSeance() {

        log.debug("chargerSeance");
        
        PrintSeanceDTO psDTO = form.getSeanceSelectionne();
        
        // on teste s'il y a des archives
        if (psDTO != null) {
        	form.setVisualiseArchiveDirecteur(	psDTO.isVisaDirecteurMemo() 
        									&& 	psDTO.isVisaDirecteurPerime());
        	
        	form.setVisualiseArchiveInspecteur(	psDTO.isVisaInspecteurMemo() 
        									&&	psDTO.isVisaInspecteurPerime());
	        form.setVisualiseBack(false);
	        ajoutSeance.alimenterSeance(psDTO.getId());
        	//ajoutSeance.getForm().setSeance( psDTO );
        } else {
        	log.debug("aucune seance de selectionnée");
        }
    }

	public AjoutSeanceControl getAjoutSeance() {
		return ajoutSeance;
	}

	public void setAjoutSeance(AjoutSeanceControl ajoutSeance) {
		this.ajoutSeance = ajoutSeance;
	}
    
	 /**
     * charge l'archive d'une seance correspondant a un visa
     */
    private void chargerArchiveSeance(SeanceDTO seance, VisaDTO visa) { 
        Integer idVisa = visa.getIdVisa();

        try {
            ArchiveSeanceDTO archiveSeance = null;
            archiveSeance = visaFacade.findArchiveSeance(
                    seance.getId(), idVisa).getValeurDTO();

            if (archiveSeance != null) {
               // ajoutSeance.getForm().setSeance(archiveSeance);
                ajoutSeance.alimenterSeance(archiveSeance.getId());
                form.setVisualiseBack(true);
            } else {
                log.error("Archive séance n'a pas été trouvé");
           //     ajoutSeance.getForm().setSeance(new SeanceDTO());
            }
        } catch (MetierException ex) {
            log.debug( "ex", ex);
        }
    }

    public void chargerArchiveDirecteur(){
    	PrintSeanceDTO psDTO= form.getSeanceSelectionne();
    	if (psDTO == null) {
    		log.debug("Aucune seance de sélectionnée");
    		return ;
    	}
    	form.setVisualiseArchiveDirecteur(false);
    	if (psDTO.isVisaDirecteurMemo() && psDTO.isVisaDirecteurPerime()) {
    		chargerArchiveSeance(psDTO, psDTO.getVisaDirecteur());
    	}
    }
    public void chargerArchiveInspecteur(){
    	PrintSeanceDTO psDTO= form.getSeanceSelectionne();
    	if (psDTO == null) {
    		log.debug("Aucune seance de sélectionnée");
    		return ;
    	}
    	form.setVisualiseArchiveInspecteur(false);
    	if (psDTO.isVisaInspecteurMemo() && psDTO.isVisaInspecteurPerime()) {
    		chargerArchiveSeance(psDTO, psDTO.getVisaInspecteur());
    	}
    }
	public VisaFacadeService getVisaFacade() {
		return visaFacade;
	}

	public void setVisaFacade(VisaFacadeService visaFacade) {
		this.visaFacade = visaFacade;
	}
    
    
}
