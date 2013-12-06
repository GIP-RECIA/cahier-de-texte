/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConsoliderEmpForm.java,v 1.10 2010/05/20 14:42:31 ent_breyton Exp $
 */

package org.crlr.web.application.form.emploi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.utils.BooleanUtils;
import org.crlr.web.application.form.AbstractPopupForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.BarreSemaineDTO;
import org.crlr.web.dto.EnteteEmploiJoursDTO;
import org.crlr.web.dto.MoisDTO;

import com.google.gson.Gson;

/**
 * ConstituerEmpForm.
 *
 * @author $author$
 * @version $Revision: 1.10 $
 */
public class ConsoliderEmpForm extends AbstractPopupForm {
    /** Identidiant de sérialisation. */
    private static final long serialVersionUID = 4100040089281471925L;

   

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Map<String, EnteteEmploiJoursDTO> enteteEmploi;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private BarreSemaineDTO semaineSelectionnee;


    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private Profil profilEnCours;


    private EtablissementDTO etablissement;
    private String etablissementJSON;

    
    private List<DetailJourEmploiDTO> listeEmploiDeTemps;    
    private String listeEmploiDeTempsJSON;

    private DetailJourEmploiDTO detailJourEmploiDTOSel;
    private Integer detailJourEmploiDTOSelIndex;

    /** Liste des enseignements de l'enseignant. */
    private List<EnseignementDTO> listeEnseignements;

    /** La barre de semaine pour l'alternance des semaines. */
    private List<BarreSemaineDTO> listeBarreSemaine;

   
    
    

    /** Map des jours ouvrés accessible. */
    private Map<TypeJour, Boolean> jourOuvreAccessible;

    /** Affichage de la recherche par classe ou groupe. */
    private Boolean renderedRecherche;

    /** Affichage de la barre de recherche semaine. */
    private Boolean renderedBarreSemaine;

    /** liste des jours des vaqués. */
    private Set<Date> listeVaque;

    /** Vrai ou Faux alternance semaine. **/
    private Boolean vraiOuFauxAlternance;
        
        
    private List<MoisDTO> listeMois;
    
    /**
     * Constructeur.
     */
    public ConsoliderEmpForm() {
        reset();
    }

    /**
     * Méthode de réinitialisation des données du formulaire saisie par
     * l'utilisateur.
     */
    public void reset() {
        //Récupère l'utilisateur DTO
        final UtilisateurDTO utilisateurDTO =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        
        

        this.listeBarreSemaine = new ArrayList<BarreSemaineDTO>();
        this.semaineSelectionnee = new BarreSemaineDTO();

        // récupération des jours ouvrées
        this.jourOuvreAccessible = GenerateurDTO.generateMapJourOuvreFromDb(utilisateurDTO.getJoursOuvresEtablissement());

        this.resetGrilleEmploi();
        this.enteteEmploi = new HashMap<String, EnteteEmploiJoursDTO>();

        final String vacances = utilisateurDTO.getAnneeScolaireDTO().getPeriodeVacances();
        this.listeVaque = GenerateurDTO.generateSetPeriodeVancanceFromDb(vacances);

        //Profil
        this.profilEnCours = utilisateurDTO.getProfil();

        //Gestion des profils
        this.renderedRecherche = true;
        this.renderedBarreSemaine = false;
        if (Profil.ELEVE.equals(this.profilEnCours) ||
                Profil.PARENT.equals(this.profilEnCours)) {
            this.renderedRecherche = false;
            this.renderedBarreSemaine = true;
        }
    }

    /**
     * Réinitialisation des grilles.
     */
    public void resetGrilleEmploi() {
        
    }

   

    /**
     * Accesseur enteteEmploi.
     *
     * @return le enteteEmploi
     */
    public Map<String, EnteteEmploiJoursDTO> getEnteteEmploi() {
        return enteteEmploi;
    }

    /**
     * Mutateur de enteteEmploi.
     *
     * @param enteteEmploi le enteteEmploi à modifier.
     */
    public void setEnteteEmploi(Map<String, EnteteEmploiJoursDTO> enteteEmploi) {
        this.enteteEmploi = enteteEmploi;
    }

    
    /**
     * Accesseur listeBarreSemaine.
     *
     * @return le listeBarreSemaine
     */
    public List<BarreSemaineDTO> getListeBarreSemaine() {
        return listeBarreSemaine;
    }

    /**
     * Mutateur de listeBarreSemaine.
     *
     * @param listeBarreSemaine le listeBarreSemaine à modifier.
     */
    public void setListeBarreSemaine(List<BarreSemaineDTO> listeBarreSemaine) {
        this.listeBarreSemaine = listeBarreSemaine;
    }

    

    /**
     * Accesseur listeEnseignements.
     *
     * @return le listeEnseignements
     */
    public List<EnseignementDTO> getListeEnseignements() {
        return listeEnseignements;
    }

    /**
     * Mutateur de listeEnseignements.
     *
     * @param listeEnseignements le listeEnseignements à modifier.
     */
    public void setListeEnseignements(List<EnseignementDTO> listeEnseignements) {
        this.listeEnseignements = listeEnseignements;
    }

    /**
     * Accesseur jourOuvreAccessible.
     *
     * @return le jourOuvreAccessible
     */
    public Map<TypeJour, Boolean> getJourOuvreAccessible() {
        return jourOuvreAccessible;
    }

    /**
     * Mutateur de jourOuvreAccessible.
     *
     * @param jourOuvreAccessible le jourOuvreAccessible à modifier.
     */
    public void setJourOuvreAccessible(Map<TypeJour, Boolean> jourOuvreAccessible) {
        this.jourOuvreAccessible = jourOuvreAccessible;
    }

    /**
     * Accesseur renderedRecherche.
     *
     * @return le renderedRecherche
     */
    public Boolean getRenderedRecherche() {
        return renderedRecherche;
    }

    /**
     * Mutateur de renderedRecherche.
     *
     * @param renderedRecherche le renderedRecherche à modifier.
     */
    public void setRenderedRecherche(Boolean renderedRecherche) {
        this.renderedRecherche = renderedRecherche;
    }

    /**
     * Accesseur profilEnCours.
     *
     * @return le profilEnCours
     */
    public Profil getProfilEnCours() {
        return profilEnCours;
    }

    /**
     * Mutateur de profilEnCours.
     *
     * @param profilEnCours le profilEnCours à modifier.
     */
    public void setProfilEnCours(Profil profilEnCours) {
        this.profilEnCours = profilEnCours;
    }

    /**
     * Accesseur listeVaque.
     *
     * @return le listeVaque
     */
    public Set<Date> getListeVaque() {
        return listeVaque;
    }

    /**
     * Mutateur de listeVaque.
     *
     * @param listeVaque le listeVaque à modifier.
     */
    public void setListeVaque(Set<Date> listeVaque) {
        this.listeVaque = listeVaque;
    }

    
    /**
     * Accesseur renderedBarreSemaine.
     *
     * @return le renderedBarreSemaine
     */
    public Boolean getRenderedBarreSemaine() {
        return renderedBarreSemaine;
    }

    /**
     * Mutateur de renderedBarreSemaine.
     *
     * @param renderedBarreSemaine le renderedBarreSemaine à modifier.
     */
    public void setRenderedBarreSemaine(Boolean renderedBarreSemaine) {
        this.renderedBarreSemaine = renderedBarreSemaine;
    }

  

    /**
     * Retourne une chaine du profil en cours.
     * @return chaine
     *
     */
    public String getProfilEnCoursChaine() {
        final String chaine;
        if (profilEnCours != null) {
            chaine = this.profilEnCours.toString();
        } else {
            chaine = "";
        }
        return chaine;
    }
    
    /**
     * Retourne une chaine du type de semaine.
     * @return Character avec le type de semaine
     */
    public Character getTypeJourEmploiChaine(){
        final Character result;
        final BarreSemaineDTO semaine = this.getSemaineSelectionnee();
        final Character typeSemaine = semaine.getTypeJourEmploi().getValeur();
        if (BooleanUtils.isTrue(this.vraiOuFauxAlternance) && semaine != null && typeSemaine != null){
            result = typeSemaine;
        } else {
            result = ' ';
        }
        return result;
    }

    /**
     * Accesseur vraiOuFauxAlternance.
     * @return le vraiOuFauxAlternance
     */
    public Boolean getVraiOuFauxAlternance() {
        return vraiOuFauxAlternance;
    }

    /**
     * Mutateur de vraiOuFauxAlternance.
     * @param vraiOuFauxAlternance le vraiOuFauxAlternance à modifier.
     */
    public void setVraiOuFauxAlternance(Boolean vraiOuFauxAlternance) {
        this.vraiOuFauxAlternance = vraiOuFauxAlternance;
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
		
		this.setListeEmploiDeTempsJSON(ConstituerEmpForm.createListeEmploiJSON(listeEmploiDeTemps));
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
	 * @return the detailJourEmploiDTOSel
	 */
	public DetailJourEmploiDTO getDetailJourEmploiDTOSel() {
		return detailJourEmploiDTOSel;
	}

	/**
	 * @param detailJourEmploiDTOSel the detailJourEmploiDTOSel to set
	 */
	public void setDetailJourEmploiDTOSel(DetailJourEmploiDTO detailJourEmploiDTOSel) {
		this.detailJourEmploiDTOSel = detailJourEmploiDTOSel;
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
     * @return the listeMois
     */
    public List<MoisDTO> getListeMois() {
        return listeMois;
    }

    /**
     * @param listeMois the listeMois to set
     */
    public void setListeMois(List<MoisDTO> listeMois) {
        this.listeMois = listeMois;
    }

  
    /**
     * @return the semaineSelectionnee
     */
    public BarreSemaineDTO getSemaineSelectionnee() {
        return semaineSelectionnee;
    }

    /**
     * @param semaineSelectionnee the semaineSelectionnee to set
     */
    public void setSemaineSelectionnee(BarreSemaineDTO semaineSelectionnee) {
        this.semaineSelectionnee = semaineSelectionnee;
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
}
