/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SimulationEleveControl.java,v 1.13 2010/06/08 12:24:06 ent_breyton Exp $
 */

package org.crlr.web.application.control.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.UserDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.RechercheGroupeQO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.securite.AuthentificationQO;
import org.crlr.dto.securite.TypeAuthentification;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.ConfidentialiteService;
import org.crlr.services.GroupeClasseService;
import org.crlr.web.application.control.AbstractControl;
import org.crlr.web.application.control.ClasseGroupeControl;
import org.crlr.web.application.control.ClasseGroupeControl.ClasseGroupeListener;
import org.crlr.web.application.form.admin.SimulationEleveForm;
import org.crlr.web.contexte.ContexteApplication;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.contexte.utils.ContexteUtils;

/**
 * AdminControl.
 *
 * @author breytond
 * @version $Revision: 1.13 $
 */
@ManagedBean(name="simulationEleve")
@ViewScoped
public class SimulationEleveControl extends AbstractControl<SimulationEleveForm> implements ClasseGroupeListener {

    @ManagedProperty(value = "#{classeGroupe}")
    private transient ClasseGroupeControl classeGroupeControl;
   
    /** groupeClasseService. */
    @ManagedProperty(value = "#{groupeClasseService}")
    protected transient GroupeClasseService groupeClasseService;
    
    /** confidentialiteService. */
    @ManagedProperty(value = "#{confidentialiteService}")
    private transient ConfidentialiteService confidentialiteService;
    
   /**
     * Mutateur de classeGroupeControl {@link #classeGroupeControl}.
     * @param classeGroupeControl le classeGroupeControl to set
     */
    public void setClasseGroupeControl(ClasseGroupeControl classeGroupeControl) {
        this.classeGroupeControl = classeGroupeControl;
    }
    
    /**
     * Mutateur de groupeClasseService {@link #groupeClasseService}.
     * @param groupeClasseService le groupeClasseService to set
     */
    public void setGroupeClasseService(GroupeClasseService groupeClasseService) {
        this.groupeClasseService = groupeClasseService;
    }
    
    /**
     * Mutateur de confidentialiteService {@link #confidentialiteService}.
     * @param confidentialiteService le confidentialiteService to set
     */
    public void setConfidentialiteService(
            ConfidentialiteService confidentialiteService) {
        this.confidentialiteService = confidentialiteService;
    }

    /**
     * Constructeur.
     */
    public SimulationEleveControl() {
        super(new SimulationEleveForm());        
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    public void onLoad() {
        classeGroupeControl.setListener(this);
        final ContexteUtilisateur contexteUtilisateur =  ContexteUtils.getContexteUtilisateur();
        if (contexteUtilisateur.getUtilisateurDTOOrigine() != null) {
            form.setEleveSelected(contexteUtilisateur.getUtilisateurDTO().getUserDTO());
        }
    }
    
    /**
     * Valide le passage en mode simulation eleve avec l'élève sélectionné.
     * Complete les infos de securite a partir de l'uid de l'eleve selectionne. 
     */
    public void valider() {
        if (form.getEleveSelected()==null) { return; }
        final String uid = form.getEleveSelected().getUid();

        // Construit les critere d'auth
        final AuthentificationQO criteres = new AuthentificationQO();
        final TypeAuthentification typeAuthentification = TypeAuthentification.CAS;
        criteres.setIdentifiant(uid);
        criteres.setTypeAuthentification(typeAuthentification);
        final ContexteApplication contexteApplication = ContexteUtils.getContexteApplication();
        criteres.setEnvironnement(contexteApplication.getEnvironnement());
        criteres.setMapProfil(contexteApplication.getMapProfil());
        criteres.setGroupsADMCentral(contexteApplication.getGroupsADMCentral());
        criteres.setRegexpAdmLocal(contexteApplication.getRegexpAdmLocal());
        
        // Recupere l'auth de l'eleve
        ResultatDTO<UtilisateurDTO> resultat;
        try {
            resultat = confidentialiteService.initialisationAuthentification(criteres);
            
            final UtilisateurDTO utilisateurDTO = resultat.getValeurDTO();
            final ContexteUtilisateur contextUtilisateur = ContexteUtils.getContexteUtilisateur();
            
            // Si on est pas en train de simuler un eleve, on stock les info du directeur
            if (contextUtilisateur.getUtilisateurDTOOrigine() == null) {
                contextUtilisateur.setUtilisateurDTOOrigine(contextUtilisateur.getUtilisateurDTO());
            } 
            
           // Positionne dans le contexte l'utilisateurDTO de l'eleve
            contextUtilisateur.setUtilisateurDTO(utilisateurDTO);
            
            log.error("Changement vers l'eleve : " + uid);
            
        } catch (MetierException e) {
            log.error("Une erreur est survenue lors du passage en simulation de l'eleve : " + uid);
        }

        
    }
    
    /**
     * Retour au mode normal.
     */
    public void annuler() {
        final ContexteUtilisateur contextUtilisateur = ContexteUtils.getContexteUtilisateur();
        if (contextUtilisateur.getUtilisateurDTOOrigine()!=null) {
            contextUtilisateur.setUtilisateurDTO(contextUtilisateur.getUtilisateurDTOOrigine());
            contextUtilisateur.setUtilisateurDTOOrigine(null);
        }
    }
    /**
     * Methode appelee suite a la selection d'une classe ou d'un groupe.
     * Charge la liste des eleves de cette classe ou de ce groupe et reset l'eleve selectionnee. 
     */
    public void classeGroupeSelectionnee() {
        
        // Reset de l'eleve selected courant.
        form.setEleveSelected(null);
        
        // Charge la liste des eleves de la classe
        final List<UserDTO> listeEleve; 
        final RechercheGroupeQO rechercheGroupeQO = new RechercheGroupeQO();
        if (classeGroupeControl.getForm().getGroupeClasseSelectionne()!= null 
         && classeGroupeControl.getForm().getGroupeClasseSelectionne().getId() != null) {
            final GroupesClassesDTO groupeClasse = classeGroupeControl.getForm().getGroupeClasseSelectionne(); 
            if (groupeClasse.getTypeGroupe().equals(TypeGroupe.CLASSE)) {
                rechercheGroupeQO.setIdClasse(groupeClasse.getId()); 
            } else {
                rechercheGroupeQO.setIdGroupe(groupeClasse.getId());
            }
            listeEleve = groupeClasseService.findListeEleve(rechercheGroupeQO);
        } else {
            listeEleve = new ArrayList<UserDTO>();
        }
        
        // Positionne la liste des eleves trouvees dans le form
        form.setListeEleve(listeEleve);
    }
    
    /**
     * Methode appelee suite a la selection d'un eleve dans la popup de choix eleve.
     */
    public void selectionnerEleve() {
        
    }

    /**
     * Appelle lors du choix du type groupe ou classe.
     */
    public void classeGroupeTypeSelectionne() {
    }
    
}
