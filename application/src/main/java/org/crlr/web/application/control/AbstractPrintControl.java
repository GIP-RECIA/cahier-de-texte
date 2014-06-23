package org.crlr.web.application.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.StateManager;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.UserDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.ArchiveEnseignantDTO;
import org.crlr.dto.application.base.ArchiveEnseignantQO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.seance.PrintSeanceDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.metier.facade.SeanceFacadeService;
import org.crlr.services.AnneeScolaireService;
import org.crlr.services.ArchiveEnseignantService;
import org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener;
import org.crlr.web.application.control.EnseignantControl.EnseignantListener;
import org.crlr.web.application.control.EnseignementControl.EnseignementListener;
import org.crlr.web.application.control.seance.SeanceListControl;
import org.crlr.web.application.form.AbstractPrintForm;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.utils.FacesUtils;

/**
 * 
 *
 * @param <F>
 */
public abstract class AbstractPrintControl<F extends AbstractPrintForm> extends
        AbstractPopupControl<F>  implements ClasseGroupeListener, 
        EnseignementListener,
        EnseignantListener{ 
    
    @ManagedProperty(value = "#{classeGroupe}")
    protected transient ClasseGroupeControl classeGroupeControl;
    
    @ManagedProperty(value = "#{enseignement}")
    protected transient EnseignementControl enseignementControl;
    
    @ManagedProperty(value = "#{enseignant}")
    protected transient EnseignantControl enseignantControl;
    
    @ManagedProperty(value="#{seanceFacade}")
    protected transient SeanceFacadeService seanceFacade;

    /** Service des anneeScolaire. */
    @ManagedProperty(value="#{anneeScolaireService}")
    private transient AnneeScolaireService anneeScolaireService;
    
    @ManagedProperty(value="#{archiveEnseignantService}")
    private transient ArchiveEnseignantService archiveEnseignantService;
    
    @ManagedProperty(value="#{listeSeances}")
    private transient SeanceListControl seanceListeControl;
    
    /**
     * indique si on est en edition d'archive
     */
    public boolean archive = false;
    public boolean enseignant = false;
    
    abstract public PrintSeanceDTO getSeanceSelectionne();
    
    abstract public DevoirDTO getDevoirSelectionne();
    
    /*
     * 
     * POUR test
     * Donne le nom du pdf pour les impression.
     * Partie  non contextuel de l'url
     * 
     * @return String
     *
    abstract public String getPdfUrl();
    */
    
    /**
     * si mode archive d'un enseignant: les données le concernant.
     */
    private List<ArchiveEnseignantDTO> listArchiveEnseignantDTO; 
    /**
     * @param form f
     */
    public AbstractPrintControl(F form) {
        super(form);
        
    }
    
   
    
    
    /**
     * Réinitialisation.
     */
    public void enseignementTousOuUnSelectionne(boolean tous) {
        
        if (tous) {
            //Tous
            chargerListeEnseignant();
        }
        
        resetDonnees();
    }
    
    /* POUR TEST
    public  void printPdf() throws IOException {
		final HttpServletRequest req = FacesUtils.getRequest();
		final FacesContext fc = FacesContext.getCurrentInstance();
		final StateManager sm = fc.getApplication().getStateManager();
		sm.saveView(fc);
		fc.responseComplete();  
		final String urlAvecContexte = FacesUtils.getContextURL(getPdfUrl());
		    
		log.debug("url de forward = {}", urlAvecContexte);      
		FacesContext.getCurrentInstance().getExternalContext().redirect(urlAvecContexte);
    }
*/
    
    /**
     * 
     */
    public void enseignementSelectionnee() {
           
    }
    

    
    /* (non-Javadoc)
     * @see org.crlr.web.application.control.EnseignantControl.EnseignantListener#enseignantTousOuUnSelectionne(boolean)
     */
    @Override
    public void enseignantTousOuUnSelectionne(boolean tous) {
        resetDonnees();
        
    }


    /* (non-Javadoc)
     * @see org.crlr.web.application.control.EnseignantControl.EnseignantListener#enseignantSelectionnee()
     */
    @Override
    public void enseignantSelectionnee() {
        
        resetDonnees(); 
        
    }
    
    
    /**
     * @see org.crlr.web.application.control.AbstractControl#onLoad()
     */
    public void onLoad() {
    	
    	ContexteUtilisateur ctx = ContexteUtils.getContexteUtilisateur();
        final UtilisateurDTO utilisateurDTO =
                ctx.getUtilisateurDTO();
     
        boolean isArchive = ctx.isOutilArchive();
        setArchive(isArchive);
        UserDTO user = utilisateurDTO.getUserDTO();
        Profil profil = utilisateurDTO.getProfil() ;
        boolean isEnseignant = profil == Profil.ENSEIGNANT;
        setEnseignant(isEnseignant);
        
        String uid = (isEnseignant && user != null) ? user.getUid() : null;
        
        
        form.setProfil(profil);
        
        classeGroupeControl.setListener(this);
        enseignementControl.setListener(this);
        enseignantControl.setListener(this);
        
        
        
        rechercherAnneeScolaire(isArchive, uid);

        rechercherClassGroup(isArchive, isEnseignant ? user.getIdentifiant() : null);
        
        //par défaut une recherche unitaire à lieu pour les enseignants
        if (!BooleanUtils.isTrue(form.getAffichageParentEleve())) {
            enseignantControl.getForm().setFiltreParEnseignant(true);
            enseignementControl.getForm().setFiltreParEnseignement(true);
            
        } else {
            enseignantControl.getForm().setFiltreParEnseignant(false);
            enseignementControl.getForm().setFiltreParEnseignement(false);
        }
        
        chargerListeEnseignant();
        rechercherEnseignement(false);
        
    }
    
    /**
     * Charge la liste des annees scolaire si on est en mode archive.
     * Si l'uid n'est pas null on ne cherche que pour l'enseignant correspondant.
     * dans ce cas on charge la liste des établissements de l'enseignant pour chaque année.
     * 
     */
    private void rechercherAnneeScolaire(boolean inArchive, String uid) {
    	listArchiveEnseignantDTO = null;
        if (inArchive  ) {
            try {
                final ResultatDTO<List<AnneeScolaireDTO>> resultatDTO = anneeScolaireService.findListeAnneeScolaire();
                final List<AnneeScolaireDTO> listAnneeScolaire = resultatDTO.getValeurDTO();
                
                if (uid != null) {
	                ArchiveEnseignantQO aeQo = new ArchiveEnseignantQO();
	                aeQo.setUid(uid);
	                aeQo.setAnneeScolaireList(listAnneeScolaire);
	                
	                listArchiveEnseignantDTO = archiveEnseignantService.findAllEtabByUid(aeQo).getValeurDTO();
	                
	                listAnneeScolaire.clear();
	                // on reconstruit la liste de annees scolaire avec ceux ou l'enseignant a des données
	                for (ArchiveEnseignantDTO aeDTO : listArchiveEnseignantDTO) {
						listAnneeScolaire.add(aeDTO.getAnneeScolaire());
					}
	               
                } else {
                	listArchiveEnseignantDTO = null;
                }
                
                form.setListeAnneeScolaire(listAnneeScolaire);
                if (listAnneeScolaire!=null && !listAnneeScolaire.isEmpty()) {
                	Integer idAnnee = listAnneeScolaire.get(0).getId();
                    form.setIdAnneeScolaire(idAnnee);
                    initDate();
                    rechercherEtablissement(idAnnee);
                    
                } else {
                	form.setIdAnneeScolaire(null);
                }
            } catch (MetierException e) {
                log.error("Erreur de chargement des années scolaire", e.toString());
                return;
            }
        } else {
            form.setListeAnneeScolaire(new ArrayList<AnneeScolaireDTO>());
        }
       
    }
    
    protected AnneeScolaireDTO getAnneeScolaireSelectionnee(){
    	List<AnneeScolaireDTO> listAnnee = form.getListeAnneeScolaire();
    	Integer idAnnee = form.getIdAnneeScolaire();
    	if (idAnnee != null && listAnnee != null) {
    		for (AnneeScolaireDTO annee : listAnnee) {
				if (idAnnee.equals(annee.getId())) {
					return annee;
				}
			}
    	}
    	return null;
    }
    
    protected ArchiveEnseignantDTO getArchiveEnseignantDTO () {
    	Integer idAnnee = form.getIdAnneeScolaire();
    	Integer idEtab = form.getIdEtablissement();
    	if (listArchiveEnseignantDTO != null && idAnnee != null && idEtab != null) {
    		for (ArchiveEnseignantDTO archive : listArchiveEnseignantDTO) {
				AnneeScolaireDTO annee = archive.getAnneeScolaire();
				if (idAnnee.equals(annee.getId())){
					return archive;
				}
			}
    	}
    	return null;
    }
    
    void rechercherEtablissement(Integer idAnnee) {
    	form.setListeEtablissement(null);
    	form.setIdEtablissement(null);
    	if (idAnnee == null || listArchiveEnseignantDTO == null ) return;
    	 for (ArchiveEnseignantDTO aeDTO : listArchiveEnseignantDTO) {
			if (idAnnee.equals(aeDTO.getAnneeScolaire().getId())) {
				List<EtablissementDTO> etabList = aeDTO.getEtabList();
				form.setListeEtablissement(etabList);
				form.setIdEtablissement(aeDTO.getIdEtablissementSelected());
				break;
			}
		}
    }
    
    
    private void rechercherClassGroup(boolean inArchive, Integer idEnseignant) {
    	boolean isEnseignant = idEnseignant != null;
    	classeGroupeControl.getForm().setIdEtablissementFiltre(null);
    	if (inArchive) {
    		
    		// anneescolaire selectionne
    			String exercice = form.getExercice();
    		
    		// s'il n'y a pas d'annee selectionné on ne charge pas de classe
    		if ( StringUtils.isBlank(exercice)) return ;
    		
            classeGroupeControl.getForm().setArchive(true);
            classeGroupeControl.getForm().setExercice(exercice);
            if (isEnseignant ) {
            	// pour les enseignants on recupere l'id de l'annee demandée
            	Integer idAnnee = form.getIdAnneeScolaire();
            	Integer idEtab = form.getIdEtablissement();
            	classeGroupeControl.getForm().setIdEtablissementFiltre(idEtab);
            	
            	if (idAnnee != null && idEtab != null && listArchiveEnseignantDTO!= null ) {
            		for (ArchiveEnseignantDTO aeDTO : listArchiveEnseignantDTO) {
            			if (aeDTO.getAnneeScolaire().getId() == idAnnee) {
            				Integer cpt = 0;
            				List<EtablissementDTO> etabList = aeDTO.getEtabList();
            				for (EtablissementDTO etab : etabList) {
								if (etab.getId() == idEtab) {
									break;
								}
								cpt++;
							}
            				Integer idEns = aeDTO.getIdEnsList().get(cpt);
            				classeGroupeControl.getForm().setIdEnseignantFiltre(idEns);
            				break;
            			}
            		}
            	}
            }
            
    	} else {
    		classeGroupeControl.getForm().setArchive(false);
        
    		if (isEnseignant) {
    			classeGroupeControl.getForm().setIdEnseignantFiltre(idEnseignant);
    		}
    	}
    	classeGroupeControl.classeGroupeTypeSelectionne(null);
    }
    
    /**
     * Appel métier de recherche des enseignements.
     */
    
    private void rechercherEnseignement(boolean inArchive) {
        enseignementControl.chargerListeEnseignement(classeGroupeControl
                .getForm().getGroupeClasseSelectionne(), classeGroupeControl
                .getForm().getTypeGroupeSelectionne(), false, inArchive, 
                inArchive ? form.getExercice(): null, classeGroupeControl.getForm().getListeGroupe());
        
    }

    
    
    /**
     * Réinitialisation des critères sélectionnés.
     */
    public void reset() {

        form.reset();
        this.classeGroupeControl.reset();
        
        //Important pour profile éléve / parent où la classe / groupe est présélectionnée.
        this.classeGroupeControl.onLoad();
        onLoad();
    }
    
   
    
    /**
     * Charge les enseignants de la classe.
     * 
     */
    protected void chargerListeEnseignant() {
        
        enseignantControl.chargerListeEnseignant(classeGroupeControl
                .getForm(),  
                enseignementControl.getForm().getEnseignementSelectionne()
                );
    }
    
    /**
     * Reset les données de la formulaire.
     */
    protected abstract void resetDonnees();
    

    /* (non-Javadoc)
     * @see org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener#classeGroupeTypeSelectionne()
     */
    @Override
    public void classeGroupeTypeSelectionne() {
        enseignementControl.chargerListeEnseignement(classeGroupeControl
                .getForm().getGroupeClasseSelectionne(), classeGroupeControl
                .getForm().getTypeGroupeSelectionne(), form
                .getAffichageParentEleve(), false, null, classeGroupeControl.getForm()
                .getListeGroupe());
        enseignementControl.filtreParEnseignementSelectionne(null);
        
        chargerListeEnseignant();        
        enseignantControl.filtreParEnseignantSelectionne(null); 
        
        resetDonnees();
        
    }

    /* (non-Javadoc)
     * @see org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener#classeGroupeSelectionnee()
     */
    @Override
    public void classeGroupeSelectionnee() {
       
        
        enseignementControl.chargerListeEnseignement(classeGroupeControl.getForm().getGroupeClasseSelectionne(),
                classeGroupeControl.getForm().getTypeGroupeSelectionne(),
                form.getAffichageParentEleve(), false, null, 
                classeGroupeControl.getForm().getListeGroupe());
        enseignementControl.filtreParEnseignementSelectionne(null);
        
        if (! isEnseignant()) {
        	chargerListeEnseignant();        
        	enseignantControl.filtreParEnseignantSelectionne(null);   
        }
        resetDonnees();
        
    }

    /**
     * @param classeGroupeControl the classeGroupeControl to set
     */
    public void setClasseGroupeControl(ClasseGroupeControl classeGroupeControl) {
        this.classeGroupeControl = classeGroupeControl;
    }

    /**
     * @param seanceFacade the seanceFacade to set
     */
    public void setSeanceFacade(SeanceFacadeService seanceFacade) {
        this.seanceFacade = seanceFacade;
    }

    /**
     * @param enseignementControl the enseignementControl to set
     */
    public void setEnseignementControl(EnseignementControl enseignementControl) {
        this.enseignementControl = enseignementControl;
    }




    /**
     * @param enseignantControl the enseignantControl to set
     */
    public void setEnseignantControl(EnseignantControl enseignantControl) {
        this.enseignantControl = enseignantControl;
    }

    /**
     * Ouverture d'une seance.
     */
    public void openSeance() {
    	PrintSeanceDTO psDTO = getSeanceSelectionne();
    	if (psDTO != null) {
    		openSeance(psDTO,!psDTO.getOpen() );
    	}
    }
    
    protected void  openSeance(PrintSeanceDTO seance, boolean open) {
    	seance.setOpen(open);
    	for (DevoirDTO devoir : seance.getDevoirs()) {
            devoir.setOpen(open);
        }
    }
    
    /**
     * ouverture/fermeture du devoir sélectionné.
     */
    public void openDevoir(){
        getDevoirSelectionne().setOpen(! getDevoirSelectionne().getOpen());
    }
    
    /**
     * Ouverture de tous les devoirs de la séance sélectionée
     */
    public void openAllDevoirs(){
    	openAllDevoirs(getSeanceSelectionne(), true);
    }
    /**
     * Fermeture des tous les devoirs de la séance sélectionée
     */
    public void closeAllDevoirs(){
    	openAllDevoirs(getSeanceSelectionne(),false);
    	
    }
    
    /**
     * Ouverture/fermeture de tous les devoirs d'une séance. 
     * @param seance
     * @param open
     */
    protected  void openAllDevoirs(PrintSeanceDTO seance, boolean open) {
    	if (seance != null) {
    		for (DevoirDTO devoir : seance.getDevoirs()){
    			devoir.setOpen(open);
    		}
    	}
    }
  
    
    public boolean isClassGroupLoadable(){
    	return true;
    }
    
    /**
     * Declenche par l'IHM lors de la selection d'une annee scolaire.
     * @throws MetierException  e
     */
    public void selectionnerAnneeScolaire() throws MetierException {
        
        form.reset();
        
        final ContexteUtilisateur ctxUtilisateur = ContexteUtils.getContexteUtilisateur(); 

        boolean isArchive = ctxUtilisateur.isOutilArchive();
      /*  // Charge la liste des annee scolaire
        rechercherAnneeScolaire(ctxUtilisateur.isOutilArchive());
       */
        if (isArchive) {
        	
        	rechercherEtablissement(form.getIdAnneeScolaire());
        	initDate();
        }
        
        // Charge la liste de enseignements proposes dans le filtre
        rechercherEnseignement(isArchive);
        
    }
    
    private void initDate(){
    	AnneeScolaireDTO anneeScolaire = getAnneeScolaireSelectionnee();
    	form.setDateDebut(anneeScolaire.getDateRentree());
    	form.setDateFin(anneeScolaire.getDateSortie());
    }
    
    public void selectionnerEtablissement(){
    	 form.reset();
         
         final ContexteUtilisateur ctxUtilisateur = ContexteUtils.getContexteUtilisateur(); 

         boolean isArchive = ctxUtilisateur.isOutilArchive();
         if (isArchive) {
        	 ArchiveEnseignantDTO archive = getArchiveEnseignantDTO();
        	 if (archive != null) {
        		 archive.selectedEtablissement(form.getIdEtablissement());
        	 }
         }
         rechercherEnseignement(isArchive);
    }
    

	public AnneeScolaireService getAnneeScolaireService() {
		return anneeScolaireService;
	}

	public void setAnneeScolaireService(AnneeScolaireService anneeScolaireService) {
		this.anneeScolaireService = anneeScolaireService;
	}

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public ArchiveEnseignantService getArchiveEnseignantService() {
		return archiveEnseignantService;
	}

	public void setArchiveEnseignantService(
			ArchiveEnseignantService archiveEnseignantService) {
		this.archiveEnseignantService = archiveEnseignantService;
	}

	public boolean isEnseignant() {
		return enseignant;
	}

	public void setEnseignant(boolean enseignant) {
		this.enseignant = enseignant;
	}
    
	public SeanceListControl getSeanceListeControl() {
		return seanceListeControl;
	}



	public void setSeanceListeControl(SeanceListControl seanceListeControl) {
		this.seanceListeControl = seanceListeControl;
	}

   
}
