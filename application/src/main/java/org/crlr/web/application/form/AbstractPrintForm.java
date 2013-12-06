/**
 * @author G-CG34-FRMP
 *
 */

package org.crlr.web.application.form;

import java.util.Date;

import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.TypeAffichage;
import org.crlr.report.Report;
import org.crlr.utils.DateUtils;
import org.crlr.web.contexte.utils.ContexteUtils;

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
    
}