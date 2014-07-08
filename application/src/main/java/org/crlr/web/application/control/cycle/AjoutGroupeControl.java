package org.crlr.web.application.control.cycle;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignantsClasseGroupeQO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.services.EtablissementService;
import org.crlr.services.GroupeClasseService;
import org.crlr.web.application.control.AbstractPopupControl;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.utils.NavigationUtils;

/**
 * Controleur associe au formulaire d'ajout/consultation des groupes collaboratifs locaux.
 * @author G-SAFIR-FRMP
 */
@ManagedBean(name = "ajoutModifGroupe")
@ViewScoped
public class AjoutGroupeControl extends AbstractPopupControl<AjoutGroupeForm> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6291879269098104515L;


	/** Identifiant du controleur. Sert lors du retour d'une navigation vers un sous ecran. */
    private final static String IDFORM = AjoutGroupeControl.class.getName() + "form";
    
    
    /** groupeClasseService. */
    @ManagedProperty(value = "#{groupeClasseService}")
    private transient GroupeClasseService groupeClasseService;
    
    @ManagedProperty(value = "#{etablissementService}")
   private transient EtablissementService etablissementService;
    
    /** Le logger. */
    protected static final Log log = LogFactory.getLog(AjoutGroupeControl.class);
    
    /**
     * les id des membres du groupe selectionné avant modif .
     */
    private Set<Integer> idMemberInbase ;
    
    private Map<String, EnseignantDTO> uid2enseignant ;
    
    private Map<Integer, GroupeDTO> id2groupe;

    // *************************************************************************
    // Construction / Initialisation 
    // *************************************************************************
    /**
     * @param form
     */
    public AjoutGroupeControl() {
        super(new AjoutGroupeForm());
    }

    /**
     * {@inheritDoc}
     */    
    @PostConstruct
    public void onLoad() {
        
       // Gestion du retour depuis un sous ecran
        final AjoutGroupeForm formSave =
            (AjoutGroupeForm) ContexteUtils.getContexteOutilControl()
                                               .recupererEtSupprimerObjet(IDFORM);
        if (formSave == null) {
            initFormulaire();
        } else {
            form = formSave;
        }
    }

    /**
     * Initialise le formulaire pour une création (ou modification).
     */
    private void initFormulaire() {
        form.reset();
       
        // Recupere l'utilisateur
        final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        
      
       
            // Recupere l'utilisateur qui est un enseignant 
       /*     final EnseignantDTO enseignant = new EnseignantDTO();
            enseignant.setId(utilisateurDTO.getUserDTO().getIdentifiant());
            enseignant.setUid(utilisateurDTO.getUserDTO().getUid());
        */  
            form.setAfficheRetour(false);
            
        // Contexte de consultation / modification 
        
       //     form.setAfficheRetour(true);
            
            
                
            form.setSelectedIdGroup(null);
        // Charge les groupes collaboratifs
       initialiseListeGroupe(utilisateurDTO);
              
    }
    
    
    
    /**
     * Initialise la liste des groupes collaboratifs .
     * @param utilisateurDTO l'utilisateur
     */
    private void initialiseListeGroupe(final UtilisateurDTO utilisateurDTO) {
    	
        final List<GroupeDTO> listeGroupe = groupeClasseService.findGroupesCollaboratifLocauxEnseignant(utilisateurDTO.getUserDTO().getIdentifiant());
        form.setListeGroupe(listeGroupe);
      
        int size = listeGroupe.size();
        id2groupe = new HashMap<Integer, GroupeDTO>(size);
        
        log.debug("nb groupe collaboratif = {0}", size);
        
        for (GroupeDTO grp : listeGroupe) {
			if (grp != null) {
				Integer id = grp.getId();
				if (id != null) {
					id2groupe.put(id, grp);
				} else {
					log.warning("groupe ({0}) avec  id null ", grp.getCode());
				}
			}
		}
      
        
        
        
   
    }
    
   
    /**
     * Charge la liste des enseignants membre d'un groupe.
     * @param idGroupe id du groupe
     * @return une liste d'enseignant.
     */
    private List<EnseignantDTO> chargeListeEnseignantGroupe(final Integer idGroupe) {
        final EnseignantsClasseGroupeQO rechercheQO = new EnseignantsClasseGroupeQO();
        rechercheQO.setIdGroupeClasse(idGroupe);
        final List<EnseignantDTO> liste = groupeClasseService.findEnseignantsGroupe(rechercheQO); 
        
       
        return liste;
    }
    
    /**
     * Charge la liste des enseignants de l'etablissement
     * @param idEtab
     * @return
     */
    private List<EnseignantDTO> chargeListeEnseignantEtab(final Integer idEtab) {
    	if (idEtab != null) {
    		List<EnseignantDTO> liste = etablissementService.findAllEnseignant(idEtab);
    		return liste;
    	} 
    	return null;
    }
   
  
    
    // *************************************************************************
    // Methode invoquees depuis le formulaire 
    // *************************************************************************
    
    
    
    
    /**
     * Sauvergarde la sequence pedagogique.
     *
    public void sauver() {
        try {
            final ResultatDTO<Integer> resultat = cycleService.saveCycle(form.getCycle());
            log.debug("Creation du cycle id = {0}", resultat.getValeurDTO());
            form.resetChampsObligatoire();
        } catch (final MetierException e) {
            form.setListeChampsObligatoire(MessageUtils.getAllCodeMessage(e.getConteneurMessage()));
            log.debug("{0}", e.getMessage());
        }
    }
 */
  
    
 

    
   
    /** 
     * Navigation vers les souc-ecran d'application d'un cycle.
     * @return la chaine de navigation.
     *
    public String appliquerCycle() {
        if (form.getCycle() != null)  {
            ContexteUtils.getContexteOutilControl().mettreAJourObjet(IDFORM, form);
            return NavigationUtils.navigationVersSousEcranAvecSauvegardeDonnees(
                    Outil.APPLY_CYCLE,
                    RechercheCycleControl.class.getName(),
                    form.getCycle());            
        } else {
            return null;
        }
    }
    */
    


    /**
     * Navigue vers l'ecran precedent.
     * 
     * @return ma chaine de navigation.
     */
    public String retour() {
        form.reset();
        return NavigationUtils.retourOutilPrecedentEnDepilant();
    }

  
    
    /**
     * Calcule  la liste des enseignants du groupe selectionné groupe.
     */
    public void afficheEnseignantGroupe(Integer idGrp) {
        if (idGrp == null ) {
            return;
        }
        List<EnseignantDTO> listMembres = chargeListeEnseignantGroupe(idGrp);
       
        
        List<String> listUid = new ArrayList<String>(listMembres.size());
        idMemberInbase = new HashSet<Integer>(listUid.size());
        
        for (EnseignantDTO membre : listMembres) {
			if (membre != null) {
				String uid = membre.getUid();
				log.debug("uid={0}", uid);
				listUid.add(uid);
				idMemberInbase.add(membre.getId());
			}
		}
        
        form.setMembresUid(listUid);
        form.setMembresChange(false);
        
        log.debug("nb membres = {0}", listMembres.size());

    }
    
    public void afficheALLEnseignant(Integer idEtab) {
        if (idEtab == null ) {
            return;
        }
        List<EnseignantDTO> listEnseignants = chargeListeEnseignantEtab(idEtab);
     
        if (listEnseignants != null) {
        	uid2enseignant = new HashMap<String, EnseignantDTO> (listEnseignants.size());
        	
        	for (EnseignantDTO ens : listEnseignants) {
        		String uid = ens.getUid();
        		uid2enseignant.put(uid, ens);
			
        	}
        } else {
        	uid2enseignant = null;
        }
        form.setEnseignants(listEnseignants);
        
        
        log.debug("nb enseignants = {0}", listEnseignants.size());
      
    }
    
    /**
     * initialise la procedure d'ajout de groupe
     */
    public void ajouter(){
    	log.debug("Init Ajout de groupe");
    	GroupeDTO groupe = new GroupeDTO();
    	form.setGroupEdited(groupe);
    	groupe.setGroupeCollaboratif(true);
    	groupe.setGroupeCollaboratifLocal(true);
    }

    
    
    
    public void saveGroup(){
    	
    	log.debug("SAVE GROUP");
    	ContexteUtilisateur context = ContexteUtils.getContexteUtilisateur();
    	UtilisateurDTO user = context.getUtilisateurDTO();
    	GroupeDTO groupe = form.getGroupEdited();
    	
    
    	
    	log.debug("new groupe nom = {0}", groupe.getIntitule());
    	log.debug("annee scolaire {0}" ,user.getAnneeScolaireDTO());
    	log.debug("etablissement {1} {0} ", user.getDesignationEtablissement(), user.getIdEtablissement());
    	
    	String code = user.getUserDTO().getUid();
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddhhmmss");
    	code += dateFormat.format(new Date());
    	
    	groupe.setCode(code);
    	
    	ResultatDTO<GroupeDTO> resultat = groupeClasseService.saveGroupeCollaboratifLocal(groupe, user);
    	
    	
    	initialiseListeGroupe(user);
    	
    	if (resultat != null) {
    		groupe = resultat.getValeurDTO();
	    	 if (groupe != null) {
	    		 log.debug("new groupe id = {0}", groupe.getId());
	    		 form.setSelectedIdGroup(groupe.getId());
	    		 rechercher();
	    	 }
    	}
    }
   
    public void rechercher (){
    	//String code = form.getSelectedCode();
    	Integer idGroup = form.getSelectedIdGroup();
    	
    	Integer idEtab  = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getIdEtablissement();
    	log.debug("Groupe selectionné : {0}", idGroup);
    	afficheEnseignantGroupe(idGroup);
    	afficheALLEnseignant(idEtab);
    }
    
    
    private Integer uid2id(String uid) {
    	EnseignantDTO ens =  uid2enseignant.get(uid);
    	if (ens != null) {
    		return ens.getId();
    	}
    	return null;
    }
    
    
    public String deleteGroupe() {
    	log.debug("Delete groupe") ;
    	form.setAlertSuppression(false);
    	Integer idGroupe = form.getSelectedIdGroup();
    	GroupeDTO groupe = id2groupe.get(idGroupe);
    	try {
			groupeClasseService.deleteEnseignantGroupe(groupe);
			initFormulaire();
		} catch (MetierException e) {
			// TODO Auto-generated catch block
		}
    	
    	return null;
    }
    
    public void saveModifMembre() throws MetierException {
    	List<String> membres =  form.getMembresUid();
    	
    	form.setAlertSuppression(false);
    	if (membres == null) {
    		return;
    	}
    	if ( membres.size() == 0 ){
			//le groupe sera vide
			log.debug("suppression de tout les membres du groupe");
			form.setAlertSuppression(true);
			return;
		}
    	
    	Integer idGroupe = form.getSelectedIdGroup();
    	
    	Set<Integer> ajout = new HashSet<Integer>();
    	Set<Integer> supprim = new HashSet<Integer>(idMemberInbase);

    	log.debug("les membres {0}", membres);
    	
    	
    	
    	
    	for (String uid : membres) {
    		Integer id = uid2id(uid);
    		if (idMemberInbase.contains(id) ) {
    			supprim.remove(id);
    			log.debug("membre a garder {0}", uid);
    		} else {
    			ajout.add(id);
    			log.debug("membre a ajouter {0}", uid);
    			
    		}
		}
    	
    	if (supprim.size() > 0 || ajout.size() > 0) {
	    	try {
				groupeClasseService.modifieEnseignantGroupe(id2groupe.get(idGroupe), supprim, ajout);
				afficheEnseignantGroupe(idGroupe);
			} catch (MetierException e) {
				//
			}
    	}
    	
    }
    
	public GroupeClasseService getGroupeClasseService() {
		return groupeClasseService;
	}

	public void setGroupeClasseService(GroupeClasseService groupeClasseService) {
		this.groupeClasseService = groupeClasseService;
	}

	public EtablissementService getEtablissementService() {
		return etablissementService;
	}

	public void setEtablissementService(EtablissementService etablissementService) {
		this.etablissementService = etablissementService;
	}
}
