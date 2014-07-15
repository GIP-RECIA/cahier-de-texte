/**
 * @author G-CG34-FRMP
 *
 */

package org.crlr.web.application.form;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.TypeAffichage;
import org.crlr.report.Report;
import org.crlr.utils.DateUtils;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.TypePreferencesEtab;

/**
 * 
 *
 */
public abstract class AbstractPrintForm extends AbstractPopupForm {

    /**
     * 
     */
    private static final long serialVersionUID = 2838623173087411703L;
    
    //Profile d'utilisateur connécté
    private Profil profil;
   
    private Set<TypePreferencesEtab> preferencesEtabs;
   
    /** La date du jour. */
    private Date dateCourante;

    /** Date de début. */
    private Date dateDebut;

    /** Date de fin. */
    private Date dateFin;
  
    
    

    /** Rapport pdf. */
    private Report report;
    
    



    

    /** Boutons radio affichage simple ou détaillé. */
    private TypeAffichage typeAffichageSelectionne;
    

    /** avec saut de page. */
    private Boolean vraiOuFauxSautPage;
    
    
    /** liste des années scolaires. */
    private List<AnneeScolaireDTO> listeAnneeScolaire; 
    
    /** Id de l'annee scolaire selectionnee dans la liste des archives. */
    private Integer idAnneeScolaire;
    
    /** id de l'etablissement pour les archives */
    private Integer idEtablissement;
    
    private List<EtablissementDTO> listeEtablissement;
    
    /**
     * reset.
     */
    public void reset() {
       
        vraiOuFauxSautPage = false;
        
        typeAffichageSelectionne = TypeAffichage.SIMPLE;
        
        dateDebut = null;
        dateFin = null;
        report = null;
        


        //On initialise la date du jour.
        dateCourante = DateUtils.getAujourdhui();
        
        //On initialise la date de début et la date de fin du formulaire de recherche.
        final AnneeScolaireDTO anneeScolaire = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO().getAnneeScolaireDTO();
        this.dateDebut = anneeScolaire.getDateRentree();
        this.dateFin = dateCourante;
        
        //Si l'année scolaire n'est pas encore commencée ou est terminé
        if (dateFin.before(dateDebut) || 
                anneeScolaire.getDateSortie().before(dateFin)    ) {
            this.dateFin = anneeScolaire.getDateSortie();
        }
        
        
    }
    
 

   
    

    /**
     * @return the profile
     */
    public Profil getProfil() {
        return profil;
    }

    /**
     * @param profile the profile to set
     */
    public void setProfil(Profil profile) {
        this.profil = profile;
    }
    
    /**
     * @return true pour affichage parent / éléve.
     */
    public boolean getAffichageParentEleve() {
        return Profil.ELEVE.equals(profil) || Profil.PARENT.equals(profil);
    }

    


    /**
     * @return the typeAffichageSelectionne
     */
    public TypeAffichage getTypeAffichageSelectionne() {
        return typeAffichageSelectionne;
    }

    /**
     * @param typeAffichageSelectionne the typeAffichageSelectionne to set
     */
    public void setTypeAffichageSelectionne(TypeAffichage typeAffichageSelectionne) {
        this.typeAffichageSelectionne = typeAffichageSelectionne;
    }

    /**
     * @return the vraiOuFauxSautPage
     */
    public Boolean getVraiOuFauxSautPage() {
        return vraiOuFauxSautPage;
    }

    /**
     * @param vraiOuFauxSautPage the vraiOuFauxSautPage to set
     */
    public void setVraiOuFauxSautPage(Boolean vraiOuFauxSautPage) {
        this.vraiOuFauxSautPage = vraiOuFauxSautPage;
    }



    /**
     * @return the dateCourante
     */
    public Date getDateCourante() {
        return dateCourante;
    }



    /**
     * @param dateCourante the dateCourante to set
     */
    public void setDateCourante(Date dateCourante) {
        this.dateCourante = dateCourante;
    }



    /**
     * @return the dateDebut
     */
    public Date getDateDebut() {
        return dateDebut;
    }



    /**
     * @param dateDebut the dateDebut to set
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }



    /**
     * @return the dateFin
     */
    public Date getDateFin() {
        return dateFin;
    }



    /**
     * @param dateFin the dateFin to set
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }



    /**
     * @return the report
     */
    public Report getReport() {
        return report;
    }



    /**
     * @param report the report to set
     */
    public void setReport(Report report) {
        this.report = report;
    }






	public List<AnneeScolaireDTO> getListeAnneeScolaire() {
		return listeAnneeScolaire;
	}






	public void setListeAnneeScolaire(List<AnneeScolaireDTO> listeAnneeScolaire) {
		this.listeAnneeScolaire = listeAnneeScolaire;
	}






	public Integer getIdAnneeScolaire() {
		return idAnneeScolaire;
	}






	public void setIdAnneeScolaire(Integer idAnneeScolaire) {
		this.idAnneeScolaire = idAnneeScolaire;
	}
	
	 /**
     * Retourne l'exercice correspondant à l'ID annee scolaire selectionnee.
     * @return exercice
     */
    public String getExercice() {
        if (this.idAnneeScolaire == null) {
            return null;
        }
        for (final AnneeScolaireDTO annee : listeAnneeScolaire) {
            if (annee.getId().equals(idAnneeScolaire)) {
                return annee.getExercice();
            }
        }
        return null;
    }






	public Integer getIdEtablissement() {
		return idEtablissement;
	}






	public void setIdEtablissement(Integer idEtablissement) {
		this.idEtablissement = idEtablissement;
	}






	public List<EtablissementDTO> getListeEtablissement() {
		return listeEtablissement;
	}






	public void setListeEtablissement(List<EtablissementDTO> listeEtablissement) {
		this.listeEtablissement = listeEtablissement;
	}


	public boolean isSeancePartage (){
		return preferencesEtabs != null && preferencesEtabs.contains(TypePreferencesEtab.SeancePartage);
	}


	public Set<TypePreferencesEtab> getPreferencesEtabs() {
		return preferencesEtabs;
	}



	public void setPreferencesEtabs(Set<TypePreferencesEtab> preferencesEtabs) {
		this.preferencesEtabs = preferencesEtabs;
	}
	
}