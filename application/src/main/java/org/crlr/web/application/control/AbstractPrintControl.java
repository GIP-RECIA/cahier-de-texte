package org.crlr.web.application.control;

import javax.faces.bean.ManagedProperty;

import org.apache.commons.lang.BooleanUtils;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.seance.PrintSeanceDTO;
import org.crlr.metier.facade.SeanceFacadeService;
import org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener;
import org.crlr.web.application.control.EnseignantControl.EnseignantListener;
import org.crlr.web.application.control.EnseignementControl.EnseignementListener;
import org.crlr.web.application.form.AbstractPrintForm;
import org.crlr.web.contexte.utils.ContexteUtils;

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

    
    abstract public PrintSeanceDTO getSeanceSelectionne();
    
    abstract public DevoirDTO getDevoirSelectionne();
    
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
        final UtilisateurDTO utilisateurDTO =
                ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
     
        form.setProfil(utilisateurDTO.getProfil());
        
        classeGroupeControl.setListener(this);
        enseignementControl.setListener(this);
        enseignantControl.setListener(this);
        
        //par défaut une recherche unitaire à lieu pour les enseignants
        if (!BooleanUtils.isTrue(form.getAffichageParentEleve())) {
            enseignantControl.getForm().setFiltreParEnseignant(true);
            enseignementControl.getForm().setFiltreParEnseignement(true);
            
        } else {
            enseignantControl.getForm().setFiltreParEnseignant(false);
            enseignementControl.getForm().setFiltreParEnseignement(false);
        }
        
        chargerListeEnseignant();
        enseignementControl.chargerListeEnseignement(classeGroupeControl
                .getForm().getGroupeClasseSelectionne(), classeGroupeControl
                .getForm().getTypeGroupeSelectionne(), form
                .getAffichageParentEleve(), false, null, classeGroupeControl.getForm()
                .getListeGroupe());
        
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
        
        chargerListeEnseignant();        
        enseignantControl.filtreParEnseignantSelectionne(null);   
        
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
  
    
}
