/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConstituerEmpForm.java,v 1.3 2010/05/20 08:24:50 ent_breyton Exp $
 */

package org.crlr.web.application.form.emploi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.PeriodeEmploiDTO;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.dto.GrilleHoraireDTO;
import org.crlr.web.dto.TypeCouleur;
import org.crlr.web.dto.TypeSemaine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * ConstituerEmpForm.
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ConstituerEmpForm extends AbstractForm {
    
    /**
     * Serial uid.
     */
    private static final long serialVersionUID = -9119316429383300695L;
    
    /** Liste des classes de l'enseignant. */
    private List<GroupesClassesDTO> listeClasses;

    /** Liste des groupes de l'enseignant. */
    private List<GroupesClassesDTO> listeGroupes;
    
    /** Liste des enseignements de l'enseignant. */
    private List<EnseignementDTO> listeEnseignements;
    
    
    /** La grille horaire ; pas modifiable. */
    private List<GrilleHoraireDTO> horairesCours;
    private String horairesCoursJSON;
           
    private List<DetailJourEmploiDTO> listeEmploiDeTemps;    
    private String listeEmploiDeTempsJSON;
    
    private DetailJourEmploiDTO detailJourEmploiDTOSel;
    private Integer detailJourEmploiDTOSelIndex;
    
    private List<Integer> listeEmploiDeTempsASupprimer;    
    
    /** Alternance semaine 1 et 2. **/
    private Boolean vraiOuFauxAlternanceSemaine;  
    
    //gestion des onglets
    /** id de l'onglet sélectionné. */
    
    private String ongletSelectionneStr;
    
    /** Permet de savoir si des modification on été faites, afin de proposer la sauvegarde
     *  lors du changement d'onglet.
     *  **/
    private Boolean vraiOuFauxModification;   
    private String texteAide;    
    private boolean donneesModifiees;
    private String enseignementPrecedent;    
        
        
    
    /** Liste des couleur possibles dans le popup d'emploi de temps. */
    private List<TypeCouleur> listeCouleur = new ArrayList<TypeCouleur>();
    
    
    private List<PeriodeEmploiDTO> listePeriode;
    
    //Période sélectionnée dans l'écran principale
    private PeriodeEmploiDTO periodeSelectionnee;
    
    //Créer une période
    private Date creerPeriodeDate;
    private Boolean initPeriodeAvecCopie;
    private PeriodeEmploiDTO periodeACopie;
    
    private EtablissementDTO etablissement;
    private String etablissementJSON;
    
    
    /**
     * Constructeur.
     */
    public ConstituerEmpForm() {        
        reset();
    }

    /**
   	 * @return the horairesCoursJSON
   	 */
   	public String getHorairesCoursJSON() {
   		return horairesCoursJSON;
   	}

   	/**
   	 * @param horairesCoursJSON the horairesCoursJSON to set
   	 */
   	public void setHorairesCoursJSON(String horairesCoursJSON) {
   		this.horairesCoursJSON = horairesCoursJSON;
   	}
   	
    /**
     * Méthode de réinitialisation des données du formulaire saisie par
     * l'utilisateur.
     */
    public void reset() {
        this.listeClasses = new ArrayList<GroupesClassesDTO>();
        this.listeGroupes = new ArrayList<GroupesClassesDTO>();
        this.listeEnseignements = new ArrayList<EnseignementDTO>();
        this.horairesCours = new ArrayList<GrilleHoraireDTO>();     
        this.listeEmploiDeTempsASupprimer = new ArrayList<Integer>();
        
        this.listeCouleur = GenerateurDTO.generateBarreCouleur();
        
        this.vraiOuFauxAlternanceSemaine = null;
        
        //Il faut que cette chaine correspond à un nom valide d'un onglet, sinon on a des faux événement onChange qui se déclenchent
        this.ongletSelectionneStr = "ongletSemaine1";
        
        this.detailJourEmploiDTOSel = new DetailJourEmploiDTO();
        
        this.resetGrilleEmploi();        
    }
    
    /**
     * Réinitialisation des grilles.
     */
    public void resetGrilleEmploi() {
        
        this.listeEmploiDeTemps = new ArrayList<DetailJourEmploiDTO>();
        this.enseignementPrecedent = null;
    }

     

    /**
     * Accesseur detailJourEmploiDTOSel.
     * @return le detailJourEmploiDTOSel
     */
    public DetailJourEmploiDTO getDetailJourEmploiDTOSel() {
        return detailJourEmploiDTOSel;
    }

    /**
     * Mutateur de detailJourEmploiDTOSel.
     * @param detailJourEmploiDTOSel le detailJourEmploiDTOSel à modifier.
     */
    public void setDetailJourEmploiDTOSel(DetailJourEmploiDTO detailJourEmploiDTOSel) {
        this.detailJourEmploiDTOSel = detailJourEmploiDTOSel;
    }

    /**
     * Accesseur listeClasses.
     * @return le listeClasses
     */
    public List<GroupesClassesDTO> getListeClasses() {
        return listeClasses;
    }

    /**
     * Mutateur de listeClasses.
     * @param listeClasses le listeClasses à modifier.
     */
    public void setListeClasses(List<GroupesClassesDTO> listeClasses) {
        this.listeClasses = listeClasses;
    }

    /**
     * Accesseur listeGroupes.
     * @return le listeGroupes
     */
    public List<GroupesClassesDTO> getListeGroupes() {
        return listeGroupes;
    }

    /**
     * Mutateur de listeGroupes.
     * @param listeGroupes le listeGroupes à modifier.
     */
    public void setListeGroupes(List<GroupesClassesDTO> listeGroupes) {
        this.listeGroupes = listeGroupes;
    }

    /**
     * Accesseur listeEnseignements.
     * @return le listeEnseignements
     */
    public List<EnseignementDTO> getListeEnseignements() {
        return listeEnseignements;
    }

    /**
     * Mutateur de listeEnseignements.
     * @param listeEnseignements le listeEnseignements à modifier.
     */
    public void setListeEnseignements(List<EnseignementDTO> listeEnseignements) {
        this.listeEnseignements = listeEnseignements;
    }

    

    /**
     * Accesseur horairesCours.
     * @return le horairesCours
     */
    public List<GrilleHoraireDTO> getHorairesCours() {
        return Collections.unmodifiableList(horairesCours);
    }

    /**
     * Mutateur de horairesCours.
     * @param horairesCours le horairesCours à modifier.
     */
    public void setHorairesCours(List<GrilleHoraireDTO> horairesCours) {
        this.horairesCours = horairesCours;
        
        Gson gson = new Gson();
        String json = gson.toJson(horairesCours);
        this.horairesCoursJSON = json;
    }

    /**
     * Accesseur vraiOuFauxAlternanceSemaine.
     * @return le vraiOuFauxAlternanceSemaine
     */
    public Boolean getVraiOuFauxAlternanceSemaine() {
        return vraiOuFauxAlternanceSemaine;
    }

    /**
     * Mutateur de vraiOuFauxAlternanceSemaine.
     * @param vraiOuFauxAlternanceSemaine le vraiOuFauxAlternanceSemaine à modifier.
     */
    public void setVraiOuFauxAlternanceSemaine(Boolean vraiOuFauxAlternanceSemaine) {
        this.vraiOuFauxAlternanceSemaine = vraiOuFauxAlternanceSemaine;
    }

    

    /**
     * Accesseur ongletSelectionne.
     * @return le ongletSelectionne
     */
    public TypeSemaine getOngletSelectionne() {
        if ("ongletSemaine2".equals(ongletSelectionneStr)) {
            return TypeSemaine.IMPAIR;
        } else {
            return TypeSemaine.PAIR;
        }
        
    }

    

    /**
     * Accesseur vraiOuFauxModification.
     * @return le vraiOuFauxModification
     */
    public Boolean getVraiOuFauxModification() {
        return vraiOuFauxModification;
    }

    /**
     * Mutateur de vraiOuFauxModification.
     * @param vraiOuFauxModification le vraiOuFauxModification à modifier.
     */
    public void setVraiOuFauxModification(Boolean vraiOuFauxModification) {
        this.vraiOuFauxModification = vraiOuFauxModification;
    }

   

    /**
     * Accesseur enseignementPrecedent.
     * @return le enseignementPrecedent.
     */
    public String getEnseignementPrecedent() {
        return enseignementPrecedent;
    }

    /**
     * Mutateur enseignementPrecedent.
     * @param enseignementPrecedent le enseignementPrecedent à modifier.
     */
    public void setEnseignementPrecedent(String enseignementPrecedent) {
        this.enseignementPrecedent = enseignementPrecedent;
    }

    /**
     * Accesseur listeCouleur.
     * @return le listeCouleur.
     */
    public List<TypeCouleur> getListeCouleur() {
        return listeCouleur;
    }

    /**
     * Mutateur listeCouleur.
     * @param listeCouleur le listeCouleur à modifier.
     */
    public void setListeCouleur(List<TypeCouleur> listeCouleur) {
        this.listeCouleur = listeCouleur;
    }

    /**
     * Accesseur de texteAide.
     * @return le texteAide
     */
    public String getTexteAide() {
        return texteAide;
    }

    /**
     * Mutateur de texteAide.
     * @param texteAide le texteAide à modifier.
     */
    public void setTexteAide(String texteAide) {
        this.texteAide = texteAide;
    }

	/**
	 * @return the listeJourJSON
	 */
	public String getListeJourJSON() {
		return listeEmploiDeTempsJSON;
	}

	/**
	 * @param listeJourJSON the listeJourJSON to set
	 */
	public void setListeJourJSON(String listeJourJSON) {
		this.listeEmploiDeTempsJSON = listeJourJSON;
	}

	/**
	 * @return the detailJourEmploiDTOSelIndex
	 */
	public Integer getDetailJourEmploiDTOSelIndex() {
		return detailJourEmploiDTOSelIndex;
	}

	/**
	 * @param detailJourEmploiDTOSelIndex the detailJourEmploiDTOSelIndex to set
	 */
	public void setDetailJourEmploiDTOSelIndex(Integer detailJourEmploiDTOSelIndex) {
		this.detailJourEmploiDTOSelIndex = detailJourEmploiDTOSelIndex;
	}

	/**
	 * @return the donneesModifiees
	 */
	public boolean isDonneesModifiees() {
		return donneesModifiees;
	}

	/**
	 * @param donneesModifiees the donneesModifiees to set
	 */
	public void setDonneesModifiees(boolean donneesModifiees) {
		this.donneesModifiees = donneesModifiees;
	}



	/**
	 * @return the creerPeriodeDate
	 */
	public Date getCreerPeriodeDate() {
		return creerPeriodeDate;
	}

	/**
	 * @param creerPeriodeDate the creerPeriodeDate to set
	 */
	public void setCreerPeriodeDate(Date creerPeriodeDate) {
		this.creerPeriodeDate = creerPeriodeDate;
	}

	

	/**
	 * @return the initPeriodeAvecCopie
	 */
	public Boolean getInitPeriodeAvecCopie() {
		return initPeriodeAvecCopie;
	}

	/**
	 * @param initPeriodeAvecCopie the initPeriodeAvecCopie to set
	 */
	public void setInitPeriodeAvecCopie(Boolean initPeriodeAvecCopie) {
		this.initPeriodeAvecCopie = initPeriodeAvecCopie;
	}

	/**
	 * @return the listePeriode
	 */
	public List<PeriodeEmploiDTO> getListePeriode() {
		return listePeriode;
	}

	/**
	 * @param listePeriode the listePeriode to set
	 */
	public void setListePeriode(List<PeriodeEmploiDTO> listePeriode) {
		this.listePeriode = listePeriode;
	}

	/**
	 * @return the periodeSelectionnee
	 */
	public PeriodeEmploiDTO getPeriodeSelectionnee() {
		return periodeSelectionnee;
	}

	/**
	 * @param periodeSelectionnee the periodeSelectionnee to set
	 */
	public void setPeriodeSelectionnee(PeriodeEmploiDTO periodeSelectionnee) {
		this.periodeSelectionnee = periodeSelectionnee;
	}


	/**
	 * @return the periodeACopie
	 */
	public PeriodeEmploiDTO getPeriodeACopie() {
		return periodeACopie;
	}

	/**
	 * @param periodeACopie the periodeACopie to set
	 */
	public void setPeriodeACopie(PeriodeEmploiDTO periodeACopie) {
		this.periodeACopie = periodeACopie;
	}

	/**
	 * @return the listeEmploiDeTemps
	 */
	public List<DetailJourEmploiDTO> getListeEmploiDeTemps() {
		return listeEmploiDeTemps;
	}

	/**
	 * @param listeEmploiDeTemps the listeEmploiDeTemps to set
	 */
	public void setListeEmploiDeTemps(List<DetailJourEmploiDTO> listeEmploiDeTemps) {
		this.listeEmploiDeTemps = listeEmploiDeTemps;
				
		this.setListeEmploiDeTempsJSON(createListeEmploiJSON(listeEmploiDeTemps));		
	}
	
	
	
	
	
	/**
	 * @param listeEmploiDeTemps la liste
	 * @return le json
	 */
	public static String createListeEmploiJSON(List<DetailJourEmploiDTO> listeEmploiDeTemps) {
		Gson gson = new GsonBuilder()
	     .registerTypeAdapter(TypeCouleur.class, new JsonSerializer<TypeCouleur>() {
	    	 public JsonElement serialize(TypeCouleur couleur, Type typeOfId, JsonSerializationContext context) {
	    		 JsonArray array = new JsonArray();
	    		 array.add( new JsonPrimitive(couleur.getId()));
	    		 array.add( new JsonPrimitive(couleur.getName()));
	    		 return array;
	    	   }
	     }).registerTypeAdapter(TypeJour.class, new JsonSerializer<TypeJour>() {
	    	 public JsonElement serialize(TypeJour jour, Type typeOfId, JsonSerializationContext context) {
	    		 JsonArray array = new JsonArray();
	    		 array.add( new JsonPrimitive(jour.getJourNum()));
	    		 array.add( new JsonPrimitive(jour.name()));
	    		 return array;
	    	   }
	     }).create();
	     
		//Gson gson = new Gson();
       JsonElement json = gson.toJsonTree(listeEmploiDeTemps);
       
       //Ajoute case déscription 
       int i = 0;
       for(JsonElement elem : json.getAsJsonArray()) {
            elem.getAsJsonObject().add(
                    "caseDescription",
                    new JsonPrimitive(listeEmploiDeTemps.get(i)
                            .getCaseDescription()));
           i++;
       }
       String jsonStr = gson.toJson(json);
       return jsonStr;
	}

	/**
	 * @return the listeEmploiDeTempsJSON
	 */
	public String getListeEmploiDeTempsJSON() {
		return listeEmploiDeTempsJSON;
	}

	/**
	 * @param listeEmploiDeTempsJSON the listeEmploiDeTempsJSON to set
	 */
	public void setListeEmploiDeTempsJSON(String listeEmploiDeTempsJSON) {
		this.listeEmploiDeTempsJSON = listeEmploiDeTempsJSON;
	}

	/**
	 * @return the listeEmploiDeTempsASupprimer
	 */
	public List<Integer> getListeEmploiDeTempsASupprimer() {
		return listeEmploiDeTempsASupprimer;
	}

	/**
	 * @param listeEmploiDeTempsASupprimer the listeEmploiDeTempsASupprimer to set
	 */
	public void setListeEmploiDeTempsASupprimer(
			List<Integer> listeEmploiDeTempsASupprimer) {
		this.listeEmploiDeTempsASupprimer = listeEmploiDeTempsASupprimer;
	}

    /**
     * @return the etablissement
     */
    public EtablissementDTO getEtablissement() {
        return etablissement;
    }

    /**
     * @param etablissement the etablissement to set
     */
    public void setEtablissement(EtablissementDTO etablissement) {
        this.etablissement = etablissement;
        
        Gson gson = new Gson();
        String json = gson.toJson(etablissement);
        setEtablissementJSON(json);
    }

    /**
     * @return the etablissementJSON
     */
    public String getEtablissementJSON() {
        return etablissementJSON;
    }

    /**
     * @param etablissementJSON the etablissementJSON to set
     */
    public void setEtablissementJSON(String etablissementJSON) {
        this.etablissementJSON = etablissementJSON;
    }

    /**
     * @return the ongletSelectionneStr
     */
    public String getOngletSelectionneStr() {
        return ongletSelectionneStr;
    }

    /**
     * @param ongletSelectionneStr the ongletSelectionneStr to set
     */
    public void setOngletSelectionneStr(String ongletSelectionneStr) {
        this.ongletSelectionneStr = ongletSelectionneStr;
    }
	
	
	
	


    
    
}
